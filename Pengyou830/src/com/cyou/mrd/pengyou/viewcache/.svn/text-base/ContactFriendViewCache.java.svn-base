package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.widget.RoundImageView;

public class ContactFriendViewCache {

	private View mView;
    private TextView mTopTV;
	private TextView txtName;// name
	private TextView phonenum; // num
	private TextView txtPyouNickName;// nickiname
	private Button mInviteIBtn;

	private RoundImageView userIcon;
	private LinearLayout itemClick;

	public ContactFriendViewCache(View view) {
		this.mView = view;
	}

	public TextView getmTopTV() {
		if (mTopTV == null) {
			mTopTV = (TextView) mView.findViewById(R.id.contact_item_top_tv);
		}
		return mTopTV;
	}

	public TextView getName() {
		if (txtName == null) {
			txtName = (TextView) mView.findViewById(R.id.contact_item_name_tv);
		}
		return txtName;
	}

	public TextView getTxtPyouNickName() {
		if (txtPyouNickName == null) {
			txtPyouNickName = (TextView) mView
					.findViewById(R.id.contact_item_nickname_tv);
		}
		return txtPyouNickName;
	}

	public TextView getTxtTele() {
		if (phonenum == null) {
			phonenum = (TextView) mView.findViewById(R.id.contact_item_num_tv);
		}
		return phonenum;
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
			userIcon = (RoundImageView) mView
					.findViewById(R.id.img_contact_usericon);
		}
		return userIcon;
	}

	public LinearLayout getItemClick() {
		if (itemClick == null) {
			itemClick = (LinearLayout) mView.findViewById(R.id.item_click);
		}
		return itemClick;
	}

}