package com.cyou.mrd.pengyou.entity;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import android.os.Parcel;
import android.os.Parcelable;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GameItem implements Parcelable {
	@JsonProperty
	private String icon; //游戏图标
	private String gamecode; 
	private String identifier;
	private String name;  
	private String frdplay;// 多少好友在玩
	private String rank;
	private String version;
	private String fullurl;
	private String fullsize;
	private int ispublic;
	private long createdate;
	private float star;// 游戏评级
	private String gtname;// :"休闲,冒险", //游戏类型名称(如果多个则用逗号分割)
	private String usrplay;// 多少玩家在玩
	public boolean isShowBar;// 是否显示操作栏
	private String versioncode;// 游戏版本号
	private String source;
	private String downloadcnt;
	private SecurityinfoBean securityinfo;
	private String gametype;
	private String gcacts;
	private long timestamp;
	private String gcid = "";
	private int mMyGameType;//区分我的游戏/我的收藏 1-我的游戏 2-我的收藏
	private String recdate; //推荐日期时间戳
	private String recdesc;//推荐理由
	private String recpic;//推荐图片
	private String localVersion;
	private int todaygcacts;//今日动态个数
	private int noticount;//未读通知数
	private int hastop;//是否存在置顶贴 0:否 1:是
	
	private long recplaydat;//最近一次玩游戏的时间点
	private String recplaydur;//最近一次玩游戏的时长
	//private List<GamePlayRecord> playrecordlist;//该游戏从上次领完积分到现在领积分之间的玩耍的时间段
	private int hasscore;//是否显示金币按钮,1是显示，0是隐藏
	private String dateMsg;//更新时间  如三分钟前 三小时前
	public static Parcelable.Creator<GameItem> CREATOR = new Creator<GameItem>() {

		@Override
		
		public GameItem createFromParcel(Parcel arg0) {
			return new GameItem(arg0.readString()//icon
					, arg0.readString()//gamecode
					,arg0.readString()//identifier
					, arg0.readString()//name
					, arg0.readString()//frdplay
					,arg0.readString()//rank
					, arg0.readString()//version
					, arg0.readString()//fullurl
					,arg0.readString()//fullsize
					, arg0.readInt()//ispublic
					, arg0.readLong()//createdate
					,arg0.readFloat()//star
					,arg0.readString()//mgcid
					,arg0.readString()//localVersion
					,arg0.readInt()
			        ,arg0.readLong()//recplaydat
			        ,arg0.readString()//recplaydur
			        ,arg0.readInt());//hasscore
			
		}

		@Override
		public GameItem[] newArray(int arg0) {
			return new GameItem[arg0];
		}

	};
	
	public GameItem(String icon, String gamecode, String identifier,
			String name, String frdplay, String rank, String version,
			String fullurl, String fullsize, int ispublic, long createdate,
			float star,String mgcid,String localVersion,
			int type, long recplaydat, String recplaydur, int hasscore) {
		this.icon = icon;
		this.gamecode = gamecode;
		this.identifier = identifier;
		this.name = name;
		this.frdplay = frdplay;
		this.rank = rank;
		this.version = version;
		this.fullurl = fullurl;
		this.fullsize = fullsize;
		this.ispublic = ispublic;
		this.createdate = createdate;
		this.star = star;
		this.gcid=mgcid;
		this.localVersion = localVersion;
		this.mMyGameType = type;
		this.recplaydat = recplaydat;
		this.recplaydur = recplaydur;
		this.hasscore = hasscore;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.icon);
		dest.writeString(this.gamecode);
		dest.writeString(this.identifier);
		dest.writeString(this.name);
		dest.writeString(this.frdplay);
		dest.writeString(this.rank);
		dest.writeString(this.version);
		dest.writeString(this.fullurl);
		dest.writeString(this.fullsize);
		dest.writeInt(this.ispublic);
		dest.writeFloat(this.createdate);
		dest.writeFloat(this.star);
		dest.writeString(this.gcid);
		dest.writeString(this.localVersion);
		dest.writeInt(this.mMyGameType);
		dest.writeLong(this.recplaydat);
		dest.writeString(this.recplaydur);
		dest.writeInt(this.hasscore);
	}
	/*
	public void setSecurityinfo(String securityinfo) {
		this.securityinfo = securityinfo;
	}
	
	public String getSecurityinfo(){
		return securityinfo;
	}
	*/
	public void setSecurityinfo(SecurityinfoBean securityinfo) {
		this.securityinfo = securityinfo;
	}
	
	public SecurityinfoBean  getSecurityinfo(){
		return securityinfo;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDownloadcnt() {
		return downloadcnt;
	}

	public void setDownloadcnt(String downloadcnt) {
		this.downloadcnt = downloadcnt;
	}

	public GameItem() {

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

	public String getVersioncode() {
		return versioncode;
	}

	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
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

	public String getGtname() {
		return gtname;
	}

	public String getUsrplay() {
		return usrplay;
	}

	public void setUsrplay(String usrplay) {
		this.usrplay = usrplay;
	}

	public void setGtname(String gtname) {
		this.gtname = gtname;
	}

	public void setFrdplay(String frdplay) {
		this.frdplay = frdplay;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

	public int getIspublic() {
		return ispublic;
	}

	public void setIspublic(int ispublic) {
		this.ispublic = ispublic;
	}

	public float getStar() {
		return star;
	}

	public void setStar(float star) {
		this.star = star;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	public long getCreatedate() {
		return createdate;
	}

	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}

	public String getGametype() {
		return gametype;
	}

	public void setGametype(String gametype) {
		this.gametype = gametype;
	}
	
	public void setGcacts(String gcacts) {
		this.gcacts = gcacts;
	}
	
	public String getGcacts() {
		return gcacts;
	}
	
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	public String getGcid() {
		return gcid;
	}

	public void setGcid(String gcid) {
		this.gcid = gcid;
	}

	public int getmMyGameType() {
		return mMyGameType;
	}

	public void setmMyGameType(int mMyGameType) {
		this.mMyGameType = mMyGameType;
	}

	public boolean isShowBar() {
		return isShowBar;
	}

	public void setShowBar(boolean isShowBar) {
		this.isShowBar = isShowBar;
	}

	public String getRecdesc() {
		return recdesc;
	}

	public void setRecdesc(String recdesc) {
		this.recdesc = recdesc;
	}

	public String getRecpic() {
		return recpic;
	}

	public void setRecpic(String recpic) {
		this.recpic = recpic;
	}

	public String getLocalVersion() {
		return localVersion;
	}

	public void setLocalVersion(String localVersion) {
		this.localVersion = localVersion;
	}

	public int getTodaygcacts() {
		return todaygcacts;
	}

	public void setTodaygcacts(int todaygcacts) {
		this.todaygcacts = todaygcacts;
	}

	public int getNoticount() {
		return noticount;
	}

	public void setNoticount(int noticount) {
		this.noticount = noticount;
	}

	public int getHastop() {
		return hastop;
	}

	public void setHastop(int hastop) {
		this.hastop = hastop;
	}
	
//    public List<GamePlayRecord> getPlayrecordlist() {
//		return playrecordlist;
//	}
//
//	public void setPlayrecordlist(List<GamePlayRecord> playrecordlist) {
//		this.playrecordlist = playrecordlist;
//	}

	//最近一次玩的终止事件，为了在UI上显示最后一次是什么时候玩了这个游戏的
	public long getRecplaydat() {
		return recplaydat;
	}

	public void setRecplaydat(long recplaydat) {
		this.recplaydat = recplaydat;
	}

	//最近一次玩的时间片段的字符串显示，为了在UI上显示最后一次玩了多久的这个游戏
	public String getRecplaydur() {
		return recplaydur;
	}

	public void setRecplaydur(String recplaydur) {
		this.recplaydur = recplaydur;
	}

	//该游戏还有多少金币未领取
	public int getHasscore() {
		return hasscore;
	}

	public void setHasscore(int hasscore) {
		this.hasscore = hasscore;
	}

	public String getRecdate() {
		return recdate;
	}

	public void setRecdate(String recdate) {
		this.recdate = recdate;
	}

	public String getDateMsg() {
		return dateMsg;
	}

	public void setDateMsg(String dateMsg) {
		this.dateMsg = dateMsg;
	}
	
	
}
