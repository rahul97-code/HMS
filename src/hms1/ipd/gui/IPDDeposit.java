package hms1.ipd.gui;

import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.AdvancePaymentSlippdf;
import hms.payments.PaymentMain;
import hms.reception.gui.ReceptionMain;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;

import java.awt.Component;
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
import javax.swing.JTextField;
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
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

public class IPDDeposit extends JDialog {

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
	private Timer timer;
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
			ward_room = "", ward_code = "", descriptionSTR = "ADVANCE",date="",time="",
			charges = "", ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	final DefaultComboBoxModel paymentModes = new DefaultComboBoxModel();
	ArrayList paymentCharges = new ArrayList();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	public static Font customFont;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTextField chargesTB;
	private JComboBox chargesTypeCB;
	private JTextField ipdNoTB;
	private JComboBox paymentModeCB;
	private JTextField receiptIDTF;
	private JLabel lblReceiptNo;
	private JTextField approvedByTF;
	private JLabel lblApprovedBy;
	private boolean alreadyPaid;
	private String req_urn;
	private double finalCharges;
	private double totalPaidPayment;
	private String payMode;

	public static void main(String[] arg) {
		String formattednumber = "$" + String.valueOf("11001");
		System.out.println(NumberToWordConverter.convert(22190));
		new IPDDeposit().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDDeposit() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDDeposit.class.getResource("/icons/rotaryLogo.png")));
		setTitle("IPD PAYMENTS");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 689, 530);

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
		GeneralDBConnection db=new GeneralDBConnection();
		date=db.getCurrentDate();
		time=db.getCurrentTime();
		db.closeConnection();

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_4.setBounds(10, 5, 663, 486);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(20, 25, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(133, 20, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchPatientTB.getText() + "";

				if (!str.equals("")) {
					getPatientsID(searchPatientTB.getText());
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
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					ipdNoTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable("0");
				}
			}
		});

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
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
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
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable("0");
				}
			}
		});
		searchBT.setBounds(315, 19, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(IPDDeposit.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(20, 58, 313, 262);
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
		lblPatientId.setBounds(353, 19, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		final JButton btnNewButton_3 = new JButton("Generate Slip");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(chargesTypeCB.getSelectedItem().equals("KARUN SPARSH") && receiptIDTF.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter receipt no.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(chargesTypeCB.getSelectedItem().equals("KARUN SPARSH") &&   approvedByTF.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Approved By.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!chargesTB.getText().equals("")	&& !ipdNoTB.getText().equals("")) {

					try {
						if(!alreadyPaid) {
						 payMode=paymentModeCB.getSelectedItem().toString();
						 finalCharges=Double.parseDouble(chargesTB.getText());
						 }
						int payModeIndex=paymentModeCB.getSelectedIndex();
						if(!alreadyPaid && (payModeIndex!=0 && payModeIndex!=2)) {		
							btnNewButton_3.setEnabled(false);	
							PaymentMain MachinePayDialog=new PaymentMain(new String[]{chargesTB.getText()+"",p_id,p_name,ReceptionMain.receptionNameSTR,descriptionSTR},"IPD PAYMENT");
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
						


						IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
						long timeInMillis = System.currentTimeMillis();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTimeInMillis(timeInMillis);
						SimpleDateFormat timeFormat = new SimpleDateFormat(
								"hh:mm:ss a");
						String type = "P";
						String data[] = new String[20];
						int advancePaymentIndex = 0;
						if (chargesTypeCB.getSelectedIndex() == 1) {
							type = "R";
						}else if(chargesTypeCB.getSelectedItem().equals("KARUN SPARSH")) {
							type = "KS";
						}
						if(chargesTypeCB.getSelectedItem().equals("KARUN SPARSH")){
							db=new IPDPaymentsDBConnection();
							data[0] = "" + ipd_id;
							data[1] = "" + p_id;
							data[2] = "" + p_name;
							data[3] = "" + telephoneTB.getText();
							data[4] = "" + ageTB.getText().split("-")[0];
							data[5] = "" + totalAmountTB.getText();
							data[6] = "" + ipdDoctorTB.getText();
							data[7] = "" + receiptIDTF.getText(); 
							data[8] = "" + chargesTB.getText(); 
							data[9] = "" + approvedByTF.getText(); 
							data[10] = "" + ReceptionMain.receptionNameSTR;
							data[11] = ""+ ReceptionMain.receptionIdSTR;
							db.inserData1(data);
							db.closeConnection();
						}
						db=new IPDPaymentsDBConnection();
						data[0] = "" + ipd_id;
						data[1] = "" + p_id;
						data[2] = "" + p_name;
						data[3] = "" + type;
						data[4] = "" + descriptionSTR;
						data[5] = "" + chargesTB.getText();
						data[6] = date;
						data[7] = time;
						data[8] = "" + ReceptionMain.receptionNameSTR;
						advancePaymentIndex = db.inserData(data);		
						db.closeConnection();	
//						JOptionPane.showMessageDialog(null, "Click ok to continue",
//								"Slip Generation", JOptionPane.INFORMATION_MESSAGE);
						dispose();
						if (type.equals("R")) {
							double finalPayment = ipd_advance
									- Double.parseDouble(chargesTB.getText());
							IPDDBConnection ipddbConnection = new IPDDBConnection();
							ipddbConnection.updateAdvanceAmount(ipd_id,finalPayment + "");
							ipddbConnection.closeConnection();
							new AdvancePaymentSlippdf(ipd_doctorname, chargesTB
									.getText().toString(), ipdNoTB.getText(),
									p_name, advancePaymentIndex + "",
									"Return Amount");
						} else {
							double finalPayment = ipd_advance
									+ Double.parseDouble(chargesTB.getText());
							IPDDBConnection ipddbConnection = new IPDDBConnection();
							ipddbConnection.updateAdvanceAmount(ipd_id,	finalPayment + "");
							ipddbConnection.closeConnection();
							double charges=Double.parseDouble(chargesTB
									.getText().toString());
							finalPayment=charges;
							if(payModeIndex!=0) {
								PaymentDBConnection dbConnection = new PaymentDBConnection();	
								double roundOff = Math.round((finalCharges- charges) * 100.0) / 100.0;
								data[0] =payMode;
								data[1] = "IPD ADVANCE";
								data[2] = advancePaymentIndex + "";
								data[3] = p_id;
								data[4] = p_name;
								data[5] = charges + "";
								data[6] = roundOff + "";
								data[7] = finalCharges+"";
								data[8] =  date;
								data[9] = time;
								data[10] = "" + ReceptionMain.receptionNameSTR;	
								data[11] =  req_urn;	
								dbConnection.inserData(data);
								dbConnection.closeConnection();
							}
							if(!chargesTypeCB.getSelectedItem().equals("KARUN SPARSH")){
								new AdvancePaymentSlippdf(ipd_doctorname, finalCharges+"", ipdNoTB.getText(),
										p_name, advancePaymentIndex + "",
										"Advance Payment");
							}
							JOptionPane.showMessageDialog(null,
									"Data Saved Successfully.",
									"Success", JOptionPane.INFORMATION_MESSAGE);
						}

					} catch (Exception e) {
						// TODO: handle exception
					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Please select patient or enter Payment",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		btnNewButton_3.setBounds(342, 419, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(499, 419, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IPDDeposit.class
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
				ipdDoctorTB.setText("");
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
		patientIDCB.setBounds(430, 17, 218, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(343, 64, 316, 344);
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

		JLabel lblAdvancePayment = new JLabel("Advance Payment :");
		lblAdvancePayment.setBounds(20, 241, 137, 14);
		panel_7.add(lblAdvancePayment);
		lblAdvancePayment.setFont(new Font("Tahoma", Font.PLAIN, 12));

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(155, 236, 137, 25);
		panel_7.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(20, 210, 108, 14);
		panel_7.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(155, 206, 137, 25);
		panel_7.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		receiptIDTF = new JTextField();
		receiptIDTF.setVisible(false);
		receiptIDTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		receiptIDTF.setColumns(10);
		receiptIDTF.setBounds(111, 273, 182, 25);
		receiptIDTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		panel_7.add(receiptIDTF);

		lblReceiptNo = new JLabel("Receipt No. :");
		lblReceiptNo.setVisible(false);
		lblReceiptNo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblReceiptNo.setBounds(10, 278, 108, 14);
		panel_7.add(lblReceiptNo);

		approvedByTF = new JTextField();
		approvedByTF.setVisible(false);
		approvedByTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		approvedByTF.setColumns(10);
		approvedByTF.setBounds(111, 307, 182, 25);
		panel_7.add(approvedByTF);

		lblApprovedBy = new JLabel("Approved By :");
		lblApprovedBy.setVisible(false);
		lblApprovedBy.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblApprovedBy.setBounds(10, 314, 108, 14);
		panel_7.add(lblApprovedBy);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Advance",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(16, 371, 320, 104);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		chargesTypeCB = new JComboBox();
		chargesTypeCB.setBounds(107, 16, 200, 25);
		panel_1.add(chargesTypeCB);
		chargesTypeCB.setModel(new DefaultComboBoxModel(new String[] {
				"ADVANCE", "RETURN", "PAYMENT","KARUN SPARSH" }));
		chargesTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectOrEnter = new JLabel("Select Desc :");
		lblSelectOrEnter.setBounds(6, 21, 135, 14);
		panel_1.add(lblSelectOrEnter);
		lblSelectOrEnter.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblCharges = new JLabel("Amount :");
		lblCharges.setBounds(6, 66, 108, 14);
		panel_1.add(lblCharges);
		lblCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));

		chargesTB = new JTextField();
		chargesTB.setBounds(107, 61, 200, 25);
		panel_1.add(chargesTB);
		chargesTB.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		chargesTB.setHorizontalAlignment(SwingConstants.TRAILING);
		chargesTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chargesTB.setColumns(10);

		paymentModeCB = new JComboBox();
		paymentModeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
			
			}
		});
		getAllPaymentModes();
		paymentModeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		paymentModeCB.setBounds(122, 331, 211, 25);
		panel_4.add(paymentModeCB);

		JLabel label = new JLabel("Payment Mode :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(20, 331, 92, 25);
		panel_4.add(label);

		chargesTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				descriptionSTR = (String) chargesTypeCB.getSelectedItem();
				int i=getPaymentModeCB().getSelectedIndex();
				if(chargesTypeCB.getSelectedIndex()==1 && (i!=0&&i!=2))
				{
			        JOptionPane.showMessageDialog(null, "Please choose another payment mode!", "Error", JOptionPane.ERROR_MESSAGE);
			        paymentModeCB.setSelectedIndex(0);
			        return ;
				}
				
				if(descriptionSTR.equals("KARUN SPARSH")) {
					receiptIDTF.setVisible(true);
					lblReceiptNo.setVisible(true);
					approvedByTF.setVisible(true);
					lblApprovedBy.setVisible(true);

					paymentModeCB.setEnabled(false);
					paymentModeCB.setSelectedIndex(0);
				}else {
					lblApprovedBy.setVisible(false);
					approvedByTF.setVisible(false);
					lblReceiptNo.setVisible(false);
					receiptIDTF.setVisible(false);
					paymentModeCB.setEnabled(true);
				}
				if(alreadyPaid)
					paymentModeCB.setEnabled(false);
			}
		});
		final JTextField tfListText = (JTextField) chargesTypeCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					descriptionSTR = text;
					System.out.println(descriptionSTR);
					// HERE YOU CAN WRITE YOUR CODE
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
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
				getIPDDoctors(ipd_id);
				populateExpensesTable(ipd_id);
			}
		} catch (SQLException e) {
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
		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveTotalIPDCharge(ipd_id);

			while (rs.next()) {
				totalCharges=rs.getDouble(1);
			}
			db.closeConnection();
			totalAmountTB.setText(totalCharges + "");
			ipd_balance = totalCharges - ipd_advance;

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	public void setAlreadyPayData(String reqUrn,double finalAmt,double WithoutChargeAmt,String PayMode,String pid,int payIndex,String desc) {
		alreadyPaid=true;
		req_urn=reqUrn;
		finalCharges=finalAmt;
		totalPaidPayment=WithoutChargeAmt;
		payMode=PayMode;
		paymentModeCB.setEnabled(false);
		paymentModeCB.setSelectedIndex(payIndex);
		searchPatientTB.setText(pid);
		searchPatientTB.setEnabled(false);
		chargesTypeCB.setSelectedItem(desc);
		chargesTypeCB.setEnabled(false);
		chargesTB.setText(WithoutChargeAmt+"");
		chargesTB.setEnabled(false);
		}
	public JComboBox getPaymentModeCB() {
		return paymentModeCB;
	}
}
