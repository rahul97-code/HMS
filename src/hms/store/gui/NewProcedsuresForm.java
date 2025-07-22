package hms.store.gui;

import hms.departments.database.DepartmentDBConnection;
import hms.departments.database.DepartmentStockDBConnection;
import hms.main.DateFormatChange;
import hms.store.database.IssuedItemsDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.ProceduresDBConnection;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.CellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.swing.ListSelectionModel;
import javax.swing.ImageIcon;
import com.toedter.calendar.JDateChooser;

import LIS_System.LIS_Status_result;

import javax.swing.JCheckBox;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;

public class NewProcedsuresForm extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField procedureNameTF;
	private JTextField chargesTF;
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField qtyIssuedTF;
	private JTable table;
	private JButton btnEditable;
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel procedureName = new DefaultComboBoxModel();
	Vector<String> supplierIDV = new Vector<String>();
	Vector<String> itemID = new Vector<String>();
	Vector<String> updateitemID = new Vector<String>();
	Vector<String> updateQtyV = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> issuedQtyV = new Vector<String>();
	Vector<String> procedureIDV = new Vector<String>();
	Vector<String> procedureNameV = new Vector<String>();
	Vector<String> procedurePriceV = new Vector<String>();
	Vector<String> preDaysV = new Vector<String>();
	Vector<String> postDaysV = new Vector<String>();
	String departmentNameSTR, departmentID, personname, supplierID;
	String itemIDSTR, itemNameSTR, itemDescSTR, taxTypeSTR, priceValueSTR;
	int qtyIssued = 0, afterIssued = 0, discountValue = 0, itemValue,
			finalTaxValue = 0, finalDiscountValue = 0, finalTotalValue = 0;
	int quantity = 0;
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JComboBox itemNameCB;
	private JLabel itemPriceLBL;
	private JComboBox<String> procedureNameCB;
	String procedureIDSTR, procedureNameSTR;
	private JButton btnUpdate;
	private JTextField preDaysTF;
	private JTextField postDaysTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			NewProcedsuresForm dialog = new NewProcedsuresForm();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public NewProcedsuresForm() {
		
		
		setTitle("New Procedures Form");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				NewProcedsuresForm.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 1040, 545);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		procedureNameTF = new JTextField();
		procedureNameTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		procedureNameTF.setColumns(10);
		procedureNameTF.setBounds(507, 28, 209, 25);
		contentPanel.add(procedureNameTF);

		JLabel lblDebit = new JLabel("Procedure Name :");
		lblDebit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblDebit.setBounds(371, 28, 126, 25);
		contentPanel.add(lblDebit);

		chargesTF = new JTextField();
		chargesTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		chargesTF.setColumns(10);
		chargesTF.setBounds(834, 28, 142, 25);
		contentPanel.add(chargesTF);
		chargesTF.addKeyListener(new KeyAdapter() {
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

		JLabel lblBalance = new JLabel("Charges :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblBalance.setBounds(746, 28, 78, 25);
		contentPanel.add(lblBalance);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 202, 150, 20);
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
		itemDescTF.setBounds(362, 202, 218, 20);
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

					afterIssued = quantity - qtyIssued;
				}
			}
		});
		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(170, 202, 172, 20);
		contentPanel.add(itemNameCB);

		qtyIssuedTF = new JTextField();
		qtyIssuedTF.setHorizontalAlignment(SwingConstants.RIGHT);
		qtyIssuedTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		qtyIssuedTF.setBounds(662, 202, 88, 20);
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
		lblSearch.setBounds(10, 177, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Description :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(362, 177, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblqtyIssued = new JLabel("Qty.");
		lblqtyIssued.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblqtyIssued.setBounds(662, 177, 88, 14);
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
				ProceduresDBConnection proceduresDBConnection = new ProceduresDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;

				String[] data = new String[30];
				data[0] = procedureIDSTR + "";
				data[1] = procedureNameTF.getText().toString();
				data[2] = itemIDSTR;
				data[3] = itemNameSTR;
				data[4] = qtyIssued + "";
				data[5] = DateFormatChange.StringToMysqlDate(new Date());
				data[6] = "" + timeFormat.format(cal1.getTime());
				data[7] = "" + StoreMain.userName;
				// `procedure_id`, `procedure_name`, `item_id`, `item_name`,
				// `item_quantity`, `date`, `time`, `added_by`
				try {
					proceduresDBConnection.insertProcedureItemsData(data);
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
		btnNewButton.setBounds(762, 191, 113, 31);
		contentPanel.add(btnNewButton);

		final JButton btnRemove = new JButton("Delete Item");
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateitemID.removeAllElements();
				EditableTableModel model1 = (EditableTableModel) table.getModel();
				for (int count = 0; count < model1.getRowCount(); count++) {

					Boolean b = Boolean.valueOf(model1.getValueAt(count, 4).toString());
					if (b) {
						updateitemID.add(model1.getValueAt(count, 0).toString());
					}
				}
				if (!(updateitemID.size()>0)) {
					JOptionPane.showMessageDialog(null,
							"Please select item!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}	
				ProceduresDBConnection proceduresDBConnection = new ProceduresDBConnection();
				try {
					proceduresDBConnection.deleteItem1(procedureIDSTR,updateitemID);
					JOptionPane.showMessageDialog(null,
							"Sucessfully Deleted", "Message",
							JOptionPane.INFORMATION_MESSAGE);
				} catch (Exception e1) {
					// TODO Auto-generated catch block 
					e1.printStackTrace();
				}
		
				proceduresDBConnection.closeConnection();
				loadItemsOfProcedure();
				
				//btnRemove.setEnabled(false);
			}
		});
		btnRemove.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRemove.setBounds(887, 191, 119, 31);
		contentPanel.add(btnRemove);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 238, 992, 213);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
//		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
//				"Item ID", "Item Name", "Item Desc.", "Qty." }));
		
		
//		final boolean[] canEdit = new boolean[] { false, false, false,
//				false};
//		// Finally load data to the table
//		DefaultTableModel model = new DefaultTableModel(new Object[][] {}, new String[] {
//				"Item ID", "Item Name", "Item Desc.", "Qty." }) {
//			@Override
//			public boolean isCellEditable(int row, int column) {
//				return false;
//			}
//		};
//		table.setModel(model);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
					"Item ID", "Item Name", "Item Desc.", "Qty.","select"
			}
		));
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(330);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(350);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action
					table.requestFocus();
					table.editCellAt(row, 4);
					 
					
				}
			}
		});
		if(StoreMain.userID.equals("6") || StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")){
			btnRemove.setEnabled(true);	
		}
	
		
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(23, 164, 966, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Add");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (procedureNameTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter procedure name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (chargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter procedure charges", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (preDaysTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter pre days", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (postDaysTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter post days", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (chargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter procedure charges", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (procedureNameV.contains(procedureNameTF.getText().toString())) {
					JOptionPane.showMessageDialog(null,
							"Procedure is already exist. Please select another", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				ProceduresDBConnection proceduresDBConnection = new ProceduresDBConnection();
				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				int index = 0;
				String[] data = new String[30];
				data[0] = procedureNameTF.getText().toString();
				data[1] = chargesTF.getText().toString();
				data[2] = preDaysTF.getText().toString();
				data[3] = postDaysTF.getText().toString();
				data[4] = DateFormatChange.StringToMysqlDate(new Date());
				data[5] = "" + timeFormat.format(cal1.getTime());
				data[6] = "" + StoreMain.userName;
				try {
					index = proceduresDBConnection.insertProcedureData(data);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

				proceduresDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null, "Procedure Added Successfully. Please Select Procedure from dropdown and add items",
						"Procedure Save", JOptionPane.INFORMATION_MESSAGE);
				getAllProcedures();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(332, 114, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(823, 114, 153, 39);
		contentPanel.add(btnCancel);

		JLabel lblPrice = new JLabel("Price");
		lblPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblPrice.setBounds(590, 180, 62, 14);
		contentPanel.add(lblPrice);

		itemPriceLBL = new JLabel("price");
		itemPriceLBL.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemPriceLBL.setBounds(593, 202, 59, 20);
		contentPanel.add(itemPriceLBL);

		JLabel lblSelectItem = new JLabel("Select Item :");
		lblSelectItem.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSelectItem.setBounds(170, 177, 153, 14);
		contentPanel.add(lblSelectItem);

		procedureNameCB = new JComboBox<String>();
		procedureNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		procedureNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					procedureNameSTR = procedureNameCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception

				}

				if (procedureNameCB.getSelectedIndex() > -1) {
					procedureIDSTR = procedureIDV.get(procedureNameCB
							.getSelectedIndex());
				}
				try {
					procedureNameTF.setText(procedureNameSTR);
					chargesTF.setText(procedurePriceV.get(procedureNameCB
							.getSelectedIndex()));
					preDaysTF.setText(preDaysV.get(procedureNameCB
							.getSelectedIndex()));
					postDaysTF.setText(postDaysV.get(procedureNameCB
							.getSelectedIndex()));
					loadItemsOfProcedure();
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		});
		procedureNameCB.setBounds(126, 28, 235, 25);
		contentPanel.add(procedureNameCB);

		JLabel label = new JLabel("Select Procedure :");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 28, 106, 25);
		contentPanel.add(label);

		btnUpdate = new JButton("Update");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (procedureNameTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter procedure name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (chargesTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter procedure charges", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}	
				if (preDaysTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter pre days", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (postDaysTF.getText().toString().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please enter post days", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				ProceduresDBConnection connection=new ProceduresDBConnection();
				try {
					connection.updateProcdure(procedureIDSTR, procedureNameTF.getText().toString(), chargesTF.getText().toString(), preDaysTF.getText().toString(), postDaysTF.getText().toString());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				connection.closeConnection();
				JOptionPane.showMessageDialog(null,
						"Updated Successfully", "Success",
						JOptionPane.INFORMATION_MESSAGE);
				getAllProcedures();
			}
		});
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnUpdate.setBounds(495, 114, 153, 39);
		contentPanel.add(btnUpdate);

		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int reply = JOptionPane.showConfirmDialog(null, "Are You sure to delete whole procedure", "Confirmation", JOptionPane.YES_NO_OPTION);
				if (reply == JOptionPane.YES_OPTION) {
					ProceduresDBConnection connection=new ProceduresDBConnection();
					try {
						connection.deleteProcedure(procedureIDSTR);
						connection.closeConnection();
						 JOptionPane.showMessageDialog(null,
									"Deleted Successfully", "Success",
									JOptionPane.INFORMATION_MESSAGE);
						       dispose();
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					}
				   
				
			}
		});
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnDelete.setBounds(660, 114, 153, 39);
		contentPanel.add(btnDelete);
		
		preDaysTF = new JTextField();
		preDaysTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		preDaysTF.setColumns(10);
		preDaysTF.setBounds(507, 78, 113, 25);
		contentPanel.add(preDaysTF);
		
		
		if(StoreMain.userID.equals("6") || StoreMain.userID.equals("16") || StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1")){
			btnDelete.setEnabled(true);
			btnUpdate.setEnabled(true);
//			btnNewButton_1.setEnabled(false);
//			btnNewButton.setEnabled(false);
			btnRemove.setEnabled(true);
		}else{
			btnDelete.setEnabled(false);
			btnUpdate.setEnabled(false);
//			btnNewButton_1.setEnabled(false);
//			btnNewButton.setEnabled(false);
			btnRemove.setEnabled(false);
		}
		
		JLabel lblPreDays = new JLabel("Pre Days :");
		lblPreDays.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPreDays.setBounds(371, 78, 126, 25);
		contentPanel.add(lblPreDays);
		
		postDaysTF = new JTextField();
		postDaysTF.setFont(new Font("Tahoma", Font.PLAIN, 14));
		postDaysTF.setColumns(10);
		postDaysTF.setBounds(834, 78, 142, 25);
		contentPanel.add(postDaysTF);
		
		JLabel lblPostDays = new JLabel("Post Days :");
		lblPostDays.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPostDays.setBounds(693, 78, 126, 25);
		contentPanel.add(lblPostDays);
		
		 btnEditable = new JButton("Update");
		 btnEditable.setEnabled(false);
		btnEditable.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				calculate();
				loadItemsOfProcedure();
			}
		});
		btnEditable.setFont(new Font("Dialog", Font.BOLD, 13));
		btnEditable.setBounds(887, 463, 119, 31);
		contentPanel.add(btnEditable);
		
		if(StoreMain.access.equals("1") || StoreMain.update_item_access.equals("1"))
		{
			btnEditable.setEnabled(true);
		}else
		{
			btnEditable.setEnabled(false);
		}
		
		JButton btnExcel = new JButton("EXCEL");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("IssueProceduresFromStore_data.xls"));
				if (fileChooser.showSaveDialog(NewProcedsuresForm.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					ReportExcel(table, file.toPath().toString());
			}
			

			}
		});
		btnExcel.setFont(new Font("Dialog", Font.BOLD, 13));
		btnExcel.setBounds(764, 463, 113, 31);
		contentPanel.add(btnExcel);
		getAllProcedures();
	}
	public void ReportExcel(JTable table, String path) {
		// TODO Auto-generated constructor stub
		try {
			String filename = path;
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("Report");
			HSSFRow row = sheet.createRow(1);
			TableModel model = table.getModel();
			HSSFRow rowhead = sheet.createRow((short) 0);

			HSSFRow headerRow = sheet.createRow(0); // Create row at line 0
			for (int headings = 0; headings < model.getColumnCount(); headings++) { // For
																					// each
																					// column
				headerRow.createCell(headings).setCellValue(
						model.getColumnName(headings));// Write column name
			}

			for (int rows = 0; rows < model.getRowCount(); rows++) { // For each
																		// table
																		// row
				for (int cols = 0; cols < table.getColumnCount(); cols++) { // For
																			// each
																			// table
																			// column
					row.createCell(cols).setCellValue(
							model.getValueAt(rows, cols).toString()); // Write
																		// value
				}

				// Set the row to the next one in the sequence
				row = sheet.createRow((rows + 2));
			}

			FileOutputStream fileOut = new FileOutputStream(filename);
			workbook.write(fileOut);
			fileOut.close();
			JOptionPane.showMessageDialog(null,
					"Excel File Generated Successfully", "Data Saved",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception ex) {
			System.out.println(ex);
		}
	}

	public void loadItemsOfProcedure() {
		itemIDV.clear();
		itemNameV.clear();
		itemDescV.clear();
		issuedQtyV.clear();
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		ResultSet resultSet = dbConnection
				.retrieveDataProcedure(procedureIDSTR);
		try {
			while (resultSet.next()) {
				itemIDV.add(resultSet.getObject(1).toString());
				itemNameV.add(resultSet.getObject(2).toString());
				itemDescV.add(resultSet.getObject(3).toString());
				issuedQtyV.add(resultSet.getObject(4).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		loadDataToTable();

	}
	public void calculate() {
		updateitemID.clear();
		updateQtyV.clear();
		EditableTableModel model1 = (EditableTableModel) table.getModel();
		for (int count = 0; count < model1.getRowCount(); count++) {

			Boolean b = Boolean.valueOf(model1.getValueAt(count, 4).toString());
			if (b) {
				updateitemID.add(model1.getValueAt(count, 0).toString());
				updateQtyV.add(model1.getValueAt(count, 3).toString());
			}
		}
		
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		try {
			dbConnection.updateData1(updateitemID, updateQtyV);
			JOptionPane.showMessageDialog(null,
					"Update Successfully", "Success",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
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

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = itemDescV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = issuedQtyV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = new Boolean(false);
		}
		TableModel model1 = new EditableTableModel(new String[]{ "Item ID", "Item Name", "Item Desc.", "Qty.","select" },ObjectArray_ListOfexamsSpecs);
		table.setModel(model1);
		
		if(table.getRowCount()>0) {
			get();
		}
		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(0).setMinWidth(75);
		table.getColumnModel().getColumn(1).setPreferredWidth(330);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		table.getColumnModel().getColumn(2).setPreferredWidth(350);
		table.getColumnModel().getColumn(2).setMinWidth(150);
		table.getColumnModel().getColumn(3).setPreferredWidth(130);
		table.getColumnModel().getColumn(3).setMinWidth(130);
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 1) {
					JTable target = (JTable) e.getSource();
					final int row = target.getSelectedRow();
					int column = target.getSelectedColumn();
					// do some action
					if(column!=4)
					{
						table.requestFocus();
						table.editCellAt(row, 3);
					}
					
					 
					
				}
			}
		});
		table.getModel().addTableModelListener(new TableModelListener() {
			  

			@Override
			public void tableChanged(TableModelEvent e) {
				// TODO Auto-generated method stub
				table.requestFocus();
			}
			});
		table.addFocusListener(new FocusListener() {
		      public void focusGained(FocusEvent e) {
		      }

		      // this function successfully provides cell editing stop
		      // on cell losts focus (but another cell doesn't gain focus)
		      public void focusLost(FocusEvent e) {
		        CellEditor cellEditor = table.getCellEditor();
		        if (cellEditor != null)
		          
		            cellEditor.stopCellEditing();
		          
		      }
		    });
	}
	public void get() {
		table.setAutoCreateRowSorter(true);
	}
	public void getAllProcedures() {
		ProceduresDBConnection dbConnection = new ProceduresDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		procedureIDV.clear();
		procedureNameV.clear();
		procedurePriceV.clear();
		preDaysV.clear();
		postDaysV.clear();
		procedureName.removeAllElements();
		int i = 0;
		try {
			while (resultSet.next()) {
				procedureIDV.add(resultSet.getObject(1).toString());
				procedureNameV.add(resultSet.getObject(2).toString());
				procedureName.addElement(resultSet.getObject(2).toString());
				procedurePriceV.add(resultSet.getObject(3).toString());
				preDaysV.add(resultSet.getObject(7).toString());
				postDaysV.add(resultSet.getObject(8).toString());
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		procedureNameCB.setModel(procedureName);
		if (i > 0) {
			procedureNameCB.setSelectedIndex(0);
		}
	}
	public JButton getBtnUpdate() {
		return btnUpdate;
	}
	public JTextField getPostDaysTF() {
		return postDaysTF;
	}
	public JTextField getPreDaysTF() {
		return preDaysTF;
	}
	class EditableTableModel extends AbstractTableModel {
		String[] columnTitles;

		Object[][] dataEntries;

		int rowCount;

		public EditableTableModel(String[] columnTitles, Object[][] objectArray_ListOfexamsSpecs) {
			this.columnTitles = columnTitles;
			this.dataEntries = objectArray_ListOfexamsSpecs;
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

