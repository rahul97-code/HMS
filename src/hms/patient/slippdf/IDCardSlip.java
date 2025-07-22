package hms.patient.slippdf;

import hms.patient.database.PatientDBConnection;

import java.awt.Desktop;
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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class IDCardSlip {
	private static Font smallBoldNew = new Font(Font.FontFamily.HELVETICA, 7,Font.BOLD);
	private static Font smallBoldNew1 = new Font(Font.FontFamily.HELVETICA, 6,Font.BOLD);
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 9);
	private static Font space = new Font(Font.FontFamily.HELVETICA, 20);
	private static Font idBold = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 10,
			Font.BOLD);
	public static String RESULT = "table.pdf";
	
	String p_id, p_code, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String mainDir = "";

	String[] open=new String[4];

	public IDCardSlip(String index) throws FileNotFoundException,
			DocumentException, SmbException, MalformedURLException,
			UnknownHostException {
		setPatientDetail(index);
		readFile();
		makeDirectory(index);
		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(145, 160, 12, 0);
		document.open();
		float[] widths = { 0.25f, 0.75f };
		PdfPTable table = new PdfPTable(widths);
		table.getDefaultCell().setBorder(0);
		PdfPCell cell = new PdfPCell(new Phrase(
				"Rotary Ambala Cancer & General Hospital" + "\n", font1));
		cell.setPaddingBottom(5);
		cell.setColspan(3);
		table.addCell(cell);
		table.addCell(new Phrase("ID No. ", idBold));
		table.addCell(new Phrase(":       " + p_id, idBold));
		table.addCell(new Phrase("Name ", smallBold));
		table.addCell(new Phrase(":       " + p_name + "  (" + p_sex + ")",
				smallBold));
		table.addCell(new Phrase("" + p_guardiantype, smallBold));
		table.addCell(new Phrase(":       " + p_p_father_husband, smallBold));
		table.addCell(new Phrase("Age ", smallBold));
		table.addCell(new Phrase(":       " + p_age, smallBold));
		table.addCell(new Phrase("Telephone", smallBold));
		table.addCell(new Phrase(":       " + p_telephone, smallBold));
		table.addCell(new Phrase("Insurance ", smallBold));
		table.addCell(new Phrase(":       " + p_insurancetype, smallBold));
		table.addCell(new Phrase("Address ", smallBold));
		table.addCell(new Phrase(":       " + p_address + ", " + p_city,
				smallBold));

		PdfContentByte cb = wr.getDirectContent();
		PdfPTable table1 = new PdfPTable(1);
		table1.getDefaultCell().setBorder(0);
		PdfPCell cell1 = new PdfPCell();
		cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
		Barcode128 code128 = new Barcode128();
		code128.setCode(p_id);
		code128.setSize(6f);
		code128.setBaseline(-1f);
		code128.setGuardBars(true);
		code128.setBarHeight(10f);
		cell1.setPaddingRight(20);
		cell1.setPaddingRight(20);
		table1.setHorizontalAlignment(Element.ALIGN_CENTER);
		table1.addCell(code128.createImageWithBarcode(cb, null, null));

		table1.addCell(new Phrase(
				"With this registration,patient agrees to receive SMS text messages", smallBoldNew));
		table1.addCell(new Phrase(
				"If You donot wish to receive messages,please inform 9896369290", smallBoldNew));
		table1.addCell(new Phrase(" ", smallBold));
		table1.addCell(new Phrase(" ", smallBold));
		table1.addCell(new Phrase(
				"      Rotary Ambala Cancer and General Hospital", idBold));
		table1.addCell(new Phrase(
				"       Opp. Dussehra Ground,Near Ram Bagh Road", smallBold));
		table1.addCell(new Phrase("                   Ambala Cantt (Haryana)",
				smallBold));
		table1.addCell(new Phrase(
				"      Reception : 0171-2690009, Admin : 09034056793", smallBold));
		table1.addCell(new Phrase(
				"    We always strive to maintain confidentiality of your information", smallBoldNew));
		table1.addCell(new Phrase(" ", smallBold));

		PdfPTable table2 = new PdfPTable(1);
		table2.setWidthPercentage(75);
		table1.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		PdfPCell cell2 = new PdfPCell(table);
		cell2.setPaddingTop(5);
		cell2.setPaddingLeft(10);
		cell2.setPaddingRight(10);
		cell2.setPaddingBottom(10);
		cell2.setBorderWidth(1);
		table2.addCell(cell2);

		PdfPCell cell4 = new PdfPCell(new Phrase(" ", space));
		cell4.setBorder(Rectangle.NO_BORDER);
		cell4.setPaddingLeft(25);
		table2.addCell(cell4);
		
		PdfPCell cell3 = new PdfPCell(table1);
		cell3.setRotation(180);
		cell3.setBorder(Rectangle.NO_BORDER);
		cell3.setPaddingLeft(0);
		cell3.setPaddingRight(0);
		cell3.setBorderWidth(1);
		table2.addCell(cell3);

		PdfPTable main = new PdfPTable(1);
		main.setWidthPercentage(80);

		PdfPCell maincell = new PdfPCell(table2);
		maincell.setBorder(Rectangle.NO_BORDER);
		maincell.setPaddingLeft(20);
		main.addCell(maincell);
		document.add(main);
		document.close();
		try {
			copyFileUsingJava7Files(RESULT, "PatientSlip/" + p_id + ".pdf");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new OpenFile(RESULT);
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
	
		new File("PatientSlip").mkdir();
		RESULT = "PatientSlip/" + pid + ".pdf";
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


}