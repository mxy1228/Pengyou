package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;

/**
 * 
 * @author xumengyang
 * 
 */
public abstract class CYBaseActivity extends FragmentActivity {

	protected int mScreenWidth;
	protected int mScreenHeight;
	protected float mDensity;
	protected LayoutInflater mInflater;
	protected InputMethodManager mImm;
	protected CYLog log;

	private WaitingDialog mWaitingDialog;
	protected AlertDialog mNetErrorDialog;
	private ForceLoginOutReceiver mForceReceiver;
	private boolean mHadRegistForce;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mWaitingDialog = new WaitingDialog(this);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		mScreenWidth = metric.widthPixels;
		mScreenHeight = metric.heightPixels;
		mDensity = metric.density;

		mInflater = LayoutInflater.from(this);
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		log = CYLog.getInstance();
		
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(mForceReceiver == null){
			mForceReceiver = new ForceLoginOutReceiver();
		}
		if(!mHadRegistForce){
			registerReceiver(mForceReceiver, new IntentFilter(Contants.ACTION.FORCE_LOGIN_OUT));
			mHadRegistForce = true;
		}
	}
	
	/***
	 * luochuang
	 * 解决窗体泄露
	 */
	@Override
	protected void onDestroy() {
		if (mNetErrorDialog != null) {
			if(mNetErrorDialog.isShowing())
			     mNetErrorDialog.dismiss();
			mNetErrorDialog = null;
		}
		try {
			if(mHadRegistForce && mForceReceiver != null){
				unregisterReceiver(mForceReceiver);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	protected abstract void initView();

	protected abstract void initEvent();

	protected abstract void initData();

	/**
	 * 显示等待对话框 xumengyang
	 */
	protected void showWaitingDialog() {
		if (mWaitingDialog != null && !mWaitingDialog.isShowing()) {
			mWaitingDialog.show();
		}
	}

	/**
	 * Dissmiss等待对话框 xumengyang
	 */
	protected void dismissWaitingDialog() {
		if (mWaitingDialog != null && mWaitingDialog.isShowing()) {
			mWaitingDialog.dismiss();
		}
	}

	protected void showShortToast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_SHORT).show();
	}

	protected void showShortToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	protected void showLongToast(int res) {
		Toast.makeText(this, res, Toast.LENGTH_LONG).show();
	}

	protected void showLongToast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_LONG).show();
	}

	/**
	 * 显示网络异常对话框
	 * 
	 * @param context
	 */
	protected void showNetErrorDialog(final Context context,
			final ReConnectListener listener) {
		if (context == null || listener == null) {
			return;
		}
		try {
			if(mNetErrorDialog == null){
				mNetErrorDialog = new AlertDialog.Builder(context)
				.setMessage(R.string.net_error)
				.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						((Activity) context).finish();
					}
				}).setNegativeButton(R.string.my_nearby_reset, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						listener.onReconnect();
					}
				}).create();
				mNetErrorDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							mNetErrorDialog.dismiss();
							((Activity) context).finish();
							return true;
						}
						return false;
					}
				});
				mNetErrorDialog.setCanceledOnTouchOutside(false);
			}
			mNetErrorDialog.show();
			
		} catch (Exception e) {
			log.e(e);
		}
	}

	public interface ReConnectListener {
		public void onReconnect();
	}

	@Override
	public void onBackPressed() {
		log.d("onBackPressed");
		super.onBackPressed();
	}
	
	/**
	 * 互斥登录监听器
	 * @author xumengyang
	 *
	 */
	private class ForceLoginOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			LoginOutDialog dialog = new LoginOutDialog(CYBaseActivity.this);
			dialog.create().show();
		}
		
	}
	
	
}
