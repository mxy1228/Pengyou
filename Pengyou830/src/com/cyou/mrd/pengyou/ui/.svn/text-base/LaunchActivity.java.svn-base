package com.cyou.mrd.pengyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.RootWorker;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 应用程序启动页
 * 
 * @author wangkang
 * 
 */
public class LaunchActivity extends BaseActivity {
	private CYLog log = CYLog.getInstance();

	protected void onCreate(Bundle bundle) {
		log.d("onCreate");
		try {
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
			super.onCreate(bundle);
			setContentView(R.layout.launch);
			init();
		} catch (Exception e) {
			log.e(e);
			Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		}
	}

	private void init() {
		Intent intent = new Intent(this,LaunchService.class);
		startService(intent);
		//判断跳转
		jump2();
	}
	

	/**
	 * 根据token判断跳转
	 */
	private void jump2() {
		// 登陆流程: 用户首次进入应用时,跳转至引导页. 当点击引导页的进入按钮后 调用登陆接口
		// .跳转至主页面.用户填写手机的注册页面是在添加好友-通讯录添加方式时调用的
		new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent intent = new Intent(LaunchActivity.this,
						MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		}, 3000);
	}

	/**
	 * 判断用户是否登陆
	 * 
	 * @return
	 */
	public boolean checkUserHasLogin() {
		String token = UserInfoUtil.getUauth();
		if (!TextUtils.isEmpty(token)) {
			return true;
		}
		return false;
	}

	public void filterGame() {
		try {
			// showLoadListProgressDialog();
			RequestParams params = new RequestParams();
			params.put("games", RootWorker.getGameList());
			MyHttpConnect.getInstance().post(HttpContants.NET.SEND_APP_LIST,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							// dismissProgressDialog();
							log.d(" RootWorker result is:" + content);
							Intent intent = new Intent(LaunchActivity.this,
									MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
							overridePendingTransition(android.R.anim.fade_in,
									android.R.anim.fade_out);
							super.onSuccess(content);
						}

						@Override
						public void onFailure(Throwable error) {
							// dismissProgressDialog();
							log.e(error);
							Intent intent = new Intent(LaunchActivity.this,
									MainActivity.class);
							intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(intent);
							finish();
							overridePendingTransition(android.R.anim.fade_in,
									android.R.anim.fade_out);
							super.onFailure(error);
						}
					});
		} catch (Exception e) {
			dismissProgressDialog();
			log.e(e);
		}
	}

}
