package hms1.ipd.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.store.gui.BatchBrowser;
import hms.store.gui.StoreMain;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.wards.database.WardsManagementDBConnection;
import lu.tudor.santec.jtimechooser.JTimeChooser;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class IPDBedCalculation extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	String[] data = new String[20];
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	int selectedRowIndex;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "0", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",chargesCurrent="",Wardcharges="",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id, wardNameSTR,quantity="",changeDateSTR;
	double totalPrice=0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel DepartmentCat = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	Vector<String> bedIndex = new Vector<String>();
	Vector<String> expensesIndexVector = new Vector<String>();
	IPDDBConnection ipddbConnection;
	public static Font customFont;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTable table;
	private JTextField ipdNoTB;
	private JTable table_1;
	private JTextField packageDaysTF;
	private JLabel textField_2;
	private JComboBox wardNameCB;
	private JTextField chargesPerDayTF;
	private JTextField totalChargesTF;
	private JLabel lblSelectWard_1;


	public static void main(String sr[]) {
		new IPDBedCalculation("0000130000014","aaishA",null).setVisible(true);
	}
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDBedCalculation(final String p_id, final String p_name,final IPDBill ipdBill) {
		this.p_id = p_id;
		this.p_name = p_name;
		Call_SQL_Procedure();
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDBedCalculation.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Bed Calculation");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1068, 508);

		try {
			// create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT,
					new File("indian.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
					"indian.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_4.setBounds(12, 12, 1047, 465);
		contentPane.add(panel_4);
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		panel_4.setLayout(null);

		JPanel panel_7 = new JPanel();
		panel_7.setBounds(10, 11, 313, 187);
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuilding.setBounds(10, 51, 108, 14);
		panel_7.add(lblBuilding);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 46, 182, 25);
		panel_7.add(ipdBuildingTB);

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(10, 81, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 76, 182, 25);
		panel_7.add(ipdWardTB);

		JLabel lblBedNo = new JLabel("Bed No :");
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNo.setBounds(10, 117, 108, 14);
		panel_7.add(lblBedNo);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 112, 182, 25);
		panel_7.add(ipdBedNoTB);

		JLabel lblNoOfDays = new JLabel("Total No. Days :");
		lblNoOfDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNoOfDays.setBounds(10, 148, 108, 14);
		panel_7.add(lblNoOfDays);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 143, 182, 25);
		panel_7.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_7.add(ipdNoTB);

		JLabel lblOpdNo = new JLabel("IPD No :");
		lblOpdNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpdNo.setBounds(10, 15, 108, 14);
		panel_7.add(lblOpdNo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 216, 775, 238);
		panel_4.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(new Object[][] { { null,
			null, null, null, null ,null,null}, }, new String[] { "S.N", "Ward Name", "Ward Category","Date","Hrs", "Ward Charge","Bed Charges" }));
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
						System.out.println("row-"+selectedRowIndex);
					}
				});
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(190);
		table.getColumnModel().getColumn(1).setMinWidth(190);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(110);
		table.getColumnModel().getColumn(6).setMinWidth(110);
		
		scrollPane.setViewportView(table);
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(333, 11, 450, 187);
		panel_4.add(scrollPane_2);

		table_1 = new JTable();
		scrollPane_2.setViewportView(table_1);
		table_1.setFont(new Font("Tahoma", Font.BOLD, 12));

		packageDaysTF = new JTextField();
		packageDaysTF.setBounds(820, 35, 205, 20);
		panel_4.add(packageDaysTF);
		packageDaysTF.setColumns(10);

		wardNameCB = new JComboBox(new Object[] {});
		wardNameCB.setBounds(820, 97, 203, 24);
		wardNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					wardNameSTR = wardNameCB.getSelectedItem().toString();
					getCharges(wardNameSTR);
				} catch (Exception e) {
					// TODO: handle exception

				}

			}
		});
		panel_4.add(wardNameCB);

		chargesPerDayTF = new JTextField();
		chargesPerDayTF.setBounds(820, 133, 206, 37);
		chargesPerDayTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		chargesPerDayTF.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Ward P/D Charges", TitledBorder.LEFT, TitledBorder.TOP, null, Color.DARK_GRAY));
		chargesPerDayTF.setEditable(false);
		chargesPerDayTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		chargesPerDayTF.setColumns(10);
		panel_4.add(chargesPerDayTF);

		totalChargesTF = new JTextField();
		totalChargesTF.setEditable(false);
		totalChargesTF.setBounds(820, 309, 206, 30);
		totalChargesTF.setColumns(10);
		panel_4.add(totalChargesTF);

		JButton btnNewButton = new JButton("SAVE");
		btnNewButton.setIcon(new ImageIcon(IPDBedCalculation.class.getResource("/icons/SAVE.GIF")));
		btnNewButton.setBounds(820, 358, 205, 42);
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				int check=db.retrieveEXIST(ipd_id, "BED CHARGES");
				if(check!=0)
				{
					 int a=JOptionPane.showConfirmDialog(null,"Bed Charges Already Exist! if you want to overwrite click Yes", "Info..", JOptionPane.WARNING_MESSAGE);  
					 if(a!=JOptionPane.YES_OPTION){  
						 return;
					 }  
					 else {
						 try {
							db.updateData("" + ipd_id);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					 }
				}
				
				String[] data1 = new String[15];
				data1[0] = "" + ipd_id;
				data1[7] = "" + p_id;
				data1[8] = "" + p_name;
				for(int i=0;i<table.getRowCount();i++) {		
					data1[1] = ""+table.getValueAt(i, 1)+"";
					data1[2] = table.getValueAt(i, 2)+"";
					data1[3] = table.getValueAt(i, 6)+"";
					data1[4] = table.getValueAt(i, 3)+"";
					data1[5] = "" + timeFormat.format(cal1.getTime());		
					data1[6] = "B"+table.getValueAt(i, 0);
					data1[9] =  table.getValueAt(i, 5)+"";
					data1[10] =  table.getValueAt(i, 4)+"";
					data1[11] = "BED CHARGES";
					data1[12] = ReceptionMain.receptionNameSTR;
					data1[13] = "BED CHARGES";
					data1[14] = "NA";		
					try {
						
						db.inserData(data1);

					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				JOptionPane
				.showMessageDialog(
						null,
						"Charges Saved Successfully.",
						"Data Save", JOptionPane.INFORMATION_MESSAGE);
				ipdBill.populateExpensesTable(ipd_id);
				db.closeConnection();
				dispose();
			}
		});
		panel_4.add(btnNewButton);

		JLabel lblSelectWard = new JLabel("");
		lblSelectWard.setBorder(new TitledBorder(null, "Select Ward", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		lblSelectWard.setBounds(812, 79, 223, 108);
		lblSelectWard.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_4.add(lblSelectWard);

		textField_2 = new JLabel();
		textField_2.setBorder(new TitledBorder(null, "Package Days", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		textField_2.setBounds(812, 11, 223, 56);
		textField_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		panel_4.add(textField_2);

		lblSelectWard_1 = new JLabel("");
		lblSelectWard_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSelectWard_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Total", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		lblSelectWard_1.setBounds(812, 285, 223, 65);
		panel_4.add(lblSelectWard_1);
		
		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.setIcon(new ImageIcon(IPDBedCalculation.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnNewButton_1.setBounds(820, 412, 205, 42);
		panel_4.add(btnNewButton_1);
		
		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(IPDBedCalculation.class.getResource("/icons/ipdbed.gif")));
		label.setBounds(846, 182, 162, 115);
		panel_4.add(label);

		table_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(
					final PropertyChangeEvent e) {
				if (e.getNewValue() instanceof JTextField) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JTextField textField = (JTextField) e
									.getNewValue();
							textField.selectAll();
						}
					});
				}
			}
		});
		getIPDDATA();
		getAllDept();
	}

	public void getIPDDATA() {

		ipd_id = "0";
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientID(p_id);
		try {
			while (resultSet.next()) {
				ipd_id = resultSet.getObject(1).toString();
				ipdNoTB.setText("" + resultSet.getObject(2));
				ipdBuildingTB.setText("" + resultSet.getObject(3));
				ipdWardTB.setText("" + resultSet.getObject(4));
				ipdBedNoTB.setText("" + resultSet.getObject(5));
				ipd_advance = Double.parseDouble(resultSet.getObject(6)
						.toString());
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				populatedDoctorTable(ipd_id);
				populatedPackage(ipd_id);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void populatedDoctorTable(String ipd_id) {

		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrievePatientBedDetailsNEW(ipd_id);

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
				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
					
				}
				Rows_Object_Array[R][0] = R+1;
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(
					Rows_Object_Array,
					new String[] { "S.N", "Ward Name", "Ward Category","Date","Hrs", "Ward Charge","Bed Charges" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			db.closeConnection();
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(50);
			table.getColumnModel().getColumn(0).setMinWidth(50);
			table.getColumnModel().getColumn(1).setPreferredWidth(200);
			table.getColumnModel().getColumn(1).setMinWidth(200);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(2).setMinWidth(180);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(6).setPreferredWidth(110);
			table.getColumnModel().getColumn(6).setMinWidth(110);
			totalChargesTF.setText("");
			double t=0;
			for(int i=0;i<model.getRowCount();i++) {
				t+=Double.parseDouble(model.getValueAt(i, 6).toString());
			}
			totalChargesTF.setText(""+t);
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		getAllDept();
	}
	public String ConvertTime(String input) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Date/time pattern of desired output date
		DateFormat outputformat = new SimpleDateFormat("hh:mm:ss a");
		Date date = null;
		String output = null;
		try {
			// Conversion of input String to date
			date = df.parse(input);
			// old date format to new date format
			output = outputformat.format(date);
			return output;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return output;
	}
	
	public void populatedPackage(String ipd_id) {

		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrievePatientPackage(ipd_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();;

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			if (NumberOfRows>0) {
				while (rs.next()) {
					for (int C = 1; C <= NumberOfColumns; C++) {
						Rows_Object_Array[R][C - 1] = rs.getObject(C);
					}
					R++;
				} 
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Package Name", "Charges", "Pre Days",
			"Post Days" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};

			table_1.setModel(model);

			rs = db.retrievePatientPackageDays(ipd_id);

			while (rs.next()) {
				Object o=rs.getObject(1);
				String s=(o==null)?"":o.toString();
				packageDaysTF.setText(s);
			}
			db.closeConnection();
		} catch (SQLException ex) {
			//			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
			//					null, ex);
		}

	}

	public void getAllDept() {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrievePatientWards();

		DepartmentCat.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				DepartmentCat.addElement(resultSet.getObject(1).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		wardNameCB.setModel(DepartmentCat);
		if (i > 0) {
			wardNameCB.setSelectedIndex(0);
		}
	}
	public void Call_SQL_Procedure() {
		WardsManagementDBConnection db = new WardsManagementDBConnection();
		try {
			db.SQLProcedureCall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void getCharges(String deptName) {
		System.out.println("arun1");
		WardsManagementDBConnection db = new WardsManagementDBConnection();
		ResultSet resultSet = db
				.retrieveAllWardCharges(deptName);

		try {
			while (resultSet.next()) {
				//				System.out.println("Total Daty  : "+resultSet.getObject(2).toString());
				charges=resultSet.getObject(4).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		chargesPerDayTF.setText(charges);
	}
}
