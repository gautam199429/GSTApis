package com.codecube.gst.utility;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.stereotype.Repository;

@Repository
public class PasswordEncryptionDescryption {

	 private static final String ALGO = "AES/CBC/NoPadding";
	  private static final byte[] keyValue =
	            new byte[]{'1','2','8','D','3','5','8','E','F','8','2','0','C','4','D','F','2','5','A','C','E','D','B','4','B','4','C','6','4','E','8','3'};

	    /**
	     * Encrypt a string with AES algorithm.
	     *
	     * @param data is a string
	     * @return the encrypted string
	     */
	  public static String encrypt(String data) throws Exception {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.ENCRYPT_MODE, key);
	        byte[] encVal = c.doFinal(data.getBytes());
	        return Base64.getEncoder().encodeToString(encVal);
	    }

	    /**
	     * Decrypt a string with AES algorithm.
	     *
	     * @param encryptedData is a string
	     * @return the decrypted string
	     */
	    public static String decrypt(String encryptedData) throws Exception {
	        Key key = generateKey();
	        Cipher c = Cipher.getInstance(ALGO);
	        c.init(Cipher.DECRYPT_MODE, key);
	        byte[] decordedValue = Base64.getDecoder().decode(encryptedData);
	        byte[] decValue = c.doFinal(decordedValue);
	        return new String(decValue);
	    }

	    /**
	     * Generate a new encryption key.
	     */
	    private static Key generateKey() throws Exception {
	        return new SecretKeySpec(keyValue, ALGO);
	    }
	    public static void main(String[] args) throws Exception {
			String plaintext = "123";
			System.out.println("PLAIN TEXT:=  "+plaintext);
			String enc = encrypt(plaintext);
			System.out.println("Encrypted text:= "+enc);
			String dec = decrypt(enc);
			System.out.println("Descrypted text:-"+dec);
			
		}
	 	
}
