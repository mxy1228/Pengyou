package com.cyou.mrd.pengyou.entity;

import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 系统消息解析
 * 
 * @author wangkang
 * 
 */
public class SystemCountMsgItem {
	
	private static CYLog log = CYLog.getInstance();

	public static final String SYSTEM = "publicnotification";
	public static final String RELATION_CIRCLE_MSG = "messagecountwithlastpostuser";
	public static final String GAME_CIRCLE_MSG = "gcmessagenotify";
	public static final String RECOMMEND_FRIEND = "newcontactnotify";
	
	private static final String CURRENT_FANS_COUNT = "countfans";
	private static final String CURRENT_FOCUS_COUNT = "countfocus";
	private static final String LAST_FOCUS_DYNAMIC_ID = "newactid";//关系圈消息对应的动态ID
	private static final String TYPE = "actiontype";//messagecountwithlastpostuser-关系圈消息/publicnotification-圆点数目
	private static final String UNREAD_COMMENTS_COUNT = "newcomments";//关系圈消息未读数目
	private static final String LAST_COMMENT_UID = "lastuid";
	private static final String AVATAR = "avatar";
	private static final String PACKAGENAME = "identifier";
	private static final String GCID = "gcid";//游戏圈ID
	private static final String GCNOTIS = "gcnotis";//游戏圈未读消息数目
	private static final String RECOMMEND_FRIEND_COUNT = "newcontacts";//猜你认识个数

	private int mCurrentFansCount;
	private int mNewFansCount;//新粉丝数
	private int mEachFocusLastDynamicID;//互相关注的人发的最后一条动态ID
	private int mCurrentFocusCount;
	private String type;//消息类型
	private int mUnreadCommentsCount;//未读关系圈消息数
	private int mLastCommentUID;//最后评论的用户的UID
	private String mAvatar;//最后评论用户的头像
	private int mUnreadLetterCount;//未读私信数目
//	private int mRecommendFriend;//推荐的好友数目
	private int mRecommendFriendCount;//猜你认识数目
	public int getmCurrentFansCount() {
		return mCurrentFansCount;
	}
	public void setmCurrentFansCount(int mCurrentFansCount) {
		this.mCurrentFansCount = mCurrentFansCount;
	}
	public int getmNewFansCount() {
		return mNewFansCount;
	}
	public void setmNewFansCount(int mNewFansCount) {
		this.mNewFansCount = mNewFansCount;
	}
	public int getmEachFocusLastDynamicID() {
		return mEachFocusLastDynamicID;
	}
	public void setmEachFocusLastDynamicID(int mEachFocusLastDynamicID) {
		this.mEachFocusLastDynamicID = mEachFocusLastDynamicID;
	}
	public int getmCurrentFocusCount() {
		return mCurrentFocusCount;
	}
	public void setmCurrentFocusCount(int mCurrentFocusCount) {
		this.mCurrentFocusCount = mCurrentFocusCount;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getmUnreadCommentsCount() {
		return mUnreadCommentsCount;
	}
	public void setmUnreadCommentsCount(int mUnreadCommentsCount) {
		this.mUnreadCommentsCount = mUnreadCommentsCount;
	}
	public int getmLastCommentUID() {
		return mLastCommentUID;
	}
	public void setmLastCommentUID(int mLastCommentUID) {
		this.mLastCommentUID = mLastCommentUID;
	}
	public String getmAvatar() {
		return mAvatar;
	}
	public void setmAvatar(String mAvatar) {
		this.mAvatar = mAvatar;
	}
	public int getmUnreadLetterCount() {
		return mUnreadLetterCount;
	}
	public void setmUnreadLetterCount(int mUnreadLetterCount) {
		this.mUnreadLetterCount = mUnreadLetterCount;
	}
	
	public int getmRecommendFriendCount() {
		return mRecommendFriendCount;
	}
	public void setmRecommendFriendCount(int mRecommendFriendCount) {
		this.mRecommendFriendCount = mRecommendFriendCount;
	}
	//	public int getmRecommendFriend() {
//		return mRecommendFriend;
//	}
//	public void setmRecommendFriend(int mRecommendFriend) {
//		this.mRecommendFriend = mRecommendFriend;
//	}
	/**
	 * 保存到SP
	 */
	public static void save(Map<String, Object> data,boolean fromLogin){
		if(data == null){
			return;
		}
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		if(!data.containsKey("actiontype")){
			return;
		}
		String type = data.get("actiontype").toString();
		if(type.equals(SYSTEM)){
			if(data.containsKey(LAST_FOCUS_DYNAMIC_ID)){
				int lastId = sp.getInt(Params.SP_PARAMS.LAST_EACH_FOCUS_DYNAMIC_ID, 0);
				int curId = Integer.valueOf(data.get(LAST_FOCUS_DYNAMIC_ID).toString());
				if(curId != lastId){
					e.putInt(Params.SP_PARAMS.LAST_EACH_FOCUS_DYNAMIC_ID, Integer.valueOf(data.get(LAST_FOCUS_DYNAMIC_ID).toString()));
					e.commit();
				}
			}
			if(data.containsKey(CURRENT_FANS_COUNT)){
				if(!fromLogin){
					int pre = sp.getInt(Params.SP_PARAMS.CURRENT_FANS_COUNT, 0);
					log.d("pre = "+pre);
					int cur = Integer.valueOf(data.get(CURRENT_FANS_COUNT).toString());
					log.d("cur = "+cur);
					int newCount = cur - pre;
					log.d("new = "+newCount);
					e.putInt(Params.SP_PARAMS.CURRENT_FANS_COUNT, cur);
					if(newCount > 0){
						e.putInt(Params.SP_PARAMS.NEW_FANS_COUNT, newCount);
					}else{
						e.putInt(Params.SP_PARAMS.NEW_FANS_COUNT, 0);
					}
					e.commit();
				}else{
					int cur = Integer.valueOf(data.get(CURRENT_FANS_COUNT).toString());
					e.putInt(Params.SP_PARAMS.CURRENT_FANS_COUNT, cur);
					e.commit();
				}
			}
			if(data.containsKey(CURRENT_FOCUS_COUNT)){
				e.putInt(Params.SP_PARAMS.CURRENT_FOCUS_COUNT, Integer.parseInt(data.get(CURRENT_FOCUS_COUNT).toString()));
				e.commit();
			}
		}else if(type.equals(RELATION_CIRCLE_MSG)){
			if(data.containsKey(UNREAD_COMMENTS_COUNT)){
				e.putInt(Params.SP_PARAMS.NEW_COMMENTS_COUNT, Integer.parseInt(data.get(UNREAD_COMMENTS_COUNT).toString()));
				e.commit();
			}
			if(data.containsKey(LAST_COMMENT_UID)){
				e.putInt(Params.SP_PARAMS.LASTUID, Integer.parseInt(data.get(LAST_COMMENT_UID).toString()));
				e.commit();
			}
			if(data.containsKey(AVATAR)){
				if(data.get(AVATAR) != null){
					e.putString(Params.SP_PARAMS.AVATAR, data.get(AVATAR).toString());
					e.commit();
				}else{
					e.putString(Params.SP_PARAMS.AVATAR, "");
					e.commit();
				}
			}
		}else if(type.equals(GAME_CIRCLE_MSG)){
			if(data.containsKey(PACKAGENAME) && data.containsKey(GCNOTIS)){
				e.putInt(data.get(PACKAGENAME).toString(), Integer.valueOf(data.get(GCNOTIS).toString()));
				e.commit();
			}
		}else if(type.equals(RECOMMEND_FRIEND)){
			if(data.containsKey(RECOMMEND_FRIEND_COUNT)){
				int pre = sp.getInt(Params.SP_PARAMS.RECOMMEND_FRIEND_COUNT, 0);
				e.putInt(Params.SP_PARAMS.RECOMMEND_FRIEND_COUNT, Integer.parseInt(data.get(RECOMMEND_FRIEND_COUNT).toString())+pre);
				e.commit();
			}
		}
	}
	
	
	/**
	 * 从SP中读取
	 * @return
	 */
	public static SystemCountMsgItem get(){
		SystemCountMsgItem item = new SystemCountMsgItem();
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		item.setmAvatar(sp.getString(Params.SP_PARAMS.AVATAR, null));
		item.setmCurrentFansCount(sp.getInt(Params.SP_PARAMS.CURRENT_FANS_COUNT, 0));
		item.setmCurrentFocusCount(sp.getInt(Params.SP_PARAMS.CURRENT_FOCUS_COUNT, 0));
		item.setmEachFocusLastDynamicID(sp.getInt(Params.SP_PARAMS.LAST_EACH_FOCUS_DYNAMIC_ID, 0));
		item.setmLastCommentUID(sp.getInt(Params.SP_PARAMS.LASTUID, 0));
		item.setmNewFansCount(sp.getInt(Params.SP_PARAMS.NEW_FANS_COUNT, 0));
		item.setmUnreadCommentsCount(sp.getInt(Params.SP_PARAMS.NEW_COMMENTS_COUNT, 0));
		item.setmUnreadLetterCount(sp.getInt(Params.SP_PARAMS.UNREAD_LETTER_COUNT, 0));
		item.setmRecommendFriendCount(sp.getInt(Params.SP_PARAMS.RECOMMEND_FRIEND_COUNT, 0));
		return item;
	}
	
	/**
	 * 
	 * @param notify 是否通知页面更新
	 */
	public static void changeGameCircleMsg(boolean notify,Context context,String packageName,int count){
		SharedPreferences sp = context.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(packageName, count);
		e.commit();
		if(notify){
			Intent intent = new Intent(Contants.ACTION.SYSTEM_MSG_ACTION);
			intent.putExtra("type", SystemCountMsgItem.GAME_CIRCLE_MSG);
			context.sendBroadcast(intent);
		}
	}
	
	/**
	 * 更改未读私信数目并通知页面更新
	 * @param change
	 */
	public static void changeUnreadLetterCount(int change,Context context){
		SharedPreferences sp = context.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		int pre = sp.getInt(Params.SP_PARAMS.UNREAD_LETTER_COUNT, 0);
		int cur = pre + change;
		if(cur < 0){
			return;
		}
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.UNREAD_LETTER_COUNT, cur);
		e.commit();
		Intent iSysItem = new Intent(Contants.ACTION.SYSTEM_MSG_ACTION);
		iSysItem.putExtra("type", SystemCountMsgItem.SYSTEM);
		context.sendBroadcast(iSysItem);
	}
	
	
	/**
	 * 将未读私信数目置空
	 */
	public static void clearUnreadLetterCount(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.UNREAD_LETTER_COUNT, 0);
		e.commit();
	}
	
	/**
	 * 将猜你认识数目清空
	 */
	public static void clearRecommendFriendCount(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.RECOMMEND_FRIEND_COUNT, 0);
		e.commit();
	}
	
	/**
	 * 根据包名获取游戏圈未读消息数目
	 * @param packageName
	 * @return
	 */
	public static int getGameCircleMsgCount(String packageName){
		return CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG, Context.MODE_PRIVATE).getInt(packageName, 0);
	}
	
	/**
	 * 将游戏圈的消息数目置空
	 * @param packageName
	 */
	public static void clearGameCircleMsgCount(String packageName){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(packageName, 0);
		e.commit();
		CyouApplication.mAppContext.sendBroadcast(new Intent(Contants.ACTION.UPDATE_GAME_CIRCLE_MSG));
	}
	
	/**
	 * 清除关系圈消息
	 */
	public static void cleanRelationCircleMsg(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.LASTUID, 0);
		e.putInt(Params.SP_PARAMS.NEW_COMMENTS_COUNT, 0);
		e.putString(Params.SP_PARAMS.AVATAR, null);
		e.commit();
	}
	
	/**
	 * 将新粉丝数置空
	 */
	public static void cleanNewFans(){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.NEW_FANS_COUNT, 0);
		e.commit();
	}
	
	/**
	 * 清除互相关注的好友发布的最后一条动态记录
	 */
	public static void cleanLastEachFocusDynmiac(int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.LAST_EACH_FOCUS_DYNAMIC_ID, 0);
		e.commit();
		cleanLastDynmiacRead(uid);
	}
	/**
	 * 保存最后一条动态记录，该记录只有读取后才被清除（用于判断系统消息是否已读）
	 */
	public static void saveLastDynmiacRead(int id, int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG + String.valueOf(uid)
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.LAST_DYNAMIC_READ, id);
		e.commit();
	}
	public static int getLastDynmiacRead(int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG+ String.valueOf(uid)
				,Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.LAST_DYNAMIC_READ, 0);
	}
	/**
	 * 清除最后一条动态记录
	 */
	public static void cleanLastDynmiacRead(int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG+ String.valueOf(uid)
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.LAST_DYNAMIC_READ, 0);
		e.commit();
	}	
	/**
	 * 保存最后一条动态最大值记录，该记录不被清除（用于判断是否系统重复消息）
	 */
	public static void saveLastMaxDynmiacID(int id,int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG+ String.valueOf(uid)
				,Context.MODE_PRIVATE);
		Editor e = sp.edit();
		e.putInt(Params.SP_PARAMS.LAST_MAX_DYNAMIC_ID, id);
		e.commit();
	}
	public static int getLastMaxDynmiacID(int uid){
		SharedPreferences sp = CyouApplication.mAppContext.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG+ String.valueOf(uid)
				,Context.MODE_PRIVATE);
		return sp.getInt(Params.SP_PARAMS.LAST_MAX_DYNAMIC_ID, 0);
	}
}
