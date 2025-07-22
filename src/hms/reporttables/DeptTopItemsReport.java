package hms.reporttables;

import hms.departments.database.DepartmentStockDBConnection;
import hms.departments.database.Dept_PillsRegisterDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
import hms.patient.slippdf.OPDSlippdf_new;
import hms.patient.slippdf.OpenFile;
import hms.reception.gui.ReceptionMain;
import hms.reception.gui.TokenEntry;
import hms.reports.excels.Indoor_OutdoorExcel;
import hms.store.database.ExamBomDBConnection;

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

public class DeptTopItemsReport extends JDialog {

	public JPanel contentPane;
	private JTable jTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private JTextField searchTF;
	Vector originalTableModel;
	public static void main(String[] arg) {
		new DeptTopItemsReport().setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public DeptTopItemsReport() {
		setResizable(false);
		setTitle("Department Top 20 Items Report");
		setIconImage(Toolkit.getDefaultToolkit().getImage(DeptTopItemsReport.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1017, 464);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 803, 418);
		contentPane.add(scrollPane);
		
		jTable = new JTable();
		jTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		jTable.getTableHeader().setReorderingAllowed(false);
		jTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"OPD No.", "Patient ID", "Patient Name", "Doctor Name", "OPD Date", "Token No.", "OPD Type"
			}
		));
		scrollPane.setViewportView(jTable);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
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
		lblSelectDisease.setBounds(52, 16, 95, 14);
		panel.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable1(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		
		
		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 177, 178, 25);
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
							populateTable1(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");
		
		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 158, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(DeptTopItemsReport.this) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  // save to file
				  ReportExcel(jTable,file.toPath().toString());
				}
			}
		});
		btnExcel.setBounds(10, 238, 147, 38);
		panel.add(btnExcel);
		populateTable1(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}
	
	public void populateTable1(String dateFrom,String dateTo)
	{
		
		try {
			Dept_PillsRegisterDBConnection db = new Dept_PillsRegisterDBConnection();
			
			ResultSet rs = db.retrieveTopItems(dateFrom, dateTo);
           
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
            //Finally load data to the table
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,columnNames.toArray()) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		jTable.setModel(model);
    		originalTableModel = (Vector) ((DefaultTableModel) jTable.getModel())
					.getDataVector().clone();
    	
    		jTable.getColumnModel().getColumn(0).setPreferredWidth(120);
    		jTable.getColumnModel().getColumn(1).setPreferredWidth(300);
    		jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    		jTable.getColumnModel().getColumn(3).setPreferredWidth(160);
    		jTable.getColumnModel().getColumn(4).setPreferredWidth(100);
    		
        } catch (SQLException ex) {
            Logger.getLogger(DeptTopItemsReport.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) jTable.getModel();
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
