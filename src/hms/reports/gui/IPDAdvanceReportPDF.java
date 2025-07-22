package hms.reports.gui;

import hms.main.DateFormatChange;
import hms.main.ServerAuthentication;
import hms.patient.slippdf.HeaderAndFooter;
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

public class IPDAdvanceReportPDF {

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
	public static String serverFile = "opdslip1.pdf";
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;

	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			user, date, token_no,desc,time,advanceAmount;
	static String OS;
	String mainDir = "", str = "";
	Font font;
	float[] TablCellWidth = { 1.2f, 1.1f, 0.9f, 0.6f,1.6f,1.0f };

	public static void main(String[] arg) {
		try {
			new IPDAdvanceReportPDF("2013-04-24", "2015-04-24");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public IPDAdvanceReportPDF(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
//		dateFrom="1111-01-01";
//		dateTo="9999-01-01";
		readFile();
		str = "IPDAdvanceToday" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
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
		java.net.URL imgURL = IPDAdvanceReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);
		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);
		java.net.URL imgURLRotaryClub = IPDAdvanceReportPDF.class
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
						"Telephone No. : 0171-2690009, Mobile No. : 09034056794",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(PdfPCell.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);
		table.addCell(TitleTable);
		table.addCell(new Phrase("IPD Advance", font3));
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
		
		opdTable.addCell(new Phrase("Patient ID", font3));
		opdTable.addCell(new Phrase("Patient Name", font3));
		opdTable.addCell(new Phrase("Desc.", font3));
		
		PdfPCell Advanceamount = new PdfPCell(new Phrase("Amount", font3));
		Advanceamount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(Advanceamount);
		opdTable.addCell(new Phrase("Date/Time", font3));
		opdTable.addCell(new Phrase("User", font3));
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

		if (isWindows()) {
			OPenFileWindows("DailySlip/" + str + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open DailySlip/" + str + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						"xdg-open DailySlip/" + str + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		try {
			copyFileUsingJava7Files("DailySlip/" + str + ".pdf", serverFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public PdfPTable IPDData(String dateFrom,String dateTo) {
		PdfPTable opdTable = new PdfPTable(TablCellWidth);
		try {
			IPDPaymentsDBConnection paymentsDBConnection = new IPDPaymentsDBConnection();
			ResultSet rs = paymentsDBConnection
					.retrieveAllDataAdvanceAmountAll(dateFrom, dateTo);

			

			while (rs.next()) {
				
				patient_id= rs.getObject(2).toString();
				patient_name= rs.getObject(3).toString();
				desc= rs.getObject(4).toString();
				advanceAmount= rs.getObject(5).toString();
				date=rs.getObject(6).toString();
				time= rs.getObject(7).toString();
				user=rs.getObject(8).toString();
				opdTable.addCell(new Phrase("" + patient_id,
						font3));
				opdTable.addCell(new Phrase("" +patient_name,
						font3));
				opdTable.addCell(new Phrase("" +desc,
						font3));
				
				PdfPCell total = new PdfPCell(new Phrase(""
						+ advanceAmount, font3));
				total.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(total);
				
				PdfPCell advance = new PdfPCell(new Phrase(""
						+ DateFormatChange.StringToDateFormat(date) +"/"+time, font3));
				advance.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(advance);
				PdfPCell date1 = new PdfPCell(new Phrase(""
						+user, font3));
				date1.setHorizontalAlignment(Element.ALIGN_LEFT);
				opdTable.addCell(date1);
			}
			paymentsDBConnection.closeConnection();
		} catch (Exception ex) {
			System.out.println(" exceptin here"+ex);
		}
		return opdTable;
	}

	public void makeDirectory(String name) {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/DailySlip",ServerAuthentication.FileServerAuthentication());
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
		SmbFile remoteFile = new SmbFile(dest,ServerAuthentication.FileServerAuthentication());
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

	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

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