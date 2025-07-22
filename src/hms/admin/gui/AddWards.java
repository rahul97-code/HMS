package hms.admin.gui;

import hms.departments.database.DepartmentDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
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
import javax.swing.SwingConstants;
import javax.swing.JComboBox;

public class AddWards extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField BuildingTB;
	private JTextField wardName;
	private JTextField roomNoTB;
	private JTextField inchargeNameTB;
	private JTextField fromBedTB;
	private JTextField toBedTB;
	private JComboBox deptNameCB;
	final DefaultComboBoxModel DeptNames = new DefaultComboBoxModel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddWards dialog = new AddWards();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddWards() {
		setBounds(100, 100, 452, 389);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblBuildingName = new JLabel("Building Name");
		lblBuildingName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuildingName.setBounds(29, 106, 96, 14);
		contentPanel.add(lblBuildingName);
		
		BuildingTB = new JTextField();
		BuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		BuildingTB.setBounds(145, 102, 227, 24);
		contentPanel.add(BuildingTB);
		BuildingTB.setColumns(10);
		
		wardName = new JTextField();
		wardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		wardName.setColumns(10);
		wardName.setBounds(145, 131, 227, 24);
		contentPanel.add(wardName);
		
		JLabel lblWardName = new JLabel("Ward Name");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(29, 135, 96, 14);
		contentPanel.add(lblWardName);
		
		roomNoTB = new JTextField();
		roomNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		roomNoTB.setColumns(10);
		roomNoTB.setBounds(145, 166, 227, 24);
		contentPanel.add(roomNoTB);
		
		JLabel lblRoomNo = new JLabel("Room No");
		lblRoomNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRoomNo.setBounds(29, 170, 96, 14);
		contentPanel.add(lblRoomNo);
		
		inchargeNameTB = new JTextField();
		inchargeNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		inchargeNameTB.setColumns(10);
		inchargeNameTB.setBounds(145, 201, 227, 24);
		contentPanel.add(inchargeNameTB);
		
		JLabel lblInchargeName = new JLabel("Incharge Name");
		lblInchargeName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInchargeName.setBounds(29, 205, 96, 14);
		contentPanel.add(lblInchargeName);
		
		fromBedTB = new JTextField();
		fromBedTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		fromBedTB.setColumns(10);
		fromBedTB.setBounds(145, 248, 227, 24);
		contentPanel.add(fromBedTB);
		
		JLabel lblBedNoFrom = new JLabel("Bed No From :");
		lblBedNoFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNoFrom.setBounds(29, 252, 96, 14);
		contentPanel.add(lblBedNoFrom);
		
		toBedTB = new JTextField();
		toBedTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		toBedTB.setColumns(10);
		toBedTB.setBounds(145, 283, 227, 24);
		contentPanel.add(toBedTB);
		
		JLabel lblBedNoTo = new JLabel("Bed No. To :");
		lblBedNoTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNoTo.setBounds(29, 287, 96, 14);
		contentPanel.add(lblBedNoTo);
		
		JLabel lblHmsWards = new JLabel("HMS Wards");
		lblHmsWards.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblHmsWards.setHorizontalAlignment(SwingConstants.CENTER);
		lblHmsWards.setBounds(50, 11, 299, 31);
		contentPanel.add(lblHmsWards);
		
		JLabel lblWardType = new JLabel("Ward Type");
		lblWardType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardType.setBounds(29, 70, 96, 14);
		contentPanel.add(lblWardType);
		
		deptNameCB = new JComboBox(new Object[]{});
		deptNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		deptNameCB.setBounds(145, 67, 227, 24);
		contentPanel.add(deptNameCB);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent arg0) {
						
						
						if(BuildingTB.getText().equals("")||wardName.getText().equals("")||roomNoTB.getText().equals("")||
								inchargeNameTB.getText().equals("")||fromBedTB.getText().equals("")||toBedTB.getText().equals(""))
						{
							JOptionPane.showMessageDialog(null,
									"Please enter values ", "Input Error",
									JOptionPane.ERROR_MESSAGE);
						}
						else{
							WardsManagementDBConnection wardsManagementDBConnection=new WardsManagementDBConnection();
							String[] data=new String[10];
							
							data[0]=BuildingTB.getText();
							data[1]=wardName.getText();
							data[2]=roomNoTB.getText();
							data[3]=inchargeNameTB.getText();
							data[4]=deptNameCB.getSelectedItem().toString();
							for (int i = Integer.parseInt(fromBedTB.getText()); i <=Integer.parseInt(toBedTB.getText()); i++) {
								data[5]=i+"";
								
								try {
									wardsManagementDBConnection.inserData(data);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									wardsManagementDBConnection.closeConnection();
								}
							}
							wardsManagementDBConnection.closeConnection();
							JOptionPane.showMessageDialog(null,
									"Data Saved Successfully ", "Data Save",
									JOptionPane.INFORMATION_MESSAGE);
							dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		getAllDept();
	}
	public void getAllDept() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWardType();
		DeptNames.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				DeptNames.addElement(resultSet.getObject(1).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		deptNameCB.setModel(DeptNames);
		if (i > 0) {
			deptNameCB.setSelectedIndex(0);
		}
		
	}
}
