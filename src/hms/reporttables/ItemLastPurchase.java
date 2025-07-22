package hms.reporttables;

import hms.main.DateFormatChange;
import hms.patient.slippdf.OpenFile;
import hms.store.database.InvoiceDBConnection;

import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class ItemLastPurchase extends JDialog {

	public JPanel contentPane;
	private JTable jTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	String dateFrom,dateTo;
	private JTextField searchTF;
	Vector originalTableModel;
	public static void main(String[] arg) {
		new ItemLastPurchase("1").setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public ItemLastPurchase(String item_id) {
		setResizable(false);
		setTitle("Last Purcahse");
		setIconImage(Toolkit.getDefaultToolkit().getImage(ItemLastPurchase.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 667, 425);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 50, 643, 339);
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
		searchTF = new JTextField();
		searchTF.setBounds(115, 11, 178, 28);
		contentPane.add(searchTF);
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
		
		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(340, 6, 147, 38);
		contentPane.add(btnExcel);
		
		JLabel lblSelectDisease = new JLabel("Search");
		lblSelectDisease.setBounds(10, 17, 95, 14);
		contentPane.add(lblSelectDisease);
		lblSelectDisease.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("Excel_data.xls"));
				if (fileChooser.showSaveDialog(ItemLastPurchase.this) == JFileChooser.APPROVE_OPTION) {
				  File file = fileChooser.getSelectedFile();
				  // save to file
				  ReportExcel(jTable,file.toPath().toString());
				}
			}
		});
		populateTable1(item_id);
	}
	
	public void populateTable1(String item_id)
	{
		
		try {
			InvoiceDBConnection db = new InvoiceDBConnection();
			
			ResultSet rs = db.lastPurchaseItem(item_id);
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
    	
    		jTable.getColumnModel().getColumn(0).setPreferredWidth(330);
    		jTable.getColumnModel().getColumn(1).setPreferredWidth(100);
    		jTable.getColumnModel().getColumn(2).setPreferredWidth(100);
    		jTable.getColumnModel().getColumn(3).setPreferredWidth(100);

    		
        } catch (SQLException ex) {
            Logger.getLogger(ItemLastPurchase.class.getName()).log(Level.SEVERE, null, ex);
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
