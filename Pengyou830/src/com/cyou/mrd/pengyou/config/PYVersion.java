package com.cyou.mrd.pengyou.config;

public class PYVersion {

	public static boolean DEBUG = true;
	public static String SMARTMAD_CODE = "";//SmartMad渠道号
	public static String INMOBI_CODE = "";//inmobi渠道号
	public static Boolean SMARTMAD_SWITCH = false;
	public static Boolean INMOBI_SWITCH = false;
	/**
	 * 测试服务器
	 * @author xumengyang
	 *
	 */
	public static class IP{
		//接口服务器地址
		public static  String HOST_IP ="http://111.206.12.99";
		//游戏图片服务器地址
		public static  String ICON_HOST = "http://respy.changyou.com/";
		//用户图片服务器地址
		public static  String IMG_HOST = "http://111.206.12.99/";
		// 分享信息路径
		public static  String APK_URL = "http://py.changyou.com";
		// 通过SMS分享信息
		public static  String APK_URL_PY = "http://py.changyou.com";
		//推送服务器地址
		public static  String PUSH_URL = "http://111.206.12.99:9011/mc/cometd";
		//游戏库轮播图地址
		public static  String GAME_IMG_IP ="http://http://111.206.12.99/cms/";
		//积分
		public static String JIFEN_IP="http://111.206.12.99/jifen/";
	}
//	
	/**
	 * 正式服务器
	 * @author xumengyang
	 *
	 */
//	public static class IP{
//		 //业务接口服务器地址
//		public static final String HOST_IP ="http://webpy.changyou.com/pyserver";
//		 //游戏图片服务器地址
//		public static  String ICON_HOST = "http://respy.changyou.com/";
//		 //用户图片服务器地址
//		public static  String IMG_HOST = "http://respy.changyou.com/";
//		//推送服务器地址
//		public static  String PUSH_URL = "http://notipy.changyou.com/msg/mc/cometd";
//		// 分享信息路径
//		public static  String APK_URL = "http://url.cn/WjaPWB";
	//游戏库轮播图地址
//		public static  String GAME_IMG_IP ="http://respy.changyou.com/";
//	}
	
//	/**
//	 * CMS测试服务器
//	 * @author xumengyang
//	 *
//	 */
//	public static class IP{
//		//接口服务器地址
//		public static  String HOST_IP ="http://111.206.12.99/pyweb";
//		//游戏图片服务器地址
//		public static  String ICON_HOST = "http://111.206.12.99/cms/";
//		//用户图片服务器地址
//		public static  String IMG_HOST = "http://111.206.12.99/";
//		// 分享信息路径
//		public static  String APK_URL = "http://url.cn/WjaPWB";
//		//推送服务器地址
//		public static  String PUSH_URL = "http://124.248.36.198/msg/mc/cometd";
//		//游戏库轮播图地址
//		public static  String GAME_IMG_IP ="http://111.206.12.99/cms/";
//	}
}
