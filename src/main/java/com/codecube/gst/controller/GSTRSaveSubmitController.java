package com.codecube.gst.controller;

import org.hibernate.SessionFactory;
import org.json.simple.JSONObject;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codecube.gst.entity.OTPModel;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/taxpayerapi")
@CrossOrigin
@Api(value="GSTR SAVE/SUBMIT", description="GSTR SAVE SUBMIT CONTROLLER")
public class GSTRSaveSubmitController {
	
	private static final String BASE_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/gstr1";
	SessionFactory sessionFactory;
	
	@RequestMapping(value ="/saveandsubmit", method= RequestMethod.PUT, headers = "Content-type=application/json")
	public JSONObject OtpRequest(@RequestBody OTPModel otp,
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
	try {
		
		
	} catch (Exception e) {
		// TODO: handle exception
	}
		
		return null;
		
	}

}
