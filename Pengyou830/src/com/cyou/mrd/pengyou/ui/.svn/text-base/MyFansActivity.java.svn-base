package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyFansAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.MyFansItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyFansActivity extends CYBaseActivity implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	// private EditText mSearchET;
	private PullToRefreshListView mListView;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	// private ImageButton mSearchCancelIBtn;
	private SearchBar mSearchBar;
	private WaitingDialog mWaitingDailog;
	private TextView mEmptyView;
	private LinearLayout mContentLL;
	private LinearLayout mSearchLL;

	private List<MyFansItem> mData;
	private MyFansAdapter mAdapter;
	private MyHttpConnect mConn;
	private InputMethodManager mImm;
	private long mLastFansCursor = 0;
	private int mUID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_fans);
		mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		log.d("onResume&&uid = "+mUID);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mUID = intent.getIntExtra(Params.FANS.UID,0);
		log.d("myFans uid = "+mUID);
		if(mUID == 0){
			this.mTitleTV.setText(getString(R.string.my_fans));
		}else{
			this.mTitleTV.setText(getString(R.string.his_fans));
			this.mSearchBar.setVisibility(View.GONE);
		}
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mData == null) {
			mData = new ArrayList<MyFansItem>();
		}
		if (mAdapter == null) {
			this.mAdapter = new MyFansAdapter(this, mData);
		}
		this.mListView.setAdapter(mAdapter);
		requestFans();
	}

	private void requestFans() {
		RequestParams params = new RequestParams();
		params.put("cursor", String.valueOf(mLastFansCursor));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		if (mUID != 0) {
			params.put("uid", String.valueOf(mUID));
		}
		mConn.post(HttpContants.NET.MY_FANS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						
					}

					@Override
					public void onFailure(Throwable error, String content) {
						try {
							mWaitingDailog.dismiss();
							showNetErrorDialog(MyFansActivity.this, new ReConnectListener() {
								
								@Override
								public void onReconnect() {
									requestFans();
								}
							});
						} catch (Exception e) {
							// TODO: handle exception
						}						
						mWaitingDailog.dismiss();
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyFansActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.i("search result = "+content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("result = " + content);
						List<MyFansItem> list = JsonUtils.json2List(
								JsonUtils.getJsonValue(content, "data"),
								MyFansItem.class);
						if (list != null && !list.isEmpty()) {
							if (list.size() < Config.PAGE_SIZE) {
								mListView.loadComplete();
							}
							mData.addAll(list);
							mAdapter.notifyDataSetChanged();
							mLastFansCursor = list.get(list.size() - 1)
									.getCursor();
							mListView.loadingFinish();
						} else {
							mListView.loadComplete();
						}
						mWaitingDailog.dismiss();
						if(mData.isEmpty()){
							mContentLL.setVisibility(View.GONE);
							mEmptyView.setVisibility(View.VISIBLE);
							if(mUID != 0){
								mEmptyView.setText(R.string.ta_no_fans);
							}else{
								mEmptyView.setText(R.string.no_fans);
							}
							mContentLL.setVisibility(View.GONE);
						}else{
							mEmptyView.setVisibility(View.GONE);
							mContentLL.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			MyFansActivity.this.finish();
			break;
		default:
			break;
		}
	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			MyFansItem item = (MyFansItem) mListView.getItemAtPosition(arg2);
			if (item != null) {
				Intent intent = new Intent(MyFansActivity.this,
						FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
				intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
				intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
				startActivity(intent);
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYFANS_DETAIL_ID,
						CYSystemLogUtil.ME.BTN_MYFANS_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
			}
		}

	}

	@Override
	protected void initView() {
		this.mWaitingDailog = new WaitingDialog(this);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.mListView = (PullToRefreshListView) findViewById(R.id.my_fensi_lv);
		this.mListView.setOnItemClickListener(new ItemClickListener());
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoad() {
				requestFans();

			}
		});
		View headerView = findViewById(R.id.my_fensi_header_bar);
		this.mTitleTV = (TextView) headerView
				.findViewById(R.id.sub_header_bar_tv);
		this.mTitleTV.setText("");
		this.mBackIBtn = (ImageButton) headerView
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		this.mSearchLL = (LinearLayout)findViewById(R.id.my_fans_search_ll);
		this.mEmptyView = (TextView)findViewById(R.id.my_fans_empty);
		this.mContentLL = (LinearLayout)findViewById(R.id.my_fans_content_ll);
		this.mSearchBar = (SearchBar) findViewById(R.id.my_fans_searchbar);
		this.mSearchBar.setTextAndActionListener(new TextAndActionListsner() {

			@Override
			public void onText() {

			}

			@Override
			public void onEmpty() {
				mContentLL.setVisibility(View.VISIBLE);
				mSearchLL.setVisibility(View.GONE);
			}

			@Override
			public void onAction(String key) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYFANS_SEARCH_ID,
						CYSystemLogUtil.ME.BTN_MYFANS_SEARCH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if(key != null && !TextUtils.isEmpty(key)){
					mContentLL.setVisibility(View.GONE);
					mSearchLL.setVisibility(View.VISIBLE);
					FansSearchResultFragment f = new FansSearchResultFragment();
					Bundle b = new Bundle();
					b.putString("key", key);
					f.setArguments(b);
					getSupportFragmentManager().beginTransaction().replace(R.id.my_fans_search_ll, f).commit();
				}
				
				View v = MyFansActivity.this.getCurrentFocus();
				if (v != null) {
					IBinder binder = v.getWindowToken();
					if (binder != null) {
						mImm.hideSoftInputFromWindow(binder,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}
		});
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	
}
