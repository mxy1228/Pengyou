package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuessGameListAdapter;
import com.cyou.mrd.pengyou.adapter.RecomGameListAdapter;
import com.cyou.mrd.pengyou.adapter.RecomGameViewFlowAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.AdvertBean;
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
import com.cyou.mrd.pengyou.viewcache.RecomGameItemViewCache;
import com.cyou.mrd.pengyou.widget.EmptyView;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.RequestParams;

/**
 * 游戏库-推荐
 * 
 * @author wangkang
 * 
 */
public class RecommendGameFragment extends BaseFragment implements
		OnClickListener {
	private CYLog log = CYLog.getInstance();
	private Activity mActivity;
	private View contentView;
	PullToRefreshListView mPullListView;
	private View headerView;
	boolean isInitData = false;// 是否已经初始化
	private ArrayList<GameItem> gameList = new ArrayList<GameItem>();
	private RecomGameListAdapter myGameAdapter;
	private int currentPageNum = 1;
	private ArrayList<GameItem> guessGameLst = new ArrayList<GameItem>();
	private GridView gridViewGuessGame;
	private GuessGameListAdapter guessGameAdapter;
	private ViewFlow viewFlow;
	private RecomGameViewFlowAdapter classGameViewAdapter;
	private CircleFlowIndicator circleFlowIndicator;
	private ArrayList<AdvertBean> topRecomGameLst = new ArrayList<AdvertBean>();
	public static final String TOP_ADS_KEY = "adsvert_data";// 顶部广告
	public static final String GUESS_GAME_KEY = "guess_game_like";// 猜你喜欢
	public static final String RECOMMENT_GAME_KEY = "recomment_game";// 精品推荐
	int width;
	int height;
	EmptyView emptyView;
	// 猜你喜欢
	// private LinearLayout layoutFootBar;
	int lastCheckedPostion = 0;
	// private TextView txtGuessGameTitle;
	// private TextView txtGameFrdCount;
	// private Button btnGuessGameDetail;
	// private Button btnGuessGameDownload;
	private DownloadDao mDownloadDao;
	private ProgressBar mReflushPB;
	private ImageButton iBtnReflush;
	private boolean mLoading = false;
	private int mPageCount = 1;
	private DownloadDao downloadDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onResume() {
		if (null != viewFlow && topRecomGameLst != null
				&& topRecomGameLst.size() > 0) {
			viewFlow.stopAutoFlowTimer();
			viewFlow.startAutoFlowTimer();
		}
		super.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		if (!isInitData) {
			isInitData = true;
		}
		downloadDao = DownloadDao.getInstance(mContext);;
		this.mActivity = getActivity();
		contentView = inflater.from(mActivity).inflate(
				R.layout.recommend_game_layout, null);
		headerView = inflater.from(mActivity).inflate(
				R.layout.recom_game_header_view, null);
		initView();
		registReceiver();
		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		log.d("PersonalCenterFragment on start!");
		if(myGameAdapter!=null){
			myGameAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}

	/**
	 * 猜你喜欢
	 */
	private int mCurrentGuessPage = 1;

	private void loadGuessGameLst() {
//		if (!NetUtil.isNetworkAvailable()) {
//			String myGuessGameData = SharedPreferenceUtil
//					.getRecommendGuessData();
//			if (!TextUtils.isEmpty(myGuessGameData)) {
//				String data = JsonUtils.getJsonValue(myGuessGameData,
//						JsonUtils.DATA);
//				if (!TextUtils.isEmpty(data)) {
//					guessGameLst = (ArrayList<GameItem>) JsonUtils.json2List(
//							data, GameItem.class);
//					if (guessGameAdapter == null) {
//						guessGameAdapter = new GuessGameListAdapter(mActivity,
//								guessGameLst, gridViewGuessGame);
//						gridViewGuessGame.setAdapter(guessGameAdapter);
//					}
//					guessGameAdapter.updateData(guessGameLst);
//					guessGameAdapter.notifyDataSetChanged();
//					return;
//				}
//			}
//		}
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mCurrentGuessPage));
		params.put("count", "3");
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GUESS_GAMS,
				params,
				new JSONAsyncHttpResponseHandler<GameItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {

					@Override
					public void onStart() {
						mReflushPB.setVisibility(View.VISIBLE);
						iBtnReflush.setVisibility(View.GONE);
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								mActivity);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onFailure(Throwable error) {
						log.e(error);
						mReflushPB.setVisibility(View.GONE);
						iBtnReflush.setVisibility(View.VISIBLE);
						super.onFailure(error);
					}

					@Override
					public void onSuccessForString(String content) {
//						SharedPreferenceUtil.saveRecommendData(null, content,
//								null);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						guessGameLst = (ArrayList<GameItem>) list;
						if (guessGameAdapter == null) {
							guessGameAdapter = new GuessGameListAdapter(
									mActivity, guessGameLst, gridViewGuessGame);
							gridViewGuessGame.setAdapter(guessGameAdapter);
						}
						guessGameAdapter.updateData(guessGameLst);
						guessGameAdapter.notifyDataSetChanged();
						mCurrentGuessPage++;
						mReflushPB.setVisibility(View.GONE);
						iBtnReflush.setVisibility(View.VISIBLE);
						super.onSuccessForList(list);
					}
				});
	}

	/**
	 * 精品推荐
	 */
	private void loadRecomGameList() {
		if (mPullListView.getHeaderViewsCount() > 1) {
			mPullListView.removeHeaderView(emptyView);
		}
		gameList = new ArrayList<GameItem>();
		myGameAdapter = new RecomGameListAdapter(mActivity, gameList);
		mPullListView.setAdapter(myGameAdapter);
		mPullListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoad() {
				// TODO Auto-generated method stub
				requestRecomGame(mPageCount, false);
			}
		});
		mPullListView.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				requestRecomGame(1, true);
			}
		});

		if (!NetUtil.isNetworkAvailable()) {
			String recommendData = SharedPreferenceUtil.getRecommendData();
			if (!TextUtils.isEmpty(recommendData)) {
				String data = JsonUtils.getJsonValue(recommendData,
						JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					gameList = (ArrayList<GameItem>) JsonUtils.json2List(data,
							GameItem.class);
					myGameAdapter = new RecomGameListAdapter(mActivity,
							gameList);
					mPullListView.setAdapter(myGameAdapter);
					mPullListView.loadComplete();
					mPullListView.loadingFinish();

					return;
				}
			}
		}
		requestRecomGame(1, false);
	}

	private void requestRecomGame(int page, final boolean refresh) {
		log.d("requestRecomGame----" + "mLoading=" + mLoading);
		if (mLoading) {
			return;
		}
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mPageCount));
		params.put("count", String.valueOf(Config.PAGE_SIZE_RECOMMEND));
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.RECOM_GAME,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						log.d("requestRecomGame----" + "mLoading=onFailure");
						mPullListView.loadComplete();
						mLoading = false;
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
						log.d("requestRecomGame----"
								+ "mLoading=onSuccessForString");
//						SharedPreferenceUtil.saveRecommendData(null, null,
//								content);
						mLoading = true;
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (list != null) {
							if (!list.isEmpty()) {
								if (!refresh) {
									gameList.addAll(list);
									myGameAdapter.notifyDataSetChanged();
									if (list.size() < Config.PAGE_SIZE_RECOMMEND) {
										mPullListView.loadComplete();
									}
									mPageCount += 1;
									mPullListView.loadingFinish();
								} else {
									gameList.clear();
									gameList.addAll(list);
									myGameAdapter.notifyDataSetChanged();
									mPageCount = 2;
									mPullListView.onRefreshFinish();
								}

							} else {
								mPullListView.loadComplete();
							}
						} else {
							log.e("list is null");
							mPullListView.loadComplete();
						}
						mPullListView.loadingFinish();
						mLoading = false;
						super.onSuccessForList(list);
					}
				});
	}

	private void initData() {
		this.mDownloadDao = DownloadDao.getInstance(mContext);;
		loadTopRecomGameLst();
		if (guessGameLst == null) {
			guessGameLst = new ArrayList<GameItem>();
		}
		if (guessGameAdapter == null) {
			guessGameAdapter = new GuessGameListAdapter(mActivity,
					guessGameLst, gridViewGuessGame);
		}
		gridViewGuessGame.setAdapter(guessGameAdapter);
		loadGuessGameLst();
		loadRecomGameList();
	}

	private void loadTopRecomGameLst() {
		if (null == topRecomGameLst || topRecomGameLst.size() == 0) {
			topRecomGameLst = new ArrayList<AdvertBean>();
		}
		if (classGameViewAdapter == null) {
//			classGameViewAdapter = new RecomGameViewFlowAdapter(mActivity,
//					topRecomGameLst,0);
		}
		if (!NetUtil.isNetworkAvailable()) {
			String topAdsData = SharedPreferenceUtil.getRecommendTopData();
			if (!TextUtils.isEmpty(topAdsData)) {
				String data = JsonUtils
						.getJsonValue(topAdsData, JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					topRecomGameLst = (ArrayList<AdvertBean>) JsonUtils
							.json2List(data, AdvertBean.class);
					if (topRecomGameLst == null || topRecomGameLst.size() == 0) {
						return;
					}
					classGameViewAdapter.updateListView(topRecomGameLst);
					viewFlow.setAdapter(classGameViewAdapter);
					viewFlow.setmSideBuffer(topRecomGameLst.size());
					circleFlowIndicator.setVisibility(View.VISIBLE);
					circleFlowIndicator.setPadding(0, 0,0,5);
					viewFlow.setFlowIndicator(circleFlowIndicator);
					viewFlow.setSelection(0);
					viewFlow.startAutoFlowTimer();
					return;
				}
			}
		}
		RequestParams params = new RequestParams();
		params.put("imgwidth", String.valueOf(width));
		params.put("imgheight", String.valueOf(height));
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GAME_STORE_TOPADS,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						AdvertBean.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
//						SharedPreferenceUtil.saveRecommendData(content, null,
//								null);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						topRecomGameLst = (ArrayList<AdvertBean>) list;
						if (topRecomGameLst == null
								|| topRecomGameLst.size() == 0) {
							return;
						}
						classGameViewAdapter.updateListView(topRecomGameLst);
						viewFlow.setAdapter(classGameViewAdapter);
						viewFlow.setmSideBuffer(topRecomGameLst.size());
						circleFlowIndicator.setVisibility(View.VISIBLE);
						circleFlowIndicator.setPadding(0, 0,0,5);
						viewFlow.setFlowIndicator(circleFlowIndicator);
						viewFlow.setSelection(0);
						viewFlow.startAutoFlowTimer();
						super.onSuccessForList(list);
					}
				});
	}

	private void initView() {
		if (null == contentView) {
			return;
		}
		emptyView = new EmptyView(mActivity);
		emptyView.setText(getString(R.string.no_data));
		emptyView.setImageGone();
		viewFlow = (ViewFlow) headerView.findViewById(R.id.viewflow_recom_game);
		viewFlow.setIsTouch(true);
		circleFlowIndicator = (CircleFlowIndicator) headerView
				.findViewById(R.id.indicator_recom_game);
		mPullListView = (PullToRefreshListView) contentView
				.findViewById(R.id.mygame_lstview);
		mPullListView.addHeaderView(headerView);
		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GAME_DETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GAME_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (gameList.size() > position - 1) {
					GameItem item = gameList.get(position - 1);
					Intent mIntent = new Intent();
					mIntent.setClass(mActivity, GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					startActivity(mIntent);
				}
			}
		});
		gridViewGuessGame = (GridView) headerView
				.findViewById(R.id.grid_guess_game);
		iBtnReflush = (ImageButton) headerView
				.findViewById(R.id.btn_reflush_game);
		this.mReflushPB = (ProgressBar) headerView
				.findViewById(R.id.regame_reflush_pb);
		gridViewGuessGame.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridViewGuessGame.setCacheColorHint(Color.TRANSPARENT);
		gridViewGuessGame.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_ICON_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_ICON_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (guessGameLst == null || guessGameLst.size() == 0
						|| position > guessGameLst.size()) {
					return;
				}
				final GameItem item = guessGameLst.get(position);
				if (item != null) {
					Intent mIntent = new Intent();
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.setClass(mActivity, GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					startActivity(mIntent);
				}

			}
		});
		iBtnReflush.setOnClickListener(this);
		width = Util.getScreenWidthSize(mActivity);
		height = (int) (Math
				.ceil(Util.getScreenWidthSize(mActivity) * 100 / 610) / 100 * 160);
		if (null != viewFlow && topRecomGameLst != null
				&& topRecomGameLst.size() > 0) {
			viewFlow.stopAutoFlowTimer();
			viewFlow.startAutoFlowTimer();
		}
		viewFlow.setLayoutParams(new LayoutParams(width, height));
		initData();
	}

	public RecommendGameFragment() {
		super();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_reflush_game:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_REFRESH_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_REFRESH_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			// layoutFootBar.setVisibility(View.GONE);
			if (guessGameLst != null) {
				guessGameLst.clear();
			}
			loadGuessGameLst();
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

	private GameItem getGameItemByPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		GameItem mGameItem = null;
		if (null == gameList || gameList.isEmpty()) {
			return mGameItem;
		}
		for (GameItem item : gameList) {
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
				log.d("删除:" + packageName);
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

	private void updateListViewItem(GameItem item) {
		if (item == null) {
			return;
		}
		int firstVisiblePosition;
		int index;
		View view = null;

		if (gameList == null || gameList.isEmpty()) {
			return;
		}
		firstVisiblePosition = mPullListView.getFirstVisiblePosition();
		index = gameList.indexOf(item);
		if (index < 0) {
			return;
		}
		view = mPullListView.getChildAt(index - firstVisiblePosition+1);
		if (null == view) {
			mPullListView.getAdapter().getView(index+1, view, mPullListView);
		}
		if (view != null) {
			RecomGameItemViewCache viewCache = (RecomGameItemViewCache) view
					.getTag();
			if (viewCache == null) {
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
