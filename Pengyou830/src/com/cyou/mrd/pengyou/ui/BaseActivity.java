package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.cyou.mrd.pengyou.widget.WaitingDialog;

public class BaseActivity extends Activity {
	private CYLog log = CYLog.getInstance();

	private Dialog mUpdateDialog;
	WaitingDialog waitingDialog;
	String lstLoadingMsg = "";
	String doLoadingMsg = "";
	private boolean mHadRegistForce=false;
	private ForceLoginOutReceiver mForceReceiver;
	protected void onCreate(Bundle bundle) {
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 竖屏
		waitingDialog = new WaitingDialog(this);
		lstLoadingMsg = getResources().getString(R.string.list_loading);
		doLoadingMsg = getResources().getString(R.string.do_loading);
		if (!NetUtil.isNetworkAvailable()) {
			showToastMessage(
					getResources().getString(
							R.string.download_error_network_error), Toast.LENGTH_SHORT);
		}
		super.onCreate(bundle);
	}

	@Override
	protected void onDestroy() {
		if (null != waitingDialog && waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
		try {
			if(mForceReceiver!=null&&mHadRegistForce){
				unregisterReceiver(mForceReceiver);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		waitingDialog = null;
		super.onDestroy();
	}
	@Override
	protected void onResume() {
		try {
			if(mForceReceiver == null){
				mForceReceiver = new ForceLoginOutReceiver();
			}
			if(!mHadRegistForce){
				registerReceiver(mForceReceiver, new IntentFilter(Contants.ACTION.FORCE_LOGIN_OUT));
				mHadRegistForce = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onResume();
	}

	protected void onStop() {
		super.onStop();

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 显示滚动的对话框
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            消息内容
	 */
	public void showProgressDialog(String message) {
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// waitingDialog.setTitle(title);//
		waitingDialog.setMessage(message);
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show(message);
	}

	/**
	 * 显示列表加载
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            消息内容
	 */
	public void showLoadListProgressDialog() {
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setMessage(lstLoadingMsg);
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
	}

	/**
	 * 显示操作加载
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            消息内容
	 */
	public void showLoadDoingProgressDialog() {
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setMessage(doLoadingMsg);
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
	}

	/**
	 * 隐藏滚动的对话框
	 */
	public void dismissProgressDialog() {
		if (waitingDialog != null && waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
	}

	/**
	 * 显示Toast提示
	 * 
	 * @param title
	 * @param message
	 */
	public void showToastMessage(String message, int duration) {
		ViewToast.showToast(getApplicationContext(), message, duration);
		return;
	}

	public void cancelToastMessage() {
		ViewToast.cancelToast(getApplicationContext());
		return;
	}
	/**
	 * 互斥登录监听器
	 * @author xumengyang
	 *
	 */
	private class ForceLoginOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			LoginOutDialog dialog = new LoginOutDialog(BaseActivity.this);
			dialog.create().show();
		}
		
	}
}
