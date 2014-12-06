package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RelaionShipCircleSupporterItem;

import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleSupporterDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private RelationShipCircleDynamicHelper mDBHelper;
	
	public RelationCircleSupporterDao(Context context){
		this.mContext = context;
		this.mDBHelper = new RelationShipCircleDynamicHelper(mContext);
	}
	
	public void insert(RelaionShipCircleSupporterItem item){
		SQLiteDatabase database = null;
		try {
			if(contain(item.getAid(), item.getUid())){
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" SET cancel = ? WHERE aid = ? AND uid = ?";
				database.execSQL(sql, new Integer[] { item.getCancel(), item.getAid(), item.getUid()});
			}
			/*
			 *    db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "uid integer," +
                "dynamictype int," +
                "nickname char" +
                "avatar char" +                 		        		
        		")");           
			 * */
			else {
				database = mDBHelper.getWritableDatabase();
				String sql = "INSERT INTO "
						+ Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER
						+ " " + "(aid," + "uid," + "dynamictype," + "nickname,"
						+ "avatar,cancel,fail" + ") VALUES (?,?,?,?,?,?,?)";
				database.execSQL(
						sql,
						new Object[] { item.getAid(), item.getUid(),
								item.getDynamicType(), item.getNickname(),
								item.getAvatar(), item.getCancel(),
								item.getFailed() });
			}
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
	
	public synchronized List<RelaionShipCircleSupporterItem> selectAll(){
		SQLiteDatabase database = null;
		List<RelaionShipCircleSupporterItem> list = new ArrayList<RelaionShipCircleSupporterItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,uid," +
					"dynamictype," +
					"nickname," +
					"avatar,cancel FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				RelaionShipCircleSupporterItem item = new RelaionShipCircleSupporterItem();	    	
				    item.setAid(c.getInt(0))
					;item.setUid(c.getInt(1))
					;item.setDynamicType(c.getInt(2))
					;item.setNickname(c.getString(3))
					;item.setAvatar(c.getString(4))
					;item.setCancel(c.getInt(5))
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
	
	public synchronized List<RelaionShipCircleSupporterItem> selectSupporter(int aid, int dynamictype){
		SQLiteDatabase database = null;
		List<RelaionShipCircleSupporterItem> list = new ArrayList<RelaionShipCircleSupporterItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,uid," +
					"dynamictype," +
					"nickname," +
					"avatar,cancel FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" WHERE aid = ? AND dynamictype = ? AND fail = 0 ";
			c = database.rawQuery(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(dynamictype).toString()});
			while(c.moveToNext()){
				RelaionShipCircleSupporterItem item = new RelaionShipCircleSupporterItem();	    	
				    item.setAid(c.getInt(0))
					;item.setUid(c.getInt(1))
					;item.setDynamicType(c.getInt(2))
					;item.setNickname(c.getString(3))
					;item.setAvatar(c.getString(4))
					;item.setCancel(c.getInt(5))
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
	
	
	public synchronized List<RelaionShipCircleSupporterItem> selectFailedSupporter( int dynamictype){
		SQLiteDatabase database = null;
		List<RelaionShipCircleSupporterItem> list = new ArrayList<RelaionShipCircleSupporterItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,uid," +
					"dynamictype," +
					"nickname," +
					"avatar,cancel FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" WHERE dynamictype = ? AND fail = 1";
			c = database.rawQuery(sql,new String[]{Integer.valueOf(dynamictype).toString()});
			while(c.moveToNext()){
				RelaionShipCircleSupporterItem item = new RelaionShipCircleSupporterItem();	    	
				    item.setAid(c.getInt(0))
					;item.setUid(c.getInt(1))
					;item.setDynamicType(c.getInt(2))
					;item.setNickname(c.getString(3))
					;item.setAvatar(c.getString(4))
					;item.setCancel(c.getInt(5))
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
	
	/**
	 * 根据包名删除DB里记录
	 * @param packageName
	 */
	public void delete(int aid, int uid, int dynamictype){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" WHERE aid = ? AND uid = ? AND dynamictype = ?";
			database.execSQL(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(uid).toString(),Integer.valueOf(dynamictype).toString()});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 根据包名删除DB里记录
	 * @param packageName
	 */
	public void deleteFailedDbSupport(int aid, int uid, int dynamictype){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" WHERE aid = ? AND uid = ? AND dynamictype = ? AND fail = 1";
			database.execSQL(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(uid).toString(),Integer.valueOf(dynamictype).toString()});
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
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER;
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
	public boolean contain(int aid,int uid){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" WHERE aid = ? AND uid = ?";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(uid).toString()});
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



