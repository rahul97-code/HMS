package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;

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
import java.util.Date;
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
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class TestTokenReportPDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";

	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;

	String mainDir = "",str="";
	Font font;
	int totalAmount = 0;
	float[] opdTablCellWidth = { 0.6f, 1.1f, 1.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new TestTokenReportPDF("2013-02-02", "2014-02-21","X-RAY");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public TestTokenReportPDF(String dateFrom, String dateTo,String exam_cat)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		readFile();
		str="Exam_Token"+DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);
		
		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.A4);
		document.setMargins(0, 0, 10, 0);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 9f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = TestTokenReportPDF.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = TestTokenReportPDF.class
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
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
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
		


		table.addCell(TitleTable);

	

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		OPDDBConnection db = new OPDDBConnection();

		opdTable.addCell(new Phrase("Reciept No", font3));
		opdTable.addCell(new Phrase("Patient ID", font3));
		opdTable.addCell(new Phrase("Patient Name", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Doctor Name", font3));
		amount.setHorizontalAlignment(Element.ALIGN_LEFT);
		opdTable.addCell(amount);

		table.addCell(opdTable);

			PdfPCell header = new PdfPCell(new Phrase(""+exam_cat
					, font3));
			header.setBorder(Rectangle.NO_BORDER);
			header.setPaddingBottom(3);
			header.setHorizontalAlignment(Element.ALIGN_CENTER);
			header.setBackgroundColor(BaseColor.LIGHT_GRAY);

			table.addCell(header);
			
				table.addCell(examToken(dateFrom, dateTo,exam_cat));
			


		document.add(table);
		document.close();

		new OpenFile(RESULT);
	}

	public PdfPTable examToken(String dateFrom, String dateTo,String cat) {

		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			
				ResultSet rs = db.retrieveAllExamsToken(dateFrom, dateTo,
						cat);
				while (rs.next()) {
					getExamData(""+rs.getObject(4));
					opdTable.addCell(new Phrase(""+rs.getObject(1), font3));
					opdTable.addCell(new Phrase(""+patient_id , font3));
					PdfPCell amount = new PdfPCell(new Phrase(""+rs.getObject(2), font3));
					amount.setHorizontalAlignment(Element.ALIGN_LEFT);
					opdTable.addCell(amount);
					opdTable.addCell(new Phrase(""+rs.getObject(3), font3));
				}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return opdTable;
	}



	public void getExamData(String examID) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataExamID(examID);
			while (rs.next()) {
				patient_id = rs.getObject(3).toString();
			}
			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void makeDirectory(String name) {
	
		new File("DailySlip").mkdir();
		
		RESULT = "DailySlip/" + name + ".pdf";
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
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
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


}