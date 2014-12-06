package com.cyou.mrd.pengyou.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ClassifyGameDetailAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.NetUtil;

/**
 * 游戏分类的详细页面
 * 
 * @author wangkang
 * 
 */
public  class ClassifyGameDetailActvity extends CYBaseActivity implements
		OnCheckedChangeListener, OnClickListener {

	private CYLog log = CYLog.getInstance();
	private static final int ANIM_END = 1;

	private ViewPager mViewPager;
	private RadioGroup mRG;
	private TextView mAnimTV;
	private int mAnimTVWidth = 0;
	private int mCurIndex = 0;
	private String classifyName;
	private String tid;
	private ImageButton imgBtnSearch;
	private ImageButton mDownloadIBtn;
	private TextView mDownloadCountTV;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.classify_game_detail);
		checkNetAvailable();
	}

	@Override
	protected void initView() {
		tid = getIntent().getStringExtra(Params.CLASSIFY_ID);
		if (TextUtils.isEmpty(tid)) {
			finish();
		}
		this.mViewPager = (ViewPager) findViewById(R.id.me_viewpager);
		this.mViewPager.setOnPageChangeListener(new PageChangeListener());
		this.mViewPager.setAdapter(new ClassifyGameDetailAdapter(
				ClassifyGameDetailActvity.this, tid));
		this.mViewPager.setOffscreenPageLimit(3);
		this.mRG = (RadioGroup) findViewById(R.id.me_rg);
		this.mRG.setOnCheckedChangeListener(this);
		this.mAnimTV = (TextView) findViewById(R.id.me_anim_tv);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mAnimTVWidth = (screenWidth - (2 * getResources()
				.getDimensionPixelSize(R.dimen.me_tab_padding))) / 3;
		this.mAnimTV.setWidth(mAnimTVWidth);
		View headerBar = findViewById(R.id.layout_headerbar);
		ImageButton mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.gamestore_sub_header_back);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mBackBtn.setBackgroundResource(R.drawable.back_btn_xbg);
		mBackBtn.setVisibility(View.VISIBLE);
		//headerBar.findViewById(R.id.header_feedback_btn).setVisibility(View.GONE);
		TextView mHeaderTV = (TextView) headerBar.findViewById(R.id.gamestore_sub_header_tv);
		classifyName = getIntent().getStringExtra(Params.CLASSIFY_NAME);
		mHeaderTV.setText(classifyName);
		imgBtnSearch = (ImageButton) findViewById(R.id.gamestore_sub_header_search);
		imgBtnSearch.setOnClickListener(this);
		mDownloadIBtn = (ImageButton) findViewById(R.id.gamestore_sub_header_download);
		mDownloadIBtn.setOnClickListener(this);
		mDownloadIBtn.setVisibility(View.VISIBLE);
		this.mDownloadCountTV = (TextView) findViewById(R.id.gamestore_sub_header_download_count);
	}
	private void checkNetAvailable(){
		if(!NetUtil.isNetworkAvailable()){
			try {
				showNetErrorDialog(ClassifyGameDetailActvity.this,new ReConnectListener() {
					
					@Override
					public void onReconnect() {
						checkNetAvailable();
					}
				});
			} catch (Exception e) {
				log.e(e);
			}
			
		}else{
			initView();
		}
	}
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
			resetRadioBtn(arg0);
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.me_personal_center_btn:
			resetRadioBtn(0);
			mViewPager.setCurrentItem(0);
			break;
		case R.id.me_myfriend_btn:
			resetRadioBtn(1);
			mViewPager.setCurrentItem(1);
			break;
		case R.id.me_mynearby_btn:
			resetRadioBtn(2);
			mViewPager.setCurrentItem(2);
			break;
		default:
			break;
		}

	}

	private boolean mHadRegistDownloadReceiver = false;

	@Override
	protected void onResume() {
		super.onResume();
		updateDownloadCount(-1);
		try {
			if (mDownloadReceiver == null) {
				mDownloadReceiver = new DownloadCountReceiver();
			}
			if (mDownloadReceiver != null && !mHadRegistDownloadReceiver) {
				registerReceiver(mDownloadReceiver, new IntentFilter(
						Contants.ACTION.DOWNLOADING_COUNT));
				mHadRegistDownloadReceiver = true;
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void onDestroy() {
		try {
			if (mDownloadReceiver == null) {
				mDownloadReceiver = new DownloadCountReceiver();
			}
			if (mDownloadReceiver != null && !mHadRegistDownloadReceiver) {
				registerReceiver(mDownloadReceiver, new IntentFilter(
						Contants.ACTION.DOWNLOADING_COUNT));
				mHadRegistDownloadReceiver = true;
			}
			if (mHadRegistDownloadReceiver && mDownloadReceiver != null) {
				unregisterReceiver(mDownloadReceiver);
			}
		} catch (Exception e) {
			log.e(e);
		}
		super.onDestroy();
	}

	private DownloadCountReceiver mDownloadReceiver;

	private class DownloadCountReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(DownloadParam.STATE, -1);
			updateDownloadCount(status);
		}

	}
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-9-25下午4:46:10
	 *
	 * @param status
	 */
	private void updateDownloadCount (int status) {
		if(mDownloadCountTV==null){
			return;
		}
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.download_count);
		int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
		log.d("MainActivity: game Count is:" + count);
		if (count > 0) {
			mDownloadCountTV.setVisibility(View.VISIBLE);
			if (count <= 99) {
				mDownloadCountTV.setText(String.valueOf(count));
			} else {
				mDownloadCountTV.setText("N");
			}
			if (status == DownloadParam.TASK.ADD ) {
				mDownloadCountTV.startAnimation(anim);
			}
		} else {
			mDownloadCountTV.setVisibility(View.GONE);
			mDownloadCountTV.setText("");
		}
	}

	private void resetRadioBtn(final int index) {
		Animation anim = new TranslateAnimation(mCurIndex * mAnimTVWidth, index
				* mAnimTVWidth, 0, 0);
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

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.gamestore_sub_header_search:// 游戏搜索
			mIntent.putExtra(Params.CLASSIFY_ID, tid);
			mIntent.setClass(ClassifyGameDetailActvity.this,
					SearchActivity.class);
			startActivity(mIntent);
			break;
		case R.id.gamestore_sub_header_download:// 下载管理
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			mIntent.setClass(ClassifyGameDetailActvity.this,
					DownloadActivity.class);
			startActivity(mIntent);
			break;
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		// TODO Auto-generated method stub
		
	}

	
}
