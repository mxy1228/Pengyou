package com.cyou.mrd.pengyou.entity.base;

import java.util.ArrayList;
import java.util.List;

import com.cyou.mrd.pengyou.entity.RecommendFriendItem;

public class RecommendFriendBase {

	private String errorDescription;
	private String successful;
	private String errorNo;
	private ArrayList<RecommendFriendItem> data;
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
	public ArrayList<RecommendFriendItem> getData() {
		return data;
	}
	public void setData(ArrayList<RecommendFriendItem> data) {
		this.data = data;
	}
	
}
