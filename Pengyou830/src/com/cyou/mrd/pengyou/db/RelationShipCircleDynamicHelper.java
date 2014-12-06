package com.cyou.mrd.pengyou.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicPic;

public class RelationShipCircleDynamicHelper extends SQLiteOpenHelper {

    public RelationShipCircleDynamicHelper(Context context) {
        super(context, Config.DBAbout.RELATIONSHIP_DYNAMIC, null, Config.DBAbout.DB_VERSION);
    }
/*
private static final long serialVersionUID = 1L;
	private int aid = -1;
	private int uid = -1;
	private String nickname;
	private String text;//文字内容
	private int type;
	private int supportcnt;
	private int commentcnt;
	private long createdtime;
	private int supported;//当前用户是否已经赞过，1-当前用户已赞过，0-当前用户没有赞过
	private String avatar;    

	private DynamicPic picture;
	private DynamicPic picturesmall;
	private DynamicPic picturemiddle;

	private int gender = 1; // 2:female 1:male
	private int cursor = 0;
	
	private int star = 0;
	
	private String path;
	private int width;
	private int height;

*/
    @Override
    public void onCreate(SQLiteDatabase db) {
//    	db.execSQL("DROP TABLE IF EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE);
//    	db.execSQL("DROP TABLE IF EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME);
//    	db.execSQL("DROP TABLE IF EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT);
//    	db.execSQL("DROP TABLE IF EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER);
    	
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "uid integer," +
                "text char," +
                "createdtime long," +
                "nickname char," +
                "type integer," +
                "dynamictype integer," +
                "supportcnt integer," +
                "supported integer," +
                "gender integer," +               
                "commentcnt integer," +
                "avatar char," +
                "star char," + 
                "cursor char," +
                
                "pic_lp char," +
                "pic_lw integer," +
                "pic_lh integer," + 
       
                "pic_mp char," +
                "pic_mw integer," +
                "pic_mh integer," + 
       
                "pic_sp char," +
                "pic_sw integer," +
                "pic_sh integer" + 
        		        		
        		")");
        
        /*
         * private int aid = -1;  //动态ID
	private String gamecode;
	private String gamenm;//游戏名称
	private String gametype;
	private String gameicon;
	private int playnum;
	private String gamedesc;//游戏简介
	private int platform;
	private int dynamicType = 0; // 0:关系圈， 1：广场 2：游戏圈
	
         * 
         * */
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_GAME+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "platform integer," +
                "gamecode char," +
                "gametype char," +
                "gameicon char," +
                "playnum integer," +
                "gamedesc char," +
                "gamenm char," +
                "dynamictype integer" +               
                
        		        		
        		")");
    /*
     * 	private int cid  = -1;//评论id
		private int aid  = -1;//
		private int dynamicType = 0; // 0:关系圈， 1：广场 2：游戏圈
		private String comment;
		private long timestamp;//评论时间戳
		private int uid;
		private String nickname;
		private String avatar;
		private DynamicCommentReplyItem replyto = new DynamicCommentReplyItem();
	    private int sendSuccess = 0; //0 sucess  1: fail
     * */
        
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_COMMENT+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "cid integer," +
                "comment char," +
                "dynamictype int," +
                "timestamp long," +
                "uid integer," +
                "nickname char," +
                "avatar char," +
                "reuid integer," +   
                "rename char," +    
                "success integer" +            
        		        		
        		")");
        /*
         * 
         * private int uid;
	private String nickname;
	private String avatar;
	private int aid = -1;  //动态ID
	private int dynamicType = 0; // 0:关系圈， 1：广场 2：游戏圈
	
         * */
        
        db.execSQL("CREATE TABLE IF NOT EXISTS "+Contants.TABLE_NAME.DYNAMIC_RELATION_CIRCLE_SUPPORTER+" " +
                "(id integer PRIMARY KEY AUTOINCREMENT," +
                "aid integer," +
                "uid integer," +
                "dynamictype integer," +
                "nickname char," +
                "avatar char," +          
                "cancel integer," + 
                "fail integer" +  
        		")");
        
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion){
            
        }
    }

}

