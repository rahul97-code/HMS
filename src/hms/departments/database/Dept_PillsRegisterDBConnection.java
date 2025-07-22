package hms.departments.database;

import hms.main.DBConnection;
import hms.test.gui.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.JOptionPane;

public class Dept_PillsRegisterDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public Dept_PillsRegisterDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public void updateStatus(String ipd_id,String newIndex) throws Exception
	  {
		
		System.out.println("UPDATE `dept_pills_register` SET `text1`='OK',`ipd_or_misc_number`='"+newIndex+"' WHERE `text1`='PENDING' AND `ipd_or_misc_number`='"+ipd_id+"'");
		statement.executeUpdate("UPDATE `dept_pills_register` SET `text1`='OK',`ipd_or_misc_number`='"+newIndex+"' WHERE `text1`='PENDING' AND `ipd_or_misc_number`='"+ipd_id+"'");
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
	public ResultSet retrieveAllPendingPills() {

		
		String query = "SELECT `ipd_or_misc_number`, `p_id`, `p_name`, `dept_name`, `date`,`text2` FROM dept_pills_register WHERE `text1`='PENDING' AND `ipd_or_misc_number`!='' GROUP BY `ipd_or_misc_number`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveConsumables(String dateFrom, String dateTo,String dept,String itemID) { 
		String query = "SELECT  `item_qty` FROM `dept_pills_register` WHERE `dept_name`='"+dept+"' AND `item_id`='"+itemID+"' AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveConsumablesWithDept(String dateFrom, String dateTo) { 
		String query = "SELECT `dept_id`as 'Dept ID', `dept_name` as 'Dept Name',COUNT(`item_id`) as 'Total Entries', `item_id` as 'Item ID',`item_name` as 'Item Name', `item_desc` as 'Item Desc', `item_price` as 'Item Price', SUM(`item_qty`) as 'Total Quantity', `enry_type` as 'Entry For' FROM `dept_pills_register` WHERE `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' GROUP BY `item_id` ORDER BY `dept_pills_register`.`dept_name`  ASC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
public ResultSet retrieveAllPendingPillsDetail(String ipd_id) {

		
		String query = "SELECT `item_id`, `item_name`, `item_desc`,  `item_qty`, `item_price`,`total_price` FROM dept_pills_register WHERE `text1`='PENDING' AND  `ipd_or_misc_number`='"+ipd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveConsumables(String dateFrom, String dateTo) { 
		String query = "SELECT R.`dept_name` AS 'DEPARTMENT NAME',R.`item_id` AS 'ITEM ID', R.`item_name` AS 'ITEM NAME', R.`item_desc` AS 'ITEM DESC.', SUM(R.`item_qty`) AS 'TOTAL QTY. CONSUMED',COUNT(R.`doctor_name`) AS 'NO. OF USES',S.`total_stock` AS 'CURRENT STOCK' FROM `dept_pills_register` R LEFT JOIN `department_stock_register` S ON R.`item_id`=S.`item_id` WHERE R.`dept_id`=S.`department_id`  AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'  GROUP BY R.`item_id` ORDER BY R.`dept_name`  ASC";
		//String query = "SELECT R.`dept_name` AS 'DEPARTMENT NAME',R.`item_id` AS 'ITEM ID', R.`item_name` AS 'ITEM NAME', R.`item_desc` AS 'ITEM DESC.',R.`item_price` AS 'ITEM PRICE', SUM(R.`item_qty`) AS 'TOTAL QTY. CONSUMED', SUM(R.`total_price`) AS 'TOTAL COST',S.`total_stock` AS 'CURRENT STOCK' FROM `dept_pills_register` R LEFT JOIN `department_stock_register` S ON R.`item_id`=S.`item_id` WHERE R.`dept_id`=S.`department_id`  AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'  GROUP BY R.`item_id` ORDER BY R.`dept_name`  ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	
	public ResultSet retrieveWardWiseDays(String dateFrom, String dateTo) { 

		SimpleDateFormat month_date = new SimpleDateFormat("MM", Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String actualDate = dateTo;

		Date date = null;
		try {
			date = sdf.parse(actualDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String month_name = month_date.format(date);
		String query = "SELECT `ipd_ward`, SUM(if(month(`ipd_entry_date`)="+month_name+",DATEDIFF(if(`ipd_discharge_date`='0000-00-00','"+dateTo+"',`ipd_discharge_date`),`ipd_entry_date`),DATEDIFF(if(`ipd_discharge_date`='0000-00-00','"+dateTo+"',`ipd_discharge_date`),'"+dateFrom+"'))+1) as `Days` FROM`ipd_doctor_incharge` D INNER JOIN `ipd_entery` I ON D.`ipd_id` = I.`ipd_id` WHERE `ipd_entry_date` <='"+dateTo+"' AND ((`ipd_discharge_date`>='"+dateFrom+"' AND `ipd_discharge_date`<='"+dateTo+"') OR `ipd_discharge_date`='0000-00-00') AND `ipd_entry_date` >='"+addMonths(dateTo)+"' GROUP BY `ipd_ward`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDoctorWiseDays(String dateFrom, String dateTo) {

		SimpleDateFormat month_date = new SimpleDateFormat("MM", Locale.ENGLISH);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		String actualDate = dateTo;

		Date date = null;
		try {
			date = sdf.parse(actualDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String month_name = month_date.format(date);
		String query = "SELECT `doctor_name`, SUM(if(month(`ipd_entry_date`)="+month_name+",DATEDIFF(if(`ipd_discharge_date`='0000-00-00','"+dateTo+"',`ipd_discharge_date`),`ipd_entry_date`),DATEDIFF(if(`ipd_discharge_date`='0000-00-00','"+dateTo+"',`ipd_discharge_date`),'"+dateFrom+"'))+1) as `Days` FROM`ipd_doctor_incharge` D INNER JOIN `ipd_entery` I ON D.`ipd_id` = I.`ipd_id` WHERE `ipd_entry_date` <='"+dateTo+"' AND ((`ipd_discharge_date`>='"+dateFrom+"' AND `ipd_discharge_date`<='"+dateTo+"') OR `ipd_discharge_date`='0000-00-00') AND `ipd_entry_date` >='"+addMonths(dateTo)+"' GROUP BY `doctor_name`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientWiseDays(String dateFrom, String dateTo) {


		
		String query = "SELECT `ipd_id`, `ipd_id2`, `p_id`, `p_name`, `ipd_entry_date`, if(`ipd_discharge_date`='0000-00-00','Not Discharged',`ipd_discharge_date`) as 'Discharge Date',DATEDIFF(if(`ipd_discharge_date`='0000-00-00','"+dateTo+"',`ipd_discharge_date`),`ipd_entry_date`) as `Days` FROM `ipd_entery`  WHERE `ipd_entry_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"';";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveTopItems(String dateFrom, String dateTo) {


		
		String query = "SELECT D.`item_id` 	AS 'ITEM ID', D.`item_name` AS 'ITEM NAME', D.`item_desc` AS 'ITEM DESC', SUM(D.`total_price`) AS 'TOTAL SALE' , SUM(D.`item_qty`) AS 'TOTAL QTY',I.`item_purchase_price` AS 'PURCHASE PRICE', I.`item_prevouse_price` AS 'PREVIOUSE PRICE', I.`item_total_stock` AS 'CURRENT STOCK' FROM `dept_pills_register` D JOIN `items_detail` I ON D.`item_id`=I.`item_id`  WHERE `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' GROUP BY D.`item_id` ORDER BY SUM(D.`item_qty`) DESC LIMIT 20;";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public String  addMonths(String dateAsString)  {
        String format = "yyyy-MM-dd" ;
        SimpleDateFormat sdf = new SimpleDateFormat(format) ;
        Date dateAsObj = null;
		try {
			dateAsObj = sdf.parse(dateAsString);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        Calendar cal = Calendar.getInstance();
        cal.setTime(dateAsObj);
        cal.add(Calendar.MONTH, -2);
        Date dateAsObjAfterAMonth = cal.getTime() ;
        return sdf.format(dateAsObjAfterAMonth).toString() ;
	}
	public ResultSet retrievePillsToPatients(String dateFrom, String dateTo,String type,String itemID) { 
		String query = "SELECT `item_price`,`item_qty`,`total_price` FROM `dept_pills_register` WHERE `item_id`='"+itemID+"' AND `enry_type`='"+type+"' AND `date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public int inserDepartmentPillsRegister(String dept_id,String dept_name,String test_id,String test_name,String patient_id,String patient_name,String date,String time) throws Exception {
		int affectedRows =statement
		.executeUpdate("INSERT INTO `dept_pills_register`(`dept_id`, `dept_name`, `doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`) SELECT '"+dept_id+"','"+dept_name+"','"+test_id+"', `exam_name`,'1','"+Test.exam_operator+"','"+patient_id+"','"+patient_name+"',`item_id`, `item_name`, `item_desc`,(SELECT `item_purchase_price` FROM `items_detail` WHERE `item_id`=E.`item_id`), `item_qty`,(SELECT `item_purchase_price` FROM `items_detail` WHERE `item_id`=E.`item_id`)*`item_qty`, '"+date+"', '"+time+"', '"+test_id+"','EXAM' FROM `exam_bom` E WHERE `exam_name`='"+test_name+"'");
		return affectedRows;
	}
	
	public int inserDepartmentPillsRegister(String dept_id,String dept_name,String test_id,String patient_id,String patient_name,String date,String time) throws Exception {
		int affectedRows =statement
		.executeUpdate("INSERT INTO `dept_pills_register`(`dept_id`, `dept_name`, `doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`) SELECT '"+dept_id+"','"+dept_name+"',T.`exam_counter`, `exam_name`,'1','"+Test.exam_operator+"','"+patient_id+"','"+patient_name+"',`item_id`, `item_name`, `item_desc`,(SELECT `item_purchase_price` FROM `items_detail` WHERE `item_id`=E.`item_id`), `item_qty`,(SELECT `item_purchase_price` FROM `items_detail` WHERE `item_id`=E.`item_id`)*`item_qty`, '"+date+"', '"+time+"', T.`exam_counter`,'EXAM' FROM `exam_bom` E JOIN  `test_results` T ON E.`exam_name`=T.`exam_sub_name` WHERE T.`exam_counter`='"+test_id+"'");
		return affectedRows;
	}
	public int insertDepartmentPillsRegister(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `dept_pills_register`(`dept_id`, `dept_name`, `doctor_id`, `doctor_name`, `user_id`, `user_name`, `p_id`, `p_name`, `item_id`, `item_name`, `item_desc`, `item_price`, `item_qty`, `total_price`, `date`, `time`, `ipd_or_misc_number`, `enry_type`, `text1`,`batch_name`,`batch_id`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);  
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {
			preparedStatement.setString(i, data[i-1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 return  rs.getInt(1);
	}
}
