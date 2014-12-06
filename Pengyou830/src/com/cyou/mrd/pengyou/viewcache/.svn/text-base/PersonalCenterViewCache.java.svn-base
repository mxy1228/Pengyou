package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class PersonalCenterViewCache {

	private View mView;
	private RelativeLayout mContentRL;
	private TextView mContentTV;
	private Button mDotBtn;
	private ImageView mPicIV;
	private TextView mHintTV;
	
	public PersonalCenterViewCache(View view){
		this.mView = view;
	}
	
	public RelativeLayout getmContentRL() {
		if(mContentRL == null){
			mContentRL = (RelativeLayout)mView.findViewById(R.id.personal_center_item_content_rl);
		}
		return mContentRL;
	}
	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.personal_center_item_tv);
		}
		return mContentTV;
	}
	public Button getmDotBtn() {
		if(mDotBtn == null){
			mDotBtn = (Button)mView.findViewById(R.id.personal_center_item_dot_btn);
		}
		return mDotBtn;
	}

	public ImageView getmPicIV() {
		if(mPicIV == null){
			mPicIV = (ImageView)mView.findViewById(R.id.personal_center_item_iv);
		}
		return mPicIV;
	}

	public TextView getmHintTV() {
		if(mHintTV == null){
			mHintTV = (TextView)mView.findViewById(R.id.personal_center_item_hint_tv);
		}
		return mHintTV;
	}
	
}
