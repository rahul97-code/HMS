
import hms.patient.database.PatientDBConnection;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;


public class SaltFilter {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub

		SaltDBConnection connection = new SaltDBConnection();
		ResultSet resultSet = connection
				.retrieveSalts();
		try {
			while (resultSet.next()) {
				String saltName = resultSet.getObject(1).toString();
//				System.out.println(saltName);
				saltName=saltName.replaceAll("\\+", " ");
				saltName=saltName.replaceAll("-", " ");
				saltName=saltName.replaceAll("/", " ");
				saltName=saltName.replaceAll("%W", "");
			
				String arrayS[]=saltName.split(" ");
				String query="";
				for (int i = 0; i < arrayS.length; i++) {
					
					query=query+" f4 LIKE '%"+StringUtils.substring(arrayS[i].trim(), 0, 4)+"%' AND";
					
				}
				System.out.println(query);
				connection.insertData(query,saltName.length()+15);
			
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.closeConnection();
	}

}
