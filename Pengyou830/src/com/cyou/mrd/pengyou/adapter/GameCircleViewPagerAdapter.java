package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;
import com.cyou.mrd.pengyou.ui.GameCircleFragment;
import com.cyou.mrd.pengyou.ui.GameDetailFragment;

public class GameCircleViewPagerAdapter extends FragmentStatePagerAdapter {

	public ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public GameCircleViewPagerAdapter(FragmentActivity activity) {
		super(activity.getSupportFragmentManager());
	}
    public GameCircleViewPagerAdapter(FragmentActivity activity , boolean isGameDetailPage,String gamecode, String gcid,String pkg,Handler mHandler) {
        super(activity.getSupportFragmentManager());
        // 如果是游戏详情界面添加2个framgment
        if (isGameDetailPage) {
            addTab(new GameDetailFragment(activity));
            addTab(new GameCircleFragment(activity,gamecode,isGameDetailPage,gcid ,pkg,mHandler));
        } else {
            addTab(new GameCircleFragment(activity,gamecode,isGameDetailPage,gcid,pkg,mHandler));
        }
    }

    public GameCircleViewPagerAdapter(FragmentActivity activity , boolean isGameDetailPage,String gamecode,Handler mHandler) {
        super(activity.getSupportFragmentManager());
        // 如果是游戏详情界面添加2个framgment
        if (isGameDetailPage) {       	
            addTab(new GameDetailFragment(activity));
            addTab(new GameCircleFragment(activity,gamecode,mHandler));
        } else {
            addTab(new GameCircleFragment(activity,gamecode,mHandler));
        }
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
