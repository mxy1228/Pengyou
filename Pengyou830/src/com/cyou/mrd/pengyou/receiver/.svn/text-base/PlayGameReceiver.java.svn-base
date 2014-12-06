package com.cyou.mrd.pengyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.log.CYLog;

public class PlayGameReceiver extends BroadcastReceiver {
	private CYLog log = CYLog.getInstance();

	private MyGameDao mMyGameDao;
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		if (intent != null) {
			try {
				mMyGameDao = new MyGameDao(context);
				final String packageName = intent.getStringExtra(Params.GAME_INFO.GAME_IDENTIFY);
				final long timestamp = intent.getLongExtra(Params.GAME_INFO.GAME_TIMESTAMP, 0);
				log.i("play packageName = " + packageName);
				mMyGameDao.update(packageName, timestamp);
				context.sendBroadcast(new Intent(
						Contants.ACTION.REORDER_MY_GAME_LIST));
			} catch (Exception e) {
				// TODO: handle exception
				log.e(e);
			}
		}
	}
}
