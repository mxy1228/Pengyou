package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class GameCircleDynamic {

	private List<DynamicItem> data;
	private String errorDescription;
	private String successful;
	private String errorNo;
	private String isremarked;
	private String gmname;

	public List<DynamicItem> getData() {
		return data;
	}

	public void setData(List<DynamicItem> data) {
		this.data = data;
	}

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

	public String getIsremarked() {
		return isremarked;
	}

	public void setIsremarked(String isremarked) {
		this.isremarked = isremarked;
	}

	public String getGmname() {
		return gmname;
	}

	public void setGmname(String gmname) {
		this.gmname = gmname;
	}
	
}
