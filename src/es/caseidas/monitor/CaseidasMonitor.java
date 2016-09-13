package es.caseidas.monitor;
import java.util.Calendar;
import java.util.Properties;
import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import es.caseidas.bbdd.DataBaseCaseidas;
import es.caseidas.mail.MailProviderSmtp2Go;
import es.caseidas.mail.Smtp2go;
import es.caseidas.utils.Constants;
import es.caseidas.utils.HashUtils;


/*
 * Goal of this class: monitor changes in .pem file
 *    - register changes
 *    - send email when changes occur
 * 
 */

public class CaseidasMonitor extends Thread {
	
	static Logger logger = Logger.getLogger(CaseidasMonitor.class);
	
	private Properties properties = null;
	
	private long milliseconds;
	
	private boolean needToCheck;
	
	
	
	public CaseidasMonitor(long milliseconds, Properties properties) {
		this.milliseconds = milliseconds;
		this.properties = properties;
		this.needToCheck = true;
	}
	
	
	public void run() {
		
		System.out.println(getName() + " is running....");		
		
		//get current date time with Calendar()
		Calendar cal = Calendar.getInstance();
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");	
		
		//create mail provider to send alert message to an email account
//		MailProviderSmtp2Go mailProvider = new MailProviderSmtp2Go(this.properties);
		Smtp2go mailProvider = new Smtp2go();
		
		while(needToCheck) {
			
			try {
				
				if( ! checkStatusOK() ){
					logger.info("------ ALERT: send mail to Security Administrator");
					mailProvider.sendMail("CA Issuer Name alert generated");
				}
				
				Thread.sleep(milliseconds);
				
			} catch (InterruptedException e) {
				logger.error(e);
				System.exit(-1);
			}	
			cal = Calendar.getInstance();
			System.out.println("Running at "+dateFormat.format(cal.getTime()));
		}
	} 
	
	
	public boolean checkStatusOK () {
		
		boolean statusOK = true; 
		
		logger.info("checkStatusOK......");
		
		//Compare file vs. bbdd
		
		//values from file
		String hashFile = "";
		
		double bytesFile;
		double kilobytesFile = 0;
		
		String filePath = null;
		File file = null;
		
		//values from bbdd
		String hashBBDD = null;
		String kilobytesBBDDString = null;
		double kilobytesBBDD = 0;
		
		
		
		try {
			filePath = properties.getProperty( Constants.REGISTERED_AUTHORITIES_FILE );
			
			
			// ****  Obtaining BBDD
			String query = properties.getProperty(Constants.QUERY_STATUS_SELECT);
			String result = DataBaseCaseidas.getSQLResultsAsCSV(properties,query);
			
			// 1-FILE_SIZE_KB, 2-HASH_SHA_512
			kilobytesBBDDString = result.substring(0,result.indexOf(";"));
			kilobytesBBDD = Double.valueOf(kilobytesBBDDString).doubleValue();
			hashBBDD = result.substring(result.indexOf(";")+1, result.length());
			
			logger.info("hashBBDD="+hashFile);
			logger.info("kilobytesFile="+kilobytesBBDDString);
			
			// ****  Obtaining FILE
			
			//Generating values from current state of file
			hashFile = HashUtils.getHash(filePath, HashUtils.HASH_TYPE_SHA_512);
			
			file = new File(filePath);
			bytesFile = file.length();
			kilobytesFile = (bytesFile / 1024);		
			
			logger.info("hashFile="+hashFile);
			logger.info("kilobytesFile="+kilobytesFile);			
			
		} catch (Exception e) {
			logger.error(e);
			System.exit(-1);
		}
		
		if( (! hashFile.equals(hashBBDD)) || (kilobytesFile != kilobytesBBDD) ) statusOK = false;		
		
		return statusOK;
	}

	
//	public static void main(String[] args) {	
//		
//		//Compare file vs. bbdd
//		
//		//values from file
//		String hashFile = null;
//		
//		double bytesFile;
//		double kilobytesFile = 0;
//		
//		String filePath = null;
//		File file = null;
//		
//		//values from bbdd
//		String hashBBDD = null;
//		String kilobytesBBDDString = null;
//		double kilobytesBBDD = 0;
//		
//		String result = "28655;b0533cbf2578cb7a19d3e4cfeac5ab547df028ed24b234daedb2f738f21cb2bb9db3057b6b19d839e0135d25110b5439b506940509fe5ee55645aeebe25f47de";
//	
//		
//	}

}
