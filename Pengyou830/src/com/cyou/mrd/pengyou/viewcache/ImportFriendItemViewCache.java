package com.cyou.mrd.pengyou.viewcache;

import com.cyou.mrd.pengyou.R;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ImportFriendItemViewCache {

	private View mView;
	private ImageView mAvatarIV;
	private TextView mNickNameTV;
	private ImageButton mLetterIBtn;
	private TextView mTopTV;

	public ImportFriendItemViewCache(View view) {
		this.mView = view;
	}

	public ImageView getmAvatarIV() {
		if (mAvatarIV == null) {
			mAvatarIV = (ImageView) mView
					.findViewById(R.id.my_friend_item_avater_iv);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if (mNickNameTV == null) {
			mNickNameTV = (TextView) mView
					.findViewById(R.id.txt_sns_name);
		}
		return mNickNameTV;
	}

	public ImageButton getmLetterIBtn() {
		if (mLetterIBtn == null) {
			mLetterIBtn = (ImageButton) mView
					.findViewById(R.id.my_friend_item_letter_ibtn);
		}
		return mLetterIBtn;
	}

	public TextView getmTopTV() {
		if (mTopTV == null) {
			mTopTV = (TextView) mView.findViewById(R.id.my_friend_item_top_tv);
		}
		return mTopTV;
	}

}
