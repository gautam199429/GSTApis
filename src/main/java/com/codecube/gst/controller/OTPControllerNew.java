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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import redis.clients.jedis.Jedis; 
import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.entity.GSTR1Model;
import com.codecube.gst.utility.AESEncryption;
import com.codecube.gst.utility.EncryptionUtil;
import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="OTP", description="OTP Generation Controller")
public class OTPControllerNew {
	
	@Autowired
	RedisConfig red;
	
	
//	@Autowired
//	GSTR1Model gstr;
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	EncryptionUtil encutil;
	
	private static final String BASE_URL = " https://devapi.gst.gov.in/taxpayerapi/v0.2/authenticate";
	
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/otprequest", method= RequestMethod.GET)
	public JSONObject OtpRequest(
			@RequestHeader("clientid") String asp_id,
			@RequestHeader("client-secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("UserId") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period)throws Exception
			{
		String result = "{\"status-cd\":\"0\",\"error\":\"ERROR OCCURE\",\"message\":\"Please Check Your Headers\"}";
		try
		{
			@SuppressWarnings("static-access")
			String appkey = app.generateSecureKey();
			System.out.println(appkey);
			@SuppressWarnings("static-access")
			String encryptedappkey = encutil.generateEncAppkey(app.decodeBase64StringTOByte(appkey));
			System.out.println(encryptedappkey);
			@SuppressWarnings("resource")
			Jedis jedis = new Jedis("localhost",6379);
			jedis.set(gstin+"encodedappkey",appkey);
			jedis.set(gstin+"encryptedappkey",encryptedappkey);
			URL url = new URL(BASE_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			String requestpayloadr ="{\"action\":\"OTPREQUEST\",\"app_key\":\""+encryptedappkey+"\",\"username\":\""+username+"\"}";
			System.out.println(requestpayloadr);
			byte[] out = requestpayloadr.getBytes(StandardCharsets.UTF_8);
			conn.setRequestMethod("POST");
			 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
			 conn.setRequestProperty("clientid", asp_id);
			 conn.setRequestProperty("client-secret", asp_secret);
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
			 int responseCode = conn.getResponseCode();
			 String response1 = conn.getResponseMessage();
			 System.out.println(response1);
			 System.out.println(responseCode);
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
				return json;	
		}
		catch(Exception e)
		{
			System.out.println("EXCEPTION OCCURE");
		}
		JSONParser parser1 = new JSONParser();
		JSONObject json = (JSONObject) parser1.parse(result);
		return json;
		
	}

}
