package hms.doctor.gui;

import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.opd.database.OPDDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.database.PaymentDBConnection;
import hms.patient.slippdf.AdvancePaymentSlippdf;
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
import java.awt.Color;
import javax.swing.border.LineBorder;

public class OPDMedDiscount extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Timer timer,timer1;
	String[] data = new String[15];
	private JTextField insuranceTypeTB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	int selectedRowIndex;
	ButtonGroup opdTypeGP = new ButtonGroup();
	ButtonGroup DiscountType = new ButtonGroup();
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String ipd_doctor_id = "", ipd_doctorname = "", opd_date = "", 
			opd_time = "", ipd_note = "", opd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", per = "(%) Percentage should be less than 100 or equal.";
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
	private JTextField opdDoctorTB;
	private JTextField totalAmountTB;
	private JTextField chargesTB;
	private JTextField opdNoTB;
	private JLabel noteLBL;
	private JTextField searchOPDTF;
	private boolean Ins_Upd=false;
	private JButton btnNewButton_3;
	private int id;
	private JLabel lblTotalAmount;


	public static void main(String[] arg) {
		//		String formattednumber = "$" + String.valueOf("11001");
		//		System.out.println(NumberToWordConverter.convert(22190));
		new OPDMedDiscount().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public OPDMedDiscount() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OPDMedDiscount.class.getResource("/icons/rotaryLogo.png")));
		setTitle("OPD Medicine Discount");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 689, 417);

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
		panel_4.setBounds(10, 5, 663, 371);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		
		opd_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(20, 57, 313, 262);
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

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(486, 297, 160, 25);
		panel_4.add(totalAmountTB);
		//		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(353, 19, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(OPDMedDiscount.class.getResource("/icons/SAVE.PNG")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select patient",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(opdNoTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"For Today patient has no OPD",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}if(Integer.parseInt(chargesTB.getText())>100) {
					JOptionPane.showMessageDialog(null,
							"Discount Percentage should be less than 100 or equal!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				
				String[] age=p_age.split("-");
				if(age.length>2) {
					if(!age[0].equals("0")) {
						p_age=age[0]+" Y";
					}else if(!age[1].equals("0")) {
						p_age=age[1]+" M";
					}else {
						p_age=age[2]+" D";
					}
				}
				data[0] = ""+ opdNoTB.getText();
				data[1] = ""+ p_id;
				data[2] = ""+ opdDoctorTB.getText();
				data[3] = ""+chargesTB.getText();
				data[4] = DoctorMain.DoctorNameSTR;//DOC NAME
				data[5] = DoctorMain.DoctorIDSTR;//DOC ID
				data[6] = ""+patientNameTB.getText();
				data[7] = ""+p_sex;
				data[8] = ""+p_age;
				data[9] = ""+telephoneTB.getText();

				OPDDBConnection OPDDBConnection=new OPDDBConnection();
				try {
					if(Ins_Upd) {
						data[0]= ""+id;
						OPDDBConnection.UpdateOPDMedDiscount(data);
						OPDDBConnection.closeConnection();
					}else {
						OPDDBConnection.InsertOPDMedDiscount(data);
						OPDDBConnection.closeConnection();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Data Saved Successfully.",
						"Success", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		btnNewButton_3.setBounds(343, 335, 147, 36);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(500, 335, 146, 36);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(OPDMedDiscount.class
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
				opdNoTB.setText("");
				opdDoctorTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				// System.out.println(p_id + "    "
				// + patientIDCB.getSelectedIndex());
				setPatientDetail(p_id);
				if (patientID.getSize() > 0) {
					patientNameTB.setText(p_name);
					addressTB.setText(p_address);
					ageTB.setText(p_age);
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

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(343, 64, 316, 221);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(10, 46, 108, 14);
		panel_7.add(lblDoctorName);

		opdDoctorTB = new JTextField();
		opdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdDoctorTB.setEditable(false);
		opdDoctorTB.setColumns(10);
		opdDoctorTB.setBounds(110, 41, 182, 25);
		panel_7.add(opdDoctorTB);

		opdNoTB = new JTextField();
		opdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdNoTB.setEditable(false);
		opdNoTB.setColumns(10);
		opdNoTB.setBounds(110, 10, 182, 25);
		panel_7.add(opdNoTB);

		JLabel lblOpdNo = new JLabel("OPD No :");
		lblOpdNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpdNo.setBounds(10, 15, 108, 14);
		panel_7.add(lblOpdNo);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 90, 294, 93);
		panel_7.add(panel_1);
		panel_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Discount", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_1.setLayout(null);

		chargesTB = new JTextField();
		chargesTB.setBounds(107, 36, 175, 25);
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
		//		chargesTB.setHorizontalAlignment(SwingConstants.TRAILING);
		chargesTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chargesTB.setColumns(10);
		
		JLabel lblTotalAmount_1 = new JLabel("Discount (%):");
		lblTotalAmount_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTotalAmount_1.setBounds(12, 41, 96, 14);
		panel_1.add(lblTotalAmount_1);
		chargesTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				fun();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				fun();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				fun();
			}

			private void fun() {
				// TODO Auto-generated method stub
				String str = chargesTB.getText() + "";
				totalAmountTB.setText(str);
				if(!str.equals("") &&Integer.parseInt(str)>100) {
					noteLBL.setVisible(true);
				}else
					noteLBL.setVisible(false);
			}
		});

		noteLBL = new JLabel(per);
		noteLBL.setForeground(Color.RED);
		noteLBL.setFont(new Font("Dialog", Font.ITALIC, 11));
		noteLBL.setBounds(10, 198, 296, 15);
		panel_7.add(noteLBL);
		noteLBL.setVisible(false);


		lblTotalAmount = new JLabel("Discount (%):");
		lblTotalAmount.setBounds(351, 301, 108, 14);
		panel_4.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton searchBT_1 = new JButton("");
		searchBT_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String str = searchOPDTF.getText() + "";
				if (!str.equals("")) {
					getPatientsOPD(searchOPDTF.getText());
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
					opdNoTB.setText("");
					opdDoctorTB.setText("");
					btnNewButton_3.setText("Save");
					chargesTB.setText("");
					noteLBL.setText(per);

				}
			}
		});
		searchBT_1.setToolTipText("Search");
		searchBT_1.setFocusable(true);
		searchBT_1.setIcon(new ImageIcon(OPDMedDiscount.class.getResource("/icons/zoom_r_button.png")));
		searchBT_1.setBounds(287, 17, 28, 25);
		panel_4.add(searchBT_1);

		searchOPDTF = new JTextField();
		searchOPDTF.setToolTipText("Search Patient");
		searchOPDTF.setFont(new Font("Dialog", Font.PLAIN, 12));
		searchOPDTF.setColumns(10);
		searchOPDTF.setBounds(105, 18, 182, 25);
		panel_4.add(searchOPDTF);
		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchOPDTF.getText() + "";

				if (!str.equals("")) {
					getPatientsOPD(searchOPDTF.getText());
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
					opdDoctorTB.setText("");
					opdNoTB.setText("");
					btnNewButton_3.setText("Save");
					chargesTB.setText("");
					noteLBL.setText(per);
				}
			}
		});
		searchOPDTF.getDocument().addDocumentListener(
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

		JLabel lblOpdNo_1 = new JLabel("OPD No :");
		lblOpdNo_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblOpdNo_1.setBounds(20, 23, 77, 14);
		panel_4.add(lblOpdNo_1);

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
		searchOPDTF.requestFocus(true);
	}

	public void getPatientsOPD(String opdID) {
		OPDDBConnection db = new OPDDBConnection();
		ResultSet resultSet = db.retrievepID(opdID);
		try {
			while (resultSet.next()) {
				p_id = resultSet.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getPatientsID(p_id);
		db.closeConnection();
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
				getOPDDATA();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
	}

	public void getOPDDATA() {

		OPDDBConnection db = new OPDDBConnection();
		ResultSet resultSet = db.retrieveOPDDATA(searchOPDTF.getText());
		try {
			while (resultSet.next()) {
				opd_id = resultSet.getObject(1).toString();
				opdNoTB.setText(opd_id);
				opdDoctorTB.setText("" + resultSet.getObject(2));
				opd_date = resultSet.getObject(4).toString();
				opd_time = resultSet.getObject(5).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		Ins_Upd=getLastDiscount(opd_id);
		if(Ins_Upd)
			btnNewButton_3.setText("Update");
		else
			btnNewButton_3.setText("Save");


	}
	public boolean getLastDiscount(String opdID) {

		OPDDBConnection db = new OPDDBConnection();
		ResultSet rs = db.retrieveOPDMedDiscount(opdID);
		try {
			while (rs.next()) {
				id=rs.getInt(1);
				totalAmountTB.setText(rs.getString(2));
				chargesTB.setText(rs.getString(2));
				if(rs.getBoolean(3)) {
					btnNewButton_3.setEnabled(false);
					noteLBL.setText("    Bill has been generated.");
					noteLBL.setVisible(true);
				}else {
					btnNewButton_3.setEnabled(true);
					noteLBL.setText(per);
					noteLBL.setVisible(false);
				}
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return false;
	}
}
