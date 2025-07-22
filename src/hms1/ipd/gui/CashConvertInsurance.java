package hms1.ipd.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import hms.exam.database.TestMasterDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.insurance.gui.InsuranceDBConnection;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.CashConvertInsuranceDBConnection;
import hms1.ipd.database.IPDDBConnection;

import javax.swing.JScrollPane;
import javax.swing.JComboBox;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class CashConvertInsurance {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTable table;
	private JTable table_1;
	private JTable table_2;
	final DefaultComboBoxModel GetInsModel = new DefaultComboBoxModel();
	private JComboBox comboBox;
	protected String ExamTable;
	 Map<String, List<String>> examCodeMap = new HashMap<>();
	 Stack<List<String>> rowStack = new Stack<>();
	protected String InsName,consultant="",insurance_category="",ipd_id,p_insurancetype,p_id,p_name;
	private JTextField textField_4;
	double REBATE = 0;
	private boolean tableUpdateInProgress = false;
	Vector<String> ITEM_NAME = new Vector<String>();
	Vector<String> ITEM_DESC = new Vector<String>();
	Vector<String> DATE = new Vector<String>();
	Vector<String> TYPE = new Vector<String>();
	Vector<String> ITEM_ID = new Vector<String>();
	Vector<String> PAGE_NO = new Vector<String>();
	Vector<String> MRP = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE = new Vector<Double>();
	Vector<String> QTY = new Vector<String>();
	Vector<Double> AMOUNT = new Vector<Double>();
	Vector<String> BATCH = new Vector<String>();
	
	Vector<String> EXPIRY = new Vector<String>();

	Vector<Integer> PACKAGE_DAYS = new Vector<Integer>();
	Vector<String> PACKAGE_DATES = new Vector<String>();
	
	
	Vector<String> EXAMCODEINS = new Vector<>();
	Vector<String> EXAMNAMEINS = new Vector<>();
	Vector<String> EXAMDESCINS = new Vector<>();
	Vector<String> EXAMRATEINS = new Vector<>();
	
	
	
	Vector<String> ITEM_NAME_NEW = new Vector<String>();
	Vector<String> ITEM_DESC_NEW = new Vector<String>();
	Vector<String> DATE_NEW = new Vector<String>();
	Vector<String> TYPE_NEW = new Vector<String>();
	Vector<String> ITEM_ID_NEW = new Vector<String>();
	Vector<String> PAGE_NO_NEW = new Vector<String>();
	Vector<String> MRP_NEW = new Vector<String>();
	Vector<Double> PER_ITEM_PRICE_NEW = new Vector<Double>();
	Vector<String> QTY_NEW = new Vector<String>();
	Vector<Double> AMOUNT_NEW = new Vector<Double>();
	Vector<String> BATCH_NEW = new Vector<String>();
	Vector<String> EXPENSE_ID_NEW = new Vector<String>();
	Vector<String> EXPIRY_NEW = new Vector<String>();
	
	Vector<Integer> PACKAGE_DAYS_NEW = new Vector<Integer>();
	Vector<String> PACKAGE_DATES_NEW = new Vector<String>();
	Vector<String> PACKAGE_AMOUNT_REMOVE_DATES_NEW = new Vector<String>();
	private JTextField textField_5;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CashConvertInsurance window = new CashConvertInsurance();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CashConvertInsurance() {
		initialize();
		
		getInsurance("75329","echs");
		
		
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1306, 696);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Patient Detail", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel.setBounds(20, 28, 556, 131);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Patient_Name : ");
		lblNewLabel.setBounds(10, 22, 80, 14);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(105, 19, 96, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Patient ID : ");
		lblNewLabel_1.setBounds(10, 64, 61, 14);
		panel.add(lblNewLabel_1);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		textField_1.setBounds(105, 61, 96, 20);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblNewLabel_2 = new JLabel("IPD ID : ");
		lblNewLabel_2.setBounds(341, 22, 49, 14);
		panel.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("Insurance : ");
		lblNewLabel_3.setBounds(341, 64, 59, 14);
		panel.add(lblNewLabel_3);
		
		textField_2 = new JTextField();
		textField_2.setEditable(false);
		textField_2.setBounds(420, 19, 96, 20);
		panel.add(textField_2);
		textField_2.setColumns(10);
		
		textField_3 = new JTextField();
		textField_3.setEditable(false);
		textField_3.setBounds(420, 61, 96, 20);
		panel.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblNewLabel_6 = new JLabel("Category : ");
		lblNewLabel_6.setBounds(10, 106, 61, 14);
		panel.add(lblNewLabel_6);
		
		textField_5 = new JTextField();
		textField_5.setEditable(false);
		textField_5.setBounds(104, 103, 97, 20);
		panel.add(textField_5);
		textField_5.setColumns(10);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Old Exam", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel_1.setBounds(20, 170, 569, 460);
		frame.getContentPane().add(panel_1);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 549, 419);
		panel_1.add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setModel(new DefaultTableModel(
		    new Object[][] {
		    }, 
		    new String[] {
		        "Exam_Code", "Exam_Name", "Exam_Desc", "Date", "Type", "Rate_Per_Exam", "Exam_Qty", "Exam_Amount", "Select"  // Corrected
		    }
		));
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(null, "Insurance Exam", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel_2.setBounds(710, 62, 563, 285);
		frame.getContentPane().add(panel_2);
		panel_2.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(10, 22, 543, 252);
		panel_2.add(scrollPane_1);
		
		table_1 = new JTable();
		table_1.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
					"Exam_Code", "Exam_Name", "Exam_Desc", "Exam_Amount", "Select"
				}
			));
		table_1.getColumnModel().getColumn(1).setPreferredWidth(115);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(98);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(124);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(101);
		scrollPane_1.setViewportView(table_1);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(null, "Updated Exam", TitledBorder.LEFT, TitledBorder.TOP, null, null));
		panel_3.setBounds(717, 358, 556, 245);
		frame.getContentPane().add(panel_3);
		panel_3.setLayout(null);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(10, 24, 536, 210);
		panel_3.add(scrollPane_2);
		
		table_2 = new JTable();
		table_2.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Exam_Code", "Exam_Name", "Exam_Desc", "Rate_Per_Exam", "Exam_Qty", "Exam_Amount"
			}
		));
		table_2.getColumnModel().getColumn(1).setPreferredWidth(103);
		table_2.getColumnModel().getColumn(2).setPreferredWidth(121);
		table_2.getColumnModel().getColumn(3).setPreferredWidth(97);
		table_2.getColumnModel().getColumn(5).setPreferredWidth(92);
		scrollPane_2.setViewportView(table_2);
		
		 comboBox = new JComboBox();
		 comboBox.addActionListener(new ActionListener() {
		 	public void actionPerformed(ActionEvent e) {
		 		 InsName=comboBox.getSelectedItem().toString();
		 		System.out.println(InsName);
		 		CashConvertInsuranceDBConnection ConvertDB =new CashConvertInsuranceDBConnection();
				
				ResultSet rs=ConvertDB.retreiveExamtable(InsName);
				try {
					while(rs.next())
					{
						 ExamTable=rs.getObject(1).toString();
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				ConvertDB.closeConnection();
				System.out.println(ExamTable);
				populateTableInsurance(ExamTable);
				
		 		
		 	}
		 });
		comboBox.setBounds(811, 24, 210, 22);
		frame.getContentPane().add(comboBox);
		
		JLabel lblNewLabel_4 = new JLabel("Insurance : ");
		lblNewLabel_4.setBounds(724, 28, 61, 14);
		frame.getContentPane().add(lblNewLabel_4);
		
		JButton btnNewButton = new JButton("Convert Exam");
		btnNewButton.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	
		    	handleButtonClick();
		    	populateTableNew();
		    }
		     
		});


		btnNewButton.setBounds(599, 228, 101, 41);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Direct");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int selectedRow = table.getSelectedRow();  // Get the selected row index from table
			        if (selectedRow != -1) {
			        	ITEM_ID_NEW.add(ITEM_ID.get(selectedRow));
			              ITEM_NAME_NEW.add(ITEM_NAME.get(selectedRow));    // Add Name to ITEM_NAME_NEW
			              ITEM_DESC_NEW.add(ITEM_DESC.get(selectedRow));    // Add Description to ITEM_DESC_NEW
			              DATE_NEW.add(DATE.get(selectedRow));
			              TYPE_NEW.add(TYPE.get(selectedRow));
			              PAGE_NO_NEW.add(PAGE_NO.get(selectedRow)); // Add Amount to DATE_NEW (assuming Amount is Date)
			              MRP_NEW.add(MRP.get(selectedRow));
			              PER_ITEM_PRICE_NEW.add(PER_ITEM_PRICE.get(selectedRow));
			              QTY_NEW.add(QTY.get(selectedRow));
			              AMOUNT_NEW.add(AMOUNT.get(selectedRow));
			              BATCH_NEW.add(BATCH.get(selectedRow));
			              EXPIRY_NEW.add(EXPIRY.get(selectedRow));
			              if(PACKAGE_DAYS.size()>0) {
			              PACKAGE_DAYS_NEW.add(PACKAGE_DAYS.get(selectedRow));
			              PACKAGE_DATES_NEW.add(PACKAGE_DATES.get(selectedRow));
			              }
			              populateTableFromVectors();

			            
			        }
			        else {
			            JOptionPane.showMessageDialog(frame, "No row selected in the first table.");
			        }
			        populateTableNew();
			}
		        
		});
		btnNewButton_1.setBounds(599, 298, 99, 41);
		frame.getContentPane().add(btnNewButton_1);
		
		JButton btnNewButton_3 = new JButton("Save");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 int selectedRow = table.getSelectedRow();  // Get the selected row index from table
			        if (selectedRow != -1) {
				SURGERY_CALCULATION(PER_ITEM_PRICE_NEW, ipd_id, p_insurancetype);
				PACKAGE_AMOUNT_CALCULATION();
				IPDExpensesDBConnection db= new IPDExpensesDBConnection();
				IPDDBConnection db1 = new IPDDBConnection();
				try {
					//db1.updateIsDraftStatus(ipd_id);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				db1.closeConnection();
				String[] data=new String[20];
				data[0]=ipd_id;
				data[1]=p_id;
				data[2]=p_name;
				data[15]=REBATE+"";
				
					for(int i=0;i<ITEM_ID_NEW.size();i++) {
						System.out.println("--------------------"+ITEM_NAME.get(i));
						data[3]=ITEM_NAME_NEW.get(i);
						data[4]=ITEM_DESC_NEW.get(i);
						data[5]=DATE_NEW.get(i);
						data[6]=TYPE_NEW.get(i);
						data[7]=ITEM_ID_NEW.get(i);
						data[8]=PAGE_NO_NEW.get(i);
						data[9]=MRP_NEW.get(i);
						data[10]=PER_ITEM_PRICE_NEW.get(i)+"";
						data[11]=QTY_NEW.get(i);
						data[12]=AMOUNT_NEW.get(i)+"";
						data[13]=BATCH_NEW.get(i);
						data[14]=EXPIRY_NEW.get(i);

						try {
							db.inserInsuranceData(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				
			     
					 CashConvertInsuranceDBConnection convertDB = new CashConvertInsuranceDBConnection();
				     convertDB.ExamMapingCode(examCodeMap,InsName);
				
			}
			        else {
			        	 JOptionPane.showMessageDialog(frame, "Atleast One Exam To Be Added."); 
			        }
		}
		});
		btnNewButton_3.setBounds(739, 614, 89, 31);
		frame.getContentPane().add(btnNewButton_3);
		
		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.setBounds(856, 614, 89, 32);
		frame.getContentPane().add(btnNewButton_4);
		final int[] currentOldExamCodeIndex = {0}; // Index for the current oldExamCode
		final List<String> oldExamCodes = new ArrayList<>(examCodeMap.keySet()); // Get list of oldExamCodes
		final List<String> currentRelatedCodes = new ArrayList<>();
		
		JLabel lblNewLabel_5 = new JLabel("Search : ");
		lblNewLabel_5.setBounds(1055, 28, 46, 14);
		frame.getContentPane().add(lblNewLabel_5);
		
		textField_4 = new JTextField();
		textField_4.setBounds(1111, 25, 155, 20);
		frame.getContentPane().add(textField_4);
		textField_4.setColumns(10);
		table.getColumnModel().getColumn(0).setPreferredWidth(83);
		table.getColumnModel().getColumn(1).setPreferredWidth(113);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(108);
		table.getColumnModel().getColumn(4).setPreferredWidth(71);
		table.getColumnModel().getColumn(5).setPreferredWidth(92);
	}
	
	public void getInsurance(String ipd_id, String Insurance) {
		CashConvertInsuranceDBConnection ConvertDB =new CashConvertInsuranceDBConnection();
		
		ResultSet rs=ConvertDB.retreiveInsurance();
		try {
			while(rs.next()) {
				GetInsModel.addElement(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		comboBox.setModel(GetInsModel);
		comboBox.setSelectedItem(Insurance.toUpperCase());
		PatientDetail(ipd_id,Insurance);
		ConvertDB.closeConnection();
		
	}
	
	
	public void getMappingData(String ExamTable, String p_insurancetype, String ipd_id) {
		CashConvertInsuranceDBConnection ConvertDB =new CashConvertInsuranceDBConnection();
//		ITEM_NAME_NEW.removeAllElements();
//		DATE_NEW.removeAllElements();
//		TYPE_NEW.removeAllElements();
//		ITEM_ID_NEW.removeAllElements();
//		PAGE_NO_NEW.removeAllElements();
//		MRP_NEW.removeAllElements();
//		PER_ITEM_PRICE_NEW.removeAllElements();
//		QTY_NEW.removeAllElements();
//		AMOUNT_NEW.removeAllElements();
//		BATCH_NEW.removeAllElements();
//		EXPIRY_NEW.removeAllElements();
//		ITEM_DESC_NEW.removeAllElements();
		ResultSet rs=ConvertDB.retreive_mapping_data(ExamTable, p_insurancetype.toLowerCase(), ipd_id);
		try {
			while(rs.next()) {
				ITEM_NAME_NEW.add(rs.getString(3));
				DATE_NEW.add(rs.getString(5));  
				TYPE_NEW.add(rs.getString(1));
				ITEM_ID_NEW.add(rs.getString(2));
				PAGE_NO_NEW.add(rs.getString(4));
				MRP_NEW.add(rs.getString(9));
				PER_ITEM_PRICE_NEW.add(rs.getDouble(10)); 
				QTY_NEW.add(rs.getString(11));
				AMOUNT_NEW.add(rs.getDouble(12));
				BATCH_NEW.add(rs.getString(6));
				EXPIRY_NEW.add(rs.getObject(7)==null||rs.getObject(7).equals("")?"":rs.getObject(7)+"");
				ITEM_DESC_NEW.add(rs.getString(3));
				if(rs.getInt(8)!=0) {
					PACKAGE_DAYS_NEW.add(rs.getInt(8));
					PACKAGE_DATES_NEW.add(rs.getString(5));
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		ConvertDB.closeConnection();
		
	}
      
	public void PatientDetail(String ipd_id1,String Insurance) {
		CashConvertInsuranceDBConnection ConvertDB =new CashConvertInsuranceDBConnection();
		
		ResultSet rs=ConvertDB.retreivePatientdetail(ipd_id1);
		try {
			while(rs.next()) {
				
				
				ipd_id=rs.getObject(2).toString();
				p_id=rs.getObject(3).toString();
				p_name=rs.getObject(1).toString();
				p_insurancetype=rs.getObject(4).toString() ;
				
				insurance_category =rs.getObject(5)==null ?"SEMI PVT CATEGORY":rs.getObject(5).toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		textField.setText(p_name);
		textField_1.setText(p_id);
		textField_2.setText(ipd_id);
		textField_3.setText(p_insurancetype);
		textField_5.setText(insurance_category);
		System.out.println(textField_2.getText().toString()+" jjjjjjjjjjj+ " + ExamTable.toLowerCase());
		
		ConvertDB.closeConnection();
		GetBillDATA();
		populateTableFromVectors();
		populateTableNew();
	}
	
	
	
	// Declare vectors to store data
	

	public void populateTableInsurance(String examName) {
		EXAMCODEINS.removeAllElements();
		EXAMNAMEINS.removeAllElements();
		EXAMDESCINS.removeAllElements();
		EXAMRATEINS.removeAllElements();
	    try {
	    	
	        System.out.println(examName + " name of exam");
	        CashConvertInsuranceDBConnection convertDB = new CashConvertInsuranceDBConnection();
	        ResultSet rs = convertDB.retreiveInsuranceTableValue(ExamTable);
	        
	        while (rs.next()) {
	            // Assuming column indexes for "Exam_Code", "Exam_Name", "Exam_Desc", "Exam_Amount" are 1, 2, 3, 4
	            EXAMCODEINS.add(rs.getString(1)); // Exam_Code
	            EXAMNAMEINS.add(rs.getString(2)); // Exam_Name
	            EXAMDESCINS.add(rs.getString(3));   // Exam_Desc
	           
	            EXAMRATEINS.add(rs.getString(4)); // Exam_Amount
	        }
	        
	        // Convert vectors to object array for table
	        int numRows = EXAMCODEINS.size();
	        Object[][] rowsObjectArray = new Object[numRows][5];  // Last column for "Select" checkbox
	        for (int i = 0; i < numRows; i++) {
	            rowsObjectArray[i][0] = EXAMCODEINS.get(i);
	            rowsObjectArray[i][1] = EXAMNAMEINS.get(i);
	            rowsObjectArray[i][2] = EXAMDESCINS.get(i);
	            rowsObjectArray[i][3] = EXAMRATEINS.get(i);
	            rowsObjectArray[i][4] = Boolean.FALSE;  // Default unselected
	        }
	        
	        String[] columnNames = {"Exam_Code", "Exam_Name", "Exam_Desc", "Exam_Amount", "Select"};
	        DefaultTableModel model = new DefaultTableModel(rowsObjectArray, columnNames) {
	            @Override
	            public Class<?> getColumnClass(int columnIndex) {
	                if (columnIndex == 4) {
	                    return Boolean.class;  // "Select" column is Boolean
	                } else if (columnIndex == 3) {
	                    return String.class;   // "Exam_Amount" column is Double
	                }
	                return String.class;       // Other columns are Strings
	            }
	        };
	        table_1.getColumn("Select").setCellRenderer(new DefaultTableCellRenderer() {
	            @Override
	            public void setValue(Object value) {
	                super.setValue(value);
	                setHorizontalAlignment(SwingConstants.CENTER);
	            }
	        });
	        
	        // Add an editor (checkbox) for the "Select" column
	        table_1.getColumn("Select").setCellEditor(new DefaultCellEditor(new JCheckBox()));

	        table_1.setModel(model);
            convertDB.closeConnection();
	    } catch (SQLException ex) {
	        Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}


	public void populateTableFromVectors() {
	    try {
	        // Prepare rows for the table
	        int numRows = 0;

	        // Count how many rows meet the condition (Type == "M" or "O")
	        for (String type : TYPE) {
	            if ("M".equals(type) || "O".equals(type)) {
	                numRows++;
	            }
	        }

	        // Prepare the rows object array
	        Object[][] rowsObjectArray = new Object[numRows][9]; // 9 columns now, including "Select"
	        int rowIndex = 0;
	        
	        // Loop through the vectors and add rows that meet the condition
	        for (int i = 0; i < ITEM_NAME.size(); i++) {
	            String type = TYPE.get(i);
	            if ("M".equals(type) || "O".equals(type)) {
	                rowsObjectArray[rowIndex][0] = ITEM_ID.get(i);        // Exam_Code
	                rowsObjectArray[rowIndex][1] = ITEM_NAME.get(i);      // Exam_Name
	                rowsObjectArray[rowIndex][2] = ITEM_DESC.get(i);      // Exam_Desc
	                rowsObjectArray[rowIndex][3] = DATE.get(i);           // Date
	                rowsObjectArray[rowIndex][4] = TYPE.get(i);           // Type
	                rowsObjectArray[rowIndex][5] = PER_ITEM_PRICE.get(i); // Rate_Per_Exam
	                rowsObjectArray[rowIndex][6] = QTY.get(i);            // Exam_Qty
	                rowsObjectArray[rowIndex][7] = AMOUNT.get(i);         // Exam_Amount
	                rowsObjectArray[rowIndex][8] = Boolean.FALSE;         // "Select" column, initialized to false
	                rowIndex++;
	            }
	        }

	        String[] columnNames = { 
	            "Exam_Code", "Exam_Name", "Exam_Desc", "Date", 
	            "Type", "Rate_Per_Exam", "Exam_Qty", "Exam_Amount", "Select"
	        };

	        // Debugging column names
	        System.out.println(Arrays.toString(columnNames));  // Ensure "Select" is part of the column names

	        // Creating the table model
	        DefaultTableModel model = new DefaultTableModel(rowsObjectArray, columnNames) {
	            @Override
	            public Class getColumnClass(int columnIndex) {
	                if (columnIndex == 7) { // Exam_Amount column
	                    return Double.class;
	                } else if (columnIndex == 6) { // Exam_Qty column
	                    return String.class;
	                } else if (columnIndex == 0) { // Exam_Code column
	                    return String.class;
	                } else if (columnIndex == 8) { // "Select" column (checkbox)
	                    return Boolean.class;
	                }
	                return super.getColumnClass(columnIndex);
	            }
	        };

	        // Set the model to the table
	       

	        // Add a checkbox renderer for the "Select" column
	        table.getColumn("Select").setCellRenderer(new DefaultTableCellRenderer() {
	            @Override
	            public void setValue(Object value) {
	                super.setValue(value);
	                setHorizontalAlignment(SwingConstants.CENTER);
	            }
	        });

	        // Add an editor (checkbox) for the "Select" column
	        table.getColumn("Select").setCellEditor(new DefaultCellEditor(new JCheckBox()));

	        // Add a TableModelListener to ensure only one checkbox is selected at a time
	        model.addTableModelListener(new TableModelListener() {
	           
	            
	            @Override
	            public void tableChanged(TableModelEvent e) {
	                if (tableUpdateInProgress) {
	                    return; // Prevent recursion
	                }
	                tableUpdateInProgress = true; // Flag as in progress

	                try {
	                	int rowCount = table.getRowCount();
		                for (int i = 0; i < rowCount; i++) {
		                    if (i != e.getFirstRow()) {
		                        table.setValueAt(Boolean.FALSE, i, 8);  // Uncheck all other checkboxes
		                    }
		                }
	                } finally {
	                    tableUpdateInProgress = false; // Reset flag
	                }
	            }
	        });

	        // Set the model to the table
	        table.setModel(model);

	    } catch (Exception ex) {
	        Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE, null, ex);
	    }
	}



	
	
	
	public void populateTableNew() {
        getMappingData(ExamTable, p_insurancetype, ipd_id);
	    int numRows = ITEM_ID_NEW.size();
	    Object[][] rowsObjectArray = new Object[numRows][5];  // Last column for "Delete" button
	    for (int i = 0; i < numRows; i++) {
	        rowsObjectArray[i][0] = ITEM_ID_NEW.get(i);
	        rowsObjectArray[i][1] = ITEM_NAME_NEW.get(i);
	        rowsObjectArray[i][2] = ITEM_DESC_NEW.get(i);
	        rowsObjectArray[i][3] = AMOUNT_NEW.get(i);
	        rowsObjectArray[i][4] = "Delete";   
	    }

	    String[] columnNames = {"Exam_Code", "Exam_Name", "Exam_Desc", "Exam_Amount", "Delete"};
	    DefaultTableModel model = new DefaultTableModel(rowsObjectArray, columnNames) {};

	    table_2.setModel(model);
	    table_2.getColumn("Delete").setCellRenderer(new ButtonRenderer());
	    table_2.getColumn("Delete").setCellEditor(new ButtonEditor(new JCheckBox()));
	}

	// ButtonRenderer for displaying the "Delete" button
	class ButtonRenderer extends JButton implements TableCellRenderer {
	    public ButtonRenderer() {
	        setText("Delete");
	        setOpaque(true);
	    }

	    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	        return this;
	    }
	}

	// ButtonEditor for editing the "Delete" button
	class ButtonEditor extends DefaultCellEditor {
	    protected JButton button;
	    private String label;
	    private int row;

	    public ButtonEditor(JCheckBox checkBox) {
	        super(checkBox);
	        button = new JButton();
	        button.setOpaque(true);

	        button.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                try {
	                	row=table_2.getSelectedRow();
	                	System.out.println(row+"   hhhhh   "+table_2.getRowCount() );
	                    // Ensure the row index is valid
	                    if (row!=-1) {
	                        // Handle the deletion of the row when the "Delete" button is clicked
	                        DefaultTableModel model = (DefaultTableModel) table_2.getModel();
	                        model.removeRow(row);

//	                         Remove data from the internal lists as well (if needed)
	                        ITEM_ID_NEW.remove(row);
	                        ITEM_NAME_NEW.remove(row);
	                        ITEM_DESC_NEW.remove(row);
	                        DATE_NEW.remove(row);
	                        TYPE_NEW.remove(row);
	                        PAGE_NO_NEW.remove(row);
	                        MRP_NEW.remove(row);
	                        PER_ITEM_PRICE_NEW.remove(row);
	                        QTY_NEW.remove(row);
	                        AMOUNT_NEW.remove(row);
	                        BATCH_NEW.remove(row);
	                        EXPIRY_NEW.remove(row);
	                        if(PACKAGE_DAYS.size()>0) {
	                        PACKAGE_DAYS_NEW.remove(row);
	                        PACKAGE_DATES_NEW.remove(row);
	                        }
	                       
	                        // Repaint to reflect the change in the UI
	                        populateTableNew();
	                    }
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            }
	        });
	    }

	    @Override
	    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
	        this.row = row;  // Ensure the row is properly set here
	        label = (value == null) ? "" : value.toString();
	        button.setText(label);
	        return button;
	    }

	    @Override
	    public Object getCellEditorValue() {
	        return label;
	    }
	}


	
	public void handleButtonClick() {
	    // Check if there is at least one selected row in both tables
	    boolean isTableSelected = table.getSelectedRow() != -1; // Check if a row is selected in table
	    boolean isTable1Selected = false;

	    // Check if any rows are selected in table_1 (multiple checkboxes can be selected)
	    int rowCount = table_1.getRowCount();
	    for (int i = 0; i < rowCount; i++) {
	        boolean isSelected = (boolean) table_1.getValueAt(i, 4);  // Assuming the checkbox is in column index 4
	        if (isSelected) {
	            isTable1Selected = true;
	            break;  // If at least one row is selected, we can break the loop
	        }
	    }

	    // Check if both tables have selected rows
	    if (isTableSelected && isTable1Selected) {
	        // Add data from selected row in table to vectors
	        int selectedRow = table.getSelectedRow();
	        String selectedCode = (String) table.getValueAt(selectedRow, 0);
	        String selectedName = (String) table.getValueAt(selectedRow, 1);
	        String selectedDesc = (String) table.getValueAt(selectedRow, 2);
	        String selectedAmount = (String) table.getValueAt(selectedRow, 3);

	     
	        // Add data from selected rows in table_1 to vectors
	        StringBuilder selectedRowsData = new StringBuilder();
	        for (int i = 0; i < rowCount; i++) {
	            boolean isSelected = (boolean) table_1.getValueAt(i, 4);  // Checkbox column
	            if (isSelected) {
	                String code = (String) table_1.getValueAt(i, 0);
	                String name = (String) table_1.getValueAt(i, 1);
	                String desc = (String) table_1.getValueAt(i, 2);
	                double amount = Double.parseDouble((String) table_1.getValueAt(i, 3));

//	              // Add to vectors
	              ITEM_ID_NEW.add(code);
	              ITEM_NAME_NEW.add(name);    // Add Name to ITEM_NAME_NEW
	              ITEM_DESC_NEW.add(desc);    // Add Description to ITEM_DESC_NEW
	              DATE_NEW.add(DATE.get(selectedRow));
	              TYPE_NEW.add(TYPE.get(selectedRow));
	              PAGE_NO_NEW.add("N"); // Add Amount to DATE_NEW (assuming Amount is Date)
	              MRP_NEW.add("0");
	              PER_ITEM_PRICE_NEW.add(amount);
	              QTY_NEW.add("1");
	              AMOUNT_NEW.add(amount);
	              BATCH_NEW.add("");
	              EXPIRY_NEW.add("");
	              if(PACKAGE_DAYS.size()>0) {
	              PACKAGE_DAYS_NEW.add(PACKAGE_DAYS.get(selectedRow));
	              PACKAGE_DATES_NEW.add(PACKAGE_DATES.get(selectedRow));
	              
	              }
	              if (!examCodeMap.containsKey(selectedCode)) {
                      // Initialize a new ArrayList for the old exam code if it doesn't exist
                      examCodeMap.put(selectedCode, new ArrayList<String>());
                  }
                  // Add the new exam code to the list
                  examCodeMap.get(selectedCode).add(code);
	                // Collect selected rows data for display
	                selectedRowsData.append("Selected Row from Table_1:\n")
	                        .append("Code: ").append(code).append("\n")
	                        .append("Name: ").append(name).append("\n")
	                        .append("Description: ").append(desc).append("\n")
	                        .append("Amount: ").append(amount).append("\n\n");
	            }
	        }

	        // Display the selected data
	        if (selectedRowsData.length() > 0) {
	            JOptionPane.showMessageDialog(null, selectedRowsData.toString());
	        }

	        // Notify user that data has been added to vectors
	        JOptionPane.showMessageDialog(null, "Data has been added to vectors.");
	        populateTableInsurance("");
	        populateTableFromVectors();
	      System.out.println(examCodeMap+ "  test ");

	      
	    } else {
	        // Show error message if no row is selected in either table
	        JOptionPane.showMessageDialog(null, "Please select rows in both tables.");
	    }
	}

	

	
	
	
	

	
   
    
    
    
    
    private void GetBillDATA() {
		// function to get all main data from ipd_expenses
		ITEM_NAME.removeAllElements();
		DATE.removeAllElements();
		TYPE.removeAllElements();
		ITEM_ID.removeAllElements();
		PAGE_NO.removeAllElements();
		MRP.removeAllElements();
		PER_ITEM_PRICE.removeAllElements();
		QTY.removeAllElements();
		AMOUNT.removeAllElements();
		BATCH.removeAllElements();
		EXPIRY.removeAllElements();
		ITEM_DESC.removeAllElements();
		IPDExpensesDBConnection db1 = new IPDExpensesDBConnection();
		ResultSet rs = db1.retrieveAllIPD_DATA(ipd_id,p_insurancetype);
		/** getting ipd data from DB**/
//		BED_CALCULAION();
//		ADD_CONSULTATION();
		try {
			while (rs.next()) {
				ITEM_NAME.add(rs.getString(3));
				DATE.add(rs.getString(5));  
				TYPE.add(rs.getString(1));
				if(rs.getString(1).equals("C")) {
					consultant=rs.getString(3);	
				}
				ITEM_ID.add(rs.getString(2));
				PAGE_NO.add(rs.getString(4));
				MRP.add(rs.getString(9));
				PER_ITEM_PRICE.add(rs.getDouble(10)); 
				QTY.add(rs.getString(11));
				AMOUNT.add(rs.getDouble(12));
				BATCH.add(rs.getString(6));
				EXPIRY.add(rs.getObject(7)==null||rs.getObject(7).equals("")?"":rs.getObject(7)+"");
				ITEM_DESC.add(rs.getString(3));
				if(rs.getInt(8)!=0) {
					PACKAGE_DAYS.add(rs.getInt(8));
					PACKAGE_DATES.add(rs.getString(5));
				}

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		SURGERY_CALCULATION(PER_ITEM_PRICE, ipd_id, p_insurancetype);
//		if(REBATE!=0) {
//			chckbxRebate.setSelected(true);
//			chckbxRebate.setText("Rebate("+REBATE+"%)");
//		}
		db1.closeConnection();
//		PACKAGE_AMOUNT_CALCULATION();

	}
    
    
    
    
    
    public void SURGERY_CALCULATION(Vector<Double> AMT , String ipd_id, String p_insurancetype) {
		// TODO Auto-generated method stub

		double max1 = 0;
		double max2 = 0;
		double max3 = 0;
		int index1 = -1;
		int index2 = -1;
		int index3 = -1;

		int loopStart=TYPE_NEW.indexOf("S");// to get first index of surgery
		int loopEnd=TYPE_NEW.lastIndexOf("S");//to get last index of surgery

		/***1# code to found top most three surgeries for applying rules of insurance****/
		if (loopStart!=-1 && loopEnd!=-1) {
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE_NEW.get(i).equals("S") && AMT.get(i) > max1) {
					max1 = AMT.get(i);
					index1 = i;
				}
			}
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE_NEW.get(i).equals("S") && AMT.get(i) > max2 && i != index1) {
					max2 = AMT.get(i);
					index2 = i;
				}
			}
			for (int i = loopStart; i <= loopEnd; i++) {
				if (TYPE_NEW.get(i).equals("S") && AMT.get(i) > max3 && i != index2 && i != index1) {
					max3 = AMT.get(i);
					index3 = i;
				}
			} 
		}
		/****1# end ****/
		//System.out.println("max1: "+max1+"max2: "+max2+"max3: "+max3);

		/** DB function for getting surgeries discounts and rebate values from database  **/
		InsuranceDBConnection db = new InsuranceDBConnection();
		ResultSet rs = db.RetrieveSurgeryDATA(p_insurancetype, insurance_category);
		/** DB end  **/
		double s1 = 0, s2 = 0, s3 = 0;// veriables for surgery percentages
		double d1 = 1, d2 = 1, d3 = 1;// dividers for three top most surgery
		try {
			while (rs.next()) {
				s1 = rs.getDouble(1);
				s2 = rs.getDouble(2);
				s3 = rs.getDouble(3);
				d1 = rs.getDouble(4);
				d2 = rs.getDouble(5);
				d3 = rs.getDouble(6);
				REBATE = rs.getDouble(7); /** rebate for overall bill calculation **/ 
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		/**2# logic for decrease/increase the amount of top three surgeries acc. to insurance rules and their ins category **/
		switch (insurance_category) {

		case "GENERAL CATEGORY":
			if (index1 != -1) {
				max1 = AMOUNT_NEW.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max1 = max1 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME_NEW.set(index1, s);
				AMOUNT.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT_NEW.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME_NEW.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME_NEW.set(index2, s);
				AMOUNT_NEW.set(index2, max2);

			}
			if (index3 != -1) {
				max3 = AMOUNT_NEW.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME_NEW.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME_NEW.set(index3, s);
				AMOUNT_NEW.set(index3, max3);
			}
			break;

		case "SEMI PVT CATEGORY":
			System.out.println("SEMI PVT CATEGORY");
			if (index1 != -1) {
				max1 = AMOUNT_NEW.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max2 = max2 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME_NEW.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME_NEW.set(index1, s);
				AMOUNT_NEW.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT_NEW.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME_NEW.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME_NEW.set(index2, s);
				AMOUNT_NEW.set(index2, max2);
			}
			if (index3 != -1) {
				max3 = AMOUNT_NEW.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME_NEW.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME_NEW.set(index3, s);
				AMOUNT_NEW.set(index3, max3);
			}
			break;

		case "PVT CATEGORY":

			System.out.println("PVT CATEGORY");
			if (index1 != -1) {
				max1 = AMOUNT_NEW.get(index1);
				System.out.println("M1 BEFORE: " + max1);
				max1 = max1 + (s1 / 100.00) * max1;
				max2 = max2 / d1;
				System.out.println("M2 AFTER: " + max1);
				String s = ITEM_NAME_NEW.get(index1);
				s = s + "(" + s1 + "%)" + "/" + d1;
				ITEM_NAME_NEW.set(index1, s);
				AMOUNT_NEW.set(index1, max1);

			}
			if (index2 != -1) {
				max2 = AMOUNT_NEW.get(index2);
				System.out.println("M1 BEFORE: " + max2);
				max2 = max2 + (s2 / 100.00) * max2;
				max2 = max2 / d2;
				System.out.println("M2 AFTER: " + max2);
				String s = ITEM_NAME_NEW.get(index2);
				s = s + "(" + s2 + "%)" + "/" + d2;
				ITEM_NAME_NEW.set(index2, s);
				AMOUNT_NEW.set(index2, max2);

			}
			if (index3 != -1) {
				max3 = AMOUNT_NEW.get(index3);
				System.out.println("M1 BEFORE: " + max3);
				max3 = max3 + (s3 / 100.00) * max3;
				max3 = max3 / d3;
				System.out.println("M2 AFTER: " + max3);
				String s = ITEM_NAME_NEW.get(index3);
				s = s + "(" + s3 + "%)" + "/" + d3;
				ITEM_NAME_NEW.set(index3, s);
				AMOUNT_NEW.set(index3, max3);
			}
			break;

		}
		/**2# end**/
	}
    
    
    public void PACKAGE_AMOUNT_CALCULATION() {
		/** Function to do Zero Amount of that items which are used in between 
		 * the surgery days except services(represented by Z) and surgeries(represented by S).
		 * **/
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		/** logic to get all dates that under the surgery **/
		for(int i=0;i<PACKAGE_DATES_NEW.size();i++) {
			Calendar c1 = Calendar.getInstance();
			Date dt = null;
			try {
				dt = sdf.parse(PACKAGE_DATES_NEW.get(i));
			} catch (ParseException ex) {

			}
			c1.setTime(dt);  
			for(int j=0;j<PACKAGE_DAYS_NEW.get(i);j++) {	
				String date=sdf.format(c1.getTime());
				if(!(PACKAGE_AMOUNT_REMOVE_DATES_NEW.indexOf(date)!=-1)) {
					PACKAGE_AMOUNT_REMOVE_DATES_NEW.add(date); 
				}
				c1.add(Calendar.DAY_OF_MONTH, 1); 
			}

		}	
		/** code end **/

		/** code to replace the amount of all items which have the same dates with surgery dates**/
		for(int i=0;i<DATE_NEW.size();i++) {
			if(PACKAGE_AMOUNT_REMOVE_DATES_NEW.indexOf(DATE.get(i))!=-1 && !TYPE.get(i).equals("S") && !TYPE.get(i).equals("Z")) {
				AMOUNT_NEW.set(i, 0.0);//to replace the amount
			}
		}
		/** code end  **/
	}
    
    
    
  
    
}