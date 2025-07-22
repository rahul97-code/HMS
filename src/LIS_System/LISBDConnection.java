package LIS_System;

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

public class LISBDConnection extends DBConnection{
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;


	public LISBDConnection() {
		// TODO Auto-generated constructor stub
		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	public void updateData(String workorderNo, String[][] data,int i)
	{
		String query="update `exam_entery` set `lis_booking_status`='"+data[i][12]+"',`lis_sample_status` = '"
				+ data[i][10] + "',`lis_logistic_status`='" + data[i][13]
				+ "',`lis_docuploded`='" + data[i][4] + "',`lis_document_status`='"+data[i][11]+"',`lis_test_id`='"+data[i][8]+"' where  `workorder_id` = '"
				+ workorderNo+"' AND `lis_code`='"+data[i][1]+"'";
		System.out.println(query);
		try {
			statement.executeUpdate(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public String retrieveLisCode(String exam_id) {
		String query = "SELECT `lis_code` FROM `exam_master_4` WHERE `item_id`="+exam_id;
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		String item_category="";
		try {
			while (rs.next()) {
				item_category=rs.getString(1);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item_category;
	}

}
