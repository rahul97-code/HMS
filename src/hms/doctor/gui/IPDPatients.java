package hms.doctor.gui;

import hms.opd.gui.OPDBrowser;

import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.DischargeSummary;


import java.awt.BorderLayout;
import java.awt.Component;

import javafx.application.Application;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JScrollPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;

import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JTextPane;

public class IPDPatients extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JLabel totalIPD;
	private JLabel userName;
	public String search = "";
	 private TableRowSorter sorter;
	 String doctorname="";

	public static void main(String[] arg)
	{
		IPDPatients billBrowser=new IPDPatients("");
		billBrowser.setVisible(true);
		
	}
	/**
	 * Create the dialog.
	 */
	public IPDPatients(String DoctorName) {
		this.doctorname=DoctorName;
		setResizable(false);
		setTitle("IPD Patients");
		setIconImage(Toolkit.getDefaultToolkit().getImage(IPDPatients.class.getResource("/icons/smallLogo.png")));
		setBounds(200, 200, 844, 465);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 69, 809, 342);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(new Object[][] {
				{ null, null, null, null, null, null, null, null },
				{ null, null, null, null, null, null, "", null }, },
				new String[] { "IPD No", "Patient ID", "Patient Name", "Ward",
						"Bed", "Entry Date", "Last Visit", "Next Visit", "Button"}));
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(100);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(80);
		table.getColumnModel().getColumn(5).setMinWidth(80);
		table.getColumnModel().getColumn(6).setPreferredWidth(80);
		table.getColumnModel().getColumn(6).setMinWidth(150);
		table.getColumnModel().getColumn(7).setPreferredWidth(80);
		table.getColumnModel().getColumn(7).setMinWidth(150);
//		
//		table.getColumnModel().getColumn(8).setMinWidth(170);
//		table.getColumnModel().getColumn(9).setMinWidth(170);
//		table.getColumnModel().getColumn(9)
//				.setCellRenderer(new JTableButtonRenderer());
//		table.addMouseListener(new JTableButtonMouseListener(
//				table));
		scrollPane.setViewportView(table);

		JLabel lblUserName = new JLabel("User Name :");
		lblUserName.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblUserName.setBounds(22, 11, 152, 20);
		contentPanel.add(lblUserName);

		userName = new JLabel("New label");
		userName.setFont(new Font("Tahoma", Font.BOLD, 16));
		userName.setBounds(200, 11, 181, 20);
		contentPanel.add(userName);

		JLabel lblTotalIpd = new JLabel("Total IPD :");
		lblTotalIpd.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblTotalIpd.setBounds(20, 38, 154, 20);
		contentPanel.add(lblTotalIpd);

		totalIPD = new JLabel("New label");
		totalIPD.setFont(new Font("Tahoma", Font.BOLD, 16));
		totalIPD.setBounds(200, 36, 268, 20);
		contentPanel.add(totalIPD);
		
		JLabel lblSearch = new JLabel("Search IPD ID");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblSearch.setBounds(406, 11, 152, 20);
		contentPanel.add(lblSearch);
		
		final JTextField searchfield = new JTextField();
		searchfield.setBounds(539, 11, 176, 20);
		contentPanel.add(searchfield);
//		searchfield.getDocument().addDocumentListener(new DocumentListener() {
//	         @Override
//	         public void insertUpdate(DocumentEvent e) {
//	            search(searchfield.getText());
//	         }
//	         @Override
//	         public void removeUpdate(DocumentEvent e) {
//	            search(searchfield.getText());
//	         }
//	         @Override
//	         public void changedUpdate(DocumentEvent e) {
//	            search(searchfield.getText());
//	         }
//	         public void search(String str) {
//	            if (str.length() == 0) {
//	               sorter.setRowFilter(null);
//	            } else {
//	               sorter.setRowFilter(RowFilter.regexFilter(str));
//	            }
//	         }
//	      });
//		
		
		searchfield.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchfield.getText() + "";
				System.out.print(str);
				if (!str.equals("")) {
					search = str;
					populateIPDHistoryTable1(doctorname);
				} else {
					populateIPDHistoryTable(doctorname);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchfield.getText() + "";
				System.out.print(str);
				if (!str.equals("")) {
					search = str;
					populateIPDHistoryTable1(doctorname);
				} else {
					populateIPDHistoryTable(doctorname);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchfield.getText() + "";
				System.out.print(str);
				if (!str.equals("")) {
					search = str;
					populateIPDHistoryTable1(doctorname);
				} else {
					populateIPDHistoryTable(doctorname);
				}
			}
		});
		populateIPDHistoryTable(doctorname);
	}
	public void populateIPDHistoryTable1(String doctorName) {

		int NumberOfColumns = 0, NumberOfRows = 0;
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataIPDSearch(search);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			ResultSet	rs1 = db.retrieveAllDataIPDSearch(search);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
			System.out.println(NumberOfRows + "ddddd");
			int R = 0;
			while (rs1.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs1.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "IPD No", "Patient ID", "Patient Name",
							"Ward", "Bed", "Entry Date", "Last Visit",
							"Next Visit" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setMinWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(80);
			table.getColumnModel().getColumn(5).setMinWidth(80);
			table.getColumnModel().getColumn(6).setPreferredWidth(80);
			table.getColumnModel().getColumn(6).setMinWidth(150);
			table.getColumnModel().getColumn(7).setPreferredWidth(80);
			table.getColumnModel().getColumn(7).setMinWidth(150);
//			table.getColumnModel().getColumn(8).setMinWidth(170);
//
//			table.getColumnModel().getColumn(8)
//					.setCellRenderer(new JTableButtonRenderer());
//			table.addMouseListener(new JTableButtonMouseListener(
//					table));
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		userName.setText(""+doctorName);

		totalIPD.setText(""+NumberOfRows);
	}
	public void populateIPDHistoryTable(String doctorName) {

		int NumberOfColumns = 0, NumberOfRows = 0;
		try {
			IPDDBConnection db = new IPDDBConnection();
			ResultSet rs = db.retrieveAllDataIPD();

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			ResultSet	rs1 = db.retrieveAllDataIPD();

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
			System.out.println(NumberOfRows + "ddddd");
			int R = 0;
			while (rs1.next()) {
				for (int C = 1; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 1] = rs1.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "IPD No", "Patient ID", "Patient Name",
							"Ward", "Bed", "Entry Date", "Last Visit",
							"Next Visit"}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(100);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(100);
			table.getColumnModel().getColumn(3).setPreferredWidth(120);
			table.getColumnModel().getColumn(3).setMinWidth(120);
			table.getColumnModel().getColumn(5).setPreferredWidth(80);
			table.getColumnModel().getColumn(5).setMinWidth(80);
			table.getColumnModel().getColumn(6).setPreferredWidth(80);
			table.getColumnModel().getColumn(6).setMinWidth(150);
			table.getColumnModel().getColumn(7).setPreferredWidth(80);
			table.getColumnModel().getColumn(7).setMinWidth(150);
//			table.getColumnModel().getColumn(8).setMinWidth(170);

//			table.getColumnModel().getColumn(8)
//					.setCellRenderer(new JTableButtonRenderer());
//			table.addMouseListener(new JTableButtonMouseListener(
//					table));
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		userName.setText(""+doctorName);

		totalIPD.setText(""+NumberOfRows);
	}
	private static class JTableButtonRenderer implements TableCellRenderer {
	@Override
	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		JButton button = new JButton("Doctor Summary");
		if (isSelected) {
			button.setForeground(table.getSelectionForeground());
			button.setBackground(table.getSelectionBackground());
		} else {
			button.setForeground(table.getForeground());
			button.setBackground(UIManager.getColor("Button.background"));
		}
		return button;
	}
}
private static class JTableButtonMouseListener extends MouseAdapter {
	private final JTable table;

	public JTableButtonMouseListener(JTable table) {
		this.table = table;
	}

	public void mouseClicked(MouseEvent e) {
		int column = table.getColumnModel().getColumnIndexAtX(e.getX());
		int row = e.getY() / table.getRowHeight();

		// String ipdentry1 = "", patientid = "", patientname = "";
		String[] arg = new String[5];

		arg[0] = table.getValueAt(row, 0).toString();
		arg[1] = table.getValueAt(row, 1).toString();
		arg[2] = table.getValueAt(row, 2).toString();
		Application.launch(DischargeSummary.class, arg);
		System.exit(0);

		// if (row < table.getRowCount() && row >= 0 && column <
		// table.getColumnCount() && column >= 0)
		// {
		// Object value = table.getValueAt(row, column);
		// System.out.println(value);
		// if (value instanceof JButton)
		// {
		// ((JButton)value).doClick();
		// }
		// }
	}
}
}
