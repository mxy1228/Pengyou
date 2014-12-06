package com.cyou.mrd.pengyou.ui;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ValidateTelActivity extends CYBaseActivity implements OnClickListener{

	public static final int SUCCESS = 0;
	
	private EditText mTelET;
	private EditText mPasET;
	private TextView mTitleTV;
	private Button mBtn;
	private ImageButton mBackIBtn;
	private WaitingDialog mWaitingDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.validate_telephone);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void initView() {
		this.mTelET = (EditText)findViewById(R.id.validate_telephone);
		this.mPasET = (EditText)findViewById(R.id.validate_password);
		this.mBtn = (Button)findViewById(R.id.validate_btn);
		this.mWaitingDialog = new WaitingDialog(this);
		this.mBackIBtn = (ImageButton)findViewById(R.id.sub_header_bar_left_ibtn);
		this.mTitleTV = (TextView)findViewById(R.id.sub_header_bar_tv);
		this.mTitleTV.setText(R.string.validate);
	}

	@Override
	protected void initEvent() {
		this.mBtn.setOnClickListener(this);
		this.mBackIBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.validate_btn:
			validate();
			break;
		case R.id.sub_header_bar_left_ibtn:
			ValidateTelActivity.this.finish();
			break;
		default:
			break;
		}
	}

	/**
	 * 向服务器请求验证
	 * xumengyang
	 */
	private void validate(){
		if (TextUtils.isEmpty(mTelET.getText().toString())) {
			Toast.makeText(ValidateTelActivity.this,
					getResources().getString(R.string.bind_error1),
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (!Util.isPhoneNum(mTelET.getText().toString())) {
			Toast.makeText(ValidateTelActivity.this,
					getResources().getString(R.string.bind_error2),
					Toast.LENGTH_SHORT).show();
			mTelET.setText("");
			return;
		}
		if (TextUtils.isEmpty(mPasET.getText().toString())) {
			Toast.makeText(ValidateTelActivity.this,
					getResources().getString(R.string.bind_error_pwd),
					Toast.LENGTH_SHORT).show();
			return;
		}
		RequestParams params = new RequestParams();
		params.put("phone", mTelET.getText().toString());
		params.put("password", mPasET.getText().toString());
		MyHttpConnect.getInstance().post(HttpContants.NET.VALIDATE_PHONENUM, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("验证手机号码="+content);
				mWaitingDialog.dismiss();
				try {
					JSONObject obj = new JSONObject(content);
					if(obj.has("errorNo")){
						if(obj.getInt("errorNo") == SUCCESS){
							Intent intent = new Intent(ValidateTelActivity.this,RegisterForTelePhoneActivity.class);
							startActivity(intent);
							ValidateTelActivity.this.finish();
						}else{
							showShortToast(R.string.error_login);
						}
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
		});
	}
}
