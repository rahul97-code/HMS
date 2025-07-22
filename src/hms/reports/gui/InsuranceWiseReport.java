package hms.reports.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.DoctorWiseSummaryReportExcel;
import hms.patient.slippdf.DoctorWiseSummaryReportPDF;
import hms.patient.slippdf.InsuranceWiseSummaryReportExcel;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class InsuranceWiseReport extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private JComboBox comboBox;
	final DefaultComboBoxModel insuranceModel = new DefaultComboBoxModel();

	/**
	 * Create the frame.
	 */
	public InsuranceWiseReport() {
		setResizable(false);
		setTitle("Insurance Wise Report");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				InsuranceWiseReport.class.getResource("/icons/rotaryLogo.png")));
		setBounds(400, 150, 388, 324);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 360, 277);
		contentPane.add(panel);
		panel.setLayout(null);
		comboBox = new JComboBox<Object>();
		comboBox.setBounds(44, 75, 270, 28);
		panel.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {
				"" }));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(136, 129, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(136, 165, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(44, 165, 73, 25);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(47, 129, 82, 25);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(InsuranceWiseReport.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		
		JLabel lblHms = new JLabel("HMS");
		lblHms.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblHms.setHorizontalAlignment(SwingConstants.CENTER);
		lblHms.setBounds(44, 11, 270, 34);
		panel.add(lblHms);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setIcon(new ImageIcon(InsuranceWiseReport.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(215, 212, 124, 35);
		panel.add(btnCancel);
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String insuranceType = comboBox.getSelectedItem().toString();
				
				try {
					new InsuranceWiseSummaryReportExcel(dateFrom, dateTo,insuranceType);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(59, 212, 124, 35);
		panel.add(btnExcel);
		getAllinsurance();
	}
	public void getAllinsurance() {
		insuranceModel.addElement("Unknown");
		InsuranceDBConnection dbConnection = new InsuranceDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				insuranceModel.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		comboBox.setModel(insuranceModel);
		comboBox.setSelectedIndex(0);
	}
	
}
