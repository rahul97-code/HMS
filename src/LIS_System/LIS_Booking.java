package LIS_System;

import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;

import LIS_UI.LIS_System;
import hms.exam.database.ExamDBConnection;
import hms.main.DateFormatChange;
public class LIS_Booking {
	static String str="";
	static String[] result;
	private static InputStream InputStream=null; 
	private static final String BOOKING_API_URL = "http://itd-saas03.cl-srv.ondgni.com/rotary/api/BookingAPI/BookingAPINew";

	public static void main(String[] args) throws IOException {
		//System.out.println("GET DONE");
		//	sendPOST();
		System.out.println("POST DONE");
	}

	public static String[] sendPOST(String[] data,Vector<String> examlisCode)  {
		
		if(!netIsAvailable()) {
			System.out.println("Please provide Internet!");
			return null;
		}
		
		try {
			URL obj = new URL(BOOKING_API_URL);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);

			StringBuilder sb = new StringBuilder();
			for (int i=0;i<examlisCode.size();i++)
			{
				if (!examlisCode.get(i).equals("0")) {
					String s = " {\n" + "        \"Rate\" :\"\",\n" + "        \"testID\" : \"\",\n"
							+ "        \"testCode\" : \"" + examlisCode.get(i) + "\",\n"
							+ "        \"integrationCode\" : \"\",\n" + "        \"dictionaryId\" : \"\",\n"
							+ "        \"sampleId\" : \"\"\n" + "      }\n" + "";
					if (examlisCode.size() - 1 > i) {
						sb.append(s).append(",");
					} else {
						sb.append(s);
					} 
				}

			}
			if(!(sb.length()>0))
			{
				return null;
			}

			String k = "{\n"
					+ "  \"mobile\" : \""+data[0]+"\",\n"
					+ "  \"email\" : \"\",\n"
					+ "  \"designation\" : \"\",\n"
					+ "  \"fullName\" : \""+data[1]+"\",\n"
					+ "  \"age\" : \""+data[2]+"\",\n"
					+ "  \"gender\" : \""+data[3]+"\",\n"
					+ "  \"area\" : \""+data[4]+"\",\n"
					+ "  \"city\" : \""+data[5]+"\",\n"
					+ "  \"patientType\" : \"\",\n"
					+ "  \"labPatientId\" : \""+data[6]+"\",\n"
					+ "  \"pincode\" : \"\",\n"
					+ "  \"patientId\" : \""+data[7]+"\",\n"
					+ "  \"dob\" : \""+data[8]+"\",\n"
					+ "  \"passportNo\" : \"\",\n"
					+ "  \"panNumber\" : \"\",\n"
					+ "  \"aadharNumber\" : \"\",\n"
					+ "  \"insuranceNo\" : \"\",\n"
					+ "  \"nationality\" : \"\",\n"
					+ "  \"ethnicity\" : \"\",\n"
					+ "  \"Panel_id\":\""+data[9]+"\",\n"
					+ "  \"CentreID\":\"1\",\n"
					+ "  \"nationalIdentityNumber\" : \"\",\n"
					+ "  \"workerCode\" : \"\",\n"
					+ "  \"doctorCode\" : \"\",\n"
					+ "  \"billDetails\" : {\n"
					+ "    \"emergencyFlag\" : \""+data[10]+"\",\n"
					+ "    \"totalAmount\" : \""+data[11]+"\",\n"
					+ "    \"advance\" : \""+data[12]+"\",\n"
					+ "    \"billDate\" : \""+data[13]+"\",\n"
					+ "    \"paymentType\" : \""+data[14]+"\",\n"
					+ "    \"referralName\" : \""+data[18]+"\",\n"
					+ "    \"otherReferral\" : \"\",\n"
					+ "    \"sampleId\" : \"\",\n"
					+ "    \"orderNumber\" : \""+data[15]+"\",\n"
					+ "    \"referralIdLH\" : \"\",\n"
					+ "    \"organisationName\" : \"\",\n"
					+ "    \"additionalAmount\" : \"0\",\n"
					+ "    \"organizationIdLH\" : \"\",\n"
					+ "    \"comments\" : \"\",\n"
					+ "    \"testList\" : [\n"
					+sb+""
					+ "    ],\n"
					+ "    \"paymentList\" : [\n"
					+ "      {\n"
					+ "        \"paymentType\" : \""+data[16]+"\",\n"
					+ "        \"paymentAmount\" : \""+data[17]+"\",\n"
					+ "        \"issueBank\" : \"\",\n"
					+ "        \"chequeNo\" : \"\"\n"
					+ "      }\n"
					+ "    ]\n"
					+ "  }\n"
					+ "}\n"
					+ "\n"
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
			if(responseCode==200) {
				org.json.JSONObject myResponse = new org.json.JSONObject(response+"");
				System.out.println(response.toString());
				result=new String[3];
				result[0]=myResponse.getString("Message");
				result[2]=myResponse.getString("url");
				JSONArray jsonArray = (JSONArray) myResponse.get("reportDetails");
				for(int i=0; i < jsonArray.length(); i++)   
				{  
					org.json.JSONObject object1 = jsonArray.getJSONObject(i); 
					result[1]=object1.getString("ledgertransactionno");

				}
			}else {
				System.out.println(response.toString());
				return null;
			}
		} catch (HeadlessException | JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return result;
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
