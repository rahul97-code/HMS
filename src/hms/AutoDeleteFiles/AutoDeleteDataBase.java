package hms.AutoDeleteFiles;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hms.main.DBConnection;

public class AutoDeleteDataBase extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AutoDeleteDataBase() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	public ResultSet getDATA() {
		String query="select folder_name, before_days from auto_delete_folder_files";	
		try {
			rs=statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

}
