package hms1.ipd.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.database.PatientDBConnection;
import hms.patient.gui.NewPatient;
import hms.patient.slippdf.ExamSlippdf;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.gui.ReceptionMain;
import hms1.ipd.database.PackageDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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

import com.itextpdf.text.DocumentException;

public class PackageExamEntery extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[] ObjectArray_examcategories;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String exam_doctorname = "", exam_date = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	final DefaultComboBoxModel examSubCat = new DefaultComboBoxModel();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examCategory = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> examRoomVector = new Vector<String>();
	Vector examID = new Vector();
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
	private JLabel examRoomNoLB;
	private JTextArea examNote;
	private Timer timer;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	private JComboBox<String> subExamCB;
	public static Font customFont;
	int discount=0;
	String packageID;
	String doctorNameSTR;
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public PackageExamEntery(String patientIDSTR,String packageID,String doctorNameSTR) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				PackageExamEntery.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New Exam Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(150, 70, 727, 634);
		setModal(true);
		this.packageID=packageID;
		this.doctorNameSTR=doctorNameSTR;
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
				.getBorder("TitledBorder.border"), "New Exam Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 699, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(10, 66, 108, 14);
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
					lastOPDDateLB.setText("Last OPD :");
				//	subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});
		
		
		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(123, 61, 182, 25);
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
					patientID.removeAllElements();
					patientIDCB.setModel(patientID);
					lastOPDDateLB.setText("Last OPD :");
					subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});
		searchBT.setBounds(305, 60, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(PackageExamEntery.class
				.getResource("/icons/zoom_r_button.png")));
		exam_date = DateFormatChange.StringToMysqlDate(new Date());

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
					patientID.removeAllElements();
					patientIDCB.setModel(patientID);
					lastOPDDateLB.setText("Last OPD :");
					subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examRoomVector.clear();
					loadDataToTable();
				}
			}
		});

		JPanel panel = new JPanel();
		panel.setBounds(369, 92, 313, 314);
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

		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(6, 284, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(369, 61, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 97, 349, 482);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(5, 11, 337, 59);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
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
					exam_doctorname = doctorNameCB.getSelectedItem().toString();
					getDoctorDetail(exam_doctorname);
					subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
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

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Exam Detail :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));
		panel_5.setBounds(5, 71, 337, 151);
		panel_2.add(panel_5);

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

						getExamSubCatList(exam_name);

					} else {
						examChargesLB.setText("");
						examRoomNoLB.setText("");
						examSubCat.removeAllElements();
						examSubCat.addElement("Select Category");
						subExamCB.setModel(examSubCat);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		examNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNameCB.setBounds(6, 16, 321, 25);
		panel_5.add(examNameCB);
//		getAllExamList();

		JLabel lblExamRoom = new JLabel("Exam Room :");
		lblExamRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamRoom.setBounds(6, 90, 108, 25);
		panel_5.add(lblExamRoom);

		JLabel lblExamCharges = new JLabel("Exam Charges :");
		lblExamCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamCharges.setBounds(6, 115, 141, 24);
		panel_5.add(lblExamCharges);

		examRoomNoLB = new JLabel("");
		examRoomNoLB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		examRoomNoLB.setBounds(145, 92, 186, 23);
		panel_5.add(examRoomNoLB);

		examChargesLB = new JLabel("");
		examChargesLB.setFont(new Font("Mangal", Font.PLAIN, 14));
		examChargesLB.setBounds(145, 115, 186, 25);
		panel_5.add(examChargesLB);

		subExamCB = new JComboBox<String>();
		subExamCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					examsub_catname = subExamCB.getSelectedItem().toString();
					if (!examsub_catname.equals("Select Category")) {
						if (patientNameTB.getText().equals("")) {
							JOptionPane
									.showMessageDialog(
											null,
											"Please select the patient first and then exam type for set prices",
											"Input Error",
											JOptionPane.ERROR_MESSAGE);
							subExamCB.setSelectedIndex(0);
							examChargesLB.setText("");
							examRoomNoLB.setText("");
							return;
						}
						if (doctorNameCB.getSelectedIndex()==0) {
							JOptionPane
									.showMessageDialog(
											null,
											"Please select the doctor first and then exam type for set prices",
											"Input Error",
											JOptionPane.ERROR_MESSAGE);
							subExamCB.setSelectedIndex(0);
							examChargesLB.setText("");
							examRoomNoLB.setText("");
							return;
						}
						// getExamDetail(""
						// + examID.get(subExamCB.getSelectedIndex()));
						//

						exam_nameid = examID.get(subExamCB.getSelectedIndex())
								+ "";
						getPrice("" + examID.get(subExamCB.getSelectedIndex()));

						examRoomNoLB.setText(examRoom.get(subExamCB
								.getSelectedIndex()) + "");
						exam_room = (examRoom.get(subExamCB.getSelectedIndex()) + "");
						examChargesLB.setText(exam_charge);
						System.out.println(examID.get(subExamCB
								.getSelectedIndex())
								+ "   "
								+ exam_charge
								+ " " + exam_room);
					} else {
						examChargesLB.setText("");
						examRoomNoLB.setText("");
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		subExamCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		subExamCB.setBounds(6, 54, 321, 25);
		panel_5.add(subExamCB);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please select the patient first and then exam type for set prices",
									"Input Error",
									JOptionPane.ERROR_MESSAGE);
					subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					return;
				}
				if (doctorNameCB.getSelectedIndex()==0) {
					JOptionPane
							.showMessageDialog(
									null,
									"Please select the doctor first and then exam type for set prices",
									"Input Error",
									JOptionPane.ERROR_MESSAGE);
					subExamCB.setSelectedIndex(0);
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					return;
				}
				if (exam_name.equals("Select Exam")
						|| examsub_catname.equals("Select Category")) {
					JOptionPane.showMessageDialog(PackageExamEntery.this,
							"Please Select an Exam", "Exam",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				Object getAlready = null;
				try {
					getAlready = itemsHashMap.get(exam_name + " "
							+ examsub_catname);
					// getAlready =v1.;
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (getAlready == null) {
					examCategory.add("PCK-"+exam_name);
					examName.add(exam_name + " " + examsub_catname);
					examChargesVector.add(exam_charge);
					examIdVector.add(exam_nameid);
					examRoomVector.add(examRoomNoLB.getText().toString());
					itemsHashMap.put(exam_name + " " + examsub_catname,
							exam_charge);
					examHashMap.put(exam_nameid, examRoomNoLB.getText()
							.toString());
					loadDataToTable();
				} else {
					JOptionPane.showMessageDialog(PackageExamEntery.this,
							"Can not enter duplicate item code",
							"Duplicate Item", JOptionPane.INFORMATION_MESSAGE);
				}
			}
		});
		btnAdd.setBounds(66, 226, 89, 25);
		panel_2.add(btnAdd);

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
				examCategory.remove(cur_selectedRow);
				examChargesVector.remove(cur_selectedRow);
				examIdVector.remove(cur_selectedRow);
				examRoomVector.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(181, 226, 89, 25);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(132, 451, 106, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 253, 329, 195);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Exam Name", "Charges" }));
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
		lblTotalcharges.setBounds(248, 451, 94, 20);
		panel_2.add(lblTotalcharges);

		JButton btnNewButton_3 = new JButton("Generate Reciept");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exam_note = examNote.getText();
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (exam_doctorname.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (examName.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add Exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					examdbConnection = new ExamDBConnection();
					data[2] = p_id;
					data[3] = p_name;
					data[4] = exam_doctorname;
					data[5] = exam_date;
					data[7] = exam_note;
					data[9] = "" + ReceptionMain.receptionNameSTR;
					data[12] = "No";
					int updatedInsertCounter = 0;
					for (int i = 0; i < ObjectArray_examname.length; i++) {
						data[0] = (String) ObjectArray_examname[i];
						data[1] = (String) ObjectArray_examnameid[i];
						data[6] = (String) ObjectArray_examcharges[i];
						data[8] = (String) ObjectArray_examroom[i];
						data[10] = (String) ObjectArray_examcategories[i];
						try {

							updatedInsertCounter = examdbConnection
									.inserData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					exam_counter = (updatedInsertCounter - (ObjectArray_examname.length - 1))
							+ "";
//					JOptionPane.showMessageDialog(null,
//							"Data Saved Successfully ", "Data Save",
//							JOptionPane.INFORMATION_MESSAGE);
				
					examdbConnection.closeConnection();

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					String[] data1 = new String[10];
					int lastRef = Integer.parseInt(exam_counter)
							+ ObjectArray_examname.length - 1;
					data1[0] = "" + exam_name;
					data1[1] = "" + exam_doctorname;
					data1[2] = "" + p_name;
					data1[3] = "" + totalCharges;
					data1[4] = "" + ReceptionMain.receptionNameSTR;
					data1[5] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data1[6] = "" + timeFormat.format(cal1.getTime());
					data1[7] = "" + exam_counter;
					data1[8] = "" + lastRef;
					int index = 0;
					AmountReceiptDBConnection amountReceiptDBConnection = new AmountReceiptDBConnection();

					try {
						index = amountReceiptDBConnection.inserDataExam(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					amountReceiptDBConnection.closeConnection();

					try {
						new ExamSlippdf(exam_counter + "",
								ObjectArray_examname, ObjectArray_examcharges,
								totalCharges + "", index,true,false,"",1.0);
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					dispose();
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
		btnNewButton_4.setIcon(new ImageIcon(PackageExamEntery.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		patientIDCB = new JComboBox(patientID);
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

			
				setPatientDetail(p_id);
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
					
					examChargesLB.setText("");
					examRoomNoLB.setText("");
					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examCategory.clear();
					examChargesVector.clear();
					examIdVector.clear();
					examRoomVector.clear();
				
					getAllExamList();
					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(472, 59, 205, 25);
		panel_4.add(patientIDCB);
		searchBT1.setFocusable(true);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(369, 406, 313, 118);
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
		searchPatientTB.setText(patientIDSTR);
		searchPatientTB.setEditable(false);
	}

	public String getInsuranceIndex()
	{
		String query = "";
		String price = "";
		String[] values = new String[2];
		values[1]="4";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(p_insurancetype);
		return values[1];
	}
	public void getPatientsID(String index) {
		lastOPDDateLB.setText("Last Exam : ");
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
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		examdbConnection = new ExamDBConnection();
		String lastOPDDate = examdbConnection.retrieveLastExamPatient(indexId);
		lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		examdbConnection.closeConnection();
		patientDBConnection.closeConnection();
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
		doctorNameCB.setSelectedItem(doctorNameSTR);
	}
	public void getDoctorDetail(String name) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				System.out.println("HELLO"+resultSet.getInt(4));
				discount = resultSet.getInt(4);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getAllExamList() {
		exams.removeAllElements();
		exams.addElement("Select Exam");
		PackageDBConnection dbConnection = new PackageDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExams(packageID);
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

	public void getExamSubCatList(String exam_name) {
		examSubCat.removeAllElements();
		examRoom.removeAllElements();
		examRate.removeAllElements();
		examID.removeAllElements();
		examSubCat.addElement("Select Category");
		examRoom.add("");
		examRate.add("");
		examID.add("");
		PackageDBConnection dbConnection = new PackageDBConnection();
		ResultSet resultSet = dbConnection.getExamSubCat(packageID,exam_name);
		try {
			while (resultSet.next()) {
				examID.add(resultSet.getObject(1).toString());
				examSubCat.addElement(resultSet.getObject(2).toString());
				examRoom.add((resultSet.getObject(3).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		subExamCB.setModel(examSubCat);
		subExamCB.setSelectedIndex(0);

	}


	public void getPrice(String exam_code) {
	
		String price = "0";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		String[] values = new String[2];
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(p_insurancetype);
		
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

		if (hours <= 24 && hours >= 20) {
			System.out.println("OKKK");

			exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
		} else if (hours <= 7 && hours >= 0) {
			System.out.println("OKKKK 2");
			exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
		}
	
		double charges=Double.parseDouble(exam_charge);
		double discountValue=(charges*discount)/100;
		charges=charges-discountValue;
		
		
		exam_charge=""+(int)charges;
		
		
	}
	public void opd(String opdID) {
		

		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId2(opdID);
			while (rs.next()) {
				
				p_id= rs.getObject(1).toString();
				searchPatientTB.setText(""+p_id);
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemsHashMap.size();
		totalCharges = 0;
		// declare two arrays one for key and other for keyValues
		ObjectArray_examnameid = new Object[size];
		ObjectArray_examname = new Object[size];
		ObjectArray_examroom = new Object[size];
		ObjectArray_examcharges = new Object[size];
		ObjectArray_examcategories = new Object[size];
		ObjectArray_ListOfexams = new Object[size][2];
	
		for (int i = 0; i < examName.size(); i++) {
			ObjectArray_examcharges[i] = examChargesVector.get(i);
			ObjectArray_ListOfexams[i][0] = examName.get(i);
			ObjectArray_examname[i] = examName.get(i);
			ObjectArray_examroom[i] = examRoomVector.get(i);
			ObjectArray_examnameid[i] = examIdVector.get(i);
			ObjectArray_examcategories[i] = examCategory.get(i);
			System.out.println("Exam code " + examIdVector.get(i));
			ObjectArray_ListOfexams[i][1] = ObjectArray_examcharges[i];
			totalCharges = totalCharges
					+ Double.parseDouble(ObjectArray_examcharges[i].toString()
							.trim());
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Exam Name", "Charges" }) {

			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		lblTotalcharges.setText("`" + totalCharges + "");
		lblTotalcharges.setFont(customFont);
	}
}
