package hms.store.gui;

import hms.main.DateFormatChange;
import hms.reporttables.ExamsDoneReport;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JButton;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BatchStock extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JLabel itemNameLB;
	private JLabel itemIdLB;
	private JLabel itemBrandLB;
	private JLabel itemDescLB;
	private JLabel ItemLocationLB;

	String idIndex="";
	
	/**
	 * Create the dialog.
	 */
	public BatchStock(String itemID) {
		setTitle("Batch Stock Register");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				BatchStock.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 909, 485);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		idIndex=itemID;
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 144, 885, 296);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, },
						new String[] { "Batch Id", "Batch Name", "Batch Stock",
								"Expiry Date", "Last Used","Last User" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Batch Stock");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 302, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(BatchStock.class
					.getResource("/icons/stockicon.png")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		{
			JLabel lblNewLabel = new JLabel("Item ID :");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewLabel.setBounds(59, 39, 120, 14);
			contentPanel.add(lblNewLabel);
		}
		{
			itemIdLB = new JLabel("New label");
			itemIdLB.setFont(new Font("Tahoma", Font.BOLD, 15));
			itemIdLB.setBounds(189, 39, 240, 14);
			contentPanel.add(itemIdLB);
		}
		{
			JLabel lblItemName = new JLabel("Item Name :");
			lblItemName.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblItemName.setBounds(59, 66, 120, 14);
			contentPanel.add(lblItemName);
		}
		{
			itemNameLB = new JLabel("New label");
			itemNameLB.setFont(new Font("Tahoma", Font.BOLD, 15));
			itemNameLB.setBounds(189, 66, 240, 14);
			contentPanel.add(itemNameLB);
		}
		
		JLabel lblIte = new JLabel("Item Location :");
		lblIte.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblIte.setBounds(477, 41, 120, 14);
		contentPanel.add(lblIte);
		
		JLabel lblItemBrand = new JLabel("Item Brand :");
		lblItemBrand.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblItemBrand.setBounds(477, 66, 120, 14);
		contentPanel.add(lblItemBrand);
		
		ItemLocationLB = new JLabel("New label");
		ItemLocationLB.setFont(new Font("Tahoma", Font.BOLD, 15));
		ItemLocationLB.setBounds(607, 41, 275, 14);
		contentPanel.add(ItemLocationLB);
		
		itemBrandLB = new JLabel("New label");
		itemBrandLB.setFont(new Font("Tahoma", Font.BOLD, 15));
		itemBrandLB.setBounds(607, 66, 275, 14);
		contentPanel.add(itemBrandLB);
		{
			JLabel lblItemDesc = new JLabel("Item Desc.");
			lblItemDesc.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblItemDesc.setBounds(59, 91, 120, 14);
			contentPanel.add(lblItemDesc);
		}
		{
			itemDescLB = new JLabel("New label");
			itemDescLB.setFont(new Font("Tahoma", Font.BOLD, 15));
			itemDescLB.setBounds(189, 91, 240, 14);
			contentPanel.add(itemDescLB);
		}
		{
			JButton btnNewButton = new JButton("Excel File");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					JFileChooser fileChooser = new JFileChooser();
					fileChooser.setSelectedFile(new File("Excel_data.xls"));
					if (fileChooser.showSaveDialog(BatchStock.this) == JFileChooser.APPROVE_OPTION) {
					  File file = fileChooser.getSelectedFile();
					  // save to file
					  ReportExcel(table,file.toPath().toString());
					}
				}
			});
			btnNewButton.setIcon(new ImageIcon(BatchStock.class.getResource("/icons/1BL.PNG")));
			btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
			btnNewButton.setBounds(506, 106, 154, 34);
			contentPanel.add(btnNewButton);
		}
		getItemDetail(idIndex);
		populateExpensesTable();
	}
	public void ReportExcel(JTable table,String path){
		// TODO Auto-generated constructor stub
		try {
			String filename = path;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			HSSFRow row = sheet.createRow(1); 
			TableModel model = table.getModel(); 
			HSSFRow rowhead = sheet.createRow((short) 0);

			HSSFRow headerRow = sheet.createRow(0); //Create row at line 0
		    for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
		        headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
		    }

		    for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
		        for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
		            row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
		        }

		        //Set the row to the next one in the sequence 
		        row = sheet.createRow((rows + 2)); 
		    }
			
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
					.showMessageDialog(
							null,
							"Excel File Generated Successfully",
							"Data Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
	public void populateExpensesTable() {

		try {
			BatchTrackingDBConnection db = new BatchTrackingDBConnection();
			ResultSet rs = db.retrievetBatchTotalStock(idIndex);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrievetBatchTotalStock(idIndex);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {  "Batch Id", "Batch Name", "Batch Stock",
					"Expiry Date", "Last Used","Last User" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(150);
			table.getColumnModel().getColumn(4).setMinWidth(150);
			table.getColumnModel().getColumn(5).setPreferredWidth(150);
			table.getColumnModel().getColumn(5).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer2());
		} catch (SQLException ex) {
			
		}

	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column).equals("With in level")){
	              cellComponent.setBackground(Color.GREEN);
	          } else{
	        	  cellComponent.setBackground(Color.RED);
	        	 
	          }
	          return cellComponent;
	      }
	  }
	class CustomRenderer2 extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          try{
	        	 
	      		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	          	Date date1 = sdf.parse(DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),3)));
	          	Date date2 = sdf.parse(table.getValueAt(row, column).toString());
	   
	          	System.out.println(sdf.format(date1));
	          	System.out.println(sdf.format(date2));
	   
	          	if(date1.compareTo(date2)>0){
	          		System.out.println("Date1 is after Date2");
	          		 cellComponent.setBackground(Color.RED);
	          	}else if(date1.compareTo(date2)<0){
	          		System.out.println("Date1 is before Date2");
	          		 cellComponent.setBackground(Color.GREEN);
	          	}else if(date1.compareTo(date2)==0){
	          		System.out.println("Date1 is equal to Date2");
	          		 cellComponent.setBackground(Color.PINK);
	          	}else{
	          		System.out.println("How to get here?");
	          	}
	   
	      	}catch(ParseException ex){
	      		ex.printStackTrace();
	      	}
	          return cellComponent;
	      }
	  }
	public void getItemDetail(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail3(index);
		double mrp = 0;
		double purchase = 0,tot=0;
		try {
			while (resultSet.next()) {

				itemIdLB.setText(resultSet.getObject(1).toString());
				itemNameLB.setText(resultSet.getObject(2).toString());
				itemDescLB.setText(resultSet.getObject(3).toString());
				itemBrandLB.setText(resultSet.getObject(4).toString());
				ItemLocationLB.setText(resultSet.getObject(5).toString());
				
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}
}
