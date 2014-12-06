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
import com.cyou.mrd.pengyou.adapter.MyFocusAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.MyFocusItem;
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

public class MyFocusActivity extends CYBaseActivity implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	// private EditText mSearchET;
	private PullToRefreshListView mListView;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	// private ImageButton mSearch.CancelIBtn;
	private SearchBar mSearchBar;
	private WaitingDialog mWaitingDialog;
	private TextView mEmptyView;
	private LinearLayout mContentLL;
	private LinearLayout mSearchLL;

	private List<MyFocusItem> mData;
	private MyFocusAdapter mAdapter;
	private MyHttpConnect mConn;
//	private List<MyFocusItem> mTempData;
	private InputMethodManager mImm;
//	private long mCursor;
	private int mRequestCount = 1;
//	private long mSearchCursor;
//	private int mSearchCount = 1;
	private int mUID;
//	private String mKey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.d("onCreate");
		setContentView(R.layout.my_attention);
		mImm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		this.mWaitingDialog = new WaitingDialog(this);
		this.mWaitingDialog.show();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.mEmptyView = (TextView)findViewById(R.id.my_focus_empty);
		this.mContentLL = (LinearLayout)findViewById(R.id.my_focus_content_ll);
		this.mSearchLL = (LinearLayout)findViewById(R.id.my_focus_search_ll);
		this.mListView = (PullToRefreshListView) findViewById(R.id.my_attention_lv);
		this.mListView.setOnItemClickListener(new ItemClickListener());
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				View v = getCurrentFocus();
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

			@Override
			public void onLoad() {
//				if(mKey == null){
					requestMyFocus();
//				}else{
//					search(mKey);
//				}
			}
		});
		View headerBar = findViewById(R.id.my_attention_header_bar);
		this.mTitleTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		this.mTitleTV.setText("");
		this.mBackIBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		this.mSearchBar = (SearchBar) findViewById(R.id.my_focus_searchbar);
		this.mSearchBar.setTextAndActionListener(new TextAndActionListsner() {

			@Override
			public void onText() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onEmpty() {
				mSearchLL.setVisibility(View.GONE);
				mContentLL.setVisibility(View.VISIBLE);
//				if (!mTempData.isEmpty()) {
//					mData.clear();
//					mData.addAll(mTempData);
//					mTempData.clear();
//					mAdapter.notifyDataSetChanged();
//					mListView.reset();
//					mSearchCount = 1;
//					if(mData.size() < Config.PAGE_SIZE){
//						mListView.loadComplete();
//					}
//				}else{
////					mKey = null;
//					mSearchCount = 1;
//					mData.clear();
//					requestMyFocus();
//				}
			}

			@Override
			public void onAction(String key) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYATTENTION_SEARCH_ID,
						CYSystemLogUtil.ME.BTN_MYATTENTION_SEARCH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if(key != null && !TextUtils.isEmpty(key)){
					mContentLL.setVisibility(View.GONE);
					mSearchLL.setVisibility(View.VISIBLE);
					FocusSearchResultFragment f = new FocusSearchResultFragment();
					Bundle b = new Bundle();
					b.putString("key", key);
					f.setArguments(b);
					getSupportFragmentManager().beginTransaction().replace(R.id.my_focus_search_ll, f).commit();
				}
				View v = MyFocusActivity.this.getCurrentFocus();
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
	protected void onResume() {
		log.d("onResume");
		super.onResume();
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mUID = intent.getIntExtra(Params.FOCUS.UID,0);
		if(mUID == 0){
			this.mTitleTV.setText(getString(R.string.my_focus));
		}else{
			this.mTitleTV.setText(getString(R.string.his_focus));
			this.mSearchBar.setVisibility(View.GONE);
		}
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mData == null) {
			mData = new ArrayList<MyFocusItem>();
		}
		if (mAdapter == null) {
			this.mAdapter = new MyFocusAdapter(this, mData);
		}
//		if (mTempData == null) {
//			mTempData = new ArrayList<MyFocusItem>();
//		}
		this.mListView.setAdapter(mAdapter);
		requestMyFocus();
	}

	private void requestMyFocus() {
		RequestParams params = new RequestParams();
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("page", String.valueOf(mRequestCount));
		if (mUID != 0) {
			params.put("uid", String.valueOf(mUID));
		}
		mConn.post(HttpContants.NET.MY_FOCUS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						
					}

					@Override
					public void onFailure(Throwable error, String content) {
						try {
							mWaitingDialog.dismiss();
							showNetErrorDialog(MyFocusActivity.this, new ReConnectListener() {								
								@Override
								public void onReconnect() {
									requestMyFocus();
								}
							});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyFocusActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("result = " + content);
						List<MyFocusItem> list = JsonUtils.json2List(
								JsonUtils.getJsonValue(content, "data"),
								MyFocusItem.class);
						if (list != null && !list.isEmpty()) {
							if (list.size() < Config.PAGE_SIZE) {
								mListView.loadComplete();
							}
							mData.addAll(list);
							mAdapter.notifyDataSetChanged();
							mListView.loadingFinish();
							mRequestCount++;
						} else {
							mListView.loadComplete();
						}
						mWaitingDialog.dismiss();
						if(mData.isEmpty()){
							mEmptyView.setVisibility(View.VISIBLE);
							mContentLL.setVisibility(View.GONE);
						}else{
							mEmptyView.setVisibility(View.GONE);
							mContentLL.setVisibility(View.VISIBLE);
						}
					}
				});
	}

//	private void search(String key) {
//		RequestParams params = new RequestParams();
//		params.put("page", String.valueOf(mSearchCount));
//		params.put("count", String.valueOf(Config.PAGE_SIZE));
//		params.put("schkey", key);
//		mConn.post(HttpContants.NET.SEARCH_MY_FOCUS, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						super.onStart();
//						mWaitingDialog.show();
//					}
//
//					@Override
//					public void onFailure(Throwable error, String content) {
//						super.onFailure(error, content);
//						mWaitingDialog.dismiss();
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						if (TextUtils.isEmpty(content)) {
//							return;
//						}
//						log.i("search result = " + content);
//						List<MyFocusItem> list = JsonUtils.json2List(
//								JsonUtils.getJsonValue(content, "data"),
//								MyFocusItem.class);
//						if(list == null){
//							list = new ArrayList<MyFocusItem>();
//						}
//						mTempData.addAll(mData);
//						if(mSearchCount == 1){
//							mData.clear();
//						}
//						mData.addAll(list);
//						mAdapter.notifyDataSetChanged();
//						mSearchCount++;
//						if (list.size() < Config.PAGE_SIZE) {
//							mListView.loadComplete();
//						}
//						mWaitingDialog.dismiss();
//					}
//				});
//	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			MyFocusItem item = (MyFocusItem) mListView.getItemAtPosition(arg2);
			if (item != null) {
				Intent intent = new Intent(MyFocusActivity.this,
						FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
				intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
				intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
				startActivity(intent);
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYATTENTION_DETAIL_ID,
						CYSystemLogUtil.ME.BTN_MYATTENTION_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			MyFocusActivity.this.finish();
			break;
		default:
			break;
		}
	}

	

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

}
