package hms.admin.gui;

import hms.admin.gui.AdminMain;
import hms.departments.database.DepartmentDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.main.DateFormatChange;
import hms1.wards.database.WardsManagementDBConnection;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;

import com.toedter.calendar.JDateChooser;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JScrollPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import lu.tudor.santec.jtimechooser.JTimeChooser;

public class WardCharges extends JFrame {
	private JTable table;
	private JComboBox wardTypeCB;
	

	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	ArrayList<String> doctor_id = new ArrayList<String>();
	
	ArrayList<String> entry_data = new ArrayList<String>();

	String doctorNameSTR, doctorIdSTR;
	private JButton btnRemove;
	private JTextField onlineChargesTF;
	private JTextField textField;


	public static void main(String[] arg) {
		new WardCharges().setVisible(true);
	}

	public WardCharges() {
		 setResizable(false);
		setTitle("Ward Charges");
		String frequency[] = { "Every", "Every 1st", "Every 2nd","Every 3rd","Every 4th","Every 5th" };
		String Days[] = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };
		setBounds(10, 21, 522, 421);
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Select Ward Type :");
		lblName.setBounds(10, 21, 141, 24);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		getContentPane().add(lblName);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 138, 506, 9);
		getContentPane().add(separator);

		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			//	JOptionPane.showMessageDialog(DoctorAvailability.this,endTimeTP.getFormatedTime().toString());
				
				if(onlineChargesTF.getText().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter values ", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else{
					WardsManagementDBConnection wardsManagementDBConnection=new WardsManagementDBConnection();
					String[] data=new String[10];
					
					data[0]=doctorNameSTR;
					data[1]=onlineChargesTF.getText();

							
						try {
							wardsManagementDBConnection.inserWardChargesData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							wardsManagementDBConnection.closeConnection();
						}
				
					wardsManagementDBConnection.closeConnection();
					JOptionPane.showMessageDialog(null,
							"Data Saved Successfully ", "Data Save",
							JOptionPane.INFORMATION_MESSAGE);
				}
				LoadTable();
				
			}

			private void elseif(boolean equals) {
				// TODO Auto-generated method stub
				
			}
		});
		btnNewButton.setBounds(10, 158, 132, 36);
		getContentPane().add(btnNewButton);

		btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
//
//				doctor_name.remove(cur_selectedRow);
//				doctor_id1.remove(cur_selectedRow);
//				freq_days.remove(cur_selectedRow);
//				frequencyType.remove(cur_selectedRow);
//				time_from.remove(cur_selectedRow);
//				time_to.remove(cur_selectedRow);
//				duration.remove(cur_selectedRow);
//				appointments.remove(cur_selectedRow);
//				charges.remove(cur_selectedRow);
//				
				
				WardsManagementDBConnection connection=new WardsManagementDBConnection();
				try {
					connection.deleteWardCharges(toDelete);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connection.closeConnection();

				LoadTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setEnabled(false);
		btnRemove.setBounds(152, 158, 132, 36);
		getContentPane().add(btnRemove);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 205, 496, 177);
		getContentPane().add(scrollPane);

		
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Ward Type","Charges"}));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		wardTypeCB = new JComboBox(new Object[] {});
		wardTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doctorNameSTR = wardTypeCB.getSelectedItem().toString();
					doctorIdSTR = doctor_id.get(wardTypeCB.getSelectedIndex())
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				LoadTable();
			}
		});
		wardTypeCB.setBounds(181, 23, 207, 24);
		getContentPane().add(wardTypeCB);
		
		onlineChargesTF = new JTextField();
		onlineChargesTF.setColumns(10);
		onlineChargesTF.setBounds(181, 103, 207, 20);
		getContentPane().add(onlineChargesTF);
		
		JLabel lblOnlineCharges = new JLabel("Charges :");
		lblOnlineCharges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOnlineCharges.setBounds(10, 103, 141, 24);
		getContentPane().add(lblOnlineCharges);
		
		JLabel lblTime = new JLabel("Time :");
		lblTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTime.setBounds(10, 68, 141, 24);
		getContentPane().add(lblTime);
		
		textField = new JTextField();
		textField.setToolTipText("1-24");
		textField.setColumns(10);
		textField.setBounds(181, 68, 207, 20);
		getContentPane().add(textField);
		
		JLabel lblHourse = new JLabel("1- 24 Hours");
		lblHourse.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblHourse.setBounds(389, 68, 141, 24);
		getContentPane().add(lblHourse);
	

		getAllDept();
		
	}
	public void getAllDept() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWardType();
		doctors.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(1).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		wardTypeCB.setModel(doctors);
		if (i > 0) {
			wardTypeCB.setSelectedIndex(0);
		}
		
	}
	

	public void LoadTable()
	{
	
		
		try {

			WardsManagementDBConnection wardsManagementDBConnection=new WardsManagementDBConnection();
			
			ResultSet rs=wardsManagementDBConnection.retrieveAllWardCharges(doctorNameSTR);
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
		
			rs=wardsManagementDBConnection.retrieveAllWardCharges(doctorNameSTR);

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
					 new String[] { "ID", "Ward Type","Time(Hours)","Charges" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			
			wardsManagementDBConnection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
	}
	public JTextField getOnlineChargesTF() {
		return onlineChargesTF;
	}
}
