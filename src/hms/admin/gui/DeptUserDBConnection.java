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

public class DeptUserDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public DeptUserDBConnection() {

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
		String insertSQL     = "UPDATE `dept_user_detail` SET `dept_user_name`=?,`dept_user_username`=?,`dept_user_password`=?,`dept_user_telephone`=?,`dept_user_address`=?,`dept_user_qualification`=?,`dept_user_text1`=? WHERE  `dept_user_id`="+data[7];
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void updateDataLastLogin(String dept_userID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `dept_user_detail` set `dept_user_lastlogin` = '"+timeStamp+"' where `dept_user_id` = "+dept_userID);
	  }
	public void updatedept_userCounter(String counter) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `dept_user_counter` SET `text`='"+counter+"' WHERE 1");
	  }
	public void updateDataPassword(String dept_userUN,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `dept_user_detail` SET `dept_user_password`='"+password+"' where `dept_user_username` = '"+dept_userUN+"'");
	  }
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `dept_user_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
        try {
			while(rs.next()){
			NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfRows;	
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `dept_user_id`, `dept_user_name`, `dept_user_username`, `dept_user_password`, `dept_user_telephone`, `dept_user_text1`, `dept_user_qualification` FROM `dept_user_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2(String index)
	{
	  String query="SELECT `dept_user_id`, `dept_user_name`, `dept_user_telephone`, `dept_user_text1` FROM `dept_user_detail` WHERE dept_user_id LIKE '%"+index+"%' OR dept_user_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2()
	{
	  String query="SELECT `dept_user_id`, `dept_user_name`, `dept_user_telephone`, `dept_user_text1` FROM `dept_user_detail` WHERE1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex(String name)
	{
	  String query="SELECT `dept_user_id` FROM `dept_user_detail` WHERE `dept_user_name` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithID(String opID)
	{
	  String query="SELECT `dept_user_name`, `dept_user_username`, `dept_user_password`,`dept_user_telephone`, `dept_user_address`, `dept_user_qualification`,`dept_user_text1` FROM `dept_user_detail` WHERE `dept_user_id` = "+opID+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUserPassword(String name,String pass)
	{
	  String query="SELECT * FROM `dept_user_detail` WHERE `dept_user_username` = '"+name+"' AND `dept_user_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievePassword(String dept_userUN)
	{
	  String query="SELECT `dept_user_password` FROM `dept_user_detail` WHERE `dept_user_username` = '"+dept_userUN+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUsernameDetail(String name)
	{
	  String query="SELECT  `dept_user_id`, `dept_user_name`,`dept_user_lastlogin`,COALESCE(`dept_user_text1`, 'NO') FROM `dept_user_detail` WHERE `dept_user_username` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmae(String index)
	{
	  String query="SELECT * FROM `dept_user_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM dept_user_detail WHERE dept_user_id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `dept_user_detail`( `dept_user_name`, `dept_user_username`, `dept_user_password`, `dept_user_telephone`, `dept_user_address`, `dept_user_qualification`,`dept_user_text1`) VALUES  (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
