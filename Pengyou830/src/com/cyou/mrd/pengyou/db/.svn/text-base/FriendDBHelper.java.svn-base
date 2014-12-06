package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;

public class FriendDBHelper extends SQLiteOpenHelper {

	public FriendDBHelper(Context context) {
		super(context, Config.DBAbout.FRIEND, null, Config.DBAbout.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// 我的好友
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ Contants.TABLE_NAME.FRIEND_LIST + " "
				+ "("+FRIEND.ID+" integer PRIMARY KEY AUTOINCREMENT, " 
				+ ""+FRIEND.UID+" integer," 
				+ ""+FRIEND.NICKNAME+" char,"
				+ ""+FRIEND.GENDER+" char,"
				+ ""+FRIEND.RECENTGMS+" char,"
				+ ""+FRIEND.GAMECOUNT+" integer,"
				+ ""+FRIEND.TIME+" long,"
				+ ""+FRIEND.PICTURE+" char)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(newVersion == 11){
			String sql = "ALTER TABLE "+Contants.TABLE_NAME.FRIEND_LIST+" ADD time long";
			db.execSQL(sql);
		}
	}

	public static class FRIEND{
		public static final String ID = "id";
		public static final String UID = "uid";
		public static final String NICKNAME = "nickname";
		public static final String GENDER = "gender";
		public static final String RECENTGMS = "recentgms";
		public static final String GAMECOUNT = "gamecount";
		public static final String TIME = "time";
		public static final String PICTURE = "picture";
	}
}
