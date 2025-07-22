package hms.mrd.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.swing.JOptionPane;

public class MRDUserDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public MRDUserDBConnection() {

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
		String insertSQL     = "UPDATE `mrd_detail` SET `mrd_name`=?,`mrd_username`=?,`mrd_password`=?,`mrd_telephone`=?,`mrd_address`=?,`mrd_qualification`=?,`mrd_text1`=? WHERE  `mrd_id`="+data[7];
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
	public ResultSet retrieveAllPatientData(String ipd_id)
	{
	  String query="SELECT p_id, p_name, insurance_type FROM ipd_entery WHERE ipd_id = "+ipd_id+"";
	  
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void updateDataLastLogin(String receptionID) throws Exception
	  {
		
		 String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(Calendar.getInstance().getTime());
	        System.out.println(timeStamp );
		statement.executeUpdate("update `mrd_detail` set `mrd_lastlogin` = '"+timeStamp+"' where `mrd_id` = "+receptionID);
	  }
	public void updateReceptionCounter(String counter) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `mrd_counter` SET `text`='"+counter+"' WHERE 1");
	  }
	public void updateDataPassword(String receptionUN,String password) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `mrd_detail` SET `mrd_password`='"+password+"' where `mrd_username` = '"+receptionUN+"'");
	  }
	
	public ResultSet retrieveAllData(String btn)
	{
	  String query="SELECT room_no , room_name FROM `library_master` WHERE rack_no ='"+btn+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public int retrieveCounterData()
	{
	  String query="SELECT * FROM `mrd_detail` WHERE 1";
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
	  String query="SELECT `mrd_id`, `mrd_name`, `mrd_username`, `mrd_password`, `mrd_telephone`, `mrd_address`, `mrd_qualification` FROM `mrd_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2(String index)
	{
	  String query="SELECT `mrd_id`, `mrd_name`, `mrd_telephone`, `mrd_address` FROM `mrd_detail` WHERE mrd_id LIKE '%"+index+"%' OR mrd_name LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData2()
	{
	  String query="SELECT `mrd_id`, `mrd_name`, `mrd_telephone`, `mrd_address` FROM `mrd_detail` WHERE1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex(String name)
	{
	  String query="SELECT `mrd_id` FROM `mrd_detail` WHERE `mrd_name` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public boolean deleteDataWithName(String name) {
	    String query = "DELETE FROM mrd_patient_record WHERE rack_name = '"+name+"'";  // Use placeholder for the parameter
	    
try {
	        // Execute the delete query
	        int rowsAffected = statement.executeUpdate(query);
	        
	        // Check if any rows were deleted (rowsAffected will be > 0 if successful)
	        if (rowsAffected > 0) {
	            System.out.println("Record with mrd_name '" + name + "' has been deleted.");
	            return true; // Deletion successful
	        } else {
	            System.out.println("No records found for mrd_name '" + name + "'.");
	            return false; // No record was found to delete
	        }
	        
	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
	        return false; // In case of an error, return false
	    }
	}
	
	public boolean updateIPDData(String ipdStart, String ipdEnd, String year, String roomNo, String roomName, String rackNo, String rowNo) {
	    String query = "UPDATE library_master SET ipd_start = ?, ipd_end = ?, year = ? WHERE room_no = ? AND room_name = ? AND rack_no = ? AND row = ?";
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {

	        // Convert parameters
	        int ipdStartInt = Integer.parseInt(ipdStart);
	        int ipdEndInt = Integer.parseInt(ipdEnd);
	        int yearInt = Integer.parseInt(year);
	        int rowInt = Integer.parseInt(rowNo);

	        // Set parameters
	        preparedStatement.setInt(1, ipdStartInt);
	        preparedStatement.setInt(2, ipdEndInt);
	        preparedStatement.setInt(3, yearInt);
	        preparedStatement.setString(4, roomNo);
	        preparedStatement.setString(5, roomName);
	        preparedStatement.setString(6, rackNo);
	        preparedStatement.setInt(7, rowInt);

	        // Print simulated query for debugging
	        String simulatedQuery = String.format(
	            "UPDATE library_master SET ipd_start = %d, ipd_end = %d, year = %d WHERE room_no = '%s' AND room_name = '%s' AND rack_no = '%s' AND row = %d;",
	            ipdStartInt, ipdEndInt, yearInt, roomNo, roomName, rackNo, rowInt
	        );
	        System.out.println("Executing query: " + simulatedQuery);

	        // Execute update
	        int result = preparedStatement.executeUpdate();
	        return result > 0;

	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "SQL ERROR",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    } catch (NumberFormatException nfe) {
	        JOptionPane.showMessageDialog(null, "Invalid number format in input values.", "FORMAT ERROR",
	            JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	}

	
	
	public boolean deleteRackRowData(String ipd_id) {
	    String query = "DELETE FROM mrd_patient_data WHERE ipd_id = '"+ipd_id+"'";  // Use placeholder for the parameter
	    
try {
	        // Execute the delete query
	        int rowsAffected = statement.executeUpdate(query);
	        
	        // Check if any rows were deleted (rowsAffected will be > 0 if successful)
	        if (rowsAffected > 0) {
	            System.out.println("Record with ipd_id '" + ipd_id + "' has been deleted.");
	            return true; // Deletion successful
	        } else {
	            System.out.println("No records found for ipd_id '" + ipd_id + "'.");
	            return false; // No record was found to delete
	        }
	        
	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
	        return false; // In case of an error, return false
	    }
	}

	
	public ResultSet retrieveRackData()
	{
	  String query="SELECT distinct(rack_no) FROM `library_master` WHERE `is_deleted` =0";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveRackRowData1(String rackno)
	{
	  String query="SELECT CONCAT('Row ', lm.`row`, ' ', lm.ipd_start, ' To ', lm.ipd_end, ' year ', lm.`year`) AS description\r\n"
	  		+ "FROM library_master lm\r\n"
	  		+ "WHERE lm.rack_no = '"+rackno+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	
	public ResultSet searchipd(String ipd)
	{
	  String query="SELECT rack_no, CONCAT('Row ', lm.`row`, ' ', lm.ipd_start, ' To ', lm.ipd_end, ' year ', lm.`year`) as row\r\n"
	  		+ "FROM library_master lm \r\n"
	  		+ "WHERE lm.ipd_start  <= "+ipd+" AND lm.ipd_end  >= "+ipd+"";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public boolean insertRackData(String[] data) {
	    String query = "INSERT INTO library_master (department_id, department_name, room_no, room_name, rack_no, row, col, ipd_start, ipd_end, year, entry_user) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    try {
	        PreparedStatement preparedStatement = connection.prepareStatement(query);

	        // Set string values for the first 7 columns
	        for (int i = 0; i < 7; i++) {
	            preparedStatement.setString(i + 1, data[i]);
	        }

	        // Parse and set integers for ipd_start (index 7), ipd_end (8), and year (9)
	        preparedStatement.setInt(8, Integer.parseInt(data[7]));
	        preparedStatement.setInt(9, Integer.parseInt(data[8]));
	        preparedStatement.setInt(10, Integer.parseInt(data[9]));

	        // Set string for entry_user (index 10)
	        preparedStatement.setString(11, data[10]);

	        int result = preparedStatement.executeUpdate();
	        return result == 1;
	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "SQL ERROR",
	            javax.swing.JOptionPane.ERROR_MESSAGE);
	        return false;
	    } catch (NumberFormatException nfe) {
	        JOptionPane.showMessageDialog(null, "Invalid number format for IPD/Year fields.", "DATA ERROR",
	            javax.swing.JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	}

	
	public boolean insertRackRowData(String p_name, String p_id, String ipd_id, String insurance, int rack_id) {
	    String query = "INSERT INTO `mrd_patient_data` (`p_name`, `p_id`, `ipd_id`, `insurance_name`, `rack_id`) VALUES (?, ?, ?, ?, ?)";
	    try {
	        // Prepare the statement to prevent SQL injection
	        PreparedStatement preparedStatement = connection.prepareStatement(query);
	        preparedStatement.setString(1, p_name); // Set the value for the rack_name column
	        preparedStatement.setString(2, p_id);
	        preparedStatement.setString(3, ipd_id);
	        preparedStatement.setString(4, insurance);
	        preparedStatement.setInt(5, rack_id);


	        // Execute the update
	        int result = preparedStatement.executeUpdate();

	        // Check if the insert was successful (1 means one row was affected)
	        return result == 1;
	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
	            javax.swing.JOptionPane.ERROR_MESSAGE);
	        return false;
	    }
	}

	
	public ResultSet retrieveRackRowData(String startipd , String ipdend) {
		
		String query = "SELECT p_id,p_name,ipd_id,insurance_type FROM ipd_entery where ipd_id  between '"+startipd+"' and '"+ipdend+"'"; // Replace with your table name
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		
		return rs;
		
	}
	
public int retrieveRackID(String Rackname) {
	int id = 0;
		String query = "SELECT id FROM mrd_patient_record where rack_name ='"+Rackname+"'"; // Replace with your table name
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		try {
			if (rs.next()) {
			     id = rs.getInt("id"); // Get the first (and presumably only) id from the result set
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return id;
		
	}
	
	
	public ResultSet retrieveDataWithID(String opID)
	{
	  String query="SELECT `mrd_name`, `mrd_username`, `mrd_password`,`mrd_telephone`, `mrd_address`, `mrd_qualification`,COALESCE(`mrd_text1`, 'NO') FROM `mrd_detail` WHERE `mrd_id` = "+opID+"";
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
	  String query="SELECT * FROM `mrd_detail` WHERE `mrd_username` = '"+name+"' AND `mrd_password` = '"+pass+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrievePassword(String receptionUN)
	{
	  String query="SELECT `mrd_password` FROM `mrd_detail` WHERE `mrd_username` = '"+receptionUN+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveUsernameDetail(String name)
	{
	  String query="SELECT  `mrd_id`, `mrd_name`,`mrd_lastlogin`,COALESCE(`mrd_text1`, 'NO') FROM `mrd_detail` WHERE `mrd_username` = '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmae(String index)
	{
	  String query="SELECT * FROM `mrd_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM mrd_detail WHERE mrd_id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `mrd_detail`( `mrd_name`, `mrd_username`, `mrd_password`, `mrd_telephone`, `mrd_address`, `mrd_qualification`,`mrd_text1`) VALUES  (?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <8; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
}
