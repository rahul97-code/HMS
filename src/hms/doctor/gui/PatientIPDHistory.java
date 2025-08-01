package hms.doctor.gui;

import hms.opd.gui.OPDBrowser;
import hms1.ipd.database.IPDDBConnection;

import java.awt.Font;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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

public class PatientIPDHistory extends JPanel {
	private JTable opdHistoryTable;
	private JLabel lblDoctorName;
	private JTextArea symptomTA;
	private JLabel lblTokenNo;
	private JTextArea prescriptionTA;

	/**
	 * Create the panel.
	 */
	public PatientIPDHistory() {
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
				//	getOPDData(""+selectedObject);
					} catch (Exception e2) {
						// TODO: handle exception
					}
				}
			});
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "Note & Symptom :", TitledBorder.LEADING, TitledBorder.TOP, new Font("Tahoma", Font.PLAIN, 12), null));
		panel.setBounds(354, 150, 249, 122);
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
		lblDoctorName.setBounds(369, 44, 256, 20);
		add(lblDoctorName);
		
		lblTokenNo = new JLabel("Token No. :");
		lblTokenNo.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTokenNo.setBounds(369, 83, 234, 20);
		add(lblTokenNo);

	}
	public void populateIPDHistoryTable(String patientID)
	{
		lblDoctorName.setText(" ");
    	symptomTA.setText("");
    	prescriptionTA.setText("");
    	lblTokenNo.setText("");
		try {
            IPDDBConnection db=new IPDDBConnection();
            ResultSet rs = db.retrieveAllDataPatientID1(patientID);
            
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
//	public void getOPDData(String opdID)
//	{
//		try {
//            OPDDBConnection db=new OPDDBConnection();
//            ResultSet rs = db.retrieveAllDataWithOpdId(opdID);
//            while(rs.next()){
//            	lblDoctorName.setText("Doctor Name : "+ rs.getObject(4));
//            	symptomTA.setText(""+ rs.getObject(6));
//            	prescriptionTA.setText(""+ rs.getObject(7));
//            	lblTokenNo.setText("Token No. : "+ rs.getObject(8));
//            }
//		} catch (SQLException ex) {
//         Logger.getLogger(OPDBrowser.class.getName()).log(Level.SEVERE, null, ex);
//     }
//	}
}
