package hms.store.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.departments.gui.DepartmentMain;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ItemsMSDBConnection;
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
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;

public class TransferStock extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private Timer timer;
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	double totalChargesIPD = 0;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemID1 = new Vector<String>();
	String itemIDSTRM = "",item;
	Vector<String> itemMSID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Vector<String> itemTypeV = new Vector<String>();
	Vector<String> itemMRPV = new Vector<String>();
	Vector<String> itemMeasV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> batchID1 = new Vector<String>();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName1 = new DefaultComboBoxModel();
	Object[][] ObjectArray_ListOfexamsSpecs;
	double ipd_advance = 0, ipd_balance = 0,batchQty = 0,batchQty1 = 0;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",itemBatchNameSTR = "",itemBatchNameSTR1 = "",
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
	String itemIDSTR,itemIDSTR1, itemNameSTR,itemNameSTR1,itemNameSTRM, itemDescSTR, itemDescSTR11, taxTypeSTR, taxValueSTR,
	expiryDateSTR = "",expiryDateSTR1 = "",expiryDateSTR11="", issuedDateSTR = "", dueDateSTR = "",
	previouseStock = "",itemtypeSTR="",itemtypeSTR1="",
	departmentName = DepartmentMain.departmentName,
	departmentID = DepartmentMain.departmentID,itemDescSTR1="";
	int quantity1=0,select;
	String batchIDSTR="";
	String batchIDSTR1="";
	int qtyIssued = 0, afterIssued = 0,qtyIssued1 = 0, afterIssued1 = 0, finalTaxValue = 0,
			finalDiscountValue = 0;
	int quantity = 0;
	String hms_id;
	double price = 0, finalTotalValue = 0, taxValue = 0, surchargeValue = 0,taxValue1 = 0,surchargeValue1 = 0,price1 = 0,
			itemValue = 0,itemValue1 = 0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName1 = new DefaultComboBoxModel();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	private JLabel lblTotalcharges;
	JComboBox itemNameCB,batchcb;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JTextField enterQtyET;
	private JTextField itemSearchET;
	private JTextField itemDescET;
	private JTextField qtyInHandET;
	private JTextField remainingItemET;
	private JButton btnRemove;

	double mrp = 0;
	double mrp1 = 0;
	int packSize = 1;
	String packSize1;
	private JTextField batchQtyET;
	private JComboBox comboBox;
	private JTextField enterQtyET_1;
	private JTextField itemSearchET_1;
	private JTextField itemDescET_1;
	private JTextField qtyInHandET_1;
	private JTextField remainingItemET_1;
	private JTextField batchQtyET_1;
	private JButton btnAdd_1;
	private JButton btnRemove_1;
	private JComboBox<String> itemNameCB_1;
	private JComboBox<String> batchcb_1;
	private JPanel panel_2_1;
	private JPanel panel_2;
	private JTextField expirtDateET;
	private JTextField expirtDateET1;
	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new TransferStock().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public TransferStock() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IndoorPillsEntryFromStore.class
				.getResource("/icons/rotaryLogo.png")));
		setTitle("Transfer Stock ");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(50, 50, 1054, 400);
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
		panel_4.setBounds(6, 5, 1178, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = dateFormat.StringToMysqlDate(new Date());

		panel_2 = new JPanel();
		panel_2.setBounds(12, 55, 509, 286);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Hospital Item Detail :", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_5.setBounds(5, 11, 494, 198);
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
				String bool = null;
				ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
				ResultSet resultSet = itemsDBConnection.getalloweditem(itemIDSTR);
				try {
					while (resultSet.next()) {
						bool=resultSet.getObject(1).toString();
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(bool!="")
				{
					itemSearchET_1.setText(bool);
				}
				//				getItemStock(itemIDSTR);
				getItemDetail(itemIDSTR);
				getItemBatchName(itemIDSTR);
				if (itemName.getSize() > 0) {
					afterIssued = quantity - qtyIssued;
					itemDescET.setText("" + itemDescSTR);
					enterQtyET.setText("");

					remainingItemET.setText("" + afterIssued);
					expirtDateET.setText("" + expiryDateSTR);
					qtyInHandET.setText("" + quantity);

				}

			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		itemNameCB.setBounds(277, 16, 207, 25);
		panel_5.add(itemNameCB);

		enterQtyET = new JTextField();
		enterQtyET.setHorizontalAlignment(SwingConstants.RIGHT);
		enterQtyET.setBounds(115, 168, 128, 25);
		enterQtyET.addKeyListener(new KeyAdapter() {
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
				itemValue();
				// costET.setText((qtyIssued * price) + "");
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
				itemValue();
				// costET.setText((qtyIssued * price) + "");
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
				itemValue();
				// costET.setText((qtyIssued * price) + "");
			}
		});
		panel_5.add(enterQtyET);
		enterQtyET.setColumns(10);

		JLabel lblAmount = new JLabel("Enter Qty.:");
		lblAmount.setBounds(23, 173, 82, 14);
		panel_5.add(lblAmount);

		timer = new Timer(500, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				timer.stop();
				String str = itemSearchET.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					enterQtyET.setText("");
					remainingItemET.setText("");
					expirtDateET.setText("");
					qtyInHandET.setText("");
					itemBatchName.removeAllElements();
					batchcb.setModel(itemBatchName);
					itemDescET.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemSearchET_1.setText("");

				}
			}
		});
		itemSearchET = new JTextField();
		itemSearchET.setBounds(109, 17, 158, 25);
		panel_5.add(itemSearchET);
		itemSearchET.setColumns(10);

		itemSearchET.setColumns(10);
		itemSearchET.getDocument().addDocumentListener(new DocumentListener() {
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
		qtyInHandET.setBounds(356, 88, 128, 25);
		panel_5.add(qtyInHandET);
		qtyInHandET.setColumns(10);

		JLabel lblQtyInhand = new JLabel("Qty. In Hand :");
		lblQtyInhand.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblQtyInhand.setBounds(247, 88, 94, 25);
		panel_5.add(lblQtyInhand);

		JLabel lblAfterEntry = new JLabel("After Entry :");
		lblAfterEntry.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblAfterEntry.setBounds(247, 122, 100, 25);
		panel_5.add(lblAfterEntry);

		remainingItemET = new JTextField();
		remainingItemET.setEditable(false);
		remainingItemET.setColumns(10);
		remainingItemET.setBounds(356, 123, 128, 25);
		panel_5.add(remainingItemET);

		JLabel Batch = new JLabel("Batch:");
		Batch.setBounds(16, 93, 82, 14);
		panel_5.add(Batch);

		batchcb = new JComboBox();
		batchcb.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				batchIDSTR="";
				if (batchcb.getSelectedIndex() > -1) {
					itemBatchNameSTR = batchcb.getSelectedItem().toString();
					batchIDSTR = batchID.get(batchcb.getSelectedIndex());
					System.out.println("Arun :"+batchcb.getSelectedIndex());
				}

				if (!batchIDSTR.equals("")) {
					getItemBatch(batchIDSTR);
					getItemDetail(itemIDSTR);
				}

			}
		});
		batchcb.setFont(new Font("Tahoma", Font.PLAIN, 12));
		batchcb.setBounds(108, 87, 128, 25);
		panel_5.add(batchcb);

		JLabel lblBatchQty = new JLabel("Batch Qty:");
		lblBatchQty.setBounds(16, 128, 82, 14);
		panel_5.add(lblBatchQty);

		batchQtyET = new JTextField();
		batchQtyET.setEditable(false);
		batchQtyET.setColumns(10);
		batchQtyET.setBounds(108, 125, 128, 25);
		panel_5.add(batchQtyET);

		expirtDateET = new JTextField();
		expirtDateET.setEditable(false);
		expirtDateET.setColumns(10);
		expirtDateET.setBounds(356, 160, 128, 25);
		panel_5.add(expirtDateET);

		JLabel expirytxt = new JLabel("Expiry Date :");
		expirytxt.setFont(new Font("Dialog", Font.PLAIN, 12));
		expirytxt.setBounds(247, 159, 100, 25);
		panel_5.add(expirytxt);
		final JButton btnAdd = new JButton("Transfer Stock");
		btnAdd.setIcon(null);
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (itemNameCB.getSelectedIndex()<0) {
					JOptionPane.showMessageDialog(null, "Please select Hospital Store item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemNameCB_1.getSelectedIndex()<0) {
					JOptionPane.showMessageDialog(null, "Please select Medical Store item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (enterQtyET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (batchQty - qtyIssued < 0) {
					JOptionPane.showMessageDialog(null,
							"Issued Quantity is greater than Batch Quantity",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
				ItemsDBConnection itemsDBConnection1 = new ItemsDBConnection();
				BatchTrackingDBConnection batchTrackingDBConnection1 = new BatchTrackingDBConnection();
				try {
					itemsDBConnection.addStock(itemIDSTR,itemIDSTR1,enterQtyET.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				itemsDBConnection.closeConnection();

				try {
					itemsDBConnection1.subtractStock(itemIDSTR,enterQtyET.getText().toString());
					batchTrackingDBConnection1.subtractStock(
							batchIDSTR+"", enterQtyET.getText().toString(),
							DateFormatChange.StringToMysqlDate(new Date()),
							StoreMain.userName);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				data[0] = itemIDSTR1;
				data[1] = itemNameCB_1.getSelectedItem().toString();
				data[2] = itemDescET_1.getText().toString();
				String str = batchcb.getSelectedItem().toString();
				String[] arrOfStr = str.split("\\(", 2);
				for (String a : arrOfStr)
				{
					data[3]=a;
					break;
				}
				data[4] = enterQtyET.getText().toString();
				data[5] =enterQtyET.getText().toString();
				data[6] =expirtDateET.getText().toString();
				data[7] = ""
						+ DateFormatChange.StringToMysqlDate(new Date());
				data[8] = "" + timeFormat.format(cal1.getTime());
				data[9] = ""
						+ DateFormatChange.StringToMysqlDate(new Date());
				data[10] = price  + "";
				data[11] = mrp+"";
				data[12] = packSize+"";
				data[13] = taxValue+"";
				data[14] =surchargeValue+"";

				data[17] = price+"";
				double k = (itemValue) * (taxValue / 100.0f);
				double surcharge = (itemValue ) * (surchargeValue / 100.0f);
				surcharge = Math.round(surcharge * 100.00) / 100.00;
				k = Math.round(k * 100.00) / 100.00;
				data[15] = k+"";
				data[16] = surcharge+"";
				ItemsMSDBConnection batchTrackingDBConnection = new ItemsMSDBConnection();
				try {
					batchTrackingDBConnection
					.inserDataBatchNew(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Tranferred Successfully", "Tranferred Item",
						JOptionPane.INFORMATION_MESSAGE);
				itemSearchET.setText("");
				itemSearchET_1.setText("");
			}
		});
		btnAdd.setBounds(182, 230, 133, 33);
		panel_2.add(btnAdd);

		btnRemove = new JButton("New Item");
		btnRemove.setIcon(null);
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				NewItemMS newItem = new NewItemMS();
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		btnRemove.setBounds(339, 230, 125, 33);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(129, 524, 106, 23);
		panel_2.add(lblTotalCharges);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(227, 527, 144, 20);
		panel_2.add(lblTotalcharges);

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(IndoorPillsEntryFromStore.class
				.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else if (itemID.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					Dept_PillsRegisterDBConnection dept_PillsRegisterDBConnection = new Dept_PillsRegisterDBConnection();
					DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
					data[0] = "" + departmentID; // dept id
					data[1] = departmentName; // dept name
					data[2] = "" + doctorID; // user id
					data[3] = doctorName; // user name
					data[4] = DepartmentMain.userID; // user id
					data[5] = DepartmentMain.userName; // user name
					data[6] = p_id;
					data[7] = p_name;
					data[14] = date;
					data[15] = "" + timeFormat.format(cal1.getTime());
					data[16] = "" + ipd_id; // ipd no
					data[17] = "INDOOR";
					data[18] = "OK";
					for (int i = 0; i < itemIDV.size(); i++) {
						data[8] = (String) itemIDV.get(i);
						data[9] = (String) "" + itemNameV.get(i);
						data[10] = (String) itemDescV.get(i);
						data[11] = (String) itemPriceV.get(i);
						data[12] = (String) issuedQtyV.get(i);
						data[13] = (String) totalPriceV.get(i);
						try {
							dept_PillsRegisterDBConnection
							.insertDepartmentPillsRegister(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					departmentStockDBConnection.closeConnection();
					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[18];
					for (int i = 0; i < itemIDV.size(); i++) {
						data1[0] = "" + ipd_id;
						data1[1] = (String) "" + itemNameV.get(i);

						data1[2] = (String) itemDescV.get(i);
						data1[3] = (String) totalPriceV.get(i);

						data1[4] = ""
								+ dateFormat.StringToMysqlDate(new Date());
						data1[5] = "" + timeFormat.format(cal1.getTime());
						data1[6] = (String) itemIDV.get(i);
						data1[7] = "" + p_id;
						data1[8] = "" + p_name;
						data1[9] = itemPriceV.get(i);
						data1[10] = issuedQtyV.get(i);
						data1[11] = db.retrieveItemSubCategory(itemIDV.get(i));
						data1[12] = StoreMain.userName;
						data1[13] = db.retrieveItemCategory(itemIDV.get(i));


						data1[14] = "N/A";
						data1[15]=itemMRPV.get(i);
						data1[16]=itemMeasV.get(i);
						data1[17]=itemTypeV.get(i);
						try {
							db.inserDataNewExpenses(data1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
						//						try {
						//							db.inserData(data1);
						//						} catch (Exception e) {
						//							// TODO Auto-generated catch block
						//							e.printStackTrace();
						//							db.closeConnection();
						//						}
					}
					db.closeConnection();
					double tot = totalChargesIPD + finalTotalValue;
					double finalValue = Math.round(tot * 100.0) / 100.0;
					IPDDBConnection ipddbConnection = new IPDDBConnection();
					try {
						ipddbConnection.updateTotalAmount(ipd_id, finalValue
								+ "");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						ipddbConnection.closeConnection();
						e.printStackTrace();
					}
					ipddbConnection.closeConnection();
					dept_PillsRegisterDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);

					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(1021, 480, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(1021, 535, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IndoorPillsEntryFromStore.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JLabel label_8 = new JLabel("");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setIcon(new ImageIcon(IndoorPillsEntryFromStore.class
				.getResource("/icons/graphics-hospitals-575145.gif")));
		label_8.setBounds(1021, 365, 103, 105);
		panel_4.add(label_8);

		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {

				select=comboBox.getSelectedIndex();
				//System.out.print(select+"sskdkdksddssss");
				if(select==0) {
					btnAdd.setEnabled(true);
					btnAdd_1.setEnabled(false);
				}
				else {
					btnAdd.setEnabled(false);
					btnAdd_1.setEnabled(true);

				}

			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Hospital To Medical Store", "Medical Store to Hospital"}));
		comboBox.setBounds(449, 19, 209, 24);
		panel_4.add(comboBox);

		panel_2_1 = new JPanel();
		panel_2_1.setLayout(null);
		panel_2_1.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,

				TitledBorder.TOP, null, null));
		panel_2_1.setBounds(536, 55, 509, 286);
		panel_4.add(panel_2_1);

		JPanel panel_5_1 = new JPanel();
		panel_5_1.setLayout(null);
		panel_5_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "MS++ Item Detail :", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		panel_5_1.setBounds(5, 11, 494, 198);
		panel_2_1.add(panel_5_1);

		JLabel lblExamName_1 = new JLabel("Exam Name :");
		lblExamName_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblExamName_1.setBounds(6, 16, 0, 25);
		panel_5_1.add(lblExamName_1);

		itemNameCB_1 = new JComboBox<String>();
		itemNameCB_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		itemNameCB_1.setBounds(277, 16, 207, 25);
		panel_5_1.add(itemNameCB_1);
		itemNameCB_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					itemNameSTR1 = itemNameCB_1.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB_1.getSelectedIndex() > -1) {
					itemIDSTR1 = itemID1.get(itemNameCB_1.getSelectedIndex());
				}
				quantity1=0;
				itemDescSTR1="";
				batchQty1=0;
				expiryDateSTR11="";
				//				getItemStock(itemIDSTR);

				getItemDetailMSD(itemIDSTR1);	
				getItemBatchNameMS(itemIDSTR1);
				if (itemName1.getSize() > 0) {
					afterIssued1 = quantity1 - qtyIssued1;
					itemDescET_1.setText("" + itemDescSTR1);
					enterQtyET_1.setText("");
					qtyInHandET_1.setText(quantity1+"");

					remainingItemET_1.setText("" + afterIssued1);

				}
			}
		});
		enterQtyET_1 = new JTextField();
		enterQtyET_1.setHorizontalAlignment(SwingConstants.RIGHT);
		enterQtyET_1.setColumns(10);
		enterQtyET_1.setBounds(115, 168, 128, 25);
		enterQtyET_1.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		enterQtyET_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = enterQtyET_1.getText() + "";
				if (!str.equals("")) {

					qtyIssued1 = Integer.parseInt(str);

				} else {

					qtyIssued1 = 0;

				}
				afterIssued1 = quantity1 - qtyIssued1;
				double afterIssuedBatch = batchQty1 - qtyIssued1;
				remainingItemET_1.setText("" + afterIssued1);
				itemValue();
				// costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = enterQtyET_1.getText() + "";
				if (!str.equals("")) {

					qtyIssued1 = Integer.parseInt(str);

				} else {

					qtyIssued1 = 0;

				}
				afterIssued1 = quantity1 - qtyIssued1;
				double afterIssuedBatch = batchQty1 - qtyIssued1;
				remainingItemET_1.setText("" + afterIssued1);
				itemValue();
				// costET.setText((qtyIssued * price) + "");
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = enterQtyET_1.getText() + "";
				if (!str.equals("")) {

					qtyIssued1 = Integer.parseInt(str);

				} else {

					qtyIssued1 = 0;

				}
				afterIssued1 = quantity1 - qtyIssued1;
				double afterIssuedBatch = batchQty1 - qtyIssued1;

				remainingItemET_1.setText("" + afterIssued1);
				itemValue();
				// costET.setText((qtyIssued * price) + "");
			}
		});
		panel_5_1.add(enterQtyET_1);

		JLabel lblAmount_1 = new JLabel("Enter Qty.:");
		lblAmount_1.setBounds(23, 173, 82, 14);
		panel_5_1.add(lblAmount_1);

		itemSearchET_1 = new JTextField();
		itemSearchET_1.setColumns(10);
		itemSearchET_1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = itemSearchET_1.getText() + "";

				if (!str.equals("")) {
					getItemNameMS(str);
				} else {

					enterQtyET_1.setText("");
					remainingItemET_1.setText("");
					expirtDateET1.setText("");
					qtyInHandET_1.setText("");
					batchQtyET_1.setText("");
					itemBatchName1.removeAllElements();
					itemDescET_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);
					batchcb_1.setModel(itemBatchName1);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = itemSearchET_1.getText() + "";
				if (!str.equals("")) {
					getItemNameMS(str);
					//					getItemNameMedicalstore(itemNameSTR);
				} else {

					batchQtyET_1.setText("");
					enterQtyET_1.setText("");
					remainingItemET_1.setText("");
					expirtDateET1.setText("");
					qtyInHandET_1.setText("");
					itemBatchName1.removeAllElements();
					itemDescET_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);
					batchcb_1.setModel(itemBatchName1);

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = itemSearchET_1.getText() + "";
				if (!str.equals("")) {
					getItemNameMS(str);
					//					getItemNameMedicalstore(itemNameSTR);
				} else {

					batchQtyET_1.setText("");
					enterQtyET_1.setText("");
					remainingItemET_1.setText("");
					expirtDateET1.setText("");
					qtyInHandET_1.setText("");
					itemBatchName1.removeAllElements();
					itemDescET_1.setText("");
					itemName1.removeAllElements();
					itemNameCB_1.setModel(itemName1);
					batchcb_1.setModel(itemBatchName1);

				}
			}
		});
		itemSearchET_1.setBounds(109, 17, 158, 25);
		panel_5_1.add(itemSearchET_1);

		JLabel lblSearchItem_1 = new JLabel("Search Item :");
		lblSearchItem_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearchItem_1.setBounds(16, 22, 89, 14);
		panel_5_1.add(lblSearchItem_1);

		JLabel lblDescription_1 = new JLabel("Description :");
		lblDescription_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDescription_1.setBounds(16, 53, 94, 25);
		panel_5_1.add(lblDescription_1);

		itemDescET_1 = new JTextField();
		itemDescET_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		itemDescET_1.setEditable(false);
		itemDescET_1.setColumns(10);
		itemDescET_1.setBounds(109, 52, 375, 25);
		panel_5_1.add(itemDescET_1);

		qtyInHandET_1 = new JTextField();
		qtyInHandET_1.setEditable(false);
		qtyInHandET_1.setColumns(10);
		qtyInHandET_1.setBounds(356, 88, 128, 25);
		panel_5_1.add(qtyInHandET_1);

		JLabel lblQtyInhand_1 = new JLabel("Qty. In Hand :");
		lblQtyInhand_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblQtyInhand_1.setBounds(247, 88, 94, 25);
		panel_5_1.add(lblQtyInhand_1);

		JLabel lblAfterEntry_1 = new JLabel("After Entry :");
		lblAfterEntry_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAfterEntry_1.setBounds(247, 122, 100, 25);
		panel_5_1.add(lblAfterEntry_1);

		remainingItemET_1 = new JTextField();
		remainingItemET_1.setEditable(false);
		remainingItemET_1.setColumns(10);
		remainingItemET_1.setBounds(356, 123, 128, 25);
		panel_5_1.add(remainingItemET_1);

		JLabel Batch_1 = new JLabel("Batch:");
		Batch_1.setBounds(16, 93, 82, 14);
		panel_5_1.add(Batch_1);

		batchcb_1 = new JComboBox<String>();
		batchcb_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		batchcb_1.setBounds(108, 87, 129, 25);
		panel_5_1.add(batchcb_1);
		batchcb_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				try {
					itemBatchNameSTR1 = batchcb_1.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (batchcb_1.getSelectedIndex() > -1) {
					batchIDSTR1= batchID1.get(batchcb_1.getSelectedIndex());
				}
				batchQty1=0;
				expiryDateSTR11="";
				getItemBatchMS(batchIDSTR1);



			}
		});
		JLabel lblBatchQty_1 = new JLabel("Batch Qty:");
		lblBatchQty_1.setBounds(16, 128, 82, 14);
		panel_5_1.add(lblBatchQty_1);

		batchQtyET_1 = new JTextField();
		batchQtyET_1.setEditable(false);
		batchQtyET_1.setColumns(10);
		batchQtyET_1.setBounds(108, 125, 128, 25);
		panel_5_1.add(batchQtyET_1);

		expirtDateET1 = new JTextField();
		expirtDateET1.setEditable(false);
		expirtDateET1.setColumns(10);
		expirtDateET1.setBounds(356, 168, 128, 25);
		panel_5_1.add(expirtDateET1);

		JLabel expirytxt_1 = new JLabel("Expiry Date :");
		expirytxt_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		expirytxt_1.setBounds(247, 167, 100, 25);
		panel_5_1.add(expirytxt_1);
		btnAdd_1 = new JButton("Transfer Stock");
		btnAdd_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (itemNameCB_1.getSelectedIndex()<0) {
					JOptionPane.showMessageDialog(null, "Please select Medical Store item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemNameCB.getSelectedIndex()<0) {
					JOptionPane.showMessageDialog(null, "Please select Hospital item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (enterQtyET_1.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (batchQty1 - qtyIssued1 < 0) {
					JOptionPane.showMessageDialog(null,
							"Issued Quantity is greater than Batch Quantity",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				ItemsDBConnection itemsDBConnection1 = new ItemsDBConnection();
				try {
					itemsDBConnection1.addStock(itemIDSTR,enterQtyET_1.getText().toString());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				itemsDBConnection1.closeConnection();
				ItemsMSDBConnection itemsDBConnection2 = new ItemsMSDBConnection();
				ItemsMSDBConnection batchtracking = new ItemsMSDBConnection();
				try {
					itemsDBConnection2.subtractStock(itemIDSTR1,enterQtyET_1.getText().toString());
					batchtracking.subtractStock(
							batchIDSTR1+"", enterQtyET_1.getText().toString(),
							DateFormatChange.StringToMysqlDate(new Date()),
							StoreMain.userName);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				data[0] = itemIDSTR;
				data[1] = itemNameCB.getSelectedItem().toString();
				data[2] = itemDescET.getText().toString();
				String str = batchcb_1.getSelectedItem().toString();
				String[] arrOfStr = str.split("\\(", 2);
				for (String a : arrOfStr)
				{
					data[3]=a;
					break;
				}
				data[4] = enterQtyET_1.getText().toString();
				data[5] =enterQtyET_1.getText().toString();
				data[6] = expirtDateET1.getText().toString();
				data[7] = ""
						+ DateFormatChange.StringToMysqlDate(new Date());
				data[8] = "" + timeFormat.format(cal1.getTime());
				data[9] = ""
						+ DateFormatChange.StringToMysqlDate(new Date());
				data[10] = price1  + "";
				data[11] = mrp1+"";
				data[12] = packSize1;
				data[13] = taxValue1+"";
				data[14] =surchargeValue1+"";

				data[17] = price1+"";
				double k = (itemValue1) * (taxValue1 / 100.0f);
				double surcharge = (itemValue1 ) * (surchargeValue1 / 100.0f);
				surcharge = Math.round(surcharge * 100.00) / 100.00;
				k = Math.round(k * 100.00) / 100.00;
				data[15] = k+"";
				data[16] = surcharge+"";
				ItemsDBConnection batchTrackingDBConnection = new ItemsDBConnection();
				try {
					batchTrackingDBConnection
					.inserDataBatchNew(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Tranferred Successfully", "Tranferred Item",
						JOptionPane.INFORMATION_MESSAGE);
				itemSearchET.setText("");
				itemSearchET_1.setText("");
			}

		});
		btnAdd_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnAdd_1.setEnabled(false);
		btnAdd_1.setBounds(182, 230, 133, 33);
		panel_2_1.add(btnAdd_1);

		btnRemove_1 = new JButton("New Item");
		btnRemove_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewItemMS o=new NewItemMS();
				o.setModal(true);
				o.setVisible(true);
				
			}
		});
		btnRemove_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnRemove_1.setBounds(339, 230, 125, 33);
		panel_2_1.add(btnRemove_1);

		JLabel lblTotalCharges_1 = new JLabel("Total Charges :");
		lblTotalCharges_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTotalCharges_1.setBounds(129, 524, 106, 23);
		panel_2_1.add(lblTotalCharges_1);

		JLabel lblTotalcharges_1 = new JLabel("");
		lblTotalcharges_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblTotalcharges_1.setBounds(227, 527, 144, 20);
		panel_2_1.add(lblTotalcharges_1);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner",
				new PropertyChangeListener() {
			public void propertyChange(
					final PropertyChangeEvent e) {
				if (e.getNewValue() instanceof JTextField) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							JTextField textField = (JTextField) e
									.getNewValue();
							textField.selectAll();
						}
					});

				}
			}
		});
	}

	public void getPatientsID(String index) {
		//		lastOPDDateLB.setText("Last Exam : ");
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
		//		patientIDCB.setModel(patientID);
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
		//		getIPDDATA();
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
		patientDBConnection.closeConnection();
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
	public void getItemNameMS(String index) {

		ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName1.removeAllElements();
		itemID1.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID1.add(resultSet.getObject(1).toString());
				itemName1.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB_1.setModel(itemName1);
		if (i > 0) {
			itemNameCB_1.setSelectedIndex(0);
		}
	}
	public void itemValue() {
		itemValue = qtyIssued * price;
		// System.out.println("unit" + itemValue);
		double k = itemValue * (taxValue / 100.0f);
		k = Math.round(k * 100.000) / 100.000;
		double s = itemValue * (surchargeValue / 100.0f);
		s = Math.round(s * 100.000) / 100.000;

		itemValue = itemValue + k;
		itemValue = itemValue + s;

		itemValue = Math.round(itemValue * 100.000) / 100.000;
		//		costET.setText(itemValue + "");

	}
	public void itemValue1() {
		itemValue1 = qtyIssued1 * price1;
		// System.out.println("unit" + itemValue);
		double k = itemValue1 * (taxValue1 / 100.0f);
		k = Math.round(k * 100.000) / 100.000;
		double s = itemValue1 * (surchargeValue1 / 100.0f);
		s = Math.round(s * 100.000) / 100.000;

		itemValue1 = itemValue1 + k;
		itemValue1 = itemValue1 + s;

		itemValue = Math.round(itemValue * 100.000) / 100.000;
		//		costET.setText(itemValue + "");

	}
	//	public void getItemDetailMS(String index) {
	//
	//		GeneralDBConnection formula = new GeneralDBConnection();
	//		double value = Double.parseDouble(formula.retrieveFormula1());
	//		formula.closeConnection();
	//		ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
	//		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
	//	
	//		double purchase = 0, tot = 0, tot1 = 0, sp = 0;
	//	
	//		String formulaActive = "";
	//		String formulaApplied = "";
	//		try {
	//			while (resultSet.next()) {
	//			
	//				itemDescSTR11 = resultSet.getObject(3).toString();
	//				
	//				quantity1 = Integer.parseInt(resultSet.getObject(4)
	//						.toString());
	//			}
	//
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		itemsDBConnection.closeConnection();
	//
	//	}
	public void getItemBatchName(String index) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();

		batchID.removeAllElements();;
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.addElement(resultSet.getObject(1).toString());
				itemBatchName.addElement(resultSet.getObject(2).toString()+"(Batch-"+(i+1)+")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
		batchcb.setModel(itemBatchName);
		if (i > 0) {
			batchcb.setSelectedIndex(0);
		}
	}
	public void getItemBatchNameMS(String index) {

		ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
		ResultSet resultSet = itemsDBConnection.itemBatch(index);
		itemBatchName1.removeAllElements();

		batchID1.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID1.add(resultSet.getObject(1).toString());



				// itemBatchName1.addItem(new ComboItem("Visible String 1",
				// "Value 1"));
				itemBatchName1.addElement(resultSet.getObject(2).toString()+"(Batch-"+(i+1)+")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		batchcb_1.setModel(itemBatchName1);
		if (i > 0) {
			batchcb_1.setSelectedIndex(0);
		}
	}
	public void getItemDetailMSD(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);

		double purchase = 0, tot = 0, tot1 = 0, sp = 0;

		String formulaActive = "";
		String formulaApplied = "";
		try {
			while (resultSet.next()) {
				itemtypeSTR1=resultSet.getObject(16).toString();
				itemDescSTR1 = resultSet.getObject(3).toString();
				taxValue1 = Double
						.parseDouble(resultSet.getObject(6).toString());
				surchargeValue1 = Double.parseDouble(resultSet.getObject(13)
						.toString());
				quantity1 = Integer.parseInt(resultSet.getObject(8)
						.toString());
				purchase = Double
						.parseDouble(resultSet.getObject(7).toString());
				price1 = Double.parseDouble(resultSet.getObject(10).toString());
				//				tot = (double) Math.round(purchase * value * 100) / 100;
				//				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				//				formulaActive = resultSet.getObject(14).toString();
				//				formulaApplied = resultSet.getObject(15).toString();
				//				try {
				packSize1 =(resultSet.getObject(4)
						.toString());
				//				} catch (Exception e) {
				//					// TODO: handle exception
				//				}
				//				double tax = taxValue1 + surchargeValue1;
				//
				mrp1 = Double.parseDouble(resultSet.getObject(11).toString());
				//				if (formulaActive.equals("1")) {
				//					sp = price1;
				//				} else {
				//					if (formulaApplied.equals("NA")) {
				//						if (purchase >= 10000 && purchase <= 20000) {
				//							
				//							
				//							double tempvar1 = mrp1 / packSize1;
				//							double tempvar2 = tempvar1
				//									* ((taxValue1 + surchargeValue1) / 100);
				//							double mrpwithouttax = tempvar1 - tempvar2;
				//							double temp = 1.15* purchase;
				//							if (mrpwithouttax > temp) {
				//								sp = temp;
				//
				//							} else {
				//								double mrpless1prcnt = mrpwithouttax
				//										- (mrpwithouttax * 0.01);
				//								sp = mrpless1prcnt;
				//							}
				////							sp = purchase * 1.15;
				//						} else if (purchase > 20000 && purchase <= 30000) {
				//							double tempvar1 = mrp1 / packSize1;
				//							double tempvar2 = tempvar1
				//									* ((taxValue1 + surchargeValue1) / 100);
				//							double mrpwithouttax = tempvar1 - tempvar2;
				//							double temp = 1.10* purchase;
				//							if (mrpwithouttax > temp) {
				//								sp = temp;
				//
				//							} else {
				//								double mrpless1prcnt = mrpwithouttax
				//										- (mrpwithouttax * 0.01);
				//								sp = mrpless1prcnt;
				//							}
				////							sp = purchase * 1.10;
				//						} else if (purchase > 30000) {
				//							double tempvar1 = mrp1 / packSize1;
				//							double tempvar2 = tempvar1
				//									* ((taxValue1 + surchargeValue1) / 100);
				//							double mrpwithouttax = tempvar1 - tempvar2;
				//							double temp =  1.05 * purchase;
				//							if (mrpwithouttax > temp) {
				//								sp = temp;
				//
				//							} else {
				//								double mrpless1prcnt = mrpwithouttax
				//										- (mrpwithouttax * 0.01);
				//								sp = mrpless1prcnt;
				//							}
				////							sp = purchase * 1.05;
				//						} else {
				//							double tempvar1 = mrp1 / packSize1;
				//							double tempvar2 = tempvar1
				//									* ((taxValue1 + surchargeValue1) / 100);
				//							double mrpwithouttax = tempvar1 - tempvar2;
				//							double temp = 2.5 * purchase;
				//							if (mrpwithouttax > temp) {
				//								sp = temp;
				//
				//							} else {
				//								double mrpless1prcnt = mrpwithouttax
				//										- (mrpwithouttax * 0.01);
				//								sp = mrpless1prcnt;
				//							}
				//						}
				//					} else {
				//						System.out.println(formulaApplied+"forumaula"+" "+purchase+"purchase");
				//						sp = purchase * Double.parseDouble(formulaApplied);
				//					}

				//	}
			}
			price = (double) Math.round(sp * 100) / 100;
			System.out.print("pricennng" + price);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

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
		try {
			while (resultSet.next()) {
				itemtypeSTR=resultSet.getObject(16).toString();
				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double
						.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13)
						.toString());
				quantity = Integer.parseInt(resultSet.getObject(8)
						.toString());
				purchase = Double
						.parseDouble(resultSet.getObject(7).toString());
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

	}
	// public void getItemDetail(String index) {
	//
	// GeneralDBConnection formula = new GeneralDBConnection();
	// double value = Double.parseDouble(formula.retrieveFormula1());
	// formula.closeConnection();
	// ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
	// ResultSet resultSet = itemsDBConnection.itemDetail2(index);
	// double mrp = 0;
	// double purchase = 0, tot = 0,tot1=0;
	// int packSize=1;
	// try {
	// while (resultSet.next()) {
	//
	// itemDescSTR = resultSet.getObject(3).toString();
	// purchase = Double
	// .parseDouble(resultSet.getObject(7).toString());
	// price = Double.parseDouble(resultSet.getObject(10).toString());
	// tot = (double) Math.round(purchase * value * 100) / 100;
	// tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
	// try {
	// packSize=Integer.parseInt( resultSet.getObject(4).toString().trim());
	// } catch (Exception e) {
	// // TODO: handle exception
	// }
	//
	// try {
	// mrp = Double
	// .parseDouble(resultSet.getObject(11).toString());
	// mrp=mrp/packSize;
	// double priceTemp=mrp*0.7;
	//
	// if(priceTemp>tot&&priceTemp<tot1)
	// {
	// tot=priceTemp;
	// }
	//
	// if(priceTemp>tot&&priceTemp>tot1)
	// {
	// tot=tot1;
	// }
	// if (tot > mrp && mrp > 0) {
	//
	// tot = mrp;
	//
	// }
	// } catch (Exception e) {
	//
	// }
	// }
	//
	// price =(double) Math.round(tot * 100) / 100;;
	//
	// } catch (SQLException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// itemsDBConnection.closeConnection();
	//
	// }
	//	public void getItemStock(String index) {
	//
	//		quantity = 0;
	//		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
	//		ResultSet resultSet = departmentStockDBConnection.retrieveStock(index,
	//				departmentName);
	//
	//		try {
	//			while (resultSet.next()) {
	//
	//				quantity = Integer
	//						.parseInt((resultSet.getObject(1).toString()));
	//				expiryDateSTR = (resultSet.getObject(2).toString());
	//			}
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		departmentStockDBConnection.closeConnection();
	//
	//	}




	public void getItemBatch(String index) {

		batchQty=0;
		expiryDateSTR="";
		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatchDetail(index);
		int i = 0;
		try {
			while (resultSet.next()) {

				batchQty = Integer.parseInt(resultSet.getObject(1).toString());
				expiryDateSTR = resultSet.getObject(2).toString();

				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		batchTrackingDBConnection.closeConnection();
		batchQtyET.setText("" + batchQty);
		expirtDateET.setText("" + expiryDateSTR);

	}
	public void getItemBatchMS(String index) {

		batchQty1=0;
		expiryDateSTR11="";
		ItemsMSDBConnection itemsDBConnection = new ItemsMSDBConnection();
		ResultSet resultSet = itemsDBConnection.itemBatchDetail(index);
		try {
			while (resultSet.next()) {

				batchQty1 = Integer.parseInt(resultSet.getObject(1).toString());
				expiryDateSTR11 = resultSet.getObject(2).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		batchQtyET_1.setText("" + batchQty1);
		expirtDateET1.setText("" + expiryDateSTR11);
	}


	public JButton getBtnRemove() {
		return btnRemove;
	}
}