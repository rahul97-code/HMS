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

public class IssuedItemsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public IssuedItemsDBConnection() {

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
	public void updateStatus(String ipd_id) throws Exception
	  {
		System.out.println("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING' AND `text2`='n/a' AND `ipd_or_misc_number`='"+ipd_id+"'");
		statement.executeUpdate("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING'  AND `text2`='n/a' AND `ipd_or_misc_number`='"+ipd_id+"'");
	  }
	public void updateProcedureStatus(String ipd_id) throws Exception
	  {
		System.out.println("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING' AND `text2`<>'n/a' AND `ipd_or_misc_number`='"+ipd_id+"'");
		statement.executeUpdate("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING' AND `text2`<>'n/a' AND `ipd_or_misc_number`='"+ipd_id+"'");
	  }
	public void deleteRequest(String ipd_id,String type) throws Exception
	  {
		String query="DELETE from store_pills_register spr WHERE ipd_or_misc_number ='"+ipd_id+"' and text1 ='PENDING' and text2 ='"+type+"'";
		statement.executeUpdate(query);
	  }
	public void updateItemStatus(String ipd_id,String item_id) throws Exception
	  {
		System.out.println("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING' AND `ipd_or_misc_number`='"+ipd_id+"' AND `item_id`='"+item_id+"'");
		statement.executeUpdate("UPDATE `store_pills_register` SET `text1`='OK' WHERE `text1`='PENDING' AND `ipd_or_misc_number`='"+ipd_id+"' AND `item_id`='"+item_id+"'");
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
	public ResultSet retrieveAllCostCenterDoctor(String dateFrom, String dateTo,String doctor) { 
		String query = "SELECT D.`issued_id`, D.`department_id`, D.`department_name`, D.`person_name`, D.`persone_id`, D.`intent_slip_no`, D.`date`, D.`time`, D.`item_id`, D.`item_name`, D.`item_desc`, D.`issued_qty`,I.`item_mrp`, I.`item_purchase_price`, D.`previouse_stock`, D.`consumable`, D.`return_date`, D.`item_returned`, D.`expiry_date`, D.`issued_by`, D.`issued_text1` FROM `issued_department_register` D JOIN `items_detail` I ON D.`item_id`=I.`item_id` WHERE `date` BETWEEN '"+ dateFrom + "' AND '" + dateTo + "' AND `issued_text1`='"+doctor+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllPendingPills() {

		
		String query = "SELECT DISTINCT `ipd_or_misc_number`, `p_id`, `p_name`, `user_name`, `date`,`text2` FROM store_pills_register WHERE `text1`='PENDING' ORDER BY date DESC";
		System.out.println(query);
		
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllPendingPillsDetail(String ipd_id) {

		
		String query = "SELECT `item_id`, `item_name`, `item_desc`,  `item_qty`, `item_price`,`total_price`,`text2`,`item_risk_type`,`item_mrp`,`item_meas_unit`,`batch_name`,`batch_id`,`text6` FROM store_pills_register WHERE `text1`='PENDING' AND `text2`='n/a' AND `ipd_or_misc_number`='"+ipd_id+"'  GROUP BY `item_id`,`batch_id` ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
public ResultSet retrieveAllProcedurePillsDetail(String ipd_id) {

		
		String query = "SELECT `item_id`, `item_name`, `item_desc`,  `item_qty`, `item_price`,`total_price`,`text2`,`item_risk_type`,`item_mrp`,`item_meas_unit`,`batch_name`,`batch_id`,`text6` FROM store_pills_register WHERE `text1`='PENDING' AND `text2`<>'n/a' AND `ipd_or_misc_number`='"+ipd_id+"'  GROUP BY `item_id` ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedRegister1(String deptId,String tax,String fromDate,String toDate) {
//		String query1= "SELECT idr.`intent_slip_no` , idr.`person_name` , idr.`issued_by` , idr.`item_name` , idr.`item_returned`, idr.`expiry_date` , idr.`issued_qty` , case when dsr.`total_stock`='' THEN \"\""
//				+ " else dsr.`total_stock` end as previouse_stock , idr.`date` , idr.`time` FROM `issued_department_register` idr inner join department_stock_register dsr on idr.department_id = dsr.department_id "
//				+ "and idr.item_id = dsr.item_id and idr.item_returned = dsr.batch_name AND dsr.`last_issued` "
//				+ "BETWEEN '"+fromDate+"' AND '"+toDate+"' WHERE idr.`department_id` = '"+deptId+"' AND idr.`item_id` LIKE '%"+itemID+"%' AND idr.`date` BETWEEN '"+fromDate+"' AND '"+toDate+"'";
//		
	    String query="SELECT * ,round((t.purchase_price*t.qty),0) as total_amount from (SELECT idr.`intent_slip_no` , idr.`person_name` , idr.`issued_by` , idr.`item_name` , idr.`item_returned`, idr.`expiry_date` , idr.`issued_qty` as qty , "
	    		+ "( SELECT coalesce(sum(dsr.total_stock),0) from department_stock_register dsr WHERE dsr.item_id = idr.item_id and dsr.total_stock>0) as previouse_stock , idr.`date` , idr.`time`, id.item_mrp ,"
	    		+ " ROUND(id.changed_unit_price + "+tax+"/100*id.changed_unit_price,2) as purchase_price FROM `issued_department_register` idr join items_detail id on idr.item_id = id.item_id WHERE"
	    		+ " idr.`department_id` = '"+deptId+"' AND idr.`date` BETWEEN '"+fromDate+"' AND '"+toDate+"' ) t";	
	    System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedRegister(String deptId,String itemID,String fromDate,String toDate) {
		String query = "SELECT `intent_slip_no` , `person_name` , `issued_by` , `item_name` , `expiry_date` , `issued_qty` , `previouse_stock` , `date` , `time` FROM `issued_department_register` WHERE `department_id` = '"+deptId+"' AND `item_id` LIKE '"+itemID+"' AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllIssuedTOPatientRegister(String deptId,String fromDate,String toDate) {
		String query = "SELECT `entry_id` AS 'Entry ID', `doctor_id` AS 'Doctor ID/Exam ID', `doctor_name` AS 'Doctor Name/Exam Name', `p_id` AS 'Patient ID', `p_name` AS 'Patient Name', `item_id` AS 'Item ID', `item_name` AS 'Item Name', `item_desc` AS 'Item Desc', `item_price` AS 'Unit Price', `item_qty` AS 'Qty.', `total_price` AS 'Total Value', `date` AS 'Date', `time` AS 'Time', `ipd_or_misc_number` AS 'Slip Number', `enry_type` AS 'Entry Type',`user_id` AS 'User ID', `user_name` AS 'User Name' FROM `dept_pills_register` WHERE `dept_id` = '"+deptId+"' AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedRegister(String fromDate,String toDate) {
		String query = "SELECT `issued_id` AS 'Issue ID', `department_id` AS 'Dept. ID', `department_name` AS 'Dept. Name', `person_name` AS 'Issued TO', `intent_slip_no` AS 'Slip Number', `date` AS 'Date', `time` AS 'Time', `item_id` AS 'Item Id', `item_name` AS 'Item Name', `item_desc` AS 'Item Desc.', `issued_qty` AS 'Qty.', `previouse_stock` AS 'Previouse Stock', `item_returned` AS 'Item Batch', `expiry_date` AS 'Expiry Date', `issued_by` AS 'Issue By' FROM `issued_department_register` WHERE  `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievebtch_detail(String ipd_id,String item_id) {
		String query = " select `batch_name`,`batch_id` FROM `store_pills_register` WHERE `item_id`='"+item_id+"' and `ipd_or_misc_number`='"+ipd_id+"' limit 1";
		System.out.println(" select `batch_name`,`batch_id` FROM `store_pills_register` WHERE `item_id`='"+item_id+"' and `ipd_or_misc_number`='"+ipd_id+"' limit 1");
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedPatientPills(String fromDate,String toDate) {
		String query = "SELECT `entry_id` AS 'Entry ID', `doctor_id` AS 'Doctor ID', `doctor_name` AS 'Doctor Name', `user_id` AS 'User ID', `user_name` AS 'User Name', `p_id` AS 'Patien ID', `p_name` AS 'Patient Name', `item_id` AS 'Item ID', `item_name` AS 'Item Name',  `item_desc` AS 'Item Desc', `item_price` AS 'Item Price', `item_qty` AS 'Qty.',coalesce(`batch_name` , 'n/a') as 'Item Batch', `total_price` AS 'Total Value',  `date` AS 'Date', `time` AS 'Time', `ipd_or_misc_number` AS 'IPD Number', `enry_type` AS 'Entry Type' FROM `store_pills_register` WHERE `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		System.out.println( query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedPills(String fromDate,String toDate) {
		String query = "SELECT a.item_id,a.item_name,a.`item_brand`,a.`item_total_stock`,(select  COALESCE(sum(b.item_qty), 0 ) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Main Store',(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Departments',(select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Total' FROM `items_detail` a";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedPillsNew(String fromDate3,String toDate3,String fromDate1,String toDate1) {
		String query = "SELECT a.item_id,a.item_name,a.`item_brand`,a.`item_total_stock`,(select  COALESCE(sum(b.item_qty), 0 ) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "') as 'Last Month',(select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "') as 'Last Three Months', round(((select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/12,0) as 'Reorder Level', round(((select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/6,0) as 'Order Qty.' FROM `items_detail` a where ((select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate1 + "' AND '" + toDate1 + "'))>0 AND round(((select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate3 + "' AND '" + toDate3 + "'))/12,0)>=a.`item_total_stock`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllIssuedPillsOLD(String fromDate,String toDate) {
		String query = "SELECT a.item_id,a.item_name,a.`item_brand`,a.`item_total_stock`,(select  COALESCE(sum(b.item_qty), 0 ) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Main Store',(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Departments',(select COALESCE(sum(b.item_qty),0) from `store_pills_register` b where b.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "')+(select COALESCE(sum(c.item_qty),0) from `dept_pills_register` c where c.item_id=a.item_id AND `date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "') as 'Total' FROM `items_detail` a";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int inserStorePillsRegister(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `store_pills_register`(`doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`, `text1`,`item_mrp`,`batch_id`,`batch_name`,`text6`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	public int inserStorePillsRegisterindoor(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `store_pills_register`(`doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`, `text1`,`item_mrp`,`batch_id`,`batch_name`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 21; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	public int inserStorePillsRegisterNewWIthMRP(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `store_pills_register`(`doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`, `text1`, `text2`,`item_risk_type`,`item_mrp`,`item_meas_unit`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
	public ResultSet retrieveTopItems(String dateFrom, String dateTo) {


		
		String query = "SELECT D.`item_id` 	AS 'ITEM ID', D.`item_name` AS 'ITEM NAME', D.`item_desc` AS 'ITEM DESC', SUM(D.`total_price`) AS 'TOTAL SALE' , SUM(D.`item_qty`) AS 'TOTAL QTY',I.`item_purchase_price` AS 'PURCHASE PRICE', I.`item_prevouse_price` AS 'PREVIOUSE PRICE', I.`item_total_stock` AS 'CURRENT STOCK' FROM `store_pills_register` D JOIN `items_detail` I ON D.`item_id`=I.`item_id`  WHERE `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND `text1`='OK' GROUP BY D.`item_id` ORDER BY SUM(D.`item_qty`) DESC LIMIT 20;";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePillsToPatients(String dateFrom, String dateTo,String type,String itemID) { 
		String query = "SELECT `item_price`,`item_qty`,`total_price` FROM `store_pills_register` WHERE `item_id`='"+itemID+"' AND `enry_type`='"+type+"' AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveIssues(String dateFrom, String dateTo,String dept,String itemID) { 
		String query = "SELECT `issued_qty` FROM `issued_department_register` WHERE `department_name`='"+dept+"' AND `item_id`='"+itemID+"' AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM issued_department_register WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserIssuedData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `issued_department_register`( `department_id`, `department_name`, `person_name`, `persone_id`, `intent_slip_no`, `date`, `time`, `item_id`, `item_name`, `item_desc`, `issued_qty`, `previouse_stock`, `consumable`, `return_date`, `item_returned`, `expiry_date`, `issued_by`,`issued_text1`,`item_unit_price`,`total_amount`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 21; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}
}
