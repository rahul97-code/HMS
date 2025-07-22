package hms.departments.gui;

import hms.exam.database.ExamDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.test.database.TestResultDBConnection;

import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import LIS_System.GetLisReportURL;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class PatientTest extends JPanel {
	private JTable testHistoryTable;
	String mainDir = "";
	public String p_id = "";
	public String date = "0000-00-00";
	private JList list;
	Vector fileList = new Vector();
	String performed = "No";
	SmbFile folder;
	SmbFile[] listOfFiles;
	 static String OS;
	File folder1;
	File[] listOfFiles1;
	private JTextField searchExamTB;
	Component component;
	private JProgressBar progressBar;
	private JTable resultsTable;
	Object results[];
	Object ReportName = "";
	private String testCodeSTR="",workOrderIDSTR="";
	String[] open=new String[4];
	Vector<String> widal = new Vector<String>();
	Vector<String> testCodeV = new Vector<String>();
	Vector<String> workOrderIDV = new Vector<String>(); 
	private JLabel lblResults;
	private JButton btnLisReport;   

	/**
	 * Create the panel.
	 */
	public PatientTest() {
		testCodeV.removeAllElements(); 
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 44, 316, 369);
		add(scrollPane);
		readFile();
		widal.add("O");
		widal.add("H");
		widal.add("A (H)");
		widal.add("B (H)");
		 OS = System.getProperty("os.name").toLowerCase();
		testHistoryTable = new JTable();
		testHistoryTable.getTableHeader().setReorderingAllowed(false);
		testHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		testHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		testHistoryTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(testHistoryTable);
		testHistoryTable.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null },
				{ null, null, null }, { null, null, null }, }, new String[] {
				"Counter", "Exam Date", "Exam Name" }));
		testHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(70);
		testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
		testHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(80);
		testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
		testHistoryTable.getColumnModel().getColumn(2).setPreferredWidth(170);
		testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Files", TitledBorder.LEADING,
				TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel.setBounds(346, 212, 303, 204);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 42, 287, 151);
		panel.add(scrollPane_1);

		list = new JList();
		list.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				try {
					
				
				if (!arg0.getValueIsAdjusting()) {
					 if(fileList.size()<1)
					 {
						 return;
					 }
					if (isWindows()) {
						OPenFileWindows("localTemp/"
								+ list.getSelectedValue().toString() + "");
					} else if (isUnix()) {
						if (list.getSelectedValue().toString().endsWith(".pdf")
								|| list.getSelectedValue().toString()
										.endsWith(".png")
								|| list.getSelectedValue().toString()
										.endsWith(".jpg")
								|| list.getSelectedValue().toString()
										.endsWith(".PNG")
								|| list.getSelectedValue().toString()
										.endsWith(".JPG")
										|| list.getSelectedValue().toString()
										.endsWith(".bmp")
								|| list.getSelectedValue().toString()
										.endsWith(".BMP")) {
							if (System.getProperty("os.version").equals(
									"3.11.0-12-generic")) {
								Run(new String[] {
										"/bin/bash",
										"-c",
										open[0]+" localTemp/"
												+ list.getSelectedValue()
														.toString() });
							} else {
								Run(new String[] {
										"/bin/bash",
										"-c",
										open[1]+" localTemp/"
												+ list.getSelectedValue()
												.toString() });
							}
						} else {
							Run(new String[] {
									"/bin/bash",
									"-c",
									open[2]+" localTemp/"
											+ list.getSelectedValue()
													.toString() });
						}
					}

				}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			
		});
		list.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane_1.setViewportView(list);

		progressBar = new JProgressBar();
		progressBar.setMinimum(0);
		progressBar.setStringPainted(true);
		progressBar.setBounds(7, 14, 286, 24);
		panel.add(progressBar);

		JLabel lblSearch = new JLabel("Search :");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSearch.setBounds(20, 16, 56, 14);
		add(lblSearch);

		searchExamTB = new JTextField();
		searchExamTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchExamTB.setBounds(89, 11, 244, 23);
		add(searchExamTB);
		searchExamTB.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(346, 22, 303, 157);
		add(scrollPane_2);
		
		resultsTable = new JTable();
		resultsTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		resultsTable.getTableHeader().setReorderingAllowed(false);
		resultsTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resultsTable.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null},
			},
			new String[] {
				"*******", "*******", "*******"
			}
		));
		resultsTable.getColumnModel().getColumn(0).setPreferredWidth(90);
		resultsTable.getColumnModel().getColumn(0).setMinWidth(100);
		resultsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		resultsTable.getColumnModel().getColumn(1).setMinWidth(100);
		resultsTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		resultsTable.getColumnModel().getColumn(2).setMinWidth(100);
		scrollPane_2.setViewportView(resultsTable);
		
		lblResults = new JLabel("Results");
		lblResults.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblResults.setBounds(346, 1, 124, 23);
		add(lblResults);
		
		btnLisReport = new JButton("Lis Report");
		btnLisReport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!testCodeSTR.equals("")) {
					GetLisReportURL GetLisReportURL=new GetLisReportURL();
					String url=GetLisReportURL.getURL(workOrderIDSTR, testCodeSTR,false); 
					Desktop desk=Desktop.getDesktop();
					try { 
						desk.browse(new URI(url));
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}else {
					JOptionPane 
					.showMessageDialog(
							null,
							"Report not found!",
							"Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		btnLisReport.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnLisReport.setEnabled(false);
		btnLisReport.setBounds(348, 185, 301, 25);
		add(btnLisReport);
		searchExamTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id,date, str);
				} else {
					populateTestHistoryTable(p_id,date);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id,date, str);
				} else {
					populateTestHistoryTable(p_id,date);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchExamTB.getText() + "";
				if (!str.equals("")) {
					populateTestHistoryTable(p_id,date, str);
				} else {
					populateTestHistoryTable(p_id,date);
				}
			}
		});

		testHistoryTable.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							btnLisReport.setEnabled(true);
							int selectedRowIndex = testHistoryTable
									.getSelectedRow();
							selectedRowIndex = testHistoryTable
									.convertRowIndexToModel(selectedRowIndex);
							Object selectedObject = testHistoryTable.getModel()
									.getValueAt(selectedRowIndex, 0);
							testCodeSTR=testCodeV.get(selectedRowIndex);
							workOrderIDSTR=workOrderIDV.get(selectedRowIndex);
							// System.out.println("Exam id" + selectedObject);
							resultsTable.setModel(new DefaultTableModel(
									new Object[][] {
										{null, null, null},
									},
									new String[] {
										"*******", "*******", "*******"
									}
								));
							resultsTable.getColumnModel().getColumn(0).setMinWidth(90);
							resultsTable.getColumnModel().getColumn(1).setMinWidth(100);
							resultsTable.getColumnModel().getColumn(2).setMinWidth(100);
							fileList.clear();
							list.setListData(fileList);
							progressBar.setValue(0);
							if(getExamData("" + selectedObject))
								
							{
								if(getTestResultCat(selectedObject+"").equals("100"))
								{
									populateTestResults(selectedObject+"");
								}
								else if(getTestResultCat(selectedObject+"").equals("010")){
									populateTestResults2(selectedObject+"");
								}
								else if(getTestResultCat(selectedObject+"").equals("200")){
									populateTestResultsValues(selectedObject+"");
								}
								else if(getTestResultCat(selectedObject+"").equals("300")){
									populateTestResults300(selectedObject+"");
								}
								
							}
						} catch (Exception e2) {
							// TODO: handle exception
						}

					}
				});

	}

	public void populateTestHistoryTable(String patientID,String date1) {
		p_id = patientID;
		date=date1;
		fileList.clear();
		list.setListData(fileList);
		progressBar.setValue(0);
		testCodeV.clear();
		workOrderIDV.clear();
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveTestHistoryDataPatientID2(patientID,date);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveTestHistoryDataPatientID2(patientID,date);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
					
				}
				testCodeV.add(rs.getString(4));
				workOrderIDV.add(rs.getString(5));
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Counter", "Exam Date", "Exam Name" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			testHistoryTable.setModel(model);
			testHistoryTable.getColumnModel().getColumn(0)
					.setPreferredWidth(70);
			testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
			testHistoryTable.getColumnModel().getColumn(1)
					.setPreferredWidth(80);
			testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
			testHistoryTable.getColumnModel().getColumn(2)
					.setPreferredWidth(170);
			testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	public void populateTestHistoryTable(String patientID,String date, String searchText) {
		fileList.clear();
		list.setListData(fileList);
		progressBar.setValue(0);
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.retrieveTestHistoryDataPatientID(patientID,date,
					searchText);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveTestHistoryDataPatientID(patientID,date, searchText);

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
					new String[] { "Counter", "Exam Date", "Exam Name" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			testHistoryTable.setModel(model);
			testHistoryTable.getColumnModel().getColumn(0)
					.setPreferredWidth(70);
			testHistoryTable.getColumnModel().getColumn(0).setMinWidth(70);
			testHistoryTable.getColumnModel().getColumn(1)
					.setPreferredWidth(80);
			testHistoryTable.getColumnModel().getColumn(1).setMinWidth(80);
			testHistoryTable.getColumnModel().getColumn(2)
					.setPreferredWidth(170);
			testHistoryTable.getColumnModel().getColumn(2).setMinWidth(170);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	
	public void populateTestResults(String testCounterNo) {
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID(testCounterNo);
			Object testName[];
			testName = new Object[NumberOfRows];
			
			Object limits[];
			limits = new Object[NumberOfRows];
			
			Object values[];
			values = new Object[NumberOfRows];
			
		
			results = new Object[NumberOfRows];
			
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][3];

			int R = 0;
			while (rs.next()) {
				
					testName[R] = rs.getObject(1);
					limits[R] = rs.getObject(2)+" - "+rs.getObject(3);
					values[R] = rs.getObject(4)+" "+rs.getObject(6);
					results[R] = rs.getObject(5);
				R++;
			}
			for (int i = 0; i < testName.length; i++) {
				
					Rows_Object_Array[i][0] =testName[i];
					Rows_Object_Array[i][1] =limits[i];
					Rows_Object_Array[i][2] =values[i];
				
			}
			resultsTable.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
						"Test Name", "Limits", "Results"
					}
				));
				resultsTable.getColumnModel().getColumn(0).setMinWidth(90);
				resultsTable.getColumnModel().getColumn(1).setMinWidth(100);
				resultsTable.getColumnModel().getColumn(2).setMinWidth(100);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
			 resultsTable.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer());
	}


	public void populateTestResultsValues(String testCounterNo) {
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID(testCounterNo);
			Object testName[];
			testName = new Object[NumberOfRows];
			
			Object limits[];
			limits = new Object[NumberOfRows];
			
			Object values[];
			values = new Object[NumberOfRows];
			
		
			results = new Object[NumberOfRows];
			
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][2];

			int R = 0;
			while (rs.next()) {
				
					testName[R] = rs.getObject(1);
					limits[R] = rs.getObject(2)+" - "+rs.getObject(3);
					values[R] = rs.getObject(4)+" "+rs.getObject(6);
					results[R] = rs.getObject(5);
				R++;
			}
			for (int i = 0; i < testName.length; i++) {
				
					Rows_Object_Array[i][0] =testName[i];
					Rows_Object_Array[i][1] =values[i];
				
			}
			resultsTable.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
						"Test Name", "Results"
					}
				));
				resultsTable.getColumnModel().getColumn(0).setMinWidth(120);
				resultsTable.getColumnModel().getColumn(1).setMinWidth(180);
				//resultsTable.getColumnModel().getColumn(2).setMinWidth(100);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		// resultsTable.getColumnModel().getColumn(2).setCellRenderer(new CustomRenderer());
	}
	public void populateTestResults2(String testCounterNo) {
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID2(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID2(testCounterNo);
			String physical[];
			physical = new String[10];
			
			String chemical[];
			chemical = new String[10];
			
			String meh[];
			meh = new String[10];
			
			String results[];
			results = new String[10];
			for (int i = 0; i < 10; i++) {
				physical[i]=" ";
				chemical[i]=" ";
				meh[i]=" ";
			}
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[10][3];

			int R = 0;
			while (rs.next()) {
				for (int i = 1; i < 11; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						physical[i-1]=(str);

				}

				for (int i = 11; i < 21; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						chemical[i-11]=(str);
				}

				for (int i = 21; i < 31; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						meh[i-21]=(str);
				}
				R++;
			}
			
			for (int i = 0; i < 10; i++) {
				
					Rows_Object_Array[i][0] =physical[i];
					Rows_Object_Array[i][1] =chemical[i];
					Rows_Object_Array[i][2] =meh[i];
				
			}
			resultsTable.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
						"Physical", "Chemical", "M/Exam./HPF"
					}
				));
				resultsTable.getColumnModel().getColumn(0).setMinWidth(130);
				resultsTable.getColumnModel().getColumn(1).setMinWidth(130);
				resultsTable.getColumnModel().getColumn(2).setMinWidth(130);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	public void populateTestResults300(String testCounterNo) {
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveDataWithTestID2(testCounterNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// System.out.println("rows : " + NumberOfRows);
			// System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveDataWithTestID2(testCounterNo);
			String physical[];
			physical = new String[30];
			
			
			for (int i = 0; i < 30; i++) {
				physical[i]=" ";
				
			}
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[4][7];

			int R = 0;
			while (rs.next()) {
				for (int i = 1; i < 31; i++) {
					String str = rs.getObject(i).toString();
					if (!str.equals("0"))
						physical[i-1]=(str);

				}

				R++;
			}
			int k=0;
			for (int i = 0; i < 4; i++) {
				
					Rows_Object_Array[i][0]=""+widal.get(i);
					for (int j = 1; j <=6; j++) {
						Rows_Object_Array[i][j] =physical[k];
						System.out.println(k+"   "+physical[k]);
						k++;
						
					}
				
			}
			resultsTable.setModel(new DefaultTableModel(
					Rows_Object_Array,
					new String[] {
						"WIDAL", "1:20", "1:40", "1:80", "1:160", "1:320", "1:640"
					}
				));
				resultsTable.getColumnModel().getColumn(0).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(1).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(2).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(3).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(4).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(5).setMinWidth(50);
				resultsTable.getColumnModel().getColumn(6).setMinWidth(50);

		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	public String getTestResultCat(String testCounter)
	{
		String str="";
		try {
			TestResultDBConnection db = new TestResultDBConnection();
			ResultSet rs = db.retrieveCategory(testCounter);
			while (rs.next()) {
				str = "" + rs.getObject(1);
			}
			
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		System.out.println("Category "+str);
		return str;
	}

	public String getDirectory(String pid, String exam_id) {

		return mainDir + "/HMS/Patient/" + pid + "/Exam/" + exam_id + "/";
	}
	public String getDirectoryReport(String pid) {

		return mainDir + "/HMS/Patient/" + pid + "/Reports/";
	}
	public void readFile() {
		// The name of the file to open.
		String fileName = "data.mdi";

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			mainDir = data[1];
			open[0]=data[2];
			open[1]=data[3];
			open[2]=data[4];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}

	public void getAllFiles(String path) throws MalformedURLException,
			SmbException {
		String files;
		folder1 = new File(path);
		listOfFiles1 = folder1.listFiles();
		fileList.clear();
		for (int i = 0; i < listOfFiles1.length; i++) {

			if (listOfFiles1[i].isFile()) {
				files = listOfFiles1[i].getName();
				fileList.add(files.toString());
			}
		}
		list.setListData(fileList);
	}

	public void LocalCopy(String path, String index)
			throws MalformedURLException, SmbException {
		String files;
		
		folder = new SmbFile(path);
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
		progressBar.setMaximum(listOfFiles.length);
		progressBar.setValue(0);
		fileList.clear();
		for (int i = 0; i < listOfFiles.length; i++) {

			if (listOfFiles[i].isFile()) {
				files = listOfFiles[i].getName();
				try {

					copyFileUsingJava7Files(getDirectory(p_id, index) + "/"
							+ files, "localTemp/" + files);
					progressBar.setValue(i + 1);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}

	private void copyFileUsingJava7Files(String source, String dest)
			throws IOException {
		new File("localTemp").mkdir();
		
		SmbFile remoteFile = new SmbFile(source);
		OutputStream os = new FileOutputStream(dest);
		InputStream is = null;
		try {
			 is = remoteFile.getInputStream();

		} catch (Exception e) {
			// TODO: handle exception
			return;
		}
	
		int bufferSize = 5096;

		byte[] b = new byte[bufferSize];
		int noOfBytes = 0;
		while ((noOfBytes = is.read(b)) != -1) {
			os.write(b, 0, noOfBytes);
		}
		os.close();
		is.close();
		try {
			getAllFiles("localTemp/");
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean deleteDirectory(File directory) {
		if (directory.exists()) {
			File[] files = directory.listFiles();
			if (null != files) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].isDirectory()) {
						deleteDirectory(files[i]);
					} else {
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	public void getComponent(Component component) {
		this.component = component;
	}
	  class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	          try {
	        	  if(results[row].equals("Out of Range")){
	        		  cellComponent.setBackground(Color.RED);
		        	  cellComponent.setForeground(Color.BLACK);
		          } else{
		        	  cellComponent.setBackground(Color.GREEN);
		        	  cellComponent.setForeground(Color.BLACK);
		          }
			} catch (Exception e) {
				// TODO: handle exception
			}
	         
	          
	          return cellComponent;
	      }
	  }
	  public boolean getExamData(final String index) {

			try {
				ExamDBConnection db = new ExamDBConnection();
				ResultSet rs = db.retrieveAllDataExamID(index);
				while (rs.next()) {
					p_id = "" + rs.getObject(3);
					performed = rs.getObject(8).toString();
					ReportName = rs.getObject(10);
				}
			} catch (SQLException ex) {
				Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
						null, ex);
			}
			if (performed.equals("No")) {
//				JOptionPane.showMessageDialog(null, "Test Results not available",
//						"Patient", JOptionPane.INFORMATION_MESSAGE);
				return false;
			}
			if (new File("localTemp").exists()) {
				deleteDirectory(new File("localTemp"));
			}
			if (ReportName == null) {
				System.out.println("nullllllll  " + ReportName);
			} else {
				System.out.println("name      " + ReportName);
				System.out.println(getDirectoryReport(p_id) + "" + ReportName);
				try {

					copyFileUsingJava7Files(getDirectoryReport(p_id) + ""
							+ ReportName+".pdf", "localTemp/" + ReportName+".pdf");
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			new Thread() {
				@Override
				public void run() {
					try {

						try {
							LocalCopy(getDirectory(p_id, index), index);
						} catch (MalformedURLException | SmbException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}.start();
			return true;
		}


	public void Run(String[] cmd) {
		try {
			Process process = Runtime.getRuntime().exec(cmd);
			int processComplete = process.waitFor();
			if (processComplete == 0) {
				System.out.println("successfully");
			} else {
				System.out.println("Failed");
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void OPenFileWindows(String path) {

		try {

			File f = new File(path);
			if (f.exists()) {
				if (Desktop.isDesktopSupported()) {
					Desktop.getDesktop().open(f);
				} else {
					System.out.println("File does not exists!");
				}
			}
		} catch (Exception ert) {
		}
	}

	public static boolean isWindows() {

		return (OS.indexOf("win") >= 0);

	}

	public static boolean isMac() {

		return (OS.indexOf("mac") >= 0);

	}

	public static boolean isUnix() {

		return (OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS
				.indexOf("aix") > 0);

	}

	public static boolean isSolaris() {

		return (OS.indexOf("sunos") >= 0);

	}
}