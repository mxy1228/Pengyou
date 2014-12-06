package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class WeiboFriendViewCache {

	private ImageView mAvatarIV;
	private TextView mNameTV;
	private TextView mStateTV;
	private View mView;
	
	public WeiboFriendViewCache(View view){
		mView = view;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.weibo_friend_item_name_tv);
		}
		return mNameTV;
	}

	public TextView getmStateTV() {
		if(mStateTV == null){
			mStateTV = (TextView)mView.findViewById(R.id.weibo_friend_item_state_tv);
		}
		return mStateTV;
	}

	public ImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (ImageView)mView.findViewById(R.id.weibo_friend_item_avatar_iv);
		}
		return mAvatarIV;
	}

	
}
