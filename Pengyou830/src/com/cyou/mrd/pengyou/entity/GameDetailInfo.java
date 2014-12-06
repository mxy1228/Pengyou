package com.cyou.mrd.pengyou.entity;

import java.util.Map;



public class GameDetailInfo {
	private String gamecode;
	private String source;
	private Map<String, String> securityinfo;
	private String fullsize;
	
	public GameDetailInfo() {
	}
	
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public Map<String, String> getSecurityinfo() {
		return securityinfo;
	}
	public void setSecurityinfo(Map<String, String> securityinfo) {
		this.securityinfo = securityinfo;
	}

	public String getFullsize() {
		return fullsize;
	}

	public void setFullsize(String fullsize) {
		this.fullsize = fullsize;
	}

	
}
