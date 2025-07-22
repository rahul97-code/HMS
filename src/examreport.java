import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class examreport {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 17,
			Font.BOLD, BaseColor.WHITE);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	Font font;
	public static final String RESULT = "examslip.pdf";
	java.awt.Font customFont;

	public static void main(String[] args) throws DocumentException,
			IOException {

		new examreport();

	}

	public examreport() throws DocumentException, MalformedURLException, IOException {
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

		java.net.URL imgURL = examreport.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = examreport.class
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
				"ROTARY AMBALA CANCER AND GENRAL HOSPITAL" + "\n", font1));
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
						"Telephone No. : 0171-2690009, Mobile No. : 09813334999",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);
//
//		PdfPTable doctorTable = new PdfPTable(2);
//		doctorTable.getDefaultCell().setBorder(0);
//
//		for (int i = 0; i < 20; i++) {
//			doctorTable.addCell(new Phrase(" a  " + i, smallBold));
//		}
		PdfPCell labNameCell = new PdfPCell(new Paragraph("CLINICO PATH LAB", font4));
		labNameCell.setPaddingTop(1);
		labNameCell.setPaddingBottom(2);
		labNameCell.setBorder(Rectangle.NO_BORDER);
		labNameCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		labNameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		//labNameCell.setBorderWidth(0.8f);
		table.addCell(labNameCell);

		PdfPCell spaceCell = new PdfPCell(new Paragraph("    ", spaceFont));
		spaceCell.setBorder(Rectangle.NO_BORDER);
		spaceCell.setPaddingTop(1);
		table.addCell(spaceCell);
		//
		PdfPTable opdTable = new PdfPTable(7);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Token No." + "\n", font4));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(5);
		tokencell.setBackgroundColor(BaseColor.LIGHT_GRAY);

		PdfPCell tokenNocell = new PdfPCell(new Phrase("" + "\n", font4));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(5);
		tokenNocell.setBackgroundColor(BaseColor.LIGHT_GRAY);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("OPD Receipt No. :", font3));
		opdTable.addCell(new Phrase("", font3));

		PdfContentByte cb = wr.getDirectContent();
		Barcode128 codeEAN = new Barcode128();
		codeEAN.setCode("0001130000012");
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
		barcodecell.setBorder(Rectangle.NO_BORDER);
		barcodecell.setColspan(2);
		barcodecell.setRowspan(2);
		opdTable.addCell(barcodecell);
		opdTable.addCell(new Phrase("", font3));

		opdTable.addCell(new Phrase("Patient :", font3));
		opdTable.addCell(new Phrase("", font3));

		PdfPCell agecell = new PdfPCell(new Phrase("" + "\n", font3));
		agecell.setPaddingBottom(5);
		agecell.setBorder(Rectangle.NO_BORDER);
		agecell.setColspan(4);
		opdTable.addCell(new Phrase("Age :", font3));
		opdTable.addCell(agecell);

		PdfPCell consultantcell = new PdfPCell(new Phrase("" + "\n", font3));
		consultantcell.setPaddingBottom(5);
		consultantcell.setBorder(Rectangle.NO_BORDER);
		consultantcell.setColspan(2);
		opdTable.addCell(new Phrase("Consultant :", font3));
		opdTable.addCell(consultantcell);
		opdTable.addCell(new Phrase("", font3));

		opdTable.addCell(new Phrase("Amt Received :", font3));
		opdTable.addCell(new Phrase("`0", font));
		opdTable.addCell(new Phrase("Signature", font3));

		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		opdTable.addCell(new Phrase("", font3));
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);

		table.addCell(createTable());
		table.addCell(new Phrase("  ", font3));
		table.addCell(createTableResult());

		PdfPCell footer = new PdfPCell(
				new Phrase(
						"Note : This report is not to be considered for medico-legal purposes.",
						font3));
		footer.setBorder(Rectangle.NO_BORDER);
		footer.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(footer);

		PdfPCell footer2 = new PdfPCell(new Phrase(
				"Saturday : General OPD Closed, Sunday : Working, Emergency : 24x7", font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setPaddingBottom(5);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		footer2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		
		table.addCell(footer2);
		
		document.add(table);
		document.close();

	}
	
	public PdfPTable createTable()
	{

		float[] packagesTableCellWidth = { 1f, 0.6f, 0.6f, 1.6f};
		PdfPTable examResults = new PdfPTable(packagesTableCellWidth);
		examResults.setWidthPercentage(30);
		examResults.getDefaultCell().setBorder(0);

		PdfPCell reportTitle = new PdfPCell(new Phrase(
				"Hormones and Tomour Marker Report", font2));
		reportTitle.setBorder(Rectangle.NO_BORDER);
		reportTitle.setColspan(4);
		reportTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell testColumn = new PdfPCell(new Phrase(
				"Test Name", smallBold));
		testColumn.setBorderWidth(1f);
		testColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell resultColumn = new PdfPCell(new Phrase(
				"Results", smallBold));
		resultColumn.setBorderWidth(1f);
		
		resultColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell flagsColumn = new PdfPCell(new Phrase(
				"Flags", smallBold));
		flagsColumn.setBorderWidth(1f);
		flagsColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell referenceRangeColumn = new PdfPCell(new Phrase(
				"Reference Range", smallBold));
		referenceRangeColumn.setBorderWidth(1f);
		referenceRangeColumn.setHorizontalAlignment(Element.ALIGN_CENTER);

		examResults.addCell(reportTitle);
		examResults.addCell(testColumn);
		examResults.addCell(resultColumn);
		examResults.addCell(flagsColumn);
		examResults.addCell(referenceRangeColumn);
		
		for (int i = 0; i < 5; i++) {
			PdfPCell testColumnValue = new PdfPCell(new Phrase(
					"Test Name", smallBold));
			testColumnValue.setBorderWidth(0.2f);
			testColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell resultColumnValue = new PdfPCell(new Phrase(
					"234 nmn/tt", smallBold));
			resultColumnValue.setBorderWidth(0.2f);
			
			resultColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell flagsColumnValue = new PdfPCell(new Phrase(
					"out of range", smallBold));
			flagsColumnValue.setBorderWidth(0.2f);
			flagsColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell referenceRangeColumnValue = new PdfPCell(new Phrase(
					"213-678", smallBold));
			referenceRangeColumnValue.setBorderWidth(0.2f);
			referenceRangeColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);

			examResults.addCell(testColumnValue);
			examResults.addCell(resultColumnValue);
			examResults.addCell(flagsColumnValue);
			examResults.addCell(referenceRangeColumnValue);
		}
		

	
		
		
		return examResults;
	}

	
	public PdfPTable createTableResult()
	{

		float[] packagesTableCellWidth = { 1f, 1f, 1f};
		PdfPTable examResults = new PdfPTable(packagesTableCellWidth);
		examResults.setWidthPercentage(30);
		examResults.getDefaultCell().setBorder(0);

		PdfPCell reportTitle = new PdfPCell(new Phrase(
				"Hormones and Tomour Marker Report", font2));
		reportTitle.setBorder(Rectangle.NO_BORDER);
		reportTitle.setColspan(4);
		reportTitle.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell physicalColumn = new PdfPCell(new Phrase(
				"Physical", smallBold));
		physicalColumn.setBorderWidth(1f);
		physicalColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell chemicalColumn = new PdfPCell(new Phrase(
				"Chemical", smallBold));
		chemicalColumn.setBorderWidth(1f);
		
		chemicalColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
		
		PdfPCell mehColumn = new PdfPCell(new Phrase(
				"M/Exam./HPF", smallBold));
		mehColumn.setBorderWidth(1f);
		mehColumn.setHorizontalAlignment(Element.ALIGN_CENTER);
	
		examResults.addCell(reportTitle);
		examResults.addCell(physicalColumn);
		examResults.addCell(chemicalColumn);
		examResults.addCell(mehColumn);
		
		for (int i = 0; i < 5; i++) {
			PdfPCell physicalColumnValue = new PdfPCell(new Phrase(
					"Test Name", smallBold));
			physicalColumnValue.setBorderWidth(0.2f);
			physicalColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell chemicalColumnValue = new PdfPCell(new Phrase(
					"234 nmn/tt", smallBold));
			chemicalColumnValue.setBorderWidth(0.2f);
			
			chemicalColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			
			PdfPCell mehColumnValue = new PdfPCell(new Phrase(
					"out of range", smallBold));
			mehColumnValue.setBorderWidth(0.2f);
			mehColumnValue.setHorizontalAlignment(Element.ALIGN_CENTER);
			

			examResults.addCell(physicalColumnValue);
			examResults.addCell(chemicalColumnValue);
			examResults.addCell(mehColumnValue);
			
		}
		

	
		
		
		return examResults;
	}
}