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

public class DoctorDataExcel {

	String doctorName,fromDateSTR, toDateSTR;
	String month_name;
	

	public DoctorDataExcel(String fromDate, String toDate,String doctorName)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		fromDateSTR = fromDate;
		toDateSTR = toDate;
		this.doctorName=doctorName;
		
		SimpleDateFormat month_date = new SimpleDateFormat("yyyy", Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String actualDate = fromDateSTR;

		Date date = null;
		try {
			date = sdf.parse(actualDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		month_name = month_date.format(date);
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + doctorName+" , Year "+month_name + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("OPD DATA");
			opdData(sheet);
			sheet = workbook.createSheet("Exam DATA");
			examData(sheet);
			sheet = workbook.createSheet("IPD DATA");
			ipdData(sheet);
			sheet = workbook.createSheet("MISC DATA");
			miscData(sheet);

			sheet = workbook.createSheet("IPD Expense Data");
			IPDEXPENSE(sheet);
			sheet = workbook.createSheet("IPD Expense Data all");
			IPDEXPENSEALL(sheet);
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

	public void opdData(HSSFSheet sheet) {

	
		int k = 0;
		try {
			DoctorDBConnection db = new DoctorDBConnection();
			ResultSet rs = db.retrieveDoctorYearOPD(doctorName, month_name);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
			k++;
			while (rs.next()) {

				HSSFRow rowhead1 = sheet.createRow((short) k);
				for (int i = 0; i < columnsNumber; i++) {
					rowhead1.createCell(i).setCellValue(
							rs.getObject(i + 1).toString());
				}
				k++;

			}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print("" + ex);
		}
	}

public void examData(HSSFSheet sheet) {

		
	
		int k = 0;
		try {
			DoctorDBConnection db = new DoctorDBConnection();
			ResultSet rs = db.retrieveDoctorYearExam(doctorName, month_name);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
			k++;
			while (rs.next()) {

				HSSFRow rowhead1 = sheet.createRow((short) k);
				for (int i = 0; i < columnsNumber; i++) {
					rowhead1.createCell(i).setCellValue(
							rs.getObject(i + 1).toString());
				}
				k++;

			}
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.print("" + ex);
		}
	}
public void ipdData(HSSFSheet sheet) {

	
	
	int k = 0;
	try {
		DoctorDBConnection db = new DoctorDBConnection();
		ResultSet rs = db.retrieveDoctorYearIPD(doctorName, month_name);
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		HSSFRow rowhead = sheet.createRow((short) k);
		for (int i = 0; i < columnsNumber; i++) {
			rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
		}
		k++;
		while (rs.next()) {

			HSSFRow rowhead1 = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead1.createCell(i).setCellValue(
						rs.getObject(i + 1).toString());
			}
			k++;

		}
		db.closeConnection();
	} catch (SQLException ex) {
		System.out.print("" + ex);
	}
}
public void miscData(HSSFSheet sheet) {

	

	int k = 0;
	try {
		DoctorDBConnection db = new DoctorDBConnection();
		ResultSet rs = db.retrieveDoctorYearMisc(doctorName, month_name);
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		HSSFRow rowhead = sheet.createRow((short) k);
		for (int i = 0; i < columnsNumber; i++) {
			rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
		}
		k++;
		while (rs.next()) {

			HSSFRow rowhead1 = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead1.createCell(i).setCellValue(
						rs.getObject(i + 1).toString());
			}
			k++;

		}
		db.closeConnection();
	} catch (SQLException ex) {
		System.out.print("" + ex);
	}
}
public void IPDEXPENSE(HSSFSheet sheet) {

	

	int k = 0;
	try {
		DoctorDBConnection db = new DoctorDBConnection();
		ResultSet rs = db.retrieveDoctorIPDEXPENSE(doctorName, fromDateSTR,toDateSTR);
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		HSSFRow rowhead = sheet.createRow((short) k);
		for (int i = 0; i < columnsNumber; i++) {
			rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
		}
		k++;
		while (rs.next()) {

			HSSFRow rowhead1 = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead1.createCell(i).setCellValue(
						rs.getObject(i + 1).toString());
			}
			k++;

		}
		db.closeConnection();
	} catch (SQLException ex) {
		System.out.print("" + ex);
	}
}
public void IPDEXPENSEALL(HSSFSheet sheet) {

	

	int k = 0;
	try {
		DoctorDBConnection db = new DoctorDBConnection();
		ResultSet rs = db.retrieveDoctorIPDEXPENSEAll(doctorName, fromDateSTR,toDateSTR);
		java.sql.ResultSetMetaData rsmd = rs.getMetaData();
		int columnsNumber = rsmd.getColumnCount();
		HSSFRow rowhead = sheet.createRow((short) k);
		for (int i = 0; i < columnsNumber; i++) {
			rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
		}
		k++;
		while (rs.next()) {

			HSSFRow rowhead1 = sheet.createRow((short) k);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead1.createCell(i).setCellValue(
						rs.getObject(i + 1).toString());
			}
			k++;

		}
		db.closeConnection();
	} catch (SQLException ex) {
		System.out.print("" + ex);
	}
}
}