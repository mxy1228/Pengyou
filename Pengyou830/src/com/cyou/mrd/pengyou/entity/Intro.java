package com.cyou.mrd.pengyou.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyou.mrd.pengyou.download.DownloadItem;

import android.os.Parcel;
import android.os.Parcelable;

public class Intro {

	private String gamecode;
	private String identifier;
	private String icon;
	private String name;
	private long createdate;
	private float star;   
	private String gtname;
	private String version;
	private String fullurl;
	private String fullsize;
	private int downloadcnt;
	private int channel;
	private String source;
	private ArrayList<String> gameshots;
	private ArrayList<String> smallshots;
	private String gamedesc;
	private Map<String,String> securityinfo;
	private Map<String,Integer> stardistr;
	private String versioncode;
	private String published; //是否下架
	private List<VersionInfo> verlist = new ArrayList<VersionInfo>();
	/*
	 * [ {"version":"1.8.1", "srclist":[{"gamecode":"werrrwr","source":"91","securityinfo":{"official":"1","security":"0","adsdisplay":"0","feetype":"0"},"fullsize":4555441},
			                     {"gamecode":"ddddd","source":"baidu","securityinfo":{"official":"1","security":"0","adsdisplay":"0","feetype":"0"},"fullsize":6588554}]
   }]
	 * */
	private int gcid;
	private int gcacts;
    private int comments;
	private int isremarked;//是否评分
	private String isremarkednum;//用户给此游戏评分
	private String changeerror;//返回积分失败与否
	private String changevalue;//游戏评分后的积分
	private String getoncescore;//游戏评分前显示的积分
	private List<FriendItem> friendplaying;
	private int gttid;
//	private float usrstar;
	private String isfavorite;
	private int distinctcmts;
	private String platform; 
    private int downloadscore;//cms传递的每个游戏下载可获取积分
	
	
	public ArrayList<String> getSmallshots() {
		return smallshots;
	}
	public void setSmallshots(ArrayList<String> smallshots) {
		this.smallshots = smallshots;
	}
	public String getPublished() {
		return published;
	}
	public void setPublished(String published) {
		//published="0";//1是发布，0是未发布
		this.published = published;
	}
	
	public String getGetoncescore() {
		return getoncescore;
	}
	public void setGetoncescore(String getoncescore) {
		this.getoncescore = getoncescore;
	}
	public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
    	//platform="1";
        this.platform = platform;
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
	
	public String getIsremarkednum() {
		return isremarkednum;
	}
	public void setIsremarkednum(String isremarkednum) {
		this.isremarkednum = isremarkednum;
	}
	
	public String getChangeerror() {
		return changeerror;
	}
	public void setChangeerror(String changeerror) {
		this.changeerror = changeerror;
	}
	public String getChangevalue() {
		return changevalue;
	}
	public void setChangevalue(String changevalue) {
		this.changevalue = changevalue;
	}
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getCreatedate() {
		return createdate;
	}
	public void setCreatedate(long createdate) {
		this.createdate = createdate;
	}
	public float getStar() {
		return star;
	}
	public void setStar(float star) {
		this.star = star;
	}
	public String getGtname() {
		return gtname;
	}
	public void setGtname(String gtname) {
		this.gtname = gtname;
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
	public int getDownloadcnt() {
		return downloadcnt;
	}
	public void setDownloadcnt(int downloadcnt) {
		this.downloadcnt = downloadcnt;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	public ArrayList<String> getGameshots() {
		return gameshots;
	}
	public void setGameshots(ArrayList<String> gameshots) {
		this.gameshots = gameshots;
	}
	public String getGamedesc() {
		return gamedesc;
	}
	public void setGamedesc(String gamedesc) {
		this.gamedesc = gamedesc;
	}
	public int getComments() {
		return comments;
	}
	public void setComments(int comments) {
		this.comments = comments;
	}

	public List<FriendItem> getFriendplaying() {
		return friendplaying;
	}
	public void setFriendplaying(List<FriendItem> friendplaying) {
		this.friendplaying = friendplaying;
	}
	public int getGttid() {
		return gttid;
	}
	public void setGttid(int gttid) {
		this.gttid = gttid;
	}
//	public float getUsrstar() {
//		return usrstar;
//	}
//	public void setUsrstar(float usrstar) {
//		this.usrstar = usrstar;
//	}
	

	public String getIsfavorite() {
		return isfavorite;
	}
	public void setIsfavorite(String isfavorite) {
		this.isfavorite = isfavorite;
	}
	public GameItem toGameItem(){
		GameItem item = new GameItem();
		item.setCreatedate(this.createdate);
		item.setGamecode(this.gamecode);
		item.setIcon(this.icon);
		item.setIdentifier(this.identifier);
		item.setName(this.name);
		return item;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	public int getDistinctcmts() {
		return distinctcmts;
	}
	public void setDistinctcmts(int distinctcmts) {
		this.distinctcmts = distinctcmts;
	}


	public int getGcid() {
		return gcid;
	}
	public void setGcid(int gcid) {
		this.gcid = gcid;
	}

	public Map<String, String> getSecurityinfo() {		
		return securityinfo;
	}
	public void setSecurityinfo(Map<String, String> securityinfo) {
		this.securityinfo = securityinfo;
	}

	public Map<String, Integer> getStardistr() {
//		stardistr=new HashMap<String, Integer>();
//		stardistr.put("s5", 200);
//		stardistr.put("s4", 200);
//		stardistr.put("s3", 200);
//		stardistr.put("s2", 200);
//		stardistr.put("s1", 200);
		
		return stardistr;
	}
	public void setStardistr(Map<String, Integer> stardistr) {
		this.stardistr = stardistr;
	}
	
	public List<VersionInfo> getVerlist() {
		
		return verlist;
	}
	public void setVerlist(List<VersionInfo> verlist) {
		this.verlist = verlist;
	}
	public int getGcacts() {
		return gcacts;
	}
	public void setGcacts(int gcacts) {
		this.gcacts = gcacts;
	}
	public int getIsremarked() {
		return isremarked;
	}
	public void setIsremarked(int isremarked) {
		this.isremarked = isremarked;
	}

	public String getKey() {
		
		return identifier+version;
	}
	public String getVersioncode() {
		return versioncode;
	}
	public void setVersioncode(String versioncode) {
		this.versioncode = versioncode;
	}
	public int getDownloadscore() {
		return downloadscore;
	}
	public void setDownloadscore(int downloadscore) {
		this.downloadscore = downloadscore;
	}
	
}
