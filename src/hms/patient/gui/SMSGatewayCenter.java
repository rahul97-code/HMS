package hms.patient.gui;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SMSGatewayCenter{

public static void main(String[] args)
{
URL url;

try {
    // get URL content

    String a="https://bulksmsapi.vispl.in/?username=Advancetrn&password=Advance@123&messageType=text&mobile=8295910531&senderId=RACAGH&message=This%20is%20a%20test%20message";
    url = new URL(a);
    URLConnection conn = url.openConnection();

    // open the stream and put it into BufferedReader
    BufferedReader br = new BufferedReader(
                       new InputStreamReader(conn.getInputStream()));

    String inputLine;
    while ((inputLine = br.readLine()) != null) {
            System.out.println(inputLine);
    }
    br.close();

    System.out.println("Done");

} catch (MalformedURLException e) {
    e.printStackTrace();
} catch (IOException e) {
    e.printStackTrace();
}

}
}