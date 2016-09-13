package es.caseidas.bbdd;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.caseidas.bean.CertificateAuthorityIssuerBean;
import es.caseidas.utils.Constants;

public class DataBaseCaseidas {
	
	static Logger logger = Logger.getLogger(DataBaseCaseidas.class);
	
	
	public static void registerCertificacionAuthoritiesFromPEMList (ArrayList<CertificateAuthorityIssuerBean> certificateAuthoritiesList, Properties properties, String insertSQL) {
		
		logger.info("insertCertificacionAuthoritiesFromList init....");
		
		
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		
		CertificateAuthorityIssuerBean certificateAuthorityIssuerBean = null;
		String OrgUnit = null;
		String CommonName = null;
		
		
		try {
			
			connection = DataBaseManager.getConnectionForMySQL(properties);			
        
			for ( int i=0; i< certificateAuthoritiesList.size(); i++  ) {
				
				certificateAuthorityIssuerBean = certificateAuthoritiesList.get(i);
				preparedStatement = connection.prepareStatement(insertSQL);
				
				// 1-COUNTRY, 2-ORGANIZATION, 3-ORG_UNIT, 4-COMMON_NAME
				preparedStatement.setString(1, certificateAuthorityIssuerBean.getCountry());
				preparedStatement.setString(2, certificateAuthorityIssuerBean.getOrganization());
				
				OrgUnit = certificateAuthorityIssuerBean.getOrganizationalUnit();
				CommonName = certificateAuthorityIssuerBean.getCommonName();
				
				if (OrgUnit != null)
					preparedStatement.setString(3, OrgUnit);
				else 
					preparedStatement.setNull(3, Types.VARCHAR);
				
				
				if (CommonName != null)
					preparedStatement.setString(4, CommonName);
				else 
					preparedStatement.setNull(4, Types.VARCHAR);
				
				preparedStatement.execute();			
				
			}
			
		//connection.commit();  No needed
			
		
		}catch (Exception e) {
			logger.error(e);
		}finally {
			try {
					preparedStatement.close();
					connection.close();
			
			}catch (Exception e) {
				logger.error(e);
			}
			
		}	
		
	}
	
	public static ArrayList<CertificateAuthorityIssuerBean> getCertificacionAuthoritiesReferenceList ( Properties properties) {
		
		logger.info("selectCertificacionAuthoritiesList init....");
		
		
		String selectSQL = null;
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		int ACs_InitialCapacity = 50;
		
		ArrayList<CertificateAuthorityIssuerBean> listCertificateAuthorityIssuerBean = new ArrayList<CertificateAuthorityIssuerBean>(ACs_InitialCapacity);		
		CertificateAuthorityIssuerBean certificateAuthorityIssuerBean = null;
		ResultSet rs = null;
		
		//Data field obtained from query
		String country;		
		String organization;		
		String orgUnit;		
		String cn;
		
		
		try {
			
			selectSQL = properties.getProperty(Constants.QUERY_SELECT);
			connection = DataBaseManager.getConnectionForMySQL(properties);
			preparedStatement = connection.prepareStatement(selectSQL);
			rs = preparedStatement.executeQuery();
			
		
        
			while ( rs.next() ) {					
				
				// 1-COUNTRY, 2-ORGANIZATION, 3-ORG_UNIT, 4-COMMON_NAME
				country = rs.getString(1);
				organization = rs.getString(2);	
				orgUnit = rs.getString(3);
				cn = rs.getString(4);
				
				certificateAuthorityIssuerBean = new CertificateAuthorityIssuerBean(country, organization,orgUnit,cn);				
				listCertificateAuthorityIssuerBean.add(certificateAuthorityIssuerBean);			
			}
			
		
		}catch (Exception e) {
			logger.error(e);
		}finally {
			try {
					preparedStatement.close();
					connection.close();
			
			}catch (Exception e) {
				logger.error(e);
			}
			
		}	
		
		return listCertificateAuthorityIssuerBean;
		
	}
	
	public static String getSQLResultsAsCSV ( Properties properties, String query) {
		
		logger.info("getSQLResultsAsCSV init....");		
		logger.info("query="+query);
		
		
		
		Connection connection = null;	
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		//Data field obtained from query converted as CSV (value1;value;....;valueN)
		String result = "";		

		
		try {	
			
			connection = DataBaseManager.getConnectionForMySQL(properties);
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();
			
		
			logger.info("ResultSet="+rs);
			
			while ( rs.next() ) {					
				
				// 1-FILE_SIZE_KB, 2-HASH_SHA_512
				logger.info("rs.getString(1)="+rs.getString(1));
				result = rs.getString(1);
				result = result + ";";
				
				logger.info("rs.getString(2)="+rs.getString(2));
				result =  result + rs.getString(2);		
			}
			
			
			logger.info("The result has been packed="+result);
		
		}catch (Exception e) {
			logger.error(e);
		}finally {
			try {
					preparedStatement.close();
					connection.close();
			
			}catch (Exception e) {
				logger.error(e);
			}
			
		}
		
		
		
		return result;
	}
	
	

}
