package hms.payments;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JOptionPane;

import org.json.JSONArray;
import org.json.JSONException;
import org.apache.commons.codec.binary.Base64;
import java.awt.HeadlessException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Vector;

public class HttpRequestService {
	private static InputStream InputStream;
	public static String sendPOST(String encryptedTxt,String url)  {
	
		try {
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("POST");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept", "application/json");
			con.setDoOutput(true);
			OutputStream os = con.getOutputStream();  
			byte[] input = encryptedTxt.getBytes("utf-8");
			os.write(input, 0, input.length);			
			os.flush();
			os.close();
			int responseCode = con.getResponseCode();
			System.out.println("POST Response Code :: " + responseCode);
			if (responseCode ==HttpURLConnection.HTTP_OK) 
				InputStream=con.getInputStream();	
			else 
				InputStream=con.getErrorStream();	

			BufferedReader in = new BufferedReader(new InputStreamReader(InputStream));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			if(responseCode==200) {
				org.json.JSONObject myResponse = new org.json.JSONObject(response+"");
				return myResponse.getString("data");
			}else {
				System.out.println(response.toString());
				JOptionPane.showMessageDialog(null, response.toString(), "Error", JOptionPane.ERROR_MESSAGE);

			}
		} catch (HeadlessException | JSONException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

}
