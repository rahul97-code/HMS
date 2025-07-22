package hms.reception.gui;

import hms.reception.database.TokenDBConnection;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Front extends JFrame implements WindowFocusListener,WindowListener {
	
	final DefaultComboBoxModel countersModel = new DefaultComboBoxModel();
	Vector<String> countersID = new Vector<String>();
	String _id;
	public Front() {
		setTitle("Token Counter");
		
		addWindowFocusListener(this);
		setAlwaysOnTop(true);
		setResizable(false);
		this.setFocusable(true);
		
		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		
		// this.setFocusableWindowState(true);
		panel = new JPanel();
		setSize(447, 133);
		// setUndecorated(true);
		setLocation(width-X-WIDTH, Y);
		getContentPane().setLayout(null);
		
		JLabel lblSelectCounter = new JLabel("Select Counter");
		lblSelectCounter.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSelectCounter.setBounds(50, 11, 111, 22);
		getContentPane().add(lblSelectCounter);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBox.setBounds(171, 12, 237, 20);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					_id = countersID.get(comboBox.getSelectedIndex());
					getCounterDetail(_id);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getContentPane().add(comboBox);
		
		btnNewButton = new JButton("Previouse");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				previouseButton();
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(10, 56, 146, 38);
		getContentPane().add(btnNewButton);
		
		btnNext = new JButton("Next");
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				nextButton();
			}
		});
		btnNext.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNext.setBounds(285, 56, 146, 38);
		getContentPane().add(btnNext);
		
		lblToken = new JLabel("Token");
		lblToken.setHorizontalAlignment(SwingConstants.CENTER);
		lblToken.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblToken.setBounds(173, 56, 102, 38);
		getContentPane().add(lblToken);
		// setExtendedState(MAXIMIZED_BOTH);
		setVisible(true);
		getAllCounters();
		ReceptionMain.tokenCounterFlag=true;
	}

	public void nextButton()
	{
		TokenDBConnection connection = new TokenDBConnection();
		try {
			connection.updateDate(_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		connection.closeConnection();
		getCounterDetail(_id);
	}
	public void previouseButton()
	{
		TokenDBConnection connection = new TokenDBConnection();
		try {
			connection.updateDataCounterDec(_id);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		connection.closeConnection();
		getCounterDetail(_id);
	}

	public void windowLostFocus(WindowEvent e) {
		if (e.getNewState() != e.WINDOW_CLOSED) {
			// toFront();
			// requestFocus();
			setAlwaysOnTop(false);
			setAlwaysOnTop(true);
			// requestFocusInWindow();
			System.out.println("focus lost");
		}

	}
	Dimension screenSize ;
	private JPanel panel;
	private static final int WIDTH = 447;
	private static final int HEIGHT = 133;
	
	private static final int X = 20;
	private static final int Y = 20;
	private JLabel lblToken;
	private JButton btnNext;
	private JButton btnNewButton;
	private JComboBox comboBox;

	public static void main(String args[]) {
		new Front();
	}
	public void getAllCounters() {
		countersID.clear();
		TokenDBConnection dbConnection = new TokenDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataCounters();
		try {
			while (resultSet.next()) {
				countersID.add(resultSet.getObject(1).toString());
				countersModel.addElement(resultSet.getObject(2).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

		comboBox.setModel(countersModel);
		comboBox.setSelectedIndex(0);
	}

	public void getCounterDetail(String id) {

		TokenDBConnection dbConnection = new TokenDBConnection();
		ResultSet resultSet = dbConnection.retrieveCounterDetail(id);
		try {
			while (resultSet.next()) {
				lblToken.setText(resultSet.getObject(4)
						.toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		ReceptionMain.tokenCounterFlag=false;
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowGainedFocus(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}