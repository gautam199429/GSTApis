package com.codecube.gst.controller;

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
import com.codecube.gst.important.GSTRFile;
import com.codecube.gst.important.GSTRSummeyData;
import com.codecube.gst.utility.AESEncryption;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="GSTR File", description="GSTR File Controller")
public class GSTRFileController {
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	GSTRSummeyData gstrsummery;
	
	@Autowired
	RedisConfig red;
	
	@Autowired
	GSTRFile gstfile;
	
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/retfile", method= RequestMethod.GET)
	public JSONObject OtpRequest(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("UserId") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period,
			@RequestHeader("otp") String otp,
			@RequestHeader("pan") String pan)throws Exception
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		String sek = red.redisGetsek(gstin);
		@SuppressWarnings("static-access")
		String appkey = red.redisGetappkey(gstin);
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		try {
			JSONObject summery = gstrsummery.gstrSummery(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn);
			String summery_status = (String) summery.get("status_cd");
			if (summery_status.equals("1")) {
				String data = (String) summery.get("data");
				String rek = (String) summery.get("rek");
				@SuppressWarnings("static-access")
				String decode = app.decodeData(data, rek, sek, appkey);
				System.out.println(decode);
				String encpayload = AESEncryption.encryptJson(decode, sek, appkey);
				String pan1=""+pan+""+"|"+""+otp+"";
				String sign = AESEncryption.generatHmacOfPayloda(pan1, sek, appkey);
				JSONObject file = gstfile.gstrFile(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, encpayload, sign, otp, pan);
				return file;
			} else {
				return summery;
			}
			
			
			//String encpayload = AESEncryption.encryptJson(summery.toString(), sek, appkey);
//			String pan1=""+pan+""+"|"+""+otp+"";
//			String sign = AESEncryption.generatHmacOfPayloda(pan1, sek, appkey);
//			JSONObject file = gstfile.gstrFile(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, encpayload, sign, otp, pan);
//			return file;
		} catch (Exception e) {
			// TODO: handle exception
		}
		JSONParser parser2 = new JSONParser();
		JSONObject json = (JSONObject) parser2.parse(result);
		return json;
	}

}
