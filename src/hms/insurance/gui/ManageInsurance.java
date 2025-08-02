package hms.insurance.gui;

import java.awt.EventQueue;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import hms.store.gui.StoreMain;

import javax.swing.CellEditor;
import javax.swing.JButton;
import javax.swing.JDialog;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Color;

public class ManageInsurance extends JDialog {

	private JFrame frame;
	private JTable table;
	Vector<String> insuID = new Vector<String>();
	Vector<String> insuNameV = new Vector<String>();
	int index=-1;
	private JButton btnUpdate;
	private JButton btnRefresh;
	private JButton btnDelete;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManageInsurance window = new ManageInsurance();
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Initialize the contents of the 
	 */
	public ManageInsurance(){

		setBounds(100, 100, 908, 377);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setModal(true);
		JScrollPane scrollPane = new JScrollPane();

		scrollPane.setBounds(174, 12, 722, 316);
		getContentPane().add(scrollPane);

		table = new JTable();
		table.setModel(new DefaultTableModel(new Object[][] {
			{ null, null, null, null, null , null, null   }, { null, null, null, null, null , null, null   }, },
				new String[] { "ID","Insuracne Name", "Insurance Detail", "Rate Percentage",
						"Rate Type","Mrp Reqd","Cash Reqd" }));
		table.setToolTipText("double click to edit cell");
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(StoreMain.update_item_access.equals("1") || StoreMain.access.equals("1"))
				{
//				btnUpdate.setEnabled(true);
//				btnDelete.setEnabled(true);
				}
				
				index=-1;
				index=table.getSelectedRow();

			}
		});
		scrollPane.setViewportView(table);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel.setBounds(32, 291, 117, 25);
		getContentPane().add(btnCancel);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(index==-1) {
					JOptionPane.showMessageDialog(null, "Please select Insurance!");
					return;
				}
				CellEditor cellEditor = table.getCellEditor();
				if (cellEditor != null)
					cellEditor.stopCellEditing();
				
				InsuranceDBConnection db = new InsuranceDBConnection();
				String[] data=new String[7];
				data[0]=table.getValueAt(index, 0)+"";
				data[1]=table.getValueAt(index, 1)+"";
				data[2]=table.getValueAt(index, 2)+"";
				data[3]=table.getValueAt(index, 3)+"";
				data[4]=table.getValueAt(index, 4)+"";
				data[5]=table.getValueAt(index, 5).toString().equals("true")?"1":"0";
				data[6]=table.getValueAt(index, 6).toString().equals("true")?"1":"0";


				db.UpdateInsuranceDATA(data);

				db.closeConnection();
				
				JOptionPane.showMessageDialog(null,"Data Saved successfully!");
				populateTable();
				index=-1;
			}
		});
		btnUpdate.setEnabled(false);
		btnUpdate.setBounds(32, 106, 117, 25);
		getContentPane().add(btnUpdate);
		
		btnRefresh = new JButton("Refresh");
		btnRefresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateTable();
			}
		});
		btnRefresh.setBounds(32, 144, 117, 25);
		getContentPane().add(btnRefresh);
		
		JButton btnNewInsurance = new JButton("New Insurance");
		btnNewInsurance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddInsuranceType addInsuranceType = new AddInsuranceType();
				addInsuranceType.setVisible(true);	
				btnRefresh.doClick();
			}
		});
		btnNewInsurance.setBounds(12, 22, 150, 25);
		getContentPane().add(btnNewInsurance);
		
	    btnDelete = new JButton("Delete");
		btnDelete.setEnabled(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				if(index==-1) {
					JOptionPane.showMessageDialog(null, "Please select Insurance!");
					return;
				}
				
				int dialogButton = JOptionPane.YES_NO_OPTION;
				int i=JOptionPane.showConfirmDialog (null, "Do you want to delete this Insurance?","Warning",dialogButton);

			    if (i == JOptionPane.YES_OPTION) { 
			    	InsuranceDBConnection db = new InsuranceDBConnection();
			    	String id=table.getValueAt(index, 0)+"";
			    	db.DeleteInsurance(id);
			    	JOptionPane.showMessageDialog(null,"Insurance deleted successfully!");
					populateTable();
					db.closeConnection();
					index=-1;
			    }
			}
		});
		btnDelete.setForeground(Color.RED);
		btnDelete.setBounds(32, 69, 117, 25);
		getContentPane().add(btnDelete);
		populateTable();
	}
	public void populateTable() {
		try {
			InsuranceDBConnection db = new InsuranceDBConnection();
			ResultSet rs = db.retrieveAllData();

			System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs.beforeFirst();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs.getObject(C);
					// System.out.println("Table: " + rs.getObject(C));
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {"ID","Insuracne Name", "Insurance Detail", "Rate Percentage",
							"Rate Type","Mrp Reqd","Cash Reqd"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if(column==0) {
						return false;
					}else {
						return true;// This causes all cells to be not editable
					}
				}
				@Override
				public Class getColumnClass(int column) {
					return getValueAt(0, column).getClass();
				}
			};

			table.setModel(model);
			DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
			centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
			table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
			table.getColumnModel().getColumn(0).setPreferredWidth(100);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(100);


			db.closeConnection();
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

	}
}
