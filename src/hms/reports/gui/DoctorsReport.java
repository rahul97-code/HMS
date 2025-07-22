package hms.reports.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.DoctorWiseSummaryReportExcel;
import hms.patient.slippdf.DoctorWiseSummaryReportPDF;
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
public class DoctorsReport extends JDialog {

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
	public DoctorsReport() {
		setResizable(false);
		setTitle("Doctors Report");
		
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DoctorsReport.class.getResource("/icons/rotaryLogo.png")));
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
				"Summary Report", "Doctor Report","Cancelled Receipt Report","Bed Occupancy Report","Exams Report","IPD Discharge Patient","IPD Pending List" }));
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

		JButton btnNewButton_2 = new JButton("PDF");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String doctorName = comboBox.getSelectedItem().toString();
				
				try {
					new DoctorWiseSummaryReportPDF(dateFrom, dateTo,doctorName);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_2.setBounds(59, 212, 87, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(null);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DoctorsReport.class
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
		btnCancel.setIcon(new ImageIcon(DoctorsReport.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(252, 212, 87, 35);
		panel.add(btnCancel);
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				String doctorName = comboBox.getSelectedItem().toString();
				
				try {
					new DoctorWiseSummaryReportExcel(dateFrom, dateTo,doctorName);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(155, 212, 87, 35);
		panel.add(btnExcel);
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
