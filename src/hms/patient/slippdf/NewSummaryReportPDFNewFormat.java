package hms.patient.slippdf;

import hms.JDialogs.gui.CashReportDialog;
import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.gui.ReceptionMain;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;

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
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class NewSummaryReportPDFNewFormat {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9.5f,
			Font.BOLD);
	private static Font whiteFont = new Font(Font.FontFamily.HELVETICA, 10,
			Font.BOLD,BaseColor.WHITE);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD, BaseColor.BLACK);
	private static Font font5 = new Font(Font.FontFamily.HELVETICA, 16,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip2.pdf"; 

	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Set<String> insuranceUnique = new LinkedHashSet<>();
	Set<String> serviceUnique = new LinkedHashSet<>();
	Vector<String> serviceV=new Vector<>();
	Vector<String> insV=new Vector<>();
	Vector<Integer> countV=new Vector<>();
	Vector<Integer> cashOrNot=new Vector<>();
	Vector<Double> amtV=new Vector<>();
	Vector<String> descV=new Vector<>();


	String opd_no, patient_id, patient_name, patient_age, doctor_name,
	amt_received, date, token_no;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmt = 0,totalCashAmt=0;
	double allTotalAmount = 0, allCancelledAmt = 0,allTotalCashAmt=0;


	public static void main(String[] arg) {
		try {

			new NewSummaryReportPDFNewFormat("2025-01-22", "2025-01-22");
		} catch (DocumentException | IOException e) { 
			e.printStackTrace();
		}
	}

	public NewSummaryReportPDFNewFormat(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		//		CashReportDialog CashReportDialog=new CashReportDialog(dateFrom,dateTo);
		//		if (!CashReportDialog.DONE) {
		//			return;
		//		}		
		getAllReportData(dateFrom, dateTo);
		readFile();
		str = "SummaryReport" + DateFormatChange.StringToMysqlDate(new Date());
		System.out.print(str);
		makeDirectory(str);

		Document document = new Document();
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(RESULT));
		HeaderAndFooter event = new HeaderAndFooter();
		wr.setPageEvent(event);
		document.setPageSize(PageSize.A4);
		document.setMargins(0, 0, 50, 80);
		document.open();
		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,BaseFont.EMBEDDED);
		font = new Font(base, 13f, Font.BOLD);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = MyCashReportNewFormat.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = MyCashReportNewFormat.class
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
		PdfPCell addressCell = new PdfPCell(new Phrase(
				"Opp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)",
				font2));
		addressCell.setPaddingBottom(2);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

		PdfPCell addressCell2 = new PdfPCell(
				new Phrase(
						"Telephone No. : 0171-2690009, Mobile No. : 09034056793",
						font2));
		addressCell2.setPaddingBottom(5);
		addressCell2.setBorder(Rectangle.NO_BORDER);
		addressCell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell2);

		table.addCell(TitleTable);

		table.addCell(new Phrase("Daily Cash Collection Report", font3));
		String timeStamp = new SimpleDateFormat(
				"EEEEEE, d MMMMMM, YYYY        hh:mm:ss a").format(Calendar
						.getInstance().getTime());
		System.out.println(timeStamp);
		table.addCell(new Phrase("Date : From     "
				+ DateFormatChange.StringToDateFormat(dateFrom)
				+ "     to     " + DateFormatChange.StringToDateFormat(dateTo),
				font3));
		table.addCell(new Phrase("As on : " + timeStamp, font3));

		PdfPTable records = new PdfPTable(1);
		records.getDefaultCell().setBorder(0);

		setHeaders(records);
		PdfPCell cell=null;
		for (String INS : insuranceUnique) {
			cell= new PdfPCell(new Phrase("" + INS,whiteFont));
			cell.setBorder(Rectangle.NO_BORDER);
			cell.setPaddingBottom(3);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			cell.setBackgroundColor(BaseColor.BLACK);
			records.addCell(cell);

			for(String SERV : serviceUnique) {
				PdfPTable t = new PdfPTable(new float[]{ 1.8f,3f});
				t.getDefaultCell().setBorder(0);
				cell= new PdfPCell(new Phrase(""+SERV+" -- >>" ,font3));
				cell.setBorder(Rectangle.NO_BORDER);
				cell.setPaddingBottom(3);
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
				t.addCell(cell);
				t.addCell(new Phrase("\n", font3));

				printData(records,INS,SERV,t);
			}
			setTotalCalculation(records);
		}
		cell = new PdfPCell(new Paragraph(new Chunk("Final Cash Amount : "
				+ (allTotalCashAmt), font5).setUnderline(0.9f, -2f)));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingTop(15);
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		records.addCell(cell);

		document.add(table);
		document.add(records);
		document.close();

		new OpenFile(RESULT);
	}

	private void setTotalCalculation(PdfPTable table) {
		// TODO Auto-generated method stub
		PdfPTable tempTable = new PdfPTable(new float[]{ 1.2f, 1.2f, 1.2f });
		
		PdfPCell footer2 = new PdfPCell(new Phrase("Cancel Amount : "	+ cancelledAmt, font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setHorizontalAlignment(Element.ALIGN_LEFT);
		tempTable.addCell(footer2);
		
		footer2 = new PdfPCell(new Phrase("Cash Amount : "	+ totalCashAmt, font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setHorizontalAlignment(Element.ALIGN_CENTER);
		tempTable.addCell(footer2);
		
		footer2 = new PdfPCell(new Phrase("Total Amount : "+ totalAmount, font4));
		footer2.setBorder(Rectangle.NO_BORDER);
		footer2.setHorizontalAlignment(Element.ALIGN_RIGHT);
		tempTable.addCell(footer2);	
		
		allCancelledAmt+=cancelledAmt;
		allTotalCashAmt+=cancelledAmt;
		allTotalAmount+=totalAmount;
		table.addCell(tempTable);
		cancelledAmt=totalCashAmt=totalAmount = 0;
	}
	private void setHeaders(PdfPTable table) {
		// TODO Auto-generated method stub
		PdfPTable opdTable = new PdfPTable(new float[]{ 2.0f, 0.7f, 1.1f });
		opdTable.addCell(new Phrase("Services", font3));
		opdTable.addCell(new Phrase("No.", font3));
		PdfPCell amount = new PdfPCell(new Phrase("Amount", font3));
		amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
		opdTable.addCell(amount);
		table.addCell(opdTable);
	}
	private void printData(PdfPTable table,String ins,String serv, PdfPTable cell) {
		// TODO Auto-generated method stub
		float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		boolean f=false;
		for(int i=0;i<descV.size();i++) {
			if(serviceV.get(i).equals(serv) && insV.get(i).equals(ins)) {
				opdTable.addCell(new Phrase(""+descV.get(i), font3));
				opdTable.addCell(new Phrase(""+countV.get(i), font3));
				String s="";
				if(cashOrNot.get(i)==-1)
				{cancelledAmt+=amtV.get(i);s="-";}
				else if(cashOrNot.get(i)==1)
				{totalCashAmt+=amtV.get(i);}
				totalAmount+=amtV.get(i);

				PdfPCell amount = new PdfPCell(new Phrase(s+amtV.get(i), font3));
				amount.setHorizontalAlignment(Element.ALIGN_RIGHT);
				opdTable.addCell(amount);
				f=true;
			}
		}
		if(f)
			table.addCell(cell);
		table.addCell(opdTable);
	}

	public void getAllReportData(String dateFrom, String dateTo) {
		MiscDBConnection miscDBConnection = new MiscDBConnection();
		ResultSet rs= miscDBConnection.getSummaryReportData(dateFrom, dateTo);
		try {
			while(rs.next()) {
				serviceV.add(rs.getString(1));
				serviceUnique.add(rs.getString(1));
				insV.add(rs.getString(2));
				insuranceUnique.add(rs.getString(2));
				descV.add(rs.getString(3));
				countV.add(rs.getInt(4));
				amtV.add(rs.getDouble(5));
				cashOrNot.add(rs.getInt(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			miscDBConnection.closeConnection();
		}
		System.out.println(insuranceUnique.size());
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