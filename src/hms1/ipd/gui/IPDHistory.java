package hms1.ipd.gui;

import hms.main.DateFormatChange;
import hms.main.NumberToWordConverter;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.AdvancePaymentSlippdf;
import hms.patient.slippdf.IPDBillSlippdf;
import hms.reception.gui.ReceptionMain;
import hms1.expenses.database.IPDExpensesDBConnection;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.database.IPDPaymentsDBConnection;
import hms1.wards.database.WardsManagementDBConnection;

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
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

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
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

public class IPDHistory extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField attendanceDateTB;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	String[] data = new String[20];
	private JTextField insuranceTypeTB;
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	int selectedRowIndex;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0, ipd_advance = 0, ipd_balance = 0;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String ipd_doctor_id = "", ipd_doctorname = "", ipd_date = "",
			ipd_time = "", ipd_note = "", ipd_id = "", ward_name = "",
			building_name = "", bed_no = "Select Bed No", ward_incharge = "",
			ward_room = "", ward_code = "", descriptionSTR = "", charges = "",
			ipd_days, ipd_hours, ipd_minutes, ipd_expenses_id;
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel building = new DefaultComboBoxModel();
	final DefaultComboBoxModel wards = new DefaultComboBoxModel();
	final DefaultComboBoxModel bedno = new DefaultComboBoxModel();
	Vector<String> expensesIndexVector = new Vector<String>();
	private JComboBox patientIDCB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	IPDDBConnection ipddbConnection;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JTextField ipdDoctorTB;
	private JTextField ipdBuildingTB;
	private JTextField ipdWardTB;
	private JTextField ipdBedNoTB;
	private JTextField bedDaysTB;
	private JTextField totalAmountTB;
	private JTextField advancePaymentTB;
	private JTextField chargesTB;
	private JComboBox chargesTypeCB;
	private JTable table;
	private JTextField amountTB;
	private JTextField balanceTB;
	private JButton btnRemove;
	private JTextField ipdNoTB;

	public static void main(String[] arg) {
		String formattednumber = "$" + String.valueOf("11001");
		System.out.println(NumberToWordConverter.convert(22190));
		new IPDHistory().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public IPDHistory() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				IPDHistory.class.getResource("/icons/rotaryLogo.png")));
		setTitle("New IPD Bill");
		setModal(true);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(200, 30, 1040, 596);

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
				.getBorder("TitledBorder.border"), "", TitledBorder.CENTER,
				TitledBorder.TOP, null, null));
		panel_4.setBounds(6, 5, 1018, 554);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		JLabel lblAttendenceDate = new JLabel("Date");
		lblAttendenceDate.setBounds(10, 19, 51, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSearchPatient = new JLabel("Search Patient :");
		lblSearchPatient.setBounds(356, 19, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				getPatientsID(searchPatientTB.getText());
			}
		});
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(469, 14, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);

		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					
					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (str.equals(""))  {
							patientNameTB.setText("");
							addressTB.setText("");
							ageTB.setText("");
							cityTB.setText("");
							telephoneTB.setText("");
							insuranceTypeTB.setText("");
							rdbtnMale.setSelected(false);
							rdbtnFemale.setSelected(false);
							patientID.removeAllElements();
							patientIDCB.setModel(patientID);
							ipdBuildingTB.setText("");
							ipdWardTB.setText("");
							ipdBedNoTB.setText("");
							bedDaysTB.setText("");
							ipdDoctorTB.setText("");
							ipdNoTB.setText("");
							advancePaymentTB.setText("");
							populateExpensesTable("0");
						}
					}

					@Override
					public void insertUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						// TODO Auto-generated method stub
						
					}

				});

		JButton searchBT = new JButton("");
		searchBT.setToolTipText("Search");
		searchBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				String str = searchPatientTB.getText() + "";
				if (!str.equals("")) {
					getPatientsID(str);
				} else {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					insuranceTypeTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					patientID.removeAllElements();
					patientIDCB.setModel(patientID);
					ipdBuildingTB.setText("");
					ipdWardTB.setText("");
					ipdNoTB.setText("");
					ipdBedNoTB.setText("");
					bedDaysTB.setText("");
					ipdDoctorTB.setText("");
					advancePaymentTB.setText("");
					populateExpensesTable("0");
				}
			}
		});
		searchBT.setBounds(651, 13, 28, 25);
		panel_4.add(searchBT);
		searchBT.setIcon(new ImageIcon(IPDHistory.class
				.getResource("/icons/zoom_r_button.png")));
		ipd_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(78, 14, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(699, 44, 313, 434);
		panel_4.add(panel);
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel.setLayout(null);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(6, 21, 108, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setEditable(false);
		patientNameTB.setBounds(106, 16, 201, 25);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setBounds(6, 57, 108, 14);
		panel.add(lblNewLabel_1);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblNewLabel_2 = new JLabel("City :");
		lblNewLabel_2.setBounds(6, 93, 93, 17);
		panel.add(lblNewLabel_2);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setBounds(6, 129, 108, 17);
		panel.add(lblTelephone);
		lblTelephone.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblAge = new JLabel("Age :");
		lblAge.setBounds(6, 164, 93, 17);
		panel.add(lblAge);
		lblAge.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setBounds(6, 192, 46, 14);
		panel.add(lblSex);
		lblSex.setFont(new Font("Tahoma", Font.PLAIN, 12));

		addressTB = new JTextField();
		addressTB.setEditable(false);
		addressTB.setBounds(106, 52, 201, 25);
		panel.add(addressTB);
		addressTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addressTB.setColumns(10);

		cityTB = new JTextField();
		cityTB.setEditable(false);
		cityTB.setBounds(106, 88, 201, 25);
		panel.add(cityTB);
		cityTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		cityTB.setColumns(10);

		telephoneTB = new JTextField();
		telephoneTB.setEditable(false);
		telephoneTB.setBounds(106, 124, 201, 25);
		panel.add(telephoneTB);
		telephoneTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		telephoneTB.setColumns(10);

		ageTB = new JTextField();
		ageTB.setEditable(false);
		ageTB.setBounds(106, 160, 201, 25);
		panel.add(ageTB);
		ageTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ageTB.setColumns(10);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(106, 192, 80, 23);
		panel.add(rdbtnMale);
		rdbtnMale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(188, 192, 109, 23);
		panel.add(rdbtnFemale);
		rdbtnFemale.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNote = new JLabel("Has Insurance :");
		lblNote.setBounds(6, 230, 108, 14);
		panel.add(lblNote);
		lblNote.setFont(new Font("Tahoma", Font.PLAIN, 12));

		insuranceTypeTB = new JTextField();
		insuranceTypeTB.setEditable(false);
		insuranceTypeTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		insuranceTypeTB.setBounds(106, 222, 201, 25);
		panel.add(insuranceTypeTB);
		insuranceTypeTB.setColumns(10);

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(689, 13, 77, 20);
		panel_4.add(lblPatientId);
		lblPatientId.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(20, 51, 349, 427);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(234, 448, 94, 20);
		panel_2.add(lblTotalcharges);

		JLabel lblCharges = new JLabel("Charges :");
		lblCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblCharges.setBounds(10, 71, 108, 14);
		panel_2.add(lblCharges);

		chargesTB = new JTextField();
		chargesTB.setHorizontalAlignment(SwingConstants.TRAILING);
		chargesTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chargesTB.setColumns(10);
		chargesTB.setBounds(147, 66, 164, 25);
		panel_2.add(chargesTB);
		chargesTB.addKeyListener(new KeyAdapter() {
             @Override
			public void keyTyped(KeyEvent e) {
                 char vChar = e.getKeyChar();
                 if (!(Character.isDigit(vChar)
                         || (vChar == KeyEvent.VK_BACK_SPACE)
                         || (vChar == KeyEvent.VK_DELETE))) {
                     e.consume();
                 }
             }
         });

		JLabel lblSelectOrEnter = new JLabel("Select or Enter Desc :");
		lblSelectOrEnter.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblSelectOrEnter.setBounds(10, 26, 135, 14);
		panel_2.add(lblSelectOrEnter);

		chargesTypeCB = new JComboBox();
		chargesTypeCB.setModel(new DefaultComboBoxModel(new String[] {
				"ORTHO PROCEDURE", "ORTHO ANESTHESIA", "ORTHO INDOOR",
				"ORTHO MEDICINE", "GENERAL PROCEDURE", "GENERAL ANESTHESIA",
				"GENERAL INDOOR", "GENERAL MEDICINE", "CANCER PROCEDURE",
				"CANCER ANESTHESIA", "CANCER INDOOR", "CANCER MEDICINE",
				"OT MEDICINES", "BED CHARGES", "DRESSING", "AMBULANCE",
				"ADMISSION FEES", "OTHER CHARGES","DISCOUNT" }));
		chargesTypeCB.setEditable(true);
		chargesTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		chargesTypeCB.setBounds(147, 21, 164, 25);
		panel_2.add(chargesTypeCB);
		chargesTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				descriptionSTR = (String) chargesTypeCB.getSelectedItem();
			}
		});
		final JTextField tfListText = (JTextField) chargesTypeCB.getEditor()
				.getEditorComponent();
		tfListText.addCaretListener(new CaretListener() {

			@Override
			public void caretUpdate(CaretEvent e) {
				String text = tfListText.getText();
				if (!text.equals("")) {

					descriptionSTR = text;
					System.out.println(descriptionSTR);
					// HERE YOU CAN WRITE YOUR CODE
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 156, 320, 260);
		panel_2.add(scrollPane);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.BOLD, 12));
		table.setModel(new DefaultTableModel(
				new Object[][] { { null, null }, }, new String[] {
						"Description", "Amount" }) {
			Class[] columnTypes = new Class[] { Object.class, Integer.class };

			@Override
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}

			boolean[] columnEditables = new boolean[] { true, false };

			@Override
			public boolean isCellEditable(int row, int column) {
				return columnEditables[column];
			}
		});
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		table.getColumnModel().getColumn(0).setPreferredWidth(150);
		table.getColumnModel().getColumn(0).setMinWidth(150);
		table.getColumnModel().getColumn(1).setResizable(false);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(150);
		scrollPane.setViewportView(table);

		JButton btnNewButton = new JButton("Add");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (descriptionSTR.equals("") || chargesTB.getText().equals("")) {

				} else {
					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					IPDExpensesDBConnection db = new IPDExpensesDBConnection();
					String[] data1 = new String[15];
					data1[0] = "" + ipd_id;
					data1[1] = "" + descriptionSTR;
					
					if(descriptionSTR.equals("DISCOUNT"))
					{
						data1[2] = descriptionSTR + "";
						data1[3] = "-" + chargesTB.getText().toString();
					}
					else {
						data1[2] = descriptionSTR + " Charges";
						data1[3] = "" + chargesTB.getText().toString();
					}
					
					data1[4] = "" + DateFormatChange.StringToMysqlDate(new Date());
					data1[5] = "" + timeFormat.format(cal1.getTime());
					data1[6] = "n/a";
					data1[7] = ""+p_id;
					data1[8] = ""+p_name;
					data1[9] = data1[3];
					data1[10] = "1";
					data1[11] = "MANUAL";
					data1[12] = ReceptionMain.receptionNameSTR;
					data1[13] = "MANUAL";
					data1[14] = "NA";
					try {
						db.inserData(data1);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						db.closeConnection();
					}
					db.closeConnection();
					populateExpensesTable(ipd_id);
				}
			}
		});
		btnNewButton.setBounds(56, 116, 102, 29);
		panel_2.add(btnNewButton);

		btnRemove = new JButton("Remove");
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedRowIndex = 0;
				selectedRowIndex = table.getSelectedRow();
				selectedRowIndex = table
						.convertRowIndexToModel(selectedRowIndex);
				if (expensesIndexVector.size() > 0)
					ipd_expenses_id = expensesIndexVector.get(selectedRowIndex);
				IPDExpensesDBConnection db = new IPDExpensesDBConnection();
				try {
					db.deleteRow(ipd_expenses_id);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					db.closeConnection();
				}
				db.closeConnection();
				populateExpensesTable(ipd_id);
			}
		});
		btnRemove.setBounds(175, 116, 102, 29);
		panel_2.add(btnRemove);

		JButton btnNewButton_3 = new JButton("Generate Bill");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (!amountTB.getText().equals("")
						&& !ipdNoTB.getText().equals("")) {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									IPDHistory.this,
									"Are you sure to generate bill after this discharge patient",
									"Discharge", dialogButton);
					if (dialogResult == 0) {
						IPDDBConnection db = new IPDDBConnection();
						long timeInMillis = System.currentTimeMillis();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTimeInMillis(timeInMillis);
						SimpleDateFormat timeFormat = new SimpleDateFormat(
								"hh:mm:ss a");
						String data[] = new String[10];
						data[0] = totalAmountTB.getText() + "";
						data[1] = balanceTB.getText() + "";
						data[2] = amountTB.getText() + "";
						data[3] = "Yes";
						data[4] = "" + DateFormatChange.StringToMysqlDate(new Date());
						;
						data[5] = "" + timeFormat.format(cal1.getTime());
						data[6] = "Yes";
						data[7] = "" + ipd_id;

						try {
							db.updateData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
						db.closeConnection();
						WardsManagementDBConnection wardsManagementDBConnection = new WardsManagementDBConnection();
						data[0] = "0";
						try {
							wardsManagementDBConnection.updateDataUnfill(data,
									ward_code);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							wardsManagementDBConnection.closeConnection();
						}
						wardsManagementDBConnection.closeConnection();
						JOptionPane.showMessageDialog(null,
								"Click ok to continue", "Bill Generation",
								JOptionPane.INFORMATION_MESSAGE);
						dispose();
						try {
							new IPDBillSlippdf("",ipd_id, ipdDoctorTB.getText(),false);
						} catch (DocumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				} else {
					JOptionPane.showMessageDialog(null,
							"Please select patient or enter Payment",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				}

			}
		});
		btnNewButton_3.setBounds(373, 489, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(862, 489, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(IPDHistory.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		patientIDCB = new JComboBox(patientID);
		patientIDCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					p_id = patientIDCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				patientNameTB.setText("");
				addressTB.setText("");
				ageTB.setText("");
				cityTB.setText("");
				telephoneTB.setText("");
				insuranceTypeTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				// System.out.println(p_id + "    "
				// + patientIDCB.getSelectedIndex());
				setPatientDetail(p_id);
				if (patientID.getSize() > 0) {
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
			}
		});
		patientIDCB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientIDCB.setBounds(766, 11, 218, 25);
		panel_4.add(patientIDCB);
		searchBT.setFocusable(true);

		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_7.setBounds(377, 50, 316, 211);
		panel_4.add(panel_7);
		panel_7.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(10, 46, 108, 14);
		panel_7.add(lblDoctorName);

		ipdDoctorTB = new JTextField();
		ipdDoctorTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdDoctorTB.setEditable(false);
		ipdDoctorTB.setColumns(10);
		ipdDoctorTB.setBounds(110, 41, 182, 25);
		panel_7.add(ipdDoctorTB);

		JLabel lblBuilding = new JLabel("Building :");
		lblBuilding.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBuilding.setBounds(10, 77, 108, 14);
		panel_7.add(lblBuilding);

		ipdBuildingTB = new JTextField();
		ipdBuildingTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBuildingTB.setEditable(false);
		ipdBuildingTB.setColumns(10);
		ipdBuildingTB.setBounds(110, 72, 182, 25);
		panel_7.add(ipdBuildingTB);

		JLabel lblWardName = new JLabel("Ward Name :");
		lblWardName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblWardName.setBounds(10, 113, 108, 14);
		panel_7.add(lblWardName);

		ipdWardTB = new JTextField();
		ipdWardTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdWardTB.setEditable(false);
		ipdWardTB.setColumns(10);
		ipdWardTB.setBounds(110, 108, 182, 25);
		panel_7.add(ipdWardTB);

		JLabel lblBedNo = new JLabel("Bed No :");
		lblBedNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBedNo.setBounds(10, 149, 108, 14);
		panel_7.add(lblBedNo);

		ipdBedNoTB = new JTextField();
		ipdBedNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdBedNoTB.setEditable(false);
		ipdBedNoTB.setColumns(10);
		ipdBedNoTB.setBounds(110, 144, 182, 25);
		panel_7.add(ipdBedNoTB);

		JLabel lblNoOfDays = new JLabel("No. of Days :");
		lblNoOfDays.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNoOfDays.setBounds(10, 180, 108, 14);
		panel_7.add(lblNoOfDays);

		bedDaysTB = new JTextField();
		bedDaysTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		bedDaysTB.setEditable(false);
		bedDaysTB.setColumns(10);
		bedDaysTB.setBounds(110, 175, 182, 25);
		panel_7.add(bedDaysTB);

		ipdNoTB = new JTextField();
		ipdNoTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		ipdNoTB.setEditable(false);
		ipdNoTB.setColumns(10);
		ipdNoTB.setBounds(110, 10, 182, 25);
		panel_7.add(ipdNoTB);

		JLabel lblOpdNo = new JLabel("IPD No :");
		lblOpdNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblOpdNo.setBounds(10, 15, 108, 14);
		panel_7.add(lblOpdNo);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Bill",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(377, 272, 317, 206);
		panel_4.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTotalAmount = new JLabel("Total Amount :");
		lblTotalAmount.setBounds(6, 21, 108, 14);
		panel_1.add(lblTotalAmount);
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 12));

		totalAmountTB = new JTextField();
		totalAmountTB.setBounds(149, 16, 147, 25);
		panel_1.add(totalAmountTB);
		totalAmountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		totalAmountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		totalAmountTB.setEditable(false);
		totalAmountTB.setColumns(10);

		JLabel lblAdvancePayment = new JLabel("Advance Payment :");
		lblAdvancePayment.setBounds(6, 66, 108, 14);
		panel_1.add(lblAdvancePayment);
		lblAdvancePayment.setFont(new Font("Tahoma", Font.PLAIN, 12));

		advancePaymentTB = new JTextField();
		advancePaymentTB.setBounds(148, 61, 148, 25);
		panel_1.add(advancePaymentTB);
		advancePaymentTB.setHorizontalAlignment(SwingConstants.TRAILING);
		advancePaymentTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		advancePaymentTB.setEditable(false);
		advancePaymentTB.setColumns(10);

		amountTB = new JTextField();
		amountTB.setHorizontalAlignment(SwingConstants.TRAILING);
		amountTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		amountTB.setColumns(10);
		amountTB.setBounds(148, 97, 148, 25);
		panel_1.add(amountTB);
		amountTB.addKeyListener(new KeyAdapter() {
             @Override
			public void keyTyped(KeyEvent e) {
                 char vChar = e.getKeyChar();
                 if (!(Character.isDigit(vChar)
                         || (vChar == KeyEvent.VK_BACK_SPACE)
                         || (vChar == KeyEvent.VK_DELETE))) {
                     e.consume();
                 }
             }
         });
		amountTB.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String str = amountTB.getText() + "";
				ipd_balance = totalCharges - ipd_advance;
				balanceTB.setText(ipd_balance + "");
				if (!str.equals("")) {
					ipd_balance = ipd_balance - Double.parseDouble(str);
					balanceTB.setText("" + ipd_balance);
				} else {
					ipd_balance = ipd_balance - 0;
					balanceTB.setText("" + ipd_balance);
				}
			}
		});

		JLabel lblEnterPayment = new JLabel("Enter Payment :");
		lblEnterPayment.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEnterPayment.setBounds(6, 102, 108, 14);
		panel_1.add(lblEnterPayment);

		balanceTB = new JTextField();
		balanceTB.setHorizontalAlignment(SwingConstants.TRAILING);
		balanceTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		balanceTB.setEditable(false);
		balanceTB.setColumns(10);
		balanceTB.setBounds(148, 141, 148, 25);
		panel_1.add(balanceTB);

		JLabel lblBalance = new JLabel("Balance :");
		lblBalance.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblBalance.setBounds(6, 146, 108, 14);
		panel_1.add(lblBalance);

		JButton cancelIPDBT = new JButton("Cancel IPD");
		cancelIPDBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (ipdNoTB.equals("")) {
					JOptionPane.showMessageDialog(null,
							"Please select patient or enter Payment",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {
					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane
							.showConfirmDialog(
									IPDHistory.this,
									"Are you sure to Cancel the IPD",
									"Cancel", dialogButton);
					if (dialogResult == 0) {
						
						IPDDBConnection db1 = new IPDDBConnection();
						long timeInMillis = System.currentTimeMillis();
						Calendar cal1 = Calendar.getInstance();
						cal1.setTimeInMillis(timeInMillis);
						SimpleDateFormat timeFormat = new SimpleDateFormat(
								"hh:mm:ss a");
						String data[] = new String[10];
						data[0] = "0";
						data[1] = "0";
						data[2] = "0";
						data[3] = "CANCELLED";
						data[4] = "" + DateFormatChange.StringToMysqlDate(new Date());;
						data[5] = "" + timeFormat.format(cal1.getTime());
						data[6] = "CANCELLED";
						data[7] = "" + ipd_id;

						try {
							db1.updateData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db1.closeConnection();
						}
						db1.closeConnection();
						
						
						WardsManagementDBConnection wardsManagementDBConnection = new WardsManagementDBConnection();
						data[0] = "0";
						try {
							wardsManagementDBConnection.updateDataUnfill(data,
									ward_code);
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							wardsManagementDBConnection.closeConnection();
						}
						wardsManagementDBConnection.closeConnection();
						double finalPayment = ipd_advance;
						IPDPaymentsDBConnection db = new IPDPaymentsDBConnection();
						

						
						Double d = Double.parseDouble(advancePaymentTB.getText().toString());
						int advancePaymentIndex = 0;
						data[0] = "" + ipd_id;
						data[1] = "" + p_id;
						data[2] = "" + p_name;
						data[3] = "R";
						data[4] = "" + "RETURN";
						data[5] = "" + Integer.valueOf(d.intValue());
						data[6] = "" + DateFormatChange.StringToMysqlDate(new Date());
						data[7] = "" + timeFormat.format(cal1.getTime());
						data[8] = "" + ReceptionMain.receptionNameSTR;

						try {
							advancePaymentIndex = db.inserData(data);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							db.closeConnection();
						}
						db.closeConnection();
						try {
							new AdvancePaymentSlippdf(ipd_doctorname,Integer.valueOf(d.intValue())+"", ipdNoTB.getText(),
									p_name, advancePaymentIndex + "",
									"Return Amount");
						} catch (DocumentException | IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		});
		cancelIPDBT.setFont(new Font("Tahoma", Font.PLAIN, 13));
		cancelIPDBT.setBounds(533, 489, 146, 44);
		panel_4.add(cancelIPDBT);

		JButton returnAmountBT = new JButton("Return Amount");
		returnAmountBT.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				IPDDeposit deposit=new IPDDeposit();
				deposit.setVisible(true);
			}
		});
		returnAmountBT.setFont(new Font("Tahoma", Font.PLAIN, 13));
		returnAmountBT.setBounds(699, 489, 146, 44);
		panel_4.add(returnAmountBT);
		
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

	public void getPatientsID(String index) {
		PatientDBConnection patientDBConnection = new PatientDBConnection();
		ResultSet resultSet = patientDBConnection
				.searchPatientWithIdOrNmae(index);
		patientID.removeAllElements();
		try {
			while (resultSet.next()) {
				patientID.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		patientIDCB.setModel(patientID);
	}

	public void setPatientDetail(String indexId) {
		ipdBuildingTB.setText("");
		ipdNoTB.setText("");
		ipdWardTB.setText("");
		ipdBedNoTB.setText("");
		bedDaysTB.setText("");
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
				getIPDDATA();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
	}

	public void getIPDDATA() {

		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataPatientID(p_id);
		try {
			while (resultSet.next()) {
				ipd_id = resultSet.getObject(1).toString();
				ipdNoTB.setText("" + resultSet.getObject(2));
				ipdBuildingTB.setText("" + resultSet.getObject(3));
				ipdWardTB.setText("" + resultSet.getObject(4));
				ipdBedNoTB.setText("" + resultSet.getObject(5));
				ipd_advance = Double.parseDouble(resultSet.getObject(6)
						.toString());
				advancePaymentTB.setText(ipd_advance + "");
				ipd_date = resultSet.getObject(7).toString();
				ipd_time = resultSet.getObject(8).toString();
				ward_code = resultSet.getObject(9).toString();
				int[] bedHours = DateFormatChange
						.calulateHoursAndDays_BetweenDates(
								DateFormatChange.javaDate(ipd_date + " "
										+ ipd_time), new Date());
				bedDaysTB.setText(bedHours[0] + " Days, " + bedHours[1]
						+ " Hours, " + bedHours[2] + " Min");
				getIPDDoctors(ipd_id);
				populateExpensesTable(ipd_id);
				
			}
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();

	}

	public void getIPDDoctors(String ipd_id) {
		IPDDBConnection db = new IPDDBConnection();
		ResultSet resultSet = db.retrieveAllDataDoctor(ipd_id);
		try {
			while (resultSet.next()) {
				ipdDoctorTB.setText("" + resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}

	public void populateExpensesTable(String ipd_id) {

		totalCharges = 0;
		expensesIndexVector.clear();
		try {
			IPDExpensesDBConnection db = new IPDExpensesDBConnection();
			ResultSet rs = db.retrieveAllData(ipd_id);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while (rs.next()) {
				NumberOfRows++;
			}
			rs = db.retrieveAllData(ipd_id);

			// to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while (rs.next()) {
				expensesIndexVector.add(rs.getObject(1).toString());
				for (int C = 2; C <= NumberOfColumns; C++) {
					Rows_Object_Array[R][C - 2] = rs.getObject(C);
				}
				R++;
			}
			// Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array,
					new String[] { "Description", "Amount" }) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			for (int i = 0; i < NumberOfRows; i++) {
				totalCharges = totalCharges
						+ Double.parseDouble(Rows_Object_Array[i][1].toString());
			}
			totalAmountTB.setText(totalCharges + "");
			table.setModel(model);
			table.getColumnModel().getColumn(0).setPreferredWidth(150);
			table.getColumnModel().getColumn(0).setMinWidth(150);
			table.getColumnModel().getColumn(1).setResizable(false);
			table.getColumnModel().getColumn(1).setPreferredWidth(150);
			table.getColumnModel().getColumn(1).setMinWidth(150);
			ipd_balance = totalCharges - ipd_advance;

			if (!amountTB.getText().equals("")) {
				ipd_balance = ipd_balance
						- Float.parseFloat(amountTB.getText().toString());
			}
			balanceTB.setText(ipd_balance + "");
			btnRemove.setEnabled(false);
			
			
		} catch (SQLException ex) {
			Logger.getLogger(IPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		
		IPDDBConnection ipddbConnection=new IPDDBConnection();
		try {
			ipddbConnection.updateTotalAmount(ipd_id, totalCharges+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			ipddbConnection.closeConnection();
			e.printStackTrace();
		}
		ipddbConnection.closeConnection();
	}
}
