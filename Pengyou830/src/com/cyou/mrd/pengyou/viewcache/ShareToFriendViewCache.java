package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class ShareToFriendViewCache {

	private View mView;
	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private CheckBox mCheckBox;
	
	public ShareToFriendViewCache(View view){
		this.mView = view;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.share_to_friend_item_avatar);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.share_to_friend_item_nickname_tv);
		}
		return mNickNameTV;
	}

	public CheckBox getmCheckBox() {
		if(mCheckBox == null){
			mCheckBox = (CheckBox)mView.findViewById(R.id.share_to_friend_item_chb);
		}
		return mCheckBox;
	}
	
	
}
