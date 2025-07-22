package hms.exams.gui;

import hms.exam.database.ReferenceTableDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class AddTestReferences extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private JTextField examSubTB;
	private JTextField examNameTB;
	private JTextField lowerLimitTB;
	private JTextField upperLimitTB;
	private JTextField unitsTB;
	Object[] ObjectArray_examSub;
	Object[] ObjectArray_patientsex;
	Object[] ObjectArray_patinettype;
	Object[] ObjectArray_minAge;
	Object[] ObjectArray_maxAge;
	Object[] ObjectArray_lowerLimit;
	Object[] ObjectArray_upperLimit;
	Object[] ObjectArray_units;
	Object[] ObjectArray_comments;
	Object[][] ObjectArray_ListOfexamsSpecs;
	Vector<String> patientV = new Vector<String>();
	Vector<String> examSubV = new Vector<String>();
	Vector<String> patientTypeV = new Vector<String>();
	Vector<String> minAgeV = new Vector<String>();
	Vector<String> maxAgeV = new Vector<String>();
	Vector<String> lowerLimitV = new Vector<String>();
	Vector<String> upperLimitV = new Vector<String>();
	Vector<String> unitsV = new Vector<String>();
	Vector<String> commentsV = new Vector<String>();
	private JTextArea commentsTA;
	ButtonGroup sexgroup = new ButtonGroup();
	String examNameSTR="",examSubNameSTR="",examCodeSTR="",patientSexSTR="Both",patientTypeSTR="Normal",minAgeSTR="0",maxAgeSTR="100",lowerLimitSTR="",upperLimitSTR="",unitSTR="",commentSTR="";
	private JButton btnAdd;
	private JButton btnRemove;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AddTestReferences dialog = new AddTestReferences("abc","1");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AddTestReferences(String examName,String examCode) {
		setTitle("Save Reference Ranges For Test");
		setIconImage(Toolkit.getDefaultToolkit().getImage(AddTestReferences.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 587, 468);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		examCodeSTR=examCode;
		examNameSTR=examName;
		setModal(true);
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 11, 554, 127);
		contentPanel.add(scrollPane);
		
		table = new JTable();
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
					"Sub Name","Patient Sex", "Patient Type", "Min Age", "Max Age", "Lower Limit", "Upper Limit", "Units", "Comments"
			}
		));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		table.getColumnModel().getColumn(1).setPreferredWidth(65);
		table.getColumnModel().getColumn(3).setPreferredWidth(55);
		table.getColumnModel().getColumn(4).setPreferredWidth(55);
		table.getColumnModel().getColumn(8).setPreferredWidth(150);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		scrollPane.setViewportView(table);
		
		JLabel lblExamId = new JLabel("Sub Name : ");
		lblExamId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamId.setBounds(10, 232, 68, 14);
		contentPanel.add(lblExamId);
		
		examSubTB = new JTextField();
		examSubTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examSubTB.setBounds(94, 227, 179, 25);
		contentPanel.add(examSubTB);
		examSubTB.setColumns(10);
		
		btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				if(examSubTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter exam sub name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(lowerLimitTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter lower limit", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				else if(upperLimitTB.getText().toString().equals("")){
					JOptionPane.showMessageDialog(null,
							"Please enter upper limit", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}else if(unitsTB.getText().toString().equals("")){
					JOptionPane.showMessageDialog(null,
							"Please enter units", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				examSubNameSTR=examSubTB.getText().toString();
				lowerLimitSTR=lowerLimitTB.getText().toString();
				upperLimitSTR=upperLimitTB.getText().toString();
				unitSTR=unitsTB.getText().toString();
				commentSTR=commentsTA.getText().toString();
				boolean i=false;
						i=examSubV.contains(examSubNameSTR);
				//if((!minAgeV.contains(minAgeSTR)||!patientV.contains(patientSexSTR))||(!maxAgeV.contains(maxAgeSTR)||!patientV.contains(patientSexSTR)))
				{
					examSubV.add(examSubNameSTR);
					patientV.add(patientSexSTR);
					patientTypeV.add(patientTypeSTR);
					minAgeV.add(minAgeSTR);
					maxAgeV.add(maxAgeSTR);
					lowerLimitV.add(lowerLimitSTR);
					upperLimitV.add(upperLimitSTR);
					unitsV.add(unitSTR);
					commentsV.add(commentSTR);
					loadDataToTable();
				}
//				else {
//					JOptionPane.showMessageDialog(null,
//							"Duplicate patient type and age range not allowed", "Input Error",
//							JOptionPane.ERROR_MESSAGE);
//				}
			}
		});
		btnAdd.setBounds(371, 146, 89, 25);
		contentPanel.add(btnAdd);
		
		btnRemove = new JButton("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = table.getSelectedRow();
				cur_selectedRow = table
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = table.getModel()
						.getValueAt(cur_selectedRow, 0).toString();
				System.out.print(cur_selectedRow+"   row index");
				examSubV.remove(cur_selectedRow);
				patientV.remove(cur_selectedRow);
				patientTypeV.remove(cur_selectedRow);
				minAgeV.remove(cur_selectedRow);
				maxAgeV.remove(cur_selectedRow);
				lowerLimitV.remove(cur_selectedRow);
				upperLimitV.remove(cur_selectedRow);
				unitsV.remove(cur_selectedRow);
				commentsV.remove(cur_selectedRow);
				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(470, 146, 89, 25);
		contentPanel.add(btnRemove);
		
		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(10, 195, 74, 14);
		contentPanel.add(lblExamName);
		
		examNameTB = new JTextField(examNameSTR);
		examNameTB.setEditable(false);
		examNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNameTB.setBounds(94, 190, 179, 25);
		contentPanel.add(examNameTB);
		examNameTB.setColumns(10);
		
		JSeparator separator = new JSeparator();
		separator.setBackground(Color.WHITE);
		separator.setBounds(10, 177, 546, 2);
		contentPanel.add(separator);
		
		JRadioButton maleRB = new JRadioButton("Male");
		
		maleRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patientSexSTR = "Male";
			}
		});
		
		maleRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maleRB.setBounds(152, 253, 51, 23);
		contentPanel.add(maleRB);
		sexgroup.add(maleRB);
		
		JRadioButton rdbtnBoth = new JRadioButton("Both");
		rdbtnBoth.setFont(new Font("Tahoma", Font.PLAIN, 12));
		rdbtnBoth.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patientSexSTR = "Both";
			}
		});
		rdbtnBoth.setBounds(94, 253, 58, 23);
		rdbtnBoth.setSelected(true);
		sexgroup.add(rdbtnBoth);
		
		contentPanel.add(rdbtnBoth);
		
		JRadioButton femaleRB = new JRadioButton("Female");
		femaleRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patientSexSTR = "Female";
			}
		});
		femaleRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		femaleRB.setBounds(205, 253, 68, 23);
		contentPanel.add(femaleRB);
		sexgroup.add(femaleRB);
		
		JLabel lblPatientType = new JLabel("Patient Type :");
		lblPatientType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPatientType.setBounds(10, 257, 80, 14);
		contentPanel.add(lblPatientType);
		
		final JComboBox minAgeCB = new JComboBox();
		minAgeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) minAgeCB.getSelectedItem();
				if(str.equals("Years"))
				{
					minAgeSTR = "0";
				}
				else {
					minAgeSTR = str;
				}
				
			}
		});
		minAgeCB.setModel(new DefaultComboBoxModel(new String[] {"Years", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100"}));
		minAgeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minAgeCB.setBounds(94, 312, 179, 25);
		contentPanel.add(minAgeCB);
		
		final JComboBox maxAgeCB = new JComboBox();
		maxAgeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) maxAgeCB.getSelectedItem();
				if(str.equals("Years"))
				{
					maxAgeSTR = "0";
				}
				else {
					maxAgeSTR = str;
					System.out.print(""+maxAgeSTR);
				}
				
			}
		});
		maxAgeCB.setModel(new DefaultComboBoxModel(new String[] {"Years", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "100"}));
		maxAgeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxAgeCB.setBounds(94, 348, 179, 25);
		contentPanel.add(maxAgeCB);
		
		JLabel lblMinAge = new JLabel("Min Age :");
		lblMinAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMinAge.setBounds(10, 317, 66, 14);
		contentPanel.add(lblMinAge);
		
		JLabel lblMaxAge = new JLabel("Max Age :");
		lblMaxAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMaxAge.setBounds(10, 353, 68, 14);
		contentPanel.add(lblMaxAge);
		
		lowerLimitTB = new JTextField();
		lowerLimitTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lowerLimitTB.setColumns(10);
		lowerLimitTB.setBounds(394, 191, 165, 25);
		contentPanel.add(lowerLimitTB);
		
		JLabel lblLowerLimit = new JLabel("Lower Limit :");
		lblLowerLimit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblLowerLimit.setBounds(304, 196, 80, 14);
		contentPanel.add(lblLowerLimit);
		
		upperLimitTB = new JTextField();
		upperLimitTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		upperLimitTB.setColumns(10);
		upperLimitTB.setBounds(394, 227, 165, 25);
		contentPanel.add(upperLimitTB);
		
		JLabel lblUpperLimit = new JLabel("Upper Limit :");
		lblUpperLimit.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUpperLimit.setBounds(304, 232, 74, 14);
		contentPanel.add(lblUpperLimit);
		
		unitsTB = new JTextField();
		unitsTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		unitsTB.setColumns(10);
		unitsTB.setBounds(394, 259, 165, 25);
		contentPanel.add(unitsTB);
		
		JLabel lblUnits = new JLabel("Units :");
		lblUnits.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblUnits.setBounds(304, 264, 68, 14);
		contentPanel.add(lblUnits);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Comments", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(300, 295, 264, 83);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 252, 60);
		panel.add(scrollPane_1);
		
		commentsTA = new JTextArea();
		commentsTA.setRows(5);
		scrollPane_1.setViewportView(commentsTA);
		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 if (patientTypeV.size()==0) {//itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add some data",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					String[] data = new String[20];
					ReferenceTableDBConnection referenceTableDBConnection=new ReferenceTableDBConnection();
					data[0] = examCodeSTR;
					data[1] = examNameSTR;
					for (int i = 0; i < ObjectArray_patientsex.length; i++) {
						data[2] = (String) ObjectArray_examSub[i];
						data[3] = (String) ObjectArray_patientsex[i];
						data[4] = (String) ObjectArray_patinettype[i];
						data[5] = (String) ObjectArray_minAge[i];
						data[6]= (String) ObjectArray_maxAge[i];
						data[7]= (String) ObjectArray_lowerLimit[i];
						data[8]= (String) ObjectArray_upperLimit[i];
						data[9]= (String) ObjectArray_units[i];
						data[10]= (String) ObjectArray_comments[i];	
					}
					try {
						referenceTableDBConnection.inserData(data);
						JOptionPane.showMessageDialog(null, "Saved Successfully",
								"Save Data", JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnSave.setBounds(152, 384, 121, 35);
		contentPanel.add(btnSave);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(295, 384, 121, 35);
		contentPanel.add(btnCancel);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) comboBox.getSelectedItem();
				patientTypeSTR=str;
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Normal", "Pregnant"}));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(94, 278, 179, 25);
		contentPanel.add(comboBox);
		
		JLabel lblExamId_1 = new JLabel("Exam ID : "+examCode);
		lblExamId_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExamId_1.setBounds(10, 149, 263, 20);
		contentPanel.add(lblExamId_1);
	}
	
	private void loadDataToTable() {
		// get size of the hashmap
		int size = patientV.size();
		System.out.print(size+" ");
		// declare two arrays one for key and other for keyValues
		ObjectArray_examSub = new Object[size];
		ObjectArray_patientsex = new Object[size];
		ObjectArray_patinettype = new Object[size];
		ObjectArray_minAge = new Object[size];
		ObjectArray_maxAge = new Object[size];
		ObjectArray_lowerLimit = new Object[size];
		ObjectArray_upperLimit = new Object[size];
		ObjectArray_units = new Object[size];
		ObjectArray_comments = new Object[size];
		ObjectArray_ListOfexamsSpecs = new Object[size][9];

		for (int i = 0; i < patientV.size(); i++) {
			ObjectArray_examSub[i] = examSubV.get(i);
			ObjectArray_patientsex[i] = patientV.get(i);
			ObjectArray_patinettype[i] = patientTypeV.get(i);
			ObjectArray_minAge[i]= minAgeV.get(i);
			ObjectArray_maxAge[i] = maxAgeV.get(i);
			ObjectArray_lowerLimit[i] = lowerLimitV.get(i);
			ObjectArray_upperLimit[i] = upperLimitV.get(i);
			ObjectArray_units[i] = unitsV.get(i);
			ObjectArray_comments[i] = commentsV.get(i);
			
			ObjectArray_ListOfexamsSpecs[i][0] = examSubV.get(i);
			ObjectArray_ListOfexamsSpecs[i][1] = patientV.get(i);
			ObjectArray_ListOfexamsSpecs[i][2] = patientTypeV.get(i);
			ObjectArray_ListOfexamsSpecs[i][3]= minAgeV.get(i);
			ObjectArray_ListOfexamsSpecs[i][4] = maxAgeV.get(i);
			ObjectArray_ListOfexamsSpecs[i][5] = lowerLimitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][6] = upperLimitV.get(i);
			ObjectArray_ListOfexamsSpecs[i][7] = unitsV.get(i);
			ObjectArray_ListOfexamsSpecs[i][8] = commentsV.get(i);

		}
		table.setModel(new DefaultTableModel(
				ObjectArray_ListOfexamsSpecs,
				new String[] {
					"Sub Name","Patient Sex", "Patient Type", "Min Age", "Max Age", "Lower Limit", "Upper Limit", "Units", "Comments"
				}
			));
			table.getColumnModel().getColumn(1).setPreferredWidth(65);
			table.getColumnModel().getColumn(3).setPreferredWidth(55);
			table.getColumnModel().getColumn(4).setPreferredWidth(55);
			table.getColumnModel().getColumn(8).setPreferredWidth(150);
	}
}
