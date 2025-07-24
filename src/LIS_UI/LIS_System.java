package LIS_UI;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.departments.gui.IndoorProcedureEntry;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.exams.gui.IPDExamEntery;
import hms.main.DateFormatChange;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.FreeExamSlippdfRegenerate;
import hms.patient.slippdf.PO_PDF;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientOnBedReport;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.DischargeSummary;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.AbstractButton;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import com.itextpdf.text.DocumentException;
import com.sun.prism.paint.Stop;
import com.toedter.calendar.JDateChooser;

import LIS_System.LIS_Booking;
import LIS_System.LIS_StatusChecking;
import LIS_System.LIS_Status_result;
import UsersActivity.database.UADBConnection;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;

public class LIS_System extends JDialog {

	UADBConnection ua=new UADBConnection();
	Vector<String> examlisCode=new Vector<String>();
	private JPanel contentPane;
	private JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo;
	private Timer timer;
	private String type="";
	private String search,receipt_id="";
	DefaultComboBoxModel modelCB=new DefaultComboBoxModel();

	private String w_ID,P_ID="",p_NAME="",r_ID="",DATE="";
	static String patientWorkOrderNo = "";
	public JTextField searchTF;
	private JComboBox comboBox;
	private Vector originalTableModel;
	private JLabel lblLoading;
	private AbstractButton pritnbtn;
	public JButton btnNewButton_2;
	private JButton btnRebooking;
	private JTextField examCatCountTF;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					LIS_System frame = new LIS_System("uname");
					frame.setVisible(true);
					//					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LIS_System(final String username) {
		setResizable(false);
		setTitle("LIS SYSTEM");
		setModal(true);
		setIconImage(Toolkit.getDefaultToolkit().getImage(LIS_System.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100,1250, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 158, 1226, 396);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Bouble click to check status\n");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Receipt ID","WorkOrder ID","Patient ID", "Patient Name","P Type", "Insurance Type", "Exam Name","Exam Price", "Exam Date", "Status"
				}
				));

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(130);
		table.getColumnModel().getColumn(2).setMinWidth(130);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setMinWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(160);
		table.getColumnModel().getColumn(5).setMinWidth(160);
		table.getColumnModel().getColumn(6).setPreferredWidth(180);
		table.getColumnModel().getColumn(6).setMinWidth(180);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(120);
		table.getColumnModel().getColumn(9).setMinWidth(120);
		scrollPane.setViewportView(table);

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

				JTable target = (JTable) arg0.getSource();

				if (arg0.getClickCount() == 2) {
					// do some action
					int row = table.getSelectedRow();	
					Object ob= table.getValueAt(row, 1);
					System.out.println(ob.toString()+"   arun");
					if(!ob.toString().equals("") && !ob.toString().equals("...")) 
					{
						LIS_Status_result window = new LIS_Status_result(ob.toString(),LIS_System.this);
						window.setModal(true);
						window.setVisible(true);
					}else {
						JOptionPane
						.showMessageDialog(
								null,
								"WorkOrder ID Cannot be null!",
								"Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
				}	
				if (arg0.getClickCount() == 1) {
					pritnbtn.setEnabled(true);
					btnRebooking.setEnabled(true);
					receipt_id="";
					int row = table.getSelectedRow();	
					Object ob= table.getValueAt(row, 0);
					receipt_id=ob.toString();		
					P_ID="";p_NAME="";r_ID="";DATE="";w_ID="";
					w_ID=table.getModel().getValueAt(row, 1).toString();
					P_ID= table.getModel().getValueAt(row, 2).toString();
					p_NAME=table.getModel().getValueAt(row, 3).toString();
					r_ID=ob.toString();
					DATE=table.getModel().getValueAt(row, 8).toString();


				}

			}
		});
		JButton btnNewButton = new JButton("OPD EXAM");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ExamEntery opdEntery = new ExamEntery();
				opdEntery.setVisible(true);
				btnNewButton_2.doClick();
			}
		});
		btnNewButton.setIcon(new ImageIcon(LIS_System.class.getResource("/icons/emp_rpt.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(951, 566, 117, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("CANCEL");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(null);
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(1099, 566, 139, 35);
		contentPane.add(btnNewButton_1);

		JButton btnIpdBill = new JButton("IPD EXAM");
		btnIpdBill.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				IPDExamEntery opdEntery = new IPDExamEntery(LIS_System.this);
				opdEntery.setVisible(true);
				btnNewButton_2.doClick();
			}
		});
		btnIpdBill.setIcon(new ImageIcon(LIS_System.class.getResource("/icons/emp_rpt.png")));
		btnIpdBill.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnIpdBill.setBounds(822, 566, 117, 35);
		contentPane.add(btnIpdBill);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 1226, 134);
		contentPane.add(panel);
		panel.setLayout(null);
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {
				String t =comboBox.getSelectedItem().toString();
				if(t.equals("ALL TYPE"))
				{
					type="";
				}else {
					type=t;
				}
				populateTable(dateFrom, dateTo,type);
			}
		});
		comboBox.setBounds(218, 85, 178, 27);
		panel.add(comboBox);
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Exam Type");
		lblSelectDisease.setBounds(241, 59, 121, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JDateChooser dateFromDC_1 = new JDateChooser();
		dateFromDC_1.setBounds(408, 85, 178, 27);
		panel.add(dateFromDC_1);
		dateFromDC_1.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							//	populateTable(dateFrom, dateTo,type);
						}
					}
				});
		dateFromDC_1.setDate(new Date());
		dateFromDC_1.setMaxSelectableDate(new Date());
		dateFromDC_1.setDateFormatString("yyyy-MM-dd");
		dateToDC = new JDateChooser();
		dateToDC.setBounds(598, 85, 178, 27);
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
							//populateTable(dateFrom, dateTo,type);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(646, 59, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(455, 59, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JRadioButton rdbtnAll = new JRadioButton("All");
		rdbtnAll.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		rdbtnAll.setBounds(894, 90, 49, 23);
		panel.add(rdbtnAll);
		rdbtnAll.setSelected(true);
		rdbtnAll.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnAll);

		JRadioButton rdbtnMale = new JRadioButton("Male");
		rdbtnMale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rdbtnMale.setBounds(940, 90, 59, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnMale);

		JRadioButton rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		rdbtnFemale.setBounds(1001, 90, 73, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 12));
		agegroup.add(rdbtnFemale);

		JLabel lblSelectSex = new JLabel("Select Sex");
		lblSelectSex.setBounds(940, 69, 73, 14);
		panel.add(lblSelectSex);
		lblSelectSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(1039, 16, 158, 96);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(LIS_System.class.getResource("/icons/graphics-hospitals-221777.gif")));
		panel.add(lblNewLabel);

		searchTF = new JTextField();
		searchTF.setBounds(24, 85, 178, 27);
		panel.add(searchTF);
		searchTF.setColumns(10);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);

			}
		});


		JLabel lblSearchPatient = new JLabel("Search Patient");
		lblSearchPatient.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearchPatient.setBounds(61, 59, 121, 14);
		panel.add(lblSearchPatient);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new LineBorder(UIManager.getColor("Button.select")));
		lblNewLabel_2.setBounds(218, 29, 747, 2);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Integrated System");
		lblNewLabel_3.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblNewLabel_3.setFont(new Font("Dialog", Font.ITALIC, 16));
		lblNewLabel_3.setBounds(40, 16, 183, 31);
		panel.add(lblNewLabel_3);

		btnNewButton_2 = new JButton("Search");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				lblLoading.setVisible(true);
				setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				timer.start();

			}
		});
		btnNewButton_2.setBounds(788, 86, 88, 25);
		panel.add(btnNewButton_2);

		lblLoading = new JLabel("Loading ...");
		lblLoading.setFont(new Font("Dialog", Font.ITALIC, 20));
		lblLoading.setBounds(616, 569, 139, 25);
		contentPane.add(lblLoading);

		pritnbtn = new JButton("PRINT");
		pritnbtn.setEnabled(false);
		pritnbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if(receipt_id.equals("")) {
					JOptionPane
					.showMessageDialog(
							null,
							"WorkOrder ID Cannot be null!",
							"Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String receiptID="",receiptID2="",amountReciptID="",Amount="",time="";
				AmountReceiptDBConnection db=new AmountReceiptDBConnection();
				ResultSet rs=db.retrieveAllRecieptNoNew(receipt_id+"");
				try {
					while(rs.next()){
						receiptID=rs.getObject(1)+""; 
						receiptID2=rs.getObject(2)+""; 
						amountReciptID=rs.getObject(3).toString(); 
						Amount=rs.getObject(4).toString(); 
						time=rs.getObject(5).toString(); 

					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					if(db.isFreeTestReceipt(receipt_id))
						new FreeExamSlippdfRegenerate(receiptID,receiptID2,amountReciptID,Amount,time);
					else	
						new ExamSlippdfRegenerate(receiptID,receiptID2,amountReciptID,Amount,time);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();


			}
		});
		pritnbtn.setBounds(22, 566, 117, 25);
		contentPane.add(pritnbtn);

		btnRebooking = new JButton("REBOOKING ");
		btnRebooking.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				ReBooking(p_NAME,P_ID,DATE,r_ID);

			}
		});
		btnRebooking.setEnabled(false);
		btnRebooking.setBounds(166, 566, 131, 25);
		contentPane.add(btnRebooking);

		examCatCountTF = new JTextField();
		examCatCountTF.setEditable(false);
		examCatCountTF.setBounds(416, 567, 90, 25);
		contentPane.add(examCatCountTF);
		examCatCountTF.setColumns(10);

		JLabel lblCount = new JLabel("COUNT :");
		lblCount.setFont(new Font("Dialog", Font.PLAIN, 13));
		lblCount.setBounds(351, 573, 70, 15);
		contentPane.add(lblCount);
		lblLoading.setVisible(false);

		timer = new Timer(1, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						populateTable(dateFrom, dateTo,type);
					}
				});
				thread.start();
				setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				lblLoading.setVisible(false);
				if (timer.isRunning()) {
					timer.stop();
				}
			}

		});

		ExamCat();
		//		btnNewButton_2.doClick();
	}

	public void ExamCat() {
		modelCB.removeAllElements();
		modelCB.addElement("ALL TYPE");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExamsCat();
		try {
			while (resultSet.next()) {

				modelCB.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(modelCB.getSize()>0) {
			comboBox.setModel(modelCB);
		}
		dbConnection.closeConnection();

	}
	public void ReBooking(String pName,String pId,String date,final String receiptId)
	{
		examlisCode.removeAllElements();
		String telephn="",address="",age="",sex="",city="",referdoctor="";
		ExamDBConnection db1=new ExamDBConnection();
		ResultSet rs=db1.retrieveBookingData(pId, receiptId);

		try {
			while(rs.next())
			{
				telephn=rs.getObject(4).toString(); 
				address=rs.getObject(2).toString();
				age=rs.getObject(1).toString();
				sex=rs.getObject(5).toString();
				city=rs.getObject(3).toString();
				examlisCode.add(rs.getObject(6).toString());
				referdoctor=rs.getObject(8).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db1.closeConnection();
		final String[] book = new String[19];	

		age=age.split("-")[0];

		if(telephn.length()>10)/***If two mobile number or more***/
			telephn=telephn.substring(0,10);
		String[] status=new String[3];
		LIS_Booking  ob=new LIS_Booking();
		book[0] = telephn;
		book[1] = pName;
		book[2] = age;
		book[3] = sex;
		book[4] = address;
		book[5] = city;
		book[6]=""+pId;
		book[7]=""+pId;
		book[8]="";
		book[9]="78";
		book[10]="0";
		book[11]= "0";
		book[12]= "0";
		book[13]= ""+DateFormatChange.StringToMysqlDate(new Date());
		book[14]="cash";
		book[15]=""+receiptId;
		book[16]="cash";
		book[17]="0";
		String[] Doctor = referdoctor.split("\\ ", 2);
		if(Doctor.length<2) /***If doctor is other **/
			book[18] = Doctor[0];
		else 
			book[18] = Doctor[1];

		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				ExamDBConnection db;
				LIS_Booking  ob=new LIS_Booking();
				String[] status=new String[3];
				status=ob.sendPOST(book, examlisCode);
				if (status!=null) {
					if (status[0].equals("Success")) {
						JOptionPane.showMessageDialog(null, "RESPONSE : " + status[0], "LIS SYSTEM Information",
								JOptionPane.INFORMATION_MESSAGE);
						status[0] = "1";
						db = new ExamDBConnection();
						db.UpdateLisData(status,receiptId+"");
						db.closeConnection();
					} 
				}else {
					status=new String[3];
					status[0] = "0";
					status[1] ="PENDING";
					status[2] ="";
					JOptionPane.showMessageDialog(null, "There are some issue in LIS SYSTEM!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					db = new ExamDBConnection();
					db.UpdateLisData(status,receiptId+"");
					db.closeConnection();
				} 

			}
		});
		thread.start();

		btnNewButton_2.doClick();

		searchTF.setText(receiptId+"");
	}
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		if(currtableModel.getRowCount()>0 )
			// To empty the table before search
		{
			currtableModel.setRowCount(0);
			// To search for contents from original table content
			for (Object rows : originalTableModel) {
				Vector rowVector = (Vector) rows;
				for (Object column : rowVector) {
					if (column.toString().toLowerCase()
							.contains(searchString.toLowerCase())) {
						// content found so adding to table
						currtableModel.addRow(rowVector);
						break;
					}
				}
			}
		}
		examCatCountTF.setText(""+table.getRowCount());
	}

	public void populateTable(String dateFrom, String dateTo,String type) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveAllData1(dateFrom, dateTo,type);
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();;

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C)==null?"...":rs.getObject(C);
				}
				R++;
			}
			db.closeConnection();
			DefaultTableModel model=null;
			// Finally load data to the table
			model = new DefaultTableModel(Rows_Object_Array,
					new String[]  {
							"Receipt ID","WorkOrder ID","Patient ID", "Patient Name","P Type", "Insurance Type", "Exam Name","Exam Price", "Exam Date","Status", "HMS Status"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}

			};
			table.setModel(model);
			examCatCountTF.setText(NumberOfRows+"");
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(130);
			table.getColumnModel().getColumn(2).setMinWidth(130);
			table.getColumnModel().getColumn(3).setPreferredWidth(100);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setMinWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(160);
			table.getColumnModel().getColumn(5).setMinWidth(160);
			table.getColumnModel().getColumn(6).setPreferredWidth(180);
			table.getColumnModel().getColumn(6).setMinWidth(180);
			table.getColumnModel().getColumn(7).setPreferredWidth(100);
			table.getColumnModel().getColumn(7).setMinWidth(100);
			table.getColumnModel().getColumn(8).setPreferredWidth(100);
			table.getColumnModel().getColumn(8).setMinWidth(100);
			table.getColumnModel().getColumn(9).setPreferredWidth(120);
			table.getColumnModel().getColumn(9).setMinWidth(120);

			table.setDefaultRenderer(Object.class, new RedYellowRenderer());

			if(NumberOfRows>0)
			{
				get();
			}

		} catch (SQLException ex) {
			Logger.getLogger(LIS_System.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}

	class RedYellowRenderer extends DefaultTableCellRenderer {
		Color Purple = new Color(102,0,153);
		Color c2 = new Color(255,204,30);
		RedYellowRenderer() {
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
			//System.out.print(table.getValueAt(row, 5)+" arun ");
			if(table.getValueAt(row, 9).equals("Booking Verified") && table.getRowCount() > 0) {
				c.setBackground(Purple);
				c.setForeground(Color.white);
			}else if(table.getValueAt(row, 9).equals("Rejected Test") && table.getRowCount() > 0) {
				c.setBackground(Color.PINK);
				c.setForeground(Color.black);
			}else if(table.getValueAt(row, 9).equals("Sample Collected") && table.getRowCount() > 0) {
				c.setBackground(c2);
				c.setForeground(Color.black);

			}else if(table.getValueAt(row, 9).equals("Sample Receive At Lab") && table.getRowCount() > 0) {
				c.setBackground(Color.green);
				c.setForeground(Color.black);
			}else if(table.getValueAt(row, 9).equals("Cancelled") || table.getValueAt(row, 10).equals("Cancel")&& table.getRowCount() > 0) {
				c.setBackground(Color.red);
				c.setForeground(Color.white);
			}else{
				c.setBackground(Color.white);
				c.setForeground(Color.black);
			}

			return c;
		}
	}
	class ImagePanel extends JComponent {
		private Image image;
		public ImagePanel(Image image) {
			this.image = image;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}

	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}
}
