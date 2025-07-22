package hms.patient.slippdf;

import hms.patient.database.PatientDBConnection;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileOutputStream;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BarcodeQRCode;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class MDIIDCardSlip {

	private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 9,Font.NORMAL,BaseColor.RED);
	private static Font space = new Font(Font.FontFamily.HELVETICA, 20);
	private static Font idBold = new Font(Font.FontFamily.HELVETICA, 9);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 10,
			Font.BOLD);
	public static String RESULT = "table.pdf";
	String p_id="", p_code="", p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String mainDir = "";
	static String OS;
	String[] open=new String[4];


	public static void main(String[] arg) {
		try {
			new MDIIDCardSlip("45");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public MDIIDCardSlip(String index) throws DocumentException, IOException {
	//	setPatientDetail(index);
		readFile();
		makeDirectory(index);
		OS = System.getProperty("os.name").toLowerCase();
		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new SmbFileOutputStream(
				RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(145, 160, 12, 0);
		document.open();

		java.net.URL imgURL = SummaryReportPDF.class
				.getResource("/icons/id card mdi.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(40);
		image.setAbsolutePosition(0, 0);
		
		java.net.URL imgURL2 = SummaryReportPDF.class
				.getResource("/icons/aladdin3.gif");
		Image image2 = Image.getInstance(imgURL2);

		image2.scalePercent(40);
		image2.setAbsolutePosition(0, 0);

		float[] widths = { 1f, 2f,0.80f };
		PdfPTable table = new PdfPTable(widths);
		table.getDefaultCell().setBorder(0);
		PdfPCell cell = new PdfPCell(image);
		cell.setColspan(2);
		cell.setBorder(0);
		cell.setPaddingLeft(2);
		cell.setPaddingRight(5);
		table.addCell(cell);
		
		PdfPCell idimages = new PdfPCell(image2);
		idimages.setBorderWidth(1);
		
		table.addCell(idimages);
		
		table.addCell(new Phrase("Employee ID", idBold));
		table.addCell(new Phrase("" + p_id, idBold));
		table.addCell(new Phrase("", smallBold));
		
		
		table.addCell(new Phrase("Name",
				smallBold));
		table.addCell(new Phrase("" , smallBold));
		table.addCell(new Phrase("", smallBold));
		
		
		table.addCell(new Phrase("Designation", smallBold));
		table.addCell(new Phrase("" + p_age, smallBold));
		table.addCell(new Phrase("", smallBold));
		
		
		table.addCell(new Phrase("Department", smallBold));
		table.addCell(new Phrase("", smallBold));
		table.addCell(new Phrase("", smallBold));
		
		table.addCell(new Phrase(" ", smallBold));
		table.addCell(new Phrase(" ", smallBold));
		table.addCell(new Phrase(" ", smallBold));
		
		
		table.addCell(new Phrase("Sd/Director", smallBold));
		table.addCell(new Phrase("", smallBold));
		table.addCell(new Phrase("Sd/Employe", smallBold));
		
	
		PdfPTable table1 = new PdfPTable(1);
		table1.getDefaultCell().setBorder(0);
		PdfPCell cell1 = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		
		 BarcodeQRCode qrcode = new BarcodeQRCode("Moby Dick by Herman Melville", 100, 100, null);
		 Image img  = qrcode.getImage();
		
		 img.scaleAbsolute(img.getHeight()*0.48f, img.getWidth()*0.48f);  
		
		
		table1.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell1.addElement(img);
		cell1.setBorder(Rectangle.NO_BORDER);
		cell1.setPaddingLeft(100);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table1.addCell(cell1);
		table1.addCell(new Phrase(" ", smallBold));
		table1.addCell(new Phrase("    Address :", smallBold));
		table1.addCell(new Phrase(
				"      Rotary Ambala Cancer and General Hospital", idBold));
		table1.addCell(new Phrase(
				"       Opp. Dussehra Ground,Near Ram Bagh Road", smallBold));
		table1.addCell(new Phrase("                   Ambala Cantt (Haryana)",
				smallBold));
		table1.addCell(new Phrase(
				"      Reception : 0171-2690009, Admin : 09034056793", smallBold));
		table1.addCell(new Phrase(" ", smallBold));

		PdfPTable table2 = new PdfPTable(1);
		table2.setWidthPercentage(90);
		table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		PdfPCell cell2 = new PdfPCell(table);
		cell2.setPaddingTop(5);
		cell2.setPaddingLeft(5);
		cell2.setPaddingRight(5);
		cell2.setPaddingBottom(5);
		cell2.setBorderWidth(1);
		table2.addCell(cell2);

		PdfPCell cell4 = new PdfPCell(new Phrase(" "));
		cell4.setBorder(Rectangle.NO_BORDER);
		cell4.setPaddingLeft(25);
		//table2.addCell(cell4);
		
		PdfPCell cell3 = new PdfPCell(table1);
		cell3.setRotation(180);
		cell3.setBorder(Rectangle.NO_BORDER);
		cell3.setBorderWidth(1);
		table2.addCell(cell3);

		PdfPTable main = new PdfPTable(1);
		main.setWidthPercentage(85);

		PdfPCell maincell = new PdfPCell(table2);
		maincell.setBorder(Rectangle.NO_BORDER);
		//maincell.setPaddingLeft(16);
		main.addCell(maincell);
		main.setHorizontalAlignment(Element.ALIGN_CENTER);
		document.add(main);
		document.close();
		try {
			copyFileUsingJava7Files(RESULT, "PatientSlip/" + p_id + ".pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isWindows()) {
			OPenFileWindows("PatientSlip/" + p_id + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0]+" PatientSlip/" + p_id + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1]+" PatientSlip/" + p_id + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
	}

	public void setPatientDetail(String indexId) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex1(indexId);
		try {
			while (resultSet.next()) {
				p_id = resultSet.getObject(1).toString();
				p_name = resultSet.getObject(2).toString();
				p_guardiantype = (resultSet.getObject(3).toString().equals("F")) ? "S/O"
						: "W/O";
				p_p_father_husband = resultSet.getObject(4).toString();
				p_age = resultSet.getObject(5).toString();
				p_sex = resultSet.getObject(6).toString();
				p_address = resultSet.getObject(7).toString();
				p_city = resultSet.getObject(8).toString();
				p_telephone = resultSet.getObject(9).toString();
				p_insurancetype = resultSet.getObject(10).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String data[] = new String[22];
		int i = 0;
		for (String retval : p_age.split("-")) {
			data[i] = retval;
			i++;
		}
		if (!data[0].equals("0")) {
			p_age = data[0] + " years";
		} else if (!data[1].equals("0")) {
			p_age = data[1] + " months";
		} else if (!data[2].equals("0")) {
			p_age = data[2] + " days";
		}
		if(p_guardiantype.equals("S/O"))
			p_guardiantype = (p_sex.equals("Male")) ? "S/O" : "D/O";

		patientDBConnection.closeConnection();
	}

	public void makeDirectory(String pid) {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient/" + pid + "/Exam");
			if (!dir.exists())
				dir.mkdirs();
			
		
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RESULT = mainDir + "/HMS/Patientslip/" + pid + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		new File("PatientSlip").mkdir();
		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = remoteFile.getInputStream();

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
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
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
}