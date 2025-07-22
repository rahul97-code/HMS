package hms.reports.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.CancelledReceiptReportPDF;
import hms.patient.slippdf.IPDDischargeReportPDF;
import hms.patient.slippdf.IPDAdvanceReportPDF;
import hms.patient.slippdf.IPDPandingsReportPDF;
import hms.patient.slippdf.MyCashReportPDF;
import hms.patient.slippdf.NearExpiryPdf;
import hms.patient.slippdf.NewSummaryReportExcel;
import hms.patient.slippdf.NewSummaryReportPDF;
import hms.patient.slippdf.NewSummaryReportPDF_copy;
import hms.patient.slippdf.TestCategoryWiseReportExcel;
import hms.patient.slippdf.TestCategoryWiseReportPDF;
import hms.patient.slippdf.WardsReportPDF;
import hms.reports.excels.DepartmentItemIssuedReportExcel;
import hms.reports.excels.DepartmentRegisterReportExcel;
import hms.reports.excels.Indoor_OutdoorExcel;
import hms.reports.excels.InvoiceItemsReportExcel;
import hms.reports.excels.PatientPillsRegisterReportExcel;
import hms.reports.excels.PatientWiseDaysReportExcel;
import hms.reports.excels.PillsRegisterReportExcel;
import hms.reports.excels.WardDoctorWiseDaysReportExcel;
import hms.reports.excels.WardsIssueConsumeExcel;
import hms.reporttables.DepartmentToPatientLog;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

@SuppressWarnings("serial")
public class DateSelection extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;

	/**
	 * Create the frame.
	 */
	public DateSelection(final int index) {
		setResizable(false);
		setTitle("Date Selection");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DateSelection.class.getResource("/icons/rotaryLogo.png")));
		setBounds(400, 150, 334, 266);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		setModal(true);
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 306, 213);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(106, 54, 178, 25);
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
		dateToDC.setBounds(106, 90, 178, 25);
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
		//dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(10, 95, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(10, 54, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("PDF");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (index == 0) {
					try {
						new NewSummaryReportPDF_copy(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} 
				else if (index == 1) {
					try {
						new CancelledReceiptReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if (index == 2) {
					try {
						new WardsReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if (index== 3) {
					try {
						new TestCategoryWiseReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (index == 4) {
					try {
						new IPDDischargeReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (index == 5) {
					try {
						new IPDPandingsReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

				else if (index == 6) {
					try {
						new MyCashReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (index == 7) {
					try {
						new IPDAdvanceReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				else if (index == 8) {
					//new SummaryReportExcel(dateFrom, dateTo);
				}
				else if (index == 9) {
					try {
						new InsuranceSummaryReportPDF(dateFrom, dateTo);
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}else if (index == 10) {
					try {
						JRadioButton allButton = new JRadioButton("All");
						JRadioButton store1Button = new JRadioButton("Store 1");
						JRadioButton store2Button = new JRadioButton("Store 2");

						// Set "All" as selected by default
						allButton.setSelected(true);

						// Group the radio buttons
						ButtonGroup group = new ButtonGroup();
						group.add(allButton);
						group.add(store1Button);
						group.add(store2Button);

						// Add buttons to a panel
						JPanel panel = new JPanel();
						panel.add(allButton);
						panel.add(store1Button);
						panel.add(store2Button);

						// Show the option pane
						int result = JOptionPane.showConfirmDialog(
								null,
								panel,
								"Select Store",
								JOptionPane.OK_CANCEL_OPTION,
								JOptionPane.QUESTION_MESSAGE
								);
						String loc="";
						if (result == JOptionPane.OK_OPTION) {
							if (store1Button.isSelected()) {
								loc="store-1";
							} else if (store2Button.isSelected()) {
								loc="store-2";			
							} 
							new NearExpiryPdf(dateFrom, dateTo,loc);}
					} catch (DocumentException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});
		btnNewButton_2.setBounds(10, 155, 87, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(null);
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(DateSelection.class
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
		btnCancel.setIcon(new ImageIcon(DateSelection.class.getResource("/icons/CANCEL.PNG")));
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnCancel.setBounds(209, 155, 87, 35);
		panel.add(btnCancel);

		JLabel lblHms = new JLabel("HMS");
		lblHms.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblHms.setHorizontalAlignment(SwingConstants.CENTER);
		lblHms.setBounds(76, 11, 133, 32);
		panel.add(lblHms);

		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(DateSelection.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();




					if (index == 0) {
						try {
							new NewSummaryReportExcel(dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} 
					else if (index == 1) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}

					else if (index == 2) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}

					else if (index== 3) {
						try {
							new TestCategoryWiseReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 4) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else if (index == 5) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}

					else if (index == 6) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else if (index == 7) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else if (index == 8) {
						new SummaryReportExcel(file.toPath().toString(),dateFrom, dateTo);
					}
					else if (index == 9) {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
					else if (index == 10) {
						try {
							new PillsRegisterReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 11) {
						try {
							new DepartmentRegisterReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 12) {
						try {
							new PatientPillsRegisterReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 13) {
						try {
							new WardsIssueConsumeExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 14) {
						try {
							new Indoor_OutdoorExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 15) {
						try {
							new InvoiceItemsReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 16) {
						try {
							new DepartmentItemIssuedReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 17) {
						try {
							new WardDoctorWiseDaysReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else if (index == 18) {
						try {
							new PatientWiseDaysReportExcel(file.toPath().toString(),dateFrom, dateTo);
						} catch (DocumentException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} catch (IOException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
					else {
						JOptionPane
						.showMessageDialog(
								null,
								"This report not found",
								"Error", JOptionPane.ERROR_MESSAGE);
					}
				}
			}
		});
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(106, 155, 87, 35);
		panel.add(btnExcel);
	}




}
