package com.codecube.gst.config;

import org.springframework.stereotype.Repository;

@Repository
public class BaseUrls {
	
	//public String AUTENTICATION_URL = "https://apiuat.spicegsp.com/taxpayerapi/v0.2/authenticate";
	//public String GSTR1_URL = "https://apiuat.spicegsp.com/taxpayerapi/v1.0/returns/gstr1";
	public static String getAUTENTICATION_URL() {
		String authenticate = "https://apiuat.spicegsp.com/taxpayerapi/v0.2/authenticate";
		//String authenticate = "https://api.spicegsp.com/taxpayerapi/v0.2/authenticate";
		return null;
	}	
	/**
	 * Production URLS
	 */
	//private static final String AUTENTICATION_URL = "https://api.spicegsp.com/taxpayerapi/v0.2/authenticate";
	//private static final String GSTR1_URL = "https://api.spicegsp.com/taxpayerapi/v1.0/returns/gstr1";
}
