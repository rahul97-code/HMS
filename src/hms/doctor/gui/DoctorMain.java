package hms.doctor.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.AboutHMS;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.main.MarqueeLabel;
import hms.main.NewsDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import UsersActivity.database.UADBConnection;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DoctorMain extends JFrame {


	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;
	private JTextField searchPatientTB;
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
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String opd_doctorname = "", opd_doctorid = "", opd_date = "",
			opd_symptom = "", opd_token = "", opd_diseasetype = "ALL TYPE",
			opd_type = "Unknown", opd_refferal = "", opd_charge = "";
	String symptomsTA = "", prescriptionTA = "";
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	OPDDBConnection opddbConnection;
	private JTextField opdWisePatientIDTB;
	private JPanel searchPatientPanel;
	private JPanel opdWisePanel;
	private JLabel textField_2;
	PatientOPDHistory opdHistory ;
	PatientTestHistory testHistory = new PatientTestHistory();
	PatientIPDHistory ipdHistory = new PatientIPDHistory();
	ImageIcon opdHistoryIcon = new ImageIcon(
			DoctorMain.class.getResource("/icons/users.png"));
	ImageIcon testHistoryIcon = new ImageIcon(
			DoctorMain.class.getResource("/icons/list_button.png"));
	ImageIcon ipdHistoryIcon = new ImageIcon(
			DoctorMain.class.getResource("/icons/list_button.png"));
	private JRadioButton opdWiseRB;
	private JRadioButton anotherSearchRB;
	private JLabel lblLastopddatelabel;
	int tokenCounter = 1;
	private JButton previousBT;
	private JButton nextBT;
	public static String DoctorNameSTR = "";
	public static String DoctorIDSTR = "";

	private JLabel lblRemainingpatient;
	private JLabel lblTodayopdcounter;
	private JLabel lblCurrentopdid;
	private JLabel opdTokenNoLB;
	int totalOPD = 0;
	private JLabel lastLoginLB;
	public JLabel doctorIDLB;
	public static JLabel doctorNameLB;
	private JTextArea prescriptionTA_1;
	private JTextArea symptomTA;
	private JLabel lblNotAllowedFor;
	private Timer timer;
	public static String username;

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

		DoctorMain frame = new DoctorMain("sukhpal");
		frame.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public DoctorMain(final String username) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				System.exit(0);
				System.out.println("closed");
			}
		});
		this.username=username;
		opdHistory= new PatientOPDHistory();
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DoctorMain.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Doctor Main");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(10, 5, 1295, 700);
		contentPane = new JPanel();
		testHistory.getComponent(contentPane);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Main",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_4.setBounds(6, 20, 1279, 650);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		opddbConnection = new OPDDBConnection();
		opd_token = opddbConnection.retrieveCounterTodayToken() + "";

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(12, 150, 574, 489);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 16, 336, 153);
		panel_7.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
		searchPatientPanel = new JPanel();
		searchPatientPanel.setBounds(6, 35, 324, 87);
		panel_1.add(searchPatientPanel);
		searchPatientPanel.setBorder(null);
		searchPatientPanel.setLayout(null);
		searchPatientPanel.setVisible(false);

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 23, 108, 14);
		searchPatientPanel.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setBounds(119, 18, 165, 25);
		searchPatientPanel.add(searchPatientTB);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		JButton searchBT = new JButton("");
		searchBT.setBounds(286, 18, 28, 25);
		searchPatientPanel.add(searchBT);
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
					patientID.removeAllElements();
					patientIDCB.setModel(patientID);
				}
			}
		});
		searchBT.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/zoom_r_button.png")));
		searchBT.setFocusable(true);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(6, 51, 77, 20);
		searchPatientPanel.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientIDCB = new JComboBox(patientID);
		patientIDCB.setBounds(119, 49, 195, 25);
		searchPatientPanel.add(patientIDCB);
		patientIDCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (!anotherSearchRB.isSelected()) {
					return;
				}
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
				if (!opdTokenNoLB.getText().equals("")
						&& opddbConnection.opdPatientAllowed(
								Integer.parseInt("0" + opdTokenNoLB.getText()),
								p_id)) {
					lblNotAllowedFor.setVisible(true);
					opddbConnection.closeConnection();
					return;
				}
				opddbConnection.closeConnection();
				setPatientDetail(p_id);
				lblNotAllowedFor.setVisible(false);
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

				}

			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdWiseRB = new JRadioButton("OPD Wise");
		opdWiseRB.setSelected(true);
		opdWiseRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					opdHistory.populateOPDHistoryTable("000");
					ipdHistory.populateIPDHistoryTable("000");
					testHistory.populateTestHistoryTable("000");
					lblLastopddatelabel.setText("");
					searchPatientPanel.setVisible(false);
					opdWisePanel.setVisible(true);
					opdWisePatientIDTB.setText(""
							+ opdWisePatientIDTB.getText());
					lblNotAllowedFor.setVisible(false);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
		});
		opdWiseRB.setBounds(29, 7, 109, 23);
		panel_1.add(opdWiseRB);
		opdWiseRB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		anotherSearchRB = new JRadioButton("Another Search");
		anotherSearchRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					opdHistory.populateOPDHistoryTable("000");
					ipdHistory.populateIPDHistoryTable("000");
					testHistory.populateTestHistoryTable("000");
					lblLastopddatelabel.setText("");
					searchPatientPanel.setVisible(true);
					opdWisePanel.setVisible(false);
					searchPatientTB.setText("" + searchPatientTB.getText());
				} catch (Exception e2) {
					// TODO: handle exception
				}

			}
		});
		anotherSearchRB.setBounds(149, 7, 133, 23);
		panel_1.add(anotherSearchRB);
		anotherSearchRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdSearchOption.add(anotherSearchRB);
		opdSearchOption.add(opdWiseRB);

		opdWisePanel = new JPanel();
		opdWisePanel.setBounds(6, 46, 324, 76);
		panel_1.add(opdWisePanel);
		opdWisePanel.setLayout(null);

		JLabel lblOpdPatientId = new JLabel("OPD Patient ID :");
		lblOpdPatientId.setBounds(1, 31, 101, 14);
		opdWisePanel.add(lblOpdPatientId);
		lblOpdPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdWisePatientIDTB = new JTextField();
		opdWisePatientIDTB.setEditable(false);
		opdWisePatientIDTB.setBounds(112, 26, 171, 25);
		opdWisePanel.add(opdWisePatientIDTB);
		opdWisePatientIDTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdWisePatientIDTB.setColumns(10);

		lblNotAllowedFor = new JLabel("Not allowed. Please click on OPD Wise");
		lblNotAllowedFor.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNotAllowedFor.setForeground(Color.RED);
		lblNotAllowedFor.setHorizontalAlignment(SwingConstants.CENTER);
		lblNotAllowedFor.setBounds(6, 119, 320, 34);
		lblNotAllowedFor.setVisible(false);
		panel_1.add(lblNotAllowedFor);
		opdWisePatientIDTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = opdWisePatientIDTB.getText() + "";
						if (!str.equals("")) {
							setPatientDetail(str);
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
						} else {
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = opdWisePatientIDTB.getText() + "";
						if (!str.equals("")) {
							setPatientDetail(str);
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
						} else {
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = opdWisePatientIDTB.getText() + "";
						if (!str.equals("")) {
							setPatientDetail(str);
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
						} else {
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
						}
					}
				});

		JPanel panel = new JPanel();
		panel.setBounds(5, 180, 337, 255);
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
		patientNameTB.setBounds(106, 16, 201, 25);
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
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 192, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(352, 92, 210, 154);
		panel_7.add(panel_3);
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Note & Symptom :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 23, 188, 121);
		panel_3.add(scrollPane);

		symptomTA = new JTextArea();
		symptomTA.setLineWrap(true);
		symptomTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		symptomTA.setRows(10);
		scrollPane.setViewportView(symptomTA);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(352, 257, 210, 166);
		panel_7.add(panel_5);
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Prescription :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 21, 191, 134);
		panel_5.add(scrollPane_1);

		prescriptionTA_1 = new JTextArea();
		prescriptionTA_1.setLineWrap(true);
		prescriptionTA_1.setRows(10);
		prescriptionTA_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_1.setViewportView(prescriptionTA_1);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(352, 16, 210, 64);
		panel_7.add(panel_6);
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_6.setLayout(null);

		JLabel lblOpdDate = new JLabel("OPD Date :");
		lblOpdDate.setHorizontalAlignment(SwingConstants.CENTER);
		lblOpdDate.setBounds(10, 11, 188, 14);
		panel_6.add(lblOpdDate);
		lblOpdDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblLastopddatelabel = new JLabel("");
		lblLastopddatelabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblLastopddatelabel.setForeground(Color.RED);
		lblLastopddatelabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLastopddatelabel.setBounds(10, 29, 188, 21);
		panel_6.add(lblLastopddatelabel);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setBounds(362, 430, 188, 32);
		panel_7.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				
				symptomsTA = symptomTA.getText().toString();
				prescriptionTA = prescriptionTA_1.getText().toString();
				String[] data = new String[5];
				data[0] = symptomsTA;
				data[1] = prescriptionTA;
				data[2] = opdHistory.rdbtnYes.isSelected()?"1":"0";
				data[3] = opdHistory.rdbtnUSG.isSelected()?"1":"0";;
				data[4] = lblCurrentopdid.getText().toString();
				OPDDBConnection opddbConnection = new OPDDBConnection();
				try {
					opddbConnection.updatePrescriptionData1(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} finally {
					opddbConnection.closeConnection();
				}
				JOptionPane.showMessageDialog(null,
						"Prescription and Symptoms saved successfully ",
						"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnNewButton_3.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/plus_button.png")));
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "News",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_11.setBounds(6, 434, 336, 47);
		panel_7.add(panel_11);
		panel_11.setLayout(null);
		NewsDBConnection newsDBConnection = new NewsDBConnection();
		JLabel newsLB = new MarqueeLabel(newsDBConnection.getNews(),
				MarqueeLabel.RIGHT_TO_LEFT, 20);
		newsDBConnection.closeConnection();
		newsLB.setBounds(6, 14, 334, 26);
		panel_11.add(newsLB);
		newsLB.setForeground(Color.RED);
		newsLB.setFont(new Font("Tahoma", Font.BOLD, 13));

		JLabel lblAddSomePrescription = new JLabel("Add Prescription & Symptom");
		lblAddSomePrescription.setHorizontalAlignment(SwingConstants.CENTER);
		lblAddSomePrescription.setBounds(367, 463, -2, 19);
		panel_7.add(lblAddSomePrescription);

		JLabel label = new JLabel("Developed By : Sukhpal Saini");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setBounds(352, 461, 210, 20);
		panel_7.add(label);
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
				//	getPatientsID(str);
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
		opd_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,
				null, null));
		panel_2.setBounds(6, 17, 1263, 50);
		panel_4.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblTodayDate = new JLabel("Date :");
		lblTodayDate.setForeground(new Color(0, 0, 128));
		lblTodayDate.setBounds(1025, 2, 69, 20);
		lblTodayDate.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(lblTodayDate);

		textField_2 = new JLabel(opd_date);
		textField_2.setForeground(new Color(0, 0, 128));
		textField_2.setBounds(1102, 2, 123, 20);
		textField_2.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_2.add(textField_2);

		JLabel lblRotaryAmbalaCancer = new JLabel(
				" Rotary Ambala Cancer and General Hospital (Ambala Cantt.)");
		lblRotaryAmbalaCancer.setForeground(Color.BLUE);
		lblRotaryAmbalaCancer.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblRotaryAmbalaCancer.setBounds(65, 10, 635, 28);
		panel_2.add(lblRotaryAmbalaCancer);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/smallLogo.png")));
		lblNewLabel.setBounds(10, 2, 53, 44);
		panel_2.add(lblNewLabel);

		JButton btnLogout = new JButton("Logout");
		btnLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainLogin frame = new MainLogin();
				frame.setVisible(true);
				System.exit(0);
				dispose();
			}
		});
		btnLogout.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/regular_close_tab.JPG")));
		btnLogout.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnLogout.setBounds(733, 9, 95, 30);
		panel_2.add(btnLogout);

		JButton btnSettings = new JButton("Settings");
		btnSettings.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DoctorSettings ds = new DoctorSettings(""
						+ doctorIDLB.getText());
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		btnSettings.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/setting.png")));
		btnSettings.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnSettings.setBounds(832, 9, 101, 30);
		panel_2.add(btnSettings);

		JLabel lblTodayTime = new JLabel("Time :");
		lblTodayTime.setForeground(new Color(0, 0, 128));
		lblTodayTime.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTodayTime.setBounds(1025, 26, 69, 20);
		panel_2.add(lblTodayTime);

		final JLabel lblTime = new JLabel("time");
		lblTime.setForeground(new Color(0, 0, 128));
		lblTime.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTime.setBounds(1102, 26, 123, 20);
		panel_2.add(lblTime);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_8.setBounds(596, 150, 673, 489);
		panel_4.add(panel_8);
		panel_8.setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setBounds(6, 11, 660, 470);
		panel_8.add(tabbedPane);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.addTab("Patient OPD History", opdHistoryIcon, opdHistory,
				"Patient OPD History");
		tabbedPane.addTab("Patient Test History", testHistoryIcon, testHistory);
		tabbedPane.addTab("Patient IPD History", testHistoryIcon, ipdHistory,
				"Patient IPD History");

		nextBT = new JButton("Next");
		nextBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				opdWiseRB.setSelected(true);
				searchPatientPanel.setVisible(false);
				opdWisePanel.setVisible(true);
				if (tokenCounter == totalOPD || totalOPD == 0) {
					JOptionPane.showMessageDialog(null, "Not Available",
							"Patient", JOptionPane.INFORMATION_MESSAGE);
				} else {
					tokenCounter++;
					getOPDwisePatient("" + tokenCounter);

				}
			}
		});
		nextBT.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/1rightarrow_hover.png")));
		nextBT.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
		nextBT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		nextBT.setBounds(1134, 86, 117, 50);
		panel_4.add(nextBT);

		previousBT = new JButton("Previous");
		previousBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opdWiseRB.setSelected(true);
				searchPatientPanel.setVisible(false);
				opdWisePanel.setVisible(true);
				if (tokenCounter == 1 || totalOPD == 0) {
					JOptionPane.showMessageDialog(null, "Not Available",
							"Patient", JOptionPane.INFORMATION_MESSAGE);
				} else {
					tokenCounter--;
					getOPDwisePatient("" + tokenCounter);
				}
			}
		});
		previousBT.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/1leftarrow_hover.png")));
		previousBT.setFont(new Font("Tahoma", Font.PLAIN, 15));
		previousBT.setBounds(912, 86, 117, 50);
		panel_4.add(previousBT);

		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Token",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_9.setBounds(1039, 78, 85, 60);
		panel_4.add(panel_9);
		panel_9.setLayout(null);

		opdTokenNoLB = new JLabel("");
		opdTokenNoLB.setHorizontalAlignment(SwingConstants.CENTER);
		opdTokenNoLB.setBounds(10, 20, 65, 29);
		panel_9.add(opdTokenNoLB);
		opdTokenNoLB.setFont(new Font("Tahoma", Font.PLAIN, 30));
		opdTokenNoLB.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					try {
						int token = Integer.parseInt(JOptionPane.showInputDialog("Input Token Number", ""));
						opdWiseRB.setSelected(true);
						searchPatientPanel.setVisible(false);
						opdWisePanel.setVisible(true);
						if (token > totalOPD || totalOPD == 0) {
							JOptionPane.showMessageDialog(null, "Not Available",
									"Patient", JOptionPane.INFORMATION_MESSAGE);
						} else if (token == 0 || totalOPD == 0) {
							JOptionPane.showMessageDialog(null, "Not Available",
									"Patient", JOptionPane.INFORMATION_MESSAGE);
						}else {
							tokenCounter=token;
							getOPDwisePatient("" + tokenCounter);
						}
						
					} catch (Exception e2) {
						// TODO: handle exception
					}
					

				}
			}
		});

		JLabel lblTodayOpdPatients = new JLabel("Today OPD Patients :");
		lblTodayOpdPatients.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTodayOpdPatients.setBounds(559, 78, 134, 21);
		panel_4.add(lblTodayOpdPatients);

		JLabel lblRemaingPatients = new JLabel("Remaining Patients :");
		lblRemaingPatients.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRemaingPatients.setBounds(559, 100, 134, 21);
		panel_4.add(lblRemaingPatients);

		JLabel lblCurrentOpdId = new JLabel("Current OPD No. :");
		lblCurrentOpdId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCurrentOpdId.setBounds(559, 124, 134, 21);
		panel_4.add(lblCurrentOpdId);

		lblTodayopdcounter = new JLabel("");
		lblTodayopdcounter.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTodayopdcounter.setBounds(703, 78, 170, 21);
		panel_4.add(lblTodayopdcounter);

		lblRemainingpatient = new JLabel("");
		lblRemainingpatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRemainingpatient.setBounds(703, 101, 170, 19);
		panel_4.add(lblRemainingpatient);

		lblCurrentopdid = new JLabel("");
		lblCurrentopdid.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCurrentopdid.setBounds(703, 124, 170, 21);
		panel_4.add(lblCurrentopdid);

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_10.setBounds(12, 70, 68, 72);
		panel_4.add(panel_10);
		panel_10.setLayout(null);

		JLabel label_1 = new JLabel("");
		label_1.setBounds(10, 4, 48, 62);
		panel_10.add(label_1);
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		label_1.setIcon(new ImageIcon(DoctorMain.class
				.getResource("/icons/operation_dialog.png")));

		JLabel lblDoctorId = new JLabel("Doctor ID :");
		lblDoctorId.setForeground(new Color(0, 128, 128));
		lblDoctorId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDoctorId.setBounds(103, 78, 134, 14);
		panel_4.add(lblDoctorId);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setForeground(new Color(0, 128, 128));
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDoctorName.setBounds(103, 102, 150, 14);
		panel_4.add(lblDoctorName);

		JLabel lblLastLogin = new JLabel("Last Login :");
		lblLastLogin.setForeground(new Color(0, 128, 128));
		lblLastLogin.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblLastLogin.setBounds(103, 124, 117, 21);
		panel_4.add(lblLastLogin);

		doctorIDLB = new JLabel("New label");
		doctorIDLB.setForeground(new Color(0, 128, 128));
		doctorIDLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		doctorIDLB.setBounds(247, 77, 302, 20);
		panel_4.add(doctorIDLB);

		doctorNameLB = new JLabel("New label");
		doctorNameLB.setForeground(new Color(0, 128, 128));
		doctorNameLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		doctorNameLB.setBounds(247, 99, 302, 20);
		panel_4.add(doctorNameLB);

		lastLoginLB = new JLabel("");
		lastLoginLB.setForeground(new Color(0, 128, 128));
		lastLoginLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lastLoginLB.setBounds(247, 124, 302, 20);
		panel_4.add(lastLoginLB);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1309, 21);
		contentPane.add(menuBar);

		JMenu mnMyAccount = new JMenu("My Account");
		menuBar.add(mnMyAccount);

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DoctorSettings ds = new DoctorSettings(""
						+ doctorIDLB.getText());
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		mnMyAccount.add(mntmChangePassword);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MainLogin frame = new MainLogin();
				frame.setVisible(true);
				dispose();
			}
		});
		mnMyAccount.add(mntmLogout);

		JMenu mnPatients = new JMenu("My Patients");
		menuBar.add(mnPatients);

		JMenuItem mntmIpdPatients = new JMenuItem("IPD Patients");
		mntmIpdPatients.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
ua.check_activity(username, 141, 1);
				MyIPDPatients ipd = new MyIPDPatients(DoctorNameSTR);
				ipd.setVisible(true);
				ipd.setModal(true);
				
			}
		});
		mnPatients.add(mntmIpdPatients);

		JMenuItem mntmOpdPatients = new JMenuItem("OPD Patients");
		//ua.check_activity(username, 142, 1)
		mnPatients.add(mntmOpdPatients);
		getDoctorsDetail(username);
		JMenu mnAbout_1 = new JMenu("Med Discount");
		if(DocMedAccess())
		menuBar.add(mnAbout_1);
		
		JMenuItem mntmAboutSoftware_1 = new JMenuItem("Patient Med Discount");
		mntmAboutSoftware_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPDMedDiscount OPDMedDiscount=new OPDMedDiscount();
				OPDMedDiscount.setVisible(true);
				OPDMedDiscount.setModal(true);
			}
		});
		mnAbout_1.add(mntmAboutSoftware_1);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmAboutSoftware = new JMenuItem("About HMS");
		mntmAboutSoftware.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				new AboutHMS().setVisible(true);
			}
		});
		mnAbout.add(mntmAboutSoftware);
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
		Timer timer = new Timer(500, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
				// System.out.print(dateFormat.format(cal1.getTime()));
				lblTime.setText(dateFormat.format(cal1.getTime()));
			}
		});
		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.start();
		Timer opdTimer = new Timer(5000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				opddbConnection = new OPDDBConnection();
				totalOPD = opddbConnection.getDoctorTodayOPD(DoctorNameSTR,
						opd_date);
				opddbConnection.closeConnection();
				if (totalOPD > 0) {
					lblTodayopdcounter.setText(totalOPD + "");
					int remainingOPD = totalOPD - tokenCounter;
					lblRemainingpatient.setText("" + remainingOPD);
				}
				if (totalOPD > 0 && getOPD) {
					// getOPDwisePatient("" + tokenCounter);
					getOPD = false;
				}
			}
		});
		opdTimer.setRepeats(true);
		opdTimer.setCoalesce(true);
		opdTimer.start();

		getOPDwisePatient("" + tokenCounter);
		nextBT.requestFocus();
		Thread t = new Thread(new Runnable() {
			private int counter = 0;

			public void run() {
				while (true) {
					counter++;
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if (counter % 2 == 0) {
								lblNotAllowedFor.setForeground(Color.red);
								counter = 0;
							} else {
								lblNotAllowedFor.setForeground(Color.black);
							}
						}
					});
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	public void getPatientsID(String index) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.searchPatientWithIdOrNmae(index);
		patientID.removeAllElements();
		try {
			while (resultSet.next()) {
				patientID.addElement(resultSet.getObject(2).toString());
				patientIDCB.setModel(patientID);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		
	}

	public void setPatientDetail(String indexId) {
		if (anotherSearchRB.isSelected()
				&& searchPatientTB.getText().equals("")) {
			opdHistory.populateOPDHistoryTable("000");
			testHistory.populateTestHistoryTable("000");
			ipdHistory.populateIPDHistoryTable("000");
		} else {
			opdHistory.populateOPDHistoryTable(indexId);
			testHistory.populateTestHistoryTable(indexId);
			ipdHistory.populateIPDHistoryTable(indexId);
		}
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
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opddbConnection = new OPDDBConnection();
		String lastOPDDate = opddbConnection.retrieveLastOpdPatient(indexId);
		opddbConnection.opdPatientAllowed(
				Integer.parseInt("0" + opdTokenNoLB.getText()), indexId);
		lblLastopddatelabel.setText(lastOPDDate);
		opddbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	public void getDoctorsDetail(String username) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveUsernameDetail(username);
		try {
			while (resultSet.next()) {
				DoctorIDSTR=resultSet.getObject(1).toString();
				doctorIDLB.setText(resultSet.getObject(1).toString());
				DoctorNameSTR = resultSet.getObject(2).toString();
				doctorNameLB.setText(DoctorNameSTR);
				lastLoginLB.setText("" + resultSet.getObject(7).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbConnection.updateDataLastLogn(doctorIDLB.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		readCounterFile(username);
	}

	public void getOPDwisePatient(String index) {
		System.out.print("" + index);
		writeCounter(tokenCounter + "");
		opddbConnection = new OPDDBConnection();
		lblNotAllowedFor.setVisible(false);
		totalOPD = opddbConnection.getDoctorTodayOPD(DoctorNameSTR, opd_date);
		if (totalOPD == 0) {
			return;
		}
		lblTodayopdcounter.setText(totalOPD + "");
		int remainingOPD = totalOPD - tokenCounter;
		lblRemainingpatient.setText("" + remainingOPD);
		ResultSet resultSet = opddbConnection.getNextOPD(DoctorNameSTR,
				opd_date, index);
		try {
			while (resultSet.next()) {
				lblCurrentopdid.setText(resultSet.getObject(1).toString());
				opdWisePatientIDTB.setText(resultSet.getObject(2).toString());
				lblLastopddatelabel.setText(resultSet.getObject(3).toString());
				opdTokenNoLB.setText(resultSet.getObject(4).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		opddbConnection.closeConnection();
	}

	public void readCounterFile(String username) {
		// The name of the file to open.
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String dateSTR = lastLoginLB.getText().toString();
		String data[] = new String[22];
		int i = 0;
		for (String retval : dateSTR.split(" ")) {
			data[i] = retval;
			System.out.println("sss " + retval);
			i++;
		}
		dateSTR = data[0];
		String counter = "";
		// System.out.println(sdf.format(date));
		Date date1 = null;
		try {
			date1 = sdf.parse(sdf.format(date));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveUsernameDetail(username);
		try {
			while (resultSet.next()) {

				counter = resultSet.getObject(8).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbConnection.updateDataLastLogn(doctorIDLB.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		try {
			System.out.println(dateSTR);
			Date date2 = sdf.parse(dateSTR);
			System.out.println(sdf.format(date1));
			System.out.println(sdf.format(date2));
			if (date1.compareTo(date2) == 0) {
				tokenCounter = Integer.parseInt(counter);
				opdWiseRB.setSelected(true);
				searchPatientPanel.setVisible(false);
				opdWisePanel.setVisible(true);
				{
					getOPDwisePatient("" + tokenCounter);
					writeCounter(tokenCounter + "");
				}
			} else {
				System.out.println("How to get here?");
				writeCounter("0");
			}

		} catch (ParseException ex) {
			ex.printStackTrace();
		}

	}

	public void writeCounter(String counter) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		try {
			dbConnection.updateDataCounter(doctorIDLB.getText().toString(),
					counter);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public boolean DocMedAccess() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet rs=dbConnection.MedDiscountAccess(DoctorMain.DoctorIDSTR);
		try {
			while (rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return false;
	}

	public JLabel getLblNotAllowedFor() {
		return lblNotAllowedFor;
	}
}
