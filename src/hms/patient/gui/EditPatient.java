package hms.patient.gui;

import hms.crypto.CryptoHandler;
import hms.exams.gui.ExamEntery;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.gui.OPDEntery;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.OPDSlippdfESI;

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
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
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

public class EditPatient extends JDialog {

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
	ButtonGroup cashlessgroup = new ButtonGroup();
	ButtonGroup insurancegroup = new ButtonGroup();
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField stateTB;
	private JTextField telephoneTB;
	private JComboBox selectYCombo;
	private JTextField guardianNameTB;
	private JComboBox insuranceTypeCB;
	public String patientNumber;
	JComboBox bloodTypeCB;
	String[] data = new String[20];
	String p_id, p_code, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",p_status="",
			p_insurance_no = "",ins_category="";
	DateFormatChange dateFormat = new DateFormatChange();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	private JComboBox selectMcombo;
	private JComboBox selectDcombo;
	private JTextArea noteTA;
	PatientDBConnection patientDBConnection;
	String year;
	private JTextField insuranceNoTB;
	private JPanel insurancePanel;
	OPDEntery opdEntery = null;
	ExamEntery examEntery = null;
	PatientBrowser patientBrowser = null;
	private JRadioButton rdbtnAge;
	private JRadioButton rdbtnBirthdate;
	private JDateChooser birthDateDC;
	private JRadioButton rdbtnFemale;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFather;
	private JRadioButton rdbtnHusband;
	private JRadioButton insuranceNoRB;
	private JRadioButton insuranceUnRB;
	private JRadioButton insuranceYesRB;
	private JLabel patientImageTV;
	private JButton takePicBT;
	private JButton clearBT;
	public boolean setImage = false;
	String mainDir = "";
	static String OS;
	String[] open = new String[4];
	String imagePathSTR="";
	private JButton btnBrowse;
	private JComboBox p_statusCB;
	private JComboBox insuranceCatCB_1;
	private boolean isCashless;
	private JRadioButton rdbtnCash;
	private JRadioButton rdbtnCashless;
	private JPasswordField aadhaarTF;
	private JLabel lblAadhaarNo;
	private JPasswordField aadhaarConfirmTF;


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
		new EditPatient("0000130000093").setVisible(true);;
	}

	/**
	 * Create the frame.
	 */
	public EditPatient(String patient_id) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EditPatient.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setModal(true);
		readFile();
		setTitle("Patient Entery Form");
		setBounds(200, 100, 865, 582);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		patientDBConnection = new PatientDBConnection();

		p_code = patient_id.substring(Math.max(0, patient_id.length() - 9));
		year = Calendar.getInstance().get(Calendar.YEAR) + "";
		year = year.substring(year.length() - 2);
		patientNumber = "Patient Entery Form ( Code : " + patient_id + " )";
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), patientNumber,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_7.setBounds(10, 0, 668, 533);
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
		rdbtnAge = new JRadioButton("Age");
		rdbtnAge.setSelected(true);
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

		rdbtnBirthdate = new JRadioButton("Birthdate");
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

		birthDateDC = new JDateChooser();
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
				if (str.equals("Years")) {
					p_ageY = "0";
				} else {
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
				"93", "94", "95", "96", "97", "98", "99", "100","101","102","103","104","105" }));
		selectYCombo.setBounds(50, 10, 73, 25);
		agePanel.add(selectYCombo);

		selectMcombo = new JComboBox();
		selectMcombo.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) selectMcombo.getSelectedItem();
				if (str.equals("Months")) {
					p_ageM = "0";
				} else {
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
				if (str.equals("Days")) {
					p_ageD = "0";
				} else {
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

		rdbtnMale = new JRadioButton("Male");
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

		rdbtnFemale = new JRadioButton("Female");
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
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setBounds(97, 258, 197, 25);
		panel_1.add(cityTB);
		cityTB.setColumns(10);

		JLabel lblNewLabel_5 = new JLabel("State :");
		lblNewLabel_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_5.setBounds(16, 298, 74, 20);
		panel_1.add(lblNewLabel_5);

		stateTB = new JTextField();
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

		JLabel lblNewLabel_8 = new JLabel("* indicates required fields");
		lblNewLabel_8.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_8.setBounds(57, 393, 190, 14);
		panel_1.add(lblNewLabel_8);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(329, 18, 333, 418);
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
		panel_4.setBounds(6, 68, 321, 100);
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

		rdbtnFather = new JRadioButton("Father");
		rdbtnFather.setSelected(true);
		rdbtnFather.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_guardiantype = "F";
			}
		});
		rdbtnFather.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnFather.setBounds(52, 23, 67, 23);
		panel_4.add(rdbtnFather);
		guardiangroup.add(rdbtnFather);

		rdbtnHusband = new JRadioButton("Husband");
		rdbtnHusband.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_guardiantype = "H";
			}
		});
		rdbtnHusband.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnHusband.setBounds(142, 23, 88, 23);
		panel_4.add(rdbtnHusband);
		guardiangroup.add(rdbtnHusband);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(6, 167, 321, 139);
		panel_6.add(panel_5);
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Has Insurance",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_5.setLayout(null);

		insuranceYesRB = new JRadioButton("Yes");
		insuranceYesRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				insurancePanel.setVisible(true);
				insuranceTypeCB.setSelectedIndex(0);
			}
		});
		insuranceYesRB.setBounds(39, 16, 62, 23);
		panel_5.add(insuranceYesRB);
		insuranceYesRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceYesRB);

		insuranceNoRB = new JRadioButton("NO");
		insuranceNoRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_id = "0000" + "" + p_code;
				patientIdTB.setText(p_id);
				p_insurancetype = "Unknown";
				insurancePanel.setVisible(false);
			}
		});
		insuranceNoRB.setBounds(208, 16, 57, 23);
		panel_5.add(insuranceNoRB);
		insuranceNoRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceNoRB);

		insuranceUnRB = new JRadioButton("Unknown");
		insuranceUnRB.setSelected(true);
		insuranceUnRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				p_id = "0000" + "" + p_code;
				patientIdTB.setText(p_id);
				p_insurancetype = "Unknown";
				insurancePanel.setVisible(false);
			}
		});

		insurancePanel = new JPanel();
		insurancePanel.setBounds(12, 34, 309, 105);
		panel_5.add(insurancePanel);
		insurancePanel.setLayout(null);
		insurancePanel.setVisible(false);

		insuranceTypeCB = new JComboBox();
		insuranceTypeCB.setBounds(118, 15, 172, 21);
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
						p_id = "00" + ss + "" + p_code;
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
		lblNewLabel_7.setBounds(12, 15, 106, 17);
		insurancePanel.add(lblNewLabel_7);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblInsuranceNo = new JLabel("Insurance No. :");
		lblInsuranceNo.setBounds(12, 39, 108, 30);
		insurancePanel.add(lblInsuranceNo);
		lblInsuranceNo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceNoTB = new JTextField();
		insuranceNoTB.setBounds(118, 48, 172, 21);
		insurancePanel.add(insuranceNoTB);
		insuranceNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceNoTB.setColumns(10);

		insuranceCatCB_1 = new JComboBox();
		insuranceCatCB_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ins_category=insuranceCatCB_1.getSelectedItem()+"";
			}
		});
		insuranceCatCB_1.setModel(new DefaultComboBoxModel(new String[] {"Please Select", "GENERAL CATEGORY", "PVT CATEGORY", "SEMI PVT CATEGORY"}));
		insuranceCatCB_1.setSelectedIndex(0);
		insuranceCatCB_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		insuranceCatCB_1.setBounds(118, 78, 172, 21);
		insurancePanel.add(insuranceCatCB_1);


		JLabel lblNewLabel_7_1 = new JLabel("Ins Category :");
		lblNewLabel_7_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_7_1.setBounds(12, 81, 94, 18);
		insurancePanel.add(lblNewLabel_7_1);
		insuranceUnRB.setBounds(103, 16, 88, 23);
		panel_5.add(insuranceUnRB);
		insuranceUnRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insurancegroup.add(insuranceUnRB);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Note",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_8.setBounds(6, 338, 319, 69);
		panel_6.add(panel_8);
		panel_8.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 299, 51);
		panel_8.add(scrollPane);

		noteTA = new JTextArea();
		noteTA.setRows(10);
		noteTA.setLineWrap(true);
		scrollPane.setViewportView(noteTA);
		noteTA.setDropMode(DropMode.INSERT);
		noteTA.setColumns(20);
		noteTA.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSetStatus = new JLabel("Set Status :");
		lblSetStatus.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSetStatus.setBounds(16, 318, 104, 17);
		panel_6.add(lblSetStatus);

		p_statusCB = new JComboBox();
		p_statusCB.setModel(new DefaultComboBoxModel(new String[] {"Active", "Inactive", "DND", "Half DND"}));
		p_statusCB.setSelectedIndex(0);
		p_statusCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		p_statusCB.setBounds(134, 318, 172, 21);
		panel_6.add(p_statusCB);

		JButton saveBT = new JButton("Save");
		saveBT.setIcon(new ImageIcon(EditPatient.class
				.getResource("/icons/ok_button.png")));
		saveBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {				

				p_id = patientIdTB.getText();
				p_name = patientNameTB.getText();
				p_address = addressTB.getText();
				p_city = cityTB.getText();
				p_telephone = telephoneTB.getText();
				p_p_father_husband = guardianNameTB.getText();
				p_note = noteTA.getText();
				p_insurance_no = insuranceNoTB.getText().toString();
				p_status=p_statusCB.getSelectedItem().toString();
				if (p_insurancetype.equals("Unknown")
						|| p_insurancetype.equals("No")) {
					p_insurance_no = "";
					ins_category=null;
				}
				if(!p_agecode.equals("birth")) {
					JOptionPane.showMessageDialog(null,
							"Please insert BirthDate of the Patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
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
				else if(p_agecode.equals("birth")) {

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

				} else if (!p_agecode.equals("age")
						&& p_birthdate.equals("1111-11-11")) {

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
				} else if (!p_insurancetype.equals("Unknown")
						&& p_insurance_no.equals("")) {
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
				} else {
					String aadhaarInput = new String(aadhaarTF.getPassword());
					String aadhaarConfirm = new String(aadhaarConfirmTF.getPassword());
					if (!aadhaarConfirm.equals(aadhaarInput)) {
						JOptionPane.showMessageDialog(null, "Confirmation Incorrect of Aadhaar No. Please try again.");
						return;
					}

					if(setImage)
					{
						String serverPath=makeDirectory(p_id);
						File sourcePath = new File(imagePathSTR); 
						try {
							copyFileUsingJava7Files(sourcePath.toString(), serverPath);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					data[0] = WordUtils.capitalize(p_name);
					data[1] = p_agecode;
					data[2] = p_age;
					data[3] = p_birthdate;
					data[4] = p_sex;
					data[5] = WordUtils.capitalize(p_address);
					data[6] = WordUtils.capitalize(p_city);
					data[7] = p_telephone;
					data[8] = p_bloodtype;
					data[9] = p_guardiantype;
					data[10] = WordUtils.capitalize(p_p_father_husband);
					data[11] = p_insurancetype;
					data[12] = p_note;
					data[13] = p_insurance_no;
					data[14] = p_status;
					data[15] = ins_category;
					data[16] = rdbtnCash.isSelected()?"0":"1";
					try {
						if(!aadhaarTF.getText().equals(""))
							data[17]=new CryptoHandler().encrypt(new String(aadhaarTF.getPassword()));
						else
							data[17]=null;
						data[18] = p_id;					
						patientDBConnection.updateData(data);
						patientDBConnection.closeConnection();
						JOptionPane.showMessageDialog(null,
								"Data Updated Successfully ", "Data Update",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
						if (opdEntery != null) {
							opdEntery.searchPatientTB.setText(p_id);
						} else if (examEntery != null) {
							examEntery.searchPatientTB.setText(p_id);
						} 
					} catch (Exception e) {
						// TODO Auto-generated catch block
						// Display the exception details in a JOptionPane
						JOptionPane.showMessageDialog(null, e.getMessage(), "Exception Occurred", JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				}
			}
		});
		saveBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		saveBT.setBounds(26, 464, 151, 34);
		panel_7.add(saveBT);

		JButton cancelBT = new JButton("Cancel");
		cancelBT.setIcon(new ImageIcon(EditPatient.class
				.getResource("/icons/close_button.png")));
		cancelBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelBT.setFont(new Font("Tahoma", Font.PLAIN, 14));
		cancelBT.setBounds(187, 464, 151, 34);
		panel_7.add(cancelBT);

		p_id = "0000" + "" + p_code;
		patientIdTB.setText(p_id);
		getAllinsurance();

		patientIdTB.setText(patient_id);

		rdbtnCash = new JRadioButton("Cash");
		rdbtnCash.setSelected(true);
		rdbtnCash.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCash.setBounds(440, 444, 67, 23);
		panel_7.add(rdbtnCash);
		cashlessgroup.add(rdbtnCash);

		rdbtnCashless = new JRadioButton("Cashless");
		rdbtnCashless.setSelected(false);
		rdbtnCashless.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnCashless.setBounds(530, 444, 88, 23);
		panel_7.add(rdbtnCashless);
		cashlessgroup.add(rdbtnCashless);

		aadhaarTF = new JPasswordField();
		aadhaarTF.setText("");
		aadhaarTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		aadhaarTF.setColumns(10);
		aadhaarTF.setBounds(450, 472, 210, 25);
		panel_7.add(aadhaarTF);

		lblAadhaarNo = new JLabel("Aadhaar No. :");
		lblAadhaarNo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAadhaarNo.setBounds(356, 476, 104, 17);
		panel_7.add(lblAadhaarNo);

		aadhaarConfirmTF = new JPasswordField();
		aadhaarConfirmTF.setText("");
		aadhaarConfirmTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		aadhaarConfirmTF.setColumns(10);
		aadhaarConfirmTF.setBounds(450, 501, 210, 25);
		panel_7.add(aadhaarConfirmTF);

		JLabel lblConfirm = new JLabel("Confirm :");
		lblConfirm.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblConfirm.setBounds(356, 505, 104, 17);
		panel_7.add(lblConfirm);

		aadhaarTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char c = e.getKeyChar();
				if (!Character.isDigit(c)) {
					e.consume();  
				}
				if (aadhaarTF.getText().length() >= 12) {
					e.consume();  
				}
			}
		});

		takePicBT = new JButton("Take Pic");
		takePicBT.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				WebCapure capure = new WebCapure();
				capure.newPatient(EditPatient.this);
				capure.setVisible(true);

			}
		});
		takePicBT.setFont(new Font("Tahoma", Font.BOLD, 14));
		takePicBT.setBounds(712, 210, 102, 55);
		contentPane.add(takePicBT);

		clearBT = new JButton("Clear Pic");
		clearBT.setFont(new Font("Tahoma", Font.BOLD, 14));
		clearBT.setBounds(712, 341, 102, 55);
		contentPane.add(clearBT);

		JPanel panel_9 = new JPanel();
		panel_9.setLayout(null);
		panel_9.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_9.setBounds(688, 11, 160, 187);
		contentPane.add(panel_9);

		patientImageTV = new JLabel("");
		patientImageTV.setBounds(10, 11, 140, 165);
		panel_9.add(patientImageTV);

		btnBrowse = new JButton("Browse");
		btnBrowse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				browseButtonActionPerformed();
			}
		});
		btnBrowse.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnBrowse.setBounds(712, 276, 102, 55);
		contentPane.add(btnBrowse);

		setPatientDetail(patient_id);
	}

	public void OPDObject(OPDEntery opdEntery) {
		this.opdEntery = opdEntery;
	}

	public void ExamEntryObject(ExamEntery examEntery) {
		this.examEntery = examEntery;
	}

	public void PatientBrowserObject(PatientBrowser patientBrowser) {
		this.patientBrowser = patientBrowser;
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

	public void setPatientDetail(String indexId) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex2(indexId);
		try {
			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_agecode = resultSet.getObject(2).toString();
				p_age = resultSet.getObject(3).toString();
				p_birthdate = resultSet.getObject(4).toString();
				p_sex = resultSet.getObject(5).toString();
				p_address = resultSet.getObject(6).toString();
				p_city = resultSet.getObject(7).toString();
				p_telephone = resultSet.getObject(8).toString();
				p_bloodtype = resultSet.getObject(9).toString();
				p_guardiantype = resultSet.getObject(10).toString();
				p_p_father_husband = resultSet.getObject(11).toString();
				p_insurancetype = resultSet.getObject(12).toString();
				p_insurance_no = "" + resultSet.getObject(13).toString();
				p_note = "" + resultSet.getObject(14).toString();
				p_status = "" + resultSet.getObject(15).toString();
				ins_category= resultSet.getString(16);
				isCashless= resultSet.getBoolean(17);
				if(isCashless)
					rdbtnCashless.setSelected(true);
				else
					rdbtnCash.setSelected(true);
				aadhaarTF.setText(new CryptoHandler().decrypt(resultSet.getString(18)));
				aadhaarConfirmTF.setText(new CryptoHandler().decrypt(resultSet.getString(18)));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();

		patientNameTB.setText(p_name);
		rdbtnAge.setSelected((p_agecode.equals("age")) ? true : false);
		agePanel.setVisible((p_agecode.equals("age")) ? true : false);
		rdbtnBirthdate.setSelected((p_agecode.equals("birth")) ? true : false);
		birthDatePanel.setVisible((p_agecode.equals("birth")) ? true : false);
		if (p_agecode.equals("birth")) {
			System.out.print(p_birthdate);
			try {
				birthDateDC.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(p_birthdate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (p_agecode.equals("age")) {
			String[] data=p_age.split("-");
			System.out.print(data.length+" len "+data[0] + "  " + data[1] + " " + data[2]);
			if(data[0]!=null && Integer.parseInt(data[0].toString())<=105)
				selectYCombo.setSelectedIndex(Integer.parseInt("" + data[0]));
			if(data[1]!=null && Integer.parseInt(data[1].toString())<=12)
				selectMcombo.setSelectedIndex(Integer.parseInt("" + data[1]));
			if(data[2]!=null && Integer.parseInt(data[2].toString())<=31)
				selectDcombo.setSelectedIndex(Integer.parseInt("" + data[2]));

		}

		rdbtnMale.setSelected((p_agecode.equals("Male")) ? true : false);
		rdbtnFemale.setSelected((p_agecode.equals("Female")) ? true : false);

		addressTB.setText(p_address);
		cityTB.setText(p_city);
		telephoneTB.setText(p_telephone);

		bloodTypeCB.setSelectedItem(p_bloodtype);
		if(p_status.equals("NULL"))
		{
			p_statusCB.setSelectedIndex(0);
		}else {
			p_statusCB.setSelectedItem(p_status);
		}



		rdbtnFather.setSelected((p_agecode.equals("F")) ? true : false);
		rdbtnHusband.setSelected((p_agecode.equals("H")) ? true : false);

		guardianNameTB.setText(p_p_father_husband);

		if (p_insurancetype.equals("Unknown")) {
			insuranceUnRB.setSelected(true);
			insuranceNoRB.setSelected(false);
			insuranceYesRB.setSelected(false);

		} else if (p_insurancetype.equals("No")) {

			insuranceNoRB.setSelected(true);
			insuranceYesRB.setSelected(false);
			insuranceUnRB.setSelected(false);
		} else {
			insuranceYesRB.setSelected(true);
			insuranceNoRB.setSelected(false);
			insuranceUnRB.setSelected(false);
			insuranceTypeCB.setSelectedItem(p_insurancetype);
			if (p_insurance_no.equals(" "))
				p_insurance_no = "";

			insuranceNoTB.setText(p_insurance_no);
			insurancePanel.setVisible(true);
		}

		noteTA.setText(p_note);

		File file = new File("Patient_Images/" + indexId + ".jpg");
		// for(File file1: file.listFiles()) file1.delete();

		String imagePath = mainDir + "/HMS/Patient_Images/" + indexId + ".jpg";

		try {
			copyFileLocal(imagePath, file.toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (file.exists()) {
			ImageIcon imageIcon = new ImageIcon("Patient_Images/" + indexId + ".jpg");
			Image image = imageIcon.getImage(); // transform it
			Image newimg = image.getScaledInstance(140, 165,
					java.awt.Image.SCALE_SMOOTH); // scale it the smooth way
			imageIcon = new ImageIcon(newimg); //
			patientImageTV.setIcon(imageIcon);
		}
	}

	public void setImageIcon(String imagePath) {
		ImageIcon imageIcon = new ImageIcon(imagePath); // load
		// the
		// image
		// to
		// a
		imagePathSTR=imagePath;																// imageIcon
		Image image = imageIcon.getImage(); // transform it
		Image newimg = image.getScaledInstance(patientImageTV.getWidth(), patientImageTV.getHeight(),  Image.SCALE_DEFAULT); // scale it the smooth way  

		imageIcon = new ImageIcon(newimg); //
		setImage = true;
		patientImageTV.setIcon(imageIcon);
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
		InputStream is = new FileInputStream(new File(source));

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}

	private void copyFileLocal(String source, String dest) throws IOException {
		new File("Patient_Images").mkdir();

		SmbFile remoteFile = new SmbFile(source);
		if (!remoteFile.exists()) {
			return;
		}
		OutputStream os = new FileOutputStream(dest);
		InputStream is = null;
		try {
			is = remoteFile.getInputStream();

		} catch (Exception e) {
			// TODO: handle exception
			return;
		}

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();

	}
	public String findDateOfBirth(int years, int months, int days) {
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
			boolean fetch = true;
			while ((line = bufferedReader.readLine()) != null && fetch) {
				// System.out.println(line);
				str = line;
				fetch = false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0] = data[2];
			open[1] = data[3];
			open[2] = data[4];
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
	public JComboBox getP_statusCB() {
		return p_statusCB;
	}
}
