package hms.reports.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.patient.slippdf.PO_PDF;
import hms.reports.excels.POExcel;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.PODBConnection;
import hms.store.gui.ItemIssueLogNEW;
import hms.test.free_test.ExcelFile;
import hms1.expenses.database.IPDExpensesDBConnection;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingUtilities;
import javax.swing.JComboBox;

public class OtShareSheet extends JDialog {

	public JPanel contentPane;
	private static JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo,insName="",docName="";
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "";
	private JTextField searchField;
	JCheckBox[] checkBoxes;
	Vector originalTableModel;
	final DefaultComboBoxModel docModal = new DefaultComboBoxModel();
	final DefaultComboBoxModel insModal = new DefaultComboBoxModel();
	private final List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
	Vector<String> uniqueWardSet = new Vector<>();
	Vector<String> examCodeUniqueV=new Vector<String>();

	Vector<String> CHANGE_ID=new Vector<>();
	Vector<String> OT_IPD_V=new Vector<>();
	Vector<String> OT_PRI_DOC_NAME=new Vector<>();
	Vector<String> OT_P_NAME=new Vector<>();
	Vector<String> OT_P_ID=new Vector<>();
	Vector<String> DOA=new Vector<>();
	Vector<String> OT_START_TIME=new Vector<>();
	Vector<String> OT_END_TIME=new Vector<>();
	Vector<String> OT_PER_CHARGES=new Vector<>();
	Vector<String> OT_CHARGES=new Vector<>();
	Vector<String> OT_TIME=new Vector<>();
	Vector<String> INSURANCE=new Vector<>();

	Vector<String> EXAM_IPD=new Vector<>();
	Vector<String> EXAM_PID=new Vector<>();
	Vector<String> EXAM_CODE=new Vector<>();
	Vector<String> EXAM_NAME=new Vector<>();
	Vector<String> EXAM_CHARGE=new Vector<>();
	Vector<String> EXAM_DOC=new Vector<>();

	Vector<String> EXP_IPD=new Vector<>();
	Vector<String> EXP_IMPLANT=new Vector<>();
	Vector<String> EXP_BED_CHARGES=new Vector<>();
	Vector<String> EXP_IPD_TOTAL=new Vector<>();
	Vector<String> EXP_HOSP_CHARG=new Vector<>();
	Vector<String> EXP_DISCOUNT=new Vector<>();

	Set<String> Docset = new HashSet<String>();
	Set<String> Wrdset = new HashSet<String>();
	Set<String> Insset = new HashSet<String>();
	TableRowSorter<DefaultTableModel> rowSorter;

	String[][] examsHeader;

	private JComboBox InsCB;
	private JComboBox DocCB;
	private JLabel wrdNo;



	public static void main(String[] arg) {
		new OtShareSheet().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public OtShareSheet() {
		setResizable(false);
		setTitle("OT Sheet");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OtShareSheet.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment()
				.getDefaultScreenDevice();
		int width = gd.getDisplayMode().getWidth();
		int height = gd.getDisplayMode().getHeight();
		System.out.println(width+"   "+height);
		setBounds(25, 10, width - 100, height - 120);
		//		setBounds(100, 100, 1270, 592);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(184, 42, this.getWidth()-200, this.getHeight()-120);
		contentPane.add(scrollPane);

		table = new JTable();
		//		table.setToolTipText("Double Click to reprint PO Slip");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				null, new String[] { "IPD No",
						"P ID", "P NAME", "INSURANCE","CONSULTANT", "DOA","WARD NAME","TIME DURATION" }));
		scrollPane.setViewportView(table);
		table.getColumnModel().getColumn(0).setMinWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(165);
		table.getColumnModel().getColumn(2).setMinWidth(165);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMinWidth(150);
		table.getColumnModel().getColumn(4).setPreferredWidth(165);
		table.getColumnModel().getColumn(4).setMinWidth(165);
		table.getColumnModel().getColumn(5).setPreferredWidth(165);
		table.getColumnModel().getColumn(5).setMinWidth(165);
		table.getColumnModel().getColumn(6).setPreferredWidth(165);
		table.getColumnModel().getColumn(6).setMinWidth(165);
		table.getColumnModel().getColumn(7).setPreferredWidth(165);
		table.getColumnModel().getColumn(7).setMinWidth(165);

		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

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
					//JTable target = (JTable) arg0.getSource();
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					// do some action



				}
				if (arg0.getClickCount() == 1) {
					//JTable target = (JTable) arg0.getSource();
					int row = table.getSelectedRow();
					int column = table.getSelectedColumn();
					// do some action


				}
			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(4, 11, 168, this.getHeight()-60);
		contentPane.add(panel);
		panel.setLayout(null);

		dateFromDC = new JDateChooser();
		dateFromDC.setBounds(20, 94, 128, 25);
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

						}
					}
				});
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DATE, -3);
		Date d = cal.getTime();
		dateFromDC.setDate(d);
		dateFromDC.setMaxSelectableDate(new Date());
		dateFromDC.setDateFormatString("yyyy-MM-dd");

		dateToDC = new JDateChooser();
		dateToDC.setBounds(20, 149, 128, 25);
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

						}
					}
				});
		dateToDC.setDate(new Date());
		dateToDC.setMaxSelectableDate(new Date());
		dateToDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblDateTo = new JLabel("DATE : TO");
		lblDateTo.setBounds(49, 130, 73, 14);
		panel.add(lblDateTo);
		lblDateTo.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDateFrom = new JLabel("DATE : From");
		lblDateFrom.setBounds(49, 74, 82, 14);
		panel.add(lblDateFrom);
		lblDateFrom.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchField = new JTextField();
		searchField.setBounds(4, 37, 152, 25);
		panel.add(searchField);
		searchField.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						applyFilters(rowSorter, InsCB,DocCB,searchField);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						applyFilters(rowSorter, InsCB,DocCB,searchField);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						applyFilters(rowSorter, InsCB,DocCB,searchField);
					}
				});
		searchField.setColumns(10);

		JLabel lblSearch = new JLabel("Search:");
		lblSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearch.setBounds(53, 22, 70, 15);
		panel.add(lblSearch);

		JButton btnSearch = new JButton("SEARCH");
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clearAllData();
				getAllDoctorsData();
				getAllOtExamsData();
				getAllIpdExpenseData();
				loadDataToTable();
			}
		});
		btnSearch.setIcon(new ImageIcon(OtShareSheet.class.getResource("/icons/search_hover.png")));
		btnSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnSearch.setBounds(23, 196, 120, 25);
		panel.add(btnSearch);

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(12, 389, 144, 25);
		panel.add(btnExcel);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("OTSheet.xls"));
				if (fileChooser.showSaveDialog(OtShareSheet.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					new ItemIssueLogNEW().ReportExcel(table, file.toPath().toString());
				}
				dispose();
			}
		});
		btnExcel.setIcon(new ImageIcon(OtShareSheet.class
				.getResource("/icons/1BL.PNG")));
		btnExcel.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JButton btnNewButton_1 = new JButton("Close");
		btnNewButton_1.setBounds(12, 426, 144, 25);
		panel.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_1.setIcon(new ImageIcon(OtShareSheet.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setIcon(new ImageIcon(OtShareSheet.class
				.getResource("/icons/CANCEL.PNG")));

		JButton btnNewButton = new JButton("Open Bill");
		btnNewButton.setIcon(new ImageIcon(OtShareSheet.class.getResource("/icons/PaySlip.png")));
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int row=table.getSelectedRow();
				String ipd_id=table.getValueAt(row, 1).toString();
				String doc=table.getValueAt(row, 6).toString();				
				if(ipd_id.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select ipd ID.",
							"Input Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					new IPDBillSlippdf("", ipd_id, doc
							,false);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(11, 517, 144, 25);
		panel.add(btnNewButton);

		System.out.println();
		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(198,this.getHeight()-75, 934, 32);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		wrdNo = new JLabel("");
		wrdNo.setBounds(162, 12, 70, 15);
		panel_1.add(wrdNo);

		JLabel lblWrdTime = new JLabel("Total Wrd time :");
		lblWrdTime.setBounds(12, 12, 133, 15);
		panel_1.add(lblWrdTime);



		InsCB = new JComboBox<String>();
		InsCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {		
				applyFilters(rowSorter, InsCB,DocCB,searchField);			
			}
		});
		InsCB.setBounds(244, 11, 213, 24);
		contentPane.add(InsCB);

		InsCB.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (index >= 0) {
					return checkBoxes[index];
				}
				return new JLabel("Select Insurance");
			}
		});

		InsCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int selectedIndex = InsCB.getSelectedIndex();
				if (selectedIndex >= 0) {
					checkBoxes[selectedIndex].setSelected(!checkBoxes[selectedIndex].isSelected());
					if (selectedIndex == 0) {
						boolean selectAll = checkBoxes[0].isSelected();
						for (int i = 1; i < checkBoxes.length; i++) {
							checkBoxes[i].setSelected(selectAll);
						}
					} else {
						boolean allSelected = true;
						for (int i = 1; i < checkBoxes.length; i++) {
							if (!checkBoxes[i].isSelected()) {
								allSelected = false;
								break;
							}
						}
						checkBoxes[0].setSelected(allSelected);
					}
					InsCB.repaint();
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							InsCB.showPopup();
						}
					});
				}
			}
		});

		InsCB.addPopupMenuListener(new PopupMenuListener() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				SwingUtilities.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (!InsCB.hasFocus()) {
							InsCB.hidePopup();
						}
					}
				});
			}

			@Override
			public void popupMenuCanceled(PopupMenuEvent e) {}
		});

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				InsCB.hidePopup();
			}
		});


		DocCB = new JComboBox();
		DocCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyFilters(rowSorter, InsCB,DocCB,searchField);
			}
		});
		DocCB.setBounds(536, 11, 213, 24);
		contentPane.add(DocCB);

		JLabel lblIns = new JLabel("Ins :");
		lblIns.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblIns.setBounds(207, 15, 43, 15);
		contentPane.add(lblIns);

		JLabel lblDoc = new JLabel("Doc :");
		lblDoc.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDoc.setBounds(475, 15, 43, 15);
		contentPane.add(lblDoc);

		btnSearch.doClick();

	}
	private void applyFilters(TableRowSorter<DefaultTableModel> rowSorter, JComboBox<String> insComboBox, JComboBox<String> docComboBox, JTextField searchField) {
//		List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();
//
//		String searchText = searchField.getText().trim();
//		if (!searchText.isEmpty()) {
//			filters.add(RowFilter.regexFilter("(?i)" + searchText));
//		}
//
//		List<RowFilter<Object, Object>> column5Filters = new ArrayList<RowFilter<Object, Object>>();
//		for (int i = 1; i < checkBoxes.length; i++) {
//			if (checkBoxes[i].isSelected()) {
//				System.out.println(checkBoxes[i].getText());
//				column5Filters.add(RowFilter.regexFilter(checkBoxes[i].getText(), 5));
//			}
//		}
//
//		if (!column5Filters.isEmpty()) {
//			filters.add(RowFilter.orFilter(column5Filters));
//		}
//
//		String doc = (String) docComboBox.getSelectedItem();
//		if (docComboBox.getSelectedIndex()!=0) {
//			filters.add(RowFilter.regexFilter(doc, 6));
//		}
//
//		RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
//		rowSorter.setRowFilter(combinedFilter);
//		table.setRowSorter(rowSorter);
	}

	public void countStringsAll()
	{
		double amt=0;
		if (table.getRowCount()>0) {
			for(int i=0;i<table.getRowCount();i++)
			{
				String[] d=table.getValueAt(i, 7).toString().split(".");
				for(int j=0;j<d.length;j++)
					System.out.println(d[j]);
			}
			wrdNo.setText(amt+"");
		}		
	}
	public void setComboBoxDta() {
		insModal.addElement("All");
		docModal.addElement("Select Doctor");
		if(docModal.getSize()>0) {
			for (String value : Insset) {
				insModal.addElement(value);
			}
			for (String value : Docset) {
				docModal.addElement(value);
			}
			InsCB.setModel(insModal);
			DocCB.setModel(docModal);
			checkBoxes = new JCheckBox[insModal.getSize()];
			for (int i = 0; i < insModal.getSize(); i++) {
				checkBoxes[i] = new JCheckBox(insModal.getElementAt(i).toString(), true);
			}
		}

	}
	private void loadDataToTable() {

		int totalMinutes=0;
		int size = OT_IPD_V.size();
		String[] uniqueHeadersArray = uniqueWardSet.toArray(new String[0]);
		double total = 0;
		Object[][] ObjectArray_ListOfexamsSpecs = new Object[size][uniqueHeadersArray.length];
		String doc="",ipd="";		
		for(int i=0;i<OT_IPD_V.size();i++) {
			ObjectArray_ListOfexamsSpecs[i][0] = i+1;
			ObjectArray_ListOfexamsSpecs[i][1] = OT_IPD_V.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = OT_P_ID.get(i);
			ObjectArray_ListOfexamsSpecs[i][3] = OT_P_NAME.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = DOA.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = INSURANCE.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = OT_PRI_DOC_NAME.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = OT_START_TIME.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = OT_END_TIME.get(i);
			ObjectArray_ListOfexamsSpecs[i][9] = OT_PER_CHARGES.get(i);
			ObjectArray_ListOfexamsSpecs[i][10] = OT_TIME.get(i);
			ObjectArray_ListOfexamsSpecs[i][11] = OT_CHARGES.get(i);

			int col=12;
			for(int k=0;k<EXAM_IPD.size();k++) {
				for(int j=0;j<examCodeUniqueV.size();j++) {
					if(OT_IPD_V.get(i).equals(EXAM_IPD.get(k))){
						if(EXAM_CODE.get(k).equals(examCodeUniqueV.get(j))) {
							ObjectArray_ListOfexamsSpecs[i][col+j]=EXAM_CHARGE.get(k);
							ObjectArray_ListOfexamsSpecs[i][(col+j)+1]=EXAM_DOC.get(k);
						}
					}
				}
			}
			col=col+examCodeUniqueV.size();
			for(int q=0;q<EXP_IPD.size();q++) {
				if(EXP_IPD.get(q).equals(OT_IPD_V.get(i))) {
					ObjectArray_ListOfexamsSpecs[i][col] = EXP_IMPLANT.get(q);
					ObjectArray_ListOfexamsSpecs[i][col+1] = EXP_BED_CHARGES.get(q);
					ObjectArray_ListOfexamsSpecs[i][col+2] = EXP_IPD_TOTAL.get(q);
					ObjectArray_ListOfexamsSpecs[i][col+3] = EXP_HOSP_CHARG.get(q);	
					ObjectArray_ListOfexamsSpecs[i][col+4] = EXP_DISCOUNT.get(q);
					break;
				}
			}

		}

		wrdNo.setText(totalMinutes+"");
		Docset.addAll(OT_PRI_DOC_NAME);
		//		Wrdset.addAll(FINAL_WARD);
		Insset.addAll(INSURANCE);
		Object[][] filteredArray = filterBlankRows(ObjectArray_ListOfexamsSpecs);
		DefaultTableModel m=new DefaultTableModel(filteredArray,uniqueHeadersArray);
		table.setModel(m);
		rowSorter=new TableRowSorter<DefaultTableModel>(m);
		table.setRowSorter(rowSorter);

		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment(JLabel.CENTER);
		table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(6).setCellRenderer(centerRenderer);
		table.getColumnModel().getColumn(7).setCellRenderer(centerRenderer);
		fitColumnsToContent(table);
		if(table.getRowCount()>0)
		{
			get();
			setComboBoxDta();
			countStringsAll();
		}

	}

	private static void fitColumnsToContent(JTable table) {
		TableColumnModel columnModel = table.getColumnModel();
		for (int col = 0; col < table.getColumnCount(); col++) {
			int maxWidth = 0;
			TableColumn column = columnModel.getColumn(col);
			TableCellRenderer headerRenderer = table.getTableHeader().getDefaultRenderer();
			Component headerComp = headerRenderer.getTableCellRendererComponent(table, column.getHeaderValue(), false, false, 0, col);
			maxWidth = headerComp.getPreferredSize().width;
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer cellRenderer = table.getCellRenderer(row, col);
				Component cellComp = table.prepareRenderer(cellRenderer, row, col);
				maxWidth = Math.max(maxWidth, cellComp.getPreferredSize().width);
			}
			column.setPreferredWidth(maxWidth + 10); 
		}
	}


	public static Object[][] filterBlankRows(Object[][] array) {
		ArrayList<Object[]> list = new ArrayList<>();
		for (Object[] row : array) {
			if (row != null && row.length > 0) {
				boolean isBlank = true;
				for (Object element : row) {
					if (element != null) {
						isBlank = false;
						break;
					}
				}
				if (!isBlank) {
					list.add(row);
				}
			}
		}
		return list.toArray(new Object[list.size()][]);
	}

	public void getAllDoctorsData() {
		DoctorDBConnection db=new DoctorDBConnection(); 
		ResultSet rs=db.getAllOtIpd(dateFrom, dateTo);
		try {
			while(rs.next()) {
				CHANGE_ID.add(rs.getString(1));
				OT_IPD_V.add(rs.getString(2));
				OT_P_ID.add(rs.getString(3));
				OT_P_NAME.add(rs.getString(4));
				DOA.add(rs.getString(5));
				INSURANCE.add(rs.getString(6));
				OT_PRI_DOC_NAME.add(rs.getString(7));
				OT_START_TIME.add(rs.getString(8));
				OT_END_TIME.add(rs.getString(9));
				OT_PER_CHARGES.add(rs.getString(10));
				OT_TIME.add(rs.getString(11));
				OT_CHARGES.add(rs.getString(12));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
	}
	public void getAllOtExamsData() {
		uniqueWardSet.add("SR. No");
		uniqueWardSet.add("IPD No");
		uniqueWardSet.add("P ID");
		uniqueWardSet.add("P NAME");
		uniqueWardSet.add("DOA");
		uniqueWardSet.add("INSURANCE");
		uniqueWardSet.add("PRI DOC");
		uniqueWardSet.add("START TIME");
		uniqueWardSet.add("END TIME");
		uniqueWardSet.add("OT AMT");
		uniqueWardSet.add("OT TIME");
		uniqueWardSet.add("OT CHARGES");

		int len=0;String header=null,codes=null;
		DoctorDBConnection db=new DoctorDBConnection(); 
		ResultSet rs=db.getAllPateintOTExams(dateFrom, dateTo);
		int d=1;
		try {
			while(rs.next()) {
				EXAM_IPD.add(rs.getString(1));
				EXAM_PID.add(rs.getString(2));
				String code=rs.getString(3);
				String name=rs.getString(4);
				if(uniqueWardSet.indexOf(name)==-1){
					examCodeUniqueV.add(code);
					examCodeUniqueV.add(d+"");
					uniqueWardSet.add(name);
					uniqueWardSet.add("DOC "+d);
					d++;
				}
				EXAM_CODE.add(code);
				EXAM_NAME.add(rs.getString(4));
				EXAM_CHARGE.add(rs.getString(5));
				EXAM_DOC.add(rs.getString(6));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
	}

	public void getAllIpdExpenseData() {
		uniqueWardSet.add("IMPLANT");
		uniqueWardSet.add("BED CHARGES");
		uniqueWardSet.add("IPD TOTAL");
		uniqueWardSet.add("HOSPITAL CHRG");
		uniqueWardSet.add("DISCOUNT");
		String ipdArr=getAllIpd();
		if (ipdArr.length()>0) {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllOtExpense(ipdArr);
			try {
				while (rs.next()) {
					EXP_IPD.add(rs.getString(1));
					EXP_IMPLANT.add(rs.getString(2));
					EXP_BED_CHARGES.add(rs.getString(3));
					EXP_IPD_TOTAL.add(rs.getString(4));
					EXP_HOSP_CHARG.add(rs.getString(5));
					EXP_DISCOUNT.add(rs.getString(6));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				db.closeConnection();
			} 
		}
	}
	public void clearAllData() {
		uniqueWardSet.clear();
		CHANGE_ID.clear();
		OT_IPD_V.clear();
		OT_PRI_DOC_NAME.clear();
		OT_P_NAME.clear();
		OT_P_ID.clear();
		DOA.clear();
		INSURANCE.clear();
		examCodeUniqueV.clear();

		EXAM_IPD.clear();
		EXAM_CHARGE.clear();
		EXAM_CODE.clear();
		EXAM_DOC.clear();
		EXAM_PID.clear();
		EXAM_NAME.clear();

		Docset.clear();
		Wrdset.clear();
		Insset.clear();

		EXP_IPD.clear();
		EXP_IMPLANT.clear();
		EXP_BED_CHARGES.clear();
		EXP_IPD_TOTAL.clear();
		EXP_HOSP_CHARG.clear();
		EXP_DISCOUNT.clear();

		insModal.removeAllElements();
		docModal.removeAllElements();
	}


	public String getAllIpd() {
		StringBuilder sb = new StringBuilder();
		for (int i=0;i<OT_IPD_V.size();i++)
		{
			if (OT_IPD_V.size() - 1 > i) {
				sb.append(OT_IPD_V.get(i)).append(",");
			} else {
				sb.append(OT_IPD_V.get(i));
			} 
		}
		return sb+"";
	}

	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}
}