package hms.test.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exam.database.ReferenceTableDBConnection;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.TestLabPatientList;
import hms.main.DateFormatChange;
import hms.main.MarqueeLabel;
import hms.main.NewsDBConnection;
import hms.main.Pdf_troubleshooting;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.OpenFile;
import hms.test.database.OperatorDBConnection;
import hms.test.database.XrayDataBase;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.CharacterRun;
import org.apache.poi.hwpf.usermodel.Paragraph;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import UsersActivity.database.UADBConnection;

import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.border.LineBorder;

public class Test extends JFrame {

	UADBConnection ua=new UADBConnection();
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Timer timer;
	public JFileChooser jfc = new JFileChooser();
	private String sex;
	public File[] file; 
	// public File[] filepath;
	private HashMap itemsHashMap = new HashMap();
	private HashMap examHashMap = new HashMap();
	Vector<String> XrayImages = new Vector();
	Vector files = new Vector();
	Vector filesPath = new Vector();
	Vector Xrayfiles = new Vector();
	Vector XrayfilesPath = new Vector();
	private String studyID;
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	String dest = "";
	public  JCheckBoxMenuItem chckbxmntmShowPerformedTest;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examdoctor_name;
	Object[] ObjectArray_examname; 
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	ButtonGroup labType = new ButtonGroup();
	String mainDir = "";
	String[] open = new String[4];
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",
			patient_age = "";
	String exam_id = "", exam_doctorname = "",receiptID, exam_date = "", exam_note = "",
			exam_charge = "", exam_name = "",
			exam_nameid = "", exam_room = "",GetXrayImages_location="";
	public static String exam_operator = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	final DefaultComboBoxModel templateFileType = new DefaultComboBoxModel();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examRefID = new Vector<String>();
	Vector<String> examRefTableID = new Vector<String>();
	Vector examID = new Vector();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	ExamDBConnection examdbConnection;
	private JTable addTestTable_1;
	private JList list;
	private JButton btnBrowseFile;
	private JLabel timeLB;
	private JTextArea commentsTA;
	private JLabel lblExamName;
	private JButton saveTestResultBT;
	private JButton addReferenceBT;
	public String news = "";
	public static String OS;
	private JLabel operatorIDLB;
	private JLabel operatorNameLB;
	private JLabel lblLabName;
	private JLabel lblRoomno;
	private JButton printReportsBT;
	private JCheckBox chckbxPregnant;
	private JLabel lastLoginLB;
	private JButton btnWidal;
	private JButton btnResults;
	private JPanel panel_11;
	private JPanel panel_Save;
	private JPanel panel;
	private JPanel panel_9;
	private JComboBox templateFileCB;
	TestLabPatientList labPatientList;
	private JButton btnRemoveFile;
	String fileSelected = "";
	public String testPerformed = "No",operator_id="";
	private JComboBox selectLabCB;
	public static String dept_id="",dept_name="";
	public JLabel PNameLBL;
	public JLabel NoImgLBL;
	public JScrollPane XrayImgscrollPane;
	private JList Xraylist;
	private JPanel XrayPanel;
	private Thread XrayThread;
	private String Short_p_id="";
	XrayDataBase XrayDB;
	private JPanel panel_4;

	public static void main(String[] args) {
		new Test("riya").setVisible(true);;
	}
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public Test(final String username) {
		setResizable(false);
		setTitle("Lab");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Test.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(20, 5, 1093, 712);
		contentPane = new JPanel();
		OS = System.getProperty("os.name").toLowerCase();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);


		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,

				null, null));
		panel_3.setBounds(6, 19, 1076, 50);
		contentPane.add(panel_3);

		JLabel label = new JLabel("Date :");
		label.setForeground(new Color(0, 0, 128));
		label.setFont(new Font("Tahoma", Font.BOLD, 12));
		label.setBounds(885, 0, 69, 20);
		panel_3.add(label);

		JLabel dateLB = new JLabel(
				DateFormatChange.StringToMysqlDate(new Date()));
		dateLB.setForeground(new Color(0, 0, 128));
		dateLB.setFont(new Font("Tahoma", Font.BOLD, 12));
		dateLB.setBounds(962, 0, 95, 20);
		panel_3.add(dateLB);

		JLabel label_2 = new JLabel(
				" Rotary Ambala Cancer and General Hospital ");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		label_2.setBounds(76, 11, 617, 28);
		panel_3.add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon(Test.class
				.getResource("/icons/smallLogo.png")));
		label_3.setBounds(10, 0, 56, 50);
		panel_3.add(label_3);

		JButton logoutBT = new JButton("Logout");
		logoutBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		logoutBT.setIcon(new ImageIcon(Test.class
				.getResource("/icons/LOGGOFF.PNG")));
		logoutBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		logoutBT.setBounds(662, 12, 105, 30);
		panel_3.add(logoutBT);

		JButton settingBT = new JButton("Settings");
		settingBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				OperatorSettings ds = new OperatorSettings(""
						+ operatorIDLB.getText());
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		settingBT.setIcon(new ImageIcon(Test.class
				.getResource("/icons/setting.png")));
		settingBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		settingBT.setBounds(774, 12, 105, 30);
		panel_3.add(settingBT);

		JLabel label_4 = new JLabel("Time :");
		label_4.setForeground(new Color(0, 0, 128));
		label_4.setFont(new Font("Tahoma", Font.BOLD, 12));
		label_4.setBounds(885, 24, 69, 20);
		panel_3.add(label_4);

		timeLB = new JLabel("time");
		timeLB.setForeground(new Color(0, 0, 128));
		timeLB.setFont(new Font("Tahoma", Font.BOLD, 12));
		timeLB.setBounds(962, 24, 95, 20);
		panel_3.add(timeLB);
		panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Exams",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 13), null));
		panel_4.setBounds(6, 165, 1076, 502);
		//		contentPane.add(panel_4);


		panel_4.setLayout(null);

		//examdbConnection = new ExamDBConnection();
		//exam_counter = examdbConnection.retrieveCounterData() + "";
		//examdbConnection.closeConnection();



		panel_11 = new JPanel();
		panel_11.setBounds(692, 102, 374, 76);
		panel_4.add(panel_11);
		panel_11.setLayout(null);
		panel_11.setVisible(false);

		panel_Save = new JPanel();
		panel_Save.setVisible(false);
		panel_Save.setBounds(706, 401, 358, 52);
		panel_4.add(panel_Save);
		panel_Save.setLayout(null);


		JButton btnNewButton_1 = new JButton("SAVE XRAY");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = addTestTable_1.getSelectedRow();
				if (selectedRowIndex == -1) {
					JOptionPane.showMessageDialog(null, "Please Select Exam ",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Xrayfiles.size()== 0) {
					JOptionPane.showMessageDialog(null, "No Images Found!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Xrayfiles.size() > 0) {
					dest = makeDirectory(p_id, exam_id);
					try {
						for (int i = 0; i < XrayfilesPath.size(); i++) {
							try {
								copyFileUsingJava7Files(XrayfilesPath.get(i)
										+ "", dest + "/" +Xrayfiles.get(i).toString().replaceAll("\\s+", "") );
							} catch (IOException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					} catch (Exception e1) {
						// TODO: handle exception
					}
					JOptionPane.showMessageDialog(null,
							"Files Attached Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					ExamDBConnection db = new ExamDBConnection();
					try {
						db.updateXrayExamData( "1", exam_operator, "Yes",studyID,receiptID);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					db.closeConnection();
				}

			}
		});
		btnNewButton_1.setIcon(new ImageIcon(Test.class.getResource("/icons/downloadICON.png")));
		btnNewButton_1.setBounds(36, 6, 290, 40);
		panel_Save.add(btnNewButton_1);
		templateFileCB = new JComboBox();
		templateFileCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		templateFileCB.setBounds(10, 1, 354, 25);
		panel_11.add(templateFileCB);

		JButton btnOpen = new JButton("Open and Add");
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedRowIndex = addTestTable_1.getSelectedRow();
				if (selectedRowIndex > -1) {
					if(templateFileType.getSize()>0)
					{
						try {
							copyFile();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

				} else {
					JOptionPane.showMessageDialog(null, "Please Select Exam ",
							"Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnOpen.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOpen.setBounds(251, 36, 113, 27);
		panel_11.add(btnOpen);

		JButton btnOpenTemplate = new JButton("Open Template");
		btnOpenTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final File folder = new File(System.getProperty("user.dir")
						+ "/" + selectLabCB.getSelectedItem().toString());

				String fileName = folder + "/"
						+ templateFileCB.getSelectedItem();
				OPenFileWindows(fileName);
				if (isWindows()) {
					OPenFileWindows(fileName);
				} else if (isUnix()) {

					if (System.getProperty("os.version").equals(
							"3.11.0-12-generic")) {
						Run(new String[] { "/bin/bash", "-c",
								open[0] + " " + fileName });
					} else {
						Run(new String[] { "/bin/bash", "-c",
								open[1] + " " + fileName });
					}
				} else {
					Run(new String[] { "/bin/bash", "-c",
							open[2] + " " + fileName });
				}
			}
		});
		btnOpenTemplate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOpenTemplate.setBounds(120, 36, 121, 27);
		panel_11.add(btnOpenTemplate);

		JButton btnOpenFolder = new JButton("Open Folder");
		btnOpenFolder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				final File folder = new File(System.getProperty("user.dir")
						+ "/" + selectLabCB.getSelectedItem().toString());

				String fileName = folder.toString();
				if (isWindows()) {
					OPenFileWindows(fileName);
				} else if (isUnix()) {

					if (System.getProperty("os.version").equals(
							"3.11.0-12-generic")) {
						System.out.println("if1");
						Run(new String[] { "/bin/bash", "-c",
								open[0] + " " + fileName });
					} else {
						System.out.println("if2");
						Run(new String[] { "/bin/bash", "-c",
								open[1] + " " + fileName });
					}
				} else {
					System.out.println("if3");
					Run(new String[] { "/bin/bash", "-c",
							open[2] + " " + fileName });
				}
			}
		});
		btnOpenFolder.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnOpenFolder.setBounds(7, 36, 103, 27);
		panel_11.add(btnOpenFolder);
		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(10, 26, 103, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		readFile();
		this.chckbxmntmShowPerformedTest = new JCheckBoxMenuItem();
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
					printReportsBT.setEnabled(false);
					try {
						loadDataToTable("0");
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(113, 21, 182, 25);
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
					try {
						loadDataToTable("0");
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		searchBT.setBounds(295, 21, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(Test.class
				.getResource("/icons/zoom_r_button.png")));
		exam_date = DateFormatChange.StringToMysqlDate(new Date());

		panel = new JPanel();
		panel.setBounds(10, 104, 313, 301);
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
		rdbtnMale.setBounds(119, 192, 53, 23);
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

		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(6, 267, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(10, 66, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientIDCB = new JComboBox(patientID);
		patientIDCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					p_id = patientIDCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
				exam_doctorname = "";
				patientNameTB.setText("");
				addressTB.setText("");
				ageTB.setText("");
				cityTB.setText("");
				telephoneTB.setText("");
				insuranceTypeTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				printReportsBT.setEnabled(false);
				addReferenceBT.setEnabled(false);
				btnResults.setEnabled(false);
				btnWidal.setEnabled(false);
				final File directory = new File(System.getProperty("user.dir")
						+ "/localTemp");
				deleteLocalTemp(directory);
				files.clear();
				filesPath.clear();
				Xrayfiles.clear();
				XrayfilesPath.clear();
				list.removeAll();
				list.setListData(files);
				setPatientDetail(p_id);
				if (patientID.getSize() > 0) {
					patientNameTB.setText(p_name);
					addressTB.setText(p_address);
					ageTB.setText(p_age + "  (Y-M-D)");
					cityTB.setText(p_city);
					telephoneTB.setText(p_telephone);
					insuranceTypeTB.setText(p_insurancetype);
					if (p_sex.equals("Male")) {
						sex="M";
						rdbtnMale.setSelected(true);
						rdbtnFemale.setSelected(false);
					} else {
						sex="F";
						rdbtnMale.setSelected(false);
						rdbtnFemale.setSelected(true);
					}
					printReportsBT.setEnabled(true);
					try {
						loadDataToTable(p_id);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(selectLabCB.getSelectedIndex()==5)
						FetchXrayData();
				}

			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(113, 62, 210, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(333, 26, 349, 465);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Exams",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_2.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 329, 432);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addTestTable_1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.getTableHeader().setReorderingAllowed(false);
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] { { null,
			null, null }, }, new String[] { "Exam No", "Exam Name",
			"Charges" }));
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(180);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(180);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(70);
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {

							if(selectLabCB.getSelectedIndex()!=5) {
								btnBrowseFile.setEnabled(true);
								saveTestResultBT.setEnabled(true);
							}


							int selectedRowIndex = addTestTable_1
									.getSelectedRow();
							Object selectedObject = addTestTable_1.getModel()
									.getValueAt(selectedRowIndex, 0);
							System.out.println("" + selectedObject);
							exam_id = "" + selectedObject;


							lblExamName.setText(""
									+ addTestTable_1.getModel().getValueAt(
											selectedRowIndex, 1));
							//							exam_doctorname = ObjectArray_examdoctor_name[selectedRowIndex]
							//									.toString();
							//
							getExamReferenceRangeIndex(""
									+ ObjectArray_examnameid[selectedRowIndex]);
							getExamReferenceTableIndex(""
									+ ObjectArray_examnameid[selectedRowIndex]);
							exam_nameid = ObjectArray_examnameid[selectedRowIndex]
									.toString();
							addReferenceBT.setEnabled(true);
							btnResults.setEnabled(true);
							btnWidal.setEnabled(true);
							if (examRefID.size() > 0 || examRefTableID.size() > 0) {
								// addReferenceBT.setEnabled(getExamStatus(""
								// + exam_id));
								// addReferenceBT.setEnabled(true);
								// btnResults.setEnabled(false);
								// btnWidal.setEnabled(false);
							}
							if (addTestTable_1.getModel()
									.getValueAt(selectedRowIndex, 1).toString()
									.contains("WIDAL")) {
								btnWidal.setEnabled(true);
								btnResults.setEnabled(false);
							}
							final File directory = new File(System
									.getProperty("user.dir") + "/localTemp");
							deleteLocalTemp(directory);
							files.clear();
							filesPath.clear();
							list.removeAll();
							list.setListData(files);
							new Thread() {
								@Override
								public void run() {
									try {
										try {
											System.out.println("Value"+getDirectory(p_id, exam_id));
											LocalCopy(
													getDirectory(p_id, exam_id),
													exam_id);
										} catch (MalformedURLException
												| SmbException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										}
									} catch (Exception ex) {
										ex.printStackTrace();
									}
								}
							}.start();
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		btnBrowseFile = new JButton("Browse File");
		btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnBrowseFile.setEnabled(false);
		btnBrowseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				file = getMultipleFile();
				final String[] values = new String[file.length];
				for (int i = 0; i < file.length; i++) {
					System.out.println("" + file[i].getPath());
					values[i] = file[i].getName();
					files.add(file[i].getName().toString());
					filesPath.add(file[i].getPath());
				}

				list.removeAll();
				list.setListData(files);
				// list.setModel(model));
			}
		});
		btnBrowseFile.setBounds(833, 412, 103, 34);
		panel_4.add(btnBrowseFile);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Files List",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setBounds(706, 271, 360, 130);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 344, 103);
		panel_1.add(scrollPane_1);

		list = new JList();
		list.setToolTipText("Double Click To Open File");
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setFont(new Font("Tahoma", Font.PLAIN, 11));
		scrollPane_1.setViewportView(list);
		list.setModel(new AbstractListModel() {
			String[] values = new String[] {};

			@Override
			public int getSize() {
				return values.length;
			}

			@Override
			public Object getElementAt(int index) {
				return values[index];
			}
		});
		list.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent event) {
				if (!event.getValueIsAdjusting()) {
					try {
						JList source = (JList) event.getSource();
						fileSelected = source.getSelectedValue().toString();
					} catch (Exception e) {
						// TODO: handle exception
					}
					if(selectLabCB.getSelectedIndex()!=5) 
						btnRemoveFile.setEnabled(true);

				}
			}
		});

		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent mouseEvent) {
				JList theList = (JList) mouseEvent.getSource();
				if (mouseEvent.getClickCount() == 2) {
					int index = theList.locationToIndex(mouseEvent.getPoint());
					if (index >= 0) {
						Object o = theList.getModel().getElementAt(index);
						//						System.out
						//								.println("Double-clicked on: " + o.toString());
						//						System.out.println("/home/hms"+filesPath.get(index).toString()+"-TESSSSS");
						//						Path currentRelativePath = Paths.get("");
						//						String s = currentRelativePath.toAbsolutePath().toString();
						//						System.out.println("Current absolute path is: " + s);
						//						System.out.println("neww" + s+"/"+filesPath.get(index).toString());
						fileSelected = fileSelected.replaceAll("\\s+", "");
						if (isWindows()) {
							OPenFileWindows("localTemp/" + fileSelected + "");
						} else if (isUnix()) {

							if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
								Run(new String[] { "/bin/bash", "-c",
										open[0] + " localTemp/" + fileSelected });
							} else {
								Run(new String[] { "/bin/bash", "-c",
										open[1] + " localTemp/" + fileSelected });
							}
						} else {
							Run(new String[] { "/bin/bash", "-c",
									open[2] + " localTemp/" + fileSelected });
						}

					}
				}
			}
		};
		list.addMouseListener(mouseListener);
		printReportsBT = new JButton("All Reports");
		printReportsBT.setEnabled(false);
		printReportsBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				AllReportsForPrint allReportsForPrint = new AllReportsForPrint(
						Test.this, p_id, exam_room);
				allReportsForPrint
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				allReportsForPrint.setLocationRelativeTo(contentPane);
				allReportsForPrint.setModal(true);
				allReportsForPrint.setVisible(true); 
			}
		});
		printReportsBT.setFont(new Font("Tahoma", Font.PLAIN, 13));
		printReportsBT.setBounds(812, 458, 159, 25);
		panel_4.add(printReportsBT);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "News", TitledBorder.RIGHT,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_7.setBounds(10, 416, 313, 72);
		panel_4.add(panel_7);
		panel_7.setLayout(null);
		NewsDBConnection newsDBConnection = new NewsDBConnection();
		JLabel lblEmployeeOfThe = new MarqueeLabel(newsDBConnection.getNews(),
				MarqueeLabel.RIGHT_TO_LEFT, 20);
		lblEmployeeOfThe.setForeground(Color.RED);
		lblEmployeeOfThe.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployeeOfThe.setBounds(10, 21, 293, 40);
		panel_7.add(lblEmployeeOfThe);

		lblExamName = new JLabel("Exam Name");
		lblExamName.setHorizontalAlignment(SwingConstants.CENTER);
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblExamName.setBounds(706, 11, 359, 25);
		panel_4.add(lblExamName);

		JPanel panel_8 = new JPanel();
		panel_8.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Comments",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_8.setBounds(706, 189, 359, 76);
		panel_4.add(panel_8);
		panel_8.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 16, 343, 49);
		panel_8.add(scrollPane_2);

		commentsTA = new JTextArea();
		commentsTA.setLineWrap(true);
		commentsTA.setRows(8);
		scrollPane_2.setViewportView(commentsTA);

		saveTestResultBT = new JButton("Save Test");               
		saveTestResultBT.setFont(new Font("Tahoma", Font.PLAIN, 13));
		saveTestResultBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			}
		});
		saveTestResultBT.setEnabled(false);
		saveTestResultBT.setToolTipText("Double Click to Save Report");
		saveTestResultBT.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				int selectedRowIndex = addTestTable_1.getSelectedRow();
				if (selectedRowIndex == -1) {
					JOptionPane.showMessageDialog(null, "Please Select Exam ",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (arg0.getClickCount() == 2) {
					if (files.size() > 0) {
						dest = makeDirectory(p_id, exam_id);
						try {
							for (int i = 0; i < filesPath.size(); i++) {
								try {
									copyFileUsingJava7Files(filesPath.get(i)
											+ "", dest + "/" +files.get(i).toString().replaceAll("\\s+", "") );
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
						// final File sourceLocation = new File(System
						// .getProperty("user.dir") + "/localTemp");
						//
						// if (sourceLocation.isDirectory()) {
						// try {
						//
						// String[] children = sourceLocation.list();
						// for (int i = 0; i < children.length; i++) {
						//
						// copyFileUsingJava7Files(new File(
						// sourceLocation, children[i])
						// .toString(), dest + "/"
						// + children[i]);
						//
						// }
						// } catch (IOException e) {
						// // TODO Auto-generated catch block
						// e.printStackTrace();
						// }
						// }
						JOptionPane.showMessageDialog(null,
								"Files Attached Successfully ", "Data Save",
								JOptionPane.INFORMATION_MESSAGE);
						files.clear();
						filesPath.clear();
						list.removeAll();
						list.setListData(files);
					}
					updateTestData(exam_id,"Yes");
					try {
						StockMaintain(exam_id,lblExamName.getText().toString());
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					btnBrowseFile.setEnabled(false);
					saveTestResultBT.setEnabled(false);

					try {
						loadDataToTable(p_id);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		saveTestResultBT.setIcon(null);
		saveTestResultBT.setBounds(720, 412, 103, 34);

		panel_4.add(saveTestResultBT);

		panel_9 = new JPanel();
		panel_9.setBounds(692, 104, 374, 74);
		panel_4.add(panel_9);
		panel_9.setLayout(null);

		addReferenceBT = new JButton("References");
		addReferenceBT.setBounds(0, 30, 115, 44);
		panel_9.add(addReferenceBT);
		addReferenceBT.setEnabled(false);
		addReferenceBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// String[] s = examRefID.toArray(new String[examRefID.size()]);
				// final String[] s2 = examRefTableID.toArray(new
				// String[examRefTableID
				// .size()]);
				// if (examRefID.size() > 0) {
				// AddTestReferences addTestReferences = new
				// AddTestReferences(p_name,
				// s, exam_id, lblExamName.getText().toString(),
				// exam_nameid);
				// addTestReferences
				// .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				// addTestReferences.setLocationRelativeTo(contentPane);
				// addTestReferences.setModal(true);
				// addTestReferences.setVisible(true);
				// }
				//
				// if (examRefTableID.size() > 0) {
				// AddTestReferenceTable addReferenceTable = new
				// AddTestReferenceTable(p_name,
				// s2, exam_id, lblExamName.getText().toString(),
				// exam_nameid);
				// addReferenceTable
				// .setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				// addReferenceTable.setLocationRelativeTo(contentPane);
				// addReferenceTable.setModal(true);
				// addReferenceTable.setVisible(true);
				// }
				String p_type = "";
				if (chckbxPregnant.isSelected()) {
					p_type = "Pregnant";
				} else {
					p_type = "Normal";
				}
				TestReferenceSelector referenceSelector = new TestReferenceSelector(
						p_id, p_name, p_age, p_type, lblExamName.getText()
						.toString(), exam_id, exam_nameid);
				referenceSelector.setLocationRelativeTo(contentPane);
				referenceSelector.setModal(true);
				referenceSelector.setVisible(true);

			}
		});
		addReferenceBT.setFont(new Font("Tahoma", Font.PLAIN, 15));

		chckbxPregnant = new JCheckBox("Pregnant");
		chckbxPregnant.setBounds(105, 0, 97, 23);
		panel_9.add(chckbxPregnant);
		chckbxPregnant.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnResults = new JButton("Results");
		btnResults.setBounds(120, 30, 115, 44);
		panel_9.add(btnResults);
		btnResults.setEnabled(false);
		btnResults.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				TestResults testResults = new TestResults(p_name, exam_id,lblExamName.getText().toString(), exam_nameid);
				testResults.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				testResults.setLocationRelativeTo(contentPane);
				testResults.setModal(true);
				testResults.setVisible(true);
			}
		});
		btnResults.setFont(new Font("Tahoma", Font.PLAIN, 15));

		btnWidal = new JButton("WIDAL");
		btnWidal.setBounds(245, 30, 115, 44);
		panel_9.add(btnWidal);
		btnWidal.setEnabled(false);
		btnWidal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WidalTestValue widalTestValue = new WidalTestValue(p_name,exam_id, lblExamName.getText().toString(), exam_nameid);
				widalTestValue.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				widalTestValue.setLocationRelativeTo(contentPane);
				widalTestValue.setModal(true);
				widalTestValue.setVisible(true);
			}
		});
		btnWidal.setFont(new Font("Tahoma", Font.PLAIN, 15));

		chckbxPregnant.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("" + chckbxPregnant.isSelected());
				addTestTable_1.getSelectionModel().clearSelection();
				addReferenceBT.setEnabled(false);
			}
		});

		btnRemoveFile = new JButton("Remove File");
		btnRemoveFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (files.contains(fileSelected)) {
					files.remove(fileSelected);
					filesPath.remove(fileSelected);
					list.removeAll();
					list.setListData(files);
				}
				btnRemoveFile.setEnabled(false);
				fileSelected = "";
			}
		});
		btnRemoveFile.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnRemoveFile.setEnabled(false);
		btnRemoveFile.setBounds(946, 412, 103, 34);
		panel_4.add(btnRemoveFile);

		selectLabCB = new JComboBox();
		selectLabCB.setFont(new Font("Tahoma", Font.BOLD, 12));
		selectLabCB.setModel(new DefaultComboBoxModel(new String[] {
				"Path Lab", "Ultrasound", "CT Scan", "MRI Scan",
				"Digital Reports","Xray","Others" }));
		selectLabCB.setBounds(803, 47, 244, 25);
		panel_4.add(selectLabCB);

		selectLabCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Xrayfiles.clear();
				XrayfilesPath.clear();
				files.clear();
				filesPath.clear();
				list.removeAll();
				list.setListData(files);
				templateFileType.removeAllElements();
				templateFileCB.setModel(templateFileType);

				switch (selectLabCB.getSelectedIndex()) {
				case 0: 
					panel_9.setVisible(true);
					panel_11.setVisible(false);
					panel_Save.setVisible(false);
					XrayPanel.setVisible(false);
					if(XrayDB!=null)
						XrayDB.closeConnection();
					break;
				case 5:
					readXrayFile();
					FetchXrayData();
					XrayPanel.setVisible(true);
					( new Thread() { public void run() { 
						if(XrayDB==null)XrayDB=new XrayDataBase();
					} } ).start(); 
					panel_Save.setVisible(true);
					panel_9.setVisible(false); 
					panel_11.setVisible(false);
					break;
				default:
					templateFilesList();
					panel_9.setVisible(false);
					panel_11.setVisible(true);
					panel_Save.setVisible(false);
					XrayPanel.setVisible(false);
					if(XrayDB!=null)
						XrayDB.closeConnection();
					break;
				}

			}
		});

		JLabel lblNewLabel = new JLabel("Select Lab");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBounds(706, 47, 93, 25);
		panel_4.add(lblNewLabel);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_5.setBounds(16, 72, 68, 82);
		contentPane.add(panel_5);

		XrayPanel=new JPanel();
		XrayPanel.setBorder(new LineBorder(new Color(0, 0, 0)));
		XrayPanel.setVisible(false);
		XrayPanel.setBounds(692, 102, 374, 76);
		XrayPanel.setLayout(null);
		panel_4.add(XrayPanel);
		XrayImgscrollPane = new JScrollPane();

		XrayImgscrollPane.setBounds(170, 12, 192, 52);
		XrayPanel.add(XrayImgscrollPane);

		PNameLBL = new JLabel("Name : "); 
		PNameLBL.setFont(new Font("Dialog", Font.PLAIN, 12));
		PNameLBL.setBounds(12, 12, 140, 15);
		XrayPanel.add(PNameLBL);

		NoImgLBL = new JLabel("No. of Img : ");
		NoImgLBL.setFont(new Font("Dialog", Font.PLAIN, 12));
		NoImgLBL.setBounds(12, 39, 140, 15);
		XrayPanel.add(NoImgLBL);

		Xraylist = new JList();
		Xraylist.setToolTipText("Double Click To Open File");
		Xraylist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Xraylist.setFont(new Font("Tahoma", Font.PLAIN, 11));
		XrayImgscrollPane.setViewportView(Xraylist);
		Xraylist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					fileSelected = Xraylist.getSelectedValue().toString().replaceAll("\\s+", "");
					System.out.println(GetXrayImages_location+"/"+studyID + fileSelected);
					new OpenFile(GetXrayImages_location+"/"+studyID +"/"+fileSelected);
				}
			}
		});
		JButton btnNewButton = new JButton("");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				labPatientList.getData();
				labPatientList.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(Test.class
				.getResource("/icons/exam_dialog.png")));
		btnNewButton.setBounds(0, 0, 68, 70);
		panel_5.add(btnNewButton);

		JLabel lblOperatorId = new JLabel("Operator ID :");
		lblOperatorId.setForeground(new Color(0, 128, 128));
		lblOperatorId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOperatorId.setBounds(107, 80, 134, 20);
		contentPane.add(lblOperatorId);

		operatorIDLB = new JLabel("New label");
		operatorIDLB.setForeground(new Color(0, 128, 128));
		operatorIDLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		operatorIDLB.setBounds(251, 79, 281, 20);
		contentPane.add(operatorIDLB);

		operatorNameLB = new JLabel("New label");
		operatorNameLB.setForeground(new Color(0, 128, 128));
		operatorNameLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		operatorNameLB.setBounds(251, 104, 286, 20);
		contentPane.add(operatorNameLB);

		JLabel lblOperatorName = new JLabel("Operator Name :");
		lblOperatorName.setForeground(new Color(0, 128, 128));
		lblOperatorName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOperatorName.setBounds(107, 104, 150, 20);
		contentPane.add(lblOperatorName);

		JLabel label_9 = new JLabel("Last Login :");
		label_9.setForeground(new Color(0, 128, 128));
		label_9.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_9.setBounds(107, 126, 117, 21);
		contentPane.add(label_9);

		lastLoginLB = new JLabel("");
		lastLoginLB.setForeground(new Color(0, 128, 128));
		lastLoginLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lastLoginLB.setBounds(251, 126, 270, 20);
		contentPane.add(lastLoginLB);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_6.setBounds(955, 64, 114, 102);
		contentPane.add(panel_6); 
		panel_6.setLayout(null);

		JLabel label_5 = new JLabel("");
		label_5.setBounds(10, 6, 98, 97);
		panel_6.add(label_5);
		label_5.setIcon(new ImageIcon(Test.class
				.getResource("/icons/x-ray2.gif")));

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_10.setBounds(542, 72, 403, 92);
		contentPane.add(panel_10);
		panel_10.setLayout(null);

		lblLabName = new JLabel("Lab Name");
		lblLabName.setBounds(6, 10, 397, 40);
		panel_10.add(lblLabName);
		lblLabName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblLabName.setHorizontalAlignment(SwingConstants.CENTER);

		lblRoomno = new JLabel("RoomNo");
		lblRoomno.setBounds(6, 64, 387, 23);
		panel_10.add(lblRoomno);
		lblRoomno.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRoomno.setHorizontalAlignment(SwingConstants.CENTER);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(6, 0, 1100, 21);
		contentPane.add(menuBar);

		JMenu mnMyPatients = new JMenu("Patients");
		menuBar.add(mnMyPatients);

		JMenuItem mntmMyPatients = new JMenuItem("My Patients");
		mntmMyPatients.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				labPatientList.getData();
				labPatientList.setVisible(true);
			}
		});
		mnMyPatients.add(mntmMyPatients);

		JMenuItem mntmPatientsTestHistory = new JMenuItem("Patients Test History");
		mntmPatientsTestHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				TestHistory testHistory = new TestHistory(p_id,exam_room);
				testHistory.setModal(true);
				testHistory.setVisible(true);
			}
		});
		mnMyPatients.add(mntmPatientsTestHistory);

		JMenu mnTestShowing = new JMenu("Test Showing");
		menuBar.add(mnTestShowing);

		chckbxmntmShowPerformedTest = new JCheckBoxMenuItem(
				"Show Performed Test");
		chckbxmntmShowPerformedTest.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {

				if (chckbxmntmShowPerformedTest.isSelected()) {
					testPerformed = "Yes";
				} else {
					testPerformed = "No";
				}
			}

		});
		mnTestShowing.add(chckbxmntmShowPerformedTest);

		JMenu mnTestResults = new JMenu("Test Results");
		menuBar.add(mnTestResults);

		JMenuItem mntmAddFreeTest = new JMenuItem("Add Free Test Results");
		mntmAddFreeTest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				hms.test.free_test.TestResults testResults = new hms.test.free_test.TestResults();
				testResults.setModal(true);
				testResults.setVisible(true);
				ua.check_activity(username, 210, 2);

			}
		});
		mnTestResults.add(mntmAddFreeTest);

		JMenu mnStore = new JMenu("Store");
		menuBar.add(mnStore);

		JMenuItem mntmMyStock = new JMenuItem("My Stock");
		mntmMyStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamDepartmentStock departmentStock = new ExamDepartmentStock();
				departmentStock.setModal(true);
				departmentStock.setVisible(true);
				ua.check_activity(username, 212, 2);
			}
		});

		JMenuItem mntmNewEntry = new JMenuItem("New Entry");
		mntmNewEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ItemsEntry itemsEntry = new ItemsEntry();
				itemsEntry.setModal(true);
				itemsEntry.setVisible(true);
				ua.check_activity(username, 211, 2);
			}
		});
		mnStore.add(mntmNewEntry);
		mnStore.add(mntmMyStock);
		
		JMenuItem allreport = new JMenuItem("Approve Reports");
		allreport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				TestApproved frame = new TestApproved(exam_room);
				frame.setModal(true);
				frame.setVisible(true);
			}
		});
		menuBar.add(allreport);
		
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
				timeLB.setText(dateFormat.format(cal1.getTime()));
			}
		});
		timer.setRepeats(true);
		timer.setCoalesce(true);
		timer.start();
		getOperatorDetail(username);
		// usgFilesList();
		labPatientList = new TestLabPatientList(exam_room, Test.this); 

		if (exam_room.equals("21A")) {
			panel_4.setVisible(false);
			FreeTestPanel paneltest = new FreeTestPanel(Test.this);
			paneltest.setLayout(null);
			paneltest.setVisible(true);
			paneltest.setBounds(6, 165, 1076, 502);
			contentPane.add(paneltest);
		}else {
			contentPane.add(panel_4);
		}

		if(dept_name.equals("Xray"))
			selectLabCB.setSelectedItem(dept_name);



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

	public void LocalCopy(String path, String index)
			throws MalformedURLException, SmbException {
		System.out.println(path);
		String files;
		SmbFile folder = new SmbFile(path);

		SmbFile[] listOfFiles;
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		// fileList.clear();
		System.out.println("Lengthgh"+listOfFiles.length);
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();

				try {
					copyFileFilesLocal(getDirectory(p_id, index) + "/" + files,
							"localTemp/" + files.replaceAll("\\s+", ""));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

		try {
			getAllFiles("localTemp/");
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getDirectory(String pid, String exam_id) {

		return mainDir + "/HMS/Patient/" + pid + "/Exam/" + exam_id + "/";
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
				Short_p_id = resultSet.getObject(11).toString();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();

		examdbConnection = new ExamDBConnection();
		String lastOPDDate = examdbConnection.retrieveLastExamPatient(indexId);
		lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		examdbConnection.closeConnection();
		String data[] = new String[22];
		int i = 0;
		for (String retval : p_age.split("-")) {
			data[i] = retval;
			i++;
		}
		patient_age = "0";
		if (!data[0].equals("0")) {
			patient_age = data[0] + "";
		}
	}

	public void getExamReferenceRangeIndex(String exam_id) {
		examRefID.clear();
		String p_type = "";
		if (chckbxPregnant.isSelected()) {
			p_type = "Pregnant";
		} else {
			p_type = "Normal";
		}

		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceRangeIndex(exam_id,
				p_sex, patient_age, p_type);
		System.out.println("" + exam_id + p_sex + patient_age + p_type);
		try {
			while (resultSet.next()) {

				examRefID.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getExamReferenceTableIndex(String exam_id) {
		examRefTableID.clear();
		String p_type = "";
		if (chckbxPregnant.isSelected()) {
			p_type = "Pregnant";
		} else {
			p_type = "Normal";
		}
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceTableIndex(exam_id,
				p_sex, patient_age, p_type);
		try {
			while (resultSet.next()) {
				examRefTableID.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public boolean getExamStatus(String exam_id) {
		boolean flag = true;

		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveExamStatus(exam_id);
		try {
			while (rs.next()) {

				// System.out.println(rs.getObject(1)+"   "+exam_id);
				if (null == rs.getObject(1)) {
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return flag;
	}

	public void loadDataToTable(String pid) throws SQLException {

		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveExamDataPatientID(pid, exam_room,testPerformed);

		// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		int NumberOfColumns = 0, NumberOfRows = 0;
		NumberOfColumns = rs.getMetaData().getColumnCount();
		rs.last();
		NumberOfRows=rs.getRow();
		rs.beforeFirst();
		Object Rows_Object_Array[][];
		Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns - 1];
		ObjectArray_examnameid = new Object[NumberOfRows];
		ObjectArray_examdoctor_name = new Object[NumberOfRows];
		int R = 0;
		while (rs.next()) {
			for (int C = 1; C <= NumberOfColumns; C++) {
				if (C == 5) {
					ObjectArray_examnameid[R] = rs.getObject(C);
				} else if (C == 6) {
					ObjectArray_examdoctor_name[R] = rs.getObject(C);
				} else {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
			}
			R++;

		}
		db.closeConnection();
		addTestTable_1.setModel(new DefaultTableModel(Rows_Object_Array,
				new String[] { "Exam No", "Exam Name", "Date", "Charges" }) {
			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		addTestTable_1.getColumnModel().getColumn(0).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(0).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(185);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(185);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(70);

		GetDoc(pid);
	}

	private void GetDoc(String pid) {
		receiptID="";
		// TODO Auto-generated method stub
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveExamPrimaryDoc(pid,exam_room,testPerformed);
		try {
			while(rs.next()) {
				exam_doctorname=rs.getString(1);
				receiptID=rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public File[] getMultipleFile() {
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
				"JPG & PNG Images", "jpg", "png");
		// jfc.setFileFilter(filter);
		jfc.setMultiSelectionEnabled(true);
		jfc.setDialogTitle("Open File");
		if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			return jfc.getSelectedFiles();
		} else {
			return null;
		}
	}

	public String makeDirectory(String pid, String exam_id) {
		try {
			System.out.println("runinng");
			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient/" + pid
					+ "/Exam/" + exam_id + "");
			if (!dir.exists()){
				dir.mkdirs();
			}
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainDir + "/HMS/Patient/" + pid + "/Exam/" + exam_id;
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
		}
	}

	public void getOperatorDetail(String username) {
		OperatorDBConnection dbConnection = new OperatorDBConnection();
		ResultSet resultSet = dbConnection.retrieveUsernameDetail(username);
		try {
			while (resultSet.next()) {
				operatorIDLB.setText(resultSet.getObject(1).toString());
				exam_operator = resultSet.getObject(2).toString();
				lblLabName.setText("Lab Name : "
						+ resultSet.getObject(3).toString());
				dept_name=resultSet.getObject(3).toString();
				exam_room = resultSet.getObject(4).toString();
				operatorNameLB.setText(exam_operator);
				lblRoomno.setText("Room No. : " + exam_room);
				lastLoginLB.setText(resultSet.getObject(7).toString());

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
		}
		try {
			dbConnection.updateDataLastLogin(operatorIDLB.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		dbConnection.closeConnection();
		getDepartmentsDetail(dept_name);
	}
	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		try {
			while (resultSet.next()) {
				dept_id = resultSet.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	private static void copyFileUsingJava7Files(String source, String dest)
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


	private void copyFileFilesLocal(String source, String dest)
			throws IOException {
		new File("localTemp").mkdir();

		SmbFile remoteFile = new SmbFile(source);
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

	public void getAllFiles(String path) throws MalformedURLException,
	SmbException {
		files.clear();
		filesPath.clear();
		File folder1 = new File(path);
		File[] listOfFiles1 = folder1.listFiles();
		// System.out.println(listOfFiles1.length+"  Total Files");
		for (int i = 0; i < listOfFiles1.length; i++) {
			if (listOfFiles1[i].isFile()) {
				files.add(listOfFiles1[i].getName().toString());
				filesPath.add(listOfFiles1[i].getPath().toString());
			}
		}
		list.removeAll();
		list.setListData(files);
	}
	public void getAllXrayFiles(String path){
		Xrayfiles.clear();
		XrayfilesPath.clear();
		File f=new File(path);
		File[] listOfFiles1 = f.listFiles();
		for (int i = 0;listOfFiles1!=null && i < listOfFiles1.length; i++) {
			if (listOfFiles1[i].isFile() && XrayImages.indexOf(listOfFiles1[i].getName())!=-1) {
				Xrayfiles.add(listOfFiles1[i].getName().toString());
				XrayfilesPath.add(listOfFiles1[i].getPath().toString());
			}
		}
		Xraylist.removeAll();
		Xraylist.setListData(Xrayfiles);
	}

	public void updateTestData(String exam_id,String status) {
		String str = "0";
		if (!getExamStatus(exam_id)) {
			str = "1";
		}
		ExamDBConnection db = new ExamDBConnection();
		try {
			db.updateExamData(exam_id, str, exam_operator, status);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void StockMaintain(String exam_id,String exam_name) throws Exception
	{
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat(
				"hh:mm:ss a");
		String date = DateFormatChange
				.StringToMysqlDate(new Date()) + "";
		String time = "" + timeFormat.format(cal1.getTime());
		Dept_PillsRegisterDBConnection connection=new Dept_PillsRegisterDBConnection();
		int index=connection.inserDepartmentPillsRegister(dept_id, dept_name, exam_id, p_id, p_name, date, time);
		if(index==0)
		{
			connection.inserDepartmentPillsRegister(dept_id, dept_name, exam_id,exam_name, p_id, p_name, date, time);
		}
		connection.closeConnection();
		DepartmentStockDBConnection stockDBConnection=new DepartmentStockDBConnection();

		int index2=stockDBConnection.subtractStockTestID(exam_id, dept_name);
		if(index2==0)
		{
			stockDBConnection.subtractStockTestName(exam_name, dept_name);
		}
		stockDBConnection.closeConnection();
	}

	public void templateFilesList() {
		System.out.println("hhh"+System.getProperty("user.dir"));
		final File folder = new File(System.getProperty("user.dir") + "/"
				+ selectLabCB.getSelectedItem().toString());
		if (folder.exists()) {
			for (final File fileEntry : folder.listFiles()) {
				if (fileEntry.isDirectory()) {
				} else {
					System.out.println(fileEntry.getName());
					templateFileType.addElement(fileEntry.getName());
				}
			}
			templateFileCB.setModel(templateFileType);
		} else {
			folder.mkdir();
		}
	}

	public void copyFile() throws IOException {
		final File folder = new File(System.getProperty("user.dir") + "/"
				+ selectLabCB.getSelectedItem().toString());
		final File targetLocation = new File(System.getProperty("user.dir")
				+ "/localTemp");
		String fileName = "" + templateFileCB.getSelectedItem();
		if (!files.contains(fileName)) {
			files.add(fileName);
			fileName = fileName.replaceAll("\\s+", "");
			filesPath.add(targetLocation + "/" + fileName);
			list.removeAll();
			list.setListData(files);
		}
		if (!targetLocation.exists()) {
			targetLocation.mkdir();
		}
		POIFSFileSystem fs = null;
		try {
			fs = new POIFSFileSystem(new FileInputStream(folder + "/"
					+ templateFileCB.getSelectedItem()));
			HWPFDocument doc = new HWPFDocument(fs);

			doc = replaceText(doc, "p_id", p_id);
			doc = replaceText(doc, "p_name", p_name);
			doc = replaceText(doc, "age_sex", patient_age + "/" + p_sex);
			doc = replaceText(doc, "doctor_ref", exam_doctorname);
			doc = replaceText(doc, "test_date",
					DateFormatChange.getCurrentDate());
			saveWord(targetLocation + "/" + fileName, doc);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (isWindows()) {
			OPenFileWindows("localTemp/" + fileName + "");
		} else if (isUnix()) {

			if (System.getProperty("os.version").equals("3.11.0-12-generic")) {
				Run(new String[] { "/bin/bash", "-c",
						open[0] + " localTemp/" + fileName });
			} else {
				Run(new String[] { "/bin/bash", "-c",
						open[1] + " localTemp/" + fileName });
			}
		} else {
			Run(new String[] { "/bin/bash", "-c",
					open[2] + " localTemp/" + fileName });
		}

	}

	private static HWPFDocument replaceText(HWPFDocument doc, String findText,
			String replaceText) {
		Range r1 = doc.getRange();

		for (int i = 0; i < r1.numSections(); ++i) {
			Section s = r1.getSection(i);
			for (int x = 0; x < s.numParagraphs(); x++) {
				Paragraph p = s.getParagraph(x);
				for (int z = 0; z < p.numCharacterRuns(); z++) {
					CharacterRun run = p.getCharacterRun(z);
					String text = run.text();
					if (text.contains(findText)) {
						run.replaceText(findText, replaceText);
					}
				}
			}
		}
		return doc;
	}

	private static void saveWord(String filePath, HWPFDocument doc)
			throws FileNotFoundException, IOException {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(filePath);
			doc.write(out);
		} finally {
			out.close();
		}
	}

	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void OPenFileWindows(String path) {

		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}

	public static boolean deleteLocalTemp(File directory) {

		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteLocalTemp(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public void FetchXrayData() {
		studyID="";
		PNameLBL.setText("Name : ");
		NoImgLBL.setText("No. of Img : 0");
		XrayImages.clear();
		Xrayfiles.clear();
		XrayfilesPath.clear();
		Xraylist.removeAll();
		Xraylist.setListData(Xrayfiles);
		XrayThread = new Thread(new Runnable(){
			@Override
			public void run() {
				if(!patientNameTB.getText().equals("")) {
					XrayDB=new XrayDataBase();
					ResultSet rs=null;
					try {
						rs=XrayDB.GetXrayData(Short_p_id, patientNameTB.getText(),sex);
						int i=0;
						while(rs.next()) {
							i++;
						}
						if(i==0)
							rs=XrayDB.GetXrayData2(Short_p_id, patientNameTB.getText(),sex);
						String folder = null;

						while(rs.next()) {
							PNameLBL.setText("Name : "+rs.getString(1));
							studyID=rs.getObject(2).toString();
							XrayImages.add(rs.getString(3)+".jpg");
						}
						XrayDB.closeConnection();
						System.out.println(XrayImages+"    "+studyID);

						NoImgLBL.setText("No. of Img : "+XrayImages.size()+"");
						if(XrayImages.size()>0) {
							System.out.println("Image Path:-"+GetXrayImages_location+studyID+"/");
							getAllXrayFiles(GetXrayImages_location+studyID+"/");	
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}finally {
						XrayDB.closeConnection();
					}
				}
			}});
		XrayThread.start();
	}
	public void readXrayFile() {
		// The name of the file to open.
		String fileName = "Xray.mdi";
		// This will reference one line at a time
		try {
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line=null;
			while ((line = bufferedReader.readLine()) != null) {
				GetXrayImages_location=line.split("@")[1];
			}
			GetXrayImages_location=GetXrayImages_location+getDateFolder()+"/";
			//			System.out.println("="+GetXrayImages_location);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}
	public String getDateFolder() {
		// TODO Auto-generated method stub
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		return c.get(Calendar.DAY_OF_MONTH)+""+(c.get(Calendar.MONTH)+1)+""+c.get(Calendar.YEAR);
	}

	public JButton getBtnRemoveFile() {
		return btnRemoveFile;
	}

	public JComboBox getSelectLabCB() {
		return selectLabCB;
	}
}
