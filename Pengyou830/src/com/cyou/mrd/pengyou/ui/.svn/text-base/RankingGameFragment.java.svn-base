package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;

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
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RankingGameViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.RequestParams;

public class RankingGameFragment extends BaseFragment implements
		OnCheckedChangeListener {
	public static final int INDEX_FRIEND = 0;// 朋友榜
	public static final int INDEX_WORLD = 1;// 世界榜
	public static final int INDEX_NEAR = 2;// 附近榜
	private CYLog log = CYLog.getInstance();
	private Activity mActivity;
	private View contentView;
	PullToRefreshListView mPullListViewFriend;
	PullToRefreshListView mPullListViewWorld;
	PullToRefreshListView mPullListViewNear;
	boolean isInitData = false;// 是否已经初始化
	private List<GameItem> friendGameList = new ArrayList<GameItem>();
	private List<GameItem> worldGameList = new ArrayList<GameItem>();
	private List<GameItem> nearGameList = new ArrayList<GameItem>();
	private RankingGameAdapter myGameFriendAdapter;
	private RankingGameAdapter myGameWorldAdapter;
	private RankingGameAdapter myGameNearAdapter;
	private int friendCurrentPageNum = 1;
	private int worldCurrentPageNum = 1;
	private int nearCurrentPageNum = 1;
	private int currentIndex = INDEX_FRIEND;
	private RadioGroup mRG;
	private RadioButton btnFriendGame;
	private RadioButton btnWorldGame;
	private RadioButton btnNearGame;
	private LocationClient mLocationClient;
	private double mLatitude;
	private double mLongitude;
	private View headerView;
	private ViewFlipper viewFlipper;
	private TextView txtEmpty;
	private DownloadDao downloadDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		if (!isInitData) {
			isInitData = true;
		}
		downloadDao = DownloadDao.getInstance(mContext);;
		this.mActivity = getActivity();
		contentView = inflater.from(mActivity).inflate(
				R.layout.gamestore_ranking, null);
		initView();
		registReceiver();
		return contentView;
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
			log.d("onReceiveLocation");
			if (arg0 == null) {
				return;
			}
			mLatitude = arg0.getLatitude();
			mLongitude = arg0.getLongitude();
		}

		@Override
		public void onReceivePoi(BDLocation arg0) {
			log.d("onReceivePoi");

		}

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		log.d("PersonalCenterFragment on start!");
		if(null!=myGameFriendAdapter){
			myGameFriendAdapter.notifyDataSetChanged();
		}
		if(null!=myGameNearAdapter){
			myGameNearAdapter.notifyDataSetChanged();
		}
		if(null!=myGameWorldAdapter){
			myGameWorldAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}

	/**
	 * 加载我的游戏
	 */
	private void loadFriendGameList() {
		mPullListViewFriend.setVisibility(View.VISIBLE);
		txtEmpty.setVisibility(View.GONE);
		RequestParams params = new RequestParams();
		String url = "";
		params.put("page", String.valueOf(friendCurrentPageNum));
		url = HttpContants.NET.GAME_RANK_FRINED;
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		MyHttpConnect.getInstance().post2Json(
				url,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						mPullListViewFriend.loadComplete();
						mPullListViewFriend.loadingFinish();
						log.e(content);
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveGameRankData(content, null,
								null);
						super.onSuccessForString(content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								mActivity);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							mPullListViewFriend.loadComplete();
							if (friendGameList == null
									|| friendGameList.size() == 0) {
								mPullListViewFriend.setVisibility(View.GONE);
								txtEmpty.setVisibility(View.VISIBLE);
							}
							return;
						}
						if (list.size() < Config.PAGE_SIZE) {
							mPullListViewFriend.loadComplete();
						}
						friendGameList.addAll(list);
						friendCurrentPageNum++;
						myGameFriendAdapter.updateData(friendGameList,
								currentIndex);
						mPullListViewFriend.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}

	/**
	 * 加载世界榜
	 */
	private void loadWorldGameList() {
		RequestParams params = new RequestParams();
		String url = "";
		url = HttpContants.NET.GAME_RANK_WORLD;
		params.put("page", String.valueOf(worldCurrentPageNum));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		MyHttpConnect.getInstance().post2Json(
				url,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						mPullListViewWorld.loadComplete();
						mPullListViewWorld.loadingFinish();
						log.e(content);
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveGameRankData(null, content,
								null);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							mPullListViewWorld.loadComplete();
							return;
						}
						if (list.size() < Config.PAGE_SIZE) {
							mPullListViewWorld.loadComplete();
						}
						worldGameList.addAll(list);
						worldCurrentPageNum++;
						myGameWorldAdapter.updateData(worldGameList,
								currentIndex);
						mPullListViewWorld.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}

	/**
	 * 加载我的游戏
	 */
	private void loadNearGameList() {
		RequestParams params = new RequestParams();
		String url = "";
		url = HttpContants.NET.GAME_RANK_NEAR;
		params.put("page", String.valueOf(nearCurrentPageNum));
		params.put("latitude", String.valueOf(mLatitude));
		params.put("longitude", String.valueOf(mLongitude));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
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
						log.e(content);
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveGameRankData(null, null,
								content);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							mPullListViewNear.loadComplete();
							return;
						}
						if (list.size() < Config.PAGE_SIZE) {
							mPullListViewNear.loadComplete();
						}
						nearGameList.addAll(list);
						myGameNearAdapter
								.updateData(nearGameList, currentIndex);
						nearCurrentPageNum++;
						mPullListViewNear.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}

	private void initData() {
		startLocate();
		switch (currentIndex) {
		case INDEX_FRIEND:
			viewFlipper.setDisplayedChild(0);
			if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
				String friendGameData = SharedPreferenceUtil
						.getGameRankFriendData();
				if (!TextUtils.isEmpty(friendGameData)) {
					String data = JsonUtils.getJsonValue(friendGameData,
							JsonUtils.DATA);
					if (!TextUtils.isEmpty(data)) {
						friendGameList = (ArrayList<GameItem>) JsonUtils
								.json2List(data, GameItem.class);
						myGameFriendAdapter.updateData(friendGameList,
								currentIndex);
						mPullListViewFriend.loadingFinish();
						return;
					} else {
						mPullListViewFriend.setVisibility(View.GONE);
						txtEmpty.setVisibility(View.VISIBLE);
					}
				} else {
					mPullListViewFriend.setVisibility(View.GONE);
					txtEmpty.setVisibility(View.VISIBLE);
				}
			}
			if (friendGameList != null && friendGameList.size() > 0) {
				myGameFriendAdapter.updateData(friendGameList, currentIndex);
				mPullListViewFriend.loadingFinish();
				return;
			}
			loadFriendGameList();
			break;
		case INDEX_WORLD:
			viewFlipper.setDisplayedChild(1);
			if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
				String worldGameData = SharedPreferenceUtil
						.getGameRankWorldData();
				if (!TextUtils.isEmpty(worldGameData)) {
					String data = JsonUtils.getJsonValue(worldGameData,
							JsonUtils.DATA);
					if (!TextUtils.isEmpty(data)) {
						worldGameList = (ArrayList<GameItem>) JsonUtils
								.json2List(data, GameItem.class);
						myGameWorldAdapter.updateData(worldGameList,
								currentIndex);
						mPullListViewWorld.loadingFinish();
						return;
					}
				}
			}
			if (worldGameList != null && worldGameList.size() > 0) {
				myGameWorldAdapter.updateData(worldGameList, currentIndex);
				mPullListViewWorld.loadingFinish();
				return;
			}
			loadWorldGameList();
			break;
		case INDEX_NEAR:
			viewFlipper.setDisplayedChild(2);
			if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
				String nearGameData = SharedPreferenceUtil
						.getGameRankNearData();
				if (!TextUtils.isEmpty(nearGameData)) {
					String data = JsonUtils.getJsonValue(nearGameData,
							JsonUtils.DATA);
					if (!TextUtils.isEmpty(data)) {
						nearGameList = (ArrayList<GameItem>) JsonUtils
								.json2List(data, GameItem.class);
						myGameNearAdapter
								.updateData(nearGameList, currentIndex);
						mPullListViewNear.loadingFinish();
						return;
					}
				}
			}
			if (nearGameList != null && nearGameList.size() > 0) {
				myGameNearAdapter.updateData(nearGameList, currentIndex);
				mPullListViewNear.loadingFinish();
				return;
			}
			loadNearGameList();

			break;
		}

	}

	private void initView() {
		if (null == contentView) {
			return;
		}
		txtEmpty = (TextView) contentView.findViewById(R.id.txt_empty);
		txtEmpty.setText(R.string.nodata_friend_rankgame);
		headerView = contentView.findViewById(R.id.layout_header);
		viewFlipper = (ViewFlipper) contentView.findViewById(R.id.viewFliper);
		mPullListViewFriend = (PullToRefreshListView) contentView
				.findViewById(R.id.lstview_friend);
		mPullListViewFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = null;
				GameItem item = null;
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_FRIEND_DETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_FRIEND_DETAIL_NAME);
				if (friendGameList == null || friendGameList.size() == 0
						|| position > friendGameList.size()) {
					return;
				}
				item = friendGameList.get(position);
				if (null != behaviorInfo) {
					CYSystemLogUtil.behaviorLog(behaviorInfo);
				}
				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(mActivity, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
				startActivity(mIntent);
			}
		});
		mPullListViewFriend.setOnLoadListener(new LoadListener() {

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
				loadFriendGameList();
			}
		});
		btnFriendGame = (RadioButton) headerView
				.findViewById(R.id.rbtn_rank_friend);
		btnWorldGame = (RadioButton) headerView
				.findViewById(R.id.rbtn_rank_world);
		btnNearGame = (RadioButton) headerView
				.findViewById(R.id.rbtn_rank_near);
		this.mRG = (RadioGroup) headerView.findViewById(R.id.rank_game_rg);
		resetRadioBtn(0);
		this.mRG.setOnCheckedChangeListener(this);
		mPullListViewWorld = (PullToRefreshListView) contentView
				.findViewById(R.id.lstview_world);
		mPullListViewWorld.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = null;
				GameItem item = null;
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_WORLD_DETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_WORLD_DETAIL_NAME);
				if (worldGameList == null || worldGameList.size() == 0
						|| position > worldGameList.size()) {
					return;
				}
				item = worldGameList.get(position);
				if (null != behaviorInfo) {
					CYSystemLogUtil.behaviorLog(behaviorInfo);
				}
				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(mActivity, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
				startActivity(mIntent);
			}
		});
		mPullListViewWorld.setOnLoadListener(new LoadListener() {

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
				loadWorldGameList();
			}
		});
		mPullListViewNear = (PullToRefreshListView) contentView
				.findViewById(R.id.lstview_near);
		mPullListViewNear.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = null;
				GameItem item = null;
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_NEAR_DETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_NEAR_DETAIL_NAME);
				if (nearGameList == null || nearGameList.size() == 0
						|| position > nearGameList.size()) {
					return;
				}
				item = nearGameList.get(position);
				if (null != behaviorInfo) {
					CYSystemLogUtil.behaviorLog(behaviorInfo);
				}
				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(mActivity, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
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
				loadNearGameList();
			}
		});

		if (null == friendGameList) {
			friendGameList = new ArrayList<GameItem>();
		}
		if (myGameFriendAdapter == null) {
			myGameFriendAdapter = new RankingGameAdapter(mActivity,
					friendGameList, currentIndex);
		}
		mPullListViewFriend.setAdapter(myGameFriendAdapter);

		if (null == worldGameList) {
			worldGameList = new ArrayList<GameItem>();
		}
		if (null == myGameWorldAdapter) {
			myGameWorldAdapter = new RankingGameAdapter(mActivity,
					worldGameList, currentIndex);
		}
		mPullListViewWorld.setAdapter(myGameWorldAdapter);

		if (nearGameList == null) {
			nearGameList = new ArrayList<GameItem>();
		}
		if (myGameNearAdapter == null) {
			myGameNearAdapter = new RankingGameAdapter(mActivity, nearGameList,
					currentIndex);
		}
		mPullListViewNear.setAdapter(myGameNearAdapter);
		initData();
	}

	private void resetRadioBtn(final int index) {
		switch (index) {
		case 0:
			currentIndex = INDEX_FRIEND;
			btnFriendGame.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.you_may_known_pic_select), null, null);
			btnNearGame.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.btn_rank_near), null,
					null);
			btnWorldGame.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.btn_rank_world),
					null, null);
			btnFriendGame.setBackgroundResource(R.drawable.img_rank_clicked_bg);
			btnNearGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));
			btnWorldGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));

			btnFriendGame.setTextColor(getResources().getColor(
					R.color.tab_text_select));
			btnNearGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));
			btnWorldGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));

			break;
		case 1:
			currentIndex = INDEX_WORLD;
			btnFriendGame.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.you_may_known_pic_normal), null, null);
			btnNearGame.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.btn_rank_near), null,
					null);
			btnWorldGame.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.btn_rank_world_clicked), null, null);

			btnWorldGame.setBackgroundResource(R.drawable.img_rank_clicked_bg);
			btnNearGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));
			btnFriendGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));

			btnNearGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));
			btnFriendGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));
			btnWorldGame.setTextColor(getResources().getColor(
					R.color.tab_text_select));
			break;
		case 2:
			currentIndex = INDEX_NEAR;
			btnFriendGame.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.you_may_known_pic_normal), null, null);
			btnNearGame.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources()
							.getDrawable(R.drawable.btn_rank_near_clicked),
					null, null);
			btnWorldGame.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.btn_rank_world),
					null, null);
			btnNearGame.setBackgroundResource(R.drawable.img_rank_clicked_bg);
			btnWorldGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));
			btnFriendGame.setBackgroundColor(getResources().getColor(
					R.color.rank_title_default));
			btnWorldGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));
			btnFriendGame.setTextColor(getResources().getColor(
					R.color.tab_text_normal));
			btnNearGame.setTextColor(getResources().getColor(
					R.color.tab_text_select));
			break;
		}
	}

	public RankingGameFragment() {
		super();
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rbtn_rank_friend:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_FRIEND_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_FRIEND_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			currentIndex = INDEX_FRIEND;
			initData();
			resetRadioBtn(0);
			break;
		case R.id.rbtn_rank_world:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_WORLD_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_WORLD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			currentIndex = INDEX_WORLD;
			initData();
			resetRadioBtn(1);
			break;
		case R.id.rbtn_rank_near:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_NEAR_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_RANK_NEAR_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			currentIndex = INDEX_NEAR;
			initData();
			resetRadioBtn(2);
			break;
		default:
			break;
		}
	}

	InstallAppReceiver installAppReceiver;
	UnInstallAppReceiver unstallAppReceiver;
	DownloadAppReceiver downloadAppReceiver;

	@Override
	public void onDestroyView() {
		unRegistReceiver();
		super.onDestroyView();
	}

	public void registReceiver() {
		try {
			if (installAppReceiver == null) {
				installAppReceiver = new InstallAppReceiver();
			}
			mContext.registerReceiver(installAppReceiver, new IntentFilter(
					Contants.ACTION.GAME_INSTALL));
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (unstallAppReceiver == null) {
				unstallAppReceiver = new UnInstallAppReceiver();
			}
			mContext.registerReceiver(unstallAppReceiver, new IntentFilter(
					Contants.ACTION.UNINSTALL));
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (downloadAppReceiver == null) {
				downloadAppReceiver = new DownloadAppReceiver();
			}
			mContext.registerReceiver(downloadAppReceiver, new IntentFilter(
					DownloadParam.UPDATE_PROGRESS_ACTION));
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void unRegistReceiver() {
		try {
			if (installAppReceiver != null) {
				mContext.unregisterReceiver(installAppReceiver);
				installAppReceiver = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (unstallAppReceiver != null) {
				mContext.unregisterReceiver(unstallAppReceiver);
				unstallAppReceiver = null;
			}
		} catch (Exception e) {
			log.e(e);
		}
		try {
			if (downloadAppReceiver != null) {
				mContext.unregisterReceiver(downloadAppReceiver);
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
		switch (currentIndex) {
		case INDEX_FRIEND:
			if (null == friendGameList || friendGameList.isEmpty()) {
				return mGameItem;
			}
			for (GameItem item : friendGameList) {
				if (item == null || TextUtils.isEmpty(item.getIdentifier())) {
					continue;
				}
				if (packageName.equals(item.getIdentifier())) {
					mGameItem = item;
					break;
				}
			}
			break;
		case INDEX_NEAR:
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
			break;
		case INDEX_WORLD:
			if (null == worldGameList || worldGameList.isEmpty()) {
				return mGameItem;
			}
			for (GameItem item : worldGameList) {
				if (item == null || TextUtils.isEmpty(item.getIdentifier())) {
					continue;
				}
				if (packageName.equals(item.getIdentifier())) {
					mGameItem = item;
					break;
				}
			}
			break;
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
		switch (currentIndex) {
		case INDEX_FRIEND:
			if (friendGameList == null || friendGameList.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewFriend
					.getFirstVisiblePosition();
			index = friendGameList.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewFriend.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewFriend.getAdapter().getView(index, view,
						mPullListViewFriend);
			}
			break;
		case INDEX_WORLD:
			if (worldGameList == null || worldGameList.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewWorld.getFirstVisiblePosition();
			index = worldGameList.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewWorld.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewWorld.getAdapter().getView(index, view,
						mPullListViewWorld);
			}
			break;
		case INDEX_NEAR:
			if (nearGameList == null || nearGameList.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewNear.getFirstVisiblePosition();
			index = nearGameList.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewNear.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewNear.getAdapter().getView(index, view,
						mPullListViewNear);
			}
			break;
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
						mContext.getResources().getColor(R.color.white));
			} else {
				if (!downloadDao.isHasInfo(item.getIdentifier(),
						item.getVersion())) {
					viewCache.getBtnDownloadGame().setBackgroundResource(
							R.drawable.download_btn_xbg);
					viewCache.getBtnDownloadGame().setText(
							R.string.game_download);
					viewCache.getBtnDownloadGame().setTextColor(
							mContext.getResources().getColor(R.color.white));
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
							mContext.getResources().getColor(R.color.white));
				} else {
					if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
						viewCache.getBtnDownloadGame().setBackgroundResource(
								R.drawable.download_btn_xbg);
						viewCache.getBtnDownloadGame().setText(
								R.string.download_btn_install);
						viewCache.getBtnDownloadGame()
								.setTextColor(
										mContext.getResources().getColor(
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
									mContext.getResources().getColor(
											R.color.downloading_text));
						} else {
							viewCache.getBtnDownloadGame()
									.setBackgroundResource(
											R.drawable.download_btn_xbg);
							viewCache.getBtnDownloadGame().setText(
									R.string.game_download);
							viewCache.getBtnDownloadGame().setTextColor(
									mContext.getResources().getColor(
											R.color.white));
						}
					}
				}

			}
		}
	}
}
