package hms.accounts.gui;

import hms.departments.gui.DepartmentMain;
import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.reception.gui.ReceptionMain;
import hms.store.database.ProceduresDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
import java.awt.Color;

public class IPDBillAccounts extends JDialog {

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
	private JComboBox hospitalSchemeCB;
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
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id,
			package_id = "N/A",p_scheme="";
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	final DefaultComboBoxModel chargesHead = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	Vector<String> expensesIndexVector = new Vector<String>();
	Vector<Integer> chargeMultiplication = new Vector<Integer>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTable table;
	private JTextField amountTB;
	private JTextField balanceTB;
	private JTextField ipdNoTB;
	private JTextField SurgicalCodeTb;
	private JTextField TFApprovedAmnt;
	Vector<String> itemsCombo = new Vector<String>();
	
	private JComboBox paymentModeCB;
	private JTextField operatioNameTB;
	private JTextField packageAmountTB;
	private JTextField packageTypeTB;

	public static void main(String[] arg) {
		new IPDBillAccounts().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDBillAccounts() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDBillAccounts.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New IPD Bill");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1040, 526);

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
		panel_4.setBounds(6, 0, 1018, 559);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		JLabel lblAttendenceDate = new JLabel("Date");
		lblAttendenceDate.setBounds(20, 19, 51, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(356, 19, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(469, 14, 182, 25);
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
							ipdNoTB.setText("");
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							advancePaymentTB.setText("");
							packageTypeTB.setText("");
							operatioNameTB.setText("");
							packageAmountTB.setText("");
							populateExpensesTable("0");
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
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							ipdNoTB.setText("");
							advancePaymentTB.setText("");
							packageTypeTB.setText("");
							operatioNameTB.setText("");
							packageAmountTB.setText("");
							populateExpensesTable("0");
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
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdNoTB.setText("");
							ipdDoctorTB.setText("");
							advancePaymentTB.setText("");
							packageTypeTB.setText("");
							operatioNameTB.setText("");
							packageAmountTB.setText("");
							populateExpensesTable("0");
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
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdNoTB.setText("");
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					packageTypeTB.setText("");
					operatioNameTB.setText("");
					packageAmountTB.setText("");
					populateExpensesTable("0");
				}
			}
		});
		searchBT.setBounds(651, 13, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(IPDBillAccounts.class
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
		panel.setBounds(703, 43, 313, 288);
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

		JLabel lblNote = new JLabel("Scheme");
		lblNote.setBounds(6, 258, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 222, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);
		hospitalSchemeCB = new JComboBox();
		hospitalSchemeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) hospitalSchemeCB.getSelectedItem();
				p_scheme = str;
			}
		});
		hospitalSchemeCB.setModel(new DefaultComboBoxModel(new String[] {
				"N/A", "Sneh Sparsh", "Karuna" }));
		hospitalSchemeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		hospitalSchemeCB.setBounds(106, 256, 201, 18);
		panel.add(hospitalSchemeCB);
		
		JLabel label_5 = new JLabel("Has Insurance :");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(6, 231, 108, 14);
		panel.add(label_5);
		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(710, 16, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(20, 51, 349, 427);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);
	

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 11, 320, 405);
		panel_2.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, }, new String[] {
						"Description", "Amount" }) {
			Class[] columnTypes = new Class[] { Object.class, Integer.class };

			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { true, false };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				});
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		scrollPane.setViewportView(table);

		JButton btnNewButton_3 = new JButton("Generate Bill");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				ward_name=ipdWardTB.getText().toString();
				double payment = Double.parseDouble("0"+amountTB.getText());
				double balance=Double.parseDouble(balanceTB.getText());
				if(balance!=0)
				{
					JOptionPane.showMessageDialog(null,
							"IPD balance should be Zero", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;

				}
				String insurance=insuranceTypeTB.getText().toString();
				if(!insurance.equals("Unknown")){
					
					if (SurgicalCodeTb.getText().equals("")){
						JOptionPane.showMessageDialog(null,
								"Please Enter Surgical Code", "Input Error",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					if (TFApprovedAmnt.getText().equals("")){
						JOptionPane.showMessageDialog(null,
								"Please Enter Approved Amount", "Input Error",
								JOptionPane.INFORMATION_MESSAGE);
						return;
					}
					
					
				

				}
				if (!amountTB.getText().equals("")
						&& !ipdNoTB.getText().equals("")) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									IPDBillAccounts.this,
									"Are you sure to generate bill and discharge patient",
									"Discharge", dialogButton);
					if (dialogResult == 0) {
						IPDDBConnection db = new IPDDBConnection();

						String bill_no = db.retrieveBillNo();
						bill_no = (Integer.parseInt(bill_no) + 1) + "";
						try {
							db.updateDataBillNo(bill_no);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							db.closeConnection();
						}

						
						double finalPayment = payment;
						if (paymentModeCB.getSelectedIndex() > 0) {
							double t = payment
									* Double.parseDouble(paymentCharges.get(
											paymentModeCB.getSelectedIndex())
											.toString());
							finalPayment = Math.round(t * 100.0) / 100.0;
						}
						long timeInMillis = System.currentTimeMillis();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTimeInMillis(timeInMillis);
						SimpleDateFormat timeFormat = new SimpleDateFormat(
								"hh:mm:ss a");
						String data[] = new String[15];
						data[0] = totalAmountTB.getText() + "";
						data[1] = balanceTB.getText() + "";
						data[2] = finalPayment + "";
						data[3] = "Yes";
						data[4] = ""
								+ DateFormatChange
										.StringToMysqlDate(new Date());
						data[5] = "" + timeFormat.format(cal1.getTime());
						data[6] = "Yes";
						data[7] = "" + bill_no;
						data[8] = "" + ReceptionMain.receptionNameSTR;
						data[9] = "" + ipd_id;
						data[10] = "" + p_scheme;
						data[11]=SurgicalCodeTb.getText()+"";
						data[12]=TFApprovedAmnt.getText()+"";

						try {
							db.updateData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
						data[0] = ""+ DateFormatChange.StringToMysqlDate(new Date());
						data[1] = "" + timeFormat.format(cal1.getTime());
						data[2] = "" +ipd_id;
						data[3] = ward_name;
						
						try {
							db.updateWardData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						db.closeConnection();
						if (paymentModeCB.getSelectedIndex() > 0) {
							try {
								PaymentDBConnection dbConnection1 = new PaymentDBConnection();

								double roundOff = Math
										.round((finalPayment - payment) * 100.0) / 100.0;
								data[0] = paymentModeCB.getSelectedItem()
										.toString();
								data[1] = "IPD BILL";
								data[2] = ipd_id + "";
								data[3] = p_id;
								data[4] = p_name;
								data[5] = payment + "";
								data[6] = roundOff + "";
								data[7] = finalPayment + "";
								data[8] = DateFormatChange
										.StringToMysqlDate(new Date());
								data[9] = ""
										+ timeFormat.format(cal1.getTime());
								data[10] = "" + ReceptionMain.receptionNameSTR;
								dbConnection1.inserData(data);
								dbConnection1.closeConnection();
							} catch (Exception e) {
								// TODO: handle exception
							}

						}
						WardsManagementDBConnection wardsManagementDBConnection = new WardsManagementDBConnection();
						data[0] = "0";
						try {
							wardsManagementDBConnection.updateDataUnfill(data,
									ward_code);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							wardsManagementDBConnection.closeConnection();
						}
						wardsManagementDBConnection.closeConnection();
						JOptionPane.showMessageDialog(null,
								"Click ok to continue", "Bill Generation",
								JOptionPane.INFORMATION_MESSAGE);

						dispose();
						try {
							new IPDBillSlippdf(bill_no, ipd_id, ipdDoctorTB
									.getText(),false);
						} catch (DocumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Please select patient or enter payment amount",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnNewButton_3.setBounds(710, 452, 121, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(863, 452, 121, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IPDBillAccounts.class
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
				ipdBuildingTB.setText("");
				ipdNoTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");
				bedDaysTB.setText("");
				ipdDoctorTB.setText("");
				packageTypeTB.setText("");
				operatioNameTB.setText("");
				packageAmountTB.setText("");
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

				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(790, 8, 218, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(377, 50, 316, 211);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(10, 46, 108, 14);
		panel_7.add(lblDoctorName);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_7.add(ipdDoctorTB);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuilding.setBounds(10, 77, 108, 14);
		panel_7.add(lblBuilding);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_7.add(ipdBuildingTB);

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(10, 113, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_7.add(ipdWardTB);

		JLabel lblBedNo = new JLabel("Bed No :");
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNo.setBounds(10, 149, 108, 14);
		panel_7.add(lblBedNo);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 144, 182, 25);
		panel_7.add(ipdBedNoTB);

		JLabel lblNoOfDays = new JLabel("No. of Days :");
		lblNoOfDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNoOfDays.setBounds(10, 180, 108, 14);
		panel_7.add(lblNoOfDays);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 175, 182, 25);
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

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Bill",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(377, 258, 317, 244);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(10, 47, 108, 14);
		panel_1.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(153, 42, 147, 25);
		panel_1.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		JLabel lblAdvancePayment = new JLabel("Advance Payment :");
		lblAdvancePayment.setBounds(10, 83, 108, 14);
		panel_1.add(lblAdvancePayment);
		lblAdvancePayment.setFont(new Font("Tahoma", Font.PLAIN, 12));

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(152, 78, 148, 25);
		panel_1.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);

		amountTB = new JTextField();
		amountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		amountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		amountTB.setColumns(10);
		amountTB.setBounds(153, 114, 148, 25);
		panel_1.add(amountTB);
		amountTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		amountTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}
		});

		JLabel lblEnterPayment = new JLabel("Enter Payment :");
		lblEnterPayment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterPayment.setBounds(10, 119, 108, 14);
		panel_1.add(lblEnterPayment);

		balanceTB = new JTextField();
		balanceTB.setHorizontalAlignment(SwingConstants.TRAILING);
		balanceTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		balanceTB.setEditable(false);
		balanceTB.setColumns(10);
		balanceTB.setBounds(153, 150, 148, 25);
		panel_1.add(balanceTB);

		JLabel lblBalance = new JLabel("Balance :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBalance.setBounds(10, 155, 108, 14);
		panel_1.add(lblBalance);

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
		paymentModeCB.setBounds(153, 11, 147, 25);
		panel_1.add(paymentModeCB);

		JLabel label = new JLabel("Payment Mode :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 11, 92, 25);
		panel_1.add(label);
		JLabel lblSurgical = new JLabel("Surgical Code");
		lblSurgical.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSurgical.setBounds(10, 180, 108, 14);
		panel_1.add(lblSurgical);
		
		SurgicalCodeTb = new JTextField();
		SurgicalCodeTb.setHorizontalAlignment(SwingConstants.TRAILING);
		SurgicalCodeTb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		SurgicalCodeTb.setColumns(10);
		SurgicalCodeTb.setBounds(153, 186, 148, 21);
		panel_1.add(SurgicalCodeTb);
		
		TFApprovedAmnt = new JTextField();
		TFApprovedAmnt.setHorizontalAlignment(SwingConstants.TRAILING);
		TFApprovedAmnt.setFont(new Font("Tahoma", Font.PLAIN, 12));
		TFApprovedAmnt.setColumns(10);
		TFApprovedAmnt.setBounds(152, 212, 148, 21);
		panel_1.add(TFApprovedAmnt);
		
		JLabel Appro = new JLabel("Approved Amount");
		Appro.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Appro.setBounds(10, 216, 108, 14);
		panel_1.add(Appro);
		
		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Package Details", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(0, 0, 0)));
		panel_3.setBounds(699, 333, 309, 119);
		panel_4.add(panel_3);
		
		JLabel label_2 = new JLabel("Operation Name :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 56, 108, 14);
		panel_3.add(label_2);
		
		operatioNameTB = new JTextField();
		operatioNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		operatioNameTB.setEditable(false);
		operatioNameTB.setColumns(10);
		operatioNameTB.setBounds(110, 51, 182, 25);
		panel_3.add(operatioNameTB);
		
		JLabel label_3 = new JLabel("Amount :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(10, 88, 108, 14);
		panel_3.add(label_3);
		
		packageAmountTB = new JTextField();
		packageAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		packageAmountTB.setEditable(false);
		packageAmountTB.setColumns(10);
		packageAmountTB.setBounds(110, 81, 182, 25);
		panel_3.add(packageAmountTB);
		
		packageTypeTB = new JTextField();
		packageTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		packageTypeTB.setEditable(false);
		packageTypeTB.setColumns(10);
		packageTypeTB.setBounds(110, 20, 182, 25);
		panel_3.add(packageTypeTB);
		
		JLabel label_4 = new JLabel("Package Type :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(10, 25, 108, 14);
		panel_3.add(label_4);

	

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
	
	}

	public void getPatientsID(String index) {
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
		ipdBuildingTB.setText("");
		ipdNoTB.setText("");
		ipdWardTB.setText("");
		ipdBedNoTB.setText("");
		bedDaysTB.setText("");
		packageTypeTB.setText("");
		operatioNameTB.setText("");
		packageAmountTB.setText("");
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
	
		package_id = "N/A";
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
				advancePaymentTB.setText(ipd_advance + "");
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				package_id = resultSet.getObject(10).toString();
				packageTypeTB.setText("" + resultSet.getObject(11));
				operatioNameTB.setText("" + resultSet.getObject(12));
				packageAmountTB.setText("" + resultSet.getObject(13));
				hospitalSchemeCB.setSelectedItem("" + resultSet.getObject(15));
				SurgicalCodeTb.setText("" + resultSet.getObject(16));
				TFApprovedAmnt.setText("" + resultSet.getObject(17));
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
				getIPDDoctors(ipd_id);
				populateExpensesTable(ipd_id);

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void getAllPaymentModes() {
		paymentModes.addElement("CASH");
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
	}

	public void populateExpensesTable(String ipd_id) {

		totalCharges = 0;
		expensesIndexVector.clear();
		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllData(ipd_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllData(ipd_id);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				expensesIndexVector.add(rs.getObject(1).toString());
				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Description", "Amount" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			for (int i = 0; i < NumberOfRows; i++) {
				totalCharges = totalCharges
						+ Double.parseDouble(Rows_Object_Array[i][1].toString());
			}
			totalCharges = Math.round(totalCharges);

			totalCharges = (int) (totalCharges);
			totalAmountTB.setText(totalCharges + "");
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(0).setMinWidth(150);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			ipd_balance = totalCharges - ipd_advance;

			if (!amountTB.getText().equals("")) {
				ipd_balance = ipd_balance
						- Float.parseFloat(amountTB.getText().toString());
			}
			balanceTB.setText(ipd_balance + "");
			
		} catch (SQLException ex) {
			Logger.getLogger(IPDBillAccounts.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		IPDDBConnection ipddbConnection = new IPDDBConnection();
		try {
			ipddbConnection.updateTotalAmount(ipd_id, totalCharges + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ipddbConnection.closeConnection();
			e.printStackTrace();
		}
		ipddbConnection.closeConnection();
	}
	
	private int round(double d) {
		double dAbs = Math.abs(d);
		int i = (int) dAbs;
		double result = dAbs - (double) i;
		if (result < 0.5) {
			return d < 0 ? -i : i;
		} else {
			return d < 0 ? -(i + 1) : i + 1;
		}
	}
}
