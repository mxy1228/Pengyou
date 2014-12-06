package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;

public class DBHelper extends SQLiteOpenHelper {

	private CYLog log = CYLog.getInstance();
	private static DBHelper mInstance;

	public DBHelper(Context context) {
		super(context, Config.DBAbout.DB_NAME, null, Config.DBAbout.DB_VERSION);
	}
	
	public synchronized static DBHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DBHelper(context);
		}
		return mInstance;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// 创建下载管理记录表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.DOWNLOAD
				+ " " + "(id integer PRIMARY KEY AUTOINCREMENT, "
				+ "name char," + "size char," + "icon char,"
				+ "packagename char," + "totalsize long," + "url char,"
				+ "state integer," + "versionname char,"
				+ "path char,gamecode char)");
		// 创建更新任务表
		db.execSQL("CREATE TABLE IF NOT EXISTS "
				+ Contants.TABLE_NAME.UPDATE_TASK + " "
				+ "(id integer primary key autoincrement,"
				+ "packageName char," + "name char," + "icon char,"
				+ "version char," + "cansaveup char," + "patchurl char,"
				+ "patchsize char," + "fullurl char," + "fullsize char,"
				+ "path char," + "state integer)");
		// 创建我的游戏表
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.MY_GAME
				+ " " + "(id integer primary key autoincrement,"
				+ MYGAME.PACKAGE_NAME+" char," 
				+ MYGAME.GAME_CODE+" char," 
				+ MYGAME.NAME+" char,"
				+ MYGAME.ICON+" char," 
				+ MYGAME.VERSION+" char," 
				+ MYGAME.VERSION_CODE+" char,"
				+ MYGAME.FULL_URL+" char," 
				+ MYGAME.FULL_SIZE+" char," 
				+ MYGAME.PLAYER_NUM+" char,"
				+ MYGAME.STAR+" char," 
				+ MYGAME.GCACTS+" char," 
				+ MYGAME.TIME_STAMP+" char,"
				+ MYGAME.GCID + " char,"
				+ MYGAME.LOCAL_VERSION + " char,"
				+ MYGAME.RECENT_STOP_PLAY_DATE+" long,"      //新增
				+ MYGAME.RECENT_PLAY_DURATION + " char,"     //新增
				+ MYGAME.HAS_SCORE + " integer,"     //新增
				+ MYGAME.IS_SHOWN+" integer)");
		
		// 创建我的游戏玩耍时间轴
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.MY_GAME_PLAY_RECORD
				+ " " + "(id integer primary key autoincrement,"
				+ "packageName char,"
				+ "begin integer," 
				+ "end integer,"
		        + "uid char)");
		
		// 创建游戏下载完成并安装记录
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD
				+ " " + "(id integer primary key autoincrement,"
				+ "packageName char,"
		        + "uid char)");

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_NAME.LETTER);
			db.execSQL("CREATE TABLE IF NOT EXISTS "
					+ Contants.TABLE_NAME.MY_GAME + " "
					+ "(id integer primary key autoincrement,"
					+ "packageName char," + "gamecode char," + "name char,"
					+ "icon char," + "version char," + "versionCode char,"
					+ "fullurl char," + "fullsize char," + "playerNum char,"
					+ "star char," + "isShown integer)");
			if (oldVersion == 6 && newVersion == 7) {
				db.execSQL("ALTER TABLE " + Contants.TABLE_NAME.DOWNLOAD
						+ " ADD versionname char");
			}
			if(oldVersion == 10 && newVersion == 11){
				db.execSQL("ALTER TABLE " + Contants.TABLE_NAME.MY_GAME
						+ " ADD gcacts char");
				db.execSQL("ALTER TABLE " + Contants.TABLE_NAME.MY_GAME
						+ " ADD timestamp char");
			}
			try {
				db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.MY_GAME+" ADD "+MYGAME.GCID+" char");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				db.execSQL("ALTER TABLE "+Contants.TABLE_NAME.MY_GAME+" ADD "+MYGAME.LOCAL_VERSION+" char");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				db.execSQL("ALTER TABLE " + Contants.TABLE_NAME.MY_GAME
						+ " ADD gcacts char");
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE " + Contants.TABLE_NAME.MY_GAME
						+ " ADD timestamp long");
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE "+ Contants.TABLE_NAME.MY_GAME+ " ADD recentStopPlayDate long");//结束游戏的时间点
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE "+ Contants.TABLE_NAME.MY_GAME+ " ADD recentPlayDuration char");//玩游戏的时长
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE "+ Contants.TABLE_NAME.MY_GAME+ " ADD hasScore integer");//>0显示金币
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE "+ Contants.TABLE_NAME.MY_GAME_PLAY_RECORD+ " ADD uid char");//uid
			} catch (Exception e) {
				log.e(e);
			}
			try {
				db.execSQL("ALTER TABLE "+ Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD+ " ADD uid char");//uid
			} catch (Exception e) {
				log.e(e);
			}
		}
		try {
			// 时间片段表
			db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_NAME.MY_GAME_PLAY_RECORD);
			db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.MY_GAME_PLAY_RECORD
					+ " " + "(id integer primary key autoincrement,"
					+ "packageName char,"
					+ "begin integer," 
					+ "end integer,"
			        + "uid char)");
		} catch (Exception e) {
			log.e(e);
		}
		try {
			// 游戏下载并安装记录
			db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD);
			db.execSQL("CREATE TABLE IF NOT EXISTS " + Contants.TABLE_NAME.GAME_DOWNLOAD_RECORD
					+ " " + "(id integer primary key autoincrement,"
					+ "packageName char,"
			        + "uid char)");
		} catch (Exception e) {
			log.e(e);
		}
		try {
			// 下载管理列表
			db.execSQL("DROP TABLE IF EXISTS " + Contants.TABLE_NAME.DOWNLOAD);
			db.execSQL("CREATE TABLE IF NOT EXISTS "
					+ Contants.TABLE_NAME.DOWNLOAD + " "
					+ "(id integer PRIMARY KEY AUTOINCREMENT, " + "name char,"
					+ "size char," + "icon char," + "packagename char,"
					+ "totalsize long," + "url char," + "state integer,"
					+ "versionname char," + "path char," + "gamecode char"
					+ ")");
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	public static class MYGAME{
		public static final String PACKAGE_NAME = "packageName";
		public static final String GAME_CODE = "gamecode";
		public static final String NAME = "name";
		public static final String ICON = "icon";
		public static final String VERSION = "version";
		public static final String VERSION_CODE = "versionCode";
		public static final String FULL_URL = "fullurl";
		public static final String FULL_SIZE = "fullsize";
		public static final String PLAYER_NUM = "playerNum";
		public static final String STAR = "star";
		public static final String GCACTS = "gcacts";
		public static final String TIME_STAMP = "timestamp";
		public static final String RECENT_STOP_PLAY_DATE = "recentStopPlayDate"; //最后一次结束游戏的时间点
		public static final String RECENT_PLAY_DURATION = "recentPlayDuration";//最后一次玩的时长，字符串类型
		public static final String HAS_SCORE = "hasScore";//是否显示金币button
		public static final String IS_SHOWN = "isShown";
		public static final String GCID = "gcid";
		public static final String LOCAL_VERSION = "localversion";


	}
	
	public static class MYGAMEPLAYRECORD{
		public static final String PACKAGE_NAME = "packageName";
		public static final String BEGIN = "begin";
		public static final String END = "end";
		public static final String UID = "uid";
	}
	
	public static class GAMEDOWNLOADRECORD {
		public static final String PACKAGE_NAME = "packageName";
	}
}
