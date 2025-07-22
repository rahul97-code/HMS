package hms.emailreports.gui;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.main.MarqueeLabel;
import hms.main.NewsDBConnection;
import hms.patient.database.PatientDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
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

public class EmailReports extends JDialog {

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
	
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	String dest = "";
	ButtonGroup opdTypeGP = new ButtonGroup();
	String mainDir = "";
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "",
			patient_age = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	Vector examID = new Vector();
	ImageIcon testHistoryIcon = new ImageIcon(
			EmailReports.class.getResource("/icons/list_button.png"));
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	ExamDBConnection examdbConnection;
	private JLabel timeLB;
	public String news = "";
	private JLabel lblLabName;
	PatientTestHistoryEmail testHistory = new PatientTestHistoryEmail();

	public static void main(String[] arg)
	{
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
		new EmailReports().setVisible(true);
	}
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public EmailReports() {
		setResizable(false);
		setTitle("Email Reports");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EmailReports.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(20, 5, 1093, 712);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setModal(true);
		contentPane.setLayout(null);
		testHistory.getComponent(contentPane);
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

		JLabel dateLB = new JLabel(DateFormatChange.StringToMysqlDate(new Date()));
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
		label_3.setIcon(new ImageIcon(EmailReports.class
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
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Exams",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 13), null));
		panel_4.setBounds(6, 136, 1076, 537);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		examdbConnection = new ExamDBConnection();
		examdbConnection.closeConnection();

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(10, 41, 103, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB = new JTextField();		searchPatientTB.addActionListener(new ActionListener() {			public void actionPerformed(ActionEvent e) {
				getPatientsID(searchPatientTB.getText());			}		});
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(113, 36, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (str.equals("")){
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
							testHistory.populateTestHistoryTable("0", "ooo");
						}
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
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
					testHistory.populateTestHistoryTable("0", "ooo");
				}
			}
		});
		searchBT.setBounds(295, 36, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(EmailReports.class
				.getResource("/icons/zoom_r_button.png")));

		JPanel panel = new JPanel();
		panel.setBounds(10, 140, 313, 283);
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
		lastOPDDateLB.setBounds(6, 255, 293, 19);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(10, 86, 77, 20);
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
				testHistory.populateTestHistoryTable("0", "ooo");
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
					testHistory.populateTestHistoryTable(p_id, p_name);
					
				}

			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(85, 82, 239, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "News", TitledBorder.RIGHT,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_7.setBounds(10, 434, 313, 72);
		panel_4.add(panel_7);
		panel_7.setLayout(null);
		NewsDBConnection newsDBConnection = new NewsDBConnection();
		JLabel lblEmployeeOfThe = new MarqueeLabel(newsDBConnection.getNews(),
				MarqueeLabel.RIGHT_TO_LEFT, 20);
		lblEmployeeOfThe.setForeground(Color.RED);
		lblEmployeeOfThe.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblEmployeeOfThe.setBounds(10, 21, 293, 40);
		panel_7.add(lblEmployeeOfThe);
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager
						.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
						TitledBorder.TOP, null, null));
		panel_1.setBounds(333, 13, 733, 513);
		panel_4.add(panel_1);
		
		JTabbedPane tabbedPane = new JTabbedPane(SwingConstants.TOP);
		tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
		tabbedPane.setSelectedIndex(-1);
		tabbedPane.setFont(new Font("Tahoma", Font.PLAIN, 12));
		tabbedPane.setBounds(6, 11, 717, 491);
		tabbedPane.addTab("Patient Test History", testHistoryIcon, testHistory,
				"Patient OPD History");
		
		
		panel_1.add(tabbedPane);
		
		JLabel lblDevelopedBy = new JLabel("Developed By : Sukhpal Saini");
		lblDevelopedBy.setBounds(36, 512, 287, 14);
		panel_4.add(lblDevelopedBy);
		
		
		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_6.setBounds(928, 69, 141, 61);
		contentPane.add(panel_6);
		panel_6.setLayout(null);

		JLabel label_5 = new JLabel("");
		label_5.setBounds(10, 6, 121, 44);
		panel_6.add(label_5);
		label_5.setIcon(new ImageIcon(EmailReports.class.getResource("/icons/email-marketing.jpg")));

		JPanel panel_10 = new JPanel();
		panel_10.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_10.setBounds(6, 69, 873, 61);
		contentPane.add(panel_10);
		panel_10.setLayout(null);

		lblLabName = new JLabel("Email Patient Reports");
		lblLabName.setBounds(6, 10, 847, 40);
		panel_10.add(lblLabName);
		lblLabName.setFont(new Font("Tahoma", Font.BOLD, 20));
		lblLabName.setHorizontalAlignment(SwingConstants.CENTER);
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

}
