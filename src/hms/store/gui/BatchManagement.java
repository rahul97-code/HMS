package hms.store.gui;

import hms.main.DateFormatChange;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.ItemsDBConnection;
import hms.store.database.SuppliersDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;



import javax.swing.border.LineBorder;



public class BatchManagement extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField searchItemTF;
	private JTextField itemDescTF;
	private JTextField BatchStockTF;
	private JTextField TotalStockTF;
	private JTable table;
	private Timer timer;
	private Timer timer1;

	int stk[];
	int c1=0,c2=0,c3=0,c4=0;
	final DefaultComboBoxModel supplierName = new DefaultComboBoxModel();
	final DefaultComboBoxModel itemName = new DefaultComboBoxModel();
	final DefaultComboBoxModel measUnit = new DefaultComboBoxModel();

	Vector<String> itemID = new Vector<String>();
	Vector<String> batchIDV = new Vector<String>();
	Vector<String> itemIDS = new Vector<String>();
	Vector<String> AllitemIDS = new Vector<String>();
	Vector<Integer> itemIDSTK = new Vector<Integer>();
	Vector<String> itemBTCHSTK = new Vector<String>();
	Vector<String> itemIDV = new Vector<String>();
	Vector<String> itemNameV = new Vector<String>();
	Vector<String> itemDescV = new Vector<String>();
	Vector<String> itemStockV = new Vector<String>();
	Vector<String> qtyV = new Vector<String>();
	Vector<String> itemDateV = new Vector<String>();
	Vector<String> itemBatchStk = new Vector<String>();
	Vector<String> itemTotalStk = new Vector<String>();
	Vector<String> itemNameStk = new Vector<String>();
	Vector<String> totalValueV = new Vector<String>();
	Vector<String> expiryDateV = new Vector<String>();
	Vector<String> batchNumberV = new Vector<String>();

	String itemIDSTR, itemNameSTR, itemDescSTR="",
			expiryDateSTR = "", invoiceDateSTR = "";

	double 
	discountValue = 0, itemValue, 
	finalDiscountValue = 0,
	surchargeAmountValue = 0, taxAmountValue = 0,
	discountPrcntValue = 0;
	int quantity = 0, freeQuantity = 0, paidQuantity = 0,BatchTotal=0,itemTotalStock=0;
	String mrp = "";
	Object[][] ObjectArray_ListOfexamsSpecs;
	Object[][] ObjectArray_ListOfexamsSpecs1;
	private JComboBox itemNameCB;
	ButtonGroup paymentOption = new ButtonGroup();
	private JTextField correctTF;
	private JTextField IncorrectTF;
	private JLabel lblNewLabel_1;
	private JTable table_1;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			BatchManagement dialog = new BatchManagement();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public BatchManagement() {
		setTitle("Stock Adjustment");
		setResizable(false);
		setBounds(100, 70, 980, 557);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		searchItemTF = new JTextField();
		searchItemTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		searchItemTF.setBounds(10, 87, 119, 20);
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
					BatchStockTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					TotalStockTF.setText("");
					itemDateV.clear();
					batchNumberV.clear();
					itemStockV.clear();
					batchIDV.clear();
					expiryDateV.clear();
					loadDataToTable();
					// measUnitCD.setModel(measUnit);

				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					BatchStockTF.setText("");
					TotalStockTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemDateV.clear();
					batchNumberV.clear();
					batchIDV.clear();
					itemStockV.clear();
					expiryDateV.clear();
					loadDataToTable();

					// measUnitCD.setModel(measUnit);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = searchItemTF.getText() + "";
				if (!str.equals("")) {
					getItemName(str);
				} else {
					itemDescTF.setText("");
					BatchStockTF.setText("");
					TotalStockTF.setText("");
					itemName.removeAllElements();
					itemNameCB.setModel(itemName);
					itemDateV.clear();
					batchNumberV.clear();
					itemStockV.clear();
					batchIDV.clear();
					expiryDateV.clear();
					loadDataToTable();

					// measUnitCD.setModel(measUnit);
				}
			}
		});

		itemDescTF = new JTextField();
		itemDescTF.setEditable(false);
		itemDescTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemDescTF.setBounds(304, 87, 163, 20);
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
				System.out.println(itemNameCB.getSelectedIndex() + "    "
						+ itemID.size());
				if (itemNameCB.getSelectedIndex() > -1) {
					itemIDSTR = itemID.get(itemNameCB.getSelectedIndex());
				}

				getItemDesc(itemIDSTR);
				getItemBatch(itemIDSTR);
				loadDataToTable();
				if (itemName.getSize() > 0) {
					itemDescTF.setText("" + itemDescSTR);
					if(BatchTotal!=itemTotalStock)
					{
						BatchStockTF.setText(""+BatchTotal );
						BatchStockTF.setForeground(Color.red);
						TotalStockTF.setText(""+itemTotalStock);
						TotalStockTF.setForeground(Color.red);
					}else
					{
						BatchStockTF.setText(""+BatchTotal );
						BatchStockTF.setForeground(Color.blue);
						TotalStockTF.setText(""+itemTotalStock);
						TotalStockTF.setForeground(Color.blue);
					}	
				}else
				{
					itemDescTF.setText("");
					BatchStockTF.setText("");
					TotalStockTF.setText("");
				}
			}


		});

		itemNameCB.setFont(new Font("Tahoma", Font.BOLD, 11));
		itemNameCB.setBounds(139, 87, 153, 20);
		contentPanel.add(itemNameCB);

		BatchStockTF = new JTextField();
		BatchStockTF.setEditable(false);
		BatchStockTF.setHorizontalAlignment(SwingConstants.RIGHT);
		BatchStockTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		BatchStockTF.setBounds(20, 145, 125, 20);
		contentPanel.add(BatchStockTF);
		BatchStockTF.setColumns(10);
		BatchStockTF.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				char vChar = e.getKeyChar();
				if (!(Character.isDigit(vChar)
						|| (vChar == KeyEvent.VK_BACK_SPACE)
						|| (vChar == KeyEvent.VK_DELETE) || vChar == '.')) {
					e.consume();

					// ||vChar== '.'
				}
			}
		});

		TotalStockTF = new JTextField();
		TotalStockTF.setEditable(false);
		TotalStockTF.setHorizontalAlignment(SwingConstants.RIGHT);
		TotalStockTF.setFont(new Font("Tahoma", Font.BOLD, 11));
		TotalStockTF.setBounds(157, 145, 106, 20);
		contentPanel.add(TotalStockTF);
		TotalStockTF.setColumns(10);

		JLabel lblNewLabel_2 = new JLabel("Total Stock :");
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblNewLabel_2.setBounds(157, 119, 86, 14);
		contentPanel.add(lblNewLabel_2);

		JLabel lblSearch = new JLabel("Search Item");
		lblSearch.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblSearch.setBounds(10, 62, 119, 14);
		contentPanel.add(lblSearch);

		JLabel lblDescription = new JLabel("Select Item :");
		lblDescription.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblDescription.setBounds(139, 62, 153, 14);
		contentPanel.add(lblDescription);

		JLabel lblMeasUnits = new JLabel("Item Description :");
		lblMeasUnits.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblMeasUnits.setBounds(304, 62, 163, 14);
		contentPanel.add(lblMeasUnits);

		JLabel lblUnitPrice = new JLabel("Total Batch Stock :");
		lblUnitPrice.setFont(new Font("Tahoma", Font.BOLD, 11));
		lblUnitPrice.setBounds(20, 119, 145, 14);
		contentPanel.add(lblUnitPrice);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 177, 523, 277);
		contentPanel.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setFont(new Font("Tahoma", Font.BOLD, 11));
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Item ID", "Item Name", "Item Batch", "Batch Qty.", "Expiry"
				}
				));
		table.getColumnModel().getColumn(0).setMinWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(115);
		table.getColumnModel().getColumn(1).setMinWidth(115);
		table.getColumnModel().getColumn(2).setPreferredWidth(95);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.getColumnModel().getColumn(3).setMinWidth(120);
		table.getColumnModel().getColumn(4).setMinWidth(90);
		scrollPane.setViewportView(table);

		JSeparator separator = new JSeparator();
		separator.setBounds(10, 46, 468, 2);
		contentPanel.add(separator);

		JButton btnNewButton_1 = new JButton("Update");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (searchItemTF.getText().equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please Search Atlest one item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemTotalStock>BatchTotal) {
					JOptionPane.showMessageDialog(null,
							"Please Add more batches of this item", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (itemTotalStock==BatchTotal) {
					JOptionPane.showMessageDialog(null, "Batch Stock Already Updated",
							"Batches Updated", JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				StockUpdateLogic();
				getStockUpdate();



				long timeInMillis = System.currentTimeMillis();
				Calendar cal1 = Calendar.getInstance();
				cal1.setTimeInMillis(timeInMillis);
				SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
				String[] datanew = new String[5];
				BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
				for (int i = 0; i < batchNumberV.size(); i++) {

					datanew[0] = itemIDSTR;
					datanew[1] = batchNumberV.get(i);
					datanew[2] = expiryDateV.get(i);
					datanew[3] = batchIDV.get(i);
					datanew[4] = stk[i]+"";
					try {
						batchTrackingDBConnection.UpdateData(datanew);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
				batchTrackingDBConnection.closeConnection();
				JOptionPane.showMessageDialog(null, "Batch Stock Update successfully",
						"Batches Update", JOptionPane.INFORMATION_MESSAGE);
				getItemName(itemIDSTR);
				getCOUNTALL();
				//dispose();
			}


		});

		lblNewLabel_1 = new JLabel("Loading ...");
		lblNewLabel_1.setForeground(UIManager.getColor("windowText"));
		lblNewLabel_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_1.setBounds(304, 147, 86, 15);
		contentPanel.add(lblNewLabel_1);
		lblNewLabel_1.setVisible(false);

		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNewButton_1.setBounds(217, 476, 153, 39);
		contentPanel.add(btnNewButton_1);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnCancel.setBounds(382, 476, 153, 39);
		contentPanel.add(btnCancel);

		JLabel lblStockAdjustment = new JLabel("Batch Stock Adjustment");
		lblStockAdjustment.setFont(new Font("Tahoma", Font.BOLD, 18));
		lblStockAdjustment.setBounds(10, 11, 429, 24);
		contentPanel.add(lblStockAdjustment);

		correctTF = new JTextField();
		correctTF.setEditable(false);
		correctTF.setFont(new Font("Dialog", Font.BOLD, 11));
		correctTF.setColumns(10);
		correctTF.setBounds(684, 38, 106, 20);
		contentPanel.add(correctTF);

		IncorrectTF = new JTextField();
		IncorrectTF.setEditable(false);
		IncorrectTF.setFont(new Font("Dialog", Font.BOLD, 11));
		IncorrectTF.setColumns(10);
		IncorrectTF.setBounds(547, 38, 125, 20);
		contentPanel.add(IncorrectTF);

		timer1 = new Timer(1, new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				lblNewLabel_1.setVisible(true);
				lblNewLabel_1.setText("loading .");
				if (timer.isRunning()) {
					timer.stop();
				}
			}

		});
		timer = new Timer(1, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				UpdateAll();
				lblNewLabel_1.setVisible(false);
				if (timer.isRunning()) {
					timer.stop();
				}
			}

		});


		JLabel lblCorrect = new JLabel("Correct :");
		lblCorrect.setBounds(684, 11, 70, 15);
		contentPanel.add(lblCorrect);

		JLabel lblNewLabel = new JLabel("Incorrect :");
		lblNewLabel.setBounds(547, 12, 103, 15);
		contentPanel.add(lblNewLabel);

		JButton btnCount = new JButton("Check");
		btnCount.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				getCOUNTALL();
			}
		});
		btnCount.setBounds(802, 35, 106, 25);
		contentPanel.add(btnCount);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(547, 70, 421, 445);
		contentPanel.add(scrollPane_1);

		table_1 = new JTable();
		table_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		table_1.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table_1.setBounds(0, 0, 1, 1);
		//contentPanel.add(table_1);
		table_1.setModel(new DefaultTableModel(new Object[][] {}, new String[]  { "Item ID", "Item Name", "Batch Stock","Total Stock"}));
		table_1.getColumnModel().getColumn(0).setMinWidth(88);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(130);
		table_1.getColumnModel().getColumn(1).setMinWidth(130);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(2).setMinWidth(100);
		table_1.getColumnModel().getColumn(3).setMinWidth(100);

		scrollPane_1.setViewportView(table_1);

		JButton btnNewButton_1_1 = new JButton("Update All");
		btnNewButton_1_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getCOUNTALL();
				int input = JOptionPane.showConfirmDialog(null, "Are you sure to Update All Batch Items?\n It may take too much time");
				if(input==0)
				{
					lblNewLabel_1.setVisible(true);
					timer.start();

				}
			}
		});
		btnNewButton_1_1.setFont(new Font("Dialog", Font.BOLD, 15));
		btnNewButton_1_1.setBounds(54, 476, 153, 39);
		contentPanel.add(btnNewButton_1_1);



	}
	private void UpdateAll()
	{
		for(int j=0;j<AllitemIDS.size();j++)
		{
			getItemDesc(AllitemIDS.get(j));
			getItemBatch(AllitemIDS.get(j));	
			StockUpdateLogic();
			System.out.print("arun :"+AllitemIDS.get(j));
			getStockUpdate();

			String[] datanew = new String[5];
			BatchTrackingDBConnection batchTrackingDBConnection = new BatchTrackingDBConnection();
			for (int i = 0; i < batchIDV.size(); i++) {

				datanew[0] = AllitemIDS.get(j);
				datanew[1] = "";
				datanew[2] = "";
				datanew[3] = batchIDV.get(i);
				datanew[4] = stk[i]+"";
				try {
					batchTrackingDBConnection.UpdateData(datanew);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
			batchTrackingDBConnection.closeConnection();
		}
		getCOUNTALL();
		JOptionPane.showMessageDialog(null, "All Batch Stock Updated successfully",
				"Batches Updated", JOptionPane.INFORMATION_MESSAGE);
	}

	private void getCOUNTALL() {
		// TODO Auto-generated method stub
		BatchTrackingDBConnection db = new BatchTrackingDBConnection();
		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet rs = itemsDBConnection.retrievetAllItemsForCheck();
		itemIDS.clear();
		itemNameStk.clear();;
		itemBatchStk.clear();
		itemTotalStk.clear();
		try {
			while(rs.next())
			{
				itemIDS.add(rs.getObject(1).toString());
				itemNameStk.add(rs.getObject(2).toString());
				itemBatchStk.add(rs.getObject(3).toString());
				itemTotalStk.add(rs.getObject(4).toString());
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();


		}
		loadDataToTable1();
		db.closeConnection();
	}


	public void getItemName(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.searchItemWithIdOrNmae(index);
		itemName.removeAllElements();
		itemID.clear();
		itemDescV.clear();
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

	public int getItemDesc(String index) {

		ItemsDBConnection itemsDBConnection = new ItemsDBConnection();
		ResultSet resultSet = itemsDBConnection.itemDetail(index);
		itemTotalStock=0;
		try {
			while (resultSet.next()) {
				itemDescSTR=resultSet.getObject(3).toString();
				itemTotalStock=Integer.parseInt(resultSet.getObject(8).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		itemsDBConnection.closeConnection();
		return itemTotalStock;
	}
	private void getStockUpdate() {
		// TODO Auto-generated method stub
		for(int i=0;i<stk.length;i++)
		{

			System.out.println("rrrr"+stk[i]);
		}
	}
	public void getItemBatch(String index) {
		BatchTrackingDBConnection db = new BatchTrackingDBConnection();
		ResultSet resultSet = db.retrievetBatchTotalStock1(index);

		batchNumberV.clear();
		itemStockV.clear();
		batchIDV.clear();
		expiryDateV.clear();

		try {
			while (resultSet.next()) {
				batchIDV.add(resultSet.getObject(1).toString());
				batchNumberV.add(resultSet.getObject(2).toString());
				itemStockV.add(resultSet.getObject(3).toString());
				expiryDateV.add(resultSet.getObject(4).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}



	private void loadDataToTable() {
		// get size of the hashmap
		int size = batchNumberV.size();

		int total = 0;
		ObjectArray_ListOfexamsSpecs = new Object[size][5];

		for (int i = 0; i < batchNumberV.size(); i++) {

			ObjectArray_ListOfexamsSpecs[i][0] = itemIDSTR;
			ObjectArray_ListOfexamsSpecs[i][1] = itemNameSTR;
			ObjectArray_ListOfexamsSpecs[i][2] = batchNumberV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = itemStockV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = expiryDateV.get(i);

			if(Integer.parseInt(itemStockV.get(i))>0)
			{
				total = total + Integer.parseInt(itemStockV.get(i));
			}

		}
		table.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs,
				new String[] { "Item ID", "Item Name", "Item Batch","Batch Qty.","Expiry"}));

		table.getColumnModel().getColumn(0).setMinWidth(70);
		table.getColumnModel().getColumn(1).setPreferredWidth(115);
		table.getColumnModel().getColumn(1).setMinWidth(115);
		table.getColumnModel().getColumn(2).setPreferredWidth(95);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.getColumnModel().getColumn(3).setMinWidth(120);
		table.getColumnModel().getColumn(4).setMinWidth(90);


		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);


		BatchTotal = total;
	}
	private void loadDataToTable1() {
		// get size of the hashmap
		int size = itemIDS.size();
		c1=c2=0;
		int total = 0;

		ObjectArray_ListOfexamsSpecs1 = new Object[size][4];

		for (int i = 0; i < itemIDS.size(); i++) {

			ObjectArray_ListOfexamsSpecs1[i][0] = itemIDS.get(i);
			ObjectArray_ListOfexamsSpecs1[i][1] = itemNameStk.get(i);
			ObjectArray_ListOfexamsSpecs1[i][2] = itemBatchStk.get(i);
			ObjectArray_ListOfexamsSpecs1[i][3] = itemTotalStk.get(i);

			if((int)Double.parseDouble(itemBatchStk.get(i))!=(int)Double.parseDouble(itemTotalStk.get(i)))
			{
				if((int)Double.parseDouble(itemBatchStk.get(i))>(int)Double.parseDouble(itemTotalStk.get(i)))
				{
					AllitemIDS.add(itemIDS.get(i));
				}
				c1++;
			}else {c2++;}

		}
		IncorrectTF.setText(c1+"");
		correctTF.setText(c2+"");
		table_1.setModel(new DefaultTableModel(ObjectArray_ListOfexamsSpecs1,
				new String[] { "Item ID", "Item Name", "Batch Stock","Item Total Stock"}));

		table_1.getColumnModel().getColumn(0).setMinWidth(40);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(150);
		table_1.getColumnModel().getColumn(1).setMinWidth(100);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(100);
		table_1.getColumnModel().getColumn(2).setMinWidth(100);
		table_1.getColumnModel().getColumn(3).setMinWidth(100);

		table_1.setDefaultRenderer(Object.class, new RedYellowRenderer());

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);


	}

	private void StockUpdateLogic() {
		// TODO Auto-generated method stub
		int size=0;
		size = itemStockV.size();
		stk=new int[size];
		for (int i = 0; i < batchNumberV.size(); i++) {
			stk[i]=Integer.parseInt(itemStockV.get(i));}
		for(int i=0;i<batchNumberV.size();i++)
		{

			if (stk[i] < itemTotalStock && stk[i] != 0 && stk[i]>0) {
				itemTotalStock = itemTotalStock - stk[i];
			}else if (stk[i] >= itemTotalStock && stk[i] != 0) {
				stk[i]=itemTotalStock;
				itemTotalStock=0;
			}else
			{
				stk[i]=0;
			}
		}
	}
	class RedYellowRenderer extends DefaultTableCellRenderer {
		RedYellowRenderer() {
			setHorizontalAlignment(CENTER);
		}
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column
				) {
			Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column
					);
			//System.out.print(table.getValueAt(row, 5)+" arun ");
			if(Double.parseDouble(table.getValueAt(row, 2)+"")!=(Double.parseDouble(table.getValueAt(row, 3)+"")) && table.getRowCount() > 1) {
				c.setBackground(Color.red);
				c.setForeground(Color.black);
				if(Double.parseDouble(table.getValueAt(row, 2)+"")<(Double.parseDouble(table.getValueAt(row, 3)+"")) && table.getRowCount() > 1)
				{
					c.setBackground(Color.cyan);
					c.setForeground(Color.black);
				}
			}else {
				c.setBackground(Color.green);
				c.setForeground(Color.black);
			}


			return c;
		}
	}
}	

