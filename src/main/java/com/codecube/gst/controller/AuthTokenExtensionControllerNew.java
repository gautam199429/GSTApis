package com.codecube.gst.controller;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.utility.AESEncryption;
import com.codecube.gst.utility.EncryptionUtil;

import io.swagger.annotations.Api;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="OTP", description="OTP Generation")
public class AuthTokenExtensionControllerNew {
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	EncryptionUtil enc;
	
	@Autowired
	RedisConfig red;
	
	
	private static final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v0.2/authenticate";

	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/authextension", method= RequestMethod.GET)
	public JSONObject authExtensionRequest(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("UserId") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period)throws Exception
			{
			String result = "{\"status-cd\":\"0\",\"error\":\"ERROR OCCURE\",\"message\":\"Please Check Your Headers or Generate your Appkey\"}";
			 @SuppressWarnings("static-access")
			String auth_token = red.redisGetauthtoken(gstin);
			@SuppressWarnings("static-access")
			String app_key_old = red.redisGetappkey(gstin);
			@SuppressWarnings("static-access")
			String sek = red.redisGetsek(gstin);
			
			 try {
				@SuppressWarnings("resource")
				Jedis jedis = new Jedis("localhost",6379);
				@SuppressWarnings("static-access")
				String appkey_new = app.generateSecureKey();
				@SuppressWarnings("static-access")
				byte[] authEK = app.decrypt(sek, app.decodeBase64StringTOByte(app_key_old));
				@SuppressWarnings("static-access")
				String new_encrypted_app_key = app.encryptEK(app.decodeBase64StringTOByte(appkey_new), authEK);
				URL url = new URL(BASE_URL);
				HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
				String requestpayload = "{\"action\":\"REFRESHTOKEN\",\"app_key\":\""+new_encrypted_app_key+"\",\"username\":\""+username+"\",\"auth_token\":\""+auth_token+"\"}";
				 System.out.println(requestpayload);
				 byte[] out = requestpayload.getBytes(StandardCharsets.UTF_8);
				 conn.setRequestMethod("POST");
				 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
				 conn.setRequestProperty("Asp-Id", asp_id);
				 conn.setRequestProperty("Asp-Secret", asp_secret);
				 conn.setRequestProperty("state-cd", state);
				 conn.setRequestProperty("txn", txn);
				 conn.setRequestProperty("ip-usr", ip_usr);;
				 conn.setRequestProperty("host", host);
				 conn.setRequestProperty("UserId", username);
				 System.out.println();
				 System.out.println("HEAERS connected");
				 conn.setDoInput(true);
				 conn.setDoOutput(true);
				 conn.connect();
				 System.out.println("connected");
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
						java.util.Date date=new java.util.Date();
						String created_at = date.toString();
						json.put("created_at", created_at);
						jedis.set(gstin+"encodedappkey",appkey_new);
						jedis.set(gstin+"encryptedappkey",new_encrypted_app_key);
						jedis.set(gstin+"authresponse", json.toString());
						return json;
					} 
					else 
					{
						jedis.set(gstin+"authresponseError", result);
						return json;
					}
			 } 
			 
			 catch (Exception e) {
				System.err.println(e);
			}
			 	JSONParser parser1 = new JSONParser();
				JSONObject json = (JSONObject) parser1.parse(result);
				return json;
			}
}
