package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class RecommendGameViewCache {

	private View mView;
	private ImageView mAvatarIV;
	private TextView mNameTV;
	private TextView mTxtGameInfo;
	private RatingBar ratingBarFirst;
	private ImageView imgToday;
	private LinearLayout linearLayout;
	public RecommendGameViewCache(View view) {
		this.mView = view;
	}

	public ImageView getmAvatarIV() {
		if (mAvatarIV == null) {
			mAvatarIV = (ImageView) mView.findViewById(R.id.img_game_icon);
		}
		return mAvatarIV;
	}
	public ImageView getImgToday() {
		if (imgToday == null) {
			imgToday = (ImageView) mView.findViewById(R.id.img_today);
		}
		return imgToday;
	}
	public TextView getmNameTV() {
		if (mNameTV == null) {
			mNameTV = (TextView) mView.findViewById(R.id.txt_game_name);
		}
		return mNameTV;
	}

	public TextView getmTxtGameInfo() {
		if (mTxtGameInfo == null) {
			mTxtGameInfo = (TextView) mView.findViewById(R.id.txt_game_info);
		}
		return mTxtGameInfo;
	}

	public RatingBar getRatingBarFirst() {
		if (ratingBarFirst == null) {
			ratingBarFirst = (RatingBar) mView.findViewById(R.id.rb_game_first);
		}
		return ratingBarFirst;
	}

	public LinearLayout getLinearLayout() {
		if(linearLayout==null){
			linearLayout= (LinearLayout) mView.findViewById(R.id.layout);
		}
		return linearLayout;
	}

}
