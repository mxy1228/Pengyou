package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RelationShipCircleCommentItem;

import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleCommentDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private RelationShipCircleDynamicHelper mDBHelper;
	
	public RelationCircleCommentDao(Context context){
		this.mContext = context;
		this.mDBHelper = new RelationShipCircleDynamicHelper(mContext);
	}
	
	public void insert(RelationShipCircleCommentItem item){
		SQLiteDatabase database = null;
		try {
			if(contain(item.getAid(), item.getCid(),item.getDynamicType(),item.getTimestamp())){
				return;
			}
			/*
			 *       db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "cid integer," +
                "comment char," +
                "dynamictype int," +
                "timestamp long" +
                "uid integer" +
                "nickname char" +
                "avatar char" +
                "reuid integer" +   
                "rename char" +    
                "success integer" +            
        		        		
        		")");
			 * */
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT+" " +
					"(aid,cid,comment," +
					"dynamictype,timestamp," +
					"uid,nickname," +
					"avatar,reuid," +
					"rename,success" +
					") VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			database.execSQL(sql, new Object[]{
					 item.getAid()
					,item.getCid()
					,item.getComment()
					,item.getDynamicType()
					,item.getTimestamp()
					,item.getUid()
					,item.getNickname()
					,item.getAvatar()
					,item.getReplyto().getUid()
					,item.getReplyto().getNickname()
					,item.getSendSuccess()
					});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
//	public void insertOrUpdate(RelationShipCircleDynamicItem item){
//		SQLiteDatabase database = null;
//		try {
//			if(contain(item.getAid(), item.getUid())){
//				database = mDBHelper.getWritableDatabase();
//				String sql = "UPDATE "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" SET versionCode = ? WHERE packageName = ? AND gamecode = ?";
//				database.execSQL(sql, new Object[]{item.getAid(),item.getUid()});
//			}else{
//				insert(item);
//			}
//		} catch (Exception e) {
//			log.e(e);
//		}finally{
//			if(database != null){
//				database.close();
//			}
//		}
//	}
	
	public synchronized List<RelationShipCircleCommentItem> selectAll(){
		SQLiteDatabase database = null;
		List<RelationShipCircleCommentItem> list = new ArrayList<RelationShipCircleCommentItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,cid,comment," +
					"dynamictype,timestamp," +
					"uid,nickname," +
					"avatar,reuid," +
					"rename,success  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				RelationShipCircleCommentItem item = new RelationShipCircleCommentItem();	    	
				    item.setAid(c.getInt(0))
					;item.setCid(c.getInt(1))
					;item.setComment(c.getString(2))
					;item.setDynamicType(c.getInt(3))
					;item.setTimestamp(c.getLong(4))
					;item.setUid(c.getInt(5))
					;item.setNickname(c.getString(6))
					;item.setAvatar(c.getString(7))
					;item.getReplyto().setUid(c.getInt(8))
					;item.getReplyto().setNickname(c.getString(9))
					;item.setSendSuccess(c.getInt(10));
					
				
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
	
	public synchronized List<RelationShipCircleCommentItem> selectDynamicComment(int aid, int dynamictype){
		SQLiteDatabase database = null;
		List<RelationShipCircleCommentItem> list = new ArrayList<RelationShipCircleCommentItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,cid,comment," +
					"dynamictype,timestamp," +
					"uid,nickname," +
					"avatar,reuid," +
					"rename,success  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT +" WHERE aid = ? AND dynamictype = ?";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(aid).toString()});
			while(c.moveToNext()){
				RelationShipCircleCommentItem item = new RelationShipCircleCommentItem();	    	
				    item.setAid(c.getInt(0))
					;item.setCid(c.getInt(1))
					;item.setComment(c.getString(2))
					;item.setDynamicType(c.getInt(3))
					;item.setTimestamp(c.getLong(4))
					;item.setUid(c.getInt(5))
					;item.setNickname(c.getString(6))
					;item.setAvatar(c.getString(7))
					;item.getReplyto().setUid(c.getInt(8))
					;item.getReplyto().setNickname(c.getString(9))
					;item.setSendSuccess(c.getInt(10));
					
				
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
	
	public synchronized List<RelationShipCircleCommentItem> selectDynamicCommentFailed(int dynamictype){
		SQLiteDatabase database = null;
		List<RelationShipCircleCommentItem> list = new ArrayList<RelationShipCircleCommentItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,cid,comment," +
					"dynamictype,timestamp," +
					"uid,nickname," +
					"avatar,reuid," +
					"rename,success  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT +" WHERE  dynamictype = ? AND success = 1";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(dynamictype).toString()});
			while(c.moveToNext()){
				RelationShipCircleCommentItem item = new RelationShipCircleCommentItem();	    	
				    item.setAid(c.getInt(0))
					;item.setCid(c.getInt(1))
					;item.setComment(c.getString(2))
					;item.setDynamicType(c.getInt(3))
					;item.setTimestamp(c.getLong(4))
					;item.setUid(c.getInt(5))
					;item.setNickname(c.getString(6))
					;item.setAvatar(c.getString(7))
					;item.getReplyto().setUid(c.getInt(8))
					;item.getReplyto().setNickname(c.getString(9))
					;item.setSendSuccess(c.getInt(10));
					
				
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
	
	
	/**
	 * 根据包名删除DB里记录
	 * @param packageName
	 */
	public void delete(int aid, int cid, int dnamictype, long time ){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT+" WHERE aid = ?  AND cid = ? AND dynamictype = ?  AND timestamp = ?";
			database.execSQL(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(cid).toString(),Integer.valueOf(dnamictype).toString(),Long.valueOf(time).toString()});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public boolean isEmpty(){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				count = c.getInt(0);
			}
			return count == 0;
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
	 *核查DB中是否已存在 
	 */
	public boolean contain(int aid,int cid, int dytype, long time){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT+" WHERE aid = ? AND cid = ?  AND dynamictype = ? AND timestamp = ?";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(cid).toString(),Integer.valueOf(dytype).toString(),Long.valueOf(time).toString()});
			while(c.moveToNext()){
				count = c.getInt(0);
			}
			return count != 0;
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
}

