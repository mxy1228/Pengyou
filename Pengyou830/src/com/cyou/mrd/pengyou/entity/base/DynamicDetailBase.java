package com.cyou.mrd.pengyou.entity.base;

import java.util.List;

import com.cyou.mrd.pengyou.entity.DynamicDetail;

public class DynamicDetailBase {

	private DynamicDetail data;
	private String errorDescription;
	private int successful;
	private int errorNo;


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

	public DynamicDetail getData() {
		return data;
	}

	public void setData(DynamicDetail data) {
		this.data = data;
	}

	
}
