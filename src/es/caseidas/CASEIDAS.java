package es.caseidas;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.BasicConfigurator;

import es.caseidas.bbdd.DataBaseManager;
import es.caseidas.bean.CertificateAuthorityIssuerBean;
import es.caseidas.utils.Constants;

public class CASEIDAS extends Thread {
	
	static Logger logger = Logger.getLogger(CASEIDAS.class);
	
	private Properties properties;
	
	public static String PROPERTIES_FILE = "caseidas.properties";
	
	private ArrayList<CertificateAuthorityIssuerBean> certificateAuthoritiesPEMList;
	
	private ArrayList<CertificateAuthorityIssuerBean> notRegisteredList;
	
	private static int ACs_InitialCapacity = 50;
	
	
	public CASEIDAS() {
		
		InputStream input = null;
      
	  	try {
	      	
	      	logger.info("Initializing process...");
	      	BasicConfigurator.configure();
	
	      	//loading process values
	      	properties = new Properties();
	      	input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
	      	properties.load(input);
	      	
	      	
	      	//loading ACs list included within .pem file
	      	certificateAuthoritiesPEMList = new ArrayList<CertificateAuthorityIssuerBean>(ACs_InitialCapacity);
	      	
	      	//not registered initially is empty
	      	notRegisteredList = new ArrayList<CertificateAuthorityIssuerBean>(ACs_InitialCapacity);
      	
	      } catch (Exception e) {
	          logger.error(e);
	      }		
	}
	
	
	public void loadCertificateAuthoritiesPEMList() {
		
		 String registeredACsFile = properties.getProperty(Constants.REGISTERED_AUTHORITIES_FILE);
		 BufferedReader reader = null;
		 String line = null;
         String certificateText = "";
         
         InputStream byteArrayInputStream = null;
         CertificateFactory certificateFactory = null;
         Certificate certificate = null;
         X509Certificate x509Certificate = null;
         String certificateIssuerData = null;
         CertificateAuthorityIssuerBean certificateAuthorityIssuerBean = null;
		 
		 try {
			 
			 reader = new BufferedReader(new FileReader(registeredACsFile));
			 
			 //Reading .pem file
			 while ((line = reader.readLine()) != null) {
				 
				 	//certificate found
					if (line.equals(Constants.BEGIN_CERT)) {
	                    
	                    certificateText = certificateText + line;
	                    certificateText = certificateText + "\r\n";
	                    
	                    //reading the certificate
	                    while (!line.equals(Constants.END_CERT)) {
	                        line = reader.readLine();
	                        certificateText = certificateText + line;
	                        certificateText = certificateText + "\r\n";
	                    }
	                    
	                    //converting text data into java object x509Certificate
	                    certificateFactory = CertificateFactory.getInstance(Constants.X509);
	                    byteArrayInputStream = new ByteArrayInputStream(certificateText.getBytes());	                    
	                    certificate = certificateFactory.generateCertificate(byteArrayInputStream);
	                    x509Certificate = (X509Certificate)certificate;
	                    
	                    //ready for next certificate data
	                    certificateText = "";
	                    
	                    
	                    //obtaining the relevant of the x509Certificate extracted
	                    certificateIssuerData = x509Certificate.getSubjectDN().toString();
	                    logger.info("Issuer obtained from certificate:"+certificateIssuerData);
	                    
	                    //creating the issuerBean from x509Certificate reading relative distinguish name
	                    certificateAuthorityIssuerBean = new CertificateAuthorityIssuerBean(certificateIssuerData);
	                    certificateAuthoritiesPEMList.add(certificateAuthorityIssuerBean);
	                    
					}                  			 
			 }	 
			 
			 logger.info("Total CAs registered="+certificateAuthoritiesPEMList.size());
			 
			 
		 } catch (Exception e) {
	        	logger.error(e);
	     }		
	}
	
	
	public ArrayList<CertificateAuthorityIssuerBean> checkCertificacionAuthoritiesFromList (ArrayList<CertificateAuthorityIssuerBean> referenceCAlist) {
		
		
		CertificateAuthorityIssuerBean certificateAuthorityIssuerBean = null;
		
		try {
				for ( int i=0; i< this.certificateAuthoritiesPEMList.size(); i++  ) {			
					certificateAuthorityIssuerBean = certificateAuthoritiesPEMList.get(i);
					logger.info("Checking item "+certificateAuthorityIssuerBean);
					
					if( ! referenceCAlist.contains(certificateAuthorityIssuerBean) ) {
						//if the reference list does not contain the item, that means something wrong 
						 this.notRegisteredList.add(certificateAuthorityIssuerBean);
						 logger.info("item included!");
					}
				}		
		
		}catch (Exception e) {
			logger.error(e);
		}
		
		logger.info("Total not registered items="+this.notRegisteredList.size());
		return this.notRegisteredList;
	}
	


	public Properties getProperties() {
		return properties;
	}


	public ArrayList<CertificateAuthorityIssuerBean> getCertificateAuthoritiesPEMList() {
		return certificateAuthoritiesPEMList;
	}	
  
  
    
}
