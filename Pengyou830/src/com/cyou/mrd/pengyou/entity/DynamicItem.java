package com.cyou.mrd.pengyou.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;


public class DynamicItem implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int aid = -1;
	private int uid;
	private String nickname;
	private String text;//文字内容
	private DynamicGameItem game;
	private int type;
	private int supportcnt;
	private int commentcnt;
	private long createdtime;
	private int supported;//当前用户是否已经赞过，1-当前用户已赞过，0-当前用户没有赞过
	private boolean isOpen = false;//是否展开了评论
	private String avatar;
	private StringBuilder comment = new StringBuilder();
	private List<DynamicCommentItem> subCommentData = new ArrayList<DynamicCommentItem>() ;
	private int lastcommentid;
	private DynamicPic picture;
	private DynamicPic picturesmall;
	private DynamicPic picturemiddle;
	private int snstag = 0; //是否通过新浪微博关注
    private int bilateral = 0;//是否相互关注
	private boolean loadingComments;
	private int gender = 0; //2:girl 1:boy
	private int cursor = 0;
	private List<DynamicSupporterItem> supportusr = new ArrayList<DynamicSupporterItem>();
	private boolean commentUpdate = false;
	private List<DynamicCommentItem> comments = new ArrayList<DynamicCommentItem>();
	private SpannableStringBuilder mSupporterStr;
	private String star ="",gamestar="" ;//单次动态评分，更新的平均分
	private int[] stardistr;//评分人数
   
	private int pid;//动态发布保存在本地sqllite中的PID
	private int sendStatus = -1;//动态发布的状态
	private String myPicture;//发布动态时本地的图片

	private boolean  needPullDown = true;
	
	private String isremarked,isremarkednum,changeerror,changevalue;
	
	
	
	
	public String getIsremarked() {
		return isremarked;
	}

	public void setIsremarked(String isremarked) {
		this.isremarked = isremarked;
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

	public int getGender() {
		return this.gender;
	}

	public void setGender(int  gd) {
		this.gender = gd;
	}
	
	
	public int[] getStardistr() {
		return stardistr;
	}

	public void setStardistr(int[] stardistr) {
		this.stardistr = stardistr;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAid() {
		return aid;
	}

	public void setAid(int aid) {
		this.aid = aid;
	}

	public String getGamestar() {
		return gamestar;
	}

	public void setGamestar(String gamestar) {
		this.gamestar = gamestar;
	}

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public DynamicGameItem getGame() {
		return game;
	}

	public void setGame(DynamicGameItem game) {
		this.game = game;
	}

	public int getSupportcnt() {
		return supportcnt;
	}

	public void setSupportcnt(int supportcnt) {
		this.supportcnt = supportcnt;
	}

	public int getCommentcnt() {
		return commentcnt;
	}

	public void setCommentcnt(int commentcnt) {
		this.commentcnt = commentcnt;
	}

	public long getCreatedtime() {
		return createdtime;
	}

	public void setCreatedtime(long createdtime) {
		this.createdtime = createdtime;
	}

//	public DynamicComment getComment() {
//		return comment;
//	}
//
//	public void setComment(DynamicComment comment) {
//		this.comment = comment;
//	}

	public int getSupported() {
		return supported;
	}

	public StringBuilder getComment() {
		return comment;
	}

	public void setComment(StringBuilder comment) {
		this.comment = comment;
	}

	public void setSupported(int supported) {
		this.supported = supported;
	}

	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public int getLastcommentid() {
		return lastcommentid;
	}

	public void setLastcommentid(int lastcommentid) {
		this.lastcommentid = lastcommentid;
	}

	public DynamicPic getPicture() {
		return picture;
	}

	public void setPicture(DynamicPic picture) {
		this.picture = picture;
	}

	public DynamicPic getPicturesmall() {
		return picturesmall;
	}

	public void setPicturesmall(DynamicPic picturesmall) {
		this.picturesmall = picturesmall;
	}

	public DynamicPic getPicturemiddle() {
		return picturemiddle;
	}

	public void setPicturemiddle(DynamicPic picturemiddle) {
		this.picturemiddle = picturemiddle;
	}

	public void setSnstag(int snstag) {
		this.snstag = snstag;
	}

	public void setBilateral(int bilateral) {
		this.bilateral = bilateral;
	}

	public int getSnstag() {
		return snstag;
	}

	public int getBilateral() {
		return bilateral;
	}

	public boolean isLoadingComments() {
		return loadingComments;
	}

	public void setLoadingComments(boolean loadingComments) {
		this.loadingComments = loadingComments;
	}

	public List<DynamicCommentItem> getSubCommentData() {
		return subCommentData;
	}

	public void setSubCommentData(List<DynamicCommentItem> subCommentData) {
		this.subCommentData = subCommentData;
	}
	
	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getSendStatus() {
		return sendStatus;
	}

	public void setSendStatus(int sendStatus) {
		this.sendStatus = sendStatus;
	}

	public String getMyPicture() {
		return myPicture;
	}

	public void setMyPicture(String myPicture) {
		this.myPicture = myPicture;
	}

	public int getCursor() {
		return cursor;
	}

	public void setCursor(int cursor) {
		this.cursor = cursor;
	}

	public List<DynamicSupporterItem> getSupportusr() {
		return supportusr;
	}

	public void setSupportusr(List<DynamicSupporterItem> supportusr) {
		if(supportusr != null){
//			StringBuilder sb = new StringBuilder();
//			for(DynamicSupporterItem item : supportusr){
//				if(supportusr.indexOf(item) == 0){
//					sb.append("<font size='26' color='#265fa5'>")
//					  .append(item.getNickname()).append("</>");
////					sb.append(item.getNickname());
//				}else{
//					sb.append("<font size='26' color='#333333'>")
//					  .append(" , ").append("</>").append("<font size='26' color='#265fa5'>")
//					  .append(item.getNickname()).append("</>");
////					sb.append(",").append(item.getNickname());
//				}
//				this.mSupporterStr = sb.toString();
//			}
			this.supportusr = supportusr;
			setmSupporterStr(supportusr);
		}else{
			this.mSupporterStr = null;
		}
	}

	public boolean isCommentUpdate() {
		return commentUpdate;
	}

	public void setCommentUpdate(boolean commentUpdate) {
		this.commentUpdate = commentUpdate;
	}

	public List<DynamicCommentItem> getComments() {
		return comments;
	}

	public void setComments(List<DynamicCommentItem> comments) {
		this.comments = comments;
	}

	public boolean isNeedPullDown() {
		return needPullDown;
	}

	public void setNeedPullDown(boolean needPullDown) {
		this.needPullDown = needPullDown;
	}

	public String getStar() {
		return star;
	}

	public void setStar(String star) {
		this.star = star;
	}

	public SpannableStringBuilder getmSupporterStr() {
		return mSupporterStr;
	}

	public void setmSupporterStr(List<DynamicSupporterItem> supportusr) {
		SpannableStringBuilder ssb = new SpannableStringBuilder();
		int lastLength = 0;
		for(DynamicSupporterItem item : supportusr){
			int index = supportusr.indexOf(item);
			if(index == 0){
//				this.mSupporterStr = "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
//						+ item.getUid()
//						+ "\">"
//				this.mSupporterStr = "<font size='26' color='#265fa5'>"
//						+ item.getNickname()
//						+ "</>"
////						+ "</a>"
//					    ;
				lastLength = item.getNickname().length();
				ssb.append(item.getNickname());
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#265FA5")), 0, lastLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				ssb.setSpan(new MySpan("pengyou://friendinfo?uid=" + String.valueOf(item.getUid())), 0, lastLength, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			}else{
//				this.mSupporterStr  += "<font size='26' color='#333333'>"
//				    +  " , "
//				    +  "</>"
////				    +  "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
////					+ item.getUid()
////					+ "\">"
//					+ "<font size='26' color='#265fa5'>"
//					+ item.getNickname()
//					+ "</>"
////					+ "</a>"
//					;
				ssb.append(",");
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#333333")), lastLength, lastLength + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.append(item.getNickname());
				ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#265FA5")), lastLength + 1,lastLength + 1 + item.getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);				
//				ssb.setSpan(new MySpan("pengyou://friendinfo?uid=" + String.valueOf(item.getUid())), index*lastLength + 1, index*lastLength + 1 + item.getNickname().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				lastLength = lastLength + item.getNickname().length() + 1;
			}
		}
		this.mSupporterStr = ssb;
	}
}
