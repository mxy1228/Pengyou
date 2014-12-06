/*
 * Created by MadClient on 2013-1-24.
 * Copyright (c) 2006-2013 Madhouse Inc. All Rights Reserved. 
 */
package cn.optimad.trackingcode.android;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.Log;
import android.webkit.WebView;

/**
 * @ClassName: OptiMadUtil
 * @Description: Utility class for OptiMad tracking code
 * @author MadClient
 * @date 2013-1-24 下午10:18:54
 */
class OptiMadUtil {

	/*
	 * Constant defined
	 */
	private static final String TRACKING_CODE_VERSION = "1.2.1";
	private static final String TRACKING_CODE_SERVER_URL = "http://tc.dsp.madserving.com/tc.m";
	private static final String TAG = "OptiMadTrackingCode";
	// RMS status
	protected static final String RMS_TRACKING_STATUS = "OptiMad_tracking_code_status";
	protected static final String RMS_ACTIVATE_STATUS = "OptiMad_activate_status";
	// Network type
	private static final int NETWORK_TYPE_NONE = -1;
	private static final int NETWORK_TYPE_2G = 0;
	private static final int NETWORK_TYPE_3G = 2;
	private static final int NETWORK_TYPE_WIFI = 3;
	// Log
	protected static final int LOG_DEBUG = 1;
	protected static final int LOG_INFO = 2;
	protected static final int LOG_WARNING = 3;
	protected static final int LOG_ERROR = 4;

	protected static boolean DEBUG = false;
	/*
	 * Constructor Methods
	 */
	private OptiMadUtil() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Print message to the console.
	 * 
	 * @param type
	 *            log type
	 * @param message
	 *            log message
	 * @return void
	 */
	protected static void LOG(int type, String message) {
		switch (type) {
		case LOG_DEBUG:
			Log.d(TAG, message);
			break;
		case LOG_INFO:
			Log.i(TAG, message);
			break;
		case LOG_WARNING:
			Log.w(TAG, message);
			break;
		case LOG_ERROR:
			Log.e(TAG, message);
			break;
		}
	}

	/**
	 * Check OptiMad tracking code required permission.
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return boolean Whether to set permissions
	 */
	protected static boolean checkPermission(Context context) {
		try {
			context.enforceCallingOrSelfPermission(
					android.Manifest.permission.READ_PHONE_STATE,
					"Tracking Code requires permission to android.Manifest.permission.READ_PHONE_STATE!");
			context.enforceCallingOrSelfPermission(
					android.Manifest.permission.ACCESS_NETWORK_STATE,
					"Tracking Code requires permission to android.Manifest.permission.ACCESS_NETWORK_STATE!");
			context.enforceCallingOrSelfPermission(
					android.Manifest.permission.ACCESS_WIFI_STATE,
					"Tracking Code requires permission to android.Manifest.permission.ACCESS_WIFI_STATE!");
			context.enforceCallingOrSelfPermission(
					android.Manifest.permission.INTERNET,
					"Tracking Code requires permission to android.Manifest.permission.INTERNET!");
			return true;
		} catch (Throwable s) {
			return false;
		}
	}

	/**
	 * Get tracking code transmission status.
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @param statusKey
	 *            key for statue
	 * @return int tracking code transmission status code
	 */
	protected static int getStatus(Context context, String statusKey) {
		SharedPreferences preferences = ((Activity) context)
				.getPreferences(Context.MODE_PRIVATE);
		return preferences.getInt(statusKey, 0);
	}

	/**
	 * Set tracking code transmission status.
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @param statusKey
	 *            key for status
	 * @param statusValue
	 *            tracking code transmission status code
	 * @return void
	 */
	protected static void setStatus(Context context, String statusKey,
			int statusValue) {
		SharedPreferences preferences = ((Activity) context)
				.getPreferences(Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(statusKey, statusValue);
		editor.commit();
	}

	/**
	 * Check network available
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return boolean network availability
	 */
	protected static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return false;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Get network type
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return int current network type
	 */
	protected static int getNetworkType(Context context) {
		try {
			ConnectivityManager connectiveityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo networkInfo = connectiveityManager
					.getActiveNetworkInfo();
			if (networkInfo != null && networkInfo.isConnected()) {
				switch (networkInfo.getType()) {
				case 0: // TYPE_MOBILE
				case 2: // TYPE_MOBILE_MMS
				case 3: // TYPE_MOBILE_SUPL
				case 4: // TYPE_MOBILE_DUN
				case 5: // TYPE_MOBILE_HIPRI
				case 6: // TYPE_WIMAX
					TelephonyManager telephonyManager = (TelephonyManager) context
							.getSystemService(Context.TELEPHONY_SERVICE);
					switch (telephonyManager.getNetworkType()) {
					case 0: // NETWORK_TYPE_UNKNOWN
					case 1: // NETWORK_TYPE_GPRS
					case 2: // NETWORK_TYPE_EDGE
						return NETWORK_TYPE_2G;
					case 3: // NETWORK_TYPE_UMTS
					case 4: // NETWORK_TYPE_CDMA
					case 5: // NETWORK_TYPE_EVDO_0
					case 6: // NETWORK_TYPE_EVDO_A
					case 7: // NETWORK_TYPE_1xRTT
					case 8: // NETWORK_TYPE_HSDPA
					case 9: // NETWORK_TYPE_HSUPA
					case 10: // NETWORK_TYPE_HSPA
					case 11: // NETWORK_TYPE_IDEN
					case 12: // NETWORK_TYPE_EVDO_B
					case 13: // NETWORK_TYPE_LTE
					case 14: // NETWORK_TYPE_EHRPD
					case 15: // NETWORK_TYPE_HSPAP
						return NETWORK_TYPE_3G;
					default: // Other 3G network
						return NETWORK_TYPE_3G;
					}
				case 1: // TYPE_WIFI
				case 9: // TYPE_ETHERNET
					return NETWORK_TYPE_WIFI;
				default: // Other network type
					return NETWORK_TYPE_WIFI;
				}
			}
		} catch (Exception e) {
			return NETWORK_TYPE_NONE;
		}
		return NETWORK_TYPE_NONE;
	}

	/**
	 * Obtain application version
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return string application version
	 */
	private static String getApplicationVersion(Context context) {
		try {
			PackageInfo pkgInfo = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0);
			return pkgInfo.versionName + "("
					+ String.valueOf(pkgInfo.versionCode) + ")";
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get language
	 * 
	 * @return String current language
	 */
	private static String getLanguage() {
		Locale loc = Locale.getDefault();
		return loc.getLanguage() + "_" + loc.getCountry();
	}

	/**
	 * Get device model
	 * 
	 * @return device brand and mobel
	 */
	private static String getDeviceModel() {
		return Build.BRAND + " " + Build.MODEL;
	}

	/**
	 * Get unique device ID
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return String device imei
	 */
	private static String getUDID(Context context) {
		try {
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String deviceId = tm.getDeviceId();
			if (deviceId == null || deviceId.length() == 0) {
				return Secure.getString(context.getContentResolver(),
						Secure.ANDROID_ID);
			}
			return deviceId;
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get 802.11 Wifi MAC address
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @return String MAC address
	 */
	private static String getWifiMACAddress(Context context) {
		try {
			WifiManager wifiManager = (WifiManager) context
					.getSystemService(Context.WIFI_SERVICE);
			String address = wifiManager.getConnectionInfo().getMacAddress();
			if (address != null) {
				return address.replaceAll(":", "");
			}
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	/**
	 * Get telephone MAC address
	 * 
	 * @return String MAC address
	 */
	private static String getTelephoneMACAddress() {
		return null;
	}

	/**
	 * Encoding for network transmission security
	 * 
	 * @param date
	 *            Need to secure send of data
	 * @return String Encoded content
	 */
	/**
	 * Encoding for network transmission security
	 * 
	 * @param date
	 *            Need to secure send of data
	 * @param isConfuse
	 *            If need to confuse
	 * @return String encoded date
	 */
	private static String encode(final String date, boolean isConfuse) {
		if (date == null || date.length() <= 0)
			return null;
		String encodedDate = Base64.encodeToString(date.getBytes(),
				Base64.NO_WRAP);
		if (isConfuse) {
//          char[] charArray = encodedDate.toCharArray();
//          for (int i = 0; i < charArray.length; i += 2) {
//              char ch = charArray[i];
//              charArray[i] = charArray[i + 1];
//              charArray[i + 1] = ch;
//          }
//          return String.valueOf(charArray);
            StringBuffer newStr = new StringBuffer();
            char[] arrStr = encodedDate.toCharArray();
            for (int i = 0, j = 1; i < arrStr.length - 1 && j <= arrStr.length; i += 2, j += 2) {
                String a = String.valueOf(arrStr[i]);
                String b = String.valueOf(arrStr[j]);
                newStr.append(b);
                newStr.append(a);
            }
            if (null != arrStr && arrStr.length % 2 != 0) {
                newStr.append(String.valueOf(arrStr[arrStr.length - 1]));
            }
            String keyStr = newStr.toString();
            return keyStr;
        }
		return encodedDate;
	}

	/**
	 * append tracking code request url parameters
	 * 
	 * @param urlParams
	 *            request url params
	 * @param key
	 *            parameters key
	 * @param value
	 *            parameters value
	 * @param firstParam
	 *            first parameters
	 * @return void
	 */
	private static void appendParams(StringBuffer urlParams, String key,
			String value, boolean firstParam) {
		if (value != null && value.length() > 0) {
			try {
				urlParams.append(firstParam ? "?" : "&")
						.append(URLEncoder.encode(key, "UTF-8")).append("=")
						.append(URLEncoder.encode(value, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				LOG(LOG_DEBUG, e.toString());
			}
		}
	}

	/**
	 * Build tracking code request url
	 * 
	 * @param context
	 *            a Context object used to access application assets
	 * @param webview
	 *            webview handle
	 * @param aId
	 *            dvertisers applications promote id
	 * @param sid 
	 * 				
	 * @return String Generate tracking request address
	 */
	protected static String buildRequestUrl(Context context, WebView webview,
			String aId, String sid) {
		StringBuffer requestUrl = new StringBuffer();
		requestUrl.append(TRACKING_CODE_SERVER_URL);
		appendParams(requestUrl, "aid", aId, true);

		appendParams(requestUrl, "db", (DEBUG?1:0)+"", false);
		
		appendParams(requestUrl, "uid", encode(getUDID(context), true), false);
		appendParams(requestUrl, "wma",encode(getWifiMACAddress(context), true), false);
		appendParams(requestUrl, "tma", encode(getTelephoneMACAddress(), true),
				false);
		appendParams(requestUrl, "did", encode(getDID(context), true),
                false);
		
		if(sid != null && sid.length() != 0) {
			appendParams(requestUrl, "sid", sid, false);
		}
		
		appendParams(requestUrl, "ua", webview.getSettings().getUserAgentString(), false);
		appendParams(requestUrl, "mod", getDeviceModel(), false);
		appendParams(requestUrl, "nt", String.valueOf(getNetworkType(context)),
				false);
		appendParams(requestUrl, "os", "0", false);
		appendParams(requestUrl, "osv", Build.VERSION.RELEASE, false);
		appendParams(requestUrl, "lng", getLanguage(), false);
		
		appendParams(requestUrl, "jb", getKeyJb(),
                false);
		
		appendParams(requestUrl, "apn", context.getPackageName(), false);
		appendParams(requestUrl, "av", getApplicationVersion(context), false);
		appendParams(requestUrl, "aas",
				String.valueOf(getStatus(context, RMS_ACTIVATE_STATUS)), false);
		appendParams(requestUrl, "pv", TRACKING_CODE_VERSION, false);
		LOG(LOG_DEBUG, requestUrl.toString());
		return requestUrl.toString();
	}
	
	// root 1  unroot 0
    private static String getKeyJb() {
        String jb = null;
        File binSu = new File("/system/bin/su");
        File xbinSu = new File("/system/xbin/su");
        if (binSu.exists() || xbinSu.exists()) {
            jb = String.valueOf(1);
        } else {
            jb = String.valueOf(0);
        }
        return jb;
    }
	
	private static String getDID(Context context) {
        String did = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
        if(did!=null) {
            if(did.length()!=0)
                return did;
        }
        return "000000000000000";
    }
}