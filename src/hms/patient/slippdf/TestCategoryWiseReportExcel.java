package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
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

public class TestCategoryWiseReportExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	static String OS;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	int k = 1;
	
	public TestCategoryWiseReportExcel(String fileName,String fromDate, String toDate)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
	
		

		try {
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Exams");
			
			examAmount(fromDate, toDate,sheet);
				

			FileOutputStream fileOut = new FileOutputStream(fileName);
			workbook.write(fileOut);
			fileOut.close();
			
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(fileName);
		} catch (Exception ex) {
			System.out.println(ex);

		}
		
		totalAmount = totalAmount - cancelledAmount;
		
	}

	public void examAmount(String dateFrom, String dateTo,HSSFSheet sheet) {
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("Exam Cat");
		rowhead.createCell(2).setCellValue("Exam Sub Type");
		rowhead.createCell(3).setCellValue("No.");
		rowhead.createCell(4).setCellValue("Amount");
		rowhead.createCell(5).setCellValue("Date");
		
		try {
			ExamDBConnection db = new ExamDBConnection();
			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAllDataCatWise2(dateFrom, dateTo);
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(4)
						.toString()) + totalServiceCharge;
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("" +rs.getObject(1)
						.toString());
				rowhead1.createCell(2).setCellValue("" +rs.getObject(2)
						.toString());
				rowhead1.createCell(3).setCellValue(rs.getObject(3)
						.toString());
				rowhead1.createCell(4).setCellValue(totalServiceCharge);
				rowhead1.createCell(5).setCellValue(rs.getObject(5).toString());
		
				totalAmount = totalServiceCharge + totalAmount;
				k++;			}
			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
		
	}

}