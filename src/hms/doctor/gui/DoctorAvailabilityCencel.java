package hms.doctor.gui;

import hms.admin.gui.AdminMain;
import hms.doctor.database.DoctorDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.main.DateFormatChange;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class DoctorAvailabilityCencel  extends JFrame {
	private JTextField reasonTF;
	private JTable table;
	private JButton removeBTN;
	private JButton addBTN;
	private JDateChooser cancleDateFromDC;
	private JLabel idLBL;
	private JComboBox doctorNameCB;
	String doctorNameSTR, doctorIdSTR,cancelFromDate="",cancelToDate="";
	
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	ArrayList<String> doctor_id = new ArrayList<String>();
	ArrayList<String> entry_data = new ArrayList<String>();
	private JLabel lblDateTo;
	private JDateChooser cancelDateToDC;
	
	public static void main(String[] arg) {
		new DoctorAvailabilityCencel().setVisible(true);
	}
	
	public DoctorAvailabilityCencel() {
		setTitle("Cancel Doctor Availability");
		getContentPane().setLayout(null);
		setBounds(10,10,731,483);
		JLabel label = new JLabel("Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(20, 24, 141, 24);
		getContentPane().add(label);
		
		doctorNameCB = new JComboBox(new Object[]{});
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doctorNameSTR = doctorNameCB.getSelectedItem().toString();
					doctorIdSTR = doctor_id.get(doctorNameCB.getSelectedIndex())
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				idLBL.setText(doctorIdSTR);
				LoadTable();
			}
		});
		
		doctorNameCB.setBounds(191, 26, 146, 24);
		getContentPane().add(doctorNameCB);
		
		JLabel label_1 = new JLabel("ID :");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label_1.setBounds(20, 56, 141, 24);
		getContentPane().add(label_1);
		
		idLBL = new JLabel("Doctor id");
		idLBL.setFont(new Font("Tahoma", Font.PLAIN, 16));
		idLBL.setBounds(191, 56, 146, 24);
		getContentPane().add(idLBL);
		
		JLabel lblDate = new JLabel("Date From :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDate.setBounds(20, 91, 141, 24);
		getContentPane().add(lblDate);
		
		cancleDateFromDC = new JDateChooser();
		cancleDateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							cancelFromDate = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		cancleDateFromDC.setDateFormatString("yyyy-MM-dd");
		cancleDateFromDC.setBounds(191, 91, 146, 25);
		getContentPane().add(cancleDateFromDC);
		
//		cancleDateDC.setDate(new Date());
//		cancleDateDC.setMaxSelectableDate(new Date());
//		cancleDateDC.setDateFormatString("yyyy-MM-dd");
		
		reasonTF = new JTextField();
		reasonTF.setColumns(10);
		reasonTF.setBounds(191, 127, 146, 23);
		getContentPane().add(reasonTF);
		
		JLabel lblReasonFor = new JLabel("Reason for cancellation :");
		lblReasonFor.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblReasonFor.setBounds(8, 126, 183, 24);
		getContentPane().add(lblReasonFor);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(0, 173, 728, 13);
		getContentPane().add(separator);
		
		addBTN = new JButton("Add"); 
		addBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				entry_data.clear();
				 if(cancelFromDate.equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter From Date");
					 return;
	                }
				 if(cancelToDate.equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter To Date");
					 return;
	                }
				 if(reasonTF.getText().toString().equals(""))
	                {
					 JOptionPane.showMessageDialog(null, "Enter Reason");
					 return;
	                }
				 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			      String time = sdf.format(new Date());
				entry_data.add(doctorIdSTR);
				entry_data.add(doctorNameSTR);
				entry_data.add(cancelFromDate);
				entry_data.add(cancelToDate);
				entry_data.add(AdminMain.username);
				entry_data.add(reasonTF.getText().toString());
				entry_data.add(time);
				DoctorDBConnection connection=new DoctorDBConnection();
				
				
					try {
						connection.inserAvailabiltyCancelData(entry_data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
			
				connection.closeConnection();
				
				 LoadTable();
				
			}
		});
		addBTN.setBounds(181, 197, 132, 36);
		getContentPane().add(addBTN);
		
		removeBTN = new JButton("Remove");
		removeBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

//				doctor_name.remove(cur_selectedRow);
//				doctor_id1.remove(cur_selectedRow);
//				freq_days.remove(cur_selectedRow);
//				frequencyType.remove(cur_selectedRow);
//				time_from.remove(cur_selectedRow);
//				time_to.remove(cur_selectedRow);
//				duration.remove(cur_selectedRow);
//				appointments.remove(cur_selectedRow);
//				charges.remove(cur_selectedRow);				
				
				DoctorDBConnection connection=new DoctorDBConnection();
				
					try {
						connection.deleteDoctorAvailabilityCancel(toDelete);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				
				connection.closeConnection();

				LoadTable();
				removeBTN.setEnabled(false);
			}
		});
		removeBTN.setEnabled(false);
		removeBTN.setBounds(482, 197, 132, 36);
		getContentPane().add(removeBTN);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(30, 244, 678, 187);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"Enrty ID", "ID","Name", "From Date","To Date", "Reason"
			}
		));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						removeBTN.setEnabled(true);
					}
				}); 
		scrollPane.setViewportView(table);
		
		lblDateTo = new JLabel("Date To :");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDateTo.setBounds(352, 90, 89, 24);
		getContentPane().add(lblDateTo);
		
		cancelDateToDC = new JDateChooser();
		cancelDateToDC.setDateFormatString("yyyy-MM-dd");
		cancelDateToDC.setBounds(451, 91, 146, 25);
		cancelDateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							cancelToDate = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		getContentPane().add(cancelDateToDC);
		
		 getAllDoctors();
	}
	
	
	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData2();
		doctor_id.clear();
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
				doctor_id.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
	}
	
	
	
	public void LoadTable()
	{
		try {

			DoctorDBConnection connection=new DoctorDBConnection();
			
			ResultSet rs=connection.retrieveCancelDoctorAvailability(doctorNameSTR);
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs=connection.retrieveCancelDoctorAvailability(doctorNameSTR);

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
					  new String[] {
								"Enrty ID", "ID","Name", "From Date","To Date", "Reason"
						}) {
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
	public JDateChooser getCancelDateToDC() {
		return cancelDateToDC;
	}
}
