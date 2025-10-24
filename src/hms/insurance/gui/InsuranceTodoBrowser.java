
package hms.insurance.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.reporttables.InvoiceItemsRegisterReport;
import hms.store.database.ItemsDBConnection;
import hms1.ipd.database.IPDDBConnection;

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

public class InsuranceTodoBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	TableRowSorter<TableModel> rowSorter;
	private JTable table;
	private JButton btnCancel;
	int selectedRowIndex;
	private JTextField searchItemTF;
	ButtonGroup Btngroup = new ButtonGroup();
	Vector originalTableModel;
	private JComboBox insCB;
	final DefaultComboBoxModel insModel = new DefaultComboBoxModel();
	private JButton btnExcel;
	private JButton button = new JButton();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			StoreMain.access="1";
//			StoreMain.update_item_access="1";
			InsuranceTodoBrowser dialog = new InsuranceTodoBrowser("rajinder");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsuranceTodoBrowser(final String check) {

		setTitle("Insurance Todo Patient");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(InsuranceTodoBrowser.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1070, 593);
		getContentPane().setLayout(new BorderLayout());
		contentPanel
				.setBorder(new TitledBorder(null, "Insurance Todo Patient", TitledBorder.LEADING, TitledBorder.TOP, null, null));
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
								"IPD ID","P ID", "P NAME", "WARD", "ENTRY DATE", "INSURANCE", "TASKS" }));
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
									
						}
						if (arg0.getClickCount() == 1) {
							int column = table.getSelectedColumn();
							if (column == 9) {
			
							}

						}
					}
				});

				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(InsuranceTodoBrowser.class.getResource("/icons/restore.gif")));
			label.setBounds(20, 22, 38, 32);
			contentPanel.add(label);
		}

		btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(InsuranceTodoBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(900, 506, 155, 44);
		contentPanel.add(btnCancel);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

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

		JLabel label = new JLabel("Search");
		label.setBounds(76, 22, 85, 20);
		contentPanel.add(label);

		btnExcel = new JButton("Excel");
		btnExcel.setEnabled(false);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(InsuranceTodoBrowser.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExcel.setBounds(742, 505, 155, 44);
		contentPanel.add(btnExcel);


		insCB = new JComboBox();
		insCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ins=insCB.getSelectedItem().toString();
				populateExpensesTable(ins);
			}
		});
		insCB.setBounds(184, 64, 161, 24);
		contentPanel.add(insCB);

		JLabel lblReplaceWith = new JLabel("Insurance");
		lblReplaceWith.setBounds(86, 69, 107, 15);
		contentPanel.add(lblReplaceWith);

		JLabel label_1 = new JLabel("");
		label_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		label_1.setBounds(59, 55, 299, 44);
		contentPanel.add(label_1);

		getDoctorDetails();

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

		InsuranceDBConnection DoctorDBConnection = new InsuranceDBConnection();
		ResultSet resultSet = DoctorDBConnection.retrieveAllData();
		insModel.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				insModel.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DoctorDBConnection.closeConnection();
		insCB.setModel(insModel);

		if (i > 0) {
			insCB.setSelectedIndex(0);
		}
	}

	public void populateExpensesTable(String ins) {

		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.getAllToDoIPDPatients(ins);

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

			TableModel model = new EditableTableModel_ItemBroswer(new String[] {
					"IPD ID","P ID", "P NAME", "WARD", "ENTRY DATE", "INSURANCE", "TASKS" },
					Rows_Object_Array) {

			};
			table.setModel(model);
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

			rowSorter = new TableRowSorter<>(table.getModel());
			table.setRowSorter(rowSorter);
			// table.getColumnModel().getColumn(9).setPreferredWidth(100);
			// table.getColumnModel().getColumn(9).setMinWidth(100);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			table.getColumnModel().getColumn(5).setCellRenderer(new CustomRenderer());
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