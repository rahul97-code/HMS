package hms.reports.gui;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.cancellation.gui.CancelledDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.HeaderAndFooter;
import hms.patient.slippdf.OpenFile;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;

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

public class InsuranceSummaryReportPDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
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

	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> cancelled = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();

	Vector<String> insurance = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;

	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new InsuranceSummaryReportPDF("2014-08-23", "2014-08-23");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InsuranceSummaryReportPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		readFile();
		str = "SummeryReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);

		getAllinsurance();
		reportCategory.add("OPD Services");
		reportCategory.add("CARD Charges");
		reportCategory.add("Exam Services");
		reportCategory.add("Misc Services");
		reportCategory.add("Indoor Services");
		reportCategory.add("Cancelled Amount");

		cancelled.add("OPD");
		cancelled.add("EXAM");
		cancelled.add("CARDS");
		cancelled.add("MISC");
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

		java.net.URL imgURL = InsuranceSummaryReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = InsuranceSummaryReportPDF.class
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
						"Telephone No. : 0171-2690009, Mobile No. : 09034056794",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		table.addCell(new Phrase("Daily Cash Collection Report ", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
				.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));
		for (int j = 0; j < insurance.size(); j++) {

			table.addCell(new Phrase("Insurance Type : " + insurance.get(j),
					font3));
			System.out.println("Insurance Type : " + insurance.get(j));
			PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
			OPDDBConnection db = new OPDDBConnection();

			opdTable.addCell(new Phrase("Services", font3));
			opdTable.addCell(new Phrase("No.", font3));

			PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
			amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
			opdTable.addCell(amount);

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
					table.addCell(cardAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 2:
					table.addCell(examAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 3:
					table.addCell(miscAmount(dateFrom, dateTo, insurance.get(j)));
					break;
				case 4:
					table.addCell(indoorAmount(dateFrom, dateTo,
							insurance.get(j)));
					break;
				case 5:
					table.addCell(cancelledAmount(dateFrom, dateTo,
							insurance.get(j)));
					break;
				case 6:

					break;
				case 7:

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

	public PdfPTable opdAmount(String dateFrom, String dateTo, String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			OPDDBConnection db = new OPDDBConnection();

			ResultSet rs = db.retrieveAllDataCategory(dateFrom, dateTo,
					insurance);

			double totalServiceCharge = 0;
			while (rs.next()) {

				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));

				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString());
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

	public PdfPTable cardAmount(String dateFrom, String dateTo,
			String insuranceType) {
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAllDataCard(dateFrom, dateTo,
					insuranceType);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(2)
						.toString()) ;
				opdTable.addCell(new Phrase("CARDS", font3));
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

	public PdfPTable miscAmount(String dateFrom, String dateTo, String insurance) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs;

			MiscDBConnection miscDBConnection = new MiscDBConnection();

			rs = miscDBConnection.retrieveAllDataCatWiseInsurance(insurance,
					dateFrom, dateTo);
			totalServiceCharge = 0;
			totalNo = 0;
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;
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

	public PdfPTable examAmount(String dateFrom, String dateTo,
			String insuranceSTR) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			int k = 1;
			ExamDBConnection db = new ExamDBConnection();

			double totalServiceCharge = 0;
			int totalNo = 0;

			ResultSet rs = db.retrieveAllDataCatWiseInsurance(insuranceSTR,
					dateFrom, dateTo);
			while (rs.next()) {

				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public PdfPTable indoorAmount(String dateFrom, String dateTo,
			String insuranceSTR) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			double totalServiceCharge = 0;
			int totalNo = 0;
			IPDPaymentsDBConnection paymentsDBConnection = new IPDPaymentsDBConnection();
			ResultSet resultSet = paymentsDBConnection
					.retrieveAllDataAdvanceAmountInsurance(insuranceSTR,
							dateFrom, dateTo);

			while (resultSet.next()) {
				totalServiceCharge = Double.parseDouble(resultSet.getObject(2)
						.toString()) ;
				opdTable.addCell(new Phrase("Advance Payment", font3));
				opdTable.addCell(new Phrase(""
						+ resultSet.getObject(1).toString(), font3));
				PdfPCell amount = new PdfPCell(new Phrase(""
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalServiceCharge + totalAmount;
			}

			totalServiceCharge = 0;
			totalNo = 0;
			resultSet = paymentsDBConnection
					.retrieveAllDataReturnedAmountInsurance(insuranceSTR,
							dateFrom, dateTo);

			while (resultSet.next()) {
				totalServiceCharge = Double.parseDouble(resultSet.getObject(2)
						.toString()) ;
				opdTable.addCell(new Phrase("Return Amount", font3));
				opdTable.addCell(new Phrase(""
						+ resultSet.getObject(1).toString(), font3));
				PdfPCell amount = new PdfPCell(new Phrase("-"
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				totalAmount = totalAmount - totalServiceCharge;
			}

			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataDischargeAmountInsurance(
					insuranceSTR, dateFrom, dateTo);
			totalServiceCharge = 0;
			totalNo = 0;
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(2)
						.toString()) ;
				opdTable.addCell(new Phrase("Indoor Discharge", font3));
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

	public PdfPTable cancelledAmount(String dateFrom, String dateTo,
			String insuranceSTR) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			CancelledDBConnection db = new CancelledDBConnection();

			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAmountInsurance(insuranceSTR, dateFrom,
					dateTo);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				PdfPCell amount = new PdfPCell(new Phrase("-"
						+ totalServiceCharge, font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				cancelledAmount = totalServiceCharge + cancelledAmount;
			}
			if (totalNo > 0) {

			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}

	public void makeDirectory(String name) {

		new File("DailySlip").mkdir();

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