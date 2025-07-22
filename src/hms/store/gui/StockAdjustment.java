package hms.store.gui;

import hms.main.DateFormatChange;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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

import com.toedter.calendar.JDateChooser;

public class StockAdjustment extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyTF;
	private JTextField unitPriceTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField totalValueTF;
	private JTable table;
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
	Vector<String> PurchasePriceVV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();
	Vector<String> mrpPriceV = new Vector<String>();
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
			itemSurchargeSTR, expiryDateSTR = "", invoiceDateSTR = "",
			dueDateSTR = "0000-00-00";
	double unitPrice = 0, oldunitPrice = 0, taxValue = 0, itemSurcharge = 0,
			discountValue = 0, itemValue, finalTaxValue = 0,
			finalDiscountValue = 0, finalTotalValue = 0,
			surchargeAmountValue = 0, taxAmountValue = 0,
			discountPrcntValue = 0;
	int quantity = 0, freeQuantity = 0, paidQuantity = 0;
	String mrp = "";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField batchNumberTF;
	private JTextField itemSurchargeTF;
	private JTextField mrpTF;
	private JTextField measunit;
	private JTextField discountprcnt;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			StockAdjustment dialog = new StockAdjustment();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public StockAdjustment() {
		setTitle("Stock Adjustment");
		setResizable(false);
		setBounds(100, 70, 1031, 557);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 87, 119, 20);
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
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					discountTF.setText("");
					// measUnitCD.setModel(measUnit);

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
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					discountTF.setText("");
					// measUnitCD.setModel(measUnit);
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
					taxTF.setText("");
					discountTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();
					discountTF.setText("");
					// measUnitCD.setModel(measUnit);
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(10, 118, 99, 20);
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
				unitPriceTF.setText("");
				taxTF.setText("");
				discountTF.setText("");
				itemSurchargeTF.setText("");

				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					if (!taxTypeSTR.equals("CompanyTax")) {
						taxTF.setText(taxValueSTR);
					}
					itemDescTF.setText("" + itemDescSTR);
					unitPriceTF.setText("" + unitPrice);
					itemSurchargeTF.setText("" + itemSurchargeSTR);
				}
			}
		});

		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 87, 210, 20);
		contentPanel.add(itemNameCB);

		qtyTF = new JTextField();
		qtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyTF.setBounds(449, 87, 86, 20);
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
		unitPriceTF.setBounds(545, 87, 86, 20);
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
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setBounds(641, 87, 90, 20);
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
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setBounds(435, 118, 48, 20);
		contentPanel.add(discountTF);
		discountTF.setColumns(10);
//		discountTF.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				char vChar = e.getKeyChar();
//				if (!(Character.isDigit(vChar)
//						|| (vChar == KeyEvent.VK_BACK_SPACE)
//						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
//					e.consume();
//
//					// ||vChar== '.'
//				}
//			}
//		});

//		discountTF.getDocument().addDocumentListener(new DocumentListener() {
//			@Override
//			public void insertUpdate(DocumentEvent e) {
//				String str = discountTF.getText() + "";
//				if (!str.equals("")) {
//
//					discountValue = Double.parseDouble("0" + str);
//					itemValue();
//
//				} else {
//
//					discountValue = 0;
//					itemValue();
//				}
//			}
//
//			@Override
//			public void removeUpdate(DocumentEvent e) {
//				String str = discountTF.getText() + "";
//				if (!str.equals("")) {
//
//					discountValue = Double.parseDouble("0" + str);
//					itemValue();
//
//				} else {
//
//					discountValue = 0;
//					itemValue();
//				}
//			}
//
//			@Override
//			public void changedUpdate(DocumentEvent e) {
//				String str = discountTF.getText() + "";
//				if (!str.equals("")) {
//
//					discountValue = Double.parseDouble("0" + str);
//					itemValue();
//
//				} else {
//
//					discountValue = 0;
//					itemValue();
//				}
//			}
//		});

		totalValueTF = new JTextField();
		totalValueTF.setEditable(false);
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		totalValueTF.setBounds(567, 118, 113, 20);
		contentPanel.add(totalValueTF);
		totalValueTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total Value :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(493, 121, 86, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 62, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Select Item :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(139, 62, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblMeasUnits = new JLabel("Meas Units");
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeasUnits.setBounds(359, 62, 80, 14);
		contentPanel.add(lblMeasUnits);

		JLabel lblQty = new JLabel("Qty.");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(452, 62, 68, 14);
		contentPanel.add(lblQty);

		JLabel lblUnitPrice = new JLabel("Unit Price ");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUnitPrice.setBounds(545, 62, 65, 14);
		contentPanel.add(lblUnitPrice);

		JLabel lblTax = new JLabel("Tax %.");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(643, 62, 88, 14);
		contentPanel.add(lblTax);

		JLabel lblDiscount = new JLabel("Discount(%)");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(813, 62, 72, 14);
		contentPanel.add(lblDiscount);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Double actualUnitPrice = 0.0, purchasePrice = 0.0, dvalue = 0.0;
				int qty1 = 0, freeq = 0;

				// dvalue=Double.parseDouble(discountTF.getText());
//				dvalue = unitPrice * (discountPrcntValue / 100.0f);
				// String str = qtyTF.getText() + "";
				// System.out.print("kk"+dvalue);
				// qty1 = Integer.parseInt(str);
				// String str1=freeQtyTF.getText();
				// freeq=Integer.parseInt(str1);
				// qty1=Double.parseDouble(qtyTF.getText()+"");
				// freeq=Double.parseDouble(freeQtyTF.getText()+"");
				actualUnitPrice = unitPrice - dvalue;
				System.out.print("mk" + quantity + "hhh" + freeQuantity);
				purchasePrice = actualUnitPrice * (quantity - freeQuantity)
						/ quantity;
//				PurchasePriceVV.add(Math.round(purchasePrice * 100.0) / 100.0
//						+ "");
				PurchasePriceVV.add(unitPrice+ "");
				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (qtyTF.getText().toString().equals("")) {
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
				} else if (batchNumberTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Batch Number", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				} else if (mrpTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter MRP",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				// else if (itemIDV.indexOf(itemIDSTR) != -1) {
				// JOptionPane.showMessageDialog(null,
				// "this item already entered", "Input Error",
				// JOptionPane.ERROR_MESSAGE);
				// return;
				// }
				// if (oldunitPrice != unitPrice) {
				// updatePrice();
				// int dialogButton = JOptionPane.YES_NO_OPTION;
				// int dialogResult = JOptionPane.showConfirmDialog(
				// StockAdjustment.this,
				// "Do you want to change the mrp price." + "\n"
				// + " Current MRP is " + mrp + " Rupees",
				// "Cancel", dialogButton);
				// if (dialogResult == 0) {
				// // EditItem editItem = new EditItem(itemIDSTR);
				// // editItem.setVisible(true);
				// // editItem.setModal(true);
				//
				//
				// String m =
				// JOptionPane.showInputDialog("Update MRP Price of Item "+itemNameSTR,
				// mrp);
				// // System.out.println(m);
				//
				// if ((m != null) && (m.length() > 0)) {
				// // mrpUpdate1(Double.parseDouble(m),data[0]);
				// // mrpPriceTF.setTexst(Double.parseDouble(m)+"");
				// mrpPriceV.add((Double.parseDouble(m)+""));
				//
				// } else {
				//
				// }
				//
				//
				//
				// }else{
				// mrpPriceV.add(mrp+"");
				// }
				// }else{
				mrpPriceV.add(mrpTF.getText() + "");
				// }

				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				measUnitV.add(measunit.getText());
				// measUnitV.add(measUnitCD.getSelectedItem().toString());
				batchNumberV.add(batchNumberTF.getText().toString()
						.toUpperCase());
				qtyV.add("" + quantity);
				freeqtyV.add("" + freeQuantity);
				paidqtyV.add("" + paidQuantity);
				unitPriceV.add(unitPrice + "");
				taxV.add(taxValue + "");
				surchargeV.add(itemSurcharge + "");
				taxValueV.add(taxAmountValue + "");
				surchargeValueV.add(surchargeAmountValue + "");
				discountV.add(discountValue + "");
				totalValueV.add(totalValueTF.getText() + "");
				expiryDateV.add(expiryDateSTR);
				searchItemTF.setText("");
				batchNumberTF.setText("");
				qtyTF.setText("");
				itemSurchargeTF.setText("");
				taxTF.setText("");
				discountprcnt.setText("");
				discountTF.setText("");
				mrpTF.setText("");
				searchItemTF.requestFocusInWindow();
//				expiryDateC.setCalendar(null);
				loadDataToTable();

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(890, 76, 114, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(682, 121, 86, 14);
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
				discountV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				batchNumberV.remove(cur_selectedRow);
				PurchasePriceVV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(890, 112, 114, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(33, 149, 992, 315);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Batch", "MU", "Qty.",
				"Batch Qty.", "Unit Price", "Discount", "Tax", "Value",
				"Expiry" }));
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
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(90);
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

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 46, 875, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Add Stock");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

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

				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				data[3] = index + "";
				data[16] = "" + DateFormatChange.StringToMysqlDate(new Date());
				data[17] = "" + timeFormat.format(cal1.getTime());
				data[18] = "" + StoreMain.userName; // /user
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
						itemsDBConnection.addStock(itemIDV.get(i), qtyV.get(i),
								expiryDateV.get(i),unitPriceV.get(i),"");
						// itemsDBConnection.updateOrderStaus(itemIDV.get(i),
						// "NO");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				itemsDBConnection.closeConnection();
				String[] datanew = new String[30];
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				for (int i = 0; i < itemIDV.size(); i++) {
					datanew[0] = itemIDV.get(i);
					datanew[1] = itemNameV.get(i);
					datanew[2] = itemDescV.get(i);
					datanew[3] = batchNumberV.get(i);
					datanew[4] = qtyV.get(i);
					datanew[5] = qtyV.get(i);
					datanew[6] = expiryDateV.get(i);
					datanew[7] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					datanew[8] = "" + timeFormat.format(cal1.getTime());
					datanew[9] = ""
							+ DateFormatChange.StringToMysqlDate(new Date());
					datanew[10] = PurchasePriceVV.get(i);
					datanew[11] = mrpPriceV.get(i);
					datanew[12] = measUnitV.get(i);
					datanew[13] = taxV.get(i);
					datanew[14] = surchargeV.get(i);
					datanew[15] = taxValueV.get(i);
					datanew[16] = surchargeValueV.get(i);
					datanew[17] = unitPriceV.get(i);
					// data[10]=PurchasePriceVV.get(i);
					try {
						batchTrackingDBConnection.inserData(datanew);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				batchTrackingDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null, "Items saved successfully",
						"Items Save", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(688, 475, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(851, 475, 153, 39);
		contentPanel.add(btnCancel);

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
						if ("date".equals(arg0.getPropertyName())) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		expiryDateC.setBounds(757, 118, 123, 20);
		contentPanel.add(expiryDateC);

		batchNumberTF = new JTextField();
		batchNumberTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchNumberTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNumberTF.setColumns(10);
		batchNumberTF.setBounds(182, 118, 138, 20);
		contentPanel.add(batchNumberTF);

		JLabel lblBatchNo = new JLabel("Batch No. :");
		lblBatchNo.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchNo.setBounds(119, 121, 80, 14);
		contentPanel.add(lblBatchNo);

		itemSurchargeTF = new JTextField();
		itemSurchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemSurchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemSurchargeTF.setColumns(10);
		itemSurchargeTF.setBounds(747, 87, 61, 20);
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
		itemSurchargeTF.getDocument().addDocumentListener(
				new DocumentListener() {
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

		JLabel lblSurChg = new JLabel("Sur. Chg.");
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(749, 62, 67, 14);
		contentPanel.add(lblSurChg);

		JLabel lblStockAdjustment = new JLabel("Stock Adjustment");
		lblStockAdjustment.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStockAdjustment.setBounds(10, 11, 429, 24);
		contentPanel.add(lblStockAdjustment);

		JButton btnResetStock = new JButton("Reset Stock");
		btnResetStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane.showConfirmDialog(
						StockAdjustment.this,
						"Are you sure to zero(0) Item Stock and Batch Stock",
						"Stock Zero", dialogButton);
				if (dialogResult == 0) {
					ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
					try {

						itemsDBConnection.resetStock(itemIDSTR);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					itemsDBConnection.closeConnection();

					BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
					try {
						batchTrackingDBConnection.resetStock(itemIDSTR);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					batchTrackingDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Item Stock Zero Successfully", "Stock Reset",
							JOptionPane.INFORMATION_MESSAGE);
				} else {

				}

			}
		});
		btnResetStock.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnResetStock.setBounds(891, 33, 114, 31);
		contentPanel.add(btnResetStock);

		JLabel lblMrp = new JLabel("MRP:");
		lblMrp.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMrp.setBounds(330, 121, 41, 14);
		contentPanel.add(lblMrp);

		mrpTF = new JTextField();
		mrpTF.setHorizontalAlignment(SwingConstants.RIGHT);
		mrpTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		mrpTF.setColumns(10);
		mrpTF.setBounds(359, 118, 55, 20);
		contentPanel.add(mrpTF);

		measunit = new JTextField();
		measunit.setHorizontalAlignment(SwingConstants.RIGHT);
		measunit.setFont(new Font("Tahoma", Font.BOLD, 11));
		measunit.setColumns(10);
		measunit.setBounds(359, 87, 80, 20);
		contentPanel.add(measunit);

		discountprcnt = new JTextField();
		discountprcnt.setHorizontalAlignment(SwingConstants.RIGHT);
		discountprcnt.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountprcnt.setColumns(10);
		discountprcnt.setBounds(820, 87, 61, 20);
		contentPanel.add(discountprcnt);

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

		measunit.addKeyListener(new KeyAdapter() {
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
				itemSurchargeSTR = resultSet.getObject(12).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		// measUnitCD.setModel(measUnit);
		// if (i > 0) {
		// measUnitCD.setSelectedIndex(0);
		// }
	}

//	public void itemValue() {
//		paidQuantity = quantity - freeQuantity;
//
//		itemValue = paidQuantity * unitPrice;
//
//		double k = itemValue * (taxValue / 100.0f);
//		double surcharge = itemValue * (itemSurcharge / 100.0f);
//
//		taxAmountValue = k;
//		surchargeAmountValue = surcharge;
//		itemValue = itemValue + k;
//		itemValue = itemValue + surcharge;
//
//		itemValue = itemValue - discountValue;
//
//		itemValue = Math.round(itemValue * 100.000) / 100.000;
//
//		totalValueTF.setText("" + itemValue);
//
//	}
	public void itemValue() {
		paidQuantity = quantity - freeQuantity;

		itemValue = paidQuantity * unitPrice;
//		itemValue = itemValue - discountValue;
		double discount= itemValue * (discountPrcntValue / 100.0f);
		double k = (itemValue-discount) * (taxValue / 100.0f);
		double surcharge = (itemValue-discount) * (itemSurcharge / 100.0f);
		surcharge = Math.round(surcharge * 100.00) / 100.00;
		k = Math.round(k * 100.00) / 100.00;
		discount= Math.round(discount * 100.00) / 100.00;
		discountValue=discount;
		discountTF.setText(discountValue+"");
		discountTF.setEnabled(false);
		taxAmountValue = k;
		surchargeAmountValue = surcharge;

//		double igst = itemValue * (itemIgst / 100.0f);
//		igst = Math.round(igst * 100.00) / 100.00;
//		igstAmountValue = igst;
		itemValue = itemValue -discount ;
		itemValue = itemValue + k;
		itemValue = itemValue + surcharge;
//		itemValue = itemValue + igst;
		itemValue = Math.round(itemValue * 100.00) / 100.00;
		totalValueTF.setText("" + itemValue);

	}
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][12];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = taxV.get(i) + "+"
					+ surchargeV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = discountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][10] = expiryDateV.get(i);
			ObjectArray_ListOfexamsSpecs[i][11] = mrpPriceV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch.", "MU",
						"Qty.", "Paid Qty.", "Unit Price", "Tax+Surcharge",
						"Discount", "Value", "Expiry", "MRP" }));

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
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(75);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(90);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		finalTotalValue = total;
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
}
