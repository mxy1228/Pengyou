package com.cyou.mrd.pengyou;

import java.io.File;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.ChatActivity;
import com.cyou.mrd.pengyou.ui.GameStoreActivity;
import com.cyou.mrd.pengyou.ui.LoginActivity;
import com.cyou.mrd.pengyou.ui.MainActivity;
import com.cyou.mrd.pengyou.ui.MyGameActivity;
import com.cyou.mrd.pengyou.ui.RelationCircleActvity;
import com.cyou.mrd.pengyou.utils.LogHandler;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.cyou.mrd.pengyou.R;

public class CyouApplication extends Application {

	private CYLog log = CYLog.getInstance();
	// 统计功能的开关
	public static boolean TIMER_SWITCH = true;
	public static Context mAppContext;
	private static ChatActivity chatActivity = null;
//	private static MyFriendFragment myFriendFragment = null;// 我的好友
	private static MainActivity mainActiv = null;// 主页
	/*
	 * 判断用户关闭程序通过back 还是home 以及关闭时的tab 索引
	 */
	public static boolean isOpenApp = false;
	public static int returnHomeIndex = Contants.RETURNHOME.RETURN_HOME;//
	private static MyGameActivity myGameActivity;// "玩游戏"页面实例
	private static GameStoreActivity gameStoreAct;
	private static Properties pro;
	private static RelationCircleActvity relationCircleAct;
	public static boolean  isControlTabsVisi =  false;
	private TimerReceiver mTimerReceiver;
	@Override
	public void onCreate() {
		super.onCreate();
		log.e("onCreate");
		loadPropertiesData();
		init();
		initBitmapManager();
		initFile();
		countInstallFirstOpen();
		if(!SharedPreferenceUtil.hadCreateShortCut()){
			createShortCut();
		}
		try {
			openCrashError();
		} catch (Exception e) {
			log.e(e);
		}
		registTimerReceiver();
		// jump2Launch();
	}

	/**
	 * 加载配置文件
	 */
	private void loadPropertiesData() {
		pro = new Properties();
		try {
			InputStream is = this.getAssets().open("conf.properties");
			pro.load(is);
			// 是否debug模式
			String isDebug = pro.getProperty("is_debug").trim();
			if ("1".equals(isDebug)) {
				PYVersion.DEBUG = true;
			} else {
				PYVersion.DEBUG = false;
			}
			String hostIp = pro.getProperty("host_ip").trim();
			String iconHostIp = pro.getProperty("icon_host").trim();
			String imgHostIp = pro.getProperty("img_host").trim();
			String apkUrl = pro.getProperty("apk_url").trim();
			String pushUrl = pro.getProperty("push_url").trim();
			String gameImgUrl=pro.getProperty("game_img_url").trim();
			String jifenUrl=pro.getProperty("jifen_url").trim();
			if (!TextUtils.isEmpty(hostIp) && hostIp.contains("http:")) {
				PYVersion.IP.HOST_IP = hostIp;
			}
			if (!TextUtils.isEmpty(gameImgUrl) && gameImgUrl.contains("http:")) {
				PYVersion.IP.GAME_IMG_IP = gameImgUrl;
			}
			if (!TextUtils.isEmpty(iconHostIp) && hostIp.contains("http:")) {
				PYVersion.IP.ICON_HOST = iconHostIp;
			}
			if (!TextUtils.isEmpty(imgHostIp) && hostIp.contains("http:")) {
				PYVersion.IP.IMG_HOST = imgHostIp;
			}
			if (!TextUtils.isEmpty(apkUrl) && hostIp.contains("http:")) {
				PYVersion.IP.APK_URL = apkUrl;
			}
			if (!TextUtils.isEmpty(pushUrl) && hostIp.contains("http:")) {
				PYVersion.IP.PUSH_URL = pushUrl;
			}
			if(!TextUtils.isEmpty(jifenUrl) && jifenUrl.contains("http:")){
				PYVersion.IP.JIFEN_IP = jifenUrl;
			}
			PYVersion.SMARTMAD_SWITCH = pro.getProperty("SmartMad").equals("1") ? true : false;
			PYVersion.INMOBI_SWITCH = pro.getProperty("inmobi").equals("1") ? true : false;
			PYVersion.SMARTMAD_CODE = pro.getProperty("SmartMadCode");
			PYVersion.INMOBI_CODE  = pro.getProperty("inmobiCode");
		} catch (Exception e) {
			e.fillInStackTrace();
			System.exit(0);
		}
	}
	public static String getChannel(){
		if(pro==null){
			return "";
		}
		String ak = pro.getProperty("channel").trim();
		return ak;
	}
	public static String getMadHouseCode(){
		if(pro==null){
			return "";
		}
		String code = pro.getProperty("madhouseCode").trim();
		return code;
	}
	/**
	 * 判断是否打开madhouse SDK
	 * @return
	 */
	public static boolean isMadHouseSdk(){
		if(pro==null){
			return false;
		}
		String isMadhouse=pro.getProperty("madhouse");
		if(TextUtils.isEmpty(isMadhouse)){
			return false;
		}
		if("1".equals(isMadhouse)){
			return true;
		}
		return false;
	}
	private void init() {
		this.mAppContext = getApplicationContext();
	}

	private void openCrashError() {
		LogHandler crashHandler = LogHandler.getInstance();
		crashHandler.init(this);
	}

	private void countInstallFirstOpen() {
		if (SharedPreferenceUtil.isInstallFirstOpen()) {
			log.i("countInstallFirstOpen");
			RequestParams params = new RequestParams();
			params.put("country", Locale.getDefault().getCountry());
			params.put("device", Util.getPhoneUA());
			params.put("downloadType", getString(R.string.app_name));
			params.put("deviceSystem", Util.getSystemVersion());
			params.put("networkType",
					Util.getNetType(CyouApplication.mAppContext));
			params.put("prisonBreak", Util.hasRootPer() == true ? "1" : "0");
			if (!TextUtils.isEmpty(Util
					.getNetExtraInfo(CyouApplication.mAppContext))) {
				String[] netInfoArr = Util.getNetExtraInfo(
						CyouApplication.mAppContext).split("_");
				if (netInfoArr != null && netInfoArr.length == 2) {
					params.put("operatorId", netInfoArr[0]);
					params.put("operator", netInfoArr[1]);
				}
			}
			MyHttpConnect.getInstance().post(
					HttpContants.NET.INSTALL_FIRST_OPEN, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(int statusCode, String content) {
							log.i("install first open result = " + content);
						}
					});
			SharedPreferenceUtil.saveInstallFirstOpen();
		}
	}

	/**
	 * 构建BitmapManager 使用universal-image-loader-1.8.4
	 */
	private void initBitmapManager() {
		File diskCache = StorageUtils.getOwnCacheDirectory(
				getApplicationContext(), Config.BitmapManagerAbout.CACHE_DIR);
		ImageLoader loader = ImageLoader.getInstance();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPoolSize(Config.BitmapManagerAbout.THREAD_SIZE)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				// 防止内存中保存同一张不同尺寸图片导致错乱
				.memoryCacheSize(Config.BitmapManagerAbout.MEMORY_CACHE_SIZE)
				.discCache(new UnlimitedDiscCache(diskCache))
				.discCacheSize(Config.BitmapManagerAbout.DISK_CACHE_SIZE)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())
				.imageDownloader(
						new BaseImageDownloader(getApplicationContext(),
								Config.BitmapManagerAbout.CONNECT_TIME_OUT,
								Config.BitmapManagerAbout.READ_TIME_OUT))
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		loader.init(config);
	}

	public static ChatActivity getChatActivity() {
		return chatActivity;
	}

	public static void setChatActivity(ChatActivity chatActivity) {
		CyouApplication.chatActivity = chatActivity;
	}

//	public static MyFriendFragment getMyFriendFragment() {
//		return myFriendFragment;
//	}
//
//	public static void setMyFriendFragment(MyFriendFragment myFriendFragment) {
//		CyouApplication.myFriendFragment = myFriendFragment;
//	}

	public static MainActivity getMainActiv() {
		return mainActiv;
	}

	public static void setMainActiv(MainActivity mainActiv) {
		CyouApplication.mainActiv = mainActiv;
	}

	/**
	 * 初始化文件路径
	 */
	private void initFile() {
		log.d("initFilePath");
		try {
			String rootPath = "";
			if (Environment.getExternalStorageState().equals(
					Environment.getExternalStorageState())) {
				if (Environment.getExternalStorageDirectory().canWrite()) {
					rootPath = Environment.getExternalStorageDirectory()
							.getAbsolutePath();
				}
			} else {
				if (Environment.getDownloadCacheDirectory().canWrite()) {
					rootPath = Environment.getDownloadCacheDirectory()
							.getAbsolutePath();
				} else {
					rootPath = Environment.getDataDirectory().getAbsolutePath()
							+ Contants.PATH.DATA_PATH;
				}
			}
			rootPath = rootPath + Contants.PATH.ROOT_PATH;
			SharedPreferenceUtil
					.saveRootPath(getApplicationContext(), rootPath);
			log.d("root path = "
					+ SharedPreferenceUtil.getRootPath(getApplicationContext()));
			File rootDir = new File(rootPath);
			if (!rootDir.exists()) {
				rootDir.mkdirs();
				log.i("create dir:" + rootDir.getAbsolutePath());
			}
			// CyouApplication.PATH.AVATAR_PATH = rootPath + "avatar.png";
			String logPath = rootPath + Contants.PATH.LOG_PATH;
			SharedPreferenceUtil.saveLogPath(getApplicationContext(), logPath);
			log.d("log path = "
					+ SharedPreferenceUtil.getLogPath(getApplicationContext()));
			File logDir = new File(logPath);
			if (!logDir.exists()) {
				logDir.mkdirs();
				log.i("create dir:" + logDir.getAbsolutePath());
			}
			// 统计日志
			String behaviorPath = rootPath + Contants.PATH.BEHAVIOR_PATH;
			SharedPreferenceUtil.saveBehaviorPath(behaviorPath);
			log.d("behavior log path = "
					+ SharedPreferenceUtil.getBehaviorPath());
			File behaviorDir = new File(behaviorPath);
			if (!behaviorDir.exists()) {
				behaviorDir.mkdirs();
				log.i("create dir:" + behaviorDir.getAbsolutePath());
			}

			String apkPath = rootPath + Contants.PATH.APK_PATH;
			SharedPreferenceUtil.saveAPKPath(getApplicationContext(), apkPath);
			log.d("apk path = "
					+ SharedPreferenceUtil.getAPKPath(getApplicationContext()));
			File apkDir = new File(apkPath);
			if (!apkDir.exists()) {
				apkDir.mkdirs();
				log.i("create dir:" + apkDir.getAbsolutePath());
			}

			String imgPath = rootPath + Contants.PATH.IMG_PATH;
			SharedPreferenceUtil.saveImgPath(getApplicationContext(), imgPath);
			log.d("img path = "
					+ SharedPreferenceUtil.getImgPath(getApplicationContext()));
			File iPath = new File(imgPath);
			if (!iPath.exists()) {
				iPath.mkdirs();
				log.i("create dir:" + iPath.getAbsolutePath());
			}

		} catch (Exception e) {
			log.e(e);
		}
	}
	
	private void registTimerReceiver(){
		if(mTimerReceiver == null){ 
			mTimerReceiver = new TimerReceiver();
		}
		registerReceiver(mTimerReceiver, new IntentFilter(Intent.ACTION_TIME_TICK));
	}

	/**
	 * 创建快捷方式
	 */
	private void createShortCut() {
		Intent target = new Intent(this, LoginActivity.class);
		Intent intent = new Intent(
				"com.android.launcher.action.INSTALL_SHORTCUT");
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
				Intent.ShortcutIconResource.fromContext(this, R.drawable.icon));
		intent.putExtra("duplicate", false);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, target);
		sendBroadcast(intent);
		SharedPreferenceUtil.createShortCut();
	}

	/**
	 * 删除快捷方式
	 */
	private void deleteShortCut() {
		try {
			Intent intent = new Intent(
					"com.android.launcher.action.UNINSTALL_SHORTCUT");
			// PackageManager pm = getPackageManager();
			// String title =
			// pm.getApplicationLabel(pm.getApplicationInfo(getPackageName(),
			// PackageManager.GET_META_DATA)).toString();
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
					getString(R.string.app_name));
			// Intent iDelete =
			// getPackageManager().getLaunchIntentForPackage(getPackageName());
			// intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, new
			// Intent(Intent.ACTION_MAIN).setComponent(component));
			sendBroadcast(intent);
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
//	 * 判断是否创建了快捷方式
//	 * 
//	 * @return
//	 */
//	private boolean hadCreateShortCut() {
//		try {
//			SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
//			return sp.getBoolean(Params.SP_PARAMS.HAD_CREATE_SHORT_CUT, false);
//		} catch (Exception e) {
//			log.e(e);
//		}
//		return false;
//	}




	public static GameStoreActivity getGameStoreAct() {
		return gameStoreAct;
	}

	public static MyGameActivity getMyGameActivity() {
		return myGameActivity;
	}

	public static void setMyGameActivity(MyGameActivity myGameActivity) {
		CyouApplication.myGameActivity = myGameActivity;
	}

	public static void setGameStoreAct(GameStoreActivity gameStoreAct) {
		CyouApplication.gameStoreAct = gameStoreAct;
	}

	/**
	 * 是否提示更新
	 * 
	 * @return
	 */
	public static boolean isUpdate() {
		String isUpdate = pro.getProperty("is_support_update").trim();
		if ("1".equals(isUpdate)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 监听Intent.ACTION_TIME_TICK
	 * 每分钟检测CoreService的连接状态
	 * @author xumengyang
	 *
	 */
	private class TimerReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("TimerReceiver:onReceive");
			if(UserInfoUtil.getCurrentUserId() == 0){
				return;
			}
			Intent iCheck = new Intent("com.cyou.mrd.pengyou.service.CoreService");
			iCheck.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.JOIN);
			iCheck.putExtra(Params.PUBLISH.UTOKEN, UserInfoUtil.getUToken());
			startService(iCheck);
		}
		
	}

	public static RelationCircleActvity getRelationCircleAct() {
		return relationCircleAct;
	}

	public static void setRelationCircleAct(RelationCircleActvity relationCircleAct) {
		CyouApplication.relationCircleAct = relationCircleAct;
	}
	
	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		super.onLowMemory();
	}
}
