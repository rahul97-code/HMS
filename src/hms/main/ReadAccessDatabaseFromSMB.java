package hms.main;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.*;
import java.util.*;
import java.util.Date;

import javax.swing.JOptionPane;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

public class ReadAccessDatabaseFromSMB {
	public Connection connect;
	public Statement statement;
	private String GetXrayDB_location;
	private static final String XrayDB_location = "/home/mdi-android-1/Documents/Backup/Xray/USG/";
	private static final String XrayImages_location = "/home/mdi-android-1/Documents/Backup/Xray/USG/";
	
	public static void main(String[] args) throws MalformedURLException {
		try {
			new ReadAccessDatabaseFromSMB();
		} catch (MalformedURLException | SmbException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	
   

	public ReadAccessDatabaseFromSMB() throws MalformedURLException, SmbException {
//    	readXrayFile();
        // Database connection parameters
        String dbURL = "jdbc:ucanaccess://///smb://Administrator:tech@192.168.1.121\\database\\PatientData.accdb";
        String username = ""; // Leave blank if not required
        String password = ""; // Leave blank if not required
        
//        String path1="smb://Administrator:tech@192.168.1.121/database/";
////        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, "Administrator", "tech");
//        SmbFile file = new SmbFile(path1);
//        SmbFile[] f=file.listFiles();
//        System.out.println(f.length);
////        for (int i= 0;i<f.length;i++) {
////System.out.println(f[i].getName());
////			
////		}
        
        Connection connection = null;
        try {
            // Load the JDBC driver class
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            // Establish the connection
            connection = DriverManager.getConnection(dbURL, username, password);
            // Create a statement
             statement = connection.createStatement();
            // Execute a query to select all data from a table
            if (connection != null) {
    			connect = connection;
    		} else {
    			JOptionPane.showMessageDialog(null,
    					"Please power on database server", "Failed to make database connection!",
    					JOptionPane.ERROR_MESSAGE);
    			System.out.println("Failed to make connection!");
    		}

            
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

	// to get connection
	public Connection getConnection() {
		return connect;
	}

	public Statement getStatement() {
		return statement;
	}
	// Close the database connection
	public void closeConnection() {
		try {
			connect.close();
		} catch (SQLException error) {
			JOptionPane.showMessageDialog(null, error.getMessage(), "Error",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}
	public void closeStatement() {
		try {
			statement.close();
		} catch (SQLException error) {
			JOptionPane.showMessageDialog(null, error.getMessage(), "Error",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}
	public void readXrayFile() {
		// The name of the file to open.
		String fileName = "Xray.mdi";
		// This will reference one line at a time
		try {
			File f=new File(fileName);
			if(!f.exists()) {
				f.createNewFile();
				writeDefaultContent(f);
			}
			FileReader fileReader = new FileReader(fileName);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line=null;
			while ((line = bufferedReader.readLine()) != null) {
				GetXrayDB_location=line.split("@")[1];
				break;
			}
//			System.out.println("arun="+GetXrayDB_location);
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
		}
	}
	private void writeDefaultContent(File f) {
		// TODO Auto-generated method stub
		String content="XrayDB_location@"+XrayDB_location+"\n"+"XrayImages_location@"+XrayImages_location;
		try {
			FileWriter writer = new FileWriter(f);
			writer.write(content);
			writer.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}	
	}
}