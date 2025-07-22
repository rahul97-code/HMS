package hms.admin.gui;

import hms.insurance.gui.InsuranceDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;

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

public class AddOPDtype extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTB;
	private JTextField charges1TB;
	private JTextField charges2TB;
	private JTextField charges3TB;
	private JTextField charges4TB;
	private JComboBox selectInsuranceCB;

	final DefaultComboBoxModel insuranceType = new DefaultComboBoxModel();
	String insuranceSTR;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddOPDtype dialog = new AddOPDtype();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddOPDtype() {
		setResizable(false);
		setTitle("Add OPD Type");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddOPDtype.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 352, 432);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblNewLabel = new JLabel("OPD Name :");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblNewLabel.setBounds(10, 41, 106, 22);
			contentPanel.add(lblNewLabel);
		}

		nameTB = new JTextField();
		nameTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameTB.setBounds(126, 36, 198, 30);
		contentPanel.add(nameTB);
		nameTB.setColumns(10);

		JLabel lblDetail = new JLabel("Charge 1 :");
		lblDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDetail.setBounds(10, 85, 86, 22);
		contentPanel.add(lblDetail);

		charges1TB = new JTextField();
		charges1TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		charges1TB.setBounds(126, 81, 198, 30);
		contentPanel.add(charges1TB);
		charges1TB.setColumns(10);

		JLabel lblRatePercentage = new JLabel("Charge 2 :");
		lblRatePercentage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRatePercentage.setBounds(10, 136, 106, 22);
		contentPanel.add(lblRatePercentage);

		charges2TB = new JTextField();
		charges2TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		charges2TB.setBounds(126, 128, 198, 30);
		contentPanel.add(charges2TB);
		charges2TB.setColumns(10);

		JLabel lblRateType = new JLabel("Charge 3 :");
		lblRateType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRateType.setBounds(10, 183, 106, 22);
		contentPanel.add(lblRateType);

		charges3TB = new JTextField();
		charges3TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		charges3TB.setBounds(126, 179, 198, 30);
		contentPanel.add(charges3TB);

		JLabel lblAddInsurance = new JLabel("Add OPD Type");
		lblAddInsurance.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddInsurance.setBounds(100, 0, 127, 25);
		contentPanel.add(lblAddInsurance);
		
		charges4TB = new JTextField();
		charges4TB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		charges4TB.setBounds(126, 220, 198, 30);
		contentPanel.add(charges4TB);
		
		JLabel lblCharge = new JLabel("Charge 4 :");
		lblCharge.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblCharge.setBounds(10, 224, 106, 22);
		contentPanel.add(lblCharge);
		
		JLabel lblSelectInsurance = new JLabel("Select Insurance :");
		lblSelectInsurance.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSelectInsurance.setBounds(10, 275, 106, 22);
		contentPanel.add(lblSelectInsurance);
		
		selectInsuranceCB = new JComboBox();
		selectInsuranceCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		selectInsuranceCB.setBounds(126, 271, 198, 30);

		contentPanel.add(selectInsuranceCB);
		selectInsuranceCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					insuranceSTR = selectInsuranceCB.getSelectedItem().toString();
					
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if(nameTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please enter name",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else if(charges1TB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please Charges 1",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else{
							String data[] =new String[10];
							
							data[0]=nameTB.getText().toString();
							data[1]=charges1TB.getText().toString();
							data[2]=charges2TB.getText().toString();
							data[3]=charges3TB.getText().toString();
							data[4]=charges4TB.getText().toString();
							
							data[5]="00";
							data[6]=insuranceSTR;
							
							
							PriceMasterDBConnection priceMasterDBConnection=new PriceMasterDBConnection();
							try {
								priceMasterDBConnection.inserData(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							priceMasterDBConnection.closeConnection();
							JOptionPane.showMessageDialog(null, "New OPD Type Added Successfully",
									"OPD Type save", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
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
		getAllinsurance();
	}
	
	public void getAllinsurance() {
		insuranceType.addElement("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceType.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		selectInsuranceCB.setModel(insuranceType);
		selectInsuranceCB.setSelectedIndex(0);
	}
	
}
