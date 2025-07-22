package hms.reception.gui;

import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.opd.gui.OPDBrowser;
import hms.opd.gui.OPDEntery;
import hms.reception.database.TokenDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TokenEntryExam extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;

	ExamsBrowser browser;


	/**
	 * Create the dialog.
	 */
	public TokenEntryExam(ExamsBrowser opdBrowser) {
		setTitle("Token Entry");
		setResizable(false);
		
		browser=opdBrowser;

		//ReceptionMain._id = "1";
		setBounds(300, 200, 449, 230);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblEnterTokenNumber = new JLabel("Enter Token Number");
			lblEnterTokenNumber.setHorizontalAlignment(SwingConstants.CENTER);
			lblEnterTokenNumber.setFont(new Font("Tahoma", Font.BOLD, 20));
			lblEnterTokenNumber.setBounds(10, 0, 413, 53);
			contentPanel.add(lblEnterTokenNumber);
		}

		textField = new JTextField();
		textField.setHorizontalAlignment(SwingConstants.CENTER);
		textField.setFont(new Font("Tahoma", Font.BOLD, 25));
		textField.setBounds(10, 51, 413, 68);
		contentPanel.add(textField);
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					System.out.println("ENTER pressed");
					updateCounter();
				}
			}
		});
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				updateCounter();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 25));
		btnNewButton.setBounds(10, 128, 413, 53);
		contentPanel.add(btnNewButton);
	}

	public void updateCounter() {
		TokenDBConnection connection = new TokenDBConnection();
		if (textField.getText().toString().equals("")) {
			JOptionPane.showMessageDialog(null, "Please Enter Token No.");
			return;
		}

		if(!connection.checkToken(ReceptionMain._id, textField.getText().toString(), "2"))
		{
			JOptionPane.showMessageDialog(null, "Not Accepted");
			textField.setText("");
			return;
		}
		
		ReceptionMain.Counter=textField
				.getText().toString();
		UpdateCounterThread counterThread=new UpdateCounterThread(textField
					.getText().toString(),"2");
		counterThread.start();
		connection.closeConnection();
		dispose();
		ExamEntery opdEntery=new ExamEntery(browser);
		opdEntery.setVisible(true);
		
	}

	public int getCounterDetail(String id) {

		int value = 0;
		TokenDBConnection dbConnection = new TokenDBConnection();
		try {
			dbConnection.updateDateCounter(id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet resultSet = dbConnection.retrieveCounterDetail(id);
		try {
			while (resultSet.next()) {
				value = Integer.parseInt(resultSet.getObject(4).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		return value;
	}
}
