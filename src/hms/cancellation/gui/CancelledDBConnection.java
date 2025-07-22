package hms.cancellation.gui;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class CancelledDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public CancelledDBConnection() {

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
	
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `can_cat`, `p_id`, `p_name`, `reciept_no`, `can_amount`, `cancelled_by`,`generate_date`, `date`, `time`, `remarks` FROM `cancel_detail` WHERE date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAmount(String dateFrom, String dateTo,String cat)
	{
	  String query="SELECT `can_amount` FROM `cancel_detail` WHERE date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `can_cat`='"+cat+"'";
//	  System.out.println("Cancel Query"+query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAmountInsurance(String insuranceType,String dateFrom, String dateTo)
	{
	  String query="SELECT C.`can_cat`, COUNT(C.`can_amount`),COALESCE(SUM(C.`can_amount`),0) FROM `cancel_detail` C,`patient_detail` P WHERE C.p_id=P.pid1 AND C.date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND P.`p_insurancetype`='"+insuranceType+"' GROUP BY C.`can_cat`";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM cancel_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void UpdateStatusOPD(String receipt_id) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("UPDATE opd_entery SET opd_status='CANCEL' WHERE opd_id='"+receipt_id+"' ");
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `cancel_detail`(`can_cat`, `p_id`, `p_name`, `reciept_no`, `can_amount`, `cancelled_by`,`generate_date`, `date`, `time`, `remarks`) VALUES (?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
