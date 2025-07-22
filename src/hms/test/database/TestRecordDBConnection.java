package hms.test.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class TestRecordDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public TestRecordDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public ResultSet retrieveData(String query) {
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }

	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM test_record_no WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	
	public int inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `test_record_no`(`p_id`, `report_name`, `date_time`) VALUES  (?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		  for (int i = 1; i <4; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		 
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	  }
}
