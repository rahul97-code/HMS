package hms1.ipd.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.TestMasterDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;
import hms.store.gui.StoreMain;
import hms1.ipd.database.PackageDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.DocumentException;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import com.toedter.calendar.JDateChooser;

public class CreateIPDPackage extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable examsTable;
	String tableNameSTR="exam_master";
	private JTextField packageNameTF;
	private JTextField amountTF;
	private JDateChooser expiryDateDC;
	String expiryDateSTR="";
	int NumberOfColumns = 0, NumberOfRows = 0;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			CreateIPDPackage dialog = new CreateIPDPackage();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public CreateIPDPackage() {
		setResizable(false);
		setTitle("Create Package");
		setBounds(100, 100, 886, 443);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		JPanel panel = new JPanel();
		panel.setBounds(10, 11, 865, 399);
		contentPanel.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(295, 11, 560, 377);
		panel.add(scrollPane);

		examsTable = new JTable();
		examsTable.getTableHeader().setReorderingAllowed(false);
		examsTable
				.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		examsTable
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		examsTable.setModel(new DefaultTableModel(new Object[][] { {
				null, null, null, null, ""}, }, new String[] { "Exam Code", "Exam Category", "Exam Name",
				"Lab Name",
				"Order Status"}) {
			boolean[] columnEditables = new boolean[] { false, false, false,
					false, false, true, true, false, false, false, false,
					false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		examsTable.getColumnModel().getColumn(0).setPreferredWidth(100);
		examsTable.getColumnModel().getColumn(0).setMinWidth(100);
		examsTable.getColumnModel().getColumn(1).setResizable(false);
		examsTable.getColumnModel().getColumn(1).setPreferredWidth(100);
		examsTable.getColumnModel().getColumn(1).setMinWidth(100);
		examsTable.getColumnModel().getColumn(2).setPreferredWidth(150);
		examsTable.getColumnModel().getColumn(2).setMinWidth(150);
		examsTable.getColumnModel().getColumn(3).setPreferredWidth(100);
		examsTable.getColumnModel().getColumn(3).setMinWidth(100);

		examsTable.setFont(new Font("Tahoma", Font.BOLD, 13));
		scrollPane.setViewportView(examsTable);
		
		JLabel lblNewPackage = new JLabel("New Package");
		lblNewPackage.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblNewPackage.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewPackage.setBounds(26, 18, 211, 28);
		panel.add(lblNewPackage);
		
		JLabel lblName = new JLabel("Name :");
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblName.setBounds(10, 57, 78, 21);
		panel.add(lblName);
		
		JLabel lblAmount = new JLabel("Amount :");
		lblAmount.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblAmount.setBounds(10, 98, 78, 27);
		panel.add(lblAmount);
		
		JLabel lblExpiry = new JLabel("Expiry :");
		lblExpiry.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblExpiry.setBounds(10, 141, 78, 27);
		panel.add(lblExpiry);
		
		packageNameTF = new JTextField();
		packageNameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		packageNameTF.setBounds(115, 54, 153, 27);
		panel.add(packageNameTF);
		packageNameTF.setColumns(10);
		
		amountTF = new JTextField();
		amountTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		amountTF.setColumns(10);
		amountTF.setBounds(115, 95, 153, 27);
		panel.add(amountTF);
		amountTF.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE))) {
                    e.consume();
                }
            }
        });
		
		expiryDateDC = new JDateChooser();
		expiryDateDC.setFont(new Font("Tahoma", Font.PLAIN, 14));
		expiryDateDC.setBounds(117, 141, 151, 27);
		panel.add(expiryDateDC);
		expiryDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							expiryDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							
						}
					}
				});
		
		expiryDateDC.setDateFormatString("yyyy-MM-dd");
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setBounds(71, 329, 158, 42);
		panel.add(btnCancel);
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		
		JButton btnCreate = new JButton("Create");
		btnCreate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(packageNameTF.getText().toString().equals(""))
				{
					JOptionPane
					.showMessageDialog(
							null,
							"Please input package name",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(amountTF.getText().toString().equals(""))
				{
					JOptionPane
					.showMessageDialog(
							null,
							"Please input amount",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(expiryDateSTR.equals(""))
				{
					JOptionPane
					.showMessageDialog(
							null,
							"Please input expiry date",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
					
				}
				boolean flag=false;
				EditableTableModel model1 = (EditableTableModel) examsTable
						.getModel();
				for (int count = 0; count < model1.getRowCount(); count++) {

					Boolean b = Boolean
							.valueOf(model1.getValueAt(count, NumberOfColumns).toString());
					if (b) {
						flag=true;
						break;
					}

				}
				if (!flag) {
					JOptionPane
					.showMessageDialog(
							null,
							"Please select atleast one exam",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				String[] data=new String[10];
				data[0]=packageNameTF.getText().toString();
				data[1]=amountTF.getText().toString();
				data[2]=expiryDateSTR;
				
				int packageID=0;
				PackageDBConnection connection=new PackageDBConnection();
				try {
					packageID=connection.inserData(data);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connection.closeConnection();
				insertExams(packageID+"");
			}
		});
		btnCreate.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnCreate.setBounds(71, 276, 158, 42);
		panel.add(btnCreate);
		populateExpensesTable();
	}

	public void populateExpensesTable() {

		try {
			TestMasterDBConnection db = new TestMasterDBConnection();
			ResultSet rs = db.retrieveAllDataOrderBy(tableNameSTR, "All Exams");

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
				NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllDataOrderBy(tableNameSTR, "All Exams");
			System.out.println(NumberOfColumns);
			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 1];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
				}
				Rows_Object_Array[R][NumberOfColumns ] = new Boolean(false);
				R++;
			}

			TableModel model1 = new EditableTableModel(new String[] {"Exam Code", "Exam Category", "Exam Name",
					"Lab Name",
					"Order Status" }, Rows_Object_Array);
			examsTable.setModel(model1);
			examsTable.getColumnModel().getColumn(0)
					.setPreferredWidth(50);
			examsTable.getColumnModel().getColumn(0).setMinWidth(50);
			examsTable.getColumnModel().getColumn(1)
					.setPreferredWidth(50);
			examsTable.getColumnModel().getColumn(1).setMinWidth(150);
			examsTable.getColumnModel().getColumn(2)
					.setPreferredWidth(150);
			examsTable.getColumnModel().getColumn(2).setMinWidth(150);
			examsTable.getColumnModel().getColumn(3)
					.setPreferredWidth(100);
			examsTable.getColumnModel().getColumn(3).setMinWidth(100);
			examsTable.getColumnModel().getColumn(4)
					.setPreferredWidth(90);
			examsTable.getColumnModel().getColumn(3).setMinWidth(100);
			
			examsTable.setFont(new Font("Tahoma", Font.BOLD, 13));
			examsTable.setFont(new Font("Tahoma", Font.BOLD, 13));

		} catch (SQLException ex) {

		}
	}


	public void insertExams(String packageID) {
		EditableTableModel model1 = (EditableTableModel) examsTable
				.getModel();
		PackageDBConnection connection=new PackageDBConnection();
		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean
					.valueOf(model1.getValueAt(count, NumberOfColumns).toString());
			if (b) {
				String examID= model1.getValueAt(count, 0).toString();
				connection.insertPackageExams(packageID, examID);
			}

		}
		connection.closeConnection();
	}
//

	class EditableTableModel extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel(String[] columnTitles, Object[][] dataEntries) {
			this.columnTitles = columnTitles;
			this.dataEntries = dataEntries;
		}

		public int getRowCount() {
			return dataEntries.length;
		}

		public int getColumnCount() {
			return columnTitles.length;
		}

		public Object getValueAt(int row, int column) {
			return dataEntries[row][column];
		}

		public String getColumnName(int column) {
			return columnTitles[column];
		}

		public Class getColumnClass(int column) {
			return getValueAt(0, column).getClass();
		}

		public boolean isCellEditable(int row, int column) {
			return true;
		}

		public void setValueAt(Object value, int row, int column) {
			dataEntries[row][column] = value;
		}
	}
	public JDateChooser getDateChooser() {
		return expiryDateDC;
	}
}
