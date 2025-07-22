package hms.store.gui;

import hms.main.DateFormatChange;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms1.ipd.gui.IPDBrowser;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class RequestRegister extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	int selectedRowIndex;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			RequestRegister dialog = new RequestRegister();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public RequestRegister() {
		setTitle("Pills Request Register");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				RequestRegister.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 767, 363);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(10, 39, 735, 276);
			contentPanel.add(scrollPane);
			{

				table = new JTable();
				table.getTableHeader().setReorderingAllowed(false);
				table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
				table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				table.setModel(new DefaultTableModel(new Object[][] { { null,
					null, null, null, null }, },
						new String[] { "NO.", "Patient ID", "Patient Name",
								"Dept.", "Date","Type" }));
				table.getColumnModel().getColumn(1).setPreferredWidth(120);
				table.getColumnModel().getColumn(1).setMinWidth(120);
				table.getColumnModel().getColumn(2).setPreferredWidth(120);
				table.getColumnModel().getColumn(2).setMinWidth(120);
				table.getColumnModel().getColumn(3).setMinWidth(120);
				table.getColumnModel().getColumn(4).setPreferredWidth(120);
				table.getColumnModel().getColumn(4).setMinWidth(120);
				table.getColumnModel().getColumn(5).setPreferredWidth(160);
				table.getColumnModel().getColumn(5).setMinWidth(160);
				table.setFont(new Font("Tahoma", Font.BOLD, 14));
				
				table.addMouseListener(new MouseAdapter() {
					public void mousePressed(MouseEvent me) {
						JTable target =(JTable) me.getSource();
						int row = target.getSelectedRow();
						if (me.getClickCount() == 2) {
							Object ipd_id = table.getModel().getValueAt(row, 0);
							Object p_id = table.getModel().getValueAt(row, 1);
							Object type = table.getModel().getValueAt(row, 5);
							if(type.toString().toLowerCase().equals("n/a")) {
								IndoorPillsDetail indoorPillsDetail=new IndoorPillsDetail(ipd_id.toString(), p_id.toString(),type.toString());
								indoorPillsDetail.setModal(true);
								indoorPillsDetail.setVisible(true);
							}else {
								IndoorProcedureDetail indoorPillsDetail=new IndoorProcedureDetail(ipd_id.toString(), p_id.toString(),type.toString());
								indoorPillsDetail.setModal(true);
								indoorPillsDetail.setVisible(true);
							}
						}}
				});

				scrollPane.setViewportView(table);
			}
		}
		{
			JLabel lblDepartmentStock = new JLabel("Request Register");
			lblDepartmentStock.setFont(new Font("Tahoma", Font.BOLD, 17));
			lblDepartmentStock.setBounds(59, 0, 216, 34);
			contentPanel.add(lblDepartmentStock);
		}
		{
			JLabel label = new JLabel("");
			label.setIcon(new ImageIcon(RequestRegister.class
					.getResource("/icons/stockicon.png")));
			label.setBounds(10, 0, 52, 34);
			contentPanel.add(label);
		}
		{
			JButton btnNewButton = new JButton("Refresh");
			btnNewButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					populateExpensesTable();
				}
			});
			btnNewButton.setBounds(617, 2, 117, 25);
			contentPanel.add(btnNewButton);
		}
		
		final JButton btnNewButton = new JButton("Delete");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				
				String ipd_id=table.getValueAt(cur_selectedRow, 0)+"";
				String type=table.getValueAt(cur_selectedRow, 5)+"";

				System.out.println(ipd_id+""+type);
				
				IssuedItemsDBConnection db=new IssuedItemsDBConnection();
				try {
					db.deleteRequest(ipd_id, type);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				db.closeConnection();
				
				btnNewButton.setEnabled(false);
			}
		});
		
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")) {
							btnNewButton.setEnabled(true);
						}
						}
				});
		btnNewButton.setBounds(242, 6, 117, 25);
		contentPanel.add(btnNewButton);
		populateExpensesTable();
	}

	public void populateExpensesTable() {

		try {
			IssuedItemsDBConnection db = new IssuedItemsDBConnection();
			ResultSet rs = db.retrieveAllPendingPills();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
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
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {  "NO.", "Patien ID", "Patient Name",
							"Dept.", "Date","Type"  }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(120);
			table.getColumnModel().getColumn(1).setMinWidth(120);
			table.getColumnModel().getColumn(2).setPreferredWidth(120);
			table.getColumnModel().getColumn(2).setMinWidth(120);
			table.getColumnModel().getColumn(3).setMinWidth(120);
			table.getColumnModel().getColumn(4).setPreferredWidth(120);
			table.getColumnModel().getColumn(4).setMinWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(160);
			table.getColumnModel().getColumn(5).setMinWidth(160);


			table.setFont(new Font("Tahoma", Font.BOLD, 12));

		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	{
		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

			if(table.getValueAt(row, column).equals("With in level")){
				cellComponent.setBackground(Color.GREEN);
			} else{
				cellComponent.setBackground(Color.RED);

			}
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
				Date date1 = sdf.parse(DateFormatChange.StringToMysqlDate(new Date()));
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
}
