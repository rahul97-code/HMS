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

public class ExamBomDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ExamBomDBConnection() {
		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	public ResultSet retrieveData(String exam_name)
	{
	    String query="SELECT `po_item_id`, `item_id`, `item_name`, `item_desc`, `exam_name`, `item_qty`, `item_date`, `item_time`, `item_entry_user` FROM `exam_bom` WHERE `exam_name`='"+exam_name+"'";
	
	    try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveData()
	{
	    String query="SELECT `po_item_id` AS 'ID',`exam_name` AS 'EXAM NAME', `item_id` AS 'ITEM ID', `item_name` AS 'ITEM NAME',  `item_qty` AS 'ITEM QTY' FROM `exam_bom` WHERE 1 ORDER BY `exam_name` ASC";
	
	    try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void deleteItem(String procedureID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `exam_bom` WHERE `po_item_id`=?");
		preparedStatement.setString(1, procedureID);
		preparedStatement.executeUpdate();
	}
	
	public void deleteAllItem(String procedureID) throws Exception {
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `exam_bom` WHERE `po_item_id`=?");
		preparedStatement.setString(1, procedureID);
		preparedStatement.executeUpdate();
	}
	
	public int insertData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `exam_bom`( `item_id`, `item_name`, `item_desc`, `exam_name`, `item_qty`, `item_date`, `item_time`, `item_entry_user`) VALUES  (?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 9; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	}

}
