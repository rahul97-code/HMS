package hms.exams.gui;

import hms.ConnectionFile.ConnectionFile;
import hms.DataTransfer.DataTransfer;
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
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.database.ReceptionistDBConnection;
import hms.reception.gui.ReceptionMain;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
import com.toedter.calendar.JDateChooser;

public class IPDExamEntery extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField searchIPDET;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	JLabel orderNoTF;
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
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String exam_doctorname = "", exam_date = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "",lis_code="";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id,
			package_id = "N/A",exam_time;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	final DefaultComboBoxModel examSubCat = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examCategory = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examlisCode = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> examPageNumberVector = new Vector<String>();
	Vector<String> examRoomVector = new Vector<String>();
	Vector<String> TodayExistExams = new Vector<String>();
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
	private JLabel examChargesLB;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	public static Font customFont;
	int ExamMaterTableIndex = 0;
	int discount = 0;
	String doctor_id = "0";
	private Timer timer;

	int totalExams = 999,i=0;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField ipdNoTB;
	private JTable addTestTable_2;
	private JTextField textField_1;
	private boolean can_add_multiple=false,AprovelAccess=false;
	private String mainDir=new ConnectionFile().getMainDirectory();
	private JDateChooser billDateCB;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] strings) {
		new IPDExamEntery(null).setVisible(true);
	}

	public IPDExamEntery(final LIS_System obj) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDExamEntery.class.getResource("/icons/rotaryLogo.png")));
		setTitle("IPD New Exam Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(110, 50, 1104, 568);
		setModal(true);
		getAprovelAcess();
	
		try {
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

		contentPane.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "New Exam Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));


		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Search and Select", TitledBorder.RIGHT, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_6.setBounds(15, 23, 326, 128);
		contentPane.add(panel_6);
		panel_6.setLayout(null);
		// examdbConnection = new ExamDBConnection();
		// exam_counter = examdbConnection.retrieveCounterData() + "";
		// examdbConnection.closeConnection();
		JLabel lblAttendenceDate = new JLabel("Enter IPD No.");
		lblAttendenceDate.setBounds(6, 21, 100, 14);
		panel_6.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 52, 108, 14);
		panel_6.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB = new JTextField();
		searchPatientTB.setBounds(111, 47, 172, 25);
		panel_6.add(searchPatientTB);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		JButton searchBT = new JButton("");
		searchBT.setBounds(291, 47, 28, 25);
		panel_6.add(searchBT);
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
					examChargesVector.clear();
					examIdVector.clear();

					examPageNumberVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});
		searchBT.setIcon(new ImageIcon(IPDExamEntery.class
				.getResource("/icons/zoom_r_button.png")));
		searchIPDET = new JTextField();
		searchIPDET.setBounds(108, 16, 212, 25);
		panel_6.add(searchIPDET);
		searchIPDET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchIPDET.setColumns(10);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(7, 83, 77, 20);
		panel_6.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientIDCB = new JComboBox(patientIDWithaName);
		patientIDCB.setBounds(113, 81, 206, 25);
		panel_6.add(patientIDCB);
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
				ipdNoTB.setText("");
				ipdBuildingTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");
				bedDaysTB.setText("");
				ipdDoctorTB.setText("");
				ipdDoctorTB.setText("");
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
					examName.clear();
					examlisCode.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					ExamMaterTableIndex = Integer.parseInt(getInsuranceIndex());

					getAllExamList();
					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchIPDET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchIPDET.getText() + "";
				if (!str.equals("")) {
					ipdSearch(str);
				} else {
					searchPatientTB.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchIPDET.getText() + "";
				if (!str.equals("")) {
					ipdSearch(str);
				} else {
					searchPatientTB.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchIPDET.getText() + "";
				if (!str.equals("")) {
					ipdSearch(str);
				} else {
					searchPatientTB.setText("");
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
		timer = new Timer(400, new ActionListener() {

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
					lastOPDDateLB.setText("Last OPD :");
					// subExamCB.setSelectedIndex(0);
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examlisCode.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();

					loadDataToTable();

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
					examChargesVector.clear();
					examIdVector.clear();
					examPageNumberVector.clear();
					examRoomVector.clear();
					ipdNoTB.setText("");
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					ipdDoctorTB.setText("");
					loadDataToTable();
				}
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(18, 166, 324, 287);
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
		lastOPDDateLB.setBounds(6, 255, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(672, 242, 420, 211);
		contentPane.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		final JButton btnRemove = new JButton("Remove");
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
				examChargesVector.remove(cur_selectedRow);
				examIdVector.remove(cur_selectedRow);
				examPageNumberVector.remove(cur_selectedRow);
				examRoomVector.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(109, 12, 206, 25);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(138, 412, 95, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 45, 396, 154);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Exam Code", "Exams", "Price","Lis Code" }));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(250, 412, 89, 20);
		panel_2.add(lblTotalcharges);

		JButton btnNewButton_3 = new JButton("Generate Reciept");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exam_note = "";

				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (ipdNoTB.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"This patient is not an indoor patient. ",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (exam_doctorname.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);

					return;
				} 
				if (examName.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add Exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} 

				if (exam_doctorname.contains("CIVIL")) {
					exam_date = calculateDate(exam_date);
				}		
				
				double finalCharges = totalCharges;

				String[] data1 = new String[11];
				data1[0] = "" + exam_name;
				data1[1] = "" + ipdDoctorTB.getText();
				data1[2] = "" + p_name;
				data1[3] = "" + finalCharges;
				data1[4] = "" + ReceptionMain.receptionNameSTR;
				data1[5] = "" + exam_date;
				data1[6] = "" + exam_time;
				data1[7] = "" ;
				data1[8] = "" ;
				data1[9] = "IPD Bill" ;
				data1[10]= null ;
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
				data[4] = exam_doctorname;
				data[5] = exam_date;
				data[7] = exam_note;
				data[9] = "" + ReceptionMain.receptionNameSTR;
				data[12] = "No";
				data[13]=""+index;
				data[14]=p_insurancetype+"";
				data[15]="IPD EXAM";
				int updatedInsertCounter = 0;
				for (int i = 0; i < ObjectArray_examname.length; i++) {
					data[0] = (String) ObjectArray_examname[i];
					data[1] = (String) ObjectArray_examnameid[i];
					data[6] = (String) ObjectArray_examcharges[i];
					data[8] = (String) ObjectArray_examroom[i];
					data[10] = (String) ObjectArray_examcategories[i];
					data[11] = (String) examPageNumberVector.get(i);
					data[16]=examlisCode.get(i);
					//data[17]=isApproved.get(i);
					try {

						updatedInsertCounter = examdbConnection
								.inserDataIPDExam(data);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				examdbConnection.closeConnection();
				System.out.println("Arun counter : "+updatedInsertCounter);
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

				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				String[] data2 = new String[17];
				for (int i = 0; i <  ObjectArray_examname.length; i++) {
					data2[0] = "" + ipd_id;
					data2[1] = "" +  ObjectArray_examname[i];
					data2[2] =  ""+ObjectArray_examname[i];
					data2[3] =""+ ObjectArray_examcharges[i];
					data2[4] = ""+ exam_date;
					data2[5] = "" + exam_time;
					data2[6] = (String) ObjectArray_examnameid[i];
					data2[7] = "" + p_id;
					data2[8] = "" + p_name;
					data2[9] = ""+ObjectArray_examcharges[i];
					data2[10] = "1";
					data2[11] = (String) ObjectArray_examcategories[i];
					data2[12] = ""+ReceptionMain.receptionNameSTR;
					data2[13] = "EXAM";
					data2[14] = (String) examPageNumberVector.get(i);
					data2[15]="D";
					data2[16]="exam_master"+InsuranceRatetype(p_insurancetype);
					try {
						db.inserDataTableName(data2);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db.closeConnection();
					}
				}
				db.closeConnection();

				IPDDBConnection ipddbConnection = new IPDDBConnection();
				try {
					ipddbConnection.updateAddAmount(ipd_id, finalCharges
							+ "");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					ipddbConnection.closeConnection();
					e.printStackTrace();
				}
				ipddbConnection.closeConnection();
				dispose();

				try {
					new ExamSlippdf(exam_counter + "",
							ObjectArray_examname, ObjectArray_examcharges,
							finalCharges + "", index, false,false,"",1.0);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				

			}
		});
		btnNewButton_3.setBounds(745, 475, 147, 44);
		contentPane.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(902, 475, 146, 44);
		contentPane.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IPDExamEntery.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));
		searchBT1.setFocusable(true);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(192, 460, 316, 59);
		contentPane.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 16, 92, 25);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameCB = new JComboBox<String>();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					exam_doctorname = doctorNameCB.getSelectedItem().toString();
					getDoctorDetail(exam_doctorname);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		doctorNameCB.setBounds(103, 18, 203, 25);
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

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,

				TitledBorder.TOP, null, null));
		panel_3.setBounds(344, 242, 316, 211);
		contentPane.add(panel_3);

		JLabel label_1 = new JLabel("Doctor Name :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 46, 108, 14);
		panel_3.add(label_1);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_3.add(ipdDoctorTB);

		JLabel label_2 = new JLabel("Building :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 77, 108, 14);
		panel_3.add(label_2);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_3.add(ipdBuildingTB);

		JLabel label_3 = new JLabel("Ward Name :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(10, 113, 108, 14);
		panel_3.add(label_3);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_3.add(ipdWardTB);

		JLabel label_4 = new JLabel("Bed No :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(10, 149, 108, 14);
		panel_3.add(label_4);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 144, 182, 25);
		panel_3.add(ipdBedNoTB);

		JLabel label_5 = new JLabel(" ");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(10, 180, 108, 14);
		panel_3.add(label_5);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 175, 182, 25);
		panel_3.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_3.add(ipdNoTB);

		JLabel label_6 = new JLabel("IPD No :");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_6.setBounds(10, 15, 108, 14);
		panel_3.add(label_6);

		JPanel panel_1_1 = new JPanel();
		panel_1_1.setFont(new Font("Dialog", Font.PLAIN, 10));
		panel_1_1.setLayout(null);
		panel_1_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Order Number", TitledBorder.LEADING, TitledBorder.TOP, null, Color.DARK_GRAY));
		panel_1_1.setBounds(15, 460, 165, 59);
		contentPane.add(panel_1_1);

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

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(358, 82, 734, 150);
		contentPane.add(scrollPane_1);
		addTestTable_2 = new JTable();
		addTestTable_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_2.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Exam Code", "Exam Cat", "Exams", "Price" }){@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
					return false;
				}});
		addTestTable_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
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

					int row=addTestTable_2.getSelectedRow();
					exam_nameid = addTestTable_2.getValueAt(row, 0).toString();
					getPrice("" + exam_nameid);
					if(!can_add_multiple && examIdVector.indexOf(exam_nameid)!=-1) {
						JOptionPane
						.showMessageDialog(
								null,
								"Exam Test Already Exist!",
								"Input Error", JOptionPane.ERROR_MESSAGE);

						return;
					} 
					if(TodayExistExams.indexOf(exam_nameid)!=-1) {
						int yes_or_no=JOptionPane
								.showConfirmDialog(
										null,
										"This Test has Already Done for Today, Do you want to ADD?",
										"Input Error", JOptionPane.YES_NO_OPTION);
						if(yes_or_no==JOptionPane.NO_OPTION) {
							return;
						}		
					}


					exam_name = addTestTable_2.getValueAt(row, 1).toString();
					examsub_catname = addTestTable_2.getValueAt(row, 2).toString();

				
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
		scrollPane_1.setViewportView(addTestTable_2);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(358, 23, 352, 50);
		contentPane.add(panel_5);
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Exam Detail :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12), null));

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);

		examNameCB = new JComboBox<String>();
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
		examNameCB.setBounds(6, 16, 334, 25);
		panel_5.add(examNameCB);
		JPanel panel_7 = new JPanel();
		panel_7.setBounds(745, 23, 347, 50);
		contentPane.add(panel_7);
		panel_7.setLayout(null);
		panel_7.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Exam Search :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12), null));


		textField_1 = new JTextField();
		textField_1.setBounds(108, 13, 227, 25);
		panel_7.add(textField_1);
		textField_1.setColumns(10);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBounds(521, 467, 188, 50);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		billDateCB = new JDateChooser();
		billDateCB.setDateFormatString("yyyy-MM-dd");
		billDateCB.setBounds(12, 12, 157, 25);
		panel_4.add(billDateCB);
		if(AprovelAccess)
			billDateCB.setEnabled(true);
		else
			billDateCB.setEnabled(false);
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
		
		textField_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = textField_1.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = textField_1.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = textField_1.getText() + "";
				searchTableContents(str);
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
		getAllDoctors();
	}

	private void getOrderNumber() {
		// TODO Auto-generated method stub
		ExamDBConnection db = new ExamDBConnection();
		i=1+Integer.parseInt(db.retrieveOrderNo());
		System.out.print(i+" arun");
		orderNoTF.setText(i+"");

	}

	public void searchAutoFill(String p_id) {
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
				p_age = p_age.split("-")[0];
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
		examdbConnection = new ExamDBConnection();
		//		String lastOPDDate = examdbConnection.retrieveLastExamPatient(indexId);
		//		lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		//		examdbConnection.closeConnection();
		patientDBConnection.closeConnection();
		getIPDDATA();
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

	public void searchTableContents(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
		}
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
	public void populateExamTable(String ExamCat) {

		try {
			ExamDBConnection dbConnection = new ExamDBConnection();
			ResultSet rs = dbConnection.GetAllExams(ExamMaterTableIndex,ExamCat,AprovelAccess);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
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
			addTestTable_2.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
							"Exam Code", "Exam Cat", "Exams", "Price"
					}
					) {@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}});
			rowSorter = new TableRowSorter<>(addTestTable_2.getModel());
			addTestTable_2.setRowSorter(rowSorter);
		}catch (SQLException ex) {

		}
	}
	public void getPrice(String exam_code) {

		String price = "";
		exam_pagenumber = "";
		exam_room="";
		lis_code="";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		String[] values = new String[2];
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(p_insurancetype);

		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.getExamSubCatRate(
				ExamMaterTableIndex, exam_code);
		try {
			while (resultSet.next()) {
				price = resultSet.getObject(1).toString();
				exam_pagenumber = resultSet.getObject(2).toString();
				exam_room=resultSet.getObject(3).toString();
				lis_code=resultSet.getObject(4).toString();
				can_add_multiple=resultSet.getBoolean(5);
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
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
			} else if (hours <= 5 && hours >= 0) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
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

	public void ipdSearch(String opdID) {
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithIPDId(opdID);
			while (rs.next()) {

				p_id = rs.getObject(1).toString();
				searchPatientTB.setText("" + p_id);
			}
			db.closeConnection();;
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

	public void getIPDDATA() {
		ResultSet resultSet=null;
		IPDDBConnection db = new IPDDBConnection();
		resultSet = db.retrieveAllDataPatientID(p_id);
		ipd_id="";
		try {
			while (resultSet.next()) {
				ipd_id = resultSet.getObject(1).toString();
				ipdNoTB.setText("" + resultSet.getObject(2));
				ipdBuildingTB.setText("" + resultSet.getObject(3));
				ipdWardTB.setText("" + resultSet.getObject(4));
				ipdBedNoTB.setText("" + resultSet.getObject(5));
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				package_id = resultSet.getObject(10).toString();
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		getIPDDoctors(ipd_id);
		Today_Exams();
	}

	public void Today_Exams() {
		IPDExpensesDBConnection db = new IPDExpensesDBConnection();
		ResultSet rs=db.TodayExams(ipd_id);
		TodayExistExams.removeAllElements();
		try {
			while(rs.next()) {
				TodayExistExams.add(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
	public String InsuranceRatetype(String ins) {

		if(ins.equals("Unknown"))
			return "";
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet rs=dbConnection.InsuranceRateType(ins);
		String ratetype="";
		try {
			while(rs.next()) {
				ratetype= rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return "_"+ratetype;
	}
	public String makeDirectory(String pid) {
		try {
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient/" + pid
					+ "/PreApprovel/");
			if (!dir.exists()){
				dir.mkdirs();
			}
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainDir + "/HMS/Patient/" + pid + "/PreApprovel/";
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

		//		doctorNameCB.setSelectedItem(ipdDoctorTB.getText().toString());
	}
}