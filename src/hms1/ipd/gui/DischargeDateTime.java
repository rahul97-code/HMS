package hms1.ipd.gui;

import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import javax.swing.JButton;
import javax.swing.JComboBox;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;

import hms.main.DateFormatChange;
import hms1.ipd.database.IPDDBConnection;

import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;
import javax.swing.JCheckBox;
import java.awt.Font;

public class DischargeDateTime extends JDialog {
	private String DischargeDateSTR;
	private JButton SaveBTN;
	private JComboBox MinutesCB;
	private JComboBox hoursCB;
	private JComboBox TimeTypeCB;
	private JDateChooser DischargeDateCB;
	int RELAXINGHOURS=-12;
	String ipd_id="";
	Date changeDateTime=null;
	String changeDateTimeSTR="";
	private JCheckBox chckbxRemaining;
	
	
	public DischargeDateTime(String ipd_id,final IPDBill ipdBill) {
		this.ipd_id=ipd_id;
		getPatientBedData();
		
		setBounds(100, 100, 457, 204);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		getContentPane().setLayout(null);
		setLocationRelativeTo(null);
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(null, "discharge time adjustment", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(12, 12, 426, 150);
		getContentPane().add(panel);
		panel.setLayout(null);
		
		DischargeDateCB = new JDateChooser();
		DischargeDateCB.setBounds(12, 26, 109, 19);
		panel.add(DischargeDateCB);
		JTextFieldDateEditor editor = (JTextFieldDateEditor) DischargeDateCB.getDateEditor();
		editor.setEditable(false);
		DischargeDateCB.setDateFormatString("yyyy-MM-dd");
		DischargeDateCB.setDate(new Date());
		DischargeDateCB.getDateEditor().addPropertyChangeListener(
				new PropertyChangeListener() {
					@Override
					public void propertyChange(PropertyChangeEvent arg0) {
						// TODO Auto-generated method stub
						if ("date".equals(arg0.getPropertyName())) {
							DischargeDateSTR = DateFormatChange
									.StringToMysqlDate((Date) arg0
											.getNewValue());
							System.out.println("arun1");
							CheckDateTime();
							
						}
					}
				});
		DischargeDateCB.setMaxSelectableDate(new Date());
		
				
				hoursCB = new JComboBox();
				hoursCB.setBounds(149, 26, 61, 19);
				panel.add(hoursCB);
				hoursCB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//System.out.println("arun2");
						CheckDateTime();
					}
				});
				hoursCB.setToolTipText("hours");
				hoursCB.setModel(new DefaultComboBoxModel(new String[] {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"}));
				
				MinutesCB = new JComboBox();
				MinutesCB.setBounds(235, 26, 61, 19);
				panel.add(MinutesCB);
				MinutesCB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						//System.out.println("arun3");
						CheckDateTime();
					}
				});
				MinutesCB.setToolTipText("minutes");
				MinutesCB.setModel(new DefaultComboBoxModel(new String[] {"00", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59"}));
				
				
				TimeTypeCB = new JComboBox();
				TimeTypeCB.setBounds(322, 26, 61, 19);
				panel.add(TimeTypeCB);
				TimeTypeCB.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						CheckDateTime();
						
					}
				});
				TimeTypeCB.setModel(new DefaultComboBoxModel(new String[] {"AM", "PM"}));
				
				SaveBTN = new JButton("Save");
				SaveBTN.setBounds(287, 117, 96, 21);
				panel.add(SaveBTN);
				SaveBTN.setEnabled(false);
				
				JButton CancelBTN = new JButton("Cancel");
				CancelBTN.setBounds(172, 117, 96, 21);
				panel.add(CancelBTN);
				
				chckbxRemaining = new JCheckBox("");
				chckbxRemaining.setFont(new Font("Dialog", Font.ITALIC, 13));
				chckbxRemaining.setBounds(12, 66, 31, 36);
				panel.add(chckbxRemaining);
				
				JLabel lblNewLabel = new JLabel("<html>Confirm that all remaining medicines have <br> been returned.</html> ");
				lblNewLabel.setFont(new Font("Dialog", Font.ITALIC, 13));
				lblNewLabel.setBounds(52, 66, 331, 43);
				panel.add(lblNewLabel);
				CancelBTN.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						changeDateTime=null;
						dispose();
					}
				});
				SaveBTN.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!chckbxRemaining.isSelected()) {
						JOptionPane.showMessageDialog(null, "Please select checkBox!", "Data Saved",
								JOptionPane.ERROR_MESSAGE);
						return;
						}
						setMedReturnedStatus();
						ipdBill.dischargeDateTime=changeDateTime;
						dispose();
						//System.out.println(hoursCB.getSelectedItem()+":"+MinutesCB.getSelectedItem()+":"+"00 "+TimeTypeCB.getSelectedItem());
					}
				});
		
		SetData();
	
	}
	
	public void SetData() {
		MinutesCB.setSelectedItem(new Date().getMinutes()+"");
		
		int hrs=new Date().getHours();
		hrs=hrs>12?hrs-12:hrs;
		hoursCB.setSelectedItem(hrs+"");
		
		hrs=new Date().getHours();
		String type=hrs>=12?"PM":"AM";
		System.out.println(type);
		TimeTypeCB.setSelectedItem(type+"");
	}

	public void CheckDateTime() {
		
		String dateTime=DischargeDateSTR+" "+hoursCB.getSelectedItem()+":"+MinutesCB.getSelectedItem()+":"+"00 "+TimeTypeCB.getSelectedItem().toString().toLowerCase();
		SimpleDateFormat timeFormat1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss aa");
		Date change_date=null;
		Date bedDate=null;
		try {
			change_date=timeFormat1.parse(dateTime);
			bedDate=timeFormat1.parse(changeDateTimeSTR);
			
		} catch (ParseException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}	
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR,RELAXINGHOURS);
		Date HourBack = cal.getTime();
		
		//System.out.println(HourBack.after(change_date)+"-----"+HourBack+"------"+change_date);
		if(!bedDate.after(change_date) && (!HourBack.after(change_date) && !new Date().before(change_date))) {
		  SaveBTN.setEnabled(true);
		  changeDateTime=change_date;
		}else {
		  SaveBTN.setEnabled(false);
		  changeDateTime=null;
		}
		this.addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
               changeDateTime=null;
            }
        });

	}
	
	public void getPatientBedData() {
		IPDDBConnection IPDDBConnection=new IPDDBConnection();
		ResultSet rs=IPDDBConnection.retrieveLastBedDateTime(ipd_id);
		changeDateTimeSTR="";
		try {
		while(rs.next()) {
			changeDateTimeSTR=rs.getString(1);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IPDDBConnection.closeConnection();
	
	}
	
	public void setMedReturnedStatus() {
		IPDDBConnection IPDDBConnection=new IPDDBConnection();
		try {
			IPDDBConnection.updateMedReturnedStatus(ipd_id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IPDDBConnection.closeConnection();
	
	}
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DischargeDateTime window = new DischargeDateTime("63848",null);
					window.setModal(true);
					window.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
