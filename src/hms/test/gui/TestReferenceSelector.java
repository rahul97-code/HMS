package hms.test.gui;

import hms.exam.database.ExamDBConnection;
import hms.exam.database.ReferenceTableDBConnection;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import java.awt.Font;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class TestReferenceSelector extends JDialog {

	private JPanel contentPane;
	private JTextField searchTestNameET;
	
	final DefaultComboBoxModel testNameCBModel = new DefaultComboBoxModel();

	String p_id,p_name,p_sex="Male", patient_age="20",p_type="Normal";
	String examName,examTestNo,examID;
	private JComboBox selectTestNameCB;
	
	Vector<String> examRefID = new Vector<String>();
	Vector<String> examRefTableID = new Vector<String>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestReferenceSelector frame = new TestReferenceSelector("","","","","","","");
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public TestReferenceSelector(String p_id,String p_name,String p_age,String p_type,String examName,String examTestNo,String examID) {
		
		
		this.p_id=p_id;
		this.p_sex=p_sex;
		this.p_name=p_name;
		this.patient_age=p_age;
		this.p_type=p_type;
		
		this.examName=examName;
		this.examTestNo=examTestNo;
		this.examID=examID;
		
		setBounds(100, 100, 450, 229);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSearchTestReferences = new JLabel("Search Test References For Results ");
		lblSearchTestReferences.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearchTestReferences.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblSearchTestReferences.setBounds(10, 11, 414, 28);
		contentPane.add(lblSearchTestReferences);
		
		JLabel lblSearchTestName = new JLabel("Search Test Name");
		lblSearchTestName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSearchTestName.setBounds(64, 50, 111, 14);
		contentPane.add(lblSearchTestName);
		
		searchTestNameET = new JTextField();
		searchTestNameET.setFont(new Font("Tahoma", Font.PLAIN, 13));
		searchTestNameET.setBounds(185, 47, 197, 20);
		contentPane.add(searchTestNameET);
		searchTestNameET.setColumns(10);
		

		searchTestNameET.getDocument().addDocumentListener(
				new DocumentListener() {
					@Override
					public void insertUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							selectTestNameCB.setModel(testNameCBModel);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}

					@Override
					public void removeUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							selectTestNameCB.setModel(testNameCBModel);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}

					@Override
					public void changedUpdate(DocumentEvent e) {
						String str = searchTestNameET.getText() + "";
						if (!str.equals("")) {
							testNameCBModel.removeAllElements();
							searchTestName(str);
							searchTestName2(str);
							selectTestNameCB.setModel(testNameCBModel);
						} else {
							testNameCBModel.removeAllElements();
							selectTestNameCB.setModel(testNameCBModel);
						}
					}
				});
		
		JLabel lblSelecTestName = new JLabel("Select Test Name");
		lblSelecTestName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSelecTestName.setBounds(64, 86, 111, 14);
		contentPane.add(lblSelecTestName);
		
		selectTestNameCB = new JComboBox();
		selectTestNameCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		selectTestNameCB.setBounds(185, 83, 197, 20);
		contentPane.add(selectTestNameCB);
	
		selectTestNameCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					 System.out.println(selectTestNameCB.getSelectedItem().toString());
					 String testName=selectTestNameCB.getSelectedItem().toString();
					 getExamReferenceRangeIndex(testName);
					getExamReferenceTableIndex(testName);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
		
		JButton btnAddResults = new JButton("Add Results");
		btnAddResults.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAddResults.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				String[] s = examRefID.toArray(new String[examRefID.size()]);
				final String[] s2 = examRefTableID.toArray(new String[examRefTableID
						.size()]);
				if (examRefID.size() > 0) {
					dispose();
					AddTestReferences addTestReferences = new AddTestReferences(TestReferenceSelector.this.p_name,
							s, TestReferenceSelector.this.examTestNo, TestReferenceSelector.this.examName,
							TestReferenceSelector.this.examID);
					
					addTestReferences.setLocationRelativeTo(contentPane);
					addTestReferences.setModal(true);
					addTestReferences.setVisible(true);
					
				}

				if (examRefTableID.size() > 0) {
					dispose();
					AddTestReferenceTable addReferenceTable = new AddTestReferenceTable(TestReferenceSelector.this.p_name,
							s2, TestReferenceSelector.this.examTestNo, TestReferenceSelector.this.examName,
							TestReferenceSelector.this.examID);
					
					addReferenceTable.setLocationRelativeTo(contentPane);
					addReferenceTable.setModal(true);
					addReferenceTable.setVisible(true);
					
				}
			}
		});
		btnAddResults.setBounds(96, 136, 138, 44);
		contentPane.add(btnAddResults);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setBounds(249, 136, 133, 44);
		contentPane.add(btnCancel);
	}
	
	public void searchTestName(String testName)
	{
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.searchTestName(testName,
				p_sex, patient_age,p_type);
		
		try {
			while (resultSet.next()) {
				
				testNameCBModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	
	public void searchTestName2(String testName)
	{
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.searchTestName2(testName,
				p_sex, patient_age,p_type);
		try {
			while (resultSet.next()) {
				testNameCBModel.addElement(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	
	public void getExamReferenceRangeIndex(String exam_id) {
		examRefID.clear();
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceRangeIndexNew(exam_id,
				p_sex, patient_age,p_type);
		
		try {
			while (resultSet.next()) {
				
				examRefID.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getExamReferenceTableIndex(String exam_id) {
		examRefTableID.clear();
		
		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceTableIndexNew(exam_id,
				p_sex, patient_age,p_type);
		try {
			while (resultSet.next()) {
				examRefTableID.add(resultSet.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public boolean getExamStatus(String exam_id) {
		boolean flag = true;

		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db.retrieveExamStatus(exam_id);
		try {
			while (rs.next()) {

				// System.out.println(rs.getObject(1)+"   "+exam_id);
				if (null == rs.getObject(1)) {
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		return flag;
	}
}
