package hms.admin.gui;

import hms.main.DateFormatChange;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DepartmentUserBrowser extends JDialog {

	private JPanel contentPane;
	private JTable browserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private TableRowSorter<TableModel> sorter;
	public String search = "";
	int selectedRowIndex;
	public JTextField searchTB;
	static String OS;
	private JButton btnEditDoctor;
	private JButton btnDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DepartmentUserBrowser frame = new DepartmentUserBrowser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DepartmentUserBrowser() {
		setResizable(false);
		setTitle("Department User Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DepartmentUserBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setModal(true);
		OS = System.getProperty("os.name").toLowerCase();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 504, 418);
		contentPane.add(scrollPane);

		browserTable = new JTable();
		browserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		browserTable.getTableHeader().setReorderingAllowed(false);
		browserTable
				.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		browserTable.setModel(new DefaultTableModel(new Object[][] { {
				null, null, null, null, null }, }, new String[] { "User ID.",
				"User Name", "Telephone No.", "Address" }));
		browserTable.getColumnModel().getColumn(0).setMinWidth(75);
		browserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
		browserTable.getColumnModel().getColumn(1).setMinWidth(120);
		browserTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		browserTable.getColumnModel().getColumn(2).setMinWidth(130);
		browserTable.getColumnModel().getColumn(3).setMinWidth(75);
		browserTable.getColumnModel().getColumn(3).setPreferredWidth(150);
		
		browserTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = browserTable.getSelectedRow();
						selectedRowIndex = browserTable
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = browserTable
								.getSelectedColumn();
						btnEditDoctor.setEnabled(true);
						btnDelete.setEnabled(true);
					}
				});
		scrollPane.setViewportView(browserTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DepartmentUserBrowser.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(48, 324, 111, 83);
		panel.add(lblNewLabel);

		searchTB = new JTextField();
		searchTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchTB.setBounds(10, 49, 172, 29);
		panel.add(searchTB);
		searchTB.setColumns(10);
		searchTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchTB.getText() + "";
				if (!str.equals("")) {
					search = str;
					populateTable();
				} else {
					populateTable1();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchTB.getText() + "";
				if (!str.equals("")) {
					search = str;
					populateTable();
				} else {
					populateTable1();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchTB.getText() + "";
				if (!str.equals("")) {
					search = str;
					populateTable();
				} else {
					populateTable1();
				}
			}
		});

		JLabel lblSearchNameOr = new JLabel("Search Name or ID");
		lblSearchNameOr.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchNameOr.setBounds(36, 24, 111, 14);
		panel.add(lblSearchNameOr);
		
		JButton btnNewButton = new JButton("New User");
		btnNewButton.setBounds(22, 109, 160, 35);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewDepartmentUser newreceptionist = new NewDepartmentUser();
				newreceptionist.setModal(true);
				newreceptionist.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(DepartmentUserBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnEditDoctor = new JButton("Edit User");
		btnEditDoctor.setBounds(22, 155, 160, 35);
		panel.add(btnEditDoctor);
		btnEditDoctor.setEnabled(false);
		btnEditDoctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Object selectedObject1 = browserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String doctor_id = selectedObject1.toString();
				EditDepartmentUser editreceptionist = new EditDepartmentUser(doctor_id);
				editreceptionist.setModal(true);
				editreceptionist.setVisible(true);
			}
		});
		btnEditDoctor.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(22, 247, 160, 35);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(DepartmentUserBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				

				Object selectedObject1 = browserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String doctor_id = selectedObject1.toString();
				DeptUserDBConnection db = new DeptUserDBConnection();
				try {
					db.deleteRow(doctor_id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();
				populateTable1();
			}
		});
		btnDelete.setEnabled(false);
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete.setBounds(22, 201, 160, 35);
		panel.add(btnDelete);
		populateTable1();
	}

	public void populateTable1() {
		try {
			DeptUserDBConnection db = new DeptUserDBConnection();
			ResultSet rs = db.retrieveAllData2();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveAllData2();

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
					new String[] { "User ID.",
					"User Name", "Telephone No." ,"Department Name"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			btnEditDoctor.setEnabled(false);
			btnDelete.setEnabled(false);
			browserTable.setModel(model);
			browserTable.getColumnModel().getColumn(0).setMinWidth(75);
			browserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			browserTable.getColumnModel().getColumn(1).setMinWidth(120);
			browserTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			browserTable.getColumnModel().getColumn(2).setMinWidth(130);
			browserTable.getColumnModel().getColumn(3).setMinWidth(75);
			browserTable.getColumnModel().getColumn(3).setPreferredWidth(150);
			} catch (SQLException ex) {
			Logger.getLogger(DepartmentUserBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void populateTable() {
		try {
			DeptUserDBConnection db = new DeptUserDBConnection();
			ResultSet rs = db.retrieveAllData2(search);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveAllData2(search);

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
					new String[] { "User ID.",
					"User Name", "Telephone No.", "Department Name" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			browserTable.setModel(model);
			btnEditDoctor.setEnabled(false);
			btnDelete.setEnabled(false);
			browserTable.getColumnModel().getColumn(0).setMinWidth(75);
			browserTable.getColumnModel().getColumn(1).setPreferredWidth(150);
			browserTable.getColumnModel().getColumn(1).setMinWidth(120);
			browserTable.getColumnModel().getColumn(2).setPreferredWidth(150);
			browserTable.getColumnModel().getColumn(2).setMinWidth(130);
			browserTable.getColumnModel().getColumn(3).setMinWidth(75);
			browserTable.getColumnModel().getColumn(3).setPreferredWidth(150);
			
		} catch (SQLException ex) {
			Logger.getLogger(DepartmentUserBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
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

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public JButton getBtnEditPatient() {
		return btnEditDoctor;
	}
	public JButton getBtnDelete() {
		return btnDelete;
	}
}
