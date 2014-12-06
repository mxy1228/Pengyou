package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;

public class ContactFriendItem implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String phone;
	private String uid;
	private String nickname;
	private String name;
	private String picture;
	private int gender;
	private int relation;// "relation":"3" 关系标识3-已关注，4-双向关注，0-无关系

	public ContactFriendItem(String phone, String name, String uid,
			String nickname, String picture, int gender, int relation) {
		this.phone = phone;
		this.name = name;
		this.uid = uid;
		this.nickname = nickname;
		this.picture = picture;
		this.gender = gender;
		this.relation = relation;
	}
	
	public ContactFriendItem()
	{
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public int getRelation() {
		return relation;
	}

	public void setRelation(int relation) {
		this.relation = relation;
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}
	
	public boolean isAttention() {
		return relation != 0;
	}
}