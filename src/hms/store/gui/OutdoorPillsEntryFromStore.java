package hms.store.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.OutdoorPillsSlippdf;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.StoreMain;

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
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class OutdoorPillsEntryFromStore extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private Timer timer;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();

	private HashMap examHashMap = new HashMap();
	String[] data = new String[21];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	boolean flag = false;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Vector<Boolean> flagV = new Vector<Boolean>();
	Vector<String> itemBTCH = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> itemEXP = new Vector<String>();
	Vector<String> batch_id = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0", p_ageM = "0", p_ageD = "0",
			p_birthdate = "1111-11-11", p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F", p_p_father_husband = "", p_insurancetype = "Unknown",
			item_stock = "", btch_name = "", p_note = "";
	String doctorName = "", btch_ID = "", doctorID = "", date = "";
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
			dueDateSTR = "", previouseStock = "", batchIDSTR = "0", 
			departmentName = "STORE",
			departmentID = "";

	int finalTaxValue = 0, finalDiscountValue = 0, f = 0;

	double qtyIssued = 0, price = 0, finalTotalValue = 0, afterIssued = 0, quantity = 0, taxValue = 0,
			surchargeValue = 0, itemValue = 0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	private JComboBox<String> itemNameCB;
	private JComboBox<String> batchNameCB;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField enterQtyET;
	private JTextField itemSearchET;
	private JTextField itemDescET;
	private JTextField qtyInHandET;
	private JTextField remainingItemET;
	private JTextField expirtDateET;
	private JTextField priceET;
	private JTextField slipNoET;
	private JTextField costET;
	String value = "";
	private double mrp;
	private int packSize;
	private JButton btnAdd;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new OutdoorPillsEntryFromStore().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public OutdoorPillsEntryFromStore() {
		setIconImage(
				Toolkit.getDefaultToolkit().getImage(OutdoorPillsEntryFromStore.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Pills Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(300, 70, 887, 634);
		setModal(true);

		try {
			// create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT, new File("indian.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("indian.ttf")));
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
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 865, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(542, 148, 313, 343);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
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
					System.out.print(departmentID);
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

				// System.out.println(p_id + " "
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

		searchPatientTB.getDocument().addDocumentListener(new DocumentListener() {
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
		panel_2.setBounds(10, 21, 509, 558);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Item Detail :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12),
				null));
		panel_5.setBounds(5, 11, 494, 238);
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
					System.out.println(itemIDSTR + " :  ID  ");
				}

				itemDescET.setText("");
				enterQtyET.setText("");
				remainingItemET.setText("");
				expirtDateET.setText("");
				qtyInHandET.setText("");
				priceET.setText("");
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!flag) {
						expirtDateET.setText("" + expiryDateSTR);
						qtyInHandET.setText("" + quantity + "");
						btch_name = "";
						batchIDSTR = "0";
					} else {
						getItemBatchName(itemIDSTR);
					}

					afterIssued = quantity - qtyIssued;
					itemDescET.setText("" + itemDescSTR);
					enterQtyET.setText("");
					priceET.setText("" + price);
				}
			}
		});
		final JTextField tfListText = (JTextField) itemNameCB.getEditor().getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					descriptionSTR = text;
					descriptionSTR = descriptionSTR.toUpperCase();
					System.out.println(descriptionSTR);
					// HERE YOU CAN WRITE YOUR CODE
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
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE)
						|| (vChar == '.'))) {
					e.consume();
				}
			}
		});
		enterQtyET.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if ((vChar == KeyEvent.VK_ENTER)) {
					btnAdd.doClick();
					itemSearchET.requestFocus();
				}
			}
		});
		enterQtyET.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {
					qtyIssued = Double.parseDouble(str);

				} else {
					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				// costET.setText((qtyIssued * price) + "");
				itemValue();
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Double.parseDouble(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				// costET.setText((qtyIssued * price) + "");
				itemValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = enterQtyET.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Double.parseDouble(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				remainingItemET.setText("" + afterIssued);
				itemValue();
				// costET.setText((qtyIssued * price) + "");
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
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
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
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
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
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
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
		itemDescET.setBounds(109, 52, 373, 25);
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
		expirtDateET.setBounds(109, 88, 128, 25);
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

		batchNameCB = new JComboBox<String>();

		batchNameCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					btch_name = batchNameCB.getSelectedItem().toString();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getStackTrace();
				}
				if (batchNameCB.getSelectedIndex() > -1) {
					batchIDSTR = batchID.get(batchNameCB.getSelectedIndex());

				}
				expirtDateET.setText("");
				qtyInHandET.setText("");
				enterQtyET.setText("");
				expiryDateSTR = "";
				quantity = 0;
				remainingItemET.setText("");
				if (itemBatchName.getSize() > 1 && batchNameCB.getSelectedItem() != ("select Batch")) {
					getItemBatch(batchIDSTR, itemIDSTR);
					expirtDateET.setText("" + expiryDateSTR);
					qtyInHandET.setText("" + quantity);
					priceET.setText("" + price);

				} else {
					expirtDateET.setText("");
					qtyInHandET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
				}
			}

		});
		batchNameCB.setBounds(277, 89, 207, 24);
		panel_5.add(batchNameCB);

		btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemDescET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (flag && itemBatchName.getSize() <= 1) {
					JOptionPane.showMessageDialog(null, "Department has not Item Batches!!You cannot Issue!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (flag && (batchNameCB.getSelectedItem().equals("select Batch"))) {
					JOptionPane.showMessageDialog(null, "Please Select Batch!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (enterQtyET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!itemNameSTR.toLowerCase().contains("charge")
						&& Double.parseDouble(remainingItemET.getText().toString()) < 0 && check() == 1) {
					JOptionPane.showMessageDialog(null, "Please Check Quantity Issued!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemNameSTR.toLowerCase().contains("charge")) {
					expiryDateSTR = "";
				}
				if (itemIDV.indexOf(itemIDSTR) != -1 && flag && batch_id.indexOf(batchIDSTR) != -1) {
					JOptionPane.showMessageDialog(null, "this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (itemIDV.indexOf(itemIDSTR) != -1 && !flag) {
					JOptionPane.showMessageDialog(null, "this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				insucheck();

			}

			private int check() {
				// TODO Auto-generated method stub
				BatchTrackingDBConnection db = new BatchTrackingDBConnection();
				ResultSet rs = db.check(itemIDSTR);
				try {
					while (rs.next()) {
						f = Integer.parseInt(rs.getObject(1).toString());
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				db.closeConnection();
				return f;

			}

			private void insucheck() {
				// TODO Auto-generated method stub
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				flagV.add(flag);
				itemDescV.add(itemDescSTR);
				batch_id.add(batchIDSTR);
				issuedQtyV.add(qtyIssued + "");
				itemPriceV.add(price + "");
				itemBTCH.add(btch_name);
				itemEXP.add(expiryDateSTR);
				totalPriceV.add(itemValue + "");
				loadDataToTable();
				itemSearchET.setText("");
			}
		});
		btnAdd.setBounds(295, 260, 89, 25);
		panel_2.add(btnAdd);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = addTestTable_1.getSelectedRow();
				cur_selectedRow = addTestTable_1.convertRowIndexToModel(cur_selectedRow);
				String toDelete = addTestTable_1.getModel().getValueAt(cur_selectedRow, 0).toString();

				itemIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				flagV.remove(cur_selectedRow);
				itemPriceV.remove(cur_selectedRow);
				totalPriceV.remove(cur_selectedRow);
				batch_id.remove(cur_selectedRow);
				itemBTCH.remove(cur_selectedRow);
				itemEXP.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(410, 260, 89, 25);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(129, 524, 106, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 296, 489, 217);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.getTableHeader().setReorderingAllowed(false);
		addTestTable_1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		addTestTable_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {}, new String[] {}));
		addTestTable_1.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				btnRemove.setEnabled(true);
			}
		});
		scrollPane.setViewportView(addTestTable_1);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(227, 527, 144, 20);
		panel_2.add(lblTotalcharges);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(OutdoorPillsEntryFromStore.class.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (itemID.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (slipNoET.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter slip no.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					int index = 0;
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data1 = new String[10];
					BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
					Dept_PillsRegisterDBConnection dept_PillsRegisterDBConnection = new Dept_PillsRegisterDBConnection();
					DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();

					ItemsDBConnection itemStockDBConnection = new ItemsDBConnection();
					data[0] = ""; // dept id
					data[1] = "STORE"; // dept name
					data[2] = "" + doctorID; // user id
					data[3] = doctorName; // user name
					data[4] = StoreMain.userID; // user id
					data[5] = StoreMain.userName; // user name
					data[6] = p_id;
					data[7] = p_name;
					data[14] = date;
					data[15] = "" + timeFormat.format(cal1.getTime());
					data[16] = slipNoET.getText().toString();
					data[17] = "OUTDOOR";
					data[18] = "OK";
					for (int i = 0; i < itemIDV.size(); i++) {
						data[8] = itemIDV.get(i);
						data[9] = "" + itemNameV.get(i);
						data[10] = itemDescV.get(i);
						data[11] = itemPriceV.get(i);
						data[12] = issuedQtyV.get(i);
						data[13] = totalPriceV.get(i);
						if (flagV.get(i)) {
							String str = itemBTCH.get(i);
							String[] arrOfStr = str.split("\\(", 2);

							for (String a : arrOfStr) {
								data[19] = a;
								break;
							}
						} else {
							data[19] = "N/A";
						}
						data[20] = flagV.get(i) ? batch_id.get(i) : "0";
						try {
							index = dept_PillsRegisterDBConnection.insertDepartmentPillsRegister(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						try {
							itemStockDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i));
							if (flagV.get(i)) {
								batchTrackingDBConnection.subtractStock(batch_id.get(i), issuedQtyV.get(i),
										DateFormatChange.StringToMysqlDate(new Date()), StoreMain.userName);
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}itemStockDBConnection.closeConnection();
					departmentStockDBConnection.closeConnection();
					dept_PillsRegisterDBConnection.closeConnection();
					batchTrackingDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null, "Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
					try {
						new OutdoorPillsSlippdf(slipNoET.getText(), itemNameV, issuedQtyV, totalPriceV,
								finalTotalValue + "", index, p_name, p_id, itemBTCH, itemEXP);
					} catch (Exception e) {
						// TODO: handle exception
					}
					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(542, 502, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(709, 502, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(OutdoorPillsEntryFromStore.class.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(542, 82, 313, 59);
		panel_4.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 16, 108, 25);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameCB = new JComboBox<String>();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doctorName = doctorNameCB.getSelectedItem().toString();
					doctorID = doctorIDV.get(doctorNameCB.getSelectedIndex());
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		doctorNameCB.setBounds(103, 18, 204, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDoctorRoom = new JLabel("Doctor Room :");
		lblDoctorRoom.setBounds(6, 52, 108, 25);
		panel_1.add(lblDoctorRoom);
		lblDoctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNewLabel_4 = new JLabel("Doctor specialization :");
		lblNewLabel_4.setBounds(6, 90, 141, 24);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorRoomLB = new JLabel("");
		doctorRoomLB.setBounds(145, 54, 186, 23);
		panel_1.add(doctorRoomLB);
		doctorRoomLB.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorSpecializationLB = new JLabel("");
		doctorSpecializationLB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorSpecializationLB.setBounds(145, 89, 186, 25);
		panel_1.add(doctorSpecializationLB);

		JLabel lblEnterSlipNo = new JLabel("Enter Slip No. :");
		lblEnterSlipNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterSlipNo.setBounds(542, 33, 84, 23);
		panel_4.add(lblEnterSlipNo);

		slipNoET = new JTextField();
		slipNoET.setFont(new Font("Tahoma", Font.PLAIN, 12));
		slipNoET.setBounds(636, 33, 219, 25);
		panel_4.add(slipNoET);
		slipNoET.setColumns(10);

		KeyboardFocusManager.getCurrentKeyboardFocusManager().addPropertyChangeListener("permanentFocusOwner",
				new PropertyChangeListener() {
			@Override
			public void propertyChange(final PropertyChangeEvent e) {
				if (e.getNewValue() instanceof JTextField) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JTextField textField = (JTextField) e.getNewValue();
							textField.selectAll();
						}
					});

				}
			}
		});
		getAllDoctors();
	}

	public void getPatientsID(String index) {
		lastOPDDateLB.setText("Last Exam : ");
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection.searchPatientWithIdOrNmae(index);
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
		ResultSet resultSet = patientDBConnection.retrieveDataWithIndex(indexId);
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
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
		patientDBConnection.closeConnection();
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
			ObjectArray_ListOfexamsSpecs[i][5] = itemEXP.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = (itemBTCH.get(i).equals("old item")
					|| itemBTCH.get(i).equals("select Batch")) ? "" : itemBTCH.get(i);
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Desc.", "Issued Qty.", "Price", "Expiry", "Batch" }));
		addTestTable_1.getColumnModel().getColumn(0).setMinWidth(75);
		addTestTable_1.getColumnModel().getColumn(1).setPreferredWidth(120);
		addTestTable_1.getColumnModel().getColumn(1).setMinWidth(120);
		addTestTable_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(2).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(3).setMinWidth(100);
		addTestTable_1.getColumnModel().getColumn(4).setPreferredWidth(100);
		addTestTable_1.getColumnModel().getColumn(4).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.LEFT);
		addTestTable_1.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		addTestTable_1.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);

		finalTotalValue = total;

		System.out.println(finalTotalValue + "   " + total);
		lblTotalcharges.setText("`" + finalTotalValue + "");
		lblTotalcharges.setFont(customFont);
	}

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				Object obj = resultSet.getObject(1);
				itemID.add((obj == null) ? "" : resultSet.getObject(1).toString());
				itemName.addElement((obj == null) ? "" : resultSet.getObject(2).toString());
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

	public void itemValue() {
		itemValue = qtyIssued * price;
		itemValue = Math.round(itemValue * 100.000) / 100.000;
		costET.setText(itemValue + "");

	}

	public void getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);

		double purchase = 0, tot = 0, tot1 = 0, sp = 0;

		String formulaActive = "";
		String formulaApplied = "";
		flag = false;
		quantity = 0;
		expiryDateSTR = "";
		try {
			while (resultSet.next()) {
				expiryDateSTR = resultSet.getObject(9).toString();
				flag = Boolean.parseBoolean(resultSet.getObject(19).toString());
				if (flag) {
					batchNameCB.setEnabled(true);
				} else {
					batchNameCB.setEnabled(false);
				}
				quantity = Integer.parseInt(resultSet.getObject(8).toString());
				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13).toString());
				purchase = Double.parseDouble(resultSet.getObject(20).toString());
				// System.out.println("mmmmm"+purchase);
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
				formulaApplied = resultSet.getObject(17).toString();
				try {
					packSize = Integer.parseInt(resultSet.getObject(4).toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;

				mrp = Double.parseDouble(resultSet.getObject(11).toString());
				if (formulaActive.equals("1")) {
					sp = price;
				} else {
					if (formulaApplied.equals("NA")) {
						if (purchase > 10000 && purchase <= 20000) {
							System.out.println("10000" + purchase);

							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							System.out.println("tempvar2" + tempvar2);
							double mrpwithouttax = tempvar1 - tempvar2;
							System.out.println("mrpwithouttax" + mrpwithouttax);
							double temp = 1.15 * purchase;
							System.out.println("temp" + temp);
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							System.out.println("20000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.10 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.10;
						} else if (purchase > 30000) {
							System.out.println("30000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.05 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.05;
						} else if (purchase > 5000 && purchase <= 10000) {
							System.out.println("5000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.25 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 1000 && purchase <= 5000) {
							System.out.println("1000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.30 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 250 && purchase <= 1000) {
							System.out.println("250" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 0 && purchase <= 250) {
							System.out.println("0" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else {
							System.out.println("mmmmmyyyyy" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
					} else {
						System.out.println(formulaApplied + "forumaula" + " " + purchase + "purchase");
						sp = purchase * Double.parseDouble(formulaApplied);
					}

				}
			}

			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023


			price = (double) Math.round(sp * 100) / 100;
			double k = price * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;

			double s = price * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			price = price + k;
			price = price + s;

			price = Math.round(price * 100.000) / 100.000;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemBatchNameDpt(String item_id) {

		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.getBatchName(item_id, departmentID);
		itemBatchName.removeAllElements();
		batchID.clear();
		batchID.add("0");
		itemBatchName.addElement("select Batch");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(3).toString());
				itemBatchName.addElement(resultSet.getObject(1).toString() + "(Batch" + (i + 1) + ")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		batchNameCB.setModel(itemBatchName);
		if (i > 0) {
			batchNameCB.setSelectedIndex(0);
		}
	}
	public void getItemBatchName(String index) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();
		batchID.clear();
		batchID.add("0");
		itemBatchName.addElement("select Batch");
		int i = 0;
		try {
			while (resultSet.next()) {

				batchID.add(resultSet.getObject(1).toString());
				itemBatchName.addElement(resultSet.getObject(2).toString() + "(Batch-" + (i + 1) + ")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
		batchNameCB.setModel(itemBatchName);
		if (i > 0) {
			batchNameCB.setSelectedIndex(0);
		}
	}

	public void getItemBatch(String batch_ID, String item_id) {
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(item_id);
		ResultSet rs = batchTrackingDBConnection.itemBatchDetail(batch_ID);
		double purchase = 0, tot = 0, tot1 = 0, sp = 0,fixedPrice=0;
		price=0;
		String formulaActive = "";
		String formulaApplied = "";
		quantity = 0;
		expiryDateSTR = "";


		try {
			while (resultSet.next()) {
				formulaActive = resultSet.getObject(15).toString();
				formulaApplied = resultSet.getObject(17).toString();
				fixedPrice=Double.parseDouble(resultSet.getObject(10).toString());
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		try {
			while (rs.next()) {
				quantity = Integer.parseInt(rs.getObject(1).toString());
				expiryDateSTR = rs.getObject(2).toString();
				taxValue = Double.parseDouble(rs.getObject(3).toString());
				surchargeValue = Double.parseDouble(rs.getObject(4).toString());
				purchase = Double.parseDouble(rs.getObject(8).toString());

				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;

				try {
					packSize = Integer.parseInt(rs.getObject(6).toString().trim());
				} catch (Exception e) {
					// TODO: handle exception
				}
				double tax = taxValue + surchargeValue;

				mrp = Double.parseDouble(rs.getObject(7).toString());
				System.out.println("price: " + price + " mrp: " + mrp);
				if (formulaActive.equals("1")) {
					sp = fixedPrice;
				} else {
					if (formulaApplied.equals("NA")) {
						if (purchase > 10000 && purchase <= 20000) {
							System.out.println("10000" + purchase);

							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							System.out.println("tempvar2" + tempvar2);
							double mrpwithouttax = tempvar1 - tempvar2;
							System.out.println("mrpwithouttax" + mrpwithouttax);
							double temp = 1.15 * purchase;
							System.out.println("temp" + temp);
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.15;
						} else if (purchase > 20000 && purchase <= 30000) {
							System.out.println("20000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.10 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.10;
						} else if (purchase > 30000) {
							System.out.println("30000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.05 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
							// sp = purchase * 1.05;
						} else if (purchase > 5000 && purchase <= 10000) {
							System.out.println("5000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.25 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 1000 && purchase <= 5000) {
							System.out.println("1000" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.30 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 250 && purchase <= 1000) {
							System.out.println("250" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 1.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else if (purchase > 0 && purchase <= 250) {
							System.out.println("0" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						} else {
							System.out.println("mmmmmyyyyy" + purchase);
							double tempvar1 = mrp / packSize;
							double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
							double mrpwithouttax = tempvar1 - tempvar2;
							double temp = 2.5 * purchase;
							if (mrpwithouttax > temp) {
								sp = temp;

							} else {
								double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
								sp = mrpless1prcnt;
							}
						}
					} else {
						System.out.println(formulaApplied + "forumaula" + " " + purchase + "purchase");
						sp = purchase * Double.parseDouble(formulaApplied);
					}

				}
			}

			sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023


			price = (double) Math.round(sp * 100) / 100;
			double k = price * (taxValue / 100.0f);
			k = Math.round(k * 100.000) / 100.000;

			double s = price * (surchargeValue / 100.0f);
			s = Math.round(s * 100.000) / 100.000;

			price = price + k;
			price = price + s;

			price = Math.round(price * 100.000) / 100.000;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		batchTrackingDBConnection.closeConnection();
	}

	public JTextField getSlipNoET() {
		return slipNoET;
	}

	public JTextField getCostET() {
		return costET;
	}
}
