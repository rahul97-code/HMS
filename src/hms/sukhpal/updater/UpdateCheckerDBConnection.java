package hms.sukhpal.updater;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class UpdateCheckerDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public UpdateCheckerDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public void updateVersionNO(String Version,String remark) throws Exception
	  {
		statement.executeUpdate("insert into `hms_version`(version_no,change_remarks)values('"+Version+"','"+remark+"')");
	  }
	public String retrieveVersionNo() {
		String query = "SELECT `version_no` FROM `hms_version` ORDER BY `change_id` DESC LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String version="";
		try {
			while (rs.next()) {
				version=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
	
}
