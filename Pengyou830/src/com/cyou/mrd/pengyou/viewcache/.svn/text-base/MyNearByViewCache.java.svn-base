package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class MyNearByViewCache {

	private View mView;
	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private TextView mDistanceTV;
	private TextView mGameTV;
	private Button mAttentionIBtn;
	
	public MyNearByViewCache(View view){
		this.mView = view;
	}
	
	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.my_nearby_item_avatar_iv);
		}
		return mAvatarIV;
	}
	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.my_nearby_item_nickname_tv);
		}
		return mNickNameTV;
	}
	public TextView getmDistanceTV() {
		if(mDistanceTV == null){
			this.mDistanceTV = (TextView)mView.findViewById(R.id.my_nearby_item_distance_tv);
		}
		return mDistanceTV;
	}
	public TextView getmGameTV() {
		if(mGameTV == null){
			mGameTV = (TextView)mView.findViewById(R.id.my_nearby_item_game_tv);
		}
		return mGameTV;
	}

	public Button getmAttentionIBtn() {
		if(mAttentionIBtn == null){
			mAttentionIBtn = (Button)mView.findViewById(R.id.my_nearby_item_attention_ibtn);
		}
		return mAttentionIBtn;
	}
	
}
