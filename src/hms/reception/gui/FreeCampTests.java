package hms.reception.gui;

import hms.amountreceipt.database.AmountReceiptDBConnection;
import hms.crypto.CryptoHandler;
import hms.departments.gui.IndoorProcedureEntry;
import hms.exam.database.ExamDBConnection;
import hms.exams.gui.ExamEntery;
import hms.exams.gui.ExamsBrowser;
import hms.exams.gui.IPDExamEntery;
import hms.main.DateFormatChange;
import hms.opd.gui.OPDEntery;
import hms.patient.database.PatientDBConnection;
import hms.patient.slippdf.ExamSlippdfRegenerate;
import hms.patient.slippdf.FreeExamSlippdfRegenerate;
import hms.patient.slippdf.PO_PDF;
import hms.reception.gui.ReceptionMain;
import hms.reporttables.PatientOnBedReport;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms.store.gui.ItemIssueLogNEW;
import hms.store.gui.ItemBrowser.CustomRenderer;
import hms1.ipd.database.IPDDBConnection;
import hms1.ipd.gui.DischargeSummary;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.itextpdf.text.DocumentException;
import com.sun.prism.paint.Stop;
import com.toedter.calendar.JDateChooser;

import LIS_System.GetLisReportURL;
import LIS_System.LIS_Booking;
import LIS_System.LIS_StatusChecking;
import LIS_System.LIS_Status_result;
import UsersActivity.database.UADBConnection;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;

public class FreeCampTests extends JDialog {

	UADBConnection ua=new UADBConnection();
	Vector<String> examlisCode=new Vector<String>();
	private JPanel contentPane;
	private JTable table;
	ButtonGroup agegroup = new ButtonGroup();
	final DefaultComboBoxModel patientID = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientIDWithaName = new DefaultComboBoxModel();
	final DefaultComboBoxModel patientFamilyHistory = new DefaultComboBoxModel();
	DateFormatChange dateFormat = new DateFormatChange();
	private JDateChooser examDateDC;
	private JDateChooser dateFromDC;
	String dateFrom, examDate;
	private Timer timer;
	private String type="";
	private String search,receipt_id="";
	DefaultComboBoxModel modelCB=new DefaultComboBoxModel();
	String p_id, p_name = "", p_agecode = "age", p_age, p_ageY = "0",
			p_ageM = "0", p_ageD = "0", p_birthdate = "1111-11-11",
			p_sex = "Male", p_address = "", p_city = "", p_telephone = "",
			p_bloodtype = "Unknown", p_guardiantype = "F",
			p_p_father_husband = "", p_insurancetype = "Unknown", p_note = "";
	private String w_ID,P_ID="",p_NAME="",r_ID="",DATE="";
	String idSTR = null, receipt_idSTR = "", patient_idSTR = "", patient_nameSTR = "", p_ageSTR = "", genderSTR = "",
			aadhar_noSTR = "", mob_noSTR = "", addressSTR = "", exam_datetimeSTR = "", hba1cSTR = "", blood_grpSTR = "", 
			bpSTR = "", htSTR = "", wtSTR = "", historySTR = "", report_collectedSTR = "", consultedSTR = "";

	static String patientWorkOrderNo = "";
	public JTextField searchTF;
	private Vector originalTableModel;
	private AbstractButton excelBtn;
	private JTextField patientNameTB;
	private JTextField addressTB;
	private JTextField cityTB;
	private JTextField telephoneTB;
	private JTextField ageTB;
	private JTextField searchReceiptTB;
	private JRadioButton rdbtnMale;
	private JRadioButton rdbtnFemale;
	private JComboBox patientIDCB;
	Vector<String> idV = new Vector<String>();
	Vector<String> receipt_idV = new Vector<String>();
	Vector<String> patient_idV = new Vector<String>();
	Vector<String> patient_nameV = new Vector<String>();
	Vector<String> p_ageV = new Vector<String>();
	Vector<String> genderV = new Vector<String>();
	Vector<String> aadhar_noV = new Vector<String>();
	Vector<String> mob_noV = new Vector<String>();
	Vector<String> addressV = new Vector<String>();
	Vector<String> exam_datetimeV = new Vector<String>();
	Vector<String> hba1cV = new Vector<String>();
	Vector<String> blood_grpV = new Vector<String>();
	Vector<String> bpV = new Vector<String>();
	Vector<String> htV = new Vector<String>();
	Vector<String> wtV = new Vector<String>();
	Vector<String> historyV = new Vector<String>();
	Vector<String> report_collectedV = new Vector<String>();
	Vector<String> consultedV = new Vector<String>();
	Object[][] ObjectArray_ListOfexamsSpecs;
	private JTextField hba1cTF;
	private JTextField bpTF;
	private JTextField htTF;
	private JTextField wtTF;
	private String aadhaar_no="",toDate="",fromDate="";
	private JComboBox bloodGrpCB;
	private JComboBox historyCB;
	private JCheckBox consultedChkBx;
	private JCheckBox reportChkBx;
	private JDateChooser fromDateDC;
	private JDateChooser toDateDC;




	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					ReceptionMain.receptionNameSTR="Arun";
					FreeCampTests frame = new FreeCampTests("");
					frame.setVisible(true);
					//					frame.dispose();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FreeCampTests(final String username) {
		setResizable(false);
		setTitle("Free Test Camp");
		setIconImage(Toolkit.getDefaultToolkit().getImage(FreeCampTests.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100,1250, 650);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		//
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 255, 1226, 346);
		contentPane.add(scrollPane);

		table = new JTable();
		table.setToolTipText("Bouble click to edit\n");
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setModel(new DefaultTableModel(
				new Object[][] {
				},
				new String[] {
						"Receipt ID","Patient ID", "Patient Name","P Age","Gender", "Aadhar No", "Mob No.","Address", "Exam Date", "HbA1c","Blood Grp","Bp","Ht","Wt","History","Report Collected","Consulted"
				}
				));

		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(120);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(130);
		table.getColumnModel().getColumn(2).setMinWidth(130);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setMinWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(120);
		table.getColumnModel().getColumn(4).setMinWidth(120);
		table.getColumnModel().getColumn(5).setPreferredWidth(160);
		table.getColumnModel().getColumn(5).setMinWidth(160);
		table.getColumnModel().getColumn(6).setPreferredWidth(180);
		table.getColumnModel().getColumn(6).setMinWidth(180);
		table.getColumnModel().getColumn(7).setPreferredWidth(100);
		table.getColumnModel().getColumn(7).setMinWidth(100);
		table.getColumnModel().getColumn(8).setPreferredWidth(100);
		table.getColumnModel().getColumn(8).setMinWidth(100);
		table.getColumnModel().getColumn(9).setPreferredWidth(120);
		table.getColumnModel().getColumn(9).setMinWidth(120);
		scrollPane.setViewportView(table);

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

				JTable target = (JTable) arg0.getSource();

				if (arg0.getClickCount() == 1) {
					// do some action
					int row = table.getSelectedRow();	
					Object ob= table.getValueAt(row, 1);
					receipt_id=ob.toString();
				}
				if (arg0.getClickCount() == 2) {
					// do some action

					if(!searchReceiptTB.getText().equals("")) { 
						JOptionPane.showMessageDialog(null,
								"Please Save above searched patient first!", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					int row = table.getSelectedRow();	
					Object ob= table.getValueAt(row, 1);
					receipt_id=ob.toString();


					if(!ob.toString().equals("")) 
					{
						searchReceiptTB.setText(receipt_id);
						idSTR=idV.get(row);
						try {
							examDateDC.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(exam_datetimeV.get(row)));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						hba1cTF.setText(hba1cV.get(row));
						bloodGrpCB.setSelectedItem(blood_grpV.get(row));
						bpTF.setText(bpV.get(row));
						htTF.setText(htV.get(row));
						wtTF.setText(wtV.get(row));
						historyCB.setSelectedItem(historyV.get(row));
						consultedChkBx.setSelected(consultedV.get(row).equals("1")?true:false);
						reportChkBx.setSelected(report_collectedV.get(row).equals("1")?true:false);


						idV.remove(row);
						receipt_idV.remove(row);
						patient_idV.remove(row);
						patient_nameV.remove(row);
						p_ageV.remove(row);
						genderV.remove(row);
						aadhar_noV.remove(row);
						mob_noV.remove(row);
						addressV.remove(row);
						exam_datetimeV.remove(row);
						hba1cV.remove(row);
						blood_grpV.remove(row);
						bpV.remove(row);
						htV.remove(row);
						wtV.remove(row);
						historyV.remove(row);
						report_collectedV.remove(row);
						consultedV.remove(row);
						populateTable();

					}
				}	

			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 1226, 210);
		contentPane.add(panel);
		panel.setLayout(null);
		panel.setBorder(new TitledBorder(
			    UIManager.getBorder("TitledBorder.border"),
			    "Welcome : " + ReceptionMain.receptionNameSTR,
			    TitledBorder.RIGHT,
			    TitledBorder.BOTTOM,
			    new Font("Tahoma", Font.ITALIC, 12),
			    new Color(255, 102, 102)
			));


		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(418, 29, 108, 89);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setVerticalAlignment(SwingConstants.TOP);
		lblNewLabel.setIcon(new ImageIcon(FreeCampTests.class.getResource("/icons/graphics-hospitals-221777.gif")));
		panel.add(lblNewLabel);

		JLabel lblNewLabel_2 = new JLabel("");
		lblNewLabel_2.setBorder(new LineBorder(UIManager.getColor("Button.select")));
		lblNewLabel_2.setBounds(176, 18, 377, 2);
		panel.add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("Free Test Camp");
		lblNewLabel_3.setForeground(UIManager.getColor("CheckBoxMenuItem.acceleratorForeground"));
		lblNewLabel_3.setFont(new Font("Dialog", Font.ITALIC, 16));
		lblNewLabel_3.setBounds(13, 2, 183, 31);
		panel.add(lblNewLabel_3);

		searchReceiptTB = new JTextField();
		searchReceiptTB.setBounds(165, 51, 152, 25);
		panel.add(searchReceiptTB);
		searchReceiptTB.setToolTipText("Search Patient");
		searchReceiptTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		searchReceiptTB.setColumns(10);

		JButton searchBT = new JButton("");
		searchBT.setBounds(317, 51, 28, 25);
		panel.add(searchBT);
		searchBT.setToolTipText("Search");
		searchBT.setFocusable(true);
		searchBT.setIcon(new ImageIcon(OPDEntery.class
				.getResource("/icons/zoom_r_button.png")));

		JLabel lblSearchPatient_1 = new JLabel("Search Receipt :");
		lblSearchPatient_1.setBounds(39, 58, 108, 14);
		panel.add(lblSearchPatient_1);
		lblSearchPatient_1.setFont(new Font("Dialog", Font.PLAIN, 12));

		patientIDCB = new JComboBox();
		patientIDCB.setBounds(163, 88, 183, 25);
		panel.add(patientIDCB);
		patientIDCB.setFont(new Font("Dialog", Font.PLAIN, 12));

		JLabel lblPatientId = new JLabel("Patient ID :");
		lblPatientId.setBounds(46, 90, 77, 20);
		panel.add(lblPatientId);
		lblPatientId.setFont(new Font("Dialog", Font.PLAIN, 12));

		JPanel panel_1 = new JPanel();
		panel_1.setBounds(571, 9, 649, 120);
		panel.add(panel_1);
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Patient Detail",
				TitledBorder.RIGHT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));

		JLabel lblPatientName = new JLabel("Patient Name :");
		lblPatientName.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblPatientName.setBounds(19, 21, 108, 14);
		panel_1.add(lblPatientName);

		patientNameTB = new JTextField();
		patientNameTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		patientNameTB.setEditable(false);
		patientNameTB.setColumns(10);
		patientNameTB.setBounds(119, 16, 201, 25);
		panel_1.add(patientNameTB);

		JLabel lblNewLabel_1 = new JLabel("Address :");
		lblNewLabel_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(19, 57, 108, 14);
		panel_1.add(lblNewLabel_1);

		JLabel lblNewLabel_2_1 = new JLabel("City :");
		lblNewLabel_2_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_2_1.setBounds(19, 93, 93, 17);
		panel_1.add(lblNewLabel_2_1);

		JLabel lblTelephone = new JLabel("Telephone :");
		lblTelephone.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTelephone.setBounds(338, 20, 108, 17);
		panel_1.add(lblTelephone);

		JLabel lblAge = new JLabel("Age :");
		lblAge.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAge.setBounds(339, 56, 93, 17);
		panel_1.add(lblAge);

		JLabel lblSex = new JLabel("Sex :");
		lblSex.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblSex.setBounds(340, 94, 80, 14);
		panel_1.add(lblSex);

		addressTB = new JTextField();
		addressTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		addressTB.setEditable(false);
		addressTB.setColumns(10);
		addressTB.setBounds(119, 52, 201, 25);
		panel_1.add(addressTB);

		cityTB = new JTextField();
		cityTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		cityTB.setEditable(false);
		cityTB.setColumns(10);
		cityTB.setBounds(119, 88, 201, 25);
		panel_1.add(cityTB);

		telephoneTB = new JTextField();
		telephoneTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		telephoneTB.setEditable(false);
		telephoneTB.setColumns(10);
		telephoneTB.setBounds(426, 16, 201, 25);
		panel_1.add(telephoneTB);

		ageTB = new JTextField();
		ageTB.setFont(new Font("Dialog", Font.PLAIN, 12));
		ageTB.setEditable(false);
		ageTB.setColumns(10);
		ageTB.setBounds(426, 52, 201, 25);
		panel_1.add(ageTB);

		rdbtnMale = new JRadioButton("Male");
		rdbtnMale.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnMale.setEnabled(false);
		rdbtnMale.setBounds(432, 87, 80, 23);
		panel_1.add(rdbtnMale);

		rdbtnFemale = new JRadioButton("Female");
		rdbtnFemale.setFont(new Font("Dialog", Font.PLAIN, 12));
		rdbtnFemale.setEnabled(false);
		rdbtnFemale.setBounds(514, 87, 109, 23);
		panel_1.add(rdbtnFemale);

		JPanel panel_2 = new JPanel();
		panel_2.setBounds(13, 133, 1207, 65);
		panel.add(panel_2);
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Exam Detail",
				TitledBorder.LEFT, TitledBorder.TOP, new Font("Tahoma",
						Font.PLAIN, 12), null));

		htTF = new JTextField();
		htTF.setBounds(380, 30, 77, 20);
		panel_2.add(htTF);
		htTF.setColumns(10);

		wtTF = new JTextField();
		wtTF.setBounds(469, 30, 77, 20);
		panel_2.add(wtTF);
		wtTF.setColumns(10);

		JLabel lblNewLabel_1_4 = new JLabel("Wt :");
		lblNewLabel_1_4.setBounds(380, 13, 108, 14);
		panel_2.add(lblNewLabel_1_4);
		lblNewLabel_1_4.setFont(new Font("Dialog", Font.PLAIN, 12));

		JLabel lblNewLabel_1_3 = new JLabel("Bp :");
		lblNewLabel_1_3.setBounds(289, 13, 108, 14);
		panel_2.add(lblNewLabel_1_3);
		lblNewLabel_1_3.setFont(new Font("Dialog", Font.PLAIN, 12));

		String[] bloodGroups = {"Select","N/A","A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-"};
		bloodGrpCB = new JComboBox(bloodGroups);
		bloodGrpCB.setBounds(742, 30, 101, 20);
		panel_2.add(bloodGrpCB);
		bloodGrpCB.setFont(new Font("Dialog", Font.PLAIN, 12));

		bpTF = new JTextField();
		bpTF.setBounds(288, 30, 77, 20);
		panel_2.add(bpTF);
		bpTF.setColumns(10);

		hba1cTF = new JTextField();
		hba1cTF.setBounds(150, 30, 126, 20);
		panel_2.add(hba1cTF);
		hba1cTF.setColumns(10);

		JLabel lblNewLabel_1_2 = new JLabel("HbA1c :");
		lblNewLabel_1_2.setBounds(188, 13, 108, 14);
		panel_2.add(lblNewLabel_1_2);
		lblNewLabel_1_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		examDateDC = new JDateChooser();
		examDateDC.setBounds(12, 30, 126, 20);
		panel_2.add(examDateDC);
		examDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							examDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							//populateTable(dateFrom, examDate,type);
						}
					}
				});
		examDateDC.setDate(new Date());
		examDateDC.setMaxSelectableDate(new Date());
		examDateDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblNewLabel_1_1 = new JLabel("Date :");
		lblNewLabel_1_1.setBounds(52, 13, 108, 14);
		panel_2.add(lblNewLabel_1_1);
		lblNewLabel_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));

		JLabel lblNewLabel_1_3_1 = new JLabel("Ht :");
		lblNewLabel_1_3_1.setBounds(469, 13, 108, 14);
		panel_2.add(lblNewLabel_1_3_1);
		lblNewLabel_1_3_1.setFont(new Font("Dialog", Font.PLAIN, 12));

		historyCB = new JComboBox();
		historyCB.setBounds(855, 29, 234, 20);
		historyCB.setEditable(true);
		historyCB.setFont(new Font("Dialog", Font.PLAIN, 12));
		final JTextField ItemLocationtext = (JTextField) historyCB.getEditor()
				.getEditorComponent();
		ItemLocationtext.addCaretListener(new CaretListener() {
			@Override
			public void caretUpdate(CaretEvent e) {
				String text = ItemLocationtext.getText();
				if (!text.equals("")) {
					historySTR = text;
				}
			}
		});
		panel_2.add(historyCB);
		getAllFamilyHistory();

		JLabel lblNewLabel_1_3_1_1 = new JLabel("Blood Grp :");
		lblNewLabel_1_3_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1_3_1_1.setBounds(742, 13, 108, 14);
		panel_2.add(lblNewLabel_1_3_1_1);

		JLabel lblNewLabel_1_3_1_2 = new JLabel("Family History :");
		lblNewLabel_1_3_1_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1_3_1_2.setBounds(855, 12, 108, 14);
		panel_2.add(lblNewLabel_1_3_1_2);

		consultedChkBx = new JCheckBox("");
		consultedChkBx.setBounds(682, 36, 33, 20);
		panel_2.add(consultedChkBx);

		reportChkBx = new JCheckBox("");
		reportChkBx.setBounds(682, 12, 33, 20);
		panel_2.add(reportChkBx);

		JLabel lblNewLabel_1_3_1_1_1 = new JLabel("Report Collected?  :");
		lblNewLabel_1_3_1_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1_3_1_1_1.setBounds(554, 15, 126, 14);
		panel_2.add(lblNewLabel_1_3_1_1_1);

		JLabel lblNewLabel_1_3_1_1_1_1 = new JLabel("Consulted?    :");
		lblNewLabel_1_3_1_1_1_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblNewLabel_1_3_1_1_1_1.setBounds(554, 38, 126, 14);
		panel_2.add(lblNewLabel_1_3_1_1_1_1);

		JButton btnNewButton = new JButton("Save");
		btnNewButton.setIcon(new ImageIcon(FreeCampTests.class.getResource("/icons/SAVE.PNG")));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(patientNameTB.getText().equals("")) { 
					JOptionPane.showMessageDialog(null,
							"Please search patient first!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(bloodGrpCB.getSelectedIndex()==0) {
					JOptionPane.showMessageDialog(null,
							"Please select blood group!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				if(historyCB.getSelectedItem().equals("Select")) {
					JOptionPane.showMessageDialog(null,
							"Please select family history!", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				String[] examData = new String[] {idSTR,
						searchReceiptTB.getText(),p_id,p_name,p_age,p_sex,aadhaar_no,telephoneTB.getText(),
						addressTB.getText() + " " + cityTB.getText(),examDate,hba1cTF.getText(),bloodGrpCB.getSelectedItem().toString(),
						bpTF.getText(),htTF.getText(),wtTF.getText(),historySTR,reportChkBx.isSelected() ? "1" : "0",
								consultedChkBx.isSelected() ? "1" : "0",
										ReceptionMain.receptionIdSTR,ReceptionMain.receptionNameSTR
				};
				System.out.println(Arrays.toString(examData));
				ExamDBConnection DB=new ExamDBConnection();
				try {
					DB.insertFreeTestData(examData);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				DB.closeConnection();
				getAllFamilyHistory();
				getAllFreeTestData(fromDate,toDate);


				searchReceiptTB.setText("");
				examDateDC.setDate(new Date());
				hba1cTF.setText("");
				bpTF.setText("");
				htTF.setText("");
				wtTF.setText("");
				consultedChkBx.setSelected(false);
				reportChkBx.setSelected(false);
				bloodGrpCB.setSelectedIndex(0);
				patientIDWithaName.removeAllElements();
				patientIDCB.setModel(patientIDWithaName);
				idSTR=null;
			}
		});
		btnNewButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		btnNewButton.setBounds(1101, 29, 94, 20);
		panel_2.add(btnNewButton);
		patientIDCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					p_id =patientID.getElementAt(patientIDCB.getSelectedIndex()).toString().toString();
				} catch (Exception e) {
					// TODO: handle exception

				}
				patientNameTB.setText("");
				addressTB.setText("");
				ageTB.setText("");
				cityTB.setText("");
				telephoneTB.setText("");
				rdbtnMale.setSelected(false);
				rdbtnFemale.setSelected(false);
				if(!p_id.equals("")){
					setPatientDetail(p_id);
				}
				if (patientIDWithaName.getSize() > 0) {
					patientNameTB.setText(p_name);
					addressTB.setText(p_address);
					ageTB.setText(p_age + "  (Y-M-D)");
					cityTB.setText(p_city);
					telephoneTB.setText(p_telephone);
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

		searchReceiptTB.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();

					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						if (timer.isRunning()) {
							timer.stop();
						}
						timer.start();
					}
				});

		timer = new Timer(800, new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				// highlightAll();
				timer.stop();
				String str = searchReceiptTB.getText() + "";
				if (!str.equals("")) {
					getPatientsID(str);
				} else {
					patientNameTB.setText("");
					addressTB.setText("");
					ageTB.setText("");
					cityTB.setText("");
					telephoneTB.setText("");
					rdbtnMale.setSelected(false);
					rdbtnFemale.setSelected(false);
					patientIDWithaName.removeAllElements();
					patientIDCB.setModel(patientIDWithaName);

				}
			}
		});

		searchTF = new JTextField();
		searchTF.setBounds(100, 228, 151, 20);
		contentPane.add(searchTF);
		searchTF.setColumns(10);
		searchTF.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);

					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTF.getText() + "";
						searchTableContents(str);
					}
				});
		JLabel lblSearchPatient = new JLabel("Search :");
		lblSearchPatient.setBounds(37, 229, 121, 14);
		contentPane.add(lblSearchPatient);
		lblSearchPatient.setFont(new Font("Dialog", Font.PLAIN, 12));

		fromDateDC = new JDateChooser();
		fromDateDC.setDateFormatString("yyyy-MM-dd");
		fromDateDC.setBounds(342, 227, 126, 20);
		contentPane.add(fromDateDC);
		fromDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							fromDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							getAllFreeTestData(fromDate,toDate);
						}
					}
				});
		fromDateDC.setDate(new Date());
		fromDateDC.setDateFormatString("yyyy-MM-dd");

		toDateDC = new JDateChooser();
		toDateDC.setDateFormatString("yyyy-MM-dd");
		toDateDC.setBounds(507, 227, 126, 20);
		contentPane.add(toDateDC);
		toDateDC.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							toDate = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							getAllFreeTestData(fromDate,toDate);
						}
					}
				});
		toDateDC.setDate(new Date());
		toDateDC.setMaxSelectableDate(new Date());
		toDateDC.setDateFormatString("yyyy-MM-dd");

		JLabel lblFrom = new JLabel("From :");
		lblFrom.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblFrom.setBounds(297, 229, 121, 14);
		contentPane.add(lblFrom);

		JLabel lblTo = new JLabel("To :");
		lblTo.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblTo.setBounds(477, 229, 93, 14);
		contentPane.add(lblTo);

		excelBtn = new JButton("Excel");
		excelBtn.setBounds(817, 227, 117, 20);
		contentPane.add(excelBtn);
		excelBtn.setFont(new Font("Dialog", Font.PLAIN, 12));
		excelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setSelectedFile(new File("FreeTestData_"+fromDate+"_"+toDate+".xls"));
				if (fileChooser.showSaveDialog(FreeCampTests.this) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					new ItemIssueLogNEW().ReportExcel(table, file.toPath().toString());
				}
			}
		});
		excelBtn.setIcon(new ImageIcon(FreeCampTests.class
				.getResource("/icons/1BL.PNG")));

		JButton excelBtn_1 = new JButton("Close");
		excelBtn_1.setIcon(new ImageIcon(FreeCampTests.class.getResource("/icons/CANCEL.PNG")));
		excelBtn_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		excelBtn_1.setFont(new Font("Dialog", Font.PLAIN, 12));
		excelBtn_1.setBounds(1103, 227, 117, 20);

		contentPane.add(excelBtn_1);

		JButton excelBtn_2 = new JButton("Report");
		excelBtn_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!receipt_id.equals("")) {
					ExamDBConnection DB=new ExamDBConnection();
					String wrkorderID=DB.retrieveWorkOrderID(receipt_id);
					DB.closeConnection();
					GetLisReportURL GetLisReportURL=new GetLisReportURL();
					String url=GetLisReportURL.getURL(wrkorderID, "",true); 
					Desktop desk=Desktop.getDesktop();
					try { 
						desk.browse(new URI(url));
					} catch (IOException | URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}else {
					JOptionPane 
					.showMessageDialog(
							null,
							"Please select row!",
							"Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		});
		excelBtn_2.setIcon(new ImageIcon(FreeCampTests.class.getResource("/icons/xp.png")));
		excelBtn_2.setFont(new Font("Dialog", Font.PLAIN, 12));
		excelBtn_2.setBounds(694, 227, 117, 20);
		contentPane.add(excelBtn_2);
		
		JButton excelBtn_3 = new JButton("");
		excelBtn_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ReceptionistSettings receptionistSettings = new ReceptionistSettings(
						username);
				receptionistSettings.setModal(true);
				receptionistSettings.setVisible(true);
			}
		});
		excelBtn_3.setIcon(new ImageIcon(FreeCampTests.class.getResource("/icons/setting.png")));
		excelBtn_3.setFont(new Font("Dialog", Font.PLAIN, 12));
		excelBtn_3.setBounds(942, 227, 34, 20);
		contentPane.add(excelBtn_3);
		searchTF.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void insertUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
				String search=searchTF.getText()+"";
				searchTableContents(search);

			}
		});
		getAllFreeTestData(fromDate, toDate);
		searchReceiptTB.requestFocus();
		
	}

	public void searchTableContents(String searchString) {
		DefaultTableModel currtableModel = (DefaultTableModel) table.getModel();
		if(currtableModel.getRowCount()>0 )
		{
			currtableModel.setRowCount(0);
			for (Object rows : originalTableModel) {
				Vector rowVector = (Vector) rows;
				for (Object column : rowVector) {
					if (column.toString().toLowerCase()
							.contains(searchString.toLowerCase())) {
						currtableModel.addRow(rowVector);
						break;
					}
				}
			}
		}
	}

	public void populateTable() {
		int size = idV.size();

		double total = 0;

		ObjectArray_ListOfexamsSpecs = new Object[size][18];

		for (int i = 0; i < idV.size(); i++) {
			ObjectArray_ListOfexamsSpecs[i][0] = idV.get(i);                  // Column 1 - id
			ObjectArray_ListOfexamsSpecs[i][1] = receipt_idV.get(i);           // Column 2 - receipt_id
			ObjectArray_ListOfexamsSpecs[i][2] = patient_idV.get(i);           // Column 3 - patient_id
			ObjectArray_ListOfexamsSpecs[i][3] = patient_nameV.get(i);         // Column 4 - patient_name
			ObjectArray_ListOfexamsSpecs[i][4] = p_ageV.get(i);                // Column 5 - p_age
			ObjectArray_ListOfexamsSpecs[i][5] = genderV.get(i);               // Column 6 - gender
			ObjectArray_ListOfexamsSpecs[i][6] = aadhar_noV.get(i);            // Column 7 - aadhar_no
			ObjectArray_ListOfexamsSpecs[i][7] = mob_noV.get(i);               // Column 8 - mob_no
			ObjectArray_ListOfexamsSpecs[i][8] = addressV.get(i);              // Column 9 - address
			ObjectArray_ListOfexamsSpecs[i][9] = exam_datetimeV.get(i);        // Column 10 - exam_datetime
			ObjectArray_ListOfexamsSpecs[i][10] = hba1cV.get(i);               // Column 11 - hba1c
			ObjectArray_ListOfexamsSpecs[i][11] = blood_grpV.get(i);           // Column 12 - blood_grp
			ObjectArray_ListOfexamsSpecs[i][12] = bpV.get(i);                  // Column 13 - bp
			ObjectArray_ListOfexamsSpecs[i][13] = htV.get(i);                  // Column 14 - ht
			ObjectArray_ListOfexamsSpecs[i][14] = wtV.get(i);                  // Column 15 - wt
			ObjectArray_ListOfexamsSpecs[i][15] = historyV.get(i);             // Column 16 - history
			ObjectArray_ListOfexamsSpecs[i][16] = report_collectedV.get(i).equals("1")?"Yes":"No";    // Column 17 - report_collected
			ObjectArray_ListOfexamsSpecs[i][17] = consultedV.get(i).equals("1")?"Yes":"No";           // Column 18 - consulted
		}
		DefaultTableModel model = new DefaultTableModel(ObjectArray_ListOfexamsSpecs,new String[] { 		
				"ID","Receipt ID","Patient ID", "Patient Name","P Age","Gender", "Aadhar No", "Mob No.","Address", "Exam Date", "HbA1c","Blood Grp","Bp","Ht","Wt","History","Report Collected","Consulted"
		}) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;// This causes all cells to be not editable
			}
		};
		table.setModel(model);
		originalTableModel = (Vector) ((DefaultTableModel) table.getModel()).getDataVector().clone();
		fitColumnsToContent(table);

		table.setDefaultRenderer(Object.class, new RedYellowRenderer());

		if(table.getRowCount()>0)
		{
			get();
		}

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
				aadhaar_no = new CryptoHandler().decrypt(resultSet.getString(13));
				System.out.println("aadhaar_no: "+aadhaar_no);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();

	}
	class RedYellowRenderer extends DefaultTableCellRenderer {
		Color Purple = new Color(102,0,153);
		Color c2 = new Color(255,204,30);
		RedYellowRenderer() {
			setHorizontalAlignment(CENTER);
		}
		@Override
		public Component getTableCellRendererComponent(
				JTable table, Object value,
				boolean isSelected, boolean hasFocus,
				int row, int column
				) {
			Component c = super.getTableCellRendererComponent(
					table, value, isSelected, hasFocus, row, column
					);
			if(table.getValueAt(row, 16).equals("Yes") && table.getValueAt(row, 17).equals("Yes") && table.getRowCount() > 0) {
				c.setBackground(Color.decode("#90EE90")); 
				c.setForeground(Color.BLACK);
			}else if(table.getValueAt(row, 16).equals("No") && table.getValueAt(row, 17).equals("Yes") && table.getRowCount() > 0) {
				c.setBackground(Color.decode("#FFB6C1"));  
				c.setForeground(Color.BLACK);
			}else if(table.getValueAt(row, 16).equals("Yes") && table.getValueAt(row, 17).equals("No") && table.getRowCount() > 0) {
				c.setBackground(Color.decode("#FFD700"));  
				c.setForeground(Color.BLACK);
			}else{
				c.setBackground(Color.white);
				c.setForeground(Color.black);
			}
			if (isSelected) {
				((JComponent) c).setBorder(BorderFactory.createLineBorder(Color.BLACK,1));
			} else {
				((JComponent) c).setBorder(BorderFactory.createEmptyBorder());
			}

			return c;
		}
	}
	class ImagePanel extends JComponent {
		private Image image;
		public ImagePanel(Image image) {
			this.image = image;
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.drawImage(image, 0, 0, this);
		}
	}
	public void getPatientsID(String index) {
		ExamDBConnection patientDBConnection = new ExamDBConnection();
		ResultSet resultSet = patientDBConnection.retrievePID(index);
		patientID.removeAllElements();
		patientIDWithaName.removeAllElements();
		try {
			while (resultSet.next()) {
				patientID.addElement(resultSet.getObject(1).toString());
				patientIDWithaName.addElement(resultSet.getObject(1).toString()+"("+resultSet.getObject(2).toString()+")");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		patientIDCB.setModel(patientIDWithaName);
		if(patientIDWithaName.getSize()>0)
			patientIDCB.setSelectedIndex(0);
	}

	public void getAllFreeTestData(String fromDate,String toDate) {
		clearAllVectors();
		ExamDBConnection patientDBConnection = new ExamDBConnection();
		ResultSet rs = patientDBConnection.retrieveFreeTestData(fromDate,toDate);
		try {
			while (rs.next()) {
				idV.add(rs.getString(1));               
				receipt_idV.add(rs.getString(2));     
				patient_idV.add(rs.getString(3));    
				patient_nameV.add(rs.getString(4));     
				p_ageV.add(rs.getString(5));           
				genderV.add(rs.getString(6));           
				aadhar_noV.add(rs.getString(7));        
				mob_noV.add(rs.getString(8));          
				addressV.add(rs.getString(9));          
				exam_datetimeV.add(rs.getString(10));  
				hba1cV.add(rs.getString(11));          
				blood_grpV.add(rs.getString(12));       
				bpV.add(rs.getString(13));             
				htV.add(rs.getString(14));              
				wtV.add(rs.getString(15));              
				historyV.add(rs.getString(16));         
				report_collectedV.add(rs.getString(17));
				consultedV.add(rs.getString(18));      
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		populateTable();
	}
	public void getAllFamilyHistory() {
		ExamDBConnection patientDBConnection = new ExamDBConnection();
		ResultSet rs = patientDBConnection.retrieveFamilyHistory();
		patientFamilyHistory.removeAllElements();
		patientFamilyHistory.addElement("Select");
		try {
			while (rs.next()) {
				patientFamilyHistory.addElement(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		patientDBConnection.closeConnection();
		if(patientFamilyHistory.getSize()>0)
			historyCB.setModel(patientFamilyHistory);
		historyCB.setSelectedIndex(0);
	}
	public void clearAllVectors() {
		idV.clear();
		receipt_idV.clear();
		patient_idV.clear();
		patient_nameV.clear();
		p_ageV.clear();
		genderV.clear();
		aadhar_noV.clear();
		mob_noV.clear();
		addressV.clear();
		exam_datetimeV.clear();
		hba1cV.clear();
		blood_grpV.clear();
		bpV.clear();
		htV.clear();
		wtV.clear();
		historyV.clear();
		report_collectedV.clear();
		consultedV.clear();
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


	private void get() {
		// TODO Auto-generated method stub
		table.setAutoCreateRowSorter(true);
	}
}
