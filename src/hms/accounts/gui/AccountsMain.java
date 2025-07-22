package hms.accounts.gui;

import hms.cancellation.gui.CancelExamSlip;
import hms.cancellation.gui.CancelIndoorExam;
import hms.cancellation.gui.CancelMiscSlip;
import hms.cancellation.gui.Cancellation;
import hms.departments.database.DepartmentDBConnection;
import hms.main.AboutHMS;
import hms.main.MainLogin;
import hms.reception.gui.ReceptionMain;
import hms.reception.gui.ReceptionistSettings;
import hms.reports.gui.DateSelection;
import hms.reports.gui.DoctorsReport;
import hms.reports.gui.ReceptionUsersReport;
import hms1.ipd.gui.IPDBill;
import hms1.ipd.gui.IPDDeposit;

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

public class AccountsMain extends JFrame {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;

	public static String mrdID = "";
	public static String userName = "";
	public static String userID = "";
	
	public static void main(String[] arg) {
		AccountsMain accountsMain = new AccountsMain("","");
		accountsMain.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public AccountsMain(final String userName, String userID) {

		AccountsMain.userName = userName;
		AccountsMain.userID = userID;
		ReceptionMain.receptionNameSTR=userName;
		setTitle(" (" + userName + ")");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AccountsMain.class.getResource("/icons/rotaryLogo.png")));
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
		mnNewMenu.setIcon(new ImageIcon(AccountsMain.class
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
		
		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AccountsSettings accountsSettings = new AccountsSettings(
						userName);
				accountsSettings.setModal(true);
				accountsSettings.setVisible(true);
			}
		});
		mntmChangePassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmChangePassword);
		mntmLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmLogout.setIcon(new ImageIcon(AccountsMain.class
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
		
		JMenu mnMiscReciept = new JMenu("Misc Reciept");
		mnMiscReciept.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnMiscReciept);
		
		JMenuItem mntmPillesRequestRegister = new JMenuItem("Medicines Request Register");
		mntmPillesRequestRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				PerformaRegister performaRegister = new PerformaRegister();
				performaRegister.setModal(true);
				performaRegister.setVisible(true);
				ua.check_activity(userName, 133, 4);
			}
		});
		mntmPillesRequestRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMiscReciept.add(mntmPillesRequestRegister);
		
		JMenuItem mntmMyReports = new JMenuItem("My Reports");
		mntmMyReports.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(6);
				dateSelection.setVisible(true);
				ua.check_activity(userName, 134, 4);
			}
		});
		mntmMyReports.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMiscReciept.add(mntmMyReports);
		
		JMenu mnIpdPatients = new JMenu("IPD Patients");
		mnIpdPatients.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnIpdPatients);
		
		JMenuItem mntmGenerateBill = new JMenuItem("Generate Bill");
		mntmGenerateBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDBillAccounts bill=new IPDBillAccounts();
				bill.setVisible(true);
				ua.check_activity(userName, 136, 4);
			}
		});
		
		JMenuItem mntmIpdAdvancereturnEntry = new JMenuItem("IPD Advance/Return Entry");
		mntmIpdAdvancereturnEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				IPDDeposit bill=new IPDDeposit();
				bill.setModal(true);
				bill.setVisible(true);
				ua.check_activity(userName, 135, 4);
			}
		});
		mntmIpdAdvancereturnEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIpdPatients.add(mntmIpdAdvancereturnEntry);
		mntmGenerateBill.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIpdPatients.add(mntmGenerateBill);
		
		JMenu menu = new JMenu("Report Accounts");
		menu.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(menu);
		
		JMenuItem menuItem = new JMenuItem("Users Report");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ReceptionUsersReport receptionUsersReport=new ReceptionUsersReport();
				receptionUsersReport.setModal(true);
				receptionUsersReport.setVisible(true);
				ua.check_activity(userName, 138, 4);
			}
		});
		
		JMenuItem menuItem_1 = new JMenuItem("Summery Report");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection = new DateSelection(0);
				dateSelection.setVisible(true);
				ua.check_activity(userName, 137, 4);
			}
		});
		menuItem_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu.add(menuItem_1);
		menuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu.add(menuItem);
		
		JMenuItem mntmDoctorsReport = new JMenuItem("Doctors Report");
		mntmDoctorsReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DoctorsReport doctorsReport = new DoctorsReport();
				doctorsReport.setModal(true); 
				doctorsReport.setVisible(true);
				ua.check_activity(userName, 139, 4);
			}
		});
		mntmDoctorsReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu.add(mntmDoctorsReport);
		
		JMenu mnNewMenu_1 = new JMenu("IPD Reports");
		mnNewMenu_1.setFont(new Font("Dialog", Font.BOLD, 14));
		menu.add(mnNewMenu_1);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Pending Patients");
		mntmNewMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(5);
				dateSelection.setVisible(true);
				ua.check_activity(userName,67,3);
			}
		});
		mntmNewMenuItem.setFont(new Font("Dialog", Font.BOLD, 14));
		mnNewMenu_1.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Advance Amount");
		mntmNewMenuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(7);
				dateSelection.setVisible(true);
				ua.check_activity(userName,68,3);
			}
		});
		mntmNewMenuItem_1.setFont(new Font("Dialog", Font.BOLD, 14));
		mnNewMenu_1.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("Discharged Patients");
		mntmNewMenuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(4);
				dateSelection.setVisible(true);
				ua.check_activity(userName,69,3);
			}
		});
		mntmNewMenuItem_2.setFont(new Font("Dialog", Font.BOLD, 14));
		mnNewMenu_1.add(mntmNewMenuItem_2);
		
		JMenuItem mntmNewMenuItem_3 = new JMenuItem("Cancelled Reports");
		mntmNewMenuItem_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(1);
				dateSelection.setVisible(true);
				ua.check_activity(userName,73,3);
			}
		});
		mntmNewMenuItem_3.setFont(new Font("Dialog", Font.BOLD, 14));
		menu.add(mntmNewMenuItem_3);
		
		JMenu mnCancellation = new JMenu("Record Cancellation");
		mnCancellation.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mnCancellation.setFont(new Font("Dialog", Font.BOLD, 15));
		menuBar.add(mnCancellation);
		
		JMenuItem mntmCancelOpd = new JMenuItem("Cancel OPD");
		mntmCancelOpd.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mntmCancelOpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cancellation cancellation = new Cancellation(0);
				cancellation.setVisible(true);
				cancellation.setModal(true);
				ua.check_activity(userName,104,3);
			}
		});
		mntmCancelOpd.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelOpd);
		
		JMenuItem mntmCancelExams = new JMenuItem("Cancel Exams");
		mntmCancelExams.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mntmCancelExams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelExamSlip cancelExamSlip = new CancelExamSlip();
				cancelExamSlip.setModal(true);
				cancelExamSlip.setVisible(true);
				ua.check_activity(userName,105,3);
			}
		});
		mntmCancelExams.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelExams);
		
		JMenuItem mntmCancelMisc = new JMenuItem("Cancel Misc");
		mntmCancelMisc.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mntmCancelMisc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelMiscSlip cancelMiscSlip = new CancelMiscSlip();
				cancelMiscSlip.setModal(true);
				cancelMiscSlip.setVisible(true);
				ua.check_activity(userName,106,3);
				
			}
		});
		mntmCancelMisc.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelMisc);
		
		JMenuItem mntmCancelPatientCard = new JMenuItem("Cancel Patient Card");
		mntmCancelPatientCard.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mntmCancelPatientCard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Cancellation cancellation = new Cancellation(2);
				cancellation.setVisible(true);
				cancellation.setModal(true);
				ua.check_activity(userName,107,3);
			}
		});
		mntmCancelPatientCard.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelPatientCard);
		
		JMenuItem mntmCancelIpd = new JMenuItem("Cancel IPD Exams");
		mntmCancelIpd.setIcon(new ImageIcon(AccountsMain.class.getResource("/icons/delete_button.png")));
		mntmCancelIpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				CancelIndoorExam cancelIndoorExam = new CancelIndoorExam();
				cancelIndoorExam.setModal(true);
				cancelIndoorExam.setVisible(true);
				ua.check_activity(userName,108,3);
			}
		});
		mntmCancelIpd.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelIpd);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}


}
