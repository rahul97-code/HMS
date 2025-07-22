package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.doctor.database.DoctorDBConnection;
import hms.main.MainLogin;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.free_test.FreeTestDBConnection;

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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
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
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.sun.media.rtp.RecvSSRCInfo;

public class OPDSlipNewFormat {

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
	private static Font font55 = new Font(Font.FontFamily.HELVETICA, 10,Font.BOLD);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 34,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenNamefont = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD, BaseColor.WHITE);
	private static Font Amountfont = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD, BaseColor.BLACK);
	private static Font fontForMedico = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD, BaseColor.BLACK);
	public static String RESULT = "opdslip1.pdf";

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,patient_sex,
	amt_received, date, token_no, time,p_type,patient_mob="";
	String mainDir = "";
	Font font;
	String[] open = new String[4];
	private String opd_entry_user;
	private boolean is_show=false;
	private String opd_type;

	public static void main(String[] argh) {
		try {
			new OPDSlipNewFormat("774817", true, "ONLINE REGISTERED",false);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OPDSlipNewFormat(String opd_no, boolean original, String online,boolean print)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		// getAllDoctorsRegular();
		getOPDData(opd_no);
		getPatientDetail(patient_id);
		readFile();
		makeDirectory(opd_no);
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

		java.net.URL imgURL = OPDSlipNewFormat.class
				.getResource("/icons/NABH.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(60);
		image.setAbsolutePosition(100, 270);

		java.net.URL imgURLRotaryClub = OPDSlipNewFormat.class
				.getResource("/icons/Rotary_Logo_New.png");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);


		PdfContentByte cb1 = wr.getDirectContent();

		Water_Mark(cb1);
		cb1.saveState();
		cb1.stroke();
		cb1.restoreState();


		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(5);
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
		logocell.setRowspan(5);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Mill Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"    Phone No. : 0171-2690009, Mobile No. : 09034056793  General Enquiry and Emergency : 09034056791",
						font2));
		addressCell2.setPaddingBottom(2);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		TitleTable.addCell(addressCell2);
		//		PdfPCell addressCellGeneral = new PdfPCell(new Phrase(
		//				"For General Enquiry and Emergency. : 09034056793", font2));
		//		addressCellGeneral.setPaddingBottom(5);
		//		addressCellGeneral.setBorder(Rectangle.NO_BORDER);
		//		addressCellGeneral.setHorizontalAlignment(Element.ALIGN_CENTER);
		//		TitleTable.addCell(addressCellGeneral);
		table.addCell(TitleTable);

		// float[] doctorTableCellWidth = {1f,1f,0.1f };
		// PdfPTable doctorTable = new PdfPTable(doctorTableCellWidth);
		// doctorTable.getDefaultCell().setBorder(0);
		// doctorTable.addCell(new Phrase("Regular Consultants", font2));
		// doctorTable.addCell(new Phrase("", font2));
		// doctorTable.addCell(new Phrase(" ", smallBold));
		// getAllDoctors("0");
		// int k=doctorname.size()/2;
		// System.out.println("regular doctor"+k);
		// for (int i = 0; i <k ; i++) {
		//
		// doctorTable.addCell(new
		// Phrase(doctorname.get(i)+""+specialization.get(i)+"", smallBold));
		// doctorTable.addCell(new
		// Phrase(doctorname.get(k+i)+""+specialization.get(k+i)+"",
		// smallBold));
		// doctorTable.addCell(new Phrase(" ", smallBold));
		// }
		// doctorname.clear();
		// specialization.clear();
		// getAllDoctors("1");
		// float[] doctorTableCellWidth1 = {1f,1f,0.1f };
		// PdfPTable doctorTable1 = new PdfPTable(doctorTableCellWidth1);
		// doctorTable1.getDefaultCell().setBorder(0);
		// doctorTable1.addCell(new Phrase("Visiting Consultants", font2));
		// doctorTable1.addCell(new Phrase("", font2));
		// doctorTable1.addCell(new Phrase(" ", smallBold));
		// int k1=doctorname.size()/2;
		// System.out.println("visiting doctor"+k1);
		// for (int i = 0; i <k1 ; i++) {
		//
		// doctorTable1.addCell(new
		// Phrase(doctorname.get(i)+""+specialization.get(i)+"", smallBold));
		// doctorTable1.addCell(new
		// Phrase(doctorname.get(k+i)+""+specialization.get(k+i)+"",
		// smallBold));
		// doctorTable1.addCell(new Phrase(" ", smallBold));
		// }
		// PdfPCell doctorCell = new PdfPCell(doctorTable);
		// PdfPCell doctorCell1 = new PdfPCell(doctorTable1);
		// doctorCell.setPaddingTop(2);
		// doctorCell.setPaddingBottom(5);
		// doctorCell.setPaddingLeft(5);
		// doctorCell.setBorderWidth(0.8f);
		// table.addCell(doctorCell);
		// table.addCell(doctorCell1);

		PdfPCell spaceCell = new PdfPCell(new Paragraph("    ", spaceFont));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setPaddingTop(1);
		table.addCell(spaceCell);

		float[] opdTablCellWidth = { 1.5f, 1f, 1.1f, 2f, 1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);
		//
		// PdfPCell tokencell = new PdfPCell(new
		// Phrase("Token No."+"\n",tokenfont4));
		// tokencell.setBorder(Rectangle.NO_BORDER);
		// tokencell.setPaddingBottom(4);
		// tokencell.setBackgroundColor(BaseColor.GRAY);
		//
		// PdfPCell tokenNocell = new PdfPCell(new
		// Phrase(token_no+"\n",tokenfont4));
		// tokenNocell.setBorder(Rectangle.NO_BORDER);
		// tokenNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// tokenNocell.setPaddingBottom(4);
		// tokenNocell.setBackgroundColor(BaseColor.GRAY);
		// ********************************

		String str = "";

		// PdfContentByte cb1 = wr.getDirectContent();
		// BaseFont bf = BaseFont.createFont();
		// Phrase phrase = new Phrase(""+token_no, new Font(bf, 30));
		// Phrase phrase2 = new Phrase("Token No.", new Font(bf, 15));
		//
		// Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
		// f1.setColor(BaseColor.GRAY);
		// Phrase phrase3 = new Phrase("DUPLICATE", f1);
		//
		//
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase,540,685,0);
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase2,540,710,0);
		//
		//
		// if(!original)
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase3,300,400,30);
		//
		//
		// cb1.saveState();
		// cb1.setColorStroke(BaseColor.BLACK);
		// cb1.rectangle(501, 676,80, 51);
		// cb1.stroke();
		// cb1.restoreState();

		// ****************************
		// opdTable.addCell(tokencell);
		// opdTable.addCell(tokenNocell);
		//
		opdTable.addCell(new Phrase("Receipt No. : ", font3));
		opdTable.addCell(new Phrase("" + opd_no, smallBold));

		// opdTable.addCell(new Phrase("", font3));

		opdTable.addCell(new Phrase("Date/Time : ", font3));
		opdTable.addCell(new Phrase("" +date+" / "+time, smallBold));

		PdfPCell tokenNocell = new PdfPCell(new Phrase(token_no+"",tokenfont4));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		tokenNocell.setBackgroundColor(BaseColor.GRAY);
		if(is_show)
			tokenNocell.setRowspan(5);
		else
			tokenNocell.setRowspan(4);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("UHID: ", font3));
		opdTable.addCell(new Phrase("" + patient_id, smallBold));

		opdTable.addCell(new Phrase("Patient Type", font3));
		opdTable.addCell(new Phrase(""+p_type, font3));

		opdTable.addCell(new Phrase("Patient Name: ", font3));
		PdfPCell patienttcell = new PdfPCell(new Phrase("" + patient_name, smallBold));
		patienttcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(patienttcell);

		opdTable.addCell(new Phrase("Age/Gender : ", font3));
		opdTable.addCell(new Phrase("" + patient_age+"/"+patient_sex, smallBold));

		opdTable.addCell(new Phrase("Patient Mob. : ", font3));
		opdTable.addCell(new Phrase(patient_mob, font3));

		PdfPCell consultantcellh = new PdfPCell(new Phrase("Consultant Name: "
				+ "\n", font3));
		consultantcellh.setBorder(Rectangle.NO_BORDER);
		consultantcellh.setColspan(1);
		opdTable.addCell(consultantcellh);
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctor_name
				+ "\n", font3));
		consultantcell.setBorder(Rectangle.NO_BORDER);
		consultantcell.setColspan(2);
		opdTable.addCell(consultantcell);


		opdTable.addCell(new Phrase("Generated By: ", font3));
		opdTable.addCell(new Phrase(""+opd_entry_user, smallBold));

		opdTable.addCell(new Phrase("Amt Received : ", font3));
		PdfPCell Amountcell = new PdfPCell(new Phrase("Rs. "+amt_received,Amountfont));
		Amountcell.setBorder(Rectangle.NO_BORDER);
		Amountcell.setHorizontalAlignment(Element.ALIGN_CENTER);
		Amountcell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		opdTable.addCell(Amountcell);

		PdfPCell OpdTypecell = new PdfPCell(new Phrase("OPD Type : "+opd_type,font3));
		OpdTypecell.setBorder(Rectangle.NO_BORDER);
		OpdTypecell.setHorizontalAlignment(Element.ALIGN_LEFT);
		OpdTypecell.setColspan(4);
		if (is_show) {
			opdTable.addCell(new Phrase(" ", font3));
			opdTable.addCell(OpdTypecell);
		}
		PdfPCell tokencell = new PdfPCell(new Phrase("Token No.",tokenNamefont));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setHorizontalAlignment(Element.ALIGN_CENTER);
		tokencell.setBackgroundColor(BaseColor.GRAY);
		opdTable.addCell(tokencell);


		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);
		float[] opdTablCellWidth1 = { 1f, 1f, 1f, 1f, 1f,1f };
		PdfPTable opdTable1 = new PdfPTable(opdTablCellWidth1);
		opdTable1.getDefaultCell().setBorder(0);
		//
		// PdfPCell tokencell = new PdfPCell(new
		// Phrase("Token No."+"\n",tokenfont4));
		// tokencell.setBorder(Rectangle.NO_BORDER);
		// tokencell.setPaddingBottom(4);
		// tokencell.setBackgroundColor(BaseColor.GRAY);
		//
		// PdfPCell tokenNocell = new PdfPCell(new
		// Phrase(token_no+"\n",tokenfont4));
		// tokenNocell.setBorder(Rectangle.NO_BORDER);
		// tokenNocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// tokenNocell.setPaddingBottom(4);
		// tokenNocell.setBackgroundColor(BaseColor.GRAY);
		// ********************************

		String str1 = "";

		// PdfContentByte cb1 = wr.getDirectContent();
		// BaseFont bf = BaseFont.createFont();
		// Phrase phrase = new Phrase(""+token_no, new Font(bf, 30));
		// Phrase phrase2 = new Phrase("Token No.", new Font(bf, 15));
		//
		// Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
		// f1.setColor(BaseColor.GRAY);
		// Phrase phrase3 = new Phrase("DUPLICATE", f1);
		//
		//
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase,540,685,0);
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase2,540,710,0);
		//
		//
		// if(!original)
		// ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER,
		// phrase3,300,400,30);
		//
		//
		// cb1.saveState();
		// cb1.setColorStroke(BaseColor.BLACK);
		// cb1.rectangle(501, 676,80, 51);
		// cb1.stroke();
		// cb1.restoreState();

		// ****************************
		// opdTable.addCell(tokencell);
		// opdTable.addCell(tokenNocell);
		//
		opdTable1.addCell(new Phrase("", font3));
		opdTable1.addCell(new Phrase("\n", font3));

		// opdTable.addCell(new Phrase("", font3));

		PdfPCell nursing = new PdfPCell(new Phrase("NURSING INITIAL ASSESSMENT"
				+ "\n", smallBold3));

		nursing.setBorder(Rectangle.NO_BORDER);
		nursing.setColspan(2);
		nursing.setRowspan(1);
		opdTable1.addCell(nursing);
		nursing = new PdfPCell(new Phrase("Vulnerable: Yes/No", smallBold3));
		nursing.setHorizontalAlignment(Element.ALIGN_RIGHT);
		nursing.setBorder(Rectangle.NO_BORDER);
		nursing.setColspan(3);
		nursing.setPaddingRight(10);
		nursing.setRowspan(1);
		opdTable1.addCell(nursing);

//		opdTable1.addCell(new Phrase(" ", font4));
		opdTable1.addCell(new Phrase("Start Time: ........... ", font3));


		opdTable1.addCell(new Phrase("PULSE :  ...... /min ", font3));
		opdTable1.addCell(new Phrase("B.P. : .......... mmHg", font3));
		opdTable1.addCell(new Phrase("TEMP. : ......... F", font3));
		opdTable1.addCell(new Phrase("WT. : .......... Kg", font3));
		opdTable1.addCell(new Phrase("SPO2 : .......... %", font3));

		PdfPCell paincell = new PdfPCell(new Phrase(
				"Pain Score : ....................(Score:-  0 : No Pain, 1-3 : Mild Pain, 4-7 : Moderate Pain, 8-10 : Severe Pain)" + "\n", font3));

		paincell.setBorder(Rectangle.NO_BORDER);
		paincell.setColspan(5);


		opdTable1.addCell(paincell);
		// opdTable1.addCell(new
		// Phrase("Pain Score :.....(Score:0: No Pain,1-3:Mild Pain,4-7:Moderate Pain,8-10:Severe Pain) ",
		// font3));

		// opdTable.addCell(new Phrase("", font3));

		//		opdTable1.addCell(new Phrase(" ", font3));
		//		opdTable1.addCell(new Phrase("", font3));

		opdTable1.addCell(new Phrase("End Time: ............", font3));


		PdfPCell opdCell1 = new PdfPCell(opdTable1);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell1);

		// PdfPCell space = new PdfPCell(new
		// Paragraph("Date / Time : "+date+" / "+time, smallBold));
		// space.setBorder(Rectangle.NO_BORDER);
		// space.setHorizontalAlignment(Element.ALIGN_RIGHT);
		//
		//
		// space.setPaddingRight(20);
		// table.addCell(space);

		float[] facilityTablCellWidth = { 0.28f, 1f};
		PdfPTable mainFacilityTable = new PdfPTable(facilityTablCellWidth);

		PdfPTable cardiacTable = new PdfPTable(1);
		cardiacTable.getDefaultCell().setBorder(0);

		cardiacTable.addCell(new Phrase(" ", smallBold));
		PdfPCell cardiacTitleCell1 = new PdfPCell(new Phrase(
				"Cardiac Care Services 24x7", font14));
		cardiacTitleCell1.setBorder(Rectangle.NO_BORDER);
		cardiacTitleCell1.setHorizontalAlignment(Element.ALIGN_CENTER);

		cardiacTable.addCell(cardiacTitleCell1);

		PdfPCell cardiacTitleCell2 = new PdfPCell(new Phrase("24x7", font14));
		cardiacTitleCell2.setBorder(Rectangle.NO_BORDER);
		cardiacTitleCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

		cardiacTable.addCell(cardiacTitleCell2);



		PdfPCell facilityCell = new PdfPCell(getOpdDSpecialties());
		//		facilityCell.setPaddingBottom(2);
		facilityCell.setPaddingLeft(0);
		facilityCell.setPaddingRight(0);
		facilityCell.setBorderWidth(0.8f);
		mainFacilityTable.addCell(facilityCell);


		PdfPTable empanelledTable = new PdfPTable(1);
		//		empanelledTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		empanelledTable.getDefaultCell().setBorder(0);
		PdfPCell empanelledTitleCell = new PdfPCell(new Phrase(
				"INVESTIGATIONS", font2));
		empanelledTitleCell.setBorder(Rectangle.NO_BORDER);
		empanelledTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);
		empanelledTitleCell.setPaddingTop(-20);
		empanelledTable.addCell(empanelledTitleCell);
//		empanelledTable.addCell(new Phrase(" "+"\n", smallBold3));

		//		empanelledTable.addCell(new Phrase(" "+"\n", smallBold3));

//		empanelledTable.addCell(new Phrase(" "+"\n", smallBold3));
		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));
		//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));
		PdfPCell space1 = new PdfPCell(new Phrase(
				"", font2));
		space1.setBorder(Rectangle.NO_BORDER);
		//		allergyTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		space1.setPaddingBottom(20);
		empanelledTable.addCell(space1);
		PdfPCell allergyTitleCell = new PdfPCell(new Phrase(
				"ALLERGY(IF ANY)", font2));
		allergyTitleCell.setBorder(Rectangle.NO_BORDER);
		allergyTitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

		empanelledTable.addCell(allergyTitleCell);
//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));

		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));

		//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));
//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));
		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));
		empanelledTable.addCell(space1);
		PdfPCell followitleCell = new PdfPCell(new Phrase(
				"NEXT FOLLLOW UP", font2));
		followitleCell.setBorder(Rectangle.NO_BORDER);
		followitleCell.setHorizontalAlignment(Element.ALIGN_LEFT);

		empanelledTable.addCell(followitleCell);
//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));

		if(!is_show)
			empanelledTable.addCell(new Phrase(""+"\n", smallBold3));

//		empanelledTable.addCell(new Phrase(""+"\n", smallBold3));

		// ///
		PdfPCell cardiacCell = new PdfPCell(new Phrase("", smallBold));

		cardiacCell.setBorderWidth(0);
		// mainFacilityTable.addCell(" ");
		// ////

		PdfPTable facilityTableNew = new PdfPTable(1);
		facilityTableNew.getDefaultCell().setBorder(Rectangle.NO_BORDER);


		facilityTableNew.addCell(new Phrase(12, "   PAST HISTORY & CHIEF COMPLAINTS",
				font2));

		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase("   DIAGNOSIS",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase("  PHARMACOLOGICAL TREATMENT",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));
		facilityTableNew.addCell(new Phrase(""+"\n",
				font2));

		PdfPCell Signature = new PdfPCell(new Phrase(
				"", font2));
		Signature.setBorder(Rectangle.NO_BORDER);
		//		Signature.setHorizontalAlignment(Element.ALIGN_RIGHT);

		facilityTableNew.addCell(Signature);
		PdfPCell facilityCellNew = new PdfPCell(facilityTableNew);
		facilityCellNew.setBorder(Rectangle.NO_BORDER);

		//		facilityCell.setPaddingBottom(2);
		facilityCell.setPaddingLeft(2);
		facilityCell.setBorderWidth(0.8f);
		mainFacilityTable.addCell(facilityCellNew);

		//		PdfPCell facilitiesCell2 = new PdfPCell(new Phrase("  PAST HISTORY & CHIEF COMPLAINTS", font2));
		//		facilitiesCell2.setBorder(Rectangle.NO_BORDER);
		//		facilitiesCell2.setPaddingBottom(2);
		//		facilitiesCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		//
		//		mainFacilityTable.addCell(facilitiesCell2);

		PdfPCell facilitiesCell3 = new PdfPCell(new Phrase("", font2));
		facilitiesCell3.setBorder(Rectangle.NO_BORDER);
		facilitiesCell3.setPaddingBottom(2);
		facilitiesCell3.setHorizontalAlignment(Element.ALIGN_LEFT);

		//		mainFacilityTable.addCell(facilitiesCell2);
		//		PdfPCell facilitiesCellbb = new PdfPCell(new Phrase("PAST HISTORY & CHIEF COMPLAINTS", font2));
		//		facilitiesCellbb.setBorder(Rectangle.NO_BORDER);
		//		facilitiesCellbb.setPaddingBottom(2);
		//		facilitiesCell2.setHorizontalAlignment(Element.ALIGN_LEFT);
		//
		//		mainFacilityTable.addCell(facilitiesCell2);
		//		mainFacilityTable.addCell(new Phrase(""+"\n", smallBold3));
		//		mainFacilityTable.addCell(new Phrase(""+"\n", smallBold3));
		//
		//
		//

		//
		mainFacilityTable.addCell(facilitiesCell3);
		//
		// //////
		PdfPCell emptyCell = new PdfPCell(new Phrase(" ", font2));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setPaddingBottom(2);
		emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		// ///
		mainFacilityTable.addCell(emptyCell);
		mainFacilityTable.addCell(facilitiesCell3);
		// ///

		mainFacilityTable.addCell(facilitiesCell3);

		// ///////
		mainFacilityTable.addCell(emptyCell);
		//

		PdfPCell emptyCell2 = new PdfPCell(new Phrase("", font2));
		emptyCell2.setBorder(Rectangle.NO_BORDER);
		emptyCell2.setPaddingBottom(1);
		emptyCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(emptyCell2);
		//
		// //////////
		//
		//
		//
		// /////////
		PdfPCell empanelledCell = new PdfPCell(empanelledTable);
		empanelledCell.setBorder(Rectangle.NO_BORDER);
		// empanelledCell.setPaddingBottom(5);
		empanelledCell.setPaddingLeft(2);
		//		empanelledCell.setBorderWidth(0.8f);
		mainFacilityTable.addCell(empanelledCell);


		PdfPCell empanelledCell2 = new PdfPCell(new Phrase("", font2));
		empanelledCell2.setBorder(Rectangle.NO_BORDER);
		empanelledCell2.setPaddingBottom(1);
		empanelledCell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		mainFacilityTable.addCell(empanelledCell2);
		PdfPCell empanelledCellNew= new PdfPCell(new Phrase("", font2));
		empanelledCellNew.setBorder(Rectangle.NO_BORDER);
		empanelledCellNew.setPaddingBottom(1);
		empanelledCellNew.setHorizontalAlignment(Element.ALIGN_CENTER);
		mainFacilityTable.addCell(empanelledCellNew);
		PdfPCell empanelledCellNew1 = new PdfPCell(new Phrase("CONSULTANT SIGN & STAMP", font2));
		empanelledCellNew1.setBorder(Rectangle.NO_BORDER);
		empanelledCellNew1.setPaddingBottom(1);
		empanelledCellNew1.setHorizontalAlignment(Element.ALIGN_RIGHT);
		mainFacilityTable.addCell(empanelledCellNew1);
		PdfPCell empanelledCellNew2 = new PdfPCell(new Phrase("", font2));
		empanelledCellNew2.setBorder(Rectangle.NO_BORDER);
		empanelledCellNew2.setPaddingBottom(1);
		empanelledCellNew2.setHorizontalAlignment(Element.ALIGN_LEFT);
		mainFacilityTable.addCell(empanelledCellNew2);
		//
		//
		//
		PdfPCell addFacilityTable = new PdfPCell(mainFacilityTable);
		addFacilityTable.setBorder(Rectangle.NO_BORDER);
		if(is_show) 
			addFacilityTable.setPaddingTop(4);
		else 
			addFacilityTable.setPaddingTop(10);
		int spaceINT = 18;
		addFacilityTable.setPaddingBottom(spaceINT);
		// addFacilityTable.setPaddingBottom(60);
		table.addCell(addFacilityTable);

		PdfPCell blankCell = new PdfPCell(new Phrase(" ", smallBold));
		blankCell.setRowspan(5);
		float[] packagesTableCellWidthNew = { 1f,1f,1f,1f };
		PdfPTable packagesTableNew = new PdfPTable(packagesTableCellWidthNew);
		packagesTableNew.setWidthPercentage(30);
		packagesTableNew.getDefaultCell().setBorder(Rectangle.NO_BORDER);
		packagesTableNew
		.addCell(new Phrase(" ", smallBold));
		packagesTableNew
		.addCell(new Phrase("", smallBold));
		PdfPCell sign = new PdfPCell(new Phrase(
				"", font2));
		sign.setBorder(Rectangle.NO_BORDER);

		sign.setHorizontalAlignment(Element.ALIGN_RIGHT);
		sign.setPaddingLeft(10);
		packagesTableNew.addCell(sign);


		PdfPCell packagesCell1 = new PdfPCell(packagesTableNew);
		packagesCell1.setBorder(Rectangle.NO_BORDER);
		packagesCell1.setPaddingBottom(0);
		packagesCell1.setPaddingLeft(0);
		packagesCell1.setBorderWidth(0.8f);
		table.addCell(packagesCell1);
		float[] packagesTableCellWidth = { 1f,1f,1f,1f,1f };
		PdfPTable packagesTable = new PdfPTable(packagesTableCellWidth);
		packagesTable.setWidthPercentage(30);
		packagesTable.getDefaultCell().setBorder(0);


		PdfPCell specialPackagesTitleCell = new PdfPCell(new Phrase(
				"Nutritional Advice", font2));
		specialPackagesTitleCell.setBorder(Rectangle.NO_BORDER);
		specialPackagesTitleCell.setColspan(5);
		specialPackagesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

		packagesTable.addCell(specialPackagesTitleCell);
		packagesTable
		.addCell(new Phrase("Normal Diet:....................... ", smallBold));
		packagesTable.addCell(new Phrase("Low Salt Diet:....................... ", smallBold));
		packagesTable.addCell(new Phrase("Low Fat Diet:..................... ", smallBold));


		packagesTable.addCell(new Phrase("High Calorie Diet:..................",smallBold));
		packagesTable.addCell(new Phrase("Diabetic Diet:....................... ", smallBold));


		packagesTable.addCell(new Phrase("Renal Diet:.......................  ", smallBold));
		packagesTable.addCell(new Phrase("Other Special Diet:................ ", smallBold));
		packagesTable.addCell(new Phrase("", font));

		packagesTable.addCell(new Phrase(" ",
				smallBold));
		packagesTable.addCell(new Phrase("", smallBold));



		//
		// packagesTable.addCell(new Phrase("9. Gyane Package ", smallBold));
		// packagesTable.addCell(new Phrase(" ", smallBold));
		// packagesTable.addCell(new Phrase("`700", font));
		//
		// packagesTable.addCell(new Phrase("10. Angiography Package ",
		// smallBold));
		// packagesTable.addCell(new Phrase(" ", smallBold));
		// packagesTable.addCell(new Phrase("`500", font));

		PdfPCell packagesCell = new PdfPCell(packagesTable);
		packagesCell.setPaddingBottom(1);
		packagesCell.setPaddingLeft(0);
		packagesCell.setBorderWidth(0.8f);
		table.addCell(packagesCell);

		//		PdfPCell footer = new PdfPCell(new Phrase("   ", font3));
		//		footer.setBorder(Rectangle.NO_BORDER);
		//		footer.setHorizontalAlignment(Element.ALIGN_CENTER);
		//		table.addCell(footer);
		//		table.addCell(new Phrase("\n", smallBold));
		emptyCell = new PdfPCell(new Phrase(" ", font2));
		emptyCell.setBorder(Rectangle.NO_BORDER);
		emptyCell.setPaddingBottom(-6);
		emptyCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(emptyCell);

		PdfPCell call3 = new PdfPCell(new Paragraph(
				"  Cardiac  Care  Services  24x7 : 9813334999 "+"\n"+" Brain  Stroke  Services  24x7 : 8295699929", font4));
		// call3.setBorder(Rectangle.NO_BORDER);
		//		call3.setRowspan(5);
		//		call3.setRotation(90);
		//		call3.setPaddingRight(2);
		call3.setPaddingBottom(3);

		call3.setHorizontalAlignment(Element.ALIGN_CENTER);
		//		call3.setVerticalAlignment(Element.ALIGN_CENTER);


		table.addCell(call3);
		PdfPCell footer2 = new PdfPCell(
				new Phrase(
						"Sunday : Working, Saturday : General OPD Closed, EMERGENCY : 24x7",
						font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(2);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		// PdfPCell footerWrap = new PdfPCell(footer2);
		// footerWrap.setPaddingLeft(50);
		// footerWrap.setPaddingRight(50);
		// footerWrap.setHorizontalAlignment(Element.ALIGN_CENTER);

		table.addCell(footer2);
		PdfPCell footer3 = new PdfPCell(
				new Phrase(
						"This Slip is valid for 2 days only",
						font55));
		footer3.setBorder(Rectangle.NO_BORDER);
		//		footer3.setPaddingBottom(2);
		footer3.setHorizontalAlignment(Element.ALIGN_CENTER);
		//		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		// PdfPCell footerWrap = new PdfPCell(footer2);
		// footerWrap.setPaddingLeft(50);
		// footerWrap.setPaddingRight(50);
		// footerWrap.setHorizontalAlignment(Element.ALIGN_CENTER);

		table.addCell(footer3);

		Chunk underlineChunk = new Chunk("Not For Medico-Legal Purpose", font55);
		underlineChunk.setUnderline(0.1f, -2f); // Thickness and Y-position of underline

		footer3 = new PdfPCell(new Phrase(underlineChunk));
		footer3.setBorder(Rectangle.NO_BORDER);
		footer3.setPaddingTop(-12);
		footer3.setHorizontalAlignment(Element.ALIGN_RIGHT);

		table.addCell(footer3);


		FreeTestDBConnection connection = new FreeTestDBConnection();
		ResultSet resultSet = connection.retrieveDataWithOPD(opd_no);
		int i = 0;
		try {
			while (resultSet.next()) {
				footer2 = new PdfPCell(new Phrase(resultSet.getObject(4)
						.toString(), font4));
				footer2.setBorder(Rectangle.NO_BORDER);
				footer2.setPaddingBottom(2);
				footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(footer2);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connection.closeConnection();

		document.add(table);

		document.close();

		new OpenFile(RESULT);
		if (print) {
			try {
				Thread.sleep(2500);
			} catch (InterruptedException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			new PrintFile(RESULT,5);
		}

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
	public PdfPTable getOpdDSpecialties() {
		PdfPTable facilityTable = new PdfPTable(1);
		facilityTable.getDefaultCell().setBorder(0);
		String headerOpd="";
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveOpdSpecialties();
			while (rs.next()) {
				PdfPCell facilitiesTitleCell = new PdfPCell(new Phrase("*** "+rs.getString(1)+" OPDs ***",font2));
				facilitiesTitleCell.setBorder(Rectangle.NO_BORDER);
				facilitiesTitleCell.setHorizontalAlignment(Element.ALIGN_CENTER);
				if(!headerOpd.equals(rs.getString(1))) {
					facilityTable.addCell(facilitiesTitleCell);
					headerOpd=rs.getString(1);
				}
				
				
				facilityTable.addCell(new Phrase(12, "* "+rs.getString(2)+"",smallBold2));
				

			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
		return facilityTable;
	}

	public void getOPDData(String opdID) {
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId(opdID);
			while (rs.next()) {
				opd_no = rs.getObject(1).toString();
				patient_id = rs.getObject(2).toString();
				patient_name = rs.getObject(3).toString();
				doctor_name = rs.getObject(4).toString();
				date = rs.getObject(5).toString();
				token_no = rs.getObject(8).toString();
				amt_received = rs.getObject(10).toString();
				time = rs.getObject(11).toString();
				p_type= rs.getObject(13).toString();
				opd_entry_user=rs.getObject(14).toString();
				opd_type=rs.getObject(12).toString();
				System.out.println("Amount Recieved :" + amt_received);
				is_show=rs.getBoolean(16);
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

				patient_name = resultSet.getObject(1).toString();
				patient_age = getAgeFormat(resultSet.getObject(2).toString());
				patient_sex = resultSet.getObject(3).toString();
				patient_mob= resultSet.getObject(6).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age);
		patientDBConnection.closeConnection();
	
	}

	public String getAgeFormat(String age){
			StringBuilder sb=new StringBuilder();
			String[] data=age.split("-");
			if(data.length>0) {
				sb.append(Integer.parseInt(data[0])+"Y");
				sb.append(Integer.parseInt(data[1])+"M");
				sb.append(Integer.parseInt(data[2])+"D");
			}
			return sb.toString();
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
		new File("OPDSlip").mkdir();
		RESULT = "OPDSlip/" + opd_no + ".pdf";
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
	public void Water_Mark(PdfContentByte cb1) {
		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 80);
		f1.setColor(new BaseColor(235,235,235));
		String str="ROTARY";
		Phrase phrase = new Phrase(str, f1);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 300,
				400, 30);

		String randm_no=Random_String();
		Font f3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 22.5f);
		f3.setColor(new BaseColor(235,235,235));

		Date date = new Date();  
		SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
		String strDate = formatter.format(date); 
		Phrase phrase4 = new Phrase(strDate+" "+randm_no, f3);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase4,310,350,30);


	}
	public String Random_String() {
		String str="";
		StringBuilder sb = new StringBuilder();
		// TODO Auto-generated constructor stub
		sb.append(Random_char_3());
		long millis = System.currentTimeMillis();
		sb.append(millis+"");
		sb.append(Random_char_3());

		return sb.toString();
	}
	public String Random_char_3() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<3;i++) {
			Random r = new Random();
			char c = (char)(r.nextInt(26) + 'A');
			sb.append(c);
		}
		return sb.toString();
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