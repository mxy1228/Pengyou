package com.cyou.mrd.pengyou.viewcache;

import android.widget.Adapter;

public class MyGameListCategory {
	private Adapter mAdapter;

	public MyGameListCategory(Adapter adapter) {
		this.mAdapter = adapter;
	}

	public void setAdapter(Adapter adapter) {
		this.mAdapter = adapter;
	}
	public Adapter getAdapter() {
		return mAdapter;
	}

	public int getCount() {
		return mAdapter.getCount();
	}
}
