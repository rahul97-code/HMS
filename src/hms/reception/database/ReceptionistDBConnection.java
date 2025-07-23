package hms.reception.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class ReceptionistDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ReceptionistDBConnection() {

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
		String insertSQL     = "UPDATE `reception_detail` SET `reception_name`=?,`reception_username`=?,`reception_password`=?,`reception_telephone`=?,`reception_address`=?,`reception_qualification`=?,`reception_text1`=? WHERE  `reception_id`="+data[7];
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void updateDataLastLogin(String receptionID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `reception_detail` set `reception_lastlogin` = '"+timeStamp+"' where `reception_id` = "+receptionID);
	  }
	public void updateReceptionCounter(String counter) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `reception_counter` SET `text`='"+counter+"' WHERE 1");
	  }
	public void updateDataPassword(String receptionUN,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `reception_detail` SET `reception_password`='"+password+"' where `reception_username` = '"+receptionUN+"'");
	  }
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `reception_detail` WHERE 1";
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
	  String query="SELECT `reception_id`, `reception_name`, `reception_username`, `reception_password`, `reception_telephone`, `reception_address`, `reception_qualification` FROM `reception_detail` WHERE 1";
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
	  String query="SELECT `reception_id`, `reception_name`, `reception_telephone`, `reception_address` FROM `reception_detail` WHERE reception_id LIKE '%"+index+"%' OR reception_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet getExamAproveAcess(String rec_id)
	{
	  String query="SELECT `exam_aprovel_access` FROM `reception_detail` WHERE reception_id = '"+rec_id+"'";
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
	  String query="SELECT `reception_id`, `reception_name`, `reception_telephone`, `reception_address` FROM `reception_detail` WHERE1";
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
	  String query="SELECT `reception_id` FROM `reception_detail` WHERE `reception_name` = '"+name+"'";
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
	  String query="SELECT `reception_name`, `reception_username`, `reception_password`,`reception_telephone`, `reception_address`, `reception_qualification`,COALESCE(`reception_text1`, 'NO') FROM `reception_detail` WHERE `reception_id` = "+opID+"";
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
	  String query="SELECT * FROM `reception_detail` WHERE `reception_username` = '"+name+"' AND `reception_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievePassword(String receptionUN)
	{
	  String query="SELECT `reception_password` FROM `reception_detail` WHERE `reception_username` = '"+receptionUN+"'";
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
	  String query="SELECT  `reception_id`, `reception_name`,`reception_lastlogin`,COALESCE(`reception_text1`, 'NO'),`shift`,ins_bill_access FROM `reception_detail` WHERE `reception_username` = '"+name+"'";
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
	  String query="SELECT * FROM `reception_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reception_detail WHERE reception_id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `reception_detail`( `reception_name`, `reception_username`, `reception_password`, `reception_telephone`, `reception_address`, `reception_qualification`,`reception_text1`) VALUES  (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
