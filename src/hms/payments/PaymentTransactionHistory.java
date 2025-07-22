package hms.payments;

import hms.doctor.database.DoctorDBConnection;
import hms.exams.gui.ExamEntery;
import hms.main.DateFormatChange;
import hms.misc.gui.MiscAmountEntery;
import hms.opd.gui.OPDBrowser;
import hms.opd.gui.OPDEntery;
import hms.patient.slippdf.PO_PDF;
import hms.payments.database.PaymentsDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.reports.excels.POExcel;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.PODBConnection;
import hms.store.gui.ItemIssueLogNEW;
import hms.test.free_test.ExcelFile;
import hms1.ipd.gui.IPDBill;
import hms1.ipd.gui.IPDDeposit;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JComboBox;
import javax.swing.JComponent;

public class PaymentTransactionHistory extends JDialog {

	public JPanel contentPane;
	private static JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo,wrdName="",insName="",docName="";
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "";
	private JTextField searchField;
	Vector originalTableModel;
	DefaultComboBoxModel typeModal = new DefaultComboBoxModel();
	DefaultComboBoxModel MachineModal = new DefaultComboBoxModel();
	DefaultComboBoxModel UsrModal = new DefaultComboBoxModel();
	private final List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
	Vector<String> uniqueWardSet = new Vector<>();
	Vector<String> CHANGE_ID=new Vector<>();
	Vector<String> IPD_ID=new Vector<>();
	Vector<String> DOC_NAME=new Vector<>();
	Vector<String> P_NAME=new Vector<>();
	Vector<String> P_ID=new Vector<>();
	Vector<String> DOA=new Vector<>();
	Vector<String> INSURANCE=new Vector<>();
	Vector<Integer> MINUTES=new Vector<>();


	Vector<String> BED_IPD=new Vector<>();
	Vector<String> WARD=new Vector<>();
	Vector<String> WARD_CAT=new Vector<>();
	Vector<String> BED_DATE=new Vector<>();
	Vector<Integer> BED_MINUTES=new Vector<>();

	Vector<String> FINAL_WARD=new Vector<>();
	Vector<String> FINAL_IPD_ID=new Vector<>();
	Vector<String> FINAL_DOC_NAME=new Vector<>();
	Vector<String> FINAL_P_NAME=new Vector<>();
	Vector<String> FINAL_P_ID=new Vector<>();
	Vector<String> FINAL_DOA=new Vector<>();
	Vector<String> FINAL_INSURANCE=new Vector<>();
	Vector<Integer> FINAL_MINUTES=new Vector<>();
	Vector<String> MACHINE_TID=new Vector<>();
	Set<String> TypeSet = new HashSet<String>();
	Set<String> MchSet = new HashSet<String>();
	Set<String> UsrSet = new HashSet<String>();
	TableRowSorter<DefaultTableModel> rowSorter;
	private JComboBox UserCB;
	private JComboBox MachineCB;
	private JComboBox TypeCB;



	public static void main(String[] arg) {
		new PaymentTransactionHistory().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public PaymentTransactionHistory() {
		setResizable(false);
		setTitle("Payment History");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				PaymentTransactionHistory.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1144, 542);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 42, 934, 404);
		contentPane.add(scrollPane);

		table = new JTable();
		//		table.setToolTipText("Double Click to reprint PO Slip");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				null, new String[] { "Req ID", "Receipt No", "Machine", "P ID", "P Name", "User", "Doc Name", "Type", "Pay Mode", "URN", "TNX Date", "TNX Modify Date", "Amount", "Final Status" }));
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 489);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 94, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});


		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 149, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 130, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 74, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchField = new JTextField();
		searchField.setBounds(4, 37, 178, 25);
		panel.add(searchField);
		searchField.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);
					}
				});
		searchField.setColumns(10);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearch.setBounds(53, 22, 70, 15);
		panel.add(lblSearch);

		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadDataToTable();
			}
		});
		btnSearch.setIcon(new ImageIcon(PaymentTransactionHistory.class.getResource("/icons/search_hover.png")));
		btnSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnSearch.setBounds(29, 196, 120, 25);
		panel.add(btnSearch);

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(12, 389, 160, 25);
		panel.add(btnExcel);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("POS_TNX_"+dateFrom+"_to_"+dateTo+".xls"));
				if (fileChooser.showSaveDialog(PaymentTransactionHistory.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					new ItemIssueLogNEW().ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(PaymentTransactionHistory.class
				.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.setBounds(12, 426, 160, 25);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(PaymentTransactionHistory.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setIcon(new ImageIcon(PaymentTransactionHistory.class
				.getResource("/icons/CANCEL.PNG")));

		Color successColor = Color.decode("#90EE90"); // LightGreen
		Color cancelledColor = Color.decode("#FFB6C1"); // LightPink
		Color failedColor = Color.decode("#FF7F7F"); // LightCoral
		Color inProgressColor = Color.decode("#FFD700"); // Gold

		// Create and add labels with colors
		JLabel lblNewLabel = new JLabel("TNX-SUCCESS");
		lblNewLabel.setBounds(33, 249, 120, 15);
		lblNewLabel.setForeground(successColor); // Set font color
		panel.add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("TNX-CANCELLED");
		lblNewLabel_1.setBounds(33, 276, 120, 15);
		lblNewLabel_1.setForeground(cancelledColor); // Set font color
		panel.add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("TNX-FAILED");
		lblNewLabel_2.setBounds(33, 303, 100, 15);
		lblNewLabel_2.setForeground(failedColor); // Set font color
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("IN-PROGRESS");
		lblNewLabel_3.setBounds(33, 330, 100, 15);
		lblNewLabel_3.setForeground(inProgressColor); // Set font color
		panel.add(lblNewLabel_3);



		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(198, 450, 934, 50);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

//		JButton btnNewButton_1_1 = new JButton("CHECK STATUS");
//		btnNewButton_1_1.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//
//			}
//		});
//		btnNewButton_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
//		btnNewButton_1_1.setBounds(122, 12, 160, 25);
//		panel_1.add(btnNewButton_1_1);

		JButton btnNewButton_1_2 = new JButton("GENERATE SLIP");
		btnNewButton_1_2.setIcon(new ImageIcon(PaymentTransactionHistory.class.getResource("/icons/Quarterly Sales.png")));
		btnNewButton_1_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row=table.getSelectedRow();
				String req_urn=table.getValueAt(row, 0)+"";
				String PayMode=table.getValueAt(row, 9)+"";
				String urn=table.getValueAt(row, 10)+"";
				String desc=table.getValueAt(row, 5)+"";
				String receipt=table.getValueAt(row, 1)==null?"":table.getValueAt(row, 1).toString();
				double finalAmt=Double.parseDouble(table.getValueAt(row, 13)+"");
				double WithoutChargeAmt=finalAmt/getCharge(PayMode);
				WithoutChargeAmt = Math.round(WithoutChargeAmt * 100.0) / 100.0;
				String pid=table.getValueAt(row, 2)+"";
				if (!receipt.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Receipt Already generated.");
					return;
				}

				try {
					String[] res = new TransactionStatus().getTransactionStatus(urn+"",MACHINE_TID.get(row),""+req_urn);
					if(res!=null && !res[0].contains("INITIATE")) {
						if(res[0].contains("success")) {
							loadDataToTable();					
						}else {
							JOptionPane.showMessageDialog(null, "Receipt can't generated. NOTE: "+res[0]);
							loadDataToTable();		
							return;
						}
					}else {
						JOptionPane.showMessageDialog(null, "Reminder: Your payment is still pending.");
						loadDataToTable();		
						return;
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				switch(table.getValueAt(row,7).toString().replace(" ", "_")) {
				case "EXAM" :{
					ExamEntery ExamEntery=new ExamEntery();
					ExamEntery.setAlreadyPayData(req_urn,finalAmt,WithoutChargeAmt, PayMode, pid, 2);
					ExamEntery.setVisible(true);
					ExamEntery.setModal(true);
					dispose();
					break;
				}
				case "MISC" :{
					MiscAmountEntery MiscAmountEntery=new MiscAmountEntery();
					MiscAmountEntery.setAlreadyPayData(req_urn,finalAmt,WithoutChargeAmt, PayMode, pid, 2);
					MiscAmountEntery.setVisible(true);
					MiscAmountEntery.setModal(true);
					dispose();
					break;}
				case "IPD_PAYMENT" :{
					IPDDeposit IPDDeposit=new IPDDeposit();
					IPDDeposit.setAlreadyPayData(req_urn,finalAmt,WithoutChargeAmt, PayMode, pid, 1,desc);
					IPDDeposit.setVisible(true);
					IPDDeposit.setModal(true);
					dispose();
					break;}
				case "IPD_BILL" :
					IPDBill IPDBill=new IPDBill();
					IPDBill.setAlreadyPayData(req_urn,finalAmt,WithoutChargeAmt, PayMode, pid, 1);
					IPDBill.setVisible(true);
					IPDBill.setModal(true);
					dispose();
					break;
				case "NEW_OPD" :
					OPDEntery OPDEntery=new OPDEntery(new OPDBrowser(ReceptionMain.userName));
					OPDEntery.setAlreadyPayData(req_urn,finalAmt,WithoutChargeAmt, PayMode, pid, 2);
					OPDEntery.setVisible(true);
					OPDEntery.setModal(true);
					dispose();
					break;
				default :
					break;
				}

			}
		});
		btnNewButton_1_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton_1_2.setBounds(325, 12, 373, 25);
		panel_1.add(btnNewButton_1_2);



		UserCB = new JComboBox<String>();
		UserCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				if(UserCB.getSelectedIndex() > 0) {

					applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);		
				}
			}
		});
		UserCB.setBounds(244, 11, 213, 24);
		contentPane.add(UserCB);

		TypeCB = new JComboBox();
		TypeCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(TypeCB.getSelectedIndex() > 0) {
					applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);
				}
			}
		});
		TypeCB.setBounds(536, 11, 213, 24);
		contentPane.add(TypeCB);

		MachineCB = new JComboBox();
		MachineCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(MachineCB.getSelectedIndex() > 0) {
					applyFilters(rowSorter, UserCB, MachineCB,TypeCB,searchField);
				}
			}
		});
		MachineCB.setBounds(828, 11, 200, 24);
		contentPane.add(MachineCB);

		JLabel lblIns = new JLabel("User :");
		lblIns.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblIns.setBounds(205, 15, 43, 15);
		contentPane.add(lblIns);

		JLabel lblDoc = new JLabel("Type :");
		lblDoc.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDoc.setBounds(475, 15, 43, 15);
		contentPane.add(lblDoc);

		JLabel lblWrd = new JLabel("Machine :");
		lblWrd.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblWrd.setBounds(759, 15, 60, 15);
		contentPane.add(lblWrd);


		btnSearch.doClick();

	}

	private static void applyFilters(TableRowSorter<DefaultTableModel> rowSorter, JComboBox<String> userComboBox, JComboBox<String> descComboBox,JComboBox<String> machineComboBox,JTextField searchField) {
		List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();

		// Gender Filter
		String searchText = searchField.getText().trim();
		if (!searchText.isEmpty()) {
			filters.add(RowFilter.regexFilter("(?i)" + searchText));
		}
		String user = (String) userComboBox.getSelectedItem();
		if (!user.trim().isEmpty() && !"select".equals(user)) {
			filters.add(RowFilter.regexFilter(user, 4));
		}
		String desc = (String) descComboBox.getSelectedItem();
		if (!desc.trim().isEmpty() && !"select".equals(desc)) {
			filters.add(RowFilter.regexFilter(desc, 6));
		}
		String machine = (String) machineComboBox.getSelectedItem();
		if (!machine.trim().isEmpty() && !"select".equals(machine)) {
			filters.add(RowFilter.regexFilter(machine, 7));
		}
		// Apply combined filters
		RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
		rowSorter.setRowFilter(combinedFilter);
		table.setRowSorter(rowSorter);
	}


	//	public void countStringsAll()
	//	{
	//		double amt=0;
	//		if (table.getRowCount()>0) {
	//			for(int i=0;i<table.getRowCount();i++)
	//			{
	//				String[] d=table.getValueAt(i, 7).toString().split(".");
	//				for(int j=0;j<d.length;j++)
	//					System.out.println(d[j]);
	//			}
	//
	//		}		
	//	}
	public void setComboBoxData() {
		MachineCB.removeAllItems();
		UserCB.removeAllItems();
		TypeCB.removeAllItems();
		MachineModal.removeAllElements();
		UsrModal.addElement("select");
		MachineModal.addElement("select");
		typeModal.addElement("select");
		for (String value : UsrSet) {
			UsrModal.addElement(value);
		}
		for (String value : MchSet) {
			MachineModal.addElement(value);
		}
		for (String value : TypeSet) {
			typeModal.addElement(value);
		}
		if(MachineModal.getSize()>1) {
			MachineCB.setModel(MachineModal);
			UserCB.setModel(UsrModal);
			TypeCB.setModel(typeModal);
		}

	}
	private void loadDataToTable() {
		MchSet.clear();
		UsrSet.clear();
		TypeSet.clear();
		MACHINE_TID.clear();
		Object Rows_Object_Array[][] = null;
		try {
			System.out.println(dateFrom +" date from ");
			PaymentsDBConnection db=new PaymentsDBConnection();
			ResultSet rs=db.getAllTransactions(dateFrom,dateTo);
			int R=0,C=0;
			C=rs.getMetaData().getColumnCount();
			rs.last();
			R=rs.getRow();
			rs.beforeFirst();	
			Rows_Object_Array = new Object[R][C];
			R = 0;
			while (rs.next()) {
				for (int c = 1; c <= C; c++) {
					Rows_Object_Array[R][c - 1] =rs.getObject(c) ;
				}
				MchSet.add(rs.getString(7));
				UsrSet.add(rs.getString(5));
				TypeSet.add(rs.getString(8));
				MACHINE_TID.add(rs.getString(C));
				R++;
			}
			db.closeConnection();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		DefaultTableModel m=new DefaultTableModel(Rows_Object_Array
				,new String[] { "Req ID", "Receipt No", "P ID", "P Name", "User", "Discription","Machine", "Type", "Final Status", "Pay Mode", "URN", "TNX Date", "TNX Modify Date", "Amount"}){
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}

		};
		table.setModel(m);

		table.getColumnModel().getColumn(0).setMinWidth(50);
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setMinWidth(90);
		table.getColumnModel().getColumn(2).setPreferredWidth(110);
		table.getColumnModel().getColumn(2).setMinWidth(110);
		table.getColumnModel().getColumn(3).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setMinWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		table.getColumnModel().getColumn(6).setPreferredWidth(50);
		table.getColumnModel().getColumn(6).setMinWidth(50);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(130);
		table.getColumnModel().getColumn(8).setMinWidth(130);
		table.getColumnModel().getColumn(9).setPreferredWidth(100);
		table.getColumnModel().getColumn(9).setMinWidth(100);
		table.getColumnModel().getColumn(10).setPreferredWidth(100);
		table.getColumnModel().getColumn(10).setMinWidth(100);
		table.getColumnModel().getColumn(11).setPreferredWidth(120);
		table.getColumnModel().getColumn(11).setMinWidth(120);
		table.getColumnModel().getColumn(12).setPreferredWidth(120);
		table.getColumnModel().getColumn(12).setMinWidth(120);
		rowSorter=new TableRowSorter<DefaultTableModel>(m);
		table.setRowSorter(rowSorter);

		table.setDefaultRenderer(Object.class, new ChangeColorRows());

		if(table.getRowCount()>0)
		{
			get();
			setComboBoxData();
		}
	}

	private double getCharge(String mode) {
		PaymentsDBConnection db=new PaymentsDBConnection();
		ResultSet rs=db.getPayModeCharge(mode);
		try {
			rs.next();
			return rs.getDouble(1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		return 0;
	}
	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}
	class ChangeColorRows extends DefaultTableCellRenderer {
		ChangeColorRows() {
			setHorizontalAlignment(CENTER);
		}
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column
				) {
			Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column
					);
			String status = (String) table.getValueAt(row, 8);
			if ("TNX-SUCCESS".equals(status)) {
				c.setBackground(Color.decode("#90EE90")); 
				c.setForeground(Color.BLACK);
			} else if ("TNX-CANCELLED".equals(status)) {
				c.setBackground(Color.decode("#FFB6C1"));  
				c.setForeground(Color.BLACK);
			} else if ("TNX-FAILED".equals(status)) {
				c.setBackground(Color.decode("#FF7F7F"));  
				c.setForeground(Color.BLACK);
			} else if ("IN-PROGRESS".equals(status)) {
				c.setBackground(Color.decode("#FFD700"));  
				c.setForeground(Color.BLACK);
			} else {
				c.setBackground(Color.decode("#F5F5F5"));  
				c.setForeground(Color.BLACK);
			}
			if (isSelected) {
				((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			} else {
				((JComponent) c).setBorder(BorderFactory.createEmptyBorder());
			}

			return c;
		}
	}
}

