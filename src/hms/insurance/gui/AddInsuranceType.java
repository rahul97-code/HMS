package hms.insurance.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class AddInsuranceType extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTB;
	private JTextField detailTB;
	private JTextField percentageTB;
	private JComboBox rateTypeCB;
	private JButton btnNewButton;
	public int global;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddInsuranceType dialog = new AddInsuranceType();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddInsuranceType() {
		setBounds(100, 100, 350, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setModal(true);
		{
			JLabel lblNewLabel = new JLabel("Insuran Name :");
			lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblNewLabel.setBounds(10, 41, 106, 22);
			contentPanel.add(lblNewLabel);
		}

		nameTB = new JTextField();
		nameTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		nameTB.setBounds(126, 36, 198, 30);
		contentPanel.add(nameTB);
		nameTB.setColumns(10);

		JLabel lblDetail = new JLabel("Detail :");
		lblDetail.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDetail.setBounds(10, 85, 86, 22);
		contentPanel.add(lblDetail);

		detailTB = new JTextField();
		detailTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		detailTB.setBounds(126, 81, 198, 30);
		contentPanel.add(detailTB);
		detailTB.setColumns(10);

		JLabel lblRatePercentage = new JLabel("Rate Percentage :");
		lblRatePercentage.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRatePercentage.setBounds(10, 136, 106, 22);
		contentPanel.add(lblRatePercentage);

		percentageTB = new JTextField();
		percentageTB.setText("0");
		percentageTB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		percentageTB.setBounds(126, 128, 198, 30);
		contentPanel.add(percentageTB);
		percentageTB.setColumns(10);

		JLabel lblRateType = new JLabel("Rate Type :");
		lblRateType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblRateType.setBounds(10, 183, 106, 22);
		contentPanel.add(lblRateType);

		rateTypeCB = new JComboBox();
		rateTypeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				global=rateTypeCB.getSelectedIndex()+1;
			}
		});
		rateTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rateTypeCB.setModel(new DefaultComboBoxModel(new String[] {
				"Rate Type 1", "Rate Type 2", "Rate Type 3", "Rate Type 4" }));
		rateTypeCB.setBounds(126, 179, 198, 30);
		contentPanel.add(rateTypeCB);

		JLabel lblAddInsurance = new JLabel("Add Insurance");
		lblAddInsurance.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddInsurance.setBounds(100, 0, 127, 25);
		contentPanel.add(lblAddInsurance);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					

					@Override
					public void actionPerformed(ActionEvent e) {
						global=0;
						int ss=rateTypeCB.getSelectedIndex()+1;
						 global=rateTypeCB.getSelectedIndex()+1;
						boolean bool = false;
						InsuranceDBConnection dbConnection=new InsuranceDBConnection();
					
						boolean resultSet1=dbConnection.checkingtable(ss);
						
						
						
						

							if(!resultSet1 && ss!=1) {
							JOptionPane.showMessageDialog(null, "Please Create table first of rate type",
									"Input Error", JOptionPane.ERROR_MESSAGE);
							btnNewButton.setEnabled(true);
							return;
							}
							
						
						if(nameTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please enter name",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else if(detailTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please enter detail",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else if(percentageTB.getText().toString().equals(""))
						{
							JOptionPane.showMessageDialog(null, "Please enter percentage",
									"Input Error", JOptionPane.ERROR_MESSAGE);
						}else{
							 ss=rateTypeCB.getSelectedIndex()+1;
							String data[] =new String[10];
							
							data[0]=nameTB.getText().toString();
							data[1]=detailTB.getText().toString();
							data[2]=percentageTB.getText().toString();
							data[3]=ss+"";
							
							
							//InsuranceDBConnection dbConnection=new InsuranceDBConnection();
							try {
								dbConnection.inserData(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							dbConnection.closeConnection();
							JOptionPane.showMessageDialog(null, "New Inurance Type Add Successfully",
									"Insurance save", JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
					}
				});
				
				 btnNewButton = new JButton("Create Table");
				btnNewButton.setEnabled(false);
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						InsuranceDBConnection dbConnection=new InsuranceDBConnection();
						// String name=JOptionPane.showInputDialog(null,"Enter RateType Integer value");  
						 dbConnection.createtable(global);
						 
						 
					}
				});
				buttonPane.add(btnNewButton);
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
}
