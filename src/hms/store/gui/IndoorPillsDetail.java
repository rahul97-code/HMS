package hms.store.gui;

import hms.departments.gui.DepartmentMain;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.PillsSlippdf;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.IPDBrowser;

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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class IndoorPillsDetail extends JDialog {

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
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	Vector<String> itemTypeV = new Vector<String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	double totalChargesIPD = 0;
	double mrp = 0;
	String[] datanew = new String[22];
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> CancelitemIDV = new Vector<String>();
	Vector<String> batchNameV = new Vector<String>();
	Vector<String> itemEXPV = new Vector<String>();
	Vector<String> batchIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> ProcedureV = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Vector<String> ItemTypeRiskV = new Vector<String>();
	Vector<String> itemMRPV = new Vector<String>();
	Vector<String> itemMeasV = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	int packSize = 1;
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
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
			previouseStock = "", departmentName = "", departmentID = "",itemtypeSTR="";
	int qtyIssued = 0, afterIssued = 0, itemValue, finalTaxValue = 0,
			finalDiscountValue = 0;
	int quantity = 0;
	double price = 0, finalTotalValue = 0, taxValue = 0, surchargeValue = 0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	private JComboBox<String> itemNameCB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField enterQtyET;
	private JTextField itemSearchET;
	private JTextField itemDescET;
	private JTextField qtyInHandET;
	private JTextField remainingItemET;
	private JTextField expirtDateET;
	private JTextField priceET;
	private JTextField costET;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField ipdNoTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTable table;
	private JButton btnRemove;

	String typeSTR = "n/a";

	public IndoorPillsDetail(final String ipdidSTR, String patient_id, String type) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IndoorPillsDetail.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Pills Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(50, 70, 1203, 634);
		setModal(true);
		departmentName = "";
		departmentID = "";
		this.typeSTR = type;
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
		panel_4.setBounds(6, 5, 1178, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(855, 11, 313, 343);
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

		searchPatientTB = new JTextField();
		searchPatientTB.setEditable(false);
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
							itemNameCB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							ipdNoTB.setText("");
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							advancePaymentTB.setText("");
							populateExpensesTable("??");
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
							patientID.removeAllElements();
							patientIDCB.setModel(patientID);
							lastOPDDateLB.setText("Last OPD :");
							// itemNameCB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							ipdNoTB.setText("");
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							advancePaymentTB.setText("");
							populateExpensesTable("??");
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
							patientID.removeAllElements();
							patientIDCB.setModel(patientID);
							lastOPDDateLB.setText("Last OPD :");
							populateExpensesTable("??");
							itemNameCB.setSelectedIndex(0);

							itemsHashMap.clear();
							examHashMap.clear();
							ipdNoTB.setText("");
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							advancePaymentTB.setText("");
							loadDataToTable();
						}
					}
				});
		searchPatientTB.requestFocus(true);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 11, 509, 568);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Item Detail :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));
		panel_5.setBounds(5, 11, 494, 1);
		panel_2.add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		itemNameCB = new JComboBox<String>();
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}
				itemDescET.setText("");
				enterQtyET.setText("");
				remainingItemET.setText("");
				expirtDateET.setText("");
				qtyInHandET.setText("");
				priceET.setText("");
				getItemStock(itemIDSTR);
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					afterIssued = quantity - qtyIssued;
					itemDescET.setText("" + itemDescSTR);
					enterQtyET.setText("");
					remainingItemET.setText("" + afterIssued);
					expirtDateET.setText("" + expiryDateSTR);
					qtyInHandET.setText("" + quantity);

					priceET.setText("" + price);
//					if (itemtypeSTR.equals("General")) {
//						itemType.setText(itemtypeSTR);
//					} else {
//						itemType.setText(itemtypeSTR);
//					}
//					if (itemtypeSTR.equals("High Risk")) {
//						itemType.setBackground(Color.RED);
//						itemType.setForeground(Color.BLACK);
//					} else if (itemtypeSTR.equals("SHC-H1")) {
//						itemType.setBackground(Color.GREEN);
//						itemType.setForeground(Color.BLACK);
//					} else {
//						itemType.setBackground(Color.WHITE);
//					}
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		itemNameCB.setBounds(277, 16, 207, 25);
		panel_5.add(itemNameCB);

		enterQtyET = new JTextField();
		enterQtyET.setHorizontalAlignment(SwingConstants.RIGHT);
		enterQtyET.setBounds(109, 169, 128, 25);
		enterQtyET.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		enterQtyET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;

				remainingItemET.setText("" + afterIssued);
				costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				costET.setText((qtyIssued * price) + "");
			}
		});
		panel_5.add(enterQtyET);
		enterQtyET.setColumns(10);

		JLabel lblAmount = new JLabel("Enter Qty.:");
		lblAmount.setBounds(22, 174, 82, 14);
		panel_5.add(lblAmount);

		itemSearchET = new JTextField();
		itemSearchET.setBounds(109, 17, 158, 25);
		panel_5.add(itemSearchET);
		itemSearchET.setColumns(10);

		itemSearchET.setColumns(10);
		itemSearchET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					qtyInHandET.setText("");
					priceET.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					qtyInHandET.setText("");
					priceET.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					qtyInHandET.setText("");
					priceET.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}
		});

		JLabel lblSearchItem = new JLabel("Search Item :");
		lblSearchItem.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearchItem.setBounds(16, 22, 89, 14);
		panel_5.add(lblSearchItem);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDescription.setBounds(16, 53, 94, 25);
		panel_5.add(lblDescription);

		itemDescET = new JTextField();
		itemDescET.setEditable(false);
		itemDescET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		itemDescET.setBounds(109, 52, 375, 25);
		panel_5.add(itemDescET);
		itemDescET.setColumns(10);

		qtyInHandET = new JTextField();
		qtyInHandET.setEditable(false);
		qtyInHandET.setBounds(109, 126, 128, 25);
		panel_5.add(qtyInHandET);
		qtyInHandET.setColumns(10);

		JLabel lblQtyInhand = new JLabel("Qty. In Hand :");
		lblQtyInhand.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQtyInhand.setBounds(16, 126, 94, 25);
		panel_5.add(lblQtyInhand);

		JLabel lblAfterEntry = new JLabel("After Entry :");
		lblAfterEntry.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAfterEntry.setBounds(258, 125, 100, 25);
		panel_5.add(lblAfterEntry);

		remainingItemET = new JTextField();
		remainingItemET.setEditable(false);
		remainingItemET.setColumns(10);
		remainingItemET.setBounds(356, 126, 128, 25);
		panel_5.add(remainingItemET);

		JLabel lblExpiryDate = new JLabel("Expiry Date :");
		lblExpiryDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExpiryDate.setBounds(16, 89, 94, 25);
		panel_5.add(lblExpiryDate);

		expirtDateET = new JTextField();
		expirtDateET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		expirtDateET.setEditable(false);
		expirtDateET.setColumns(10);
		expirtDateET.setBounds(109, 88, 375, 25);
		panel_5.add(expirtDateET);

		priceET = new JTextField();
		priceET.setEditable(false);
		priceET.setColumns(10);
		priceET.setBounds(356, 170, 128, 25);
		panel_5.add(priceET);

		JLabel lblPrice = new JLabel("Price per unit :");
		lblPrice.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPrice.setBounds(258, 168, 100, 25);
		panel_5.add(lblPrice);

		JLabel lblCost = new JLabel("Cost :");
		lblCost.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCost.setBounds(182, 200, 69, 25);
		panel_5.add(lblCost);

		costET = new JTextField();
		costET.setEditable(false);
		costET.setColumns(10);
		costET.setBounds(249, 202, 128, 25);
		panel_5.add(costET);

		JButton btnAdd = new JButton("Add");
		btnAdd.setBounds(224, 230, 125, 33);
		panel_5.add(btnAdd);
		btnAdd.setIcon(new ImageIcon(IndoorPillsDetail.class
				.getResource("/icons/plus_button.png")));
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));

		btnRemove = new JButton("Remove");
		btnRemove.setBounds(359, 230, 125, 33);
		panel_5.add(btnRemove);
		btnRemove.setIcon(new ImageIcon(IndoorPillsDetail.class
				.getResource("/icons/exit.png")));
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

				itemIDV.remove(cur_selectedRow);
				batchNameV.remove(cur_selectedRow);
				batchIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				itemEXPV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				itemPriceV.remove(cur_selectedRow);
				totalPriceV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (enterQtyET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				// if (Double.parseDouble(remainingItemET.getText().toString())
				// < 0) {
				// JOptionPane.showMessageDialog(null,
				// "your requirements exceeded the stock",
				// "Input Error", JOptionPane.ERROR_MESSAGE);
				// return;
				// }

				else if (itemIDV.indexOf(itemIDSTR) != -1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				
				itemDescV.add(itemDescSTR);
				issuedQtyV.add(qtyIssued + "");
				itemPriceV.add(price + "");
				itemTypeV.add(itemtypeSTR + "");
//				itemMRPV.add(mrp+"");
//				itemMeasV.add(packSize+"");
				totalPriceV.add((qtyIssued * price) + "");
				loadDataToTable();
				itemSearchET.setText("");
			}
		});

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(129, 524, 106, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 23, 489, 490);
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
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);
		addTestTable_1.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					calculate();

				}
			}

			private void calculate() {
				// TODO Auto-generated method stub
				CancelitemIDV.removeAllElements();
				EditableTableModel2 model1 = (EditableTableModel2) addTestTable_1.getModel();
				for (int count = 0; count < model1.getRowCount(); count++) {
					Boolean b = Boolean.valueOf(model1.getValueAt(count, 5).toString());
					if (b) {;
					CancelitemIDV.add(model1.getValueAt(count, 0)+"");
					}
				}
				
				
			}
		});

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(227, 527, 144, 20);
		panel_2.add(lblTotalcharges);
		
		JButton btnNewButton = new JButton("Delete");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IssuedItemsDBConnection db = new IssuedItemsDBConnection();
				for(int i=0;i<CancelitemIDV.size();i++)
				{
					try {
						db.updateItemStatus(ipdidSTR,CancelitemIDV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				db.closeConnection();
				itemIDV.removeAllElements();
				itemNameV.removeAllElements();
				itemDescV.removeAllElements();
				issuedQtyV.removeAllElements();
				itemPriceV.removeAllElements();
				totalPriceV.removeAllElements();
				ProcedureV.removeAllElements();
				ItemTypeRiskV.removeAllElements();
				itemMRPV.removeAllElements();
				itemMeasV.removeAllElements();
				batchNameV.removeAllElements();
				batchIDV.removeAllElements();
				itemEXPV.removeAllElements();
				pillsEnteryData(ipdidSTR);
			}
			
		});
		btnNewButton.setBounds(382, 523, 117, 25);
		panel_2.add(btnNewButton);
		if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			btnNewButton.setEnabled(true);
		}

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(IndoorPillsDetail.class
				.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} 
				else if (ipdNoTB.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"This patient is not a indoor patient. ",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (itemIDV.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int index = 0;
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					IssuedItemsDBConnection storePillsRegisterDBConnection = new IssuedItemsDBConnection();
					BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
					ItemsDBConnection itemStockDBConnection = new ItemsDBConnection();

					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[18];
//					String[] datanew = new String[18];
					int flag = 0;
					String stocklessItem = "";
					StringBuilder result = new StringBuilder();
					for (int i = 0; i < itemIDV.size(); i++) {

						getItemStock(itemIDV.get(i));
						if (quantity < Double.parseDouble(issuedQtyV.get(i))) {

							flag = 1;
							result.append(itemNameV.get(i));
							result.append(",");
						}

					}

					for (int i = 0; i < itemIDV.size(); i++) {
						if (flag == 1) {
							JOptionPane.showMessageDialog(null, result
									+ "!Please Maintain Stock of this Items Or you can delete these from request",
									"Stock Less",
									JOptionPane.INFORMATION_MESSAGE);
							return;
						} else {
							try {
								storePillsRegisterDBConnection
										.updateStatus(ipd_id);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							datanew[0] = "" + ipd_id;
							datanew[1] = "" + itemNameV.get(i);

							datanew[2] = itemDescV.get(i);
							datanew[3] = totalPriceV.get(i);
							datanew[4] = ""
									+ DateFormatChange
											.StringToMysqlDate(new Date());
							datanew[5] = "" + timeFormat.format(cal1.getTime());
							datanew[6] = itemIDV.get(i);
							datanew[7] = "" + p_id;
							datanew[8] = "" + p_name;
							datanew[9] = itemPriceV.get(i);
							datanew[10] = issuedQtyV.get(i);
							datanew[11] = db.retrieveItemSubCategory(itemIDV
									.get(i));
							datanew[12] = StoreMain.userName;
							datanew[13] = db.retrieveItemCategory(itemIDV.get(i));
							datanew[14] = ProcedureV.get(i);
							datanew[15] = ItemTypeRiskV.get(i);
							datanew[16] =itemMRPV.get(i);
							datanew[17] = itemMeasV.get(i);
							datanew[18] = batchIDV.get(i);
							datanew[19] = batchNameV.get(i);
							datanew[20] = "D";
//							if (typeSTR.equals("n/a")) {
								try {
									db.inserData2(datanew);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
//							}
								try {
									itemStockDBConnection.subtractStock(itemIDV.get(i),
											issuedQtyV.get(i));
									if (batchIDV.get(i).equals("0") || batchNameV.get(i).toLowerCase().contains("n/a")) {
										batchTrackingDBConnection.subtractStock(batchIDV.get(i), issuedQtyV.get(i),
												DateFormatChange.StringToMysqlDate(new Date()),
												StoreMain.userName);
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								if (typeSTR.equals("n/a")) {
									double tot = totalChargesIPD + finalTotalValue;
									double finalValue = Math.round(tot * 100.0) / 100.0;
									IPDDBConnection ipddbConnection = new IPDDBConnection();
									try {
										ipddbConnection.updateTotalAmount(ipd_id,
												finalValue + "");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									ipddbConnection.closeConnection();
								}
							
								JOptionPane.showMessageDialog(null,
										"Data Saved Successfully ", "Data Save",
										JOptionPane.INFORMATION_MESSAGE);
								try {
									 new PillsSlippdf(ipdNoTB.getText(), itemNameV,
									 issuedQtyV,totalPriceV, finalTotalValue+"", index,
									 p_name, p_id,batchNameV,itemEXPV);
								} catch (Exception e) {
									// TODO: handle exception
								}
								dispose();

						}
					}
					storePillsRegisterDBConnection.closeConnection();
					batchTrackingDBConnection.closeConnection();
					itemStockDBConnection.closeConnection();
					db.closeConnection();	
					
				}
			}
		});
		btnNewButton_3.setBounds(1021, 480, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(1021, 535, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IndoorPillsDetail.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,

		TitledBorder.TOP, null, null));
		panel_3.setBounds(529, 11, 316, 219);
		panel_4.add(panel_3);

		JLabel label = new JLabel("Doctor Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 46, 108, 14);
		panel_3.add(label);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_3.add(ipdDoctorTB);

		JLabel label_1 = new JLabel("Building :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_1.setBounds(10, 77, 108, 14);
		panel_3.add(label_1);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_3.add(ipdBuildingTB);

		JLabel label_2 = new JLabel("Ward Name :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(10, 113, 108, 14);
		panel_3.add(label_2);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_3.add(ipdWardTB);

		JLabel label_3 = new JLabel("Bed No :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(10, 149, 108, 14);
		panel_3.add(label_3);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 144, 182, 25);
		panel_3.add(ipdBedNoTB);

		JLabel label_4 = new JLabel("No. of Days :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(10, 180, 108, 14);
		panel_3.add(label_4);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 175, 182, 25);
		panel_3.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_3.add(ipdNoTB);

		JLabel label_5 = new JLabel("IPD No :");
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_5.setBounds(10, 15, 108, 14);
		panel_3.add(label_5);

		JPanel panel_6 = new JPanel();
		panel_6.setLayout(null);
		panel_6.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Bill",

		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(529, 238, 316, 116);
		panel_4.add(panel_6);

		JLabel label_6 = new JLabel("Total Amount :");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_6.setBounds(6, 21, 108, 14);
		panel_6.add(label_6);

		totalAmountTB = new JTextField();
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);
		totalAmountTB.setBounds(149, 16, 147, 25);
		panel_6.add(totalAmountTB);

		JLabel label_7 = new JLabel("Advance Payment :");
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_7.setBounds(6, 66, 108, 14);
		panel_6.add(label_7);

		advancePaymentTB = new JTextField();
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);
		advancePaymentTB.setBounds(148, 61, 148, 25);
		panel_6.add(advancePaymentTB);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(539, 365, 458, 214);
		panel_4.add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		JLabel label_8 = new JLabel("");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setIcon(new ImageIcon(IndoorPillsDetail.class
				.getResource("/icons/graphics-hospitals-575145.gif")));
		label_8.setBounds(1021, 365, 103, 105);
		panel_4.add(label_8);
		searchPatientTB.setText(patient_id);
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
		pillsEnteryData(ipdidSTR);
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
		getIPDDATA();
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
		patientDBConnection.closeConnection();
	}

	public void pillsEnteryData(String ipd_id) {
		IssuedItemsDBConnection issuedItemsDBConnection = new IssuedItemsDBConnection();
		ResultSet resultSet = issuedItemsDBConnection
				.retrieveAllPendingPillsDetail(ipd_id);

		try {
			while (resultSet.next()) {
				itemIDV.add(resultSet.getObject(1).toString());
				itemNameV.add(resultSet.getObject(2).toString());
				itemDescV.add(resultSet.getObject(3).toString());
				issuedQtyV.add(resultSet.getObject(4).toString());
				itemPriceV.add(resultSet.getObject(5).toString());
				totalPriceV.add(resultSet.getObject(6).toString());
				ProcedureV.add(resultSet.getObject(7).toString());
				ItemTypeRiskV.add(resultSet.getObject(8).toString());
				itemMRPV.add(resultSet.getObject(9).toString());
				itemMeasV.add(resultSet.getObject(10).toString());
				batchNameV.add(resultSet.getObject(11)+"");
				batchIDV.add(resultSet.getObject(12)+"");
				itemEXPV.add(resultSet.getObject(13)+"");

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		issuedItemsDBConnection.closeConnection();
		loadDataToTable();
		//itemSearchET.setText("");
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
			ObjectArray_ListOfexamsSpecs[i][5] = new Boolean(false);
			ObjectArray_ListOfexamsSpecs[i][6] = batchNameV.get(i);
			
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		TableModel model1 = new EditableTableModel2(new String[] { "Item ID",
				"Item Name", "Item Desc.", "Issued Qty.", "Cost","select","Item Batch"},
				ObjectArray_ListOfexamsSpecs);
		addTestTable_1.setModel(model1);
		addTestTable_1.getColumnModel().getColumn(0).setMinWidth(50);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(90);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(90);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(4).setPreferredWidth(70);
		addTestTable_1.getColumnModel().getColumn(4).setMinWidth(70);
		addTestTable_1.getColumnModel().getColumn(5).setPreferredWidth(50);
		addTestTable_1.getColumnModel().getColumn(5).setMinWidth(50);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		addTestTable_1.getColumnModel().getColumn(3)
				.setCellRenderer(centerRenderer);
		addTestTable_1.getColumnModel().getColumn(4)
				.setCellRenderer(centerRenderer);

		finalTotalValue = total;

		System.out.println(finalTotalValue + "   " + total);
		lblTotalcharges.setText("`" + finalTotalValue + "");
		lblTotalcharges.setFont(customFont);
		btnRemove.setEnabled(false);
	}

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}

	public void getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		
		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
	
		String formulaActive = "";
		try {
			while (resultSet.next()) {
				itemtypeSTR = resultSet.getObject(18).toString();
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
				try {
					packSize = Integer.parseInt(resultSet.getObject(4)
							.toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;

				mrp = Double.parseDouble(resultSet.getObject(11).toString());
				System.out.println("MRP"+mrp);
				if (formulaActive.equals("1")) {
					sp = price;
				} else {
					
					if (purchase > 10000 && purchase <= 20000) {
						
						
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
//						sp = purchase * 1.15;
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
//						sp = purchase * 1.10;
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
//						sp = purchase * 1.05;
					}else if(purchase > 5000 && purchase <= 10000){
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
						} else {
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
				}
			}
			
			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023

			price = (double) Math.round(sp * 100) / 100;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemStock(String index) {

		quantity = 0;

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		int i = 0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				quantity = Integer.parseInt(resultSet.getObject(8).toString());
				expiryDateSTR = resultSet.getObject(9).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getIPDDATA() {

		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientID(p_id);
		try {
			while (resultSet.next()) 
			{
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
		} catch (Exception e) {
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

		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllData(ipd_id);

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

				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Description","Qty", "Amount" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			for (int i = 0; i < NumberOfRows; i++) {
				totalChargesIPD = totalChargesIPD
						+ Double.parseDouble(Rows_Object_Array[i][2].toString());
			}
			totalChargesIPD = Math.round(totalChargesIPD * 100.0) / 100.0;
			totalAmountTB.setText(totalChargesIPD + "");
			table.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(0).setMinWidth(150);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(50);
			table.getColumnModel().getColumn(1).setMinWidth(50);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(100);
			ipd_balance = totalChargesIPD - ipd_advance;

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public JButton getBtnRemove() {
		return btnRemove;
	}
	class EditableTableModel2 extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel2(String[] columnTitles, Object[][] dataEntries) {
			this.columnTitles = columnTitles;
			this.dataEntries = dataEntries;
		}

		public int getRowCount() {
			return dataEntries.length;
		}

		public int getColumnCount() {
			return columnTitles.length;
		}

		public Object getValueAt(int row, int column) {
			return dataEntries[row][column];
		}

		public String getColumnName(int column) {
			return columnTitles[column];
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(int row, int column) {
				return true;	
			
			
		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
}
