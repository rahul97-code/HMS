package hms.store.gui;

import hms.admin.gui.AdminMain;
import hms.departments.gui.DepartmentItemProfile.CustomRenderer;
import hms.store.database.ItemsDBConnection;

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

public class BatchBrowser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	int selectedRowIndex;
	Vector originalTableModel;
	private JLabel Item_id_value;
	private JButton btnNewButton;
	private JTextField BatchTotalTF;
	private JTextField TotalTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			StoreMain.access="1";
			BatchBrowser dialog = new BatchBrowser("1", "");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BatchBrowser(final String itemid, String itemName) {
		setTitle("Batch Detail");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(ItemBrowser.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 1060, 536);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 1035, 402);
			contentPanel.add(scrollPane);
			{
				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null, null, null, null, null }, },
						new String[] { "Id", "Batch Name", "Qty Entered", "Qty Remaining", "Entry Date", "MRP",
								"Pack Size", "Purchase Price" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(180);
				table.getColumnModel().getColumn(1).setMinWidth(150);
				table.getColumnModel().getColumn(2).setPreferredWidth(180);
				table.getColumnModel().getColumn(2).setMinWidth(150);
				table.getColumnModel().getColumn(3).setMinWidth(150);
				table.getColumnModel().getColumn(4).setPreferredWidth(150);
				table.getColumnModel().getColumn(4).setMinWidth(100);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = table.getSelectedRow();
						selectedRowIndex = table.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = table.getSelectedColumn();
						System.out.println("row-" + selectedRowIndex);
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
						if (arg0.getClickCount() == 2) {
							Object id = table.getModel().getValueAt(selectedRowIndex, 1);
							Object Prestock = table.getModel().getValueAt(selectedRowIndex, 4);
							Object remark = table.getModel().getValueAt(selectedRowIndex, 0);
							Object exp = table.getModel().getValueAt(selectedRowIndex, 5);
							Object unitprice = table.getModel().getValueAt(selectedRowIndex, 9);
							Object mrp = table.getModel().getValueAt(selectedRowIndex, 7);
							Object tax1 = table.getModel().getValueAt(selectedRowIndex, 11);
							Object sur = table.getModel().getValueAt(selectedRowIndex, 12);
							Object measunit = table.getModel().getValueAt(selectedRowIndex, 8);
							Object batch = table.getModel().getValueAt(selectedRowIndex, 2);
							Object changeunitPrice = table.getModel().getValueAt(selectedRowIndex, 13);
							Object billdate = table.getModel().getValueAt(selectedRowIndex, 14);

							if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
								AddBatch window = new AddBatch(itemid, BatchBrowser.this, Prestock + "", remark + "",
										exp + "", unitprice + "", mrp + "", tax1 + "", sur + "", measunit + "",
										batch + "", true, id + "",changeunitPrice+"",billdate+"");
								window.setVisible(true);
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
			lblDepartmentStock.setBounds(59, 0, 115, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(ItemBrowser.class.getResource("/icons/restore.gif")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}

		Item_id_value = new JLabel("");
		Item_id_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_id_value.setBounds(143, 0, 115, 34);
		contentPanel.add(Item_id_value);
		Item_id_value.setText(itemid);

		JLabel lblItemName = new JLabel("Item Name:");
		lblItemName.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblItemName.setBounds(278, 0, 115, 34);
		contentPanel.add(lblItemName);

		JLabel Item_name_value = new JLabel("<dynamic>");
		Item_name_value.setFont(new Font("Tahoma", Font.BOLD, 17));
		Item_name_value.setBounds(394, 0, 212, 34);
		contentPanel.add(Item_name_value);
		Item_name_value.setText(itemName);

		JLabel lblBatchTotal = new JLabel("Batch Total :");
		lblBatchTotal.setBounds(450, 461, 115, 15);
		contentPanel.add(lblBatchTotal);

		BatchTotalTF = new JTextField();
		BatchTotalTF.setEditable(false);
		BatchTotalTF.setBounds(562, 453, 114, 25);
		contentPanel.add(BatchTotalTF);
		BatchTotalTF.setColumns(10);

		TotalTF = new JTextField();
		TotalTF.setEditable(false);
		TotalTF.setColumns(10);
		TotalTF.setBounds(789, 453, 114, 25);
		contentPanel.add(TotalTF);

		TotalTF.setText(getTotalStock(itemid));
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			TotalTF.setEditable(true);
		}

		JLabel lblItemTotal = new JLabel("Item Total :");
		lblItemTotal.setBounds(694, 459, 115, 15);
		contentPanel.add(lblItemTotal);

		btnNewButton = new JButton("Add New Batch");
		btnNewButton.setBounds(20, 453, 140, 25);
		contentPanel.add(btnNewButton);
		btnNewButton.setEnabled(false);

		JButton btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ItemsDBConnection db = new ItemsDBConnection();
				try {
					db.updateStockOnly(itemid, TotalTF.getText() + "");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();
				JOptionPane.showMessageDialog(null, "Data Updated Successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
		btnUpdate.setBounds(940, 453, 105, 25);
		contentPanel.add(btnUpdate);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddBatch window = new AddBatch(itemid, BatchBrowser.this, "", "", "", "", "", "", "", "", "", false,
						"","","");
				window.setVisible(true);
				// add batch class
			}
		});
		if (StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
			btnNewButton.setEnabled(true);
			btnUpdate.setEnabled(true);
		}

		populateExpensesTable(itemid);
	}

	private String getTotalStock(String itemid) {
		// TODO Auto-generated method stub
		ItemsDBConnection db = new ItemsDBConnection();
		ResultSet rs = db.retrieveStock(itemid);
		int s = 0;
		try {
			while (rs.next()) {
				s = rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return s + "";
	}

	// public class CustomRenderer extends DefaultTableCellRenderer {
	// @Override
	// public Component getTableCellRendererComponent(JTable table,
	// Object value, boolean isSelected, boolean hasFocus, int row,
	// int column) {
	// Component cellComponent = super.getTableCellRendererComponent(
	// table, value, isSelected, hasFocus, row, column);
	// if (Double.parseDouble(table.getValueAt(row,
	// column).toString())>Double.parseDouble(table.getValueAt(row, 6).toString()))
	// {
	// cellComponent.setBackground(Color.GREEN);
	// } else {
	// cellComponent.setBackground(Color.RED);
	// }
	// return cellComponent;
	// }
	// }
	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		// To empty the table before search
		currtableModel.setRowCount(0);
		// To search for contents from original table content
		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			for (Object column : rowVector) {
				if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
					// content found so adding to table
					currtableModel.addRow(rowVector);
					break;
				}
			}
		}
	}

	public void populateExpensesTable(String item_id) {
		BatchTotalTF.setText("0");
		try {
			ItemsDBConnection db = new ItemsDBConnection();
			ResultSet rs = db.retrievetAllBatches(item_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();
			;

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 1];

			int R = 0, t = 0;
			while (rs.next()) {

				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C) == null ? "N/A" : rs.getObject(C);
				}
				t += rs.getInt(5);
				R++;
			}
			BatchTotalTF.setText("" + t);
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Remark", "Id", "Batch Name", "Qty Entered", "Qty Remaining", "Expiry Date",
							"Entry Date", "MRP", "Pack Size", "On Bill Purchase Price", "Discounted Purchase Price",
							"Tax", "Surcharge", "Changed Unit Price","Bill Date" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();

			table.getColumnModel().getColumn(1).setPreferredWidth(80);
			table.getColumnModel().getColumn(1).setMinWidth(80);
			table.getColumnModel().getColumn(2).setPreferredWidth(150);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(3).setMinWidth(100);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(100);
			table.getColumnModel().getColumn(6).setPreferredWidth(150);
			table.getColumnModel().getColumn(6).setMinWidth(150);
			table.getColumnModel().getColumn(7).setPreferredWidth(80);
			table.getColumnModel().getColumn(7).setMinWidth(80);
			table.getColumnModel().getColumn(14).setPreferredWidth(100);
			table.getColumnModel().getColumn(14).setMinWidth(100);
			// table.setDefaultRenderer(Object.class, new RedYellowRenderer00());
			// table.getColumnModel().getColumn(8).setPreferredWidth(150);
			// table.getColumnModel().getColumn(8).setMinWidth(150);
			table.setFont(new Font("Tahoma", Font.BOLD, 12));

		} catch (SQLException ex) {

		}

	}

	class RedYellowRenderer00 extends DefaultTableCellRenderer {
		Color Purple = new Color(102, 0, 153);
		Color c2 = new Color(255, 204, 30);

		RedYellowRenderer00() {
			setHorizontalAlignment(CENTER);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {
			Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			// System.out.print(table.getValueAt(row, 5)+" arun ");
			if (!table.getValueAt(row, 0).equals("N/A") && table.getRowCount() > 0) {
				c.setBackground(Purple);
				c.setForeground(Color.white);
			}

			return c;
		}
	}

}
