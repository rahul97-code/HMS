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

import java.awt.Color;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class InsuranceRulesPDF {

	// data storage vectors
	Vector<String> ITEM_NAME = new Vector<String>();
	Vector<String> DATE = new Vector<String>();
	Vector<String> TYPE = new Vector<String>();
	Vector<String> ITEM_ID = new Vector<String>();
	Vector<String> PAGE_NO = new Vector<String>();
	Vector<String> MRP = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE = new Vector<Double>();
	Vector<String> QTY = new Vector<String>();
	Vector<Double> AMOUNT = new Vector<Double>();
	Vector<String> BATCH = new Vector<String>();
	Vector<String> EXPIRY = new Vector<String>();
	double REBATE=0;

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.TIMES_ROMAN, 17, Font.BOLDITALIC, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8, Font.BOLD);
	private static Font HeaderFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.BOLDITALIC);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 13, Font.NORMAL);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9.5f, Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0, payableAmount = 0;
	String ipd_no_a, patient_id, patient_name, patient_address, patient_insurancetype, patient_claim_id,
			patient_insurance_no, patient_age, doctor_name, amt_received, date, bill_no;
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "", ipd_time = "", ipd_date_dis = "", ipd_time_dis = "",
			ipd_note = "", ipd_id = "", ward_name = "", building_name = "", bed_no = "Select Bed No",
			ward_incharge = "", ward_room = "", ward_code = "", descriptionSTR = "", charges = "", ipd_days, ipd_hours,
			ipd_minutes, ipd_expenses_id, emergency = "No";

	String mainDir = "";
	Font font;
//	float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f,1.1f,1.1f };
	String[] open = new String[4];
	private String insurance_category;

	public static void main(String[] argh) {
		try {
			new InsuranceRulesPDF("ECHS");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InsuranceRulesPDF(String ins)
			throws DocumentException, IOException {
		readFile();

		makeDirectory(ins);

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

		java.net.URL imgURL = InsuranceRulesPDF.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = InsuranceRulesPDF.class.getResource("/icons/Rotary-Club-logo.jpg");
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

		String INSURANCE=ins.toUpperCase();
		String bill = INSURANCE+" IPD SUBMISSION CHECKLIST ";
		

		PdfPCell spaceCell = new PdfPCell(new Paragraph(bill, HeaderFont));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		spaceCell.setPaddingTop(1);
		spaceCell.setPaddingBottom(5);
		table.addCell(spaceCell);

		PdfContentByte cb1 = wr.getDirectContent();
		BaseFont bf = BaseFont.createFont();

		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 40);
		f1.setColor(BaseColor.GRAY);
		Phrase phrase3 = new Phrase(bill, f1);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3, 300, 400, 40);

		cb1.saveState();
		// cb1.setColorStroke(BaseColor.BLACK);
		// cb1.rectangle(501, 657,80, 70);
		cb1.stroke();
		cb1.restoreState();

		float[] opdTablCellWidth = { 0.1f,1.0f,0.1f};
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.setWidthPercentage(60);
//		opdTable.getDefaultCell().setBorder(0);
		
		Paragraph p = new Paragraph("",font3);
		Font zapfdingbats = new Font(Font.FontFamily.ZAPFDINGBATS, 9);
		Chunk chunk = new Chunk("3", zapfdingbats);
		Chunk chunk1 = new Chunk("8", zapfdingbats);
		p.add(chunk);
		p.add("/");
		p.add(chunk1);
		
		PdfPCell cell0 = new PdfPCell(new Paragraph("S No.",font4));
		cell0.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell0.setHorizontalAlignment(Element.ALIGN_CENTER);
		opdTable.addCell(cell0);
		
		PdfPCell cell = new PdfPCell(new Paragraph("Details",font4));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		opdTable.addCell(cell);
		
		PdfPCell cell1 = new PdfPCell(p);
		cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
		opdTable.addCell(cell1);
		

		
		opdTable.addCell(new Phrase("1.", font3));
		opdTable.addCell(new Phrase("Patient Signature", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("2.", font3));
		opdTable.addCell(new Phrase("Bill", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("3.", font3));
		opdTable.addCell(new Phrase("ECHS Card", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("4.", font3));
		opdTable.addCell(new Phrase("Referral Letter", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("5.", font3));
		opdTable.addCell(new Phrase("Doctor Notes", font3));
		opdTable.addCell(new Phrase("", font3));

		
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setPaddingRight(200);
		opdCell.setBorder(Rectangle.NO_BORDER);
		table.addCell(opdCell);
		
		float[] opdTableCellWidth = { 1.0f};
		PdfPTable Table = new PdfPTable(opdTableCellWidth);
		Table.setWidthPercentage(90);
		Table.getDefaultCell().setBorder(0);
	
		Paragraph ph = new Paragraph("",font3);
		Font zapfdingbat = new Font(Font.FontFamily.ZAPFDINGBATS, 9);
		Chunk ch = new Chunk("1", zapfdingbat);
		ph.add(ch);
		
		PdfPCell c1 = new PdfPCell(new Paragraph(new Chunk(INSURANCE+" INSURANCE RULES...", font1).setUnderline(0.5f, -2f)));
		c1.setHorizontalAlignment(Element.ALIGN_LEFT);
		c1.setPaddingBottom(5);
		c1.setBorder(Rectangle.NO_BORDER);
		Table.addCell(c1);
		if(INSURANCE.equals("ECHS"))
		{
		Table.addCell(pdfCell("1. All Medicine Bill Will Be Uploaded With Date Of Manufcturing & Expiry"));
		Table.addCell(pdfCell("2. Sticker Will Required For Avobe 1000 Any Other Items"));
		Table.addCell(pdfCell("3. All Test Reports Of The Patient"));
		Table.addCell(pdfCell("4. Cathterization 15-Day*Rate"));
		Table.addCell(pdfCell("5. Referral Day Stay 11-Days"));
		Table.addCell(pdfCell("6. In Emergency Stay Depend On Emergency Referrl Letter Which Came From Polyclinic"));
		Table.addCell(pdfCell("7. Final Bill Deposit With In Seven Days From Date Of Discharge"));
		Table.addCell(pdfCell("8. Patient Admission With Timing 48Hrs"));
		Table.addCell(pdfCell("9. Implant Or Sticker Is Required With Bill"));
		Table.addCell(pdfCell("10. No Need For Unlisted Apprval For Rs 5000"));
		Table.addCell(pdfCell("11. Take Approval For Above 5000 Unlisted Procedure"));
		Table.addCell(pdfCell("12. Origional Blood Slip And Platelet Count"));
		Table.addCell(pdfCell("13. Ambulances Charges Not Covered Under Echs"));
		Table.addCell(pdfCell("14. If Any Query Is Raise After Admission In timation Of The Patient Check Need More Info Intimation"));
		Table.addCell(pdfCell("15. No Need Referral For Above 75 Age"));
		Table.addCell(pdfCell("16. Final Bill Upload With Patient Attandant Ssignature And Phone No."));
		Table.addCell(pdfCell("17. Query Cleaer With In 180-Days"));
		}
		else if(INSURANCE.equals("RAILWAY")) {
			Table.addCell(pdfCell("1. All Medicine Bill Will Be Uploaded With Date Of Manufcturing & Expiry"));
			Table.addCell(pdfCell("2. Sticker Will Required For Avobe 1000 Any Other Items"));
			Table.addCell(pdfCell("3. All Test Reports Of The Patient"));
			Table.addCell(pdfCell("4. Cathterization 15-Day*Rate"));
			Table.addCell(pdfCell("5. Referral Day Stay 11-Days"));
			Table.addCell(pdfCell("6. In Emergency Stay Depend On Emergency Referrl Letter Which Came From Polyclinic"));
			Table.addCell(pdfCell("7. Final Bill Deposit With In Seven Days From Date Of Discharge"));
			Table.addCell(pdfCell("8. Patient Admission With Timing 48Hrs"));
			Table.addCell(pdfCell("9. Implant Or Sticker Is Required With Bill"));
			Table.addCell(pdfCell("10. No Need For Unlisted Apprval For Rs 5000"));
			Table.addCell(pdfCell("11. Take Approval For Above 5000 Unlisted Procedure"));
			Table.addCell(pdfCell("12. Origional Blood Slip And Platelet Count"));
			Table.addCell(pdfCell("13. Ambulances Charges Not Covered Under Echs"));
			Table.addCell(pdfCell("14. If Any Query Is Raise After Admission In timation Of The Patient Check Need More Info Intimation"));
			Table.addCell(pdfCell("15. No Need Referral For Above 75 Age"));
			Table.addCell(pdfCell("16. Final Bill Upload With Patient Attandant Ssignature And Phone No."));
			Table.addCell(pdfCell("17. Query Cleaer With In 180-Days"));
		}
		else if(INSURANCE.equals("BPL")) {
			Table.addCell(pdfCell("1. All Medicine Bill Will Be Uploaded With Date Of Manufcturing & Expiry"));
			Table.addCell(pdfCell("2. Sticker Will Required For Avobe 1000 Any Other Items"));
			Table.addCell(pdfCell("3. All Test Reports Of The Patient"));
			Table.addCell(pdfCell("4. Cathterization 15-Day*Rate"));
			Table.addCell(pdfCell("5. Referral Day Stay 11-Days"));
			Table.addCell(pdfCell("6. In Emergency Stay Depend On Emergency Referrl Letter Which Came From Polyclinic"));
			Table.addCell(pdfCell("7. Final Bill Deposit With In Seven Days From Date Of Discharge"));
			Table.addCell(pdfCell("8. Patient Admission With Timing 48Hrs"));
			Table.addCell(pdfCell("9. Implant Or Sticker Is Required With Bill"));
			Table.addCell(pdfCell("10. No Need For Unlisted Apprval For Rs 5000"));
			Table.addCell(pdfCell("11. Take Approval For Above 5000 Unlisted Procedure"));
			Table.addCell(pdfCell("12. Origional Blood Slip And Platelet Count"));
			Table.addCell(pdfCell("13. Ambulances Charges Not Covered Under Echs"));
			Table.addCell(pdfCell("14. If Any Query Is Raise After Admission In timation Of The Patient Check Need More Info Intimation"));
			Table.addCell(pdfCell("15. No Need Referral For Above 75 Age"));
			Table.addCell(pdfCell("16. Final Bill Upload With Patient Attandant Ssignature And Phone No."));
			Table.addCell(pdfCell("17. Query Cleaer With In 180-Days"));
		}
//		PdfPCell opdCel = new PdfPCell(Table);
//		opdCel.setPaddingRight(250);
//		opdCel.setBorder(Rectangle.NO_BORDER);
//		table.addCell(opdCel);
		
		table.addCell(new Phrase("  ", font3));
	
		document.add(table);
		document.add(Table);
		//document.add(ipdAmount(ipd_no));

		PdfPTable table2 = new PdfPTable(1);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(90);
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
		



		PdfPCell sign = new PdfPCell(new Paragraph("Authorised Signatory-Hospital", font4));
		sign.setBorder(Rectangle.NO_BORDER);
		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingTop(10);
		sign.setPaddingBottom(1);

		document.add(table2);
		
		
		// onEndPage(wr,document);
		document.close();
		new OpenFile(RESULT);
		Return_Result();

	}

	public String Return_Result() {
		// TODO Auto-generated method stub
		return RESULT;
		
	}

	public PdfPCell pdfCell(String phrase) {
		PdfPCell c = new PdfPCell(new Phrase(phrase, font3));
		c.setPaddingBottom(2);
		c.setHorizontalAlignment(Element.ALIGN_LEFT);
		c.setBorder(Rectangle.NO_BORDER);
		return c;
	}

	public void makeDirectory(String ins) {

		new File("InsuranceRules").mkdir();
		RESULT = "InsuranceRules/" + ins + ".pdf";
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



}