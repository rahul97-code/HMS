package hms.patient.slippdf;

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

public class NearExpiryPdf {
	static String OS;
	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
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



	String mainDir = "",location="";
	Font font;
//	float[] TablCellWidth = {  0.7f,2.0f,1.0f, 1.1f,1.1f,1.1f };
	String[] open = new String[4];

	public static void main(String[] argh) {
		try {
			new NearExpiryPdf("2022-01-02","2023-01-02","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NearExpiryPdf(String fromDate,String DateTo,String location)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		this.location=location;
	
		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		wr.setBoxSize("art", new Rectangle(36, 54, 559, 788));
		document.setPageSize(PageSize.LETTER);
		document.setMargins(0, 0, 10, 0);
		document.open();
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

		java.net.URL imgURL = NearExpiryPdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		image.scalePercent(50);
		image.setAbsolutePosition(100, 260);

		java.net.URL imgURLRotaryClub = NearExpiryPdf.class
				.getResource("/icons/Rotary-Club-logo.jpg");
		Image imageRotaryClub = Image.getInstance(imgURLRotaryClub);

		// imageRotaryClub.scalePercent(60);
		// imageRotaryClub.setAbsolutePosition(40, 750);

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
				"Item Expiry Report From "+fromDate+" To "+DateTo+"",font4));
		addressCell.setPaddingTop(7);
		addressCell.setPaddingBottom(7);
		addressCell.setBorder(Rectangle.NO_BORDER);
		addressCell.setHorizontalAlignment(Element.ALIGN_CENTER);
		TitleTable.addCell(addressCell);

	
		document.add(TitleTable);
		document.add(NearExpiry(fromDate,DateTo) );
		
		
		// onEndPage(wr,document);
		document.close();

		OpenFile(RESULT);
	
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
	public PdfPTable NearExpiry(String fromDate,String DateTo) {
		
		
		float[] TablCellWidth=new float[7];
		
			 TablCellWidth = new float[]  {0.3f,0.5f,1.2f,1.5f,1.0f,0.9f,0.7f,0.9f};
//			 TablCellWidth[]= {0.24f,1.5f,0.4f,0.4f,0.4f,0.3f,0.3f,0.35f,0.35f,0.35f,0.35f};

		PdfPTable Table = new PdfPTable(TablCellWidth);
		Table.setWidthPercentage(90);
		PdfPCell header = new PdfPCell(new Phrase("S.No.", font3));
		header.setHorizontalAlignment(Element.ALIGN_CENTER);
		header.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header);
		PdfPCell header1 = new PdfPCell(new Phrase("Item Id", font3));
		header1.setHorizontalAlignment(Element.ALIGN_CENTER);
		header1.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header1);
		PdfPCell header2 = new PdfPCell(new Phrase("Item name", font3));
		header2.setHorizontalAlignment(Element.ALIGN_CENTER);
		header2.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header2);
		PdfPCell header3 = new PdfPCell(new Phrase("Item Desc.", font3));
		header3.setHorizontalAlignment(Element.ALIGN_CENTER);
		header3.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header3);
		PdfPCell header4 = new PdfPCell(new Phrase("Item Batch", font3));
		header4.setHorizontalAlignment(Element.ALIGN_CENTER);
		header4.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header4);
		PdfPCell header5 = new PdfPCell(new Phrase("Expiry", font3));
		header5.setHorizontalAlignment(Element.ALIGN_CENTER);
		header5.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header5);
		PdfPCell header6 = new PdfPCell(new Phrase("Item Stock", font3));
		header6.setHorizontalAlignment(Element.ALIGN_CENTER);
		header6.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header6);
		PdfPCell header7 = new PdfPCell(new Phrase("Vendor Name", font3));
		header7.setHorizontalAlignment(Element.ALIGN_CENTER);
		header7.setBackgroundColor(BaseColor.LIGHT_GRAY);
		Table.addCell(header7);
		try {
			BatchTrackingDBConnection db1 = new BatchTrackingDBConnection();
			ResultSet rs = db1.retrieveAllExpiryData(fromDate,DateTo,location);

			int r = 1;
			while (rs.next()) {
				Table.addCell(new Phrase(	"" + r, font3));
				Table.addCell(new Phrase(rs.getObject(1)+"", font3));
				Table.addCell(new Phrase(rs.getObject(2)+"" , font3));
				Table.addCell(new Phrase(rs.getObject(3)+"" , font3));
				Table.addCell(new Phrase(rs.getObject(4)+"", font3));
				Table.addCell(new Phrase(rs.getObject(5)+"" , font3));
				Table.addCell(new Phrase(rs.getObject(6)+"" , font3));
				Table.addCell(new Phrase(rs.getObject(7)+"" , font3));
				r++;
			}
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return Table;
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