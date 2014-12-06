package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class MyNearByItem {

	private String gender;
	private String nickname;
	private String picture;
	private int platform;
	private int uid;
	private int distance;
	private List<String> recentgms;
	private String birthday;
	private int gms;
	private double longitude;
	private double latitude;
	private int isfocus;
	
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
	public int getPlatform() {
		return platform;
	}
	public void setPlatform(int platform) {
		this.platform = platform;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getDistance() {
		return distance;
	}
	public void setDistance(int distance) {
		this.distance = distance;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public int getGms() {
		return gms;
	}
	public void setGms(int gms) {
		this.gms = gms;
	}
	public int getIsfocus() {
		return isfocus;
	}
	public void setIsfocus(int isfocus) {
		this.isfocus = isfocus;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public List<String> getRecentgms() {
		return recentgms;
	}
	public void setRecentgms(List<String> recentgms) {
		this.recentgms = recentgms;
	}
	
}
