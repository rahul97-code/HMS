package hms.reports.gui;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OpenFile;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;

import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import jxl.Sheet;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFCell;

import com.itextpdf.text.pdf.PdfPTable;

public class SummaryReportExcel {
	

	public SummaryReportExcel(String filename,String fromDate,String toDate)
	{
		try {
		
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("OPD");
			opdAmount(fromDate,toDate, sheet);
			
			HSSFSheet sheet1 = workbook.createSheet("CARDS");
			cardAmount(fromDate,toDate, sheet1);
			
			HSSFSheet sheet2 = workbook.createSheet("MISC");
			miscAmount(fromDate,toDate, sheet2);
		
			try {
				HSSFSheet sheet3 = workbook.createSheet("EXAMS");
				examAmount(fromDate,toDate, sheet3);
			} catch (Exception e) {
				System.out.print(e+"");
			}
			HSSFSheet sheet4 = workbook.createSheet("ADVANCE-RETURN");
			Advance(fromDate,toDate, sheet4);
			
			HSSFSheet sheet5 = workbook.createSheet("INDOOR DISCHARGE");
			indoorAmount(fromDate,toDate, sheet5);
			
			HSSFSheet sheet6 = workbook.createSheet("EXAM RECIEPTS");
			examAmountReciepts(fromDate,toDate, sheet6);
			
//			HSSFSheet sheet7 = workbook.createSheet("All PATIENTS");
//			examAmountReciepts(fromDate,toDate, sheet7);
			
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();

			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);

		}
	}
	public void opdAmount(String dateFrom, String dateTo,HSSFSheet sheet) {

	
		try {
			OPDDBConnection db = new OPDDBConnection();

		
			
				ResultSet rs = db.retrieveAllDataExcel(dateFrom, dateTo);
				 
				 HSSFRow rowhead = sheet.createRow((short) 0);
					rowhead.createCell(0).setCellValue("OPD No.");
					rowhead.createCell(1).setCellValue("Patient ID");
					rowhead.createCell(2).setCellValue("Patient Name");
					rowhead.createCell(3).setCellValue("Doctor");
					rowhead.createCell(4).setCellValue("OPD TYPE");
					rowhead.createCell(5).setCellValue("Amount");
					rowhead.createCell(6).setCellValue("Date");
					rowhead.createCell(7).setCellValue("Time");
					rowhead.createCell(8).setCellValue("User");
				int i=1;
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) i);
					rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
					rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
					rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
					rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
					rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
					rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
					rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
					rowhead1.createCell(7).setCellValue(rs.getObject(8).toString());
					rowhead1.createCell(8).setCellValue(rs.getObject(9).toString());
					i++;
				}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
	public void cardAmount(String dateFrom, String dateTo,HSSFSheet sheet) {
		
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			
			
			ResultSet rs = db.retrieveAllDataCardExcel(dateFrom, dateTo);
			
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Reciept No");
			rowhead.createCell(1).setCellValue("ID NO.");
			rowhead.createCell(2).setCellValue("Patient Name");
			rowhead.createCell(3).setCellValue("Amount");
			rowhead.createCell(4).setCellValue("User");
			rowhead.createCell(5).setCellValue("Date");
			rowhead.createCell(6).setCellValue("Time");
			
			int i=1;
			while (rs.next()) {
				HSSFRow rowhead1 = sheet.createRow((short) i);
				rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
				rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
				rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
				rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
				rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
				rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
				rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
				
				i++;
			}

			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}

	public void miscAmount(String dateFrom, String dateTo,HSSFSheet sheet) {

		
		try {
			
		
			
			MiscDBConnection miscDBConnection=new MiscDBConnection();
			
		
			ResultSet rs = miscDBConnection.retrieveAllDataCatWiseExcel(dateFrom, dateTo);
		
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Reciept No.");
			rowhead.createCell(1).setCellValue("MISC TYPE");
			rowhead.createCell(2).setCellValue("Patient ID");
			rowhead.createCell(3).setCellValue("Patient Name");
			rowhead.createCell(4).setCellValue("Doctor");
			rowhead.createCell(5).setCellValue("Amount");
			rowhead.createCell(6).setCellValue("Date");
			rowhead.createCell(7).setCellValue("Time");
			rowhead.createCell(8).setCellValue("User");
		int i=1;
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) i);
					rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
					rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
					rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
					rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
					rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
					rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
					rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
					rowhead1.createCell(7).setCellValue(rs.getObject(8).toString());
					rowhead1.createCell(8).setCellValue(rs.getObject(9).toString());
					i++;
				}
				
			
			miscDBConnection.closeConnection();
			
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
		
		
		
	}

	public void examAmount(String dateFrom, String dateTo,HSSFSheet sheet) {

		
		try {

			ExamDBConnection db = new ExamDBConnection();
			
					ResultSet rs = db.retrieveAllDataExcel(dateFrom, dateTo);
				
					HSSFRow rowhead = sheet.createRow((short) 0);
					rowhead.createCell(0).setCellValue("Exam No.");
					rowhead.createCell(1).setCellValue("Patient ID");
					rowhead.createCell(2).setCellValue("Patient Name");
					rowhead.createCell(3).setCellValue("Doctor");
					rowhead.createCell(4).setCellValue("Exam TYPE");
					rowhead.createCell(5).setCellValue("Amount");
					rowhead.createCell(6).setCellValue("Date");
					rowhead.createCell(7).setCellValue("User");
				int i=1;
					while (rs.next()) {	
						HSSFRow rowhead1 = sheet.createRow((short) i);
						rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
						rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
						rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
						rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
						rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
						rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
						rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
						rowhead1.createCell(7).setCellValue(rs.getObject(8).toString());
						i++;
					}
				
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.println(ex+"");
		}
		
	}

public void examAmountReciepts(String dateFrom, String dateTo,HSSFSheet sheet) {


		try {

			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			
					ResultSet rs = db.retrieveAllExamsExcel(dateFrom, dateTo);
				
					HSSFRow rowhead = sheet.createRow((short) 0);
					rowhead.createCell(0).setCellValue("Reciept No.");
					rowhead.createCell(1).setCellValue("Exam Cat");
					rowhead.createCell(2).setCellValue("Doctor");
					rowhead.createCell(3).setCellValue("Patient Name");
					rowhead.createCell(4).setCellValue("Amount");
					rowhead.createCell(5).setCellValue("User");
					rowhead.createCell(6).setCellValue("Date");
					rowhead.createCell(7).setCellValue("Time");
					rowhead.createCell(8).setCellValue("Exam Start Counter");
					rowhead.createCell(9).setCellValue("Exam End Counter");
				int i=1;
					while (rs.next()) {	
						HSSFRow rowhead1 = sheet.createRow((short) i);
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
						
						i++;
					}
				
			db.closeConnection();
		} catch (SQLException ex) {
			System.out.println(ex+"");
		}
		
	}

	public void Advance(String dateFrom, String dateTo,HSSFSheet sheet) {

	
		try {
			
			IPDPaymentsDBConnection paymentsDBConnection = new IPDPaymentsDBConnection();
			ResultSet rs = paymentsDBConnection
					.retrieveAllDataAdvanceAmountExcel(dateFrom, dateTo);
			 
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Payment No.");
			rowhead.createCell(1).setCellValue("IPD ID");
			rowhead.createCell(2).setCellValue("Patient ID");
			rowhead.createCell(3).setCellValue("Patient Name");
			rowhead.createCell(4).setCellValue("Payment TYPE");
			rowhead.createCell(5).setCellValue("Amount");
			rowhead.createCell(6).setCellValue("Date");
			rowhead.createCell(7).setCellValue("Time");
			rowhead.createCell(8).setCellValue("User");
		int i=1;
			while (rs.next()) {
				HSSFRow rowhead1 = sheet.createRow((short) i);
				rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
				rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
				rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
				rowhead1.createCell(3).setCellValue(rs.getObject(4).toString());
				rowhead1.createCell(4).setCellValue(rs.getObject(5).toString());
				rowhead1.createCell(5).setCellValue(rs.getObject(6).toString());
				rowhead1.createCell(6).setCellValue(rs.getObject(7).toString());
				rowhead1.createCell(7).setCellValue(rs.getObject(8).toString());
				rowhead1.createCell(8).setCellValue(rs.getObject(9).toString());
				i++;
			}
			
			paymentsDBConnection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}

	public void indoorAmount(String dateFrom, String dateTo,HSSFSheet sheet) {

	
		try {
	
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataExcel(dateFrom, dateTo);
		
			HSSFRow rowhead = sheet.createRow((short) 0);
			rowhead.createCell(0).setCellValue("Running No.");
			rowhead.createCell(1).setCellValue("IPD ID");
			rowhead.createCell(2).setCellValue("Patient ID");
			rowhead.createCell(3).setCellValue("Patient Name");
			rowhead.createCell(4).setCellValue("Entry Date");
			rowhead.createCell(5).setCellValue("Entry Time");
			rowhead.createCell(6).setCellValue("Entry User");
			rowhead.createCell(7).setCellValue("Advance");
			rowhead.createCell(9).setCellValue("Discharge Date");
			rowhead.createCell(10).setCellValue("Discharge Time");
			rowhead.createCell(11).setCellValue("Discharge User");
			
		int i=1;
			while (rs.next()) {
				HSSFRow rowhead1 = sheet.createRow((short) i);
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
				i++;
			}
			

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}
}