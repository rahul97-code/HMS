package hms.doctor.database;

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

public class DoctorDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public DoctorDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	
	public ResultSet getAllDocOfIpd(String fromDate,String toDate) {
		String query = "SELECT\r\n"
				+ "	idi.handover_charge_id,idi.ipd_id as ipd,idi.doctor_name,idi.p_name,idi.p_id,concat(ie.ipd_entry_date,' ',ie.ipd_enter_time) DOA,\r\n"
				+ "	TIMESTAMPDIFF(minute , \r\n"
				+ "	        STR_TO_DATE(CONCAT(idi.start_date, ' ', idi.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),\r\n"
				+ "	    STR_TO_DATE(CONCAT(if(idi.end_date='0000-00-00',ie.ipd_discharge_date,idi.end_date), ' ', if(idi.end_time='n/a',ie.ipd_discharge_time,idi.end_time)),\r\n"
				+ "	    	'%Y-%m-%d %h:%i:%s %p')) AS minutes,ie.insurance_type,\r\n"
				+ "	    	COALESCE(t.amt,0) as Discount\r\n"
				+ "from\r\n"
				+ "ipd_entery ie\r\n"
				+ "left join ipd_doctor_incharge idi on idi.ipd_id =ie.ipd_id \r\n"
				+ "left join (select t.ipd_id,sum(t.amt)as amt from (select ie.ipd_id as ipd_id,ie.expense_amount as amt  from ipd_expenses ie where ie.expense_name like '%discoun%' \r\n"
				+ "union all\r\n"
				+ "select ip.ipd_id,concat('-',ip.payment_amount) from ipd_payments ip where ip.payment_type='ks')t group by 1)t on t.ipd_id=ie.ipd_id \r\n"
				+ "where\r\n"
				+ "	 ie.ipd_discharge_date BETWEEN '"+fromDate+"' and '"+toDate+"' and idi.ipd_id in ((SELECT distinct ipd_id from ipd_expenses ie where ie.charges_id=1149))\r\n"
				+ "GROUP by 1 \r\n"
				+ "order by 1,2";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet getAllOtIpd(String fromDate,String toDate) {
		String query = "select\r\n"
				+ "iod.entry_id,\r\n"
				+ "iod.ipd_id ,\r\n"
				+ "iod.p_id ,\r\n"
				+ "iod.p_name ,\r\n"
				+ "ie.ipd_entry_date as DOA,\r\n"
				+ "ie.insurance_type  as Insurance,\r\n"
				+ "ie.doctor_name as Primary_DOC,\r\n"
				+ "iod.start_time  ,\r\n"
				+ "iod.end_time  ,\r\n"
				+ "@a:=(select oc.charges  from ot_charges oc where oc.ot_id =iod.ot_id ) as `Per OT Charge`,\r\n"
				+ "@b:=TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT(iod.start_date, ' ', iod.start_time),'%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(iod.end_date, ' ', iod.end_time),'%Y-%m-%d %h:%i:%s %p')) as Ot_Time	\r\n"
				+ ",round((@a/60)*@b) as `OT Charges`\r\n"
				+ "from\r\n"
				+ "	ipd_ot_details iod\r\n"
				+ "left join ipd_entery ie on\r\n"
				+ "	ie.ipd_id = iod.ipd_id\r\n"
				+ "where ie.ipd_discharge_date between '"+fromDate+"' and '"+toDate+"'\r\n"
				+ "group by 2\r\n"
				+ "order by 2";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet getAllPateintBeds(String ipds) {
		String query = "SELECT\r\n"
				+ "		A.ipd_id,\r\n"
				+ "		A.ipd_ward as ward,\r\n"
				+ "		A.ipd_ward_category as ward_cat,\r\n"
				+ "		A.start_date as bed_date,\r\n"
				+ "		sum(TIMESTAMPDIFF(minute, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),\r\n"
				+ "	    case\r\n"
				+ "	     	when STR_TO_DATE(CONCAT(A.end_date, ' ', A.end_time),\r\n"
				+ "	    	'%Y-%m-%d %h:%i:%s %p') is null then now()\r\n"
				+ "	    	else STR_TO_DATE(CONCAT(A.end_date, ' ', A.end_time),\r\n"
				+ "	    	'%Y-%m-%d %h:%i:%s %p')\r\n"
				+ "			end)) AS minutes\r\n"
				+ "	from\r\n"
				+ "			ipd_bed_details A\r\n"
				+ "	where A.ipd_id in ("+ipds+")		\r\n"
				+ "	group by\r\n"
				+ "		1,2\r\n"
		        + "	order by 1,4";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getAllPateintOTExams(String fromDate,String toDate) {
		String query = "select ie.ipd_id as ipd_no,\r\n"
				+ "	ee.exam_pid,\r\n"
				+ "	ee.exam_nameid,\r\n"
				+ "	(CONCAT(LEFT(ee.exam_name, LOCATE('OT CHARGES',ee.exam_name) - 1),SUBSTRING(ee.exam_name FROM LOCATE('OT CHARGES', ee.exam_name) + LENGTH('OT CHARGES')))) as Exam_Name,\r\n"
				+ "	ee.exam_charges,\r\n"
				+ "	ee.exam_doctorreff\r\n"
				+ "from\r\n"
				+ "	ipd_entery ie\r\n"
				+ "left join exam_entery ee on ee.exam_pid =ie.p_id and ee.exam_date between ie.ipd_entry_date and ie.ipd_discharge_date 	\r\n"
				+ "where\r\n"
				+ "	ie.ipd_discharge_date between '"+fromDate+"' and '"+toDate+"' and	(ee.exam_nameid between 99000 and 100000) and ee.exam_performed <>'cancel'	\r\n"
				+ "	order by 1,3";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getDocter() {
		String query = "SELECT doc_name FROM doctor_detail";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet MedDiscountAccess(String id) {
		String query = "SELECT med_discount_access FROM doctor_detail where doc_id='"+id+"'";
		System.out.println(query);
		try {
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
	
	
	public void updateData(String[] data) throws Exception {
		String insertSQL = "UPDATE `doctor_detail` SET `doc_name`=?,`doc_username`=?,`doc_password`=?,`doc_specialization`=?,`doc_opdroom`=?,`doc_telephone`=?,`doc_address`=?,`doc_qualification`=?,`doc_text1`=?,`doctor_discount`=?,`doctor_exam`=?,`doctor_opd`=? WHERE `doc_id` = "
				+ data[12] + "";

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 13; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}

	public void updateDoctorAvailability(ArrayList<String> data, String index)
			throws Exception {
		String insertSQL = "UPDATE `doctor_availability` SET `doc_frequency`=?,`doc_days`=?,`doc_time_from`=?,`doc_time_to`=?,`doc_opd_start_time`=?,`doc_duration_per_patient`=?,`doc_no_of_appointments`=?,`doc_total_patient`=?,`doc_charges`=?,`doc_online_charges`=? WHERE `doc_entry_id`= "
				+ index;

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < data.size() + 1; i++) {

			preparedStatement.setString(i, data.get(i - 1));
		}
		preparedStatement.executeUpdate();
	}

	public void updateDataLastLogn(String docID) throws Exception {

		String timeStamp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a")
				.format(Calendar.getInstance().getTime());
		System.out.println(timeStamp);
		
		statement.executeUpdate("update `doctor_detail` set `doc_lastlogin` = '"
						+ timeStamp + "' where `doc_id` = " + docID);
	}

	public void updateDataStatus(String docID, String status) throws Exception {
		statement.executeUpdate("update `doctor_detail` set `doc_active` = '"
				+ status + "' where `doc_id` = " + docID);
	}

	public void updateDataCounter(String docID, String counter)
			throws Exception {

		statement.executeUpdate("UPDATE `doctor_detail` SET `doc_text2`='"
				+ counter + "' where `doc_id` = " + docID);
	}

	public void updateDataPassword(String docID, String password)
			throws Exception {

		statement.executeUpdate("UPDATE `doctor_detail` SET `doc_password`='"
				+ password + "' where `doc_id` = " + docID);
	}

	public int retrieveCounterData() {
		String query = "SELECT * FROM `patient_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
		try {
			while (rs.next()) {
				NumberOfRows++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfRows;
	}

	public ResultSet searchDocter(String doc) {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `doctor_detail` WHERE doc_name like '%"+doc+"%' and `doc_active`="+1;
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `doctor_detail` WHERE `doc_active`="+1;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataASC() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification` FROM `doctor_detail` WHERE `doc_text1`!='NOT' ORDER BY CAST( `doc_text1` AS unsigned ) ASC ";
//		System.out.println(query);
		
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String doct_type) {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`, `doc_qualification`  FROM `doctor_detail` WHERE `doc_text1`!='NOT' and `doctor_type`='"+doct_type+"'";
		System.out.println(query);
		
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllName() {
//		String query = "SELECT DISTINCT `opd_doctor` FROM `opd_entery`";
		String query = "SELECT DISTINCT `doc_name` FROM `doctor_detail` where doc_active=1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllData2(String index) {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_text1`,`doc_active` FROM `doctor_detail` WHERE doc_id LIKE '%"
				+ index + "%' OR doc_name LIKE '%" + index + "%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllData2() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_text1`,`doc_active` FROM `doctor_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllActiveData() {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`, `doc_telephone`,`doc_text1`,`doc_active` FROM `doctor_detail` WHERE 	`doc_active`='1'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDataWithIndex(String name) {
		String query = "SELECT `doc_id`, `doc_specialization`, `doc_opdroom`,`doctor_discount`,`doctor_exam`,`doctor_opd` FROM `doctor_detail` WHERE `doc_name` = '"
				+ name + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveData() {
		String query = "SELECT `doc_name` FROM `doctor_detail` ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDataWithID(String docID) {
		String query = "SELECT `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_qualification`,`doc_text1`,`doctor_discount`,`doctor_exam`, `doctor_opd` FROM `doctor_detail` WHERE `doc_id` = "
				+ docID + "";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveUserPassword(String name, String pass) {
		String query = "SELECT * FROM `doctor_detail` WHERE `doc_username` = '"
				+ name + "' AND `doc_password` = '" + pass + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveUserPasswordEMO(String name, String pass) {
		String query = "SELECT * FROM `doctor_detail` WHERE `doc_username` = '"
				+ name + "' AND `doc_password` = '" + pass + "' and `emo_active`=1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievePassword(String id) {
		String query = "SELECT `doc_password` FROM `doctor_detail` WHERE `doc_id` = '"
				+ id + "' ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveUsernameDetail(String name) {
		String query = "SELECT  `doc_id`, `doc_name`, `doc_specialization`, `doc_opdroom`,`doc_username`,`doc_password`,`doc_lastlogin`,`doc_text2`,`is_karuna` FROM `doctor_detail` WHERE `doc_username` = '"
				+ name + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet searchPatientWithIdOrNmae(String index) {
		String query = "SELECT * FROM `doctor_detail` where pid1 LIKE '%"
				+ index + "%' OR p_name LIKE '%" + index + "%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDoctorAvailability(String doctorname) {
		String query = "SELECT `doc_entry_id`, `doc_id`, `doc_name`, `doc_frequency`, `doc_days`, `doc_time_from`, `doc_time_to`,`doc_opd_start_time`, `doc_duration_per_patient`, `doc_no_of_appointments`, `doc_total_patient`, `doc_charges`,`doc_online_charges` FROM `doctor_availability` WHERE `doc_name` = '"
				+ doctorname + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDoctorAvailabilityDetail(String index) {
		String query = "SELECT `doc_entry_id`, `doc_id`, `doc_name`, `doc_frequency`, `doc_days`, `doc_time_from`, `doc_time_to`,`doc_opd_start_time`, `doc_duration_per_patient`, `doc_no_of_appointments`, `doc_total_patient`, `doc_charges`,`doc_online_charges` FROM `doctor_availability` WHERE `doc_entry_id` = "
				+ index + "";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveCancelDoctorAvailability(String doctorname) {
		String query = "SELECT `record_id` , `doctor_id`, `doctor_name`, `cancel_date`,`cancel_date_to`, `cancel_reason`, `entry_user` FROM `doctor_cancel_availability` WHERE `doctor_name` = '"
				+ doctorname + "' ORDER BY `record_id` DESC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveDoctorIPDData(String doctorName,String fromDate,String toDate,String type)
	{
	  String query="SELECT I.`ipd_id` AS 'IPD NO1', I.`ipd_id2` AS 'IPD NO', I.`p_id` AS 'PATIENT ID', I.`p_name` AS 'PATIENT NAME',(SELECT `p_insurancetype` FROM `patient_detail` WHERE `pid1`=I.`p_id`) AS 'INSURANCE TYPE' , D.`doctor_name` AS 'DOCTOR NAME', I.`ipd_package_name` AS 'PACKAGE NAME', I.`ipd_operation_name` AS 'OPERATION NAME', I.`ipd_package_amount` AS 'PACKAGE AMOUNT', I.`ipd_advanced_payment` AS 'ADVANCE PAYMENT', I.`ipd_total_charges` AS 'TOTAL CHARGES', I.`ipd_recieved_amount` AS 'TOTAL PAYMENT',I.`ipd_discharged` AS 'DISCHARGED',I.`ipd_entry_date` AS 'ENTRY DATE',I.`ipd_enter_time` AS 'ENTRY TIME', IF(I.`ipd_discharge_date`='0000-00-00','NOT DISCHARGED',I.`ipd_discharge_date`) AS 'DISCHARGE DATE', I.`ipd_discharge_time` AS 'DISCHARGE TIME' FROM `ipd_doctor_incharge` D INNER JOIN `ipd_entery` I ON D.`ipd_id` = I.`ipd_id` WHERE `doctor_name` = '"+doctorName+"' AND `start_date` BETWEEN '"+fromDate+"' AND '"+toDate+"' AND I.`ipd_operation_name`='"+type+"'";
		
	
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDoctorIPDExpenseData(String ipdID)
	{
	  String query="SELECT  `expense_name` AS 'EXPENSE NAME', `expense_amount` AS 'AMOUNT' FROM `ipd_expenses` WHERE `ipd_id`='"+ipdID+"' and `charges_id`='n/a'";
	 try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDoctorIPDExpenseMedData(String ipdID)
	{
	  String query="SELECT COUNT(  `expense_id` ) , SUM(`expense_amount`) AS 'AMOUNT' FROM `ipd_expenses` WHERE `ipd_id`='"+ipdID+"' and `charges_id`!='n/a'";
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	// //////////Reports

	public ResultSet retrieveDoctorYearIPD(String doctorName,String year)
	{
	  String query="SELECT MONTH( D.`start_date` ) , D.`doctor_name` , count( * ) , SUM( `ipd_total_charges` ) FROM `ipd_doctor_incharge` D INNER JOIN `ipd_entery` I ON D.`ipd_id` = I.`ipd_id` WHERE `doctor_name` = '"+doctorName+"' AND YEAR( `start_date` ) ="+year+" GROUP BY MONTH(D.`start_date`)";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDoctorYearExam(String doctorName,String year)
	{
	  String query="SELECT MONTH( `exam_date` ) ,`exam_doctorreff` , count( * ) , SUM( `exam_charges` ) FROM `exam_entery` WHERE `exam_doctorreff` = '"+doctorName+"' AND YEAR( `exam_date` ) ="+year+" GROUP BY MONTH(`exam_date`)";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	
	public ResultSet retrieveDoctorYearOPD(String doctorName,String year)
	{
	  String query="SELECT MONTH( `opd_date` ) ,`opd_doctor` , count( * ) , SUM( `opd_charge` ) FROM `opd_entery` WHERE `opd_doctor` = '"+doctorName+"' AND YEAR( `opd_date` ) ="+year+" GROUP BY MONTH(`opd_date`)";
		System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveDoctorYearMisc(String doctorName,String year)
	{
	  String query="SELECT MONTH( `misc_date` ) ,`misc_doctor_ref` , count( * ) , SUM( `misc_amount` ) FROM `misc_entry` WHERE `misc_doctor_ref` = '"+doctorName+"' AND YEAR( `misc_date` ) ="+year+" GROUP BY MONTH(`misc_date`)";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDoctorIPDEXPENSE(String doctorName,String fromDate,String toDate)
	{
	  String query="SELECT COUNT(`expense_name`),`expense_name`,SUM(`expense_amount`) FROM `ipd_doctor_incharge` D INNER JOIN `ipd_expenses` I ON D.`ipd_id` = I.`ipd_id` WHERE `doctor_name` = '"+doctorName+"' AND `expense_date` BETWEEN '"+ fromDate + "' AND '" + toDate + "'  GROUP BY `expense_name`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDoctorIPDEXPENSEAll(String doctorName,String fromDate,String toDate)
	{
	  String query="SELECT `expense_name`,`expense_amount`,`expense_date` FROM `ipd_doctor_incharge` D INNER JOIN `ipd_expenses` I ON D.`ipd_id` = I.`ipd_id` WHERE `doctor_name` = '"+doctorName+"' AND `expense_date` BETWEEN '"+ fromDate + "' AND '" + toDate + "'";
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
				.prepareStatement("DELETE FROM doctor_detail WHERE doc_id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public void deleteDoctorAvailability(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `doctor_availability` WHERE `doc_entry_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	public void deleteDoctorAvailabilityCancel(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM `doctor_cancel_availability` WHERE `record_id`=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int checkLeaves(String doctorID,String date) {
		String query="SELECT DATEDIFF(`cancel_date_to`, '"+date+"')+1 AS leaves  FROM `doctor_cancel_availability` WHERE `doctor_id`='"+doctorID+"' AND '"+date+"' BETWEEN `cancel_date` AND `cancel_date_to`" ;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		int days = 0;
		try {
			while (rs.next()) {
				days=Integer.parseInt(rs.getObject(1).toString());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return days;
	}
	public void inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `doctor_detail`( `doc_name`, `doc_username`, `doc_password`, `doc_specialization`, `doc_opdroom`, `doc_telephone`, `doc_address`, `doc_qualification`,`doc_text1`,`doctor_type`,`emo_active`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 12; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}

	public void inserAvailabiltyData(ArrayList<String> data) throws Exception {
		String insertSQL = "INSERT INTO `doctor_availability`(`doc_id`, `doc_name`, `doc_frequency`, `doc_days`, `doc_time_from`, `doc_time_to`,`doc_opd_start_time`, `doc_duration_per_patient`, `doc_no_of_appointments`,`doc_total_patient` , `doc_charges`,`doc_online_charges`,`doc_entry_user`,`doc_entry_date_time`) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 15; i++) {

			preparedStatement.setString(i, data.get(i - 1));
		}
		preparedStatement.executeUpdate();
	}

	public void inserAvailabiltyCancelData(ArrayList<String> data)
			throws Exception {
		String insertSQL = "INSERT INTO `doctor_cancel_availability`(`doctor_id`, `doctor_name`, `cancel_date`,`cancel_date_to`, `entry_user`, `cancel_reason`,`entry_date`) VALUES(?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 8; i++) {
			preparedStatement.setString(i, data.get(i - 1));
		}
		preparedStatement.executeUpdate();
	}
}
