package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class ContactViewCache {

	private View mView;
	private TextView mTopTV;
	private TextView mNameTV;
	private TextView mNumTV;
	private Button mInviteIBtn;
	
	public ContactViewCache(View view){
		this.mView = view;
	}

	public TextView getmTopTV() {
		if(mTopTV == null){
			mTopTV = (TextView)mView.findViewById(R.id.contact_item_top_tv);
		}
		return mTopTV;
	}

	public TextView getmNameTV() {
		if(mNameTV == null){
			mNameTV = (TextView)mView.findViewById(R.id.contact_item_name_tv);
		}
		return mNameTV;
	}

	public TextView getmNumTV() {
		if(mNumTV == null){
			mNumTV = (TextView)mView.findViewById(R.id.contact_item_num_tv);
		}
		return mNumTV;
	}

	public Button getmInviteIBtn() {
		if(mInviteIBtn == null){
			mInviteIBtn = (Button)mView.findViewById(R.id.contact_item_letter_ibtn);
		}
		return mInviteIBtn;
	}
	
	
}
