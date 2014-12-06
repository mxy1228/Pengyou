package com.cyou.mrd.pengyou.entity;

import java.util.List;

public class SeminarBean {
	private String id;
	private String name;
	private String desc;
	private String picture;
	private Long topicdate;
	private List<GameItem> topicgms;
//	private String memo;


	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Long getTopicdate() {
		return topicdate;
	}

	public void setTopicdate(Long topicdate) {
		this.topicdate = topicdate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<GameItem> getTopicgms() {
		return topicgms;
	}

	public void setTopicgms(List<GameItem> topicgms) {
		this.topicgms = topicgms;
	}

//	public String getMemo() {
//		return memo;
//	}
//
//	public void setMemo(String memo) {
//		this.memo = memo;
//	}
	

}
