package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.StoreMain;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

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
import javax.swing.JCheckBox;

public class Edititem extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTB;
	private JTextField minQtyTB;
	private JTextField maxQtyTB;
	private JTextField currentStockLB;

	
	/**
	 * Create the dialog.
	 */
	public Edititem(final DepartmentItemProfile departmentItemProfile,final String itemId,String itemName,String currentStock,String minQty,String maxQty,final String departmentName,final String stockaccess,final String minimum_max_qty_access,final String ID ) {
		System.out.println("stockaccess"+stockaccess);
		setResizable(false);
		setTitle("Edit Item");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Edititem.class.getResource("/icons/rotaryLogo.png")));
		setBounds(200, 200, 352, 277);
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
		minQtyTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		minQtyTB.setBounds(126, 90, 198, 30);
		contentPanel.add(minQtyTB);
		minQtyTB.setColumns(10);

		JLabel lblRatePercentage = new JLabel("Maximum Qty. :");
		lblRatePercentage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRatePercentage.setBounds(10, 139, 106, 22);
		contentPanel.add(lblRatePercentage);

		maxQtyTB = new JTextField(maxQty);
		maxQtyTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		maxQtyTB.setBounds(126, 131, 198, 30);
		contentPanel.add(maxQtyTB);
		maxQtyTB.setColumns(10);
		 
		JLabel lblCurrentStock = new JLabel("Current Stock :");
		lblCurrentStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCurrentStock.setBounds(10, 53, 106, 22);
		contentPanel.add(lblCurrentStock);
		
		currentStockLB = new JTextField(currentStock);
		currentStockLB.setEditable(false);
		currentStockLB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		currentStockLB.setColumns(10);
		currentStockLB.setBounds(126, 49, 198, 30);
		contentPanel.add(currentStockLB);
		
		JLabel lblItemStatus = new JLabel("Item Status:");
		lblItemStatus.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblItemStatus.setBounds(10, 181, 106, 22);
		contentPanel.add(lblItemStatus);
		
		final JCheckBox chckbxNewCheckBox = new JCheckBox("Inactive");
		chckbxNewCheckBox.setBounds(126, 182, 97, 23);
		contentPanel.add(chckbxNewCheckBox);
		if(minimum_max_qty_access.equals("0")){
			 minQtyTB.setEnabled(false);
			 maxQtyTB.setEnabled(false);
			
		 }
		 if(!flag(itemId) && (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1"))){
				
				currentStockLB.setEditable(true);
			}else{
				currentStockLB.setEditable(false);
			}
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Save");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(itemNameTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please enter name",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(currentStockLB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Current Stock",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
//						boolean checked = chckbxNewCheckBox.getState();
//						if(chckbxNewCheckBox.getState()) {
//						  //c1 is checked
//						} else if (c2.getState()) {
//						  //
//						}
						String item_status="";
					if (chckbxNewCheckBox.isSelected() == true){
						item_status="0";
					}else{
						item_status="1";
					}
						if(minQtyTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Minimum Qty.",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(maxQtyTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Maximum Qty.",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						DepartmentStockDBConnection connection=new DepartmentStockDBConnection();
						try {
							connection.updateMinMaxQty1(ID,currentStockLB.getText().toString(), minQtyTB.getText().toString(), maxQtyTB.getText().toString(), departmentName,item_status);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						connection.closeConnection();
							JOptionPane.showMessageDialog(null, "Updated Successfully",
									"Edit Data", JOptionPane.INFORMATION_MESSAGE);
							dispose();
							departmentItemProfile.populateExpensesTable(departmentName,"1");
						
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
	private boolean flag(String itemID) {
		// TODO Auto-generated method stub
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet rs=itemsDBConnection.CheckBatchReqd(itemID);
		boolean i=false;
		try {
			while(rs.next()) {
				i=rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
	    return i;
	}
	public JTextField getCurrentStockLB() {
		return currentStockLB;
	}
}
