package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.main.DateFormatChange;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class NewDepartment extends JDialog {

	private JPanel contentPane;
	private JTextField departmentNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField opdRoomTB;
	String departmentName = "", departmentID = "", departmentUserName = "", departmentPassword = "",
			departmentLabName = "", departmentOPDRoom = "";
	private JTextField LabNameTB;
	String[] data = new String[20];
	Vector<String> allDepartments = new Vector<String>();
	Vector<String> allDepartmentsUsername = new Vector<String>();

	/**
	 * Create the frame.
	 */
	public NewDepartment() {
		setTitle("Add New Department");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewDepartment.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 302);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAlldepartments();
		int departmentID=allDepartments.size()+1;
		JPanel panel_3 = new JPanel();
		setModal(true);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Department ID : "+departmentID,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_3.setBounds(0, 0, 649, 267);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(10, 25, 321, 167);
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lbldepartmentName = new JLabel("Department Name :");
		lbldepartmentName.setBounds(6, 19, 119, 14);
		panel_1.add(lbldepartmentName);
		lbldepartmentName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblUserName = new JLabel("User Name :");
		lblUserName.setBounds(6, 56, 81, 14);
		panel_1.add(lblUserName);
		lblUserName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblPassword = new JLabel("Password :");
		lblPassword.setBounds(6, 90, 81, 14);
		panel_1.add(lblPassword);
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblConfirmPassword = new JLabel("Confirm Password :");
		lblConfirmPassword.setBounds(6, 125, 119, 14);
		panel_1.add(lblConfirmPassword);
		lblConfirmPassword.setFont(new Font("Tahoma", Font.PLAIN, 12));

		departmentNameTB = new JTextField();
		departmentNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(departmentNameTB);
		departmentNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		departmentNameTB.setColumns(10);

		userNameTB = new JTextField();
		userNameTB.setBounds(135, 53, 180, 25);
		panel_1.add(userNameTB);
		userNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		userNameTB.setColumns(10);

		passwordTB = new JPasswordField();
		passwordTB.setBounds(135, 87, 180, 25);
		panel_1.add(passwordTB);
		passwordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		passwordTB.setColumns(10);

		confPasswordTB = new JPasswordField();
		confPasswordTB.setBounds(135, 122, 180, 25);
		panel_1.add(confPasswordTB);
		confPasswordTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		confPasswordTB.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(337, 25, 300, 167);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblOpdRoom = new JLabel("Room No.:");
		lblOpdRoom.setBounds(10, 85, 104, 14);
		panel_2.add(lblOpdRoom);
		lblOpdRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdRoomTB = new JTextField();
		opdRoomTB.setBounds(124, 80, 165, 25);
		panel_2.add(opdRoomTB);
		opdRoomTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdRoomTB.setColumns(10);

		LabNameTB = new JTextField();
		LabNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		LabNameTB.setColumns(10);
		LabNameTB.setBounds(124, 47, 165, 25);
		panel_2.add(LabNameTB);

		JLabel lblLabName = new JLabel("Room Name :");
		lblLabName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLabName.setBounds(10, 50, 95, 18);
		panel_2.add(lblLabName);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Ward", "Lab", "Store", "Operation Theater", "Doctor Room", "Other"}));
		comboBox.setBounds(124, 11, 165, 25);
		panel_2.add(comboBox);
		
		JLabel lblDepartmentType = new JLabel("Dept. Type :");
		lblDepartmentType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDepartmentType.setBounds(10, 16, 104, 18);
		panel_2.add(lblDepartmentType);

		JButton savedepartmentBT = new JButton("Save");
		savedepartmentBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				departmentName = departmentNameTB.getText().toString();
				departmentPassword = passwordTB.getText().toString();
				departmentUserName = userNameTB.getText().toString();
				departmentLabName = LabNameTB.getText().toString();
				departmentOPDRoom = opdRoomTB.getText().toString();
				
				int getAlready = 0,getAlready1=0;
				try {
					getAlready = allDepartments.indexOf(departmentName);
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				try {
					getAlready1 = allDepartmentsUsername.indexOf(departmentUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (departmentName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter department name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					departmentNameTB.requestFocus();
					return;
				} else if (getAlready != -1) {
					JOptionPane.showMessageDialog(NewDepartment.this,
							"Can not enter duplicate Department Name .Department name already exist",
							"Duplicate Department Name ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				else if (getAlready1 != -1) {
					JOptionPane.showMessageDialog(NewDepartment.this,
							"Can not enter duplicate User Name .User name already exist",
							"Duplicate User Name ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}else if (departmentUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (departmentPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(departmentPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (departmentLabName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter Room Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					LabNameTB.requestFocus();
					return;

				} else if (departmentOPDRoom.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter OPD Room", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					opdRoomTB.requestFocus();
					return;

				}  else {
					data[0] = departmentName.toUpperCase();
					data[1] = departmentUserName;
					data[2] = departmentPassword;
					data[3] = comboBox.getSelectedItem().toString();
					data[4] = departmentLabName;
					data[5] = departmentOPDRoom;
					data[6] = "Yes";
					data[7] = "";  //user name
					data[8] = DateFormatChange.StringToMysqlDate(new Date());
					DepartmentDBConnection dbConnection = new DepartmentDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"department add successfully", "Data save",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						dbConnection.closeConnection();
					}
					dbConnection.closeConnection();
				}

			}
		});
		savedepartmentBT.setIcon(new ImageIcon(NewDepartment.class.getResource("/icons/exam_dialog.png")));
		savedepartmentBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		savedepartmentBT.setBounds(158, 203, 166, 45);
		panel_3.add(savedepartmentBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewDepartment.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(348, 203, 166, 45);
		panel_3.add(closeBT);
	}

	public void savedepartmentData() {

	}

	public void getAlldepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allDepartments.add(resultSet.getObject(2).toString());
				allDepartmentsUsername.add(resultSet.getObject(3).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
}
