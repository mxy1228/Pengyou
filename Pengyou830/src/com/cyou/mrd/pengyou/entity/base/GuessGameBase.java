package com.cyou.mrd.pengyou.entity.base;

import java.util.ArrayList;

import com.cyou.mrd.pengyou.entity.GuessGameItem;

public class GuessGameBase {

	private String errorDescription;
	private String successful;
	private String errorNo;
	private ArrayList<GuessGameItem> data;
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
	public ArrayList<GuessGameItem> getData() {
		return data;
	}
	public void setData(ArrayList<GuessGameItem> data) {
		this.data = data;
	}
	
}
