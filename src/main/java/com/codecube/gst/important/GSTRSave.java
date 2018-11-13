package com.codecube.gst.important;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.codecube.gst.utility.AESEncryption;

@Repository
public class GSTRSave {
	
	private static final String BASE_URL = "https://devapi.gst.gov.in/taxpayerapi/v0.3/returns/gstr1";
	
	@Autowired
	AESEncryption app;
	
	@SuppressWarnings("unchecked")
	public JSONObject gstrSave(
			String sek,
			String auth_token,
			String appkey,
			String asp_id,
			String asp_secret,
			String username,
			String ip_usr,
			String host,
			String gstin,
			String ret_period,
			String state_cd,
			String txns,
			String encpayload,
			String hmac) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, Exception
	{
		String result = "{\"status_cd\":\"0\",\"Error\":\"Please Check Your Headers\"}";
		HttpsURLConnection conn = null;
		DataOutputStream dataOutputStream = null;
		try {
			URL url = new URL(BASE_URL);
			conn = (HttpsURLConnection) url.openConnection();
			String requestpayloadr ="{\"action\":\"RETSAVE\",\"data\":\""+encpayload+"\",\"hmac\":\""+hmac+"\"}";
			System.out.println(requestpayloadr);
			System.out.println("Save Encrypted Apyload"+encpayload);
			System.out.println("Save Encrypted Apyload HMAC"+hmac);
			byte[] out = requestpayloadr.getBytes(StandardCharsets.UTF_8);
			conn.setRequestMethod("PUT");
			 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
			 conn.setRequestProperty("clientid", asp_id);
			 conn.setRequestProperty("client-secret", asp_secret);
			 conn.setRequestProperty("state-cd", state_cd);
			 conn.setRequestProperty("txn", txns);
			 conn.setRequestProperty("ip-usr", ip_usr);;
			 conn.setRequestProperty("host", host);
			 conn.setRequestProperty("username", username);
			 conn.setRequestProperty("GSTIN", gstin);
			 conn.setRequestProperty("ret_period", ret_period);
			 conn.setRequestProperty("auth-token", auth_token);
			 System.out.println(auth_token);
			 conn.setDoOutput(true);
			 conn.setDoInput(true);			 
			 dataOutputStream = new DataOutputStream(conn.getOutputStream());
			 dataOutputStream.write(out);
			 dataOutputStream.flush();
	         dataOutputStream.close();
	         conn.connect();		 
//			 DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
//			 System.out.println("outputstream");
//			 wr.writeBytes(requestpayloadr);
//			 System.out.println("1");
//			 wr.flush();
//			 System.out.println("2");
//			 wr.close();
//			 conn.getInputStream();
//			 conn.connect();			
//			 OutputStreamWriter wr = new OutputStreamWriter(
//					    conn.getOutputStream());
//					wr.write(requestpayloadr);
//					wr.close();
//					conn.getInputStream();			 
//			 OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
//			 System.out.println("1");
//		        osw.write(requestpayloadr);
//		        System.out.println("1");
//		        osw.flush();
//		        System.out.println("1");
//		        osw.close();
//		        System.out.println("1");
//		        conn.getInputStream();
//		        System.out.println("1");
//		        conn.connect();
//		        System.out.println("1");
//		        System.err.println(conn.getResponseCode());			 
			 BufferedReader in = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						 System.out.println("3");
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
							 System.out.println("3");
						}
						in.close();
						result =response.toString();
						 System.out.println("4");
						JSONParser parser1 = new JSONParser();
						JSONObject json = (JSONObject) parser1.parse(result);
						String status_cd = (String) json.get("status_cd");
						if (status_cd.equals("1")) {
							String data = (String) json.get("data");
							String rek = (String) json.get("rek");
							@SuppressWarnings("static-access")
							String saveresponse = app.decodeData(data, rek, sek, appkey);
							System.out.println(saveresponse);
							JSONParser p = new JSONParser();
							JSONObject jsonp = (JSONObject) p.parse(saveresponse);
							String reference_id = (String) jsonp.get("reference_id");
							System.out.println(reference_id);
							json.put("reference_id", reference_id);
							return json;
						} else 
						{
							return json;
						}
			
		} catch (IOException exception) {
			 exception.printStackTrace();
			 //logger.error(e.getMessage());
		}
		JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(result);
		return json;
		
		
	}

}
