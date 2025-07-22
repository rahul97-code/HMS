package hms.patient.slippdf;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class ExamReportFooter extends PdfPageEventHelper {

	
@Override
public void onEndPage (PdfWriter writer, Document document) {
    Rectangle rect = writer.getBoxSize("art");
    Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
    switch(writer.getPageNumber() % 2) {
    case 0:
//        ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_RIGHT, new Phrase("even header"),
//                rect.getBorderWidthRight(), rect.getBorderWidthTop(), 0);
//        break;
    case 1:
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase("Page "+String.format("%d", writer.getPageNumber()),font3),
                300f, 30f, 0);
        ColumnText.showTextAligned(writer.getDirectContent(),
                Element.ALIGN_CENTER, new Phrase(
						"Note : This report is not to be considered for medico-legal purposes.",
						font3),
                300f, 50f, 0);
        break;
    }
}
}