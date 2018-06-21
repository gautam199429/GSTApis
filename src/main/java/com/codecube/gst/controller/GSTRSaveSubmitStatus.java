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
import org.springframework.web.bind.annotation.RestController;
import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.important.GSTRCheckStatus;
import com.codecube.gst.important.GSTRSave;
import com.codecube.gst.important.GSTRSubmit;
import com.codecube.gst.utility.AESEncryption;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="OTP", description="OTP Generation")
public class GSTRSaveSubmitStatus {
	
	@Autowired
	GSTRSave gstsave;
	
	@Autowired
	GSTRSubmit gstsubmit;
	
	@Autowired
	GSTRCheckStatus gstcheckstatus;
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	RedisConfig red;
	
	@Consumes(javax.ws.rs.core.MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@RequestMapping(value ="/savesubmit", method= RequestMethod.GET)
	public JSONObject saveSubmitStatus(
			@RequestHeader("Asp-Id") String asp_id,
			@RequestHeader("Asp-Secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("username") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period)
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		String sek = red.redisGetsek(gstin);
		@SuppressWarnings("static-access")
		String appkey = red.redisGetappkey(gstin);
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		try {
			String encpayload = "";
			String hmac = "";
			JSONObject save = gstsave.gstrSave(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, encpayload, hmac);
			String status_cd = (String) save.get("status_cd");
			String ref_id = (String) save.get("ref_id");
			if (status_cd.equals("1")) {
				JSONObject checkstatus = gstcheckstatus.gstCheckStatus(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, ref_id);
				String checkstatus_cd = (String) checkstatus.get("status_cd");
				if (checkstatus_cd.equals("P")) {
					
					
				} else {

				}
				
			} else {
					return save;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
	

}
