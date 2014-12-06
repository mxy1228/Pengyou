package com.cyou.mrd.pengyou.entity.base;

import java.util.List;

import com.cyou.mrd.pengyou.entity.AdvertBean;

public class AdvertBase {
	private String errorDescription;
	private String successful;
	private String errorNo;
	private List<AdvertBean> data;
	
	public String getErrorDescription() {
		return errorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	public String getSuccessful() {
		return successful;
	}
	public void setSuccessful(String successful) {
		this.successful = successful;
	}
	public String getErrorNo() {
		return errorNo;
	}
	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}
	public List<AdvertBean> getData() {
		return data;
	}
	public void setData(List<AdvertBean> data) {
		this.data = data;
	}
	
}
