/**
 * @author gautam kumar sah
 * @date   18/06/2018
 */





package com.codecube.gst.entity;

import io.swagger.annotations.ApiModelProperty;

public class GSTRSaveModel {
	@ApiModelProperty(example="RETSAVE")
	private String action;
	
	private String data;
	
	private String hmac;
	
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getHmac() {
		return hmac;
	}
	public void setHmac(String hmac) {
		this.hmac = hmac;
	}
	

}
