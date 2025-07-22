package LIS_System;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import org.json.JSONArray;
import org.json.simple.JSONObject;

public class GetLisReportURL {
	static String str="",testID="";
	static String reportURL="http://192.168.1.130:8080/Rotary/Design/Lab/labreportnew.aspx?IsPrev=0&PHead=1&testid=";
	//	private static final String STATUS_API_URL = "http://itd-saas03.cl-srv.ondgni.com/Rotary/api/BookingAPI/TestStatusAPI";
	private static final String STATUS_LIVE_API_URL="http://192.168.1.130:8080/Rotary/api/BookingAPI/TestStatusAPI";

	// sendGET();
	public static void main(String[] args) throws IOException {
		System.out.println("POST DONE "+getURL("RGH121556","640",false));
	}
	public static String getURL(String workorderNO,String testCode,boolean all){
		System.out.println(workorderNO);
		try {
			URL	obj = new URL(STATUS_LIVE_API_URL);

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
			int responseCode = con.getResponseCode();
			//		System.out.println(k);
			System.out.println("POST Response Code :: " + responseCode);
			if (responseCode == HttpURLConnection.HTTP_OK) { 
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				str=response.toString();
				if(all)
					getAllTestIDData(str);
				else
					getData(str,testCode);
			} else {
				System.out.println("POST request did not work.");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				System.out.println(reportURL+testID+",");
		return reportURL+testID+",";
	}

	private static void getData(String str2,String testCode) {
		JSONArray array = new JSONArray(str2);
		for(int i=0; i < array.length(); i++)   
		{  
			org.json.JSONObject object = array.getJSONObject(i); 
			if(object.get("Testcode").toString().equals(testCode))
			{testID=object.get("Test_ID").toString();
			break;}
		}  
	}
	private static void getAllTestIDData(String str2) {
		JSONArray array = new JSONArray(str2);
		StringBuilder testIDs = new StringBuilder();
		for (int i = 0; i < array.length(); i++) {
			org.json.JSONObject object = array.getJSONObject(i);
			if (object.has("Test_ID")) {
				if (testIDs.length() > 0) {
					testIDs.append(",");
				}
				testIDs.append(object.get("Test_ID").toString());
			}
		}
		testID= testIDs.toString();
	}

}
