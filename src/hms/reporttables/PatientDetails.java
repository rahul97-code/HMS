package hms.reporttables;

import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OPDSlippdf;
import hms.patient.slippdf.OPDSlippdf_new;
import hms.patient.slippdf.OpenFile;
import hms.reception.gui.ReceptionMain;
import hms.reception.gui.TokenEntry;
import hms.reports.excels.Indoor_OutdoorExcel;
import hms.store.database.ExamBomDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

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
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;

public class PatientDetails extends JDialog {

	public JPanel contentPane;
	private JTable jTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom,dateTo;
	private JTextField searchTF;
	Vector originalTableModel;
	Vector originalTableModel1;
	Vector originalTableModel2;
	Vector originalTableModel3;
	private JTable common;
	private JTable table_1;
	private JTable table_2;
	private JDateChooser dateFromDC;
	private JDateChooser dateToDC;
	private JLabel ipdCount;
	private JLabel dischargeCount;
	private JLabel AllipdCount;
	public static void main(String[] arg) {
		new PatientDetails().setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public PatientDetails() {
		setResizable(false);
		setTitle("Patient Details");
		setIconImage(Toolkit.getDefaultToolkit().getImage(PatientDetails.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1017, 539);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setFont(new Font("Dialog", Font.ITALIC, 12));
		scrollPane.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "IPD", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		scrollPane.setBounds(214, 12, 394, 225);
		contentPane.add(scrollPane);

		jTable = new JTable();
		jTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.setAutoCreateRowSorter(true);
		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Ward Name", "Room Number", "Patient Count"
				}
				));
		jTable.addMouseListener(new MouseListener() {

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
					originalTableModel=null;
					originalTableModel=originalTableModel1;
					table_1.clearSelection();
					table_2.clearSelection();
					common=jTable;
			}
		});
		jTable.setToolTipText("Double Click to view details");
		scrollPane.setViewportView(jTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 13, 192, 250);
		contentPane.add(panel);
		panel.setLayout(null);
		searchTF = new JTextField();
		searchTF.setBounds(6, 41, 178, 28);
		panel.add(searchTF);
		searchTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}
				});
		searchTF.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSelectDisease = new JLabel("Search");
		lblSelectDisease.setBounds(69, 15, 82, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDateFrom.setBounds(52, 81, 82, 14);
		panel.add(lblDateFrom);

		dateFromDC = new JDateChooser();
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		dateFromDC.setDate(new Date());
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable1(dateFrom,dateTo);
							populateTable2(dateFrom,dateTo);
						}
					}
				});
		dateFromDC.setBounds(6, 101, 178, 25);

		panel.add(dateFromDC);

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDateTo.setBounds(52, 137, 73, 14);
		panel.add(lblDateTo);

		dateToDC = new JDateChooser();
		dateToDC.setDateFormatString("yyyy-MM-dd");
		dateToDC.setDate(new Date());
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable1(dateFrom,dateTo);
							populateTable2(dateFrom,dateTo);

						}
					}
				});

		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setBounds(6, 156, 178, 25);
		panel.add(dateToDC);
		
		JButton btnBedDetails = new JButton("Bed details");
		btnBedDetails.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PatientOnBedReport bedReport = new PatientOnBedReport();
				bedReport.setModal(true);
				bedReport.setVisible(true);
				
			}
		});
		btnBedDetails.setBounds(34, 200, 117, 25);
		panel.add(btnBedDetails);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "All Hospital IPD's", TitledBorder.CENTER, TitledBorder.TOP, null, new Color(51, 51, 51)));
		scrollPane_1.setBounds(12, 265, 995, 196);
		contentPane.add(scrollPane_1);

		table_2 = new JTable();
		table_2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_2.getTableHeader().setReorderingAllowed(false);
		table_2.setAutoCreateRowSorter(true);
		table_2.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				originalTableModel=null;
				originalTableModel=originalTableModel3;
				jTable.clearSelection();
				table_1.clearSelection();
				common=table_2;
			}
		});
		scrollPane_1.setViewportView(table_2);
		

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Discharge", TitledBorder.LEFT, TitledBorder.TOP, null, new Color(51, 51, 51)));
		scrollPane_2.setBounds(614, 11, 391, 225);
		contentPane.add(scrollPane_2);

		table_1 = new JTable();
		table_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				originalTableModel=null;
				originalTableModel=originalTableModel2;
				jTable.clearSelection();
				table_2.clearSelection();
				common=table_1;
			}
		});
		table_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table_1.setAutoCreateRowSorter(true);
		table_1.getTableHeader().setReorderingAllowed(false);
		table_1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		scrollPane_2.setViewportView(table_1);
		
		JLabel lblTotal = new JLabel("Count : ");
		lblTotal.setBounds(224, 238, 70, 25);
		contentPane.add(lblTotal);
		
		JLabel lblTotal_1 = new JLabel("Count : ");
		lblTotal_1.setBounds(624, 238, 70, 25);
		contentPane.add(lblTotal_1);
		
	    ipdCount = new JLabel("");
		ipdCount.setBounds(293, 238, 70, 25);
		contentPane.add(ipdCount);
		
		dischargeCount = new JLabel("");
		dischargeCount.setBounds(684, 238, 70, 25);
		contentPane.add(dischargeCount);
		
		JLabel lblTotal_2 = new JLabel("Count : ");
		lblTotal_2.setBounds(22, 465, 70, 25);
		contentPane.add(lblTotal_2);
		
		AllipdCount = new JLabel("");
		AllipdCount.setBounds(91, 465, 70, 25);
		contentPane.add(AllipdCount);
		
		populateTable1(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
		populateTable2(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
		populateTable3();
	}

	public void populateTable1(String dateFrom,String dateTo)
	{
		ipdCount.setText("");
		try {
			WardsManagementDBConnection db = new WardsManagementDBConnection();

			ResultSet rs = db.retrievePatientIPDData(dateFrom,dateTo);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			Vector<String> columnNames = new Vector<String>();
			for (int i = 0; i < columnsNumber; i++) {
				columnNames.add((rsmd.getColumnName(i + 1)));
			}
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while(rs.next()){
				NumberOfRows++;
			}

			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);
				}
				R++;
			}
			ipdCount.setText(""+R);
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,columnNames.toArray()) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			jTable.setModel(model);
			originalTableModel1 = (Vector) ((DefaultTableModel) jTable.getModel())
					.getDataVector().clone();

			jTable.getColumnModel().getColumn(1).setPreferredWidth(130);
			jTable.getColumnModel().getColumn(2).setPreferredWidth(130);
			jTable.getColumnModel().getColumn(3).setPreferredWidth(100);


		} catch (SQLException ex) {
			Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void populateTable2(String dateFrom,String dateTo)
	{
		dischargeCount.setText("");
		try {
			WardsManagementDBConnection db = new WardsManagementDBConnection();

			ResultSet rs = db.retrievePatientIPDDischargeData(dateFrom,dateTo);

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			Vector<String> columnNames = new Vector<String>();
			for (int i = 0; i < columnsNumber; i++) {
				columnNames.add((rsmd.getColumnName(i + 1)));
			}
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while(rs.next()){
				NumberOfRows++;
			}

			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);
				}
				R++;
			}
			dischargeCount.setText(""+R);
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,columnNames.toArray()) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table_1.setModel(model);
			originalTableModel2 = (Vector) ((DefaultTableModel) table_1.getModel())
					.getDataVector().clone();

			table_1.getColumnModel().getColumn(1).setPreferredWidth(130);
			table_1.getColumnModel().getColumn(2).setPreferredWidth(130);
			table_1.getColumnModel().getColumn(3).setPreferredWidth(100);


		} catch (SQLException ex) {
			Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void populateTable3()
	{
		AllipdCount.setText("");
		try {
			WardsManagementDBConnection db = new WardsManagementDBConnection();

			ResultSet rs = db.retrievePatientAllIPDData();

			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			Vector<String> columnNames = new Vector<String>();
			for (int i = 0; i < columnsNumber; i++) {
				columnNames.add((rsmd.getColumnName(i + 1)));
			}
			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while(rs.next()){
				NumberOfRows++;
			}

			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);
				}
				R++;
			}
			AllipdCount.setText(""+R);
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,columnNames.toArray()) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table_2.setModel(model);
			originalTableModel3= (Vector) ((DefaultTableModel) table_2.getModel())
					.getDataVector().clone();

			table_2.getColumnModel().getColumn(1).setPreferredWidth(130);
			table_2.getColumnModel().getColumn(2).setPreferredWidth(130);
			table_2.getColumnModel().getColumn(3).setPreferredWidth(100);


		} catch (SQLException ex) {
			Logger.getLogger(PatientDetails.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void searchTableContents(String searchString) {
		if(common==null) {
			JOptionPane
			.showMessageDialog(
					null,
					"Please select Atleast one row data !",
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		DefaultTableModel currtableModel = (DefaultTableModel) common.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				String str=column+"";
				if (str.toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}
	public void ReportExcel(JTable table,String path){
		// TODO Auto-generated constructor stub
		try {
			String filename = path;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			HSSFRow row = sheet.createRow(1); 
			TableModel model = table.getModel(); 
			HSSFRow rowhead = sheet.createRow((short) 0);

			HSSFRow headerRow = sheet.createRow(0); //Create row at line 0
			for(int headings = 0; headings < model.getColumnCount(); headings++){ //For each column
				headerRow.createCell(headings).setCellValue(model.getColumnName(headings));//Write column name
			}

			for(int rows = 0; rows < model.getRowCount(); rows++){ //For each table row
				for(int cols = 0; cols < table.getColumnCount(); cols++){ //For each table column
					row.createCell(cols).setCellValue(model.getValueAt(rows, cols).toString()); //Write value
				}

				//Set the row to the next one in the sequence 
				row = sheet.createRow((rows + 2)); 
			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane
			.showMessageDialog(
					null,
					"Excel File Generated Successfully",
					"Data Saved", JOptionPane.INFORMATION_MESSAGE);
			new OpenFile(path);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}
}
