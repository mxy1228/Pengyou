package com.cyou.mrd.pengyou.db.provider;

import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.log.CYLog;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

public class MyGameProvider extends ContentProvider {

	public static final String URI = "content://com.cyou.mrd.pengyou.mygame.game_changed";
	private CYLog log = CYLog.getInstance();
	
	private MyGameDao mDao;
	
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		mDao.delete(selection);
		getContext().getContentResolver().notifyChange(uri, null);
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		log.d("insert");
//		mDao.insert(values);
		mDao.insertUpdate(values);
		getContext().getContentResolver().notifyChange(uri, null);
		return uri;
	}

	@Override
	public boolean onCreate() {
		mDao = new MyGameDao(getContext());
		return false;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		mDao.update(values.getAsString(DBHelper.MYGAME.PACKAGE_NAME), System.currentTimeMillis());
		getContext().getContentResolver().notifyChange(uri, null);
		return 0;
	}

}
