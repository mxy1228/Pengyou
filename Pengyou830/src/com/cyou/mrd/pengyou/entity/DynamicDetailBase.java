package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class DynamicDetailBase {

	private DynamicDetail data;
	private String errorDescription;
	private int successful;
	private int errorNo;


	public DynamicDetail getData() {
		return data;
	}

	public void setData(DynamicDetail data) {
		this.data = data;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	public int getSuccessful() {
		return successful;
	}

	public void setSuccessful(int successful) {
		this.successful = successful;
	}

	public int getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(int errorNo) {
		this.errorNo = errorNo;
	}
	
}
