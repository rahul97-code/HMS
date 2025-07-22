package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms1.ipd.database.IPDDBConnection;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class DoctorWiseSummaryReportPDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> ipdID = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	Vector<String> insurance = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;

	String mainDir = "", str = "", DoctorName = "Dr M Kumar";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new DoctorWiseSummaryReportPDF("2014-03-24", "2014-04-24",
					"Dr. Karan Sobti");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DoctorWiseSummaryReportPDF(String dateFrom, String dateTo,
			String doctorName) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		DoctorName = doctorName;
		readFile();
		str = "DoctorWise " + doctorName + " "
				+ DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
		getAllinsurance();
		reportCategory.add("OPD Services");
		reportCategory.add("Exam Services");
		reportCategory.add("Misc Services");
		reportCategory.add("Indoor Services");

		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4);
		document.setMargins(0, 0, 50, 80);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 13f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = DoctorWiseSummaryReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = DoctorWiseSummaryReportPDF.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(5);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		table.addCell(new Phrase("Doctor Report Card", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
				.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));

		table.addCell(new Phrase("", font3));
		table.addCell(new Phrase("Doctr Name : " + DoctorName, font5));

		for (int j = 0; j < insurance.size(); j++) {

			table.addCell(new Phrase("Insurance Type : " + insurance.get(j),
					font3));
			PdfPTable opdTable = new PdfPTable(opdTablCellWidth);

			opdTable.addCell(new Phrase("Services", font3));
			opdTable.addCell(new Phrase("No.", font3));

			PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
			amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
			opdTable.addCell(amount);

			System.out.println(insurance.get(j));
			table.addCell(opdTable);
			for (int i = 0; i < reportCategory.size(); i++) {

				PdfPCell header = new PdfPCell(new Phrase(""
						+ reportCategory.get(i), font3));
				header.setBorder(Rectangle.NO_BORDER);
				header.setPaddingBottom(3);
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);

				table.addCell(header);
				switch (i) {
				case 0:
					table.addCell(opdAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 1:
					table.addCell(examAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 2:
					table.addCell(miscAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 3:
					table.addCell(indoorAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 4:

					break;
				case 5:

					break;
				case 6:

					break;

				default:
					break;
				}

			}

			totalAmount = totalAmount - cancelledAmount;
			PdfPCell footer2 = new PdfPCell(new Phrase("Total Amount : `"
					+ totalAmount, font));
			footer2.setBorder(Rectangle.NO_BORDER);
			footer2.setPaddingBottom(5);
			footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
			// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

			table.addCell(footer2);
			totalAmount = 0;
		}
		document.add(table);
		document.close();

		new OpenFile(RESULT);
		
	}

	public PdfPTable opdAmount(String dateFrom, String dateTo,String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			OPDDBConnection db = new OPDDBConnection();

			double totalServiceCharge = 0;
			ResultSet rs = db.retrieveAllDataDoctor(dateFrom, dateTo,insurance,
					DoctorName);
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString());
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalServiceCharge + totalAmount;
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public PdfPTable miscAmount(String dateFrom, String dateTo,String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
//			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
//
//			// opdTable.addCell(new Phrase("Services", font3));
//			// opdTable.addCell(new Phrase("No.", font3));
//			//
//			// opdTable.addCell(new Phrase("Amount", font3));
//
//			double totalServiceCharge = 0;
//			int totalNo = 0;
//			ResultSet rs = db.retrieveAllDataDoctorWise(dateFrom, dateTo,
//					DoctorName);
//
//			while (rs.next()) {
//				totalServiceCharge = Double.parseDouble(rs.getObject(3)
//						.toString()) + totalServiceCharge;
//				totalNo++;
//			}
//
//			if (totalNo > 0) {
//				opdTable.addCell(new Phrase("MISC", font3));
//				opdTable.addCell(new Phrase("" + totalNo, font3));
//				PdfPCell amount = new PdfPCell(new Phrase(""
//						+ totalServiceCharge, font3));
//				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				opdTable.addCell(amount);
//				totalAmount = totalServiceCharge + totalAmount;
//			}
//			db.closeConnection();

			MiscDBConnection miscDBConnection = new MiscDBConnection();
			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs= miscDBConnection.retrieveAllDataDoctorWise(dateFrom, dateTo,
					DoctorName,insurance);
			
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString());
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalServiceCharge + totalAmount;
			}

			miscDBConnection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public PdfPTable examAmount(String dateFrom, String dateTo,String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {

			ExamDBConnection db = new ExamDBConnection();

			double totalServiceCharge = 0;

			ResultSet rs = db.retrieveAllDataDoctor(dateFrom, dateTo,
					DoctorName,insurance);
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString());
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalServiceCharge + totalAmount;
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public PdfPTable indoorAmount(String dateFrom, String dateTo,String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		ipdID.clear();
		try {
			double totalServiceCharge = 0;
			int totalNo = 0;
			IPDDBConnection db3 = new IPDDBConnection();

			ResultSet rs3 = db3.retrieveAllDataDoctorIPD1(dateFrom, dateTo,
					DoctorName,insurance);

			while (rs3.next()) {
				try {
					opdTable.addCell(new Phrase("IPD", font3));
					opdTable.addCell(new Phrase("" + rs3.getObject(1).toString(), font3));
					
					totalServiceCharge=Double.parseDouble(rs3.getObject(2).toString());
					PdfPCell amount = new PdfPCell(new Phrase(""
							+ totalServiceCharge, font3));
					amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
					opdTable.addCell(amount);
					
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			totalAmount = totalServiceCharge + totalAmount;
			rs3 = db3.retrieveAllDataDoctorIPDAdvance(dateFrom, dateTo,
					DoctorName,insurance);

			while (rs3.next()) {
				try {
					opdTable.addCell(new Phrase("Advance Payments", font3));
					opdTable.addCell(new Phrase(""
							+ rs3.getObject(1).toString(), font3));
					PdfPCell amount = new PdfPCell(new Phrase(""
							+ rs3.getObject(2), font3));
					amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
					opdTable.addCell(amount);
					totalAmount = Double.parseDouble("0"
							+ rs3.getObject(2).toString())
							+ totalAmount;
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			db3.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public void makeDirectory(String name) {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/DailySlip");
			if (!dir.exists())
				dir.mkdirs();

		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new File("DailySlip").mkdir();
		serverFile = mainDir + "/HMS/DailySlip/" + name + ".pdf";
		RESULT = "DailySlip/" + name + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source));

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}

	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
	public void getAllinsurance() {
		insurance.add("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insurance.add(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	
}