package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.DownloadFragmentAdapter;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.log.CYLog;

public class DownloadActivity extends BaseFragmentActivity implements
		OnCheckedChangeListener {

	private CYLog log = CYLog.getInstance();
	private static final int ANIM_END = 1;

	private ViewPager mViewPager;
	private RadioGroup mRG;
	private TextView mAnimTV;
	private int mAnimTVWidth = 0;
	private int mCurIndex = 0;
	private ArrayList<Fragment> fragmentLst = new ArrayList<Fragment>();
	private DownloadFragment downloadFragment;
	private UpdateGameFragment updateGragment;
	private TextView txtDownloadCount;
	
	private TextView txtUpdateCount;
	private InstallGameFragment installGameFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_layout);
		initView();
		Intent intent = new Intent(this, DownloadService.class);
		startService(intent);
	}

	@Override
	protected void onRestart() {
		log.d("DownloadActivity onRestart");
		super.onRestart();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		log.d("onResume");
	}
	
	private void initView() {
		this.mViewPager = (ViewPager) findViewById(R.id.me_viewpager);
		this.mViewPager.setOnPageChangeListener(new PageChangeListener());
		downloadFragment = new DownloadFragment();
		installGameFragment = new InstallGameFragment();
		updateGragment = new UpdateGameFragment();
		fragmentLst.add(downloadFragment);
		fragmentLst.add(updateGragment);
		fragmentLst.add(installGameFragment);
		this.mViewPager.setAdapter(new DownloadFragmentAdapter(
				DownloadActivity.this, fragmentLst));
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
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView mHeaderTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		mHeaderTV.setText(R.string.pengyou_download_manager);
		txtDownloadCount = (TextView) findViewById(R.id.txt_download_count);
		txtUpdateCount = (TextView) findViewById(R.id.txt_update_count);
		setSelectedFragment();
	}
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-10-12下午3:17:57
	 * 
	 */
	private void setSelectedFragment () {
		updateGragment.loadUpdateGameList();
		int mDownloadCount = DownloadDao.getInstance(this).getDownloadingTaskSize();
		int mUpdateCount = updateGragment.getUpdateListCount();
		if (mDownloadCount <= 0 && mUpdateCount >0) {
			mViewPager.setCurrentItem(1);
		}else {
			mViewPager.setCurrentItem(0);
		}
		setUpdateCount(mUpdateCount);
		showUpdateCount();
	}

	public void setDownloadCount() {
		int mCount = DownloadDao.getInstance(this).getDownloadingTaskSize();
		log.d("当前下载:" + mCount);
		if (mCount == 0) {
			txtDownloadCount.setVisibility(View.GONE);
			txtDownloadCount.setText("0");
		} else {
			if (mCount < 100) {
				txtDownloadCount.setText(String.valueOf(mCount));
			} else {
				txtDownloadCount.setText("N");
			}
			if (mViewPager.getCurrentItem() != 0) {
				txtDownloadCount.setVisibility(View.VISIBLE);
			} else {
				txtDownloadCount.setVisibility(View.GONE);
			}

		}
//		if (installGameFragment != null) {
//			installGameFragment.initData();
//		}
	}

	private void showDownloadCount() {
		String txtCount = txtDownloadCount.getText().toString();
		log.d("当前文字:" + txtCount);
		if (mViewPager.getCurrentItem() == 0) {
			txtDownloadCount.setVisibility(View.GONE);
		} else {
			if (!TextUtils.isEmpty(txtCount) && !"0".equals(txtCount)) {
				txtDownloadCount.setVisibility(View.VISIBLE);
			} else {
				txtDownloadCount.setVisibility(View.GONE);
			}
		}
	}
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-10-12下午3:31:03
	 *
	 */
	public void setUpdateCount(int count) {
		if (count == 0) {
			txtUpdateCount.setVisibility(View.GONE);
			txtUpdateCount.setText("0");
		} else {
			if (count < 100) {
				txtUpdateCount.setText(String.valueOf(count));
			} else {
				txtUpdateCount.setText("N");
			}
			if (mViewPager.getCurrentItem() != 1) {
				txtUpdateCount.setVisibility(View.VISIBLE);
			} else {
				txtUpdateCount.setVisibility(View.GONE);
			}

		}
	}
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-10-12下午3:30:15
	 *
	 */
	private void showUpdateCount() {
		String txtCount = txtUpdateCount.getText().toString();
		log.d("当前文字:" + txtCount);
		if (mViewPager.getCurrentItem() == 1) {
			txtUpdateCount.setVisibility(View.GONE);
		} else {
			if (!TextUtils.isEmpty(txtCount) && !"0".equals(txtCount)) {
				txtUpdateCount.setVisibility(View.VISIBLE);
			} else {
				txtUpdateCount.setVisibility(View.GONE);
			}
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
			if (arg0 == 0) {
				downloadFragment.initData();
				downloadFragment.registReceiver();
				// downloadFragment.checkInstalled();
			}
			if (arg0 == 2) {
				if (installGameFragment != null) {
					installGameFragment.initData();
				}
			}
			setDownloadCount();
			showDownloadCount();
			showUpdateCount();
		}

	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_download:
			resetRadioBtn(0);
			downloadFragment.initData();
			mViewPager.setCurrentItem(0);
			downloadFragment.registReceiver();
			// downloadFragment.checkInstalled();
			showDownloadCount();
			showUpdateCount();
			break;
		case R.id.rbtn_update:
			resetRadioBtn(1);
			mViewPager.setCurrentItem(1);
			showDownloadCount();
			showUpdateCount();
			break;
		case R.id.rbtn_install:
			resetRadioBtn(2);
			mViewPager.setCurrentItem(2);
			showDownloadCount();
			showUpdateCount();
			break;
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

}
