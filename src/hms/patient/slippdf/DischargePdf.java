package hms.patient.slippdf;

import hms.doctor.database.DoctorDBConnection;
import hms.main.MainLogin;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.free_test.FreeTestDBConnection;
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
import java.io.StringReader;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.sf.jasperreports.engine.export.oasis.ParagraphStyle;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.html.simpleparser.HTMLWorker;
import com.itextpdf.text.html.HtmlTags;
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class DischargePdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font smallBold3 = new Font(Font.FontFamily.HELVETICA, 10,
			Font.BOLD, BaseColor.BLACK);
	private static Font smallBold2 = new Font(Font.FontFamily.HELVETICA, 6.5f,
			Font.BOLD);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 20,
			Font.BOLD, BaseColor.BLACK);
	private static Font font14 = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "test1.pdf";

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no, time,entry_date,discharge_date,p_father_husband,p_address,p_city,p_gender;
	String mainDir = "";
	Font font;
	String[] open = new String[4];

	public static void main(String[] argh) {
		try {
			new DischargePdf("12171", true, "ONLINE REGISTERED", "","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DischargePdf(String ipd_no, boolean original, String online,
			String message,String remarks) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		// getAllDoctorsRegular();
		getOPDData(ipd_no);
		
		readFile();
		makeDirectory(ipd_no);
		Document document = new Document();

		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 10, 0);
		document.open();

		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 8f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = OPDSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = DischargePdf.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(4);
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
		logocell.setRowspan(4);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell1 = new PdfPCell(
				new Phrase(
						"Managed by:Rotary Ambala Cancer Detection & Welfare Soceity(Regd)",
						font2));
		addressCell1.setPaddingBottom(2);
		addressCell1.setBorder(Rectangle.NO_BORDER);
		addressCell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell1);
		
		
		
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

		float[] doctorTableCellWidth = { 0.5f, 1f, 0.5f,1f };
		PdfPTable doctorTable = new PdfPTable(doctorTableCellWidth);
		
		doctorTable.getDefaultCell().setBorder(0);
		
//		doctorTable.addCell(new Phrase("", font4));
//		doctorTable.addCell(new Phrase("Patient Detail", font4));
//		doctorTable.addCell(new Phrase(" ", smallBold));
//		doctorTable.addCell(new Phrase("Admission Detail", font4));
		PdfPCell tokencell_top = new PdfPCell(new Phrase("" + "\n", font3));
		tokencell_top.setBorder(Rectangle.NO_BORDER);
		tokencell_top.setPaddingBottom(10);
		PdfPCell tokenNocell_top = new PdfPCell(new Phrase("Patient Detail", font3));
		tokenNocell_top.setBorder(Rectangle.NO_BORDER);
		tokenNocell_top.setPaddingBottom(10);
		PdfPCell admittingcell_top = new PdfPCell(new Phrase("Admission Details" + "\n", font3));
		admittingcell_top.setBorder(Rectangle.NO_BORDER);
		admittingcell_top.setPaddingBottom(10);
		PdfPCell admittingcell1_top = new PdfPCell(new Phrase("", font3));
		admittingcell1_top.setBorder(Rectangle.NO_BORDER);
		admittingcell1_top.setPaddingBottom(10);
		doctorTable.addCell(tokencell_top);
		doctorTable.addCell(tokenNocell_top);
		doctorTable.addCell(admittingcell_top);
		doctorTable.addCell(admittingcell1_top);
		PdfPCell tokencell = new PdfPCell(new Phrase("Name." + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(5);
		PdfPCell tokenNocell = new PdfPCell(new Phrase(patient_name, smallBold));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(5);
		PdfPCell admittingcell = new PdfPCell(new Phrase("Admitting Consultant." + "\n", font3));
		admittingcell.setBorder(Rectangle.NO_BORDER);
		admittingcell.setPaddingBottom(5);
		PdfPCell admittingcell1 = new PdfPCell(new Phrase(doctor_name, smallBold));
		admittingcell1.setBorder(Rectangle.NO_BORDER);
		admittingcell1.setPaddingBottom(5);
		doctorTable.addCell(tokencell);
		doctorTable.addCell(tokenNocell);
		doctorTable.addCell(admittingcell);
		doctorTable.addCell(admittingcell1);
		
		
		
		
		
		PdfPCell tokencell_2nd = new PdfPCell(new Phrase("Father Name." + "\n", font3));
		tokencell_2nd.setBorder(Rectangle.NO_BORDER);
		tokencell_2nd.setPaddingBottom(5);
		PdfPCell tokenNocell_2nd = new PdfPCell(new Phrase(""+p_father_husband, smallBold));
		tokenNocell_2nd.setBorder(Rectangle.NO_BORDER);
		tokenNocell_2nd.setPaddingBottom(5);
		PdfPCell admittingcell_2nd = new PdfPCell(new Phrase("Admission Date." + "\n", font3));
		admittingcell_2nd.setBorder(Rectangle.NO_BORDER);
		admittingcell_2nd.setPaddingBottom(5);
		PdfPCell admittingcell1_2nd = new PdfPCell(new Phrase(entry_date, smallBold));
		admittingcell1_2nd.setBorder(Rectangle.NO_BORDER);
		admittingcell1_2nd.setPaddingBottom(5);
		doctorTable.addCell(tokencell_2nd);
		doctorTable.addCell(tokenNocell_2nd);
		doctorTable.addCell(admittingcell_2nd);
		doctorTable.addCell(admittingcell1_2nd);
		
		
		PdfPCell tokencell_3rd = new PdfPCell(new Phrase("Address" + "\n", font3));
		tokencell_3rd.setBorder(Rectangle.NO_BORDER);
		tokencell_3rd.setPaddingBottom(5);
		PdfPCell tokenNocell_3rd= new PdfPCell(new Phrase(""+p_address+"  "+p_city, smallBold));
		tokenNocell_3rd.setBorder(Rectangle.NO_BORDER);
		tokenNocell_3rd.setPaddingBottom(5);
		PdfPCell admittingcell_3rd = new PdfPCell(new Phrase("Discharging Consultant" + "\n", font3));
		admittingcell_3rd.setBorder(Rectangle.NO_BORDER);
		admittingcell_3rd.setPaddingBottom(5);
		PdfPCell admittingcell1_3rd = new PdfPCell(new Phrase(""+doctor_name, smallBold));
		admittingcell1_3rd.setBorder(Rectangle.NO_BORDER);
		admittingcell1_3rd.setPaddingBottom(5);
		doctorTable.addCell(tokencell_3rd);
		doctorTable.addCell(tokenNocell_3rd);
		doctorTable.addCell(admittingcell_3rd);
		doctorTable.addCell(admittingcell1_3rd);
		
		
		PdfPCell tokencell_4th = new PdfPCell(new Phrase("UHID ID" + "\n", font3));
		tokencell_4th.setBorder(Rectangle.NO_BORDER);
		tokencell_4th.setPaddingBottom(5);
		PdfPCell tokenNocell_4th= new PdfPCell(new Phrase(patient_id, smallBold));
		tokenNocell_4th.setBorder(Rectangle.NO_BORDER);
		tokenNocell_4th.setPaddingBottom(5);
		PdfPCell admittingcell_4th = new PdfPCell(new Phrase("Discharge Type" + "\n", font3));
		admittingcell_4th.setBorder(Rectangle.NO_BORDER);
		admittingcell_4th.setPaddingBottom(5);
		PdfPCell admittingcell1_4th = new PdfPCell(new Phrase(""+remarks, smallBold));
		admittingcell1_4th.setBorder(Rectangle.NO_BORDER);
		admittingcell1_4th.setPaddingBottom(5);
		doctorTable.addCell(tokencell_4th);
		doctorTable.addCell(tokenNocell_4th);
		doctorTable.addCell(admittingcell_4th);
		doctorTable.addCell(admittingcell1_4th);
		
		
		
		
		PdfPCell tokencell_5th = new PdfPCell(new Phrase("IPD No" + "\n", font3));
		tokencell_5th.setBorder(Rectangle.NO_BORDER);
		tokencell_5th.setPaddingBottom(5);
		PdfPCell tokenNocell_5th= new PdfPCell(new Phrase(""+ipd_no, smallBold));
		tokenNocell_5th.setBorder(Rectangle.NO_BORDER);
		tokenNocell_5th.setPaddingBottom(5);
		PdfPCell admittingcell_5th = new PdfPCell(new Phrase("" + "\n", font3));
		admittingcell_5th.setBorder(Rectangle.NO_BORDER);
		admittingcell_5th.setPaddingBottom(5);
		PdfPCell admittingcell1_5th = new PdfPCell(new Phrase("", smallBold));
		admittingcell1_5th.setBorder(Rectangle.NO_BORDER);
		admittingcell1_5th.setPaddingBottom(5);
		doctorTable.addCell(tokencell_5th);
		doctorTable.addCell(tokenNocell_5th);
		doctorTable.addCell(admittingcell_5th);
		doctorTable.addCell(admittingcell1_5th);
		PdfPCell tokencell_6th = new PdfPCell(new Phrase("Sex:" + "\n", font3));
		tokencell_6th.setBorder(Rectangle.NO_BORDER);
		tokencell_6th.setPaddingBottom(5);
		PdfPCell tokenNocell_6th= new PdfPCell(new Phrase(""+p_gender, smallBold));
		tokenNocell_6th.setBorder(Rectangle.NO_BORDER);
		tokenNocell_6th.setPaddingBottom(5);
		PdfPCell admittingcell_6th = new PdfPCell(new Phrase("" + "\n", font3));
		admittingcell_6th.setBorder(Rectangle.NO_BORDER);
		admittingcell_6th.setPaddingBottom(5);
		PdfPCell admittingcell1_6th = new PdfPCell(new Phrase("", smallBold));
		admittingcell1_6th.setBorder(Rectangle.NO_BORDER);
		admittingcell1_6th.setPaddingBottom(5);
		doctorTable.addCell(tokencell_6th);
		doctorTable.addCell(tokenNocell_6th);
		doctorTable.addCell(admittingcell_6th);
		doctorTable.addCell(admittingcell1_6th);
//		getAllDoctors("0");
//		int k = doctorname.size() / 2;
//		System.out.println("regular doctor" + k);
//		for (int i = 0; i < k; i++) {
//
		
//		}

		PdfPCell doctorCell = new PdfPCell(doctorTable);

		doctorCell.setPaddingTop(2);
		doctorCell.setPaddingBottom(5);
		doctorCell.setPaddingLeft(5);
		doctorCell.setBorderWidth(0.8f);
		table.addCell(doctorCell);

		
//		HTMLWorker htmlWorker = new HTMLWorker(document);
		
		
	
		document.add(table);
		ArrayList p=new ArrayList();
		HTMLWorker htmlWorker = new HTMLWorker(document);
		p = (ArrayList) htmlWorker.parseToList(new StringReader(message), null);
		 Paragraph paragraph=new Paragraph();
	     for (int k = 0; k < p.size(); ++k){
	    	 paragraph.setIndentationLeft(26);
	         paragraph.add((Element)p.get(k));
	     }
	     document.add(paragraph);

//		PdfPCell cell = new PdfPCell(new Paragraph(htmlWorker.parse(new StringReader(message))+""));
////		String k1 = "<style type='text/css'>body {margin:0;padding:0}</style>";
//		HTMLWorker htmlWorker = new HTMLWorker(document);
//		
//		htmlWorker.parse(new StringReader(message));
		document.close();

		new OpenFile(RESULT);

	}

	public void getAllDoctors(String type) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData(type);
		try {
			while (resultSet.next()) {
				doctorname.addElement(resultSet.getObject(2).toString());
				specialization.addElement(" ("
						+ resultSet.getObject(3).toString() + ")");
				achievements.addElement(", "
						+ resultSet.getObject(7).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		if (doctorname.size() % 2 != 0) {
			doctorname.addElement("");
			specialization.addElement("");
			achievements.addElement("");
		}
	}

	public void getOPDData(String opdID) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithIpdId(opdID);
			while (rs.next()) {
			
				patient_id = rs.getObject(1).toString();
				patient_name = rs.getObject(2).toString();
				doctor_name = rs.getObject(5).toString();
				entry_date = rs.getObject(3).toString();
				discharge_date = rs.getObject(4).toString();
				System.out.println("Amount Recieved :" + amt_received);
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		getPatientDetail(patient_id);
		System.out.println(patient_id);
	}

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex1(indexId);

			while (resultSet.next()) {

				patient_name = resultSet.getObject(2).toString();
				patient_age = resultSet.getObject(5).toString();
				p_father_husband = resultSet.getObject(4).toString();
				p_address = resultSet.getObject(7).toString();
				p_city = resultSet.getObject(8).toString();
				p_gender = resultSet.getObject(6).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age);
		patientDBConnection.closeConnection();
//		String data[] = new String[22];
//		int i = 0;
//		for (String retval : patient_age.split("-")) {
//			data[i] = retval;
//			i++;
//		}
//		if (!data[0].equals("0")) {
//			patient_age = data[0] + " years";
//		} else if (!data[1].equals("0")) {
//			patient_age = data[1] + " months";
//		} else if (!data[2].equals("0")) {
//			patient_age = data[2] + " days";
//		}
	}

	public void makeDirectory(String opd_no) {

		// try {
		// SmbFile dir = new SmbFile(mainDir + "/HMS/opdslip");
		// if (!dir.exists())
		// dir.mkdirs();
		//
		// } catch (SmbException | MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		new File("DischargeSummaryReport").mkdir();
		RESULT = "DischargeSummaryReport/" + opd_no + ".pdf";
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
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
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

	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_RIGHT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getRight(), rect.getTop(), 0);
			break;
		case 1:
			ColumnText
					.showTextAligned(
							writer.getDirectContent(),
							Element.ALIGN_LEFT,
							new Phrase(
									"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
									font4), rect.getLeft(), rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}
}