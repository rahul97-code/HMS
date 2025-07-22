package hms.admin.gui;
import hms.accounts.gui.AccountsUserBrowser;
import hms.accounts.gui.NewAccountsUser;
import hms.cancellation.gui.CancelExamSlip;
import hms.cancellation.gui.CancelIndoorExam;
import hms.cancellation.gui.CancelMiscSlip;
import hms.cancellation.gui.Cancellation;
import hms.departments.gui.DepartmentItemProfile;
import hms.departments.gui.NewDepartment;
import hms.doctor.gui.DoctorAvailability;
import hms.doctor.gui.DoctorAvailabilityCencel;
import hms.doctor.gui.DoctorBrowser;
import hms.doctor.gui.NewDoctor;
import hms.emailreports.gui.EmailReports;
import hms.exams.gui.AllExamsDetail;
import hms.exams.gui.ExamsBrowser;
import hms.insurance.gui.AddInsuranceType;
import hms.insurance.gui.insuranse_item_status;
import hms.main.AboutHMS;
import hms.main.ChangeMessage;
import hms.main.MainLogin;
import hms.main.MarqueeLabel;
import hms.main.NewsDBConnection;
import hms.main.RealTimeClock;
import hms.misc.gui.MISCBrowser;
import hms.misc.gui.MiscAmountEntery;
import hms.mrd.gui.MrdUserBrowser;
import hms.mrd.gui.NewMrdUser;
import hms.opd.gui.OPDBrowser;
import hms.patient.gui.PatientBrowser;
import hms.reception.gui.NewReceptionist;
import hms.reception.gui.PatientWardVisit;
import hms.reception.gui.ReceptionistBrowser;
import hms.reports.excels.ItemRateListReportExcel;
import hms.reports.excels.PurchaseOrderExcel;
import hms.reports.gui.ConservativeSheet;
import hms.reports.gui.DateSelection;
import hms.reports.gui.DoctorsCostCenter;
import hms.reports.gui.DoctorsReport;
import hms.reports.gui.InsuranceWiseReport;
import hms.reports.gui.OtShareSheet;
import hms.reporttables.DepartmentIssueRegisterReport;
import hms.reporttables.DepartmentToPatientLog;
import hms.reporttables.DeptTopItemsReport;
import hms.reporttables.ExamsDoneReport;
import hms.reporttables.InvoiceItemsRegisterReport;
import hms.reporttables.ItemConsumedReport;
import hms.reporttables.MainStoreTopatientReport;
import hms.reporttables.PurchaseOrderReport;
import hms.reporttables.StoreTopItemsReport;
import hms.store.gui.BatchManagement;
import hms.store.gui.ExamBomForm;
import hms.store.gui.ExamBomReport;
import hms.store.gui.IndoorPillsEntryFromStore;
import hms.store.gui.IndoorPillsReturnFromStore;
import hms.store.gui.InvoiceBrowser;
import hms.store.gui.ItemBrowser;
import hms.store.gui.ItemIssueLog;
import hms.store.gui.ItemIssueLogNEW;
import hms.store.gui.NewInvoice;
import hms.store.gui.NewIssuedForm;
import hms.store.gui.NewItem;
import hms.store.gui.NewPO;
import hms.store.gui.NewProcedsuresForm;
import hms.store.gui.NewReturnForm;
import hms.store.gui.NewReturnInvoice;
import hms.store.gui.NewReturnItemsForm;
import hms.store.gui.NewStoreAccount;
import hms.store.gui.NewSupplier;
import hms.store.gui.OpeningStockForm;
import hms.store.gui.POBrowser;
import hms.store.gui.QuotationPrice;
import hms.store.gui.RequestRegister;
import hms.store.gui.ReturnInvoiceBrowser;
import hms.store.gui.StockAdjustment;
import hms.store.gui.StoreAccountBrowser;
import hms.store.gui.StoreMain;
import hms.store.gui.SupplierBrowser;
import hms.store.gui.TotalStock;
import hms.store.gui.TransferStock;
import hms.sukhpal.updater.NewUpdates;
import hms.test.free_test.FreeTestData;
import hms.test.gui.NewOperator;
import hms.test.gui.OperatorBrowser;
import hms1.ipd.gui.CreateIPDPackage;
import hms1.ipd.gui.DialysisIpdBrowser;
import hms1.ipd.gui.EmergencyIpdBrowser;
import hms1.ipd.gui.IPDBrowser;
import hms1.ipd.gui.ProcedureIpdBrowser;
import UsersActivity.database.*;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

import com.itextpdf.text.DocumentException;

import LIS_System.LIS_Cancel;
import LIS_UI.LIS_Exam_Cancellation;

import java.awt.SystemColor;
import java.io.IOException;

public class AdminMain extends JFrame {

	private JPanel contentPane;
	UADBConnection ua=new UADBConnection();
	public static String username="mdi";
	public static String acess="";
	private String exam_open_password=""; //***************
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					AdminMain frame = new AdminMain("admin","1");
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
	public AdminMain(final String userName,String acess) {
		setTitle("Admin Login");

		username=userName;
		StoreMain.userName=userName;
		StoreMain.access=acess;
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AdminMain.class.getResource("/icons/rotaryLogo.png")));
		setBackground(new Color(32, 178, 170));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		setBounds(10, 10, width - 20, height - 60);
		contentPane = new JPanel();
		setVisible(true);
		setResizable(false);
		AdminDBConnection db = new AdminDBConnection();
		try {
			db.updateDataLastLogn(userName);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		db.closeConnection();

		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.control);
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "News", TitledBorder.RIGHT,

				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_1.setBounds(867, 127, 463, 72);
		contentPane.add(panel_1);

		NewsDBConnection newsDBConnection = new NewsDBConnection();
		JLabel newsLB = new MarqueeLabel(newsDBConnection.getNews(),
				MarqueeLabel.RIGHT_TO_LEFT, 20);
		newsDBConnection.closeConnection();
		newsLB.setForeground(Color.RED);
		newsLB.setFont(new Font("Tahoma", Font.BOLD, 14));
		newsLB.setBounds(10, 21, 443, 40);
		panel_1.add(newsLB);

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 1340, 43);
		contentPane.add(menuBar);

		JMenu mnMyAccount = new JMenu("My Account");
		mnMyAccount.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/employee.png")));
		mnMyAccount.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnMyAccount);

		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AdminSettings ds = new AdminSettings(userName);
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
			}
		});
		mntmChangePassword.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/SECURITY.PNG")));
		mntmChangePassword.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmChangePassword);

		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				dispose();
				MainLogin mainLogin = new MainLogin();
				mainLogin.setVisible(true);
			}
		});
		mntmLogout.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/hoverOver_close_tab.JPG")));
		mntmLogout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmLogout);

		JMenuItem mntmAbout = new JMenuItem("About HMS");
		mntmAbout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AboutHMS aboutHMS = new AboutHMS();
				aboutHMS.setModal(true);
				aboutHMS.setVisible(true);
			}
		});
		mntmAbout.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnMyAccount.add(mntmAbout);

		JMenu mnSukhpal = new JMenu("Manage Accounts");
		mnSukhpal.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/emp.png")));
		mnSukhpal.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnSukhpal);

		JMenu mnDoctor = new JMenu("Doctor");
		mnDoctor.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnDoctor.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnDoctor);

		JMenuItem mntmNewDoctor = new JMenuItem("New Doctor");
		mntmNewDoctor.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NewDoctor newDoctor = new NewDoctor();
				newDoctor.setVisible(true);
				ua.check_activity(userName,0,3);
			}
		});
		mntmNewDoctor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDoctor.add(mntmNewDoctor);

		JMenuItem mntmManadeDoctors = new JMenuItem("Manage Doctors");
		mntmManadeDoctors.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DoctorBrowser doctorBrowser = new DoctorBrowser();
				doctorBrowser.setVisible(true);
				ua.check_activity(userName,1,3);
			}
		});
		mntmManadeDoctors.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDoctor.add(mntmManadeDoctors);

		JMenuItem mntmDoctorAvailability = new JMenuItem("Doctor Availability");
		mntmDoctorAvailability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DoctorAvailability doctorAvailability = new DoctorAvailability();
				doctorAvailability.setVisible(true);
				ua.check_activity(userName,2,3);

			}
		});
		mntmDoctorAvailability.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDoctor.add(mntmDoctorAvailability);

		JMenuItem mntmCancelDoctorAvailability = new JMenuItem("Cancel Doctor Availability");
		mntmCancelDoctorAvailability.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DoctorAvailabilityCencel doctorAvailabilityCencel = new DoctorAvailabilityCencel();
				doctorAvailabilityCencel.setVisible(true);
				ua.check_activity(userName,3,3);
			}
		});
		mntmCancelDoctorAvailability.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDoctor.add(mntmCancelDoctorAvailability);

		JMenu mnSalaries = new JMenu("Salaries");
		mnSalaries.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnSalaries.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnSalaries);

		JMenuItem mntmAddSalary = new JMenuItem("Add Salary");
		mntmAddSalary.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				AddSalary addSalary = new AddSalary();
				addSalary.setVisible(true);
				ua.check_activity(userName,4,3);
			}
		});
		mntmAddSalary.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnSalaries.add(mntmAddSalary);

		JMenu mnLabOperator = new JMenu("Lab Operator");
		mnLabOperator.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnLabOperator.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnLabOperator);

		JMenuItem mntmNewOperator = new JMenuItem("New Operator");
		mntmNewOperator.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NewOperator newOperator = new NewOperator();
				newOperator.setVisible(true);
				ua.check_activity(userName,5,3);
			}
		});
		mntmNewOperator.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnLabOperator.add(mntmNewOperator);

		JMenuItem mntmManageOperators = new JMenuItem("Manage Operators");
		mntmManageOperators.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				OperatorBrowser operatorBrowser = new OperatorBrowser();
				operatorBrowser.setVisible(true);
				ua.check_activity(userName,6,3);
			}
		});
		mntmManageOperators.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnLabOperator.add(mntmManageOperators);

		JMenu mnReception = new JMenu("Receptionist");
		mnReception.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnReception.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnReception);

		JMenuItem mntmNewReception = new JMenuItem("New Receptionist");
		mntmNewReception.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewReceptionist newReceptionist = new NewReceptionist();
				newReceptionist.setVisible(true);
				ua.check_activity(userName,7,3);
			}
		});
		mntmNewReception.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception.add(mntmNewReception);

		JMenuItem mntmManageReceptions = new JMenuItem("Manage Receptionists");
		mntmManageReceptions.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ReceptionistBrowser receptionistBrowser = new ReceptionistBrowser();
				receptionistBrowser.setVisible(true);
				ua.check_activity(userName,8,3);
			}
		});
		mntmManageReceptions.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception.add(mntmManageReceptions);



		JMenuItem mntmReceptionCounters = new JMenuItem("Reception Counters");
		mntmReceptionCounters.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ReceptionCounters receptionistBrowser = new ReceptionCounters();
				receptionistBrowser.setVisible(true);
				ua.check_activity(userName,9,3);
			}
		});
		mntmReceptionCounters.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception.add(mntmReceptionCounters);


		JMenu mrdJMenu = new JMenu("MRD");
		mrdJMenu.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mrdJMenu.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mrdJMenu);

		JMenuItem mntmNewMrd = new JMenuItem("New User");
		mntmNewMrd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewMrdUser newReceptionist = new NewMrdUser();
				newReceptionist.setVisible(true);
				ua.check_activity(userName,10,3);
			}
		});
		mntmNewMrd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mrdJMenu.add(mntmNewMrd);

		JMenuItem mntmManageMrd = new JMenuItem("Manage User");
		mntmManageMrd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				MrdUserBrowser receptionistBrowser = new MrdUserBrowser();
				receptionistBrowser.setVisible(true);
				ua.check_activity(userName,11,3);
			}
		});
		mntmManageMrd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mrdJMenu.add(mntmManageMrd);

		JMenu mnAccounts = new JMenu("Accounts");
		mnAccounts.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/OPEN.GIF")));
		mnAccounts.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnAccounts);

		JMenuItem menuItem_1 = new JMenuItem("New User");
		menuItem_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewAccountsUser newReceptionist = new NewAccountsUser();
				newReceptionist.setVisible(true);
				ua.check_activity(userName,12,3);
			}
		});
		menuItem_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnAccounts.add(menuItem_1);

		JMenuItem menuItem_2 = new JMenuItem("Manage User");
		menuItem_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				AccountsUserBrowser receptionistBrowser = new AccountsUserBrowser();
				receptionistBrowser.setVisible(true);
				ua.check_activity(userName,13,3);
			}
		});
		menuItem_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnAccounts.add(menuItem_2);

		JMenu mnWards = new JMenu("Departments");
		mnWards.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnWards.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnWards);

		JMenuItem mntmNewWard = new JMenuItem("Creat New Sub Department");
		mntmNewWard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddWards ds = new AddWards();
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ua.check_activity(userName,14,3);
				ds.setVisible(true);
			}
		});
		mntmNewWard.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnWards.add(mntmNewWard);

		JMenuItem mntmNewWardLogin = new JMenuItem("New Departments Login");
		mntmNewWardLogin.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewDepartment newDepartment = new NewDepartment();
				newDepartment.setModal(true);
				newDepartment.setVisible(true);
				ua.check_activity(userName,15,3);
			}
		});
		mntmNewWardLogin.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnWards.add(mntmNewWardLogin);


		JMenuItem mntmManageWardCharges = new JMenuItem("Manage Departments Charges");
		mntmManageWardCharges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				WardCharges newDepartment = new WardCharges();
				newDepartment.setVisible(true);
				ua.check_activity(userName,16,3);
			}
		});
		mntmManageWardCharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnWards.add(mntmManageWardCharges);


		JMenuItem mntmManageDeptUser = new JMenuItem("Manage Department User Login");
		mntmManageDeptUser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DepartmentUserBrowser newDepartment = new DepartmentUserBrowser();
				newDepartment.setVisible(true);
				ua.check_activity(userName,17,3);
			}
		});
		mntmManageDeptUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnWards.add(mntmManageDeptUser);

		JMenu mnStoreAccount = new JMenu("Store Account");
		mnStoreAccount.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/OPEN.GIF")));
		mnStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSukhpal.add(mnStoreAccount);

		JMenuItem mntmNewStoreAccount = new JMenuItem("New Store Account");
		mntmNewStoreAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewStoreAccount newStoreAccount = new NewStoreAccount();
				newStoreAccount.setModal(true);
				newStoreAccount.setVisible(true);
				ua.check_activity(userName,18,3);
			}
		});
		mntmNewStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnStoreAccount.add(mntmNewStoreAccount);

		JMenuItem mntmManageStoreAccount = new JMenuItem("Manage Store Account");
		mntmManageStoreAccount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				StoreAccountBrowser storeAccountBrowser = new StoreAccountBrowser();
				storeAccountBrowser.setModal(true);
				storeAccountBrowser.setVisible(true);
				ua.check_activity(userName,19,3);
			}
		});
		mntmManageStoreAccount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnStoreAccount.add(mntmManageStoreAccount);

		JMenu mnManagePrice = new JMenu("Masters");
		mnManagePrice.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/money_dialog.png")));
		mnManagePrice.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnManagePrice);

		JMenu mnOpdMaster = new JMenu("OPD");
		mnOpdMaster.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/plus_button.png")));
		mnOpdMaster.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnManagePrice.add(mnOpdMaster);

		JMenuItem mntmNewOpdType = new JMenuItem("Manager");
		mntmNewOpdType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				OPDTypeBrowser ds = new OPDTypeBrowser();
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
				ua.check_activity(userName,20,3);
			}
		});
		mntmNewOpdType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnOpdMaster.add(mntmNewOpdType);


		JMenu mnExamMaster = new JMenu("Exam");
		mnExamMaster.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/plus_button.png")));
		mnExamMaster.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnManagePrice.add(mnExamMaster);

		JMenuItem mntmNewExamType = new JMenuItem("New Exam Type");
		mntmNewExamType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String name=GetName();
				//String name=JOptionPane.showInputDialog(null,"Enter Password");
				AdminDBConnection AdminDBConnection=new AdminDBConnection();
				exam_open_password=AdminDBConnection.retrieveExamModulePassword();
				AdminDBConnection.closeConnection();
				if(name.equals(exam_open_password)){
					AllExamsDetail ds = new AllExamsDetail();
					ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					ds.setLocationRelativeTo(contentPane);
					ds.setVisible(true);
					ua.check_activity(userName,21,3);
				}
				else {
					JOptionPane.showMessageDialog(null,
							"Wrong Password", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		mntmNewExamType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnExamMaster.add(mntmNewExamType);

		JMenuItem mntmManageExamTypes = new JMenuItem("Manage Exam Types");
		mntmManageExamTypes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnExamMaster.add(mntmManageExamTypes);

		JMenu mnInsuranceMaster = new JMenu("Insurance");
		mnInsuranceMaster.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnInsuranceMaster.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/plus_button.png")));
		mnManagePrice.add(mnInsuranceMaster);

		JMenuItem mntmNewInuranceType = new JMenuItem("New Inurance Type");
		mntmNewInuranceType.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				AddInsuranceType addInsuranceType = new AddInsuranceType();
				addInsuranceType.setVisible(true);
				ua.check_activity(userName,23,3);
			}
		});
		mntmNewInuranceType.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnInsuranceMaster.add(mntmNewInuranceType);

		JMenuItem mntmMangeInsuranceTypes = new JMenuItem(
				"Mange Insurance Types");
		mntmMangeInsuranceTypes.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnInsuranceMaster.add(mntmMangeInsuranceTypes);

		JMenu mnIndoorPackage = new JMenu("Indoor Package");
		mnIndoorPackage.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnManagePrice.add(mnIndoorPackage);

		JMenuItem mntmNewPackage = new JMenuItem("New Package");
		mntmNewPackage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				CreateIPDPackage createIPDPackage=new CreateIPDPackage();
				createIPDPackage.setVisible(true);
				ua.check_activity(userName,25,3);
			}
		});
		mntmNewPackage.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnIndoorPackage.add(mntmNewPackage);

		JMenu mnManageStore = new JMenu("Store");
		mnManageStore.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/restore.gif")));
		mnManageStore.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnManageStore);


		JMenu mnOutdoorPatient = new JMenu("Suppliers");
		mnOutdoorPatient.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/officer.png")));
		mnOutdoorPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnOutdoorPatient);

		JMenuItem mntmNewSupplier = new JMenuItem("New Supplier");
		mntmNewSupplier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewSupplier newSupplier=new NewSupplier();
				newSupplier.setModal(true);
				newSupplier.setVisible(true);
				ua.check_activity(userName,26,3);
			}
		});
		mntmNewSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmNewSupplier);

		JMenuItem mntmEditSupplier = new JMenuItem("Edit Supplier");
		mntmEditSupplier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				SupplierBrowser supplierBrowser=new SupplierBrowser();
				supplierBrowser.setModal(true);
				supplierBrowser.setVisible(true);
				ua.check_activity(userName,27,3);
			}
		});
		mntmEditSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmEditSupplier);


		JMenu mnItems = new JMenu("Items Master");
		mnItems.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/restore.gif")));
		mnItems.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnItems);

		JMenuItem mntmNewItem = new JMenuItem("New Item");
		mntmNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem=new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);
				ua.check_activity(userName,28,3);
			}
		});
		mntmNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmNewItem);

		JMenuItem mntmManageItems = new JMenuItem("Manage Items");
		mntmManageItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				ItemBrowser itemBrowser=new ItemBrowser("admin");
				itemBrowser.setModal(true);
				itemBrowser.setVisible(true);
				ua.check_activity(userName,29,3);
			}
		});
		mntmManageItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmManageItems);

		JMenuItem mntmStockAdjustment = new JMenuItem("Stock Adjustment");
		mntmStockAdjustment.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StockAdjustment stockAdjustment=new StockAdjustment();
				stockAdjustment.setModal(true);
				stockAdjustment.setVisible(true);
				ua.check_activity(userName,32,3);
			}
		});

		JMenuItem mntmInsuranceItemStatus = new JMenuItem("Insurance Items Status");
		mntmInsuranceItemStatus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				insuranse_item_status window = new insuranse_item_status();
				window.frame.setVisible(true);
			}
		});
		JMenuItem mntmDepartmentItemMinimum = new JMenuItem("Department Item Minimum Set");
		mntmDepartmentItemMinimum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DepartmentItemProfile departmentItemProfile=new DepartmentItemProfile("","");
				departmentItemProfile.setModal(true);
				departmentItemProfile.setVisible(true);
				ua.check_activity(userName,30,3);
			}
		});
		mntmDepartmentItemMinimum.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmDepartmentItemMinimum);
		JMenuItem mntmStockRegister_1 = new JMenuItem("Stock Register");
		mntmStockRegister_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(userName,31,3);
			}
		});
		mntmStockRegister_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmStockRegister_1);
		mntmStockAdjustment.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmStockAdjustment);

		JMenuItem mntmStockManagement = new JMenuItem("Stock Management");
		mntmStockManagement.setFont(new Font("Dialog", Font.BOLD, 14));
		mntmStockManagement.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BatchManagement StockManagement=new BatchManagement();
				StockManagement.setModal(true);
				StockManagement.setVisible(true);
			}
		});
		mnItems.add(mntmStockManagement);
		mntmInsuranceItemStatus.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmInsuranceItemStatus);

		JMenu quotationMenu = new JMenu("Quotation");
		quotationMenu.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/invoice.png")));
		quotationMenu.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(quotationMenu);

		JMenuItem mntmQuotationEntry = new JMenuItem("Quotation entry");
		mntmQuotationEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				QuotationPrice quotationPrice=new QuotationPrice();
				quotationPrice.setModal(true);
				quotationPrice.setVisible(true);
				ua.check_activity(userName,33,3);
			}
		});
		mntmQuotationEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		quotationMenu.add(mntmQuotationEntry);

		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/list_dialog.png")));
		mnReports.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnReports);

		JMenuItem mntmItemsDetail = new JMenuItem("Items Detail");
		mntmItemsDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					new ItemRateListReportExcel();
					ua.check_activity(userName,34,3);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mntmItemsDetail.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemsDetail);
		JMenuItem mntmDepartmentIssuedRegister = new JMenuItem("Department Issued Register");
		mntmDepartmentIssuedRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				DateSelection dateSelection=new DateSelection(11);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,35,3);
			}
		});

		mntmDepartmentIssuedRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmDepartmentIssuedRegister);

		JMenuItem mntmPatientIssuedRegister = new JMenuItem("Patient Issued Register");
		mntmPatientIssuedRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(12);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,36,3);
			}
		});
		mntmPatientIssuedRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmPatientIssuedRegister);

		JMenuItem mntmExamsDoneReport = new JMenuItem("Exams Done Report");
		mntmExamsDoneReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamsDoneReport dateSelection = new ExamsDoneReport();
				dateSelection.setVisible(true);
				ua.check_activity(userName,37,3);
			}
		});
		mntmExamsDoneReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmExamsDoneReport);

		JMenuItem mntmItemConsumedReport = new JMenuItem("Item Consumed Report");
		mntmItemConsumedReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemConsumedReport dateSelection = new ItemConsumedReport();
				dateSelection.setVisible(true);
				ua.check_activity(userName,38,3);
			}
		});

		mntmItemConsumedReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemConsumedReport);

		JMenuItem mntmTotalPillsIssued = new JMenuItem("Total Pills Issued");
		mntmTotalPillsIssued.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(10);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,39,3);
			}
		});
		mntmTotalPillsIssued.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmTotalPillsIssued);
		JMenuItem mntmPurchaseOrder = new JMenuItem("Purchase Order");
		mntmPurchaseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new PurchaseOrderExcel();
					ua.check_activity(userName,41,3);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		JMenuItem mntmDeptWiseIssued = new JMenuItem("Dept Wise Issued");
		mntmDeptWiseIssued.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ItemIssueLog issueLog=new ItemIssueLog();
				issueLog.setModal(true);
				issueLog.setVisible(true);
				ua.check_activity(userName,40,3);
			}
		});
		mntmDeptWiseIssued.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmDeptWiseIssued);
		
		JMenuItem mntmDeptWiseIssued_2 = new JMenuItem("Dept Wise Issued NEW");
		mntmDeptWiseIssued_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemIssueLogNEW ob=new ItemIssueLogNEW();
				ob.setVisible(true);
				ob.setModal(true);
				
			}
		});
		mntmDeptWiseIssued_2.setFont(new Font("Dialog", Font.BOLD, 14));
		mnReports.add(mntmDeptWiseIssued_2);
		mntmPurchaseOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmPurchaseOrder);

		JMenuItem mntmInvoiceDetails = new JMenuItem("Invoice Items Reports");
		mntmInvoiceDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection=new DateSelection(15);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,42,3);

			}
		});
		mntmInvoiceDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmInvoiceDetails);

		JMenuItem mntmItemsIssuedFrom = new JMenuItem("Items Issued From Dept.");
		mntmItemsIssuedFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection=new DateSelection(16);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,43,3);
			}
		});
		mntmItemsIssuedFrom.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemsIssuedFrom);

		JMenu mnInvoice = new JMenu("Invoice/PO");
		mnInvoice.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/invoice.png")));
		mnInvoice.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnInvoice);

		JMenu mnPurchaseInvoice = new JMenu("Purchase Invoice");
		mnPurchaseInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnPurchaseInvoice);

		JMenuItem mntmNewInvoice = new JMenuItem("New Invoice");
		mntmNewInvoice.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewInvoice newInvoice=new NewInvoice("","","");
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
				ua.check_activity(userName,45,3);
			}
		});
		mntmNewInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmNewInvoice);

		JMenu mnPurchaseOrder = new JMenu("Purchase Order");
		mnPurchaseOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnPurchaseOrder);

		JMenuItem mntmNewForm_2 = new JMenuItem("New Form");
		mntmNewForm_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO newPO=new NewPO();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(userName,47,3);
			}
		});
		mntmNewForm_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder.add(mntmNewForm_2);

		JMenuItem mntmManager_1 = new JMenuItem("Manager");
		mntmManager_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				POBrowser newPO=new POBrowser();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(userName,48,3);
			}
		});
		mntmManager_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder.add(mntmManager_1);

		JMenuItem mntmAutomaticGenerate = new JMenuItem("Automatic Generate");
		mntmAutomaticGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PurchaseOrderReport dateSelection=new PurchaseOrderReport();
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,49,3);
			}
		});
		mntmAutomaticGenerate.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder.add(mntmAutomaticGenerate);


		JMenu mnReturnInvoice = new JMenu("Return Invoice");
		mnReturnInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnReturnInvoice);

		JMenuItem mntmNewInvoice_1 = new JMenuItem("New Invoice");
		mntmNewInvoice_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewReturnItemsForm newReturnInvoice=new NewReturnItemsForm();
				newReturnInvoice.setModal(true);
				newReturnInvoice.setVisible(true);
				ua.check_activity(userName,50,3);
			}
		});
		mntmNewInvoice_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice.add(mntmNewInvoice_1);
		JMenuItem mntmNewInvoice_12 = new JMenuItem("Return Invoice Manager");
		mntmNewInvoice_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ReturnInvoiceBrowser newReturnInvoice=new ReturnInvoiceBrowser();
				newReturnInvoice.setModal(true);
				newReturnInvoice.setVisible(true);
				ua.check_activity(userName,51,3);
			}
		});
		mntmNewInvoice_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice.add(mntmNewInvoice_12);
		JMenu mnItemIssue = new JMenu("Issue/Return Register");
		mnItemIssue.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnItemIssue.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnItemIssue);

		JMenu mnIssueItems = new JMenu("Issue Items");
		mnIssueItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnIssueItems);

		JMenuItem mntmNewIssueForm = new JMenuItem("New Issue Form");
		mntmNewIssueForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewIssuedForm newIssuedForm=new NewIssuedForm();
				newIssuedForm.setModal(true);
				newIssuedForm.setVisible(true);
				ua.check_activity(userName,52,3);
			}
		});
		mntmNewIssueForm.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIssueItems.add(mntmNewIssueForm);

		JMenu mnOpeningStock = new JMenu("Opening Stock");
		mnOpeningStock.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnOpeningStock);

		JMenuItem mntmNewForm = new JMenuItem("New Form");
		mntmNewForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpeningStockForm newIssuedForm=new OpeningStockForm();
				newIssuedForm.setModal(true);
				newIssuedForm.setVisible(true);
				ua.check_activity(userName,53,3);
			}
		});
		mntmNewForm.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOpeningStock.add(mntmNewForm);
		
		JMenuItem mntmTransferStock = new JMenuItem("Transfer Stock");
		mntmTransferStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				TransferStock ob=new TransferStock();
				ob.setVisible(true);
				ob.setModal(true);
			}
		});
		mntmTransferStock.setFont(new Font("Dialog", Font.BOLD, 14));
		mnItemIssue.add(mntmTransferStock);

		JMenu mnReturnItems = new JMenu("Return Items");
		mnReturnItems.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnReturnItems);

		JMenuItem mntmNewReturnForm = new JMenuItem("New Return Form");
		mntmNewReturnForm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewReturnForm newReturnForm=new NewReturnForm();
				newReturnForm.setModal(true);
				newReturnForm.setVisible(true);
				ua.check_activity(userName,54,3);
			}
		});
		mntmNewReturnForm.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnItems.add(mntmNewReturnForm);

		JMenu mnIssueToPatient = new JMenu("Issue To Patient");
		mnIssueToPatient.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnIssueToPatient);

		JMenuItem mntmNewIssueForm_1 = new JMenuItem("New Issue Form");
		mntmNewIssueForm_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				IndoorPillsEntryFromStore IndoorPillsEntryFromStore=new IndoorPillsEntryFromStore();
				IndoorPillsEntryFromStore.setModal(true);
				IndoorPillsEntryFromStore.setVisible(true);
				ua.check_activity(userName,55,3);
			}
		});
		mntmNewIssueForm_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIssueToPatient.add(mntmNewIssueForm_1);

		JMenu mnReturnFromPatient = new JMenu("Return From Patient");
		mnReturnFromPatient.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnReturnFromPatient);

		JMenuItem mntmNewForm_1 = new JMenuItem("New Form");
		mntmNewForm_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				IndoorPillsReturnFromStore reIndoorPillsReturnFromStore=new IndoorPillsReturnFromStore();
				reIndoorPillsReturnFromStore.setModal(true);
				reIndoorPillsReturnFromStore.setVisible(true);
				ua.check_activity(userName,56,3);
			}
		});
		mntmNewForm_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnFromPatient.add(mntmNewForm_1);

		JMenu mnRequestRegister = new JMenu("Request Register");
		mnRequestRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnRequestRegister);

		JMenuItem mntmRegister = new JMenuItem("Register");
		mntmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				RequestRegister requestRegister=new RequestRegister();
				requestRegister.setModal(true);
				requestRegister.setVisible(true);
				ua.check_activity(userName,57,3);
			}
		});
		mntmRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnRequestRegister.add(mntmRegister);

		JMenu mnProcedureEntry = new JMenu("Procedure Entry");
		mnProcedureEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnProcedureEntry);

		JMenuItem mntmNewEntry = new JMenuItem("New Entry");
		mntmNewEntry.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				NewProcedsuresForm newProcedsuresForm=new NewProcedsuresForm();
				newProcedsuresForm.setModal(true);
				newProcedsuresForm.setVisible(true); 
				ua.check_activity(userName,58,3);
			}
		});
		mntmNewEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnProcedureEntry.add(mntmNewEntry);

		JMenu mnExamBom = new JMenu("Exam BOM");
		mnExamBom.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnExamBom);

		JMenuItem menuItem = new JMenuItem("New Entry");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamBomForm examBomForm=new ExamBomForm();
				examBomForm.setModal(true);
				examBomForm.setVisible(true);
				ua.check_activity(userName,59,3);
			}
		});
		menuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnExamBom.add(menuItem);

		JMenuItem mntmDetails = new JMenuItem("Details");
		mntmDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamBomReport bomReport=new ExamBomReport();
				bomReport.setModal(true);
				bomReport.setVisible(true);
				ua.check_activity(userName,60,3);
			}
		});
		mntmDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnExamBom.add(mntmDetails);

		JMenu mnMyStock = new JMenu("My Stock");
		mnMyStock.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/stockicon.png")));
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnManageStore.add(mnMyStock);

		JMenuItem mntmStockRegister = new JMenuItem("Stock Register");
		mntmStockRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(userName,61,3);
			}
		});
		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmStockRegister);

		JMenu mnReportsMain = new JMenu("Reports");
		mnReportsMain.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/list_dialog.png")));
		mnReportsMain.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnReportsMain);

		JMenuItem mntmSummeryReport = new JMenuItem("Summery Report");
		mntmSummeryReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DateSelection dateSelection = new DateSelection(0);
				dateSelection.setVisible(true);
				ua.check_activity(userName,62,3);
			}
		});
		mntmSummeryReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmSummeryReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmSummeryReport);

		JMenuItem mntmExamsReport = new JMenuItem("Exams Report");
		mntmExamsReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(3);
				dateSelection.setVisible(true);
				ua.check_activity(userName,64,3);
			}
		});
		JMenuItem mntmInsuranceSummery = new JMenuItem("Insurance Summery");
		mntmInsuranceSummery.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmInsuranceSummery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(9);
				dateSelection.setVisible(true);
				ua.check_activity(userName,63,3);
			}
		});
		mntmInsuranceSummery.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmInsuranceSummery);

		JMenuItem mntmNearExpiryItems = new JMenuItem("Expiry Items Report");
		mntmNearExpiryItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection=new DateSelection(10);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);        
			}
		});
		mntmNearExpiryItems.setIcon(new ImageIcon(AdminMain.class.getResource("/icons/NEW.PNG")));
		mntmNearExpiryItems.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnReportsMain.add(mntmNearExpiryItems);
		mntmExamsReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmExamsReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmExamsReport);

		JMenuItem mntmExamsDoneReport1 = new JMenuItem("Exams Done Report");
		mntmExamsDoneReport1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamsDoneReport dateSelection = new ExamsDoneReport();
				dateSelection.setVisible(true);
				ua.check_activity(userName,65,3);
			}
		});
		
		JMenuItem mntmkarunameddiscount = new JMenuItem("Karuna Med Discount Report");
		mntmkarunameddiscount.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmkarunameddiscount.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmkarunameddiscount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				karunaMedDiscountReport dateSelection = new karunaMedDiscountReport();
				dateSelection.setVisible(true);
				ua.check_activity(userName,65,3);
			}
		});
		mntmkarunameddiscount.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnReportsMain.add(mntmkarunameddiscount);
		mntmExamsDoneReport1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmExamsDoneReport1);

		JMenuItem mntmItemConsumedReport1 = new JMenuItem("Item Consumed Report");
		mntmItemConsumedReport1.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmItemConsumedReport1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemConsumedReport dateSelection = new ItemConsumedReport();
				dateSelection.setVisible(true);
				ua.check_activity(userName,66,3);
			}
		});
		mntmItemConsumedReport1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmItemConsumedReport1);

		JMenu mnIpdReports = new JMenu("IPD Reports");
		mnIpdReports.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mnIpdReports.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mnIpdReports);

		JMenuItem mntmPendingPatients = new JMenuItem("Pending Patients");
		mntmPendingPatients.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(5);
				dateSelection.setVisible(true);
				ua.check_activity(userName,67,3);

			}
		});
		mntmPendingPatients.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnIpdReports.add(mntmPendingPatients);

		JMenuItem mntmDischargedPatients = new JMenuItem("Discharged Patients");
		mntmDischargedPatients.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DateSelection dateSelection = new DateSelection(4);
				dateSelection.setVisible(true);
				ua.check_activity(userName,69,3);
			}
		});

		JMenuItem mntmAdvanceAmount = new JMenuItem("Advance Amount");
		mntmAdvanceAmount.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(7);
				dateSelection.setVisible(true);
				ua.check_activity(userName,68,3);
			}
		});
		mntmAdvanceAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnIpdReports.add(mntmAdvanceAmount);
		mntmDischargedPatients.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnIpdReports.add(mntmDischargedPatients);

		JMenuItem mntmDoctorsReport = new JMenuItem("Doctors Report");
		mntmDoctorsReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				DoctorsReport doctorsReport = new DoctorsReport();
				doctorsReport.setModal(true); 
				doctorsReport.setVisible(true);
				ua.check_activity(userName,70,3);
			}
		});
		mntmDoctorsReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmDoctorsReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmDoctorsReport);

		JMenuItem mntmInsuranceReport = new JMenuItem("Insurance Wise Report");
		mntmInsuranceReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				InsuranceWiseReport doctorsReport = new InsuranceWiseReport();
				doctorsReport.setModal(true);
				doctorsReport.setVisible(true);
				ua.check_activity(userName,72,3);
			}
		});

		JMenuItem mntmDoctorsCostCenter = new JMenuItem("Doctors Cost Center Report");
		mntmDoctorsCostCenter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DoctorsCostCenter doctorsReport = new DoctorsCostCenter();
				doctorsReport.setModal(true); 
				doctorsReport.setVisible(true);
				ua.check_activity(userName,71,3);
			}
		});
		mntmDoctorsCostCenter.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmDoctorsCostCenter);
		
		
	
		mntmInsuranceReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmInsuranceReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmInsuranceReport);

		JMenuItem mntmCancelledReport = new JMenuItem("Cancelled Report");
		mntmCancelledReport.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(1);
				dateSelection.setVisible(true);
				ua.check_activity(userName,73,3);
			}
		});
		mntmCancelledReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmCancelledReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmCancelledReport);

		JMenuItem mntmSummeryExcelFile = new JMenuItem("Summery Excel");
		mntmSummeryExcelFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection = new DateSelection(8);
				dateSelection.setVisible(true);
				ua.check_activity(userName,74,3);
			}
		});
		mntmSummeryExcelFile.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/NEW.PNG")));
		mntmSummeryExcelFile.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmSummeryExcelFile);

		JMenuItem mntmFreeTestReport = new JMenuItem("Free Test Report");
		mntmFreeTestReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				FreeTestData data = new FreeTestData();
				data.setModal(true);
				data.setVisible(true);
				ua.check_activity(userName,75,3);
			}
		});
		mntmFreeTestReport.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmFreeTestReport);

		JMenuItem mntmPatientsWardVisit = new JMenuItem("Patients Ward Visit");
		mntmPatientsWardVisit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PatientWardVisit data = new PatientWardVisit();
				data.setVisible(true);
				ua.check_activity(userName,76,3);
			}
		});
		mntmPatientsWardVisit.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mntmPatientsWardVisit);

		JMenu mnReports_1 = new JMenu("Reports");
		mnReports_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReportsMain.add(mnReports_1);

		JMenuItem mntmWardsIssuedConsume = new JMenuItem("Wards Issued Consume");
		mntmWardsIssuedConsume.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DateSelection dateSelection = new DateSelection(13);
				dateSelection.setVisible(true);
				ua.check_activity(userName,77,3);
			}
		});
		mntmWardsIssuedConsume.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReports_1.add(mntmWardsIssuedConsume);

		JMenuItem mntmIpdOpd = new JMenuItem("IPD & OPD Medicines");
		mntmIpdOpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(14);
				dateSelection.setVisible(true);
				ua.check_activity(userName,78,3);
			}
		});
		mntmIpdOpd.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReports_1.add(mntmIpdOpd);

		JMenuItem mntmWardWiseDoctor = new JMenuItem("Ward Wise, Doctor Wise Days");
		mntmWardWiseDoctor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(17);
				dateSelection.setVisible(true);
				ua.check_activity(userName,79,3);
			}
		});
		mntmWardWiseDoctor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReports_1.add(mntmWardWiseDoctor);

		JMenuItem mntmPatientWiseDays = new JMenuItem("Patient Wise Days");
		mntmPatientWiseDays.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DateSelection dateSelection = new DateSelection(18);
				dateSelection.setVisible(true);
				ua.check_activity(userName,80,3);
			}
		});
		mntmPatientWiseDays.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReports_1.add(mntmPatientWiseDays);

		JMenuItem mntmExamBomDetails = new JMenuItem("Exam BOM Details");
		mntmExamBomDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ExamBomReport bomReport=new ExamBomReport();
				bomReport.setModal(true);
				bomReport.setVisible(true);
				ua.check_activity(userName,44,3);
			}
		});
		mntmExamBomDetails.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnReports.add(mntmExamBomDetails);

		JMenu misReport = new JMenu("Records Manager");
		misReport.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/list_dialog.png")));
		misReport.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(misReport);

		JMenu mnCentralStore = new JMenu("Central Store");
		mnCentralStore.setFont(new Font("Tahoma", Font.PLAIN, 15));
		misReport.add(mnCentralStore);

		JMenuItem mntmMainStock = new JMenuItem("Main Stock");
		mntmMainStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(userName,81,3);
			}
		});
		mntmMainStock.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmMainStock);

		JMenuItem mntmIssuedTodepartmentRegister = new JMenuItem("Department Wise Issue Register");
		mntmIssuedTodepartmentRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ItemIssueLog totalStock=new ItemIssueLog();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(userName,89,3);

			}
		});

		JMenuItem mntmInvoiceRegister = new JMenuItem("Purchase Invoice Register");
		mntmInvoiceRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				InvoiceBrowser invoiceBrowser=new InvoiceBrowser();
				invoiceBrowser.setModal(true);
				invoiceBrowser.setVisible(true);
			}
		});

		JMenu mntmManagers = new JMenu("Purchase Manager");

		mntmManagers.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnCentralStore.add(mntmManagers);

		JMenu mnPurchaseInvoice1 = new JMenu("Purchase Invoice");
		mnPurchaseInvoice1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmManagers.add(mnPurchaseInvoice1);

		JMenuItem mntmNewInvoice1 = new JMenuItem("New Invoice");
		mntmNewInvoice1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewInvoice newInvoice=new NewInvoice("","","");
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
				ua.check_activity(userName,82,3);
			}
		});
		mntmNewInvoice1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice1.add(mntmNewInvoice1);

		JMenuItem mntmManager = new JMenuItem("Manager");
		mntmManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				InvoiceBrowser newInvoice=new InvoiceBrowser();
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
			}
		});
		mntmManager.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmManager);

		JMenu mnPurchaseOrder1 = new JMenu("Purchase Order");
		mnPurchaseOrder1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmManagers.add(mnPurchaseOrder1);

		mntmNewForm_2 = new JMenuItem("New Form");
		mntmNewForm_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO newPO=new NewPO();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(userName,83,3);
			}
		});
		mntmNewForm_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder1.add(mntmNewForm_2);

		mntmManager_1 = new JMenuItem("Manager");
		mntmManager_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				POBrowser newPO=new POBrowser();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(userName,84,3);
			}
		});
		mntmManager_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder1.add(mntmManager_1);

		mntmAutomaticGenerate = new JMenuItem("Automatic Generate");
		mntmAutomaticGenerate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PurchaseOrderReport dateSelection=new PurchaseOrderReport();
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(userName,85,3);
			}
		});
		mntmAutomaticGenerate.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder1.add(mntmAutomaticGenerate);

		JMenu mnReturnInvoice_1 = new JMenu("Return Invoice");
		mnReturnInvoice_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmManagers.add(mnReturnInvoice_1);

		JMenuItem mntmNewInvoice_2 = new JMenuItem("New Invoice");
		mntmNewInvoice_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewReturnInvoice newReturnInvoice=new NewReturnInvoice();
				newReturnInvoice.setModal(true);
				newReturnInvoice.setVisible(true);
				ua.check_activity(userName,86,3);
			}
		});
		mntmNewInvoice_2.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice_1.add(mntmNewInvoice_2);

		JMenuItem mntmInvoiceItemRegister = new JMenuItem("Item Wise Purchase ManagerS");
		mntmInvoiceItemRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				InvoiceItemsRegisterReport invoiceItemsRegisterReport=new InvoiceItemsRegisterReport();
				invoiceItemsRegisterReport.setModal(true);
				invoiceItemsRegisterReport.setVisible(true);
				ua.check_activity(userName,87,3);
			}
		});
		mntmInvoiceItemRegister.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmInvoiceItemRegister);

		JMenuItem mntmItemIssuedTo = new JMenuItem("Item Issued To Patient");
		mntmItemIssuedTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				MainStoreTopatientReport mainStoreTopatientReport=new MainStoreTopatientReport();
				mainStoreTopatientReport.setModal(true);
				mainStoreTopatientReport.setVisible(true);
				ua.check_activity(userName,88,3);
			}
		});
		mntmItemIssuedTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmItemIssuedTo);
		mntmIssuedTodepartmentRegister.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmIssuedTodepartmentRegister);

		JMenuItem mntmAllIssueTo = new JMenuItem("All Issue To Department Register");
		mntmAllIssueTo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DepartmentIssueRegisterReport departmentIssueRegisterReport=new DepartmentIssueRegisterReport();
				departmentIssueRegisterReport.setModal(true);
				departmentIssueRegisterReport.setVisible(true);
				ua.check_activity(userName,90,3);
			}
		});
		mntmAllIssueTo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmAllIssueTo);


		JMenuItem mntmStoreTopItems = new JMenuItem("Main Store Top 20 Items");
		mntmStoreTopItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StoreTopItemsReport storeTopItemsReport=new StoreTopItemsReport();
				storeTopItemsReport.setModal(true);
				storeTopItemsReport.setVisible(true);
				ua.check_activity(userName,91,3);
			}
		});
		mntmStoreTopItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmStoreTopItems);

		JMenuItem mntmDeptTopItems = new JMenuItem("Department Top 20 Items");
		mntmDeptTopItems.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				DeptTopItemsReport deptTopItemsReport=new DeptTopItemsReport();
				deptTopItemsReport.setModal(true);
				deptTopItemsReport.setVisible(true);
				ua.check_activity(userName,92,3);
			}
		});
		mntmDeptTopItems.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnCentralStore.add(mntmDeptTopItems);




		JMenu mnReception_1 = new JMenu("Reception");
		mnReception_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		misReport.add(mnReception_1);

		JMenuItem mntmPatientManager = new JMenuItem("Patient Manager");
		mntmPatientManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				PatientBrowser patientBrowser = new PatientBrowser();
				patientBrowser
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				patientBrowser.setLocationRelativeTo(contentPane);
				patientBrowser.setVisible(true);
				ua.check_activity(userName,93,3);
			}
		});
		mntmPatientManager.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmPatientManager);

		JMenuItem mntmOpdManager = new JMenuItem("OPD Manager");
		mntmOpdManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				OPDBrowser opdBrowser = new OPDBrowser(username);

				opdBrowser
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				opdBrowser.setLocationRelativeTo(contentPane);
				opdBrowser.setVisible(true);
				ua.check_activity(userName,94,3);
			}
		});
		mntmOpdManager.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmOpdManager);

		JMenuItem mntmExamManager = new JMenuItem("Exam Manager");
		mntmExamManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ExamsBrowser examsBrowser = new ExamsBrowser(username);
				examsBrowser
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				examsBrowser.setLocationRelativeTo(contentPane);
				examsBrowser.setVisible(true);
				ua.check_activity(userName, 162, 3);

			}
		});
		mntmExamManager.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmExamManager);

		JMenuItem mntmMiscManager = new JMenuItem("Misc. Manager");
		mntmMiscManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				MISCBrowser ds = new MISCBrowser(username);
				ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ds.setLocationRelativeTo(contentPane);
				ds.setModal(true);
				ds.setVisible(true);
				ua.check_activity(userName,95,3);
			}
		});
		mntmMiscManager.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmMiscManager);

		JMenuItem mntmIpdManager = new JMenuItem("IPD Manager");
		mntmIpdManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				IPDBrowser ipdBrowser = new IPDBrowser(username);
				ipdBrowser.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser.setLocationRelativeTo(contentPane);
				ipdBrowser.setVisible(true);
				ua.check_activity(userName,96,3);
			}
		});
		mntmIpdManager.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmIpdManager);
		JMenuItem mntmIpdManager1 = new JMenuItem("Procedure IPD Manager");
		mntmIpdManager1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				ProcedureIpdBrowser	ipdBrowser1 = new ProcedureIpdBrowser();
				ipdBrowser1
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser1.setLocationRelativeTo(contentPane);
				ipdBrowser1.setVisible(true);
				ua.check_activity(userName,97,3);
			}
		});
		mntmIpdManager1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmIpdManager1);
		JMenuItem mntmIpdManager_dialsis = new JMenuItem("Dialysis IPD Manager");
		mntmIpdManager_dialsis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DialysisIpdBrowser	ipdBrowser1 = new DialysisIpdBrowser();
				ipdBrowser1
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser1.setLocationRelativeTo(contentPane);
				ipdBrowser1.setVisible(true);
				ua.check_activity(userName,98,3);
			}
		});
		mntmIpdManager_dialsis.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmIpdManager_dialsis);
		JMenuItem mntmIpdManager2 = new JMenuItem("Emergency IPD Manager");
		mntmIpdManager2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				EmergencyIpdBrowser	ipdBrowser1 = new EmergencyIpdBrowser();
				ipdBrowser1
				.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				ipdBrowser1.setLocationRelativeTo(contentPane);
				ipdBrowser1.setVisible(true);
				ua.check_activity(userName,99,3);
			}
		});
		mntmIpdManager2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnReception_1.add(mntmIpdManager2);
		JMenu mnDepartments = new JMenu("Departments");
		mnDepartments.setFont(new Font("Tahoma", Font.PLAIN, 15));
		misReport.add(mnDepartments);

		JMenuItem mntmItemStock = new JMenuItem("Department Stock Register");
		mntmItemStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DepartmentItemProfile departmentItemProfile=new DepartmentItemProfile("","");
				departmentItemProfile.setModal(true);
				departmentItemProfile.setVisible(true);
				ua.check_activity(userName,100,3);
			}
		});
		mntmItemStock.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDepartments.add(mntmItemStock);

		JMenuItem mntmDepartmentWiseItem = new JMenuItem("Item Recieved From Store Register");
		mntmDepartmentWiseItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemIssueLog totalStock=new ItemIssueLog();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(userName,101,3);
			}
		});
		mntmDepartmentWiseItem.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDepartments.add(mntmDepartmentWiseItem);

		JMenuItem menuItem_6 = new JMenuItem("All Recieve from Store Register");
		menuItem_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {


				DepartmentIssueRegisterReport departmentIssueRegisterReport=new DepartmentIssueRegisterReport();
				departmentIssueRegisterReport.setModal(true);
				departmentIssueRegisterReport.setVisible(true);
				ua.check_activity(userName,102,3);

			}
		});
		menuItem_6.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDepartments.add(menuItem_6);

		JMenuItem mntmIssuedToPatient = new JMenuItem("Issued To Patient");
		mntmIssuedToPatient.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				DepartmentToPatientLog departmentToPatientLog=new DepartmentToPatientLog();
				departmentToPatientLog.setModal(true);
				departmentToPatientLog.setVisible(true);
				ua.check_activity(userName,103,3);
			}
		});
		mntmIssuedToPatient.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mnDepartments.add(mntmIssuedToPatient);
		
		JMenu mnDepartments_1 = new JMenu("Doctor Share");
		mnDepartments_1.setFont(new Font("Dialog", Font.PLAIN, 15));
		misReport.add(mnDepartments_1);
		
		JMenuItem mntmItemStock_1 = new JMenuItem("Conservative Sheet");
		mntmItemStock_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ConservativeSheet ConservativeSheet=new ConservativeSheet();
				ConservativeSheet.setModal(true);
				ConservativeSheet.setVisible(true);	
			}
		});
		mntmItemStock_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		mnDepartments_1.add(mntmItemStock_1);
		
		JMenuItem mntmItemStock_1_1 = new JMenuItem("OT Sheet");
		mntmItemStock_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OtShareSheet OtShareSheet=	new OtShareSheet();
				OtShareSheet.setModal(true);
				OtShareSheet.setVisible(true);
				
			}
		});
		mntmItemStock_1_1.setFont(new Font("Dialog", Font.PLAIN, 14));
		mnDepartments_1.add(mntmItemStock_1_1);

		JMenu mnCancellation = new JMenu("Record Cancellation");
		mnCancellation.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/delete_dialog.png")));
		mnCancellation.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnCancellation);

		JMenuItem mntmCancelOpd = new JMenuItem("Cancel OPD");
		mntmCancelOpd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				Cancellation cancellation = new Cancellation(0);
				cancellation.setModal(true);
				cancellation.setVisible(true);
				ua.check_activity(userName,104,3);
			}
		});
		mntmCancelOpd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmCancelOpd.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/close_button.png")));
		mnCancellation.add(mntmCancelOpd);
		JMenuItem mntmCancelMisc = new JMenuItem("Cancel Misc");
		mntmCancelMisc.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				CancelMiscSlip cancelMiscSlip = new CancelMiscSlip();
				cancelMiscSlip.setModal(true);
				cancelMiscSlip.setVisible(true);
				ua.check_activity(userName,106,3);
			}
		});
		mntmCancelMisc.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmCancelMisc.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/close_button.png")));
		mnCancellation.add(mntmCancelMisc);

		JMenuItem mntmCancelPatientCard = new JMenuItem("Cancel Patient Card");
		mntmCancelPatientCard.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				Cancellation cancellation = new Cancellation(2);
				cancellation.setVisible(true);
				cancellation.setModal(true);
				ua.check_activity(userName,107,3);
			}
		});
		mntmCancelPatientCard.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmCancelPatientCard.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/close_button.png")));
		mnCancellation.add(mntmCancelPatientCard);

		JMenuItem mntmCancelIpd = new JMenuItem("Cancel IPD Exams");
		mntmCancelIpd.setEnabled(false);
		mntmCancelIpd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				CancelIndoorExam cancelIndoorExam = new CancelIndoorExam();
				cancelIndoorExam.setModal(true);
				cancelIndoorExam.setVisible(true);
				ua.check_activity(userName,108,3);
			}
		});
		mntmCancelIpd.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mntmCancelIpd.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/close_button.png")));
		mnCancellation.add(mntmCancelIpd);

		JMenuItem mntmCancelLisExams = new JMenuItem("Cancel Exams(IPD/OPD)");
		mntmCancelLisExams.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				LIS_Exam_Cancellation obj=new LIS_Exam_Cancellation();
				obj.setVisible(true);
				obj.setModal(true);
			}


		});
		mntmCancelLisExams.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/close_button.png")));
		mntmCancelLisExams.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnCancellation.add(mntmCancelLisExams);

		JMenu mnEmailReports = new JMenu("Email Reports");
		mnEmailReports.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/email.png")));
		mnEmailReports.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnEmailReports);

		JMenuItem mntmEmailSettings = new JMenuItem("Email Settings");
		mntmEmailSettings.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/1rightarrow.png")));
		mntmEmailSettings.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnEmailReports.add(mntmEmailSettings);

		JMenuItem mntmEmailReports = new JMenuItem("Email Reports");
		mntmEmailReports.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				EmailReports emailReports = new EmailReports();
				emailReports.setVisible(true);
				ua.check_activity(userName,110,3);
			}
		});
		mntmEmailReports.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/1rightarrow.png")));
		mntmEmailReports.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnEmailReports.add(mntmEmailReports);

		JMenu mnSystemSettings = new JMenu("System Settings");
		mnSystemSettings.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/Settings.png")));
		mnSystemSettings.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnSystemSettings);

		JMenuItem mntmSystemMessage = new JMenuItem("System Message");
		mntmSystemMessage.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ChangeMessage dialog = new ChangeMessage();
				dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
				dialog.setVisible(true);

			}
		});
		mntmSystemMessage.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSystemSettings.add(mntmSystemMessage);

		JMenuItem mntmVersionControl = new JMenuItem("New Updates");
		mntmVersionControl.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				NewUpdates newUpdates = new NewUpdates();
				newUpdates.setModal(true);
				newUpdates.setVisible(true);
			}
		});
		mntmVersionControl.setFont(new Font("Tahoma", Font.PLAIN, 15));
		mnSystemSettings.add(mntmVersionControl);
		
		JMenuItem mntmDepartmentStockReset = new JMenuItem("Department Stock Reset");
		mntmDepartmentStockReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				managestock obj=new managestock();
				obj.frame.setVisible(true);
			}
		});
		mntmDepartmentStockReset.setFont(new Font("Dialog", Font.PLAIN, 15));
		mnSystemSettings.add(mntmDepartmentStockReset);

		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		panel.setBounds(0, 44, 1425, 72);
		contentPane.add(panel);
		panel.setLayout(null);

		JLabel label = new JLabel("");
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setIcon(new ImageIcon(AdminMain.class
				.getResource("/icons/smallLogo.png")));
		label.setBounds(10, 0, 75, 67);
		panel.add(label);

		JLabel lblRotary = new JLabel(
				" Rotary Ambala Cancer and General Hospital (Ambala Cantt.)");
		lblRotary.setForeground(Color.BLUE);
		lblRotary.setFont(new Font("Tahoma", Font.BOLD, 25));
		lblRotary.setBounds(95, 11, 892, 54);
		panel.add(lblRotary);

		JPanel panel_2 = new JPanel();
		panel_2.add(new RealTimeClock());
		panel_2.setBounds(948, 16, 375, 41);
		panel.add(panel_2);
	}
	public String GetName() {
	    JPasswordField jpf = new JPasswordField(24);
	    JLabel jl = new JLabel("Enter Password: ");
	    Box box = Box.createHorizontalBox();
	    box.add(jl);
	    box.add(jpf);
	    int x = JOptionPane.showConfirmDialog(null, box, "Password", JOptionPane.OK_CANCEL_OPTION);

	    if (x == JOptionPane.OK_OPTION) {
	      return jpf.getText().toString();
	    }
	    return null;
	  }
}
