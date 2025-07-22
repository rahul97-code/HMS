package hms.store.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.ReturnChallan_PDF;
import hms.reports.excels.POExcel;
import hms.store.database.ChallanDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.PODBConnection;
import hms.test.free_test.ExcelFile;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

public class ReturnChallanBrowser extends JDialog {

	public JPanel contentPane;
	private JTable pobrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "",selectedType="";
	public static void main(String[] arg) {
		ReturnChallanBrowser browser = new ReturnChallanBrowser();
		browser.setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public ReturnChallanBrowser() {
		setResizable(false);
		setTitle(" Return/Issue Challan List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ReturnChallanBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		pobrowserTable = new JTable();
		pobrowserTable.setToolTipText("Double Click to reprint  Challan Slip");
		pobrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		pobrowserTable.getTableHeader().setReorderingAllowed(false);
		pobrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		pobrowserTable.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Challan No", "Vendor Name", "Date", "Time",
						"Tax", "Surcharge", "Total" }));
		scrollPane.setViewportView(pobrowserTable);
		pobrowserTable.addMouseListener(new MouseListener() {

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
				if (arg0.getClickCount() == 1) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					selectedPOId = pobrowserTable.getModel().getValueAt(row, 0)
							.toString();
					selectedPONum = pobrowserTable.getModel()
							.getValueAt(row, 1).toString();
					selectedVendName = pobrowserTable.getModel()
							.getValueAt(row, 2).toString();
					selectedType = pobrowserTable.getModel()
							.getValueAt(row, 2).toString();
					try {
						new ReturnChallan_PDF(selectedPOId + "",selectedType);
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}


				}
				if (arg0.getClickCount() == 3) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action

					selectedPOId = pobrowserTable.getModel().getValueAt(row, 0)
							.toString();
					selectedPONum = pobrowserTable.getModel()
							.getValueAt(row, 1).toString();
					selectedVendName = pobrowserTable.getModel().getValueAt(row, 2).toString();
				
//
//					selectedVendName = pobrowserTable.getModel()
//							.getValueAt(row, 2).toString();
//					selectedStatus = pobrowserTable.getModel()
//							.getValueAt(row, 6).toString();

				}
			}
		});
		JButton btnNewButton = new JButton("New Challan");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewChallan opdEntery = new NewChallan();
				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		btnNewButton.setVisible(false);
		btnNewButton.setIcon(new ImageIcon(ReturnChallanBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(212, 440, 160, 35);
		contentPane.add(btnNewButton);
		JButton BillButton = new JButton("Return to Vendor");
		BillButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		BillButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		BillButton.setBounds(552, 440, 160, 35);
		contentPane.add(BillButton);
		 BillButton.setVisible(false);
		BillButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// NewInvoice Bill = new NewInvoice();
				if (selectedStatus.equals("CLOSE")) {
					JOptionPane.showMessageDialog(null,
							"PO Closed..", "PO Closed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					System.out.println(selectedVendName+"hhh");
					ReturnChallan opdEntery = new ReturnChallan(selectedPOId,selectedPONum,selectedVendName);
					opdEntery.setModal(true);
					opdEntery.setVisible(true);
				}

			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
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
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 177, 178, 25);
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
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 158, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new POExcel(dateFrom, dateTo);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setVisible(false);
		btnExcel.setIcon(new ImageIcon(ReturnChallanBrowser.class
				.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(42, 440, 160, 35);
		contentPane.add(btnExcel);
		
		JButton btnIssueToHospital = new JButton("Issue To Hospital");
		btnIssueToHospital.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnIssueToHospital.setVisible(false);
		btnIssueToHospital.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// NewInvoice Bill = new NewInvoice();
				if (selectedStatus.equals("CLOSE")) {
					JOptionPane.showMessageDialog(null,
							"PO Closed..", "PO Closed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} else {
					System.out.println(selectedVendName+"hhh");
					IssueToHospital opdEntery = new IssueToHospital(selectedPOId,selectedPONum,selectedVendName);
					opdEntery.setModal(true);
					opdEntery.setVisible(true);
				}

			}
		});
		btnIssueToHospital.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIssueToHospital.setBounds(718, 440, 160, 35);
		contentPane.add(btnIssueToHospital);
		
		JButton btnItemStock = new JButton("Item Stock");
		btnItemStock.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			
				ChallanItems Bill = new ChallanItems(selectedPOId,selectedPONum);
				
				Bill.setModal(true);
				Bill.setVisible(true);
			}
		});
		btnItemStock.setVisible(false);
		btnItemStock.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnItemStock.setBounds(382, 440, 160, 35);
		contentPane.add(btnItemStock);

		populateTable(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
	}

	public void populateTable(String dateFrom, String dateTo) {
		try {
			ChallanDBConnection db = new ChallanDBConnection();
			ResultSet rs = db.retrieveAllDatareturnChallan(dateFrom, dateTo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllDatareturnChallan(dateFrom, dateTo);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "ID", "Challan No","Type","Ref Challan No.", "Vendor Name", "Date",
						 "Total" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			pobrowserTable.setModel(model);
			TableColumn columnsize = null;
			for (int i = 0; i < 6; i++) {
				columnsize = pobrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110);
				if (i == 1 || i == 2 || i == 3) {
					columnsize.setPreferredWidth(150);
				}
				if (i == 5)
					columnsize.setPreferredWidth(60);
			}
		} catch (SQLException ex) {
			Logger.getLogger(ReturnChallanBrowser.class.getName()).log(Level.SEVERE, null,
					ex);
		}
	}
}
