package com.cyou.mrd.pengyou.download;

import android.os.Parcel;
import android.os.Parcelable;

public class DownloadItem implements Parcelable{

	private String mName;
	private String mURL;
	private String mSize;
	private String mLogoURL;
	private int mPercent;
	private int mState;//下载状态
	private String mPackageName;
	private String versionName;
	private long mTotalSize;
	private int mSpeed;
	public String pNumber;
	public Float rate;
	private String gameCode;
	private String path;//存储路径
	private String key;//唯一标示：mPackageName+versionName
	public static final Parcelable.Creator<DownloadItem> CREATOR = new Parcelable.Creator<DownloadItem>() {

		@Override
		public DownloadItem createFromParcel(Parcel source) {
			return new DownloadItem(source.readString()
					, source.readString()
					, source.readString()
					, source.readString()
					, source.readInt()
					, source.readInt()
					, source.readString()
					, source.readLong()
					, source.readInt()
					,source.readString()
					,source.readString()
					,source.readString(),source.readString());
		}

		@Override
		public DownloadItem[] newArray(int size) {
			return new DownloadItem[size];
		}
	};
	
	public DownloadItem(){
		
	}
	
	public String getGameCode() {
		return gameCode;
	}

	public void setGameCode(String gameCode) {
		this.gameCode = gameCode;
	}

	public DownloadItem(String name
			,String url
			,String size
			,String logoUrl
			,int percent
			,int state
			,String packagename
			,long totalSize
			,int speed
			,String mGamecode
			,String path
			,String versionname,String mGameCode){
		this.mName = name;
		this.mURL = url;
		this.mSize = size;
		this.mLogoURL = logoUrl;
		this.mPercent = percent;
		this.mState = state;
		this.mPackageName = packagename;
		this.mTotalSize = totalSize;
		this.mSpeed = speed;
		gameCode=mGamecode;
		this.path = path;
		this.versionName = versionname;
		this.gameCode=mGameCode;
	}
	
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmURL() {
		return mURL;
	}
	public void setmURL(String mURL) {
		this.mURL = mURL;
	}
	public String getmSize() {
		return mSize;
	}

	public void setmSize(String mSize) {
		this.mSize = mSize;
	}

	public String getmLogoURL() {
		return mLogoURL;
	}

	public void setmLogoURL(String mLogoURL) {
		this.mLogoURL = mLogoURL;
	}

	public int getmState() {
		return mState;
	}

	public void setmState(int mState) {
		this.mState = mState;
	}

	public int getmPercent() {
		return mPercent;
	}

	public void setmPercent(int mPercent) {
		this.mPercent = mPercent;
	}
	
	public String getmPackageName() {
		return mPackageName;
	}

	public void setmPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

	public long getmTotalSize() {
		return mTotalSize;
	}

	public void setmTotalSize(long mTotalSize) {
		this.mTotalSize = mTotalSize;
	}

	public int getmSpeed() {
		return mSpeed;
	}

	public void setmSpeed(int mSpeed) {
		this.mSpeed = mSpeed;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public String getVersionName() {
		return versionName;
	}

	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(mName);
		dest.writeString(mURL);
		dest.writeString(mSize);
		dest.writeString(mLogoURL);
		dest.writeInt(mPercent);
		dest.writeInt(mState);
		dest.writeString(mPackageName);
		dest.writeLong(mTotalSize);
		dest.writeInt(mSpeed);
		dest.writeString(gameCode);
		dest.writeString(path);
		dest.writeString(versionName);
		dest.writeString(gameCode);
	}

	public String getKey() {
		key = mPackageName + versionName;
		return key;
	}

}
