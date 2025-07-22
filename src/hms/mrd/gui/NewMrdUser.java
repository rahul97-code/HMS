package hms.mrd.gui;

import hms.mrd.database.MRDUserDBConnection;


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
import javax.swing.JCheckBox;

public class NewMrdUser extends JDialog {

	private JPanel contentPane;
	private JTextField MrdNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String MrdName = "", MrdID = "", MrdUserName = "", MrdPassword = "",
		 MrdTelephone = "",
			MrdAddress = "", MrdAchievement = "";
	private HashMap allMrds = new HashMap();
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
					NewMrdUser frame = new NewMrdUser();
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
	public NewMrdUser() {
		setTitle("Add New User");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewMrdUser.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllMrds();
		setModal(true);
		int MrdID=allMrds.size()+1;
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Mrd ID : "+MrdID,
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

		JLabel lblMrdName = new JLabel("Name :");
		lblMrdName.setBounds(6, 19, 119, 14);
		panel_1.add(lblMrdName);
		lblMrdName.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		MrdNameTB = new JTextField();
		MrdNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(MrdNameTB);
		MrdNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		MrdNameTB.setColumns(10);

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

		JButton saveMrdBT = new JButton("Save");
		saveMrdBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				MrdName = MrdNameTB.getText().toString();
				MrdPassword = passwordTB.getText().toString();
				MrdUserName = userNameTB.getText().toString();
				MrdTelephone = telephoneTB.getText().toString();
				MrdAchievement = achievementTA.getText().toString();
				MrdAddress = addressTA.getText().toString();
				Object getAlready = null;
				try {
					getAlready = allMrds.get(MrdUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (MrdName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Mrd name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					MrdNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewMrdUser.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (MrdUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (MrdPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(MrdPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (MrdTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = MrdName;
					data[1] = MrdUserName;
					data[2] = MrdPassword;
					data[3] = MrdTelephone;
					data[4] = MrdAddress;
					data[5] = MrdAchievement;
					data[6] = chckbxWithToken.isSelected() ? "YES" : "NO";
					MRDUserDBConnection dbConnection = new MRDUserDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"Mrd add successfully", "Data save",
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
		saveMrdBT.setIcon(new ImageIcon(NewMrdUser.class.getResource("/icons/exam_dialog.png")));
		saveMrdBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveMrdBT.setBounds(153, 272, 166, 45);
		panel_3.add(saveMrdBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewMrdUser.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
	}

	public void saveMrdData() {

	}

	public void getAllMrds() {
		MRDUserDBConnection dbConnection = new MRDUserDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allMrds.put(resultSet.getObject(5).toString(), resultSet
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
