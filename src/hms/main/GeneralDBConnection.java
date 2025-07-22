package hms.main;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class GeneralDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public GeneralDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public String  getCurrentDate() {
		String query = "select CURRENT_DATE() ";
		try {
			rs = statement.executeQuery(query);
			while(rs.next()) {
				return rs.getObject(1).toString();
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}
	
	public String  getCurrentTime() {
		String query = "select TIME_FORMAT(CURRENT_TIME(),'%r')";
		try {
			rs = statement.executeQuery(query);
			while(rs.next()) {
				return rs.getObject(1).toString();
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	public String retrieveFormula1() {
		String query = "SELECT `detail_desc` FROM `hms_detail` WHERE `detail_id`=2";
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
