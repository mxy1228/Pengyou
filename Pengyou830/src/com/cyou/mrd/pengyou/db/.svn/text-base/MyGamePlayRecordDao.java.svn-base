package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.provider.MyGamePlayRecordProvider;
import com.cyou.mrd.pengyou.entity.GamePlayRecord;
import com.cyou.mrd.pengyou.log.CYLog;

public class MyGamePlayRecordDao {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private DBHelper mDBHelper;
	
	public MyGamePlayRecordDao(Context context){
		this.mContext = context;
		this.mDBHelper = new DBHelper(mContext);
	}

	public MyGamePlayRecordDao(){
		this.mContext = CyouApplication.mAppContext;
		this.mDBHelper = new DBHelper(mContext);
	}

	//插入某游戏的一条时间片段
	public void insert(String packageName, long begin, long end, String uid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.MY_GAME_PLAY_RECORD + " " +
					"(packageName,begin,end,uid) VALUES (?,?,?,?)";
			database.execSQL(sql, new Object[]{packageName, begin, end, uid});
			mContext.getContentResolver().notifyChange(Uri.parse(MyGamePlayRecordProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}

	//插入某游戏的一条时间片段
	public void insert(ContentValues values) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "
					+ Contants.TABLE_NAME.MY_GAME_PLAY_RECORD + " " + "("
					+ DBHelper.MYGAMEPLAYRECORD.PACKAGE_NAME + ","
					+ DBHelper.MYGAMEPLAYRECORD.BEGIN + ","
					+ DBHelper.MYGAMEPLAYRECORD.END + "," + DBHelper.MYGAMEPLAYRECORD.UID +") VALUES (?,?,?,?)";
			database.execSQL(
					sql,
					new Object[] {
							values.getAsString(DBHelper.MYGAMEPLAYRECORD.PACKAGE_NAME),
							values.getAsString(DBHelper.MYGAMEPLAYRECORD.BEGIN),
							values.getAsString(DBHelper.MYGAMEPLAYRECORD.END),values.getAsString(DBHelper.MYGAMEPLAYRECORD.UID) });
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	public synchronized List<GamePlayRecord> selectGamePlayRecordList(
			String packageName, String uid) {
		SQLiteDatabase database = null;
		List<GamePlayRecord> list = new ArrayList<GamePlayRecord>();
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT begin,end FROM "
					+ Contants.TABLE_NAME.MY_GAME_PLAY_RECORD
					+ " WHERE packageName = ? AND uid = ?";
			c = database.rawQuery(sql, new String[] { packageName, uid });
			while (c.moveToNext()) {
				{
					GamePlayRecord record = new GamePlayRecord(c.getLong(0),
							c.getLong(1));
					list.add(record);
				}
			}
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
			if (c != null) {
				c.close();
			}
		}
		return list;
	}
	
	public boolean isDisplayCoin(String packageName, String uid) {
		SQLiteDatabase database = null;
		GamePlayRecord record;
		Cursor c = null;
		boolean isDisplayCoin = false;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT begin,end FROM "
					+ Contants.TABLE_NAME.MY_GAME_PLAY_RECORD
					+ " WHERE packageName = ? AND uid = ?";
			c = database.rawQuery(sql, new String[] { packageName, uid });
			while (c.moveToNext()) {
				{
					record = new GamePlayRecord(c.getLong(0), c.getLong(1));
					if (record.getEnd() - record.getBegin() > 60 * 1000) {
						isDisplayCoin = true;
						break;
					}
				}
			}
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
			if (c != null) {
				c.close();
			}
		}
		return isDisplayCoin;
	}
	
	/**
	 * 兑换积分成功时根据包名清除该游戏的时间片段
	 * @param packageName
	 */
	public void delete(String packageName, String uid){
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.MY_GAME_PLAY_RECORD+" WHERE packageName = ? AND uid = ?";
			database.execSQL(sql,new String[]{packageName, uid});
//			mContext.getContentResolver().notifyChange(Uri.parse(MyGamePlayRecordProvider.URI), null);
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
}
