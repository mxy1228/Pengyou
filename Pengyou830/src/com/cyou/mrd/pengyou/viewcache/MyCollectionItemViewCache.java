package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class MyCollectionItemViewCache {

	private View mView;
	private TextView mNameTV;
	private RatingBar mRB;
	private TextView mFriendCountTV;
	private Button mDownloadBtn;
	private ImageView mIconIV;
	
	public MyCollectionItemViewCache(View view){
		this.mView = view;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.my_collection_item_name_tv);
		}
		return mNameTV;
	}

	public RatingBar getmRB() {
		if(mRB == null){
			mRB = (RatingBar)mView.findViewById(R.id.my_collection_item_rb);
		}
		return mRB;
	}

	public TextView getmFriendCountTV() {
		if(mFriendCountTV == null){
			mFriendCountTV = (TextView)mView.findViewById(R.id.my_collection_item_friend_count_tv);
		}
		return mFriendCountTV;
	}

	public Button getmDownloadBtn() {
		if(mDownloadBtn == null){
			mDownloadBtn = (Button)mView.findViewById(R.id.my_collection_item_download_btn);
		}
		return mDownloadBtn;
	}

	public ImageView getmIconIV() {
		if(mIconIV == null){
			mIconIV = (ImageView)mView.findViewById(R.id.my_collection_item_icon_iv);
		}
		return mIconIV;
	}
	
	
}
