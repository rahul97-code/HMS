package hms.test.gui;

import hms.exam.database.ExamDBConnection;
import hms.exam.database.ReferenceTableDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;
import java.awt.TextArea;
import javax.swing.JTextPane;

public class AddTestReferenceTable extends JDialog {

	private final JPanel contentPanel = new JPanel();
	String exam_counter = "0", examname_id = "0", examsub_name = "0",
			lower_limit = "0", upper_limit = "0", results_value = "0",
			comments = "0", results1 = "0", results2 = "", category = "010";
	final DefaultComboBoxModel reportTypeCBM = new DefaultComboBoxModel();
	String reportTypeSTR, ref_id = "", exam_name;
	String[] phy_paraSTR = new String[10];
	String[] chem_paraSTR = new String[10];
	String[] meh_paraSTR = new String[10];
	JLabel[] lblPhy = new JLabel[10];
	JTextField[] phyTB = new JTextField[10];

	JLabel[] lblChem = new JLabel[10];
	JTextField[] chemTB = new JTextField[10];

	JLabel[] lblMeh = new JLabel[10];
	JTextField[] mehTB = new JTextField[10];

	Vector<String> phyVector = new Vector<String>();

	Vector<String> chemVector = new Vector<String>();

	Vector<String> mehVector = new Vector<String>();
	private JComboBox selectReportTypeCB;
	private JLabel lblExamName;
	boolean valueSet;
	JTextPane commentTA;
	String commentText = "";

	/**
	 * Create the dialog.
	 */
	public AddTestReferenceTable(String p_name, final String[] examIndex,
			final String exam_id, final String exam_name,
			final String examName_id) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AddTestReferenceTable.class
						.getResource("/icons/rotaryLogo.png")));
		this.exam_name = exam_name;
		setResizable(false);
		setBounds(100, 100, 746, 635);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		try {
			getExamReferenceRange(examIndex[0]);
		} catch (Exception e) {
			// TODO: handle exception
		}

		JPanel phyPanel = new JPanel(new GridLayout(10, 2));
		phyPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		phyPanel.setBounds(10, 187, 220, 409);
		contentPanel.add(phyPanel);

		JPanel chemPanel = new JPanel(new GridLayout(10, 2));
		chemPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		chemPanel.setBounds(259, 187, 220, 409);
		contentPanel.add(chemPanel);

		JPanel mehPanel = new JPanel(new GridLayout(10, 2));
		mehPanel.setBorder(new LineBorder(new Color(0, 0, 0), 2));
		mehPanel.setBounds(507, 187, 220, 409);
		contentPanel.add(mehPanel);

		JLabel lblPhysical = new JLabel("Physical");
		lblPhysical.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPhysical.setBounds(64, 156, 88, 20);
		contentPanel.add(lblPhysical);

		JLabel lblChemical = new JLabel("Chemical");
		lblChemical.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblChemical.setBounds(332, 157, 93, 20);
		contentPanel.add(lblChemical);

		JLabel lblMexamhpf = new JLabel("M/Exam./HPF");
		lblMexamhpf.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblMexamhpf.setBounds(567, 156, 118, 20);
		contentPanel.add(lblMexamhpf);

		JLabel lblSelectReportType = new JLabel("Select Report type :");
		lblSelectReportType.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblSelectReportType.setBounds(408, 32, 128, 20);
		contentPanel.add(lblSelectReportType);
		reportTypeCBM.removeAllElements();
		reportTypeCBM.addElement("Select Report Type");
		selectReportTypeCB = new JComboBox();
		selectReportTypeCB.setFont(new Font("Tahoma", Font.PLAIN, 13));
		selectReportTypeCB.setBounds(546, 28, 184, 28);
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
		lblMexamhpf.setBounds(567, 156, 118, 20);
		contentPanel.add(lblMexamhpf);

		lblExamName = new JLabel(exam_name);
		lblExamName.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblExamName.setBounds(10, 19, 372, 45);
		contentPanel.add(lblExamName);

		JButton btnSave = new JButton("Save");
		btnSave.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				if (reportTypeSTR.equals("Select Report Type")) {
					JOptionPane.showMessageDialog(null,
							"Please Select The Report Type", "Input Error",
							JOptionPane.ERROR_MESSAGE);
				} else {
					String[] data = new String[40];
					for (int i = 0; i < phyVector.size(); i++) {
						if (!phyTB[i].getText().toString().equals(""))
							phy_paraSTR[i] = phyVector.get(i) + "  -  "
									+ phyTB[i].getText().toString();
					}

					for (int i = 0; i < chemVector.size(); i++) {
						if (!chemTB[i].getText().toString().equals(""))
							chem_paraSTR[i] = chemVector.get(i) + "  -  "
									+ chemTB[i].getText().toString();
					}

					for (int i = 0; i < mehVector.size(); i++) {
						if (!mehTB[i].getText().toString().equals(""))
							meh_paraSTR[i] = mehVector.get(i) + "  -  "
									+ mehTB[i].getText().toString();
					}

					for (int i = 0; i < 10; i++) {

						data[i] = phy_paraSTR[i];
					}

					for (int i = 0; i < 10; i++) {

						data[10 + i] = chem_paraSTR[i];

					}

					for (int i = 0; i < 10; i++) {

						data[20 + i] = meh_paraSTR[i];

					}
					TestResultDBConnection dbConnection = new TestResultDBConnection();
					if (!valueSet) {
						data[30] = exam_id;
						data[31] = examName_id;
						data[32] = examsub_name;
						data[33] = category;
						data[34] = commentTA.getText().toString() + "";

						try {
							dbConnection.inserData2(data);
							updateTestData(exam_id, "" + reportTypeSTR);
							dispose();
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							dbConnection.closeConnection();
						}
					} else {
//						data[30] = ref_id;
						data[30] = commentTA.getText().toString() + "";
//						System.out.println("udate"+data[31]);
						try {
							dbConnection.updateTableValue(data,ref_id);
							updateTestData(exam_id, "" + reportTypeSTR);
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
		btnSave.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnSave.setBounds(405, 94, 151, 51);
		contentPanel.add(btnSave);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		btnCancel.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnCancel.setBounds(567, 94, 150, 51);
		contentPanel.add(btnCancel);

	
		
		for (int i = 0; i < 10; i++) {
			phy_paraSTR[i] = "0";
			chem_paraSTR[i] = "0";
			meh_paraSTR[i] = "0";
		}
		for (int i = 0; i < phyVector.size(); i++) {
			phyPanel.add(createPhyPanel(i));
		}

		for (int i = 0; i < chemVector.size(); i++) {
			chemPanel.add(createChemPanel(i));
		}

		for (int i = 0; i < mehVector.size(); i++) {
			mehPanel.add(createMehPanel(i));
		}
		getReportTypes();
		getData(exam_id);
		getReportType(exam_id);
		commentTA = new JTextPane();
		commentTA.setBounds(172, 94, 210, 51);
		if (commentText.equals("")) {
			commentTA.setVisible(false);
			
		} else {
			commentTA.setVisible(true);
			commentTA.setText(commentText+"");
		}

		contentPanel.add(commentTA);

		JButton btnComment = new JButton("Add Comment");
		btnComment.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnComment.setBounds(24, 102, 128, 34);
		btnComment.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				commentTA.setVisible(true);
			}
		});
		contentPanel.add(btnComment);
	}

	private JPanel createPhyPanel(int i) {
		JPanel p = new JPanel(new MigLayout("", "[grow][]", "[][]"));
		lblPhy[i] = new JLabel();
		lblPhy[i].setText("" + phyVector.get(i) + ":");
		lblPhy[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(lblPhy[i], "cell 0 0,alignx trailing");
		phyTB[i] = new JTextField(12);
		phyTB[i].setText("");
		phyTB[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(phyTB[i], "cell 1 0,growx");
		p.revalidate();
		return p;
	}

	private JPanel createChemPanel(int i) {
		JPanel p = new JPanel(new MigLayout("", "[grow][]", "[][]"));
		lblChem[i] = new JLabel();
		lblChem[i].setText("" + chemVector.get(i) + ":");
		lblChem[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(lblChem[i], "cell 0 0,alignx trailing");
		chemTB[i] = new JTextField(12);
		chemTB[i].setText("");
		chemTB[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(chemTB[i], "cell 1 0,growx");
		p.revalidate();
		return p;
	}

	private JPanel createMehPanel(int i) {
		JPanel p = new JPanel(new MigLayout("", "[grow][]", "[][]"));
		lblMeh[i] = new JLabel();
		lblMeh[i].setText("" + mehVector.get(i) + ":");
		lblMeh[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(lblMeh[i], "cell 0 0,alignx trailing");
		mehTB[i] = new JTextField(12);
		mehTB[i].setText("");
		mehTB[i].setFont(new Font("Tahoma", Font.BOLD, 12));
		p.add(mehTB[i], "cell 1 0,growx");
		p.revalidate();
		return p;
	}

	public void getExamReferenceRange(String index) {

		ReferenceTableDBConnection dbConnection = new ReferenceTableDBConnection();
		ResultSet resultSet = dbConnection.retrieveReferenceTableWithId(index);
		try {
			while (resultSet.next()) {

				for (int i = 1; i < 11; i++) {
					String str = resultSet.getObject(i).toString();
					if (!str.equals("0"))
						phyVector.add(str);

				}

				for (int i = 11; i < 21; i++) {
					String str = resultSet.getObject(i).toString();
					if (!str.equals("0"))
						chemVector.add(str);
				}

				for (int i = 21; i < 31; i++) {
					String str = resultSet.getObject(i).toString();
					if (!str.equals("0"))
						mehVector.add(str);
				}
				examsub_name = resultSet.getObject(31).toString();

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
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

	public void getData(String examCounter) {
		System.out.println(examCounter + "   " + exam_name);
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveDATA2(examCounter,
				examsub_name);
		try {
			while (resultSet.next()) {
				ref_id = resultSet.getObject(1).toString();
				for (int i = 0; i < phyVector.size(); i++) {
					String str = resultSet.getObject(i + 2).toString();

					if (!str.equals("0"))
						phyTB[i].setText("" + str.split("  -  ")[1]);

				}

				for (int i = 0; i < chemVector.size(); i++) {
					String str = resultSet.getObject(i + 12).toString();
					if (!str.equals("0"))
						chemTB[i].setText("" + str.split("  -  ")[1]);

				}

				for (int i = 0; i < mehVector.size(); i++) {
					String str = resultSet.getObject(i + 22).toString();
					if (!str.equals("0"))
						mehTB[i].setText("" + str.split("  -  ")[1]);

				}
				commentText="";
				commentText = resultSet.getObject(32).toString()+"";
				
				valueSet = true;
			}
		
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}

	public void getReportType(String exam_id) {
		ExamDBConnection db = new ExamDBConnection();
		ResultSet resultSet = db.retrieveExamStatus(exam_id);
		Object type = "";
		try {
			while (resultSet.next()) {

				type = resultSet.getObject(1);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
		if (type != null) {
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

	public JLabel getLblExamName() {
		return lblExamName;
	}
}
