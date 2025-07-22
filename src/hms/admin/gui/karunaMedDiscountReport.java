package hms.admin.gui;

import hms.main.DateFormatChange;
import hms.opd.database.OPDDBConnection;
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
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class karunaMedDiscountReport extends JDialog {

	public JPanel contentPane;
	private JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom,dateTo;
	private String content;
	double totalAmount=0;
	Vector originalTableModel;
	private JTextField searchField;
	Vector<String> id=new Vector<>();
	public JTextField textFieldCount = new JTextField();
	JPopupMenu contextMenu = new JPopupMenu();
	public static void main(String[] arg)
	{
		karunaMedDiscountReport browser=new karunaMedDiscountReport();
		browser.setVisible(true);
	}
	/**
	 * Create the frame.
	 */
	public karunaMedDiscountReport() {
//		JMenuItem search=new JMenuItem("  Search <<");
//		search.setFont(new Font("Dialog", Font.ITALIC, 12));
//		contextMenu.add(search);

		setResizable(false);
		setTitle("MedicalStore Karuna Discount");
		setIconImage(Toolkit.getDefaultToolkit().getImage(karunaMedDiscountReport.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1035, 524);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				int row=table.getSelectedRow();
				int col=table.getSelectedColumn();
				String value=table.getValueAt(row, col).toString();		
				System.out.println(value);
			}
		});
		
		scrollPane.setBounds(198, 11, 804, 418);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Double Click to reprint Bill Slip");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"OPD ID", "Patient Name", "Patient ID", "Receipt ID", "Doctor","Bill Amount","Discount Amount","Age","Gender","Mobile No"
				}
				));
		scrollPane.setViewportView(table);
		table.addMouseListener(new MouseListener() {

			
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
				        content=table.getValueAt(row, column)+"";
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
			
			      //  saveToDatabase(data);
			    
			private void doPop(MouseEvent e) {
				if (table.getSelectedRowCount() == 0) {
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
				if (arg0.getClickCount() == 1) {
					int row=table.getSelectedRow();
					int col=table.getSelectedColumn();
				table.editCellAt(row, 3);

				}
			}
		});
//		search.addActionListener(new ActionListener(){  
//			public void actionPerformed(ActionEvent e) {        
////				String str=""+table.getValueAt(table.getSelectedRow(), table.getSelectedColumn());
//				DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
//				// To empty the table before search
//				currtableModel.setRowCount(0);
//				System.out.println(content);
//				// To search for contents from original table content
//				for (Object rows : originalTableModel) {
//					Vector rowVector = (Vector) rows;
//					for (Object column : rowVector) {
//						if (column.toString().toLowerCase().equals(content.toLowerCase())) {
//							// content found so adding to table
//							currtableModel.addRow(rowVector);
//							break;
//						}
//					}
//				} 
//				countALLString();
//			}  
//		});

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(karunaMedDiscountReport.class.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setBounds(668, 441, 160, 35);
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
		lblCount.setBounds(12, 242, 70, 15);
		panel.add(lblCount);


		textFieldCount.setEditable(false);
		textFieldCount.setBounds(12, 269, 170, 28);
		panel.add(textFieldCount);
		textFieldCount.setColumns(10);
		textFieldCount.setText("");
		JButton btnExcel = new JButton("Excel");
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				try {
					new ExcelFile("Invoice Detail", table);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(karunaMedDiscountReport.class.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnExcel.setBounds(124, 440, 160, 35);
		contentPane.add(btnExcel);
		
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				OPDDBConnection OPDDBConnection=new OPDDBConnection();
				TableCellEditor celledit = table.getCellEditor();
				if(celledit!=null) {
					celledit.stopCellEditing();
				}
				for(int i=0;i<table.getRowCount();i++) {
					if (!id.get(i).equals("")) {
						try {
							System.out.println(id.get(i)+"      "+table.getValueAt(i, 3));
							OPDDBConnection.UpdateKarunaReceiptID(id.get(i), table.getValueAt(i, 3)+"");
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						} 
					}
				}
				OPDDBConnection.closeConnection();
			}
		});
		btnSave.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnSave.setBounds(363, 441, 160, 35);
		contentPane.add(btnSave);


		populateTable(DateFormatChange.StringToMysqlDate(new Date()),DateFormatChange.StringToMysqlDate(new Date()));
	}

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
		countALLString();
	}

	public void populateTable(String dateFrom,String dateTo)
	{
		double amt=0;

		try {
			OPDDBConnection db=new OPDDBConnection();
			ResultSet rs = db.retrieveAllKarunaMSDS(dateFrom, dateTo);

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
				for(int C=2; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-2] = rs.getObject(C)==null?"":rs.getObject(C);

				}
				id.add(rs.getString(1));
				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"OPD ID", "Patient Name", "Patient ID", "Receipt ID", "Doctor","Bill Amount","Discount Amount","Age","Gender","Mobile No"

			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					if(column==3)
						return true;
					else
						return false;
								
				}
			};
			table.setModel(model);
			if(NumberOfRows>0)
			{countALLString();
			}
			TableColumn columnsize = null;
			for (int i = 0; i < 6; i++) {
				columnsize = table.getColumnModel().getColumn(i);
				columnsize.setPreferredWidth(110); 
				if(i==1||i==2||i==3)
				{
					columnsize.setPreferredWidth(150); 
				}
				if(i==5)
					columnsize.setPreferredWidth(60); 
			}  
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			if(NumberOfRows>0)
			{
				get();
			}

		} catch (SQLException ex) {
			Logger.getLogger(karunaMedDiscountReport.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public void countALLString() {
		double amt=0;int r=0;
		// TODO Auto-generated method stub
		for(int i=0;i<table.getRowCount();i++)
		{
		
			r++;
		}
		if(table.getRowCount()>0)
		{

			textFieldCount.setText(r+"");
		}
	}
	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}

	private void handleRowClick(MouseEvent e) {
		ListSelectionModel selectionModel = table.getSelectionModel();
		Point contextMenuOpenedAt = e.getPoint();
		int clickedRow = table.rowAtPoint(contextMenuOpenedAt);

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
