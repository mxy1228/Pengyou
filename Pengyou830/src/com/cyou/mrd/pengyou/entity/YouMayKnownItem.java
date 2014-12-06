package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class YouMayKnownItem {

	
	private String grpnm;//用户组标志
	private String grpdesc;//用户组名称
	private List<FriendItem> frds;
	public String getGrpnm() {
		return grpnm;
	}
	public void setGrpnm(String grpnm) {
		this.grpnm = grpnm;
	}
	public String getGrpdesc() {
		return grpdesc;
	}
	public void setGrpdesc(String grpdesc) {
		this.grpdesc = grpdesc;
	}
	public List<FriendItem> getFrds() {
		return frds;
	}
	public void setFrds(List<FriendItem> frds) {
		this.frds = frds;
	}
}
