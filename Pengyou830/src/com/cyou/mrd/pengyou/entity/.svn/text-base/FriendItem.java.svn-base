package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class FriendItem {

	private int uid;
	private String picture;
	private String avatar;
	private String platform;
	private int snstag;
	private long cursor;
	private String nickname;
	private int playnum;
	private String game;
	private int gender;
	private String bilateral;
	private List<String> recentgms;
	private String recentgmsStr;
	private int isfocus;// 是否关注 0没关注  1已关注
//	private boolean isBindSina;
	private String name;
	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
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

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public int getPlaynum() {
		return playnum;
	}

	public void setPlaynum(int playnum) {
		this.playnum = playnum;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getBilateral() {
		return bilateral;
	}

	public void setBilateral(String bilateral) {
		this.bilateral = bilateral;
	}

	public int getIsfocus() {
		return isfocus;
	}

	public void setIsfocus(int isfocus) {
		this.isfocus = isfocus;
	}

	public String getGame() {
		return game;
	}

	public void setGame(String game) {
		this.game = game;
	}


//	public boolean isBindSina() {
//		return isBindSina;
//	}
//
//	public void setBindSina(boolean isBindSina) {
//		this.isBindSina = isBindSina;
//	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRecentgms() {
		return recentgms;
	}

	public void setRecentgms(List<String> recentgms) {
		this.recentgms = recentgms;
	}
	
//	public void setRecentgms(String str){
//		if(str == null || TextUtils.isEmpty(str)){
//			return;
//		}
//		try {
//			if(recentgms == null){
//				recentgms = new ArrayList<String>();
//			}
//			JSONArray array = new JSONArray(str);
//			for(int i=0;i<array.length();i++){
//				recentgms.add(array.optString(i));
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	public String getRecentgmsStr() {
		if(recentgmsStr == null && recentgms != null){
			recentgmsStr = recentgms.toString();
		}
		return recentgmsStr;
	}

	public void setRecentgmsStr(String recentgmsStr) {
		this.recentgmsStr = recentgmsStr;
	}

}
