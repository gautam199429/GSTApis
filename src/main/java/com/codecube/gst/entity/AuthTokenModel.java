/**
 * @author gautam kumar sah
 * @date   18/06/2018
 */


package com.codecube.gst.entity;

import io.swagger.annotations.ApiModelProperty;

public class AuthTokenModel {
	
	@ApiModelProperty(example="AUTHTOKEN")
	private String action;
	private String app_key;
	private String username;
	private String otp;
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getApp_key() {
		return app_key;
	}
	public void setApp_key(String app_key) {
		this.app_key = app_key;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getOtp() {
		return otp;
	}
	public void setOtp(String otp) {
		this.otp = otp;
	}

}
