package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ShareToFriendAdapter;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ShareSearchResultFragment extends CYBaseFragment implements OnScrollListener{

	private ListView mListView;
	
	private List<FriendItem> mData;
	private Activity mActivity;
	private ShareToFriendAdapter mAdapter;
	private InputMethodManager mImm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.share_search_result, null);
		this.mListView = (ListView)view.findViewById(R.id.share_search_result_lv);
		initEvent();
		initData();
		return view;
	}
	
	@Override
	protected void initEvent() {
		mListView.setOnScrollListener(this);
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<FriendItem>();
		this.mAdapter = new ShareToFriendAdapter(mActivity, mData);
		this.mListView.setAdapter(mAdapter);
		Bundle b = getArguments();
		if(b != null){
			String key = b.getString("key");
			List<FriendItem> data = new FriendDao(
					mActivity).search(key);
			mData.addAll(data);
			mAdapter.notifyDataSetChanged();
			if(mData.isEmpty()){
				Toast.makeText(mActivity, R.string.search_friend_empty_hint, Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		this.mActivity = activity;
		this.mImm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		super.onAttach(activity);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}

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
		View v = mActivity.getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				mImm.hideSoftInputFromWindow(binder,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	private void asyncFriendInfo(JSONArray array) {
		RequestParams params = new RequestParams();
		params.put("frdids", array.toString());
		MyHttpConnect.getInstance().post(HttpContants.NET.MYFRIEND_LIST, params,
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
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
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
								new FriendDao(mActivity).update(list);
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
}
