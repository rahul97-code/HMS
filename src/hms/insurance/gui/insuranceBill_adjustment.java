package hms.insurance.gui;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.toedter.calendar.JDateChooser;

import LIS_UI.LIS_System;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.IPDExamEntery;
import hms.main.DateFormatChange;
import hms.reception.gui.ReceptionMain;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JTable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;

import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;

import javax.swing.JScrollPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.awt.event.ActionEvent;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.ImageIcon;

public class insuranceBill_adjustment extends JDialog{

	private JTable table;
	private Object[][] ObjectArray_ListOfexamsSpecs;
	Vector<String> ITEM_NAME = new Vector<String>();
	Vector<Boolean> IS_DELETED = new Vector<Boolean>();
	Vector<String> ID = new Vector<String>();
	Vector<String> ITEM_ID = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE = new Vector<Double>();
	Vector<Integer> QTY = new Vector<Integer>();
	Vector<Double> AMOUNT = new Vector<Double>();
	Vector<String> DATE = new Vector<String>();
	Vector<String> EXAMDATE = new Vector<String>();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	Vector<String> TYPE = new Vector<String>();
	TableRowSorter<TableModel> rowSorter;
	TableRowSorter<TableModel> rowSorter1;
	String ipd_id,insurance_type,p_no,p_name;
	boolean isdraft;
	private JTextField searchItemTF;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	String exam_name = "", exam_charge = "",exam_room = "",invoiceDateSTR="",lis_code="",examsub_catname="",exam_nameid = "";
	private JTable addTestTable_2;
	int ExamMaterTableIndex = 0;
	double totalCharges = 0;
	String exam_pagenumber = "0";
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[] ObjectArray_examcategories;
	Object[][] ObjectArray_ListOfexams;
	private JComboBox<String> examNameCB;
	ArrayList paymentCharges = new ArrayList();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examCategory = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examlisCode = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> examPageNumberVector = new Vector<String>();
	Vector<String> examRoomVector = new Vector<String>();
	Vector<Boolean> flag = new Vector<Boolean>();
	final DefaultComboBoxModel examSubCat = new DefaultComboBoxModel();

	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	Vector examID = new Vector();
	Vector lisCodes = new Vector();
	Vector examRoom = new Vector();
	Vector examRate = new Vector();
	private JTable table_1;
	int discount=0;
	int totalExams = 999;
	private JLabel lblTotalcharges;
	private JButton btnRemove;
	private JTextField searchTF1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					insuranceBill_adjustment window = new insuranceBill_adjustment("22775","ECHS","","",null,false);

					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void searchTableContents(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter.setRowFilter(null);
		} else {
			rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
		}
	}
	public void searchTableContents1(String searchString) {
		if (searchString.trim().length() == 0) {
			rowSorter1.setRowFilter(null);
		} else {
			rowSorter1.setRowFilter(RowFilter.regexFilter("(?i)" + searchString));
		}
	}
	public String getInsuranceIndex() {
		ExamMaterTableIndex=0;
		String query = "";
		String price = "";
		String[] values = new String[2];
		values[1] = "4";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(insurance_type);
		ExamMaterTableIndex = Integer.parseInt(values[1]);
		return values[1];
	}
	public void getAllExamList() {
		getInsuranceIndex();
		exams.removeAllElements();
		exams.addElement("Select Exam");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllExams(ExamMaterTableIndex);
		try {
			while (resultSet.next()) {
				exams.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		examNameCB.setModel(exams);
		examNameCB.setSelectedIndex(0);
	}
	private void GetBillDATA() {
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		ID.removeAllElements();
		IS_DELETED.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
		ResultSet rs = db1.retrieveAllInsuranceData(ipd_id,insurance_type);
		try {

			while (rs.next()) {
				TYPE.add(rs.getString(7));
				ID.add(rs.getString(1));
				ITEM_NAME.add(rs.getString(2));
				DATE.add(rs.getString(6));
				ITEM_ID.add(rs.getString(3));
				PER_ITEM_PRICE.add(rs.getDouble(5));
				QTY.add(rs.getInt(4));
				AMOUNT.add(rs.getDouble(8));
				IS_DELETED.add(rs.getBoolean(9));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db1.closeConnection();	
	}
	private void loadDataToTable() {
		// get size of the hashmap
		//		int size = itemsHashMap.size();
		int size = examName.size();
		totalCharges = 0;
		System.out.print("Size"+size);
		// declare two arrays one for key and other for keyValues
		ObjectArray_examnameid = new Object[size];
		ObjectArray_examname = new Object[size];
		ObjectArray_examroom = new Object[size];
		ObjectArray_examcharges = new Object[size];
		ObjectArray_examcategories = new Object[size];
		ObjectArray_ListOfexams = new Object[size][5];

		for (int i = 0; i < examName.size(); i++) {
			ObjectArray_examcharges[i] = examChargesVector.get(i);
			ObjectArray_ListOfexams[i][0] =  examIdVector.get(i);
			ObjectArray_ListOfexams[i][1] = examName.get(i);
			ObjectArray_examname[i] = examName.get(i);
			ObjectArray_examroom[i] = examRoomVector.get(i);
			ObjectArray_examnameid[i] = examIdVector.get(i);
			ObjectArray_examcategories[i] = examCategory.get(i);
			ObjectArray_ListOfexams[i][2] =  examChargesVector.get(i);
			ObjectArray_ListOfexams[i][3] = examlisCode.get(i);
			ObjectArray_ListOfexams[i][4] = EXAMDATE.get(i);
			totalCharges = totalCharges
					+ Double.parseDouble(ObjectArray_examcharges[i].toString()
							.trim());
		}
		table_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Exam Code","Exams", "Charges","LIS Code","Date" }) {

			boolean[] canEdit = new boolean[] { false, false, false,false,false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		lblTotalcharges.setText("`" + totalCharges + "");
	}
	public void populateExamTable(String ExamCat) {

		try {
			ExamDBConnection dbConnection = new ExamDBConnection();
			ResultSet rs = dbConnection
					.GetAllExams(ExamMaterTableIndex,ExamCat,true);

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
			dbConnection.closeConnection();
			addTestTable_2.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
							"Exam Code", "Exam Cat", "Exams", "Price"
					}
					) {@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
						return false;
					}});
			rowSorter1 = new TableRowSorter<>(addTestTable_2.getModel());
			addTestTable_2.setRowSorter(rowSorter1);
		}catch (SQLException ex) {

		}
	}
	public void getDoctorDetail(String name) {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithIndex(name);
		try {
			while (resultSet.next()) {
				//doctor_id = resultSet.getInt(1) + "";
				discount = resultSet.getInt(4);
				totalExams = Integer.parseInt(resultSet.getString(5));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public void getPrice(String exam_code) {

		String price = "";
		exam_pagenumber = "";
		exam_room="";
		lis_code="";
		InsuranceDBConnection insuranceDBConnection = new InsuranceDBConnection();
		String[] values = new String[2];
		values = insuranceDBConnection
				.retrieveDataWithInsuranceType(insurance_type);

		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.getExamSubCatRate(
				ExamMaterTableIndex, exam_code);
		try {
			while (resultSet.next()) {
				price = resultSet.getObject(1).toString();
				exam_pagenumber = resultSet.getObject(2).toString();
				exam_room=resultSet.getObject(3).toString();
				lis_code=resultSet.getObject(4).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		int intPrice = Integer.parseInt(price);
		int intPercentage = (intPrice * Integer.parseInt(values[0])) / 100;
		int finalPrice = intPrice - intPercentage;
		exam_charge = finalPrice + "";
		Calendar mytime = Calendar.getInstance();
		// calendar.setTime(yourdate);
		int hours = mytime.get(Calendar.HOUR_OF_DAY);
		int minutes = mytime.get(Calendar.MINUTE);
		int seconds = mytime.get(Calendar.SECOND);

		System.out.println(hours + "  " + minutes + "  " + seconds);
		if(!(insurance_type.toString().equals("Unknown")))
		{

			if (hours <= 24 && hours >= 21) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
			} else if (hours <= 5 && hours >= 0) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.0) + "";
			}
		}else
		{

			if (hours <= 24 && hours >= 21) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
			} else if (hours <= 5 && hours >= 0) {
				exam_charge = (Double.parseDouble(exam_charge) * 1.5) + "";
			}
		}

		double charges = Double.parseDouble(exam_charge);
		double discountValue = (charges * discount) / 100;
		charges = charges - discountValue;
		exam_charge = "" + (int) charges;
	}
	public void populateExpensesTable() {
		GetBillDATA();
		ObjectArray_ListOfexamsSpecs = new Object[ITEM_ID.size()][10];

		for (int i = 0; i < ITEM_ID.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = ID.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = ITEM_NAME.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = ITEM_ID.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = PER_ITEM_PRICE.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = QTY.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = AMOUNT.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = DATE.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = TYPE.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = IS_DELETED.get(i);
		}
		// Finally load data to the table
		TableModel model = new EditableTableModel_Insurance(
				new String[] { "ID","Name","Item_ID","Price","Qnty", "Amnt","Date","Type","Delete" },ObjectArray_ListOfexamsSpecs) {

		};	
		table.setModel(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(40);
		table.getColumnModel().getColumn(0).setMinWidth(30);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(30);
		table.getColumnModel().getColumn(2).setPreferredWidth(80);
		table.getColumnModel().getColumn(2).setMinWidth(80);
		table.getColumnModel().getColumn(3).setPreferredWidth(60);
		table.getColumnModel().getColumn(3).setMinWidth(60);
		table.getColumnModel().getColumn(4).setPreferredWidth(40);
		table.getColumnModel().getColumn(4).setMinWidth(40);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(100);
		rowSorter = new TableRowSorter<>(table.getModel());
		table.setRowSorter(rowSorter);
	}
	/**
	 * Create the application.
	 */


	/**
	 * Initialize the contents of the 
	 * @param insuranceIPDBill 
	 */
	public insuranceBill_adjustment(final String ipd_id,String insurance_type,final String p_no, final String p_name, final insuranceIPDBill insuranceIPDBill,final boolean isdraft) {
		setTitle("Insurance Adjustment");
		setBounds(150, 80, 750, 720);
		this.ipd_id=ipd_id;
		this.p_no=p_no;
		this.p_name=p_name;
		this.insurance_type=insurance_type;
		this.isdraft=isdraft;

		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setModal(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//				flag.removeAllElements();
				//				if(e.getClickCount()==1) {
				//					for(int i=0;i<table.getRowCount();i++) {
				//						flag.add((Boolean) table.getValueAt(table.getSelectedRow(),8));
				//					}
				//				}
			}
		});
		scrollPane.setBounds(12, 390, 714, 240);
		getContentPane().add(scrollPane);

		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"ID","Name","Item_ID","Price","Qnty", "Amnt","Date","Type"
				}
				));
		populateExpensesTable();


		JLabel lblSearch = new JLabel("Search :-");
		lblSearch.setBounds(154, 356, 72, 22);
		getContentPane().add(lblSearch);

		searchItemTF = new JTextField();
		searchItemTF.setBounds(229, 356, 124, 22);
		getContentPane().add(searchItemTF);
		searchItemTF.setColumns(10);

		textField = new JTextField();
		textField.setEditable(false);
		textField.setText("Date : "+new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
		textField.setBounds(12, 357, 124, 21);
		getContentPane().add(textField);
		textField.setColumns(10);

		JLabel lblNewLabel = new JLabel("Ipd Id :");
		lblNewLabel.setBounds(517, 14, 61, 15);
		getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Insurance Type :");
		lblNewLabel_1.setBounds(448, 41, 130, 15);
		getContentPane().add(lblNewLabel_1);

		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(577, 12, 149, 19);
		getContentPane().add(textField_1);
		textField_1.setColumns(10);
		textField_1.setText(ipd_id);

		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(577, 39, 149, 19);
		getContentPane().add(textField_2);
		textField_2.setColumns(10);
		textField_2.setText(insurance_type);
		JDateChooser invoiceDate = new JDateChooser(new Date());
		invoiceDate.setBounds(413, 200, 165, 22);
		getContentPane().add(invoiceDate);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							invoiceDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());

						}
					}
				});

		JButton btnNewButton = new JButton("CANCEL");
		btnNewButton.setIcon(new ImageIcon(insuranceBill_adjustment.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(608, 642, 117, 36);
		getContentPane().add(btnNewButton);

		final JButton btnNewButton_1 = new JButton("SAVE");
		btnNewButton_1.setIcon(new ImageIcon(insuranceBill_adjustment.class.getResource("/icons/SAVE.PNG")));
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				String[] data = new String[3];
				if(isdraft) {
					try {
						db.DeleteInsurancetracking(ipd_id);
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				for (int i = 0; i < table.getRowCount(); i++) {
					data[0]=table.getValueAt(i, 0).toString();
					data[1]=table.getValueAt(i, 3).toString();
					data[2]=table.getValueAt(i, 8).toString()=="false"?"0":"1";
					try {
						db.UpdateIsDelete(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	
				}
				try {
					db.Updatedraft(ipd_id,false);
					db.closeConnection();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null,
						"Update SuccessFully", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				insuranceIPDBill.searchBT.doClick();
				dispose();
			}
		});
		btnNewButton_1.setBounds(479, 642, 117, 36);
		getContentPane().add(btnNewButton_1);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

				.getBorder("TitledBorder.border"), "Exam Detail :",

				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

						Font.PLAIN, 12), null));
		panel_5.setBounds(12, 12, 352, 50);
		getContentPane().add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);

		examNameCB = new JComboBox<String>();
		examNameCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					exam_name = examNameCB.getSelectedItem().toString();
					if (!exam_name.equals("Select Exam")) {

						populateExamTable(exam_name);

					} else {
						examSubCat.removeAllElements();
						examSubCat.addElement("Select Category");
						populateExamTable("");
					}
				} catch (Exception ex) {
					// TODO: handle exception
				}
			}
		});
		examNameCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		examNameCB.setBounds(6, 16, 334, 25);
		panel_5.add(examNameCB);
		getAllExamList();
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 68, 714, 120);
		getContentPane().add(scrollPane_1);

		addTestTable_2 = new JTable();
		scrollPane_1.setViewportView(addTestTable_2);
		addTestTable_2.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Exam Code", "Exam Cat", "Exams", "Price"
				}
				));
		addTestTable_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount()==2) {
					System.out.println("clicked");
					//				if (patientNameTB.getText().equals("")) {
					//					JOptionPane
					//					.showMessageDialog(
					//							null,
					//							"Please select the patient first and then exam type for set prices",
					//							"Input Error", JOptionPane.ERROR_MESSAGE);
					//					return;
					//				}
					//				if (doctorNameCB.getSelectedIndex() == 0) {
					//					JOptionPane
					//					.showMessageDialog(
					//							null,
					//							"Please select the doctor first and then exam type for set prices",
					//							"Input Error", JOptionPane.ERROR_MESSAGE);
					//				
					//					return;
					//				}
					//				if (doctorNameCB.getSelectedIndex() == 0) {
					//					JOptionPane
					//					.showMessageDialog(
					//							null,
					//							"Please select the doctor first and then exam type for set prices",
					//							"Input Error", JOptionPane.ERROR_MESSAGE);
					//				
					//					return;
					//				}

					int row=addTestTable_2.getSelectedRow();
					exam_nameid = addTestTable_2.getValueAt(row, 0).toString();
					if(invoiceDateSTR.equals(""))
					{
						JOptionPane
						.showMessageDialog(
								null,
								"First, Select Exam Date ",
								"Input Error", JOptionPane.ERROR_MESSAGE);

						return;	
					}
//					if(examIdVector.indexOf(exam_nameid)!=-1 ) {
//						JOptionPane
//						.showMessageDialog(
//								null,
//								"Exam Test Already Exist!",
//								"Input Error", JOptionPane.ERROR_MESSAGE);
//
//						return;
//					}
					//				if(EXAMDATE.contains(invoiceDateSTR))
					//				{
					//					JOptionPane
					//					.showMessageDialog(
					//							null,
					//							" Can`t Add Two or More Exam On Same Date ",
					//							"Input Error", JOptionPane.ERROR_MESSAGE);
					//				
					//					return;	
					//				}

					getPrice("" + exam_nameid);
					exam_name = addTestTable_2.getValueAt(row, 1).toString();
					examsub_catname = addTestTable_2.getValueAt(row, 2).toString();
					examCategory.add(exam_name);
					examName.add(exam_name + " " + examsub_catname);
					examlisCode.add(lis_code);
					examChargesVector.add(exam_charge);
					examIdVector.add(exam_nameid);
					examPageNumberVector.add(exam_pagenumber);
					examRoomVector.add(exam_room);
					itemsHashMap.put(exam_name + " " + examsub_catname,
							exam_charge);
					examHashMap.put(exam_nameid, exam_room);
					EXAMDATE.add(invoiceDateSTR);
					loadDataToTable();
					invoiceDateSTR="";

				}
			}
		});
		btnRemove = new JButton("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table_1.getSelectedRow();
				cur_selectedRow = table_1
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table_1.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				itemsHashMap.remove(toDelete);
				examHashMap.remove(toDelete);
				examName.remove(cur_selectedRow);
				examlisCode.remove(cur_selectedRow);
				examCategory.remove(cur_selectedRow);
				examChargesVector.remove(cur_selectedRow);
				examIdVector.remove(cur_selectedRow);
				examPageNumberVector.remove(cur_selectedRow);
				examRoomVector.remove(cur_selectedRow);
				EXAMDATE.remove(cur_selectedRow);
				invoiceDateSTR="";
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(609, 200, 117, 25);
		getContentPane().add(btnRemove);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(12, 237, 714, 107);
		getContentPane().add(scrollPane_2);

		table_1 = new JTable();
		table_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		scrollPane_2.setViewportView(table_1);
		table_1.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Exam Code", "Exams", "Price","Lis Code","Date"
				}
				));

		JLabel lblSearch_1 = new JLabel("Date :-");
		lblSearch_1.setBounds(361, 200, 61, 22);
		getContentPane().add(lblSearch_1);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setBounds(487, 359, 61, 19);
		getContentPane().add(lblTotalcharges);

		JLabel lblTotalCharges = new JLabel("Total Charges : ");
		lblTotalCharges.setBounds(371, 360, 114, 15);
		getContentPane().add(lblTotalCharges);

		JButton btnSaveExam = new JButton("Save Exam");
		btnSaveExam.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				
				double finalCharges;
				finalCharges=totalCharges;
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat(
						"hh:mm:ss a");
				String[] data2 = new String[16];
				if(isdraft) {
					try {
						db.DeleteInsurancetracking(ipd_id);
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				for (int i = 0; i <  ObjectArray_examname.length; i++) {
					data2[0] = ipd_id;
					data2[1] = "" +  ObjectArray_examname[i];

					data2[2] =  ""+ObjectArray_examname[i];
					data2[3] =""+ ObjectArray_examcharges[i];

					data2[4] = EXAMDATE.get(i);
					data2[5] = "" + timeFormat.format(cal1.getTime());
					data2[6] = (String) ObjectArray_examnameid[i];
					data2[7] = p_no;
					data2[8] = p_name;
					data2[9] = ""+ObjectArray_examcharges[i];
					data2[10] = "1";
					data2[11] = (String) ObjectArray_examcategories[i];
					data2[12] = ""+ReceptionMain.receptionNameSTR;
					data2[13] = !examlisCode.get(i).equals("0")?"LIS EXAM":"EXAM";
					data2[14] = (String) examPageNumberVector.get(i);
					data2[15]="INS";
					try {
						IPDDBConnection ipddbConnection = new IPDDBConnection();
						ipddbConnection.updateAddAmount(ipd_id, finalCharges
								+ "");
						ipddbConnection.closeConnection();	
						// TODO Auto-generated catch block

						db.inserData1(data2);
						
					} catch (Exception ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
						db.closeConnection();
					}
				}
				IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
				try {
					db1.Updatedraft(ipd_id,false);
					db1.closeConnection();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
            	
            	
            	
       
				JOptionPane.showMessageDialog(null,
						"Save Exam SuccessFully", "Message",
						JOptionPane.INFORMATION_MESSAGE);
				populateExpensesTable();
				itemsHashMap.clear();
				examHashMap.clear();
				examName.removeAllElements();
				examlisCode.removeAllElements();
				examCategory.removeAllElements();
				examChargesVector.removeAllElements();
				examIdVector.removeAllElements();
				examPageNumberVector.removeAllElements();
				examRoomVector.removeAllElements();
				EXAMDATE.removeAllElements();
				invoiceDateSTR="";
				loadDataToTable();
				db.closeConnection();
				insuranceIPDBill.searchBT.doClick();
			}
		});
		btnSaveExam.setBounds(575, 355, 116, 25);
		getContentPane().add(btnSaveExam);
		
		searchTF1 = new JTextField();
		searchTF1.setColumns(10);
		searchTF1.setBounds(87, 200, 124, 22);
		getContentPane().add(searchTF1);
		searchTF1.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchTF1.getText() + "";
				searchTableContents1(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchTF1.getText() + "";
				searchTableContents1(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchTF1.getText() + "";
				searchTableContents1(str);
			}
		});

		
		JLabel lblSearch_2 = new JLabel("Search :-");
		lblSearch_2.setBounds(12, 200, 72, 22);
		getContentPane().add(lblSearch_2);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				searchTableContents(str);
			}
		});
		populateExamTable("");
	}
	class EditableTableModel_Insurance extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel_Insurance(String[] columnTitles, Object[][] dataEntries) {
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
			if (column == 8 ) {
				return true;
			} else {
				return false;
			}

		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
}
