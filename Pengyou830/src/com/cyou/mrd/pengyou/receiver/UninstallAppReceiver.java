package com.cyou.mrd.pengyou.receiver;


import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;

/**
 * 接收手机上删除应用的广播
 * 
 * @author xumengyang
 * 
 */
public class UninstallAppReceiver extends BroadcastReceiver {

	private CYLog log = CYLog.getInstance();
	private MyGameDao myGameDao = new MyGameDao();
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			log.d("UninstallAppReceiver:onReceive");
			// //因intent.getDataString()的值为package:xxx.xxx，所以取子串substring(8)
			final String packageName = intent.getDataString().substring(8);
			
			if (packageName != null && !"".equals(packageName)) {
				String versionName = myGameDao.getLocalVersionName(packageName);
				if (versionName != null && !"".equals(versionName)) {
					log.i("delete download data versionName = " + versionName);
					DownloadDao downloadDao = DownloadDao.getInstance(context);
					downloadDao.delete(packageName, versionName);
					String filePath = SharedPreferenceUtil.getDownloadedInstallFile(context);
					if (!"".equals(filePath)) {
						File file = new File(filePath);
						try {
							if (file.exists()) {
								Thread.sleep(1000);
								Util.installApk(file, context);
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				}
			}
			SharedPreferenceUtil.setDownloadedInstallFile(context, "");
			context.getContentResolver().delete(Uri.parse(MyGameProvider.URI),packageName, null);
			log.d("卸载包名为:"+packageName+"的游戏!");
			Intent installIntent = new Intent(Contants.ACTION.UNINSTALL);
			installIntent.putExtra(DownloadParam.PACKAGE_NAME, packageName);
			context.sendBroadcast(installIntent);
		}
	}
}
