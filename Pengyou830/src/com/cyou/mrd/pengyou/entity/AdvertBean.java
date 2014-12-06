package com.cyou.mrd.pengyou.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class AdvertBean implements Parcelable {
	private String img;
	private String uri;
	private String name;
	private String desc;
	private String identifier;
	private String gamecode;
	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getGamecode() {
		return gamecode;
	}

	public void setGamecode(String gamecode) {
		this.gamecode = gamecode;
	}

}
