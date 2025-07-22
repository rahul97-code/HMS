package LIS_System_Pdf_Slips;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;


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
import java.util.Vector;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class CancelExamSlipPdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font spaceFont1 = new Font(Font.FontFamily.HELVETICA, 13,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	String exam_id, patient_id, patient_name, patient_age, patient_sex,
			doctor_name, date, insuranceType;
	String mainDir = "";
	Font font;
	String[] open = new String[4];

	/**
	 * @param exam_id
	 * @param cancellisName
	 * @param cancellisCharges
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	public CancelExamSlipPdf(String exam_id, Vector<String> cancellisName, Vector<String> cancellisCharges,
			String totalAmountSTR, int index, boolean original)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		getOPDData(exam_id);
		getPatientDetail(patient_id);

		readFile();
		makeDirectory(exam_id);
		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 50, 0);
		document.open();

		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 9f);
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(40);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = CancelExamSlipPdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = CancelExamSlipPdf.class
				.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);

		PdfContentByte cb1 = wr.getDirectContent();
		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 59);
		f1.setColor(BaseColor.GRAY);
		Phrase phrase3 = new Phrase("DUPLICATE", f1);
		if (!original)
			ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase3, 300,
					400, 30);
		cb1.saveState();
		cb1.stroke();
		cb1.restoreState();

		image.scalePercent(30);
		image.setAbsolutePosition(40, 750);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingRight(5);
		TitleTable.addCell(logocell);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "\n", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(3);
		// namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(1);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(2);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		PdfPCell cashReceipt = new PdfPCell(new Paragraph("LIS EXAM CANCEL RECEIPT ",
				spaceFont));
		cashReceipt.setBorder(Rectangle.NO_BORDER);
		cashReceipt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cashReceipt.setPaddingBottom(3);
		table.addCell(cashReceipt);

		float[] opdTablCellWidth = { 1f, 2f, 0.8f, 1.2f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Receipt No. :" + "\n",
				font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(4);

		PdfPCell tokenNocell = new PdfPCell(new Phrase("" + index, font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(4);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient ID : ", font3));
		opdTable.addCell(new Phrase("" + patient_id, font3));

		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + patient_name, font3));

		opdTable.addCell(new Phrase("Sex : ", font3));
		opdTable.addCell(new Phrase("" + patient_sex, font3));

		opdTable.addCell(new Phrase("Ref By :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase("" + doctor_name,
				font3));
		consultantcell.setPaddingBottom(5);
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
		opdTable.addCell(new Phrase("Age : ", font3));
		opdTable.addCell(new Phrase("" + patient_age, font3));
		opdTable.addCell(new Phrase("Insurance : ", font3));
		opdTable.addCell(new Phrase("" +insuranceType , font3));

		opdTable.addCell(new Phrase(" ", font3));
		opdTable.addCell(new Phrase("" , font3));
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);
		String dateSTR="";
		String dateDisplay=date;
		try {
			if(dateDifference()>0)
			{
				dateSTR="\n("+date+")";
				dateDisplay=DateFormatChange
						.StringToMysqlDate(new Date()) + "";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
//		PdfPCell space = new PdfPCell(new Paragraph("Date : " + dateDisplay + " / "
//				+ dateFormat.format(cal1.getTime()), font3));
//		space.setBorder(Rectangle.NO_BORDER);
//		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
//		space.setPaddingBottom(6);
//		space.setPaddingTop(4);
//		table.addCell(space);
		float[] generated = { 0.3f,  0.7f };
		PdfPTable generatedtable = new PdfPTable(generated);

		PdfPCell generatedby = new PdfPCell(new Paragraph("Generated By : "+MainLogin.userName1, font3));
		generatedby.setBorder(Rectangle.NO_BORDER); 
		generatedby.setHorizontalAlignment(Element.ALIGN_LEFT);
		generatedby.setPaddingTop(15);
//		table.addCell(generatedby);
	
		PdfPCell space = new PdfPCell(new Paragraph("Date / Time : "+dateDisplay+" / "+ dateFormat.format(cal1.getTime()), font3));
		space.setBorder(Rectangle.NO_BORDER);
		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
		space.setPaddingTop(15);

           generatedtable.addCell(generatedby);
           generatedtable.addCell(space);

	
		table.addCell(generatedtable);
		if (insuranceType.equals("Unknown")) {
			float[] examTableWidth = { 0.3f, 2.5f, 0.7f };
			PdfPTable examtable = new PdfPTable(examTableWidth);

			examtable.addCell(new Phrase("S. No", font3));
			examtable.addCell(new Phrase("Exam Name", font3));
			examtable.addCell(new Phrase("Amount", font3));
			for (int i = 0; i < cancellisName.size(); i++) {
				examtable.addCell(new Phrase("" + (i + 1), font3));
				examtable.addCell(new Phrase("" + cancellisName.get(i), font3));
				examtable.addCell(new Phrase("" + cancellisCharges.get(i), font3));
			}
			table.addCell(examtable);
		} else {
			float[] examTableWidth = { 1f, 3f };
			PdfPTable examtable = new PdfPTable(examTableWidth);

			examtable.addCell(new Phrase("S. No", font3));
			examtable.addCell(new Phrase("Exam Name", font3));
			for (int i = 0; i < cancellisName.size(); i++) {
				examtable.addCell(new Phrase("" + (i + 1), font3));
				examtable.addCell(new Phrase("" + cancellisName.get(i), font3));
			}
			table.addCell(examtable);
		}

		PdfPCell totalAmount = new PdfPCell(new Paragraph("Total Amount : `"
				+ totalAmountSTR + "", font));
		totalAmount.setBorder(Rectangle.NO_BORDER);
		totalAmount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		totalAmount.setPaddingTop(5);
		if (insuranceType.equals("Unknown")) {
			table.addCell(totalAmount);
		}
		float[] tabelTokenW = { 1f, 0.5f };
		PdfPTable tabelToken = new PdfPTable(tabelTokenW);
		tabelToken.getDefaultCell().setBorder(0);
		
		
		
		ExamDBConnection connection=new ExamDBConnection();
		PdfPCell signature = new PdfPCell(new Paragraph("Token Number : "+connection.examCounter(index+"",date)+dateSTR, spaceFont1));
		connection.closeConnection();
		signature.setBorder(Rectangle.NO_BORDER);
		signature.setHorizontalAlignment(Element.ALIGN_LEFT);
		signature.setPaddingTop(15);
		tabelToken.addCell(signature);
		signature = new PdfPCell(new Paragraph("Signature", font3));
		signature.setBorder(Rectangle.NO_BORDER);
		signature.setHorizontalAlignment(Element.ALIGN_RIGHT);
		signature.setPaddingTop(15);
		tabelToken.addCell(signature);
		table.addCell(tabelToken);
		table.addCell(image2);

		float[] table2w = { 1f, 0.1f, 1f };
		PdfPTable table2 = new PdfPTable(table2w);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(94);

		PdfPCell call1 = new PdfPCell(table);
		call1.setBorder(Rectangle.NO_BORDER);
		call1.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call1);

		PdfPCell call3 = new PdfPCell(
				new Paragraph(
						"   - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -   ",
						speratorLine));
		call3.setBorder(Rectangle.NO_BORDER);
		call3.setRotation(270);
		call3.setPaddingRight(7);
		call3.setPaddingLeft(5);
		call3.setHorizontalAlignment(Element.ALIGN_CENTER);

		table2.addCell(call3);

		PdfPCell cell2 = new PdfPCell(table);
		cell2.setBorder(Rectangle.NO_BORDER);
		cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell2);

		document.add(table2);
		document.close();

		new Open_File1(RESULT);
	}
	public int dateDifference() throws ParseException
	{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
	    Date firstDate = sdf.parse( DateFormatChange
				.StringToMysqlDate(new Date()));
	    Date secondDate = sdf.parse(date);
	 
	    long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
	    int diff = (int) TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		System.out.println(secondDate+"  "+firstDate);
	    System.out.println("DATE DIFFRENCE "+diff);
	    return diff;
	}
	
	public void getOPDData(String opdID) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataExamID(opdID);
			while (rs.next()) {
				patient_id = rs.getObject(3).toString();
				patient_name = rs.getObject(4).toString();
				doctor_name = rs.getObject(5).toString();
				date = rs.getObject(6).toString();
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	
	}


	public void getPatientDetail(String indexId) {

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {

				patient_age = resultSet.getObject(2).toString();
				patient_sex = resultSet.getObject(3).toString();
				insuranceType = resultSet.getObject(7).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print(patient_age);
		patientDBConnection.closeConnection();
		String data[] = new String[22];
		int i = 0;
		for (String retval : patient_age.split("-")) {
			data[i] = retval;
			i++;
		}
		if (!data[0].equals("0")) {
			patient_age = data[0] + " years";
		} else if (!data[1].equals("0")) {
			patient_age = data[1] + " months";
		} else if (!data[2].equals("0")) {
			patient_age = data[2] + " days";
		}
	}

	public void makeDirectory(String exam_id) {

	
		new File("ExamSlip").mkdir();
		RESULT = "ExamSlip/" + exam_id + ".pdf";
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

}