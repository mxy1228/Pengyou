package com.cyou.mrd.pengyou.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;


@JsonIgnoreProperties(ignoreUnknown = true)
public class SimilarGameItem implements Parcelable{
	@JsonProperty
	private String icon; //游戏图标
	private String gamecode; 
	private String identifier;
	private String name;  
	private String frdplay;// 多少好友在玩
	private String fullurl;//下载地址
	private String fullsize;
	private String gametype;
	
	public SimilarGameItem() {

	}

	public SimilarGameItem(String icon, String gamecode, String identifier,
			String name, String frdplay,String fullurl, String fullsize,String gametype) {
		this.icon = icon;
		this.gamecode = gamecode;
		this.identifier = identifier;
		this.name = name;
		this.frdplay = frdplay;
		this.fullurl = fullurl;
		this.fullsize = fullsize;
		this.gametype=gametype;
	}
	public static Parcelable.Creator<SimilarGameItem> CREATOR = new Creator<SimilarGameItem>() {

		@Override
		public SimilarGameItem createFromParcel(Parcel arg0) {
			return new SimilarGameItem(arg0.readString(), arg0.readString(),
					arg0.readString(), arg0.readString(), arg0.readString(),
					arg0.readString(), arg0.readString(), arg0.readString());
		}

		@Override
		public SimilarGameItem[] newArray(int arg0) {
			return new SimilarGameItem[arg0];
		}

	};
	
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.icon);
		dest.writeString(this.gamecode);
		dest.writeString(this.identifier);
		dest.writeString(this.name);
		dest.writeString(this.frdplay);
		dest.writeString(this.fullurl);
		dest.writeString(this.fullsize);
		dest.writeString(this.gametype);
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getGamecode() {
		return gamecode;
	}
	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}
	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFrdplay() {
		return frdplay;
	}
	public void setFrdplay(String frdplay) {
		this.frdplay = frdplay;
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
	public String getGametype() {
		return gametype;
	}
	public void setGametype(String gametype) {
		this.gametype = gametype;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


}

