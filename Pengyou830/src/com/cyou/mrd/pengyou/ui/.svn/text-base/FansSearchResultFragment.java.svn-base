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
import com.cyou.mrd.pengyou.adapter.MyFansAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.MyFansItem;
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

public class FansSearchResultFragment extends CYBaseFragment implements OnItemClickListener,LoadListener{

	private PullToRefreshListView mListView;
	
	private List<MyFansItem> mData;
	private MyFansAdapter mAdapter;
	private String mKey;
	private long mSearchLastFansCursor = 0;
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
	protected void initEvent() {
		this.mListView.setOnItemClickListener(this);
		this.mListView.setOnLoadListener(this);
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<MyFansItem>();
		this.mAdapter = new MyFansAdapter(getActivity(), mData);
		this.mListView.setAdapter(mAdapter);
		Bundle b = getArguments();
		if(b != null){
			mKey = b.getString("key");
			search(mKey);
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		MyFansItem item = (MyFansItem) mListView.getItemAtPosition(arg2);
		if (item != null) {
			Intent intent = new Intent(getActivity(),
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

	private void search(String key) {
		RequestParams params = new RequestParams();
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("schkey", key);
		params.put("cursor", String.valueOf(mSearchLastFansCursor));
		MyHttpConnect.getInstance().post(HttpContants.NET.SEARCH_MY_FANS, params,
			new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
				}

				@Override
				public void onFailure(Throwable error, String content) {
					// TODO Auto-generated method stub
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
					List<MyFansItem> list = JsonUtils.json2List(
							JsonUtils.getJsonValue(content, "data"),
							MyFansItem.class);
					if(list == null){
						list = new ArrayList<MyFansItem>();
					}
					mData.addAll(list);
					mAdapter.notifyDataSetChanged();
					if (list != null && !list.isEmpty()) {
						mSearchLastFansCursor = list.get(list.size() - 1).getCursor();
					}
					if (list.size() < Config.PAGE_SIZE) {
						mListView.loadComplete();
					}
					mListView.loadingFinish();
				}

			});
	}
	
}
