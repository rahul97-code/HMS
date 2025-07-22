package hms.store.gui;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import hms.departments.gui.DepartmentMain;
import hms.main.DateFormatChange;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.ItemsDBConnection;
import com.toedter.calendar.JDateChooser;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;

public class AddBatch extends JDialog {
	private final JPanel contentPanel = new JPanel();

	private JTextField stockTF;
	private JTextField unitTF;
	private JTextField itemTaxTF;
	private JTextField surchrgeTF;
	private JTextField measTF;
	private JTextField mrpTF;
	private JTextField batchTF;
	private JTextField itmnametf;
	String[] data = new String[25];
	String Recidate, date = "",billDate="";
	private JTextField itmdsrtf;
	private JTextField itmcodetf;
	private JTextField hsncodetf;
	private JTextField itemidtf;
	private JTextField itmbrandtf;
	private JTextField ctgrytf;
	private JPanel panel;
	private double tax;
	private double suchrge;
	private double Tf, Sf;
	private double unit;
	private JDateChooser ExpiryDate;
	private JTextField remarkTF;
	boolean flag = false;

	private JButton Save;

	private JButton Update;
	private JTextField changeUnitpriceTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
	}

	public void value() {
		tax = Double.parseDouble(itemTaxTF.getText());
		unit = Double.parseDouble(unitTF.getText());
		Tf = unit * (tax / 100.0f);
		Tf = Math.round(Tf * 100.000) / 100.000;

	}

	public void value1() {

		suchrge = Double.parseDouble(surchrgeTF.getText());
		unit = Double.parseDouble(unitTF.getText());
		Sf = unit * (suchrge / 100.0f);
		Sf = Math.round(Sf * 100.000) / 100.000;

	}

	public void get_data(String itemId, boolean b) {
		ItemsDBConnection db = new ItemsDBConnection();
		ResultSet rs = db.getdata(itemId);
		try {
			while (rs.next()) {
				itemidtf.setText(rs.getObject(1).toString());
				itmnametf.setText(rs.getObject(2).toString());
				itmdsrtf.setText(rs.getObject(3).toString());
				itmcodetf.setText(rs.getObject(4).toString());
				hsncodetf.setText(rs.getObject(5).toString());
				itmbrandtf.setText(rs.getObject(6).toString());
				ctgrytf.setText(rs.getObject(7).toString());

				if (!b) {
					itemTaxTF.setText(rs.getObject(8).toString());
					surchrgeTF.setText(rs.getObject(9).toString());
					measTF.setText(rs.getObject(10).toString());
					mrpTF.setText(rs.getObject(11).toString());
					unitTF.setText(rs.getObject(12).toString());
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	public AddBatch(final String itemid, final BatchBrowser batchBrowser, String Prestock, String remark, String exp,
			String unitprice, String mrp, String tax1, String sur, String measunit, String batch, boolean b,
			final String id,String changeUnitPrice,String bill_date) {
		System.out.println(itemid);

		setModal(true);
		setTitle("ADD BATCH");
		setBounds(100, 100, 511, 484);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		date = DateFormatChange.StringToMysqlDate(new Date());
		JLabel lblNewLabel = new JLabel("Expiry Date");
		lblNewLabel.setBounds(38, 233, 94, 15);
		getContentPane().add(lblNewLabel);

		stockTF = new JTextField();
		stockTF.setBounds(130, 259, 129, 19);
		getContentPane().add(stockTF);
		stockTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		stockTF.setColumns(10);
		stockTF.setText(Prestock);

		JLabel lblNewLabel_1 = new JLabel("Enter Stock");
		lblNewLabel_1.setBounds(42, 261, 94, 15);
		getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("Unit Price");
		lblNewLabel_1_1.setBounds(42, 292, 70, 15);
		getContentPane().add(lblNewLabel_1_1);

		unitTF = new JTextField();
		unitTF.setColumns(10);
		unitTF.setBounds(130, 290, 129, 19);
		getContentPane().add(unitTF);
		unitTF.setText(unitprice);

		mrpTF = new JTextField();
		mrpTF.setColumns(10);
		mrpTF.setBounds(130, 325, 129, 19);
		getContentPane().add(mrpTF);
		mrpTF.setText(mrp);

		JLabel lblNewLabel_1_1_2 = new JLabel("Item Mrp");
		lblNewLabel_1_1_2.setBounds(42, 327, 70, 15);
		getContentPane().add(lblNewLabel_1_1_2);

		JLabel lblItemId = new JLabel("Item Id");
		lblItemId.setBounds(42, 49, 70, 15);
		getContentPane().add(lblItemId);

		itmnametf = new JTextField();
		itmnametf.setEditable(false);
		itmnametf.setColumns(10);
		itmnametf.setBounds(348, 47, 101, 19);
		getContentPane().add(itmnametf);

		itmdsrtf = new JTextField();
		itmdsrtf.setEditable(false);
		itmdsrtf.setColumns(10);
		itmdsrtf.setBounds(140, 78, 309, 19);
		getContentPane().add(itmdsrtf);

		JLabel lblNewLabel_1_2 = new JLabel("Item Description");
		lblNewLabel_1_2.setBounds(38, 80, 94, 15);
		getContentPane().add(lblNewLabel_1_2);

		JLabel lblNewLabel_1_1_3 = new JLabel("Item Code");
		lblNewLabel_1_1_3.setBounds(42, 111, 90, 15);
		getContentPane().add(lblNewLabel_1_1_3);

		itmcodetf = new JTextField();
		itmcodetf.setEditable(false);
		itmcodetf.setColumns(10);
		itmcodetf.setBounds(141, 109, 101, 19);
		getContentPane().add(itmcodetf);

		JLabel lblNewLabel_1_1_1_2_2 = new JLabel("HSN Code");
		lblNewLabel_1_1_1_2_2.setBounds(260, 109, 70, 15);
		getContentPane().add(lblNewLabel_1_1_1_2_2);

		hsncodetf = new JTextField();
		hsncodetf.setEditable(false);
		hsncodetf.setColumns(10);
		hsncodetf.setBounds(348, 109, 101, 19);
		getContentPane().add(hsncodetf);

		itemidtf = new JTextField();
		itemidtf.setEditable(false);
		itemidtf.setColumns(10);
		itemidtf.setBounds(141, 47, 101, 19);
		getContentPane().add(itemidtf);

		JLabel lblNewLabel_1_1_1_3 = new JLabel("Item Name");
		lblNewLabel_1_1_1_3.setBounds(260, 49, 88, 15);
		getContentPane().add(lblNewLabel_1_1_1_3);

		JLabel lblNewLabel_1_1_3_1 = new JLabel("Item Brand");
		lblNewLabel_1_1_3_1.setBounds(42, 144, 90, 15);
		getContentPane().add(lblNewLabel_1_1_3_1);

		itmbrandtf = new JTextField();
		itmbrandtf.setEditable(false);
		itmbrandtf.setColumns(10);
		itmbrandtf.setBounds(141, 142, 101, 19);
		getContentPane().add(itmbrandtf);

		ctgrytf = new JTextField();
		ctgrytf.setEditable(false);
		ctgrytf.setColumns(10);
		ctgrytf.setBounds(348, 142, 101, 19);
		getContentPane().add(ctgrytf);

		JLabel lblNewLabel_1_1_1_2_2_1 = new JLabel("Category");
		lblNewLabel_1_1_1_2_2_1.setBounds(260, 142, 70, 15);
		getContentPane().add(lblNewLabel_1_1_1_2_2_1);

		panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Item Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(25, 12, 446, 165);
		getContentPane().add(panel);

		ExpiryDate = new JDateChooser();
		ExpiryDate.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {

					Recidate = DateFormatChange.StringToMysqlDate((Date) arg0.getNewValue());
					//System.out.print(Recidate + "rahulrrrrrrr");
				}
			}
		});
		ExpiryDate.setBounds(130, 230, 135, 19);
		getContentPane().add(ExpiryDate);
		if (!exp.equals("")) {
			Date date1 = null;
			try {
				date1 = new SimpleDateFormat("yyyy-MM-dd").parse(exp);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			ExpiryDate.setDate(date1);
		}
		Save = new JButton("Save");
		Save.setEnabled(false);
		Save.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				value();
				value1();
				if (itemidtf.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Item Id", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (batchTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Item Batch", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (itemTaxTF.getText().toString().equals("") || surchrgeTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Tax Value", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (stockTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Stocks", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (remarkTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Remark", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (changeUnitpriceTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please insert change unit price", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String m = "";
				if (mrpTF.getText().toString() != "" && !flag) {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(AddBatch.this,
							"Do you want to change the mrp price." + "\n" + " Current MRP is "
									+ mrpTF.getText().toString() + " Rupees",
							"Cancel", dialogButton);
					if (dialogResult == 0) {
						mrpTF.setText(JOptionPane.showInputDialog("Update MRP Price", mrpTF.getText().toString()));
						flag = true;
					}
				}
				Date date1;
				date1 = ExpiryDate.getDate();
				if (date1 == null) {
					JOptionPane.showMessageDialog(null, "Choose Date from Right Box.", "Error",
							JOptionPane.ERROR_MESSAGE);
					ExpiryDate.grabFocus();
					return;
				}
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				ItemsDBConnection ItemsDBConnection = new ItemsDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				data[0] = itemidtf.getText().toString(); // dept id
				data[1] = itmnametf.getText().toString(); // dept name
				data[2] = itmdsrtf.getText().toString(); // user id
				data[3] = batchTF.getText().toString(); // user name
				data[4] = stockTF.getText().toString(); // user id
				data[5] = stockTF.getText().toString();// user name
				data[6] = Recidate;
				data[7] = date;
				data[8] = "" + timeFormat.format(cal1.getTime());
				data[9] = date;
				data[10] = unitTF.getText().toString();
				data[11] = mrpTF.getText().toString();
				data[12] = measTF.getText().toString();
				data[13] = itemTaxTF.getText().toString();
				data[14] = surchrgeTF.getText().toString();
				data[15] = "" + Tf;
				data[16] = "" + Sf; // ipd no
				data[17] = unitTF.getText().toString();
				data[18] = StoreMain.userName;
				data[19] = remarkTF.getText() + "";
				data[20] = changeUnitpriceTF.getText().toString();
				data[21]=billDate;
				
				try {
					int a = JOptionPane.showConfirmDialog(null, "Are you sure to Store These Details In Data");
					if (a == JOptionPane.YES_OPTION) {

						batchTrackingDBConnection.insertbatch(data);
						ItemsDBConnection.AddStock(itemidtf.getText(), stockTF.getText().toString());

						JOptionPane.showMessageDialog(null, "Data Successfully Updated", "Success",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} else {
						setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				batchBrowser.populateExpensesTable(itemidtf.getText().toString());
				batchTrackingDBConnection.closeConnection();
				ItemsDBConnection.closeConnection();
			}
		});
		Save.setBounds(194, 393, 117, 25);
		getContentPane().add(Save);

		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setBounds(348, 393, 117, 25);
		getContentPane().add(btnNewButton_1);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Batch Details", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(25, 179, 456, 263);
		getContentPane().add(panel_1);
		panel_1.setLayout(null);

		remarkTF = new JTextField();
		remarkTF.setBounds(343, 24, 101, 19);
		remarkTF.setColumns(10);
		panel_1.add(remarkTF);
		remarkTF.setText(remark);

		JLabel lblNewLabel_1_1_1_1_1 = new JLabel("Remark :");
		lblNewLabel_1_1_1_1_1.setBounds(272, 26, 69, 15);
		panel_1.add(lblNewLabel_1_1_1_1_1);

		Update = new JButton("Update");
		Update.setEnabled(false);
		Update.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				value();
				value1();
				ItemsDBConnection db = new ItemsDBConnection();
				if (itemidtf.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Item Id", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (batchTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Item Batch", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (itemTaxTF.getText().toString().equals("") || surchrgeTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Tax Value", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (stockTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Stocks", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if (remarkTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please Enter Remark", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				String m = "";
				if (mrpTF.getText().toString() != "" && !flag) {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(AddBatch.this,
							"Do you want to change the mrp price." + "\n" + " Current MRP is "
									+ mrpTF.getText().toString() + " Rupees",
							"Cancel", dialogButton);
					if (dialogResult == 0) {
						mrpTF.setText(JOptionPane.showInputDialog("Update MRP Price", mrpTF.getText().toString()));
						flag = true;
					}
				}
				Date date1;
				date1 = ExpiryDate.getDate();
				if (date1 == null) {
					JOptionPane.showMessageDialog(null, "Choose Date from Right Box.", "Error",
							JOptionPane.ERROR_MESSAGE);
					ExpiryDate.grabFocus();
					return;
				}

				String[] data = new String[15];

				data[0] = batchTF.getText().toString();
				data[1] = stockTF.getText().toString();
				data[2] = Recidate;
				data[3] = unitTF.getText().toString();
				data[4] = mrpTF.getText().toString();
				data[5] = measTF.getText().toString();
				data[6] = itemTaxTF.getText().toString();
				data[7] = surchrgeTF.getText().toString();
				data[8] = "" + Tf;
				data[9] = "" + Sf;
				data[10] = remarkTF.getText() + "";
				data[11] = changeUnitpriceTF.getText()+"";
				data[12] = billDate;

				try {

					db.UpdateBatch(data, id);

				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Data updated successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				batchBrowser.populateExpensesTable(itemid + "");
				db.closeConnection();
				dispose();
			}
		});
		Update.setBounds(12, 215, 117, 25);
		panel_1.add(Update);
		
		JLabel lblNewLabel_1_1_4 = new JLabel("Change U.P.");
		lblNewLabel_1_1_4.setBounds(12, 26, 94, 15);
		panel_1.add(lblNewLabel_1_1_4);
		
		changeUnitpriceTF = new JTextField();
		changeUnitpriceTF.setText("<dynamic>");
		changeUnitpriceTF.setColumns(10);
		changeUnitpriceTF.setBounds(108, 24, 129, 19);
		changeUnitpriceTF.setText(changeUnitPrice);
		panel_1.add(changeUnitpriceTF);
		
		JDateChooser bill_dateChooser = new JDateChooser();
		bill_dateChooser.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {
					billDate = DateFormatChange.StringToMysqlDate((Date) arg0.getNewValue());
				}
			}
		});
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Date d=null;
		if (!bill_date.equals("")) {
			try {
				d = format.parse(bill_date);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		}else {
			d=new Date();
		}
		bill_dateChooser.setDate(d);
		bill_dateChooser.setBounds(104, 184, 133, 19);
		bill_dateChooser.setDateFormatString("yyyy-MM-dd");
		panel_1.add(bill_dateChooser);
		
		JLabel lblNewLabel_2 = new JLabel("Bill Date");
		lblNewLabel_2.setBounds(12, 187, 94, 15);
		panel_1.add(lblNewLabel_2);
		
				JLabel lblNewLabel_1_1_1 = new JLabel("Item Tax %");
				lblNewLabel_1_1_1.setBounds(257, 55, 84, 15);
				panel_1.add(lblNewLabel_1_1_1);
				
						itemTaxTF = new JTextField();
						itemTaxTF.setBounds(343, 53, 101, 19);
						panel_1.add(itemTaxTF);
						itemTaxTF.setColumns(10);
						itemTaxTF.addKeyListener(new KeyAdapter() {
							@Override
							public void keyTyped(KeyEvent e) {
								char vChar = e.getKeyChar();
								if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
									e.consume();
								}
							}
						});
						itemTaxTF.setText(tax1);
						
								surchrgeTF = new JTextField();
								surchrgeTF.setBounds(343, 84, 101, 19);
								panel_1.add(surchrgeTF);
								surchrgeTF.setColumns(10);
								surchrgeTF.addKeyListener(new KeyAdapter() {
									@Override
									public void keyTyped(KeyEvent e) {
										char vChar = e.getKeyChar();
										if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
											e.consume();
										}
									}
								});
								surchrgeTF.setText(sur);
								
										measTF = new JTextField();
										measTF.setBounds(343, 115, 101, 19);
										panel_1.add(measTF);
										measTF.setColumns(10);
										measTF.addKeyListener(new KeyAdapter() {
											@Override
											public void keyTyped(KeyEvent e) {
												char vChar = e.getKeyChar();
												if (!(Character.isDigit(vChar) || (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
													e.consume();
												}
											}
										});
										measTF.setText(measunit);
										
												batchTF = new JTextField();
												batchTF.setBounds(343, 146, 101, 19);
												panel_1.add(batchTF);
												batchTF.setColumns(10);
												batchTF.setText(batch);
												
														JLabel lblNewLabel_1_1_1_2_1 = new JLabel("Item Batch");
														lblNewLabel_1_1_1_2_1.setBounds(257, 148, 84, 15);
														panel_1.add(lblNewLabel_1_1_1_2_1);
														
																JLabel lblNewLabel_1_1_1_2 = new JLabel("Meas Unit");
																lblNewLabel_1_1_1_2.setBounds(261, 117, 80, 15);
																panel_1.add(lblNewLabel_1_1_1_2);
																
																		JLabel lblNewLabel_1_1_1_1 = new JLabel("Itm Surchrg%");
																		lblNewLabel_1_1_1_1.setBounds(240, 82, 101, 19);
																		panel_1.add(lblNewLabel_1_1_1_1);
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			Update.setEnabled(true);
			Save.setEnabled(true);
		} else {
			Update.setEnabled(false);
			Save.setEnabled(false);
		}
		if (b) {
			Update.setEnabled(true);
			Save.setEnabled(false);
		} else {
			Update.setEnabled(false);
			Save.setEnabled(true);
		}

		get_data(itemid, b);

	}
}
