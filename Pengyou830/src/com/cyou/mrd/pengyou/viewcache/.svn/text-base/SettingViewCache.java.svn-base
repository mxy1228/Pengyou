package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class SettingViewCache {

	private View mView;
	private TextView mNameTV;
	private ImageView mArrowIV;
	private CheckBox mCheckBox;
	private TextView mContentTV;
	private RelativeLayout mContentRL;
	
	public SettingViewCache(View view){
		this.mView = view;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.setting_item_name_tv);
		}
		return mNameTV;
	}

	public ImageView getmArrowIV() {
		if(mArrowIV == null){
			mArrowIV = (ImageView)mView.findViewById(R.id.setting_item_arrow_iv);
		}
		return mArrowIV;
	}

	public CheckBox getmCheckBox() {
		if(mCheckBox == null){
			mCheckBox = (CheckBox)mView.findViewById(R.id.setting_item_cb);
		}
		return mCheckBox;
	}

	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.setting_item_tv);
		}
		return mContentTV;
	}

	public RelativeLayout getmContentRL() {
		if(mContentRL == null){
			mContentRL = (RelativeLayout)mView.findViewById(R.id.setting_item_content_rl);
		}
		return mContentRL;
	}
	
	
}
