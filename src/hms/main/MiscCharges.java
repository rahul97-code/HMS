package hms.main;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.doctor.database.DoctorDBConnection;
import hms.exam.database.ExamDBConnection;
import hms.patient.slippdf.MiscSlippdf;
import hms.reception.gui.ReceptionMain;

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
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;

public class MiscCharges extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField attendanceDateTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	int totalCharges = 0;
	DateFormatChange dateFormat = new DateFormatChange();

	String misc_doctorname = "", misc_date = "", misc_charge = "",
			misc_name = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel exams = new DefaultComboBoxModel();
	final DefaultComboBoxModel examSubCat = new DefaultComboBoxModel();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector<String> examRoomVector = new Vector<String>();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	ExamDBConnection examdbConnection;
	private JComboBox doctorNameCB;
	private JLabel doctorRoomLB;
	private JLabel doctorSpecializationLB;
	private JTextField miscChargesTB;
	private JComboBox miscDescTB;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	public static Font customFont;
	private JTextField patientNameTB;

	String descriptionSTR = "";

	public static void main(String[] asd) {
		new MiscCharges().setVisible(true);
	}

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public MiscCharges() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				MiscCharges.class.getResource("/icons/rotaryLogo.png")));
		setTitle(" Misc Charges Entry");
		setResizable(false);
		setBounds(200, 30, 727, 541);
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
				.getBorder("TitledBorder.border"), "Misc Charges Entry Form",
				TitledBorder.CENTER, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_4.setBounds(5, 11, 711, 502);
		contentPane.add(panel_4);
		panel_4.setLayout(null);
		examdbConnection = new ExamDBConnection();

		examdbConnection.closeConnection();
		JLabel lblAttendenceDate = new JLabel("Attendence Date");
		lblAttendenceDate.setBounds(10, 26, 128, 14);
		panel_4.add(lblAttendenceDate);
		lblAttendenceDate.setFont(new Font("Tahoma", Font.PLAIN, 12));
		misc_date = DateFormatChange.StringToMysqlDate(new Date());
		attendanceDateTB = new JTextField(
				DateFormatChange.StringToMysqlDate(new Date()));
		attendanceDateTB.setBounds(123, 23, 212, 25);
		panel_4.add(attendanceDateTB);
		attendanceDateTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		attendanceDateTB.setEditable(false);
		attendanceDateTB.setColumns(10);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 67, 349, 323);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(5, 11, 337, 59);
		panel_2.add(panel_1);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Doctor Refference",
				TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));
		panel_1.setLayout(null);

		JLabel lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setBounds(6, 16, 108, 25);
		panel_1.add(lblDoctorName);
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		doctorNameCB = new JComboBox<String>();
		doctorNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					misc_doctorname = doctorNameCB.getSelectedItem().toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		getAllDoctors();
		doctorNameCB.setBounds(103, 18, 228, 25);
		panel_1.add(doctorNameCB);
		doctorNameCB.setFont(new Font("Tahoma", Font.PLAIN, 12));

		JLabel lblDoctorRoom = new JLabel("Doctor Room :");
		lblDoctorRoom.setBounds(6, 52, 108, 25);
		panel_1.add(lblDoctorRoom);
		lblDoctorRoom.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblNewLabel_4 = new JLabel("Doctor specialization :");
		lblNewLabel_4.setBounds(6, 90, 141, 24);
		panel_1.add(lblNewLabel_4);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorRoomLB = new JLabel("");
		doctorRoomLB.setBounds(145, 54, 186, 23);
		panel_1.add(doctorRoomLB);
		doctorRoomLB.setFont(new Font("Tahoma", Font.PLAIN, 14));

		doctorSpecializationLB = new JLabel("");
		doctorSpecializationLB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		doctorSpecializationLB.setBounds(145, 89, 186, 25);
		panel_1.add(doctorSpecializationLB);

		JPanel panel_5 = new JPanel();
		panel_5.setLayout(null);
		panel_5.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Exam Detail :",

		TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma",

		Font.PLAIN, 12), null));
		panel_5.setBounds(5, 71, 337, 177);
		panel_2.add(panel_5);

		JLabel lblExamName = new JLabel("Exam Name :");
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamName.setBounds(6, 16, 0, 25);
		panel_5.add(lblExamName);
		
		JLabel lblExamRoom = new JLabel("Description:");
		lblExamRoom.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamRoom.setBounds(6, 52, 85, 25);
		panel_5.add(lblExamRoom);

		JLabel lblExamCharges = new JLabel("Charges :");
		lblExamCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblExamCharges.setBounds(6, 108, 94, 24);
		panel_5.add(lblExamCharges);

		miscDescTB = new JComboBox();
		miscDescTB.setModel(new DefaultComboBoxModel(new String[] {
				"MISC SERVICES", "INJECTION", "CANCER-MISC", "CANCER-CHEMO",
				"AMBULANCE CHARGES" }));
		miscDescTB.setEditable(true);
		miscDescTB.setFont(new Font("Tahoma", Font.PLAIN, 14));
		miscDescTB.setBounds(145, 54, 186, 23);
		panel_5.add(miscDescTB);
		miscDescTB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (miscDescTB.getSelectedIndex() > 0) {
					descriptionSTR = (String) miscDescTB.getSelectedItem();
					System.out.println(descriptionSTR);
				}
			}
		});
		final JTextField tfListText = (JTextField) miscDescTB.getEditor()
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

		miscChargesTB = new JTextField("");
		miscChargesTB.setFont(new Font("Mangal", Font.PLAIN, 14));
		miscChargesTB.setBounds(145, 107, 186, 25);
		miscChargesTB.addKeyListener(new KeyAdapter() {
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
		panel_5.add(miscChargesTB);

		JButton btnAdd = new JButton("Add");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (miscChargesTB.getText().equals("")
						|| descriptionSTR.equals("")) {
					JOptionPane.showMessageDialog(MiscCharges.this,
							"Please description and charges", "MISC" + "",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				try {
					Integer.parseInt(miscChargesTB.getText());
				} catch (Exception e) {
					// TODO: handle exception
					JOptionPane.showMessageDialog(MiscCharges.this,
							"Please Enter charges in numerics", "MISC" + "",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				examName.add(descriptionSTR);
				examChargesVector.add(miscChargesTB.getText());
				loadDataToTable();
			}
		});
		btnAdd.setBounds(65, 259, 89, 25);
		panel_2.add(btnAdd);

		final JButton btnRemove = new JButton("Remove");
		btnRemove.setFont(new Font("Tahoma", Font.PLAIN, 12));
		btnRemove.setEnabled(false);
		btnRemove.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int cur_selectedRow;
				cur_selectedRow = addTestTable_1.getSelectedRow();
				cur_selectedRow = addTestTable_1
						.convertRowIndexToModel(cur_selectedRow);
				String toDelete = addTestTable_1.getModel()
						.getValueAt(cur_selectedRow, 0).toString();

				examName.remove(cur_selectedRow);
				examChargesVector.remove(cur_selectedRow);

				loadDataToTable();
				btnRemove.setEnabled(false);
			}
		});
		btnRemove.setBounds(194, 259, 89, 25);
		panel_2.add(btnRemove);

		JButton btnNewButton_3 = new JButton("Generate Reciept");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (misc_doctorname.equals("Select Doctor")) {
					JOptionPane.showMessageDialog(null,
							"Please select opd doctor", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (patientNameTB.getText().equals("")) {// itemsHashMap.size()
																// == 0
					JOptionPane.showMessageDialog(null,
							"Please enter patient name", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else if (examName.size() == 0) {// itemsHashMap.size() == 0
					JOptionPane.showMessageDialog(null,
							"Please add some charges", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {

					long timeInMillis = System.currentTimeMillis();
					Calendar cal1 = Calendar.getInstance();
					cal1.setTimeInMillis(timeInMillis);
					SimpleDateFormat timeFormat = new SimpleDateFormat(
							"hh:mm:ss a");
					String[] data1 = new String[10];

					data1[0] = "MISC";
					data1[1] = "" + totalCharges;
					data1[2] = "" + ReceptionMain.receptionNameSTR;
					data1[3] = "" + misc_doctorname;
					data1[4] = "" + patientNameTB.getText();
					data1[5] = "" + DateFormatChange.StringToMysqlDate(new Date());
					data1[6] = "" + timeFormat.format(cal1.getTime());

					int index = 0;
					AmountReceiptDBConnection amountReceiptDBConnection = new AmountReceiptDBConnection();

					try {
						index = amountReceiptDBConnection.inserData(data1);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					amountReceiptDBConnection.closeConnection();
					try {
						new MiscSlippdf(misc_doctorname, ObjectArray_examname,
								ObjectArray_examcharges, totalCharges + "",
								index, patientNameTB.getText(),"","");
					} catch (DocumentException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					dispose();
				}
			}
		});
		btnNewButton_3.setBounds(212, 401, 147, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JButton btnNewButton_4 = new JButton("Cancel");
		btnNewButton_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton_4.setBounds(369, 401, 146, 44);
		panel_4.add(btnNewButton_4);
		btnNewButton_4.setIcon(new ImageIcon(MiscCharges.class
				.getResource("/icons/close_button.png")));
		btnNewButton_4.setFont(new Font("Tahoma", Font.PLAIN, 13));

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(369, 67, 332, 323);
		panel_4.add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(6, 50, 320, 217);
		panel.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Exam Name", "Charges" }));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setBounds(16, 287, 106, 23);
		panel.add(lblTotalCharges);
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setBounds(132, 290, 94, 20);
		panel.add(lblTotalcharges);
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(8, 20, 106, 14);
		panel.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setBounds(124, 16, 202, 24);
		panel.add(patientNameTB);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setIcon(new ImageIcon(MiscCharges.class
				.getResource("/icons/graphics-hospitals-575145.gif")));
		lblNewLabel.setBounds(10, 392, 81, 99);
		panel_4.add(lblNewLabel);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(MiscCharges.class
				.getResource("/icons/graphics-hospitals-221777.gif")));
		label.setBounds(608, 392, 81, 99);
		panel_4.add(label);
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
	}

	public void getAllDoctors() {
		doctors.addElement("Select Doctor");
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllData();
		try {
			while (resultSet.next()) {
				doctors.addElement(resultSet.getObject(2).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		doctors.addElement("Other");
		doctorNameCB.setModel(doctors);
		doctorNameCB.setSelectedIndex(0);
	}



	private void loadDataToTable() {
		// get size of the hashmap
		int size = examChargesVector.size();
		totalCharges = 0;
		// declare two arrays one for key and other for keyValues

		ObjectArray_examname = new Object[size];

		ObjectArray_examcharges = new Object[size];
		ObjectArray_ListOfexams = new Object[size][2];

		for (int i = 0; i < examName.size(); i++) {
			ObjectArray_examcharges[i] = examChargesVector.get(i);

			ObjectArray_examname[i] = examName.get(i);

			totalCharges = totalCharges
					+ Integer.parseInt(ObjectArray_examcharges[i].toString()
							.trim());
			ObjectArray_ListOfexams[i][0] = examName.get(i);
			ObjectArray_ListOfexams[i][1] = examChargesVector.get(i);
		}
		addTestTable_1.setModel(new DefaultTableModel(ObjectArray_ListOfexams,
				new String[] { "Description", "Charges" }) {

			boolean[] canEdit = new boolean[] { false, false, false };

			@Override
			public boolean isCellEditable(int rowIndex, int columnIndex) {
				return canEdit[columnIndex];
			}
		});
		lblTotalcharges.setText("`" + totalCharges + "");
		lblTotalcharges.setFont(customFont);
	}

	public JTextField getPatientNameTB() {
		return patientNameTB;
	}
}
