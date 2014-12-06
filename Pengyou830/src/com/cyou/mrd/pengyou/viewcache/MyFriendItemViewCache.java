package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class MyFriendItemViewCache {

	private View mView;
	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private TextView mGameCountTV;
	private TextView mGameDetailTV;
	private Button mLetterIBtn;
	private TextView mTopTV;
	private ImageView mEachFocusIV;
	private Button mFocusIBtn;
	
	public MyFriendItemViewCache(View view){
		this.mView = view;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.my_friend_item_avater_iv);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.my_friend_item_nickname_tv);
		}
		return mNickNameTV;
	}

	public TextView getmGameCountTV() {
		if(mGameCountTV == null){
			mGameCountTV = (TextView)mView.findViewById(R.id.my_friend_item_game_count_tv);
		}
		return mGameCountTV;
	}

	public TextView getmGameDetailTV() {
		if(mGameDetailTV == null){
			mGameDetailTV = (TextView)mView.findViewById(R.id.my_friend_item_game_detail_tv);
		}
		return mGameDetailTV;
	}

	public Button getmLetterIBtn() {
		if(mLetterIBtn == null){
			mLetterIBtn = (Button)mView.findViewById(R.id.my_friend_item_letter_ibtn);
		}
		return mLetterIBtn;
	}

	public TextView getmTopTV() {
		if(mTopTV == null){
			mTopTV = (TextView)mView.findViewById(R.id.my_friend_item_top_tv);
		}
		return mTopTV;
	}

	public ImageView getmEachFocusIV() {
		if(mEachFocusIV == null){
			mEachFocusIV = (ImageView)mView.findViewById(R.id.my_friend_item_each_focus_iv);
		}
		return mEachFocusIV;
	}

	public Button getmFocusIBtn() {
		if(mFocusIBtn == null){
			mFocusIBtn = (Button)mView.findViewById(R.id.my_friend_item_focus_ibtn);
		}
		return mFocusIBtn;
	}
	
}
