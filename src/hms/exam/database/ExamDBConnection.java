package hms.exam.database;

import hms.main.DBConnection;
import hms.main.DateFormatChange;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import javax.swing.JOptionPane;

public class ExamDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public ExamDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	} 

	public ResultSet retrieveExamkarunadesc(String examname) {
		String query="select description from karun_sparsh_param where dept='"+examname+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveFreeTestData(String fromDate,String toDate) {
		String query = "select * from free_test_examination fte where is_deleted<>1 and `date` BETWEEN '"+fromDate+"' and '"+toDate+"' order by id desc";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet retrieveFamilyHistory() {
		String query = "select distinct history from free_test_examination";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public int insertFreeTestData(String[] data) throws Exception {
	    String insertSQL = "REPLACE INTO free_test_examination "
	            + "(id,receipt_id, p_id, p_name, p_age, gender, aadhar_no, mob_no, address, "
	            + "date, hba1c, blood_grp, bp, ht, wt, history, report_collected, "
	            + "consulted, user_id, user_name) "
	            + "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	    PreparedStatement preparedStatement = connection.prepareStatement(
	            insertSQL, Statement.RETURN_GENERATED_KEYS);
	    for (int i = 1; i <= 20; i++) {
	        preparedStatement.setString(i, data[i - 1]);
	    }
	    preparedStatement.executeUpdate();
	    ResultSet rs = preparedStatement.getGeneratedKeys();
	    if (rs.next()) {
	        return rs.getInt(1);
	    } else {
	        throw new SQLException("Insert failed, no ID obtained.");
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
	public ResultSet GetAllOTExams(int price_index,String Examcat,boolean AprovelAccess) { 

		String table_name = "exam_master",str="";
		boolean bool=Examcat!=null?false:true;
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
			if(!AprovelAccess)
			str="AND exam_rate<5000";
		}

		String query = "SELECT exam_code ,exam_desc ,exam_subcat ,exam_rate  from "+table_name+" where IF("+bool+" , exam_desc like '%"+Examcat+"%' "+str+" ,exam_desc like '"+Examcat+"' "+str+" ) AND `exam_text1`!='No' AND exam_code between 99001 and 99999 order by 2,3,1";
		System.out.println(query+"name ");
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllOTType() {
		String query = "SELECT DISTINCT ot_name,ot_id from ot_charges oc ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE); 
		}
		return rs;
	}
	public ResultSet retrieveAllOTExams(int price_index) {
		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}
		String query = "SELECT DISTINCT `exam_desc` FROM " + table_name+" where exam_code between 99001 and 99999";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE); 
		}
		return rs;
	}

	public ResultSet retrieveFreeCampTestData(String exam_code,String ins) {
		String query="SELECT  daily_limit, repeat_test_days, allow_printing,aadhaar_mandatory,age_limit\r\n"
				+ "FROM free_test_camp_master WHERE exam_code='"+exam_code+"' and insurance='"+ins+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet checkPatientFreeTestData(String pid,String exam_code,String repeat_days,String today_test_count) {
		String query="SELECT (SELECT IFNULL(\r\n"
				+ " (SELECT (DATEDIFF(CURRENT_DATE(), b.exam_date) >= '"+repeat_days+"')\r\n"
				+ "     FROM exam_entery b\r\n"
				+ "     WHERE b.exam_pid = '"+pid+"' AND b.exam_nameid = '"+exam_code+"'\r\n"
				+ "     ORDER BY b.exam_id DESC LIMIT 1),1)) as can_repeat_test,(COUNT(a.exam_nameid)<'"+today_test_count+"') AS can_do_today,\r\n"
				+ "     (SELECT IF(encrypted_aadhaar_no ='' or encrypted_aadhaar_no is null,0,1)\r\n"
				+ "from patient_detail pd where pid1 = '"+pid+"') as has_aadhaar\r\n"
				+ "FROM exam_entery a\r\n"
				+ "WHERE a.exam_nameid = '"+exam_code+"' AND a.exam_date = CURRENT_DATE()";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveBookingData(String pId,String receiptId) {
		String query="select IFNULL( CONCAT( DATE_FORMAT( pd.p_age , '%y' ) + DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( (SELECT `receipt__date`FROM `amountreceipt`WHERE `receipt_ref_doctor` = '\"+index+\"' LIMIT 1) ) ) , '%Y' ) , '-', DATE_FORMAT( pd.p_age , '%m' ) , '-', DATE_FORMAT( pd.p_age , '%d' ) ) , pd.p_age ) AS age , pd.p_addresss, pd.p_city , pd.p_telephone , pd.p_sex , ee.lis_code,ee.exam_charges,ee.exam_doctorreff FROM patient_detail pd left join exam_entery ee on pd.pid1 = ee.exam_pid "
				+ "WHERE ee.exam_pid ='"+pId+"' and ee.receipt_id ='"+receiptId+"' and (ee.lis_code <>'' or ee.lis_code <>'0') and (ee.workorder_id is null or ee.workorder_id='PENDING') GROUP by ee.lis_code ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateExamCancel(String receipt_id, String exam_id)
			throws Exception {
		String query="update `exam_entery` set `exam_performed` = 'Cancel' where `receipt_id` = '"+ receipt_id+"' and `exam_nameid` ='"+exam_id+"'";
		System.out.println(query);
		statement.executeUpdate(query);
	}
	public void updateExamData(String exam_id, String status,
			String examOperator, String examPerformed) throws Exception {
		statement.executeUpdate("update `exam_entery` set `exam_performed` = '"
				+ examPerformed + "',`exam_reportno`='" + status
				+ "',`exam_operator`='" + examOperator + "' where `exam_id` = "
				+ exam_id);
	}
	public void updateXrayExamData( String status,
			String examOperator, String examPerformed,String studyID,String receiptID) throws Exception {
		statement.executeUpdate("update `exam_entery` set `exam_performed` = '"
				+ examPerformed + "',`exam_operator`='" + examOperator + "' ,`xray_study_id`='" + studyID +"' where `receipt_id` ='"
				+ receiptID +"' and exam_room='10'");
	}
	public void updatelisCancel(String CancelLisCode, String WorkOrderID)
			throws Exception {
		statement
		.executeUpdate("update `exam_entery` set `lis_booking_status` = 'Cancelled' where `workorder_id` = '"+ WorkOrderID+"' and lis_code='"+CancelLisCode+"'");
	}
	public void updateLisExamData(String lis_code, String status,
			String examOperator, String examPerformed,String receiptNo,String lis_status) throws Exception {
		statement.executeUpdate("update `exam_entery` set `exam_performed` = '"
				+ examPerformed + "',`exam_reportno`='" + status
				+ "',`exam_operator`='" + examOperator + "',`lis_booking_status`='"+lis_status+"' where `lis_code` = '"
				+ lis_code +"' AND `receipt_id`='"+receiptNo+"' ");
	}
	public void updateExamData(String exam_id,String p_id,String date, String status,
			String examOperator, String examPerformed) throws Exception {
		System.out.println("update `exam_entery` set `exam_performed` = '"
				+ examPerformed + "',`exam_reportno`='" + status
				+ "',`exam_operator`='" + examOperator + "' where  `exam_nameid` = '"
				+ exam_id+"' AND `exam_pid`='"+p_id+"' AND `exam_date`='"+date+"'");
		statement.executeUpdate("update `exam_entery` set `exam_performed` = '"
				+ examPerformed + "',`exam_reportno`='" + status
				+ "',`exam_operator`='" + examOperator + "' where  `exam_nameid` = '"
				+ exam_id+"' AND `exam_pid`='"+p_id+"' AND `exam_date`='"+date+"' ORDER BY `exam_id` DESC LIMIT 1");
	}
	public void updateExamDataStatus(String exam_id, String reportName)
			throws Exception {
		statement
		.executeUpdate("update `exam_entery` set `exam_reportno`='2',`exam_reportname2` = '"
				+ reportName + "' where `exam_id` = " + exam_id);
	}

	public void updateExamReportType(String exam_id, String reportType)
			throws Exception {
		statement
		.executeUpdate("update `exam_entery` set `exam_reportname1` = '"
				+ reportType + "' where `exam_id` = " + exam_id);
	}
	public void updateExamComment(String exam_id,String comment)
			throws Exception {
		statement
		.executeUpdate("update `exam_entery` set `exam_comment` = '"
				+ comment + "' where `exam_id` = " + exam_id);
	}

	public void updateData(String[] data) throws Exception {
		String insertSQL = "";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 11; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public ResultSet retrieveExamComment(String exam_id) {
		String query="SELECT tc.comment from test_comments tc left join exam_entery ee on tc.insurance = ee.p_insurancetype and tc.exam_code = ee.exam_nameid WHERE ee.exam_id ='"+exam_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet lis_retrieveExamDataWithOPD(String id) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_id`,`exam_pid`,`lis_code`,`workorder_id`,exam_nameid FROM `exam_entery` WHERE receipt_id ='"+id+"' and exam_performed !='cancel' and (exam_result5 != 'IPD EXAM' or exam_result5 is NULL )";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public String retrieveOrderNo() {
		String query = "SELECT `receipt_id` FROM `exam_entery` WHERE receipt_id<>'' order by exam_id DESC limit 1";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String id = "";
		try {
			while (rs.next()) {
				id=rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}
	
	public String retrievePaymentAccessPass() {
		String query = "SELECT `value` FROM `karun_sparsh_param` WHERE dept = 'PAYMENT_TRACK'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
			while (rs.next()) {
				return rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
	public ResultSet retrievePID(String rec_id) {
		String query = "SELECT `exam_pid`,`exam_pname` FROM `exam_entery` WHERE receipt_id='"+rec_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public String retrieveWorkOrderID(String rec_id) {
		String query = "SELECT `workorder_id` FROM `exam_entery` WHERE receipt_id='"+rec_id+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
			while(rs.next()) {
				return rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	public int retrieveCounterData() {
		String query = "SELECT * FROM `exam_entery` WHERE 1";
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
		NumberOfRows++;
		return NumberOfRows;
	}

	public int retrieveCounterTodayToken() {
		new DateFormatChange();
		String query = "SELECT * FROM `exam_entery` WHERE `exam_date`= '"
				+ DateFormatChange.StringToMysqlDate(new Date()) + "'";
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
		NumberOfRows++;
		return NumberOfRows;
	}

	public ResultSet retrieveAllData() {
		String query = "SELECT  `exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_note1` FROM `exam_entery` WHERE 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public String retrieveviewreport(String workorder_id ,String test_id) {
		String url = null;
		String query = "SELECT  `lis_report_url` FROM `exam_entery` WHERE `workorder_id` = '"+ workorder_id+"' limit 1";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
			while(rs.next()) {
				url= rs.getObject(1).toString();
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return url;
	}
	public ResultSet retrieveAllData1(String dateFrom, String dateTo, String type) {
		String query = "SELECT receipt_id ,workorder_id,exam_pid,exam_pname,exam_result5,p_insurancetype ,exam_name ,exam_charges ,exam_date ,lis_booking_status,exam_performed,lis_test_id FROM `exam_entery` WHERE `exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND (exam_cat like '%"+type+"%') ORDER BY `exam_id` DESC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrievePerformedExamData(String dateFrom, String dateTo, String room) {
		String query = "select exam_id,exam_pid, exam_pname,exam_name, ee.exam_performed  from 	exam_entery ee WHERE\r\n"
				+ "	ee.`exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "	and ee.exam_performed <> 'Cancel'\r\n"
				+ "	and ee.exam_room = '"+room+"' order by ee.receipt_id";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllOpdExams(String dateFrom, String dateTo, int iscashless) {
		String query = "SELECT exam_id,receipt_id,exam_name,exam_pid,exam_pname,exam_doctorreff,exam_date,exam_charges,ee.p_insurancetype,payment_mode,is_cashless  from exam_entery ee ,patient_detail pd where ee.p_insurancetype<>'Unknown' and ee.exam_pid =pd.pid1 and exam_result5 is null  and ee.exam_performed <>'cancel' and ee.exam_date between '"+dateFrom+"' AND '"+dateTo+"' and pd.is_cashless = "+iscashless+" ORDER BY `exam_id` DESC";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExamsCat() {
		String query = "SELECT DISTINCT * from (SELECT exam_desc  FROM exam_master em \r\n"
				+ "union all\r\n"
				+ "SELECT exam_desc  FROM exam_master_8 em2) as e order by 1\r\n"
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
	public ResultSet retrieveAllData(String dateFrom, String dateTo) {
		String query = "SELECT  `exam_id`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_name`,`exam_charges`, `exam_nameid` FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataAmountReceipt(String dateFrom, String dateTo) {
		String query = "SELECT `exam_receiptid`,`exam_doctor_ref`, `exam_patientname`, `exam_amount`, `exam_date`  FROM `exam_amountreceipt` WHERE exam_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  ORDER BY `exam_receiptid` DESC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataNonPaid(String dateFrom, String dateTo) {
		String query = "SELECT  `exam_id`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_name`,`exam_charges`, `exam_nameid` FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND  `exam_chargespaid`='No' ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataExcel(String dateFrom, String dateTo) {
		String query = "SELECT `exam_id`, `exam_pid`, `exam_pname`, `exam_doctorreff`,`exam_name`,`exam_charges`,`exam_date`,`exam_note2` FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND `exam_note2` IS NOT NULL ";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataIDBetween(String amountReceiptID) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_date`,is_karuna,charges FROM `exam_entery` WHERE `receipt_id`="+amountReceiptID+"  AND `exam_note2` IS NOT NULL AND `exam_performed`!='Cancel'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataIDBetween1(String id1,String id2) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_date`,`is_karuna`,`charges` FROM `exam_entery` WHERE `exam_id` BETWEEN "
				+ id1 + " AND " + id2 + " AND `exam_note2` IS NOT NULL AND `exam_performed`!='Cancel'";
		System.out.println(query);
		//		String query = "SELECT `exam_name`,`exam_charges` FROM `exam_entery` WHERE `receipt_id`="+amountReceiptID+"  AND `exam_note2` IS NOT NULL AND `exam_performed`!='Cancel'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo,
			String doctorname) {
		String query = "SELECT  `exam_id`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_name`, `exam_nameid` FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND `exam_doctorreff`='"
				+ doctorname + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}



	public ResultSet retrieveAllDataCatGroup(String dateFrom, String dateTo,
			String insuranceType) {
		// String query =
		// "SELECT  `exam_cat`,count(`exam_id`),sum(`exam_charges`) FROM `exam_entery` WHERE exam_date BETWEEN '"
		// + dateFrom + "' AND '" + dateTo + "' GROUP BY `exam_cat`";
		//
		String query = "SELECT  E.`exam_cat`,count(E.`exam_id`),COALESCE(sum(E.`exam_charges`),0) FROM `exam_entery` E,`patient_detail` P WHERE P.pid1=E.`exam_pid` AND E.exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND P.`p_insurancetype`='"
				+ insuranceType + "' GROUP BY `exam_cat`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet InsuranceRateType(String ins) {
		String query = "SELECT ins_ratetype from insurance_detail where ins_name='"+ins+"'";
		try {

			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataCatWise(String dateFrom, String dateTo,String category) {
		String query = "SELECT  `exam_name`,COUNT(`exam_id`),SUM(`exam_charges`) FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "'AND `exam_cat`= '"+category+"' GROUP BY `exam_name`";
		try {

			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCatWise2(String dateFrom, String dateTo) {
		String query = "SELECT  `exam_cat` AS 'Exam Lab',`exam_name` AS 'Exam Name',COUNT(`exam_id`) AS 'Exams Count',SUM(`exam_charges`) AS 'Exams Sum',`exam_date` AS 'Exams Date' FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' GROUP BY `exam_name`, `exam_date` ORDER BY `exam_date` ASC,`exam_cat` ASC";
		try {

			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDoneitems(String dateFrom, String dateTo) {
		String query = "SELECT  `dept_name` AS 'Department',`doctor_name` AS 'EXAMS',COUNT(`entry_id`) AS 'Count',`date` AS 'Date' FROM `dept_pills_register` WHERE `date` BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND `dept_name` LIKE '%LAB%' GROUP BY `doctor_name`, `date` ORDER BY `date` ASC,`doctor_name` ASC";
		try {

			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCatWiseInsurance(String insuranceType,
			String dateFrom, String dateTo) {
		//		String query = "SELECT E.`exam_cat`,COUNT(E.`exam_id`), COALESCE(SUM(E.`exam_charges`),0) FROM `exam_entery` E,`patient_detail` P WHERE P.pid1=E.`exam_pid` AND E.exam_date BETWEEN '"
		//				+ dateFrom
		//				+ "' AND '"
		//				+ dateTo
		//				+ "' AND  P.`p_insurancetype`='" + insuranceType + "' AND `exam_chargespaid`='Yes' GROUP BY E.`exam_cat`";
		String query = "SELECT E.`exam_cat`,COUNT(E.`exam_id`), COALESCE(SUM(E.`exam_charges`),0) FROM `exam_entery` E  WHERE  E.exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND  E.`p_insurancetype`='" + insuranceType + "' AND `exam_chargespaid`='Yes' GROUP BY E.`exam_cat`";
		System.out.println(query+"");
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDetailCatWiseInsurance(String insuranceType,
			String dateFrom, String dateTo) {
		String query = "SELECT E.`exam_id` AS \"Exam ID\",P.pid1 AS \"Patient ID\",P.p_name AS \"Patient Name\",E.`exam_cat` AS \"Exam Type\", E.`exam_charges` AS \"Exam Charges\",E.exam_date AS \"Exam Date\" FROM `exam_entery` E,`patient_detail` P WHERE P.pid1=E.`exam_pid` AND E.exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND `exam_chargespaid`='Yes' AND  P.`p_insurancetype`='" + insuranceType + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String username) {
		String query = "SELECT  `exam_cat`,COUNT(`exam_id`), COALESCE(SUM(`exam_charges`),0)  FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND  `exam_note2`='"
				+username + "' AND `exam_chargespaid`='Yes' GROUP BY `exam_cat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String username,String insuranceType) {
		String query = "SELECT E.`exam_cat`,COUNT(E.`exam_id`), COALESCE(SUM(E.`exam_charges`),0) FROM `exam_entery` E WHERE  E.exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND  E.`exam_note2`='"
				+username + "' AND  `p_insurancetype`='" + insuranceType + "' AND `exam_chargespaid`='Yes'  GROUP BY E.`exam_cat`";
		//		String query = "SELECT E.`exam_cat`,COUNT(E.`exam_id`), COALESCE(SUM(E.`exam_charges`),0) FROM `exam_entery` E,`patient_detail` P WHERE P.pid1=E.`exam_pid` AND E.exam_date BETWEEN '"
		//				+ dateFrom
		//				+ "' AND '"
		//				+ dateTo
		//				+ "' AND  E.`exam_note2`='"
		//				+username + "' AND  P.`p_insurancetype`='" + insuranceType + "' AND `exam_chargespaid`='Yes'  GROUP BY E.`exam_cat`";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctor(String dateFrom, String dateTo,String doctorname,String insuranceType) {
		String query = "SELECT E.`exam_cat`,COUNT(E.`exam_id`), COALESCE(SUM(E.`exam_charges`),0) FROM `exam_entery` E,`patient_detail` P WHERE P.pid1=E.`exam_pid` AND E.exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND  E.`exam_doctorreff`='" + doctorname + "' AND  P.`p_insurancetype`='" + insuranceType + "'  GROUP BY E.`exam_cat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctor(String dateFrom, String dateTo, String doctorname) {
		String query = "SELECT `exam_cat`,COUNT(`exam_id`), COALESCE(SUM(`exam_charges`),0) FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "' AND `exam_doctorreff`='" + doctorname + "' GROUP BY `exam_cat`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCategory(String dateFrom, String dateTo) {
		String query = "SELECT DISTINCT `exam_cat` FROM `exam_entery` WHERE exam_date BETWEEN '"
				+ dateFrom
				+ "' AND '"
				+ dateTo
				+ "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveTestHistoryDataPatientID(String patientID) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name`,COALESCE(lis_code,''),COALESCE(workorder_id,'') FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveTestHistoryDataPatientIDRoom(String patientID,String room) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " AND `exam_room` = '"
				+ room
				+ "' ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveTestHistoryDataPatientIDDate(String patientID,String dateFrom,String dateTo) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " AND `exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'  ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveTestPatientExams(String dateFrom,String dateTo,String patientID) {
		String query = "SELECT `exam_id`, `exam_nameid`, `exam_name`, `exam_date`, `exam_charges`,COALESCE(`exam_sample5`, 0 ) FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " AND `exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveTestPatientExams2(String dateFrom,String dateTo,String patientID) {
		String query = "SELECT `exam_id`, `exam_nameid`, `exam_name`, `exam_date`, COALESCE((SELECT `exam_rate` FROM `` WHERE `exam_code`= `exam_nameid`),0),COALESCE(`exam_sample5`, 0 ) FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " AND `exam_chargespaid`='Yes' AND `exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' ";
		System.out.println(query);

		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveTestPatient(String dateFrom,String dateTo,String patientID) {
		String query = "SELECT `exam_id`, `exam_nameid`, `exam_name`, `exam_date`, `exam_charges` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID + " AND `exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveTestHistoryDataPatientID2(String patientID,
			String date) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name`, COALESCE(lis_code,''),COALESCE(workorder_id,'') FROM `exam_entery` WHERE `exam_date` >='"
				+ date
				+ "' and `exam_pid` = "
				+ patientID
				+ " ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveTestHistoryDataPatientID(String patientID,
			String searchSTR) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name` FROM `exam_entery` WHERE (exam_id LIKE '%"
				+ searchSTR
				+ "%' OR exam_name LIKE '%"
				+ searchSTR
				+ "%') AND `exam_pid` = "
				+ patientID
				+ " ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveTestHistoryDataPatientIDRoom(String patientID,
			String searchSTR,String room) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name` FROM `exam_entery` WHERE (exam_id LIKE '%"
				+ searchSTR
				+ "%' OR exam_name LIKE '%"
				+ searchSTR
				+ "%') AND `exam_pid` = "
				+ patientID
				+ " AND `exam_room` = '"
				+ room
				+ "' ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveTestHistoryDataPatientID(String patientID,
			String date, String searchSTR) {
		String query = "SELECT  `exam_id`,`exam_date`,`exam_name` FROM `exam_entery` WHERE (exam_id LIKE '%"
				+ searchSTR
				+ "%' OR exam_name LIKE '%"
				+ searchSTR
				+ "%') AND `exam_date` >='"
				+ date
				+ "' and ``exam_pid` = "
				+ patientID + " ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveExamDataPatientID(String patientID, String room) {
		String query = "SELECT  `exam_id`,`exam_name`,`exam_date`,`exam_charges`,`exam_nameid`,`exam_doctorreff` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID
				+ " And `exam_performed` = 'No' AND `exam_room` = '"
				+ room
				+ "'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamDataPatientID(String patientID, String room,String performed) {
		String query = "SELECT  `exam_id`,`exam_name`,`exam_date`,`exam_charges`,`exam_nameid`,`exam_doctorreff` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID
				+ " And `exam_performed` = '"+performed+"' AND `exam_room` = '"
				+ room
				+ "'";
		try {
			System.out.println(query + "  queryyyyy");
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveFreeTestDayLimit() {
		String query = "SELECT MAX(daily_limit) from free_test_camp_master ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamPrimaryDoc(String patientID, String room,String performed) {
		String query = "SELECT exam_doctor_ref,exam_receiptid from exam_amountreceipt ea where exam_receiptid=(SELECT receipt_id FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID
				+ " And `exam_performed` = '"+performed+"' AND `exam_room` = '"
				+ room
				+ "' order by 1 desc limit 1)";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamDataPatientID(String patientID) {
		String query = "SELECT  `exam_id`,`exam_name`,`exam_date`,`exam_charges` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID
				+ " ORDER BY `exam_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamDataWithBetweenId(String idFrom, String idTo) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_id`,`exam_pid` FROM `exam_entery` WHERE ( ( `exam_id` >= "
				+ idFrom + " ) AND ( `exam_id` <= " + idTo + " ) ) AND `exam_performed`='No' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet lis_retrieveExamDataWithBetweenId(String idFrom, String idTo) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_id`,`exam_pid`,`lis_code`,`workorder_id` FROM `exam_entery` WHERE ( ( `exam_id` >= "
				+ idFrom + " ) AND ( `exam_id` <= " + idTo + " ) ) AND `exam_performed`='No' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet lis_retrieveExamDataWithCancel(String id) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_id`,`exam_pid`,`lis_code`,`workorder_id`,exam_nameid,`exam_result5` FROM `exam_entery` WHERE receipt_id ='"+id+"' and exam_performed !='cancel' and exam_performed !='yes'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet lis_retrieveExamDataWithId(String id) {
		String query = "SELECT `exam_name`,`exam_charges`,`exam_id`,`exam_pid`,`lis_code`,`workorder_id` FROM `exam_entery` WHERE receipt_id ='"+id+"' AND `exam_performed`='No' AND `workorder_id`<>'' ";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamDataPatientIDAndCompletion(String patientID,
			String room) {
		String query = "SELECT  `exam_id`,`exam_name`,`exam_reportname1` FROM `exam_entery` WHERE `exam_pid` = "
				+ patientID
				+ " And `exam_reportno` = '1' AND `exam_room` = '"
				+ room + "'";
		System.out.print(query);
		// String
		// query="SELECT  `exam_id`,`exam_name`,`exam_reportname1` FROM `exam_entery` WHERE `exam_pid` = "+patientID+" And `exam_reportno` = '1' AND `exam_room` = '"+room+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {

		}
		return rs;
	}

	public String retrieveLastExamPatient(String index) {
		String date = "";
		String query = "SELECT  `exam_name`, `exam_date` FROM `exam_entery` WHERE `exam_pid` = "
				+ index +" ORDER BY `exam_date` DESC LIMIT 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				date = rs.getObject(2).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return date;
	}


	public String examCounter(String index,String date) {

		String counter="0";
		String query = "SELECT count(`exam_receiptid`) FROM `exam_amountreceipt` WHERE `exam_namecat`=(SELECT `exam_namecat` FROM `exam_amountreceipt` WHERE `exam_receiptid`="+index+") AND `exam_date`='"+date+"' AND `exam_receiptid`<="+ index;
		//		String query = "SELECT count(`exam_receiptid`) FROM `exam_amountreceipt` WHERE `exam_namecat`=(SELECT `exam_namecat` FROM `exam_amountreceipt` WHERE `exam_receiptid`="+index+") AND `exam_date`='"+date+"' AND `exam_receiptid`<="+ index;
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				counter = rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return counter;
	}

	public int examTotalLabWise(String labSTR,String date) {
		String counter="0";
		String query = "SELECT count(`exam_receiptid`) FROM `exam_amountreceipt` WHERE `exam_namecat`='"+labSTR+"' AND `exam_date`='"+date+"' AND `exam_doctor_ref` LIKE '%CIVIL%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				counter = rs.getObject(1).toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Integer.parseInt(counter);
	}


	public ResultSet retrieveAllDataExamID(String index) {
		String query = "SELECT `exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_performed` ,`exam_note1`,`exam_reportname2`,`exam_comment` ,receipt_id,`exam_note2` FROM exam_entery WHERE `exam_id` = "
				+ index+"";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveDoctor(String index) {
		String query = "select\r\n"
				+ "	ea.exam_doctor_ref,ee.exam_doctorreff \r\n"
				+ "from\r\n"
				+ "	exam_amountreceipt ea\r\n"
				+ "inner join exam_entery ee on\r\n"
				+ "	ea.exam_receiptid = ee.receipt_id\r\n"
				+ "where\r\n"
				+ "	ea.exam_receiptid = '"+index+"' limit 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllDataExamID(String index, String examRoom) {
		String query = "SELECT `exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_performed` ,`exam_note1`,`exam_reportname2`  FROM exam_entery WHERE `exam_id` = '"
				+ index + "' AND `exam_room` = '" + examRoom + "'";
		//		System.out.println(query);

		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveExamStatus(String index) {
		String query = "SELECT `exam_reportname1`  FROM exam_entery WHERE `exam_id` = "
				+ index;
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void deleteRowExams(String rowID,String ExamName) throws Exception
	{

		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `"+ExamName+"` WHERE `exam_code`='"+rowID+"'");
		//preparedStatement.setString(1, rowID);
		System.out.print(preparedStatement);
		preparedStatement.executeUpdate();

	}
	public ResultSet retrieveAllExams() {

		String query = "SELECT DISTINCT `exam_desc` FROM exam_master";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllExams(String tableName) {

		String query = "SELECT DISTINCT `exam_desc` FROM "+tableName;
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveExamCode(String tableName,boolean f) {
		String str=f?"":"not";
		String query = "select max(exam_code)+1 from "+tableName+" em where em.exam_code "+str+" between '99000' and '99999'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat(String examCat) {

		String query = "SELECT `exam_code`,`exam_subcat`, `exam_room` FROM `exam_master` WHERE `exam_desc` = '"
				+ examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat1(String examCat) {

		String query = "SELECT DISTINCT `exam_subcat` FROM `exam_master` WHERE `exam_desc` = '"
				+ examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllExams(int price_index) {

		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}

		String query = "SELECT DISTINCT `exam_desc` FROM " + table_name;
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet GetAllExams(int price_index,String Examcat,boolean AprovelAccess) {

		String table_name = "exam_master",str="";

		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
			if(!AprovelAccess)
				str="AND exam_rate<5000";
		}

		String query = "SELECT exam_code ,exam_desc ,exam_subcat ,exam_rate  from "+table_name+" where exam_desc like '%"+Examcat+"%' "+str+" AND `exam_text1`!='No' order by 2,3,1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat(int price_index, String examCat) {
		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}

		String query = "SELECT `exam_code`,`exam_subcat`, `exam_room`,`exam_rate`,`lis_mapping_code` FROM "
				+ table_name
				+ " WHERE `exam_desc` = '"
				+ examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat4(int price_index, String examCat) {
		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}

		String query = "SELECT `exam_code`,`exam_desc`,`exam_subcat`, `exam_room`,`exam_rate`,`lis_mapping_code` FROM "
				+ table_name
				+ " WHERE `exam_desc` = '"
				+ examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getExamSubCatRate(int price_index, String exam_code) {
		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}

		String query = "SELECT `exam_rate`,COALESCE(`exam_text2`, 0 ),exam_room ,COALESCE(`lis_mapping_code`, 0 ),`can_add_multiple`,`is_free_test` FROM " + table_name
				+ " WHERE `exam_code` = '" + exam_code + "'";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat1(int price_index, String examCat) {

		String table_name = "exam_master";
		if (price_index > 1) {
			table_name = table_name + "_" + price_index;
		}

		String query = "SELECT DISTINCT `exam_subcat` FROM " + table_name
				+ " WHERE `exam_desc` = '" + examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM exam_entery WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}

	// `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`,
	// `opd_type`
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `exam_entery`(`exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_note1`,`exam_room`,`exam_note2`,`exam_cat`,`exam_sample5`,`exam_chargespaid`,`receipt_id`,`p_insurancetype`,`payment_mode`,`lis_code`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public int inserDatakaruna(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `exam_entery`(`exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_note1`,`exam_room`,`exam_note2`,`exam_cat`,`exam_sample5`,`exam_chargespaid`,`receipt_id`,`p_insurancetype`,`payment_mode`,`lis_code`,`is_karuna`,`charges`,`free_usg`,`ipd_opd_no`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 22; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	public void UpdateLisData(String[] data,String index){
		String insertSQL = "UPDATE `exam_entery` set `workorder_id`='"+data[1]+"',`lis_report_url`='"+data[2]+"' where `receipt_id`='"+index+"' and `lis_code`>'0'";

		try {
			statement.executeUpdate(insertSQL);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public int inserDataIPDExam(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `exam_entery`(`exam_name`, `exam_nameid`, `exam_pid`, `exam_pname`, `exam_doctorreff`, `exam_date`, `exam_charges`, `exam_note1`,`exam_room`,`exam_note2`,`exam_cat`,`exam_sample5`,`exam_chargespaid`,`receipt_id`,`p_insurancetype`,`exam_result5`,`lis_code`,`ipd_opd_no`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 19; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
}
