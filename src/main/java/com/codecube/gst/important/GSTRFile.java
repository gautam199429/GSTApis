package com.codecube.gst.important;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.codecube.gst.utility.AESEncryption;

@Repository
public class GSTRFile {
	
private static final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/returns/gstr1";
	
	@Autowired
	AESEncryption app;
	
	public JSONObject gstrFile(String sek,
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
			String payload,
			String sign,
			String otp,
			String pan) throws ParseException
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headerssave\"}";
		
		try {
			URL url = new URL(BASE_URL);
			String pan1=""+pan+""+"|"+""+otp+"";
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			String requestpayloadr ="{\"action\":\"RETFILE\",\"data\":\""+payload+"\",\"sign\":\""+sign+"\",\"st\":\"EVC\",\"sid\":\""+pan1+"\"}";
			System.out.println(requestpayloadr);
			System.out.println("Submit Encrypted Apyload"+payload);
			System.out.println("Submit Encrypted Apyload HMAC"+sign);
			byte[] out = requestpayloadr.getBytes(StandardCharsets.UTF_8);
			conn.setRequestMethod("POST");
			 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
			 conn.setRequestProperty("Asp-Id", asp_id);
			 conn.setRequestProperty("Asp-Secret", asp_secret);
			 conn.setRequestProperty("state-cd", state_cd);
			 conn.setRequestProperty("txn", txns);
			 conn.setRequestProperty("ip-usr", ip_usr);;
			 conn.setRequestProperty("host", host);
			 conn.setRequestProperty("username", username);
			 conn.setRequestProperty("GSTIN", gstin);
			 conn.setRequestProperty("ret_period", ret_period);
			 conn.setRequestProperty("auth-token", auth_token);
			 conn.setDoOutput(true);
			 conn.setDoInput(true);
			 DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			 System.out.println("outputstream1");
			 wr.write(out);
			 System.out.println("outputstream2");
			 wr.flush();
			 System.out.println("outputstream3");
			 wr.close();
//			 conn.connect();
			 System.out.println("outputstream4");
			 BufferedReader in = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
							System.out.println("outputstream5");
						}
						in.close();
						result =response.toString();
						JSONParser parser1 = new JSONParser();
						System.out.println("outputstream6");
						JSONObject json = (JSONObject) parser1.parse(result);
						String status_cd = (String) json.get("status_cd");
						if (status_cd.equals("1")) {
							String data = (String) json.get("data");
							String rek = (String) json.get("rek");
							@SuppressWarnings("static-access")
							String decode = app.decodeData(data, rek, sek, appkey);
							System.out.println(decode);
							JSONParser decodeparser = new JSONParser();
							JSONObject decodedjson = (JSONObject) decodeparser.parse(decode);
							return decodedjson;	
						} else {
							return json;
						}
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(result);
		return json;
		
	}

}
