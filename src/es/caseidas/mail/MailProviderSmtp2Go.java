package es.caseidas.mail;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;

import es.caseidas.utils.Constants;

public class MailProviderSmtp2Go {
	
	/*
	 *  This class has been facilitated by Smtp2Go in the following address
	 *  https://www.smtp2go.com/docs/java/
	 */
	
	static Logger logger = Logger.getLogger(MailProviderSmtp2Go.class);
	
	private Properties properties = null;	
	
	private String user;	
	private String password;
	private String server;
	private String debugMail;
	private String transportProtocol;
	private String storeProtocol;
	private String smtpPort;
	
//    props.put("mail.user", properties.getProperty("mail.user"));
//		props.put("mail.password", properties.getProperty("mail.password"));		
//		props.put("mail.server", properties.getProperty("mail.server"));
//		props.put("mail.debug", properties.getProperty("mail.debug"));
//		props.put("mail.store.protocol", properties.getProperty("mail.store.protocol"));
//		props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
//		props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));	
	
	
	public MailProviderSmtp2Go (Properties properties) {		
		this.user = properties.getProperty("mail.user");
		this.password = properties.getProperty("mail.password");
		this.server = properties.getProperty("mail.server");
		this.debugMail = properties.getProperty("mail.debug");
		this.storeProtocol = properties.getProperty("mail.store.protocol");
		this.transportProtocol = properties.getProperty("mail.transport.protocol");		
		this.smtpPort = properties.getProperty("mail.smtp.port");
	}
	
	
	
	public boolean sendMessage(Properties properties ) {
		
		try {
			
	        Properties propertiesRequiredByS2Go = new Properties();     
	        propertiesRequiredByS2Go.put("mail.user", properties.getProperty("mail.user"));
	        propertiesRequiredByS2Go.put("mail.password", properties.getProperty("mail.password"));
			
			
	        propertiesRequiredByS2Go.put("mail.server", properties.getProperty("mail.server"));
	        propertiesRequiredByS2Go.put("mail.debug", properties.getProperty("mail.debug"));
	        propertiesRequiredByS2Go.put("mail.store.protocol", properties.getProperty("mail.store.protocol"));
	        propertiesRequiredByS2Go.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
	        propertiesRequiredByS2Go.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));	
			
			Session session = Session.getInstance(propertiesRequiredByS2Go);
			
			
	        try {
	        	
	        	logger.info("Creating message to begin the process....");
	        	
	            Message message = new MimeMessage(session);
	            message.setFrom(new InternetAddress(properties.getProperty("mail.sender")));
	            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("mail.reciever")));
	            message.setSubject(properties.getProperty("mail.subject"));
	            message.setText(properties.getProperty("mail.text") + "\n" + "TEXTO" + "\n");
	            Transport.send(message);
	            
	            logger.info("eMail has been sent successfully!");
	        } catch (MessagingException e) {
	        	logger.error(e);
	        }
        	
			
			
			
			
        } catch (Exception ioe) {
        	logger.error(ioe);
        }
		
		
		return true;
	}
	
	
    
    public void envioMail(String issuer) {
        final Properties properties = new Properties();
        try {
        	logger.info("properties.load");
        	properties.load(new FileInputStream(Constants.PROPERTIES));
        } catch (IOException e1) {
        	logger.error(e1);
        }
        logger.info("envioMail");
        Properties props = new Properties();     
        props.put("mail.user", properties.getProperty("mail.user"));
		props.put("mail.password", properties.getProperty("mail.password"));
		
		
		props.put("mail.server", properties.getProperty("mail.server"));
		props.put("mail.debug", properties.getProperty("mail.debug"));
		props.put("mail.store.protocol", properties.getProperty("mail.store.protocol"));
		props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));	
		
		Session session = Session.getInstance(props);
		
		
        try {
        	logger.info("Comienzo envio mail");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.sender")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("mail.reciever")));
            message.setSubject(properties.getProperty("mail.subject"));
            message.setText(properties.getProperty("mail.text") + "\n" + issuer + "\n");
            Transport.send(message);
            logger.info("Mail enviado correctamente");
        } catch (MessagingException e) {
        	logger.error(e);
        }
    }
    
    
    public static void main(String[] args) {
		
    	final String username = "joseluis.unir.2016";
		final String password = "mX3zbKJ7rMVq"; 
		
		
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "mail.smtp2go.com");
		props.put("mail.smtp.port", "2525"); // 8025, 587 and 25 can also be used. 
		
		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  }); 
		
		
		try { 
			
			Message message = new MimeMessage(session);
			Multipart mp = new MimeMultipart("alternative");
			
			BodyPart textmessage = new MimeBodyPart();
			textmessage.setText("It is a text message \n");
			
			BodyPart htmlmessage = new MimeBodyPart();
			htmlmessage.setContent("It is a html message.", "text/html");
			mp.addBodyPart(textmessage);
			mp.addBodyPart(htmlmessage);
			
			message.setFrom(new InternetAddress("sender@example.com"));
			
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse("joseluis.unir.2016@gmail.com"));
			
			message.setSubject("Java Mail using SMTP2GO");
			message.setContent(mp); 
			Transport.send(message); 
			System.out.println("Done"); 
			
		} catch (Exception e) {
			logger.error(e);
			System.exit(-1);
		}
    }
    
}
