package LIS_System;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.simple.JSONObject;

public class LIS_StatusChecking {
	static String str="";
	static String[][] result; 
	private static final String STATUS_API_URL = "http://itd-saas03.cl-srv.ondgni.com/Rotary/api/BookingAPI/TestStatusAPI\n"
			+ "";

	//private static final String POST_PARAMS ;

	// sendGET();
	public static void main(String[] args) throws IOException {
		//System.out.println("GET DONE");
		CheckStatus("RGH121556");
		System.out.println("POST DONE");
	}
	public static String[][] CheckStatus(String workorderNO) throws IOException {
		URL obj = new URL(STATUS_API_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);

		String k = "{\n"
				+ "    \"WorkOrderID\":\""+workorderNO+"\"\n"
				+ "}\n"
				+ "";

		OutputStream os = con.getOutputStream();
		byte[] input = k.getBytes("utf-8");
		os.write(input, 0, input.length);			

		os.flush();
		os.close();
		// For POST only - END

		int responseCode = con.getResponseCode();
		System.out.println(k);

		System.out.println("POST Response Code :: " + responseCode);

		if (responseCode == 
				HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response+"gggg");
			str=response.toString();
			getData(str);
		} else {
			System.out.println("POST request did not work.");
		}
		return result;
	}

	private static void getData(String str2) {

		JSONArray array = new JSONArray(str2);
		result=new String[array.length()][21];
		for(int i=0; i < array.length(); i++)   
		{  
			org.json.JSONObject object = array.getJSONObject(i); 

			result[i][0]=object.getString("Password_web");
			result[i][1]=object.get("Testcode").toString();
			result[i][2]=object.get("BarcodeNo").toString();
			result[i][3]=object.getString("EntryDate");
			result[i][4]=object.getString("DocUploded");
			result[i][5]=object.getString("PanelName");
			result[i][6]=object.getString("Dept");
			result[i][7]=object.getString("ItemName");
			result[i][8]=object.get("Test_ID").toString();
			result[i][9]=object.get("Approved").toString(); 
			result[i][10]=object.getString("SampleStatus"); 
			result[i][11]=object.getString("DocumentStatus"); 
			result[i][12]=object.getString("Booking_Status"); 
			result[i][13]=object.getString("LogisticStatus"); 
			result[i][14]=object.getString("Patient_ID"); 
			result[i][15]=object.getString("pinfo"); 
			result[i][16]=object.getString("PName"); 
			result[i][17]=object.getString("Password_web");
			result[i][18]=object.getString("DocumentStatus");
			result[i][19]=object.getString("SampleStatus");
			result[i][20]=object.getString("LogisticStatus");

		}  
		for(int i=0;i<result.length;i++)
		{
			for(int j=0;j<result[0].length;j++)
			{
				System.out.println("arun :"+result[i][j]);
			}
			System.out.println("next-------------------");
		}

	}
}
