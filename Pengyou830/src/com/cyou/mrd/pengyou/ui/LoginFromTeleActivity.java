package com.cyou.mrd.pengyou.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.RootWorker;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 通过手机号登陆
 * 
 * @author wangkang
 * 
 */
public class LoginFromTeleActivity extends BaseActivity {
	private EditText editTelephone;
	private EditText editPassword;
	private Button btnLogin;
	private Button btnRegist;
	private InputMethodManager mImm;
	private CYLog log = CYLog.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.login_telephone);
		super.onCreate(savedInstanceState);
		mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
	}

	private void initView() {
		editTelephone = (EditText) findViewById(R.id.edit_telephone);
		editPassword = (EditText) findViewById(R.id.edit_password);
		String lastTelephone = SharedPreferenceUtil.getLastTelephone();
		if (!TextUtils.isEmpty(lastTelephone)) {
			editTelephone.setText(lastTelephone);
		}
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnRegist = (Button) findViewById(R.id.btn_register);
		btnRegist.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				mIntent.setClass(LoginFromTeleActivity.this,
						RegisterForTelePhoneActivity.class);
				startActivity(mIntent);
				// finish();
			}
		});
		btnLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doLogin();
			}
		});
		View headerBar = findViewById(R.id.edit_headerbar);
		ImageButton mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				//返回时需要清空token，防止页面跳转
				UserInfoUtil.setUToken("");
				finish();
			}
		});
		TextView mHeaderTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		mHeaderTV.setText(R.string.login_tele_title);
	}
@Override
public boolean onKeyDown(int keyCode, KeyEvent event) {
	if (keyCode == KeyEvent.KEYCODE_BACK) { 
		//返回时需要清空token，防止页面跳转
		UserInfoUtil.setUToken("");
		finish();
    } 
	return super.onKeyDown(keyCode, event);
}
	/**
	 * 登陆
	 */
	private void doLogin() {
		hideSoftInput();
		if (!NetUtil.isNetworkAvailable()) {
			showToastMessage(
					getResources().getString(
							R.string.download_error_network_error),
					Toast.LENGTH_SHORT);
			return;
		}
		if (TextUtils.isEmpty(editTelephone.getText().toString())) {
			Toast.makeText(LoginFromTeleActivity.this,
					getResources().getString(R.string.bind_error1),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Util.isPhoneNum(editTelephone.getText().toString())) {
			Toast.makeText(LoginFromTeleActivity.this,
					getResources().getString(R.string.bind_error2),
					Toast.LENGTH_SHORT).show();
			editTelephone.setText("");
			return;
		}
		if (TextUtils.isEmpty(editPassword.getText().toString())) {
			Toast.makeText(LoginFromTeleActivity.this,
					getResources().getString(R.string.bind_error_pwd),
					Toast.LENGTH_SHORT).show();
			return;
		}
		showLoadDoingProgressDialog();
		// ...
		RequestParams params = new RequestParams();
		params.put("phone", editTelephone.getText().toString());
		params.put("password", editPassword.getText().toString());
		MyHttpConnect.getInstance().post(HttpContants.NET.USER_LOGIN_TELE,
				params, new JsonHttpResponseHandler() {

					public void onFailure(Throwable error, String content) {
						// if(content==null||TextUtils.isEmpty(content)){
						dismissProgressDialog();
						Toast.makeText(LoginFromTeleActivity.this,
								R.string.fail_login, Toast.LENGTH_SHORT).show();
						// }
					}

					@Override
					public void onFailure(Throwable e, JSONArray errorResponse) {
						super.onFailure(e, errorResponse);
					}
					
					@Override
					public void onStart() {
						super.onStart();
						// 停止CoreService，防止推送服务器错乱
						Intent intent = new Intent(LoginFromTeleActivity.this,
								CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHANGE_BIND_ING);
						intent.putExtra(Params.PUBLISH.STATE, true);
						startService(intent);
					}

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						dismissProgressDialog();
						Intent intent = new Intent(LoginFromTeleActivity.this,
								CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHANGE_BIND_ING);
						intent.putExtra(Params.PUBLISH.STATE, false);
						intent.putExtra(Params.PUBLISH.LOGIN, false);
						startService(intent);
						super.onFailure(e, errorResponse);
					}

					@Override
					public void onSuccess(JSONObject response) {
						if (null == response) {
							Toast.makeText(LoginFromTeleActivity.this,
									R.string.error_login, Toast.LENGTH_SHORT)
									.show();
							return;
						}
						try {
							if (!response.has("data")) {
								Util.loginOut(false,LoginFromTeleActivity.this);
								RootPojo rootPojo = JsonUtils.fromJson(
										response.toString(), RootPojo.class);
								if (rootPojo == null) {
									Toast.makeText(LoginFromTeleActivity.this,
											R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									dismissProgressDialog();
									return;
								}
								if (String.valueOf(
										Contants.ERROR_NO.ERROR_UNREGIST)
										.equals(rootPojo.getErrorNo())) {// 注册过
									Toast.makeText(LoginFromTeleActivity.this,
											getString(R.string.tele_regist_ok),
											Toast.LENGTH_SHORT).show();
									dismissProgressDialog();
									return;
								}
								if (String.valueOf(
										Contants.ERROR_NO.ERROR_USERNAME)
										.equals(rootPojo.getErrorNo())) {// 注册过
									Toast.makeText(LoginFromTeleActivity.this,
											R.string.error_login,
											Toast.LENGTH_SHORT).show();
									dismissProgressDialog();
									return;
								}
							}
							String data = response.getString("data");
							if (TextUtils.isEmpty(data)) {
								Toast.makeText(LoginFromTeleActivity.this,
										R.string.error_txt, Toast.LENGTH_SHORT)
										.show();
								dismissProgressDialog();
								return;
							}
							String userToken = JsonUtils.getJsonValue(data,
									"uauth");
							if (TextUtils.isEmpty(userToken)) {
								Toast.makeText(LoginFromTeleActivity.this,
										R.string.error_txt, Toast.LENGTH_SHORT)
										.show();
								return;
							}
							if (!TextUtils.isEmpty(UserInfoUtil.getUauth())) {
								if (!userToken.equals(UserInfoUtil.getUauth())) {
									SharedPreferenceUtil.initUserSign();
								}
							}
							String utoken = JsonUtils.getJsonValue(data, "utoken");
							if(!TextUtils.isEmpty(utoken)){
								log.d("新utoken = "+utoken);
								UserInfoUtil.setUToken(utoken);
							}
							UserInfoUtil.setUauth(userToken, false);
							SharedPreferenceUtil
									.saveLastTelephone(editTelephone.getText()
											.toString());
							setResult(LoginActivity.LOGIN_RESULT_CODE);
							filterGame();
						} catch (Exception e) {
							log.e(e);
						}
						stopService(new Intent(LoginFromTeleActivity.this,CoreService.class));
						Intent intent = new Intent(LoginFromTeleActivity.this, CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.JOIN);
						intent.putExtra(Params.PUBLISH.UTOKEN, UserInfoUtil.getUToken());
						intent.putExtra(Params.PUBLISH.LOGIN, true);
						startService(intent);
						super.onSuccess(response);
					}

				});
	}
	
	private void hideSoftInput(){
		View v = LoginFromTeleActivity.this.getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				mImm.hideSoftInputFromWindow(binder,
				InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	/**
	 * 上传游戏
	 */
	public void filterGame() {
		try {
			SharedPreferenceUtil.mustWaitFilterGameDown();
			RequestParams params = new RequestParams();
			params.put("games", RootWorker.getGameList());
			MyHttpConnect.getInstance().post(HttpContants.NET.SEND_APP_LIST,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							dismissProgressDialog();
							log.d("上传游戏="+content);
							LoginFromTeleActivity.this.finish();
						}

						@Override
						public void onFailure(Throwable error) {
							dismissProgressDialog();
						}
					});
		} catch (Exception e) {
			log.e(e);
		}
	}
}
