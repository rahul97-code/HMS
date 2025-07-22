package hms.reception.gui;

import hms.reception.database.ReceptionistDBConnection;

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

public class NewReceptionist extends JDialog {

	private JPanel contentPane;
	private JTextField receptionistNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField telephoneTB;
	String receptionistName = "", receptionistID = "", receptionistUserName = "", receptionistPassword = "",
		 receptionistTelephone = "",
			receptionistAddress = "", receptionistAchievement = "";
	private HashMap allreceptionists = new HashMap();
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
					NewReceptionist frame = new NewReceptionist();
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
	public NewReceptionist() {
		setTitle("Add New Receptionist");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewReceptionist.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAllreceptionists();
		setModal(true);
		int receptionistID=allreceptionists.size()+1;
		JPanel panel_3 = new JPanel();
		
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Receptionist ID : "+receptionistID,
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

		JLabel lblreceptionistName = new JLabel("Name :");
		lblreceptionistName.setBounds(6, 19, 119, 14);
		panel_1.add(lblreceptionistName);
		lblreceptionistName.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		receptionistNameTB = new JTextField();
		receptionistNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(receptionistNameTB);
		receptionistNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		receptionistNameTB.setColumns(10);

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
		chckbxWithToken.setBounds(136, 165, 97, 23);
		panel_1.add(chckbxWithToken);

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

		JButton savereceptionistBT = new JButton("Save");
		savereceptionistBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				receptionistName = receptionistNameTB.getText().toString();
				receptionistPassword = passwordTB.getText().toString();
				receptionistUserName = userNameTB.getText().toString();
				receptionistTelephone = telephoneTB.getText().toString();
				receptionistAchievement = achievementTA.getText().toString();
				receptionistAddress = addressTA.getText().toString();
				Object getAlready = null;
				try {
					getAlready = allreceptionists.get(receptionistUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (receptionistName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter receptionist name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					receptionistNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewReceptionist.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (receptionistUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (receptionistPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(receptionistPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (receptionistTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = receptionistName;
					data[1] = receptionistUserName;
					data[2] = receptionistPassword;
					data[3] = receptionistTelephone;
					data[4] = receptionistAddress;
					data[5] = receptionistAchievement;
					data[6] = chckbxWithToken.isSelected() ? "YES" : "NO";
					ReceptionistDBConnection dbConnection = new ReceptionistDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"receptionist add successfully", "Data save",
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
		savereceptionistBT.setIcon(new ImageIcon(NewReceptionist.class.getResource("/icons/exam_dialog.png")));
		savereceptionistBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		savereceptionistBT.setBounds(153, 272, 166, 45);
		panel_3.add(savereceptionistBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewReceptionist.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
	}

	public void savereceptionistData() {

	}

	public void getAllreceptionists() {
		ReceptionistDBConnection dbConnection = new ReceptionistDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				allreceptionists.put(resultSet.getObject(5).toString(), resultSet
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
