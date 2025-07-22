package hms.exams.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.TestTokenReportPDF;
import hms.reception.gui.ReceptionMain;
import hms.reception.gui.TokenEntryExam;

import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import jcifs.smb.SmbFile;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

import UsersActivity.database.UADBConnection;

public class ExamsBrowser extends JDialog {

	UADBConnection ua=new UADBConnection();
	private JPanel contentPane;
	private JTable opdbrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	DefaultComboBoxModel exams = new DefaultComboBoxModel();
	private JComboBox comboBoxExams;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ExamsBrowser frame = new ExamsBrowser("uname");
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
	public ExamsBrowser(final String username) {
		setTitle("Exam Entry List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ExamsBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(400, 150, 950, 524);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		System.out.println("font value" + Font.TRUETYPE_FONT);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);
		setModal(true);
		opdbrowserTable = new JTable();
		opdbrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdbrowserTable.getTableHeader().setReorderingAllowed(false);
		opdbrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdbrowserTable
				.setModel(new DefaultTableModel(new Object[][] {},
						new String[] { "Exam No.", "Patient ID",
								"Patient Name", "Doctor Name", "Exam Date",
								"Exam Name","Exam Charges", "Exam Code" }));
		scrollPane.setViewportView(opdbrowserTable);
		
		opdbrowserTable.addMouseListener(new MouseListener() {
			
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
				         JTable target = (JTable)arg0.getSource();
				         int row = target.getSelectedRow();
				         int column = target.getSelectedColumn();
				         // do some action
				         String receiptID="",receiptID2="",amountReciptID="",Amount="",time="";
				         Object selectedObject = opdbrowserTable.getModel().getValueAt(row, 0);
				         AmountReceiptDBConnection amountReceiptDBConnection=new AmountReceiptDBConnection();
				         
				         ResultSet rs;
//				         if(Integer.parseInt(selectedObject.toString())>262656){
				        	  rs=amountReceiptDBConnection.retrieveAllRecieptNoNew(selectedObject+"");
//				         }
//				         else{
//				        	  rs=amountReceiptDBConnection.retrieveAllRecieptNo(selectedObject+""); 
//				         }
				         try {
				        	 while(rs.next()){
								receiptID=rs.getObject(1).toString(); 
								receiptID2=rs.getObject(2).toString(); 
								amountReciptID=rs.getObject(3).toString(); 
								Amount=rs.getObject(4).toString(); 
								time=rs.getObject(5).toString(); 
								
							 }
						} catch (SQLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				    //     copyFileUsingJava7Files(,"ExamSlip/" + receiptID + ".pdf")
				       System.out.print("Reciept id :"  +receiptID);
				       System.out.print("Reciept id2 :"  +receiptID2);
				       System.out.print("Amount: "  +amountReciptID);
				 			try {
								new ExamSlippdfRegenerate(receiptID,receiptID2,amountReciptID,Amount,time);
							} catch (DocumentException | IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
				  }
			}
		});
		populateTable(DateFormatChange.StringToMysqlDate(new Date()),
				DateFormatChange.StringToMysqlDate(new Date()));
		JButton btnNewButton = new JButton("New Exam");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				
				if (ReceptionMain._id.equals("0")) {
					ExamEntery opdEntery = new ExamEntery(ExamsBrowser.this);
					opdEntery.setVisible(true);
				} else {
					TokenEntryExam tokenEntry = new TokenEntryExam(ExamsBrowser.this);
					tokenEntry.setModal(true);
					tokenEntry.setVisible(true);
					ua.check_activity(username,117,0);

				}
			}
		});
		btnNewButton.setIcon(new ImageIcon(ExamsBrowser.class
				.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(238, 440, 160, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(ExamsBrowser.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(673, 440, 160, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 464);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(10, 146, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(10, 201, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		//dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(56, 182, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(56, 126, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.setBounds(10, 268, 49, 23);
		panel.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setBounds(56, 268, 59, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setBounds(117, 268, 73, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);

		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(56, 247, 73, 14);
		panel.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_2 = new JButton("Search");
		btnNewButton_2.setBounds(40, 324, 111, 35);
		panel.add(btnNewButton_2);
		btnNewButton_2.setIcon(new ImageIcon(ExamsBrowser.class
				.getResource("/icons/zoom_r_button.png")));
		btnNewButton_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(ExamsBrowser.class
				.getResource("/icons/graphics-hospitals-521568.gif")));
		lblNewLabel.setBounds(39, 370, 111, 83);
		panel.add(lblNewLabel);
		
		comboBoxExams = new JComboBox();
		comboBoxExams.setBounds(10, 11, 172, 25);
		panel.add(comboBoxExams);
		
		JButton btnGet = new JButton("GET");
		btnGet.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				try {
					new TestTokenReportPDF(dateFrom, dateTo, comboBoxExams.getSelectedItem().toString());
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnGet.setBounds(20, 47, 147, 23);
		panel.add(btnGet);
		
		JButton btnIpdNewExam = new JButton("IPD Exam Entry");
		btnIpdNewExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				IPDExamEntery opdEntery = new IPDExamEntery(ExamsBrowser.this);
				opdEntery.setVisible(true);
			}
		});
		btnIpdNewExam.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdNewExam.setBounds(451, 440, 160, 35);
		contentPane.add(btnIpdNewExam);
		getAllExamList();
	}

	public void populateTable(String dateFrom, String dateTo) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllDataAmountReceipt(dateFrom, dateTo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();
			
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
					new String[] { "Receipt No", "Doctor Name", "Patient Name",
							"Amount","Exam Date",
							 }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			opdbrowserTable.setModel(model);
			TableColumn columnsize = null;
			for (int i = 0; i < 5; i++) {
				columnsize = opdbrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110);
				if (i == 1 || i == 2 || i == 3) {
					columnsize.setPreferredWidth(150);
				}
				if (i == 5)
					columnsize.setPreferredWidth(150);
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	public void getAllExamList() {
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExams();
		try {
			while (resultSet.next()) {
				
				exams.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		comboBoxExams.setModel(exams);
		comboBoxExams.setSelectedIndex(0);

	}
	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = remoteFile.getInputStream();

		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();
	}
	public void OPenFileWindows(String path) {
		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					JOptionPane.showMessageDialog(null, "Please add Exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		} catch (Exception ert) {
			
			
		}
	}
}
