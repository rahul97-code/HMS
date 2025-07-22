package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.main.DateFormatChange;
import hms.store.database.ChallanDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.JTextArea;

public class EditInvoice extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField supplierTF;
	private JComboBox supplierCB;
	private JTextField mobileTF;
	private JTextField addressTF;
	private JTextField invoiceNoTF;
	private JTextField orderNoTF;
	public JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyTF;
	private JTextField unitPriceTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField totalValueTF;
	private JTable table;
	private JTextField finalTaxTF;
	private JTextField finalDiscountTF;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> measUnitV = new Vector<String>();
	Vector<String> qtyV = new Vector<String>();
	Vector<String> freeqtyV = new Vector<String>();
	Vector<String> paidqtyV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> taxV = new Vector<String>();
	Vector<String> taxValueV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> igst = new Vector<String>();
	Vector<String> igstValueV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();
	Vector<Boolean> addStock = new Vector<Boolean>();

	Vector<String> returnGoodBatch = new Vector<String>();
	Vector<String> challanBatch = new Vector<String>();
	Vector<String> returnitemID = new Vector<String>();
	Vector<String> challanItemID = new Vector<String>();


	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
	supplierNameSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,itemSurchargeSTR,itemIgstSTR,
	expiryDateSTR = "", invoiceDateSTR = "", dueDateSTR = "0000-00-00";
	double unitPrice = 0, oldunitPrice = 0, taxValue = 0,itemSurcharge = 0,itemIgst = 0, discountValue = 0,
			itemValue, finalTaxValue = 0, finalDiscountValue = 0,finalReturnAmount=0,
			finalTotalValue = 0,surchargeAmountValue=0,igstAmountValue=0,taxAmountValue=0,finalTotalValueCoin=0;
	int quantity = 0,freeQuantity=0,paidQuantity=0;
	String mrp = "";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JComboBox measUnitCD;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField batchNumberTF;
	private JTextField freeQtyTF;
	private JTextField itemSurchargeTF;
	private JLabel taxableAmountLB;
	private JLabel surchargeLB;
	private JLabel taxAmountLB;
	private JLabel coinADJLB;
	private JTextField igstTF;
	String invoiceNumber="1";
	private JDateChooser invoiceDate;
	private JDateChooser dueDate;
	private JTextField enteryUserET;
	private JTextField returnAmntTF;
	private JTextField paybleAmtTF;
	private JTextArea returnReasonTA;
	private JTextField textField;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditInvoice dialog = new EditInvoice("3");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public EditInvoice(final String invoiceNumber) {
		this.invoiceNumber=invoiceNumber;
		setTitle("Invoice Detail");
		setResizable(false);
		setBounds(100, 70, 1031, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Search Supplier :");
		lblNewLabel_1.setBounds(20, 11, 126, 25);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblNewLabel_1);

		supplierTF = new JTextField();
		supplierTF.setBounds(156, 11, 218, 25);
		supplierTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(supplierTF);
		supplierTF.setColumns(10);
		supplierTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {

					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					supplierCB.setModel(supplierName);
				}
			}
		});

		supplierCB = new JComboBox();
		supplierCB.setBounds(156, 44, 218, 25);
		supplierCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					supplierDisplaySTR = supplierCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");

				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText("" + addressSTR);
					mobileTF.setText("" + mobileSTR);
				}
			}
		});
		supplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(supplierCB);

		JLabel lblCredit = new JLabel("Select Supplier");
		lblCredit.setBounds(20, 44, 126, 25);
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblCredit);

		mobileTF = new JTextField();
		mobileTF.setBounds(156, 80, 218, 25);
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		contentPanel.add(mobileTF);

		JLabel lblDebit = new JLabel("Mobile :");
		lblDebit.setBounds(20, 80, 126, 25);
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblDebit);

		addressTF = new JTextField();
		addressTF.setBounds(156, 116, 218, 25);
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		contentPanel.add(addressTF);

		JLabel lblBalance = new JLabel("Address :");
		lblBalance.setBounds(20, 116, 126, 25);
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setBounds(535, 11, 165, 25);
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Invoice No. :");
		lblInvoiceNo.setBounds(399, 11, 126, 25);
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblInvoiceNo);

		orderNoTF = new JTextField();
		orderNoTF.setBounds(535, 44, 165, 25);
		orderNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		orderNoTF.setColumns(10);
		contentPanel.add(orderNoTF);

		JLabel lblDueDate = new JLabel("Due Date :");
		lblDueDate.setBounds(399, 116, 126, 25);
		lblDueDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblDueDate);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setBounds(399, 80, 126, 25);
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblDate);

		JLabel lblBillorderNo = new JLabel("Bill/Order No. :");
		lblBillorderNo.setBounds(399, 44, 126, 25);
		lblBillorderNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblBillorderNo);

		searchItemTF = new JTextField();
		searchItemTF.setBounds(10, 190, 119, 20);
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
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
					unitPriceTF.setText("");
					taxTF.setText("");
					igstTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					unitPriceTF.setText("");
					igstTF.setText("");
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					unitPriceTF.setText("");
					igstTF.setText("");
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setBounds(10, 221, 163, 20);
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(itemDescTF);
		itemDescTF.setColumns(10);

		itemNameCB = new JComboBox();
		itemNameCB.setBounds(139, 190, 210, 20);
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				System.out.println(itemNameCB.getSelectedIndex() + "    "
						+ itemID.size());
				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}

				itemDescTF.setText("");
				igstTF.setText("");
				unitPriceTF.setText("");
				taxTF.setText("");
				discountTF.setText("");
				itemSurchargeTF.setText("");
				freeQtyTF.setText("");
				//	returnAmntTF.setText("");

				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!taxTypeSTR.equals("CompanyTax")) {
						taxTF.setText(taxValueSTR);
					}
					itemDescTF.setText("" + itemDescSTR);
					unitPriceTF.setText("" + unitPrice);
					itemSurchargeTF.setText(""+itemSurchargeSTR);
					igstTF.setText(""+itemIgstSTR);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(itemNameCB);

		measUnitCD = new JComboBox();
		measUnitCD.setBounds(110, 249, 136, 20);
		measUnitCD.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(measUnitCD);

		qtyTF = new JTextField();
		qtyTF.setBounds(359, 190, 61, 20);
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(qtyTF);
		qtyTF.setColumns(10);
		qtyTF.addKeyListener(new KeyAdapter() {
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
		qtyTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {

					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {

					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyTF.getText() + "";
				if (!str.equals("")) {
					quantity = Integer.parseInt(str);
					itemValue();

				} else {

					quantity = 0;
					itemValue();
				}

			}
		});

		unitPriceTF = new JTextField();
		unitPriceTF.setBounds(511, 190, 65, 20);
		unitPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(unitPriceTF);
		unitPriceTF.setColumns(10);
		unitPriceTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		unitPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					itemValue();

				} else {

					unitPrice = 0;
					itemValue();
				}
			}
		});

		taxTF = new JTextField();
		taxTF.setBounds(586, 190, 61, 20);
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(taxTF);
		taxTF.setColumns(10);
		taxTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		taxTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = taxTF.getText() + "";
				if (!str.equals("")) {

					taxValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					taxValue = 0;
					itemValue();
				}
			}
		});

		discountTF = new JTextField();
		discountTF.setBounds(819, 190, 61, 20);
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(discountTF);
		discountTF.setColumns(10);
		discountTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		discountTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = discountTF.getText() + "";
				if (!str.equals("")) {

					discountValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountValue = 0;
					itemValue();
				}
			}
		});

		totalValueTF = new JTextField();
		totalValueTF.setBounds(525, 221, 113, 20);
		totalValueTF.setEditable(false);
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(totalValueTF);
		totalValueTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setBounds(434, 224, 86, 14);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setBounds(10, 165, 119, 14);
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Select Item :");
		lblDescription.setBounds(139, 165, 153, 14);
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblDescription);

		JLabel lblMeasUnits = new JLabel("Meas Units");
		lblMeasUnits.setBounds(20, 252, 80, 14);
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblMeasUnits);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setBounds(362, 165, 68, 14);
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblQty);

		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setBounds(511, 165, 65, 14);
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblUnitPrice);

		JLabel lblTax = new JLabel("CGST");
		lblTax.setBounds(588, 165, 67, 14);
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblTax);

		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setBounds(820, 165, 61, 14);
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblDiscount);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.setBounds(891, 156, 114, 25);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (qtyTF.getText().toString().equals("") || qtyTF.getText().toString().equals("0")) {
					JOptionPane.showMessageDialog(null,
							"Please enter quantity", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (unitPriceTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select item or enter unit price",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} else if (totalValueTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter final value", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				else if (batchNumberTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Batch Number", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} 
				//				else if (itemIDV.indexOf(itemIDSTR) != -1) {
				//					JOptionPane.showMessageDialog(null,
				//							"this item already entered", "Input Error",
				//							JOptionPane.ERROR_MESSAGE);
				//					return;
				//				}
				if (oldunitPrice != unitPrice) {
					updatePrice();
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							EditInvoice.this,
							"Do you want to change the mrp price." + "\n"
									+ " Current MRP is " + mrp + " Rupees",
									"Cancel", dialogButton);
					if (dialogResult == 0) {
						EditItem editItem = new EditItem(itemIDSTR);
						editItem.setModal(true);
						editItem.setVisible(true);
					}
				}
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				measUnitV.add(measUnitCD.getSelectedItem().toString());
				batchNumberV.add(batchNumberTF.getText().toString().toUpperCase());
				qtyV.add("" + quantity);
				freeqtyV.add("" + freeQuantity);
				paidqtyV.add("" + paidQuantity);
				unitPriceV.add(unitPrice + "");
				taxV.add(taxValue + "");
				surchargeV.add(itemSurcharge+"");
				igst.add(itemIgst+"");
				taxValueV.add(taxAmountValue+"");
				surchargeValueV.add(surchargeAmountValue+"");
				igstValueV.add(igstAmountValue+"");
				discountV.add(discountValue + "");
				totalValueV.add(totalValueTF.getText() + "");
				expiryDateV.add(expiryDateSTR);
				addStock.add(true);
				searchItemTF.setText("");
				searchItemTF.requestFocusInWindow();
				expiryDateC.setCalendar(null);
				expiryDateSTR="";
				loadDataToTable();

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setBounds(645, 224, 86, 14);
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblExpireDate);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.setBounds(890, 190, 114, 25);
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
				measUnitV.remove(cur_selectedRow);
				qtyV.remove(cur_selectedRow);
				freeqtyV.remove(cur_selectedRow);
				paidqtyV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				taxV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				taxValueV.remove(cur_selectedRow);
				surchargeValueV.remove(cur_selectedRow);
				igst.remove(cur_selectedRow);
				igstValueV.remove(cur_selectedRow);
				discountV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				batchNumberV.remove(cur_selectedRow);
				addStock.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 280, 992, 223);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Double Click to edit item");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {
		}, new String[] {  "Item ID", "Item Name", "Item Batch.", 
				"Qty.","Paid Qty.", "Unit Price","CGST","SGST", "Discount", "Amount(Wouthout Vat)", "Value",
		"Expiry"  }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setPreferredWidth(65);
		table.getColumnModel().getColumn(6).setMinWidth(65);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(65);
		table.getColumnModel().getColumn(8).setMinWidth(65);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(75);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(90);
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
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getClickCount() == 2) {
					int cur_selectedRow;
					cur_selectedRow = table.getSelectedRow();
					cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
					String toDelete = table.getModel()
							.getValueAt(cur_selectedRow, 0).toString();

					searchItemTF.setText(itemNameV.get(cur_selectedRow));
					batchNumberTF.setText(batchNumberV.get(cur_selectedRow));
					qtyTF.setText((qtyV.get(cur_selectedRow)));
					taxTF.setText(taxV.get(cur_selectedRow));
					discountTF.setText(discountV.get(cur_selectedRow));
					itemSurchargeTF.setText(surchargeV.get(cur_selectedRow));
					expiryDateC.setDate(new Date(DateFormatChange.StringToDateFormat(expiryDateV.get(cur_selectedRow))));
					freeQtyTF.setText(freeqtyV.get(cur_selectedRow));


					itemIDV.remove(cur_selectedRow);
					itemNameV.remove(cur_selectedRow);
					itemDescV.remove(cur_selectedRow);
					measUnitV.remove(cur_selectedRow);
					qtyV.remove(cur_selectedRow);
					freeqtyV.remove(cur_selectedRow);
					paidqtyV.remove(cur_selectedRow);
					unitPriceV.remove(cur_selectedRow);
					taxV.remove(cur_selectedRow);
					surchargeV.remove(cur_selectedRow);
					taxValueV.remove(cur_selectedRow);
					surchargeValueV.remove(cur_selectedRow);
					discountV.remove(cur_selectedRow);
					totalValueV.remove(cur_selectedRow);
					expiryDateV.remove(cur_selectedRow);
					batchNumberV.remove(cur_selectedRow);
					addStock.remove(cur_selectedRow);


					loadDataToTable();
					btnRemove.setEnabled(false);
					qtyTF.requestFocusInWindow();
				}
			}
		});
		JSeparator separator = new JSeparator();
		separator.setBounds(20, 151, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Update Invoice");
		btnNewButton_1.setBounds(672, 551, 153, 39);
		btnNewButton_1.setEnabled(false);
		if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1"))
		{
			btnNewButton_1.setEnabled(true);
		}else{
			btnNewButton_1.setEnabled(false);
		}
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (returnReasonTA.getText().toString().equals("")) {

					JOptionPane.showMessageDialog(null,
							"Please Type Reason", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (supplierCB.getSelectedIndex() == -1) {
					JOptionPane.showMessageDialog(null,
							"Please select supplier", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (orderNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter order no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (invoiceDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemIDV.size() == 0) {
					JOptionPane.showMessageDialog(null,
							"Please add atlest one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data = new String[30];
				data[0] = invoiceNoTF.getText().toString();
				data[1] = orderNoTF.getText().toString();
				data[2] = supplierID;
				data[3] = supplierDisplaySTR;
				data[4] = invoiceDateSTR;
				data[5] = "" + timeFormat.format(cal1.getTime());
				data[6] = dueDateSTR;
				data[7] = "Yes";
				data[8] = "" + StoreMain.userName; // user
				data[9] = finalTotalValue + "";
				data[10] = finalTaxValue + "";
				data[11] = finalDiscountTF.getText() + "";
				data[12] = paybleAmtTF.getText().toString();
				data[13] = returnReasonTA.getText().toString();
				data[14] = returnAmntTF.getText()+"";
				data[15] = DateFormatChange.StringToMysqlDate(new Date()) +" "+timeFormat.format(cal1.getTime());
				InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
				try {
					index = invoiceDBConnection.updateInvoiceEntry(data,EditInvoice.this.invoiceNumber);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				ChallanDBConnection challanDBConnection=new ChallanDBConnection();
				ReturnInvoiceDBConnection returnInvoiceDBConnection=new ReturnInvoiceDBConnection();
				data[3] = invoiceNumber + "";
				data[16] = "" + DateFormatChange.StringToMysqlDate(new Date());
				data[17] = "" + timeFormat.format(cal1.getTime());
				data[18] = ""+StoreMain.userName; // /user
				for (int i = 0; i < itemIDV.size(); i++) {
					data[0] = itemIDV.get(i);
					data[1] = itemNameV.get(i);
					data[2] = itemDescV.get(i);
					data[4] = measUnitV.get(i);
					data[5] = qtyV.get(i);
					data[6] = freeqtyV.get(i);
					data[7] = paidqtyV.get(i);
					data[8] = unitPriceV.get(i);
					data[9] = discountV.get(i);
					data[10] = taxV.get(i);
					data[11] = surchargeV.get(i);
					data[12] = taxValueV.get(i);
					data[13] = surchargeValueV.get(i);
					data[14] = totalValueV.get(i);
					data[15] = expiryDateV.get(i);
					data[19] = batchNumberV.get(i);

					try {
						invoiceDBConnection.updateInvoiceItem(data,invoiceNumber);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					if(challanBatch.contains(batchNumberV.get(i)))
					{
						try {
							challanDBConnection.updateChallanStatus1(challanItemID.get(challanBatch.indexOf(batchNumberV.get(i))));
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}

					try {
						if(addStock.get(i))
						{
							itemsDBConnection.addStock1(itemIDV.get(i), qtyV.get(i),
									expiryDateV.get(i), unitPriceV.get(i));
						}

						itemsDBConnection.updateOrderStaus(itemIDV.get(i), "NO",qtyV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				challanDBConnection.closeConnection();
				returnInvoiceDBConnection.closeConnection();
				invoiceDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Sucessfully Updated", "Message",
						JOptionPane.INFORMATION_MESSAGE);

				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(835, 551, 153, 39);
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		contentPanel.add(btnCancel);

		JLabel lblTax_1 = new JLabel("Tax % :");
		lblTax_1.setBounds(32, 540, 80, 14);
		lblTax_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblTax_1);

		JLabel lblDiscount_1 = new JLabel("Discount :");
		lblDiscount_1.setBounds(156, 540, 80, 14);
		lblDiscount_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblDiscount_1);

		finalTaxTF = new JTextField();
		finalTaxTF.setBounds(30, 566, 100, 25);
		finalTaxTF.setEditable(false);
		finalTaxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTaxTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(finalTaxTF);
		finalTaxTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		finalTaxTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					finalTotal();

				} else {
					finalTaxValue = 0;
					finalTotal();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					finalTotal();

				} else {

					finalTaxValue = 0;
					finalTotal();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = finalTaxTF.getText() + "";
				if (!str.equals("")) {

					finalTaxValue = Double.parseDouble("0" + str);
					finalTotal();

				} else {

					finalTaxValue = 0;
					finalTotal();
				}
			}
		});
		finalTaxTF.setColumns(10);

		finalDiscountTF = new JTextField();
		finalDiscountTF.setBounds(156, 565, 100, 25);
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		finalDiscountTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalDiscountValue = 0;
							finalTotal();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalDiscountValue = 0;
							finalTotal();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = finalDiscountTF.getText() + "";
						if (!str.equals("")) {

							finalDiscountValue = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalDiscountValue = 0;
							finalTotal();
						}
					}
				});
		contentPanel.add(finalDiscountTF);

		expiryDateC = new JDateChooser();
		expiryDateC.setBounds(728, 221, 152, 20);
		expiryDateC.getCalendarButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		expiryDateC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub

						if ("date".equals(arg0.getPropertyName())&&arg0
								.getNewValue()!=null) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		contentPanel.add(expiryDateC);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setBounds(419, 535, 50, 24);
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblTotal);

		finalTotalTF = new JTextField();
		finalTotalTF.setBounds(408, 566, 107, 25);
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		contentPanel.add(finalTotalTF);

		invoiceDate = new JDateChooser();
		invoiceDate.setBounds(535, 80, 165, 25);
		contentPanel.add(invoiceDate);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							invoiceDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		invoiceDate.setDate(new Date());
		dueDate = new JDateChooser();
		dueDate.setBounds(535, 116, 165, 25);
		contentPanel.add(dueDate);

		batchNumberTF = new JTextField();
		batchNumberTF.setBounds(260, 221, 164, 20);
		batchNumberTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchNumberTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNumberTF.setColumns(10);
		contentPanel.add(batchNumberTF);

		JLabel lblBatchNo = new JLabel("Batch No. :");
		lblBatchNo.setBounds(183, 224, 80, 14);
		lblBatchNo.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblBatchNo);

		freeQtyTF = new JTextField();
		freeQtyTF.setBounds(430, 190, 71, 20);
		freeQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		freeQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		freeQtyTF.setColumns(10);
		contentPanel.add(freeQtyTF);
		freeQtyTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		freeQtyTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {

					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {

					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {
					freeQuantity = Integer.parseInt(str);
					itemValue();

				} else {

					freeQuantity = 0;
					itemValue();
				}

			}
		});

		JLabel lblFreeQty = new JLabel("Free Qty.");
		lblFreeQty.setBounds(433, 165, 68, 14);
		lblFreeQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblFreeQty);

		itemSurchargeTF = new JTextField();
		itemSurchargeTF.setBounds(665, 190, 61, 20);
		itemSurchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemSurchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemSurchargeTF.setColumns(10);
		contentPanel.add(itemSurchargeTF);
		itemSurchargeTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		itemSurchargeTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = itemSurchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurcharge = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurcharge = 0;
					itemValue();
				}
			}
		});


		JLabel lblSurChg = new JLabel("SGST");
		lblSurChg.setBounds(667, 165, 67, 14);
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblSurChg);


		JLabel lblTaxebleAmount = new JLabel("Taxable Amount :");
		lblTaxebleAmount.setBounds(22, 514, 124, 14);
		contentPanel.add(lblTaxebleAmount);

		taxableAmountLB = new JLabel("taxableAmountLB");
		taxableAmountLB.setBounds(135, 514, 100, 14);
		contentPanel.add(taxableAmountLB);

		taxAmountLB = new JLabel("taxableAmountLB");
		taxAmountLB.setBounds(349, 514, 100, 14);
		contentPanel.add(taxAmountLB);

		JLabel lblSaleTax = new JLabel("CGST :");
		lblSaleTax.setBounds(282, 514, 68, 14);
		contentPanel.add(lblSaleTax);

		surchargeLB = new JLabel("surchargeLB");
		surchargeLB.setBounds(566, 514, 100, 14);
		contentPanel.add(surchargeLB);

		JLabel lblSurchargeAmount = new JLabel("SGST  :");
		lblSurchargeAmount.setBounds(479, 514, 77, 14);
		contentPanel.add(lblSurchargeAmount);

		coinADJLB = new JLabel("coints");
		coinADJLB.setBounds(865, 514, 100, 14);
		contentPanel.add(coinADJLB);

		JLabel lblCoinAdj = new JLabel("Coin ADJ.  :");
		lblCoinAdj.setBounds(778, 514, 77, 14);
		contentPanel.add(lblCoinAdj);

		igstTF = new JTextField();
		igstTF.setBounds(740, 190, 61, 20);
		igstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		igstTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		igstTF.setColumns(10);
		contentPanel.add(igstTF);

		igstTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		igstTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = igstTF.getText() + "";
				if (!str.equals("")) {

					itemIgst = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemIgst = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = igstTF.getText() + "";
				if (!str.equals("")) {

					itemIgst = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemIgst = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = igstTF.getText() + "";
				if (!str.equals("")) {

					itemIgst = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemIgst = 0;
					itemValue();
				}
			}
		});



		JLabel lblIgst = new JLabel("IGST");
		lblIgst.setBounds(742, 165, 67, 14);
		lblIgst.setFont(new Font("Tahoma", Font.BOLD, 11));
		contentPanel.add(lblIgst);

		JLabel lblIentryUser = new JLabel("Entry User :");
		lblIentryUser.setBounds(728, 11, 102, 25);
		lblIentryUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPanel.add(lblIentryUser);

		enteryUserET = new JTextField();
		enteryUserET.setBounds(835, 11, 170, 25);
		enteryUserET.setEditable(false);
		enteryUserET.setFont(new Font("Tahoma", Font.PLAIN, 14));
		enteryUserET.setColumns(10);
		contentPanel.add(enteryUserET);

		returnAmntTF = new JTextField();
		returnAmntTF.setHorizontalAlignment(SwingConstants.RIGHT);
		returnAmntTF.setFont(new Font("Dialog", Font.PLAIN, 14));
		returnAmntTF.setColumns(10);
		returnAmntTF.setBounds(292, 565, 100, 25);
		returnAmntTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		returnAmntTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = returnAmntTF.getText() + "";
						if (!str.equals("")) {

							finalReturnAmount = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalReturnAmount = 0;
							finalTotal();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = returnAmntTF.getText() + "";
						if (!str.equals("")) {

							finalReturnAmount = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalReturnAmount = 0;
							finalTotal();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = returnAmntTF.getText() + "";
						if (!str.equals("")) {

							finalReturnAmount = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalReturnAmount = 0;
							finalTotal();
						}
					}
				});
		contentPanel.add(returnAmntTF);

		JLabel returnLabl = new JLabel("Return Amnt :");
		returnLabl.setFont(new Font("Dialog", Font.PLAIN, 14));
		returnLabl.setBounds(292, 540, 100, 14);
		contentPanel.add(returnLabl);

		JLabel lblPaybal = new JLabel("Paybal Amnt:");
		lblPaybal.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblPaybal.setBounds(546, 535, 92, 24);
		contentPanel.add(lblPaybal);

		paybleAmtTF = new JTextField();
		paybleAmtTF.setText("0");
		paybleAmtTF.setHorizontalAlignment(SwingConstants.RIGHT);
		paybleAmtTF.setFont(new Font("Dialog", Font.BOLD, 14));
		paybleAmtTF.setEditable(false);
		paybleAmtTF.setColumns(10);
		paybleAmtTF.setBounds(531, 565, 107, 25);
		contentPanel.add(paybleAmtTF);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(831, 80, 178, 61);
		contentPanel.add(scrollPane_1);
		
				returnReasonTA = new JTextArea();
				scrollPane_1.setViewportView(returnReasonTA);
				returnReasonTA.setRows(5);

		JLabel lblReason = new JLabel("Reason :");
		lblReason.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblReason.setBounds(750, 80, 80, 25);
		contentPanel.add(lblReason);
		
		JLabel lblSelectDoctor = new JLabel("Select Doctor :");
		lblSelectDoctor.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblSelectDoctor.setBounds(712, 44, 113, 25);
		contentPanel.add(lblSelectDoctor);
		
		textField = new JTextField();
		textField.setFont(new Font("Dialog", Font.PLAIN, 14));
		textField.setEditable(false);
		textField.setColumns(10);
		textField.setBounds(835, 47, 170, 25);
		contentPanel.add(textField);
		dueDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dueDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		invoiceDetail();
	}

	public void getSupplierName(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		supplierName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				supplierName.addElement(resultSet.getObject(3).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		supplierCB.setModel(supplierName);
		if (i > 0) {
			supplierCB.setSelectedIndex(0);
		}

	}

	public void getSupplierDetail(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		try {
			while (resultSet.next()) {

				supplierID = resultSet.getObject(1).toString();
				supplierNameSTR = resultSet.getObject(2).toString();
				mobileSTR = (resultSet.getObject(4).toString());
				addressSTR = (resultSet.getObject(6).toString() + ", "
						+ resultSet.getObject(7).toString() + ", " + resultSet
						.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
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

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		measUnit.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				measUnit.addElement(resultSet.getObject(4).toString());
				taxTypeSTR = resultSet.getObject(5).toString();
				taxValueSTR = resultSet.getObject(6).toString();
				System.out
				.println(itemDescSTR + "  "
						+ resultSet.getObject(7).toString() + " "
						+ taxValueSTR);
				unitPrice = Double.parseDouble("0"
						+ resultSet.getObject(7).toString());
				oldunitPrice = unitPrice;
				mrp = resultSet.getObject(11).toString();
				itemSurchargeSTR= resultSet.getObject(12).toString();
				itemIgstSTR= resultSet.getObject(13).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		measUnitCD.setModel(measUnit);
		if (i > 0) {
			measUnitCD.setSelectedIndex(0);
		}
	}

	public void itemValue() {
		paidQuantity=quantity-freeQuantity;

		itemValue = paidQuantity * unitPrice;
		itemValue = itemValue - discountValue;
		double k = itemValue * (taxValue / 100.0f);
		double surcharge = itemValue * (itemSurcharge / 100.0f);
		surcharge=Math.round(surcharge * 100.00) / 100.00;
		k=Math.round(k * 100.00) / 100.00;
		taxAmountValue=k;
		surchargeAmountValue=surcharge;

		double igst = itemValue * (itemIgst / 100.0f);
		igst=Math.round(igst * 100.00) / 100.00;
		igstAmountValue=igst;
		itemValue = itemValue + k;
		itemValue = itemValue + surcharge;
		itemValue = itemValue + igst;
		itemValue = Math.round(itemValue * 100.00) / 100.00;
		totalValueTF.setText("" + itemValue);

	}

	public void finalTotal() {
		double total = 0;
		total = finalTotalValue - finalDiscountValue;
		total=total-finalReturnAmount;
		double k = total * (finalTaxValue / 100.0f);

		total = total + k;

		total = Math.round(total * 100.000) / 100.000;


		if (total - Math.floor(total) > 0.5) {
			paybleAmtTF.setText("" + Math.ceil(total));
			finalTotalValueCoin=Math.ceil(total)-total;
		} else {
			paybleAmtTF.setText("" + Math.floor(total));
			finalTotalValueCoin=Math.floor(total)-total;
		}
		finalTotalValueCoin = Math.round(finalTotalValueCoin * 100.000) / 100.000;
		System.out.print(total+"  "+finalTotalValueCoin);
		coinADJLB.setText("" + (finalTotalValueCoin));

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		double taxable=0,tax=0,surcharge=0;
		ObjectArray_ListOfexamsSpecs = new Object[size][12];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			//	ObjectArray_ListOfexamsSpecs[i][3] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = taxV.get(i)+"("+taxValueV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][7] = surchargeV.get(i)+"("+surchargeValueV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][8] = discountV.get(i);
			double amountWT= Double.parseDouble("0"+paidqtyV.get(i))* Double.parseDouble("0"+unitPriceV.get(i));
			amountWT = Math.round(amountWT * 100.00) / 100.00;
			ObjectArray_ListOfexamsSpecs[i][9] = amountWT;
			ObjectArray_ListOfexamsSpecs[i][10] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][11] = expiryDateV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
			taxable=taxable+amountWT;
			tax=tax+Double.parseDouble("0"+taxValueV.get(i));
			surcharge=surcharge+Double.parseDouble("0"+surchargeValueV.get(i));
		}
		DefaultTableModel model = new DefaultTableModel(ObjectArray_ListOfexamsSpecs,new String[] { "Item ID", "Item Name", "Item Batch.", 
				"Qty.","Paid Qty.", "Unit Price","CGST","SGST", "Discount", "Amount(W/T)", "Value",
		"Expiry" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}
		};
		table.setModel(model);
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setPreferredWidth(65);
		table.getColumnModel().getColumn(6).setMinWidth(65);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(65);
		table.getColumnModel().getColumn(8).setMinWidth(65);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(75);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(90);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		finalTotalValue = total;
		double Sample=0;
		tax = Math.round(tax * 100.00) / 100.00;
		surcharge = Math.round(surcharge * 100.00) / 100.00;
		Sample= Math.round(finalTotalValue);
		if(Math.floor(Sample) > 0.5)
			finalTotalTF.setText("" + Math.ceil(Sample));
		else
			finalTotalTF.setText("" + Math.floor(Sample));
		paybleAmtTF.setText("" + finalTotalValue);
		taxable = Math.round(taxable * 100.00) / 100.00;
		finalTotal();
		taxableAmountLB.setText(""+taxable);
		taxAmountLB.setText(""+tax);
		surchargeLB.setText(""+surcharge);
	}

	public void updatePrice() {
		ItemsDBConnection db = new ItemsDBConnection();
		try {
			db.updateprice(itemIDSTR, unitPrice + "", oldunitPrice + "");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void getItems()
	{
		InvoiceDBConnection challanDBConnection = new InvoiceDBConnection();
		ResultSet resultSet = challanDBConnection.retrieveItems(invoiceNumber);

		try {
			while (resultSet.next()) {
				itemIDV.add(resultSet.getObject(1).toString());
				itemNameV.add(resultSet.getObject(2).toString());
				itemDescV.add(resultSet.getObject(3).toString());
				measUnitV.add(resultSet.getObject(4).toString());
				qtyV.add(resultSet.getObject(5).toString());
				freeqtyV.add(resultSet.getObject(6).toString());
				paidqtyV.add(resultSet.getObject(7).toString());
				unitPriceV.add(resultSet.getObject(8).toString());
				discountV.add(resultSet.getObject(9).toString());
				taxV.add(resultSet.getObject(10).toString());
				surchargeV.add(resultSet.getObject(11).toString());
				taxValueV.add(resultSet.getObject(12).toString()+"");
				surchargeValueV.add(resultSet.getObject(13).toString());
				totalValueV.add(resultSet.getObject(14).toString());
				expiryDateV.add(resultSet.getObject(15).toString());
				batchNumberV.add(resultSet.getObject(17).toString());
				challanBatch.add(resultSet.getObject(17).toString());
				challanItemID.add(resultSet.getObject(18).toString());
				addStock.add(false);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		challanDBConnection.closeConnection();
		loadDataToTable();
	}	
	public void invoiceDetail()
	{
		InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
		ResultSet resultSet = invoiceDBConnection.invoiceWithID(invoiceNumber);

		try {
			while (resultSet.next()) {
				invoiceNoTF.setText(resultSet.getObject(2).toString());
				orderNoTF.setText(resultSet.getObject(3).toString());
				supplierTF.setText(resultSet.getObject(5).toString());
				textField.setText(resultSet.getString(16));
				
				try {
					invoiceDate.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getObject(6).toString()));
					//	dueDate.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(resultSet.getObject(8).toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					//e.printStackTrace();
				}

				totalValueTF.setText(resultSet.getObject(9).toString());
				finalTaxTF.setText(resultSet.getObject(10).toString());
				finalDiscountTF.setText(resultSet.getObject(11).toString());
				finalTotalTF.setText(resultSet.getObject(12).toString());
				invoiceNoTF.setText(resultSet.getObject(2).toString());
				enteryUserET.setText(resultSet.getObject(13).toString());
				returnAmntTF.setText(resultSet.getObject(14).toString());
				returnReasonTA.setText(resultSet.getObject(15).toString());
				//paybleAmtTF.setText(resultSet.getObject(12).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		invoiceDBConnection.closeConnection();
		getItems();
		//loadDataToTable();
	}	
	public JDateChooser getInvoiceDate() {
		return invoiceDate;
	}
	public JDateChooser getDueDate() {
		return dueDate;
	}
}
