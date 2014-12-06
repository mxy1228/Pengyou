package com.cyou.mrd.pengyou.viewcache;

import com.cyou.mrd.pengyou.R;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class GuessGameViewCache {

	private View mView;
	private ImageView mAvatarIV;
	private TextView mNameTV;
	public GuessGameViewCache(View view) {
		this.mView = view;
	}

	public ImageView getmAvatarIV() {
		if (mAvatarIV == null) {
			mAvatarIV = (ImageView) mView.findViewById(R.id.img_game_icon);
		}
		return mAvatarIV;
	}

	public TextView getmNameTV() {
		if (mNameTV == null) {
			mNameTV = (TextView) mView.findViewById(R.id.txt_game_name);
		}
		return mNameTV;
	}

}
