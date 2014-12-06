package com.cyou.mrd.pengyou.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.AddFriendAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;

public class AddFriendActivity extends FragmentActivity implements OnCheckedChangeListener,OnClickListener{

	private CYLog log = CYLog.getInstance();
	private static final int ANIM_END = 1;
	private boolean mHadRegistForce=false;
	private ViewPager mViewPager;
	private RadioGroup mRG;
	private RadioButton mKnownRBtn;
	private RadioButton mContactsRBtn;
	private RadioButton mImportRBtn;
	private RadioButton mQRCodeRBtn;
	private ImageView mAnimIV;
	private TextView mHeaderTV;
	private ImageButton mBackBtn;
	
	private AddFriendAdapter mAdapter;
	private int mCurIndex = 0;
	private int mAnimTVWidth;
	
	private ForceLoginOutReceiver mForceReceiver;
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.add_friend);
		initView();
	}
	
	private void initView(){
		this.mViewPager = (ViewPager)findViewById(R.id.add_friend_viewpager);
		this.mViewPager.setOnPageChangeListener(new PageChangeListsner());
		this.mViewPager.setOffscreenPageLimit(3);
		this.mRG = (RadioGroup)findViewById(R.id.add_friend_rg);
		this.mKnownRBtn = (RadioButton)findViewById(R.id.add_friend_guess_rbtn);
		this.mContactsRBtn = (RadioButton)findViewById(R.id.add_friend_contacts_rbtn);
		this.mImportRBtn = (RadioButton)findViewById(R.id.add_friend_import_rbtn);
		this.mQRCodeRBtn = (RadioButton)findViewById(R.id.add_friend_code_rbtn);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		this.mAnimIV = (ImageView)findViewById(R.id.add_friend_anim_iv);
		mAnimTVWidth = screenWidth / 3;
		LayoutParams params = new LayoutParams(mAnimTVWidth, LayoutParams.WRAP_CONTENT);
		this.mAnimIV.setLayoutParams(params);
		this.mAnimIV.setScaleType(ScaleType.FIT_CENTER);
		this.mAnimIV.setImageResource(R.drawable.add_friend_triangle);
		this.mRG.setOnCheckedChangeListener(this);
		View headerBar = findViewById(R.id.add_friend_headerbar);
		this.mBackBtn = (ImageButton)headerBar.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mHeaderTV = (TextView)headerBar.findViewById(R.id.sub_header_bar_tv);
		this.mHeaderTV.setText(R.string.add_friends);
		mBackBtn.setOnClickListener(this);
		resetRadioBtn(0);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(mAdapter == null){
			this.mAdapter = new AddFriendAdapter(this);
			this.mViewPager.setAdapter(mAdapter);
		}
	}
	protected void onResume() {
		if(mForceReceiver == null){
			mForceReceiver = new ForceLoginOutReceiver();
		}
		if(!mHadRegistForce){
			registerReceiver(mForceReceiver, new IntentFilter(Contants.ACTION.FORCE_LOGIN_OUT));
			mHadRegistForce = true;
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		try {
			if(mForceReceiver!=null&&mHadRegistForce){
				unregisterReceiver(mForceReceiver);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}
	/**
	 * 互斥登录监听器
	 * @author xumengyang
	 *
	 */
	private class ForceLoginOutReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			LoginOutDialog dialog = new LoginOutDialog(AddFriendActivity.this);
			dialog.create().show();
		}
		
	}
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.add_friend_guess_rbtn:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.add_friend_contacts_rbtn:
			mViewPager.setCurrentItem(1);
			break;
		case R.id.add_friend_import_rbtn:
			mViewPager.setCurrentItem(2);
			break;
		case R.id.add_friend_code_rbtn:
			mViewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
	
	private class PageChangeListsner implements OnPageChangeListener{

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onPageSelected(int arg0) {
			if(arg0 != 1){
				Fragment f = mAdapter.getItem(1);
				if(f instanceof ContactsFragment){
					((ContactsFragment)f).pause();
				}
			}
			resetRadioBtn(arg0);
		}
		
	}
	
	private void resetRadioBtn(final int index){
		Animation anim = new TranslateAnimation(mCurIndex * mAnimTVWidth
				, index * mAnimTVWidth
				, 0
				, 0);
		anim.setFillAfter(true);
		anim.setDuration(300);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
				Message msg = mHandler.obtainMessage();
				msg.what = ANIM_END;
				mHandler.sendMessage(msg);
			}
		});
		mAnimIV.startAnimation(anim);
		mCurIndex = index;
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case ANIM_END:
				for(int i=0;i<mRG.getChildCount();i++){
					if(i == mCurIndex){
						((RadioButton)mRG.getChildAt(i)).setTextColor(getResources().getColor(R.color.tab_text_select));
					}else{
						((RadioButton)mRG.getChildAt(i)).setTextColor(getResources().getColor(R.color.tab_text_normal));
					}
				}
				switch (mCurIndex) {
				case 0:
					mKnownRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.you_may_known_pic_select)
							, null, null);
					mContactsRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.contacts_pic_normal)
							, null, null);
					mImportRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.import_pic_normal)
							, null, null);
					mQRCodeRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.qr_code_pic_normal)
							, null, null);
					break;
				case 1:
					mKnownRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.you_may_known_pic_normal)
							, null, null);
					mContactsRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.contacts_pic_select)
							, null, null);
					mImportRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.import_pic_normal)
							, null, null);
					mQRCodeRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.qr_code_pic_normal)
							, null, null);
					break;
				case 2:
					mKnownRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.you_may_known_pic_normal)
							, null, null);
					mContactsRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.contacts_pic_normal)
							, null, null);
					mImportRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.import_pic_select)
							, null, null);
					mQRCodeRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.qr_code_pic_normal)
							, null, null);
					break;
				case 3:
					mKnownRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.you_may_known_pic_normal)
							, null, null);
					mContactsRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.contacts_pic_normal)
							, null, null);
					mImportRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.import_pic_normal)
							, null, null);
					mQRCodeRBtn.setCompoundDrawablesWithIntrinsicBounds(null
							, getResources().getDrawable(R.drawable.qr_code_pic_select)
							, null, null);
					break;
				default:
					break;
				}
				break;

			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			AddFriendActivity.this.finish();
			break;

		default:
			break;
		}
	}
}
