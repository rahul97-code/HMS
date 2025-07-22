package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.main.DateFormatChange;
import hms1.wards.database.WardsManagementDBConnection;

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

public class WardsReportPDF {

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

	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> wards = new Vector<String>();
	Vector<String> building = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;

	String mainDir = "", str = "";
	Font font;
	int totalBeds = 0, cancelledAmount = 0;
	float[] TablCellWidth = { 0.5f, 2.0f, 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new WardsReportPDF("2013-02-02", "2014-02-21");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public WardsReportPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		readFile();
		str = "WardsReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
		getAllBuilding();
		Document document = new Document();
		
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4);
		document.setMargins(0,0, 50,70);
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
		java.net.URL imgURL = WardsReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);
		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);
		java.net.URL imgURLRotaryClub = WardsReportPDF.class
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
		table.addCell(new Phrase("Bed Occupancy Report", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
				.getInstance().getTime());
		System.out.println(timeStamp);
		// table.addCell(new Phrase("Date : From     "
		// + DateFormatChange.StringToDateFormat(dateFrom)
		// + "     to     " + DateFormatChange.StringToDateFormat(dateTo),
		// font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));
		PdfPTable opdTable = new PdfPTable(TablCellWidth);
		opdTable.addCell(new Phrase("No.", font3));
		opdTable.addCell(new Phrase("Patient ID", font3));
		opdTable.addCell(new Phrase("Patient Name", font3));
		opdTable.addCell(new Phrase("Bed No.", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Date", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(amount);
		table.addCell(opdTable);
		for (int i = 0; i < building.size(); i++) {
			getAllWards(building.get(i));
			for (int j = 0; j < wards.size(); j++) {
				PdfPCell header = new PdfPCell(new Phrase("" + building.get(i)
						+ " -> " + wards.get(j), font3));
				header.setBorder(Rectangle.NO_BORDER);
				header.setPaddingBottom(3);
				header.setHorizontalAlignment(Element.ALIGN_CENTER);
				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
				table.addCell(header);
				table.addCell(wardData(building.get(i), wards.get(j)));
			}
		}
		PdfPCell footer2 = new PdfPCell(new Phrase("Total Bed Occupancy : "
				+ totalBeds, font1));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		table.addCell(footer2);
		document.add(table);
		document.close();

		new OpenFile(RESULT);
	}
	public PdfPTable wardData(String building, String ward) {
		PdfPTable opdTable = new PdfPTable(TablCellWidth);
		try {
			WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
			ResultSet rs = dbConnection.retrieveAllBedsFilled(building, ward);
			int i = 1;
			while (rs.next()) {
				opdTable.addCell(new Phrase("" + i, font3));
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(2).toString(),
						font3));
				opdTable.addCell(new Phrase("" + rs.getObject(3).toString(),
						font3));
				PdfPCell date = new PdfPCell(new Phrase(""
						+ DateFormatChange.StringToDateFormat(rs.getObject(4)
								.toString()), font3));
				date.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(date);
				i++;
				totalBeds++;
			}
			dbConnection.closeConnection();
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


	public void getAllBuilding() {
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllBuilding();
		try {
			while (resultSet.next()) {
				building.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getAllWards(String building_name) {
		wards.removeAllElements();
		wards.clear();
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWards(building_name);
		try {
			while (resultSet.next()) {
				wards.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

}