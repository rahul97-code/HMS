package hms.admin.gui;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class MessageDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public MessageDBConnection() {

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

	public void updateMessage(String messageID, String message,
			String messageStatus) throws Exception {

		statement.executeUpdate("UPDATE `dispaly_message` SET `message`='"
				+ message + "',`status`=" + messageStatus + " WHERE `_id`="
				+ messageID);
	}

	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `dispaly_message` WHERE `_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public ResultSet retrieveAllMessages() {
		String query = "SELECT * FROM `dispaly_message` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `dispaly_message`( `message`, `status`) VALUES (?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <3; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }

}
