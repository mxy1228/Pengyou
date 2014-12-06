package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;

public class SNSItem implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String snsId;
	private String snsName;//为UI显示的名称
	private String name;//为服务器上传参数名称
	private long updateTime;
	private String token;
	private String exportTime;

	public String getSnsId() {
		return snsId;
	}

	public void setSnsId(String snsId) {
		this.snsId = snsId;
	}

	public String getSnsName() {
		return snsName;
	}

	public void setSnsName(String snsName) {
		this.snsName = snsName;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getExportTime() {
		return exportTime;
	}

	public void setExportTime(String exportTime) {
		this.exportTime = exportTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
