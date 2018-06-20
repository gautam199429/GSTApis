package com.codecube.gst.utility;

import java.io.FileInputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import org.springframework.stereotype.Repository;

/**
 * This class is used to encrypt the string using a 
 * public key
 * 
 * @date 16th September, 2016
 */
@Repository
public class EncryptionUtil {
	
	/**
	 * GSTN Sandbox UAT Key
	 * @ExpireyDate 16/04/2019
	 * @publicKeyUrl1, This key is provided by the GST Department
	 * Enable the below publicKeyUrl1 for Sandbox
	 */
	public static String publicKeyUrl1 ="D:/software/GSTN_G2A_SANDBOX_UAT_public.cer";
	/**
	 * GSTN Production Key
	 * @ExpireyDate 02/08/2018
	 * @publicKeyUrl1, This key is provided by the GST Department 
	 * Enable the below publicKeyUrl1 for Production
	 */
	//public static String publicKeyUrl1 ="D:/software/GSTN_G2B_Prod_Public.cer";
			@SuppressWarnings("unused")
			private static String file;
	
	private static PublicKey readPublicKey(String filename) throws Exception
	{
		FileInputStream fin = new FileInputStream(filename);
		CertificateFactory f = CertificateFactory.getInstance("X.509");
		X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
		PublicKey pk = certificate.getPublicKey();
		return pk;
	         
	}
	/**
	 * This method is used to encrypt the string , passed to it 
	 * using a public key provided
	 * 
	 * @param planTextToEncrypt
	 *       : Text to encrypt
	 * @return
	 *       :encrypted string 
	 */
	public static String encrypt(byte[] plaintext) throws Exception,NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	{

		PublicKey key = readPublicKey(publicKeyUrl1);
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		cipher.init(Cipher.ENCRYPT_MODE, key);
		byte[] encryptedByte= cipher.doFinal(plaintext);
		String encodedString = new String(java.util.Base64.getEncoder().encode(encryptedByte));
		return encodedString;
	}
	
	/**
	 * This Method is used to generate Appkey
	 * @param key
	 * @return app_key
	 */
	public static String generateEncAppkey(byte[] key){
		try {
			return encrypt(key);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}	
}
