package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class ChallanDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ChallanDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public String retrieveReturnIssuenceNUMBER() {
		String poNumber="";
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());

		try {
			ResultSet r = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM `challan_items_issuance_return` where type='ISSUE'");
			r.next();
			int count = r.getInt("rowcount");
			r.close();
			count++;
			System.out.println("MyTable has " + count + " row(s).");
			poNumber="IN-"+formattedDate+"-"+String.format("%04d", count); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return poNumber;
	}
	public String retrieveReturnChallanNUMBER() {
		String poNumber="";
		DateFormat df = new SimpleDateFormat("yy"); // Just the year, with 2 digits
		String formattedDate = df.format(Calendar.getInstance().getTime());

		try {
			ResultSet r = statement.executeQuery("SELECT COUNT(*) AS rowcount FROM `challan_return_entry` where type='RETURN'");
			r.next();
			int count = r.getInt("rowcount");
			r.close();
			count++;
			System.out.println("MyTable has " + count + " row(s).");
			poNumber="RC-"+formattedDate+"-"+String.format("%04d", count); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return poNumber;
	}
	public ResultSet retrievetAllItems()
	{
	  String query="SELECT `item_id`, `challan_item_name`,sum(`item_qty`)+sum(`item_free_quantity`) FROM `challan_items` group by `item_id` having sum(`item_qty`)+sum(`item_free_quantity`)>0  ";
//	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit`,`item_mrp`,`item_risk_type` FROM `items_detail` WHERE 1";
	  System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveChallanItemData(String ID) { 
		String query = "SELECT	`po`.`challan_item_name`,`po`.`item_qty`,`po`.`item_id`,`po`.`item_free_quantity`,`po`.`item_batch_number`  FROM `challan_items` `po` LEFT JOIN `items_detail` ON((`items_detail`.`item_id` = CONVERT(`po`.`item_id` USING utf8))) WHERE	`po`.`challan_id` ='"+ID+"'";
System.out.println(query);
		//		String query = "SELECT `entry_id` AS 'ID', `item_id` AS 'Item ID', `itme_name` AS 'Item Name', `item_desc` AS 'Item Desc', `item_brand` AS 'Item Brand', `item_salt` AS 'Item Salt', `item_pack_size` AS 'Pack Size', `item_mrp` AS 'MRP', `item_purchase_price` AS 'Purchase Price', `item_stock` AS 'Item Stock', `item_15_day` AS '15 Days Qty', `item_already_order` AS 'Already Ordered', `item_to_be_ordered` AS 'Auto Generate Qty', `item_indent_qty` AS 'Requested Qty', `item_preferred_vendor` AS 'Preferred Vendor' FROM `indent_items` WHERE  `item_preferred_vendor`='"+vendor_name+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void UpdateChallanReturnstock(String challan_id,String item_id,String qty) throws Exception
	  {
		statement.executeUpdate("UPDATE `challan_items` SET `item_free_quantity`=`item_free_quantity`-"+qty+" WHERE `challan_id`='"+challan_id+"' AND `item_id` ='"+item_id+"'");
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
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `challan_id`, `challan_id2`, `challan_supplier_name`, `challan_date`, `challan_time`,  `challan_total_amount`,`challan_status` FROM `challan_entry` WHERE  `challan_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `challan_id` DESC ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDatareturnChallan(String dateFrom, String dateTo) { 
		String query = "SELECT `challan_id`, `challan_id2`,`type`,`ref_no`, `challan_supplier_name`, `challan_date`, `challan_total_amount` FROM `challan_return_entry` WHERE  `challan_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `challan_id` DESC ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int inserChallanEntry(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `challan_entry`(`challan_id2`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount`) VALUES (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 8; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	public int inserChallanEntryReturn(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `challan_return_entry`(`challan_id2`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount`,`ref_no`,`type`) VALUES (?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 10; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
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

		public void updateChallanStatus(String challan_id,String payment_id) throws Exception
		  {
			statement.executeUpdate("UPDATE `challan_entry` SET `challan_text1`='"+payment_id+"' WHERE `challan_id`="+challan_id);
		  }
		public void updateChallanStatus1(String challan_id) throws Exception
		  {
			statement.executeUpdate("UPDATE `challan_items` SET `item_text6`='USED' WHERE `challan_item_id`="+challan_id);
		  }
		public ResultSet retrieveAllchallanItems(String fromDate,String toDate) {
			String query = "SELECT `challan_item_id`, `item_id`, `challan_item_name`, `challan_item_desc`, `challan_id`, `item_meas_units`, `item_qty`, `item_unit_price`, `item_discount`, `item_tax`, `challan_value`, `item_expiry_date`, `item_date`, `item_time` FROM `challan_items` WHERE `item_date` BETWEEN '"
					+ fromDate + "' AND '" + toDate + "'";
			try {
				rs = statement.executeQuery(query);

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveChallan(String search,String supplier_id) {
			String query = "SELECT `item_id`, `challan_item_name`, `challan_item_desc`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`challan_value`, `item_expiry_date`, `item_date`,`item_batch_number`,`challan_item_id` FROM `challan_items` WHERE `challan_supplier_id`='"+supplier_id+"' AND `challan_id`='"+search+"' AND `item_text6` ='null'";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveWithID(String id) {
			String query = "SELECT `challan_id`, `challan_id2`, `challan_order_no`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount` FROM `challan_entry` WHERE `challan_text1`='"+id+"'";
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveWithIDNew(String id) {
			String query = "SELECT `challan_id`, `challan_id2`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount` FROM `challan_entry` WHERE `challan_id`='"+id+"'";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveWithIDNewReturn(String id) {
			String query = "SELECT `challan_id`, `challan_id2`, `challan_supplier_id`, `challan_supplier_name`, `challan_date`, `challan_time`, `challan_entry_user`, `challan_total_amount`,`ref_no` FROM `challan_return_entry` WHERE `challan_id`='"+id+"'";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveItems(String po_id) {
			String query = "SELECT `challan_item_name`,(SELECT I.`item_hsn_code` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1),`item_qty`, `item_unit_price`,ROUND((`item_qty`*`item_unit_price`),2), `item_discount`,ROUND(((`item_qty`*`item_unit_price`)-`item_discount`), 2), `item_tax`,`item_tax_value`,`item_surcharge`, `item_surcharge_value` FROM `challan_items` WHERE `challan_id`='"+po_id+"'";
			
			System.out.println("HELLO : "+query);
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public ResultSet retrieveItemsReturn(String po_id) {
			String query = "SELECT `item_name`,(SELECT I.`item_hsn_code` FROM `items_detail` I WHERE I.`item_id`=`item_id` LIMIT 1),`item_qty`, `item_unit_price`,ROUND((`item_qty`*`item_unit_price`),2), ROUND(((`item_qty`*`item_unit_price`)), 2) FROM `challan_items_issuance_return` WHERE `return_challan_id`='"+po_id+"'";
			
			System.out.println("HELLO : "+query);
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
	public void inserChallanItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `challan_items`(  `challan_id`,`challan_supplier_id`,`challan_supplier_name`,`item_id`, `challan_item_name`, `challan_item_desc`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`challan_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <23; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
//		  ResultSet rs = preparedStatement.getGeneratedKeys();
//		  rs.next();
//		 
//		 return  rs.getInt(1);
	  }
	public void insertReturnChallanItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `challan_items_issuance_return`(  `challan_no`,`vendor_id`,`vendor_name_or_patientname`,`return_challan_date`, `entry_time`, `entry_username`, `entry_date`, `item_id`, `item_name`, `item_qty`, `item_unit_price`,`return_challan_no`,`return_challan_id`,`type`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <15; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
//		  ResultSet rs = preparedStatement.getGeneratedKeys();
//		  rs.next();
//		 
//		 return  rs.getInt(1);
	  }
	public void insertStockHistory(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `item_stock_history`(`challan_id`,`challan_no`,`vendor_id`,`vendor_name`, `entry_date`, `entry_time`,`entry_user`, `item_id`, `item_name`, `item_stock`) VALUES (?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
}
