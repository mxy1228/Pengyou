package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ConversationItem;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.entity.UnreadUserLetterInfo;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;

public class LetterDao {
	private CYLog log = CYLog.getInstance();
	
	public static final int PAGE_SIZE = 20;

	private Context mContext;
	private LetterDBHelper mDBHelper;
	
	public static final Object lock = new Object();
	
	public LetterDao(Context context){
		this.mContext = context;
		mDBHelper = new LetterDBHelper(mContext);
	}
	
	public String getTableName(int uid){
		return new StringBuilder().append(Contants.TABLE_NAME.LETTER).append("_").append(uid).toString();
	}
	
	private void checkTable(int uid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "CREATE TABLE IF NOT EXISTS "+getTableName(uid)+
					" ("+LetterDBHelper.LETTER.ID+" integer primary key autoincrement," +
	        		""+LetterDBHelper.LETTER.FROM+" integer,"+
	        		""+LetterDBHelper.LETTER.TO+" integer,"+
	        		""+LetterDBHelper.LETTER.CREATEDATE+" long,"+
	        		""+LetterDBHelper.LETTER.CONTENT+" char,"+
	        		""+LetterDBHelper.LETTER.NICKNAME+" char,"+
	        		""+LetterDBHelper.LETTER.AVATAR+" char,"+
	        		""+LetterDBHelper.LETTER.ISREAD+" integer,"+//新增
	        		""+LetterDBHelper.LETTER.TA_UID+" integer,"+//新增
	        		""+LetterDBHelper.LETTER.MSG_ID+" char,"+//新增
	        		""+LetterDBHelper.LETTER.GAME_NAME+" char,"+
	        		""+LetterDBHelper.LETTER.RATING+" float,"+
	        		""+LetterDBHelper.LETTER.ICON+" char,"+
	        		""+LetterDBHelper.LETTER.SEND_SUCCESS+" char,"+
	        		""+LetterDBHelper.LETTER.TYPE+" integer,"+
	        		""+LetterDBHelper.LETTER.MSGSEQ+" char,"+
	        		""+LetterDBHelper.LETTER.SEND_STATE+" integer,"+
	        		""+LetterDBHelper.LETTER.GAME_CODE+" char)";
			database.execSQL(sql);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 检查uid的私信表里是否已存在msgseq对应的私信记录
	 * @param msgseq
	 * @param uid
	 * @return true = 已存在/false = 不存在
	 */
	private boolean checkMsgSeq(String msgseq,int uid){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String sql = "SELECT COUNT(*) FROM "+getTableName(uid)+" WHERE "+LetterDBHelper.LETTER.MSGSEQ+" = ?";
				c = database.rawQuery(sql, new String[]{msgseq});
				while(c.moveToNext()){
					return c.getInt(0) != 0;
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(c != null){
				c.close();
			}
			
		}
		return false;
	}
	
	/**
	 * 批量插入同一UID的私信
	 * @param queue
	 * @param tauid
	 * @result true = 全部插入成功 false = 全部插入失败
	 */
	public int insert(Queue<MyMessageItem> queue){
		int successCount = 0;
		if(queue == null || queue.isEmpty()){
			log.e("queue is null or empty");
			return 0;
		}
		MyMessageItem topItem = queue.peek();
		if(topItem == null){
			log.e("topItem is null");
			return 0;
		}
		if(topItem.getTauid() == 0){
			if(PYVersion.DEBUG){
				log.e("收到uid=0的私信消息");
			}
			return 0;
		}
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				checkTable(topItem.getTauid());
				for(MyMessageItem item : queue){
					if(checkMsgSeq(item.getMsgseq(), item.getTauid())){
						log.e(getTableName(item.getTauid())+"中已存在"+item.getMsgseq());
						continue;
					}
					database = mDBHelper.getWritableDatabase();
					String sql = "INSERT INTO "+getTableName(topItem.getTauid())+" ("+LetterDBHelper.LETTER.FROM+"" +
							","+LetterDBHelper.LETTER.TO+
							","+LetterDBHelper.LETTER.CREATEDATE+
							","+LetterDBHelper.LETTER.CONTENT+
							","+LetterDBHelper.LETTER.NICKNAME+
							","+LetterDBHelper.LETTER.AVATAR+
							","+LetterDBHelper.LETTER.GAME_CODE+
							","+LetterDBHelper.LETTER.TA_UID+
							","+LetterDBHelper.LETTER.ISREAD+
							","+LetterDBHelper.LETTER.MSG_ID+
							","+LetterDBHelper.LETTER.MSGSEQ+
							","+LetterDBHelper.LETTER.SEND_STATE+
							","+LetterDBHelper.LETTER.SEND_SUCCESS+") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
					int read = item.getFrom() == UserInfoUtil.getCurrentUserId() ? Contants.LETTER_IS_READ.YES : Contants.LETTER_IS_READ.NO;
					database.execSQL(sql, new Object[]{item.getFrom()
							,item.getTo()
							,item.getCreatetime()
							,item.getContent()
							,item.getNickname()
							,item.getAvatar()
							,item.getGamecode()
							,item.getTauid()
							,read
							,item.getMsgid()
							,item.getMsgseq()
							,LetterDBHelper.SEND_STATE.SEND_SUCCESS
							,Contants.LETTER_SEND_SUCCESS.NO});
					log.d("插入私信:"+item.getTauid()+":"+item.getContent());
					//发送广播通知IM页面更新
					successCount++;
					Intent intent = new Intent(Contants.ACTION.NEW_CHAT);
					intent.putExtra(Params.CHAT.FROM, item.getTauid());
					intent.putExtra(Params.CHAT.TO, UserInfoUtil.getCurrentUserId());
					intent.putExtra(Params.CHAT.ITEM, item);
					intent.putExtra(Params.CHAT.TIME, item.getCreatetime());
					CyouApplication.mAppContext.sendBroadcast(intent);
				}
			}
			if(topItem != null){
				if(topItem.getFrom() == UserInfoUtil.getCurrentUserId()){
					updateRecentUser(topItem.getTauid(),topItem.getCreatetime());
				}else{
					updateRecentUser(topItem.getTauid(),topItem.getAvatar(),topItem.getNickname(),topItem.getCreatetime());
				}
			}
			return successCount;
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
		return 0;
	}
	
	public void insert(MyMessageItem item){
		if(item.getTauid() == 0){
			if(PYVersion.DEBUG){
				Toast.makeText(mContext, "Error:收到uid=0的私信消息", Toast.LENGTH_LONG).show();
			}
			return;
		}
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				checkTable(item.getTauid());
				if(!TextUtils.isEmpty(item.getMsgseq()) && checkMsgSeq(item.getMsgseq(), item.getTauid())){
					log.e(getTableName(item.getTauid())+"中已存在"+item.getMsgseq());
					return;
				}
				database = mDBHelper.getWritableDatabase();
				String sql = "INSERT INTO "+getTableName(item.getTauid())+" ("+LetterDBHelper.LETTER.FROM+"" +
						","+LetterDBHelper.LETTER.TO+
						","+LetterDBHelper.LETTER.CREATEDATE+
						","+LetterDBHelper.LETTER.CONTENT+
						","+LetterDBHelper.LETTER.NICKNAME+
						","+LetterDBHelper.LETTER.AVATAR+
						","+LetterDBHelper.LETTER.GAME_CODE+
						","+LetterDBHelper.LETTER.TA_UID+
						","+LetterDBHelper.LETTER.ISREAD+
						","+LetterDBHelper.LETTER.MSG_ID+
						","+LetterDBHelper.LETTER.MSGSEQ+
						","+LetterDBHelper.LETTER.SEND_STATE+
						","+LetterDBHelper.LETTER.SEND_SUCCESS+") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
				int read = item.getFrom() == UserInfoUtil.getCurrentUserId() ? Contants.LETTER_IS_READ.YES : Contants.LETTER_IS_READ.NO;
				String success = null;
				if(item.getSendSuccess() != null){
					success = item.getSendSuccess().equals(Contants.LETTER_SEND_SUCCESS.YES) ? Contants.LETTER_SEND_SUCCESS.YES : Contants.LETTER_SEND_SUCCESS.NO;
				}else{
					success = Contants.LETTER_SEND_SUCCESS.NO;
				}
				database.execSQL(sql, new Object[]{item.getFrom()
						,item.getTo()
						,item.getCreatetime()
						,item.getContent()
						,item.getNickname()
						,item.getAvatar()
						,item.getGamecode()
						,item.getTauid()
						,read
						,item.getMsgid()
						,item.getMsgseq()
						,item.getSendState()
						,success});
				if(item.getFrom() == UserInfoUtil.getCurrentUserId()){
					updateRecentUser(item.getTauid(),item.getCreatetime());
				}else{
					updateRecentUser(item.getTauid(),item.getAvatar(),item.getNickname(),item.getCreatetime());
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
//	public void insert(ContentValues values){
//		int ta_uid = values.getAsInteger(LetterDBHelper.LETTER.TA_UID);
//		log.i("ta_uid = "+ta_uid);
//		if(ta_uid == 0){
//			if(PYVersion.DEBUG){
//				Toast.makeText(mContext, "Error:收到uid=0的私信消息", Toast.LENGTH_LONG).show();
//			}
//			return;
//		}
//		SQLiteDatabase database = null;
//		try {
//			database = mDBHelper.getWritableDatabase();
//			database.insert(getTableName(ta_uid), null, values);
//		} catch (Exception e) {
//			log.e(e);
//		}finally{
//			if(database != null){
//				database.close();
//			}
//		}
//	}
	
	/**
	 * 查询该uid是否已在最近联系人中存在记录
	 * true--存在
	 * false--不存在
	 * @param uid
	 */
	private boolean isRecentUserExists(int uid){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			synchronized (lock) {
				database = mDBHelper.getWritableDatabase();
				String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.RECENT_USER+" WHERE uid = ?";
				c = database.rawQuery(sql, new String[]{String.valueOf(uid)});
				while(c.moveToNext()){
					return c.getInt(0) != 0;
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(c != null){
				c.close();
			}
			
		}
		return false;
	}
	
	
	private void updateRecentUser(int uid,long time){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				if(isRecentUserExists(uid)){
					database = mDBHelper.getWritableDatabase();
					String updateSql = "UPDATE "+Contants.TABLE_NAME.RECENT_USER+" SET time = ? WHERE uid = ?";
					database.execSQL(updateSql, new Object[]{time,uid});
				}else{
					database = mDBHelper.getWritableDatabase();
					String insertSql = "INSERT INTO "+Contants.TABLE_NAME.RECENT_USER+" (uid,time) VALUES (?,?)";
					database.execSQL(insertSql, new Object[]{uid,time});
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	public void updateRecentUser(int uid,String avatar,String nickname,long time){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				if(isRecentUserExists(uid)){
					database = mDBHelper.getWritableDatabase();
					String updateSql = "UPDATE "+Contants.TABLE_NAME.RECENT_USER+" SET time = ?,avatar = ?,nickname = ? WHERE uid = ?";
					database.execSQL(updateSql, new Object[]{time,avatar,nickname,uid});
				}else{
					database = mDBHelper.getWritableDatabase();
					String insertSql = "INSERT INTO "+Contants.TABLE_NAME.RECENT_USER+" (uid,time,avatar,nickname) VALUES (?,?,?,?)";
					database.execSQL(insertSql, new Object[]{uid,time,avatar,nickname});
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	public void updateRecentUser(int uid,String avatar,String nickname){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				if(isRecentUserExists(uid)){
					database = mDBHelper.getWritableDatabase();
					String updateSql = "UPDATE "+Contants.TABLE_NAME.RECENT_USER+" SET avatar = ?,nickname = ? WHERE uid = ?";
					database.execSQL(updateSql, new Object[]{avatar,nickname,uid});
				}else{
					database = mDBHelper.getWritableDatabase();
					String insertSql = "INSERT INTO "+Contants.TABLE_NAME.RECENT_USER+" (uid,avatar,nickname) VALUES (?,?,?,?)";
					database.execSQL(insertSql, new Object[]{uid,avatar,nickname});
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	/**
	 * 根据preCursor和lastCursor的区间查询私信
	 * @param lastcursor
	 * @param precursor
	 * @param uid
	 * @return
	 */
	public List<MyMessageItem> selectLetterDescByCursor(long lastcursor,long precursor,int uid){
		List<MyMessageItem> list = new ArrayList<MyMessageItem>();
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			synchronized (lock) {
				checkTable(uid);
				database = mDBHelper.getReadableDatabase();
				String sql = "SELECT from_uid,to_uid,createdate,content,nickname,avatar,gamecode,gamename,rating,icon,isread,ta_uid,msgId,sendSuccess,msgseq,send_state FROM "+getTableName(uid)+" WHERE createdate > ? AND createdate <= ? ORDER BY createdate ASC";
				c = database.rawQuery(sql, new String[]{String.valueOf(precursor),String.valueOf(lastcursor)});
				while(c.moveToNext()){
					MyMessageItem item = new MyMessageItem();
					item.setFrom(c.getInt(0));
					item.setTo(c.getInt(1));
					item.setCreatetime(c.getLong(2));
					item.setContent(c.getString(3));
					item.setNickname(c.getString(4));
					item.setAvatar(c.getString(5));
					item.setGamecode(c.getString(6));
					item.setGamename(c.getString(7));
					item.setRating(c.getFloat(8));
					item.setIcon(c.getString(9));
					item.setIsread(c.getInt(10));
					item.setTauid(c.getInt(11));
					item.setMsgid(c.getString(12));
					item.setSendSuccess(c.getString(13));
					if(item.getGamecode() == null || TextUtils.isEmpty(item.getGamecode())){
						item.setType(Contants.CHAT_TYPE.TEXT);
					}else{
						item.setType(Contants.CHAT_TYPE.GAME);
					}
					item.setMsgseq(c.getString(14));
					item.setSendState(c.getInt(15));
					list.add(item);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(c != null){
				c.close();
			}
		}
		return list;
	}
	
	/**
	 * 根据uid查询PAGE_SIZE条聊天记录
	 * @param uid
	 * @return
	 */
	public List<MyMessageItem> selectDataByUID(int uid,long cursor){
		List<MyMessageItem> list = new ArrayList<MyMessageItem>();
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			synchronized (lock) {
				checkTable(uid);
				database = mDBHelper.getReadableDatabase();
				String sql = null;
				if(cursor == 0){
					sql = "SELECT from_uid,to_uid,createdate,content,nickname,avatar,gamecode,gamename,rating,icon,isread,ta_uid,msgId,sendSuccess,msgseq,send_state FROM "+getTableName(uid)+" ORDER BY createdate DESC LIMIT "+PAGE_SIZE;
					c = database.rawQuery(sql, null);
				}else{
					sql = "SELECT from_uid,to_uid,createdate,content,nickname,avatar,gamecode,gamename,rating,icon,isread,ta_uid,msgId,sendSuccess,msgseq,send_state FROM "+getTableName(uid)+" WHERE createdate < ? ORDER BY createdate DESC LIMIT 20";
					c = database.rawQuery(sql, new String[]{String.valueOf(cursor)});
				}
				while(c.moveToNext()){
					MyMessageItem item = new MyMessageItem();
					item.setFrom(c.getInt(0));
					item.setTo(c.getInt(1));
					item.setCreatetime(c.getLong(2));
					item.setContent(c.getString(3));
					item.setNickname(c.getString(4));
					item.setAvatar(c.getString(5));
					item.setGamecode(c.getString(6));
					item.setGamename(c.getString(7));
					item.setRating(c.getFloat(8));
					item.setIcon(c.getString(9));
					item.setIsread(c.getInt(10));
					item.setTauid(c.getInt(11));
					item.setMsgid(c.getString(12));
					item.setSendSuccess(c.getString(13));
					if(item.getGamecode() == null || TextUtils.isEmpty(item.getGamecode())){
						item.setType(Contants.CHAT_TYPE.TEXT);
					}else{
						item.setType(Contants.CHAT_TYPE.GAME);
					}
					item.setMsgseq(c.getString(14));
					item.setSendState(c.getInt(15));
					list.add(item);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(c != null){
				c.close();
			}
			if(database != null){
				database.close();
			}
			
		}
		return list;
	}
	
	private List<UserInfo> selectRecentUser(){
		SQLiteDatabase database = null;
		Cursor cUser = null;
		List<UserInfo> users = new ArrayList<UserInfo>();
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String userSql = "SELECT uid,time,avatar,nickname FROM "+Contants.TABLE_NAME.RECENT_USER+" ORDER BY time DESC";
				cUser = database.rawQuery(userSql, null);
				while(cUser.moveToNext()){
					UserInfo info = new UserInfo();
					info.setUid(cUser.getInt(0));
					info.setPicture(cUser.getString(2));
					info.setNickname(cUser.getString(3));
					users.add(info);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(cUser != null){
				cUser.close();
			}
			
		}
		return users;
	}
	
	private MyMessageItem selectLastLetterByUid(int uid){
		SQLiteDatabase database = null;
		Cursor cLetter = null;
		MyMessageItem item = new MyMessageItem();
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String letterSql = "SELECT from_uid,to_uid,MAX(createdate),content,nickname,avatar,gamecode,gamename,rating,icon,isread,ta_uid,msgId,msgseq FROM "+getTableName(uid);
				cLetter = database.rawQuery(letterSql, null);
				while(cLetter.moveToNext()){
					item.setCreatetime(cLetter.getLong(2));
					item.setContent(cLetter.getString(3));
					item.setMsgseq(cLetter.getString(13));
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(cLetter != null){
				cLetter.close();
			}
			
		}
		return item;
	}
	
	
	/**
	 * 查询聊天会话组
	 * @return
	 */
	public List<ConversationItem> selectConversations(){
		SQLiteDatabase database = null;
		Cursor cUnreadLetter = null;
		List<ConversationItem> list = new ArrayList<ConversationItem>();
		try {
			synchronized (lock) {
				List<UserInfo> users = selectRecentUser();
				database = mDBHelper.getReadableDatabase();
				for(UserInfo user : users){
					ConversationItem con = new ConversationItem();
					con.setUid(user.getUid());
					con.setAvatar(user.getPicture());
					con.setNickname(user.getNickname());
					String unreadSql = "SELECT COUNT(*) FROM "+getTableName(con.getUid())+" WHERE isread = "+Contants.LETTER_IS_READ.NO;
					cUnreadLetter = database.rawQuery(unreadSql, null);
					int unreadCount = 0;
					while(cUnreadLetter.moveToNext()){
						unreadCount = cUnreadLetter.getInt(0);
					}
					con.setUnreadLetterCount(unreadCount);
					list.add(con);
				}
				database.close();
				for(ConversationItem item : list){
					MyMessageItem i = selectLastLetterByUid(item.getUid());
					item.setLastLetter(i.getContent());
					item.setTime(i.getCreatetime());
				}
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(cUnreadLetter != null){
				cUnreadLetter.close();
			}
			
		}
		return list;
	}

	
	/**
	 * 更新用户信息
	 * @param item
	 */
	public void updateUserInfo(int uid,String nickName,String avatar){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				checkTable(uid);
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+getTableName(uid)+" SET nickname = ?,avatar = ? WHERE from_uid = ?";
				database.execSQL(sql, new Object[]{nickName,avatar,uid});
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	/**
	 * 更新游戏信息
	 * @param item
	 */
	public void updateGameInfo(MyMessageItem item){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				checkTable(item.getTauid());
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+getTableName(item.getTauid()) + " SET gamename = ?,rating = ?,icon = ? WHERE gamecode = ?";
				database.execSQL(sql,new Object[]{item.getGamename(),item.getRating(),item.getIcon(),item.getGamecode()});
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	/**
	 * 根据uid删除聊天记录
	 * @param uid
	 */
	public void deleteByUid(int uid){
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		try {
			synchronized (lock) {
				String sql = "DROP TABLE "+getTableName(uid);
				database.execSQL(sql);
				String delUserSql = "DELETE FROM "+Contants.TABLE_NAME.RECENT_USER+" WHERE uid = ?";
				database.execSQL(delUserSql, new Object[]{uid});
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			database.close();
		}
	}
	
	/**
	 * 将该uid私信表里面的信息全部标记为已读
	 * @param uid
	 */
	public void markRead(int uid){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+getTableName(uid)+" SET isread = "+Contants.LETTER_IS_READ.YES;
				database.execSQL(sql);
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	/**
	 * 获取私信总条数
	 * @return
	 */
	public int count(){
		SQLiteDatabase database = null;
		Cursor userC = null;
		Cursor letterC = null;
		int count = 0;
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String userSql = "SELECT uid,time FROM "+Contants.TABLE_NAME.RECENT_USER+" ORDER BY time DESC";
				userC = database.rawQuery(userSql, null);
				while(userC.moveToNext()){
					int uid = userC.getInt(0);
					String letterSql = "SELECT COUNT(*) FROM "+getTableName(uid);
					database = mDBHelper.getReadableDatabase();
					letterC = database.rawQuery(letterSql, null);
					while(letterC.moveToNext()){
						count += letterC.getInt(0);
					}
				}
			}
			return count;
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(userC != null){
				userC.close();
			}
			if(letterC != null){
				letterC.close();
			}
			
		}
		return count;
	}
	
	/**
	 * 统计未读消息数目
	 * @return
	 */
	public int countUnread(){
		SQLiteDatabase database = null;
		Cursor userC = null;
		Cursor letterC = null;
		int count = 0;
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String userSql = "SELECT uid,time FROM "+Contants.TABLE_NAME.RECENT_USER+" ORDER BY time DESC";
				userC = database.rawQuery(userSql, null);
				while(userC.moveToNext()){
					int uid = userC.getInt(0);
					String letterSql = "SELECT COUNT(*) FROM "+getTableName(uid)+" WHERE isread = "+Contants.LETTER_IS_READ.NO;
					database = mDBHelper.getReadableDatabase();
					letterC = database.rawQuery(letterSql, null);
					while(letterC.moveToNext()){
						count += letterC.getInt(0);
					}
				}
			}
			return count;
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(userC != null){
				userC.close();
			}
			if(letterC != null){
				letterC.close();
			}
			
		}
		return count;
	}
	
	/**
	 * 统计所有未读私信数及用户数
	 * @return
	 */
	public UnreadUserLetterInfo countUnreadLetterAndUser(){
		UnreadUserLetterInfo info = new UnreadUserLetterInfo();
		SQLiteDatabase database = null;
		Cursor cUser = null;
		Cursor cLetter = null;
		int userCount = 0;
		int letterCount = 0;
		try {
			synchronized (lock) {
				database = mDBHelper.getReadableDatabase();
				String userSql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				cUser = database.rawQuery(userSql, null);
				while(cUser.moveToNext()){
					int uid = cUser.getInt(0);
					String letterSql = "SELECT COUNT(*) FROM "+getTableName(uid)+" WHERE isread = "+Contants.LETTER_IS_READ.NO;
					database = mDBHelper.getReadableDatabase();
					cLetter = database.rawQuery(letterSql, null);
					while(cLetter.moveToNext()){
						int count = cLetter.getInt(0);
						if(count != 0){
							userCount += 1;
							letterCount += count;
						}
					}
				}
				info.setmTotalUnreadUser(userCount);
				info.setmTotalUnreadLetter(letterCount);
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if (cUser != null) {
				cUser.close();
			}
			if (cLetter != null) {
				cLetter.close();
			}
			if(database != null){
				database.close();
			}
			
		}
		return info;
	}
	
	/**
	 * 更新私信发送结果
	 * @param time
	 * @param result Contants.LETTER_SEND_SUCCESS.YES/Contants.LETTER_SEND_SUCCESS.NO
	 * @param uid
	 */
	public void updateSendResult(long time,int result,int uid,String msgseq){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				checkTable(uid);
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+getTableName(uid)+" SET send_state = ?,msgseq = ? WHERE createdate = ?";
				database.execSQL(sql, new Object[]{result,msgseq,time});		
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public void deleteSingleLetter(MyMessageItem item){
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
				database = mDBHelper.getWritableDatabase();
				String sql = "DELETE FROM "+getTableName(item.getTo())+" WHERE createdate = ?";
				database.execSQL(sql, new Object[]{item.getCreatetime()});
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			
		}
	}
	
	/**
	 * 清除所有聊天记录
	 */
	public void clean(){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			synchronized (lock) {
				database = mDBHelper.getWritableDatabase();
				String sql = "SELECT uid,time FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = database.rawQuery(sql, null);
				while(c.moveToNext()){
					String del = "DROP TABLE "+getTableName(c.getInt(0));
					database.execSQL(del);
				}
				String cleanSql = "DELETE FROM "+Contants.TABLE_NAME.RECENT_USER;
				database.execSQL(cleanSql);
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
			if(c != null){
				c.close();
			}
			
		}
	}
}
