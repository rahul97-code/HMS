package hms.JDialogs.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import hms.main.DBConnection;

public class JDialogsDBConnection extends DBConnection{
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public JDialogsDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}

	public ResultSet GetUserCashTotalAmount(String userName,String FromDate,String ToDate) {
		String query="SELECT sum(t.amt) from (SELECT\n"
				+ "	COALESCE(SUM(OPD.`opd_charge`), 0) as amt,\n"
				+ "	p_insurance_type\n"
				+ "FROM\n"
				+ "	`opd_entery` OPD\n"
				+ "WHERE\n"
				+ "	OPD.`opd_date` BETWEEN '"+FromDate+"' AND '"+ToDate+"'\n"
				+ "	AND OPD.`opd_entry_user` = '"+userName+"'\n"
				+ "	AND OPD.`opd_text3` IS NULL\n"
				+ "GROUP by 2	\n"
				+ "	UNION all\n"
				+ "SELECT\n"
				+ "		COALESCE(SUM(`receipt_amount`),0),\n"
				+ "	receipt_text1\n"
				+ "FROM\n"
				+ "	`amountreceipt`\n"
				+ "WHERE\n"
				+ "	`receipt__date` BETWEEN '"+FromDate+"' AND '"+ToDate+"'\n"
				+ "	AND `receipt_type` = 'CARD'\n"
				+ "	AND `receipt_username` = '"+userName+"'\n"
				+ "	AND `receipt_text2` = 'N/A'\n"
				+ "group by 2\n"
				+ "UNION all\n"
				+ "SELECT\n"
				+ "	COALESCE(SUM(E.`exam_charges`), 0),\n"
				+ "	E.p_insurancetype\n"
				+ "FROM\n"
				+ "	`exam_entery` E\n"
				+ "WHERE\n"
				+ "	E.exam_date BETWEEN '"+FromDate+"' AND '"+ToDate+"'\n"
				+ "	AND E.`exam_note2` = '"+userName+"'\n"
				+ "	AND `exam_chargespaid` = 'Yes'\n"
				+ "GROUP by 2	\n"
				+ "UNION all\n"
				+ "SELECT\n"
				+ "	COALESCE(SUM(M.`misc_amount`), 0),\n"
				+ "	'' as ins\n"
				+ "FROM\n"
				+ "	`misc_entry` M\n"
				+ "WHERE\n"
				+ "	M.misc_date BETWEEN '"+FromDate+"' AND '"+ToDate+"'\n"
				+ "	AND M.`misc_username` = '"+userName+"'\n"
				+ "UNION all\n"
				+ "SELECT\n"
				+ "	COALESCE(SUM(I.`payment_amount`), 0),\n"
				+ "	P.`p_insurancetype`\n"
				+ "FROM\n"
				+ "	`ipd_payments` I,\n"
				+ "	`patient_detail` P\n"
				+ "WHERE\n"
				+ "	P.pid1 = I.p_id\n"
				+ "	AND I.`payment_type` = 'P'\n"
				+ "	AND I.`payment_date` BETWEEN '"+FromDate+"' AND '"+ToDate+"'\n"
				+ "	AND I.`payment_entry_user` = '"+userName+"'\n"
				+ "	AND I.`payment_text5` = 'N/A'\n"
				+ ")t";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet GetTodayReportsCount(String userName) {
		String query="SELECT count(*) from cash_reports_details crd where cast(`date` as date) =CURRENT_DATE() and user_name='"+userName+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public int InsertData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO cash_reports_details(user_id,user_name,entered_cash_amount,bill_cash_amount,department,reason,report_status)VALUES(?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 8; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		 preparedStatement.executeUpdate();
		 ResultSet rs = preparedStatement.getGeneratedKeys();
		  rs.next();
		 return  rs.getInt(1);
	}
}
