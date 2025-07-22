package hms.reports.excels;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OpenFile;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.ItemsDBConnection;
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

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

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

public class ItemRateListReportExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> ipdID = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	static String OS;
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new ItemRateListReportExcel();
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public ItemRateListReportExcel() throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		reportCategory.add("OPD Services");
		reportCategory.add("Exam Services");
		reportCategory.add("Misc Services");
		reportCategory.add("Indoor Services");

		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + "Rate List" + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Item Rate List");
			itemRate( sheet);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully on your Desktop with Rate List -"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void itemRate( HSSFSheet sheet) {

		
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Item Id");
		rowhead.createCell(1).setCellValue("Item Name");
		rowhead.createCell(2).setCellValue("Desc");
		rowhead.createCell(3).setCellValue("Brand");
		rowhead.createCell(4).setCellValue("Salt Name");
		rowhead.createCell(5).setCellValue("MRP");
		rowhead.createCell(6).setCellValue("Purchase Price");
		rowhead.createCell(7).setCellValue("Previouse Price");
		rowhead.createCell(8).setCellValue("Selling Price");
		rowhead.createCell(9).setCellValue("Total Stock");
		rowhead.createCell(10).setCellValue("Minimum Item");
		rowhead.createCell(11).setCellValue("Status");
		rowhead.createCell(12).setCellValue("Expiry Date");
		rowhead.createCell(13).setCellValue("Entry User");
		int k = 1;
		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllItemsExcel();
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
					rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
					rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
					rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
					rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
					rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
					rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
					rowhead1.createCell(7).setCellValue(rs.getObject(8).toString());
					rowhead1.createCell(8).setCellValue(rs.getObject(9).toString());
					rowhead1.createCell(9).setCellValue(rs.getObject(10).toString());
					rowhead1.createCell(10).setCellValue(rs.getObject(11).toString());
					rowhead1.createCell(11).setCellValue(rs.getObject(12).toString());
					rowhead1.createCell(12).setCellValue(rs.getObject(13).toString());
					rowhead1.createCell(13).setCellValue(rs.getObject(14).toString());
					k++;
				}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		totalAmount=0;
	}


}