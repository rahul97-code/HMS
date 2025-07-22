package hms.doctor.gui;

import hms.doctor.database.DoctorDBConnection;
import hms.main.DateFormatChange;
import hms.main.MainLogin;
import hms.opd.database.OPDDBConnection;
import hms.opd.gui.OPDBrowser;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.JRadioButton;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class PatientOPDHistory extends JPanel {
	private JTable opdHistoryTable;
	private JLabel lblDoctorName;
	private JTextArea symptomTA;
	private JLabel lblTokenNo;
	private JTextArea prescriptionTA;
	JRadioButton rdbtnNo;
	JRadioButton rdbtnYes;
	private ButtonGroup group;
	private ButtonGroup groupUSG;
	JRadioButton rdbtnUSG;
	JRadioButton rdbtnnoUSG;
	private JLabel lblfreeUSG;
	private int value;
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public PatientOPDHistory() {
		setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 41, 316, 367);
		add(scrollPane);

		opdHistoryTable = new JTable();
		opdHistoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		opdHistoryTable.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		opdHistoryTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		opdHistoryTable.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(opdHistoryTable);
		opdHistoryTable.setModel(new DefaultTableModel(
				new Object[][] {
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
					{null, null},
				},
				new String[] {
						"OPD ID", "OPD Date"
				}
				));
		opdHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
		opdHistoryTable.getColumnModel().getColumn(0).setMinWidth(149);
		opdHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(160);
		opdHistoryTable.getColumnModel().getColumn(1).setMinWidth(160);
		opdHistoryTable .getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				try {
					int selectedRowIndex = opdHistoryTable.getSelectedRow();
					selectedRowIndex=opdHistoryTable.convertRowIndexToModel(selectedRowIndex);
					Object selectedObject = opdHistoryTable.getModel().getValueAt(selectedRowIndex, 0);
					Object selectedObjectDate = opdHistoryTable.getModel().getValueAt(selectedRowIndex, 1);
					getOPDData(""+selectedObject);
					if(selectedObjectDate.toString().equals(DateFormatChange.StringToMysqlDate(new Date())))
					{
						rdbtnYes.setEnabled(true);
						rdbtnNo.setEnabled(true);
						rdbtnUSG.setEnabled(true);
						rdbtnUSG.setEnabled(true);
					}
					else
					{
						rdbtnYes.setEnabled(false);
						rdbtnNo.setEnabled(false);
						rdbtnnoUSG.setEnabled(false);
						rdbtnnoUSG.setEnabled(false);
					}
				} catch (Exception e2) {
					// TODO: handle exception
				}
			}
		});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Note & Symptom :", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel.setBounds(354, 150, 249, 119);
		add(panel);
		panel.setLayout(null);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(6, 16, 237, 99);
		panel.add(scrollPane_1);

		symptomTA = new JTextArea();
		symptomTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		symptomTA.setRows(10);
		scrollPane_1.setViewportView(symptomTA);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(null, "Prescription :", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel_1.setBounds(354, 281, 249, 132);
		add(panel_1);
		panel_1.setLayout(null);

		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(6, 16, 237, 109);
		panel_1.add(scrollPane_2);

		prescriptionTA = new JTextArea();
		prescriptionTA.setFont(new Font("Tahoma", Font.PLAIN, 12));
		prescriptionTA.setRows(10);
		scrollPane_2.setViewportView(prescriptionTA);

		lblDoctorName = new JLabel("Doctor Name :");
		lblDoctorName.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblDoctorName.setBounds(369, 30, 256, 20);
		add(lblDoctorName);

		lblTokenNo = new JLabel("Token No. :");
		lblTokenNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTokenNo.setBounds(369, 91, 224, 20);
		add(lblTokenNo);
		group = new ButtonGroup();

		JLabel lblAffordable = new JLabel("Karuna :-");
		lblAffordable.setVisible(false);
		lblAffordable.setFont(new Font("Dialog", Font.PLAIN, 12));
		lblAffordable.setBounds(369, 123, 77, 15);
		add(lblAffordable);

		rdbtnYes = new JRadioButton("Yes");
		rdbtnYes.setVisible(false);
		rdbtnYes.setBounds(469, 123, 57, 15);
		rdbtnYes.setFont(new Font("Dialog", Font.PLAIN, 12));
		group.add(rdbtnYes);
		add(rdbtnYes);

		rdbtnNo = new JRadioButton("No");
		rdbtnNo.setVisible(false);
		rdbtnNo.setSelected(true);
		rdbtnNo.setBounds(546, 123,  57, 15);
		rdbtnNo.setFont(new Font("Dialog", Font.PLAIN, 12));
		group.add(rdbtnNo);
		add(rdbtnNo);

		groupUSG = new ButtonGroup();
		lblfreeUSG = new JLabel("Free USG : ");
		lblfreeUSG.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblfreeUSG.setBounds(369, 62, 70, 15);

		add(lblfreeUSG);

		rdbtnUSG = new JRadioButton("Yes");
		rdbtnUSG.setBounds(469, 58, 57, 23);
		rdbtnUSG.setFont(new Font("Dialog", Font.PLAIN, 12));
		groupUSG.add(rdbtnUSG);
		add(rdbtnUSG);

		rdbtnnoUSG = new JRadioButton("No");
		rdbtnnoUSG.setSelected(true);
		rdbtnnoUSG.setBounds(541, 58, 47, 23);
		rdbtnnoUSG.setFont(new Font("Dialog", Font.PLAIN, 12));
		groupUSG.add(rdbtnnoUSG);
		add(rdbtnnoUSG);

		textField = new JTextField();
		textField.setBounds(606, 60, 45, 21);
		add(textField);
		textField.setColumns(10);

		if(docAccess() && getfreeUSG()) {
			rdbtnUSG.setEnabled(true);
			rdbtnnoUSG.setEnabled(true);
		}
		else {
			rdbtnUSG.setEnabled(false);
			rdbtnnoUSG.setEnabled(false);
		}
		if(DocIsKaruna()){
			rdbtnNo.setVisible(true);
			rdbtnYes.setVisible(true);
			lblAffordable.setVisible(true);
		}else{
			rdbtnNo.setVisible(false);
			rdbtnYes.setVisible(false);
			lblAffordable.setVisible(false);
		}
	}
	private boolean DocIsKaruna() {
		// TODO Auto-generated method stub
		DoctorDBConnection dbConnection = new DoctorDBConnection();
		ResultSet resultSet = dbConnection.retrieveUsernameDetail(DoctorMain.username);
		try {
			while (resultSet.next()) {
				return (resultSet.getInt(9)==1?true:false);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void populateOPDHistoryTable(String patientID)
	{
		lblDoctorName.setText("Doctor Name : ");
		symptomTA.setText("");
		prescriptionTA.setText("");
		lblTokenNo.setText("Token No. : ");
		try {
			OPDDBConnection db=new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataPatientID(patientID);

			// System.out.println("Table: " + rs.getMetaData().getTableName(1));
			int NumberOfColumns = 0, NumberOfRows = 0;
			NumberOfColumns = rs.getMetaData().getColumnCount();

			while(rs.next()){
				NumberOfRows++;
			}
			rs.beforeFirst();

			//to set rows in this array
			Object Rows_Object_Array[][];
			Rows_Object_Array = new Object[NumberOfRows][NumberOfColumns];

			int R = 0;
			while(rs.next()) {
				for(int C=1; C<=NumberOfColumns;C++) {
					Rows_Object_Array[R][C-1] = rs.getObject(C);
				}
				R++;
			}
			//Finally load data to the table
			DefaultTableModel model = new DefaultTableModel(Rows_Object_Array, new String[] {
					"OPD No.","OPD Date"
			}) {
				@Override
				public boolean isCellEditable(int row, int column) {
					return false;// This causes all cells to be not editable
				}
			};
			opdHistoryTable.setModel(model); 
			opdHistoryTable.getColumnModel().getColumn(0).setPreferredWidth(150);
			opdHistoryTable.getColumnModel().getColumn(0).setMinWidth(149);
			opdHistoryTable.getColumnModel().getColumn(1).setPreferredWidth(165);
			opdHistoryTable.getColumnModel().getColumn(1).setMinWidth(165);
			db.closeConnection();
		} catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	public boolean getfreeUSG() {
		OPDDBConnection db=new OPDDBConnection();
		value=0;
		ResultSet rs= db.remaningFreeUSG(MainLogin.DoctorName);
		try {
			while(rs.next()) {
				value=rs.getInt(1);
				System.out.println(value+"totallllll");
			}
			textField.setText(value+"");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return (value>0)?true:false;
	}
	public boolean docAccess() {
		OPDDBConnection db=new OPDDBConnection();
		ResultSet rs= db.DocAccess(MainLogin.DoctorName);
		try {
			while(rs.next()) {
				return rs.getBoolean(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	public void getOPDData(String opdID)
	{
		try {
			boolean bool=false;

			OPDDBConnection db=new OPDDBConnection();
			ResultSet rs = db.retrieveAllDataWithOpdId(opdID);
			while(rs.next()){
				lblDoctorName.setText("Doctor Name : "+ rs.getObject(4));
				symptomTA.setText(""+ rs.getObject(6));
				prescriptionTA.setText(""+ rs.getObject(7));
				lblTokenNo.setText("Token No. : "+ rs.getObject(8));
				bool=rs.getBoolean(15);
				if(bool) rdbtnYes.setSelected(true);
				else  rdbtnNo.setSelected(true);
				bool=rs.getBoolean(16);
				if(bool) rdbtnUSG.setSelected(true);
				else  rdbtnnoUSG.setSelected(true);
				System.out.println(bool);
			}
			System.out.println(group.getSelection().getActionCommand()+"valueeee");
		}
		catch (SQLException ex) {
			Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
