package hms.amountreceipt.database;

import hms.main.DBConnection;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class AmountReceiptDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public AmountReceiptDBConnection() {

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
	public void updateDataCacellation(String repID) throws Exception
	  {
		
		
		statement.executeUpdate("UPDATE `amountreceipt` SET `receipt_text2`='CANCEL' WHERE `receipt_id`="+repID);
	  }
	public void UpdateDataExam(String exam_text1,int exam_text2,int index) throws Exception
	  {
		
		String query="UPDATE `exam_amountreceipt` SET `exam_text1`="+exam_text1+",`exam_text2`="+exam_text2+" WHERE `exam_receiptid`="+index;
		System.out.println(query);
		statement.executeUpdate(query);
	  }
	public void updateDataCacellationExam(String repID) throws Exception
	  {
		
		
		statement.executeUpdate("UPDATE `exam_amountreceipt` SET `exam_text4`='CANCEL' WHERE `exam_receiptid`="+repID);
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

	public int retrieveCounterData() {
		String query = "SELECT * FROM `amountreceipt` WHERE 1";
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

	public boolean isFreeTestReceipt(String recID) {
		String query = "select * from  exam_entery ee  join free_test_camp_master ftcm on ftcm.insurance= ee.p_insurancetype and ee.exam_nameid =ftcm.exam_code  where ee.receipt_id ='"+recID+"'";
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
		return NumberOfRows>0;
	}
	public ResultSet retrieveAllData(String dateFrom,String dateTo) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='MISC' AND `receipt_text2`='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCancelledData(String dateFrom,String dateTo) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='MISC' AND `receipt_text2`!='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCard(String dateFrom,String dateTo) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_text2`='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllDataCard(String dateFrom,String dateTo,String insurance) {
		String query = "SELECT count(`receipt_id`),COALESCE(sum(`receipt_amount`),0) FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_text1`='" + insurance + "'  AND `receipt_text2`='N/A'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataByCategory(String dateFrom,String dateTo,String insurance) {
		String query = "SELECT `receipt_id`,`receipt_amount`,`receipt__date` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_text1`='" + insurance + "'  AND `receipt_text2`='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCardExcel(String dateFrom,String dateTo) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`, `receipt_patientname`,`receipt_amount`, `receipt_username`,  `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_text2`='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCardUserWise(String dateFrom,String dateTo,String username) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_username`='" + username + "' AND`receipt_text2`='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataCardUserWise(String dateFrom,String dateTo,String username,String insurance) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_username`='" + username + "' AND `receipt_text1`='" + insurance + "' AND`receipt_text2`='N/A'";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCancelledDataCard(String dateFrom,String dateTo) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_type`='CARD' AND `receipt_text2`!='N/A'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllDataDoctorWise(String dateFrom,String dateTo,String doctor) {
		String query = "SELECT `receipt_id`, `receipt_ref_doctor`,`receipt_amount`, `receipt__date`, `receipt_time` FROM `amountreceipt` WHERE `receipt__date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `receipt_ref_doctor`='"+doctor+"'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExams(String dateFrom,String dateTo,String type) {
		String query = "SELECT `exam_amount` FROM `exam_amountreceipt` WHERE `exam_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `exam_namecat`='"+type+"' AND `exam_text4`='n/a'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExamsExcel(String dateFrom,String dateTo) {
		String query = "SELECT `exam_receiptid`, `exam_namecat`, `exam_doctor_ref`, `exam_patientname`, `exam_amount`, `exam_username`, `exam_date`, `exam_time`,`exam_text1`, `exam_text2` FROM `exam_amountreceipt` WHERE `exam_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `exam_text4`='n/a'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCancelledExams(String dateFrom,String dateTo,String type) {
		String query = "SELECT `exam_amount` FROM `exam_amountreceipt` WHERE `exam_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `exam_namecat`='"+type+"' AND `exam_text4`!='n/a'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllExamsToken(String dateFrom,String dateTo,String type) {
		String query = "SELECT `exam_receiptid`,`exam_patientname`,`exam_doctor_ref`,`exam_text1` FROM `exam_amountreceipt` WHERE `exam_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND `exam_namecat`='"+type+"' AND `exam_text4`='n/a'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExamsToken(String dateFrom,String dateTo) {
		String query = "SELECT `exam_receiptid`,`exam_patientname`,`exam_doctor_ref`,`exam_text1` FROM `exam_amountreceipt` WHERE `exam_date` BETWEEN '"
				+ dateFrom + "' AND '" + dateTo + "' AND  `exam_text4`='n/a'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExamsToken1(String dateFrom,String dateTo,String room) {
		String query = "SELECT  ee.exam_pid , ea.`exam_patientname`, ea.`exam_doctor_ref`, ee.exam_performed "
				+ "FROM exam_entery ee left join `exam_amountreceipt` ea on ea.exam_receiptid=ee.receipt_id"
				+ " WHERE ea.`exam_date` BETWEEN '"+dateFrom+"' AND '"+dateTo+"' AND ea.`exam_text4` = 'n/a' and ee.exam_performed<>'Cancel' and ee.exam_room ='"+room+"' GROUP by ea.exam_receiptid order by ea.exam_receiptid";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllCardsReciept(String recieptID) {
		String query = "SELECT `receipt_id`, `receipt__date`,`receipt_ref_doctor`, `receipt_patientname`,`receipt_amount` FROM `amountreceipt` WHERE `receipt_ref_doctor` LIKE  '%"+recieptID+"%' AND `receipt_type` ='CARD'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllMISCReciept(String recieptID) {
		String query = "SELECT `receipt_amount`, `receipt_ref_doctor`, `receipt_patientname`, `receipt__date`,`receipt_id` FROM `amountreceipt` WHERE `receipt_id`="+recieptID+" AND `receipt_type` ='MISC'";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllExamsReciept(String recieptID) {
		String query = "SELECT `exam_namecat`, `exam_patientname`, `exam_amount`, `exam_date`, `exam_text1`, `exam_text2`, `exan_text3`,`exam_receiptid`  FROM `exam_amountreceipt` WHERE `exam_receiptid`="+recieptID+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveAllRecieptNo(String examNo) {
		String query = "SELECT `exam_text1`,`exam_text2`,`exam_receiptid`,`exam_amount`,`exam_time`  FROM `exam_amountreceipt` WHERE `exam_receiptid`="+ examNo;
		System.out.println(query);
//		String query = "SELECT `exam_text1`,`exam_text2`,`exam_receiptid`,`exam_amount`,`exam_time`  FROM `exam_amountreceipt` WHERE `exam_receiptid`= "+receiptid;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retrieveAllRecieptNoNew(String receiptid) {
		String query = "SELECT `exam_text1`,`exam_text2`,`exam_receiptid`,`exam_amount`,`exam_time`  FROM `exam_amountreceipt` WHERE `exam_receiptid`= "+receiptid;
		System.out.println(query);
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
				.prepareStatement("DELETE FROM `amountreceipt` WHERE id=?");
		preparedStatement.setString(1, rowID);
		preparedStatement.executeUpdate();
	}
	public int inserDataMisc(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `misc_amount_reciept`(`misc_desc`, `misc_p_id`, `misc_date`,`request_urn`) VALUES (?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 5; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
	public int inserDataExam(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `exam_amountreceipt`(`exam_namecat`, `exam_doctor_ref`, `exam_patientname`, `exam_amount`, `exam_username`, `exam_date`, `exam_time`,`exam_text1`, `exam_text2`,`payment_mode`,`request_urn`) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 12; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `amountreceipt`(`receipt_type`, `receipt_amount`, `receipt_username`, `receipt_ref_doctor`, `receipt_patientname`, `receipt__date`, `receipt_time`,`receipt_text1`,`is_cashless`) VALUES (?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 10; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}
	
}
