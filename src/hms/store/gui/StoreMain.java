package hms.store.gui;

import hms.departments.gui.DepartmentItemProfile;
import hms.departments.gui.DepartmentMain;
import hms.departments.gui.OutdoorPillsEntry;
import hms.departments.gui.OutdoorPillsPerforma;
import hms.departments.gui.OutdoorProcedureEntry;
import hms.main.AboutHMS;
import hms.main.MainLogin;
import hms.reports.excels.ItemRateListReportExcel;
import hms.reports.excels.PurchaseOrderExcel;
import hms.reports.gui.DateSelection;
import hms.reports.gui.ReceptionUsersReport;
import hms.reporttables.ExamsDoneReport;
import hms.reporttables.ItemConsumedReport;
import hms.reporttables.PurchaseOrderReport;

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

import com.itextpdf.text.DocumentException;

import UsersActivity.database.UADBConnection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.io.IOException;

public class StoreMain extends JFrame {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;

	
	public static String userName="";
	public static String userID="";
	public static String departmental_stock_access="";
	public static String stock_adjustment_access="";
	public static String stockaccess="";
	public static String minimum_max_qty_access="";
	public static String update_item_access="";
	public static String access="";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					StoreMain frame = new StoreMain("nidhi","rajinder","","","","","","","");
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
	public StoreMain(final String storeID,final String storename,String userID,String departmental_stock_access,String stock_adjustment_access,String stockaccess,String minimum_max_qty_access,String update_item_access,String store_account_access) {
	
		
		
		StoreMain.userName=storename;
		StoreMain.userID=userID;
		StoreMain.departmental_stock_access=departmental_stock_access;
		StoreMain.stock_adjustment_access=stock_adjustment_access;
		StoreMain.stockaccess=stockaccess;
		StoreMain.minimum_max_qty_access=minimum_max_qty_access;
		StoreMain.update_item_access=update_item_access;
		
		setTitle(userName);
		setIconImage(Toolkit.getDefaultToolkit().getImage(StoreMain.class.getResource("/icons/rotaryLogo.png")));
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
		mnNewMenu.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/author.png")));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				MainLogin mainLogin=new MainLogin();
				mainLogin.setVisible(true);
				dispose();
			}
		});
		
		JMenuItem mntmChangePassword = new JMenuItem("Change Password");
		mntmChangePassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				StoreSettings settings=new StoreSettings(storeID);
				settings.setVisible(true);
				settings.setModal(true);
			}
		});
		mntmChangePassword.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmChangePassword);
		mntmLogout.setFont(new Font("Tahoma", Font.BOLD, 14));
		mntmLogout.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/exits.png")));
		mnNewMenu.add(mntmLogout);
		
		JMenuItem mntmAboutHms = new JMenuItem("About HMS");
		mntmAboutHms.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AboutHMS aboutHMS=new AboutHMS();
				aboutHMS.setVisible(true);
				aboutHMS.setModal(true);
			}
		});
		mntmAboutHms.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnNewMenu.add(mntmAboutHms);
		
		JMenu mnOutdoorPatient = new JMenu("Suppliers");
		mnOutdoorPatient.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/officer.png")));
		mnOutdoorPatient.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnOutdoorPatient);
		
		JMenuItem mntmNewSupplier = new JMenuItem("New Supplier");
		mntmNewSupplier.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewSupplier newSupplier=new NewSupplier();
				newSupplier.setModal(true);
				newSupplier.setVisible(true);
				ua.check_activity(storename, 163, 5);
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
				ua.check_activity(storename, 164, 5);
			}
		});
		mntmEditSupplier.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient.add(mntmEditSupplier);
		
		
		JMenu mnItems = new JMenu("Items Master");
		mnItems.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/restore.gif")));
		mnItems.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnItems);
		
		JMenuItem mntmNewItem = new JMenuItem("New Item");
		mntmNewItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NewItem newItem=new NewItem();
				newItem.setModal(true);
				newItem.setVisible(true);
				ua.check_activity(storename, 165, 5);
			}
		});
		mntmNewItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmNewItem);
		
		JMenuItem mntmManageItems = new JMenuItem("Manage Items");
		mntmManageItems.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				ItemBrowser itemBrowser=new ItemBrowser("store");
				itemBrowser.setModal(true);
				itemBrowser.setVisible(true);
				ua.check_activity(storename, 166, 5);
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
				ua.check_activity(storename, 169, 5);
			}
		});
		
		JMenuItem mntmStockRegister_1 = new JMenuItem("Stock Register");
		mntmStockRegister_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(storename, 168, 5);
			}
		});
		mntmStockRegister_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmStockRegister_1);
		if(StoreMain.stock_adjustment_access.equals("1")){
			mntmStockAdjustment.setFont(new Font("Tahoma", Font.BOLD, 14));
			mnItems.add(mntmStockAdjustment);
		}
	
	
		JMenuItem mntmDepartmentItemMinimum = new JMenuItem("Department Item Minimum Set");
		mntmDepartmentItemMinimum.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
//				DepartmentItemProfile departmentItemProfile=new DepartmentItemProfile();
				DepartmentItemProfile departmentItemProfile=new DepartmentItemProfile(StoreMain.stockaccess,StoreMain.minimum_max_qty_access);
				departmentItemProfile.setModal(true);
				departmentItemProfile.setVisible(true);
				ua.check_activity(storename, 167, 5);
			}
		});
	   if(StoreMain.departmental_stock_access.equals("1")){
		mntmDepartmentItemMinimum.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItems.add(mntmDepartmentItemMinimum);
		
		}
	
		JMenu mnOrder = new JMenu("Order");
		mnOrder.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/list_dialog.png")));
		mnOrder.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnOrder);
		
		JMenuItem mntmPurchaseOrder_1 = new JMenuItem("Purchase Order");
		mntmPurchaseOrder_1.setEnabled(false);
		mntmPurchaseOrder_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				PurchaseOrderReport dateSelection=new PurchaseOrderReport();
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(storename, 171, 5);
			}
		});
		mntmPurchaseOrder_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOrder.add(mntmPurchaseOrder_1);
		
		if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")){
			mntmPurchaseOrder_1.setEnabled(true);
		}else {
			mntmPurchaseOrder_1.setEnabled(false);
		}
		
		
		JMenu mnReports = new JMenu("Reports");
		mnReports.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/list_dialog.png")));
		mnReports.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnReports);
		
		JMenuItem mntmItemsDetail = new JMenuItem("Items Detail");
		mntmItemsDetail.setEnabled(false);
		mntmItemsDetail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					new ItemRateListReportExcel();
					ua.check_activity(storename, 172, 5);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		mntmItemsDetail.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemsDetail);
		
		if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")){
			mntmItemsDetail.setEnabled(true);
		}else {
			mntmItemsDetail.setEnabled(false);
		}
		
		JMenuItem mntmDepartmentIssuedRegister = new JMenuItem("Department Issued Register");
		mntmDepartmentIssuedRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DateSelection dateSelection=new DateSelection(11);
				dateSelection.setModal(true);
				dateSelection.setVisible(true);
				ua.check_activity(storename, 173, 5);
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
				ua.check_activity(storename, 174, 5);
			}
		});
		mntmPatientIssuedRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmPatientIssuedRegister);
		
		JMenuItem mntmExamsDoneReport = new JMenuItem("Exams Done Report");
		mntmExamsDoneReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ExamsDoneReport dateSelection = new ExamsDoneReport();
				dateSelection.setVisible(true);
				ua.check_activity(storename, 175, 5);
			}
		});
		mntmExamsDoneReport.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmExamsDoneReport);
		
		JMenuItem mntmItemConsumedReport = new JMenuItem("Item Consumed Report");
		mntmItemConsumedReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				ItemConsumedReport dateSelection = new ItemConsumedReport();
				dateSelection.setVisible(true);
				ua.check_activity(storename, 176, 5);
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
				ua.check_activity(storename, 177, 5);
			}
		});
		mntmTotalPillsIssued.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmTotalPillsIssued);
		JMenuItem mntmPurchaseOrder = new JMenuItem("Purchase Order");
		mntmPurchaseOrder.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new PurchaseOrderExcel();
					ua.check_activity(storename, 170, 5);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		
		
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
				ua.check_activity(storename, 179, 5);
				
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
				ua.check_activity(storename, 180, 5);
			}
		});
		mntmItemsIssuedFrom.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReports.add(mntmItemsIssuedFrom);
		
		JMenu mnQuotation = new JMenu("Quotation");
		mnQuotation.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/invoice.png")));
		mnQuotation.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnQuotation);
		
		JMenu mnQuotationEntry = new JMenu("Quotation Entry");
		mnQuotationEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnQuotation.add(mnQuotationEntry);
		
		JMenuItem mntmNewEntry_1 = new JMenuItem("New Entry");
		mntmNewEntry_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				QuotationPrice quotationPrice=new QuotationPrice();
				quotationPrice.setModal(true);
				quotationPrice.setVisible(true);
				ua.check_activity(storename, 181, 5);
			}
		});
		mntmNewEntry_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnQuotationEntry.add(mntmNewEntry_1);

		JMenu mnInvoice = new JMenu("Invoice/PO");
		mnInvoice.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/invoice.png")));
		mnInvoice.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnInvoice);
		
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
				ua.check_activity(storename, 182, 5);
			}
		});
		mntmNewInvoice.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmNewInvoice);
		
		JMenuItem mntmManager = new JMenuItem("Manager");
		mntmManager.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				InvoiceBrowser newInvoice=new InvoiceBrowser();
				newInvoice.setModal(true);
				newInvoice.setVisible(true);
				ua.check_activity(storename, 183, 5);
			}
		});
		mntmManager.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseInvoice.add(mntmManager);
		
		JMenu mnPurchaseOrder = new JMenu("Purchase Order");
		mnPurchaseOrder.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnInvoice.add(mnPurchaseOrder);
		
		JMenuItem mntmNewForm_1 = new JMenuItem("New Form");
		mntmNewForm_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				NewPO newPO=new NewPO();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(storename, 184, 5);
			}
		});
		mntmNewForm_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder.add(mntmNewForm_1);
		
		JMenuItem mntmManager_1 = new JMenuItem("Manager");
		mntmManager_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				POBrowser newPO=new POBrowser();
				newPO.setModal(true);
				newPO.setVisible(true);
				ua.check_activity(storename, 185, 5);
			}
		});
		mntmManager_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnPurchaseOrder.add(mntmManager_1);
		
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
				ua.check_activity(storename, 186, 5);
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
				ua.check_activity(storename, 187, 5);
			}
		});
		mntmNewInvoice_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnInvoice.add(mntmNewInvoice_12);
		JMenu mnItemIssue = new JMenu("Issue/Return Register");
		mnItemIssue.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnItemIssue.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnItemIssue);
		
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
				ua.check_activity(storename, 188, 5);
			}
		});
		mntmNewIssueForm.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIssueItems.add(mntmNewIssueForm);
		
		JMenu mnOpeningStock = new JMenu("Opening Stock");
		mnOpeningStock.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnOpeningStock);
		
		JMenuItem mntmNewForm = new JMenuItem("New Form");
		mntmNewForm.setEnabled(false);
		mntmNewForm.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OpeningStockForm newIssuedForm=new OpeningStockForm();
				newIssuedForm.setModal(true);
				newIssuedForm.setVisible(true);
				ua.check_activity(storename, 189, 5);
			}
		});
		mntmNewForm.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOpeningStock.add(mntmNewForm);
		JMenuItem mntmNewEntry12tranfer = new JMenuItem("Tranfer Stock");
		mntmNewEntry12tranfer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
					TransferStock newProcedsuresForm = new TransferStock();
					newProcedsuresForm.setModal(true);
					newProcedsuresForm.setVisible(true);
					ua.check_activity(storename, 190, 5);
				}
			}
		});
		mntmNewEntry12tranfer.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mntmNewEntry12tranfer);
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
				ua.check_activity(storename, 191, 5);
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
				ua.check_activity(storename, 192, 5);
			}
		});
		mntmNewIssueForm_1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnIssueToPatient.add(mntmNewIssueForm_1);
		
		
		
		JMenu mnOutdoorPatient1 = new JMenu("Pills Entry Outdoor Patients");

		mnOutdoorPatient1.setFont(new Font("Tahoma", Font.BOLD, 15));
		mnItemIssue.add(mnOutdoorPatient1);
		JMenu mnItemChallan = new JMenu("Challan");
		mnItemChallan.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/PRODUCT.PNG")));
		mnItemChallan.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnItemChallan);
		JMenu challanNew = new JMenu("Challan List");
		challanNew.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemChallan.add(challanNew);
		
		JMenuItem mntmNewChallan = new JMenuItem("New Challan");
		mntmNewChallan.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				NewChallan IndoorPillsEntryFromStore=new NewChallan();
				IndoorPillsEntryFromStore.setModal(true);
				IndoorPillsEntryFromStore.setVisible(true);
				ua.check_activity(storename, 204, 5);
			}
		});
		mntmNewChallan.setFont(new Font("Tahoma", Font.BOLD, 14));
		challanNew.add(mntmNewChallan);
		JMenuItem Challanmgr = new JMenuItem("Challan Manager");
		Challanmgr.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				ChallanBrowser IndoorPillsEntryFromStore=new ChallanBrowser();
				IndoorPillsEntryFromStore.setModal(true);
				IndoorPillsEntryFromStore.setVisible(true);
				ua.check_activity(storename, 205, 5);
			}
		});
		Challanmgr.setFont(new Font("Tahoma", Font.BOLD, 14));
		challanNew.add(Challanmgr);
		
		JMenuItem challanReturn = new JMenuItem("Return/Issue Challan");
		challanReturn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				ReturnChallanBrowser outdoorPillsEntry = new ReturnChallanBrowser();
				outdoorPillsEntry.setModal(true);
				outdoorPillsEntry.setVisible(true);
				ua.check_activity(storename, 206, 5);
			}
		});
		challanReturn.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemChallan.add(challanReturn);
		JMenuItem mntmNewEntry1 = new JMenuItem("New Entry");
		mntmNewEntry1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				OutdoorPillsEntryFromStore outdoorPillsEntry = new OutdoorPillsEntryFromStore();
				outdoorPillsEntry.setModal(true);
				outdoorPillsEntry.setVisible(true);
				ua.check_activity(storename, 199, 5);
			}
		});
		mntmNewEntry1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmNewEntry1);

		JMenuItem mntmNewMenuItem = new JMenuItem("Previouse Entries");
		mntmNewMenuItem.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmNewMenuItem);
		//ua.check_activity(storename, 200, 5);
		
		JMenuItem mntmMedicineBillRequest = new JMenuItem("Medicine Bill Request");
		mntmMedicineBillRequest.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OutdoorPillsPerforma outdoorPillsEntry = new OutdoorPillsPerforma();
				outdoorPillsEntry.setModal(true);
				outdoorPillsEntry.setVisible(true);
				ua.check_activity(storename, 202, 5);
			}
		});
		
		JMenuItem menuItem1 = new JMenuItem("Procedure Entry");
		menuItem1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OutdoorProcedureEntry outdoorProcedureEntry = new OutdoorProcedureEntry();
				outdoorProcedureEntry.setModal(true);
				outdoorProcedureEntry.setVisible(true);
				ua.check_activity(storename, 201, 5);
			}
		});
		menuItem1.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(menuItem1);
		mntmMedicineBillRequest.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnOutdoorPatient1.add(mntmMedicineBillRequest);
		
		JMenu mnReturnFromPatient = new JMenu("Return From Patient");
		mnReturnFromPatient.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnReturnFromPatient);
		
		JMenuItem mntmNewForm_12 = new JMenuItem("Hospital Return");
		mntmNewForm_12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				IndoorPillsReturnFromStore reIndoorPillsReturnFromStore=new IndoorPillsReturnFromStore();
				reIndoorPillsReturnFromStore.setModal(true);
				reIndoorPillsReturnFromStore.setVisible(true);
				ua.check_activity(storename, 194, 5);
			}
		});
		mntmNewForm_12.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnFromPatient.add(mntmNewForm_12);
		JMenuItem mntmNewForm_123 = new JMenuItem("Medical Store Return");
		mntmNewForm_123.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			
				ua.check_activity(storename, 193, 5);
			}
		});
		mntmNewForm_123.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnReturnFromPatient.add(mntmNewForm_123);
		JMenu mnRequestRegister = new JMenu("Dept. Request Register");
		mnRequestRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnRequestRegister);
		
		JMenuItem mntmRegister = new JMenuItem("Register");
		mntmRegister.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				RequestRegister requestRegister=new RequestRegister();
				requestRegister.setModal(true);
				requestRegister.setVisible(true);
				ua.check_activity(storename, 195, 5);
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
				ua.check_activity(storename, 196, 5);
			}
		});
		mntmNewEntry.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnProcedureEntry.add(mntmNewEntry);
		
		
		JMenuItem mntmNewEntry12 = new JMenuItem("Department  Request Pending Register");
		mntmNewEntry12.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IssuedReport newProcedsuresForm=new IssuedReport();
				newProcedsuresForm.setModal(true);
				newProcedsuresForm.setVisible(true);  
				ua.check_activity(storename, 203, 5);
			}
		});
		mntmNewEntry12.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mntmNewEntry12);
		
		JMenu mnExamBom = new JMenu("Exam BOM");
		mnExamBom.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnItemIssue.add(mnExamBom);
		
		JMenuItem menuItem = new JMenuItem("New Entry");
		menuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				ExamBomForm examBomForm=new ExamBomForm();
				examBomForm.setModal(true);
				examBomForm.setVisible(true);
				ua.check_activity(storename, 197, 5);
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
				ua.check_activity(storename, 198, 5);
			}
		});
		mntmDetails.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnExamBom.add(mntmDetails);
		
		JMenu mnMyStock = new JMenu("My Stock");
		mnMyStock.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/stockicon.png")));
		mnMyStock.setFont(new Font("Tahoma", Font.BOLD, 15));
		menuBar.add(mnMyStock);
		
		JMenuItem mntmStockRegister = new JMenuItem("Stock Register");
		mntmStockRegister.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			
				TotalStock totalStock=new TotalStock();
				totalStock.setModal(true);
				totalStock.setVisible(true);
				ua.check_activity(storename, 207, 5);
			}
		});
		mntmStockRegister.setFont(new Font("Tahoma", Font.BOLD, 14));
		mnMyStock.add(mntmStockRegister);
		if(store_account_access.equals("1")){
			JMenu managestockaccount = new JMenu("Store Account");
			managestockaccount.setIcon(new ImageIcon(StoreMain.class.getResource("/icons/stockicon.png")));
			managestockaccount.setFont(new Font("Tahoma", Font.BOLD, 15));
			menuBar.add(managestockaccount);
			
			JMenuItem mntmStockRegister1 = new JMenuItem("New Store Account");
			mntmStockRegister1.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				
					NewStoreAccount newStoreAccount = new NewStoreAccount();
					newStoreAccount.setModal(true);
					newStoreAccount.setVisible(true);
				}
			});
			mntmStockRegister1.setFont(new Font("Tahoma", Font.BOLD, 14));
			managestockaccount.add(mntmStockRegister1);
			JMenuItem mntmStockRegister2 = new JMenuItem("Manage Store Account");
			mntmStockRegister2.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
				
					StoreAccountBrowser storeAccountBrowser = new StoreAccountBrowser();
					storeAccountBrowser.setModal(true);
					storeAccountBrowser.setVisible(true);
				}
			});
			mntmStockRegister2.setFont(new Font("Tahoma", Font.BOLD, 14));
			managestockaccount.add(mntmStockRegister2);
		}
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	

}
