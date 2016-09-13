package es.caseidas.mail;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.apache.log4j.Logger;
import es.caseidas.utils.PropertiesUtil;

/*
 *  This class has been facilitated by Smtp2Go in the following address
 *  https://www.smtp2go.com/docs/java/
 */

public class Smtp2go {
	
	static Logger logger = Logger.getLogger(Smtp2go.class);
	

    public void sendMail(String issuerCAName) {
        Properties properties = new Properties();
        
        try {
        	logger.info("properties loading ");        	
        	properties = new PropertiesUtil().loadProperties(PropertiesUtil.DEFAULT_PROPERTIES_FILE);
        } catch (Exception e1) { 	logger.error(e1);      }
        
        logger.info("Adjusting email values....");
        
        Properties props = new Properties();     
        props.put("mail.user", properties.getProperty("mail.user"));
		props.put("mail.password", properties.getProperty("mail.password"));
		props.put("mail.host", properties.getProperty("mail.host"));
		props.put("mail.debug", properties.getProperty("mail.debug"));
		props.put("mail.store.protocol", properties.getProperty("mail.store.protocol"));
		props.put("mail.transport.protocol", properties.getProperty("mail.transport.protocol"));
		props.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));	
		
		Session session = Session.getInstance(props);	
		
        try {
        	logger.info("Beginning of the email");
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(properties.getProperty("mail.sender")));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(properties.getProperty("mail.reciever")));
            message.setSubject(properties.getProperty("mail.subject"));
            message.setText(properties.getProperty("mail.text") + "\n" + issuerCAName + "\n");
            Transport.send(message);
            
            logger.info("The eMail has been sent successfully...");
        } catch (MessagingException e) {
        	logger.error(e);
        }
    }
    
    
    
	public static void main(String[] args) {
		//new Smtp2go().envioMail("");
	}
    
    
}
