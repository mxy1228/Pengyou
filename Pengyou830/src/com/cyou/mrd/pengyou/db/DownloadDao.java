package com.cyou.mrd.pengyou.db;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.Util;

public class DownloadDao {

    private CYLog log = CYLog.getInstance();
    private DBHelper mDBHelper;
    private Context mContext;
    private static DownloadDao mDao;
//    private SQLiteDatabase sqliteReadDatabase = null;
//    private SQLiteDatabase sqliteWriteDatabase = null;
    
    public static final Object lock = new Object();

    private DownloadDao() {
        this.mContext = CyouApplication.mAppContext;
        mDBHelper = DBHelper.getInstance(mContext);
    }
    
    private DownloadDao(Context context){
        this.mContext = context;
        mDBHelper = DBHelper.getInstance(mContext);
    }
    
    public static DownloadDao getInstance(Context context) {
    	if (mDao == null) {
    		mDao = new DownloadDao(context);
    	}
    	return mDao;
    }

    /**
     * 删除下载完成的数据
     * 
     * @param s
     */
    public synchronized void delete(DownloadItem item) {
        log.d("删除:" + item.getmPackageName() + "__" + item.getmName());
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                database.delete(Contants.TABLE_NAME.DOWNLOAD,"packagename = ? AND versionname = ?",
                        new String[] { item.getmPackageName(),
                        item.getVersionName() });
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 删除下载完成或者安装完成的数据
     * 
     * @param s
     */
    public synchronized void deleteAllDone() {
        log.d("delete");
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                database.delete(Contants.TABLE_NAME.DOWNLOAD,"state=? OR state=? ",
                    new String[] { String.valueOf(DownloadParam.C_STATE.DONE),
                        String.valueOf(DownloadParam.C_STATE.INSTALLED) });
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 删除下载完成的数据
     * 
     * @param s
     */
    public synchronized void delete(String packageName) {
        log.d("delete");
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                database.delete(Contants.TABLE_NAME.DOWNLOAD,"packagename = ?", new String[] { packageName });
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 删除下载完成的数据
     * 
     * @param s
     */
    public synchronized void delete(String packageName, String versionName) {
        log.d("delete");
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                database.delete(Contants.TABLE_NAME.DOWNLOAD,"packagename = ? AND versionname = ?", new String[] {packageName, versionName });
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 根据pakageName获取DownloadItem
     * 
     * @param packageName
     * @return
     */
    public synchronized DownloadItem getDowloadItem(String packageName, String versionName) {
        Cursor c = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                if (TextUtils.isEmpty(versionName)
                        || TextUtils.isEmpty(packageName)) {
                    return null;
                }
                String sql = "SELECT name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode FROM "
                        + Contants.TABLE_NAME.DOWNLOAD
                        + " where packagename = ? AND versionname = ?";
                c = database.rawQuery(sql, new String[] { packageName,versionName });
                DownloadItem item = new DownloadItem();
                while (c.moveToNext()) {
                    item.setmName(c.getString(0));
                    item.setmSize(c.getString(1));
                    item.setmLogoURL(c.getString(2));
                    item.setmPackageName(c.getString(3));
                    item.setmURL(c.getString(4));
                    item.setmTotalSize(c.getLong(5));
                    item.setmState(c.getInt(6));
                    item.setPath(c.getString(7));
                    item.setVersionName(c.getString(8));
                    item.setGameCode(c.getString(9));
                    if (item.getmState() == DownloadParam.C_STATE.DONE) {
                        item.setmPercent(100);
                    } else {
                        if (!TextUtils.isEmpty(item.getPath())) {
                            File tempFile = new File(item.getPath());
                            if (tempFile.exists()) {
                                long length = tempFile.length();
                                long totalSize = item.getmTotalSize();
                                if (0 != totalSize) {
                                    item.setmPercent((int) (length * 100 / totalSize));
                                }
                            }
                        }
                    }
                }
                return item;
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (c != null) {
                c.close();
            }
        }
        return null;
    }
    
    /**
     * 根据pakageName获取DownloadItem
     * 
     * @param packageName
     * @return
     */
    public synchronized DownloadItem getDowloadItem(String packageName) {
        Cursor c = null;
        SQLiteDatabase database = null;
        try {
//            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                if (TextUtils.isEmpty(packageName)) {
                    return null;
                }
                String sql = "SELECT name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode FROM "
                        + Contants.TABLE_NAME.DOWNLOAD
                        + " where packagename = ?";
                c = database.rawQuery(sql, new String[] { packageName});
                DownloadItem item = new DownloadItem();
                while (c.moveToNext()) {
                    item.setmName(c.getString(0));
                    item.setmSize(c.getString(1));
                    item.setmLogoURL(c.getString(2));
                    item.setmPackageName(c.getString(3));
                    item.setmURL(c.getString(4));
                    item.setmTotalSize(c.getLong(5));
                    item.setmState(c.getInt(6));
                    item.setPath(c.getString(7));
                    item.setVersionName(c.getString(8));
                    item.setGameCode(c.getString(9));
                    if (item.getmState() == DownloadParam.C_STATE.DONE) {
                        item.setmPercent(100);
                    } else {
                        if (!TextUtils.isEmpty(item.getPath())) {
                            File tempFile = new File(item.getPath());
                            if (tempFile.exists()) {
                                long length = tempFile.length();
                                long totalSize = item.getmTotalSize();
                                if (0 != totalSize) {
                                    item.setmPercent((int) (length * 100 / totalSize));
                                }
                            }
                        }
                    }
//                }
                return item;
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (c != null) {
                c.close();
            }
        }
        return null;
    }

    
    /**
     * 获取所有的下载信息
     * 
     * @param s
     * @return
     */
    public synchronized List<DownloadItem> getDownloadItems() {
        log.d("getDownloadInfos");
        List<DownloadItem> list = new ArrayList<DownloadItem>();
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
//            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode FROM " + Contants.TABLE_NAME.DOWNLOAD;
                cursor = database.rawQuery(sql, null);
                while (cursor.moveToNext()) {
                    // File tempFile =
                    // DownloadUtil.getTempFile(cursor.getString(3));
                    int percent = 0;
                    if (!TextUtils.isEmpty(cursor.getString(7))) {
                        File tempFile = new File(cursor.getString(7));
                        if (tempFile.exists()) {
                            long length = tempFile.length();
                            long totalSize = cursor.getLong(5);
                            percent = (int) (length * 100 / totalSize);
                            if (percent < 0) {
                                percent = 0;
                            } else if (percent > 100) {
                                percent = 100;
                            }
                        }
                    }
//                if (!cursor.getString(3).equals(mContext.getPackageName())) {
                    DownloadItem item = new DownloadItem(cursor.getString(0),
                            cursor.getString(4), cursor.getString(1),
                            cursor.getString(2), percent, cursor.getInt(6),
                            cursor.getString(3), cursor.getLong(5), 0, "",
                            cursor.getString(7), cursor.getString(8),
                            cursor.getString(9));
                    list.add(item);
//                }
//                }
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
    /**
     * 根据状态查询数据
     * @param state 
     * @return
     */
    public synchronized List<DownloadItem> getDownloadItemsByState(int state) {
        log.d("getDownloadInfos");
        List<DownloadItem> list = new ArrayList<DownloadItem>();
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode FROM "
                        + Contants.TABLE_NAME.DOWNLOAD + " where state = ? ";
                cursor = database.rawQuery(sql, new String[]{String.valueOf(state)});
                while (cursor.moveToNext()) {
                    // File tempFile =
                    // DownloadUtil.getTempFile(cursor.getString(3));
                    int percent = 0;
                    if (!TextUtils.isEmpty(cursor.getString(7))) {
                        File tempFile = new File(cursor.getString(7));
                        if (tempFile.exists()) {
                            long length = tempFile.length();
                            long totalSize = cursor.getLong(5);
                            if (0 == totalSize) {
                                percent = 0;
                            } else {
                                percent = (int) (length * 100 / totalSize);
                            }
                        }
                    }
                    if (!cursor.getString(3).equals(mContext.getPackageName())) {
                        DownloadItem item = new DownloadItem(cursor.getString(0),
                                cursor.getString(4), cursor.getString(1),
                                cursor.getString(2), percent, cursor.getInt(6),
                                cursor.getString(3), cursor.getLong(5), 0, "",
                                cursor.getString(7), cursor.getString(8),
                                cursor.getString(9));
                        list.add(item);
                    }
                }
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-14上午9:40:59
     *
     * @param pkg
     * @return
     */
    public synchronized List<DownloadItem> getDownloadItemsByPackage(String pkg) {
        log.d("getDownloadInfos");
        List<DownloadItem> list = new ArrayList<DownloadItem>();
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode FROM "
                        + Contants.TABLE_NAME.DOWNLOAD + " where packagename = ? ";
                cursor = database.rawQuery(sql, new String[]{pkg});
                while (cursor.moveToNext()) {
                    int percent = 0;
                    if (!TextUtils.isEmpty(cursor.getString(7))) {
                        File tempFile = new File(cursor.getString(7));
                        if (tempFile.exists()) {
                            long length = tempFile.length();
                            long totalSize = cursor.getLong(5);
                            if (0 == totalSize) {
                                percent = 0;
                            } else {
                                percent = (int) (length * 100 / totalSize);
                            }
                        }
                    }
                    if (!cursor.getString(3).equals(mContext.getPackageName())) {
                        DownloadItem item = new DownloadItem(cursor.getString(0),
                                cursor.getString(4), cursor.getString(1),
                                cursor.getString(2), percent, cursor.getInt(6),
                                cursor.getString(3), cursor.getLong(5), 0, "",
                                cursor.getString(7), cursor.getString(8),
                                cursor.getString(9));
                        list.add(item);
                    }
                }
                
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return list;
    }

    /**
     * 根据DownloadItem查询是否有数据
     * 
     * @param url
     * @return true:DB存在该DownloadItem数据
     * @return false:DB不存在该DownloadItem数据
     */
    public synchronized boolean isHasInfos(DownloadItem item) {
        if (null == item || TextUtils.isEmpty(item.getVersionName())
                || !Util.isDownloadUrl(item.getmURL())) {
            return true;
        }
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT COUNT(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " WHERE packagename = ? AND versionname = ?";
                cursor = database.rawQuery(sql,new String[] { item.getmPackageName(),item.getVersionName()});
                cursor.moveToFirst();
                count = cursor.getInt(0);
                log.i("isHasInfos:count=" + count);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return count != 0;
    }

    /**
     * 根据packageName查询DB中是否有数据
     * 
     * @param packageName
     * @return true:DB中存在该packageName对应数据
     * @return false:DB中不存在该packageName对应数据
     */
    public synchronized boolean isHasInfo(String packageName, String versionName) {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT COUNT(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " WHERE packagename = ? AND versionName = ?";
                cursor = database.rawQuery(sql, new String[] {packageName, versionName });
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            // log.i("isHasInfos:count=" + count);
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return count != 0;
    }
    
    /**
     * 根据packageName查询DB中是否有数据
     * 
     * @param packageName
     * @return true:DB中存在该packageName对应数据
     */
    public synchronized boolean isHasInfo(String packageName) {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT COUNT(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " WHERE packagename = ?";
                cursor = database.rawQuery(sql, new String[] {packageName });
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            // log.i("isHasInfos:count=" + count);
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return count != 0;
    }

    

    /**
     * 保存下载信息
     * 
     * @param infos
     */
    public synchronized void save(DownloadItem item) {
        log.d("save " + item.getmName());
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "INSERT INTO " + Contants.TABLE_NAME.DOWNLOAD
                        + " (name,size,icon,packagename,url,totalsize,state,path,versionname,gamecode) VALUES (?,?,?,?,?,?,?,?,?,?)";
                Object[] args = { item.getmName(), item.getmSize(),
                        item.getmLogoURL(), item.getmPackageName(), item.getmURL(),
                        item.getmTotalSize(), item.getmState(), item.getPath(),
                        item.getVersionName(), item.getGameCode() };
                database.execSQL(sql, args);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 获取正在下载,等待的游戏
     * 
     * @return
     */
    public synchronized int getDownloadingTaskSize() {
        int taskCount = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT count(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " where state=? or state=? or state=? ";
                String[] args = new String[] {
                        String.valueOf(DownloadParam.C_STATE.DOWNLOADING),
                        String.valueOf(DownloadParam.C_STATE.WAITING),
                        String.valueOf(DownloadParam.C_STATE.PAUSE) };
                cursor = database.rawQuery(sql, args);
                cursor.moveToFirst();
                taskCount = cursor.getInt(0);
            }
        } catch (Exception e) {
            taskCount = 0;
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskCount;
    }

    /**
     * 获取正在下载,等待的,暂停的游戏wuzheng
     * 
     * @return
     */
    public synchronized int getDownloadPauseTaskSize() {
        int taskCount = 0;
        SQLiteDatabase database = null;
        Cursor cursor = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT count(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " where state=? or state=? or state=?";
                String[] args = new String[] {
                        String.valueOf(DownloadParam.C_STATE.DOWNLOADING),
                        String.valueOf(DownloadParam.C_STATE.WAITING),
                        String.valueOf(DownloadParam.C_STATE.PAUSE) };
                cursor = database.rawQuery(sql, args);
                cursor.moveToFirst();
                taskCount = cursor.getInt(0);
            }
        } catch (Exception e) {
            taskCount = 0;
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskCount;
    }
    /**
     * 获取正在下载中的游戏个数 wuzheng
     * 
     * @return
     */
    public synchronized int getDownloadTaskSize() {
        int taskCount = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT count(*) FROM " + Contants.TABLE_NAME.DOWNLOAD + " where state=?";
                String[] args = new String[] {String.valueOf(DownloadParam.C_STATE.DOWNLOADING)};
                cursor = database.rawQuery(sql, args);
                cursor.moveToFirst();
                taskCount = cursor.getInt(0);
            }
        } catch (Exception e) {
            taskCount = 0;
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return taskCount;
    }


    /**
     * 根据包名和版本号获取下载进度百分比
     * 
     * @param apkUrl
     * @return
     */
    public synchronized int getFilePercentByPackageName(String packName, String versionName) {
        int filepercent = 0;
        Cursor cursor = null;
        log.d("getFilePercent --" + packName);
        if (null == packName || "".equals(packName.trim())) {
            return 0;
        }
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT totalsize,path FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " where packagename=? AND versionname = ?";
                String[] args = new String[] { packName, versionName };
                cursor = database.rawQuery(sql, args);
                while (cursor.moveToNext()) {
                    if (!TextUtils.isEmpty(cursor.getString(1))) {
                        File tempFile = new File(cursor.getString(1));
                        if (tempFile.exists()) {
                            long length = tempFile.length();
                            long totalSize = cursor.getLong(0);
                            if (0 != totalSize) {
                                filepercent = (int) (length * 100 / totalSize);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            filepercent = 0;
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return filepercent;
    }

    /**
     * 更新下载文件长度信息和文件保存路径
     * 
     * @param i
     * @param l
     * @param s
     */
    public synchronized void updateItem(DownloadItem item) {
        log.d("updateTotalSize:" + item.getmName() + ":" + item.getmTotalSize());
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
//                String sql = "UPDATE "
//                        + Contants.TABLE_NAME.DOWNLOAD
//                        + " SET totalsize = ? , state = ?,path = ? WHERE packagename = ? AND versionName = ?";
//                Object[] args = { item.getmTotalSize(), item.getmState(),
//                        item.getPath(), item.getmPackageName(),
//                        item.getVersionName() };
                ContentValues values = new ContentValues();
                values.put("totalsize", item.getmTotalSize());
                values.put("state", item.getmState());
                values.put("path", item.getPath());
                log.i("execSQL update");
                database.update(Contants.TABLE_NAME.DOWNLOAD, values, "packagename = ? AND versionName = ?", new String[]{item.getmPackageName(),item.getVersionName()});
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
//            closeReadDb();
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    /**
     * 清除所有数据
     */
    public synchronized void clean() {
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "DELETE FROM " + Contants.TABLE_NAME.DOWNLOAD;
                database.execSQL(sql);
            }
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
        }
    }

    // add by xuhan
    public synchronized boolean isDownloaded(String packageName, String versionName) {
        int count = 0;
        Cursor cursor = null;
        SQLiteDatabase database = null;
        try {
            synchronized (lock) {
                database = mDBHelper.getReadableDatabase();
                String sql = "SELECT COUNT(*) FROM " + Contants.TABLE_NAME.DOWNLOAD
                        + " WHERE packagename = ? AND versionName = ? AND state= ?";
                cursor = database.rawQuery(sql,new String[] { packageName, versionName,
                        String.valueOf(DownloadParam.C_STATE.DONE) });
                cursor.moveToFirst();
                count = cursor.getInt(0);
            }
            // log.i("isHasInfos:count=" + count);
        } catch (Exception e) {
            log.e(e);
        } finally {
            if (database != null && database.isOpen()) {
                database.close();
            }
            if (cursor != null) {
                cursor.close();
            }
        }
        return count != 0;
    }
}
