package hms.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class NewsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public NewsDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "UPDATE `systemnews` SET `news`=? WHERE 1";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <2; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public String getNews()
	{
		String news="";
	  String query="SELECT * FROM `systemnews` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		try {
			while (rs.next()) {
				news=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return news;
	}
	
	public String[] getEmail()
	{
		String[] news=new String[3];
	  String query="SELECT `email_id`, `password`, `email_account_type` FROM `mailsetting` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		try {
			while (rs.next()) {
				news[0]=rs.getObject(1).toString();
				news[1]=rs.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeConnection();
		return news;
	}
}
