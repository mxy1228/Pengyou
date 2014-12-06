package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class MyFocusItem {

	private String gender;
	private String nickname;
	private String picture;
	private String platform;
	private int uid;
	private int bilateral;//是否相互关注。0-否，1-是
	private List<String> recentgms;
	private int playnum;
	private int snstag;
	private long cursor;//时间戳
	private int relation;//0-无关系/3-已关注/4-相互关注
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getPlaynum() {
		return playnum;
	}
	public void setPlaynum(int playnum) {
		this.playnum = playnum;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public int getSnstag() {
		return snstag;
	}
	public void setSnstag(int snstag) {
		this.snstag = snstag;
	}
	public long getCursor() {
		return cursor;
	}
	public void setCursor(long cursor) {
		this.cursor = cursor;
	}
	public int getBilateral() {
		return bilateral;
	}
	public void setBilateral(int bilateral) {
		this.bilateral = bilateral;
	}
	public int getRelation() {
		return relation;
	}
	public void setRelation(int relation) {
		this.relation = relation;
	}
	public List<String> getRecentgms() {
		return recentgms;
	}
	public void setRecentgms(List<String> recentgms) {
		this.recentgms = recentgms;
	}
	
}
