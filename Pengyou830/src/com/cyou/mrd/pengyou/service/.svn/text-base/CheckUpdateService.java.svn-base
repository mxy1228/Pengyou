package com.cyou.mrd.pengyou.service;

import java.io.File;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RemoteViews;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadListener;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.download.DownloadTask;
import com.cyou.mrd.pengyou.entity.PYUpdate;
import com.cyou.mrd.pengyou.entity.base.PYUpdateBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.ui.UpdateActivity;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CheckUpdateService extends Service {

	private CYLog log = CYLog.getInstance();
	private static final int UPDATE_NOTIFICATION_ID = -1;
	private static final int UPDATE_REMOTEVIEW = 1;
	public static final int CHECK = 1;
	public static final int CANCEL = 2;
	public static final int UPDATE = 3;
	public static String NEW_VERSION = "NEW_VERSION";
	
	private Notification UpdateNotification;
	private NotificationManager mNotificationManager;
	private PYUpdate mUpdateInfo;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if(intent == null){
			return;
		}
		int type = intent.getIntExtra(Params.PENGYOU_UPDATE.TYPE, 0);
		switch (type) {
		case CHECK:
			checkUpdate();
			break;
		case UPDATE:
			startUpdate();
			break;
		default:
			CheckUpdateService.this.stopSelf();
			break;
		}
	}
	/**
	 * 检查更新
	 */
	private void checkUpdate() {
		RequestParams params = new RequestParams();
		params.put("type", CyouApplication.getChannel());
		MyHttpConnect.getInstance().post(HttpContants.NET.CHECK_UPDATE, params,
			new AsyncHttpResponseHandler() {
				@Override
				public void onSuccess(int statusCode, String content) {
					if (TextUtils.isEmpty(content)) {
						return;
					}
					log.i("check pengyou update result = " + content);
					try {
						PYUpdateBase base = new ObjectMapper()
						.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.readValue(content, new TypeReference<PYUpdateBase>() {
								});
						if (base != null) {
							mUpdateInfo = base.getData();
							PackageManager pm = getPackageManager();
							PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
							if (mUpdateInfo.getApkversion() > pi.versionCode && !TextUtils.isEmpty(mUpdateInfo.getApkdesc())) {
								Intent intent = new Intent(CheckUpdateService.this,UpdateActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								intent.putExtra(Params.PENGYOU_UPDATE.VERSION, mUpdateInfo.getApkvername());
								intent.putExtra(Params.PENGYOU_UPDATE.URL, mUpdateInfo.getApkurl());
								intent.putExtra(Params.PENGYOU_UPDATE.CONTENT, mUpdateInfo.getApkdesc());
								startActivity(intent);
							}
						}
					} catch (Exception e) {
						log.e(e);
					}
				}
				
				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					CheckUpdateService.this.stopSelf();
				}
				});
	}

	
	private void startUpdate(){
		if(mUpdateInfo.getApkurl() != null && TextUtils.isEmpty(mUpdateInfo.getApkurl())){
			log.e("url is null");
			return;
		}
		CheckUpdateService.NEW_VERSION=mUpdateInfo.getApkvername();
		//首先删除之前的更新文件
		File pengyouApk = new File(SharedPreferenceUtil.getAPKPath(CheckUpdateService.this),getPackageName()+CheckUpdateService.NEW_VERSION+ ".apk");
		if (pengyouApk.exists()) {
			pengyouApk.delete();
		}
//		showNotification();
		DownloadItem item = new DownloadItem();
		item.setmURL(mUpdateInfo.getApkurl());
		item.setmPackageName(getPackageName());
		item.setVersionName(NEW_VERSION);
		item.setmName(getString(R.string.app_name));
		Intent intent = new Intent(this, DownloadService.class);
		intent.putExtra(DownloadParam.DOWNLOAD_ITEM,item);
		intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.ADD);
		startService(intent);
		//跳转到下载管理
		Intent mIntent = new Intent();
		mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
		mIntent.setClass(this, DownloadActivity.class);
		startActivity(mIntent);
//		DownloadDao.getInstance(mContext);.save(item);
//		DownloadTask task = new DownloadTask(item,new DownloadListener() {
//
//					@Override
//					public void updateProgress(String gameString,int percent, int speed) {
//						if (percent % 5 == 0) {
//							Message msg = mHandler
//									.obtainMessage();
//							msg.what = UPDATE_REMOTEVIEW;
//							msg.arg1 = percent;
//							mHandler.sendMessage(msg);
//						}
//					}
//
//					@Override
//					public void updateItemInDB(DownloadItem item) {
//						DownloadDao.getInstance(mContext);.updateItem(item);
//					}
//
//					@Override
//					public void preDownload(DownloadTask task) {
//						//TODO
//					}
//
//					@Override
//					public void finish(DownloadTask task) {
//						mUpdating = false;
//						task.getDownloadItem().setmState(DownloadParam.C_STATE.DONE);
//						mNotificationManager.cancel(UPDATE_NOTIFICATION_ID);
//						Intent intent = new Intent(Intent.ACTION_VIEW);
//						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//						intent.setDataAndType(Uri.fromFile(
//								new File(SharedPreferenceUtil.getAPKPath(CheckUpdateService.this),getPackageName()+NEW_VERSION+ ".apk")),
//								"application/vnd.android.package-archive");
//						startActivity(intent);
//						CheckUpdateService.this.stopSelf();
//					}
//
//					@Override
//					public void error(DownloadTask task,int error) {
//						// TODO
//
//					}
//
//					@Override
//					public void deleteTask(DownloadItem item) {
//						// TODO
//
//					}
//				}, CheckUpdateService.this);
//		task.execute(null);
	}
	
	private void showNotification() {
		UpdateNotification = new Notification(R.drawable.notification_pic,
				getString(R.string.begin_update), System.currentTimeMillis());
		UpdateNotification.flags = Notification.FLAG_ONGOING_EVENT;
		RemoteViews view = new RemoteViews(getPackageName(),
				R.layout.update_notification);
		UpdateNotification.contentView = view;
		Intent intent = new Intent(Intent.ACTION_VIEW);
		PendingIntent pending = PendingIntent.getActivity(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		UpdateNotification.contentIntent = pending;
		mNotificationManager.notify(UPDATE_NOTIFICATION_ID, UpdateNotification);
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_REMOTEVIEW:
				if (msg.arg1 < 100) {
					RemoteViews view = UpdateNotification.contentView;
					view.setProgressBar(R.id.update_notification_pb, 100,
							msg.arg1, false);
					mNotificationManager.notify(UPDATE_NOTIFICATION_ID,
							UpdateNotification);
				}
				break;
			default:
				break;
			}
		};
	};
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
}
