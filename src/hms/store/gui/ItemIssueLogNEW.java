package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import java.awt.Toolkit;

public class ItemIssueLogNEW extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox departmentCB;
	private JTextField departmentTF;
	private JTextField searchItemTF;
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();

	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR = "%%", itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, tax = "0", expiryDateSTR = "",
			dueDateSTR = "", dateFrom, dateTo, previouseStock = "";
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, itemValue, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0;
	int quantity = 0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JDateChooser fromDate;
	private JDateChooser toDate;
	Vector originalTableModel;
	private JTextField textFieldTax;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ItemIssueLogNEW dialog = new ItemIssueLogNEW();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemIssueLogNEW() {
		setTitle("Issue Items From Store");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ItemIssueLogNEW.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 501);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		departmentCB = new JComboBox();
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					departmentNameSTR = departmentCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				getDepartmentsDetail(departmentNameSTR);
				departmentTF.setText("");
				searchItemTF.setText("");
				if (departmentName.getSize() > 0) {

					departmentTF.setText("" + departmentID);
				}
			}
		});
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(150, 11, 218, 25);
		contentPanel.add(departmentCB);

		JLabel lblCredit = new JLabel("Select Department");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 11, 126, 25);
		contentPanel.add(lblCredit);

		departmentTF = new JTextField();
		departmentTF.setEditable(false);
		departmentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentTF.setColumns(10);
		departmentTF.setBounds(150, 47, 218, 25);
		contentPanel.add(departmentTF);

		JLabel lblDebit = new JLabel("Department ID :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 47, 126, 25);
		contentPanel.add(lblDebit);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(121, 109, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
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

		JLabel lblSearch = new JLabel("Search");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(14, 112, 106, 14);
		contentPanel.add(lblSearch);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 149, 992, 315);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Intend Slip No", "Issued To", "Issued By", "Item Name", "Item Batch", "Expiry Date",
						"Issued Qty.", "Previouse Stock", "Date", "Time", "Mrp", "Purchase Price" }));
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(110);
		table.getColumnModel().getColumn(4).setMinWidth(110);
		table.getColumnModel().getColumn(5).setPreferredWidth(110);
		table.getColumnModel().getColumn(5).setMinWidth(110);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(130);
		table.getColumnModel().getColumn(7).setMinWidth(130);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(100);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int selectedRowIndex = table.getSelectedRow();
				selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);
				int selectedColumnIndex = table.getSelectedColumn();

			}
		});
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(14, 96, 1005, 2);
		contentPanel.add(separator);

		toDate = new JDateChooser();
		toDate.setDateFormatString("yyyy-MM-dd");
		toDate.setBounds(584, 47, 218, 25);
		contentPanel.add(toDate);
		toDate.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				if ("date".equals(arg0.getPropertyName())) {
					dateTo = DateFormatChange.StringToMysqlDate((Date) arg0.getNewValue());
				}
			}
		});
		toDate.setDate(new Date());
		JLabel lblToDate = new JLabel("To Date :");
		lblToDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblToDate.setBounds(428, 47, 126, 25);
		contentPanel.add(lblToDate);

		JLabel lblFromDate = new JLabel("From Date :");
		lblFromDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblFromDate.setBounds(428, 11, 126, 25);
		contentPanel.add(lblFromDate);

		fromDate = new JDateChooser();
		fromDate.setDateFormatString("yyyy-MM-dd");
		fromDate.setBounds(584, 11, 218, 25);
		contentPanel.add(fromDate);
		fromDate.getDateEditor().addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent arg0) {
				// TODO Auto-generated method stub
				if ("date".equals(arg0.getPropertyName())) {
					dateFrom = DateFormatChange.StringToMysqlDate((Date) arg0.getNewValue());
				}
			}
		});
		fromDate.setDate(new Date());

		JButton btnNewButton = new JButton("Get Data");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				loadDataToTable();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 17));
		btnNewButton.setBounds(824, 11, 162, 61);
		contentPanel.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Excel");
		btnNewButton_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("IssueFromStore_data.xls"));
				if (fileChooser.showSaveDialog(ItemIssueLogNEW.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnNewButton_1.setBounds(869, 106, 117, 25);
		contentPanel.add(btnNewButton_1);

		JLabel lblNew = new JLabel("NEW");
		lblNew.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblNew.setBounds(428, 111, 70, 15);
		contentPanel.add(lblNew);

		textFieldTax = new JTextField("0.0");
		textFieldTax.setFont(new Font("Dialog", Font.BOLD, 11));
		textFieldTax.setColumns(10);
		textFieldTax.setBounds(721, 109, 81, 20);
		contentPanel.add(textFieldTax);

		JLabel lblTax = new JLabel("Tax % :");
		lblTax.setBounds(662, 111, 57, 15);
		contentPanel.add(lblTax);
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
				if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	private void loadDataToTable() {
		// get size of the hashmap
		tax = textFieldTax.getText().toString();
		tax = tax.equals("") ? "0" : tax;
		try {
			IssuedItemsDBConnection db = new IssuedItemsDBConnection();
			ResultSet rs = db.retrieveAllIssuedRegister1(departmentID, tax, dateFrom, dateTo);

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
			double totalamount=0;
			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C) == null ? "..." : rs.getObject(C);	
				}
				totalamount+=rs.getDouble(13);
				R++;
			}
			System.out.println("Table rows: " + R);
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Intend Slip No", "Issued To", "Issued By", "Item Name", "Item Batch", "Expiry Date",
							"Issued Qty.", "Previouse Stock", "Date", "Time", "Mrp", "Purchase Price","Total Amount" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			String Row[]= {"---","---","---","---","---","---","---","---","---","---","---","total:",totalamount+""};
			model.addRow(Row);
			table.setModel(model);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(110);
			table.getColumnModel().getColumn(4).setMinWidth(110);
			table.getColumnModel().getColumn(5).setPreferredWidth(110);
			table.getColumnModel().getColumn(5).setMinWidth(110);
			table.getColumnModel().getColumn(6).setPreferredWidth(100);
			table.getColumnModel().getColumn(6).setMinWidth(100);
			table.getColumnModel().getColumn(7).setPreferredWidth(130);
			table.getColumnModel().getColumn(7).setMinWidth(130);
			table.getColumnModel().getColumn(8).setPreferredWidth(100);
			table.getColumnModel().getColumn(8).setMinWidth(100);
			table.getColumnModel().getColumn(9).setPreferredWidth(100);
			table.getColumnModel().getColumn(9).setMinWidth(100);

			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	public void ReportExcel(JTable table, String path) {
		// TODO Auto-generated constructor stub
		try {
			String filename = path;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			HSSFRow row = sheet.createRow(1);
			TableModel model = table.getModel();
			HSSFRow rowhead = sheet.createRow((short) 0);

			HSSFRow headerRow = sheet.createRow(0);  
			for (int headings = 0; headings < model.getColumnCount(); headings++) {
				headerRow.createCell(headings).setCellValue(model.getColumnName(headings));
			}

			for (int rows = 0; rows < model.getRowCount(); rows++) {
				for (int cols = 0; cols < table.getColumnCount(); cols++) { 
					row.createCell(cols).setCellValue(model.getValueAt(rows, cols)==null?"":model.getValueAt(rows, cols).toString()); 														
				}
				row = sheet.createRow((rows + 2));
			}
			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null, "Excel File Generated Successfully", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentID = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

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

	public JDateChooser getFromDate() {
		return fromDate;
	}

	public JDateChooser getToDate() {
		return toDate;
	}

	public JTable getTable() {
		return table;
	}
}
