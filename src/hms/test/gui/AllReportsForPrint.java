package hms.test.gui;

import hms.exam.database.ExamDBConnection;
import hms.patient.slippdf.ExamReportPDF;
import hms.test.database.TestRecordDBConnection;
import hms.test.database.TestResultDBConnection;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import com.itextpdf.text.DocumentException;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class AllReportsForPrint extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	Vector<String> reportTypes = new Vector<String>();
	Vector<String> exam_ID = new Vector<String>();

	Object Rows_Object_Array[][];
	private JButton btnNewButton;
	private JButton btnNewButton_2;
	private JButton btnNewButton_1;
	int selectedRowIndex;
	private JTextField labSrNoTB;
	/**
	 * Create the dialog.
	 */
	public AllReportsForPrint(final Test test,final String patientID, final String roomNo) {
		setTitle("Reports");
		setIconImage(Toolkit.getDefaultToolkit().getImage(
				AllReportsForPrint.class.getResource("/icons/rotaryLogo.png")));
		setBounds(100, 100, 589, 514);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 135, 550, 330);
		contentPanel.add(scrollPane);
		table = new JTable();
		scrollPane.setViewportView(table);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setFont(new Font("Tahoma", Font.PLAIN, 12));
		table.getTableHeader().setReorderingAllowed(false);
		table.setModel(new DefaultTableModel(new Object[][] {
				{ null, "Exam Name", null }, { null, null, null }, },
				new String[] { "Exam Number", "New column", "Report Type" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(107);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(150);
		table.getColumnModel().getColumn(1).setMinWidth(120);
		table.getColumnModel().getColumn(2).setPreferredWidth(120);
		table.getColumnModel().getColumn(2).setMinWidth(120);
		table.setFont(new Font("Tahoma", Font.PLAIN, 13));
		table.getSelectionModel().addListSelectionListener(
				new ListSelectionListener() {
					@Override
					public void valueChanged(ListSelectionEvent e) {
						// TODO Auto-generated method stub
						selectedRowIndex = table.getSelectedRow();
						selectedRowIndex = table
								.convertRowIndexToModel(selectedRowIndex);
						int selectedColumnIndex = table
								.getSelectedColumn();
						btnNewButton_2.setEnabled(true);
					}
				});

		btnNewButton = new JButton("Generate & Save");
		btnNewButton.setEnabled(false);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(labSrNoTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter the Lab Sr. No", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				int counter=record_counter(patientID);
				try {
					new ExamReportPDF(labSrNoTB.getText().toString(),counter+"","", Rows_Object_Array, reportTypes,
							roomNo);
				} catch (DocumentException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				ExamDBConnection examDBConnection=new ExamDBConnection();
				for (int i = 0; i < exam_ID.size(); i++) {
					try {
						examDBConnection.updateExamDataStatus(""+exam_ID.get(i), ""+counter);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				examDBConnection.closeConnection();
				try {
					loadDataToTable(patientID, roomNo);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton.setBounds(377, 11, 183, 64);
		contentPanel.add(btnNewButton);
		
		btnNewButton_1 = new JButton("Preview");
		btnNewButton_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(labSrNoTB.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please enter the Lab Sr. No", "Input Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}
				try {
					new ExamReportPDF(labSrNoTB.getText().toString(),"Preview","", Rows_Object_Array, reportTypes,
							roomNo);
				} catch (DocumentException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_1.setBounds(10, 11, 140, 64);
		contentPanel.add(btnNewButton_1);
		
		btnNewButton_2 = new JButton("Recorrect");
		btnNewButton_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				Object selectedObject1 = table.getModel()
						.getValueAt(selectedRowIndex, 0);
				System.out.print("" + selectedObject1);
				String id = selectedObject1.toString();
				updateTestData(id);
				try {
					loadDataToTable(patientID, roomNo);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				try {
					test.loadDataToTable(patientID);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnNewButton_2.setEnabled(false);
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 18));
		btnNewButton_2.setBounds(160, 11, 140, 64);
		contentPanel.add(btnNewButton_2);
		
		JLabel lblLabSrNo = new JLabel("Lab Sr.  No. :");
		lblLabSrNo.setFont(new Font("Tahoma", Font.BOLD, 17));
		lblLabSrNo.setBounds(38, 86, 165, 28);
		contentPanel.add(lblLabSrNo);
		
		labSrNoTB = new JTextField();
		labSrNoTB.setFont(new Font("Tahoma", Font.BOLD, 16));
		labSrNoTB.setBounds(210, 86, 284, 28);
		contentPanel.add(labSrNoTB);
		labSrNoTB.setColumns(10);
		getReportTypes();
		try {
			loadDataToTable(patientID, roomNo);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void loadDataToTable(String pid, String exam_room)
			throws SQLException {
		exam_ID.clear();
		btnNewButton.setEnabled(false);
		ExamDBConnection db = new ExamDBConnection();
		ResultSet rs = db
				.retrieveExamDataPatientIDAndCompletion(pid, exam_room);
		// System.out.println("Table: " + rs.getMetaData().getTableName(1));
		int NumberOfColumns = 0, NumberOfRows = 0;
		NumberOfColumns = rs.getMetaData().getColumnCount();
		while (rs.next()) {
			NumberOfRows++;
		}
		System.out.println("rows : " + NumberOfRows);
		System.out.println("columns : " + NumberOfColumns);
		rs = db.retrieveExamDataPatientIDAndCompletion(pid, exam_room);

		Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];
		int R = 0;
		while (rs.next()) {
			for (int C = 1; C <= NumberOfColumns; C++) {
				Rows_Object_Array[R][C - 1] = rs.getObject(C);
				btnNewButton.setEnabled(true);
			}
			exam_ID.add(rs.getObject(1).toString());
			R++;
		}
		table.setModel(new DefaultTableModel(Rows_Object_Array, new String[] {
				"Exam Number", "Exam Name", "Report Type" }));
		table.getColumnModel().getColumn(0).setPreferredWidth(107);
		table.getColumnModel().getColumn(0).setMinWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(250);
		table.getColumnModel().getColumn(1).setMinWidth(220);
		table.getColumnModel().getColumn(2).setPreferredWidth(220);
		table.getColumnModel().getColumn(2).setMinWidth(220);
		btnNewButton_2.setEnabled(false);
	}

	public void getReportTypes() {
		reportTypes.clear();
		TestResultDBConnection dbConnection = new TestResultDBConnection();
		ResultSet resultSet = dbConnection.retrieveAllReportType();
		try {
			while (resultSet.next()) {

				reportTypes.add(resultSet.getObject(2).toString());

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dbConnection.closeConnection();
	}
	public JButton getBtnNewButton() {
		return btnNewButton;
	}
	public int record_counter(String patient_id)
	{
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		String[] data=new String[4];
		data[0]=patient_id;
		data[1]="";
		data[2]=timeStamp;
		int value=0;
		TestRecordDBConnection testRecordDBConnection=new TestRecordDBConnection();
		
		try {
			value=testRecordDBConnection.inserData(data);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			testRecordDBConnection.closeConnection();
		}
		
		testRecordDBConnection.closeConnection();
		return value;
	}
	public void updateTestData(String exam_id) {
		String str = "0";
		ExamDBConnection db = new ExamDBConnection();
		try {
			db.updateExamData(exam_id, str, "","No");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		db.closeConnection();
	}
	public JTextField getLabSrNoTB() {
		return labSrNoTB;
	}
}
