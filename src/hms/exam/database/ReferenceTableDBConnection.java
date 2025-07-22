package hms.exam.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class ReferenceTableDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ReferenceTableDBConnection() {

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
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL     = "";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <11; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	
	public ResultSet retrieveReferenceRange(String exam_id,String patient_sex,String patient_age)
	{
		
	  String query="SELECT `ref_lowerlimit`, `ref_upperlimit`, `ref_units`, `ref_comments` FROM `test_reference` WHERE `exam_id` = '"+exam_id+"' AND `ref_agemin`<="+patient_age+" AND `ref_agemax`>="+patient_age+" AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet searchTestName(String testName,String patient_sex,String patient_age,String p_type)
	{
	  //String query="SELECT DISTINCT(`ref_testname`) FROM `test_reference` WHERE `ref_testname` LIKE '%"+testName+"%' AND `ref_patienttype` = '"+p_type+"' AND `ref_agemin`<="+patient_age+" AND `ref_agemax`>="+patient_age+" AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  String query="SELECT DISTINCT(`ref_testname`) FROM `test_reference` WHERE `ref_testname` LIKE '%"+testName+"%' AND `ref_patienttype` = '"+p_type+"' AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
		 
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchTestName(String testName)
	{
	  String query="SELECT DISTINCT(`ref_testname`) FROM `test_reference` WHERE `ref_testname` LIKE '%"+testName+"%'";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchTestSubName(String testName)
	{
	  String query="SELECT DISTINCT(`ref_testsubname`) FROM `test_reference` WHERE `ref_testname` LIKE '%"+testName+"%'";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveReferenceRangeIndexNew(String testName,String patient_sex,String patient_age,String p_type)
	{
	  String query="SELECT `ref_id` FROM `test_reference` WHERE `ref_testname` = '"+testName+"' AND `ref_patienttype` = '"+p_type+"' AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveReferenceRangeIndex(String exam_id,String patient_sex,String patient_age,String p_type)
	{
	  String query="SELECT `ref_id` FROM `test_reference` WHERE `exam_id` = '"+exam_id+"' AND `ref_patienttype` = '"+p_type+"' AND `ref_agemin`<="+patient_age+" AND `ref_agemax`>="+patient_age+" AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet searchTestName2(String testName,String patient_sex,String patient_age,String p_type)
	{
	  String query="SELECT DISTINCT(`ref_testname`) FROM `test_output_reference` WHERE  `ref_testname` LIKE '%"+testName+"%' AND `ref_patienttype` = '"+p_type+"' AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchTestName2(String testName)
	{
		  String query="SELECT DISTINCT(`ref_testname`) FROM `test_output_reference` WHERE  `ref_testname` LIKE '%"+testName+"%'";
		  try {
				rs = statement.executeQuery(query);
				
				
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
				javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
			return rs;
		}
	public ResultSet searchTestSubName2(String testName)
	{
		  String query="SELECT DISTINCT(`ref_testsubname`) FROM `test_output_reference` WHERE  `ref_testname` LIKE '%"+testName+"%'";
		  try {
				rs = statement.executeQuery(query);
				
				
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
				javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
			return rs;
		}
	public ResultSet searchTestName3(String testName)
	{
		  String query="SELECT DISTINCT(`exam_name`) FROM `exam_entery` WHERE `exam_name` LIKE '%"+testName+"%'";
		  try {
				rs = statement.executeQuery(query);
				
				
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
				javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
			return rs;
		}
	
	public ResultSet retrieveReferenceTableIndexNew(String testName,String patient_sex,String patient_age,String p_type)
	{
	  String query="SELECT `ref_id` FROM `test_output_reference` WHERE  `ref_testname` = '"+testName+"' AND `ref_patienttype` = '"+p_type+"' AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	 System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveReferenceTableIndex(String exam_id,String patient_sex,String patient_age,String p_type)
	{
	  String query="SELECT `ref_id` FROM `test_output_reference` WHERE  `exam_id` = '"+exam_id+"' AND `ref_patienttype` = '"+p_type+"' AND `ref_agemin`<="+patient_age+" AND `ref_agemax`>="+patient_age+" AND `ref_patientsex` IN ('Both', '"+patient_sex+"')";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveReferenceRangeWithId(String index)
	{
	  String query="SELECT `ref_lowerlimit`, `ref_upperlimit`, `ref_units`, `ref_comments`, `ref_testsubname` FROM `test_reference` WHERE `ref_id` = '"+index+"'";
	  try {
			rs = statement.executeQuery(query);			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveReferenceTableWithId(String index)
	{
	  String query="SELECT `ref_phy_para1`, `ref_phy_para2`, `ref_phy_para3`, `ref_phy_para4`, `ref_phy_para5`, `ref_phy_para6`, `ref_phy_para7`, `ref_phy_para8`, `ref_phy_para9`, `ref_phy_para10`, `ref_chem_para1`, `ref_chem_para2`, `ref_chem_para3`, `ref_chem_para4`, `ref_chem_para5`, `ref_chem_para6`, `ref_chem_para7`, `ref_chem_para8`, `ref_chem_para9`, `ref_chem_para10`, `ref_meh_para1`, `ref_meh_para2`, `ref_meh_para3`, `ref_meh_para4`, `ref_meh_para5`, `ref_meh_para6`, `ref_meh_para7`, `ref_meh_para8`, `ref_meh_para9`, `ref_meh_para10`, `ref_testsubname` FROM `test_output_reference` WHERE `ref_id` = '"+index+"'";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}

	
	public ResultSet retrieveReferenceTableExamCode(String index)
	{
	  String query="SELECT `ref_phy_para1`, `ref_phy_para2`, `ref_phy_para3`, `ref_phy_para4`, `ref_phy_para5`, `ref_phy_para6`, `ref_phy_para7`, `ref_phy_para8`, `ref_phy_para9`, `ref_phy_para10`, `ref_chem_para1`, `ref_chem_para2`, `ref_chem_para3`, `ref_chem_para4`, `ref_chem_para5`, `ref_chem_para6`, `ref_chem_para7`, `ref_chem_para8`, `ref_chem_para9`, `ref_chem_para10`, `ref_meh_para1`, `ref_meh_para2`, `ref_meh_para3`, `ref_meh_para4`, `ref_meh_para5`, `ref_meh_para6`, `ref_meh_para7`, `ref_meh_para8`, `ref_meh_para9`, `ref_meh_para10`, `ref_testsubname` FROM `test_output_reference` WHERE `exam_id` = '"+index+"'";
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}

	public ResultSet retrieveAllDataExamID(String index)
	{
	  String query="SELECT `exam_id`, `ref_testname`, `ref_patientsex`, `ref_patienttype`, `ref_agemin`, `ref_agemax`, `ref_lowerlimit`, `ref_upperlimit`, `ref_units`, `ref_comments` FROM `test_reference` WHERE `exam_code` = "+index;
	  try {
			rs = statement.executeQuery(query);
			
			
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void deleteRow(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM test_reference WHERE `ref_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserDataReferenceOutput(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `test_output_reference`( `exam_id`, `ref_testname`, `ref_testsubname`, `ref_patientsex`, `ref_patienttype`, `ref_agemin`, `ref_agemax`, `ref_phy_para1`, `ref_phy_para2`, " +
		  		"`ref_phy_para3`, `ref_phy_para4`, `ref_phy_para5`, `ref_phy_para6`, `ref_phy_para7`, `ref_phy_para8`, `ref_phy_para9`, `ref_phy_para10`, `ref_chem_para1`, `ref_chem_para2`, `ref_chem_para3`, `ref_chem_para4`, `ref_chem_para5`, " +
		  		"`ref_chem_para6`, `ref_chem_para7`, `ref_chem_para8`, `ref_chem_para9`, `ref_chem_para10`,`ref_meh_para1`, `ref_meh_para2`, `ref_meh_para3`, `ref_meh_para4`, `ref_meh_para5`, `ref_meh_para6`, `ref_meh_para7`, `ref_meh_para8`," +
		  		" `ref_meh_para9`, `ref_meh_para10`,  `ref_selected`, `ref_comments`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <40; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `test_reference`(`exam_id`, `ref_testname`, `ref_testsubname`,`ref_patientsex`, `ref_patienttype`, `ref_agemin`, `ref_agemax`, `ref_lowerlimit`, `ref_upperlimit`, `ref_units`, `ref_comments`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <12; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
