package hms.exams.gui;

import hms.admin.gui.OPDTypeBrowser;
import hms.exam.database.ExamDBConnection;
import hms.exam.database.TestMasterDBConnection;
import hms.pricemaster.database.PriceMasterDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.ComboBoxModel;

public class AllExamsDetail extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JButton btnEditTableValue;
	private JButton btnEditTestRef;
	private JButton btnNewButton;
	private JButton btnNewButton_1;
	private JComboBox comboBox;
	private JButton btnAddTableValue;
	private JButton btnEditExam;
	int selectedRowIndex;
	JButton btnNewButton_dlt;
	Vector<String> allExams = new Vector<String>();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	
	final DefaultComboBoxModel tableName = new DefaultComboBoxModel();
	String exam_name = "All Exams";
	AddExamsType addExamsType;
	String examIDSTR = "", examNameSTR = "";
	private JLabel lblSelectTable;
	private JComboBox selectTableCB;
	String tableNameSTR="exam_master";
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AllExamsDetail dialog = new AllExamsDetail();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AllExamsDetail() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AllExamsDetail.class.getResource("/icons/rotaryLogo.png")));
		setTitle("All Exams");
		setBounds(100, 100, 939, 561);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		setModal(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(200, 11, 713, 501);
		contentPanel.add(scrollPane);
	
		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null }, { null, null, null, null }, },
				new String[] { "Exam Code", "Exam Category", "Exam Name",
						"Lab Name" }));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						try {
							btnNewButton_dlt.setEnabled(true);
							int selectedRowIndex = table.getSelectedRow();
							selectedRowIndex = table
									.convertRowIndexToModel(selectedRowIndex);
							Object examIndex = table.getModel().getValueAt(
									selectedRowIndex, 0);

							Object examCat = table.getModel().getValueAt(
									selectedRowIndex, 1);

							Object examName = table.getModel().getValueAt(
									selectedRowIndex, 2);

							examIDSTR = examIndex.toString();
							examNameSTR = examCat + " " + examName;
						} catch (Exception e2) {
							// TODO: handle exception
						}
					}
				});
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(150);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		scrollPane.setViewportView(table);
		//populateTable(exam_name);
		comboBox = new JComboBox(exams);
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					exam_name = comboBox.getSelectedItem().toString();
					System.out.println("Exam Name : +" + exam_name);
					populateTable(exam_name);

				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		comboBox.setBounds(17, 117, 173, 32);
		contentPanel.add(comboBox);

		JLabel lblNewLabel = new JLabel("Select Exam Category");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(17, 85, 173, 21);
		contentPanel.add(lblNewLabel);

		btnNewButton = new JButton("Add New Exam");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				addExamsType = new AddExamsType(tableNameSTR);
				addExamsType.setModal(true);
				addExamsType.setVisible(true);
			}
		});
		btnNewButton.setBounds(28, 159, 155, 37);
		contentPanel.add(btnNewButton);

		btnNewButton_1 = new JButton("Add Test Ref.");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (examIDSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select an exam", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AddTestReferences ds = new AddTestReferences(examNameSTR,
							"" + examIDSTR);

					ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					ds.setModal(true);
					ds.setVisible(true);

				}
			}
		});
		btnNewButton_1.setBounds(28, 207, 155, 37);
		contentPanel.add(btnNewButton_1);

		btnAddTableValue = new JButton("Add Table Value");
		btnAddTableValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (examIDSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select an exam", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					AddTestReferencesOutputs ds = new AddTestReferencesOutputs(
							examNameSTR, "" + examIDSTR);

					ds.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
					ds.setModal(true);
					ds.setVisible(true);

				}
			}
		});
		btnAddTableValue.setBounds(28, 255, 155, 37);
		contentPanel.add(btnAddTableValue);

		btnEditTestRef = new JButton("Edit Test Ref.");
		btnEditTestRef.setBounds(28, 303, 155, 37);
		contentPanel.add(btnEditTestRef);

		btnEditTableValue = new JButton("Edit Table Value");
		btnEditTableValue.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (examIDSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select an exam", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

//					new EditExamsType(examIDSTR).setVisible(true);
				}
			}
		});
		btnEditTableValue.setBounds(28, 351, 155, 37);
		contentPanel.add(btnEditTableValue);

		btnEditExam = new JButton("Edit Exam");
		btnEditExam.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (examIDSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select an exam", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					new EditExamsType(examIDSTR,tableNameSTR).setVisible(true);
				}

			}
		});
		btnEditExam.setBounds(28, 396, 155, 37);
		contentPanel.add(btnEditExam);
		
		lblSelectTable = new JLabel("Select Table");
		lblSelectTable.setHorizontalAlignment(SwingConstants.CENTER);
		lblSelectTable.setBounds(17, 10, 173, 21);
		contentPanel.add(lblSelectTable);
		
		selectTableCB = new JComboBox(tableName);
		selectTableCB.setBounds(17, 42, 173, 32);
		contentPanel.add(selectTableCB);
		
	    btnNewButton_dlt = new JButton("Delete");
		btnNewButton_dlt.setEnabled(false);
	
		btnNewButton_dlt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int dialogResult = JOptionPane
						.showConfirmDialog(
								AllExamsDetail.this,
								"Are you sure to delete OPD type",
								"Cancel", dialogButton);
				if (dialogResult == 0) {
					String statusSTR="1";
					selectedRowIndex = table.getSelectedRow();
					
					Object selectedObject1 = table.getModel()
							.getValueAt(selectedRowIndex, 0);
					
					String opdType_id = selectedObject1.toString();
					btnNewButton_dlt.setEnabled(false);
					//System.out.print(opdType_id+"ddddddddddddddddddd"+tableNameSTR);
				
					
				ExamDBConnection db = new ExamDBConnection();
				try {
					db.deleteRowExams(opdType_id,tableNameSTR);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();
				
				populateTable(exam_name);
				btnNewButton_dlt.setEnabled(false);
				}
				
			}
		});
		btnNewButton_dlt.setBounds(28, 445, 155, 37);
		contentPanel.add(btnNewButton_dlt);
		
		selectTableCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					tableNameSTR= selectTableCB.getSelectedItem().toString();
					getAllExamList(tableNameSTR);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		 try {
				getTabels() ;
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	public void populateTable(String examName) {
		try {
			TestMasterDBConnection db = new TestMasterDBConnection();
			ResultSet rs = db.retrieveAllData(tableNameSTR,examName);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			System.out.println("rows : " + NumberOfRows);
			System.out.println("columns : " + NumberOfColumns);
			rs = db.retrieveAllData(tableNameSTR,examName);

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
					new String[] { "Exam Code", "Exam Category", "Exam Name",
							"Lab Name", "Status" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.removeAll();
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setPreferredWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		TableColumnModel tcm = table.getColumnModel();
        TableColumn tm = tcm.getColumn(4);
        tm.setCellRenderer(new ColoredTableCellRenderer());
	}

	public void getTabels() throws SQLException
	{
		tableName.removeAllElements();
		TestMasterDBConnection db = new TestMasterDBConnection();
		ResultSet rs = db.retrieveALLMasterTable();

		
		while (rs.next()) {
			System.out.println(rs.getObject(1).toString());
			tableName.addElement(rs.getObject(1));
		}
		selectTableCB.setModel(tableName);
		selectTableCB.setSelectedIndex(0);
	}
	class ColoredTableCellRenderer extends DefaultTableCellRenderer {
		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean selected, boolean focused, int row,
				int column) {
			setEnabled(table == null || table.isEnabled()); // see question
															// above

			if (table.getValueAt(row, column).equals("No"))
				setBackground(Color.red);
			else
				setBackground(Color.green);

			super.getTableCellRendererComponent(table, value, selected,
					focused, row, column);

			return this;
		}
	}

	public void getAllExamList(String tableName) {
		exams.removeAllElements();
		exams.addElement("All Exams");
		ExamDBConnection dbConnection = new ExamDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllExams(tableName);
		try {
			while (resultSet.next()) {

				exams.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}
}
