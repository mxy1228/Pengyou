package com.cyou.mrd.pengyou.ui;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.service.LaunchService;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginAndRegistActivity extends CYBaseActivity implements AnimationListener,OnClickListener{

	public static final int LOGIN_RESULT_CODE = 100;// 若登陆成功
	
	private ImageView mPic1;
	private ImageView mPic2;
	private ImageView mPic3;
	private ImageView mPic4;
	private ImageView mPic5;
	private TextView txtGuess;
	private LinearLayout mLoginLL;
	private LinearLayout mRegistLL;

	private Animation mAnim1;
	private Animation mAnim2;
	private Animation mAnim3;
	private Animation mAnim4;
	private Animation mAnim5;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_and_regist);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void initView() {
		mPic1 = (ImageView)findViewById(R.id.login_pic1);
		mPic2 = (ImageView)findViewById(R.id.login_pic2);
		mPic3 = (ImageView)findViewById(R.id.login_pic3);
		mPic4 = (ImageView)findViewById(R.id.login_pic4);
		mPic5 = (ImageView)findViewById(R.id.login_pic5);
		this.mLoginLL = (LinearLayout)findViewById(R.id.login_and_regist_login_ll);
		this.mRegistLL = (LinearLayout)findViewById(R.id.login_and_regist_regist_ll);
		startAnim();
	}

	@Override
	protected void initEvent() {
		this.mLoginLL.setOnClickListener(this);
		this.mRegistLL.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		
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

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.login_and_regist_login_ll:
			mIntent.setClass(LoginAndRegistActivity.this, LoginFromTeleActivity.class);
			startActivityForResult(mIntent, LOGIN_RESULT_CODE);
			break;
		case R.id.login_and_regist_regist_ll:
			Intent intent = new Intent(LoginAndRegistActivity.this,RegisterForTelePhoneActivity.class);
			startActivityForResult(intent,LOGIN_RESULT_CODE);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (LOGIN_RESULT_CODE == requestCode && resultCode == LOGIN_RESULT_CODE) {// 通过手机号登陆成功
			Intent intent = new Intent(LoginAndRegistActivity.this, LaunchService.class);
			startService(intent);
			// 判断跳转
			Intent mIntent = new Intent(LoginAndRegistActivity.this, MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(mIntent);
			finish();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
