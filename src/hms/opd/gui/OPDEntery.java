package hms.opd.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.gui.NewPatient;
import hms.patient.slippdf.OPDSlippdf;
import hms.patient.slippdf.*;
import hms.patient.slippdf.OPDSlippdf_new;
import hms.payments.PaymentMain;
import hms.patient.slippdf.OPDSlippdf2;
import hms.patient.slippdf.OPDSlippdfESI;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.reception.gui.UpdateCounterThread;
import hms.test.free_test.FreeTestDBConnection;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class OPDEntery extends JDialog {

	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField attendanceDateTB;
	private JTextField opdNOTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	String[] data = new String[20];
	private JTextField insuranceTypeTB;	
	JComboBox opdTypeCB;
	JLabel lastOPDDateLB;
	ButtonGroup opdTypeGP = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String opd_doctorname = "", opd_doctorid = "", opd_date = "",
			opd_symptom = "", opd_token = "", opd_type = "Select OPD Type",
			opd_Attendence_type = "Unknown", opd_refferal = "",
			opd_charge = "";
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctorsSRC = new DefaultComboBoxModel();
	final DefaultComboBoxModel opdTypeCBM = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	ArrayList opdPriceAL = new ArrayList();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	OPDDBConnection opddbConnection;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JCheckBox chckbxRefferalFrom;
	private JCheckBox chckbxReattendance;
	private JCheckBox chckbxRefferalTo;
	private JCheckBox chckbxNewAttendance;
	private JTextArea symptomsTA;
	private JLabel OPDChargesLB;
	Font customFont;
	private JList freeTestList;
	OPDBrowser opdBrowser;
	ArrayList freeTestID = new ArrayList();
	ArrayList freeTestName = new ArrayList();
	ArrayList freeTestSortName = new ArrayList();
	ArrayList freeTestUnits = new ArrayList();
	private Timer timer;
	private Timer timer1;
	private JComboBox paymentModeCB;
	int finalPrice = 0;
	private JTextField search_doctor;
	private JTextField search_type;
	private boolean isCashless;
	private JLabel lblCashless;
	private JButton btnNewButton_3;
	private boolean alreadyPaid;
	private String req_urn;
	private double finalCharges;
	private double totalPaidPayment;
	private String payMode;
	private JLabel lblOpdCharges_1;
	private JLabel OPDChargesLB_1;

	public static void main(String[] args) {
		new OPDEntery(null).setVisible(true);;
	}
	/**
	 * Create the frame.
	 */
	public OPDEntery(OPDBrowser opdBrowser) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OPDEntery.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New OPD");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		this.opdBrowser = opdBrowser;
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
		setBounds(150, 70, 735, 689);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "New OPD Entery Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 13), null));
		panel_4.setBounds(12, 0, 699, 648);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		// opddbConnection = new OPDDBConnection();
		// opd_token = opddbConnection.retrieveCounterTodayToken() + "";
		// opddbConnection.closeConnection();
		JLabel lblAttendenceDate = new JLabel("Attendence Date");
		lblAttendenceDate.setBounds(6, 44, 128, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 84, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					getPatientsID(str);

				} else {
					opdTypeCB.setSelectedIndex(0);
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");

					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					//					patientID.removeAllElements();
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					lastOPDDateLB.setText("Last OPD :");


				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(119, 79, 182, 25);
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
						search_doctor.setText("");
						search_type.setText("");
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
						search_doctor.setText("");
						search_type.setText("");
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
						search_doctor.setText("");
						search_type.setText("");
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
					opdTypeCB.setSelectedIndex(0);
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					//					patientID.removeAllElements();
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					//					patientIDCB.setModel(patientID);
					lastOPDDateLB.setText("Last OPD :");
				}
			}
		});
		searchBT.setBounds(301, 78, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/zoom_r_button.png")));
		opd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(119, 41, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JLabel lblOpd = new JLabel("OPD No.");
		lblOpd.setBounds(365, 40, 67, 23);
		panel_4.add(lblOpd);
		lblOpd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdNOTB = new JTextField("Auto Generated");

		opdNOTB.setBounds(442, 41, 171, 25);
		panel_4.add(opdNOTB);
		opdNOTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdNOTB.setEditable(false);
		opdNOTB.setColumns(10);

		JButton addNewPatientBT = new JButton("");
		addNewPatientBT.setToolTipText("Add New Patient");
		addNewPatientBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewPatient newPatient = new NewPatient();
				newPatient.OPDObject(OPDEntery.this);
				newPatient.setVisible(true);
				// JInternalFrame internal = new JInternalFrame("Frame", false,
				// true, false, true);
				// internal.setContentPane(newPatient.getContentPane());
				// internal.setVisible(true);
				// internal.pack();
				// internal.setBounds(50, 50, 690, 524);
				// internal.putClientProperty("dragMode", "fixed");
				// desktop.add(internal);
			}
		});
		addNewPatientBT.setBounds(615, 77, 30, 25);
		panel_4.add(addNewPatientBT);
		addNewPatientBT.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/plus_button.png")));

		JButton editPatientBT = new JButton("");
		editPatientBT.setToolTipText("Edit Patient");
		editPatientBT.setBounds(647, 77, 30, 25);
		panel_4.add(editPatientBT);
		editPatientBT.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/edit_button.png")));

		lastOPDDateLB = new JLabel("Last OPD :");
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setBounds(178, 115, 364, 19);
		panel_4.add(lastOPDDateLB);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel = new JPanel();
		panel.setBounds(365, 145, 313, 296);
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

		JLabel lblType = new JLabel("Type :");
		lblType.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblType.setBounds(16, 256, 70, 28);
		panel.add(lblType);

		lblCashless = new JLabel("");
		lblCashless.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblCashless.setBounds(106, 256, 191, 28);
		panel.add(lblCashless);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(365, 79, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(4, 146, 349, 409);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(5, 12, 337, 150);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Detail",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 52, 108, 25);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameCB = new JComboBox();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					opd_doctorname = doctorNameCB.getSelectedItem().toString();
					System.out.print("" + opd_doctorname);
					getDoctorDetail(opd_doctorname);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		doctorNameCB.setBounds(107, 52, 218, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDoctorRoom = new JLabel("Doctor Room :");
		lblDoctorRoom.setBounds(6, 89, 108, 25);
		panel_1.add(lblDoctorRoom);
		lblDoctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_4 = new JLabel("Doctor specialization :");
		lblNewLabel_4.setBounds(6, 117, 141, 24);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorRoomLB = new JLabel("");
		doctorRoomLB.setBounds(145, 82, 186, 23);
		panel_1.add(doctorRoomLB);
		doctorRoomLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorSpecializationLB = new JLabel("");
		doctorSpecializationLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorSpecializationLB.setBounds(145, 104, 186, 25);
		panel_1.add(doctorSpecializationLB);


		search_doctor = new JTextField();
		search_doctor.setBounds(107, 12, 190, 25);
		panel_1.add(search_doctor);
		search_doctor.setColumns(10);
		search_doctor.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = search_doctor.getText() + "";
						getStringCB(str);
					}
					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = search_doctor.getText() + "";
						getStringCB(str);
					}
					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = search_doctor.getText() + "";
						getStringCB(str);
					}
				});
		JButton searchBT_1 = new JButton("");
		searchBT_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = search_doctor.getText() + "";

				getStringCB(str);

			}
		});
		searchBT_1.setIcon(new ImageIcon(OPDEntery.class.getResource("/icons/zoom_r_button.png")));
		searchBT_1.setToolTipText("Search");
		searchBT_1.setFocusable(true);
		searchBT_1.setBounds(297, 12, 28, 25);
		panel_1.add(searchBT_1);

		JLabel lblNewLabel = new JLabel("Search Doc:");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel.setBounds(6, 17, 87, 15);
		panel_1.add(lblNewLabel);

		JLabel lblNewLabel_3 = new JLabel("OPD Type :");
		lblNewLabel_3.setBounds(33, 249, 92, 25);
		panel_2.add(lblNewLabel_3);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdTypeCB = new JComboBox();

		opdTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//				if (patientNameTB.getText().toString().equals("")) {
				//					JOptionPane
				//							.showMessageDialog(
				//									null,
				//									"Please select the patient first and then opd type for set prices",
				//									"Input Error", JOptionPane.ERROR_MESSAGE);
				//					opdTypeCB.setSelectedIndex(0);
				//				}
				try {
					opd_type = opdTypeCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!opd_type.equals("Select OPD Type")
						&& opdTypeCB.getSelectedIndex() >= 0) {
					System.out.println("Index  :  "
							+ opdTypeCB.getSelectedIndex());
					System.out.println("Index  :  "
							+ opdPriceAL.get(opdTypeCB.getSelectedIndex())
							+ "  " + opdTypeCB.getSelectedIndex());
					getPrice(opdPriceAL.get(opdTypeCB.getSelectedIndex()) + "");
				} else {
					OPDChargesLB.setText("");
				}
			}
		});
		opdTypeCB.setBounds(114, 249, 228, 25);
		panel_2.add(opdTypeCB);
		opdTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Note & Symptom :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_3.setBounds(5, 293, 337, 89);
		panel_2.add(panel_3);
		panel_3.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 325, 77);
		panel_3.add(scrollPane_1);
		symptomsTA = new JTextArea();
		symptomsTA.setLineWrap(true);
		symptomsTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		symptomsTA.setRows(10);
		scrollPane_1.setViewportView(symptomsTA);

		paymentModeCB = new JComboBox();
		paymentModeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					paymentModeCB.getSelectedItem().toString();
					System.out.print("" + opd_doctorname);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getAllPaymentModes();
		paymentModeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paymentModeCB.setBounds(114, 174, 228, 25);
		panel_2.add(paymentModeCB);

		JLabel lblPamentMode = new JLabel("Payment Mode :");
		lblPamentMode.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPamentMode.setBounds(15, 174, 92, 25);
		panel_2.add(lblPamentMode);

		search_type = new JTextField();
		search_type.setBounds(114, 207, 202, 25);
		panel_2.add(search_type);
		search_type.setColumns(10);
		search_type.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = search_type.getText() + "";

						getAllOPDType(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = search_type.getText() + "";

						getAllOPDType(str);

					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = search_type.getText() + "";

						getAllOPDType(str);

					}


				});


		JButton searchBT_1_1 = new JButton("");
		searchBT_1_1.setIcon(new ImageIcon(OPDEntery.class.getResource("/icons/zoom_r_button.png")));
		searchBT_1_1.setToolTipText("Search");
		searchBT_1_1.setFocusable(true);
		searchBT_1_1.setBounds(314, 207, 28, 25);
		panel_2.add(searchBT_1_1);

		JLabel lblSearchType = new JLabel("Search Type:");
		lblSearchType.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearchType.setBounds(25, 212, 82, 15);
		panel_2.add(lblSearchType);

		btnNewButton_3 = new JButton("Save");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if (isDublicateOPD()) {
					int response = JOptionPane.showConfirmDialog(null,"Patient OPD already registered. Do you want to continue?","Confirm",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (response == JOptionPane.YES_OPTION) {
						saveOPD();
					} 
				}else {
					saveOPD();
				}
				
				
			}
		});
		btnNewButton_3.setBounds(376, 530, 148, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/plus_button.png")));
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();

			}
		});
		btnNewButton_4.setBounds(539, 530, 148, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		chckbxReattendance = new JCheckBox("Re-Attendance");
		chckbxReattendance.setBounds(71, 16, 126, 23);
		panel_4.add(chckbxReattendance);
		chckbxReattendance.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdTypeGP.add(chckbxReattendance);

		chckbxNewAttendance = new JCheckBox("New Attendance");
		chckbxNewAttendance.setBounds(199, 16, 132, 23);
		panel_4.add(chckbxNewAttendance);
		chckbxNewAttendance.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdTypeGP.add(chckbxNewAttendance);

		chckbxRefferalFrom = new JCheckBox("Refferal From");
		chckbxRefferalFrom.setBounds(365, 18, 114, 23);
		panel_4.add(chckbxRefferalFrom);
		chckbxRefferalFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		chckbxRefferalTo = new JCheckBox("Refferal To");
		chckbxRefferalTo.setBounds(493, 18, 107, 23);
		panel_4.add(chckbxRefferalTo);
		chckbxRefferalTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

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
				System.out.println(p_id + "fgfdg");
				if(p_id.contains("(")){
					p_id = p_id.substring(0, p_id.indexOf("("));
					setPatientDetail(p_id);
					getStringCB("");
					getAllOPDType("");	
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
		patientIDCB.setBounds(442, 77, 171, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JLabel lblOpdCharges = new JLabel("OPD Charges :");
		lblOpdCharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblOpdCharges.setBounds(394, 457, 115, 25);
		panel_4.add(lblOpdCharges);

		OPDChargesLB = new JLabel("");
		OPDChargesLB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		OPDChargesLB.setBounds(519, 452, 114, 34);
		panel_4.add(OPDChargesLB);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(null, "Free Test",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(6, 557, 349, 67);
		panel_4.add(panel_6);
		panel_6.setLayout(null);

		freeTestList = new JList();
		freeTestList.setFont(new Font("Tahoma", Font.BOLD, 13));
		freeTestList.setBounds(10, 13, 329, 45);
		panel_6.add(freeTestList);

		lblOpdCharges_1 = new JLabel("OPD Paid Amt :");
		lblOpdCharges_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblOpdCharges_1.setBounds(394, 489, 115, 25);
		panel_4.add(lblOpdCharges_1);

		OPDChargesLB_1 = new JLabel("");
		OPDChargesLB_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		OPDChargesLB_1.setBounds(519, 484, 114, 34);
		panel_4.add(OPDChargesLB_1);

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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				searchPatientTB.requestFocus();
			}
		});
		// searchPatientTB.requestFocusInWindow();
		contentPane.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					saveOPD();
				}
			}
		});
	}

	public void searchAutoFill(String p_id)
	{
		searchPatientTB.setText(p_id);
		searchPatientTB.setEnabled(false);
	}
	public void saveOPD() {
		opd_symptom = symptomsTA.getText() + "";
		if (chckbxReattendance.isSelected()) {
			opd_Attendence_type = "Re-Attendance";
		} else {
			opd_Attendence_type = "New Attendance";
		}

		if (patientNameTB.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "Please select the patient",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		} else if (opd_doctorid.equals("")) {
			JOptionPane.showMessageDialog(null, "Please select opd doctor",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		} else if (opd_type.equals("Select OPD Type")) {
			JOptionPane.showMessageDialog(null, "Please select opd type",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		}  else if (paymentModeCB.getSelectedItem().toString().equals("Select Payment Mode")) {
			JOptionPane.showMessageDialog(null, "Please select Payment Mode",
					"Input Error", JOptionPane.ERROR_MESSAGE);
		} else {
			int payModeIndex=paymentModeCB.getSelectedIndex();
			if((!p_insurancetype.equals("Unknown") && payModeIndex==1) || (p_insurancetype.equals("Unknown") && payModeIndex==3 )){
				
				  int response = JOptionPane.showConfirmDialog(
			                null,
			                "You have selected pay mode: " + paymentModeCB.getSelectedItem() + " for Ins: "+p_insurancetype+"\nDo you want to proceed?",
			                "Confirmation",
			                JOptionPane.YES_NO_OPTION,
			                JOptionPane.WARNING_MESSAGE
			            );
			            if (!(response == JOptionPane.YES_OPTION)) {
			            	return;
			            }
			}
			if(!alreadyPaid) {
				payMode=paymentModeCB.getSelectedItem().toString();
				finalCharges=finalPrice;
			}
			if(!alreadyPaid && (payModeIndex!=1 && payModeIndex!=3)) {		
				btnNewButton_3.setEnabled(false);	
				PaymentMain MachinePayDialog=new PaymentMain(new String[]{finalPrice+"",p_id,p_name,ReceptionMain.receptionNameSTR,opd_doctorname},"NEW OPD");
				MachinePayDialog.setModal(true);
				MachinePayDialog.setVisible(true);
				req_urn=MachinePayDialog.getRequestedUrn();
				finalCharges = MachinePayDialog.getTotalCharges();
				payMode=MachinePayDialog.getPaymentMode();
				boolean result=MachinePayDialog.getFinalStatus();
				if(!result) {
					System.out.println("{"+result+ ", "+finalCharges+", "+req_urn+", "+payMode+"} Rejected Tnx ");
					btnNewButton_3.setEnabled(true);
					return;
				}
			}
			if(alreadyPaid && totalPaidPayment!=finalPrice) {
				JOptionPane.showMessageDialog(null, "Incorrect amount. Please try again. \n Accepted Amt: "+totalPaidPayment+" and Your Amt: "+finalPrice+"",
						"Input Error", JOptionPane.ERROR_MESSAGE);
				return;
			}
			long timeInMillis = System.currentTimeMillis();
			Calendar cal1 = Calendar.getInstance();
			cal1.setTimeInMillis(timeInMillis);
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

			data[0] = p_id;
			data[1] = p_name;
			data[2] = opd_doctorname;
			data[3] = opd_doctorid;
			data[4] = opd_date;
			data[5] = opd_symptom;
			data[6] = "";
			data[7] = opd_type;
			data[8] = opd_Attendence_type;
			data[9] = opd_refferal;
			data[10] = finalCharges+"";
			data[11] = "" + ReceptionMain.receptionNameSTR;
			data[12] = "" + timeFormat.format(cal1.getTime());
			data[13]=p_insurancetype;
			data[14]=p_insurancetype;
			data[15]=payMode;
			data[16]=req_urn;
			try {
				opddbConnection = new OPDDBConnection();
				int opd_no = opddbConnection.inserData(data);
				opddbConnection.closeConnection();
				opddbConnection = new OPDDBConnection();
				opd_token = opddbConnection.retrieveCounterTodayTokenDoctorWise(opd_doctorname,opd_no) + "";
				opddbConnection.UpdateOPDToken(opd_token,opd_no);
				opddbConnection.closeConnection();


				if (opdBrowser != null)
					opdBrowser.populateTable1(
							DateFormatChange.StringToMysqlDate(new Date()),
							DateFormatChange.StringToMysqlDate(new Date()));

				insertFreeTestData(opd_no + "");
				if (p_insurancetype.equals("Unknown")) {
					new OPDSlipNewFormat(opd_no+"",true,"",true);
				} else {
					new OPDSlipNewFormat(opd_no+"",true,"",true);
				}


				if (paymentModeCB.getSelectedIndex() > 1) {
					PaymentDBConnection dbConnection = new PaymentDBConnection();

					double roundOff = Math.round((finalCharges - finalPrice) * 100.0) / 100.0;
					data[0] = payMode;
					data[1] = "OPD";
					data[2] = opd_no + "";
					data[3] = p_id;
					data[4] = p_name;
					data[5] = finalPrice + "";
					data[6] = roundOff + "";
					data[7] = finalCharges+"";
					data[8] = opd_date;
					data[9] = "" + timeFormat.format(cal1.getTime());
					data[10] = "" + ReceptionMain.receptionNameSTR;
					data[11] = req_urn;
					dbConnection.inserData(data);
					dbConnection.closeConnection();
				}

				dispose();
				String[] options = {"Yes", "No"};
				int x = JOptionPane.showOptionDialog(null, "Do you want generate another OPD",
						"New OPD",
						JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[1]);
				System.out.println(x);
				dispose();
				if(x==0)
				{
					OPDEntery opdEntery=new OPDEntery(opdBrowser);
					opdEntery.searchAutoFill(p_id);
					opdEntery.setVisible(true);

				}
				new TokenSlipPrint(opd_no + "", doctorRoomLB.getText());
			} catch (Exception e) {
				e.printStackTrace();
				dispose();
			}
		}



	}

	public void getPatientsID(String index) {
		lastOPDDateLB.setText("Last OPD : ");
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
				isCashless=resultSet.getBoolean(12);
				if(!resultSet.getBoolean(12)) {
					lblCashless.setText("Cash Patient");
					lblCashless.setForeground(Color.RED);
				}else {
					lblCashless.setText("Cashless Patient");
					lblCashless.setForeground(Color.BLACK);
					paymentModeCB.setSelectedItem("paymentModes");
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		chckbxNewAttendance.setSelected(false);
		chckbxReattendance.setSelected(true);

		patientDBConnection.closeConnection();
		getAllFreeTest();
	}

	public void getAllPaymentModes() {
		paymentModes.addElement("Select Payment Mode");
		paymentModes.addElement("CASH");
		paymentCharges.add(0);
		paymentCharges.add(1);
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
	private void getStringCB(String str) {
		// TODO Auto-generated method stub
		doctors.removeAllElements();
		doctors.addElement("Select Doctor");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.searchDocter(str);
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorNameCB.setModel(doctors);
		if(doctors.getSize()>1 && str!="")
		{
			doctorNameCB.setSelectedIndex(1);
		}else {
			doctorNameCB.setSelectedIndex(0);
		}
	}
	public void getAllOPDType(String srch) {
		opdTypeCBM.removeAllElements();
		opdPriceAL.clear();
		opdTypeCBM.addElement("Select OPD Type");
		opdPriceAL.add("0");
		PriceMasterDBConnection priceMasterDBConnection = new PriceMasterDBConnection();
		ResultSet resultSet = priceMasterDBConnection.retrieveDataWithCategorySrch(
				"00", p_insurancetype,opd_doctorid,srch);

		System.out.println("opddo"+opd_doctorid);
		try {
			while (resultSet.next()) {
				opdTypeCBM.addElement(resultSet.getObject(2).toString());
				opdPriceAL.add(resultSet.getObject(3).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		priceMasterDBConnection.closeConnection();
		opdTypeCB.setModel(opdTypeCBM);
		if(opdTypeCBM.getSize()>1 && srch!="")
		{
			opdTypeCB.setSelectedIndex(1);	
		}else
		{
			opdTypeCB.setSelectedIndex(0);
		}

	}

	public void getPrice(String price) {
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		String[] values = new String[2];
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(p_insurancetype);

		int intPrice = Integer.parseInt(price);
		int intPercentage = (intPrice * Integer.parseInt(values[0])) / 100;
		finalPrice = intPrice - intPercentage;
		OPDChargesLB.setText("`" + finalPrice + "");
		OPDChargesLB.setFont(customFont);
		opd_charge = finalPrice + "";
		insuranceDBConnection.closeConnection();
	}

	public void getDoctorDetail(String name) {
		opd_doctorid = "";
		doctorSpecializationLB.setText("");
		doctorRoomLB.setText("");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				opd_doctorid = resultSet.getObject(1).toString();
				doctorSpecializationLB.setText(resultSet.getObject(2)
						.toString());
				doctorRoomLB.setText(resultSet.getObject(3).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("doctor_id"+opd_doctorid);
		dbConnection.closeConnection();
	}

	public void getAllFreeTest() {

		freeTestID.clear();
		freeTestName.clear();
		freeTestSortName.clear();
		freeTestUnits.clear();
		freeTestList.removeAll();
		FreeTestDBConnection freeTestDBConnection = new FreeTestDBConnection();
		ResultSet resultSet = freeTestDBConnection
				.retrieveAllFreeTest(opd_date);
		try {
			while (resultSet.next()) {

				if (checkFreeTestEligibilty(resultSet.getObject(1).toString(),
						resultSet.getObject(5).toString())) {
					freeTestID.add(resultSet.getObject(1).toString());
					freeTestName.add(resultSet.getObject(2).toString());
					freeTestSortName.add(resultSet.getObject(3).toString());
					freeTestUnits.add(resultSet.getObject(4).toString());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		freeTestDBConnection.closeConnection();

		freeTestList.setListData(freeTestName.toArray());

	}
	public void setAlreadyPayData(String reqUrn,double finalAmt,double WithoutChargeAmt,String PayMode,String pid,int payIndex) {
		alreadyPaid=true;
		req_urn=reqUrn;
		finalCharges=finalAmt;
		totalPaidPayment=WithoutChargeAmt;
		payMode=PayMode;
		paymentModeCB.setSelectedIndex(payIndex);
		paymentModeCB.setEnabled(false);
		searchPatientTB.setText(pid);
		searchPatientTB.setEnabled(false);
		OPDChargesLB_1.setText(""+WithoutChargeAmt);
	}
	public boolean checkFreeTestEligibilty(String test_id, String date) {
		FreeTestDBConnection freeTestDBConnection = new FreeTestDBConnection();
		ResultSet resultSet = freeTestDBConnection.checkEligibilty(p_id,
				test_id, date);
		int count = 0;
		try {
			while (resultSet.next()) {
				count = Integer.parseInt(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		freeTestDBConnection.closeConnection();
		if (count > 0) {
			return false;
		} else {
			return true;
		}

	}

	public boolean isDublicateOPD() {
		OPDDBConnection OPDDBConnection = new OPDDBConnection();
		ResultSet resultSet = OPDDBConnection.checkIfDublicateOpd(p_id,opd_type,opd_doctorid);
		try {
			while (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		OPDDBConnection.closeConnection();
			return false;
	}

	public void insertFreeTestData(String opd_id) {
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		FreeTestDBConnection connection = new FreeTestDBConnection();

		for (int i = 0; i < freeTestID.size(); i++) {

			data[0] = freeTestID.get(i).toString();
			data[1] = freeTestName.get(i).toString();
			data[2] = freeTestSortName.get(i).toString();
			data[3] = opd_id;
			data[4] = p_id;
			data[5] = p_name;
			data[6] = opd_date;
			data[7] = "" + timeFormat.format(cal1.getTime());
			data[8] = "" + ReceptionMain.receptionNameSTR;
			data[9] = freeTestUnits.get(i).toString();
			try {

				connection.inserData(data);

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			connection.closeConnection();
		}
	}
}
