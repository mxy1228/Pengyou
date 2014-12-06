package com.cyou.mrd.pengyou.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MessageCenterAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.log.CYLog;

/**
 * 私信点击进入的收件箱-消息中心
 * 
 * @author wangkang
 * 
 */
public class MessageCenterActivity extends BaseFragmentActivity implements
		OnClickListener {
	private CYLog log = CYLog.getInstance();
	private static final int ANIM_END = 1;

	private ViewPager mViewPager;
	private TextView mAnimTV;
	private TextView mPersonalTV;
	private TextView mRelationCircleTV;
	private ImageButton mBackBtn;
	private TextView mHeaderBarTV;
	private LinearLayout mTabLL;
	public static final int FROM_RELATION = 1;
	private int mCurIndex;
	private int mAnimTVWidth = 0;
	
	private MessageCenterAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_center);
		initView();
	}

	private void initView() {
		this.mViewPager = (ViewPager) findViewById(R.id.me_viewpager);
		this.mViewPager.setOnPageChangeListener(new PageChangeListener());
		String gcid  =  getIntent().getStringExtra(Params.GCID);
		this.mAdapter = new MessageCenterAdapter(MessageCenterActivity.this, gcid);
		this.mViewPager.setAdapter(mAdapter);
		this.mPersonalTV = (TextView) findViewById(R.id.message_center_personal_tv);
		this.mRelationCircleTV = (TextView) findViewById(R.id.message_center_relation_circle_tv);
		this.mTabLL = (LinearLayout)findViewById(R.id.message_center_tab_ll);
		this.mPersonalTV.setOnClickListener(this);
		this.mRelationCircleTV.setOnClickListener(this);
		this.mAnimTV = (TextView) findViewById(R.id.message_center_anim_tv);
		int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mAnimTVWidth = (screenWidth - (2 * getResources()
				.getDimensionPixelSize(R.dimen.me_tab_padding))) / 2;
		View headerBar = findViewById(R.id.message_center_header);
		this.mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackBtn.setOnClickListener(this);
		this.mHeaderBarTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		this.mHeaderBarTV.setText(R.string.message);
		this.mAnimTV.setWidth(mAnimTVWidth);
		int from = getIntent().getIntExtra(Params.FROM, 0);
		if (FROM_RELATION == from) {// 从关系圈跳转过来
			resetRadioBtn(1);
			this.mViewPager.setCurrentItem(1);
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
				if(mViewPager.getCurrentItem() == 1){
					Fragment f = mAdapter.getItem(1);
					if(f != null){
						((RelationCircleMessageFragement)f).requestMessage();
					}
				}
			}
		});
		mAnimTV.startAnimation(anim);
		mCurIndex = index;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case ANIM_END:
				for (int i = 0; i < mTabLL.getChildCount(); i++) {
					if (i == mCurIndex) {
						((TextView) mTabLL.getChildAt(i))
								.setTextColor(getResources().getColor(
										R.color.tab_text_select));
					} else {
						((TextView) mTabLL.getChildAt(i))
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			MessageCenterActivity.this.finish();
			break;
		case R.id.message_center_personal_tv:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.message_center_relation_circle_tv:
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}
}
