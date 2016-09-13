package es.caseidas.bbdd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.caseidas.utils.Constants;

public class DataBaseManager {
	
	static Logger logger = Logger.getLogger(DataBaseManager.class);

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static Connection getConnectionForOracle  (Properties properties) throws SQLException {
		
		logger.info("getConnection method...");
		
		Connection conn = null;		 

		//Data Base values
		String connectionChain = "jdbc:oracle:thin:@";
		String dataBaseIP = properties.getProperty("bbdd.oracle.ip");
		String dataBasePort = properties.getProperty("bbdd.oracle.port");
		String dataBaseService = properties.getProperty("bbdd.oracle.service");
		String dataBaseUser = properties.getProperty("bbdd.oracle.user");
		String dataBasePass = properties.getProperty("bbdd.oracle.password");
		
	
		connectionChain = connectionChain + dataBaseIP +  ":" + dataBasePort +  ":" + dataBaseService;
		logger.info("connectionChain="+connectionChain);	
		
		try {		
			
			Class.forName(Constants.DRIVER_ORACLE);
			logger.info("Driver charging....");
			
			conn = DriverManager.getConnection(connectionChain,dataBaseUser,dataBasePass);			
			logger.info("Connected to database....");
		
		} catch (Exception e) {
			logger.error(e);
		}
		
		return conn;
	}
	
	
	public static Connection getConnectionForMySQL (Properties properties) throws SQLException {
		
		logger.info("getConnection method...");
		
		Connection conn = null;		 

		//Data Base values
		String connectionChain = "jdbc:mysql://"; 
		String dataBaseIP = properties.getProperty("bbdd.mysql.ip");
		String dataBasePort = properties.getProperty("bbdd.mysql.port");
		String dataBaseSchema = properties.getProperty("bbdd.mysql.schema");
		String dataBaseUser = properties.getProperty("bbdd.mysql.user");
		String dataBasePass = properties.getProperty("bbdd.mysql.password");
		
		//Example: DB_URL = "jdbc:mysql://localhost/EMP";
		connectionChain = connectionChain + dataBaseIP +  ":" + dataBasePort +  "/" + dataBaseSchema;
		logger.info("connectionChain="+connectionChain);	
		
		try {		
			
			Class.forName(Constants.DRIVER_MYSQL);
			logger.info("Driver charging....");
			
			conn = DriverManager.getConnection(connectionChain,dataBaseUser,dataBasePass);			
			logger.info("Connected to database....");
		
		} catch (Exception e) {
			logger.error(e);
		}
		
		return conn;
	}
	
	
	
}
