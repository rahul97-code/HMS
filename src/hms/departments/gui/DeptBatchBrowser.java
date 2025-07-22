package hms.departments.gui;

import hms.admin.gui.AdminMain;
import hms.departments.database.DepartmentStockDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.StoreMain;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JTextField;

public class DeptBatchBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	int selectedRowIndex;
	Vector originalTableModel;
	private JLabel Item_id_value;
	private JButton btnStockZero;
	private Vector<String> SelectedBatchIDV=new Vector<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			DeptBatchBrowser dialog = new DeptBatchBrowser("1","ader","EMERGENCY WARD");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Create the dialog.
	 */
	public DeptBatchBrowser(final String itemid,final String itemName,final String dept) {
		setTitle("Batch Detail");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				DepartmentItemProfile.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 713, 536);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 692, 444);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null,null }, },
						new String[] { "Id","Batch ID","Batch Name","Expiry Date",  "Qty Remaining","Select" }));
				table.getColumnModel().getColumn(0).setMinWidth(80);
				table.getColumnModel().getColumn(0).setPreferredWidth(80);
				table.getColumnModel().getColumn(1).setPreferredWidth(80);
				table.getColumnModel().getColumn(1).setMinWidth(80);
				table.getColumnModel().getColumn(2).setPreferredWidth(150);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(180);
			
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				table.getSelectionModel().addListSelectionListener(
						new ListSelectionListener() {
							@Override
							public void valueChanged(ListSelectionEvent e) {
								// TODO Auto-generated method stub
								selectedRowIndex = table.getSelectedRow();
								selectedRowIndex = table
										.convertRowIndexToModel(selectedRowIndex);
								int selectedColumnIndex = table
										.getSelectedColumn();
							
							}
						});
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
						if (arg0.getClickCount() == 1) {
							SelectedBatchIDV.removeAllElements();
							for(int i=0;i<table.getRowCount();i++) {
								if(Boolean.parseBoolean(table.getModel().getValueAt(i, 5)+"")) {
									SelectedBatchIDV.add(table.getValueAt(i, 0)+"");	
								}
							}	
							
						}
						if (arg0.getClickCount() == 2) {
							if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
							String id = table.getModel().getValueAt(selectedRowIndex, 0)+"";
							String batch = table.getModel().getValueAt(selectedRowIndex, 2)+"";
							String batchid = table.getModel().getValueAt(selectedRowIndex, 1)+"";
							String exp = table.getModel().getValueAt(selectedRowIndex, 3)+"";
							String stock = table.getModel().getValueAt(selectedRowIndex, 4)+"";
							EdititemBatch b=new EdititemBatch(DeptBatchBrowser.this,itemName,id,batch,batchid,exp,stock,dept,itemid);
							b.setModal(true);
							b.setVisible(true);
									
							}
							
						}
						
					}
				});
				
				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Item Id:");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(10, 0, 115, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			//label.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/restore.gif")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		
		Item_id_value = new JLabel("");
		Item_id_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_id_value.setBounds(94, 0, 115, 34);
		contentPanel.add(Item_id_value);
		Item_id_value.setText(itemid);
		
		JLabel lblItemName = new JLabel("Item Name:");
		lblItemName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblItemName.setBounds(229, 0, 115, 34);
		contentPanel.add(lblItemName);
		
		JLabel Item_name_value = new JLabel("<dynamic>");
		Item_name_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_name_value.setBounds(345, 0, 212, 34);
		contentPanel.add(Item_name_value);
		Item_name_value.setText(itemName);
		
		btnStockZero = new JButton("ZERO STOCK");
		btnStockZero.setEnabled(true);
		btnStockZero.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(SelectedBatchIDV.size()<1) {	
						JOptionPane.showMessageDialog(null, "Please select items",
								"Input Error", JOptionPane.ERROR_MESSAGE);
						return;
				}
				 int result = JOptionPane.showConfirmDialog(null,"Sure? To Zero Stock?", "Confirmation",
			               JOptionPane.YES_NO_OPTION,
			               JOptionPane.QUESTION_MESSAGE);
			            if(result == JOptionPane.YES_OPTION){
			              
				DepartmentStockDBConnection connection=new DepartmentStockDBConnection();
				for(int i=0;i<SelectedBatchIDV.size();i++) {
					try {
						connection.DeptSetZeroStock(SelectedBatchIDV.get(i));
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				connection.closeConnection();
					JOptionPane.showMessageDialog(null, "Data Updated Successfully",
							"Edit Data", JOptionPane.INFORMATION_MESSAGE);		
					populateExpensesTable(itemid,dept);
		
			}
			}
		});
		btnStockZero.setBounds(518, 12, 133, 22);
		contentPanel.add(btnStockZero);
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			btnStockZero.setEnabled(true);
		}

		
	
		populateExpensesTable(itemid,dept);
	}
	
	public void populateExpensesTable(String item_id,String dept) {
		SelectedBatchIDV.removeAllElements();
		try {
			DepartmentStockDBConnection db = new DepartmentStockDBConnection();
			ResultSet rs = db.retrievetAllBatches1(item_id,dept);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrievetAllBatches1(item_id,dept);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];

			int R = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C).toString();
					Rows_Object_Array[R][NumberOfColumns]=new Boolean(false);
				}
				
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {  "Id","Batch ID","Batch Name","Expiry Date",  "Qty Remaining","Select" 
							 }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if (column == 5) {
						return true;
					} else {
						return false;
					}
				}
				@Override
				public Class getColumnClass(int column) {
					return getValueAt(0, column).getClass();
				}
				
			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			table.getColumnModel().getColumn(0).setMinWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(70);
			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(1).setMinWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(150);
			table.getColumnModel().getColumn(4).setMinWidth(150);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			
//			table.getColumnModel().getColumn(8).setPreferredWidth(150);
//			table.getColumnModel().getColumn(8).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));
//			table.getColumnModel().getColumn(6).setCellRenderer(new CustomRenderer());
		} catch (SQLException ex) {
			
		}

	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	  {
	      @Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	      {
	          Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

	          if(table.getValueAt(row, column).equals("Yes")){
	              cellComponent.setBackground(Color.GREEN);
	          } else{
	        	  cellComponent.setBackground(Color.RED);
	        	 
	          }
	          return cellComponent;
	      }
	  }
}
