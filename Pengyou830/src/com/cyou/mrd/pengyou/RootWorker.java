package com.cyou.mrd.pengyou;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.os.IBinder;

import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 登陆成功后RootWorker会接收到广播，然后将App列表发送给服务器
 * 
 * @author xumengyang
 * 
 */
public class RootWorker extends Service {

	private static CYLog log = CYLog.getInstance();
	private Context mContext;
	private MyHttpConnect mConn;

	@Override
	public void onCreate() {
		sendAppListRequest(this);
		super.onCreate();
	}

	/**
	 * 上传游戏列表
	 * 
	 * @param context
	 */
	public void sendAppListRequest(Context context) {
		try {
			if (mConn == null) {
				mConn = MyHttpConnect.getInstance();
			}
			RequestParams params = new RequestParams();
			params.put("games", getGameList());
			mConn.post(HttpContants.NET.SEND_APP_LIST, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							log.d(" RootWorker result is:" + content);
							super.onSuccess(content);
						}

						@Override
						public void onFailure(Throwable error) {
							log.e(error);
							super.onFailure(error);
						}
					});
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 获取我的好友列表
	 */
	private void getFriendList() {
		try {
			if (mConn == null) {
				mConn = MyHttpConnect.getInstance();
			}
			RequestParams params = new RequestParams();
			params.put("page", "1");
			params.put("count", "1000");
			mConn.post(HttpContants.NET.MYFRIEND_LIST, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							log.d(" RootWorker result is:" + content);
							super.onSuccess(content);
						}

						@Override
						public void onFailure(Throwable error) {

							log.e(error);
							super.onFailure(error);
						}
					});
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 获取用户手机中非系统的应用列表，并处理为json格式
	 * 
	 * @param context
	 * @return
	 */
	public static  String getGameList() {
		try {
			List<PackageInfo> list = CyouApplication.mAppContext.getPackageManager()
					.getInstalledPackages(0);
			StringBuffer sb = new StringBuffer();
			sb.append("[");
			for (int i = 0; i < list.size(); i++) {
				PackageInfo packageInfo = list.get(i);
				if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
					String packageName = packageInfo.packageName;
					sb.append("{\"plytime\":\""
							+ (packageInfo.lastUpdateTime / 1000) + "\","
							+ "\"version\":\"" + packageInfo.versionName
							+ "\"," + "\"identifier\":\"" + packageName
							+ "\"},");
				}

			}
			String param = sb.toString();
			if (param.length() > 1) {
				param = param.substring(0, param.length() - 1) + "]";
			} else {
				param = param.substring(0, param.length()) + "]";
			}
			return param;
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
