package com.cyou.mrd.pengyou.entity.base;

import java.util.List;

import com.cyou.mrd.pengyou.entity.GameSimpleInfo;

public class GameSimpleInfoBase {
	private String errorDescription;
	private String successful;
	private String errorNo;
	private List<GameSimpleInfo> data;
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
	public List<GameSimpleInfo> getData() {
		return data;
	}
	public void setData(List<GameSimpleInfo> data) {
		this.data = data;
	}
	
}
