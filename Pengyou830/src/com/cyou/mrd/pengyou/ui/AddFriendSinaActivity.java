package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.SinaFriendAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SinaFriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.RequestParams;

public class AddFriendSinaActivity extends BaseActivity {
	private CYLog log = CYLog.getInstance();
	private ListView mListView;
	private Animation loadingInAnim;
	private Animation loadingOutAnim;
	private List<SinaFriendItem> mContactList = new ArrayList<SinaFriendItem>();
	private SinaFriendAdapter mAdapter;
	private MyHttpConnect mConn;
	private String from;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_friend_sina);
		from = getIntent().getStringExtra(Params.FROM);
		initView();
		initData();
	}

	public void updateLstView(int position) {
		if (null != mContactList && mContactList.size() > position) {
			SinaFriendItem item = mContactList.get(position);
			item.setAttention(true);
			mContactList.set(position, item);
			mAdapter.updateListData(mContactList);
			mAdapter.notifyDataSetChanged();
		}
	}

	private void initView() {
		try {
			View headerBar = findViewById(R.id.edit_headerbar);
			ImageButton mBackBtn = (ImageButton) headerBar
					.findViewById(R.id.sub_header_bar_left_ibtn);
			mBackBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			TextView mHeaderTV = (TextView) headerBar
					.findViewById(R.id.sub_header_bar_tv);
			mHeaderTV.setText(R.string.app_name);
			Button mOkBtn = (Button) headerBar
					.findViewById(R.id.sub_header_bar_right_ibtn);
			mOkBtn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					finish();
				}
			});
			mOkBtn.setBackgroundResource(R.drawable.header_btn_xbg);
			mOkBtn.setText(R.string.btn_finish);

			mListView = (ListView) findViewById(R.id.add_friend_contacts_lv);
			mListView.setOnScrollListener(new OnScrollListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					View v = AddFriendSinaActivity.this.getCurrentFocus();
					if (v != null) {
						IBinder binder = v.getWindowToken();
						if (binder != null) {
							inputMethodManager.hideSoftInputFromWindow(binder,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}

				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}
			});
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void onDestroy() {
		mAdapter = null;
		super.onDestroy();
	}

	private void initData() {
		try {
			this.mConn = MyHttpConnect.getInstance();
			this.loadingInAnim = AnimationUtils.loadAnimation(this,
					R.anim.fading_in);
			this.loadingInAnim.setDuration(600);
			this.loadingOutAnim = AnimationUtils.loadAnimation(this,
					R.anim.fading_out);
			this.loadingOutAnim.setDuration(600);
			loadUserList();
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void loadUserList() {
		showLoadListProgressDialog();
		RequestParams params = new RequestParams();
		params.put("snsnm", "sina");
		params.put("snsusrid", SharedPreferenceUtil.getSinaWeiboUid());
		params.put("access_token", SharedPreferenceUtil.getSinaWeiboToken());
		params.put("country", Util.getCountryID());
		mConn.post2Json(HttpContants.NET.SNS_LIST, params,
				new JSONAsyncHttpResponseHandler<SinaFriendItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						SinaFriendItem.class) {
					@Override
					public void onFailure(Throwable error) {
						if (Params.FROM_EDIT.equals(from)) {
							finish();
						} else {
							Intent loginIntent = new Intent();
							loginIntent.setClass(AddFriendSinaActivity.this,
									LaunchActivity.class);
							startActivity(loginIntent);
							finish();
						}
						super.onFailure(error);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								AddFriendSinaActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onSuccessForList(List list) {
						dismissProgressDialog();
						if (null == list || list.size() == 0) {
							if (Params.FROM_EDIT.equals(from)) {
								finish();
							} else {
								Intent loginIntent = new Intent();
								loginIntent.setClass(
										AddFriendSinaActivity.this,
										LaunchActivity.class);
								startActivity(loginIntent);
								finish();
							}
							return;
						}
						mContactList = list;
						if (mAdapter == null) {
							mAdapter = new SinaFriendAdapter(
									AddFriendSinaActivity.this, mContactList,
									AddFriendSinaActivity.this);
						}
						mListView.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
						super.onSuccessForList(list);
					}
				});
	}

}
