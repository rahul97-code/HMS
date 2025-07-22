package hms.departments.gui;

import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import hms.departments.database.DepartmentDBConnection;
import hms.patient.database.PatientDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.gui.InvoiceBrowser;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JComboBox;

public class bed_details extends JDialog {


	private JTable table;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private int c1=0,c2=0;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	Vector originalTableModel;
	private JComboBox comboBox;
	final DefaultComboBoxModel<String> departmentname = new DefaultComboBoxModel<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					bed_details window = new bed_details("EMERGENCY WARD");
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	public void populateTable(String department_name,String space)
	{
		double amt=0;
		c1=c2=0;
		try {
			 InvoiceDBConnection db=new InvoiceDBConnection();
            ResultSet rs = db.retrieveAllbeddata(department_name,space);
            
           // System.out.println("Table: " + rs.getMetaData().getTableName(1));
            int NumberOfColumns = 0, NumberOfRows = 0;
            NumberOfColumns = rs.getMetaData().getColumnCount();
            
            while(rs.next()){
            NumberOfRows++;
            }
            System.out.println("rows : "+NumberOfRows);
            System.out.println("columns : "+NumberOfColumns);
            rs = db.retrieveAllbeddata(department_name,space);
            
            //to set rows in this array
            Object Rows_Object_Array[][];
            Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
            
            int R = 0;
            while(rs.next()) {
                for(int C=1; C<=NumberOfColumns;C++) {
                    Rows_Object_Array[R][C-1] = rs.getObject(C);
                    
                }
                if(rs.getObject(6).toString().equals("1"))
                {
                	c1++;
                }
                 if(rs.getObject(6).toString().equals("0"))
                {
                	c2++;
                	}
                R++;
            }
            //Finally load data to the table
            DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
            		"WARD_ID", "WARD_NAME", "BED_NO.", "P_ID", "P_NAME", "BED_FILLED", "BUILDING_NAME"
    			}) {
    			@Override
    			public boolean isCellEditable(int row, int column) {
    				return false;// This causes all cells to be not editable
    			}
    		};
    		table.setModel(model);
    		 if(NumberOfRows > 0) {
    			 textField.setText("  "+c1+"");
     	
    			 textField_1.setText("  "+c2 + "");
        }
    		 originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
    					.getDataVector().clone();
		}
		
    		 catch (SQLException ex) {
            Logger.getLogger(InvoiceBrowser.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	public void departmentname() {
		DepartmentDBConnection DBConnection = new DepartmentDBConnection();
		ResultSet resultSet = DBConnection
				.retrieve_department();
		departmentname.removeAllElements();
		try {
			while (resultSet.next()) {
				departmentname.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DBConnection.closeConnection();
		comboBox.setModel(departmentname);
	}
	/**
	 * Create the application.
	 * @param string 
	 */
	

	/**
	 * Initialize the contents of the 
	 */
	public bed_details(String string) {
		final String department_name=string;
		setTitle(department_name);
		setBounds(100, 100, 845, 424);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(173, 12, 650, 364);
		getContentPane().add(scrollPane);
		
		table = new JTable();
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"WARD_ID", "WARD_NAME", "BED_NO.", "P_ID", "P_NAME", "BED_FILLED", "BUILDING_NAME"
				}
			));
		scrollPane.setViewportView(table);
		
		 btnNewButton = new JButton("Filled");
		 btnNewButton.setForeground(Color.BLACK);
		 btnNewButton.setBackground(Color.RED);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String filled="1";
				populateTable(department_name,filled);
				//btnNewButton.setEnabled(false);
				 btnNewButton_1.setBackground(Color.RED);
				 btnNewButton.setBackground(Color.GREEN);
				 
				 textField_1.setBackground(Color.RED);
				 textField.setBackground(Color.GREEN);
				//btnNewButton_1.setEnabled(true);
			}
		});
		btnNewButton.setBounds(21, 127, 115, 25);
		getContentPane().add(btnNewButton);
		
		btnNewButton_1 = new JButton("Blank");
		btnNewButton_1.setForeground(Color.BLACK);
		btnNewButton_1.setBackground(Color.RED);
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String blank="0";
				populateTable(department_name,blank);
				//btnNewButton_1.setEnabled(false);
				 btnNewButton.setBackground(Color.RED);
				 btnNewButton_1.setBackground(Color.GREEN);
				 
				 textField.setBackground(Color.RED);
				 textField_1.setBackground(Color.GREEN);
				//btnNewButton.setEnabled(true);
			}
		});
		btnNewButton_1.setBounds(21, 221, 115, 25);
		getContentPane().add(btnNewButton_1);
		
		textField = new JTextField();
		textField.setBackground(Color.RED);
		textField.setEditable(false);
		textField.setBounds(31, 175, 94, 19);
		getContentPane().add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setBackground(Color.RED);
		textField_1.setEditable(false);
		textField_1.setColumns(10);
		textField_1.setBounds(31, 269, 94, 19);
		getContentPane().add(textField_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(11, 82, 150, 25);
		getContentPane().add(textField_2);
		textField_2.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = textField_2.getText() + "";
						searchTableContents(str);
						
					}

					

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = textField_2.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = textField_2.getText() + "";
						searchTableContents(str);
					}
				});
		textField_2.setColumns(10);
		
		comboBox = new JComboBox();
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String departmentname = comboBox.getSelectedItem().toString();
				populateTable(departmentname,"");
			}
		});
		comboBox.setBounds(11, 31, 150, 24);
		getContentPane().add(comboBox);
		populateTable(department_name,"");
		departmentname();
	}
	protected void searchTableContents(String str) {
		// TODO Auto-generated method stub
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase().contains(str.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	
	}
}
