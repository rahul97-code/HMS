package hms.insurance.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.GeneralDBConnection;
import hms.patient.slippdf.PillsSlipDepartment;
import hms.reception.gui.ReceptionMain;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.InvoiceBrowser;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class InsuranceTodoMaster extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctorName = new DefaultComboBoxModel();
	final DefaultComboBoxModel insModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel taskTypeModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel freqModel = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();

	Vector<String> insID = new Vector<String>();

	double price = 0, taxValue = 0, surchargeValue = 0;
	String doctorNameSTR;
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR, expiryDateSTR = "", issuedDateSTR = "",
			dueDateSTR = "", previouseStock = "", itemBatchNameSTR = "", itemLocationSTR = "", stockItem = "";
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, finalTaxValue = 0, finalDiscountValue = 0,
			finalTotalValue = 0;
	double itemValue;
	int quantity = 0, batchQty = 0;
	String batchIDSTR = "0";
	boolean flag = false;
	Object[][] ObjectArray_ListOfexamsSpecs;
	int input = -1;
	private JButton btnNewButton;
	private JTextField taskNameTF;
	private JTextField taskDescTF;
	protected String taskTypeSTR;
	private JComboBox taskTypeTFCB;
	private JComboBox freqCB;
	private JComboBox insCB;
	private JCheckBox chckbxCh1;
	private JCheckBox chckbxCh2;
	private JCheckBox chckbxCh3;
	protected String taskID=null;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			 ReceptionMain.userName="test";            // created_by (if not used, leave empty)
			    ReceptionMain.receptionIdSTR="10";   
			InsuranceTodoMaster dialog = new InsuranceTodoMaster();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public InsuranceTodoMaster() {
		setTitle("Issue Items Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InsuranceTodoMaster.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 545);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		btnNewButton = new JButton("Save"); 
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (taskNameTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter Task Name.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (taskDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter Task Description.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (insCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please select Insurance Type.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (freqCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please select frequency Type.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (taskTypeTFCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null, "Please select Task Type.", "Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				// --- Prepare data ---
				String taskId = taskID; // Generate short random ID
				String taskName = taskNameTF.getText().trim();
				String taskDesc = taskDescTF.getText().trim();
				String taskType = taskTypeSTR;
				String insuranceType = insCB.getSelectedItem().toString();
				String frequency = freqCB.getSelectedItem().toString();

				String choiceSet1 = chckbxCh1.isSelected()?"1":"0";
				String choiceSet2 = chckbxCh2.isSelected()?"1":"0";
				String choiceSet3 = chckbxCh3.isSelected()?"1":"0";

				String createdAt = null;
				String updatedAt = null;

				// --- Add to Table ---
				String[] todoData = new String[] {
					    taskId,        // assuming all are strings
					    taskName,
					    taskDesc,
					    taskType,
					    insuranceType,
					    insID.get(insCB.getSelectedIndex()),            // ins_id (if not used, leave empty)
					    frequency,
					    ReceptionMain.userName,            // created_by (if not used, leave empty)
					    ReceptionMain.receptionIdSTR,            // created_by_id (if not used, leave empty)
					    choiceSet1,
					    choiceSet2,
					    choiceSet3
					};
				InsuranceDBConnection DB = new InsuranceDBConnection();
				try {
					DB.insertTodo(todoData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				DB.closeConnection();
				populateTable();
				clearData();

			}
			public void clearData() {
				taskNameTF.setText("");
				taskDescTF.setText("");
				taskTypeTFCB.setSelectedIndex(0);
				insCB.setSelectedIndex(0);
				freqCB.setSelectedIndex(0);
				chckbxCh1.setSelected(false);
				chckbxCh2.setSelected(false);
				chckbxCh3.setSelected(false);

			}
		});



		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(503, 68, 106, 31);
		contentPanel.add(btnNewButton);

		final JButton btnRemove = new JButton("Delete");
		btnRemove.addActionListener(new ActionListener() {
		    @Override
		    public void actionPerformed(ActionEvent e) {
		        int cur_selectedRow = table.getSelectedRow();
		        if (cur_selectedRow == -1) {
		            JOptionPane.showMessageDialog(null, "Please select a row to delete.", "No Selection", JOptionPane.WARNING_MESSAGE);
		            return;
		        }

		        cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
		        String toDelete = table.getModel().getValueAt(cur_selectedRow, 0).toString();

		        // Confirmation dialog
		        int confirm = JOptionPane.showConfirmDialog(
		                null,
		                "Are you sure you want to delete this record?",
		                "Delete Confirmation",
		                JOptionPane.YES_NO_OPTION
		        );

		        if (confirm == JOptionPane.YES_OPTION) {
		            try {
		            	InsuranceDBConnection DB = new InsuranceDBConnection();
		            	DB.deleteTaskRow(toDelete); // implement this method to delete from DB

		                // Remove row from table
		                ((DefaultTableModel) table.getModel()).removeRow(cur_selectedRow);

		                JOptionPane.showMessageDialog(null, "Record deleted successfully.");
		            } catch (Exception ex) {
		                JOptionPane.showMessageDialog(null, "Error deleting record: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		            }
		        }
		    }
		});

		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(621, 68, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 138, 992, 362);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(
			    new Object[][] {},
			    new String[] {
			        "Task ID", 
			        "Task Name", 
			        "Description", 
			        "Task Type", 
			        "Insurance Type", 
			        "Frequency", 
			        "Choice Set 1", 
			        "Choice Set 2", 
			        "Choice Set 3", 
			        "Created At", 
			        "Updated At"
			    }
			) {
			    @Override
			    public boolean isCellEditable(int row, int column) {
			        return false; // ❌ Disable editing for all cells
			    }
			});


		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				int selectedRowIndex = table.getSelectedRow();
				selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);
				int selectedColumnIndex = table.getSelectedColumn();
				btnRemove.setEnabled(true);
			}
		});
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {

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
				if (arg0.getClickCount() == 2) {
					int row = table.getSelectedRow();

		            // Get model index (in case sorting is applied)
		            int modelRow = table.convertRowIndexToModel(row);
		            DefaultTableModel model = (DefaultTableModel) table.getModel();

		            // --- Retrieve data from selected row ---
		             taskID       = (String) model.getValueAt(modelRow, 0);
		            String taskName     = (String) model.getValueAt(modelRow, 1);
		            String taskDesc     = (String) model.getValueAt(modelRow, 2);
		            String taskType     = (String) model.getValueAt(modelRow, 3);
		            String insuranceType= (String) model.getValueAt(modelRow, 4);
		            String frequency    = (String) model.getValueAt(modelRow, 5);
		            String choiceSet1   = (String) model.getValueAt(modelRow, 6);
		            String choiceSet2   = (String) model.getValueAt(modelRow, 7);
		            String choiceSet3   = (String) model.getValueAt(modelRow, 8);
		            // createdAt = model.getValueAt(modelRow, 9);
		            // updatedAt = model.getValueAt(modelRow, 10);

		            // --- Set data into form fields ---
		            taskNameTF.setText(taskName);
		            taskDescTF.setText(taskDesc);

		            // Select combo boxes safely
		            taskTypeTFCB.setSelectedItem(taskType);
		            insCB.setSelectedItem(insuranceType);
		            freqCB.setSelectedItem(frequency);

		            // Checkboxes (convert from "1"/"0" or true/false)
		            chckbxCh1.setSelected("1".equals(choiceSet1) || "true".equalsIgnoreCase(choiceSet1));
		            chckbxCh2.setSelected("1".equals(choiceSet2) || "true".equalsIgnoreCase(choiceSet2));
		            chckbxCh3.setSelected("1".equals(choiceSet3) || "true".equalsIgnoreCase(choiceSet3));

		            // Optionally focus back to the first field
		            model.removeRow(modelRow);
		            taskNameTF.requestFocus();
				}
			}
		});

		JSeparator separator = new JSeparator();
		separator.setBounds(20, 111, 966, 2);
		contentPanel.add(separator);

		taskNameTF = new JTextField();
		taskNameTF.setFont(new Font("Dialog", Font.BOLD, 11));
		taskNameTF.setColumns(10);
		taskNameTF.setBounds(33, 36, 119, 20);
		contentPanel.add(taskNameTF);

		taskDescTF = new JTextField();
		taskDescTF.setFont(new Font("Dialog", Font.BOLD, 11));
		taskDescTF.setColumns(10);
		taskDescTF.setBounds(164, 36, 193, 20);
		contentPanel.add(taskDescTF);

		freqCB = new JComboBox();
		freqCB.setFont(new Font("Dialog", Font.BOLD, 11));
		freqCB.setBounds(369, 36, 163, 20);
		contentPanel.add(freqCB);

		insCB = new JComboBox();
		insCB.setFont(new Font("Dialog", Font.BOLD, 11));
		insCB.setBounds(544, 36, 163, 20);
		contentPanel.add(insCB);

		chckbxCh1 = new JCheckBox("Choice 1");
		chckbxCh1.setBounds(46, 64, 93, 23);
		contentPanel.add(chckbxCh1);

		chckbxCh2 = new JCheckBox("Choice 2");
		chckbxCh2.setBounds(139, 64, 99, 23);
		contentPanel.add(chckbxCh2);

		chckbxCh3 = new JCheckBox("Choice 3");
		chckbxCh3.setBounds(234, 64, 99, 23);
		contentPanel.add(chckbxCh3);

		taskTypeTFCB = new JComboBox();
		taskTypeTFCB.setEditable(true);
		taskTypeTFCB.setFont(new Font("Dialog", Font.BOLD, 11));
		taskTypeTFCB.setBounds(719, 36, 163, 20);
		contentPanel.add(taskTypeTFCB);

		JLabel lblTaskName = new JLabel("Task Name :");
		lblTaskName.setBounds(33, 12, 93, 15);
		contentPanel.add(lblTaskName);

		JLabel lblTaskDesc = new JLabel("Task Desc :");
		lblTaskDesc.setBounds(164, 9, 93, 15);
		contentPanel.add(lblTaskDesc);

		JLabel lblTaskName_1 = new JLabel("Task frequency :");
		lblTaskName_1.setBounds(369, 12, 117, 15);
		contentPanel.add(lblTaskName_1);

		JLabel lblTaskName_2 = new JLabel("Task Insurance :");
		lblTaskName_2.setBounds(544, 12, 139, 15);
		contentPanel.add(lblTaskName_2);

		JLabel lblTaskName_3 = new JLabel("Task Type :");
		lblTaskName_3.setBounds(719, 12, 93, 15);
		contentPanel.add(lblTaskName_3);
		final JTextField ItemLocationtext = (JTextField) taskTypeTFCB.getEditor()
				.getEditorComponent();
		ItemLocationtext.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = ItemLocationtext.getText();
				if (!text.equals("")) {
					taskTypeSTR = text;
				}
			}
		});
		getAllTaskType();
		getAllInsType();
		getAllFreqType();
		populateTable();
	}

	public void getAllTaskType() {
		InsuranceDBConnection DB = new InsuranceDBConnection();
		ResultSet resultSet = DB.getTaskType();
		int i=0;
		taskTypeModel.addElement("");
		try {
			while (resultSet.next()) {
				taskTypeModel.addElement(resultSet.getString(1));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB.closeConnection();
		taskTypeTFCB.setModel(taskTypeModel);
		if (i > 0) {
			taskTypeTFCB.setSelectedIndex(0);
		}
	}
	public void getAllFreqType() {
		String[] taskFreqArr=null;
		InsuranceDBConnection DB = new InsuranceDBConnection();
		ResultSet resultSet = DB.getAllFreqType();
		freqModel.addElement("Select");
		int i=0;
		try {
			while (resultSet.next()) {
				taskFreqArr=resultSet.getString(1).split(",");
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (taskFreqArr != null) {
			for (int k = 0; k < taskFreqArr.length; k++) {
				freqModel.addElement(taskFreqArr[k].trim());
			}
		}

		DB.closeConnection();
		freqCB.setModel(freqModel);
		if (i > 0) {
			freqCB.setSelectedIndex(0);
		}
	}
	public void populateTable()
	{
		double amt=0;

		try {
			InsuranceDBConnection db = new InsuranceDBConnection();
			ResultSet rs = db.RetrieveTaskMasterDATA();

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
					Rows_Object_Array[R][C-1] = rs.getString(C);

				}

				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					  "Task ID", 
				        "Task Name", 
				        "Description", 
				        "Task Type", 
				        "Insurance Type", 
				        "Frequency", 
				        "Choice Set 1", 
				        "Choice Set 2", 
				        "Choice Set 3", 
				        "Created At", 
				        "Updated At"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
		


		} catch (SQLException ex) {
			Logger.getLogger(InvoiceBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void getAllInsType() {
		String[] taskTypeArr=null;
		InsuranceDBConnection DB = new InsuranceDBConnection();
		ResultSet resultSet = DB.retrieveAllData();
		insModel.addElement("Select");
		insID.add("0");
		int i=0;
		try {
			while (resultSet.next()) {
				insID.add(resultSet.getString(1));
				insModel.addElement(resultSet.getString(2));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DB.closeConnection();
		insCB.setModel( insModel);
		if (i > 0) {
			insCB.setSelectedIndex(0);
		}
	}
	
}
