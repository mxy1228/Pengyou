package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;

public class MyMessageItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int from;
	private int to;
	private String content;
	private long createtime;
	private String gamecode;
	private String avatar;
	private String icon;
	private String gamename;
	private float rating;
	private int type;
	private String nickname;
	private int tauid;
	private int isread;
	private String msgid;
	private String sendSuccess;
	private String msgseq;
	private int sendState;
	public int getFrom() {
		return from;
	}
	public void setFrom(int from) {
		this.from = from;
	}
	public int getTo() {
		return to;
	}
	public void setTo(int to) {
		this.to = to;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(long createtime) {
		this.createtime = createtime;
	}
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getGamename() {
		return gamename;
	}
	public void setGamename(String gamename) {
		this.gamename = gamename;
	}
	public float getRating() {
		return rating;
	}
	public void setRating(float rating) {
		this.rating = rating;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTauid() {
		return tauid;
	}
	public void setTauid(int tauid) {
		this.tauid = tauid;
	}
	public int getIsread() {
		return isread;
	}
	public void setIsread(int isread) {
		this.isread = isread;
	}
	public String getMsgid() {
		return msgid;
	}
	public void setMsgid(String msgid) {
		this.msgid = msgid;
	}
	public String getSendSuccess() {
		return sendSuccess;
	}
	public void setSendSuccess(String sendSuccess) {
		this.sendSuccess = sendSuccess;
	}
	public String getMsgseq() {
		return msgseq;
	}
	public void setMsgseq(String msgseq) {
		this.msgseq = msgseq;
	}
	public int getSendState() {
		return sendState;
	}
	public void setSendState(int sendState) {
		this.sendState = sendState;
	}
	
}
