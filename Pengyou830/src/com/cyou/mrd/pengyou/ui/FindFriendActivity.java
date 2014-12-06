package com.cyou.mrd.pengyou.ui;

import java.io.IOException;


import org.json.JSONObject;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.WeiboParameters;
import com.weibo.sdk.android.net.AsyncWeiboRunner;
import com.weibo.sdk.android.net.RequestListener;

/**
 * Edit by luochuang
 * 
 * @author luochuang_zk
 * 
 */
public class FindFriendActivity extends BaseActivity implements OnClickListener {

	private TextView tv_find_friend_title;
	private ImageButton ib_back;
	private final String APPKEY = "3443142555";
	private final String URL = "http://123.126.49.182/weibocallback.html";
	private LinearLayout ll_search_friend;
	private LinearLayout search_from_contact;
	private LinearLayout search_from_sina;
//	private LinearLayout ll_guess_your_know;
	private Intent intent;
	private Weibo mWeibo;

	private final String TOKEN = "access_token";
	private final String EXPIRES_IN = "expires_in";
	private final String UID = "uid";
	public String SNS_SINA = "sina";

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.find_friend);
		initview();
		ib_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FindFriendActivity.this.finish();
			}
		});
		ll_search_friend.setOnClickListener(this);
//		ll_guess_your_know.setOnClickListener(this);
		search_from_sina.setOnClickListener(this);
		search_from_contact.setOnClickListener(this);
	}

	private void initview() {
		View headerview = this.findViewById(R.id.find_friend_header);
		tv_find_friend_title = (TextView) headerview
				.findViewById(R.id.sub_header_bar_tv);
		tv_find_friend_title.setText(R.string.find_friend);
		ib_back = (ImageButton) findViewById(R.id.sub_header_bar_left_ibtn);
		ll_search_friend = (LinearLayout) this
				.findViewById(R.id.ll_search_friend);
		search_from_contact = (LinearLayout) this
				.findViewById(R.id.ll_search_from_contact);
		search_from_sina = (LinearLayout) this
				.findViewById(R.id.search_from_sina);
//		ll_guess_your_know = (LinearLayout) this
//				.findViewById(R.id.guess_your_know);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_search_friend:
			startAct(FindFriendActivity.this, SearchUserActivity.class);
			break;
//		case R.id.guess_your_know:
//			startAct(FindFriendActivity.this, GuessYouKnowActivity.class);
//			break;
		case R.id.search_from_sina:
			boolean isBind = WeiboApi.getInstance().isBindSina();
			Log.d("luochuang", "isbind:" + isBind);
			if (!isBind) {
				mWeibo = Weibo.getInstance(APPKEY, URL);
				mWeibo.authorize(FindFriendActivity.this, new AuthorizeListener());				
			} else {
				importSnsFriend(SNS_SINA);
			}
			break;
		case R.id.ll_search_from_contact:
//			startAct(FindFriendActivity.this, AddFriendContactsActivitys.class);
			intent = new Intent(FindFriendActivity.this, AddFriendContactsActivity.class);
			intent.putExtra("search_from", false);
			startActivity(intent);
			break;
		}
	}

	public void startAct(Context packageContext, Class<?> cls) {
		intent = new Intent(packageContext, cls);
		startActivity(intent);
	}

	private class AuthorizeListener implements WeiboAuthListener {

		@Override
		public void onCancel() {
		}

		@Override
		public void onComplete(Bundle arg0) {
			try {
				String token = arg0.getString(TOKEN);
				String expires_in = arg0.getString(EXPIRES_IN);
				String uid = arg0.getString(UID);
				snsUserLogin(SNS_SINA, token, uid, expires_in);
			} catch (Exception e) {
			}
		}

		@Override
		public void onError(WeiboDialogError arg0) {
			log.i("onError arg0 = " + arg0.toString());
		}

		@Override
		public void onWeiboException(WeiboException arg0) {
			log.i(" onWeiboException arg0 = " + arg0.toString());
		}
	}
	 private CYLog log = CYLog.getInstance();
	private void snsUserLogin(final String snsName, final String snsToken,
			final String snsUid, final String extime) {
		showLoadDoingProgressDialog();
		RequestParams params = new RequestParams();
		params.put("snsnm", snsName);
		params.put("snstoken", snsToken);
		params.put("snsusrid", snsUid);
		params.put("country", Util.getCountryID());
		MyHttpConnect.getInstance().post(HttpContants.NET.SNS_BIND, params,
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
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(FindFriendActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onSuccess(int code,String content) {
						super.onSuccess(code, content);
						dismissProgressDialog();
						if (TextUtils.isEmpty(content)) {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
							return;
						}
						String successful = JsonUtils.getJsonValue(content,
								"successful");
						if (!TextUtils.isEmpty(successful)
								&& "1".equals(successful)) {
							saveToken(snsToken, extime, snsUid);
							// 授权成功
							importSnsFriend(snsName);
							return;
						}
						String errorNo = JsonUtils.getJsonValue(content,
								"errorNo");						
						//做登出操作
						WeiboParameters params = new WeiboParameters();
						params.add("access_token", snsToken);
						AsyncWeiboRunner.request("https://api.weibo.com/oauth2/revokeoauth2", params, "POST", new RequestListener() {
							
							@Override
							public void onIOException(IOException arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onError(WeiboException arg0) {
								// TODO Auto-generated method stub
								
							}
							
							@Override
							public void onComplete(String arg0) {
								// TODO Auto-generated method stub
							}
						});
						
						weiboLogOut(snsToken);
						WeiboApi.getInstance().unBindSina(
								FindFriendActivity.this);
						
						if (!TextUtils.isEmpty(errorNo)
								&& String.valueOf(
										Contants.ERROR_NO.ERROR_SNS_BINDED)
										.equals(errorNo)) {
							showToastMessage(
									getResources().getString(
											R.string.bind_sns_bined_error), 1);

						} else {
							showToastMessage(
									getResources()
											.getString(
													R.string.download_error_network_error),
									1);
						}
						super.onSuccess(content);
					}
				});
	}
	private void importSnsFriend(String snsName) {
		RequestParams params = new RequestParams();
		params.put("snsnm", snsName);
		params.put("access_token", SharedPreferenceUtil.getSinaWeiboToken());
		params.put("snsusrid", SharedPreferenceUtil.getSinaWeiboUid());
		MyHttpConnect.getInstance().post(HttpContants.NET.SNS_IMPORT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						Intent mIntent = new Intent();
						mIntent.setClass(FindFriendActivity.this, SNSFriendLstActivity.class);
						startActivity(mIntent);
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
						Intent mIntent = new Intent();
						mIntent.setClass(FindFriendActivity.this, SNSFriendLstActivity.class);
						startActivity(mIntent);
						super.onFailure(error);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(FindFriendActivity.this);
						dialog.create().show();
					}
				});

	}
	private void saveToken(String token, String expires_in, String uid) {
		SharedPreferenceUtil.saveSinaWeiboInfo(uid, token, expires_in);
	}
	
	private void weiboLogOut(String snsToken) {
		//做登出操作
		WeiboParameters params = new WeiboParameters();
		params.add("access_token", snsToken);
		AsyncWeiboRunner.request("https://api.weibo.com/oauth2/revokeoauth2", params, "POST", new RequestListener() {
			
			@Override
			public void onIOException(IOException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onError(WeiboException arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(String arg0) {
				// TODO Auto-generated method stub
			}
		});
	}
}