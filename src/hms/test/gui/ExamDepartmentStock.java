package hms.test.gui;

import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms1.ipd.gui.IPDBrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;

public class ExamDepartmentStock extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField searchItemTF;
	Vector originalTableModel;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExamDepartmentStock dialog = new ExamDepartmentStock();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExamDepartmentStock() {
		setTitle("Department Stock Register");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ExamDepartmentStock.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 764, 578);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 738, 490);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, },
						new String[] { "Item Id", "Item Name", "Item Desc.",
								"Expiry Date", "Item Stock" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Department Stock");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 310, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(ExamDepartmentStock.class
					.getResource("/icons/stockicon.png")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		{
			searchItemTF = new JTextField();
			searchItemTF.setBounds(541, 8, 207, 20);
			contentPanel.add(searchItemTF);
			searchItemTF.setColumns(10);
			searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					String str = searchItemTF.getText() + "";
					searchTableContents(str);
				}

				@Override
				public void removeUpdate(DocumentEvent e) {
					String str = searchItemTF.getText() + "";
					searchTableContents(str);
				}

				@Override
				public void changedUpdate(DocumentEvent e) {
					String str = searchItemTF.getText() + "";
					searchTableContents(str);
				}
			});
		}
		{
			JLabel lblSearch = new JLabel("Search");
			lblSearch.setBounds(479, 11, 52, 17);
			contentPanel.add(lblSearch);
		}
		populateExpensesTable();
	}
	public void searchTableContents(String searchString) {
	    DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
	    //To empty the table before search
	    currtableModel.setRowCount(0);
	    //To search for contents from original table content
	    for (Object rows : originalTableModel) {
	        Vector rowVector = (Vector) rows;
	        for (Object column : rowVector) {
	            if (column.toString().contains(searchString)) {
	                //content found so adding to table
	                currtableModel.addRow(rowVector);
	                break;
	            }
	        }

	    }
	}
	public void populateExpensesTable() {

		try {
			DepartmentStockDBConnection db = new DepartmentStockDBConnection();
			ResultSet rs = db.retrieveStock(Test.dept_name);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveStock(Test.dept_name);

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
					new String[] { "Item Id","Item Code", "Item Name", "Item Desc.",
							"Expiry Date", "Item Stock" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(180);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			 originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
			   
		//	table.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer2());
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	class CustomRenderer2 extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          try{
	        	 
	      		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	          	Date date1 = sdf.parse(DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),3)));
	          	Date date2 = sdf.parse(table.getValueAt(row, column).toString());
	   
	          	System.out.println(sdf.format(date1));
	          	System.out.println(sdf.format(date2));
	   
	          	if(date1.compareTo(date2)>0){
	          		System.out.println("Date1 is after Date2");
	          		 cellComponent.setBackground(Color.RED);
	          	}else if(date1.compareTo(date2)<0){
	          		System.out.println("Date1 is before Date2");
	          		 cellComponent.setBackground(Color.GREEN);
	          	}else if(date1.compareTo(date2)==0){
	          		System.out.println("Date1 is equal to Date2");
	          		 cellComponent.setBackground(Color.PINK);
	          	}else{
	          		System.out.println("How to get here?");
	          	}
	   
	      	}catch(ParseException ex){
	      		ex.printStackTrace();
	      	}
	          return cellComponent;
	      }
	  }
	public JTextField getSearchItemTF() {
		return searchItemTF;
	}
}
