package com.cyou.mrd.pengyou.db;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.GameUpdateInfo;
import com.cyou.mrd.pengyou.log.CYLog;

public class UpdateDao {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private DBHelper mDBHelper;

	public UpdateDao(Context context) {
		this.mContext = context;
		this.mDBHelper = new DBHelper(mContext);
	}

	public void delete(GameUpdateInfo info) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getReadableDatabase();
			database.delete(Contants.TABLE_NAME.UPDATE_TASK, "packageName = ?",
					new String[] { info.identifier });
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	public void deleteAll() {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getReadableDatabase();
			database.delete(Contants.TABLE_NAME.UPDATE_TASK, null, null);
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	public GameUpdateInfo select(String packageName) {
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String str = "SELECT packageName,name,icon,version,cansaveup,patchurl,patchsize,fullurl,fullsize,path,state FROM "
					+ Contants.TABLE_NAME.UPDATE_TASK
					+ " WHERE packageName = ?";
			c = database.rawQuery(str, new String[] { packageName });
			GameUpdateInfo info = new GameUpdateInfo();
			while (c.moveToNext()) {
				info.identifier = c.getString(0);
				info.name = c.getString(1);
				info.icon = c.getString(2);
				info.version = c.getString(3);
				info.cansaveup = c.getString(4);
				info.patchurl = c.getString(5);
				info.patchsize = c.getString(6);
				info.fullurl = c.getString(7);
				info.fullsize = c.getString(8);
				info.path = c.getString(9);
				info.state = c.getInt(10);
			}
			return info;
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
		return null;
	}

	public void saveAll(List<GameUpdateInfo> list) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			for (GameUpdateInfo info : list) {
				String sql = "INSERT INTO "
						+ Contants.TABLE_NAME.UPDATE_TASK
						+ " (packageName,name,icon,version,cansaveup,patchurl,patchsize,fullurl,fullsize,path,state) "
						+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
				Object[] params = new Object[] { info.identifier, info.name,
						info.icon, info.version, info.cansaveup, info.patchurl,
						info.patchsize, info.fullurl, info.fullsize, info.path,
						info.state };
				database.execSQL(sql, params);
			}
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
				database.close();
			}
		}
	}

	public List<GameUpdateInfo> selectAll() {
		SQLiteDatabase database = null;
		Cursor c = null;
		try {
			database = mDBHelper.getReadableDatabase();
			String sql = "SELECT packageName,name,icon,version,cansaveup,patchurl,patchsize,fullurl,fullsize,path,state FROM "
					+ Contants.TABLE_NAME.UPDATE_TASK;
			c = database.rawQuery(sql, null);
			List<GameUpdateInfo> list = new ArrayList<GameUpdateInfo>();
			while (c.moveToNext()) {
				GameUpdateInfo info = new GameUpdateInfo();
				info.identifier = c.getString(0);
				info.name = c.getString(1);
				info.icon = c.getString(2);
				info.version = c.getString(3);
				info.cansaveup = c.getString(4);
				info.patchurl = c.getString(5);
				info.patchsize = c.getString(6);
				info.fullurl = c.getString(7);
				info.fullsize = c.getString(8);
				info.path = c.getString(9);
				info.state = c.getInt(10);
				list.add(info);
			}
			return list;
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
		return null;
	}

	public void update(GameUpdateInfo info) {
		SQLiteDatabase database = null;
		try {
			database = mDBHelper.getWritableDatabase();
			String sql = "UPDATE " + Contants.TABLE_NAME.UPDATE_TASK
					+ " SET state = ?,path = ? WHERE packagename = ?";
			database.execSQL(sql, new Object[] { info.state, info.path,
					info.identifier });
		} catch (Exception e) {
			log.e(e);
		} finally {
			if (database != null) {
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
			String sql = "DELETE FROM "+Contants.TABLE_NAME.UPDATE_TASK;
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
