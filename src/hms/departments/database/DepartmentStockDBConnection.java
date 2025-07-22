package hms.departments.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class DepartmentStockDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public DepartmentStockDBConnection() {

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

	public ResultSet retrieveStockrahul(String departmentName,String batch) {
		String query="";
		if(batch.equals("batch")) {
			query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`, sum(`total_stock`) FROM `department_stock_register` D WHERE `department_name`='"
					+ departmentName + "' and `item_active`=1 and batch_id <> '' and total_stock>0 GROUP BY 1 ORDER BY CAST(`item_id` as unsigned)";
		}
		else {
			query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`,  SUM(`total_stock`) FROM `department_stock_register` D WHERE `department_name`='"
					+ departmentName + "' and `item_active`=1 and total_stock>0 and batch_id <> '' GROUP BY 1 ORDER BY CAST(`item_id` as unsigned)";
		}
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveStockbt(String itemid, String departmentName,String batch_id) {
		String query = "SELECT `total_stock`, `expiry_date` FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveBatchDATA(String itemid, String departmentName) {
		String query = "SELECT `batch_name`, `expiry_date`,`total_stock` FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' order by  `expiry_date`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrievetAllBatches1(String itemid, String departmentName) {
		String query = "SELECT `stock_item_id`,`batch_id`,`batch_name`, `expiry_date`,`total_stock` FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' order by  `expiry_date` desc";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveStockbt1(String itemid, String departmentName) {
		String query="\r\n"
				+ "SELECT SUM(total_stock),(\r\n"
				+ "SELECT `expiry_date` FROM `department_stock_register` WHERE `item_id`='"+itemid+"' AND `department_name`='"+departmentName+"' and total_stock>0  ORDER BY CAST(expiry_date AS UNSIGNED ) desc limit 1) as expiry "
				+ " FROM `department_stock_register` WHERE `item_id`='"+itemid+"' AND `department_name`='"+departmentName+"' and total_stock>0 ";


		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public void addStock(String itemID, String value, String departmentName)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register` SET `total_stock`=`total_stock`+'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "'");
	}

	public void setStock(String itemID, String value, String departmentName)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register` SET `total_stock`='"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "'");
	}
	public void insertRequestItem(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO `requested_item_from_department`( `item_id`, `item_name`, `dept_name`, `requested_qty`, `status`, `entered_date`, `requested_user`) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		for (int i = 1; i <8; i++) {

			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();
	}
	public void subtractStock(String itemID, String value, String departmentName, String batch_id)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`=`total_stock`-'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"'");

	}
	public void subtractStock(String itemID, String value, String departmentName)
			throws Exception {

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`=`total_stock`-'"
				+ value
				+ "' WHERE `item_id`="
				+ itemID
				+ " AND `department_name`='" + departmentName + "' AND `total_stock`>0 LIMIT 1");

	}
	public void DeptSetZeroStock(String ID)
			throws Exception {
		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`='0' WHERE `stock_item_id`='" + ID + "'");

	}

	public void updateMinMaxQty(String ID, String currentStock,String batchID,String batch,String exp)
			throws Exception {
		System.out.println("UPDATE `department_stock_register`  SET `total_stock`='"
				+ currentStock
				+ "',`batch_id`='"
				+ batchID+ "',`batch_name`='"
				+ batch
				+ "',`expiry_date`='"+exp+"' WHERE `stock_item_id`='" + ID + "'");

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`='"
				+ currentStock
				+ "',`batch_id`='"
				+ batchID+ "',`batch_name`='"
				+ batch
				+ "',`expiry_date`='"+exp+"' WHERE `stock_item_id`='" + ID + "'");

	}
	public void updateMinMaxQty1(String ID, String currentStock,String miniqty,String maxqty,String name,String status)
			throws Exception {
		System.out.println("UPDATE `department_stock_register`  SET `total_stock`='" +currentStock+ "',`minimum_Qty`='" +miniqty+ "', `maximum_Qty`= '" +maxqty+ "',`item_active`='"+status+"' WHERE `stock_item_id`='" + ID + "' and `department_name`='" + name + "'");

		statement
		.executeUpdate("UPDATE `department_stock_register`  SET `total_stock`='" +currentStock+ "',`minimum_Qty`='" +miniqty+ "', `maximum_Qty`= '" +maxqty+ "',`item_active`='"+status+"' WHERE `stock_item_id`='" + ID + "' and `department_name`='" + name + "'");

	}
	public void updateIssuedItem(String itemID, String status,String issuedperson,String issuedqty, String departmentName,String reid)
			throws Exception {

		statement
		.executeUpdate("UPDATE `requested_item_from_department`  SET `status`='"
				+ status
				+ "',`issued_person`='"
				+ issuedperson
				+ "',`issued_qty`=`issued_qty`+'"+issuedqty+ "' WHERE `item_id`="
				+ itemID
				+ " AND `dept_name`='" + departmentName + "' and id='"+reid+"'");

	}
	public int subtractStockTestName(String test_name, String departmentName)
			throws Exception {

		int affectedRows =statement
				.executeUpdate("UPDATE `department_stock_register` tab1 INNER JOIN `exam_bom` tab2 ON tab1.`item_id`=tab2.`item_id` SET tab1.`total_stock`=tab1.`total_stock`-tab2.`item_qty` WHERE tab2.`exam_name`='"
						+ test_name
						+ "' AND tab1.`department_name`='"
						+ departmentName + "'");
		return affectedRows;
	}

	public int subtractStockTestID(String test_id, String departmentName)
			throws Exception {

		int affectedRows =statement
				.executeUpdate("UPDATE `department_stock_register` tab1 JOIN `exam_bom` tab2 ON tab1.`item_id`=tab2.`item_id` JOIN `test_results` tab3 ON tab2.`exam_name`=tab3.`exam_sub_name` SET tab1.`total_stock`=tab1.`total_stock`-tab2.`item_qty` WHERE tab3.`exam_counter`='"
						+ test_id
						+ "' AND tab1.`department_name`='"
						+ departmentName + "'");
		return affectedRows;

	}

	public  ResultSet getBatchName(String item_id,String dept_id) {

		String query = "SELECT `batch_name`, total_stock, batch_id, expiry_date FROM department_stock_register where `item_id` = '"+item_id+"' AND `total_stock`>0 "
				+ "AND department_id = '"+dept_id+"' HAVING (batch_id<>'' or batch_id<>'0') ORDER BY CAST(expiry_date AS UNSIGNED ), CAST( total_stock AS UNSIGNED ) ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

			//	System.out.println(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", javax.swing.JOptionPane.ERROR_MESSAGE);
		} 

		return rs;
	}
	public ResultSet retrieveDptStock(String itemid, String departmentName) {
		String query = "SELECT  SUM(total_stock) FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND total_stock>0 and batch_id<>''";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDptStock1(String itemid, String departmentName) {
		String query = "SELECT  SUM(total_stock) FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND total_stock>0 and batch_id=''";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDptLastWeekLimit(String item_id, String departID) {
		String query = "SELECT case when SUM(issued_qty)<>'' THEN SUM(issued_qty) else 0 end as qty from issued_department_register idr WHERE item_id ='"+item_id+"' and department_id ='"+departID+"' and `date` BETWEEN DATE_SUB(CURRENT_DATE() , INTERVAL 10 DAY) and  CURRENT_DATE() ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getalloweditem(String itemid, String insurancename) {
		System.out.println(itemid+"------"+insurancename);
		String query = "\r\n"
				+ "SELECT\r\n"
				+ "    CASE WHEN EXISTS \r\n"
				+ "    (\r\n"
				+ "        SELECT * FROM insurance_items_adjustment_list WHERE item_id='"+itemid+"' AND insurance_name ='"+insurancename+"'\r\n"
				+ "    )\r\n"
				+ "    THEN '1' ELSE '0' END as RESULT ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveStock1(String itemid, String departmentName,String batch_id) {

		String query = "SELECT  `batch_name`,`batch_id`,`total_stock`, `expiry_date` FROM `department_stock_register` WHERE `item_id`='"
				+ itemid + "' AND `department_name`='" + departmentName + "' AND `batch_id`='"+batch_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveStock(String itemid, String departmentName) {
		String query="\r\n"
				+ "SELECT SUM(total_stock),(\r\n"
				+ "SELECT `expiry_date` FROM `department_stock_register` WHERE `item_id`='"+itemid+"' AND `department_name`='"+departmentName+"' and total_stock>0  ORDER BY CAST(expiry_date AS UNSIGNED ) desc limit 1) as expiry "
				+ " FROM `department_stock_register` WHERE `item_id`='"+itemid+"' AND `department_name`='"+departmentName+"' and total_stock>0 ";


		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveStock(String departmentName) {
		String query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`, case when expiry_date = '0000-00-00' then 'N/A' else expiry_date end as expiry_date, `total_stock` FROM `department_stock_register` D WHERE `department_name`='"
				+ departmentName + "' and `item_active`=1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveProfile(String departmentName) {
		String query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`, case when expiry_date = '0000-00-00' then 'N/A' else expiry_date end as expiry_date, `total_stock`,`minimum_qty`,`maximum_qty`, (CASE WHEN CONVERT(`total_stock`,DECIMAL) >=CONVERT(`minimum_qty`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `department_stock_register` D WHERE `department_name`='"
				+ departmentName + "' and `item_active`='1' ORDER BY CAST(`item_id` AS unsigned ) ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveProfileInactive(String departmentName,String check) {
		String query="";
		if(check=="1") {
			query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`, case when expiry_date = '0000-00-00' then 'N/A' else expiry_date end as expiry_date, `total_stock`,`minimum_qty`,`maximum_qty`, (CASE WHEN CONVERT(`total_stock`,DECIMAL) >=CONVERT(`minimum_qty`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `department_stock_register` D WHERE `department_name`='"
					+ departmentName + "' and `item_active`='0' AND batch_id <> '' ORDER BY CAST(`item_id` AS unsigned ) ASC";
		}else {
			query = "SELECT  `item_id`, (SELECT `item_code` FROM `items_detail` WHERE `item_id`=D.`item_id`),(SELECT `item_name` FROM `items_detail` WHERE `item_id`=D.`item_id`), `item_desc`, case when expiry_date = '0000-00-00' then 'N/A' else expiry_date end as expiry_date, `total_stock`,`minimum_qty`,`maximum_qty`, (CASE WHEN CONVERT(`total_stock`,DECIMAL) >=CONVERT(`minimum_qty`,DECIMAL) THEN 'With in level' ELSE 'Out of level' END) FROM `department_stock_register` D WHERE `department_name`='"
					+ departmentName + "' and `item_active`='0' ORDER BY CAST(`item_id` AS unsigned ) ASC";
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




	public ResultSet retrieveProfile(String departmentName,String check) {
		String query="SELECT\r\n"
				+ "	D.item_id,\r\n"
				+ "	I.item_code,\r\n"
				+ "	I.item_desc ,\r\n"+ "	I.`item_name` ,\r\n"
				+ "	'N/A',\r\n"
				+ "	COALESCE(sum(D.total_stock),0),\r\n"
				+ "	COALESCE((SELECT dsr.minimum_qty from department_stock_register dsr where dsr.`department_name` = '"+departmentName+"' and dsr.item_id = D.item_id and dsr.minimum_qty >0 order by dsr.last_issued desc limit 1), 0) ,\r\n"
				+ "	COALESCE((SELECT dsr.maximum_qty from department_stock_register dsr where dsr.`department_name` = '"+departmentName+"' and dsr.item_id = D.item_id and dsr.maximum_qty >0 order by dsr.last_issued desc limit 1), 0) ,\r\n"
				+ "	(CASE\r\n"
				+ "		WHEN CONVERT(D.`total_stock`,\r\n"
				+ "		DECIMAL) >= CONVERT(D.`minimum_qty`,\r\n"
				+ "		DECIMAL) THEN 'With in level'\r\n"
				+ "		ELSE 'Out of level'\r\n"
				+ "	END),\r\n"
				+ " D.stock_item_id\r\n"
				+ "FROM\r\n"
				+ "	department_stock_register D\r\n"
				+ "join items_detail I on\r\n"
				+ "	I.item_id = D.item_id\r\n"
				+ "WHERE\r\n"
				+ "	`department_name` = '"+departmentName+"'\r\n"
				+ "	and `item_active` = '1' and D.`total_stock`>=0\r\n"
				+ "GROUP by\r\n"
				+ "	1\r\n"
				+ "ORDER BY\r\n"
				+ "	CAST(I.`item_id` AS unsigned ) ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveminmax(String itemid,String departmentName) {
		String query = "SELECT  minimum_qty,maximum_qty FROM `department_stock_register`  WHERE `department_name`='"
				+ departmentName + "' and `item_id`='"+itemid+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM items_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}


	public int inserDepartmentStock(String[] data) throws Exception {
		String check="SELECT * FROM department_stock_register dsr  WHERE item_id ='"+data[4]+"' and batch_id ='"+data[10]+"' AND department_id='"+data[0]+"' limit 1";
		try {
			rs = statement.executeQuery(check);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int i=0;
		while(rs.next())
		{
			i++;
		}
		if(i>0){
			return Updatedata(data);
		}else{
			if(data[11].equals("N/A") && checkItem(data[4],data[0]))
			{
				return UpdateStock(data);
			}else {
				return Insertdata(data);
			}
		}		
	}

	private boolean checkItem(String itemID,String deptID) throws SQLException {
		// TODO Auto-generated method stub
		String check="SELECT * FROM department_stock_register dsr  WHERE item_id ='"+itemID+"' AND department_id='"+deptID+"' AND batch_id='0' limit 1";
		try {
			rs = statement.executeQuery(check);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int i=0;
		while(rs.next())
		{
			i++;
		}
		if(i>0)
		{
			return true;
		}else {
			return false;
		}

	}
	private int UpdateStock(String[] data) {
		// TODO Auto-generated method stub
		String upadte="UPDATE `department_stock_register` SET `user_id`='"+data[2]+"', `user_name`='"+data[3]+"', `total_stock`=`total_stock`+'"+data[7]+"', `last_issued`='"+data[8]+"', `expiry_date`='"+data[9]+"' WHERE item_id ='"+data[4]+"' AND department_id='"+data[0]+"' limit 1";
		System.out.println(upadte);
		int rs=0;
		try {
			rs=statement.executeUpdate(upadte);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	private int Updatedata(String[] data) {
		// TODO Auto-generated method stub
		String upadte="UPDATE `department_stock_register` SET `user_id`='"+data[2]+"', `user_name`='"+data[3]+"', `total_stock`=`total_stock`+'"+data[7]+"', `last_issued`='"+data[8]+"', `expiry_date`='"+data[9]+"' WHERE item_id ='"+data[4]+"' and batch_id ='"+data[10]+"' AND department_id='"+data[0]+"'";
		System.out.println(upadte);
		int rs=0;
		try {
			rs=statement.executeUpdate(upadte);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	private int Insertdata(String[] data) {
		// TODO Auto-generated method stub
		String insert=" INSERT INTO `department_stock_register`(`department_id`, `department_name`,`user_id`, `user_name`, `item_id`, `item_name`, `item_desc`, `total_stock`, `last_issued`, `expiry_date`,`batch_id`,`batch_name`) VALUES ('"+data[0]+"','"+data[1]+"','"+data[2]+"','"+data[3]+"','"+data[4]+"','"+data[5]+"','"+data[6]+"','"+data[7]+"','"+data[8]+"','"+data[9]+"','"+data[10]+"','"+data[11]+"')";
		System.out.println(insert);
		int rs=0;
		try {
			rs=statement.executeUpdate(insert);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}

	public ResultSet retrieveBatches(String item_id, String dept_name) {
		// TODO Auto-generated method stub
		String query = "SELECT  `batch_name`,`batch_id` FROM `department_stock_register` WHERE `item_id`='"
				+ item_id + "' AND `department_name`='" + dept_name + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


}
