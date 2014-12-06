package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.cyou.mrd.pengyou.widget.WaitingDialog;

public class BaseFragment extends Fragment {
	WaitingDialog waitingDialog;
	Activity mContext;
	String lstLoadingMsg = "";
	String doLoadingMsg = "";

	public BaseFragment() {
		super();
	}

	public BaseFragment(Activity activity) {
		mContext = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext=getActivity();
		if (null != mContext) {
			waitingDialog = new WaitingDialog(mContext);
			lstLoadingMsg = getResources().getString(R.string.list_loading);
			doLoadingMsg = getResources().getString(R.string.do_loading);
		}
		if(!NetUtil.isNetworkAvailable()) {
			showToastMessage(getResources().getString(R.string.download_error_network_error),Toast.LENGTH_SHORT);
		}
	}

	/**
	 * 显示滚动的对话框
	 * 
	 * @param title
	 *            标题
	 * @param message
	 *            消息内容
	 */
	public void showProgressDialog(Context context, String title, String message) {
		if (null == waitingDialog) {
			waitingDialog = new WaitingDialog(mContext);
			lstLoadingMsg = getResources().getString(R.string.list_loading);
			doLoadingMsg = getResources().getString(R.string.do_loading);
		}
		if (waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setTitle(title);
		waitingDialog.setMessage(message);
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
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
		if (null == waitingDialog) {
			waitingDialog = new WaitingDialog(mContext);
			lstLoadingMsg = getResources().getString(R.string.list_loading);
			doLoadingMsg = getResources().getString(R.string.do_loading);
		}
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		// waitingDialog.setTitle(title);//
		waitingDialog.setMessage(message);
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
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
		if (null == waitingDialog) {
			waitingDialog = new WaitingDialog(mContext);
			lstLoadingMsg = getResources().getString(R.string.list_loading);
			doLoadingMsg = getResources().getString(R.string.do_loading);
		}
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
		if (null == waitingDialog) {
			waitingDialog = new WaitingDialog(mContext);
			lstLoadingMsg = getResources().getString(R.string.list_loading);
			doLoadingMsg = getResources().getString(R.string.do_loading);
		}
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
		waitingDialog = null;
	}

	/**
	 * 显示Toast提示
	 * 
	 * @param title
	 * @param message
	 */
	public void showToastMessage(String message, int duration) {
		ViewToast.showToast(CyouApplication.mAppContext, message, duration);
		return;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}
}
