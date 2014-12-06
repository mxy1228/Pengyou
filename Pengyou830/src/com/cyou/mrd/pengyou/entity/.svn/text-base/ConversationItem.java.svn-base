package com.cyou.mrd.pengyou.entity;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 会话组
 * @author xumengyang
 *
 */
public class ConversationItem implements Parcelable{

	private int uid;
	private long time;
	private String lastLetter;
	private int unreadLetterCount;
	private String nickname;
	private String avatar;
	
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getLastLetter() {
		return lastLetter;
	}
	public void setLastLetter(String lastLetter) {
		this.lastLetter = lastLetter;
	}
	public int getUnreadLetterCount() {
		return unreadLetterCount;
	}
	public void setUnreadLetterCount(int unreadLetterCount) {
		this.unreadLetterCount = unreadLetterCount;
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
	
	public String toString(){
		JSONObject obj = new JSONObject();
		try {
			obj.put("uid", this.uid);
			obj.put("time", this.time);
			obj.put("lastLetter", this.lastLetter);
			obj.put("unreadLetterCount", this.unreadLetterCount);
			obj.put("nickname", this.nickname);
			obj.put("avatar", this.avatar);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return obj.toString();
	}
	
	public ConversationItem(){
		
	}
	
	public ConversationItem(int uid
			,long time
			,String lastLetter
			,int unreadLetterCount
			,String nickname
			,String avatar){
		this.uid = uid;
		this.time = time;
		this.lastLetter = lastLetter;
		this.unreadLetterCount = unreadLetterCount;
		this.nickname = nickname;
		this.avatar = avatar;
	}
	
	public static Parcelable.Creator<ConversationItem> CREATOR = new Creator<ConversationItem>() {
		
		@Override
		public ConversationItem[] newArray(int size) {
			return new ConversationItem[size];
		}
		
		@Override
		public ConversationItem createFromParcel(Parcel source) {
			return new ConversationItem(source.readInt()
					, source.readLong()
					, source.readString()
					, source.readInt()
					, source.readString()
					, source.readString());
		}
	};
	
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(uid);
		dest.writeLong(time);
		dest.writeString(lastLetter);
		dest.writeInt(unreadLetterCount);
		dest.writeString(nickname);
		dest.writeString(avatar);
	}
}
