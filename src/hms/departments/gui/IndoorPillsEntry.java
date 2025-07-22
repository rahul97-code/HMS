package hms.departments.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.store.database.BatchTrackingDBConnection;
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

public class IndoorPillsEntry extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JComboBox<String> batch_nameCB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private Timer timer;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();

	private HashMap examHashMap = new HashMap();
	String[] data = new String[21];
	private JTextField insuranceTypeTB;
	JLabel lastOPDDateLB;
	double totalChargesIPD = 0;
	int packSize = 1;
	boolean flag = false;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	Vector<String> itemTypeV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemPriceV = new Vector<String>();
	Vector<String> totalPriceV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> cmprname = new Vector<String>();
	Vector<String> doctorIDV = new Vector<String>();
	Vector<String> itemMRPV = new Vector<String>();
	Vector<Boolean> flagV = new Vector<Boolean>();
	Vector<String> itemMeasV = new Vector<String>();
	Vector<String> itemBTCH = new Vector<String>();
	Vector<String> itemEXP = new Vector<String>();
	Vector<String> batch_id = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	double ipd_advance = 0, ipd_balance = 0;
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0", p_ageM = "0", p_ageD = "0",
			p_birthdate = "1111-11-11", p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F", p_p_father_husband = "", p_insurancetype = "Unknown",
			p_note = "";
	String doctorName = "", doctorID = "", date = "", btch_ID = "";
	String ipd_doctor_id = "", ipd_doctorname = "", item_stock = "", ipd_date = "", ipd_time = "", ipd_note = "",
			ipd_id = "", batch_name = "", ward_name = "", building_name = "", bed_no = "Select Bed No",
			ward_incharge = "", ward_room = "", ward_code = "", descriptionSTR1 = "", charges = "", batchIDSTR = "0",
			ipd_days, ipd_houres, ipd_minutes, ipd_expenses_id;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
			dueDateSTR = "", previouseStock = "", itemtypeSTR = "", departmentName = "", Cash_Reqd,
			departmentID = "";
	int finalTaxValue = 0, finalDiscountValue = 0, f = 0;
	double price = 0, finalTotalValue = 0, qtyIssued = 0, afterIssued = 0, quantity = 0, taxValue = 0,
			surchargeValue = 0, itemValue = 0, mrp = 0;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel btchName = new DefaultComboBoxModel();
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
	private JTextField itemType;
	private JButton btnAdd;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new IndoorPillsEntry().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	public IndoorPillsEntry() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(IndoorPillsEntry.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Pills Entry");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(50, 70, 1014, 634);
		setModal(true);
		departmentName = DepartmentMain.departmentName;
		departmentID = DepartmentMain.departmentID;
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
		panel_4.setBounds(6, 5, 995, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,

				TitledBorder.TOP, null, null));
		panel_3.setBounds(10, 131, 954, 112);
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
		label_1.setBounds(302, 15, 108, 14);
		panel_3.add(label_1);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(402, 10, 182, 25);
		panel_3.add(ipdBuildingTB);

		JLabel label_2 = new JLabel("Ward Name :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_2.setBounds(302, 51, 90, 14);
		panel_3.add(label_2);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(402, 46, 182, 25);
		panel_3.add(ipdWardTB);

		JLabel label_3 = new JLabel("Bed No :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_3.setBounds(602, 15, 90, 14);
		panel_3.add(label_3);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(702, 10, 182, 25);
		panel_3.add(ipdBedNoTB);

		JLabel label_4 = new JLabel("No. of Days :");
		label_4.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label_4.setBounds(602, 46, 90, 14);
		panel_3.add(label_4);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(702, 41, 182, 25);
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

		JLabel label_6 = new JLabel("Total Amount :");
		label_6.setBounds(10, 81, 108, 14);
		panel_3.add(label_6);
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(110, 76, 182, 25);
		panel_3.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		JLabel label_7 = new JLabel("Advance Payment :");
		label_7.setBounds(302, 81, 108, 14);
		panel_3.add(label_7);
		label_7.setFont(new Font("Tahoma", Font.PLAIN, 12));

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(436, 76, 148, 25);
		panel_3.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 254, 509, 325);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		btnAdd = new JButton("Add");
		btnAdd.setIcon(new ImageIcon(IndoorPillsEntry.class.getResource("/icons/plus_button.png")));
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemDescET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (enterQtyET.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(!CheckInsuranceItem(itemIDSTR)) {
					JOptionPane.showMessageDialog(null,
							"You cannot add this item for "+insuranceTypeTB.getText()+" patients", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (cmprname.indexOf(itemNameSTR) != -1 && flag && batch_nameCB.getItemCount() < 2) {
					// insucheck();
					int a = JOptionPane.showConfirmDialog(null,
							"" + itemNameSTR + " is Already Entered !" + "\n" + "If you want to Add again,Click YES");
					if (a == JOptionPane.YES_OPTION) {
						// name();
						insucheck();
					} else {
						itemSearchET.setText("");
						Cash_Reqd = "0";
					}
					return;
				}

				if (flag && btchName.getSize() <= 1) {
					JOptionPane.showMessageDialog(null, "Department has not Item Batches!!You cannot Issue!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (!itemNameSTR.toLowerCase().contains("charge")
						&& Double.parseDouble(remainingItemET.getText().toString()) < 0 && check() == 1) {
					JOptionPane.showMessageDialog(null, "Please Check Quantity Issued!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (flag && (btchName.getSelectedItem().equals("select Batch"))) {
					JOptionPane.showMessageDialog(null, "Please Select Batch!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
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
				if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
					boolean bool = false;
					DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
					ResultSet resultSet = departmentStockDBConnection.getalloweditem(itemIDSTR,
							insuranceTypeTB.getText().toString());

					try {
						while (resultSet.next()) {
							bool = resultSet.getBoolean(1);
							System.out.print("wfffffffffffffffffffffff" + bool);
						}
						if (bool) {
							int a = JOptionPane.showConfirmDialog(null, "This Item is Not Incuded in Insurance Items!"
									+ "\n" + "Click 'YES' To ADD Forcefully.");
							if (a == JOptionPane.YES_OPTION) {
								itemIDV.add(itemIDSTR);

								itemNameV.add(itemNameSTR);
								itemBTCH.add(batch_name);
								flagV.add(flag);
								itemEXP.add(expiryDateSTR);
								batch_id.add(batchIDSTR);
								itemDescV.add(itemDescSTR);
								issuedQtyV.add(qtyIssued + "");
								itemPriceV.add(price + "");
								totalPriceV.add(itemValue + "");
								itemMRPV.add(mrp + "");
								itemMeasV.add(packSize + "");
								itemTypeV.add(itemtypeSTR + "");
								// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
								// / 100 + "");
								loadDataToTable();
								itemSearchET.setText("");
								Cash_Reqd = "1";
							} else {
								itemSearchET.setText("");
								Cash_Reqd = "0";
							}
						} else {
							itemIDV.add(itemIDSTR);

							itemNameV.add(itemNameSTR);
							itemBTCH.add(batch_name);
							flagV.add(flag);
							itemEXP.add(expiryDateSTR);
							batch_id.add(batchIDSTR);
							itemDescV.add(itemDescSTR);
							issuedQtyV.add(qtyIssued + "");
							if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
								itemPriceV.add(mrp + "");
							} else
								itemPriceV.add(price + "");
							totalPriceV.add(itemValue + "");
							itemMRPV.add(mrp + "");
							itemMeasV.add(packSize + "");
							itemTypeV.add(itemtypeSTR + "");
							// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
							// / 100 + "");
							loadDataToTable();
							itemSearchET.setText("");
							Cash_Reqd = "0";

						}

					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					itemIDV.add(itemIDSTR);

					itemNameV.add(itemNameSTR);
					itemBTCH.add(batch_name);
					itemEXP.add(expiryDateSTR);
					flagV.add(flag);
					batch_id.add(batchIDSTR);
					itemDescV.add(itemDescSTR);
					issuedQtyV.add(qtyIssued + "");
					if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
						itemPriceV.add(mrp + "");
					} else
						itemPriceV.add(price + "");
					totalPriceV.add(itemValue + "");
					itemMRPV.add(mrp + "");
					itemMeasV.add(packSize + "");
					itemTypeV.add(itemtypeSTR + "");
					// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
					// / 100 + "");
					loadDataToTable();
					itemSearchET.setText("");
					Cash_Reqd = "0";
				}
			}

			private void name() {
				// TODO Auto-generated method stub
				itemIDV.add(itemIDSTR);

				itemNameV.add(itemNameSTR);
				itemBTCH.add(batch_name);
				itemEXP.add(expiryDateSTR);
				batch_id.add(batchIDSTR);
				itemDescV.add(itemDescSTR);
				issuedQtyV.add(qtyIssued + "");
				if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
					itemPriceV.add(mrp + "");
				} else
					itemPriceV.add(price + "");
				totalPriceV.add(itemValue + "");
				itemMRPV.add(mrp + "");
				itemMeasV.add(packSize + "");
				itemTypeV.add(itemtypeSTR + "");
				// totalPriceV.add((double) Math.round(qtyIssued * price * 100)
				// / 100 + "");
				loadDataToTable();
				itemSearchET.setText("");
				Cash_Reqd = "0";
			}
		});
		btnAdd.setBounds(239, 256, 125, 33);
		panel_2.add(btnAdd);

		btnRemove = new JButton("Remove");
		btnRemove.setIcon(new ImageIcon(IndoorPillsEntry.class.getResource("/icons/exit.png")));
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
				itemBTCH.remove(cur_selectedRow);
				batch_id.remove(cur_selectedRow);
				itemEXP.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(374, 256, 125, 33);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(129, 524, 106, 23);
		panel_2.add(lblTotalCharges);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(227, 527, 144, 20);
		panel_2.add(lblTotalcharges);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(10, 11, 494, 238);
		panel_2.add(panel_5);
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Item Detail :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12),
				null));

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

				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!flag) {
						getItemStock(itemIDSTR);
						expirtDateET.setText("" + expiryDateSTR);
						qtyInHandET.setText("" + quantity + "");
						batch_name = "N/A";
						batchIDSTR = "0";
					} else {
						getItemBatchName(itemIDSTR);

					}
					itemDescET.setText("" + itemDescSTR);
					// expirtDateET.setText("" + expiryDateSTR);
					// qtyInHandET.setText("" + quantity);
					if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
						priceET.setText("" + mrp);
					} else {
						priceET.setText("" + price);
					}
					if (itemtypeSTR.equals("General")) {
						itemType.setText(itemtypeSTR);
					} else {
						itemType.setText(itemtypeSTR);
					}
					if (itemtypeSTR.equals("High Risk")) {
						itemType.setBackground(Color.RED);
						itemType.setForeground(Color.BLACK);
					} else if (itemtypeSTR.equals("SHC-H1")) {
						itemType.setBackground(Color.GREEN);
						itemType.setForeground(Color.BLACK);
					} else {
						itemType.setBackground(Color.WHITE);
					}
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
				itemValue();
				// costET.setText((qtyIssued * price) + "");
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
				itemValue();
				// costET.setText((qtyIssued * price) + "");
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
					btchName.removeAllElements();
					batch_nameCB.setModel(btchName);
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
					btchName.removeAllElements();
					batch_nameCB.setModel(btchName);
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
					btchName.removeAllElements();
					batch_nameCB.setModel(btchName);
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
		lblCost.setBounds(21, 199, 69, 25);
		panel_5.add(lblCost);

		costET = new JTextField();
		costET.setEditable(false);
		costET.setColumns(10);
		costET.setBounds(109, 205, 128, 25);
		panel_5.add(costET);

		JLabel lblItemType = new JLabel("Item Type");
		lblItemType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblItemType.setBounds(258, 205, 100, 25);
		panel_5.add(lblItemType);

		itemType = new JTextField();
		itemType.setEditable(false);
		itemType.setColumns(10);
		itemType.setBounds(356, 206, 128, 25);
		panel_5.add(itemType);

		batch_nameCB = new JComboBox<String>();
		batch_nameCB.setModel(new DefaultComboBoxModel(new String[] { "select Batch" }));
		batch_nameCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					batch_name = batch_nameCB.getSelectedItem().toString();

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.getStackTrace();
				}
				if (batch_nameCB.getSelectedIndex() > -1) {
					batchIDSTR = batchID.get(batch_nameCB.getSelectedIndex());

				}
				quantity = 0;
				expirtDateET.setText("");
				qtyInHandET.setText("");
				enterQtyET.setText("");
				remainingItemET.setText("");
				if (btchName.getSize() > 1 && batch_nameCB.getSelectedItem() != ("select Batch")) {

					getItemStock(itemIDSTR, batchIDSTR);
					expirtDateET.setText("" + expiryDateSTR);
					qtyInHandET.setText("" + quantity);
					getItemBatch(batchIDSTR, itemIDSTR);
					if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
						priceET.setText("" + mrp);
					} else {
						priceET.setText("" + price);
					}
				} else {
					expirtDateET.setText("");
					qtyInHandET.setText("");
					enterQtyET.setText("");
					remainingItemET.setText("");
				}
			}
		});

		batch_nameCB.setBounds(277, 90, 207, 24);
		panel_5.add(batch_nameCB);

		JPanel panel = new JPanel();
		panel.setBounds(10, 0, 954, 133);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(317, 36, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(417, 31, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(317, 72, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(628, 36, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(628, 72, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 101, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(317, 101, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(417, 67, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(728, 31, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(728, 67, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 97, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(417, 101, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(499, 101, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(628, 108, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(728, 100, 201, 25);
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
				ipdBuildingTB.setText("");
				ipdNoTB.setText("");
				ipdWardTB.setText("");
				ipdBedNoTB.setText("");
				bedDaysTB.setText("");
				ipdDoctorTB.setText("");
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

		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.setIcon(new ImageIcon(IndoorPillsEntry.class.getResource("/icons/SAVE.GIF")));
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select the patient", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (ipdNoTB.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "This patient is not a indoor patient. ", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (doctorName.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null, "Please select doctor", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (itemID.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add some items", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
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
							dept_PillsRegisterDBConnection.insertDepartmentPillsRegister(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (flagV.get(i)) {
							try {
								departmentStockDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i),
										departmentName, batch_id.get(i));
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else if (!itemNameV.get(i).toLowerCase().contains("charge")) {
							try {
								departmentStockDBConnection.subtractStock(itemIDV.get(i), issuedQtyV.get(i),
										departmentName);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					departmentStockDBConnection.closeConnection();
					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[22];
					for (int i = 0; i < itemIDV.size(); i++) {
						data1[0] = "" + ipd_id;
						data1[1] = "" + itemNameV.get(i);

						data1[2] = itemDescV.get(i);
						data1[3] = totalPriceV.get(i);

						data1[4] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data1[5] = "" + timeFormat.format(cal1.getTime());
						data1[6] = itemIDV.get(i);
						data1[7] = "" + p_id;
						data1[8] = "" + p_name;
						data1[9] = itemPriceV.get(i);
						data1[10] = issuedQtyV.get(i);
						data1[11] = db.retrieveItemSubCategory(itemIDV.get(i));
						data1[12] = DepartmentMain.userName;
						data1[13] = db.retrieveItemCategory(itemIDV.get(i));
						data1[14] = "N/A";
						data1[15] = itemMRPV.get(i);
						data1[16] = itemMeasV.get(i);
						data1[17] = itemTypeV.get(i);
						data1[18] = flagV.get(i) ? batch_id.get(i) : "0";
						if (flagV.get(i)) {
							String str = itemBTCH.get(i);
							String[] arrOfStr = str.split("\\(", 2);
							for (String a : arrOfStr) {
								data1[19] = a;
								break;
							}
						} else {
							data1[19] = "N/A";
						}
						data1[20] = "D";
						data1[21] = Cash_Reqd;
						try {
							db.inserDataNewExpenses(data1);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}

					}
					db.closeConnection();
					double tot = totalChargesIPD + finalTotalValue;
					double finalValue = Math.round(tot * 100.0) / 100.0;
					IPDDBConnection ipddbConnection = new IPDDBConnection();
					try {
						ipddbConnection.updateTotalAmount(ipd_id, finalValue + "");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						ipddbConnection.closeConnection();
						e.printStackTrace();
					}
					ipddbConnection.closeConnection();
					dept_PillsRegisterDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null, "Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);

					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(551, 535, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(708, 535, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IndoorPillsEntry.class.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(529, 266, 456, 98);
		panel_4.add(scrollPane_1);

		table = new JTable();
		scrollPane_1.setViewportView(table);

		JLabel label_8 = new JLabel("");
		label_8.setHorizontalAlignment(SwingConstants.CENTER);
		label_8.setIcon(new ImageIcon(IndoorPillsEntry.class.getResource("/icons/graphics-hospitals-575145.gif")));
		label_8.setBounds(1021, 365, 103, 105);
		panel_4.add(label_8);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(529, 380, 456, 144);
		panel_4.add(scrollPane);

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
		getIPDDATA();
		// miscdbConnection = new MiscDBConnection();
		// String lastOPDDate =
		// miscdbConnection.retrieveLastExamPatient(indexId);
		// lastOPDDateLB.setText("Last Exam : " + lastOPDDate);
		// miscdbConnection.closeConnection();
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
			ObjectArray_ListOfexamsSpecs[i][5] = itemEXP.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = itemBTCH.get(i) == "select Batch" ? "" : itemBTCH.get(i);
			total = total + Double.parseDouble(totalPriceV.get(i));
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Desc.", "Issued Qty.", "Cost", "Expiry", "Batch" }));
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

	// date 11 may 2023
	// public void itemValue() {
	// if(insuranceTypeTB.getText().equals("ECHS") ||
	// insuranceTypeTB.getText().equals("Railway"))
	// {
	// itemValue=qtyIssued * mrp;
	// }
	// else {
	// itemValue = qtyIssued * price;
	// // System.out.println("unit" + itemValue);
	// double k = itemValue * (taxValue / 100.0f);
	// k = Math.round(k * 100.000) / 100.000;
	// double s = itemValue * (surchargeValue / 100.0f);
	// s = Math.round(s * 100.000) / 100.000;
	//
	// itemValue = itemValue + k;
	// itemValue = itemValue + s;
	//
	// itemValue = Math.round(itemValue * 100.000) / 100.000;
	// }
	// costET.setText(itemValue + "");
	//
	// }
	public void itemValue() {
		if (InsuranceMrpReqd(insuranceTypeTB.getText())) {
			itemValue = qtyIssued * mrp;
		} else {

			itemValue = qtyIssued * price;
			// System.out.println("unit" + itemValue);

			itemValue = Math.round(itemValue * 100.000) / 100.000;
		}
		costET.setText(itemValue + "");

	}

	public void getItemDetail(String index) {

		GeneralDBConnection formula = new GeneralDBConnection();
		double value = Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		flag = false;
		double purchase = 0, tot = 0, tot1 = 0, sp = 0;

		String formulaActive = "";
		try {
			while (resultSet.next()) {
				itemtypeSTR = resultSet.getObject(18).toString();
				flag = Boolean.parseBoolean(resultSet.getObject(19).toString());
				if (flag) {
					batch_nameCB.setEnabled(true);
				} else {
					batch_nameCB.setEnabled(false);
				}
				itemDescSTR = resultSet.getObject(3).toString();
				taxValue = Double.parseDouble(resultSet.getObject(6).toString());
				surchargeValue = Double.parseDouble(resultSet.getObject(13).toString());
				purchase = Double.parseDouble(resultSet.getObject(20).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				tot = (double) Math.round(purchase * value * 100) / 100;
				tot1 = (double) Math.round(purchase * 2.5 * 100) / 100;
				formulaActive = resultSet.getObject(15).toString();
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
					if (purchase >= 10000 && purchase <= 20000) {

						double tempvar1 = mrp / packSize;
						double tempvar2 = tempvar1 * ((taxValue + surchargeValue) / 100);
						double mrpwithouttax = tempvar1 - tempvar2;
						double temp = 1.15 * purchase;
						if (mrpwithouttax > temp) {
							sp = temp;

						} else {
							double mrpless1prcnt = mrpwithouttax - (mrpwithouttax * 0.01);
							sp = mrpless1prcnt;
						}
						// sp = purchase * 1.15;
					} else if (purchase > 20000 && purchase <= 30000) {
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
				}
			}

		    sp=sp<purchase?purchase:sp;// to find the wrong price calculation 30 dec 2023
		    
			price = (double) Math.round(sp * 100) / 100;
			System.out.println(price + "fhgfhg");
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
	

	public void getItemStock(String index, String batch_id) {

		quantity = 0;
		expiryDateSTR = "";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt(index, departmentName, batch_id);
		try {
			while (resultSet.next()) {
				Object obj = resultSet.getObject(1);
				Object obj1 = resultSet.getObject(2);
				quantity = Double.parseDouble((obj == null) ? "0" : resultSet.getObject(1).toString());
				expiryDateSTR = (obj1 == null) ? "" : resultSet.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}

	public void getItemStock(String index) {

		quantity = 0;
		expiryDateSTR = "";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt1(index, departmentName);
		try {
			while (resultSet.next()) {
				Object obj = resultSet.getObject(1);
				Object obj1 = resultSet.getObject(2);
				quantity = Double.parseDouble((obj == null) ? "0" : resultSet.getObject(1).toString());
				expiryDateSTR = (obj1 == null) ? "" : resultSet.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

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
				ipd_advance = Double.parseDouble(resultSet.getObject(6).toString());
				advancePaymentTB.setText(ipd_advance + "");
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				int[] bedHours = DateFormatChange.calulateHoursAndDays_BetweenDates(
						DateFormatChange.javaDate(ipd_date + " " + ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1] + " Hours, " + bedHours[2] + " Min");
				getIPDDoctors(ipd_id);
				populateExpensesTable(ipd_id);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void getItemBatchName(String item_id) {

		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.getBatchName(item_id, departmentID);
		btchName.removeAllElements();
		batchID.clear();
		batchID.add("0");
		btchName.addElement("select Batch");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(3).toString());
				btchName.addElement(resultSet.getObject(1).toString() + "(Batch" + (i + 1) + ")");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
		batch_nameCB.setModel(btchName);
		if (i > 0) {
			batch_nameCB.setSelectedIndex(0);
		}
	}

	private boolean InsuranceMrpReqd(String Ins) {
		// TODO Auto-generated method stub
		InsuranceDBConnection InsuranceDBConnection = new InsuranceDBConnection();
		ResultSet rs = InsuranceDBConnection.InsMrpReqdOrNot(Ins);
		boolean r = false;
		try {
			while (rs.next()) {
				r = rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		InsuranceDBConnection.closeConnection();
		return r;
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
			rs = db.retrieveAllData(ipd_id);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
				}
				cmprname.add(Rows_Object_Array[R][0].toString());
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
				totalChargesIPD = totalChargesIPD + Double.parseDouble(Rows_Object_Array[i][2].toString());
			}
			totalChargesIPD = Math.round(totalChargesIPD * 100.0) / 100.0;
			totalAmountTB.setText(totalChargesIPD + "");
			table.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(0).setMinWidth(150);
			table.getColumnModel().getColumn(1).setPreferredWidth(30);
			table.getColumnModel().getColumn(1).setMinWidth(30);
			table.getColumnModel().getColumn(2).setResizable(false);
			table.getColumnModel().getColumn(2).setPreferredWidth(60);
			table.getColumnModel().getColumn(2).setMinWidth(60);
			ipd_balance = totalChargesIPD - ipd_advance;

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}

	}
	public boolean CheckInsuranceItem(String itemID) {
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.GetInsuranceItem(itemID);
		try {
			while(resultSet.next()) {                              
				int o=resultSet.getInt(1);
				if(o==0) {
					return true;
				}else if(o==1){
					if(p_insurancetype.equals("Unknown") || p_insurancetype.equals("Ayushman Bharat")) {
						return true;
					}else {
						return false;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		return false;
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

	public JButton getBtnRemove() {
		return btnRemove;
	}
}
