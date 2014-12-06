package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RelationShipCircleCommentItem;
import com.cyou.mrd.pengyou.entity.RelationShipCircleGameItem;

import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleGameDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private RelationShipCircleDynamicHelper mDBHelper;
	
	public RelationCircleGameDao(Context context){
		this.mContext = context;
		this.mDBHelper = new RelationShipCircleDynamicHelper(mContext);
	}
	public void insert(RelationShipCircleGameItem item){
		SQLiteDatabase database = null;
		try {
			if(contain(item.getAid(),item.getDynamicType())){
				delete(item.getAid(),item.getDynamicType());
			}
			/*
			 *    db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "platform integer," +
                "gamecode char," +
                "gametype char," +
                "gameicon char" +
                "playnum integer" +
                "platform integer" +
                "gamedesc char" +
                "dynamicype integer" +               	        		
        		")");
			 * */
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" " +
					"(aid," +
                "platform," +
                "gamecode," +
                "gametype," +
                "gameicon," +
                "playnum," +
                "gamedesc," +
                "gamenm," +
                "dynamictype" +           
					") VALUES (?,?,?,?,?,?,?,?,?)";
			database.execSQL(sql, new Object[]{
					 item.getAid()
					,item.getPlatform()
					,item.getGamecode()
					,item.getGametype()
					,item.getGameicon()
					,item.getPlaynum()
					,item.getGamedesc()
					,item.getGamenm()
					,item.getDynamicType()
		
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
	
	public synchronized List<RelationShipCircleGameItem> selectAll(){
		SQLiteDatabase database = null;
		List<RelationShipCircleGameItem> list = new ArrayList<RelationShipCircleGameItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,platform,gamecode," +
					"gametype,gameicon," +
					"playnum,gamedesc,gamenm," +
					"dynamictype" +
					"  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				RelationShipCircleGameItem item = new RelationShipCircleGameItem();	    	
				    item.setAid(c.getInt(0))
					;item.setPlatform(c.getInt(1))
					;item.setGamecode(c.getString(2))
					;item.setGametype(c.getString(3))
					;item.setGameicon(c.getString(4))
					;item.setPlaynum(c.getInt(5))
					;item.setGamedesc(c.getString(6))
					;item.setGamenm(c.getString(7))
					;item.setDynamicType(c.getInt(8))
					;
					
				
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
	
	public synchronized RelationShipCircleGameItem selectGame(int aid, int dynamicype){
		SQLiteDatabase database = null;
		RelationShipCircleGameItem gameItem = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,platform,gamecode," +
					"gametype,gameicon," +
					"playnum,gamedesc,gamenm," +
					"dynamictype" +
					"  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" WHERE aid = ? AND dynamictype = ?";
			c = database.rawQuery(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(dynamicype).toString()});
			while(c.moveToNext()){
			  	     gameItem = new RelationShipCircleGameItem();
				     gameItem.setAid(c.getInt(0))
					;gameItem.setPlatform(c.getInt(1))
					;gameItem.setGamecode(c.getString(2))
					;gameItem.setGametype(c.getString(3))
					;gameItem.setGameicon(c.getString(4))
					;gameItem.setPlaynum(c.getInt(5))
					;gameItem.setGamedesc(c.getString(6))
					;gameItem.setGamenm(c.getString(7))
					;gameItem.setDynamicType(c.getInt(8))
					;
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
		return gameItem;
	}
	
	
	/**
	 * 根据包名删除DB里记录
	 * @param packageName
	 */
	public void delete(int aid, int dynamicype){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" WHERE aid = ? AND dynamictype = ?";
			database.execSQL(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(dynamicype).toString()});
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
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME;
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
	public boolean contain(int aid ,int dynamicype){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" WHERE aid = ? AND dynamictype = ?";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(dynamicype).toString()});
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


