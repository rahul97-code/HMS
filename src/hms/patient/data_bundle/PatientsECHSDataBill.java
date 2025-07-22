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
import hms.patient.data_bundle.PatientHistoryECHS;
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

public class PatientsECHSDataBill {

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

	public PatientsECHSDataBill(HashMap<String,String> hm,String patient_id,String dateFrom, String dateTo) throws FileNotFoundException, DocumentException {
		
		this.dateFrom=dateFrom;
		this.dateTo=dateTo;
		patientID=patient_id;
		getPatientDetail(patientID);
		RESULT = "PatientFile/" + patientID + ".pdf";
		// create document
		Document document = new Document(PageSize.A4, 36, 36, 100, 36);
		writer = PdfWriter.getInstance(document,
				new FileOutputStream(RESULT));

		// add header and footer
		HeaderFooter event = new HeaderFooter();
		writer.setPageEvent(event);

		// write to document

		document.open();
		document.add(new Paragraph("  "));

	

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
		//table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(new Phrase("Bill No", font1));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.BILL_NUMBER_KEY), font1));
		table.addCell(new Phrase(new Chunk("Bill Date:", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.SUBMMISSION_DATE_KEY), font1));

		table.addCell(new Phrase(new Chunk("Name Of IP", font1)));
		table.addCell(new Phrase("" + hm.get(PatientHistoryECHS.PATIENT_NAME_KEY), font1));
		table.addCell(new Phrase(new Chunk("Patient Name", font1)));
		table.addCell(new Phrase("" + patient_name, font1));

		table.addCell(new Phrase(new Chunk("TPA: ", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.REFERRED_BY_KEY), font1));
		table.addCell(new Phrase(new Chunk("Date of Referral", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.REFERRAL_DATE_KEY), font1));
		
		table.addCell(new Phrase(new Chunk("Address", font1)));
		table.addCell(new Phrase(patient_address, font1));
		table.addCell(new Phrase(new Chunk("Referral No.", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.REF_NO_BY_KEY), font1));
		
		
		table.addCell(new Phrase(new Chunk("IPD No.", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.IPD_NO_BY_KEY), font1));
		table.addCell(new Phrase(new Chunk("Date if Admission", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.ADMISSION_DATE_BY_KEY), font1));
		
		table.addCell(new Phrase(new Chunk("Room Type", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.TYPE_BY_KEY), font1));
		table.addCell(new Phrase(new Chunk("Date if Discharge", font1)));
		table.addCell(new Phrase(hm.get(PatientHistoryECHS.DISCHARGE_DATE_BY_KEY), font1));
		
		table.addCell(new Phrase(new Chunk("Pan No.", font1)));
		table.addCell(new Phrase("AABAR5319R", font1));
		
		table.addCell(new Phrase(new Chunk("Consultant", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.DOCTOR_NAME_KEY) , font1));

	
	

		table.addCell(new Phrase(new Chunk("Diagnosis", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.DIAGNOSIS_KEY), font1));
		table.addCell(new Phrase(new Chunk("Med. Card No.", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.CARD_NO_KEY), font1));
		
		
		table.addCell(new Phrase(new Chunk("ECHC", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_1)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_1), font1));
		table.addCell(new Phrase(new Chunk("Bill Pages No.", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_2)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_2), font1));
		
		
		table.addCell(new Phrase(new Chunk("ECHC Smart Card", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_3)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_3), font1));
		table.addCell(new Phrase(new Chunk("Satisfaction Certificate", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_4)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_4), font1));
		
		
		table.addCell(new Phrase(new Chunk("Discharge Summery", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_5)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_5), font1));
		table.addCell(new Phrase(new Chunk("Investigation Reports", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_6)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_6), font1));
		
		table.addCell(new Phrase(new Chunk("Summery", font1)));
		table.addCell(new Phrase(""+hm.get(PatientHistoryECHS.ATTACHEMNT_7)+" Page No. "+hm.get(PatientHistoryECHS.PAGE_NUMBER_7), font1));
		table.addCell(new Phrase(new Chunk(" ", font1)));
		table.addCell(new Phrase(" ", font1));
		
		
		document.add(table);

		// second table

		document.add(new Paragraph("  "));
	
		PdfPTable table1 = new PdfPTable(8);
		float[] columnWidths1 = new float[] { 10f, 20f, 10f, 20f, 10f, 20f,
				10f, 20f };
		table1.setWidths(columnWidths1);
		table1.setWidthPercentage(100);
		// table1.getDefaultCell().setBorder(Rectangle.);
		table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table1.addCell(new Phrase("S.No", font1));
		table1.addCell(new Phrase("Perticulars", font1));
		table1.addCell(new Phrase("CGHS Page No.", font1));
		table1.addCell(new Phrase(new Chunk("CGHS Code No.", font1)));
		table1.addCell(new Phrase(new Chunk("Amount",
				font1)));
		table1.addCell(new Phrase("Times", font1));
		table1.addCell(new Phrase(new Chunk("Discount(Room Type+Hospital Discount)", font1)));
		table1.addCell(new Phrase("Final Amount", font1));


		addIPDData(table1);
		examData(table1);
		opdData(table1);
		miscData(table1);
		PdfPCell cell = new PdfPCell(new Phrase("Total",font1));
		cell.setColspan(7);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table1.addCell(cell);
		table1.addCell(new Phrase(new Chunk(""+totalAmount2, font1)));
		
		
		document.add(table1);
		document.add(new Paragraph(" "));

		document.add(new Paragraph("(Final Amount in words) Amount Claimed : "+NumberToWordConverter.convert((int)totalAmount2)+" only", new Font(
				Font.FontFamily.HELVETICA, 10f, Font.BOLD )));
		document.add(new Paragraph(" "));
//		document.add(new Paragraph(
//				"Bank Detail:",
//				new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD)));
//		document.add(new Paragraph(
//				"Bank Name : Yes Bank Ltd.",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
//		document.add(new Paragraph(
//				"Address : Opp. Civil Hospital, Ambala Cantt",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
//		document.add(new Paragraph(
//				"IFSC Code : YESB0000120",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
//		document.add(new Paragraph(
//				"A/C No. : 021088700000271",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
//		document.add(new Paragraph(
//				"Pan No. : AABAR5319R",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
//		document.add(new Paragraph(
//				"GST No. : Not applicable in our hospital (Tax Exempted Hospital)",
//				new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));
		
document.newPage();

		
		
		paragraph1 = new Paragraph("COVER NOTE",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		
		paragraph1 = new Paragraph("ECSH, Polclinic, AMBALA ONLINE",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		
		paragraph1 = new Paragraph("REFERAL Empanelled Hospital/REMIMBURSEMENT TO PATIENT",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		
		document.add(new Paragraph("  "));
		
		
		PdfPTable table2 = new PdfPTable(3);
		float[] columnWidths2 = new float[] {5f, 15f, 20f};
		table2.setWidths(columnWidths2);
		table2.setWidthPercentage(100);
		//table2.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		table2.addCell(new Phrase("1", font1));
		table2.addCell(new Phrase("ECHS Regstration No.", font1));
		table2.addCell(getCellBlank());
		
		table2.addCell(new Phrase("2", font1));
		table2.addCell(new Phrase(new Chunk("Service No. Rank & Name", font1)));
		table2.addCell(getCellBlank());

		
		table2.addCell(new Phrase("2a", font1));
		table2.addCell(new Phrase(new Chunk("Date of Membership", font1)));
		table2.addCell(getCellBlank());

		table2.addCell(new Phrase("2b", font1));
		table2.addCell(new Phrase(new Chunk("Name of Patient", font1)));
		table2.addCell(new Phrase(patient_name, font1));
		
		table2.addCell(new Phrase("3", font1));
		table2.addCell(new Phrase(new Chunk("Dianosis", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DIAGNOSIS_KEY), font1));
		
		
		table2.addCell(new Phrase("4", font1));
		table2.addCell(new Phrase(new Chunk("Which Referred", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.REFERRED_BY_KEY), font1));
		
		table2.addCell(new Phrase("5", font1));
		table2.addCell(new Phrase("ECHS Referral No", font1));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.REF_NO_BY_KEY), font1));
		
		table2.addCell(new Phrase("6", font1));
		table2.addCell(new Phrase(new Chunk("Date of Referral", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.REFERRAL_DATE_KEY), font1));
		
		table2.addCell(new Phrase("7", font1));
		table2.addCell(new Phrase(new Chunk("Hospital/Clinic/Lab/Diagnostic", font1)));
		table2.addCell(new Phrase("Rotary Ambala Cancer & General Hospital, Ambala", font1));
		
		table2.addCell(new Phrase("8", font1));
		table2.addCell(new Phrase(new Chunk("Date of Addmission", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.ADMISSION_DATE_BY_KEY), font1));
		
		
		table2.addCell(new Phrase("9", font1));
		table2.addCell(new Phrase(new Chunk("Date of Discharge", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DISCHARGE_DATE_BY_KEY), font1));

		int[] bedHours = DateFormatChange
				.calulateHoursAndDays_BetweenDates(
						DateFormatChange.StringToDate( hm.get(PatientHistoryRailway.ADMISSION_DATE_BY_KEY)), DateFormatChange.StringToDate( hm.get(PatientHistoryRailway.DISCHARGE_DATE_BY_KEY)));
		
		table2.addCell(new Phrase("10", font1));
		table2.addCell(new Phrase(new Chunk("No. of Days", font1)));
		table2.addCell(new Phrase(""+bedHours[0], font1));
		
		table2.addCell(new Phrase("11", font1));
		table2.addCell(new Phrase(new Chunk("Were the tests/Procedures undertaken as per referral", font1)));
		table2.addCell(getCellBlank());
		
		
		table2.addCell(new Phrase("12", font1));
		table2.addCell(new Phrase(new Chunk("Emergency was informed within 48 hours", font1)));
		table2.addCell(getCellBlank());
		
		
		table2.addCell(new Phrase("13", font1));
		table2.addCell(new Phrase(new Chunk("Reimbursemnet Claim is entitled", font1)));
		table2.addCell(getCellBlank());
		
		table2.addCell(new Phrase("14", font1));
		table2.addCell(new Phrase(new Chunk("TA Claim is entitled(Attach SEMO Saction)", font1)));
		table2.addCell(getCellBlank());
		

		table2.addCell(new Phrase("15", font1));
		table2.addCell(new Phrase(new Chunk("OPD/IPD No.", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.IPD_NO_BY_KEY), font1));
		
		table2.addCell(new Phrase("16", font1));
		table2.addCell(new Phrase(new Chunk("Bill No.", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.BILL_NUMBER_KEY), font1));
		
		table2.addCell(new Phrase("17", font1));
		table2.addCell(new Phrase(new Chunk("Bill Received in Polyclinic on", font1)));
		table2.addCell(new Phrase("-------------", font1));
		
		table2.addCell(new Phrase("18", font1));
		table2.addCell(new Phrase(new Chunk("Bill Date", font1)));
		table2.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.SUBMMISSION_DATE_KEY), font1));
		
		table2.addCell(new Phrase("19", font1));
		table2.addCell(new Phrase(new Chunk("Remarks/Others", font1)));
		table2.addCell(getCellBlank());
		
		
		document.add(table2);
		document.add(new Paragraph(" "));
		document.add(new Paragraph(" "));
		document.add(new Paragraph("Patient Sign.________________________",
				new Font(Font.FontFamily.HELVETICA, 10)));
		
		document.add(new Paragraph("Mobile No.   ________________________",
				new Font(Font.FontFamily.HELVETICA, 10)));
		document.add(new Paragraph(" "));
		
		
		
		
		
		
		
		document.newPage();

		
		
		paragraph1 = new Paragraph("SATISFACTORY TREATMENT ECHS PATIENS",
				new Font(Font.FontFamily.HELVETICA, 10f, Font.BOLD
						| Font.UNDERLINE));
		paragraph1.setAlignment(Element.ALIGN_CENTER);
		document.add(paragraph1);
		

		
		document.add(new Paragraph("  "));
		
		
		PdfPTable table3 = new PdfPTable(3);
		float[] columnWidths3 = new float[] {5f, 15f, 20f};
		table3.setWidths(columnWidths3);
		table3.setWidthPercentage(100);
		//table3.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

	
		table3.addCell(new Phrase("1", font1));
		table3.addCell(new Phrase(new Chunk("Name of Patient", font1)));
		table3.addCell(new Phrase(patient_name, font1));
		
		table3.addCell(new Phrase("2", font1));
		table3.addCell(new Phrase("ECHS Referral No", font1));
		table3.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.REF_NO_BY_KEY), font1));
		
		table3.addCell(new Phrase("3", font1));
		table3.addCell(new Phrase(new Chunk("Dianosis", font1)));
		table3.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DIAGNOSIS_KEY), font1));
		
		table3.addCell(new Phrase("4", font1));
		table3.addCell(new Phrase(new Chunk("Were the Tests/Procedures undertaken as per referral", font1)));
		table3.addCell(getCellBlank());
		
		
		table3.addCell(new Phrase("5", font1));
		table3.addCell(new Phrase(new Chunk("Date of Addmission", font1)));
		table3.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.ADMISSION_DATE_BY_KEY), font1));
		
		
		table3.addCell(new Phrase("6", font1));
		table3.addCell(new Phrase(new Chunk("Date of Discharge", font1)));
		table3.addCell(new Phrase(""
				+ hm.get(PatientHistoryRailway.DISCHARGE_DATE_BY_KEY), font1));

	
		table3.addCell(new Phrase("7", font1));
		table3.addCell(new Phrase(new Chunk("OPD Visit Date", font1)));
		table3.addCell(getCellBlank());
		
		
		document.add(table3);
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
					table1.addCell(new Phrase("-", font2));
					table1.addCell(new Phrase(rs.getObject(5).toString(), font2));
					table1.addCell(new Phrase(new Chunk(" ", font2)));
					totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
					table1.addCell(new Phrase(new Chunk("10%", font2)));
					double amount=Double.parseDouble(rs.getObject(5).toString());
					double temp=(amount*0.10);
					temp=amount-temp;
					totalAmount2=totalAmount2+(temp);
					table1.addCell(new Phrase(temp+"", font2));
					
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
				table1.addCell(new Phrase(""+ rs.getObject(6).toString(), font2));
				table1.addCell(new Phrase(new Chunk(""+ rs.getObject(2).toString(), font2)));
				table1.addCell(new Phrase(""+ rs.getObject(5).toString(), font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(new Chunk("", font2)));
				double amount=Double.parseDouble(rs.getObject(5).toString());
				double temp=(amount*0.10);
				temp=amount-temp;
				totalAmount2=amount;
				table1.addCell(new Phrase(amount+"", font2));
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
				table1.addCell(new Phrase(new Chunk("-", font2)));
				table1.addCell(new Phrase("-", font2));
				table1.addCell(new Phrase(""+ rs.getObject(5).toString(), font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(5).toString());
				table1.addCell(new Phrase(new Chunk("", font2)));
				double amount=Double.parseDouble(rs.getObject(5).toString());
				
				totalAmount2=amount;
				table1.addCell(new Phrase(amount+"", font2));
				
				
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
				table1.addCell(new Phrase(new Chunk("-", font2)));
				table1.addCell(new Phrase("-", font2));
				table1.addCell(new Phrase("" + rs.getObject(4).toString(), font2));
				table1.addCell(new Phrase(new Chunk("", font2)));
				totalAmount1=totalAmount1+Double.parseDouble(rs.getObject(4).toString());
				table1.addCell(new Phrase(new Chunk("", font2)));
				double amount=Double.parseDouble(rs.getObject(4).toString());
				
				totalAmount2=totalAmount2+(amount);
				table1.addCell(new Phrase(amount+"", font2));

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