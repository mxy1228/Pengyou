package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class Dynamic {

	private List<DynamicItem> data;
	private String errorDescription;
	private String successful;
	private String errorNo;

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
}
