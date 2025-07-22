package hms.test.free_test;

import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.test.gui.Test;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.Color;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JComboBox;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.awt.Font;
import javax.swing.JTextField;

public class FreeTestData extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JDateChooser toDatePicker;
	private JComboBox testStatusCB;
	private JComboBox resultRangeMaxCB;
	private JComboBox contactStatusCB;
	private JComboBox resultRangeMinCB;
	private JDateChooser fromDatePicker;
	private JComboBox testNameCB;

	DefaultComboBoxModel testType = new DefaultComboBoxModel();
	DefaultComboBoxModel testStatus = new DefaultComboBoxModel();
	DefaultComboBoxModel contactStatus = new DefaultComboBoxModel();
	DefaultComboBoxModel minRange = new DefaultComboBoxModel();
	DefaultComboBoxModel maxRange = new DefaultComboBoxModel();

	String dateFrom, dateTo, contactDate;
	private JTextField contactByTF;
	private JTextField contactToTF;
	private JDateChooser contactDatePicker;
	private JLabel testNameLB;
	private JLabel testResultsLB;
	private JLabel patientNameLB;
	private JComboBox contactStatusCB2;
	private JLabel mobileNumberLB;
	String testID="";

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			FreeTestData dialog = new FreeTestData();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public FreeTestData() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				FreeTestData.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Test Data");
		setBounds(50, 50, 1074, 487);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(151, 11, 897, 304);
		contentPanel.add(scrollPane_2);

		table = new JTable();
		scrollPane_2.setViewportView(table);
		table.getTableHeader().setReorderingAllowed(true);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
				null, null, null, null, null, null, null }, }, new String[] {
				"Test Number", "OPD Number", "Test Name", "Test Value",
				"Patient ID", "Patient Name", "Mobile Number", "Address",
				"Date", "Test Status", "Contact Status", "Contact To",
				"Contact By", "Contact Date" }));
		table.getColumnModel().getColumn(0).setMinWidth(90);
		table.getColumnModel().getColumn(1).setMinWidth(90);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setMinWidth(110);
		table.getColumnModel().getColumn(5).setPreferredWidth(120);
		table.getColumnModel().getColumn(5).setMinWidth(110);
		table.getColumnModel().getColumn(6).setPreferredWidth(90);
		table.getColumnModel().getColumn(6).setMinWidth(90);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub

						try {
							int selectedRowIndex = table.getSelectedRow();
							testID = table.getModel()
									.getValueAt(selectedRowIndex, 0).toString();

							testNameLB
									.setText(table.getModel()
											.getValueAt(selectedRowIndex, 2)
											.toString());

							testResultsLB
									.setText(table.getModel()
											.getValueAt(selectedRowIndex, 3)
											.toString());

							patientNameLB
									.setText(table.getModel()
											.getValueAt(selectedRowIndex, 5)
											.toString());

							mobileNumberLB
									.setText(table.getModel()
											.getValueAt(selectedRowIndex, 6)
											.toString());

							// Contact Status", "Contact To", "Contact
							// By", "Contact Date

							contactStatusCB2.setSelectedItem(table.getModel()
									.getValueAt(selectedRowIndex, 10)
									.toString());
							contactToTF.setText(table.getModel()
									.getValueAt(selectedRowIndex, 11)
									.toString());
							contactByTF.setText(table.getModel()
									.getValueAt(selectedRowIndex, 12)
									.toString());

						} catch (Exception e2) {
							// TODO: handle exception
						}

					}
				});
		JPanel panel = new JPanel();
		panel.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel.setBounds(10, 11, 131, 419);
		contentPanel.add(panel);
		panel.setLayout(null);

		fromDatePicker = new JDateChooser();
		fromDatePicker.setBounds(10, 64, 111, 20);
		panel.add(fromDatePicker);

		fromDatePicker.getDateEditor().addPropertyChangeListener(
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
		fromDatePicker.setDate(new Date());
		fromDatePicker.setMaxSelectableDate(new Date());
		fromDatePicker.setDateFormatString("yyyy-MM-dd");

		toDatePicker = new JDateChooser();
		toDatePicker.setBounds(10, 109, 111, 20);
		panel.add(toDatePicker);
		toDatePicker.getDateEditor().addPropertyChangeListener(
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
		toDatePicker.setDate(new Date());
		toDatePicker.setMaxSelectableDate(new Date());
		toDatePicker.setDateFormatString("yyyy-MM-dd");

		JLabel lblFromDate = new JLabel("From Date");
		lblFromDate.setBounds(10, 50, 65, 14);
		panel.add(lblFromDate);

		JLabel lblToDate = new JLabel("To Date");
		lblToDate.setBounds(10, 95, 46, 14);
		panel.add(lblToDate);

		testNameCB = new JComboBox();

		testNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					String testName = testNameCB.getSelectedItem().toString();

					testData(testName);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		testNameCB.setBounds(10, 19, 111, 20);
		panel.add(testNameCB);

		JLabel lblStatus = new JLabel("Test Performed");
		lblStatus.setBounds(10, 140, 111, 14);
		panel.add(lblStatus);

		testStatusCB = new JComboBox();
		testStatusCB.setModel(new DefaultComboBoxModel(new String[] { "All",
				"YES", "NO" }));
		testStatusCB.setBounds(10, 158, 111, 20);
		panel.add(testStatusCB);

		contactStatusCB = new JComboBox();
		contactStatusCB.setModel(new DefaultComboBoxModel(new String[] {
				"Pending", "Done", "Call again" }));
		contactStatusCB.setBounds(10, 219, 111, 20);
		panel.add(contactStatusCB);

		JLabel label = new JLabel("Contact Status");
		label.setBounds(10, 201, 111, 14);
		panel.add(label);

		JLabel lblRange = new JLabel("Range");
		lblRange.setBounds(10, 250, 111, 14);
		panel.add(lblRange);

		resultRangeMinCB = new JComboBox();
		resultRangeMinCB.setBounds(10, 272, 111, 20);
		panel.add(resultRangeMinCB);

		resultRangeMaxCB = new JComboBox();
		resultRangeMaxCB.setBounds(10, 313, 111, 20);
		panel.add(resultRangeMaxCB);

		JButton btnFilter = new JButton("Filter");
		btnFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				populateTable();
			}
		});
		btnFilter.setBounds(10, 357, 111, 51);
		panel.add(btnFilter);

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					new ExcelFile("Test Data", table);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setBounds(151, 335, 111, 42);
		contentPanel.add(btnExcel);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new LineBorder(new Color(0, 0, 0)));
		panel_1.setBounds(278, 326, 770, 112);
		contentPanel.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTestName = new JLabel("Test Name");
		lblTestName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTestName.setBounds(22, 11, 121, 14);
		panel_1.add(lblTestName);

		testNameLB = new JLabel("");
		testNameLB.setFont(new Font("Tahoma", Font.BOLD, 13));
		testNameLB.setBounds(164, 11, 167, 14);
		panel_1.add(testNameLB);

		JLabel lblPatientName = new JLabel("Patient Name");
		lblPatientName.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPatientName.setBounds(22, 61, 121, 14);
		panel_1.add(lblPatientName);

		patientNameLB = new JLabel("");
		patientNameLB.setFont(new Font("Tahoma", Font.BOLD, 13));
		patientNameLB.setBounds(164, 61, 167, 14);
		panel_1.add(patientNameLB);

		JLabel lblMobileNumber = new JLabel("Mobile Number");
		lblMobileNumber.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMobileNumber.setBounds(22, 87, 121, 14);
		panel_1.add(lblMobileNumber);

		mobileNumberLB = new JLabel("");
		mobileNumberLB.setFont(new Font("Tahoma", Font.BOLD, 13));
		mobileNumberLB.setBounds(164, 87, 167, 14);
		panel_1.add(mobileNumberLB);

		JLabel lblTestResults = new JLabel("Test Results");
		lblTestResults.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblTestResults.setBounds(22, 36, 121, 14);
		panel_1.add(lblTestResults);

		testResultsLB = new JLabel("");
		testResultsLB.setFont(new Font("Tahoma", Font.BOLD, 13));
		testResultsLB.setBounds(164, 36, 167, 14);
		panel_1.add(testResultsLB);

		JLabel lblContactBy = new JLabel("Contact By");
		lblContactBy.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblContactBy.setBounds(361, 12, 121, 14);
		panel_1.add(lblContactBy);

		JLabel lblContactTo = new JLabel("Contact To");
		lblContactTo.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblContactTo.setBounds(361, 37, 121, 14);
		panel_1.add(lblContactTo);

		JLabel lblContactStatus = new JLabel("Contact Status");
		lblContactStatus.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblContactStatus.setBounds(361, 62, 121, 14);
		panel_1.add(lblContactStatus);

		JLabel lblContactDate = new JLabel("Contact Date");
		lblContactDate.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblContactDate.setBounds(361, 88, 121, 14);
		panel_1.add(lblContactDate);

		contactByTF = new JTextField();
		contactByTF.setBounds(524, 9, 132, 20);
		panel_1.add(contactByTF);
		contactByTF.setColumns(10);

		contactToTF = new JTextField();
		contactToTF.setColumns(10);
		contactToTF.setBounds(524, 34, 132, 20);
		panel_1.add(contactToTF);

		contactStatusCB2 = new JComboBox();
		contactStatusCB2.setModel(new DefaultComboBoxModel(new String[] {
				"Pending", "Done", "Call again" }));
		contactStatusCB2.setBounds(524, 59, 132, 20);
		panel_1.add(contactStatusCB2);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(testID.equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please Select Record", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				String[] data = new String[20];
				data[0] = contactByTF.getText().toString();
				data[1] = contactDate;
				data[2] = contactStatusCB2.getSelectedItem().toString();
				data[3] = contactToTF.getText().toString();
				data[4] = testID;

				FreeTestDBConnection dbConnection = new FreeTestDBConnection();

				try {
					dbConnection.updateData1(data);
					JOptionPane.showMessageDialog(null,
							"Data Updated Successfully", "Data Update",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				populateTable();
				dbConnection.closeConnection();
				testID = "";

				testNameLB
						.setText("");

				testResultsLB
						.setText("");

				patientNameLB
						.setText("");

				mobileNumberLB
						.setText("");


				contactStatusCB2.setSelectedIndex(0);
				contactToTF.setText("");
				contactByTF.setText("");
				contactDatePicker.setDate(new Date());
			}
		});
		btnUpdate.setBounds(666, 11, 89, 93);
		panel_1.add(btnUpdate);

		contactDatePicker = new JDateChooser();
		contactDatePicker.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							contactDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		contactDatePicker.setDate(new Date());
		contactDatePicker.setMaxSelectableDate(new Date());
		contactDatePicker.setDateFormatString("yyyy-MM-dd");
		contactDatePicker.setBounds(524, 87, 132, 20);
		panel_1.add(contactDatePicker);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(151, 388, 111, 42);
		contentPanel.add(btnCancel);

		allTestName();

		populateTable();
	}

	public void allTestName() {
		testType.removeAllElements();

		FreeTestDBConnection freeTestDBConnection = new FreeTestDBConnection();
		ResultSet resultSet = freeTestDBConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				testType.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		freeTestDBConnection.closeConnection();
		testNameCB.setModel(testType);
		testNameCB.setSelectedIndex(0);
	}

	public void testData(String name) {
		minRange.removeAllElements();
		maxRange.removeAllElements();

		int min = 0;
		int max = 0;

		FreeTestDBConnection freeTestDBConnection = new FreeTestDBConnection();
		ResultSet resultSet = freeTestDBConnection.retrieveDataWithName(name);
		try {
			while (resultSet.next()) {
				min = Integer.parseInt(resultSet.getObject(5).toString());
				max = Integer.parseInt(resultSet.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		freeTestDBConnection.closeConnection();

		for (int i = min; i <= max; i++) {
			minRange.addElement("" + i);
			maxRange.addElement("" + i);
		}
		resultRangeMinCB.setModel(minRange);
		resultRangeMinCB.setSelectedIndex(0);
		resultRangeMaxCB.setModel(maxRange);
		resultRangeMaxCB.setSelectedIndex(0);

	}

	public void populateTable() {

		try {
			FreeTestDBConnection db = new FreeTestDBConnection();
			ResultSet rs = db.getData(testNameCB.getSelectedItem().toString(),
					dateFrom, dateTo,
					testStatusCB.getSelectedItem().toString(), contactStatusCB
							.getSelectedItem().toString(), resultRangeMinCB
							.getSelectedItem().toString(), resultRangeMaxCB
							.getSelectedItem().toString());

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs = db.getData(testNameCB.getSelectedItem().toString(), dateFrom,
					dateTo, testStatusCB.getSelectedItem().toString(),
					contactStatusCB.getSelectedItem().toString(),
					resultRangeMinCB.getSelectedItem().toString(),
					resultRangeMaxCB.getSelectedItem().toString());

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
					new String[] { "Test Number", "OPD Number", "Test Name",
							"Test Value", "Patient ID", "Patient Name",
							"Mobile Number", "Address", "Date",
							"Test Performed", "Contact Status", "Contact To",
							"Contact By", "Contact Date" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(0).setMinWidth(90);
			table.getColumnModel().getColumn(1).setMinWidth(90);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setMinWidth(110);
			table.getColumnModel().getColumn(5).setPreferredWidth(120);
			table.getColumnModel().getColumn(5).setMinWidth(110);
			table.getColumnModel().getColumn(6).setPreferredWidth(90);
			table.getColumnModel().getColumn(6).setMinWidth(90);
			table.getColumnModel().getColumn(7).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setMinWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(100);
			table.getColumnModel().getColumn(8).setMinWidth(100);
			table.getColumnModel().getColumn(9).setPreferredWidth(100);
			table.getColumnModel().getColumn(9).setMinWidth(100);
			table.getColumnModel().getColumn(10).setPreferredWidth(100);
			table.getColumnModel().getColumn(10).setMinWidth(100);
			table.getColumnModel().getColumn(11).setPreferredWidth(100);
			table.getColumnModel().getColumn(11).setMinWidth(100);
			table.getColumnModel().getColumn(12).setPreferredWidth(100);
			table.getColumnModel().getColumn(12).setMinWidth(100);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

}
