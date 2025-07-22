import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
  
public class forwarded{  
 public static void main(String[] args)throws Exception {  
  final String user= "rajiv@mdimembrane.com";//change accordingly  
  final String password="Mdi//9000";//change accordingly  
  
  
  Properties properties = new Properties();
  properties.put("mail.store.protocol", "pop3");
  properties.put("mail.pop3s.host", "pop.bizmail.yahoo.com");
  properties.put("mail.pop3s.port", "995");
 
	properties.put("mail.smtp.host", "smtp.bizmail.yahoo.com");
properties.put("mail.smtp.socketFactory.port", "465");
properties.put("mail.smtp.socketFactory.class",
		"javax.net.ssl.SSLSocketFactory");
properties.put("mail.smtp.auth", "true");
properties.put("mail.smtp.port", "587");

Session session = Session.getDefaultInstance(properties,  
	    new javax.mail.Authenticator() {  
	    @Override
		protected PasswordAuthentication getPasswordAuthentication() {  
	        return new PasswordAuthentication(user,password);  
	    }  
	  });  
 

     // session.setDebug(true);
     // Get a Store object and connect to the current host
     Store store = session.getStore("pop3s");
     store.connect("pop.bizmail.yahoo.com", "rajiv@mdimembrane.com",
    		 "Mdi//9000");//change the user and password accordingly
  
  //Create a Folder object and open the folder  
  Folder folder = store.getFolder("inbox");  
  folder.open(Folder.READ_ONLY);  
    
  Message message = folder.getMessage(1);  
  
  // Get all the information from the message  
  String from = InternetAddress.toString(message.getFrom());  
  if (from != null) {  
  System.out.println("From: " + from);  
  }  
  String replyTo = InternetAddress.toString(  
  message.getReplyTo());  
  if (replyTo != null) {  
  System.out.println("Reply-to: " + replyTo);  
  }  
  String to ="sukhpalsaini05@gmail.com";
  if (to != null) {  
  System.out.println("To: " + to);  
  }  
  
  String subject = message.getSubject();  
  if (subject != null) {  
  System.out.println("Subject: " + subject);  
  }  
  Date sent = message.getSentDate();  
  if (sent != null) {  
  System.out.println("Sent: " + sent);  
  }  
  System.out.println(message.getContent());  
  
  // compose the message to forward  
  Message message2 = new MimeMessage(session);  
  message2.setSubject("Fwd: " + message.getSubject());  
  message2.setFrom(new InternetAddress(from));  
  message2.addRecipient(Message.RecipientType.TO,  
  new InternetAddress(to));  
  
  // Create your new message part  
  BodyPart messageBodyPart = new MimeBodyPart();  
  messageBodyPart.setText("Oiginal message:\n\n");  
  
  // Create a multi-part to combine the parts  
  Multipart multipart = new MimeMultipart();  
  multipart.addBodyPart(messageBodyPart);  
  
  // Create and fill part for the forwarded content  
  messageBodyPart = new MimeBodyPart();  
  //messageBodyPart.setDataHandler(message.getDataHandler());  
  
  // Add part to multi part  
  multipart.addBodyPart(messageBodyPart);  
  
  // Associate multi-part with message  
  message2.setContent(multipart);  
  
  // Send message  
  Transport.send(message2);  
  
  System.out.println("message forwarded ....");  
  }  
}   