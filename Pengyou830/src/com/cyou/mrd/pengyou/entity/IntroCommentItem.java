package com.cyou.mrd.pengyou.entity;

import java.util.List;


public class IntroCommentItem {

	private int cid;
	private String text;//评论内容
	private float star;
	private long timestamp;
	private int uid;//评论人用户id
	private String nickname;
	private String avatar;
	private int replynum;//回复个数
	private int cursor;
	private StringBuilder subcomments = new StringBuilder();
	private boolean open = false;//是否展开子评论
	private List<IntroSubComment> subCommentData;
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public float getStar() {
		return star;
	}
	public void setStar(float star) {
		this.star = star;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public int getReplynum() {
		return replynum;
	}
	public void setReplynum(int replynum) {
		this.replynum = replynum;
	}
	public int getCursor() {
		return cursor;
	}
	public void setCursor(int cursor) {
		this.cursor = cursor;
	}
	public StringBuilder getSubcomments() {
		return subcomments;
	}
	public void setSubcomments(StringBuilder subcomments) {
		this.subcomments = subcomments;
	}
	public boolean isOpen() {
		return open;
	}
	public void setOpen(boolean open) {
		this.open = open;
	}
	public List<IntroSubComment> getSubCommentData() {
		return subCommentData;
	}
	public void setSubCommentData(List<IntroSubComment> subCommentData) {
		this.subCommentData = subCommentData;
	}
	
}
