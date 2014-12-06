package com.cyou.mrd.pengyou.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 游戏收藏
 * 
 * @author xuhan
 * 
 */
abstract public class FavGameListHeader extends BaseAdapter {
	private String mTitle;
	
	public FavGameListHeader(String header) {
		this.mTitle = header;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		return getTitleView(mTitle, position,convertView, parent);
	}

	protected abstract View getTitleView(String caption,int index,View convertView,ViewGroup parent);  
}
