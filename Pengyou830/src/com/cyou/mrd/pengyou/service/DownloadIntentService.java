/**
 * tuozhonghua_zk
 * 2013-11-18
 * TODO
 */
package com.cyou.mrd.pengyou.service;

import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.utils.Util;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/**
 * @author tuozhonghua_zk
 *
 */
public class DownloadIntentService extends IntentService {

	/**
	 * 
	 */
	public DownloadIntentService() {
		super("DownloadIntentService");
	}
	@Override
	public IBinder onBind(Intent intent) {
		return super.onBind(intent);
	}
	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		Util.showDownloadNotification(new MyGameDao(this), DownloadDao.getInstance(this), this, 
				(NotificationManager)getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE));
	}

}
