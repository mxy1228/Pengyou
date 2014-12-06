package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;

public class GameDynamicDBHelper extends SQLiteOpenHelper {

    public GameDynamicDBHelper(Context context) {
        super(context, Config.DBAbout.GAME_DYNAMIC, null, Config.DBAbout.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "pid integer," +
                "content char," +//发布文字信息
                "picture char," +//消息id
                "date long," +//发布时间
                "status integer,"+
                "score float," +
                "gamecode char," +
                "gameCircleID char," +
                "uid integer," +
                "width integer," +
                "height integer)");// +
                //"uid integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
        	try {
       	        db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO+" ADD COLUMN width integer");
                db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.GAME_DYNAMIC_INFO+" ADD COLUMN height integer");
			} catch (Exception e) {
				// TODO: handle exception
			}
        }
    }

}
