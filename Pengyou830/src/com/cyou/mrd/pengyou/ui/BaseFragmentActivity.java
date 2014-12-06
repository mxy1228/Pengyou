package com.cyou.mrd.pengyou.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;

public class BaseFragmentActivity extends FragmentActivity {
	private boolean mHadRegistForce=false;
	private ForceLoginOutReceiver mForceReceiver;
	@Override
	protected void onCreate(Bundle arg0) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
		super.onCreate(arg0);
	}
	@Override
	protected void onResume() {
		if(mForceReceiver == null){
			mForceReceiver = new ForceLoginOutReceiver();
		}
		if(!mHadRegistForce){
			registerReceiver(mForceReceiver, new IntentFilter(Contants.ACTION.FORCE_LOGIN_OUT));
			mHadRegistForce = true;
		}
		super.onResume();
	}
	/**
	 * 互斥登录监听器
	 * @author xumengyang
	 *
	 */
	private class ForceLoginOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			LoginOutDialog dialog = new LoginOutDialog(BaseFragmentActivity.this);
			dialog.create().show();
		}
		
	}
}
