package hms.reports.excels;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OpenFile;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.IssuedItemsDBConnection;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;

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
import com.mysql.jdbc.ResultSetMetaData;

public class DoctorIPDDataExcel {

	
	String doctorName, fromDateSTR, toDateSTR;
	String type="General";
	int rows=0;
	CellStyle style;
	CellStyle style2;
	ArrayList<String> packageName = new ArrayList();

	public static void main(String[] arg) {
		try {
			new DoctorIPDDataExcel("DR RAHUL TREHAN","2017-09-29", "2018-01-30");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public DoctorIPDDataExcel(String doctorName,String fromDate, String toDate)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		fromDateSTR = fromDate;
		toDateSTR = toDate;
		this.doctorName = doctorName;
		packageName.add("General");
		packageName.add("Minor");
		packageName.add("Minor (With Anesthesia)");
		packageName.add("Major");
		packageName.add("Major (With Anesthesia)");
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + doctorName + " , IPD DATA "
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			style = workbook.createCellStyle();
		    style.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
		    style.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    
		    style2 = workbook.createCellStyle();
		    style2.setFillForegroundColor(IndexedColors.LIGHT_ORANGE.getIndex());
		    style2.setFillPattern(CellStyle.SOLID_FOREGROUND);
		    for (int i = 0; i < packageName.size(); i++) {
				type=packageName.get(i);
				
				HSSFSheet sheet = workbook.createSheet(type);
				ipdData(sheet);
			}
		
		
//			sheet = workbook.createSheet("IPD DATA");
//			ipdData(sheet);
//			
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully on your Desktop with IssuedRegister -"
							+ DateFormatChange.StringToMysqlDate(new Date())
							+ " Name", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void ipdData(HSSFSheet sheet) {

		rows=0;
		try {
			DoctorDBConnection db = new DoctorDBConnection();
			ResultSet rs = db.retrieveDoctorIPDData(doctorName, fromDateSTR,toDateSTR,type);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) rows);
	
			for (int i = 0; i < columnsNumber; i++) {
				Cell cell =rowhead.createCell(i);
				cell.setCellValue(rsmd.getColumnName(i + 1));
				cell.setCellStyle(style);
			}
			rows++;
			while (rs.next()) {

				HSSFRow rowhead1 = sheet.createRow((short) rows);
				for (int i = 0; i < columnsNumber; i++) {
					
					Cell cell =rowhead1.createCell(i);
					cell.setCellValue(
							rs.getObject(i + 1).toString()+"");
					cell.setCellStyle(style2);
				}
				
				ipdExpense(sheet,rs.getObject(1).toString());
				rows++;

			}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print("" + ex);
		}
	}

	public void ipdExpense(HSSFSheet sheet,String ipdID) {

		rows++;
		try {
			DoctorDBConnection db = new DoctorDBConnection();
			ResultSet rs = db.retrieveDoctorIPDExpenseData(ipdID);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) rows);
			for (int i = 0; i < columnsNumber; i++) {
				Cell cell =rowhead.createCell(i);
				cell.setCellValue(rsmd.getColumnName(i + 1));
				cell.setCellStyle(style);
			}
			rows++;
			while (rs.next()) {

				HSSFRow rowhead1 = sheet.createRow((short) rows);
				for (int i = 0; i < columnsNumber; i++) {
					rowhead1.createCell(i).setCellValue(
							rs.getObject(i + 1).toString());
				}
				rows++;

			}
			
			rs = db.retrieveDoctorIPDExpenseMedData(ipdID);
			
			while (rs.next()) {

				if(!rs.getObject(1).toString().equals("0"))
				{
					HSSFRow rowhead1 = sheet.createRow((short) rows);
					rowhead1.createCell(0).setCellValue("MEDICINES");
					rowhead1.createCell(1).setCellValue(
							rs.getObject(2).toString());
				}
				
				rows++;

			}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print("" + ex);
		}
	}

}