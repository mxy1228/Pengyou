package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;

public class GameDownloadRecordDao {


	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private DBHelper mDBHelper;
	public static final Object lock = new Object();
	
	public GameDownloadRecordDao(Context context) {
		this.mContext = context;
		this.mDBHelper = new DBHelper(mContext);
	}

	public GameDownloadRecordDao() {
		// TODO Auto-generated constructor stub
		this.mContext = CyouApplication.mAppContext;
		this.mDBHelper = new DBHelper(mContext);
	}

	//插入某游戏的一条时间片段
	public synchronized void insert(String packageName, String uid) {
		SQLiteDatabase database = null;
		try {
			synchronized (lock) {
			database = mDBHelper.getWritableDatabase();
			String sql = "INSERT INTO "+Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD + " " +
					"(packageName,uid) VALUES (?,?)";
			database.execSQL(sql, new Object[]{packageName,uid});
			}
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null) {
				database.close();
			}
		}
	}

	public List<String> selectList(String uid) {
		SQLiteDatabase database = null;
		List<String> list = new ArrayList<String>();
		log.i("  selectList  uid:" + uid);
		Cursor c = null;
		synchronized (lock) {
			try {
				database = mDBHelper.getReadableDatabase();
				String sql = "SELECT packageName FROM "
						+ Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD
						+ " WHERE uid = ?";
				c = database.rawQuery(sql, new String[] {uid});
				while (c.moveToNext()) {
					{
						list.add(c.getString(0));
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
		}
		return list;
	}
	
	/**
	 * 清除数据
	 */
	public void clean(String uid) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "DELETE FROM "+Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD + " WHERE uid = ?";
			database.execSQL(sql,new Object[]{uid});
		} catch (Exception e) {
			log.e(e);
		}finally{
			if(database != null){
				database.close();
			}
		}
	}
}
