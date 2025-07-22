package hms1.wards.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class WardsManagementDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public WardsManagementDBConnection() {

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

	public void updateData(String[] data, String wardIndex) throws Exception {
		String insertSQL = "UPDATE `ward_management` SET `p_id`=?,`p_name`=?,`bed_filled`=?,`ward_entry_date`=? WHERE `ward_id`="
				+ wardIndex + "";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	
	public ResultSet RetrieveBedHourPercentage() {
		String query="select * from bed_hour_percentage";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public void updateDataUnfill(String[] data, String wardIndex)
			throws Exception {
		String insertSQL = "UPDATE `ward_management` SET `bed_filled`=? WHERE `ward_id`="
				+ wardIndex + "";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 2; i++) {
			
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveWardCategory(String dept) {
		String query = "SELECT `ward_category` FROM `ward_management` WHERE `ward_name`='"
				+ dept + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllWards(String building) {
		String query = "SELECT DISTINCT `ward_name` FROM `ward_management` WHERE `building_name`='"
				+ building + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllWards() {
		String query = "SELECT DISTINCT `ward_name` FROM `ward_management` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientCount() {
		String query = "SELECT `ipd_ward` AS  'WARD NAME', `ipd_room` AS 'WARD NUMBER',COUNT(`ipd_id`) AS 'PATIENT COUNT' FROM `ipd_entery` WHERE `ipd_discharged`= 'No' GROUP BY `ipd_ward` ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientIPDDataCount(String datefrom,String dateto) {
		String query = "SELECT\r\n"
				+ "	sum(case when `ipd_discharged` = 'No' then 1 else 0 end) as Ipd,\r\n"
				+ "	sum(case when `ipd_discharged` = 'Yes' then 1 else 0 end) as Discharge\r\n"
				+ "FROM\r\n"
				+ "	`ipd_entery`\r\n"
				+ "WHERE\r\n"
				+ "     ipd_entry_date between '"+datefrom+"' and '"+dateto+"'\r\n"
				+ "";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievePatientIPDData(String datefrom,String dateto) {
		String query = "SELECT ipd_id as 'IPD Id', p_id as 'P Id' , p_name as 'P Name', ipd_ward as 'Ward' FROM `ipd_entery` WHERE `ipd_discharged` = 'No' and ipd_entry_date between '"+datefrom+"' and '"+dateto+"' GROUP BY 1 ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientIPDDischargeData(String datefrom,String dateto) {
		String query = "SELECT ipd_id as 'IPD Id', p_id as 'P Id' , p_name as 'P Name', ipd_ward as 'Ward' FROM `ipd_entery` WHERE `ipd_discharged` = 'Yes' and ipd_entry_date between '"+datefrom+"' and '"+dateto+"' GROUP BY 1 ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientAllIPDData() {
		String query = "SELECT ipd_id2 as 'IPD Id', p_id as 'P Id', p_name as 'P Name', ipd_entry_date as 'Entry Date' , ipd_enter_time AS 'Entry Time', doctor_name AS 'Doctor Name', ipd_ward AS 'Ward Name', ipd_room AS 'Room', ipd_bed_no AS 'Bed No.', ipd_advanced_payment AS 'Advance Payment', ipd_total_charges AS 'IPD Charges', ipd_recieved_amount AS 'Recieved Amount' FROM `ipd_entery` WHERE `ipd_discharged` like '%No%' and ipd_entry_date BETWEEN date_sub(CURRENT_DATE(),INTERVAL 3 MONTH) and CURRENT_DATE() GROUP BY 1 ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveWardCharges(String wardType) {
		String query = "SELECT ic.`charges` FROM `ward_cahrges` ic left join ward_management ib on ib.ward_category = ic.ward_type WHERE ib.ward_name  = '"+wardType+"' LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveInsuranceWardCharges(String ins_name,String ins_cat,String wardType) {
		String query = "select charges FROM insurance_bed_detail WHERE ins_name='"+ins_name+"' and ins_category='"+ins_cat+"' and ward_type='"+wardType+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveGetDetail(String wardName) {
		String query = "SELECT `ipd_building` AS 'BUILDING NAME', `ipd_ward` AS 'WARD NAME', `ipd_room` AS 'ROOM NUMBER', `ipd_bed_no` AS 'BED NUMBER',`p_id` AS 'PATIENT ID', `p_name` AS 'PATIENT NAME' FROM `ipd_entery` WHERE `ipd_discharged`= 'No' AND `ipd_ward`='"+wardName+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String wardIndex) {
		String query = "SELECT `ward_room_no`, `ward_incharge`,`ward_category` FROM `ward_management` WHERE `ward_id`="
				+ wardIndex + "";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllWardCharges(String wardType,String ipd_id) {
		String query = "SELECT ic.`charges` FROM `ward_cahrges` ic left join ipd_bed_details ib on ib.ipd_ward_category = ic.ward_type \r\n"
				+ "WHERE ib.ipd_ward ='"+wardType+"' and ib.`ipd_id`='"+ipd_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllWardCharges(String wardType) {
		String query = "SELECT `charge_id`, `ward_type`,`ward_time`, `charges` FROM `ward_cahrges` WHERE `ward_type`='"
				+ wardType + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllBuilding() {
		String query = "SELECT DISTINCT `building_name` FROM `ward_management` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllBeds(String building, String wardname) {
		String query = "SELECT `ward_id`,`bed_no` FROM `ward_management` WHERE `building_name`='"
				+ building
				+ "' AND `ward_name`='"+ wardname +"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllEnableBeds(String building, String wardname) {
		String query = "SELECT `ward_id`,`bed_no` FROM `ward_management` WHERE `building_name`='"
				+ building
				+ "' AND `ward_name`='"+ wardname +"' AND bed_filled <> 1";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllBedsFilled(String building, String wardname) {
		String query = "SELECT `p_id`,`p_name`,`bed_no`,`ward_entry_date` FROM `ward_management` WHERE `building_name`='"
				+ building
				+ "' AND `ward_name`='"
				+ wardname
				+ "' AND `bed_filled`='1'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteWardCharges(String rowID) throws Exception
	{
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `ward_cahrges` WHERE `charge_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM ward_management WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void SQLProcedureCall() throws Exception {
		String q="CALL Update_bed_Category()";
		statement.executeUpdate(q);
	}

	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ward_management`( `building_name`, `ward_name`, `ward_room_no`, `ward_incharge`,`ward_category`, `bed_no`) VALUES  (?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 7; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	
	public int inserWardChargesData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ward_cahrges`(`ward_type`, `charges`) VALUES (?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 3; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
