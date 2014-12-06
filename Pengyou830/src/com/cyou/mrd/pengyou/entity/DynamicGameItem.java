package com.cyou.mrd.pengyou.entity;

public class DynamicGameItem {

	private String gamecode;
	private String gamenm;//游戏名称
	private String gametype;
	private String gameicon;
	private int playnum;
	private String gamedesc;//游戏简介
	private int platform;
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getGamenm() {
		return gamenm;
	}
	public void setGamenm(String gamenm) {
		this.gamenm = gamenm;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public String getGameicon() {
		return gameicon;
	}
	public void setGameicon(String gameicon) {
		this.gameicon = gameicon;
	}
	public int getPlaynum() {
		return playnum;
	}
	public void setPlaynum(int playnum) {
		this.playnum = playnum;
	}
	public String getGamedesc() {
		return gamedesc;
	}
	public void setGamedesc(String gamedesc) {
		this.gamedesc = gamedesc;
	}
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	
}
