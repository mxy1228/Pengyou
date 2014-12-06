package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.provider.FriendDBProvider;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 用户好友相关业务类
 * 
 * @author wangkang
 * 
 */
public class FriendDao {

	private CYLog log = CYLog.getInstance();
	private FriendDBHelper mDBHelper;
	private Context mContext;
	
	public FriendDao(Context context) {
		this.mContext = context;
		mDBHelper = new FriendDBHelper(mContext);
	}

	public void closeDB() {
		mDBHelper.close();
	}
	
	/**
	 * 获取所有的好友信息
	 * 
	 * @param s
	 * @return
	 */
	public List<FriendItem> getFriendList(int start, int count) {
		synchronized(this){
//			Log.e("FriendDao", "getFriendList from:" + start + " count:" + count);
			List<FriendItem> list = new ArrayList<FriendItem>();
			Cursor cursor = null;
			SQLiteDatabase sqlitedatabase = null;
			Set<Integer> set = new HashSet<Integer>();
			try {
				sqlitedatabase = mDBHelper.getReadableDatabase();
				String sql = "SELECT "+FriendDBHelper.FRIEND.UID
						+","+FriendDBHelper.FRIEND.NICKNAME
						+","+FriendDBHelper.FRIEND.GENDER
						+","+FriendDBHelper.FRIEND.PICTURE
						+","+FriendDBHelper.FRIEND.RECENTGMS
						+","+FriendDBHelper.FRIEND.GAMECOUNT+" FROM "+Contants.TABLE_NAME.FRIEND_LIST+" ORDER BY "+FriendDBHelper.FRIEND.TIME+" DESC"
						+ " LIMIT " +  start + "," + count;
				cursor = sqlitedatabase.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					int uid = cursor.getInt(0);
					if(set.add(uid)){
						String nickname = cursor.getString(1);
						int gender = cursor.getInt(2);
						String picture = cursor.getString(3);
						FriendItem friend = new FriendItem();
						friend.setUid(uid);
						friend.setNickname(nickname);
						friend.setGender(gender);
						friend.setPicture(picture);
						friend.setRecentgmsStr(cursor.getString(4));
						friend.setPlaynum(cursor.getInt(5));
						list.add(friend);
					}
				}
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				if (sqlitedatabase != null) {
					sqlitedatabase.close();
				}
			}
			return list;
		}
	}

	/**
	 * 获取所有的好友信息
	 * 
	 * @param s
	 * @return
	 */
	public List<FriendItem> getAllFriendList() {
		synchronized(this){
			log.d("getAllFriendList");
			List<FriendItem> list = new ArrayList<FriendItem>();
			Cursor cursor = null;
			SQLiteDatabase sqlitedatabase = null;
			Set<Integer> set = new HashSet<Integer>();
			try {
				sqlitedatabase = mDBHelper.getReadableDatabase();
				String sql = "SELECT "+FriendDBHelper.FRIEND.UID
						+","+FriendDBHelper.FRIEND.NICKNAME
						+","+FriendDBHelper.FRIEND.GENDER
						+","+FriendDBHelper.FRIEND.PICTURE
						+","+FriendDBHelper.FRIEND.RECENTGMS
						+","+FriendDBHelper.FRIEND.GAMECOUNT+" FROM "+Contants.TABLE_NAME.FRIEND_LIST+" ORDER BY "+FriendDBHelper.FRIEND.TIME+" DESC";
				cursor = sqlitedatabase.rawQuery(sql, null);
				while (cursor.moveToNext()) {
					int uid = cursor.getInt(0);
					if(set.add(uid)){
						String nickname = cursor.getString(1);
						int gender = cursor.getInt(2);
						String picture = cursor.getString(3);
						FriendItem friend = new FriendItem();
						friend.setUid(uid);
						friend.setNickname(nickname);
						friend.setGender(gender);
						friend.setPicture(picture);
						friend.setRecentgmsStr(cursor.getString(4));
						friend.setPlaynum(cursor.getInt(5));
						list.add(friend);
					}
				}
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (cursor != null) {
					cursor.close();
				}
				if (sqlitedatabase != null) {
					sqlitedatabase.close();
				}
			}
			return list;
		}
	}

	/**
	 * 根据关键字查询好友
	 * 
	 * @param s
	 * @return
	 */
	public List<FriendItem> getFriendListByNickname(String nickname) {
		log.d("getAllFriendList");
		List<FriendItem> list = new ArrayList<FriendItem>();
		Cursor cursor = null;
		SQLiteDatabase sqlitedatabase = null;
		try {
			sqlitedatabase = mDBHelper.getReadableDatabase();
			String sql = "SELECT "+FriendDBHelper.FRIEND.UID
					+","+FriendDBHelper.FRIEND.NICKNAME
					+","+FriendDBHelper.FRIEND.GENDER
					+","+FriendDBHelper.FRIEND.PICTURE
					+","+FriendDBHelper.FRIEND.RECENTGMS
					+","+FriendDBHelper.FRIEND.GAMECOUNT+" FROM "
					+ Contants.TABLE_NAME.FRIEND_LIST + " where "+FriendDBHelper.FRIEND.NICKNAME+" like ?";
			cursor = sqlitedatabase.rawQuery(sql, new String[]{"%"+nickname+"%"});
			while (cursor.moveToNext()) {
				int uid = cursor.getInt(0);
				String nickName = cursor.getString(1);
				int gender = cursor.getInt(2);
				String picture = cursor.getString(3);
				FriendItem friend = new FriendItem();
				friend.setUid(uid);
				friend.setNickname(nickName);
				friend.setGender(gender);
				friend.setPicture(picture);
				friend.setRecentgmsStr(cursor.getString(4));
				friend.setPlaynum(cursor.getInt(5));
				list.add(friend);
			}
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			if (sqlitedatabase != null) {
				sqlitedatabase.close();
			}
		}
		return list;
	}

	/**
	 * 删除某个好友
	 * 
	 * @param s
	 */
	public void deleteFriend(int uid) {
		log.i("deleteFriend");
		SQLiteDatabase sqlitedatabase = null;
		try {
			sqlitedatabase = mDBHelper.getWritableDatabase();
			sqlitedatabase.delete(Contants.TABLE_NAME.FRIEND_LIST, FriendDBHelper.FRIEND.UID+" = ?",
					new String[] { String.valueOf(uid) });
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (sqlitedatabase != null) {
				sqlitedatabase.close();
			}
		}
	}
	
	/**
	 * 批量插入
	 * @param list
	 * @return
	 */
	public void insertFriends(List<FriendItem> list){
		SQLiteDatabase database = null;
		try {
			String sql = "INSERT INTO "+Contants.TABLE_NAME.FRIEND_LIST+" ("+FriendDBHelper.FRIEND.UID
					+","+FriendDBHelper.FRIEND.NICKNAME
					+","+FriendDBHelper.FRIEND.GENDER
					+","+FriendDBHelper.FRIEND.PICTURE
					+","+FriendDBHelper.FRIEND.RECENTGMS
					+","+FriendDBHelper.FRIEND.GAMECOUNT
					+","+FriendDBHelper.FRIEND.TIME+") VALUES (?,?,?,?,?,?,?)";
			for(FriendItem item : list){
				if(!contain(item.getUid())){
					database = mDBHelper.getWritableDatabase();
					database.execSQL(sql, new Object[]{item.getUid()
							,item.getNickname()
							,item.getGender()
							,item.getPicture()
							,item.getRecentgms().toString()
							,item.getPlaynum()
							,System.currentTimeMillis()});
				}
			}
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public void insertOrUpdateFriends(List<FriendItem> list){
//		Log.e("FriendDao", "insertOrUpdateFriends:" + list.size());
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			for(FriendItem item : list){
				ContentValues cv = new ContentValues();
				cv.put(FriendDBHelper.FRIEND.UID, item.getUid());
				cv.put(FriendDBHelper.FRIEND.NICKNAME, item.getNickname());
				cv.put(FriendDBHelper.FRIEND.GENDER, item.getGender());
				cv.put(FriendDBHelper.FRIEND.PICTURE, item.getPicture());
				cv.put(FriendDBHelper.FRIEND.RECENTGMS, item.getRecentgms().toString());
//				cv.put(FriendDBHelper.FRIEND.RECENTGMS, item.getRecentgms());
				cv.put(FriendDBHelper.FRIEND.GAMECOUNT, item.getPlaynum());
				cv.put(FriendDBHelper.FRIEND.TIME, System.currentTimeMillis());
//				long n =  database.insert(Contants.TABLE_NAME.FRIEND_LIST, "", cv);
				long n = database.update(Contants.TABLE_NAME.FRIEND_LIST, cv, FriendDBHelper.FRIEND.UID + " = '" + item.getUid() + "'", null);
//				Log.e("FriendDao", "update uid:" + item.getUid() + " return:" + n );
				if(n <= 0){
//					int n = database.update(Contants.TABLE_NAME.FRIEND_LIST, cv, FriendDBHelper.FRIEND.UID + " = '" + item.getUid() + "'", null);
				n =  database.insert(Contants.TABLE_NAME.FRIEND_LIST, "", cv);
//					Log.e("FriendDao", "insert uid:" + item.getUid() + " return:" + n );
				}
			}
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
//	public void insertFriendsByUids(List<Integer> list){
//		synchronized (this) {
//			SQLiteDatabase database = null;
//			try {
//				String sql = "INSERT INTO "+Contants.TABLE_NAME.FRIEND_LIST+" ("+FriendDBHelper.FRIEND.UID+") VALUES (?)";
//				for(Integer i : list){
//					if(!contain(i)){
//						log.d("insert friend "+i);
//						database = mDBHelper.getWritableDatabase();
//						database.execSQL(sql, new Object[]{i});
//					}
//				}
//				mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
//			} catch (Exception e) {
//				log.e(e);
//			}finally{
//				if(database != null){
//					database.close();
//				}
//			}
//		}
//	}
	
	public void deleteFriends(List<Integer> uids){
		SQLiteDatabase sqlitedatabase = null;
		try {
			sqlitedatabase = mDBHelper.getWritableDatabase();
			for(Integer i : uids){
				sqlitedatabase.delete(Contants.TABLE_NAME.FRIEND_LIST, FriendDBHelper.FRIEND.UID+" = ?",
						new String[] { String.valueOf(i) });
			}
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (sqlitedatabase != null) {
				sqlitedatabase.close();
			}
		}
	}
	
	/**
	 * 单个插入
	 * @param item
	 */
	public void insertFriend(FriendItem item,long time){
		log.i("insertFriend");
		SQLiteDatabase database = null;
		try {
			if(contain(item.getUid())){
				return;
			}
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.FRIEND_LIST+" ("+FriendDBHelper.FRIEND.UID
					+","+FriendDBHelper.FRIEND.NICKNAME
					+","+FriendDBHelper.FRIEND.GENDER
					+","+FriendDBHelper.FRIEND.PICTURE
					+","+FriendDBHelper.FRIEND.TIME+") VALUES (?,?,?,?,?)";
			database.execSQL(sql, new Object[]{item.getUid(),item.getNickname(),item.getGender(),item.getPicture(),time});
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	
	/**
	 * 查询所有的Uid并生成JSONArray
	 * @return
	 */
	public Set<Integer> selectUids(){
		SQLiteDatabase databse = null;
		Cursor c = null;
		Set<Integer> set = new HashSet<Integer>();
		try {
			databse = mDBHelper.getReadableDatabase();
			String sql = "SELECT "+FriendDBHelper.FRIEND.UID+" FROM "+Contants.TABLE_NAME.FRIEND_LIST;
			c = databse.rawQuery(sql, null);
			while(c.moveToNext()){
				set.add(c.getInt(0));
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(databse != null){
				databse.close();
			}
			if(c != null){
				c.close();
			}
		}
		return set;
	}
	
	public void update(List<FriendItem> list){
		log.d("FriendDao : update");
		SQLiteDatabase databse = null;
		try {
			databse = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.FRIEND_LIST+" SET "
			+FriendDBHelper.FRIEND.NICKNAME+" = ?,"
					+FriendDBHelper.FRIEND.GENDER+" = ?,"
			+FriendDBHelper.FRIEND.PICTURE+" = ?,"
					+FriendDBHelper.FRIEND.RECENTGMS+" = ?,"
			+FriendDBHelper.FRIEND.GAMECOUNT+"=? WHERE "
					+FriendDBHelper.FRIEND.UID+" = ?";
			for(FriendItem item : list){
				databse.execSQL(sql, new Object[]{item.getNickname(),item.getGender(),item.getPicture(),item.getRecentgms().toString(),item.getPlaynum(),item.getUid()});
			}
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(databse != null){
				databse.close();
			}
		}
	}
	
	public boolean contain(int uid){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.FRIEND_LIST+" WHERE "+FriendDBHelper.FRIEND.UID+" = ?";
			c = database.rawQuery(sql, new String[]{String.valueOf(uid)});
			while(c.moveToNext()){
				if(c.getInt(0) == 0){
					return false;
				}else{
					return true;
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
		return true;
	}
	
	/**
	 * 根据关键字搜索
	 * @param key
	 * @return
	 */
	public List<FriendItem> search(String key){
		SQLiteDatabase database = null;
		Cursor c = null;
		List<FriendItem> list = new ArrayList<FriendItem>();
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT "+FriendDBHelper.FRIEND.UID+","
			+FriendDBHelper.FRIEND.NICKNAME
			+","+FriendDBHelper.FRIEND.GENDER
			+","+FriendDBHelper.FRIEND.PICTURE
			+","+FriendDBHelper.FRIEND.RECENTGMS
			+","+FriendDBHelper.FRIEND.GAMECOUNT
			+" FROM "+Contants.TABLE_NAME.FRIEND_LIST+" WHERE "+FriendDBHelper.FRIEND.NICKNAME+" LIKE '%"+key+"%'";
			c = database.rawQuery(sql,null);
			while(c.moveToNext()){
				FriendItem item = new FriendItem();
				item.setUid(c.getInt(0));
				item.setNickname(c.getString(1));
				item.setGender(c.getInt(2));
				item.setPicture(c.getString(3));
				item.setRecentgmsStr(c.getString(4));
				item.setPlaynum(c.getInt(5));
				list.add(item);
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
	
	public void updateTime(int uid,long time){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.FRIEND_LIST+" SET "+FriendDBHelper.FRIEND.TIME+" = ? WHERE "+FriendDBHelper.FRIEND.UID+" = ?";
			database.execSQL(sql, new Object[]{time,uid});
			mContext.getContentResolver().notifyChange(Uri.parse(FriendDBProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 清除所有数据
	 */
	public void clean(){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.FRIEND_LIST;
			database.execSQL(sql);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
}
