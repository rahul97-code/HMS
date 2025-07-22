package hms.patient.slippdf;

import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.main.NumberToWordConverter;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.util.PDFMergerUtility;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class InsuranceIpdPdfSlip {

	// data storage vectors
	//User Define variables:
	static String DRUG_LICENCE="ABC",PAN_NO="RTKR05299A";
	String discharge_type="";

	double REBATE = 0;

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17, Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	private static Font HeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLDITALIC);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f, Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9, Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0, payableAmount = 0;
	String ipd_no_a, patient_id, patient_name, patient_address, patient_insurancetype, patient_claim_id,
	patient_insurance_no, patient_age, doctor_name, amt_received, date, bill_no;
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "", ipd_time = "", ipd_date_dis = "", ipd_time_dis = "",
			ipd_note = "", ipd_id = "", ward_name = "", building_name = "", bed_no = "Select Bed No",
			ward_incharge = "", ward_room = "", ward_code = "", descriptionSTR = "", charges = "", ipd_days, ipd_hours,
			ipd_minutes, ipd_expenses_id, emergency = "No";

	String mainDir = "",patient_mob="";
	Font font;
	//	float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f,1.1f,1.1f };
	String[] open = new String[4];
	private String insurance_category;



	public InsuranceIpdPdfSlip(String ipd_bill, String ipd_no, String doctorName, Vector<String> ITEM_NAME, Vector<String> ITEM_DESC, Vector<String> DATE, Vector<String> TYPE, Vector<String> ITEM_ID, Vector<String> PAGE_NO, Vector<String> MRP, Vector<Double> PER_ITEM_PRICE, Vector<String> QTY, Vector<Double> AMOUNT, Vector<String> BATCH, Vector<String> EXPIRY,double rebate)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		bill_no = ipd_bill;
		getIPDData(ipd_no);
		getPatientDetail(patient_id);
		readFile();
		this.ipd_id = ipd_no;
		this.REBATE=rebate;
		makeDirectory(ipd_no);

		Document document = new Document();

		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 10, 0);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI, BaseFont.EMBEDDED);
		font = new Font(base, 8f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = InsuranceIpdPdfSlip.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = InsuranceIpdPdfSlip.class.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase("ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
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
		PdfPCell addressCell = new PdfPCell(
				new Phrase("Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)", font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase("Telephone No. : 0171-2690009, Mobile No. : 09034056793", font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		PdfPCell spaceCell = new PdfPCell(new Paragraph(patient_insurancetype + " Polyclinic Ambala Online", font4));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);

		PdfPCell spaceCell1 = new PdfPCell(new Paragraph("PAN : "+PAN_NO, font4));
		spaceCell1.setBorder(Rectangle.NO_BORDER);
		spaceCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		spaceCell1.setPaddingTop(-13);
		spaceCell1.setPaddingBottom(5);
		table.addCell(spaceCell1);

//		PdfPCell spaceCell2 = new PdfPCell(new Paragraph("DRUG LICENSE : "+DRUG_LICENCE, font4));
//		spaceCell2.setBorder(Rectangle.NO_BORDER);
//		spaceCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
//		spaceCell2.setPaddingTop(-13);
//		spaceCell2.setPaddingBottom(5);
//		table.addCell(spaceCell2);

		PdfContentByte cb1 = wr.getDirectContent();
		BaseFont bf = BaseFont.createFont();

//		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
//		f1.setColor(BaseColor.GRAY);
//		Phrase phrase3 = new Phrase("PROVISIONAL", f1);
//		if (bill_no.equals("Provisional Bill"))
//			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3, 300, 400, 30);
//
//		cb1.saveState();
//		// cb1.setColorStroke(BaseColor.BLACK);
//		// cb1.rectangle(501, 657,80, 70);
//		cb1.stroke();
//		cb1.restoreState();

		float[] opdTablCellWidth = { 0.8f, 1.3f, 1f, 1f };
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
		if (emergency.equals("Yes")) {
			opdTable.addCell(new Phrase("" + ipd_no_a + " " + "(Emergency)", font3));
		} else {
			opdTable.addCell(new Phrase("" + ipd_no_a, font3));
		}

		opdTable.addCell(new Phrase("Patient Name: ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		opdTable.addCell(new Phrase("Doctor Incharge :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctorName, font3));
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);

		PdfPCell agecell = new PdfPCell(new Phrase("" + patient_age, font3));
		agecell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(new Phrase("Age : ", font3));
		opdTable.addCell(agecell);

		opdTable.addCell(new Phrase("Admissioin Date/Time : ", font3));
		opdTable.addCell(new Phrase("" + DateFormatChange.StringToDateFormat(ipd_date) + " / " + ipd_time, font3));

		opdTable.addCell(new Phrase("Address : ", font3));
		opdTable.addCell(new Phrase("" + patient_address, font3));

		opdTable.addCell(new Phrase("Discharge Date/Time : ", font3));
		opdTable.addCell(
				new Phrase("" + DateFormatChange.StringToDateFormat(ipd_date_dis) + " / " + ipd_time_dis, font3));

		opdTable.addCell(new Phrase("Insurance : ", font3));
		opdTable.addCell(new Phrase("" + patient_insurancetype, font3));

		//		opdTable.addCell(new Phrase("Bed No. : ", font3));
		//		opdTable.addCell(new Phrase(""+bed_no+" ("+ward_name+")", font3));
		opdTable.addCell(new Phrase("No. of Days", font3));
		opdTable.addCell(new Phrase("" + ipd_days + " Days, " + ipd_hours + " Hours", font3));

		opdTable.addCell(new Phrase("Patient Mob. : ", font3));
		opdTable.addCell(new Phrase("" +patient_mob , font3));

		opdTable.addCell(new Phrase("Discharge Type : ", font3));
		opdTable.addCell(new Phrase("" +discharge_type , font3));

		opdTable.addCell(new Phrase("Generated By:", font3));
		opdTable.addCell(new Phrase(MainLogin.userName1, font3));

		opdTable.addCell(new Phrase("Claim ID. : ", font3));
		opdTable.addCell(new Phrase("" + patient_claim_id, font3));

		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase(" ", font3));
		//		opdTable.addCell(new Phrase("Emergency:", font3));
		//		opdTable.addCell(new Phrase(MainLogin.userName1, font3));


		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		opdCell.setPaddingBottom(5);
		opdCell.setColspan(10);
		table.addCell(opdCell);

		table.addCell(new Phrase("  ", font3));
		table.addCell(AddAllHeaders());
		PdfPCell Main = new PdfPCell(table);
		Main.setBorder(Rectangle.NO_BORDER);
		Main.setColspan(10);

		float[] TablCellWidth = new float[15];
		TablCellWidth = new float[] { 0.7f, 3.4f, 1.2f, 0.7f, 0.9f, 1.1f, 1.2f, 1.2f, 0.9f };

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.setWidthPercentage(90);
		double amt=0;

		Table.addCell(Main);


		Table.setHeaderRows(1);
		int r = 1, B = 0, C = 0, M = 0, E = 0,CUN=0, O = 0, P = 0, R = 0,Z=0;
		for (int i = 0; i < ITEM_ID.size(); i++) {
			Object o3 = TYPE.get(i);
			if (o3.equals("B") && B == 0) {
				Table.addCell(Header("ACCOMODATION"));
				B++;
			}else if (o3.equals("C") && C == 0) {	
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("CONSULTATION"));
				amt=0;
				C++;
			}else if ((o3.equals("E")) && E == 0) {		
				Table.addCell(TotalAmountHeader(amt));				
				Table.addCell(Header("EXAMS"));
				amt=0;
				E++;
			} else if ((o3.equals("I") || o3.equals("M")) && M == 0) {		
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("PHARMACY"));
				amt=0;
				M++;
			}else if (o3.equals("O") && CUN == 0) {		
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("CONSUMABLES"));
				amt=0;
				CUN++;
			}  else if (o3.equals("R") && R == 0) {		
				Table.addCell(TotalAmountHeader(amt));				
				Table.addCell(Header("RETURNS"));
				amt=0;
				R++;
			} else if (o3.equals("P") && P == 0) {	
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("PROCEDURES"));
				amt=0;
				P++;
			} else if (o3.equals("S") && O == 0) {	
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("SURGERY"));
				amt=0;
				O++;
			} 
			else if (o3.equals("Z") && Z == 0) {	
				Table.addCell(TotalAmountHeader(amt));			
				Table.addCell(Header("SERVICES"));
				amt=0;
				Z++;
			} 

			Table.addCell(new Phrase("" + (i + 1), font3));
			Table.addCell(new Phrase(ITEM_NAME.get(i), font3));
			Table.addCell(new Phrase(DATE.get(i), font3));
			//Table.addCell(new Phrase(TYPE.get(i), font3));
			//Table.addCell(new Phrase(PAGE_NO.get(i), font3));
			Table.addCell(new Phrase(ITEM_NAME.get(i).contains(ITEM_ID.get(i)) ? "N/A" : ITEM_ID.get(i), font3));
			Table.addCell(new Phrase("" + (Math.round(PER_ITEM_PRICE.get(i) * 100.00) / 100.00), font3));
			Table.addCell(new Phrase(QTY.get(i), font3));
			Table.addCell(new Phrase(BATCH.get(i), font3));
			Table.addCell(new Phrase(EXPIRY.get(i), font3));
			Table.addCell(new Phrase(AMOUNT.get(i) + "", font3));
			amt+=AMOUNT.get(i);
			totalCharges+=AMOUNT.get(i);
			if (i+1==ITEM_ID.size()) {		
				Table.addCell(TotalAmountHeader(amt));	
			}

		}
		totalCharges=Math.round(totalCharges * 100.00) / 100.00;
		payableAmount=totalCharges;
		document.add(Table);

		PdfPTable table2 = new PdfPTable(1);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(90);

		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		PdfPCell total = new PdfPCell(new Paragraph(new Chunk("Total :     " + totalCharges, font3).setUnderline(0.5f, -2f)));
		total.setBorder(Rectangle.NO_BORDER);
		total.setHorizontalAlignment(Element.ALIGN_RIGHT);
		total.setPaddingTop(10);
		total.setPaddingBottom(5);
		table2.addCell(total);

		if (REBATE != 0) {
			payableAmount = totalCharges - (REBATE / 100.00) * totalCharges;

			PdfPCell REBATE = new PdfPCell(new Paragraph(new Chunk(" Rebate :     " + this.REBATE + "%", font3).setUnderline(0.5f, -2f)));
			REBATE.setBorder(Rectangle.NO_BORDER);
			REBATE.setHorizontalAlignment(Element.ALIGN_RIGHT);
			REBATE.setPaddingTop(2);
			REBATE.setPaddingBottom(5);
			table2.addCell(REBATE);
		}

		PdfPCell payable = new PdfPCell(
				new Paragraph(new Chunk(" Amount Payable :     " + payableAmount, font3).setUnderline(0.5f, -2f)));
		payable.setBorder(Rectangle.NO_BORDER);
		payable.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payable.setPaddingTop(2);
		payable.setPaddingBottom(1);
		table2.addCell(payable);

		System.out.println("payableAmount  :" + payableAmount);
		String neg1 = "";
		double cashTotal2 = 0;
		cashTotal2 = payableAmount;
		if (cashTotal2 < 0) {
			cashTotal2 = Math.abs(cashTotal2);
			neg1 = "-";
		} else {
			neg1 = "";
		}

		PdfPCell payableInWords = new PdfPCell(new Paragraph(
				"In Words : " + neg1 + " " + NumberToWordConverter.convert((int) cashTotal2) + " Only", font3));
		// PdfPCell payableInWords = new PdfPCell(new Paragraph("In Words :
		// "+NumberToWordConverter.convert((int)payableAmount)+" Only", font3));
		payableInWords.setBorder(Rectangle.NO_BORDER);
		payableInWords.setHorizontalAlignment(Element.ALIGN_RIGHT);
		payableInWords.setPaddingTop(5);
		payableInWords.setPaddingBottom(10);
		table2.addCell(payableInWords);


		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		sign.setBorder(Rectangle.NO_BORDER);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(0);
		sign.setPaddingBottom(1);

		PdfPCell Attendantsign = new PdfPCell(new Paragraph("Pateint/Attendant Signatory", font4));
		Attendantsign.setBorder(Rectangle.NO_BORDER);
		Attendantsign.setHorizontalAlignment(Element.ALIGN_LEFT);
		Attendantsign.setPaddingTop(-2);
		Attendantsign.setPaddingBottom(1);

		table2.addCell(Signatureline(2,20));
		table2.addCell(sign);
		table2.addCell(Signatureline(0,0));
		table2.addCell(Attendantsign);

		document.add(table2);
		// onEndPage(wr,document);
		document.close();

//		InsuranceRulesPDF r1=new InsuranceRulesPDF(patient_insurancetype); 
//
//
//		PDFMergerUtility PDFmerger = new PDFMergerUtility();  
//		PDFmerger.setDestinationFileName(r1.Return_Result()); 
//		PDFmerger.addSource(r1.Return_Result());
//		PDFmerger.addSource(RESULT);  
//		try {
//			PDFmerger.mergeDocuments();
//		} catch (COSVisitorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  




		new OpenFile(RESULT);

	}


	public PdfPCell Signatureline(int L_R,int Toppdd) {
		PdfPCell underline = new PdfPCell(
				new Paragraph("----------------------------------------------", font3));
		underline.setBorder(Rectangle.NO_BORDER);
		underline.setHorizontalAlignment(L_R);
		underline.setPaddingTop(Toppdd);
		underline.setPaddingBottom(3);
		return underline;
	}

	private PdfPCell AddAllHeaders() {
		// TODO Auto-generated method stub
		float[] TablCellWidth = new float[9];
		TablCellWidth = new float[] { 0.7f, 3.4f, 1.2f, 0.7f, 0.9f, 1.1f, 1.2f, 1.2f, 0.9f };

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.addCell(new Phrase("S.No.", font3));
		Table.addCell(new Phrase("Description", font3));
		Table.addCell(new Phrase("Date", font3));
		//		Table.addCell(new Phrase("Type", font3));
		//		Table.addCell(new Phrase("Remarks", font3));
		Table.addCell(new Phrase("Code", font3));
		Table.addCell(new Phrase("Rate", font3));
		Table.addCell(new Phrase("Qty", font3));
		Table.addCell(new Phrase("Batch", font3));
		Table.addCell(new Phrase("Expiry", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		Table.addCell(amount);

		PdfPCell Main = new PdfPCell(Table);
		Main.setBorder(Rectangle.NO_BORDER);
		Main.setColspan(10);
		return Main;

	}

	public PdfPCell Header(String n) {
		PdfPCell cell = new PdfPCell(new Phrase("" + n, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setColspan(10);
		cell.setPaddingBottom(3);
		return cell;
	}
	public PdfPCell TotalAmountHeader(double amt) {
		amt=Math.round(amt * 100.00) / 100.00;
		PdfPCell cell = new PdfPCell(new Phrase("Total : " + amt+"              ", font2));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		//cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(10);
		cell.setPaddingBottom(3);
		return cell;
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
				//totalCharges = Double.parseDouble(rs.getObject(9).toString());
				ipd_balance = Double.parseDouble(rs.getObject(10).toString());
				//payableAmount = Double.parseDouble(rs.getObject(11).toString());
				insurance_category = rs.getString(15);
				discharge_type= rs.getString(16);
				emergency = rs.getObject(14).toString();
				if (!bill_no.equals("Provisional Bil")) {
					ipd_date_dis = rs.getObject(12).toString();
					ipd_time_dis = rs.getObject(13).toString();
				} else {
					ipd_date_dis = DateFormatChange.StringToMysqlDate(new Date());
					ipd_time_dis = "";
				}

				int[] bedHours = DateFormatChange.calulateHoursAndDays_BetweenDates(
						DateFormatChange.javaDate(ipd_date + " " + ipd_time),
						DateFormatChange.javaDate(ipd_date_dis + " " + ipd_time_dis));

				ipd_days = bedHours[0] + "";
				ipd_hours = bedHours[1] + "";
				ipd_minutes = bedHours[2] + "";
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
		System.out.println(patient_id);
	}

	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_age = resultSet.getObject(2).toString();
				patient_address = resultSet.getObject(4).toString();
				patient_mob=resultSet.getString(6);
				patient_insurancetype = resultSet.getObject(7).toString();
				patient_insurance_no = resultSet.getObject(8).toString();
				//				if(patient_insurancetype.equals("ECHS") || patient_insurancetype.equals("Railway")){
				// patient_claim_id=resultSet.getObject(10).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IPDDBConnection ipddbConnection = new IPDDBConnection();
		ResultSet rs = ipddbConnection.retrieveClaim(ipd_no_a);
		try {
			if (rs.next()) {

				patient_claim_id = rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age + "  " + indexId);
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

		new File("IPDSlip").mkdir();
		RESULT = "IPDSlip/" + opd_no + ".pdf";
	}

	private void copyFileUsingJava7Files(String source, String dest) throws IOException {
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
		}
	}

	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText.showTextAligned(writer.getDirectContent(),

					Element.ALIGN_RIGHT,
					new Phrase("Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
					rect.getRight(), rect.getTop(), 0);
			break;
		case 1:
			ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_LEFT,
					new Phrase("Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
					rect.getLeft(), rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase(String.format("page %d", 1)), (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}

}