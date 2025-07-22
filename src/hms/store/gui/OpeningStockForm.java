package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
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

public class OpeningStockForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox departmentCB;
	private JComboBox doctorCB;
	private JTextField departmentTF;
	private JTextField personTF;
	private JTextField invoiceNoTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyInHandTF;
	private JTextField qtyIssuedTF;
	private JTextField afterIssuedTF;
	private JTextField previouseStockTF;
	private JTable table;
	final DefaultComboBoxModel departmentName = new DefaultComboBoxModel();
	final DefaultComboBoxModel doctorName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> previouseStockV = new Vector<String>();
	Vector<String> consumableV = new Vector<String>();

	Vector<String> expiryDateV = new Vector<String>();
	String doctorNameSTR;
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, taxValueSTR,
			expiryDateSTR = "", issuedDateSTR = "", dueDateSTR = "",
			previouseStock = "";
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0;
	int quantity = 0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JDateChooser expiryDateC;
	private JCheckBox chckbxConsumable;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			OpeningStockForm dialog = new OpeningStockForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public OpeningStockForm() {
		setTitle("Opening Stock Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OpeningStockForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1031, 595);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		departmentCB = new JComboBox();
		departmentCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (itemIDV.size() > 0) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									OpeningStockForm.this,
									"Are you sure to change department.then all added previous items cleard",
									"Clear Added Items", dialogButton);
					if (dialogResult == 0) {
						itemIDV.clear();
						itemNameV.clear();
						itemDescV.clear();
						issuedQtyV.clear();
						previouseStockV.clear();
						consumableV.clear();
						expiryDateV.clear();
						loadDataToTable();
						searchItemTF.setText("");

					} else {
						departmentCB.setSelectedItem(departmentNameSTR);
						return;
					}
				}
				try {
					departmentNameSTR = departmentCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				getDepartmentsDetail(departmentNameSTR);
				departmentTF.setText("");
				searchItemTF.setText("");
				if (departmentName.getSize() > 0) {

					departmentTF.setText("" + departmentID);
				}
			}
		});
		departmentCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentCB.setBounds(150, 11, 218, 25);
		contentPanel.add(departmentCB);

		JLabel lblCredit = new JLabel("Select Department");
		lblCredit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCredit.setBounds(14, 11, 126, 25);
		contentPanel.add(lblCredit);

		departmentTF = new JTextField();
		departmentTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		departmentTF.setColumns(10);
		departmentTF.setBounds(150, 47, 218, 25);
		contentPanel.add(departmentTF);

		JLabel lblDebit = new JLabel("Department ID :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(14, 47, 126, 25);
		contentPanel.add(lblDebit);

		personTF = new JTextField();
		personTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		personTF.setColumns(10);
		personTF.setBounds(150, 83, 218, 25);
		contentPanel.add(personTF);

		JLabel lblBalance = new JLabel("Person Name :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(14, 83, 126, 25);
		contentPanel.add(lblBalance);

		invoiceNoTF = new JTextField();
		invoiceNoTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		invoiceNoTF.setColumns(10);
		invoiceNoTF.setBounds(750, 47, 218, 25);
		contentPanel.add(invoiceNoTF);

		JLabel lblInvoiceNo = new JLabel("Intend Slip No. :");
		lblInvoiceNo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblInvoiceNo.setBounds(614, 47, 126, 25);
		contentPanel.add(lblInvoiceNo);

		JLabel lblDate = new JLabel("Date :");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDate.setBounds(614, 83, 126, 25);
		contentPanel.add(lblDate);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 190, 119, 20);
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
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					previouseStockTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

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
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

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
					afterIssuedTF.setText("");
					afterIssuedTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					measUnit.removeAllElements();

				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(317, 190, 153, 20);
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
				qtyInHandTF.setText("");
				afterIssuedTF.setText("");
				previouseStockTF.setText("");
				getItemStock(itemIDSTR);
				getItemDetail(itemIDSTR);
				if (itemName.getSize() > 0) {

					itemDescTF.setText("" + itemDescSTR);
					qtyInHandTF.setText("" + quantity);
					afterIssued = quantity - qtyIssued;
					afterIssuedTF.setText("" + afterIssued);
					previouseStockTF.setText(previouseStock + "");
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 190, 168, 20);
		contentPanel.add(itemNameCB);

		qtyInHandTF = new JTextField();
		qtyInHandTF.setEditable(false);
		qtyInHandTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyInHandTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		qtyInHandTF.setBounds(480, 190, -1, 20);
		contentPanel.add(qtyInHandTF);
		qtyInHandTF.setColumns(10);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(480, 190, 88, 20);
		contentPanel.add(qtyIssuedTF);
		qtyIssuedTF.setColumns(10);
		qtyIssuedTF.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE) || (vChar == KeyEvent.VK_DELETE))) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});
		qtyIssuedTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;

				afterIssuedTF.setText("" + afterIssued);

			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = qtyIssuedTF.getText() + "";
				if (!str.equals("")) {

					qtyIssued = Integer.parseInt(str);

				} else {

					qtyIssued = 0;

				}
				afterIssued = quantity - qtyIssued;
				afterIssuedTF.setText("" + afterIssued);

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
				afterIssuedTF.setText("" + afterIssued);
			}
		});

		afterIssuedTF = new JTextField();
		afterIssuedTF.setEditable(false);
		afterIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		afterIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 13));
		afterIssuedTF.setBounds(680, 190, -8, 20);
		contentPanel.add(afterIssuedTF);
		afterIssuedTF.setColumns(10);

		previouseStockTF = new JTextField();
		previouseStockTF.setEditable(false);
		previouseStockTF.setHorizontalAlignment(SwingConstants.RIGHT);
		previouseStockTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		previouseStockTF.setBounds(139, 221, 164, 20);
		contentPanel.add(previouseStockTF);
		previouseStockTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Current Stock :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(20, 224, 109, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 165, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(317, 165, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblqtyIssued = new JLabel("Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(480, 165, 88, 14);
		contentPanel.add(lblqtyIssued);

		JButton btnNewButton = new JButton("Add Line");
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
//				if (Integer.parseInt(afterIssuedTF.getText().toString()) < 0) {
//					JOptionPane.showMessageDialog(null,
//							"your requirements exceeded the stock",
//							"Input Error", JOptionPane.ERROR_MESSAGE);
//					return;
//				} else
					if (expiryDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter expiry date", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				else if (itemIDV.indexOf(itemIDSTR) != -1) {
					JOptionPane.showMessageDialog(null,
							"this item is already entered", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				itemIDV.add(itemIDSTR);
				itemNameV.add(itemNameSTR);
				itemDescV.add(itemDescSTR);
				

				issuedQtyV.add(qtyIssued + "");
				if (previouseStockTF.getText().toString().equals("")) {
					previouseStockV.add("New Item");
				} else {
					previouseStockV.add(previouseStockTF.getText().toString());
				}

				consumableV.add(chckbxConsumable.isSelected() ? "Yes" : "No");
				expiryDateV.add(expiryDateSTR);
				loadDataToTable();
				searchItemTF.setText("");

			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(909, 165, 106, 31);
		contentPanel.add(btnNewButton);

		JLabel lblExpireDate = new JLabel("Expire Date :");
		lblExpireDate.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblExpireDate.setBounds(576, 193, 86, 14);
		contentPanel.add(lblExpireDate);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

				itemIDV.remove(cur_selectedRow);
				itemNameV.remove(cur_selectedRow);
				itemDescV.remove(cur_selectedRow);
				issuedQtyV.remove(cur_selectedRow);
				previouseStockV.remove(cur_selectedRow);
				consumableV.remove(cur_selectedRow);

				expiryDateV.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(909, 210, 106, 31);
		contentPanel.add(btnRemove);
		btnRemove.setEnabled(false);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 247, 992, 253);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Item ID", "Item Name", "Item Desc.", "Issued Qty.",
				"Previouse Stock", "Cosumable", "Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);
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
		separator.setBounds(10, 140, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Save Form");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				// if(departmentCB.getSelectedIndex()==-1)
				// {
				// JOptionPane.showMessageDialog(null,
				// "Please select supplier", "Input Error",
				// JOptionPane.ERROR_MESSAGE);
				// return;
				// }

				if (invoiceNoTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter invoice no", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (issuedDateSTR.equals("")) {
					JOptionPane.showMessageDialog(null, "Please enter date",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (personTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter person name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				if (itemIDV.size() <= 0) {
					JOptionPane.showMessageDialog(null,
							"Please enter atleast one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				IssuedItemsDBConnection issuedItemsDBConnection = new IssuedItemsDBConnection();
				ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;

				String[] data = new String[30];
				data[0] = departmentID;
				data[1] = departmentNameSTR;
				data[2] = personTF.getText().toString();
				data[3] = "N/A";
				data[4] = "" + invoiceNoTF.getText().toString();
				data[5] = issuedDateSTR;
				data[6] = "" + timeFormat.format(cal1.getTime());
				for (int i = 0; i < itemIDV.size(); i++) {
					data[7] = itemIDV.get(i);
					data[8] = itemNameV.get(i);
					data[9] = itemDescV.get(i);
					data[10] = issuedQtyV.get(i);
					data[11] = previouseStockV.get(i);
					data[12] = consumableV.get(i);
					data[13] = "0000-00-00";
					data[14] = "No";
					data[15] = expiryDateV.get(i);
					data[16] = ""+StoreMain.userName; // user name
					data[17] = doctorNameSTR;
					try {
						issuedItemsDBConnection.inserIssuedData(data);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
//					try {
//						itemsDBConnection.subtractStock(itemIDV.get(i),
//								issuedQtyV.get(i));
//					} catch (Exception e1) {
//						// TODO Auto-generated catch block
//						e1.printStackTrace();
//					}
				}
				issuedItemsDBConnection.closeConnection();
				DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();

				for (int i = 0; i < itemIDV.size(); i++) {
					data[0] = departmentID;
					data[1] = departmentNameSTR;
					data[2] = "n/a";
					data[3] = personTF.getText().toString();
					data[4] = itemIDV.get(i);
					data[5] = itemNameV.get(i);
					data[6] = itemDescV.get(i);
					data[7] = issuedQtyV.get(i);
					data[8] = issuedDateSTR;
					data[9] = expiryDateV.get(i);

					if (previouseStockV.get(i).equals("New Item")) {
						try {
							departmentStockDBConnection
									.inserDepartmentStock(data);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						try {
							departmentStockDBConnection.setStock(
									itemIDV.get(i), issuedQtyV.get(i),
									departmentNameSTR);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				departmentStockDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Item Issued Successfully", "Issued Items",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(33, 511, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(196, 511, 153, 39);
		contentPanel.add(btnCancel);

		expiryDateC = new JDateChooser();
		expiryDateC.getCalendarButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

			}
		});
		expiryDateC.getDateEditor().addPropertyChangeListener(
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
		expiryDateC.setMinSelectableDate(new Date());
		expiryDateC.setDateFormatString("yyyy-MM-dd");
		expiryDateC.setBounds(658, 190, 153, 20);
		contentPanel.add(expiryDateC);

		JDateChooser invoiceDate = new JDateChooser();
		invoiceDate.getCalendarButton().addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		invoiceDate.setBounds(750, 83, 218, 25);
		contentPanel.add(invoiceDate);

		chckbxConsumable = new JCheckBox("Consumable");
		chckbxConsumable.setSelected(true);
		chckbxConsumable.setFont(new Font("Tahoma", Font.PLAIN, 13));
		chckbxConsumable.setBounds(804, 187, 97, 25);
		contentPanel.add(chckbxConsumable);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setIcon(new ImageIcon(OpeningStockForm.class
				.getResource("/icons/aladdin3.gif")));
		lblNewLabel.setBounds(416, 0, 171, 133);
		contentPanel.add(lblNewLabel);
		invoiceDate.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							issuedDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
						}
					}
				});
		invoiceDate.setDate(new Date());
		
		
//		doctorCB = new JComboBox();
//		doctorCB.addActionListener(new ActionListener() {
//			@Override
//			public void actionPerformed(ActionEvent arg0) {
//				
//				doctorCB.setSelectedItem(doctorNameSTR);
//				
//				try {
//					doctorNameSTR =doctorCB.getSelectedItem()
//							.toString();
//					
//				} catch (Exception e) {
//					// TODO: handle exception
//
//				}
//
//				getDoctorDetail(doctorNameSTR);
//				
//			}
//		});
		
		
		doctorCB = new JComboBox();
		doctorCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				try {
					doctorNameSTR =doctorCB.getSelectedItem()
							.toString();
					
				} catch (Exception e1) {
					// TODO: handle exception

				}
			}
		});
		doctorCB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorCB.setBounds(750, 11, 218, 25);
		contentPanel.add(doctorCB);
		
		JLabel lblSelectDoctor = new JLabel("Select Doctor");
		lblSelectDoctor.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblSelectDoctor.setBounds(614, 11, 126, 25);
		contentPanel.add(lblSelectDoctor);
		getAllDoctors();
		getAllDepartments();
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
				expiryDateC.setDate(DateFormatChange.StringToDate(resultSet
						.getObject(9).toString()));
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		itemsDBConnection.closeConnection();

	}

	public void getItemStock(String index) {

		previouseStock = "";
		DepartmentStockDBConnection departmentStockDBConnection = new DepartmentStockDBConnection();
		ResultSet resultSet = departmentStockDBConnection.retrieveStock(index,
				departmentNameSTR);

		try {
			while (resultSet.next()) {

				previouseStock = (resultSet.getObject(1).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		departmentStockDBConnection.closeConnection();

	}

	private void loadDataToTable() {
		// get size of the hashmap
		int size = itemIDV.size();

		int total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][10];

		for (int i = 0; i < itemIDV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = previouseStockV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = consumableV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = expiryDateV.get(i);

		}
		table.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Desc.",
						"Issued Qty.", "Previouse Stock", "Cosumable", "Expiry" }));
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(180);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(180);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(150);
		table.getColumnModel().getColumn(4).setMinWidth(150);
		table.getColumnModel().getColumn(5).setPreferredWidth(150);
		table.getColumnModel().getColumn(5).setMinWidth(150);
		table.getColumnModel().getColumn(6).setPreferredWidth(100);
		table.getColumnModel().getColumn(6).setMinWidth(100);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);

		finalTotalValue = total;

	}

	public void getDepartmentsDetail(String deptName) {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveDataWithName(deptName);
		int i = 0;
		try {
			while (resultSet.next()) {
				departmentID = resultSet.getObject(1).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();

	}

	public void getAllDepartments() {
		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		departmentName.removeAllElements();
		int i = 0;
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
	
//	public void getDoctorDetail(String docName) {
//		DepartmentDBConnection dbConnection = new DepartmentDBConnection();
//		ResultSet resultSet = dbConnection.retrieveDataWithName(docName);
//		int i = 0;
//		try {
//			while (resultSet.next()) {
//				departmentID = resultSet.getObject(3).toString();
//				i++;
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		dbConnection.closeConnection();
//
//	}
//	
	
	public void getAllDoctors() {
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		doctorName.removeAllElements();
		doctorName.addElement("Other Doctor");
		int i = 0;
		try {
			while (resultSet.next()) {
				doctorName.addElement(resultSet.getObject(2).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctorCB.setModel(doctorName);
		if (i > 0) {
			doctorCB.setSelectedIndex(0);
		}
	}
}
