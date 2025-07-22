package hms.main;

import java.sql.ResultSet;
import java.sql.SQLException;

import jcifs.smb.NtlmPasswordAuthentication;

public class ServerAuthentication {

	public static NtlmPasswordAuthentication  FileServerAuthentication()
	{
		String[] str=serverAuth();
		 NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, str[0],str[1]);
		 
		 return auth;
	}
	public static String[] serverAuth()
	{
		String[] str=new String[2];
		ServerAuthDBConnection serverAuthDBConnection = new ServerAuthDBConnection();
		ResultSet resultSet = serverAuthDBConnection
				.retrieveUserPassword();
		try {
			while (resultSet.next()) {
				str[0] = resultSet.getObject(1).toString();
				str[1] = resultSet.getObject(2).toString();
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		serverAuthDBConnection.closeConnection();
		return str;
	}
}
