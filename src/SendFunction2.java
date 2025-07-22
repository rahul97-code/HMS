/**
 * Developed by Amit Kumar
 * @MDI
 * Production Planner
 */

import hms.main.DBConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.pop3.POP3Store;

/**
 * 
 * @author Amit
 */
public class SendFunction2 {

	static DBConnection db = new DBConnection();
	static String username = "";
	static String password = "";
	static Message msgg;

	/*
	 * Used to send email
	 */
	public static boolean sendMail() throws IOException {
		try {
			Transport.send(msgg);
			return true;
		} catch (MessagingException ex) {
			Logger.getLogger(SendFunction2.class.getName()).log(Level.SEVERE,
					null, ex);
			return false;
		}

	}

	/*
	 * Used o get Session by authenticating using username and password
	 */
	public static Session getSession() throws IOException {
		InputStream inp = null;
		Session session = null;
//		username = "sukhpalsaini05@yahoo.com";
//		password = "karmodevi";
		
		username = "rajiv@mdimembrane.com";
		password = "Mdi//9000";

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.bizmail.yahoo.com");
		props.put("mail.pop3.host",  "pop.bizmail.yahoo.com");  
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "995");
		props.put("mail.smtp.socketFactory.fallback", "false");  
		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		return session;
	}

	public static void inbox() throws MessagingException, IOException
	{
		
		
		   
		POP3Store emailStore = (POP3Store) getSession().getStore("pop3");  
		   emailStore.connect(username, password);  
		  
		   //3) create the folder object and open it  
		   Folder emailFolder = emailStore.getFolder("INBOX");  
		   emailFolder.open(Folder.READ_ONLY);  
		  
		   //4) retrieve the messages from the folder in an array and print it  
		   Message[] messages = emailFolder.getMessages();  
		   for (int i = 0; i < messages.length; i++) {  
		    Message message = messages[i];  
		    System.out.println("---------------------------------");  
		    System.out.println("Email Number " + (i + 1));  
		    System.out.println("Subject: " + message.getSubject());  
		    System.out.println("From: " + message.getFrom()[0]);  
		    System.out.println("Text: " + message.getContent().toString());  
		   }  
		  
		   //5) close the store and folder objects  
		   emailFolder.close(false);  
		   emailStore.close();  
		  
		  
	}
	/*
	 * Used to build message by settings different properties of the message
	 */

	public static void setMessage(String to, String dearName, String subject,
			String content) throws IOException {
		try {
			msgg = new MimeMessage(getSession());
			msgg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
//			String to1[] = {"a@gmail.com"} //Mail id you want to send;
//			  InternetAddress[] address = new InternetAddress[to1.length];
//			  for(int i =0; i< to1.length; i++)
//			  {
//			      address[i] = new InternetAddress(to1[i]);
//			  }
//
//			   msgg.setRecipients(Message.RecipientType.TO, address);
			msgg.setFrom(new InternetAddress(username));
			msgg.setSubject(subject);
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = null;
			multipart = new MimeMultipart();
			 messageBodyPart.setContent(dearName+"   \n"+content,"text/html; charset=utf-8");
			 multipart.addBodyPart(messageBodyPart);
//			for (int i = 0; i < attachment.length; i++) {
//				messageBodyPart = new MimeBodyPart();
//				System.out.print(attachment[i]);
//				String file = "localTemp/"+attachment[i];
//				String fileName = ""+attachment[i];
//				DataSource source = new FileDataSource(file);
//				messageBodyPart.setDataHandler(new DataHandler(source));
//				messageBodyPart.setFileName(fileName);
//				multipart.addBodyPart(messageBodyPart);
//			}

			msgg.setContent(multipart);
			String msg = content, str1, str2, str3;
			int indexOf = content.indexOf("Sir,");
			if (indexOf > 0) {
				str1 = content.substring(0, indexOf);
				str2 = "Dr. " + dearName;
				str3 = content.substring(indexOf + 2);
				msg = str1.concat(str2).concat(str3);
			}
			indexOf = content.indexOf("sir,");
			if (indexOf > 0) {
				str1 = content.substring(0, indexOf);
				str2 = "Dr. " + dearName;
				str3 = content.substring(indexOf + 2);
				msg = str1.concat(str2).concat(str3);
			}
			// msgg.setContent("<h1>sukhpal saini</h1>", "text/html; charset=utf-8");
		} catch (Exception ex) {
			Logger.getLogger(SendFunction2.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}
public static void main(String[] arfg) {
		
		String[] ss=new String[1];
		
		ss[0]="logo.png";
		try {
			setMessage("sukhpalsaini05@gmail.com", "sukhpal", "sukhpal",
					"sukhpal");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			inbox();
		} catch (MessagingException | IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		try {
			System.out.println(sendMail());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
