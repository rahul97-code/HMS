package hms1.ipd.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.gui.NewPatient;
import hms.patient.slippdf.AdvancePaymentSlippdf;
import hms.patient.slippdf.PO_PDF;
import hms.reception.gui.ReceptionMain;
import hms.store.database.ProceduresDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;
import hms1.ipd.database.PackageDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

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
import javax.swing.JTextArea;
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

import com.itextpdf.text.DocumentException;

public class EmergencyIpd extends JDialog {

	/**
	 * 	
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField attendanceDateTB;
	private JTextField examNOTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private Timer timer;
	private JTextField telephoneTB;
	private JTextField ageTB,SchemeTB;
	JTextField remarks;
	ButtonGroup cashgroup = new ButtonGroup();

	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",operationNameSTr="",p_scheme="";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_note = "", ipd_counter = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", wardCategorySTR = "",emergency="";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	final DefaultComboBoxModel packagesCBM = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	final DefaultComboBoxModel procedureName = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	Vector<String> bedIndex = new Vector<String>();
	private JComboBox patientIDCB,hospitalSchemeCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JTextArea examNote;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JComboBox<String> wardCB;
	private JComboBox<String> bedNoCB;
	private JComboBox<String> buildingCB;
	private JLabel wardInchargeLB;
	private JLabel wardRoomNoLB;
	private JTextField refferenceDoctorTB;
	private JTextField advancePaymentTB;
	private JTextField ipdNoTB;
	private JComboBox<String> indoorPackageCB;
	String packaheIDSTR = "N/A";
	private JTextField packageAmountLB;
	private JComboBox paymentModeCB;
	private JComboBox operationTypeCB;
	private JTextField SurgicalCodeTb;
	private JTextField TFApprovedAmnt;
	private JComboBox<String> cmbEmergency;
	private JTextField claim_idtxt;
	private JRadioButton rdbtnCash;
	private JRadioButton rdbtnCashless;

	 public static void main(String[] arg) {
	 new EmergencyIpd(null).setVisible(true);
	 }

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public EmergencyIpd(final EmergencyIpdBrowser emergencyIpdBrowser) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EmergencyIpd.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Emergency IPD Entry");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1040, 634);

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
				.getBorder("TitledBorder.border"), "Emergency IPD Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 1018, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		ipddbConnection = new IPDDBConnection();
		ipd_counter = ipddbConnection.retrieveCounterData() + "";
		ipddbConnection.closeConnection();
		JLabel lblAttendenceDate = new JLabel("Attendence Date");
		lblAttendenceDate.setBounds(20, 17, 128, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(362, 63, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";
				
				if (!str.equals(""))  {
					getPatientsID(str);
				}
				else {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					
					rdbtnFemale.setSelected(false);
//					patientID.removeAllElements();
//					patientIDCB.setModel(patientID);
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					lastOPDDateLB.setText("Last OPD :");
					
					
					itemsHashMap.clear();
					examHashMap.clear();
					claim_idtxt.setEnabled(false);
					claim_idtxt.setText("");
				}

				
			}
		});
		searchPatientTB = new JTextField();
		
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(475, 58, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
						
					//if (insuranceTypeTB.getText().toString()!=("ECHS") || insuranceTypeTB.getText().toString()!=("Railway")) {
						
						
					//}
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
						
					//if (insuranceTypeTB.getText().toString()!=("ECHS") || insuranceTypeTB.getText().toString()!=("Railway")) {
						
						
					//}
					
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					//if (insuranceTypeTB.getText().toString()!=("ECHS") || insuranceTypeTB.getText().toString()!=("Railway")) {
						
						
					//}
					}
					

					
				});

		JButton searchBT = new JButton("");
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
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					lastOPDDateLB.setText("Last OPD :");
					itemsHashMap.clear();
					examHashMap.clear();

				}
			}
		});
		searchBT.setBounds(657, 57, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(EmergencyIpd.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(133, 14, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JLabel lblIPD = new JLabel("IPD No.");
		lblIPD.setBounds(20, 42, 103, 23);
		panel_4.add(lblIPD);
		lblIPD.setFont(new Font("Tahoma", Font.PLAIN, 12));

		examNOTB = new JTextField(ipd_counter + "");
		examNOTB.setVisible(false);
		examNOTB.setBounds(133, 43, 212, 25);
		panel_4.add(examNOTB);
		examNOTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNOTB.setEditable(false);
		examNOTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(695, 91, 313, 349);
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
		lblSex.setBounds(6, 192, 46, 14);
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
		lblNote.setBounds(6, 230, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 222, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);
		JLabel lblNote1 = new JLabel("Has Scheme :");
		lblNote1.setBounds(6, 260, 108, 14);
		panel.add(lblNote1);
		lblNote1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		hospitalSchemeCB = new JComboBox();
		hospitalSchemeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) hospitalSchemeCB.getSelectedItem();
				p_scheme = str;
			}
		});
		System.out.println(p_scheme);
		hospitalSchemeCB.setModel(new DefaultComboBoxModel(new String[] { "N/A",
				"Sneh Sparsh", "Karuna"}));
		hospitalSchemeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		hospitalSchemeCB.setBounds(106, 260, 201, 25);
		panel.add(hospitalSchemeCB);
//		SchemeTB.setColumns(10);
		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(6, 284, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		claim_idtxt = new JTextField();
		claim_idtxt.setEnabled(false);
		claim_idtxt.setBounds(106, 315, 201, 22);
		panel.add(claim_idtxt);
		claim_idtxt.setColumns(10);
		
		JLabel lblClaimId = new JLabel("Claim Id");
		lblClaimId.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblClaimId.setBounds(16, 315, 70, 15);
		panel.add(lblClaimId);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(695, 57, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 76, 349, 503);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(5, 11, 337, 119);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor ",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 16, 108, 25);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameCB = new JComboBox<String>();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ipd_doctorname = doctorNameCB.getSelectedItem().toString();
					getDoctorDetail(ipd_doctorname);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getAllDoctors();
		doctorNameCB.setBounds(103, 18, 228, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDoctorRoom = new JLabel("Doctor Room :");
		lblDoctorRoom.setBounds(6, 52, 108, 25);
		panel_1.add(lblDoctorRoom);
		lblDoctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_4 = new JLabel("Doctor specialization :");
		lblNewLabel_4.setBounds(6, 90, 141, 24);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorRoomLB = new JLabel("");
		doctorRoomLB.setBounds(145, 54, 186, 23);
		panel_1.add(doctorRoomLB);
		doctorRoomLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorSpecializationLB = new JLabel("");
		doctorSpecializationLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorSpecializationLB.setBounds(145, 89, 186, 25);
		panel_1.add(doctorSpecializationLB);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);

		JPanel panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Ward Selection",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(5, 276, 337, 227);
		panel_2.add(panel_5);
		panel_5.setLayout(null);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setBounds(6, 16, 96, 25);
		panel_5.add(lblBuilding);
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));

		buildingCB = new JComboBox<String>();
		buildingCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					building_name = buildingCB.getSelectedItem().toString();
					getAllWards();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		buildingCB.setBounds(103, 18, 214, 25);
		panel_5.add(buildingCB);
		buildingCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setBounds(6, 65, 96, 25);
		panel_5.add(lblWardName);
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		wardCB = new JComboBox<String>();
		wardCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ward_name = wardCB.getSelectedItem().toString();
					getAllBed();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		wardCB.setBounds(103, 65, 214, 25);
		panel_5.add(wardCB);
		wardCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblBedNo = new JLabel("Bed No. :");
		lblBedNo.setBounds(6, 119, 96, 25);
		panel_5.add(lblBedNo);
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		bedNoCB = new JComboBox<String>();
		bedNoCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					bed_no = bedNoCB.getSelectedItem().toString();
					ward_code = "0";
					if (bedNoCB.getSelectedIndex() > 0) {
						ward_code = bedIndex.get(bedNoCB.getSelectedIndex() - 1);
					}
					getWardDetail();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		bedNoCB.setBounds(103, 119, 214, 25);
		panel_5.add(bedNoCB);
		bedNoCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblWardIncharge = new JLabel("Ward Incharge :");
		lblWardIncharge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardIncharge.setBounds(6, 155, 108, 25);
		panel_5.add(lblWardIncharge);

		JLabel lblRoomNo = new JLabel("Room No. :");
		lblRoomNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRoomNo.setBounds(6, 182, 108, 25);
		panel_5.add(lblRoomNo);

		wardInchargeLB = new JLabel("");
		wardInchargeLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardInchargeLB.setBounds(119, 155, 194, 25);
		panel_5.add(wardInchargeLB);

		wardRoomNoLB = new JLabel("");
		wardRoomNoLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardRoomNoLB.setBounds(119, 182, 194, 25);
		panel_5.add(wardRoomNoLB);

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Package ",

		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.setBounds(5, 129, 337, 136);
		panel_2.add(panel_9);

		JLabel lblPackage = new JLabel("Package :");
		lblPackage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPackage.setBounds(6, 16, 108, 25);
		panel_9.add(lblPackage);

		indoorPackageCB = new JComboBox<String>();
		// indoorPackageCB.setSelectedIndex(0);
		indoorPackageCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		indoorPackageCB.setBounds(103, 18, 224, 25);
		panel_9.add(indoorPackageCB);
		indoorPackageCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				packageAmountLB.setText("");
				try {
					if (indoorPackageCB.getSelectedIndex() > 0) {
						operationTypeCB.setSelectedIndex(0);
						packageAmountLB.setText("");
					} else {
						operationTypeCB.setSelectedItem("");
						packageAmountLB.setText("");
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		JLabel lblAmount = new JLabel("Amount :");
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAmount.setBounds(6, 100, 98, 25);
		panel_9.add(lblAmount);

		packageAmountLB = new JTextField("");
		packageAmountLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		packageAmountLB.setBounds(103, 102, 224, 23);
		panel_9.add(packageAmountLB);
		packageAmountLB.addKeyListener(new KeyAdapter() {
		         public void keyTyped(KeyEvent e) {
		           char c = e.getKeyChar();
		           if (!(Character.isDigit(c) ||
		              (c == KeyEvent.VK_BACK_SPACE) ||
		              (c == KeyEvent.VK_DELETE))) {
		                e.consume();
		              }
		         }
		       });
		JLabel lblOperationType = new JLabel("Operation Name:");
		lblOperationType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOperationType.setBounds(6, 64, 98, 25);
		panel_9.add(lblOperationType);

		operationTypeCB = new JComboBox();
		operationTypeCB.setEditable(true);
		operationTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		operationTypeCB.setBounds(103, 66, 224, 23);
		operationTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				operationNameSTr = (String) operationTypeCB.getSelectedItem();
			}
		});
		final JTextField tfListText = (JTextField) operationTypeCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					operationNameSTr = text;
	
					// HERE YOU CAN WRITE YOUR CODE
				}
			}
		});
		panel_9.add(operationTypeCB);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ipd_note = examNote.getText();
		
//				String emergency1 = cmbEmergency.getSelectedItem().toString();
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (ipd_doctorname.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null,
							"Please select opd doctor", "Input Error",
							JOptionPane.ERROR_MESSAGE);

				} else if (bed_no.equals("Select Bed No")) {
					JOptionPane.showMessageDialog(null,
							"Please select bed no.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (ipdNoTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select IPD No.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
//				 else if (cmbEmergency.getSelectedItem().toString().equals("Select Emergency OPD")) {
//						JOptionPane.showMessageDialog(null,
//								"Please select Emergency OPD", "Input Error",
//								JOptionPane.ERROR_MESSAGE);
//					}
				
				
//				else if(!p_insurancetype.equals("Unknown")){
//
//					if (SurgicalCodeTb.getText().equals("")){
//						JOptionPane.showMessageDialog(null,
//								"Please Enter Surgical Code", "Input Error",
//								JOptionPane.ERROR_MESSAGE);
////						return;
//					}
//					if (TFApprovedAmnt.getText().equals("")){
//						JOptionPane.showMessageDialog(null,
//								"Please Enter Approved Amount", "Input Error",
//								JOptionPane.ERROR_MESSAGE);
////						return;
////					}	
//				}
//				}
				
				
//		if(!p_insurancetype.equals("Unknown")){
//					
//					if (SurgicalCodeTb.getText().equals("")){
//						JOptionPane.showMessageDialog(null,
//								"Please Enter Surgical Code", "Input Error",
//								JOptionPane.ERROR_MESSAGE);
//						return;
//					}
//					if (TFApprovedAmnt.getText().equals("")){
//						JOptionPane.showMessageDialog(null,
//								"Please Enter Approved Amount", "Input Error",
//								JOptionPane.ERROR_MESSAGE);
//						return;
//					}
//					
//					
//				
//
//				}
//				else if (advancePaymentTB.getText().toString().equals("")) {
//					JOptionPane.showMessageDialog(null,
//							"Please enter advance payment", "Input Error",
//							JOptionPane.ERROR_MESSAGE);
//				}
//				else if (advancePaymentTB.getText().toString().equals("")) {
//					JOptionPane.showMessageDialog(null,
//							"Please enter advance payment", "Input Error",
//							JOptionPane.ERROR_MESSAGE);
//				}
				else if (getIPDDATA()) {
					JOptionPane
							.showMessageDialog(
									null,
									"This Patient Already Indoor Patient Please Discharge or add advance Payments",
									"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
//					double advancePayment = Double.parseDouble(advancePaymentTB
//							.getText().toString());
//					double finalAdvance = advancePayment;
//					if (paymentModeCB.getSelectedIndex() > 0) {
//						double t = advancePayment
//								* Double.parseDouble(paymentCharges.get(
//										paymentModeCB.getSelectedIndex())
//										.toString());
//						finalAdvance = Math.round(t * 100.0) / 100.0;
//					}

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					int insertedID = 0;
					ipddbConnection = new IPDDBConnection();
					String[] data = new String[26];
					data[0] = ipdNoTB.getText().toUpperCase();
					data[1] = p_id;
					data[2] = p_name;
					data[3] = refferenceDoctorTB.getText().toString() + "";
					data[4] = building_name;
					data[5] = ward_name;
					data[6] = wardRoomNoLB.getText();
					data[7] = bed_no;
					data[8] = ward_code;
					data[9] = indoorPackageCB.getSelectedItem().toString();
					data[10] = operationNameSTr;
					data[11] = packageAmountLB.getText().toString();
					data[12] = ipd_date;
					data[13] = "" + timeFormat.format(cal1.getTime());
					data[14] = "0.0";
					data[15] = ReceptionMain.receptionNameSTR;
					data[16] = packaheIDSTR;
					data[17] = p_insurancetype;
					data[18] = ipd_doctorname;
					data[19] = p_scheme;
					data[20]=SurgicalCodeTb.getText()+"";
					data[21]=TFApprovedAmnt.getText()+"";
					data[22]="Emergency";
					data[23]=remarks.getText();
					data[24]=claim_idtxt.getText()+"";
					data[25]=rdbtnCashless.isSelected()?"1":"0";
					try {
						insertedID = ipddbConnection.inserData(data);
						ipddbConnection.UpdateIpdId2(insertedID+"");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String[] data1 = new String[20];
					data1[0] = "" + insertedID;
					data1[1] = "" + ipd_doctorname;
					data1[2] = "" + ipd_doctor_id;
					data1[3] = "" + p_id;
					data1[4] = "" + p_name;
					data1[5] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data1[6] = "" + timeFormat.format(cal1.getTime());
					try {
						ipddbConnection.inserIPDDoctor(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					data1[0] = "" + insertedID;
					data1[1] = "" + p_id;
					data1[2] = "" + p_name;
					data1[3] = building_name;
					data1[4] = ward_name;
					data1[5] = wardRoomNoLB.getText();
					data1[6] = bed_no;
					data1[7] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data1[8] = "" + timeFormat.format(cal1.getTime());
					data1[9] = "" + ward_code;
					data1[10] = "" + wardCategorySTR;
					data1[11] = ReceptionMain.receptionNameSTR; // user id
					data1[12] = ReceptionMain.receptionNameSTR; // user name
					try {
						ipddbConnection.inserIPDBedDetail(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					ipddbConnection.closeConnection();
					WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
					String[] data2 = new String[10];
					data2[0] = "" + p_id;
					data2[1] = "" + p_name;
					data2[2] = "1";
					data2[3] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());

					try {
						dbConnection.updateData(data2, ward_code);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dbConnection.closeConnection();
					}
					dbConnection.closeConnection();

//					IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
//					int advancePaymentIndex = 0;
//					String data[] = new String[13];
//					data[0] = "" + insertedID;
//					data[1] = "" + p_id;
//					data[2] = "" + p_name;
//					data[3] = "P";
//					data[4] = "ADVANCE";
//					data[5] = "" + advancePayment;
//					data[6] = ""
//							+ DateFormatChange.StringToMysqlDate(new Date());
//					data[7] = "" + timeFormat.format(cal1.getTime());
//					data[8] = "" + ReceptionMain.receptionNameSTR;
//
//					try {
//						advancePaymentIndex = db.inserData(data);
//					} catch (Exception e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						db.closeConnection();
//					}
//					db.closeConnection();
//					try {
//						new AdvancePaymentSlippdf(ipd_doctorname, finalAdvance
//								+ "", ipdNoTB.getText(), p_name,
//								advancePaymentIndex + "", "Advance Payment");
//					} catch (DocumentException | IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				
//					if (paymentModeCB.getSelectedIndex() > 0) {
//						try {
//							PaymentDBConnection dbConnection1 = new PaymentDBConnection();
//
//							double roundOff = Math
//									.round((finalAdvance - advancePayment) * 100.0) / 100.0;
//							data[0] = paymentModeCB.getSelectedItem()
//									.toString();
//							data[1] = "IPD";
//							data[2] = insertedID + "";
//							data[3] = p_id;
//							data[4] = p_name;
//							data[5] = advancePayment + "";
//							data[6] = roundOff + "";
//							data[7] = finalAdvance + "";
//							data[8] = DateFormatChange
//									.StringToMysqlDate(new Date());
//							data[9] = "" + timeFormat.format(cal1.getTime());
//							data[10] = "" + ReceptionMain.receptionNameSTR;
//							dbConnection1.inserData(data);
//							dbConnection1.closeConnection();
//						} catch (Exception e) {
//							// TODO: handle exception
//						}
//
//					}
					JOptionPane.showMessageDialog(null,
							"Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					try {
						new IPDSlipPdfRegenrate(insertedID + "","1");
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					dispose();
					dispose();
					if (emergencyIpdBrowser != null)
						emergencyIpdBrowser.populateTable(
								""
										+ DateFormatChange
												.StringToMysqlDate(new Date()),
								""
										+ DateFormatChange
												.StringToMysqlDate(new Date()));

				}
			}
		});
		btnNewButton_3.setBounds(379, 535, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(536, 535, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(EmergencyIpd.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

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
					hospitalSchemeCB.setSelectedItem(p_scheme);
//					SchemeTB.setText(p_scheme);
					if (p_sex.equals("Male")) {
						rdbtnMale.setSelected(true);
						rdbtnFemale.setSelected(false);
					} else {
						rdbtnMale.setSelected(false);
						rdbtnFemale.setSelected(true);
					}

					itemsHashMap.clear();
					examHashMap.clear();

				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(772, 55, 164, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(695, 441, 313, 118);
		panel_4.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Note :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_3.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 297, 91);
		panel_3.add(scrollPane_1);

		examNote = new JTextArea();
		examNote.setLineWrap(true);
		examNote.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNote.setRows(10);
		scrollPane_1.setViewportView(examNote);

		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				NewPatient newPatient = new NewPatient();

				newPatient
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				newPatient.setLocationRelativeTo(contentPane);
//				newPatient.IPDEnteryObject(EmergencyIpd.this);
				newPatient.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(EmergencyIpd.class
				.getResource("/icons/plus_button.png")));
		btnNewButton.setBounds(941, 55, 50, 25);
		panel_4.add(btnNewButton);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_6.setBounds(369, 203, 316, 78);
		panel_4.add(panel_6);
		panel_6.setLayout(null);

		JLabel lblRefferedFrom = new JLabel("Reffered From :");
		lblRefferedFrom.setBounds(10, 31, 101, 14);
		panel_6.add(lblRefferedFrom);
		lblRefferedFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		refferenceDoctorTB = new JTextField();
		refferenceDoctorTB.setBounds(114, 24, 196, 29);
		panel_6.add(refferenceDoctorTB);
		refferenceDoctorTB.setToolTipText("");
		refferenceDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		refferenceDoctorTB.setColumns(10);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(369, 292, 316, 232);
		panel_4.add(panel_7);
		panel_7.setLayout(null);
		
		JLabel lblSurgicalCode = new JLabel("Surgical Code");
		lblSurgicalCode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSurgicalCode.setBounds(10, 21, 101, 14);
		panel_7.add(lblSurgicalCode);
		
		SurgicalCodeTb = new JTextField();
		SurgicalCodeTb.setToolTipText("");
		SurgicalCodeTb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		SurgicalCodeTb.setColumns(10);
		SurgicalCodeTb.setBounds(120, 19, 186, 21);
		panel_7.add(SurgicalCodeTb);
		
		JLabel lblApprovedAmount = new JLabel("Approved Amount");
		lblApprovedAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblApprovedAmount.setBounds(0, 64, 111, 14);
		panel_7.add(lblApprovedAmount);
		
		TFApprovedAmnt = new JTextField();
		TFApprovedAmnt.setToolTipText("");
		TFApprovedAmnt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TFApprovedAmnt.setColumns(10);
		TFApprovedAmnt.setBounds(120, 61, 186, 21);
		panel_7.add(TFApprovedAmnt);
		
		JLabel LblEmergency = new JLabel("  Remarks");
		LblEmergency.setFont(new Font("Tahoma", Font.PLAIN, 12));
		LblEmergency.setBounds(3, 114, 108, 14);
		panel_7.add(LblEmergency);
		
		
		 remarks = new JTextField(); 
//		remarks.setRows(100);
//		 cmbEmergency = new JComboBox();
//		
//		    cmbEmergency.addItemListener(new ItemListener() {
//			@Override
//			public void itemStateChanged(ItemEvent ie) {
//				String str_emergency = (String) cmbEmergency.getSelectedItem();
//				emergency = str_emergency;
//			}
//		});
//			cmbEmergency.setModel(new DefaultComboBoxModel(new String[] {"Select Emergency OPD", "No",
//			"Yes"}));
		remarks.setFont(new Font("Tahoma", Font.PLAIN, 12));
		remarks.setBounds(120, 109, 186, 25);
		panel_7.add(remarks);
		
		JLabel lblInsurancePatient = new JLabel("Type : -");
		lblInsurancePatient.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblInsurancePatient.setBounds(10, 168, 144, 15);
		panel_7.add(lblInsurancePatient);
		
		rdbtnCash = new JRadioButton("Cash");
		rdbtnCash.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCash.setBounds(130, 164, 60, 23);
		panel_7.add(rdbtnCash);
		cashgroup.add(rdbtnCash);
		
		rdbtnCashless = new JRadioButton("Cashless");
		rdbtnCashless.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCashless.setBounds(200, 164, 100, 23);
		panel_7.add(rdbtnCashless);
		cashgroup.add(rdbtnCashless);
			
			
//			JTextArea remarks = new JTextArea(100,100);
//			remarks.setFont(new Font("Tahoma", Font.PLAIN, 12));
//			remarks.setLineWrap(true);
////			remarks.setRows(100);
//			remarks.setBounds(120, 109, 186, 25);
//			panel_7.add(remarks);
//		String str_emergency = (String) cmbEmergency.getSelectedItem();
//		System.out.println("ll"
//				+emergency);
//	

//		JLabel lblAdvancePayment = new JLabel("Advance Payment :");
//		lblAdvancePayment.setBounds(10, 66, 108, 14);
//		panel_7.add(lblAdvancePayment);
//		lblAdvancePayment.setFont(new Font("Tahoma", Font.PLAIN, 12));
//
//		advancePaymentTB = new JTextField();
//		advancePaymentTB.setBounds(139, 59, 167, 25);
//		advancePaymentTB.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				char vChar = e.getKeyChar();
//				if (!(Character.isDigit(vChar)
//						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
//					e.consume();
//				}
//			}
//		});
//		panel_7.add(advancePaymentTB);
//		advancePaymentTB.setFont(new Font("Tahoma", Font.BOLD, 17));
//		advancePaymentTB.setColumns(10);

//		paymentModeCB = new JComboBox();
//		paymentModeCB.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				try {
//					paymentModeCB.getSelectedItem().toString();
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}
//		});
//		getAllPaymentModes();
//		paymentModeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
//		paymentModeCB.setBounds(139, 11, 167, 25);
//		panel_7.add(paymentModeCB);

//		JLabel label = new JLabel("Payment Mode :");
//		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
//		label.setBounds(10, 11, 296, 25);
//		panel_7.add(label);

		JPanel panel_8 = new JPanel();
		panel_8.setLayout(null);
		panel_8.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,

		TitledBorder.TOP, null, null));
		panel_8.setBounds(369, 101, 316, 78);
		panel_4.add(panel_8);

		JLabel lblIpdNo = new JLabel("IPD No. :");
		lblIpdNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblIpdNo.setBounds(10, 31, 101, 14);
		panel_8.add(lblIpdNo);

		ipdNoTB = new JTextField(ipd_counter + "");
		ipdNoTB.setVisible(false);
		ipdNoTB.setEditable(false);
		ipdNoTB.setToolTipText("");
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setColumns(10);
		ipdNoTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		ipdNoTB.setBounds(114, 24, 196, 29);
		panel_8.add(ipdNoTB);
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

		getAllBuilding();
		getAllPackages();
		getAllProcedures();
		
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				searchPatientTB.requestFocus();
			}
		});
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
				p_scheme = resultSet.getObject(9).toString();
				if(resultSet.getBoolean(12))
					rdbtnCashless.setSelected(true);
				else
					rdbtnCash.setSelected(true);
				if(p_insurancetype.equals("Unknown")){
					rdbtnCashless.setEnabled(false);
					rdbtnCash.setEnabled(false);
				}else {
					rdbtnCashless.setEnabled(true);
					rdbtnCash.setEnabled(true);
				}
			}
			if(p_insurancetype.equals("ECHS") || p_insurancetype.equals("Railway") || p_insurancetype.equals("CGHS"))
			{
				claim_idtxt.setEnabled(true);	
				
			}else
			{
				claim_idtxt.setEnabled(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// ipddbConnection = new IPDDBConnection();
		// String lastOPDDate =
		// ipddbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// ipddbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	public void getAllPackages() {
		packagesCBM.removeAllElements();

		packagesCBM.addElement("General");
		packagesCBM.addElement("Minor");
		packagesCBM.addElement("Minor (With Anesthesia)");
		packagesCBM.addElement("Major");
		packagesCBM.addElement("Major (With Anesthesia)");
		indoorPackageCB.setModel(packagesCBM);
		indoorPackageCB.setSelectedIndex(0);
	}

	public void getAllDoctors() {
		doctors.addElement("Select Doctor");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctors.addElement("Other");
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
	}

	public void getAllPaymentModes() {
		paymentModes.addElement("CASH");
		paymentCharges.add(0);
		PaymentDBConnection dbConnection = new PaymentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllPaymentMode();
		try {
			while (resultSet.next()) {
				paymentModes.addElement(resultSet.getObject(1).toString());
				paymentCharges.add(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		paymentModeCB.setModel(paymentModes);
		paymentModeCB.setSelectedIndex(0);
	}

	public void getDoctorDetail(String name) {
		ipd_doctor_id = "";
		doctorSpecializationLB.setText("");
		doctorRoomLB.setText("");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				ipd_doctor_id = resultSet.getObject(1).toString();
				doctorSpecializationLB.setText(resultSet.getObject(2)
						.toString());
				doctorRoomLB.setText(resultSet.getObject(3).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getAllBuilding() {
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllBuilding();
		try {
			while (resultSet.next()) {
				building.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		buildingCB.setModel(building);
		buildingCB.setSelectedIndex(0);
	}

	public void getAllWards() {
		wards.removeAllElements();
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWards(building_name);
		try {
			while (resultSet.next()) {
				wards.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		wardCB.setModel(wards);
		wardCB.setSelectedIndex(0);
	}

	public void getAllBed() {
		bedno.removeAllElements();
		bedIndex.clear();
		bedno.addElement("Select Bed No");
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllBeds(building_name,
				ward_name);
		try {
			while (resultSet.next()) {
				bedIndex.add(resultSet.getObject(1).toString());
				bedno.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		bedNoCB.setModel(bedno);
		if (bedIndex.size() > 0) {
			bedNoCB.setSelectedIndex(1);

		} else {
			bedNoCB.setSelectedIndex(0);
		}
	}

	public void getWardDetail() {
		wardInchargeLB.setText("");
		wardRoomNoLB.setText("");
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData(ward_code);
		try {
			while (resultSet.next()) {
				wardInchargeLB.setText(resultSet.getObject(2).toString());
				wardRoomNoLB.setText(resultSet.getObject(1).toString());
				wardCategorySTR = resultSet.getObject(3).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void insertPackageAmount(String ipd_id) {
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		IPDExpensesDBConnection db = new IPDExpensesDBConnection();
		String[] data1 = new String[15];
		data1[0] = ipd_id;
		data1[1] = indoorPackageCB.getSelectedItem().toString();

		data1[2] = indoorPackageCB.getSelectedItem().toString() + " Charges";
		data1[3] = "" + packageAmountLB.getText();

		data1[4] = "" + DateFormatChange.StringToMysqlDate(new Date());
		data1[5] = "" + timeFormat.format(cal1.getTime());
		data1[6] = "n/a";
		data1[7] = "" + p_id;
		data1[8] = "" + p_name;
		data1[9] = data1[3];
		data1[10] = "1";
		data1[11] = "PROCEDURE";
		data1[12] = ReceptionMain.receptionNameSTR;
		data1[13] = "PROCEDURE";
		data1[14] = "NA";
		try {
			db.inserData(data1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			db.closeConnection();
		}
		db.closeConnection();

	}

	public boolean getIPDDATA() {

		boolean flag = false;
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientID(p_id);
		try {
			while (resultSet.next()) {
				flag = true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return flag;
	}
	
	public void getAllProcedures() {
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();

		procedureName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				
				procedureName.addElement(resultSet.getObject(2).toString());
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		operationTypeCB.setModel(procedureName);
		if (i > 0) {
			operationTypeCB.setSelectedIndex(0);
		}
		operationTypeCB.setSelectedItem("");
	}
}
