package hms.patient.slippdf;

import hms.Printer.PrintFile;
import hms.main.MainLogin;
import hms1.ipd.database.IPDDBConnection;

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
import java.util.Random;
import java.util.Vector;

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

public class AdvancePaymentSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 7,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "";

	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();


	String mainDir = "";
	Font font;
	String[] open = new String[4];

	
	public static void main(String a[]) {
		try {
			new AdvancePaymentSlippdf("cs","d","55153","d","d","d");
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	public AdvancePaymentSlippdf(String doctorName, String totalAmountSTR,
			String index, String p_name,String paymentSlipIndex,String slipType) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		readFile();
		makeDirectory("" + paymentSlipIndex);
		
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
		
PdfContentByte cb1 = wr.getDirectContent();
		
		Water_Mark(cb1);
		cb1.saveState();
		cb1.stroke();
		cb1.restoreState();
		
		float[] tiltelTablCellWidth = { 0.1f, 1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = AdvancePaymentSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = AdvancePaymentSlippdf.class
				.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);

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
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
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

		PdfPCell cashReceipt = new PdfPCell(new Paragraph("CASH RECEIPT ",
				spaceFont));
		cashReceipt.setBorder(Rectangle.NO_BORDER);
		cashReceipt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cashReceipt.setPaddingBottom(3);
		table.addCell(cashReceipt);
		//
		float[] opdTablCellWidth = { 1.1f, 2f, 1.2f, 1.2f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);
		opdTable.addCell(new Phrase("Reciept No : ", font3));
		opdTable.addCell(new Phrase(""+paymentSlipIndex, font3));

		PdfPCell tokencell = new PdfPCell(new Phrase("IPD No. :" + "\n", font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(4);

		PdfPCell tokenNocell = new PdfPCell(new Phrase("" + index, font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(4);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase("" + p_name, font3));

		opdTable.addCell(new Phrase("Ref By : ", font3));
		opdTable.addCell(new Phrase(""+doctorName, font3));
	
		
		opdTable.addCell(new Phrase("Patient ID : ", font3));
		opdTable.addCell(new Phrase("" + Get_Pid(index), font3));
		
	
		opdTable.addCell(new Phrase(" ", font3));
		PdfPCell cell = new PdfPCell(new Phrase(" ",
				font3));
		cell.setPaddingBottom(5);
		cell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(cell);
		
		PdfPCell opdCell = new PdfPCell(opdTable);
		opdCell.setBorderWidth(0.8f);
		table.addCell(opdCell);

		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd hh:mm:ss a");
		PdfPCell space = new PdfPCell(new Paragraph("Date : "
				+ dateFormat.format(cal1.getTime()), font3));
		space.setBorder(Rectangle.NO_BORDER);
		space.setHorizontalAlignment(Element.ALIGN_RIGHT);
		space.setPaddingBottom(6);
		space.setPaddingTop(4);
		table.addCell(space);

		float[] examTableWidth = { 0.3f, 2.5f, 0.7f };
		PdfPTable examtable = new PdfPTable(examTableWidth);

		examtable.addCell(new Phrase("S. No", font3));
		examtable.addCell(new Phrase("Description", font3));
		examtable.addCell(new Phrase("Amount", font3));

		examtable.addCell(new Phrase("1", font3));
		examtable.addCell(new Phrase(""+slipType, font3));
		examtable.addCell(new Phrase("" + totalAmountSTR, font3));
		table.addCell(examtable);

		PdfPCell totalAmount = new PdfPCell(new Paragraph("Total Amount : `"
				+ totalAmountSTR + "", font));
		totalAmount.setBorder(Rectangle.NO_BORDER);
		totalAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
		totalAmount.setPaddingTop(5);
		table.addCell(totalAmount);
	
		float[] generated = { 0.3f,  0.7f };
		PdfPTable generatedtable = new PdfPTable(generated);

		PdfPCell generatedby = new PdfPCell(new Paragraph("Generated By:"+MainLogin.userName1, font3));
		generatedby.setBorder(Rectangle.NO_BORDER);
		generatedby.setHorizontalAlignment(Element.ALIGN_LEFT);
		generatedby.setPaddingTop(15);
//		table.addCell(generatedby);
	
		PdfPCell signature = new PdfPCell(new Paragraph("Signature", font3));
		signature.setBorder(Rectangle.NO_BORDER);
		signature.setHorizontalAlignment(Element.ALIGN_RIGHT);
		signature.setPaddingTop(15);
//		table.addCell(signature);
//		table.addCell(image2);
generatedtable.addCell(generatedby);
generatedtable.addCell(signature);

	
		table.addCell(generatedtable);
		
		
		
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
						"   - - - - - - - - - - - - - - - - - - - - - - - - - - - - -  - - - - - - - - - - - -   ",
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

		new OpenFile(RESULT);
//		new PrintFile(RESULT);
		
	}
	public void Water_Mark(PdfContentByte cb1) {
		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 40);
		f1.setColor(new BaseColor(204,204,204));
		String str="ROTARY";	
		Phrase phrase = new Phrase(str, f1);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 160,
				630, 30);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 460,
				630, 30);
		
		String randm_no=Random_String();
	        Font f3 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 11.5f);
	        f3.setColor(new BaseColor(204,204,204));
	       
	        Date date = new Date();  
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-M-yyyy hh:mm:ss");  
	        String strDate = formatter.format(date); 
	        Phrase phrase4 = new Phrase(strDate+" "+randm_no, f3);
	        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase4,170,620,30);
	        ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase4,470,620,30);
	    
	        
	
		
	}
	public String Random_String() {
		String str="";
		StringBuilder sb = new StringBuilder();
		// TODO Auto-generated constructor stub
		sb.append(Random_char_3());
		long millis = System.currentTimeMillis();
		sb.append(millis+"");
		sb.append(Random_char_3());
		
		return sb.toString();
	}
	public String Random_char_3() {
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<3;i++) {
			Random r = new Random();
			char c = (char)(r.nextInt(26) + 'A');
			sb.append(c);
		}
		return sb.toString();
	}
	public void makeDirectory(String slipNo) {

		new File("IPDAdvancePayment").mkdir();
		
		RESULT = "IPDAdvancePayment/" + slipNo + ".pdf";
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
	public String Get_Pid(String id) {
		IPDDBConnection IPDDBConnection=new IPDDBConnection();
		ResultSet rs=IPDDBConnection.retrieveData("select p_id from ipd_entery where ipd_id='"+id+"'");
		try {
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			IPDDBConnection.closeConnection();
		}
		
		return "";
	}

}