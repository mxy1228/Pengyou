package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.cyou.mrd.pengyou.ui.RelationShipCircleFragment;
import com.cyou.mrd.pengyou.ui.RelationShipSquareFragment;

public class RelationCircleViewPagerAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
//	private RelationShipCircleFragment myRelationShipCircleFragment;
//	private RelationShipSquareFragment myRelationShipSquareFragment;

	public RelationCircleViewPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
//		myRelationShipCircleFragment = new RelationShipCircleFragment();
//		myRelationShipSquareFragment = new RelationShipSquareFragment();
		addTab(new RelationShipCircleFragment());
		addTab(new RelationShipSquareFragment());
//		addTab(new RelationSNSFragment(mActivity));
	}

	public RelationCircleViewPagerAdapter(FragmentActivity activity, ArrayList<Fragment> fragmentLst) {
		super(activity.getSupportFragmentManager());
		mFragments = fragmentLst;
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

//	public RelationShipCircleFragment getRelCircleFragment(){
//		return  this.myRelationShipCircleFragment;
//	}
//	public RelationShipSquareFragment getRelSquareFragment(){
//		return  this.myRelationShipSquareFragment;
//	}
}
