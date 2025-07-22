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
import hms1.expenses.database.IPDExpensesDBConnection;
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

public class Indoor_OutdoorExcel {

	
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
	Vector<String> itemsCombo = new Vector<String>();
	static String OS;
	String mainDir = "", str = "", DoctorName = "Dr. Karan Sobti";
	Font font;
	Vector<String> type = new Vector<String>();
	Vector<String> items = new Vector<String>();
	Vector<String> itemsid = new Vector<String>();
	double totalAmount = 0, cancelledAmount = 0;
	float[] opdTablCellWidth = { 2.0f, 0.7f, 1.1f };
	String fromDateSTR,toDateSTR;
	double unitprice=0,quantity=0,totalcost=0;
	

	public Indoor_OutdoorExcel(String filename,String fromDate,String toDate) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		
		fromDateSTR=fromDate;
		toDateSTR=toDate;
		System.out.print("hey");
		type.addElement("INDOOR");
		type.addElement("INDOOR-Procedure");
		type.addElement("OUTDOOR");
		
		itemsCombo.add("ORTHO PROCEDURE");
		itemsCombo.add("ORTHO ANESTHESIA");
		itemsCombo.add("ORTHO INDOOR");
		itemsCombo.add("ORTHO MEDICINE");
		itemsCombo.add("GENERAL PROCEDURE");
		itemsCombo.add("GENERAL ANESTHESIA");
		itemsCombo.add("GENERAL INDOOR");
		itemsCombo.add("GENERAL MEDICINE");
		itemsCombo.add("CANCER PROCEDURE");
		itemsCombo.add("CANCER ANESTHESIA");
		itemsCombo.add("CANCER INDOOR");
		itemsCombo.add("CANCER MEDICINE");
		itemsCombo.add("OT MEDICINES");
		itemsCombo.add("BED CHARGES");
		itemsCombo.add("DRESSING");
		itemsCombo.add("AMBULANCE");
		itemsCombo.add("ADMISSION FEES");
		itemsCombo.add("OTHER CHARGES");
		itemsCombo.add("DISCOUNT");
		itemsCombo.add("BPL DISCOUNT");
		itemsCombo.add("BSNL DISCOUNT");
		try {
			getItems();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
		
			HSSFWorkbook workbook = new HSSFWorkbook();
			
			for (int i = 0; i < type.size(); i++) {
				System.out.println(""+type.get(i));
				if(i==0)
				{
					HSSFSheet sheet = workbook.createSheet(""+type.get(i));
					issuedRegister( sheet,type.get(i));
				}
				else if(i==1){
					HSSFSheet sheet = workbook.createSheet(""+type.get(i));
					procedureRegister( sheet,type.get(i));
				}
				else {
					HSSFSheet sheet = workbook.createSheet(""+type.get(i));
					outdoor( sheet,type.get(i));
				}
				
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
		
			rowhead0.createCell(0).setCellValue("Data Type : " );
			rowhead0.createCell(1).setCellValue(" "+dept);
			rowhead0.createCell(2).setCellValue("-----");
			rowhead0.createCell(3).setCellValue("Generated Date");
			rowhead0.createCell(4).setCellValue(""+DateFormatChange.StringToMysqlDate(new Date()));
			String[] st = null;
			if(dept.equals(type.get(0)))
			{
				try {
					st=ipdDetail();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				
			}
			
			rowhead0 = sheet.createRow((short) 1);
			rowhead0.createCell(0).setCellValue("Total Patients : ");
			rowhead0.createCell(1).setCellValue(" "+st[0]);
			
			
			rowhead0 = sheet.createRow((short) 2);
			rowhead0.createCell(0).setCellValue("Total Days : ");
			rowhead0.createCell(1).setCellValue(" "+st[1]);
			
			rowhead0 = sheet.createRow((short) 3);
			rowhead0.createCell(0).setCellValue("Total Income : ");
			rowhead0.createCell(1).setCellValue(" "+st[2]);
			
			
			rowhead0 = sheet.createRow((short) 4);
			rowhead0.createCell(0).setCellValue("Date ");
			rowhead0.createCell(1).setCellValue(" From : ");
			rowhead0.createCell(2).setCellValue(""+fromDateSTR);
			rowhead0.createCell(3).setCellValue(" To :");
			rowhead0.createCell(4).setCellValue(""+toDateSTR);
			
			rowhead0 = sheet.createRow((short) 5);
			rowhead0.createCell(0).setCellValue("Medicines ");
			rowhead0.createCell(1).setCellValue(" ");
		int k = 6;
		try {
			
			
		
			HSSFRow rowhead = sheet.createRow((short) 5);
			
			rowhead.createCell(0).setCellValue("Item ID");
			rowhead.createCell(1).setCellValue("Item name");
			rowhead.createCell(2).setCellValue("Unit Price");
			rowhead.createCell(3).setCellValue("Quantity");
			rowhead.createCell(4).setCellValue("Total Cost");
			
			
			for (int i = 0; i < itemsid.size(); i++) {
				
				departments(dept,itemsid.get(i));
				if(unitprice!=0)
				{

					HSSFRow rowhead1 = sheet.createRow((short) k);
					k++;
					rowhead1.createCell(0).setCellValue(itemsid.get(i));
					rowhead1.createCell(1).setCellValue(items.get(i));
					rowhead1.createCell(2).setCellValue(unitprice);
					rowhead1.createCell(3).setCellValue(quantity);
					rowhead1.createCell(4).setCellValue(totalcost);
				}
				
			}
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
public void outdoor( HSSFSheet sheet,String dept) {
		
		HSSFRow rowhead0 = sheet.createRow((short) 0);
		
			rowhead0.createCell(0).setCellValue("Data Type : " );
			rowhead0.createCell(1).setCellValue(" "+dept);
			rowhead0.createCell(2).setCellValue("-----");
			rowhead0.createCell(3).setCellValue("Generated Date");
			rowhead0.createCell(4).setCellValue(""+DateFormatChange.StringToMysqlDate(new Date()));
			String[] st = null;
			if(dept.equals(type.get(0)))
			{
				try {
					st=ipdDetail();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				
			}
			
			
			
			rowhead0 = sheet.createRow((short) 4);
			rowhead0.createCell(0).setCellValue("Date ");
			rowhead0.createCell(1).setCellValue(" From : ");
			rowhead0.createCell(2).setCellValue(""+fromDateSTR);
			rowhead0.createCell(3).setCellValue(" To :");
			rowhead0.createCell(4).setCellValue(""+toDateSTR);
			
			rowhead0 = sheet.createRow((short) 5);
			rowhead0.createCell(0).setCellValue("Medicines ");
			rowhead0.createCell(1).setCellValue(" ");
		int k = 6;
		try {
			
			
		
			HSSFRow rowhead = sheet.createRow((short) 5);
			
			rowhead.createCell(0).setCellValue("Item ID");
			rowhead.createCell(1).setCellValue("Item name");
			rowhead.createCell(2).setCellValue("Unit Price");
			rowhead.createCell(3).setCellValue("Quantity");
			rowhead.createCell(4).setCellValue("Total Cost");
			
			
			for (int i = 0; i < itemsid.size(); i++) {
				
				departments(dept,itemsid.get(i));
				if(unitprice!=0)
				{

					HSSFRow rowhead1 = sheet.createRow((short) k);
					k++;
					rowhead1.createCell(0).setCellValue(itemsid.get(i));
					rowhead1.createCell(1).setCellValue(items.get(i));
					rowhead1.createCell(2).setCellValue(unitprice);
					rowhead1.createCell(3).setCellValue(quantity);
					rowhead1.createCell(4).setCellValue(totalcost);
				}
				
			}
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
public void procedureRegister( HSSFSheet sheet,String dept) {
		
		HSSFRow rowhead0 = sheet.createRow((short) 0);
		
			rowhead0.createCell(0).setCellValue("Data Type : " );
			rowhead0.createCell(1).setCellValue(" "+dept);
			rowhead0.createCell(2).setCellValue("-----");
			rowhead0.createCell(3).setCellValue("Generated Date");
			rowhead0.createCell(4).setCellValue(""+DateFormatChange.StringToMysqlDate(new Date()));
			String[] st = null;
			try {
				st=ipdDetail();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rowhead0 = sheet.createRow((short) 1);
			rowhead0.createCell(0).setCellValue("Total Patients : ");
			rowhead0.createCell(1).setCellValue(" "+st[0]);
			
			
			rowhead0 = sheet.createRow((short) 2);
			rowhead0.createCell(0).setCellValue("Total Days : ");
			rowhead0.createCell(1).setCellValue(" "+st[1]);
			
			rowhead0 = sheet.createRow((short) 3);
			rowhead0.createCell(0).setCellValue("Total Income : ");
			rowhead0.createCell(1).setCellValue(" "+st[2]);
			
			
			rowhead0 = sheet.createRow((short) 4);
			rowhead0.createCell(0).setCellValue("Date ");
			rowhead0.createCell(1).setCellValue(" From : ");
			rowhead0.createCell(2).setCellValue(""+fromDateSTR);
			rowhead0.createCell(3).setCellValue(" To :");
			rowhead0.createCell(4).setCellValue(""+toDateSTR);
			
			rowhead0 = sheet.createRow((short) 5);
			rowhead0.createCell(0).setCellValue("Procedures");
			rowhead0.createCell(1).setCellValue(" ");
		int k = 6;
		try {
			
			
		
			HSSFRow rowhead = sheet.createRow((short) 5);
			
			rowhead.createCell(0).setCellValue("Item name");
			rowhead.createCell(1).setCellValue("No");
			rowhead.createCell(2).setCellValue("Total Cost");
			
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllExpences(fromDateSTR, toDateSTR);
			try {
				while (rs.next()) {

					HSSFRow rowhead1 = sheet.createRow((short) k);
					k++;
//					quantity++;
//					totalcost = totalcost+Double
//							.parseDouble((rs.getObject(1).toString()));
					rowhead1.createCell(0).setCellValue(rs.getObject(1).toString());
					rowhead1.createCell(1).setCellValue(rs.getObject(2).toString());
					rowhead1.createCell(2).setCellValue(rs.getObject(3).toString());
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.closeConnection();
//			for (int i = 0; i < itemsCombo.size(); i++) {
//				
//				patientsExpenxes(itemsCombo.get(i));
//				
//					HSSFRow rowhead1 = sheet.createRow((short) k);
//					k++;
//					rowhead1.createCell(0).setCellValue(itemsCombo.get(i));
//					rowhead1.createCell(1).setCellValue(quantity);
//					rowhead1.createCell(2).setCellValue(totalcost);
//				
//				
//			}
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
	
	
	public void departments(String dept,String itemID)
	{
		unitprice=0;
		quantity=0;
		totalcost=0;
		Dept_PillsRegisterDBConnection dept_PillsRegisterDBConnection = new Dept_PillsRegisterDBConnection();
		ResultSet resultSet = dept_PillsRegisterDBConnection.retrievePillsToPatients(fromDateSTR, toDateSTR, dept, itemID);

		try {
			while (resultSet.next()) {

				unitprice = Double
						.parseDouble((resultSet.getObject(1).toString()));
				quantity = quantity+Double
						.parseDouble((resultSet.getObject(2).toString()));
				totalcost = totalcost+Double
						.parseDouble((resultSet.getObject(3).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dept_PillsRegisterDBConnection.closeConnection();
		mainStore(dept,itemID);
	}
	
	
	public void mainStore(String dept,String itemID)
	{
		IssuedItemsDBConnection issuedItemsDBConnection = new IssuedItemsDBConnection();
		ResultSet resultSet = issuedItemsDBConnection.retrievePillsToPatients(fromDateSTR, toDateSTR, dept, itemID);

		try {
			while (resultSet.next()) {

				unitprice = Double
						.parseDouble((resultSet.getObject(1).toString()));
				quantity = quantity+Double
						.parseDouble((resultSet.getObject(2).toString()));
				totalcost = totalcost+Double
						.parseDouble((resultSet.getObject(3).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		issuedItemsDBConnection.closeConnection();
	}
	

	public void patientsExpenxes(String itemID)
	{
		unitprice=0;
		quantity=0;
		totalcost=0;
		IPDExpensesDBConnection db = new IPDExpensesDBConnection();
		ResultSet rs = db.retrieveAllExpences(itemID, fromDateSTR, toDateSTR);
		try {
			while (rs.next()) {

				
				quantity++;
				totalcost = totalcost+Double
						.parseDouble((rs.getObject(1).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
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
	
	public String[] ipdDetail() throws Exception
	{
		
		String[] st=new String[3];
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveALLPatientsdays_amount(fromDateSTR,toDateSTR);
			int i=0;
			int days=0;
			double amount=0;
			while (rs.next()) {
				i++;
				days=days+Integer.parseInt(rs.getObject(2).toString());
				amount=amount+Double.parseDouble(rs.getObject(3).toString());
			}
			db.closeConnection();
			
			st[0]=i+"";
			st[1]=days+"";
			st[2]=amount+"";
			
			return st;
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