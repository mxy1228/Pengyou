package com.cyou.mrd.pengyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class RecommendFriendItem implements Parcelable{

	private String nickname;
	private String avatar;
	private int gender;
	private int uid;
	private String recmndtips;//推荐理由
	private String game;//玩同款游戏的名称
	private int playnum;//玩过的游戏数量
	
	public static Parcelable.Creator<RecommendFriendItem> CREATOR = new Creator<RecommendFriendItem>() {
		
		@Override
		public RecommendFriendItem[] newArray(int size) {
			return new RecommendFriendItem[size];
		}
		
		@Override
		public RecommendFriendItem createFromParcel(Parcel source) {
			return new RecommendFriendItem(source.readString()
					, source.readString()
					, source.readInt()
					, source.readInt()
					, source.readString()
					, source.readString()
					, source.readInt());
		}
	};
	
	public RecommendFriendItem(){};
	
	private RecommendFriendItem(String nickname
			,String avatar
			,int gender
			,int uid
			,String recmndtips
			,String game
			,int playnum){
		this.nickname = nickname;
		this.avatar = avatar;
		this.gender = gender;
		this.uid = uid;
		this.recmndtips = recmndtips;
		this.game = game;
		this.playnum = playnum;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.nickname);
		dest.writeString(this.avatar);
		dest.writeInt(this.gender);
		dest.writeInt(this.uid);
		dest.writeString(this.recmndtips);
		dest.writeString(this.game);
		dest.writeInt(this.playnum);
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
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getRecmndtips() {
		return recmndtips;
	}
	public void setRecmndtips(String recmndtips) {
		this.recmndtips = recmndtips;
	}
	public String getGame() {
		return game;
	}
	public void setGame(String game) {
		this.game = game;
	}
	public int getPlaynum() {
		return playnum;
	}
	public void setPlaynum(int playnum) {
		this.playnum = playnum;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
