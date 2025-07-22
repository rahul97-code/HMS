package LIS_System;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieManager;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.simple.JSONObject;

public class LIS_Cancel {
	static String str="";
	private static final String CANCEL_API_URL = "http://192.168.1.130:8080/rotary/api/BookingAPI/TestCancelAPINew"
			+ "";

	//private static final String POST_PARAMS ;

	// sendGET(); 
	public static void main(String[] args) throws IOException {

	}
	public static Boolean cancelExam(String workorderNO,String examlisCode) throws IOException {
		if(!netIsAvailable()) {
			System.out.println("Please Provide Internet!");
			return false;
		}
		URL obj = new URL(CANCEL_API_URL);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "application/json");
		con.setRequestProperty("Accept", "application/json");
		con.setDoOutput(true);
		con.setDoInput(true);
		StringBuilder sb = new StringBuilder();
				String s = "" + "\"" + examlisCode + "\"" + "";
					sb.append(s);
		String k = "{\n"
				+ "\"WorkOrderID\":\""+workorderNO+"\",\n"
				+ "\"TestCode\": ["+sb+"]\n"
				+ "}";
		System.out.println(k);
		OutputStream os = con.getOutputStream();
		byte[] input = k.getBytes("utf-8");
		os.write(input, 0, input.length);			
		os.flush();
		os.close();
		// For POST only - END
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		InputStream InputStream=null;
		if (responseCode == 
				HttpURLConnection.HTTP_OK) { 
			InputStream=con.getInputStream();	
		}else {
			InputStream=con.getErrorStream();	
		}
		BufferedReader in = new BufferedReader(new InputStreamReader(InputStream));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
			str = response.toString();
			System.out.println(str);
			if (str.equals("Record Saved Successfully")) {
				return true;
			} else if (str.equals("No Record Found")) {
				return true;
			} else {
				org.json.JSONObject myResponse = new org.json.JSONObject(str + "");
				if(myResponse.getString("data")!="")
				{
				System.out.println(myResponse.getString("data"));
				return false;
				}else
				{	System.out.println( myResponse.getString("message"));
					return false;
				}
			}
	}
	private static boolean netIsAvailable() {
	    try {
	        final URL url = new URL("http://www.google.com");
	        final URLConnection conn = url.openConnection();
	        conn.connect();
	        conn.getInputStream().close();
	        return true;
	    } catch (MalformedURLException e) {
	        throw new RuntimeException(e);
	    } catch (IOException e) {
	        return false;
	    }
	}


}