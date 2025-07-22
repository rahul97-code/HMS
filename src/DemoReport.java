import hms.patient.slippdf.OPDSlippdf2;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hslf.record.Environment;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;

public class DemoReport {

	public static void main(String[] arg) {
		new DemoReport().automaticPDF();
	}
	   PdfTemplate total;
	    String header="Sukhpal";
	public String outfile;
	Font heading = new Font(Font.FontFamily.HELVETICA, 30, Font.BOLD,new BaseColor(0, 0, 26));
	Font heading2 = new Font(Font.FontFamily.HELVETICA, 25,Font.NORMAL,new BaseColor(0, 0, 26));
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 13,
			Font.BOLD, BaseColor.WHITE);
	Font smallBold = new Font(Font.FontFamily.HELVETICA, 13,Font.NORMAL,new BaseColor(0, 0, 26));
	Font smallBold2 = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);

	Font footer = new Font(Font.FontFamily.HELVETICA, 10, Font.BOLD);
	Font footer2 = new Font(Font.FontFamily.HELVETICA, 8);
	
	float[] testDataCell = { 0.7f, 1.0f };

	float[] headingTableCell = { 0.3f, 1.0f };
	
	float[] usersTableCell = { 1.0f,1.0f,1.0f,0.5f,0.8f };

	public String automaticPDF() {

		File dir = new File("C:/Users/MDI/Desktop/demo.pdf");
		outfile = "" + dir;
		try {
			Document document = new Document(PageSize.A4, 40, 40, 30, 30);
			PdfWriter writer = PdfWriter.getInstance(document,
					new FileOutputStream(outfile));
			RedBorder event = new RedBorder();
			writer.setPageEvent(event);
			document.open();
			document.setPageCount(1);
			createTable1(document);
			
			writeFooterTable(writer, document);
			
			document.close();
		} catch (Exception e) {
			// TODO: handle exception

		}
		return outfile;
	}

	public class RedBorder extends PdfPageEventHelper {
		@Override
		public void onEndPage(PdfWriter writer, Document document) {
			PdfContentByte canvas = writer.getDirectContent();
			// Rectangle rect = document.getPageSize();
			float x = 20, y = 20, w = document.getPageSize().getWidth() - 20, h = document
					.getPageSize().getHeight() - 20;

			float x1 = 24, y1 = 24, w1 = document.getPageSize().getWidth() - 24, h1 = document
					.getPageSize().getHeight() - 24;
			Rectangle rect = new Rectangle(x, y, w, h);

			rect.setBorder(Rectangle.BOX); // left, right, top, bottom border
			rect.setBorderWidth(1.5f); // a width of 5 user units
			rect.setBorderColor(BaseColor.BLACK); // a red border
			rect.setUseVariableBorders(true); // the full width will be visible
			canvas.rectangle(rect);

			Rectangle rect1 = new Rectangle(x1, y1, w1, h1);

			rect1.setBorder(Rectangle.BOX); // left, right, top, bottom border
			rect1.setBorderWidth(1); // a width of 5 user units
			rect1.setBorderColor(BaseColor.BLACK); // a red border
			rect1.setUseVariableBorders(true); // the full width will be visible
			// canvas.rectangle(rect1);
			
			 PdfPTable table = new PdfPTable(3);
             total = writer.getDirectContent().createTemplate(30, 16);
            try {
                table.setWidths(new int[]{24, 24, 2});
                table.setTotalWidth(527);
                table.setLockedWidth(true);
                table.getDefaultCell().setFixedHeight(20);
                table.getDefaultCell().setBorder(Rectangle.NO_BORDER);
                table.addCell(header);
                table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(String.format("Page %d of", writer.getPageNumber()));
                PdfPCell cell = new PdfPCell(Image.getInstance(total));
                cell.setBorder(Rectangle.NO_BORDER);
                table.addCell(cell);
                table.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
                  
            }
            catch(DocumentException de) {
                throw new ExceptionConverter(de);
            }
		}
		public void onCloseDocument(PdfWriter writer, Document document) {
		     
            ColumnText.showTextAligned(total, Element.ALIGN_LEFT,
                    new Phrase(String.valueOf(writer.getPageNumber() - 1)),
                    2, 2, 0);
        }
	}

	 private void writeFooterTable(PdfWriter writer, Document document) {
	        final int FIRST_ROW = 0;
	        final int LAST_ROW = -1;
	        
	    	PdfPTable footerTable = new PdfPTable(1);
			footerTable.setWidthPercentage(100);
			footerTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);
			footerTable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
			footerTable.addCell(new Phrase("Advanced Microdevices Pvt. Ltd.", footer));
			footerTable.addCell(new Phrase("20-21, Industrial Area, Ambala Cantt. PIN - 133006, INDIA", footer2));
			footerTable.addCell(new Phrase("Phone: +91-171-2699 290, 2966471  Fax: +91-171-2699 221", footer2));
			footerTable.addCell(new Phrase("Email: info@mdimembrane.com  Wesite: www.mdimembrane.com", footer2));
			
			PdfPCell cell;
			cell = new PdfPCell(footerTable);
			cell.setBorder(Rectangle.BOX);
			cell.setPadding(5);
			PdfPTable footerTable1 = new PdfPTable(1);
			footerTable1.setWidthPercentage(100);
			footerTable1.addCell(cell);
	        
	        //Table must have absolute width set.
	        if(footerTable1.getTotalWidth()==0)
	        	footerTable1.setTotalWidth((document.right()-document.left())*footerTable1.getWidthPercentage()/100f);
	        footerTable1.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom()+footerTable1.getTotalHeight(),writer.getDirectContent());
	    }
	private void createTable1(Document subCatPart) throws DocumentException,
			MalformedURLException, IOException {
		java.net.URL imgURL = DemoReport.class.getResource("/icons/logo.png");

		Image image = Image.getInstance(imgURL);

		image.scalePercent(60);
		java.text.DateFormat.getDateTimeInstance().format(
				Calendar.getInstance().getTime());
		Paragraph preface = new Paragraph();

		PdfPTable headingTable = new PdfPTable(headingTableCell);
		headingTable.setWidthPercentage(100);
		headingTable.getDefaultCell().setBorder(Rectangle.BOX);

		PdfPCell cell;
		cell = new PdfPCell(image);
		cell.setRowspan(2);
		cell.setPadding(5);
		cell.setBorder(Rectangle.LEFT | Rectangle.TOP | Rectangle.BOTTOM);
		headingTable.addCell(cell);

		cell = new PdfPCell(new Phrase("Test Report", heading));
		cell.setPadding(5);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		cell.setBorder(Rectangle.RIGHT | Rectangle.TOP);
		headingTable.addCell(cell);

		cell = new PdfPCell(new Phrase("FilterCheck-06", heading2));
		cell.setPadding(5);
		cell.setBorder(Rectangle.RIGHT | Rectangle.BOTTOM);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		headingTable.addCell(cell);

		preface.add(headingTable);

	
		Paragraph para1 = new Paragraph();
        para1.setSpacingBefore(10);
		preface.add(para1);
		
		PdfPTable dataTable = new PdfPTable(testDataCell);
		dataTable.setWidthPercentage(100);
		dataTable.getDefaultCell().setBorder(Rectangle.BOX);

		dataTable.getDefaultCell().setPadding(5);

		cell = new PdfPCell(new Phrase("Test Setup", font1));
		cell.setBackgroundColor(new BaseColor(44, 130, 201));
		cell.setUseBorderPadding(true);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		dataTable.addCell(cell);
		dataTable.addCell(new Phrase("Instrument Serial Number", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Test Serial Number", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Test Date", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Test Time", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Process Area", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Test Type", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Test Mode", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		cell = new PdfPCell(new Phrase("Product Information", font1));
		cell.setBackgroundColor(new BaseColor(44, 130, 201));
		cell.setUseBorderPadding(true);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		dataTable.addCell(cell);
		dataTable.addCell(new Phrase("Product Name", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Product Lot Number", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		cell = new PdfPCell(new Phrase("Test Filter Information", font1));
		cell.setBackgroundColor(new BaseColor(44, 130, 201));
		cell.setUseBorderPadding(true);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		dataTable.addCell(cell);
		dataTable.addCell(new Phrase("Catalog Number", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Lot Number", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Wetting Fluid", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Pore Size", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Filter Size", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Minimum Bubble Point", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		cell = new PdfPCell(new Phrase("Test Results :", font1));
		cell.setBackgroundColor(new BaseColor(44, 130, 201));
		cell.setUseBorderPadding(true);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		dataTable.addCell(cell);
		dataTable.addCell(new Phrase("Test Value", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		dataTable.addCell(new Phrase("Pass/Fail", smallBold));
		dataTable.addCell(new Phrase("", smallBold));

		preface.add(dataTable);

		para1.setSpacingBefore(20);
		preface.add(para1);
		
		PdfPTable usersTable = new PdfPTable(usersTableCell);
		usersTable.setWidthPercentage(100);
		usersTable.getDefaultCell().setBorder(0);
		usersTable.getDefaultCell().setVerticalAlignment(Element.ALIGN_BOTTOM);
		
		java.net.URL verifyURL = DemoReport.class.getResource("/icons/verify.png");

		Image verifyImage = Image.getInstance(verifyURL);
		verifyImage.scalePercent(15);
		
		PdfPCell cell2 = new PdfPCell(verifyImage, false);
		//cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell2.setBorder(0);
     	
		usersTable.addCell(new Phrase("Sukhpal Saini", smallBold));
		usersTable.addCell(new Phrase("Bunty", smallBold));
		usersTable.addCell(cell2);
		usersTable.addCell(new Phrase("Date :", smallBold2));
		usersTable.addCell(new Phrase("2015-12-30", smallBold2));

		
		usersTable.addCell(new Phrase("Test Operator", smallBold2));
		usersTable.addCell(new Phrase("Supervisor", smallBold2));
		usersTable.addCell(new Phrase("Report Status", smallBold2));
		usersTable.addCell(new Phrase("Time :", smallBold2));
		usersTable.addCell(new Phrase("10:10:10 PM", smallBold2));

		
	

		preface.add(usersTable);
		subCatPart.add(preface);
	}

}
