
package hms.store.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

import javax.swing.JOptionPane;

public class ItemsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;
	String fromDateSTR1,toDateSTR1;
	String fromDateSTR3,toDateSTR3;

	public ItemsDBConnection() {

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
	public ResultSet getdata(String itemid) {
		String query = "SELECT `item_id`,`item_name`,`item_desc`,`item_code`,`item_hsn_code`,`item_brand`,`item_category_name`,`item_tax_value`,`item_surcharge`,`item_meas_unit`,`item_mrp`,`item_purchase_price` FROM `items_detail` WHERE `item_id`='"
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
	public void UpdateBatch(String data[],String id) throws Exception {
		
		System.out.print("UPDATE  `batch_tracking` SET  `item_batch`='"+data[0]+"' ,`item_stock`='"+data[1]+"', `item_expiry`='"+data[2]+"',`item_unit_price_new`='"+data[3]+"',`item_mrp`='"+data[4]+"',`item_measunits`='"+data[5]+"',`item_tax`='"+data[6]+"',`item_surcharge`='"+data[7]+"',`item_taxvalue`='"+data[8]+"',`item_surcharge_value`='"+data[9]+"',`item_text3`='"+data[10]+"',`changed_unit_price`='"+data[11]+"',`bill_date`='"+data[12]+"' WHERE  `id` =  "
				+ id);
		statement
		.executeUpdate("UPDATE  `batch_tracking` SET  `item_batch`='"+data[0]+"' ,`item_stock`='"+data[1]+"', `item_expiry`='"+data[2]+"',`item_unit_price_new`='"+data[3]+"',`item_mrp`='"+data[4]+"',`item_measunits`='"+data[5]+"',`item_tax`='"+data[6]+"',`item_surcharge`='"+data[7]+"',`item_taxvalue`='"+data[8]+"',`item_surcharge_value`='"+data[9]+"',`item_text3`='"+data[10]+"',`changed_unit_price`='"+data[11]+"',`bill_date`='"+data[12]+"' WHERE  `id` =  "
				+ id);
	}
	public ResultSet retrieveAllexam() {

		String query = "SELECT ins_name, from insurance_detail ";

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public ResultSet getReturnCharge() {

		String query = "SELECT return_charge_reqd from `items_detail` limit 1 ";

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public void UpdateReturnCharge(String index) {

		String query = "UPDATE `items_detail` SET return_charge_reqd='"+index+"'";

		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

	}
	
	public void updateData(String[] data) throws Exception
	{
		String insertSQL     = "UPDATE `items_detail` SET `item_name`=?,`item_code`=?, `item_desc`=?, `item_brand`=?, `item_hsn_code`=?, `item_treatment`=?, `item_lab`=?, `item_surgery`=?, `item_drug`=?, `item_imaging`=?, `item_inpatient`=?, `item_medic_income`=?, `item_category`=?, `item_category_name`=?, `item_meas_unit`=?, `item_type`=?, `item_tax_type`=?, `item_tax_value`=?, `item_surcharge`=?, `item_igst`=?,`item_purchase_price`=?, `item_selling_price`=?, `item_total_stock`=?, `item_mrp`=?, `item_minimum_units`=?, `item_status`=?, `item_expiry_date`=?, `item_date`=?, `item_time`=?, `item_entry_user`=?, `item_location`=?,`item_maximum_units`=?,`item_reoder_level`=?,`item_salt_name`=?,`item_text1`=?, `item_text2`=?, `item_text3`=?, `formula_active`=?,`item_risk_type`=?,`item_category_type`=?,`doctor_refrence`=?,`item_other`=?,`changed_unit_price`=? ,`is_price_change`=? WHERE `item_id` = "+data[44]+"";

		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i <45; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public void UpadateBatchStatus(int status,String itemID) throws Exception
	{
		String insertSQL = "UPDATE `items_detail` SET `batch_reqd`='"+status+"' WHERE `item_id` = "+itemID+"";
		statement.executeUpdate(insertSQL);
	}
	public void addStock(String itemID, String value) throws Exception {
		System.out.print("UPDATE `items_detail` SET  `item_total_stock`=`item_total_stock`+'" + value
				+ "' WHERE `item_id`="+itemID+"");
		statement.executeUpdate("UPDATE `items_detail` SET  `item_total_stock`=`item_total_stock`+'" + value
				+ "' WHERE `item_id`='"+itemID+"'"); 
	}
	public ResultSet retrievetAllItemsForCheck()
	{
		String query="SELECT bt.item_id , bt.item_name, SUM(case when bt.item_stock>0 then bt.item_stock else 0 end), id.item_total_stock from batch_tracking bt join items_detail id on id.item_id = bt.item_id GROUP by item_id order by cast(bt.item_id as UNSIGNED ) ";
		//	  String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit`,`item_mrp`,`item_risk_type` FROM `items_detail` WHERE 1";
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
		String query = "SELECT `item_text3`,`id`,`item_batch`, `item_qauntity_entered`, `item_stock`,`item_expiry`, `item_date`, `item_mrp`, `item_measunits`, `item_unit_price_new`,`item_unitprice`,`item_tax`,`item_surcharge`,`changed_unit_price`,`bill_date` FROM `batch_tracking` WHERE `item_id`='"
				+ item_id + "' order by id desc";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
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
	public void subtractStock_outdorr(String itemID, String value)
			throws Exception {
		String query="UPDATE `items_detail`  SET `item_total_stock`=`item_total_stock`-'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID;
		System.out.println(query);
		statement.executeUpdate(query);

	}
	public void AddStock(String itemID, String value)
			throws Exception {
		String query="UPDATE `items_detail`  SET `item_total_stock`=`item_total_stock`+'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID;
		System.out.println(query);
		statement.executeUpdate(query);

	}
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
	public void updateDataCounter(String docID,String counter) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `doc_text2`='"+counter+"' where `doc_id` = "+docID);
	}
	public void updateprice(String itemID,String purchase,String previouse) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `changed_unit_price`='"+purchase+"',`item_purchase_price`='"+purchase+"', `item_prevouse_price`='"+previouse+"' WHERE `item_id`="+itemID);
	}
	public void mrpupdateprice(String itemID,String mrpprice) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `item_mrp`='"+mrpprice+"' WHERE `item_id`="+itemID);
	}
	public void Updateunitprice(String itemID,String Uprice) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `changed_unit_price`='"+Uprice+"' WHERE `item_id`="+itemID);
	}	
	public void addStockFromChallan(String itemID, String value) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET  `item_total_stock`=`item_total_stock`+'" + value
				+ "' WHERE `item_id`="
				+ itemID); //update mrp on 13jan 2021(Happy Lohri)
		//		statement.executeUpdate("UPDATE `items_detail` SET  `item_purchase_price`='" + unitprice
		//				+ "',`item_total_stock`=`item_total_stock`+'" + value
		//				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
		//				+ itemID);
	}


	public void addStock(String itemID, String value, String expiryDate,
			String unitprice,String mrp) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET  `item_mrp`='"+mrp+"', `item_purchase_price`='" + unitprice
				+ "',`item_total_stock`=`item_total_stock`+'" + value
				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
				+ itemID); //update mrp on 13jan 2021(Happy Lohri)
		//		statement.executeUpdate("UPDATE `items_detail` SET  `item_purchase_price`='" + unitprice
		//				+ "',`item_total_stock`=`item_total_stock`+'" + value
		//				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
		//				+ itemID);
	}
	public void addStock1(String itemID, String value, String expiryDate,
			String unitprice) throws Exception {

		statement.executeUpdate("UPDATE `items_detail` SET   `item_purchase_price`='" + unitprice
				+ "',`item_total_stock`=`item_total_stock`+'" + value
				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
				+ itemID); //update mrp on 13jan 2021(Happy Lohri)
		//		statement.executeUpdate("UPDATE `items_detail` SET  `item_purchase_price`='" + unitprice
		//				+ "',`item_total_stock`=`item_total_stock`+'" + value
		//				+ "', `item_expiry_date`='" + expiryDate + "' WHERE `item_id`="
		//				+ itemID);
	}
	public void updateStockOnly(String itemID,String value) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`='"+value+"' WHERE `item_id`="+itemID);
	}
	public void updateStock(String itemID,String value,String expiryDate) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`='"+value+"', `item_expiry_date`='"+expiryDate+"' WHERE `item_id`="+itemID);
	}
	public void addStockByReturn1(String itemID,String value) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"+value+"' WHERE `item_id`='"+itemID+"'");
		System.out.print("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`+'"+value+"' WHERE `item_id`='"+itemID+"'");
	}
	public void subtractStock(String itemID,String value) throws Exception
	{
		System.out.println("UPDATE `items_detail` SET `item_total_stock`=`item_total_stock`-'"+value+"' WHERE `item_id`="+itemID);
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
	public ResultSet retrieveItemLocations()
	{
		String query="SELECT DISTINCT(`item_location`) FROM `items_detail` WHERE 1 	ORDER BY 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveMainCategories()
	{
		String query="SELECT DISTINCT(`item_category`) FROM `items_detail` WHERE 1 	ORDER BY 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet getCharge(String itemID)
	{
		String query="SELECT return_charge_reqd FROM `items_detail` WHERE item_id='"+itemID+"'";
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
		String query="SELECT DISTINCT(`item_category_name`) FROM `items_detail` WHERE 1 ORDER BY 1";
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
		String query="SELECT `item_name`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`, `item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_location`,`item_igst`,`item_code`,`item_maximum_units`,`item_reoder_level`,`item_text1`, `item_text2`, `item_text3`, `item_text4`,`item_salt_name`,`item_risk_type`,`item_category_type`,`doctor_refrence`,`item_other`,`formula_active`,`changed_unit_price`,`is_price_change` FROM `items_detail` WHERE `item_id` = '"+id+"' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveLastItemID()
	{
		String query="SELECT `item_id` from items_detail order by `item_id` desc limit 1 ";
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
		String query="SELECT `item_id`, `item_name`, `item_desc`, `item_brand`, `item_total_stock`, `item_minimum_units`,case when `item_expiry_date`='0000-00-00' then '' else `item_expiry_date` end,(CASE WHEN CONVERT(`item_total_stock`,DECIMAL) >=CONVERT(`item_minimum_units`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `items_detail` WHERE 1";
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

	public void updateDoctor(String Doctor_to,String Doctor_from) throws Exception
	{

		statement.executeUpdate("UPDATE `items_detail` SET `doctor_refrence`='"+Doctor_to+"' WHERE `doctor_refrence`='"+Doctor_from+"'");
		System.out.print("UPDATE `items_detail` SET `doctor_refrence`='"+Doctor_to+"' WHERE `doctor_refrence`='"+Doctor_from+"'");
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
	public ResultSet retrievetAllItems(String storeType,String str)
	{
		String query="SELECT `item_id`, `item_name`, `doctor_refrence`, `item_purchase_price`, `item_selling_price`, `item_status` ,  `item_meas_unit`,`item_risk_type`,`item_mrp`,`batch_reqd` FROM `items_detail` WHERE item_location like '%"+str+"%' and item_location like '%"+storeType+"%'";
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
	public ResultSet searchItemWithIdOrNmae(String index)
	{
		String query = "SELECT * FROM (SELECT `item_id` AS A, `item_name` AS B, `item_desc` AS C, `item_meas_unit` AS D, `item_tax_type` AS E, `item_tax_value` AS F, `item_purchase_price` AS G, `item_total_stock` AS H, `item_expiry_date` AS I, `item_selling_price` AS J, `item_mrp` AS K, `item_meas_unit` AS M FROM `items_detail` where item_id LIKE '%" + index + "%' and item_status ='Yes' "
				+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_desc LIKE '%" + index + "%' and item_status ='Yes'"
				+ " UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_name LIKE '%" + index + "%' and item_status ='Yes' "
				+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_salt_name LIKE '%" + index + "%' and item_status ='Yes') T GROUP BY 1";
		System.out.println("HOLLOE-"+query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchItemWithIdOrNmae(String index,boolean flag)
	{
		String query;
	    
		
		if(flag)
		{
			query="SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_id="+index+"";
		}
		else {
			query = "SELECT * FROM (SELECT `item_id` AS A, `item_name` AS B, `item_desc` AS C, `item_meas_unit` AS D, `item_tax_type` AS E, `item_tax_value` AS F, `item_purchase_price` AS G, `item_total_stock` AS H, `item_expiry_date` AS I, `item_selling_price` AS J, `item_mrp` AS K, `item_meas_unit` AS M FROM `items_detail` where item_id LIKE '%" + index + "%' "
					+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_desc LIKE '%" + index + "%'"
					+ " UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_name LIKE '%" + index + "%' "
					+ "UNION ALL SELECT `item_id`, `item_name`, `item_desc`, `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`, `item_mrp`, `item_meas_unit` FROM `items_detail` where item_salt_name LIKE '%" + index + "%') T GROUP BY 1";
		}
		System.out.println("HOLLOE-"+query);
		System.out.println("HOLLOE-"+flag);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchItemWithIdOr()
	{

		String query = "SELECT `item_id`,`item_name` from `items_detail`";

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public ResultSet CheckBatchReqd(String itemID)
	{

		String query = "SELECT `batch_reqd` from `items_detail` where `item_id`='"+itemID+"'";

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public ResultSet searchItemWithIdNew(String index)
	{
		String query = "SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_meas_unit` FROM `items_detail` where item_id LIKE '%"+index+"%' OR `item_code` LIKE '%"+index+"%' OR item_name LIKE '%"+index+"%' OR item_desc LIKE '%"+index+"%'  OR item_salt_name LIKE '%"+index+"%'";	
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet GetInsuranceItem(String index)
	{
		String query = "SELECT for_insurance from items_detail WHERE item_id ='"+index+"'";	
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
		String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`, `item_surcharge`,`item_igst`,`item_location`,`item_oredered`,`stock_type`,`batch_reqd` FROM `items_detail` where  `item_id` = '"+index+"'";
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
		String query="SELECT `item_id`,`item_name`, `item_desc`,  `item_meas_unit`, `item_tax_type`, `item_tax_value`, `item_purchase_price`, `item_total_stock`, `item_expiry_date`, `item_selling_price`,`item_mrp`,`item_location`, `item_surcharge`,`item_hsn_code`,`formula_active`,`stock_type`,`formula_applied`,`item_risk_type`,`batch_reqd`,`changed_unit_price`,`is_price_change` FROM `items_detail` where  `item_id` = '"+index+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet itemDetailBatch2(String index) {
		String query = "SELECT `item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_stock` FROM `batch_tracking` where  `id` = '"
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
		String insertSQL = "INSERT INTO `items_detail`(`item_name`,`item_code`, `item_desc`, `item_brand`, `item_hsn_code`, `item_treatment`, `item_lab`, `item_surgery`, `item_drug`, `item_imaging`, `item_inpatient`, `item_medic_income`, `item_category`,`item_category_name`, `item_meas_unit`, `item_location`, `item_type`, `item_tax_type`, `item_tax_value`, `item_surcharge`,`item_igst`,`item_purchase_price`, `item_selling_price`, `item_total_stock`, `item_mrp`, `item_minimum_units`, `item_status`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`,`item_maximum_units`,`item_reoder_level`,`item_salt_name`,`item_text1`, `item_text2`, `item_text3`, `formula_active`,`item_risk_type`,`item_category_type`,`doctor_refrence`,`item_other`,`batch_reqd`,`changed_unit_price`,`is_price_change`) VALUES  (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i <46; i++) {
			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public  void inserDataBatchNew(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `batch_tracking`( `item_id`, `item_name`, `item_desc`, `item_batch`, `item_stock`, `item_qauntity_entered`, `item_expiry`, `item_date`, `item_time`, `item_last_used`,`item_unitprice`,`item_mrp`,`item_measunits`,`item_tax`,`item_surcharge`,`item_taxvalue`,`item_surcharge_value`,`item_unit_price_new`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	System.out.println(insertSQL);
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <19; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		 
	  preparedStatement.executeUpdate();
	  }
}