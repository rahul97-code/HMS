package hms1.ipd.gui;

import hms.departments.gui.DepartmentMain;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.OPDSlippdf_new;
import hms.patient.slippdf.PatientTransferSlippdf;
import hms.reception.gui.ReceptionMain;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDEntery.EnabledJComboBoxRenderer;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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
import javax.swing.plaf.basic.BasicComboBoxRenderer;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;
import lu.tudor.santec.jtimechooser.JTimeChooser;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;

public class IPDChangeBed extends JDialog {

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
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	int selectedRowIndex;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "0", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code_ipd = "", ward_code = "",
			descriptionSTR = "", charges = "", ipd_days, ipd_hours,ipd_ward_id="",
			ipd_minutes, ipd_expenses_id, wardCategorySTR = "",
			changeDateSTR = "",TodayCharge="",quantity="",
	        bed_start_date_time="";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	DefaultListSelectionModel model = new DefaultListSelectionModel();
	Vector<String> DisableIndex = new Vector<String>();
	Vector<String> bedIndex = new Vector<String>();
	Vector<String> expensesIndexVector = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	public static Font customFont;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTable table;
	private Timer timer;
	private JTextField ipdNoTB;
	private JComboBox<String> wardCB;
	private JComboBox<String> bedNoCB;
	private JComboBox<String> buildingCB;
	private JLabel wardInchargeLB;
	private JLabel wardRoomNoLB;
	private JDateChooser changeDate;
	private JTimeChooser changeTime;
	private JTextField ward_chargesTF;

	public static void main(String[] arg) {
		String formattednumber = "$" + String.valueOf("11001");
		System.out.println(NumberToWordConverter.convert(22190));
		new IPDChangeBed().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDChangeBed() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDChangeBed.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Change Ward");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1051, 598);

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
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_4.setBounds(6, 5, 1024, 558);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		JLabel lblAttendenceDate = new JLabel("Date");
		lblAttendenceDate.setBounds(10, 19, 51, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(333, 19, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					getPatientsID(str);
				}
				else
				{
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
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdBedNoTB.setText("");

					ipdNoTB.setText("");
					populatedDoctorTable("0");
				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(446, 14, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);


		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();

					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();


					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
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
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdNoTB.setText("");
					ipdBedNoTB.setText("");

					populatedDoctorTable("0");
				}
			}
		});
		searchBT.setBounds(628, 13, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(IPDChangeBed.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(78, 14, 232, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(699, 44, 313, 259);
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

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(689, 13, 77, 20);
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
				ipdBuildingTB.setText("");
				ipdNoTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");

				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				populatedDoctorTable("0");
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

				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(766, 11, 218, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(10, 69, 313, 180);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuilding.setBounds(10, 51, 108, 14);
		panel_7.add(lblBuilding);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 46, 182, 25);
		panel_7.add(ipdBuildingTB);

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(10, 98, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 93, 182, 25);
		panel_7.add(ipdWardTB);

		JLabel lblBedNo = new JLabel("Bed No :");
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNo.setBounds(10, 134, 108, 14);
		panel_7.add(lblBedNo);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 129, 182, 25);
		panel_7.add(ipdBedNoTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_7.add(ipdNoTB);

		JLabel lblOpdNo = new JLabel("IPD No :");
		lblOpdNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpdNo.setBounds(10, 15, 108, 14);
		panel_7.add(lblOpdNo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 309, 1002, 239);
		panel_4.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
			null }, }, new String[] { "", "" }) {
			Class[] columnTypes = new Class[] { Object.class, Integer.class,
					Object.class };

			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getClickCount() == 2) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					Object selectedObject = table.getModel().getValueAt(row, 0);

					try {
						try {
							new PatientTransferSlippdf(ipd_id, selectedObject
									+ "", table.getModel().getValueAt(row, 5)
									.toString());
						} catch (DocumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);
		panel_1.setBounds(333, 69, 337, 180);
		panel_4.add(panel_1);

		JLabel label = new JLabel("Building :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(6, 12, 96, 25);
		panel_1.add(label);

		buildingCB = new JComboBox<String>();
		buildingCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					building_name = buildingCB.getSelectedItem().toString();
					getAllWards();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		buildingCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		buildingCB.setBounds(103, 12, 214, 25);
		panel_1.add(buildingCB);

		JLabel label_1 = new JLabel("Ward Name :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(6, 49, 96, 25);
		panel_1.add(label_1);

		wardCB = new JComboBox<String>();
		wardCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					ward_name = wardCB.getSelectedItem().toString();
					getAllBed();
					getWardCharges(ward_name);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		wardCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardCB.setBounds(103, 49, 214, 25);
		panel_1.add(wardCB);

		JLabel label_2 = new JLabel("Bed No. :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(6, 86, 96, 25);
		panel_1.add(label_2);

		bedNoCB = new JComboBox<String>();
		bedNoCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					bed_no = bedNoCB.getSelectedItem().toString();
					if(DisableIndex.indexOf(bed_no)==-1 && bedNoCB.getSelectedIndex()!=0) {
						JOptionPane.showMessageDialog(null,
								"This Bed is Occupied!", "ERROR",
								JOptionPane.ERROR_MESSAGE);

						bedNoCB.setSelectedIndex(0);
						return;
					}
					ward_code = "";
					if (bedno.getSize() > 1) {
						ward_code = bedIndex.get(bedNoCB.getSelectedIndex() - 1);
						getWardDetail();
					}
					System.out.println("------"+ward_code);
				
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		bedNoCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedNoCB.setBounds(103, 86, 214, 25);
		panel_1.add(bedNoCB);

		JLabel label_3 = new JLabel("Ward Incharge :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(6, 221, 108, 25);
		panel_1.add(label_3);

		wardInchargeLB = new JLabel("");
		wardInchargeLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardInchargeLB.setBounds(119, 221, 194, 25);
		panel_1.add(wardInchargeLB);

		wardRoomNoLB = new JLabel("");
		wardRoomNoLB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardRoomNoLB.setBounds(83, 149, 0, 25);
		panel_1.add(wardRoomNoLB);

		JLabel label_2_1_1 = new JLabel("Ward Charges :");
		label_2_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		label_2_1_1.setBounds(6, 118, 127, 25);
		panel_1.add(label_2_1_1);

		ward_chargesTF = new JTextField();
		ward_chargesTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		ward_chargesTF.setEditable(false);
		ward_chargesTF.setColumns(10);
		ward_chargesTF.setBounds(135, 118, 182, 25);
		panel_1.add(ward_chargesTF);

		JButton btnNewButton = new JButton("Change Bed");
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (ipd_id.equals("0")) {
					JOptionPane.showMessageDialog(null,
							"Please Select Patient", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (ipdWardTB.getText().toString().equals(ward_name)) {
					JOptionPane.showMessageDialog(null,
							"Patient already in this ward", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (ward_name.equals("select ward")) {
					JOptionPane.showMessageDialog(null,
							"Please select bed no.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (bed_no.equals("Select Bed No")) {
					JOptionPane.showMessageDialog(null,
							"Please select bed no.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String dateTime=changeDateSTR+" "+ConvertTime(changeTime.getFormatedTime().toString());
				SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
				Date change_date=null;
				Date ward_date=null;
				try {
					change_date=timeFormat1.parse(dateTime);
					ward_date=timeFormat1.parse(bed_start_date_time);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	
//				if(ward_date.compareTo(change_date)>0 || change_date.compareTo(new Date())>0)
//				{
//					JOptionPane.showMessageDialog(null,
//							"please provide valid date and time!", "Input Error",
//							JOptionPane.ERROR_MESSAGE);
//					return;
//				}
				
				if(ward_date.after(change_date) || new Date().before(change_date))
				{
					JOptionPane.showMessageDialog(null,
							"please provide valid date and time!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				IPDDBConnection db1 = new IPDDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");

				data[0] = building_name;
				data[1] = ward_name;
				data[2] = wardRoomNoLB.getText();
				data[3] = bed_no;
				data[4] = ward_code;
				data[5] = "" + ipd_id;

				try {
					db1.updateBedData(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				data[0] = changeDateSTR;
				data[1] = "" +ConvertTime(changeTime.getFormatedTime().toString());
				data[2] = "" + ipd_id;
				data[3] = ipdWardTB.getText().toString();
				data[4] = "0";

				try {
					db1.updateWardData(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}



				WardsManagementDBConnection wardsManagementDBConnection = new WardsManagementDBConnection();
				data[0] = "0";
				try {
					wardsManagementDBConnection.updateDataUnfill(data,
							ward_code_ipd);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					wardsManagementDBConnection.closeConnection();
				}
				wardsManagementDBConnection.closeConnection();
				WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
				String[] data2 = new String[10];
				data2[0] = "" + p_id;
				data2[1] = "" + p_name;
				data2[2] = "1";
				data2[3] = changeDateSTR;

				try {
					dbConnection.updateData(data2, ward_code);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					dbConnection.closeConnection();
				}
				dbConnection.closeConnection();

				String data3[] = new String[13];
				data3[0] = "" + ipd_id;
				data3[1] = "" + p_id;
				data3[2] = "" + p_name;
				data3[3] = building_name;
				data3[4] = ward_name;
				data3[5] = wardRoomNoLB.getText();
				data3[6] = bed_no;
				data3[7] = changeDateSTR;
				data3[8] =  "" +ConvertTime(changeTime.getFormatedTime().toString());
				data3[9] = "" + ward_code;
				data3[10] = "" + wardCategorySTR;
				data3[11] = DepartmentMain.userID;  //user id
				data3[12] = DepartmentMain.userName;  //user name
				int id = 0;
				try {
					id = db1.inserIPDBedDetail(data3);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db1.closeConnection();

				JOptionPane.showMessageDialog(null, "Bed Changed Successfully",
						"Save Data", JOptionPane.INFORMATION_MESSAGE);
				// dispose();
				getIPDDATA();
				populatedDoctorTable(ipd_id);
				try {
					new PatientTransferSlippdf(ipd_id, id + "",
							DateFormatChange.StringToMysqlDate(new Date()));
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnNewButton.setBounds(543, 260, 127, 30);
		panel_4.add(btnNewButton);

		JLabel lblCurrentLocation = new JLabel("Current Location");
		lblCurrentLocation.setForeground(Color.BLUE);
		lblCurrentLocation.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblCurrentLocation.setBounds(10, 50, 138, 14);
		panel_4.add(lblCurrentLocation);

		JLabel lblTranasferTo = new JLabel("Tranasfer To");
		lblTranasferTo.setForeground(Color.BLUE);
		lblTranasferTo.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblTranasferTo.setBounds(333, 50, 138, 14);
		panel_4.add(lblTranasferTo);

		changeTime = new JTimeChooser();
		changeTime.getTimeField().setFont(new Font("Tahoma", Font.BOLD, 13));
		changeTime.setBounds(290, 260, 100, 30);
		panel_4.add(changeTime);
		changeTime.setTime(new Date());

		JLabel lblTime = new JLabel("Time");
		lblTime.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTime.setBounds(239, 261, 42, 30);
		panel_4.add(lblTime);

		JLabel lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDate.setBounds(20, 260, 61, 30);
		panel_4.add(lblDate);

		changeDate = new JDateChooser();
		changeDate.getCalendarButton().setFont(new Font("Tahoma", Font.BOLD, 13));
		changeDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							changeDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		changeDate.setDateFormatString("yyyy-MM-dd");
		changeDate.setBounds(67, 261, 154, 25);
		panel_4.add(changeDate);
		changeDate.setDate(new Date());

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
		searchPatientTB.requestFocus(true);
		getAllBuilding();
	}

	public String ConvertTime(String input) {

		DateFormat df = new SimpleDateFormat("HH:mm:ss");
		// Date/time pattern of desired output date
		DateFormat outputformat = new SimpleDateFormat("hh:mm:ss aa");
		Date date = null;
		String output = null;
		try {
			// Conversion of input String to date
			date = df.parse(input);
			// old date format to new date format
			output = outputformat.format(date);
			return output;
		} catch (ParseException pe) {
			pe.printStackTrace();
		}
		return output;
	}

	public void getPatientsID(String index) {
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
		ipdBuildingTB.setText("");
		ipdNoTB.setText("");
		ipdWardTB.setText("");
		ipdBedNoTB.setText("");

		populatedDoctorTable("0");
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
				getIPDDATA();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
	}

	public void getIPDDATA() {

		ipd_id = "0";
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientID(p_id);
		try {
			while (resultSet.next()) {
				ipd_id = resultSet.getObject(1).toString();
				ipdNoTB.setText("" + resultSet.getObject(2));
				ipdBuildingTB.setText("" + resultSet.getObject(3));
				ipdWardTB.setText("" + resultSet.getObject(4));
				ipdBedNoTB.setText("" + resultSet.getObject(5));
				ipd_advance = Double.parseDouble(resultSet.getObject(6)
						.toString());
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code_ipd = resultSet.getObject(9).toString();

				populatedDoctorTable(ipd_id);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void populatedDoctorTable(String ipd_id) {

		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveBedDetailData(ipd_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					//System.out.println(rs.getObject(C).toString());
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(
					Rows_Object_Array,
					new String[] { "S.N", "Building", "Ward", "Room", "Bed No",
							"Start Date", "Start Time", "End Date", "End Time"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};

			String S_date=model.getValueAt(model.getRowCount()-1, 5)+"";
			String S_time=model.getValueAt(model.getRowCount()-1, 6)+"";
			bed_start_date_time=S_date+" "+S_time;
//			int[] bedHours = DateFormatChange
//					.calulateHoursAndDays_BetweenDates(
//							DateFormatChange.javaDate(S_date + " "
//									+ S_time),DateFormatChange.javaDate((changeDateSTR+" "+ConvertTime(changeTime.getFormatedTime().toString()))));
			 table.setModel(model);
			 table.getColumnModel().getColumn(0).setPreferredWidth(150);
			 table.getColumnModel().getColumn(0).setMinWidth(150);
			 table.getColumnModel().getColumn(1).setResizable(false);
			 table.getColumnModel().getColumn(1).setPreferredWidth(150);
			 table.getColumnModel().getColumn(1).setMinWidth(150);

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void getAllBuilding() {
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllBuilding();
		try {
			while (resultSet.next()) {
				building.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		buildingCB.setModel(building);
		buildingCB.setSelectedIndex(0);
	}

	public void getAllWards() {
		wards.removeAllElements();
		wards.addElement("select ward");
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWards(building_name);
		try {
			while (resultSet.next()) {
				wards.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		wardCB.setModel(wards);
		wardCB.setSelectedIndex(0);
	}

	public void getAllBed() {
		model.clearSelection();
		bedIndex.clear();
		bedno.removeAllElements();
		bedno.addElement("Select Bed No");
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllBeds(building_name,
				ward_name);
		try {
			while (resultSet.next()) {
				bedIndex.add(resultSet.getObject(1).toString());
				bedno.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		bedNoCB.setModel(bedno);
		model.addSelectionInterval(0, 0);
		ResultSet rs = dbConnection.retrieveAllEnableBeds(building_name,ward_name);
		try {
			while (rs.next()) {
				model.addSelectionInterval(rs.getInt(2),rs.getInt(2));
				DisableIndex.add(rs.getString(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		EnabledJComboBoxRenderer1 enableRenderer = new EnabledJComboBoxRenderer1(model);
		bedNoCB.setRenderer(enableRenderer);
		bedNoCB.setSelectedIndex(0); 
	}

	public void getWardDetail() {
		wardInchargeLB.setText("");
		wardRoomNoLB.setText("");
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData(ward_code);
		try {
			while (resultSet.next()) {
				wardRoomNoLB.setText(resultSet.getObject(1).toString());
				wardInchargeLB.setText(resultSet.getObject(2).toString());
				wardCategorySTR = resultSet.getObject(3).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public JDateChooser getChangeDate() {
		return changeDate;
	}

	public JTimeChooser getChangeTime() {
		return changeTime;
	}
	public void getWardCharges(String deptName) {
		System.out.println("arun1 "+ deptName);
		WardsManagementDBConnection db = new WardsManagementDBConnection();
		ResultSet resultSet = db.retrieveWardCharges(deptName);
		try {
			while (resultSet.next()) {
				ward_chargesTF.setText(resultSet.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
	
	public class EnabledJComboBoxRenderer1 extends BasicComboBoxRenderer{
		static final long serialVersionUID = -984932432414L;
		private final ListSelectionModel enabledItems;
		private Color disabledColor = Color.lightGray;
		/**
		 * Constructs a new renderer for a JComboBox which enables/disables items
		 * based upon the parameter model.
		 * @param enabled
		 */
		public EnabledJComboBoxRenderer1(ListSelectionModel enabled){
			super();
			this.enabledItems = enabled;
		}
		/**
		 * Sets the color to render disabled items. 
		 * @param disabledColor
		 */
		public void setDisabledColor(Color disabledColor){
			this.disabledColor = disabledColor;
		}
		/**
		 * Custom implementation to color items as enabled or disabled. 
		 */
		@Override
		public Component getListCellRendererComponent(JList list,
				Object value,
				int index,
				boolean isSelected,
				boolean cellHasFocus) {
			Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if ( !enabledItems.isSelectedIndex(index) ){//not enabled
				if ( isSelected ){
					c.setBackground(UIManager.getColor("ComboBox.background"));
				}else{
					c.setBackground(super.getBackground());
				}
				c.setForeground(disabledColor);
			}else{
				c.setBackground(super.getBackground());
				c.setForeground(super.getForeground());
			}
			return c;
		}
	}
	
}