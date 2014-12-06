package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;

public class MessageDBHelper extends SQLiteOpenHelper {

	public MessageDBHelper(Context context) {
		super(context, Config.DBAbout.MESSAGE, null, Config.DBAbout.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" " +
				"(id integer PRIMARY KEY AUTOINCREMENT," +
				"type char," +
				"mid integer," +//消息id
				"uid integer," +
				"nickname char," +
				"avatar char," +
				"aid integer," +//动态id
				"msg char," +
				"acttext char," +//动态内容段落
				"actpic char," +//动态缩略图
				"msgmemo char," +//消息简介
				"isread integer," +
				"srctype integer,"+//消息来源
				"gmname char,"+
				"date long)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if(oldVersion < 16){
			db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" ADD COLUMN srctype integer");
			db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" ADD COLUMN gmname char");
		}
//		if(newVersion > oldVersion){
//			if(oldVersion == 6 && newVersion == 7){
//				db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.RELATIONCIRCLE_MSG+" ADD msgmemo char");
//			}
//		}
	}

}
