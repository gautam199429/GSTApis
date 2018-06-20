package com.codecube.gst.utility;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Repository;

@Repository
public class Crypto {
    Cipher ecipher;
    Cipher dcipher;
    public static final String workingKey = "FDF58BADA82F35351EBC8539A6BE71EB";
   
    /**
    * Input a string that will be md5 hashed to create the key.
    * @return void, cipher initialized
    */

    public Crypto(){
        try{
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            kgen.init(128);
            this.setupCrypto(kgen.generateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public Crypto(String key){
        SecretKeySpec skey = new SecretKeySpec(getMD5(key), "AES");
        this.setupCrypto(skey);
    }

    private void setupCrypto(SecretKey key){
        // Create an 8-byte initialization vector
        byte[] iv = new byte[]
        {
            0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f
        };

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);
        try
        {
            ecipher = Cipher.getInstance("AES/CBC/NoPadding");
            dcipher = Cipher.getInstance("AES/CBC/NoPadding");

            // CBC requires an initialization vector
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    // Buffer used to transport the bytes from one stream to another
    byte[] buf = new byte[1024];

    public void encrypt(InputStream in, OutputStream out){
        try {
            // Bytes written to out will be encrypted
            out = new CipherOutputStream(out, ecipher);

            // Read in the cleartext bytes and write to out to encrypt
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0){
                out.write(buf, 0, numRead);
            }
            out.close();
        }
        catch (java.io.IOException e){
            e.printStackTrace();
        }
    }

    /**
    * Input is a string to encrypt.
    * @return a Hex string of the byte array
    */
    public String encrypt(String plaintext){
        try{
            byte[] ciphertext = ecipher.doFinal(plaintext.getBytes("UTF-8"));
            return Crypto.byteToHex(ciphertext);
        } catch (Exception e){
             e.printStackTrace();
            return null;
        }

    }

    public void decrypt(InputStream in, OutputStream out){
        try {
            // Bytes read from in will be decrypted
            in = new CipherInputStream(in, dcipher);

            // Read in the decrypted bytes and write the cleartext to out
            int numRead = 0;
            while ((numRead = in.read(buf)) >= 0) {
                out.write(buf, 0, numRead);
            }
            out.close();
        } catch (java.io.IOException e) {
             e.printStackTrace();
        }
    }

    /**
    * Input encrypted String represented in HEX
    * @return a string decrypted in plain text
    */
    public String decrypt(String hexCipherText){
        try{
            String plaintext = new String(dcipher.doFinal(Crypto.hexToByte(hexCipherText)), "UTF-8");
            return  plaintext;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(byte[] ciphertext){
        try{
            String plaintext = new String(dcipher.doFinal(ciphertext), "UTF-8");
            return  plaintext;
        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] getMD5(String input){
        try{
            byte[] bytesOfMessage = input.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(bytesOfMessage);
        }  catch (Exception e){
             return null;
        }
    }

    static final String HEXES = "0123456789ABCDEF";

    public static String byteToHex( byte [] raw ) {
        if ( raw == null ) {
          return null;
        }
		String result = "";
		for (int i=0; i < raw.length; i++) {
			result +=
			Integer.toString( ( raw[i] & 0xff ) + 0x100, 16).substring( 1 );
		}
		return result;
	}

    public static byte[] hexToByte( String hexString){
        int len = hexString.length();
        byte[] ba = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            ba[i/2] = (byte) ((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i+1), 16));
        }
        return ba;
    }


    @SuppressWarnings("unused")
	public static void main(String args[]){
		String result=null;
		String err=null;
		String key = null;
		String data = null;
		String action = null;

		if(args==null || args.length<3) 
			err="error: missing one or more arguments. Usage: AesCryptUtil key data <enc|dec>";
		else {
			key = args[0];
			data = args[1];
			action = args[2];

			if(key==null)
				err="error: no key";
			else
			if(key.length()<32)
				err="error: key length less than 32 bytes";
			else
			if(data==null || action==null)
				err="error: no data";
			else
			if(action==null)
				err="error: no action";
			else
			if(!action.equals("enc") && !action.equals("dec"))
				err="error: invalid action";
		}

		if(err==null) {
			try {
				Crypto encrypter = new Crypto(key);

				if(action.equals("enc"))
					result = encrypter.encrypt(data);
				else
					result = encrypter.decrypt(data);
			}
			catch (Exception e) {
				err="error : Exception in performing the requested operation : " + e;
			}
		}
    }
}
