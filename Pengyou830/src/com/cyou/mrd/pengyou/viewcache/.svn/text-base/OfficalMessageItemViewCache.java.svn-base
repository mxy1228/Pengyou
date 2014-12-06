package com.cyou.mrd.pengyou.viewcache;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class OfficalMessageItemViewCache {

	private View mView;
	private RoundImageView mAvatarIV;
	private TextView mContentTV;
	private TextView mDateTV;
	private ImageButton mDetailIBtn;
	private TextView mNameTV;
	
	public OfficalMessageItemViewCache(View view){
		this.mView = view;
	}

	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.offical_message_item_content_tv);
		}
		return mContentTV;
	}

	public TextView getmDateTV() {
		if(mDateTV == null){
			mDateTV = (TextView)mView.findViewById(R.id.offical_message_item_date_iv);
		}
		return mDateTV;
	}

	public ImageButton getmDetailIBtn() {
		if(mDetailIBtn == null){
			mDetailIBtn = (ImageButton)mView.findViewById(R.id.offical_message_item_detail_ibtn);
		}
		return mDetailIBtn;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.offical_message_item_name_tv);
		}
		return mNameTV;
	}

	public RoundImageView getmAvatarIV() {
		if(mAvatarIV == null){
			mAvatarIV = (RoundImageView)mView.findViewById(R.id.offical_message_item_avatar_iv);
		}
		return mAvatarIV;
	}
	
}
