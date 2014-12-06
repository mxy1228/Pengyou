package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;

public class DynamicCommentItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int cid;//评论id
	private String comment;
	private long timestamp;//评论时间戳
	private int uid;
	private String nickname;
	private String avatar = "";
	private String replaytoString = "";
	private DynamicCommentReplyItem replyto;
    private int sendSuccess = 0;
    private String text;
    private String star = "";
    private int replynum = 0;
	
	public int getCid() {
		return cid;
	}
	public void setCid(int cid) {
		this.cid = cid;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
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
	public String getReplaytoString() {
		return replaytoString;
	}
	public void setReplaytoString(String replaytoString) {
		this.replaytoString = replaytoString;
	}
	public DynamicCommentReplyItem getReplyto() {
		return replyto;
	}
	public void setReplyto(DynamicCommentReplyItem replyto) {
		this.replyto = replyto;
	}
	public int getSendSuccess() {
		return sendSuccess;
	}
	public void setSendSuccess(int sendSuccess) {
		this.sendSuccess = sendSuccess;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public int getReplynum() {
		return replynum;
	}
	public void setReplynum(int replynum) {
		this.replynum = replynum;
	}
	
}
