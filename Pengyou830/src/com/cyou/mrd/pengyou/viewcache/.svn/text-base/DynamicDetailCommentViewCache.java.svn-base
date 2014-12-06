package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class DynamicDetailCommentViewCache {

	private RoundImageView mAvatarIV;
	private TextView mNickNameTV;
	private TextView mContentTV;
	private TextView mDateTV;
	private View mView;
	
	public DynamicDetailCommentViewCache(View view){
		this.mView = view;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.dynamic_detail_comment_item_avatar_iv);
		}
		return mAvatarIV;
	}

	public TextView getmNickNameTV() {
		if(mNickNameTV == null){
			mNickNameTV = (TextView)mView.findViewById(R.id.dynamic_detail_comment_item_nickname_tv);
		}
		return mNickNameTV;
	}

	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.dynamic_detail_comment_item_content_tv);
		}
		return mContentTV;
	}

	public TextView getmDateTV() {
		if(mDateTV == null){
			mDateTV = (TextView)mView.findViewById(R.id.dynamic_detail_comment_item_date_tv);
		}
		return mDateTV;
	}
	
	
}
