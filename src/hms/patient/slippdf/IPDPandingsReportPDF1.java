package hms.patient.slippdf;

import hms.main.DateFormatChange;
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class IPDPandingsReportPDF1 {

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

	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no,ward,bed,totalAmount,advanceAmount;
	String mainDir = "", str = "";
	Font font;
	float[] TablCellWidth = { 0.7f, 1.2f, 1.5f, 0.9f, 0.6f,0.6f,1.2f };

	public static void main(String[] arg) {
		try {
			new IPDPandingsReportPDF("2013-02-02", "2014-05-21");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IPDPandingsReportPDF1(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
//		dateFrom="1111-01-01";
//		dateTo="9999-01-01";
		readFile();
		str = "IPDPandingsReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
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
		font = new Font(base, 13f, Font.BOLD);
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(80);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);
		java.net.URL imgURL = IPDPandingsReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);
		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);
		java.net.URL imgURLRotaryClub = IPDPandingsReportPDF.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);
		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);
		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(PdfPCell.NO_BORDER);
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
		logocell.setBorder(PdfPCell.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(PdfPCell.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);
		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(PdfPCell.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);
		table.addCell(TitleTable);
		table.addCell(new Phrase("IPD Pending List", font3));
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
		opdTable.addCell(new Phrase("IPD No.", font3));
		opdTable.addCell(new Phrase("Patient ID", font3));
		opdTable.addCell(new Phrase("Patient Name", font3));
		opdTable.addCell(new Phrase("Ward/Bed", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Total", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(amount);
		PdfPCell Advanceamount = new PdfPCell(new Phrase("Advance (No.)", font3));
		Advanceamount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(Advanceamount);
		opdTable.addCell(new Phrase("Date", font3));
		table.addCell(opdTable);
		
//				PdfPCell header = new PdfPCell(new Phrase(""
//						+ " -> " , font3));
//				header.setBorder(PdfPCell.NO_BORDER);
//				header.setPaddingBottom(3);
//				header.setHorizontalAlignment(Element.ALIGN_CENTER);
//				header.setBackgroundColor(BaseColor.LIGHT_GRAY);
//				table.addCell(header);
		table.addCell(IPDData(dateFrom,dateTo));
//		PdfPCell footer2 = new PdfPCell(new Phrase("Total Bed Occupancy : "
//				+ totalBeds, font1));
//		footer2.setBorder(PdfPCell.NO_BORDER);
//		footer2.setPaddingBottom(5);
//		footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		// footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
//		table.addCell(footer2);
		document.add(table);
		document.close();

		new OpenFile(RESULT);
	}
	public PdfPTable IPDData(String dateFrom,String dateTo) {
		PdfPTable opdTable = new PdfPTable(TablCellWidth);
		try {
			IPDDBConnection dbConnection = new IPDDBConnection();
			ResultSet rs = dbConnection.retrieveAllDataNonDischargeAmount(dateFrom, dateTo);

			while (rs.next()) {
				opdTable.addCell(new Phrase("" + rs.getObject(1).toString(),
						font3));
				patient_id= rs.getObject(2).toString();
				patient_name= rs.getObject(3).toString();
				ward= rs.getObject(4).toString();
				bed= rs.getObject(5).toString();
				totalAmount=rs.getObject(6).toString();
				advanceAmount= rs.getObject(7).toString();
				date=rs.getObject(8).toString();
				opdTable.addCell(new Phrase("" + patient_id,
						font3));
				opdTable.addCell(new Phrase("" +patient_name,
						font3));
				opdTable.addCell(new Phrase("" +ward+"/"+bed,
						font3));
				
				PdfPCell total = new PdfPCell(new Phrase(""
						+ totalAmount, font3));
				total.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(total);
				
				PdfPCell advance = new PdfPCell(new Phrase(""
						+ advanceAmount+"("+getIPDPayments(patient_id,date)+")", font3));
				advance.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(advance);
				PdfPCell date1 = new PdfPCell(new Phrase(""
						+ DateFormatChange.StringToDateFormat(date), font3));
				date1.setHorizontalAlignment(Element.ALIGN_LEFT);
				opdTable.addCell(date1);
			}
			dbConnection.closeConnection();
		} catch (Exception ex) {
			System.out.println(" exceptin here"+ex);
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

	public int getIPDPayments(String ipd_id,String dateFrom) {
		IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
		ResultSet resultSet = db.retrieveAllDataAdvanceAmountPatient(ipd_id,dateFrom);
		int i=0;
		try {
			while (resultSet.next()) {
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return i;
	}

}