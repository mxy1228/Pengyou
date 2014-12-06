package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.FrameLayout;

public class DynamicDetailSupporterViewCache {

	private FrameLayout mAvatarIV;
	
	public DynamicDetailSupporterViewCache(View view){
		this.mAvatarIV = (FrameLayout)view;
	}

	public FrameLayout getmAvatarIV() {
		return mAvatarIV;
	}

}
