package hms.insurance.gui;

import hms.main.DBConnection;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

public class InsuranceDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public InsuranceDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public void Updatestatustracking(String index, String ipd)
	{
		String query="UPDATE `ipd_entery` SET `ipd_text5`='"+index+"' where `ipd_id2`='"+ipd+"'";
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	 public ResultSet RetrieveCatheterizationData(String ins_name) { 
		 String RateType=retrieveDataWithInsuranceType(ins_name)[1];
			String Query="SELECT exam_code,exam_rate from exam_master_"+RateType+" em where exam_code =18 or exam_code =512";	
					try {
						rs = statement.executeQuery(Query);

					} catch (SQLException sqle) {
						JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
								javax.swing.JOptionPane.ERROR_MESSAGE);
					} 	
			return rs;
		}
	 public String retrieveEditInsExamModulePassword()
		{
		  String query="SELECT value FROM `karun_sparsh_param` WHERE `dept`='INS_EXAM' ";
		  String pass="";
			try {
				rs = statement.executeQuery(query);
				while(rs.next()) {
					pass=rs.getString(1);
				}
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
				javax.swing.JOptionPane.ERROR_MESSAGE);
			} 
			return pass;
		}
	public void DeleteInsurance(String index)
	{
		String query="DELETE FROM insurance_detail WHERE ins_id ='"+index+"'";
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public void updatedatatracking(String[] data) throws Exception {
		String insertSQL = "UPDATE insurance_reimbursement_tracking SET ipd_id=?, recieved_amount=?, obj_raised=?, recieve_date=?, receive_entry_user=?, utr_number='"+data[8]+"',submitted_amount='"+data[9]+"' where `ipd_id`='"+data[0]+"'";
		System.out.println(insertSQL);  
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 6; i++) {
			preparedStatement.setString(i, data[i-1]);
		}
		preparedStatement.executeUpdate();

	}
	public ResultSet RetrieveSurgeryDATA(String ins,String cat) { 
		String query ="SELECT surgery1_percentage ,surgery2_percentage ,surgery3_percentage ,surgery1_divideby ,surgery2_divideby ,surgery3_divideby,ins_rebate FROM insurance_surgery_detail isd where ins_name ='"+ins+"' and ins_category ='"+cat+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);


		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievestatus(String id) { 
		String query = "SELECT `ipd_text5` FROM ipd_entery where ipd_id2="+id+"";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);


		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void insertdatatracking(String[] data) throws Exception {
		String insertSQL = "INSERT INTO insurance_reimbursement_tracking (ipd_id, insurance_type, bill_amount, submitted_amount, recieved_amount, obj_raised, submit_date, submit_entry_user,utr_number) VALUES(?,?,?, ?, ?, ?, ?, ?, ?)";
		System.out.println(insertSQL);  
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 10; i++) {
			preparedStatement.setString(i, data[i-1]);
		} 
		preparedStatement.executeUpdate();

	}
	
	public void insertOrUpdateOpdDataTracking(String[] data) throws Exception {
		String insertSQL = "REPLACE INTO insurance_opd_reimbursement_tracking (id,opd_id, insurance_type, bill_amount, submitted_amount,recieve_date, recieved_amount, obj_raised, submit_date, submit_entry_user,utr_number,claim_id) VALUES(?,?,?,?,?, ?, ?, ?, ?, ?, ?,?)";
		System.out.println(insertSQL);  
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 13; i++) {
			preparedStatement.setString(i, data[i-1]);
		} 
		preparedStatement.executeUpdate();

	}
	public ResultSet retrieveInsuranceOpdData(String opd_id) {
		String query="select id,opd_id, insurance_type, bill_amount, submitted_amount,recieve_date, recieved_amount, obj_raised, submit_date, submit_entry_user,utr_number,status,DATE_ADD(submit_date , INTERVAL 1 DAY)=CURRENT_DATE() as flag,claim_id from insurance_opd_reimbursement_tracking where opd_id='"+opd_id+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
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
	public ResultSet retrieveAllData12(String dateFrom, String dateTo, int iscashless) { 
		String query ="select\r\n"
				+ "	ie.ipd_id2 ,\r\n"
				+ "	ie.p_name ,\r\n"
				+ "	ie.insurance_type ,\r\n"
				+ "	(case\r\n"
				+ "		when ie.ipd_text5 = 'N/A' then 'NOT SUBMITTED'\r\n"
				+ "		else ie.ipd_text5\r\n"
				+ "	end )as ipd_text5,\r\n"
				+ "	ie.ipd_total_charges ,\r\n"
				+ "	irt.submitted_amount ,\r\n"
				+ "	irt.recieved_amount ,\r\n"
				+ "	irt.obj_raised ,\r\n"
				+ "	ie.ipd_discharge_date,\r\n"
				+ "	irt.submit_date ,\r\n"
				+ "	irt.recieve_date\r\n"
				+ "from\r\n"
				+ "	ipd_entery ie\r\n"
				+ "left join insurance_reimbursement_tracking irt on\r\n"
				+ "	ie.ipd_id2 = irt.ipd_id\r\n"
				+ "WHERE\r\n"
				+ "	ie.insurance_type <> 'Unknown'\r\n"
				+ "AND ie.ipd_discharged = 'Yes'\r\n"
				+ "AND ie.ipd_discharge_date BETWEEN '"+ dateFrom +"' AND '"+ dateTo +"' AND ie.is_cashless = "+iscashless+"";
		try {
			System.out.println(query);
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
		String query="SELECT `ins_id`, `ins_name`, `ins_detail`,`ins_ratepercentage`,`ins_ratetype`,`ins_mrp_reqd`,`ins_cash_reqd` FROM `insurance_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet InsMrpReqdOrNot(String ins_name)
	{
		String query="SELECT `ins_mrp_reqd` FROM `insurance_detail` WHERE  `ins_name`='"+ins_name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void UpdateInsuranceDATA(String[] data)
	{
		String query="UPDATE `insurance_detail` SET `ins_name`='"+data[1]+"', `ins_detail`='"+data[2]+"',`ins_ratepercentage`='"+data[3]+"',`ins_ratetype`='"+data[4]+"',`ins_mrp_reqd`='"+data[5]+"',`ins_cash_reqd`='"+data[6]+"' WHERE `ins_id`='"+data[0]+"' ";
		System.out.println(query);
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		
	}
	public ResultSet retrieveInsuranceItems()
	{
		String query="SELECT `item_id`,`item_name`,`insurance_name` FROM `insurance_items_adjustment_list` ORDER BY `item_name` ASC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void insertInsuranceItems(String[] data) throws SQLException
	{
		String insertSQL = "INSERT INTO `insurance_item_status`( `item_id`,`item_name`,`insurance_id`,`insurance_name`) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i-1]);
		}

		preparedStatement.executeUpdate();

	}
	public void deleteItemInsuRow(String insuName,String itemID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `insurance_item_status` WHERE `insurance_name`=? and `item_id`=?");
		preparedStatement.setString(1, insuName);
		preparedStatement.setString(2, itemID);
		preparedStatement.executeUpdate();
	}
	public String[] retrieveDataWithInsuranceType(String name)
	{
		String[] values=new String[2];
		values[0]="0";
		values[1]="1";
		String query="SELECT  `ins_ratepercentage`, `ins_ratetype` FROM `insurance_detail` WHERE `ins_name`= '"+name+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		try {
			while(rs.next()){
				values[0]=rs.getObject(1).toString();
				values[1]=rs.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return values;
	}
	public ResultSet searchPatientWithIdOrNmae(String index)
	{
		String query="SELECT * FROM `doctor_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'";
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM doctor_detail WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public ResultSet checkinstype(int itemid) {

		String query = "\r\n"
				+ "SELECT\r\n"
				+ "    CASE WHEN EXISTS \r\n"
				+ "    (\r\n"
				+ "        SELECT * FROM insurance_detail WHERE ins_ratetype='"+itemid+"'\r\n"
				+ "    )\r\n"
				+ "    THEN '1' ELSE '0' END as RESULT ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public boolean checkingtable(int itemid) {
		String table="exam_master_"+itemid;
		boolean bool = false;
		String query = "SELECT IF( EXISTS(\r\n"
				+ "             SELECT *\r\n"
				+ "             FROM INFORMATION_SCHEMA.TABLES\r\n"
				+ "           WHERE TABLE_NAME = '"+table+"'), 1, 0);";

		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
			while (rs.next()) {
				bool=rs.getBoolean(1);
				System.out.print("djdjdjdjd"+bool);
			}
		}catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return bool;
	}
	public ResultSet createtable(int itemid) {
		String table_name = "exam_master";
		table_name = table_name + "_" + itemid;
		String query ="CREATE TABLE `"+table_name+"` (\r\n"
				+ "  `exam_code` int(10) NOT NULL AUTO_INCREMENT,\r\n"
				+ "  `exam_desc` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_subcat` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_lab` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_room` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_operator` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_rate` varchar(250) NOT NULL DEFAULT '0',\r\n"
				+ "  `exam_code_1` varchar(250) NOT NULL DEFAULT '0',\r\n"
				+ "  `exam_name_1` varchar(250) NOT NULL DEFAULT 'NA',\r\n"
				+ "  `exam_text1` varchar(255) DEFAULT NULL,\r\n"
				+ "  `exam_text2` varchar(255) DEFAULT NULL,\r\n"
				+ "  `lis_mapping_code` int(10) DEFAULT NULL,\r\n"
				+ "  PRIMARY KEY (`exam_code`)\r\n"
				+ ") ENGINE = InnoDB AUTO_INCREMENT = 2090 DEFAULT CHARSET = utf8;"; 

		try {
			statement.execute(query);
			JOptionPane.showMessageDialog(null,"Sucessfully Uploaded "+table_name+" table",
					"Sucess", javax.swing.JOptionPane.INFORMATION_MESSAGE);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int inserData(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO `insurance_detail`( `ins_name`, `ins_detail`, `ins_ratepercentage`, `ins_ratetype`) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i <5; i++) {

			preparedStatement.setString(i, data[i-1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}