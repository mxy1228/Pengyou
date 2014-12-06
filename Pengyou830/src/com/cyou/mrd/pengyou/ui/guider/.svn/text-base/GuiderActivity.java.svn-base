package com.cyou.mrd.pengyou.ui.guider;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.CYBaseActivity;
import com.cyou.mrd.pengyou.utils.Util;

/**
 * 引导页
 * 
 * @author wangkang
 * 
 */
public class GuiderActivity extends CYBaseActivity implements OnPageChangeListener{
	
	private ViewPager mViewPager;
	private LinearLayout mIndicatorLL;
	
	private List<Fragment> mFragments;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.guider);
		/**
		 * 后台统计日志
		 */
		String systemInfo = Util.getPhoneUA() + "_" + Util.getSdkCode();
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.BEHAVIOR_SYSTEMINFO, systemInfo);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		initView();
		initEvent();
		initData();
	}

	@Override
	protected void initView() {
		mViewPager = (ViewPager)findViewById(R.id.guider_viewpager);
		mIndicatorLL = (LinearLayout)findViewById(R.id.guider_indicator_ll);
	}

	@Override
	protected void initEvent() {
		mViewPager.setOnPageChangeListener(this);
	}

	@Override
	protected void initData() {
		mFragments = new ArrayList<Fragment>();
		mFragments.add(GuiderFragment.newInstance(0));
		mFragments.add(GuiderFragment.newInstance(1));
		mFragments.add(GuiderFragment.newInstance(2));
		mFragments.add(GuiderFragment.newInstance(3));
		mFragments.add(GuiderFragment.newInstance(4));
		mViewPager.setAdapter(new GuiderAdapter(getSupportFragmentManager()));
		mViewPager.setOffscreenPageLimit(4);
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {
		
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		((GuiderFragment)mFragments.get(arg0)).showAnim(arg0);
		indicator(arg0);
	}
	
	
	private class GuiderAdapter extends FragmentPagerAdapter{

		public GuiderAdapter(FragmentManager fm) {
			super(fm);
		}
		
		@Override
		public int getCount() {
			return mFragments.size();
		}
		
		@Override
		public Fragment getItem(int arg0) {
			return mFragments.get(arg0);
		}
	}

	private void indicator(int index){
		for(int i=0;i<mIndicatorLL.getChildCount();i++){
			ImageView iv = (ImageView)mIndicatorLL.getChildAt(i);
			if(i == index){
				iv.setImageResource(R.drawable.dot_selected);
			}else{
				iv.setImageResource(R.drawable.dot_unselected);
			}
		}
	}
}
