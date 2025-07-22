package hms.patient.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class PatientDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PatientDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public ResultSet retrieveDataWithIndextracking(String index)
	{
	  String query="SELECT  `p_name`, IFNULL( CONCAT( DATE_FORMAT( `p_age` , '%y' ) + DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( (SELECT `receipt__date`FROM `amountreceipt`WHERE `receipt_ref_doctor` = '"+index+"' LIMIT 1) ) ) , '%Y' ) , '-', DATE_FORMAT( `p_age` , '%m' ) , '-', DATE_FORMAT( `p_age` , '%d' ) ) , `p_age` ) AS age, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_insurancetype`,`p_insurance_no`,`p_scheme`,`insurance_category`,`is_cashless` FROM `patient_detail` WHERE `pid1` = "+index;
	System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmaeNewtracking(String index)
	{
	  String query="SELECT p_id,p_name FROM `ipd_entery` where ipd_id2 ='"+index+"'";
	  System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet searchkarunsparsh(String index)
	{
	  String query="SELECT\r\n"
	  		+ "	sum(if(opd_date=CURRENT_DATE() and not_affordable!=0,'1','0'))t,\r\n"
	  		+ "	sum(if(opd_date=CURRENT_DATE() and is_free_usg!=0,'1','0'))d\r\n"
	  		+ "FROM\r\n"
	  		+ "	opd_entery\r\n"
	  		+ "WHERE\r\n"
	  		+ "	p_id  LIKE '%"+index+"%'\r\n"
	  		+ "	and opd_status != 'CANCEL'\r\n"
	  		+ "	order by opd_date desc limit 1";
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
	public void updateData(String[] data) throws Exception
	  {
		String insertSQL ="UPDATE `patient_detail` SET `p_name`= ?, `p_agetype`= ?, `p_age`= ?, `p_birthdate`= ?, `p_sex`= ?, `p_addresss`= ?, `p_city`= ?, `p_telephone`= ?, `p_bloodtype`= ?, `p_guardiantype`= ?, `p_father_husband`= ?, `p_insurancetype`= ?,`p_text1`= ?,`p_insurance_no`= ?,`p_text2`= ?,`insurance_category`=?,`is_cashless`=?,`encrypted_aadhaar_no`=?  WHERE `pid1` = '"+data[18]+"'";
		
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <19; i++) {
				  preparedStatement.setString(i, data[i-1]);
			}
		  System.out.println("--"+data[16]);
		  preparedStatement.executeUpdate();
	  }

	public void updateDataEmail(String p_id,String email) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `patient_detail` SET `p_text6`='"+email+"' where `pid1` = '"+p_id+"'");
	  }
	public void updateInsuranceDischargetype(String ipd_id,String cat) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `ipd_entery` SET `discharge_type`='"+cat+"' where `ipd_id2` = '"+ipd_id+"'");
	  }
	public void updateInsuranceCategory(String p_id,String cat) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `patient_detail` SET `insurance_category`='"+cat+"' where `pid1` = '"+p_id+"'");
	  }
	public void updateDataPatientID(String autoINC,String p_code) throws Exception
	  {
		
		statement.executeUpdate("UPDATE `patient_detail` SET `pid1`='"+p_code+"',`pid2`='"+p_code+"' where `id` = "+autoINC);
	  }
	public int retrieveCounterData()
	{
	  String query="SELECT COUNT(*) FROM `patient_detail` WHERE 1";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		int NumberOfRows = 0;
        try {
			while(rs.next()){
				NumberOfRows= rs.getInt(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return NumberOfRows;	
	}
	public ResultSet retrieveDataWithIndex(String index)
	{
	  String query="SELECT  `p_name`, if(p_agetype='birth',  CONCAT(\r\n"
	  		+ "    TIMESTAMPDIFF(YEAR, p_birthdate, CURDATE()), '-',TIMESTAMPDIFF(MONTH, p_birthdate, CURDATE()) % 12, '-', DATEDIFF(\r\n"
	  		+ "      CURDATE(),DATE_ADD(DATE_ADD(p_birthdate,INTERVAL TIMESTAMPDIFF(YEAR, p_birthdate, CURDATE()) YEAR\r\n"
	  		+ "        ),INTERVAL TIMESTAMPDIFF(MONTH, p_birthdate, CURDATE()) % 12 MONTH))),IFNULL( CONCAT( DATE_FORMAT( `p_age` , '%y' ) + DATE_FORMAT( FROM_DAYS( TO_DAYS( NOW( ) ) - TO_DAYS( (SELECT `receipt__date`FROM `amountreceipt`WHERE `receipt_ref_doctor` = '"+index+"' LIMIT 1) ) ) , '%Y' ) , '-', DATE_FORMAT( `p_age` , '%m' ) , '-', DATE_FORMAT( `p_age` , '%d' ) ) , `p_age` )) AS age, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_insurancetype`,`p_insurance_no`,`p_scheme`,`insurance_category`,`id`,`is_cashless`,`encrypted_aadhaar_no` FROM `patient_detail` WHERE `pid1` = "+index;
	System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveEmailWithIndex(String index)
	{
	  String query="SELECT  `p_text6` FROM `patient_detail` WHERE `pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex1(String index)
	{
	  String query="SELECT `pid1`, `p_name`, `p_guardiantype`, `p_father_husband`, `p_age`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_insurancetype` FROM `patient_detail` WHERE`pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveDataWithIndex2(String index)
	{
	  String query="SELECT `p_name`, `p_agetype`, `p_age`, `p_birthdate`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_bloodtype`, `p_guardiantype`, `p_father_husband`, `p_insurancetype`, `p_insurance_no`, `p_text1`,IFNULL(`p_text2`,\"Active\"),`insurance_category`,`is_cashless`,`encrypted_aadhaar_no`  FROM `patient_detail` WHERE `pid1` = "+index;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	
	public ResultSet retrieveAadhaarData(String index)
	{
	  String query="SELECT pid1 ,p_name FROM patient_detail pd WHERE encrypted_aadhaar_no ='"+index+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData(String datefrom,String dateto)
	{
	  String query="SELECT `id`, `pid1`, `p_name`, `p_father_husband`, `p_insurancetype` ,`p_age`,`p_sex` FROM `patient_detail` WHERE CAST(patient_entry_datetime AS DATE) BETWEEN '"+datefrom+"' and '"+dateto+"'";
		System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllData(String index)
	{
	  String query="SELECT `id`, `pid1`, `p_name`, `p_father_husband`, `p_insurancetype` ,`p_age`,`p_sex` FROM `patient_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' OR p_telephone LIKE '%"+index+"%'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmae1(String index)
	{
	  String query="SELECT * FROM `patient_detail` where pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' OR `p_telephone` LIKE '%"+index+"%' OR `p_insurance_no` LIKE '%"+index+"%' ";
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
	  String query="SELECT * FROM `patient_detail` where `is_patient_blacklisted`='0' and pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%'  LIMIT 10 ";
//		System.out.println(query);
//	  String query="SELECT pid1,p_name FROM `patient_detail` where `is_patient_blacklisted`='0' and pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' OR p_telephone LIKE '%"+index+"%'  LIMIT 10 ";
	  System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPatientWithIdOrNmaeNew(String index)
	{

	  String query="SELECT pid1,p_name FROM `patient_detail` where `is_patient_blacklisted`='0' and pid1 LIKE '%"+index+"%' OR p_name LIKE '%"+index+"%' OR p_telephone LIKE '%"+index+"%' order by patient_entry_datetime  LIMIT 10 ";
	  System.out.println(query);
	  try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet searchPhoneno(String index)
	{
	  String query="SELECT * FROM `patient_detail` where `p_telephone`='"+index+"' and `is_patient_blacklisted`='1'";
		System.out.println(query);
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
		PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `patient_detail` WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserData(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `patient_detail`(`pid1`, `pid2`, `p_name`, `p_agetype`, `p_age`, `p_birthdate`, `p_sex`, `p_addresss`, `p_city`, `p_telephone`, `p_bloodtype`, `p_guardiantype`, `p_father_husband`, `p_insurancetype`,`p_text1`,`p_insurance_no`,`p_scheme`,`patient_entry_datetime`,`p_text3`,`mobile_verified`,`insurance_category`,`is_cashless`,`encrypted_aadhaar_no`) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		  for (int i = 1; i <24; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
		  
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 
		 return  rs.getInt(1);
	  }
}
