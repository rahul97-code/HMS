package hms.departments.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.exams.gui.ExamsBrowserIPD;
import hms.main.AboutHMS;
import hms.main.MainLogin;
import hms.reception.gui.ReceptionMain;
import hms1.ipd.gui.IPDChangeBed;
import hms1.ipd.gui.IPDOTDetails;
import hms1.ipd.gui.IPDOTExamEntry;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
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

public class DepartmentMain extends JFrame {

	private JPanel contentPane;

	public static String departmentID = "";
	public static String departmentName = "";
	public static String userName = "";
	public static String userID = "";
	/** 
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					DepartmentMain frame = new DepartmentMain("", "sukhpal","1");
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
	public DepartmentMain(String dept, String userName, String userID) {

		departmentName = dept;
		getDepartmentsDetail(departmentName);
		DepartmentMain.userName = userName;
		
		DepartmentMain.userID = userID;
		ReceptionMain.receptionNameSTR=userName;
		ReceptionMain.receptionIdSTR=userID;
		setTitle(departmentName + " (" + userName + ")");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DepartmentMain.class.getResource("/icons/rotaryLogo.png")));
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
		mnNewMenu.setIcon(new ImageIcon(DepartmentMain.class
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
		mntmLogout.setIcon(new ImageIcon(DepartmentMain.class
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

		JMenu mnPillsRequest = new JMenu("Pills Request");
		mnPillsRequest.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnPillsRequest);

		JMenu menu_2 = new JMenu("Indoor Patients");
		menu_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPillsRequest.add(menu_2);

		JMenuItem menuItem_2 = new JMenuItem("New Entry");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				IndoorPillsRequest indoorPillsRequest = new IndoorPillsRequest();
				indoorPillsRequest.setModal(true);
				indoorPillsRequest.setVisible(true);
			}
		});
		menuItem_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		menu_2.add(menuItem_2);
		
		JMenuItem mntmMsIndoorPills = new JMenuItem("MS Indoor Pills Request ");
		mntmMsIndoorPills.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IndoorPillsRequestMedicalStore indoorPillsRequest = new IndoorPillsRequestMedicalStore();
				indoorPillsRequest.setModal(true);
				indoorPillsRequest.setVisible(true);
			}
		});
		mntmMsIndoorPills.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnPillsRequest.add(mntmMsIndoorPills);
		
		JMenu mnPatientTest = new JMenu("Patient Exams");
		mnPatientTest.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnPatientTest);
		
		JMenuItem mntmTestHistory = new JMenuItem("Test History");
		mntmTestHistory.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IndoorTestHistory patientTest=new IndoorTestHistory();
				patientTest.setModal(true);
				patientTest.setVisible(true);
			}
		});
		JMenuItem mntmNewTestEntry = new JMenuItem("New Exam Entry");
		mntmNewTestEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExamsBrowserIPD examsBrowserIPD=new ExamsBrowserIPD();
				examsBrowserIPD.setModal(true);
				examsBrowserIPD.setVisible(true);
			}
		});
		mntmNewTestEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPatientTest.add(mntmNewTestEntry);
		mnPatientTest.add(mntmTestHistory);
		mntmTestHistory.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JMenu mnIndoorPatient = new JMenu("Indoor Patient");
		mnIndoorPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnIndoorPatient);
		
		JMenuItem mntmChangeBed = new JMenuItem("Change Bed");
		mntmChangeBed.setEnabled(false);
		mntmChangeBed.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				IPDChangeBed chengBed=new IPDChangeBed();
				chengBed.setVisible(true);
			}
		});
		mntmChangeBed.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndoorPatient.add(mntmChangeBed);
		
		JMenuItem mntmOperationTheatreTime = new JMenuItem("Operation Theatre Time");
		mntmOperationTheatreTime.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDOTExamEntry chengBed=new IPDOTExamEntry(null);
				chengBed.setVisible(true);
			}
		});
		mntmOperationTheatreTime.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndoorPatient.add(mntmOperationTheatreTime);
		
		JMenuItem mntmReturnStatus = new JMenuItem("Return Status");
		mntmReturnStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IndoorPillsReturnStatus o=new IndoorPillsReturnStatus();
				o.setModal(true);
				o.setVisible(true);
			}
		});
		mntmReturnStatus.setFont(new Font("Dialog", Font.BOLD, 14));
		mnIndoorPatient.add(mntmReturnStatus);

		JMenu mnOutdoorPatient = new JMenu("Pills Entry");
		mnOutdoorPatient.setIcon(new ImageIcon(DepartmentMain.class
				.getResource("/icons/medical_dialog.png")));
		mnOutdoorPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnOutdoorPatient);

		JMenu mnOutdoorPatients = new JMenu("Outdoor Patients");
		mnOutdoorPatients.setIcon(new ImageIcon(DepartmentMain.class
				.getResource("/icons/emp.png")));
		mnOutdoorPatients.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mnOutdoorPatients);

		JMenuItem mntmNewEntry = new JMenuItem("New Entry");
		mntmNewEntry.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				OutdoorPillsEntry outdoorPillsEntry = new OutdoorPillsEntry("department");
				outdoorPillsEntry.setModal(true);
				outdoorPillsEntry.setVisible(true);
			}
		});
		mntmNewEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatients.add(mntmNewEntry);

		JMenuItem mntmNewMenuItem = new JMenuItem("Previouse Entries");
		mntmNewMenuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatients.add(mntmNewMenuItem);
		
		JMenuItem mntmMedicineBillRequest = new JMenuItem("Medicine Bill Request");
		mntmMedicineBillRequest.setEnabled(false);
		mntmMedicineBillRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OutdoorPillsPerforma outdoorPillsEntry = new OutdoorPillsPerforma();
				outdoorPillsEntry.setModal(true);
				outdoorPillsEntry.setVisible(true);
			}
		});
		
		JMenuItem menuItem = new JMenuItem("Procedure Entry");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OutdoorProcedureEntry outdoorProcedureEntry = new OutdoorProcedureEntry();
				outdoorProcedureEntry.setModal(true);
				outdoorProcedureEntry.setVisible(true);
			}
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatients.add(menuItem);
		mntmMedicineBillRequest.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatients.add(mntmMedicineBillRequest);

		JMenu mnIndoorPatients = new JMenu("Indoor Patients");
		mnIndoorPatients.setIcon(new ImageIcon(DepartmentMain.class
				.getResource("/icons/emp.png")));
		mnIndoorPatients.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mnIndoorPatients);

		JMenuItem mntmNewEntry_1 = new JMenuItem("New Entry");
		mntmNewEntry_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				IndoorPillsEntry indoorPillsEntry = new IndoorPillsEntry();
				indoorPillsEntry.setModal(true);
				indoorPillsEntry.setVisible(true);
			}
		});
		mntmNewEntry_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndoorPatients.add(mntmNewEntry_1);
		
		JMenuItem mntmProcedureEntry = new JMenuItem("Procedure Entry");
		mntmProcedureEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IndoorProcedureEntry indoorProcedureEntry = new IndoorProcedureEntry();
				indoorProcedureEntry.setModal(true);
				indoorProcedureEntry.setVisible(true);
			}
		});
		mntmProcedureEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndoorPatients.add(mntmProcedureEntry);

		JMenuItem mntmNewMenuItem_1 = new JMenuItem("Previouse Entries");
		mntmNewMenuItem_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIndoorPatients.add(mntmNewMenuItem_1);
		
		JMenuItem mntmIssueToPatient = new JMenuItem("Issue To Patient From MS");
		mntmIssueToPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IndoorPillsEntryfromMS IndoorPillsEntryfromMS = new IndoorPillsEntryfromMS();
				IndoorPillsEntryfromMS.setModal(true);
				IndoorPillsEntryfromMS.setVisible(true);
			}
		});
		mntmIssueToPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnIndoorPatients.add(mntmIssueToPatient);

		JMenu mnMyStock = new JMenu("My Stock");
		mnMyStock.setIcon(new ImageIcon(DepartmentMain.class
				.getResource("/icons/stockicon.png")));
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnMyStock);

		JMenuItem mntmStockRegister = new JMenuItem("Stock Register");
		mntmStockRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DepartmentStock departmentStock = new DepartmentStock();
				departmentStock.setModal(true);
				departmentStock.setVisible(true);
			}
		});
		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmStockRegister);
		
		JMenuItem mntmBedDetails = new JMenuItem("Bed Details");
		mntmBedDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bed_details window = new bed_details(departmentName);
				window.setVisible(true);
			}
		});
		mntmBedDetails.setFont(new Font("Dialog", Font.BOLD, 14));
		mnMyStock.add(mntmBedDetails);

		JMenu mnPillsReminder = new JMenu("Pills Reminder");
		mnPillsReminder.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnPillsReminder.setIcon(new ImageIcon(DepartmentMain.class
				.getResource("/icons/remindericon.png")));
		menuBar.add(mnPillsReminder);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}

	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentID = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

}