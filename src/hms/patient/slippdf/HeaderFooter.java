package hms.patient.slippdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.IOException;
import java.net.MalformedURLException;

public class HeaderFooter extends PdfPageEventHelper {

    private PdfTemplate t;
    private Image total;

    
    public void onOpenDocument(PdfWriter writer, Document document) {
        t = writer.getDirectContent().createTemplate(30, 16);
        try {
            total = Image.getInstance(t);
            total.setRole(PdfName.ACTIVATION);
        } catch (DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        try {
			addHeader(writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       // addFooter(writer);
    }

    private void addHeader(PdfWriter writer) throws MalformedURLException, IOException{
        PdfPTable header = new PdfPTable(2);
        try {
            // set defaults
            header.setWidths(new int[]{2, 24});
            header.setTotalWidth(527);
            header.setLockedWidth(true);
            header.getDefaultCell().setFixedHeight(40);
            header.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
            header.getDefaultCell().setBorder(Rectangle.BOTTOM);
            header.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add image
            Image logo = Image.getInstance(HeaderFooter.class.getResource("/icons/rotaryLogo.png"));
            header.addCell(logo);

           
           
            PdfPTable headerContent = new PdfPTable(1);
            
            // add text
            PdfPCell text1 = new PdfPCell(new Phrase("ROTARY AMBALA CANCER AND GENERAL HOSPITAL", new Font(Font.FontFamily.HELVETICA, 17,Font.BOLD, BaseColor.BLACK)));
            text1.setBorder(Rectangle.NO_BORDER);
            text1.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerContent.addCell(text1);
            
            PdfPCell text2 = new PdfPCell(new Phrase("Managed By : Rotary Ambala Cancer Detection & Welfare Socity", new Font(Font.FontFamily.HELVETICA, 9)));
            text2.setBorder(Rectangle.NO_BORDER);
            text2.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerContent.addCell(text2);
            
            
            PdfPCell text3 = new PdfPCell(new Phrase("Address : RAM BAGH ROAD,OPPOSITE OLD DUSSEHRA GROUND,AMBALA CANTT.Ph : 0171-2690009,email- racgh2010@gmail.com", new Font(Font.FontFamily.HELVETICA, 7,Font.BOLD)));
            text3.setBorder(Rectangle.NO_BORDER);
            text3.setHorizontalAlignment(Element.ALIGN_CENTER);
            headerContent.addCell(text3);
            
            PdfPCell main = new PdfPCell(headerContent);
            main.setPaddingBottom(15);
            main.setPaddingLeft(10);
            main.setBorder(Rectangle.BOTTOM);
            main.setHorizontalAlignment(Element.ALIGN_CENTER);
            main.setBorderColor(BaseColor.LIGHT_GRAY);            
            header.addCell(main);
          
            // write content
            header.writeSelectedRows(0, -1, 34, 803, writer.getDirectContent());
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
        
    }

    private void addFooter(PdfWriter writer){
        PdfPTable footer = new PdfPTable(3);
        try {
            // set defaults
            footer.setWidths(new int[]{24, 2, 1});
            footer.setTotalWidth(527);
            footer.setLockedWidth(true);
            footer.getDefaultCell().setFixedHeight(40);
            footer.getDefaultCell().setBorder(Rectangle.TOP);
            footer.getDefaultCell().setBorderColor(BaseColor.LIGHT_GRAY);

            // add copyright
            footer.addCell(new Phrase("\u00A9 Memorynotfound.com", new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD)));

            // add current page count
            footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(new Phrase(String.format("Page %d of", writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)));

            // add placeholder for total page count
            PdfPCell totalPageCount = new PdfPCell(total);
            totalPageCount.setBorder(Rectangle.TOP);
            totalPageCount.setBorderColor(BaseColor.LIGHT_GRAY);
            footer.addCell(totalPageCount);

            // write page
            PdfContentByte canvas = writer.getDirectContent();
            canvas.beginMarkedContentSequence(PdfName.ACTIVATION);
            footer.writeSelectedRows(0, -1, 34, 50, canvas);
            canvas.endMarkedContentSequence();
        } catch(DocumentException de) {
            throw new ExceptionConverter(de);
        }
    }

    public void onCloseDocument(PdfWriter writer, Document document) {
        int totalLength = String.valueOf(writer.getPageNumber()).length();
        int totalWidth = totalLength * 5;
        ColumnText.showTextAligned(t, Element.ALIGN_RIGHT,
                new Phrase(String.valueOf(writer.getPageNumber()), new Font(Font.FontFamily.HELVETICA, 8)),
                totalWidth, 6, 0);
    }
}