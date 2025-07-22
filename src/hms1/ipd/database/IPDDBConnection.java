
package hms1.ipd.database;

import hms.main.DBConnection;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class IPDDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public IPDDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}


	public ResultSet retrievestatustracking(String index) {
		String query = "SELECT\r\n"
				+ "				submitted_amount,\r\n"
				+ "				recieved_amount,\r\n"
				+ "				CASE WHEN `recieve_date` != 'NULL' THEN `recieve_date` else  CURRENT_DATE() END new_date1,\r\n"
				+ "				CASE WHEN `submit_date` != 'NULL' THEN `submit_date` else  CURRENT_DATE() END new_date2\r\n"
				+ "			FROM\r\n"
				+ "				hospital_db.insurance_reimbursement_tracking Where ipd_id ='"+index+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getallotcharges() { 
		String query = "select * from ot_charges";
		System.out.println(query); 
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateIsDraftStatus(String ipdID) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `is_draft` ='1' WHERE `ipd_id`= "+ipdID+"");
	}
	public void updateTotalAmounttracking(String opdID,String amount) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_total_charges` ='"+amount+"' WHERE `ipd_id`= "+opdID+"");
	}

	public ResultSet retrieveAllDataPatientIDNEWtracking(String patientID,String ipd_id) {
		String query = "SELECT\r\n"
				+ "	`ipd_id`,\r\n"
				+ "	`ipd_id2`,\r\n"
				+ "	`ipd_building`,\r\n"
				+ "	`ipd_ward`,\r\n"
				+ "	`ipd_bed_no`,\r\n"
				+ "	`ipd_advanced_payment`,\r\n"
				+ "	`ipd_entry_date`,\r\n"
				+ "	`ipd_enter_time`,\r\n"
				+ "	`ipd_ward_code`,\r\n"
				+ "	`ipd_text3`,\r\n"
				+ "	`ipd_package_name`,\r\n"
				+ "	`ipd_operation_name`,\r\n"
				+ "	`ipd_package_amount`,\r\n"
				+ "	REPLACE(FORMAT(`ipd_total_charges`, 2), ',', ''),\r\n"
				+ "	`p_scheme`,\r\n"
				+ "	`surgical_code`,\r\n"
				+ "	`approved_amnt`,\r\n"
				+ "	`ipd_recieved_amount`,\r\n"
				+ "	`ipd_doctor_refference`,\r\n"
				+ "	'',\r\n"
				+ "	`is_draft`,\r\n"
				+ "	`ipd_discharge_date`,\r\n"
				+ "	`ipd_empanelled_p_ref_no`,\r\n"
				+ "	`discharge_type`,\r\n"
				+ "	`is_cashless`\r\n"
				+ "FROM\r\n"
				+ "	`ipd_entery`\r\n"
				+ "WHERE\r\n"
				+ "	`p_id` ='"+patientID+"'\r\n"
				+ "	and `ipd_id2` = '"+ipd_id+"'\r\n"
				+ "	";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveAllOPDData(String patientID,String opd_date,String instype, String mode) {
		String query = "SELECT 'C' as exp_type,'1' as exp_id,opd_doctor,opd_date,'1' as qty,opd_charge \r\n"
				+ "from opd_entery oe \r\n"
				+ "where p_id='"+patientID+"' and opd_date='"+opd_date+"' and p_insurance_type = '"+instype+"' and payment_mode = '"+mode+"'\r\n"
				+ "union all\r\n"
				+ "select	'E' as exp_type,exam_nameid ,exam_name ,exam_date,'1' as qty,exam_charges \r\n"
				+ "from	exam_entery ee\r\n"
				+ "where	exam_performed <> 'cancel'	and exam_result5 is null and p_insurancetype = '"+instype+"' and payment_mode = '"+mode+"'\r\n"
				+ "	and exam_pid = '"+patientID+"' and exam_date='"+opd_date+"' \r\n";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	
	public ResultSet retrieveAllOPDEXAMData(String patientID,String opd_date, String instype, String mode) {
		String query ="select 'E' as exp_type,exam_nameid ,exam_name ,exam_date,'1' as qty,exam_charges \r\n"
				+ "from	exam_entery ee\r\n"
				+ "where	exam_performed <> 'cancel'	and exam_result5 is null and p_insurancetype = '"+instype+"' and payment_mode = '"+mode+"'\r\n"
				+ "	and exam_pid = '"+patientID+"' and exam_date='"+opd_date+"' \r\n";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;

	}
	public ResultSet retrieveAllDataDoctortracking(String ipd_id) { 
		String query = "SELECT `doctor_name`, `doctor_id`, `start_date`, `start_time`, `end_date`, `end_time` FROM `ipd_doctor_incharge` WHERE `ipd_id`='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteDraftBill(String ipd_id)
	{
		String query="update ipd_entery set is_draft='0' where ipd_id ='"+ipd_id+"'";
		String query1="delete from Insurance_Tracking_list where ipd_id ='"+ipd_id+"'";
		try {
			statement.executeUpdate(query);
			statement.executeUpdate(query1);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
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
	
	public ResultSet retrieveConsultantData(String ipd_id) {
		String query="	\r\n"
				+ "SELECT T2.ipd_id,T2.doctor_name,T2.consultant_date,T2.qty,(T2.qty*350) as doctor_charge FROM(	\r\n"
				+ "SELECT *,CASE\r\n"
				+ "	       when T.ipd_entry_date=T.ipd_discharge_date then '1'\r\n"
				+ "	       when T.ipd_entry_date<>T.ipd_discharge_date and T.consultant_date=T.ipd_entry_date  and STR_TO_DATE(T.ipd_enter_time,'%h:%i:%s %p') > '12:00:00' then '1'\r\n"
				+ "	       when T.ipd_entry_date<>T.ipd_discharge_date and T.consultant_date=T.ipd_discharge_date THEN '1'\r\n"
				+ "	       else '2'\r\n"
				+ "        END as qty FROM(	\r\n"
				+ "	SELECT  A.ipd_id,\r\n"
				+ "	A.ipd_entry_date,\r\n"
				+ "	A.ipd_discharge_date,\r\n"
				+ "	A.ipd_enter_time,\r\n"
				+ "		A.doctor_name,\r\n"
				+ "		ADDDATE(A.ipd_entry_date,n) as consultant_date	 \r\n"
				+ "		FROM ( \r\n"
				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
				+ "SELECT \r\n"
				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
				+ "         FROM (\r\n"
				+ "                /**** $$ This query get ***/\r\n"
				+ "                  SELECT 1 as c, ((((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
				+ "(SELECT @row_number := 0) AS x\r\n"
				+ "ORDER BY n asc \r\n"
				+ "/**** %% Query end *****/  \r\n"
				+ ")T,ipd_entery A where n>=0 and n<=DATEDIFF(A.ipd_discharge_date\r\n"
				+ " ,A.ipd_entry_date) and A.ipd_id2='"+ipd_id+"' )T)T2";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveLastBedDateTime(String ipd_id) {
		String query="SELECT CONCAT(start_date, ' ',start_time) from ipd_bed_details WHERE ipd_id ='"+ipd_id+"' order by 1 desc limit 1 \r\n"
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
	
	public ResultSet retrieveInsuranceBedCharge(String insurance,String ins_cat) {
		String query="select ward,charges  from insurance_bed_detail ibd WHERE ward_type='PRIVATE ROOM WITH AC' and ins_name='"+insurance+"' and ins_category ='"+ins_cat+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBedDetailData(String ipd_id) {
		String query = "SELECT `change_id`,\r\n"
		+ "	`ipd_building`,\r\n"
		+ "	`ipd_ward`,\r\n"
		+ "	`ipd_room`,\r\n"
		+ "	`ipd_bed_no`,\r\n"
		+ "	`start_date`,\r\n"
		+ "	`start_time`,\r\n"
		+ "	CASE when 	`end_date`='0000-00-00'\r\n"
		+ "    then CURRENT_DATE() else end_date\r\n"
		+ "    end as E_date,\r\n"
		+ "    CASE when 	`end_time`='n/a'\r\n"
		+ "    then CURRENT_TIME() else end_time\r\n"
		+ "	end as E_time,\r\n"
		+ "	CASE when 	`ward_charges_rate`='NA'\r\n"
		+ "    then \"0\" else ward_charges_rate\r\n"
		+ "	end as ward_charges_rate\r\n"
		+ "FROM\r\n"
		+ "	`ipd_bed_details` \r\n"
		+ "WHERE\r\n"
		+ "	`ipd_id` = '"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateMedReturnedStatus(String ipdID) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `is_med_returned`='1' WHERE `ipd_id2`= '"+ipdID+"'");
	}
	public void updateCancellation(String opdID) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_text5`='CANCEL' WHERE `ipd_id2`= '"+opdID+"'");
	}
	public void updateAdvanceAmount(String opdID,String amount) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_advanced_payment` ='"+amount+"' WHERE `ipd_id`= "+opdID+"");
	}
	public void updateBadDetails(String[] data,String s_no) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_bed_details` SET `ipd_building` ='"+data[0]+"',`ipd_ward` ='"+data[1]+"',`start_date` ='"+data[2]+"',`end_date` ='"+data[3]+"',`ipd_room` ='"+data[4]+"',`ipd_bed_no` ='"+data[5]+"',`start_time` ='"+data[6]+"',`ward_charges_rate` ='"+data[7]+"',`end_time` ='"+data[8]+"' WHERE `change_id`= "+s_no+"");
	}
	public void DeleteBad(String s_no) throws Exception
	{

		statement.executeUpdate("DELETE FROM `ipd_bed_details` WHERE `change_id`= "+s_no+"");
	}
	public void updateTotalAmount(String opdID,String amount) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_total_charges` ='"+amount+"' WHERE `ipd_id`= "+opdID+"");
	}
	public void updateAddAmount(String opdID,String amount) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_entery` SET `ipd_total_charges` =`ipd_total_charges`+'"+amount+"' WHERE `ipd_id`= "+opdID+"");
	}
	public ResultSet retrieveClaim(String ipd_id)
	{
		String query="SELECT ipd_empanelled_p_ref_no from ipd_entery Where ipd_id2='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void updateDataIpdCancel(String[] data) throws Exception {


		System.out.println("jk"+data[9]);
		String insertSQL = "UPDATE `ipd_entery` SET `ipd_total_charges`=?, `ipd_balance`=?, `ipd_recieved_amount`=?, `ipd_payment_recieved`=?, `ipd_discharge_date`=?, `ipd_discharge_time`=?, `ipd_discharged`=?,`ipd_text1`=?,`ipd_text2`=?,`ipd_id`=?,`p_scheme`=?,`surgical_code`=?,`approved_amnt`=? WHERE `ipd_id`="+data[9];
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 14; i++) { 
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateData(String[] data) throws Exception {


		System.out.println("jk"+data[9]);
		String insertSQL = "UPDATE `ipd_entery` SET `ipd_total_charges`=?, `ipd_balance`=?, `ipd_recieved_amount`=?, `ipd_payment_recieved`=?, `ipd_discharge_date`=?, `ipd_discharge_time`=?, `ipd_discharged`=?,`ipd_text1`=?,`ipd_text2`=?,`ipd_id`=?,`p_scheme`=?,`surgical_code`=?,`approved_amnt`=?,`discharge_type`=? WHERE `ipd_id`="+data[9];
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 15; i++) { 
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateBedData(String[] data) throws Exception {
		String insertSQL = "UPDATE `ipd_entery` SET `ipd_building`= ? ,`ipd_ward`= ? ,`ipd_room`= ? ,`ipd_bed_no`= ? ,`ipd_ward_code`=? WHERE `ipd_id`="+data[5];
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 6; i++) { 
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateDoctorData(String doctorName,String ipd_id) throws Exception {
		statement.executeUpdate("UPDATE `ipd_entery` SET `doctor_name` ='"+doctorName+"' WHERE `ipd_id`= "+ipd_id+"");

	}
	public void updatePackageData(String[] data) throws Exception {
		String insertSQL = "UPDATE `ipd_entery` SET `ipd_package_name`= ? ,`ipd_operation_name`= ? ,`ipd_package_amount`=? WHERE `ipd_id`="+data[3];
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 4; i++) { 
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updateWardData(String[] data) throws Exception {

		System.out.println(data[0]+"  "+data[1]+"  "+data[2]+"  "+data[3]);
		String insertSQL = "UPDATE `ipd_bed_details` SET `end_date`='"+data[0]+"', `end_time` = '"+data[1]+"',`ward_charges_rate`='"+data[4]+"' WHERE `ipd_id`='"+data[2]+"' AND `ipd_ward`='"+data[3]+"' AND `end_time` ='n/a'" ;
		System.out.println(insertSQL);
		statement.executeUpdate(insertSQL);
	}
	public void updateDataBillNo(String billNo) throws Exception
	{

		statement.executeUpdate("UPDATE `ipd_bill` SET `bill_no`='"+billNo+"' WHERE 1");
	}
	public void updateDataEmergency(String ipdId) throws Exception
	{
		String query="UPDATE `ipd_entery` SET `emergency_opd`='No' WHERE `ipd_id`='"+ipdId+"'";
		System.out.println(query);
		statement.executeUpdate(query);
	}
	public String retrieveBillNo() {
		String query = "SELECT * FROM `ipd_bill` WHERE 1",billNo="";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}

		try {
			while (rs.next()) {
				billNo=rs.getObject(2)+"";
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return billNo;
	}
	public ResultSet retrieveAllDataIPD(String index) {
		String query = "SELECT `ipd_id`, `p_id`, `p_name`, `doctor_name`, `emergency_opd`, `remarks`,`ipd_entry_date`,`ipd_enter_time`,`ipd_package_amount` FROM ipd_entery WHERE `ipd_id` = "
				+ index+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public int retrieveCounterData() {
		String query = "SELECT `ipd_id` FROM `ipd_entery` ORDER BY `ipd_id` DESC LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		NumberOfRows++;
		return NumberOfRows;
	}
	public ResultSet retrieveChargesHead() {
		String query = "SELECT * FROM `ipd_charges_head` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`, `p_id`, I.`p_name`,`insurance_type`,`ipd_ward`, `ipd_bed_no`,`ipd_entry_date`,CASE WHEN `ipd_discharge_date` = '0000-00-00' THEN '' ELSE `ipd_discharge_date` END AS `ipd_discharge_date` FROM `ipd_entery` I  WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`!='CANCELLED' AND `emergency_opd` NOT IN('Emergency' ,'Procedure') ORDER BY `ipd_id` DESC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataEmergency(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`,`emergency_opd`, `p_id`, I.`p_name`,`p_insurancetype`,`ipd_ward`, `ipd_bed_no`,`ipd_entry_date` FROM `ipd_entery` I LEFT JOIN `patient_detail` P ON `pid1`=`p_id` WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`!='CANCELLED' AND `emergency_opd`='Emergency' ORDER BY `ipd_id` DESC";

		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataProcedure(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`,`emergency_opd`, `p_id`, I.`p_name`,`p_insurancetype`,`ipd_ward`, `ipd_bed_no`,`ipd_entry_date` FROM `ipd_entery` I LEFT JOIN `patient_detail` P ON `pid1`=`p_id` WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`!='CANCELLED' AND `emergency_opd`='Procedure' ORDER BY `ipd_id` DESC";

		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDialysis(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`,`emergency_opd`, `p_id`, I.`p_name`,`p_insurancetype`,`ipd_ward`, `ipd_bed_no`,`ipd_entry_date` FROM `ipd_entery` I LEFT JOIN `patient_detail` P ON `pid1`=`p_id` WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`!='CANCELLED' AND `emergency_opd`='Dialysis' ORDER BY `ipd_id` DESC";

		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAdvanceAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_advanced_payment` FROM `ipd_entery` WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `ipd_text5`='N/A'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataExcel(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id`,`ipd_id2`, `p_id`, `p_name`, `ipd_entry_date`, `ipd_enter_time`, `ipd-entry_user`,`ipd_advanced_payment`, REPLACE(FORMAT(`ipd_total_charges`,2),',',''), `ipd_discharge_date`, `ipd_discharge_time`, `ipd_text2`  FROM `ipd_entery` WHERE `ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`='Yes'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDischargeAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_recieved_amount` FROM `ipd_entery` WHERE `ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`='Yes'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataDischargeAmountInsurance(String insuranceType,String dateFrom, String dateTo) { 
		String query = "SELECT COUNT(I.`ipd_recieved_amount`),COALESCE(SUM(I.`ipd_recieved_amount`),0) FROM `ipd_entery` I,`ipd_entery` P WHERE P.ipd_id=I.ipd_id AND I.`ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND I.`ipd_discharged`='Yes' AND I.`insurance_type`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataInsurance(String insuranceType,String dateFrom, String dateTo) { 
		String query = "SELECT I.`ipd_id` AS \"IPD ID\", I.`ipd_id2` AS \"IPD ID 2\", I.`p_id` AS \"Patient ID\", I.`p_name` AS \"Patient Name\", I.`ipd_entry_date` AS \"Entery Date\", I.`ipd_enter_time` AS \"Entery Time\", I.`ipd_advanced_payment` AS \"Advance Amount\",REPLACE(FORMAT(`ipd_total_charges`,2),',','')  AS \"Total Charges\", I.`ipd_discharge_date` AS \"Discharge Date\", I.`ipd_discharge_time` AS \"Discharge Time\", I.`ipd_discharged` AS \"Discharged\" FROM `ipd_entery` I,`patient_detail` P WHERE P.pid1=I.p_id AND I.`ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND I.`ipd_discharged`='Yes' AND P.`p_insurancetype`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDischargeAmountUserWise(String dateFrom, String dateTo,String username,String insurance) { 
		String query = "SELECT COUNT(I.`ipd_recieved_amount`),COALESCE(SUM(I.`ipd_recieved_amount`),0) FROM `ipd_entery` I,`patient_detail` P WHERE P.pid1=I.p_id AND I.`ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND I.`ipd_text2`='" + username + "'  AND I.`ipd_discharged`='Yes' AND P.`p_insurancetype`='"+insurance+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDischargeAmountUserWise(String dateFrom, String dateTo,String username) { 
		String query = "SELECT COUNT(`ipd_recieved_amount`),COALESCE(SUM(`ipd_recieved_amount`),0) FROM `ipd_entery` WHERE `ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_text2`='" + username + "' AND`ipd_discharged`='Yes'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDischargeAmountInsurance(String insuranceType,String dateFrom, String dateTo,String username) { 
		String query = "SELECT COUNT(I.`ipd_recieved_amount`),COALESCE(SUM(I.`ipd_recieved_amount`),0) FROM `ipd_entery` I WHERE  I.`ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND I.`ipd_text2`='" + username + "'  AND I.`ipd_discharged`='Yes' AND `insurance_type`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataNonDischargeAmount(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`,`p_id`, `p_name`, `ipd_ward`,`ipd_bed_no`,REPLACE(FORMAT(`ipd_total_charges`,2),',',''), `ipd_advanced_payment`,`ipd_entry_date` FROM `ipd_entery` WHERE `ipd_entry_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`='No'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataDischargePatient(String dateFrom, String dateTo) { 
		String query = "SELECT `ipd_id2`,`p_id`, `p_name`, `ipd_recieved_amount`,REPLACE(FORMAT(`ipd_total_charges`,2),',',''), `ipd_advanced_payment`,`ipd_entry_date`,ipd_discharge_date FROM `ipd_entery` WHERE `ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_discharged`='Yes'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorIPD1(String dateFrom, String dateTo,String doctorName,String insurance) { 
		//		String query = "SELECT `ipd_total_charges`, `ipd_advanced_payment`,`ipd_entry_date`,ipd_discharge_date FROM `ipd_entery` WHERE `ipd_discharge_date` BETWEEN '"
		//				+ dateFrom + "' AND '" + dateTo + "'  AND `ipd_id`='"+ipd_id+"'";
		//		String query = "SELECT COUNT(I.`ipd_total_charges`),COALESCE(SUM(I.`ipd_total_charges`),0) FROM `ipd_entery` I,`patient_detail` P,`ipd_doctor_incharge` D WHERE P.pid1=I.p_id AND I.`ipd_id`=D.`ipd_id` and I.`ipd_discharge_date` BETWEEN '"
		//				+ dateFrom + "' AND '" + dateTo + "' AND D.`doctor_name`='"+doctorName+"'  AND I.`ipd_discharged`='Yes' AND P.`p_insurancetype`='"+insurance+"'";
		//	
		String query = "SELECT COUNT(I.`ipd_total_charges`),COALESCE(SUM(I.`ipd_total_charges`),0) FROM `ipd_entery` I,`ipd_doctor_incharge` D WHERE I.`ipd_id`=D.`ipd_id` and I.`ipd_discharge_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND D.`doctor_name`='"+doctorName+"'  AND I.`ipd_discharged`='Yes' AND I.`insurance_type`='"+insurance+"'";


		//String query = "SELECT b.`ipd_total_charges` FROM `ipd_doctor_incharge` a, `ipd_entery` b WHERE a.`doctor_name`='"+doctorName+"' and a.`ipd_id`=b.`ipd_id` and b.`ipd_discharge_date` BETWEEN '"+ dateFrom + "' AND '" + dateTo + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorIPD1(String dateFrom, String dateTo,String doctorName) { 

		String query = "SELECT REPLACE(FORMAT(`ipd_total_charges`,2),',','')  FROM `ipd_doctor_incharge` a, `ipd_entery` b WHERE a.`doctor_name`='"+doctorName+"' and a.`ipd_id`=b.`ipd_id` and b.`ipd_discharge_date` BETWEEN '"+ dateFrom + "' AND '" + dateTo + "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataDoctorIPDAdvance(String dateFrom, String dateTo,String doctorName,String insurance) { 
		String query = "SELECT count(A. `payment_amount`),COALESCE(sum(A. `payment_amount`),0) FROM `ipd_entery` I,`patient_detail` P,`ipd_doctor_incharge` D ,`ipd_payments` A WHERE P.pid1=I.p_id  AND I.`ipd_id`=D.`ipd_id` AND I.`ipd_id`=A.`ipd_id` AND A.`payment_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND D.`doctor_name`='"+doctorName+"'  AND A.`payment_type` ='P' AND I.`ipd_discharged`='No' AND P.`p_insurancetype`='"+insurance+"'";

		System.out.println(query);
		//String query = "SELECT count(c. `payment_amount`),COALESCE(sum(c. `payment_amount`),0) FROM `ipd_doctor_incharge` a, `ipd_entery` b,`ipd_payments` c WHERE a.`ipd_id`=b.`ipd_id` and b.`ipd_id`=c.`ipd_id` and c.`payment_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' and b.`ipd_discharged`='No' and `payment_type` ='P' and a.`doctor_name`='"+doctorName+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorIPDAdvance(String dateFrom, String dateTo,String doctorName) { 
		String query = "SELECT count(c. `payment_amount`),COALESCE(sum(c. `payment_amount`),0) FROM `ipd_doctor_incharge` a, `ipd_entery` b,`ipd_payments` c WHERE a.`ipd_id`=b.`ipd_id` and b.`ipd_id`=c.`ipd_id` and c.`payment_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' and b.`ipd_discharged`='No' and `payment_type` ='P' and a.`doctor_name`='"+doctorName+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctor(String ipd_id) { 
		String query = "SELECT `doctor_name`, `doctor_id`, `start_date`, `start_time`, `end_date`, `end_time` FROM `ipd_doctor_incharge` WHERE `ipd_id`='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveInsuranceIpdBedData(String ipd_id) { 
		String query = "SELECT T.ipd_id,C.ward,T.bed_date,'1' as qty,C.charges as ward_charge,sum(T.minutes)\r\n"
				+ "	FROM (\r\n"
				+ "	/**** ## Query to calculate minutes from start_date time to the end of the day *****/  \r\n"
				+ "	SELECT\r\n"
				+ "	    A.ipd_id as ipd_id,\r\n"
				+ "	    A.p_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "		A.start_date AS bed_date,\r\n"
				+ "	    CASE \r\n"
				+ "	    	when A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN   TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) , ' ',(case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p')) ELSE   TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(A.start_date, ' ','11:59:59 pm'),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'))\r\n"
				+ "	    END	AS minutes\r\n"
				+ "	FROM\r\n"
				+ "			ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'		\r\n"
				+ "		/**** ## Query end *****/  \r\n"
				+ "UNION ALL	\r\n"
				+ "/**** && Query to calculate minutes from day start to the end_date time *****/  \r\n"
				+ "		SELECT\r\n"
				+ "		A.ipd_id,\r\n"
				+ "		A.p_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "	    (case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) AS bed_date,\r\n"
				+ "		CASE \r\n"
				+ "			WHEN A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN null \r\n"
				+ "                               else TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
				+ "                               ,' ','12:00:00 am'),'%Y-%m-%d %h:%i:%s %p'),\r\n"
				+ "	             STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end),' ',\r\n"
				+ "	                           (case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
				+ "	    	                   '%Y-%m-%d %h:%i:%s %p'))\r\n"
				+ "		END	AS minutes\r\n"
				+ "	FROM\r\n"
				+ "		ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'	\r\n"
				+ "		/**** && Query end*****/  \r\n"
				+ "UNION ALL \r\n"
				+ "/**** @@ This query get all In Between dates of start_date to end_date with 1440 minutes *****/  \r\n"
				+ "SELECT  A.ipd_id,\r\n"
				+ "		A.p_id,\r\n"
				+ "        A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat\r\n"
				+ "		,ADDDATE(A.start_date,n) as bed_date, -- here we add n in the start date\r\n"
				+ "		24*60 as minutes\r\n"
				+ "		FROM ( \r\n"
				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
				+ "SELECT \r\n"
				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
				+ "         FROM (\r\n"
				+ "                /**** $$ This query get ***/\r\n"
				+ "                  SELECT 1 as c, ((((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
				+ "(SELECT @row_number := 0) AS x\r\n"
				+ "ORDER BY n asc \r\n"
				+ "/**** %% Query end *****/  \r\n"
				+ ")T,ipd_bed_details A where n>0 and n<DATEDIFF((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
				+ " ,A.start_date) and A.ipd_id='"+ipd_id+"'  \r\n"
				+ "/**** @@ Query end *****/\r\n"
				+ "	) T 	\r\n"
				+ "left join patient_detail B on\r\n"
				+ "		  T.p_id = B.pid2\r\n"
				+ "left join insurance_bed_detail C on\r\n"
				+ "	T.ward_cat = C.ward_type\r\n"
				+ "and B.p_insurancetype = C.ins_name\r\n"
				+ "and C.ins_category = B.insurance_category\r\n"
				+ "WHERE minutes>0\r\n"
				+ "GROUP by bed_date\r\n"
				+ "order by bed_date,MAX(ward_charge) desc\r\n"
				+ "\r\n"
				+ "\r\n"
				+ "";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctor2(String ipd_id) { 
		String query = "SELECT `doctor_name`,`start_date`,`start_time` FROM `ipd_doctor_incharge` WHERE `ipd_id`='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorNew(String ipd_id) { 
		String query = " SELECT `handover_charge_id`,`doctor_name`,`start_date`,`start_time`,(case when `end_date`='0000-00-00' then '' else `end_date` end) as end_date,(case when `end_time`='n/a' then '' else `end_time` end) as end_time FROM `ipd_doctor_incharge` WHERE `ipd_id`='"+ipd_id+"' order by 1 "
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
	public ResultSet retrieveAllRecieptID(String ipd_id) { 
		String query = "SELECT `p_id`, `p_name`, `ipd_entry_date`, `ipd_advanced_payment` FROM `ipd_entery` WHERE `ipd_id2`='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDoctorIndoor(String dateFrom, String dateTo,String doctorName) { 
		String query = "SELECT  `ipd_id`,`doctor_id` FROM `ipd_doctor_incharge` WHERE `start_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `doctor_name`='"+doctorName+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataPatientID(String patientID) {
		String query = "SELECT `ipd_id`,`ipd_id2`,`ipd_building`, `ipd_ward`, `ipd_bed_no`,`ipd_advanced_payment`,`ipd_entry_date`, `ipd_enter_time`, `ipd_ward_code`,`ipd_text3`,`ipd_package_name`, `ipd_operation_name`, `ipd_package_amount`,REPLACE(FORMAT(`ipd_total_charges`,2),',',''),`p_scheme`,`surgical_code`,`approved_amnt` FROM ipd_entery WHERE `p_id` = '"
				+ patientID+"' AND `ipd_discharged`='No'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientIDNEW(String patientID) {
		String query = "SELECT `ipd_id`,`ipd_id2`,`ipd_building`, `ipd_ward`, `ipd_bed_no`,`ipd_advanced_payment`,`ipd_entry_date`, `ipd_enter_time`, `ipd_ward_code`,`ipd_text3`,`ipd_package_name`, `ipd_operation_name`, `ipd_package_amount`,REPLACE(FORMAT(`ipd_total_charges`,2),',',''),`p_scheme`,`surgical_code`,`approved_amnt` FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"' AND `ipd_discharged`='No' order by ipd_id DESC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID3(String patientID) {
		String query = "SELECT `ipd_id`,`ipd_id2`,`ipd_entry_date`, REPLACE(FORMAT(`ipd_total_charges`,2),',','') FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID2(String patientID) {
		String query = "SELECT `ipd_id`,`ipd_id2`,`ipd_building`, `ipd_ward`, `ipd_bed_no`,`ipd_advanced_payment`,`ipd_entry_date`, `ipd_enter_time`, `ipd_ward_code`,`ipd_text3`,`ipd_package_name`, `ipd_operation_name`, `ipd_package_amount` FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID(String patientID,String ipd_id) {
		String query = "SELECT `ipd_id`,`ipd_id2`,`ipd_building`, `ipd_ward`, `ipd_bed_no`,`ipd_advanced_payment`,`ipd_entry_date`, `ipd_enter_time`, `ipd_ward_code`,ipd_text1,`ipd_discharged`,`ipd_recieved_amount`,`ipd_discharge_date`,`ipd_discharge_time` FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"' AND `ipd_id2`='"+ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID1(String patientID) {
		String query = "SELECT `ipd_id2`,`ipd_entry_date` FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithIPDId(String ipd_id) { 
		String query = "SELECT `p_id`, `p_name`, `ipd_doctor_refference`, `ipd_entry_date`, REPLACE(FORMAT(`ipd_total_charges`,2),',',''),`ipd_id`  FROM `ipd_entery` WHERE ipd_id2='"
				+ ipd_id +"' AND `ipd_discharged`='No' ORDER BY ipd_id ASC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID1(String patientID,String dateFrom,String dateTo) {
		String query = "SELECT `ipd_id2`,`ipd_entry_date` FROM `ipd_entery` WHERE `p_id` = '"
				+ patientID+"' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataIPDID(String ipd_id) {
		String query = "SELECT `ipd_id2`, `p_id`, `p_name`, `ipd_ward`, `ipd_bed_no`, `ipd_entry_date`, `ipd_enter_time`, `ipd_advanced_payment`, REPLACE(FORMAT(`ipd_total_charges`,2),',',''), `ipd_balance`, `ipd_recieved_amount`, `ipd_discharge_date`, `ipd_discharge_time`,`emergency_opd`,`insurance_category`,`discharge_type`,`ipd_text2` FROM `ipd_entery` WHERE `ipd_id` = "
				+ ipd_id+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveWardTransfer(String ipd_id,String change_id) {
		String query = "SELECT `ipd_ward` FROM `ipd_bed_details` WHERE `ipd_id`= '"+ipd_id+"' AND `change_id`<= "+change_id+" ORDER BY `change_id` DESC LIMIT 2";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientWardsCharges(String ipd_id,String ward_type) {
		String query = "SELECT 	SUM(ward_charges_rate) as ward_charges_rate,MIN( start_date) as start_date,start_time ,CASE when MAX(end_date)='0000-00-00' THEN '' else MAX(end_date) end as end_date ,(SELECT end_time from ipd_bed_details WHERE `ipd_id` ='"+ipd_id+"'\r\n"
				+ "	AND `ipd_ward_category` = '"+ward_type+"' ORDER by end_time DESC limit 1) as end_time,(SELECT SUM(ward_charges_rate) FROM ipd_bed_details WHERE `ipd_id` ='"+ipd_id+"') FROM `ipd_bed_details` WHERE `ipd_id` = '"+ipd_id+"' AND `ipd_ward_category` = '"+ward_type+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientBedDetailsNEW(String ipd_id) {
//		String query = "SELECT T5.ipd_id,T5.ward,T5.ward_cat,T5.bed_date,T5.minutes,T5.ward_charge,\r\n"
//				+ "CASE\r\n"
//				+ "	when IE.ipd_entry_date=CURRENT_DATE() and (T5.minutes+0)<720 then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	when IE.ipd_entry_date<>CURRENT_DATE() and T5.bed_date=IE.ipd_entry_date  and STR_TO_DATE(IE.ipd_enter_time,'%h:%i:%s %p') > '12:00:00' then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	when IE.ipd_entry_date<>CURRENT_DATE() and T5.bed_date=CURRENT_DATE() and CURRENT_TIME()  > '12:00:00' then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	else T5.ward_charge\r\n"
//				+ "END as Actual_charge\r\n"
//				+ "FROM (SELECT * from (select * from (SELECT \r\n"
//				+ "CASE when T2.bed_date =(SELECT ie.ipd_entry_date from ipd_entery ie where ie.ipd_id2='"+ipd_id+"')  or \r\n"
//				+ "	T2.bed_date =CURRENT_DATE()  then T2.sr else 0 end as grp,T2.* \r\n"
//				+ "FROM (\r\n"
//				+ "/** $$ Query start **/ \r\n"
//				+ "SELECT \r\n"
//				+ "    (@row_number := @row_number + 1)\r\n"
//				+ "    AS sr ,T1.*\r\n"
//				+ "FROM (\r\n"
//				+ "/** !! Query to get all beds with thier minutes and charges**/\r\n"
//				+ "SELECT T.ipd_id,T.ward,T.ward_cat,T.bed_date,sum(T.minutes) as minutes,wc.charges as ward_charge\r\n"
//				+ "	FROM (\r\n"
//				+ "	/**** ## Query to calculate minutes from start_date time to the end of the day *****/  \r\n"
//				+ "	SELECT\r\n"
//				+ "	    A.ipd_id as ipd_id,\r\n"
//				+ "		A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat,\r\n"
//				+ "		A.start_date AS bed_date,\r\n"
//				+ "	    CASE \r\n"
//				+ "	    	when A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN   TIMESTAMPDIFF(MINUTE, \r\n"
//				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) , ' ',(case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p')) ELSE   TIMESTAMPDIFF(MINUTE, \r\n"
//				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(A.start_date, ' ','11:59:59 pm'),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'))\r\n"
//				+ "	    END	AS minutes\r\n"
//				+ "	FROM\r\n"
//				+ "			ipd_bed_details A\r\n"
//				+ "	WHERE\r\n"
//				+ "		ipd_id = '"+ipd_id+"'		\r\n"
//				+ "		/**** ## Query end *****/  \r\n"
//				+ "UNION ALL	\r\n"
//				+ "/**** && Query to calculate minutes from day start to the end_date time *****/  \r\n"
//				+ "		SELECT\r\n"
//				+ "		A.ipd_id,\r\n"
//				+ "		A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat,\r\n"
//				+ "	    (case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) AS bed_date,\r\n"
//				+ "		CASE \r\n"
//				+ "			WHEN A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN null \r\n"
//				+ "                               else TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
//				+ "                               ,' ','12:00:00 am'),'%Y-%m-%d %h:%i:%s %p'),\r\n"
//				+ "	             STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end),' ',\r\n"
//				+ "	                           (case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
//				+ "	    	                   '%Y-%m-%d %h:%i:%s %p'))\r\n"
//				+ "		END	AS minutes\r\n"
//				+ "	FROM\r\n"
//				+ "		ipd_bed_details A\r\n"
//				+ "	WHERE\r\n"
//				+ "		ipd_id = '"+ipd_id+"'	\r\n"
//				+ "		/**** && Query end*****/  \r\n"
//				+ "UNION ALL \r\n"
//				+ "/**** @@ This query get all In Between dates of start_date to end_date with 1440 minutes *****/  \r\n"
//				+ "SELECT  A.ipd_id,\r\n"
//				+ "        A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat\r\n"
//				+ "		,ADDDATE(A.start_date,n) as bed_date, -- here we add n in the start date\r\n"
//				+ "		24*60 as minutes\r\n"
//				+ "		FROM ( \r\n"
//				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
//				+ "SELECT \r\n"
//				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
//				+ "         FROM (\r\n"
//				+ "                /**** $$ This query get ***/\r\n"
//				+ "                  SELECT 1 as c, ((((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
//				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
//				+ "(SELECT @row_number := 0) AS x\r\n"
//				+ "ORDER BY n asc \r\n"
//				+ "/**** %% Query end *****/  \r\n"
//				+ ")T,ipd_bed_details A where n>0 and n<DATEDIFF((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
//				+ " ,A.start_date) and A.ipd_id='"+ipd_id+"'  \r\n"
//				+ "/**** @@ Query end *****/\r\n"
//				+ "	) T \r\n"
//				+ "	left join ward_cahrges wc on\r\n"
//				+ "	wc.ward_type =T.ward_cat\r\n"
//				+ "	GROUP by T.ward_cat,T.bed_date,T.ward\r\n"
//				+ "	order by bed_date,max(minutes) desc	\r\n"
//				+ "	/** !! Query End**/\r\n"
//				+ "	)T1,\r\n"
//				+ "(SELECT @row_number := 0) AS x\r\n"
//				+ "/*** $$ Query End  **/)T2)T3\r\n"
//				+ "group by T3.bed_date,T3.grp\r\n"
//				+ "order by T3.bed_date,max(T3.ward_charge+0) desc\r\n"
//				+ ")T4\r\n"
//				+ "GROUP by T4.bed_date \r\n"
//				+ "order by T4.bed_date )T5\r\n"
//				+ "left join ipd_entery IE on\r\n"
//				+ "IE.ipd_id2 ='"+ipd_id+"'\r\n"
//				+ "\r\n"
//				+ "";
		String query_new="SELECT T3.ipd_id,T3.ward,T3.ward_cat,T3.bed_date,(T3.bed_charge/T3.charges) as percentage,T3.charges,T3.bed_charge from(SELECT T2.*,\r\n"
				+ "CASE \r\n"
				+ "	when 0<=(T2.bed_hrs+0) and (T2.bed_hrs+0)<=3 THEN (20/100)*(T2.charges+0)\r\n"
				+ "	when 3<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=7 THEN (40/100)*(T2.charges+0)\r\n"
				+ "	when 7<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=12 THEN (60/100)*(T2.charges+0)\r\n"
				+ "	when 12<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=18 THEN (80/100)*(T2.charges+0)\r\n"
				+ "    when 18<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=24 THEN (T2.charges+0)\r\n"
				+ "END as bed_charge\r\n"
				+ "from (   SELECT T1.*,wc.charges,ROUND(sum(T1.minutes)/60) as bed_hrs  from (SELECT\r\n"
				+ "	    A.ipd_id as ipd_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "		A.start_date AS bed_date,\r\n"
				+ "	    if(A.start_date=(if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)),TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)) , ' ',(if(A.end_time='n/a',TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\"),A.end_time))),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p')),  TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(A.start_date, ' ','11:59:59 pm'),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'))) AS minutes\r\n"
				+ "	FROM\r\n"
				+ "			ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'		\r\n"
				+ "		/**** ## Query end *****/  \r\n"
				+ "UNION ALL	\r\n"
				+ "/**** && Query to calculate minutes from day start to the end_date time *****/  \r\n"
				+ "		SELECT\r\n"
				+ "		A.ipd_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "	    (if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)) AS bed_date,\r\n"
				+ "	     if(A.start_date=(if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)), null \r\n"
				+ "                               ,TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date))\r\n"
				+ "                               ,' ','12:00:00 am'),'%Y-%m-%d %h:%i:%s %p'),\r\n"
				+ "	             STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)),' ',\r\n"
				+ "	                           (if(A.end_time='n/a',TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\"),A.end_time))),\r\n"
				+ "	    	                   '%Y-%m-%d %h:%i:%s %p')))AS minutes\r\n"
				+ "	FROM\r\n"
				+ "		ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'	\r\n"
				+ "	HAVING minutes>0	\r\n"
				+ "		/**** && Query end*****/  	\r\n"
				+ "UNION ALL \r\n"
				+ "/**** @@ This query get all In Between dates of start_date to end_date with 1440 minutes *****/ \r\n"
				+ "SELECT  A.ipd_id,	\r\n"
				+ "        A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat\r\n"
				+ "		,ADDDATE(A.start_date,n) as bed_date, -- here we add n in the start date\r\n"
				+ "		24*60 as minutes\r\n"
				+ "		FROM ( \r\n"
				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
				+ "SELECT \r\n"
				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
				+ "         FROM (\r\n"
				+ "                /**** $$ This query get ***/\r\n"
				+ "                  SELECT 1 as c, ((\r\n"
				+ "                  ((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
				+ "(SELECT @row_number := 0) AS x\r\n"
				+ "ORDER BY n asc \r\n"
				+ "/**** %% Query end *****/  \r\n"
				+ ")T,ipd_bed_details A where n>0 and n<DATEDIFF((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date))\r\n"
				+ " ,A.start_date) and A.ipd_id='"+ipd_id+"'\r\n"
				+ " )T1 left join ward_cahrges wc on\r\n"
				+ " wc.ward_type =T1.ward_cat\r\n"
				+ " GROUP BY ward_cat,bed_date\r\n"
				+ " order by bed_date,max(wc.charges))T2)T3";
		System.out.print(query_new);
		try {
			rs = statement.executeQuery(query_new);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientOTDetails(String ipd_id) {
		String query = "SELECT `entry_id`,`start_date`, `start_time`, `end_time`, `user_name` FROM `ipd_ot_details` WHERE `ipd_id` = '"
				+ ipd_id+"'";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientTotalDaysWards(String ipd_id,String wardName) {
		String query = "SELECT SUM(DATEDIFF(CONCAT(if(`end_date`='0000-00-00',CURDATE(),`end_date`),' ',if(`end_time`='n/a',TIME_FORMAT(CURRENT_TIME(), '%h:%i:%s %p'),`end_time`)),CONCAT(`start_date`,' ', `start_time`))) FROM `ipd_bed_details` WHERE `ipd_id` = '"
				+ ipd_id+"' AND `ipd_ward`='"+wardName+"'";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveWardCharges(String wardName) {
		String query = "SELECT C.`charges` FROM `ward_management` M LEFT JOIN `ward_cahrges` C ON M.`ward_category`=C.`ward_type` WHERE `ward_name`='"+wardName+"' ORDER BY C.`charge_id` DESC LIMIT 1";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientWards() {
		String query = "select DISTINCT ward_category  from ward_management";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrievePatientWardData(String wardName,String fromDate,String toDate) {
		String query = "SELECT I.`p_id` , I.`p_name` , P.`p_sex` , P.`p_addresss` , P.`p_city` , P.`p_telephone`,I.`ipd_ward`,I.`start_date` FROM  `ipd_bed_details` I LEFT JOIN  `patient_detail` P ON I.`p_id` = P.`pid1` WHERE `ipd_ward`='"+wardName+"' AND `start_date` BETWEEN  '"+fromDate+"' AND  '"+toDate+"' GROUP BY I.`p_id`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientBedDetails1(String ipd_id) {
		String query = "SELECT  `ipd_building`, `ipd_ward`, `ipd_room`, `ipd_bed_no`, `start_date`, `start_time`,if(`end_date`='0000-00-00',CURDATE(),`end_date`),if(`end_time`='n/a',TIME_FORMAT(CURRENT_TIME(), '%h:%i:%s %p'),`end_time`)  FROM `ipd_bed_details` WHERE `ipd_id` = '"
				+ ipd_id+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientPackage(String ipd_id) {
		String query = "SELECT `procedure_name`, `procedure_charges`, `procedure_pre_days`, `procedure_post_days` FROM `ipd_expenses` I LEFT JOIN `procedures_detail` P ON I.`charges_id`=P.`procedure_id` WHERE I.`ipd_id`='"+ipd_id+"' AND I.`expense_text3`='PACKAGE'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrievePatientPackageDays(String ipd_id) {
		//		String query = "SELECT SUM(`procedure_pre_days`+`procedure_post_days`) FROM `ipd_expenses` I LEFT JOIN `procedures_detail` P ON I.`charges_id`=P.`procedure_id` WHERE I.`ipd_id`='"+ipd_id+"' AND expense_text3='PACKAGE'";
		String query = "SELECT SUM(`procedure_pre_days`+`procedure_post_days`) FROM `ipd_expenses` I LEFT JOIN `procedures_detail` P ON I.`charges_id`=P.`procedure_id` WHERE I.`ipd_id`='"+ipd_id+"' AND I.`expense_text3`='PROCEDURE'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorIPD(String Doctor) {
		String query = "SELECT a.`ipd_id2`, a.`p_id`, a.`p_name`, a.`ipd_ward`, a.`ipd_bed_no`, a.`ipd_entry_date`,b.`text1`,b.`text2` FROM `ipd_entery` a, `ipd_doctor_incharge` b WHERE a.`ipd_id` = b.`ipd_id` AND b.`doctor_name`='"
				+ Doctor+"' AND a.`ipd_discharged`='No' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataIPD() {
		String query = "SELECT a.`ipd_id2`, a.`p_id`, a.`p_name`, a.`ipd_ward`, a.`ipd_bed_no`, a.`ipd_entry_date`,b.`text1`,b.`text2` FROM `ipd_entery` a, `ipd_doctor_incharge` b WHERE a.`ipd_id` = b.`ipd_id`  AND a.`ipd_discharged`='No' and `ipd_entry_date`>'2020-07-14'";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataIPDSearch(String index) {
		String query = "SELECT a.`ipd_id2`, a.`p_id`, a.`p_name`, a.`ipd_ward`, a.`ipd_bed_no`, a.`ipd_entry_date`,b.`text1`,b.`text2` FROM `ipd_entery` a, `ipd_doctor_incharge` b WHERE a.`ipd_id` = b.`ipd_id`  AND a.`ipd_discharged`='No' and  a.`ipd_id2` LIKE '%"+index+"%'";
		//		String query = "SELECT a.`ipd_id2`, a.`p_id`, a.`p_name`, a.`ipd_ward`, a.`ipd_bed_no`, a.`ipd_entry_date`,b.`text1`,b.`text2` FROM `ipd_entery` a, `ipd_doctor_incharge` b WHERE a.`ipd_id` = b.`ipd_id`  AND a.`ipd_discharged`='No' and  ipd_id LIKE '%"+index+"%' OR store_name LIKE '%"+index+"%'";
		System.out.print(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveALLPatientsdays_amount(String dateFrom,String dateTo) {
		String query = "SELECT `p_name`,CASE WHEN CASE WHEN `ipd_discharged` ='No' THEN CASE WHEN `ipd_entry_date`>'"+dateFrom+"' THEN DATEDIFF('"+dateTo+"','"+dateFrom+"') ELSE DATEDIFF('"+dateTo+"',`ipd_entry_date`) END ELSE CASE WHEN `ipd_entry_date`<'"+dateFrom+"' THEN DATEDIFF(`ipd_discharge_date`,'"+dateFrom+"' ) ELSE DATEDIFF(`ipd_discharge_date`,`ipd_entry_date` ) END END = 0 THEN '1' ELSE CASE WHEN `ipd_discharged` ='No' THEN DATEDIFF('"+dateTo+"',`ipd_entry_date`) ELSE DATEDIFF(`ipd_discharge_date`,`ipd_entry_date` ) END END AS DAYS,CASE WHEN `ipd_discharged` ='No' THEN `ipd_advanced_payment` ELSE `ipd_recieved_amount`+`ipd_advanced_payment` END AS AMOUNT FROM `ipd_entery` WHERE `ipd_payment_recieved` !='CANCELLED' AND (`ipd_entry_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' OR `ipd_discharge_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"')";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM ipd_entery WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}


	public int inserIPD_OT_Detail(String[] data) throws Exception {
		String insertSQL ="INSERT INTO `ipd_ot_details`(`ipd_id`, `p_id`, `p_name`, `ipd_ward`, `start_date`, `start_time`, `end_date`, `end_time`,`user_id`, `user_name`,`ot_id`,`ot_name`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 13; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}



	public int inserIPDBedDetail(String[] data) throws Exception {
		String insertSQL ="INSERT INTO `ipd_bed_details`( `ipd_id`, `p_id`, `p_name`, `ipd_building`, `ipd_ward`, `ipd_room`, `ipd_bed_no`, `start_date`, `start_time`,`note1`,`ipd_ward_category`,`text1`,`text2`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 14; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}


	public int inserIPDDoctor(String[] data) throws Exception {
		String insertSQL =" INSERT INTO `ipd_doctor_incharge`(`ipd_id`, `doctor_name`, `doctor_id`, `p_id`, `p_name`, `start_date`, `start_time`) VALUES (?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 8; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}

	//
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `ipd_entery`(`ipd_id2`,`p_id`, `p_name`, `ipd_doctor_refference`, `ipd_building`, `ipd_ward`, `ipd_room`, `ipd_bed_no`, `ipd_ward_code`, `ipd_package_name`, `ipd_operation_name`, `ipd_package_amount`,`ipd_entry_date`, `ipd_enter_time`, `ipd_advanced_payment`,`ipd-entry_user`,`ipd_text3`,`insurance_type`, `doctor_name`,`p_scheme`,`surgical_code`,`approved_amnt`,`emergency_opd`,`remarks`,`ipd_empanelled_p_ref_no`,`is_cashless`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 27; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return  rs.getInt(1);
	}
	public void UpdateIpdId2(String ipd_id)
	{
		String query="update ipd_entery set ipd_id2='"+ipd_id+"' where ipd_id ='"+ipd_id+"'";
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public int inserDataDischargeSummary(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `discharge_summary_table`(`message_body`,`ipd_id`,`patient_id`,`patient_name`) VALUES (?,?,?,?)";
		System.out.println(insertSQL);
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
	//	public int inserData(String[] data) throws Exception {
	//		String insertSQL = "INSERT INTO `ipd_entery`(`ipd_id2`,`p_id`, `p_name`, `ipd_doctor_refference`, `ipd_building`, `ipd_ward`, `ipd_room`, `ipd_bed_no`, `ipd_ward_code`, `ipd_package_name`, `ipd_operation_name`, `ipd_package_amount`,`ipd_entry_date`, `ipd_enter_time`,`ipd-entry_user`,`ipd_text3`,`insurance_type`, `doctor_name`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	//		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
	//		for (int i = 1; i < 20; i++) {
	//
	//			preparedStatement.setString(i, data[i - 1]);
	//		}
	//		 preparedStatement.executeUpdate();
	//		 ResultSet rs = preparedStatement.getGeneratedKeys();
	//		  rs.next();
	//		 
	//		 return  rs.getInt(1);
	//	}


	public ResultSet searchIPDDischargeSummary(String index)
	{
		String query="SELECT message_body,ipd_id,patient_id,patient_name FROM `discharge_summary_table` where ipd_id LIKE '%"+index+"%' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public void UpdateClaimID(String index, String ipd)
	{
		String query="UPDATE `ipd_entery` SET `ipd_empanelled_p_ref_no`='"+index+"' where `ipd_id2`='"+ipd+"'";
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public void UpdateClaimIDDichargeType(String index, String ipd,String type)
	{
		String query="UPDATE `ipd_entery` SET `ipd_empanelled_p_ref_no`='"+index+"',`discharge_type`='"+type+"' where `ipd_id2`='"+ipd+"'";
		try {
			statement.executeUpdate(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
	}
	public ResultSet retrieveAllDataWithIpdId(String ipd_id) { 
		String query = "SELECT `p_id`,`p_name`,`ipd_entry_date`,`ipd_discharge_date`,`doctor_name` FROM `ipd_entery` WHERE ipd_id ='"
				+ ipd_id +"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateDoctorEndTime(String ID,String end_date,String end_time) throws Exception
	{
		statement.executeUpdate("UPDATE `ipd_doctor_incharge` SET `end_date`='"+end_date+"',`end_time`='"+end_time+"' WHERE `handover_charge_id`='"+ID+"'");
	}
	public void updateDischargeSummary(String[] data) throws Exception
	{
		String str=data[0].replace("\'", "\\'");
		//		Replace(data[0], "", """");
		System.out.println("UPDATE `discharge_summary_table` SET `message_body`='"+str+"',`patient_id`='"+data[2]+"',`remarks`='"+data[4]+"' where `ipd_id` = "+data[1]);
		statement.executeUpdate("UPDATE `discharge_summary_table` SET `message_body`='"+str+"',`patient_id`='"+data[2]+"',`remarks`='"+data[4]+"' where `ipd_id` = "+data[1]);
	}


	
}
