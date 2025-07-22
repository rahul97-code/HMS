package hms.exams.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.opd.gui.OPDEntery;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.gui.NewPatient;
import hms.patient.slippdf.ExamSlippdf;
import hms.patient.slippdf.FreeExamSlippdf;
import hms.payments.PaymentMain;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.database.ReceptionistDBConnection;
import hms.reception.gui.ReceptionMain;

import java.awt.BorderLayout;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.midi.SysexMessage;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RowFilter;
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
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.DocumentException;

import LIS_System.LIS_Booking;
import LIS_System.LIS_StatusChecking;
import LIS_UI.LIS_System;

import javax.swing.border.LineBorder;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import com.toedter.calendar.JDateChooser;

public class ExamEntery extends JDialog {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField searchOPDET;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	JLabel orderNoTF;  
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[25];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	TableRowSorter<TableModel> rowSorter;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[] ObjectArray_examcategories;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0;
	String exam_pagenumber = "0";
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",lis_code="";
	String exam_doctorname = "",exam_performing_doc, exam_date = "",exam_time = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel prformdoctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	final DefaultComboBoxModel examSubCat = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examCategory = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examlisCode = new Vector<String>();
	Vector<String> iskaruna = new Vector<String>();
	Vector<String> isFreeUSG = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> examPageNumberVector = new Vector<String>();
	Vector<String> examRoomVector = new Vector<String>();
	Vector<Boolean> isFreeTest = new Vector<Boolean>();
	Vector examID = new Vector();
	Vector lisCodes = new Vector();
	Vector examRoom = new Vector();
	Vector examRate = new Vector();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	ExamDBConnection examdbConnection;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JComboBox examNameCB;
	private JTextArea examNote;
	private JTable addTestTable_1;
	public static Font customFont;
	int ExamMaterTableIndex = 0;
	int discount = 0;
	String doctor_id = "0";
	private Timer timer;
	private JComboBox paymentModeCB;
	int value=0;
	int totalExams = 999,i=0;
	private JLabel lblTotalcharges;
	private JTable table;
	private JTextField SearchExamTF;
	private boolean affordable =false;
	private JLabel lblNewLabel;
	protected String str="";
	double rate=1.0;
	private boolean can_add_multiple=false;
	private JComboBox<String> doctorNameCB_1;
	private boolean FreeUSG=false;
	double finalCharges =0,totalExamCharge=0;
	boolean alreadyPaid=false,AprovelAccess=false;
	String req_urn=null;
	String payMode;
	private JLabel lblTotalcharges_1;
	private JDateChooser billDateCB;
	private boolean is_free_test,isFreeTestSlip;


	public static void main(String r[]) {
		new ExamEntery().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public ExamEntery() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ExamEntery.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New Exam Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		//setResizable(false);
		setBounds(150, 70, 900, 655);
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
		getAprovelAcess();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(null);
		final JPanel contentPane = new JPanel();
		contentPane.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "New Exam Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		contentPane.setBounds(191, 12, 699, 601);
		getContentPane().add(contentPane, BorderLayout.CENTER);
		contentPane.setLayout(null);

		// examdbConnection = new ExamDBConnection();
		// exam_counter = examdbConnection.retrieveCounterData() + "";
		// examdbConnection.closeConnection();
		JLabel lblAttendenceDate = new JLabel("Enter OPD No.");
		lblAttendenceDate.setBounds(317, 27, 108, 14);
		contentPane.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(25, 27, 108, 14);
		contentPane.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
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
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);
					doctorNameCB.setSelectedIndex(0);
					doctorNameCB_1.setSelectedIndex(0);
					lastOPDDateLB.setText("Last OPD :");
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examlisCode.clear();
					examCategory.clear();
					iskaruna.clear();
					isFreeUSG.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					loadDataToTable();

				}
			}
		});
		searchPatientTB =  new JTextField();
		searchPatientTB.setBounds(138, 22, 138, 25);
		searchPatientTB.setToolTipText("Search Patient");
		contentPane.add(searchPatientTB);
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

		JButton searchBT = new JButton("");
		searchBT.setBounds(277, 22, 28, 25);
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
					examName.clear();
					examlisCode.clear();
					examCategory.clear();
					iskaruna.clear();
					isFreeUSG.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});
		contentPane.add(searchBT);
		searchBT.setIcon(new ImageIcon(ExamEntery.class
				.getResource("/icons/zoom_r_button.png")));
		searchOPDET = new JTextField();
		searchOPDET.setBounds(412, 22, 138, 25); 
		contentPane.add(searchOPDET);
		searchOPDET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchOPDET.setColumns(10);

		searchOPDET.getDocument().addDocumentListener(new DocumentListener() {


			@Override
			public void insertUpdate(DocumentEvent e) {
				str = searchOPDET.getText() + "";
				if (!str.equals("")) {
					opd(str);
				} else {
					searchPatientTB.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchOPDET.getText() + "";
				if (!str.equals("")) {
					opd(str);
				} else {
					searchPatientTB.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchOPDET.getText() + "";
				if (!str.equals("")) {
					opd(str);
				} else {
					searchPatientTB.setText("");
				}
			}
		});

		JButton searchBT1 = new JButton("");
		searchBT1.setToolTipText("Search");
		searchBT1.addActionListener(new ActionListener() {
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
					examName.clear();
					examlisCode.clear();
					examCategory.clear();
					iskaruna.clear();
					isFreeUSG.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(562, 102, 313, 343);
		contentPane.add(panel);
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

		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(4, 256, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setLayout(null);
		panel_1_1.setFont(new Font("Dialog", Font.PLAIN, 10));
		panel_1_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Order Number", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panel_1_1.setBounds(16, 277, 165, 54);
		panel.add(panel_1_1);

		orderNoTF = new JLabel("");
		orderNoTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		orderNoTF.setBounds(6, 22, 147, 25);
		panel_1_1.add(orderNoTF);
		getOrderNumber();
		JLabel lblDoctorRoom_1 = new JLabel("Doctor Room :");
		lblDoctorRoom_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblDoctorRoom_1.setBounds(6, 52, 108, 25);
		panel_1_1.add(lblDoctorRoom_1);

		JLabel lblNewLabel_4_1 = new JLabel("Doctor specialization :");
		lblNewLabel_4_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNewLabel_4_1.setBounds(6, 90, 141, 24);
		panel_1_1.add(lblNewLabel_4_1);

		JLabel doctorRoomLB_1 = new JLabel("");
		doctorRoomLB_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		doctorRoomLB_1.setBounds(145, 54, 186, 23);
		panel_1_1.add(doctorRoomLB_1);

		JLabel doctorSpecializationLB_1 = new JLabel("");
		doctorSpecializationLB_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		doctorSpecializationLB_1.setBounds(145, 89, 186, 25);
		panel_1_1.add(doctorSpecializationLB_1);

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Karun Sparsh", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(193, 277, 115, 55);
		panel.add(panel_2);
		panel_2.setLayout(null);

		lblNewLabel = new JLabel("");
		lblNewLabel.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 26, 60, 15);
		panel_2.add(lblNewLabel);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(562, 22, 77, 20);
		contentPane.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));


		contentPane.setBounds(213, 95, 339, 478);

		contentPane.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 53, 253, 59);
		contentPane.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setLayout(null);

		doctorNameCB = new JComboBox<String>();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					exam_doctorname = doctorNameCB.getSelectedItem().toString();
					//					doctorNameCB_1.setSelectedIndex(doctorNameCB.getSelectedIndex());
					getDoctorDetail(exam_doctorname);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		JPanel panel_1_2 = new JPanel();
		panel_1_2.setLayout(null);
		panel_1_2.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Doctor Performing",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12), null));
		panel_1_2.setBounds(287, 53, 263, 59);
		contentPane.add(panel_1_2);

		doctorNameCB_1 = new JComboBox<String>();
		doctorNameCB_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				exam_performing_doc = doctorNameCB_1.getSelectedItem().toString();
			}
		});
		doctorNameCB_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		doctorNameCB_1.setBounds(29, 22, 207, 25);
		panel_1_2.add(doctorNameCB_1);
		getAllDoctors();
		doctorNameCB.setBounds(12, 22, 217, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDoctorRoom = new JLabel("Doctor Room :");
		lblDoctorRoom.setBounds(6, 52, 108, 25);
		panel_1.add(lblDoctorRoom);
		lblDoctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNewLabel_4 = new JLabel("Doctor specialization :");
		lblNewLabel_4.setBounds(6, 90, 141, 24);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorRoomLB = new JLabel("");
		doctorRoomLB.setBounds(145, 54, 186, 23);
		panel_1.add(doctorRoomLB);
		doctorRoomLB.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorSpecializationLB = new JLabel("");
		doctorSpecializationLB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorSpecializationLB.setBounds(145, 89, 186, 25);
		panel_1.add(doctorSpecializationLB);


		final JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(70, 373, 253, 25);
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = addTestTable_1.getSelectedRow();
				cur_selectedRow = addTestTable_1
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = addTestTable_1.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				itemsHashMap.remove(toDelete);
				examHashMap.remove(toDelete);
				examName.remove(cur_selectedRow);
				examlisCode.remove(cur_selectedRow);
				examCategory.remove(cur_selectedRow);
				iskaruna.remove(cur_selectedRow);
				isFreeUSG.remove(cur_selectedRow);
				examChargesVector.remove(cur_selectedRow);
				examIdVector.remove(cur_selectedRow);
				examPageNumberVector.remove(cur_selectedRow);
				examRoomVector.remove(cur_selectedRow);
				isFreeTest.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		contentPane.add(btnRemove);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 410, 538, 180);
		contentPane.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Exam Code", "Exams", "Price","Lis Code"}));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		final JButton btnNewButton_3 = new JButton("Generate Reciept");
		btnNewButton_3.setBounds(562, 539, 147, 44);
		btnNewButton_3.addActionListener(new ActionListener() {


			@Override
			public void actionPerformed(ActionEvent arg0) {
				exam_note = examNote.getText();
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (exam_doctorname.equals("Select Doctor") || exam_performing_doc.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
				else if (paymentModeCB.getSelectedItem().toString().equals("Select Payment Mode")) {
					JOptionPane.showMessageDialog(null, "Please select Payment Mode",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (examName.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add Exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					if (exam_doctorname.contains("CIVIL")) {
						exam_date = calculateDate(exam_date);
					}
					int payModeIndex=paymentModeCB.getSelectedIndex();

					if((!p_insurancetype.equals("Unknown") && payModeIndex==1) || (p_insurancetype.equals("Unknown") && payModeIndex==3)){

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
						finalCharges=totalCharges;
					}
					if(!alreadyPaid && (payModeIndex!=1 && payModeIndex!=3)) {	
						btnNewButton_3.setEnabled(false);
						PaymentMain MachinePayDialog=new PaymentMain(new String[]{totalCharges+"",p_id,p_name,ReceptionMain.receptionNameSTR,exam_doctorname},"EXAM");
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
					if(alreadyPaid && totalExamCharge!=totalCharges) {
						JOptionPane.showMessageDialog(null, "Incorrect amount. Please try again. \n Accepted Amt: "+totalExamCharge+" and Your Amt: "+totalCharges+"",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					String desc=affordable?examdesc(ObjectArray_examcategories[0]+""):null;

					String[] data1 = new String[12];
					data1[0] = "" + exam_name;
					data1[1] = "" + exam_doctorname;
					data1[2] = "" + p_name;
					data1[3] = "" + finalCharges;
					data1[4] = "" + ReceptionMain.receptionNameSTR;
					data1[5] = "" + exam_date;
					data1[6] = "" + exam_time;
					data1[7] = "" ;
					data1[8] = "" ;
					data1[9]=payMode;
					data1[10] = req_urn ;
					int index = 0;
					AmountReceiptDBConnection amountReceiptDBConnection = new AmountReceiptDBConnection();

					try {
						index = amountReceiptDBConnection.inserDataExam(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					amountReceiptDBConnection.closeConnection();

					examdbConnection = new ExamDBConnection();
					data[2] = p_id;
					data[3] = p_name;
					data[4] = exam_performing_doc;
					data[5] = exam_date;
					data[7] = exam_note;
					data[9] = "" + ReceptionMain.receptionNameSTR;
					data[12] = "Yes";
					data[13] = ""+index;
					data[14]=p_insurancetype+"";
					data[15]=payMode;

					int updatedInsertCounter = 0;
					for (int i = 0; i < ObjectArray_examname.length; i++) {
						data[0] = (String) ObjectArray_examname[i];
						data[1] = (String) ObjectArray_examnameid[i];
						data[6] = (String) ObjectArray_examcharges[i]; 
						data[8] = (String) ObjectArray_examroom[i];
						data[10] = (String) ObjectArray_examcategories[i];
						data[11] = (String) examPageNumberVector.get(i);
						data[16] = examlisCode.get(i);
						data[17] = iskaruna.get(i);
						data[18] = rate+"";
						data[19] = isFreeUSG.get(i)+"";
						try {

							updatedInsertCounter = examdbConnection.inserDatakaruna(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					examdbConnection.closeConnection();
					exam_counter = (updatedInsertCounter - (ObjectArray_examname.length - 1))
							+ "";
					int lastRef = Integer.parseInt(exam_counter)
							+ ObjectArray_examname.length - 1;
					AmountReceiptDBConnection amountReceiptDBConnection1 = new AmountReceiptDBConnection();

					try {
						amountReceiptDBConnection1.UpdateDataExam(exam_counter,lastRef,index);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace(); 
					}
					amountReceiptDBConnection.closeConnection();

					

					if (payModeIndex>1) { 

						PaymentDBConnection dbConnection = new PaymentDBConnection();
						double roundOff = Math.round((finalCharges - totalCharges) * 100.0) / 100.0;
						data[0] = payMode;
						data[1] = "EXAM";
						data[2] = index + "";
						data[3] = p_id;
						data[4] = p_name;
						data[5] = totalCharges + "";
						data[6] = roundOff + "";
						data[7] = finalCharges + "";
						data[8] = exam_date;
						data[9] = exam_time;
						data[10] = "" + ReceptionMain.receptionNameSTR;
						data[11] =  req_urn;
						try {
							dbConnection.inserData(data);
							dbConnection.closeConnection();

						} catch (Exception e) {
							// TODO: handle exception
							dbConnection.closeConnection();

						}
					}
					dispose();
					try {
						if(isFreeTest.indexOf(true)!=-1) {
							new FreeExamSlippdf(exam_counter + "",
									ObjectArray_examname, ObjectArray_examcharges,
									finalCharges + "", index, true,affordable,desc,rate);
						}else {
							new ExamSlippdf(exam_counter + "",
									ObjectArray_examname, ObjectArray_examcharges,
									finalCharges + "", index, true,affordable,desc,rate);
						}
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			private String examdesc(String objectArray_examcategories) {
				// TODO Auto-generated method stub
				String desc = null;
				ExamDBConnection db=new ExamDBConnection();
				ResultSet rs=db.retrieveExamkarunadesc(objectArray_examcategories);
				try {
					while(rs.next()) {
						desc = rs.getObject(1).toString();
						rate=rs.getDouble(2);
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				return desc;
			}
		});
		contentPane.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Dialog", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.setBounds(721, 539, 146, 44);
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		contentPane.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(ExamEntery.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		patientIDCB = new JComboBox(patientIDWithaName);
		patientIDCB.setBounds(649, 20, 164, 25);
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
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examCategory.clear();
					iskaruna.clear();
					examlisCode.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					ExamMaterTableIndex = Integer.parseInt(getInsuranceIndex());

					getAllExamList();
					populateExamTable("");
					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(patientIDCB);
		searchBT1.setFocusable(true);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(572, 446, 313, 92);
		contentPane.add(panel_3);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Note :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_3.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 297, 71);
		panel_3.add(scrollPane_1);

		examNote = new JTextArea();
		examNote.setLineWrap(true);
		examNote.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNote.setRows(10);
		scrollPane_1.setViewportView(examNote);

		JButton btnNewButton = new JButton("");
		btnNewButton.setBounds(825, 20, 50, 25);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				NewPatient newPatient = new NewPatient();
				newPatient.ExamEntryObject(ExamEntery.this);
				newPatient
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				newPatient.setLocationRelativeTo(contentPane);
				newPatient.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(ExamEntery.class
				.getResource("/icons/plus_button.png")));
		contentPane.add(btnNewButton);

		JLabel label = new JLabel("Payment Mode :");
		label.setBounds(572, 65, 92, 25);
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(label);

		paymentModeCB = new JComboBox();
		paymentModeCB.setBounds(674, 65, 206, 25);
		paymentModeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getAllPaymentModes();
		paymentModeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		contentPane.add(paymentModeCB);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTotalcharges.setBounds(397, 595, 94, 20);
		contentPane.add(lblTotalcharges);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTotalCharges.setBounds(296, 595, 106, 23);
		contentPane.add(lblTotalCharges);

		JScrollPane scrollPane_2 = new JScrollPane();

		scrollPane_2.setBounds(12, 161, 538, 200);
		contentPane.add(scrollPane_2);

		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Exam Code", "Exam Cat", "Exams", "Price"
				}
				) {@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}});
		table.getColumnModel().getColumn(0).setPreferredWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(3).setPreferredWidth(45);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					isFreeTestSlip=false;
					System.out.println("clicked");
					if (patientNameTB.getText().equals("")) {
						JOptionPane
						.showMessageDialog(
								null,
								"Please select the patient first and then exam type for set prices",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					if (doctorNameCB.getSelectedIndex() == 0) {
						JOptionPane
						.showMessageDialog(
								null,
								"Please select the doctor first and then exam type for set prices",
								"Input Error", JOptionPane.ERROR_MESSAGE);

						return;
					}
					if (doctorNameCB.getSelectedIndex() == 0) {
						JOptionPane
						.showMessageDialog(
								null,
								"Please select the doctor first and then exam type for set prices",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}

					int row=table.getSelectedRow();
					exam_nameid = table.getValueAt(row, 0).toString();
					exam_name = table.getValueAt(row, 1).toString();
					examsub_catname = table.getValueAt(row, 2).toString();

					getPrice("" + exam_nameid);
					if (is_free_test) {
						String[] arr = getFreeCampTestData(exam_nameid);
						if (arr != null) {
							int age=Integer.parseInt(arr[4]);
							if (Integer.parseInt(p_age)<=age) {
								JOptionPane.showMessageDialog(null,
										"Patient age should be greater than "+age+" years", "Age Validation",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							String[] res = getPatientLastTestData(exam_nameid, arr[1], arr[0]);
							if (res[0].equals("0")) {
								JOptionPane.showMessageDialog(null,
										"You can't do this test again. Try it in the next cycle.", "Input Error",
										JOptionPane.ERROR_MESSAGE);
								return;
							}
							if (res[1].equals("0")) {
								JOptionPane.showMessageDialog(null,
										"The limit for this test has been exceeded." + " Please try again tomorrow.",
										"Limit Exceeded", JOptionPane.INFORMATION_MESSAGE);
								return;
							}
							if (arr[3].equals("1") && res[2].equals("0")) {
								JOptionPane.showMessageDialog(null, "Please register your Aadhaar first",
										"Registration Required", JOptionPane.WARNING_MESSAGE);
								return;
							}
						}else {
							JOptionPane.showMessageDialog(null, "Please define test rules first",
									"Rules Not Found!", JOptionPane.WARNING_MESSAGE);
							return;
						}
						isFreeTestSlip=true;
					}
					if(!can_add_multiple && examIdVector.indexOf(exam_nameid)!=-1) {
						JOptionPane
						.showMessageDialog(
								null,
								"Exam Test Already Exist!",
								"Input Error", JOptionPane.ERROR_MESSAGE);

						return;
					}


					if(insuranceTypeTB.getText().toString().equals("Unknown") && affordable && (exam_name.contains("LAB") || exam_name.contains("X-RAY") || exam_name.contains("CT SCAN") || exam_name.contains("MRI"))) {
						exam_charge=(Double.parseDouble(exam_charge)/2)+"";
						System.out.println(exam_charge);
						iskaruna.add("1");
					}
					else {
						iskaruna.add("0");
					}
					if(insuranceTypeTB.getText().toString().equals("Unknown") && FreeUSG && exam_name.contains("USG")) {
						exam_charge=0.0+"";
						isFreeUSG.add("1");
					}
					else
					{
						isFreeUSG.add("0");
					}
					System.out.println(exam_charge);
					isFreeTest.add(isFreeTestSlip);
					examCategory.add(exam_name);
					examName.add(exam_name + " " + examsub_catname);
					examlisCode.add(lis_code);
					examChargesVector.add(exam_charge);
					examIdVector.add(exam_nameid);
					examPageNumberVector.add(exam_pagenumber);
					examRoomVector.add(exam_room);
					itemsHashMap.put(exam_name + " " + examsub_catname,
							exam_charge);
					examHashMap.put(exam_nameid, exam_room);
					loadDataToTable();

				}
			}
		});
		scrollPane_2.setViewportView(table);

		examNameCB = new JComboBox<String>();
		examNameCB.setBounds(12, 124, 228, 25);
		contentPane.add(examNameCB);
		examNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					exam_name = examNameCB.getSelectedItem().toString();
					if (!exam_name.equals("Select Exam")) {

						populateExamTable(exam_name);
					} else {
						examSubCat.removeAllElements();
						examSubCat.addElement("Select Category");
						populateExamTable("");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		examNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		SearchExamTF = new JTextField();
		SearchExamTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		SearchExamTF.setColumns(10);
		SearchExamTF.setBounds(353, 124, 180, 25);
		contentPane.add(SearchExamTF);
		SearchExamTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = SearchExamTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = SearchExamTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = SearchExamTF.getText() + "";
				searchTableContents(str);
			}
		});

		JLabel lblAttendenceDate_1 = new JLabel("Search Exam :");
		lblAttendenceDate_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAttendenceDate_1.setBounds(258, 129, 108, 14);
		contentPane.add(lblAttendenceDate_1);

		JLabel lblTotalCharges_1 = new JLabel("Paid Amt :");
		lblTotalCharges_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTotalCharges_1.setBounds(45, 592, 106, 23);
		contentPane.add(lblTotalCharges_1);

		lblTotalcharges_1 = new JLabel("");
		lblTotalcharges_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTotalcharges_1.setBounds(146, 592, 94, 20);
		contentPane.add(lblTotalcharges_1);

		billDateCB = new JDateChooser();
		if(AprovelAccess)
			billDateCB.setEnabled(true);
		else
			billDateCB.setEnabled(false);
		billDateCB.setDateFormatString("yyyy-MM-dd");
		billDateCB.setBounds(393, 374, 157, 25);
		contentPane.add(billDateCB);
		GeneralDBConnection db=new GeneralDBConnection();
		exam_date = db.getCurrentDate();
		exam_time=db.getCurrentTime();
		db.closeConnection();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(exam_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		billDateCB.setDate(date);
		billDateCB.setMaxSelectableDate(date);
		billDateCB.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							exam_date = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
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
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				searchPatientTB.requestFocus();
			}
		});
	}
	public void searchAutoFill(String p_id)
	{
		searchPatientTB.setText(p_id);
		searchPatientTB.setEnabled(false);
	}
	public String getInsuranceIndex() {
		String query = "";
		String price = "";
		String[] values = new String[2];
		values[1] = "4";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(p_insurancetype);
		return values[1];
	}
	private void getOrderNumber() {
		// TODO Auto-generated method stub
		ExamDBConnection db = new ExamDBConnection();
		i=1+Integer.parseInt(db.retrieveOrderNo());
		System.out.print(i+"");
		orderNoTF.setText(i+"");

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
		System.out.println();
		System.out.println(str+"dddjdjdj");

		patientDBConnection.closeConnection();
		patientIDCB.setModel(patientIDWithaName);
	}

	public void setPatientDetail(String indexId) {
		affordable=false;
		FreeUSG=false;
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex(indexId);
		try {
			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_age = resultSet.getObject(2).toString();
				String[] arrOfStr = p_age.split("-");
				for (String a : arrOfStr)
				{
					p_age=a;
					break;
				}
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

		resultSet = patientDBConnection
				.searchkarunsparsh(indexId);
		try {
			while(resultSet.next()) {
				affordable=resultSet.getBoolean(1);
				FreeUSG=resultSet.getBoolean(2);
			}
			lblNewLabel.setText(affordable?"Yes":"No");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
	}

	public void getAllDoctors() {
		doctors.addElement("Select Doctor");
		prformdoctors.addElement("Select Doctor");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
				prformdoctors.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctors.addElement("Other");
		prformdoctors.addElement("Other");
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
		doctorNameCB_1.setModel(prformdoctors);
		doctorNameCB_1.setSelectedIndex(0);
	}

	public void getDoctorDetail(String name) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				doctor_id = resultSet.getInt(1) + "";
				discount = resultSet.getInt(4);
				totalExams = Integer.parseInt(resultSet.getString(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
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

	public void getAllExamList() {
		exams.removeAllElements();
		exams.addElement("Select Exam");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllExams(ExamMaterTableIndex);
		try {
			while (resultSet.next()) {
				exams.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		examNameCB.setModel(exams);
		examNameCB.setSelectedIndex(0);
	}



	public void getPrice(String exam_code) {

		String price = "";
		exam_pagenumber="";
		exam_room="";
		lis_code="";
		is_free_test=false;
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		String[] values = new String[2];
		values = insuranceDBConnection.retrieveDataWithInsuranceType(p_insurancetype);
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.getExamSubCatRate(ExamMaterTableIndex, exam_code);
		try {
			while (resultSet.next()) {
				price = resultSet.getObject(1).toString();
				exam_pagenumber = resultSet.getObject(2).toString();
				exam_room=resultSet.getObject(3).toString();
				lis_code=resultSet.getObject(4).toString();
				can_add_multiple=resultSet.getBoolean(5);
				is_free_test=resultSet.getBoolean(6);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		int intPrice = Integer.parseInt(price);
		int intPercentage = (intPrice * Integer.parseInt(values[0])) / 100;
		int finalPrice = intPrice - intPercentage;
		exam_charge = finalPrice + "";

		Calendar mytime = Calendar.getInstance();
		// calendar.setTime(yourdate);
		int hours = mytime.get(Calendar.HOUR_OF_DAY);
		int minutes = mytime.get(Calendar.MINUTE);
		int seconds = mytime.get(Calendar.SECOND);

		System.out.println(hours + "  " + minutes + "  " + seconds);
		if(insuranceTypeTB.getText().toString().equals("Unknown") && ((exam_name.equals("USG") || (exam_name.equals("MRI") || (exam_name.equals("CT SCAN"))))))
		{
			if (hours <= 24 && hours >= 21) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
			} else if (hours <= 5 && hours >= 0) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
			}
		}else {
			if (hours <= 24 && hours >= 21) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
			} else if (hours <= 5 && hours >= 0) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
			}
		}

		double charges = Double.parseDouble(exam_charge);
		double discountValue = (charges * discount) / 100;
		charges = charges - discountValue;
		exam_charge = "" + (int) charges;

	}

	public void opd(String opdID) {
		FreeUSG=false;
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId3(opdID);
			while (rs.next()) {
				p_id = rs.getObject(1).toString();
				searchPatientTB.setText("" + p_id);
				affordable  = rs.getBoolean(7);
				FreeUSG  = rs.getBoolean(8);
			}
			lblNewLabel.setText(affordable?"Yes":"No");

			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	private void loadDataToTable() {
		// get size of the hashmap
		//		int size = itemsHashMap.size();
		int size = examName.size();
		totalCharges = 0;
		System.out.print("Size"+size);
		// declare two arrays one for key and other for keyValues
		ObjectArray_examnameid = new Object[size];
		ObjectArray_examname = new Object[size];
		ObjectArray_examroom = new Object[size];
		ObjectArray_examcharges = new Object[size];
		ObjectArray_examcategories = new Object[size];
		ObjectArray_ListOfexams = new Object[size][4];

		for (int i = 0; i < examName.size(); i++) {
			ObjectArray_examcharges[i] = examChargesVector.get(i);
			ObjectArray_ListOfexams[i][0] =  examIdVector.get(i);
			ObjectArray_ListOfexams[i][1] = examName.get(i);
			ObjectArray_examname[i] = examName.get(i);
			ObjectArray_examroom[i] = examRoomVector.get(i);
			ObjectArray_examnameid[i] = examIdVector.get(i);
			ObjectArray_examcategories[i] = examCategory.get(i);
			ObjectArray_ListOfexams[i][2] =  examChargesVector.get(i);
			ObjectArray_ListOfexams[i][3] = examlisCode.get(i);
			totalCharges = totalCharges
					+ Double.parseDouble(ObjectArray_examcharges[i].toString()
							.trim());
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Exam Code","Exams", "Charges","LIS Code" }) {

			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		lblTotalcharges.setText("`" + totalCharges + "");
		lblTotalcharges.setFont(customFont);
	}
	public void populateExamTable(String ExamCat) {

		try {
			ExamDBConnection dbConnection = new ExamDBConnection();
			ResultSet rs = dbConnection
					.GetAllExams(ExamMaterTableIndex,ExamCat,AprovelAccess);
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			dbConnection.closeConnection();
			table.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
							"Exam Code", "Exam Cat", "Exams", "Price"
					}
					) {@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}});
			table.getColumnModel().getColumn(0).setPreferredWidth(50);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(180);
			table.getColumnModel().getColumn(3).setPreferredWidth(45);
			rowSorter = new TableRowSorter<>(table.getModel());
			table.setRowSorter(rowSorter);
		}catch (SQLException ex) {

		}
	}
	public void searchTableContents(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
		}
	}
	public String calculateDate(String exam_date) {

		String calculatedDate;
		ExamDBConnection connection = new ExamDBConnection();
		int counter = connection.examTotalLabWise(exam_name, exam_date);
		connection.closeConnection();

		DoctorDBConnection db = new DoctorDBConnection();
		int days = db.checkLeaves(doctor_id, exam_date);
		db.closeConnection();

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = null;
		try {
			date = sdf.parse(exam_date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		System.out.println("SUKHPAL " + days + "  " + counter);
		if (counter >= totalExams) {
			cal.add(Calendar.DATE, 1);
			calculatedDate = sdf.format(cal.getTime());
			calculatedDate = calculateDate(calculatedDate);
		} else if (days > 0) {
			cal.add(Calendar.DATE, days);
			calculatedDate = sdf.format(cal.getTime());
			calculatedDate = calculateDate(calculatedDate);
		} else if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			cal.add(Calendar.DATE, 1);
			calculatedDate = sdf.format(cal.getTime());
			calculatedDate = calculateDate(calculatedDate);
		} else {
			calculatedDate = exam_date;

		}
		return calculatedDate;
	}
	public void getAprovelAcess() {
		ReceptionistDBConnection db=new ReceptionistDBConnection();
		ResultSet rs=db.getExamAproveAcess(ReceptionMain.receptionIdSTR);
		try {
			while(rs.next()) {
				AprovelAccess= rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
	}
	public String[] getFreeCampTestData(String exam_code) {
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs=db.retrieveFreeCampTestData(exam_code,p_insurancetype);
		try {
			while(rs.next()) {
				return new String[]{rs.getString(1),rs.getString(2),rs.getString(3),rs.getString(4),rs.getString(5)};
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		return null;
	}

	public String[] getPatientLastTestData(String exam_code,String repeat_days,String test_count) {
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs=db.checkPatientFreeTestData(p_id,exam_code,repeat_days,test_count);
		try {
			while(rs.next()) {
				return new String[]{rs.getString(1),rs.getString(2),rs.getString(3)};
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		return null;
	}
	public void setAlreadyPayData(String reqUrn,double finalAmt,double WithoutChargeAmt,String PayMode,String pid,int payIndex) {
		alreadyPaid=true;
		req_urn=reqUrn;
		finalCharges=finalAmt;
		totalExamCharge=WithoutChargeAmt;
		lblTotalcharges_1.setText(WithoutChargeAmt+"");
		payMode=PayMode;
		paymentModeCB.setSelectedIndex(payIndex);
		paymentModeCB.setEnabled(false);
		searchPatientTB.setText(pid);
		searchPatientTB.setEnabled(false);
		searchOPDET.setEnabled(false);
	}
}