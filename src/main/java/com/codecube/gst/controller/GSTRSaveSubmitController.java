package com.codecube.gst.controller;

import java.net.URL;

import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.entity.OTPModel;
import com.codecube.gst.utility.AESEncryption;
import com.codecube.gst.utility.EncryptionUtil;

import io.swagger.annotations.Api;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="GSTR SAVE/SUBMIT", description="GSTR SAVE SUBMIT CONTROLLER")
public class GSTRSaveSubmitController {
	
	private static final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/gstr1";
	
	@Autowired
	SessionFactory sessionFactory;
	
	@Autowired
	RedisConfig redc;
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	EncryptionUtil encutil;
	
	@RequestMapping(value ="/saveandsubmit", method= RequestMethod.GET)
	public JSONObject saveSubmitRequest(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("username") String username,
			@RequestHeader("GSTN") String gstn,
			@RequestHeader("ret_period") String ret_period)throws Exception
	{
		@SuppressWarnings("static-access")
		String sek = redc.redisGetsek(gstn);
		
		@SuppressWarnings("static-access")
		String appkey = redc.redisGetappkey(gstn);
		
		@SuppressWarnings("static-access")
		String auth_token = redc.redisGetauthtoken(gstn);
	try {
		URL url = new URL(BASE_URL);	
	} 
	catch (Exception e) {
		// TODO: handle exception
	}
		
		return null;
		
	}
}
