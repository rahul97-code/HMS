
package hms.store.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.reporttables.InvoiceItemsRegisterReport;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.RowSorterEvent;
import javax.swing.event.RowSorterListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import java.awt.Button;
import javax.swing.SwingConstants;

public class ItemBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	TableRowSorter<TableModel> rowSorter;
	private JTable table;
	private JButton btnCancel;
	private JButton btnNewItem;
	private JButton btnEditItem;
	int selectedRowIndex;
	private JButton btnDelete;
	private JTextField searchItemTF;
	ButtonGroup Btngroup = new ButtonGroup();
	Vector originalTableModel;
	JButton btnViewBatch;
	private JComboBox comboBox_from;
	private JComboBox comboBox_to;
	final DefaultComboBoxModel itemName_from = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName_to = new DefaultComboBoxModel();
	private JButton btnUpdate;
	private JRadioButton rdbtnAll;
	private JRadioButton rdbtnStore;
	private JRadioButton rdbtnStore_1;
	private JLabel lblSelectStore;
	private JButton btnExcel;
	private JButton button = new JButton();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			StoreMain.access="1";
//			StoreMain.update_item_access="1";
			ItemBrowser dialog = new ItemBrowser("rajinder");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ItemBrowser(final String check) {

		setTitle("Items Browser");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ItemBrowser.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1070, 593);
		getContentPane().setLayout(new BorderLayout());
		contentPanel
				.setBorder(new TitledBorder(null, "Item Browser", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(20, 111, 1035, 383);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(
						new DefaultTableModel(new Object[][] { { null, null, null, null, null }, }, new String[] {
								"Item Id", "Item Name", "Item Desc.", "Expiry Date", "Item Stock", "Pack Size" }));
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
							String id = String.valueOf(table.getModel().getValueAt(
									table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 0));
							EditItem editItem = new EditItem(id);
							editItem.setModal(true);
							editItem.setVisible(true);
							btnEditItem.setEnabled(false);
							btnDelete.setEnabled(false);
							btnViewBatch.setEnabled(true);
						}
						if (arg0.getClickCount() == 1) {
							btnEditItem.setEnabled(true);
							btnViewBatch.setEnabled(true);
							if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
								btnDelete.setEnabled(true);
								btnUpdate.setEnabled(true);
							}

							int column = table.getSelectedColumn();
							if (column == 9) {
								Object selectedObject1 = String.valueOf(table.getModel().getValueAt(
										table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 9));
								Object itemID = String.valueOf(table.getModel().getValueAt(
										table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 0));
								ItemsDBConnection db = new ItemsDBConnection();
								try {
									db.UpadateBatchStatus(Boolean.parseBoolean(selectedObject1 + "") ? 1 : 0,
											itemID + "");
								} catch (Exception e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								}
								db.closeConnection();
							}

						}
					}
				});

				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/restore.gif")));
			label.setBounds(20, 22, 38, 32);
			contentPanel.add(label);
		}

		btnNewItem = new JButton("New Item");
		btnNewItem.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem = new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);

			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewItem.setBounds(20, 506, 136, 44);
		contentPanel.add(btnNewItem);

		btnEditItem = new JButton("Edit Item");
		btnEditItem.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/edit_button.png")));
		btnEditItem.setEnabled(false);
		btnEditItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String id = String.valueOf(table.getModel()
						.getValueAt(table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 0));
				EditItem editItem = new EditItem(id);
				editItem.setModal(true);
				editItem.setVisible(true);
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		btnEditItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnEditItem.setBounds(160, 505, 144, 46);
		contentPanel.add(btnEditItem);

		btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(900, 506, 155, 44);
		contentPanel.add(btnCancel);

		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(ItemBrowser.this, "Are you sure to delete item",
						"Cancel", dialogButton);
				if (dialogResult == 0) {
					String id = String.valueOf(table.getModel()
							.getValueAt(table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 0));
					ItemsDBConnection connection = new ItemsDBConnection();
					try {
						connection.deleteRow(id);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					connection.closeConnection();

					populateExpensesTable("", "");

				}
				btnDelete.setEnabled(false);
				btnEditItem.setEnabled(false);
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnDelete.setBounds(306, 505, 136, 44);
		contentPanel.add(btnDelete);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				populateExpensesTable("", "");
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRefresh.setBounds(582, 505, 155, 44);
		contentPanel.add(btnRefresh);

		searchItemTF = new JTextField();
		searchItemTF.setColumns(10);
		searchItemTF.setBounds(169, 22, 189, 24);
		contentPanel.add(searchItemTF);
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

		JLabel label = new JLabel("Search Item");
		label.setBounds(76, 22, 85, 20);
		contentPanel.add(label);

		btnExcel = new JButton("Excel");
		btnExcel.setEnabled(false);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(ItemBrowser.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExcel.setBounds(742, 505, 155, 44);
		contentPanel.add(btnExcel);
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			btnExcel.setEnabled(true);
		}

		btnViewBatch = new JButton("View Batch");
		btnViewBatch.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnViewBatch.setBounds(444, 506, 136, 44);
		btnViewBatch.setEnabled(false);
		btnViewBatch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String id = String.valueOf(table.getModel()
						.getValueAt(table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 0));
				String name = String.valueOf(table.getModel()
						.getValueAt(table.getRowSorter().convertRowIndexToModel(table.getSelectedRow()), 1));
				BatchBrowser editItem = new BatchBrowser(id, name);
				editItem.setModal(true);
				editItem.setVisible(true);
				btnEditItem.setEnabled(false);
				btnDelete.setEnabled(false);
			}
		});
		contentPanel.add(btnViewBatch);

		comboBox_to = new JComboBox();
		comboBox_to.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		comboBox_to.setBounds(875, 31, 161, 24);
		contentPanel.add(comboBox_to);

		comboBox_from = new JComboBox();
		comboBox_from.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		comboBox_from.setBounds(582, 31, 161, 24);
		contentPanel.add(comboBox_from);

		btnUpdate = new JButton("update");
		btnUpdate.setEnabled(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String from, to;
				from = comboBox_from.getSelectedItem().toString();
				to = comboBox_to.getSelectedItem().toString();
				ItemsDBConnection db = new ItemsDBConnection();
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(ItemBrowser.this, "Are you sure to change doctor!",
						"Cancel", dialogButton);
				if (dialogResult == 0) {
					try {
						db.updateDoctor(to, from);
						populateExpensesTable("", "");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				db.closeConnection();
			}
		});
		btnUpdate.setBounds(769, 31, 85, 25);
		contentPanel.add(btnUpdate);

		JLabel lblFind = new JLabel("find Doctor");
		lblFind.setBounds(628, 67, 90, 15);
		contentPanel.add(lblFind);

		JLabel lblReplaceWith = new JLabel("Replace With");
		lblReplaceWith.setBounds(903, 67, 107, 15);
		contentPanel.add(lblReplaceWith);

		JLabel label_1 = new JLabel("");
		label_1.setBorder(new TitledBorder(null, "Change Doctor", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		label_1.setBounds(557, 12, 498, 87);
		contentPanel.add(label_1);

		rdbtnAll = new JRadioButton("All");
		rdbtnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateExpensesTable("", "");
			}
		});
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnAll.setBounds(169, 67, 49, 23);
		Btngroup.add(rdbtnAll);
		contentPanel.add(rdbtnAll);

		rdbtnStore = new JRadioButton("Store-1");
		rdbtnStore.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateExpensesTable("st", "1");
			}
		});
		rdbtnStore.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnStore.setBounds(215, 67, 73, 23);
		Btngroup.add(rdbtnStore);
		contentPanel.add(rdbtnStore);

		rdbtnStore_1 = new JRadioButton("Store-2");
		rdbtnStore_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateExpensesTable("st", "2");
			}
		});
		rdbtnStore_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnStore_1.setBounds(292, 67, 90, 23);
		Btngroup.add(rdbtnStore_1);
		contentPanel.add(rdbtnStore_1);

		lblSelectStore = new JLabel("Select Store :");
		lblSelectStore.setBounds(53, 64, 103, 24);
		contentPanel.add(lblSelectStore);

		button = new JButton("OFF");
		button.setEnabled(false);
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String i = button.getText().equals("OFF") ? "1" : "0";
				ItemsDBConnection db = new ItemsDBConnection();

				db.UpdateReturnCharge(i);

				getReturnChargeOnOff();

			}
		});
		button.setBounds(473, 22, 73, 25);
		contentPanel.add(button);
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			button.setEnabled(true);
		}

		JLabel lblReturn = new JLabel("Return");
		lblReturn.setHorizontalAlignment(SwingConstants.CENTER);
		lblReturn.setBounds(483, 53, 56, 15);
		contentPanel.add(lblReturn);

		JLabel lblCharge = new JLabel(" Charge");
		lblCharge.setHorizontalAlignment(SwingConstants.CENTER);
		lblCharge.setBounds(463, 64, 90, 20);
		contentPanel.add(lblCharge);
		populateExpensesTable("", "");
		getDoctorDetails();
		getReturnChargeOnOff();
	}

	public void getReturnChargeOnOff() {
		ItemsDBConnection db = new ItemsDBConnection();
		ResultSet rs = db.getReturnCharge();
		try {
			while (rs.next()) {
				if (rs.getBoolean(1)) {
					button.setText("ON");
					button.setBackground(Color.green);
					button.setForeground(Color.black);
				} else {
					button.setText("OFF");
					button.setBackground(Color.red);
					button.setForeground(Color.black);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
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

			HSSFRow headerRow = sheet.createRow(0); // Create row at line 0
			for (int headings = 0; headings < model.getColumnCount(); headings++) { // For
				// each
				// column
				headerRow.createCell(headings).setCellValue(model.getColumnName(headings));// Write column name
			}

			for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
				// table
				// row
				for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
					// each
					// table
					// column
					row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); // Write
					// value
				}

				// Set the row to the next one in the sequence
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

	public void searchTableContents(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
		}
	}

	public void getDoctorDetails() {

		DoctorDBConnection DoctorDBConnection = new DoctorDBConnection();
		ResultSet resultSet = DoctorDBConnection.getDocter();
		itemName_from.removeAllElements();
		itemName_to.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemName_from.addElement(resultSet.getObject(1).toString());
				itemName_to.addElement(resultSet.getObject(1).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DoctorDBConnection.closeConnection();
		comboBox_from.setModel(itemName_from);
		comboBox_to.setModel(itemName_to);

		if (i > 0) {
			comboBox_from.setSelectedIndex(0);
			comboBox_to.setSelectedIndex(0);
		}
	}

	public void populateExpensesTable(String storetype, String str) {

		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllItems(storetype, str);

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
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table

			TableModel model = new EditableTableModel_ItemBroswer(new String[] { "Item Id", "Item Name", "Doctor Name",
					"purchase Price", "Selling Price", "Active", "Pack Size", "Item Type", "MRP", "Batch Reqd" },
					Rows_Object_Array) {

			};
			TableModel model1 = new EditableTableModel_ItemBroswer(new String[] { "Item Id", "Item Name", "Doctor Name",
					"purchase Price", "Selling Price", "Active", "Pack Size", "Item Type", "MRP" }, Rows_Object_Array) {

			};
			if (StoreMain.update_item_access.equals("1") || StoreMain.access.equals("1")) {
				table.setModel(model);
			} else {
				table.setModel(model1);
			}
			table.getColumnModel().getColumn(1).setPreferredWidth(90);
			table.getColumnModel().getColumn(1).setMinWidth(90);
			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(80);
			table.getColumnModel().getColumn(5).setMinWidth(80);
			table.getColumnModel().getColumn(6).setPreferredWidth(80);
			table.getColumnModel().getColumn(6).setMinWidth(80);
			table.getColumnModel().getColumn(7).setPreferredWidth(80);
			table.getColumnModel().getColumn(7).setMinWidth(80);
			rowSorter = new TableRowSorter<>(table.getModel());
			table.setRowSorter(rowSorter);
			// table.getColumnModel().getColumn(9).setPreferredWidth(100);
			// table.getColumnModel().getColumn(9).setMinWidth(100);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer());
			table.getColumnModel().getColumn(7).setCellRenderer(new CustomRenderer1());
		} catch (SQLException ex) {

		}

	}

	public class CustomRenderer1 extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);
			System.out.println(table.getValueAt(row, column));
			if (table.getValueAt(row, column).equals("High Risk")) {
				cellComponent.setBackground(Color.RED);
			} else if (table.getValueAt(row, column).equals("SHC-H1")) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.WHITE);
			}
			return cellComponent;
		}
	}

	public class CustomRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row,
					column);

			if (table.getValueAt(row, column).equals("Yes")) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.RED);

			}
			return cellComponent;
		}
	}

	public JButton getBtnDelete() {
		return btnDelete;
	}

	public JTextField getSearchItemTF() {
		return searchItemTF;
	}

	class EditableTableModel_ItemBroswer extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel_ItemBroswer(String[] columnTitles, Object[][] dataEntries) {
			this.columnTitles = columnTitles;
			this.dataEntries = dataEntries;
		}

		public int getRowCount() {
			return dataEntries.length;
		}

		public int getColumnCount() {
			return columnTitles.length;
		}

		public Object getValueAt(int row, int column) {
			return dataEntries[row][column];
		}

		public String getColumnName(int column) {
			return columnTitles[column];
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(int row, int column) {
			if (column == 9) {
				return true;
			} else {
				return false;
			}

		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
}