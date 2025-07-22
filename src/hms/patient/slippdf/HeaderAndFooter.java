package hms.patient.slippdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderAndFooter extends PdfPageEventHelper {

	
@Override
public void onEndPage (PdfWriter writer, Document document) {
    Rectangle rect = writer.getBoxSize("art");
    switch(writer.getPageNumber() % 2) {
    case 0:
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("even header"),
//                rect.getBorderWidthRight(), rect.getBorderWidthTop(), 0);
//        break;
    case 1:
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("Page "+String.format("%d", writer.getPageNumber())),
                300f, 30f, 0);
       
        break;
    }
}
}