package com.cyou.mrd.pengyou.ui;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.ui.guider.GuiderActivity;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

/**
 * 登陆方式选择页面
 * 
 * @author wangkang
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener,AnimationListener {
	private CYLog log = CYLog.getInstance();
	private static final String NEW_USER = "1";
	private final String APPKEY = "3443142555";
	private final String URL = "http://123.126.49.182/weibocallback.html";
	private Weibo mWeibo;
	private final String TOKEN = "access_token";
	private final String EXPIRES_IN = "expires_in";
	private final String UID = "uid";
	private final String WEIBO_UID = "weibo_uid";
	public String SNS_SINA = "sina";
	public static final int LOGIN_RESULT_CODE = 100;// 若登陆成功
//	private boolean isExitIntent = false;
	
	private ImageView mPic1;
	private ImageView mPic2;
	private ImageView mPic3;
	private ImageView mPic4;
	private ImageView mPic5;
	private TextView txtGuess;
	private TextView txtResetPassword;//重置密码

	private Animation mAnim1;
	private Animation mAnim2;
	private Animation mAnim3;
	private Animation mAnim4;
	private Animation mAnim5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.d("onCreate");
		setContentView(R.layout.login);
//		isExitIntent = getIntent().getBooleanExtra(Params.FROM, false);
		initView();
		startAnim();
		
		
		if(checkUserHasLogin()|| !CyouApplication.isOpenApp){
			//为了用户覆盖安装，1.2中没有token，升级后需要联网获取token，但是需要判断用游客登录还是手机号账号登录，用唯一标示手机去判断，如果有好的方案，请整理
			//因为游客和手机是两个接口，需要用两个接口去访问，为空是游客登录，不空为手机账号
			if(("").equals(UserInfoUtil.getUToken())||UserInfoUtil.getUToken()==null){
				guestLogin();
			}else{
				
			}
			jump2();
		}
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

	/**
	 * 根据token判断跳转
	 */
	private void jump2() {
		// 登陆流程: 用户首次进入应用时,跳转至引导页. 当点击引导页的进入按钮后 调用登陆接口
		// .跳转至主页面.用户填写手机的注册页面是在添加好友-通讯录添加方式时调用的
		if (SharedPreferenceUtil.isFirstOpenApp()
				&& TextUtils.isEmpty(UserInfoUtil.getUauth())) {// 首次进入
			Intent intent = new Intent(LoginActivity.this, GuiderActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(android.R.anim.fade_in,
					android.R.anim.fade_out);
		} else if (checkUserHasLogin()&& !CyouApplication.isOpenApp) {// 直接跳转至首页
				if (NetUtil.isNetworkAvailable()) {
					Intent loginIntent = new Intent();
					loginIntent.setClass(LoginActivity.this,
							LaunchActivity.class);
					startActivity(loginIntent);
					finish();
				}
		}else{
			Intent intent = new Intent(LoginActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

	}
	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
			mHomeIntent.addCategory(Intent.CATEGORY_HOME);
			startActivity(mHomeIntent);
			return true;
		}
		return false;
	};
	
	private void initView() {
		findViewById(R.id.btn_login_telephone).setOnClickListener(this);
		findViewById(R.id.btn_login_changyou).setOnClickListener(this);
		findViewById(R.id.btn_login_qq).setOnClickListener(this);
//		findViewById(R.id.btn_login_weibo).setOnClickListener(this);
		txtGuess = (TextView) findViewById(R.id.txt_login_guest);
		txtGuess.setOnClickListener(this);
		txtResetPassword = (TextView) findViewById(R.id.txt_login_reset_password);
		txtResetPassword.setOnClickListener(this);
		txtGuess.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);// 下划线
		mPic1 = (ImageView)findViewById(R.id.login_pic1);
		mPic2 = (ImageView)findViewById(R.id.login_pic2);
		mPic3 = (ImageView)findViewById(R.id.login_pic3);
		mPic4 = (ImageView)findViewById(R.id.login_pic4);
		mPic5 = (ImageView)findViewById(R.id.login_pic5);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		String utoken = UserInfoUtil.getUToken();
//		txtGuess.setVisibility(UserInfoUtil.isBindPhone()&&utoken==null ? View.GONE : View.VISIBLE);
		txtGuess.setVisibility(View.GONE);
	}
	
	private void startAnim(){
		try {
			mAnim1 = AnimationUtils.loadAnimation(this, R.anim.login_anim1);
			mAnim1.setAnimationListener(this);
			mPic1.startAnimation(mAnim1);
			mAnim2 = AnimationUtils.loadAnimation(this, R.anim.login_anim2);
			mAnim2.setAnimationListener(this);
			mPic2.startAnimation(mAnim2);
			mAnim3 = AnimationUtils.loadAnimation(this, R.anim.login_anim3);
			mAnim3.setAnimationListener(this);
			mPic3.startAnimation(mAnim3);
			mAnim4 = AnimationUtils.loadAnimation(this, R.anim.login_anim4);
			mAnim4.setAnimationListener(this);
			mPic4.startAnimation(mAnim4);
			mAnim5 = AnimationUtils.loadAnimation(this, R.anim.login_anim5);
			mAnim5.setAnimationListener(this);
			mPic5.startAnimation(mAnim5);
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.btn_login_telephone:
			mIntent.setClass(LoginActivity.this, LoginFromTeleActivity.class);
			startActivityForResult(mIntent, LOGIN_RESULT_CODE);
			// finish();
			break;
		case R.id.btn_login_changyou:

			break;
		case R.id.btn_login_qq:

			break;
		case R.id.txt_login_reset_password:
			mIntent.setClass(LoginActivity.this, RegisterForTelePhoneActivity.class);
			mIntent.putExtra(Params.RESET_PASSWORD.RESET_PASSWORD, true);
			startActivity(mIntent);
			break;
//		case R.id.btn_login_weibo:
//			if (!NetUtil.isNetworkAvailable()) {
//				showToastMessage(
//						getResources().getString(
//								R.string.download_error_network_error), Toast.LENGTH_SHORT);
//				return;
//			}
//			mWeibo = Weibo.getInstance(APPKEY, URL);
//			mWeibo.authorize(LoginActivity.this, new AuthorizeListener());
//			break;
		case R.id.txt_login_guest:
			guestUserLogin();
			break;
		}
	}
	
	private void guestUserLogin() {
		if (!NetUtil.isNetworkAvailable() &&  TextUtils.isEmpty(UserInfoUtil.getUauth())) {
			showToastMessage(
					getResources().getString(
							R.string.download_error_network_error), Toast.LENGTH_SHORT);
			return;
		}
		if (!NetUtil.isNetworkAvailable() &&  !TextUtils.isEmpty(UserInfoUtil.getUauth())) {
			Intent mIntent = new Intent(LoginActivity.this,MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mIntent);
			return;
		}
		guestLogin();
	}

	private void guestLogin() {
		try {
			PackageManager pm = getPackageManager();
			RequestParams params = new RequestParams();
			params.put("type", CyouApplication.getChannel());
			params.put("version", pm.getPackageInfo(getPackageName(), 0).versionName);
			params.put("imsi", Util.getIMSI());
			MyHttpConnect.getInstance().post(HttpContants.NET.GUEST_LOGIN, params,
					new JsonHttpResponseHandler() {

						@Override
						public void onStart() {
							showLoadListProgressDialog();
						}
						
						@Override
						public void onFailure(Throwable e, JSONArray errorResponse) {
							dismissProgressDialog();
						}

						@Override
						public void onSuccess(JSONObject response) {
							dismissProgressDialog();
							log.d("guest login result :"+response);
							if (null == response) {
								Toast.makeText(LoginActivity.this, R.string.error_txt,
										Toast.LENGTH_SHORT).show();
								return;
							}
							try {
								if (!response.has("data")) {
									Toast.makeText(LoginActivity.this, R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String data = response.getString("data");
								if (TextUtils.isEmpty(data)) {
									Toast.makeText(LoginActivity.this, R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String userToken = JsonUtils.getJsonValue(data,
										"uauth");
								if (TextUtils.isEmpty(userToken)) {
									Toast.makeText(LoginActivity.this, R.string.error_txt,
											Toast.LENGTH_SHORT).show();
									return;
								}
								String utoken = JsonUtils.getJsonValue(data, "utoken");
								if(!TextUtils.isEmpty(utoken)){
									if(UserInfoUtil.setUToken(utoken)){
										SharedPreferenceUtil.setHasOpenApp();
										UserInfoUtil.setUauth(userToken, true);
										String isNewUser = JsonUtils.getJsonValue(data, "isnewusr");
										if(!TextUtils.isEmpty(isNewUser)){
											Intent intent = new Intent(LoginActivity.this,LaunchService.class);
											LoginActivity.this.startService(intent);
											if(isNewUser.equals(NEW_USER)){
												Intent iComplete = new Intent(LoginActivity.this,CompleteRegistActivity.class);
												startActivity(iComplete);
											}else{
												Intent mIntent = new Intent(LoginActivity.this,MainActivity.class);
												mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
												startActivity(mIntent);
											}
										}else{
											log.e("isnewusr is null");
										}
									}
								}
							} catch (Exception e) {
								log.e(e);
							}
							LoginActivity.this.finish();
						}

					});
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	/**
	 * 社交账号登陆
	 * 
	 * @param snsName
	 * @param snsToken
	 * @param snsUid
	 */
	private void snsUserLogin(String snsName, final String snsToken,
			final String snsUid, final String extime) {
		showLoadDoingProgressDialog();
		PackageManager pm = getPackageManager();
		RequestParams params = new RequestParams();
		params.put("snsnm", snsName);
		params.put("snstoken", snsToken);
		params.put("snsusrid", snsUid);
		params.put("country", Util.getCountryID());
		params.put("type", CyouApplication.getChannel());
		try {
			params.put("version", pm.getPackageInfo(getPackageName(), 0).versionName);
		} catch (Exception e) {
			log.e(e);
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.SNS_LOGIN, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						dismissProgressDialog();
						showToastMessage(
								getResources().getString(
										R.string.download_error_network_error),
								1);
						super.onFailure(error);
					}

					@Override
					public void onSuccess(String content) {
						log.d("sns user login is:" + content);
						if (TextUtils.isEmpty(content)) {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
							return;
						}
						String data = JsonUtils.getJsonValue(content, "data");
						if (TextUtils.isEmpty(data)) {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
							return;
						}
						String uauth = JsonUtils.getJsonValue(data, "uauth");
						if (TextUtils.isEmpty(uauth)) {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
							return;
						}
						if (!TextUtils.isEmpty(UserInfoUtil.getUauth())) {
							if (!uauth.equals(UserInfoUtil.getUauth())) {
								SharedPreferenceUtil.initUserSign();
							}
						}
						if(!TextUtils.isEmpty(uauth)){
							UserInfoUtil.setUauth(uauth, false);
						}
						String utoken = JsonUtils.getJsonValue(data, "utoken");
						if(!TextUtils.isEmpty(utoken)){
							UserInfoUtil.setUToken(utoken);
						}
						saveToken(snsToken, extime, snsUid);
						String isnewusr = JsonUtils.getJsonValue(data,
								"isnewusr");
						if ("0".equals(isnewusr)) {// 旧 用户
							Intent intent = new Intent(LoginActivity.this,
									LaunchService.class);
							startService(intent);
							// 判断跳转
							Intent mIntent = new Intent(LoginActivity.this,
									MainActivity.class);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mIntent);
							finish();
						} else {
							Intent intent = new Intent(LoginActivity.this,
									LaunchService.class);
							startService(intent);
							// 判断跳转
							Intent mIntent = new Intent(LoginActivity.this,
									MainActivity.class);
							mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
							startActivity(mIntent);
							finish();
						}
						dismissProgressDialog();
						super.onSuccess(content);
					}
				});
	}

	private class AuthorizeListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
			log.d("weibo->onCancel");
		}

		@Override
		public void onComplete(Bundle arg0) {
			try {
				String token = arg0.getString(TOKEN);
				String expires_in = arg0.getString(EXPIRES_IN);
				String uid = arg0.getString(UID);
				snsUserLogin(SNS_SINA, token, uid, expires_in);
			} catch (Exception e) {
				log.e(e);
			}

		}

		@Override
		public void onError(WeiboDialogError arg0) {
			log.d("weibo->onError");
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			log.d("weibo->onWeiboException");
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (LOGIN_RESULT_CODE == requestCode && resultCode == LOGIN_RESULT_CODE) {// 通过手机号登陆成功
			Intent intent = new Intent(LoginActivity.this, LaunchService.class);
			startService(intent);
			// 判断跳转
			Intent mIntent = new Intent(LoginActivity.this, MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mIntent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void saveToken(String token, String expires_in, String uid) {
		try {
			log.d("weibo->save info " + token + "____" + expires_in + "__"
					+ uid);
			SharedPreferenceUtil.clearSinaWeiboInfo();
			SharedPreferenceUtil.saveSinaWeiboInfo(uid, token, expires_in);
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		if(animation == mAnim1){
			mPic1.startAnimation(mAnim1);
		}else if(animation == mAnim2){
			mPic2.startAnimation(mAnim2);
		}else if(animation == mAnim3){
			mPic3.startAnimation(mAnim3);
		}else if(animation == mAnim4){
			mPic4.startAnimation(mAnim4);
		}else if(animation == mAnim5){
			mPic5.startAnimation(mAnim5);
		}
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}

}
