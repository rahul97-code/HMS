
package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;

public class IssuedItem extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTB;
	private JTextField minQtyTB;
	private JTextField maxQtyTB;
	private JTextField currentStockLB;
	String dept_name="";
	String minQty,maxQty,totalStock;
	private JTextField requestTF;
	private JTextField IssuedTF;

	/**
	 * Create the dialog.
	 */
	public IssuedItem(final IssuedReport issuedReport,final String id,final String itemID,String itemName,String requestedqty,String department_name ) {
//		System.out.println("stockaccess"+stockaccess);
		setResizable(false);
		dept_name=department_name;
		getItemDetail(itemID,dept_name);
		getItemDetailStock(itemID);
		setTitle("Edit Item");
		setIconImage(Toolkit.getDefaultToolkit().getImage(IssuedItem.class.getResource("/icons/rotaryLogo.png")));
		setBounds(200, 200, 379, 354);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("Item Name :");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblNewLabel.setBounds(10, 16, 106, 22);
			contentPanel.add(lblNewLabel);
		}

		itemNameTB = new JTextField(itemName);
		itemNameTB.setEditable(false);
		itemNameTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		itemNameTB.setBounds(126, 11, 198, 30);
		contentPanel.add(itemNameTB);
		itemNameTB.setColumns(10);

		JLabel lblDetail = new JLabel("Minimum Qty. :");
		lblDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDetail.setBounds(10, 94, 106, 22);
		contentPanel.add(lblDetail);

		minQtyTB = new JTextField(minQty);
		minQtyTB.setEditable(false);
		minQtyTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minQtyTB.setBounds(126, 90, 198, 30);
		contentPanel.add(minQtyTB);
		minQtyTB.setColumns(10);

		JLabel lblRatePercentage = new JLabel("Maximum Qty. :");
		lblRatePercentage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRatePercentage.setBounds(10, 139, 106, 22);
		contentPanel.add(lblRatePercentage);

		maxQtyTB = new JTextField(maxQty);
		maxQtyTB.setEditable(false);
		maxQtyTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		maxQtyTB.setBounds(126, 131, 198, 30);
		contentPanel.add(maxQtyTB);
		maxQtyTB.setColumns(10);
//		 if(minimum_max_qty_access.equals("0")){
//			 minQtyTB.setEnabled(false);
//			 maxQtyTB.setEnabled(false);
//		 }
		JLabel lblCurrentStock = new JLabel("Current Stock :");
		lblCurrentStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCurrentStock.setBounds(10, 53, 106, 22);
		contentPanel.add(lblCurrentStock);
		
		currentStockLB = new JTextField(totalStock);
		currentStockLB.setEditable(false);
		currentStockLB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		currentStockLB.setColumns(10);
		currentStockLB.setBounds(126, 49, 198, 30);
		contentPanel.add(currentStockLB);
		
		JLabel lblRequestedQty = new JLabel("Requested Qty. :");
		lblRequestedQty.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRequestedQty.setBounds(10, 187, 106, 22);
		contentPanel.add(lblRequestedQty);
		
		requestTF = new JTextField(requestedqty);
		requestTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		requestTF.setColumns(10);
		requestTF.setEditable(false);
		requestTF.setBounds(126, 189, 198, 30);
		contentPanel.add(requestTF);
		
		JLabel lblIssuedQty = new JLabel("Issued Qty. :");
		lblIssuedQty.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblIssuedQty.setBounds(10, 239, 106, 22);
		contentPanel.add(lblIssuedQty);
		
		IssuedTF = new JTextField((String) null);
		IssuedTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		IssuedTF.setColumns(10);
		IssuedTF.setBounds(126, 241, 198, 30);
		contentPanel.add(IssuedTF);
//		if(stockaccess.equals("0")){
//			currentStockLB.setEditable(false);
//		}else{
//			currentStockLB.setEditable(true);
//		}
		
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
//						if(itemNameTB.getText().toString().equals(""))
//						{
//							JOptionPane.showMessageDialog(null, "Please enter name",
//									"Input Error", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
//						if(currentStockLB.getText().toString().equals(""))
//						{
//							JOptionPane.showMessageDialog(null, "Please Enter Current Stock",
//									"Input Error", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
//						if(minQtyTB.getText().toString().equals(""))
//						{
//							JOptionPane.showMessageDialog(null, "Please Enter Minimum Qty.",
//									"Input Error", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
						if(IssuedTF.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Issue Qty.",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
//						if(Double.parseDouble(requestTF.getText().toString())>Double.parseDouble(maxQtyTB.getText().toString()))
//						{
//							JOptionPane.showMessageDialog(null, "Request Qty Greater than Maximum Qty",
//									"Input Error", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
						if(Double.parseDouble(IssuedTF.getText().toString())>Double.parseDouble(requestTF.getText().toString()))
						{
							JOptionPane.showMessageDialog(null, "Issue Qty Greater than Request Qty",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						DepartmentStockDBConnection connection=new DepartmentStockDBConnection();
						String[] data = new String[30];
						data[0] =itemID;
						data[1] = itemNameTB.getText().toString();
						data[2] = dept_name;
						data[3] = IssuedTF.getText().toString();
						data[4] = "Issued";
						data[5] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[6]=StoreMain.userName;
						if(Double.parseDouble(IssuedTF.getText().toString())==Double.parseDouble(requestTF.getText().toString()))
						{
							data[4] = "Issued";
						}
						if(Double.parseDouble(IssuedTF.getText().toString())<Double.parseDouble(requestTF.getText().toString()))
						{
							data[4] = "Partially Issued";
						}
//					
						try {
							connection.updateIssuedItem(itemID,data[4], data[6],data[3], data[2],id );
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						try {
							connection.addStock(itemID, data[3], data[2]);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						ItemsDBConnection itemconnection=new ItemsDBConnection();
						try {
							itemconnection.subtractStock(itemID,data[3]);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
//						connection.closeConnection();
							JOptionPane.showMessageDialog(null, "Updated Successfully",
									"Edit Data", JOptionPane.INFORMATION_MESSAGE);
							dispose();
//							departmentStock.populateExpensesTable(departmentName);
						
					}
				});
				okButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	}
	public JTextField getCurrentStockLB() {
		return currentStockLB;
	}
	public void getItemDetail(String index,String dept_name) {

		DepartmentStockDBConnection db = new DepartmentStockDBConnection();
		ResultSet rs = db.retrieveminmax(index,dept_name);
		
		int i = 0;
		try {
			while (rs.next()) {

				minQty = rs.getObject(1).toString();
				maxQty=rs.getObject(2).toString();
			}
		}catch (Exception e) {
				// TODO: handle exception
			}
		System.out.println(minQty+"minnn");
		
	}
	public void getItemDetailStock(String Itemid) {

		InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
		ResultSet rs = invoiceDBConnection.retrieverequestedItemStock(Itemid);

		
		int i = 0;
		try {
			while (rs.next()) {

				totalStock = rs.getObject(1).toString();
//				maxQty=rs.getObject(2).toString();
			}
		}catch (Exception e) {
				// TODO: handle exception
			}
		
	}
}
