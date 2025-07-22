package hms.patient.slippdf;

import hms.exam.database.ExamDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.database.TestRecordDBConnection;
import hms.test.database.TestResultDBConnection;
import hms.test.gui.AllReportsForPrint;

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
import java.util.Vector;
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
import com.itextpdf.text.pdf.Barcode128;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ExamRepertPDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 12);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.WHITE);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	public static String serverfile = "opdslip1.pdf";
	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String patient_id, patient_name, patient_age, doctor_name,
			 patient_sex;
	String mainDir = "";
	Font font;
	String reportName="";
	Vector<String> reportTypes = new Vector<String>();
	Vector<String> cat1 = new Vector<String>();
	Vector<String> cat2 = new Vector<String>();
	Vector<String> cat3 = new Vector<String>();
	Vector<String> cat4 = new Vector<String>();
	Vector<String> cat5 = new Vector<String>();
	Vector<String> cat6 = new Vector<String>();
	Vector<String> cat7 = new Vector<String>();
	Vector<String> cat8 = new Vector<String>();
	Vector<String> cat9 = new Vector<String>();
	Vector<String> cat10 = new Vector<String>();
	Vector<String> cat11 = new Vector<String>();
	Vector<String> cat12 = new Vector<String>();
	Vector<String> cat13 = new Vector<String>();
	Vector<String> cat14 = new Vector<String>();
	Vector<String> cat15 = new Vector<String>();
	
	
	Vector<String> reportCat1 = new Vector<String>();
	Vector<String> reportCat2 = new Vector<String>();
	Vector<String> reportCat3 = new Vector<String>();
	Vector<String> reportCat4 = new Vector<String>();
	Vector<String> reportCat5 = new Vector<String>();
	Vector<String> reportCat6 = new Vector<String>();
	Vector<String> reportCat7 = new Vector<String>();
	Vector<String> reportCat8 = new Vector<String>();
	Vector<String> reportCat9 = new Vector<String>();
	Vector<String> reportCat10 = new Vector<String>();
	Vector<String> reportCat11 = new Vector<String>();
	Vector<String> reportCat12 = new Vector<String>();
	Vector<String> reportCat13 = new Vector<String>();
	Vector<String> reportCat14 = new Vector<String>();
	Vector<String> reportCat15 = new Vector<String>();
	
	Vector<String> report010Cat1 = new Vector<String>();
	Vector<String> report010Cat2 = new Vector<String>();
	Vector<String> report010Cat3 = new Vector<String>();
	Vector<String> report010Cat4 = new Vector<String>();
	Vector<String> report010Cat5 = new Vector<String>();
	Vector<String> report010Cat6 = new Vector<String>();
	Vector<String> report010Cat7 = new Vector<String>();
	Vector<String> report010Cat8 = new Vector<String>();
	Vector<String> report010Cat9 = new Vector<String>();
	Vector<String> report010Cat10 = new Vector<String>();
	Vector<String> report010Cat11 = new Vector<String>();
	Vector<String> report010Cat12 = new Vector<String>();
	Vector<String> report010Cat13 = new Vector<String>();
	Vector<String> report010Cat14 = new Vector<String>();
	Vector<String> report010Cat15 = new Vector<String>();
	Object Rows_Object_Array[][];
	Object testName[];
	Object limits[];
	Object values[];
	Object units[];
	Object results[];
	AllReportsForPrint allReportsForPrint;
	int record_counter=0;
	String[] open=new String[4];
//	public static void main(String[] argh) {
//		try {
//			new ExamRepertPDF("39");
//		} catch (DocumentException | IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public ExamRepertPDF(String exam_counter,Object[][] Rows_Object_Array,Vector<String> reportType,String roomNo) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
//		getAllDoctors();
		getExamData(Rows_Object_Array[0][0].toString());
		
		getPatientDetail(patient_id);
		readFile();
		makeDirectory(exam_counter);
	
		this.Rows_Object_Array=Rows_Object_Array;
		reportTypes=reportType;
		filterData();
		record_counter=record_counter();
		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
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

		java.net.URL imgURL = ExamRepertPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = ExamRepertPDF.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(PdfPCell.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(5);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ABC GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(5);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(PdfPCell.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(3);
		TitleTable.addCell(logocell);

		PdfPCell addressCell = new PdfPCell(new Phrase(
				"XYZ, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(PdfPCell.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-XXXXXX, Mobile No. : 0XXXXXXXX",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(PdfPCell.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);
		//
		// PdfPTable doctorTable = new PdfPTable(2);
		// doctorTable.getDefaultCell().setBorder(0);
		//
		// for (int i = 0; i < 20; i++) {
		// doctorTable.addCell(new Phrase(" a  " + i, smallBold));
		// }
		PdfPCell labNameCell = new PdfPCell(new Paragraph("CLINICO PATH LAB",
				font4));
		labNameCell.setPaddingTop(1);
		labNameCell.setPaddingBottom(2);
		labNameCell.setBorder(PdfPCell.NO_BORDER);
		labNameCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		labNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		// labNameCell.setBorderWidth(0.8f);
		table.addCell(labNameCell);

		PdfPCell spaceCell = new PdfPCell(new Paragraph("    ", spaceFont));
		spaceCell.setBorder(PdfPCell.NO_BORDER);
		spaceCell.setPaddingTop(1);
		table.addCell(spaceCell);
		
		PdfPCell pathologistName = new PdfPCell(new Paragraph("Dr. Vandana Goyal M.B.B.S., M.D. (Path)",
				font3));
		pathologistName.setPaddingBottom(4);
		pathologistName.setBorder(PdfPCell.NO_BORDER);
		pathologistName.setHorizontalAlignment(Element.ALIGN_RIGHT);
		// labNameCell.setBorderWidth(0.8f);
		table.addCell(pathologistName);
		//
		PdfPTable opdTable = new PdfPTable(7);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Record No. : " , font4));
		tokencell.setBorder(PdfPCell.NO_BORDER);
		tokencell.setPaddingBottom(5);
		tokencell.setBackgroundColor(BaseColor.LIGHT_GRAY);

		PdfPCell tokenNocell = new PdfPCell(new Phrase(record_counter+ "", font4));
		tokenNocell.setBorder(PdfPCell.NO_BORDER);
		tokenNocell.setPaddingBottom(5);
		tokenNocell.setBackgroundColor(BaseColor.LIGHT_GRAY);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Lab S. No. :", font3));
		opdTable.addCell(new Phrase(" "+roomNo, font3));

		PdfContentByte cb = wr.getDirectContent();
		Barcode128 codeEAN = new Barcode128();
		codeEAN.setCode(""+patient_id);
		codeEAN.setX(1.5f);
		codeEAN.setN(1.5f);
		codeEAN.setSize(12f);
		codeEAN.setBaseline(-1f);
		codeEAN.setGuardBars(true);
		codeEAN.setBarHeight(20f);
		PdfPCell barcodecell = new PdfPCell(codeEAN.createImageWithBarcode(cb,
				null, null));
		barcodecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		barcodecell.setPaddingRight(5);
		barcodecell.setBorder(PdfPCell.NO_BORDER);
		barcodecell.setColspan(2);
		barcodecell.setRowspan(2);
		opdTable.addCell(barcodecell);
		opdTable.addCell(new Phrase("", font3));

		opdTable.addCell(new Phrase("Patient :", font3));
		opdTable.addCell(new Phrase(" "+patient_name, font3));

		PdfPCell agecell = new PdfPCell(new Phrase("" + "\n", font3));
		agecell.setPaddingBottom(5);
		agecell.setBorder(PdfPCell.NO_BORDER);
		agecell.setColspan(4);
		opdTable.addCell(new Phrase("Age : "+patient_age, font3));
		opdTable.addCell(agecell);

		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctor_name, font3));
		consultantcell.setPaddingBottom(5);
		consultantcell.setBorder(PdfPCell.NO_BORDER);
		consultantcell.setColspan(2);
		opdTable.addCell(new Phrase("Ref By :", font3));
		opdTable.addCell(consultantcell);
		opdTable.addCell(new Phrase("", font3));

		opdTable.addCell(new Phrase("Sex :", font3));
		opdTable.addCell(new Phrase(" "+patient_sex, font3));
		opdTable.addCell(new Phrase(" ", font3));

		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);

		if(reportCat1.size()>0)
		{
			table.addCell(createTable(reportCat1,reportTypes.get(0)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat2.size()>0)
		{
			table.addCell(createTable(reportCat2,reportTypes.get(1)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat3.size()>0)
		{
			table.addCell(createTable(reportCat3,reportTypes.get(2)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat4.size()>0)
		{
			table.addCell(createTable(reportCat4,reportTypes.get(3)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat5.size()>0)
		{
			table.addCell(createTable(reportCat5,reportTypes.get(4)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat6.size()>0)
		{
			table.addCell(createTable(reportCat6,reportTypes.get(5)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat7.size()>0)
		{
			table.addCell(createTable(reportCat7,reportTypes.get(6)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat8.size()>0)
		{
			table.addCell(createTable(reportCat8,reportTypes.get(7)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat9.size()>0)
		{
			table.addCell(createTable(reportCat9,reportTypes.get(8)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat10.size()>0)
		{
			table.addCell(createTable(reportCat10,reportTypes.get(9)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat11.size()>0)
		{
			table.addCell(createTable(reportCat11,reportTypes.get(10)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat12.size()>0)
		{
			table.addCell(createTable(reportCat12,reportTypes.get(11)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat13.size()>0)
		{
			table.addCell(createTable(reportCat13,reportTypes.get(12)));
			table.addCell(new Phrase("  ", font3));
		}
		
		if(reportCat14.size()>0)
		{
			table.addCell(createTable(reportCat14,reportTypes.get(13)));
			table.addCell(new Phrase("  ", font3));
		}
		if(reportCat15.size()>0)
		{
			table.addCell(createTable(reportCat15,reportTypes.get(14)));
			table.addCell(new Phrase("  ", font3));
		}
		
		for (int i = 0; i < report010Cat1.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat1.get(i),reportTypes.get(0)));
		}
		
		for (int i = 0; i < report010Cat2.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat2.get(i),reportTypes.get(1)));
		}
		
		for (int i = 0; i < report010Cat3.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat3.get(i),reportTypes.get(2)));
		}
		
		for (int i = 0; i < report010Cat4.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat4.get(i),reportTypes.get(3)));
		}
		
		for (int i = 0; i < report010Cat5.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat5.get(i),reportTypes.get(4)));
		}
		
		for (int i = 0; i < report010Cat6.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat6.get(i),reportTypes.get(5)));
		}
		
		for (int i = 0; i < report010Cat7.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat7.get(i),reportTypes.get(6)));
		}
		
		for (int i = 0; i < report010Cat8.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat8.get(i),reportTypes.get(7)));
		}
		
		for (int i = 0; i < report010Cat9.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat9.get(i),reportTypes.get(8)));
		}
		
		
		for (int i = 0; i < report010Cat10.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat10.get(i),reportTypes.get(9)));
		}
		
		for (int i = 0; i < report010Cat11.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat11.get(i),reportTypes.get(10)));
		}
		for (int i = 0; i < report010Cat12.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat12.get(i),reportTypes.get(11)));
		}
		
		for (int i = 0; i < report010Cat13.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat13.get(i),reportTypes.get(12)));
		}
		
		for (int i = 0; i < report010Cat14.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat14.get(i),reportTypes.get(13)));
		}
		
		for (int i = 0; i < report010Cat15.size(); i++) {
			table.addCell(new Phrase("  ", font3));
			table.addCell(createTableResult(report010Cat15.get(i),reportTypes.get(14)));
		}
		
		PdfPCell pathologist = new PdfPCell(
				new Phrase(
						"Pathologist",
						font3));
		pathologist.setBorder(PdfPCell.NO_BORDER);
		pathologist.setHorizontalAlignment(Element.ALIGN_RIGHT);
		pathologist.setPaddingBottom(20);
		pathologist.setPaddingTop(20);
		table.addCell(pathologist);

		PdfPCell footer = new PdfPCell(
				new Phrase(
						"Note : This report is not to be considered for medico-legal purposes.",
						font3));
		footer.setBorder(PdfPCell.NO_BORDER);
		footer.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(footer);

		PdfPCell footer2 = new PdfPCell(
				new Phrase(
						"Saturday : General OPD Closed, Sunday : Working, Emergency : 24x7",
						font4));
		footer2.setBorder(PdfPCell.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);

		table.addCell(footer2);

		document.add(table);
		//onEndPage(wr,document);
		document.close();

		new OpenFile(RESULT);
		try {
			copyFileUsingJava7Files(RESULT, serverfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			allReportsForPrint.loadDataToTable(patient_id, roomNo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void object(AllReportsForPrint allReportsForPrintObject)
	{
		allReportsForPrint=allReportsForPrintObject;
	}
	public PdfPTable createTable(Vector<String> vector,String reportName) {

		float[] packagesTableCellWidth = { 1.2f, 0.6f, 0.6f, 0.6f, 1f };
		PdfPTable examResults = new PdfPTable(packagesTableCellWidth);
		examResults.setWidthPercentage(30);
		examResults.getDefaultCell().setBorder(0);

		PdfPCell reportTitle = new PdfPCell(new Phrase(
				reportName+" Report", font2));
		reportTitle.setBorder(PdfPCell.NO_BORDER);
		reportTitle.setColspan(5);
		reportTitle.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell testColumn = new PdfPCell(new Phrase("Test Name", smallBold));
		testColumn.setBorderWidth(1f);
		testColumn.setRowspan(2);
		testColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell resultColumn = new PdfPCell(new Phrase("Results", smallBold));
		resultColumn.setBorderWidth(1f);
		resultColumn.setColspan(2);
		resultColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell flagsColumn = new PdfPCell(new Phrase("Unit", smallBold));
		flagsColumn.setBorderWidth(1f);
		flagsColumn.setRowspan(2);
		flagsColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell referenceRangeColumn = new PdfPCell(new Phrase(
				"Reference Range", smallBold));
		referenceRangeColumn.setBorderWidth(1f);
		referenceRangeColumn.setRowspan(2);
		referenceRangeColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell result1 = new PdfPCell(new Phrase("Within Range", smallBold));
		result1.setBorderWidth(0.3f);
		result1.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell result2 = new PdfPCell(new Phrase("Out of Range", smallBold));
		result2.setBorderWidth(0.3f);
		result2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		result2.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		examResults.addCell(reportTitle);
		examResults.addCell(testColumn);
		examResults.addCell(resultColumn);
		examResults.addCell(flagsColumn);
		examResults.addCell(referenceRangeColumn);
		examResults.addCell(result1);
		examResults.addCell(result2);

		for (int i = 0; i < vector.size(); i++) {
			populateTestResults(vector.get(i));
			for (int j = 0; j < testName.length; j++) {
				
				PdfPCell testColumnValue = new PdfPCell(new Phrase(""+testName[j],
						smallBold));
				testColumnValue.setBorderWidth(0.2f);
				testColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
				examResults.addCell(testColumnValue);
				
				if(results[j].equals("Within Range"))
				{
					
					PdfPCell resultColumnValue = new PdfPCell(new Phrase(""+values[j],
							smallBold));
					resultColumnValue.setBorderWidth(0.2f);

					resultColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					PdfPCell resultColumnValue1 = new PdfPCell(new Phrase("   ",
							font2));
					resultColumnValue1.setBorderWidth(0.2f);

					resultColumnValue1.setHorizontalAlignment(Element.ALIGN_CENTER);
					examResults.addCell(resultColumnValue);
					examResults.addCell(resultColumnValue1);
				}
				else {
					

					PdfPCell resultColumnValue = new PdfPCell(new Phrase("   ",
							smallBold));
					resultColumnValue.setBorderWidth(0.2f);

					resultColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					PdfPCell resultColumnValue1 = new PdfPCell(new Phrase(""+values[j],
							font2));
					resultColumnValue1.setBorderWidth(0.2f);

					resultColumnValue1.setHorizontalAlignment(Element.ALIGN_CENTER);
					
					examResults.addCell(resultColumnValue);
					examResults.addCell(resultColumnValue1);
				}
				PdfPCell unitsColumn = new PdfPCell(new Phrase(""+units[j],
						smallBold));
				unitsColumn.setBorderWidth(0.2f);

				unitsColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

//				PdfPCell flagsColumnValue = new PdfPCell(new Phrase(""+results[j],
//						smallBold));
//				flagsColumnValue.setBorderWidth(0.2f);
//				flagsColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);

				PdfPCell referenceRangeColumnValue = new PdfPCell(new Phrase(
						""+limits[j]+" "+units[j], smallBold));
				referenceRangeColumnValue.setBorderWidth(0.2f);
				referenceRangeColumnValue
						.setHorizontalAlignment(Element.ALIGN_CENTER);

				
				examResults.addCell(unitsColumn);
				examResults.addCell(referenceRangeColumnValue);
			}
			
		}

		return examResults;
	}

	public PdfPTable createTableResult(String index,String reportName) {

		float[] packagesTableCellWidth = { 1f, 1f, 1f };
		PdfPTable examResults = new PdfPTable(packagesTableCellWidth);
		examResults.setWidthPercentage(30);
		examResults.getDefaultCell().setBorder(0);

		PdfPCell reportTitle = new PdfPCell(new Phrase(
				reportName+" Report", font2));
		reportTitle.setBorder(PdfPCell.NO_BORDER);
		reportTitle.setColspan(4);
		reportTitle.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell physicalColumn = new PdfPCell(
				new Phrase("Physical", smallBold));
		physicalColumn.setBorderWidth(1f);
		physicalColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell chemicalColumn = new PdfPCell(
				new Phrase("Chemical", smallBold));
		chemicalColumn.setBorderWidth(1f);

		chemicalColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		PdfPCell mehColumn = new PdfPCell(new Phrase("M/Exam./HPF", smallBold));
		mehColumn.setBorderWidth(1f);
		mehColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		examResults.addCell(reportTitle);
		examResults.addCell(physicalColumn);
		examResults.addCell(chemicalColumn);
		examResults.addCell(mehColumn);
		Object[][] value=populateTestResults2(""+index);
		for (int i = 0; i < 10; i++) {
			PdfPCell physicalColumnValue = new PdfPCell(new Phrase(""+value[i][0],
					smallBold));
		///	physicalColumnValue.setBorderWidth(0.2f);
			physicalColumnValue.setBorder(PdfPCell.NO_BORDER);
			physicalColumnValue.setPaddingLeft(20);
		//	physicalColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell chemicalColumnValue = new PdfPCell(new Phrase(
					""+value[i][1], smallBold));
		///	chemicalColumnValue.setBorderWidth(0.2f);
			chemicalColumnValue.setPaddingLeft(20);
			chemicalColumnValue.setBorder(PdfPCell.NO_BORDER);

			//chemicalColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);

			PdfPCell mehColumnValue = new PdfPCell(new Phrase(""+value[i][2],
					smallBold));
		///	mehColumnValue.setBorderWidth(0.2f);
			mehColumnValue.setPaddingLeft(20);
			mehColumnValue.setBorder(PdfPCell.NO_BORDER);
			//mehColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);

			examResults.addCell(physicalColumnValue);
			examResults.addCell(chemicalColumnValue);
			examResults.addCell(mehColumnValue);

		}

		return examResults;
	}

	public void getExamData(String exam_counter) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataExamID(exam_counter);
			while (rs.next()) {
				patient_id = rs.getObject(3).toString();
				patient_name = rs.getObject(4).toString();
				doctor_name = rs.getObject(5).toString();
				rs.getObject(6).toString();
			}
			db.closeConnection();
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
				patient_sex = resultSet.getObject(3).toString();
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

			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient/" + patient_id
					+ "/Reports/");
			if (!dir.exists())
				dir.mkdirs();
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("hhmmss");
		// System.out.print(dateFormat.format(cal1.getTime()));
		reportName=patient_id+"_"+dateFormat.format(cal1.getTime())+".pdf";
		serverfile= mainDir + "/HMS/Patient/" + patient_id + "/Reports/" + reportName;
		new File("Reports").mkdir();
		RESULT="Reports/tempReport.pdf";
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
			while ((line = bufferedReader.readLine()) != null) {
				// System.out.println(line);
				str = line;
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



	public void filterData() {
		for (int i = 0; i < reportTypes.size(); i++) {

			for (int j = 0; j < Rows_Object_Array.length; j++) {

				if (reportTypes.get(i).equals(
						Rows_Object_Array[j][2].toString())) {
					if (i == 0)
						cat1.add(Rows_Object_Array[j][0].toString());
					else if (i == 1) {
						cat2.add(Rows_Object_Array[j][0].toString());

					} else if (i == 2) {
						cat3.add(Rows_Object_Array[j][0].toString());

					} else if (i == 3) {
						cat4.add(Rows_Object_Array[j][0].toString());

					} else if (i == 4) {
						cat5.add(Rows_Object_Array[j][0].toString());

					} else if (i == 5) {
						cat6.add(Rows_Object_Array[j][0].toString());

					} else if (i == 6) {
						cat7.add(Rows_Object_Array[j][0].toString());

					} else if (i == 7) {
						cat8.add(Rows_Object_Array[j][0].toString());

					} else if (i == 8) {
						cat9.add(Rows_Object_Array[j][0].toString());

					} else if (i == 9) {
						cat10.add(Rows_Object_Array[j][0].toString());

					} else if (i == 10) {
						cat11.add(Rows_Object_Array[j][0].toString());

					} else if (i == 11) {
						cat12.add(Rows_Object_Array[j][0].toString());

					} else if (i == 12) {
						cat13.add(Rows_Object_Array[j][0].toString());

					} else if (i == 13) {
						cat14.add(Rows_Object_Array[j][0].toString());

					} else if (i == 14) {
						cat15.add(Rows_Object_Array[j][0].toString());

					}

				}
			}
		}
		
		
		for (int k = 0; k <cat1.size(); k++) {
			if(getTestResultCat(cat1.get(k)).equals("100"))
			{
				reportCat1.add(cat1.get(k));
			}
			else if(getTestResultCat(cat1.get(k)).equals("010")){
				report010Cat1.add(cat1.get(k));
			}
		}
		
		for (int k = 0; k <cat2.size(); k++) {
			if(getTestResultCat(cat2.get(k)).equals("100"))
			{
				reportCat2.add(cat2.get(k));
			}
			else if(getTestResultCat(cat2.get(k)).equals("010")){
				report010Cat2.add(cat2.get(k));
			}
		}
		
		for (int k = 0; k <cat3.size(); k++) {
			if(getTestResultCat(cat3.get(k)).equals("100"))
			{
				reportCat3.add(cat3.get(k));
			}
			else if(getTestResultCat(cat3.get(k)).equals("010")){
				report010Cat3.add(cat3.get(k));
			}
		}
		
		for (int k = 0; k <cat4.size(); k++) {
			if(getTestResultCat(cat4.get(k)).equals("100"))
			{
				reportCat4.add(cat4.get(k));
			}
			else if(getTestResultCat(cat4.get(k)).equals("010")){
				report010Cat4.add(cat4.get(k));
			}
		}
		
		for (int k = 0; k <cat5.size(); k++) {
			if(getTestResultCat(cat5.get(k)).equals("100"))
			{
				reportCat5.add(cat5.get(k));
			}
			else if(getTestResultCat(cat5.get(k)).equals("010")){
				report010Cat5.add(cat5.get(k));
			}
		}
		
		for (int k = 0; k <cat6.size(); k++) {
			if(getTestResultCat(cat6.get(k)).equals("100"))
			{
				reportCat6.add(cat6.get(k));
			}
			else if(getTestResultCat(cat6.get(k)).equals("010")){
				report010Cat6.add(cat6.get(k));
			}
		}
		
		for (int k = 0; k <cat7.size(); k++) {
			if(getTestResultCat(cat7.get(k)).equals("100"))
			{
				reportCat7.add(cat7.get(k));
			}
			else if(getTestResultCat(cat7.get(k)).equals("010")){
				report010Cat7.add(cat7.get(k));
			}
		}
		
		for (int k = 0; k <cat8.size(); k++) {
			if(getTestResultCat(cat8.get(k)).equals("100"))
			{
				reportCat8.add(cat8.get(k));
			}
			else if(getTestResultCat(cat8.get(k)).equals("010")){
				report010Cat8.add(cat8.get(k));
			}
		}
		for (int k = 0; k <cat9.size(); k++) {
			if(getTestResultCat(cat9.get(k)).equals("100"))
			{
				reportCat9.add(cat9.get(k));
			}
			else if(getTestResultCat(cat9.get(k)).equals("010")){
				report010Cat9.add(cat9.get(k));
			}
		}
		for (int k = 0; k <cat10.size(); k++) {
			if(getTestResultCat(cat10.get(k)).equals("100"))
			{
				reportCat10.add(cat10.get(k));
			}
			else if(getTestResultCat(cat10.get(k)).equals("010")){
				report010Cat10.add(cat10.get(k));
			}
		}
		for (int k = 0; k <cat11.size(); k++) {
			if(getTestResultCat(cat11.get(k)).equals("100"))
			{
				reportCat11.add(cat11.get(k));
			}
			else if(getTestResultCat(cat11.get(k)).equals("010")){
				report010Cat11.add(cat11.get(k));
			}
		}
		for (int k = 0; k <cat12.size(); k++) {
			if(getTestResultCat(cat12.get(k)).equals("100"))
			{
				reportCat12.add(cat12.get(k));
			}
			else if(getTestResultCat(cat12.get(k)).equals("010")){
				report010Cat12.add(cat12.get(k));
			}
		}
		for (int k = 0; k <cat13.size(); k++) {
			if(getTestResultCat(cat13.get(k)).equals("100"))
			{
				reportCat13.add(cat13.get(k));
			}
			else if(getTestResultCat(cat13.get(k)).equals("010")){
				report010Cat13.add(cat13.get(k));
			}
		}
		for (int k = 0; k <cat14.size(); k++) {
			if(getTestResultCat(cat14.get(k)).equals("100"))
			{
				reportCat14.add(cat14.get(k));
			}
			else if(getTestResultCat(cat14.get(k)).equals("010")){
				report010Cat14.add(cat14.get(k));
			}
		}
		for (int k = 0; k <cat15.size(); k++) {
			if(getTestResultCat(cat15.get(k)).equals("100"))
			{
				reportCat15.add(cat15.get(k));
			}
			else if(getTestResultCat(cat15.get(k)).equals("010")){
				report010Cat15.add(cat15.get(k));
			}
		}
	}
	
	public String getTestResultCat(String testCounter)
	{
		String str="";
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveCategory(testCounter);
			while (rs.next()) {
				str = "" + rs.getObject(1);
			}
			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println("Category "+str);
		return str;
	}
	
	public void populateTestResults(String testCounterNo) {
		
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID(testCounterNo);
	
			testName = new Object[NumberOfRows];
			
			
			limits = new Object[NumberOfRows];
			
			
			values = new Object[NumberOfRows];
			units= new Object[NumberOfRows];
		
			results = new Object[NumberOfRows];
			
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][3];

			int R = 0;
			while (rs.next()) {
				
					testName[R] = rs.getObject(1);
					limits[R] = rs.getObject(2)+" - "+rs.getObject(3);
					values[R] = rs.getObject(4)+"";
					units[R]=""+rs.getObject(6);
					results[R] = rs.getObject(5);
				R++;
			}
			
			for (int i = 0; i < testName.length; i++) {
				
					Rows_Object_Array[i][0] =testName[i];
					Rows_Object_Array[i][1] =limits[i];
					Rows_Object_Array[i][2] =values[i];
					System.out.println(testName[i]+"  "+limits[i]+" "+values[i]);
				
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		ExamDBConnection examDBConnection=new ExamDBConnection();
		try {
			examDBConnection.updateExamDataStatus(testCounterNo, reportName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		examDBConnection.closeConnection();
	}
	public Object[][] populateTestResults2(String testCounterNo) {
		System.out.println("Test Counter"+testCounterNo);
		Object Rows_Object_Arrayvalue[][] = null;
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID2(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID2(testCounterNo);
			String physical[];
			physical = new String[10];
			
			String chemical[];
			chemical = new String[10];
			
			String meh[];
			meh = new String[10];
			
			String results[];
			results = new String[10];
			for (int i = 0; i < 10; i++) {
				physical[i]=" ";
				chemical[i]=" ";
				meh[i]=" ";
			}
			// to set rows in this array
		
			Rows_Object_Arrayvalue = new Object[10][3];

			int R = 0;
			while (rs.next()) {
				for (int i = 1; i < 11; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						physical[i-1]=(str);
				}

				for (int i = 11; i < 21; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						chemical[i-11]=(str);
				}

				for (int i = 21; i < 31; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						meh[i-21]=(str);
				}
				R++;
			}
			
			for (int i = 0; i < 10; i++) {
				
				Rows_Object_Arrayvalue[i][0] =physical[i];
				Rows_Object_Arrayvalue[i][1] =chemical[i];
				Rows_Object_Arrayvalue[i][2] =meh[i];
				
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		ExamDBConnection examDBConnection=new ExamDBConnection();
		try {
			examDBConnection.updateExamDataStatus(testCounterNo, reportName);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		examDBConnection.closeConnection();
		return Rows_Object_Arrayvalue;
	}
	
	public void onEndPage(PdfWriter writer, Document document) {
        Rectangle rect = writer.getBoxSize("art");
        switch(writer.getPageNumber() % 2) {
        case 0:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_RIGHT, new Phrase(
            				"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
                    rect.getRight(), rect.getTop(), 0);
            break;
        case 1:
            ColumnText.showTextAligned(writer.getDirectContent(),
                    Element.ALIGN_LEFT, new Phrase(
            				"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7", font4),
                    rect.getLeft(), rect.getTop(), 0);
            break;
        }
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
                (rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18, 0);
    }
	public int record_counter()
	{
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		String[] data=new String[4];
		data[0]=patient_id;
		data[1]=serverfile;
		data[2]=timeStamp;
		int value=0;
		TestRecordDBConnection testRecordDBConnection=new TestRecordDBConnection();
		
		try {
			value=testRecordDBConnection.inserData(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testRecordDBConnection.closeConnection();
		}
		
		testRecordDBConnection.closeConnection();
		return value;
	}
}
