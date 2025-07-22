package hms.store.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.Bill_PDF;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ChallanDBConnection;

import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;
import hms.store.database.VendorQuotationDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
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
import javax.swing.JFrame;
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
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import java.awt.event.MouseAdapter;

public class NewInvoice extends JDialog {

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
	private JTextField discountprcnt;
	private JTextField totalValueTF;
	private JTable table;
	private JTextField finalTaxTF;
	private JTextField finalDiscountTF;
	private JTextField mrpPriceTF;
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
	Vector<String> mrpPriceV = new Vector<String>();
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
	Vector<String> PurchasePriceVV= new Vector<String>();
	Vector<String> returnGoodBatch = new Vector<String>();
	Vector<String> challanBatch = new Vector<String>();
	Vector<String> itemPrPrice = new Vector<String>();
	Vector<String> returnitemID = new Vector<String>();
	Vector<String> challanItemID = new Vector<String>();

	String measUnitS="";
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
	supplierNameSTR;
	boolean flag =false;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,itemSurchargeSTR,itemIgstSTR,
	expiryDateSTR = "", invoiceDateSTR = "", dueDateSTR = "0000-00-00",item_pr_price="";
	double oldMrp=0,unitPrice = 0, oldunitPrice = 0, taxValue = 0,itemSurcharge = 0,itemIgst = 0, discountValue = 0,
			itemValue, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0,surchargeAmountValue=0,igstAmountValue=0,taxAmountValue=0,finalTotalValueCoin=0,mrpPrice=0,finalReturnAmount=0;
	int quantity = 0,freeQuantity=0,paidQuantity=0,oldfreeQuantity=0;
	double mrp =0,discountPrcntValue=0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField batchNumberTF;
	private JTextField freeQtyTF;
	private JTextField itemSurchargeTF;
	private JLabel taxableAmountLB;
	private JLabel surchargeLB;
	private JLabel taxAmountLB;
	private JLabel coinADJLB;
	private JLabel lblPurchasePrice;
	private JLabel lblLastPurchaseMrp;
	private JLabel lblLastPurchaseQty;

	private JTextField igstTF;
	public static String po_id="";
	private JTextField measUnitET;
	private JTextField returnAmount;
	private JTextField poQtyTF;
	private JTextField discountTF;
	private JTextField finalamountTF;
	private JTextField old_pr_price;
	private JTextField old_mrp;
	private JTextField old_freeQty;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewInvoice dialog = new NewInvoice("","","");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public NewInvoice(final String ID, String PONumber, String VendorName) {
		NewInvoice.po_id=ID;
		setResizable(false);
		setTitle("New Invoice");
		setBounds(100, 70, 1031, 640);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Search Supplier :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(20, 11, 126, 25);
		contentPanel.add(lblNewLabel_1);

		supplierTF = new JTextField();
		supplierTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierTF.setBounds(156, 11, 218, 25);
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
		supplierCB.setBounds(156, 44, 218, 25);
		contentPanel.add(supplierCB);

		JLabel lblCredit = new JLabel("Select Supplier");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(20, 44, 126, 25);
		contentPanel.add(lblCredit);

		mobileTF = new JTextField();
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(156, 80, 218, 25);
		contentPanel.add(mobileTF);

		JLabel lblDebit = new JLabel("Mobile :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(20, 80, 126, 25);
		contentPanel.add(lblDebit);

		addressTF = new JTextField();
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(156, 116, 218, 25);
		contentPanel.add(addressTF);

		JLabel lblBalance = new JLabel("Address :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(20, 116, 126, 25);
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(758, 11, 218, 25);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Invoice No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(622, 11, 126, 25);
		contentPanel.add(lblInvoiceNo);

		orderNoTF = new JTextField();
		orderNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		orderNoTF.setColumns(10);
		orderNoTF.setBounds(758, 44, 218, 25);
		contentPanel.add(orderNoTF);

		JLabel lblDueDate = new JLabel("Due Date :");
		lblDueDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDueDate.setBounds(541, 116, 84, 25);
		contentPanel.add(lblDueDate);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(622, 80, 126, 25);
		contentPanel.add(lblDate);

		JLabel lblBillorderNo = new JLabel("Bill/Order No. :");
		lblBillorderNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBillorderNo.setBounds(622, 44, 126, 25);
		contentPanel.add(lblBillorderNo);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
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
					unitPriceTF.setText("");
					taxTF.setText("");
					igstTF.setText("");		
					measUnitET.setText("");
					itemName.removeAllElements();
					discountTF.setText("");
					itemNameCB.setModel(itemName);
					old_pr_price.setText("");
					old_mrp.setText("");
					old_freeQty.setText("");
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
					mrpPriceTF.setText("");
					igstTF.setText("");
					taxTF.setText("");
					
					measUnitET.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					discountTF.setText("");
					old_pr_price.setText("");
					old_mrp.setText("");
					old_freeQty.setText("");
					//					measUnitCD.setModel(measUnit);
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
					mrpPriceTF.setText("");
					igstTF.setText("");
					taxTF.setText("");
					measUnitET.setText("");
				
					itemName.removeAllElements();
					discountTF.setText("");
					itemNameCB.setModel(itemName);
					old_pr_price.setText("");
					old_mrp.setText("");
					old_freeQty.setText("");
					//					measUnit.removeAllElements();
					//					measUnitCD.setModel(measUnit);
					qtyTF.setText("");
					batchNumberTF.setText("");
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(10, 221, 163, 20);
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
				System.out.println(itemNameCB.getSelectedIndex() + "    "
						+ itemID.size());
				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}

				itemDescTF.setText("");
				igstTF.setText("");
				unitPriceTF.setText("");
				mrpPriceTF.setText("");
				taxTF.setText("");
			
				itemSurchargeTF.setText("");
				freeQtyTF.setText("");

				getItemDetail(itemIDSTR);
				getItemFreeQty(itemIDSTR);
				 
				if (itemName.getSize() > 0) {
					if (!taxTypeSTR.equals("CompanyTax")) {
						taxTF.setText(taxValueSTR);
					}
					itemDescTF.setText("" + itemDescSTR);
					unitPriceTF.setText("" + unitPrice);
					mrpPriceTF.setText(""+mrp);
					itemSurchargeTF.setText(""+itemSurchargeSTR);
					igstTF.setText(""+itemIgstSTR);
					measUnitET.setText(measUnitS+"");
					old_pr_price.setText(""+oldunitPrice);
					old_mrp.setText(""+oldMrp);
					old_freeQty.setText(""+oldfreeQuantity);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 210, 20);
		contentPanel.add(itemNameCB);

		qtyTF = new JTextField();
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyTF.setBounds(359, 190, 61, 20);
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
		unitPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		unitPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		unitPriceTF.setBounds(511, 190, 65, 20);
		contentPanel.add(unitPriceTF);
		unitPriceTF.setColumns(10);

		mrpPriceTF = new JTextField();
		mrpPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpPriceTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		//		mrpPriceTF.setBounds(511, 190, 65, 20);
		mrpPriceTF.setBounds(290, 252, 84, 20);
		contentPanel.add(mrpPriceTF);
		mrpPriceTF.setColumns(10);

		unitPriceTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();
				}
			}
		});
		mrpPriceTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();
				}
			}
		});
		unitPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					unitprice_check(unitPrice);
					itemValue();

				} else {

					unitPrice = 0;
					unitprice_check(unitPrice);
					itemValue();
				}

			}

			private void unitprice_check(double unitPrice) {
				// TODO Auto-generated method stub
				if(unitPrice>oldunitPrice)
				{
					old_pr_price.setText(oldunitPrice+ "");
					old_pr_price.setForeground(Color.RED);
					lblPurchasePrice.setForeground(Color.RED);
				}else
				{
					old_pr_price.setText(oldunitPrice+ "");
					old_pr_price.setForeground(Color.BLACK);
					lblPurchasePrice.setForeground(Color.BLACK);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					unitprice_check(unitPrice);
					itemValue();

				} else {

					unitPrice = 0;
					unitprice_check(unitPrice);
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = unitPriceTF.getText() + "";
				if (!str.equals("")) {

					unitPrice = Double.parseDouble(str);
					unitprice_check(unitPrice);
					itemValue();

				} else {

					unitPrice = 0;
					unitprice_check(unitPrice);
					itemValue();
				}
			}


		});
		mrpPriceTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = mrpPriceTF.getText() + "";
				if (!str.equals("")) {

					mrpPrice = Double.parseDouble(str);
					mrpPrice_check(mrpPrice);
					//					itemValue();

				} else {

					mrpPrice = 0;
					mrpPrice_check(mrpPrice);
					//					itemValue();
				}

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = mrpPriceTF.getText() + "";
				if (!str.equals("")) {

					mrpPrice =  Double.parseDouble(str);
					mrpPrice_check(mrpPrice);
					//					itemValue();

				} else {

					mrpPrice = 0;
					mrpPrice_check(mrpPrice);
					//					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = mrpPriceTF.getText() + "";
				if (!str.equals("")) {

					mrpPrice =  Double.parseDouble(str);
					mrpPrice_check(mrpPrice);
					//					itemValue();

				} else {

					mrpPrice = 0;
					mrpPrice_check(mrpPrice);
					//					itemValue();
				}
			}
			private void mrpPrice_check(double mrpPrice) {
				// TODO Auto-generated method stub
				if(mrpPrice>oldMrp)
				{
					old_mrp.setText(oldMrp+ "");
					old_mrp.setForeground(Color.RED);
					lblLastPurchaseMrp.setForeground(Color.RED);
				}else
				{
					old_mrp.setText(oldMrp+ "");
					old_mrp.setForeground(Color.BLACK);
					lblLastPurchaseMrp.setForeground(Color.BLACK);
				}
			}
		});
		taxTF = new JTextField();
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setBounds(586, 190, 61, 20);
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

		discountprcnt = new JTextField();
		discountprcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		discountprcnt.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountprcnt.setBounds(819, 190, 61, 20);
		contentPanel.add(discountprcnt);
		discountprcnt.setColumns(10);
		discountprcnt.addKeyListener(new KeyAdapter() {
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

		discountprcnt.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = discountprcnt.getText() + "";
				if (!str.equals("")) {

					discountPrcntValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountPrcntValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = discountprcnt.getText() + "";
				if (!str.equals("")) {

					discountPrcntValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountPrcntValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = discountprcnt.getText() + "";
				if (!str.equals("")) {

					discountPrcntValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					discountPrcntValue = 0;
					itemValue();
				}
			}
		});

		totalValueTF = new JTextField();
		totalValueTF.setEditable(false);
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalValueTF.setBounds(525, 221, 113, 20);
		contentPanel.add(totalValueTF);
		totalValueTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(434, 224, 86, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Select Item :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(139, 165, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblMeasUnits = new JLabel("Meas Units :");
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeasUnits.setBounds(20, 252, 80, 20);
		contentPanel.add(lblMeasUnits);

		JLabel lblMRP = new JLabel("MRP :");
		lblMRP.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMRP.setBounds(246, 255, 80, 14);
		contentPanel.add(lblMRP);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(362, 165, 68, 14);
		contentPanel.add(lblQty);

		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUnitPrice.setBounds(511, 165, 70, 14);
		contentPanel.add(lblUnitPrice);

		JLabel lblTax = new JLabel("CGST");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(588, 165, 67, 14);
		contentPanel.add(lblTax);

		JLabel lblDiscount = new JLabel("Discount(%)");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(813, 165, 78, 14);
		contentPanel.add(lblDiscount);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
//				int i=0;
//				InvoiceDBConnection db=new InvoiceDBConnection();
//				ResultSet rs=db.retrievePOItemCheck(ID, itemIDSTR);
//				try {
//					while(rs.next()) {
//						i++;
//					}
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				db.closeConnection();
//				if(!(i>0)) {
//					poQtyTF.setText("0");
//					System.out.println("arun");
//				}
//				
				
				addItemLine();

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(891, 156, 114, 25);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(645, 224, 86, 14);
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
				measUnitV.remove(cur_selectedRow);
				qtyV.remove(cur_selectedRow);
				freeqtyV.remove(cur_selectedRow);
				paidqtyV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				mrpPriceV.remove(cur_selectedRow);
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
		btnRemove.setBounds(890, 190, 114, 25);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 312, 992, 191);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Double Click to edit item");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Item ID", "Item Name", "Item Batch.", "Qty.", "Free Qty", "Paid Qty.", "Unit Price", "CGST", "SGST", "Discount", "Amount(Wouthout Vat)", "Value", "Expiry"
				}
				));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(75);
		table.getColumnModel().getColumn(5).setMinWidth(75);
		table.getColumnModel().getColumn(6).setMinWidth(75);
		table.getColumnModel().getColumn(7).setPreferredWidth(65);
		table.getColumnModel().getColumn(7).setMinWidth(65);
		table.getColumnModel().getColumn(8).setPreferredWidth(65);
		table.getColumnModel().getColumn(8).setMinWidth(65);
		table.getColumnModel().getColumn(9).setPreferredWidth(65);
		table.getColumnModel().getColumn(9).setMinWidth(65);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(75);
		table.getColumnModel().getColumn(11).setPreferredWidth(100);
		table.getColumnModel().getColumn(11).setMinWidth(90);
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

					searchItemTF.setText(itemIDV.get(cur_selectedRow));
					batchNumberTF.setText(batchNumberV.get(cur_selectedRow));
					java.util.Date date = null;
					if(!expiryDateV.get(cur_selectedRow).equals(""))
					{
					try {
						date = new SimpleDateFormat("yyyy-MM-d").parse(String
								.valueOf(expiryDateV.get(cur_selectedRow)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}}
					expiryDateC.setDate(date);
					//						qtyTF.setText((qtyV.get(cur_selectedRow)));
					qtyTF.setText((paidqtyV.get(cur_selectedRow)));
					taxTF.setText(taxV.get(cur_selectedRow));
					discountTF.setText(discountV.get(cur_selectedRow));
					itemSurchargeTF.setText(surchargeV.get(cur_selectedRow));
					//						expiryDateC.setDate(new Date(DateFormatChange.StringToDateFormat(expiryDateV.get(cur_selectedRow))));
					freeQtyTF.setText(freeqtyV.get(cur_selectedRow));
					poQtyTF.setText(qtyV.get(cur_selectedRow));
					itemIDV.remove(cur_selectedRow);
					itemNameV.remove(cur_selectedRow);
					itemDescV.remove(cur_selectedRow);
					measUnitV.remove(cur_selectedRow);
					qtyV.remove(cur_selectedRow);
					freeqtyV.remove(cur_selectedRow);
					paidqtyV.remove(cur_selectedRow);
					unitPriceV.remove(cur_selectedRow);
					mrpPriceV.remove(cur_selectedRow);
					taxV.remove(cur_selectedRow);
					surchargeV.remove(cur_selectedRow);
					taxValueV.remove(cur_selectedRow);
					surchargeValueV.remove(cur_selectedRow);
					discountV.remove(cur_selectedRow);
					totalValueV.remove(cur_selectedRow);
					expiryDateV.remove(cur_selectedRow);
					batchNumberV.remove(cur_selectedRow);
					addStock.remove(cur_selectedRow);
					//						PurchasePriceVV.remove(cur_selectedRow);
					//						discountV.get(cur_selectedRow);
					//						 discountTF.setText(discountV.get(cur_selectedRow));
					loadDataToTable();
					btnRemove.setEnabled(false);
					qtyTF.requestFocusInWindow();
				}
			}
		});
		JSeparator separator = new JSeparator();
		separator.setBounds(10, 149, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Save Invoice");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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
				if (finalamountTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Invoice Amount", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else{
					double finalamountentered=Double.parseDouble(finalamountTF.getText().toString());
					double finalamountgenerated= Double.parseDouble(finalTotalTF.getText().toString());
					if (finalamountentered!=finalamountgenerated) {
						JOptionPane.showMessageDialog(null, "Invoice Amount and Total Amount are not match ",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
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
				data[11] = finalDiscountValue + "";
				data[12] = finalTotalTF.getText().toString();
				data[13] = finalReturnAmount+"";

				InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
				TableModel model = table.getModel();
				for (int rows = 0; rows < model.getRowCount(); rows++) { // For
					// each
					// table
					// row
					for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
						// each
						// table
						// column
						if (model.getValueAt(rows, 11).toString().equals("")) {
							JOptionPane.showMessageDialog(null,
									"Please enter Expiry Date", "Input Error",
									JOptionPane.ERROR_MESSAGE);
							return ;
						}
						if (model.getValueAt(rows, 2).toString().equals("")) {
							JOptionPane.showMessageDialog(null,
									"Please enter Batch Value", "Input Error",
									JOptionPane.ERROR_MESSAGE);
							return;
						}
					}
				}
				try {
					index = invoiceDBConnection.inserInvoice(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				ChallanDBConnection challanDBConnection=new ChallanDBConnection();
				ReturnInvoiceDBConnection returnInvoiceDBConnection=new ReturnInvoiceDBConnection();
				data[3] = index + "";
				data[16] = "" + DateFormatChange.StringToMysqlDate(new Date());
				data[17] = "" + timeFormat.format(cal1.getTime());
				data[18] = ""+StoreMain.userName; // /user
				DecimalFormat df = new DecimalFormat("#.##");
				for (int i = 0; i < itemIDV.size(); i++) {
					data[0] = itemIDV.get(i);
					data[1] = itemNameV.get(i);
					data[2] = itemDescV.get(i);
					data[4] = measUnitV.get(i);
					data[5] = qtyV.get(i);
					data[6] = freeqtyV.get(i);
					data[7] =model.getValueAt(i, 4).toString();
					data[8] = unitPriceV.get(i);
					data[9] = discountV.get(i);
					data[10] = taxV.get(i);
					data[11] = surchargeV.get(i);
					data[12] = taxValueV.get(i);
					data[13] = surchargeValueV.get(i);
					data[14] = totalValueV.get(i);
					data[15] =  model.getValueAt(i, 12).toString();
					data[19] =model.getValueAt(i, 2).toString();
					data[20]=mrpPriceV.get(i);



					Double actualUnitPrice=0.0,purchasePrice=0.0,dvalue=0.0;
					int qty1=0,freeq=0;

					//					dvalue=Double.parseDouble(discountTF.getText());
					dvalue=  Double.parseDouble(discountV.get(i))/ (Double.parseDouble(qtyV.get(i))-Double.parseDouble(freeqtyV.get(i)));

					System.out.print("unitppp"+dvalue);
					actualUnitPrice=(Double.parseDouble(unitPriceV.get(i))-dvalue) *100.0f/ 100.0f;
					//					Syste
					System.out.print("unit"+unitPriceV.get(i));
					purchasePrice=actualUnitPrice*(Double.parseDouble(qtyV.get(i))-Double.parseDouble(freeqtyV.get(i)))/Double.parseDouble(qtyV.get(i));

					if(purchasePrice==0.0){
						purchasePrice=actualUnitPrice;
					}

					data[21] = df.format(purchasePrice)+""; 
					PurchasePriceVV.add(data[21] +"");

					mrpUpdate(Double.parseDouble(mrpPriceV.get(i)));  
					unitPriceUpdate(itemIDV.get(i),unitPriceV.get(i));
					try {
						invoiceDBConnection.inserInvoiceItem(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					int s1=Integer.parseInt(model.getValueAt(i, 4).toString());
					try {
						invoiceDBConnection.UpdateReceivedQtyInPO(NewInvoice.po_id, itemIDV.get(i),String.valueOf(s1));
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
							itemsDBConnection.addStock(itemIDV.get(i), String.valueOf(s1),
									expiryDateV.get(i),unitPriceV.get(i),mrpPriceV.get(i));


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

				BatchTrackingDBConnection batchTrackingDBConnection=new BatchTrackingDBConnection();
				for (int i = 0; i < itemIDV.size(); i++) {
					//					
					//					data[0] = itemIDV.get(i);
					//					data[1] = itemNameV.get(i);
					//					data[2] = itemDescV.get(i);
					//					data[3] = batchNumberV.get(i);
					//					data[4] = qtyV.get(i);
					//					data[5] = qtyV.get(i);
					//					data[6] = expiryDateV.get(i);
					//					data[7] =  "" + DateFormatChange.StringToMysqlDate(new Date());
					//					data[8] = "" + timeFormat.format(cal1.getTime());
					//					data[9] ="" + DateFormatChange.StringToMysqlDate(new Date());
					data[0] = itemIDV.get(i);
					data[1] = itemNameV.get(i);
					data[2] = itemDescV.get(i);
					data[3] = model.getValueAt(i, 2).toString();
					data[4] = paidqtyV.get(i);
					data[5] = paidqtyV.get(i);
					data[6] = model.getValueAt(i, 12).toString();
					data[7] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					data[8] = "" + timeFormat.format(cal1.getTime());
					data[9] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					Double actualUnitPrice=0.0,purchasePrice=0.0,dvalue=0.0;
					int qty1=0,freeq=0;

					//					dvalue=Double.parseDouble(discountTF.getText());
					dvalue=  Double.parseDouble(discountV.get(i))/ (Double.parseDouble(qtyV.get(i))-Double.parseDouble(freeqtyV.get(i)));
					System.out.println("dvale"+dvalue);
					actualUnitPrice=(Double.parseDouble(unitPriceV.get(i))-dvalue) *100.0f/ 100.0f;
					System.out.print("unit"+actualUnitPrice+"mk"+quantity+"hhh"+freeQuantity);
					purchasePrice=actualUnitPrice*(Double.parseDouble(qtyV.get(i))-Double.parseDouble(freeqtyV.get(i)))/Double.parseDouble(qtyV.get(i));

					if(purchasePrice==0.0){
						purchasePrice=actualUnitPrice;
					}
					//					
					data[10] = df.format(purchasePrice)+""; 

					data[11]=mrpPriceV.get(i);
					data[12]=measUnitV.get(i);
					data[13]= taxV.get(i);
					data[14]=surchargeV.get(i);
					data[15]=taxValueV.get(i);
					data[16]=surchargeValueV.get(i);
					data[17]=unitPriceV.get(i);
					data[18]=unitPriceV.get(i); 
					data[19]=invoiceDateSTR;
					try {
						if(addStock.get(i)&&!qtyV.get(i).equals("0"))
						{
							//							if(!addStockItem.get(i))
							//							{
							//								batchTrackingDBConnection.deleteItemBatches(itemIDV.get(i));
							//							}
							batchTrackingDBConnection.inserDataNEW(data);
						}
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				batchTrackingDBConnection.closeConnection();
				InvoiceDBConnection invoiceDBConnection1 = new InvoiceDBConnection();
				ResultSet rs1= invoiceDBConnection1.retrievePOReceQty(NewInvoice.po_id);
				int counter=0;
				//				try {
				//					 counter = rs1.getRow();
				//				} catch (SQLException e2) {
				//					// TODO Auto-generated catch block
				//					e2.printStackTrace();
				//				}
				int i=0;
				//				ResultSet rs = db.retrievePOItemData(ID);
				try {
					while (rs1.next()) {
						String item_qty= rs1.getString("item_qty");
						String item_paid_qty = rs1.getString("item_paid_quantity");
						System.out.println(item_qty+"total"+item_paid_qty);
						if (Integer.parseInt(item_qty)<=Integer.parseInt(item_paid_qty)) {
							//						if (item_qty.equals(item_paid_qty)){
							System.out.println("show"+i);
							i++;
						}
						// System.out.println(s + "   " + n);
						counter++;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				System.out.println("i"+i+"cou"+counter);
				if(i==counter){
					try {
						invoiceDBConnection1.UpdatePoStatus(NewInvoice.po_id,"CLOSE");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else{
					try {
						invoiceDBConnection1.UpdatePoStatus(NewInvoice.po_id,"PENDING");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				invoiceDBConnection1.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Invoice saved successfully", "Invoice Save",
						JOptionPane.INFORMATION_MESSAGE);
				try {
					new Bill_PDF(index + "",supplierDisplaySTR);
				} catch (DocumentException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(672, 551, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(835, 551, 153, 39);
		contentPanel.add(btnCancel);

		JLabel label_1 = new JLabel("");
		label_1.setIcon(new ImageIcon(NewInvoice.class
				.getResource("/icons/invoice_icon.jpg")));
		label_1.setBounds(419, 0, 100, 148);
		contentPanel.add(label_1);

		JLabel lblTax_1 = new JLabel("Tax % :");
		lblTax_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTax_1.setBounds(32, 540, 51, 14);
		contentPanel.add(lblTax_1);

		JLabel lblDiscount_1 = new JLabel("Discount :");
		lblDiscount_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDiscount_1.setBounds(186, 540, 71, 14);
		contentPanel.add(lblDiscount_1);

		finalTaxTF = new JTextField();
		finalTaxTF.setEditable(false);
		finalTaxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTaxTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalTaxTF.setBounds(20, 558, 109, 25);
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
		finalDiscountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalDiscountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalDiscountTF.setColumns(10);
		finalDiscountTF.setBounds(183, 558, 109, 25);
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
		expiryDateC.setBounds(728, 221, 152, 20);
		contentPanel.add(expiryDateC);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotal.setBounds(523, 540, 80, 14);
		contentPanel.add(lblTotal);

		finalTotalTF = new JTextField();
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		finalTotalTF.setBounds(511, 558, 113, 25);
		contentPanel.add(finalTotalTF);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.setBounds(758, 80, 218, 25);
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
		invoiceDate.setMaxSelectableDate(new Date());
		JDateChooser dueDate = new JDateChooser();
		dueDate.setBounds(622, 116, 135, 25);
		contentPanel.add(dueDate);

		batchNumberTF = new JTextField();
		batchNumberTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchNumberTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNumberTF.setColumns(10);
		batchNumberTF.setBounds(260, 221, 164, 20);
		contentPanel.add(batchNumberTF);

		JLabel lblBatchNo = new JLabel("Batch No. :");
		lblBatchNo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchNo.setBounds(183, 224, 80, 14);
		contentPanel.add(lblBatchNo);

		freeQtyTF = new JTextField();
		freeQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		freeQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		freeQtyTF.setColumns(10);
		freeQtyTF.setBounds(430, 190, 71, 20);
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
					checkFreeQTY(freeQuantity);
					itemValue();

				} else {

					freeQuantity = 0;
					checkFreeQTY(freeQuantity);
					itemValue();
				}
			}


			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {

					freeQuantity = Integer.parseInt(str);
					checkFreeQTY(freeQuantity);
					itemValue();

				} else {

					freeQuantity = 0;
					checkFreeQTY(freeQuantity);
					itemValue();
				}

			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = freeQtyTF.getText() + "";
				if (!str.equals("")) {
					freeQuantity = Integer.parseInt(str);
					checkFreeQTY(freeQuantity);
					itemValue();

				} else {

					freeQuantity = 0;
					checkFreeQTY(freeQuantity);
					itemValue();
				}

			}

			private void checkFreeQTY(int freeQty) {
				// TODO Auto-generated method stub
				if(freeQty<oldfreeQuantity && freeQty!=0)
				{
					old_freeQty.setText(oldfreeQuantity+ "");
					old_freeQty.setForeground(Color.RED);
					lblLastPurchaseQty.setForeground(Color.RED);
				}else if(freeQty>oldfreeQuantity && freeQty!=0)
				{
					old_freeQty.setText(oldfreeQuantity+ "");
					old_freeQty.setForeground(Color.blue);
					lblLastPurchaseQty.setForeground(Color.blue);
				}else
				{
					old_freeQty.setText(oldfreeQuantity+ "");
					old_freeQty.setForeground(Color.BLACK);
					lblLastPurchaseQty.setForeground(Color.BLACK);
				}
			}
		});

		JLabel lblFreeQty = new JLabel("Free Qty.");
		lblFreeQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblFreeQty.setBounds(433, 165, 68, 14);
		contentPanel.add(lblFreeQty);

		itemSurchargeTF = new JTextField();
		itemSurchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemSurchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemSurchargeTF.setColumns(10);
		itemSurchargeTF.setBounds(665, 190, 61, 20);
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
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(667, 165, 67, 14);
		contentPanel.add(lblSurChg);

		JButton btnNewItem = new JButton("");
		btnNewItem.setIcon(new ImageIcon(NewInvoice.class.getResource("/icons/plus_button.png")));
		btnNewItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				NewItem newItem=new NewItem();
				newItem.InvoiceInstance(NewInvoice.this);
				newItem.setModal(true);
				newItem.setVisible(true);
			}
		});
		btnNewItem.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewItem.setBounds(891, 220, 51, 25);
		contentPanel.add(btnNewItem);

		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(!itemDescSTR.equals(""))
				{
					EditItem editItem=new EditItem(itemIDSTR);
					editItem.newInvoiceInstatnce(NewInvoice.this);
					editItem.setModal(true);
					editItem.setVisible(true);
				}

			}
		});
		button.setIcon(new ImageIcon(NewInvoice.class.getResource("/icons/edit_button.png")));
		button.setFont(new Font("Tahoma", Font.BOLD, 13));
		button.setBounds(952, 220, 51, 25);
		contentPanel.add(button);


		JLabel lblTaxebleAmount = new JLabel("Taxable Amount :");
		lblTaxebleAmount.setBounds(22, 514, 124, 14);
		contentPanel.add(lblTaxebleAmount);

		taxableAmountLB = new JLabel("taxableAmountLB");
		taxableAmountLB.setBounds(156, 514, 100, 14);
		contentPanel.add(taxableAmountLB);

		taxAmountLB = new JLabel("taxableAmountLB");
		taxAmountLB.setBounds(401, 515, 100, 14);
		contentPanel.add(taxAmountLB);

		JLabel lblSaleTax = new JLabel("CGST :");
		lblSaleTax.setBounds(330, 514, 68, 14);
		contentPanel.add(lblSaleTax);

		surchargeLB = new JLabel("surchargeLB");
		surchargeLB.setBounds(631, 515, 100, 14);
		contentPanel.add(surchargeLB);

		JLabel lblSurchargeAmount = new JLabel("SGST  :");
		lblSurchargeAmount.setBounds(561, 514, 77, 14);
		contentPanel.add(lblSurchargeAmount);

		coinADJLB = new JLabel("coints");
		coinADJLB.setBounds(865, 514, 100, 14);
		contentPanel.add(coinADJLB);

		JLabel lblCoinAdj = new JLabel("Coin ADJ.  :");
		lblCoinAdj.setBounds(778, 514, 77, 14);
		contentPanel.add(lblCoinAdj);


		igstTF = new JTextField();
		igstTF.setHorizontalAlignment(SwingConstants.RIGHT);
		igstTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		igstTF.setColumns(10);
		igstTF.setBounds(740, 190, 61, 20);
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
		lblIgst.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblIgst.setBounds(742, 165, 67, 14);
		contentPanel.add(lblIgst);

		JButton button_1 = new JButton("Add Return Goods");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				AddReturnGoods addReturnGoods=new AddReturnGoods(supplierNameSTR,NewInvoice.this);
				addReturnGoods.setModal(true);
				addReturnGoods.setVisible(true);

			}
		});
		button_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		button_1.setBounds(645, 249, 172, 25);
		contentPanel.add(button_1);

		JButton button_2 = new JButton("Add Challan");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if(!supplierID.equals(""))
				{
					SearchChallan searchChallan=new SearchChallan(supplierID);
					searchChallan.NewInvoiceInstance(NewInvoice.this);
					searchChallan.setModal(true);
					searchChallan.setVisible(true);
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Please select supplier first", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		button_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		button_2.setBounds(828, 249, 114, 25);
		contentPanel.add(button_2);
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
		supplierTF.setText(VendorName);
		orderNoTF.setText(PONumber);

		measUnitET = new JTextField();
		measUnitET.setFont(new Font("Tahoma", Font.BOLD, 11));
		measUnitET.setColumns(10);
		measUnitET.setBounds(110, 252, 119, 20);
		contentPanel.add(measUnitET);

		JLabel label = new JLabel("Return Amount:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(342, 540, 110, 14);
		contentPanel.add(label);

		returnAmount = new JTextField();
		returnAmount.setHorizontalAlignment(SwingConstants.RIGHT);
		returnAmount.setFont(new Font("Tahoma", Font.BOLD, 14));
		returnAmount.setColumns(10);
		returnAmount.setBounds(333, 558, 119, 25);
		contentPanel.add(returnAmount);

		JLabel label_2 = new JLabel("PO Qty:");
		label_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_2.setBounds(388, 255, 51, 14);
		contentPanel.add(label_2);

		poQtyTF = new JTextField();
		poQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		poQtyTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		poQtyTF.setEditable(false);
		poQtyTF.setColumns(10);
		poQtyTF.setBounds(444, 252, 61, 20);
		contentPanel.add(poQtyTF);

		JLabel label_3 = new JLabel("Discount:");
		label_3.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_3.setBounds(511, 255, 61, 14);
		contentPanel.add(label_3);

		discountTF = new JTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setColumns(10);
		discountTF.setBounds(574, 250, 61, 22);
		contentPanel.add(discountTF);

		JLabel labelfinalamount = new JLabel("Invoice Amount");
		labelfinalamount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		labelfinalamount.setBounds(768, 116, 112, 25);
		contentPanel.add(labelfinalamount);

		finalamountTF = new JTextField();
		finalamountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		finalamountTF.setColumns(10);
		finalamountTF.setBounds(880, 116, 135, 25);
		contentPanel.add(finalamountTF);

		old_pr_price = new JTextField();
		old_pr_price.setEditable(false);
		old_pr_price.setBounds(171, 281, 92, 19);
		contentPanel.add(old_pr_price);
		old_pr_price.setColumns(10);

		lblPurchasePrice = new JLabel("Last Purchase Price :");
		lblPurchasePrice.setFont(new Font("Dialog", Font.BOLD, 11));
		lblPurchasePrice.setBounds(25, 285, 135, 14);
		contentPanel.add(lblPurchasePrice);

		old_mrp = new JTextField();
		old_mrp.setEditable(false);
		old_mrp.setBounds(419, 281, 61, 19);
		contentPanel.add(old_mrp);
		old_mrp.setColumns(10);


		lblLastPurchaseMrp = new JLabel("Last Purchase MRP :");
		lblLastPurchaseMrp.setFont(new Font("Dialog", Font.BOLD, 11));
		lblLastPurchaseMrp.setBounds(270, 286, 164, 14);
		contentPanel.add(lblLastPurchaseMrp);

		lblLastPurchaseQty = new JLabel("Last purchase Free Qty (%) :");
		lblLastPurchaseQty.setFont(new Font("Dialog", Font.BOLD, 11));
		lblLastPurchaseQty.setBounds(506, 285, 196, 14);
		contentPanel.add(lblLastPurchaseQty);

		old_freeQty = new JTextField();
		old_freeQty.setEditable(false);
		old_freeQty.setBounds(701, 281, 100, 19);
		contentPanel.add(old_freeQty);
		old_freeQty.setColumns(10);



		returnAmount.addKeyListener(new KeyAdapter() {
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
		returnAmount.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = returnAmount.getText() + "";
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
						String str = returnAmount.getText() + "";
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
						String str = returnAmount.getText() + "";
						if (!str.equals("")) {

							finalReturnAmount = Double.parseDouble("0" + str);
							finalTotal();

						} else {

							finalReturnAmount = 0;
							finalTotal();
						}
					}
				});
		InvoiceDBConnection db = new InvoiceDBConnection();

		ResultSet rs = db.retrievePOItemData(ID);
		try {
			while (rs.next()) {
				String s = rs.getString("item_id");
				String n = rs.getString("item_qty");
				String paid_qty = rs.getString("item_paid_quantity");
				if(Integer.parseInt(paid_qty)<Integer.parseInt(n) || paid_qty.equals("0")){
					flag =true;
					searchItemTF.setText(s);
					qtyTF.setText(n);
					
					addItemLine();
				}
				// System.out.println(s + "   " + n);
				//				searchItemTF.setText(s);
				//				qtyTF.setText(n);
				//				addItemLine();
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


	}
	public void addItemLine(){


		//	Double actualUnitPrice=0.0,purchasePrice=0.0,dvalue=0.0;
		//	int qty1=0,freeq=0;
		//	
		////	dvalue=Double.parseDouble(discountTF.getText());
		//	dvalue=unitPrice * (discountPrcntValue / 100.0f);
		////	String str = qtyTF.getText() + "";
		//	System.out.print("kk"+dvalue);
		////	qty1 = Integer.parseInt(str);
		////	String str1=freeQtyTF.getText();
		////	freeq=Integer.parseInt(str1);
		////	qty1=Double.parseDouble(qtyTF.getText()+"");
		////	freeq=Double.parseDouble(freeQtyTF.getText()+"");
		//	actualUnitPrice=unitPrice-dvalue;
		//	System.out.print("mk"+quantity+"hhh"+freeQuantity);
		//	purchasePrice=actualUnitPrice*(quantity-freeQuantity)/quantity;
		////	System.out.println("pp"+purchasePrice);
		////	PurchasePriceVV.add(Math.round(purchasePrice)+"");
		//	PurchasePriceVV.add(unitPrice+"");
		//	PurchasePriceVV.add(Math.round(purchasePrice* 100.0) / 100.0+"");
		if (supplierCB.getSelectedIndex() == -1) {
			JOptionPane.showMessageDialog(null,
					"Please select supplier first", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
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
		//	else if (batchNumberTF.getText().toString().equals("")) {
		//		JOptionPane.showMessageDialog(null,
		//				"Please enter Batch Number", "Input Error",
		//				JOptionPane.ERROR_MESSAGE);
		//		return;
		//	} else if (expiryDateSTR.equals("")) {
		//		JOptionPane.showMessageDialog(null,
		//				"Please enter expiry date", "Input Error",
		//				JOptionPane.ERROR_MESSAGE);
		//		return;
		//	} 
		//	else if (itemIDV.indexOf(itemIDSTR) != -1) {
		//		JOptionPane.showMessageDialog(null,
		//				"this item already entered", "Input Error",
		//				JOptionPane.ERROR_MESSAGE);
		//		return;
		//	}
		VendorQuotationDBConnection quotationDBConnection = new VendorQuotationDBConnection();

		String price=quotationDBConnection.retrieveItems(supplierID+"",itemIDSTR+"",unitPrice+"");

		//	if(!price.equals(unitPrice))
		//	{
		//		JOptionPane.showMessageDialog(null,
		//				"Last Quotation Price Difference\n Quoted Price : "+price, "Price Difference",
		//				JOptionPane.INFORMATION_MESSAGE);
		//	}
		String m="";
		if (oldunitPrice != unitPrice || oldMrp!=mrpPrice) {
			updatePrice();
			//	

			int dialogButton = JOptionPane.YES_NO_OPTION;
			int dialogResult = JOptionPane.showConfirmDialog(
					NewInvoice.this,
					"Do you want to change the mrp price." + "\n"
							+ " Current MRP is " + mrp + " Rupees",
							"Cancel", dialogButton);
			if (dialogResult == 0) {
				m = JOptionPane.showInputDialog("Update MRP Price", mrp);
				//		        System.out.println(m);


				if ((m != null) && (m.length() > 0)) {
					mrpUpdate(Double.parseDouble(m));  
				}else{

				}
				//			  if(!m.equals("")){
				//				
				//			  }


				//		    final JFrame frame = new JFrame("Frame");
				//	        JTextField field = new JTextField("Click me to open dialog!");
				//	        frame.add(field);
				//	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				//	        frame.setSize(200, 200);
				//	        setLocationRelativeTo(null);  
				//	        frame.setVisible(true);
			}
			//		oldunitPrice
		}
		//	if(oldMrp!=mrpPrice){
		//		
		//	}
		//	else{

		if(measUnitET.getText().toString().equals("")){
			measUnitV.add(""+measUnitS);
		}else{
			measUnitV.add(measUnitET.getText().toString());
		}
		if(m.equals("")){
			mrpPriceV.add(mrpPriceTF.getText().toString() + "");
		}else{
			mrpPriceV.add(Double.parseDouble(m) + "");
		}
		itemIDV.add(itemIDSTR);
		itemNameV.add(itemNameSTR);
		itemDescV.add(itemDescSTR);
		//		measUnitV.add(measUnitCD.getSelectedItem().toString());
		batchNumberV.add(batchNumberTF.getText().toString().toUpperCase());
		if (poQtyTF.getText().toString().equals("")) {
			qtyV.add("" + quantity);
		} else {
			qtyV.add(poQtyTF.getText().toString());
		}
		freeqtyV.add("" + freeQuantity);
		paidqtyV.add("" + quantity);
		//		paidqtyV.add("" + paidQuantity);
		unitPriceV.add(unitPrice + "");
		//		mrpPriceV.add(mrpPriceTF.getText().toString() + "");
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
		measUnitET.setText("");
		poQtyTF.setEditable(false);
		searchItemTF.requestFocusInWindow();
		expiryDateC.setCalendar(null);
		expiryDateSTR="";
		loadDataToTable();
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
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index,flag);
		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				itemPrPrice.addElement(resultSet.getObject(7).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		flag=false;
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}
	public void getItemFreeQty(String index) {

		InvoiceDBConnection itemsDBConnection = new InvoiceDBConnection();
		ResultSet resultSet = itemsDBConnection.retrieveFreeQuantity(index);
	    int number=0;
		try {
			while (resultSet.next()) {
		
				String str=resultSet.getString(1);
				try
				{
				    if(str != null)
				      number = Integer.parseInt(str);
				}
				catch (NumberFormatException e)
				{
				    number = 0;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		oldfreeQuantity=number;
		itemsDBConnection.closeConnection();
		
	}

	public void getItemDetail(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		measUnit.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				measUnitS=resultSet.getObject(4).toString();
				//				measUnit.addElement(resultSet.getObject(4).toString());
				taxTypeSTR = resultSet.getObject(5).toString();
				taxValueSTR = resultSet.getObject(6).toString();
				System.out
				.println(itemDescSTR + "  "
						+ resultSet.getObject(7).toString() + " "
						+ taxValueSTR);
				unitPrice = Double.parseDouble("0"
						+ resultSet.getObject(7).toString());
				oldunitPrice = unitPrice;
				mrp = Double.parseDouble("0"+resultSet.getObject(11).toString());

				oldMrp=mrp;
				itemSurchargeSTR= resultSet.getObject(12).toString();
				itemIgstSTR= resultSet.getObject(13).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		//		measUnitCD.setModel(measUnit);
		//		if (i > 0) {
		//			measUnitCD.setSelectedIndex(0);
		//		}
	}

	public void itemValue() {
		paidQuantity=quantity-freeQuantity;

		itemValue = paidQuantity * unitPrice;
		double discount= itemValue * (discountPrcntValue / 100.0f);
		discountValue=discount;
		discountTF.setText(discountValue+"");
		itemValue = itemValue - discountValue;
		System.out.print("before"+itemValue);
		//		itemValue = itemValue - discountValue;
		double k = itemValue * (taxValue / 100.0f);
		double surcharge = itemValue * (itemSurcharge / 100.0f);
		surcharge=Math.round(surcharge * 100.00) / 100.00;
		k=Math.round(k * 100.00) / 100.00;
		taxAmountValue=k;
		surchargeAmountValue=surcharge;

		System.out.print("after"+itemValue);
		double igst = itemValue * (itemIgst / 100.0f);
		igst=Math.round(igst * 100.00) / 100.00;
		igstAmountValue=igst;
		itemValue = itemValue + k;
		System.out.print("after1"+itemValue);
		itemValue = itemValue + surcharge;
		System.out.print("after2"+itemValue);
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
			finalTotalTF.setText("" + Math.ceil(total));
			finalTotalValueCoin=Math.ceil(total)-total;
		} else {
			finalTotalTF.setText("" + Math.floor(total));
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
		ObjectArray_ListOfexamsSpecs = new Object[size][14];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			// ObjectArray_ListOfexamsSpecs[i][3] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = freeqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = taxV.get(i) + "("
					+ taxValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][8] = surchargeV.get(i) + "("
					+ surchargeValueV.get(i) + ")";
			ObjectArray_ListOfexamsSpecs[i][9] = discountV.get(i);
			//			System.out.println("free"+freeqtyV.get(i));
			double amountWT =( Double.parseDouble(paidqtyV.get(i))-Double.parseDouble(freeqtyV.get(i)))
					* Double.parseDouble(unitPriceV.get(i));
			amountWT = Math.round(amountWT * 100.00) / 100.00;
			ObjectArray_ListOfexamsSpecs[i][10] = amountWT;
			ObjectArray_ListOfexamsSpecs[i][11] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][12] = expiryDateV.get(i);
			ObjectArray_ListOfexamsSpecs[i][13] = mrpPriceV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
			taxable=taxable+amountWT;
			tax=tax+Double.parseDouble(taxValueV.get(i));
			surcharge=surcharge+Double.parseDouble(surchargeValueV.get(i));
		}
		final boolean[] canEdit = new boolean[] { false, false, false, false,
				false, false, false, false, false, false, false, false, false };
		DefaultTableModel model = new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs, new String[] { "Item ID",
						"Item Name", "Item Batch.", "PO Qty.", "Recieved Qty.","Free Qty",
						"Unit Price", "CGST", "SGST", "Discount",
						"Amount(W/T)", "Value", "Expiry", "MRP" }) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return canEdit[column];
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
		table.getColumnModel().getColumn(11).setPreferredWidth(100);
		table.getColumnModel().getColumn(11).setMinWidth(90);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		finalTotalValue = total;

		tax = Math.round(tax * 100.00) / 100.00;
		surcharge = Math.round(surcharge * 100.00) / 100.00;
		finalTotalTF.setText("" + finalTotalValue);
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
	public void mrpUpdate(Double mrp) {
		ItemsDBConnection db = new ItemsDBConnection();
		try {
			db.mrpupdateprice(itemIDSTR,mrp+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}
	public void unitPriceUpdate(String itemID,String UnitPrice) {
		ItemsDBConnection db = new ItemsDBConnection();
		try {
			db.Updateunitprice(itemID,UnitPrice);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			db.closeConnection();
			e.printStackTrace();
		}
		db.closeConnection();
	}
	public void addReturnGoods(String[] itemsID)
	{
		ReturnInvoiceDBConnection returnInvoiceDBConnection = new ReturnInvoiceDBConnection();
		for (int i = 0; i < itemsID.length; i++) {
			ResultSet resultSet = returnInvoiceDBConnection.retrieveReturenedGoodByid(itemsID[i]);

			try {
				while (resultSet.next()) {
					returnitemID.add(itemsID[i]);
					itemIDV.add(resultSet.getObject(1).toString());
					itemNameV.add(resultSet.getObject(2).toString());
					itemDescV.add(resultSet.getObject(3).toString());
					measUnitV.add("NA");
					qtyV.add("0");
					freeqtyV.add(resultSet.getObject(4).toString());
					paidqtyV.add("-"+resultSet.getObject(4).toString());
					unitPriceV.add(resultSet.getObject(5).toString());
					discountV.add(resultSet.getObject(6).toString());
					taxV.add(resultSet.getObject(7).toString());
					surchargeV.add(resultSet.getObject(8).toString());
					taxValueV.add("-"+resultSet.getObject(9).toString()+"");
					surchargeValueV.add("-"+resultSet.getObject(10).toString());
					totalValueV.add("-"+resultSet.getObject(11).toString());
					expiryDateV.add(resultSet.getObject(12).toString());
					batchNumberV.add(resultSet.getObject(13).toString());
					returnGoodBatch.add(resultSet.getObject(13).toString());
					addStock.add(false);

				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		returnInvoiceDBConnection.closeConnection();
		loadDataToTable();
	}	


	public void searchChallan(String searchChallan,String supplier_id)
	{
		ChallanDBConnection challanDBConnection = new ChallanDBConnection();
		ResultSet resultSet = challanDBConnection.retrieveChallan(searchChallan, supplier_id);

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

	public JTextField getIgstTF() {
		return igstTF;
	}
}
