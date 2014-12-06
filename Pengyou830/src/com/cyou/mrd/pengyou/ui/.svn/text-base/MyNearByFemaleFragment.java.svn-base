package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyNearByAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ChangeFocusInfo;
import com.cyou.mrd.pengyou.entity.MyNearByItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyNearByFemaleFragment extends CYBaseFragment implements LoadListener,RefreshListener,BDLocationListener,OnItemClickListener{

private PullToRefreshListView mListView;
	
private LocationClient mLocationClient;
private double mLatitude;
private double mLongitude;
private MyNearByAdapter mAdapter;
private Activity mActivity;
private List<MyNearByItem> mData;
private int mPageCount = 1; 
private boolean mLoading;
@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container,
		Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_nearby_lv, null);
		this.mListView = (PullToRefreshListView)view.findViewById(R.id.my_nearby_lv);
		initEvent();
		initData();
		return view;
	}
	
	@Override
	protected void initEvent() {
		this.mListView.setOnLoadListener(this);
		this.mListView.setOnRefreshListener(this);
		this.mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
		((MyNearByActivity)mActivity).showWaiting();
	}
	@Override
	public void onResume() {
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
	@Override
	protected void initData() {
		this.mData = new ArrayList<MyNearByItem>();
		startLocate();
	}
	
	@Override
	public void onLoad() {
		requestMyNearBy(mPageCount, false);
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
	public void onRefresh() {
		requestMyNearBy(1, true);
	}

	@Override
	public void onReceiveLocation(BDLocation arg0) {
		if (arg0 == null) {
			return;
		}
		mLatitude = arg0.getLatitude();
		mLongitude = arg0.getLongitude();
		if(mAdapter == null){
			this.mAdapter = new MyNearByAdapter(mActivity, mData, mLatitude, mLongitude);
			this.mListView.setAdapter(mAdapter);
		}
		mAdapter.setLatitue(mLatitude);
		mAdapter.setLongitude(mLongitude);
		requestMyNearBy(mPageCount, false);
		mLocationClient.stop();
	}

	@Override
	public void onReceivePoi(BDLocation arg0) {
		// TODO Auto-generated method stub
		
	}

	private void startLocate() {
		this.mLocationClient = new LocationClient(CyouApplication.mAppContext);
		this.mLocationClient.registerLocationListener(this);
		setLocationOption();
		this.mLocationClient.start();
		this.mLocationClient.requestLocation();
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setAddrType("all");
		option.setPriority(LocationClientOption.NetWorkFirst); // 设置网络优先
		option.setPoiNumber(10);
		option.disableCache(true);
		this.mLocationClient.setLocOption(option);
	}

	private void requestMyNearBy(final int page,final boolean refresh){
	if(mLoading){
		return;
	}
	RequestParams params = new RequestParams();
	params.put("latitude", String.valueOf(mLatitude));
	params.put("longitude", String.valueOf(mLongitude));
	params.put("page", String.valueOf(page));
	params.put("count", String.valueOf(Config.PAGE_SIZE));
	params.put("gender", String.valueOf(MyNearByActivity.FEMALE)); 
	MyHttpConnect.getInstance().post(HttpContants.NET.MY_NEARBY, params,
			new AsyncHttpResponseHandler() {
				@Override
				public void onStart() {
					log.d("requestMyNearBy----" + "mLoading=onStart");
					mLoading = true;
				}

				@Override
				public void onFailure(Throwable error, String content) {
					log.d("requestMyNearBy----" + "mLoading=onFailure");
					mLoading = false;
					if(mActivity != null){
						((MyNearByActivity)mActivity).showNetError();
					}
					((MyNearByActivity)mActivity).dismissWaiting();
				}
				
				@Override
				public void onLoginOut() {
					LoginOutDialog dialog = new LoginOutDialog(mActivity);
					dialog.create().show();
				}

				@Override
				public void onSuccess(int statusCode, String content) {
					super.onSuccess(statusCode, content);
					log.i("result = " + content);
					((MyNearByActivity)mActivity).dismissWaiting();
					List<MyNearByItem> list = JsonUtils.json2List(
							JsonUtils.getJsonValue(content, "data"),
							MyNearByItem.class);
					if (list != null && !list.isEmpty()) {
						if (!refresh) {
							mData.addAll(list);
							mPageCount += 1;
							mListView.loadingFinish();
						} else {
							mData.clear();
							mData.addAll(list);
							mPageCount = 2;
							mListView.onRefreshFinish();
						}
						mAdapter.notifyDataSetChanged();
						if (list.size() < Config.PAGE_SIZE) {
							mListView.loadComplete();
						}
					} else {
						log.e("list is null");
						if(page == 1 && mActivity != null){
							((MyNearByActivity)mActivity).showLocationError();
						}
						mListView.loadComplete();
					}
					mListView.loadingFinish();
					mLoading = false;
				}
			});
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		if(mData.isEmpty()){
			return;
		}
		int position = arg2 - 1;
		if(position < mData.size()){
			MyNearByItem item = mData.get(arg2 - 1);
			if (item != null) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYNEAR_DETAIL_ID,
						CYSystemLogUtil.ME.BTN_MYNEAR_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				Intent intent = new Intent(mActivity,
						FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
				mActivity.startActivity(intent);
			}
		}
	}
}
