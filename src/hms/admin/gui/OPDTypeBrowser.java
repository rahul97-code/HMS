package hms.admin.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.doctor.gui.DoctorBrowser;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.pricemaster.database.PriceMasterDBConnection;
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
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JComboBox;

public class OPDTypeBrowser extends JDialog {

	private JPanel contentPane;
	private JTable opdBrwoserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private TableRowSorter<TableModel> sorter;
	public String search = "";
	int selectedRowIndex;
	public JComboBox selectInsuranceCB;
	static String OS;
	private JButton btnEditOPD;
	private JButton activeBT;
	
	final DefaultComboBoxModel insuranceType = new DefaultComboBoxModel();
	String insuranceSTR;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					OPDTypeBrowser frame = new OPDTypeBrowser();
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
	public OPDTypeBrowser() {
		setResizable(false);
		setTitle("Doctor Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OPDTypeBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 729, 463);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		OS = System.getProperty("os.name").toLowerCase();
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 515, 418);
		contentPane.add(scrollPane);
		setModal(true);
	
		opdBrwoserTable = new JTable();
		opdBrwoserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdBrwoserTable.getTableHeader().setReorderingAllowed(false);
		opdBrwoserTable
				.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdBrwoserTable.setModel(new DefaultTableModel(new Object[][] { {
				null, null, null, null, null }, }, new String[] { "OPD ID",
				"OPD TYPE", "Rate", "Insurance"}));
		opdBrwoserTable.getColumnModel().getColumn(0).setMinWidth(90);
		opdBrwoserTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		opdBrwoserTable.getColumnModel().getColumn(1).setMinWidth(180);
		opdBrwoserTable.getColumnModel().getColumn(2).setPreferredWidth(130);
		opdBrwoserTable.getColumnModel().getColumn(2).setMinWidth(130);
		opdBrwoserTable.getColumnModel().getColumn(3).setMinWidth(100);
		opdBrwoserTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = opdBrwoserTable.getSelectedRow();
						selectedRowIndex = opdBrwoserTable
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = opdBrwoserTable
								.getSelectedColumn();
					
						btnEditOPD.setEnabled(true);
						activeBT.setEnabled(true);
					}
				});
		scrollPane.setViewportView(opdBrwoserTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		selectInsuranceCB = new JComboBox();
		selectInsuranceCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		selectInsuranceCB.setBounds(10, 62, 172, 29);
		panel.add(selectInsuranceCB);
		
		selectInsuranceCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					insuranceSTR = selectInsuranceCB.getSelectedItem().toString();
					
					populateTable1(insuranceSTR);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		JLabel lblSearchNameOr = new JLabel("Select Insurance");
		lblSearchNameOr.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchNameOr.setBounds(36, 37, 111, 14);
		panel.add(lblSearchNameOr);
		
		JButton btnNewButton = new JButton("New OPD Type");
		btnNewButton.setBounds(22, 122, 160, 35);
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				AddOPDtype addOPDtype=new AddOPDtype();
				addOPDtype.setModal(true);
				addOPDtype.setVisible(true);
			
			}
		});
		btnNewButton.setIcon(new ImageIcon(OPDTypeBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnEditOPD = new JButton("Edit OPD Type");
		btnEditOPD.setBounds(22, 168, 160, 35);
		panel.add(btnEditOPD);
		btnEditOPD.setEnabled(false);
		btnEditOPD.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Object selectedObject1 = opdBrwoserTable.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String opdid = selectedObject1.toString();
				
				EditOPDtype editOPDtype=new EditOPDtype(opdid);
				editOPDtype.setModal(true);
				editOPDtype.setVisible(true);
				
				btnEditOPD.setEnabled(false);
				activeBT.setEnabled(false);
			}
		});
		btnEditOPD.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.setBounds(22, 260, 160, 35);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(OPDTypeBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		activeBT = new JButton("Delete");
		activeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane
						.showConfirmDialog(
								OPDTypeBrowser.this,
								"Are you sure to delete OPD type",
								"Cancel", dialogButton);
				if (dialogResult == 0) {
					String statusSTR="1";
					Object selectedObject1 = opdBrwoserTable.getModel()
							.getValueAt(selectedRowIndex, 0);
					
					String opdType_id = selectedObject1.toString();
					
					activeBT.setEnabled(false);
					
				PriceMasterDBConnection db = new PriceMasterDBConnection();
				try {
					db.deleteRow(opdType_id);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				populateTable1(insuranceSTR);
				}
				btnEditOPD.setEnabled(false);
				activeBT.setEnabled(false);
			}
		});
		activeBT.setEnabled(false);
		activeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		activeBT.setBounds(22, 214, 160, 35);
		panel.add(activeBT);
		getAllinsurance() ;
		
	}

	public void populateTable1(String insuranceType) {
		try {
			PriceMasterDBConnection db = new PriceMasterDBConnection();
			ResultSet rs = db.retrieveDataWithCategory("00",insuranceType,"");

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithCategory("00",insuranceType,"");

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
					new String[] { "OPD ID",
					"OPD TYPE", "Rate", "Insurance"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			opdBrwoserTable.setModel(model);
			opdBrwoserTable.getColumnModel().getColumn(0).setMinWidth(90);
			opdBrwoserTable.getColumnModel().getColumn(1).setPreferredWidth(180);
			opdBrwoserTable.getColumnModel().getColumn(1).setMinWidth(180);
			opdBrwoserTable.getColumnModel().getColumn(2).setPreferredWidth(130);
			opdBrwoserTable.getColumnModel().getColumn(2).setMinWidth(130);
			opdBrwoserTable.getColumnModel().getColumn(3).setMinWidth(100);
			
		} catch (SQLException ex) {
			Logger.getLogger(OPDTypeBrowser.class.getName()).log(Level.SEVERE,
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
	
	public void getAllinsurance() {
		insuranceType.addElement("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceType.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		selectInsuranceCB.setModel(insuranceType);
		selectInsuranceCB.setSelectedIndex(0);
	}
	
}
