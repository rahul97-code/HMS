package hms1.ipd.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;

import hms.main.DateFormatChange;
import hms.store.database.ItemsDBConnection;
import hms.store.gui.AddBatch;
import hms.store.gui.BatchBrowser;
import hms1.ipd.database.IPDDBConnection;

import javax.swing.JButton;
import javax.swing.JDialog;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.awt.event.ActionEvent;
import com.toedter.calendar.JDateChooser;
import javax.swing.JToolBar;
import java.awt.Color;

public class Edit_Bed_Details extends JDialog {

	
	private JTextField SnoTF;
	String Recidate,Recidate1;
	private JTextField BuildingTF;
	private JTextField WardTF;
	private JTextField RoomTF;
	private JTextField BednoTF;
	private JTextField timerTF;
	private JTextField ChargeTF;
	private JTextField EndtimeTF;
	private JDateChooser Startdc;
	private JDateChooser Enddatedc;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	

	public Edit_Bed_Details(final Object s_no, final IPDBedCalculation ipdBedCalculation, String building, String ward,
			String room, String bed_no, String start_date, String start_time, String end_date, String charges,final String ipd_id,String end_time) {
		setTitle("BED DETAILS");
		setBounds(100, 100, 564, 308);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setModal(true);
		JLabel lblSNo = new JLabel("S. No. :-");
		lblSNo.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo.setBounds(29, 24, 83, 25);
		getContentPane().add(lblSNo);
		
		SnoTF = new JTextField();
		SnoTF.setBounds(105, 27, 114, 19);
		getContentPane().add(SnoTF);
		SnoTF.setColumns(10);
		SnoTF.setText(s_no+"");
		
		JLabel lblBuilding = new JLabel("Building : -");
		lblBuilding.setFont(new Font("Dialog", Font.BOLD, 14));
		lblBuilding.setBounds(12, 66, 88, 25);
		getContentPane().add(lblBuilding);
		
		BuildingTF = new JTextField();
		BuildingTF.setColumns(10);
		BuildingTF.setBounds(105, 69, 114, 19);
		getContentPane().add(BuildingTF);
		BuildingTF.setText(building);
		WardTF = new JTextField();
		WardTF.setColumns(10);
		WardTF.setBounds(105, 109, 114, 19);
		getContentPane().add(WardTF);
		WardTF.setText(ward);
		
		RoomTF = new JTextField();
		RoomTF.setColumns(10);
		RoomTF.setBounds(105, 151, 114, 19);
		getContentPane().add(RoomTF);
		RoomTF.setText(room);
		
		JLabel lblSNo_1_1 = new JLabel("Room :-");
		lblSNo_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_1_1.setBounds(35, 148, 61, 25);
		getContentPane().add(lblSNo_1_1);
		
		JLabel lblWard = new JLabel("Ward :-");
		lblWard.setFont(new Font("Dialog", Font.BOLD, 14));
		lblWard.setBounds(35, 106, 77, 25);
		getContentPane().add(lblWard);
		
		BednoTF = new JTextField();
		BednoTF.setColumns(10);
		BednoTF.setBounds(105, 190, 114, 19);
		getContentPane().add(BednoTF);
		BednoTF.setText(bed_no);
		
		JLabel lblSNo_1_1_1 = new JLabel("Bed No. :-");
		lblSNo_1_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_1_1_1.setBounds(19, 187, 93, 25);
		getContentPane().add(lblSNo_1_1_1);
		
		JLabel lblStartDate = new JLabel("Start Date :-");
		lblStartDate.setFont(new Font("Dialog", Font.BOLD, 14));
		lblStartDate.setBounds(268, 24, 103, 25);
		getContentPane().add(lblStartDate);
		
		JLabel lblSNo_1_2 = new JLabel("End Date :-");
		lblSNo_1_2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_1_2.setBounds(278, 66, 103, 25);
		getContentPane().add(lblSNo_1_2);
		
		JLabel lblSNo_2_1 = new JLabel("Start Time :-");
		lblSNo_2_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_2_1.setBounds(268, 106, 103, 25);
		getContentPane().add(lblSNo_2_1);
		
		timerTF = new JTextField();
		timerTF.setColumns(10);
		timerTF.setBounds(376, 109, 114, 19);
		getContentPane().add(timerTF);
		timerTF.setText(start_time);
		
		JLabel lblSNo_1_1_2 = new JLabel("Charges :-");
		lblSNo_1_1_2.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_1_1_2.setBounds(282, 148, 89, 25);
		getContentPane().add(lblSNo_1_1_2);
		
		ChargeTF = new JTextField();
		ChargeTF.setColumns(10);
		ChargeTF.setBounds(376, 151, 114, 19);
		getContentPane().add(ChargeTF);
		ChargeTF.setText(charges);
		
		JLabel lblSNo_1_1_1_1 = new JLabel("End Time :-");
		lblSNo_1_1_1_1.setFont(new Font("Dialog", Font.BOLD, 14));
		lblSNo_1_1_1_1.setBounds(274, 187, 107, 25);
		getContentPane().add(lblSNo_1_1_1_1);
		
		EndtimeTF = new JTextField();
		EndtimeTF.setColumns(10);
		EndtimeTF.setBounds(376, 190, 114, 19);
		getContentPane().add(EndtimeTF);
		EndtimeTF.setText(end_time);
		
		JButton btnNewButton = new JButton("Update");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				IPDDBConnection db = new IPDDBConnection();
				if(SnoTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please Enter S No.", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(BuildingTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please Enter Building name", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(BednoTF.getText().toString().equals("") || RoomTF.getText().toString().equals("") )
				{
					JOptionPane.showMessageDialog(null,
							"Please Enter Bed No. or Room No.", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				if(WardTF.getText().toString().equals(""))
				{
					JOptionPane.showMessageDialog(null,
							"Please Enter Ward", "Failed",
							JOptionPane.INFORMATION_MESSAGE);
					return;
				}
				
				Date date1;
				date1 = Startdc.getDate();
				if(date1==null)
				{
					JOptionPane.showMessageDialog(null, "Choose Start Date from Right Box.", "Error", JOptionPane.ERROR_MESSAGE);
					Startdc.grabFocus();
					return;
				}
				Date date2;
				date2 = Enddatedc.getDate();
				if(date2==null)
				{
					JOptionPane.showMessageDialog(null, "Choose End Date from Right Box.", "Error", JOptionPane.ERROR_MESSAGE);
					Enddatedc.grabFocus();
					return;
				}
                 String[] data=new String[11];
				
				data[0] = BuildingTF.getText().toString(); 
				data[1] = WardTF.getText().toString();
				data[2] = Recidate;
				data[3] = Recidate1;
				data[4] = RoomTF.getText().toString();
				data[5] = BednoTF.getText().toString();
				data[6] = timerTF.getText().toString();
				data[7] = ChargeTF.getText().toString();
				data[8] =  EndtimeTF.getText().toString();;
				
					try {
						
						db.updateBadDetails(data,s_no+"");
						
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "Data updated successfully", "Success",
							JOptionPane.INFORMATION_MESSAGE);
					ipdBedCalculation.populatedDoctorTable(ipd_id);
					db.closeConnection();
					dispose();
			}
		});
		btnNewButton.setBounds(254, 238, 117, 25);
		getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Cancel");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			      dispose();
			}
		});
		btnNewButton_1.setBounds(376, 238, 117, 25);
		getContentPane().add(btnNewButton_1);
		
	 Startdc = new JDateChooser();
		Startdc.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {

					Recidate = DateFormatChange
							.StringToMysqlDate((Date) arg0
									.getNewValue());
					System.out.print(Recidate+"rahulrrrrrrr");
				}
			}
		});
		Startdc.setBounds(378, 27, 112, 19);
		getContentPane().add(Startdc);
		 if (!start_date.equals("")) {
				Date date1 = null;
				try {
					date1 = new SimpleDateFormat("yyyy-MM-dd").parse(start_date);
				} catch (ParseException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				Startdc.setDate(date1);
			}
		
		 Enddatedc = new JDateChooser();
		Enddatedc.addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent arg0) {
				if ("date".equals(arg0.getPropertyName())) {

					Recidate1 = DateFormatChange
							.StringToMysqlDate((Date) arg0
									.getNewValue());
					System.out.print(Recidate+"rahulrrrrrrr");
				}
			}
		});
		Enddatedc.setBounds(376, 72, 114, 19);
		getContentPane().add(Enddatedc);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				int i=JOptionPane.showConfirmDialog(null,"Do you want to delete this bed?","Info",JOptionPane.YES_NO_OPTION);
				
				if(i==1)
				{
					System.out.println("arun");
					return;
				}
				IPDDBConnection db = new IPDDBConnection();
				try {
					db.DeleteBad(s_no+"");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JOptionPane.showMessageDialog(null, "Bed deleted successfully", "Delete",
						JOptionPane.INFORMATION_MESSAGE);
				ipdBedCalculation.populatedDoctorTable(ipd_id);
				db.closeConnection();
			}
		});
		btnDelete.setForeground(Color.RED);
		btnDelete.setBounds(12, 238, 117, 25);
		getContentPane().add(btnDelete);
		if (!end_date.equals("")) {
			Date date1 = null;
			try {
				date1 = new SimpleDateFormat("yyyy-MM-dd").parse(end_date);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			Enddatedc.setDate(date1);
		}
		// TODO Auto-generated constructor stub
	}
}
