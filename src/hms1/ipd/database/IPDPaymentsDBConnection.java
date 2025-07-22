package hms1.ipd.database;

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

public class IPDPaymentsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public IPDPaymentsDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public void updateCancellation(String payment_id) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `ipd_payments` SET `payment_text5`='CANCEL' WHERE `payment_id`= "+payment_id+"");
	  }
	public ResultSet retrieveData(String query) {
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
//			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
//					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `payment_amount` FROM `ipd_payments` WHERE `payment_type`='P' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmountInsurance(String insuranceType,String dateFrom, String dateTo) { 
		String query = "SELECT COUNT(I.`payment_amount`),COALESCE(SUM(I.`payment_amount`),0) FROM `ipd_payments` I,`ipd_entery` P WHERE P.ipd_id=I.ipd_id AND I.`payment_type`='P' AND I.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`payment_text5`='N/A' AND P.`insurance_type`='"+insuranceType+"'";
		try {
			System.out.println(""+query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllDataTrasactionAmountInsurance(String insuranceType,String dateFrom, String dateTo) { 
		String query = "SELECT P.pid1,P.p_name,I.`payment_amount` FROM `ipd_payments` I,`patient_detail` P WHERE P.pid1=I.p_id AND I.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`payment_text5`='N/A' AND P.`p_insurancetype`='"+insuranceType+"'";
		try {
			System.out.println("Advance:  "+query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmountExcel(String dateFrom, String dateTo) { 
		String query = "SELECT `payment_id`, `ipd_id`, `p_id`, `p_name`, `payment_desc`, `payment_amount`, `payment_date`, `payment_time`, `payment_entry_user`  FROM `ipd_payments` WHERE `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmountAll(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id`, `p_id`, `p_name`, `payment_desc`, `payment_amount`, `payment_date`, `payment_time`, `payment_entry_user` FROM `ipd_payments` WHERE `payment_type`='P' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmountUserWise(String dateFrom, String dateTo,String username) { 
		String query = "SELECT COUNT(`payment_amount`),COALESCE(SUM(`payment_amount`),0) FROM `ipd_payments` WHERE `payment_type`='P' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_entry_user`='" + username+ "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmountUserWise(String dateFrom, String dateTo,String username,String insurance) { 
		String query = "SELECT COUNT(I.`payment_amount`),COALESCE(SUM(I.`payment_amount`),0) FROM `ipd_payments` I,`ipd_entery` P WHERE P.ipd_id=I.ipd_id AND I.`payment_type`='P' AND I.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`payment_entry_user`='" + username+ "' AND I.`payment_text5`='N/A' AND P.`insurance_type`='"+insurance+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	

	public ResultSet retrieveAllDataAdvanceAmountPatient(String p_name,String dateFrom) { 
		
		@SuppressWarnings("static-access")
		String dateTo= new DateFormatChange().StringToMysqlDate(new Date());
		String query = "SELECT `payment_amount` FROM `ipd_payments` WHERE `payment_type`='P' AND `p_id`='"+p_name+"' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCancelledDataAdvanceAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `payment_amount` FROM `ipd_payments` WHERE `payment_type`='P' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_text5`!='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllDataReturnedAmountUserWise(String dateFrom, String dateTo,String username) { 
		String query = "SELECT COUNT(`payment_amount`),COALESCE(SUM(`payment_amount`),0) FROM `ipd_payments` WHERE `payment_type`='R' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `payment_entry_user`='" + username + "' AND `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllDataReturnedAmountUserWise(String dateFrom, String dateTo,String username,String insurance) { 
		String query = "SELECT COUNT(I.`payment_amount`),COALESCE(SUM(I.`payment_amount`),0) FROM `ipd_payments` I,`ipd_entery` P WHERE P.ipd_id=I.ipd_id AND I.`payment_type`='R' AND I.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`payment_entry_user`='" + username + "' AND I.`payment_text5`='N/A' AND P.`insurance_type`='"+insurance+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataReturnedAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `payment_amount` FROM `ipd_payments` WHERE `payment_type`='R' AND `payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND  `payment_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataReturnedAmountInsurance(String insuranceType,String dateFrom, String dateTo) { 
		String query = "SELECT COUNT(I.`payment_amount`),COALESCE(SUM(I.`payment_amount`),0) FROM `ipd_payments` I,`ipd_entery` P WHERE P.ipd_id=I.ipd_id AND I.`payment_type`='R' AND I.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`payment_text5`='N/A' AND P.`insurance_type`='"+insuranceType+"'";
		System.out.println("iNDOOR : "+ query);
		try {
			
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String reciept_id) { 
		String query = "SELECT `p_id`, `p_name`, `payment_date`, `payment_amount`,`payment_id` FROM `ipd_payments` WHERE `payment_id`="+reciept_id+"";
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
				.prepareStatement("DELETE FROM ipd_payments WHERE `expense_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_payments`(`ipd_id`, `p_id`, `p_name`, `payment_type`, `payment_desc`, `payment_amount`, `payment_date`, `payment_time`, `payment_entry_user`)  VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 10; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserData1(String[] data) throws Exception {
		String insertSQL = "INSERT INTO karun_sparsh_record(ipd_id, uhid, p_name, contact, age, bill_amount, consultant_doc, receipt_id, relief_amount, approved_by, created_by,created_by_id)VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 13; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
}
