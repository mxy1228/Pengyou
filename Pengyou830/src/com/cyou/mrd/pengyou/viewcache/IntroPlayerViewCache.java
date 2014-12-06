package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class IntroPlayerViewCache {

	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private View mView;
	
	public IntroPlayerViewCache(View view){
		this.mView = view;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.intro_player_item_avatar);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.intro_player_item_nickname_tv);
		}
		return mNickNameTV;
	}
	
}
