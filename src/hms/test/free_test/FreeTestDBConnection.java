package hms.test.free_test;

import hms.main.DBConnection;
import hms.main.DateFormatChange;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class FreeTestDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public FreeTestDBConnection() {

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
	public void updateData(String[] data) throws Exception {
		
		
		String insertSQL = "UPDATE `free_test_table` SET `test_done`=?,`test_results`=?,`test_remarks`=?,`test_by`=?,`test_date`=?,`test_time`=? WHERE `id`="+data[6];

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 7; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateData1(String[] data) throws Exception {
		
		
		String insertSQL = "UPDATE `free_test_table` SET `contact_by`=?,`contact_date`=?,`contact_status`=?,`contact_to`=? WHERE `id`="+data[4];

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	
	
	public ResultSet retrieveAllData() {
		String query = "SELECT `test_id`, `test_type`, `sort_name`, `units`, `min_value`, `max_value`, `start_date`, `end_date`, `entry_user` FROM `free_test_master` WHERE 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllFreeTest(String date) {
		String query = "SELECT `test_id`, `test_type`, `sort_name`,`units`,`start_date` FROM `free_test_master` WHERE `active`='1' AND `start_date` <='"+date+"' AND `end_date` >='"+date+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet checkEligibilty(String p_id,String test_id,String date) {
		String query = "SELECT COUNT(*) FROM `free_test_table` WHERE `p_id`='"+p_id+"' AND `test_type_id`="+test_id+" AND `entry_date`>='"+date+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	

	public ResultSet getData(String testName,String dateFrom,String dateTo,String testStatus,String conctactStatus,String rangeMin,String rangeMax) {
		String query ;
		if(testStatus.equals("Yes"))
		{
			 query = "SELECT F.`id`, F.`opd_id`,F.`test_type`, concat(F.`test_results`, ' ', F.`test_results_units`) ,  F.`p_id`, F.`p_name`,P.`p_telephone`,P.`p_addresss`, F.`entry_date`, F.`test_done`, F.`contact_status` ,F.`contact_by`,  F.`contact_to`,  IF(F.`contact_date` = '1111-11-11', '', F.`contact_date`) FROM `free_test_table` F LEFT JOIN `patient_detail` P ON F.`p_id`=P.`pid1` WHERE F.`test_type`='"+testName+"' AND F.`entry_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND F.`test_done`='"+testStatus+"' AND F.`contact_status`='"+conctactStatus+"' AND F.`test_results`>="+rangeMin+" AND F.`test_results`<="+rangeMax+"";

		}
		else if(testStatus.equals("NO")){
			
			 query = "SELECT F.`id`, F.`opd_id`,F.`test_type`, concat(F.`test_results`, ' ', F.`test_results_units`) ,  F.`p_id`, F.`p_name`,P.`p_telephone`,P.`p_addresss`, F.`entry_date`, F.`test_done`, F.`contact_status` ,F.`contact_by`,  F.`contact_to`,  IF(F.`contact_date` = '1111-11-11', '', F.`contact_date`) FROM `free_test_table` F LEFT JOIN `patient_detail` P ON F.`p_id`=P.`pid1` WHERE F.`test_type`='"+testName+"' AND F.`entry_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND F.`test_done`='"+testStatus+"' AND F.`contact_status`='"+conctactStatus+"'";
		}
		else {
			
			 query = "SELECT F.`id`, F.`opd_id`,F.`test_type`, concat(F.`test_results`, ' ', F.`test_results_units`) ,  F.`p_id`, F.`p_name`,P.`p_telephone`,P.`p_addresss`, F.`entry_date`, F.`test_done`, F.`contact_status` ,F.`contact_by`,  F.`contact_to`,  IF(F.`contact_date` = '1111-11-11', '', F.`contact_date`) FROM `free_test_table` F LEFT JOIN `patient_detail` P ON F.`p_id`=P.`pid1` WHERE F.`test_type`='"+testName+"' AND F.`entry_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND F.`contact_status`='"+conctactStatus+"'";
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
	
	public ResultSet retrieveDataWithOPD(String opdID) {
		String query = "SELECT `id`, `test_type_id`, `test_type`, `test_type_short`, `opd_id`, `p_id` FROM `free_test_table` WHERE `opd_id` = '"
				+ opdID+"' AND `test_done`= 'NO'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDataWithID(String id) {
		String query = "SELECT `id`, `test_type_id`, `test_type`, `test_type_short`, `opd_id`, `p_id`,`test_results_units` FROM `free_test_table` WHERE `id` = "
				+ id+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDataWithName(String name) {
		String query = "SELECT `test_id`, `test_type`, `sort_name`, `units`, `min_value`, `max_value`, `start_date`, `end_date`, `entry_user` FROM `free_test_master` WHERE `test_type`='"
				+ name+"'";
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
				.prepareStatement("DELETE FROM inventory WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `free_test_table`(`test_type_id`, `test_type`,`test_type_short`, `opd_id`, `p_id`, `p_name`, `entry_date`, `entry_time`, `entry_user`, `test_results_units`) VALUES (?,?,?,?,?,?,?,?,?,?)";
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
