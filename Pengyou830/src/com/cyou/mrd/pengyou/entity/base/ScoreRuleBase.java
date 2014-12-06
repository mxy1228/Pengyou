package com.cyou.mrd.pengyou.entity.base;

import java.util.List;

import com.cyou.mrd.pengyou.entity.ScoreRuleItem;


public class ScoreRuleBase {

	private String errorDescription;
	private String successful;
	private String errorNo;
	private List<ScoreRuleItem> data;
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
	public List<ScoreRuleItem> getData() {
		return data;
	}
	public void setData(List<ScoreRuleItem> data) {
		this.data = data;
	}
	
}
