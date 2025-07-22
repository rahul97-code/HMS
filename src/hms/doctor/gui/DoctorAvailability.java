package hms.doctor.gui;

import hms.admin.gui.AdminMain;
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
import java.text.ParseException;
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

public class DoctorAvailability extends JFrame {
	private JTextField durationPerPatientTF;
	private JTextField noOfAppointmentsTF;
	private JTextField totalPatientTF;
	private JTextField chargesTF;
	private JTable table;
	private JComboBox docNameCB;
	private JComboBox dailyFreqCB;
	private JComboBox weeklyFreqCB;

	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	ArrayList<String> doctor_id = new ArrayList<String>();

	ArrayList<String> entry_data = new ArrayList<String>();
	// ArrayList<String> doctor_id1 = new ArrayList<String>();
	// ArrayList<String> frequencyType = new ArrayList<String>();
	// ArrayList<String> freq_days = new ArrayList<String>();
	// ArrayList<String> time_from = new ArrayList<String>();
	// ArrayList<String> time_to = new ArrayList<String>();
	// ArrayList<String> duration = new ArrayList<String>();
	// ArrayList<String> appointments = new ArrayList<String>();
	// ArrayList<String> charges = new ArrayList<String>();

	private JLabel lblDoctorId;

	String doctorNameSTR, doctorIdSTR;
	private JTimeChooser startTimeTP;
	private JTimeChooser endTimeTP;
	private JButton btnRemove;
	private JTextField onlineChargesTF;
	private JButton btnEdit;
	private JTimeChooser opdStartTime;

	public static void main(String[] arg) {
		new DoctorAvailability().setVisible(true);
	}

	public DoctorAvailability() {
		setResizable(false);
		setTitle("Doctor Availability");
		String frequency[] = { "Every", "Every 1st", "Every 2nd", "Every 3rd",
				"Every 4th", "Every 5th" };
		String Days[] = { "Sunday", "Monday", "Tuesday", "Wednesday",
				"Thursday", "Friday", "Saturday" };
		setBounds(10, 21, 1019, 570);
		getContentPane().setLayout(null);

		JLabel lblName = new JLabel("Name :");
		lblName.setBounds(10, 21, 141, 24);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		getContentPane().add(lblName);

		JLabel lblId = new JLabel("ID :");
		lblId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblId.setBounds(10, 53, 141, 24);
		getContentPane().add(lblId);

		JLabel lblSelectAvailability = new JLabel("Select Availability :");
		lblSelectAvailability.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblSelectAvailability.setBounds(10, 88, 141, 24);
		getContentPane().add(lblSelectAvailability);

		dailyFreqCB = new JComboBox(frequency);
		dailyFreqCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int position = dailyFreqCB.getSelectedIndex();

			}
		});

		dailyFreqCB.setBounds(181, 90, 132, 24);
		getContentPane().add(dailyFreqCB);

		weeklyFreqCB = new JComboBox(Days);
		weeklyFreqCB.setBounds(340, 90, 178, 24);
		getContentPane().add(weeklyFreqCB);

		JLabel lblTimeFrom = new JLabel("Time From :");
		lblTimeFrom.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTimeFrom.setBounds(10, 123, 141, 24);
		getContentPane().add(lblTimeFrom);

		JLabel lblTimeTo = new JLabel("Time To :");
		lblTimeTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTimeTo.setBounds(340, 125, 85, 24);
		getContentPane().add(lblTimeTo);

		JLabel lblDurationPerPatient = new JLabel("Duration per patient :");
		lblDurationPerPatient.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDurationPerPatient.setBounds(10, 158, 158, 24);
		getContentPane().add(lblDurationPerPatient);

		durationPerPatientTF = new JTextField();
		durationPerPatientTF.setColumns(10);
		durationPerPatientTF.setBounds(181, 162, 132, 20);
		getContentPane().add(durationPerPatientTF);

		JLabel lblNoOfAppoinytments = new JLabel("No of appointments :");
		lblNoOfAppoinytments.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblNoOfAppoinytments.setBounds(10, 193, 158, 24);
		getContentPane().add(lblNoOfAppoinytments);

		noOfAppointmentsTF = new JTextField();
		noOfAppointmentsTF.setColumns(10);
		noOfAppointmentsTF.setBounds(181, 197, 132, 20);
		getContentPane().add(noOfAppointmentsTF);

		JLabel lblCharges = new JLabel("OPD Charges :");
		lblCharges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblCharges.setBounds(10, 228, 141, 24);
		getContentPane().add(lblCharges);

		chargesTF = new JTextField();
		chargesTF.setColumns(10);
		chargesTF.setBounds(181, 228, 132, 20);
		getContentPane().add(chargesTF);

		JSeparator separator = new JSeparator();
		separator.setBounds(0, 263, 1029, 9);
		getContentPane().add(separator);

		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				// JOptionPane.showMessageDialog(DoctorAvailability.this,endTimeTP.getFormatedTime().toString());

				if (startTimeTP.getFormatedTime().toString().equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter Time From");
					return;
				}
				if (endTimeTP.getFormatedTime().toString().equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter Time To");
					return;
				}
				if (opdStartTime.getFormatedTime().toString()
						.equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter OPD Start Time");
					return;
				}
				if (durationPerPatientTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter duration Per Patient");
					return;
				}
				if (noOfAppointmentsTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter No. Of Appointments");
					return;
				}
				if (totalPatientTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter Total No. Of Patient");
					return;
				}
				if (chargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter opd charges");
					return;
				}
				if (onlineChargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter online charges");
					return;
				}
				SimpleDateFormat sdf = new SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				String time = sdf.format(new Date());
				entry_data.clear();
				entry_data.add(doctorIdSTR);
				entry_data.add(doctorNameSTR);
				entry_data.add(dailyFreqCB.getSelectedItem().toString());
				entry_data.add(weeklyFreqCB.getSelectedItem().toString());
				// freq_days.add(weeklyFreqCB.getSelectedItem().toString());
				entry_data.add(startTimeTP.getFormatedTime().toString());
				entry_data.add(endTimeTP.getFormatedTime().toString());
				entry_data.add(opdStartTime.getFormatedTime().toString());
				entry_data.add(durationPerPatientTF.getText().toString());
				entry_data.add(noOfAppointmentsTF.getText().toString());
				entry_data.add(totalPatientTF.getText().toString());
				entry_data.add(chargesTF.getText().toString());
				entry_data.add(onlineChargesTF.getText().toString());
				entry_data.add(AdminMain.username);
				entry_data.add(time);
				DoctorDBConnection connection = new DoctorDBConnection();

				try {
					connection.inserAvailabiltyData(entry_data);
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
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				//
				// doctor_name.remove(cur_selectedRow);
				// doctor_id1.remove(cur_selectedRow);
				// freq_days.remove(cur_selectedRow);
				// frequencyType.remove(cur_selectedRow);
				// time_from.remove(cur_selectedRow);
				// time_to.remove(cur_selectedRow);
				// duration.remove(cur_selectedRow);
				// appointments.remove(cur_selectedRow);
				// charges.remove(cur_selectedRow);
				//

				DoctorDBConnection connection = new DoctorDBConnection();
				try {
					connection.deleteDoctorAvailability(toDelete);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connection.closeConnection();

				LoadTable();

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
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Entry ID", "ID", "Name", "Frequency", "Days / Date",
				"Time From", "Time To", "OPD Start Time", "Duration/Patient",
				"No of Appointments", "Total Patient", "OPD Charges",
				"Online Charges" }));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							btnRemove.setEnabled(true);
							btnEdit.setEnabled(true);
							int cur_selectedRow;
							cur_selectedRow = table.getSelectedRow();
							cur_selectedRow = table
									.convertRowIndexToModel(cur_selectedRow);
							String index = table.getModel()
									.getValueAt(cur_selectedRow, 0).toString();
							getRowDetail(index);
							
						} catch (Exception e2) {
							// TODO: handle exception
						}

					}
				});
		docNameCB = new JComboBox(new Object[] {});
		docNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					doctorNameSTR = docNameCB.getSelectedItem().toString();
					doctorIdSTR = doctor_id.get(docNameCB.getSelectedIndex())
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				lblDoctorId.setText(doctorIdSTR);
				LoadTable();
			}
		});
		docNameCB.setBounds(181, 23, 132, 24);
		getContentPane().add(docNameCB);

		lblDoctorId = new JLabel("Doctor id");
		lblDoctorId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDoctorId.setBounds(181, 53, 132, 24);
		getContentPane().add(lblDoctorId);

		startTimeTP = new JTimeChooser();
		startTimeTP.setBounds(181, 127, 132, 20);
		getContentPane().add(startTimeTP);

		endTimeTP = new JTimeChooser();
		endTimeTP.setBounds(435, 125, 132, 20);
		getContentPane().add(endTimeTP);

		JLabel lblTotalNumberOf = new JLabel("Total number of patient :");
		lblTotalNumberOf.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblTotalNumberOf.setBounds(340, 160, 186, 24);
		getContentPane().add(lblTotalNumberOf);

		totalPatientTF = new JTextField();
		totalPatientTF.setColumns(10);
		totalPatientTF.setBounds(535, 162, 132, 20);
		getContentPane().add(totalPatientTF);

		onlineChargesTF = new JTextField();
		onlineChargesTF.setColumns(10);
		onlineChargesTF.setBounds(511, 228, 132, 20);
		getContentPane().add(onlineChargesTF);

		JLabel lblOnlineCharges = new JLabel("Online Charges :");
		lblOnlineCharges.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOnlineCharges.setBounds(340, 228, 141, 24);
		getContentPane().add(lblOnlineCharges);

		opdStartTime = new JTimeChooser();
		opdStartTime.setBounds(749, 123, 132, 20);
		getContentPane().add(opdStartTime);

		JLabel lblOpdStartTime = new JLabel("OPD Start Time :");
		lblOpdStartTime.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblOpdStartTime.setBounds(598, 123, 141, 24);
		getContentPane().add(lblOpdStartTime);

		btnEdit = new JButton("Update");
		btnEdit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				if (startTimeTP.getFormatedTime().toString().equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter Time From");
					return;
				}
				if (endTimeTP.getFormatedTime().toString().equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter Time To");
					return;
				}
				if (opdStartTime.getFormatedTime().toString()
						.equals("00:00:00")) {
					JOptionPane.showMessageDialog(null, "Enter OPD Start Time");
					return;
				}
				if (durationPerPatientTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter duration Per Patient");
					return;
				}
				if (noOfAppointmentsTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter No. Of Appointments");
					return;
				}
				if (totalPatientTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Enter Total No. Of Patient");
					return;
				}
				if (chargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter opd charges");
					return;
				}
				if (onlineChargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Enter online charges");
					return;
				}
				entry_data.clear();
				entry_data.add(dailyFreqCB.getSelectedItem().toString());
				entry_data.add(weeklyFreqCB.getSelectedItem().toString());
				// freq_days.add(weeklyFreqCB.getSelectedItem().toString());
				entry_data.add(startTimeTP.getFormatedTime().toString());
				entry_data.add(endTimeTP.getFormatedTime().toString());
				entry_data.add(opdStartTime.getFormatedTime().toString());
				entry_data.add(durationPerPatientTF.getText().toString());
				entry_data.add(noOfAppointmentsTF.getText().toString());
				entry_data.add(totalPatientTF.getText().toString());
				entry_data.add(chargesTF.getText().toString());
				entry_data.add(onlineChargesTF.getText().toString());
				
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String index = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				
				DoctorDBConnection connection = new DoctorDBConnection();

				try {
					connection.updateDoctorAvailability(entry_data,index);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				connection.closeConnection();

				LoadTable();

				
				

			}
		});
		btnEdit.setEnabled(false);
		btnEdit.setBounds(420, 274, 132, 36);
		getContentPane().add(btnEdit);

		table.getColumnModel().getColumn(4).setPreferredWidth(85);
		table.getColumnModel().getColumn(7).setPreferredWidth(96);
		table.getColumnModel().getColumn(8).setPreferredWidth(108);
		getAllDoctors();

	}

	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllActiveData();
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
		docNameCB.setModel(doctors);
		docNameCB.setSelectedIndex(0);
	}

	public void getRowDetail(String index) throws ParseException {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveDoctorAvailabilityDetail(index);
		// "Entry ID","ID","Name", "Frequency",
		// "Days / Date", "Time From", "Time To","OPD Start Time",
		// "Duration/Patient", "No of Appointments", "Total Patient",
		// "OPD Charges","Online Charges"
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
		try {
			while (resultSet.next()) {

				dailyFreqCB.setSelectedItem(resultSet.getObject(4).toString());
				weeklyFreqCB.setSelectedItem(resultSet.getObject(5).toString());
				durationPerPatientTF.setText(resultSet.getObject(9).toString());
				noOfAppointmentsTF.setText(resultSet.getObject(10).toString());
				totalPatientTF.setText(resultSet.getObject(11).toString());
				chargesTF.setText(resultSet.getObject(12).toString());
				onlineChargesTF.setText(resultSet.getObject(13).toString());
				 startTimeTP.setTime(sdf.parse(resultSet.getObject(6).toString()));
				 endTimeTP.setTime(sdf.parse(resultSet.getObject(7).toString()));
				 opdStartTime.setTime(sdf.parse(resultSet.getObject(8).toString()));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	public void LoadTable() {

		table.getSelectionModel().clearSelection();
		btnRemove.setEnabled(false);
		try {

			DoctorDBConnection connection = new DoctorDBConnection();

			ResultSet rs = connection.retrieveDoctorAvailability(doctorNameSTR);
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs = connection.retrieveDoctorAvailability(doctorNameSTR);

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
					new String[] { "Entry ID", "ID", "Name", "Frequency",
							"Days / Date", "Time From", "Time To",
							"OPD Start Time", "Duration/Patient",
							"No of Appointments", "Total Patient",
							"OPD Charges", "Online Charges" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);

			connection.closeConnection();
			
			
			
			// startTimeTP.setTime(time)
//			durationPerPatientTF.setText("");
//			noOfAppointmentsTF.setText("");
//			totalPatientTF.setText("");
//			chargesTF.setText("");
//			onlineChargesTF.setText("");
//			
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	public JTextField getOnlineChargesTF() {
		return onlineChargesTF;
	}

	public JButton getBtnEdit() {
		return btnEdit;
	}

	public JTimeChooser getOpdStartTime() {
		return opdStartTime;
	}
}
