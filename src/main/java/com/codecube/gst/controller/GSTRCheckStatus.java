package com.codecube.gst.controller;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.utility.AESEncryption;
import com.codecube.gst.utility.EncryptionUtil;
import redis.clients.jedis.Jedis;
@Repository
public class GSTRCheckStatus {
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	EncryptionUtil enc;
	
	@Autowired
	RedisConfig red;
	
	
	Jedis jedis = new Jedis("localhost",6379);
	
	public JSONObject gstrCheckStatus(
			String aspid, 
			String asp_secret,
			String ip_usr,
			String txn,
			String host,
			String gstn,
			String ret_period,
			String username,
			String ref_no,
			String state_cd) throws ParseException
	{
		String result = "{\"status-cd\":\"0\",\"Error\":\"Error Occure\",\"Message\":\"Error Occure while checking GSTR Status\"}";
		String auth_token = red.redisGetauthtoken(gstn);
		final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/returns?gstn="+gstn+"&action=RETSTATUS&ret_period="+ret_period+"&ref_id="+ref_no+"";
		try 
		{
			URL url = new URL(BASE_URL);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
			 conn.setRequestProperty("Asp-Id", aspid);
			 conn.setRequestProperty("Asp-Secret", asp_secret);
			 conn.setRequestProperty("state-cd",state_cd );
			 conn.setRequestProperty("txn", txn);
			 conn.setRequestProperty("ip-usr", ip_usr);;
			 conn.setRequestProperty("host", host);
			 conn.setRequestProperty("username", username);
			 conn.setRequestProperty("auth_token", auth_token);
			 conn.setRequestProperty("GSTIN", gstn);
			 conn.setRequestProperty("ret_period", ret_period);
			 conn.setDoInput(true);
			 conn.setDoOutput(true);
			 BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			 String inputLine;
				StringBuffer respons = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					respons.append(inputLine);
				}
				in.close();
				result =respons.toString();
				JSONParser parser2 = new JSONParser();
				JSONObject jsonres = (JSONObject) parser2.parse(result);
				return jsonres;
			
		} 
		catch (Exception e) {
			// TODO: handle exception
		}
		
		return null;
		
	}

}
