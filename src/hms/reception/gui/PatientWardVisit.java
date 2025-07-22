package hms.reception.gui;

import hms.admin.gui.AdminMain;
import hms.admin.gui.DailyCash;
import hms.doctor.database.DoctorDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JComboBox;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class PatientWardVisit  extends JFrame {
	private JTable table;
	private JDateChooser dateFromDC;
	private JComboBox departmentNameCB;
	String departmentNameSTR, fromDate="",toDate="";
	
	final DefaultComboBoxModel department = new DefaultComboBoxModel();
	ArrayList<String> entry_data = new ArrayList<String>();
	private JLabel lblDateTo;
	private JDateChooser dateToDC;
	private JButton btnLoadData;
	private JButton btnCancel;
	private JButton btnExportData;
	public static void main(String[] arg) {
		new PatientWardVisit().setVisible(true);
	}
	
	public PatientWardVisit() {
		setResizable(false);
		setTitle("Patient Ward Visit");
		getContentPane().setLayout(null);
		setBounds(10,10,850,483);
		JLabel label = new JLabel("Name :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 16));
		label.setBounds(20, 24, 141, 24);
		getContentPane().add(label);
		
		departmentNameCB = new JComboBox(new Object[]{});
		departmentNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					departmentNameSTR = departmentNameCB.getSelectedItem().toString();
				
				} catch (Exception e) {
					// TODO: handle exception

				}
			}
		});
		
		departmentNameCB.setBounds(191, 26, 146, 24);
		getContentPane().add(departmentNameCB);
		
		JLabel lblDate = new JLabel("Date From :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDate.setBounds(20, 70, 141, 24);
		getContentPane().add(lblDate);
		
		dateFromDC = new JDateChooser();
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							fromDate = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		dateFromDC.setDateFormatString("yyyy-MM-dd");
		dateFromDC.setBounds(191, 70, 146, 25);
		getContentPane().add(dateFromDC);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 164, 814, 270);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
					"Patient ID", "Patient Name","Gender", "Address","City", "Mobile","Ward Name","Date"
			}
		));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						
					}
				}); 
		scrollPane.setViewportView(table);
		
		lblDateTo = new JLabel("Date To :");
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblDateTo.setBounds(352, 69, 89, 24);
		getContentPane().add(lblDateTo);
		
		dateToDC = new JDateChooser();
		dateToDC.setDateFormatString("yyyy-MM-dd");
		dateToDC.setBounds(451, 70, 146, 25);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							toDate = DateFormatChange.StringToMysqlDate((Date) arg0
									.getNewValue());
						}
					}
				});
		getContentPane().add(dateToDC);
		
		btnLoadData = new JButton("Load Data");
		btnLoadData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				LoadTable();
			}
		});
		btnLoadData.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnLoadData.setBounds(88, 113, 186, 40);
		getContentPane().add(btnLoadData);
		
		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(482, 113, 195, 40);
		getContentPane().add(btnCancel);
		
		btnExportData = new JButton("Export Data");
		btnExportData.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					File desktop = new File(System.getProperty("user.home"), "Desktop");
					String filename = desktop + "/" + "Patient Ward Data-"
							+ DateFormatChange.StringToMysqlDate(new Date()) + ".xls";
					HSSFWorkbook workbook = new HSSFWorkbook();
					HSSFSheet sheet = workbook.createSheet("Item Rate List");
					create( sheet,table);

					FileOutputStream fileOut = new FileOutputStream(filename);
					workbook.write(fileOut);
					fileOut.close();
					JOptionPane
							.showMessageDialog(
									null,
									"Excel File Generated Successfully on your Desktop with Patient Ward Data-"
											+ DateFormatChange.StringToMysqlDate(new Date())+" Name",
									"Data Saved", JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception ex) {
					System.out.println(ex);
				}
			}
		});
		btnExportData.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnExportData.setBounds(280, 113, 195, 40);
		getContentPane().add(btnExportData);
		
		 getAllDoctors();
	}
	
	
	public void getAllDoctors() {
		WardsManagementDBConnection dbConnection = new WardsManagementDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllWards();
	
		try {
			while (resultSet.next()) {
				department.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentNameCB.setModel(department);
		departmentNameCB.setSelectedIndex(0);
	}
	
	
	
	public void LoadTable()
	{
		try {

			IPDDBConnection connection=new IPDDBConnection();
			
			ResultSet rs=connection.retrievePatientWardData(departmentNameSTR,fromDate,toDate);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
			rs=connection.retrievePatientWardData(departmentNameSTR,fromDate,toDate);
			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					  new String[] {
								"Patient ID", "Patient Name","Gender", "Address","City", "Mobile","Ward Name","Date"
						}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			
			connection.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
	}
	
	


	public void create( HSSFSheet sheet,JTable Table) {
		 DefaultTableModel model1  = (DefaultTableModel) Table.getModel();
		int k = 1;
		try {
			
			HSSFRow rowhead = sheet.createRow((short) 0);
			for (int i = 0; i <  model1.getColumnCount(); i++) {
				rowhead.createCell(i).setCellValue(model1.getColumnName(i).toString());
			}
	        for (int count = 0; count < model1.getRowCount(); count++){
	             
	            	HSSFRow rowhead1 = sheet.createRow((short) k);
	  				for (int i = 0; i <  model1.getColumnCount(); i++) {
	  					rowhead1.createCell(i).setCellValue(model1.getValueAt(count,i).toString());
	  				}
	  				k++;    
	        }
		} catch (Exception ex) {
			System.out.print(""+ex);
		}
	}
}
