package com.cyou.mrd.pengyou.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.LocationAdapter;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;

public class LocationSelectActivity extends Activity {

	private CYLog log = CYLog.getInstance();
	private final int RESULT_FROM_LOCATION = 5;

	private ExpandableListView mListView;
	private LocationAdapter mAdapter;
	// private HeaderBar mHeaderBar;
	private ImageButton mBackBtn;
	private WaitingDialog mDialog;
	private TextView mHeaderTV;
	private String[] parent = null;
	private String[][] childs = null;

	private String mLocate;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		try {
			setContentView(R.layout.location_select);
			mDialog = new WaitingDialog(this);
			initData();
			mDialog.show();
			new Thread() {

				@Override
				public void run() {
					mHandler.sendMessage(mHandler.obtainMessage());
				}

			}.start();
		} catch (Exception e) {
			log.e(e);
		}
	}

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				mDialog.dismiss();
			} catch (Exception e) {
				log.e(e);
			}
		}

	};

	private void initView() {
		try {
			this.mListView = (ExpandableListView) findViewById(R.id.location_select_lv);
			this.mAdapter = new LocationAdapter(parent, childs, this);
			this.mListView.setAdapter(mAdapter);
			this.mListView.setOnChildClickListener(new ItemClickListener());
			View headerBar = findViewById(R.id.add_friend_headerbar);
			this.mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			this.mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			this.mHeaderTV.setText(R.string.select_location);
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void initData() {
		try {
			this.parent = getResources().getStringArray(R.array.parents);
			int length = parent.length;
			this.childs = new String[length][];
			childs[0] = getResources().getStringArray(R.array.child0);
			childs[1] = getResources().getStringArray(R.array.child1);
			childs[2] = getResources().getStringArray(R.array.child2);
			childs[3] = getResources().getStringArray(R.array.child3);
			childs[4] = getResources().getStringArray(R.array.child4);
			childs[5] = getResources().getStringArray(R.array.child5);
			childs[6] = getResources().getStringArray(R.array.child6);
			childs[7] = getResources().getStringArray(R.array.child7);
			childs[8] = getResources().getStringArray(R.array.child8);
			childs[9] = getResources().getStringArray(R.array.child9);
			childs[10] = getResources().getStringArray(R.array.child10);
			childs[11] = getResources().getStringArray(R.array.child11);
			childs[12] = getResources().getStringArray(R.array.child12);
			childs[13] = getResources().getStringArray(R.array.child13);
			childs[14] = getResources().getStringArray(R.array.child14);
			childs[15] = getResources().getStringArray(R.array.child15);
			childs[16] = getResources().getStringArray(R.array.child16);
			childs[17] = getResources().getStringArray(R.array.child17);
			childs[18] = getResources().getStringArray(R.array.child18);
			childs[19] = getResources().getStringArray(R.array.child19);
			childs[20] = getResources().getStringArray(R.array.child20);
			childs[21] = getResources().getStringArray(R.array.child21);
			childs[22] = getResources().getStringArray(R.array.child22);
			childs[23] = getResources().getStringArray(R.array.child23);
			childs[24] = getResources().getStringArray(R.array.child24);
			childs[25] = getResources().getStringArray(R.array.child25);
			childs[26] = getResources().getStringArray(R.array.child26);
			childs[27] = getResources().getStringArray(R.array.child27);
			childs[28] = getResources().getStringArray(R.array.child28);
			childs[29] = getResources().getStringArray(R.array.child29);
			childs[30] = getResources().getStringArray(R.array.child30);
			childs[31] = getResources().getStringArray(R.array.child31);
			childs[32] = getResources().getStringArray(R.array.child32);
			childs[33] = getResources().getStringArray(R.array.child33);
			initView();
		} catch (Exception e) {
			log.e(e);
		}
	}

	private class ItemClickListener implements OnChildClickListener {

		@Override
		public boolean onChildClick(ExpandableListView parent, View v,
				int groupPosition, int childPosition, long id) {
			try {
				String p = LocationSelectActivity.this.parent[groupPosition];
				String c = LocationSelectActivity.this.childs[groupPosition][childPosition];
				if (p != null && c != null) {
					StringBuilder sb = new StringBuilder();
					mLocate = sb.append(p).append("-").append(c).toString();
					Intent intent = new Intent();
					intent.putExtra("location", mLocate);
					setResult(RESULT_FROM_LOCATION, intent);
					LocationSelectActivity.this.finish();
				} else {
					log.e("p or c is null");
				}
				return true;
			} catch (Exception e) {
				log.e(e);
			}
			return true;

		}

	}

	private class OnClickListener implements android.view.View.OnClickListener {

		@Override
		public void onClick(View v) {
			LocationSelectActivity.this.finish();
		}

	}
}
