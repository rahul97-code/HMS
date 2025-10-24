
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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.JTextField;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.toedter.calendar.JDateChooser;

import javax.swing.JComboBox;
import javax.swing.border.TitledBorder;
import javax.swing.JRadioButton;
import java.awt.Button;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import java.awt.Choice;

public class InsuranceTodoEntry extends JDialog {

	private final JPanel contentPanel = new JPanel();
	TableRowSorter<TableModel> rowSorter;
	private JTable table;
	private JTable todoMasterTable;
	private JButton btnCancel;
	int selectedRowIndex;
	private JTextField searchItemTF;
	ButtonGroup Btngroup = new ButtonGroup();
	Vector originalTableModel;
	private JComboBox insCB;
	final DefaultComboBoxModel insModel = new DefaultComboBoxModel();
	private JButton btnExcel;
	private JButton button = new JButton();
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_9;
	private JTextField textField_11;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
//			StoreMain.access="1";
//			StoreMain.update_item_access="1";
			InsuranceTodoEntry dialog = new InsuranceTodoEntry("rajinder");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsuranceTodoEntry(final String check) {

		setTitle("Insurance Todo Patient");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(InsuranceTodoEntry.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1070, 593);
		getContentPane().setLayout(new BorderLayout());
		contentPanel
				.setBorder(new TitledBorder(null, "Insurance Todo Patient", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 70, 510, 199);
			contentPanel.add(scrollPane);
			{
				
//			 task_type, ins_type, ins_id, frequency, created_by, created_by_id, choice_set1, choice_set2, choice_set3, created_at, updated_at
//				FROM hospital_db.ins_todo_master
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(
						new DefaultTableModel(new Object[][] { { null, null, null, null}, }, new String[] {
								"ID","TASK", "TYPE", "FREQUENCY"}));
//				table.getColumnModel().getColumn(1).setPreferredWidth(180);
//				table.getColumnModel().getColumn(1).setMinWidth(150);
//				table.getColumnModel().getColumn(2).setPreferredWidth(180);
//				table.getColumnModel().getColumn(2).setMinWidth(150);
//				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.setFont(new Font("Tahoma", Font.PLAIN, 12));
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

		btnCancel = new JButton("Cancel");
		btnCancel.setIcon(new ImageIcon(InsuranceTodoEntry.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnCancel.setBounds(900, 518, 155, 32);
		contentPanel.add(btnCancel);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRefresh.setBounds(742, 474, 155, 32);
		contentPanel.add(btnRefresh);

		btnExcel = new JButton("Excel");
		btnExcel.setEnabled(false);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(InsuranceTodoEntry.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnExcel.setBounds(742, 517, 155, 32);
		contentPanel.add(btnExcel);
		
		
//		SELECT todo_id,, task_type, upload_path, created_by, created_by_id, created_at, updated_at, is_completed
//		FROM hospital_db.ins_todo_entry;
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 319, 718, 226);
		contentPanel.add(scrollPane);
		todoMasterTable = new JTable();
		todoMasterTable.getTableHeader().setReorderingAllowed(false);
		todoMasterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		todoMasterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		todoMasterTable.setModel(
				new DefaultTableModel(new Object[][] { { null, null, null, null, null }, }, new String[] {
						"TODO ID","TASK","TYPE","ENTRY DATE","DUE DATE", "PRIORITY", "DONE" }));
		todoMasterTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		todoMasterTable.getColumnModel().getColumn(1).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		todoMasterTable.getColumnModel().getColumn(2).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(3).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(4).setPreferredWidth(150);
		todoMasterTable.getColumnModel().getColumn(4).setMinWidth(100);
		todoMasterTable.setFont(new Font("Tahoma", Font.BOLD, 14));
		todoMasterTable.addMouseListener(new MouseListener() {

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
					String id = String.valueOf(todoMasterTable.getModel().getValueAt(
							todoMasterTable.getRowSorter().convertRowIndexToModel(todoMasterTable.getSelectedRow()), 0));
							
				}
				if (arg0.getClickCount() == 1) {
					int column = todoMasterTable.getSelectedColumn();
					if (column == 9) {
	
					}

				}
			}
		});

		scrollPane.setViewportView(todoMasterTable);
		
		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager
						.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
		panel_7.setBounds(742, 25, 313, 86);
		contentPanel.add(panel_7);
		
		
				insCB = new JComboBox();
				insCB.setBounds(159, 49, 128, 24);
				panel_7.add(insCB);
				
						JLabel lblReplaceWith = new JLabel("Insurance");
						lblReplaceWith.setBounds(70, 54, 217, 15);
						panel_7.add(lblReplaceWith);
										{
											JLabel label_2 = new JLabel("");
											label_2.setBounds(22, 8, 39, 32);
											panel_7.add(label_2);
											label_2.setIcon(new ImageIcon(InsuranceTodoEntry.class.getResource("/icons/restore.gif")));
										}
				insCB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						String ins=insCB.getSelectedItem().toString();
						populateExpensesTable(ins);
					}
				});
		
		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager
						.getBorder("TitledBorder.border"), "Patient Detail",
						TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
								Font.PLAIN, 12), null));
		panel.setBounds(742, 117, 313, 210);
		contentPanel.add(panel);
		
		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPatientName.setBounds(6, 66, 108, 14);
		panel.add(lblPatientName);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_4.setEditable(false);
		textField_4.setColumns(10);
		textField_4.setBounds(106, 61, 201, 25);
		panel.add(textField_4);
		
		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNote.setBounds(6, 136, 108, 26);
		panel.add(lblNote);
		
		textField_9 = new JTextField();
		textField_9.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_9.setEditable(false);
		textField_9.setColumns(10);
		textField_9.setBounds(106, 99, 201, 25);
		panel.add(textField_9);
		
		textField_11 = new JTextField();
		textField_11.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_11.setEditable(false);
		textField_11.setColumns(10);
		textField_11.setBounds(106, 136, 201, 25);
		panel.add(textField_11);
		
		JLabel claim_idlbl_1 = new JLabel("P Id:");
		claim_idlbl_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		claim_idlbl_1.setBounds(6, 104, 93, 15);
		panel.add(claim_idlbl_1);
		
		textField_3 = new JTextField();
		textField_3.setBounds(106, 24, 201, 25);
		panel.add(textField_3);
		textField_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField_3.setEditable(false);
		textField_3.setColumns(10);
		
		JLabel lblOpdNo = new JLabel("IPD No :");
		lblOpdNo.setBounds(6, 29, 108, 14);
		panel.add(lblOpdNo);
		lblOpdNo.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 12));
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(106, 174, 201, 25);
		panel.add(textField);
		
		JLabel claim_idlbl_1_1 = new JLabel("Ins Category :");
		claim_idlbl_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		claim_idlbl_1_1.setBounds(6, 179, 93, 15);
		panel.add(claim_idlbl_1_1);
		
				searchItemTF = new JTextField();
				searchItemTF.setBounds(123, 34, 138, 24);
				contentPanel.add(searchItemTF);
				searchItemTF.setColumns(10);
				
						JLabel label = new JLabel("Search");
						label.setBounds(30, 34, 231, 20);
						contentPanel.add(label);
						
						JRadioButton radioButton = new JRadioButton("");
						radioButton.setBounds(482, 34, 28, 23);
						contentPanel.add(radioButton);
						
						JRadioButton radioButton_1 = new JRadioButton("");
						radioButton_1.setBounds(438, 34, 28, 23);
						contentPanel.add(radioButton_1);
						
						JRadioButton radioButton_2 = new JRadioButton("");
						radioButton_2.setBounds(393, 34, 28, 23);
						contentPanel.add(radioButton_2);
						
						JLabel lblChoices = new JLabel("Choices :");
						lblChoices.setFont(new Font("Dialog", Font.PLAIN, 12));
						lblChoices.setBounds(303, 38, 108, 14);
						contentPanel.add(lblChoices);
						
						JButton btnRefresh_1 = new JButton("Add");
						btnRefresh_1.setFont(new Font("Dialog", Font.BOLD, 14));
						btnRefresh_1.setBounds(42, 281, 155, 24);
						contentPanel.add(btnRefresh_1);
						
						JButton btnRefresh_2 = new JButton("Remove");
						btnRefresh_2.setFont(new Font("Dialog", Font.BOLD, 14));
						btnRefresh_2.setBounds(213, 281, 155, 24);
						contentPanel.add(btnRefresh_2);
						
						JLabel label_1_1 = new JLabel("");
						label_1_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
						label_1_1.setBounds(12, 275, 641, 37);
						contentPanel.add(label_1_1);
						
								JLabel label_1 = new JLabel("");
								label_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
								label_1.setBounds(12, 25, 510, 37);
								contentPanel.add(label_1);
								
								textField_1 = new JTextField();
								textField_1.setColumns(10);
								textField_1.setBounds(489, 283, 138, 24);
								contentPanel.add(textField_1);
								
								JLabel label_2 = new JLabel("Search");
								label_2.setBounds(393, 287, 231, 20);
								contentPanel.add(label_2);
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
	        InsuranceDBConnection DoctorDBConnection = new InsuranceDBConnection();
	        ResultSet rs = DoctorDBConnection.retrieveAllTasks(ins);

	        int NumberOfColumns = rs.getMetaData().getColumnCount();
	        int NumberOfRows = 0;

	        while (rs.next()) NumberOfRows++;
	        rs.beforeFirst();

	        Object[][] Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns]; // +1 for DATE column
	        int R = 0;
	        while (rs.next()) {
	            for (int C = 1; C <= NumberOfColumns; C++) {
	                Rows_Object_Array[R][C - 1] = rs.getObject(C);
	            }
	            R++;
	        }

	        DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[]{"ID", "TASK", "TYPE", "FREQUENCY", "DATE", "PRIORITY"}) {
		            @Override
		            public boolean isCellEditable(int row, int column) {
		                return true;
		            }
		        };
	        table.setModel(model);
	        
	        table.getColumn("DATE").setCellEditor(new DateChooserEditor());
	        JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});
	        table.getColumn("PRIORITY").setCellEditor(new DefaultCellEditor(priorityCombo));

	        rowSorter = new TableRowSorter<>(table.getModel());
	        table.setRowSorter(rowSorter);
	        table.setFont(new Font("Tahoma", Font.PLAIN, 12));
	        autoResizeColumns(table);

	    } catch (SQLException ex) {
	        ex.printStackTrace();
	    }
	}

	class DateChooserEditor extends AbstractCellEditor implements TableCellEditor {
	    private final JDateChooser dateChooser;
	    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	    public DateChooserEditor() {
	        dateChooser = new JDateChooser();
	        dateChooser.setDateFormatString("yyyy-MM-dd"); // display format
	    }

	    @Override
	    public Object getCellEditorValue() {
	        Date selectedDate = dateChooser.getDate();
	        if (selectedDate != null) {
	            return dateFormat.format(selectedDate); // returns formatted String
	        }
	        return null;
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value,
	                                                 boolean isSelected, int row, int column) {
	        if (value != null) {
	            try {
	                if (value instanceof String)
	                    dateChooser.setDate(dateFormat.parse((String) value));
	                else if (value instanceof Date)
	                    dateChooser.setDate((Date) value);
	            } catch (Exception e) {
	                dateChooser.setDate(null);
	            }
	        } else {
	            dateChooser.setDate(null);
	        }
	        return dateChooser;
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
	public static void autoResizeColumns(JTable table) {
	    final TableColumnModel columnModel = table.getColumnModel();

	    for (int column = 0; column < table.getColumnCount(); column++) {
	        int width = 50; // Minimum width
	        TableColumn tableColumn = columnModel.getColumn(column);

	        // --- Measure header width ---
	        TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
	        Component headerComp = headerRenderer.getTableCellRendererComponent(
	                table, tableColumn.getHeaderValue(), false, false, 0, column);
	        width = Math.max(width, headerComp.getPreferredSize().width);

	        // --- Measure cell content width ---
	        for (int row = 0; row < table.getRowCount(); row++) {
	            TableCellRenderer cellRenderer = table.getCellRenderer(row, column);
	            Component c = table.prepareRenderer(cellRenderer, row, column);
	            width = Math.max(width, c.getPreferredSize().width + 10); // add padding
	        }

	        if (width > 300) width = 300; // Max width limit (optional)
	        columnModel.getColumn(column).setPreferredWidth(width);
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