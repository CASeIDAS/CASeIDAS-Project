package es.caseidas.utils;

import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public class PropertiesUtil {
	
	static Logger logger = Logger.getLogger(PropertiesUtil.class);
	
	public static String DEFAULT_PROPERTIES_FILE = "caseidas.properties";
	
	
	public Properties loadProperties(String propertiesFilePath) {
		
		InputStream input = null;
		Properties properties = null;
	      
	  	try {
		
				//loading process values
		      	properties = new Properties();
		      	input = getClass().getClassLoader().getResourceAsStream(propertiesFilePath);
		      	properties.load(input);
				
		
	  	} catch (Exception e) {
	          logger.error(e);
	    }
	  	
	  	return properties;
	}

	
	
}
