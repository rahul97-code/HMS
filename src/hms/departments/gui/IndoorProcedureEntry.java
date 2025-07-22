package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ProceduresDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

import java.awt.Color;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
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

public class IndoorProcedureEntry extends JDialog {

	/**
	 * 
	 */
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	double mrp = 0,mrpTotal=0;
	private HashMap examHashMap = new HashMap();
	String[] data = new String[22];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	double totalChargesIPD = 0;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemTypeV = new Vector<String>(); 
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> mrpPriceV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> itemAvailableV = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	double ipd_advance = 0, ipd_balance = 0;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String doctorName = "", doctorID = "", date = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR1 = "", charges = "",
			ipd_days, ipd_houres, ipd_minutes, ipd_expenses_id;
	String procedureIDSTR, procedureNameSTR, itemDescSTR, taxTypeSTR,
	taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
	dueDateSTR = "", previouseStock = "",itemtypeSTR="",
	departmentName = DepartmentMain.departmentName,
	departmentID = DepartmentMain.departmentID;
	int qtyIssued = 0, afterIssued = 0, finalTaxValue = 0,
			finalDiscountValue = 0;
	int quantity = 0;
	double price = 0, finalTotalValue = 0, taxValue = 0, surchargeValue = 0,
			itemValue = 0;

	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel procedureName = new DefaultComboBoxModel();
	Vector<String> procedureIDV = new Vector<String>();
	Vector<String> procedureNameV = new Vector<String>();
	Vector<String> procedurePriceV = new Vector<String>();
	Vector<String> procedurePreDaysV = new Vector<String>();
	Vector<String> procedurePostDaysV = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JTable addTestTable_1;
	private JComboBox<String> procedureNameCB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField priceET;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField ipdNoTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTable table;
	private JTextField predaysLBL;
	private JTextField postdaysLBL;
	int packSize = 1;
	private Timer timer;
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new IndoorProcedureEntry().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public IndoorProcedureEntry() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(
						IndoorProcedureEntry.class
						.getResource("/icons/rotaryLogo.png")));
		setTitle("Patient Procedures Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(50, 70, 1203, 531);
		setModal(true);
		departmentName = DepartmentMain.departmentName;
		departmentID = DepartmentMain.departmentID;
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
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 1178, 493);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(855, 11, 313, 343);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 106, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 101, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 142, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(6, 178, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(6, 214, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 249, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(6, 277, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(106, 137, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 173, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 209, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 245, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 277, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 277, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 315, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 307, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);

		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(6, 284, 29, -1);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 36, 108, 14);
		panel.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setBounds(106, 31, 201, 25);
		panel.add(searchPatientTB);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		patientIDCB = new JComboBox(patientID);
		patientIDCB.setBounds(106, 65, 201, 25);
		panel.add(patientIDCB);
		patientIDCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					p_id = patientIDCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				patientNameTB.setText("");
				addressTB.setText("");
				ageTB.setText("");
				cityTB.setText("");
				telephoneTB.setText("");
				insuranceTypeTB.setText("");
				ipdBuildingTB.setText("");
				ipdNoTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");
				bedDaysTB.setText("");
				ipdDoctorTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);

				// System.out.println(p_id + "    "
				// + patientIDCB.getSelectedIndex());
				setPatientDetail(p_id);
				if (patientID.getSize() > 0) {
					patientNameTB.setText(p_name);
					addressTB.setText(p_address);
					ageTB.setText(p_age + "  (Y-M-D)");
					cityTB.setText(p_city);
					telephoneTB.setText(p_telephone);
					insuranceTypeTB.setText(p_insurancetype);
					if (p_sex.equals("Male")) {
						rdbtnMale.setSelected(true);
						rdbtnFemale.setSelected(false);
					} else {
						rdbtnMale.setSelected(false);
						rdbtnFemale.setSelected(true);
					}

					itemsHashMap.clear();
					examHashMap.clear();
					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(6, 67, 77, 20);
		panel.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					getPatientsID(str);
				} else {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					patientID.removeAllElements();
					patientIDCB.setModel(patientID);
					lastOPDDateLB.setText("Last OPD :");
					procedureNameCB.setSelectedIndex(0);

					itemsHashMap.clear();
					examHashMap.clear();
					ipdNoTB.setText("");
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable("??");
					loadDataToTable();
				}}
		});
		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}
				});
		searchPatientTB.requestFocus(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 509, 472);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Procedures Detail :",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(5, 11, 494, 166);
		panel_2.add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		procedureNameCB = new JComboBox<String>();
		procedureNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					procedureNameSTR = procedureNameCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (procedureNameCB.getSelectedIndex() > -1) {
					procedureIDSTR = procedureIDV.get(procedureNameCB
							.getSelectedIndex());
				}

				priceET.setText("");
				priceET.setText(""
						+ procedurePriceV.get(procedureNameCB
								.getSelectedIndex()));
				predaysLBL.setText(""
						+ procedurePreDaysV.get(procedureNameCB
								.getSelectedIndex()));
				postdaysLBL.setText(""
						+ procedurePostDaysV.get(procedureNameCB
								.getSelectedIndex()));
				itemIDV.clear();
				itemNameV.clear();
				itemDescV.clear();
				issuedQtyV.clear();
				itemPriceV.clear();
				totalPriceV.clear();
				itemAvailableV.clear();
				ProceduresDBConnection dbConnection = new ProceduresDBConnection();
				ResultSet resultSet = dbConnection
						.retrieveDataProcedure(procedureIDSTR);
				System.out.println(procedureIDSTR+"procedureIDSTR");
				try {
					while (resultSet.next()) {
						System.out.println(resultSet.getObject(1).toString()+"ITEMID");
						itemIDV.add(resultSet.getObject(1).toString());
						itemTypeV.add(resultSet.getObject(7).toString());
						itemNameV.add(resultSet.getObject(2).toString());
						itemDescV.add(resultSet.getObject(3).toString());
						double itemPrice = getItemDetail(resultSet.getObject(1)
								.toString());
						double quantity = Double.parseDouble(resultSet
								.getObject(4).toString());
						double finalPrice = itemPrice * quantity;
						double mrpnew = getItemMRP(resultSet.getObject(1).toString());
						System.out.println(mrpnew+"mrpnew");
						//						mrpPriceV.add(mrpnew+"");
						measUnitV.add(packSize+"");
						issuedQtyV.add(quantity + "");
						totalPriceV.add(finalPrice + "");
						itemPriceV.add(itemPrice + "");
						itemAvailableV.add("From Store");
						// getItemStock(resultSet.getObject(1).toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbConnection.closeConnection();
				loadDataToTable();
				priceET.setText("" + finalTotalValue);
			}
		});
		procedureNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		procedureNameCB.setBounds(170, 16, 283, 25);
		panel_5.add(procedureNameCB);

		priceET = new JTextField();
		priceET.setEditable(false);
		priceET.setColumns(10);
		priceET.setBounds(170, 52, 283, 25);
		panel_5.add(priceET);

		JLabel lblPrice = new JLabel("Price :");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(26, 51, 113, 25);
		panel_5.add(lblPrice);

		JLabel lblSelectProcedure = new JLabel("Select Procedure :");
		lblSelectProcedure.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelectProcedure.setBounds(25, 16, 135, 25);
		panel_5.add(lblSelectProcedure);

		JLabel lblPreDays = new JLabel("Pre Days :");
		lblPreDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPreDays.setBounds(26, 88, 113, 25);
		panel_5.add(lblPreDays);

		predaysLBL = new JTextField();
		predaysLBL.setEditable(false);
		predaysLBL.setColumns(10);
		predaysLBL.setBounds(170, 89, 283, 25);
		panel_5.add(predaysLBL);

		JLabel lblPostDays = new JLabel("Post Days :");
		lblPostDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPostDays.setBounds(26, 124, 113, 25);
		panel_5.add(lblPostDays);

		postdaysLBL = new JTextField();
		postdaysLBL.setEditable(false);
		postdaysLBL.setColumns(10);
		postdaysLBL.setBounds(170, 125, 283, 25);
		panel_5.add(postdaysLBL);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 188, 489, 277);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.getTableHeader().setReorderingAllowed(false);
		addTestTable_1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		addTestTable_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {}));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
		scrollPane.setViewportView(addTestTable_1);

		JButton btnNewButton_3 = new JButton("Request");
		btnNewButton_3.setIcon(new ImageIcon(IndoorProcedureEntry.class
				.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (ipdNoTB.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"This patient is not a indoor patient. ",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (itemIDV.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					// } else if (itemAvailableV.contains("Not Available")) {//
					// itemsHashMap.size()
					// // == 0
					// JOptionPane
					// .showMessageDialog(
					// null,
					// "Some Items are not available in your stock please issue from main store",
					// "Input Error", JOptionPane.ERROR_MESSAGE);
				} else {

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");

					IssuedItemsDBConnection storePillsRegisterDBConnection = new IssuedItemsDBConnection();
					ItemsDBConnection itemStockDBConnection = new ItemsDBConnection();

					data[0] = "" + doctorID; //
					data[1] = ipdDoctorTB.getText().toString(); //
					data[2] = "" + DepartmentMain.departmentID; // user id
					data[3] = "" + DepartmentMain.departmentName; // user name
					data[4] = p_id;
					data[5] = p_name;
					data[12] = date;
					data[13] = "" + timeFormat.format(cal1.getTime());
					data[14] = "" + ipd_id; // ipd no
					data[15] = "INDOOR";
					data[16] = "PENDING";
					data[17] = procedureNameSTR;

					for (int i = 0; i < itemIDV.size(); i++) {
						data[6] = itemIDV.get(i);
						data[7] = "" + itemNameV.get(i);
						data[8] = itemDescV.get(i);
						double pricenew = getItemDetail(itemIDV.get(i));
						data[9] = pricenew+"";
						data[10] = issuedQtyV.get(i);
						data[11] = totalPriceV.get(i);
						data[18]=itemTypeV.get(i);
						double mrpnew = getItemMRP(itemIDV.get(i));
						data[19]=mrpnew+"";
						System.out.println("ItemID"+itemIDV.get(i)+"ItemMRP"+mrpnew);
						data[20]=measUnitV.get(i);
						try {
							storePillsRegisterDBConnection
							.inserStorePillsRegisterNewWIthMRP(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					itemStockDBConnection.closeConnection();

					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[16];
					for (int i = 0; i < itemIDV.size(); i++) {
						data1[0] = "" + ipd_id;
						data1[1] = "" + itemNameV.get(i);
						data1[2] = itemDescV.get(i);
						data1[3] = totalPriceV.get(i);
						data1[4] = ""
								+ DateFormatChange
								.StringToMysqlDate(new Date());
						data1[5] = "" + timeFormat.format(cal1.getTime());
						data1[6] = itemIDV.get(i);
						data1[7] = "" + p_id;
						data1[8] = "" + p_name;
						data1[9] = itemPriceV.get(i);
						data1[10] = issuedQtyV.get(i);
						data1[11] = db.retrieveItemSubCategory(itemIDV.get(i));
						data1[12] = DepartmentMain.userName;
						data1[13] = db.retrieveItemCategory(itemIDV.get(i));
						data1[14] = procedureNameSTR;
						data1[15]=itemTypeV.get(i);
						try {
							//							db.inserData2(data1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
					}

					db.closeConnection();
					double tot = totalChargesIPD
							+ Double.parseDouble(priceET.getText().toString());
					double finalValue = Math.round(tot * 100.0) / 100.0;
					IPDDBConnection ipddbConnection = new IPDDBConnection();
					try {
						ipddbConnection.updateTotalAmount(ipd_id, finalValue
								+ "");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						ipddbConnection.closeConnection();
						e.printStackTrace();
					}
					ipddbConnection.closeConnection();
					JOptionPane
					.showMessageDialog(
							null,
							"Data Requested Successfully. Please Contact to Store ",
							"Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(1021, 365, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(1022, 434, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IndoorProcedureEntry.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_3.setBounds(529, 11, 316, 219);
		panel_4.add(panel_3);

		JLabel label = new JLabel("Doctor Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 46, 108, 14);
		panel_3.add(label);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_3.add(ipdDoctorTB);

		JLabel label_1 = new JLabel("Building :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 77, 108, 14);
		panel_3.add(label_1);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_3.add(ipdBuildingTB);

		JLabel label_2 = new JLabel("Ward Name :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 113, 108, 14);
		panel_3.add(label_2);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_3.add(ipdWardTB);

		JLabel label_3 = new JLabel("Bed No :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(10, 149, 108, 14);
		panel_3.add(label_3);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 144, 182, 25);
		panel_3.add(ipdBedNoTB);

		JLabel label_4 = new JLabel("No. of Days :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(10, 180, 108, 14);
		panel_3.add(label_4);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 175, 182, 25);
		panel_3.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_3.add(ipdNoTB);

		JLabel label_5 = new JLabel("IPD No :");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(10, 15, 108, 14);
		panel_3.add(label_5);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Bill",

				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(529, 238, 316, 116);
		panel_4.add(panel_6);

		JLabel label_6 = new JLabel("Total Amount :");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_6.setBounds(6, 21, 108, 14);
		panel_6.add(label_6);

		totalAmountTB = new JTextField();
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);
		totalAmountTB.setBounds(149, 16, 147, 25);
		panel_6.add(totalAmountTB);

		JLabel label_7 = new JLabel("Advance Payment :");
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_7.setBounds(6, 66, 108, 14);
		panel_6.add(label_7);

		advancePaymentTB = new JTextField();
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);
		advancePaymentTB.setBounds(148, 61, 148, 25);
		panel_6.add(advancePaymentTB);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(539, 365, 458, 116);
		panel_4.add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

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
		getAllProcedures();
	}

	public void getPatientsID(String index) {
		lastOPDDateLB.setText("Last Exam : ");
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.searchPatientWithIdOrNmae(index);
		patientID.removeAllElements();
		try {
			while (resultSet.next()) {
				patientID.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		patientIDCB.setModel(patientID);
	}

	public void setPatientDetail(String indexId) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex(indexId);
		try {
			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_age = resultSet.getObject(2).toString();
				p_sex = resultSet.getObject(3).toString();
				p_address = resultSet.getObject(4).toString();
				p_city = resultSet.getObject(5).toString();
				p_telephone = resultSet.getObject(6).toString();
				p_insurancetype = resultSet.getObject(7).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getIPDDATA();
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = totalPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = itemAvailableV.get(i);
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		addTestTable_1.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Desc.", "Issued Qty.", "Cost",
				"Item Availability" }));
		addTestTable_1.getColumnModel().getColumn(0).setPreferredWidth(50);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(120);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(4).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(4).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(5).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(5).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		addTestTable_1.getColumnModel().getColumn(3)
		.setCellRenderer(centerRenderer);
		addTestTable_1.getColumnModel().getColumn(4)
		.setCellRenderer(centerRenderer);

		finalTotalValue = total;
	}

	// public void itemValue(){
	// itemValue = qtyIssued * price;
	// // System.out.println("unit" + itemValue);
	// double k = itemValue * (taxValue / 100.0f);
	// k = Math.round(k * 100.000) / 100.000;
	// double s = itemValue * (surchargeValue / 100.0f);
	// s = Math.round(s * 100.000) / 100.000;
	//
	// itemValue = itemValue + k;
	// itemValue = itemValue + s;
	//
	// itemValue = Math.round(itemValue * 100.000) / 100.000;
	// costET.setText(itemValue + "");
	//
	// }
	//june 2020
	//	public double getItemDetail(String index) {
	//
	//		GeneralDBConnection formula = new GeneralDBConnection();
	//		double value = Double.parseDouble(formula.retrieveFormula1());
	//		formula.closeConnection();
	//		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
	//		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
	//		double mrp = 0;
	//		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
	//		int packSize = 1;
	//		String formulaActive = "";
	//		try {
	//			while (resultSet.next()) {
	//
	//				itemDescSTR = resultSet.getObject(3).toString();
	//				taxValue = Double
	//						.parseDouble(resultSet.getObject(6).toString());
	//				surchargeValue = Double.parseDouble(resultSet.getObject(13)
	//						.toString());
	//				purchase = Double
	//						.parseDouble(resultSet.getObject(7).toString());
	//				price = Double.parseDouble(resultSet.getObject(10).toString());
	//				tot = (double) Math.round(purchase * value * 100) / 100;
	//				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
	//				formulaActive = resultSet.getObject(15).toString();
	//				try {
	//					packSize = Integer.parseInt(resultSet.getObject(4)
	//							.toString().trim());
	//				} catch (Exception e) {
	//					// TODO: handle exception
	//				}
	//				double tax = taxValue + surchargeValue;
	//
	//				mrp = Double.parseDouble(resultSet.getObject(11).toString());
	//				if (formulaActive.equals("1")) {
	//					sp = price;
	//				} else {
	//					if (purchase >= 10000 && purchase <= 20000) {
	//						sp = purchase * 1.15;
	//					} else if (purchase > 20000 && purchase <= 30000) {
	//						sp = purchase * 1.10;
	//					}else if(purchase>30000){
	//						sp = purchase * 1.05;
	//					} else {
	//						double tempvar1 = mrp / packSize;
	//						double tempvar2 = tempvar1
	//								* ((taxValue + surchargeValue) / 100);
	//						double mrpwithouttax = tempvar1 - tempvar2;
	//						double temp = 2.5 * purchase;
	//						if (mrpwithouttax > temp) {
	//							sp = temp;
	//
	//						} else {
	//							double mrpless1prcnt = mrpwithouttax
	//									- (mrpwithouttax * 0.01);
	//							sp = mrpless1prcnt;
	//						}
	//
	//					}
	//				}
	//			}
	//
	//			price = (double) Math.round(sp * 100) / 100;
	//
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		itemsDBConnection.closeConnection();
	//		return price;
	//
	//	}


	public double getItemMRP(String index){

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		double mrpc=0;
		try {
			while (resultSet.next()) {
				mrpc =Double.parseDouble( resultSet.getObject(11).toString());
			}
		}catch(Exception e){

		}
		itemsDBConnection.closeConnection();
		return mrpc;

	}
	public double getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		//		double mrp = 0;
		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
		//		int packSize = 1;
		String formulaActive = "";
		String formulaApplied = "";
		try {
			while (resultSet.next()) {
				itemtypeSTR = resultSet.getObject(18).toString();
				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double
						.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13)
						.toString());
				purchase = Double
						.parseDouble(resultSet.getObject(20).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
				formulaApplied = resultSet.getObject(17).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(4)
							.toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;

				mrp = Double.parseDouble(resultSet.getObject(11).toString());
				if (formulaActive.equals("1")) {
					sp = price;
				} else {
					if (formulaApplied.equals("NA")) {
						if (purchase >= 10000 && purchase <= 20000) {


							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.15 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.10* purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.10;
						} else if (purchase > 30000) {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp =  1.05 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.05;
						} else if(purchase > 5000 && purchase <= 10000){
							System.out.println("5000"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.25 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if(purchase > 1000 && purchase <= 5000){
							System.out.println("1000"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.30 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
						else if(purchase > 250 && purchase <= 1000){
							System.out.println("250"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
						else if(purchase > 0 && purchase <= 250){
							System.out.println("0"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}else {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
					} else {
						System.out.println(formulaApplied+"forumaula"+" "+purchase+"purchase");
						sp = purchase * Double.parseDouble(formulaApplied);
					}

				}
			}

			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023

			price = (double) Math.round(sp * 100) / 100;
			double finalprice = 1.0 * price;

			double k = finalprice * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;
			double s = finalprice * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			finalprice = finalprice + k;
			finalprice = finalprice + s;

			price = Math.round(finalprice * 100.000) / 100.000;
			System.out.println("finalpricennnnnnnnn" + price);
			System.out.print("pricennng" + price);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		return price;

	}
	
	public void getIPDDATA() {

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
				advancePaymentTB.setText(ipd_advance + "");
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
				getIPDDoctors(ipd_id);
				populateExpensesTable(ipd_id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void getIPDDoctors(String ipd_id) {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataDoctor(ipd_id);
		try {
			while (resultSet.next()) {
				ipdDoctorTB.setText("" + resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void getItemStock(String index) {

		quantity = 0;
		String itemAvailable = "Not Available";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStock(index,
				departmentName);

		try {
			while (resultSet.next()) {

				quantity = Integer
						.parseInt((resultSet.getObject(1).toString()));
				itemAvailable = quantity + "";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		itemAvailableV.add(itemAvailable);
	}

	public void populateExpensesTable(String ipd_id) {

		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllData(ipd_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllData(ipd_id);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Description", "Amount" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			for (int i = 0; i < NumberOfRows; i++) {
				totalChargesIPD = totalChargesIPD
						+ Double.parseDouble(Rows_Object_Array[i][1].toString());
			}
			totalChargesIPD = Math.round(totalChargesIPD * 100.0) / 100.0;
			totalAmountTB.setText(totalChargesIPD + "");
			table.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(0).setMinWidth(150);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			ipd_balance = totalChargesIPD - ipd_advance;

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public void getAllProcedures() {
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		procedureName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				procedureIDV.add(resultSet.getObject(1).toString());
				procedureNameV.add(resultSet.getObject(2).toString());
				procedureName.addElement(resultSet.getObject(2).toString());
				procedurePriceV.add(resultSet.getObject(3).toString());
				procedurePreDaysV.add(resultSet.getObject(7).toString());
				procedurePostDaysV.add(resultSet.getObject(8).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		procedureNameCB.setModel(procedureName);
		if (i > 0) {
			procedureNameCB.setSelectedIndex(0);
		}
	}

	public JTextField getPostdaysLBL() {
		return postdaysLBL;
	}

	public JTextField getPredaysLBL() {
		return predaysLBL;
	}
}
