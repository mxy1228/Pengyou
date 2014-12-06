package com.cyou.mrd.pengyou.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ChangeFocusInfo;

public class NotifyBroadcastReceiver extends BroadcastReceiver {

	private static NotifyBroadcastReceiver notifyBroadcastReceiver;


	@Override
	public void onReceive(Context context, Intent intent) {
		ChangeFocusInfo changeFocusBean =ChangeFocusInfo.getInstance();
		changeFocusBean.follow = intent.getIntExtra(Params.FOLLOW, 3);
		changeFocusBean.uid = intent.getIntExtra(Params.FRIEND_INFO.UID,0);
		ChangeFocusInfo.changeList.add(changeFocusBean);
	}
}
