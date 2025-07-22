package hms.store.gui;

import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.slippdf.ReturnInvoicePdf;
import hms.store.database.BatchTrackingDBConnection;

import hms.store.database.ItemsDBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
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
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextArea;
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

import com.itextpdf.text.DocumentException;

public class NewReturnItemsForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyIssuedTF;
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemBatchName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> itemBatchIDV = new Vector<String>();
	Vector<String> itemBatchV = new Vector<String>();
	Vector<String> unitPriceV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> taxPercentageV = new Vector<String>();
	Vector<String> DiscountV = new Vector<String>();
	Vector<String> taxAmountV = new Vector<String>();
	Vector<String> cuttingQtyV = new Vector<String>();
	Vector<String> damageQtyV = new Vector<String>();
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeAmountValueV = new Vector<String>();
	Vector<String> batchID = new Vector<String>();
	
	Vector<String> expiryDateV = new Vector<String>();
	String supplierDisplaySTR,mobileSTR,addressSTR,supplierID,supplierNameSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, 
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
			previouseStock = "",itemBatchNameSTR="",itemLocationSTR="",payableSTR="";
	double qtyIssued = 0, afterIssued = 0, discountValue = 0,taxValue=0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0,price = 0,batchQty=0,taxAmountValue=0,surchargeAmountValue=0,taxAmountValue2,totalTaxAmount=0,completeTaxAmount,itemSurchargeValue;
	int quantity = 0;
	String batchIDSTR="0";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JTextField batchQtyTF;
	private JTextField taxTF;
	private JTextField discountTF;
	private JTextField itemPriceTF;
	private JTextField totalValueTF;
	private JTextField expiryDateTF;
	private JTextField totalPayAmountTF;
	private JComboBox batchNameCB;
	private JTextField itemLocationTF;
	private JTextField supplierTF;
	private JTextField mobileTF;
	private JTextField addressTF;
	private JTextField invoiceNoTF;
	private JComboBox supplierCB;
	private JTextArea returnReasonTA;
	private JTextField surchargeTF;
	private JTextField cuttingqtyTF;
	private JTextField damageqtyTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewReturnItemsForm dialog = new NewReturnItemsForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewReturnItemsForm() {
		setTitle("Return to supplier Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewReturnItemsForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 40, 1031, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 190, 106, 20);
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
					qtyIssuedTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
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
					qtyIssuedTF.setText("");
				
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
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
					qtyIssuedTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemBatchName.removeAllElements();
					batchNameCB.setModel(itemBatchName);
					measUnit.removeAllElements();
					
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(10, 221, 188, 20);
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
				itemBatchName.removeAllElements();
				batchNameCB.setModel(itemBatchName);
				itemDescTF.setText("");
				qtyInHandTF.setText("");
				itemPriceTF.setText("");
				itemLocationTF.setText("");
				taxTF.setText("");
				surchargeTF.setText("");
				cuttingqtyTF.setText("");
				damageqtyTF.setText("");
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {
					getItemBatchName(itemIDSTR);
					itemDescTF.setText("" + itemDescSTR);
					qtyInHandTF.setText("" + quantity);
					itemPriceTF.setText(""+price);
					afterIssued = quantity - qtyIssued;
					itemLocationTF.setText(itemLocationSTR);
					taxTF.setText(""+taxValue);
					surchargeTF.setText(""+itemSurchargeValue);
					cuttingqtyTF.setText("0.0");
					damageqtyTF.setText("0.0");
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(126, 190, 175, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(317, 220, 92, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(485, 190, 68, 20);
		contentPanel.add(qtyIssuedTF);
		qtyIssuedTF.setColumns(10);
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if(qtyIssued>batchQty)
				{
					batchQtyTF.setForeground(Color.RED);
				}
				else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if(qtyIssued>batchQty)
				{
					batchQtyTF.setForeground(Color.RED);
				}
				else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				if(qtyIssued>batchQty)
				{
					batchQtyTF.setForeground(Color.RED);
				}
				else {
					batchQtyTF.setForeground(Color.BLACK);
				}
				itemValue();
			}
		});

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblQty = new JLabel("Total Qty. in Hand");
		lblQty.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblQty.setBounds(201, 224, 119, 14);
		contentPanel.add(lblQty);

		JLabel lblqtyIssued = new JLabel("Enter Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(485, 165, 88, 14);
		contentPanel.add(lblqtyIssued);

		JButton btnNewButton = new JButton("Add Line");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (qtyIssuedTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				double totalstock=Double.parseDouble(qtyInHandTF.getText().toString());
				double cuttingqty=Double.parseDouble(cuttingqtyTF.getText().toString());
				
				double damagedqty=Double.parseDouble(damageqtyTF.getText().toString());
				qtyIssued=qtyIssued-(cuttingqty+damagedqty);
				if(totalstock-qtyIssued<0){
					JOptionPane.showMessageDialog(null,
							"Issued Quantity is greater than Stock Quantity",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (batchQty-qtyIssued < 0) {
					JOptionPane.showMessageDialog(null,
							"Issued Quantity is greater than Batch Quantity",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				} 
					if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				else if (itemBatchIDV.indexOf(batchIDSTR) != -1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
					itemValue();
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				issuedQtyV.add(qtyIssued + "");
				expiryDateV.add(expiryDateSTR);
				itemBatchIDV.add(batchIDSTR);
				itemBatchV.add(itemBatchNameSTR);
				unitPriceV.add(price+"");
				totalValueV.add(""+itemValue);
				taxPercentageV.add(taxValue+"");
				DiscountV.add(discountValue+"");
				taxAmountV.add(taxAmountValue+"");
				surchargeV.add(itemSurchargeValue+"");
				surchargeAmountValueV.add(surchargeAmountValue+"");
				cuttingQtyV.add(cuttingqty+"");
				damageQtyV.add(damagedqty+"");
				loadDataToTable();
				searchItemTF.setText("");

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(909, 176, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(672, 251, 86, 14);
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
				issuedQtyV.remove(cur_selectedRow);
				expiryDateV.remove(cur_selectedRow);
				itemBatchIDV.remove(cur_selectedRow);
				itemBatchV.remove(cur_selectedRow);
				unitPriceV.remove(cur_selectedRow);
				totalValueV.remove(cur_selectedRow);
				taxPercentageV.remove(cur_selectedRow);
				DiscountV.remove(cur_selectedRow);
				taxAmountV.remove(cur_selectedRow);
				surchargeV.remove(cur_selectedRow);
				surchargeAmountValueV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 221, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 278, 1002, 232);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Batch",
				"Unit Price", "Quantity", "tax", "Discount","Total Value","expiry" }));
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
		separator.setBounds(14, 152, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Save");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(supplierCB.getSelectedIndex()==-1)
				{
					JOptionPane.showMessageDialog(null,
							"Please select supplier", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				

				if(invoiceNoTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(itemIDV.size()==0)
				{
					JOptionPane.showMessageDialog(null,
							"Please add atlest one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index=0;
				String[] data=new String[30];
//				Double amount=Double.parseDouble(finalTotalValue);
				data[0]=invoiceNoTF.getText().toString();
				data[1]=supplierID;
				data[2]=supplierDisplaySTR;
				data[3]=DateFormatChange.StringToMysqlDate(new Date());
				data[4]=""+timeFormat.format(cal1.getTime());
				data[5]=""+StoreMain.userName;  //user
				data[6]=finalTotalValue-totalTaxAmount+"";
				data[7]=totalTaxAmount+"";
				data[8]=finalDiscountValue+"";
				data[9]=totalPayAmountTF.getText().toString();
				data[10]=returnReasonTA.getText().toString()+"";
				
				ReturnInvoiceDBConnection invoiceDBConnection=new ReturnInvoiceDBConnection();
				try {
					index=invoiceDBConnection.inserreturn_invoice(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				BatchTrackingDBConnection batchTrackingDBConnection=new BatchTrackingDBConnection();
				ItemsDBConnection itemsDBConnection=new ItemsDBConnection();
				data[3]=index+"";
				data[15]=""+DateFormatChange.StringToMysqlDate(new Date());
				data[16]=""+timeFormat.format(cal1.getTime());
				data[17]=""+StoreMain.userName;  ///user
				for (int i = 0; i < itemIDV.size(); i++) {
					double finalissued=Double.parseDouble(issuedQtyV.get(i))+Double.parseDouble(cuttingQtyV.get(i))+Double.parseDouble(damageQtyV.get(i));
					data[0]=itemIDV.get(i);
					data[1]=itemNameV.get(i);
					data[2]=itemDescV.get(i);
					data[4] = itemBatchIDV.get(i);
					data[5] = itemBatchV.get(i);
					data[6]=issuedQtyV.get(i);
					data[7]=unitPriceV.get(i);
					data[8]=DiscountV.get(i);
					data[9]=taxPercentageV.get(i);
					data[10]=taxAmountV.get(i);
					data[11]=surchargeV.get(i);
					data[12]=surchargeAmountValueV.get(i);
					data[13]=totalValueV.get(i);
					data[14]=expiryDateV.get(i);
					data[18]="1";
					data[19]=cuttingQtyV.get(i);
					data[20]=damageQtyV.get(i);
					data[21]=finalissued+"";
//					data[15]=""/
					
					try {
						invoiceDBConnection.inserInvoiceItem(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					try {
						itemsDBConnection.subtractStock(itemIDV.get(i),
								finalissued+"");
						batchTrackingDBConnection.subtractStock(itemBatchIDV.get(i),
								finalissued+"",DateFormatChange.StringToMysqlDate(new Date()), StoreMain.userName);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
				invoiceDBConnection.closeConnection();
				batchTrackingDBConnection.closeConnection();
				itemsDBConnection.closeConnection();
				
				JOptionPane.showMessageDialog(null,
						"Return Invoice saved successfully", "Return Invoice Save",
						JOptionPane.INFORMATION_MESSAGE);
				try {
					new ReturnInvoicePdf(index + "");
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
		btnNewButton_1.setBounds(694, 521, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(862, 521, 153, 39);
		contentPanel.add(btnCancel);
//		BillingDBConnection billingDBConnection=new BillingDBConnection();
//		billingDBConnection.closeConnection();
		
		batchNameCB = new JComboBox();
		batchNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					itemBatchNameSTR=batchNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (batchNameCB.getSelectedIndex() > -1) {
					batchIDSTR=batchID.get(batchNameCB.getSelectedIndex());
				}
				batchQtyTF.setText("");
				expiryDateTF.setText("");
				getItemBatch(batchIDSTR);

				if (itemBatchName.getSize() > 0) {

					int addDays=20;
					batchQtyTF.setText("" + batchQty);
					expiryDateTF.setText("" + expiryDateSTR);
					String untildate=DateFormatChange.StringToMysqlDate(new Date());  
					SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );   
					Calendar cal = Calendar.getInstance();    
					try {
						cal.setTime( (Date)dateFormat.parse(untildate));
					
					cal.add( Calendar.DATE, addDays );    
					String convertedDate=dateFormat.format(cal.getTime());    
					Date date1=(Date)dateFormat.parse(convertedDate);
					Date date2=(Date)dateFormat.parse(expiryDateSTR);
					 if(date1.compareTo(date2)<0)
					    expiryDateTF.setForeground(Color.BLACK);
					  else if(date1.compareTo(date2)>0)
						  expiryDateTF.setForeground(Color.RED);
					  else
						  expiryDateTF.setForeground(Color.BLACK);  
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}    
				}
			}
		});
		batchNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		batchNameCB.setBounds(311, 190, 164, 20);
		contentPanel.add(batchNameCB);
		
		JLabel lblSelectBatch = new JLabel("Select Batch");
		lblSelectBatch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectBatch.setBounds(317, 165, 92, 14);
		contentPanel.add(lblSelectBatch);
		
		JLabel lblSelectItem = new JLabel("Select Item");
		lblSelectItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectItem.setBounds(140, 165, 92, 14);
		contentPanel.add(lblSelectItem);
		
		batchQtyTF = new JTextField();
		batchQtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		batchQtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		batchQtyTF.setEditable(false);
		batchQtyTF.setColumns(10);
		batchQtyTF.setBounds(533, 220, 92, 20);
		contentPanel.add(batchQtyTF);
		
		JLabel lblBatchQtyIn = new JLabel("Batch Qty. in Hand");
		lblBatchQtyIn.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblBatchQtyIn.setBounds(417, 224, 119, 14);
		contentPanel.add(lblBatchQtyIn);
		
		taxTF = new JTextField();
		taxTF.setHorizontalAlignment(SwingConstants.RIGHT);
		taxTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		taxTF.setColumns(10);
		taxTF.setBounds(727, 190, 69, 20);
		contentPanel.add(taxTF);
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
		
		JLabel lblTax = new JLabel("CGST");
		lblTax.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTax.setBounds(746, 165, 69, 14);
		contentPanel.add(lblTax);
		
		discountTF = new JTextField();
		discountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		discountTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		discountTF.setColumns(10);
		discountTF.setBounds(704, 221, 119, 20);
		contentPanel.add(discountTF);
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
		
		JLabel lblDiscount = new JLabel("Discount");
		lblDiscount.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDiscount.setBounds(646, 224, 62, 14);
		contentPanel.add(lblDiscount);
		
		itemPriceTF = new JTextField();
		itemPriceTF.setHorizontalAlignment(SwingConstants.RIGHT);
		itemPriceTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemPriceTF.setEditable(false);
		itemPriceTF.setColumns(10);
		itemPriceTF.setBounds(92, 247, 106, 20);
		contentPanel.add(itemPriceTF);
		
		JLabel lblItemPrice = new JLabel("Item Price");
		lblItemPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemPrice.setBounds(14, 252, 78, 14);
		contentPanel.add(lblItemPrice);
		
		totalValueTF = new JTextField();
		totalValueTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalValueTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		totalValueTF.setEditable(false);
		totalValueTF.setColumns(10);
		totalValueTF.setBounds(317, 247, 92, 20);
		contentPanel.add(totalValueTF);
		
		JLabel lblTotalValue = new JLabel("Total Value");
		lblTotalValue.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblTotalValue.setBounds(239, 251, 68, 14);
		contentPanel.add(lblTotalValue);
		
		expiryDateTF = new JTextField();
		expiryDateTF.setHorizontalAlignment(SwingConstants.RIGHT);
		expiryDateTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		expiryDateTF.setEditable(false);
		expiryDateTF.setColumns(10);
		expiryDateTF.setBounds(746, 247, 138, 20);
		contentPanel.add(expiryDateTF);
		
		JLabel lblTotalPayablet = new JLabel("Total Amount :");
		lblTotalPayablet.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalPayablet.setBounds(20, 521, 98, 25);
		contentPanel.add(lblTotalPayablet);
		
		totalPayAmountTF = new JTextField();
		totalPayAmountTF.setHorizontalAlignment(SwingConstants.RIGHT);
		totalPayAmountTF.setEditable(false);
		totalPayAmountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		totalPayAmountTF.setColumns(10);
		totalPayAmountTF.setBounds(120, 521, 146, 25);
		contentPanel.add(totalPayAmountTF);
		
		JLabel lblItemLocation = new JLabel("Item Location");
		lblItemLocation.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblItemLocation.setBounds(443, 251, 92, 14);
		contentPanel.add(lblItemLocation);
		
		itemLocationTF = new JTextField();
		itemLocationTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		itemLocationTF.setEditable(false);
		itemLocationTF.setColumns(10);
		itemLocationTF.setBounds(532, 247, 138, 20);
		contentPanel.add(itemLocationTF);
		
		JLabel label = new JLabel("Search Supplier :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(10, 11, 126, 25);
		contentPanel.add(label);
		
		supplierTF = new JTextField();
		supplierTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierTF.setColumns(10);
		supplierTF.setBounds(146, 11, 218, 25);
		contentPanel.add(supplierTF);
		supplierTF.getDocument().addDocumentListener(
				new DocumentListener() {
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
					supplierDisplaySTR = supplierCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");
			
				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText(""+addressSTR);
					mobileTF.setText(""+mobileSTR);
				}
			}
		});
		supplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		supplierCB.setBounds(146, 44, 218, 25);
		contentPanel.add(supplierCB);
		
		JLabel label_1 = new JLabel("Select Supplier");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_1.setBounds(10, 44, 126, 25);
		contentPanel.add(label_1);
		
		JLabel label_2 = new JLabel("Mobile :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(10, 80, 126, 25);
		contentPanel.add(label_2);
		
		mobileTF = new JTextField();
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(146, 80, 218, 25);
		contentPanel.add(mobileTF);
		
		addressTF = new JTextField();
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(146, 116, 218, 25);
		contentPanel.add(addressTF);
		
		JLabel label_3 = new JLabel("Address :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(10, 116, 126, 25);
		contentPanel.add(label_3);
		
		JLabel label_4 = new JLabel("");
		label_4.setHorizontalAlignment(SwingConstants.CENTER);
//		label_4.setIcon(new ImageIcon(NewReturnInvoice.class.getResource("/icons/invoice_icon.jpg")));
		label_4.setIcon(new ImageIcon(NewReturnItemsForm.class.getResource("/icons/icon-returns.jpg")));
		label_4.setBounds(390, 4, 210, 137);
		contentPanel.add(label_4);
		
		JLabel label_5 = new JLabel("Return To Supplier");
		label_5.setHorizontalAlignment(SwingConstants.CENTER);
		label_5.setFont(new Font("Tahoma", Font.BOLD, 15));
		label_5.setBounds(610, 11, 345, 25);
		contentPanel.add(label_5);
		
		JLabel label_6 = new JLabel("Invoice No. :");
		label_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_6.setBounds(610, 44, 126, 25);
		contentPanel.add(label_6);
		
		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(746, 44, 218, 25);
		contentPanel.add(invoiceNoTF);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(746, 82, 219, 59);
		contentPanel.add(scrollPane_1);
		
		returnReasonTA = new JTextArea();
		scrollPane_1.setViewportView(returnReasonTA);
		returnReasonTA.setRows(5);
		
		JLabel lblReason = new JLabel("Reason  :");
		lblReason.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblReason.setBounds(610, 87, 126, 25);
		contentPanel.add(lblReason);
		
		surchargeTF = new JTextField();
		surchargeTF.setHorizontalAlignment(SwingConstants.RIGHT);
		surchargeTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		surchargeTF.setColumns(10);
		surchargeTF.setBounds(822, 190, 62, 20);
		contentPanel.add(surchargeTF);
		surchargeTF.addKeyListener(new KeyAdapter() {
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
		surchargeTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurchargeValue = 0;
					itemValue();
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurchargeValue = 0;
					itemValue();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = surchargeTF.getText() + "";
				if (!str.equals("")) {

					itemSurchargeValue = Double.parseDouble("0" + str);
					itemValue();

				} else {

					itemSurchargeValue = 0;
					itemValue();
				}
			}
		});
		
		JLabel lblSurChg = new JLabel("SGST");
		lblSurChg.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSurChg.setBounds(822, 165, 62, 14);
		contentPanel.add(lblSurChg);
		
		JLabel label_7 = new JLabel("Cutting Qty");
		label_7.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_7.setBounds(563, 165, 78, 14);
		contentPanel.add(label_7);
		
		cuttingqtyTF = new JTextField();
		cuttingqtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		cuttingqtyTF.setColumns(10);
		cuttingqtyTF.setBounds(563, 190, 68, 20);
		contentPanel.add(cuttingqtyTF);
		
		JLabel label_8 = new JLabel("Damage Qty");
		label_8.setFont(new Font("Tahoma", Font.BOLD, 11));
		label_8.setBounds(644, 165, 78, 14);
		contentPanel.add(label_8);
		
		damageqtyTF = new JTextField();
		damageqtyTF.setHorizontalAlignment(SwingConstants.RIGHT);
		damageqtyTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		damageqtyTF.setColumns(10);
		damageqtyTF.setBounds(646, 190, 68, 20);
		contentPanel.add(damageqtyTF);
		
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
	public void getItemBatchName(String index) {

		BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
		ResultSet resultSet = batchTrackingDBConnection.itemBatch(index);
		itemBatchName.removeAllElements();
		batchID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				batchID.add(resultSet.getObject(1).toString());
				itemBatchName.addElement(resultSet.getObject(2).toString());
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

	public void getItemDetail(String index) {

		GeneralDBConnection formula=new GeneralDBConnection();
		double value=Double.parseDouble(formula.retrieveFormula1());
		formula.closeConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail2(index);
		double mrp = 0;
		double purchase = 0,tot=0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				taxValue= Double.parseDouble(resultSet.getObject(6).toString());
				purchase = Double.parseDouble(resultSet.getObject(7).toString());
				quantity= Integer.parseInt(resultSet.getObject(8).toString());
				price = Double.parseDouble(resultSet.getObject(10).toString());
				
				price=purchase;
				itemLocationSTR=resultSet.getObject(12).toString();
				itemSurchargeValue=Double.parseDouble(resultSet.getObject(13).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}
	public void getItemBatch(String index) {

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

	}
public void getSupplierName(String index) {
		
		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		supplierName.removeAllElements();
		int i=0;
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
		if (i>0) {
			supplierCB.setSelectedIndex(0);
		}
		
	}
	public void getSupplierDetail(String index) {
		
		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		try {
			while (resultSet.next()) {
				
				supplierID=resultSet.getObject(1).toString();
				supplierNameSTR=resultSet.getObject(2).toString();
				mobileSTR=(resultSet.getObject(4).toString());
				addressSTR=(resultSet.getObject(6).toString()+", "+resultSet.getObject(7).toString()+", "+resultSet.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}
	
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();
		finalTotalValue=0;
		totalTaxAmount=0;
		ObjectArray_ListOfexamsSpecs = new Object[size][11];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemBatchV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = taxPercentageV.get(i)+"%("+taxAmountV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][6] = surchargeV.get(i)+"%("+surchargeAmountValueV.get(i)+")";
			ObjectArray_ListOfexamsSpecs[i][7] = DiscountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = expiryDateV.get(i);
			finalTotalValue=finalTotalValue+Double.parseDouble(totalValueV.get(i));
			totalTaxAmount=totalTaxAmount+(Double.parseDouble(taxAmountV.get(i))+Double.parseDouble(surchargeAmountValueV.get(i)));
			
		}
		table.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch",
						"Unit Price", "Quantity", "tax","Surcharge","Discount","Total Value","expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(75);
		table.getColumnModel().getColumn(4).setPreferredWidth(75);
		table.getColumnModel().getColumn(4).setMinWidth(75);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
	
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		finalTotalValue= Math.round(finalTotalValue *  100.000) /  100.000;
		totalTaxAmount= Math.round(totalTaxAmount *  100.000) /  100.000;
		totalPayAmountTF.setText(finalTotalValue+"");
	}

	public void itemValue() {
		itemValue = qtyIssued * price;

		itemValue = itemValue - discountValue;
		double k = itemValue * (taxValue / 100.0f);
		k = Math.round(k * 100.000) / 100.000;
		
		double s = itemValue * (itemSurchargeValue/ 100.0f);
		s = Math.round(s * 100.000) / 100.000;
		taxAmountValue=k;
		surchargeAmountValue=s;
		itemValue = itemValue + k;
		itemValue = itemValue + s;

		itemValue = Math.round(itemValue * 100.000) / 100.000;

		totalValueTF.setText("" + itemValue);

	}
}
