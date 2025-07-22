package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class ReturnInvoiceDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ReturnInvoiceDBConnection() {

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
		String insertSQL     = "";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <9; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveReturenedGood(String supplierID) {
		String query = "SELECT `return_invoice_item_id`, F.`return_invoice_id2`,`return_invoice_item_name`, `item_batch_name`, `item_qty`, `return_invoice_value`, `item_expiry_date` ,`item_date` FROM `return_invoice_items` T INNER JOIN `return_invoice_entry` F ON T.`return_invoice_id` = F.`return_invoice_id` AND  F.`return_invoice_supplier_id` = '"
				+ supplierID + "' AND `item_text6` ='null'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveReturenedGoodByid(String id) {
		String query = "SELECT `item_id`, `return_invoice_item_name`, `return_invoice_item_desc`,`item_qty`,`item_unit_price`,`item_discount`,`item_tax`,`surcharge_percentage`,`taxValue`,`surcharge_value`,`return_invoice_value`,`item_expiry_date`,`item_batch_name` FROM `return_invoice_items` WHERE `return_invoice_item_id`="+id+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
//	public void inserInvoiceItem(String[] data) throws Exception
//	  {
//		  String insertSQL = "INSERT INTO `return_invoice_items`( `item_id`, `return_invoice_item_name`, `return_invoice_item_desc`, `return_invoice_id`, `item_meas_units`, `item_qty`, `item_unit_price`, `item_discount`, `item_tax`, `return_invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
//		  for (int i = 1; i <15; i++) {
//			
//				  preparedStatement.setString(i, data[i-1]);
//			}
//		  preparedStatement.executeUpdate();
//	  }
	
	public void inserInvoiceItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `return_invoice_items`( `item_id`, `return_invoice_item_name`, `return_invoice_item_desc`, `return_invoice_id`, `item_batch_id`, `item_batch_name`,`item_qty`, `item_unit_price`, `item_discount`, `item_tax`,`taxValue`, `surcharge_percentage`, `surcharge_value`, `return_invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`,`item_meas_units`,`cutting_qty`,`damage_qty`,`total_qty`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <23; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public int inserreturn_invoice(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `return_invoice_entry`(`return_invoice_id2`, `return_invoice_supplier_id`, `return_invoice_supplier_name`, `return_invoice_date`, `return_invoice_time`, `return_invoice_entry_user`, `return_invoice_total_amount`, `return_invoice_tax`, `return_invoice_discount`, `return_invoice_final_amount`) VALUES (?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 11; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
}
