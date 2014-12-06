package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cyou.mrd.pengyou.ui.MyMessageFragment;
import com.cyou.mrd.pengyou.ui.RelationCircleMessageFragement;

public class MessageCenterAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();;
	private Activity mActivity;

	public MessageCenterAdapter(FragmentActivity activity, String gcid) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
		addTab(new MyMessageFragment());
		addTab(new RelationCircleMessageFragement(gcid));
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
}
