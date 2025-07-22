package hms.patient.data_bundle;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.main.NewsDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.PatientsTotalBillPDF;

import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.ComboBoxModel;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;

public class PatientHistoryECHS extends JDialog {

	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	String[] data = new String[20];
	public boolean getOPD = true;
	private JTextField insuranceTypeTB;
	ButtonGroup opdSearchOption = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown",
			p_insuranceNumber = "0", p_note = "";
	String opd_doctorname = "", opd_doctorid = "", opd_date = "",
			opd_symptom = "", opd_token = "", opd_diseasetype = "ALL TYPE",
			opd_type = "Unknown", opd_refferal = "", opd_charge = "";
	String symptomsTA = "", prescriptionTA = "";
	
	String submissionDarteSTR="",referralDateSTR="",admissionDateSTR="",dischargeDateSTR="";
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	OPDDBConnection opddbConnection;
	private JPanel searchPatientPanel;
	PatientOPD opdHistory = new PatientOPD();
	PatientTest testHistory = new PatientTest();
	PatientIPD ipdHistory = new PatientIPD();
	ImageIcon opdHistoryIcon = new ImageIcon(
			PatientHistoryECHS.class.getResource("/icons/users.png"));
	ImageIcon testHistoryIcon = new ImageIcon(
			PatientHistoryECHS.class.getResource("/icons/list_button.png"));
	ImageIcon ipdHistoryIcon = new ImageIcon(
			PatientHistoryECHS.class.getResource("/icons/list_button.png"));
	int tokenCounter = 1;
	public String DoctorNameSTR = "";
	int totalOPD = 0;
	private JDateChooser dateToChooser;
	private JDateChooser dateFromChooser;
	String insuranceSTR, dateFromSTR, dateToSTR;
	private JTextField billNumberTF;
	private JTextField patientNameTF;
	private JTextField diagnosisTF;
	private JTextField referredByTF;
	private JTextField insuranceNumberTF;
	HashMap<String, String> hm = new HashMap<String, String>();
	private JDateChooser referralDateDC;
	private JDateChooser submissionDateDC;

	
	public static String BILL_NUMBER_KEY="bill_number";
	public static String SUBMMISSION_DATE_KEY="submission_date";
	public static String REFERRAL_DATE_KEY="referall_date";
	public static String PATIENT_NAME_KEY="patient_name";
	public static String DIAGNOSIS_KEY="diagnosis";
	public static String REFERRED_BY_KEY="referral_by";
	public static String ADMISSION_DATE_BY_KEY="admission_by";
	public static String DISCHARGE_DATE_BY_KEY="discharge_by";
	public static String REF_NO_BY_KEY="ref_no_by";
	public static String IPD_NO_BY_KEY="ipd_no_by";
	public static String TYPE_BY_KEY="type_by";
	public static String DOCTOR_NAME_KEY="doctor_by";
	public static String CARD_NO_KEY="card_by";
	
	public static String ATTACHEMNT_1="attachemnt_1";
	public static String ATTACHEMNT_2="attachemnt_2";
	public static String ATTACHEMNT_3="attachemnt_3";
	public static String ATTACHEMNT_4="attachemnt_4";
	public static String ATTACHEMNT_5="attachemnt_5";
	public static String ATTACHEMNT_6="attachemnt_6";
	public static String ATTACHEMNT_7="attachemnt_7";
	
	public static String PAGE_NUMBER_1="page_no_1";
	public static String PAGE_NUMBER_2="page_no_1";
	public static String PAGE_NUMBER_3="page_no_1";
	public static String PAGE_NUMBER_4="page_no_1";
	public static String PAGE_NUMBER_5="page_no_1";
	public static String PAGE_NUMBER_6="page_no_1";
	public static String PAGE_NUMBER_7="page_no_1";
	
	private JTextField ipdNoTF;
	private JTextField refNoTF;
	private JComboBox typeCB;
	private JDateChooser admissionDC;
	private JDateChooser dischargeDC;
	private JTextField doctorNameTF;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;
	private JTextField textField_7;
	private JPanel panel_1;
	private JComboBox comboBox_3;
	private JComboBox comboBox_4;
	private JComboBox comboBox_2;
	private JComboBox comboBox_6;
	private JComboBox comboBox;
	private JComboBox comboBox_5;
	private JComboBox comboBox_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		PatientHistoryECHS frame = new PatientHistoryECHS();
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public PatientHistoryECHS() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				PatientHistoryECHS.class.getResource("/icons/rotaryLogo.png")));
		setTitle("ECHS Bill Format");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(10, 5, 1302, 651);
		contentPane = new JPanel();
		testHistory.getComponent(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient History",
				TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_1.setBounds(10, 11, 1280, 601);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		opddbConnection = new OPDDBConnection();
		opd_token = opddbConnection.retrieveCounterTodayToken() + "";

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(10, 11, 302, 479);
		panel_1.add(panel_7);
		panel_7.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(5, 171, 293, 297);
		panel_7.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 21, 90, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 16, 175, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 57, 108, 12);
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
		addressTB.setBounds(106, 52, 175, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 88, 175, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 124, 175, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 160, 175, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 192, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 192, 93, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 230, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 222, 175, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);
		
		JLabel lblInsuranceNo = new JLabel("Insurance No. :");
		lblInsuranceNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInsuranceNo.setBounds(6, 266, 108, 14);
		panel.add(lblInsuranceNo);
		
		insuranceNumberTF = new JTextField();
		insuranceNumberTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceNumberTF.setColumns(10);
		insuranceNumberTF.setBounds(106, 258, 175, 25);
		panel.add(insuranceNumberTF);
		NewsDBConnection newsDBConnection = new NewsDBConnection();
		newsDBConnection.closeConnection();

		JLabel lblAddSomePrescription = new JLabel("Add Prescription & Symptom");
		lblAddSomePrescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddSomePrescription.setBounds(367, 463, -2, 19);
		panel_7.add(lblAddSomePrescription);
		searchPatientPanel = new JPanel();
		searchPatientPanel.setBounds(5, 73, 293, 87);
		panel_7.add(searchPatientPanel);
		searchPatientPanel.setBorder(null);
		searchPatientPanel.setLayout(null);

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 23, 108, 14);
		searchPatientPanel.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPatientsID(searchPatientTB.getText());
				
			}
		});
		searchPatientTB.setBounds(101, 20, 147, 25);
		searchPatientPanel.add(searchPatientTB);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		JButton searchBT = new JButton("");
		searchBT.setBounds(258, 20, 28, 25);
		searchPatientPanel.add(searchBT);
		searchBT.setToolTipText("Search");
		searchBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				InsurancePatientList insurancePatientList=new InsurancePatientList();
				//insurancePatientList.setContext(PatientHistoryRailway.this);
				insurancePatientList.setModal(true);
				insurancePatientList.setVisible(true);
			}
		});
		searchBT.setIcon(new ImageIcon(PatientHistoryECHS.class
				.getResource("/icons/zoom_r_button.png")));
		searchBT.setFocusable(true);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(6, 51, 77, 20);
		searchPatientPanel.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientIDCB = new JComboBox(patientID);
		patientIDCB.setBounds(101, 51, 186, 25);
		searchPatientPanel.add(patientIDCB);
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

				// System.out.println(p_id + "    "
				// + patientIDCB.getSelectedIndex());
				opddbConnection = new OPDDBConnection();

				opddbConnection.closeConnection();
				setPatientDetail(p_id);
				if (patientID.getSize() > 0) {
					patientNameTB.setText(p_name);
					addressTB.setText(p_address);
					ageTB.setText(p_age + "  (Y-M-D)");
					cityTB.setText(p_city);
					telephoneTB.setText(p_telephone);
					insuranceTypeTB.setText(p_insurancetype);
					insuranceNumberTF.setText(p_insuranceNumber);
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

		JLabel label = new JLabel("Date From");
		label.setBounds(10, 11, 90, 20);
		panel_7.add(label);

		dateFromChooser = new JDateChooser();
		dateFromChooser.setBounds(83, 11, 215, 20);
		panel_7.add(dateFromChooser);

		JLabel label_1 = new JLabel("Date To");
		label_1.setBounds(10, 42, 90, 20);
		panel_7.add(label_1);

		dateToChooser = new JDateChooser();
		dateToChooser.setBounds(83, 42, 215, 20);
		panel_7.add(dateToChooser);

		dateFromChooser.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFromSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		dateFromChooser.setDate(new Date());

		dateToChooser.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateToSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		dateToChooser.setDate(new Date());
/*
 * for search on every index...
		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
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
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
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
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
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
						}
					}
				});
		*/
		opd_date = DateFormatChange.StringToMysqlDate(new Date());
		

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_8.setBounds(635, 21, 635, 479);
		panel_1.add(panel_8);
		panel_8.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(6, 11, 619, 470);
		panel_8.add(tabbedPane);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addTab("Patient OPD History", opdHistoryIcon, opdHistory,
				"Patient OPD History");
		tabbedPane.addTab("Patient Test History", testHistoryIcon, testHistory);
		tabbedPane.addTab("Patient IPD History", testHistoryIcon, ipdHistory,
				"Patient IPD History");
		
		JButton btnAllData = new JButton("Print Bill");
		btnAllData.setBounds(1029, 517, 230, 41);
		panel_1.add(btnAllData);
		btnAllData.setFont(new Font("Tahoma", Font.BOLD, 13));
		
		billNumberTF = new JTextField();
		billNumberTF.setText("RACGH/ECHS/");
		billNumberTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		billNumberTF.setColumns(10);
		billNumberTF.setBounds(423, 21, 202, 25);
		panel_1.add(billNumberTF);
		
		JLabel lblBillNumber = new JLabel("Bill Number :");
		lblBillNumber.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBillNumber.setBounds(323, 26, 97, 14);
		panel_1.add(lblBillNumber);
		
		patientNameTF = new JTextField();
		patientNameTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTF.setColumns(10);
		patientNameTF.setBounds(423, 143, 202, 25);
		panel_1.add(patientNameTF);
		
		JLabel lblPatientName_1 = new JLabel("IP Name :");
		lblPatientName_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPatientName_1.setBounds(323, 148, 97, 14);
		panel_1.add(lblPatientName_1);
		
		diagnosisTF = new JTextField();
		diagnosisTF.setText("For Admit");
		diagnosisTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		diagnosisTF.setColumns(10);
		diagnosisTF.setBounds(423, 173, 202, 25);
		panel_1.add(diagnosisTF);
		
		JLabel lblDiagnosis = new JLabel("Diagnosis :");
		lblDiagnosis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDiagnosis.setBounds(323, 178, 97, 14);
		panel_1.add(lblDiagnosis);
		
		referredByTF = new JTextField();
		referredByTF.setText("ESI Dispensary, Ambala Cantt");
		referredByTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		referredByTF.setColumns(10);
		referredByTF.setBounds(423, 203, 202, 25);
		panel_1.add(referredByTF);
		
		JLabel lblReferralBy = new JLabel("Referral By :");
		lblReferralBy.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblReferralBy.setBounds(323, 208, 97, 14);
		panel_1.add(lblReferralBy);
		
		submissionDateDC = new JDateChooser();
		submissionDateDC.setBounds(424, 51, 201, 20);
		panel_1.add(submissionDateDC);
		
		submissionDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							submissionDarteSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		
		referralDateDC = new JDateChooser();
		referralDateDC.setBounds(424, 74, 201, 20);
		panel_1.add(referralDateDC);
		referralDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							referralDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		
		JLabel lblDateOfReferaral = new JLabel("Date of referral");
		lblDateOfReferaral.setBounds(322, 74, 90, 20);
		panel_1.add(lblDateOfReferaral);
		
		JLabel lblDateOfSubmission = new JLabel("Date of submission");
		lblDateOfSubmission.setBounds(322, 51, 90, 20);
		panel_1.add(lblDateOfSubmission);
		
		typeCB = new JComboBox(new DefaultComboBoxModel(new String[] {"General", "Semi Private", "Private"}));
		typeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		typeCB.setBounds(423, 233, 202, 25);
		panel_1.add(typeCB);
		
		JLabel lblType = new JLabel("Type :");
		lblType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblType.setBounds(322, 233, 77, 20);
		panel_1.add(lblType);
		
		JLabel lblPanNo = new JLabel("IPD No. :");
		lblPanNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPanNo.setBounds(323, 269, 97, 14);
		panel_1.add(lblPanNo);
		
		ipdNoTF = new JTextField();
		ipdNoTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTF.setColumns(10);
		ipdNoTF.setBounds(423, 264, 202, 25);
		panel_1.add(ipdNoTF);
		
		JLabel lblRefNo = new JLabel("Ref. No. :");
		lblRefNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRefNo.setBounds(322, 299, 97, 14);
		panel_1.add(lblRefNo);
		
		refNoTF = new JTextField();
		refNoTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		refNoTF.setColumns(10);
		refNoTF.setBounds(422, 294, 202, 25);
		panel_1.add(refNoTF);
		
		JLabel lblDateOfAdmission = new JLabel("Date of Admission");
		lblDateOfAdmission.setBounds(322, 96, 90, 20);
		panel_1.add(lblDateOfAdmission);
		
		admissionDC = new JDateChooser();
		admissionDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							admissionDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		
		admissionDC.setBounds(424, 96, 201, 20);
		panel_1.add(admissionDC);
		
		JLabel lblDateOfDischarge = new JLabel("Date of Discharge");
		lblDateOfDischarge.setBounds(322, 118, 90, 20);
		panel_1.add(lblDateOfDischarge);
		
		dischargeDC = new JDateChooser();
		dischargeDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dischargeDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		
		dischargeDC.setBounds(424, 118, 201, 20);
		panel_1.add(dischargeDC);
		
		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(322, 329, 97, 14);
		panel_1.add(lblDoctorName);
		
		doctorNameTF = new JTextField();
		doctorNameTF.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorNameTF.setColumns(10);
		doctorNameTF.setBounds(422, 324, 202, 25);
		panel_1.add(doctorNameTF);
		
		comboBox = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(423, 373, 106, 25);
		panel_1.add(comboBox);
		
		JLabel lblEchs = new JLabel("ECHS");
		lblEchs.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEchs.setBounds(322, 373, 98, 25);
		panel_1.add(lblEchs);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_1.setColumns(10);
		textField_1.setBounds(533, 373, 97, 25);
		panel_1.add(textField_1);
		
		JLabel lblPageNumber = new JLabel("Page Number");
		lblPageNumber.setBounds(539, 360, 86, 14);
		panel_1.add(lblPageNumber);
		
		JLabel lblBillsPage = new JLabel("Bills Page");
		lblBillsPage.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBillsPage.setBounds(322, 403, 98, 25);
		panel_1.add(lblBillsPage);
		
		comboBox_1 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_1.setBounds(423, 403, 106, 25);
		panel_1.add(comboBox_1);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_2.setColumns(10);
		textField_2.setBounds(533, 403, 97, 25);
		panel_1.add(textField_2);
		
		JLabel lblEchsSmartCard = new JLabel("ECHS Smart Card");
		lblEchsSmartCard.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEchsSmartCard.setBounds(322, 432, 98, 25);
		panel_1.add(lblEchsSmartCard);
		
		comboBox_2 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_2.setBounds(423, 432, 106, 25);
		panel_1.add(comboBox_2);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_3.setColumns(10);
		textField_3.setBounds(533, 432, 97, 25);
		panel_1.add(textField_3);
		
		JLabel lblStaisfactionCer = new JLabel("Staisfaction Certificate");
		lblStaisfactionCer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblStaisfactionCer.setBounds(322, 465, 98, 25);
		panel_1.add(lblStaisfactionCer);
		
		comboBox_3 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_3.setBounds(423, 465, 106, 25);
		panel_1.add(comboBox_3);
		
		textField_4 = new JTextField();
		textField_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_4.setColumns(10);
		textField_4.setBounds(533, 465, 97, 25);
		panel_1.add(textField_4);
		
		JLabel lblDichargeSummery = new JLabel("Dicharge Summery");
		lblDichargeSummery.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDichargeSummery.setBounds(323, 496, 98, 25);
		panel_1.add(lblDichargeSummery);
		
		comboBox_4 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_4.setBounds(424, 496, 106, 25);
		panel_1.add(comboBox_4);
		
		textField_5 = new JTextField();
		textField_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_5.setColumns(10);
		textField_5.setBounds(534, 496, 97, 25);
		panel_1.add(textField_5);
		
		JLabel lblInvestigationReport = new JLabel("Investigation Report");
		lblInvestigationReport.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInvestigationReport.setBounds(323, 533, 98, 25);
		panel_1.add(lblInvestigationReport);
		
		comboBox_5 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_5.setBounds(424, 533, 106, 25);
		panel_1.add(comboBox_5);
		
		textField_6 = new JTextField();
		textField_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_6.setColumns(10);
		textField_6.setBounds(534, 533, 97, 25);
		panel_1.add(textField_6);
		
		JLabel lblSummery = new JLabel("Summery");
		lblSummery.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSummery.setBounds(323, 565, 98, 25);
		panel_1.add(lblSummery);
		
		comboBox_6 = new JComboBox(new DefaultComboBoxModel(new String[] {"Attached", "N/A"}));
		comboBox_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox_6.setBounds(424, 565, 106, 25);
		panel_1.add(comboBox_6);
		
		textField_7 = new JTextField();
		textField_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		textField_7.setColumns(10);
		textField_7.setBounds(534, 565, 97, 25);
		panel_1.add(textField_7);
		btnAllData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(p_id.equals(""))
				{
					
				}else {
			
					 hm.put(BILL_NUMBER_KEY,billNumberTF.getText().toString());  
					 hm.put(SUBMMISSION_DATE_KEY,submissionDarteSTR);  
					 hm.put(REFERRAL_DATE_KEY,referralDateSTR);  
					 hm.put(PATIENT_NAME_KEY,patientNameTF.getText().toString());  
					 hm.put(DIAGNOSIS_KEY,diagnosisTF.getText().toString());  
					 hm.put(REFERRED_BY_KEY,referredByTF.getText().toString());
					 hm.put(ADMISSION_DATE_BY_KEY,admissionDateSTR);
					 hm.put(DISCHARGE_DATE_BY_KEY,dischargeDateSTR);
					 hm.put(REF_NO_BY_KEY,refNoTF.getText().toString());
					 hm.put(IPD_NO_BY_KEY,ipdNoTF.getText().toString());
					 hm.put(TYPE_BY_KEY,typeCB.getSelectedItem().toString());
					 hm.put(DOCTOR_NAME_KEY,doctorNameTF.getText().toString());
					 hm.put(CARD_NO_KEY,insuranceNumberTF.getText().toString());
					 
					 hm.put(ATTACHEMNT_1,comboBox.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_2,comboBox_1.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_3,comboBox_2.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_4,comboBox_3.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_5,comboBox_4.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_6,comboBox_5.getSelectedItem().toString());
					 hm.put(ATTACHEMNT_7,comboBox_6.getSelectedItem().toString());
					 
					 
					 hm.put(PAGE_NUMBER_1,textField_1.getText().toString());
					 hm.put(PAGE_NUMBER_2,textField_2.getText().toString());
					 hm.put(PAGE_NUMBER_3,textField_3.getText().toString());
					 hm.put(PAGE_NUMBER_4,textField_4.getText().toString());
					 hm.put(PAGE_NUMBER_5,textField_5.getText().toString());
					 hm.put(PAGE_NUMBER_6,textField_6.getText().toString());
					 hm.put(PAGE_NUMBER_7,textField_7.getText().toString());
					try {
						new PatientsECHSDataBill(hm,p_id,dateFromSTR,dateToSTR);
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
				}
			}
		});

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MainLogin frame = new MainLogin();
				frame.setVisible(true);
				dispose();
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

	}

	public void getPatientsID(String index) {
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

		opdHistory.populateOPDHistoryTable(indexId, dateFromSTR, dateToSTR);
		testHistory.populateTestHistoryTable(indexId, dateFromSTR, dateToSTR);
		ipdHistory.populateIPDHistoryTable(indexId);

		PatientDBConnection patientDBConnection = null;
		try {
			patientDBConnection = new PatientDBConnection();
			ResultSet resultSet = patientDBConnection
					.retrieveDataWithIndex(indexId);

			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_age = resultSet.getObject(2).toString();
				p_sex = resultSet.getObject(3).toString();
				p_address = resultSet.getObject(4).toString();
				p_city = resultSet.getObject(5).toString();
				p_telephone = resultSet.getObject(6).toString();
				p_insurancetype = resultSet.getObject(7).toString();
				p_insuranceNumber = resultSet.getObject(8).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opddbConnection = new OPDDBConnection();
		String lastOPDDate = opddbConnection.retrieveLastOpdPatient(indexId);
		opddbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	public void writeCounter(String counter) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		try {
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	public JPanel getPanel_4() {
		return panel_1;
	}
	public JTextField getTextField_1() {
		return textField_1;
	}
	public JTextField getTextField_4() {
		return textField_4;
	}
	public JComboBox getComboBox_3() {
		return comboBox_3;
	}
	public JComboBox getComboBox_4() {
		return comboBox_4;
	}
	public JTextField getTextField_7() {
		return textField_7;
	}
	public JTextField getTextField_2() {
		return textField_2;
	}
	public JComboBox getComboBox_2() {
		return comboBox_2;
	}
	public JComboBox getComboBox_6() {
		return comboBox_6;
	}
	public JComboBox getComboBox() {
		return comboBox;
	}
	public JTextField getTextField_3() {
		return textField_3;
	}
	public JTextField getTextField_5() {
		return textField_5;
	}
	public JComboBox getComboBox_5() {
		return comboBox_5;
	}
	public JComboBox getComboBox_1() {
		return comboBox_1;
	}
	public JTextField getTextField_6() {
		return textField_6;
	}
}
