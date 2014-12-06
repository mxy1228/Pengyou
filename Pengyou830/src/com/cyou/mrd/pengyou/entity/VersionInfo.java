package com.cyou.mrd.pengyou.entity;

import java.util.ArrayList;
import java.util.List;


public class VersionInfo{
	private String version;
	private List<GameDetailInfo> srclist = new ArrayList<GameDetailInfo>();
	
	public VersionInfo() {
		// TODO Auto-generated constructor stub
	}
	
//	public VersionInfo(Parcel in) {
//		this.version = in.readString();
//		this.srclist = in.readArrayList(ClassLoader.getSystemClassLoader());
//	}
//	
//	public static final Parcelable.Creator<VersionInfo> CREATOR = new Parcelable.Creator<VersionInfo>() {
//
//		@Override
//		public VersionInfo createFromParcel(Parcel source) {
//		
//			return new VersionInfo(source);
//			
//		}
//
//		@Override
//		public VersionInfo[] newArray(int size) {
//			return new VersionInfo[size];
//		}
//	};
	
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(version); 
//		dest.writeTypedList(srclist); 
//	}
//	@Override
//	public int describeContents() {
//		return 0;
//	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List<GameDetailInfo> getSrclist() {
		return srclist;
	}
	public void setSrclist(List<GameDetailInfo> srclist) {
		this.srclist = srclist;
	}
	
	
}
