package com.cyou.mrd.pengyou.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;

/**
 * 游戏库顶部Web跳转
 * 
 * @author wangkang
 * 
 */
public class GameStoreWebActivity extends CYBaseActivity {
	WebView webView;
	Handler handler;
	TextView mHeaderTV ;
	private String urlSbuffer;
	public static final int FILECHOOSER_RESULTCODE = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gamestore_web);
		initView();
		initData();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
	}

	

	



	public void loadurl(final WebView view, final String url) {
		view.loadUrl(url);
		new Thread() {
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}.start();
	}

	

	@Override
	protected void initView() {
		View headerBar = findViewById(R.id.edit_headerbar);
		ImageButton mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mHeaderTV= (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		webView = (WebView) findViewById(R.id.web_view);
		WebSettings webSetting = webView.getSettings();
		webSetting.setJavaScriptEnabled(true);// 设置支持javascript脚本
		webSetting.setAppCacheEnabled(true);
		webSetting.setDomStorageEnabled(true);
		webSetting.setDatabaseEnabled(true);
		webSetting.setJavaScriptCanOpenWindowsAutomatically(true);
		webSetting.setCacheMode(WebSettings.LOAD_DEFAULT);
		webSetting.setAllowFileAccess(true);// 设置允许访问文件数据
		webView.setScrollBarStyle(0);
		webView.setBackgroundColor(0);
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				return super.shouldOverrideUrlLoading(view, url);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}
		});

		webView.setWebChromeClient(new WebChromeClient() {
			public void onProgressChanged(WebView view, int progress) {
				if (progress == 100) {
					handler.sendEmptyMessage(1);
				}
				super.onProgressChanged(view, progress);
			}

//			/***************** android中使用WebView来打开本机的文件选择器 *************************/
//			// js上传文件的<input type="file" name="fileField" id="fileField" />事件捕获
//			// Android > 4.1.1 调用这个方法
//			public void openFileChooser(ValueCallback<Uri> uploadMsg,
//					String acceptType, String capture) {
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("image/*");
//				startActivityForResult(
//						Intent.createChooser(intent, "完成操作需要使用"),
//						GameStoreWebActivity.FILECHOOSER_RESULTCODE);
//
//			}
//
//			// 3.0 + 调用这个方法
//			public void openFileChooser(ValueCallback<Uri> uploadMsg,
//					String acceptType) {
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("image/*");
//				startActivityForResult(
//						Intent.createChooser(intent, "完成操作需要使用"),
//						GameStoreWebActivity.FILECHOOSER_RESULTCODE);
//			}
//
//			// Android < 3.0 调用这个方法
//			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//				intent.addCategory(Intent.CATEGORY_OPENABLE);
//				intent.setType("image/*");
//				startActivityForResult(
//						Intent.createChooser(intent, "完成操作需要使用"),
//						GameStoreWebActivity.FILECHOOSER_RESULTCODE);
//
//			}
		});
		handler=new Handler(new Callback() {
			
			@Override
			public boolean handleMessage(Message msg) {
				dismissWaitingDialog();
				String title=webView.getTitle();
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
		urlSbuffer = getIntent().getStringExtra(Params.GAMESTORE_HTML);
//		urlSbuffer=PYVersion.IP.APK_URL;
		showWaitingDialog();
		loadurl(webView, urlSbuffer.toString());
	}

}
