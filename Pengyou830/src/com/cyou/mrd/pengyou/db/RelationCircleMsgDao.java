package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RelationCircleMessageItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleMsgDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private MessageDBHelper mDBHelper;
	
	public RelationCircleMsgDao(Context context){
		this.mContext = context;
		this.mDBHelper = new MessageDBHelper(mContext);
	}
	
	public void insert(RelationCircleMessageItem item){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" (type,mid,uid,nickname,avatar,aid,msg,acttext,actpic,isread,date,msgmemo,srctype,gmname) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			database.execSQL(sql, new Object[]{item.getType()
					,item.getMid()
					,item.getUid()
					,item.getNickname()
					,item.getUsrpicture()
					,item.getAid(),item.getMsg()
					,item.getActtext()
					,item.getActpic()
					,Contants.LETTER_IS_READ.YES
					,item.getTimestamp()
					,item.getMsgmemo()
					,item.getSrctype()
					,item.getGmname()});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 将该Mid的关系圈消息设置为已读
	 * @param mid
	 */
	public void setRead(int mid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" SET isread = ? WHERE mid = ?";
			database.execSQL(sql, new Object[]{Contants.LETTER_IS_READ.YES,mid});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 根据mid删除
	 * @param mid
	 */
	public void delete(int mid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" WHERE mid = ?";
			database.execSQL(sql, new Object[]{mid});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 根据UID删除
	 * @param aid
	 */
	public void deleteByAid(int aid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" WHERE aid = ?";
			database.execSQL(sql, new Object[]{aid});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 查询所有未读关系圈消息
	 */
	public List<RelationCircleMessageItem> selectMsgs(){
		SQLiteDatabase database = null;
		Cursor c = null;
		List<RelationCircleMessageItem> list = new ArrayList<RelationCircleMessageItem>();
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT type,mid,uid,nickname,avatar,aid,msg,acttext,actpic,isread,date,msgmemo,srctype,gmname FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" ORDER BY date DESC";
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				RelationCircleMessageItem item = new RelationCircleMessageItem();
				item.setType(c.getString(0));
				item.setMid(c.getInt(1));
				item.setUid(c.getInt(2));
				item.setNickname(c.getString(3));
				item.setUsrpicture(c.getString(4));
				item.setAid(c.getInt(5));
				item.setMsg(c.getString(6));
				item.setActtext(c.getString(7));
				item.setActpic(c.getString(8));
				item.setHasread(c.getInt(9));
				item.setTimestamp(c.getLong(10));
				item.setMsgmemo(c.getString(11));
				item.setSrctype(c.getInt(12));
				item.setGmname(c.getString(13));
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
	 * 统计关系圈消息总数
	 * @return
	 */
	public int count(){
		SQLiteDatabase database = null;
		int count = 0;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				count = c.getInt(0);
			}
			return count;
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
		return count;
	}
	
	/**
	 * 统计未读消息数目
	 * @return
	 */
//	public int countUnread(){
//		SQLiteDatabase database = null;
//		int count = 0;
//		Cursor c = null;
//		try {
//			database = mDBHelper.getReadableDatabase();
//			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" WHERE isread = "+Contants.LETTER_IS_READ.NO;
//			c = database.rawQuery(sql, null);
//			while(c.moveToNext()){
//				count = c.getInt(0);
//			}
//			return count;
//		} catch (Exception e) {
//			log.e(e);
//		}finally{
//			if(database != null){
//				database.close();
//			}
//			if(c != null){
//				c.close();
//			}
//		}
//		return count;
//	}
	
	public void clean(){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG;
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
