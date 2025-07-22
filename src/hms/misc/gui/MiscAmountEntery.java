package hms.misc.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.MiscSlippdf;
import hms.payments.PaymentMain;
import hms.reception.gui.ReceptionMain;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
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

public class MiscAmountEntery extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField attendanceDateTB;
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
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
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
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel miscCat = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	MiscDBConnection miscdbConnection;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JTextArea examNote;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	private JComboBox<String> miscDescTB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField miscAmountTB;
	private JComboBox paymentModeCB;
	private boolean alreadyPaid;
	private String req_urn;
	private double finalCharges=0;
	private double totalPaidPayment=0;
	private String payMode;
	private JLabel lblTotalcharges_1;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new MiscAmountEntery().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public MiscAmountEntery() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MiscAmountEntery.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New Misc Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(220, 70, 727, 634);
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

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "New Misc Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 699, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblAttendenceDate = new JLabel("Attendence Date");
		lblAttendenceDate.setBounds(10, 26, 128, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(10, 66, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

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
							miscDescTB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();
							loadDataToTable();
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
							patientIDWithaName.removeAllElements();
							patientIDCB.setModel(patientIDWithaName);
							lastOPDDateLB.setText("Last OPD :");
							miscDescTB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();

							loadDataToTable();
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
							patientIDWithaName.removeAllElements();
							patientIDCB.setModel(patientIDWithaName);
							lastOPDDateLB.setText("Last OPD :");
							miscDescTB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();

							loadDataToTable();
						}
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
					miscDescTB.setSelectedIndex(0);

					itemsHashMap.clear();
					examHashMap.clear();
					examName.clear();
					examChargesVector.clear();
					examIdVector.clear();

					loadDataToTable();
				}
			}
		});
		searchBT.setBounds(305, 60, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(MiscAmountEntery.class
				.getResource("/icons/zoom_r_button.png")));
		exam_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(123, 23, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

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
		lastOPDDateLB.setBounds(6, 284, 29, -1);
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

		.getBorder("TitledBorder.border"), "Misc Detail :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));
		panel_5.setBounds(5, 120, 337, 92);
		panel_2.add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		miscCat.addElement("MISC SERVICES");
		miscCat.addElement("INJECTION");
		miscCat.addElement("CANCER-MISC");
		miscCat.addElement("CANCER-CHEMO");
		miscCat.addElement("AMBULANCE CHARGES");
		miscDescTB = new JComboBox<String>();

		miscDescTB.setEditable(true);
		miscDescTB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (miscDescTB.getSelectedIndex() > 0) {
					descriptionSTR = (String) miscDescTB.getSelectedItem();
					descriptionSTR = descriptionSTR.toUpperCase();
					System.out.println(descriptionSTR);
				}
			}
		});
		final JTextField tfListText = (JTextField) miscDescTB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					descriptionSTR = text;
					descriptionSTR = descriptionSTR.toUpperCase();
					System.out.println(descriptionSTR);
					// HERE YOU CAN WRITE YOUR CODE
				}
			}
		});
		miscDescTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		miscDescTB.setBounds(16, 16, 311, 25);
		panel_5.add(miscDescTB);
		getAllMISCType();

		miscAmountTB = new JTextField();
		miscAmountTB.setHorizontalAlignment(SwingConstants.RIGHT);
		miscAmountTB.setBounds(141, 52, 186, 25);
		miscAmountTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		panel_5.add(miscAmountTB);
		miscAmountTB.setColumns(10);

		JLabel lblAmount = new JLabel("Amount :");
		lblAmount.setBounds(26, 57, 105, 14);
		panel_5.add(lblAmount);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (miscAmountTB.getText().equals("")
						|| descriptionSTR.equals("")) {
					JOptionPane.showMessageDialog(MiscAmountEntery.this,
							"Please description and charges", "MISC" + "",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					Integer.parseInt(miscAmountTB.getText());
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(MiscAmountEntery.this,
							"Please Enter charges in numerics", "MISC" + "",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				examName.add(descriptionSTR);
				examChargesVector.add(miscAmountTB.getText());
				loadDataToTable();
			}
		});
		btnAdd.setBounds(63, 212, 89, 25);
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

				examName.remove(cur_selectedRow);
				examChargesVector.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(178, 212, 89, 25);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Amt:");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(159, 448, 68, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 248, 329, 189);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Misc Name", "Charges" }));
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
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);

		paymentModeCB = new JComboBox();
		paymentModeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					paymentModeCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getAllPaymentModes();
		paymentModeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paymentModeCB.setBounds(114, 84, 225, 25);
		panel_2.add(paymentModeCB);

		JLabel label = new JLabel("Payment Mode :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(12, 84, 92, 25);
		panel_2.add(label);
		
		lblTotalcharges_1 = new JLabel("");
		lblTotalcharges_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTotalcharges_1.setBounds(80, 448, 94, 20);
		panel_2.add(lblTotalcharges_1);
		
		JLabel lblTotalCharges_1 = new JLabel("Total Amt:");
		lblTotalCharges_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTotalCharges_1.setBounds(5, 448, 68, 23);
		panel_2.add(lblTotalCharges_1);

		final JButton btnNewButton_3 = new JButton("Generate Reciept");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				exam_note = examNote.getText();
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (paymentModeCB.getSelectedItem().toString().equals("Select Payment Mode")) {
					JOptionPane.showMessageDialog(null, "Please select Payment Mode",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}else if (exam_doctorname.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (examName.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add Misc Charges", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {


					
					if(!alreadyPaid) {
					payMode=paymentModeCB.getSelectedItem().toString();
					finalCharges=totalCharges;
					}
					int payModeIndex=paymentModeCB.getSelectedIndex();
					if(!alreadyPaid && (payModeIndex!=1 && payModeIndex!=3)) {		
						btnNewButton_3.setEnabled(false);					
						PaymentMain MachinePayDialog=new PaymentMain(new String[]{totalCharges+"",p_id,p_name,ReceptionMain.receptionNameSTR,"MISC RECEIPT"},"MISC");
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
					if(alreadyPaid && totalPaidPayment!=totalCharges) {
						JOptionPane.showMessageDialog(null, "Incorrect amount. Please try again. \n Accepted Amt: "+totalPaidPayment+" and Your Amt: "+totalCharges+"",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					String[] data1 = new String[10];
					data1[0] = "" + exam_note;
					data1[1] = "" + p_id;
					data1[2] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data1[3]=req_urn;

					int index = 0;
					AmountReceiptDBConnection amountReceiptDBConnection = new AmountReceiptDBConnection();

					try {
						index = amountReceiptDBConnection.inserDataMisc(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					amountReceiptDBConnection.closeConnection();
					miscdbConnection = new MiscDBConnection();
					data[0] = index + "";

					data[3] = exam_doctorname;
					data[4] = p_id;
					data[5] = p_name;
					data[7] = "" + ReceptionMain.receptionNameSTR;
					data[8] = exam_date;
					data[9] = "" + timeFormat.format(cal1.getTime());
					data[10]=""+p_insurancetype;
					data[11]=payMode;
					double charges = 0;
					for (int i = 0; i < ObjectArray_examname.length; i++) {
						data[1] = (String) ObjectArray_examname[i];
						data[2] = "";
						double price = (Double
								.parseDouble((String) ObjectArray_examcharges[i]));
//						if (paymentModeCB.getSelectedIndex() > 1) {
//							double t = price
//									* Double.parseDouble(paymentCharges.get(
//											paymentModeCB.getSelectedIndex())
//											.toString());
//							price = Math.round(t * 100.0) / 100.0;
//							System.out.println(price+"   Hello");
//						}
						data[6] = price + "";
						charges = charges + price;
						try {

							miscdbConnection.inserData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					try {
						if (paymentModeCB.getSelectedIndex() > 1) {
							PaymentDBConnection dbConnection = new PaymentDBConnection();
//							double t = charges
//									* Double.parseDouble(paymentCharges.get(
//											paymentModeCB.getSelectedIndex())
//											.toString());
							double roundOff = Math.round(finalCharges * 100.0) / 100.0;
							double chargesEx = Math
									.round((roundOff - totalCharges) * 100.0) / 100.0;
							data[0] =payMode
									.toString();
							data[1] = "MISC";
							data[2] = index + "";
							data[3] = p_id;
							data[4] = p_name;
							data[5] = totalCharges + "";
							data[6] = chargesEx + "";
							data[7] = roundOff + "";
							data[8] = exam_date;
							data[9] = "" + timeFormat.format(cal1.getTime());
							data[10] = "" + ReceptionMain.receptionNameSTR;
							data[11] =  req_urn;
							dbConnection.inserData(data);
							dbConnection.closeConnection();
							charges=roundOff;
						}

					} catch (Exception e) {
						// TODO: handle exception
						dispose();
					}

					dispose();
					// JOptionPane.showMessageDialog(null,
					// "Data Saved Successfully ", "Data Save",
					// JOptionPane.INFORMATION_MESSAGE);

					miscdbConnection.closeConnection();
					try {
						new MiscSlippdf(exam_doctorname, ObjectArray_examname,
								ObjectArray_examcharges, charges + "",
								index, patientNameTB.getText(), p_id,p_insurancetype);
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
		btnNewButton_4.setIcon(new ImageIcon(MiscAmountEntery.class
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
					examChargesVector.clear();
					examIdVector.clear();

					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(446, 59, 236, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

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
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	public void getAllPaymentModes() {
		paymentModes.addElement("Select Payment Mode");
		paymentModes.addElement("CASH");
//		paymentCharges.add(0);
		paymentCharges.add(0);
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

	private void loadDataToTable() {
		// get size of the hashmap
		int size = examChargesVector.size();
		totalCharges = 0;
		// declare two arrays one for key and other for keyValues

		ObjectArray_examname = new Object[size];

		ObjectArray_examcharges = new Object[size];
		ObjectArray_ListOfexams = new Object[size][2];

		for (int i = 0; i < examName.size(); i++) {
			ObjectArray_examcharges[i] = examChargesVector.get(i);

			ObjectArray_examname[i] = examName.get(i);

			totalCharges = totalCharges
					+ Integer.parseInt(ObjectArray_examcharges[i].toString()
							.trim());
			ObjectArray_ListOfexams[i][0] = examName.get(i);
			ObjectArray_ListOfexams[i][1] = examChargesVector.get(i);
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Description", "Charges" }) {

			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		lblTotalcharges.setText("`" + totalCharges + "");
		lblTotalcharges.setFont(customFont);
	}

	public void getAllMISCType() {
		MiscDBConnection miscDBConnection = new MiscDBConnection();
		ResultSet resultSet = miscDBConnection.retrieveAllMiscCat();
		try {
			while (resultSet.next()) {
				String str = resultSet.getObject(1).toString();

				if (miscCat.getIndexOf(str) == -1) {
					miscCat.addElement(resultSet.getObject(1).toString());
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		miscDBConnection.closeConnection();
		miscDescTB.setModel(miscCat);
		miscDescTB.setSelectedIndex(0);
	}
	public void setAlreadyPayData(String reqUrn,double finalAmt,double WithoutChargeAmt,String PayMode,String pid,int payIndex) {
		alreadyPaid=true;
		req_urn=reqUrn;
		finalCharges=finalAmt;
		totalPaidPayment=WithoutChargeAmt;
		lblTotalcharges_1.setText(WithoutChargeAmt+"");
		payMode=PayMode;
		paymentModeCB.setSelectedIndex(payIndex);
		paymentModeCB.setEnabled(false);
		searchPatientTB.setText(pid);
		searchPatientTB.setEnabled(false);
		}
	public JComboBox getPaymentModeCB() {
		return paymentModeCB;
	}
}
