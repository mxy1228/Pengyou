package com.cyou.mrd.pengyou.config;

public class Config {
	/**
	 * BitmapManager相关参数
	 * @author xumengyang
	 *
	 */
	public static final String IMGTYPE=".png";
	//圆角弧度
	public static final int ROUND = 15;
	
	public static final int IMAGE_MAX_WIDTH = 480;
	
	public static final int IMAGE_MAX_HEIGHT = 800;
	
	public static boolean  needResendComment = false;
	
	public static boolean  needResendSupport = false;
	
	public static final  int dynamicCharCountLimit = 160;
	public static final  int dynamicCharShowLimit = 157;
	
	public static int screenWidthWithDip = 0;
	
	public static float SreenDensity = 1.5f;
	
	public static boolean isAskConfirmNetChange = true;//在非wifi网络下载时时候需要用户确认
	
	/**
	 * 每页请求个数
	 * @author wangkang
	 *
	 */
	public static final int imageBrightness = -40;
	public static final int PAGE_SIZE=10;
	public static final int PAGE_SIZE_RELATION_CIRCLE=25;
	public static final int PAGE_SIZE_RECOMMEND = 20;
	public static class BitmapManagerAbout{
		public static final int THREAD_SIZE = 3;//BitmapManager下载线程数量
		public static final int MEMORY_CACHE_SIZE = 1024 * 1024 * 5;//BitmapManager最大内存大小（单位：MB）
		public static final int DISK_CACHE_SIZE = 1024 * 1024 * 10;//BitmapManager本地缓存大小（单位：MB）
		public static final String CACHE_DIR = "pengyou/image_cache";//物理缓存存储目录
		public static final int READ_TIME_OUT = 5 * 1000;//读取超时时间
		public static final int CONNECT_TIME_OUT = 5 * 1000;//连接超时时间
	}
	
	public static class DBAbout{
		public static final String DB_NAME = "pengyou.db";
		public static final String FRIEND = "friend.db";
		public static final String LETTER = "letter.db";
		public static final String MESSAGE = "message.db";
		public static final String DYNAMIC = "dynamic.db";
		public static final String GAME_DYNAMIC = "dynamicgame.db";
		public static final String RELATIONSHIP_DYNAMIC = "relationshipdynamic.db";
		public static final int DB_VERSION = 21;
	}
	
	public static class DownloadAbout{
		public static final int DOWNLOAD_COUNT = 3;//同时下载任务数
	}
	public static final String APK_SUFFIX=".apk";//安装包后缀
	public static final int USERICON_WIDTH=250;//用户头像裁剪宽度
	public static final int USERICON_HEIGHT=250;//用户头像裁剪高度
	public static final int EDITUSERICON_WIDTH=150;//编辑个人信息显示宽度
	public static final int EDITUSERICON_HEIGTH=150;//编辑个人信息显示宽度
	
	public static class USER_EDIT{
		public static final int USER_SIGN_MAX_COUNT=40;
	}
	
	public static class CHAT_BRROADCAST_PRIORITY{
		public static final int CHATACTIVITY = 500;
		public static final int MY_MESSAGE_FRAGMENT = 400;
	}
	
	/**
	 * 玩游戏列表的Item是否显示标示
	 * @author xumengyang
	 *
	 */
	public static class MY_GAME_ACTIVITY{
		public static final int YES = 1;
		public static final int NO = 0;
	}
}
