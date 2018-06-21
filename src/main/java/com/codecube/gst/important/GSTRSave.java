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
public class GSTRSave {
	
	private static final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/returns/gstr1";
	
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
			String hmac) throws ParseException
	{
		String result = "{\"status_cd\":\"0\",\"Error\":\"Please Check Your Headers\"}";
		try {
			URL url = new URL(BASE_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			String requestpayloadr ="{\"action\":\"RETSAVE\",\"data\":\""+encpayload+"\",\"hmac\":\""+hmac+"\"}";
			byte[] out = requestpayloadr.getBytes(StandardCharsets.UTF_8);
			conn.setRequestMethod("PUT");
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
			 conn.setRequestProperty("auth_token", auth_token);
			 conn.setDoInput(true);
			 conn.setDoOutput(true);
			 conn.connect();
			 DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
			 System.out.println("outputstream");
			 wr.write(out);
			 wr.flush();
			 wr.close();
			 BufferedReader in = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						result =response.toString();
						JSONParser parser1 = new JSONParser();
						JSONObject json = (JSONObject) parser1.parse(result);
						String status_cd = (String) json.get("status_cd");
						if (status_cd.equals("1")) {
							String data = (String) json.get("data");
							String rek = (String) json.get("rek");
							@SuppressWarnings("static-access")
							String ref_id = app.decodeData(data, rek, sek, appkey);
							json.put("saveref_id", ref_id);
							return json;
						} else 
						{
							return json;
						}
			
		} catch (Exception e) {
			System.out.println("EXCEPTION OCCURE");
		}
		JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(result);
		return json;
		
		
	}

}
