package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyFocusAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.MyFocusItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FocusSearchResultFragment extends CYBaseFragment implements OnItemClickListener,LoadListener{

	private PullToRefreshListView mListView;
	
	private String mKey;
	private int mPage = 1;
	private List<MyFocusItem> mData;
	private MyFocusAdapter mAdapter;
	private Activity mActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_search_result, null);
		this.mListView = (PullToRefreshListView)view.findViewById(R.id.my_search_result_lv);
		initEvent();
		initData();
		return view;
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	
	@Override
	protected void initEvent() {
		this.mListView.setOnItemClickListener(this);
		this.mListView.setOnLoadListener(this);
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<MyFocusItem>();
		this.mAdapter = new MyFocusAdapter(getActivity(), mData);
		this.mListView.setAdapter(mAdapter);
		Bundle b = getArguments();
		if(b != null){
			mKey = b.getString("key");
			search(mKey);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyFocusItem item = (MyFocusItem) mListView.getItemAtPosition(arg2);
		if (item != null) {
			Intent intent = new Intent(getActivity(),
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

	@Override
	public void onLoad() {
		search(mKey);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}

	private void search(String key) {
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mPage));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("schkey", key);
		MyHttpConnect.getInstance().post(HttpContants.NET.SEARCH_MY_FOCUS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("search result = " + content);
						List<MyFocusItem> list = JsonUtils.json2List(
								JsonUtils.getJsonValue(content, "data"),
								MyFocusItem.class);
						if(list == null){
							list = new ArrayList<MyFocusItem>();
						}
						mData.addAll(list);
						mAdapter.notifyDataSetChanged();
						mPage++;
						if (list.size() < Config.PAGE_SIZE) {
							mListView.loadComplete();
						}
						mListView.loadingFinish();
					}
				});
	}
}
