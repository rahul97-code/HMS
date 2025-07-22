package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ProceduresDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

import java.awt.Color;
import java.awt.Container;
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
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OutdoorProcedureEntry extends JDialog {

	/**
	 * 
	 */
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private Timer timer;
	private JTextField telephoneTB;
	private JTextField ageTB;
	int packSize=0;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();

	private HashMap examHashMap = new HashMap();  
	String[] data = new String[22];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	double totalChargesIPD = 0;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemTypeV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> itemAvailableV = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	double ipd_advance = 0, ipd_balance = 0;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String doctorName = "", doctorID = "", date = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR1 = "", charges = "",
			ipd_days, ipd_houres, ipd_minutes, ipd_expenses_id;
	String procedureIDSTR, procedureNameSTR, itemDescSTR, taxTypeSTR,
	taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
	dueDateSTR = "", previouseStock = "",
	departmentName = DepartmentMain.departmentName,
	departmentID = DepartmentMain.departmentID;
	int qtyIssued = 0, afterIssued = 0, itemValue, finalTaxValue = 0,
			finalDiscountValue = 0;
	int quantity = 0;
	double price = 0, finalTotalValue = 0,taxValue = 0,surchargeValue = 0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel procedureName = new DefaultComboBoxModel();
	Vector<String> procedureIDV = new Vector<String>();
	Vector<String> procedureNameV = new Vector<String>();
	Vector<String> procedurePriceV = new Vector<String>();
	Vector<String> procedurePreDaysV = new Vector<String>();
	Vector<String> procedurePostDaysV = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JTable addTestTable_1;
	private JComboBox<String> procedureNameCB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField priceET;
	private JTextField predaysLBL;
	private JTextField postdaysLBL;
	private JTextField slipNoET;
	private JComboBox<String> doctorNameCB;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new OutdoorProcedureEntry().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public OutdoorProcedureEntry() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(
						OutdoorProcedureEntry.class
						.getResource("/icons/rotaryLogo.png")));
		setTitle("Patient Procedures Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(50, 70, 869, 632);
		setModal(true);
		departmentName = DepartmentMain.departmentName;
		departmentID = DepartmentMain.departmentID;
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
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 1178, 588);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(529, 130, 313, 343);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 106, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 101, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 142, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(6, 178, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(6, 214, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 249, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(6, 277, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(106, 137, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 173, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 209, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 245, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 277, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 277, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 315, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 307, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);

		lastOPDDateLB = new JLabel("Last Exam :");
		lastOPDDateLB.setBounds(6, 284, 29, -1);
		panel.add(lastOPDDateLB);
		lastOPDDateLB.setHorizontalAlignment(SwingConstants.CENTER);
		lastOPDDateLB.setForeground(Color.RED);
		lastOPDDateLB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(6, 36, 108, 14);
		panel.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));
		timer = new Timer(500, new ActionListener() {

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
					procedureNameCB.setSelectedIndex(0);

					itemsHashMap.clear();
					examHashMap.clear();

					loadDataToTable();
				}
			}
		});
		searchPatientTB = new JTextField();
		searchPatientTB.setBounds(106, 31, 201, 25);
		panel.add(searchPatientTB);
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		patientIDCB = new JComboBox(patientID);
		patientIDCB.setBounds(106, 65, 201, 25);
		panel.add(patientIDCB);
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

					itemsHashMap.clear();
					examHashMap.clear();
					loadDataToTable();
				}
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(6, 67, 77, 20);
		panel.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

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
		searchPatientTB.requestFocus(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 509, 566);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Procedures Detail :",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setBounds(5, 11, 494, 166);
		panel_2.add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		procedureNameCB = new JComboBox<String>();
		procedureNameCB.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					procedureNameSTR = procedureNameCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (procedureNameCB.getSelectedIndex() > -1) {
					procedureIDSTR = procedureIDV.get(procedureNameCB
							.getSelectedIndex());
				}

				priceET.setText("");
				priceET.setText(""
						+ procedurePriceV.get(procedureNameCB
								.getSelectedIndex()));
				predaysLBL.setText(""
						+ procedurePreDaysV.get(procedureNameCB
								.getSelectedIndex()));
				postdaysLBL.setText(""
						+ procedurePostDaysV.get(procedureNameCB
								.getSelectedIndex()));
				itemIDV.clear();
				itemNameV.clear();
				itemDescV.clear();
				issuedQtyV.clear();
				itemPriceV.clear();
				totalPriceV.clear();
				itemAvailableV.clear();
				ProceduresDBConnection dbConnection = new ProceduresDBConnection();
				ResultSet resultSet = dbConnection
						.retrieveDataProcedure(procedureIDSTR);
				try {
					while (resultSet.next()) {
						itemIDV.add(resultSet.getObject(1).toString());
						itemNameV.add(resultSet.getObject(2).toString());
						itemDescV.add(resultSet.getObject(3).toString());
						itemTypeV.add(resultSet.getObject(7).toString());

						double itemPrice=getItemDetail(resultSet.getObject(1).toString());
						double quantity=Double.parseDouble(resultSet.getObject(4).toString());
						double finalPrice=itemPrice*quantity;

						measUnitV.add(packSize+"");
						issuedQtyV.add(quantity+"");
						totalPriceV.add(finalPrice+"");
						itemPriceV.add(itemPrice+"");
						itemAvailableV.add("From Store");
						//getItemStock(resultSet.getObject(1).toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dbConnection.closeConnection();
				loadDataToTable();
				priceET.setText(""+finalTotalValue);
			}
		});
		procedureNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		procedureNameCB.setBounds(170, 16, 283, 25);
		panel_5.add(procedureNameCB);

		priceET = new JTextField();
		priceET.setEditable(false);
		priceET.setColumns(10);
		priceET.setBounds(170, 52, 283, 25);
		panel_5.add(priceET);

		JLabel lblPrice = new JLabel("Price :");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(26, 51, 113, 25);
		panel_5.add(lblPrice);

		JLabel lblSelectProcedure = new JLabel("Select Procedure :");
		lblSelectProcedure.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelectProcedure.setBounds(25, 16, 135, 25);
		panel_5.add(lblSelectProcedure);

		JLabel lblPreDays = new JLabel("Pre Days :");
		lblPreDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPreDays.setBounds(26, 88, 113, 25);
		panel_5.add(lblPreDays);

		predaysLBL = new JTextField();
		predaysLBL.setEditable(false);
		predaysLBL.setColumns(10);
		predaysLBL.setBounds(170, 89, 283, 25);
		panel_5.add(predaysLBL);

		JLabel lblPostDays = new JLabel("Post Days :");
		lblPostDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPostDays.setBounds(26, 124, 113, 25);
		panel_5.add(lblPostDays);

		postdaysLBL = new JTextField();
		postdaysLBL.setEditable(false);
		postdaysLBL.setColumns(10);
		postdaysLBL.setBounds(170, 125, 283, 25);
		panel_5.add(postdaysLBL);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 188, 489, 367);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.getTableHeader().setReorderingAllowed(false);
		addTestTable_1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		addTestTable_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] {}));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub

					}
				});
		scrollPane.setViewportView(addTestTable_1);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(OutdoorProcedureEntry.class
				.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (slipNoET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"This patient is not a indoor patient. ",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (itemIDV.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					//				} else if (itemAvailableV.contains("Not Available")) {// itemsHashMap.size()
					//																		// == 0
					//					JOptionPane
					//							.showMessageDialog(
					//									null,
					//									"Some Items are not available in your stock please issue from main store",
					//									"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					IssuedItemsDBConnection storePillsRegisterDBConnection = new IssuedItemsDBConnection();
					ItemsDBConnection itemStockDBConnection = new ItemsDBConnection();

					data[0] = "" + doctorID; // 
					data[1] = doctorName; // 
					data[2] = ""+DepartmentMain.departmentID; // user id
					data[3] =  ""+DepartmentMain.departmentName; // user name
					data[4] = p_id;
					data[5] = p_name;
					data[12] = date;
					data[13] = "" + timeFormat.format(cal1.getTime());
					data[14] = ""+slipNoET.getText().toString(); // ipd no
					data[15] = "OUTDOOR";
					data[16] = "PENDING";
					data[17] = procedureNameSTR;
					for (int i = 0; i < itemIDV.size(); i++) {
						data[6] = itemIDV.get(i);
						data[7] = "" + itemNameV.get(i);
						data[8] = itemDescV.get(i);
						data[9] = itemPriceV.get(i);
						data[10] = issuedQtyV.get(i);
						data[11] = totalPriceV.get(i);
						data[18]=itemTypeV.get(i);
						double mrpnew = getItemMRP(itemIDV.get(i));
						data[19]=mrpnew+"";
						data[20]=measUnitV.get(i);
						try {
							storePillsRegisterDBConnection
							.inserStorePillsRegisterNewWIthMRP(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					itemStockDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Data Requested Successfully. Please Contact to Store ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(529, 533, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(685, 533, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(OutdoorProcedureEntry.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setBounds(529, 60, 313, 59);
		panel_4.add(panel_1);

		JLabel label = new JLabel("Doctor Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(6, 16, 108, 25);
		panel_1.add(label);

		doctorNameCB = new JComboBox<String>();
		//		doctorNameCB.setSelectedIndex(0);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		doctorNameCB.setBounds(103, 18, 204, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doctorName = doctorNameCB.getSelectedItem().toString();
					doctorID=doctorIDV.get(doctorNameCB.getSelectedIndex());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		JLabel label_1 = new JLabel("Doctor Room :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(6, 52, 108, 25);
		panel_1.add(label_1);

		JLabel label_2 = new JLabel("Doctor specialization :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(6, 90, 141, 24);
		panel_1.add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(145, 54, 186, 23);
		panel_1.add(label_3);

		JLabel label_4 = new JLabel("");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_4.setBounds(145, 89, 186, 25);
		panel_1.add(label_4);

		JLabel label_5 = new JLabel("Enter Slip No. :");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(529, 11, 84, 23);
		panel_4.add(label_5);

		slipNoET = new JTextField();
		slipNoET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		slipNoET.setColumns(10);
		slipNoET.setBounds(623, 11, 219, 25);
		panel_4.add(slipNoET);

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
		getAllProcedures();
		getAllDoctors();
	}
	public void getAllDoctors() {
		doctors.addElement("Select Doctor");
		doctorIDV.clear();
		doctorIDV.add("");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				doctorIDV.add(resultSet.getObject(1).toString());
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
	public double getItemMRP(String index){

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		double mrpc=0;
		try {
			while (resultSet.next()) {
				mrpc =Double.parseDouble( resultSet.getObject(11).toString());
			}
		}catch(Exception e){

		}
		itemsDBConnection.closeConnection();
		return mrpc;

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

		patientDBConnection.closeConnection();
	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = totalPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = itemAvailableV.get(i);
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		addTestTable_1.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Desc.", "Issued Qty.", "Cost",
				"Item Availability" }));
		addTestTable_1.getColumnModel().getColumn(0).setPreferredWidth(50);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(120);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(4).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(4).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(5).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(5).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		addTestTable_1.getColumnModel().getColumn(3)
		.setCellRenderer(centerRenderer);
		addTestTable_1.getColumnModel().getColumn(4)
		.setCellRenderer(centerRenderer);

		finalTotalValue = total;
	}
	public double getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		double mrp = 0;
		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
		//int packSize = 1;
		String formulaActive = "";
		String formulaApplied = "";
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double
						.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13)
						.toString());
				purchase = Double
						.parseDouble(resultSet.getObject(20).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
				formulaApplied = resultSet.getObject(17).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(4)
							.toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;

				mrp = Double.parseDouble(resultSet.getObject(11).toString());
				if (formulaActive.equals("1")) {
					sp = price;
				} else {
					if (formulaApplied.equals("NA")) {
						if (purchase >= 10000 && purchase <= 20000) {


							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.15* purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.10* purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.10;
						} else if (purchase > 30000) {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp =  1.05 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							//							sp = purchase * 1.05;
						} else if(purchase > 5000 && purchase <= 10000){
							System.out.println("5000"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.25 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if(purchase > 1000 && purchase <= 5000){
							System.out.println("1000"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.30 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
						else if(purchase > 250 && purchase <= 1000){
							System.out.println("250"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
						else if(purchase > 0 && purchase <= 250){
							System.out.println("0"+purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}else {
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1
									* ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax
										- (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
					} else {
						System.out.println(formulaApplied+"forumaula"+" "+purchase+"purchase");
						sp = purchase * Double.parseDouble(formulaApplied);
					}

				}
			}

			price = (double) Math.round(sp * 100) / 100;
			System.out.print("pricennng" + price);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		return price;

	}
	//june 2020
	//	public double getItemDetail(String index) {
	//
	//		GeneralDBConnection formula = new GeneralDBConnection();
	//		double value = Double.parseDouble(formula.retrieveFormula1());
	//		formula.closeConnection();
	//		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
	//		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
	//		double mrp = 0;
	//		double purchase = 0, tot = 0, tot1 = 0,sp=0;
	//		int packSize = 1;
	//		String formulaActive="";
	//		try {
	//			while (resultSet.next()) {
	//
	//				itemDescSTR = resultSet.getObject(3).toString();
	//				taxValue = Double.parseDouble(resultSet.getObject(6).toString());
	//				surchargeValue = Double.parseDouble(resultSet.getObject(13).toString());
	//				purchase = Double
	//						.parseDouble(resultSet.getObject(7).toString());
	//				price = Double.parseDouble(resultSet.getObject(10).toString());
	//				tot = (double) Math.round(purchase * value * 100) / 100;
	//				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
	//				formulaActive= resultSet.getObject(15).toString();
	//				try {
	//					packSize = Integer.parseInt(resultSet.getObject(4)
	//							.toString().trim());
	//				} catch (Exception e) {
	//					// TODO: handle exception
	//				}
	//				double tax = taxValue + surchargeValue;
	//
	//				mrp = Double.parseDouble(resultSet.getObject(11).toString());
	//				if(formulaActive.equals("1")){
	//					sp = price;
	//				}else{
	//					if (purchase >= 10000 && purchase <= 20000) {
	//						sp = purchase * 1.15;
	//					} else if (purchase > 20000 && purchase <=30000) {
	//						sp = purchase * 1.10;
	//					}else if(purchase>30000){
	//						sp = purchase * 1.05;
	//					} else {
	//				double tempvar1 = mrp / packSize;
	//				double tempvar2 = tempvar1
	//						* ((taxValue + surchargeValue) / 100);
	//				double mrpwithouttax = tempvar1 - tempvar2;
	//				double temp=2.5*purchase;
	//				if(mrpwithouttax>temp){
	//					sp=temp;
	//					
	//				}else{
	//					double mrpless1prcnt=mrpwithouttax-(mrpwithouttax*0.01);
	//					sp=mrpless1prcnt;
	//				}
	//				
	//			}
	//				}
	//				}
	//
	//			price = (double) Math.round(sp * 100) / 100;
	//			
	//
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		itemsDBConnection.closeConnection();
	//		return price;
	//
	//	}
	//	public double getItemDetail(String index) {
	//
	//		GeneralDBConnection formula = new GeneralDBConnection();
	//		double value = Double.parseDouble(formula.retrieveFormula1());
	//		formula.closeConnection();
	//		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
	//		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
	//		double mrp = 0;
	//		double purchase = 0, tot = 0,tot1=0;
	//		int packSize=1;
	//		try {
	//			while (resultSet.next()) {
	//
	//				itemDescSTR = resultSet.getObject(3).toString();
	//				purchase = Double
	//						.parseDouble(resultSet.getObject(7).toString());
	//				price = Double.parseDouble(resultSet.getObject(10).toString());
	//				tot = (double) Math.round(purchase * value * 100) / 100;
	//				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
	//				try {
	//					packSize=Integer.parseInt( resultSet.getObject(4).toString().trim());
	//				} catch (Exception e) {
	//					// TODO: handle exception
	//				}
	//				
	//				try {
	//					mrp = Double
	//							.parseDouble(resultSet.getObject(11).toString());
	//					mrp=mrp/packSize;
	//					double priceTemp=mrp*0.7;
	//					
	//					if(priceTemp>tot&&priceTemp<tot1)
	//					{
	//						tot=priceTemp;
	//					}
	//					
	//					if(priceTemp>tot&&priceTemp>tot1)
	//					{
	//						tot=tot1;
	//					}
	//					if (tot > mrp && mrp > 0) {
	//						
	//						tot = mrp;
	//
	//					}
	//				} catch (Exception e) {
	//
	//				}
	//			}
	//			 
	//			price =(double) Math.round(tot * 100) / 100;;
	//			
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		itemsDBConnection.closeConnection();
	//
	//		return price;
	//	}
	public void getItemStock(String index) {

		quantity = 0;
		String itemAvailable = "Not Available";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStock(index,
				departmentName);

		try {
			while (resultSet.next()) {

				quantity = Integer
						.parseInt((resultSet.getObject(1).toString()));
				itemAvailable = quantity + "";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		itemAvailableV.add(itemAvailable);
	}


	public void getAllProcedures() {
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		procedureName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				procedureIDV.add(resultSet.getObject(1).toString());
				procedureNameV.add(resultSet.getObject(2).toString());
				procedureName.addElement(resultSet.getObject(2).toString());
				procedurePriceV.add(resultSet.getObject(3).toString());
				procedurePreDaysV.add(resultSet.getObject(7).toString());
				procedurePostDaysV.add(resultSet.getObject(8).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		procedureNameCB.setModel(procedureName);
		if (i > 0) {
			procedureNameCB.setSelectedIndex(0);
		}
	}

}
