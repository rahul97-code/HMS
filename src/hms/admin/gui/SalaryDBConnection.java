package hms.admin.gui;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class SalaryDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public SalaryDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}


	
	public ResultSet retrieveSalary(String deptname)
	{
	  String query="SELECT `sal_id`,`department_id`, `department_name`, `employee_code`, `employee_name`, `employee_type`,  `salary` FROM `salaries` WHERE `department_name` = '"+deptname+"'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `salaries` WHERE `sal_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	

	public void insertSalaryData(ArrayList<String> data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `salaries`(`employee_code`, `employee_name`, `employee_type`, `department_id`, `department_name`, `salary`) VALUES (?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <=data.size(); i++) {
			
				  preparedStatement.setString(i, data.get(i-1));
			}
		  preparedStatement.executeUpdate();
	  }

}

