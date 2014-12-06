package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyFriendAdapter;
import com.cyou.mrd.pengyou.adapter.MyFriendRecommendAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.RecommendFriendItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.base.RecommendFriendBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyFriendActivity extends CYBaseActivity implements OnClickListener,OnItemClickListener {

	private CYLog log = CYLog.getInstance();

	private static final int REMOVE_RECOMMEND = 1;
	// private EditText mSearchET;
	private LinearLayout mGuessFriendLL;
	private static TextView mNewFriendCountTV;
	private GridView mRecommendGV;
	private PullToRefreshListView mListView;
	private TextView mFriendCountTV;
	private TextView mRecommendDividerTV;
	private ImageButton mIgnoreIBtn;
	private LinearLayout mRecommendTitleLL;
	private View mHeaderView;
	private SearchBar mSearchBar;
	private LinearLayout mGVLoadingLL;
	private TextView mEmptyView;
	private ImageButton mBackIBtn;
//	private ImageButton mAddFriendIBtn;
	private LinearLayout mAddFriendLL;
	private TextView mRecommendFriendCountTV;

	private MyFriendRecommendAdapter mRecommendAdapter;
	private List<RecommendFriendItem> mRecommendData;
	private List<FriendItem> mData;
	private MyFriendAdapter mAdapter;
	private MyHttpConnect mConn;
	private int mFriendReqeustCount = 1;
	private int mRecommendFriendPage = 1;
	private List<FriendItem> mSearchData;
//	private FriendDataObserver mFriendObserver;
	//add list step by step
	
	private int mIndex;
	private volatile int mCount;
	private volatile boolean mIsloading;


	@Override
	public void onCreate(Bundle savedInstanceState) {
//		CyouApplication.setMyFriendFragment(this);
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYFRIEND_ID,
				CYSystemLogUtil.ME.BTN_MYFRIEND_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
//		mFriendObserver = new FriendDataObserver(mHandler);
//		getContentResolver().registerContentObserver(
//				Uri.parse(FriendDBProvider.URI), true, mFriendObserver);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_friend);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		mHeaderView = mInflater.inflate(R.layout.my_friend_header_view, null);
		this.mRecommendFriendCountTV = (TextView)mHeaderView.findViewById(R.id.my_friend_guess_friend_count_tv);
		this.mEmptyView = (TextView) mHeaderView
				.findViewById(R.id.my_friend_empty);
		this.mGVLoadingLL = (LinearLayout) mHeaderView
				.findViewById(R.id.my_friend_recommend_loading_ll);
		this.mSearchBar = new SearchBar(this);
		this.mSearchBar.setTextAndActionListener(new TextAndActionListsner() {

			@Override
			public void onText() {

			}

			@Override
			public void onEmpty() {
				try {
					mAdapter.changeData(mData);
					if (mListView.getHeaderViewsCount() == 1) {
						mListView.addHeaderView(mHeaderView);
					}
				} catch (Exception e) {
					e.fillInStackTrace();
				}
			}

			@Override
			public void onAction(String key) {
				try {
					mSearchData = new FriendDao(MyFriendActivity.this).search(key);
					mAdapter.changeData(mSearchData);
					mListView.removeHeaderView(mHeaderView);
					InputMethodManager mImm = (InputMethodManager) MyFriendActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					View v = getCurrentFocus();
					if (v != null) {
						IBinder binder = v.getWindowToken();
						if (binder != null) {
							mImm.hideSoftInputFromWindow(binder,
									InputMethodManager.HIDE_NOT_ALWAYS);
						}
					}
				} catch (Exception e) {
					e.fillInStackTrace();
				}
			}
		});
		this.mGuessFriendLL = (LinearLayout) mHeaderView
				.findViewById(R.id.my_friend_guess_friend_fl);
		this.mGuessFriendLL.setOnClickListener(this);
		this.mNewFriendCountTV = (TextView) mHeaderView
				.findViewById(R.id.my_friend_add_friend_count_tv);
		this.mRecommendGV = (GridView) mHeaderView
				.findViewById(R.id.my_friend_recommend_gv);
		this.mListView = (PullToRefreshListView) findViewById(R.id.my_friend_lv);
		// this.mListView.addHeaderView(mSearchBar);
		this.mListView.addHeaderView(mHeaderView);
		this.mFriendCountTV = (TextView) mHeaderView
				.findViewById(R.id.my_friend_count_tv);
		this.mListView.setOnItemClickListener(this);
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
//					int headerCount = mListView.getHeaderViewsCount();
					int first = mListView.getFirstVisiblePosition();
					int last = mListView.getLastVisiblePosition();
					JSONArray array = new JSONArray();
					if (first >= 0) {
						for (int i = first; i <= last; i++) {
							if(i <= (mData.size() - 1)){
								array.put(mData.get(i).getUid());
							}
						}
						asyncFriendInfo(array);
					}
				}
				InputMethodManager inputMethodManager = (InputMethodManager) MyFriendActivity.this
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				View v = MyFriendActivity.this.getCurrentFocus();
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

			}

			@Override
			public void onLoad() {
				loadMoreFriend();

			}
		});
		this.mRecommendDividerTV = (TextView) mHeaderView
				.findViewById(R.id.my_friend_recommend_divider_tv);
		this.mRecommendGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				RecommendFriendItem item = (RecommendFriendItem) mRecommendGV
						.getItemAtPosition(arg2);
				if (item != null) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.ME.BTN_MYFRIEND_RECOM_DETAIL_ID,
							CYSystemLogUtil.ME.BTN_MYFRIEND_RECOM_DETAIL_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					Intent intent = new Intent(MyFriendActivity.this,
							FriendInfoActivity.class);
					intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
					intent.putExtra(Params.CHAT.NICK_NAME, item.getNickname());
					startActivity(intent);
				}
			}
		});
		this.mIgnoreIBtn = (ImageButton) mHeaderView
				.findViewById(R.id.my_friend_recommend_ignore_ibtn);
		this.mIgnoreIBtn.setOnClickListener(this);
		this.mRecommendTitleLL = (LinearLayout) mHeaderView
				.findViewById(R.id.my_friend_recommend_title);
		this.mBackIBtn = (ImageButton)findViewById(R.id.my_friend_back_ibtn);
		this.mBackIBtn.setOnClickListener(this);
//		this.mAddFriendIBtn = (ImageButton)findViewById(R.id.my_friend_add_btn);
//		this.mAddFriendIBtn.setOnClickListener(this);
		this.mAddFriendLL = (LinearLayout)findViewById(R.id.my_friend_add_friend_fl);
		this.mAddFriendLL.setOnClickListener(this);
	}
	@Override
	protected void onResume() {
		if(mAdapter!=null){
			getFriend(0, Config.PAGE_SIZE_RECOMMEND);
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
//		if (mFriendObserver != null) {
//			getContentResolver().unregisterContentObserver(
//					mFriendObserver);
//		}
		super.onDestroy();
	}
	
	private void asyncTop5Friends() {
		JSONArray array = new JSONArray();
		int length = mData.size() >= 5 ? 5 : mData.size();
		for (int i = 0; i < length; i++) {
			array.put(mData.get(i).getUid());
		}
		log.d("array = "+array.toString());
		asyncFriendInfo(array);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_friend_guess_friend_fl:
			Intent intent = new Intent(MyFriendActivity.this, GuessYouKnowActivity.class);
			startActivity(intent);
			mRecommendFriendCountTV.setVisibility(View.GONE);
			SystemCountMsgItem.clearRecommendFriendCount();
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_ADD_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_ADD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			break;
		case R.id.my_friend_recommend_ignore_ibtn:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_LOSE_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_LOSE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			startIgnoreAnim();
//			SharedPreferenceUtil.saveIgnoreRecommendFriendTime();
			break;
		case R.id.my_friend_back_ibtn:
			MyFriendActivity.this.finish();
			break;
//		case R.id.my_friend_add_btn:
//			intent = new Intent(MyFriendActivity.this, FindFriendActivity.class);
//			startActivity(intent);
//			break;
		case R.id.my_friend_add_friend_fl:
			intent = new Intent(MyFriendActivity.this, FindFriendActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void startIgnoreAnim() {
		TranslateAnimation anim = new TranslateAnimation(0,
				Util.getScreenWidthSize(MyFriendActivity.this), 0, 0);
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				mRecommendGV.setVisibility(View.GONE);
				mRecommendTitleLL.setVisibility(View.GONE);
			}
		});
		anim.setFillAfter(true);
		anim.setDuration(300);
		mRecommendGV.startAnimation(anim);
		mRecommendTitleLL.startAnimation(anim);
		mGVLoadingLL.setVisibility(View.GONE);
	}

	private void getFriend(int start, int count) {
		mData.clear();
//		List<FriendItem> list = new FriendDao(MyFriendActivity.this).getAllFriendList();
		List<FriendItem> list = new FriendDao(MyFriendActivity.this).getFriendList(start, count);
		Log.e("ShareToFriendActivity", "getFriendList count:=" + list.size());
		mData.addAll(list);
		mAdapter.notifyDataSetChanged();
		mFriendCountTV.setText(getString(R.string.hufen, mData.size()));
		if (mData.isEmpty()) {
			mEmptyView.setVisibility(View.VISIBLE);
		} else {
			mEmptyView.setVisibility(View.GONE);
		}
	}

	private void asyncFriendInfo(JSONArray array) {
		RequestParams params = new RequestParams();
		params.put("frdids", array.toString());
		mConn.post(HttpContants.NET.MYFRIEND_LIST, params,
				new AsyncHttpResponseHandler() {

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onStart() {
						super.onStart();
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyFriendActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.i("同步好友信息=" + content);
						try {
							String data = JsonUtils.getJsonValue(content,
									"data");
							for(FriendItem item : mData){
								log.d("======"+item.getUid()+"=======");
							}
							if (TextUtils.isEmpty(data)) {
								return;
							}
							List<FriendItem> list = JsonUtils.json2List(data,
									FriendItem.class);
							if (list != null) {
								new FriendDao(MyFriendActivity.this).update(list);
								for (FriendItem item : list) {
									for (FriendItem i : mData) {
										if (i.getUid() == item.getUid()) {
											i.setPicture(item.getPicture());
											i.setNickname(item.getNickname());
											i.setGender(item.getGender());
											i.setRecentgms(item.getRecentgms());
											i.setPlaynum(item.getPlaynum());
										}
									}
								}
								mAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}

	private void recommendFriends() {
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mRecommendFriendPage));
		params.put("count", String.valueOf(3));
		mConn.post(HttpContants.NET.RECOMMEND_FRIEND, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("get recommend friend result = " + content);
						try {
							RecommendFriendBase base = new ObjectMapper()
									.configure(
											DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
											false)
									.readValue(
											content,
											new TypeReference<RecommendFriendBase>() {
											});
							if (base == null) {
								return;
							}
							if (base.getData().isEmpty()) {
								mRecommendGV.setVisibility(View.GONE);
								mRecommendDividerTV.setVisibility(View.GONE);
								mGVLoadingLL.setVisibility(View.GONE);
								mRecommendTitleLL.setVisibility(View.GONE);
							} else {
								mRecommendGV.setVisibility(View.VISIBLE);
								mGVLoadingLL.setVisibility(View.GONE);
								mRecommendDividerTV.setVisibility(View.VISIBLE);
								mRecommendTitleLL.setVisibility(View.VISIBLE);
							}
							List<RecommendFriendItem> data = base.getData();
							mRecommendData.addAll(data);
							mRecommendAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyFriendActivity.this);
						dialog.create().show();
					}
				});
	}
	
	 private void loadMoreFriend(){
		 if (mIsloading) {
			return;
		}
		 mCount += com.cyou.mrd.pengyou.config.Config.PAGE_SIZE;
//		 Log.e("ShareToFriendActivity", "loadFriend count:=" + mCount + "page:" +(mIndex + 1));
		 mIsloading = true;
		RequestParams params = new RequestParams();
	//			params.put("frdids", array.toString());
		params.put("page", Integer.toString(mIndex + 1));
		params.put("count", Integer.toString(mCount) );
		mConn.post(HttpContants.NET.MYFRIEND_LIST, params,
			new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable error, String content) {
					super.onFailure(error, content);
					mIsloading= false;
//					Log.e("ShareToFriendActivity", "onFailure");
				}

				@Override
				public void onStart() {
					super.onStart();
				}
				
				@Override
				public void onLoginOut() {
					LoginOutDialog dialog = new LoginOutDialog(MyFriendActivity.this);
					dialog.create().show();
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
//					Log.e("ShareToFriendActivity", "loadFriend count:=" + mCount + " content: " + content);
					try {
						String data = JsonUtils.getJsonValue(content,
								"data");
						if (TextUtils.isEmpty(data)) {
							return;
						}
						List<FriendItem> list = JsonUtils.json2List(data,
								FriendItem.class);
						if (list != null) {
//							Log.e("ShareToFriendActivity", "list count:=" + list.size());
							mData.clear();
							mData.addAll(list);
							new FriendDao(MyFriendActivity.this).insertOrUpdateFriends(list);
							mAdapter.notifyDataSetChanged();
							if (list.size() < mCount ) {
								mListView.loadComplete();
							}
						}else{
							mListView.loadComplete();
						}
					mListView.loadingFinish();
					mIsloading = false;
					mFriendCountTV.setText(getString(R.string.hufen, mData.size()));
					} catch (Exception e) {
						log.e(e);
					}
				}
			}); 
	 }

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case REMOVE_RECOMMEND:
				startIgnoreAnim();
				break;
			default:
				break;
			}
		};
	};

//	/**
//	 * 监听我的好友数据库变化
//	 * 
//	 * @author xumengyang
//	 * 
//	 */
//	private class FriendDataObserver extends ContentObserver {
//
//		public FriendDataObserver(Handler handler) {
//			super(handler);
//		}
//
//		@Override
//		public void onChange(boolean selfChange) {
//			log.d("FriendDataObserver:onChange");
//			getFriend();
//		}
//	}

	
	@Override
	protected void initEvent() {
		
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		int recommendCount = intent.getIntExtra("count", 0);
		if(recommendCount == 0){
			log.e("recommendCount is null");
		}else{
			if(recommendCount == 0){
				mRecommendFriendCountTV.setVisibility(View.GONE);
			}else{
				mRecommendFriendCountTV.setVisibility(View.VISIBLE);
				if(recommendCount <= 99){
					mRecommendFriendCountTV.setText(String.valueOf(recommendCount));
				}else{
					mRecommendFriendCountTV.setText("99+");
				}
			}
		}
		boolean isGuestUser = UserInfoUtil.isGuestUser();
		if (isGuestUser) {
			// showToastMessage("当前用户为游客身份!",0);
			// return;
		}
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mRecommendData == null) {
			mRecommendData = new ArrayList<RecommendFriendItem>();
		}
		if (mRecommendAdapter == null) {
			this.mRecommendAdapter = new MyFriendRecommendAdapter(MyFriendActivity.this,
					mRecommendData, mHandler);
		}
		this.mRecommendGV.setAdapter(mRecommendAdapter);
		if (mData == null) {
			mData = new ArrayList<FriendItem>();
		}
		if (mAdapter == null) {
			mAdapter = new MyFriendAdapter(MyFriendActivity.this, mData);
//			if ((System.currentTimeMillis() - SharedPreferenceUtil
//					.getIgnoreRecommendFriendTime()) > ONE_DAY) {
				recommendFriends();
//			} else {
//				mRecommendGV.setVisibility(View.GONE);
//				mRecommendTitleLL.setVisibility(View.GONE);
//				mGVLoadingLL.setVisibility(View.GONE);
//			}
		}
		mListView.setAdapter(mAdapter);
		mData.clear();
		getFriend(0, Config.PAGE_SIZE_RECOMMEND);
		mCount = mData.size();
//		mListView.loadComplete();
		asyncTop5Friends();
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			if((arg2 - mListView.getHeaderViewsCount()) >= 0){
				FriendItem item = mData.get(arg2 - mListView.getHeaderViewsCount());
				if (item != null && item.getUid() != 0) {
					Intent intent = new Intent(MyFriendActivity.this, FriendInfoActivity.class);
					intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
					intent.putExtra(Params.CHAT.NICK_NAME, item.getNickname());
					startActivity(intent);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
}