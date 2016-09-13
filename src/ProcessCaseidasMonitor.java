import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.caseidas.monitor.CaseidasMonitor;
import es.caseidas.utils.PropertiesUtil;



public class ProcessCaseidasMonitor {
	
	public static String PROPERTIES_FILE = "caseidas.properties";
	
	static Logger logger = Logger.getLogger(ProcessCaseidasMonitor.class);
	
	private static Properties properties;

	public static void main(String[] args) {		
		
		System.out.println("Launching ProcessCaseidasMonitor...");	
		
		//seconds default value is 1/2 minute
		int SECONDS_DEFAULT_VALUE = 30;
		
		int seconds = SECONDS_DEFAULT_VALUE;
				
		if( args != null  ) 
			if( args.length > 0 ) {
				try {
					seconds = Integer.valueOf(args[0]).intValue();
				}catch(Exception e) { 
					seconds = SECONDS_DEFAULT_VALUE;
					e.printStackTrace(); 
				}
			}			
		
		System.out.println("Establishing seconds="+seconds);
		
		properties = new PropertiesUtil().loadProperties(PropertiesUtil.DEFAULT_PROPERTIES_FILE);
		
		//1 millisecond = 1000 seconds
		new CaseidasMonitor(seconds*1000,properties).start();
	}
	


}
