package mytrial;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GrabData {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		try {
			new GrabData().getData();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println("अम्बे जी कि आरती");
 catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getData() throws SQLException, FileNotFoundException, UnsupportedEncodingException
	{
		
		PrintWriter writer = new PrintWriter("C:/Users/Sukhpal/Desktop/sukhpal.txt", "UTF-8");
		
		
		TrialDBConnection connection=new TrialDBConnection();
		
	     ResultSet rs = connection.retrieveData();
       
          while(rs.next()) {
        	  String str="$";
                  str =str+ "\""+rs.getObject(1)+ "\",";
                  str =str+ "\""+rs.getObject(2)+ "\",";
                  str =str+ "\""+rs.getObject(3)+ "\",";
                  str =str+ "\""+rs.getObject(4)+ "\",";
                  str =str+ "\""+rs.getObject(5)+ "\"#";
                 // System.out.println(str);
                  writer.println(str);

          }
          writer.close();
        //  file(str);
	}
	
	public void file(String str)
	{
		try{
    		String data = str;
    		
    		File file =new File("C:/Users/Sukhpal/Desktop/javaio-appendfile.txt");
    		
    		//if file doesnt exists, then create it
    		if(!file.exists()){
    			file.createNewFile();
    		}
    		
    		//true = append file
    		FileWriter fileWritter = new FileWriter(file.getName(),true);
    	    BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
    	    bufferWritter.write(data);
    	    bufferWritter.close();
    	    
	        System.out.println("Done");
	        
    	}catch(IOException e){
    		e.printStackTrace();
    	}
	}
}
