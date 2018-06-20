package com.codecube.gst.controller;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.codecube.gst.utility.AESEncryption;
import com.codecube.gst.utility.EncryptionUtil;

@Repository
public class GSTRCheckStatus {
	
	@Autowired
	AESEncryption app;
	
	@Autowired
	EncryptionUtil enc;
	
	
	
	public JSONObject gstrSubmi(
			String aspid, 
			String asp_secret,
			String ip_usr,
			String txn,
			String host,
			String gstn,
			String ret_period,
			String username,
			String ref_no)
	{
		return null;
		
	}

}
