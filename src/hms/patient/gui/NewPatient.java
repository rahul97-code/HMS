package hms.patient.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.crypto.CryptoHandler;
import hms.exams.gui.ExamEntery;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.opd.gui.OPDEntery;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.IDCardSlip;
import hms.reception.gui.ReceptionMain;
import hms.store.gui.NewInvoice;
import hms1.ipd.gui.IPDEntery;

import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import org.apache.commons.lang3.text.WordUtils;

import com.toedter.calendar.JDateChooser;
import javax.swing.JPasswordField;


public class NewPatient extends JDialog {

	private JPanel contentPane;
	private JTextField patientIdTB;
	private JTextField patientNameTB;
	private JPanel panel_2;
	private JPanel birthDatePanel;
	private JPanel agePanel;
	// Group the radio buttons.
	ButtonGroup agegroup = new ButtonGroup();
	ButtonGroup sexgroup = new ButtonGroup();
	ButtonGroup guardiangroup = new ButtonGroup();
	ButtonGroup Cashgroup = new ButtonGroup();
	ButtonGroup insurancegroup = new ButtonGroup();
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField stateTB;
	private JTextField telephoneTB;
	private JComboBox selectYCombo;
	private JTextField guardianNameTB;
	private JComboBox insuranceTypeCB;
	public String patientNumber;
	public int totalPatient = 0;
	String mobile_verified="OTP Verified";
	JComboBox bloodTypeCB,hospitalSchemeCB;
	String[] data = new String[23];
	String p_id, p_code, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",ins_category="",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",p_insurance_no="",p_scheme="";
	DateFormatChange dateFormat = new DateFormatChange();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	private JComboBox selectMcombo;
	private JComboBox selectDcombo;
	private JTextArea noteTA;
	PatientDBConnection patientDBConnection;
	String year;
	private JTextField insuranceNoTB;
	private JPanel insurancePanel;
	OPDEntery opdEntery=null;
	ExamEntery examEntery=null;
	PatientBrowser patientBrowser=null;
	IPDEntery ipdEntery=null;
	private JButton clearPicBT;
	private JButton takePicBT;
	public JLabel patientImageTV;
	public boolean setImage=false;
	String mainDir = "";
	static String OS;
	String[] open=new String[4];

	String imagePathSTR="";
	int otpverified=0;
	private JComboBox insuranceCatCB_1;
	private JRadioButton rdbtnCashless;
	private JRadioButton rdbtnCash;
	private JPasswordField aadhaarTF;
	private JPasswordField aadhaarConfimTF;

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
		// NewPatient frame = new NewPatient();
		// frame.setVisible(true);
		// System.out.print("daasdasd");
		new NewPatient().setVisible(true);

	}

	/**
	 * Create the frame.
	 */
	public NewPatient() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewPatient.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		setTitle("Patient Entery Form");
		setBounds(200, 100, 868, 589);
		readFile();
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		patientDBConnection = new PatientDBConnection();
		totalPatient = patientDBConnection.retrieveCounterData() + 1;
		StringBuffer ss = new StringBuffer(totalPatient + "");
		while (ss.length() < 7) {
			ss.insert(0, '0');
		}
		System.out.println(ss);
		p_code = ss.toString();
		year = Calendar.getInstance().get(Calendar.YEAR) + "";
		year = year.substring(year.length() - 2);
		patientNumber = "Patient Entery Form ( Code : " + totalPatient + " )";
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), patientNumber,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_7.setBounds(10, 0, 842, 540);
		contentPane.add(panel_7);
		panel_7.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 18, 313, 418);
		panel_7.add(panel_1);
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lblPatientId = new JLabel("Patient ID* :");
		lblPatientId.setBounds(6, 19, 84, 14);
		panel_1.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientName = new JLabel("Patient Name*:");
		lblPatientName.setBounds(6, 57, 98, 17);
		panel_1.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Age* :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setBounds(6, 85, 297, 99);
		panel_1.add(panel);
		panel.setLayout(null);
		JRadioButton rdbtnAge = new JRadioButton("Age");
		rdbtnAge.setSelected(false);
		rdbtnAge.setEnabled(false);
		rdbtnAge.setBounds(66, 16, 82, 25);
		panel.add(rdbtnAge);
		rdbtnAge.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				agePanel.setVisible(true);
				birthDatePanel.setVisible(false);
				p_agecode = "age";
			}
		});
		rdbtnAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAge);

		JRadioButton rdbtnBirthdate = new JRadioButton("Birthdate");
		rdbtnBirthdate.setSelected(true);
		rdbtnBirthdate.setBounds(150, 16, 114, 25);
		panel.add(rdbtnBirthdate);
		rdbtnBirthdate.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				birthDatePanel.setVisible(true);
				agePanel.setVisible(false);
				p_agecode = "birth";
			}
		});
		rdbtnBirthdate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnBirthdate);
		
		birthDatePanel = new JPanel();
		birthDatePanel.setBounds(6, 48, 281, 44);
		panel.add(birthDatePanel);
		birthDatePanel.setLayout(null);
		birthDatePanel.setVisible(false);

		JLabel lblNewLabel_1 = new JLabel("BirthDate :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 11, 73, 22);
		birthDatePanel.add(lblNewLabel_1);

		JDateChooser birthDateDC = new JDateChooser();
		birthDateDC.setBounds(92, 11, 152, 25);
		birthDatePanel.add(birthDateDC);
		birthDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							p_birthdate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							//calculateAge(p_birthdate);
						}
					}
				});
		birthDateDC.setMaxSelectableDate(new Date());
		birthDateDC.setDateFormatString("yyyy-MM-dd");

		agePanel = new JPanel();
		agePanel.setBounds(6, 48, 281, 44);
		panel.add(agePanel);
		agePanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Age :");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 46, 22);
		agePanel.add(lblNewLabel);

		selectYCombo = new JComboBox();
		selectYCombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) selectYCombo.getSelectedItem();
				if(str.equals("Years"))
				{
					p_ageY = "0";
				}
				else {
					p_ageY = str;
				}

			}
		});
		selectYCombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		selectYCombo.setModel(new DefaultComboBoxModel(new String[] { "Years",
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
				"23", "24", "25", "26", "27", "28", "29", "30", "31", "32",
				"33", "34", "35", "36", "37", "38", "39", "40", "41", "42",
				"43", "44", "45", "46", "47", "48", "49", "50", "51", "52",
				"53", "54", "55", "56", "57", "58", "59", "60", "61", "62",
				"63", "64", "65", "66", "67", "68", "69", "70", "71", "72",
				"73", "74", "75", "76", "77", "78", "79", "80", "81", "82",
				"83", "84", "85", "86", "87", "88", "89", "90", "91", "92",
				"93", "94", "95", "96", "97", "98", "99", "100","101","102","103","104","105"  }));
		selectYCombo.setBounds(50, 10, 73, 25);
		agePanel.add(selectYCombo);

		selectMcombo = new JComboBox();
		selectMcombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) selectMcombo.getSelectedItem();
				if(str.equals("Months"))
				{
					p_ageM = "0";
				}
				else {
					p_ageM = str;
				}
			}
		});
		selectMcombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		selectMcombo
		.setModel(new DefaultComboBoxModel(new String[] { "Months",
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10",
				"11", "12" }));
		selectMcombo.setBounds(126, 10, 78, 25);
		agePanel.add(selectMcombo);

		selectDcombo = new JComboBox();
		selectDcombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) selectDcombo.getSelectedItem();
				if(str.equals("Days"))
				{
					p_ageD = "0";
				}
				else {
					p_ageD = str;
				}
			}
		});
		selectDcombo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		selectDcombo.setModel(new DefaultComboBoxModel(new String[] { "Days",
				"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12",
				"13", "14", "15", "16", "17", "18", "19", "20", "21", "22",
				"23", "24", "25", "26", "27", "28", "29", "30", "31" }));
		selectDcombo.setBounds(208, 10, 73, 25);
		agePanel.add(selectDcombo);
		patientIdTB = new JTextField();
		patientIdTB.setEditable(false);
		patientIdTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIdTB.setBounds(106, 16, 197, 25);
		panel_1.add(patientIdTB);
		patientIdTB.setColumns(10);

		patientNameTB = new JTextField();
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setBounds(106, 55, 197, 25);
		panel_1.add(patientNameTB);
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Sex* :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_2.setBounds(16, 189, 46, 14);
		panel_1.add(lblNewLabel_2);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setSelected(true);
		rdbtnMale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_sex = "Male";
			}
		});
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnMale.setBounds(110, 185, 59, 23);
		panel_1.add(rdbtnMale);
		sexgroup.add(rdbtnMale);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_sex = "Female";
			}
		});
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnFemale.setBounds(192, 185, 84, 23);
		panel_1.add(rdbtnFemale);
		sexgroup.add(rdbtnFemale);

		JLabel lblNewLabel_3 = new JLabel("Address :");
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_3.setBounds(16, 222, 74, 14);
		panel_1.add(lblNewLabel_3);

		addressTB = new JTextField();
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setBounds(97, 221, 197, 25);
		panel_1.add(addressTB);
		addressTB.setColumns(10);

		JLabel lblNewLabel_4 = new JLabel("City :");
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_4.setBounds(16, 258, 46, 17);
		panel_1.add(lblNewLabel_4);

		cityTB = new JTextField();
		cityTB.setText("Ambala Cantt");
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setBounds(97, 258, 197, 25);
		panel_1.add(cityTB);
		cityTB.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("State :");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(16, 298, 74, 20);
		panel_1.add(lblNewLabel_5);

		stateTB = new JTextField();
		stateTB.setText("Haryana");
		stateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		stateTB.setBounds(98, 298, 196, 25);
		panel_1.add(stateTB);
		stateTB.setColumns(10);

		JLabel lblNewLabel_6 = new JLabel("Telephone :");
		lblNewLabel_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_6.setBounds(16, 343, 74, 22);
		panel_1.add(lblNewLabel_6);

		telephoneTB = new JTextField();
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setBounds(97, 342, 197, 25);
		panel_1.add(telephoneTB);
		telephoneTB.setColumns(10);
		JLabel lblNewLabel_71 = new JLabel("Hospital Scheme :");
		lblNewLabel_71.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_71.setBounds(16, 379, 74, 22);
		panel_1.add(lblNewLabel_71);

		hospitalSchemeCB = new JComboBox();
		hospitalSchemeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) hospitalSchemeCB.getSelectedItem();
				p_scheme = str;
			}
		});
		//		System.out.println(p_scheme);
		hospitalSchemeCB.setModel(new DefaultComboBoxModel(new String[] { "N/A",
				"Sneh Sparsh", "Karuna"}));
		hospitalSchemeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(hospitalSchemeCB);
		hospitalSchemeCB.setBounds(97, 379, 197, 25);
		panel_1.add(hospitalSchemeCB);
		//		telephoneTB.setColumns(10);
		JLabel lblNewLabel_8 = new JLabel("* indicates required fields");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_8.setBounds(57, 410, 190, 14);
		//		panel_1.add(lblNewLabel_8);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(329, 18, 333, 458);
		panel_7.add(panel_6);
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_6.setLayout(null);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(6, 11, 321, 59);
		panel_6.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Blood Type",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_3.setLayout(null);

		bloodTypeCB = new JComboBox();
		bloodTypeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) bloodTypeCB.getSelectedItem();
				p_bloodtype = str;
			}
		});
		bloodTypeCB.setModel(new DefaultComboBoxModel(new String[] { "Unknown",
				"O+", "A+", "B+", "AB+", "O-", "A-", "B-", "AB-" }));
		bloodTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bloodTypeCB.setBounds(83, 21, 156, 27);
		panel_3.add(bloodTypeCB);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(6, 78, 321, 86);
		panel_6.add(panel_4);
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Guardian",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setLayout(null);

		guardianNameTB = new JTextField();
		guardianNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		guardianNameTB.setBounds(29, 53, 262, 25);
		panel_4.add(guardianNameTB);
		guardianNameTB.setColumns(10);

		JRadioButton rdbtnNewRadioButton_3 = new JRadioButton("Father");
		rdbtnNewRadioButton_3.setSelected(true);
		rdbtnNewRadioButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_guardiantype = "F";
			}
		});
		rdbtnNewRadioButton_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnNewRadioButton_3.setBounds(52, 23, 67, 23);
		panel_4.add(rdbtnNewRadioButton_3);
		guardiangroup.add(rdbtnNewRadioButton_3);

		JRadioButton rdbtnNewRadioButton_4 = new JRadioButton("Husband");
		rdbtnNewRadioButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_guardiantype = "H";
			}
		});
		rdbtnNewRadioButton_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnNewRadioButton_4.setBounds(142, 23, 88, 23);
		panel_4.add(rdbtnNewRadioButton_4);
		guardiangroup.add(rdbtnNewRadioButton_4);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(6, 165, 321, 183);
		panel_6.add(panel_5);
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Has Insurance",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_5.setLayout(null);

		final JRadioButton insuranceYesRB = new JRadioButton("Yes");
		insuranceYesRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				insurancePanel.setVisible(true);
				insuranceTypeCB.setSelectedIndex(0);
				rdbtnCashless.setSelected(true);
				rdbtnCash.setEnabled(true);
				rdbtnCashless.setEnabled(true);
			}
		});
		insuranceYesRB.setBounds(39, 16, 62, 23);
		panel_5.add(insuranceYesRB);
		insuranceYesRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceYesRB);

		JRadioButton insuranceNoRB = new JRadioButton("NO");
		insuranceNoRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_id = "0000" + "" + year + p_code;
				patientIdTB.setText(p_id);
				p_insurancetype = "Unknown";
				insurancePanel.setVisible(false);
				rdbtnCash.setSelected(true);
				rdbtnCash.setEnabled(false);
				rdbtnCashless.setEnabled(false);
			}
		});
		insuranceNoRB.setBounds(208, 16, 57, 23);
		panel_5.add(insuranceNoRB);
		insuranceNoRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceNoRB);

		JRadioButton insuranceUnRB = new JRadioButton("Unknown");
		insuranceUnRB.setSelected(true);
		insuranceUnRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_id = "0000" + "" + year + p_code;
				patientIdTB.setText(p_id);
				p_insurancetype = "Unknown";
				insurancePanel.setVisible(false);
				rdbtnCash.setSelected(true);
				rdbtnCash.setEnabled(false);
				rdbtnCashless.setEnabled(false);
			}
		});
		insuranceUnRB.setBounds(103, 16, 88, 23);
		panel_5.add(insuranceUnRB);
		insuranceUnRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceUnRB);

		insurancePanel = new JPanel();
		insurancePanel.setBounds(10, 36, 290, 103);
		panel_5.add(insurancePanel);
		insurancePanel.setLayout(null);
		insurancePanel.setVisible(false);

		insuranceTypeCB = new JComboBox();
		insuranceTypeCB.setBounds(118, 12, 172, 25);
		insurancePanel.add(insuranceTypeCB);
		insuranceTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					System.out.println("select");
					String str = (String) insuranceTypeCB.getSelectedItem();
					int selected = insuranceTypeCB.getSelectedIndex() + 1;
					System.out.println(selected + "   " + str);
					if (insuranceYesRB.isSelected()) {
						StringBuffer ss = new StringBuffer(selected + "");
						while (ss.length() < 2) {
							ss.insert(0, '0');
						}
						p_insurancetype = str; 
						p_id = "00" + ss + "" + year + p_code;
						patientIdTB.setText(p_id);
					}
					if(str.equals("Unknown")) {
						ins_category=null;
						insuranceCatCB_1.setEnabled(false); 
					}else { 
						if(!ins_category.equals("")) {
							insuranceCatCB_1.setSelectedItem(ins_category);
						}
						insuranceCatCB_1.setEnabled(true);
					}

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		insuranceTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_7 = new JLabel("Insurance Type :");
		lblNewLabel_7.setBounds(0, 16, 104, 17);
		insurancePanel.add(lblNewLabel_7);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblInsuranceNo = new JLabel("Insurance No. :");
		lblInsuranceNo.setBounds(0, 50, 104, 17);
		insurancePanel.add(lblInsuranceNo);
		lblInsuranceNo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceNoTB = new JTextField();
		insuranceNoTB.setBounds(118, 46, 172, 25);
		insurancePanel.add(insuranceNoTB);
		insuranceNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceNoTB.setColumns(10);

		insuranceCatCB_1 = new JComboBox();
		insuranceCatCB_1.setModel(new DefaultComboBoxModel(new String[] {"Please Select", "GENERAL CATEGORY", "PVT CATEGORY", "SEMI PVT CATEGORY"}));
		insuranceCatCB_1.setSelectedIndex(0);
		insuranceCatCB_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		insuranceCatCB_1.setBounds(118, 74, 172, 21);
		insurancePanel.add(insuranceCatCB_1);
		insuranceCatCB_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ins_category=insuranceCatCB_1.getSelectedItem()+"";
			}
		});

		JLabel lblNewLabel_7_1 = new JLabel("Ins Category :");
		lblNewLabel_7_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_7_1.setBounds(10, 75, 94, 18);
		insurancePanel.add(lblNewLabel_7_1);

		rdbtnCash = new JRadioButton("Cash");
		rdbtnCash.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCash.setBounds(129, 147, 62, 23);
		panel_5.add(rdbtnCash);
		Cashgroup.add(rdbtnCash);
		rdbtnCash.setSelected(true);
		rdbtnCash.setEnabled(false);

		rdbtnCashless = new JRadioButton("Cashless");
		rdbtnCashless.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCashless.setBounds(216, 147, 97, 23);
		panel_5.add(rdbtnCashless);
		Cashgroup.add(rdbtnCashless);
		rdbtnCashless.setEnabled(false);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Note",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_8.setBounds(6, 360, 319, 86);
		panel_6.add(panel_8);
		panel_8.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 22, 299, 64);
		panel_8.add(scrollPane);

		noteTA = new JTextArea();
		noteTA.setRows(10);
		noteTA.setLineWrap(true);
		scrollPane.setViewportView(noteTA);
		noteTA.setDropMode(DropMode.INSERT);
		noteTA.setColumns(20);
		noteTA.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton saveBT = new JButton("Save");
		saveBT.setIcon(new ImageIcon(NewPatient.class
				.getResource("/icons/ok_button.png")));
		saveBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String[] data=checkAlreadyAadhaar(new CryptoHandler().encrypt(new String(aadhaarTF.getPassword())));
					if(data!=null) {
						JOptionPane.showMessageDialog(null, "You have already registered with '"+data[0]+"' ('"+data[1]+"')", "Information", JOptionPane.INFORMATION_MESSAGE);
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				p_id = patientIdTB.getText();
				p_name = patientNameTB.getText();
				p_address = addressTB.getText();
				p_city = cityTB.getText();
				p_telephone = telephoneTB.getText();
				ResultSet resultSet = patientDBConnection
						.searchPhoneno(p_telephone);
				boolean exists = false;
				try {
					while( resultSet.next() ) {
						// ResultSet processing here
						exists = true;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				if(exists) {
					JOptionPane.showMessageDialog(null,
							"This Number has  Registration Problem. You Cannot Create New Patient With this Number ", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					exists=true;
					//					p_telephone="";
					//					 telephoneTB.setText("");
					return;
				}
				//				if(otpverified==0){
				//					JOptionPane.showMessageDialog(null,
				//							"This Number is Not Verified.Please Verify", "Input Error",
				//							JOptionPane.ERROR_MESSAGE);
				//					return;
				//				}
				p_p_father_husband = guardianNameTB.getText();
				p_note = noteTA.getText();
				p_insurance_no=insuranceNoTB.getText().toString();
				if (p_insurancetype.equals("Unknown")||p_insurancetype.equals("No")) {
					p_insurance_no="";
					ins_category=null;
				}
				if (p_id.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please insert the Patient ID", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (p_name.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please insert the Patient name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(p_agecode.equals("birth")){
					try {
						calculateAge(p_birthdate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				else if (p_agecode.equals("age")) {
					p_age = p_ageY + "-" + p_ageM + "-" + p_ageD;
					if (p_ageY.equals("0") && p_ageM.equals("0")
							&& p_ageD.equals("0")) {
						JOptionPane.showMessageDialog(null,
								"Please insert Patient Age", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					int year=Integer.parseInt(p_ageY);
					int month=Integer.parseInt(p_ageM);
					int day=Integer.parseInt(p_ageD);
					p_birthdate=findDateOfBirth(year,month,day);

				}
				else if (!p_agecode.equals("age")&&p_birthdate.equals("1111-11-11")) {

					JOptionPane.showMessageDialog(null,
							"Please insert Date of Birth", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (p_city.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please insert the City", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (p_p_father_husband.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please insert the Guardian Name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (!p_insurancetype.equals("Unknown")&&p_insurance_no.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please insert insurance no.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if (!p_insurancetype.equals("Unknown") && insuranceCatCB_1.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null,
							"Please select insurance category", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if (!aadhaarTF.getText().equals("") && !(aadhaarTF.getText().length() == 12)) {
					JOptionPane.showMessageDialog(null,
							"Aadhaar number must be exactly 12 digits.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else {
					String aadhaarInput = new String(aadhaarTF.getPassword());
					String aadhaarConfirm = new String(aadhaarConfimTF.getPassword());
					if (!aadhaarConfirm.equals(aadhaarInput)) {
						JOptionPane.showMessageDialog(null, "Confirmation Incorrect of Aadhaar No. Please try again.");
						return;
					}

					data[0] = p_id;
					data[1] = p_id;
					data[2] = WordUtils.capitalize(p_name);
					data[3] = p_agecode;
					data[4] = p_age;
					data[5] = p_birthdate;
					data[6] = p_sex;
					data[7] = WordUtils.capitalize(p_address);
					data[8] = WordUtils.capitalize(p_city);
					data[9] = p_telephone;
					data[10] = p_bloodtype;
					data[11] = p_guardiantype;
					data[12] = WordUtils.capitalize(p_p_father_husband);
					data[13] = p_insurancetype;
					data[14] = p_note;
					data[15] = p_insurance_no;
					data[16]=p_scheme;


					long timeInMillis1 = System.currentTimeMillis();
					Calendar cal_ = Calendar.getInstance();
					cal_.setTimeInMillis(timeInMillis1);
					SimpleDateFormat timeFormat1 = new SimpleDateFormat("hh:mm:ss");
					//					System.out.println(dateFormat.format(cal));
					data[17]=""+DateFormatChange.StringToMysqlDate(new Date())+" "+timeFormat1.format(cal_.getTime());
					data[18]=""+ReceptionMain.receptionNameSTR;
					data[19]=mobile_verified;
					data[20]=ins_category;
					data[21]=rdbtnCashless.isSelected()?"1":"0";
					try {
						if(!aadhaarTF.getText().equals(""))
							data[22]=new CryptoHandler().encrypt(new String(aadhaarTF.getPassword()));
						else
							data[22]=null;
						int patient_code=patientDBConnection.inserData(data);
						int autoINC=patient_code;
						if(patient_code>=9999999)
						{
							patient_code=patient_code%9999999;
						}
						StringBuffer ss = new StringBuffer(patient_code + "");
						while (ss.length() < 7) {
							ss.insert(0, '0');
						}
						String subSTR=p_id.substring(0, 6);

						String final_p_id=subSTR+ss.toString();
						patientDBConnection.updateDataPatientID(autoINC+"", final_p_id);
						patientDBConnection.closeConnection();
						if(setImage)
						{
							String serverPath=makeDirectory(final_p_id);
							File sourcePath = new File(imagePathSTR); 
							copyFileUsingJava7Files(sourcePath.toString(), serverPath);
						}
						dispose();
						//						JOptionPane.showMessageDialog(null,
						//								"Data Saved Successfully ", "Data Save",
						//								JOptionPane.INFORMATION_MESSAGE);
						new IDCardSlip(final_p_id);
						if(opdEntery!=null)
						{
							opdEntery.searchPatientTB.setText(final_p_id);
						}
						else if(examEntery!=null){
							examEntery.searchPatientTB.setText(final_p_id);
						}
						else if(ipdEntery!=null){
							ipdEntery.searchPatientTB.setText(final_p_id);
						}else {
							patientBrowser.searchTB.setText(final_p_id);
						}
						long timeInMillis = System.currentTimeMillis();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTimeInMillis(timeInMillis);
						SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
						String[] data1=new String[10];
						data1[0]="CARD";
						data1[1]="10";
						data1[2]=""+ReceptionMain.receptionNameSTR;
						data1[3]=""+final_p_id;
						data1[4]=WordUtils.capitalize(p_name);
						data1[5]=""+ DateFormatChange.StringToMysqlDate(new Date());
						data1[6]=""+timeFormat.format(cal1.getTime());
						data1[7]=""+p_insurancetype;
						data1[8]= rdbtnCashless.isSelected()?"1":"0";
						AmountReceiptDBConnection amountReceiptDBConnection=new AmountReceiptDBConnection();	
						amountReceiptDBConnection.inserData(data1);
						amountReceiptDBConnection.closeConnection();


					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		saveBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		saveBT.setBounds(10, 466, 151, 34);
		panel_7.add(saveBT);

		JButton cancelBT = new JButton("Cancel");
		cancelBT.setIcon(new ImageIcon(NewPatient.class
				.getResource("/icons/close_button.png")));
		cancelBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBT.setBounds(171, 466, 151, 34);
		panel_7.add(cancelBT);

		p_id = "0000" + "" + year + p_code;
		patientIdTB.setText(p_id);

		final JButton btnVerify = new JButton("Verify");
		btnVerify.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				URL url;

				try {
					// get URL content

					//				    String a="https://bulksmsapi.vispl.in/?username=Advancetrn&password=Advance@123&messageType=text&mobile=7206490601&senderId=RACAGH&message=This%20is%20a%20test%20message";
					//   String a="http://maps.google.at/maps?saddr=4714&daddr=Marchtrenk&hl=de";
					//					  String urlParameters = "u=8708883806";
					//				      String request = "http://localhost/uhc/post.php?";
					Random rand = new Random();
					int id =  rand.nextInt(1000000);
					String mobile=telephoneTB.getText().toString();
					String u="http://192.168.1.77/sms_api/sms.php?action=otp&mobile="+mobile+"&otp="+id;
					url = new URL(u);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setRequestMethod("GET");
					conn.setRequestProperty("Content-Type", "text/plain");
					conn.setRequestProperty("charset", "utf-8");
					// open the stream and put it into BufferedReader
					BufferedReader br = new BufferedReader(
							new InputStreamReader(conn.getInputStream()));

					String inputLine;
					while ((inputLine = br.readLine()) != null) {
						System.out.println(inputLine);
					}
					//				    br.close();
					int rc = conn.getResponseCode();
					if(rc==200){
						//				    	int dialogButton = JOptionPane.YES_NO_OPTION;
						//						int dialogResult = JOptionPane.showConfirmDialog(NewPatient.this,
						//								"Enter OTP ." + "\n"
						//										+ " Current MRP is " +  model.getValueAt(i, 13).toString() + " Rupees of item "+data[1], "Cancel",
						//								dialogButton);
						//						if (dialogResult == 0) {
						String m = JOptionPane.showInputDialog("OTP ",  "");
						// System.out.println(m);
						System.out.println(m+"generrate"+id);
						if ((m != null) && (m.length() > 0)) {
							//								mrpUpdate1(Double.parseDouble(m),data[0]);
							//								mrpPriceTF.setTexst(Double.parseDouble(m)+"");
							int verifyn=Integer.parseInt(m);
							System.out.println(verifyn+"generrate"+id);
							if(MainLogin.userName1.equals("admin")){
								id=1234;
								mobile_verified="Manual Verified";
							}
							if(verifyn==id ){
								//									JOptionPane.showMessageDialog(null,
								//											"Verified OTP", "Verified OTP",
								//											JOptionPane.ERROR_MESSAGE);
								JOptionPane.showConfirmDialog(null,
										"Mobile No Verified", "Mobile No Verified", JOptionPane.DEFAULT_OPTION);
								String mobile1=telephoneTB.getText().toString();
								//									String msg="Mobile No. Verified";
								//									String encoded_message=URLEncoder.encode(msg);
								//									String u1="http://192.168.1.77/sms_api/sms.php?action=msg&mobile="+mobile1+"&message="+encoded_message;
								String u1="http://192.168.1.77/sms_api/sms.php?action=msg&mobile="+mobile1;
								URL urlN = new URL(u1);
								HttpURLConnection connO = (HttpURLConnection) urlN.openConnection();
								connO.setRequestMethod("GET");
								connO.setRequestProperty("Content-Type", "text/plain");
								connO.setRequestProperty("charset", "utf-8");
								// open the stream and put it into BufferedReader
								BufferedReader br1 = new BufferedReader(
										new InputStreamReader(connO.getInputStream()));

								String inputLine1;
								while ((inputLine1 = br1.readLine()) != null) {
									System.out.println(inputLine1);
								}
								br1.close();
								int rc1 = connO.getResponseCode();
								otpverified=1;
								btnVerify.setText("Verified");
								btnVerify.setEnabled(false);
							}	else {
								JOptionPane.showMessageDialog(null,
										"NOT Verified OTP.Please Verify Again", " NOT Verified OTP",
										JOptionPane.ERROR_MESSAGE);
								boolean var=checkotp(id,mobile);
								if(var){
									btnVerify.setText("Verified");
									btnVerify.setEnabled(false);
								}

							}

						}else{
							JOptionPane.showMessageDialog(null,
									"Enter OTP", " Enter OTP",
									JOptionPane.ERROR_MESSAGE);
						}

					}else{
						JOptionPane.showMessageDialog(null,
								"Verify Again", "Verify Again",
								JOptionPane.ERROR_MESSAGE);
					}

					System.out.print("Response Code = "+rc+"\n");

					System.out.println("Done");

				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

			}

		}
				);
		btnVerify.setFont(new Font("Tahoma", Font.PLAIN, 14));
		//		btnVerify.setBounds(220, 334, 93, 34);
		//		panel_1.add(btnVerify);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.setBounds(672, 22, 160, 187);
		panel_7.add(panel_9);
		panel_9.setLayout(null);

		patientImageTV = new JLabel("");
		patientImageTV.setBounds(10, 11, 140, 165);
		panel_9.add(patientImageTV);

		takePicBT = new JButton("Take Pic");
		takePicBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				WebCapure capure=new WebCapure();
				capure.newPatient(NewPatient.this);
				capure.setVisible(true);


			}
		});
		takePicBT.setFont(new Font("Tahoma", Font.BOLD, 14));
		takePicBT.setBounds(710, 219, 102, 55);
		panel_7.add(takePicBT);

		clearPicBT = new JButton("Clear Pic");
		clearPicBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				patientImageTV.setIcon(null) ;
				setImage=false;
			}
		});
		clearPicBT.setFont(new Font("Tahoma", Font.BOLD, 14));
		clearPicBT.setBounds(710, 356, 102, 55);
		panel_7.add(clearPicBT);

		JButton btnBrowsePic = new JButton("Browse");
		btnBrowsePic.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				browseButtonActionPerformed();
			}
		});
		btnBrowsePic.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBrowsePic.setBounds(710, 285, 102, 55);
		panel_7.add(btnBrowsePic);

		aadhaarTF = new JPasswordField();
		aadhaarTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		aadhaarTF.setColumns(10);
		aadhaarTF.setBounds(462, 481, 206, 25);
		panel_7.add(aadhaarTF);

		aadhaarTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();  // Ignore non-numeric input
				}
				if (aadhaarTF.getText().length() >= 12) {
					e.consume();  // Ignore input if 12 digits are already entered
				}
			}
		});

		JLabel lblNewLabel_7_1_1 = new JLabel("Aadhaar Number :");
		lblNewLabel_7_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_7_1_1.setBounds(339, 478, 114, 28);
		panel_7.add(lblNewLabel_7_1_1);

		aadhaarConfimTF = new JPasswordField();
		aadhaarConfimTF.setText("");
		aadhaarConfimTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		aadhaarConfimTF.setColumns(10);
		aadhaarConfimTF.setBounds(462, 509, 206, 25);
		panel_7.add(aadhaarConfimTF);

		JLabel lblConfirm = new JLabel("Confirm :");
		lblConfirm.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblConfirm.setBounds(364, 513, 104, 17);
		panel_7.add(lblConfirm);

		JButton btnCheckAadhaar = new JButton("Check Aadhaar");
		btnCheckAadhaar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(new String(aadhaarTF.getPassword()));
				try {
					System.out.println(new CryptoHandler().encrypt(new String(aadhaarTF.getPassword())));

					String[] data=checkAlreadyAadhaar(new CryptoHandler().encrypt(new String(aadhaarTF.getPassword())));
					if(data!=null) {
						JOptionPane.showMessageDialog(null, "You have already registered with '"+data[0]+"' ('"+data[1]+"')", "Information", JOptionPane.INFORMATION_MESSAGE);
						return;
					}else {
						JOptionPane.showMessageDialog(null, "Aadhar No. Not Found", "Error", JOptionPane.ERROR_MESSAGE);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnCheckAadhaar.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnCheckAadhaar.setBounds(680, 483, 151, 20);
		panel_7.add(btnCheckAadhaar);
		if(rdbtnBirthdate.isSelected()) {
			birthDatePanel.setVisible(true);
			agePanel.setVisible(false);
			p_agecode = "birth";
		}
		getAllinsurance();

	}
	public void OPDObject(OPDEntery opdEntery)
	{
		this.opdEntery=opdEntery;
	}
	public void ExamEntryObject(ExamEntery examEntery)
	{
		this.examEntery=examEntery;
	}
	public void PatientBrowserObject(PatientBrowser patientBrowser)
	{
		this.patientBrowser=patientBrowser;
	}

	public void IPDEnteryObject(IPDEntery ipdEntery)
	{
		this.ipdEntery=ipdEntery;
	}
	public void getAllinsurance() {
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceModel.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		insuranceTypeCB.setModel(insuranceModel);
		insuranceTypeCB.setSelectedIndex(0);
	}
	public void setImageIcon(String imagepath)
	{

		imagePathSTR=imagepath;
		ImageIcon imageIcon = new ImageIcon(imagepath); // load the image to a imageIcon
		Image image = imageIcon.getImage(); // transform it 
		Image newimg = image.getScaledInstance(patientImageTV.getWidth(), patientImageTV.getHeight(),  Image.SCALE_DEFAULT); // scale it the smooth way  
		imageIcon = new ImageIcon(newimg);  //
		setImage=true;

		patientImageTV.setIcon(imageIcon);
	}
	public void calculateAge(String dateOfBirthStr) throws ParseException {
		// Parse the date of birth string
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar dob = Calendar.getInstance();
		dob.setTime(sdf.parse(dateOfBirthStr));

		// Get the current date
		Calendar now = Calendar.getInstance();

		// Calculate the difference between dates
		int years = now.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
		int months = now.get(Calendar.MONTH) - dob.get(Calendar.MONTH);
		int days = now.get(Calendar.DAY_OF_MONTH) - dob.get(Calendar.DAY_OF_MONTH);

		// Adjust for negative months or days
		if (months < 0 || (months == 0 && days < 0)) {
			years--;
			if (months < 0) {
				months += 12;
			}
			if (days < 0) {
				// Get the number of days in the previous month
				Calendar temp = (Calendar) now.clone();
				temp.add(Calendar.MONTH, -1);
				int daysInPreviousMonth = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
				days += daysInPreviousMonth;
				months--;
			}
		}

		// Print the age
		p_age= years + "-" + months + "-" + days;
	}


	public String makeDirectory(String pid) {
		String RESULT;
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient_Images");
			if (!dir.exists())
				dir.mkdirs();


		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		RESULT = mainDir + "/HMS/Patient_Images/" + pid + ".jpg";

		return RESULT;
	}
	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(dest);
		OutputStream os = remoteFile.getOutputStream();
		InputStream is = new FileInputStream(new File(source)) ;

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}

	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	private void browseButtonActionPerformed() {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new ImageFileFilter());
		int res = fc.showOpenDialog(null);
		// We have an image!
		try {
			if (res == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				setImageIcon(file.toString());
			} // Oops!
			else {
				JOptionPane.showMessageDialog(null,
						"You must select one image to be the reference.", "Aborting...",
						JOptionPane.WARNING_MESSAGE);
			}
		} catch (Exception iOException) {
		}
	}
	public static String findDateOfBirth(int years, int months, int days) {
		// Get the current date
		Calendar currentDate = Calendar.getInstance();

		// Subtract years, months, and days from the current date
		currentDate.add(Calendar.YEAR, -years);
		currentDate.add(Calendar.MONTH, -months);
		currentDate.add(Calendar.DAY_OF_MONTH, -days);

		// Format the date of birth as a string
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String formattedDOB = dateFormat.format(currentDate.getTime());

		return formattedDOB;
	}

	private boolean checkotp(int id,String mobile ){
		URL url;
		String n = JOptionPane.showInputDialog("Enter OTP Again ",  "");
		int verifyn1=Integer.parseInt(n);
		if(verifyn1==id){
			//				JOptionPane.showMessageDialog(null,
			//						"Verified OTP", "Verified OTP",
			//						JOptionPane.ERROR_MESSAGE);
			JOptionPane.showConfirmDialog(null,
					"Mobile No Verified", "Mobile No Verified", JOptionPane.DEFAULT_OPTION);


			URL url1;

			try {
				// get URL content

				//				    String a="https://bulksmsapi.vispl.in/?username=Advancetrn&password=Advance@123&messageType=text&mobile=7206490601&senderId=RACAGH&message=This%20is%20a%20test%20message";
				//   String a="http://maps.google.at/maps?saddr=4714&daddr=Marchtrenk&hl=de";
				//					  String urlParameters = "u=8708883806";
				//				      String request = "http://localhost/uhc/post.php?";
				Random rand = new Random();
				//					int id1 =  rand.nextInt(10000);

				//					String msg="Mobile No. Verified.You can download this https://play.google.com/store/apps/details?id=com.rotary.main for details";
				String msg="Mobile No. Verified.";
				String encoded_message=URLEncoder.encode(msg);
				//					String u1="http://192.168.1.77/sms_api/sms.php?action=msg&mobile="+mobile+"&message="+encoded_message;
				String u1="http://192.168.1.77/sms_api/sms.php?action=msg&mobile="+mobile;
				url1 = new URL(u1);
				HttpURLConnection conn = (HttpURLConnection) url1.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Content-Type", "text/plain");
				conn.setRequestProperty("charset", "utf-8");
				// open the stream and put it into BufferedReader
				BufferedReader br = new BufferedReader(
						new InputStreamReader(conn.getInputStream()));

				String inputLine;
				while ((inputLine = br.readLine()) != null) {
					System.out.println(inputLine);
				}
				br.close();
				int rc = conn.getResponseCode();

				System.out.print("Response Code = "+rc+"\n");

				System.out.println("Done");

			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			otpverified=1;

			//				btnVerify.setEnabled(false);
		}	else{

			JOptionPane.showMessageDialog(null,
					"NOT Verified OTP.Please Verify Again", " NOT Verified OTP",
					JOptionPane.ERROR_MESSAGE);
			checkotp(id,mobile);

		}
		return true;
	}

	public String[] checkAlreadyAadhaar(String index) {
		PatientDBConnection DB = new PatientDBConnection();
		ResultSet rs = DB.retrieveAadhaarData(index);
		try {
			while(rs.next()) {
				return new String[]{rs.getString(1),rs.getString(2)};
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DB.closeConnection();
		}
		return null;
	}
}
