
import java.io.IOException;
import java.io.InputStream;
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

public class forward {
	static String username = "";
	static String password = "";
	static Message msgg;

    public static void main(String args[]) throws Exception {
        String host = "192.168.10.205";
        String user = "test";
        String password = "test";


        // Get a Store object that implements the specified protocol.
        Store store = getSession().getStore("pop3");
        store.connect("pop.bizmail.yahoo.com", username,
                forward.password);//change the user and password accordingly


        //Connect to the current host using the specified username and password.
        store.connect(host, user, password);

        //Create a Folder object corresponding to the given name.
        Folder folder = store.getFolder("inbox");

        // Open the Folder.
        folder.open(Folder.READ_ONLY);

        Message message = folder.getMessage(1);

        // Here's the big change...
        String from = InternetAddress.toString(message.getFrom());
        if (from != null) {
            System.out.println("From: " + from);
        }
        String replyTo = InternetAddress.toString(
                message.getReplyTo());
        if (replyTo != null) {
            System.out.println("Reply-to: " + replyTo);
        }
        String to = InternetAddress.toString(
                message.getRecipients(Message.RecipientType.TO));
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

        // Create the message to forward
        Message forward = new MimeMessage(getSession());

        // Fill in header
        forward.setSubject("Fwd: " + message.getSubject());
        forward.setFrom(new InternetAddress(from));
        forward.addRecipient(Message.RecipientType.TO,
                new InternetAddress(to));

        // Create your new message part
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Oiginal message:\n\n");

        // Create a multi-part to combine the parts
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);

        // Create and fill part for the forwarded content
        messageBodyPart = new MimeBodyPart();
        
        // Changed 2023-12-30 to fix compile error
        // messageBodyPart.setDataHandler(message.getDataHandler());

        // Add part to multi part
        multipart.addBodyPart(messageBodyPart);

        // Associate multi-part with message
        forward.setContent(multipart);

        // Send message
        Transport.send(forward);

        System.out.println("msg forward ....");
    }
    
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
}