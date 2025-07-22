package hms.cancellation.gui;

import hms.main.DateFormatChange;
import hms.misc.database.MiscDBConnection;
import hms.opd.gui.OPDBrowser;
import hms.patient.slippdf.MiscSlippdf;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
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

public class CancelMiscSlip extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	public JTextField searchPatientTB;
	private JTextField patientNameTB;
	private Map<String, String> itemsHashMap = new HashMap<String, String>();
	private HashMap examHashMap = new HashMap();
	String[] data = new String[20];
	Object[] ObjectArray_examroom;
	Object[] ObjectArray_examnameid;
	Object[] ObjectArray_examname;
	Object[] ObjectArray_examcharges;
	Object[][] ObjectArray_ListOfexams;
	ButtonGroup opdTypeGP = new ButtonGroup();
	double totalCharges = 0;
	int examStartNo, examEndNo;
	double amount;
	String date, reciept_no;
	DateFormatChange dateFormat = new DateFormatChange();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	String exam_doctorname = "", exam_date = "", exam_note = "",
			exam_counter = "", exam_charge = "", exam_name = "",
			examsub_catname = "", exam_nameid = "", exam_room = "";
	final DefaultComboBoxModel<String> patientID = new DefaultComboBoxModel<String>();
	final DefaultComboBoxModel doctors = new DefaultComboBoxModel();
	final DefaultComboBoxModel miscCat = new DefaultComboBoxModel();
	Vector<String> examCharges = new Vector<String>();
	Vector<String> examName = new Vector<String>();
	Vector<String> examChargesVector = new Vector<String>();
	Vector<String> examIdVector = new Vector<String>();
	Vector examID = new Vector();
	Vector examRoom = new Vector();
	MiscDBConnection miscdbConnection;
	private JTable addTestTable_1;
	private JLabel lblTotalcharges;
	public static Font customFont;
	String descriptionSTR = "MISC SERVICES";
	private JLabel lblTotalAmount;
	private JLabel lblExamType;
	private JLabel lblDate;
	private JTextArea textArea;

	@SuppressWarnings("unchecked")
	/**
	 * Create the frame.
	 */
	public static void main(String[] asd) {
		new CancelMiscSlip().setVisible(true);
	}

	public CancelMiscSlip() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				CancelMiscSlip.class.getResource("/icons/rotaryLogo.png")));
		setTitle("Cancel Misc Slip");
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setResizable(false);
		setBounds(300, 70, 397, 634);
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
		panel_4.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Cancel Misc Slip", TitledBorder.CENTER, TitledBorder.TOP, null, null));
		panel_4.setBounds(6, 5, 372, 590);
		contentPane.add(panel_4);
		panel_4.setLayout(null);

		JLabel lblSearchPatient = new JLabel("Search Reciept :");
		lblSearchPatient.setBounds(10, 23, 108, 14);
		panel_4.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Tahoma", Font.PLAIN, 12));

		searchPatientTB = new JTextField();
		searchPatientTB.setToolTipText("Search Patient");
		searchPatientTB.setBounds(123, 18, 182, 25);
		panel_4.add(searchPatientTB);
		searchPatientTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		searchPatientTB.setColumns(10);
		searchPatientTB.addKeyListener(new KeyAdapter() {
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

		searchPatientTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {

							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientNameTB.setText("");
							patientID.removeAllElements();

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();
							loadDataToTable();
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {
							patientNameTB.setText("");
							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientID.removeAllElements();

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();

							loadDataToTable();
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchPatientTB.getText() + "";
						if (!str.equals("")) {
							getPatientsID(str);
						} else {
							patientNameTB.setText("");
							lblExamType.setText("Exam Category : ");
							lblTotalAmount.setText("Amount : ");
							lblDate.setText("Exam Date : ");
							patientID.removeAllElements();

							itemsHashMap.clear();
							examHashMap.clear();
							examName.clear();
							examChargesVector.clear();
							examIdVector.clear();

							loadDataToTable();
						}
					}
				});
		exam_date = DateFormatChange.StringToMysqlDate(new Date());

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(10, 54, 349, 470);
		panel_4.add(panel_2);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_2.setLayout(null);

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
		btnRemove.setBounds(15, 357, 99, 25);
		panel_2.add(btnRemove);

		JLabel lblTotalCharges = new JLabel("Total Charges :");
		lblTotalCharges.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalCharges.setBounds(137, 358, 106, 23);
		panel_2.add(lblTotalCharges);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(15, 129, 329, 217);
		panel_2.add(scrollPane);

		addTestTable_1 = new JTable();
		addTestTable_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addTestTable_1.setModel(new DefaultTableModel(new Object[][] {},
				new String[] { "Misc Name", "Charges" }));
		addTestTable_1.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						btnRemove.setEnabled(true);
					}
				});
		scrollPane.setViewportView(addTestTable_1);

		lblTotalcharges = new JLabel("");
		lblTotalcharges.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblTotalcharges.setBounds(255, 358, 94, 20);
		panel_2.add(lblTotalcharges);

		lblExamType = new JLabel("Exam Type");
		lblExamType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblExamType.setBounds(21, 47, 303, -6);
		panel_2.add(lblExamType);

		lblTotalAmount = new JLabel("Total Amount");
		lblTotalAmount.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblTotalAmount.setBounds(20, 72, 319, 20);
		panel_2.add(lblTotalAmount);

		lblDate = new JLabel("Date");
		lblDate.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblDate.setBounds(21, 98, 303, 20);
		panel_2.add(lblDate);

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setBounds(21, 16, 108, 14);
		panel_2.add(lblPatientName);
		lblPatientName.setFont(new Font("Tahoma", Font.PLAIN, 12));

		patientNameTB = new JTextField();
		patientNameTB.setBounds(121, 11, 201, 25);
		panel_2.add(patientNameTB);
		patientNameTB.setEditable(false);
		patientNameTB.setFont(new Font("Tahoma", Font.PLAIN, 12));
		patientNameTB.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Comments",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(9, 393, 335, 73);
		panel_2.add(panel);
		panel.setLayout(null);

		textArea = new JTextArea();
		textArea.setFont(new Font("Tahoma", Font.PLAIN, 13));
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setBounds(10, 16, 315, 46);
		panel.add(textArea);

		JButton btnNewButton_3 = new JButton("Cancel And Generate Reciept");
		btnNewButton_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (patientNameTB.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "Please select Slip",
							"Input Error", JOptionPane.ERROR_MESSAGE);
				} else {

					int dialogButton = JOptionPane.YES_NO_OPTION;
					int dialogResult = JOptionPane.showConfirmDialog(
							CancelMiscSlip.this,
							"Are you sure to cancel this slip", "Cancellation",
							dialogButton);
					if (dialogResult == 0) {

						try {

							inserCancelled();
							dispose();
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();

						}
					}

				}
			}
		});
		btnNewButton_3.setBounds(10, 535, 349, 44);
		panel_4.add(btnNewButton_3);
		btnNewButton_3.setFont(new Font("Tahoma", Font.PLAIN, 13));

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
		exam(index);
		System.out.println("working1");

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
					+ Double.parseDouble(ObjectArray_examcharges[i].toString()
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

	public void inserCancelled() throws Exception {
		double finalAmount = amount - totalCharges;
		if (finalAmount==0) {
			JOptionPane.showMessageDialog(null, "Please select item then click remove",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return;
		}
		long timeInMillis = System.currentTimeMillis();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(timeInMillis);
		SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss a");
		CancelledDBConnection cancelledDBConnection = new CancelledDBConnection();
		
		data[0] = "MISC";
		data[1] = p_id;
		data[2] = p_name;
		data[3] = reciept_no;
		data[4] = "" + finalAmount;
		data[5] = "Admin";
		data[6] = date;
		new DateFormatChange();
		data[7] = DateFormatChange.StringToMysqlDate(new Date());
		data[8] = "" + timeFormat.format(cal1.getTime());
		data[9] = textArea.getText();

		cancelledDBConnection.inserData(data);

		cancelledDBConnection.closeConnection();

		if (finalAmount > 0) {
			try {
				new MiscSlippdf(exam_doctorname, ObjectArray_examname,
						ObjectArray_examcharges, totalCharges + "",
						Integer.parseInt(reciept_no), patientNameTB.getText(),p_id,p_insurancetype);
			} catch (DocumentException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void exam(String examID) {
		p_id = "";
		amount=0;
		examName.clear();
		examChargesVector.clear();
		itemsHashMap.clear();
		examHashMap.clear();

		examIdVector.clear();
		try {
			MiscDBConnection db = new MiscDBConnection();
			ResultSet rs = db.retrieveAllDataAmount(examID);
			while (rs.next()) {

				reciept_no = rs.getObject(1).toString();
				lblExamType.setText(""
						+ rs.getObject(2).toString());
				p_id = rs.getObject(3).toString();
				p_name = rs.getObject(4).toString();
				amount = amount+Double.parseDouble(rs.getObject(5).toString());
				date = rs.getObject(6).toString();

				lblTotalAmount.setText("Amount : " + amount);
				lblDate.setText("Exam Date : " + date);
				examName.add(rs.getObject(2).toString());
				examChargesVector.add(rs.getObject(5).toString());
			}
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE,
					null, ex);
		}

		patientNameTB.setText("");
		patientNameTB.setText(p_name);


		loadDataToTable();


	}


}
