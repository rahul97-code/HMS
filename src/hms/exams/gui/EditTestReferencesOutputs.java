package hms.exams.gui;

import hms.exam.database.ReferenceTableDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

public class EditTestReferencesOutputs extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField examSubTB;
	private JTextField examNameTB;
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

	Vector<String> phyVector = new Vector<String>();

	Vector<String> chemVector = new Vector<String>();

	Vector<String> mehVector = new Vector<String>();
	String[] phy_paraSTR=new String[10];
	String[] chem_paraSTR=new String[10];
	String[] meh_paraSTR=new String[10];
	
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
	String phy_paraSTR1 = "0", phy_paraSTR2 = "0", phy_paraSTR3 = "0",
			phy_paraSTR4 = "0", phy_paraSTR5 = "0", phy_paraSTR6 = "0",
			phy_paraSTR7 = "0", phy_paraSTR8 = "0", phy_paraSTR9 = "0",
			phy_paraSTR10 = "0";
	String chem_paraSTR1 = "0", chem_paraSTR2 = "0", chem_paraSTR3 = "0",
			chem_paraSTR4 = "0", chem_paraSTR5 = "0", chem_paraSTR6 = "0",
			chem_paraSTR7 = "0", chem_paraSTR8 = "0", chem_paraSTR9 = "0",
			chem_paraSTR10 = "0";
	String meh_paraSTR1 = "0", meh_paraSTR2 = "0", meh_paraSTR3 = "0",
			meh_paraSTR4 = "0", meh_paraSTR5 = "0", meh_paraSTR6 = "0",
			meh_paraSTR7 = "0", meh_paraSTR8 = "0", meh_paraSTR9 = "0",
			meh_paraSTR10 = "0";
	String examNameSTR = "", examSubNameSTR = "", examCodeSTR = "",
			patientSexSTR = "Both", patientTypeSTR = "Normal", minAgeSTR = "0",
			maxAgeSTR = "100", commentSTR = "";
	private JTextField phy_pataTB2;
	private JTextField phy_pataTB3;
	private JTextField phy_pataTB4;
	private JTextField phy_pataTB5;
	private JTextField phy_pataTB6;
	private JTextField phy_pataTB7;
	private JTextField phy_pataTB8;
	private JTextField phy_pataTB1;
	private JTextField chem_paraTB1;
	private JTextField chem_paraTB2;
	private JTextField chem_paraTB3;
	private JTextField chem_paraTB4;
	private JTextField chem_paraTB5;
	private JTextField chem_paraTB6;
	private JTextField chem_paraTB7;
	private JTextField chem_paraTB8;
	private JTextField meh_paraTB1;
	private JTextField meh_paraTB2;
	private JTextField meh_paraTB3;
	private JTextField meh_paraTB4;
	private JTextField meh_paraTB5;
	private JTextField meh_paraTB6;
	private JTextField meh_paraTB7;
	private JTextField meh_paraTB8;
	private JTextField phy_pataTB9;
	private JTextField chem_paraTB9;
	private JTextField meh_paraTB9;
	private JTextField meh_paraTB10;
	private JTextField chem_paraTB10;
	private JTextField phy_pataTB10;
	private JCheckBox chckbxPhysical;
	private JCheckBox chckbxChemical;
	private JCheckBox chckbxMexamhpf;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			EditTestReferencesOutputs dialog = new EditTestReferencesOutputs(
					"abc", "1");
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public EditTestReferencesOutputs(String examName, String examCode) {
		setTitle("Save Reference Ranges For Test");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				EditTestReferencesOutputs.class
						.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 587, 599);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		examCodeSTR = examCode;
		examNameSTR = examName;
		setModal(true);
		JLabel lblExamId = new JLabel("Sub Name : ");
		lblExamId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamId.setBounds(10, 94, 68, 14);
		contentPanel.add(lblExamId);

		examSubTB = new JTextField();
		examSubTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examSubTB.setBounds(94, 89, 179, 25);
		contentPanel.add(examSubTB);
		examSubTB.setColumns(10);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(10, 57, 74, 14);
		contentPanel.add(lblExamName);

		examNameTB = new JTextField(examNameSTR);
		examNameTB.setEditable(false);
		examNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		examNameTB.setBounds(94, 52, 179, 25);
		contentPanel.add(examNameTB);
		examNameTB.setColumns(10);

		JRadioButton maleRB = new JRadioButton("Male");

		maleRB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				patientSexSTR = "Male";
			}
		});

		maleRB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maleRB.setBounds(152, 115, 51, 23);
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
		rdbtnBoth.setBounds(94, 115, 58, 23);
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
		femaleRB.setBounds(205, 115, 68, 23);
		contentPanel.add(femaleRB);
		sexgroup.add(femaleRB);

		JLabel lblPatientType = new JLabel("Patient Type :");
		lblPatientType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblPatientType.setBounds(10, 119, 80, 14);
		contentPanel.add(lblPatientType);

		final JComboBox minAgeCB = new JComboBox();
		minAgeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) minAgeCB.getSelectedItem();
				if (str.equals("Years")) {
					minAgeSTR = "0";
				} else {
					minAgeSTR = str;
				}

			}
		});
		minAgeCB.setModel(new DefaultComboBoxModel(new String[] { "Years", "1",
				"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
				"24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
				"34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
				"44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
				"54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
				"64", "65", "66", "67", "68", "69", "70", "71", "72", "73",
				"74", "75", "76", "77", "78", "79", "80", "81", "82", "83",
				"84", "85", "86", "87", "88", "89", "90", "91", "92", "93",
				"94", "95", "96", "97", "98", "99", "100" }));
		minAgeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		minAgeCB.setBounds(367, 11, 179, 25);
		contentPanel.add(minAgeCB);

		final JComboBox maxAgeCB = new JComboBox();
		maxAgeCB.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) maxAgeCB.getSelectedItem();
				if (str.equals("Years")) {
					maxAgeSTR = "0";
				} else {
					maxAgeSTR = str;
					System.out.print("" + maxAgeSTR);
				}

			}
		});
		maxAgeCB.setModel(new DefaultComboBoxModel(new String[] { "Years", "1",
				"2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13",
				"14", "15", "16", "17", "18", "19", "20", "21", "22", "23",
				"24", "25", "26", "27", "28", "29", "30", "31", "32", "33",
				"34", "35", "36", "37", "38", "39", "40", "41", "42", "43",
				"44", "45", "46", "47", "48", "49", "50", "51", "52", "53",
				"54", "55", "56", "57", "58", "59", "60", "61", "62", "63",
				"64", "65", "66", "67", "68", "69", "70", "71", "72", "73",
				"74", "75", "76", "77", "78", "79", "80", "81", "82", "83",
				"84", "85", "86", "87", "88", "89", "90", "91", "92", "93",
				"94", "95", "96", "97", "98", "99", "100" }));
		maxAgeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		maxAgeCB.setBounds(367, 47, 179, 25);
		contentPanel.add(maxAgeCB);

		JLabel lblMinAge = new JLabel("Min Age :");
		lblMinAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMinAge.setBounds(283, 16, 66, 14);
		contentPanel.add(lblMinAge);

		JLabel lblMaxAge = new JLabel("Max Age :");
		lblMaxAge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMaxAge.setBounds(283, 52, 68, 14);
		contentPanel.add(lblMaxAge);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Comments",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(283, 89, 264, 83);
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
				String selectedType1="0",selectedType2="0",selectedType3="0";
				if (examSubTB.getText().equals("")) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null, "Please add some data",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					if (chckbxPhysical.isSelected()) {
						if (!phy_pataTB1.getText().toString().equals(""))
							phy_paraSTR1 = phy_pataTB1.getText().toString();
						if (!phy_pataTB2.getText().toString().equals(""))
							phy_paraSTR2 = phy_pataTB2.getText().toString();

						if (!phy_pataTB3.getText().toString().equals(""))
							phy_paraSTR3 = phy_pataTB3.getText().toString();

						if (!phy_pataTB4.getText().toString().equals(""))
							phy_paraSTR4 = phy_pataTB4.getText().toString();

						if (!phy_pataTB5.getText().toString().equals(""))
							phy_paraSTR5 = phy_pataTB5.getText().toString();

						if (!phy_pataTB6.getText().toString().equals(""))
							phy_paraSTR6 = phy_pataTB6.getText().toString();

						if (!phy_pataTB7.getText().toString().equals(""))
							phy_paraSTR7 = phy_pataTB7.getText().toString();

						if (!phy_pataTB8.getText().toString().equals(""))
							phy_paraSTR8 = phy_pataTB8.getText().toString();

						if (!phy_pataTB9.getText().toString().equals(""))
							phy_paraSTR9 = phy_pataTB9.getText().toString();

						if (!phy_pataTB10.getText().toString().equals(""))
							phy_paraSTR10 = phy_pataTB10.getText().toString();
						selectedType1="1";
					}
					
					if(chckbxChemical.isSelected())
					{
						if(!chem_paraTB1.getText().toString().equals(""))
							chem_paraSTR1=chem_paraTB1.getText().toString();
						
						if(!chem_paraTB2.getText().toString().equals(""))
							chem_paraSTR2=chem_paraTB2.getText().toString();
						
						if(!chem_paraTB3.getText().toString().equals(""))
							chem_paraSTR3=chem_paraTB3.getText().toString();
						
						if(!chem_paraTB4.getText().toString().equals(""))
							chem_paraSTR4=chem_paraTB4.getText().toString();
						
						if(!chem_paraTB5.getText().toString().equals(""))
							chem_paraSTR5=chem_paraTB5.getText().toString();
						
						if(!chem_paraTB6.getText().toString().equals(""))
							chem_paraSTR6=chem_paraTB6.getText().toString();
						
						if(!chem_paraTB7.getText().toString().equals(""))
							chem_paraSTR7=chem_paraTB7.getText().toString();
						
						if(!chem_paraTB8.getText().toString().equals(""))
							chem_paraSTR8=chem_paraTB8.getText().toString();
						
						if(!chem_paraTB9.getText().toString().equals(""))
							chem_paraSTR9=chem_paraTB9.getText().toString();
						
						if(!chem_paraTB10.getText().toString().equals(""))
							chem_paraSTR10=chem_paraTB10.getText().toString();
						
						selectedType2="1";
					}

					if(chckbxMexamhpf.isSelected())
					{
						if(!meh_paraTB1.getText().toString().equals(""))
							meh_paraSTR1=meh_paraTB1.getText().toString();
						
						if(!meh_paraTB2.getText().toString().equals(""))
							meh_paraSTR2=meh_paraTB2.getText().toString();
						
						if(!meh_paraTB3.getText().toString().equals(""))
							meh_paraSTR3=meh_paraTB3.getText().toString();
						
						if(!meh_paraTB4.getText().toString().equals(""))
							meh_paraSTR4=meh_paraTB4.getText().toString();
						
						if(!meh_paraTB5.getText().toString().equals(""))
							meh_paraSTR5=meh_paraTB5.getText().toString();
						
						if(!meh_paraTB6.getText().toString().equals(""))
							meh_paraSTR6=meh_paraTB6.getText().toString();
						
						if(!meh_paraTB7.getText().toString().equals(""))
							meh_paraSTR7=meh_paraTB7.getText().toString();
						
						if(!meh_paraTB8.getText().toString().equals(""))
							meh_paraSTR8=meh_paraTB8.getText().toString();
						
						if(!meh_paraTB9.getText().toString().equals(""))
							meh_paraSTR9=meh_paraTB9.getText().toString();
						
						if(!meh_paraTB10.getText().toString().equals(""))
							meh_paraSTR10=meh_paraTB10.getText().toString();
						
						selectedType3="1";
					}
					
					String[] data = new String[40];
					ReferenceTableDBConnection referenceTableDBConnection = new ReferenceTableDBConnection();
					data[0] = examCodeSTR;
					data[1] = examNameSTR;
					data[2] = examSubTB.getText().toString();
					data[3] = patientSexSTR;
					data[4] = patientTypeSTR;
					data[5] = minAgeSTR;
					data[6] = maxAgeSTR;
					
					data[7]=phy_paraSTR1;
					data[8]=phy_paraSTR2;
					data[9]=phy_paraSTR3;
					data[10]=phy_paraSTR4;
					data[11]=phy_paraSTR5;
					data[12]=phy_paraSTR6;
					data[13]=phy_paraSTR7;
					data[14]=phy_paraSTR8;
					data[15]=phy_paraSTR9;
					data[16]=phy_paraSTR10;
					
					data[17]=chem_paraSTR1;
					data[18]=chem_paraSTR2;
					data[19]=chem_paraSTR3;
					data[20]=chem_paraSTR4;
					data[21]=chem_paraSTR5;
					data[22]=chem_paraSTR6;
					data[23]=chem_paraSTR7;
					data[24]=chem_paraSTR8;
					data[25]=chem_paraSTR9;
					data[26]=chem_paraSTR10;
					
					data[27]=meh_paraSTR1;
					data[28]=meh_paraSTR2;
					data[29]=meh_paraSTR3;
					data[30]=meh_paraSTR4;
					data[31]=meh_paraSTR5;
					data[32]=meh_paraSTR6;
					data[33]=meh_paraSTR7;
					data[34]=meh_paraSTR8;
					data[35]=meh_paraSTR9;
					data[36]=meh_paraSTR10;
					
					data[37]=selectedType1+selectedType2+selectedType3;
					data[38]=commentsTA.getText().toString();
					
					
					
					
					try {
						referenceTableDBConnection
								.inserDataReferenceOutput(data);
						JOptionPane.showMessageDialog(null,
								"Saved Successfully", "Save Data",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		btnSave.setBounds(167, 515, 121, 35);
		contentPanel.add(btnSave);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(313, 515, 121, 35);
		contentPanel.add(btnCancel);

		final JComboBox comboBox = new JComboBox();
		comboBox.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent ie) {
				String str = (String) comboBox.getSelectedItem();
				patientTypeSTR = str;
			}
		});
		comboBox.setModel(new DefaultComboBoxModel(new String[] { "Normal",
				"Pregnant" }));
		comboBox.setFont(new Font("Tahoma", Font.PLAIN, 12));
		comboBox.setBounds(94, 140, 179, 25);
		contentPanel.add(comboBox);
		getExamReferenceRange(examCode);
		JLabel lblExamId_1 = new JLabel("Exam ID : " + examCode);
		lblExamId_1.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExamId_1.setBounds(10, 11, 263, 20);
		contentPanel.add(lblExamId_1);

		phy_pataTB2 = new JTextField(phy_paraSTR[1]);
		phy_pataTB2.setBounds(10, 243, 121, 20);
		contentPanel.add(phy_pataTB2);
		phy_pataTB2.setColumns(10);

		phy_pataTB3 = new JTextField(phy_paraSTR[2]);
		phy_pataTB3.setColumns(10);
		phy_pataTB3.setBounds(10, 274, 121, 20);
		contentPanel.add(phy_pataTB3);

		phy_pataTB4 = new JTextField(phy_paraSTR[3]);
		phy_pataTB4.setColumns(10);
		phy_pataTB4.setBounds(10, 305, 121, 20);
		contentPanel.add(phy_pataTB4);

		phy_pataTB5 = new JTextField(phy_paraSTR[4]);
		phy_pataTB5.setColumns(10);
		phy_pataTB5.setBounds(10, 336, 121, 20);
		contentPanel.add(phy_pataTB5);

		phy_pataTB6 = new JTextField(phy_paraSTR[5]);
		phy_pataTB6.setColumns(10);
		phy_pataTB6.setBounds(10, 367, 121, 20);
		contentPanel.add(phy_pataTB6);

		phy_pataTB7 = new JTextField(phy_paraSTR[6]);
		phy_pataTB7.setColumns(10);
		phy_pataTB7.setBounds(10, 398, 121, 20);
		contentPanel.add(phy_pataTB7);

		phy_pataTB8 = new JTextField(phy_paraSTR[7]);
		phy_pataTB8.setColumns(10);
		phy_pataTB8.setBounds(10, 429, 121, 20);
		contentPanel.add(phy_pataTB8);

		phy_pataTB1 = new JTextField(phy_paraSTR[0]);
		phy_pataTB1.setColumns(10);
		phy_pataTB1.setBounds(10, 214, 121, 20);
		contentPanel.add(phy_pataTB1);

		chem_paraTB1 = new JTextField(chem_paraSTR[0]);
		chem_paraTB1.setColumns(10);
		chem_paraTB1.setBounds(187, 214, 121, 20);
		contentPanel.add(chem_paraTB1);

		chem_paraTB2 = new JTextField(chem_paraSTR[1]);
		chem_paraTB2.setColumns(10);
		chem_paraTB2.setBounds(187, 243, 121, 20);
		contentPanel.add(chem_paraTB2);

		chem_paraTB3 = new JTextField(chem_paraSTR[2]);
		chem_paraTB3.setColumns(10);
		chem_paraTB3.setBounds(187, 274, 121, 20);
		contentPanel.add(chem_paraTB3);

		chem_paraTB4 = new JTextField(chem_paraSTR[3]);
		chem_paraTB4.setColumns(10);
		chem_paraTB4.setBounds(187, 305, 121, 20);
		contentPanel.add(chem_paraTB4);

		chem_paraTB5 = new JTextField(chem_paraSTR[4]);
		chem_paraTB5.setColumns(10);
		chem_paraTB5.setBounds(187, 336, 121, 20);
		contentPanel.add(chem_paraTB5);

		chem_paraTB6 = new JTextField(chem_paraSTR[5]);
		chem_paraTB6.setColumns(10);
		chem_paraTB6.setBounds(187, 367, 121, 20);
		contentPanel.add(chem_paraTB6);

		chem_paraTB7 = new JTextField(chem_paraSTR[6]);
		chem_paraTB7.setColumns(10);
		chem_paraTB7.setBounds(187, 398, 121, 20);
		contentPanel.add(chem_paraTB7);

		chem_paraTB8 = new JTextField(chem_paraSTR[7]);
		chem_paraTB8.setColumns(10);
		chem_paraTB8.setBounds(187, 429, 121, 20);
		contentPanel.add(chem_paraTB8);

		meh_paraTB1 = new JTextField(chem_paraSTR[0]);
		meh_paraTB1.setColumns(10);
		meh_paraTB1.setBounds(386, 214, 121, 20);
		contentPanel.add(meh_paraTB1);

		meh_paraTB2 = new JTextField(meh_paraSTR[1]);
		meh_paraTB2.setColumns(10);
		meh_paraTB2.setBounds(386, 243, 121, 20);
		contentPanel.add(meh_paraTB2);

		meh_paraTB3 = new JTextField(meh_paraSTR[2]);
		meh_paraTB3.setColumns(10);
		meh_paraTB3.setBounds(386, 274, 121, 20);
		contentPanel.add(meh_paraTB3);

		meh_paraTB4 = new JTextField(meh_paraSTR[3]);
		meh_paraTB4.setColumns(10);
		meh_paraTB4.setBounds(386, 305, 121, 20);
		contentPanel.add(meh_paraTB4);

		meh_paraTB5 = new JTextField(meh_paraSTR[4]);
		meh_paraTB5.setColumns(10);
		meh_paraTB5.setBounds(386, 336, 121, 20);
		contentPanel.add(meh_paraTB5);

		meh_paraTB6 = new JTextField(meh_paraSTR[5]);
		meh_paraTB6.setColumns(10);
		meh_paraTB6.setBounds(386, 367, 121, 20);
		contentPanel.add(meh_paraTB6);

		meh_paraTB7 = new JTextField(meh_paraSTR[6]);
		meh_paraTB7.setColumns(10);
		meh_paraTB7.setBounds(386, 398, 121, 20);
		contentPanel.add(meh_paraTB7);

		meh_paraTB8 = new JTextField(meh_paraSTR[7]);
		meh_paraTB8.setColumns(10);
		meh_paraTB8.setBounds(386, 429, 121, 20);
		contentPanel.add(meh_paraTB8);

		phy_pataTB9 = new JTextField(meh_paraSTR[8]);
		phy_pataTB9.setColumns(10);
		phy_pataTB9.setBounds(10, 460, 121, 20);
		contentPanel.add(phy_pataTB9);

		chem_paraTB9 = new JTextField(chem_paraSTR[8]);
		chem_paraTB9.setColumns(10);
		chem_paraTB9.setBounds(187, 460, 121, 20);
		contentPanel.add(chem_paraTB9);

		meh_paraTB9 = new JTextField(meh_paraSTR[8]);
		meh_paraTB9.setColumns(10);
		meh_paraTB9.setBounds(386, 460, 121, 20);
		contentPanel.add(meh_paraTB9);

		meh_paraTB10 = new JTextField(meh_paraSTR[9]);
		meh_paraTB10.setColumns(10);
		meh_paraTB10.setBounds(386, 491, 121, 20);
		contentPanel.add(meh_paraTB10);

		chem_paraTB10 = new JTextField(chem_paraSTR[9]);
		chem_paraTB10.setColumns(10);
		chem_paraTB10.setBounds(187, 491, 121, 20);
		contentPanel.add(chem_paraTB10);

		phy_pataTB10 = new JTextField(phy_paraSTR[9]);
		phy_pataTB10.setColumns(10);
		phy_pataTB10.setBounds(10, 491, 121, 20);
		contentPanel.add(phy_pataTB10);

		chckbxPhysical = new JCheckBox("Physical");
		chckbxPhysical.setBounds(21, 184, 97, 23);
		contentPanel.add(chckbxPhysical);

		chckbxChemical = new JCheckBox("Chemical");
		chckbxChemical.setBounds(191, 184, 97, 23);
		contentPanel.add(chckbxChemical);

		chckbxMexamhpf = new JCheckBox("M/Exam./HPF");
		chckbxMexamhpf.setBounds(395, 184, 97, 23);
		contentPanel.add(chckbxMexamhpf);
	}
	public void getExamReferenceRange(String index) {

		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceTableExamCode(index);
		try {
			while (resultSet.next()) {

				for (int i = 1; i < 11; i++) {
					String str = resultSet.getObject(i).toString();
					phy_paraSTR[i-1]="";
					if (!str.equals("0"))
					{
						phyVector.add(str);
						phy_paraSTR[i-1]=""+str;
					}

				}

				for (int i = 11; i < 21; i++) {
					String str = resultSet.getObject(i).toString();
					chem_paraSTR[i-11]="";
					if (!str.equals("0"))
					{
						chemVector.add(str);
						chem_paraSTR[i-11]=""+str;
					}
					
				}

				for (int i = 21; i < 31; i++) {
					String str = resultSet.getObject(i).toString();
					meh_paraSTR[i-21]="";
					if (!str.equals("0"))
					{
						mehVector.add(str);
						meh_paraSTR[i-21]=""+str;
					}
						
				}
				examSubTB.setText(resultSet.getObject(31).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

}
