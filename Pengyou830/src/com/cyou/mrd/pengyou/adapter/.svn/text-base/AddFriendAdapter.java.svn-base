package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.cyou.mrd.pengyou.ui.AddFriendImportFragment;
import com.cyou.mrd.pengyou.ui.ContactsFragment;
import com.cyou.mrd.pengyou.ui.FriendKnownFragment;

public class AddFriendAdapter extends FragmentStatePagerAdapter {

	private FragmentActivity mActivity;
	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public AddFriendAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
		this.mActivity = activity;
		addTab(new FriendKnownFragment());
		addTab(new ContactsFragment());
		addTab(new AddFriendImportFragment());
//		addTab(new AddFriendByDCodeFragment(mActivity));
	}

	@Override
	public Fragment getItem(int arg0) {
		return mFragments.get(arg0);
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}

	public void addTab(Fragment fragment) {
		mFragments.add(fragment);
		notifyDataSetChanged();
	}
}
