package hms.reports.excels;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OpenFile;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnItemsDBConnection;
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

public class WardsIssueConsumeExcel {

	
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
	Vector<String> dept = new Vector<String>();
	Vector<String> items = new Vector<String>();
	Vector<String> itemsid = new Vector<String>();
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR,toDateSTR;
	double stock=0,issued=0,consumable=0,returned=0;
	

	public WardsIssueConsumeExcel(String filename,String fromDate,String toDate) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		getAllDepartments() ;
		try {
			getItems();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			for (int i = 0; i < dept.size(); i++) {
				System.out.println(""+dept.get(i));
				HSSFSheet sheet = workbook.createSheet(""+dept.get(i));
				issuedRegister( sheet,dept.get(i));
			}
			

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

	public void issuedRegister( HSSFSheet sheet,String dept) {
		
		HSSFRow rowhead0 = sheet.createRow((short) 0);
		
			rowhead0.createCell(0).setCellValue("Department Name :");
			rowhead0.createCell(1).setCellValue(" "+dept);
			rowhead0.createCell(2).setCellValue("-----");
			rowhead0.createCell(3).setCellValue("Generated Date");
			rowhead0.createCell(4).setCellValue(""+DateFormatChange.StringToMysqlDate(new Date()));
		int k = 2;
		try {
			
			
		
			HSSFRow rowhead = sheet.createRow((short) 1);
			
			rowhead.createCell(0).setCellValue("Item ID");
			rowhead.createCell(1).setCellValue("Item name");
			rowhead.createCell(2).setCellValue("Item Stock");
			rowhead.createCell(3).setCellValue("Item Issued");
			rowhead.createCell(4).setCellValue("Item Consumed");
			rowhead.createCell(5).setCellValue("Item Returned");
			
			for (int i = 0; i < itemsid.size(); i++) {
				
				if(getItemStock(itemsid.get(i),dept))
				{
					HSSFRow rowhead1 = sheet.createRow((short) k);
					k++;
					rowhead1.createCell(0).setCellValue(itemsid.get(i));
					rowhead1.createCell(1).setCellValue(items.get(i));
					rowhead1.createCell(2).setCellValue(stock);
					rowhead1.createCell(3).setCellValue(issued);
					rowhead1.createCell(4).setCellValue(consumable);
					rowhead1.createCell(5).setCellValue(returned);
				}
			}
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
	
	public boolean getItemStock(String itemID,String dept) {

		double quantity = 0;
		boolean stockB=false;
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStock(itemID,
				dept);

		try {
			while (resultSet.next()) {

				quantity = Double
						.parseDouble((resultSet.getObject(1).toString()));
				stockB=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(""+itemID+"   "+dept);
		departmentStockDBConnection.closeConnection();
		if(stockB)
		{
			stock=quantity;
			issued=issued(dept, itemID);
			consumable=consumables(dept, itemID);
			returned=returned(dept, itemID);
			return true;
		}
		else {
			return false;
		}

	}
	public double consumables(String dept,String itemID)
	{
		double quantity = 0;
		Dept_PillsRegisterDBConnection dept_PillsRegisterDBConnection = new Dept_PillsRegisterDBConnection();
		ResultSet resultSet = dept_PillsRegisterDBConnection.retrieveConsumables(fromDateSTR, toDateSTR, dept, itemID);

		try {
			while (resultSet.next()) {

				quantity = quantity+Double
						.parseDouble((resultSet.getObject(1).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dept_PillsRegisterDBConnection.closeConnection();
		return quantity;
	}
	
	
	public double issued(String dept,String itemID)
	{
		double quantity = 0;
		IssuedItemsDBConnection issuedItemsDBConnection = new IssuedItemsDBConnection();
		ResultSet resultSet = issuedItemsDBConnection.retrieveIssues(fromDateSTR, toDateSTR, dept, itemID);

		try {
			while (resultSet.next()) {

				quantity = quantity+Double
						.parseDouble((resultSet.getObject(1).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		issuedItemsDBConnection.closeConnection();
		return quantity;
	}
	public double returned(String dept,String itemID)
	{
		double quantity = 0;
		ReturnItemsDBConnection returnItemsDBConnection = new ReturnItemsDBConnection();
		ResultSet resultSet = returnItemsDBConnection.retrieveReturened(fromDateSTR, toDateSTR, dept, itemID);

		try {
			while (resultSet.next()) {

				quantity = quantity+Double
						.parseDouble((resultSet.getObject(1).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		returnItemsDBConnection.closeConnection();
		return quantity;
	}
	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		dept.removeAllElements();
		System.out.print("hey");
		try {
			while (resultSet.next()) {
				dept.addElement(resultSet.getObject(2).toString());
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getItems() throws Exception
	{
		
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllItems();
			items.clear();
			itemsid.clear();
			while (rs.next()) {
				itemsid.add(rs.getObject(1).toString());
				items.add(rs.getObject(2).toString());
			}
			db.closeConnection();
	}
}