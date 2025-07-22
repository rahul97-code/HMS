package hms.test.gui;

import hms.exam.database.ExamDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.jgoodies.forms.factories.DefaultComponentFactory;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;

public class AddTestReferences extends JDialog {

	private JPanel contentPane;
	private List<JPanel> panels = new ArrayList<JPanel>(); // Your List
	private JPanel orderList;
	TestReferencePanel[] p1 = new TestReferencePanel[500];
	int hieght = 120;
	private JLabel lblExamName;
	String exam_counter = "0", examname_id = "0", examsub_name = "0",
			lower_limit = "0", upper_limit = "0", results_value = "0",
			comments = "0", results1 = "0", results2 = "", category = "100";
	final DefaultComboBoxModel reportTypeCBM = new DefaultComboBoxModel();
	String reportTypeSTR;
	private JComboBox selectReportTypeCB;
	private JTextArea textAreaComment;

	/**
	 * Create the frame.
	 */
	public AddTestReferences(String p_name,final String[] examIndex, final String exam_id,
			String exam_name, final String examName_id) {
		setTitle("Add References ");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AddTestReferences.class.getResource("/icons/rotaryLogo.png")));
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1085, 610);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane
				.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane
				.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		scrollPane.setBounds(10, 72, 1049, 435);
		contentPane.add(scrollPane);
		
		scrollPane.getVerticalScrollBar().setUnitIncrement(20);
		scrollPane.getHorizontalScrollBar().setUnitIncrement(20);

		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(850, hieght));
		scrollPane.setViewportView(panel);
		panel.setLayout(new GridLayout(0, 1, 0, 0));

		lblExamName = new JLabel(exam_name);
		lblExamName.setFont(new Font("Tahoma", Font.PLAIN, 18));
		lblExamName.setBounds(10, 11, 338, 49);
		contentPane.add(lblExamName);
		reportTypeCBM.removeAllElements();
		reportTypeCBM.addElement("Select Report Type");
		selectReportTypeCB = new JComboBox(reportTypeCBM);
		selectReportTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
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
		selectReportTypeCB.setBounds(804, 22, 255, 28);
		contentPane.add(selectReportTypeCB);

		JLabel lblSelectReportType = DefaultComponentFactory.getInstance()
				.createLabel("Select Report Type :");
		lblSelectReportType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSelectReportType.setBounds(665, 22, 130, 23);
		contentPane.add(lblSelectReportType);
		
		JLabel lblPatientnamelb = new JLabel("Patient Name : "+p_name);
		lblPatientnamelb.setHorizontalAlignment(SwingConstants.CENTER);
		lblPatientnamelb.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblPatientnamelb.setBounds(398, 25, 255, 23);
		contentPane.add(lblPatientnamelb);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(20, 517, 284, 49);
		contentPane.add(scrollPane_1);
		
		textAreaComment = new JTextArea();
		textAreaComment.setRows(5);
		scrollPane_1.setViewportView(textAreaComment);
		
		JButton btnCancel_1 = new JButton("Cancel");
		btnCancel_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancel_1.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnCancel_1.setBounds(950, 526, 125, 40);
		contentPane.add(btnCancel_1);
		
		JButton btnCancel_2 = new JButton("Save Results");
		btnCancel_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (reportTypeSTR.equals("Select Report Type")) {
					JOptionPane.showMessageDialog(null,
							"Please Select The Report Type", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				TestResultDBConnection dbConnection = new TestResultDBConnection();

				for (int i = 0; i < examIndex.length; i++) {
					String[] data = new String[40];
					if (!p1[i].enterValueTB.getText().toString().equals("")&&!p1[i].valueSet) {

						data[0] = exam_id;
						data[1] = examName_id;
						data[2] = p1[i].lblTestName.getText().toString();
						data[3] = p1[i].lowerLimitSTR;
						data[4] = p1[i].upperLimitSTR;
						data[5] =p1[i].enterValueTB.getText().toString();
						data[6] = p1[i].commentSTR;
						data[7] = p1[i].lblResults.getText().toString();
						data[8] = p1[i].unitSTR;
						data[9] = category;
						data[10]=p1[i].comboBox.getSelectedItem().toString();
						if(p1[i].textArea.getText().toString().equals("N/A") ||p1[i].textArea.getText().toString().equals("")||p1[i].textArea.getText().toString().equals(".....")){
							data[11]=".....";
						}else{
							data[11]=p1[i].textArea.getText().toString();
						}
						
//                        System.out.println("comment"+p1[i].textArea.getText().toString());
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
					else if(p1[i].valueSet)
					{
						String valuechk="";
						if(p1[i].textArea.getText().toString().equals("N/A") ||p1[i].textArea.getText().toString().equals("")||p1[i].textArea.getText().toString().equals(".....")){
							valuechk=".....";
						}else{
							valuechk=p1[i].textArea.getText().toString();
						}
						
						try {
							dbConnection.updateTestValue(p1[i].ref_id,p1[i].enterValueTB.getText().toString(), p1[i].lblResults.getText().toString(),p1[i].comboBox.getSelectedItem().toString(),valuechk+"");
//							dbConnection.updateTestValue(p1[i].ref_id,p1[i].enterValueTB.getText().toString(), p1[i].lblResults.getText().toString());
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}
				updateTestData(exam_id + "", "" + reportTypeSTR);
				updateComment(exam_id);
				dbConnection.closeConnection();
				dispose();
			}
		});
		btnCancel_2.setFont(new Font("Dialog", Font.PLAIN, 13));
		btnCancel_2.setBounds(820, 526, 125, 40);
		contentPane.add(btnCancel_2);

		for (int i = 0; i < examIndex.length; i++) {
			TestReferencePanel p = new TestReferencePanel(examIndex[i],exam_id);
			p1[i] = p;
			panels.add(p);
		}
		for (int i = 0; i < panels.size(); i++) {
			panel.add(panels.get(i));
			panel.setPreferredSize(new Dimension(850, hieght + (i * 80)));
			panel.revalidate();
			System.out.print("" + p1[i].lowerLimitSTR);
			panel.revalidate();
			panel.repaint();
		}
		repaint();
		getReportTypes();
		getReportType(exam_id);
		GetExamComment(exam_id);
	}

	/**
	 * 
	 */
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
	public void updateComment(String exam_id) {
		ExamDBConnection db = new ExamDBConnection();
		try {
			db.updateExamComment(exam_id,textAreaComment.getText()+"");
		} catch (Exception e) {
			// TODO Auto-generated catch block  
			e.printStackTrace(); 
		}
		db.closeConnection();
	}
	public void GetExamComment(String exam_id) {
		textAreaComment.setText("");
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs=db.retrieveExamComment(exam_id);
		Object comment=null;
		if (rs!=null) {
			try {
				while (rs.next()) {
					if(rs.getObject(1)!=null || !rs.getObject(1).equals("") ){
						textAreaComment.setText(rs.getObject(1)+"");
					}	
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}	
		db.closeConnection();
	}
}
