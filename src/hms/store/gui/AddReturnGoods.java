package hms.store.gui;

import hms.store.database.InvoiceDBConnection;
import hms.store.database.ReturnInvoiceDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class AddReturnGoods extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchTF;
	private JTextField mobileTF;
	private JTextField addressTF;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	private JComboBox suplierCB;
	String supplierDisplaySTR, mobileSTR, addressSTR, supplierID,
			supplierNameSTR;
	private JTable table;
	double totalAmount = 0, amountAdjustmentDouble = 0,finalAmount=0;
	int paymetID = 0;
	Vector<String> itemID = new Vector<String>();

	/**
	 * Create the dialog.
	 */
	public AddReturnGoods(String supplierNameSTR,final NewInvoice invoice) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AddReturnGoods.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setTitle("Supplier Payment");
		setBounds(100, 100, 975, 463);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JLabel lblSupplier = new JLabel("Supplier :");
		lblSupplier.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSupplier.setBounds(35, 11, 126, 25);
		contentPanel.add(lblSupplier);

		searchTF = new JTextField();
		searchTF.setEnabled(false);
		searchTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		searchTF.setColumns(10);
		searchTF.setBounds(171, 11, 218, 25);
		contentPanel.add(searchTF);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {

					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchTF.getText() + "";
				if (!str.equals("")) {
					getSupplierName(str);
				} else {
					addressTF.setText("");
					mobileTF.setText("");
					supplierName.removeAllElements();
					suplierCB.setModel(supplierName);
				}
			}
		});

		suplierCB = new JComboBox();
		suplierCB.setEnabled(false);
		suplierCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		suplierCB.setBounds(171, 44, 218, 25);
		contentPanel.add(suplierCB);
		suplierCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					supplierDisplaySTR = suplierCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				addressTF.setText("");
				mobileTF.setText("");
				table.setModel(new DefaultTableModel(new Object[][] { { null,
						null, null, null, null, null, null }, }, new String[] {
						"ID", "Invoice No.", "Order No.", "Tax", "Amount",
						"Date", "Select" }) {
					boolean[] columnEditables = new boolean[] { false, false,
							false, false, false, false, true };

					public boolean isCellEditable(int row, int column) {
						return columnEditables[column];
					}
				});
				totalAmount = 0;
				getSupplierDetail(supplierDisplaySTR);
				if (supplierName.getSize() > 0) {
					addressTF.setText("" + addressSTR);
					mobileTF.setText("" + mobileSTR);
					populateTable();
				}
			}
		});

		JLabel lblSupplier_1 = new JLabel("Supplier :");
		lblSupplier_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSupplier_1.setBounds(35, 44, 126, 25);
		contentPanel.add(lblSupplier_1);

		JLabel label_2 = new JLabel("Mobile :");
		label_2.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_2.setBounds(418, 8, 126, 25);
		contentPanel.add(label_2);

		mobileTF = new JTextField();
		mobileTF.setEditable(false);
		mobileTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		mobileTF.setColumns(10);
		mobileTF.setBounds(554, 8, 218, 25);
		contentPanel.add(mobileTF);

		addressTF = new JTextField();
		addressTF.setEditable(false);
		addressTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addressTF.setColumns(10);
		addressTF.setBounds(554, 44, 218, 25);
		contentPanel.add(addressTF);

		JLabel label_3 = new JLabel("Address :");
		label_3.setFont(new Font("Tahoma", Font.PLAIN, 14));
		label_3.setBounds(418, 44, 126, 25);
		contentPanel.add(label_3);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 95, 949, 296);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] { { null, null,
				null, null, null, null, null }, }, new String[] { "ID",
				"Invoice No.", "Item Name", "Batch No.", "Qty.", "Value",
				"Expiry","Return Date","Select" }) {
			boolean[] columnEditables = new boolean[] { false, false, false,
					false, false, false, true };

			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				JTable target = (JTable) arg0.getSource();
				int row = target.getSelectedRow();
				int column = target.getSelectedColumn();
				// do some action

				Object selectedObject = table.getModel().getValueAt(row, 0);
				amount();
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

					Object selectedObject = table.getModel().getValueAt(row, 0);
				}
			}
		});
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		scrollPane.setViewportView(table);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {

						String[] strings = itemID.toArray(new String[itemID.size()]);
						invoice.addReturnGoods(strings);
						dispose();
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}

		searchTF.setText(supplierNameSTR);
	}

	public void populateTable() {

		try {
			ReturnInvoiceDBConnection db = new ReturnInvoiceDBConnection();
			ResultSet rs = db.retrieveReturenedGood(supplierID);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveReturenedGood(supplierID);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns + 2];

			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = rs.getObject(1).toString();
				Rows_Object_Array[R][1] = rs.getObject(2).toString();
				Rows_Object_Array[R][2] = rs.getObject(3).toString();
				Rows_Object_Array[R][3] = rs.getObject(4).toString();
				Rows_Object_Array[R][4] = rs.getObject(5).toString();
				Rows_Object_Array[R][5] = rs.getObject(6).toString();
				Rows_Object_Array[R][6] = rs.getObject(7).toString();
				Rows_Object_Array[R][7] = rs.getObject(8).toString();
				Rows_Object_Array[R][8] = new Boolean(false);
				R++;
			}
			TableModel model1 = new EditableTableModel(new String[] { "ID",
					"Invoice No.", "Item Name", "Batch No.", "Qty.", "Value",
					"Expiry","Return Date","Select" }, Rows_Object_Array);
			table.setModel(model1);
			table.getColumnModel().getColumn(0).setMinWidth(100);
			table.getColumnModel().getColumn(1).setPreferredWidth(100);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			table.getColumnModel().getColumn(2).setPreferredWidth(100);
			table.getColumnModel().getColumn(2).setMinWidth(150);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(5).setPreferredWidth(100);
			table.getColumnModel().getColumn(5).setMinWidth(150);
			amount();
		} catch (SQLException ex) {

			System.out.println(ex);
		}
	}

	public void amount() {
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		totalAmount = 0;
		itemID.clear();
		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean.valueOf(model1.getValueAt(count, 8).toString());
			if (b) {
				itemID.add(model1.getValueAt(count, 0)
								.toString());
			}
		}
	}

	public void updateInvoice() {
		InvoiceDBConnection connection = new InvoiceDBConnection();
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean.valueOf(model1.getValueAt(count, 6).toString());
			if (b) {
			
				try {
					connection.updateInvoiceStatus(model1.getValueAt(count, 0)
							.toString(), "" + paymetID);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		connection.closeConnection();
	}

	public void getSupplierName(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		supplierName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				supplierName.addElement(resultSet.getObject(3).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
		suplierCB.setModel(supplierName);
		if (i > 0) {
			suplierCB.setSelectedIndex(0);
		}
	}

	public void getSupplierDetail(String index) {

		SuppliersDBConnection suppliersDBConnection = new SuppliersDBConnection();
		ResultSet resultSet = suppliersDBConnection
				.searchSupplierWithIdOrNmae(index);
		try {
			while (resultSet.next()) {

				supplierID = resultSet.getObject(1).toString();
				supplierNameSTR = resultSet.getObject(2).toString();
				mobileSTR = (resultSet.getObject(4).toString());
				addressSTR = (resultSet.getObject(6).toString() + ", "
						+ resultSet.getObject(7).toString() + ", " + resultSet
						.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		suppliersDBConnection.closeConnection();
	}

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

}
