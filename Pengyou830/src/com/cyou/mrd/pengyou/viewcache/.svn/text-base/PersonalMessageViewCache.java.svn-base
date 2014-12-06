package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class PersonalMessageViewCache {

	private View mView;
	private TextView mNameTV;
	private RoundImageView mAvatarIV;
//	private TextView mTypeTV;
	private TextView mContentTV;
	private TextView mDateTV;
//	private RelativeLayout mInviteRL;
//	private ImageView mIconIV;
//	private TextView mGameNameTV;
//	private TextView mPlayerNumTV;
	private TextView mUnreadCountTV;
	
	public PersonalMessageViewCache(View view){
		this.mView = view;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.message_item_name_tv);
		}
		return mNameTV;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.message_item_avatar_iv);
		}
		return mAvatarIV;
	}

	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.message_item_content_tv);
		}
		return mContentTV;
	}

	public TextView getmDateTV() {
		if(mDateTV == null){
			mDateTV = (TextView)mView.findViewById(R.id.message_item_date_tv);
		}
		return mDateTV;
	}

	public TextView getmUnreadCountTV() {
		if(mUnreadCountTV == null){
			mUnreadCountTV = (TextView)mView.findViewById(R.id.message_item_unread_count);
		}
		return mUnreadCountTV;
	}

}
