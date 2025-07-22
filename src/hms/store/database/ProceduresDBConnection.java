package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;

import javax.swing.JOptionPane;

public class ProceduresDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ProceduresDBConnection() {
		super();
		connection = getConnection();
		statement = getStatement();
	}
	public void updateProcdure(String procedureID,String name,String amount,String preDays,String postDays) throws Exception
	  {
		statement.executeUpdate("UPDATE `procedures_detail` SET `procedure_name` ='"+name+"',`procedure_charges` ='"+amount+"',`procedure_pre_days` ='"+preDays+"',`procedure_post_days` ='"+postDays+"' WHERE `procedure_id`= "+procedureID+"");
	  }
	public ResultSet retrieveDataProcedure(String procedureID)
	{
		
	    String query="SELECT P.`item_id`, P.`item_name`,I.`item_desc`, P.`item_quantity`,P.`item_quantity`*I.`item_purchase_price`*(SELECT `detail_desc` FROM `hms_detail` WHERE `detail_id`=2) AS TOTAL,I.`item_purchase_price`*(SELECT `detail_desc` FROM `hms_detail` WHERE `detail_id`=2) AS PRICE,I.item_risk_type  FROM `procedures_items` P INNER JOIN `items_detail` I ON P.`item_id`=I.`item_id` WHERE `procedure_id`='"+procedureID+"'";
	    System.out.println(query);
	    try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void updateData1(Vector<String> itemID,Vector<String> issuedQtyV) throws Exception
	{
		for (int i = 0; i <itemID.size(); i++) {
		String insertSQL  = "UPDATE `procedures_items` SET `item_quantity`="+issuedQtyV.get(i)+"  WHERE `item_id` = "+itemID.get(i)+"";
System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
			preparedStatement.executeUpdate();
		}
		
	}
	public void deleteItem(String procedureID,String itemId) throws Exception { 
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `procedures_items` WHERE `procedure_id`=? AND `item_id`=?");
		preparedStatement.setString(1, procedureID);
		preparedStatement.setString(2, itemId);
		preparedStatement.executeUpdate();
	}
	public void deleteItem1(String procedureID,Vector<String> updateitemID) throws Exception {
		for (int i = 0; i <updateitemID.size(); i++) {
			String insertSQL  = "DELETE FROM `procedures_items` WHERE `procedure_id`="+procedureID+ " AND `item_id`="+updateitemID.get(i)+"";
	System.out.println(insertSQL);
			PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
				preparedStatement.executeUpdate();
				
		}
	}
	public void deleteProcedure(String procedureID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `procedures_detail` WHERE `procedure_id`=?");
		preparedStatement.setString(1, procedureID);
		preparedStatement.executeUpdate();
		deleteAllItem(procedureID);
	}
	public void deleteAllItem(String procedureID) throws Exception {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `procedures_items` WHERE `procedure_id`=?");
		preparedStatement.setString(1, procedureID);
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `procedure_id`, `procedure_name`, `procedure_charges`, `procedure_date`, `procedure_time`, `procedure_user_add`,`procedure_pre_days`, `procedure_post_days` FROM `procedures_detail` WHERE 1 order by `procedure_name` asc";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public int insertProcedureItemsData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `procedures_items`(`procedure_id`, `procedure_name`, `item_id`, `item_name`, `item_quantity`, `date`, `time`, `added_by`) VALUES  (?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 9; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}

	public int insertProcedureData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `procedures_detail`(`procedure_name`, `procedure_charges`,`procedure_pre_days`, `procedure_post_days`, `procedure_date`, `procedure_time`, `procedure_user_add`) VALUES (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 8; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
}
