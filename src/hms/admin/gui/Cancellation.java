package hms.admin.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.slippdf.OPDSlippdf2;
import hms1.ipd.database.IPDPaymentsDBConnection;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JTextArea;

@SuppressWarnings("serial")
public class Cancellation extends JDialog {

	private JPanel contentPane;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom, dateTo;
	private JComboBox comboBox;
	private JLabel lblInfo4;
	private JLabel lblInfo2;
	private JLabel lblInfo3;
	private JLabel lblInfo1;
	private JTextField recieptNoTB;
	int examStartNo = 0, examEndNo = 0;
	String amountRecieptID = "",can_cat="OPD",p_name,p_id,reciept_no,amount,remarks,cancelled_by,generate_date;
	private JLabel lblInfo6;
	private JLabel lblInfo5;
	private JTextArea textArea;
	String[] data = new String[20];
	public static void main(String[] arg) {
		new Cancellation().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public Cancellation() {
		setResizable(false);
		setTitle("Cancellation");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				Cancellation.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 544, 467);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		cancelled_by="Admin";
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(10, 11, 521, 417);
		contentPane.add(panel);
		panel.setLayout(null);
		comboBox = new JComboBox<Object>();
		comboBox.setBounds(145, 14, 342, 28);
		panel.add(comboBox);
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "OPD",
				"EXAM", "CARDS", "MISC" }));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				recieptNoTB.setText("");
				can_cat=comboBox.getSelectedItem()+"";
			}
		});
		JLabel lblSelectDisease = new JLabel("Select Charges Type");
		lblSelectDisease.setBounds(10, 21, 125, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblInfo1 = new JLabel("info1");
		lblInfo1.setBounds(23, 133, 238, 14);
		panel.add(lblInfo1);

		lblInfo2 = new JLabel("info1");
		lblInfo2.setBounds(271, 133, 240, 14);
		panel.add(lblInfo2);

		lblInfo3 = new JLabel("info1");
		lblInfo3.setBounds(23, 179, 238, 14);
		panel.add(lblInfo3);

		lblInfo4 = new JLabel("info1");
		lblInfo4.setBounds(271, 179, 240, 14);
		panel.add(lblInfo4);

		recieptNoTB = new JTextField();
		recieptNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		recieptNoTB.setBounds(145, 53, 342, 28);
		panel.add(recieptNoTB);
		recieptNoTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = recieptNoTB.getText() + "";
				if (!str.equals("")) {
					switch (comboBox.getSelectedIndex()) {
					case 0:
						opd(str);
						break;
					case 1:
						exam(str);
						break;
					case 2:
						cards(str);
						break;
					case 3:
						amountRecieptID = str;
						misc(str);
						break;
					case 4:
						ipd(str);
						break;
					case 5:

						break;

					default:
						break;
					}
				} else {
					lblInfo1.setText("");
					lblInfo2.setText("");
					lblInfo3.setText("");
					lblInfo4.setText("");
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = recieptNoTB.getText() + "";
				if (!str.equals("")) {
					switch (comboBox.getSelectedIndex()) {
					case 0:
						opd(str);
						break;
					case 1:
						exam(str);
						break;
					case 2:
						cards(str);
						break;
					case 3:
						amountRecieptID = str;
						misc(str);
						break;
					case 4:
						ipd(str);
						break;
					case 5:

						break;

					default:
						break;
					}
				} else {
					lblInfo1.setText("");
					lblInfo2.setText("");
					lblInfo3.setText("");
					lblInfo4.setText("");
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = recieptNoTB.getText() + "";
				if (!str.equals("")) {
					switch (comboBox.getSelectedIndex()) {
					case 0:
						opd(str);
						break;
					case 1:
						exam(str);
						break;
					case 2:
						cards(str);
						break;
					case 3:
						amountRecieptID = str;
						misc(str);
						break;
					case 4:
						ipd(str);
						break;
					case 5:

						break;

					default:
						break;
					}
				} else {

					lblInfo1.setText("");
					lblInfo2.setText("");
					lblInfo3.setText("");
					lblInfo4.setText("");
				}
			}
		});

		JLabel lblRecieptNo = new JLabel("Reciept No. :");
		lblRecieptNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRecieptNo.setBounds(10, 60, 125, 14);
		panel.add(lblRecieptNo);

		final JButton btnNewButton = new JButton("Cancel Reciept");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (lblInfo1.getText().equals("")||lblInfo1.getText().equals("info1")) {
					JOptionPane.showMessageDialog(null, "please select reciept ",
							"ERROR", JOptionPane.ERROR_MESSAGE);
				} else {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							Cancellation.this, "Are you sure to cancel this slip", "Cancellation",
							dialogButton);
					if (dialogResult == 0) {
					
						try {
							btnNewButton.setEnabled(false);
							inserCancelled();
							recieptNoTB.setText("");
							btnNewButton.setEnabled(true);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							btnNewButton.setEnabled(true);
						}
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 12));
		btnNewButton.setBounds(85, 371, 358, 36);
		panel.add(btnNewButton);
		
		lblInfo5 = new JLabel("info1");
		lblInfo5.setBounds(25, 224, 238, 14);
		panel.add(lblInfo5);
		
		lblInfo6 = new JLabel("info1");
		lblInfo6.setBounds(273, 224, 238, 14);
		panel.add(lblInfo6);
		
		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setLineWrap(true);
		textArea.setRows(50);
		textArea.setBounds(33, 283, 465, 77);
		panel.add(textArea);
		
		JLabel lblEnterDescription = new JLabel("Enter Description :");
		lblEnterDescription.setBounds(33, 260, 115, 14);
		panel.add(lblEnterDescription);
	}

	public void opd(String opdID) {
		lblInfo1.setText("");
		lblInfo2.setText("");
		lblInfo3.setText("");
		lblInfo4.setText("");
		lblInfo5.setText("");
		lblInfo6.setText("");
		try {
			OPDDBConnection db = new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId2(opdID);
			while (rs.next()) {
				
				p_id= rs.getObject(1).toString();
				p_name= rs.getObject(2).toString();
				generate_date=rs.getObject(4).toString();
				amount= rs.getObject(5).toString();
				reciept_no=rs.getObject(6).toString();
				lblInfo1.setText("Patient ID : " +p_id);
				lblInfo2.setText("Patient Name : " + p_name);
				lblInfo3.setText("Doctor Name : " + rs.getObject(3).toString());
				lblInfo4.setText("OPD Date : " + generate_date);
				lblInfo5.setText("OPD Amount : " +amount);
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void exam(String examID) {
		lblInfo1.setText("");
		lblInfo2.setText("");
		lblInfo3.setText("");
		lblInfo4.setText("");
		lblInfo5.setText("");
		lblInfo6.setText("");
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllExamsReciept(examID);
			while (rs.next()) {

				lblInfo1.setText("Exam Category : "
						+ rs.getObject(1).toString());
				p_name= rs.getObject(2).toString();
				amount= rs.getObject(3).toString();
				generate_date=rs.getObject(4).toString();
				
				lblInfo2.setText("Patient Name : " + p_name);
				lblInfo3.setText("Amount : " +amount);
				lblInfo4.setText("OPD Date : " + generate_date);
				
				examStartNo = Integer.parseInt(rs.getObject(5).toString());
				examEndNo = Integer.parseInt(rs.getObject(6).toString());
				p_id= rs.getObject(7).toString();
				reciept_no= rs.getObject(8).toString();
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void cards(String cardID) {
		lblInfo1.setText("");
		lblInfo2.setText("");
		lblInfo3.setText("");
		lblInfo4.setText("");
		lblInfo5.setText("");
		lblInfo6.setText("");
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllCardsReciept(cardID);
			while (rs.next()) {
				amountRecieptID = rs.getObject(1).toString();
				reciept_no=amountRecieptID;
				generate_date= rs.getObject(2).toString();
				
				p_id= rs.getObject(3).toString();
				p_name= rs.getObject(4).toString();
				amount= rs.getObject(5).toString();
				
				lblInfo1.setText("Reciept ID : " + amountRecieptID);
				lblInfo2.setText("Patient ID : " + p_id);
				lblInfo3.setText("Patient Name : " + p_name);
				lblInfo4.setText("Amount : " +amount);
				lblInfo5.setText("OPD Date : " + generate_date);

			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void misc(String miscID) {
		lblInfo1.setText("");
		lblInfo2.setText("");
		lblInfo3.setText("");
		lblInfo4.setText("");
		lblInfo5.setText("");
		lblInfo6.setText("");
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllMISCReciept(miscID);
			while (rs.next()) {
				p_id="N/A";
				p_name= rs.getObject(3).toString();
				amount= rs.getObject(1).toString();
				generate_date=rs.getObject(4).toString();
				reciept_no=rs.getObject(5).toString();
				lblInfo2.setText("Doctor Name : " + rs.getObject(2).toString());
				lblInfo1.setText("Patient Name : " +p_name);
				lblInfo3.setText("Amount : " +amount);
				lblInfo4.setText("Date : " +generate_date );

			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void ipd(String ipdID) {
		lblInfo1.setText("");
		lblInfo2.setText("");
		lblInfo3.setText("");
		lblInfo4.setText("");
		lblInfo5.setText("");
		lblInfo6.setText("");
		try {
			IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
			ResultSet rs = db.retrieveAllData(ipdID);
			while (rs.next()) {
				
				p_id= rs.getObject(1).toString();
				p_name= rs.getObject(2).toString();
				amount= rs.getObject(4).toString();
				generate_date= rs.getObject(3).toString();
				reciept_no= rs.getObject(5).toString();
				lblInfo2.setText("Patient ID : " + p_id);
				lblInfo1.setText("Patient Name : " + p_name);
				lblInfo3.setText("Amount : " + amount);
				lblInfo4.setText("Date : " + generate_date);
				lblInfo4.setText("Slip : " + reciept_no);

			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	public void inserCancelled() throws Exception
	{
		
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		CancelledDBConnection cancelledDBConnection=new CancelledDBConnection();
		
		data[0] = can_cat;
		data[1] = p_id;
		data[2] = p_name;
		data[3] = reciept_no;
		data[4] = amount;
		data[5] = cancelled_by;
		data[6] = generate_date;
		data[7] = new DateFormatChange().StringToMysqlDate(new Date());
		data[8] =""+timeFormat.format(cal1.getTime());
		data[9] = textArea.getText();
		
		
		cancelledDBConnection.inserData(data);
		
		cancelledDBConnection.closeConnection();
		
	}
	
//
//	public void updateMISC_CARD(String recieptID) {
//
//		try {
//			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
//			db.updateDataCacellation(recieptID);
//			db.closeConnection();
//		} catch (Exception ex) {
//			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//		JOptionPane.showMessageDialog(null, "Reciept Cancelled Successfuly ",
//				"Cancelled", JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	public void updateEXAMS(String examID) {
//		try {
//			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
//			db.updateDataCacellationExam(examID);
//			db.closeConnection();
//		} catch (Exception ex) {
//			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//
//		for (int i = examStartNo; i <= examEndNo; i++) {
//			updateTestData(i + "");
//		}
//		JOptionPane.showMessageDialog(null, "Reciept Cancelled Successfuly ",
//				"Cancelled", JOptionPane.INFORMATION_MESSAGE);
//
//	}
//
//	public void updateTestData(String exam_id) {
//
//		ExamDBConnection db = new ExamDBConnection();
//		try {
//			db.updateExamData(exam_id, "0", "TEST CANCEL");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		db.closeConnection();
//	}
//
//	public void updateOPD(String opdID) {
//		try {
//			OPDDBConnection db = new OPDDBConnection();
//			db.updateCancellation(opdID);
//			db.closeConnection();
//		} catch (Exception ex) {
//			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//		JOptionPane.showMessageDialog(null, "Reciept Cancelled Successfuly ",
//				"Cancelled", JOptionPane.INFORMATION_MESSAGE);
//	}
//
//	public void updateIPD(String ipdID) {
//		try {
//			IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
//			db.updateCancellation(ipdID);
//			db.closeConnection();
//		} catch (Exception ex) {
//			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//		JOptionPane.showMessageDialog(null, "Reciept Cancelled Successfuly ",
//				"Cancelled", JOptionPane.INFORMATION_MESSAGE);
//	}
}
