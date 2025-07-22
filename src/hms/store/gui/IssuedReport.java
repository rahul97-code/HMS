package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.EditItem;
import hms1.ipd.gui.IPDBrowser;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
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
import javax.swing.table.TableCellRenderer;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextField;
import javax.swing.JComboBox;

public class IssuedReport extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField searchItemTF;
	Vector originalTableModel;
	private JComboBox departmentCB;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			IssuedReport dialog = new IssuedReport();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public IssuedReport() {
		setTitle("Issue Stock to Department");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IssuedReport.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 764, 578);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 76, 738, 453);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setToolTipText("Double Click to Issue Item");
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null }, },
						new String[] { "Item Id", "Item Name", "Item Desc.",
								"Expiry Date", "Item Stock","Request" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
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
							JTable target = (JTable) arg0.getSource();
							int row = target.getSelectedRow();
							int column = target.getSelectedColumn();
							// do some action
							Object id = table.getModel().getValueAt(row, 0);
							Object itemID = table.getModel().getValueAt(row, 1);
							Object itemName = table.getModel().getValueAt(row,
									2);
							Object deptName = table.getModel().getValueAt(row,
									3);
							Object requestedqty = null;
							if(table.getModel().getValueAt(row, 5) != null){
								 requestedqty = table.getModel().getValueAt(row, 5);
							}else{
								requestedqty=0;
							}
							
							
							String reqstqty="";
//							if(requestedqty.toString().equals("")){
//								reqstqty="0";
//							}else{
								reqstqty=requestedqty.toString();
//							}
							IssuedItem edititem = new IssuedItem(IssuedReport.this,id.toString(),itemID.toString(),itemName.toString(),reqstqty,deptName.toString());
							edititem.setModal(true);
							edititem.setVisible(true);
						
						}
					}
				});
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Request Pending Register");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 310, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(IssuedReport.class
					.getResource("/icons/stockicon.png")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		{
			searchItemTF = new JTextField();
			searchItemTF.setBounds(539, 38, 207, 20);
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
			lblSearch.setBounds(477, 40, 52, 17);
			contentPanel.add(lblSearch);
		}
		
		departmentCB = new JComboBox();
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(151, 40, 218, 25);
		contentPanel.add(departmentCB);
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				//searchItemTF.setText("");
				if (departmentName.getSize() > 0) {

if((departmentCB.getSelectedItem()
		.toString().equals("All Departments"))){
	populateExpensesTableAll();
}else{
	populateExpensesTable(departmentCB.getSelectedItem()
			.toString());
}
			            	
			            
				}
			}
		});
		JLabel label = new JLabel("Select Department");
		label.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label.setBounds(20, 40, 126, 25);
		contentPanel.add(label);
//		populateExpensesTable();
		getAllDepartments();
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
	public void populateExpensesTableAll() {

		try {
			InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
			ResultSet rs = invoiceDBConnection.retrieverequestedItemAll();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = invoiceDBConnection.retrieverequestedItemAll();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
//					Rows_Object_Array[R][6] = Button.class;
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {"Id","Item Id","Item Name", "Department Name", "Requested User",
							"Requested Qty", "Entered Date","Status"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(1).setMinWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(100);
			
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			 originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
//			    TableCellRenderer buttonRenderer = new JTableButtonRenderer();
//				table.getColumnModel().getColumn(6).setCellRenderer(buttonRenderer);
//				table.addMouseListener(new JTableButtonMouseListener(table));
//			 table.getColumnModel().getColumn(6).setCellRenderer(new CustomRendererButton());
		//	table.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer2());
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	public void populateExpensesTable(String departmentname) {

		try {
			InvoiceDBConnection invoiceDBConnection = new InvoiceDBConnection();
			ResultSet rs = invoiceDBConnection.retrieverequestedItem(departmentname);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = invoiceDBConnection.retrieverequestedItem(departmentname);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
//					Rows_Object_Array[R][6] = Button.class;
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {"Id","Item Id","Item Name", "Department Name", "Requested User",
							"Requested Qty", "Entered Date","Status"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(1).setMinWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(100);
			
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
			 originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
//			    TableCellRenderer buttonRenderer = new JTableButtonRenderer();
//				table.getColumnModel().getColumn(6).setCellRenderer(buttonRenderer);
//				table.addMouseListener(new JTableButtonMouseListener(table));
//			 table.getColumnModel().getColumn(6).setCellRenderer(new CustomRendererButton());
		//	table.getColumnModel().getColumn(3).setCellRenderer(new CustomRenderer2());
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
    class JTableButtonRenderer implements TableCellRenderer {       
        @Override public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            JButton button = (JButton)value;
//            button.setForeground(Color.BLACK);
            return button; 
        }
    }
	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
		departmentName.addElement("All Departments");
		try {
			while (resultSet.next()) {
				departmentName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		departmentCB.setModel(departmentName);
		if (i > 0) {
			departmentCB.setSelectedIndex(0);
		}
	}
    class JTableButtonMouseListener extends MouseAdapter {
        private final JTable table;

        public JTableButtonMouseListener(JTable table) {
            this.table = table;
        }

        public void mouseClicked(MouseEvent e) {
            int column = table.getColumnModel().getColumnIndexAtX(e.getX()); // get the coloum of the button
            int row    = e.getY()/table.getRowHeight(); //get the row of the button

                    //*Checking the row or column is valid or not
            if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
                Object value = table.getValueAt(row, column);
                if (value instanceof JButton) {
                    //perform a click event
                    ((JButton)value).doClick();
                }
            }
        }
    }
	class CustomRendererButton extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          JButton button = (JButton)value;
//	          cellComponent.setBackground(Color.RED);
	          cellComponent.addMouseListener((MouseListener) new JButton());
			  return cellComponent;
	       
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
