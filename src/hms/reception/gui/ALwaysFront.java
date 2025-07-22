package hms.reception.gui;

import hms.reception.database.TokenDBConnection;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

public class ALwaysFront extends JDialog {

	private JPanel contentPane;
	Dimension screenSize;
	int counter = 0;
	int lastCounter = 0;
	int lastCounterRegister = 0;
	long mLastClickTime = 0;
	Timer tokenTimer;
	CounterUpdateAlert counterUpdateAlert;
	ArrayList<Integer> tokenCounters = new ArrayList<Integer>();

	int refreshTime = 3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ALwaysFront frame = new ALwaysFront();
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
	public ALwaysFront() {
		setAlwaysOnTop(true);

		screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int) screenSize.getWidth();
		int height = (int) screenSize.getHeight();
		setResizable(false);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(width - 600, height - 120, 600, 60);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(0, 0, 0, 0));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));

		counterUpdateAlert = new CounterUpdateAlert();
		counterUpdateAlert.setModal(true);

		JSeparator separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		contentPane.add(separator);

		tokenTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// getUnreportedCounters(counterID);
				if (!ReceptionMain._id.equals("0")) {
					getNextCounters();
				}

			}
		});
		tokenTimer.setRepeats(true);
		tokenTimer.setCoalesce(true);
		tokenTimer.start();

		addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				System.out.println("jdialog window closed");
				tokenTimer.stop();
			}

			public void windowClosing(WindowEvent e) {
				System.out.println("jdialog window closing");
			}
		});
	}

	public void getNextCounters() {

		contentPane.removeAll();
		lastCounter = 0;
		counter = 0;
		tokenCounters.clear();
		
		TokenDBConnection dbConnection = new TokenDBConnection();

		ResultSet resultSet = dbConnection
				.retrieveDisplay(ReceptionMain._id);
		int counter=0;
		try {
			while (resultSet.next()) {

				JLabel lblNewLabel_4 = new JLabel(resultSet.getObject(1)
						.toString()+"-"+resultSet.getObject(2)
						.toString());
				lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
				Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
				lblNewLabel_4.setBorder(border);
				lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);

				counter++;
				contentPane.add(lblNewLabel_4);

//				setTitle("Total Token Printed : "
//						+ resultSet.getObject(3).toString());

				tokenCounters.add(Integer.parseInt(resultSet.getObject(2)
						.toString()));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(counter==0)
		{
			JLabel lblNewLabel_4 = new JLabel("OVER");
			lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 20));
			Border border = BorderFactory.createLineBorder(Color.BLUE, 2);
			lblNewLabel_4.setBorder(border);
			lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);

			contentPane.add(lblNewLabel_4);
		}
		dbConnection.closeConnection();
		contentPane.revalidate();
		// autoIncrementCounter(ReceptionMain._id);
	}

//	public void autoIncrementCounter(String id) {
//		if (tokenCounters.size() == 5) {
//
//			lastCounter = tokenCounters.get(tokenCounters.size() - 1);
//			if (lastCounterRegister == lastCounter) {
//
//				if (System.currentTimeMillis() - mLastClickTime > refreshTime * 60 * 1000) {
//
//					int val = tokenCounters.get(0);
//					mLastClickTime = System.currentTimeMillis();
//					counterUpdateAlert.updateCounter(val + "");
//					counterUpdateAlert.setVisible(true);
//
//				}
//
//			} else {
//				lastCounterRegister = lastCounter;
//				mLastClickTime = System.currentTimeMillis();
//				counterUpdateAlert.setVisible(false);
//			}
//		} else {
//			mLastClickTime = System.currentTimeMillis();
//			counterUpdateAlert.setVisible(false);
//		}
//	}
}
