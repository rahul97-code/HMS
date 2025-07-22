package hms1.ipd.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import hms.main.DBConnection;

public class CashConvertInsuranceDBConnection extends DBConnection {

	Connection connection = null;
	Statement statement = null;
	ResultSet rs = null;

	public CashConvertInsuranceDBConnection() { 

		super();
		connection = getConnection();
		statement = getStatement();
	}
	
	
	public ResultSet retreiveExamtable(String insurance) {
		String query="SELECT \r\n"
				+ "    CASE \r\n"
				+ "        WHEN ins_ratetype > 1 THEN CONCAT(\"exam_master_\", ins_ratetype)\r\n"
				+ "        ELSE \"exam_master\"\r\n"
				+ "    END AS result\r\n"
				+ "FROM insurance_detail id \r\n"
				+ "WHERE ins_name = '"+insurance+"'";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query); 
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retreiveInsurance() {
		String query="SELECT UPPER(ins_name) AS ins_name_uppercase FROM insurance_detail";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	
	public void ExamMapingCode(Map<String, List<String>> examCodeMap,String InsName) { 
	    for (Map.Entry<String, List<String>> entry : examCodeMap.entrySet()) {
	        String examCode = entry.getKey();
	        List<String> values = entry.getValue();
	        InsName=InsName.toLowerCase();
	        // Manually join the values (Java 7 compatible)
	        StringBuilder valueStringBuilder = new StringBuilder();
	        for (int i = 0; i < values.size(); i++) {
	            valueStringBuilder.append(values.get(i));
	            if (i < values.size() - 1) {
	                valueStringBuilder.append(",");  // Add comma between values
	            }
	        }
	        String valueString = valueStringBuilder.toString();  // Final joined string

	        // SQL query to update the table
	        String sql = "UPDATE exam_master SET " + InsName + "_code = ? WHERE exam_code = ?";
	        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
	            // Set the parameters for the query
	            stmt.setString(1, valueString);  // Set the values (1498, 1791) as a comma-separated string
	            stmt.setString(2, examCode);  // Set the exam code (5)

	            // Execute the update
	            int rowsUpdated = stmt.executeUpdate();
	            if (rowsUpdated > 0) {
	                System.out.println("Successfully updated exam code " + examCode);
	            } else {
	                System.out.println("No record found for exam code " + examCode);
	            }
	        }
	        catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	}

	public void saveData(String[] data) {
	    String query = "UPDATE Insurance_tracking_list SET item_id = ?, item_name = ?, item_desc = ?, per_item_price = ?, qty = ?, amount = ? WHERE ipd_id = ? AND type IN ('E', 'S')";
	    System.out.println(query);
	    
	    try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
	        // Assuming data array is properly ordered: item_id, item_name, item_desc, per_item_price, qty, amount, ipd_id
	        preparedStatement.setString(1, data[0]); // item_id
	        preparedStatement.setString(2, data[1]); // item_name
	        preparedStatement.setString(3, data[2]); // item_desc
	        preparedStatement.setDouble(4, Double.parseDouble(data[3])); // per_item_price
	        preparedStatement.setInt(5, Integer.parseInt(data[4])); // qty
	        preparedStatement.setDouble(6, Double.parseDouble(data[5])); // amount
	        preparedStatement.setString(7, data[6]); // ipd_id
	         preparedStatement.executeUpdate();
	    } catch (SQLException sqle) {
	        JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
	    } catch (NumberFormatException nfe) {
	        JOptionPane.showMessageDialog(null, "Invalid number format in input data.", "ERROR", JOptionPane.ERROR_MESSAGE);
	    }
	}

	
	public ResultSet retreiveInsuranceTableValue(String table) {
		String query="select exam_code, exam_subcat, exam_desc, exam_rate from "+table+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retreive_mapping_data(String table_name, String ins_type, String ipd_id) {
		String query="   \r\n"
				+ "SELECT if(em1.exam_lab like '%surgery%','S','E') as Type,em1.exam_code as item_id,em1.exam_subcat as name,em1.exam_subcat as 'desc','' as page_no,t.expense_date,'' as batch,'' as exp,em1.package_time_period as pck_days,'0' as mrp,em1.exam_rate as per_item_price,t.quantity as qty,(em1.exam_rate*t.quantity) as amt from "+table_name+" em1 , (SELECT\r\n"
				+ "	expense_id ,\r\n"
				+ "	charges_id,\r\n"
				+ "	expense_name,\r\n"
				+ "	expense_per_item ,\r\n"
				+ "	quantity,\r\n"
				+ "	expense_amount,\r\n"
				+ "	expense_date,\r\n"
				+ "	GROUP_CONCAT( em."+ins_type+"_code ) as fg\r\n"
				+ "from\r\n"
				+ "	ipd_expenses ie\r\n"
				+ "	left join exam_master em on em.exam_code =ie.charges_id \r\n"
				+ "where\r\n"
				+ "	ipd_id = "+ipd_id+"\r\n"
				+ "	and expense_type like '%exam%'\r\n"
				+ "	GROUP by expense_id)t\r\n"
				+ "	where FIND_IN_SET(exam_code,t.fg  ) > 0\r\n"
				+ "	GROUP by em1.exam_code,t.charges_id";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	public ResultSet retreivePatientdetail(String Ipd_Id) {
		String query="SELECT ie.p_name, ie.ipd_id, ie.p_id, ie.insurance_type , pd.insurance_category from ipd_entery ie left join patient_detail pd on ie.p_id =pd.pid1 where ie.ipd_id ="+Ipd_Id+"";
		System.out.println(query);
		try {
			rs = statement.executeQuery(query);
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
}
