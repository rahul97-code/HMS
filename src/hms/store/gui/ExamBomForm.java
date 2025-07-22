package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.exam.database.ReferenceTableDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.ExamBomDBConnection;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ProceduresDBConnection;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import com.toedter.calendar.JDateChooser;
import javax.swing.JCheckBox;
import java.awt.Toolkit;

public class ExamBomForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyIssuedTF;
	private JTable table;
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel testNameCBModel = new DefaultComboBoxModel();
	Vector<String> ID = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, priceValueSTR;
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0;
	int quantity = 0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	JComboBox itemNameCB;
	JLabel itemPriceLBL;
	String procedureIDSTR, procedureNameSTR;
	private JTextField searchTestNameET;
	private JComboBox selectTestNameCB;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			ExamBomForm dialog = new ExamBomForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public ExamBomForm() {
		setTitle("Exam BOM");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				ExamBomForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1013, 445);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 125, 140, 20);
		contentPanel.add(searchItemTF);
		searchItemTF.setColumns(10);
		searchItemTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {

					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					itemPriceLBL.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					itemPriceLBL.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					qtyIssuedTF.setText("");
					itemPriceLBL.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);

				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(352, 125, 218, 20);
		contentPanel.add(itemDescTF);
		itemDescTF.setColumns(10);

		itemNameCB = new JComboBox();
		itemNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					itemNameSTR = itemNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}

				itemDescTF.setText("");
				itemPriceLBL.setText("");
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {

					itemDescTF.setText("" + itemDescSTR);
					itemPriceLBL.setText("" + priceValueSTR);
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(160, 125, 172, 20);
		contentPanel.add(itemNameCB);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(652, 125, 88, 20);
		contentPanel.add(qtyIssuedTF);
		qtyIssuedTF.setColumns(10);
//		qtyIssuedTF.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				char vChar = e.getKeyChar();
//				if (!(Character.isDigit(vChar)
//						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
//					e.consume();
//
//					// ||vChar== '.'
//				}
//			}
//		});
		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				
				

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
			}
		});

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 100, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(352, 100, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblqtyIssued = new JLabel("Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(652, 100, 88, 14);
		contentPanel.add(lblqtyIssued);

		JButton btnNewButton = new JButton("Add Item");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (itemDescTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select item",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (qtyIssuedTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter issued qty.", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				else if (itemIDV.indexOf(itemIDSTR) != -1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				ExamBomDBConnection proceduresDBConnection = new ExamBomDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
			//	`item_id`, `item_name`, `item_desc`, `exam_name`, `item_qty`, `item_date`, `item_time`, `item_entry_user`,
				String[] data = new String[30];
				data[0] = itemIDSTR + "";
				data[1] = itemNameSTR;
				data[2] = itemDescSTR;
				data[3] = selectTestNameCB.getSelectedItem().toString();
				data[4] = qtyIssuedTF.getText().toString() + "";
				data[5] = DateFormatChange.StringToMysqlDate(new Date());
				data[6] = "" + timeFormat.format(cal1.getTime());
				data[7] = "" + StoreMain.userName;
				// `procedure_id`, `procedure_name`, `item_id`, `item_name`,
				// `item_quantity`, `date`, `time`, `added_by`
				try {
					proceduresDBConnection.insertData(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				proceduresDBConnection.closeConnection();
				loadItemsOfProcedure();
				searchItemTF.setText("");

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(750, 119, 113, 31);
		contentPanel.add(btnNewButton);

		final JButton btnRemove = new JButton("Delete Item");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

				ExamBomDBConnection proceduresDBConnection = new ExamBomDBConnection();
				try {
					proceduresDBConnection.deleteItem(toDelete);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				proceduresDBConnection.closeConnection();
				loadItemsOfProcedure();
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(873, 119, 119, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 173, 992, 233);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Desc.", "Qty." }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(0).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setPreferredWidth(280);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(280);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(250);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						int selectedRowIndex = table.getSelectedRow();
						selectedRowIndex = table
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = table.getSelectedColumn();
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(12, 87, 980, 2);
		contentPanel.add(separator);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrice.setBounds(580, 103, 62, 14);
		contentPanel.add(lblPrice);

		itemPriceLBL = new JLabel("price");
		itemPriceLBL.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemPriceLBL.setBounds(583, 125, 59, 20);
		contentPanel.add(itemPriceLBL);

		JLabel lblSelectItem = new JLabel("Select Item :");
		lblSelectItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectItem.setBounds(160, 100, 153, 14);
		contentPanel.add(lblSelectItem);
		
		JLabel label = new JLabel("Select Test Name");
		label.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label.setBounds(23, 56, 197, 20);
		contentPanel.add(label);
		
		selectTestNameCB = new JComboBox();
		selectTestNameCB.setFont(new Font("Tahoma", Font.PLAIN, 15));
		selectTestNameCB.setBounds(216, 53, 647, 26);
		selectTestNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					 System.out.println(selectTestNameCB.getSelectedItem().toString());
					 String testName=selectTestNameCB.getSelectedItem().toString();
					 loadItemsOfProcedure();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		contentPanel.add(selectTestNameCB);
		
		searchTestNameET = new JTextField();
		searchTestNameET.setFont(new Font("Tahoma", Font.PLAIN, 15));
		searchTestNameET.setColumns(10);
		searchTestNameET.setBounds(215, 20, 648, 25);
		contentPanel.add(searchTestNameET);

		searchTestNameET.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							searchTestName3(str);
							selectTestNameCB.setModel(testNameCBModel);
							if(testNameCBModel.getSize()>0)
								selectTestNameCB.setSelectedIndex(0);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							searchTestName3(str);
							selectTestNameCB.setModel(testNameCBModel);
							if(testNameCBModel.getSize()>0)
								selectTestNameCB.setSelectedIndex(0);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							searchTestName3(str);
							selectTestNameCB.setModel(testNameCBModel);
							if(testNameCBModel.getSize()>0)
								selectTestNameCB.setSelectedIndex(0);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}
				});
		
		JLabel label_1 = new JLabel("Search Test Name");
		label_1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		label_1.setBounds(24, 21, 153, 23);
		contentPanel.add(label_1);
	}
	public void searchTestName(String testName)
	{
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.searchTestSubName(testName);
		
		try {
			while (resultSet.next()) {
				
				testNameCBModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	
	public void searchTestName2(String testName)
	{
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.searchTestSubName2(testName);
		try {
			while (resultSet.next()) {
				testNameCBModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		
	}
	public void searchTestName3(String testName)
	{
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.searchTestName3(testName);
		try {
			while (resultSet.next()) {
				testNameCBModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public void loadItemsOfProcedure() {
		itemIDV.clear();
		itemNameV.clear();
		itemDescV.clear();
		issuedQtyV.clear();
		ExamBomDBConnection dbConnection = new ExamBomDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveData(selectTestNameCB.getSelectedItem().toString());
		try {
			while (resultSet.next()) {
				ID.add(resultSet.getObject(1).toString());
				itemIDV.add(resultSet.getObject(2).toString());
				itemNameV.add(resultSet.getObject(3).toString());
				itemDescV.add(resultSet.getObject(4).toString());
				issuedQtyV.add(resultSet.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		loadDataToTable();

	}

	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		int i = 0;
		try {
			while (resultSet.next()) {
				itemID.add(resultSet.getObject(1).toString());
				itemName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();
		itemNameCB.setModel(itemName);
		if (i > 0) {
			itemNameCB.setSelectedIndex(0);
		}
	}

	public void getItemDetail(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		int i = 0;
		try {
			while (resultSet.next()) {

				itemDescSTR = resultSet.getObject(3).toString();
				quantity = Integer.parseInt(resultSet.getObject(8).toString());
				priceValueSTR = resultSet.getObject(10).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		int total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = ID.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = issuedQtyV.get(i);
			
		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] {"ID" ,"Item ID", "Item Name", "Item Desc.", "Qty." }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(0).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(70);
		table.getColumnModel().getColumn(1).setMinWidth(70);
		table.getColumnModel().getColumn(2).setPreferredWidth(350);
		table.getColumnModel().getColumn(2).setMinWidth(350);
		table.getColumnModel().getColumn(3).setPreferredWidth(350);
		table.getColumnModel().getColumn(3).setMinWidth(350);
		table.getColumnModel().getColumn(4).setPreferredWidth(140);
		table.getColumnModel().getColumn(4).setMinWidth(140);

	}

}
