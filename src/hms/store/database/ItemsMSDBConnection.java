package hms.store.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;
import hms.main.MSDBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.JOptionPane;

public class ItemsMSDBConnection extends MSDBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	String fromDateSTR1,toDateSTR1;
	String fromDateSTR3,toDateSTR3;

	public ItemsMSDBConnection() {

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
	
	public String retrieveItemCategory(String item_id) {
		String query = "SELECT `item_category` FROM `items_detail` WHERE `item_id`="+item_id;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String item_category="";
		try {
			while (rs.next()) {
				item_category=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item_category;
	}
public int insertPillsRequest(String[] data, int newInvoiceId) throws Exception {
	    

	    // Step 2: Insert the new row with the incremented invoice_id
	    String insertSQL = "INSERT INTO medicalstore_db.request_pills_register "
	            + "(dept_id, dept_name, ipd_id, p_id, p_name, doctor_id, doctor_name, user_id, user_name, request_item_desc, request_qty, invoice_id) "
	            + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";

	    // Prepare the insert statement with the new invoice_id
	    PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
	    
	    // Set the values for the insert statement
	    preparedStatement.setInt(12, newInvoiceId);  // Set the incremented invoice_id
	    for (int i = 1; i < 12; i++) {
	        preparedStatement.setString(i, data[i - 1]);  // Set each value from the data array
	    }

	    // Execute the insert statement
	    preparedStatement.executeUpdate();

	    // Return the new invoice_id (for any further use)
	    return newInvoiceId;
	}
public int retreiveInvoiceID() throws SQLException {
	// Step 1: Retrieve the last invoice_id
	
    String selectSQL = "SELECT MAX(invoice_id) AS last_invoice_id FROM medicalstore_db.request_pills_register";
    PreparedStatement selectStmt = connection.prepareStatement(selectSQL);
    ResultSet rs = selectStmt.executeQuery();
    
    int lastInvoiceId = 0;
    if (rs.next()) {
        lastInvoiceId = rs.getInt("last_invoice_id");
    }

    // Increment the last_invoice_id by 1 for the new row
    int newInvoiceId = lastInvoiceId + 1;
	return newInvoiceId;
}
	public ResultSet retrieveStockbt(String itemid, String departmentName,String batch_id) {
		String query = "select SUM(total_stock), expiry_date from department_stock_register dsr  where department_name = '"+departmentName+"' and item_id = "+itemid+" and batch_id = '"+batch_id+"' group by batch_id HAVING (batch_id<>'' or batch_id<>'0') ORDER BY CAST(expiry_date AS UNSIGNED ), CAST( total_stock AS UNSIGNED ) ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public String retrieveItemSubCategory(String item_id) {
		String query = "SELECT `item_category_name` FROM `items_detail` WHERE `item_id`="+item_id;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String item_category="";
		try {
			while (rs.next()) {
				item_category=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item_category;
	}
	public int inserDepartmentStock(String[] data) throws Exception {
	    int rowsAffected = 0;
	    PreparedStatement preparedStatement = null;

	    try {
	        // Assuming this method exists in your DB connection class and provides a valid connection
	        String sql = "INSERT INTO department_stock_register (department_id, department_name, user_name, item_id, item_name, item_desc, total_stock, entry_date, expiry_date, batch_id, batch_name, is_consumable, user_id, med_source, dept_user_name) " +
	                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	        // Prepare the SQL statement
	        preparedStatement = connection.prepareStatement(sql);

	        // Set values from data array to the SQL query
	        preparedStatement.setString(1, data[0]);  // department_id (departmentID)
	        preparedStatement.setString(2, data[1]);  // department_name (departmentNameSTR)
	        preparedStatement.setString(3, data[2]);  // user_name (personTF or whoever this is)
	        preparedStatement.setString(4, data[3]);  // item_id (itemIDV)
	        preparedStatement.setString(5, data[4]);  // item_name (itemNameV)
	        preparedStatement.setString(6, data[5]);  // item_desc (itemDescV), should be String, if not adjust
	        preparedStatement.setString(7, data[6]);  // total_stock (issuedQtyV), should be int
	        preparedStatement.setString(8, data[7]);  // entry_date (issuedDateSTR)
	        preparedStatement.setString(9, data[8]);  // expiry (expiryDateV or itemBatchIDV)
	        preparedStatement.setString(10, data[9]); // batch_id (itemBatchIDV or itemBatch)
	        preparedStatement.setString(11, data[10]); // batch_name (itemBatchIDV or itemBatch)
	        preparedStatement.setString(12, data[11]); // batch_name (itemBatchIDV or itemBatch)
	        preparedStatement.setString(13, data[12]);
	        preparedStatement.setString(14, data[13]);
	        preparedStatement.setString(15, data[14]);

	        // Execute the insert statement and return the number of affected rows
	        rowsAffected = preparedStatement.executeUpdate();
	        
	    } catch (SQLException e) {
	        e.printStackTrace();
	        throw new Exception("Error inserting department stock", e);
	    } finally {
	        // Clean up database resources (Only close if you opened the connection)
	        if (preparedStatement != null) {
	            preparedStatement.close();
	        }
	        // Do not close connection if it's managed elsewhere
	        // if (connection != null) {
	        //    connection.close();
	        // }
	    }

	    return rowsAffected;
	}
	public int inserBillEntry(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `bills_entry`(`opd_no`, `patient_name`, `mobile_no`, `doctor_name`, `insurance_type`,`insurance_no`, `payable`, `total_amount`,`total_roundoff_amount`, `tax`, `discount`, `tax_amount`,`total_surcharge_value`, `date`, `time`, `user_id`, `user_name`,`bill_text1`,`bill_text2`,`payment_mode`,`bill_type`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println("hlo"+insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	
	public int insertBillingItems(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `bill_items`( `bill_id`, `patient_name`, `doctor_name`, `insurance_type`, `payable`, `item_id`, `item_name`, `item_desc`,`item_hsn_code`, `item_batch_id`, `item_batch`, `unit_price`, `quantity`, `tax_percentage`, `discount`, `tax_amount`, `total_value`, `surchargePercentage`, `surchargeValue`,`expiry_date`, `date`, `time`, `user_id`, `user_name`,`mrp`,`pack_size`,`item_risk_type`,`karuna_discount`,`new_unit_price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 30; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	
	public String retrieveFormula1() {
		String query = "SELECT `detail_desc` FROM `hms_detail` WHERE `detail_id`=2";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String version="";
		try {
			while (rs.next()) {
				version=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return version;
	}
	
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "UPDATE `items_detail` SET `item_name`=?,`item_code`=?, `item_desc`=?, `item_brand`=?, `item_hsn_code`=?, `item_treatment`=?, `item_lab`=?, `item_surgery`=?, `item_drug`=?, `item_imaging`=?, `item_inpatient`=?, `item_medic_income`=?, `item_category`=?, `item_category_name`=?, `item_meas_unit`=?, `item_type`=?, `item_tax_type`=?, `item_tax_value`=?, `item_surcharge`=?, `item_igst`=?,`item_purchase_price`=?, `item_selling_price`=?, `item_total_stock`=?, `item_mrp`=?, `item_minimum_units`=?, `item_status`=?, `item_expiry_date`=?, `item_date`=?, `item_time`=?, `item_entry_user`=?, `item_location`=?,`item_maximum_units`=?,`item_reoder_level`=?,`item_salt_name`=?,`item_text1`=?, `item_text2`=?, `item_text3`=?, `item_text4`=?,`item_risk_type`=?,`item_category_type`=?,`doctor_refrence`=?  WHERE `item_id` = "+data[41]+"";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <42; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public ResultSet retrieveDataNew(String index)
	//to retrieve tax values from database...
	{
		String query="SELECT `id`,`billing_cutting_chrg` from `cancel_restock_master_1` where `type`='"+index+"' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public  ResultSet getBatchName(String item_id,String dept_id) {

		String query = "select batch_name, batch_id from department_stock_register dsr  where department_id = "+dept_id+" and item_id = "+item_id+" group by batch_id HAVING (batch_id<>'' or batch_id<>'0') ORDER BY CAST(expiry_date AS UNSIGNED ), CAST( total_stock AS UNSIGNED ) ASC";

		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
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
	public void subtractStock(String batchID,String value,String date,String user) throws Exception
	{

		statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`-'"+value+"',`item_last_used`='"+date+"',`item_last_user`='"+user+"' WHERE `id`='"+batchID+"'");
	}
	
	public void subtractStockDepartment(String itemID, String value, String departmentName, String batch_id)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`=`total_stock`-'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"' group by batch_id");

	}
	public ResultSet getalloweditem(String itemid) {
		String query = "SELECT `item_id` FROM `items_detail` WHERE `hms_id`= "+itemid+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getalloweditem1(String itemid) {
		String query = "SELECT `hms_id` FROM `items_detail` WHERE `item_id`= "+itemid+"";
		System.out.println("ffffffffff"+query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void resetStock(String item_id) throws Exception {
		statement
				.executeUpdate("UPDATE  `items_detail` SET  `item_total_stock` =  '0' WHERE  `item_id` =  "
						+ item_id);
	}
	public ResultSet retrievetAllBatches(String item_id) {
		String query = "SELECT `item_batch`, `item_qauntity_entered`, `item_stock`, `item_date`, `item_mrp`, `item_measunits`, `item_unit_price_new`,`item_unitprice` FROM `batch_tracking` WHERE `item_id`='"
				+ item_id + "' order by id desc";
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
	 
			String query = "SELECT  `id`,`item_batch` FROM `batch_tracking` WHERE `item_id`='" + index
					+ "' AND item_stock >0 ORDER BY CAST(item_expiry AS UNSIGNED ), CAST( item_stock AS UNSIGNED ) ASC";
			System.out.println(query + "");
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
		
		return rs;
	}
	public ResultSet retrieveStock(String itemid) {
		String query = "SELECT  `item_total_stock`, `item_expiry_date` FROM `items_detail` WHERE `item_id`='"
				+ itemid + "' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet searchItem(String itemid) {

			String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_meas_unit` FROM `items_detail` where item_id LIKE '%"
					+ itemid + "%' OR item_name LIKE '%" + itemid + "%' OR item_desc LIKE '%" + itemid
					+ "%'  OR item_salt_name LIKE '%" + itemid + "%'";
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
			} 

			return rs;
		}
	public void subtractStock_outdorr(String itemID, String value)
			throws Exception {
String query="UPDATE `items_detail`  SET `item_total_stock`=`item_total_stock`-'"
						+ value
						+ "' WHERE `item_id`="
						+ itemID;
		System.out.println(query);
      statement.executeUpdate(query);

	}
	public void inserDataBatch(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `batch_tracking`( `item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_used`,`item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_unit_price_new`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <19; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
//	public void updateOrderStaus(String item_id,String status) throws Exception
//	  {
//		statement.executeUpdate("update `items_detail` set `item_oredered` = '"+status+"' where `item_id` = "+item_id);
//	  }
	public void updateOrderStaus(String item_id,String status,String items_order) throws Exception
	  {		
			
		if(status.equals("YES"))
		{
		String updatesql="update `items_detail` set `item_oredered` = '"+status+"',`item_order_qty`=item_order_qty+'"+items_order+"' where `item_id` = "+item_id+"";	
//		System.out.println(sql);	
		statement.executeUpdate(updatesql);

		}
		else{
			 String query="SELECT * FROM `items_detail` WHERE `item_id`='"+item_id+"'";
				System.out.println(query);
				try {
					rs = statement.executeQuery(query);
				} catch (SQLException sqle) {
					JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
				}
				 int item_ordered=0 ;
				while(rs.next()){
					  item_ordered = Integer.parseInt(rs.getString("item_order_qty"));
				}
				System.out.println("item_order"+item_ordered);
				int check=  item_ordered -Integer.parseInt(items_order);
				System.out.println("check"+check);
				if(check<0){
					statement.executeUpdate("update `items_detail` set `item_oredered` = '"+status+"',`item_order_qty`='0' where `item_id` = "+item_id+" AND `item_order_qty`>0");	
				}else{
					statement.executeUpdate("update `items_detail` set `item_oredered` = '"+status+"',`item_order_qty`=item_order_qty-'"+items_order+"' where `item_id` = "+item_id+" AND `item_order_qty`>0");	
				}
			
//
		}
	  }
	public void subtractStockBatch(String batchID,String value,String date,String user) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `batch_tracking` SET `item_stock`=`item_stock`-'"+value+"',`item_last_used`='"+date+"',`item_last_user`='"+user+"' WHERE `id`="+batchID);
	  }
	public void updateDataCounter(String docID,String counter) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `doc_text2`='"+counter+"' where `doc_id` = "+docID);
	  }
	public void updateprice(String itemID,String purchase,String previouse) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_purchase_price`='"+purchase+"', `item_prevouse_price`='"+previouse+"' WHERE `item_id`="+itemID);
	  }
	public void mrpupdateprice(String itemID,String mrpprice) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_mrp`='"+mrpprice+"' WHERE `item_id`="+itemID);
	  }
//	public void addStock(String itemID,String value,String expiryDate) throws Exception
//	  {
//		
//		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"+value+"', `item_expiry_date`='"+expiryDate+"' WHERE `item_id`="+itemID);
//	  }
//	
//	
	public void addStock(String itemID,String itemID1, String value) throws Exception {
		System.out.print("UPDATE `items_detail` SET  `item_total_stock`=`item_total_stock`+'" + value
				+ "',`hms_id`='"+itemID+"' WHERE `item_id`="+itemID1+"");
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'" + value
				+ "',`hms_id`='"+itemID+"' WHERE `item_id`="
				+ itemID1); 
		
		//update mrp on 13jan 2021(Happy Lohri)
//		statement.executeUpdate("UPDATE `items_detail` SET  `item_purchase_price`='" + unitprice
//				+ "',`item_total_stock`=`item_total_stock`+'" + value
//				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
//				+ itemID);
	}

	public void updateStock(String itemID,String value,String expiryDate) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`='"+value+"', `item_expiry_date`='"+expiryDate+"' WHERE `item_id`="+itemID);
	  }
	public void addStockByReturn1(String itemID,String value) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"+value+"' WHERE `item_id`="+itemID);
	  }
	public void subtractStock(String itemID,String value) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`-'"+value+"' WHERE `item_id`="+itemID);
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
	
	
	
	
	public ResultSet retrieveMainCategories()
	{
	  String query="SELECT DISTINCT(`item_category`) FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveSubCategories()
	{
	  String query="SELECT DISTINCT(`item_category_name`) FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData()
	{
	  String query="SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievePurcahseOrder()
	{
	  String query="SELECT `item_id` AS 'Item ID', `item_code` AS 'Item Code', `item_name` AS 'Item Name', `item_desc` AS 'Item Desc',`item_purchase_price` AS 'Purchase Price',`item_total_stock` AS 'Current Stock',`item_minimum_units` AS 'Min. Qty Level',(COALESCE((SELECT SUM(`item_qty`) FROM `store_pills_register` WHERE  `date` >= ( CURDATE() - INTERVAL 15 DAY ) AND `item_id`=I.`item_id` GROUP BY `item_id`),0)+COALESCE((SELECT SUM(`item_qty`) FROM `dept_pills_register` WHERE  `date` >= ( CURDATE() - INTERVAL 15 DAY ) AND `item_id`=I.`item_id` GROUP BY `item_id`),0)) AS 'To Be Order Qty',`item_text1` as 'Vendor 1', `item_text2` as 'Vendor 2', `item_text3` as 'Vendor 3', `item_text4` as 'Vendor 4' FROM `items_detail` I WHERE `item_total_stock`<=`item_minimum_units`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievePurcahseOrder2()
	{
	  String query="SELECT I.`item_id` AS 'ITEM CODE',I.`item_name` AS 'ITEM NAME', I.`item_desc` AS 'DESC', I.`item_brand` AS 'BRAND', I.`item_salt_name` AS 'SALT',I.`item_category_name` AS 'Item Category', I.`item_meas_unit` AS 'PACK SIZE', I.`item_mrp` AS 'MRP', ROUND((I.`item_mrp`/I.`item_meas_unit`),3) AS 'MRP/Unit', I.`item_purchase_price` AS 'PURCHASE PRICE',I.`item_total_stock` AS 'CURRENT STOCK', ROUND(SUM(B.`item_qty`),0) AS 'Two Months Qty', ROUND(SUM(B.`item_qty`)/2,0) AS 'One Month Qty',IF ((ROUND(SUM(B.`item_qty`)/2,0)-I.`item_total_stock`)>0 ,ROUND(SUM(B.`item_qty`)/4,0) , 'NO ORDER') AS 'ORDER STATUS' ,I.`item_text1` as 'Vendor 1', I.`item_text2` as 'Vendor 2', I.`item_text3` as 'Vendor 3', I.`item_text4` as 'Vendor 4' FROM `pills_register` B JOIN `items_detail` I ON B.`item_id`=I.`item_id` WHERE  B.`date` BETWEEN (NOW() - INTERVAL 2 MONTH) AND  NOW() AND I.`item_oredered`='NO'  GROUP BY B.`item_id` ORDER BY I.`item_id` ASC";
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
	  String query="SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_active` FROM `items_detail` WHERE doc_id LIKE '%"+index+"%' OR doc_name LIKE '%"+index+"%'";
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
	  String query="SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_active` FROM `items_detail` WHERE 1";
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
	  String query="SELECT `doc_id`, `doc_specialization`, `doc_opdroom` FROM `items_detail` WHERE `doc_name` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDataWithID(String docID)
	{
	  String query="SELECT `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_qualification` FROM `items_detail` WHERE `doc_id` = "+docID+"";
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
	  String query="SELECT * FROM `items_detail` WHERE `doc_username` = '"+name+"' AND `doc_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveItemDetail(String id)
	{
	  String query="SELECT `item_name`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`, `item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_location`,`item_igst`,`item_code`,`item_maximum_units`,`item_reoder_level`,`item_text1`, `item_text2`, `item_text3`, `item_text4`,`item_salt_name`,`item_risk_type`,`item_category_type`,`doctor_refrence` FROM `items_detail` WHERE `item_id` = '"+id+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievetTotalStock()
	{
	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_total_stock`, `item_minimum_units`, `item_expiry_date`,(CASE WHEN CONVERT(`item_total_stock`,DECIMAL) >=CONVERT(`item_minimum_units`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `items_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievetTotalStock1()
	{
		
		fromDateSTR1=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-1));
		toDateSTR1=DateFormatChange.StringToMysqlDate(new Date());
		fromDateSTR3=DateFormatChange.StringToMysqlDate(DateFormatChange.addMonths(new Date(),-3));
		toDateSTR3=DateFormatChange.StringToMysqlDate(new Date());
	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_total_stock`,  round(((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDateSTR3 + "' AND '" + toDateSTR3 + "'))/6,0) as 'Reorder Level', `item_total_stock`- round(((select COALESCE(sum(b.quantity),0) from `bill_items` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDateSTR3 + "' AND '" + toDateSTR3 + "'))/6,0), (SELECT Min(`item_expiry`) as Min_date  FROM `batch_tracking` b WHERE  a.`item_id`=b.`item_id`) ,(CASE WHEN CONVERT(`item_total_stock`,DECIMAL) >=CONVERT(`item_minimum_units`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `items_detail` a WHERE 1";
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
	  String query="SELECT `item_id`, `item_name`, `doctor_refrence`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit`,`item_risk_type`,`item_mrp` FROM `items_detail` WHERE 1";
//	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit`,`item_mrp`,`item_risk_type` FROM `items_detail` WHERE 1";
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
//	public ResultSet searchItemWithIdOrNmae(String index)
//	{
//
//		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_meas_unit` FROM `items_detail` where item_id LIKE '%"
//				+ index + "%' OR item_name LIKE '%" + index + "%' OR item_desc LIKE '%" + index
//				+ "%'  OR item_salt_name LIKE '%" + index + "%'";
//		System.out.println(query);
//		try {
//			rs = statement.executeQuery(query);
//		} catch (SQLException sqle) {
//			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
//		} 
//
//		return rs;
//	}
	public ResultSet searchItemWithIdOrNmae(String index)
	{
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_meas_unit` FROM `items_detail` where item_id LIKE '%"
				+ index + "%' OR item_name LIKE '%" + index + "%' OR item_desc LIKE '%" + index
				+ "%'  OR item_salt_name LIKE '%" + index + "%'";
	System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchItemWithIdNew(String index)
	{
	  String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price` FROM `items_detail` where item_id="+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchItemWithIdOrNmaeOrCode(String index)
	{
	  String query="SELECT `item_id`,`item_code`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` FROM `items_detail` where item_id LIKE '%"+index+"%' OR `item_code` LIKE '%"+index+"%' OR item_name LIKE '%"+index+"%' OR item_desc LIKE '%"+index+"%'  OR item_salt_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemDetail(String index)
	{
	  String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`, `item_surcharge`,`item_igst`,`item_location`,`item_oredered`,`stock_type` FROM `items_detail` where  `item_id` = '"+index+"'";
		System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemDetail2(String index)
	{
		String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_location`, `item_surcharge`,`item_hsn_code`,`formula_active`,`item_risk_type` FROM `items_detail` where  `item_id` = '"+index+"' and item_total_stock>0";
		System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemDetailMS(String index) {
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_location`, `item_surcharge`,`item_hsn_code`, `item_meas_unit`,`item_med_type`,`formula_active`,`item_category`,`changed_unit_price` FROM `items_detail` where  `item_id` = '"
				+ index + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet itemDetailBatch2(String index) {
		String query = "SELECT `item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_stock`,`item_desc`,`item_expiry` FROM `batch_tracking` where  `id` = '"
				+ index + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet itemDetail3(String index)
	{
	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_location`,`stock_type` FROM `items_detail` WHERE `item_id`= '"+index+"'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM items_detail WHERE `item_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `items_detail`(`item_name`,`item_code`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_location`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`,`item_igst`,`item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`,`item_maximum_units`,`item_reoder_level`,`item_salt_name`,`item_text1`, `item_text2`, `item_text3`, `item_text4`,`item_risk_type`,`item_category_type`,`doctor_refrence`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <42; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public int inserDataBatchNew(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `batch_tracking`( `item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_used`,`item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_unit_price_new`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <19; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  System.out.println(preparedStatement+"");
		  preparedStatement.executeUpdate();
			 ResultSet rs = preparedStatement.getGeneratedKeys();
			  rs.next();
			 
			 return  rs.getInt(1);
//		  preparedStatement.executeUpdate();
	  }
}