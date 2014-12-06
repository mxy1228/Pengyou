package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class DynamicCommentItemViewCache {

	private View mView;
	private TextView mContentTV;
	
	public DynamicCommentItemViewCache(View view){
		this.mView = view;
	}

	public TextView getmContentTV() {
		if(mContentTV == null){
			mContentTV = (TextView)mView.findViewById(R.id.dynamic_comment_item_content_tv);
		}
		return mContentTV;
	}

}
