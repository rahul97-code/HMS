package hms.exams.gui;

import hms.admin.gui.DailyCash;
import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
import hms.opd.gui.OPDBrowser;
import hms.store.gui.IndoorPillsDetail;
import hms.store.gui.TotalStock.CustomRenderer;
import hms.test.database.XrayDataBase;
import hms.test.gui.Test;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

public class TestLabPatientList extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	
	String opd_no, patient_id, patient_name, patient_age, doctor_name,
	amt_received, date, token_no,test_performed,roomNo;
	private JTable table;
	Test test;
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
//				try {
//					TestLabPatientList frame = new TestLabPatientList();
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestLabPatientList(String roomNo,Test test1) {
		setResizable(false);
		this.test=test1;
		this.roomNo=roomNo;
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 542, 589);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 516, 529);
		contentPane.add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"S.N", "Patient ID", "Patient Name", "Doctor", "Performed"
			}
		));
		table.addMouseListener(new MouseAdapter() {
		    public void mousePressed(MouseEvent me) {
		        JTable table =(JTable) me.getSource();
		        Point p = me.getPoint();
		        int row = table.rowAtPoint(p);
		        if (me.getClickCount() == 2) {
		        	  Object ipd_id = table.getModel().getValueAt(row, 0);
		        	  Object p_id = table.getModel().getValueAt(row, 1);
		        	  System.out.println(""+p_id);
		        	  if(p_id!=null)
		        	  {
		        		  	test.searchPatientTB.setText(p_id+"");
		        		  	test.requestFocusInWindow(); 
		        		  	test.toFront();
		        		  	test.repaint();
			        		populateTable( DateFormatChange.StringToMysqlDate(new Date()), DateFormatChange.StringToMysqlDate(new Date()));
		        	  }
		        	 
		        }
		    }
		});
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.getColumnModel().getColumn(3).setMinWidth(120);
		table.getColumnModel().getColumn(4).setPreferredWidth(70);
		table.getColumnModel().getColumn(4).setMinWidth(70);
		table.setFont(new Font("Tahoma", Font.BOLD, 14));
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		scrollPane.setViewportView(table);
		populateTable( DateFormatChange.StringToMysqlDate(new Date()), DateFormatChange.StringToMysqlDate(new Date()));
		
		}
	public void getData()
	{
		populateTable(DateFormatChange.StringToMysqlDate(new Date()), DateFormatChange.StringToMysqlDate(new Date()));
	}

	
	public void populateTable(String dateFrom, String dateTo) {
		try {
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllExamsToken1(dateFrom, dateTo,roomNo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
		
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			
				while (rs.next()) {
						Rows_Object_Array[R][0] = (R + 1) + "";
						Rows_Object_Array[R][1] = rs.getObject(1);
						Rows_Object_Array[R][2] = rs.getObject(2);
						Rows_Object_Array[R][3] = rs.getObject(3);
						Rows_Object_Array[R][4] = rs.getObject(4);
						R++;

				} 
			
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {
					"S.N", "Patient ID", "Patient Name", "Doctor", "Performed"
				}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setMinWidth(120);
			table.getColumnModel().getColumn(3).setMinWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(70);
			table.getColumnModel().getColumn(4).setMinWidth(70);
			table.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
//	public boolean getExamData(String examID) {
//		
//		boolean flag=false;
//		try {
//			ExamDBConnection db = new ExamDBConnection();
//			ResultSet rs = db.retrieveAllDataExamID(examID,roomNo);
//			while (rs.next()) {
//				patient_id = rs.getObject(3).toString();
//				test_performed= rs.getObject(8).toString();
//				flag=true;
//			}
//			
//			db.closeConnection();
//		} catch (SQLException ex) {
//			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
//					null, ex);
//		}
//		return flag;
//	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column)!=null)
	          {
	        	  if(table.getValueAt(row, column).equals("Yes")){
		              cellComponent.setBackground(Color.GREEN);
		          } else{
		        	  cellComponent.setBackground(Color.WHITE);
		        	 
		          }
	          }
	         
	          return cellComponent;
	      }
	  }
}
