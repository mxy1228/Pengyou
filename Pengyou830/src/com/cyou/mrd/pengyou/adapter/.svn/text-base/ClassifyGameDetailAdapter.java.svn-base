package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.cyou.mrd.pengyou.ui.ClassifyGameGoodFragment;
import com.cyou.mrd.pengyou.ui.ClassifyGameHotFragment;
import com.cyou.mrd.pengyou.ui.ClassifyGameNewFragment;

public class ClassifyGameDetailAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private Activity mActivity;

	public ClassifyGameDetailAdapter(FragmentActivity activity,String tid) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
		addTab(new ClassifyGameNewFragment(mActivity,tid));
		addTab(new ClassifyGameHotFragment(mActivity,tid));
		addTab(new ClassifyGameGoodFragment(mActivity,tid));
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	public void addTab(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.destroyItem(container, position, object);
	}

}
