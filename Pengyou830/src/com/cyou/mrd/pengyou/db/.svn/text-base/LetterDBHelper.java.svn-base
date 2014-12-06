package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;

public class LetterDBHelper extends SQLiteOpenHelper{

	private CYLog log = CYLog.getInstance();
	
	public LetterDBHelper(Context context) {
		super(context, Config.DBAbout.LETTER, null, Config.DBAbout.DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.RECENT_USER+" (" +
				"id integer PRIMARY KEY AUTOINCREMENT," +
				"uid integer," +
				"avatar char,"+
				"nickname char,"+
				"time long)"); 
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Cursor c = null;
		switch (newVersion) {
		case 8:
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN msgId char";
					db.execSQL(alterSql);
				}
			} catch (Exception e) {
				log.e(e);
			}finally{
				if(c != null){
					c.close();
				}
			}
			break;
		case 9:
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN sendSuccess char";
					db.execSQL(alterSql);
				}
			} catch (Exception e) {
				log.e(e);
			}finally{
				if(c != null){
					c.close();
				}
			}
			break;
		case 10:
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN type integer";
					db.execSQL(alterSql);
				}
			} catch (Exception e) {
				log.e(e);
			}finally{
				if(c != null){
					c.close();
				}
			}
			break;
		case 12:
			try {
				db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.RECENT_USER+" ADD COLUMN avatar char");
				db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.RECENT_USER+" ADD COLUMN nickname char");
			} catch (Exception e) {
				log.e(e);
			}
			break;
			
		case 21:
		
			break;
		
		default:
			break;
		}
		if(oldVersion < 21){
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN "+LETTER.MSGSEQ+" char";
					db.execSQL(alterSql);
				}
				
				
			} catch (Exception e) {
				log.e(e);
			}finally{
				if(c != null){
					c.close();
				}
			}
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN "+LETTER.SEND_STATE+" integer";
					db.execSQL(alterSql);
				}
				
				
			} catch (Exception e) {
				log.e(e);
			}finally{
				if(c != null){
					c.close();
				}
			}
		}
		if(newVersion > oldVersion){
			try {
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				if(c.moveToFirst()){
					while(c.moveToNext()){
						int uid = c.getInt(0);
						String s = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN "+LETTER.SEND_STATE+" integer";
						db.execSQL(s);
					} 
				}
				
			} catch (Exception e) {
				log.e("数据库升级异常："+e);
				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
				c = db.rawQuery(sql, null);
				while(c.moveToNext()){
					int uid = c.getInt(0);
					String s = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN "+LETTER.SEND_STATE+" integer";
					db.execSQL(s);
				}
			}finally{
				if(c != null){
					c.close();
				}
			}
			
			
			
		}
//		if(oldVersion==18&&newVersion==21){
//			try {
//				String sql = "SELECT uid FROM "+Contants.TABLE_NAME.RECENT_USER;
//				c = db.rawQuery(sql, null);
//				while(c.moveToNext()){
//					int uid = c.getInt(0);
//					String alterSql = "ALTER TABLE "+Contants.TABLE_NAME.LETTER+"_"+uid+" ADD COLUMN "+LETTER.SEND_STATE+" integer";
//					db.execSQL(alterSql);
//				}
//				
//				
//			} catch (Exception e) {
//				log.e(e);
//			}finally{
//				if(c != null){
//					c.close();
//				}
//			}
//		}
	}

	public static class LETTER{
		public static final String ID = "id";
		public static final String FROM = "from_uid";
		public static final String TO = "to_uid";
		public static final String CREATEDATE = "createdate";
		public static final String CONTENT = "content";
		public static final String NICKNAME = "nickname";
		public static final String AVATAR = "avatar";
		public static final String ISREAD = "isread";
		public static final String TA_UID = "ta_uid";
		public static final String MSG_ID = "msgId";
		public static final String GAME_NAME = "gamename";
		public static final String RATING = "rating";
		public static final String ICON = "icon";
		public static final String SEND_SUCCESS = "sendSuccess";
		public static final String GAME_CODE = "gamecode";
		public static final String TYPE = "type";//Contants.CHAT_TYPE.TEXT/Contants.CHAT_TYPE.GAME
		public static final String MSGSEQ = "msgseq";//标示消息唯一的标示
		public static final String SEND_STATE = "send_state";//发送状态
	}
	
	public static class SEND_STATE {
		public static final int SENDING = 1;//正在发送
		public static final int SEND_SUCCESS = 2;//发送成功
		public static final int SEND_FAILED = 3;//发送失败
	}
}
