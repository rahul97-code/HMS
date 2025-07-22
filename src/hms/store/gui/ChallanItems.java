package hms.store.gui;

import hms.reporttables.InvoiceItemsRegisterReport;
import hms.store.database.ChallanDBConnection;
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
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ChallanItems extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnCancel;
	private JButton btnNewItem;
	int selectedRowIndex;
	private JTextField searchItemTF;
	Vector originalTableModel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ChallanItems dialog = new ChallanItems("","");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ChallanItems(final String check,String Challanno) {
		setTitle("Challan Items");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ChallanItems.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 650, 536);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 1035, 398);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, }, new String[] { "Item Id",
						"Item Name", "Item Desc.", "Expiry Date", "Item Stock",
						"Pack Size" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				table.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(ListSelectionEvent e) {
								// TODO Auto-generated method stub
								selectedRowIndex = table.getSelectedRow();
								selectedRowIndex = table
										.convertRowIndexToModel(selectedRowIndex);
								int selectedColumnIndex = table
										.getSelectedColumn();
//								btnEditItem.setEnabled(true);
//								btnViewBatch.setEnabled(true);
//								if (check.equals("admin")) {
//									btnDelete.setEnabled(true);
//								}

							}
						});
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
//						// TODO Auto-generated method stub
//						if (arg0.getClickCount() == 2) {
//							JTable target = (JTable) arg0.getSource();
//							int row = target.getSelectedRow();
//							int column = target.getSelectedColumn();
//							// do some action
//
//							Object selectedObject1 = table.getModel()
//									.getValueAt(selectedRowIndex, 0);
//							String id = selectedObject1.toString();
//							EditItem editItem = new EditItem(id);
//							editItem.setModal(true);
//							editItem.setVisible(true);
////							btnEditItem.setEnabled(false);
////							btnDelete.setEnabled(false);
////							btnViewBatch.setEnabled(true);
//						}
					}
				});

				scrollPane.setViewportView(table);
			}
		}

		btnNewItem = new JButton("Issue to Patient");
		btnNewItem.setIcon(new ImageIcon(ChallanItems.class
				.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem = new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);

			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnNewItem.setBounds(10, 449, 173, 44);
		contentPanel.add(btnNewItem);
		btnNewItem.setVisible(false);

		btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(ChallanItems.class
				.getResource("/icons/CANCEL.PNG")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(890, 449, 155, 44);
		contentPanel.add(btnCancel);

		searchItemTF = new JTextField();
		searchItemTF.setColumns(10);
		searchItemTF.setBounds(434, 10, 131, 20);
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
		label.setBounds(357, 10, 85, 20);
		contentPanel.add(label);

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(ChallanItems.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExcel.setBounds(732, 448, 155, 44);
		contentPanel.add(btnExcel);
		populateExpensesTable();
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
				headerRow.createCell(headings).setCellValue(
						model.getColumnName(headings));// Write column name
			}

			for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
																		// table
																		// row
				for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
																			// each
																			// table
																			// column
					row.createCell(cols).setCellValue(
							model.getValueAt(rows, cols).toString()); // Write
																		// value
				}

				// Set the row to the next one in the sequence
				row = sheet.createRow((rows + 2));
			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase()
						.contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	public void populateExpensesTable() {

		try {
			ChallanDBConnection db = new ChallanDBConnection();
			ResultSet rs = db.retrievetAllItems();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrievetAllItems();

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
					new String[] { " Item Id", "Item Name", " Remaining Stock in Challan"
							}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();

			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(2).setMinWidth(180);
		
		

			// table.getColumnModel().getColumn(9).setPreferredWidth(100);
			// table.getColumnModel().getColumn(9).setMinWidth(100);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			
		} catch (SQLException ex) {

		}

	}

	public class CustomRenderer1 extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);
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
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			Component cellComponent = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column);

			if (table.getValueAt(row, column).equals("Yes")) {
				cellComponent.setBackground(Color.GREEN);
			} else {
				cellComponent.setBackground(Color.RED);

			}
			return cellComponent;
		}
	}



	public JTextField getSearchItemTF() {
		return searchItemTF;
	}
}
