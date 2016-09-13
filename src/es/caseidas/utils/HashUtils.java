package es.caseidas.utils;

import org.apache.log4j.Logger;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashUtils {
	
	static Logger logger = Logger.getLogger(HashUtils.class);
	
	public static String HASH_TYPE_SHA_512 = "SHA-512";
	
	public static String HASH_TYPE_SHA_256 = "SHA-256";
	
	
	public static void main(String[] args) {	
		  String hash = null;
		  try {
			  hash = HashUtils.getHash("C:/JLM/caseidas.cafile/application_cafile.pem", HashUtils.HASH_TYPE_SHA_512);
		  }catch(Exception e) {
			  logger.error(e);
		  }
		  logger.info("Obtained hash=" + hash);
	}
	
	
	public static String getHash( String filePath, String hashType  ) throws Exception {
				
		MessageDigest md = MessageDigest.getInstance(hashType);
        FileInputStream fis = new FileInputStream(filePath);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        }
        
        byte[] mdbytes = md.digest();

        //convert the byte to hex format method 1
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }

        logger.info("Hex format : " + sb.toString());
		return sb.toString();
	}
	
	
	public static String getHashByteToHex( String filePath, String hashType  ) throws Exception {
		
		MessageDigest md = MessageDigest.getInstance(hashType);
        FileInputStream fis = new FileInputStream(filePath);

        byte[] dataBytes = new byte[1024];

        int nread = 0;
        while ((nread = fis.read(dataBytes)) != -1) {
          md.update(dataBytes, 0, nread);
        }
        
        byte[] mdbytes = md.digest(); 

       //convert the byte to hex format method 2
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<mdbytes.length;i++) {
    	  hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
    	} 	
    	logger.info("Hex format : " + hexString.toString());	
		
		return hexString.toString();
	}
	
	

}
