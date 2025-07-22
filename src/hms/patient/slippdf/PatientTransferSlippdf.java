package hms.patient.slippdf;

import hms.main.DateFormatChange;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
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
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PatientTransferSlippdf {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 10,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.WHITE);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";


	 Vector<String> wardtransfer = new Vector<String>();
	String ipd_number,p_id,p_name,doctor_name;
	String fromDept="",toDept="";

	String mainDir = "";
	Font font;
	String[] open=new String[4];

	public static void main(String[] arg)
	{
		
		try {
			new PatientTransferSlippdf("1", "1","");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public PatientTransferSlippdf(String ipd_number,String slip_number,String date) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
	
		readFile();
		makeDirectory(""+slip_number);
		getIPDData(ipd_number);
		getWardTransfer(ipd_number, slip_number);
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
		table.setWidthPercentage(90);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		float[] tiltelTablCellWidth = { 0.1f, 1f };
		PdfPTable TitleTable = new PdfPTable(tiltelTablCellWidth);
		TitleTable.getDefaultCell().setBorder(0);

		java.net.URL imgURL = PatientTransferSlippdf.class
				.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = PatientTransferSlippdf.class
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

		PdfPCell cashReceipt = new PdfPCell(new Paragraph("Patient-Ward Transfer Slip",
				spaceFont));
		cashReceipt.setBorder(Rectangle.NO_BORDER);
		cashReceipt.setHorizontalAlignment(Element.ALIGN_CENTER);
		cashReceipt.setPaddingBottom(3);
		table.addCell(cashReceipt);
		//
		float[] opdTablCellWidth = { 1f, 2f, 1.2f, 1.2f };
		PdfPTable opdTable = new PdfPTable(opdTablCellWidth);
		opdTable.getDefaultCell().setBorder(0);

		PdfPCell tokencell = new PdfPCell(new Phrase("Slip No. :" + "\n",
				font3));
		tokencell.setBorder(Rectangle.NO_BORDER);
		tokencell.setPaddingBottom(4);

		PdfPCell tokenNocell = new PdfPCell(new Phrase( ""+slip_number, font3));
		tokenNocell.setBorder(Rectangle.NO_BORDER);
		tokenNocell.setPaddingBottom(4);

		opdTable.addCell(tokencell);
		opdTable.addCell(tokenNocell);

		opdTable.addCell(new Phrase("Patient ID : ", font3));
		opdTable.addCell(new Phrase(""+p_id, font3));

		opdTable.addCell(new Phrase("Date : ", font3));
		opdTable.addCell(new Phrase(""+date, font3));
		
		
		opdTable.addCell(new Phrase("Patient Name : ", font3));
		opdTable.addCell(new Phrase(""+p_name, font3));
		
		
		opdTable.addCell(new Phrase("Doctor Incharge :", font3));
		PdfPCell consultantcell = new PdfPCell(new Phrase(""+doctor_name
				, font3));
		consultantcell.setPaddingBottom(5);
		consultantcell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(consultantcell);
		
		

		opdTable.addCell(new Phrase("IPD Number", font3));
		

		opdTable.addCell(new Phrase(""+this.ipd_number, font3));
		
		opdTable.addCell(new Phrase("Transfer :", font3));
	
		if(wardtransfer.size()>1)
		{
			fromDept=wardtransfer.get(1);
		}
		if(wardtransfer.size()>0)
		{
			toDept=wardtransfer.get(0);
		}
		
		PdfPCell transferCell = new PdfPCell(new Phrase(""+fromDept+"    TO      "+toDept
				, font3));
		transferCell.setColspan(3);
		transferCell.setBorder(Rectangle.NO_BORDER);
		opdTable.addCell(transferCell);

		
		opdTable.addCell(new Phrase(" ", font3));
		opdTable.addCell(new Phrase(" ", font3));
		
		
		opdTable.addCell(new Phrase(" ", font3));
		opdTable.addCell(new Phrase(" ", font3));
		
		opdTable.addCell(new Phrase("Doctor Sign ", font3));
		opdTable.addCell(new Phrase("", font3));
		
		
		opdTable.addCell(new Phrase("Auth. Sign ", font3));
		opdTable.addCell(new Phrase("", font3));
		
		table.addCell(opdTable);

		table.addCell(image2);

		document.add(table);
		document.close();

		new OpenFile(RESULT);
		
	}

		public void getIPDData(String ipdID) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataIPDID(ipdID);
			while (rs.next()) {
				ipd_number = rs.getObject(1).toString();
				p_id = rs.getObject(2).toString();
				p_name = rs.getObject(3).toString();

			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		getIPDDoctors(ipdID);

	}
	
	public void getWardTransfer(String ipdID,String index) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveWardTransfer(ipdID, index);
			while (rs.next()) {
				wardtransfer.add(rs.getObject(1).toString());

			}
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		getIPDDoctors(ipdID);
	}
	public void getIPDDoctors(String ipd_id) {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataDoctor(ipd_id);
		try {
			while (resultSet.next()) {
				doctor_name=("" + resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void makeDirectory(String slipNo) {

		new File("TransferSlips").mkdir();
		RESULT = "TransferSlips/" + slipNo + ".pdf";
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
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
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