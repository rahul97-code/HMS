package hms.store.gui;

import hms.main.DateFormatChange;
import hms.patient.slippdf.Bill_PDF;
import hms.store.database.InvoiceDBConnection;
import hms.test.free_test.ExcelFile;

import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

public class InvoiceBrowser extends JDialog {

	public JPanel contentPane;
	private JTable invoicebrowserTable;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private String content;
	double totalAmount=0;
	Vector originalTableModel;
	private JTextField searchField;
	public JTextField textFieldCount = new JTextField();
	public JTextField textFieldAmt  = new JTextField();
	JPopupMenu contextMenu = new JPopupMenu();
	public static void main(String[] arg)
	{
		InvoiceBrowser browser=new InvoiceBrowser();
		browser.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public InvoiceBrowser() {
		JMenuItem search=new JMenuItem("  Search <<");
		search.setFont(new Font("Dialog", Font.ITALIC, 12));
		contextMenu.add(search);

		setResizable(false);
		setTitle("Invoice List");
		setIconImage(Toolkit.getDefaultToolkit().getImage(InvoiceBrowser.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 950, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(198, 11, 726, 418);
		contentPane.add(scrollPane);

		invoicebrowserTable = new JTable();
		invoicebrowserTable.setToolTipText("Double Click to reprint Bill Slip");
		invoicebrowserTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		invoicebrowserTable.getTableHeader().setReorderingAllowed(false);
		invoicebrowserTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		invoicebrowserTable.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"ID", "Vendor Name", "Date", "Time", "Invoice No","Total"
				}
				));
		scrollPane.setViewportView(invoicebrowserTable);
		invoicebrowserTable.addMouseListener(new MouseListener() {

			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.isPopupTrigger()) {
					doPop(e);
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				 JTable source = (JTable)e.getSource();
				    int row = source.rowAtPoint(e.getPoint());
				    int column = source.columnAtPoint(e.getPoint());

				    if (!source.isRowSelected(row)) {
				        source.changeSelection(row, column, false, false);
				        content=invoicebrowserTable.getValueAt(row, column)+"";
				    }
				handleRowClick(e);
				if (e.isPopupTrigger()) {
					doPop(e);
				} else {
					hidePop();
				}
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
			private void doPop(MouseEvent e) {
				if (invoicebrowserTable.getSelectedRowCount() == 0) {
					return;
				}
				contextMenu.setVisible(true);
				contextMenu.show(e.getComponent(), e.getX(), e.getY());
			}

			private void hidePop() {
				contextMenu.setVisible(false);
			}
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getClickCount() == 2) {
					//  JTable target = (JTable)arg0.getSource();
					int row = invoicebrowserTable.getSelectedRow();
					int column = invoicebrowserTable.getSelectedColumn();
					// do some action

					Object selectedObject = invoicebrowserTable.getValueAt(row, 0);
					System.out.print("hdhdhdhdhdhdhdh"+selectedObject.toString());

					EditInvoice opdEntery=new EditInvoice(selectedObject.toString());

					opdEntery.setModal(true);
					opdEntery.setVisible(true);

				}
				if (arg0.getClickCount() == 1) {
					//  JTable target = (JTable)arg0.getSource();
					int row = invoicebrowserTable.getSelectedRow();
					int column = invoicebrowserTable.getSelectedColumn();
					// do some action
					
					Object selectedObject = invoicebrowserTable.getValueAt(row, 0);
					Object selectedObjectvendername = invoicebrowserTable.getValueAt(row, 1);
					try {
						new Bill_PDF(selectedObject+"",selectedObjectvendername+"");
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}
			}
		});
		search.addActionListener(new ActionListener(){  
			public void actionPerformed(ActionEvent e) {        
//				String str=""+invoicebrowserTable.getValueAt(invoicebrowserTable.getSelectedRow(), invoicebrowserTable.getSelectedColumn());
				DefaultTableModel currtableModel = (DefaultTableModel) invoicebrowserTable.getModel();
				// To empty the table before search
				currtableModel.setRowCount(0);
				System.out.println(content);
				// To search for contents from original table content
				for (Object rows : originalTableModel) {
					Vector rowVector = (Vector) rows;
					for (Object column : rowVector) {
						if (column.toString().toLowerCase().equals(content.toLowerCase())) {
							// content found so adding to table
							currtableModel.addRow(rowVector);
							break;
						}
					}
				} 
				countALLString();
			}  
		});  

		JButton btnNewButton = new JButton("New Invoice");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				NewInvoice opdEntery=new NewInvoice("","","");
				opdEntery.setModal(true);
				opdEntery.setVisible(true);
			}
		});
		btnNewButton.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/Business.png")));
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton.setBounds(294, 440, 160, 35);
		contentPane.add(btnNewButton);

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(473, 440, 160, 35);
		contentPane.add(btnNewButton_1);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 192, 418);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(4, 122, 178, 25);
		panel.add(dateFromDC);
		dateFromDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateFrom = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateFromDC.setDate(new Date());
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");


		dateToDC = new JDateChooser();
		dateToDC.setBounds(4, 177, 178, 25);
		panel.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							dateTo = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(dateFrom, dateTo);
						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(50, 158, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(50, 102, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchField = new JTextField();
		searchField.setBounds(4, 40, 178, 25);
		panel.add(searchField);
		searchField.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str);
					}
				});
		searchField.setColumns(10);

		JLabel lblSearch = new JLabel("Search :");
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearch.setBounds(62, 22, 70, 15);
		panel.add(lblSearch);

		JLabel lblCount = new JLabel("Count :");
		lblCount.setBounds(14, 224, 70, 15);
		panel.add(lblCount);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(12, 291, 111, 15);
		panel.add(lblTotalAmount);


		textFieldCount.setEditable(false);
		textFieldCount.setBounds(12, 251, 170, 28);
		panel.add(textFieldCount);
		textFieldCount.setColumns(10);
		textFieldCount.setText("");


		textFieldAmt.setEditable(false);
		textFieldAmt.setColumns(10);
		textFieldAmt.setBounds(12, 318, 170, 28); 
		panel.add(textFieldAmt);
		textFieldAmt.setText("");
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					new ExcelFile("Invoice Detail", invoicebrowserTable);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(InvoiceBrowser.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(124, 440, 160, 35);
		contentPane.add(btnExcel);


		populateTable(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) invoicebrowserTable.getModel();
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
		countALLString();
	}

	public void populateTable(String dateFrom,String dateTo)
	{
		double amt=0;

		try {
			InvoiceDBConnection db=new InvoiceDBConnection();
			ResultSet rs = db.retrieveAllData(dateFrom, dateTo);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while(rs.next()){
				NumberOfRows++;
			}
			System.out.println("rows : "+NumberOfRows);
			System.out.println("columns : "+NumberOfColumns);
			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);

				}

				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"ID", "Vendor Name", "Date", "Time", "Invoice No","Total"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			invoicebrowserTable.setModel(model);
			if(NumberOfRows>0)
			{countALLString();
			}
			TableColumn columnsize = null;
			for (int i = 0; i < 6; i++) {
				columnsize = invoicebrowserTable.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110); 
				if(i==1||i==2||i==3)
				{
					columnsize.setPreferredWidth(150); 
				}
				if(i==5)
					columnsize.setPreferredWidth(60); 
			}  
			originalTableModel = (Vector) ((DefaultTableModel) invoicebrowserTable.getModel())
					.getDataVector().clone();
			if(NumberOfRows>0)
			{
				get();
			}

		} catch (SQLException ex) {
			Logger.getLogger(InvoiceBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void countALLString() {
		double amt=0;int r=0;
		// TODO Auto-generated method stub
		for(int i=0;i<invoicebrowserTable.getRowCount();i++)
		{
			amt+=Double.parseDouble(!invoicebrowserTable.getValueAt(i, 5).toString().equals("")?invoicebrowserTable.getValueAt(i, 5).toString():0+"");
			r++;
		}
		if(invoicebrowserTable.getRowCount()>0)
		{

			textFieldCount.setText(r+"");
			textFieldAmt.setText(amt+"");
		}
	}
	private void get() {
		// TODO Auto-generated method stub
		invoicebrowserTable.setAutoCreateRowSorter(true);
	}

	private void handleRowClick(MouseEvent e) {
		ListSelectionModel selectionModel = invoicebrowserTable.getSelectionModel();
		Point contextMenuOpenedAt = e.getPoint();
		int clickedRow = invoicebrowserTable.rowAtPoint(contextMenuOpenedAt);

		if (clickedRow < 0) {
			// No row selected
			selectionModel.clearSelection();
		} else {
			// Some row selected
			if ((e.getModifiers() & InputEvent.SHIFT_MASK) == InputEvent.SHIFT_MASK) {
				int maxSelect = selectionModel.getMaxSelectionIndex();

				if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
					// Shift + CTRL
					selectionModel.addSelectionInterval(maxSelect, clickedRow);
				} else {
					// Shift
					selectionModel.setSelectionInterval(maxSelect, clickedRow);
				}
			} else if ((e.getModifiers() & InputEvent.CTRL_MASK) == InputEvent.CTRL_MASK) {
				// CTRL
				selectionModel.addSelectionInterval(clickedRow, clickedRow);
			} else {
				// No modifier key pressed
				selectionModel.setSelectionInterval(clickedRow, clickedRow);
			}
		}
	}


}
