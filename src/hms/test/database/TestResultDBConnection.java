package hms.test.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class TestResultDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public TestResultDBConnection() {

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
	public void updateTableValue(String[] data,String ref_id) throws Exception
	  {
		System.out.print("upddd"+data[31]);
	
		String insertSQL     ="UPDATE `test_results` SET `phy_parameter1`= ?, `phy_parameter2`= ?, `phy_parameter3`= ?, `phy_parameter4`= ?, `phy_parameter5`= ?, `phy_parameter6`= ?, `phy_parameter7`= ?, " +
		  		"`phy_parameter8`= ?, `phy_parameter9`= ?, `phy_parameter10`= ?, `chem_parameter1`= ?, `chem_parameter2`= ?, `chem_parameter3`= ?, `chem_parameter4`= ?, `chem_parameter5`= ?, `chem_parameter6`= ?, `chem_parameter7`= ?," +
		  		" `chem_parameter8`= ?, `chem_parameter9`= ?, `chem_parameter10`= ?, `meh_parameter1`= ?, `meh_parameter2`= ?, `meh_parameter3`= ?, `meh_parameter4`= ?, `meh_parameter5`= ?, `meh_parameter6`= ?, `meh_parameter7`= ?, `meh_parameter8`= ?," +
		  		" `meh_parameter9`= ?, `meh_parameter10`= ?,`result_text1`=?  WHERE `result_id` = "+ref_id;
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <32; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void updateTestValue(String ref_id, String value,String result,String result_sign,String comment_exam)
			throws Exception {
		statement
				.executeUpdate("update `test_results` set `result_value` = '"
						+ value + "', `results1` = '"
						+ result + "',`result_sign`='"
						+ result_sign + "',`result_text1`='"
						+ comment_exam + "' where `result_id` = " + ref_id);
	}

	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
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
	public ResultSet retrieveAllData()
	{
	  String query="SELECT  `operator_id`, `operator_name`, `operator_labname`, `operator_opdroom`,`operator_username`,`operator_password`, `operator_qualification` FROM `test_operator_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllReportType()
	{
	  String query="SELECT * FROM `report_type` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDataWithTestID(String name)
	{
	  String query="SELECT `exam_sub_name`, `lower_limit`, `upper_limit`, `result_value`, `results1`, `results2`,`result_sign`,`result_text1` FROM `test_results` WHERE `exam_counter`='"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDataWithTestID2(String name)
	{
	  String query="SELECT `phy_parameter1`, `phy_parameter2`, `phy_parameter3`, `phy_parameter4`, `phy_parameter5`, `phy_parameter6`, `phy_parameter7`, " +
		  		"`phy_parameter8`, `phy_parameter9`, `phy_parameter10`, `chem_parameter1`, `chem_parameter2`, `chem_parameter3`, `chem_parameter4`, `chem_parameter5`, `chem_parameter6`, `chem_parameter7`," +
		  		" `chem_parameter8`, `chem_parameter9`, `chem_parameter10`, `meh_parameter1`, `meh_parameter2`, `meh_parameter3`, `meh_parameter4`, `meh_parameter5`, `meh_parameter6`, `meh_parameter7`, `meh_parameter8`," +
		  		" `meh_parameter9`, `meh_parameter10`, `exam_sub_name`,`result_text1`  FROM `test_results` WHERE `exam_counter`='"+name+"'";
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
	  String query="SELECT * FROM `test_operator_detail` WHERE `operator_username` = '"+name+"' AND `operator_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveCategory(String testCounter)
	{
	  String query="SELECT  `result_cat` FROM `test_results` WHERE `exam_counter` = '"+testCounter+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDATA(String testCounter,String examCat)
	{
	  String query="SELECT `result_id`,`result_value`,`result_sign`,`result_text1` FROM `test_results` WHERE `exam_counter`='"+testCounter+"' AND `exam_sub_name`='"+examCat+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDATA2(String testCounter,String examCat)
	{
	  String query="SELECT `result_id`,`phy_parameter1`, `phy_parameter2`, `phy_parameter3`, `phy_parameter4`, `phy_parameter5`, `phy_parameter6`, `phy_parameter7`, " +
		  		"`phy_parameter8`, `phy_parameter9`, `phy_parameter10`, `chem_parameter1`, `chem_parameter2`, `chem_parameter3`, `chem_parameter4`, `chem_parameter5`, `chem_parameter6`, `chem_parameter7`," +
		  		" `chem_parameter8`, `chem_parameter9`, `chem_parameter10`, `meh_parameter1`, `meh_parameter2`, `meh_parameter3`, `meh_parameter4`, `meh_parameter5`, `meh_parameter6`, `meh_parameter7`, `meh_parameter8`," +
		  		" `meh_parameter9`, `meh_parameter10`,`result_text1` FROM `test_results` WHERE `exam_counter`='"+testCounter+"' AND `exam_sub_name`='"+examCat+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	
	//SELECT `exam_sub_name`, `lower_limit`, `upper_limit`, `result_value`, `results2` FROM `test_results` WHERE 1
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM test_results WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `test_results`( `exam_counter`, `examcat_id`, `exam_sub_name`, `lower_limit`, `upper_limit`, `result_value`, `comments`,"+
		  		" `results1`, `results2`, `result_cat`,`result_sign`,`result_text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <13; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void inserData2(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `test_results`(  `phy_parameter1`, `phy_parameter2`, `phy_parameter3`, `phy_parameter4`, `phy_parameter5`, `phy_parameter6`, `phy_parameter7`, " +
		  		"`phy_parameter8`, `phy_parameter9`, `phy_parameter10`, `chem_parameter1`, `chem_parameter2`, `chem_parameter3`, `chem_parameter4`, `chem_parameter5`, `chem_parameter6`, `chem_parameter7`," +
		  		" `chem_parameter8`, `chem_parameter9`, `chem_parameter10`, `meh_parameter1`, `meh_parameter2`, `meh_parameter3`, `meh_parameter4`, `meh_parameter5`, `meh_parameter6`, `meh_parameter7`, `meh_parameter8`," +
		  		" `meh_parameter9`, `meh_parameter10`, `exam_counter`, `examcat_id`, `exam_sub_name`,`result_cat`,`result_text1`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <36; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
