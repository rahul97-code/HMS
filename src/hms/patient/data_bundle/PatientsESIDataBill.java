package hms.patient.data_bundle;

import com.itextpdf.text.*;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.CMYKColor;
import com.itextpdf.text.pdf.PRAcroForm;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.TextField;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.itextpdf.text.pdf.events.FieldPositioningEvents;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.HeaderFooter;
import hms.patient.slippdf.OpenFile;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.gui.IPDBrowser;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatientsESIDataBill {

	int r = 0;
	static String ipd_no_a, patient_id, patient_name, patient_sex,
			patient_address, patient_insurancetype, patient_insurance_no,
			patient_age, patient_mobile, doctor_name, amt_received, date,
			bill_no;
	Font font1 = new Font(Font.FontFamily.HELVETICA, 8.5f, Font.BOLD);

	Font font2 = new Font(Font.FontFamily.HELVETICA, 8.5f);
	Font font3 = new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD);
	String dateFrom="2013-01-01",dateTo="2019-01-01",patientID="0000130000001";
	double totalAmount1=0,totalAmount2=0;
	public static String RESULT = "opdslip1.pdf";
	String[] open = new String[4];
	PdfWriter writer;
	int counter=0;

	public static void main(String... args) {

//		try {
//			new PatientsTotalDataBill("0000130000001","2014-09-26", "2017-10-26");
//		} catch (FileNotFoundException | DocumentException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public PatientsESIDataBill(HashMap<String,String> hm,String patient_id,String dateFrom, String dateTo) throws FileNotFoundException, DocumentException {
		
		this.dateFrom=dateFrom;
		this.dateTo=dateTo;
		patientID=patient_id;
		getPatientDetail(patientID);
		RESULT = "PatientFile/" + patientID + ".pdf";
		// create document
		Document document = new Document(PageSize.A4, 36, 36, 90, 36);
		writer = PdfWriter.getInstance(document,
				new FileOutputStream(RESULT));

		// add header and footer
		HeaderFooter event = new HeaderFooter();
		writer.setPageEvent(event);

		// write to document

		document.open();
		document.add(new Paragraph("  "));

		// text in center
		Paragraph paragraph = new Paragraph(
				"To be used by tie-up hospital (P-III)", new Font(
						Font.FontFamily.HELVETICA, 10f, Font.BOLD
								| Font.UNDERLINE));
		paragraph.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph);

		// text in center
		Paragraph paragraph1 = new Paragraph("Consolidated Bill Format",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		document.add(new Paragraph("  "));

		PdfPTable table = new PdfPTable(4);
		float[] columnWidths = new float[] { 15f, 20f, 15f, 20f };
		table.setWidths(columnWidths);
		table.setWidthPercentage(100);
		table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(new Phrase("Bill No", font1));
		table.addCell(new Phrase(""+hm.get(PatientHistoryESI.BILL_NUMBER_KEY), font1));
		table.addCell(new Phrase(new Chunk("Date Of Submission:", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryESI.SUBMMISSION_DATE_KEY), font1));

		table.addCell(new Phrase(new Chunk("Name Of IP", font1)));
		table.addCell(new Phrase("" + hm.get(PatientHistoryESI.PATIENT_NAME_KEY), font1));
		table.addCell(new Phrase(new Chunk("Patient Name", font1)));
		table.addCell(new Phrase("" + patient_name, font1));

		table.addCell(new Phrase(new Chunk("IP No", font1)));
		table.addCell(new Phrase("" + patient_insurance_no, font1));
		table.addCell(new Phrase(new Chunk("Age/Sex", font1)));
		table.addCell(new Phrase("" + patient_age + "/" + patient_sex, font1));

		table.addCell(new Phrase(new Chunk("Date of Referral", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryESI.REFERRAL_DATE_KEY), font1));
		table.addCell(new Phrase(new Chunk("Address", font1)));
		table.addCell(new Phrase(patient_address, font1));

		table.addCell(new Phrase(new Chunk("Diagnosis", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryESI.DIAGNOSIS_KEY), font1));
		table.addCell(new Phrase(new Chunk("Phone no.", font1)));
		table.addCell(new Phrase(patient_mobile, font1));

		table.addCell(new Phrase(new Chunk("Referred by hospital: ", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryESI.REFERRED_BY_KEY), font1));
		table.addCell(new Phrase(new Chunk(" ", font1)));
		table.addCell(new Phrase(" ", font1));
		document.add(table);

		// second table

		document.add(new Paragraph("  "));
		document.add(new Paragraph("I. Existing in the pakage rate list's:",
				font1));
		document.add(new Paragraph("  "));

		PdfPTable table1 = new PdfPTable(8);
		float[] columnWidths1 = new float[] { 10f, 20f, 10f, 20f, 10f, 20f,
				10f, 20f };
		table1.setWidths(columnWidths1);
		table1.setWidthPercentage(100);
		// table1.getDefaultCell().setBorder(Rectangle.);
		table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table1.addCell(new Phrase("S.No", font1));
		table1.addCell(new Phrase("Diag./Procedure for which referred", font1));
		table1.addCell(new Phrase(new Chunk("CGHS Code No.", font1)));
		table1.addCell(new Phrase("CGHS Page No.", font1));
		table1.addCell(new Phrase(new Chunk("Other if not in CGHS rate list",
				font1)));
		table1.addCell(new Phrase("Amount claimed with date", font1));
		table1.addCell(new Phrase(new Chunk("Amount entitled with date", font1)));
		table1.addCell(new Phrase("Re-marks", font1));


		addIPDData(table1);
		examData(table1);
		opdData(table1);
		miscData(table1);
		
		table1.addCell(new Phrase(new Chunk("", font2)));
		table1.addCell(new Phrase("", font2));
		table1.addCell(new Phrase(new Chunk("", font2)));
		table1.addCell(new Phrase("", font2));
		table1.addCell(new Phrase(new Chunk("Total", font1)));
		table1.addCell(new Phrase(""+totalAmount1, font1));
		table1.addCell(new Phrase(new Chunk(""+totalAmount2, font1)));
		table1.addCell(new Phrase("", font2));
		
		document.add(table1);

		// third table

		document.add(new Paragraph("  "));
		document.add(new Paragraph(
				"II. (Non-package Rates) for procedures done (not existing in the list of package rates)",
				font1));
		document.add(new Paragraph("  "));

		PdfPTable table2 = new PdfPTable(8);
		float[] columnWidths2 = new float[] { 10f, 20f, 10f, 20f, 10f, 20f,
				10f, 20f };
		table2.setWidths(columnWidths2);
		table2.setWidthPercentage(100);
		// table1.getDefaultCell().setBorder(Rectangle.);
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table2.addCell(new Phrase("S.No", font1));
		table2.addCell(new Phrase("Diag./Procedure for which referred", font1));
		table2.addCell(new Phrase(new Chunk("CGHS Code No.", font1)));
		table2.addCell(new Phrase("CGHS Page No.", font1));
		table2.addCell(new Phrase(new Chunk("Other if not in CGHS rate list",
				font1)));
		table2.addCell(new Phrase("Amount claimed with date", font1));
		table2.addCell(new Phrase(new Chunk("Amount entitled with date", font1)));
		table2.addCell(new Phrase("Re-marks", font1));

		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));

		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk("   ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));

		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase("   ", font2));
		table2.addCell(new Phrase(new Chunk("   ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));
		table2.addCell(new Phrase(new Chunk(" ", font2)));
		table2.addCell(new Phrase(" ", font2));

		document.add(table2);

		document.add(new Paragraph("  "));
		document.add(new Paragraph(
				"Charges of Implants / Device and___________________________________",
				new Font(Font.FontFamily.HELVETICA, 10f)));
		document.add(new Paragraph(
				"Amount Claimed ____________________Rs._____________________________",
				new Font(Font.FontFamily.HELVETICA, 10f)));
		document.add(new Paragraph("  "));
		document.add(new Paragraph(
				"Additional procedure done with rationale and documented permissions",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE)));
		document.add(new Paragraph("  "));

		// fourth table
		PdfPTable table3 = new PdfPTable(8);
		float[] columnWidths3 = new float[] { 10f, 20f, 10f, 20f, 10f, 20f,
				10f, 20f };
		table3.setWidths(columnWidths3);
		table3.setWidthPercentage(100);
		// table1.getDefaultCell().setBorder(Rectangle.);
		table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table3.addCell(new Phrase("S.No", font1));
		table3.addCell(new Phrase("Diag./Procedure for which referred", font1));
		table3.addCell(new Phrase(new Chunk("CGHS Code No.", font1)));
		table3.addCell(new Phrase("CGHS Page No.", font1));
		table3.addCell(new Phrase(new Chunk("Other if not in CGHS rate list",
				font1)));
		table3.addCell(new Phrase("Amount claimed with date", font1));
		table3.addCell(new Phrase(new Chunk("Amount entitled with date", font1)));
		table3.addCell(new Phrase("Re-marks", font1));

		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));

		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk("   ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));

		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase("   ", font2));
		table3.addCell(new Phrase(new Chunk("   ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));
		table3.addCell(new Phrase(new Chunk(" ", font2)));
		table3.addCell(new Phrase(" ", font2));

		document.add(table3);

		// line separator
		LineSeparator ls = new LineSeparator();
		document.add(new Chunk(ls));

		document.add(new Paragraph("  "));
		document.add(new Paragraph(
				"Total Amount Claimed(I+II+III)Rs. "+totalAmount1+"("+NumberToWordConverter.convert((int)totalAmount1)+" Only/-)",
				font3));
		document.add(new Paragraph(
				"Total Amount Admitted(X)(I+II+III) Rs. _______________________________________________",
				font3));

//		// new page
//		document.newPage();
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Remarks", font3));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(
				"      The charge in the bill has/have been claimed as per the conditions laid down in agreement.",
				font3));
		document.add(new Paragraph(
				"     No money has been received/demanded/charged from the patient/his/her/relative.",
				font3));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		// add text left and right in same line
		Chunk glue = new Chunk(new VerticalPositionMark());
		Paragraph p = new Paragraph(
				"Sign./tumb Impression of patient with date", font2);
		p.add(new Chunk(glue));
		p.add("Sign.& Stamp Of Authorized Signatory");
		document.add(p);

		// line separator
		document.add(new Chunk(ls));

		// text in center
		Paragraph paragraph2 = new Paragraph("(For official use of ESIS)",
				new Font(Font.FontFamily.HELVETICA, 8f, Font.BOLD));
		paragraph2.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph2);

		document.add(new Paragraph(" "));

		// add text left and right in same line
		Chunk glue2 = new Chunk(new VerticalPositionMark());
		Paragraph p2 = new Paragraph(" ", font3);
		p2.add(new Chunk(glue2));
		p2.add("Total Amt Payable:           ");
		document.add(p2);

		// add text left and right in same line
		Chunk glue3 = new Chunk(new VerticalPositionMark());
		Paragraph p3 = new Paragraph(" ", font3);
		p3.add(new Chunk(glue3));
		p3.add("Date of Payment               ");
		document.add(p3);

		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		// add text left and right in same line
		Chunk glue1 = new Chunk(new VerticalPositionMark());
		Paragraph p1 = new Paragraph("Signature Of dealing Assistant", font3);
		p1.add(new Chunk(glue1));
		p1.add("Signature Of Supreintendent");
		document.add(p1);

		// line separator
		document.add(new Chunk(ls));

		document.add(new Paragraph("Note :", new Font(
				Font.FontFamily.HELVETICA, 10f, Font.BOLD | Font.UNDERLINE)));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(
				"Bill According To : Nabhi's Compendium Of Orders Under Cghs 15th Revised Edition, April,2014 _____________________",
				new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
		document.add(new Paragraph(
				"The amount may be credited to our account no 012088700000271 RTGS no YesB0000120 and intimate the same through email/fax/hardcopy at the address."));
		
		
		document.newPage();

		
		paragraph1 = new Paragraph("Date : "
				+ hm.get(PatientHistoryRailway.SUBMMISSION_DATE_KEY));
		paragraph1.setAlignment(Element.ALIGN_RIGHT);
		document.add(paragraph1);
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Bill No. "
				+ hm.get(PatientHistoryRailway.BILL_NUMBER_KEY), new Font(
				Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph("From :", new Font(
				Font.FontFamily.HELVETICA, 10)));

		document.add(new Paragraph("Medical Superintendent", new Font(
				Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph("Rotary Ambala Cancer & General Hospital",
				new Font(Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph("Ambala Cantt.", new Font(
				Font.FontFamily.HELVETICA, 10)));

		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));

		document.add(new Paragraph("To,", new Font(Font.FontFamily.HELVETICA,
				10)));
		document.add(new Paragraph("The Chief Medical Superintendent",
				new Font(Font.FontFamily.HELVETICA, 10)));

		document.add(new Paragraph("Northern Railway Divisional Hospital,",
				new Font(Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph("Ambala Cantt.", new Font(
				Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		paragraph1 = new Paragraph(
				"Sub : Submission of Medical bill for reimbursement", new Font(
						Font.FontFamily.HELVETICA, 12f, Font.BOLD
								| Font.UNDERLINE));
		document.add(paragraph1);
		document.add(new Paragraph("Sir", new Font(Font.FontFamily.HELVETICA,
				10)));

		document.add(new Paragraph(
				"Kindly refer to the the above mentioned subject. A bill of Rs."
						+ totalAmount1
						+ "/- ("
						+ NumberToWordConverter.convert((int) totalAmount1)
						+ " only)"
						+ " pertaining to the treatment of patient "
						+ patient_name
						+ ", "
						+ patient_address
						+ " referral No. "
						+ hm.get(PatientHistoryRailway.REF_NO_BY_KEY)
						+ ""
						+ " is being submitted in duplicate copy. You are requested to kindly release the payment at the earlies."
						+ "", new Font(Font.FontFamily.HELVETICA, 10)));

		document.add(new Paragraph("Detail of Pages attached in the bill",
				new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD)));
		document.add(new Paragraph(" "));
		PdfPTable attachementTable = new PdfPTable(3);
		float[] attachementTablecolumnWidths = new float[] { 10f, 20f, 10f };
		attachementTable.setWidths(attachementTablecolumnWidths);
		attachementTable.setWidthPercentage(100);

		attachementTable.addCell(new Phrase("S.No", font1));
		attachementTable.addCell(new Phrase("Attached Documnet Detail", font1));
		attachementTable.addCell(new Phrase("Attached(Yes/NA)", font1));

		attachementTable.addCell(new Phrase("1", font1));
		attachementTable.addCell(new Phrase("Bill Details", font1));
		attachementTable.addCell(getCell());

	
		attachementTable.addCell(new Phrase("2", font1));
		attachementTable.addCell(new Phrase("P1 Letter", font1));
		attachementTable.addCell(getCell());

		
		attachementTable.addCell(new Phrase("3", font1));
		attachementTable.addCell(new Phrase(
				"Approval From ESI", font1));
		attachementTable.addCell(getCell());

	
		attachementTable.addCell(new Phrase("4", font1));
		attachementTable.addCell(new Phrase("ESI OPD Slip", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("5", font1));
		attachementTable.addCell(new Phrase("Employee Detail", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("6", font1));
		attachementTable.addCell(new Phrase("Employee Entitlement", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("7", font1));
		attachementTable.addCell(new Phrase("ESI Card", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("8", font1));
		attachementTable.addCell(new Phrase("Aadhar Card", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("9", font1));
		attachementTable.addCell(new Phrase("Affidavit", font1));
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("10", font1));
		attachementTable.addCell(new Phrase("Lab Report", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("11", font1));
		attachementTable.addCell(new Phrase("USG Report", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("12", font1));
		attachementTable.addCell(new Phrase("X-Ray Report", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("13", font1));
		attachementTable.addCell(new Phrase("ECG", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("14", font1));
		attachementTable.addCell(new Phrase("Doctor Notes", font1));
		attachementTable.addCell(getCell());

		
		attachementTable.addCell(new Phrase("15", font1));
		attachementTable.addCell(new Phrase("Anesthesia Notes", font1));
		attachementTable.addCell(getCell());
		
		
		attachementTable.addCell(new Phrase("16", font1));
		attachementTable.addCell(new Phrase("Opertaion Notes", font1));
		attachementTable.addCell(getCell());
		
		
		attachementTable.addCell(new Phrase("17", font1));
		attachementTable.addCell(new Phrase("Discharge Card", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("18", font1));
		attachementTable.addCell(new Phrase("Satisfactory", font1));
		attachementTable.addCell(getCell());
		
		attachementTable.addCell(new Phrase("19", font1));
		attachementTable.addCell(getCellBlank());
		attachementTable.addCell(getCell());

		attachementTable.addCell(new Phrase("20", font1));
		attachementTable.addCell(getCellBlank());
		attachementTable.addCell(getCell());

		document.add(attachementTable);
		
		
		
		
		
		
		document.newPage();

		
		
		paragraph1 = new Paragraph("SATISFACTORY TREATMENT ECHS PATIENS",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		

		
		document.add(new Paragraph("  "));
		
		
		PdfPTable table4 = new PdfPTable(3);
		float[] columnWidths4 = new float[] {5f, 15f, 20f};
		table4.setWidths(columnWidths4);
		table4.setWidthPercentage(100);
		//table4.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

	
		table4.addCell(new Phrase("1", font1));
		table4.addCell(new Phrase(new Chunk("Name of Patient", font1)));
		table4.addCell(new Phrase(patient_name, font1));
		
		table4.addCell(new Phrase("2", font1));
		table4.addCell(new Phrase("ESI Referral No", font1));
		table4.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.REF_NO_BY_KEY), font1));
		
		table4.addCell(new Phrase("3", font1));
		table4.addCell(new Phrase(new Chunk("Dianosis", font1)));
		table4.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DIAGNOSIS_KEY), font1));
		
		table4.addCell(new Phrase("4", font1));
		table4.addCell(new Phrase(new Chunk("Were the tests/Procedures undertaken as per referral", font1)));
		table4.addCell(getCellBlank());
		
		
		table4.addCell(new Phrase("5", font1));
		table4.addCell(new Phrase(new Chunk("Date of Addmission", font1)));
		table4.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.ADMISSION_DATE_BY_KEY), font1));
		
		
		table4.addCell(new Phrase("6", font1));
		table4.addCell(new Phrase(new Chunk("Date of Discharge", font1)));
		table4.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DISCHARGE_DATE_BY_KEY), font1));

	
		table4.addCell(new Phrase("7", font1));
		table4.addCell(new Phrase(new Chunk("opd vISIT dATE", font1)));
		table4.addCell(getCellBlank());
		
		
		document.add(table4);
		document.add(new Paragraph("I am satified with treatment/investigation carried out at the above institution."));
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Patient Sign.________________________",
				new Font(Font.FontFamily.HELVETICA, 10)));
		
		document.add(new Paragraph("Mobile No.   ________________________",
				new Font(Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph(" "));

		
		
		
		
		
		
		
		
		
		
		
		document.close();
		
		new OpenFile(RESULT);
	}
	public PdfPCell getCell() {
		TextField tf = new TextField(writer, new Rectangle(64, 785, 340, 800),
				"fn"+counter);
		tf.setFontSize(10);
		tf.setText("Page No. ");
		tf.setBackgroundColor(BaseColor.WHITE);
		FieldPositioningEvents events = null;
		try {
			events = new FieldPositioningEvents(writer, tf.getTextField());
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c2 = new PdfPCell();
		c2.setCellEvent(events);
		counter++;
		return c2;
	}
	public PdfPCell getCellBlank() {
		TextField tf = new TextField(writer, new Rectangle(20, 20, 20, 200),
				"fn"+counter);
		tf.setFontSize(10);
		tf.setText("");
		tf.setBackgroundColor(BaseColor.WHITE);
		FieldPositioningEvents events = null;
		try {
			events = new FieldPositioningEvents(writer, tf.getTextField());
		} catch (IOException | DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		PdfPCell c2 = new PdfPCell();
		c2.setCellEvent(events);
		counter++;
		return c2;
	}
	public void addIPDData(PdfPTable table1) {
		
		
			try {
				IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
				ResultSet rs = db1.retrieveAllDataExpense(patient_id, dateFrom,dateTo);

			
				while (rs.next()) {
					r++;			
					table1.addCell(new Phrase(new Chunk(""+r, font2)));
					table1.addCell(new Phrase(rs.getObject(2).toString(), font2));
					table1.addCell(new Phrase(new Chunk(rs.getObject(6).toString(), font2)));
					table1.addCell(new Phrase(" ", font2));
					table1.addCell(new Phrase(new Chunk(" ", font2)));
					table1.addCell(new Phrase(rs.getObject(5).toString(), font2));
					totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
					table1.addCell(new Phrase(new Chunk(rs.getObject(5).toString(), font2)));
					totalAmount2=totalAmount2+Double.parseDouble(rs.getObject(5).toString());
					table1.addCell(new Phrase(" ", font2));
					
				//	System.out.println(""+rs.getObject(3).toString());
				}
			} catch (SQLException ex) {
				Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
						null, ex);
			}	

	}
	
	public void examData(PdfPTable table1) {

		
		try {
			ExamDBConnection db1 = new ExamDBConnection();
			ResultSet rs = db1.retrieveTestPatientExams2(dateFrom,dateTo,patientID);
			
			while (rs.next()) {
				r++;
				
				table1.addCell(new Phrase(new Chunk(""+r, font2)));
				table1.addCell(new Phrase(""+ rs.getObject(3).toString(), font2));
				table1.addCell(new Phrase(new Chunk(""+ rs.getObject(2).toString(), font2)));
				table1.addCell(new Phrase(""+ rs.getObject(6).toString(), font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				table1.addCell(new Phrase(""+ rs.getObject(5).toString(), font2));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(new Chunk(""+ rs.getObject(5).toString(), font2)));
				totalAmount2=totalAmount2+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(" ", font2));
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	
	
	public void opdData(PdfPTable table1) {

		try {
			OPDDBConnection db1 = new OPDDBConnection();
			ResultSet rs = db1.retrieveAllDataPatientOpd(dateFrom,dateTo,patient_id);
			
			while (rs.next()) {
				r++;
				
				table1.addCell(new Phrase(new Chunk(""+r, font2)));
				table1.addCell(new Phrase(""+ rs.getObject(3).toString(), font2));
				table1.addCell(new Phrase(new Chunk("   ", font2)));
				table1.addCell(new Phrase(" ", font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				table1.addCell(new Phrase(""+ rs.getObject(5).toString(), font2));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(new Chunk(""+ rs.getObject(5).toString(), font2)));
				totalAmount2=totalAmount2+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(" ", font2));
				
				
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
	}
	
	
	
	public void miscData(PdfPTable table1) {

		
		try {
			MiscDBConnection db1 = new MiscDBConnection();
			ResultSet rs = db1.retrieveAllPatientWiseMiscData(dateFrom,dateTo,patient_id);

			while (rs.next()) {
				r++;
				
				table1.addCell(new Phrase(new Chunk(""+r, font2)));
				table1.addCell(new Phrase("" + rs.getObject(2).toString(), font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				table1.addCell(new Phrase("", font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				table1.addCell(new Phrase("" + rs.getObject(4).toString(), font2));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(4).toString());
				table1.addCell(new Phrase(new Chunk("" + rs.getObject(4).toString(), font2)));
				totalAmount2=totalAmount2+Double.parseDouble(rs.getObject(4).toString());
				table1.addCell(new Phrase("", font2));

			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	
	}
	
	
	

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);
			patient_id=indexId;
			while (resultSet.next()) {

				patient_name = resultSet.getObject(1).toString();
				patient_age = resultSet.getObject(2).toString();
				patient_sex = resultSet.getObject(3).toString();
				patient_address = resultSet.getObject(4).toString();
				patient_mobile = resultSet.getObject(6).toString();
				patient_insurancetype = resultSet.getObject(7).toString();
				patient_insurance_no = resultSet.getObject(8).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age + "  " + indexId);
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
	

}