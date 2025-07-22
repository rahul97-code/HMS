package hms.opd.gui;

import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OpenFile;
import hms.reception.database.TokenDBConnection;

import java.awt.print.PrinterException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

public class TokenSlipPrint {
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no, time;
	private static Font font1 = new Font(Font.FontFamily.COURIER, 10,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.COURIER, 20,
			Font.BOLD);
	static String userHomeFolder = System.getProperty("user.home");
	 static String OS;
	public static String RESULT = "token.pdf";

	public TokenSlipPrint(String opdid, String doctorRoomSTR)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		   OS = System.getProperty("os.name").toLowerCase();
		getOPDData(opdid);
		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.A4);
		document.setMargins(0, 0, 0, 0);
		document.open();

		PdfContentByte cb1 = wr.getDirectContent();
		BaseFont bf = BaseFont.createFont();

		Phrase doctorName = new Phrase(doctor_name, font1);
		Phrase doctorRoom = new Phrase("Room No. " + doctorRoomSTR, font1);
		Phrase date = new Phrase(getDate(), font1);

		Phrase phrase = new Phrase(token_no, font2);

	
		if (isWindows()) {
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, doctorName, 54,
					823, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, doctorRoom, 54,
					810, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, date, 54, 797, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 54, 775,
					0);
		}  else if (isUnix()) {
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, doctorName, 54,
					800, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, doctorRoom, 54,
					787, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, date, 54, 774, 0);
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 54, 752,
					0);
		}
	
		cb1.saveState();
		cb1.setColorStroke(BaseColor.BLACK);
		cb1.stroke();
		cb1.restoreState();
		document.close();

//		new OpenFile(RESULT);
//		try {
////			new PrintDocument(RESULT);
//		} catch (PrinterException e) {
//			
//		}

	}

	public String StringToDateFormat() {
		SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = null;
		try {
			date1 = dt.parse(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// *** same for the format String below
		SimpleDateFormat dt1 = new SimpleDateFormat("MMM d");
		return dt1.format(date1);

	}

	public String getDate() {
		String pattern = ", HH:mm";
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		// formatting
		String str = StringToDateFormat() + format.format(new Date());
		return str;

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
			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println(patient_id);
	}

	public static void main(String arg[]) {
		try {
			new TokenSlipPrint("1", "ss");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);
		

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}
}