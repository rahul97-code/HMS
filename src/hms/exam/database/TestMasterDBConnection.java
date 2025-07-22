package hms.exam.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class TestMasterDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public TestMasterDBConnection() {

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
	public void updateData(String[] data,String tableName) throws Exception
	  {
		String insertSQL     = "UPDATE `"+tableName+"` SET `exam_desc`=?,`exam_subcat`=?,`exam_lab`=? ,`exam_room`=?, `exam_operator`=?,`exam_rate`=?, `exam_text1`=?,`lis_mapping_code`=? WHERE `exam_code` = '"+data[8]+"'";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <9; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	public ResultSet retrieveAllDataExamID(String index,String tableName)
	{
	  String query="SELECT `exam_desc`,`exam_subcat`, `exam_lab`, `exam_room`, `exam_operator`,`exam_rate`, `exam_text1`,`lis_mapping_code` FROM `"+tableName+"` WHERE `exam_code` = "+index;
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	
	public ResultSet retrieveALLMasterTable()
	{
	  String query="SHOW TABLES LIKE 'exam_master%'";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveAllData(String examCat)
	{
	  String query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_text1` FROM `exam_master` WHERE `exam_desc`= '"+examCat+"' ORDER BY `exam_subcat` ASC";
	  if(examCat.equals("All Exams"))
	  {
		  query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_text1` FROM `exam_master` ORDER BY `exam_subcat` ASC";
	  }
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData(String tableName,String examCat)
	{
	  String query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_text1` FROM "+tableName+" WHERE `exam_desc`= '"+examCat+"' ORDER BY `exam_subcat` ASC";
	  if(examCat.equals("All Exams"))
	  {
		  query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_text1` FROM "+tableName+" ORDER BY `exam_subcat` ASC";
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
	public ResultSet retrieveAllDataOrderBy(String tableName,String examCat)
	{
	  String query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab` FROM "+tableName+" WHERE `exam_desc`= '"+examCat+"' ORDER BY `exam_desc` ASC";
	  if(examCat.equals("All Exams"))
	  {
		  query="SELECT `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab` FROM "+tableName+" ORDER BY `exam_desc` ASC";
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
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM exam_entery WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	//`opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_type`
	  public int inserData(String[] data,String tableName) throws Exception
	  {
		  String insertSQL = "INSERT INTO `"+tableName+"`( `exam_code`,`exam_desc`,`exam_subcat`, `exam_lab`, `exam_room`, `exam_operator`,`exam_rate`, `exam_text1`,lis_mapping_code) VALUES (?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <10; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
			 ResultSet rs = preparedStatement.getGeneratedKeys();
			  rs.next();
			 
			 return  rs.getInt(1);
	  }
}
