package hms.admin.gui;

import hms.admin.gui.AdminMain;
import hms.departments.database.DepartmentDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.main.DateFormatChange;

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

public class AddSalary extends JFrame {
	private JTextField employeeCodeTF;
	private JTextField employeeNameTF;
	private JTextField salaryTF;
	private JTextField employeeTypeTF;
	private JTable table;
	private JComboBox deptNameCB;
	

	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	ArrayList<String> doctor_id = new ArrayList<String>();
	
	ArrayList<String> entry_data = new ArrayList<String>();

	
	private JLabel lblDeptId;

	String deptNameSTR, deptIdSTR;
	private JButton btnRemove;


	public static void main(String[] arg) {
		new AddSalary().setVisible(true);
	}

	public AddSalary() {
		 setResizable(false);
		setTitle("Employee Salaries");

		setBounds(10, 21, 1019, 570);
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Department :");
		lblName.setBounds(10, 21, 141, 24);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		getContentPane().add(lblName);

		JLabel lblId = new JLabel("Department ID :");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblId.setBounds(10, 53, 141, 24);
		getContentPane().add(lblId);

		JLabel lblDurationPerPatient = new JLabel("Employee Code");
		lblDurationPerPatient.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDurationPerPatient.setBounds(10, 88, 158, 24);
		getContentPane().add(lblDurationPerPatient);

		employeeCodeTF = new JTextField();
		employeeCodeTF.setColumns(10);
		employeeCodeTF.setBounds(181, 92, 132, 20);
		getContentPane().add(employeeCodeTF);

		JLabel lblNoOfAppoinytments = new JLabel("Employee Name");
		lblNoOfAppoinytments.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNoOfAppoinytments.setBounds(10, 123, 158, 24);
		getContentPane().add(lblNoOfAppoinytments);

		employeeNameTF = new JTextField();
		employeeNameTF.setColumns(10);
		employeeNameTF.setBounds(181, 127, 132, 20);
		getContentPane().add(employeeNameTF);

		JLabel lblCharges = new JLabel("Employee Type");
		lblCharges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCharges.setBounds(10, 158, 141, 24);
		getContentPane().add(lblCharges);

		employeeTypeTF = new JTextField();
		employeeTypeTF.setColumns(10);
		employeeTypeTF.setBounds(181, 158, 132, 20);
		getContentPane().add(employeeTypeTF);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 263, 1029, 9);
		getContentPane().add(separator);

		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			//	JOptionPane.showMessageDialog(DoctorAvailability.this,endTimeTP.getFormatedTime().toString());
				

				
				 if(employeeCodeTF.getText().toString().equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter duration Per Patient");
					 return;
	                }
				 if(employeeNameTF.getText().toString().equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter No. Of Appointments");
					 return;
	                }
				 if(salaryTF.getText().toString().equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter Total No. Of Patient");
					 return;
	                }
				 if(employeeTypeTF.getText().toString().equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter charges");
					 return;
	                }
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			      String time = sdf.format(new Date());
				entry_data.clear();

				entry_data.add(employeeCodeTF.getText().toString());
				entry_data.add(employeeNameTF.getText().toString());
				entry_data.add(employeeTypeTF.getText().toString());
				entry_data.add(deptIdSTR);
				entry_data.add(deptNameSTR);
				entry_data.add(salaryTF.getText().toString());
				SalaryDBConnection connection=new SalaryDBConnection();
				
				
				try {
					connection.insertSalaryData(entry_data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				connection.closeConnection();
				
				LoadTable();
				
			}

			private void elseif(boolean equals) {
				// TODO Auto-generated method stub
				
			}
		});
		btnNewButton.setBounds(268, 274, 132, 36);
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
				
				SalaryDBConnection connection=new SalaryDBConnection();
				try {
					connection.deleteRow(toDelete);
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
		btnRemove.setBounds(569, 274, 132, 36);
		getContentPane().add(btnRemove);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 331, 992, 187);
		getContentPane().add(scrollPane);

		
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] { "Sal ID", "DepartMent ID","Dpartment Name", "Employee Code",
				"Employee Name",  "Employee Type", "Salary" }));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		deptNameCB = new JComboBox(new Object[] {});
		deptNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					deptNameSTR = deptNameCB.getSelectedItem().toString();
					getDepartmentsDetail(deptNameSTR);
				} catch (Exception e) {
					// TODO: handle exception

				}
				LoadTable();
			}
		});
		deptNameCB.setBounds(181, 23, 132, 24);
		getContentPane().add(deptNameCB);

		lblDeptId = new JLabel("Deptid");
		lblDeptId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDeptId.setBounds(181, 53, 132, 24);
		getContentPane().add(lblDeptId);
		
		JLabel lblTotalNumberOf = new JLabel("Salary");
		lblTotalNumberOf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTotalNumberOf.setBounds(10, 193, 158, 24);
		getContentPane().add(lblTotalNumberOf);
		
		salaryTF = new JTextField();
		salaryTF.setColumns(10);
		salaryTF.setBounds(181, 197, 132, 20);
		getContentPane().add(salaryTF);
	
//		table.getColumnModel().getColumn(4).setPreferredWidth(85);
//		table.getColumnModel().getColumn(7).setPreferredWidth(96);
//		table.getColumnModel().getColumn(8).setPreferredWidth(108);
		getAllDept();
		
	}
	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				deptIdSTR = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		lblDeptId.setText(deptIdSTR);

	}
	public void getAllDept() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		doctors.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		deptNameCB.setModel(doctors);
		if (i > 0) {
			deptNameCB.setSelectedIndex(0);
		}
		
	}

	public void LoadTable()
	{
	
		
		try {

			SalaryDBConnection connection=new SalaryDBConnection();
			
			ResultSet rs=connection.retrieveSalary(deptNameSTR);
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs=connection.retrieveSalary(deptNameSTR);

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
					 new String[] { "Sal ID", "Department ID","Dpartment Name", "Employee Code",
					"Employee Name",  "Employee Type", "Salary"  }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			
			connection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
	}
	
}
