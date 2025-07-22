package hms.accounts.gui;

import hms.accounts.database.AccountsUserDBConnection;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

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

public class NewAccountsUser extends JDialog {

	private JPanel contentPane;
	private JTextField AccountsNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String AccountsName = "", AccountsID = "", AccountsUserName = "", AccountsPassword = "",
		 AccountsTelephone = "",
			AccountsAddress = "", AccountsAchievement = "";
	private HashMap allAccountss = new HashMap();
	String[] data = new String[20];
	private JCheckBox chckbxWithToken;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					NewAccountsUser frame = new NewAccountsUser();
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
	public NewAccountsUser() {
		setTitle("Add New User");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewAccountsUser.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllAccountss();
		setModal(true);
		int AccountsID=allAccountss.size()+1;
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Accounts ID : "+AccountsID,
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

		JLabel lblAccountsName = new JLabel("Name :");
		lblAccountsName.setBounds(6, 19, 119, 14);
		panel_1.add(lblAccountsName);
		lblAccountsName.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		AccountsNameTB = new JTextField();
		AccountsNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(AccountsNameTB);
		AccountsNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		AccountsNameTB.setColumns(10);

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
		
		chckbxWithToken = new JCheckBox("Without Token");
		chckbxWithToken.setBounds(136, 165, -3, 23);
		panel_1.add(chckbxWithToken);
		
				JPanel panel = new JPanel();
				panel.setBounds(6, 150, 309, 75);
				panel_1.add(panel);
				panel.setBorder(new TitledBorder(UIManager
						.getBorder("TitledBorder.border"), "Addresss :",
						TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
								Font.PLAIN, 12), null));
				panel.setLayout(null);
				
						JScrollPane scrollPane = new JScrollPane();
						scrollPane.setBounds(6, 16, 293, 48);
						panel.add(scrollPane);
						
								final JTextArea addressTA = new JTextArea();
								addressTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
								addressTA.setLineWrap(true);
								addressTA.setRows(4);
								scrollPane.setViewportView(addressTA);

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

		JButton saveAccountsBT = new JButton("Save");
		saveAccountsBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				AccountsName = AccountsNameTB.getText().toString();
				AccountsPassword = passwordTB.getText().toString();
				AccountsUserName = userNameTB.getText().toString();
				AccountsTelephone = telephoneTB.getText().toString();
				AccountsAchievement = achievementTA.getText().toString();
				AccountsAddress = addressTA.getText().toString();
				Object getAlready = null;
				try {
					getAlready = allAccountss.get(AccountsUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (AccountsName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Accounts name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					AccountsNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewAccountsUser.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (AccountsUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (AccountsPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(AccountsPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (AccountsTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = AccountsName;
					data[1] = AccountsUserName;
					data[2] = AccountsPassword;
					data[3] = AccountsTelephone;
					data[4] = AccountsAddress;
					data[5] = AccountsAchievement;
					data[6] = chckbxWithToken.isSelected() ? "YES" : "NO";
					AccountsUserDBConnection dbConnection = new AccountsUserDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"Accounts add successfully", "Data save",
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
		saveAccountsBT.setIcon(new ImageIcon(NewAccountsUser.class.getResource("/icons/exam_dialog.png")));
		saveAccountsBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveAccountsBT.setBounds(153, 272, 166, 45);
		panel_3.add(saveAccountsBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewAccountsUser.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
	}

	public void saveAccountsData() {

	}

	public void getAllAccountss() {
		AccountsUserDBConnection dbConnection = new AccountsUserDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allAccountss.put(resultSet.getObject(5).toString(), resultSet
						.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public JCheckBox getChckbxWithToken() {
		return chckbxWithToken;
	}
}
