
import hms.exam.database.ExamDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.test.free_test.FreeTestDBConnection;

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
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ExamSlippdf {

	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 50,
			Font.BOLD);

	public static String RESULT = "C:/Users/Sukhpal/Desktop/opdslip1.pdf";

	public static void main(String[] arg) {
		try {
			new ExamSlippdf();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ExamSlippdf() throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		Document document = new Document();

		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.A4);
		document.setMargins(0, 0, 0, 0);
		document.open();

		float[] tiltelTablCellWidth = { 1f };

		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);
		TitleTable.setWidthPercentage(100);

		TitleTable.addCell(new Phrase("Date : 2015-05-05", font1));

		PdfPCell logocell = new PdfPCell(new Phrase("9999", font2));

		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_LEFT);

		TitleTable.addCell(logocell);

		// document.add(TitleTable);

		PdfContentByte cb1 = wr.getDirectContent();
		BaseFont bf = BaseFont.createFont();
		Phrase phrase = new Phrase("999", font2);
		Phrase phrase2 = new Phrase("Date : 2015-05-05", new Font(bf, 10));

		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 54, 783,
				0);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase2, 50, 828,
				0);

		cb1.saveState();
		cb1.setColorStroke(BaseColor.BLACK);
		// cb1.rectangle(501, 657,80, 70);
		cb1.stroke();
		cb1.restoreState();

		document.close();
		Desktop desktop = Desktop.getDesktop();
		try {
			desktop.print(new File(RESULT));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}