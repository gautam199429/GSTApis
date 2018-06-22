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
public class GSTRSubmit {
	
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
			String txns
			) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		JSONObject submitpayload = new JSONObject();
		submitpayload.put("gstin", gstin);
		submitpayload.put("ret_period", ret_period);
		@SuppressWarnings("static-access")
		String encodedsubmitpayload = app.encodedjson(submitpayload.toString());
		@SuppressWarnings("static-access")
		String encsubmitpayload = app.encryptJson(submitpayload.toString(), sek, appkey);
		@SuppressWarnings("static-access")
		String hmac = app.generatHmacOfPayloda(encodedsubmitpayload, sek, appkey);
		try {
			URL url = new URL(BASE_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			String requestpayloadr ="{\"action\":\"RETSAVE\",\"data\":\""+encsubmitpayload+"\",\"hmac\":\""+hmac+"\"}";
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
							String submitresponse = app.decodeData(data, rek, sek, appkey);
							JSONParser p = new JSONParser();
							JSONObject jsonp = (JSONObject) p.parse(submitresponse);
							String reference_id = (String) jsonp.get("reference_id");
							json.put("reference_id", reference_id);
							return json;
						} else 
						{
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
