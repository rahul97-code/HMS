package hms.admin.gui;

import hms.departments.database.DepartmentDBConnection;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JComboBox;

public class NewDepartmentUser extends JDialog {

	private JPanel contentPane;
	private JTextField UserNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String UserName = "", UserID = "", UserUserName = "", UserPassword = "",
		 UserTelephone = "",
			UserAddress = "", UserAchievement = "";
	private HashMap allUsers = new HashMap();
	String[] data = new String[20];
	private JComboBox departmentCB;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					NewDepartmentUser frame = new NewDepartmentUser();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public NewDepartmentUser() {
		setTitle("Add New User");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewDepartmentUser.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllUsers();
		setModal(true);
		int UserID=allUsers.size()+1;
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "User ID : "+UserID,
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 14), null));
		panel_3.setBounds(0, 0, 649, 331);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(6, 25, 321, 236);
		panel_3.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_1.setLayout(null);

		JLabel lblName = new JLabel("Name :");
		lblName.setBounds(6, 19, 119, 14);
		panel_1.add(lblName);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		UserNameTB = new JTextField();
		UserNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(UserNameTB);
		UserNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		UserNameTB.setColumns(10);

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
		
		departmentCB = new JComboBox();
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		departmentCB.setBounds(146, 158, 169, 25);
		panel_1.add(departmentCB);
		
		JLabel label = new JLabel("Select Department");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 158, 132, 25);
		panel_1.add(label);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(343, 25, 300, 236);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblTelephone = new JLabel("Telephone No. :");
		lblTelephone.setBounds(12, 14, 104, 18);
		panel_2.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		telephoneTB = new JTextField();
		telephoneTB.setBounds(126, 11, 165, 25);
		panel_2.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(12, 129, 269, 96);
		panel_2.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Addresss :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 16, 257, 73);
		panel.add(scrollPane);

		final JTextArea addressTA = new JTextArea();
		addressTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTA.setLineWrap(true);
		addressTA.setRows(4);
		scrollPane.setViewportView(addressTA);
		
				JPanel panel_4 = new JPanel();
				panel_4.setBounds(12, 54, 305, 75);
				panel_2.add(panel_4);
				panel_4.setBorder(new TitledBorder(null, "Achievement :",
						TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
								Font.PLAIN, 12), null));
				panel_4.setLayout(null);
				
						JScrollPane scrollPane_1 = new JScrollPane();
						scrollPane_1.setBounds(10, 19, 289, 47);
						panel_4.add(scrollPane_1);
						
								final JTextArea achievementTA = new JTextArea();
								achievementTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
								achievementTA.setLineWrap(true);
								achievementTA.setRows(3);
								scrollPane_1.setViewportView(achievementTA);

		JButton saveUserBT = new JButton("Save");
		saveUserBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				UserName = UserNameTB.getText().toString();
				UserPassword = passwordTB.getText().toString();
				UserUserName = userNameTB.getText().toString();
				UserTelephone = telephoneTB.getText().toString();
				UserAchievement = achievementTA.getText().toString();
				UserAddress = addressTA.getText().toString();
				Object getAlready = null;
				try {
					getAlready = allUsers.get(UserUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (UserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter User name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					UserNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewDepartmentUser.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (UserUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (UserPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(UserPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (UserTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = UserName;
					data[1] = UserUserName;
					data[2] = UserPassword;
					data[3] = UserTelephone;
					data[4] = UserAddress;
					data[5] = UserAchievement;
					data[6] = departmentCB.getSelectedItem().toString();
					DeptUserDBConnection dbConnection = new DeptUserDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"User add successfully", "Data save",
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
		saveUserBT.setIcon(new ImageIcon(NewDepartmentUser.class.getResource("/icons/exam_dialog.png")));
		saveUserBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveUserBT.setBounds(153, 272, 166, 45);
		panel_3.add(saveUserBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewDepartmentUser.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
		getAllDepartments();
	}

	public void saveUserData() {

	}
	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}
	
	public void getAllUsers() {
		DeptUserDBConnection dbConnection = new DeptUserDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allUsers.put(resultSet.getObject(5).toString(), resultSet
						.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

}
