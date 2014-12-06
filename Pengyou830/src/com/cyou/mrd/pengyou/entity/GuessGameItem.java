package com.cyou.mrd.pengyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class GuessGameItem implements Parcelable{

	private String name; //游戏名称
	private String gamecode;//游戏代码
	private String gametype; //游戏类型
	private String identifier; //游戏标识
	private String icon;//游戏图标
	private String fullurl;//kdjowe.com/sdfwer.apk", //下载地址
	private String fullsize;//大小
	private String frdplay;//好友在玩的个数
	private String version;
	private String recdesc;//推荐理由
	
	public GuessGameItem(){
		
	}
	
	public GuessGameItem(String name
			,String gamecode
			,String gametype
			,String identifier
			,String icon
			,String fullurl
			,String fullsize
			,String frdplay
			,String version
			,String recdesc){
		this.name = name;
		this.gamecode = gamecode;
		this.gametype = gametype;
		this.identifier = identifier;
		this.icon = icon;
		this.fullurl = fullurl;
		this.fullsize = fullsize;
		this.frdplay = frdplay;
		this.version = version;
		this.recdesc = recdesc;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.name);
		dest.writeString(this.gamecode);
		dest.writeString(this.gametype);
		dest.writeString(this.identifier);
		dest.writeString(this.icon);
		dest.writeString(this.fullurl);
		dest.writeString(this.fullsize);
		dest.writeString(this.frdplay);
		dest.writeString(this.version);
		dest.writeString(this.recdesc);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getFullurl() {
		return fullurl;
	}
	public void setFullurl(String fullurl) {
		this.fullurl = fullurl;
	}
	public String getFullsize() {
		return fullsize;
	}
	public void setFullsize(String fullsize) {
		this.fullsize = fullsize;
	}
	public String getFrdplay() {
		return frdplay;
	}
	public void setFrdplay(String frdplay) {
		this.frdplay = frdplay;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getRecdesc() {
		return recdesc;
	}

	public void setRecdesc(String recdesc) {
		this.recdesc = recdesc;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public static Parcelable.Creator<GuessGameItem> CREATOR = new Creator<GuessGameItem>() {
		
		@Override
		public GuessGameItem[] newArray(int size) {
			return new GuessGameItem[size];
		}
		
		@Override
		public GuessGameItem createFromParcel(Parcel source) {
			return new GuessGameItem(source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString());
		}
	};
}
