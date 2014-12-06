
package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.DynamicPic;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.RelationShipCircleDynamicItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleDynamicDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private RelationShipCircleDynamicHelper mDBHelper;
	
	public RelationCircleDynamicDao(Context context){
		this.mContext = context;
		this.mDBHelper = new RelationShipCircleDynamicHelper(mContext);
	}
	/*
	 *     db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" " +
               "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "uid integer," +
                "text char," +
                "createdtime long," +
                "nickname char" +
                "type integer" +
                "supportcnt integer" +
                "supported integer" +
                "gender integer" +               
                "commentcnt integer" +
                "avatar char" +
                "star integer" + 
                "cursor char" +
                
                "pic_lp char" +
                "pic_lw integer" +
                "pic_lh integer" + 
       
                "pic_mp char" +
                "pic_mw integer" +
                "pic_mh integer" + 
       
                "pic_sp char" +
                "pic_sw integer" +
                "pic_sh integer" + 
        		        		
        		")");
    */
	public void insert(RelationShipCircleDynamicItem item){
		SQLiteDatabase database = null;
		try {
			if(contain(item.getAid(), item.getUid(),item.getDynamicType())){
				
				delete(item.getAid(),item.getDynamicType());				
				//return;
			}
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" " +
					"(aid,uid,text," +
					"createdtime,nickname," +
					"type,dynamictype,supportcnt," +
					"supported,gender," +
					"commentcnt,avatar," +
					"star,cursor," +
					"pic_lp,pic_lw, pic_lh," +
					"pic_mp,pic_mw, pic_mh," +
					"pic_sp,pic_sw, pic_sh" +
					") VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			database.execSQL(sql, new Object[]{
					 item.getAid()
					,item.getUid()
					,item.getText()
					,item.getCreatedtime()
					,item.getNickname()
					,item.getType()
					,item.getDynamicType()
					,item.getSupportcnt()
					,item.getSupported()
					,item.getGender()
					,item.getCommentcnt()
					,item.getAvatar()
					,item.getStar()
					,item.getCursor()
					,item.getPicture().getPath()
					,item.getPicture().getWidth()
					,item.getPicture().getHeight()
					,item.getPicturemiddle().getPath()
					,item.getPicturemiddle().getWidth()
					,item.getPicturemiddle().getHeight()
					,item.getPicturesmall().getPath()
					,item.getPicturesmall().getWidth()
					,item.getPicturesmall().getHeight()
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
	
	public synchronized List<RelationShipCircleDynamicItem> selectAll(){
		SQLiteDatabase database = null;
		List<RelationShipCircleDynamicItem> list = new ArrayList<RelationShipCircleDynamicItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT aid,uid,text," +
					"createdtime,nickname," +
					"type,dynamictype,supportcnt," +
					"supported,gender," +
					"commentcnt,avatar," +
					"star,cursor," +
					"pic_lp,pic_lw, pic_lh," +
					"pic_mp,pic_mw, pic_mh," +
					"pic_sp,pic_sw, pic_sh  FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE;
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
			    	RelationShipCircleDynamicItem item = new RelationShipCircleDynamicItem();
			    	DynamicPic  dpl  = new DynamicPic();
			    	item.setPicture(dpl);
			    	DynamicPic  dpm  = new DynamicPic();
			    	item.setPicturemiddle(dpm);
			    	DynamicPic  dps  = new DynamicPic();
			    	item.setPicturesmall(dps);
			    	
				    item.setAid(c.getInt(0))
					;item.setUid(c.getInt(1))
					;item.setText(c.getString(2))
					;item.setCreatedtime(c.getLong(3))
					;item.setNickname(c.getString(4))
					;item.setType(c.getInt(5))
					;item.setDynamicType(c.getInt(6))
					;item.setSupportcnt(c.getInt(7))
					;item.setSupported(c.getInt(8))
					;item.setGender(c.getInt(9))
					;item.setCommentcnt(c.getInt(10))
					;item.setAvatar(c.getString(11))
					;item.setStar(c.getString(12))
					;item.setCursor(c.getInt(13))
					;item.getPicture().setPath(c.getString(14))
					;item.getPicture().setWidth(c.getInt(15))
					;item.getPicture().setHeight(c.getInt(16))
					;item.getPicturemiddle().setPath(c.getString(17))
					;item.getPicturemiddle().setWidth(c.getInt(18))
					;item.getPicturemiddle().setHeight(c.getInt(19))
					;item.getPicturesmall().setPath(c.getString(20))
					;item.getPicturesmall().setWidth(c.getInt(21))
					;item.getPicturesmall().setHeight(c.getInt(22));
				
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
	public void delete(int aid, int dynamictype){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" WHERE aid = ? AND dynamictype = ?";
			database.execSQL(sql,new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(dynamictype).toString()});
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
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE;
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
	public boolean contain(int aid,int uid, int dynamictype){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" WHERE aid = ? AND uid = ? AND dynamictype = ?";
			c = database.rawQuery(sql, new String[]{Integer.valueOf(aid).toString(),Integer.valueOf(uid).toString(),Integer.valueOf(dynamictype).toString()});
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

