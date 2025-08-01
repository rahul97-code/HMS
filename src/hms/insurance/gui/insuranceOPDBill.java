package hms.insurance.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.exams.gui.AllExamsDetail;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.main.NumberToWordConverter;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.AdvancePaymentSlippdf;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.patient.slippdf.InsuranceIpdPdfSlip;
import hms.patient.slippdf.InsuranceOpdPdfSlip;
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
import javax.swing.JTextArea;

public class insuranceOPDBill extends JDialog {

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
	String  claim_id,status,
	opd_id = "", ward_name = "",
	building_name = "", bed_no = "Select Bed No", ward_incharge = "",
	ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
	ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id,date="",first_start_time="",opd_date="",mode="",instype="",
	package_id = "N/A", p_scheme = "",submitted_amount="",rs_amount="",subdate="",recvdate="",ipd_referrence="";
	String ID;
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();

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
	private JTextField totalAmountTB;
	private JTextField submitAmountTB;
	private JTable table;
	private JTextField receivedAmountTB;
	private JTextField claim_idtf;
	String[] data = new String[12];
	private JDateChooser recieveddateDC;
	private JButton submitbtn;
	private JCheckBox chckbxObjection;
	private JTextField categoryTF;
	private JRadioButton rdbtnReferral;
	private JRadioButton rdbtnNonRef;
	private String insurance_category="";
	private Object[][] ObjectArray_ListOfexamsSpecs;
	private JButton btnOpenBill;

	public JButton searchBT;
	private JLabel lblEnterPayment_1;
	private JTextField utrNumberTF;

	private JComboBox patientIDCB_1;

	private JCheckBox checkbox;

	private Object Dischargetype;
	private String DischargeType;
	private JLabel lblCashPatient;

	private JTextArea objectionTF;
	public static void main(String[] arg) {
		new insuranceOPDBill("0000130000002","548473","2024-08-23","ECHS","CASH").setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public insuranceOPDBill(String p_ID,final String opdID,String opd_date,String instype, String mode) {
		this.opd_date=opd_date;
		opd_id=opdID;
		this.instype=instype;
		this.mode=mode;
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				insuranceOPDBill.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Insurance OPD Aproved Bill");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1176, 489);

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
		panel_4.setBounds(6, 0, 1158, 431);
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
					// if (!p_insurancetype.equals("Unknown")) {
					// // panel_4.add(btnNewButton_3);
					// btnNewButton_3.setVisible(true);
					// }
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
					submitAmountTB.setText("");
					populateExpensesTable();
					claim_idtf.setText("");
					submitAmountTB.setEditable(true);

				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setEditable(false);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(372, 14, 139, 25);
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

		searchPatientTB.setText(opdID);
		searchBT = new JButton("");
		searchBT.setToolTipText("Search");
		searchBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				clear();
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					System.out.println("working"+str);
					getPatientsID(str);
				} else {
					clear();
				}
			}
		});
		searchBT.setBounds(511, 14, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(insuranceOPDBill.class
				.getResource("/icons/zoom_r_button.png")));
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
		lblPatientId.setBounds(552, 16, 77, 20);
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
		patientIDCB.setBounds(647, 16, 192, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Bill",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(516, 50, 313, 282);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(16, 17, 106, 14);
		panel_1.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(153, 12, 147, 25);
		panel_1.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		submitAmountTB = new JTextField();
		submitAmountTB.setBounds(152, 49, 148, 25);
		panel_1.add(submitAmountTB);
		submitAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		submitAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		submitAmountTB.setColumns(10);
		submitAmountTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});

		receivedAmountTB = new JTextField("");
		receivedAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		receivedAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		receivedAmountTB.setColumns(10);
		receivedAmountTB.setBounds(153, 79, 148, 25);
		panel_1.add(receivedAmountTB);
		receivedAmountTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});

		JLabel lblEnterPayment = new JLabel("Received Payment :");
		lblEnterPayment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterPayment.setBounds(15, 84, 131, 14);
		panel_1.add(lblEnterPayment);

		chckbxObjection = new JCheckBox(" Objection Raised...");
		chckbxObjection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxObjection.isSelected()) {
					objectionTF.setEditable(true);
				}else {
					objectionTF.setEditable(false);
				}
			}
		});
		chckbxObjection.setFont(new Font("Dialog", Font.PLAIN, 12));
		chckbxObjection.setBounds(15, 151, 190, 23);
		panel_1.add(chckbxObjection);

		recieveddateDC = new JDateChooser();
		recieveddateDC.setBounds(143, 245, 147, 25);
		panel_1.add(recieveddateDC);
		recieveddateDC.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if (recieveddateDC.getDate()!=null &&  "date".equals(arg0.getPropertyName())) {
					Recidate = DateFormatChange
							.StringToMysqlDate((Date) arg0
									.getNewValue());
				}
			}

		});
		recieveddateDC.setDateFormatString("yyyy-MM-dd");
		JLabel lblNote1 = new JLabel("Recieved Date :");
		lblNote1.setBounds(10, 247, 108, 23);
		recieveddateDC.setMaxSelectableDate(new Date());
		panel_1.add(lblNote1);
		lblNote1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblEnterPayment_1 = new JLabel("UTR Number :");
		lblEnterPayment_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblEnterPayment_1.setBounds(16, 117, 120, 14);
		panel_1.add(lblEnterPayment_1);

		utrNumberTF = new JTextField("");
		utrNumberTF.setHorizontalAlignment(SwingConstants.TRAILING);
		utrNumberTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		utrNumberTF.setColumns(10);
		utrNumberTF.setBounds(153, 111, 148, 25);
		panel_1.add(utrNumberTF);



		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(15, 182, 286, 50);
		panel_1.add(scrollPane_1);

		objectionTF = new JTextArea();
		objectionTF.setRows(5);
		objectionTF.setEditable(false);
		scrollPane_1.setViewportView(objectionTF);

		JLabel lblSubmitAmount = new JLabel("Submit Amount :");
		lblSubmitAmount.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSubmitAmount.setBounds(15, 54, 106, 14);
		panel_1.add(lblSubmitAmount);

		submitbtn = new JButton("SUBMIT");
		submitbtn.setIcon(new ImageIcon(insuranceOPDBill.class.getResource("/icons/ok_button.png")));
		submitbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(submitAmountTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"please enter Submit amount", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}Date date1;
				date1 = recieveddateDC.getDate();
				String str=receivedAmountTB.getText();
				if(date1==null && !(str.equals("") || str.equals("0") ))
				{
					JOptionPane.showMessageDialog(null, "Choose Recieved Date!", "Error", JOptionPane.ERROR_MESSAGE);
					recieveddateDC.grabFocus();
					return;
				}
				InsuranceDBConnection db = new InsuranceDBConnection();

				data[0] = ID; 
				data[1] = ""+opdID; 
				data[2] = "" + insuranceTypeTB.getText(); 
				data[3] =  totalAmountTB.getText().toString();
				data[4] = submitAmountTB.getText().toString();
				data[5] = Recidate;  
				data[6] = receivedAmountTB.getText().toString().equals("")?"0.0":receivedAmountTB.getText().toString();
				data[7] = objectionTF.getText();
				data[8] = new GeneralDBConnection().getCurrentDate();
				data[9] = ReceptionMain.receptionNameSTR;
				data[10] = utrNumberTF.getText()+"";
				data[11] = claim_idtf.getText()+"";

				try {
					db.insertOrUpdateOpdDataTracking(data);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Data Successfully Updated", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		submitbtn.setBounds(687, 381, 131, 25);
		panel_4.add(submitbtn);

		JButton cancelbtn = new JButton("CANCEL");
		cancelbtn.setIcon(new ImageIcon(insuranceOPDBill.class.getResource("/icons/CANCEL.PNG")));
		cancelbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelbtn.setBounds(527, 383, 131, 25);
		panel_4.add(cancelbtn);

		btnOpenBill = new JButton("OPEN BILL");
		btnOpenBill.setEnabled(true);
		btnOpenBill.setIcon(new ImageIcon(insuranceOPDBill.class.getResource("/icons/inventory.png")));
		btnOpenBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new InsuranceOpdPdfSlip(opdID,p_id,
							ITEM_NAME ,
							ITEM_DESC,
							DATE ,
							TYPE ,
							ITEM_ID,
							PER_ITEM_PRICE, 
							QTY ,
							AMOUNT									
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

		btnOpenBill.setBounds(607, 344, 131, 25);
		panel_4.add(btnOpenBill);

		lblCashPatient = new JLabel("CASH PATIENT");
		lblCashPatient.setVisible(false);
		lblCashPatient.setForeground(Color.RED);
		lblCashPatient.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		lblCashPatient.setBounds(870, 17, 170, 22);
		panel_4.add(lblCashPatient);

		JLabel lblOpdId = new JLabel("OPD ID :");
		lblOpdId.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOpdId.setBounds(277, 17, 77, 20);
		panel_4.add(lblOpdId);
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
		OPDDBConnection patientDBConnection = new OPDDBConnection();
		ResultSet resultSet = patientDBConnection.retrievePatientData(index);
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
				categoryTF.setText(insurance_category);					
			}
			patientDBConnection.closeConnection();
			if(categoryTF.getText().equals("")) {
				AddInsuranceCategory(indexId);
			}	
		} catch (SQLException e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		GetBillDATA();
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
			receivedAmountTB.setEditable(false);
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
			submitAmountTB.setEditable(false);
		}
	}


	public void populateExpensesTable() {
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

		getInsuranceOpdData();
	}

	private void getInsuranceOpdData() {
		InsuranceDBConnection db = new InsuranceDBConnection();
		ResultSet rs=db.retrieveInsuranceOpdData(opd_id);
		try {
			while(rs.next()) {
				ID=rs.getString(1);
				submitAmountTB.setText(rs.getString(5));
				receivedAmountTB.setText(rs.getString(7));
				if(rs.getDate(6)!=null)
					recieveddateDC.setDate(rs.getDate(6));
				objectionTF.setText(rs.getString(8));
				utrNumberTF.setText(rs.getString(11));
				if(rs.getBoolean(13)) {
					submitAmountTB.setEditable(false);
				}else
					submitAmountTB.setEditable(true);
				claim_idtf.setText(rs.getString(14));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
	}
	private void GetBillDATA() {
		// function to get all main data from ipd_expenses
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		PAGE_NO.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		ITEM_DESC.removeAllElements();
		IPDDBConnection db = new IPDDBConnection();
		ResultSet rs = db.retrieveAllOPDData(this.p_id,this.opd_date, this.instype, this.mode);
		/** getting ipd data from DB**/
		try {
			while (rs.next()) {
				ITEM_NAME.add(rs.getString(3));
				DATE.add(rs.getString(4));  
				TYPE.add(rs.getString(1));
				ITEM_ID.add(rs.getString(2));
				PAGE_NO.add(rs.getString(4));
				PER_ITEM_PRICE.add(rs.getDouble(6)); 
				QTY.add(rs.getString(5));
				AMOUNT.add(rs.getDouble(6));
				ITEM_DESC.add(rs.getString(3));				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {	
			db.closeConnection();
		}
		populateExpensesTable();
	}

	private void clear() {
		patientNameTB.setText("");
		submitAmountTB.setText("");
		receivedAmountTB.setText("");
		utrNumberTF.setText("");
		addressTB.setText("");
		ageTB.setText("");
		objectionTF.setText("");
		cityTB.setText("");
		telephoneTB.setText("");
		insuranceTypeTB.setText("");
		recieveddateDC.setDate(null);
		rdbtnMale.setSelected(false);
		rdbtnFemale.setSelected(false);
		claim_idtf.setEnabled(false);
		patientIDWithaName.removeAllElements();
		patientIDCB.setModel(patientIDWithaName);
		submitAmountTB.setText("");
		populateExpensesTable();
		submitAmountTB.setEditable(true);
	}
}
