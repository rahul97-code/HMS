package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms1.ipd.database.IPDDBConnection;

import java.awt.Color;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.pdfbox.pdmodel.PDDocument;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
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

public class PatientLabelPrint {




	public static String RESULT = "opdslip1.pdf";
	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String exam_id, patient_id, patient_name,receipt_id, patient_age, patient_sex,
	doctor_name, second_doctor,date, insuranceType;
	String mainDir = "";
	Font font;
	String[] open = new String[4];

	public static void main(String r[]) {
		Object[] o= {1,2,3};
		try {
			new PatientLabelPrint("0000250368304","77341","2025-03-18","MANVEER SINGH GTDSED",200,40,40);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @param karun 
	 * @param desc2 
	 * @param examDoctorV 
	 * @throws DocumentException
	 * @throws IOException
	 */
	public PatientLabelPrint(String pid,String ipdID,String DOA,String pName,float X,float Y,float fontSize)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		Font font1 = new Font(Font.FontFamily.HELVETICA, fontSize,
				Font.BOLD, BaseColor.BLACK);
		readFile();
		getPatientDetail(pid);
		//makeDirectory(exam_id);


		Document document = new Document();

		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		float customHeight = 215f; 
		Rectangle customSize = new Rectangle(PageSize.A1.getWidth(), customHeight);
		document.setPageSize(customSize);
		//		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 0, 4);
		document.open();

//		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
//				BaseFont.EMBEDDED);
//		font = new Font(base, 9f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100);
		//		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);

		PdfPTable TitleTable = new PdfPTable(1);
		TitleTable.getDefaultCell().setBorder(0);

		PdfPCell cell = new PdfPCell(new Phrase(""+pName,font1));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(-1);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		TitleTable.addCell(cell);

		cell = new PdfPCell(new Phrase("Age/Sex: "+patient_age+"/"+patient_sex.charAt(0),font1));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(-1);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		TitleTable.addCell(cell);

		cell = new PdfPCell(new Phrase("UHID: "+pid,font1));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(-1);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		TitleTable.addCell(cell);

		cell = new PdfPCell(new Phrase("DOA: "+DOA,font1));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(-1);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		TitleTable.addCell(cell);


		float[] w = {1f,1f};
		PdfPTable t = new PdfPTable(w);
		t.getDefaultCell().setBorder(0);

		cell = new PdfPCell(TitleTable);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingRight(X);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.addCell(cell);

		cell = new PdfPCell(TitleTable);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingLeft(X);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		t.addCell(cell);


		PdfPCell call1 = new PdfPCell(t);
		call1.setBorder(Rectangle.NO_BORDER);
		call1.setPaddingBottom(Y);
		call1.setHorizontalAlignment(Element.ALIGN_LEFT);

		table.addCell(call1);

		writeFooterTable(wr,document,table);

		document.close();

		new OpenFile(RESULT);

	}

	public void makeDirectory(String exam_id) {


		new File("PatientLabels").mkdir();
		RESULT = "PatientLabels/" + exam_id + ".pdf";
	}

	private void writeFooterTable(PdfWriter writer, Document document, PdfPTable table) {
		final int FIRST_ROW = 0;
		final int LAST_ROW = -1;
		//Table must have absolute width set.
		if(table.getTotalWidth()==0)
			table.setTotalWidth((document.right()-document.left())*table.getWidthPercentage()/100f);
		table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom()+table.getTotalHeight(),writer.getDirectContent());
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
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];

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
	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection.retrieveDataWithIndex(indexId);
			while (resultSet.next()) {
				patient_age = resultSet.getObject(2).toString();
				patient_sex= resultSet.getObject(3).toString();			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		String data[] = new String[10];
		int i = 0;
		for (String retval : patient_age.split("-")) {
			data[i] = retval;
			i++;
		}
		if (!data[0].equals("0")) {
			patient_age = data[0] + "Y";
		} else if (!data[1].equals("0")) {
			patient_age = data[1] + "M";
		} else if (!data[2].equals("0")) {
			patient_age = data[2] + "D";
		}
	}


}