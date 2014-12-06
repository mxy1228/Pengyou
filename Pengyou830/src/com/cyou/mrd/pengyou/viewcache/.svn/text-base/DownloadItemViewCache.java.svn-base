package com.cyou.mrd.pengyou.viewcache;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;

public class DownloadItemViewCache {

	private View mView;
	private RelativeLayout mTopLL;
	private TextView mTopTV;
	private ImageView mLogoIV;
	private TextView mNameTV;
	private ProgressBar mPB;
	private TextView mSizeTV;
//	private Button mBtn;
	private TextView mPercentTV;
	private TextView mSpeedTV;
	private Button btnUpdateAll;
	private TextView txtState;

	public DownloadItemViewCache(View view) {
		this.mView = view;
	}

	public RelativeLayout getmTopLL() {
		if (mTopLL == null) {
			mTopLL = (RelativeLayout) mView
					.findViewById(R.id.download_item_top_ll);
		}
		return mTopLL;
	}

	public TextView getTxtState() {
		if (txtState == null) {
			txtState = (TextView) mView.findViewById(R.id.txt_state);
		}
		return txtState;
	}

	public Button getBtnUpdateAll() {
		if (btnUpdateAll == null) {
			btnUpdateAll = (Button) mView.findViewById(R.id.btn_update_all);
		}
		return btnUpdateAll;
	}

	public TextView getmTopTV() {
		if (mTopTV == null) {
			mTopTV = (TextView) mView.findViewById(R.id.download_item_top_tv);
		}
		return mTopTV;
	}

	public ImageView getmLogoIV() {
		if (mLogoIV == null) {
			mLogoIV = (ImageView) mView
					.findViewById(R.id.download_item_logo_iv);
		}
		return mLogoIV;
	}

	public TextView getmNameTV() {
		if (mNameTV == null) {
			mNameTV = (TextView) mView.findViewById(R.id.download_item_name_tv);
		}
		return mNameTV;
	}

	public ProgressBar getmPB() {
		if (mPB == null) {
			mPB = (ProgressBar) mView.findViewById(R.id.download_item_pb);
		}
		return mPB;
	}

	public TextView getmSizeTV() {
		if (mSizeTV == null) {
			mSizeTV = (TextView) mView.findViewById(R.id.download_item_size_tv);
		}
		return mSizeTV;
	}

//	public Button getmBtn() {
//		if (mBtn == null) {
//			mBtn = (Button) mView.findViewById(R.id.btn_download);
//		}
//		return mBtn;
//	}

	public TextView getmPercentTV() {
		if (mPercentTV == null) {
			mPercentTV = (TextView) mView
					.findViewById(R.id.download_item_percent_tv);
		}
		return mPercentTV;
	}

	public TextView getmSpeedTV() {
		if (mSpeedTV == null) {
			mSpeedTV = (TextView) mView
					.findViewById(R.id.download_item_speed_tv);
		}
		return mSpeedTV;
	}

}
