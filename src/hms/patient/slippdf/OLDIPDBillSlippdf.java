package hms.patient.slippdf;

import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

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
import java.util.Date;
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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class OLDIPDBillSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverFile = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0,
			payableAmount = 0;
	String ipd_no_a, patient_id, patient_name, patient_address, patient_age, doctor_name,
			amt_received, date, bill_no;
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_date_dis = "", ipd_time_dis = "", ipd_note = "",
			ipd_id = "", ward_name = "", building_name = "",
			bed_no = "Select Bed No", ward_incharge = "", ward_room = "",
			ward_code = "", descriptionSTR = "", charges = "", ipd_days,
			ipd_hours, ipd_minutes, ipd_expenses_id;
	static String OS;
	String mainDir = "";
	Font font;
	float[] TablCellWidth = {  0.7f,2.0f, 1.1f };
	String[] open = new String[4];

	public static void main(String[] argh) {
		try {
			new OLDIPDBillSlippdf("1","79", "sdfsdf");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OLDIPDBillSlippdf(String ipd_bill,String ipd_no, String doctorName)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		bill_no=ipd_bill;
		getIPDData(ipd_no);
		getPatientDetail(patient_id);
		readFile();
		this.ipd_id=ipd_no;
		makeDirectory(ipd_no);
	
		Document document = new Document();
		OS = System.getProperty("os.name").toLowerCase();
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

		java.net.URL imgURL = OLDIPDBillSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = OLDIPDBillSlippdf.class
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

		
		PdfPCell spaceCell = new PdfPCell(new Paragraph(" Bill cum Cash Receipt ", font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);
		//
		float[] opdTablCellWidth = { 1f, 1f, 1f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Bill No." + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(bill_no + "\n", font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient ID: ", font3));
		opdTable.addCell(new Phrase("" + patient_id, font3));

		opdTable.addCell(new Phrase("IPD No. : ", font3));
		opdTable.addCell(new Phrase("" + ipd_no_a, font3));

		opdTable.addCell(new Phrase("Patient Name: ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));
		
		
		opdTable.addCell(new Phrase("Doctor Incharge :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctorName,
				font3));
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
		
		PdfPCell agecell = new PdfPCell(new Phrase(""+patient_age, font3));
		agecell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(new Phrase("Age : ", font3));
		opdTable.addCell(agecell);


		opdTable.addCell(new Phrase("Admissioin Date : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(ipd_date), font3));

		opdTable.addCell(new Phrase("Address : ", font3));
		opdTable.addCell(new Phrase(""+patient_address, font3));


		opdTable.addCell(new Phrase("Discharge Date : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(ipd_date_dis), font3));
		

		opdTable.addCell(new Phrase("Bed No. : ", font3));
		opdTable.addCell(new Phrase(""+bed_no+" ("+ward_name+")", font3));

		opdTable.addCell(new Phrase("No. of Days", font3));
		opdTable.addCell(new Phrase(""+ipd_days+" Days, "+ipd_hours+" Hours", font3));

		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));

		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(5);
		table.addCell(opdCell);

		table.addCell(new Phrase("  ", font3));
		table.addCell(ipdAmount(ipd_no));
		
		
		PdfPCell total = new PdfPCell(new Paragraph("Total :     "+totalCharges, smallBold));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingTop(10);
		total.setPaddingBottom(1);
		table.addCell(total);
		
		PdfPCell advance = new PdfPCell(new Paragraph("(-)Less Advance Amt :     "+ipd_advance, smallBold));
		advance.setBorder(Rectangle.NO_BORDER);
		advance.setHorizontalAlignment(Element.ALIGN_RIGHT);
		advance.setPaddingTop(2);
		advance.setPaddingBottom(1);
		table.addCell(advance);
		
		PdfPCell payable = new PdfPCell(new Paragraph("Amount Payable :     "+payableAmount, font3));
		payable.setBorder(Rectangle.NO_BORDER);
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payable.setPaddingTop(2);
		payable.setPaddingBottom(1);
		table.addCell(payable);
		
		PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words : "+NumberToWordConverter.convert((int)payableAmount)+" Only", font3));
		payableInWords.setBorder(Rectangle.NO_BORDER);
		payableInWords.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payableInWords.setPaddingTop(5);
		payableInWords.setPaddingBottom(10);
		table.addCell(payableInWords);
		

		
		PdfPCell footer2 = new PdfPCell(
				new Phrase(
						"Received Rs "+payableAmount+" by cash on date "+DateFormatChange.StringToDateFormat(ipd_date_dis)+" toward settlement of the above bill.",
						font3));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		//footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell(footer2);

		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		sign.setBorder(Rectangle.NO_BORDER);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(10);
		sign.setPaddingBottom(1);
		table.addCell(sign);
		document.add(table);
		// onEndPage(wr,document);
		document.close();

		if (isWindows()) {
			OPenFileWindows("IPDSlip/" + ipd_no + ".pdf");
			System.out.println("This is Windows");
		} else if (isMac()) {
			System.out.println("This is Mac");
		} else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0] + " IPDSlip/" + ipd_no + ".pdf" });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1] + " IPDSlip/" + ipd_no + ".pdf" });
			}
			System.out.println("This is Unix or Linux");
		} else if (isSolaris()) {
			System.out.println("This is Solaris");
		} else {
			System.out.println("Your OS is not support!!");
		}
		try {
			copyFileUsingJava7Files("IPDSlip/" + ipd_no + ".pdf", serverFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PdfPTable ipdAmount(String ipd_no) {

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("S.No.", font3));
		Table.addCell(new Phrase("Description", font3));

		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
		try {
			IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
			ResultSet rs = db1.retrieveAllData(ipd_id);

			int r = 1;
			while (rs.next()) {
				Table.addCell(new Phrase("" + r, font3));
				Table.addCell(new Phrase("" + rs.getObject(2).toString(), font3));

				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ rs.getObject(3).toString(), font3));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				Table.addCell(amount1);
				r++;
			//	System.out.println(""+rs.getObject(3).toString());
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
	}

	public void getIPDData(String ipdID) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataIPDID(ipdID);
			while (rs.next()) {
				ipd_no_a = rs.getObject(1).toString();
				patient_id = rs.getObject(2).toString();
				patient_name = rs.getObject(3).toString();
				ward_name = rs.getObject(4).toString();
				bed_no = rs.getObject(5).toString();
				ipd_date = rs.getObject(6).toString();
				ipd_time = rs.getObject(7).toString();
				ipd_advance = Double.parseDouble(rs.getObject(8).toString());
				totalCharges = Double.parseDouble(rs.getObject(9).toString());
				ipd_balance = Double.parseDouble(rs.getObject(10).toString());
				payableAmount = Double.parseDouble(rs.getObject(11).toString());
				if(!bill_no.equals("Provisional Bill"))
				{
					ipd_date_dis = rs.getObject(12).toString();
					ipd_time_dis = rs.getObject(13).toString();
				}
				else {
					ipd_date_dis=DateFormatChange.StringToMysqlDate(new Date());
					ipd_time_dis="";
				}
				
				System.out.println(ipd_date+"  "+ipd_date_dis);
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time),
										
										
											
								DateFormatChange.javaDate(ipd_date_dis + " "
										+ ipd_time_dis));
				
				ipd_days=bedHours[0]+"";
				ipd_hours=bedHours[1]+"";
				ipd_minutes=bedHours[2]+"";
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println(patient_id);
	}

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_age = resultSet.getObject(2).toString();
				patient_address= resultSet.getObject(4).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age);
		patientDBConnection.closeConnection();
		String data[] = new String[22];
		int i = 0;
		for (String retval : patient_age.split("-")) {
			data[i] = retval;
			i++;
		}
		if (!data[0].equals("0")) {
			patient_age = data[0] + " years";
		} else if (!data[1].equals("0")) {
			patient_age = data[1] + " months";
		} else if (!data[2].equals("0")) {
			patient_age = data[2] + " days";
		}
	}

	public void makeDirectory(String opd_no) {

		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/IPDSlip");
			if (!dir.exists())
				dir.mkdirs();
			
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new File("IPDSlip").mkdir();
		serverFile = mainDir + "/HMS/IPDSlip/" + opd_no + ".pdf";
		RESULT = "IPDSlip/" + opd_no + ".pdf";
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