package com.codecube.gst.utility;

import org.springframework.stereotype.Repository;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

@Repository
public class AESEncryption {
	
	public static final String AES_TRANSFORMATION = "AES/ECB/PKCS5Padding";
	public static final String AES_ALGORITHM = "AES";
	public static final int ENC_BITS = 256;
	public static final String CHARACTER_ENCODING = "UTF-8";
	
	private static Cipher ENCRYPT_CIPHER;
	private static Cipher DECRYPT_CIPHER;
	private static KeyGenerator KEYGEN;
	static{
		try{
			ENCRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			DECRYPT_CIPHER = Cipher.getInstance(AES_TRANSFORMATION);
			KEYGEN = KeyGenerator.getInstance(AES_ALGORITHM);
			KEYGEN.init(ENC_BITS);
		}catch(NoSuchAlgorithmException | NoSuchPaddingException e) {
			e.printStackTrace();
		}
	}

	/**
     * This method is used to encode bytes[] to base64 string.
     * 
     * @param bytes
     *            : Bytes to encode
     * @return : Encoded Base64 String
     */
	//private
    public static String encodeBase64String(byte[] bytes) {
         return new String(java.util.Base64.getEncoder().encode(bytes));
   }
   /**
    * This method is used to decode the base64 encoded string to byte[]
    * 
    * @param stringData
    *            : String to decode
    * @return : decoded String
    * @throws UnsupportedEncodingException
    */
   //private
    public static byte[] decodeBase64StringTOByte(String stringData) throws Exception {
		return java.util.Base64.getDecoder().decode(stringData.getBytes(CHARACTER_ENCODING));
	}
   
   /**
    * This method is used to generate the base64 encoded secure AES 256 key     * 
    * @return : base64 encoded secure Key
    * @throws NoSuchAlgorithmException
    * @throws IOException
    * @author gautam kumar sah
    * @return encoded String
    */
   //private
	 public static String generateSecureKey() throws Exception{
		SecretKey secretKey = KEYGEN.generateKey();
		return encodeBase64String(secretKey.getEncoded());
	}
    /**
    * This method is used to encrypt the string which is passed to it as byte[] and return base64 encoded
    * encrypted String
     * @param plainText
    *            : byte[]
    * @param secret
    *            : Key using for encrypt
    * @return : base64 encoded of encrypted string.
    * 
    */
	 //private
	 public static String encryptEK(byte[] plainText, byte[] secret){
		try{
			
			SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
			ENCRYPT_CIPHER.init(Cipher.ENCRYPT_MODE, sk);
			return Base64.encodeBase64String(ENCRYPT_CIPHER
                     .doFinal(plainText));
			
		}catch(Exception e){
			e.printStackTrace();
			return "";
		}
	}
	/**
    * This method is used to decrypt base64 encoded string using an AES 256 bit key.
    * 
     * @param plainText
    *            : plain text to decrypt
    * @param secret
    *            : key to decrypt
    * @return : Decrypted String
    * @throws IOException
    * @throws InvalidKeyException
    * @throws BadPaddingException
    * @throws IllegalBlockSizeException
    */
    public static byte[] decrypt(String plainText, byte[] secret)
                throws InvalidKeyException, IOException, IllegalBlockSizeException,
                BadPaddingException,Exception {
		SecretKeySpec sk = new SecretKeySpec(secret, AES_ALGORITHM);
		DECRYPT_CIPHER.init(Cipher.DECRYPT_MODE, sk);		
          return DECRYPT_CIPHER.doFinal(Base64.decodeBase64(plainText));
    }  
    
    /**
     * This method is used to generate HMAC/hash
     * @param encoded payload
     * @param authEk
     * @return hash(HMAC)
     */
    public static String generateHmac(String data, byte[] ek)
    {
    	String hash = null;
    	try{ 
	    	Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
	        SecretKeySpec secret_key = new SecretKeySpec(ek, "HmacSHA256");
	        sha256_HMAC.init(secret_key);
	        hash = Base64.encodeBase64String(sha256_HMAC.doFinal(data.getBytes()));
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return hash;
    }   
    public static String decodeData(String data, String rek, String sek, String appkey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
    
    {
    	byte[] authEk = AESEncryption.authEk(sek, appkey);
    	byte[] apiEK = decrypt(rek, authEk);
    	String decodedata = new String(decodeBase64StringTOByte(new String(decrypt(data, apiEK))));
		return decodedata;
    	
    }
    
    public static String encryptJson(String payload, String sek, String appkey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
    {
    	String encryptedJsonData = AESEncryption.encryptEK(AESEncryption.encodedjson(payload).getBytes(), AESEncryption.authEk(sek, appkey));
		return encryptedJsonData;
    	
    }
    
    public static byte[] authEk(String sek, String appkey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
    {
        byte[] authEK = AESEncryption.decrypt(sek, decodeBase64StringTOByte(appkey)); 
		return authEK;
    	
    }
    
    public static String encodedjson(String payload)
    {
    	String encodedJson = AESEncryption.encodeBase64String(payload.toString().getBytes());
		return encodedJson;
    	
    }
    
    public static String generatHmacOfPayloda(String payload, String sek, String appkey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
    {
    	String hmackey = AESEncryption.generateHmac(AESEncryption.encodedjson(payload), AESEncryption.authEk(sek, appkey));
		return hmackey;
    	
    }
    
    public static String generateSign(String otp, String pan,String sek, String appkey) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
    {
    	String pan_number=""+pan+""+"|"+""+otp+"";
    	String encodedpan = AESEncryption.encodeBase64String(pan_number.toString().getBytes());
    	String sign1 = generateHmac(encodedpan, AESEncryption.authEk(sek, appkey));
		return sign1;
    	
    }
}