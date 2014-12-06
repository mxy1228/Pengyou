package com.cyou.mrd.pengyou.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.utils.WeiboApi.WeiboApiListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;

/**
 * 分享到新浪微博
 * 
 * @author wangkang
 * 
 */
public class ShareAppToSinaActivity extends BaseActivity {
	private CYLog log = CYLog.getInstance();
	private EditText editShareApp;
	private MyHandler myHandler = new MyHandler();
    private  boolean flag=false;
	private final String APPKEY = "3443142555";
	private final String URL = "http://123.126.49.182/weibocallback.html";
	private final String TOKEN = "access_token";
	private final String EXPIRES_IN = "expires_in";
	private final String UID = "uid";
	public String SNS_SINA = "sina";
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.share_app_sina);
		initView();
	}

	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		try {
			View headerBar = findViewById(R.id.add_friend_headerbar);
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
			Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.share);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ifBindThenshareToSina();
				}
			});
			mHeaderTV.setText(R.string.share_app_sina);
			editShareApp = (EditText) findViewById(R.id.edit_shareapp);
			String defauleText = getString(R.string.share_txt, PYVersion.IP.APK_URL);
			editShareApp.setText(defauleText);
			editShareApp.setSelection(defauleText.length());
		} catch (Exception e) {
			log.e(e);
		}
	}

	//如果没有绑定就做一次绑定操作
	private void ifBindThenshareToSina() {
		try {
			boolean isBind = WeiboApi.getInstance().isBindSina();
			if (isBind) {
				shareToSina();
			} else {
				Weibo.getInstance(APPKEY, URL).startAuthDialog(
						ShareAppToSinaActivity.this, new AuthorizeListener());
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.e(e);
		}
	}
	
	/**
	 * 分享到新浪微博
	 */
	private void shareToSina() {
		if (!isValidateText()) {
			return;
		}
		String content = editShareApp.getText().toString().trim();
		WeiboApi.getInstance().shareWeibo(content, null, null,
				new WeiboApiListener() {

					@Override
					public void onFinish(String arg0) {
					}

					@Override
					public void onFaild(Exception exp) {
						Message msg = myHandler.obtainMessage();
						msg.what = Contants.WEIBO_SHARE_RESULT.FAILED;
						msg.obj = exp.getMessage();
						myHandler.sendMessage(msg);
					}

					@Override
					public void onSuccess(boolean success) {
						if (success) {
							myHandler.sendEmptyMessage(Contants.WEIBO_SHARE_RESULT.SUCCESS);
						}
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
	
	class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Contants.WEIBO_SHARE_RESULT.SUCCESS) {//如果新浪微博分享成功就去尝试兑换积分
				redeemScores();
			}  else if (msg.what == Contants.WEIBO_EXCHANGE_SCORE.RETURN) {//如果兑换积分流程结束就退出这个activity
				finish();
			}  else if (msg.what == Contants.WEIBO_SHARE_RESULT.FAILED) {
				String error_code = JsonUtils.getJsonValue((String) msg.obj,
						"error_code");
				if (!TextUtils.isEmpty(error_code)) {
					switch (Integer.parseInt(error_code)) {
					case Contants.WEIBO_ERROR_CODE.TOKEN_EXPIRED:
					case Contants.WEIBO_ERROR_CODE.EXPIRED_TOKEN:
						// 如果过期的话，重新登录
						showToastMessage(getString(R.string.token_expired), 0);
						Weibo.getInstance(APPKEY, URL).startAuthDialog(
								ShareAppToSinaActivity.this,
								new AuthorizeListener());
						break;
					case Contants.WEIBO_ERROR_CODE.INVALID_ACCESS_TOKEN:
						// 如果过期的话，重新登录
						showToastMessage(getString(R.string.invalid_access_token), 0);
						Weibo.getInstance(APPKEY, URL).startAuthDialog(
								ShareAppToSinaActivity.this,
								new AuthorizeListener());
						break;
					case Contants.WEIBO_ERROR_CODE.OUT_OF_LIMIT:
						// 发微博的次数超过限制
						showToastMessage(getString(R.string.out_of_limit), 0);
						break;
					case Contants.WEIBO_ERROR_CODE.REPEAT_SIMINAL_CONTENT:
						// 发相似的微博
						showToastMessage(
								getString(R.string.repeat_siminal_content), 0);
						break;
					case Contants.WEIBO_ERROR_CODE.REPEAT_SAME_CONTENT:
						// 发相同的微博
						showToastMessage(
								getString(R.string.repeat_same_content), 0);
						break;
					case Contants.WEIBO_ERROR_CODE.USER_DOES_NOT_EXISTS:
						// 用户不存在
						showToastMessage(
								getString(R.string.user_does_not_exists), 0);
						break;
					default:
						showToastMessage(
								getString(R.string.shareapp_sina_error), 0);
						break;
					}
				}
			} else {
				showToastMessage(
						getString(R.string.shareapp_sina_error), 0);
			}
			super.handleMessage(msg);
		}
	}
	private void snsUserLogin(String snsName, final String snsToken,
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
					public void onSuccess(String content) {
						dismissProgressDialog();
						log.d("sns user login is:" + content);
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
							shareToSina();
							return;
						}
						String errorNo = JsonUtils.getJsonValue(content,
								"errorNo");
						WeiboApi.getInstance().unBindSina(ShareAppToSinaActivity.this);
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

	private void saveToken(String token, String expires_in, String uid) {
		try {
			log.d("weibo->save info " + token + "____" + expires_in + "__"
					+ uid);
			SharedPreferenceUtil.saveSinaWeiboInfo(uid, token, expires_in);
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	private boolean isValidateText() {
		String nickName = editShareApp.getText().toString();
		if (TextUtils.isEmpty(nickName)) {
			showToastMessage(getString(R.string.shareapp_sina_nocontent), 0);
			return false;
		}
		return true;
	}
	
	private void redeemScores() {
		RequestParams params = new RequestParams();
		params.put("act", String.valueOf(Contants.SCORE_ACTION.EXCHANGE_SHARE_SINA_SCORE));

		MyHttpConnect.getInstance().post(HttpContants.NET.SCORE_ACTION,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						myHandler.sendEmptyMessage(Contants.WEIBO_EXCHANGE_SCORE.RETURN);
						super.onFailure(error);
					}

					@Override
					public void onSuccess(String content) {
						log.i("jifen content = " + content);
						if (TextUtils.isEmpty(content)) {
							myHandler
									.sendEmptyMessage(Contants.WEIBO_EXCHANGE_SCORE.RETURN);
							return;
						}
						String successful = JsonUtils.getJsonValue(content,
								"successful");
						if (!TextUtils.isEmpty(successful)
								&& "1".equals(successful)) {
							String isSuccessful = JsonUtils.getJsonValue(
									JsonUtils.getJsonValue(content, "data"),
									"isSuccessful");// 兑换成功
							if (isSuccessful.equals("true")) {
								int coins = Integer.parseInt(JsonUtils.getJsonValue(
										JsonUtils.getJsonValue(content, "data"),
										"changeValue"));
								int addOrSub = Integer.parseInt(JsonUtils
										.getJsonValue(JsonUtils.getJsonValue(
												content, "data"), "addOrSub"));
								showToastMessage(
										getApplicationContext().getResources().getString(R.string.share_success_and_get_score,
										UserInfoUtil.getCMSShareSinaScore()),0);// 提示：分享成功，您获得了XXX积分
								UserInfoUtil.sumAvailableScore(coins, addOrSub);
								myHandler
										.sendEmptyMessage(Contants.WEIBO_EXCHANGE_SCORE.RETURN);
								return;
							} else {
								// 如果因为服务器不稳定或者积分达到上限或者不符合积分兑换规则
								// 提示用户
								String data_errorno = JsonUtils.getJsonValue(
										JsonUtils.getJsonValue(content, "data"),
										"errorNo");
								if (!TextUtils.isEmpty(data_errorno)) {
									switch (Integer.valueOf(data_errorno)) {
									case Contants.EXCHANGE_ERROR_NO.NOT_CONFORM_RULES:
										showToastMessage(
												getString(R.string.share_success_but_get_score_limit),
												0);// 积分兑换已达到今日上限
										break;
									default:
										showToastMessage(
												getString(R.string.share_success_but_get_score_failed),
												0);// 网络不稳定
										break;
									}
								}
							}
						} else {
							// 如果successful为0，网络有问题
							String errorNo = JsonUtils.getJsonValue(content,
									"errorNo");
							if (!TextUtils.isEmpty(errorNo)) {
								showToastMessage(
										getString(R.string.download_error_network_error),
										0);
							}
						}
						myHandler
								.sendEmptyMessage(Contants.WEIBO_EXCHANGE_SCORE.RETURN);
						super.onSuccess(content);
					}
				});
	}
}
