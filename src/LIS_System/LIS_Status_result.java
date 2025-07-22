package LIS_System;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import LIS_UI.LIS_System;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JButton;

public class LIS_Status_result extends JDialog{

	private JTextField workorderTF;
	private JTextField PNameTF;
	private JTextField PInfoTF;
	private JTextField PaqtientIDTF;
	String workorderNO="",url;
	static String[][] result;
	JLabel testcodelbl;
	JLabel entrydatelbl;
	JLabel legerTransnolbl;
	JLabel barcodelbl;	
	JLabel Deptlbl;	
	JLabel DocUplodedlbl;	
	JLabel ItemNamelbl;	
	JLabel Test_IDlbl;
	JLabel Approvedlbl;	
	JLabel BookingStatuslbl;	
	JLabel LISDetailslbl;
	JComboBox testCB ;
	JLabel webpassTF;
	final DefaultComboBoxModel tests = new DefaultComboBoxModel();
	private JLabel smapleStatusTF;
	private JLabel docStatusTF;
	private JLabel logisticstatusTF;
	private JLabel lblNewLabel;
	private JButton btnUpdate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LIS_Status_result window = new LIS_Status_result("RGH121556",null);
					window.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 * @param lis_System 
	 */
	public LIS_Status_result(final String workorderNO, final LIS_System lis_System) {
		this.workorderNO=workorderNO;
		
		setResizable(false);
		setTitle("LIS Result Status");
		setBounds(100, 100, 873, 473);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);

		testcodelbl = new JLabel("");
		testcodelbl.setBounds(38, 34, 221, 37);
		testcodelbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		testcodelbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Testcode", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(testcodelbl);

		entrydatelbl = new JLabel("");
		entrydatelbl.setBounds(38, 76, 221, 37);
		entrydatelbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		entrydatelbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "EntryDate", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(entrydatelbl);

		legerTransnolbl = new JLabel("");
		legerTransnolbl.setBounds(271, 120, 234, 37);
		legerTransnolbl.setFont(new Font("Diwalog", Font.PLAIN, 11));
		legerTransnolbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Ledger Trans.No", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(legerTransnolbl);

		barcodelbl = new JLabel("");
		barcodelbl.setBounds(38, 165, 221, 37);
		barcodelbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		barcodelbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Barcode No.", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(barcodelbl);

		Deptlbl = new JLabel("");
		Deptlbl.setBounds(271, 34, 234, 37);
		Deptlbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		Deptlbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Dept", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(Deptlbl);

		DocUplodedlbl = new JLabel("");
		DocUplodedlbl.setBounds(38, 206, 221, 37);
		DocUplodedlbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		DocUplodedlbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "DocUploded", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(DocUplodedlbl);

		ItemNamelbl = new JLabel("");
		ItemNamelbl.setBounds(271, 76, 234, 37);
		ItemNamelbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		ItemNamelbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "ItemName", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(ItemNamelbl);

		Test_IDlbl = new JLabel("");
		Test_IDlbl.setBounds(38, 120, 221, 37);
		Test_IDlbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		Test_IDlbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Test ID", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(Test_IDlbl);

		Approvedlbl = new JLabel("");
		Approvedlbl.setBounds(271, 165, 234, 37);
		Approvedlbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		Approvedlbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Approved", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(Approvedlbl);

		BookingStatuslbl = new JLabel("");
		BookingStatuslbl.setBounds(271, 206, 234, 37);
		BookingStatuslbl.setFont(new Font("Dialog", Font.PLAIN, 11));
		BookingStatuslbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Booking Status", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(BookingStatuslbl);

		testCB = new JComboBox();
		testCB.setBounds(601, 46, 221, 24);
		testCB.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int index=testCB.getSelectedIndex();
				if(index>=0)
				{getAllDetails(index);
				}else
				{
					testcodelbl.setText("");
					entrydatelbl.setText("");
					legerTransnolbl.setText("");
					barcodelbl.setText("");	
					Deptlbl.setText("");	
					DocUplodedlbl.setText("");	
					ItemNamelbl.setText("");	
					Test_IDlbl.setText("");
					Approvedlbl.setText("");	
					BookingStatuslbl.setText("");	
				}

				System.out.print(index+"ssssssss");
			}


		});
		getContentPane().add(testCB);

		JLabel lblWorkorderid = new JLabel("WorkOrderID :-");
		lblWorkorderid.setBounds(601, 113, 108, 15);
		getContentPane().add(lblWorkorderid);

		workorderTF = new JTextField();
		workorderTF.setBounds(601, 140, 134, 19);
		workorderTF.setEditable(false);
		getContentPane().add(workorderTF);
		workorderTF.setColumns(10);

		JLabel lblPatientname = new JLabel("patientName :-");
		lblPatientname.setBounds(601, 181, 108, 15);
		getContentPane().add(lblPatientname);

		PNameTF = new JTextField();
		PNameTF.setBounds(601, 209, 134, 19);
		PNameTF.setEditable(false);
		PNameTF.setColumns(10);
		getContentPane().add(PNameTF);

		PInfoTF = new JTextField();
		PInfoTF.setBounds(601, 279, 134, 19);
		PInfoTF.setEditable(false);
		PInfoTF.setColumns(10);
		getContentPane().add(PInfoTF);

		JLabel lblPatientInfo = new JLabel("patient Info :-");
		lblPatientInfo.setBounds(601, 252, 113, 15);
		getContentPane().add(lblPatientInfo);

		JLabel lblPatientId = new JLabel("Patient ID :-");
		lblPatientId.setBounds(601, 321, 91, 15);
		getContentPane().add(lblPatientId);

		PaqtientIDTF = new JTextField();
		PaqtientIDTF.setBounds(601, 348, 134, 19);
		PaqtientIDTF.setEditable(false);
		PaqtientIDTF.setColumns(10);
		getContentPane().add(PaqtientIDTF);

		JLabel Testlbl = new JLabel("");
		Testlbl.setBounds(579, 13, 255, 67);
		Testlbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Tests", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		getContentPane().add(Testlbl);

		JLabel Userdetailslbl = new JLabel("");
		Userdetailslbl.setBounds(579, 82, 255, 299);
		Userdetailslbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "User Details", TitledBorder.LEADING, TitledBorder.TOP, null, new Color(51, 51, 51)));
		getContentPane().add(Userdetailslbl);

		smapleStatusTF = new JLabel((String) null);
		smapleStatusTF.setFont(new Font("Dialog", Font.PLAIN, 11));
		smapleStatusTF.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Sample Status", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		smapleStatusTF.setBounds(38, 252, 221, 37);
		getContentPane().add(smapleStatusTF);

		docStatusTF = new JLabel((String) null);
		docStatusTF.setFont(new Font("Dialog", Font.PLAIN, 11));
		docStatusTF.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Document Status", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		docStatusTF.setBounds(271, 255, 234, 37);
		getContentPane().add(docStatusTF);

		logisticstatusTF = new JLabel((String) null);
		logisticstatusTF.setFont(new Font("Dialog", Font.PLAIN, 11));
		logisticstatusTF.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Logistic Status", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		logisticstatusTF.setBounds(38, 299, 221, 37);
		getContentPane().add(logisticstatusTF);

		webpassTF = new JLabel((String) null);
		webpassTF.setFont(new Font("Dialog", Font.PLAIN, 11));
		webpassTF.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "Web Password", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		webpassTF.setBounds(271, 299, 234, 37);
		getContentPane().add(webpassTF);

		LISDetailslbl = new JLabel("");
		LISDetailslbl.setBounds(22, 12, 545, 369);
		LISDetailslbl.setFont(new Font("Dialog", Font.PLAIN, 10));
		LISDetailslbl.setBorder(new TitledBorder(new LineBorder(new Color(184, 207, 229)), "LIS Details", TitledBorder.LEADING, TitledBorder.TOP, null, SystemColor.activeCaptionText));
		getContentPane().add(LISDetailslbl);

		LIS_StatusChecking obj=new LIS_StatusChecking();
		try {
			result=obj.CheckStatus(workorderNO);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for(int i=0;i<result.length;i++)
		{
			tests.addElement(result[i][7]+"");
		}
		if(tests.getSize()>0)
		{
			testCB.setModel(tests);
		}
		if(result.length>0)
		{
			workorderTF.setText(workorderNO);
			PaqtientIDTF.setText(result[0][14]);
			PInfoTF.setText(result[0][15]);
			PNameTF.setText(result[0][16]);
			getAllDetails(0); 
		}else {
			JOptionPane
			.showMessageDialog(
					null,
					"Record not found!",
					"Input Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}


		JButton btnNewButton = new JButton("CANCEL");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(709, 388, 125, 36);
		getContentPane().add(btnNewButton);

		lblNewLabel = new JLabel("Developed by Arun & Rahul ...");
		lblNewLabel.setFont(new Font("Dialog", Font.ITALIC, 9));
		lblNewLabel.setBounds(22, 409, 247, 15);
		getContentPane().add(lblNewLabel);

		btnUpdate = new JButton("UPDATE");
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				updateStatus(workorderNO);
				JOptionPane
				.showMessageDialog(
						null,
						"Data updated successfully.",
						"Success",
						JOptionPane.INFORMATION_MESSAGE);
				dispose();
				lis_System.populateTable(DateFormatChange.StringToMysqlDate(new Date()),
						DateFormatChange.StringToMysqlDate(new Date()),"");

			}
		});
		btnUpdate.setBounds(568, 388, 125, 36);
		getContentPane().add(btnUpdate);

		JButton viewReportBT = new JButton("View Report");
		viewReportBT.addActionListener(new ActionListener() {


			public void actionPerformed(ActionEvent e) {
				ExamDBConnection db = new ExamDBConnection();
				url=db.retrieveviewreport(workorderTF.getText().toString(),Test_IDlbl.getText().toString());
				db.closeConnection();
				if(url==null || url=="") {
					JOptionPane.showMessageDialog(null,
							"This patient have not any report", "Error",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				else {
					Desktop desk=Desktop.getDesktop();
					try {
						desk.browse(new URI(url));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (URISyntaxException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				} 
			}

		});
		viewReportBT.setBounds(431, 388, 125, 36);
		getContentPane().add(viewReportBT);


	}
	private void updateStatus(String workorderNo) {
		// TODO Auto-generated method stub
		String data[][] = null;
		LIS_StatusChecking obj=new LIS_StatusChecking();
		try {
			data=obj.CheckStatus(workorderNo);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LISBDConnection db=new LISBDConnection();
		for(int i=0;i<data.length;i++)
		{
			db.updateData(workorderNo,data,i);
		}
		db.closeConnection();

	}
	public void getAllDetails(int i) {
		// TODO Auto-generated method stub

		testcodelbl.setText(result[i][1]);
		entrydatelbl.setText(result[i][3]);
		barcodelbl.setText(result[i][2]);	
		Deptlbl.setText(result[i][6]);	
		DocUplodedlbl.setText(result[i][4]);	
		ItemNamelbl.setText(result[i][7]);	
		Test_IDlbl.setText(result[i][8]);
		Approvedlbl.setText(result[i][9]);	
		BookingStatuslbl.setText(result[i][12]);
		webpassTF.setText(result[i][17]);
		docStatusTF.setText(result[i][18]);
		smapleStatusTF.setText(result[i][19]);
		logisticstatusTF.setText(result[i][20]);

	}
}