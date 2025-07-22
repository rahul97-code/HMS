

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class SaltDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public SaltDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public void insertData(String like,int len) throws SQLException
	{
		String insertSQL = "INSERT INTO salt_master_2 (SELECT * FROM salt_master WHERE "+like+" CHAR_LENGTH(f4) <="+len+" ORDER BY f12 DESC)";
		//String insertSQL = "INSERT INTO salt_master_2 (SELECT * FROM salt_master WHERE "+like+" CHAR_LENGTH(f4) <="+len+" AND f12!='ROTARY' ORDER BY CAST(f7 AS DECIMAL) ASC LIMIT 1)";
		
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		preparedStatement.executeUpdate();
	 
	}
	public ResultSet retrieveSalts()
	{
	  String query="SELECT DISTINCT f4 FROM salt_master WHERE f12='ROTARY' ORDER BY f4 ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
}
