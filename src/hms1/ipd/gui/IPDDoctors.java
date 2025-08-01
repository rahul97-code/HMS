package hms1.ipd.gui;

import hms.doctor.database.DoctorDBConnection;

import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms1.ipd.database.IPDDBConnection;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
import javax.swing.JTextField;
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
import com.toedter.calendar.JDateChooser;

public class IPDDoctors extends JDialog {

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
	private Timer timer;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",enterDate="";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "0", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	Vector<String> expensesIndexVector = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JComboBox doctorNameCB;
	private JTable table;
	private JTextField ipdNoTB;
	private JLabel doctorSpecilization;
	private JLabel doctorRoom;
	private JComboBox AM_PMCB;
	private JComboBox MinCB;
	private JComboBox HourCB;
	Date h=new Date();
	private String last_doc_id;
	private String doc_start_date_time;

	public static void main(String[] arg) {
		String formattednumber = "$" + String.valueOf("11001");
		System.out.println(NumberToWordConverter.convert(22190));
		new IPDDoctors().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDDoctors() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDDoctors.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Change Doctor");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1040, 523);

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
		panel_4.setBounds(6, 5, 1018, 485);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		JLabel lblAttendenceDate = new JLabel("Date");
		lblAttendenceDate.setBounds(10, 19, 51, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(356, 19, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPatientsID(searchPatientTB.getText());

			}
		});
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
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdNoTB.setText("");
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
				}
			}});
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(469, 14, 182, 25);
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
					getPatientsID(str);}else {
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
						bedDaysTB.setText("");
					}
			}
		});
		searchBT.setBounds(651, 13, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(IPDDoctors.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(78, 14, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(699, 44, 313, 300);
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

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(20, 50, 349, 180);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);

		JLabel lblSelectOrEnter = new JLabel("Doctor");
		lblSelectOrEnter.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelectOrEnter.setBounds(10, 26, 61, 14);
		panel_2.add(lblSelectOrEnter);

		doctorNameCB = new JComboBox();
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorNameCB.setBounds(78, 21, 233, 25);
		panel_2.add(doctorNameCB);
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				ipd_doctorname = (String) doctorNameCB.getSelectedItem();

				getDoctorDetail(ipd_doctorname);
			}
		});

		JLabel label = new JLabel("Doctor Room :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 51, 108, 25);
		panel_2.add(label);

		JLabel label_1 = new JLabel("Doctor specialization :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 89, 141, 24);
		panel_2.add(label_1);

		doctorRoom = new JLabel("");
		doctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorRoom.setBounds(149, 53, 186, 23);
		panel_2.add(doctorRoom);

		doctorSpecilization = new JLabel("");
		doctorSpecilization.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorSpecilization.setBounds(149, 88, 186, 25);
		panel_2.add(doctorSpecilization);

		enterDate=DateFormatChange.StringToMysqlDate(new Date());
		JDateChooser dateFromDC = new JDateChooser();
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		dateFromDC.setBounds(13, 150, 116, 21);
		panel_2.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							enterDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		dateFromDC.setDate(new Date());		    

		HourCB = new JComboBox();
		HourCB.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
		HourCB.setSelectedIndex(0);
		HourCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		HourCB.setBounds(137, 150, 61, 21);
		panel_2.add(HourCB);


		HourCB.setSelectedIndex((h.getHours()>12?h.getHours()-12:h.getHours())-1);

		JLabel lblDate = new JLabel("Date ");
		lblDate.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDate.setBounds(73, 121, 75, 25);
		panel_2.add(lblDate);

		MinCB = new JComboBox();
		MinCB.setModel(new DefaultComboBoxModel(new String[] {"00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));
		MinCB.setSelectedIndex(0);
		MinCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		MinCB.setBounds(202, 150, 65, 21);
		panel_2.add(MinCB);
		MinCB.setSelectedIndex(h.getMinutes());

		JLabel lblTime = new JLabel("Time ");
		lblTime.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTime.setBounds(214, 121, 61, 25);
		panel_2.add(lblTime);

		AM_PMCB = new JComboBox();
		AM_PMCB.setModel(new DefaultComboBoxModel(new String[] {"AM", "PM"}));
		AM_PMCB.setSelectedIndex(0);
		AM_PMCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		AM_PMCB.setBounds(271, 150, 70, 21);
		panel_2.add(AM_PMCB);
		SimpleDateFormat amPmFormat = new SimpleDateFormat("a");
		String amOrPm = amPmFormat.format(new Date());
		AM_PMCB.setSelectedItem(amOrPm.toUpperCase());

		System.out.println(HourCB.getSelectedItem()+":"+MinCB.getSelectedItem()+":"+h.getMinutes()+" "+AM_PMCB.getSelectedItem());

		JButton btnNewButton_4 = new JButton("Close");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(721, 429, 273, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IPDDoctors.class
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
				ipdBuildingTB.setText("");
				ipdNoTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");
				bedDaysTB.setText("");

				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
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
		panel_7.setBounds(377, 50, 316, 180);
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
		lblWardName.setBounds(10, 81, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 76, 182, 25);
		panel_7.add(ipdWardTB);

		JLabel lblBedNo = new JLabel("Bed No :");
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNo.setBounds(10, 117, 108, 14);
		panel_7.add(lblBedNo);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 112, 182, 25);
		panel_7.add(ipdBedNoTB);

		JLabel lblNoOfDays = new JLabel("No. of Days :");
		lblNoOfDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNoOfDays.setBounds(10, 148, 108, 14);
		panel_7.add(lblNoOfDays);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 143, 182, 25);
		panel_7.add(bedDaysTB);

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
		scrollPane.setBounds(10, 241, 679, 234);
		panel_4.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null, null},
				},
				new String[] {
						"Doctor Name", "Start Date","Start Time","End Date","End Time"
				}
				) {

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

		JButton btnNewButton = new JButton("Save");
		btnNewButton.setIcon(new ImageIcon(IPDDoctors.class.getResource("/icons/SAVE.PNG")));
		btnNewButton.setBounds(721, 373, 273, 44);
		panel_4.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {


				if (ipd_doctorname.equals("Select Doctor") || ipd_id.equals("0")) {
					JOptionPane.showMessageDialog(null,
							"Please Select Doctor or Patient", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(ipd_doctorname.equals(table.getValueAt(table.getRowCount()-1,1)+"")) {
					JOptionPane.showMessageDialog(null,
							"Patient already under this doctor.", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} 

				String ChangeDateTime=enterDate+" "+HourCB.getSelectedItem()+":"+MinCB.getSelectedItem()+":"+h.getMinutes()+" "+AM_PMCB.getSelectedItem();
				SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
				Date change_date=null;
				Date ward_date=null;
				System.out.println("-----"+ChangeDateTime);
				System.out.println("-----"+doc_start_date_time);
				try {
					change_date=timeFormat1.parse(ChangeDateTime);
					ward_date=timeFormat1.parse(doc_start_date_time);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}	

				if(ward_date.after(change_date) || new Date().before(change_date))
				{
					JOptionPane.showMessageDialog(null,
							"please provide valid date and time!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				{
					ipddbConnection = new IPDDBConnection();
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					String[] data1 = new String[10];
					data1[0] = "" +ipd_id ;
					data1[1] = "" + ipd_doctorname;
					data1[2] = "" + ipd_doctor_id;
					data1[3] = "" + p_id;
					data1[4] = "" + p_name;
					data1[5] = "" + enterDate;
					data1[6] = "" + HourCB.getSelectedItem()+":"+MinCB.getSelectedItem()+":"+h.getMinutes()+" "+AM_PMCB.getSelectedItem();
					try {
						ipddbConnection.inserIPDDoctor(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						ipddbConnection.updateDoctorData(ipd_doctorname,ipd_id);
						ipddbConnection.updateDoctorEndTime(last_doc_id,enterDate,"" + HourCB.getSelectedItem()+":"+MinCB.getSelectedItem()+":"+h.getMinutes()+" "+AM_PMCB.getSelectedItem());
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ipddbConnection.closeConnection();
					populatedDoctorTable(ipd_id);

					JOptionPane.showMessageDialog(null,
							"Data saved successfully.", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
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
		searchPatientTB.requestFocus(true);
		getAllDoctors();
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
		bedDaysTB.setText("");
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

		ipd_id="0";
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
				ward_code = resultSet.getObject(9).toString();
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
				populatedDoctorTable(ipd_id);

			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void getIPDDoctors(String ipd_id) {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataDoctor(ipd_id);
		try {
			while (resultSet.next()) {

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void populatedDoctorTable(String ipd_id) {
		last_doc_id="";
		doc_start_date_time="";
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataDoctorNew(ipd_id);

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
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				last_doc_id=rs.getObject(1)+"";
				doc_start_date_time=rs.getObject(3)+" "+rs.getObject(4);
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Id","Doctor Name", "Start Date","Start Time","End Date","End Time" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};


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
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
	}
	public void getDoctorDetail(String name) {
		ipd_doctor_id = "";
		doctorSpecilization.setText("");
		doctorRoom.setText("");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				ipd_doctor_id = resultSet.getObject(1).toString();
				doctorSpecilization.setText(resultSet.getObject(2)
						.toString());
				doctorRoom.setText(resultSet.getObject(3).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
}
