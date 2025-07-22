package hms.exams.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.TestLabPatientList.CustomRenderer;

import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.DefaultComboBoxModel;

public class showtable {

	private JFrame frame;
	private JTable table;
	private JComboBox comboBox;
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					showtable window = new showtable();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void getAllExamList() {
		exams.removeAllElements();
		exams.addElement("Select Exam");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveAllExams(4);
		try {
			while (resultSet.next()) {
				exams.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		comboBox.setModel(exams);
		comboBox.setSelectedIndex(0);
	}

	public void populateTable(String examname) {
		try {
			ExamDBConnection db = new ExamDBConnection();
			ResultSet rs = db.getExamSubCat4(4,
					examname);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			System.out.println(NumberOfColumns+"gggg");

			while (rs.next()) {
				NumberOfRows++;
			}
		
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			
				while (rs.next()) {
					
						Rows_Object_Array[R][0] = rs.getObject(1);
						Rows_Object_Array[R][1] = rs.getObject(2);
						Rows_Object_Array[R][2] = rs.getObject(3);
						Rows_Object_Array[R][3] = rs.getObject(4);
						Rows_Object_Array[R][4] = rs.getObject(5);
						Rows_Object_Array[R][5] = rs.getObject(6);
						R++;
					

				} 
			
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {
							"Exam Room", "Exam Sub-Cat", "Exam Name", "Exam Code", "Exam Charge", "LIs Code"
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
	/**
	 * Create the application.
	 */
	public showtable() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 593, 366);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(23, 83, 539, 216);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Exam Room", "Exam Sub-Cat", "Exam Name", "Exam Code", "Exam Charge", "LIs Code"
			}
		));
		
		 comboBox = new JComboBox();
		 comboBox.setModel(new DefaultComboBoxModel(new String[] {"Select Exam"}));
		 comboBox.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		try {
					String exam_name = comboBox.getSelectedItem().toString();
					if (!exam_name.equals("Select Exam")) {

						populateTable(exam_name);

					} else {
						
					}
				} catch (Exception e1) {
					// TODO: handle exception
				}
			}
		});
		comboBox.setBounds(135, 36, 269, 24);
		frame.getContentPane().add(comboBox);
		
		JLabel lblExamCat = new JLabel("Exam Cat :-");
		lblExamCat.setBounds(31, 41, 86, 15);
		frame.getContentPane().add(lblExamCat);
		table.getColumnModel().getColumn(0).setPreferredWidth(88);
		table.getColumnModel().getColumn(1).setPreferredWidth(106);
		table.getColumnModel().getColumn(2).setPreferredWidth(96);
		table.getColumnModel().getColumn(4).setPreferredWidth(111);
		getAllExamList();
	}
}
