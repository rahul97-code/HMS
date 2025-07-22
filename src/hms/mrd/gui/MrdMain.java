package hms.mrd.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.main.AboutHMS;
import hms.main.MainLogin;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import UsersActivity.database.UADBConnection;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.ImageIcon;
import java.awt.Font;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MrdMain extends JFrame {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;

	public static String mrdID = "";
	public static String userName = "";
	public static String userID = "";
	

	/**
	 * Create the frame.
	 */
	public MrdMain(final String userName, String userID) {

		MrdMain.userName = userName;
		MrdMain.mrdID = userID;
		setTitle(" (" + userName + ")");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MrdMain.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setBounds(10, 10, width - 20, height - 60);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		JMenu mnNewMenu = new JMenu("My Account");
		mnNewMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnNewMenu.setIcon(new ImageIcon(MrdMain.class
				.getResource("/icons/author.png")));
		menuBar.add(mnNewMenu);
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MainLogin mainLogin = new MainLogin();
				mainLogin.setVisible(true);
				dispose();
			}
		});
		mntmLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmLogout.setIcon(new ImageIcon(MrdMain.class
				.getResource("/icons/exits.png")));
		mnNewMenu.add(mntmLogout);

		JMenuItem mntmAboutHms = new JMenuItem("About HMS");
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutHMS aboutHMS = new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);
			}
		});
		mntmAboutHms.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmAboutHms);

		JMenu mnMyStock = new JMenu("MRD Records");
		mnMyStock.setIcon(new ImageIcon(MrdMain.class
				.getResource("/icons/stockicon.png")));
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnMyStock);

		JMenuItem mntmStockRegister = new JMenuItem("IPD Files");
		mntmStockRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IPDFiles departmentStock = new IPDFiles();
				departmentStock.setVisible(true);
				ua.check_activity(userName, 144, 7);
				
			}
		});
		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmStockRegister);
		
		JMenuItem mntmOpdRecords = new JMenuItem("OPD Files");
		mntmOpdRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OPDFiles departmentStock = new OPDFiles();
				departmentStock.setVisible(true);
				ua.check_activity(userName, 145, 7);
			}
		});
		mntmOpdRecords.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmOpdRecords);
		
		JMenuItem mntmExamRecords = new JMenuItem("Exam Files");
		mntmExamRecords.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExamFiles departmentStock = new ExamFiles();
				departmentStock.setVisible(true);
				ua.check_activity(userName, 146, 7);
			}
		});
		mntmExamRecords.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmExamRecords);
		
		JMenuItem mntmPatientRecord = new JMenuItem("Patient Record");
		mntmPatientRecord.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmPatientRecord.addActionListener(new ActionListener() {
			

			public void actionPerformed(ActionEvent e) {
				MrdSystem window = new MrdSystem();
				window.setModal(true);
                window.setVisible(true);
			}
		});
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmPatientRecord);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}


}
