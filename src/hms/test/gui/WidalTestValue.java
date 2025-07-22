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

public class WidalTestValue extends JDialog {

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
	public WidalTestValue(String p_name, final String exam_id,
			final String exam_name,final String examName_id) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(WidalTestValue.class.getResource("/icons/rotaryLogo.png")));
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
		
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(SystemColor.control);
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_3.setBounds(30, 72, 556, 255);
		contentPanel.add(panel_3);
		panel_3.setLayout(null);
		
		JPanel panel = new JPanel(new GridLayout(4,6,1,1));
		panel.setBounds(78, 48, 454, 196);
		panel_3.add(panel);
		for (int i = 0; i < 24; i++) {
			panel.add(createTextFields(i));
		}
		for (int i = 0; i <30; i++) {
			valueSTR[i]="0";
		}
		JPanel panel_1 = new JPanel();
		panel_1.setBounds(78, 13, 451, 35);
		panel_3.add(panel_1);
		panel_1.setBackground(SystemColor.activeCaption);
		panel_1.setLayout(new GridLayout(1, 0, 0, 0));
		
		JLabel lblNewLabel = new JLabel("1:20");
		lblNewLabel.setBackground(SystemColor.activeCaption);
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("1:40");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("1:80");
		lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_2.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel_2);
		
		JLabel lblNewLabel_3 = new JLabel("1:160");
		lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_3.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel_3);
		
		JLabel lblNewLabel_4 = new JLabel("1:320");
		lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_4.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel_4);
		
		JLabel lblNewLabel_5 = new JLabel("1:640");
		lblNewLabel_5.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_5.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_1.add(lblNewLabel_5);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBounds(19, 48, 61, 196);
		panel_3.add(panel_2);
		panel_2.setBackground(SystemColor.activeCaption);
		panel_2.setLayout(new GridLayout(0, 1, 0, 0));
		
		JLabel lblNewLabel_7 = new JLabel("O");
		lblNewLabel_7.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_7.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_2.add(lblNewLabel_7);
		
		JLabel lblNewLabel_8 = new JLabel("H");
		lblNewLabel_8.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_8.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_2.add(lblNewLabel_8);
		
		JLabel lblNewLabel_9 = new JLabel("A (H)");
		lblNewLabel_9.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_9.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_2.add(lblNewLabel_9);
		
		JLabel lblNewLabel_6 = new JLabel("B (H)");
		lblNewLabel_6.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel_6.setFont(new Font("Tahoma", Font.BOLD, 13));
		panel_2.add(lblNewLabel_6);
		
		JLabel lblWidal = new JLabel("WIDAL");
		lblWidal.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblWidal.setBounds(22, 25, 46, 14);
		panel_3.add(lblWidal);
		
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
					String[] data=new String[40];
					for (int i = 0; i <24; i++) {
						if(!values[i].getText().toString().equals(""))
							valueSTR[i]=values[i].getText().toString();
					}

					
					for (int i = 0; i < 30; i++) {
					
						data[i]=valueSTR[i];
					}

					TestResultDBConnection dbConnection=new TestResultDBConnection();
					if(!valueSet)
					{
						data[30]=exam_id;
						data[31]=examName_id;
						data[32]=exam_nameSTR;
						data[33]=category;
						
						try {
							dbConnection.inserData2(data);
							updateTestData(exam_id, ""+reportTypeSTR);
							dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							dbConnection.closeConnection();
						}
					}
					else {
						data[30]=ref_id;
						
						try {
							dbConnection.updateTableValue(data,ref_id);
							updateTestData(exam_id, ""+reportTypeSTR);
							dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							dbConnection.closeConnection();
						}
					}
					
					dbConnection.closeConnection();
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
	
	private JTextField createTextFields(int i) {
		values[i] = new JTextField(50);
		values[i].setText("");
		values[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		return values[i];
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
		ResultSet resultSet = dbConnection.retrieveDATA2(examCounter,examsub_name);
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
