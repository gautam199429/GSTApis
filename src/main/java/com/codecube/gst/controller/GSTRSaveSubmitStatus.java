package com.codecube.gst.controller;

import java.io.IOException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
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
@Api(value="GSTR Save/GSTR Submit", description="GSTR Save Submit Controller")
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
			@RequestHeader("ret_period") String ret_period) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
	{
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		String sek = red.redisGetsek(gstin);
		@SuppressWarnings("static-access")
		String appkey = red.redisGetappkey(gstin);
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		String payload = "{\"gstin\":\"27BCAMH0498C1Z3\",\"fp\":\"052018\",\"gt\":3782969.01,\"cur_gt\":3782969.01,\"b2b\":[{\"ctin\":\"33TABTN1602C1ZV\",\"inv\":[{\"inum\":\"S24\",\"idt\":\"04-05-2018\",\"val\":729248.16,\"pos\":\"33\",\"rchrg\":\"N\",\"inv_typ\":\"R\",\"itms\":[{\"num\":1,\"itm_det\":{\"rt\":5,\"txval\":10000,\"iamt\":833.33,\"csamt\":500}}]}]}]} ";
		System.out.println(payload);
		String encpayload = AESEncryption.encryptJson(payload, sek, appkey);
		String hmac = AESEncryption.generatHmacOfPayloda(payload, sek, appkey);
		try {
			JSONObject save = gstsave.gstrSave(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, encpayload, hmac);
			String save_status_cd = (String) save.get("status_cd");
			if (save_status_cd.equals("1")) 
			{
				String save_reference_id = (String) save.get("reference_id");
				JSONObject save_check_status = gstcheckstatus.gstCheckStatus(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, save_reference_id);
				String check_status_cd = (String) save_check_status.get("status_cd");
				if (check_status_cd.equals("P")) 
				{
					JSONObject submit = gstsubmit.gstrSubmit(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn);
					String submit_status_cd = (String) submit.get("status_cd");
					if (submit_status_cd.equals("1")) 
					{
						String submit_reference_id = (String) submit.get("reference_id");
						JSONObject submit_check_status = gstcheckstatus.gstCheckStatus(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, submit_reference_id);
						String check_submit_status_cd = (String) submit_check_status.get("status_cd");
						if (check_submit_status_cd.equals("P")) 
						{
							return submit_check_status;
						} else 
						{
							return submit_check_status;
						}
					} 
					else
					{
						return submit;
					}
				} 
				else 
				{
					return save_check_status;
				}
			} 
			else 
			{
				return save;

			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return null;
		
	}
	

}
