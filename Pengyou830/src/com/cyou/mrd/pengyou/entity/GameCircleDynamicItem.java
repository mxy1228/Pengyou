package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;
import java.util.List;


public class GameCircleDynamicItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int aid;
	private int uid;
	private String nickname;
	private int gender;//性别 0,1 区别 暂时未知
	private String text;//文字内容
	private DynamicGameItem game;
	private int type;
    private int supportcnt;
	private int commentcnt;
	private long createdtime;
	private int supported;//当前用户是否已经赞过，1-当前用户已赞过，0-当前用户没有赞过
	private boolean isOpen = false;//是否展开了评论
	private String avatar;
	private StringBuilder comment = new StringBuilder();
	private List<DynamicCommentItem> subCommentData;
	private int lastcommentid;
	private DynamicPic picture;
	private DynamicPic picturesmall;
	private DynamicPic picturemiddle;
	private int snstag;
	private int bilateral;//是否相互关注
	private boolean loadingComments;
	public int getType() {
		return type;
	}
    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
	public void setType(int type) {
		this.type = type;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DynamicGameItem getGame() {
		return game;
	}

	public void setGame(DynamicGameItem game) {
		this.game = game;
	}

	public int getSupportcnt() {
		return supportcnt;
	}

	public void setSupportcnt(int supportcnt) {
		this.supportcnt = supportcnt;
	}

	public int getCommentcnt() {
		return commentcnt;
	}

	public void setCommentcnt(int commentcnt) {
		this.commentcnt = commentcnt;
	}

	public long getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(long createdtime) {
		this.createdtime = createdtime;
	}

//	public DynamicComment getComment() {
//		return comment;
//	}
//
//	public void setComment(DynamicComment comment) {
//		this.comment = comment;
//	}

	public int getSupported() {
		return supported;
	}

	public StringBuilder getComment() {
		return comment;
	}

	public void setComment(StringBuilder comment) {
		this.comment = comment;
	}

	public void setSupported(int supported) {
		this.supported = supported;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getLastcommentid() {
		return lastcommentid;
	}

	public void setLastcommentid(int lastcommentid) {
		this.lastcommentid = lastcommentid;
	}

	public DynamicPic getPicture() {
		return picture;
	}

	public void setPicture(DynamicPic picture) {
		this.picture = picture;
	}

	public DynamicPic getPicturesmall() {
		return picturesmall;
	}

	public void setPicturesmall(DynamicPic picturesmall) {
		this.picturesmall = picturesmall;
	}

	public DynamicPic getPicturemiddle() {
		return picturemiddle;
	}

	public void setPicturemiddle(DynamicPic picturemiddle) {
		this.picturemiddle = picturemiddle;
	}

	public void setSnstag(int snstag) {
		this.snstag = snstag;
	}

	public void setBilateral(int bilateral) {
		this.bilateral = bilateral;
	}

	public int getSnstag() {
		return snstag;
	}

	public int getBilateral() {
		return bilateral;
	}

	public boolean isLoadingComments() {
		return loadingComments;
	}

	public void setLoadingComments(boolean loadingComments) {
		this.loadingComments = loadingComments;
	}

	public List<DynamicCommentItem> getSubCommentData() {
		return subCommentData;
	}

	public void setSubCommentData(List<DynamicCommentItem> subCommentData) {
		this.subCommentData = subCommentData;
	}

}
