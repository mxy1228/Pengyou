package com.cyou.mrd.pengyou.entity;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GuessYourFriendItem {
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
	private String phone;
	private int isfocus;// 是否关注 0没关注  1已关注
	private String name;
	private String smsname;
	private String snsname;
    private int recommedType;//1:通讯录用户   2：微博用户  3：玩同款游戏
    //好友在玩
    public GuessYourFriendItem(int uid, String nickname, int gender, List<String> recentgms, int recommedType, String avatar) {
		
		this.uid = uid;
		this.nickname = nickname;
		this.gender = gender;
		this.recentgms = recentgms;
		this.recommedType = recommedType;
		this.avatar = avatar;
    }
    public GuessYourFriendItem() {
    }
    
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

	public String getRecentgmsStr() {
		if(recentgmsStr == null && recentgms != null){
			recentgmsStr = recentgms.toString();
		}
		return recentgmsStr;
	}

	public void setRecentgmsStr(String recentgmsStr) {
		this.recentgmsStr = recentgmsStr;
	}

	public void setPhone(String phone) {//只有手机用户才有
		this.phone = phone;
	}

	public String getPhone() {
		return phone;
	}
	
	public int getRecommedType() {
		return recommedType;
	}

	public void setRecommedType(int recommedType) {
		this.recommedType = recommedType;
	}
	public String getSmsname() {
		return smsname;
	}
	public void setSmsname(String smsname) {
		this.smsname = smsname;
	}
	public String getSnsname() {
		return snsname;
	}
	public void setSnsname(String snsname) {
		this.snsname = snsname;
	}
}
