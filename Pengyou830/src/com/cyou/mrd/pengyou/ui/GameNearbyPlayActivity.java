package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RankingGameAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.CYBaseActivity.ReConnectListener;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RankingGameViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.RequestParams;

public class GameNearbyPlayActivity extends CYBaseActivity implements OnClickListener  {
	public static final int INDEX_FRIEND = 0;// 朋友榜
	public static final int INDEX_NEAR = 2;// 附近榜
	private CYLog log = CYLog.getInstance();

	PullToRefreshListView mPullListViewNear;
	boolean isInitData = false;// 是否已经初始化
	private List<GameItem> nearGameList = new ArrayList<GameItem>();
	private RankingGameAdapter myGameNearAdapter;
	private int nearCurrentPageNum = 1;

	private LocationClient mLocationClient;
	private double mLatitude;
	private double mLongitude;
	private TextView txtEmpty;
	private DownloadDao downloadDao;
	private ImageButton  mBackBtn;
	private ImageButton  mSearchBtn;
	private ImageButton mDownloadImg;
	private TextView mDownloadCountTV;
	private boolean mHadRegistDownloadReceiver = false;
	private DownloadCountReceiver mDownloadReceiver;
	private boolean refresh = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEARPLAYING_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEARPLAYING_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		setContentView(R.layout.gamestore_nearby);
		initView();
		initData();	
		registReceiver();
		registerDownloads();
	}

	private void startLocate() {
		this.mLocationClient = new LocationClient(CyouApplication.mAppContext);
		this.mLocationClient.registerLocationListener(new LocationListener());
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

	private class LocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			log.d("loadNearGameList 0");
			if (arg0 == null) {
				return;
			}
			mLatitude = arg0.getLatitude();
			mLongitude = arg0.getLongitude();
			loadNearGameList();
			mLocationClient.stop();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			log.d("onReceivePoi");

		}

	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		log.d("PersonalCenterFragment on start!");
		
		if(null!=myGameNearAdapter){
			myGameNearAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}

	/**
	 * 加载附近的游戏
	 */
	private void loadNearGameList() {
		RequestParams params = new RequestParams();
		String url = "";
		url = HttpContants.NET.GAME_RANK_NEAR;
		params.put("page", String.valueOf(nearCurrentPageNum));
		params.put("latitude", String.valueOf(mLatitude));
		params.put("longitude", String.valueOf(mLongitude));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		mPullListViewNear.reset();
		MyHttpConnect.getInstance().post2Json(
				url,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						mPullListViewNear.loadComplete();
						mPullListViewNear.loadingFinish();
						showNetErrorDialog(GameNearbyPlayActivity.this,new ReConnectListener() {				
							@Override
							public void onReconnect() {
								if (!mLocationClient.isStarted()) {
									mLocationClient.start();
									mLocationClient.requestLocation();
								} else {
									loadNearGameList();
								}						
							}
						});
						log.e(content);
						super.onFailure(error, content);
					}
					/*@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveGameRankData(null, null,
								content);
						super.onSuccessForString(content);
					}*/
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameNearbyPlayActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onSuccessForList(List list) {
						log.d("onSuccessForList");
						if (null == list || list.size() == 0) {
							mPullListViewNear.loadComplete();
							refresh = false;
							return;
						}
                        if (refresh) {
                            nearGameList.clear();
                        	mPullListViewNear.onRefreshFinish();
                        }
                        nearGameList.addAll(list);
                        nearCurrentPageNum++;
						myGameNearAdapter.updateData(nearGameList, INDEX_NEAR);
                        myGameNearAdapter.notifyDataSetChanged();
                        mPullListViewNear.loadingFinish();
                        if (list.size() < Config.PAGE_SIZE) {
                        	mPullListViewNear.loadComplete();
                        }
                        refresh = false;
						super.onSuccessForList(list);
					}
				});
	}

	protected void initData() {
		startLocate();
		/*if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
			String nearGameData = SharedPreferenceUtil
					.getGameRankNearData();
			if (!TextUtils.isEmpty(nearGameData)) {
				String data = JsonUtils.getJsonValue(nearGameData,
							JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					nearGameList = (ArrayList<GameItem>) JsonUtils
								.json2List(data, GameItem.class);
					myGameNearAdapter
								.updateData(nearGameList, INDEX_NEAR);
					mPullListViewNear.loadingFinish();
					return;
				}
			}
		}
		if (nearGameList != null && nearGameList.size() > 0) {
			myGameNearAdapter.updateData(nearGameList, INDEX_NEAR);
			mPullListViewNear.loadingFinish();
			return;
		}
		log.d("loadNearGameList 1");*/
		///loadNearGameList();
	}

	protected  void initView() {
		if (!isInitData) {
			isInitData = true;
		}
		downloadDao = DownloadDao.getInstance(this);
		
		//txtEmpty = (TextView) findViewById(R.id.txt_empty);
		//txtEmpty.setText(R.string.nodata_friend_rankgame);
		mBackBtn=((ImageButton)findViewById(R.id.gamestore_sub_header_back));
		mBackBtn.setOnClickListener(this);
		mSearchBtn=((ImageButton)findViewById(R.id.gamestore_sub_header_search));
		mSearchBtn.setOnClickListener(this);
		mDownloadImg = (ImageButton) findViewById(R.id.gamestore_sub_header_download);
		mDownloadImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent();
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
						CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				mIntent.setClass(GameNearbyPlayActivity.this, DownloadActivity.class);
				startActivity(mIntent);
			}
		});
		mDownloadCountTV = (TextView) findViewById(R.id.gamestore_sub_header_download_count);
		
		mPullListViewNear = (PullToRefreshListView) findViewById(R.id.lstview_near);
		mPullListViewNear.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = null;
				GameItem item = null;
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEARPLAYING_GAMEDETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEARPLAYING_GAMEDETAIL_NAME);
				if (nearGameList == null || nearGameList.size() == 0
						|| position > nearGameList.size()) {
					return;
				}
				item = nearGameList.get(position-1);
				if (null != behaviorInfo) {
					CYSystemLogUtil.behaviorLog(behaviorInfo);
				}
				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(GameNearbyPlayActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivity(mIntent);
			}
		});
		mPullListViewNear.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			@Override
			public void onLoad() {
				log.d("loadNearGameList 2");
				loadNearGameList();
			}
		});
		
		mPullListViewNear.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {
				nearCurrentPageNum = 1;
				refresh = true;
		        if(Util.isNetworkConnected(GameNearbyPlayActivity.this)){
		        	loadNearGameList();
                }else{
                    Toast.makeText(GameNearbyPlayActivity.this, GameNearbyPlayActivity.this.getString(R.string.check_network), 
                    		Toast.LENGTH_SHORT).show();
                    mPullListViewNear.onRefreshFinish();
                    mPullListViewNear.loadComplete();
                    
                }
			}
		});

		if (nearGameList == null) {
			nearGameList = new ArrayList<GameItem>();
		}
		if (myGameNearAdapter == null) {
			myGameNearAdapter = new RankingGameAdapter(GameNearbyPlayActivity.this, nearGameList,
					INDEX_NEAR);
		}
		mPullListViewNear.setAdapter(myGameNearAdapter);
	}
	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gamestore_sub_header_back:
			finish();
			break;
		case R.id.gamestore_sub_header_search:
			Intent mIntent = new Intent();
			mIntent.setClass(GameNearbyPlayActivity.this, SearchActivity.class);
			startActivity(mIntent);
			break;

		}
	}
	
	private void registerDownloads() {
		updateDownloadCount();
		if (mDownloadReceiver == null) {
			mDownloadReceiver = new DownloadCountReceiver();
		}
		if (mDownloadReceiver != null && !mHadRegistDownloadReceiver) {
			registerReceiver(mDownloadReceiver, new IntentFilter(
					Contants.ACTION.DOWNLOADING_COUNT));
			mHadRegistDownloadReceiver = true;
		}
	}
	
	public void updateDownloadCount() {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.download_count);
		int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
		if (count > 0) {
			mDownloadCountTV.setVisibility(View.VISIBLE);
			if (count <= 99) {
				mDownloadCountTV.setText(String.valueOf(count));
			} else {
				mDownloadCountTV.setText("N");
			}
			mDownloadCountTV.startAnimation(anim);
		} else {
			mDownloadCountTV.setVisibility(View.GONE);
			mDownloadCountTV.setText("");
		}
	}
	
	private class DownloadCountReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(DownloadParam.STATE, -1);
			Animation anim = AnimationUtils.loadAnimation(context,
					R.anim.download_count);
			int count = DownloadDao.getInstance(GameNearbyPlayActivity.this).getDownloadingTaskSize();
			if (count > 0) {
				mDownloadCountTV.setVisibility(View.VISIBLE);
				if (count <= 99) {
					mDownloadCountTV.setText(String.valueOf(count));
				} else {
					mDownloadCountTV.setText("N");
				}
				//只有当添加了下载任务时，才播放动画
				if (status == DownloadParam.TASK.ADD ) {
	                mDownloadCountTV.startAnimation(anim);
	            }
			} else {
				mDownloadCountTV.setVisibility(View.GONE);
				mDownloadCountTV.setText("");
			}
		}
	}

	InstallAppReceiver installAppReceiver;
	UnInstallAppReceiver unstallAppReceiver;
	DownloadAppReceiver downloadAppReceiver;

	protected void onDestroy() {
		unRegistReceiver();
		if (mHadRegistDownloadReceiver && mDownloadReceiver != null) {
			unregisterReceiver(mDownloadReceiver);
		}
		super.onDestroy();
	}

	public void registReceiver() {
		try {
			if (installAppReceiver == null) {
				installAppReceiver = new InstallAppReceiver();
			}
			registerReceiver(installAppReceiver, new IntentFilter(
					Contants.ACTION.GAME_INSTALL));
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (unstallAppReceiver == null) {
				unstallAppReceiver = new UnInstallAppReceiver();
			}
			registerReceiver(unstallAppReceiver, new IntentFilter(
					Contants.ACTION.UNINSTALL));
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (downloadAppReceiver == null) {
				downloadAppReceiver = new DownloadAppReceiver();
			}
			registerReceiver(downloadAppReceiver, new IntentFilter(
					DownloadParam.UPDATE_PROGRESS_ACTION));
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void unRegistReceiver() {
		try {
			if (installAppReceiver != null) {
				unregisterReceiver(installAppReceiver);
				installAppReceiver = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (unstallAppReceiver != null) {
				unregisterReceiver(unstallAppReceiver);
				unstallAppReceiver = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (downloadAppReceiver != null) {
				unregisterReceiver(downloadAppReceiver);
				downloadAppReceiver = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 安装
	 * 
	 * @author wangkang
	 * 
	 */
	class InstallAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String packageName = intent
					.getStringExtra(DownloadParam.PACKAGE_NAME);
			updateListViewItem(getGameItemByPackage(packageName));
		}

	}

	/**
	 * 卸载
	 * 
	 * @author wangkang
	 * 
	 */
	class UnInstallAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String packageName = intent
					.getStringExtra(DownloadParam.PACKAGE_NAME);
			updateListViewItem(getGameItemByPackage(packageName));
		}

	}

	/**
	 * 下载过程中广播
	 * 
	 * @author wangkang
	 * 
	 */
	class DownloadAppReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (null == intent) {
				return;
			}
			int state = intent.getIntExtra(DownloadParam.STATE, 100);
			String packageName = intent
					.getStringExtra(DownloadParam.PACKAGE_NAME);
			switch (state) {
			case DownloadParam.TASK.DELETE:
				log.d("删除:"+packageName);
				updateListViewItem(getGameItemByPackage(packageName));
				break;
			case DownloadParam.TASK.DONE:
				updateListViewItem(getGameItemByPackage(packageName));
				break;
			case DownloadParam.TASK.ADD:
				updateListViewItem(getGameItemByPackage(packageName));
				break;
			case DownloadParam.TASK.CONTINUE:
				updateListViewItem(getGameItemByPackage(packageName));
				break;
			}
		}

	}

	private GameItem getGameItemByPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		GameItem mGameItem = null;
			if (null == nearGameList || nearGameList.isEmpty()) {
				return mGameItem;
			}
			for (GameItem item : nearGameList) {
				if (item == null || TextUtils.isEmpty(item.getIdentifier())) {
					continue;
				}
				if (packageName.equals(item.getIdentifier())) {
					mGameItem = item;
					break;
				}
			}
		return mGameItem;
	}

	private void updateListViewItem(GameItem item) {
		if (item == null) {
			return;
		}
		int firstVisiblePosition;
		int index;
		View view = null;

			if (nearGameList == null || nearGameList.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewNear.getFirstVisiblePosition();
			index = nearGameList.indexOf(item)+1;
			if (index < 0) {
				return;
			}
			view = mPullListViewNear.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewNear.getAdapter().getView(index, view,
						mPullListViewNear);
			}
	
		if (view != null) {
			RankingGameViewCache viewCache = (RankingGameViewCache) view
					.getTag();
			if(viewCache==null){
				log.e("视图为空!");
				return;
			}
			if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
				viewCache.getBtnDownloadGame().setBackgroundResource(
						R.drawable.play_btn_xbg);
				viewCache.getBtnDownloadGame().setText(R.string.game_play);
				viewCache.getBtnDownloadGame().setTextColor(
						getResources().getColor(R.color.white));
			} else {
				if (!downloadDao.isHasInfo(item.getIdentifier(),
						item.getVersion())) {
					viewCache.getBtnDownloadGame().setBackgroundResource(
							R.drawable.download_btn_xbg);
					viewCache.getBtnDownloadGame().setText(
							R.string.game_download);
					viewCache.getBtnDownloadGame().setTextColor(
							getResources().getColor(R.color.white));
					return;
				}
				DownloadItem downloadItem = downloadDao.getDowloadItem(
						item.getIdentifier(), item.getVersion());
				if (downloadItem == null
						|| TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
					viewCache.getBtnDownloadGame().setBackgroundResource(
							R.drawable.download_btn_xbg);
					viewCache.getBtnDownloadGame().setText(
							R.string.game_download);
					viewCache.getBtnDownloadGame().setTextColor(
							getResources().getColor(R.color.white));
				} else {
					if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
						viewCache.getBtnDownloadGame().setBackgroundResource(
								R.drawable.download_btn_xbg);
						viewCache.getBtnDownloadGame().setText(
								R.string.download_btn_install);
						viewCache.getBtnDownloadGame()
								.setTextColor(getResources().getColor(
												R.color.white));
					} else {
						if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
								|| downloadItem.getmState() == DownloadParam.C_STATE.WAITING
								|| downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
							viewCache.getBtnDownloadGame()
									.setBackgroundResource(
											R.drawable.downloading_btn_xbg);
							viewCache.getBtnDownloadGame().setText(
									R.string.game_downloading);
							viewCache.getBtnDownloadGame().setTextColor(
									getResources().getColor(
											R.color.downloading_text));
						} else {
							viewCache.getBtnDownloadGame()
									.setBackgroundResource(
											R.drawable.download_btn_xbg);
							viewCache.getBtnDownloadGame().setText(
									R.string.game_download);
							viewCache.getBtnDownloadGame().setTextColor(
									getResources().getColor(
											R.color.white));
						}
					}
				}

			}
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}


}
