package hms.patient.slippdf;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.cancellation.gui.CancelledDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;

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

public class NewSummaryReportExcel1 {

	private static Font smallBold = new Font(Font.FontFamily.HELVETICA, 8);
	private static Font spaceFont = new Font(Font.FontFamily.HELVETICA, 2);
	private static Font font1 = new Font(Font.FontFamily.HELVETICA, 15,
			Font.BOLD, BaseColor.BLACK);
	private static Font font2 = new Font(Font.FontFamily.HELVETICA, 8,
			Font.BOLD);
	private static Font font3 = new Font(Font.FontFamily.HELVETICA, 9.5f,
			Font.BOLD);
	private static Font font4 = new Font(Font.FontFamily.HELVETICA, 9,
			Font.BOLD, BaseColor.BLACK);
	private static Font tokenfont4 = new Font(Font.FontFamily.HELVETICA, 11,
			Font.BOLD, BaseColor.WHITE);
	public static String RESULT = "opdslip1.pdf";
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;
	Vector<String> doctorname = new Vector<String>();
	Vector<String> opdtypes = new Vector<String>();
	Vector<String> exams = new Vector<String>();
	Vector<String> examsSubCat = new Vector<String>();
	Vector<String> cancelled = new Vector<String>();
	Vector<String> achievements = new Vector<String>();
	Vector<String> specialization = new Vector<String>();
	Vector<String> reportCategory = new Vector<String>();
	Vector<String> miscType = new Vector<String>();
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
			amt_received, date, token_no;
	String mainDir = "", str = "";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	int k = 1;
	public static void main(String[] arg) {
		try {
			new NewSummaryReportExcel1("2015-09-20", "2015-09-20");
		} catch (DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NewSummaryReportExcel1(String dateFrom, String dateTo)
			throws DocumentException, IOException {
		// TODO Auto-generated constructor stub

		getAllOPDType();
		getAllExamList();
		getAllMISCType();
		reportCategory.add("OPD Services");
		reportCategory.add("CARD Charges");
		reportCategory.add("Exam Services");
		reportCategory.add("Misc Services");
		reportCategory.add("Indoor Services");
		reportCategory.add("Cancelled Amount");

		cancelled.add("OPD");
		cancelled.add("EXAM");
		cancelled.add("CARDS");
		cancelled.add("MISC");
		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/DailySummeryReport-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("DailySummeryReport");
			for (int i = 0; i < reportCategory.size(); i++) {

				
				switch (i) {
				case 0:
					opdAmount(dateFrom, dateTo,sheet);
					break;
				case 1:
					cardAmount(dateFrom, dateTo,sheet);
					break;
				case 2:
					examAmount(dateFrom, dateTo,sheet);
					break;
				case 3:
					miscAmount(dateFrom, dateTo,sheet);
					break;
				case 4:
					indoorAmount(dateFrom, dateTo,sheet);
					break;
				case 5:
					cancelledAmount(dateFrom, dateTo,sheet);
					break;
				case 6:

					break;
				case 7:

					break;

				default:
					break;
				}

			}
			totalAmount = totalAmount - cancelledAmount;

			HSSFRow rowhead1 = sheet.createRow((short) k);
			
			rowhead1.createCell(2).setCellValue("Total");
			rowhead1.createCell(3).setCellValue(totalAmount);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully on your Desktop with DailySummeryReport-"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);

		}
		

		
	}

	public void opdAmount(String dateFrom, String dateTo, HSSFSheet sheet) {
		HSSFRow rowhead = sheet.createRow((short) 0);
		rowhead.createCell(0).setCellValue("Serial No.");
		rowhead.createCell(1).setCellValue("Type");
		rowhead.createCell(2).setCellValue("No.");
		rowhead.createCell(3).setCellValue("Amount");

		
	
		try {
			OPDDBConnection db = new OPDDBConnection();

			for (int i = 0; i < opdtypes.size(); i++) {
				double totalServiceCharge = 0;
				int totalNo = 0;
				ResultSet rs = db.retrieveAllData(dateFrom, dateTo, ""
						+ opdtypes.get(i));
				//System.out.println(opdtypes.get(i)+"");
				while (rs.next()) {
					System.out.println(rs.getObject(8)
							.toString());
//					totalServiceCharge = Double.parseDouble(rs.getObject(8)
//							.toString()) + totalServiceCharge;
//					totalNo++;
				}

				if (totalNo > 0) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(k);
					rowhead1.createCell(1).setCellValue("" );
					rowhead1.createCell(2).setCellValue(totalNo);
					rowhead1.createCell(3).setCellValue(totalServiceCharge);
					k++;
					totalAmount = totalServiceCharge + totalAmount;
					
				}
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}

	public void cardAmount(String dateFrom, String dateTo, HSSFSheet sheet) {
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			// opdTable.addCell(new Phrase("Services", font3));
			// opdTable.addCell(new Phrase("No.", font3));
			//
			// opdTable.addCell(new Phrase("Amount", font3));

			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAllDataCard(dateFrom, dateTo);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) + totalServiceCharge;
				totalNo++;
			}

			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("CARDS");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				k++;
				totalAmount = totalServiceCharge + totalAmount;
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}

	public void miscAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();

			// opdTable.addCell(new Phrase("Services", font3));
			// opdTable.addCell(new Phrase("No.", font3));
			//
			// opdTable.addCell(new Phrase("Amount", font3));

			double totalServiceCharge = 0;
			int totalNo = 0;
			ResultSet rs = db.retrieveAllData(dateFrom, dateTo);

			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(3)
						.toString()) + totalServiceCharge;
				totalNo++;
			}

			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("MISC");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				k++;
				
				totalAmount = totalServiceCharge + totalAmount;
			}
			db.closeConnection();
			
			MiscDBConnection miscDBConnection=new MiscDBConnection();
			
			for (int i = 0; i < miscType.size(); i++) {
				rs=miscDBConnection.retrieveAllDataCatWise(dateFrom, dateTo, miscType.get(i));
				totalServiceCharge = 0;
				totalNo = 0;
				while (rs.next()) {
					totalServiceCharge = Double.parseDouble(rs.getObject(1)
							.toString()) + totalServiceCharge;
					totalNo++;
				}
				if (totalNo > 0) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(k);
					rowhead1.createCell(1).setCellValue(""+miscType.get(i));
					rowhead1.createCell(2).setCellValue(totalNo);
					rowhead1.createCell(3).setCellValue(totalServiceCharge);
					k++;
					
					totalAmount = totalServiceCharge + totalAmount;
				}
			}
			
			miscDBConnection.closeConnection();
			
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
		
		
	}

	public void examAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		try {
		
			ExamDBConnection db = new ExamDBConnection();
			for (int i = 0; i < exams.size(); i++) {
				double totalServiceCharge = 0;
				int totalNo = 0;
				getExamSubCatList(exams.get(i));
				for (int j = 0; j < examsSubCat.size(); j++) {

					ResultSet rs = db.retrieveAllDataCatWise2(dateFrom, dateTo);
					System.out.println(exams.get(i)+" "+examsSubCat.get(j)+"");
					while (rs.next()) {
						
						totalServiceCharge = Double.parseDouble(rs.getObject(1)
								.toString()) + totalServiceCharge;
//						System.out.println(rs.getObject(2)
//								.toString()+"");
						totalNo++;
						
					}
				}
				if (totalNo > 0) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(k);
					rowhead1.createCell(1).setCellValue(""+ exams.get(i));
					rowhead1.createCell(2).setCellValue(totalNo);
					rowhead1.createCell(3).setCellValue(totalServiceCharge);
					k++;
					
					totalAmount = totalServiceCharge + totalAmount;
				}
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void indoorAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

	
		try {
			double totalServiceCharge = 0;
			int totalNo = 0;
			IPDPaymentsDBConnection paymentsDBConnection = new IPDPaymentsDBConnection();
			ResultSet resultSet = paymentsDBConnection
					.retrieveAllDataAdvanceAmount(dateFrom, dateTo);

			while (resultSet.next()) {
				totalServiceCharge = Double.parseDouble(resultSet.getObject(1)
						.toString()) + totalServiceCharge;
				totalNo++;
			}
			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("Advance Payment");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				k++;
				totalAmount = totalServiceCharge + totalAmount;
			}

			totalServiceCharge = 0;
			totalNo = 0;
			resultSet = paymentsDBConnection.retrieveAllDataReturnedAmount(
					dateFrom, dateTo);

			while (resultSet.next()) {
				totalServiceCharge = Double.parseDouble(resultSet.getObject(1)
						.toString()) + totalServiceCharge;
				totalNo++;
			}
			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("Return Amount");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(-totalServiceCharge);
				k++;
				totalAmount = totalAmount - totalServiceCharge;
			}

			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataDischargeAmount(dateFrom, dateTo);
			totalServiceCharge = 0;
			totalNo = 0;
			while (rs.next()) {
				totalServiceCharge = Double.parseDouble(rs.getObject(1)
						.toString()) + totalServiceCharge;
				totalNo++;
			}
			if (totalNo > 0) {
				HSSFRow rowhead1 = sheet.createRow((short) k);
				rowhead1.createCell(0).setCellValue(k);
				rowhead1.createCell(1).setCellValue("Indoor Discharge");
				rowhead1.createCell(2).setCellValue(totalNo);
				rowhead1.createCell(3).setCellValue(totalServiceCharge);
				k++;
				
				totalAmount = totalServiceCharge + totalAmount;
			}

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}

	public void cancelledAmount(String dateFrom, String dateTo, HSSFSheet sheet) {

		
		try {
			CancelledDBConnection db = new CancelledDBConnection();

			for (int i = 0; i < cancelled.size(); i++) {
				double totalServiceCharge = 0;
				int totalNo = 0;
				ResultSet rs = db.retrieveAmount(dateFrom, dateTo, ""
						+ cancelled.get(i));
				System.out.println(cancelled.get(i));
				while (rs.next()) {
					totalServiceCharge = Double.parseDouble(rs.getObject(1)
							.toString()) + totalServiceCharge;
					totalNo++;
				}
				if (totalNo > 0) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					rowhead1.createCell(0).setCellValue(k);
					rowhead1.createCell(1).setCellValue("" + cancelled.get(i));
					rowhead1.createCell(2).setCellValue(totalNo);
					rowhead1.createCell(3).setCellValue(-totalServiceCharge);
					k++;
					cancelledAmount = totalServiceCharge + cancelledAmount;
				}
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(DailyCash.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		
	}

	public void getAllExamList() {

		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExams();
		try {
			while (resultSet.next()) {

				exams.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	public void getExamSubCatList(String exam_name) {
		examsSubCat.removeAllElements();
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.getExamSubCat1(exam_name);
		try {
			while (resultSet.next()) {
				examsSubCat.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getAllOPDType() {
		PriceMasterDBConnection priceMasterDBConnection = new PriceMasterDBConnection();
		ResultSet resultSet = priceMasterDBConnection
				.retrieveDataWithCategory("00");
		try {
			while (resultSet.next()) {
				opdtypes.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		priceMasterDBConnection.closeConnection();
	}
	
	public void getAllMISCType() {
		MiscDBConnection miscDBConnection = new MiscDBConnection();
		ResultSet resultSet = miscDBConnection
				.retrieveAllMiscCat();
		try {
			while (resultSet.next()) {
				miscType.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		miscDBConnection.closeConnection();
	}

}