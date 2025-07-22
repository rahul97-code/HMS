package hms.test.gui;

import hms.test.database.OperatorDBConnection;

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

public class NewOperator extends JDialog {

	private JPanel contentPane;
	private JTextField operatorNameTB;
	private JTextField userNameTB;
	private JPasswordField passwordTB;
	private JPasswordField confPasswordTB;
	private JTextField opdRoomTB;
	private JTextField telephoneTB;
	String operatorName = "", operatorID = "", operatorUserName = "", operatorPassword = "",
			operatorLabName = "", operatorOPDRoom = "", operatorTelephone = "",
			operatorAddress = "", operatorAchievement = "";
	private JTextField LabNameTB;
	private HashMap alloperators = new HashMap();
	String[] data = new String[20];

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					NewOperator frame = new NewOperator();
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
	public NewOperator() {
		setTitle("New Exam Operator");
		setIconImage(Toolkit.getDefaultToolkit().getImage(NewOperator.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 667, 374);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		getAlloperators();
		int operatorID=alloperators.size()+1;
		JPanel panel_3 = new JPanel();
		setModal(true);
		panel_3.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Operator ID : "+operatorID,
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

		JLabel lbloperatorName = new JLabel("Name :");
		lbloperatorName.setBounds(6, 19, 119, 14);
		panel_1.add(lbloperatorName);
		lbloperatorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

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

		operatorNameTB = new JTextField();
		operatorNameTB.setBounds(135, 16, 180, 25);
		panel_1.add(operatorNameTB);
		operatorNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		operatorNameTB.setColumns(10);

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

		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "Achievement :",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 150, 305, 75);
		panel_1.add(panel_4);
		panel_4.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 19, 289, 47);
		panel_4.add(scrollPane_1);

		final JTextArea achievementTA = new JTextArea();
		achievementTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		achievementTA.setLineWrap(true);
		achievementTA.setRows(3);
		scrollPane_1.setViewportView(achievementTA);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(343, 25, 300, 236);
		panel_3.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 14), null));
		panel_2.setLayout(null);

		JLabel lblOpdRoom = new JLabel("Room No.:");
		lblOpdRoom.setBounds(6, 49, 104, 14);
		panel_2.add(lblOpdRoom);
		lblOpdRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone No. :");
		lblTelephone.setBounds(6, 80, 104, 18);
		panel_2.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		opdRoomTB = new JTextField();
		opdRoomTB.setBounds(120, 44, 165, 25);
		panel_2.add(opdRoomTB);
		opdRoomTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdRoomTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setBounds(120, 77, 165, 25);
		panel_2.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(12, 109, 269, 96);
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

		LabNameTB = new JTextField();
		LabNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		LabNameTB.setColumns(10);
		LabNameTB.setBounds(120, 11, 165, 25);
		panel_2.add(LabNameTB);

		JLabel lblLabName = new JLabel("Exam Lab Name :");
		lblLabName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLabName.setBounds(6, 14, 95, 18);
		panel_2.add(lblLabName);

		JButton saveoperatorBT = new JButton("Save");
		saveoperatorBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				operatorName = operatorNameTB.getText().toString();
				operatorPassword = passwordTB.getText().toString();
				operatorUserName = userNameTB.getText().toString();
				operatorLabName = LabNameTB.getText().toString();
				operatorOPDRoom = opdRoomTB.getText().toString();
				operatorTelephone = telephoneTB.getText().toString();
				operatorAchievement = achievementTA.getText().toString();
				operatorAddress = addressTA.getText().toString();
				Object getAlready = null;
				try {
					getAlready = alloperators.get(operatorUserName);
				} catch (Exception e) {
					// TODO: handle exception
				}

				if (operatorName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter operator name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					operatorNameTB.requestFocus();
					return;
				} else if (getAlready != null) {
					JOptionPane.showMessageDialog(NewOperator.this,
							"Can not enter duplicate username .Username already exist",
							"Duplicate Username ",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else if (operatorUserName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Enter User Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					userNameTB.requestFocus();
					return;

				} else if (operatorPassword.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter password", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					passwordTB.requestFocus();
					return;

				} else if (!confPasswordTB.getText().toString()
						.equals(operatorPassword)) {
					JOptionPane.showMessageDialog(null,
							"Password does not match", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					confPasswordTB.requestFocus();
					return;

				} else if (operatorLabName.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter Lab Name", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					LabNameTB.requestFocus();
					return;

				} else if (operatorOPDRoom.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter OPD Room", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					opdRoomTB.requestFocus();
					return;

				} else if (operatorTelephone.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter telephone number", "Input Error",
							JOptionPane.INFORMATION_MESSAGE);
					telephoneTB.requestFocus();
					return;

				} else {
					data[0] = operatorName;
					data[1] = operatorUserName;
					data[2] = operatorPassword;
					data[3] = operatorLabName;
					data[4] = operatorOPDRoom;
					data[5] = operatorTelephone;
					data[6] = operatorAddress;
					data[7] = operatorAchievement;
					OperatorDBConnection dbConnection = new OperatorDBConnection();
					try {
						dbConnection.inserData(data);
						JOptionPane.showMessageDialog(null,
								"operator add successfully", "Data save",
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
		saveoperatorBT.setIcon(new ImageIcon(NewOperator.class.getResource("/icons/exam_dialog.png")));
		saveoperatorBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		saveoperatorBT.setBounds(153, 272, 166, 45);
		panel_3.add(saveoperatorBT);

		JButton closeBT = new JButton("Cancel");
		closeBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		closeBT.setIcon(new ImageIcon(NewOperator.class
				.getResource("/icons/close_button.png")));
		closeBT.setFont(new Font("Tahoma", Font.PLAIN, 12));
		closeBT.setBounds(343, 272, 166, 45);
		panel_3.add(closeBT);
	}

	public void saveoperatorData() {

	}

	public void getAlloperators() {
		OperatorDBConnection dbConnection = new OperatorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				alloperators.put(resultSet.getObject(5).toString(), resultSet
						.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
}
