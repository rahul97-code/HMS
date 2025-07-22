package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.PillsSlipDepartment;
import hms.patient.slippdf.ReturnPillsSlipDepartment;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

public class NewReturnForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox departmentCB;
	private JTextField departmentTF;
	private JTextField personTF;
	private JTextField invoiceNoTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyreturnTF;
	private JTextField afterreturnTF;
	private JComboBox returnReasonCB;
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> returnQtyV = new Vector<String>();
	Vector<String> returnReasonV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> consumableV = new Vector<String>();
	Vector<Boolean> flagV = new Vector<Boolean>();


	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
	expiryDateSTR = "", returnDateSTR = "", dueDateSTR = "",
	returnReasonSTR = "",previouseStock = "", itemBatchNameSTR = "";
	int qtyreturn = 0, afterreturn = 0, discountValue = 0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0;
	int quantity = 0,batchQty=0;
	String batchIDSTR = "0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JCheckBox chckbxConsumable;
	JComboBox batchNameCB;
	private JTextField expiry_textField;
	private JTextField batchQtyText;
	private JTextField searchBtchTF;
	private boolean flag;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewReturnForm dialog = new NewReturnForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewReturnForm() {
		setTitle("Return Items Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewReturnForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 595);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		departmentCB = new JComboBox();
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemIDV.size() > 0) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									NewReturnForm.this,
									"Are you sure to change department.then all added prevoise items cleard",
									"Clear Added Items", dialogButton);
					if (dialogResult == 0) {
						itemIDV.clear();
						itemNameV.clear();
						itemDescV.clear();
						batchID.clear();
						itemBatchIDV.clear();
						flagV.clear();
						itemBatchV.clear();
						returnQtyV.clear();
						returnReasonV.clear();
						consumableV.clear();
						expiryDateV.clear();
						loadDataToTable();
						searchItemTF.setText("");
						expiry_textField.setText("");
						batchQtyText.setText("");
					} else {
						departmentCB.setSelectedItem(departmentNameSTR);
						return;
					}
				}
				try {
					departmentNameSTR = departmentCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				getDepartmentsDetail(departmentNameSTR);
				departmentTF.setText("");
				searchItemTF.setText("");
				qtyInHandTF.setText("");
				if (departmentName.getSize() > 0) {

					departmentTF.setText("" + departmentID);
				}
			}
		});
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(150, 11, 218, 25);
		contentPanel.add(departmentCB);

		JLabel lblCredit = new JLabel("Select Department");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 11, 126, 25);
		contentPanel.add(lblCredit);

		departmentTF = new JTextField();
		departmentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentTF.setColumns(10);
		departmentTF.setBounds(150, 47, 218, 25);
		contentPanel.add(departmentTF);

		JLabel lblDebit = new JLabel("Department ID :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 47, 126, 25);
		contentPanel.add(lblDebit);

		personTF = new JTextField();
		personTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		personTF.setColumns(10);
		personTF.setBounds(150, 83, 218, 25);
		contentPanel.add(personTF);

		JLabel lblBalance = new JLabel("Person Name :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(14, 83, 126, 25);
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(758, 11, 218, 25);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Intend Slip No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(622, 11, 126, 25);
		contentPanel.add(lblInvoiceNo);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(622, 47, 126, 25);
		contentPanel.add(lblDate);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Dialog", Font.BOLD, 10));
		searchItemTF.setBounds(10, 190, 119, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);

				} else {

					itemDescTF.setText("");
					qtyreturnTF.setText("");
					expiry_textField.setText("");
					batchQtyText.setText("");
					afterreturnTF.setText("");
					afterreturnTF.setText("");
					itemBatchName.removeAllElements();
					itemName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					expiry_textField.setText("");
					batchQtyText.setText("");
					qtyreturnTF.setText("");
					afterreturnTF.setText("");
					afterreturnTF.setText("");
					itemBatchName.removeAllElements();
					itemName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					qtyreturnTF.setText("");
					expiry_textField.setText("");
					batchQtyText.setText("");
					afterreturnTF.setText("");
					afterreturnTF.setText("");
					itemBatchName.removeAllElements();

					itemName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(317, 190, 142, 20);
		contentPanel.add(itemDescTF);
		itemDescTF.setColumns(10);

		itemNameCB = new JComboBox();
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

				itemDescTF.setText("");
				qtyInHandTF.setText("");
				afterreturnTF.setText("");
				expiry_textField.setText("");
				batchQtyText.setText("");
				quantity=0;
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!flag) {
						getItemStock(itemIDSTR);
						qtyInHandTF.setText("" + quantity);
						itemBatchNameSTR="N/A";
						expiry_textField.setText("" + expiryDateSTR);
					}else
					{
						getItemStock(itemIDSTR);
						getItemBatchNameDpt(itemIDSTR);
						qtyInHandTF.setText(quantity+"");
					}
					
					itemDescTF.setText("" + itemDescSTR);

					afterreturn = quantity - qtyreturn;
					afterreturnTF.setText("" + afterreturn);
				}
			}
		});
		itemNameCB.setFont(new Font("Dialog", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 168, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(471, 190, 101, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyreturnTF = new JTextField();
		qtyreturnTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyreturnTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyreturnTF.setBounds(582, 190, 88, 20);
		contentPanel.add(qtyreturnTF);
		qtyreturnTF.setColumns(10);
		qtyreturnTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		qtyreturnTF.getDocument().addDocumentListener(new DocumentListener() {


			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyreturnTF.getText() + "";
				if (!str.equals("")) {

					qtyreturn = Integer.parseInt(str);

				} else {

					qtyreturn = 0;
					return;
				}
				afterreturn = quantity - qtyreturn;

				afterreturnTF.setText("" + afterreturn);

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyreturnTF.getText() + "";
				if (!str.equals("")) {

					qtyreturn = Integer.parseInt(str);

				} else {

					qtyreturn = 0;

				}
				afterreturn = quantity - qtyreturn;
				afterreturnTF.setText("" + afterreturn);

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyreturnTF.getText() + "";
				if (!str.equals("")) {

					qtyreturn = Integer.parseInt(str);

				} else {

					qtyreturn = 0;

				}
				afterreturn = quantity - qtyreturn;
				afterreturnTF.setText("" + afterreturn);
			}


		});

		afterreturnTF = new JTextField();
		afterreturnTF.setEditable(false);
		afterreturnTF.setHorizontalAlignment(SwingConstants.RIGHT);
		afterreturnTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		afterreturnTF.setBounds(680, 190, 118, 20);
		contentPanel.add(afterreturnTF);
		afterreturnTF.setColumns(10);

		returnReasonCB = new JComboBox();
		returnReasonCB.setModel(new DefaultComboBoxModel(new String[] {"Near to Expiry", "Other"}));
		returnReasonCB.setEditable(true);
		returnReasonCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		returnReasonCB.setBounds(593, 219, 126, 20);
		contentPanel.add(returnReasonCB);
		returnReasonCB.setEditable(true);
		returnReasonCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				returnReasonSTR = (String) returnReasonCB.getSelectedItem();
				System.out.print(""+returnReasonSTR);
			}
		});
		final JTextField tfListText = (JTextField) returnReasonCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					returnReasonSTR = text;

				}
			}
		});
		returnReasonCB.setSelectedIndex(0);

		JLabel lblNewLabel_2 = new JLabel("Return Reason :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(481, 222, 107, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(317, 165, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblQty = new JLabel("Dept Total Qty.");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(471, 165, 104, 14);
		contentPanel.add(lblQty);

		JLabel lblqtyreturn = new JLabel("Return Qty.");
		lblqtyreturn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyreturn.setBounds(582, 165, 88, 14);
		contentPanel.add(lblqtyreturn);

		JLabel lblTax = new JLabel("After Return Stock.");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(682, 165, 116, 14);
		contentPanel.add(lblTax);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (flag && itemBatchName.getSize() <=0){
					JOptionPane.showMessageDialog(null,
							"Department has not Item Batches!!You cannot Return!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (flag && batchNameCB.getSelectedItem().equals("select Batch") )
				{
					JOptionPane.showMessageDialog(null,
							"Please select Item Batch!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;

				}
				if (qtyreturnTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter return qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (flag && Integer.parseInt(batchQtyText.getText().toString()) < Integer.parseInt(qtyreturnTF.getText().toString())) {
					JOptionPane.showMessageDialog(null,
							"your requirements exceeded the Item Batch stock!",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (Integer.parseInt(afterreturnTF.getText().toString()) < 0) {
					JOptionPane.showMessageDialog(null,
							"your requirements exceeded the stock",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (itemIDV.indexOf(itemIDSTR) != -1 && flag && itemBatchIDV.indexOf(batchIDSTR)!=-1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemBatchIDV.add(batchIDSTR);
				itemBatchV.add(itemBatchNameSTR);
				itemDescV.add(itemDescSTR);
				flagV.add(flag);
				returnQtyV.add(qtyreturn + "");
				returnReasonV.add(returnReasonSTR);
				consumableV.add(chckbxConsumable.isSelected() ? "Yes" : "No");
				expiryDateV.add(expiryDateSTR);
				loadDataToTable();
				searchItemTF.setText("");
				expiry_textField.setText("");
				batchQtyText.setText("");
				itemBatchName.removeAllElements();
				itemName.removeAllElements();
				batchNameCB.setModel(itemBatchName);

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(909, 165, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expiry :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(731, 226, 54, 14);
		contentPanel.add(lblExpireDate);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

				itemIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				flagV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				returnQtyV.remove(cur_selectedRow);
				returnReasonV.remove(cur_selectedRow);
				consumableV.remove(cur_selectedRow);

				expiryDateV.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 210, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane exp_text = new JScrollPane();
		exp_text.setBounds(20, 247, 992, 253);
		contentPanel.add(exp_text);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Desc.", "Return Qty.",
				"Return Reason", "Cosumable","Item Batch", "Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						int selectedRowIndex = table.getSelectedRow();
						selectedRowIndex = table
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = table.getSelectedColumn();
						btnRemove.setEnabled(true);
					}
				});
		exp_text.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 140, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Return Items");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// if(departmentCB.getSelectedIndex()==-1)
				// {
				// JOptionPane.showMessageDialog(null,
				// "Please select supplier", "Input Error",
				// JOptionPane.ERROR_MESSAGE);
				// return;
				// }

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (returnDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (personTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter person name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (itemIDV.size() <= 0) {
					JOptionPane.showMessageDialog(null,
							"Please enter atleast one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				ReturnItemsDBConnection returnItemsDBConnection = new ReturnItemsDBConnection();
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;

				String[] data = new String[30];
				data[0] = departmentID;
				data[1] = departmentNameSTR;
				data[2] = personTF.getText().toString();
				data[3] = "N/A";
				data[4] = "" + invoiceNoTF.getText().toString();
				data[5] = returnDateSTR;
				data[6] = "" + timeFormat.format(cal1.getTime());
				for (int i = 0; i < itemIDV.size(); i++) {
					data[7] = itemIDV.get(i);
					data[8] = itemNameV.get(i);
					data[9] = itemDescV.get(i);
					data[10] = returnQtyV.get(i);
					data[11] = returnReasonV.get(i);
					data[12] = consumableV.get(i);
					data[13] = returnDateSTR;	
					if (flagV.get(i)) {
						String str = itemBatchV.get(i);
						String[] arrOfStr = str.split("\\(", 2);
						for (String a : arrOfStr) {
							data[14] = a;
							break;
						} 
					}else {
						data[14]="N/A";
					}
					data[15] = expiryDateV.get(i);
					data[16] = ""+StoreMain.userName; // user name
					try {
						returnItemsDBConnection.inserreturnData(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.addStockByReturn1(itemIDV.get(i),
								returnQtyV.get(i));
						if (flagV.get(i)) {
							batchTrackingDBConnection.subtractStock(itemBatchIDV.get(i), "-" + returnQtyV.get(i),
									DateFormatChange.StringToMysqlDate(new Date()), StoreMain.userName);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				returnItemsDBConnection.closeConnection();
				DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();

				for (int i = 0; i < itemIDV.size(); i++) {
					data[0] = departmentID;
					data[1] = departmentNameSTR;
					data[2] = "n/a";
					data[3] = personTF.getText().toString();
					data[4] = itemIDV.get(i);
					data[5] = itemNameV.get(i);
					data[6] = itemDescV.get(i);
					data[7] = returnQtyV.get(i);
					data[8] = returnDateSTR;
					data[9] = expiryDateV.get(i);


					try {
						if (flagV.get(i)) {
							departmentStockDBConnection.subtractStock(itemIDV.get(i), returnQtyV.get(i),
									departmentNameSTR, itemBatchIDV.get(i));
						}else {
							departmentStockDBConnection.subtractStock(
									itemIDV.get(i), returnQtyV.get(i),
									departmentNameSTR);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				batchTrackingDBConnection.closeConnection();
				departmentStockDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Item Returned Successfully", "Return Items",
						JOptionPane.INFORMATION_MESSAGE);
				try {
					new ReturnPillsSlipDepartment(departmentNameSTR, itemNameV,
							returnQtyV,index,itemBatchV,expiryDateV,personTF.getText());
				} catch (Exception exception) {
					// TODO: handle exception
				}
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(33, 511, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(196, 511, 153, 39);
		contentPanel.add(btnCancel);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.setBounds(758, 47, 218, 25);
		contentPanel.add(invoiceDate);

		chckbxConsumable = new JCheckBox("Consumable");
		chckbxConsumable.setSelected(true);
		chckbxConsumable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxConsumable.setBounds(804, 187, 97, 25);
		contentPanel.add(chckbxConsumable);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(NewReturnForm.class
				.getResource("/icons/aladdin3.gif")));
		lblNewLabel.setBounds(416, 0, 171, 133);
		contentPanel.add(lblNewLabel);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							returnDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		invoiceDate.setDate(new Date());
		getAllDepartments();
		batchNameCB = new JComboBox();
		batchNameCB.setModel(new DefaultComboBoxModel(new String[] {"select item"}));
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				qtyreturnTF.setText("");
				try {
					itemBatchNameSTR = batchNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (batchNameCB.getSelectedIndex() > -1) {
					batchIDSTR = batchID.get(batchNameCB.getSelectedIndex());

				}
				batchQty=0;
				expiryDateSTR="";
				if (itemBatchName.getSize() > 0 && batchNameCB.getSelectedItem()!=("select Batch")) {

					getItemStock(itemIDSTR,batchIDSTR);
					batchQtyText.setText("" + batchQty); 
					expiry_textField.setText("" + expiryDateSTR);
				}
				
			}
		});
		batchNameCB.setFont(new Font("Dialog", Font.BOLD, 11));
		batchNameCB.setBounds(120, 219, 168, 20);
		contentPanel.add(batchNameCB);

		expiry_textField = new JTextField();
		expiry_textField.setEditable(false);
		expiry_textField.setBounds(786, 222, 114, 19);
		contentPanel.add(expiry_textField);
		expiry_textField.setColumns(10);

		JLabel lblBatchQty = new JLabel("Batch Qty :");
		lblBatchQty.setFont(new Font("Dialog", Font.BOLD, 11));
		lblBatchQty.setBounds(306, 222, 86, 14);
		contentPanel.add(lblBatchQty);

		batchQtyText = new JTextField();
		batchQtyText.setEditable(false);
		batchQtyText.setColumns(10);
		batchQtyText.setBounds(385, 219, 78, 19);
		contentPanel.add(batchQtyText);

		searchBtchTF = new JTextField();
		searchBtchTF.setFont(new Font("Dialog", Font.BOLD, 10));
		searchBtchTF.setColumns(10);
		searchBtchTF.setBounds(20, 220, 88, 20);
		contentPanel.add(searchBtchTF);
		searchBtchTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchBtchTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchBtchTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchBtchTF.getText() + "";
				searchTableContents(str);
			}
		});

	}
	public void searchTableContents(String searchString) {	
		for (int i=0;i<batchID.size();i++)
		{
			if (batchID.get(i).toString().toLowerCase()
					.contains(searchString.toLowerCase())) {
				// content found so adding to table
				batchNameCB.setSelectedIndex(i);
				break;
			}
		}
	}


	


	public void getItemBatchNameDpt(String item_id) {

		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.getBatchName(item_id,departmentID);
		itemBatchName.removeAllElements();
		batchID.clear();
		batchID.add("0");
		itemBatchName.addElement("select Batch");
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(3).toString());
				itemBatchName.addElement(resultSet.getObject(1).toString()
						+ "(Batch" + (i + 1) + ")");
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

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdNew(index);
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
		flag=false;
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		int i = 0;
		try {
			while (resultSet.next()) {
				flag=Boolean.parseBoolean(resultSet.getObject(17).toString());
				if(flag)
				{
					batchNameCB.setEnabled(true);
				}else
				{
					batchNameCB.setEnabled(false);
				}
				itemDescSTR = resultSet.getObject(3).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemStock(String item_id,String batch_id) {
		double batchQty1=0; 
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt(item_id, departmentNameSTR, batch_id);
		batchQty=0;
		expiryDateSTR="";
		try {
			while (resultSet.next()) { 
				batchQty=resultSet.getInt(1);
				expiryDateSTR = resultSet.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}
	public void getItemStock(String item_id) {
		double batchQty1=0; 
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStockbt1(item_id,
					departmentNameSTR);
		quantity=0;
		expiryDateSTR="";
		try {
			while (resultSet.next()) { 
				quantity=resultSet.getInt(1);
				expiryDateSTR = resultSet.getString(2);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();
	     
	}
	
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		int total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = returnQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = returnReasonV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = consumableV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = expiryDateV.get(i);

		}
		table.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Desc.",
						"Return Qty.", "Previouse Stock", "Cosumable","Item Batch", "Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);


		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		finalTotalValue = total;

	}

	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentID = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}
}
