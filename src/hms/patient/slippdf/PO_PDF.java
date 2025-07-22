package hms.patient.slippdf;

import hms.reports.excels.PurchaseOrderExcel;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.PODBConnection;
import hms.store.database.SuppliersDBConnection;

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
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

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

public class PO_PDF {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 5);
	private static Font speratorLine = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD, BaseColor.RED);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.DARK_GRAY);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 19,
			Font.BOLD);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 14,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9f);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 7f,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";


	Vector<String> doctorname = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();


	String mainDir = "";
	Font font;
	String[] open = new String[4];

	int index = 1;
	String po_number;
	String PO_NUMBER,PO_DATE,REF_NO,SUPPLIER_ID,SUPPLIER_NAME,SUPPLIER_ADDRESS,ODERED_BY,TOTAL_AMOUNT,TAX_AMOUNT,FINAL_AMOUNT;
	/**
	 * @param exam_id
	 * @param examnames
	 * @param examRates
	 * @param totalAmountSTR
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void main(String[] argh) {
		try {
			new PO_PDF("1");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PO_PDF(String po_number) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		this.po_number=po_number;
		readFile();
		makeDirectory("" + index);
		PODetail();
		Document document = new Document();
	
		PdfWriter wr = PdfWriter.getInstance(document, new FileOutputStream(
				RESULT));
		document.setPageSize(PageSize.A4.rotate());
		document.setMargins(10, 10, 10, 10);
		document.open();

		BaseFont base = BaseFont.createFont("indian.ttf", BaseFont.WINANSI,
				BaseFont.EMBEDDED);
		font = new Font(base, 9f);
		float[] table1Cell = { 1.5f, 0.4f, 0.5f, 1.5f };

		PdfPTable table = new PdfPTable(table1Cell);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100);
		table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		java.net.URL imgURL = PO_PDF.class.getResource("/icons/rotaryLogo.png");
		Image image = Image.getInstance(imgURL);

		java.net.URL imgURL2 = PO_PDF.class.getResource("/icons/footer.PNG");
		Image image2 = Image.getInstance(imgURL2);

		image.scalePercent(30);
		image.setAbsolutePosition(40, 750);

		PdfPCell cell;
		cell = new PdfPCell(new Phrase(
				"ROTARY AMBALA CANCER AND GENERAL HOSPITAL", font1));
		cell.setRowspan(2);
		cell.setColspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Purchase Order"));
		table.addCell(cell);
		cell = new PdfPCell(new Phrase("Vendor Address", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(
						"Rotary Ambala Cancer And General Hospital\nOpp. Dussehra Ground, Ram Bagh Road, Ambala Cantt.\nTelephone No. : 0171-2690009\nMobile No. : 7496956703,7082840888"));
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("PO No.", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(""+PO_NUMBER, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(
				SUPPLIER_NAME+"\n"+SUPPLIER_ADDRESS));
		cell.setRowspan(3);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("PO Date", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(""+PO_DATE, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Ref. No.", font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase(""+REF_NO, font2));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cell);

		cell = new PdfPCell(new Phrase("Details of Receiver(Billed To)", font2));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		//table.addCell(cell);

		cell = new PdfPCell(new Phrase("Details of Consignee (Shipped To)",
				font2));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		//table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(
						"ROTARY AMBALA CANCER AND GENERAL HOSPITAL\nOpp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)\nTelephone No. : 0171-2690009, Mobile No. : 7496956703"));
		cell.setColspan(2);
		//table.addCell(cell);

		cell = new PdfPCell(
				new Phrase(
						"ROTARY AMBALA CANCER AND GENERAL HOSPITAL\nOpp. Dussehra Ground, Ram Bagh Road, Ambala Cantt (Haryana)\nTelephone No. : 0171-2690009, Mobile No. : 7496956703"));
		cell.setColspan(2);
		//ble.addCell(cell);

		float[] table2Cell = { 0.07f, 0.6f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f, 0.1f,
				0.1f, 0.1f, 0.1f, 0.1f };

		PdfPTable table2 = new PdfPTable(table2Cell);
		table2.getDefaultCell().setBorder(0);
		table2.setWidthPercentage(100);
		table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		cell = new PdfPCell(new Phrase("S.No.", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Description of Goods", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("HSN Code", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Qty", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);
		
//		cell = new PdfPCell(new Phrase("Free Qty", font3));
//		cell.setRowspan(2);
//		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Unit Price", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Total", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Discount", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Taxable Amt", font3));
		cell.setRowspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("CGST", font3));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("SGST", font3));
		cell.setColspan(2);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Rate", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Amt", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Rate", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		cell = new PdfPCell(new Phrase("Amt", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table2.addCell(cell);

		PODBConnection podbConnection = new PODBConnection();
		ResultSet resultSet = podbConnection.retrieveItems(po_number);
		int counter=0;
		try {
			while (resultSet.next()) {
				counter++;
				cell = new PdfPCell(new Phrase(counter+"", font3));
				
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(1).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);


				cell = new PdfPCell(new Phrase(resultSet.getObject(2).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_LEFT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(3).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
//				cell = new PdfPCell(new Phrase(resultSet.getObject(12).toString(), font3));
//				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
//				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(4).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(5).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(6).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(7).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(8).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(9).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(10).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
				
				cell = new PdfPCell(new Phrase(resultSet.getObject(11).toString(), font3));
				cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
				table2.addCell(cell);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		podbConnection.closeConnection();
		
		float[] table3Cell = { 0.5F, 2f, 0.5f, 0.5f };

		PdfPTable table3 = new PdfPTable(table3Cell);
		table3.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
		table3.setWidthPercentage(100);
		table3.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		cell = new PdfPCell(new Phrase("Terms & Conditions :", font3));
	
		table3.addCell(cell);

		cell = new PdfPCell(new Phrase("You will get your cheque by 20th of the next month", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table3.addCell(cell);
		
		
		cell = new PdfPCell(new Phrase("Warranty/ Guarantee:", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("N.A.", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("Sub Total", font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase(""+TOTAL_AMOUNT, font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		
		
		
		cell = new PdfPCell(new Phrase("Transport Terms:", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("Total Tax", font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase(""+TAX_AMOUNT, font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		
		
		cell = new PdfPCell(new Phrase("Payment Terms:", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("", font3));
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase("Total", font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		cell = new PdfPCell(new Phrase(""+FINAL_AMOUNT, font3));
		cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
		table3.addCell(cell);
		
		
		float[] table4Cell = { 1f, 1f, 1f, 1f };

		PdfPTable table4 = new PdfPTable(table4Cell);
		table4.getDefaultCell().setBorder(PdfPCell.LEFT | PdfPCell.RIGHT);
		table4.setWidthPercentage(100);
		table4.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		cell = new PdfPCell(new Phrase("Importan :\nPlease mention the following information on invoice:\n1) Our P.O. No. & Date\n2) Our GSTIN No. ", font3));
		table4.addCell(cell);
		cell = new PdfPCell(new Phrase("Order By", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table4.addCell(cell);
		cell = new PdfPCell(new Phrase("Checked By", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table4.addCell(cell);
		cell = new PdfPCell(new Phrase("Approved By", font3));
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		table4.addCell(cell);
		
		
		document.add(table);
		document.add(table2);
		
		PdfPTable footer = new PdfPTable(1);
		footer.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
		footer.setWidthPercentage(100);
		footer.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);

		footer.addCell(table3);
		footer.addCell(table4);
		writeFooterTable(wr,document,footer);
		document.close();

		new OpenFile(RESULT);

	}
	private void writeFooterTable(PdfWriter writer, Document document, PdfPTable table) {
        final int FIRST_ROW = 0;
        final int LAST_ROW = -1;
        //Table must have absolute width set.
        if(table.getTotalWidth()==0)
            table.setTotalWidth((document.right()-document.left())*table.getWidthPercentage()/100f);
        table.writeSelectedRows(FIRST_ROW, LAST_ROW, document.left(), document.bottom()+table.getTotalHeight(),writer.getDirectContent());
    }
	public void makeDirectory(String slipNo) {
		new File("PO_Slip").mkdir();
		RESULT = "PO_Slip/" + slipNo + ".pdf";
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
	public void PODetail()
	{
		PODBConnection podbConnection = new PODBConnection();
		ResultSet resultSet = podbConnection.retrieveWithID(po_number);
	
		try {
			while (resultSet.next()) {
				PO_NUMBER=(resultSet.getObject(2).toString());
				REF_NO=(resultSet.getObject(3).toString());
				SUPPLIER_ID=(resultSet.getObject(4).toString());
				
				
				SUPPLIER_NAME=(resultSet.getObject(5).toString());
				PO_DATE=(resultSet.getObject(6).toString());
				ODERED_BY=(resultSet.getObject(8).toString());
				TOTAL_AMOUNT=(resultSet.getObject(9).toString());
				TAX_AMOUNT=(resultSet.getObject(10).toString());
				FINAL_AMOUNT=(resultSet.getObject(12).toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getSupplierDetail(SUPPLIER_ID);
	}	
	public void getSupplierDetail(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithId(index);
		try {
			while (resultSet.next()) {

				
				SUPPLIER_NAME = resultSet.getObject(2).toString();
				SUPPLIER_ADDRESS = (resultSet.getObject(6).toString() + ", "
						+ resultSet.getObject(7).toString() + ", " + resultSet
						.getObject(8).toString())+"\n"+(resultSet.getObject(4).toString());;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}

}