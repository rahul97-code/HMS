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

public class AdminDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AdminDBConnection() {

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
	public void inserIpAddressData(String user ,String department) throws Exception
	  {
		  String insertSQL = "INSERT INTO hospital_db.user_ip_address\r\n"
		  		+ "(`User`, ip_address,department)\r\n"
		  		+ "VALUES('"+user+"',(select SUBSTRING_INDEX(USER(), '@', -1)),'"+department+"');";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  preparedStatement.executeUpdate();
	  }
	public void updateDataLastLogn(String docID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `admin_account` set `last_login` = '"+timeStamp+"' where `admin_username` = '"+docID+"'");
	  }
	
	public void updateDataPassword(String docID,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `admin_account` SET `admin_password`='"+password+"' where `admin_username` = '"+docID+"'");
	  }
	
	public ResultSet retrieveUserPassword(String name,String pass)
	{
	  String query="SELECT * FROM `admin_account` WHERE `admin_username` = '"+name+"' AND  `admin_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public String retrieveExamModulePassword()
	{
	  String query="SELECT value FROM `karun_sparsh_param` WHERE `dept`='ADMIN_EXAM' ";
	  String pass="";
		try {
			rs = statement.executeQuery(query);
			while(rs.next()) {
				pass=rs.getString(1);
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return pass;
	}
	public ResultSet retrievePassword(String id)
	{
	  String query="SELECT `admin_password` FROM `admin_account` WHERE `admin_username` = '"+id+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM doctor_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `doctor_detail`( `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_qualification`) VALUES (?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <9; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
