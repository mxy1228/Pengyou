package com.cyou.mrd.pengyou.db.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import com.cyou.mrd.pengyou.db.MyGamePlayRecordDao;
import com.cyou.mrd.pengyou.log.CYLog;

public class MyGamePlayRecordProvider extends ContentProvider {
	public static final String URI = "content://com.cyou.mrd.pengyou.mygameplayrecord.changed";
	private CYLog log = CYLog.getInstance();
	
	private MyGamePlayRecordDao mDao;
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		mDao.delete(selectionArgs[0], selectionArgs[1]);
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		log.d("insert");
		mDao.insert(values);
		getContext().getContentResolver().notifyChange(uri, null);
		return null;
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mDao = new MyGamePlayRecordDao(getContext());
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
		// TODO Auto-generated method stub
		return 0;
	}

}
