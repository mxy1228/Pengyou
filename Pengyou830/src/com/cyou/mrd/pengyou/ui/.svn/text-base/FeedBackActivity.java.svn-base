package com.cyou.mrd.pengyou.ui;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 意见反馈页
 * @author 
 *
 */
public class FeedBackActivity extends BaseActivity {

	private CYLog log = CYLog.getInstance();

	private ImageButton mBackBtn;
	private Button mSendBtn;
	private EditText mET;
	private WaitingDialog mWaitingDialog;
	private TextView tv_title;

	private MyHttpConnect mConn;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.feed_back);
		initView();
	}

	private void initView() {
		try {
			View headerbar = findViewById(R.id.feed_back_title_bar);
			tv_title = (TextView) headerbar
					.findViewById(R.id.sub_header_bar_tv);
			tv_title.setText(R.string.feed);
			this.mBackBtn = (ImageButton) headerbar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			this.mBackBtn.setOnClickListener(new ClickListener());
			this.mSendBtn = (Button) headerbar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			this.mSendBtn.setVisibility(View.VISIBLE);
			this.mSendBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			this.mSendBtn.setText(R.string.send);
			this.mSendBtn.setEnabled(false);
			// this.mSendBtn.setVisibility(View.GONE);
			this.mWaitingDialog = new WaitingDialog(this);
			this.mET = (EditText) findViewById(R.id.feed_back_et);

			this.mET.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					if (!TextUtils.isEmpty(mET.getText().toString().trim())) {
						mSendBtn.setClickable(true);
						mSendBtn.setEnabled(true);
					} else {
						mSendBtn.setEnabled(false);
						mSendBtn.setClickable(false);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					mSendBtn.setClickable(false);
				}

				@Override
				public void afterTextChanged(Editable s) {

				}
			});
			this.mSendBtn.setOnClickListener(new ClickListener());
			SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			String feedback_hint = sp.getString(Params.SP_PARAMS.FEED_BACK_HINT, null);
			if(feedback_hint != null && !TextUtils.isEmpty(feedback_hint)){
				mET.setHint(feedback_hint);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	private class ClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				switch (v.getId()) {
				case R.id.sub_header_bar_left_ibtn:
					FeedBackActivity.this.finish();
					break;
				case R.id.sub_header_bar_right_ibtn:
					String content = mET.getText().toString();
					if (!TextUtils.isEmpty(content.trim())) {
						if (isNetWorks()) {
							mWaitingDialog.show();
							sendFeedBack(content);
						} else {
							Toast.makeText(FeedBackActivity.this,
									R.string.message_send_failed,
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(FeedBackActivity.this,
								R.string.empty_message, Toast.LENGTH_SHORT)
								.show();
						mET.setText("");
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

	private void sendFeedBack(String content) {
		RequestParams params = new RequestParams();
		params.put("text", content);
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		mConn.post(HttpContants.NET.FEED_BACK, params,
				new AsyncHttpResponseHandler() {
			
				@Override
				public void onLoginOut() {
					LoginOutDialog dialog = new LoginOutDialog(FeedBackActivity.this);
					dialog.create().show();
				}
			
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("feed back result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (!TextUtils.isEmpty(obj
										.getString(Params.HttpParams.SUCCESSFUL))
										&& "1".equals(obj
												.getString(Params.HttpParams.SUCCESSFUL))) {
									Toast.makeText(FeedBackActivity.this,
											R.string.message_send_success,
											Toast.LENGTH_SHORT).show();
									mHandler.sendMessage(mHandler
											.obtainMessage());
									Intent intent = new Intent(
											FeedBackActivity.this,
											FeedBackSubActivity.class);
									startActivity(intent);
									FeedBackActivity.this.finish();
									return;
								}else{
									Toast.makeText(FeedBackActivity.this,
											R.string.feed_back_error,
											Toast.LENGTH_SHORT).show();
								}
							}
							mHandler.sendMessage(mHandler.obtainMessage());
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Toast.makeText(FeedBackActivity.this,
								R.string.networks_available,
								Toast.LENGTH_SHORT).show();
					}
				});
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				if (mWaitingDialog.isShowing()) {
					mWaitingDialog.dismiss();
				}
			} catch (Exception e) {
				log.e(e);
			}
		}

	};

	// 判断是否联网
	private boolean isNetWorks() {
		ConnectivityManager connectivityManager = (ConnectivityManager) FeedBackActivity.this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		if (activeNetInfo != null) {
			return activeNetInfo.isAvailable();
		}
		return false;
	}

}
