
package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.pricemaster.database.PriceMasterDBConnection;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;

public class RequestItem extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTB;
	private JTextField minQtyTB;
	private JTextField maxQtyTB;
	private JTextField currentStockLB;
	String dept_name="";
	String minQty,maxQty;
	private JTextField requestTF;
	private JTable table;

	
	/**
	 * Create the dialog.
	 */
	public RequestItem(final DepartmentStock departmentStock,final String itemID,String itemName,String currentStock ) {
//		System.out.println("stockaccess"+stockaccess);
		setResizable(false);
		dept_name=DepartmentMain.departmentName;
		getItemDetail(itemID,dept_name);
		setTitle("Edit Item");
		setIconImage(Toolkit.getDefaultToolkit().getImage(Edititem.class.getResource("/icons/rotaryLogo.png")));
		setBounds(200, 200, 352, 443);
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
		
		currentStockLB = new JTextField(currentStock);
		currentStockLB.setEditable(false);
		currentStockLB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		currentStockLB.setColumns(10);
		currentStockLB.setBounds(126, 49, 198, 30);
		contentPanel.add(currentStockLB);
		
		JLabel lblRequestedQty = new JLabel("Requested Qty. :");
		lblRequestedQty.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRequestedQty.setBounds(10, 187, 106, 22);
		contentPanel.add(lblRequestedQty);
		
		requestTF = new JTextField((String) null);
		requestTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		requestTF.setColumns(10);
		requestTF.setBounds(126, 189, 198, 22);
		contentPanel.add(requestTF);
		
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 229, 330, 128);
		contentPanel.add(scrollPane);
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Item Batch","Expiry","Qty"
			}
		));
		table.setBounds(0, 0, 1, 1);
		scrollPane.setViewportView(table);
		
		getTableDATA(itemID);
		
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
//						if(maxQtyTB.getText().toString().equals(""))
//						{
//							JOptionPane.showMessageDialog(null, "Please Enter Maximum Qty.",
//									"Input Error", JOptionPane.ERROR_MESSAGE);
//							return;
//						}
						if(Double.parseDouble(requestTF.getText().toString())>Double.parseDouble(maxQtyTB.getText().toString()))
						{
							JOptionPane.showMessageDialog(null, "Request Qty Greater than Maximum Qty",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						DepartmentStockDBConnection connection=new DepartmentStockDBConnection();
						String[] data = new String[30];
						data[0] =itemID;
						data[1] = itemNameTB.getText().toString();
						data[2] = dept_name;
						data[3] = requestTF.getText().toString();
						data[4] = "Pending";
						data[5] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[6]=DepartmentMain.userName;
						try {
							connection.insertRequestItem(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
//					
//						try {
//							connection.updateMinMaxQty(itemID,currentStockLB.getText().toString(), minQtyTB.getText().toString(), maxQtyTB.getText().toString(), departmentName);
//						} catch (Exception e1) {
//							// TODO Auto-generated catch block
//							e1.printStackTrace();
//						}
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
	private void getTableDATA(String index) {
		// TODO Auto-generated method stub
		
		try {
			DepartmentStockDBConnection db = new DepartmentStockDBConnection();
			ResultSet rs = db.retrieveBatchDATA(index,dept_name);
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs = db.retrieveBatchDATA(index,dept_name);
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Item Batch","Expiry","Qty"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
		
	}
}
