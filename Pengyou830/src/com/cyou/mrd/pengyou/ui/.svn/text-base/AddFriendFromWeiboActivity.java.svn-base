package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.WeiboFriendAdapter;
import com.cyou.mrd.pengyou.entity.WeiboUser;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;

public class AddFriendFromWeiboActivity extends BaseActivity {

	private static final String TAG = "AddFriendFromWeiboActivity";

	private CYLog log = CYLog.getInstance();
	private final String APPKEY = "3443142555";
	private final int TYPE = 2;
	private static final int PAGE_SIZE = 20;
	private final String ID = "id";
	private final String NAME = "name";
	private final String AVATAR = "picture";
	private final String STATE = "state";
	private final String RESULT = "data";
	private final String UID = "uid";
	// private final String APPKEY = "304959529";
	// "http://www.test.com";

	private Button mBackBtn;
	private PullToRefreshListView mListView;
	private LinearLayout mLoadingView;
	private LinearLayout mEmptyLL;

	private WeiboFriendAdapter mAdapater;
	private MyHttpConnect mConn;
	private List<WeiboUser> mData = new ArrayList<WeiboUser>();

	private int mRequestCount = 1;
	private String mUID;
	private String mToken;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.add_friend_weibo);
		// mSsoHandler = new SsoHandler(this, mWeibo);
		// mSsoHandler.authorize(new AuthorizeListener());
		initView();
		Intent intent = getIntent();
		mUID = intent.getStringExtra("uid");
		mToken = intent.getStringExtra("token");
		mConn = MyHttpConnect.getInstance();
		// getWeiboFriend(TYPE, mUID, mToken, APPKEY, mRequestCount);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		mAdapater = null;
		super.onDestroy();
	}

	private void initView() {
		try {
			this.mListView = (PullToRefreshListView) findViewById(R.id.add_friend_weibo_lv);
			this.mListView.setOnLoadListener(new LoadListener() {

				@Override
				public void onLoad() {
					// getWeiboFriend(TYPE, mUID, mToken, APPKEY,
					// mRequestCount);
				}

				@Override
				public void onScroll(AbsListView view, int firstVisibleItem,
						int visibleItemCount, int totalItemCount) {
					// TODO Auto-generated method stub

				}

				@Override
				public void onScrollStateChanged(AbsListView view,
						int scrollState) {
					if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
					} else {
					}
				}
			});
			this.mLoadingView = (LinearLayout) findViewById(R.id.add_friend_weibo_loading_ll);
			this.mListView.setOnItemClickListener(new ItemClick());
			this.mEmptyLL = (LinearLayout) findViewById(R.id.add_friend_weibo_empty_ll);
		} catch (Exception e) {
			log.e(e);
		}
	}

	// private void getWeiboFriend(int type, final String uid, final String
	// token,
	// final String appkey, int requestCount) {
	// AjaxParams params = new AjaxParams();
	// params.put("accountType", String.valueOf(type));
	// params.put("weibouid", uid);
	// params.put("access_token", token);
	// params.put("pageNo", String.valueOf(requestCount));
	// params.put("pageSize", String.valueOf(PAGE_SIZE));
	// params.put("source", appkey);
	// mConn.post(HttpContants.NET.BIND_SNS, params,
	// new AjaxCallBack<String>() {
	//
	// @Override
	// public void onStart() {
	// // TODO Auto-generated method stub
	// super.onStart();
	// }
	//
	// @Override
	// public void onLoading(long count, long current) {
	// // TODO Auto-generated method stub
	// super.onLoading(count, current);
	// }
	//
	// @Override
	// public void onTimeOut() {
	// Toast.makeText(AddFriendFromWeiboActivity.this,
	// R.string.network_timeout, Toast.LENGTH_SHORT)
	// .show();
	// }
	//
	// @Override
	// public void onSuccess(String t) {
	// log.i(TAG, "result = " + t);
	// if (!TextUtils.isEmpty(t)) {
	// try {
	// List<WeiboUser> list = json2List(t);
	// if (mLoadingView.isShown()) {
	// mLoadingView.setVisibility(View.GONE);
	// }
	// if (!mListView.isShown()) {
	// mListView.setVisibility(View.VISIBLE);
	// }
	// if (list != null) {
	// mData.addAll(list);
	// if (mData.size() != 0) {
	// if (mAdapater == null) {
	// mAdapater = new WeiboFriendAdapter(
	// mData,
	// AddFriendFromWeiboActivity.this,
	// mBitmapManager);
	// mListView.setAdapter(mAdapater);
	// } else {
	// mAdapater.notifyDataSetChanged();
	// }
	// } else {
	// mListView.setVisibility(View.GONE);
	// mEmptyLL.setVisibility(View.VISIBLE);
	// }
	// if (list.size() < PAGE_SIZE) {
	// mListView.loadComplete();
	// }
	// mRequestCount++;
	// }
	// mListView.loadingFinish();
	// } catch (Exception e) {
	// log.e(TAG, e);
	// }
	// } else {
	// log.e(TAG, "result is null");
	// }
	// }
	//
	// });
	// }

	private class ItemClick implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			try {
				WeiboUser user = (WeiboUser) mListView.getItemAtPosition(arg2);
				if (user != null) {
					Intent intent = new Intent(AddFriendFromWeiboActivity.this,
							MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					intent.putExtra("user", user);
					startActivity(intent);
				} else {
					log.e("user is null");
				}
			} catch (Exception e) {
				log.e(e);
			}
		}

	}

	private List<WeiboUser> json2List(String result) {
		List<WeiboUser> list = new ArrayList<WeiboUser>();
		try {
			JSONObject obj = new JSONObject(result);
			if (obj.has(RESULT)) {
				JSONArray array = obj.getJSONArray(RESULT);
				int length = array.length();
				for (int i = 0; i < length; i++) {
					JSONObject json = array.optJSONObject(i);
					WeiboUser user = new WeiboUser();
					if (json.has(ID)) {
						user.setId(json.getString(ID));
					}
					if (json.has(NAME)) {
						user.setName(json.getString(NAME));
					}
					if (json.has(AVATAR)) {
						user.setAvatar(json.getString(AVATAR));
					}
					if (json.has(STATE)) {
						user.setState(json.getBoolean(STATE));
					}
					if (json.has(UID)) {
						user.uid = json.getString(UID);
					}
					list.add(user);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}
		return list;
	}
}
