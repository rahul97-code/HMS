import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * Hello World: document constructor.
 */
public class pdfSize {

	/** Path to the resulting PDF file. */
	public static final String RESULT = "C:/Users/Sukhpal/Desktop/hello_narrow.pdf";
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 0.5f,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);

	/**
	 * Creates a PDF file: hello_narrow.pdf
	 * 
	 * @param args
	 *            no arguments needed
	 */
	public static void main(String[] args) throws DocumentException,
			IOException {
		// step 1
		// Using a custom page size
		Rectangle pagesize = new Rectangle(20f, 20f);
		Document document = new Document(pagesize, 0, 0, 0.3f, 0);
		// step 2
		PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		// step 3
		document.open();
		// step 4
		float[] tiltelTablCellWidth = { 2f };

		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		//TitleTable.getDefaultCell().setBorder(0);
		TitleTable.setWidthPercentage(110);
		
		
		PdfPCell tokenNocell = new PdfPCell(new Phrase("999", font2));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		//tokenNocell.setPadding(0);
		document.add(new Phrase("Date : 2015-05-05"+"\n"+"9999", font1));
		document.add(new Phrase("999", font2));

		TitleTable.addCell(tokenNocell);
		//TitleTable.addCell(new Phrase("Date : 2015-05-05", font1));

	//	document.add(TitleTable);
		// step 5
		document.close();
	}
}
