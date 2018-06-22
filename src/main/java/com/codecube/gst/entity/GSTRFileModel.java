/**
 * @author gautam kumar sah
 * @date   18/06/2018
 */
package com.codecube.gst.entity;

import io.swagger.annotations.ApiModelProperty;

public class GSTRFileModel {

	@ApiModelProperty(example="RETFILE")
	private String action;
	private String data;
	private String sign;
	private String st;
	private String sid;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getSt() {
		return st;
	}
	public void setSt(String st) {
		this.st = st;
	}
	public String getSid() {
		return sid;
	}
	public void setSid(String sid) {
		this.sid = sid;
	}
}
