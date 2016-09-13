package es.caseidas.bean;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import org.apache.log4j.Logger;

import es.caseidas.utils.Constants;

public class CertificateAuthorityIssuerBean {
	
	
	static Logger logger = Logger.getLogger(CertificateAuthorityIssuerBean.class);
	
	/* 
	 *   CerticateAuthority Issuer relevant fields extracted from x509Certificates
	 */
	
	private String country;
	
	private String organization;
	
	private String organizationalUnit;
	
	private String commonName;
	
	
	

	public CertificateAuthorityIssuerBean (String country, String organization, String OrgUnit, String cn) {
		this.country = country;
		this.organization = organization;
		this.organizationalUnit = OrgUnit;
		this.commonName = cn;	
	}
	
	public CertificateAuthorityIssuerBean (String IssuerData) {
		
		LdapName ldapName;
		String relativeDNType = null;
		String relativeDNValue = null;
		
		try {
			
			logger.info("loading CertificateAuthorityIssuerBean from IssuerData...");
			ldapName = new LdapName(IssuerData);
			
			for (Rdn rdn : ldapName.getRdns()) {
				
				//obtaining type to fill bean suitable data
				
				relativeDNType = rdn.getType();
				logger.info("Type obtained="+relativeDNType);
				
				relativeDNValue = rdn.getValue().toString();
				logger.info("Data obtained="+relativeDNValue);
				
				
				if (relativeDNType.equalsIgnoreCase(Constants.COUNTRY)) 
					this.country = relativeDNValue;					
				else if (relativeDNType.equalsIgnoreCase(Constants.ORGANIZATION))
					this.organization = relativeDNValue;
				else if (relativeDNType.equalsIgnoreCase(Constants.ORG_UNIT))
					this.organizationalUnit = relativeDNValue;		
				else if (relativeDNType.equalsIgnoreCase(Constants.COMMON_NAME))
					this.commonName = relativeDNValue;
				
			}
		} catch (InvalidNameException e) {
			logger.error(e);
		}
		
	}
	
	
	public boolean equals (Object obj) {
		
		if ( ! (obj instanceof CertificateAuthorityIssuerBean))
			return false;
		
		
		CertificateAuthorityIssuerBean bean = (CertificateAuthorityIssuerBean)obj;
		 
			
		//Country
		if(  bean.getCountry() == null ) {
			if( this.getCountry() != null ) 
				return false;
		}else {
			if( ! bean.getCountry().equals(this.getCountry()))
				return false;
		}
		
		//Organization
		if(  bean.getOrganization() == null ) {
			if( this.getOrganization() != null ) 
				return false;
		}else {
			if( ! bean.getOrganization().equals(this.getOrganization()))
				return false;
		}	
		
		//Organizational Unit
		if(  bean.getOrganizationalUnit() == null ) {
			if( this.getOrganizationalUnit() != null ) 
				return false;
		}else {
			if( ! bean.getOrganizationalUnit().equals(this.getOrganizationalUnit()))
				return false;
		}		
				
		//Common Name
		if(  bean.getCommonName() == null ) {
			if( this.getCommonName() != null ) 
				return false;
		}else {
			if( ! bean.getCommonName().equals(this.getCommonName()))
				return false;
		}	
		
	
		return true;
	}
	
	
	
	public String toString() {
		return "C="+this.country+" O="+this.organization+" OU="+this.organizationalUnit+" CN="+this.commonName;
	}
	
	
	
	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getOrganizationalUnit() {
		return organizationalUnit;
	}

	public void setOrganizationalUnit(String organizationalUnit) {
		this.organizationalUnit = organizationalUnit;
	}

	public String getCommonName() {
		return commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

//	public static void main(String[] args) {
//		System.out.print("value".equals(null));
//	}
	
	
}
