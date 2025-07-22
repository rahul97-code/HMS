package hms.pricemaster.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class PriceMasterDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PriceMasterDBConnection() {

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
		String insertSQL     = "UPDATE `price_master` SET `item_desc`=?,`item-charges`=?, `item_charges2`=?, `item_charges3`=?, `item_charges4`=?,`item_text1`=? WHERE `item_code` = '"+data[6]+"'";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <7; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `ins_id`, `ins_name`, `ins_detail` FROM `insurance_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithCategorySrch(String cat,String insuranceType,String opd_doctorid,String Srch)
	{
	  String query="SELECT `item_code`, `item_desc`, `item-charges`,`item_text1` FROM `price_master` WHERE `item_category`= '"+cat+"' AND `item_text1` = '"+insuranceType+"' and item_desc like '%"+Srch+"%'";
	  System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithCategory(String cat)
	{
	  String query="SELECT `item_code`, `item_desc`, `item-charges`,`item_text1` FROM `price_master` WHERE `item_category`= '"+cat+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithCategory(String cat,String insuranceType,String opd_doctorid)
	{
		 String query;
		if(opd_doctorid.equals("")){
			   query="SELECT `item_code`, `item_desc`, `item-charges`,`item_text1` FROM `price_master` WHERE `item_category`= '"+cat+"' AND `item_text1` = '"+insuranceType+"'";
		}else{
			 query="SELECT `item_code`, `item_desc`, `item-charges`,`item_text1` FROM `price_master` WHERE `item_category`= '"+cat+"' AND `item_text1` = '"+insuranceType+"' and `doctor_id`='"+opd_doctorid+"'";		
		}
	  System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithID(String id)
	{
	  String query="SELECT `item_code`, `item_desc`, `item-charges`, `item_charges2`, `item_charges3`, `item_charges4`,`item_text1` FROM `price_master` WHERE `item_code`= '"+id+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex(String index)
	{
	  String query="SELECT  `item-charges`, `item_charges2`, `item_charges3`, `item_charges4` FROM `price_master` WHERE `item_code`= '"+index+"'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `price_master` WHERE `item_code`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	
	public int inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `price_master`(`item_desc`, `item-charges`, `item_charges2`, `item_charges3`, `item_charges4`, `item_category`,`item_text1`) VALUES  (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		 
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	  }
}
