package hms.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

//this class handles all database connections
public class DBConnection {
	Developing_environment DE=new Developing_environment();
	Connection connect = null;
	Statement statement = null;
	ResultSet rs = null;

	// Database configuration 
	String DB_PORT_NUMBER = "3306";
	String DB_NAME = "hospital_db";
	  String DB_USER_NAME ="hospital";
	  String DB_PASSWORD =DE.ACTIVE!=false?"rotaryhospital":"ambalarotaryhospital";
	  String server =DE.ACTIVE!=false?"db.dev.erp.mdi":"192.168.1.33";
	
	DateFormat dateFormat = new SimpleDateFormat("DD/MM/yyyy");
	Calendar cal = Calendar.getInstance();
	String d=dateFormat.format(cal.getTime());
	/** Creates a new instance of ConnectToDB */
	// initialize the variables in this constructor

	public static void main(String d[]) {
		DBConnection DBConnection=new DBConnection();
		
		DBConnection.closeConnection();
	}
	public DBConnection() {
		readCounterFile();
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			System.out.println(server);
			return;
		}
		Connection connection = null;
		try {
			connection = DriverManager.getConnection("jdbc:mysql://"+server+"/"
					+ DB_NAME, DB_USER_NAME, DB_PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null,
					"Please power on database server", "Failed to make database connection!",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("Failed to make connection!");
			return;
		}

		if (connection != null) {
			connect = connection;
		} else {
			JOptionPane.showMessageDialog(null,
					"Please power on database server", "Failed to make database connection!",
					JOptionPane.ERROR_MESSAGE);
			System.out.println("Failed to make connection!");
		}
	}

	// to get connection
	public Connection getConnection() {
		return connect;
	}

	public Statement getStatement() {
		return statement;
	}

	// To get column names and table name
	public void writeMetaData(ResultSet resultSet) throws SQLException {
		System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
			System.out.println("Column " + i + " "
					+ resultSet.getMetaData().getColumnName(i));
		}

	}

	// Close the database connection
	public void closeConnection() {
		try {
			statement.close();
			connect.close();
		} catch (SQLException error) {
			JOptionPane.showMessageDialog(null, error.getMessage(), "Error",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}
	public void readCounterFile() {
		// The name of the file to open.
		String fileName = "data.mdi";
		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(fileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String str = null;
			boolean fetch=true;
			while ((line = bufferedReader.readLine()) != null&&fetch) {
				// System.out.println(line);
				str = line;
				fetch=false;
			}
			String data[] = new String[22];
			int i = 0;
			for (String retval : str.split("@")) {
				data[i] = retval;
				i++;
			}
			server=data[0];
			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + fileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + fileName + "'");
			// Or we could just do this:
			// ex.printStackTrace();
		}
	}
}
