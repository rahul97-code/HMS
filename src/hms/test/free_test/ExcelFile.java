package hms.test.free_test;

import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;

public class ExcelFile {

	public ExcelFile(String name,JTable table) throws DocumentException, IOException {
		// TODO Auto-generated constructor stub
		

		try {
			File desktop = new File(System.getProperty("user.home"), "Desktop");
			String filename = desktop + "/" + ""+name + "-"
					+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Item Rate List");
			create( sheet,table);

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully on your Desktop with "+name+"-"
									+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void create( HSSFSheet sheet,JTable Table) {
		 DefaultTableModel model1  = (DefaultTableModel) Table.getModel();
		int k = 1;
		try {
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i <  model1.getColumnCount(); i++) {
				rowhead.createCell(i).setCellValue(model1.getColumnName(i).toString());
			}
	        for (int count = 0; count < model1.getRowCount(); count++){
	             
	            	HSSFRow rowhead1 = sheet.createRow((short) k);
	  				for (int i = 0; i <  model1.getColumnCount(); i++) {
	  					rowhead1.createCell(i).setCellValue(model1.getValueAt(count,i).toString());
	  				}
	  				k++;    
	        }
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
}
