package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;
import java.util.List;
import com.cyou.mrd.pengyou.utils.WeiboApi;

public class UserInfo implements Serializable {

	/**
	 * 用户实体Bean
	 */
	private static final long serialVersionUID = 1L;
	private String nickname;// 昵称
	private String school;// 学校
	private long birthday;// 生日
	private int gender;// 性别
	private String picture;// 头像路径
	private String favgame;// 最喜欢游戏类型
	private String signature;// 签名
	private int uid;// 编号
	private int level;// 等级
	private int exp;// 经验
	private int explimit;// 经验上限
	private int focus;// 关注数
	private int fans;// 粉丝数
	private int acts;// 动态数
	private int msgs;// 私信数
	private String uauth;// 用户验证串
	private String phone;// 手机号
	private String localPicPath;// 存放头像本地路径
	private String password;
	private String areaid;
	private int games;
	private String pictureorig;//用户头像原图
	private int unreadChatLetter;//未读私信消息数目
	private int unreadRelationCircleLetter;//未读关系圈消息数目
	private List<SNSBean> snsbindlist;
	private boolean canexchangescore;//用户是否可以领取完成个人信息的积分
	private int availablescore;//用户当前可用积分总数
	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getSchool() {
		return school;
	}

	public void setSchool(String school) {
		this.school = school;
	}

	public long getBirthday() {
		return birthday;
	}

	public void setBirthday(long birthday) {
		this.birthday = birthday;
	}

	public int getGender() {
		return gender;
	}

	public void setGender(int gender) {
		this.gender = gender;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getFavgame() {
		return favgame;
	}

	public void setFavgame(String favgame) {
		this.favgame = favgame;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public int getGames() {
		return games;
	}

	public void setGames(int games) {
		this.games = games;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

	public int getExplimit() {
		return explimit;
	}

	public void setExplimit(int explimit) {
		this.explimit = explimit;
	}

	public int getFocus() {
		return focus;
	}

	public void setFocus(int focus) {
		this.focus = focus;
	}

	public int getFans() {
		return fans;
	}

	public void setFans(int fans) {
		this.fans = fans;
	}

	public int getActs() {
		return acts;
	}

	public void setActs(int acts) {
		this.acts = acts;
	}

	public int getMsgs() {
		return msgs;
	}

	public void setMsgs(int msgs) {
		this.msgs = msgs;
	}

	public String getUauth() {
		return uauth;
	}

	public void setUauth(String uauth) {
		this.uauth = uauth;
	}


	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocalPicPath() {
		return localPicPath;
	}

	public void setLocalPicPath(String localPicPath) {
		this.localPicPath = localPicPath;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAreaid() {
		return areaid;
	}

	public void setAreaid(String areaid) {
		this.areaid = areaid;
	}

	public String getPictureorig() {
		return pictureorig;
	}

	public void setPictureorig(String pictureorig) {
		this.pictureorig = pictureorig;
	}

	public int getUnreadChatLetter() {
		return unreadChatLetter;
	}

	public void setUnreadChatLetter(int unreadChatLetter) {
		this.unreadChatLetter = unreadChatLetter;
	}

	public int getUnreadRelationCircleLetter() {
		return unreadRelationCircleLetter;
	}

	public void setUnreadRelationCircleLetter(int unreadRelationCircleLetter) {
		this.unreadRelationCircleLetter = unreadRelationCircleLetter;
	}

	public List<SNSBean> getSnsbindlist() {
		return snsbindlist;
	}

	public void setSnsbindlist(List<SNSBean> snsbindlist) {
		this.snsbindlist = snsbindlist;
	}
//	public boolean isBindPhone() {
//		return !(getPhone() == null || getPhone().isEmpty());
//	}
	public boolean isBindSns() {
		return WeiboApi.getInstance().isBindSina();
	}
	public boolean isBindQQ() {
		return false;
	}

	public boolean isCanexchangescore() {
		return canexchangescore;
	}

	public void setCanexchangescore(boolean canexchangescore) {
		this.canexchangescore = canexchangescore;
	}

	public int getAvailablescore() {
		return availablescore;
	}

	public void setAvailablescore(int availablescore) {
		this.availablescore = availablescore;
	}

}
