package hms.doctor.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms1.ipd.gui.IPDBill;

import java.awt.Color;
import java.awt.Component;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class DoctorBrowser extends JDialog {

	private JPanel contentPane;
	private JTable doctorbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private TableRowSorter<TableModel> sorter;
	public String search = "";
	int selectedRowIndex;
	public JTextField searchTB;
	static String OS;
	private JButton btnEditDoctor;
	private JButton activeBT;
	private JButton btnDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DoctorBrowser frame = new DoctorBrowser();
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
	public DoctorBrowser() {
		setResizable(false);
		setTitle("Doctor Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DoctorBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 838, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		OS = System.getProperty("os.name").toLowerCase();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 624, 418);
		contentPane.add(scrollPane);
		setModal(true);
		doctorbrowserTable = new JTable();
		doctorbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorbrowserTable.getTableHeader().setReorderingAllowed(false);
		doctorbrowserTable
				.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		doctorbrowserTable.setModel(new DefaultTableModel(new Object[][] { {
				null, null, null, null, null }, }, new String[] { "Doctor ID.",
				"Doctor Name", "Specialization", "Room No", "Telephone No.","Index" ,"Active"}));
		doctorbrowserTable.getColumnModel().getColumn(0).setMinWidth(75);
		doctorbrowserTable.getColumnModel().getColumn(1).setPreferredWidth(120);
		doctorbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
		doctorbrowserTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		doctorbrowserTable.getColumnModel().getColumn(2).setMinWidth(130);
		doctorbrowserTable.getColumnModel().getColumn(3).setMinWidth(75);
		doctorbrowserTable.getColumnModel().getColumn(4).setPreferredWidth(100);
		doctorbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
		doctorbrowserTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = doctorbrowserTable.getSelectedRow();
						selectedRowIndex = doctorbrowserTable
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = doctorbrowserTable
								.getSelectedColumn();
						btnEditDoctor.setEnabled(true);
						btnDelete.setEnabled(true);
						activeBT.setEnabled(true);
					}
				});
		scrollPane.setViewportView(doctorbrowserTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DoctorBrowser.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);

		searchTB = new JTextField();
		searchTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchTB.setBounds(10, 62, 172, 29);
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
		lblSearchNameOr.setBounds(36, 37, 111, 14);
		panel.add(lblSearchNameOr);
		
		JButton btnNewButton = new JButton("New Doctor");
		btnNewButton.setBounds(22, 122, 160, 35);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewDoctor newPatient = new NewDoctor();
				newPatient.setModal(true);
				newPatient.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(DoctorBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnEditDoctor = new JButton("Edit Doctor");
		btnEditDoctor.setBounds(22, 168, 160, 35);
		panel.add(btnEditDoctor);
		btnEditDoctor.setEnabled(false);
		btnEditDoctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Object selectedObject1 = doctorbrowserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String doctor_id = selectedObject1.toString();
				EditDoctor editPatient = new EditDoctor(doctor_id);
				editPatient.setModal(true);
				editPatient.setVisible(true);
			}
		});
		btnEditDoctor.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(22, 305, 160, 35);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(DoctorBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		activeBT = new JButton("Active/Inactive");
		activeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String statusSTR="1";
				Object selectedObject1 = doctorbrowserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				
				Object status = doctorbrowserTable.getModel()
						.getValueAt(selectedRowIndex, 6);
				String doctor_id = selectedObject1.toString();
				System.out.print("" + status.toString());
				if(status.toString().equals("Yes"))
				{
					statusSTR="0";
				}else
				{
					statusSTR="1";
				}
				DoctorDBConnection dbConnection = new DoctorDBConnection();
				
				try {
					dbConnection.updateDataStatus(doctor_id, statusSTR);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbConnection.closeConnection();
				populateTable1();
				activeBT.setEnabled(false);
			}
		});
		activeBT.setEnabled(false);
		activeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		activeBT.setBounds(22, 214, 160, 35);
		panel.add(activeBT);
		
		btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane
						.showConfirmDialog(
								DoctorBrowser.this,
								"Are you sure to delete doctor account",
								"Cancel", dialogButton);
				if (dialogResult == 0) {
				Object selectedObject1 = doctorbrowserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String doctor_id = selectedObject1.toString();
				DoctorDBConnection db = new DoctorDBConnection();
				try {
					db.deleteRow(doctor_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				populateTable1();
				}
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnDelete.setBounds(22, 259, 160, 35);
		panel.add(btnDelete);
		populateTable1();
	}

	public void populateTable1() {
		try {
			DoctorDBConnection db = new DoctorDBConnection();
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
					
					if(C==NumberOfColumns)
					{
						if(rs.getObject(C).equals("1"))
							Rows_Object_Array[R][C - 1] = "Yes";
						else {
							Rows_Object_Array[R][C - 1] = "No";
						}
					}
					else {
						Rows_Object_Array[R][C - 1] = rs.getObject(C);
					}
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Doctor ID.", "Doctor Name",
							"Specialization", "Room No", "Telephone No.","Index","Active"  }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			doctorbrowserTable.setModel(model);
			doctorbrowserTable.getColumnModel().getColumn(0).setMinWidth(75);
			doctorbrowserTable.getColumnModel().getColumn(1)
					.setPreferredWidth(120);
			doctorbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
			doctorbrowserTable.getColumnModel().getColumn(2)
					.setPreferredWidth(130);
			doctorbrowserTable.getColumnModel().getColumn(2).setMinWidth(130);
			doctorbrowserTable.getColumnModel().getColumn(3).setMinWidth(75);
			doctorbrowserTable.getColumnModel().getColumn(4)
					.setPreferredWidth(100);
			doctorbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
			doctorbrowserTable.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer());
		} catch (SQLException ex) {
			Logger.getLogger(DoctorBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void populateTable() {
		try {
			DoctorDBConnection db = new DoctorDBConnection();
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
					if(C==NumberOfColumns)
					{
						if(rs.getObject(C).equals("1"))
							Rows_Object_Array[R][C - 1] = "Yes";
						else {
							Rows_Object_Array[R][C - 1] = "No";
						}
					}
					else {
						Rows_Object_Array[R][C - 1] = rs.getObject(C);
					}
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Doctor ID.", "Doctor Name",
							"Specialization", "Room No", "Telephone No.","Index","Active"  }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			doctorbrowserTable.setModel(model);
			btnEditDoctor.setEnabled(false);
			btnDelete.setEnabled(true);
			doctorbrowserTable.getColumnModel().getColumn(0).setMinWidth(75);
			doctorbrowserTable.getColumnModel().getColumn(1)
					.setPreferredWidth(120);
			doctorbrowserTable.getColumnModel().getColumn(1).setMinWidth(120);
			doctorbrowserTable.getColumnModel().getColumn(2)
					.setPreferredWidth(130);
			doctorbrowserTable.getColumnModel().getColumn(2).setMinWidth(130);
			doctorbrowserTable.getColumnModel().getColumn(3).setMinWidth(75);
			doctorbrowserTable.getColumnModel().getColumn(4)
					.setPreferredWidth(100);
			doctorbrowserTable.getColumnModel().getColumn(4).setMinWidth(100);
			
			doctorbrowserTable.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer());
		} catch (SQLException ex) {
			Logger.getLogger(DoctorBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column).equals("Yes")){
	              cellComponent.setBackground(Color.GREEN);
	          } else{
	        	  cellComponent.setBackground(Color.RED);
	        	 
	          }
	          return cellComponent;
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
	public JButton getBtnDelete() {
		return btnDelete;
	}
}
