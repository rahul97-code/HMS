package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.InvoiceDBConnection;
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

public class InsuranceWiseSummaryReportExcel {

	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;

	Vector<String> reportCategory = new Vector<String>();

	String mainDir = "", str = "", InsurancetypeSTR = "Dr. Karan Sobti";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	HSSFSheet sheet ;
	public static void main(String[] arg) {
		try {
			new InsuranceWiseSummaryReportExcel("2015-04-24", "2015-04-24",
					"Unknown");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InsuranceWiseSummaryReportExcel(String fromDate, String toDate,
			String InsuranceType) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		InsurancetypeSTR = InsuranceType;

		reportCategory.add(InsurancetypeSTR+" OPD");
		reportCategory.add(InsurancetypeSTR+" Exam");
		reportCategory.add(InsurancetypeSTR+" Misc");
		reportCategory.add(InsurancetypeSTR+" IPD");

		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + InsurancetypeSTR + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			for (int i = 0; i < reportCategory.size(); i++) {
				switch (i) {
				case 0:
					 sheet = workbook.createSheet(reportCategory.get(i));
						opdAmount(fromDate, toDate, sheet);
					break;
				case 1:
					 sheet = workbook.createSheet(reportCategory.get(i));
						examAmount(fromDate, toDate, sheet);
					break;
				case 2:
					 sheet = workbook.createSheet(reportCategory.get(i));
						miscAmount(fromDate, toDate, sheet);
					break;
				case 3:
					 sheet = workbook.createSheet(reportCategory.get(i));
						ipdAmount(fromDate, toDate, sheet);
					break;

				default:
					break;
				}

			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();

			JOptionPane.showMessageDialog(
					null,
					"Excel File Generated Successfully on your Desktop with "
							+ InsuranceType + "-"
							+ DateFormatChange.StringToMysqlDate(new Date())
							+ " Name", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);

		}
	}

	public void opdAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		int k = 1;
		try {
			OPDDBConnection db = new OPDDBConnection();

			ResultSet rs = db.retrieveAllDetailCategory(dateFrom, dateTo,
					InsurancetypeSTR);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
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
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void examAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		int k = 1;
		try {
			ExamDBConnection db = new ExamDBConnection();

			ResultSet rs = db.retrieveAllDetailCatWiseInsurance(
					InsurancetypeSTR, dateFrom, dateTo);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
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
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void miscAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		int k = 1;
		try {
			MiscDBConnection miscDBConnection = new MiscDBConnection();

			ResultSet rs = miscDBConnection.retrieveAllDetailCatWiseInsurance(
					InsurancetypeSTR, dateFrom, dateTo);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
			while (rs.next()) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				for (int i = 0; i < columnsNumber; i++) {
					rowhead1.createCell(i).setCellValue(
							rs.getObject(i + 1).toString());
				}
				k++;
			}
			miscDBConnection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void ipdAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		int k = 1;
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataInsurance(InsurancetypeSTR,
					dateFrom, dateTo);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i + 1));
			}
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
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

}