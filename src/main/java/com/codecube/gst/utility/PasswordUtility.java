package com.codecube.gst.utility;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Repository;

@Repository
public class PasswordUtility {
	
	 public static final String AES = "AES";
	 public static String key="F21FA726B74B6E05F715EDC721AF3B5C";

	    private static String byteArrayToHexString(byte[] b) {
	        StringBuffer sb = new StringBuffer(b.length * 2);
	        for (int i = 0; i < b.length; i++) {
	            int v = b[i] & 0xff;
	            if (v < 16) {
	                sb.append('0');
	            }
	            sb.append(Integer.toHexString(v));
	        }
	        return sb.toString().toUpperCase();
	    }
	    
	    private static byte[] hexStringToByteArray(String s) {
	        byte[] b = new byte[s.length() / 2];
	        for (int i = 0; i < b.length; i++) {
	            int index = i * 2;
	            int v = Integer.parseInt(s.substring(index, index + 2), 16);
	            b[i] = (byte) v;
	        }
	        return b;
	
	    }
	    public static String encryption(String plaintext) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException, IOException
	    {
	    	
	    	byte[] bytekey = hexStringToByteArray(key);
	        SecretKeySpec sks = new SecretKeySpec(bytekey, PasswordUtility.AES);
	        Cipher cipher = Cipher.getInstance(PasswordUtility.AES);
	        cipher.init(Cipher.ENCRYPT_MODE, sks, cipher.getParameters());
	        byte[] encrypted = cipher.doFinal(plaintext.getBytes());
	        String encryptedpwd = byteArrayToHexString(encrypted);
			return encryptedpwd;
	    	
	    }
	    
	    public static String decryption(String encpassword) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
	    {
	    	byte[] bytekey = hexStringToByteArray(key);
	        SecretKeySpec sks = new SecretKeySpec(bytekey, PasswordUtility.AES);
	        Cipher cipher = Cipher.getInstance(PasswordUtility.AES);
	        cipher.init(Cipher.DECRYPT_MODE, sks);
	        byte[] decrypted = cipher.doFinal(hexStringToByteArray(encpassword));
	        String OriginalPassword = new String(decrypted);
			return OriginalPassword;
	    	
	    }

}
