package hms1.ipd.database;

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

public class PackageDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PackageDBConnection() {

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
	public ResultSet retrieveAllData()
	{
	  String query="SELECT `package_id`, `package_name`, `package_amount`, `package_expiry_date`, `package_active`, `package_text` FROM `indoor_package` WHERE package_active=1 AND `package_expiry_date` >= NOW()";
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
			javax.swing.JOptionPane.ERROR_MESSAGE);
		} 
		return rs;
	}
	public ResultSet retrieveAllExams(String packageID) {

		String table_name = "package_exam_master";
		
		String query = "SELECT DISTINCT `exam_desc` FROM " + table_name+"  WHERE `package_id`='"+packageID+"'";
		try {
			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExamSubCat(String packageID,String examCat) {
		String table_name = "package_exam_master";
		
		String query = "SELECT `exam_code`,`exam_subcat`, `exam_room`,`exam_rate` FROM "
				+ table_name
				+ " WHERE `package_id`='"+packageID+"' AND `exam_desc` = '"
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

	
	public ResultSet getExamSubCat1(String packageID, String examCat) {

		String table_name = "package_exam_master";
		String query = "SELECT DISTINCT `exam_subcat` FROM " + table_name
				+ " WHERE `package_id`='"+packageID+"' AND `exam_desc` = '" + examCat
				+ "' AND `exam_text1`!='No' ORDER BY `exam_subcat` ASC ";
		try {

			rs = statement.executeQuery(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public int inserData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO `indoor_package`(`package_name`, `package_amount`, `package_expiry_date`) VALUES (?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 4; i++) {

			preparedStatement.setString(i, data[i - 1]);
		}

		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();

		return rs.getInt(1);
	}

	public void insertPackageExams(String packageID, String examID) {
		String query = "INSERT INTO `package_exam_master` ( `package_id`, `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_room`, `exam_operator`, `exam_rate`, `exam_text1`, `exam_text2`) SELECT  '"
				+ packageID
				+ "', `exam_code`, `exam_desc`, `exam_subcat`, `exam_lab`, `exam_room`, `exam_operator`, `exam_rate`, `exam_text1`, `exam_text2` FROM  `exam_master` WHERE  `exam_code` = "
				+ examID;
		try {
			statement.executeUpdate(query);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
	}

}
