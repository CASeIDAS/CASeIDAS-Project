package es.caseidas.utils;

public class Constants {
	
	public static String PROPERTIES = "caseidas.properties";
	
	
	/*
	 *  Values to delimited the begin and and of certificates 
	 *  included within .pem registration file
	 */
	public static String BEGIN_CERT = "-----BEGIN CERTIFICATE-----";
	public static String END_CERT = "-----END CERTIFICATE-----";
	
	//Type of certificate according to RFC 5280
	public static String X509 = "X.509";
	
	
	//File path for .pem registration file
	public static String REGISTERED_AUTHORITIES_FILE = "path";
	
	
	//Data Base values
	public static String DRIVER_ORACLE = "oracle.jdbc.driver.OracleDriver";
	public static String DRIVER_MYSQL = "com.mysql.jdbc.Driver";
	
	public static String QUERY_OU = "query.OU";
	public static String QUERY_CN = "query.CN";
	
	public static String QUERY_INSERT_CAs = "query.insert.authorities";
	public static String QUERY_INSERT_NOT_REGISTERED = "query.insert.notRegistered";	
	public static String QUERY_SELECT = "query.select.all.authorities";
	
	
	//Queries for CaseidasMonitor
	public static String QUERY_STATUS_INSERT = "query.registeredfile.status.insert";
	public static String QUERY_STATUS_SELECT = "query.registeredfile.status.select";
	public static String QUERY_STATUS_UPDATE = "query.registeredfile.status.update";
	
	
	//Fields contained in RFC 5280 for x509.v3 digital certificates
	public static String COUNTRY = "C";
	public static String ORGANIZATION = "O";
	public static String ORG_UNIT = "OU";
	public static String COMMON_NAME = "CN";

}
