package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class SuppliersDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public SuppliersDBConnection() {

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
		String insertSQL     = "UPDATE `supplier_detail` SET `supplier_name`=?, `display_name`=?, `supplier_mobile`=?, `supplier_email`=?, `supplier_street`=?, `supplier_city`=?, `supplier_state`=?, `supplier_zipcode`=?, `supplier_region`=?, `supplier_phone`=?, `supplier_fax`=?, `supplier_fiscal_code`=?, `supplier_regno`=?, `supplier_tax_id`=?, `supplier_manager`=?, `supplier_bank`=?, `supplier_account`=?, `supplier_status`=?, `supplier_date`=?, `supplier_time`=?, `supplier_entry_user`=? WHERE `supplier_id` = "+data[21]+"";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <22; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	public void updateDataLastLogn(String docID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `supplier_detail` set `doc_lastlogin` = '"+timeStamp+"' where `doc_id` = "+docID);
	  }
	public void updateDataStatus(String docID,String status) throws Exception
	  {
		statement.executeUpdate("update `supplier_detail` set `doc_active` = '"+status+"' where `doc_id` = "+docID);
	  }
	public void updateDataCounter(String docID,String counter) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `supplier_detail` SET `doc_text2`='"+counter+"' where `doc_id` = "+docID);
	  }
	
	public void updateDataPassword(String docID,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `supplier_detail` SET `doc_password`='"+password+"' where `doc_id` = "+docID);
	  }
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `patient_detail` WHERE 1";
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
	  String query="SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `supplier_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDetail(String index)
	{
	  String query="SELECT  `supplier_name`, `display_name`, `supplier_mobile`, `supplier_email`, `supplier_street`, `supplier_city`, `supplier_state`, `supplier_zipcode`, `supplier_region`, `supplier_phone`, `supplier_fax`, `supplier_fiscal_code`, `supplier_regno`, `supplier_tax_id`, `supplier_manager`, `supplier_bank`, `supplier_account`, `supplier_credits`, `supplier_debits`, `supplier_rating`, `supplier_status`, `supplier_date`, `supplier_time`, `supplier_entry_user` FROM `supplier_detail` WHERE supplier_id ="+index+"";
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
	  String query="SELECT `supplier_id`, `supplier_name`, `supplier_mobile`, `supplier_credits`, `supplier_debits`, `supplier_rating` FROM `supplier_detail` ORDER BY  `supplier_name` ASC ";
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
	  String query="SELECT `doc_id`, `doc_specialization`, `doc_opdroom` FROM `supplier_detail` WHERE `doc_name` = '"+name+"'";
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
	  String query="SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`,`doc_lastlogin`,`doc_text2` FROM `supplier_detail` WHERE `doc_username` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchSupplierWithIdOrNmae(String index)
	{
	  String query="SELECT `supplier_id`, `supplier_name`, `display_name`, `supplier_mobile`, `supplier_email`, `supplier_street`, `supplier_city`, `supplier_state`, `supplier_zipcode`, `supplier_region`, `supplier_phone`, `supplier_fax`, `supplier_fiscal_code` FROM `supplier_detail` where supplier_id LIKE '%"+index+"%' OR supplier_name LIKE '%"+index+"%' OR display_name LIKE '%"+index+"%'";
		System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchSupplierWithId(String index)
	{
	  String query="SELECT * FROM `supplier_detail` where supplier_id LIKE '"+index+"'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM supplier_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `supplier_detail`(`supplier_name`, `display_name`, `supplier_mobile`, `supplier_email`, `supplier_street`, `supplier_city`, `supplier_state`, `supplier_zipcode`, `supplier_region`, `supplier_phone`, `supplier_fax`, `supplier_fiscal_code`, `supplier_regno`, `supplier_tax_id`, `supplier_manager`, `supplier_bank`, `supplier_account`, `supplier_credits`, `supplier_debits`, `supplier_rating`, `supplier_status`, `supplier_date`, `supplier_time`, `supplier_entry_user`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <25; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
