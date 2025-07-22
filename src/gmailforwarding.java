import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.mail.Flags;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;

public class gmailforwarding {

	public static void main(String[] args) {
		Properties properties = new Properties();
		properties.put("mail.store.protocol", "pop3");
		properties.put("mail.pop3s.host", "pop.gmail.com");
		properties.put("mail.pop3s.port", "995");

		properties.put("mail.smtp.host", "smtp.gmail.com");
		properties.put("mail.smtp.socketFactory.port", "465");
		properties.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.port", "587");
		Session session = Session.getDefaultInstance(properties);
		for (int i = 1; i <3 ; i++) {
		try {
			// session.setDebug(true);
			// Get a Store object and connect to the current host
			Store store = session.getStore("pop3s");
			store.connect("pop.gmail.com", "sukhpalsaini05@gmail.com",
					"karmodevi");// change the user and password accordingly

			// Create a Folder object and open the folder
			Folder folder = store.getFolder("inbox");
			//folder.open(Folder.READ_ONLY);
			folder.open(Folder.READ_WRITE);
			 int count = folder.getMessageCount();
			 
			 System.out.println("sdfsdfsd"+count);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					System.in));
			int n=folder.getMessageCount();
			Message[] messages = folder.search(new FlagTerm(new Flags(Flag.RECENT), false));
			 
			System.out.println("messages.length" + messages.length);
			if (messages.length != 0) {

				 

				Message message = messages[messages.length-i];
				System.out.println(message);
				// Get all the information from the message
				String from = "sukhpalsaini05@gmail.com";
				if (from != null) {
					System.out.println("From: " + from);
				}
				String replyTo = InternetAddress.toString(message.getReplyTo());
				if (replyTo != null) {
					System.out.println("Reply-to: " + replyTo);
				}
				String to = (Message.RecipientType.TO).toString();
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
				 System.out.println(i + ": " + messages[i].getFrom()[0] 
				         + "	" + messages[i].getSubject());

				      
				         messages[messages.length-i].setFlag(Flags.Flag.DELETED, true);
				         System.out.println("Msg Delete .....");
				System.out.print("Do you want to reply [y/n] : ");
				String ans = reader.readLine();
				if ("Y".equals(ans) || "y".equals(ans)) {
					Message forward = new MimeMessage(session);
					// Fill in header
					forward.setRecipients(Message.RecipientType.TO,
							InternetAddress.parse(from));
					forward.setSubject("Fwd: " + message.getSubject());

					forward.setFrom(new InternetAddress(to));
					
					// Create the message part
					MimeBodyPart messageBodyPart = new MimeBodyPart();
					// Create a multipart message
					Multipart multipart = new MimeMultipart();
					// set content
					messageBodyPart.setContent(message, "message/rfc822");
					// Add part to multi part
					multipart.addBodyPart(messageBodyPart);
					// Associate multi-part with message
					forward.setContent(multipart);
					forward.saveChanges();

					// Send the message by authenticating the SMTP server
					// Create a Transport instance and call the sendMessage
					Transport t = session.getTransport("smtp");
					try {
						// connect to the smpt server using transport instance
						// change the user and password accordingly
						t.connect("sukhpalsaini05@gmail.com", "karmodevi");
						t.sendMessage(forward, forward.getAllRecipients());
					} finally {
						t.close();
					}

					System.out.println("message forwarded successfully....");

					// close the store and folder objects
					folder.close(false);
					store.close();
				}// end if

			}// end for
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		}
	}

}