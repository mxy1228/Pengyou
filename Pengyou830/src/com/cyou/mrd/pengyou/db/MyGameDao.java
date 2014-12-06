package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.Util;

public class MyGameDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private DBHelper mDBHelper;
	
	public MyGameDao(Context context){
		this.mContext = context;
		this.mDBHelper = new DBHelper(mContext);
	}

	/**
	 * tuozhonghua_zk
	 * 2013-10-12下午3:17:57
	 */
	public MyGameDao(){
		this.mContext = CyouApplication.mAppContext;
		this.mDBHelper = new DBHelper(mContext);
	}
	
	public void insert(GameItem item){
		SQLiteDatabase database = null;
		try {
			if(contain(item.getIdentifier())){
				return;
			}
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.MY_GAME+" " +
					"(packageName" +
					",gamecode" +
					",name" +
					",icon" +
					",version" +
					",versionCode" +
					",fullurl" +
					",fullsize" +
					",playerNum" +
					",star" +
					",gcacts" +
					",timestamp" +
					",isShown" +
					","+DBHelper.MYGAME.LOCAL_VERSION+
					",gcid) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			database.execSQL(sql, new Object[]{item.getIdentifier()
					,item.getGamecode()
					,item.getName()
					,item.getIcon()
					,item.getVersion()
					,item.getVersioncode()
					,item.getFullurl()
					,item.getFullsize()
					,item.getFrdplay()
					,item.getStar()
					,item.getGcacts()
					,System.currentTimeMillis()
					,item.getIspublic()
					,item.getLocalVersion()
					,item.getGcid()});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public void insert(ContentValues values){
		SQLiteDatabase database = null;
		try {
			if(contain(values.getAsString(DBHelper.MYGAME.PACKAGE_NAME))){
				return;
			}
			database = mDBHelper.getWritableDatabase();
			database.insert(Contants.TABLE_NAME.MY_GAME, null, values);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public void insertUpdate(ContentValues values){
		SQLiteDatabase database = mDBHelper.getWritableDatabase();
		long n = database.update(Contants.TABLE_NAME.MY_GAME, values, 
									DBHelper.MYGAME.PACKAGE_NAME + " = '" + 
									values.getAsString(DBHelper.MYGAME.PACKAGE_NAME) 
									+ "'", null);
		if(n <= 0) {
			database.insert(Contants.TABLE_NAME.MY_GAME, null, values);
		}
		if(database != null){
			database.close();
		}
	}
	
	
	public void insertOrUpdate(GameItem item){
		log.d("insertOrUpdate");
		SQLiteDatabase database = null;
		try {
			if(contain(item.getIdentifier())){
				database = mDBHelper.getWritableDatabase();
				String sql = "UPDATE "+Contants.TABLE_NAME.MY_GAME+" SET gamecode = ?" +
						",name = ?" +
						",icon = ?" +
						",version = ?" +
						",versionCode = ?" +
						",fullurl = ?" +
						",fullsize = ?" +
						",playerNum = ?" +
						",star = ?" +
						",gcacts = ?" +
						","+DBHelper.MYGAME.GCID+" = ?"+
						","+DBHelper.MYGAME.LOCAL_VERSION+" = ? "+
						",timestamp = ?"+
						",isShown = ? WHERE packageName = ?";
				database.execSQL(sql, new Object[]{item.getGamecode()
						,item.getName()
						,item.getIcon()
						,item.getVersion()
						,item.getVersioncode()
						,item.getFullurl()
						,item.getFullsize()
						,item.getFrdplay()
						,item.getStar()
						,item.getGcacts()
						,item.getGcid()
						,item.getLocalVersion()
						,item.getTimestamp()
						,item.getIspublic()
						,item.getIdentifier()});
			}else{
				item.setTimestamp(System.currentTimeMillis());
				insert(item);
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}

	public synchronized List<GameItem> selectAll(){
		SQLiteDatabase database = null;
		List<GameItem> list = new ArrayList<GameItem>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT packageName" +
					",gamecode" +
					",name" +
					",icon" +
					",version" +
					",versionCode" +
					",fullurl" +
					",fullsize" +
					",playerNum" +
					",star" +
					",gcacts" +
					",timestamp" +
					","+DBHelper.MYGAME.GCID +
					","+DBHelper.MYGAME.LOCAL_VERSION+
					",recentStopPlayDate"+
					",recentPlayDuration"+
					",hasScore"+
					",isShown FROM "+Contants.TABLE_NAME.MY_GAME + " ORDER BY timestamp DESC";
			c = database.rawQuery(sql, null);
			while(c.moveToNext()){
				GameItem item = new GameItem();
				item.setIdentifier(c.getString(0));
				item.setGamecode(c.getString(1));
				item.setName(c.getString(2));
				item.setIcon(c.getString(3));
				item.setVersion(c.getString(4));
				item.setVersioncode(c.getString(5));
				item.setFullurl(c.getString(6));
				item.setFullsize(c.getString(7));
				item.setFrdplay(c.getString(8));
				item.setStar(Float.valueOf(c.getString(9)));
				item.setGcacts(c.getString(10));
				item.setTimestamp(c.getLong(11));
				item.setGcid(c.getString(12));
				item.setLocalVersion(c.getString(13));
				item.setRecplaydat(c.getLong(14));
				item.setRecplaydur(c.getString(15));
				item.setHasscore(c.getInt(16));
				item.setIspublic(c.getInt(17));
				if(Util.isInstallByread(item.getIdentifier())){
					list.add(item);
				}else{
					delete(item.getIdentifier());
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
	 * 根据包名删除DB里记录
	 * @param packageName
	 */
	public void delete(String packageName){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.MY_GAME+" WHERE packageName = ?";
			database.execSQL(sql,new String[]{packageName});
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
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.MY_GAME;
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
	 *根据包名和版本名核查DB中是否已存在 
	 * @param packageName
	 * @param versionName
	 * @return
	 */
	public boolean contain(String packageName,String versionName){
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			int count = 0;
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.MY_GAME+" WHERE packageName = ? AND version = ?";
			c = database.rawQuery(sql, new String[]{packageName,versionName});
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
	
	//根据包名判断DB中是否存在
	public boolean contain(String packageName){
		SQLiteDatabase database = null;
		Cursor c = null;
		int count = 0;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT COUNT(*) FROM "+Contants.TABLE_NAME.MY_GAME+" WHERE packageName = ?";
			c = database.rawQuery(sql, new String[]{packageName});
			while(c.moveToNext()){
				count = c.getInt(0);
			}
//			return count != 0;
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
		if(count != 0){
			return true;
		}
		return false;
	}

	//如果用户启动了一次游戏，就更新这个游戏的timestamp
	public void update(String packageName, long timestamp) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.MY_GAME+" SET timestamp = ? WHERE packageName = ?";
			database.execSQL(sql, new Object[]{timestamp, packageName});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 更新游戏显示/隐藏
	 * @param packageName
	 * @param timestamp
	 */
	public void updateIsShown(String packageName, int ispublic) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.MY_GAME+" SET "+DBHelper.MYGAME.IS_SHOWN+" = ? WHERE packageName = ?";
			database.execSQL(sql, new Object[]{ispublic, packageName});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	/**
	 * 清除数据
	 */
	public void clean(){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.MY_GAME;
			database.execSQL(sql);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
	public String getVersionName (String packageName) {
		String versionName = "";
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT version FROM "+Contants.TABLE_NAME.MY_GAME+" WHERE packageName = ? ";
			c = database.rawQuery(sql, new String[]{packageName});
			while (c.moveToNext()) {
				versionName = c.getString(0);
			}
		} catch (Exception e) {
			log.e(e);
		} finally{
			if(database != null){
				database.close();
			}
		}
		return versionName;
	}
	/**
	 * 卸载时，获取本地安装过的应用的版本名称
	 * tuozhonghua_zk
	 * 2013-11-14下午1:15:44
	 *
	 * @param packageName
	 * @return
	 */
	public String getLocalVersionName (String packageName) {
		String versionName = "";
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT localversion FROM "+Contants.TABLE_NAME.MY_GAME+" WHERE packageName = ? ";
			c = database.rawQuery(sql, new String[]{packageName});
			while (c.moveToNext()) {
				versionName = c.getString(0);
			}
		} catch (Exception e) {
			log.e(e);
		} finally{
			if(database != null){
				database.close();
			}
		}
		return versionName;
	}
	
	//如果用户玩了一会某游戏，利用这个游戏最后一次玩的起始时间，结束时间，算出玩了多久，以及保存相关的值
	public void updateLastPlayInfo(String packageName, long recentBeginPlayDate, long recentStopPlayDate) {
		SQLiteDatabase database = null;
		log.i("jifen  recentBeginPlayDate = " + recentBeginPlayDate);
		log.i("jifen  recentStopPlayDate = " + recentStopPlayDate);
		try {
			//录时长
			String recentPlayDuration = Util.countGamePlayDur(recentBeginPlayDate);//玩了多久
			int coins = (int)((recentStopPlayDate - recentBeginPlayDate)/1000/60) + getHasScore(packageName);
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE " + Contants.TABLE_NAME.MY_GAME + " SET recentStopPlayDate = ?, recentPlayDuration = ?, hasScore = ? WHERE packageName = ?";
			database.execSQL(sql, new Object[]{recentStopPlayDate, recentPlayDuration, coins, packageName});
		} catch (Exception e) {
			log.i("jifen 出异常 = " + e.toString());
			log.e(e);
		}finally{
			if(database != null) {
				database.close();
			}
		}
	}	
	
//	//根据包名获取该游戏未领取积分
//	public int getCoins(String packageName){
//		SQLiteDatabase database = null;
//		Cursor c = null;		
//		try {
//			int coins = 0;
//			database = mDBHelper.getReadableDatabase();
//			String sql = "SELECT hasScore FROM " +Contants.TABLE_NAME.MY_GAME + " WHERE packageName = ?";
//			c = database.rawQuery(sql, new String[]{packageName});
//			while(c.moveToNext()){
//				coins = c.getInt(0);
//			}
//			return coins;
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
//		return 0;
//	}

//	public boolean getHasScore(String packageName){
//		SQLiteDatabase database = null;
//		Cursor c = null;		
//		try {
//			int hasscore = 0;
//			database = mDBHelper.getReadableDatabase();
//			String sql = "SELECT hasScore FROM " +Contants.TABLE_NAME.MY_GAME + " WHERE packageName = ?";
//			c = database.rawQuery(sql, new String[]{packageName});
//			while(c.moveToNext()){
//				hasscore = c.getInt(0);
//			}
//			return hasscore == Contants.GAME_HAS_SCORE.HAS ? true : false;
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
//		return false;
//	}
	//根据包名获取是否显示金币的状态
	public int getHasScore(String packageName){
	SQLiteDatabase database = null;
	Cursor c = null;		
	try {
		int hasscore = 0;
		database = mDBHelper.getReadableDatabase();
		String sql = "SELECT hasScore FROM " +Contants.TABLE_NAME.MY_GAME + " WHERE packageName = ?";
		c = database.rawQuery(sql, new String[]{packageName});
		while(c.moveToNext()){
			hasscore = c.getInt(0);
		}
		return hasscore;
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
	return 0;
	}
	
	//如果用户点击了金币的图案，就清除掉数据库中的未领取积分个数
	public void clearCoin(String packageName) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE "+Contants.TABLE_NAME.MY_GAME+" SET hasScore = ? WHERE packageName = ?";
			database.execSQL(sql, new Object[]{0, packageName});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
	
}
