package hms.reception.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import hms.main.DBConnection;

public class financialCounclingDBConnection extends DBConnection {
	private Connection connection = null;
	private ResultSet rs = null;
	Statement statement = null;

	public financialCounclingDBConnection() {
		super();
		connection = getConnection();
		statement = getStatement();
	}

	public ResultSet retrievePatientData(String fromDate, String toDate) {
		String query = "SELECT \n"
				+ "    ipd_id, \n"
				+ "    p_id, \n"
				+ "    p_name, \n"
				+ "    ipd_entry_date, \n"
				+ "    CASE \n"
				+ "        WHEN ipd_discharge_date = '0000-00-00' THEN '' \n"
				+ "        ELSE ipd_discharge_date \n"
				+ "    END AS ipd_discharge_date,\n"
				+ "    ipd_discharged, \n"
				+ "    ipd_total_charges, \n"
				+ "    ipd_recieved_amount, \n"
				+ "    insurance_type, \n"
				+ "    doctor_name \n"
				+ "FROM ipd_entery \n"
				+ "WHERE ipd_entry_date BETWEEN ? AND ? \n"
				+ "";
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, fromDate);
			pst.setString(2, toDate);
			System.out.println(query);
			rs = pst.executeQuery();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet retrieveOPDPatientData(String fromDate, String toDate) {
		String query = "SELECT \n"
				+ "    ipd_id, \n"
				+ "    p_id, \n"
				+ "    p_name, \n"
				+ "    ipd_entry_date, \n"
				+ "    CASE \n"
				+ "        WHEN ipd_discharge_date = '0000-00-00' THEN '' \n"
				+ "        ELSE ipd_discharge_date \n"
				+ "    END AS ipd_discharge_date,\n"
				+ "    ipd_discharged, \n"
				+ "    ipd_total_charges, \n"
				+ "    ipd_recieved_amount, \n"
				+ "    insurance_type, \n"
				+ "    doctor_name \n"
				+ "FROM ipd_entery \n"
				+ "WHERE ipd_entry_date BETWEEN ? AND ? \n"
				+ "";
		try {
			PreparedStatement pst = connection.prepareStatement(query);
			pst.setString(1, fromDate);
			pst.setString(2, toDate);
			rs = pst.executeQuery();
		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}
	
	public ResultSet getExamsByPatient(String ipdId) throws SQLException {
	    String query = "SELECT expense_name,expense_desc, expense_per_item, quantity, expense_amount, expense_date, expense_type " +
	                   "FROM ipd_expenses " +
	                   "WHERE ipd_id = ? AND expense_type IN (?, ?, ?, ?, ?) ORDER BY CAST(expense_per_item AS DECIMAL) DESC";
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.setString(1, ipdId);
	    ps.setString(2, "EXAM");
	    ps.setString(3, "TEST");
	    ps.setString(4, "LIS EXAM");
	    ps.setString(5, "PROCEDURE");
	    ps.setString(6, "Procedures");
//	    ps.setString(6, "BLOOD LANCETS");
//	    ps.setString(8, "PEN DEVICES");
	    return ps.executeQuery();
	}
 
	
	  
	public ResultSet getMedicinesByPatient(String ipdId) throws SQLException {
	    String query = "SELECT expense_name,expense_desc, expense_per_item, quantity, expense_amount, expense_date, expense_type " +
	                   "FROM ipd_expenses " +
	                   "WHERE ipd_id = ? AND expense_type NOT IN (" +
	                   "'EXAM', 'TEST', 'LIS EXAM', 'PROCEDURE', 'Procedures'" +
	                   ") and quantity not like '-%' and expense_type not like '%charges%' ORDER BY CAST(expense_per_item AS DECIMAL) DESC";
	    PreparedStatement ps = connection.prepareStatement(query);
	    ps.setString(1, ipdId);
	    return ps.executeQuery();
	}
	

	public ResultSet getReturnsByPatient(String ipdId) throws SQLException{
		// TODO Auto-generated method stub
		String query = "SELECT expense_name, expense_desc, expense_per_item, quantity, expense_amount, expense_date, expense_type " +
                "FROM ipd_expenses " +
                "WHERE ipd_id = ? and quantity like '-%' ORDER BY CAST(expense_per_item AS DECIMAL) DESC";
 PreparedStatement ps = connection.prepareStatement(query);
 ps.setString(1, ipdId);
 return ps.executeQuery();
	}
	

	public ResultSet getBedsByPatient(String ipdId) throws SQLException {
		// TODO Auto-generated method stub
		String query = "SELECT ipd_building, ipd_ward, ipd_room, ipd_bed_no, start_date, start_time, " +
	               "CASE WHEN end_date = '0000-00-00' THEN '' ELSE end_date END AS end_date, " +
	               "CASE WHEN end_time = 'n/a' THEN '' ELSE end_time END AS end_time " +
	               "FROM ipd_bed_details " +
	               "WHERE ipd_id = ?";

		 PreparedStatement ps = connection.prepareStatement(query);
		 ps.setString(1, ipdId);
		 return ps.executeQuery();
	}
	
	public ResultSet retrievePatientBedDetailsNEW(String ipd_id) {
//		String query = "SELECT T5.ipd_id,T5.ward,T5.ward_cat,T5.bed_date,T5.minutes,T5.ward_charge,\r\n"
//				+ "CASE\r\n"
//				+ "	when IE.ipd_entry_date=CURRENT_DATE() and (T5.minutes+0)<720 then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	when IE.ipd_entry_date<>CURRENT_DATE() and T5.bed_date=IE.ipd_entry_date  and STR_TO_DATE(IE.ipd_enter_time,'%h:%i:%s %p') > '12:00:00' then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	when IE.ipd_entry_date<>CURRENT_DATE() and T5.bed_date=CURRENT_DATE() and CURRENT_TIME()  > '12:00:00' then (T5.ward_charge+0)-((20/100)*(T5.ward_charge+0))\r\n"
//				+ "	else T5.ward_charge\r\n"
//				+ "END as Actual_charge\r\n"
//				+ "FROM (SELECT * from (select * from (SELECT \r\n"
//				+ "CASE when T2.bed_date =(SELECT ie.ipd_entry_date from ipd_entery ie where ie.ipd_id2='"+ipd_id+"')  or \r\n"
//				+ "	T2.bed_date =CURRENT_DATE()  then T2.sr else 0 end as grp,T2.* \r\n"
//				+ "FROM (\r\n"
//				+ "/** $$ Query start **/ \r\n"
//				+ "SELECT \r\n"
//				+ "    (@row_number := @row_number + 1)\r\n"
//				+ "    AS sr ,T1.*\r\n"
//				+ "FROM (\r\n"
//				+ "/** !! Query to get all beds with thier minutes and charges**/\r\n"
//				+ "SELECT T.ipd_id,T.ward,T.ward_cat,T.bed_date,sum(T.minutes) as minutes,wc.charges as ward_charge\r\n"
//				+ "	FROM (\r\n"
//				+ "	/**** ## Query to calculate minutes from start_date time to the end of the day *****/  \r\n"
//				+ "	SELECT\r\n"
//				+ "	    A.ipd_id as ipd_id,\r\n"
//				+ "		A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat,\r\n"
//				+ "		A.start_date AS bed_date,\r\n"
//				+ "	    CASE \r\n"
//				+ "	    	when A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN   TIMESTAMPDIFF(MINUTE, \r\n"
//				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) , ' ',(case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p')) ELSE   TIMESTAMPDIFF(MINUTE, \r\n"
//				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(A.start_date, ' ','11:59:59 pm'),\r\n"
//				+ "	        '%Y-%m-%d %h:%i:%s %p'))\r\n"
//				+ "	    END	AS minutes\r\n"
//				+ "	FROM\r\n"
//				+ "			ipd_bed_details A\r\n"
//				+ "	WHERE\r\n"
//				+ "		ipd_id = '"+ipd_id+"'		\r\n"
//				+ "		/**** ## Query end *****/  \r\n"
//				+ "UNION ALL	\r\n"
//				+ "/**** && Query to calculate minutes from day start to the end_date time *****/  \r\n"
//				+ "		SELECT\r\n"
//				+ "		A.ipd_id,\r\n"
//				+ "		A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat,\r\n"
//				+ "	    (case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) AS bed_date,\r\n"
//				+ "		CASE \r\n"
//				+ "			WHEN A.start_date=(case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end) THEN null \r\n"
//				+ "                               else TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
//				+ "                               ,' ','12:00:00 am'),'%Y-%m-%d %h:%i:%s %p'),\r\n"
//				+ "	             STR_TO_DATE(CONCAT((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end),' ',\r\n"
//				+ "	                           (case when A.end_time='n/a' then TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\") else A.end_time end)),\r\n"
//				+ "	    	                   '%Y-%m-%d %h:%i:%s %p'))\r\n"
//				+ "		END	AS minutes\r\n"
//				+ "	FROM\r\n"
//				+ "		ipd_bed_details A\r\n"
//				+ "	WHERE\r\n"
//				+ "		ipd_id = '"+ipd_id+"'	\r\n"
//				+ "		/**** && Query end*****/  \r\n"
//				+ "UNION ALL \r\n"
//				+ "/**** @@ This query get all In Between dates of start_date to end_date with 1440 minutes *****/  \r\n"
//				+ "SELECT  A.ipd_id,\r\n"
//				+ "        A.ipd_ward AS ward,\r\n"
//				+ "		A.ipd_ward_category AS ward_cat\r\n"
//				+ "		,ADDDATE(A.start_date,n) as bed_date, -- here we add n in the start date\r\n"
//				+ "		24*60 as minutes\r\n"
//				+ "		FROM ( \r\n"
//				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
//				+ "SELECT \r\n"
//				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
//				+ "         FROM (\r\n"
//				+ "                /**** $$ This query get ***/\r\n"
//				+ "                  SELECT 1 as c, ((((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
//				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
//				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
//				+ "(SELECT @row_number := 0) AS x\r\n"
//				+ "ORDER BY n asc \r\n"
//				+ "/**** %% Query end *****/  \r\n"
//				+ ")T,ipd_bed_details A where n>0 and n<DATEDIFF((case when A.end_date='0000-00-00' then CURRENT_DATE() else A.end_date end)\r\n"
//				+ " ,A.start_date) and A.ipd_id='"+ipd_id+"'  \r\n"
//				+ "/**** @@ Query end *****/\r\n"
//				+ "	) T \r\n"
//				+ "	left join ward_cahrges wc on\r\n"
//				+ "	wc.ward_type =T.ward_cat\r\n"
//				+ "	GROUP by T.ward_cat,T.bed_date,T.ward\r\n"
//				+ "	order by bed_date,max(minutes) desc	\r\n"
//				+ "	/** !! Query End**/\r\n"
//				+ "	)T1,\r\n"
//				+ "(SELECT @row_number := 0) AS x\r\n"
//				+ "/*** $$ Query End  **/)T2)T3\r\n"
//				+ "group by T3.bed_date,T3.grp\r\n"
//				+ "order by T3.bed_date,max(T3.ward_charge+0) desc\r\n"
//				+ ")T4\r\n"
//				+ "GROUP by T4.bed_date \r\n"
//				+ "order by T4.bed_date )T5\r\n"
//				+ "left join ipd_entery IE on\r\n"
//				+ "IE.ipd_id2 ='"+ipd_id+"'\r\n"
//				+ "\r\n"
//				+ "";
		String query_new="SELECT T3.ipd_id,T3.ward,T3.ward_cat,T3.bed_date,(T3.bed_charge/T3.charges) as percentage,T3.charges,T3.bed_charge from(SELECT T2.*,\r\n"
				+ "CASE \r\n"
				+ "	when 0<=(T2.bed_hrs+0) and (T2.bed_hrs+0)<=3 THEN (20/100)*(T2.charges+0)\r\n"
				+ "	when 3<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=7 THEN (40/100)*(T2.charges+0)\r\n"
				+ "	when 7<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=12 THEN (60/100)*(T2.charges+0)\r\n"
				+ "	when 12<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=18 THEN (80/100)*(T2.charges+0)\r\n"
				+ "    when 18<(T2.bed_hrs+0) and (T2.bed_hrs+0)<=24 THEN (T2.charges+0)\r\n"
				+ "END as bed_charge\r\n"
				+ "from (   SELECT T1.*,wc.charges,ROUND(sum(T1.minutes)/60) as bed_hrs  from (SELECT\r\n"
				+ "	    A.ipd_id as ipd_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "		A.start_date AS bed_date,\r\n"
				+ "	    if(A.start_date=(if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)),TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)) , ' ',(if(A.end_time='n/a',TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\"),A.end_time))),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p')),  TIMESTAMPDIFF(MINUTE, \r\n"
				+ "	        STR_TO_DATE(CONCAT(A.start_date, ' ', A.start_time),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'),STR_TO_DATE(CONCAT(A.start_date, ' ','11:59:59 pm'),\r\n"
				+ "	        '%Y-%m-%d %h:%i:%s %p'))) AS minutes\r\n"
				+ "	FROM\r\n"
				+ "			ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'		\r\n"
				+ "		/**** ## Query end *****/  \r\n"
				+ "UNION ALL	\r\n"
				+ "/**** && Query to calculate minutes from day start to the end_date time *****/  \r\n"
				+ "		SELECT\r\n"
				+ "		A.ipd_id,\r\n"
				+ "		A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat,\r\n"
				+ "	    (if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)) AS bed_date,\r\n"
				+ "	     if(A.start_date=(if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)), null \r\n"
				+ "                               ,TIMESTAMPDIFF(MINUTE,STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date))\r\n"
				+ "                               ,' ','12:00:00 am'),'%Y-%m-%d %h:%i:%s %p'),\r\n"
				+ "	             STR_TO_DATE(CONCAT((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date)),' ',\r\n"
				+ "	                           (if(A.end_time='n/a',TIME_FORMAT(CURRENT_TIME() , \"%h:%i:%s %p\"),A.end_time))),\r\n"
				+ "	    	                   '%Y-%m-%d %h:%i:%s %p')))AS minutes\r\n"
				+ "	FROM\r\n"
				+ "		ipd_bed_details A\r\n"
				+ "	WHERE\r\n"
				+ "		ipd_id = '"+ipd_id+"'	\r\n"
				+ "	HAVING minutes>0	\r\n"
				+ "		/**** && Query end*****/  	\r\n"
				+ "UNION ALL \r\n"
				+ "/**** @@ This query get all In Between dates of start_date to end_date with 1440 minutes *****/ \r\n"
				+ "SELECT  A.ipd_id,	\r\n"
				+ "        A.ipd_ward AS ward,\r\n"
				+ "		A.ipd_ward_category AS ward_cat\r\n"
				+ "		,ADDDATE(A.start_date,n) as bed_date, -- here we add n in the start date\r\n"
				+ "		24*60 as minutes\r\n"
				+ "		FROM ( \r\n"
				+ "/**** %% Query to get series from 0 to 1024 numbers *****/  \r\n"
				+ "SELECT \r\n"
				+ "    (@row_number := @row_number + 1)-1 AS n\r\n"
				+ "         FROM (\r\n"
				+ "                /**** $$ This query get ***/\r\n"
				+ "                  SELECT 1 as c, ((\r\n"
				+ "                  ((((((b9.0) << 1 | b8.0) << 1 | b7.0) << 1 | b6.0) << 1 | b5.0) << 1 | b4.0 << 1 | b3.0) << 1 | b2.0) << 1 | b1.0) << 1 | b0.0 AS n\r\n"
				+ "                  FROM  (SELECT 0 union all SELECT 1) AS b0,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b1,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b2,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b3,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b4,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b5,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b6,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b7,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b8,\r\n"
				+ "                      (SELECT 0 union all SELECT 1) AS b9)y,\r\n"
				+ "(SELECT @row_number := 0) AS x\r\n"
				+ "ORDER BY n asc \r\n"
				+ "/**** %% Query end *****/  \r\n"
				+ ")T,ipd_bed_details A where n>0 and n<DATEDIFF((if(A.end_date='0000-00-00',CURRENT_DATE(),A.end_date))\r\n"
				+ " ,A.start_date) and A.ipd_id='"+ipd_id+"'\r\n"
				+ " )T1 left join ward_cahrges wc on\r\n"
				+ " wc.ward_type =T1.ward_cat\r\n"
				+ " GROUP BY ward_cat,bed_date\r\n"
				+ " order by bed_date,max(wc.charges))T2)T3";
		System.out.print(query_new);
		try {
			rs = statement.executeQuery(query_new);

		} catch (SQLException sqle) {
			JOptionPane.showMessageDialog(null, sqle.getMessage(), "ERROR",
					javax.swing.JOptionPane.ERROR_MESSAGE);
		}
		return rs;
	}

	public ResultSet getExtrasByPatient(String ipdId) throws SQLException {
		// TODO Auto-generated method stub
		String query = "SELECT expense_name, expense_desc, expense_per_item, quantity, expense_amount, expense_date, expense_type " +
	               "FROM ipd_expenses " +
	               "WHERE ipd_id = ? AND expense_type LIKE '%charges%' " +
	               "ORDER BY CAST(expense_per_item AS DECIMAL) DESC";

 PreparedStatement ps = connection.prepareStatement(query);
 ps.setString(1, ipdId);
 return ps.executeQuery();
	}



}
