package hms.store.gui;

import hms.store.database.ChallanDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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

public class SearchChallan extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField supplierTF;
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
	Vector<String> surchargeV = new Vector<String>();
	Vector<String> surchargeValueV = new Vector<String>();
	Vector<String> discountV = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR,challanNoSTR;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,itemSurchargeSTR,
			expiryDateSTR = "", invoiceDateSTR = "", dueDateSTR = "0000-00-00";
	double unitPrice = 0, oldunitPrice = 0, taxValue = 0,itemSurcharge = 0, discountValue = 0,
			itemValue, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0,surchargeAmountValue=0,taxAmountValue=0;
	int quantity = 0,freeQuantity=0,paidQuantity=0;
	String mrp = "";
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JTextField finalTotalTF;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField challanDate;
	NewInvoice newInvoice=null;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			SearchChallan dialog = new SearchChallan("3");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public SearchChallan(String supplier_id) {
		setResizable(false);
		supplierID=supplier_id;
		setBounds(100, 70, 1014, 478);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Enter Challan No");
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
					searchChallan(str,supplierID);
				} else {
					itemIDV.clear();
					itemNameV.clear();
					itemDescV.clear();
					measUnitV.clear();
					qtyV.clear();
					freeqtyV.clear();
					paidqtyV.clear();
					unitPriceV.clear();
					taxV.clear();
					surchargeV.clear();
					taxValueV.clear();
					surchargeValueV.clear();
					discountV.clear();
					totalValueV.clear();
					expiryDateV.clear();
					batchNumberV.clear();
					challanDate.setText("");
					loadDataToTable();

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					searchChallan(str,supplierID);
				} else {
					itemIDV.clear();
					itemNameV.clear();
					itemDescV.clear();
					measUnitV.clear();
					qtyV.clear();
					freeqtyV.clear();
					paidqtyV.clear();
					unitPriceV.clear();
					taxV.clear();
					surchargeV.clear();
					taxValueV.clear();
					surchargeValueV.clear();
					discountV.clear();
					totalValueV.clear();
					expiryDateV.clear();
					batchNumberV.clear();
					challanDate.setText("");
					loadDataToTable();
				}
			}
			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = supplierTF.getText() + "";
				if (!str.equals("")) {
					searchChallan(str,supplierID);
				} else {
					itemIDV.clear();
					itemNameV.clear();
					itemDescV.clear();
					measUnitV.clear();
					qtyV.clear();
					freeqtyV.clear();
					paidqtyV.clear();
					unitPriceV.clear();
					taxV.clear();
					surchargeV.clear();
					taxValueV.clear();
					surchargeValueV.clear();
					discountV.clear();
					totalValueV.clear();
					expiryDateV.clear();
					batchNumberV.clear();
					challanDate.setText("");
					loadDataToTable();
				}
			}
		});

		JLabel lblDueDate = new JLabel("Date :");
		lblDueDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDueDate.setBounds(543, 11, 126, 25);
		contentPanel.add(lblDueDate);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 102, 992, 287);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {
		}, new String[] { "Item ID", "Item Name", "Item Batch", "MU", "Qty.","Batch Qty.",
				"Unit Price", "Discount", "Tax", "Value", "Expiry" }));
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
						
					}
				});
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 80, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("ADD");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if(itemDescV.size()==0)
				{
					JOptionPane.showMessageDialog(null,
							"Please search Challan No. then add", "Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					if(newInvoice!=null)
					{
						
						newInvoice.searchChallan(challanNoSTR, supplierID);
						dispose();
					}
				}
				
				
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(20, 400, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(183, 400, 153, 39);
		contentPanel.add(btnCancel);

		JLabel lblTotal = new JLabel("Total :");
		lblTotal.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotal.setBounds(756, 419, 80, 14);
		contentPanel.add(lblTotal);

		finalTotalTF = new JTextField();
		finalTotalTF.setHorizontalAlignment(SwingConstants.RIGHT);
		finalTotalTF.setEditable(false);
		finalTotalTF.setFont(new Font("Tahoma", Font.BOLD, 14));
		finalTotalTF.setColumns(10);
		finalTotalTF.setBounds(836, 414, 162, 25);
		contentPanel.add(finalTotalTF);
		
		challanDate = new JTextField();
		challanDate.setHorizontalAlignment(SwingConstants.RIGHT);
		challanDate.setFont(new Font("Tahoma", Font.BOLD, 14));
		challanDate.setEditable(false);
		challanDate.setColumns(10);
		challanDate.setBounds(679, 11, 285, 25);
		contentPanel.add(challanDate);
	}

	public void searchChallan(String searchChallan,String supplier_id)
	{
		itemIDV.clear();
		itemNameV.clear();
		itemDescV.clear();
		measUnitV.clear();
		qtyV.clear();
		freeqtyV.clear();
		paidqtyV.clear();
		unitPriceV.clear();
		taxV.clear();
		surchargeV.clear();
		taxValueV.clear();
		surchargeValueV.clear();
		discountV.clear();
		totalValueV.clear();
		expiryDateV.clear();
		batchNumberV.clear();
		challanDate.setText("");
		ChallanDBConnection challanDBConnection = new ChallanDBConnection();
		ResultSet resultSet = challanDBConnection.retrieveChallan(searchChallan, supplier_id);
		
		try {
			while (resultSet.next()) {
				challanNoSTR=searchChallan;
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
				challanDate.setText(resultSet.getObject(16).toString());
				batchNumberV.add(resultSet.getObject(17).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		challanDBConnection.closeConnection();
		loadDataToTable();
		
	}	
	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][11];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = measUnitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = qtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = paidqtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = unitPriceV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = taxV.get(i)+"+"+surchargeV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = discountV.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = totalValueV.get(i);
			ObjectArray_ListOfexamsSpecs[i][10] = expiryDateV.get(i);
			total = total + Double.parseDouble(totalValueV.get(i));
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch.", "MU",
						"Qty.","Paid Qty.", "Unit Price","Tax+Surcharge", "Discount",  "Value",
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

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.RIGHT);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(8).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(9).setCellRenderer(centerRenderer);
		finalTotalValue = total;
		finalTotalTF.setText("" + finalTotalValue);
	}
	public void NewInvoiceInstance(NewInvoice invoice)
	{
		newInvoice=invoice;
	}
}
