package hms.AutoDeleteFiles;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.FileTime;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AutoDeleteFiles {
	private static  String folder;
	private static  int before_days;


	/**  This program is used to delete the file which are old **/	
	public static void AutoDeleteFiles(String Folder,int BeforeDay) {
		
		// TODO Auto-generated constructor stub	public void DeleteFiles(String Folder,int BeforeDay) {
		try {
			File folder = new File(Folder);
			File[] listOfFiles = folder.listFiles();
			if(listOfFiles!=null) {
			for (int i = 0; i < listOfFiles.length; i++) {
				if (listOfFiles[i].isFile()) {
					FileTime creationTime = (FileTime) Files.getAttribute(listOfFiles[i].toPath(), "creationTime");
					SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd"); 
					Date date = dt.parse(creationTime.toString());
					Calendar cal = Calendar.getInstance();
					cal.setTime(new Date());
					cal.add(Calendar.DATE, BeforeDay);
					Date dateBefore1 = cal.getTime();
					System.out.println(listOfFiles[i].getName()+" Creation Date:-"+dt.format(date));
					if(date.before(dateBefore1)) {
						listOfFiles[i].delete();
					}
				} 
			}
			}
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	public static void DeleteFolderData() {
		// TODO Auto-generated method stub
		AutoDeleteDataBase AutoDeleteDataBase=new AutoDeleteDataBase();
		ResultSet rs=AutoDeleteDataBase.getDATA();
		try {
			while(rs.next()) {
				AutoDeleteFiles(rs.getString(1),rs.getInt(2));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			
			new AutoDeleteFiles().DeleteFolderData();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
