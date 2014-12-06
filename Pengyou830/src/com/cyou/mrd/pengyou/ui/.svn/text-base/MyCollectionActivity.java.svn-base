package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyCollectionAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.base.MyGameBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MyCollectionActivity extends CYBaseActivity implements OnClickListener,OnItemClickListener{

	private PullToRefreshListView mListView;
	private ImageButton mBackIBtn;
	
	private List<GameItem> mData;
	private MyCollectionAdapter mAdapter;
	private TextView mEmptyView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_collection);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void initView() {
		this.mListView = (PullToRefreshListView)findViewById(R.id.my_collection_lv);
		this.mBackIBtn = (ImageButton)findViewById(R.id.my_collection_back_ibtn);
		this.mEmptyView = (TextView)findViewById(R.id.my_collection_empty);
	}

	@Override
	protected void initEvent() {
		this.mBackIBtn.setOnClickListener(this);
		this.mListView.setOnItemClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		loadMyCollection();
	}
	
	@Override
	protected void initData() {
		this.mData = new ArrayList<GameItem>();
		this.mAdapter = new MyCollectionAdapter(this, mData);
		this.mListView.setAdapter(mAdapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_collection_back_ibtn:
			MyCollectionActivity.this.finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 加载我的收藏列表
	 * xumengyang
	 */
	private void loadMyCollection(){
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_FAV_LIST, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
				showWaitingDialog();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(MyCollectionActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				try {
					MyGameBase base = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<MyGameBase>() {});
					List<GameItem> list = base.getData();
					//隐藏掉已安装的游戏
					if (list != null && !list.isEmpty()) {
						removeGameIfInstalled(list);
					}
					mData.clear();
					mData.addAll(list);
					mAdapter.notifyDataSetChanged();
					mListView.loadingFinish();
					mListView.loadComplete();
					if(mData.isEmpty()){
						mEmptyView.setVisibility(View.VISIBLE);
						mListView.setVisibility(View.GONE);
					}
				} catch (Exception e) {
					log.e(e);
				}
				log.d("我的收藏 = " + content);
				dismissWaitingDialog();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				showNetErrorDialog(MyCollectionActivity.this, new ReConnectListener(){

					@Override
					public void onReconnect() {
						loadMyCollection();
					}
					
				});
			}
		});
	}
	
	/**
	 * 去除我的收藏中已经安装的游戏
	 * @param list
	 */
	private void removeGameIfInstalled(List<GameItem> list) {
		List<PackageInfo> infos = getPackageManager().getInstalledPackages(0);
		Set<String> packages = new HashSet<String>();
		for(PackageInfo info : infos){
			packages.add(info.packageName);
		}
		Iterator<GameItem> it = list.iterator();
		while(it.hasNext()){
			if(!packages.add(it.next().getIdentifier())){
				it.remove();
			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try {
			if(arg2 < mData.size()){
				GameItem item = mData.get(arg2);
				if(item == null){
					log.e("item is null");
					return;
				}
				Intent detailIntent = new Intent();
				detailIntent.setClass(MyCollectionActivity.this,GameCircleDetailActivity.class);
				detailIntent.putExtra(Params.INTRO.GAME_CODE,item.getGamecode());
				detailIntent.putExtra(Params.INTRO.GAME_NAME,item.getName());
				startActivity(detailIntent);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
}
