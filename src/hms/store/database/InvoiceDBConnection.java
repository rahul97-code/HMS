package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class InvoiceDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public InvoiceDBConnection() {

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
	public ResultSet retrieveAllDataReturn(String dateFrom, String dateTo) { 
		String query = "SELECT `return_invoice_id`, `return_invoice_id2`, `return_invoice_supplier_name`, `return_invoice_date`, `return_invoice_time`,  `return_invoice_final_amount` FROM `return_invoice_entry` WHERE `return_invoice_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `return_invoice_id` DESC ";
		System.out.println(query);
		try {
			//			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllbeddata(String ward_name,String space) { 
		String query="";
		if(space.equals("1"))
		{
			query ="SELECT ward_id, ward_name , bed_no, p_id , p_name,bed_filled, building_name FROM `ward_management` WHERE `ward_name`='"+ward_name+"' AND bed_filled <> 0";
		}
		else if(space.equals("0"))
		{
			query ="SELECT ward_id, ward_name , bed_no, p_id , p_name,bed_filled, building_name FROM `ward_management` WHERE `ward_name`='"+ward_name+"' AND bed_filled <> 1";

		}
		else {
			query ="SELECT ward_id, ward_name , bed_no, p_id , p_name,bed_filled, building_name FROM `ward_management` WHERE `ward_name`='"+ward_name+"'";
		}
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public  ResultSet retrievePOReceQty(String supplier_id) {
		String query = "SELECT * from po_items where po_id='"+supplier_id+"' ";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void UpdatePoStatus(String po_id,String status) throws Exception
	{
		statement.executeUpdate("UPDATE `po_entry` SET `po_status`='"+status+"' WHERE `po_id`='"+po_id+"'");
	}
	public ResultSet retrievePOItemData(String ID) { 
		String query = "SELECT	`po`.`po_item_name`,`po`.`item_qty`,`po`.`item_id`,`po`.`item_paid_quantity`  FROM `po_items` `po` LEFT JOIN `items_detail` ON((`items_detail`.`item_id` = CONVERT(`po`.`item_id` USING utf8))) WHERE	`po`.`po_id` ='"+ID+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePOItemCheck(String ID,String ItemID) { 
		String query = "SELECT	*  FROM `po_items` `po` LEFT JOIN `items_detail` ON((`items_detail`.`item_id` = CONVERT(`po`.`item_id` USING utf8))) WHERE	`po`.`po_id` ='"+ID+"' and `po`.`item_id`='"+ItemID+"'";
		System.out.println(query);

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateInvoiceStatus(String invoice_id,String payment_id) throws Exception
	{
		statement.executeUpdate("UPDATE `invoice_entry` SET `invoice_text1`='"+payment_id+"' WHERE `invoice_id`="+invoice_id);
	}
	public ResultSet retrieveAllInvoiceItems(String fromDate,String toDate) {
		String query = "SELECT `invoice_item_id` AS 'Entry ID',(SELECT `invoice_id2` FROM `invoice_entry` WHERE `invoice_id`=I.`invoice_id`) AS 'Invoice Number',(SELECT `invoice_supplier_name` FROM `invoice_entry` WHERE `invoice_id`=I.`invoice_id`) AS 'Supplier Name', `item_id` AS 'Item Id', `invoice_item_name` AS 'Item Name', `invoice_item_desc` AS 'Item Desc', `item_batch_number`  AS 'Batch Number',`item_meas_units` AS 'Item Units', `item_qty` AS 'Qty', `item_unit_price` AS 'Unit Price', `item_discount` AS 'Discount',  `item_free_quantity` AS 'Free Qty', `item_paid_quantity`  AS 'Qty Paid',`item_tax`  AS 'CGST %',`item_surcharge`  AS 'SGST %', `item_tax_value` AS 'CGST Value', `item_surcharge_value` AS 'SGST Value', `invoice_value`  AS 'Total Value', `item_expiry_date` AS 'Expiry Date', `item_date` AS 'Date', `item_time` AS 'Time' FROM `invoice_items` I WHERE `item_date` BETWEEN '"
				+ fromDate + "' AND '" + toDate + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllPending(String supplier_id) {
		String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_tax`, `invoice_total_amount`, `invoice_date` FROM `invoice_entry` WHERE `invoice_supplier_id`='"+supplier_id+"' AND `invoice_text1`='null'";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveItems(String invoice_id) {
		String query = "SELECT `item_id`, `invoice_item_name`, `invoice_item_desc`,`item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`, `invoice_value`, `item_expiry_date`, `item_date`, `item_batch_number`,`invoice_item_id` FROM `invoice_items` WHERE `invoice_id` ='"+invoice_id+"'";

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
	public int inserInvoice(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `invoice_entry`(`invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_due_date`, `invoice_paid`, `invoice_entry_user`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_text2`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 15; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}

	public void inserInvoiceItem(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price_new`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`,`item_text1`,`item_unit_price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i <23; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public ResultSet lastPurchaseItem(String item_id) {
		String query = "SELECT A.`invoice_supplier_name` AS 'Vender Name',B.`item_qty` AS 'Qty', B.`item_unit_price` AS 'Unit Price',A.`invoice_date` AS 'Date' FROM `invoice_entry` A JOIN `invoice_items` B ON A.`invoice_id`=B.`invoice_id` WHERE B.`item_id`='"+item_id+"' ORDER BY A.`invoice_date` DESC ";

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
	public ResultSet retrieveFreeQuantity(String index)
	{
		String query="SELECT `item_free_quantity` FROM `invoice_items` where  `item_id` = '"+index+"' order by item_date desc limit 1";
		System.out.println("HELLO : "+query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveWithID(String id) {
		String query = "SELECT `invoice_id`, `invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_entry_user`, `invoice_total_amount` FROM `invoice_entry` WHERE `invoice_text1`='"+id+"'";
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet invoiceWithID(String id) {
		String query = "SELECT\r\n"
				+ "	`invoice_id`,\r\n"
				+ "	`invoice_id2`,\r\n"
				+ "	`invoice_order_no`,\r\n"
				+ "	`invoice_supplier_id`,\r\n"
				+ "	`invoice_supplier_name`,\r\n"
				+ "	`invoice_date`,\r\n"
				+ "	`invoice_time`,\r\n"
				+ "	`invoice_due_date`,\r\n"
				+ "	`invoice_total_amount`,\r\n"
				+ "	`invoice_tax`,\r\n"
				+ "	`invoice_discount`,\r\n"
				+ "	`invoice_final_amount`,\r\n"
				+ "	`invoice_entry_user`,\r\n"
				+ "	`invoice_text2`,\r\n"
				+ "	`invoice_text7`,\r\n"
				+ "	dd.doc_name\r\n"
				+ "FROM\r\n"
				+ "	invoice_entry ie\r\n"
				+ "left join po_entry pe on\r\n"
				+ "	ie.invoice_order_no = pe.po_id2 \r\n"
				+ "left join doctor_detail dd on\r\n"
				+ "	dd.doc_id = pe.doctor_id\r\n"
				+ "WHERE\r\n"
				+ "	`invoice_id` = '"+id+"'";
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
	
	public ResultSet retrieverequestedItemAll() {
		String query = "SELECT `id`, `item_id`, `item_name`,`dept_name`,`requested_user`,`requested_qty`-`issued_qty`,`entered_date`,`status` FROM `requested_item_from_department` where `status`!='Issued'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieverequestedItem(String deptname) {
		String query = "SELECT `id`, `item_id`, `item_name`,`dept_name`,`requested_user`,`requested_qty`-`issued_qty`,`entered_date`,`status` FROM `requested_item_from_department` where `status`!='Issued' and `dept_name`='"+deptname+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieverequestedItemStock(String itemid) {
		String query = "SELECT  `item_total_stock` FROM `items_detail` WHERE `item_id`='"+itemid+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`,`invoice_id2`,   COALESCE(`invoice_final_amount`, 0 ) FROM `invoice_entry` WHERE `invoice_date`  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `invoice_id` DESC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllItemsData(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_item_id`, `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_batch_number`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`, `invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user` FROM `invoice_items` WHERE `item_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveSuppliersSalesTax1(String dateFrom, String dateTo) { 
		String query = "SELECT `invoice_id` as 'ID',`invoice_supplier_id`, `invoice_supplier_name`,`invoice_id2`,`invoice_order_no`,`invoice_final_amount`, `invoice_discount`,`invoice_date`, `invoice_time` FROM `invoice_entry` where `invoice_date` between '"+dateFrom+"' and '"+dateTo+"'   ORDER BY `invoice_supplier_id`*1  ASC, `invoice_id` ASC";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveSalesTaxReport(String id) { 
		String query = "SELECT b.`invoice_id`,`invoice_supplier_id`, `invoice_supplier_name`,`invoice_id2` as 'Invoice Number',`invoice_order_no`,`invoice_final_amount`, `invoice_discount`,`invoice_date`, `invoice_time`,`item_tax`, sum(`item_tax_value`) as 'TAX AMOUNT',`item_surcharge`, SUM(`item_surcharge_value`)as 'Surcharge Amount' FROM `invoice_entry` a,`invoice_items` b where a.`invoice_id`=b.`invoice_id` and b.`invoice_id` IN('"+id+"') group by `item_tax`";
		try {
			System.out.println(query);
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

	public void UpdateReceivedQtyInPO(String po_id,String item_id,String qty) throws Exception
	{
		statement.executeUpdate("UPDATE `po_items` SET `item_paid_quantity`=`item_paid_quantity`+'"+qty+"' WHERE `po_id`='"+po_id+"' and `item_id`='"+item_id+"'");
	}
	//	public void inserInvoiceItem(String[] data) throws Exception
	//	  {
	//		  String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price_new`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`,`item_text1`,`item_unit_price`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
	//		  for (int i = 1; i <23; i++) {
	//			
	//				  preparedStatement.setString(i, data[i-1]);
	//			}
	//		  preparedStatement.executeUpdate();
	//	  }
	//	public int inserInvoice(String[] data) throws Exception {
	//		String insertSQL = "INSERT INTO `invoice_entry`(`invoice_id2`, `invoice_order_no`, `invoice_supplier_id`, `invoice_supplier_name`, `invoice_date`, `invoice_time`, `invoice_due_date`, `invoice_paid`, `invoice_entry_user`, `invoice_total_amount`, `invoice_tax`, `invoice_discount`, `invoice_final_amount`,`invoice_text2`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	//		for (int i = 1; i < 15; i++) {
	//
	//			preparedStatement.setString(i, data[i - 1]);
	//		}
	//		 preparedStatement.executeUpdate();
	//		 ResultSet rs = preparedStatement.getGeneratedKeys();
	//		  rs.next();
	//		 
	//		 return  rs.getInt(1);
	//	}
	public void updateInvoiceItem(String[] data,String id) throws Exception
	{
		String updateSQL=" UPDATE `invoice_items` SET `item_id`='"+data[0]+"',`invoice_item_name`='"+data[1]+"',`invoice_item_desc`='"+data[2]+"',`invoice_id`='"+data[3]+"',`item_meas_units`='"+data[4]+"',`item_qty`='"+data[5]+"',`item_free_quantity`='"+data[6]+"',`item_paid_quantity`='"+data[7]+"',`item_unit_price`='"+data[8]+"',`item_discount`='"+data[9]+"',`item_tax`='"+data[10]+"',`item_surcharge`='"+data[11]+"',`item_tax_value`='"+data[12]+"', `item_surcharge_value`='"+data[13]+"',`invoice_value`='"+data[14]+"', `item_expiry_date`='"+data[15]+"', `item_date`='"+data[16]+"', `item_time`='"+data[17]+"', `item_entry_user`='"+data[18]+"', `item_batch_number`='"+data[19]+"' where item_id='"+data[0]+"' and invoice_id="+id+"";
		//		  String insertSQL = "INSERT INTO `invoice_items`( `item_id`, `invoice_item_name`, `invoice_item_desc`, `invoice_id`, `item_meas_units`, `item_qty`, `item_free_quantity`, `item_paid_quantity`, `item_unit_price`, `item_discount`, `item_tax`, `item_surcharge`, `item_tax_value`, `item_surcharge_value`,`invoice_value`, `item_expiry_date`, `item_date`, `item_time`, `item_entry_user`, `item_batch_number`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(updateSQL);
		System.out.println(updateSQL);
		//		  for (int i = 1; i <21; i++) {
		//			
		//				  preparedStatement.setString(i, data[i-1]);
		//			}
		preparedStatement.executeUpdate();
	}
	public int updateInvoiceEntry(String[] data,String id) throws Exception {
		String insertSQL = "UPDATE `invoice_entry` SET `invoice_id2`=?, `invoice_order_no`=?, `invoice_supplier_id`=?, `invoice_supplier_name`=?, `invoice_date`=?, `invoice_time`=?, `invoice_due_date`=?, `invoice_paid`=?, `invoice_entry_user`=?, `invoice_total_amount`=?, `invoice_tax`=?, `invoice_discount`=?, `invoice_final_amount`=?, `invoice_text7`=?,`invoice_text2`=?,`invoice_text6`=? Where invoice_id="+id+"";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 17; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}
