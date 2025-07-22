package hms.patient.slippdf;

import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamEntery;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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

public class PatientsTotalBillPDF {

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
	String ipd_no_a, patient_id, patient_name,patient_sex, patient_address,patient_insurancetype,patient_insurance_no, patient_age,patient_mobile, doctor_name,
			amt_received, date, bill_no;

	String mainDir = "";
	Font font;

	String[] open = new String[4];
	PdfPCell header ;
	double totalAmount=0;
	
	PdfPTable addtable;

	public static void main(String[] argh) {
		try {
			new PatientsTotalBillPDF("0000130000001","2014-09-26", "2017-10-26");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	public PatientsTotalBillPDF( String patient_id,String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		getPatientDetail(patient_id);
		makeDirectory(patient_id);
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

		java.net.URL imgURL = PatientsTotalBillPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = PatientsTotalBillPDF.class
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
//		// write content
//        header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
		
		String bill=" Patient Bill ";
		
		PdfPCell spaceCell = new PdfPCell(new Paragraph(bill, font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);
		
		float[] opdTablCellWidth = { 1f, 1f, 1f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Patient ID :" + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(patient_id + "\n", font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Address : ", font3));
		opdTable.addCell(new Phrase("" + patient_address, font3));

		
		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		
		opdTable.addCell(new Phrase("Mobile No. : ", font3));
		opdTable.addCell(new Phrase("" + patient_mobile, font3));
		
		
		opdTable.addCell(new Phrase("Patient Age :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + patient_age,
				font3));
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
		
		
		opdTable.addCell(new Phrase("Insurance Type :", font3));
		
		PdfPCell agecell = new PdfPCell(new Phrase(""+patient_insurancetype, font3));
		agecell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(agecell);


		opdTable.addCell(new Phrase("Patient Sex : ", font3));
		opdTable.addCell(new Phrase(""+patient_sex, font3));

		opdTable.addCell(new Phrase("Insurance No. : ", font3));
		opdTable.addCell(new Phrase(""+patient_insurance_no, font3));


		opdTable.addCell(new Phrase("Date : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(DateFormatChange.StringToMysqlDate(new Date())), font3));
		
		
		
		
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
	
		opdTable.addCell(new Phrase("Time :", font3));
		opdTable.addCell(new Phrase(""+timeFormat.format(cal1.getTime()), font3));

		
		
		opdTable.addCell(new Phrase("From Date : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(dateFrom), font3));
	
		
		opdTable.addCell(new Phrase("To Date : ", font3));
		opdTable.addCell(new Phrase(""+DateFormatChange.StringToDateFormat(dateTo), font3));
	
		
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(5);
		table.addCell(opdCell);

		header = new PdfPCell(new Phrase("Patient OPD ", font3));
		header.setBorder(Rectangle.NO_BORDER);
		header.setPaddingBottom(3);
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setBackgroundColor(BaseColor.LIGHT_GRAY);

		
		
		table.addCell(new Phrase("  ", font3));
		
		addtable=opdTable(patient_id,dateFrom,dateTo);
		if(totalAmount>0)
		{
			table.addCell(header);
			table.addCell(addtable);
		}
		
	
		
		header = new PdfPCell(new Phrase("Patient Exams ", font3));
		header.setBorder(Rectangle.NO_BORDER);
		header.setPaddingBottom(3);
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		
		
		table.addCell(new Phrase("  ", font3));
		addtable=examTable(patient_id,dateFrom,dateTo);
		if(totalAmount>0)
		{
			table.addCell(header);
			table.addCell(addtable);
		}
		
		
		header = new PdfPCell(new Phrase("Patient Miscellaneous ", font3));
		header.setBorder(Rectangle.NO_BORDER);
		header.setPaddingBottom(3);
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		
		
		table.addCell(new Phrase("  ", font3));
		addtable=miscTable(patient_id,dateFrom,dateTo);
		if(totalAmount>0)
		{
			table.addCell(header);
			table.addCell(addtable);
		}
		
		
		
		PdfPCell total = new PdfPCell(new Paragraph("Total :     "+totalCharges, smallBold));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingTop(10);
		total.setPaddingBottom(1);
	//	table.addCell(total);
		
		
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
		

		

		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		sign.setBorder(Rectangle.NO_BORDER);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(10);
		sign.setPaddingBottom(1);
		table.addCell(sign);
		document.add(table);
		// onEndPage(wr,document);
		document.close();

		new OpenFile(RESULT);
	}

	public PdfPTable opdTable(String patient_id,String dateFrom,String dateTo) {

		float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f };
		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("OPD No.", font3));
		Table.addCell(new Phrase("OPD Type", font3));
		Table.addCell(new Phrase("Date", font3));

		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
	
		try {
			OPDDBConnection db1 = new OPDDBConnection();
			ResultSet rs = db1.retrieveAllDataPatient(dateFrom,dateTo,patient_id);
			totalAmount=0;
			while (rs.next()) {
				Table.addCell(new Phrase("" + rs.getObject(1).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(3).toString(), font3));
				Table.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(rs.getObject(4).toString()), font3));
				
				totalAmount=Double.parseDouble(rs.getObject(5).toString());
				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ totalAmount, font3));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				payableAmount=payableAmount+totalAmount;
				Table.addCell(amount1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
	}

	
	public PdfPTable examTable(String patient_id,String dateFrom,String dateTo) {

		float[] TablCellWidth = {  0.5f, 0.5f,2.0f,1.2f, 0.5f };
		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("Exam No.", font3));
		Table.addCell(new Phrase("CGHS Sr. No.", font3));
		Table.addCell(new Phrase("Exam Name", font3));
		Table.addCell(new Phrase("Date", font3));

		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
		totalAmount=0;
		try {
			ExamDBConnection db1 = new ExamDBConnection();
			ResultSet rs = db1.retrieveTestPatient(dateFrom,dateTo,patient_id);

			while (rs.next()) {
				Table.addCell(new Phrase("" + rs.getObject(1).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(2).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(3).toString(), font3));
				Table.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(rs.getObject(4).toString()), font3));
				
				totalAmount=Double.parseDouble(rs.getObject(5).toString());
				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ totalAmount, font3));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				payableAmount=payableAmount+totalAmount;
				Table.addCell(amount1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
	}


	
	public PdfPTable miscTable(String patient_id,String dateFrom,String dateTo) {

		float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f };
		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("Misc. No.", font3));
		Table.addCell(new Phrase("Misc. Name", font3));
		Table.addCell(new Phrase("Date", font3));

		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);
		totalAmount=0;
		try {
			MiscDBConnection db1 = new MiscDBConnection();
			ResultSet rs = db1.retrieveAllPatientWise(dateFrom,dateTo,patient_id);

			while (rs.next()) {
				Table.addCell(new Phrase("" + rs.getObject(1).toString(), font3));
				Table.addCell(new Phrase("" + rs.getObject(2).toString(), font3));
				Table.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(rs.getObject(3).toString()), font3));
				
				totalAmount=Double.parseDouble(rs.getObject(4).toString());
				PdfPCell amount1 = new PdfPCell(new Phrase(""
						+ totalAmount, font3));
				amount1.setHorizontalAlignment(Element.ALIGN_RIGHT);
				
				payableAmount=payableAmount+totalAmount;
				Table.addCell(amount1);
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
	}



	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_name = resultSet.getObject(1).toString();
				patient_age = resultSet.getObject(2).toString();
				patient_sex = resultSet.getObject(3).toString();
				patient_address= resultSet.getObject(4).toString();
				patient_mobile= resultSet.getObject(6).toString();
				patient_insurancetype=resultSet.getObject(7).toString();
				patient_insurance_no=resultSet.getObject(8).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age+"  "+indexId);
		patientDBConnection.closeConnection();
		String data[] = new String[22];
		int i = 0;
		try {
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
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
	}

	public void makeDirectory(String p_id) {

		
		new File("PatientFile").mkdir();
		RESULT = "PatientFile/" + p_id + ".pdf";
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