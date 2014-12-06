package com.cyou.mrd.pengyou.entity.base;

import com.cyou.mrd.pengyou.entity.Intro;

public class IntroBase {

	private Intro data;
	private String errorDescription;
	private String successful;
	private String errorNo;
	public Intro getData() {
		return data;
	}
	
    public void setData(Intro data) {
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
