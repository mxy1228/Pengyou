package com.cyou.mrd.pengyou.ui;

import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;
/**
 * 重置密码页，注册绑定手机密码页
 * @author 
 *
 */
public class RegisterForCodeActivity extends Activity {
	private CYLog log = CYLog.getInstance();
	private TextView txtReSend;
	private Button mOKBtn;
	private EditText mValidateET;
	private MyHttpConnect mConn;
	String validateNum = "";
	boolean isFirstSendCode = false;
	private int t = 60;
	private TextView txt_telephone;
	private EditText editPassword;
	private boolean isResetPassword;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.register_code);
		validateNum = getIntent().getStringExtra("phone");
		isResetPassword = getIntent().getBooleanExtra(Params.RESET_PASSWORD.RESET_PASSWORD, false);
		initView();
		mConn = MyHttpConnect.getInstance();
	}

	private void initView() {
		try {
			mValidateET = (EditText) findViewById(R.id.edit_telephone);
			txtReSend = (TextView) findViewById(R.id.txt_resend);
			txtReSend.setOnClickListener(new ClickListener());
			View headerBar = findViewById(R.id.edit_headerbar);
			ImageButton mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mOKBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOKBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			
			if(isResetPassword){
				mHeaderTV.setText(R.string.reset_password_title);
				mOKBtn.setText(R.string.btn_next_finish);
			}else{
				
				mHeaderTV.setText(R.string.regist_tele_title);
				mOKBtn.setText(R.string.btn_next);
			}
			
			

			mOKBtn.setOnClickListener(new ClickListener());
			txt_telephone = (TextView) findViewById(R.id.txt_telephone);
			txt_telephone.setText(txt_telephone.getText().toString()
					+ validateNum);
			txtReSend.setClickable(false);
			Timer timer = new Timer();
			timer.schedule(new ValidateTimer(timer), 0, 1000);
			editPassword = (EditText) findViewById(R.id.edit_password);
		} catch (Exception e) {
			log.e(e);
		}
	}
	private class ClickListener implements android.view.View.OnClickListener {

		public void onClick(View view) {
			try {
				switch (view.getId()) {
				case R.id.sub_header_bar_right_ibtn:
					registerCode();
					break;
				case R.id.txt_resend:
					if (!TextUtils.isEmpty(validateNum) || validateNum.trim().length()>4) {
						if (Util.isPhoneNum(validateNum)) {
							getValidate(validateNum);
							txtReSend.setClickable(false);
							Timer timer = new Timer();
							timer.schedule(new ValidateTimer(timer), 0, 1000);
						} else {
							Toast.makeText(
									RegisterForCodeActivity.this,
									getResources().getString(
											R.string.bind_error2),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(RegisterForCodeActivity.this,
								getResources().getString(R.string.bind_error1),
								Toast.LENGTH_SHORT).show();
					}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				log.e(e);
			}
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				if (msg.arg1 == 0) {
					txtReSend.setText(getString(R.string.re_get_validate));
					txtReSend.setClickable(true);
					((Timer) msg.obj).cancel();
					t = 60;
					isFirstSendCode = false;
				} else {
					txtReSend.setText(getString(R.string.please_input_validate,
							msg.arg1));
				}
			} catch (Exception e) {
				log.e(e);
			}
		}
	};

	class ValidateTimer extends TimerTask {

		private Timer timer;

		public ValidateTimer(Timer timer) {
			this.timer = timer;
		}

		@Override
		public void run() {
			try {
				--t;
				if (t >= 0) {
					Message msg = mHandler.obtainMessage();
					msg.arg1 = t;
					msg.obj = timer;
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				log.e(e);
			}
		}

	}

	private void registerCode() {
		if (!isValidateUser()) {
			return;
		}
		final String phoneNumText = mValidateET.getText().toString();
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		RequestParams params = new RequestParams();
		params.put("phone", validateNum);
		params.put("vfcode", phoneNumText);
		params.put("password", editPassword.getText().toString());
		String url;
		if(isResetPassword){
			url = HttpContants.NET.RESET_PASSWORD;
			params.put("udid", Util.getUDIDNum());
			params.put("platform", "1");
			params.put("newpwd", editPassword.getText().toString());
		}else{
			url = HttpContants.NET.BIND_PHONE;
			params.put("password", editPassword.getText().toString());
		}
		mConn.post2Json(url, params,
				new JSONAsyncHttpResponseHandler() {
					@Override
					public void onSuccessForString(String content) {
						
						if(isResetPassword){
							if (TextUtils.isEmpty(content)) {
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.tele_reset_password_error),
										Toast.LENGTH_SHORT).show();
								return;
							}
						}else{
							if (TextUtils.isEmpty(content)) {
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.tele_regist_error),
										Toast.LENGTH_SHORT).show();
								return;
							}
						}
						
						String errorNo = JsonUtils.getJsonValue(content,
								"errorNo");
						if (String.valueOf(Contants.ERROR_NO.ERROR_2).equals(
								errorNo)) {
							Toast.makeText(RegisterForCodeActivity.this,
									getString(R.string.tele_vacode_timeout),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (String.valueOf(Contants.ERROR_NO.ERROR_1).equals(
								errorNo)) {
							Toast.makeText(RegisterForCodeActivity.this,
									getString(R.string.has_regist_error),
									Toast.LENGTH_SHORT).show();
							return;
						}
						if (String.valueOf(Contants.ERROR_NO.ERROR_3).equals(
								errorNo)) {
							Toast.makeText(RegisterForCodeActivity.this,
									getString(R.string.vacode_error),
									Toast.LENGTH_SHORT).show();
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if(obj.has("data")){
								JSONObject data = obj.getJSONObject("data");
								if(data.has("utoken")){
									String utoken = data.getString("utoken");
									log.d("新utoken = "+utoken);
									UserInfoUtil.setUToken(utoken);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						String successful = JsonUtils.getJsonValue(content,
								"successful");
						if(isResetPassword){
							if (TextUtils.isEmpty(successful) || !"1".equals(successful)) {
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.reset_password_fail),
										Toast.LENGTH_SHORT).show();
								return;
							} else {// 更改成功
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.reset_password_success),
										Toast.LENGTH_SHORT).show();
								UserInfoUtil.setPhoneNumber(validateNum);
								setResult(RegisterForTelePhoneActivity.ISRESETPASSWORD);
								finish();
//								Intent mIntent  = new Intent();
//								mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//								mIntent.setClass(RegisterForCodeActivity.this, LoginActivity.class);
//								startActivity(mIntent);
//								finish();
							}
						}else{
							if (TextUtils.isEmpty(successful) || !"1".equals(successful)) {
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.tele_regist_error),
										Toast.LENGTH_SHORT).show();
								return;
							} else {// 注册成功
								Toast.makeText(RegisterForCodeActivity.this,
										getString(R.string.bind_tele_suc),
										Toast.LENGTH_SHORT).show();
								UserInfoUtil.setPhoneNumber(validateNum);
								setResult(RegisterForTelePhoneActivity.RESULT_CODE);
								finish();
							}
						}
						
						stopService(new Intent(RegisterForCodeActivity.this,CoreService.class));
						Intent intent = new Intent(RegisterForCodeActivity.this, CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.JOIN);
						intent.putExtra(Params.PUBLISH.UTOKEN, UserInfoUtil.getUToken());
						startService(intent);
						super.onSuccess(content);
					}
					
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								RegisterForCodeActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						Toast.makeText(RegisterForCodeActivity.this,
								getString(R.string.error_txt),
								Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(RegisterForCodeActivity.this,
								CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHANGE_BIND_ING);
						intent.putExtra(Params.PUBLISH.STATE, false);
						startService(intent);
						super.onFailure(error, content);
					}
					
					@Override
					public void onStart() {
						// 停止CoreService，防止推送服务器错乱
						Intent intent = new Intent(RegisterForCodeActivity.this,
								CoreService.class);
						intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHANGE_BIND_ING);
						intent.putExtra(Params.PUBLISH.STATE, true);
						startService(intent);
					}
				});
	}

	private boolean isValidateUser() {
		String validate = mValidateET.getText().toString();
		String password = editPassword.getText().toString();
		if (TextUtils.isEmpty(validate)) {
			Toast.makeText(RegisterForCodeActivity.this,
					getResources().getString(R.string.bind_error3),
					Toast.LENGTH_SHORT).show();
			return false;
		}
		if (TextUtils.isEmpty(password)) {
			Toast.makeText(RegisterForCodeActivity.this,
					getString(R.string.bind_error_pwd), Toast.LENGTH_SHORT)
					.show();
			return false;
		}
		if (password.length() < 6) {
			Toast.makeText(RegisterForCodeActivity.this,
					getString(R.string.password_length_error),
					Toast.LENGTH_SHORT).show();
			editPassword.setText("");
			return false;
		}
		return true;
	}

	private void getValidate(String phoneNum) {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		RequestParams params = new RequestParams();
		params.put("phone", phoneNum);
		String url = null;
		if(isResetPassword){
			 url = HttpContants.NET.PWDVF_CODE;
		}else{
			 url = HttpContants.NET.CHECK_PHONE;
			if (!isFirstSendCode) {
				url = HttpContants.NET.REGVF_CODE;
			}
		}
		
		
		mConn.post2Json(url, params, new JSONAsyncHttpResponseHandler() {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("get validate num is:" + content);
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(RegisterForCodeActivity.this,
							getString(R.string.getvacode_error),
							Toast.LENGTH_SHORT).show();
					return;
				}
				RootPojo rootPojo = JsonUtils.fromJson(content, RootPojo.class);
				if (null == rootPojo) {
					Toast.makeText(RegisterForCodeActivity.this,
							getString(R.string.getvacode_error),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if (String.valueOf(Contants.ERROR_NO.ERROR_1).equals(
						rootPojo.getErrorNo())&&!isResetPassword) {// 注册过，并不是重置密码
					Toast.makeText(RegisterForCodeActivity.this,
							getString(R.string.has_regist_error),
							Toast.LENGTH_SHORT).show();
					return;
				}
				if ("1".equals(rootPojo.getSuccessful())) {
					Toast.makeText(RegisterForCodeActivity.this,
							getString(R.string.hassend_vacode),
							Toast.LENGTH_SHORT).show();
					// validateNum = JsonUtils.getJsonValue(rootPojo.getData()
					// .toString(), "vfcode");
					log.d("validateNum is:" + validateNum);
					return;
				} else {
					Toast.makeText(RegisterForCodeActivity.this,
							getString(R.string.getvacode_error),
							Toast.LENGTH_SHORT).show();
				}
				super.onSuccess(statusCode, content);
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(RegisterForCodeActivity.this);
				dialog.create().show();
			}

			@Override
			public void onFailure(Throwable error, String content) {
				Toast.makeText(RegisterForCodeActivity.this,
						getString(R.string.getvacode_error), Toast.LENGTH_SHORT)
						.show();
				super.onFailure(error, content);
			}
		});

	}
}
