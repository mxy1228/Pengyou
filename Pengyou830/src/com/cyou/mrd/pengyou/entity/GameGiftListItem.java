package com.cyou.mrd.pengyou.entity;

public class GameGiftListItem {
	private long giftid;
	private String giftname;
	private int giftnum;
	private int lastnum;
	private String usergift = "";
	private long begintime;
	private long endtime;
	private GameGiftListGameInfoItem game;
	private String giftdesc = "";
	public long getGiftid() {
		return giftid;
	}
	public void setGiftid(long giftid) {
		this.giftid = giftid;
	}
	public String getGiftname() {
		return giftname;
	}
	public void setGiftname(String giftname) {
		this.giftname = giftname;
	}
	public int getGiftnum() {
		return giftnum;
	}
	public void setGiftnum(int giftnum) {
		this.giftnum = giftnum;
	}
	public int getLastnum() {
		return lastnum;
	}
	public void setLastnum(int lastnum) {
		this.lastnum = lastnum;
	}
	public String getUsergift() {
		return usergift;
	}
	public void setUsergift(String usergift) {
		this.usergift = usergift;
	}
	public GameGiftListGameInfoItem getGame() {
		return game;
	}
	public void setGame(GameGiftListGameInfoItem game) {
		this.game = game;
	}
	public long getEndtime() {
		return endtime;
	}
	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}
	public long getBegintime() {
		return begintime;
	}
	public void setBegintime(long begintime) {
		this.begintime = begintime;
	}
	public String getGiftdesc() {
		return giftdesc;
	}
	public void setGiftdesc(String giftdesc) {
		this.giftdesc = giftdesc;
	}
}
