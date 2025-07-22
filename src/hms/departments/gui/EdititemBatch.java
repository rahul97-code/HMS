package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.pricemaster.database.PriceMasterDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
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
import javax.swing.JCheckBox;
import com.toedter.calendar.JDateChooser;

public class EdititemBatch extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField itemNameTB;
	private JTextField batchNamTF;
	private JTextField currentStockLB;
	private JTextField batchIDTF;
	private JDateChooser expiryDateC;
	String expiryDateSTR="";

	public static void main(String ar[]) {
		EdititemBatch b=new EdititemBatch(null,"","","","","","","","");
		b.setVisible(true);
	}
	
	/**
	 * Create the dialog.
	 */
	public EdititemBatch(final DeptBatchBrowser deptBatchBrowser,String itemName,final String Id,String batchName,String batchID,String exp,final String stock,final String dept,final String itemID) {

		setResizable(false);
		setTitle("Edit Item");
		setIconImage(Toolkit.getDefaultToolkit().getImage(EdititemBatch.class.getResource("/icons/rotaryLogo.png")));
		setBounds(200, 200, 352, 323);
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

		JLabel lblDetail = new JLabel("Expiry :");
		lblDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDetail.setBounds(10, 94, 106, 22);
		contentPanel.add(lblDetail);

		JLabel lblRatePercentage = new JLabel("Batch Name :");
		lblRatePercentage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRatePercentage.setBounds(10, 139, 106, 22);
		contentPanel.add(lblRatePercentage);

		batchNamTF = new JTextField(batchName);
		batchNamTF.setFont(new Font("Tahoma", Font.PLAIN, 13));
		batchNamTF.setBounds(126, 131, 198, 30);
		contentPanel.add(batchNamTF);
		batchNamTF.setColumns(10);
		
		JLabel lblCurrentStock = new JLabel("Current Stock :");
		lblCurrentStock.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCurrentStock.setBounds(10, 53, 106, 22);
		contentPanel.add(lblCurrentStock);
		
		currentStockLB = new JTextField(stock);
		currentStockLB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		currentStockLB.setColumns(10);
		currentStockLB.setBounds(126, 49, 198, 30);
		contentPanel.add(currentStockLB);
		
		batchIDTF = new JTextField(batchID);
		batchIDTF.setFont(new Font("Dialog", Font.PLAIN, 13));
		batchIDTF.setColumns(10);
		batchIDTF.setEditable(true);
		batchIDTF.setBounds(126, 179, 198, 30);
		batchIDTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE)|| (vChar == '-'))) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		contentPanel.add(batchIDTF);
		
		
		JLabel lblBatchId = new JLabel("Batch ID :");
		lblBatchId.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblBatchId.setBounds(10, 182, 106, 22);
		contentPanel.add(lblBatchId);
		 
		Date date1=null;
	    try {
			 date1=new SimpleDateFormat("yyyy-MM-dd").parse(exp);
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}  
	    expiryDateSTR=exp;
		expiryDateC = new JDateChooser(date1);
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		expiryDateC.setBounds(126, 94, 158, 22);
		contentPanel.add(expiryDateC);
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
		//expiryDateC.setMinSelectableDate(new Date());
		
		
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
						if(expiryDateSTR.equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Expiry Date",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
					
						if(batchIDTF.getText().toString().equals("") || batchIDTF.getText().toString().equals("null"))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Batch ID",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						if(batchNamTF.getText().toString().equals("") || batchNamTF.getText().toString().equals("null"))
						{
							JOptionPane.showMessageDialog(null, "Please Enter Batch Name",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							return;
						}
						
						DepartmentStockDBConnection connection=new DepartmentStockDBConnection();
						try {
							connection.updateMinMaxQty(Id,currentStockLB.getText().toString(), batchIDTF.getText().toString(), batchNamTF.getText().toString(),expiryDateSTR);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						connection.closeConnection();
							JOptionPane.showMessageDialog(null, "Updated Successfully",
									"Edit Data", JOptionPane.INFORMATION_MESSAGE);
							
							deptBatchBrowser.populateExpensesTable(itemID,dept);
							dispose();
						
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
}
