package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.OPDSlippdf_new;
import hms.reporttables.ExamsDoneReport;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.NewIssuedForm;
import hms.store.gui.TotalStock.CustomRenderer;
import hms1.ipd.gui.IPDBrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import com.itextpdf.text.DocumentException;
import javax.swing.JButton;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.swing.JCheckBox;

public class DepartmentItemProfile extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField searchItemTF;
	Vector originalTableModel;
	private JComboBox departmentCB;
	String check="";
	String itemID="",ItemName="";
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	private JCheckBox chk_new;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DepartmentItemProfile dialog = new DepartmentItemProfile("1","1");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public DepartmentItemProfile(final String stockaccess,final String minimum_max_qty_access) {
		setTitle("Department Item Profile");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DepartmentItemProfile.class
						.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 951, 578);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 62, 925, 437);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.setToolTipText("Double Click To Edit");
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, },
						new String[] { "Item Id", "Item Name", "Item Desc.",
								"Expiry Date", "Item Stock" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				table.addMouseListener(new MouseListener() {

					@Override
					public void mouseReleased(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mousePressed(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseExited(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseEntered(MouseEvent arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void mouseClicked(MouseEvent arg0) {
						// TODO Auto-generated method stub
						if (arg0.getClickCount() == 2) {
							JTable target = (JTable) arg0.getSource();
							int row = target.getSelectedRow();
							int column = target.getSelectedColumn();
							// do some action
							Object itemID = table.getModel().getValueAt(row, 0);
							Object itemName = table.getModel().getValueAt(row,
									2);
							Object currentStock = table.getModel().getValueAt(row, 5);
							Object minQty = table.getModel().getValueAt(row, 6);
							Object maxQty = table.getModel().getValueAt(row, 7);
							Object ID = table.getModel().getValueAt(row, 9);

							Edititem edititem = new Edititem(
									DepartmentItemProfile.this, itemID
											.toString(), itemName.toString(),
											currentStock.toString(),minQty.toString(),  maxQty.toString(),
									departmentCB.getSelectedItem().toString(),stockaccess,minimum_max_qty_access,ID+"");
							edititem.setModal(true);
							edititem.setVisible(true);
						}
						if (arg0.getClickCount() == 1) {
							
							JTable target = (JTable) arg0.getSource();
							int row = target.getSelectedRow();
							int column = target.getSelectedColumn();
							// do some action
							itemID="";ItemName="";
							 itemID = table.getModel().getValueAt(row, 0)+"";
							 ItemName = table.getModel().getValueAt(row,
									2)+"";
							 if(flag(itemID)) {
									btnNewButton.setEnabled(true);}else {
										btnNewButton.setEnabled(false);
									}
							
						}
					}
				});
				scrollPane.setViewportView(table);
			}
		}
		{
			searchItemTF = new JTextField();
			searchItemTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
			searchItemTF.setBounds(592, 22, 207, 25);
			contentPanel.add(searchItemTF);
			searchItemTF.setColumns(10);
			searchItemTF.getDocument().addDocumentListener(
					new DocumentListener() {
						@Override
						public void insertUpdate(DocumentEvent e) {
							String str = searchItemTF.getText() + "";
							searchTableContents(str);
						}

						@Override
						public void removeUpdate(DocumentEvent e) {
							String str = searchItemTF.getText() + "";
							searchTableContents(str);
						}

						@Override
						public void changedUpdate(DocumentEvent e) {
							String str = searchItemTF.getText() + "";
							searchTableContents(str);
						}
					});
		}
		{
			JLabel lblSearch = new JLabel("Search");
			lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 14));
			lblSearch.setBounds(523, 22, 59, 25);
			contentPanel.add(lblSearch);
		}

		final JCheckBox chckbxNewCheckBox = new JCheckBox("Show Inactive Items");
		chckbxNewCheckBox.setEnabled(false);
		chckbxNewCheckBox.setBounds(372, 35, 144, 23);
		contentPanel.add(chckbxNewCheckBox);
		chckbxNewCheckBox.addActionListener(new ActionListener() {
	         public void actionPerformed(ActionEvent e) {
	        	 check="";
		            if (chckbxNewCheckBox.isSelected() && !chk_new.isSelected() ){
		            	System.out.println("clicked");
//		            	userid=Userid;
		            	check="0";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check );
		            }
		            else if(!chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(!chckbxNewCheckBox.isSelected() && !chk_new.isSelected()){
		            	check="0";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else{
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
	         }
	});
		departmentCB = new JComboBox();
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(146, 22, 218, 25);
		contentPanel.add(departmentCB);
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//searchItemTF.setText("");
				if (departmentName.getSize() > 0) {
					if (chckbxNewCheckBox.isSelected() && !chk_new.isSelected() ){
		            	System.out.println("clicked");
//		            	userid=Userid;
		            	check="0";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check );
		            }
		            else if(!chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(!chckbxNewCheckBox.isSelected() && !chk_new.isSelected()){
		            	check="0";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else{
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
				}
			}
		});

		JLabel label = new JLabel("Select Department");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(10, 22, 126, 25);
		contentPanel.add(label);
		
		  chk_new = new JCheckBox("New");
		  chk_new.setEnabled(false);
		  chk_new.setSelected(true);
		  chk_new.addActionListener(new ActionListener() {
		  	public void actionPerformed(ActionEvent e) {
		  		 check="";
		            if (chckbxNewCheckBox.isSelected() && !chk_new.isSelected() ){
		            	System.out.println("clicked");
//		            	userid=Userid;
		            	check="0";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTableInactive(departmentCB.getSelectedItem()
								.toString(),check );
		            }
		            else if(!chckbxNewCheckBox.isSelected() && chk_new.isSelected()) {
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else if(!chckbxNewCheckBox.isSelected() && !chk_new.isSelected()){
		            	check="0";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		            else{
		            	check="1";
		            	populateExpensesTable(departmentCB.getSelectedItem()
								.toString(),check);
		            }
		  	}
		  });
		chk_new.setBounds(372, 8, 65, 23);
		contentPanel.add(chk_new);
		
		btnNewButton = new JButton("View Batch");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DeptBatchBrowser ob=new DeptBatchBrowser(itemID,ItemName,departmentCB.getSelectedItem()+"");
				ob.setModal(true);
				ob.setVisible(true);
			}
		});
		btnNewButton.setBounds(682, 510, 117, 25);
		contentPanel.add(btnNewButton);
		
		JButton btnExportExcel = new JButton("Export Excel");
		btnExportExcel.setFont(new Font("Dialog", Font.BOLD, 11));
		btnExportExcel.setBounds(818, 511, 117, 25);
		contentPanel.add(btnExportExcel);
		
		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 populateExpensesTable(departmentCB.getSelectedItem()
							.toString(),check) ;
			}
		});
		btnRefresh.setBounds(818, 22, 117, 25);
		contentPanel.add(btnRefresh);
		getAllDepartments();
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				String str=column+"";
				if (str.toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
				
			}
		}
	}
	public void populateExpensesTableInactive(String departmentName,String check) {

		try {
			DepartmentStockDBConnection db = new DepartmentStockDBConnection();
			ResultSet rs = db.retrieveProfileInactive(departmentName,check);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveProfileInactive(departmentName,check);

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
					new String[] { "Item Id", "Item Code", "Item Name",
							"Item Desc.", "Expiry Date", "Current Stock",
							"Min. Qty", "Max. Qty","Status", }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};

			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(120);
			table.getColumnModel().getColumn(8).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(5)
			.setCellRenderer(new CustomRenderer());
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();

			// table.getColumnModel().getColumn(3).setCellRenderer(new
			// CustomRenderer2());
			searchTableContents(searchItemTF.getText());
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	public void populateExpensesTable(String departmentName,String check) {

		try {
			DepartmentStockDBConnection db = new DepartmentStockDBConnection();
			ResultSet rs = db.retrieveProfile(departmentName,check);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = C==6?Double.valueOf(Double.parseDouble(rs.getObject(C)+"0")).intValue():rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Item Id", "Item Code", "Item Name",
							"Item Desc.", "Expiry Date", "Current Stock",
							"Min. Qty", "Max. Qty","Status","Stock_ID" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};

			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(120);
			table.getColumnModel().getColumn(8).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(5)
			.setCellRenderer(new CustomRenderer());
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();

			// table.getColumnModel().getColumn(3).setCellRenderer(new
			// CustomRenderer2());
			searchTableContents(searchItemTF.getText());
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	public class CustomRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
			if (Double.parseDouble(table.getValueAt(row, column).toString())>Double.parseDouble(table.getValueAt(row, 6).toString())) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.RED);
			}
			return cellComponent;
		}
	}
	class CustomRenderer2 extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Date date1 = sdf.parse(DateFormatChange
						.StringToMysqlDate(DateFormatChange.addMonths(
								new Date(), 3)));
				Date date2 = sdf
						.parse(table.getValueAt(row, column).toString());

				if (date1.compareTo(date2) > 0) {
					cellComponent.setBackground(Color.RED);
				} else if (date1.compareTo(date2) < 0) {
					cellComponent.setBackground(Color.GREEN);
				} else if (date1.compareTo(date2) == 0) {
					cellComponent.setBackground(Color.PINK);
				} else {
				}
			} catch (ParseException ex) {
				ex.printStackTrace();
			}
			return cellComponent;
		}
	}
	

	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}
	private boolean flag(String itemID) {
		// TODO Auto-generated method stub
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet rs=itemsDBConnection.CheckBatchReqd(itemID);
		boolean i=false;
		try {
			while(rs.next()) {
				i=rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
	    return i;
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
		        	String str=model.getValueAt(rows, cols)+"";
		            row.createCell(cols).setCellValue(str); //Write value
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
}
