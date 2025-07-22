package hms.reports.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.patient.slippdf.PO_PDF;
import hms.reports.excels.POExcel;
import hms.store.database.BatchTrackingDBConnection;
import hms.store.database.InvoiceDBConnection;
import hms.store.database.PODBConnection;
import hms.store.gui.ItemIssueLogNEW;
import hms.test.free_test.ExcelFile;

import java.awt.Component;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.itextpdf.text.DocumentException;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.JComboBox;

public class OtShareSheet3 extends JDialog {

	public JPanel contentPane;
	private static JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser dateToDC;
	private JDateChooser dateFromDC;
	String dateFrom, dateTo,wrdName="",insName="",docName="";
	double totalAmount = 0;
	String selectedPOId = "", selectedPONum = "", selectedVendName = "",
			selectedStatus = "";
	private JTextField searchField;
	Vector originalTableModel;
	final DefaultComboBoxModel docModal = new DefaultComboBoxModel();
	final DefaultComboBoxModel wrdModal = new DefaultComboBoxModel();
	final DefaultComboBoxModel insModal = new DefaultComboBoxModel();
	private final List<RowFilter<TableModel, Integer>> filters = new ArrayList<>();
	Vector<String> uniqueWardSet = new Vector<>();
	Vector<String> CHANGE_ID=new Vector<>();
	Vector<String> OT_IPD_V=new Vector<>();
	Vector<String> OT_PRI_DOC_NAME=new Vector<>();
	Vector<String> OT_P_NAME=new Vector<>();
	Vector<String> OT_P_ID=new Vector<>();
	Vector<String> DOA=new Vector<>();
	Vector<String> OT_NAME=new Vector<>();
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

	Vector<String> FINAL_WARD=new Vector<>();
	Vector<String> FINAL_IPD_ID=new Vector<>();
	Vector<String> FINAL_DOC_NAME=new Vector<>();
	Vector<String> FINAL_P_NAME=new Vector<>();
	Vector<String> FINAL_P_ID=new Vector<>();
	Vector<String> FINAL_DOA=new Vector<>();
	Vector<String> FINAL_INSURANCE=new Vector<>();
	
	Set<String> Docset = new HashSet<String>();
	Set<String> Wrdset = new HashSet<String>();
	Set<String> Insset = new HashSet<String>();
	TableRowSorter<DefaultTableModel> rowSorter;
	private JComboBox InsCB;
	private JComboBox WrdCB;
	private JComboBox DocCB;
	private JLabel wrdNo;



	public static void main(String[] arg) {
		new OtShareSheet3().setVisible(true);
	}

	/**
	 * Create the frame.
	 */
	public OtShareSheet3() {
		setResizable(false);
		setTitle("Conservative Sheet");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				OtShareSheet3.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1270, 592);
		contentPane = new JPanel();
		setModal(true);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(184, 42, 1074, 456);
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
		panel.setBounds(4, 11, 168, 531);
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
						applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);
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
				getAllBedsData();
				getFinalData();
//				for(int i=0;i<FINAL_DOC_NAME.size();i++) {
//					System.out.println(FINAL_IPD_ID.get(i)+"    "+FINAL_WARD.get(i)+"   "+FINAL_DOC_NAME.get(i)+"   "+FINAL_MINUTES.get(i));
//				}
				loadDataToTable();
			}
		});
		btnSearch.setIcon(new ImageIcon(OtShareSheet3.class.getResource("/icons/search_hover.png")));
		btnSearch.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnSearch.setBounds(23, 196, 120, 25);
		panel.add(btnSearch);

		JButton btnExcel = new JButton("Excel");
		btnExcel.setBounds(12, 389, 144, 25);
		panel.add(btnExcel);
		btnExcel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("ConservativeSheet.xls"));
				if (fileChooser.showSaveDialog(OtShareSheet3.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					// save to file
					new ItemIssueLogNEW().ReportExcel(table, file.toPath().toString());
				}
			}
		});
		btnExcel.setIcon(new ImageIcon(OtShareSheet3.class
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
		btnNewButton_1.setIcon(new ImageIcon(OtShareSheet3.class
				.getResource("/icons/CANCEL.PNG")));
		btnNewButton_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnNewButton_1.setIcon(new ImageIcon(OtShareSheet3.class
				.getResource("/icons/CANCEL.PNG")));

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(198, 510, 934, 32);
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
				applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);			
			}
		});
		InsCB.setBounds(244, 11, 213, 24);
		contentPane.add(InsCB);

		DocCB = new JComboBox();
		DocCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);
			}
		});
		DocCB.setBounds(536, 11, 213, 24);
		contentPane.add(DocCB);

		WrdCB = new JComboBox();
		WrdCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				applyFilters(rowSorter, InsCB, WrdCB,DocCB,searchField);
			}
		});
		WrdCB.setBounds(828, 11, 200, 24);
		contentPane.add(WrdCB);

		JLabel lblIns = new JLabel("Ins :");
		lblIns.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblIns.setBounds(207, 15, 43, 15);
		contentPane.add(lblIns);

		JLabel lblDoc = new JLabel("Doc :");
		lblDoc.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblDoc.setBounds(475, 15, 43, 15);
		contentPane.add(lblDoc);

		JLabel lblWrd = new JLabel("Wrd :");
		lblWrd.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblWrd.setBounds(767, 15, 43, 15);
		contentPane.add(lblWrd);


		btnSearch.doClick();

	}
	private static void applyFilters(TableRowSorter<DefaultTableModel> rowSorter, JComboBox<String> insComboBox, JComboBox<String> wrdComboBox,JComboBox<String> docComboBox,JTextField searchField) {
		List<RowFilter<Object, Object>> filters = new ArrayList<RowFilter<Object, Object>>();

		// Gender Filter
		String searchText = searchField.getText().trim();
		if (!searchText.isEmpty()) {
			filters.add(RowFilter.regexFilter("(?i)" + searchText));
		}
		String ins = (String) insComboBox.getSelectedItem();
		if (!"select".equals(ins)) {
			filters.add(RowFilter.regexFilter(ins, 3));
		}
		String wrd = (String) wrdComboBox.getSelectedItem();
		if (!"select".equals(wrd)) {
			filters.add(RowFilter.regexFilter(wrd, 6));
		}
		String doc = (String) docComboBox.getSelectedItem();
		if (!"select".equals(doc)) {
			filters.add(RowFilter.regexFilter(doc, 4));
		}
		// Apply combined filters
		RowFilter<Object, Object> combinedFilter = RowFilter.andFilter(filters);
		rowSorter.setRowFilter(combinedFilter);
		table.setRowSorter(rowSorter);
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
		insModal.addElement("select");
		wrdModal.addElement("select");
		docModal.addElement("select");
		if(docModal.getSize()>0) {
			for (String value : Insset) {
				insModal.addElement(value);
			}
			for (String value : Wrdset) {
				wrdModal.addElement(value);
			}
			for (String value : Docset) {
				docModal.addElement(value);
			}
			WrdCB.setModel(wrdModal);
			InsCB.setModel(insModal);
			DocCB.setModel(docModal);
		}

	}
	private void loadDataToTable() {
		int totalMinutes=0;
		int size = FINAL_IPD_ID.size();
		String[] uniqueHeadersArray = uniqueWardSet.toArray(new String[0]);
		double total = 0;
		Object[][] ObjectArray_ListOfexamsSpecs = new Object[size][uniqueHeadersArray.length];
		String doc="",ipd="";int k=-1;
		for (int i = 0; i < FINAL_IPD_ID.size(); i++) {

			if(FINAL_IPD_ID.get(i).equals(ipd) && FINAL_DOC_NAME.get(i).equals(doc)) {
				for(int j=6;j<uniqueHeadersArray.length;j++) {
					if(FINAL_WARD.get(i).equals(uniqueHeadersArray[j]))
						ObjectArray_ListOfexamsSpecs[k][j] = convertMinutes(FINAL_MINUTES.get(i));
					totalMinutes+=FINAL_MINUTES.get(i);
				}
			}else {
				ipd=FINAL_IPD_ID.get(i);doc=FINAL_DOC_NAME.get(i);k=i;
				ObjectArray_ListOfexamsSpecs[i][0] = FINAL_IPD_ID.get(i);
				ObjectArray_ListOfexamsSpecs[i][1] = FINAL_P_ID.get(i);
				ObjectArray_ListOfexamsSpecs[i][2] = FINAL_P_NAME.get(i);
				ObjectArray_ListOfexamsSpecs[i][3] = FINAL_INSURANCE.get(i);
				ObjectArray_ListOfexamsSpecs[i][4] = FINAL_DOC_NAME.get(i);
				ObjectArray_ListOfexamsSpecs[i][5] = FINAL_DOA.get(i);
				for(int j=6;j<uniqueHeadersArray.length;j++) {
					if(FINAL_WARD.get(i).equals(uniqueHeadersArray[j])){
						ObjectArray_ListOfexamsSpecs[i][j] = convertMinutes(FINAL_MINUTES.get(i));
						totalMinutes+=FINAL_MINUTES.get(i);
					}
					else
						ObjectArray_ListOfexamsSpecs[i][j] = "0";
				}
			}
		}
		wrdNo.setText(totalMinutes+"");
		Docset.addAll(OT_PRI_DOC_NAME);
		Wrdset.addAll(FINAL_WARD);
		Insset.addAll(INSURANCE);
		Object[][] filteredArray = filterBlankRows(ObjectArray_ListOfexamsSpecs);
		DefaultTableModel m=new DefaultTableModel(filteredArray
				,uniqueHeadersArray);
		table.setModel(m);
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

		if(table.getRowCount()>0)
		{
			get();
			setComboBoxDta();
			countStringsAll();
		}

	}

	public static String convertMinutes(int totalMinutes) {
		int days = totalMinutes / (24 * 60);
		int hours = (totalMinutes % (24 * 60)) / 60;
		return days+"."+hours;
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
	public void getFinalData() {
		for(int b=0;b<EXAM_IPD.size();b++) {
			for(int d=0;d<OT_IPD_V.size();d++) {
				if ((EXAM_IPD.get(b).equals(OT_IPD_V.get(d))) && (BED_MINUTES.get(b)>0 && 0<DOC_MINUTES.get(d))) {
					if (BED_MINUTES.get(b)>DOC_MINUTES.get(d)) {
						FINAL_WARD.add(WARD.get(b));
						FINAL_IPD_ID.add(OT_IPD_V.get(d));
						FINAL_DOC_NAME.add(OT_PRI_DOC_NAME.get(d));
						FINAL_P_NAME.add(OT_P_NAME.get(d));
						FINAL_P_ID.add(OT_P_ID.get(d));
						FINAL_DOA.add(DOA.get(d));
						FINAL_INSURANCE.add(INSURANCE.get(d));
						FINAL_MINUTES.add(DOC_MINUTES.get(d));	
						BED_MINUTES.set(b,BED_MINUTES.get(b)-DOC_MINUTES.get(d));
						DOC_MINUTES.set(d,0);
					}else if(BED_MINUTES.get(b)<DOC_MINUTES.get(d)) {
						FINAL_WARD.add(WARD.get(b));
						FINAL_IPD_ID.add(OT_IPD_V.get(d));
						FINAL_DOC_NAME.add(OT_PRI_DOC_NAME.get(d));
						FINAL_P_NAME.add(OT_P_NAME.get(d));
						FINAL_P_ID.add(OT_P_ID.get(d));
						FINAL_DOA.add(DOA.get(d));
						FINAL_INSURANCE.add(INSURANCE.get(d));
						FINAL_MINUTES.add(BED_MINUTES.get(b));	
						BED_MINUTES.set(b,0);
						DOC_MINUTES.set(d,DOC_MINUTES.get(d)-BED_MINUTES.get(b));
						break;
					}else {
						FINAL_WARD.add(WARD.get(b));
						FINAL_IPD_ID.add(OT_IPD_V.get(d));
						FINAL_DOC_NAME.add(OT_PRI_DOC_NAME.get(d));
						FINAL_P_NAME.add(OT_P_NAME.get(d));
						FINAL_P_ID.add(OT_P_ID.get(d));
						FINAL_DOA.add(DOA.get(d));
						FINAL_INSURANCE.add(INSURANCE.get(d));
						FINAL_MINUTES.add(BED_MINUTES.get(b));	
						BED_MINUTES.set(b,0);
						DOC_MINUTES.set(d,0);
						break;
					}
				}
			}	
		}

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
				OT_NAME.add(rs.getString(8));
				OT_PER_CHARGES.add(rs.getString(9));
				OT_TIME.add(rs.getString(10));
				OT_CHARGES.add(rs.getString(11));
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
	}
	public void getAllBedsData() {
		uniqueWardSet.add("IPD No");
		uniqueWardSet.add("P ID");
		uniqueWardSet.add("P NAME");
		uniqueWardSet.add("INSURANCE");
		uniqueWardSet.add("PRI DOC");
		uniqueWardSet.add("DOA");

		DoctorDBConnection db=new DoctorDBConnection(); 
		ResultSet rs=db.getAllPateintOTExams(dateFrom, dateTo);
		try {
			while(rs.next()) {
				EXAM_IPD.add(rs.getString(1));
				EXAM_PID.add(rs.getString(2));
				EXAM_CODE.add(rs.getString(3));
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
	public void clearAllData() {
		uniqueWardSet.clear();
		CHANGE_ID.clear();
		OT_IPD_V.clear();
		OT_PRI_DOC_NAME.clear();
		OT_P_NAME.clear();
		OT_P_ID.clear();
		DOA.clear();
		INSURANCE.clear();
		DOC_MINUTES.clear();

		EXAM_IPD.clear();
		EXAM_CHARGE.clear();
		EXAM_CODE.clear();
		EXAM_DOC.clear();
		EXAM_PID.clear();
		EXAM_NAME.clear();

		FINAL_WARD.clear();
		FINAL_IPD_ID.clear();
		FINAL_DOC_NAME.clear();
		FINAL_P_NAME.clear();
		FINAL_P_ID.clear();
		FINAL_DOA.clear();
		FINAL_INSURANCE.clear();
		FINAL_MINUTES.clear();

		Docset.clear();
		Wrdset.clear();
		Insset.clear();
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