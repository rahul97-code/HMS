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
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JComboBox;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class TestResults extends JDialog {

	private final JPanel contentPanel = new JPanel();
	final DefaultComboBoxModel reportTypeCBM = new DefaultComboBoxModel();
	String reportTypeSTR,ref_id;
	private JComboBox selectReportTypeCB;
	private JTextArea testResultTA;
	boolean valueSet;
	String exam_nameSTR;
	JTextArea commentTA;
	/**
	 * Create the dialog.
	 */
	public TestResults(String p_name, final String exam_id,
			final String exam_name, final String examName_id) {
		setTitle("Result Value");
		setIconImage(Toolkit.getDefaultToolkit().getImage(TestResults.class.getResource("/icons/rotaryLogo.png")));
		setResizable(false);
		setBounds(100, 100, 608, 266);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		exam_nameSTR=exam_name;
		contentPanel.setLayout(null);
		{
			JLabel label = new JLabel(""+exam_nameSTR);
			label.setFont(new Font("Tahoma", Font.BOLD, 13));
			label.setBounds(10, 11, 210, 45);
			contentPanel.add(label);
		}
		{
			JLabel label = new JLabel("Report Type :");
			label.setFont(new Font("Tahoma", Font.BOLD, 13));
			label.setBounds(223, 23, 132, 20);
			contentPanel.add(label);
		}
		{
			selectReportTypeCB = new JComboBox();
			selectReportTypeCB.setFont(new Font("Tahoma", Font.BOLD, 13));
			selectReportTypeCB.setBounds(365, 19, 221, 29);
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
		}
		{
			JPanel panel = new JPanel();
			panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Results :", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			panel.setBounds(44, 63, 518, 115);
			contentPanel.add(panel);
			panel.setLayout(null);
			
			JScrollPane scrollPane = new JScrollPane();
			scrollPane.setBounds(6, 16, 506, 92);
			panel.add(scrollPane);
			
			testResultTA = new JTextArea();
			testResultTA.setFont(new Font("Tahoma", Font.BOLD, 14));
			testResultTA.setRows(10);
			scrollPane.setViewportView(testResultTA);
		}
		{
			JButton button = new JButton("Save");
			button.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					
					if (reportTypeSTR.equals("Select Report Type")) {
						JOptionPane.showMessageDialog(null,
								"Please Select The Report Type", "Input Error",
								JOptionPane.ERROR_MESSAGE);
						return;
					}
					TestResultDBConnection dbConnection = new TestResultDBConnection();

					String[] data = new String[40];
						if (!valueSet) {

							data[0] = exam_id;
							data[1] = examName_id;
							data[2] = ""+exam_nameSTR;
							data[3] = "N/A";
							data[4] = "N/A";
							data[5] =testResultTA.getText().toString();
							data[6] = "N/A";
							data[7] = "N/A";
							data[8] = "N/A";
							data[9] = "200";
							data[10]="None";
							data[11]=commentTA.getText().toString();
							

							try {
								dbConnection.inserData(data);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								JOptionPane.showMessageDialog(null, "" + e1,
										"Input Error", JOptionPane.ERROR_MESSAGE);
								dbConnection.closeConnection();
								return;
							}
						}
						else
						{
							try {
								dbConnection.updateTestValue(ref_id,testResultTA.getText().toString(),"N/A","N/A",commentTA.getText().toString());
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					updateTestData(exam_id + "", "" + reportTypeSTR);
					dbConnection.closeConnection();
					dispose();
				}
			});
			button.setFont(new Font("Tahoma", Font.BOLD, 13));
			button.setBounds(292, 180, 143, 37);
			contentPanel.add(button);
		}
		{
			JButton button = new JButton("Cancel");
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}
			});
			button.setFont(new Font("Tahoma", Font.BOLD, 13));
			button.setBounds(443, 180, 143, 37);
			contentPanel.add(button);
		}
		
		 commentTA = new JTextArea();
		commentTA.setBounds(89, 180, 187, 37);
		contentPanel.add(commentTA);
		
		JLabel lblComment = new JLabel("Comment");
		lblComment.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblComment.setBounds(10, 189, 71, 20);
		contentPanel.add(lblComment);
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
	//	System.out.println(lblTestName.getText().toString()+"  ;;;;;  "+examCounter);
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveDATA(examCounter, ""+exam_nameSTR);
		try {
			while (resultSet.next()) {
				ref_id=resultSet.getObject(1).toString();
				testResultTA.setText(resultSet.getObject(2).toString());
			//	enterValueTB.setEditable(false);
				 valueSet=true;
			//	 System.out.println("value   :-  "+enterValueTB.getText());
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
