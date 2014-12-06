package com.cyou.mrd.pengyou.ui;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap.Config;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ShareToFriendAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.db.LetterDBHelper;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ShareToFriendActivity extends CYBaseActivity implements
		OnClickListener {

	private CYLog log = CYLog.getInstance();

	private ImageView mIconIV;
	private EditText mContentET;
	private PullToRefreshListView mListView;
	// private EditText mSearchET;
	private ImageButton mBackIBtn;
	private Button mShareBtn;
	private TextView mTitleTV;
	private SearchBar mSearchBar;
	private TextView mEmptyView;
	private LinearLayout mSearchLL;
	private LinearLayout mContentLL;

	private List<FriendItem> mData;
	private ShareToFriendAdapter mAdapter;
	private GameItem mGameItem;
	public static Map<Integer, FriendItem> mSelectData;
	private MyHttpConnect mConn;
	private InputMethodManager mImm;
	private WaitingDialog mWaitingDialog;
	private boolean mSharing;
	
	private int mIndex;
	private volatile int mCount;
	private volatile boolean mIsloading;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.share_to_friend);
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		this.mEmptyView = (TextView)findViewById(R.id.share_to_friend_empty);
		this.mIconIV = (ImageView) findViewById(R.id.share_to_friend_icon_iv);
		this.mContentET = (EditText) findViewById(R.id.share_to_friend_et);
		this.mListView = (PullToRefreshListView) findViewById(R.id.share_to_friend_lv);
		this.mListView.setOnLoadListener(new LoadListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
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
				View v = ShareToFriendActivity.this.getCurrentFocus();
				if (v != null) {
					IBinder binder = v.getWindowToken();
					if (binder != null) {
						mImm.hideSoftInputFromWindow(binder,
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
				loadMoreFriend();
				// TODO Auto-generated method stub
				
			}
		});
		this.mSearchBar = (SearchBar) findViewById(R.id.shared_to_friend_searchbar);
		View headerBar = findViewById(R.id.share_to_friend_headerbar);
		this.mBackIBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		this.mTitleTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		this.mTitleTV.setText(R.string.share_to_friend);
		this.mShareBtn = (Button) headerBar
				.findViewById(R.id.sub_header_bar_right_ibtn);
		this.mShareBtn
				.setBackgroundResource(R.drawable.header_btn_xbg);
		this.mShareBtn.setText(R.string.share);
		this.mShareBtn.setOnClickListener(this);
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
				if(key != null && !TextUtils.isEmpty(key)){
					mContentLL.setVisibility(View.GONE);
					mSearchLL.setVisibility(View.VISIBLE);
					ShareSearchResultFragment f = new ShareSearchResultFragment();
					Bundle b = new Bundle();
					b.putString("key", key);
					f.setArguments(b);
					getSupportFragmentManager().beginTransaction().replace(R.id.share_to_friend_search_ll, f).commit();
				}
				
				View v = ShareToFriendActivity.this.getCurrentFocus();
				if (v != null) {
					IBinder binder = v.getWindowToken();
					if (binder != null) {
						mImm.hideSoftInputFromWindow(binder,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}
		});
		this.mWaitingDialog = new WaitingDialog(this);
		this.mSearchLL = (LinearLayout)findViewById(R.id.share_to_friend_search_ll);
		this.mContentLL = (LinearLayout)findViewById(R.id.share_to_friend_container_ll);
	}

	@Override
	protected void initData() {
		Intent intent = getIntent();
		mGameItem = intent.getParcelableExtra(Params.SHARE_TO_FRIEND.GAME_ITEM);
		if (mGameItem != null) {
			DisplayImageOptions option = new DisplayImageOptions.Builder()
					.cacheInMemory(true).cacheOnDisc(true)
					.showImageForEmptyUri(R.drawable.icon_default)
					.showImageOnFail(R.drawable.icon_default)
					.showStubImage(R.drawable.icon_default).build();
			if (!TextUtils.isEmpty(mGameItem.getIcon())) {
				CYImageLoader.displayIconImage(mGameItem.getIcon(), mIconIV,option);
			}
			if (!TextUtils.isEmpty(mGameItem.getName())) {
				mContentET.setText(getString(R.string.share_to_friend_hint,
						mGameItem.getName()));
			}
		}
		mConn = MyHttpConnect.getInstance();
		mSelectData = new HashMap<Integer, FriendItem>();
		mData = new FriendDao(this).getFriendList(0, com.cyou.mrd.pengyou.config.Config.PAGE_SIZE_RECOMMEND);
		mAdapter = new ShareToFriendAdapter(this, mData);
		mListView.setAdapter(mAdapter);
//		mListView.loadComplete();
		if(mData.isEmpty()){
			mEmptyView.setVisibility(View.VISIBLE);
		}else{
			mEmptyView.setVisibility(View.GONE);
		}
		int length = mData.size() >= 10 ? 10 : mData.size();
		mCount = mData.size();
		JSONArray array = new JSONArray();
		for (int i = 0; i < length; i++) {
			array.put(mData.get(i).getUid());
		}
		log.d("array = "+array.toString());
		asyncFriendInfo(array);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mWaitingDialog.dismiss();
			Toast.makeText(ShareToFriendActivity.this, R.string.send_sms_success, Toast.LENGTH_SHORT).show();
			mSharing = false;
			ShareToFriendActivity.this.finish();
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			ShareToFriendActivity.this.finish();
			break;
		case R.id.sub_header_bar_right_ibtn:
			if(mSharing){
				return;
			}
			final String content = mContentET.getText().toString();
			if (TextUtils.isEmpty(content.trim())) {
				showShortToast(R.string.share_to_friend_empty);
				return;
			}
			if (mSelectData.isEmpty()) {
				showShortToast(R.string.share_no_friends);
				return;
			}
			mWaitingDialog.show();
			share(content);
			break;
		default:
			break;
		}
	}

	private void share(final String shareContent) {
		if(mSharing){
			return;
		}
		mSharing = true;
		Iterator<Integer> it = mSelectData.keySet().iterator();
		final long time = System.currentTimeMillis();
		JSONArray array = new JSONArray();
		final LetterDao dao = new LetterDao(this);
		while (it.hasNext()) {
			int uid = it.next();
			array.put(uid);
		}
		RequestParams params = new RequestParams();
		params.put("text", shareContent);
		params.put("gamecode", mGameItem.getGamecode());
		params.put("tofrdid", array.toString());
		MyHttpConnect.getInstance().post(HttpContants.NET.SHARE_GAME_TO_FRIEND, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, final String content) {
				super.onSuccess(statusCode, content);
				if(content == null || TextUtils.isEmpty(content)){
					return;
				}
				log.d("分享游戏给好友="+content);
				try {
					JSONObject obj = new JSONObject(content);
					if(obj.has("successful")){
						if(obj.getInt("successful") == 1){
							new Thread(){
								public void run() {
									Iterator<Integer> it = mSelectData.keySet().iterator();
									while(it.hasNext()){
										int uid = it.next();
										MyMessageItem item = new MyMessageItem();
										item.setFrom(UserInfoUtil.getCurrentUserId());
										item.setTauid(uid);
										item.setTo(uid);
										item.setContent(shareContent);
										item.setType(Contants.CHAT_TYPE.GAME);
										item.setGamecode(mGameItem.getGamecode());
										item.setCreatetime(time);
										item.setSendSuccess(Contants.LETTER_SEND_SUCCESS.YES);
										item.setMsgseq("");
										item.setSendState(LetterDBHelper.SEND_STATE.SEND_SUCCESS);
										dao.insert(item);
									}
									mHandler.sendEmptyMessage(0);
								};
							}.start();
						}
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(ShareToFriendActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				showShortToast(R.string.share_to_friend_failed);
				mWaitingDialog.dismiss();
				mSharing = false;
			}
			
		});
	}

	 public boolean isNetworkConnected() {  
         ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
         if (mNetworkInfo != null) {  
             return mNetworkInfo.isAvailable();  
         }  
	     return false;  
	 }
	 
	 private void loadMoreFriend(){
//		 Log.e("ShareToFriendActivity", "loadFriend count:=" + mCount);
		 if (mIsloading) {
			return;
		}
		 mIsloading = true;
			mCount += com.cyou.mrd.pengyou.config.Config.PAGE_SIZE;
		RequestParams params = new RequestParams();
	//			params.put("frdids", array.toString());
		params.put("page", Integer.toString(mIndex + 1));
		params.put("count", Integer.toString(mCount) );
		mConn.post(HttpContants.NET.MYFRIEND_LIST, params,
			new AsyncHttpResponseHandler() {

				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
					super.onFailure(error, content);
					mIsloading = false;
				}

				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
				}
				
				@Override
				public void onLoginOut() {
					LoginOutDialog dialog = new LoginOutDialog(ShareToFriendActivity.this);
					dialog.create().show();
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
//					Log.e("ShareToFriendActivity", "loadFriend:=" + content);
					try {
						String data = JsonUtils.getJsonValue(content,
								"data");
						if (TextUtils.isEmpty(data)) {
							return;
						}
						List<FriendItem> list = JsonUtils.json2List(data,
								FriendItem.class);
						if (list != null) {
							mData.clear();
							mData.addAll(list);
							mAdapter.notifyDataSetChanged();
							new FriendDao(ShareToFriendActivity.this).insertOrUpdateFriends(list);
							if (list.size() < mCount ) {
								mListView.loadComplete();
							}
						}else{
							mListView.loadComplete();
						}
					
						mListView.loadingFinish();
					} catch (Exception e) {
						log.e(e);
					}
					mIsloading = false;
				}
			}); 
	 }
	 
	 private void asyncFriendInfo(JSONArray array) {
			RequestParams params = new RequestParams();
			params.put("frdids", array.toString());
			mConn.post(HttpContants.NET.MYFRIEND_LIST, params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable error, String content) {
							// TODO Auto-generated method stub
							super.onFailure(error, content);
						}

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
						}
						
						@Override
						public void onLoginOut() {
							LoginOutDialog dialog = new LoginOutDialog(ShareToFriendActivity.this);
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
									new FriendDao(ShareToFriendActivity.this).update(list);
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
	 
	 @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(mWaitingDialog.isShowing()){
				ShareToFriendActivity.this.finish();
			}
			break;

		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

}
