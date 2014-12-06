package com.cyou.mrd.pengyou.entity.base;

import java.util.List;

import com.cyou.mrd.pengyou.entity.IntroCommentItem;

public class IntroCommentBase {

	private List<IntroCommentItem> data;
	private String errorDescription;
	private String successful;
	private String errorNo;
	public List<IntroCommentItem> getData() {
		return data;
	}
	public void setData(List<IntroCommentItem> data) {
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
