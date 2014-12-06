package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

public class GuiderAdapter extends PagerAdapter {

	private List<View> mViews;
	private ViewPager mViewPager;
	
	public GuiderAdapter(List<View> views,ViewPager viewpager){
		this.mViews = views;
		this.mViewPager = viewpager;
	}
	
	@Override
	public int getCount() {
		return mViews.size();
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(View container, int position) {
		this.mViewPager.addView(mViews.get(position));
		return mViews.get(position);
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		this.mViewPager.removeView(mViews.get(position));
	}

}
