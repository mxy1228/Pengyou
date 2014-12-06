package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.SearchUserAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ChangeFocusInfo;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.receiver.NotifyBroadcastReceiver;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.RequestParams;

public class SearchUserResultActivity extends CYBaseActivity {

	private View headview;
	private ImageButton mBackBtn;
	private PullToRefreshListView mPullListView;
	private int mRequestCount = 1;
	private String keyValue;
	private List<FriendItem> mData;
	private TextView emptyView;
	private MyHttpConnect mConn;
	private SearchUserAdapter mAdapter;
	// 刷新关注按钮页面不同步 广播 TODO
	private NotifyBroadcastReceiver notifyBroadcastReceiver;
	private boolean isNotifyBroadcastReceiver = false;
//	private List<FriendItem> mNotifyData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.search_user_result);
		initView();
	}

	public void initView() {
		mConn = MyHttpConnect.getInstance();
		keyValue = this.getIntent().getStringExtra("key");
		emptyView = (TextView) findViewById(R.id.search_result_txt_empty);
		emptyView.setText(getString(R.string.user_search_nodata_hint));
		emptyView.setVisibility(View.GONE);
		headview = findViewById(R.id.ll_search_user_result);
		mBackBtn = (ImageButton) headview
				.findViewById(R.id.sub_header_bar_left_ibtn);
		mBackBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		TextView tv_header = (TextView) headview
				.findViewById(R.id.sub_header_bar_tv);
		tv_header.setText(R.string.search_user_title);
		mPullListView = (PullToRefreshListView) findViewById(R.id.search_user_result_lv);

		search(keyValue, false);

		this.mPullListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			@Override
			public void onLoad() {
				keyValue = GetIntent();
				search(keyValue, false);
			}
		});

		if (mData == null) {
			mData = new ArrayList<FriendItem>();
		}
		if (mAdapter == null) {
			mAdapter = new SearchUserAdapter(SearchUserResultActivity.this,
					mData);
		}
		mPullListView.setAdapter(mAdapter);

		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (null != mData && mData.size() > 0) {
					FriendItem item = mData.get(position);
					Intent intent = new Intent(SearchUserResultActivity.this,
							FriendInfoActivity.class);
					intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
					startActivity(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		// 刷新关注按钮页面不同步 广播 TODO
		if (ChangeFocusInfo.changeList != null) {
			ArrayList<ChangeFocusInfo> ChangeList = ChangeFocusInfo.changeList;
			for(ChangeFocusInfo changeFocusBean:ChangeList){
				int follow = changeFocusBean.follow;
				int uid = changeFocusBean.uid;
				if(follow==1||follow==0){
					for(int i=0;i<mData.size();i++){
						if(uid==mData.get(i).getUid()){
							mData.get(i).setIsfocus(follow);
						}
					}
				}
			}
		}
		
		if (mAdapter != null) {
			mAdapter.updateData(mData);
			mAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}
//	@Override
//	protected void onDestroy() {
//		mAdapter = null;
//		if (notifyBroadcastReceiver != null && isNotifyBroadcastReceiver) {
//			unregisterReceiver(notifyBroadcastReceiver);
//			isNotifyBroadcastReceiver = false;
//		}
//		super.onDestroy();
//	}

	public String GetIntent() {
		Intent intent = getIntent();
		String key = intent.getStringExtra("key");
		return key;
	}

	/**
	 * 搜索用户
	 * 
	 * @param keyword
	 * @param currentPage
	 */
	@SuppressWarnings("unchecked")
	private void search(final String keyword, final boolean isreSearch) {
		// if(mPullListView.getHeaderViewsCount()>1){
		// mPullListView.removeHeaderView(emptyView);
		// }
		mPullListView.reset();
		if (mData == null) {
			mData = new ArrayList<FriendItem>();
		}
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mRequestCount));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("schkey", keyword);
		mPullListView.setVisibility(View.VISIBLE);
		emptyView.setVisibility(View.GONE);
		Log.d("lianwangxinxi", params.toString());
		mConn.post2Json(HttpContants.NET.SEARCH_USER, params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						FriendItem.class) {

					@Override
					public void onStart() {
						if (mRequestCount == 1) {
							showWaitingDialog();
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						try {
							dismissWaitingDialog();
							showNetErrorDialog(SearchUserResultActivity.this,
									new ReConnectListener() {
										@Override
										public void onReconnect() {
											search(keyword, isreSearch);
										}
									});
						} catch (Exception e) {
							// TODO: handle exception
						}

						super.onFailure(error, content);
					}

					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								SearchUserResultActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}

					@Override
					public void onSuccessForList(List list) {
						dismissWaitingDialog();
						if (isreSearch) {
							if (mData != null) {
								mData.clear();
							}
						}
						if (null == list || list.size() == 0) {
							mPullListView.loadingFinish();
							mPullListView.loadComplete();
							if (mData == null || mData.size() == 0) {
								// showToastMessage(
								// getString(R.string.user_search_nodata),
								// 1);
								// mPullListView.setEmptyView(emptyView);
								mPullListView.setVisibility(View.GONE);
								emptyView.setVisibility(View.VISIBLE);
							}
							return;
						}
						if (list.size() < Config.PAGE_SIZE) {
							mPullListView.loadComplete();
						}
						mData.addAll(list);
						mAdapter.updateData(mData);
						mAdapter.notifyDataSetChanged();
//						if (!isAddmRequestCount) {
							mRequestCount++;
//						}
						mPullListView.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void initData() {

	}

	@Override
	protected void onPause() {

		super.onPause();
	}

}
