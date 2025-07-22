package hms.reception.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class TokenDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public TokenDBConnection() {

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

	public ResultSet retrieveDataCounters() {
		String query = "SELECT `_id`,`reception_type` FROM `reception_counter` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public boolean checkToken(String id,String tokenNumber,String token_type) {
		boolean flag = false;
		//int maxIndex=retrieveMaxIndex(id);
		String tokenNumberSTR="0";
		String tokenTypeSTR="0";
		String query = "SELECT `token_no`,`counter_id` FROM `reception_counter_display` Where `text`='"+id+"' AND `date`=CAST(CURRENT_TIMESTAMP AS DATE) AND `checked`=0 ORDER BY `indexing` LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				
				tokenNumberSTR=rs.getString(1);
				tokenTypeSTR=rs.getString(2);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(tokenNumberSTR.equals(tokenNumber)&&tokenTypeSTR.equals(token_type))
		{
			flag = true;
		}
		return flag;
	}
	public void unReported(String id) {
		String tokenNumberSTR="0";
		String tokenTypeSTR="0";
		String query = "SELECT `counter_id`,`token_no` FROM `reception_counter_display` Where `text`='"+id+"' AND `date`=CAST(CURRENT_TIMESTAMP AS DATE) AND `checked`=0 ORDER BY `indexing` LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				tokenTypeSTR=rs.getString(1);
				tokenNumberSTR=rs.getString(2);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		query="UPDATE `reception_counter_display` SET `checked`=2 WHERE `counter_id`="
				+ tokenTypeSTR
				+ " AND `token_no`="
				+ tokenNumberSTR
				+ " AND `date`= CAST(CURRENT_TIMESTAMP AS DATE)";

		try {
			statement
					.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	
	}
	public ResultSet retrieveDisplay(String id) {
		int maxIndex=retrieveMaxIndex(id);
		String query = "SELECT `counter_name`,`token_no` FROM `reception_counter_display` Where `text`='"+id+"' AND  `date`=CAST(CURRENT_TIMESTAMP AS DATE) AND `checked`=0  AND `indexing`>"+maxIndex+" ORDER BY `indexing` LIMIT 3";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int retrieveMaxIndex(String id) {
		String query = "SELECT MAX(`indexing`) FROM `reception_counter_display` where `text`='"+id+"' AND `checked`=1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int counter=0;
		try {
			while (rs.next()) {
				counter=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}
	public ResultSet retrieveCounterDetail(String id) {
		String query = "SELECT `_id`, `reception_type`, `total`, `counter`,`date` FROM `reception_counter` WHERE `_id`="
				+ id;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public boolean retrieveUpdated(String counter_id,String type_id, String counter) {
		boolean flag = false;
		String query = "SELECT  *  FROM `reception_counter_display` WHERE `text`="
				+ counter_id
				+ " AND `counter_id`="
				+ type_id
				+ " AND `token_no`='"
				+ counter
				+ "' AND `checked`=1 AND `date`=CAST(CURRENT_TIMESTAMP AS DATE)";

		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}

		try {
			while (rs.next()) {
				flag = true;

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public void incrementDataCounter(String id,String setValue) throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter` SET `counter`='"+setValue+"' where `_id` = "
						+ id + " AND `counter`<`total`");
	}

	public void updateDateCounter(String id) throws Exception {

		statement
				.executeUpdate("update `reception_counter` set `date` = CAST(CURRENT_TIMESTAMP AS DATE),`total`=0,`counter`=0  WHERE `_id` = "
						+ id + " AND `date` < CAST(CURRENT_TIMESTAMP AS DATE)");

	}

	public void updateDate(String id) throws Exception {

		statement
				.executeUpdate("update `reception_counter` set `date` = CAST(CURRENT_TIMESTAMP AS DATE),`total`=0,`counter`=0  WHERE `_id` = "
						+ id + " AND `date` < CAST(CURRENT_TIMESTAMP AS DATE)");
		updateDataCounter(id);

	}

	public ResultSet retrieveUnreported(String id) {
		String query = "SELECT A.`token_no`,A.`checked`,B.`total` FROM `reception_counter_display` A INNER JOIN `reception_counter` B ON A.`counter_id`=B.`_id` AND B.`_id`="
				+ id
				+ " AND A.`date`=CAST(CURRENT_TIMESTAMP AS DATE) AND A.`checked` IN('0','3') AND A.`token_no`>B.`counter`-10 LIMIT 5";
		
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public String retrieveFirstNumber(String id) {
		
		String number="0";
		String query = "SELECT A.`token_no`,A.`checked`,B.`total` FROM `reception_counter_display` A INNER JOIN `reception_counter` B ON A.`counter_id`=B.`_id` AND B.`_id`="
				+ id
				+ " AND A.`date`=CAST(CURRENT_TIMESTAMP AS DATE) AND A.`checked` IN('0','3') AND A.`token_no`>B.`counter`-10 LIMIT 1";
		
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		
		try {
			while (rs.next()) {
				
				number=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return number;
	}

	public void updateDataCounter(String id) throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter` SET `counter`=`counter`+1 where `_id` = "
						+ id + " AND `counter`<`total`");
	}

	public void updateDataCounterDec(String id) throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter` SET `counter`=`counter`-1 where `_id` = "
						+ id + " AND `counter`>1");
	}

	public void updateDataScreenData(String id, String counter,String token_type)
			throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter_display` SET `checked`=1 WHERE `counter_id`="
						+ token_type
						+ " AND `text`="
						+ id
						+ " AND `token_no`="
						+ counter
						+ " AND `date`= CAST(CURRENT_TIMESTAMP AS DATE)");
	}
	public void updateDataCounter(String id, String counter) throws Exception {

		statement.executeUpdate("UPDATE `reception_counter` SET `counter`='"
				+ counter + "' where `_id` = " + id + "");
	}
	public void CancelCounter(String id, String counter)
			throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter_display` SET `checked`=0 WHERE `counter_id`="
						+ id
						+ " AND `token_no`="
						+ counter
						+ " AND `date`= CAST(CURRENT_TIMESTAMP AS DATE)");
	}
	
	public void updateDataScreenDataStatus(String id, String counter)
			throws Exception {

		statement
				.executeUpdate("UPDATE `reception_counter_display` SET `checked`=3 WHERE `counter_id`="
						+ id
						+ " AND `token_no`="
						+ counter
						+ " AND `date`= CAST(CURRENT_TIMESTAMP AS DATE)");
	}
	
	public void updateDataScreenDataIncrese(String id, String counter)
			throws Exception {
		String query="UPDATE `reception_counter_display` SET `checked`=2 WHERE `counter_id`="
				+ id
				+ " AND `token_no`="
				+ counter
				+ " AND `date`= CAST(CURRENT_TIMESTAMP AS DATE)";
		System.out.println(query);
		statement
				.executeUpdate(query);
		
	}
}
