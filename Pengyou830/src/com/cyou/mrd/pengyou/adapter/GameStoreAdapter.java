package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.cyou.mrd.pengyou.ui.ClassifyFragment;
import com.cyou.mrd.pengyou.ui.RankingGameFragment;
import com.cyou.mrd.pengyou.ui.RecommendGameFragment;

public class GameStoreAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	private Activity mActivity;

	public GameStoreAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
		addTab(new RecommendGameFragment());
		addTab(new ClassifyFragment());
		addTab(new RankingGameFragment());
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
