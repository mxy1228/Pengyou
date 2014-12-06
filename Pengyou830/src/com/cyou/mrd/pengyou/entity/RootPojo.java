package com.cyou.mrd.pengyou.entity;

public class RootPojo {
	private String errorNo;
	private String errorDescription;
	private Object data;
	private String successful;
	public static final String SUCCESS="1";
	public String getErrorNo() {
		return errorNo;
	}

	public void setErrorNo(String errorNo) {
		this.errorNo = errorNo;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}

	

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getSuccessful() {
		return successful;
	}

	public void setSuccessful(String successful) {
		this.successful = successful;
	}

	public String toString() {
		return getErrorNo() + "_" + getErrorDescription() + "_" + getData()
				+ "_" + getSuccessful();
	}
}
