package hms.payments.database;

import hms.main.DBConnection;
import hms.reception.gui.ReceptionMain;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

public class PaymentsDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public PaymentsDBConnection() {

		super();
		connection = getConnection();
		statement = getStatement();
	}
	public ResultSet getPayModes() {
		String query="SELECT `id`,`payment_mode`,`type`,`action_id`,`charges` FROM payment_machine_modes";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getPayModeCharge(String mode) {
		String query="SELECT `charges` FROM payment_machine_modes where `payment_mode`='"+mode+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet getPayMachines(String id) {
		String query="SELECT `id`, `machine_name`, `tid`, `busy` FROM payment_machine_master WHERE `pay_mode_id`='"+id+"'";
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
	public ResultSet getAllTransactions(String fromDate,String toDate) {
		String query="SELECT request_urn,receipt_no, additional_attribute1, additional_attribute2, additional_attribute3, additional_attribute4,(SELECT pmm.machine_name from payment_machine_master pmm where pmm.tid= A.tid) as machine_name, additional_attribute5,final_status,(SELECT pmm2.payment_mode  from payment_machine_modes pmm2 WHERE pmm2.`type`=A.`type`) as mode, urn, created_at, updated_at,amount,A.tid\n"
				+ "FROM payment_machine_tnx A where date(created_at) BETWEEN  '"+fromDate+"' AND '"+toDate+"' ORDER BY 1 DESC";
		try {
			System.out.println(query);
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public void UpdateResponseData(String[] data) throws Exception {
		String insertSQL = "UPDATE payment_machine_tnx SET response_code = ?, response_message = ?, urn = ?,invoiceNumber = ?, rrn = ?, cb_amt = ?, " +
				"app_code = ?, tokenisedValue = ? WHERE request_urn = ?";
		PreparedStatement preparedStatement = connection.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 10; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void updatePaymentSubmitStatus(int req_urn,String rrn) throws Exception
	{
		statement.executeUpdate("UPDATE `payment_machine_tnx` SET `rrn`='"+rrn+"' WHERE `request_urn`="+req_urn);

	}
	public void updatePaymentCancelStatus(int req_urn) throws Exception
	{
		statement.executeUpdate("UPDATE `payment_machine_tnx` SET `final_status`='TNX-CANCELLED' WHERE `urn`="+req_urn);

	}
	public void inserPaymentCancelData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO payment_machine_tnx_status\n"
				+ "(request_urn, response_code, response_message, urn, tid, amount, invoiceNumber, rrn, type, cb_amt, app_code, tokenised_value_of_card_number, actionId)\n"
				+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 14; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public void inserPaymentStatusData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO payment_machine_tnx_status\n"
				+ "(request_urn,response_code,card_holder_name,TP_TXN_ID,mid,tsi,type,upi_txn_id,tid,tokenised_value_of_card_number,txn_time,additional_attribute1,additional_attribute2,additional_attribute3,additional_attribute4,additional_attribute5,batchNumber,app_code,invoicenumber,capture_method,amount,chip,cb_amt,masked_card_number,rrn,tc,urn,card_scheme,txn_date,fetch_count,tvr,response_message,txn_type,billing_number,actionId,aid,status)\n"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i <=37; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
	}
	public int inserPaymentData(String[] data) throws Exception {
		String insertSQL = "INSERT INTO payment_machine_tnx( tid, amount, organization_code, additional_attribute1, additional_attribute2, additional_attribute3, additional_attribute4, additional_attribute5, `type`, actionId)\n"
				+ "VALUES(?,?,?,?,?,?,?,?,?,?)";
		PreparedStatement preparedStatement = connection.prepareStatement(
				insertSQL, Statement.RETURN_GENERATED_KEYS);
		for (int i = 1; i < 11; i++) {
			preparedStatement.setString(i, data[i - 1]);
		}
		preparedStatement.executeUpdate();
		ResultSet rs = preparedStatement.getGeneratedKeys();
		rs.next();
		return rs.getInt(1);
	}
	

}
