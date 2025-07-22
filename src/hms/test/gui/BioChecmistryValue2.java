package hms.test.gui;

import hms.exam.database.ExamDBConnection;
import hms.exam.database.TestMasterDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.BorderLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JComboBox;

import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Toolkit;

public class BioChecmistryValue2 extends JDialog {

	private final JPanel contentPanel = new JPanel();
	JTextField[] values = new JTextField[25];
	private JComboBox selectReportTypeCB;
	final DefaultComboBoxModel reportTypeCBM = new DefaultComboBoxModel();
	String reportTypeSTR,ref_id="",exam_name;
	boolean valueSet;
	String[] valueSTR=new String[30];
	String exam_counter = "0", examname_id = "0", examsub_name = "WIDAL",
			lower_limit = "0", upper_limit = "0", results_value = "0",
			comments = "0", results1 = "0",
			results2 = "", category = "300",exam_nameSTR;
	/**
	 * Create the dialog.
	 */
	public BioChecmistryValue2(String p_name, final String exam_id,
			final String exam_name,final String examName_id) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(BioChecmistryValue2.class.getResource("/icons/rotaryLogo.png")));
		setTitle("WIDAL TEST VALUES");
		setResizable(false);
		setBounds(100, 100, 627, 424);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		exam_nameSTR=exam_name;
		selectReportTypeCB = new JComboBox();
		selectReportTypeCB.setFont(new Font("Tahoma", Font.BOLD, 13));
		selectReportTypeCB.setBounds(365, 21, 221, 29);
		contentPanel.add(selectReportTypeCB);
		selectReportTypeCB.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					reportTypeSTR = selectReportTypeCB.getSelectedItem()
							.toString();
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		});
	
		
		JButton btnNewButton = new JButton("Save");
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {

				if(reportTypeSTR.equals("Select Report Type"))
				{
					JOptionPane.showMessageDialog(null,
							"Please Select The Report Type", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
				else {
					
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton.setBounds(292, 338, 143, 37);
		contentPanel.add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnNewButton_1.setBounds(443, 338, 143, 37);
		contentPanel.add(btnNewButton_1);
		
		JLabel label = new JLabel(""+exam_nameSTR);
		label.setFont(new Font("Tahoma", Font.BOLD, 13));
		label.setBounds(10, 13, 210, 45);
		contentPanel.add(label);
		
		JLabel lblReportType = new JLabel("Report Type :");
		lblReportType.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblReportType.setBounds(223, 25, 132, 20);
		contentPanel.add(lblReportType);
		getReportTypes();
		getData(exam_id);
		getReportType(exam_id);
	}
	
	
	public void getReportTypes() {

		reportTypeCBM.removeAllElements();
		reportTypeCBM.addElement("Select Report Type");
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllReportType();
		try {
			while (resultSet.next()) {

				reportTypeCBM.addElement(resultSet.getObject(2).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
		selectReportTypeCB.setModel(reportTypeCBM);
		selectReportTypeCB.setSelectedIndex(0);

	}
	public void getReportType(String exam_id) {
		ExamDBConnection db = new ExamDBConnection();
		ResultSet resultSet = db.retrieveExamStatus(exam_id);
		Object type="";
		try {
			while (resultSet.next()) {

				type=resultSet.getObject(1);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		if(type!=null)
		{
			selectReportTypeCB.setSelectedItem(type);
		}
		
	}
	public void getData(String examCounter)
	{
		System.out.println(examCounter+"   "+exam_name);
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveDATA2(examCounter,exam_name);
		try {
			while (resultSet.next()) {
				ref_id=resultSet.getObject(1).toString();
				for (int i = 0; i <24; i++) {
					String str=resultSet.getObject(i+2).toString();
					
					if(!str.equals("0"))
						values[i].setText(""+str);
					
				}
				
				 valueSet=true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public void updateTestData(String exam_id, String exam_type) {
		ExamDBConnection db = new ExamDBConnection();
		try {
			db.updateExamReportType(exam_id, exam_type);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
	
}
