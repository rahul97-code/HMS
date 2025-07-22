package hms1.expenses.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class IPDExpensesDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public IPDExpensesDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	public ResultSet retrieveAllOtExpense(String ipd_id) { 

		String query = "select a.ipd_id, round(sum(if(a.expense_type='implant' ,a.expense_amount,0)))as `Implant`,\r\n"
				+ "				sum(if(a.charges_id like 'B%',a.expense_amount,0))as `Bed Charges`,\r\n"
				+ "			 	round(sum(a.expense_amount))as `ipd_total`,\r\n"
				+ "				sum(if(a.charges_id = 1146,a.expense_amount,0))as `Hospital Charges` ,\r\n"
				+ "				COALESCE(t.discount,0) as discount\r\n"
				+ "	from ipd_expenses a\r\n"
				+ "	left join (	 select sum(t.s) as discount,t.ipd_id from (select ie.expense_amount as s ,ie.ipd_id  from ipd_expenses ie where ie.expense_name like '%discount%' \r\n"
				+ "	            UNION all\r\n"
				+ "	            SELECT -ip.payment_amount,ip.ipd_id   from ipd_payments ip where ip.payment_type ='ks' )t group by 2)t on t.ipd_id =a.ipd_id\r\n"
				+ "	where a.ipd_id in("+ipd_id+")\r\n"
				+ "	group by 1";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDatatracking(String ipd_id) { 

		String query = "SELECT `expense_id`, `expense_name`, `expense_amount`,`quantity` FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
public ResultSet retrieveInsuranceDataForIsdraft(String ipd_id) { 
		
      	String q1 ="Select * from Insurance_Tracking_list where ipd_id='"+ipd_id+"'";
      	System.out.println(q1);
			try {
				rs = statement.executeQuery(q1);
				

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
		
	return rs;
}
	public int deleterowtracking(String ipd_id,String expense_id) throws Exception {
		String SQL = "DELETE FROM Insurance_Tracking_list WHERE ipd_id="+ipd_id+" and expense_id="+expense_id+"";
		System.out.println(SQL);
		statement.executeUpdate(SQL);
		return 1;
		
	}
public void Updatedraft(String ipd_id, boolean isdraft) throws Exception {
		
		String insertSQL = "UPDATE `ipd_entery` SET `is_draft`=? WHERE `ipd_id2`="+ipd_id+"";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
			preparedStatement.setBoolean(1,isdraft );
		preparedStatement.executeUpdate();
	}

public void UpdateData(String[] data) throws Exception {
		
		String insertSQL = "Replace into `Insurance_Tracking_list` (`ipd_id`,`p_id`,`p_name`,`item_name`,`item_desc`,`date`,`type`,`item_id`,`page_no`,`mrp`,`per_item_price`,`qty`,`amount`,`batch`,`expiry`,`rebate`,`expense_id`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL); 
		for(int i=1;i<18;i++) {
		preparedStatement.setString(i, data[i-1]);
         }
		preparedStatement.executeUpdate();
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
	
	public void DeleteInsurancetracking(String ipd_id) throws Exception
	{

		statement.executeUpdate("DELETE FROM `Insurance_Tracking_list` WHERE `ipd_id`= "+ipd_id+"");
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
	
	public int updateData(String data) throws Exception {
		String insertSQL = "DELETE FROM ipd_expenses\r\n"
				+ "WHERE ipd_id="+data+" and expense_type='BED CHARGES'";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int retrieveEXIST(String ipd_id,String bed_charges) {
		String query = "SELECT COUNT(*) FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"' and `expense_text3`='"+bed_charges+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int c=0;
		try {
			while (rs.next()) {
				c=Integer.parseInt(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
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
	public void updateData(String[] data, String wardIndex) throws Exception {
		String insertSQL = "UPDATE `ward_management` SET `p_id`=?,`p_name`=?,`bed_filled`=?,`ward_entry_date`=? WHERE `ward_id`="
				+ wardIndex + "";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void UpdateIsDelete(String[] data) throws Exception {
		String insertSQL = "UPDATE `ipd_expenses` SET `is_deleted`=? WHERE `expense_id`="+data[0]+"";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
			preparedStatement.setString(1, data[2]);
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveTotalIPDCharge(String ipd_id) { 

		String query = "SELECT coalesce(sum(`expense_amount`),0) FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String ipd_id) { 

		String query = "SELECT `expense_id`, `expense_name`,`quantity`, `expense_amount` FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet TodayExams(String ipd_id) { 

		String query = "SELECT charges_id from ipd_expenses ie WHERE ipd_id = '"+ipd_id+"' and expense_date = CURRENT_DATE() and expense_type like '%exam%' and expense_amount >'1000' GROUP by charges_id ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveReturnItemData(String ipd_id,String dateFrom,String dateTo) { 

		String query = "SELECT `expense_id`,`charges_id` , `expense_name`,`quantity`, `expense_amount`,`expense_date` ,`expense_time`  FROM `ipd_expenses` WHERE return_handling_charges <>'N/A' and  expense_date BETWEEN '"+dateFrom+"' and '"+dateTo+"' and `ipd_id`='"+ipd_id+"'\r\n"
				+ "";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExpences(String expenseType,String dateFrom,String dateTo) { 

		String query = "SELECT `expense_amount` FROM `ipd_expenses` WHERE `expense_name`='"+expenseType+"' AND  `expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExpences(String dateFrom,String dateTo) { 

		String query = "SELECT `expense_name`,count(`expense_amount`),sum(`expense_amount`) FROM `ipd_expenses` WHERE   `expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' group by `expense_name`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataExpense(String patient_id,String dateFrom,String dateTo) { 
		new DateFormatChange();

		String query = "SELECT E.`expense_id`, E.`expense_name`,E.`expense_per_item`, E.`quantity`, E.`expense_amount`,E.`expense_page_no` FROM `ipd_expenses` E JOIN `ipd_entery` I ON E.`ipd_id`=I.`ipd_id` WHERE I.`p_id`='"+patient_id+"' AND E.`expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataFull(String ipd_id) { 
		new DateFormatChange();
		String dateTo= DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `expense_id`, `expense_type`,`expense_name`,`expense_per_item`, REPLACE(FORMAT(SUM(`quantity`),0),',',''), SUM(`expense_amount`),`charges_id`,item_mrp,item_meas_unit,charges_id FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"'  GROUP BY `expense_name` ORDER BY `expense_type` ASC";
		//		String query = "SELECT `expense_id`, `expense_type`,`expense_name`,`expense_per_item`, REPLACE(FORMAT(SUM(`quantity`),0),',',''), SUM(`expense_amount`),`charges_id`, i.`item_mrp`,i.`item_purchase_price`,i.`item_meas_unit` FROM `ipd_expenses` left join items_detail i on(i.item_id=`charges_id`) WHERE `ipd_id`='"+ipd_id+"'  and (i.item_mrp!='' OR i.item_mrp!='n/a') GROUP BY `expense_name` ORDER BY `expense_type` ASC";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCategoryWise(String ipd_id) { 
		new DateFormatChange();
		String dateTo= DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `expense_type`, SUM(`expense_amount`) FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"' GROUP BY `expense_type` ORDER BY `expense_type` ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllInsuranceData(String ipd_id,String insurance) {
		String ratetype="";
		String query="SELECT ins_ratetype  from insurance_detail id where ins_name ='"+insurance+"'";
		try {
			rs = statement.executeQuery(query);
			while(rs.next()){
				ratetype=rs.getString(1);
			}

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		String Query="select\r\n"
				+ "IE.expense_id ,EM.exam_subcat,EM.exam_code ,IE.quantity,EM.exam_rate,IE.expense_date ,EM.exam_lab,IE.expense_amount\r\n"
				+ ",is_deleted from\r\n"
				+ "exam_master_8 EM	\r\n"
				+ "left join ipd_expenses IE on IE.charges_id = EM.exam_code\r\n"
				+ "AND IE.expense_type like '%EXAM%'\r\n"
				+ "where\r\n"
				+ "	IE.ipd_id = "+ipd_id+"";
		try {
			rs = statement.executeQuery(Query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs; 
	
	}
	public ResultSet retrieveAllIPD_DATA(String ipd_id,String insurance) { 
		String ratetype="";
		String dep_date="2023-12-30";
		String query="SELECT ins_ratetype  from insurance_detail id where ins_name ='"+insurance+"'";
		try {
			rs = statement.executeQuery(query);
			while(rs.next()){
				ratetype=rs.getString(1);
			}

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		
		String Query="select\r\n"
				+ "expense_type,\r\n"
				+ "item_id,\r\n"
				+ "item_name,\r\n"
				+ "`change`,\r\n"
				+ "expense_date,\r\n"
				+ "Batch,\r\n"
				+ "Expiry,\r\n"
				+ "package_time,\r\n"
				+ "mrp,\r\n"
				+ "expense_per_item,\r\n"
				+ "sum(Qty) as Qty,\r\n"
				+ "round(sum((expense_per_item + 0) * Qty), 0) as Amount\r\n"
				+ "FROM\r\n"
				+ "(\r\n"
				+ "SELECT\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE IE.expense_name\r\n"
				+ "END AS item_name,\r\n"
				+ "IE.expense_date ,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> ''\r\n"
				+ "AND IE.expense_amount > '0' THEN IA.exp_type\r\n"
				+ "WHEN IE.expense_amount < '0' then 'R'\r\n"
				+ "WHEN expense_desc like '%consulta%' then 'C'\r\n"
				+ "WHEN expense_type like 'inj%'\r\n"
				+ "or expense_text3 like 'inj%'\r\n"
				+ "or expense_text3 like '%medic%' then 'M'\r\n"
				+ "WHEN expense_type like '%service%'\r\n"
				+ "or expense_text3 like '%service%' or expense_text3 in('implant','stent') then 'Z'\r\n"
				+ "ELSE 'O'\r\n"
				+ "END as expense_type,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_id IS NOT NULL\r\n"
				+ "AND IA.new_item_id <> '' THEN IA.new_item_id\r\n"
				+ "ELSE IE.charges_id\r\n"
				+ "END AS item_id ,\r\n"
				+ "COALESCE(IA.`change`,\r\n"
				+ "'A') AS 'change' ,\r\n"
				+ "IE.item_mrp as mrp,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_rate IS NOT NULL\r\n"
				+ "AND IA.new_rate <> '' THEN IA.new_rate\r\n"
				+ "ELSE GREATEST(CASE WHEN IE.quantity >500 THEN IE.quantity ELSE IE.expense_per_item end,\r\n"
				+ "COALESCE(round((bt2.item_mrp + 0) / bt2.item_measunits,2),\r\n"
				+ "0),\r\n"
				+ "COALESCE(round((bt.item_mrp + 0) / bt.item_meas_unit,2),\r\n"
				+ "0))\r\n"
				+ "END AS expense_per_item ,CASE WHEN IE.quantity >500 THEN 1 ELSE\r\n"
				+ "round(SUM(IE.quantity), 0) END AS Qty,\r\n"
				+ "CASE\r\n"
				+ "WHEN IE.batch_name is null then ''\r\n"
				+ "ELSE IE.batch_name\r\n"
				+ "END as Batch,\r\n"
				+ "CASE\r\n"
				+ "WHEN bt.item_expiry_date is null then bt2.item_expiry\r\n"
				+ "ELSE bt.item_expiry_date\r\n"
				+ "END as Expiry,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE IE.expense_desc\r\n"
				+ "END AS expense_desc ,\r\n"
				+ "'0' as package_time /*provide days of minor/major surgery ELSE 0*/\r\n"
				+ "FROM\r\n"
				+ "ipd_expenses IE\r\n"
				+ "LEFT JOIN medicalstore_db.items_detail bt ON\r\n"
				+ "IE.charges_id = bt.item_id\r\n"
				+ "AND expense_type = 'Medical Store' /*predefined*/\r\n"
				+ "LEFT JOIN batch_tracking bt2 ON\r\n"
				+ "IE.batch_id = bt2.id\r\n"
				+ "AND expense_type <> 'Medical Store' /*predefined*/\r\n"
				+ "LEFT JOIN insurance_items_adjustment_list IA ON\r\n"
				+ "IE.charges_id = IA.item_id\r\n"
				+ "AND IE.expense_name = IA.item_name\r\n"
				+ "AND IA.insurance_name = '"+insurance+"' /*User define variable to provide insurance*/\r\n"
				+ "WHERE\r\n"
				+ "IE.ipd_id = '"+ipd_id+"'/*User define variable to provide insurance*/\r\n"
				+ "and expense_type not like '%BED CHARGES%'\r\n"
				+ "and expense_type not like '%ADMISSION CHARGES%'\r\n"
				+ "and expense_type not like 'DISCOUNT%'\r\n"
				+ "and IE.expense_type NOT LIKE '%EXAM%'\r\n"
				+ "GROUP BY\r\n"
				+ "batch_id ,\r\n"
				+ "charges_id,\r\n"
				+ "expense_text3,\r\n"
				+ "IE.expense_date\r\n"
				+ "HAVING\r\n"
				+ "`change` <> 'D'\r\n"
				+ "and expense_type <> 'C' /*To delete Unwanted data */\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE EM.exam_subcat\r\n"
				+ "END AS item_name,\r\n"
				+ "IE.expense_date ,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> ''\r\n"
				+ "AND IE.expense_amount > '0' THEN IA.exp_type\r\n"
				+ "WHEN IE.expense_amount < '0' THEN 'R'\r\n"
				+ "WHEN exam_lab like '%path%'\r\n"
				+ "or exam_lab like '%radio%'\r\n"
				+ "or exam_lab like '%usg%'\r\n"
				+ "or exam_lab like '%mri%'\r\n"
				+ "or exam_lab like '%lab%'\r\n"
				+ "or exam_lab like '%ct%' then 'E'\r\n"
				+ "WHEN exam_lab like '%visit%'\r\n"
				+ "or exam_lab like '%consult%' then 'C'\r\n"
				+ "WHEN exam_lab like '%m-ot%' then 'S'\r\n"
				+ "ELSE 'P'\r\n"
				+ "END as expense_type,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_id IS NOT NULL\r\n"
				+ "AND IA.new_item_id <> '' THEN IA.new_item_id\r\n"
				+ "ELSE IE.charges_id\r\n"
				+ "END AS item_id ,\r\n"
				+ "COALESCE(IA.`change`,\r\n"
				+ "'A') AS 'change' ,\r\n"
				+ "'0' as mrp,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_rate IS NOT NULL\r\n"
				+ "AND IA.new_rate <> '' THEN IA.new_rate\r\n"
				+ "ELSE EM.exam_rate\r\n"
				+ "END AS expense_per_item ,\r\n"
				+ "CASE WHEN IE.quantity >500 THEN 1 ELSE\r\n"
				+ "round(SUM(IE.quantity), 0) END AS Qty,\r\n"
				+ "'',\r\n"
				+ "'',\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE IE.expense_desc\r\n"
				+ "END AS expense_desc ,\r\n"
				+ "COALESCE(EM.package_time_period ,\r\n"
				+ "'0') as package_time /*provide days of minor/major surgery ELSE 0*/\r\n"
				+ "FROM\r\n"
				+ "ipd_expenses IE\r\n"
				+ "LEFT JOIN exam_master_4 EM ON\r\n"
				+ "IE.charges_id = EM.exam_code\r\n"
				+ "AND IE.expense_type like '%EXAM%'\r\n"
				+ "LEFT JOIN insurance_items_adjustment_list IA ON\r\n"
				+ "IE.charges_id = IA.item_id\r\n"
				+ "AND IE.expense_name = IA.item_name\r\n"
				+ "AND IA.insurance_name = '"+insurance+"' /*User define variable to provide insurance*/\r\n"
				+ "WHERE\r\n"
				+ "IE.ipd_id = '"+ipd_id+"'/*User define variable to provide insurance*/\r\n"
				+ "AND IE.expense_type like '%EXAM%'\r\n"
				+ "AND (IE.expense_date < '"+dep_date+"'/*User define variable to provide insurance*/\r\n"
				+ "and IE.charges_id > 1780)\r\n"
				+ "AND is_deleted <> '1'\r\n"
				+ "GROUP BY\r\n"
				+ "charges_id,\r\n"
				+ "expense_text3,\r\n"
				+ "IE.expense_date\r\n"
				+ "HAVING\r\n"
				+ "`change` <> 'D'\r\n"
				+ "and expense_type <> 'C' /*To delete Unwanted data */\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE EM.exam_subcat\r\n"
				+ "END AS item_name,\r\n"
				+ "IE.expense_date ,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> ''\r\n"
				+ "AND IE.expense_amount > '0' THEN IA.exp_type\r\n"
				+ "WHEN IE.expense_amount < '0' THEN 'R'\r\n"
				+ "ELSE upper(substring(exam_lab, 1, 1))\r\n"
				+ "END as expense_type,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_id IS NOT NULL\r\n"
				+ "AND IA.new_item_id <> '' THEN IA.new_item_id\r\n"
				+ "ELSE IE.charges_id\r\n"
				+ "END AS item_id ,\r\n"
				+ "COALESCE(IA.`change`,\r\n"
				+ "'A') AS 'change' ,\r\n"
				+ "'0' as mrp,\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_rate IS NOT NULL\r\n"
				+ "AND IA.new_rate <> '' THEN IA.new_rate\r\n"
				+ "ELSE EM.exam_rate\r\n"
				+ "END AS expense_per_item ,\r\n"
				+ "CASE WHEN IE.quantity >500 THEN 1 ELSE\r\n"
				+ "round(SUM(IE.quantity), 0) END AS Qty,\r\n"
				+ "'',\r\n"
				+ "'',\r\n"
				+ "CASE\r\n"
				+ "WHEN IA.`change` = 'U'\r\n"
				+ "AND IA.new_item_name IS NOT NULL\r\n"
				+ "AND IA.new_item_name <> '' THEN IA.new_item_name\r\n"
				+ "ELSE IE.expense_desc\r\n"
				+ "END AS expense_desc ,\r\n"
				+ "COALESCE(EM.package_time_period ,\r\n"
				+ "'0') as package_time /*provide days of minor/major surgery ELSE 0*/\r\n"
				+ "FROM\r\n"
				+ "ipd_expenses IE\r\n"
				+ "LEFT JOIN exam_master_"+ratetype+" EM ON\r\n"
				+ "IE.charges_id = EM.exam_code\r\n"
				+ "AND IE.expense_type like '%EXAM%'\r\n"
				+ "LEFT JOIN insurance_items_adjustment_list IA ON\r\n"
				+ "IE.charges_id = IA.item_id\r\n"
				+ "AND IE.expense_name = IA.item_name\r\n"
				+ "AND IA.insurance_name = '"+insurance+"' /*User define variable to provide insurance*/\r\n"
				+ "WHERE\r\n"
				+ "IE.ipd_id = '"+ipd_id+"'/*User define variable to provide insurance*/\r\n"
				+ "AND IE.expense_type like '%EXAM%'\r\n"
				+ "AND (IE.expense_date >= '"+dep_date+"'\r\n"
				+ "or IE.charges_id < 1780)\r\n"
				+ "AND is_deleted <> '1'\r\n"
				+ "GROUP BY\r\n"
				+ "charges_id,\r\n"
				+ "expense_text3,\r\n"
				+ "IE.expense_date\r\n"
				+ "HAVING\r\n"
				+ "`change` <> 'D'\r\n"
				+ "and expense_type <> 'C' /*To delete Unwanted data */\r\n"
				+ "ORDER By\r\n"
				+ "3,\r\n"
				+ "4,\r\n"
				+ "2,\r\n"
				+ "1 ) T\r\n"
				+ "group by\r\n"
				+ "1,\r\n"
				+ "2,\r\n"
				+ "3,\r\n"
				+ "4,\r\n"
				+ "5,\r\n"
				+ "6,\r\n"
				+ "7,\r\n"
				+ "8,\r\n"
				+ "9,\r\n"
				+ "10\r\n"
				+ "having\r\n"
				+ "Qty <> 0\r\n"
				+ "order by\r\n"
				+ "1,\r\n"
				+ "2,\r\n"
				+ "5";
		
          	System.out.println(Query);
				try {
					rs = statement.executeQuery(Query);

				} catch (SQLException sqle) {
					JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
							javax.swing.JOptionPane.ERROR_MESSAGE);
				} 
			
		return rs;
	}
	public ResultSet retrieveAllCashIPD_DATA(String ipd_id) { 
	
      	String q1 ="SELECT\r\n"
      			+ "	IE.expense_id,\r\n"
      			+ "	IE.expense_name,\r\n"
      			+ "	IE.expense_date ,\r\n"
      			+ "	case\r\n"
      			+ "		when IE.expense_amount < '0' then 'R'\r\n"
      			+ "		when expense_desc like '%consulta%' then 'C'\r\n"
      			+ "		when expense_desc like '%surgery%' && expense_type like \"%exam%\" then 'S'\r\n"
      			+ "		when expense_type like 'EXAM%'\r\n"
      			+ "		or expense_text3 like 'LAB%' or expense_type like 'Procedur%' \r\n"
      			+ "    	or expense_text3 like 'Procedur%' then 'T'\r\n"
      			+ "    	when expense_type like 'inj%'\r\n"
      			+ "		or expense_text3 like 'inj%' then 'I'\r\n"
      			+ "		when expense_type like 'Medic%'\r\n"
      			+ "		or expense_text3 like 'medic' then 'M'\r\n"
      			+ "		when expense_type = 'BED CHARGES' then 'B'\r\n"
      			+ "		else 'O'\r\n"
      			+ "	end as expense_type,    \r\n"
      			+ "	IE.charges_id,\r\n"
      			+ "	IE.expense_page_no,\r\n"
      			+ "	case \r\n"
      			+ "		   when (0 + IE.item_mrp)= '0' then (0 + IE.item_mrp)\r\n"
      			+ "		when (0 + IE.item_mrp)<(0 + IE.expense_per_item) then 'N/A'\r\n"
      			+ "		else IE.item_mrp\r\n"
      			+ "	end as mrp,\r\n"
      			+ "	IE.expense_per_item ,\r\n"
      			+ "	SUM(IE.quantity),\r\n"
      			+ "	round(SUM(IE.expense_amount), 2),\r\n"
      			+ "	case\r\n"
      			+ "		when IE.batch_name is null then ''\r\n"
      			+ "		else IE.batch_name\r\n"
      			+ "	end as Batch,\r\n"
      			+ "	case\r\n"
      			+ "		when bt.item_expiry is null then bt2 .item_expiry\r\n"
      			+ "		else bt.item_expiry\r\n"
      			+ "	end as Expiry\r\n"
      			+ "FROM\r\n"
      			+ "	ipd_expenses IE\r\n"
      			+ "LEFT JOIN medicalstore_db.batch_tracking bt ON\r\n"
      			+ "	IE.batch_id = bt.id\r\n"
      			+ "	and expense_type = 'Medical Store'\r\n"
      			+ "LEFT JOIN batch_tracking bt2 on\r\n"
      			+ "	IE.batch_id = bt2.id\r\n"
      			+ "	and expense_type <> 'Medical Store'\r\n"
      			+ "WHERE\r\n"
      			+ "	IE.ipd_id = '"+ipd_id+"'\r\n"
      			+ "group by\r\n"
      			+ "	batch_id ,\r\n"
      			+ "	charges_id, \r\n"
      			+ "	expense_text3\r\n"
      			+ "ORDER By\r\n"
      			+ "	4,\r\n"
      			+ "	3,\r\n"
      			+ "	2";
      	System.out.println(q1);
			try {
				rs = statement.executeQuery(q1);

			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
		
	return rs;
}
	public ResultSet retrieveAllDataFull1(String ipd_id,String dateFrom) { 
		new DateFormatChange();
		String dateTo= DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `expense_id`, `expense_name`,`expense_per_item`, `quantity`, `expense_amount`,`charges_id`,`expense_date`, `expense_time`,`batch_name`,`batch_id`,`med_source`,`expense_type` FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"' AND`expense_type`!='Medical Store' AND `expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataFull2(String ipd_id,String dateFrom) { 
		new DateFormatChange();
		String dateTo= DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `expense_id`, `expense_name`,`expense_per_item`, `quantity`, `expense_amount`,`charges_id`,`expense_date`, `expense_time`  FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"' AND `expense_type`='Medical Store' AND `expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataExam(String ipd_id,String dateFrom) { 
		new DateFormatChange();
		String dateTo= DateFormatChange.StringToMysqlDate(new Date());
		String query = "SELECT `expense_id`, `expense_name`,`expense_per_item`, `quantity`, `expense_amount`,`charges_id`,`expense_date`, `expense_time`,`expense_type`, `expense_text3` FROM `ipd_expenses` WHERE `ipd_id`='"+ipd_id+"' AND  `expense_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `expense_type` LIKE '%EXAM%'";
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
				.prepareStatement("DELETE FROM ipd_expenses WHERE `expense_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int deleteRow(String ipd_id,String end) throws Exception {
		String SQL = "DELETE FROM ipd_expenses WHERE expense_text3='"+end+"' and ipd_id='"+ipd_id+"'";
		System.out.println(SQL);
		statement.executeUpdate(SQL);
		return 1;
		
	}
	public void inserInsuranceData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `Insurance_Tracking_list`(`ipd_id`,`p_id`,`p_name`,`item_name`,`item_desc`,`date`, `type`, `item_id`,`page_no`,`mrp`, `per_item_price`, `qty`, `amount`,`batch`, `expiry`, `rebate`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 17; i++) {

			preparedStatement.setString(i, data[i - 1]);
			
		}

		preparedStatement.executeUpdate();
	}
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`item_risk_type`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 16; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserDataNewExpenses(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`return_handling_charges`,`item_mrp`,`item_meas_unit`,`item_risk_type`,`batch_id`,`batch_name`,`med_source`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 1; i <= 21; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserDataReturnExpenses(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`return_handling_charges`,`item_mrp`,`item_meas_unit`,`item_risk_type`,`batch_id`,`batch_name`,`med_source`,return_item_reason) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		
		for (int i = 1; i <= 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserData1(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`expense_page_no`,`med_source`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 17; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserDataTableName(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`expense_page_no`,`med_source`,`from_table`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserData2(String[] data) throws Exception {
		//		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`expense_procedure_name`,`item_risk_type`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		String insertSQL = "INSERT INTO `ipd_expenses`(`ipd_id`, `expense_name`, `expense_desc`, `expense_amount`, `expense_date`, `expense_time`,`charges_id`, `expense_text1`, `expense_text2`, `expense_per_item`, `quantity`, `expense_text3`, `expense_text4`,`expense_type`,`expense_procedure_name`,`item_risk_type`,`item_mrp`,`item_meas_unit`,`batch_id`,`batch_name`,`med_source`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
