
package hms.insurance.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.InvoiceItemsRegisterReport;
import hms.store.database.ItemsDBConnection;
import hms1.ipd.database.IPDDBConnection;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;

import javax.swing.AbstractCellEditor;
import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
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
	TableRowSorter<TableModel> rowSorter1;
	private JTable table;
	Vector files = new Vector();
	Vector filesPath = new Vector();
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
	private JTextField ipdTF;
	private JTextField pNameTF;
	private JTextField pIdTF;
	private String[] open=new String[5];
	private JTextField insTF;
	private JTextField insCatTF;
	private JTextField searchTodoListTF;
	private String reg_no;
	private JList list;
	private String mainDir;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			//			StoreMain.access="1";
			//			StoreMain.update_item_access="1";
			InsuranceTodoEntry dialog = new InsuranceTodoEntry("rajinder","63857");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsuranceTodoEntry(final String check,String ipd_id) {

		setTitle("Insurance Todo Patient");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(InsuranceTodoEntry.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1070, 676);
		getContentPane().setLayout(new BorderLayout());
		contentPanel
		.setBorder(new TitledBorder(null, "Insurance Todo Patient", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(12, 70, 718, 240);
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
		btnCancel.setBounds(878, 600, 155, 32);
		contentPanel.add(btnCancel);

		JButton btnRefresh = new JButton("Save");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				saveTableData();
			}
		});
		btnRefresh.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnRefresh.setBounds(513, 599, 155, 32);
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
		btnExcel.setBounds(720, 599, 155, 32);
		contentPanel.add(btnExcel);


		//		SELECT todo_id,, task_type, upload_path, created_by, created_by_id, created_at, updated_at, is_completed
		//		FROM hospital_db.ins_todo_entry;


		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 365, 1031, 170);
		contentPanel.add(scrollPane);
		todoMasterTable = new JTable();
		todoMasterTable.getTableHeader().setReorderingAllowed(false);
		todoMasterTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		todoMasterTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		todoMasterTable.setModel(
				new DefaultTableModel(
						new Object[][] {}, // no rows
						new String[] { 
								"TODO ID", "TASK", "TYPE", "ENTRY DATE", "DUE DATE", "PRIORITY", "DONE" 
						   }
						)
				);

		todoMasterTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		todoMasterTable.getColumnModel().getColumn(1).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(2).setPreferredWidth(180);
		todoMasterTable.getColumnModel().getColumn(2).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(3).setMinWidth(150);
		todoMasterTable.getColumnModel().getColumn(4).setPreferredWidth(150);
		todoMasterTable.getColumnModel().getColumn(4).setMinWidth(100);
		todoMasterTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
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
					try {

						int row=table.getSelectedRow();
						final File directory = new File(System
								.getProperty("user.dir") + "/localTemp");
						deleteLocalTemp(directory);
						files.clear();
						filesPath.clear();
						list.removeAll();
						list.setListData(files);
						new Thread() {
							@Override
							public void run() {
//								try {
//									try {
//										LocalCopy(
//												getDirectory(pid, examid),
//												examid);
//									} catch (MalformedURLException
//											| SmbException e1) {
//										// TODO Auto-generated catch block
//										e1.printStackTrace();
//									}
//								} catch (Exception ex) {
//									ex.printStackTrace();
//								}
							}
						}.start();
					} catch (Exception e2) {
						// TODO: handle exception
					}

				}
			}
		});


//		table.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseClicked(MouseEvent e) {
//				if (e.getClickCount() == 2) { // double-click
//					int selectedRow = table.getSelectedRow();
//					if (selectedRow != -1) {
//						DefaultTableModel sourceModel = (DefaultTableModel) table.getModel();
//						DefaultTableModel targetModel = (DefaultTableModel) todoMasterTable.getModel();
//
//						Object id = sourceModel.getValueAt(selectedRow, sourceModel.findColumn("ID"));
//						Object task = sourceModel.getValueAt(selectedRow, sourceModel.findColumn("TASK"));
//						Object type = sourceModel.getValueAt(selectedRow, sourceModel.findColumn("TYPE"));
//						Object frequency = sourceModel.getValueAt(selectedRow, sourceModel.findColumn("FREQUENCY"));
//
//						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//						java.util.Date now = new java.util.Date();
//						String entryDate = sdf.format(now);
//
//						java.util.Calendar cal = java.util.Calendar.getInstance();
//						cal.setTime(now);
//						cal.add(java.util.Calendar.DATE, 1);
//						String dueDate = sdf.format(cal.getTime());
//
//						targetModel.addRow(new Object[]{
//								id,
//								task,
//								type,
//								entryDate,
//								dueDate,
//								"Medium", // default priority
//								Boolean.FALSE // DONE checkbox
//						});
//					}
//				}
//			}
//		});


		scrollPane.setViewportView(todoMasterTable);

		JPanel panel_7 = new JPanel();
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(742, 25, 313, 63);
		contentPanel.add(panel_7);
		{
			JLabel label_2 = new JLabel("");
			label_2.setBounds(22, 8, 39, 32);
			panel_7.add(label_2);
			label_2.setIcon(new ImageIcon(InsuranceTodoEntry.class.getResource("/icons/restore.gif")));
		}

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setBounds(742, 100, 313, 210);
		contentPanel.add(panel);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPatientName.setBounds(6, 66, 108, 14);
		panel.add(lblPatientName);

		pNameTF = new JTextField();
		pNameTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		pNameTF.setEditable(false);
		pNameTF.setColumns(10);
		pNameTF.setBounds(106, 61, 201, 25);
		panel.add(pNameTF);

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNote.setBounds(6, 136, 108, 26);
		panel.add(lblNote);

		pIdTF = new JTextField();
		pIdTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		pIdTF.setEditable(false);
		pIdTF.setColumns(10);
		pIdTF.setBounds(106, 99, 201, 25);
		panel.add(pIdTF);

		insTF = new JTextField();
		insTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		insTF.setEditable(false);
		insTF.setColumns(10);
		insTF.setBounds(106, 136, 201, 25);
		panel.add(insTF);

		JLabel claim_idlbl_1 = new JLabel("P Id:");
		claim_idlbl_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		claim_idlbl_1.setBounds(6, 104, 93, 15);
		panel.add(claim_idlbl_1);

		ipdTF = new JTextField();
		ipdTF.setBounds(106, 24, 201, 25);
		panel.add(ipdTF);
		ipdTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		ipdTF.setEditable(false);
		ipdTF.setColumns(10);

		JLabel lblOpdNo = new JLabel("IPD No :");
		lblOpdNo.setBounds(6, 29, 108, 14);
		panel.add(lblOpdNo);
		lblOpdNo.setFont(new Font("Dialog", Font.PLAIN, 12));

		insCatTF = new JTextField();
		insCatTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		insCatTF.setEditable(false);
		insCatTF.setColumns(10);
		insCatTF.setBounds(106, 174, 201, 25);
		panel.add(insCatTF);

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
		Btngroup.add(radioButton);

		JRadioButton radioButton_1 = new JRadioButton("");
		radioButton_1.setBounds(438, 34, 28, 23);
		contentPanel.add(radioButton_1);
		Btngroup.add(radioButton_1);

		JRadioButton radioButton_2 = new JRadioButton("");
		radioButton_2.setBounds(393, 34, 28, 23);
		contentPanel.add(radioButton_2);
		Btngroup.add(radioButton_2);

		JLabel lblChoices = new JLabel("Choices :");
		lblChoices.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblChoices.setBounds(303, 38, 108, 14);
		contentPanel.add(lblChoices);

		JButton btnRefresh_1 = new JButton("Add");
		btnRefresh_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// double-click
				DefaultTableModel sourceModel = (DefaultTableModel) table.getModel();
				DefaultTableModel targetModel = (DefaultTableModel) todoMasterTable.getModel();

				for (int i = 0; i < table.getRowCount(); i++) {
					Boolean isSelected = (Boolean) table.getValueAt(i, table.getColumn("SELECT").getModelIndex());
					if (isSelected != null && isSelected) {

						Object id = sourceModel.getValueAt(i, sourceModel.findColumn("ID"));
						Object task = sourceModel.getValueAt(i, sourceModel.findColumn("TASK"));
						Object type = sourceModel.getValueAt(i, sourceModel.findColumn("TYPE"));
						Object frequency = sourceModel.getValueAt(i, sourceModel.findColumn("FREQUENCY"));
						Object priority = sourceModel.getValueAt(i, sourceModel.findColumn("PRIORITY"));

						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						java.util.Date now = new java.util.Date();
						String entryDate = sdf.format(now);

						java.util.Calendar cal = java.util.Calendar.getInstance();
						cal.setTime(now);
						cal.add(java.util.Calendar.DATE, 1);
						String dueDate = sdf.format(cal.getTime());

						targetModel.addRow(new Object[]{
								"",
								task,
								type,
								entryDate,
								dueDate,
								priority, // default priority
								Boolean.FALSE // DONE checkbox
						});

					}
				}

			}
		});
		btnRefresh_1.setFont(new Font("Dialog", Font.BOLD, 14));
		btnRefresh_1.setBounds(42, 322, 155, 24);
		contentPanel.add(btnRefresh_1);

		JButton btnRefresh_2 = new JButton("Remove");
		btnRefresh_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRow = todoMasterTable.getSelectedRow();
				if (selectedRow != -1) {
					DefaultTableModel model = (DefaultTableModel) todoMasterTable.getModel();
					model.removeRow(selectedRow);
				} else {
					JOptionPane.showMessageDialog(null, "Please select a row to remove.");
				}
			}
		});
		btnRefresh_2.setFont(new Font("Dialog", Font.BOLD, 14));
		btnRefresh_2.setBounds(213, 322, 155, 24);
		contentPanel.add(btnRefresh_2);

		searchTodoListTF = new JTextField();
		searchTodoListTF.setColumns(10);
		searchTodoListTF.setBounds(489, 324, 138, 24);
		contentPanel.add(searchTodoListTF);

		JLabel label_2 = new JLabel("Search");
		label_2.setBounds(393, 328, 231, 20);
		contentPanel.add(label_2);


		insCB = new JComboBox();
		insCB.setBounds(565, 32, 155, 24);
		contentPanel.add(insCB);

		JLabel lblReplaceWith = new JLabel("Ins :");
		lblReplaceWith.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblReplaceWith.setBounds(524, 39, 206, 15);
		contentPanel.add(lblReplaceWith);

		JLabel label_1 = new JLabel("");
		label_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		label_1.setBounds(12, 25, 718, 37);
		contentPanel.add(label_1);

		JLabel label_1_1 = new JLabel("");
		label_1_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		label_1_1.setBounds(12, 316, 641, 37);
		contentPanel.add(label_1_1);
		insCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String ins=insCB.getSelectedItem().toString();
				populateExpensesTable(ins);
			}
		});
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

		searchTodoListTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchTodoListTF.getText() + "";
				searchTableContents1(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchTodoListTF.getText() + "";
				searchTableContents1(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchTodoListTF.getText() + "";
				searchTableContents1(str);
			}
		});

		getDoctorDetails();
		getIPDDATA(ipd_id);
		rowSorter1 = new TableRowSorter<>(todoMasterTable.getModel());
		todoMasterTable.setRowSorter(rowSorter1); // ✅ Attach sorter to table
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING,
						TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_1.setBounds(42, 547, 303, 82);
		contentPanel.add(panel_1);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 12, 281, 58);
		panel_1.add(scrollPane_1);
		list = new JList();
		list.setToolTipText("Double Click To Open File");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_1.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});

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

	public void searchTableContents1(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter1.setRowFilter(null);
		} else {
			rowSorter1.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
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
	public void getIPDDATA(String ipd_id) {
		ResultSet resultSet=null;
		IPDDBConnection db = new IPDDBConnection();
		resultSet = db.retrieveIPDData(ipd_id);
		try {
			while (resultSet.next()) {
				ipdTF.setText(ipd_id);
				pIdTF.setText("" + resultSet.getObject(1));
				pNameTF.setText("" + resultSet.getObject(2));
				insTF.setText("" + resultSet.getObject(6));
				insCatTF.setText("" + resultSet.getString(9));
				insCB.setSelectedItem(resultSet.getString(6));
				reg_no=resultSet.getString(8);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void populateExpensesTable(String ins) {
		try {
			InsuranceDBConnection DoctorDBConnection = new InsuranceDBConnection();
			ResultSet rs = DoctorDBConnection.retrieveAllTasks(ins);

			int NumberOfColumns = rs.getMetaData().getColumnCount();
			int NumberOfRows = 0;

			while (rs.next()) NumberOfRows++;
			rs.beforeFirst();

			Object[][] Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 1]; // +1 for DATE column
			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				// Initialize checkbox column (last one) to false
				Rows_Object_Array[R][NumberOfColumns] = Boolean.FALSE;
				R++;
			}

			DefaultTableModel model = new DefaultTableModel(
					Rows_Object_Array,
					new String[]{"ID", "TASK", "TYPE", "FREQUENCY", "DATE", "PRIORITY", "SELECT"}) {

				@Override
				public boolean isCellEditable(int row, int column) {
					// Allow editing only for specific columns
					String columnName = getColumnName(column);
					return columnName.equals("DATE") ||
							columnName.equals("PRIORITY") ||
							columnName.equals("SELECT");
				}

				@Override
				public Class<?> getColumnClass(int columnIndex) {
					// Make the last column (SELECT) a checkbox
					if (columnIndex == getColumnCount() - 1) {
						return Boolean.class;
					}
					return Object.class;
				}
			};

			table.setModel(model);

			// --- Date chooser editor for DATE column ---
			table.getColumn("DATE").setCellEditor(new DateChooserEditor());

			// --- ComboBox editor for PRIORITY column ---
			JComboBox<String> priorityCombo = new JComboBox<>(new String[]{"High", "Medium", "Low"});
			table.getColumn("PRIORITY").setCellEditor(new DefaultCellEditor(priorityCombo));

			// --- Sorting & visual settings ---
			rowSorter = new TableRowSorter<>(table.getModel());
			table.setRowSorter(rowSorter);
			table.setFont(new Font("Tahoma", Font.PLAIN, 12));

			autoResizeColumns(table);

		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}

	public void saveTableData() {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

			for (int i = 0; i < table.getRowCount(); i++) {
				Boolean isSelected = (Boolean) table.getValueAt(i, table.getColumn("SELECT").getModelIndex());
				if (isSelected != null && isSelected) {

					String[] data = new String[15]; // total fields as per ins_todo_entry

					data[0] = ipdTF.getText();  // ipd_id
					data[1] = pIdTF.getText();  // p_id
					data[2] = pNameTF.getText(); // p_name
					data[3] = insTF.getText();   // ins_type
					data[4] = insCatTF.getText(); // ins_category

					Object dateObj = table.getValueAt(i, table.getColumn("DATE").getModelIndex());
					data[5] = (dateObj != null) ? sdf.format((java.util.Date) dateObj) : sdf.format(new java.util.Date());
					if (dateObj instanceof java.util.Date) {
						data[5] = sdf.format((java.util.Date) dateObj);
					} else if (dateObj instanceof String) {
						data[5] = dateObj.toString(); // already formatted
					} else {
						data[5] = sdf.format(new java.util.Date()); // default current date
					}

					data[6] = (String) table.getValueAt(i, table.getColumn("PRIORITY").getModelIndex()); // priority

					data[7] = reg_no; // ins_registration_no
					data[8] = String.valueOf(table.getValueAt(i, table.getColumn("ID").getModelIndex())); // task_id
					data[9] = String.valueOf(table.getValueAt(i, table.getColumn("TASK").getModelIndex())); // task_name
					data[10] = String.valueOf(table.getValueAt(i, table.getColumn("TYPE").getModelIndex())); // task_type
					data[11] = ""; // upload_path (optional or to be updated later)
					data[12] = "system";//ReceptionMain.userName;
					data[13] = "11";//ReceptionMain.receptionIdSTR; // created_by_id
					data[14] = "0"; // created_by_id

					InsuranceDBConnection DoctorDBConnection = new InsuranceDBConnection();
					int todo_id=DoctorDBConnection.saveTaskToDB(data);
					DoctorDBConnection.updateTodoPath(data[1]+"/images/"+todo_id+"/",todo_id+"");
					DoctorDBConnection.closeConnection();
				}
			}

			JOptionPane.showMessageDialog(null, "Selected rows saved successfully!");
		} catch (Exception ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Error while saving: " + ex.getMessage());
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
	public void getAllFiles(String path) throws MalformedURLException,
	SmbException {
		files.clear();
		filesPath.clear();
		File folder1 = new File(path);
		File[] listOfFiles1 = folder1.listFiles();
		// System.out.println(listOfFiles1.length+"  Total Files");
		for (int i = 0; i < listOfFiles1.length; i++) {
			if (listOfFiles1[i].isFile()) {
				files.add(listOfFiles1[i].getName().toString());
				filesPath.add(listOfFiles1[i].getPath().toString());
			}
		}
		list.removeAll();
		list.setListData(files);
	}
	public static boolean deleteLocalTemp(File directory) {

		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteLocalTemp(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}
	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}

	public String getDirectory(String path) {
		return mainDir + "/HMS/Patient/" + path + "/";
	}




	public void LocalCopy(String path, String index)
			throws MalformedURLException, SmbException {
		System.out.println(path);
		String files;
		SmbFile folder = new SmbFile(path);

		SmbFile[] listOfFiles;
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		// fileList.clear();
		System.out.println("Lengthgh"+listOfFiles.length);
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();

				try {
					copyFileFilesLocal(getDirectory(index) + "/" + files,
							"localTemp/" + files.replaceAll("\\s+", ""));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		try {
			getAllFiles("localTemp/");
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void copyFileFilesLocal(String source, String dest)
			throws IOException {
		new File("localTemp").mkdir();

		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = null;
		try {
			is = remoteFile.getInputStream();

		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

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