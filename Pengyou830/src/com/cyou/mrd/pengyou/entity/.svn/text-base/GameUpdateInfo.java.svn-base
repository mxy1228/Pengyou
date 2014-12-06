package com.cyou.mrd.pengyou.entity;

import android.os.Parcel;
import android.os.Parcelable;


public class GameUpdateInfo implements Parcelable{

	public String identifier;//包名
	public String name;//游戏名称
	public String icon;//logo
	public String version;//最新版本
	public String cansaveup;//是否为省流量更新。Y/N
	public String patchurl;//patch包地址
	public String patchsize;//patch包大小（字节）
	public String fullurl;//全包地址
	public String fullsize;//全包大小
	public String path;//下载安装包存储路径
	public int state;//下载状态
	public int progress;//下载进度
	
	public static Parcelable.Creator<GameUpdateInfo> CREATOR = new Creator<GameUpdateInfo>() {
		
		@Override
		public GameUpdateInfo[] newArray(int size) {
			return new GameUpdateInfo[size];
		}
		
		@Override
		public GameUpdateInfo createFromParcel(Parcel source) {
			return new GameUpdateInfo(source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readInt()
					, source.readInt());
		}
	};
	
	public GameUpdateInfo(){
	}
	
	public GameUpdateInfo(String identifier
			,String name
			,String icon
			,String version
			,String cansaveup
			,String patchurl
			,String patchsize
			,String fullurl
			,String fullsize
			,String path
			,int state
			,int progress){
		this.identifier = identifier;
		this.name = name;
		this.icon = icon;
		this.version = version;
		this.cansaveup = cansaveup;
		this.patchurl = patchurl;
		this.patchsize = patchsize;
		this.fullurl = fullurl;
		this.fullsize = fullsize;
		this.path = path;
		this.state = state;
		this.progress = progress;
		
	}
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.identifier);
		dest.writeString(this.name);
		dest.writeString(this.icon);
		dest.writeString(this.version);
		dest.writeString(this.cansaveup);
		dest.writeString(this.patchurl);
		dest.writeString(this.patchsize);
		dest.writeString(this.fullurl);
		dest.writeString(this.fullsize);
		dest.writeString(this.path);
		dest.writeInt(this.state);
		dest.writeInt(this.progress);
	}
	
}
