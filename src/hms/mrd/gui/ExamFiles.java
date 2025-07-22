package hms.mrd.gui;

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
import hms.patient.database.PatientDBConnection;
import hms.test.database.OperatorDBConnection;
import hms1.ipd.database.IPDDBConnection;

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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

public class ExamFiles extends JFrame {

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
	public JFileChooser jfc = new JFileChooser();
	public File[] file;
	// public File[] filepath;
	
	Vector files = new Vector();
	Vector filesPath = new Vector();
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	String dest = "";
	
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
	String ipd_id = "", ipd_date = "", ipd_charges = "", exam_name = "",
			exam_nameid = "", exam_room = "45";
	public static String exam_operator = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examRefID = new Vector<String>();
	Vector<String> examRefTableID = new Vector<String>();

	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	ExamDBConnection examdbConnection;
	private JTable ipdDataTable;
	private JList list;
	private JButton btnBrowseFile;
	private JLabel timeLB;
	private JButton saveTestResultBT;
	public String news = "";
	public static String OS;
	private JPanel panel;
	TestLabPatientList labPatientList;
	private JButton btnRemoveFile;
	String fileSelected = "";
	String testPerformed = "No";
	
	public static void main(String[] arg)
	{
		new ExamFiles().setVisible(true);
	}
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public ExamFiles() {
		setResizable(false);
		setTitle("Exam Files");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ExamFiles.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(20, 5, 1093, 604);
		contentPane = new JPanel();
		OS = System.getProperty("os.name").toLowerCase();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,

		null, null));
		panel_3.setBounds(6, 11, 1076, 50);
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
				" Rotary Ambala Cancer and General Hospital (Ambala Cantt.)");
		label_2.setForeground(Color.BLUE);
		label_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		label_2.setBounds(76, 11, 617, 28);
		panel_3.add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setIcon(new ImageIcon(ExamFiles.class
				.getResource("/icons/smallLogo.png")));
		label_3.setBounds(10, 0, 56, 50);
		panel_3.add(label_3);

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
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_4.setBounds(6, 63, 1076, 502);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(10, 41, 103, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		readFile();
		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(113, 36, 182, 25);
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
							patientID.removeAllElements();
							patientIDCB.setModel(patientID);
							lastOPDDateLB.setText("Last OPD :");
							try {
								loadDataToTable("0");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
							lastOPDDateLB.setText("Last OPD :");
							try {
								loadDataToTable("0");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
							lastOPDDateLB.setText("Last OPD :");
							try {
								loadDataToTable("0");
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
		searchBT.setBounds(295, 36, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(ExamFiles.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());

		panel = new JPanel();
		panel.setBounds(10, 72, 313, 301);
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
		lblPatientId.setBounds(356, 38, 77, 20);
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

				patientNameTB.setText("");
				addressTB.setText("");
				ageTB.setText("");
				cityTB.setText("");
				telephoneTB.setText("");
				insuranceTypeTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				final File directory = new File(System.getProperty("user.dir")
						+ "/localTemp");
				files.clear();
				filesPath.clear();
				list.removeAll();
				list.setListData(files);
				try {
					loadDataToTable("0");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// System.out.println(p_id + "    "
				// + patientIDCB.getSelectedIndex());
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
					try {
						loadDataToTable(p_id);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(431, 34, 251, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(333, 72, 349, 419);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Exam DATA", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_2.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 329, 387);
		panel_2.add(scrollPane);

		ipdDataTable = new JTable();
		ipdDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		ipdDataTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		ipdDataTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDataTable.getTableHeader().setReorderingAllowed(false);
		ipdDataTable.setModel(new DefaultTableModel(new Object[][] { { null,
				null, null }, }, new String[] {"Exam No", "Exam Name", "Date", "Amount"  }));
		ipdDataTable.getColumnModel().getColumn(1).setPreferredWidth(180);
		ipdDataTable.getColumnModel().getColumn(1).setMinWidth(180);
		ipdDataTable.getColumnModel().getColumn(2).setPreferredWidth(70);
		ipdDataTable.getColumnModel().getColumn(2).setMinWidth(70);
		ipdDataTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							int selectedRowIndex = ipdDataTable
									.getSelectedRow();
							Object selectedObject = ipdDataTable.getModel()
									.getValueAt(selectedRowIndex, 0);
							System.out.println("" + selectedObject);
							ipd_id = "" + selectedObject;

							btnBrowseFile.setEnabled(true);
							saveTestResultBT.setEnabled(true);
						
					
							if (examRefID.size() > 0
									|| examRefTableID.size() > 0) {
								// addReferenceBT.setEnabled(getExamStatus(""
								// + ipd_id));
								// addReferenceBT.setEnabled(true);
								// btnResults.setEnabled(false);
								// btnWidal.setEnabled(false);
							}
							
							final File directory = new File(System
									.getProperty("user.dir") + "/localTemp");
						
							files.clear();
							filesPath.clear();
							list.removeAll();
							list.setListData(files);
							new Thread() {
								@Override
								public void run() {
									try {
										try {
											LocalCopy(
													getDirectory(p_id, ipd_id),
													ipd_id);
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
		scrollPane.setViewportView(ipdDataTable);

		btnBrowseFile = new JButton("Browse File");
		btnBrowseFile.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnBrowseFile.setEnabled(false);
		btnBrowseFile.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				file = getMultipleFile();
				if(file.length>0)
				{
					final String[] values = new String[file.length];
					for (int i = 0; i < file.length; i++) {
						System.out.println("" + file[i].getPath());
						values[i] = file[i].getName();
						files.add(file[i].getName().toString());
						filesPath.add(file[i].getPath());
					}

					list.removeAll();
					list.setListData(files);
				}
			
				// list.setModel(model));
			}
		});
		btnBrowseFile.setBounds(906, 308, 146, 34);
		panel_4.add(btnBrowseFile);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Files List",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setBounds(692, 72, 360, 215);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 344, 188);
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
						System.out
								.println("Double-clicked on: " + o.toString());
						;
						OPenFileWindows(filesPath.get(index).toString());
						if (isWindows()) {
							//OPenFileWindows(filesPath.get(index).toString());
						} else if (isUnix()) {

							if (System.getProperty("os.version").equals(
									"3.11.0-12-generic")) {
								Run(new String[] { "/bin/bash", "-c",
										open[0] + " " + filesPath.get(index) });
							} else {
								Run(new String[] { "/bin/bash", "-c",
										open[1] + " " + filesPath.get(index) });
							}
						} else {
							Run(new String[] { "/bin/bash", "-c",
									open[2] + " " + filesPath.get(index) });
						}

					}
				}
			}
		};
		list.addMouseListener(mouseListener);
		NewsDBConnection newsDBConnection = new NewsDBConnection();

		saveTestResultBT = new JButton("Save File");
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

				int selectedRowIndex = ipdDataTable.getSelectedRow();
				if (selectedRowIndex == -1) {
					JOptionPane.showMessageDialog(null, "Please Select Exam ",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (arg0.getClickCount() == 2) {
					if (files.size() > 0) {
						dest = makeDirectory(p_id, ipd_id);
						try {
							for (int i = 0; i < filesPath.size(); i++) {
								try {
									copyFileUsingJava7Files(filesPath.get(i)
											+ "", dest + "/" + files.get(i));
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
		saveTestResultBT.setBounds(702, 308, 146, 34);

		panel_4.add(saveTestResultBT);

		JLabel lblDevelopedBy = new JLabel("Developed By : Sukhpal Saini");
		lblDevelopedBy.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDevelopedBy.setBounds(20, 463, 302, 28);
		panel_4.add(lblDevelopedBy);

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
		btnRemoveFile.setBounds(702, 373, 146, 34);
		panel_4.add(btnRemoveFile);
		
		JButton button = new JButton("Open Folder");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				final File folder = new File("localTemp");
				String fileName = folder.toString();
				OPenFileWindows(fileName);
				if (isWindows()) {
					//OPenFileWindows(fileName);
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
		button.setFont(new Font("Tahoma", Font.PLAIN, 13));
		button.setBounds(906, 373, 146, 34);
		panel_4.add(button);
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
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();

				try {
					copyFileFilesLocal(getDirectory(p_id, index) + "/" + files,
							"localTemp/" + files);
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

	public String getDirectory(String pid, String ipd_id) {

		return mainDir + "/HMS/Patient/" + pid + "/IPD/" + ipd_id + "/";
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



	public void loadDataToTable(String pid) throws SQLException {

		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveExamDataPatientID(pid);

		// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		int NumberOfColumns = 0, NumberOfRows = 0;
		NumberOfColumns = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			NumberOfRows++;
		}

		rs = db.retrieveExamDataPatientID(pid);
		Object Rows_Object_Array[][];
		Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
		
		int R = 0;
		while (rs.next()) {
			for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
			}
			R++;
		}
		ipdDataTable.setModel(new DefaultTableModel(Rows_Object_Array,
				new String[] { "Exam No", "Exam Name", "Date", "Amount"}));
		ipdDataTable.getColumnModel().getColumn(0).setPreferredWidth(80);
		ipdDataTable.getColumnModel().getColumn(0).setMinWidth(80);
		ipdDataTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		ipdDataTable.getColumnModel().getColumn(1).setMinWidth(80);
		ipdDataTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		ipdDataTable.getColumnModel().getColumn(2).setMinWidth(100);
		ipdDataTable.getColumnModel().getColumn(3).setPreferredWidth(70);
		ipdDataTable.getColumnModel().getColumn(3).setMinWidth(70);
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

	public String makeDirectory(String pid, String ipd_id) {
		try {

			SmbFile dir = new SmbFile(mainDir + "/HMS/Patient/" + pid
					+ "/IPD/");
			if (!dir.exists())
				dir.mkdirs();
			dir = new SmbFile(mainDir + "/HMS/Patient/" + pid
					+ "/IPD/" + ipd_id + "");
			if (!dir.exists())
				dir.mkdirs();
		} catch (SmbException | MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mainDir + "/HMS/Patient/" + pid + "/IPD/" + ipd_id;
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

}
