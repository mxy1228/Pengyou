package com.cyou.mrd.pengyou.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;

public class NetUtil {
	/**
	 * 判断网络是否可用
	 * 
	 * @return
	 */
	// public static boolean isNetWorkAvailable() {
	// ConnectivityManager connectivitymanager = (ConnectivityManager)
	// CyouApplication.mAppContext
	// .getSystemService(Context.CONNECTIVITY_SERVICE);
	// if (connectivitymanager == null) {
	// return false;
	// }
	// NetworkInfo info = connectivitymanager.getActiveNetworkInfo();
	// if (info == null || !info.isAvailable()) {
	// return false;
	// }
	// return true;
	// }

	public static boolean isNetworkAvailable() {
		ConnectivityManager cm = (ConnectivityManager) CyouApplication.mAppContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (cm == null) {
			return false;
		} else {// 如果仅仅是用来判断网络连接　　　　　　 //则可以使用
				// cm.getActiveNetworkInfo().isAvailable();
			NetworkInfo[] info = cm.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					NetworkInfo temp = info[i];
					if (temp.getState() == NetworkInfo.State.CONNECTED
							&& temp.isAvailable() && temp.isConnected()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取网络状态
	 * 
	 * @return
	 */
	public int getNetWorkStat() {
		try {
			ConnectivityManager connectivitymanager = (ConnectivityManager) CyouApplication.mAppContext
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mobleInfo = connectivitymanager
					.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			NetworkInfo wifiInfo = connectivitymanager
					.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			NetworkInfo activeInfo = connectivitymanager.getActiveNetworkInfo();
			if (!mobleInfo.isConnected() && wifiInfo.isConnected()) {
				return Contants.NET_STATE.WIFI;
			} else if (mobleInfo.isConnected() && !wifiInfo.isConnected()) {
				return Contants.NET_STATE.MOBILE;
			} else if (activeInfo == null) {
				return Contants.NET_STATE.UNAVAILABLE;
			}
		} catch (Exception e) {
			CYLog.getInstance().e(e);
		}
		return Contants.NET_STATE.UNAVAILABLE;
	}

}
