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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;

import com.codecube.gst.config.RedisConfig;
import com.codecube.gst.entity.GSTR1Model;
import com.codecube.gst.important.GSTRCheckStatus;
import com.codecube.gst.important.GSTRSave;
import com.codecube.gst.important.GSTRSubmit;
import com.codecube.gst.utility.AESEncryption;

import io.swagger.annotations.Api;
import redis.clients.jedis.Jedis;

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
			@RequestHeader("clientid") String asp_id,
			@RequestHeader("client-secret") String asp_secret,
			@RequestHeader("state-cd") String state,
			@RequestHeader("ip-usr") String ip_usr,
			@RequestHeader("host") String host,
			@RequestHeader("txn") String txn,
			@RequestHeader("username") String username,
			@RequestHeader("GSTIN") String gstin,
			@RequestHeader("ret_period") String ret_period) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException, Exception
	{
		@SuppressWarnings("unused")
		String result = "{\"status_cd\":\"0\",\"error\":\"Please Check Your Headers\"}";
		@SuppressWarnings("static-access")
		
		Jedis jedis = new Jedis("localhost",6379);
		
		String sek = red.redisGetsek(gstin);
		
		@SuppressWarnings("static-access")
		String appkey = red.redisGetappkey(gstin);
		@SuppressWarnings("static-access")
		String auth_token = red.redisGetauthtoken(gstin);
		String payload="{\r\n" + 
				"	\"gstin\": \"27BCAMH0498C1Z3\",\r\n" + 
				"	\"fp\": \"092018\",\r\n" + 
				"	\"version\": \"GST2.2.9\",\r\n" + 
				"	\"hash\": \"hash\",\r\n" + 
				"	\"b2b\": [{\r\n" + 
				"		\"ctin\": \"27BABMH5613A1ZJ\",\r\n" + 
				"		\"inv\": [{\r\n" + 
				"				\"inum\": \"1000\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 50000,\r\n" + 
				"				\"pos\": \"37\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"diff_percent\": 0.65,\r\n" + 
				"				\"inv_typ\": \"CBW\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1201,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 45000,\r\n" + 
				"						\"rt\": 12,\r\n" + 
				"						\"iamt\": 3510,\r\n" + 
				"						\"csamt\": 756\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A1001\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 43000,\r\n" + 
				"				\"pos\": \"12\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"DE\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"						\"num\": 501,\r\n" + 
				"						\"itm_det\": {\r\n" + 
				"							\"txval\": 40000,\r\n" + 
				"							\"rt\": 5,\r\n" + 
				"							\"iamt\": 2000,\r\n" + 
				"							\"csamt\": 0\r\n" + 
				"						}\r\n" + 
				"					},\r\n" + 
				"					{\r\n" + 
				"						\"num\": 1201,\r\n" + 
				"						\"itm_det\": {\r\n" + 
				"							\"txval\": 40000,\r\n" + 
				"							\"rt\": 12,\r\n" + 
				"							\"iamt\": 4800,\r\n" + 
				"							\"csamt\": 0\r\n" + 
				"						}\r\n" + 
				"					}\r\n" + 
				"				]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"1000A\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 42000,\r\n" + 
				"				\"pos\": \"36\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"CBW\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1201,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 40000,\r\n" + 
				"						\"rt\": 12,\r\n" + 
				"						\"iamt\": 4800,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A/1001\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 55000,\r\n" + 
				"				\"pos\": \"05\",\r\n" + 
				"				\"rchrg\": \"Y\",\r\n" + 
				"				\"inv_typ\": \"R\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 2801,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 50000,\r\n" + 
				"						\"rt\": 28,\r\n" + 
				"						\"iamt\": 14000,\r\n" + 
				"						\"csamt\": 6700\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A/1002\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 40330,\r\n" + 
				"				\"pos\": \"05\",\r\n" + 
				"				\"rchrg\": \"Y\",\r\n" + 
				"				\"inv_typ\": \"R\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 26,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 40000,\r\n" + 
				"						\"rt\": 0.25,\r\n" + 
				"						\"iamt\": 100,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A/1003\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 50000,\r\n" + 
				"				\"pos\": \"37\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"R\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1201,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 10000,\r\n" + 
				"						\"rt\": 12,\r\n" + 
				"						\"iamt\": 1200,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A-10010\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 26000,\r\n" + 
				"				\"pos\": \"37\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"CBW\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1801,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 25000,\r\n" + 
				"						\"rt\": 18,\r\n" + 
				"						\"iamt\": 4500,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"1-10010\",\r\n" + 
				"				\"idt\": \"15-09-2018\",\r\n" + 
				"				\"val\": 48000,\r\n" + 
				"				\"pos\": \"36\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"R\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 40000,\r\n" + 
				"						\"rt\": 0,\r\n" + 
				"						\"iamt\": 0,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			},\r\n" + 
				"			{\r\n" + 
				"				\"inum\": \"A-KNP/1000/06-17\",\r\n" + 
				"				\"idt\": \"15-09-2018\",\r\n" + 
				"				\"val\": 45000,\r\n" + 
				"				\"pos\": \"37\",\r\n" + 
				"				\"rchrg\": \"N\",\r\n" + 
				"				\"inv_typ\": \"R\",\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 301,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 40000,\r\n" + 
				"						\"rt\": 3,\r\n" + 
				"						\"iamt\": 1200,\r\n" + 
				"						\"csamt\": 0\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			}\r\n" + 
				"		]\r\n" + 
				"	}],\r\n" + 
				"	\"b2cl\": [{\r\n" + 
				"			\"pos\": \"37\",\r\n" + 
				"			\"inv\": [{\r\n" + 
				"					\"inum\": \"10002\",\r\n" + 
				"					\"idt\": \"14-09-2018\",\r\n" + 
				"					\"val\": 250000.01,\r\n" + 
				"					\"inv_typ\": \"CBW\",\r\n" + 
				"					\"itms\": [{\r\n" + 
				"						\"num\": 1,\r\n" + 
				"						\"itm_det\": {\r\n" + 
				"							\"txval\": 255000,\r\n" + 
				"							\"rt\": 0,\r\n" + 
				"							\"iamt\": 0,\r\n" + 
				"							\"csamt\": 20756\r\n" + 
				"						}\r\n" + 
				"					}]\r\n" + 
				"				},\r\n" + 
				"				{\r\n" + 
				"					\"inum\": \"10004\",\r\n" + 
				"					\"idt\": \"14-09-2018\",\r\n" + 
				"					\"val\": 250000.01,\r\n" + 
				"					\"diff_percent\": 0.65,\r\n" + 
				"					\"itms\": [{\r\n" + 
				"						\"num\": 501,\r\n" + 
				"						\"itm_det\": {\r\n" + 
				"							\"txval\": 265000,\r\n" + 
				"							\"rt\": 5,\r\n" + 
				"							\"iamt\": 8612.5,\r\n" + 
				"							\"csamt\": 20756\r\n" + 
				"						}\r\n" + 
				"					}]\r\n" + 
				"				}\r\n" + 
				"			]\r\n" + 
				"		},\r\n" + 
				"		{\r\n" + 
				"			\"pos\": \"32\",\r\n" + 
				"			\"inv\": [{\r\n" + 
				"				\"inum\": \"10003\",\r\n" + 
				"				\"idt\": \"14-09-2018\",\r\n" + 
				"				\"val\": 250000.01,\r\n" + 
				"				\"itms\": [{\r\n" + 
				"					\"num\": 1201,\r\n" + 
				"					\"itm_det\": {\r\n" + 
				"						\"txval\": 255000,\r\n" + 
				"						\"rt\": 12,\r\n" + 
				"						\"iamt\": 30600,\r\n" + 
				"						\"csamt\": 20756\r\n" + 
				"					}\r\n" + 
				"				}]\r\n" + 
				"			}]\r\n" + 
				"		}\r\n" + 
				"	],\r\n" + 
				"	\"hsn\": {\r\n" + 
				"		\"data\": [{\r\n" + 
				"			\"num\": 1,\r\n" + 
				"			\"hsn_sc\": \"998313\",\r\n" + 
				"			\"desc\": \"Service\",\r\n" + 
				"			\"uqc\": \"NOS\",\r\n" + 
				"			\"qty\": 14,\r\n" + 
				"			\"val\": 1903578.19,\r\n" + 
				"			\"txval\": 1175500,\r\n" + 
				"			\"iamt\": 76155.83,\r\n" + 
				"			\"camt\": 1199.25,\r\n" + 
				"			\"samt\": 1199.25,\r\n" + 
				"			\"csamt\": 70224\r\n" + 
				"		}]\r\n" + 
				"	}\r\n" + 
				"}";
//		String payload = model.toString();
//		System.out.println(payload);
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
					return save_check_status;
//					JSONObject submit = gstsubmit.gstrSubmit(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn);
//					String submit_status_cd = (String) submit.get("status_cd");
//					if (submit_status_cd.equals("1")) 
//					{
//						String submit_reference_id = (String) submit.get("reference_id");
//						JSONObject submit_check_status = gstcheckstatus.gstCheckStatus(sek, auth_token, appkey, asp_id, asp_secret, username, ip_usr, host, gstin, ret_period, state, txn, submit_reference_id);
//						String check_submit_status_cd = (String) submit_check_status.get("status_cd");
//						if (check_submit_status_cd.equals("P")) 
//						{
//							return submit_check_status;
//						} else 
//						{
//							return submit_check_status;
//						}
//					} 
//					else
//					{
//						return submit;
//					}
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
