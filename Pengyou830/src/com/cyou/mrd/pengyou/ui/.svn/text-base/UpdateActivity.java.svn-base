package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.service.CheckUpdateService;

public class UpdateActivity extends Activity implements OnClickListener{

	private TextView mTitleTV;
	private TextView mContentTV;
	private Button mOKBtn;
	private Button mCancelBtn;
	
	private String mVersionName;
	private String mUrl;
	private String mContent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.update);
		initView();
		initData();
	}
	
	private void initView(){
		this.mTitleTV = (TextView)findViewById(R.id.update_title_tv);
		this.mContentTV = (TextView)findViewById(R.id.update_content_tv);
		this.mOKBtn = (Button)findViewById(R.id.update_ok_btn);
		this.mCancelBtn = (Button)findViewById(R.id.update_cancel_btn);
		this.mOKBtn.setOnClickListener(this);
		this.mCancelBtn.setOnClickListener(this);
	}
	
	private void initData(){
		Intent intent = getIntent();
		if(intent == null){
			return;
		}
		this.mVersionName = intent.getStringExtra(Params.PENGYOU_UPDATE.VERSION);
		this.mUrl = intent.getStringExtra(Params.PENGYOU_UPDATE.URL);
		this.mContent = intent.getStringExtra(Params.PENGYOU_UPDATE.CONTENT);
		this.mTitleTV.setText(getString(R.string.find_new_version, mVersionName));
		if(mContent != null){
			this.mContentTV.setText(Html.fromHtml(mContent));
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.update_ok_btn:
			Intent intent = new Intent(UpdateActivity.this,CheckUpdateService.class);
			intent.putExtra(Params.PENGYOU_UPDATE.TYPE, CheckUpdateService.UPDATE);
			startService(intent);
			UpdateActivity.this.finish();
			break;
		case R.id.update_cancel_btn:
			UpdateActivity.this.finish();
			break;
		default:
			break;
		}
	}
}
