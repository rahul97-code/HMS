package LIS_UI;

import hms.admin.gui.AdminMain;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.cancellation.gui.CancelledDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.slippdf.ExamSlippdf;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;

import LIS_System.LISBDConnection;
import LIS_System.LIS_Cancel;
import LIS_System.LIS_StatusChecking;
import LIS_System_Pdf_Slips.CancelExamSlipPdf;

import javax.swing.border.LineBorder;
import java.awt.Color;

public class LIS_Exam_Cancellation extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchReceiptID;
	private JTextField patientNameTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0;
	int examStartNo, examEndNo;
	double amount;
	String date, reciept_no;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String exam_doctorname = "", exam_date = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel miscCat = new DefaultComboBoxModel();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> lisCodes = new Vector<String>();
	Vector<Boolean> Selected = new Vector<Boolean>();
	Vector<String> CancellisCodes = new Vector<String>();
	Vector<String> examnameID = new Vector<String>();
	Vector<String> CancellisName = new Vector<String>();
	Vector<String> CancellisCharges = new Vector<String>();
	Vector<String> CancelWorkOrederID = new Vector<String>();
	Vector<String> CancelexamID = new Vector<String>();
	Vector<String> examNameRm = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> WorkOrderID = new Vector<String>();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	MiscDBConnection miscdbConnection;
	private JTable table;
	private JLabel lblTotalcharges;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JLabel lblTotalAmount;
	private JLabel lblExamType;
	private JLabel lblDate;
	private JLabel lblCancelcharges;
	private JTextArea textArea;
	private JButton btnNewButton;
	private String ipd_id="";
	public String receipt_id,Type;
	private JLabel lblReceiptType;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new LIS_Exam_Cancellation().setVisible(true);
	}

	public LIS_Exam_Cancellation() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				LIS_Exam_Cancellation.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Cancel Exam Slip");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(300, 70, 541, 593);
		setModal(true);
		try {
			// create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT,
					new File("indian.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
					"indian.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setFont(new Font("Dialog", Font.ITALIC, 11));
		panel_4.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Cancel Exam Slip", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(64, 64, 64)));
		panel_4.setBounds(6, 0, 523, 544);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblSearchPatient = new JLabel("Search Reciept :");
		lblSearchPatient.setBounds(10, 23, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchReceiptID = new JTextField();
		searchReceiptID.setToolTipText("Search Patient");
		searchReceiptID.setBounds(123, 18, 182, 25);
		panel_4.add(searchReceiptID);
		searchReceiptID.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchReceiptID.setColumns(10);
		searchReceiptID.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE))) {
					e.consume();
				}
			}
		});

		searchReceiptID.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchReceiptID.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {

							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientNameTB.setText("");

							patientID.removeAllElements();

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							lisCodes.clear();

							examChargesVector.clear();
							examIdVector.clear();
							loadDataToTable();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchReceiptID.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {
							patientNameTB.setText("");
							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientID.removeAllElements();

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();

							lisCodes.clear();
							examChargesVector.clear();
							examIdVector.clear();

							loadDataToTable();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchReceiptID.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {
							patientNameTB.setText("");
							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientID.removeAllElements();
							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							lisCodes.clear();
							examChargesVector.clear();
							examIdVector.clear();
							loadDataToTable();
						}
					}
				});
		exam_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 54, 504, 470);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(15, 343, 106, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 134, 482, 172);
		panel_2.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Description", "Charges","select"}));

		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action
					//	DoSelect(row);
					calculate();

				}
			}

			public void DoSelect(int row) {

				String WrkOrdrIDString=WorkOrderID.get(row);
				System.out.println(WrkOrdrIDString+"-------111");
				boolean b = Boolean.valueOf(table.getValueAt(row, 2).toString());
				for (int i = 0; i < table.getRowCount(); i++) {		
					if(WrkOrdrIDString.equals(WorkOrderID.get(i))) {
						Selected.set(i, b);
					}



				}

				loadDataToTable();

			}


		});

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(133, 343, 94, 20);
		panel_2.add(lblTotalcharges);

		lblExamType = new JLabel("Exam Type");
		lblExamType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExamType.setBounds(21, 47, 303, 20);
		panel_2.add(lblExamType);

		lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTotalAmount.setBounds(20, 72, 304, 20);
		panel_2.add(lblTotalAmount);

		lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDate.setBounds(20, 99, 250, 23);
		panel_2.add(lblDate);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(21, 16, 108, 14);
		panel_2.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setBounds(121, 11, 201, 25);
		panel_2.add(patientNameTB);
		patientNameTB.setEditable(false);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblCancelCharges = new JLabel("Cancel Charges :");
		lblCancelCharges.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblCancelCharges.setBounds(15, 369, 106, 23);
		panel_2.add(lblCancelCharges);

		lblCancelcharges = new JLabel("");
		lblCancelcharges.setFont(new Font("Dialog", Font.PLAIN, 14));
		lblCancelcharges.setBounds(133, 369, 94, 20);
		panel_2.add(lblCancelcharges);

		JPanel panel = new JPanel();
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Comments",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(245, 328, 250, 73);
		panel_2.add(panel);

		textArea = new JTextArea();
		textArea.setRows(5);
		textArea.setLineWrap(true);
		textArea.setFont(new Font("Dialog", Font.PLAIN, 13));
		textArea.setBounds(10, 16, 228, 46);
		panel.add(textArea);


		btnNewButton = new JButton("Cancel And Generate Reciept");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				ExamDBConnection db;
				IPDDBConnection ipddbConnection;
				IPDExpensesDBConnection db1;
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select Slip",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(CancellisName.size()<=0) {
					JOptionPane.showMessageDialog(null, "Please select exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(Type.equals("IPD")) {
					if(ipd_id.equals("")) {
						JOptionPane.showMessageDialog(null, "patient is not Indoor Patient",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
					}
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							LIS_Exam_Cancellation.this,
							"Are you sure to cancel this slip", "Cancellation",
							dialogButton);
					if (dialogResult == 0) {
						InsertIPDData();
					}
				}else if(Type.equals("OPD")) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							LIS_Exam_Cancellation.this,
							"Are you sure to cancel this slip", "Cancellation",
							dialogButton);
					if (dialogResult == 0) {
						InsertOPDData();
					}
				}
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.ITALIC, 13));
		btnNewButton.setBounds(234, 413, 258, 44);
		panel_2.add(btnNewButton);

		lblReceiptType = new JLabel("Bill Type  :  ");
		lblReceiptType.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 14));
		lblReceiptType.setBounds(323, 20, 189, 20);
		panel_4.add(lblReceiptType);

		KeyboardFocusManager.getCurrentKeyboardFocusManager()
		.addPropertyChangeListener("permanentFocusOwner",
				new PropertyChangeListener() {
			public void propertyChange(
					final PropertyChangeEvent e) {
				if (e.getNewValue() instanceof JTextField) {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							JTextField textField = (JTextField) e
									.getNewValue();
							textField.selectAll();
						}
					});

				}
			}
		});
		searchReceiptID.requestFocus(true);
	}
	public void InsertIPDData(){
		ExamDBConnection ExamDBConnection;
		IPDDBConnection ipddbConnection;
		IPDExpensesDBConnection IPDExpensesDBConnection;
		LIS_Cancel lisObj=new LIS_Cancel();

		for(int j=0;j<CancellisName.size();j++) { 
			try {
				if(CancelWorkOrederID.get(j).toLowerCase().contains("rgh"))
				{
					if(lisObj.cancelExam(CancelWorkOrederID.get(j), CancellisCodes.get(j))) {
					ExamDBConnection=new ExamDBConnection();
					ExamDBConnection.updatelisCancel(CancellisCodes.get(j),CancelWorkOrederID.get(j));
					ExamDBConnection.closeConnection();
					}else {
						continue;
					}
				}
					ExamDBConnection=new ExamDBConnection();
					ExamDBConnection.updateExamCancel(reciept_no,CancelexamID.get(j));
					ExamDBConnection.closeConnection();

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					String[] data1 = new String[15];
					data1[0] = "" + ipd_id;
					data1[1] = "" + CancellisName.get(j);
					data1[2] = CancellisName.get(j);
					data1[3] = "-" + CancellisCharges.get(j);
					data1[4] = "" + DateFormatChange.StringToMysqlDate(new Date());
					data1[5] = "" + timeFormat.format(cal1.getTime());
					data1[6] = CancelexamID.get(j);
					data1[7] = "" + p_id;
					data1[8] = "" + p_name;
					data1[9] = CancellisCharges.get(j);
					data1[10] = "-1";
					data1[11] = CancellisName.get(j) + " CANCELLED";
					data1[12] = AdminMain.username;
					data1[13] = "Exam";
					data1[14]="NA";

					IPDExpensesDBConnection=new IPDExpensesDBConnection();
					IPDExpensesDBConnection.inserData(data1);
					IPDExpensesDBConnection.closeConnection();

					ipddbConnection=new IPDDBConnection();
					ipddbConnection.updateAddAmount(ipd_id, "-"
							+ CancellisCharges.get(j).toString());
					ipddbConnection.closeConnection();

			} catch (Exception e) { 
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		JOptionPane.showMessageDialog(null,
				"Test Cancelled Successfully ", "Data Cancelled",
				JOptionPane.INFORMATION_MESSAGE);
		dispose();
	}		
	public void InsertOPDData(){
		ExamDBConnection ExamDBConnection;
		IPDDBConnection ipddbConnection;
		IPDExpensesDBConnection IPDExpensesDBConnection;
		LIS_Cancel lisObj=new LIS_Cancel();
		for(int j=0;j<CancellisName.size();j++) {
			try {
				if(CancelWorkOrederID.get(j).toLowerCase().contains("rgh"))
				{
					if(lisObj.cancelExam(CancelWorkOrederID.get(j), CancellisCodes.get(j))) {
					ExamDBConnection=new ExamDBConnection();
					ExamDBConnection.updatelisCancel(CancellisCodes.get(j),CancelWorkOrederID.get(j));
					ExamDBConnection.closeConnection();}
//					else {
//						continue;
//					}
				}

					ExamDBConnection=new ExamDBConnection();
					ExamDBConnection.updateExamCancel(reciept_no,CancelexamID.get(j));
					ExamDBConnection.closeConnection();
			}catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
			try {
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
					CancelledDBConnection cancelledDBConnection = new CancelledDBConnection();
					double finalAmount = amount - totalCharges;
					data[0] = "Exam";
					data[1] = p_id;
					data[2] = p_name;
					data[3] = reciept_no;
					data[4] = lblCancelcharges.getText()+"";
					data[5] = "Admin";
					data[6] = date;
					new DateFormatChange();
					data[7] = DateFormatChange.StringToMysqlDate(new Date());
					data[8] = "" + timeFormat.format(cal1.getTime());
					data[9] = textArea.getText();
					cancelledDBConnection.inserData(data);
					cancelledDBConnection.closeConnection();
					

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		try {
			new CancelExamSlipPdf(examStartNo + "", CancellisName,
					CancellisCharges, lblCancelcharges.getText()+"",
					Integer.parseInt(reciept_no),false);
		} catch (NumberFormatException | DocumentException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dispose();

	}
	protected void calculate() {
		// TODO Auto-generated method stub
		CancellisCodes.removeAllElements();
		CancellisName.removeAllElements();
		CancellisCharges.removeAllElements();
		CancelWorkOrederID.removeAllElements();
		CancelexamID.removeAllElements();
		double amount1 = 0;

		for (int count = 0; count < table.getRowCount(); count++) {
			Boolean b = Boolean.valueOf(table.getValueAt(count, 2).toString());
			if (b) {
				amount1+=Double.parseDouble((""+examChargesVector.get(count)));
				CancellisCodes.add(""+lisCodes.get(count));
				CancellisName.add(""+examName.get(count));
				CancellisCharges.add(""+examChargesVector.get(count));
				CancelWorkOrederID.add(""+WorkOrderID.get(count) );
				CancelexamID.add(""+examnameID.get(count));
			}
		}
		lblCancelcharges.setText(amount1+"");
	}

	public void getPatientsID(String index) {
		receipt_id=index;
		exam(index);
		getAllExamList(index);

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = examChargesVector.size();
		totalCharges = 0;
		// declare two arrays one for key and other for keyValues

		ObjectArray_ListOfexams = new Object[size][3];

		for (int i = 0; i < examName.size(); i++) {

			totalCharges = totalCharges
					+ Double.parseDouble(examChargesVector.get(i).toString()
							.trim());
			ObjectArray_ListOfexams[i][0] = examName.get(i);
			ObjectArray_ListOfexams[i][1] = examChargesVector.get(i);	
			ObjectArray_ListOfexams[i][2] = new Boolean(false);

		}
		table.setModel(new EditableTableModel1(
				new String[] { "Description", "Charges","select"},ObjectArray_ListOfexams) {
		});
		lblTotalcharges.setText("`" + totalCharges + "");
		lblTotalcharges.setFont(customFont);
	}


	public void exam(String examID) {
		p_id = "";
		examStartNo=0;
		examEndNo=0;
		reciept_no="";
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllExamsReciept(examID);
			while (rs.next()) {

				lblExamType.setText("Exam Category : "
						+ rs.getObject(1).toString());
				p_name = rs.getObject(2).toString();
				amount = Double.parseDouble(rs.getObject(3).toString());
				date = rs.getObject(4).toString();
				lblTotalAmount.setText("Amount : " + amount);
				lblDate.setText("Exam Date : " + date);
				examStartNo = Integer.parseInt(rs.getObject(5).toString());
				examEndNo = Integer.parseInt(rs.getObject(6).toString());
				reciept_no = rs.getObject(8).toString();
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		patientNameTB.setText(p_name);
		itemsHashMap.clear();
		examHashMap.clear();
		examName.clear();
		examChargesVector.clear();
		examIdVector.clear();



	}

	public void getAllExamList(String reciept_no ) {
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.lis_retrieveExamDataWithCancel(reciept_no);
		lisCodes.removeAllElements();
		examIdVector.removeAllElements();
		examChargesVector.removeAllElements();
		examName.removeAllElements();
		WorkOrderID.removeAllElements();
		Selected.removeAllElements();
		examnameID.removeAllElements();
		ipd_id="";
		try {
			while (resultSet.next()) {
				examName.add(resultSet.getObject(1).toString());
				examChargesVector.add(resultSet.getObject(2).toString());
				examIdVector.add(resultSet.getObject(3).toString());
				examnameID.add(resultSet.getObject(7).toString());
				p_id = resultSet.getObject(4).toString();
				lisCodes.add(resultSet.getObject(5)+"");
				WorkOrderID.add(resultSet.getObject(6)+"");
				Type=resultSet.getObject(8)==null?"OPD":"IPD";
				//Selected.add(false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		lblReceiptType.setText("Bill Type  :  "+Type);
		dbConnection.closeConnection();
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet1 = db.retrieveAllDataPatientID(p_id);
		try {
			while(resultSet1.next()) {
				ipd_id=resultSet1.getObject(2).toString();
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

		loadDataToTable();
	}


	public void searchAutoFill(String p_id)
	{
		searchReceiptID.setText(p_id);
	}
	class EditableTableModel1 extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel1(String[] columnTitles, Object[][] dataEntries) {
			this.columnTitles = columnTitles;
			this.dataEntries = dataEntries;
		}

		public int getRowCount() {
			return dataEntries.length;
		}

		public int getColumnCount() {
			return columnTitles.length;
		}

		public Object getValueAt(int row, int column) {
			return dataEntries[row][column];
		}

		public String getColumnName(int column) {
			return columnTitles[column];
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(int row, int column) {

			return true;	

		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
}