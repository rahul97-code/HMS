package hms.insurance.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

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
import javax.swing.JCheckBox;
import javax.swing.border.BevelBorder;

public class AddInsuranceType extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField nameTB;
	private JTextField detailTB;
	private JTextField percentageTB;
	private JComboBox rateTypeCB;
	DefaultComboBoxModel rateModel=new DefaultComboBoxModel();
	DefaultComboBoxModel copyRateModel=new DefaultComboBoxModel();

	Vector<String> rateV=new Vector<>();
	Vector<String> copyRateV=new Vector<>();
	public int global;
	private JTextField RatyTF;
	private JComboBox rateTypeCB_1;

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
		setBounds(100, 100, 669, 252);
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
		lblRateType.setBounds(365, 41, 106, 22);
		contentPanel.add(lblRateType);

		rateTypeCB = new JComboBox();
		rateTypeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int index=rateTypeCB.getSelectedIndex();
				if(index==0) {
					rateTypeCB_1.setEnabled(true);
				}else {
					rateTypeCB_1.setEnabled(false);
				}
			}
		});
		rateTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		rateTypeCB.setBounds(452, 37, 198, 30);
		contentPanel.add(rateTypeCB);

		JLabel lblAddInsurance = new JLabel("Add Insurance");
		lblAddInsurance.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblAddInsurance.setBounds(100, 0, 127, 25);
		contentPanel.add(lblAddInsurance);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBounds(344, 85, 306, 88);
		contentPanel.add(panel);
		panel.setLayout(null);

		rateTypeCB_1 = new JComboBox();
		rateTypeCB_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		rateTypeCB_1.setBounds(95, 46, 198, 30);
		panel.add(rateTypeCB_1);
		rateTypeCB_1.setEnabled(false);

		JLabel lblCopy = new JLabel("Copy Of :");
		lblCopy.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblCopy.setBounds(8, 50, 106, 22);
		panel.add(lblCopy);

		RatyTF = new JTextField();
		RatyTF.setEditable(false);
		RatyTF.setBounds(132, 11, 54, 25);
		panel.add(RatyTF);
		RatyTF.setColumns(10);

		JLabel lblNewRateType = new JLabel("New Rate Type :");
		lblNewRateType.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblNewRateType.setBounds(8, 12, 106, 22);
		panel.add(lblNewRateType);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("SAVE");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						int index=rateTypeCB.getSelectedIndex();
						String rateTypeStr=RatyTF.getText().toString();
						String oldRateType=copyRateV.get(rateTypeCB_1.getSelectedIndex());
						String data[] =new String[10];

						InsuranceDBConnection dbConnection=new InsuranceDBConnection();	
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
							data[0]=nameTB.getText().toString();
							data[1]=detailTB.getText().toString();
							data[2]=percentageTB.getText().toString();
							data[3]=index==0?rateTypeStr:rateV.get(rateTypeCB.getSelectedIndex());;
							try {
								dbConnection.inserData(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							if(index==0) {
								if(dbConnection.checkingtable(Integer.parseInt(rateTypeStr))) {
									JOptionPane.showMessageDialog(null, "This RatyType Table is already exist.",
											"Input Error", JOptionPane.ERROR_MESSAGE);
									return;
								}
								int rateTypeStrInt=Integer.parseInt(rateTypeStr);
								int oldRateTypeInt=Integer.parseInt(oldRateType);
								dbConnection.createNewInsTable(rateTypeStrInt,oldRateTypeInt);
								dbConnection.copyInsTable(rateTypeStrInt, oldRateTypeInt);
							}

							dbConnection.closeConnection();
							JOptionPane.showMessageDialog(null, "New Inurance Type Add Successfully",
									"Insurance save", JOptionPane.INFORMATION_MESSAGE);
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
					public void actionPerformed(ActionEvent arg0 ) {
						dispose();
					}
				});
				cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 13));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
			getAllRateType();

		}
	}

	public void getAllRateType(){
		rateV.removeAllElements();
		rateModel.removeAllElements();
		copyRateV.removeAllElements();
		copyRateModel.removeAllElements();
		InsuranceDBConnection db=new InsuranceDBConnection();
		ResultSet rs=db.retrieveAllRateType();
		rateModel.addElement("NEW RATE TYPE");
		rateV.add("0");
		try {
			while(rs.next()) {
				copyRateModel.addElement("RATE TYPE "+rs.getString(1));
				rateModel.addElement("RATE TYPE "+rs.getString(1));
				rateV.add(rs.getString(1));
				copyRateV.add(rs.getString(1));
				RatyTF.setText(""+(rs.getInt(1)+1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		if(rateModel.getSize()>0) {
			rateTypeCB.setModel(rateModel);
			rateTypeCB_1.setModel(copyRateModel);
			rateTypeCB.setSelectedIndex(1);
		}
	}
}
