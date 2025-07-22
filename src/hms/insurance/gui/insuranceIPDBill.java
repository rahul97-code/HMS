package hms.insurance.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.exams.gui.AllExamsDetail;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;

import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.AdvancePaymentSlippdf;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.patient.slippdf.InsuranceIpdPdfSlip;
import hms.reception.gui.ReceptionMain;
import hms.store.database.ProceduresDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.Box;
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
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Choice;
import javax.swing.ComboBoxModel;
import java.awt.Checkbox;
import java.awt.Color;

public class insuranceIPDBill extends JDialog {

	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;

	double CATHETERIZATION_PRICE=0,OXYGEN_PRICE=0;
	String CATHETERIZATION_CODE="",OXYGEN_CODE="";
	private String exam_open_password="";
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField attendanceDateTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Timer timer;
	private String discharge_type="";
	String ref_ipd_id="";
	private JTextField insuranceTypeTB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	int selectedRowIndex,last=-1;
	ButtonGroup opdTypeGP = new ButtonGroup();
	boolean sflag =false;
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown",Recidate, p_note = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",claim_id,status,
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id,date="",first_start_time="",last_end_time="",
			package_id = "N/A", p_scheme = "",submitted_amount="",rs_amount="",subdate="",recvdate="",ipd_referrence="";
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	Vector<String> ITEM_NAME = new Vector<String>();
	Vector<String> ITEM_DESC = new Vector<String>();
	Vector<String> DATE = new Vector<String>();
	Vector<String> TYPE = new Vector<String>();
	Vector<String> ITEM_ID = new Vector<String>();
	Vector<String> PAGE_NO = new Vector<String>();
	Vector<String> MRP = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE = new Vector<Double>();
	Vector<String> QTY = new Vector<String>();
	Vector<Double> AMOUNT = new Vector<Double>();
	Vector<String> BATCH = new Vector<String>();
	Vector<String> EXPENSE_ID = new Vector<String>();
	Vector<String> EXPIRY = new Vector<String>();

	Vector<Integer> PACKAGE_DAYS = new Vector<Integer>();
	Vector<String> PACKAGE_DATES = new Vector<String>();
	Vector<String> PACKAGE_AMOUNT_REMOVE_DATES = new Vector<String>();	

	Vector<String> Dates1 = new Vector<String>();
	Vector<String> wardV1 = new Vector<String>();
	Vector<Double> chargeV1=new Vector<Double>();

	Vector<String> Dates=new Vector<String>();
	//Vector<String> Dates1=new Vector<String>();
	Vector<String> Diffhours=new Vector<String>();
	Vector<Integer> removeIndex=new Vector<Integer>();

	double REBATE = 0;
	String  consultant="",ipd_discharge_date="";
	boolean isdraft=false;
	ArrayList paymentCharges = new ArrayList();
	Vector<String> expensesIndexVector = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale; 
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTable table;
	private JTextField amountTB;
	private JTextField ipdNoTB;
	Vector<String> itemsCombo = new Vector<String>();
	private JTextField claim_idtf;
	private JTextField checktf;
	String[] data = new String[10];
	private JDateChooser recieveddateDC;
	private JButton submitbtn;
	private JCheckBox chckbxObjection;
	private JTextField categoryTF;
	private JRadioButton rdbtnReferral;
	private JRadioButton rdbtnNonRef;
	private String insurance_category="";
	private Object[][] ObjectArray_ListOfexamsSpecs;
	private JButton btnOpenBill;
	private JCheckBox chckbxRebate;

	private JButton btnAdjustmentBtn;

	public JButton searchBT;
	private JLabel lblEnterPayment_1;
	private JTextField Urn_NumberTF;

	private JComboBox patientIDCB_1;
	private JComboBox dischargeCB;

	private JCheckBox checkbox;

	private JCheckBox chckbxOxygenCharge;

	private JCheckBox chckbxCatherization;

	private Object Dischargetype;

	private JButton SaveBtn;
	private String DischargeType;
	private JLabel lblCashPatient;
	public static void main(String[] arg) {
		new insuranceIPDBill("77654").setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public insuranceIPDBill(String string) {
		this.ref_ipd_id=string;
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				insuranceIPDBill.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Insurance IPD Aproved Bill");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1176, 527);

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
		date = DateFormatChange.StringToMysqlDate(new Date());
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_4.setBounds(6, 0, 1158, 478);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		JLabel lblAttendenceDate = new JLabel("Date");
		lblAttendenceDate.setBounds(20, 19, 51, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";

				if (!str.equals("")) {
					getPatientsID(str);
				} else {
					recieveddateDC.setDate(new Date());
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					claim_idtf.setEnabled(false);
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					ipdNoTB.setText("");
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable();
					claim_idtf.setText("");
					advancePaymentTB.setEditable(true);

				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setEditable(false);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(512, 14, 139, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

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

		searchPatientTB.setText(string);
		searchBT = new JButton("");
		searchBT.setToolTipText("Search");
		searchBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
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
					claim_idtf.setEnabled(false);
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdNoTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable();
					advancePaymentTB.setEditable(true);
				}
			}
		});
		searchBT.setBounds(651, 13, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(insuranceIPDBill.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(78, 14, 108, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JPanel panel = new JPanel();
		// panel.setBounds(699, 44, 313, 259);
		panel.setBounds(833, 44, 313, 371);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 21, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 16, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 57, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(6, 93, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(6, 129, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 164, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(16, 196, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(106, 52, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 88, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 124, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 160, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 192, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 192, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 218, 108, 26);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 219, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);

		claim_idtf = new JTextField();
		claim_idtf.setBounds(106, 256, 201, 25);
		panel.add(claim_idtf);
		claim_idtf.setColumns(10);

		JLabel claim_idlbl = new JLabel("Claim ID :");
		claim_idlbl.setFont(new Font("Dialog", Font.PLAIN, 12));
		claim_idlbl.setBounds(16, 261, 70, 15);
		panel.add(claim_idlbl);

		rdbtnReferral = new JRadioButton("Referral");
		rdbtnReferral.setFont(new Font("Dialog", Font.PLAIN, 14));
		rdbtnReferral.setEnabled(false);
		rdbtnReferral.setBounds(33, 327, 80, 23);
		panel.add(rdbtnReferral);

		rdbtnNonRef = new JRadioButton("Non Referral");
		rdbtnNonRef.setFont(new Font("Dialog", Font.PLAIN, 14));
		rdbtnNonRef.setEnabled(false);
		rdbtnNonRef.setBounds(115, 327, 140, 23);
		panel.add(rdbtnNonRef);

		categoryTF = new JTextField();
		categoryTF.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {

				}
			}
		});
		categoryTF.setEditable(false);
		categoryTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		categoryTF.setColumns(10);
		categoryTF.setBounds(106, 294, 201, 25);
		panel.add(categoryTF);

		JLabel claim_idlbl_1 = new JLabel("Ins Category :");
		claim_idlbl_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		claim_idlbl_1.setBounds(6, 299, 93, 15);
		panel.add(claim_idlbl_1);


		Choice choice = new Choice();
		choice.setBounds(106, 294, 201, 25);
		choice.add("cteg");
		panel.add(choice);
		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(699, 16, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 51, 501, 364);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 12, 479, 344);
		panel_2.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, }, new String[] {
						"Description","Type","Date","Rate","Qty", "Amount" }));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
					}
				});
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setMinWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setMinWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setMinWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		scrollPane.setViewportView(table);

		patientIDCB = new JComboBox(patientIDWithaName);
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
				ipdDoctorTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				if(p_id.contains("(")){
					p_id = p_id.substring(0, p_id.indexOf("("));
					setPatientDetail(p_id);
				}
				if (patientIDWithaName.getSize() > 0) {
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

				}
			}
		});

		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(794, 11, 192, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(513, 51, 313, 147);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(10, 46, 108, 14);
		panel_7.add(lblDoctorName);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_7.add(ipdDoctorTB);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuilding.setBounds(10, 77, 108, 14);
		panel_7.add(lblBuilding);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_7.add(ipdBuildingTB);

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(10, 113, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_7.add(ipdWardTB);

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

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Bill",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(512, 203, 313, 212);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(10, 17, 108, 14);
		panel_1.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(153, 12, 147, 25);
		panel_1.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(152, 49, 148, 25);
		panel_1.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setColumns(10);

		amountTB = new JTextField("");
		amountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		amountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		amountTB.setColumns(10);
		amountTB.setBounds(153, 79, 148, 25);
		panel_1.add(amountTB);
		JLabel lblEnterPayment = new JLabel("Received Payment :");
		lblEnterPayment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterPayment.setBounds(10, 84, 136, 14);
		panel_1.add(lblEnterPayment);

		chckbxObjection = new JCheckBox("Objection Raised");
		chckbxObjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxObjection.isSelected()) {
					checktf.setEnabled(true);
				}
				else {
					checktf.setEnabled(false);
				}
			}
		});
		chckbxObjection.setFont(new Font("Dialog", Font.PLAIN, 12));
		chckbxObjection.setBounds(10, 139, 121, 23);
		panel_1.add(chckbxObjection);

		checktf = new JTextField();
		checktf.setEnabled(false);
		checktf.setBounds(153, 140, 147, 25);
		panel_1.add(checktf);
		checktf.setColumns(10);

		recieveddateDC = new JDateChooser();
		recieveddateDC.setBounds(153, 173, 147, 25);
		panel_1.add(recieveddateDC);
		recieveddateDC.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {

					Recidate = DateFormatChange
							.StringToMysqlDate((Date) arg0
									.getNewValue());
					System.out.print(Recidate+"rahulrrrrrrr");
				}
			}

		});
		recieveddateDC.setDateFormatString("yyyy-MM-dd");
		JLabel lblNote1 = new JLabel("Recieved Date :");
		lblNote1.setBounds(20, 175, 108, 23);
		recieveddateDC.setMaxSelectableDate(new Date());
		panel_1.add(lblNote1);
		lblNote1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblEnterPayment_1 = new JLabel("UTR Number :");
		lblEnterPayment_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEnterPayment_1.setBounds(10, 117, 109, 14);
		panel_1.add(lblEnterPayment_1);

		Urn_NumberTF = new JTextField("");
		Urn_NumberTF.setHorizontalAlignment(SwingConstants.TRAILING);
		Urn_NumberTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		Urn_NumberTF.setColumns(10);
		Urn_NumberTF.setBounds(153, 111, 148, 25);
		panel_1.add(Urn_NumberTF);

		checkbox = new JCheckBox("Submit Amount");
		checkbox.setFont(new Font("Dialog", Font.PLAIN, 12));
		checkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(checkbox.isSelected())
					sflag=true;
				else
					sflag=false;
			}
		});
		checkbox.setBounds(15, 49, 120, 23);
		panel_1.add(checkbox);

		submitbtn = new JButton("SUBMIT");
		submitbtn.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/ok_button.png")));
		submitbtn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				if(ipdNoTB.getText().toString().equals(""))
				{

					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(advancePaymentTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"please enter Submit amount", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(discharge_type.equals("Select Type"))
				{
					JOptionPane.showMessageDialog(null,
							"Select Discharge Type", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(status.equals("SUBMITTED") && amountTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"please enter receive amount", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				Date date1;
				date1 = recieveddateDC.getDate();
				if(date1==null && (status.equals("CLOSED") || status.equals("CLOSED") || status.equals("CLOSED(OBJ)")))
				{
					JOptionPane.showMessageDialog(null, "Choose Date from Right Box.", "Error", JOptionPane.ERROR_MESSAGE);
					recieveddateDC.grabFocus();
					return;
				}
				InsuranceDBConnection db = new InsuranceDBConnection();
				System.out.println(amountTB.getText().toString()+"gggggggggggg"); 
				data[0] = ipdNoTB.getText().toString(); // dept name
				data[1] = "" + insuranceTypeTB.getText(); // user id
				data[2] =  totalAmountTB.getText().toString();// user name
				data[3] = advancePaymentTB.getText().toString(); // user id
				data[4] = amountTB.getText().toString().equals("")?"0.0":amountTB.getText().toString();// user name
				data[5] = checktf.getText().toString();
				data[6] = date;
				data[7] = ReceptionMain.receptionNameSTR;
				data[8] = Urn_NumberTF.getText()+"";
				//				data[8] = Recidate;
				if(status.equals("SUBMITTED") || status.equals("CLOSED") || status.equals("CLOSED(OBJ)") || status.equals("") ) {
					try {
						data[0] = ipdNoTB.getText().toString(); // dept name
						data[1] = amountTB.getText().toString();; // user id
						data[2] = checktf.getText().toString();
						data[3] = Recidate;
						data[4] = ReceptionMain.receptionNameSTR;
						data[8] =  Urn_NumberTF.getText()+"";
						data[9] = advancePaymentTB.getText().toString();
						db.updatedatatracking(data);		
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else {
					try {
						db.insertdatatracking(data);

					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				IPDDBConnection db11 = new IPDDBConnection();
				db11.UpdateClaimIDDichargeType(claim_idtf.getText().toString(), ipdNoTB.getText().toString(),discharge_type);
				db11.closeConnection();
				if(ipdNoTB.getText().toString()!="") {
					if((status.equals("SUBMITTED") || status.equals("CLOSED"))&& !chckbxObjection.isSelected())
					{
						db.Updatestatustracking( "CLOSED",ipdNoTB.getText().toString());
					}
					else if(status.equals("CLOSED") && chckbxObjection.isSelected())
					{
						db.Updatestatustracking( "CLOSED(OBJ)",ipdNoTB.getText().toString());
					}
					else if(sflag)
					{
						db.Updatestatustracking( "SUBMITTED",ipdNoTB.getText().toString());

					}
					else if(!sflag)
					{
						db.Updatestatustracking( "",ipdNoTB.getText().toString());
					}
				}
				JOptionPane.showMessageDialog(null,
						"Data Successfully Updated", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		submitbtn.setBounds(439, 428, 131, 34);
		panel_4.add(submitbtn);

		JButton cancelbtn = new JButton("CANCEL");
		cancelbtn.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/CANCEL.PNG")));
		cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelbtn.setBounds(10, 429, 131, 32);
		panel_4.add(cancelbtn);

		btnOpenBill = new JButton("OPEN BILL");
		btnOpenBill.setEnabled(false);
		btnOpenBill.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/inventory.png")));
		btnOpenBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new InsuranceIpdPdfSlip(ipd_id, ipd_id, ipdDoctorTB
							.getText(),
							ITEM_NAME ,
							ITEM_DESC,
							DATE ,
							TYPE ,
							ITEM_ID,
							PAGE_NO ,
							MRP ,
							PER_ITEM_PRICE, 
							QTY ,
							AMOUNT, 
							BATCH ,
							EXPIRY ,
							REBATE
							);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});

		btnOpenBill.setBounds(153, 428, 131, 34);
		panel_4.add(btnOpenBill);

		chckbxRebate = new JCheckBox("Rebate()");
		chckbxRebate.setBounds(747, 451, 116, 22);
		panel_4.add(chckbxRebate);

		chckbxOxygenCharge = new JCheckBox("Oxygen Charge");
		//chckbxOxygenCharge.setSelected(true);
		chckbxOxygenCharge.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxOxygenCharge.isSelected()) {
					GetOxygenAndCatheterizationData();
					String hour = JOptionPane.showInputDialog(null, "Enter Oxygen Hours:");
					ITEM_NAME.add("Oxygen Charges");
					DATE.add("");
					TYPE.add("Z");
					ITEM_ID.add("N/A");
					PAGE_NO.add("N/A");
					MRP.add("0");
					PER_ITEM_PRICE.add(OXYGEN_PRICE);
					QTY.add(hour);
					AMOUNT.add((OXYGEN_PRICE * Integer.parseInt(hour)));
					BATCH.add("");
					EXPIRY.add("");
					EXPENSE_ID.add(null);
					ITEM_DESC.add("Oxygen Charges");
				}else {
					int i=ITEM_DESC.indexOf("Oxygen Charges");
					if (isdraft) {
						IPDExpensesDBConnection db = new IPDExpensesDBConnection();
						try {
							db.deleterowtracking(ipd_id, EXPENSE_ID.get(i));
							db.closeConnection();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
					ITEM_DESC.remove(i);
					TYPE.remove(i);
					ITEM_NAME.remove(i);
					DATE.remove(i);
					ITEM_ID.remove(i);
					PAGE_NO.remove(i);
					MRP.remove(i);
					PER_ITEM_PRICE.remove(i);
					QTY.remove(i);
					AMOUNT.remove(i);
					BATCH.remove(i);
					EXPENSE_ID.remove(i);
					EXPIRY.remove(i);


				}
				populateExpensesTable();
			}
		});
		chckbxOxygenCharge.setBounds(599, 451, 144, 23);
		panel_4.add(chckbxOxygenCharge);

		chckbxCatherization = new JCheckBox("Catheterization");
		chckbxCatherization.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (chckbxCatherization.isSelected()) {
					GetOxygenAndCatheterizationData();
					String qty = JOptionPane.showInputDialog(null, "Enter Catheterization Qty:");
					ITEM_NAME.add("Catheterisation Charges");
					DATE.add("");
					TYPE.add("Z");
					ITEM_ID.add("N/A");
					PAGE_NO.add("N/A");
					MRP.add("0");
					PER_ITEM_PRICE.add(CATHETERIZATION_PRICE);
					QTY.add(Integer.parseInt(qty) + "");
					AMOUNT.add((Integer.parseInt(qty) * CATHETERIZATION_PRICE));
					BATCH.add("");
					EXPIRY.add("");
					EXPENSE_ID.add(null);
					ITEM_DESC.add("Catheterisation Charges");
				}else {
					int i=ITEM_DESC.indexOf("Catheterisation Charges");
					IPDExpensesDBConnection db= new IPDExpensesDBConnection();
					try {
						db.deleterowtracking(ipd_id, EXPENSE_ID.get(i));
						db.closeConnection();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					TYPE.remove(i);
					ITEM_NAME.remove(i);
					DATE.remove(i);
					ITEM_ID.remove(i);
					PAGE_NO.remove(i);
					MRP.remove(i);
					PER_ITEM_PRICE.remove(i);
					QTY.remove(i);
					AMOUNT.remove(i);
					BATCH.remove(i);
					EXPIRY.remove(i);
					EXPENSE_ID.remove(i);
					ITEM_DESC.remove(i);
				}	
				populateExpensesTable();
			}
		});
		chckbxCatherization.setBounds(599, 423, 144, 23);
		panel_4.add(chckbxCatherization);

		btnAdjustmentBtn = new JButton("ADJUSTMENT");
		btnAdjustmentBtn.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/SECURITY.PNG")));
		btnAdjustmentBtn.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnAdjustmentBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String pass=ExamOperatorPassword();
				//String name=JOptionPane.showInputDialog(null,"Enter Password");
				InsuranceDBConnection db=new InsuranceDBConnection();
				exam_open_password=	db.retrieveEditInsExamModulePassword();
				db.closeConnection();
				if(pass.equals(exam_open_password)){
					new insuranceBill_adjustment(ipd_id,p_insurancetype,p_id,p_name,insuranceIPDBill.this,isdraft).setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Wrong Password", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnAdjustmentBtn.setBounds(198, 14, 137, 25);
		panel_4.add(btnAdjustmentBtn);



		JLabel lbldischargetype = new JLabel("Discharge :");
		lbldischargetype.setFont(new Font("Dialog", Font.PLAIN, 12));
		lbldischargetype.setBounds(833, 425, 87, 20);
		panel_4.add(lbldischargetype);

		dischargeCB = new JComboBox(new DefaultComboBoxModel(new String[] {"Select Type","Normal Discharge","LAMA(Leave Against Medical Advice)","Referral Discharge","Discharge On Request","Discharge Due To Death"}));
		dischargeCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		dischargeCB.setBounds(923, 425, 218, 22);
		panel_4.add(dischargeCB);

		SaveBtn = new JButton("SAVE ");
		SaveBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(discharge_type.equals("Select Type"))
				{
					JOptionPane.showMessageDialog(null,
							"Select Discharge Type", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				IPDExpensesDBConnection db= new IPDExpensesDBConnection();
				IPDDBConnection db1 = new IPDDBConnection();
				try {
					//db1.updateIsDraftStatus(ipd_id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				db1.closeConnection();
				String[] data=new String[20];
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				data[0]=ipd_id;
				data[1]=p_id;
				data[2]=p_name;
				data[15]=REBATE+"";
				if(!isdraft) {
					for(int i=0;i<ITEM_ID.size();i++) {
						System.out.println("--------------------"+ITEM_NAME.get(i));
						data[3]=ITEM_NAME.get(i);
						data[4]=ITEM_DESC.get(i);
						data[5]=DATE.get(i);
						data[6]=TYPE.get(i);
						data[7]=ITEM_ID.get(i);
						data[8]=PAGE_NO.get(i);
						data[9]=MRP.get(i);
						data[10]=PER_ITEM_PRICE.get(i)+"";
						data[11]=QTY.get(i);
						data[12]=AMOUNT.get(i)+"";
						data[13]=BATCH.get(i);
						data[14]=EXPIRY.get(i);

						try {
							db.inserInsuranceData(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					//					try {
					//						db.Updatedraft(ipd_id,false);
					//					} catch (Exception e1) {
					//						// TODO Auto-generated catch block
					//						e1.printStackTrace();
					//					}
				}
				else {
					for(int i=0;i<ITEM_ID.size();i++) {

						data[3]=ITEM_NAME.get(i);
						data[4]=ITEM_DESC.get(i);
						data[5]=DATE.get(i);
						data[6]=TYPE.get(i);
						data[7]=ITEM_ID.get(i);
						data[8]=PAGE_NO.get(i);
						data[9]=MRP.get(i);
						data[10]=PER_ITEM_PRICE.get(i)+"";
						data[11]=QTY.get(i);
						data[12]=AMOUNT.get(i)+"";
						data[13]=BATCH.get(i);
						data[14]=EXPIRY.get(i);
						data[16]=EXPENSE_ID.get(i);

						try {
							db.UpdateData(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				}
				try {
					db.Updatedraft(ipd_id,true);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();
				IPDDBConnection db11 = new IPDDBConnection();
				db11.UpdateClaimIDDichargeType(claim_idtf.getText().toString(), ipdNoTB.getText().toString(),discharge_type);
				db11.closeConnection();
				
				searchBT.doClick();
			}
		});
		SaveBtn.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/SAVE.PNG")));
		SaveBtn.setBounds(296, 428, 131, 34);
		panel_4.add(SaveBtn);

		lblCashPatient = new JLabel("CASH PATIENT");
		lblCashPatient.setVisible(false);
		lblCashPatient.setForeground(Color.RED);
		lblCashPatient.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblCashPatient.setBounds(995, 12, 154, 22);
		panel_4.add(lblCashPatient);

		JButton btnReset = new JButton("RESET");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int dialogResult = JOptionPane.showConfirmDialog(null, "Do you want to reset draft bill?","Warning", JOptionPane.YES_NO_OPTION);
				if(dialogResult != JOptionPane.YES_OPTION){
					return;
				}
				String pass=ExamOperatorPassword();
				//String name=JOptionPane.showInputDialog(null,"Enter Password");
				InsuranceDBConnection db=new InsuranceDBConnection();
				exam_open_password=	db.retrieveEditInsExamModulePassword();
				db.closeConnection();
				if(pass.equals(exam_open_password)){
					System.out.println("IPD DELETING...");
					IPDDBConnection db1 = new IPDDBConnection();
					db1.deleteDraftBill(ipd_id);
					db1.closeConnection();
					searchBT.doClick();
				}else{
					JOptionPane.showMessageDialog(null,
							"Wrong Password!", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		btnReset.setIcon(new ImageIcon(insuranceIPDBill.class.getResource("/icons/remove_patient_button.png")));
		btnReset.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnReset.setBounds(357, 14, 122, 25);
		panel_4.add(btnReset);
		dischargeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				discharge_type=dischargeCB.getSelectedItem().toString();
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
		searchPatientTB.requestFocus(true);
	}

	public void getPatientsID(String index) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.searchPatientWithIdOrNmaeNewtracking(index);
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


	private void GetOxygenAndCatheterizationData() {
		// TODO Auto-generated method stub
		InsuranceDBConnection db1 = new InsuranceDBConnection();
		ResultSet rs = db1.RetrieveCatheterizationData(p_insurancetype);
		try {
			while(rs.next()) {
				if(rs.absolute(1)) {
					CATHETERIZATION_CODE=rs.getString(1);
					CATHETERIZATION_PRICE=rs.getDouble(2);
				}
				if(rs.absolute(2)) {
					OXYGEN_CODE=rs.getString(1);
					OXYGEN_PRICE=rs.getDouble(2);
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block 
			e.printStackTrace();
		}
		db1.closeConnection();
	}

	public void setPatientDetail(String indexId) {
		ipdBuildingTB.setText("");
		ipdNoTB.setText("");
		ipdWardTB.setText("");
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndextracking(indexId);
		try {
			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_age = resultSet.getObject(2).toString();
				p_sex = resultSet.getObject(3).toString();
				p_address = resultSet.getObject(4).toString();
				p_city = resultSet.getObject(5).toString();
				p_telephone = resultSet.getObject(6).toString();
				p_insurancetype = resultSet.getObject(7).toString();
				insurance_category=resultSet.getString(10);
				System.out.println(insurance_category);
				categoryTF.setText(insurance_category);	
				//p_scheme = resultSet.getObject(9).toString();
				getIPDDATA();
			}
			patientDBConnection.closeConnection();
			if(categoryTF.getText().equals("")) {
				AddInsuranceCategory(indexId);
			}	

			if(p_insurancetype.equals("ECHS") || p_insurancetype.equals("Railway") || p_insurancetype.equals("CGHS"))
			{
				claim_idtf.setEnabled(true);	
			}
		} catch (SQLException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void AddInsuranceCategory(String indexId) {
		// TODO Auto-generated method stub
		String InsuranceCategory="";
		JPanel panel = new JPanel(new GridBagLayout());
		Object[] info = { "Please Select", "GENERAL CATEGORY", "PVT CATEGORY", "SEMI PVT CATEGORY"};
		JComboBox comboBox = new JComboBox(info); 
		int i= JOptionPane.showConfirmDialog(null, comboBox, "Insurance Category",
				JOptionPane.OK_CANCEL_OPTION);
		if(i==0 && !comboBox.getSelectedItem().equals("Please Select")) {
			InsuranceCategory=comboBox.getSelectedItem()+"";
			System.out.print(i+""+comboBox.getSelectedItem());
			try {
				PatientDBConnection patientDBConnection = new PatientDBConnection();
				patientDBConnection.updateInsuranceCategory(p_id,InsuranceCategory);
				patientDBConnection.closeConnection();
				setPatientDetail(indexId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else {
			dispose();
		}
		panel.add(comboBox);
	}

	public void getIPDDATA() {

		status="";
		package_id = "N/A";
		submitted_amount="";
		rs_amount="";
		recvdate="";
		subdate="";
		isdraft=false;
		String dt="";
		java.sql.Date date;

		Date todayDate =new Date();
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientIDNEWtracking(p_id,ref_ipd_id);
		try {
			while (resultSet.next()) {
				ipd_id = resultSet.getObject(1).toString();
				ipdNoTB.setText("" + resultSet.getObject(2));
				ipdBuildingTB.setText("" + resultSet.getObject(3));
				ipdWardTB.setText("" + resultSet.getObject(4));
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				package_id = resultSet.getObject(10).toString();

				ipd_referrence = resultSet.getString(19);
				if(ipd_referrence.equals("")) {
					rdbtnNonRef.setSelected(true);
					rdbtnReferral.setSelected(false);
				}else {
					rdbtnNonRef.setSelected(false);
					rdbtnReferral.setSelected(true);
				}
				ipd_discharge_date=resultSet.getString(22);	
				isdraft= resultSet.getBoolean(21);
				claim_idtf.setText(resultSet.getObject(23).toString());
				Dischargetype=resultSet.getObject(24);
				if(Dischargetype==null || Dischargetype.equals("")) {
					dischargeCB.setSelectedIndex(0);
				}else {
					dischargeCB.setSelectedItem(Dischargetype.toString());
				}
				if(!resultSet.getBoolean(25)){
					( new Thread() { public void run() { 
						while(true) {
							try {
								Thread.sleep(800);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							lblCashPatient.setVisible(!lblCashPatient.isVisible());
						}
					} } ).start(); 
				}
			}
			if(Dischargetype==null || Dischargetype.equals("")) {
				DischargeType="";
				JPanel panel = new JPanel(new GridBagLayout());
				Object[] info = { "Select Type","Normal Discharge","LAMA(Leave Against Medical Advice)","Referral Discharge","Discharge On Request","Discharge Due To Death"};
				JComboBox comboBox = new JComboBox(info); 
				int i= JOptionPane.showConfirmDialog(null, comboBox, "Insurance Category",
						JOptionPane.OK_CANCEL_OPTION);
				if(i==0) {
					DischargeType=comboBox.getSelectedItem()+"";
					System.out.print(i+""+comboBox.getSelectedItem());
					try {
						resultSet.close();
						PatientDBConnection patientDBConnection = new PatientDBConnection();
						patientDBConnection.updateInsuranceDischargetype(ipd_id,DischargeType);
						patientDBConnection.closeConnection();
						searchBT.doClick();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}else {
					return;
				}

				panel.add(comboBox);
			}	
			getIPDDoctors(ref_ipd_id);
			if(isdraft) {
				btnOpenBill.setEnabled(true);
				SaveBtn.setText("SAVED");
				SaveBtn.setBackground(new Color(154, 205, 50));
				GetDraftBillDATA();
			}else {
				btnOpenBill.setEnabled(false);
				SaveBtn.setText("SAVE");
				SaveBtn.setBackground(null);
				GetBillDATA();
			}
			populateExpensesTable();
			fun1(ipd_discharge_date,10);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InsuranceDBConnection db1 = new InsuranceDBConnection();
		ResultSet rs=db1.retrievestatus(ipdNoTB.getText().toString());
		try {
			while(rs.next()) {
				status=rs.getObject(1).toString();
			}
			if(status!="" || status=="N/A")
				btnOpenBill.setEnabled(true);
			else
				btnOpenBill.setEnabled(false);

		} catch (SQLException e2) {
			// TODO Auto-generated catch block 
			e2.printStackTrace();

		}
		db1.closeConnection();
		if(status.equals("") || status.equals("CLOSED") || status.equals("CLOSED(OBJ)")) {
			ResultSet rs1 = db.retrievestatustracking(ipdNoTB.getText().toString());
			try {
				while (rs1.next()) {
					submitted_amount = "" + rs1.getObject(1);
					//dt=resultSet.getDate(20).toString().equals("null")?"2023-08-03".toString():resultSet.getDate(20).toString();
					if (!submitted_amount.equals("null")) {
						advancePaymentTB.setText(submitted_amount);
						checkbox.setSelected(true);
						//advancePaymentTB.setEditable(false);
					}
					rs_amount = "" + rs1.getObject(2);
					recvdate= "" + rs1.getDate(3);
					subdate=""+rs1.getDate(4);
					//dt=resultSet.getDate(20).toString().equals("null")?"2023-08-03".toString():resultSet.getDate(20).toString();
					if (!rs_amount.equals("null")) {
						amountTB.setText(rs_amount);
						//advancePaymentTB.setEditable(false);
					}

					fun(recvdate);
					fun1(subdate,1);

				}
			}catch (HeadlessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		db.closeConnection();

	}
	private void fun(String recvdate2) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(recvdate2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date currentDate = new Date(System.currentTimeMillis());
		cal.add(Calendar.DAY_OF_MONTH, 1);
		String dateAfter = sdf.format(cal.getTime());
		Date d1 = null;
		try {
			d1 = sdf.parse(dateAfter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("date :" + d1 + "  current :" + currentDate);
		if (!currentDate.before(d1)) {
			//msubmitbtn.setEnabled(false);
			amountTB.setEditable(false);
			recieveddateDC.setEnabled(false);
			chckbxObjection.setEnabled(false);
			JOptionPane.showMessageDialog(null, "This Bill Has Been Closed You Can't Edit it",
					"Input Error", JOptionPane.ERROR_MESSAGE);

		}
	}

	private void fun1(String date2,int day) {
		// TODO Auto-generated method stub
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(sdf.parse(date2));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Date currentDate = new Date(System.currentTimeMillis());
		cal.add(Calendar.DAY_OF_MONTH, day);
		String dateAfter = sdf.format(cal.getTime());
		Date d1 = null;
		try {
			d1 = sdf.parse(dateAfter);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.print("date :" + d1 + "  current :" + currentDate);
		if (!currentDate.before(d1) && day==1) {
			advancePaymentTB.setEditable(false);

		}
		if(!currentDate.before(d1) && day==10) {
			btnAdjustmentBtn.setEnabled(false);
		}
	}

	public void getIPDDoctors(String ipd_id) {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataDoctortracking(ipd_id);
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
	public void populateExpensesTable() {

		System.out.println("arun");
		totalCharges = 0;
		expensesIndexVector.clear();
		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[ITEM_ID.size()][6];

		for (int i = 0; i < ITEM_ID.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = ITEM_NAME.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = TYPE.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = DATE.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = PER_ITEM_PRICE.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = QTY.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = AMOUNT.get(i);
			totalCharges+=AMOUNT.get(i);
		}
		if(ITEM_DESC.indexOf("Oxygen Charges")!=-1)
			chckbxOxygenCharge.setSelected(true);
		if(ITEM_DESC.indexOf("Catheterisation Charges")!=-1)
			chckbxCatherization.setSelected(true);
		// Finally load data to the table
		DefaultTableModel model = new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Description","Type","Date","Rate","Qty", "Amount" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}
		};
		totalCharges = Math.round(totalCharges);	
		totalAmountTB.setText(totalCharges + "");
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setPreferredWidth(30);
		table.getColumnModel().getColumn(1).setMinWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setMinWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setMinWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		ipd_balance = totalCharges - ipd_advance;

		if (!amountTB.getText().equals("")) {
			ipd_balance = ipd_balance
					- Float.parseFloat(amountTB.getText().toString());
		}

		IPDDBConnection ipddbConnection = new IPDDBConnection();
		try {
			//ipddbConnection.updateTotalAmounttracking(ipd_id, totalCharges + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ipddbConnection.closeConnection();
			e.printStackTrace();
		}
		ipddbConnection.closeConnection();
	}
	public String ExamOperatorPassword() {
		JPasswordField jpf = new JPasswordField(24);
		JLabel jl = new JLabel("Enter Password: ");
		Box box = Box.createHorizontalBox();
		box.add(jl);
		box.add(jpf);
		int x = JOptionPane.showConfirmDialog(null, box, "Password", JOptionPane.OK_CANCEL_OPTION);

		if (x == JOptionPane.OK_OPTION) {
			return jpf.getText().toString();
		}
		return null;
	}
	private void GetBillDATA() {
		// function to get all main data from ipd_expenses
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		PAGE_NO.removeAllElements();
		MRP.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		BATCH.removeAllElements();
		EXPIRY.removeAllElements();
		ITEM_DESC.removeAllElements();
		IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
		ResultSet rs = db1.retrieveAllIPD_DATA(ipd_id,p_insurancetype);
		/** getting ipd data from DB**/
		BED_CALCULAION();
		ADD_CONSULTATION();
		try {
			while (rs.next()) {
				ITEM_NAME.add(rs.getString(3));
				DATE.add(rs.getString(5));  
				TYPE.add(rs.getString(1));
				if(rs.getString(1).equals("C")) {
					consultant=rs.getString(3);	
				}
				ITEM_ID.add(rs.getString(2));
				PAGE_NO.add(rs.getString(4));
				MRP.add(rs.getString(9));
				PER_ITEM_PRICE.add(rs.getDouble(10)); 
				QTY.add(rs.getString(11));
				AMOUNT.add(rs.getDouble(12));
				BATCH.add(rs.getString(6));
				EXPIRY.add(rs.getObject(7)==null||rs.getObject(7).equals("")?"":rs.getObject(7)+"");
				ITEM_DESC.add(rs.getString(3));
				if(rs.getInt(8)!=0) {
					PACKAGE_DAYS.add(rs.getInt(8));
					PACKAGE_DATES.add(rs.getString(5));
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SURGERY_CALCULATION(PER_ITEM_PRICE);
		if(REBATE!=0) {
			chckbxRebate.setSelected(true);
			chckbxRebate.setText("Rebate("+REBATE+"%)");
		}
		db1.closeConnection();
		PACKAGE_AMOUNT_CALCULATION();

	}

	private void GetDraftBillDATA() {
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		PAGE_NO.removeAllElements();
		MRP.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		BATCH.removeAllElements();
		EXPIRY.removeAllElements();
		ITEM_DESC.removeAllElements();
		EXPENSE_ID.removeAllElements();
		IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
		ResultSet rs = db1.retrieveInsuranceDataForIsdraft(ipd_id);
		/** getting draft bill from insurance_tracking_list table **/
		try {
			while (rs.next()) {
				ITEM_NAME.add(rs.getString(5));
				ITEM_DESC.add(rs.getString(6));
				DATE.add(rs.getString(7));
				TYPE.add(rs.getString(8));
				ITEM_ID.add(rs.getString(9));
				PAGE_NO.add(rs.getString(10));
				MRP.add(rs.getString(11));
				PER_ITEM_PRICE.add(rs.getDouble(12));
				QTY.add(rs.getString(13));
				AMOUNT.add(rs.getDouble(14));
				BATCH.add(rs.getString(15));
				EXPIRY.add(rs.getString(16));
				REBATE=rs.getDouble(17);
				EXPENSE_ID.add(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db1.closeConnection();

	}

	private void ADD_CONSULTATION() {	
		/** Here we get all consultation from ipd_entery table using entry_date_time 
		 * if entry and discharge date would same then 1 consultation
		 * if entry time is less than 12:00:00 pm then 2 conlsutation
		 * in the discharge date always put 1 consultation 
		 **/
		IPDDBConnection db = new IPDDBConnection();
		ResultSet rs = db.retrieveConsultantData(ipd_id);	
		try {
			while(rs.next()) {
				ITEM_NAME.add(rs.getString(2));
				DATE.add(rs.getString(3));
				TYPE.add("C");
				ITEM_ID.add("N/A");
				PAGE_NO.add("N/A");
				MRP.add("0");
				PER_ITEM_PRICE.add(rs.getDouble(5));
				QTY.add(rs.getString(4));
				AMOUNT.add(rs.getDouble(5));
				BATCH.add("");
				EXPIRY.add("");
				ITEM_DESC.add(rs.getString(2));

			}
			db.closeConnection();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	

	}
	private void BED_CALCULAION() {
		// TODO Auto-generated method stub
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveInsuranceIpdBedData(ipd_id);//to get all beds from database

		Vector<String> WardV=new Vector<String>();
		Vector<String> WardDateV=new Vector<String>();
		Vector<Double> WardChargeV=new Vector<Double>();
		int dayCareTime=0;
		try {
			while (resultSet.next()) {
				WardV.add(resultSet.getString(2));
				WardDateV.add(resultSet.getString(3));
				WardChargeV.add(resultSet.getDouble(5));
				dayCareTime=resultSet.getInt(6);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();	
		/** code to remove last bed**/
		if(WardDateV.size()>1) {
			WardV.removeElementAt(WardDateV.size()-1);
			WardDateV.removeElementAt(WardDateV.size()-1);
			WardChargeV.removeElementAt(WardDateV.size()-1);
		}
		if(WardDateV.size()==1 && dayCareTime<=360) {
			WardV.set(0, "DAY CARE");
			WardChargeV.set(0, 500.0);
		}
		/** code end **/	

		if(Dischargetype.equals("Normal Discharge")) {
			/** code to find no. of ICU and OtherWard **/
			int icu=0;
			for(int i=0;i<=WardV.lastIndexOf("ICU");i++) {
				if(WardV.get(i).equals("ICU")) {
					icu++;
				}else{
					icu=0;
				}
			}
			int OtherWard=WardV.size()-WardV.lastIndexOf("ICU")-1;
			/** code end **/

			/** here we check how many OtherWard we need to replace in the beds dates
			 * EXP:-Suppose Above code gives us counting like ICU=10 and OtherWard=1
			 * after that the below code will (add=2-1) then we have add=1 means we need 
			 * to replace OtherWard only one in the last 
			 * **/
			int add=0;
			if(icu>=1 && icu<=7) {
				add=1-OtherWard;
			}else if(icu>7 && icu<=10) {
				add=2-OtherWard;
			}else if(icu>10) {
				add=3-OtherWard;
			}
			add=add>0?add:0;/** (add variable always should be greater ) **/

			db = new IPDDBConnection();
			ResultSet rs = db.retrieveInsuranceBedCharge(p_insurancetype,insurance_category);
			String wardName="";
			double charge=0;
			try {
				while(rs.next()) {
					wardName=rs.getString(1);
					charge=rs.getDouble(2);
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			db.closeConnection();

			/** code to replace the beds in main vectors**/
			int lastICUIndex=WardV.lastIndexOf("ICU");
			int c=0;
			while(add!=0) {
				WardV.set(lastICUIndex-c,wardName);
				WardChargeV.set(lastICUIndex-c, charge);
				c++;
				add--;
			}	
		}
		/** code end **/
		for(int i=0;i<WardV.size();i++) {
			ITEM_NAME.add(WardV.get(i));
			DATE.add(WardDateV.get(i));
			TYPE.add("B");
			ITEM_ID.add("N/A");
			PAGE_NO.add("N/A");
			MRP.add("0");
			PER_ITEM_PRICE.add(WardChargeV.get(i));
			QTY.add("1");
			AMOUNT.add(WardChargeV.get(i));
			BATCH.add("");
			EXPIRY.add("");
			ITEM_DESC.add(WardV.get(i));
		}
	}


	public void PACKAGE_AMOUNT_CALCULATION() {
		/** Function to do Zero Amount of that items which are used in between 
		 * the surgery days except services(represented by Z) and surgeries(represented by S).
		 * **/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/** logic to get all dates that under the surgery **/
		for(int i=0;i<PACKAGE_DATES.size();i++) {
			Calendar c1 = Calendar.getInstance();
			Date dt = null;
			try {
				dt = sdf.parse(PACKAGE_DATES.get(i));
			} catch (ParseException ex) {

			}
			c1.setTime(dt);  
			for(int j=0;j<PACKAGE_DAYS.get(i);j++) {	
				String date=sdf.format(c1.getTime());
				if(!(PACKAGE_AMOUNT_REMOVE_DATES.indexOf(date)!=-1)) {
					PACKAGE_AMOUNT_REMOVE_DATES.add(date); 
				}
				c1.add(Calendar.DAY_OF_MONTH, 1); 
			}

		}	
		/** code end **/

		/** code to replace the amount of all items which have the same dates with surgery dates**/
		for(int i=0;i<DATE.size();i++) {
			if(PACKAGE_AMOUNT_REMOVE_DATES.indexOf(DATE.get(i))!=-1 && !TYPE.get(i).equals("S") && !TYPE.get(i).equals("Z")) {
				AMOUNT.set(i, 0.0);//to replace the amount
			}
		}
		/** code end  **/
	}

	public void SURGERY_CALCULATION(Vector<Double> AMT) {
		// TODO Auto-generated method stub

		double max1 = 0;
		double max2 = 0;
		double max3 = 0;
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;

		int loopStart=TYPE.indexOf("S");// to get first index of surgery
		int loopEnd=TYPE.lastIndexOf("S");//to get last index of surgery

		/***1# code to found top most three surgeries for applying rules of insurance****/
		if (loopStart!=-1 && loopEnd!=-1) {
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE.get(i).equals("S") && AMT.get(i) > max1) {
					max1 = AMT.get(i);
					index1 = i;
				}
			}
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE.get(i).equals("S") && AMT.get(i) > max2 && i != index1) {
					max2 = AMT.get(i);
					index2 = i;
				}
			}
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE.get(i).equals("S") && AMT.get(i) > max3 && i != index2 && i != index1) {
					max3 = AMT.get(i);
					index3 = i;
				}
			} 
		}
		/****1# end ****/
		//System.out.println("max1: "+max1+"max2: "+max2+"max3: "+max3);

		/** DB function for getting surgeries discounts and rebate values from database  **/
		InsuranceDBConnection db = new InsuranceDBConnection();
		ResultSet rs = db.RetrieveSurgeryDATA(p_insurancetype, insurance_category);
		/** DB end  **/
		double s1 = 0, s2 = 0, s3 = 0;// veriables for surgery percentages
		double d1 = 1, d2 = 1, d3 = 1;// dividers for three top most surgery
		try {
			while (rs.next()) {
				s1 = rs.getDouble(1);
				s2 = rs.getDouble(2);
				s3 = rs.getDouble(3);
				d1 = rs.getDouble(4);
				d2 = rs.getDouble(5);
				d3 = rs.getDouble(6);
				REBATE = rs.getDouble(7); /** rebate for overall bill calculation **/ 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		/**2# logic for decrease/increase the amount of top three surgeries acc. to insurance rules and their ins category **/
		switch (insurance_category) {

		case "GENERAL CATEGORY":

			System.out.println("GENERAL CATEGORY "+REBATE);
			if (index1 != -1) {
				max1 = AMOUNT.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max1 = max1 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME.set(index1, s);
				AMOUNT.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME.set(index2, s);
				AMOUNT.set(index2, max2);

			}
			if (index3 != -1) {
				max3 = AMOUNT.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME.set(index3, s);
				AMOUNT.set(index3, max3);
			}
			break;

		case "SEMI PVT CATEGORY":
			System.out.println("SEMI PVT CATEGORY");
			if (index1 != -1) {
				max1 = AMOUNT.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max2 = max2 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME.set(index1, s);
				AMOUNT.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME.set(index2, s);
				AMOUNT.set(index2, max2);
			}
			if (index3 != -1) {
				max3 = AMOUNT.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME.set(index3, s);
				AMOUNT.set(index3, max3);
			}
			break;

		case "PVT CATEGORY":

			System.out.println("PVT CATEGORY");
			if (index1 != -1) {
				max1 = AMOUNT.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max2 = max2 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME.set(index1, s);
				AMOUNT.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME.set(index2, s);
				AMOUNT.set(index2, max2);

			}
			if (index3 != -1) {
				max3 = AMOUNT.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME.set(index3, s);
				AMOUNT.set(index3, max3);
			}
			break;

		}
		/**2# end**/
	}
}
