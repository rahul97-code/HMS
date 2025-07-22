package hms.main;

import hms.sukhpal.updater.UpdateCheckerDBConnection;

import java.awt.BorderLayout;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class AboutHMS extends JDialog {

	private final JPanel contentPanel = new JPanel();
	String[] data=new String[10];
	String version="";
	String newVersion="",mainDir="";
	 static String OS;
	 String path;
		String mainPath;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			AboutHMS dialog = new AboutHMS();
			dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public AboutHMS() {
		setTitle("About HMS");
		setResizable(false);
		setIconImage(Toolkit.getDefaultToolkit().getImage(AboutHMS.class.getResource("/icons/smallLogo.png")));
		setModal(true);
		setBounds(400, 200, 416, 332);
		readData();
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(144, 238, 144));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		{
			JLabel lblHms = new JLabel("HMS");
			lblHms.setFont(new Font("Tahoma", Font.BOLD, 32));
			lblHms.setHorizontalAlignment(SwingConstants.CENTER);
			lblHms.setBounds(75, 11, 245, 39);
			contentPanel.add(lblHms);
		}
		{
			JLabel lblHospitalManagementSystem = new JLabel("Hospital Management System");
			lblHospitalManagementSystem.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblHospitalManagementSystem.setHorizontalAlignment(SwingConstants.CENTER);
			lblHospitalManagementSystem.setBounds(85, 49, 245, 25);
			contentPanel.add(lblHospitalManagementSystem);
		}
		{
			JLabel lblDevelopedManaged = new JLabel("Developed & Managed");
			lblDevelopedManaged.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblDevelopedManaged.setHorizontalAlignment(SwingConstants.CENTER);
			lblDevelopedManaged.setBounds(75, 137, 251, 19);
			contentPanel.add(lblDevelopedManaged);
		}
		{
			JLabel lblBy = new JLabel("From");
			lblBy.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblBy.setHorizontalAlignment(SwingConstants.CENTER);
			lblBy.setBounds(75, 167, 251, 20);
			contentPanel.add(lblBy);
		}
		{
			JLabel lblNewLabel = new JLabel("MDI");
			lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 15));
			lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
			lblNewLabel.setBounds(75, 198, 251, 19);
			contentPanel.add(lblNewLabel);
		}
		{
			JLabel lblMob = new JLabel("Mob. : 9215575671");
			lblMob.setHorizontalAlignment(SwingConstants.CENTER);
			lblMob.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblMob.setBounds(75, 228, 251, 19);
			contentPanel.add(lblMob);
		}
		{
			JLabel lblEmailSukhpalsainigmailcom = new JLabel("Email : racghambala@gmail.com");
			lblEmailSukhpalsainigmailcom.setHorizontalAlignment(SwingConstants.CENTER);
			lblEmailSukhpalsainigmailcom.setFont(new Font("Tahoma", Font.PLAIN, 15));
			lblEmailSukhpalsainigmailcom.setBounds(75, 258, 251, 19);
			contentPanel.add(lblEmailSukhpalsainigmailcom);
		}
		{
			
			JLabel lblVersionNo = new JLabel("Current Version No :  " +version);
			lblVersionNo.setHorizontalAlignment(SwingConstants.CENTER);
			lblVersionNo.setFont(new Font("Tahoma", Font.PLAIN, 13));
			lblVersionNo.setBounds(84, 84, 224, 25);
			contentPanel.add(lblVersionNo);
		}
		UpdateCheckerDBConnection updateCheckerDBConnection=new UpdateCheckerDBConnection();
		String newVersion=updateCheckerDBConnection.retrieveVersionNo();
		updateCheckerDBConnection.closeConnection();
		
		JLabel label = new JLabel("New Version No :  "+newVersion);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setFont(new Font("Tahoma", Font.PLAIN, 13));
		
		if(!newVersion.equals(version))
		{
			label.setBounds(96, 109, 224, 25);
		}
		contentPanel.add(label);
	}
	
	public void readData() {
		FileInputStream fis = null;
		int counter = 0;
		try {
			File file = new File("data.mdi");
			fis = new FileInputStream(file);
			// Get the object of DataInputStream
			DataInputStream in = new DataInputStream(fis);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;
			// Read File Line By Line
			while ((strLine = br.readLine()) != null) {
				
				data[counter] = strLine;
				counter++;
				System.out.println(strLine);
			}
			String mainDir1[] = new String[22];
			int j = 0;
			for (String retval : data[0].split("@")) {
				mainDir1[j] = retval;
				j++;
			}
			mainDir = mainDir1[1];
			System.out.println("dddddd"+mainDir);
			if(counter==1)
			{
				return;
			}
			
			String verion[] = new String[22];
			int i = 0;
			for (String retval : data[1].split(":")) {
				verion[i] = retval;
				i++;
			}
			version=verion[1].trim();
			in.close();
		} catch (Exception ex) {
			System.out.print(""+ex);
		} finally {
			try {
				if (null != fis)
					fis.close();
			} catch (IOException ex) {
			}
		}

	}
}
