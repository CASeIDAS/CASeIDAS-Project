import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.caseidas.CASEIDAS;
import es.caseidas.bbdd.DataBaseCaseidas;
import es.caseidas.bbdd.DataBaseManager;
import es.caseidas.bean.CertificateAuthorityIssuerBean;
import es.caseidas.mail.MailProviderSmtp2Go;
import es.caseidas.utils.Constants;
import es.caseidas.utils.PropertiesUtil;


public class Process {
	
	static Logger logger = Logger.getLogger(Process.class);

	public static void main(String[] args) {
		Properties properties = new PropertiesUtil().loadProperties(PropertiesUtil.DEFAULT_PROPERTIES_FILE);		
		MailProviderSmtp2Go mailprovider = new MailProviderSmtp2Go(properties);
		mailprovider.sendMessage(properties);
		
		
		
		int serviceCalled = -1;
		
		try {
			//each operation is considered a service, so it is called individually
			
			serviceCalled = (Integer.valueOf(args[0])).intValue();
			
			switch(serviceCalled) {
			
			case 1: 
					break;
				
			
			}
			
			
			
		} catch (Exception e) {			
	          logger.error(e);
	          System.exit(serviceCalled);
	     }
		

			
		CASEIDAS caseidas = null;			
		ArrayList<CertificateAuthorityIssuerBean> refenceCAlist =  null;
		ArrayList<CertificateAuthorityIssuerBean> notRegisteredCAlist =  null;
		
	  	try {
	      	
	  		caseidas = new CASEIDAS();
	  		caseidas.loadCertificateAuthoritiesPEMList();     	
	   
 		    String insertSQL = null;
 		    
// 		    insertSQL = caseidas.getProperties().getProperty(Constants.QUERY_INSERT_CAs);
//	  		DataBaseCaseidas.insertCertificacionAuthoritiesFromList(caseidas.getCertificateAuthoritiesList(), caseidas.getProperties());
	  		
 		   refenceCAlist = DataBaseCaseidas.getCertificacionAuthoritiesReferenceList(caseidas.getProperties());
	  		
 		   notRegisteredCAlist = caseidas.checkCertificacionAuthoritiesFromList(refenceCAlist);
	  		
	  		if( notRegisteredCAlist.size() > 0  ) {
	  			insertSQL = caseidas.getProperties().getProperty(Constants.QUERY_INSERT_NOT_REGISTERED);
	  			DataBaseCaseidas.registerCertificacionAuthoritiesFromPEMList(
	  					notRegisteredCAlist , caseidas.getProperties(),insertSQL);
	  		}
	  		
	  		
	  		
	  		logger.info("Process finished...");
	  		
	      } catch (Exception e) {
	          logger.error(e);
	      }

	}
	


}
