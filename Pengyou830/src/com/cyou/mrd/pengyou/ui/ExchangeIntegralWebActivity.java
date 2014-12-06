package com.cyou.mrd.pengyou.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;


public class ExchangeIntegralWebActivity extends CYBaseActivity {
	WebView mWebView;
	Handler handler;
	TextView mHeaderTV ;
	private String urlSbuffer;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exchange_integral_web);
		initView();
		initData();
	}
	
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ImageButton mBackBtn = (ImageButton)findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(mWebView != null && mWebView.canGoBack()) {
					mWebView.goBack();
				} else {
					finish();
				}
			}
		});
		ImageButton mShareBtn = (ImageButton)findViewById(R.id.sub_header_bar_share_btn);
		mShareBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Util.shareToChooseApps(ExchangeIntegralWebActivity.this, true);
			}
		});
		ImageButton mRefreshBtn = (ImageButton)findViewById(R.id.sub_header_bar_refresh_btn);
		mRefreshBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (Util.isNetWorkAvailable()) {
					showWaitingDialog();
					mWebView.reload();
				} else {
					retryUrl();
				}
			}
		});
		mHeaderTV= (TextView)findViewById(R.id.sub_header_bar_tv);
		mWebView = (WebView) findViewById(R.id.web_view);
		WebSettings webSetting = mWebView.getSettings();
		webSetting.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSetting.setAppCacheEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE);
		webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
		mWebView.setScrollBarStyle(0);
		mWebView.setBackgroundColor(0);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					handler.sendEmptyMessage(1);
				}
				super.onProgressChanged(view, progress);
			}
		});
		handler=new Handler(new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				if(msg.what == 3){
					if (Util.isNetWorkAvailable()) {
						loadurl(mWebView, urlSbuffer.toString());
					} else {
						retryUrl();
					}
					return false;
				}
				dismissWaitingDialog();
				String title=mWebView.getTitle();
				mHeaderTV.setText(title);
				return false;
			}
		});
	}
	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		urlSbuffer= HttpContants.NET.JIFEN_URL + "?uauth=" + UserInfoUtil.getUauth() + "&platform=" + Params.HttpParams.PLATFORM_VALUE + 
				"&udid=" + Util.getUDIDNum();
		if(Util.isNetWorkAvailable()){
		   showWaitingDialog();
		   loadurl(mWebView, urlSbuffer.toString());
		}
		else{
			retryUrl();
		}
	}

	private final ReConnectListener  listener = new ReConnectListener() {
		@Override
		public void onReconnect() {			
			handler.sendEmptyMessage(3);
		}
	};
	private void  retryUrl(){
		try {
			showNetErrorDialog(ExchangeIntegralWebActivity.this,listener);
					
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		 if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView != null && mWebView.canGoBack()) {
			 mWebView.goBack();
			 return true;
		 }
		return super.onKeyDown(keyCode, event);	
	}
}
