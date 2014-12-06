package com.cyou.mrd.pengyou.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.security.cert.Certificate;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StatFs;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.TextUtils;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.RelationCircleMsgDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadManager;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.IntroSubComment;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.ui.LoginActivity;
import com.cyou.mrd.pengyou.widget.ViewToast;

public class Util {
	private static String TAG = "Util";
	private static CYLog log = CYLog.getInstance();
	private static long ONE_DAY = 24 * 60 * 60 * 1000;
	private static long ONE_HOUR = 60 * 60 * 1000;
	private static Toast toast;

	public static String getGameCodeByUrl(String url, String type) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url)) {
			return "";
		}
		String gameCode = url.replace("cyou://" + type + "/", "");
		return gameCode;
	}

	/**
	 * 游戏库首页-顶部轮播图 根据服务端返回的uri获取对应的跳转类型
	 * 
	 * @param url
	 * @return
	 */
	public static String getAdsType(String url) {
		if (TextUtils.isEmpty(url) || TextUtils.isEmpty(url)) {
			return "";
		}
		String type = "";
		try {
			type = url.substring(url.indexOf("cyou://") + "cyou://".length(),
					url.indexOf("/", "cyou://".length()));
		} catch (Exception e) {
			log.e(e);
		}
		if (TextUtils.isEmpty(type)) {
			type = url.replace("cyou://", "");
		}
		return type;
	}

	public static boolean isDownloadUrl(String url) {
		log.d("Check Download Url is :" + url);
		if (TextUtils.isEmpty(url)) {
			return false;
		}
		// if(url.contains("http") && url.contains(".apk")){
		// return true;
		// }
		return true;
	}

	/**
	 * 获取用户头像保存地址
	 * 
	 * @param context
	 * @return
	 */
	public static String getPersonalAvatarPath(Context context) {
		SharedPreferences setting_sp = context.getSharedPreferences(
				Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		SharedPreferences user_sp = context.getSharedPreferences(
				Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return new StringBuffer()
				.append(setting_sp.getString(Params.SP_PARAMS.ROOT_PATH, null))
				.append(user_sp.getInt(Params.SP_PARAMS.KEY_UID, 0))
				.append(".png").toString();
	}

	public static void hideSoftInput(Activity mActivity, View view) {
		try {
			mActivity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			if (null != view) {
				InputMethodManager imm = (InputMethodManager) mActivity
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 获取用户昵称
	 * 
	 * @param context
	 * @return
	 */
	public static String getNickName(Context context) {
		SharedPreferences sp = context.getSharedPreferences(
				Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		return sp.getString(Params.SP_PARAMS.KEY_NICKNAME, null);
	}

	/**
	 * return XXX
	 * 
	 * @param context
	 * @param size
	 * @return
	 */
	public static String getGameSize(String size) {
		if (TextUtils.isEmpty(size)) {
			return "0";
		}
		try {
			return new DecimalFormat("0.00").format(Float.valueOf(size)
					/ (1024 * 1024));
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * return XXX
	 * 
	 * @param context
	 * @param size
	 * @return
	 */
	public static String getGameIntSize(String size) {
		if (TextUtils.isEmpty(size)) {
			return "0";
		}
		try {
			return new DecimalFormat("0").format(Float.valueOf(size)
					/ (1024 * 1024));
		} catch (Exception e) {
			return "0";
		}
	}

	/**
	 * 获取屏幕宽度分辨率
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getScreenWidthSize(Activity mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (null != dm) {
			return dm.widthPixels;
		}
		return 0;
	}

	/**
	 * 获取屏幕宽度分辨率
	 * 
	 * @param mContext
	 * @return
	 */
	public static int getScreenHeightSize(Activity mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		if (null != dm) {
			return dm.heightPixels;
		}
		return 0;
	}

	/**
	 * 获取imei地址 需要有电话功能设备 获取不是很可靠
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getUDIDNum() {
		TelephonyManager mTm = (TelephonyManager) CyouApplication.mAppContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = getAndroidID();
		}
		if (TextUtils.isEmpty(imei)) {
			imei = getSerialNum();
		}
		if (TextUtils.isEmpty(imei)) {
			imei = getMacByLinuxCode();
		}
		return imei == null ? "" : imei;
	}
	
	public static String getIMSI(){
		TelephonyManager mTm = (TelephonyManager) CyouApplication.mAppContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		log.d("imsi = "+mTm.getSubscriberId());
		return mTm.getSubscriberId();
	}

	/**
	 * 验证手机号码格式
	 * 
	 * @param num
	 * @return
	 */
	public static boolean isPhoneNum(String num) {
		try {
			Pattern p = Pattern
					.compile("^((13[0-9])|(15[^4,\\D])|(18[0-9]))\\d{8}$");
			Matcher m = p.matcher(num);
			return m.matches();
		} catch (Exception e) {
			log.e(e);
		}
		return false;
	}

	/**
	 * Android ID
	 * 
	 * @return
	 */
	public static String getAndroidID() {
		String androidId = Settings.Secure.getString(
				CyouApplication.mAppContext.getContentResolver(),
				Settings.Secure.ANDROID_ID);
		if (TextUtils.isEmpty(androidId)) {
			return "";
		}
		return androidId;
	}

	/**
	 * 获取设备序列号
	 * 
	 * @return
	 */
	public static String getSerialNum() {
		String serialnum = "";
		try {
			Class<?> c = Class.forName("android.os.SystemProperties");
			Method get = c.getMethod("get", String.class, String.class);
			serialnum = (String) (get.invoke(c, "ro.serialno", "unknown"));
			return serialnum;
		} catch (Exception e) {
			e.fillInStackTrace();
			return "";
		}
	}

	/**
	 * 通过linux命令获取mac地址 若wifi正在使用 则无法获取
	 * 
	 * @return
	 */
	public static String getMacByLinuxCode() {
		String macSerial = null;
		String str = "";
		try {
			Process pp = Runtime.getRuntime().exec(
					"cat /sys/class/net/wlan0/address ");
			InputStreamReader ir = new InputStreamReader(pp.getInputStream());
			LineNumberReader input = new LineNumberReader(ir);
			for (; null != str;) {
				str = input.readLine();
				if (str != null) {
					macSerial = str.trim();// 去空格
					break;
				}
			}
		} catch (IOException ex) {
			// 赋予默认值
			ex.printStackTrace();
			macSerial = "";
		}
		return macSerial;
	}

	/**
	 * 获取手机型号
	 * 
	 * @return
	 */
	public static String getPhoneUA() {
		return Build.MODEL == null ? "NAN" : Build.MODEL;
	}

	/**
	 * 手机制造商
	 * 
	 * @return
	 */
	public static String getProduct() {
		return Build.PRODUCT;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getSdkCode() {
		return android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 登陆国家ID
	 * 
	 * @return
	 */
	public static String getCountryID() {
		return Locale.getDefault().getCountry();
	}

	/**
	 * 获取登陆国家名称
	 * 
	 * @return
	 */
	public static String getCountryName() {
		return Locale.getDefault().getDisplayCountry();
	}

	/**
	 * 获取机型
	 * 
	 * @return
	 */
	public static String getPhoneModel() {
		return android.os.Build.MODEL;
	}

	/**
	 * 获取系统版本号
	 * 
	 * @return
	 */
	public static String getSystemVersion() {
		return "Android" + android.os.Build.VERSION.RELEASE;
	}

	/**
	 * 获取上网方式
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getNetType(Context mContext) {
		String netType = "";
		ConnectivityManager connectionManager = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectionManager.getActiveNetworkInfo();
		if (null != info && info.isAvailable()) {
			netType = info.getTypeName();
		}
		return netType;
	}

	/**
	 * 获取运营商信息
	 * 
	 * @param mContext
	 * @return 通过_区分id和名称
	 */
	public static String getNetExtraInfo(Context mContext) {
		String netExtraInfo = "";
		TelephonyManager mTm = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		if (mTm.getSimState() == TelephonyManager.SIM_STATE_READY) {
			netExtraInfo = mTm.getSimOperator();
			if (null != netExtraInfo) {
				if (netExtraInfo.equals("46000")
						|| netExtraInfo.equals("46002")
						|| netExtraInfo.equals("46007")) {
					// 中国移动
					netExtraInfo = netExtraInfo + "_" + "中国移动";
				} else if (netExtraInfo.equals("46001")) {

					// 中国联通
					netExtraInfo = netExtraInfo + "_" + "中国联通";
				} else if (netExtraInfo.equals("46003")) {

					// 中国电信
					netExtraInfo = netExtraInfo + "_" + "中国电信";
				} else {
					netExtraInfo = netExtraInfo + "_" + "其他";
				}
			}
		}
		return netExtraInfo;
	}

	/**
	 * 根据yyyy-MM-dd格式日期获取年龄
	 * 
	 * @param s
	 * @return
	 */
	public static int getAge(long birthday) {
		if (birthday != 0) {
			try {
				Date date = new Date(birthday * 1000);
				Date now = new Date();
				int current_year = now.getYear();
				int birthday_year = date.getYear();
				return current_year - birthday_year;
			} catch (Exception e) {
				log.e(e);
			}
		} else {
			log.e("birthday is 0");
		}
		return 23;
	}

	/**
	 * 根据yyyy-MM-dd格式日期获取星座
	 * 
	 * @param s
	 * @return
	 */
	public static String getConstellation(long birthday) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date date = new Date(Long.valueOf(birthday * 1000));
			String[] dateStr = format.format(date).split("-");
			final String[] constellationArr = CyouApplication.mAppContext
					.getResources().getStringArray(R.array.constellation);
			final int[] costellationEdgeDay = { 20, 18, 20, 20, 20, 21, 22, 22,
					22, 22, 21, 21 };
			int month = Integer.parseInt(dateStr[1]);
			int day = Integer.parseInt(dateStr[2]);
			if (day <= costellationEdgeDay[month - 1]) {
				month = month - 1;
			}
			if (month >= 0) {
				return constellationArr[month];
			}
			return constellationArr[11];
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}

	public static String getDateForH(long l) {
		Date date = new Date(l * 1000);
		return (new SimpleDateFormat("yyyy-MM-dd")).format(date);
	}

	public static String getDate(long l) {
		Date date = new Date(l * 1000);
		Date today = new Date(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		if (format.format(date).equals(format.format(today))) {
			return new SimpleDateFormat("HH:mm").format(date);
		} else {
			return new SimpleDateFormat("MM-dd").format(date);
		}
	}

	public static String getFormatDate(long date) {
		String dateStr = "刚刚";
		date = date * 1000;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Long time = System.currentTimeMillis() - (date);
		if (time <= 0) {
			return dateStr;
		} else if (time <= 1 * 3600 * 1000) {
			dateStr = new SimpleDateFormat("mm", Locale.getDefault())
					.format(time) + " 分钟前";
			if (dateStr.startsWith("00")) {
				dateStr = "刚刚";
				return dateStr;
			}
			if (dateStr.startsWith("0")) {
				dateStr = dateStr.substring(1);
				return dateStr;
			}
		} else if (time <= 24 * 3600 * 1000) {
			if (!format.format(date).equals(
					format.format(System.currentTimeMillis()))) {
				dateStr = "昨天 "
						+ new SimpleDateFormat("HH:mm", Locale.getDefault())
								.format(date);
				return dateStr;
			} else {
				dateStr = "今天 "
						+ new SimpleDateFormat("HH:mm", Locale.getDefault())
								.format(date);
				return dateStr;
			}
		} else if (time <= 2 * 24 * 3600 * 1000) {
			if (format.format(System.currentTimeMillis() - (24 * 3600 * 1000))
					.equals(format.format(date))) {
				dateStr = "昨天 "
						+ new SimpleDateFormat("HH:mm", Locale.getDefault())
								.format(date);
				return dateStr;
			} else {
				dateStr = new SimpleDateFormat("MM-dd HH:mm",
						Locale.getDefault()).format(date);
				if (dateStr.startsWith("0")) {
					dateStr = dateStr.substring(0);
				}
				return dateStr;
			}
		} else if (time <= 365 * 24 * 3600 * 1000) {
			dateStr = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
					.format(date);
			if (dateStr.startsWith("0")) {
				dateStr = dateStr.substring(0);
			}
			return dateStr;
		} else {
			dateStr = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
					.format(date);
			return dateStr;
		}
		return dateStr;
	}

	public static String getDateTime(long l) {
		Date date = new Date(l * 1000);
		return (new SimpleDateFormat("yyyy/MM/dd HH:mm")).format(date);
	}

	/**
	 * 根据时间戳获取相差天数
	 * 
	 * @param time
	 * @return
	 */
	public static int getGapDate(long time) {
		return (int) (((new Date()).getTime() - time) / 24 / 60 / 60 / 1000);
	}

	/**
	 * 是否为当天
	 * 
	 * @param time
	 * @return
	 */
	public static boolean isToday(long time) {
		try {
			SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
			Date mDate = new Date(time * 1000);
			Date currentDate = new Date();
			String mDateString = sd.format(mDate);
			String mCurrentDateString = sd.format(currentDate);
			if (mDateString.equals(mCurrentDateString)) {
				return true;
			}
		} catch (Exception e) {
			log.e(e);
		}
		return false;
	}

	/**
	 * 得到IP地址
	 * 
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}
		} catch (SocketException ex) {
			ex.fillInStackTrace();
		}
		return "";
	}

	/**
	 * 是否有ROOT权限
	 * 
	 * @return
	 */
	public static synchronized boolean hasRootPer() {
		// 若已经ROOT 此代码直接弹出提示框 判断是否授权ROOT权限 影响体验
		// Process process = null;
		// DataOutputStream os = null;
		// try {
		// process = Runtime.getRuntime().exec("su");
		// os = new DataOutputStream(process.getOutputStream());
		// os.writeBytes("exit\n");
		// os.flush();
		// int exitValue = process.waitFor();
		// if (exitValue == 0) {
		// return true;
		// } else {
		// return false;
		// }
		// } catch (Exception e) {
		// Log.d("*** DEBUG ***", "Unexpected error - Here is what I know: "
		// + e.getMessage());
		// return false;
		// } finally {
		// try {
		// if (os != null) {
		// os.close();
		// }
		// process.destroy();
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		// }

		// 判断机器 Android是否已经root，即是否获取root权限
		try {
			Process p = Runtime.getRuntime().exec("echo test");
			int exitValue = p.waitFor();
			if (-1 != exitValue) {
				return true;
			}
		} catch (Exception e) {
			e.fillInStackTrace();
			return false;
		}
		return false;
	}

	/**
	 * dip转为px
	 * 
	 * @param context
	 * @param dip
	 * @return
	 */
	public static int dip2px(Context context, double dip) {
		try {
			float scale = context.getResources().getDisplayMetrics().density;
			return (int) (dip * scale + 0.5f);
		} catch (Exception e) {
			log.e(e);
		}
		return 0;
	}

	/**
	 * 判断是否安装目标应用
	 * 
	 * @param packageName
	 *            目标应用安装后的包名
	 * @return 是否已安装目标应用
	 */
	public static boolean isInstallByread(String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = CyouApplication.mAppContext.getPackageManager()
					.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			packageInfo = null;
			// e.printStackTrace();
		}
		if (packageInfo != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 获取安装文件的版本名
	 * 
	 * @param packageName
	 * @return
	 */
	public static String getAppVersionName(String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = CyouApplication.mAppContext.getPackageManager()
					.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			packageInfo = null;
		}
		if (null == packageInfo) {
			return null;
		}
		return packageInfo.versionName;
	}

	/**
	 * 获取安装文件的版本号
	 * 
	 * @param packageName
	 * @return
	 */
	public static int getAppVersionCode(String packageName) {
		PackageInfo packageInfo;
		try {
			packageInfo = CyouApplication.mAppContext.getPackageManager()
					.getPackageInfo(packageName, 0);
		} catch (Exception e) {
			packageInfo = null;
		}
		if (null == packageInfo) {
			return 0;
		}
       log.d("-----本地安装游戏的versioncode"+packageInfo.versionCode);
		return packageInfo.versionCode;
	}

	/**
	 * 通过verionName判断应用是否更新
	 * 
	 * @param localVersionCode
	 *            本地版本号
	 * @param netGameVersionCode
	 *            //网络版本号
	 * @return
	 */
	public static boolean isUpdate(int localVersionCode,
			String netGameVersionCode) {
		boolean isUpadate = false;
		try {
			int version2 = Integer.parseInt(netGameVersionCode);
			if (version2 > localVersionCode) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		// int verLengh1 = versionName1.length();
		// int verLengh2 = versionName2.length();
		// int isLengh = 0;
		// if (verLengh1 > verLengh2) {
		// isLengh = verLengh2;
		// } else {
		// isLengh = verLengh1;
		// }
		// if (versionName1.contains(versionName2)) {
		// return false;
		// }
		// if (versionName2.contains(versionName1)) {
		// return true;
		// }
		// for (int i = 0; i < isLengh; i++) {
		// char temp1 = versionName1.charAt(i);
		// char temp2 = versionName2.charAt(i);
		// if (temp1 == '.' && temp2 == '.') {
		// continue;
		// } else {
		// if (temp1 == '.') {
		// return true;
		// } else if (temp2 == '.') {
		// return false;
		// }
		// }
		// try {
		// if (Character.isDigit(temp1) && Character.isDigit(temp2)) {
		// if (temp1 > temp2) {// 若1大于2 则不更新
		// return false;
		// } else if (temp1 < temp2) {
		// return true;
		// } else {
		// continue;
		// }
		// } else {
		// return false;
		// }
		//
		// } catch (Exception e) {
		// e.fillInStackTrace();
		// }
		// }
		return isUpadate;
	}

	/**
	 * 返回true表示本地已经安装的版本和当前显示的版本相同 通过verioncode判断当前游戏是否和已经安装的游戏版本相同 wuzheng
	 * 
	 * 
	 * @param localVersionCode
	 *            本地版本号
	 * @param netGameVersionCode
	 *            //网络版本号
	 * @return
	 */
	public static boolean isInstalledGame(int localVersionCode,
			String netGameVersionCode) {
		boolean isInstalledGame = false;
		try {
			int version2 = Integer.parseInt(netGameVersionCode);
			if (version2 == localVersionCode) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return isInstalledGame;
	}

	/**
	 * wuzheng 判断当前游戏版本和本地已安装游戏版本是否相同
	 * 
	 * @return
	 */
	public static boolean isInstallVersion(String localVersion,
			String netGameVersion) {
		boolean isInstallVersion = false;
		try {
			// int version2 = Integer.parseInt(netGameVersion);

			if (localVersion.equals(netGameVersion)) {
				return true;
			}
		} catch (Exception e) {
			return false;
		}
		return isInstallVersion;
	}

	/**
	 * 获取imei地址 需要有电话功能设备 获取不是很可靠
	 * 
	 * @param mContext
	 * @return
	 */
	public static String getPhoneImei() {
		TelephonyManager mTm = (TelephonyManager) CyouApplication.mAppContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		String imei = mTm.getDeviceId();
		if (TextUtils.isEmpty(imei)) {
			imei = getAndroidID();
		}
		if (TextUtils.isEmpty(imei)) {
			imei = getSerialNum();
		}
		if (TextUtils.isEmpty(imei)) {
			imei = getMacByLinuxCode();
		}
		return imei == null ? "" : imei;
	}

	/**
	 * 得到两个经纬度之间的距离
	 * 
	 * @param startLat
	 * @param startLon
	 * @param endLat
	 * @param endLon
	 */
	private static final double EARTH_RADIUS = 6378137;// 单位：米

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 得到两个坐标点之间的距离
	 * 
	 * @param lat1
	 *            维度1
	 * @param lng1
	 *            经度1
	 * @param lat2
	 *            维度2
	 * @param lng2
	 *            经度2
	 * @return 单位:米
	 */
	public static String getDistance(double lat1, double lng1, double lat2,
			double lng2) {
		if (lat1 == 0 || lng1 == 0 || lat2 == 0 || lng2 == 0) {
			return "0";
		}
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
				+ Math.cos(radLat1) * Math.cos(radLat2)
				* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return formatNum(s);
	}

	public static String getOneDouble(double d) {
		DecimalFormat df = new DecimalFormat(".#");
		String str = df.format(d);
		return str;
	}

	public static String getTwoDouble(double d) {
		if (d <= 0) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat(".##");
		String str = df.format(d);
		return str;
	}

	public static String getDouble(double d) {
		if (d <= 0) {
			return "0";
		}
		DecimalFormat df = new DecimalFormat("");
		String str = df.format(d);
		return str;
	}

	public static String formatNum(double value) {
		String retValue = null;
		DecimalFormat df = new DecimalFormat();
		df.setMinimumFractionDigits(0);
		df.setMaximumFractionDigits(0);
		retValue = df.format(value);
		retValue = retValue.replaceAll(",", "");
		return retValue;
	}

	/**
	 * 生成动态评论
	 * 
	 * @param nickname
	 * @param content
	 * @param uid
	 * @return
	 */
	public static String getDynamicComment(String nickname, String content,
			int uid) {
		// return "<p>"
		// +
		// "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
		// + uid + "\">" + "<font size='26' color='#265fa5'>" + nickname
		// + ":" + "</>" + "</a>" + "<font size='26' color='#333333'>"
		// + content + "</></p>";
		return "<br><a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ uid
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ nickname
				+ ":"
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ content + "</>";
	}

	/**
	 * 生成游戏版本列表
	 * 
	 * @param from
	 * @param size
	 * @param gameid
	 *            游戏版本号的唯一标示
	 * @return
	 */
	public static String getGameVersion(String from, String size, String gameUrl) {
		// return "<p>"
		// +
		// "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
		// + uid + "\">" + "<font size='26' color='#265fa5'>" + nickname
		// + ":" + "</>" + "</a>" + "<font size='26' color='#333333'>"
		// + content + "</></p>";
		return "<br><a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ gameUrl
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ from
				+ ":"
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ size + "</>";
	}

	private static String getSingleComment(String nickname, String content,
			int uid) {
		return "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ uid
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ nickname
				+ ":"
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ content + "</></p>";
	}

	public static String getSingleUser(String nickname, int uid, String content) {
		return "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ uid
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ nickname
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ ":"
				+ content + "</></p>";
	}

	public static String getReplyUser(String nickname, int uid, String rename,
			int reuid, String content) {
		return "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ uid
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ nickname
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ "回复"
				+ "</>"
				+ "<a style=\"text-decoration:none; color:#7c7c7c\" href=\"pengyou://friendinfo?uid="
				+ reuid
				+ "\">"
				+ "<font size='26' color='#265fa5'>"
				+ rename
				// + "回复:"
				+ "</>"
				+ "</a>"
				+ "<font size='26' color='#333333'>"
				+ ":"
				+ content + "</></p>";
	}

	public static StringBuilder comment2Html(List<IntroSubComment> data) {
		int length = data.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			IntroSubComment c = data.get(i);
			if (i != 0) {
				sb.append("<br>");
			}
			sb.append(getSingleComment(c.getNickname(), c.getText(), c.getUid()));
		}
		return sb;
	}

	public static StringBuilder dynamicComment2Html(
			List<DynamicCommentItem> data) {
		int length = data.size();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			DynamicCommentItem c = data.get(i);
			if (i != 0) {
				sb.append("<br>");
			}
			sb.append(getSingleComment(c.getNickname(), c.getComment(),
					c.getUid()));
		}
		return sb;
	}

	public static void initComment(TextView tv) {
		if (TextUtils.isEmpty(tv.getText().toString())) {
			return;
		}
		Spannable s = (Spannable) tv.getText();
		URLSpan[] spans = s.getSpans(0, s.length(), URLSpan.class);
		for (URLSpan span : spans) {
			int start = s.getSpanStart(span);
			int end = s.getSpanEnd(span);
			s.removeSpan(span);
			span = new MySpan(span.getURL());
			s.setSpan(span, start, end, 0);
		}
		tv.setText(s);
	}

	/**
	 * 请求更新粉丝数及消息数
	 */
	public static void requestFansAndLetterCount(Context context,String type) {
		if (context == null) {
			return;
		}
		Intent intent = new Intent(Contants.ACTION.SYSTEM_MSG_ACTION);
		intent.putExtra("type", type);
		context.sendBroadcast(intent);
	}

	/*
	 * 判断是否有网络连接
	 */

	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 获取字符串的长度，如果有中文，则每个中文字符计为2位
	 * 
	 * @param value
	 *            指定的字符串
	 * @return 字符串的长度
	 */
	public static int getLength(String value) {
		if (TextUtils.isEmpty(value)) {
			return 0;
		}
		int valueLength = 0;
		String chinese = "[\u0391-\uFFE5]";
		/* 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1 */
		for (int i = 0; i < value.length(); i++) {
			/* 获取一个字符 */
			String temp = value.substring(i, i + 1);
			/* 判断是否为中文字符 */
			if (temp.matches(chinese)) {
				/* 中文字符长度为2 */
				valueLength += 2;
			} else {
				/* 其他字符长度为1 */
				valueLength += 1;
			}
		}
		return valueLength;
	}

	public static int getScreenHeight(Activity mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		log.i("====================== location screen w:" + width);
		log.i("====================== location screen h:" + height);
		return height;
	}

	public static int getScreenWidth(Activity mContext) {
		DisplayMetrics dm = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(dm);
		int width = dm.widthPixels;
		int height = dm.heightPixels;
		log.i("====================== location screen w:" + width);
		log.i("====================== location screen h:" + height);
		return width;
	}

	public static int getSentenceLines(String Sentence, int ViewWidth,
			float size) {
		if (Sentence == null)
			return 0;
		// 获取字符串宽度
		Paint FontPaint = new Paint();
		FontPaint.setTextSize(size); // text size
		float fontWidth = FontPaint.measureText(Sentence);
		log.i("-------------Sentence: " + Sentence);
		log.i("-------------ViewWidth: " + ViewWidth);
		log.i("-------------FontWidth: " + fontWidth);
		// 计算行数
		int ClearlyLineNumber = (int) Math.floor(fontWidth / ViewWidth); // 字符串可见行数
		log.i("-------------ClearlyLineNumber: " + ClearlyLineNumber);
		float RemainLine = fontWidth % ViewWidth; // 不满一行（余数）
		log.i("-------------RemainLine: " + RemainLine);
		if (ClearlyLineNumber == 0 && RemainLine == 0) // 空行也算一行
			ClearlyLineNumber = 1;

		if (RemainLine != 0) // 有余数则需要额外的一行
			ClearlyLineNumber += 1;
		return ClearlyLineNumber;
	}

    /**
     * toast 显示不及时问题处理 tuozhonghua_zk 2013-9-26上午10:25:51
     * 
     * @param context
     * @param text
     * @param duration
     */
    public static void showToast(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text, duration);
        } else {
            toast.setText(text);
            toast.setDuration(duration);
        }
        toast.show();
    }
  
  /**
   * 
   * tuozhonghua_zk
   * 2013-10-29上午10:00:51
   *
   * @return
   */
  public static boolean isNetWorkAvailable() {
      ConnectivityManager connectivitymanager = (ConnectivityManager) CyouApplication.mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
      if (connectivitymanager == null) {
          log.i("network is bad");
          return false;
      }
      NetworkInfo info = connectivitymanager.getActiveNetworkInfo();
      if (info == null || !info.isAvailable()) {
          log.i("network is bad");
          return false;
      }
      return true;
    }
      
      /**
       * android 源码获取签名方式，此处绕过API，自己去获取签名
       * tuozhonghua_zk
       * 2013-11-4上午10:46:22
       *
       * @param archiveFilePath
       * @return
       */
    public static Signature[] getSignature(String archiveFilePath) {
        
        Signature[] mSignatures = null;
        byte[] readBuffer = null;
        if (readBuffer == null) {
            readBuffer = new byte[8192];
        }
        
        try {
            JarFile jarFile = new JarFile(archiveFilePath);
            Certificate[] certs = null;
            Enumeration entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry je = (JarEntry)entries.nextElement();
                if (je.isDirectory()) continue;
                if (je.getName().startsWith("META-INF/")) continue;
                Certificate[] localCerts = loadCertificates(jarFile, je,readBuffer);
                if (localCerts == null) {
                    jarFile.close();
                } else if (certs == null) {
                    certs = localCerts;
                    break;
                } else {
                    for (int i=0; i<certs.length; i++) {
                        boolean found = false;
                        for (int j=0; j<localCerts.length; j++) {
                            if (certs[i] != null &&
                                    certs[i].equals(localCerts[j])) {
                                found = true;
                                break;
                            }
                        }
                        if (!found || certs.length != localCerts.length) {
                            jarFile.close();
                        }
                    }
                }
            }
            jarFile.close();
            if (certs != null && certs.length > 0) {
                final int N = certs.length;
                mSignatures = new Signature[certs.length];
                for (int i=0; i<N; i++) {
                    mSignatures[i] = new Signature(certs[i].getEncoded());
                }
            } else {
                
            }
            
        } catch (Exception e) {
            
        }
        return mSignatures;
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-4上午10:45:46
     *
     * @param jarFile
     * @param je
     * @param readBuffer
     * @return
     */
    private static Certificate[] loadCertificates(JarFile jarFile, JarEntry je,byte[] readBuffer) {
        try {
            // We must read the stream for the JarEntry to retrieve
            // its certificates.
            InputStream is = new BufferedInputStream(jarFile.getInputStream(je));
            while (is.read(readBuffer, 0, readBuffer.length) != -1) {
                // not using
            }
            is.close();
            return je != null ? je.getCertificates() : null;
        } catch (IOException e) {
            Log.i(TAG, "Exception reading " + je.getName() + " in " + jarFile.getName());
        } catch (RuntimeException e) {
            Log.i(TAG, "Exception reading " + je.getName() + " in " + jarFile.getName(), e);
        }
        return null;
    }
    /**
     * 获取已安装应用的信息包括签名信息
     * tuozhonghua_zk
     * 2013-11-3下午1:01:09
     *
     * @param packageName
     * @return
     */
    public static PackageInfo getAppPackageInfo(String packageName) {
        PackageInfo packageInfo;
        try {
            packageInfo = CyouApplication.mAppContext.getPackageManager().getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
        } catch (Exception e) {
            packageInfo = null;
        }
        if (null == packageInfo) {
            return null;
        }
        return packageInfo;
    }
  /**
   * 比对签名
   * tuozhonghua_zk
   * 2013-11-4上午10:45:07
   *
   * @param s1
   * @param s2
   * @return
   */
  public static boolean IsSignaturesSame(Signature[] s1, Signature[] s2) {
      if (s1 == null) {
          return false;
      }
      if (s2 == null) {
          return false;
      }
      HashSet<Signature> set1 = new HashSet<Signature>();
          for (Signature sig : s1) {
              set1.add(sig);
      }
      HashSet<Signature> set2 = new HashSet<Signature>();
          for (Signature sig : s2) {
              set2.add(sig);
      }
      // Make sure s2 contains all signatures in s1.
      if (set1.equals(set2)) {
          return true;
      }
      return false;
      }
    /**
     * 卸载APP
     * tuozhonghua_zk
     * 2013-11-4上午10:44:39
     *
     * @param mContext
     * @param packageName
     */
    public static void unInstallApp(Context mContext ,String packageName) {
        try {
            Uri packageURI = Uri.parse("package:" + packageName);
            Intent uninstallIntent = new Intent(Intent.ACTION_DELETE,packageURI);
            uninstallIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(uninstallIntent);
        } catch (Exception e) {
            ViewToast.showToast(mContext, R.string.download_installapp_error, 0);
            log.e(e);
        }
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-3下午5:24:09
     *
     * @param context
     * @param filePath
     * @param pakcageName
     */
  public static boolean installApkDefaul(Context context,String filePath,String pakcageName){
      Log.d(TAG, "jing mo an zhuang");
      boolean isSuccess = false;
      File file = new File(filePath);
      int installFlags = 0;
      if(!file.exists())
          return isSuccess;
      //one method
//      installFlags |= PackageManager.INSTALL_REPLACE_EXISTING;
//      PackageManager pm = context.getPackageManager();
//      IPackageInstallObserver observer = new MyPakcageInstallObserver();
//      pm.installPackage(Uri.fromFile(file), null, installFlags, pakcageName);
      //second method
      try {
          isSuccess = execCommand("system/bin/pm install -f " + filePath);
          log.i("isSuccess=" + isSuccess);
      } catch (Exception e) {
        
      }
      return isSuccess;
  }
  
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-4上午10:44:50
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean uninstallApkDefaul(Context context,String packageName){
        boolean isSuccess = false;
//        PackageManager pm = context.getPackageManager();
//        IPackageDeleteObserver observer = new MyPackageDeleteObserver();
//        pm.deletePackage(packageName, observer, 0);
        try {
            isSuccess = execCommand("system/bin/pm uninstall " + packageName);
            log.i("isSuccess=" + isSuccess);
        } catch (Exception e) {
            
        }
        return isSuccess;
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-4上午10:44:56
     *
     * @param cmd
     * @return
     */
    private static boolean execCommand(String cmd) {
         Process process = null;
         try {
             process = Runtime.getRuntime().exec(cmd);
             process.waitFor();
         } catch (Exception e) {
             return false;
         } finally {
             try {
                 process.destroy();
             } catch (Exception e) {
             }
         }
         return true;
    }
    /**
     * 另一种获取签名的方法 调用系统影藏API
     * tuozhonghua_zk
     * 2013-11-4下午4:40:44
     *
     * @param apkPath
     * @param context
     * @return
     */
    public static Signature[] getApkSignature(String apkPath,Context context) {
        Signature mSignatures[] = null;
        try {
            Class clazz = Class.forName("android.content.pm.PackageParser");
            Method parsePackageMethod = clazz.getMethod("parsePackage", File.class, String.class, DisplayMetrics.class, int.class);
            
            Object packageParser = clazz.getConstructor(String.class).newInstance("");
            Object packag = parsePackageMethod.invoke(packageParser, new File(apkPath), null, context.getResources().getDisplayMetrics(), 0x0004);
            
            Method collectCertificatesMethod = clazz.getMethod("collectCertificates", Class.forName("android.content.pm.PackageParser$Package"), int.class);
            collectCertificatesMethod.invoke(packageParser, packag, PackageManager.GET_SIGNATURES);
            mSignatures = (Signature[]) packag.getClass().getField("mSignatures").get(packag);
            
        } catch (Exception e) {
            
        }
        return mSignatures;
    }
    
    /**
     * 处于非wifi环境的提示框
     * 2013-11-13
     *
     * @param context
     */
    public static void show3GNetDialog(final Context context,final DownloadManager downloadManager,final DownloadItem item) {
        AlertDialog dialog = new AlertDialog.Builder(context)
        	.setMessage(context.getResources().getString(R.string.download_not_wifi_message))
            .setTitle(context.getResources().getString(R.string.download_not_wifi_titile))
            .setPositiveButton(context.getResources().getString(R.string.download_not_wifi_continue), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	Config.isAskConfirmNetChange = false;
                	downloadManager.add(item);
                }})
            .setNegativeButton(context.getResources().getString(R.string.download_not_wifi_pause), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	downloadManager.addPause(item);
                    dialog.dismiss();
                }
            }).create();
            try {
                dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            } catch (Exception e) {
  
            }    
    }
    
    /**
     * 由wifi变为非wifi环境的提示框
     * 2013-11-13
     *
     * @param context
     */
    public static void showNetChangeDialog(final Context context,final DownloadManager downloadManager) {
        AlertDialog dialog = new AlertDialog.Builder(context)
        	.setMessage(context.getResources().getString(R.string.download_not_wifi_message))
            .setTitle(context.getResources().getString(R.string.download_not_wifi_titile))
            .setPositiveButton(context.getResources().getString(R.string.download_not_wifi_continue), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	Config.isAskConfirmNetChange = false;
                	downloadManager.contAll();
                }})
            .setNegativeButton(context.getResources().getString(R.string.download_not_wifi_pause), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            }).create();
            try {
                dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                dialog.setCanceledOnTouchOutside(true);
                dialog.show();
            } catch (Exception e) {
  
            }
    }
    /**
     * 是否为wifi网络环境
     * tuozhonghua_zk
     * 2013-11-19上午10:06:25
     *
     * @return
     */
    public static boolean is3GNetWork() {
        try {
            ConnectivityManager connectivitymanager = (ConnectivityManager) CyouApplication.mAppContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mobleInfo = connectivitymanager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            NetworkInfo wifiInfo = connectivitymanager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo activeInfo = connectivitymanager.getActiveNetworkInfo();
            if (mobleInfo.isConnected() && !wifiInfo.isConnected()) {
                return true;
            } 
        } catch (Exception e) {
            CYLog.getInstance().e(e);
        }
        return false;
    }
    
    /**
     * 安装前检测是否签名冲突
     * tuozhonghua_zk
     * 2013-11-4下午6:43:06
     *
     * @param mTempDonloadItem
     * @param context
     */
    public static void install(final DownloadItem mTempDonloadItem,final Context context) {
        File file = new File(mTempDonloadItem.getPath());
        SharedPreferenceUtil.setDownloadedInstallFile(context, "");
        String pkg = mTempDonloadItem.getmPackageName();
        if (file.exists()) {
            if (pkg != null && !"".equals(pkg)) {
            PackageInfo packageInfo = Util.getAppPackageInfo(pkg);
                if (packageInfo != null) {
                	//如果是朋游自身就不判断签名冲突了
                	if (pkg.equals(context.getPackageName())) {
                		installApk(file,context);
                	}else {
                		Signature sig1[] = packageInfo.signatures;
                		Signature sig2[] = Util.getSignature(mTempDonloadItem.getPath());
                		Boolean isSameSignature = Util.IsSignaturesSame(sig1, sig2);
                		log.i("isSameSignature=" + isSameSignature);
                		if (isSameSignature) {
                			installApk(file,context);
                		}else {
                			AlertDialog dialog = new AlertDialog.Builder(context)
                			.setMessage(context.getResources().getString(R.string.update_prompt_content, mTempDonloadItem.getmName()))
                			.setTitle(context.getResources().getString(R.string.update_prompt))
                			.setPositiveButton(context.getResources().getString(R.string.game_update_confirm), new DialogInterface.OnClickListener() {
                				@Override
                				public void onClick(DialogInterface dialog, int which) {
                					SharedPreferenceUtil.setDownloadedInstallFile(context, mTempDonloadItem.getPath());
                					Util.unInstallApp(context, mTempDonloadItem.getmPackageName());
                				}})
                				.setNegativeButton(context.getResources().getString(R.string.game_update_cancel), new DialogInterface.OnClickListener() {
                					@Override
                					public void onClick(DialogInterface dialog, int which) {
                						dialog.dismiss();
                					}
                				}).create();
                			try {
                				dialog.getWindow().setType((WindowManager.LayoutParams.TYPE_SYSTEM_ALERT));
                				dialog.setCanceledOnTouchOutside(true);
                				dialog.show();
                				
                			} catch (Exception e) {
                				log.e(e);
                			}
                		}
                		
                	}
                }else {
                    installApk(file,context);
                }
            }
        } else {
            Util.showToast(context, context.getResources().getString(R.string.download_install_file_noexit), Toast.LENGTH_SHORT);
//            deleteGame();
        }
        
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-11-4下午6:44:34
     *
     * @param file
     * @param context
     */
    public static void installApk (File file,Context context) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
        context.startActivity(i);
    }

	/**
	 * 过滤评论
	 * 
	 * @param input
	 * @return
	 */
	public static String filterComment(String input) {
		StringBuffer sb = new StringBuffer(input.length());
		char c;
		for (int i = 0; i < input.length(); i++) {
			c = input.charAt(i);
			switch (c) {
			case '<':
				sb.append("");
				break;
			case '>':
				sb.append("");
				break;
			default:
				sb.append(c);
				break;
			}
		}
		return sb.toString();
	}

	/*
	 * 获得屏幕宽度
	 */
	public static int getScreenDpWidth(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = activity.getResources().getDisplayMetrics();
		float density = dm.density;
		int width = dm.widthPixels;
		return (int) (width / density);
	}

	/*
	 * 获得屏幕密度
	 */
	public static float getScreenDensity(Activity activity) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = activity.getResources().getDisplayMetrics();
		return dm.density;
	}

	/*
	 * bichunhe touch picture, change the brightness
	 */
	public static OnTouchListener onTouchCaptureListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_UP:
				setViewLight((ImageView) view, 0);
				break;
			case MotionEvent.ACTION_DOWN:
				setViewLight((ImageView) view, Config.imageBrightness);
				break;
			case MotionEvent.ACTION_MOVE:
				// setViewLight((ImageView) view, 0);
				break;
			case MotionEvent.ACTION_CANCEL:
				setViewLight((ImageView) view, 0);
				break;
			default:
				break;
			}
			return false;
		}

	};

	private static void setViewLight(ImageView iv, int light) {
		ColorMatrix cMatrix = new ColorMatrix();
		cMatrix.set(new float[] { 1, 0, 0, 0, light, 0, 1, 0, 0, light, 0, 0,
				1, 0, light, 0, 0, 0, 1, 0 });
		iv.setColorFilter(new ColorMatrixColorFilter(cMatrix));
	}

	/**
	 * 退出登录
	 * boolean 是否需重启进程
	 */
	public static void loginOut(boolean retart,Context context) {
		try{
			String phoneNum = UserInfoUtil.getCurrentUserPhoneNum();
			SharedPreferences uerInfoSP = context
					.getSharedPreferences(Contants.SP_NAME.USER_INFO,
							Context.MODE_PRIVATE);
			Editor eUserInfo = uerInfoSP.edit();
			eUserInfo.clear();
			if(eUserInfo.commit()){
				eUserInfo.putString(Params.SP_PARAMS.KEY_PHONE, phoneNum);
				if(eUserInfo.commit()){
					WeiboApi.getInstance().unBindSina(context);
					SharedPreferences msgSP = context
							.getSharedPreferences(Contants.SP_NAME.SYSTEM_MSG,
									Context.MODE_PRIVATE);
					Editor msgE = msgSP.edit();
					msgE.clear();
					if(msgE.commit()){
						// 清除数据库
						new FriendDao(context).clean();
						new LetterDao(context).clean();
						new RelationCircleMsgDao(context).clean();
						new MyGameDao(context).clean();
						// 停止CoreService，防止推送服务器错乱
						Intent intent = new Intent(context,
								CoreService.class);
						context.stopService(intent);
						NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
						manager.cancel(R.id.letter_notification);
						//退出时取消下载广播
						manager.cancel(DownloadManager.DOWNLOAD_NOTIFICATION);
						if(retart){
//							if (CyouApplication.getMainActiv() != null) {
//								log.d("finish MainActivity");
//								CyouApplication.getMainActiv().finish();
//							} else {
//								log.d("MainActivity is null");
//							}
							UserInfoUtil.setUToken("");
//							intent = new Intent(Intent.ACTION_MAIN);
							intent = new Intent();
//							intent.addCategory(Intent.CATEGORY_HOME);
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//							Class.forName("com.cyou.mrd.pengyou.ui.LoginActivity").;
//							intent.setClassName(context.getPackageName(),"com.cyou.mrd.pengyou.ui.LoginActivity");
//							intent.setClass(context.getPackageName(),Class.forName("com.cyou.mrd.pengyou.ui.LoginActivity"));
							intent.setClass(context,LoginActivity.class);
							Log.d("fugaianzhuang", "util==1856");
//							intent.putExtra(Params.FROM, true);
							context.startActivity(intent);

//							android.os.Process.killProcess(android.os.Process.myPid());
						}
					}
				}
			}
			
		}catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 通过包名获取大小
	 * 
	 * @param packageName
	 * @return
	 */
	public static String getInstallGameSize(Context mContext, String packageName) {
		String fileSize = "0";
		try {
			if (TextUtils.isEmpty(packageName)) {
				return fileSize;
			}
			PackageInfo packageInfo = null;
			packageInfo = mContext.getPackageManager().getPackageInfo(
					packageName, 0);
			if (null == packageInfo) {
				return fileSize;
			}
			ApplicationInfo p = packageInfo.applicationInfo;
			String dir = p.publicSourceDir;
			fileSize = String.valueOf(new File(dir).length());
			fileSize = getGameSize(fileSize);
		} catch (Exception e) {
			log.e(e);
		}
		return fileSize;
	}
	/**
	 * 朋游启动时如果有下载，显示在通知栏
	 * tuozhonghua_zk
	 * 2013-11-18下午3:32:50
	 *
	 * @param myGameDao
	 * @param downloadDao
	 */
	public static void showDownloadNotification (MyGameDao myGameDao,DownloadDao downloadDao,Context context,NotificationManager mNotificationManager) {
		int downloadingNum = 0;
		int pauseNum = 0;
		int updateNum = 0;
		List<GameItem> updateList = new ArrayList<GameItem>();
		List<DownloadItem> downloadingList = new ArrayList<DownloadItem>();
		List<DownloadItem> pauseList = new ArrayList<DownloadItem>();
		downloadingList.addAll(downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.DOWNLOADING));
		downloadingList.addAll(downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.WAITING));
		pauseList = downloadDao.getDownloadItemsByState(DownloadParam.C_STATE.PAUSE);
		List<GameItem> tempList = new MyGameDao().selectAll();
		if (null != tempList && tempList.size() > 0) {
			for (int i = 0; i < tempList.size(); i++) {
				GameItem item = tempList.get(i);
				if (Util.isInstallByread(item.getIdentifier())) {
					boolean isUpdate = Util.isUpdate(Util.getAppVersionCode(item.getIdentifier()),item.getVersioncode());
					if (isUpdate) {
						updateList.add(item);
					}
				}
			}
		}
		downloadingNum = downloadingList.size();
		pauseNum = pauseList.size();
		updateNum = updateList.size();
		
		if (downloadingNum > 0) {
			StringBuffer content = new StringBuffer();
			for (DownloadItem item : downloadingList) {
                if (item != null) {
                    content.append(item.getmName());
                    content.append("、");
                }
            }
			showNotification(context.getResources().getString(R.string.notification_downloading_text, downloadingNum), 
					content.substring(0, content.length()-1),context,mNotificationManager);
		}else if (pauseNum > 0) {
			StringBuffer content = new StringBuffer();
			for (DownloadItem item : pauseList) {
                if (item != null) {
                    content.append(item.getmName());
                    content.append("、");
                }
            }
			showNotification(context.getResources().getString(R.string.notification_all_pause_text, pauseNum), 
					content.substring(0, content.length()-1),context,mNotificationManager);
		}else if (updateNum > 0) {
			StringBuffer content = new StringBuffer();
			for (GameItem item : updateList) {
                if (item != null) {
                    content.append(item.getName());
                    content.append("、");
                }
            }
			showNotification(context.getResources().getString(R.string.notification_update_text, updateNum), 
					content.substring(0, content.length()-1),context,mNotificationManager);
		}
		
	}
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-11-18下午4:08:10
	 *
	 * @param title
	 * @param content
	 * @param context
	 * @param mNotificationManager
	 */
    private static void showNotification (String title,String content,Context context,NotificationManager mNotificationManager) {
        Intent intent = new Intent(context, DownloadActivity.class);
        PendingIntent pending = PendingIntent.getActivity(context, 0,intent, PendingIntent.FLAG_ONE_SHOT);
        Notification notification = new Notification(R.drawable.icon_small, title,System.currentTimeMillis());
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        RemoteViews view = new RemoteViews(context.getPackageName(),R.layout.notification_download);
        view.setTextViewText(R.id.notification_download_main_tv,title);
        view.setTextViewText(R.id.notification_download_sub_tv, content);
//        view.setTextViewText(R.id.notification_download_time, Util.getDate(System.currentTimeMillis()/1000));
        notification.contentView = view;
        if (pending != null) {
            notification.contentIntent = pending;
        }
        mNotificationManager.notify(DownloadManager.DOWNLOAD_NOTIFICATION,notification);
    }
	
	public static String countGamePlayDur(long date) {//玩了多久
		String dateStr = "";
		int hh;
		int mm;
		int ss;
		Long time = Math.abs(System.currentTimeMillis() - (date));
		if (time <= 1 * 60 * 1000) {//如果小于1分钟就显示多少秒
			ss = (int) (time/(1000));
			dateStr = ss + " 秒";
		} else if (time <= 1 * 3600 * 1000) {//如果小于1个小时，就显示多少分钟
			mm = (int) (time/(60 * 1000));//多少分钟
			dateStr = mm + " 分钟";
		} else if (time <= 24 * 3600 * 1000) {//如果小于1天，就显示多少小时多少分钟
		    hh = (int) (time/(60* 60 * 1000));
		    time -= hh * (60* 60 * 1000);
		    mm = (int) (time/(60 * 1000));//多少分钟
		    dateStr = hh + " 小时" + mm + " 分钟";
		}
		return dateStr;
	}
	
	public static long getPlayMinius(long date) {//计算玩了多少秒	 
		return (Math.abs(System.currentTimeMillis() - (date * 1000)))/1000;
	}
	
	public static long getCoins(long playtimecount) {
		return playtimecount/(10);
	}

	//add com.sina.weibo
	public static void shareToChooseApps(Context mContext, boolean containWeibo) {
		Intent iShare = new Intent(Intent.ACTION_SEND);
		iShare.setType("text/plain");
		List<ResolveInfo> resInfo = mContext.getPackageManager()
				.queryIntentActivities(iShare, 0);
		if (!resInfo.isEmpty()) {
			List<Intent> targetedShareIntents = new ArrayList<Intent>();
			for (ResolveInfo info : resInfo) {
				Intent targeted = new Intent(Intent.ACTION_SEND);
				targeted.setType("text/plain");
				ActivityInfo activityInfo = info.activityInfo;
				if(activityInfo.packageName.contains("com.tencent.mm")
						|| activityInfo.name.contains("com.tencent.mm")) {
					//如果是微信，需要再生成一个target，一个分享给朋友圈，一个分享给好友
					//分享到朋友圈
					ComponentName toTimeLine = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareToTimeLineUI");
					targeted.setComponent(toTimeLine);
					targeted.putExtra(android.content.Intent.EXTRA_TEXT, mContext
							.getString(R.string.share_txt, PYVersion.IP.APK_URL));
					targetedShareIntents.add(targeted);
					
					//分享给朋友
					Intent targeted_toFriend = new Intent(Intent.ACTION_SEND);
					targeted.setType("text/plain");
					ComponentName toFriend = new ComponentName("com.tencent.mm",
                            "com.tencent.mm.ui.tools.ShareImgUI");
					targeted_toFriend.setComponent(toFriend);
					targeted_toFriend.putExtra(android.content.Intent.EXTRA_TEXT, mContext
							.getString(R.string.share_txt, PYVersion.IP.APK_URL));
					targetedShareIntents.add(targeted_toFriend);
				}
				if (activityInfo.packageName.contains("com.tencent.WBlog")
						|| activityInfo.packageName.contains("com.android.mms")
						|| activityInfo.name.contains("com.tencent.WBlog")
						|| activityInfo.name.contains("com.android.mms")) {
					targeted.setPackage(activityInfo.packageName);
					targeted.putExtra(android.content.Intent.EXTRA_TEXT, mContext
							.getString(R.string.share_txt, PYVersion.IP.APK_URL));
					targetedShareIntents.add(targeted);
				}
				if((activityInfo.packageName.contains("com.sina.weibo")
						|| activityInfo.name.contains("com.sina.weibo")) && containWeibo) {
					targeted.setPackage(activityInfo.packageName);
					targeted.putExtra(android.content.Intent.EXTRA_TEXT, mContext
							.getString(R.string.share_txt, PYVersion.IP.APK_URL));
					targetedShareIntents.add(targeted);
				}
			}
			Intent chooserIntent = Intent.createChooser(
					targetedShareIntents.remove(0),
					mContext.getString(R.string.select_share_app));
			if (chooserIntent == null) {
				return;
			}
			chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
			mContext.startActivity(chooserIntent);
		}
	}

	public static boolean isExStorageAbl(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			if(Environment.getExternalStorageDirectory().canWrite()){
              return true;
			}else {
			  return false;
			}
        }  
        else {  
            return false;  
        }  
	}

	public  static float getExtStorageAvailableSize(){
		String sPath = Environment.getExternalStorageDirectory().getPath();  
		StatFs sf = new StatFs(sPath);
		long blockSize = sf.getBlockSize();
		long freeBlocks = sf.getAvailableBlocks();
		return (float)((freeBlocks * blockSize)/1024f /1024f);
	}

}
