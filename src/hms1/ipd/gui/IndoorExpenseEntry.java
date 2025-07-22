package hms1.ipd.gui;

import hms.departments.database.DepartmentStockDBConnection;

import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
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

import javax.annotation.*;
import javax.swing.ButtonGroup;
import javax.swing.ComboBoxModel;
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

public class IndoorExpenseEntry extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB; 
	private JTextField telephoneTB;
	private JTextField ageTB;
	private JComboBox<String> batch_nameCB;
	private Timer timer;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();

	private HashMap examHashMap = new HashMap();
	String[] data = new String[21];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	double totalChargesIPD = 0;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	double mrp = 0;
	int packSize = 1;
	boolean flag=false;
	boolean stock_item=false;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemMRPV = new Vector<String>();
	Vector<String> itemMeasV = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<Boolean> stockItemV = new Vector<Boolean>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> itemTypeV = new Vector<String>();
	Vector<String> itemBTCH = new Vector<String>();
	Vector<String> itemEXP = new Vector<String>();
	Vector<String> batch_id = new Vector<String>();
	Vector<Boolean> flagV = new Vector<Boolean>();
	Vector<String> cmprname = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	double ipd_advance = 0, ipd_balance = 0;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String doctorName = "", doctorID = "", date = "", itemtypeSTR = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "", btch_name="",
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",btch_ID="",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR1 = "", charges = "",item_stock="",
			ipd_days, ipd_houres, ipd_minutes, ipd_expenses_id;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
	expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
	previouseStock = "", departmentName = "RECEPTION",batchIDSTR="",
	departmentID = "10",item_batch,Cash_Reqd;
	int qtyIssued = 0, afterIssued = 0, finalTaxValue = 0,
			finalDiscountValue = 0;
	int quantity = 0;
	double price = 0, finalTotalValue = 0, taxValue = 0, surchargeValue = 0,
			itemValue = 0;
	int f=0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel btchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();


	Vector examID = new Vector();
	Vector examRoom = new Vector();
	private JComboBox patientIDCB;

	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	private JComboBox comboBox;
	private JComboBox<String> itemNameCB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField enterQtyET;
	private JTextField itemSearchET;
	private JTextField itemDescET;
	private JTextField qtyInHandET;
	private JTextField remainingItemET;
	private JTextField expirtDateET;
	private JTextField priceET;
	private JTextField costET;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField ipdNoTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTable table;
	private JButton btnRemove;
	private JLabel estimatedTotalLBL;
	private JLabel ipdTotalLBL;
	private JTextField itemType;
	private JTextField stockTF;
	private JLabel lblPackageAmount;
	private JLabel packageAmountTF;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new IndoorExpenseEntry("", null).setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public IndoorExpenseEntry(final String patientIDSTR, final IPDBill ipdBill) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IndoorExpenseEntry.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Expense Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(150, 30, 927, 634);
		setModal(true);

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
		panel_4.setBounds(46, 0, 867, 601);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "IPD Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(0, 334, 327, 245);
		panel_4.add(panel_3);

		JLabel label = new JLabel("Doctor Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 54, 85, 14);
		panel_3.add(label);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(117, 50, 200, 25);
		panel_3.add(ipdDoctorTB);

		JLabel label_1 = new JLabel("Building :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 88, 108, 14);
		panel_3.add(label_1);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(117, 84, 200, 25);
		panel_3.add(ipdBuildingTB);

		JLabel label_2 = new JLabel("Ward Name :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 128, 108, 14);
		panel_3.add(label_2);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(117, 124, 200, 25);
		panel_3.add(ipdWardTB);

		JLabel label_3 = new JLabel("Bed No :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(575, 9, 108, 14);
		panel_3.add(label_3);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(675, 4, 182, 25);
		panel_3.add(ipdBedNoTB);

		JLabel label_4 = new JLabel("No. of Days :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(575, 48, 108, 14);
		panel_3.add(label_4);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(675, 43, 182, 25);
		panel_3.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(117, 18, 200, 25);
		panel_3.add(ipdNoTB);

		JLabel label_5 = new JLabel("IPD No :");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(10, 22, 91, 14);
		panel_3.add(label_5);

		JLabel label_6 = new JLabel("Total Amount :");
		label_6.setBounds(10, 164, 108, 14);
		panel_3.add(label_6);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(118, 164, 199, 25);
		panel_3.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		JLabel lblAdvancePay = new JLabel("Advance Pay :");
		lblAdvancePay.setBounds(10, 206, 99, 14);
		panel_3.add(lblAdvancePay);
		lblAdvancePay.setFont(new Font("Tahoma", Font.PLAIN, 12));

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(117, 201, 200, 25);
		panel_3.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(10, 0, 317, 325);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 106, 93, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 101, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 136, 77, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(6, 166, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(6, 264, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 198, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(6, 229, 46, 25);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(106, 131, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 161, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 259, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 194, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 229, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 229, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 297, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 289, 201, 25);
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

		patientIDCB = new JComboBox(patientIDWithaName);
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

				if(p_id.contains("(")){
					p_id = p_id.substring(0, p_id.indexOf("("));
					setPatientDetail(p_id);
				}

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
				if (!str.equals(""))  {
					getPatientsID(str);
				}else {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					lastOPDDateLB.setText("Last OPD :");
					// itemNameCB.setSelectedIndex(0);

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
				}


			}
		});
		searchPatientTB.getDocument().addDocumentListener( 
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();

					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}

				});
		searchPatientTB.requestFocus(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(339, 98, 515, 398);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 11, 494, 238);
		panel_2.add(panel_5);
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Item Detail :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12), null));

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		itemNameCB = new JComboBox<String>();
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}
				itemDescET.setText("");
				enterQtyET.setText("");
				remainingItemET.setText("");
				expirtDateET.setText("");
				stockTF.setText("");
				priceET.setText("");
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!flag) {
						getItemStock(itemIDSTR);
						expirtDateET.setText("" + expiryDateSTR);
						stockTF.setText("" + quantity+"");
						btch_name="N/A";
						batchIDSTR="0";
					}else
					{
						getItemBatchName(itemIDSTR);

					}
					afterIssued = quantity - qtyIssued;
					itemDescET.setText("" + itemDescSTR);
					enterQtyET.setText("");
					remainingItemET.setText("" + afterIssued);
					//expirtDateET.setText("" + expiryDateSTR);
					//	stockTF.setText("" + quantity);
					if(InsuranceMrpReqd(insuranceTypeTB.getText()))		
					{        
						priceET.setText("" + mrp);
					}
					else
					{

						priceET.setText("" + price);
					}
					if (itemtypeSTR.equals("General")) {
						itemType.setText(itemtypeSTR);
					} else {
						itemType.setText(itemtypeSTR);
					}
					if (itemtypeSTR.equals("High Risk")) {
						itemType.setBackground(Color.RED);
						itemType.setForeground(Color.BLACK);
					} else if (itemtypeSTR.equals("SHC-H1")) {
						itemType.setBackground(Color.GREEN);
						itemType.setForeground(Color.BLACK);
					} else {
						itemType.setBackground(Color.WHITE);
					}
				}
			}


		});
		itemNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		itemNameCB.setBounds(277, 16, 207, 25);
		panel_5.add(itemNameCB);

		enterQtyET = new JTextField();
		enterQtyET.setHorizontalAlignment(SwingConstants.RIGHT);
		enterQtyET.setBounds(347, 91, 137, 25);
		enterQtyET.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		enterQtyET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {
					enterQtyET.setText("");
				}
				afterIssued = quantity - qtyIssued;

				remainingItemET.setText("" + afterIssued);
				itemValue1();
				// costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				itemValue1();
				// costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				itemValue1();
				// costET.setText((qtyIssued * price) + "");
			}
		});
		panel_5.add(enterQtyET);
		enterQtyET.setColumns(10);

		JLabel lblAmount = new JLabel("Enter Qty.:");
		lblAmount.setBounds(277, 96, 75, 14);
		panel_5.add(lblAmount);

		itemSearchET = new JTextField();
		itemSearchET.setBounds(109, 17, 158, 25);
		panel_5.add(itemSearchET);
		itemSearchET.setColumns(10);

		itemSearchET.setColumns(10);
		itemSearchET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					stockTF.setText("");
					priceET.setText("");
					itemType.setText("");

					itemName.removeAllElements();
					btchName.removeAllElements();
					itemNameCB.setModel(itemName);
					comboBox.setModel(btchName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					stockTF.setText("");
					priceET.setText("");
					itemType.setText("");

					btchName.removeAllElements();
					itemName.removeAllElements();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					stockTF.setText("");
					priceET.setText("");
					itemType.setText("");

					itemName.removeAllElements();
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					comboBox.setModel(btchName);

				}
			}
		});

		JLabel lblSearchItem = new JLabel("Search Item :");
		lblSearchItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchItem.setBounds(16, 22, 89, 14);
		panel_5.add(lblSearchItem);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescription.setBounds(16, 53, 94, 25);
		panel_5.add(lblDescription);

		itemDescET = new JTextField();
		itemDescET.setEditable(false);
		itemDescET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		itemDescET.setBounds(109, 52, 205, 25);
		panel_5.add(itemDescET);
		itemDescET.setColumns(10);

		priceET = new JTextField();
		priceET.setEditable(false);
		priceET.setColumns(10);
		priceET.setBounds(109, 87, 158, 25);
		panel_5.add(priceET);

		JLabel lblPrice = new JLabel("Price per unit :");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(11, 85, 89, 25);
		panel_5.add(lblPrice);

		JLabel lblCost = new JLabel("Cost :");
		lblCost.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCost.setBounds(16, 123, 69, 25);
		panel_5.add(lblCost);

		costET = new JTextField();
		costET.setEditable(false);
		costET.setColumns(10);
		costET.setBounds(109, 123, 158, 25);
		panel_5.add(costET);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(110, 193, 125, 33);
		panel_5.add(btnAdd);
		btnAdd.setIcon(new ImageIcon(IndoorExpenseEntry.class
				.getResource("/icons/plus_button.png")));
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnRemove = new JButton("Remove");
		btnRemove.setBounds(252, 193, 125, 33);
		panel_5.add(btnRemove);
		btnRemove.setIcon(new ImageIcon(IndoorExpenseEntry.class
				.getResource("/icons/exit.png")));
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.setEnabled(false);

		JLabel lblItemType = new JLabel("Item Type");
		lblItemType.setBounds(277, 129, 75, 14);
		panel_5.add(lblItemType);

		itemType = new JTextField();
		itemType.setHorizontalAlignment(SwingConstants.RIGHT);
		itemType.setColumns(10);
		itemType.setBounds(347, 126, 137, 25);
		panel_5.add(itemType);
		itemType.setEditable(false);

		JLabel lblExpiryDate = new JLabel("Expiry Date :");
		lblExpiryDate.setBounds(16, 156, 100, 25);
		panel_5.add(lblExpiryDate);
		lblExpiryDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		expirtDateET = new JTextField();
		expirtDateET.setBounds(109, 160, 158, 25);
		panel_5.add(expirtDateET);
		expirtDateET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		expirtDateET.setEditable(false);
		expirtDateET.setColumns(10);

		batch_nameCB = new JComboBox<String>();
		batch_nameCB.setModel(new DefaultComboBoxModel(new String[] {"select Batch"}));
		batch_nameCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btch_name=batch_nameCB.getSelectedItem().toString();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getStackTrace();
				}
				if (batch_nameCB.getSelectedIndex() > -1) {
					batchIDSTR= batchID.get(batch_nameCB.getSelectedIndex());

				}
				expirtDateET.setText("");
				stockTF.setText("");
				enterQtyET.setText("");
				remainingItemET.setText("");
				if(btchName.getSize() >=0 && batch_nameCB.getSelectedItem()!=("select Batch"))
				{

					getItemStock(itemIDSTR,batchIDSTR);
					expirtDateET.setText("" + expiryDateSTR);
					stockTF.setText("" + quantity);
				}else {
					expirtDateET.setText("");
					stockTF.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
				}
			}

		});
		batch_nameCB.setBounds(326, 55, 158, 24);
		panel_5.add(batch_nameCB);

		stockTF = new JTextField();
		stockTF.setHorizontalAlignment(SwingConstants.RIGHT);
		stockTF.setEditable(false);
		stockTF.setColumns(10);
		stockTF.setBounds(347, 163, 137, 25);
		panel_5.add(stockTF);

		JLabel lblTotalStock = new JLabel("Stock");
		lblTotalStock.setFont(new Font("Dialog", Font.BOLD, 12));
		lblTotalStock.setBounds(304, 166, 48, 15);
		panel_5.add(lblTotalStock);

		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = addTestTable_1.getSelectedRow();
				cur_selectedRow = addTestTable_1
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = addTestTable_1.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

				itemIDV.remove(cur_selectedRow);
				stockItemV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				flagV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				itemPriceV.remove(cur_selectedRow);
				totalPriceV.remove(cur_selectedRow);
				itemEXP.remove(cur_selectedRow);
				itemBTCH.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {		
				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				String Itemcategory=db.retrieveItemCategory(itemIDSTR);
				db.closeConnection();
				if (itemDescET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!CheckInsuranceItem(itemIDSTR)) {
					JOptionPane.showMessageDialog(null,
							"You cannot add this item for "+insuranceTypeTB.getText()+" patients", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (!(Itemcategory.toLowerCase().contains("charge") || Itemcategory.toLowerCase().contains("procedur"))  && (flag && batch_nameCB.getSelectedItem().equals("select Batch"))) {
					JOptionPane.showMessageDialog(null,
							"Please select batch Name!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}  
				if (enterQtyET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				if (!(Itemcategory.toLowerCase().contains("charge") || Itemcategory.toLowerCase().contains("procedur")) && Double.parseDouble(remainingItemET.getText().toString())<0 ) {
					JOptionPane.showMessageDialog(null,
							"Please Check Qunatity Issued!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 

				if (itemIDV.indexOf(itemIDSTR) != -1 && flag && batch_id.indexOf(batchIDSTR)!=-1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if(itemIDV.indexOf(itemIDSTR) != -1 && !flag ) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				insucheck();
			}

			private void insucheck() {
				// TODO Auto-generated method stub
				if(InsuranceMrpReqd(insuranceTypeTB.getText()))
				{
					boolean bool = false;
					DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
					ResultSet resultSet = departmentStockDBConnection.getalloweditem(itemIDSTR, insuranceTypeTB.getText().toString());
					try {
						while (resultSet.next()) {
							bool=resultSet.getBoolean(1);
						}
						if(bool)
						{
							int a=JOptionPane.showConfirmDialog( null, "This Item is Not Incuded in Insurance Items!"+"\n"+"Click 'YES' To ADD Forcefully.");  
							if(a==JOptionPane.YES_OPTION){  
								itemIDV.add(itemIDSTR);
								itemNameV.add(itemNameSTR);
								stockItemV.add(stock_item);
								flagV.add(flag);
								itemBTCH.add(btch_name);
								itemEXP.add(expiryDateSTR);
								batch_id.add(batchIDSTR);
								itemDescV.add(itemDescSTR);
								issuedQtyV.add(qtyIssued + "");
								itemPriceV.add(price + "");
								totalPriceV.add(itemValue + "");
								itemMRPV.add(mrp + "");
								itemMeasV.add(packSize + "");
								itemTypeV.add(itemtypeSTR + "");
								loadDataToTable();
								itemSearchET.setText("");
								Cash_Reqd="1";
							} 
							else
							{
								itemSearchET.setText("");
								Cash_Reqd="0";
							}
						}
						else {
							itemIDV.add(itemIDSTR);
							stockItemV.add(stock_item);
							itemNameV.add(itemNameSTR);
							itemBTCH.add(btch_name);
							flagV.add(flag);
							itemEXP.add(expiryDateSTR);
							batch_id.add(batchIDSTR);
							itemDescV.add(itemDescSTR);
							issuedQtyV.add(qtyIssued + "");
							if(InsuranceMrpReqd(insuranceTypeTB.getText()))
							{
								itemPriceV.add(mrp + "");
							}
							else
								itemPriceV.add(price + "");
							totalPriceV.add(itemValue + "");
							itemMRPV.add(mrp + "");
							itemMeasV.add(packSize + "");
							itemTypeV.add(itemtypeSTR + "");
							// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
							// / 100 + "");
							loadDataToTable();
							itemSearchET.setText("");
							Cash_Reqd="0";

						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}
				else {

					itemIDV.add(itemIDSTR);
					flagV.add(flag);
					itemNameV.add(itemNameSTR);
					stockItemV.add(stock_item);
					itemBTCH.add(btch_name);
					itemEXP.add(expiryDateSTR);
					batch_id.add(batchIDSTR);
					itemDescV.add(itemDescSTR);
					issuedQtyV.add(qtyIssued + "");
					if(InsuranceMrpReqd(insuranceTypeTB.getText()))
					{
						itemPriceV.add(mrp + "");
					}
					else
						itemPriceV.add(price + "");
					totalPriceV.add(itemValue + "");
					itemMRPV.add(mrp + "");
					itemMeasV.add(packSize + "");
					itemTypeV.add(itemtypeSTR + "");
					// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
					// / 100 + "");
					loadDataToTable();
					itemSearchET.setText("");
					Cash_Reqd="0";
				}
			}


		});

		JLabel lblQtyInhand = new JLabel("Qty. In Hand :");
		lblQtyInhand.setBounds(548, 65, 100, 25);
		panel_2.add(lblQtyInhand);
		lblQtyInhand.setFont(new Font("Tahoma", Font.PLAIN, 12));
		JLabel lblAfterEntry = new JLabel("After Entry :");
		lblAfterEntry.setBounds(790, 64, -142, 25);
		panel_2.add(lblAfterEntry);
		lblAfterEntry.setFont(new Font("Tahoma", Font.PLAIN, 12));
		remainingItemET = new JTextField();
		remainingItemET.setBounds(888, 65, -240, 25);
		panel_2.add(remainingItemET);
		remainingItemET.setEditable(false);
		remainingItemET.setColumns(10);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 261, 494, 125);
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
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(IndoorExpenseEntry.class
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
							"This patient is not an indoor patient. ",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (itemID.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					Dept_PillsRegisterDBConnection dept_PillsRegisterDBConnection = new Dept_PillsRegisterDBConnection();
					DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
					data[0] = "" + departmentID; // dept id
					data[1] = departmentName; // dept name
					data[2] = "" + doctorID; // user id
					data[3] = doctorName; // user name
					data[4] = ReceptionMain.receptionIdSTR; // user id
					data[5] = ReceptionMain.receptionNameSTR; // user name
					data[6] = p_id;
					data[7] = p_name;
					data[14] = date;
					data[15] = "" + timeFormat.format(cal1.getTime());
					data[16] = "" + ipd_id; // ipd no
					data[17] = "INDOOR";
					data[18] = "OK";
					for (int i = 0; i < itemIDV.size(); i++) {
						data[8] = itemIDV.get(i);
						data[9] = "" + itemNameV.get(i);
						data[10] = itemDescV.get(i);
						data[11] = itemPriceV.get(i);
						data[12] = issuedQtyV.get(i);
						data[13] = totalPriceV.get(i);		
						String str = itemBTCH.get(i);
						String[] arrOfStr = str.split("\\(", 2);
						for (String a : arrOfStr)
						{
							data[19] = a;
							break;
						}
						data[20] = 	batch_id.get(i);
						try {
							dept_PillsRegisterDBConnection
							.insertDepartmentPillsRegister(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						if (flagV.get(i) && !itemNameV.get(i).toLowerCase().contains("charge")) {
							try {
								departmentStockDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i),
										departmentName, batch_id.get(i));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else {
							try {
								departmentStockDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i),
										departmentName);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} 

					}
					departmentStockDBConnection.closeConnection();
					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[22];
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
						data1[12] = ReceptionMain.receptionNameSTR;
						data1[13] = db.retrieveItemCategory(itemIDV.get(i));
						data1[14] = "N/A";
						data1[15] = itemMRPV.get(i);
						data1[16] = itemMeasV.get(i);
						data1[17] = itemTypeV.get(i);
						data1[18] = batch_id.get(i);
						String str = itemBTCH.get(i);
						String[] arrOfStr = str.split("\\(", 2);
						for (String a : arrOfStr)
						{
							data1[19] = a;
							break;
						}
						data1[20]="D";
						data1[21]=Cash_Reqd;
						// System.out.print("ggggggggggggggg"+ Cash_Reqd);
						try {
							db.inserDataNewExpenses(data1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
					}
					db.closeConnection();
					double tot = totalChargesIPD + finalTotalValue;
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
					dept_PillsRegisterDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);

					if (ipdBill != null) {
						ipdBill.populateExpensesTable(ipd_id);
					}
					dispose();
				}
			}
		});

		btnNewButton_3.setBounds(624, 535, 112, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(742, 535, 112, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IndoorExpenseEntry.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(337, 11, 517, 75);
		panel_4.add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		JLabel label_8 = new JLabel("");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setIcon(new ImageIcon(IndoorExpenseEntry.class
				.getResource("/icons/graphics-hospitals-575145.gif")));
		label_8.setBounds(1021, 365, 103, 105);
		panel_4.add(label_8);

		JLabel lblTotalCharges = new JLabel("Item Charges :");
		lblTotalCharges.setBounds(349, 507, 109, 22);
		panel_4.add(lblTotalCharges);
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setBounds(465, 507, 98, 20);
		panel_4.add(lblTotalcharges);
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblIpdTotal = new JLabel("IPD Total :");
		lblIpdTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIpdTotal.setBounds(349, 535, 109, 22);
		panel_4.add(lblIpdTotal);

		ipdTotalLBL = new JLabel("");
		ipdTotalLBL.setFont(new Font("Tahoma", Font.PLAIN, 14));
		ipdTotalLBL.setBounds(465, 535, 98, 20);
		panel_4.add(ipdTotalLBL);

		JLabel lblEstimatedTotal = new JLabel("Estimated Total :");
		lblEstimatedTotal.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEstimatedTotal.setBounds(349, 557, 109, 22);
		panel_4.add(lblEstimatedTotal);

		estimatedTotalLBL = new JLabel("");
		estimatedTotalLBL.setFont(new Font("Tahoma", Font.PLAIN, 14));
		estimatedTotalLBL.setBounds(465, 557, 98, 20);
		panel_4.add(estimatedTotalLBL);

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

		searchPatientTB.setText(patientIDSTR);

		packageAmountTF = new JLabel("");
		packageAmountTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		packageAmountTF.setBounds(692, 509, 98, 20);
		panel_4.add(packageAmountTF);

		lblPackageAmount = new JLabel("Package Amount :");
		lblPackageAmount.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPackageAmount.setBounds(562, 508, 137, 22);
		panel_4.add(lblPackageAmount);
	}

	private void name() {
		// TODO Auto-generated method stub
		itemIDV.add(itemIDSTR);

		itemNameV.add(itemNameSTR);
		itemBTCH.add(btch_name.equals("select Batch")?"":btch_name);
		itemEXP.add(expiryDateSTR);
		batch_id.add(batchIDSTR);
		itemDescV.add(itemDescSTR);
		issuedQtyV.add(qtyIssued + "");
		if(InsuranceMrpReqd(insuranceTypeTB.getText()))
		{
			itemPriceV.add(mrp + "");
		}
		else
			itemPriceV.add(price + "");
		totalPriceV.add(itemValue + "");
		itemMRPV.add(mrp + "");
		itemMeasV.add(packSize + "");
		itemTypeV.add(itemtypeSTR + "");
		// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
		// / 100 + "");
		loadDataToTable();
		itemSearchET.setText("");
		Cash_Reqd="0";
	}
	private boolean InsuranceMrpReqd(String Ins) {
		// TODO Auto-generated method stub
		InsuranceDBConnection InsuranceDBConnection=new InsuranceDBConnection();
		ResultSet rs=InsuranceDBConnection.InsMrpReqdOrNot(Ins);
		boolean r=false;
		try {
			while(rs.next()) {
				r=rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InsuranceDBConnection.closeConnection();
		return r;
	}
	public void getPatientsID(String index) {
		lastOPDDateLB.setText("Last Exam : ");
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.searchPatientWithIdOrNmaeNew(index);
		patientID.removeAllElements();
		patientIDWithaName.removeAllElements();
		try {
			while (resultSet.next()) {
				patientID.addElement(resultSet.getObject(1).toString());
				patientIDWithaName.addElement(resultSet.getObject(1).toString()+"("+resultSet.getObject(2).toString()+")");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		patientIDCB.setModel(patientIDWithaName);
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
			ObjectArray_ListOfexamsSpecs[i][5] = itemEXP.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = itemBTCH.get(i)=="select Batch"?"":itemBTCH.get(i);
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		addTestTable_1.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Desc.", "Issued Qty.", "Cost","Item Exp","Item BTCH" }));
		addTestTable_1.getColumnModel().getColumn(0).setMinWidth(75);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(120);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(120);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(4).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(4).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(5).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(5).setMinWidth(100);


		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		addTestTable_1.getColumnModel().getColumn(3)
		.setCellRenderer(centerRenderer);
		addTestTable_1.getColumnModel().getColumn(4)
		.setCellRenderer(centerRenderer);

		finalTotalValue = total;
		ipdTotalLBL.setText(totalChargesIPD + "");
		lblTotalcharges.setText(finalTotalValue + "");
		estimatedTotalLBL.setText((totalChargesIPD + finalTotalValue) + "");
		btnRemove.setEnabled(false);
	}

	public void getItemName(String index) {

		//	getItemEXP_BTCH(index);
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);

		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}

	public void itemValue() {

		if(InsuranceMrpReqd(insuranceTypeTB.getText()))
		{
			itemValue=qtyIssued * mrp;
		}
		else {
			itemValue = qtyIssued * price;
			// System.out.println("unit" + itemValue);
			double k = itemValue * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;
			double s = itemValue * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			itemValue = itemValue + k;
			itemValue = itemValue + s;

			itemValue = Math.round(itemValue * 100.000) / 100.000;
		}
		costET.setText(itemValue + "");

	}

	public void itemValue1() {
		if(InsuranceMrpReqd(insuranceTypeTB.getText()))
		{
			itemValue=qtyIssued * mrp;
		}
		else {


			itemValue = qtyIssued * price;
			// System.out.println("unit" + itemValue);

			itemValue = Math.round(itemValue * 100.000) / 100.000;
		}
		costET.setText(itemValue + "");

	}

	public void getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);

		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
		flag=false;
		stock_item=false;
		String formulaActive = "";
		try {
			while (resultSet.next()) {
				flag=Boolean.parseBoolean(resultSet.getObject(19).toString());
				if(flag)
				{
					batch_nameCB.setEnabled(true);
				}else
				{
					batch_nameCB.setEnabled(false);
				}
				stock_item=resultSet.getString(16).equals("si")?true:false;
				itemtypeSTR = resultSet.getObject(18).toString();
				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double
						.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13)
						.toString());
				purchase = Double
						.parseDouble(resultSet.getObject(7).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
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
						// sp = purchase * 1.15;
					} else if (purchase > 20000 && purchase <= 30000) {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1
								* ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.10 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax
									- (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.10;
					} else if (purchase > 30000) {
						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1
								* ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.05 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax
									- (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.05;
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
				}
			}

			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023

			price = (double) Math.round(sp * 100) / 100;
			System.out.println(price + "fhgfhg");
			double k = price * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;
			double s = price * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			price = price + k;
			price = price + s;

			price = Math.round(price * 100.000) / 100.000;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}
	public void getItemBatchName(String item_id) {

		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.getBatchName(item_id,departmentID);
		btchName.removeAllElements();
		batchID.clear();
		batchID.add("0");
		btchName.addElement("select Batch");
		int i = 0;
		try {
			while (resultSet.next()) {

				batchID.add(resultSet.getString(3));
				btchName.addElement(resultSet.getString(1)+"(Batch-"+(i+1)+")");

				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		batch_nameCB.setModel(btchName);
		if (i > 0) {
			batch_nameCB.setSelectedIndex(0);
		}
	} 
	public void getItemStock(String index,String batch_id) {

		double quantity1 = 0;
		expiryDateSTR="";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt(index,
				departmentName,batch_id);
		try {
			while (resultSet.next()) {
				Object obj=resultSet.getObject(1);
				Object obj1=resultSet.getObject(2);
				quantity1 = Double.parseDouble((obj==null)?"":resultSet.getObject(1).toString());
				expiryDateSTR = (obj1==null)?"":resultSet.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		quantity=(int)quantity1;
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
				packageAmountTF.setText("" + resultSet.getObject(13));
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

	public boolean CheckInsuranceItem(String itemID) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.GetInsuranceItem(itemID);
		try {
			while(resultSet.next()) {
				int o=resultSet.getInt(1);
				if(o==0) {
					return true;
				}else if(o==1){
					if(p_insurancetype.equals("Unknown") || p_insurancetype.equals("Ayushman Bharat")) {
						return true;
					}else {
						return false;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		return false;
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
		expiryDateSTR="";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt1(index,
				departmentName);
		try {
			while (resultSet.next()) { 
				Object obj=resultSet.getObject(1);
				Object obj1=resultSet.getObject(2);
				quantity =(int)Double.parseDouble((obj==null)?"0":resultSet.getObject(1).toString());
				expiryDateSTR = (obj1==null)?"":resultSet.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}

	//	private int check() {
	//		// TODO Auto-generated method stub
	//		BatchTrackingDBConnection db= new BatchTrackingDBConnection();
	//		ResultSet rs=db.check(itemIDSTR);
	//		try {
	//			while(rs.next()) {
	//				 f=Integer.parseInt(rs.getObject(1).toString());
	//			}
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		db.closeConnection();
	//		return f;
	//	
	//	}
	public void populateExpensesTable(String ipd_id) {

		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllData(ipd_id);

			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllData(ipd_id);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Object Rowss_Object_Array[];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
			Rowss_Object_Array = new Object[NumberOfRows];
			int R = 0;
			while (rs.next()) {

				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
					//System.out.print(Rows_Object_Array[R][C - 2]+"dddddddddddddddddddddd");

				}
				cmprname.add(Rows_Object_Array[R][0].toString());

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
}
