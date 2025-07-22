package hms.store.database;

import hms.main.MSDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class StockUpdateHospitalConnection extends MSDBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public StockUpdateHospitalConnection() {

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
	public  ResultSet retrieveBatchName(String supplier_id) {
		String query = "SELECT * from batch_tracking where id='"+supplier_id+"' ";
		try {
			rs = statement.executeQuery(query);

		//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void resetStock(String item_id) throws Exception
	  {
		statement.executeUpdate("UPDATE  `batch_tracking` SET  `item_stock` =  '0' WHERE  `item_id` =  "+item_id);
	  }
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "UPDATE `items_detail` SET `item_name`=?, `item_desc`=?, `item_brand`=?, `item_salt_name`=?, `item_treatment`=?, `item_lab`=?, `item_surgery`=?, `item_drug`=?, `item_imaging`=?, `item_inpatient`=?, `item_medic_income`=?, `item_category`=?, `item_meas_unit`=?, `item_type`=?, `item_tax_type`=?, `item_tax_value`=?, `item_purchase_price`=?, `item_selling_price`=?, `item_total_stock`=?, `item_mrp`=?, `item_minimum_units`=?, `item_status`=?, `item_expiry_date`=?, `item_date`=?, `item_time`=?, `item_entry_user`=?, `item_location`=? WHERE `item_id` = "+data[27]+"";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <28; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void subtractStock(String itemID,String value) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`-'"+value+"' WHERE `item_id`="+itemID);
	  }
//	public void SubtractStockBatch(String batchID,String value) throws Exception
//	  {
//		
//		statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`-'"+value+"' WHERE `id`="+batchID);
//	  }
	
	public void addStock(String itemID,String value,String expiryDate) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"+value+"', `item_expiry_date`='"+expiryDate+"' WHERE `item_id`="+itemID);
	  }
	public void subtractStock(String batchID,String value,String date,String user) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`-'"+value+"',`item_last_used`='"+date+"',`item_last_user`='"+user+"' WHERE `id`="+batchID);
	  }
	public void returnStock(String batchID,String value) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`+'"+value+"' WHERE `id`="+batchID);
	  }
	public ResultSet retrieveItemDetail(String id)
	{
	  String query="SELECT `item_name`, `item_desc`, `item_brand`, `item_salt_name`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`, `item_meas_unit`, `item_type`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user` FROM `items_detail` WHERE `item_id` = '"+id+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievetBatchTotalStock(String index)
	{
	  String query="SELECT  `id`, `item_batch`, `item_stock`, `item_expiry`, `item_last_used`, `item_last_user` FROM `batch_tracking` WHERE `item_id`='"+index+"' AND `item_stock`>'0'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievetAllItems()
	{
	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievetAllItemsExcel()
	{
	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_salt_name`,`item_mrp`, `item_purchase_price`, `item_prevouse_price`,CASE WHEN `item_purchase_price`*`hms_detail`.`detail_desc` > `item_mrp` AND  `item_mrp`!='N/A'  THEN `item_mrp`  ELSE `item_purchase_price`*`hms_detail`.`detail_desc` END AS Value, `item_total_stock`,`item_minimum_units`, `item_status`, `item_expiry_date`, `item_entry_user` FROM `items_detail`,`hms_detail` WHERE `hms_detail`.`detail_id`=2 ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemBatch(String index)
	{
	  String query="SELECT  `id`,`item_batch` FROM `batch_tracking` WHERE `item_id`='"+index+"' AND `item_stock`>'0' ORDER BY `item_expiry` ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemBatch(String index,String batchName)
	{
	  String query="SELECT  `id`,`item_batch` FROM `batch_tracking` WHERE `item_batch` LIKE '%"+batchName+"%' AND `item_id`='"+index+"' AND `item_stock`>'0' ORDER BY `item_expiry` ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemBatchDetail(String index)
	{
	  String query="SELECT `item_stock`,`item_expiry` FROM `batch_tracking` WHERE `id`= "+index+"";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void deleteItemBatches(String item_id) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `batch_tracking` WHERE `item_id`=?");
		preparedStatement.setString(1, item_id);
		preparedStatement.executeUpdate();
	}
//	public void inserData(String[] data) throws Exception
//	  {
//		  String insertSQL = "INSERT INTO `batch_tracking`( `item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_used`) VALUES (?,?,?,?,?,?,?,?,?,?)";
//		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
//		  for (int i = 1; i <11; i++) {
//				  preparedStatement.setString(i, data[i-1]);
//			}
//		  preparedStatement.executeUpdate();
//	  }
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `batch_tracking`( `item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_used`,`item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_unit_price_new`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <19; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void SubtractStockBatch(String batchID, String value) {
		// TODO Auto-generated method stub
		try {
			statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`-'"+value+"' WHERE `id`="+batchID);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
