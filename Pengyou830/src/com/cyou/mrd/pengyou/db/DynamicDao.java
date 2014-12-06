package com.cyou.mrd.pengyou.db;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.DynamicInfoItem;
import com.cyou.mrd.pengyou.log.CYLog;

public class DynamicDao {

    private CYLog log = CYLog.getInstance();
    
    private Context mContext;
    private DynamicDBHelper mDBHelper;
    
    static byte[] lock = new byte[0];
    
    
    public DynamicDao(Context context){
        this.mContext = context;
        this.mDBHelper = new DynamicDBHelper(mContext);
    }
    
    public void insert(DynamicInfoItem item){
        SQLiteDatabase database = null;
		synchronized (lock) {
			try {
				database = mDBHelper.getWritableDatabase();
				String sql = "INSERT INTO "
						+ Contants.TABLE_NAME.DYNAMIC_INFO
						+ " "
						+ "(pid,content,picture,date,uid,status,width,height) VALUES (?,?,?,?,?,?,?,?)";

				database.execSQL(
						sql,
						new Object[] { item.getPid(), item.getContent(),
								item.getPicture(), item.getDate(),
								item.getUid(), item.getStatus(),item.getWidth(),item.getHeight() });
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (database != null) {
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
				String sql = "DELETE * FROM" + Contants.TABLE_NAME.DYNAMIC_INFO;
				database.execSQL(sql, null);
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (database != null) {
					database.close();
				}
			}
		}
    }
    /**
     * 
     * @param pid
     */
    public Integer deleteByPid(Integer pid,Integer uid){
    	Integer result = 0;
        SQLiteDatabase database = null;
		synchronized (lock) {
			try {
				database = mDBHelper.getWritableDatabase();
				result = database
						.delete(Contants.TABLE_NAME.DYNAMIC_INFO,
								"pid = ? and uid = ?",
								new String[] { String.valueOf(pid),
										String.valueOf(uid) });
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (database != null) {
					database.close();
				}
			}
		}
        return result;
    }
    
    
    
    public void updateStatus (Integer status,Integer pid,Integer uid) {
    	SQLiteDatabase database = null;
    	synchronized (lock) {
			try {
				database = mDBHelper.getWritableDatabase();
				String sql = "update " + Contants.TABLE_NAME.DYNAMIC_INFO
						+ " set status = ? " + " where pid = ? and uid = ?";
				database.execSQL(sql, new Integer[] { status, pid, uid });
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (database != null) {
					database.close();
				}
			}
    	}
    }
    
    public void updateDate (Integer status,Long date,Integer pid,Integer uid) {
    	SQLiteDatabase database = null;
		synchronized (lock) {
			try {
				database = mDBHelper.getWritableDatabase();
				String sql = "update " + Contants.TABLE_NAME.DYNAMIC_INFO
						+ " set status = ? , date = ?"
						+ " where pid = ? and uid = ?";
				database.execSQL(sql, new Object[] { status, date, pid, uid });
			} catch (Exception e) {
				log.e(e);
			} finally {
				if (database != null) {
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
    public List<DynamicInfoItem> getAllDynamic (Integer uid) {
        List<DynamicInfoItem> list = new ArrayList<DynamicInfoItem>();
        Cursor cursor = null;
        SQLiteDatabase sqlitedatabase = null;
        synchronized (lock) {
        try {
            sqlitedatabase = mDBHelper.getWritableDatabase();
            String sql = "SELECT id,pid,content,picture,date,uid,status,width,height FROM "
                    + Contants.TABLE_NAME.DYNAMIC_INFO 
                    + " WHERE status in (0,2) and uid = ?"
                    +" order by date";
            cursor = sqlitedatabase.rawQuery(sql,  new String[]{String.valueOf(uid)});
            while (cursor.moveToNext()) {
                DynamicInfoItem item = new DynamicInfoItem();
                item.setId(cursor.getInt(0));
                item.setPid(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setPicture(cursor.getString(3));
                item.setDate(cursor.getLong(4));
                item.setUid(cursor.getInt(5));
                item.setStatus(cursor.getInt(6));
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
    
    /**
     * 
     * @param pid
     * @param uid 用户编号
     * @return
     */
    public DynamicInfoItem getDynamicByPid (Integer pid,Integer uid) {
        Cursor cursor = null;
        SQLiteDatabase sqlitedatabase = null;
        DynamicInfoItem item = new DynamicInfoItem();
        synchronized (lock) {
        try {
            sqlitedatabase = mDBHelper.getReadableDatabase();
            String sql = "SELECT id,pid,content,picture,date,uid,status,width,height FROM "
                    + Contants.TABLE_NAME.DYNAMIC_INFO 
                    +" WHERE pid = ? and uid = ?";
            cursor = sqlitedatabase.rawQuery(sql, new String[]{String.valueOf(pid),String.valueOf(uid)});
            while (cursor.moveToNext()) {
                item.setId(cursor.getInt(0));
                item.setPid(cursor.getInt(1));
                item.setContent(cursor.getString(2));
                item.setPicture(cursor.getString(3));
                item.setDate(cursor.getLong(4));
                item.setUid(cursor.getInt(5));
                item.setStatus(cursor.getInt(6));
                item.setWidth(cursor.getInt(7));
                item.setHeight(cursor.getInt(8));
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
            String sql = "SELECT max(id) FROM "+ Contants.TABLE_NAME.DYNAMIC_INFO;
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

}
