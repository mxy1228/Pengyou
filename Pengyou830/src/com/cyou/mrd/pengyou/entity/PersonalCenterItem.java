package com.cyou.mrd.pengyou.entity;

public class PersonalCenterItem {

	public static final int UP = 1;
	public static final int MID = 2;
	public static final int DOWN = 3;
	public static final int WHOLE = 4;
	
	private String name;
	private int tag;//1-up;2-mid;3-down;4-whole;
	private String action;
	private boolean showHint;
	private String hintContent;
	private int msgCount;//未读消息数目
	private int recommendCount;//好友推荐数目

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isShowHint() {
		return showHint;
	}

	public void setShowHint(boolean showHint) {
		this.showHint = showHint;
	}

	public String getHintContent() {
		return hintContent;
	}

	public void setHintContent(String hintContent) {
		this.hintContent = hintContent;
	}

	public int getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(int msgCount) {
		this.msgCount = msgCount;
	}

	public int getRecommendCount() {
		return recommendCount;
	}

	public void setRecommendCount(int recommendCount) {
		this.recommendCount = recommendCount;
	}
	
}
