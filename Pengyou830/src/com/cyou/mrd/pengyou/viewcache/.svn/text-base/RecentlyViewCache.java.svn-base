package com.cyou.mrd.pengyou.viewcache;

import com.cyou.mrd.pengyou.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentlyViewCache {

	private View mView;
	private ImageView mIconIV;
	private TextView mTV;
	
	public RecentlyViewCache(View view){
		this.mView = view;
	}

	public TextView getmTV() {
		if(mTV == null){
			mTV = (TextView)mView.findViewById(R.id.recently_game_item_tv);
		}
		return mTV;
	}

	public ImageView getmIconIV() {
		if(mIconIV == null){
			mIconIV = (ImageView)mView.findViewById(R.id.recently_game_item_icon_iv);
		}
		return mIconIV;
	}
	
	
}
