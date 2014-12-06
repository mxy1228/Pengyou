package com.cyou.mrd.pengyou.entity;

import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;
public class GamePlayRecord implements Parcelable {
	@JsonProperty
    private long begin;
    private long end;
	public static Parcelable.Creator<GamePlayRecord> CREATOR = new Creator<GamePlayRecord>() {

		@Override
		
		public GamePlayRecord createFromParcel(Parcel arg0) {
			return new GamePlayRecord(arg0.readLong(),arg0.readLong());
		}

		@Override
		public GamePlayRecord[] newArray(int arg0) {
			return new GamePlayRecord[arg0];
		}

	};

	public GamePlayRecord(long begin, long end) {
		// TODO Auto-generated constructor stub
		this.begin = begin;
		this.end = end;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeLong(this.begin);
		dest.writeLong(this.end);
	}

	public long getBegin() {
		return begin;
	}

	public void setBegin(long begin) {
		this.begin = begin;
	}

	public long getEnd() {
		return end;
	}

	public void setEnd(long end) {
		this.end = end;
	}

}
