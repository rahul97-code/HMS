package hms.opd.database;

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

public class OPDDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public OPDDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public ResultSet retrievepID(String opdID)
	{
		String query="SELECT p_id from opd_entery oe where opd_id ='"+opdID+"' and opd_date =CURRENT_DATE()";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet checkIfDublicateOpd(String pid,String disease,String docID)
	{
		String query="SELECT * from opd_entery oe where opd_date =CURRENT_DATE() and opd_doctorid='"+docID+"' and opd_diseasestype='"+disease+"' and p_id='"+pid+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrievePatientData(String opdID)
	{
		String query="SELECT p_id,p_name from opd_entery oe where opd_id ='"+opdID+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet remaningFreeUSG(String doc_name) { 
		String query = "select (((TIMESTAMPDIFF(MONTH , '2024-07-01', CURDATE())+1)* 25)-COUNT(*)) from opd_entery oe left join exam_entery ee  on oe.p_id =ee.exam_pid  and oe.opd_date =ee.exam_date \r\n"
				+ "where ee.free_usg ='1' and oe.opd_doctor ='"+doc_name+"' and ee.exam_performed !='Cancel' and oe.is_free_usg ='1'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet DocAccess(String doc_name) { 
		String query = "select free_usg_access from doctor_detail where doc_name='"+doc_name+"' ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getAllInsuranceOpdData(String fromDate,String toDate, int iscashless) { 
		String query = "SELECT\r\n"
				+ "	opd_id,p_id ,oe.p_name ,p_insurance_type ,opd_doctor,opd_date, opd_entry_time,opd_entry_user,payment_mode,is_cashless\r\n"
				+ "from\r\n"
				+ "	opd_entery oe , patient_detail pd where oe.p_id =pd.pid1 and\r\n"
				+ "	p_insurance_type <> 'Unknown'\r\n"
				+ "	and opd_date BETWEEN '"+fromDate+"' and '"+toDate+"'\r\n"
				+ "group by p_id having is_cashless ="+iscashless+"	\r\n"
				+ "order by opd_id	\r\n";

		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void UpdateKarunaReceiptID(String id, String receiptID) throws Exception
	{

		statement.executeUpdate("UPDATE `karuna_med_discount` SET `receipt_id`='"+receiptID+"' WHERE `id`= "+id);
	}
	
	
	
	
	
	
		public ResultSet retrieveAllKarunaMSDS(String dateFrom, String dateTo) { 
		String query = "SELECT `id`,`opd_id`, `p_name`, `p_id`, `receipt_id`,`consultant`,`bill_amount`,`bill_discount_amount`,`p_age`,`p_sex`,`p_mobile` FROM `karuna_med_discount` WHERE cast(date_time as date)  BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `date_time` DESC ";
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
	public ResultSet retrieveOPDDATA(String opd_id)
	{
		String query="SELECT opd_id,opd_doctor,opd_doctorid ,opd_date,opd_entry_time from opd_entery oe where opd_id ='"+opd_id+"' and opd_date =CURRENT_DATE() ";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveOPDMedDiscount(String opdID) {
		String query="SELECT id,discount,discount_isconsumed FROM karuna_med_discount where opd_id='"+opdID+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void updateCancellation(String opdID) throws Exception
	{

		statement.executeUpdate("UPDATE `opd_entery` SET `opd_text3`='CANCEL' WHERE `opd_id`= "+opdID);
	}
	public void updatePrescriptionData(String[] data) throws Exception {
		String insertSQL = "UPDATE `opd_entery` SET `opd_symptom`=?,`opd_prescription`=? WHERE `opd_id`="+data[2];

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 3; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updatePrescriptionData1(String[] data) throws Exception {
		String insertSQL = "UPDATE `opd_entery` SET `opd_symptom`=?,`opd_prescription`=?, `not_affordable`=?,`is_free_usg`=? WHERE `opd_id`="+data[4];

		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 5; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void UpdateOPDMedDiscount(String[] data) throws Exception
	{

		statement.executeUpdate("UPDATE `karuna_med_discount` SET `discount`='"+data[3]+"' WHERE `id`= "+data[0]);
	}
	public void InsertOPDMedDiscount(String[] data) throws Exception
	{
		String insertSQL = "INSERT INTO karuna_med_discount(opd_id, p_id,consultant,discount,discount_approved_by, discount_approved_by_id,p_name,p_sex,p_age,p_mobile)VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection
				.prepareStatement(insertSQL);
		for (int i = 1; i < 11; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public int retrieveCounterData() {
		String query = "SELECT COUNT(*) FROM `opd_entery` WHERE 1";
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
	public ResultSet retrieveAllReceptionName()
	{
		String query="SELECT DISTINCT `opd_entry_user` FROM `opd_entery`";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public int retrieveCounterTodayToken() {
		new DateFormatChange();
		String query = "SELECT * FROM `opd_entery` WHERE `opd_date`= '"
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

	public int retrieveCounterTodayTokenDoctorWise(String doctorName,int opd_id) {
		new DateFormatChange();
		String query = "select COUNT(*)  from opd_entery oe where opd_date =CURRENT_DATE() and opd_doctor ='"+doctorName+"' and opd_id <='"+opd_id+"'\r\n"
				+ "";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int c = 0;
		try {
			while (rs.next()) {
				c=rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return c;
	}
	public ResultSet retrieveAllData() {
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_type` FROM `opd_entery` WHERE 1";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllData1(String dateFrom, String dateTo,String index) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`,`opd_charge`, `opd_status` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND p_id LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllReportData(String dateFrom, String dateTo) { 
		String query ="SELECT\r\n"
				+ "	'OPD SERVICES' AS 'TABLENAME',\r\n"
				+ "	p_insurance_type ,\r\n"
				+ "	OPD.`opd_diseasestype` as 'DESCRIPTION',\r\n"
				+ "	COUNT(\"1\") as 'COUNT',\r\n"
				+ "	COALESCE(SUM(OPD.`opd_charge`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`opd_entery` OPD\r\n"
				+ "WHERE\r\n"
				+ "	OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "	AND OPD.`opd_text3` IS NULL\r\n"
				+ "GROUP BY\r\n"
				+ "	   3\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'CARD CHARGES' AS 'TABLENAME',\r\n"
				+ "	`receipt_text1` ,\r\n"
				+ "	'CARD CHARGES' AS 'DESCRIPTION',\r\n"
				+ "	count('1'),\r\n"
				+ "	COALESCE(sum(`receipt_amount`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`amountreceipt`\r\n"
				+ "WHERE\r\n"
				+ "	`receipt__date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "	AND `receipt_type` = 'CARD'\r\n"
				+ "	AND `receipt_text2` = 'N/A'	\r\n"
				+ "	GROUP by 2\r\n"
				+ "	UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'EXAM SERVICES' AS 'TABLENAME',\r\n"
				+ "	E.`p_insurancetype`,\r\n"
				+ "	`exam_cat` AS 'DESCRIPTION',\r\n"
				+ "	COUNT('1'),\r\n"
				+ "	COALESCE(SUM(E.`exam_charges`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`exam_entery` E\r\n"
				+ "WHERE\r\n"
				+ "	E.exam_date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "	AND `exam_chargespaid` = 'Yes'\r\n"
				+ "GROUP BY\r\n"
				+ "	2,3\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'MISC SERVICES' AS 'TABLENAME',\r\n"
				+ "	M.`p_insurancetype`,\r\n"
				+ "	M.`misc_namecat`,\r\n"
				+ "	COUNT('1'),\r\n"
				+ "	COALESCE(SUM(M.`misc_amount`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`misc_entry` M\r\n"
				+ "WHERE\r\n"
				+ "	M.misc_date BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "GROUP BY\r\n"
				+ "	2,3\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'Indoor Services' ,\r\n"
				+ "	I2.insurance_type,\r\n"
				+ "	CASE\r\n"
				+ "		WHEN I.payment_type = 'P' THEN 'Advance Amount'\r\n"
				+ "		WHEN I.payment_type = 'R' THEN 'Return Amount'\r\n"
				+ "	END,\r\n"
				+ "	COUNT('1'),\r\n"
				+ "	COALESCE(SUM(I.`payment_amount`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`ipd_payments` I\r\n"
				+ "left join\r\n"
				+ "    ipd_entery I2 on\r\n"
				+ "	I.ipd_id = I2.ipd_id2\r\n"
				+ "WHERE \r\n"
				+ "	I.`payment_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'\r\n"
				+ "	AND I.`payment_text5` = 'N/A'\r\n"
				+ "GROUP by\r\n"
				+ "	3,\r\n"
				+ "	2\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'Indoor Services' ,\r\n"
				+ "	I.insurance_type ,\r\n"
				+ "	'Indoor Discharge' ,\r\n"
				+ "	COUNT(I.`ipd_recieved_amount`),\r\n"
				+ "	COALESCE(SUM(I.`ipd_recieved_amount`), 0)\r\n"
				+ "FROM\r\n"
				+ "	`ipd_entery` I\r\n"
				+ "where\r\n"
				+ "	 I.`ipd_discharge_date` BETWEEN '"+dateFrom+"'\r\n"
				+ "				 AND '"+dateTo+"'\r\n"
				+ "	AND I.`ipd_discharged` = 'Yes'\r\n"
				+ "GROUP by\r\n"
				+ "	2\r\n"
				+ "UNION all\r\n"
				+ "SELECT\r\n"
				+ "	'Cancelled Amount',\r\n"
				+ "	'Unknown',\r\n"
				+ "	can_cat,\r\n"
				+ "	COUNT('1'),\r\n"
				+ "	COALESCE(SUM(can_amount), 0)\r\n"
				+ "FROM\r\n"
				+ "	`cancel_detail`\r\n"
				+ "WHERE\r\n"
				+ "	date BETWEEN '"+dateFrom+"'\r\n"
				+ "				 AND '"+dateTo+"'\r\n"
				+ "GROUP by\r\n"
				+ "	3";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllOnlineData(String dateFrom, String dateTo,String index) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND p_id LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' AND `opd_entry_user`='ONLINE' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllOnlineData(String dateFrom, String dateTo) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_entry_user`='ONLINE' AND `opd_status`='ACTIVE' ORDER BY `opd_id` DESC ";
		System.out.println(query+"");
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientOpd(String dateFrom, String dateTo,String index) { 
		String query = "SELECT  `opd_id`, `opd_doctor`,  `opd_diseasestype`,`opd_date`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND p_id LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}


	public ResultSet retrieveAllDataPatient(String dateFrom, String dateTo,String index) { 
		String query = "SELECT  `opd_id`, `opd_doctor`,  `opd_diseasestype`,`opd_date`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND p_id LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`,`opd_charge`, `opd_status` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ORDER BY `opd_id` DESC ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataExcel(String dateFrom, String dateTo) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_diseasestype`, `opd_charge`,`opd_date`, `opd_entry_time`,`opd_entry_user` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' ";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllData(String dateFrom, String dateTo, String services) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_diseasestype`='"+services+"' AND `opd_text3` IS NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}



	public ResultSet retrieveAllDataCategory(String dateFrom, String dateTo, String insurance) { 
		String query = "SELECT  COUNT(OPD.`opd_id`), OPD.`opd_diseasestype`, COALESCE(SUM(OPD.`opd_charge`),0) FROM `opd_entery` OPD  WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND OPD.`p_insurance_type`='"+insurance+"' AND  OPD.`opd_text3` IS NULL GROUP BY OPD.`opd_diseasestype`";
		//		String query = "SELECT  COUNT(OPD.`opd_id`), OPD.`opd_diseasestype`, COALESCE(SUM(OPD.`opd_charge`),0) FROM `opd_entery` OPD LEFT JOIN `patient_detail` PT ON OPD.`p_id`=PT.`pid2` WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND PT.`p_insurancetype`='"+insurance+"' AND  OPD.`opd_text3` IS NULL GROUP BY OPD.`opd_diseasestype`";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDetailCategory(String dateFrom, String dateTo, String insurance) { 
		String query = "SELECT OPD.`opd_id` AS \"OPD ID\" , OPD.`p_id` AS \"Patient ID\" , PT.`p_name` AS \"Patient Name\" , OPD.`opd_diseasestype` AS \"OPD Type\", OPD.`opd_charge` AS \"OPD Charges\", OPD.`opd_date` AS \"OPD Date\", OPD.`opd_entry_time` AS \"OPD Time\" FROM `opd_entery` OPD LEFT JOIN `patient_detail` PT ON OPD.`p_id`=PT.`pid2` WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND PT.`p_insurancetype`='"+insurance+"' AND  OPD.`opd_text3` IS NULL";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllPatients(String dateFrom, String dateTo, String insurance) { 
		String query = "SELECT  OPD.`p_id`, OPD.`p_name`,OPD.`opd_date`,  PT.`p_insurancetype` FROM `opd_entery` OPD LEFT JOIN `patient_detail` PT ON OPD.`p_id`=PT.`pid2` WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND PT.`p_insurancetype`='"+insurance+"'";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataInsurance1(String insuranceType,String dateFrom, String dateTo, String services) { 
		String query = "SELECT  O.`opd_id`, O.`p_id`, O.`p_name`, O.`opd_doctor`, O.`opd_date`, O.`opd_token`, O.`opd_diseasestype`, O.`opd_charge` FROM `opd_entery` O,`patient_detail` P WHERE O.p_id=P.`pid1` AND O.opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND O.`opd_diseasestype`='"+services+"' AND O.`opd_text3` IS NULL AND P.`p_insurancetype`='"+insuranceType+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String user,String insurance) { 
		String query = "SELECT  COUNT(OPD.`opd_id`), OPD.`opd_diseasestype`, COALESCE(SUM(OPD.`opd_charge`),0) FROM `opd_entery` OPD  WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND OPD.`opd_entry_user`='"+user+"' AND `p_insurance_type`='"+insurance+"' AND  OPD.`opd_text3` IS NULL GROUP BY OPD.`opd_diseasestype`";
		//		String query = "SELECT  COUNT(OPD.`opd_id`), OPD.`opd_diseasestype`, COALESCE(SUM(OPD.`opd_charge`),0) FROM `opd_entery` OPD LEFT JOIN `patient_detail` PT ON OPD.`p_id`=PT.`pid2` WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND OPD.`opd_entry_user`='"+user+"' AND PT.`p_insurancetype`='"+insurance+"' AND  OPD.`opd_text3` IS NULL GROUP BY OPD.`opd_diseasestype`";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataUserWise(String dateFrom, String dateTo,String user) { 
		String query = "SELECT  COUNT(`opd_id`), `opd_diseasestype`, COALESCE(SUM(`opd_charge`),0) FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_entry_user`='"+user+"' AND `opd_text3` IS NULL GROUP BY `opd_diseasestype`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}



	public ResultSet retrieveAllDataDoctor(String dateFrom, String dateTo,String doctor) { 
		String query = "SELECT  COUNT(`opd_id`), `opd_diseasestype`, COALESCE(SUM(`opd_charge`),0) FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "'  AND `opd_doctor`='"+doctor+"' AND `opd_text3` IS NULL GROUP BY `opd_diseasestype`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctor(String dateFrom, String dateTo, String insurance,String doctor) { 
		String query = "SELECT  COUNT(OPD.`opd_id`), OPD.`opd_diseasestype`, COALESCE(SUM(OPD.`opd_charge`),0) FROM `opd_entery` OPD LEFT JOIN `patient_detail` PT ON OPD.`p_id`=PT.`pid2` WHERE OPD.`opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND PT.`p_insurancetype`='"+insurance+"' AND OPD.`opd_doctor`='"+doctor+"' AND  OPD.`opd_text3` IS NULL GROUP BY OPD.`opd_diseasestype`";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllCancelledData(String dateFrom, String dateTo, String services) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_diseasestype`='"+services+"' AND `opd_text3` IS NOT NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorWise(String dateFrom, String dateTo, String doctorname) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_token`, `opd_diseasestype`, `opd_charge` FROM `opd_entery` WHERE opd_date BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `opd_doctor`='"+doctorname+"' AND `opd_text3` IS NULL";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithOpdId(String opd_id) { 
		String query = "SELECT  `opd_id`, `p_id`, `p_name`, `opd_doctor`, `opd_date`, `opd_symptom`, `opd_prescription`, `opd_token`, `opd_type`, `opd_charge`,`opd_entry_time`,`opd_diseasestype`,`p_type`,opd_entry_user,not_affordable,is_free_usg FROM `opd_entery` WHERE opd_id ='"
				+ opd_id +"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveOpdSpecialties() { 
		String query = "select opd_type,name from opd_specialties_master osm where is_active =1 order by id";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataWithOpdId2(String opd_id) { 
		String query = "SELECT  `p_id`, `p_name`, `opd_doctor`,`opd_date`, `opd_charge`,`opd_id` FROM `opd_entery` WHERE opd_id ='"
				+ opd_id +"' and `opd_status`!='CANCEL'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet retrieveAllDataWithOpdId3(String opd_id) { 
		String query = "SELECT  `p_id`, `p_name`, `opd_doctor`,`opd_date`, `opd_charge`,`opd_id`, if(opd_date=CURRENT_DATE() and not_affordable!=0,'1','0')t,if(opd_date=CURRENT_DATE() and is_free_usg!=0,'1','0')d FROM `opd_entery` WHERE opd_id ='"
				+ opd_id +"' and `opd_status`!='CANCEL'"; 
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID(String patientID) {
		String query = "SELECT  `opd_id`,`opd_date` FROM `opd_entery` WHERE `p_id` = "
				+ patientID+" ORDER BY `opd_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID1(String patientID) {
		String query = "SELECT  `opd_id`,`opd_doctor`,`opd_date`, `opd_charge` FROM `opd_entery` WHERE `p_id` = "
				+ patientID+" ORDER BY `opd_id` DESC";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataPatientID(String patientID,String dateFrom,String dateTo) {
		String query = "SELECT  `opd_id`,`opd_date` FROM `opd_entery` WHERE `p_id` = "
				+ patientID+" AND `opd_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public String retrieveLastOpdPatient(String index) {
		String date = "";
		String query = "SELECT  `opd_doctor`, `opd_date` FROM `opd_entery` WHERE `p_id` = "
				+ index+" ORDER BY `opd_id` DESC LIMIT 1";
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
	public boolean opdPatientAllowed(int token,String index) {
		int tokenNo =0;

		String query = "SELECT `opd_token` FROM `opd_entery` WHERE `p_id` = "
				+ index+" and opd_date='"
				+ DateFormatChange.StringToMysqlDate(new Date()) + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		try {
			while (rs.next()) {
				tokenNo =Integer.parseInt(rs.getObject(1).toString());
				System.out.println(DateFormatChange.StringToMysqlDate(new Date())+"    "+tokenNo);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(tokenNo<=token)
		{
			return false;
		}
		return true;
	}
	public ResultSet searchPatientWithIdOrNmae(String index) {
		String query = "SELECT * FROM `patient_detail` where pid1 LIKE '%"
				+ index + "%' OR p_name LIKE '%" + index + "%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getNextOPD(String doctorname, String date, String index) {
		String query = "SELECT `opd_id`,`p_id`,`opd_date`,`opd_token` FROM `opd_entery` a where "
				+ index
				+ "= (select count(distinct(opd_id)) from `opd_entery` where opd_id<=a.opd_id and opd_date='"
				+ date
				+ "' and opd_doctor='"
				+ doctorname
				+ "' ) and opd_doctor='"
				+ doctorname
				+ "' and opd_date='"
				+ date + "'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int getDoctorTodayOPD(String doctorname, String date) {
		int counter=0;
		String query = "select count(distinct(opd_id)) from `opd_entery` where opd_id<=opd_id and opd_date='"
				+ date + "' and opd_doctor='" + doctorname + "'";
		try {
			rs = statement.executeQuery(query);
			while (rs.next()) {
				counter=Integer.parseInt( rs.getString(1));
			}
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return counter;
	}

	public void deleteRow(String rowID) throws Exception {
		PreparedStatement preparedStatement = connection
				.prepareStatement("DELETE FROM inventory WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public void UpdateOPDToken(String count,int opd_id) throws Exception
	{
		statement.executeUpdate("UPDATE `opd_entery` SET `opd_token`='"+count+"' WHERE `opd_id`= "+opd_id);
	}
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `opd_entery`(`p_id`, `p_name`, `opd_doctor`, `opd_doctorid`, `opd_date`, `opd_symptom`, `opd_token`, `opd_diseasestype`, `opd_type`, `opd_refferal`, `opd_charge` ,`opd_entry_user`, `opd_entry_time`,`p_type`,`p_insurance_type`,`payment_mode`,`request_urn`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 18; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return  rs.getInt(1);
	}
}
