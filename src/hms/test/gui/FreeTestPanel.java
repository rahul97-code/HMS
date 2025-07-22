package hms.test.gui;

import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamsBrowser;
import hms.main.DateFormatChange;

import javax.swing.border.BevelBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import com.toedter.calendar.JDateChooser;

public class FreeTestPanel extends JPanel {

	private JTable table;
	private JTable examTable;
	private JTextField searchField;
	String examID="",searchDate="";
	Test test;
	Vector originalTableModel;
	private JButton doneBTN;
	private JButton btnSearch;
	private JLabel TotalDayLBL;
	private JLabel todayCountLBL;
	private JLabel remainingLBL;
	private JLabel doneTestLBL;
	private JLabel courrentTokenLBL;
	Vector<Integer> tokenV=new Vector<>();
	int tokenNoIndex=0;
	private JCheckBox checkBox;
	private JButton notDoneBTN;

	public FreeTestPanel(final Test test) {
		setLayout(null);
		this.test=test;
		setBounds(6, 165, 1076, 502);
		setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Free Test Camp",
				TitledBorder.CENTER, TitledBorder.CENTER, new Font("Tahoma",
						Font.PLAIN, 13), null));
		JPanel panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Free Test Patient",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_2.setBounds(12, 74, 703, 275);
		add(panel_2);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 21, 681, 242);
		panel_2.add(scrollPane);

		table = new JTable();		
		table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// Check if the selection is not empty
				if (!e.getValueIsAdjusting()) {
					int selectedRow = table.getSelectedRow();
					if (selectedRow != -1) {
						String selectedData = (String) table.getValueAt(selectedRow, 1); 
						loadDataToTable(selectedData+"");                   
					}
				}
			}
		});


		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(new Object[][]{
			{null, null, null},
		}, new String[]{"Token No.", "Patient ID", "Patient Name", "Doctor", "Performed"}));
		table.getColumnModel().getColumn(1).setMinWidth(123);
		table.getColumnModel().getColumn(2).setMinWidth(200);
		table.getColumnModel().getColumn(3).setMinWidth(180);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setMinWidth(100);
		scrollPane.setViewportView(table);

		JPanel panel = new JPanel();
		panel.setBounds(727, 27, 337, 456);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Token Details",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		add(panel);
		panel.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(12, 22, 313, 106);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Current Token No",
				TitledBorder.CENTER, TitledBorder.BOTTOM, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.add(panel_1);
		panel_1.setLayout(null);

		JButton button = new JButton("");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tokenNoIndex>0)
					tokenNoIndex--;
				setTokenText(tokenNoIndex);
			}
		});
		button.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/2leftarrow_hover.png")));
		button.setBounds(23, 30, 52, 45);
		panel_1.add(button);

		JButton button_1 = new JButton("");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(tokenNoIndex<tokenV.size()-1)
					tokenNoIndex++;
				setTokenText(tokenNoIndex);
			}
		});
		button_1.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/2rightarrow_hover.png")));
		button_1.setBounds(235, 30, 52, 45);
		panel_1.add(button_1);

		JPanel panel_6 = new JPanel();
		panel_6.setBounds(81, 6, 154, 85);
		panel_6.setLayout(new GridBagLayout());

		courrentTokenLBL = new JLabel("");
		courrentTokenLBL.setBounds(12, 0, 110, 68);
		courrentTokenLBL.setFont(new Font("Dialog", Font.BOLD, 86));
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = 0;
		gbc.gridy = 0;
		panel_6.add(courrentTokenLBL, gbc);
		panel_1.add(panel_6);

		doneBTN = new JButton("Done");
		doneBTN.setEnabled(false);
		doneBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!examID.equals("")) {
					test.updateTestData(examID, "Yes");
					btnSearch.doClick();
					loadDataToTable("0");
					examID="";
					notDoneBTN.setEnabled(false);
					doneBTN.setEnabled(false);
				}else {
					JOptionPane.showMessageDialog(null, "Please Select Exam!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		doneBTN.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/ok_button.png")));
		doneBTN.setForeground(new Color(0, 153, 51));
		doneBTN.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		doneBTN.setBounds(178, 322, 136, 32);
		panel.add(doneBTN);

		notDoneBTN = new JButton("Not Done");
		notDoneBTN.setEnabled(false);
		notDoneBTN.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!examID.equals("")) {
					test.updateTestData(examID,"No");
					test.chckbxmntmShowPerformedTest.setSelected(false);
					btnSearch.doClick();
					loadDataToTable("0");
					examID="";
					notDoneBTN.setEnabled(false);
					doneBTN.setEnabled(false);
				}else {
					JOptionPane.showMessageDialog(null, "Please Select Exam!",
							"Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		notDoneBTN.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/delete_Hover.png")));
		notDoneBTN.setForeground(new Color(255, 51, 0));
		notDoneBTN.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 18));
		notDoneBTN.setBounds(18, 322, 148, 32);
		panel.add(notDoneBTN);

		JButton btnClose = new JButton("  Logout");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				test.dispose();
			}
		});
		btnClose.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 17));
		btnClose.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/exits.png")));
		btnClose.setBounds(85, 414, 176, 30);
		panel.add(btnClose);

		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(12, 382, 318, 14);
		panel.add(panel_3);

		JPanel panel_5 = new JPanel();
		panel_5.setBounds(12, 132, 313, 171);
		panel.add(panel_5);
		panel_5.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "",
				TitledBorder.CENTER, TitledBorder.BOTTOM, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_5.setLayout(null);

		JLabel lblTodayCount = new JLabel("Today Count");
		lblTodayCount.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblTodayCount.setBounds(26, 58, 84, 15);
		panel_5.add(lblTodayCount);

		doneTestLBL = new JLabel("0");
		doneTestLBL.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 42));
		doneTestLBL.setBounds(26, 92, 70, 45);
		panel_5.add(doneTestLBL);

		JLabel lblDayTotal = new JLabel("Day Total");
		lblDayTotal.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblDayTotal.setBounds(192, 58, 84, 15);
		panel_5.add(lblDayTotal);

		JLabel lblDoneTest = new JLabel("Done Test");
		lblDoneTest.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblDoneTest.setBounds(28, 142, 84, 15);
		panel_5.add(lblDoneTest);

		JLabel lblRemaining = new JLabel("Remaining Test");
		lblRemaining.setFont(new Font("Dialog", Font.ITALIC, 13));
		lblRemaining.setBounds(175, 142, 113, 15);
		panel_5.add(lblRemaining);

		remainingLBL = new JLabel("0");
		remainingLBL.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 42));
		remainingLBL.setBounds(204, 92, 70, 45);
		panel_5.add(remainingLBL);

		todayCountLBL = new JLabel("0");
		todayCountLBL.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 42));
		todayCountLBL.setBounds(26, 12, 70, 45);
		panel_5.add(todayCountLBL);

		TotalDayLBL = new JLabel(getDayLimit());
		TotalDayLBL.setFont(new Font("Dialog", Font.BOLD | Font.ITALIC, 42));
		TotalDayLBL.setBounds(204, 12, 70, 45);
		panel_5.add(TotalDayLBL);

		JPanel panel_4 = new JPanel();
		panel_4.setBounds(12, 27, 703, 41);
		add(panel_4);
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setLayout(null);

		JButton searchBT = new JButton("");
		searchBT.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/search.png")));
		searchBT.setToolTipText("Search");
		searchBT.setFocusable(true);
		searchBT.setBounds(659, 9, 28, 25);
		panel_4.add(searchBT);

		searchField = new JTextField();
		searchField.setToolTipText("Search Patient");
		searchField.setFont(new Font("Dialog", Font.PLAIN, 12));
		searchField.setColumns(10);
		searchField.setBounds(518, 9, 141, 25);
		panel_4.add(searchField);
		searchField.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str,checkBox.isSelected());
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str,checkBox.isSelected());
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchField.getText() + "";
						searchTableContents(str,checkBox.isSelected());
					}
				});

		JLabel lblSearchPatient = new JLabel("Search  :");
		lblSearchPatient.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSearchPatient.setBounds(456, 14, 63, 14);
		panel_4.add(lblSearchPatient);

		JDateChooser dateToDC = new JDateChooser();
		dateToDC.setDateFormatString("yyyy-MM-dd");
		dateToDC.setBounds(12, 9, 127, 25);
		panel_4.add(dateToDC);
		dateToDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							searchDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							populateTable(searchDate, searchDate);
						}
					}
				});
		dateToDC.setDate(new Date());		

		btnSearch = new JButton("Refresh");
		btnSearch.setIcon(new ImageIcon(FreeTestPanel.class.getResource("/icons/RELOAD.PNG")));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				populateTable(searchDate, searchDate);
			}
		});
		btnSearch.setFont(new Font("Dialog", Font.ITALIC, 12));
		btnSearch.setBounds(150, 9, 118, 25);
		panel_4.add(btnSearch);

		checkBox = new JCheckBox("");
		checkBox.setToolTipText("Only for token search.");
		checkBox.setBounds(412, 10, 28, 23);
		panel_4.add(checkBox);

		JLabel lblToken = new JLabel("T/S :");
		lblToken.setToolTipText("Only for token search.");
		lblToken.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblToken.setBounds(379, 14, 63, 14);
		panel_4.add(lblToken);
		JPanel panel_2_1 = new JPanel();
		panel_2_1.setLayout(null);
		panel_2_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Free Test Patient",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_2_1.setBounds(22, 355, 693, 128);
		add(panel_2_1);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(12, 21, 669, 95);
		panel_2_1.add(scrollPane_1);

		examTable = new JTable();
		examTable.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				try {
					notDoneBTN.setEnabled(true);
					doneBTN.setEnabled(true);
					int selectedRowIndex = examTable.getSelectedRow();
					Object selectedObject = examTable.getModel()
							.getValueAt(selectedRowIndex, 0);
					System.out.println("" + selectedObject);
					examID=selectedObject+"";
				} catch (Exception e2) {
					e2.printStackTrace();
				}
			}
		});
		examTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		examTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		examTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examTable.getTableHeader().setReorderingAllowed(false);
		examTable.setModel(new DefaultTableModel(new Object[][]{
			{null, null, null},
		}, new String[]{"Exam ID", "Exam Name", "Exam Date"}));
		examTable.getColumnModel().getColumn(1).setMinWidth(470);
		examTable.getColumnModel().getColumn(2).setMinWidth(120);
		scrollPane_1.setViewportView(examTable);
	}

	private String getDayLimit() {
		// TODO Auto-generated method stub
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveFreeTestDayLimit();
		try {
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			db.closeConnection();
		}
		return "??";	
	}

	public void populateTable(String dateFrom, String dateTo) {
		tokenV.clear();
		try {		
			AmountReceiptDBConnection db = new AmountReceiptDBConnection();
			ResultSet rs = db.retrieveAllExamsToken1(dateFrom, dateTo,test.exam_room);
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();
			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];
			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = (R + 1) + "";
				Rows_Object_Array[R][1] = rs.getObject(1);
				Rows_Object_Array[R][2] = rs.getObject(2);
				Rows_Object_Array[R][3] = rs.getObject(3);
				Rows_Object_Array[R][4] = rs.getObject(4);
				if(rs.getString(4).contains("No"))
					tokenV.add((R+1));
				R++;

			} 
			todayCountLBL.setText(R+"");
			doneTestLBL.setText((R-tokenV.size())+"");
			remainingLBL.setText(tokenV.size()+"");
			db.closeConnection();
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] {
							"Token No.", "Patient ID", "Patient Name", "Doctor", "Performed"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;
				}
			};
			table.setModel(model);
			table.getColumnModel().getColumn(1).setMinWidth(123);
			table.getColumnModel().getColumn(2).setMinWidth(200);
			table.getColumnModel().getColumn(3).setMinWidth(180);
			table.getColumnModel().getColumn(4).setPreferredWidth(100);
			table.getColumnModel().getColumn(4).setMinWidth(100);
			table.getColumnModel().getColumn(4).setCellRenderer(new CustomRenderer());
			originalTableModel = (Vector) ((DefaultTableModel) table.getModel())
					.getDataVector().clone();
			if(tokenV.size()>0) {
				courrentTokenLBL.setText(tokenV.get(0)+"");
				tokenNoIndex=0;
			}
			else {
				courrentTokenLBL.setText("");
			}
		} catch (SQLException ex) {
			Logger.getLogger(ExamsBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
	public void loadDataToTable(String pid)  {
		ExamDBConnection db = new ExamDBConnection();
		try {
			ResultSet rs = db.retrieveExamDataPatientID(pid, test.exam_room,test.testPerformed);
			int NumberOfColumns = 0, NumberOfRows = 0;

			NumberOfColumns = rs.getMetaData().getColumnCount();

			rs.last();
			NumberOfRows=rs.getRow();
			rs.beforeFirst();
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns+1];
			int R = 0;
			while (rs.next()) {
				Rows_Object_Array[R][0] = rs.getObject(1);
				Rows_Object_Array[R][1] = rs.getObject(2);
				Rows_Object_Array[R][2] = rs.getObject(3);
				R++;

			} 
			db.closeConnection();
			examTable.setModel(new DefaultTableModel(Rows_Object_Array,
					new String[] {"Exam ID", "Exam Name", "Exam Date"}) {
				boolean[] canEdit = new boolean[] { false, false, false };

				@Override
				public boolean isCellEditable(int rowIndex, int columnIndex) {
					return canEdit[columnIndex];
				}
			});
			examTable.getColumnModel().getColumn(1).setMinWidth(470);
			examTable.getColumnModel().getColumn(2).setMinWidth(120);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void setTokenText(int i){
		courrentTokenLBL.setText(tokenV.get(i)+"");
		selectRowByName(table,tokenV.get(i)+"");
	}

	public void searchTableContents(String searchString, boolean isCheckboxSelected) {
		DefaultTableModel currTableModel = (DefaultTableModel) table.getModel();
		currTableModel.setRowCount(0); 

		for (Object rows : originalTableModel) {
			Vector rowVector = (Vector) rows;
			boolean matchFound = false;
			if (isCheckboxSelected) {
				Object firstColumn = rowVector.get(0); 
				if (firstColumn.toString().toLowerCase().contains(searchString.toLowerCase())) {
					currTableModel.addRow(rowVector);
				}
			} else {
				for (Object column : rowVector) {
					if (column.toString().toLowerCase().contains(searchString.toLowerCase())) {
						currTableModel.addRow(rowVector);
						break; 
					}
				}
			}
		}
	}

	public static void selectRowByName(JTable table, String name) {
		for (int row = 0; row < table.getRowCount(); row++) {
			if (table.getValueAt(row,0).equals(name)) {
				table.setRowSelectionInterval(row, row); 

				Rectangle rect = table.getCellRect(row, 0, true);
				table.scrollRectToVisible(rect);
				break;
			}
		}
	}
	public class CustomRenderer extends DefaultTableCellRenderer 
	{
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			Component cellComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if(table.getValueAt(row, column)!=null)
			{
				if(table.getValueAt(row, column).equals("Yes")){
					cellComponent.setBackground(Color.GREEN);
				} else{
					cellComponent.setBackground(Color.RED);}
			}
			return cellComponent;
		}
	}
}

