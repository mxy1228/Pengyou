package com.cyou.mrd.pengyou.db;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.GameDynamicInfoItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class GameDynamicDao {

    private CYLog log = CYLog.getInstance();
    
    private Context mContext;
    private GameDynamicDBHelper mDBHelper;
    static byte[] lock = new byte[0];
    
    public GameDynamicDao(Context context){
        this.mContext = context;
        this.mDBHelper = new GameDynamicDBHelper(mContext);
    }
    
    public void insert(GameDynamicInfoItem item){
        SQLiteDatabase database = null;
        synchronized (lock) {
        try {
            database = mDBHelper.getWritableDatabase();
            String sql = "INSERT INTO "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO+" " +
                    "(pid,content,picture,date,status,score,gamecode,gameCircleID,uid,width,height) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
            
            database.execSQL(sql, new Object[]{item.getPid()
                    ,item.getContent()
                    ,item.getPicture()
                    ,item.getDate()
                    ,item.getStatus()
                    ,item.getScore()
                    ,item.getGamecode()
                    ,item.getGameCircleId()
                    ,item.getUid()
                    ,item.getWidth()
                    ,item.getHeight()});
        } catch (Exception e) {
            log.e(e);
        }finally{
            if(database != null){
                database.close();
            }
        }
        }
    }
    public void delete(){
        SQLiteDatabase database = null;
        synchronized (lock) {
        try {
            database = mDBHelper.getWritableDatabase();
            String sql = "DELETE * FROM"+Contants.TABLE_NAME.GAME_DYNAMIC_INFO;
            database.execSQL(sql, null);
        } catch (Exception e) {
            log.e(e);
        }finally{
            if(database != null){
                database.close();
            }
        }
        }
    }
    /**
     * 
     * @param pid
     */
    public Integer deleteByPid(Integer pid){
    	Integer result = 0;
        SQLiteDatabase database = null;
        synchronized (lock) {
        try {
            database = mDBHelper.getWritableDatabase();
//            String sql = "DELETE * FROM"+Contants.TABLE_NAME.GAME_DYNAMIC_INFO
//            		+ " WHERE pid = ?";
            result = database.delete(Contants.TABLE_NAME.GAME_DYNAMIC_INFO, "pid = ?", new String[]{String.valueOf(pid)});
        } catch (Exception e) {
            log.e(e);
        }finally{
            if(database != null){
                database.close();
            }
        }
        }
        return result;
    }
    
    
    
    public void updateStatus (Integer status,Integer pid) {
    	SQLiteDatabase database = null;
    	synchronized (lock) {
        try {
            database = mDBHelper.getWritableDatabase();
            String sql = "update "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO
            		+ " set status = ? "
            		+ " where pid = ?";
            database.execSQL(sql, new Integer[]{status,pid});
        } catch (Exception e) {
            log.e(e);
        }finally{
            if(database != null){
                database.close();
            }
        }
    	}
    }
    
    public void updateDate (Integer status,Long date,Integer pid) {
    	SQLiteDatabase database = null;
    	synchronized (lock) {
        try {
            database = mDBHelper.getWritableDatabase();
            String sql = "update "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO
            		+ " set status = ? and date = ?"
            		+ " where pid = ?";
            database.execSQL(sql, new Object[]{status,date,pid});
        } catch (Exception e) {
            log.e(e);
        }finally{
            if(database != null){
                database.close();
            }
        }
    	}
    }
    
    /**
     * 所有的发布未成功动态数据
     * @param s
     * @return
     */
    public List<GameDynamicInfoItem> getAllDynamic (String gamecode,Integer uid) {
        List<GameDynamicInfoItem> list = new ArrayList<GameDynamicInfoItem>();
        Cursor cursor = null;
        SQLiteDatabase sqlitedatabase = null;
        synchronized (lock) {
        try {
            sqlitedatabase = mDBHelper.getReadableDatabase();
            String sql = "SELECT id,pid,content,picture,date,status,score,width,height FROM "
                    + Contants.TABLE_NAME.GAME_DYNAMIC_INFO 
                    + " WHERE status in (0,2) AND gamecode = ? AND uid = ?"
                    +" order by date";
            cursor = sqlitedatabase.rawQuery(sql, new String[]{gamecode,String.valueOf(uid)});
            while (cursor.moveToNext()) {
                GameDynamicInfoItem item = new GameDynamicInfoItem();
                item.setId(cursor.getInt(0));
                item.setPid(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setPicture(cursor.getString(3));
                item.setDate(cursor.getLong(4));
                item.setStatus(cursor.getInt(5));
                item.setScore(cursor.getFloat(6));
                item.setWidth(cursor.getInt(7));
                item.setHeight(cursor.getInt(8));
                list.add(item);
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
        }
        return list;
    }
    
    public GameDynamicInfoItem getDynamicByPid (Integer pid) {
        Cursor cursor = null;
        SQLiteDatabase sqlitedatabase = null;
        GameDynamicInfoItem item = new GameDynamicInfoItem();
        synchronized (lock) {
        try {
            sqlitedatabase = mDBHelper.getReadableDatabase();
            String sql = "SELECT id,pid,content,picture,date,status,score,gamecode,gameCircleID,width,height FROM "
                    + Contants.TABLE_NAME.GAME_DYNAMIC_INFO 
                    +" WHERE pid = ?";
            cursor = sqlitedatabase.rawQuery(sql, new String[]{String.valueOf(pid)});
            while (cursor.moveToNext()) {
                item.setId(cursor.getInt(0));
                item.setPid(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setPicture(cursor.getString(3));
                item.setDate(cursor.getLong(4));
                item.setStatus(cursor.getInt(5));
                item.setScore(cursor.getFloat(6));
                item.setGamecode(cursor.getString(7));
                item.setGameCircleId(cursor.getString(8));
                item.setWidth(cursor.getInt(9));
                item.setHeight(cursor.getInt(10));
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
        }
        return item;
    }
    
    public Integer getMaxId () {
        int result = 0;
        Cursor cursor = null;
        SQLiteDatabase sqlitedatabase = null;
        synchronized (lock) {
        try {
            sqlitedatabase = mDBHelper.getReadableDatabase();
            String sql = "SELECT max(id) FROM "+ Contants.TABLE_NAME.GAME_DYNAMIC_INFO;
            cursor = sqlitedatabase.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                result = cursor.getInt(0);
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
        }
        return result + 1;
    }
    
//    //获得该游戏的打分值
//    public float getScore(String gamecode){
//    	float score = 0;
//    	SQLiteDatabase sqlitedatabase = null;
//    	Cursor cursor = null;
//    	try{
//    		sqlitedatabase = mDBHelper.getReadableDatabase();
//    		String sql = "SELECT max(score) FROM "+ Contants.TABLE_NAME.GAME_DYNAMIC_INFO+" WHERE gamecode = ?";
//    		Log.e("SendGameCircleDynamic","gamecode="+gamecode);
//    		cursor = sqlitedatabase.rawQuery(sql, new String[]{gamecode});
////    		if(cursor.getCount() > 0){
////    			cursor.moveToFirst();
////    			score = cursor.getFloat(0);
////    		}
//    		while(cursor.moveToNext()){
//    			score = cursor.getFloat(0);
//    		}
//    	}catch (Exception e) {
//    		log.e(e);
//		} finally {
//            if (cursor != null) {
//                cursor.close();
//            }
//            if (sqlitedatabase != null) {
//                sqlitedatabase.close();
//            }
//        }
//    	
//    	return score;
//    	
//    }

}
