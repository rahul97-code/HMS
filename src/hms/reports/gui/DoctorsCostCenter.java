package hms.reports.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.DoctorWiseCostCenterReportExcel;
import hms.patient.slippdf.DoctorWiseSummaryReportExcel;
import hms.patient.slippdf.DoctorWiseSummaryReportPDF;
import hms.reports.excels.DoctorDataExcel;
import hms.reports.excels.DoctorIPDDataExcel;

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
public class DoctorsCostCenter extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private JComboBox comboBox;
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();

	/**
	 * Create the frame.
	 */
	public DoctorsCostCenter() {
		setResizable(false);
		setTitle("Doctors Cost Center Report");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DoctorsCostCenter.class.getResource("/icons/rotaryLogo.png")));
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
		comboBox.setBounds(44, 11, 270, 28);
		panel.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel<Object>(new String[] {
				"Summary Report", "Doctor Report","Cancelled Receipt Report","Bed Occupancy Report","Exams Report","IPD Discharge Patient","IPD Pending List" }));
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 12));

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(143, 50, 178, 25);
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
		dateToDC.setBounds(143, 86, 178, 25);
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
		lblDateTo.setBounds(51, 86, 73, 25);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(54, 50, 82, 25);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DoctorsCostCenter.class
				.getResource("/icons/opd.gif")));
		lblNewLabel.setBounds(35, 324, 111, 83);
		panel.add(lblNewLabel);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setIcon(new ImageIcon(DoctorsCostCenter.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(212, 231, 138, 35);
		panel.add(btnCancel);
		
		JButton btnExcel = new JButton("Issued Medicines");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String doctorName = comboBox.getSelectedItem().toString();
				
				try {
					new DoctorWiseCostCenterReportExcel(dateFrom, dateTo,doctorName);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(10, 231, 138, 35);
		panel.add(btnExcel);
		
		JButton btnYearWiseReport = new JButton("Year Wise Report");
		btnYearWiseReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				

				String doctorName = comboBox.getSelectedItem().toString();
				
				try {
					new DoctorDataExcel(dateFrom, dateTo,doctorName);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnYearWiseReport.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnYearWiseReport.setBounds(10, 131, 170, 35);
		panel.add(btnYearWiseReport);
		
		JButton btnIpdData = new JButton("IPD DATA");
		btnIpdData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
		String doctorName = comboBox.getSelectedItem().toString();
				
				try {
					new DoctorIPDDataExcel(doctorName,dateFrom, dateTo);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnIpdData.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdData.setBounds(10, 177, 170, 35);
		panel.add(btnIpdData);
		getAllDoctors();
	}
	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllName();
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctors.addElement("Other");
		comboBox.setModel(doctors);
		comboBox.setSelectedIndex(0);
	}
}
