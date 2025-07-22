package hms.emailreports.gui;
/**
 * Developed by sukhpal saini
 * @MDI
 * Production Planner
 */

import hms.main.DBConnection;
import hms.main.NewsDBConnection;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
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

/**
 * 
 * @author Amit
 */
public class SendFunction {

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
			Logger.getLogger(SendFunction.class.getName()).log(Level.SEVERE,
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
		
		NewsDBConnection newsDBConnection=new NewsDBConnection();
		String[] para=newsDBConnection.getEmail();
		username = para[0];
		password = para[1];
		newsDBConnection.closeConnection();

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");
		session = Session.getDefaultInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		return session;
	}

	/*
	 * Used to build message by settings different properties of the message
	 */

	public static void setMessage(String to, String dearName, String subject,
			String content, String[] attachment) throws IOException {

		try {
			msgg = new MimeMessage(getSession());
			msgg.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(to));
			msgg.setFrom(new InternetAddress(username));
			msgg.setSubject(subject);
			MimeBodyPart messageBodyPart = new MimeBodyPart();
			Multipart multipart = null;
			multipart = new MimeMultipart();
			 messageBodyPart.setContent(""+content,"text/html; charset=utf-8");
			 multipart.addBodyPart(messageBodyPart);
			for (int i = 0; i < attachment.length; i++) {
				messageBodyPart = new MimeBodyPart();
				System.out.print(attachment[i]);
				String file = "localTemp/"+attachment[i];
				String fileName = ""+attachment[i];
				DataSource source = new FileDataSource(file);
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(fileName);
				multipart.addBodyPart(messageBodyPart);
			}

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
			//msgg.setContent("<h1>sukhpal saini</h1>", "text/html; charset=utf-8");
		} catch (Exception ex) {
			Logger.getLogger(SendFunction.class.getName()).log(Level.SEVERE,
					null, ex);
		}
	}

	
}
