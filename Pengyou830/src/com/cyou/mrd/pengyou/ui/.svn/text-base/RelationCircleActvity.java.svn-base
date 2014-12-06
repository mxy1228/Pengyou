package com.cyou.mrd.pengyou.ui;

import java.lang.ref.SoftReference;
import java.util.HashMap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.widget.RelativeLayout.LayoutParams;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RelationCircleViewPagerAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 关系圈
 * 
 * @author wangkang
 * 
 */
public class RelationCircleActvity extends BaseFragmentActivity implements
		OnCheckedChangeListener, OnClickListener {

	private CYLog log = CYLog.getInstance();
	private static final int ANIM_END = 1;

	private ViewPager mViewPager;
	private RadioGroup mRG;
//	private RadioButton mDynamicRBtn;
//	private RadioButton mSNSRBtn;
	private TextView mAnimTV;
//	private TextView mHeaderTV;
	private ImageButton mSendDynamicIBtn;
	private int mAnimTVWidth = 0;
	private int mCurIndex = 0;
	private Button mRelationDotBtn;
	private SystemPullReceiver mSystemPullReceiver;
	private boolean mHadRegistSystemPullReceiver = false;
	private HashMap<String, SoftReference<Bitmap>> mImageCache;
	private boolean recycled = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.i("RelationCircleActvity onCreate");
		CyouApplication.setRelationCircleAct(this);
		if (savedInstanceState != null) {
			recycled = savedInstanceState.getBoolean("recycle");
		}
		setContentView(R.layout.relation_circle_layout);
		mImageCache = new HashMap<String, SoftReference<Bitmap>>();
		initView();
	}

//	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			CyouApplication.returnHomeIndex = Contants.RETURNHOME.RETURN_HOME_RELATIONS;
//			Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//			mHomeIntent.addCategory(Intent.CATEGORY_HOME);
//			startActivity(mHomeIntent);
//			MainActivity.mFirstResume = true;
//			return true;
//		}
//		return false;
//	};

	@Override
	protected void onRestart() {
		if (CyouApplication.returnHomeIndex != Contants.RETURNHOME.RETURN_HOME) {// 通过back切换home
			CyouApplication.returnHomeIndex = Contants.RETURNHOME.RETURN_HOME;
			if (CyouApplication.getMainActiv() != null) {
				CyouApplication.getMainActiv().initTabIndex();
			}
		}
		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
		super.onRestart();
	}

	private void initView() {
		this.mViewPager = (ViewPager) findViewById(R.id.relationcircle_viewpager);
		this.mViewPager.setOnPageChangeListener(new PageChangeListener());
//		viewPager = new RelationCircleViewPagerAdapter(
//				RelationCircleActvity.this);
		this.mViewPager.setAdapter( new RelationCircleViewPagerAdapter(RelationCircleActvity.this));
		this.mViewPager.setOffscreenPageLimit(2);

		// this.mViewPager.setAdapter(new RelationCircleViewPagerAdapter(
		// RelationCircleActvity.this));
		// this.mViewPager.setOffscreenPageLimit(0);
		this.mRG = (RadioGroup) findViewById(R.id.relationcircle_rg);
		this.mRG.setOnCheckedChangeListener(this);
//		this.mDynamicRBtn = (RadioButton) findViewById(R.id.relation_dynamic);
//		this.mSNSRBtn = (RadioButton) findViewById(R.id.relation_sns);
		this.mAnimTV = (TextView) findViewById(R.id.relationcircle_anim_tv);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mAnimTVWidth = (screenWidth - (2 * getResources()
				.getDimensionPixelSize(R.dimen.search_bar_height))) / 2;
		LayoutParams params = (LayoutParams) mAnimTV.getLayoutParams();
		log.i("RelationCircleActvity mAnimTVWidth:" + mAnimTVWidth);
		params.width = (screenWidth - (3 * getResources()
				.getDimensionPixelSize(R.dimen.search_bar_height))) / 2;
		;
		log.i("RelationCircleActvity params.width:" + params.width);
		this.mAnimTV.setLayoutParams(params);
		// this.mAnimTV.setWidth(mAnimTVWidth);
		this.mRelationDotBtn = (Button) findViewById(R.id.relation_circle_dot);
		// this.mSquareDotBtn =(Button)findViewById(R.id.relation_square_dot);

		this.mViewPager.setCurrentItem(0);
//		this.mHeaderTV = (TextView) findViewById(R.id.circle_header_tv);
		this.mSendDynamicIBtn = (ImageButton) findViewById(R.id.circle_header_right_btn);
		this.mSendDynamicIBtn.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		log.i("RelationCircleActvity onResume");
		// if(newDynamic()){
		// this.mRelationDotBtn.setVisibility(View.VISIBLE);
		// // this.mSquareDotBtn.setVisibility(View.VISIBLE);
		// }
		// else{
		// this.mRelationDotBtn.setVisibility(View.GONE);
		// // this.mSquareDotBtn.setVisibility(View.GONE);
		// }

		if (mSystemPullReceiver == null) {
			mSystemPullReceiver = new SystemPullReceiver();
		}
		if (mSystemPullReceiver != null && !mHadRegistSystemPullReceiver) {
			registerReceiver(mSystemPullReceiver, new IntentFilter(
					Contants.ACTION.UPDATE_RELATION_MSG_DOT_ACTION));
			mHadRegistSystemPullReceiver = true;
		}
		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
	}

	// private int newMsgCount(){
	// SystemCountMsgItem item = SystemCountMsgItem.get();
	// return item.getmUnreadCommentsCount();
	// }
	//
	// private boolean newDynamic(){
	// SystemCountMsgItem item = SystemCountMsgItem.get();
	// int newComment = item.getmUnreadCommentsCount();
	// int lastEachFocusDynamicID = item.getmEachFocusLastDynamicID();
	// log.i("newDynamic lastEachFocusDynamicID: " + lastEachFocusDynamicID +
	// " newComment : " + newComment);
	// if(newComment != 0 || lastEachFocusDynamicID != 0){
	// return true;
	// }
	// else {
	// return false;
	// }
	// }

	private class PageChangeListener implements OnPageChangeListener {

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
			log.i("-------------------------- onPageSelected arg0: " + arg0);
			resetRadioBtn(arg0);
			if (arg0 == 0) {
				// if(newDynamic()){
				// Intent intent = new Intent(
				// Contants.ACTION.REFRESH_RELATION_CIRCLE);
				// intent.putExtra("force", false);
				// sendBroadcast(intent);
				// }
				// SystemCountMsgItem item = SystemCountMsgItem.get();
				// int lastEachFocusDynamicID =
				// item.getmEachFocusLastDynamicID();
				// SystemCountMsgItem.saveLastDynmiacID(lastEachFocusDynamicID);
				if (!recycled) {
//					viewPager.getRelCircleFragment().forceRequest(false);
					 Intent intent = new Intent(
					 Contants.ACTION.REFRESH_RELATION_CIRCLE);
					 intent.putExtra("force", false);
					 sendBroadcast(intent);
				}

			} else {
//				 Intent intent = new Intent(
//				 Contants.ACTION.REFRESH_SQUARE_CIRCLE);
//				 sendBroadcast(intent);
				if (!recycled) {
//					viewPager.getRelSquareFragment().forceRequest();
					 Intent intent = new Intent(
							 Contants.ACTION.REFRESH_SQUARE_CIRCLE);
							 sendBroadcast(intent);
				}

			}
			// hideKeyboard();
		}

	}

	// private void hideKeyboard () {
	// try {
	// InputMethodManager imm =
	// (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
	// if (imm.isActive()) {
	// imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
	// InputMethodManager.HIDE_NOT_ALWAYS);
	// }
	// }
	// catch (Exception e) {
	// // TODO: handle exception
	// }
	// }
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		log.i("-------------------------- onCheckedChanged checkedId: "
				+ checkedId);
		switch (checkedId) {
		case R.id.relation_dynamic:
			log.i("-------------------------- onCheckedChanged relation_dynamic ");
			// resetRadioBtn(0);
			mViewPager.setCurrentItem(0);
			// if(newDynamic()){
			// Intent intent = new Intent(
			// Contants.ACTION.REFRESH_RELATION_CIRCLE);
			// sendBroadcast(intent);
			// }

			break;
		case R.id.relation_sns:
			log.i("-------------------------- onCheckedChanged relation_sns ");
			// resetRadioBtn(1);
			mViewPager.setCurrentItem(1);
			// Intent intent = new Intent(
			// Contants.ACTION.REFRESH_SQUARE_CIRCLE);
			// sendBroadcast(intent);

			break;
		}

	}

	private void resetRadioBtn(final int index) {
		Animation anim = new TranslateAnimation(mCurIndex * mAnimTVWidth, index * mAnimTVWidth, 0, 0);
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
		mAnimTV.startAnimation(anim);
		mCurIndex = index;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ANIM_END:
				for (int i = 0; i < mRG.getChildCount(); i++) {
					if (i == mCurIndex) {
						((RadioButton) mRG.getChildAt(i))
								.setTextColor(getResources().getColor(
										R.color.tab_text_select));
						((RadioButton) mRG.getChildAt(i)).performClick();
					} else {
						((RadioButton) mRG.getChildAt(i))
								.setTextColor(getResources().getColor(
										R.color.tab_text_normal));
					}
				}
				break;
			default:
				break;
			}
		};
	};

	private class SystemPullReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("SystemPullReceiver:onReceive");
			if (intent == null) {
				log.e("intent is null");
				return;
			}
			try {
				int comment = intent.getIntExtra("newComment", 0);
				int dynamic = intent.getIntExtra("newDynamic", 0);
				if (comment != 0 || dynamic != 0) {
					mRelationDotBtn.setVisibility(View.VISIBLE);
				} else {
					mRelationDotBtn.setVisibility(View.GONE);
				}
//				if (viewPager.getRelCircleFragment() != null) {
//					viewPager.getRelCircleFragment().checkNewSystemMessage();
//				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

	}

	@Override
	protected void onDestroy() {
		log.i("RelationCircleActvity onDestroy");
		if (mSystemPullReceiver != null && mHadRegistSystemPullReceiver) {
			unregisterReceiver(mSystemPullReceiver);
		}
		CyouApplication.setRelationCircleAct(null);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.circle_header_right_btn:
			intent.setClass(RelationCircleActvity.this,SendMyDynamicAcivity.class);
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	// @Override
	// protected void initEvent() {
	// // TODO Auto-generated method stub
	//
	// }
	// @Override
	// protected void initData() {
	// // TODO Auto-generated method stub
	//
	// }

	public HashMap<String, SoftReference<Bitmap>> getImageCache() {
		return this.mImageCache;
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("recycle", true);
	}

	/**
	 * 切换到广场
	 */
	public void intentSquareFragment() {
		try {
			mViewPager.setCurrentItem(1);
			resetRadioBtn(1);
			if (!recycled) {
//				viewPager.getRelSquareFragment().forceRequest();
				Intent intent = new Intent(
						 Contants.ACTION.REFRESH_SQUARE_CIRCLE);
						 sendBroadcast(intent);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
}
