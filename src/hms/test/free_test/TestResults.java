package hms.test.free_test;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.insurance.gui.InsuranceDBConnection;
import hms.main.DateFormatChange;
import hms.patient.database.PatientDBConnection;
import hms.patient.gui.NewPatient;
import hms.patient.slippdf.ExamSlippdf;
import hms.pricemaster.database.PriceMasterDBConnection;
import hms.reception.gui.ReceptionMain;
import hms.test.database.OperatorDBConnection;
import hms.test.gui.Test;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.awt.KeyboardFocusManager;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfStructTreeController.returnType;

public class TestResults extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;

	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String exam_doctorname = "", exam_date = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "";

	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	ExamDBConnection examdbConnection;
	private JTable addTestTable_1;
	public static Font customFont;
	private JTextField patient_id;
	private JTextField resultsTB;
	private JLabel lblUnits;
	private JComboBox comboBox;
	private JLabel test_NameTV;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	
	public static void main(String[] arg)
	{
		new TestResults().setVisible(true);
	}
	public TestResults() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				TestResults.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Test Results");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(300, 70, 727, 436);
		setModal(true);
		try {
			// create the font to use. Specify the size!
			customFont = Font.createFont(Font.TRUETYPE_FONT,
					new File("indian.ttf")).deriveFont(12f);
			GraphicsEnvironment ge = GraphicsEnvironment
					.getLocalGraphicsEnvironment();
			// register the font
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File(
					"indian.ttf")));
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Test Results",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(6, 5, 699, 390);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		examdbConnection = new ExamDBConnection();
		exam_counter = examdbConnection.retrieveCounterData() + "";
		examdbConnection.closeConnection();

		JLabel lblSearchPatient = new JLabel("Search OPD ID :");
		lblSearchPatient.setBounds(10, 42, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(123, 37, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							searchOPDID(str);
						} else {
							test_NameTV.setText("");
							resultsTB.setText("");
							
							lblUnits.setText("");
							test_NameTV.setText("");
							patient_id.setText("");
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
						
							loadDataToTable("0");
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							searchOPDID(str);
						} else {
							test_NameTV.setText("");
							resultsTB.setText("");
							
							lblUnits.setText("");
							test_NameTV.setText("");
							patient_id.setText("");
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
							
							loadDataToTable("0");
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							searchOPDID(str);
						} else {
							test_NameTV.setText("");
							resultsTB.setText("");
							
							lblUnits.setText("");
							test_NameTV.setText("");
							patient_id.setText("");
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
							
							loadDataToTable("0");
						}
					}
				});

		JButton searchBT = new JButton("");
		searchBT.setToolTipText("Search");
		searchBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					searchOPDID(str);
				} else {
					test_NameTV.setText("");
					patient_id.setText("");
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					
					loadDataToTable("0");
				}
			}
		});
		searchBT.setBounds(309, 37, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(TestResults.class
				.getResource("/icons/zoom_r_button.png")));
		exam_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel = new JPanel();
		panel.setBounds(369, 26, 313, 346);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(10, 63, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(110, 58, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(10, 99, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(10, 135, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(10, 171, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(10, 206, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(10, 234, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(110, 94, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(110, 130, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(110, 166, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(110, 202, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(110, 234, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(192, 234, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(10, 272, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(110, 264, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(10, 24, 77, 20);
		panel.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		patient_id = new JTextField();
		patient_id.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patient_id.setEditable(false);
		patient_id.setColumns(10);
		patient_id.setBounds(110, 22, 201, 25);
		panel.add(patient_id);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 73, 349, 299);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);
		

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 11, 329, 137);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Test ID", "Test Name"
			}
		));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						
						test_NameTV.setText("");
						resultsTB.setText("");
						
						lblUnits.setText("");
						
						try {
							int cur_selectedRow;
							cur_selectedRow = addTestTable_1.getSelectedRow();
							System.out.println(cur_selectedRow);
							String id = addTestTable_1.getModel()
									.getValueAt(cur_selectedRow, 0).toString();
							
							getFreeTestData(id);
							
						} catch (Exception e2) {
							// TODO: handle exception
						}
						
						
					}
				});
		scrollPane.setViewportView(addTestTable_1);
		
		JLabel lblTestResults = new JLabel("Test Results");
		lblTestResults.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTestResults.setBounds(31, 189, 86, 14);
		panel_2.add(lblTestResults);
		
		resultsTB = new JTextField();
		resultsTB.setFont(new Font("Tahoma", Font.BOLD, 12));
		resultsTB.setBounds(121, 186, 139, 20);
		panel_2.add(resultsTB);
		resultsTB.setColumns(10);
		resultsTB.addKeyListener(new KeyAdapter() {
            @Override
			public void keyTyped(KeyEvent e) {
                char vChar = e.getKeyChar();
                if (!(Character.isDigit(vChar)
                        || (vChar == KeyEvent.VK_BACK_SPACE)
                        || (vChar == KeyEvent.VK_DELETE)||vChar== '.')) {
                    e.consume();   
                }
            }
        });
	
		resultsTB.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				int key = e.getKeyCode();
				if (key == KeyEvent.VK_ENTER) {
					Toolkit.getDefaultToolkit().beep();
					updateData();
				}
			}
		});
		
		lblUnits = new JLabel("Units");
		lblUnits.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblUnits.setBounds(267, 189, 72, 14);
		panel_2.add(lblUnits);
		
		JLabel lblTestRemarks = new JLabel("Test Remarks");
		lblTestRemarks.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTestRemarks.setBounds(31, 227, 86, 14);
		panel_2.add(lblTestRemarks);
		
		comboBox = new JComboBox();
		comboBox.setFont(new Font("Tahoma", Font.BOLD, 13));
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"Normal", "High", "Low"}));
		comboBox.setBounds(122, 223, 138, 22);
		panel_2.add(comboBox);

		JButton btnNewButton_3 = new JButton("Update");
		btnNewButton_3.setBounds(113, 256, 147, 32);
		panel_2.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				
				updateData();
			

			}
		});
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		JLabel lblTestResults_1 = new JLabel("Test Name :");
		lblTestResults_1.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTestResults_1.setBounds(31, 159, 86, 14);
		panel_2.add(lblTestResults_1);
		
		test_NameTV = new JLabel("");
		test_NameTV.setFont(new Font("Tahoma", Font.BOLD, 12));
		test_NameTV.setBounds(121, 159, 139, 14);
		panel_2.add(test_NameTV);
		searchBT.setFocusable(true);
		KeyboardFocusManager.getCurrentKeyboardFocusManager()
				.addPropertyChangeListener("permanentFocusOwner",
						new PropertyChangeListener() {
							@Override
							public void propertyChange(
									final PropertyChangeEvent e) {
								if (e.getNewValue() instanceof JTextField) {
									SwingUtilities.invokeLater(new Runnable() {
										@Override
										public void run() {
											JTextField textField = (JTextField) e
													.getNewValue();
											textField.selectAll();
										}
									});
								}
							}
						});
		searchPatientTB.requestFocus(true);
	}

	public void updateData()
	{
		
		if(test_NameTV.getText().toString().equals(""))
		{
			
			JOptionPane
			.showMessageDialog(
					null,
					"Please Select Test From table",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		if(resultsTB.getText().toString().equals(""))
		{
			
			JOptionPane
			.showMessageDialog(
					null,
					"Please enter results",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
//		
//		 `test_done`=?,`test_results`=?,`test_remarks`=?,`test_by`=?,`test_date`=?,`test_time`=? 
		int cur_selectedRow;
		cur_selectedRow = addTestTable_1.getSelectedRow();
		System.out.println(cur_selectedRow);
		String id = addTestTable_1.getModel()
				.getValueAt(cur_selectedRow, 0).toString();

		
		data[0] = "Yes";
		data[1] = resultsTB.getText().toString();
		data[2] = comboBox.getSelectedItem().toString();
		data[3] = Test.exam_operator+"";
		data[4] =	DateFormatChange.StringToMysqlDate(new Date());
		data[5] =	DateFormatChange.StringToMysqlDate(new Date());
		data[6] =	id;
		for (int i = 0; i < 7; i++) {
			System.out.println("HHHHHHHHHHHH "+data[i]);
		}
		
		FreeTestDBConnection dbConnection = new FreeTestDBConnection();
		try {
			dbConnection.updateData(data);
			JOptionPane.showMessageDialog(null,
					"Data Updated Successfully", "Data Update",
					JOptionPane.INFORMATION_MESSAGE);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			dbConnection.closeConnection();
		}
		dbConnection.closeConnection();
		
		searchPatientTB.setText(searchPatientTB.getText());
		searchPatientTB.requestFocus(true);
	}
	public void getFreeTestData(String id)
	{
		
		FreeTestDBConnection connection = new FreeTestDBConnection();
		ResultSet resultSet = connection.retrieveDataWithID(id);
		try {
			while (resultSet.next()) {
				
				test_NameTV.setText(""+resultSet.getObject(3).toString());
				resultsTB.setText("");
				
				lblUnits.setText(""+resultSet.getObject(7).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		connection.closeConnection();
	}
	
	public void searchOPDID(String opdID)
	{	
		patient_id.setText("");
		patientNameTB.setText("");
		addressTB.setText("");
		ageTB.setText("");
		cityTB.setText("");
		telephoneTB.setText("");
		insuranceTypeTB.setText("");
		rdbtnMale.setSelected(false);
		rdbtnFemale.setSelected(false);
		test_NameTV.setText("");
		resultsTB.setText("");
		lblUnits.setText("");
		test_NameTV.setText("");
		FreeTestDBConnection connection = new FreeTestDBConnection();
		ResultSet resultSet = connection.retrieveDataWithOPD(opdID);
		try {
			while (resultSet.next()) {
				System.out.println(resultSet.getObject(6).toString());
				setPatientDetail(resultSet.getObject(6).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		loadDataToTable(opdID);
		connection.closeConnection();
		
	}
	public void setPatientDetail(String indexId) {
		
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.retrieveDataWithIndex(indexId);
		try {
			while (resultSet.next()) {
				p_name = resultSet.getObject(1).toString();
				p_age = resultSet.getObject(2).toString();
				p_sex = resultSet.getObject(3).toString();
				p_address = resultSet.getObject(4).toString();
				p_city = resultSet.getObject(5).toString();
				p_telephone = resultSet.getObject(6).toString();
				p_insurancetype = resultSet.getObject(7).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		patient_id.setText(""+indexId);
		patientNameTB.setText(p_name);
		addressTB.setText(p_address);
		ageTB.setText(p_age + "  (Y-M-D)");
		cityTB.setText(p_city);
		telephoneTB.setText(p_telephone);
		insuranceTypeTB.setText(p_insurancetype);
		if (p_sex.equals("Male")) {
			rdbtnMale.setSelected(true);
			rdbtnFemale.setSelected(false);
		} else {
			rdbtnMale.setSelected(false);
			rdbtnFemale.setSelected(true);
		}
	}



	private void loadDataToTable(String opdId) {
		// get size of the hashmap
		

		FreeTestDBConnection connection = new FreeTestDBConnection();
		ResultSet resultSet = connection.retrieveDataWithOPD(opdId);
		
		int size = 0;
		
		try {
			while (resultSet.next()) {
				size++;
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		// declare two arrays one for key and other for keyValues
		
		ObjectArray_ListOfexams = new Object[size][2];
	
		int i=0;
		resultSet = connection.retrieveDataWithOPD(opdId);
		try {
			while (resultSet.next()) {
				setPatientDetail(resultSet.getObject(5).toString());
				

				ObjectArray_ListOfexams[i][0] = resultSet.getObject(1).toString();
				ObjectArray_ListOfexams[i][1] = resultSet.getObject(3).toString();
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		connection.closeConnection();
		
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Test ID", "Test Name" }) {

			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
	}
	public JTextField getPatient_id() {
		return patient_id;
	}
	public JLabel getTest_NameTV() {
		return test_NameTV;
	}
}
