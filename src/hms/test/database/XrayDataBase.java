package hms.test.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import hms.main.XrayDBConnection;

public class XrayDataBase extends XrayDBConnection {
	Connection connect=null;
	Statement stmt = null;
	ResultSet rs = null;
	public XrayDataBase() {
		// TODO Auto-generated constructor stub
		super();
		stmt=getStatement();
		connect=getConnection();
	}

	public ResultSet GetXrayData(String p_id,String p_name,String sex) {
		String query="SELECT PB.[PName],PB.[StudyID],PB.[ImageName] AS date FROM PatientInfo PIN\n"
				+ "LEFT JOIN\n"
				+ "PatientBook PB ON PIN.[StudyID]=PB.[StudyID] AND ImageAccept='YES'\n"
				+ "WHERE Val(PatientID) ='"+p_id+"' AND LCASE([PatientName]) ='"+p_name.toLowerCase()+"' AND [Sex] ='"+sex+"' AND FORMAT(PB.`StudyDateTime`,'yyyy-mm-dd')=FORMAT(Date(),'yyyy-dd-mm') ";
		try {
			System.out.println(query);
			rs=stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	public ResultSet GetXrayData2(String p_id,String p_name,String sex) {
		String query="SELECT PB.[PName],PB.[StudyID],PB.[ImageName] AS date FROM PatientInfo PIN\n"
				+ "LEFT JOIN\n"
				+ "PatientBook PB ON PIN.[StudyID]=PB.[StudyID] AND ImageAccept='YES'\n"
				+ "WHERE Val(PatientID) ='"+p_id+"' AND LCASE([PatientName]) ='"+p_name.toLowerCase()+"' AND [Sex] ='"+sex+"' AND FORMAT(PB.`StudyDateTime`,'yyyy-mm-dd')=FORMAT(Date(),'yyyy-mm-dd') ";
		try {
			System.out.println(query);
			rs=stmt.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}
