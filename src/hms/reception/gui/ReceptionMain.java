package hms.reception.gui;

import hms.admin.gui.DailyCash;
import hms.doctor.gui.DoctorMain;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.insurance.gui.InsurancePayments;
import hms.main.AboutHMS;
import hms.main.MainLogin;
import hms.main.MarqueeLabel;
import hms.main.NewsDBConnection;
import hms.main.RealTimeClock;
import hms.misc.gui.MISCBrowser;
import hms.misc.gui.MiscAmountEntery;
import hms.opd.gui.OPDBrowser;
import hms.opd.gui.OPDEntery;
import hms.opd.gui.OnlineOPDBrowser;
import hms.patient.data_bundle.PatientHistoryECHS;
import hms.patient.data_bundle.PatientHistoryESI;
import hms.patient.data_bundle.PatientHistoryRailway;
import hms.patient.gui.PatientBrowser;
import hms.patient.history.PatientHistory;
import hms.payments.PaymentTransactionHistory;
import hms.reception.database.ReceptionistDBConnection;
import hms.reception.database.TokenDBConnection;
import hms.reports.gui.DateSelection;
import hms1.ipd.gui.DialysisIPD;
import hms1.ipd.gui.DialysisIpdBrowser;
import hms1.ipd.gui.EmergencyIpd;
import hms1.ipd.gui.EmergencyIpdBrowser;
import hms1.ipd.gui.IPDBill;
import hms1.ipd.gui.IPDBrowser;
import hms1.ipd.gui.IPDEntery;
import hms1.ipd.gui.ProcedureIPD;
import hms1.ipd.gui.ProcedureIpdBrowser;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.border.LineBorder;

import com.barcodelib.barcode.a.g.c.e;

import LIS_UI.LIS_System;
import UsersActivity.database.UADBConnection;

public class ReceptionMain<EmergencyIPD> extends JFrame {

	private JPanel contentPane;
	OPDBrowser opdBrowser;
	ExamsBrowser examsBrowser;
	PatientBrowser patientBrowser;
	IPDBrowser ipdBrowser;
	private JLabel receptionIDLB;
	private JLabel receptionNameLB;
	private JLabel lastLoginLB;
	public static String receptionIdSTR = "";
	public static String receptionNameSTR = "",receptionShift="";
	private JPanel shortCutsPanel;
	Front front;
	public static boolean tokenCounterFlag = false;
	private JComboBox counterComboBox;
	UADBConnection ua=new UADBConnection();

	final DefaultComboBoxModel countersModel = new DefaultComboBoxModel();
	Vector<String> countersID = new Vector<String>();
	public static String _id = "0";
	public static String Counter = "0";
	public static String userName = "";
	ALwaysFront aLwaysFront;
	String receptionTokenCounter="NO";
	public static boolean insBillAccess;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ReceptionMain frame = new ReceptionMain("test");
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
	public ReceptionMain(final String username) {
		setResizable(false);
		setTitle("Menu");
		userName=username;
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ReceptionMain.class.getResource("/icons/rotaryLogo.png")));
		setBackground(new Color(32, 178, 170));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setBounds(10, 10, width - 20, height - 60);
		KeyboardFocusManager manager = KeyboardFocusManager
				.getCurrentKeyboardFocusManager();

		contentPane = new JPanel();

		// setExtendedState(JFrame.MAXIMIZED_BOTH);
		setVisible(true);
	
	
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 102, 232, 503);

		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(20);
		contentPane.setLayout(null);
		contentPane.add(scrollPane);
		JPanel panel = new JPanel();
		scrollPane.setViewportView(panel);
		panel.setBackground(SystemColor.control);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setLayout(null);
		panel.setLayout(null);

		JButton opdBT = new JButton("OPD");
		opdBT.setBounds(10, 61, 199, 39);
		panel.add(opdBT);
		panel.setPreferredSize(new Dimension(100, 400));
		opdBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				opdBrowser = new OPDBrowser(username);
				opdBrowser.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				opdBrowser.setLocationRelativeTo(contentPane);
				opdBrowser.setVisible(true);

			}
		});
		opdBT.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton examBT = new JButton("EXAMS");
		examBT.setBounds(10, 111, 199, 39);
		panel.add(examBT);
		examBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				LIS_System window=new LIS_System(username);
				window.setVisible(true);

			}
		});
		examBT.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton patientsBT = new JButton("Patients");
		patientsBT.setBounds(10, 211, 199, 39);
		panel.add(patientsBT);
		patientsBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patientBrowser = new PatientBrowser();
				patientBrowser
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				patientBrowser.setLocationRelativeTo(contentPane);
				patientBrowser.setVisible(true);
				ua.check_activity(username, 126, 0);

			}
		});
		patientsBT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		JButton exit = new JButton("Exit");
		exit.setBounds(10, 418, 199, 39);
		panel.add(exit);
		exit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				aLwaysFront.setVisible(false);
				new MainLogin().setVisible(true);
			}
		});
		exit.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton btnMiscCharges = new JButton("MISC Charges");
		btnMiscCharges.setBounds(10, 261, 199, 39);
		panel.add(btnMiscCharges);
		btnMiscCharges.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

//				MiscAmountEntery ds = new MiscAmountEntery();
				MISCBrowser ds = new MISCBrowser(username);
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		btnMiscCharges.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton btnDailyCollection = new JButton("Daily Collection");
		btnDailyCollection.setBounds(10, 312, 199, 39);
		panel.add(btnDailyCollection);
		btnDailyCollection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DailyCash ds = new DailyCash();
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
				ua.check_activity(username, 128, 0);
			}
		});
		btnDailyCollection.setFont(new Font("Tahoma", Font.PLAIN, 20));

		JButton ipdBT = new JButton("IPD");
		ipdBT.setBounds(10, 161, 199, 39);
		ipdBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

			
				ipdBrowser = new IPDBrowser(username);
				ipdBrowser
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser.setLocationRelativeTo(contentPane);
				ipdBrowser.setVisible(true);
			}
		});
		ipdBT.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(ipdBT);

		final JButton btnShortcuts = new JButton("Hide Shortcuts");
		btnShortcuts.setBounds(10, 11, 199, 39);
		btnShortcuts.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (btnShortcuts.getText().equals("Show Shortcuts")) {
					shortCutsPanel.setVisible(true);
					btnShortcuts.setText("Hide Shortcuts");
				} else {
					btnShortcuts.setText("Show Shortcuts");
					shortCutsPanel.setVisible(false);
				}

			}
		});
		btnShortcuts.setFont(new Font("Tahoma", Font.PLAIN, 20));
		panel.add(btnShortcuts);
		
		JButton btnDfg = new JButton("Payment Track");
		btnDfg.setBounds(10, 367, 199, 39);
		btnDfg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InsurancePayments obj=new InsurancePayments("", "");
				obj.setVisible(true);
				obj.setModal(true);
				
			}
		});
		btnDfg.setFont(new Font("Dialog", Font.PLAIN, 20));
		panel.add(btnDfg);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(30, 684, 313, 59);
		panel_1.setBackground(SystemColor.control);
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "News", TitledBorder.RIGHT,

		TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		contentPane.add(panel_1);
		
		NewsDBConnection newsDBConnection = new NewsDBConnection();
		JLabel newsLB = new MarqueeLabel(newsDBConnection.getNews(),
				MarqueeLabel.RIGHT_TO_LEFT, 20);
		newsDBConnection.closeConnection();
		newsLB.setForeground(Color.RED);
		newsLB.setFont(new Font("Tahoma", Font.BOLD, 14));
		newsLB.setBounds(10, 21, 293, 40);
		panel_1.add(newsLB);

		JLabel lblReceptionistId = new JLabel("Receptionist ID :");
		lblReceptionistId.setBounds(308, 217, 167, 20);
		lblReceptionistId.setForeground(new Color(0, 128, 128));
		lblReceptionistId.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblReceptionistId);

		receptionIDLB = new JLabel("New label");
		receptionIDLB.setBounds(500, 217, 281, 20);
		receptionIDLB.setForeground(new Color(0, 128, 128));
		receptionIDLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(receptionIDLB);

		receptionNameLB = new JLabel("New label");
		receptionNameLB.setBounds(500, 241, 286, 20);
		receptionNameLB.setForeground(new Color(0, 128, 128));
		receptionNameLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(receptionNameLB);

		JLabel lblReceptionistName = new JLabel("Receptionist Name :");
		lblReceptionistName.setBounds(308, 241, 167, 20);
		lblReceptionistName.setForeground(new Color(0, 128, 128));
		lblReceptionistName.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lblReceptionistName);

		JLabel label_5 = new JLabel("Last Login :");
		label_5.setBounds(308, 263, 117, 21);
		label_5.setForeground(new Color(0, 128, 128));
		label_5.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(label_5);

		lastLoginLB = new JLabel("");
		lastLoginLB.setBounds(500, 263, 270, 20);
		lastLoginLB.setForeground(new Color(0, 128, 128));
		lastLoginLB.setFont(new Font("Tahoma", Font.PLAIN, 16));
		contentPane.add(lastLoginLB);

		JPanel panel_3 = new JPanel();
		panel_3.setBounds(10, 25, 1312, 66);
		panel_3.setLayout(null);
		panel_3.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null,

		null, null));
		contentPane.add(panel_3);

		JLabel label_3 = new JLabel(
				" Rotary Ambala Cancer and General Hospital (Ambala Cantt.)");
		label_3.setForeground(Color.BLUE);
		label_3.setFont(new Font("Tahoma", Font.BOLD, 18));
		label_3.setBounds(73, 18, 635, 28);
		panel_3.add(label_3);

		JLabel label_4 = new JLabel("");
		label_4.setIcon(new ImageIcon(ReceptionMain.class
				.getResource("/icons/smallLogo.png")));
		label_4.setBounds(10, 11, 53, 44);
		panel_3.add(label_4);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(893, 11, 379, 44);
		panel_3.add(panel_2);
		panel_2.add(new RealTimeClock());

		shortCutsPanel = new JPanel();
		shortCutsPanel.setBounds(275, 308, 611, 155);
		contentPane.add(shortCutsPanel);
		shortCutsPanel.setLayout(null);

		JButton btnNewButton = new JButton("New OPD");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (ReceptionMain._id.equals("0")) {
					OPDEntery opdEntery = new OPDEntery(null);
					opdEntery.setVisible(true);
				} else {
					TokenEntry tokenEntry = new TokenEntry(null);
					tokenEntry.setModal(true);
					tokenEntry.setVisible(true);
				}
			}
		});
		btnNewButton.setBounds(20, 11, 126, 63);
		shortCutsPanel.add(btnNewButton);
		shortCutsPanel.setVisible(true);

		JButton btnNewIpd = new JButton("New IPD");
		btnNewIpd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				IPDEntery ipdEntery = new IPDEntery(null);
				ipdEntery.setVisible(true);
			}
		});
		btnNewIpd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnNewIpd.setBounds(158, 11, 140, 63);
		shortCutsPanel.add(btnNewIpd);

		JButton btnMiscSlip = new JButton("Misc Slip");
		btnMiscSlip.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				MiscAmountEntery miscCharges = new MiscAmountEntery();
				miscCharges.setVisible(true);
			}
		});
		btnMiscSlip.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnMiscSlip.setBounds(158, 86, 136, 63);
		shortCutsPanel.add(btnMiscSlip);
		
		JButton btnOnlineOpd = new JButton("ONLINE OPD");
		btnOnlineOpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				OnlineOPDBrowser miscCharges = new OnlineOPDBrowser();
				miscCharges.setVisible(true);
			}
		});
		btnOnlineOpd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnOnlineOpd.setBounds(454, 86, 145, 63);
		shortCutsPanel.add(btnOnlineOpd);
		
		JButton EmergencyIPD = new JButton("Emergency IPD");
		EmergencyIPD.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				EmergencyIPD emripd=(EmergencyIPD) new EmergencyIpd(null);
				((Dialog) emripd).setVisible(true);
			}
		});
		EmergencyIPD.setFont(new Font("Tahoma", Font.PLAIN, 15));
		EmergencyIPD.setBounds(306, 11, 145, 63);
		shortCutsPanel.add(EmergencyIPD);
		
		JButton procedureipd = new JButton("Procedure IPD");
		procedureipd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ProcedureIPD procipd=(ProcedureIPD) new ProcedureIPD(null);
				((Dialog) procipd).setVisible(true);
			}
		});
		procedureipd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		procedureipd.setBounds(461, 11, 138, 63);
		shortCutsPanel.add(procedureipd);
		
		JButton btnDialysisIpd = new JButton("Dialysis IPD");
		btnDialysisIpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DialysisIPD dialyipd=(DialysisIPD) new DialysisIPD(null);
				((Dialog) dialyipd).setVisible(true);
			}
		});
		btnDialysisIpd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDialysisIpd.setBounds(302, 86, 141, 63);
		shortCutsPanel.add(btnDialysisIpd);
		
		JButton btnOpdExam = new JButton("OPD EXAM");
		btnOpdExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ExamEntery opdEntery = new ExamEntery();
				opdEntery.setVisible(true);
			}
		});
		btnOpdExam.setFont(new Font("Dialog", Font.PLAIN, 15));
		btnOpdExam.setBounds(20, 86, 126, 63);
		shortCutsPanel.add(btnOpdExam);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1405, 21);
		contentPane.add(menuBar);

		JMenu mnMyAccount = new JMenu("My Account");
		menuBar.add(mnMyAccount);

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ReceptionistSettings receptionistSettings = new ReceptionistSettings(
						username);
				receptionistSettings.setModal(true);
				receptionistSettings.setVisible(true);
			}
		});
		mnMyAccount.add(mntmChangePassword);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				aLwaysFront.setVisible(false);
				new MainLogin().setVisible(true);
			}
		});
		mnMyAccount.add(mntmLogout);

		JMenu mnMyReport = new JMenu("My Report");
		menuBar.add(mnMyReport);

		JMenuItem mntmMyCollection = new JMenuItem("My Collection");
		mntmMyCollection.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection = new DateSelection(6);
				dateSelection.setVisible(true);
				ua.check_activity(username,112,0);
			}
		});
		mnMyReport.add(mntmMyCollection);
		
		JMenu mnIpdBilling = new JMenu("IPD Billing");
		menuBar.add(mnIpdBilling);
		
		JMenuItem mntmEsiBill = new JMenuItem("ESI Bill");
		mntmEsiBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PatientHistoryESI patientHistory = new PatientHistoryESI();
				patientHistory.setModal(true);
				patientHistory.setVisible(true);
				ua.check_activity(username,113,0);
				
			}
		});
		mnIpdBilling.add(mntmEsiBill);
		
		JMenuItem mntmEchsBill = new JMenuItem("ECHS Bill");
		mntmEchsBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PatientHistoryECHS patientHistory = new PatientHistoryECHS();
				patientHistory.setModal(true);
				patientHistory.setVisible(true);
				ua.check_activity(username,114,0);
				
			}
		});
		mnIpdBilling.add(mntmEchsBill);
		
		JMenuItem mntmRailwayBill = new JMenuItem("Railway Bill");
		mntmRailwayBill.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PatientHistoryRailway patientHistory = new PatientHistoryRailway();
				patientHistory.setModal(true);
				patientHistory.setVisible(true);
				ua.check_activity(username,115,0);
			}
		});
		mnIpdBilling.add(mntmRailwayBill);

		JMenu mnAbout = new JMenu("About");
		menuBar.add(mnAbout);

		JMenuItem mntmAboutHms = new JMenuItem("About HMS");
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				AboutHMS aboutHMS = new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);

			}
		});
		mnAbout.add(mntmAboutHms);

		JButton btnPatientHistory = new JButton("Patient History");
		btnPatientHistory.setBounds(275, 466, 205, 42);
		btnPatientHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PatientHistory patientHistory = new PatientHistory("");
				patientHistory.setModal(true);
				patientHistory.setVisible(true);
				ua.check_activity(username,129,0);
			}
		});
		btnPatientHistory.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(btnPatientHistory);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(298, 102, 489, 104);
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Token System",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		counterComboBox = new JComboBox();
		counterComboBox.setFont(new Font("Tahoma", Font.BOLD, 12));
		counterComboBox.setBounds(10, 24, 343, 20);

		counterComboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					_id = countersID.get(counterComboBox.getSelectedIndex());

				} catch (Exception e) {
					// TODO: handle exception
				}
				
				if(!_id.equals("0"))
				{
					if(!aLwaysFront.isVisible())
					{
						aLwaysFront =new ALwaysFront();
						aLwaysFront.setVisible(true);
					}
					
				}else {
					aLwaysFront.setVisible(false);
				}
			}
		});
		panel_4.add(counterComboBox);
		aLwaysFront =new ALwaysFront();
		JButton btnStart = new JButton("Start Token Screen");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				if (!_id.equals("0")) {
					if(!aLwaysFront.isVisible())
						aLwaysFront =new ALwaysFront();
						aLwaysFront.setVisible(true);

				} else {
					JOptionPane
							.showMessageDialog(null, "Please Select Counter");
				}

			}
		});
		btnStart.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnStart.setBounds(10, 52, 226, 41);
		panel_4.add(btnStart);

		JButton btnIncrementTokenNumber = new JButton("Increment Token  Number");
		btnIncrementTokenNumber.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (!_id.equals("0")) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							ReceptionMain.this,
							"Are you sure to increment token Number", "Alert",
							dialogButton);
					if (dialogResult == 0) {

						TokenDBConnection connection = new TokenDBConnection();
						try {
							connection.unReported(
									ReceptionMain._id);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} finally {
							connection.closeConnection();

						}

					}
				}
				else {
					JOptionPane
							.showMessageDialog(null, "Please Select Counter");
				}
			}
		});
		btnIncrementTokenNumber.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnIncrementTokenNumber.setBounds(246, 52, 226, 41);
		panel_4.add(btnIncrementTokenNumber);
		
		JButton emergencyIpdMgr = new JButton("EMG IPD Manager");
		emergencyIpdMgr.setBounds(484, 466, 241, 42);
		emergencyIpdMgr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			EmergencyIpdBrowser	ipdBrowser1 = new EmergencyIpdBrowser();
			ipdBrowser1
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			ipdBrowser1.setLocationRelativeTo(contentPane);
			ipdBrowser1.setVisible(true);
			ua.check_activity(username,130,0);
			}
		});
		emergencyIpdMgr.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(emergencyIpdMgr);
		
		JButton btnProcedureIpdManager = new JButton("Procedure IPD Manager");
		btnProcedureIpdManager.setBounds(733, 466, 251, 42);
		btnProcedureIpdManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			ProcedureIpdBrowser	ipdBrowser1 = new ProcedureIpdBrowser();
			ipdBrowser1
						.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			ipdBrowser1.setLocationRelativeTo(contentPane);
			ipdBrowser1.setVisible(true);
			ua.check_activity(username,131,0);
			}
		});
		btnProcedureIpdManager.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(btnProcedureIpdManager);
		
		JButton buttonDialysisMgr = new JButton("Dialysis IPD Manager");
		buttonDialysisMgr.setBounds(276, 520, 251, 42);
		buttonDialysisMgr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DialysisIpdBrowser	ipdBrowser1 = new DialysisIpdBrowser();
				ipdBrowser1
							.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser1.setLocationRelativeTo(contentPane);
				ipdBrowser1.setVisible(true);
				ua.check_activity(username,132,0);
			}
		});
		buttonDialysisMgr.setFont(new Font("Tahoma", Font.BOLD, 18));
		contentPane.add(buttonDialysisMgr);
		
		JButton buttonDialysisMgr_1 = new JButton("Machine Transactions");
		buttonDialysisMgr_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PaymentTransactionHistory PaymentTransactionHistory=new PaymentTransactionHistory();
				PaymentTransactionHistory.setVisible(true);
				PaymentTransactionHistory.setModal(true);
				
			}
		});
		buttonDialysisMgr_1.setBackground(new Color(173, 216, 230));
		buttonDialysisMgr_1.setForeground(new Color(0, 0, 139));
		buttonDialysisMgr_1.setFont(new Font("Dialog", Font.BOLD, 18));
		buttonDialysisMgr_1.setBounds(539, 520, 270, 42);
		contentPane.add(buttonDialysisMgr_1);
		
		JButton buttonDialysisMgr_2 = new JButton("Free Test Camp");
		buttonDialysisMgr_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FreeCampTests frame = new FreeCampTests(username);
				frame.setModal(true);
				frame.setVisible(true);
			}
		});
		buttonDialysisMgr_2.setFont(new Font("Dialog", Font.BOLD, 18));
		buttonDialysisMgr_2.setBounds(821, 520, 251, 42);
		contentPane.add(buttonDialysisMgr_2);
		
		JButton btnFinancialCouncling = new JButton("Financial Councling");
		btnFinancialCouncling.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FinancialCouncling financialCouncling = new FinancialCouncling();
				financialCouncling.setModal(true);
				financialCouncling.setVisible(true);
			}
		});
		btnFinancialCouncling.setFont(new Font("Dialog", Font.BOLD, 18));
		btnFinancialCouncling.setBounds(992, 466, 251, 42);
		contentPane.add(btnFinancialCouncling);

		getOperatorDetail(username);

		getAllCounters();
	}

	public void getOperatorDetail(String username) {
		ReceptionistDBConnection dbConnection = new ReceptionistDBConnection();
		ResultSet resultSet = dbConnection.retrieveUsernameDetail(username);
		try {
			while (resultSet.next()) {
				receptionIdSTR=resultSet.getObject(1).toString();
				receptionIDLB.setText(receptionIdSTR);
				receptionNameSTR = resultSet.getObject(2).toString();
				receptionNameLB.setText(receptionNameSTR);
				lastLoginLB.setText(resultSet.getObject(3).toString());
				receptionTokenCounter=resultSet.getObject(4).toString();
				receptionShift=resultSet.getObject(5).toString();
				insBillAccess=resultSet.getBoolean(6);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dbConnection
					.updateDataLastLogin(receptionIDLB.getText().toString());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getAllCounters() {
		countersID.clear();
		if(receptionTokenCounter.equals("YES"))
		{
			countersID.add("0");
			countersModel.addElement("Without Token");
		}
		
		countersID.add("1");
		countersModel.addElement("Counter 1");
		countersID.add("2");
		countersModel.addElement("Counter 2");
		countersID.add("3");
		countersModel.addElement("Counter 3");

		counterComboBox.setModel(countersModel);
		counterComboBox.setSelectedIndex(0);
	}
}
