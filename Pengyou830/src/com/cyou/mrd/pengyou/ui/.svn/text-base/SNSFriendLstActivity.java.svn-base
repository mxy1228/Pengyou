package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.AddSNSFriendAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.AddSNSFriendLst;
import com.cyou.mrd.pengyou.entity.SinaFriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class SNSFriendLstActivity extends CYBaseActivity {
	private CYLog log = CYLog.getInstance();
	private PullToRefreshListView mListView;
	private Animation loadingInAnim;
	private Animation loadingOutAnim;
	private List<SinaFriendItem> mContactList = new ArrayList<SinaFriendItem>();
	private AddSNSFriendAdapter mAdapter;
	private MyHttpConnect mConn;
	private TextView txtEmpty;

	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_friend_sina_list);
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

	public void initView() {
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
			mHeaderTV.setText(R.string.add_sns_friend_title);
			mListView = (PullToRefreshListView) findViewById(R.id.add_friend_contacts_lv);
			mListView.setOnLoadListener(new LoadListener() {

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onLoad() {
					loadUserList();
				}
			});
			mAdapter = new AddSNSFriendAdapter(SNSFriendLstActivity.this,
					mContactList, SNSFriendLstActivity.this);
			mListView.setAdapter(mAdapter);
			txtEmpty = (TextView) findViewById(R.id.txt_empty);
			txtEmpty.setText(R.string.nodata_sinafriend);
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void onDestroy() {
		mAdapter = null;
		super.onDestroy();
	}

	protected void initData() {
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

	private int currentPageIndex = 1;

	private void loadUserList() {
		RequestParams params = new RequestParams();
		params.put("snsnm", "sina");
		params.put("snsusrid", SharedPreferenceUtil.getSinaWeiboUid());
		params.put("access_token", SharedPreferenceUtil.getSinaWeiboToken());
		params.put("page", String.valueOf(currentPageIndex));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		mConn.post(HttpContants.NET.SNS_LIST_FRIEND, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						// log.d("sns frinends is:" + content);
						if (TextUtils.isEmpty(content)) {
							// showToastMessage(getString(R.string.load_snsfriend_error),
							// 1);
							txtEmpty.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							mListView.loadComplete();
							return;
						}
						String data = JsonUtils.getJsonValue(content, "data");
						if (TextUtils.isEmpty(data)) {
							// showToastMessage(getString(R.string.load_snsfriend_error),
							// 1);
							mListView.loadComplete();
							txtEmpty.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							return;
						}
						AddSNSFriendLst addSNSFriendItem = JsonUtils.fromJson(
								data, AddSNSFriendLst.class);
						if (null == addSNSFriendItem) {
							// showToastMessage(getString(R.string.load_snsfriend_error),
							// 1);
							mListView.loadComplete();
							txtEmpty.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
							return;
						}
						ArrayList<SinaFriendItem> pengyouLst = addSNSFriendItem
								.getPengyou();
						ArrayList<SinaFriendItem> onlySnsLst = addSNSFriendItem
								.getOnlysns();
						int count = 0;
						if (null != pengyouLst && pengyouLst.size() > 0) {
							mContactList.addAll(pengyouLst);
							count = pengyouLst.size();
						}
						if (null != onlySnsLst && onlySnsLst.size() > 0) {
							mContactList.addAll(onlySnsLst);
							count += onlySnsLst.size();
						}
						if (count < Config.PAGE_SIZE) {
							mListView.loadComplete();
						}
						if (mContactList == null || mContactList.size() == 0) {
							// showToastMessage(getString(R.string.load_snsfriend_nodata),
							// 1);
							txtEmpty.setVisibility(View.VISIBLE);
							mListView.setVisibility(View.GONE);
						}
						if (mAdapter == null && mContactList != null) {
							mAdapter = new AddSNSFriendAdapter(
									SNSFriendLstActivity.this, mContactList,
									SNSFriendLstActivity.this);
							mListView.setAdapter(mAdapter);
						}
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
						currentPageIndex++;
						mListView.loadingFinish();
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
                        try {
                        	showNetErrorDialog(SNSFriendLstActivity.this, new ReConnectListener() {
    							@Override
    							public void onReconnect() {
    								loadUserList();
    							}
    						});
						} catch (Exception e) {
							// TODO: handle exception
						}						
						super.onFailure(error);
					}
				});
	}

	@Override
	protected void initEvent() {
		
	}

}
