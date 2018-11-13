package com.codecube.gst.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.important.GSTR2AB2BInvoice;

import io.swagger.annotations.Api;
import redis.clients.jedis.Jedis;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="EVC", description="EVC Otp Controller")
public class GSTR2InvoiceController {
	
	@Autowired
	RedisConfig red;
	
	@Autowired
	GSTR2AB2BInvoice gstr2a;
	
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/gstr2ab2b", method= RequestMethod.GET)
	public JSONObject getgstr2ab2bInvoice(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("username") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period,
			@RequestParam(value = "action", required = true) String action
			)
	{
		Jedis jedis = new Jedis("localhost",6379);
		@SuppressWarnings("unused")
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		String sek = red.redisGetsek(gstin);
		@SuppressWarnings("static-access")
		String appkey = red.redisGetappkey(gstin);
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		try {
			JSONObject getinvoice = gstr2a.gstr2ab2bInoice(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, action);
			
			return getinvoice;
			
		} catch (Exception e) {
			// TODO: handle exception
			
		}
		return null;
		
	}

}
