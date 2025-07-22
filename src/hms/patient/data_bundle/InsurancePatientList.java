package hms.patient.data_bundle;

import hms.admin.gui.OPDTypeBrowser;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.pricemaster.database.PriceMasterDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.SwingConstants;
import java.awt.Font;
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

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;

public class InsurancePatientList extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable patientDataTable;
	private JDateChooser dateFromChooser;
	private JDateChooser dateToChooser;
	private JComboBox selectInsuranceCB;

	final DefaultComboBoxModel insuranceType = new DefaultComboBoxModel();

	String insuranceSTR, dateFromSTR, dateToSTR;
	PatientHistoryESI patientHistoryDate = null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			InsurancePatientList dialog = new InsurancePatientList();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsurancePatientList() {
		setResizable(false);
		setTitle("Patient Record");
		setBounds(100, 100, 490, 456);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 454, 128);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Date From");
		lblNewLabel.setBounds(33, 11, 122, 20);
		panel.add(lblNewLabel);

		JLabel lblDateTo = new JLabel("Date To");
		lblDateTo.setBounds(33, 42, 122, 20);
		panel.add(lblDateTo);

		JLabel lblSelectInsurance = new JLabel("Select Insurance");
		lblSelectInsurance.setBounds(33, 73, 122, 20);
		panel.add(lblSelectInsurance);

		selectInsuranceCB = new JComboBox();
		selectInsuranceCB.setBounds(180, 73, 215, 20);
		panel.add(selectInsuranceCB);

		selectInsuranceCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					insuranceSTR = selectInsuranceCB.getSelectedItem()
							.toString();

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});

		dateFromChooser = new JDateChooser();
		dateFromChooser.setBounds(180, 11, 215, 20);
		panel.add(dateFromChooser);

		dateFromChooser.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFromSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		dateFromChooser.setDate(new Date());

		dateToChooser = new JDateChooser();
		dateToChooser.setBounds(180, 42, 215, 20);
		panel.add(dateToChooser);

		dateToChooser.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateToSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							// populateTable1(insuranceSTR);
						}
					}
				});
		dateToChooser.setDate(new Date());

		JButton btnGetData = new JButton("Get Data");
		btnGetData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				populateTable1(insuranceSTR);
			}
		});
		btnGetData.setBounds(190, 100, 205, 23);
		panel.add(btnGetData);
		JLabel lblNewLabel_1 = new JLabel("Patients List");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setBounds(10, 138, 454, 25);
		contentPanel.add(lblNewLabel_1);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 163, 454, 244);
		contentPanel.add(scrollPane);

		patientDataTable = new JTable();
		patientDataTable.setModel(new DefaultTableModel(new Object[][] { {
				null, null, null, null }, }, new String[] { "Patient ID",
				"Patient Name", "Date", "Insurance" }));
		patientDataTable.getColumnModel().getColumn(0).setMinWidth(120);
		patientDataTable.getColumnModel().getColumn(1).setMinWidth(140);
		patientDataTable.getColumnModel().getColumn(2).setMinWidth(30);
		scrollPane.setViewportView(patientDataTable);

		
		patientDataTable.addMouseListener(new MouseListener() {

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
				if (arg0.getClickCount() == 2) {
					JTable target = (JTable) arg0.getSource();
					int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action
					
					Object selectedObject = patientDataTable.getModel()
							.getValueAt(row, 0);
					if(patientHistoryDate!=null)
					{
						patientHistoryDate.searchPatientTB.setText(selectedObject.toString());
						dispose();
					}
				
				}
			}
		});

		getAllinsurance();

	}

	public void populateTable1(String insuranceType) {
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllPatients(dateFromSTR, dateToSTR,
					insuranceType);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}

			rs = db.retrieveAllPatients(dateFromSTR, dateToSTR, insuranceType);

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
					new String[] { "Patient ID", "Patient Name", "Date",
							"Insurance" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			patientDataTable.setModel(model);
			patientDataTable.getColumnModel().getColumn(0).setMinWidth(120);
			patientDataTable.getColumnModel().getColumn(1).setMinWidth(140);
			patientDataTable.getColumnModel().getColumn(2).setMinWidth(30);

		} catch (SQLException ex) {
			Logger.getLogger(OPDTypeBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void setContext(PatientHistoryESI patientHistoryDate) {
		this.patientHistoryDate=patientHistoryDate;
	}

	public void getAllinsurance() {
		insuranceType.addElement("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceType.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		selectInsuranceCB.setModel(insuranceType);
		selectInsuranceCB.setSelectedIndex(0);
	}
}
