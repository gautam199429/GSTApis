package com.codecube.gst.important;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.codecube.gst.utility.AESEncryption;

@Repository
public class GSTRCheckStatus {
	
	
	@Autowired
	AESEncryption app;
	
	public JSONObject gstCheckStatus(
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
			String ref_id) throws ParseException
	{
		
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		try {
			URL url = new URL("https://apiuat.spicegsp.com/taxpayerapi/v1.0/returns?gstin="+gstin+"&ret_period="+ret_period+"&action=RETSTATUS&"+ref_id+"");
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
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
							String decode = app.decodeData(data, rek, sek, appkey);
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
