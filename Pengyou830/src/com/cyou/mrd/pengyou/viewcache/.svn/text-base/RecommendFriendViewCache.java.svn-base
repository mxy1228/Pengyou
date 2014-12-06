package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class RecommendFriendViewCache {

	private View mView;
	private RoundImageView mAvatarIV;
	private TextView mNameTV;
	private TextView mDetailTV;
	private ImageButton mFocusIBtn;
	
	public RecommendFriendViewCache(View view){
		this.mView = view;
	}
	
	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.my_friend_recommend_avatar);
		}
		return mAvatarIV;
	}
	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.my_friend_recommend_nickname_tv);
		}
		return mNameTV;
	}
	public TextView getmDetailTV() {
		if(mDetailTV == null){
			mDetailTV = (TextView)mView.findViewById(R.id.my_friend_recommend_detail_tv);
		}
		return mDetailTV;
	}

	public ImageButton getmFocusIBtn() {
		if(mFocusIBtn == null){
			mFocusIBtn = (ImageButton)mView.findViewById(R.id.my_friend_recommend_focus_btn);
		}
		return mFocusIBtn;
	}
	
	
}
