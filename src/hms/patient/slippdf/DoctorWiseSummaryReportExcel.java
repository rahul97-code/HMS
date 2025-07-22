package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
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

public class DoctorWiseSummaryReportExcel {

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
	String mainDir = "", str = "", DoctorName = "Dr. Karan Sobti";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };

	public static void main(String[] arg) {
		try {
			new DoctorWiseSummaryReportExcel("2013-03-24", "2015-04-24",
					"Dr. Karan Sobti");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public DoctorWiseSummaryReportExcel(String fromDate, String toDate,
			String doctorName) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		DoctorName = doctorName;

		reportCategory.add("OPD Services");
		reportCategory.add("Exam Services");
		reportCategory.add("Misc Services");
		reportCategory.add("Indoor Services");

		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + doctorName + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("OPD");
			opdAmount(fromDate, toDate, sheet);

			HSSFSheet sheet2 = workbook.createSheet("MISC");
			miscAmount(fromDate, toDate, sheet2);

			try {
				HSSFSheet sheet3 = workbook.createSheet("EXAMS");
				examAmount(fromDate, toDate, sheet3);
			} catch (Exception e) {
				System.out.print(e + "");
			}

			HSSFSheet sheet5 = workbook.createSheet("INDOOR DISCHARGE");
			indoorAmount(fromDate, toDate, sheet5);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();

			JOptionPane.showMessageDialog(
					null,
					"Excel File Generated Successfully on your Desktop with "
							+ doctorName + "-"
							+ DateFormatChange.StringToMysqlDate(new Date())
							+ " Name", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);

		}
	}

	public void opdAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("OPD Type");
		rowhead.createCell(2).setCellValue("No.");
		rowhead.createCell(3).setCellValue("Amount");

		int k = 1;
		try {
			OPDDBConnection db = new OPDDBConnection();

			double totalServiceCharge = 0;
			ResultSet rs = db.retrieveAllDataDoctor(dateFrom, dateTo,
					DoctorName);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;

				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue(
						"" + rs.getObject(2).toString());
				rowhead1.createCell(2).setCellValue(rs.getObject(1).toString());
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}

			HSSFRow rowhead1 = sheet.createRow((short) k);

			rowhead1.createCell(2).setCellValue("Total");
			rowhead1.createCell(3).setCellValue(totalAmount);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		totalAmount = 0;
	}

	public void miscAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("Misc Type");
		rowhead.createCell(2).setCellValue("No.");
		rowhead.createCell(3).setCellValue("Amount");

		int k = 1;
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();


			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAllDataDoctorWise(dateFrom, dateTo,
					DoctorName);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) + totalServiceCharge;
				totalNo++;
			}

			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(1);
				rowhead1.createCell(1).setCellValue("MISC");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				
				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}
			db.closeConnection();

			MiscDBConnection miscDBConnection = new MiscDBConnection();

			rs = miscDBConnection.retrieveAllDataDoctorWise(dateFrom, dateTo,
					DoctorName);
			totalServiceCharge = 0;
			totalNo = 0;
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue(
						"" + rs.getObject(1).toString());
				rowhead1.createCell(2).setCellValue(rs.getObject(2).toString());
				rowhead1.createCell(3).setCellValue(totalServiceCharge);

				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}

			HSSFRow rowhead1 = sheet.createRow((short) k);

			rowhead1.createCell(2).setCellValue("Total");
			rowhead1.createCell(3).setCellValue(totalAmount);
			miscDBConnection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		totalAmount = 0;
	}

	public void examAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("Exam Type");
		rowhead.createCell(2).setCellValue("No.");
		rowhead.createCell(3).setCellValue("Amount");

		int k = 1;
		try {

			ExamDBConnection db = new ExamDBConnection();

			double totalServiceCharge = 0;
			ResultSet rs = db.retrieveAllDataDoctor(dateFrom, dateTo,
					DoctorName);
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) ;
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("" +rs.getObject(1)
						.toString());
				rowhead1.createCell(2).setCellValue(rs.getObject(2)
						.toString());
				rowhead1.createCell(3).setCellValue(totalServiceCharge);

				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}

			HSSFRow rowhead1 = sheet.createRow((short) k);

			rowhead1.createCell(2).setCellValue("Total");
			rowhead1.createCell(3).setCellValue(totalAmount);

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		totalAmount = 0;
	}

	public void indoorAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("IPD");
		rowhead.createCell(2).setCellValue("No.");
		rowhead.createCell(3).setCellValue("Amount");

		int k = 1;
		ipdID.clear();
		try {
			double totalServiceCharge = 0;
			int totalNo = 0;
			IPDDBConnection db3 = new IPDDBConnection();
			ResultSet rs3 = db3.retrieveAllDataDoctorIPD1(dateFrom, dateTo,
					DoctorName);

			while (rs3.next()) {

				totalServiceCharge = Double.parseDouble(rs3.getObject(1)
						.toString()) + totalServiceCharge;
				totalNo++;
			}

			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(1);
				rowhead1.createCell(1).setCellValue("IPD");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				totalAmount = totalServiceCharge + totalAmount;
				k++;
			}

			rs3 = db3.retrieveAllDataDoctorIPDAdvance(dateFrom, dateTo,
					DoctorName);

			while (rs3.next()) {
				try {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(1);
					rowhead1.createCell(1).setCellValue("Advance Payments");
					rowhead1.createCell(2).setCellValue(
							rs3.getObject(1).toString());
					rowhead1.createCell(3).setCellValue(
							rs3.getObject(2).toString());
					k++;
					totalAmount = Double.parseDouble(rs3.getObject(2)
							.toString()) + totalAmount;
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			HSSFRow rowhead1 = sheet.createRow((short) k);
			rowhead1.createCell(2).setCellValue("Total");
			rowhead1.createCell(3).setCellValue(totalAmount);
			db3.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		totalAmount = 0;
	}

}