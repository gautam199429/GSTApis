package com.codecube.gst.controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codecube.gst.config.RedisConfig;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="OTP", description="OTP Generation")
public class GSTREVCOTPController {
	
	@Autowired
	RedisConfig red;
	
	
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/evc", method= RequestMethod.GET)
	public JSONObject evcOtpRequest(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("username") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period,
			@RequestHeader("Form-Type") String form_type,
			@RequestHeader("pan") String pan) throws ParseException
	
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		try {
			URL url = new URL("https://apiuat.spicegsp.com/taxpayerapi/v0.2/authenticate?gstin="+gstin+"&action=EVCOTP&pan="+pan+"&form_type="+form_type+"");
//			https://apiuat.spicegsp.com/taxpayerapi/v1.0/authenticate?gstin=27BABMH5613A1ZJ&action=EVCOTP&pan=AHWPB0871H&form_type=R1
			System.out.println(url);
			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			 conn.setRequestProperty("Content-Type",javax.ws.rs.core.MediaType.APPLICATION_JSON );
			 conn.setRequestProperty("Asp-Id", asp_id);
			 conn.setRequestProperty("Asp-Secret", asp_secret);
			 conn.setRequestProperty("state-cd", state);
			 conn.setRequestProperty("txn", txn);
			 conn.setRequestProperty("ip-usr", ip_usr);;
			 conn.setRequestProperty("host", host);
			 conn.setRequestProperty("username", username);
			 conn.setRequestProperty("GSTIN", gstin);
			 conn.setRequestProperty("ret_period", ret_period);
			 conn.setRequestProperty("auth-token", auth_token);
			 System.out.println("HEADERS SEND");
			 conn.connect();
			 System.out.println(conn.getResponseCode());
			 BufferedReader in = new BufferedReader(
					 new InputStreamReader(conn.getInputStream()));
						String inputLine;
						StringBuffer response = new StringBuffer();
						System.out.println("1236547");
						while ((inputLine = in.readLine()) != null) {
							response.append(inputLine);
						}
						in.close();
						result =response.toString();
						JSONParser parser1 = new JSONParser();
						JSONObject json = (JSONObject) parser1.parse(result);
						return json;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(result);
		return json;
		
	}

}
