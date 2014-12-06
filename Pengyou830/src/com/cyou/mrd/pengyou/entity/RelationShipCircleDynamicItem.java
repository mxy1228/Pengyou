package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;

public class RelationShipCircleDynamicItem implements Serializable{

	/**
	 * {"aid":1, //动态id
 "cursor":788378434, //动态游标(用于动态记录的分页)
 "uid":3, //用户id
 "nickname":XXXX, //用户昵称
 "gender":"1", //性别
 "avatar":"img/sdfouwer.png", //用户头像
 "text":"爽玩了一下午！", //文字内容
// "game":{"gamecode":"009bd05190ef732766", //游戏代码
//               "gamenm":"神庙逃亡2", //游戏名称
//               "gametype":"跑酷", //游戏类别
//               "gameicon":"images/w114/1/1870.png", //游戏图片
//               "playnum":100, //多少人在玩
//               "gamedesc":"游戏简介", //在动态详情界面时才返回
//               "platform":1 //平台
//              }
 "supported":"1", //当前用户是否已经赞过
 "supportcnt":10, //被顶次数
 "commentcnt":20, //评论个数
 "type":0, //动态类型(分享游戏0-sharegame)
 //"supportusr":[{"uid":22,"avatar":"img/dfwer.png"}...], //赞的用户列表(带图标无昵称，只在动态详情中返回)
 //"supportusr":[{"uid":22,"nickname":"呵呵"}...], //赞的用户列表(无图标有昵称，只在关系圈动态列表中返回)
 "createdtime":"1366879866", //创建时间}, 
	 */
	private static final long serialVersionUID = 1L;
	private int aid = -1;
	private int uid = -1;
	private String nickname = "";
	private String text = "";//文字内容
	private int type = -1;
	private int supportcnt = -1;
	private int commentcnt =-1;
	private long createdtime = 0;
	private int supported = -1;//当前用户是否已经赞过，1-当前用户已赞过，0-当前用户没有赞过
	private String avatar = "";    

	private DynamicPic picture;
	private DynamicPic picturesmall;
	private DynamicPic picturemiddle;

	private int gender = 1; // 2:female 1:male
	private int cursor = 0;
	
	private String star =  "";
	
	private int dynamicType = 0;

	
	public int getGender() {
		return this.gender;
	}

	public void setGender(int  gd) {
		this.gender = gd;
	}
	
	public int getType() {
		return type;
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

	

	public void setSupported(int supported) {
		this.supported = supported;
	}

	

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
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

	

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public int getDynamicType() {
		return dynamicType;
	}

	public void setDynamicType(int dynamicType) {
		this.dynamicType = dynamicType;
	}

	
	
}
