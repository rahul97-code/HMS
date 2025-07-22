package hms.store.database;

import hms.main.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

public class VendorQuotationDBConnection extends DBConnection {
	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public VendorQuotationDBConnection() {

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
	
		public ResultSet retrieveItems(String supplier_id) {
			String query = "SELECT `id`, `item_id`, `item_name`, `item_desc`, `unit_price`, `date` FROM `vendor_quotation` WHERE `supplier_id`='"+supplier_id+"' ORDER BY `id` DESC";
			
			//System.out.println(query);
			try {
				rs = statement.executeQuery(query);
			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			return rs;
		}
		public String retrieveItems(String supplier_id,String item_id,String item_price) {
			String query = "SELECT `unit_price` FROM `vendor_quotation` WHERE `supplier_id`='"+supplier_id+"' AND `item_id`='"+item_id+"' ORDER BY `id` DESC LIMIT 1";
			
			String price=item_price;
			System.out.println(query);
			try {
				rs = statement.executeQuery(query);

			//	System.out.println(query);
			} catch (SQLException sqle) {
				JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
						javax.swing.JOptionPane.ERROR_MESSAGE);
			}
			try {
				while (rs.next()) {
					price = rs.getObject(1).toString();	
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return price;
		}
		public void deleteRow(String rowID) throws Exception
		{
			PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM `vendor_quotation` WHERE `id`=?");
			preparedStatement.setString(1, rowID);
			preparedStatement.executeUpdate();
		}
	public void inserquotationItem(String[] data) throws Exception
	  {
		  String insertSQL = "INSERT INTO `vendor_quotation`(`supplier_id`, `supplier_name`, `item_id`, `item_name`, `item_desc`, `unit_price`, `entry_by`, `date`, `time`) VALUES  (?,?,?,?,?,?,?,?,?)";
		  PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
		  for (int i = 1; i <10; i++) {
			
				  preparedStatement.setString(i, data[i-1]);
			}
		  preparedStatement.executeUpdate();
	  }
	
}
