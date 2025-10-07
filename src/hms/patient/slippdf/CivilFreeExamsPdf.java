package hms.patient.slippdf;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.main.NumberToWordConverter;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;

import hms.store.database.BatchTrackingDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

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
import java.util.Date;
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

public class CivilFreeExamsPdf {
	static String OS;
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font fontHeader = new Font(Font.FontFamily.HELVETICA, 13,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD); 
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 8.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 12,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "Near_Expiry_Report.pdf";



	String mainDir = "",location="",billTo="",billPeriod="",desc="";
	Font font;
	double qty=7;
	double rate=300;
	//	float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f,1.1f,1.1f };
	String[] open = new String[4];

	public static void main(String[] argh) {
		try {
			new CivilFreeExamsPdf("2022-01-02","2023-01-02","",7,300.0);
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public CivilFreeExamsPdf(String fromDate,String DateTo,String billTo,double qty,double rate)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		this.billTo=billTo;
		this.rate=rate;
		this.qty=qty;
		desc=billTo;
		billPeriod=fromDate+" To "+DateTo;

		Document document = new Document();

		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 36, 36);
		document.open();
		
		PdfContentByte cb1 = wr.getDirectContent();

		Water_Mark(cb1);
		cb1.saveState();
		cb1.stroke();
		cb1.restoreState();

		Rectangle pageSize = document.getPageSize();
		float pageWidth = pageSize.getWidth();
		float pageHeight = pageSize.getHeight();
		Rectangle OutterRect = getRectangle(pageWidth-10, pageHeight-10,10,10,2);
		Rectangle InnerRect = getRectangle(pageWidth-25, pageHeight-25,25,25,1);
		// Step 4: Create a rectangle and add it to the document
		document.add(OutterRect);
		document.add(InnerRect);

		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 8f);

		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f, 0.1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = CivilFreeExamsPdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = CivilFreeExamsPdf.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		PdfPCell logocell2 = new PdfPCell(imageRotaryClub);
		logocell2.setRowspan(3);
		logocell2.setBorder(Rectangle.NO_BORDER);
		logocell2.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell2.setPaddingRight(7);
		TitleTable.addCell(logocell2);

		PdfPCell namecell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL" + "", font1));
		namecell.setHorizontalAlignment(Element.ALIGN_CENTER);
		namecell.setPaddingBottom(7);
		namecell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		TitleTable.addCell(namecell);

		PdfPCell logocell = new PdfPCell(image);
		logocell.setRowspan(3);
		logocell.setBorder(Rectangle.NO_BORDER);
		logocell.setHorizontalAlignment(Element.ALIGN_CENTER);
		logocell.setPaddingLeft(10);
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


		document.add(TitleTable);
		document.add(MainTable(fromDate,DateTo) );

		PdfPCell bottomCell = getLastTable(); 

		PdfPTable bottomTable = new PdfPTable(1);
		bottomTable.setTotalWidth(document.right() - document.left());
		bottomTable.setWidthPercentage(90);
		bottomTable.addCell(bottomCell);

		ColumnText ct = new ColumnText(wr.getDirectContent());
		ct.setSimpleColumn(
				document.left(),
				document.bottomMargin(),     // Y start
				document.right(),
				document.bottomMargin() + bottomTable.getTotalHeight() // Y end
				);
		ct.addElement(bottomTable);
		ct.go();

		document.close();

		OpenFile(RESULT);

	}
	
	public PdfPTable MainTable(String fromDate,String DateTo) {
		float[] TablCellWidth=new float[7];
		double finalAmount=round(qty*rate);
		TablCellWidth = new float[]  {1f,1f};
		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.setWidthPercentage(90);
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());

		Table.addCell(getBlankCell());
		Table.addCell(FirstTable());

		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getSecondTable("ROTARY AMBALA CANCER AND GENERAL HOSPITAL",this.billTo,"OBS FREE",this.billPeriod));
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getThirdTable(desc,qty,rate));

		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getBlankCell());
		Table.addCell(getForthTable(finalAmount+"","0.0","0.0",finalAmount+""));

		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());


		double cashTotal2=0;
		String neg1;
		cashTotal2=finalAmount;
		if(cashTotal2<0)
		{
			cashTotal2=Math.abs(cashTotal2);
			neg1="-";
		}else {
			neg1="";
		}

		PdfPCell cell = new PdfPCell(new Paragraph("Amount Payable: Rs."+cashTotal2+"("+neg1+" "+NumberToWordConverter.convert((int)cashTotal2)+" Only)".toUpperCase(), font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingBottom(10);
		Table.addCell(cell);


		cell = new PdfPCell(new Paragraph(new Chunk("Payments should be made in favor of:", font4).setUnderline(0.5f, -2f)));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		Table.addCell(cell);

		cell = new PdfPCell(new Paragraph("ROTARY AMBALA CANCER AND GENERAL HOSPITAL", font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingBottom(4);
		Table.addCell(cell);
		cell = new PdfPCell(new Paragraph("YES BANK Ltd. (GF 4307, SHANTI COMPLEX, JAGADHARI ROAD, AMBALA CANTT 133001)", font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingTop(-1);
		Table.addCell(cell);
		cell = new PdfPCell(new Paragraph("A/C NO. : 012088700000271", font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingTop(-1);
		Table.addCell(cell);
		
		cell = new PdfPCell(new Paragraph("RTGS/NEFT/IFSC : YESB0000120 ", font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		cell.setPaddingTop(-1);
		Table.addCell(cell);


//		cell = new PdfPCell(new Paragraph("PAN CARD NO.: ", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.NO_BORDER);
//		cell.setColspan(2);
//		Table.addCell(cell);
//
//		cell = new PdfPCell(new Paragraph("GST NO.: ", font3));
//		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
//		cell.setBorder(Rectangle.NO_BORDER);
//		cell.setColspan(2);
//		Table.addCell(cell);

		Table.addCell(getLineSpace());
		Table.addCell(getLineSpace());
		Table.addCell(getLastTable());

		return Table;
	}
	public void OpenFile(String filePath)
	{
		OS = System.getProperty("os.name").toLowerCase();

		if (isWindows()) {
			OPenFileWindows(filePath);

		}else if (isUnix()) {
			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open "+filePath });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						"exo-open "+filePath });
			}
			System.out.println("This is Unix or Linux");
		} 
	}
	public void OPenFileWindows(String path) {

		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}
	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}
	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);
	}

	public PdfPCell FirstTable() {
		PdfPTable table = new PdfPTable( new float[]  {1f,1f});
		table.getDefaultCell().setBorder(0);
		PdfPCell cell= new PdfPCell(new Phrase("INVOICE NO.", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("RACGH / "+generateInvoiceNumber(), font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("DATE", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(new SimpleDateFormat("yyyy/MM/dd").format(new Date()), font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(table);
		//		cell.setRowspan(3);
		cell.setBorder(Rectangle.NO_BORDER);
		//		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingLeft(10);

		return cell;
	}

	public PdfPCell getForthTable(String subTotal,String cgst,String igst,String total) {
		PdfPTable table = new PdfPTable( new float[]  {2f,1f});
		table.getDefaultCell().setBorder(0);
		PdfPCell cell= new PdfPCell(new Phrase("SUBTOTAL", font4));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(subTotal, font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("CGST @"+cgst+"% ON "+subTotal, font4));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(cgst, font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("IGST @"+igst+"% ON "+subTotal, font4));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(igst, font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);


		cell = new PdfPCell(new Phrase("TOTAL", font4));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(total, font4));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(table);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingLeft(10);

		return cell;
	}

	public PdfPCell getSecondTable(String centre,String billTo,String modality,String billPeriod) {
		PdfPTable table = new PdfPTable( new float[]  {1f,3.5f});
		table.getDefaultCell().setBorder(0);

		PdfPCell cell= new PdfPCell(new Phrase("BILLING ADDRESS :", fontHeader));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setColspan(2);
		cell.setPaddingBottom(5);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("CENTRE", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(centre, font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("BILL TO", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(billTo, font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("MODALITY", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(modality, font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("BILL PERIOD", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(billPeriod, font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(table);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingLeft(10);
		cell.setColspan(2);

		return cell;
	}
	public PdfPCell getLastTable() {
		PdfPTable table = new PdfPTable(1);
		table.getDefaultCell().setBorder(0);

		PdfPCell cell= new PdfPCell(new Phrase("Terms & Conditions:", fontHeader));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setPaddingBottom(5);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("1. This is a computer-generated receipt, NO NEED TO SIGNATURE ON IT. If any correction or rectification, contact immediately.", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("2. For any queries, please call: +91-8295699984, +91-9034056793 or email: coordinator@rotaryhospital.com", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		table.addCell(cell);


		cell = new PdfPCell(new Phrase("3. Payments should be made either through cheque or bank transfer.", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);

		table.addCell(cell);

		cell = new PdfPCell(new Phrase("4. Cheques subject to realization.", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingBottom(5);
		table.addCell(cell);

		cell = new PdfPCell(table);
		//		cell.setBorder(Rectangle.NO_BORDER);
		//		cell.setPaddingLeft(10);
		//		cell.setColspan(2);

		return cell;
	}
	
	public void Water_Mark(PdfContentByte cb1) {
		Font f1 = FontFactory.getFont(FontFactory.TIMES_ROMAN, 140);
		f1.setColor(new BaseColor(204,204,204));
		String str="RACGH";
		Phrase phrase = new Phrase(str, f1);
		ColumnText.showTextAligned(cb1, Element.ALIGN_CENTER, phrase, 325,
				400, 45);
	}
	public PdfPCell getThirdTable(String desc,double qty,double rate) {
		PdfPTable table = new PdfPTable( new float[]  {1f,1f,3.5f,1f,1f,1f,1f});
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(70);

		PdfPCell cell= new PdfPCell(new Phrase("STUDY INFORMATION :", fontHeader));
		cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setColspan(7);
		cell.setPaddingBottom(5);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("S NO.", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("PRODUCT", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("DESCRIPTION", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("QTY", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("RATE", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("TAX", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("AMOUNT", font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		table.addCell(cell);

		//		for(int i=0;i<data.size();i++) {

		table.addCell(getBoldData("1"));
		table.addCell(getBoldData("Service"));
		table.addCell(getBoldData("OBS "+desc));
		table.addCell(getBoldData(qty+""));
		table.addCell(getBoldData(rate+""));
		table.addCell(getBoldData("0%GST"));
		table.addCell(getBoldData(""+round(qty*rate)));


		//		}

		cell = new PdfPCell(table);
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setPaddingLeft(10);
		cell.setColspan(2);

		return cell;
	}
    public static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

	public PdfPCell getBoldData(String data){
		PdfPCell cell = new PdfPCell(new Phrase(data, font3));
		cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		return cell;
	}

	public PdfPCell getBlankCell() {
		PdfPCell cell = new PdfPCell(new Phrase("", font3));
		cell.setBorder(Rectangle.NO_BORDER);
		return cell;
	}

	public PdfPCell getLineSpace() {
		PdfPCell cell = new PdfPCell(new Phrase("\n", font3));
		cell.setBorder(Rectangle.NO_BORDER);
		cell.setColspan(2);
		return cell;
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
	public static Rectangle getRectangle(float pageWidth,float pageHeight,float x,float y,float thick) {
		Rectangle rect = new Rectangle(pageWidth, pageHeight);

		rect.setLeft(x); // X coordinate (left position)
		rect.setBottom(y);// Y coordinate (right position)

		rect.enableBorderSide(Rectangle.LEFT);
		rect.enableBorderSide(Rectangle.RIGHT);
		rect.enableBorderSide(Rectangle.TOP);
		rect.enableBorderSide(Rectangle.BOTTOM);
		rect.setBorder(Rectangle.BOX);
		rect.setBorderWidth(thick);
		rect.setBorderColor(BaseColor.BLACK);

		return rect;
	}

	public static String generateInvoiceNumber() {
		// Get current date
		Date date = new Date();

		// Format date as YYYYMMDD
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String formattedDate = sdf.format(date);

		// Create invoice number
		return "INV-" + formattedDate;
	}

	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		switch (writer.getPageNumber() % 2) {
		case 0:
			ColumnText
			.showTextAligned(
					writer.getDirectContent(),

					Element.ALIGN_RIGHT,
					new Phrase(
							"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
							font4), rect.getRight(), rect.getTop(), 0);
			break;
		case 1:
			ColumnText
			.showTextAligned(
					writer.getDirectContent(),
					Element.ALIGN_LEFT,
					new Phrase(
							"Saturday : General OPD Closed, Sunday : Working, EMERGENCY : 24x7",
							font4), rect.getLeft(), rect.getTop(), 0);
			break;
		}
		ColumnText.showTextAligned(writer.getDirectContent(),
				Element.ALIGN_CENTER, new Phrase(String.format("page %d", 1)),
				(rect.getLeft() + rect.getRight()) / 2, rect.getBottom() - 18,
				0);
	}
}