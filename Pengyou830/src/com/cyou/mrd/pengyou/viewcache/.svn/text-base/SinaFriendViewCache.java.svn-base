package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class SinaFriendViewCache {

	private View mView;
	private TextView mTopTV;
	private TextView txtTeleName;
	private TextView txtPyouNickName;
	private Button mInviteIBtn;
	private RoundImageView userIcon;
	private LinearLayout ll_alldate;

	public SinaFriendViewCache(View view) {
		this.mView = view;
	}

	public TextView getmTopTV() {
		if (mTopTV == null) {
			mTopTV = (TextView) mView.findViewById(R.id.contact_item_top_tv);
		}
		return mTopTV;
	}

	public TextView getTxtTeleName() {
		if (txtTeleName == null) {
			txtTeleName = (TextView) mView
					.findViewById(R.id.contact_item_name_tv);
		}
		return txtTeleName;
	}

	public TextView getTxtPyouNickName() {
		if (txtPyouNickName == null) {
			txtPyouNickName = (TextView) mView
					.findViewById(R.id.contact_item_nickname_tv);
		}
		return txtPyouNickName;
	}

	public Button getmInviteIBtn() {
		if (mInviteIBtn == null) {
			mInviteIBtn = (Button) mView
					.findViewById(R.id.contact_item_letter_ibtn);
		}
		return mInviteIBtn;
	}

	public RoundImageView getUserIcon() {
		if (userIcon == null) {
			userIcon = (RoundImageView) mView.findViewById(R.id.img_usericon);
		}
		return userIcon;
	}

	public LinearLayout GetAllUserInfo() {
		if (ll_alldate == null) {
			ll_alldate = (LinearLayout) mView.findViewById(R.id.ll_sian_friend);
		}
		return ll_alldate;
	}

}
