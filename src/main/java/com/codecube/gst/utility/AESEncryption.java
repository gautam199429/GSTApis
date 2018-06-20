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
    /**
     * This Method is used to Generate Enc Payload, Sing, Decoding of Data from REK
     * Generatio of hmac of the payload
     * Generation of Sign data for GSTR1 RETFILE from PAN or OTP
     * Decodeing of data and rek of the RETFILE, RETSTATUS, RETSUBMIT, RETSAVE and others where we get the data and rek in response.
     */
    @SuppressWarnings("unused")
	private static void encJsonAndGenHmac()
    
    {
    	
    	try {
    		/*
    		 * GSTR1 Payload
    		 * GSTR1 RETSAVE Payload
    		 * This is a sample payload
    		 */
    		
    		String jsonpayload="{\"gstin\":\"27BABMH5613A1ZJ\",\"fp\":\"102017\",\"gt\":3782969.01,\"cur_gt\":3782969.01,\"b2b\":[{\"ctin\":\"33TABTN1602C1ZV\",\"inv\":[{\"inum\":\"S18968\",\"idt\":\"04-10-2017\",\"val\":729248.16,\"pos\":\"33\",\"rchrg\":\"N\",\"inv_typ\":\"R\",\"itms\":[{\"num\":1,\"itm_det\":{\"rt\":5,\"txval\":10000,\"iamt\":833.33,\"csamt\":500}}]}]}]} ";
    		/*
    		 * GSTR1 RETSUBMIT Payload
    		 * In this payload we only pass the GSTN Number and Retern Period
    		 */
    		
    		String jsonrets="{\"gstin\":\"27BABMH5613A1ZJ\",\"ret_period\":\"102017\"}";
    		/*
    		 * GSTR1 RETFILE Payload
    		 * This Payload is download from the RETSUM Api
    		 * Decode received data from RETSUM ASI with received REk
    		 * Then you will get the below payload
    		 */
    		
    		
			String jsonpayloadr="{\"gstin\":\"27BABMH5613A1ZJ\",\"ret_period\":\"072017\",\"chksum\":\"79e10c5b91105bcf8be38caa3bfd701f5a3324f57fa1a3e6c675285f4c6c827d\",\"sec_sum\":[{\"sec_nm\":\"CDNUR\",\"chksum\":\"74313561d1897af3dc03f4fae174960d28968f92b49230523faca462b848db60\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"EXPA\",\"chksum\":\"3b7546ed79e3e5a7907381b093c5a182cbf364c5dd0443dfa956c8cca271cc33\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"DOC_ISSUE\",\"chksum\":\"\",\"ttl_rec\":0,\"ttl_doc_issued\":0,\"ttl_doc_cancelled\":0,\"net_doc_issued\":0},{\"sec_nm\":\"TXPDA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"HSN\",\"chksum\":\"\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"EXP\",\"chksum\":\"3b7546ed79e3e5a7907381b093c5a182cbf364c5dd0443dfa956c8cca271cc33\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"CDNURA\",\"chksum\":\"74313561d1897af3dc03f4fae174960d28968f92b49230523faca462b848db60\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"NIL\",\"chksum\":\"\",\"ttl_rec\":0,\"ttl_expt_amt\":0.0,\"ttl_ngsup_amt\":0.0,\"ttl_nilsup_amt\":0.0},{\"sec_nm\":\"CDNRA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"CDNR\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2CL\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2CSA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2CS\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"AT\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"ATA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"TXPD\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2BA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2CLA\",\"chksum\":\"e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855\",\"ttl_rec\":0,\"ttl_tax\":0.0,\"ttl_igst\":0.0,\"ttl_val\":0.0,\"ttl_cess\":0.0},{\"sec_nm\":\"B2B\",\"chksum\":\"9f92b01939b9f3321a5fedd9bd79ac8f4effbc90bbadf0615c0340a93bd0b39a\",\"ttl_rec\":1,\"ttl_tax\":10000.0,\"ttl_igst\":833.33,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":729248.16,\"ttl_cess\":500.0,\"cpty_sum\":[{\"ctin\":\"33TABTN1602C1ZV\",\"chksum\":\"70a6b5d07ebf6b5f6c14d9d87b0054202a048896ed4eee8d8ce28e5a49873f7e\",\"ttl_rec\":1,\"ttl_tax\":10000.0,\"ttl_igst\":833.33,\"ttl_sgst\":0.0,\"ttl_cgst\":0.0,\"ttl_val\":729248.16,\"ttl_cess\":500.0}]}]}";
    		/*
    		 * GSTR3B payload
    		 * This is a sample payload.
    		 */
    		
			String gstrb3="{\"gstin\": \"27BCAMH0498C1Z3\",\"ret_period\": \"072017\",\"sup_details\": {\"osup_det\": {\"txval\": 2000,\"iamt\": 360,\"camt\": 0,\"samt\":0,\"csamt\":0},\"osup_zero\": {\"txval\": 2000,\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},\"osup_nil_exmp\": {\"txval\": 0,\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},\"isup_rev\": {\"txval\": 0,\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},\"osup_nongst\": {\"txval\": 0,\"iamt\":0,\"camt\": 0,\"samt\": 0,\"csamt\": 0}},\"inter_sup\": {\"unreg_details\": [{\"pos\": \"35\",\"txval\": 100,\"iamt\": 90}],\"comp_details\":[{\"pos\": \"35\",\"txval\": 100,\"iamt\": 90}],\"uin_details\": [{\"pos\": \"35\",\"txval\": 100,\"iamt\": 90}]},\"itc_elg\": {\"itc_avl\": [\r\n" + 
    				"{\"ty\": \"IMPG\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"IMPS\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"ISRC\",\"iamt\":0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"ISD\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"OTH\",\"iamt\": 200,\"camt\": 100,\"samt\": 60,\"csamt\": 0}],\"itc_rev\": [{\"ty\": \"RUL\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"OTH\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0}],\"itc_net\": {\"iamt\": 200,\"camt\": 100,\"samt\": 60,\"csamt\": 0},\"itc_inelg\": [{\"ty\": \"RUL\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0},{\"ty\": \"OTH\",\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0}]},\"inward_sup\": \"isup_details\": [\r\n" + 
    				"{\"ty\": \"GST\",\"inter\": 0,\"intra\": 0},{\"ty\": \"NONGST\",\"inter\": 0,\"intra\": 0}]}\"intr_ltfee\": {\"intr_details\": {\"iamt\": 0,\"camt\": 0,\"samt\": 0,\"csamt\": 0}},\"tx_pmt\": {\"tx_py\": [{\"tran_desc\": \"Other than Reverse Charge\",\"liab_ldg_id\": 1530,\"trans_typ\": 30002,\"igst\": {\"tx\": 0,\"intr\": 0,\"fee\": 0},\"cgst\": {\"tx\": 0,\"intr\": 0,\"fee\": 0},\"sgst\": {\"tx\": 0,\"intr\": 0,\"fee\": 0},\"cess\": {\"tx\": 0,\"intr\": 0,\"fee\": 0}},{\"tran_desc\": \"Reverse Charge\",\"liab_ldg_id\": 1531,\"trans_typ\": 30003,\"igst\": {\"tx\": 0,\"intr\": 0,\"fee\": 0},\"cgst\": {\"tx\": 0,\"intr\": 0,\"fee\": 0},\"sgst\": {\"intr\": 0,\"fee\": 0},\"cess\": {\"tx\": 0,\"intr\": 0,\"fee\": }}],\"pdcash\": [],\"pditc\":{\"liab_ldg_id\":1530,\"trans_typ\": 30002,\"i_pdi\": 200,\"i_pdc\": 100,\"i_pds\": 60,\"c_pdi\": 0,\"c_pdc\": 0,\"s_pdi\": 0,\"s_pds\": 0,\"cs_pdcs\": 0}}}";
    	
    		String gstr3b = "{\r\n" + 
    				"    \"gstin\": \"27BCAMH0498C1Z3\",\r\n" + 
    				"    \"ret_period\": \"08017\",\r\n" + 
    				"    \"sup_details\": {\r\n" + 
    				"        \"osup_det\": {\r\n" + 
    				"            \"txval\": 1050908,\r\n" + 
    				"            \"iamt\": 112312,\r\n" + 
    				"            \"camt\": 20016,\r\n" + 
    				"            \"samt\": 20016,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"osup_zero\": {\r\n" + 
    				"            \"txval\": 0,\r\n" + 
    				"            \"iamt\": 0,\r\n" + 
    				"            \"camt\": 0,\r\n" + 
    				"            \"samt\": 0,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"osup_nil_exmp\": {\r\n" + 
    				"            \"txval\": 24580,\r\n" + 
    				"            \"iamt\": 0,\r\n" + 
    				"            \"camt\": 0,\r\n" + 
    				"            \"samt\": 0,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"isup_rev\": {\r\n" + 
    				"            \"txval\": 248575,\r\n" + 
    				"            \"iamt\": 550,\r\n" + 
    				"            \"camt\": 5939,\r\n" + 
    				"            \"samt\": 5939,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"osup_nongst\": {\r\n" + 
    				"            \"txval\": 0,\r\n" + 
    				"            \"iamt\": 0,\r\n" + 
    				"            \"camt\": 0,\r\n" + 
    				"            \"samt\": 0,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        }\r\n" + 
    				"    },\r\n" + 
    				"    \"itc_elg\": {\r\n" + 
    				"        \"itc_avl\": [\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"IMPG\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"IMPS\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"ISRC\",\r\n" + 
    				"                \"iamt\": 550,\r\n" + 
    				"                \"camt\": 5939,\r\n" + 
    				"                \"samt\": 5939,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"ISD\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"OTH\",\r\n" + 
    				"                \"iamt\": 1807,\r\n" + 
    				"                \"camt\": 43361,\r\n" + 
    				"                \"samt\": 43361,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            }\r\n" + 
    				"        ],\r\n" + 
    				"        \"itc_rev\": [\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"RUL\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"OTH\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 68,\r\n" + 
    				"                \"samt\": 68,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            }\r\n" + 
    				"        ],\r\n" + 
    				"        \"itc_net\": {\r\n" + 
    				"            \"iamt\": 2357,\r\n" + 
    				"            \"camt\": 49232,\r\n" + 
    				"            \"samt\": 49232,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"itc_inelg\": [\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"RUL\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"OTH\",\r\n" + 
    				"                \"iamt\": 0,\r\n" + 
    				"                \"camt\": 0,\r\n" + 
    				"                \"samt\": 0,\r\n" + 
    				"                \"csamt\": 0\r\n" + 
    				"            }\r\n" + 
    				"        ]\r\n" + 
    				"    },\r\n" + 
    				"    \"inward_sup\": {\r\n" + 
    				"        \"isup_details\": [\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"GST\",\r\n" + 
    				"                \"inter\": 0,\r\n" + 
    				"                \"intra\": 90399\r\n" + 
    				"            },\r\n" + 
    				"            {\r\n" + 
    				"                \"ty\": \"NONGST\",\r\n" + 
    				"                \"inter\": 1200,\r\n" + 
    				"                \"intra\": 0\r\n" + 
    				"            }\r\n" + 
    				"        ]\r\n" + 
    				"    },\r\n" + 
    				"    \"intr_ltfee\": {\r\n" + 
    				"        \"intr_details\": {\r\n" + 
    				"            \"iamt\": 0,\r\n" + 
    				"            \"camt\": 0,\r\n" + 
    				"            \"samt\": 0,\r\n" + 
    				"            \"csamt\": 0\r\n" + 
    				"        },\r\n" + 
    				"        \"ltfee_details\": {}\r\n" + 
    				"    },\r\n" + 
    				"    \"inter_sup\": {\r\n" + 
    				"        \"unreg_details\": [],\r\n" + 
    				"        \"comp_details\": [],\r\n" + 
    				"        \"uin_details\": []\r\n" + 
    				"    }\r\n" + 
    				"}";
    		
    		String gstr3bsum = "{\"gstin\":\"27BCAMH0498C1Z3\",\"ret_period\":\"072017\",\"sup_details\":{\"osup_det\":{\"txval\":1050908.0,\"iamt\":112312.0,\"camt\":20016.0,\"samt\":20016.0,\"csamt\":0.0},\"osup_zero\":{\"txval\":0.0,\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},\"osup_nil_exmp\":{\"txval\":24580.0,\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},\"isup_rev\":{\"txval\":248575.0,\"iamt\":550.0,\"camt\":5939.0,\"samt\":5939.0,\"csamt\":0.0},\"osup_nongst\":{\"txval\":0.0,\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0}},\"inter_sup\":{\"unreg_details\":[],\"comp_details\":[],\"uin_details\":[]},\"itc_elg\":{\"itc_avl\":[{\"ty\":\"IMPG\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},{\"ty\":\"IMPS\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},{\"ty\":\"ISRC\",\"iamt\":550.0,\"camt\":5939.0,\"samt\":5939.0,\"csamt\":0.0},{\"ty\":\"ISD\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},{\"ty\":\"OTH\",\"iamt\":1807.0,\"camt\":43361.0,\"samt\":43361.0,\"csamt\":0.0}],\"itc_rev\":[{\"ty\":\"RUL\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},{\"ty\":\"OTH\",\"iamt\":0.0,\"camt\":68.0,\"samt\":68.0,\"csamt\":0.0}],\"itc_net\":{\"iamt\":2357.0,\"camt\":49232.0,\"samt\":49232.0,\"csamt\":0.0},\"itc_inelg\":[{\"ty\":\"RUL\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},{\"ty\":\"OTH\",\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0}]},\"inward_sup\":{\"isup_details\":[{\"ty\":\"GST\",\"inter\":0.0,\"intra\":90399.0},{\"ty\":\"NONGST\",\"inter\":1200.0,\"intra\":0.0}]},\"intr_ltfee\":{\"intr_details\":{\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0},\"ltfee_details\":{\"iamt\":0.0,\"camt\":0.0,\"samt\":0.0,\"csamt\":0.0}},\"tx_pmt\":{\"tx_py\":[{\"tran_desc\":\"Other than Reverse Charge\",\"liab_ldg_id\":0,\"trans_typ\":30002,\"igst\":{\"tx\":112312.0,\"intr\":0.0,\"fee\":0.0},\"cgst\":{\"tx\":20016.0,\"intr\":0.0,\"fee\":0.0},\"sgst\":{\"tx\":20016.0,\"intr\":0.0,\"fee\":0.0},\"cess\":{\"tx\":0.0,\"intr\":0.0,\"fee\":0.0}},{\"tran_desc\":\"Reverse Charge\",\"liab_ldg_id\":0,\"trans_typ\":30003,\"igst\":{\"tx\":550.0,\"intr\":0.0,\"fee\":0.0},\"cgst\":{\"tx\":5939.0,\"intr\":0.0,\"fee\":0.0},\"sgst\":{\"tx\":5939.0,\"intr\":0.0,\"fee\":0.0},\"cess\":{\"tx\":0.0,\"intr\":0.0,\"fee\":0.0}}]}}";
    		String panh = "AHWPB0871H";
    		/*
    		 * Encoded Json
    		 */
    		String encodedJson = AESEncryption.encodeBase64String(jsonrets.toString().getBytes());
    		System.out.println(encodedJson);
    		byte[] asBytes = Base64.decodeBase64(encodedJson);
    		System.out.println("NEW STRING"+new String(asBytes, "utf-8"));
    		/*
    		 * Sek is the Session Key which is generated by AUTHTOKEN API 
    		 */
    		String sek="IitOPD+s6MRQtBmmzRMYvAGT7XA/U+ykDwTEhfF6AstlAT3Ti4NDH8dpsE6sildD";
    		byte[] authEK = AESEncryption.decrypt(sek, decodeBase64StringTOByte("Q/XkhYUUbvfg6wCdxrPzPqMb4SPMrYRXQ5w2WVnxw90="));
    		String encryptedJsonData = AESEncryption.encryptEK(encodedJson.getBytes(), authEK);
    		/*
    		 * Encrypted Json
    		 */
    		System.out.println("Encrypted json--->"+encryptedJsonData);
    		String hmackey = AESEncryption.generateHmac(encodedJson, authEK);
    		System.out.println("hmac--->"+hmackey);
    		/*
    		 * REK and DATA is used to decrypt the information provide by the GSTN in Encrypted form
    		 * This is used in RETSTATUS(Status information), RETSUM(Json summary), RETSUBMIT(Ref-id), RETSAVE(Ref-id)
    		 */
    		String gotREK="Jtw85O5bL3eaYLfnhNBFuIvWRibX/5zmbpOW9ch08QGMpFBFDxLo5x4HtRXrf1BR";
    		String data="gvw+17329dm9VYUdpUSMkhdrhf5kYe1rTJHQ+Cpwm3avjZraBHSU2OsrPMWyPS0q4HpeT06pT7cwnibOeB9QID2s3FuKJPFZM1fFbKrPdJ8=";
    		byte[] apiEK = decrypt(gotREK, authEK);
			System.out.println("Encoded Api EK (Received):"+ encodeBase64String(apiEK));
			String refid = new String(decodeBase64StringTOByte(new String(decrypt(data, apiEK))));
			System.out.println(refid);
			String pan1="AHWPB0871H"+"|"+"9925F4";
			//String pan1="AHWPB0871H";
			System.out.println(pan1);
			String pan = AESEncryption.encodeBase64String(pan1.toString().getBytes());
			String sign = generateHmac(encodedJson, AESEncryption.decodeBase64StringTOByte(pan));
			String sign1 = generateHmac(pan, authEK);
//			 String Sign = hmac(base64encodedstring (summarypayoad), decodeString2Byte(base64encodedstring(PAN|OTP)));
			System.out.println("Sign:---->"+sign1);
    		}
    	catch (Exception e) {
			System.out.println("Someting is going wrong");
		}
    }  
    /**
     * Genertion of Encrypted AppKey
     * Encryption of OTP with Encoded Appkey
     */
//    @SuppressWarnings("unused")
//	private static void produceSampleData(){
//    	
//    	try {
//			/*
//			 * Generation of app key. this will be in encoded.
//			 * Store this for the use to decrypt the SEK and generate the AUTHEK
//			 */
//			String appkey = generateSecureKey();
//			System.out.println("**********************ENCODED APP KEY**************************");
//			System.out.println("App key in encoded : "+ appkey);
//			System.out.println("********************************************************************");
//			/*
//			 * Encrypt with GSTN public key
//			 */
//			String encryptedAppkey = EncryptionUtil.generateEncAppkey(decodeBase64StringTOByte(appkey));
//			System.out.println("**********************ENCRYPTED APP KEY************************");
//			System.out.println("Encrypted App Key ->"+ encryptedAppkey);
//			System.out.println("*********************************************************************");				
//			/*
//			 * Received OTP in registered number
//			 */
//			String otp = "658186";
//			String encryptedOtp = encryptEK(otp.getBytes(),decodeBase64StringTOByte("Q/XkhYUUbvfg6wCdxrPzPqMb4SPMrYRXQ5w2WVnxw90="));
//			/*
//			 * Encrypted OTP sent to AUTHTOKEN API
//			 */
//			System.out.println("**********************ENCRYPTED OTP*****************************");
//			System.out.println("OTP :"+encryptedOtp);
//			System.out.println("*********************************************************************");
//	
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
    /**
     * Main
     * @param args
     * @throws Exception
//     */
//	public static void main(String args[])throws Exception{
//		//produceSampleData();
//		encJsonAndGenHmac();
//		
//	}
}