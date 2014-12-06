package com.cyou.mrd.pengyou.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
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
import com.cyou.mrd.pengyou.utils.ActivityManager;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;

/**
 * 注册-手机号,更改手机账号密码
 * 
 * @author wangkang
 * 
 */
public class RegisterForTelePhoneActivity extends BaseActivity {
	private CYLog log = CYLog.getInstance();
	private Button mGetValidateBtn;
	private EditText mNumET;
	private MyHttpConnect mConn;
	String validateNum = "";
	private boolean isResetPassword=false;
	public static int RESULT_CODE = 10;
	public static int ISRESETPASSWORD = 20;
//	private TextView txtLogin;
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.register_telephone);
		ActivityManager.getInstance().addActivity(this);
		isResetPassword = getIntent().getBooleanExtra(Params.RESET_PASSWORD.RESET_PASSWORD, false);
		initView();
		mConn = MyHttpConnect.getInstance();
	}

	private void initView() {
		try {
			mNumET = (EditText) findViewById(R.id.edit_telephone);
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
			if(isResetPassword){
				mHeaderTV.setText(R.string.reset_password_title);
			}else{
				
				mHeaderTV.setText(R.string.regist_tele_title);
			}
			mGetValidateBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mGetValidateBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mGetValidateBtn.setText(R.string.btn_next);

			mGetValidateBtn.setOnClickListener(new ClickListener());
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				public void run()

				{
					InputMethodManager inputManager = (InputMethodManager) mNumET
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					inputManager.showSoftInput(mNumET, 0);
				}
			}, 998);
		} catch (Exception e) {
			log.e(e);
		}
	}

	private class ClickListener implements android.view.View.OnClickListener {

		public void onClick(View view) {
			try {
				switch (view.getId()) {
				case R.id.sub_header_bar_right_ibtn:
					String phoneNum = mNumET.getText().toString();
					if (!TextUtils.isEmpty(phoneNum)) {
						((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
								.hideSoftInputFromWindow(
										mNumET.getWindowToken(),
										InputMethodManager.HIDE_NOT_ALWAYS);
						if (Util.isPhoneNum(phoneNum)) {
							getValidate(phoneNum);
							// mGetValidateBtn.setClickable(false);
						} else {
							Toast.makeText(
									RegisterForTelePhoneActivity.this,
									getResources().getString(
											R.string.bind_error2),
									Toast.LENGTH_SHORT).show();
							mNumET.setText("");
						}
					} else {
						Toast.makeText(RegisterForTelePhoneActivity.this,
								getResources().getString(R.string.bind_error1),
								Toast.LENGTH_SHORT).show();
						mNumET.setText("");
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
	
/**
 * 重置密码，添加密码
 * @param phoneNum
 */
	private void getValidate(final String phoneNum) {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		showLoadDoingProgressDialog();
		RequestParams params = new RequestParams();
		params.put("phone", phoneNum);
		
		//判断是否为重置密码
		String url;
		if(isResetPassword){
			url = HttpContants.NET.PWDVF_CODE;
			params.put("udid", Util.getUDIDNum());
			params.put("platform", "1");
		}else{
			
			 url = HttpContants.NET.CHECK_PHONE;
		}
		mConn.post2Json(url, params, new JSONAsyncHttpResponseHandler() {
			@Override
			public void onSuccessForString(String content) {
				dismissProgressDialog();
				if (TextUtils.isEmpty(content)) {
					Toast.makeText(RegisterForTelePhoneActivity.this,
							getString(R.string.getvacode_error),
							Toast.LENGTH_SHORT).show();
					mGetValidateBtn.setClickable(true);
					return;
				}
				RootPojo rootPojo = JsonUtils.fromJson(content, RootPojo.class);
				//如果不是修改密码不执行
				if(!isResetPassword){
					if (null == rootPojo) {
						Toast.makeText(RegisterForTelePhoneActivity.this,
								getString(R.string.getvacode_error),
								Toast.LENGTH_SHORT).show();
						mGetValidateBtn.setClickable(true);
						return;
					}
					if (String.valueOf(Contants.ERROR_NO.ERROR_1).equals(
							rootPojo.getErrorNo())) {// 注册过
						Toast.makeText(RegisterForTelePhoneActivity.this,
								getString(R.string.has_regist_error),
								Toast.LENGTH_SHORT).show();
						mGetValidateBtn.setClickable(true);
						return;
					}
				}
				//当重置手机密码时候，手机号没有注册
				if(String.valueOf(Contants.ERROR_NO.ERROR_UNREGIST).equals(
						rootPojo.getErrorNo())&&isResetPassword) {// 未绑定手机
					Toast.makeText(RegisterForTelePhoneActivity.this,
							getString(R.string.game_gift_game_got_nobind),
							Toast.LENGTH_SHORT).show();
					mGetValidateBtn.setClickable(true);
					return;
				}
				if (String.valueOf(Contants.ERROR_NO.ERROR_5).equals(
						rootPojo.getErrorNo())) {// 不能重复发验证码 应跳转至输入验证码页面
					showToastMessage(getString(R.string.hassend_vacode_error),
							1);
					Intent mIntent = new Intent();
					mIntent.putExtra("phone", phoneNum);
					mIntent.setClass(RegisterForTelePhoneActivity.this,
							RegisterForCodeActivity.class);
					//如果是在重置密码页过来的需要传值
					if(isResetPassword){
						mIntent.putExtra(Params.RESET_PASSWORD.RESET_PASSWORD, true);
					}
					startActivity(mIntent);
					return;
				}
				if ("1".equals(rootPojo.getSuccessful())) {// 发送成功
					Intent mIntent = new Intent();
					mIntent.putExtra("phone", phoneNum);
					mIntent.setClass(RegisterForTelePhoneActivity.this,
							RegisterForCodeActivity.class);
					//如果是在重置密码页过来的需要传值
					if(isResetPassword){
						mIntent.putExtra(Params.RESET_PASSWORD.RESET_PASSWORD, true);
					}
					startActivityForResult(mIntent, RESULT_CODE);
					Toast.makeText(RegisterForTelePhoneActivity.this,
							getString(R.string.hassend_vacode),
							Toast.LENGTH_SHORT).show();
					return;
				} else {
					Toast.makeText(RegisterForTelePhoneActivity.this,
							getString(R.string.getvacode_error),
							Toast.LENGTH_SHORT).show();
				}
				super.onSuccess(content);
			}
			@Override
			protected void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(
						RegisterForTelePhoneActivity.this);
				dialog.create().show();
				super.onLoginOut();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				dismissProgressDialog();
				Toast.makeText(RegisterForTelePhoneActivity.this,
						getString(R.string.getvacode_error), Toast.LENGTH_SHORT)
						.show();
				mGetValidateBtn.setClickable(true);
				super.onFailure(error, content);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_CODE) {
			Intent intent = new Intent(RegisterForTelePhoneActivity.this,
					GetContactsActivity.class);
			intent.putExtra("from", 2);
			startActivity(intent);
			setResult(LoginAndRegistActivity.LOGIN_RESULT_CODE);
			finish();
		}else if(resultCode == ISRESETPASSWORD){
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
