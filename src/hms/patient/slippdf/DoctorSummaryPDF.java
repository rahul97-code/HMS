package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
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

public class DoctorSummaryPDF {

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

	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;
	String mainDir = "";
	Font font;
	int totalAmount = 0;
	String str;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 0.7f, 0.7f, 0.7f };

	public static void main(String[] arg) {
		try {
			new DoctorSummaryPDF("2024-01-01", "2024-01-31");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public DoctorSummaryPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		readFile();
		str="DoctorReports"+DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
		getAllDoctors();
		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4);
		document.setMargins(0,0, 50,80);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 9f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = DoctorSummaryPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = DoctorSummaryPDF.class
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

		table.addCell(new Phrase("Daily Doctor Summary ", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
				.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		OPDDBConnection db = new OPDDBConnection();

		opdTable.addCell(new Phrase("Doctor Name", font3));
		opdTable.addCell(new Phrase("OPD (Amount)", font3));

		opdTable.addCell(new Phrase("Exams", font3));
		opdTable.addCell(new Phrase("MISC", font3));
		opdTable.addCell(new Phrase("Indoor", font3));

		table.addCell(opdTable);

		table.addCell(doctorSummary(dateFrom, dateTo));

		document.add(table);
		document.close();

		new OpenFile(RESULT);
	}

	public PdfPTable doctorSummary(String dateFrom, String dateTo) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			for (int i = 0; i < doctorname.size(); i++) {
				int totalOPD = 0;
				int totalExam = 0;
				int totalMisc = 0;
				int totalIndoor = 0;
				double totalOPDAmount = 0;
				OPDDBConnection db = new OPDDBConnection();
				ResultSet rs = db.retrieveAllDataDoctorWise(dateFrom, dateTo,
						"" + doctorname.get(i));

				while (rs.next()) {
					totalOPDAmount = Double.parseDouble("0"
							+ rs.getObject(8).toString())
							+ totalOPDAmount;
					// System.out.println(rs.getObject(8).toString());
					totalOPD++;
				}
				db.closeConnection();
				ExamDBConnection db1 = new ExamDBConnection();
				ResultSet rs1 = db1.retrieveAllDataDoctorWise(dateFrom, dateTo,
						"" + doctorname.get(i));

				while (rs1.next()) {
					totalExam++;
				}
				db1.closeConnection();

				AmountReceiptDBConnection db2 = new AmountReceiptDBConnection();
				ResultSet rs2 = db2.retrieveAllDataDoctorWise(dateFrom, dateTo,
						"" + doctorname.get(i));

				while (rs2.next()) {

					totalMisc++;
				}
				db2.closeConnection();
				
				IPDDBConnection db3 = new IPDDBConnection();
				ResultSet rs3 = db3.retrieveAllDoctorIndoor(dateFrom, dateTo,
						"" + doctorname.get(i));

				while (rs3.next()) {

					totalIndoor++;
				}
				db2.closeConnection();

				if (totalOPD > 0 || totalExam > 0 || totalMisc > 0) {
					opdTable.addCell(new Phrase("" + doctorname.get(i), font3));
					opdTable.addCell(new Phrase("" + totalOPD + " ("
							+ totalOPDAmount + ")", font3));
					opdTable.addCell(new Phrase("" + totalExam, font3));
					opdTable.addCell(new Phrase("" + totalMisc, font3));
					opdTable.addCell(new Phrase("" + totalIndoor, font3));
				}
				
				
			}

		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}
	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllName();
		try {
			while (resultSet.next()) {
				doctorname.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		
	}
//	public void getAllDoctors() {
//
//		DoctorDBConnection dbConnection = new DoctorDBConnection();
//		ResultSet resultSet = dbConnection.retrieveAllData();
//		try {
//			while (resultSet.next()) {
//				doctorname.add(resultSet.getObject(2).toString());
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbConnection.closeConnection();
//
//	}

	public void makeDirectory(String opd_no) {

		new File("DailySlip").mkdir();
	
		RESULT = "DailySlip/" + opd_no + ".pdf";
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
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
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



	
}