package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class SimilarGameViewCache {

	private View mView;
	private ImageView mGameImage;
	private TextView mNameTV;
	private TextView mGameType;
	private TextView frdcount;//frdplay显示多少好友在玩

	public SimilarGameViewCache(View view) {
		this.mView = view;
	}

	public ImageView getmGameImage() {
		if (mGameImage == null) {
			mGameImage = (ImageView) mView.findViewById(R.id.similar_game_icon);
		}
		return mGameImage;
	}

	public TextView getmNameTV() {
		if (mNameTV == null) {
			mNameTV = (TextView) mView.findViewById(R.id.similar_game_name);
		}
		return mNameTV;
	}

	public TextView getmGameType() {
		if (mGameType == null) {
			mGameType = (TextView) mView.findViewById(R.id.similar_game_type);
		}
		return mGameType;
	}



	public TextView getFrdcount() {
		if (frdcount == null) {
			frdcount = (TextView) mView.findViewById(R.id.similar_game_frdcount);
		}
		return frdcount;
	}



}
