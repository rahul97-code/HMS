package hms.reports.excels;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
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
import com.mysql.jdbc.ResultSetMetaData;

public class PatientPillsRegisterReportExcel {

	
	Object Rows_Object_Array[][];
	int NumberOfColumns = 0, NumberOfRows = 0;

	static String OS;
	String mainDir = "", str = "", DoctorName = "Dr. Karan Sobti";
	Font font;
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR,toDateSTR;
	
	public PatientPillsRegisterReportExcel(String filename,String fromDate,String toDate) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		
		try {
			
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Item Rate List");
			issuedRegister( sheet);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(filename);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void issuedRegister( HSSFSheet sheet) {
		
		int k = 1;
		try {
			IssuedItemsDBConnection db = new IssuedItemsDBConnection();
			ResultSet rs = db.retrieveAllIssuedPatientPills(fromDateSTR,toDateSTR);
			java.sql.ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i < columnsNumber; i++) {
				rowhead.createCell(i).setCellValue(rsmd.getColumnName(i+1));
			}
				while (rs.next()) {
					HSSFRow rowhead1 = sheet.createRow((short) k);
					for (int i = 0; i < columnsNumber; i++) {
						rowhead1.createCell(i).setCellValue(rs.getObject(i+1)==null || rs.getObject(i+1).equals("")?"...":rs.getObject(i+1).toString());
					}
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