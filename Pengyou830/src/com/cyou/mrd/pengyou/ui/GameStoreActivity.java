package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.taptwo.android.widget.CircleFlowIndicator;
import org.taptwo.android.widget.ViewFlow;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameStoreHotListAdapter;
import com.cyou.mrd.pengyou.adapter.GameStoreNewListAdapter;
import com.cyou.mrd.pengyou.adapter.GameStoreRecomListAdapter;
import com.cyou.mrd.pengyou.adapter.RecomGameViewFlowAdapter;
import com.cyou.mrd.pengyou.adapter.SpecialGameViewFlowAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.AdvertBean;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.SeminarBean;
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
import com.cyou.mrd.pengyou.widget.GameGridView;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.OrderView;
import com.cyou.mrd.pengyou.widget.OrderView.StayViewListener;
import com.loopj.android.http.RequestParams;

public class GameStoreActivity extends BaseActivity implements OnClickListener {
	private RecomGameViewFlowAdapter classGameViewAdapter;
	private CircleFlowIndicator circleFlowIndicator;
	private ArrayList<AdvertBean> topRecomGameLst = new ArrayList<AdvertBean>();
	private ViewFlow viewFlow;
	private RelativeLayout relaytiveLayoutTop;
	int width;
	int topAdsHeight;
	int seminarHeight;
	private SpecialGameViewFlowAdapter gameSeminarViewAdapter;
	private CircleFlowIndicator gameSeminarFlowIndicator;
	private ArrayList<SeminarBean> seminarGameLst = new ArrayList<SeminarBean>();
	private ViewFlow gameSeminarViewFlow;

	private GameGridView gridViewRecommendGame;
	private GameStoreRecomListAdapter recommendGameAdapter;
	private List<GameItem> recommendGameLst = new ArrayList<GameItem>();

	private GameGridView gridViewHotGame;
	private GameStoreHotListAdapter hotGameAdapter;
	private List<GameItem> hotGameLst = new ArrayList<GameItem>();

	private GameGridView gridViewNewGame;
	private GameStoreNewListAdapter newGameAdapter;
	private List<GameItem> newGameLst = new ArrayList<GameItem>();

	private TextView txtFriendPlay;
	private TextView txtNearPlay;
	private TextView txtClassify;
	private TextView txtYouMayLike;

	private TextView txtFriendPlayStatic;
	private TextView txtNearPlayStatic;
	private TextView txtClassifyStatic;
	private TextView txtYouMayLikeStatic;

	private RelativeLayout btnMoreRecommend;
	private Button btnMoreSeminar;// 更多专题

	private ImageButton mSearchIBtn;
	private ImageButton mDownloadIBtn;
	private TextView mDownloadCountTV;
	private DownloadCountReceiver mDownloadReceiver;
	private TextView txtDesc;
	private CYLog log = CYLog.getInstance();
	private TextView txtSeminarInfo;
	private RelativeLayout rlytSeminar;
	private RelativeLayout rlytTop;
	private OrderView refreshableView;
	private ScrollView scrollView;
	private RelativeLayout ryoutHeader;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.game_store_layout);
		CyouApplication.setGameStoreAct(this);
		initView();
		initData(false);
		registReceiver();
	}

	public void initData(boolean isShowDialog) {
		try {
			if (isShowDialog) {
				// showLoadListProgressDialog();
			}
			loadTopRecomGameLst();
			loadRecommendGameLst();
			loadSeminarGameLst();
			loadHotGameLst();
			loadNewGameLst();
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	public void onResume() {
		if (null != viewFlow && topRecomGameLst != null
				&& topRecomGameLst.size() > 0) {
			viewFlow.stopAutoFlowTimer();
			viewFlow.startAutoFlowTimer();
		}
		if (null != gameSeminarViewFlow && seminarGameLst != null
				&& seminarGameLst.size() > 0) {
//			gameSeminarViewFlow.stopAutoFlowTimer();
//			gameSeminarViewFlow.startAutoFlowTimer();
		}
		updateDownloadCount(-1);
		try {
			if (scrollView != null) {
				scrollView.smoothScrollTo(0, scrollView.getScrollY());
			}
		} catch (Exception e) {
			log.e(e);
		}
		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
		if (CyouApplication.getMainActiv() != null) {
			CyouApplication.getMainActiv().hideGameStoreDotImage();
//			SharedPreferenceUtil.setGamestoreDataTime();
		}
		super.onResume();
	}

	@Override
	protected void onRestart() {
		if (CyouApplication.returnHomeIndex != Contants.RETURNHOME.RETURN_HOME) {// 通过back切换home
			CyouApplication.returnHomeIndex = Contants.RETURNHOME.RETURN_HOME;
			if (CyouApplication.getMainActiv() != null) {
				CyouApplication.getMainActiv().initTabIndex();
			}
		}
		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
//		if (CyouApplication.getMainActiv() != null) {
//			CyouApplication.getMainActiv().hideGameStoreDotImage();
//			SharedPreferenceUtil.setGamestoreDataTime();
//		}
		super.onRestart();
	}

	private void destoryRes() {
		classGameViewAdapter = null;
		circleFlowIndicator = null;
		topRecomGameLst = null;
		viewFlow = null;
		gameSeminarViewAdapter = null;
		gameSeminarFlowIndicator = null;
		seminarGameLst = null;
		gameSeminarViewFlow = null;
		gridViewRecommendGame = null;
		recommendGameAdapter = null;
		recommendGameLst = null;
		gridViewHotGame = null;
		hotGameAdapter = null;
		hotGameLst = null;
		gridViewNewGame = null;
		newGameAdapter = null;
		newGameLst = null;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		destoryRes();
		unregistReceiver();
	}

	/**
	 * 精品推荐
	 */

	private void loadRecommendGameLst() {
		if (!NetUtil.isNetworkAvailable()) {
			String recommendGameData = SharedPreferenceUtil.getRecommendData();
			if (!TextUtils.isEmpty(recommendGameData)) {
				String data = JsonUtils.getJsonValue(recommendGameData,
						JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					recommendGameLst = (ArrayList<GameItem>) JsonUtils
							.json2List(data, GameItem.class);
					recommendGameAdapter = new GameStoreRecomListAdapter(
							GameStoreActivity.this, recommendGameLst,
							gridViewRecommendGame);
					gridViewRecommendGame.setAdapter(recommendGameAdapter);
					return;
				}
			}
		}
		if (recommendGameLst == null) {
			recommendGameLst = new ArrayList<GameItem>();
		}
		recommendGameLst.clear();
		recommendGameLst.add(new GameItem());
		recommendGameLst.add(new GameItem());
		recommendGameLst.add(new GameItem());
		recommendGameLst.add(new GameItem());
		recommendGameLst.add(new GameItem());
		recommendGameLst.add(new GameItem());
		if (recommendGameAdapter == null) {
			recommendGameAdapter = new GameStoreRecomListAdapter(
					GameStoreActivity.this, recommendGameLst,
					gridViewRecommendGame);
		} else {
			recommendGameAdapter.updateData(recommendGameLst);
		}
		gridViewRecommendGame.setAdapter(recommendGameAdapter);

		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(1));
		params.put("count", "6");
		params.put("withpic", "1");
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.RECOM_GAME,
				params,
				new JSONAsyncHttpResponseHandler<GameItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameStoreActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}

					@Override
					public void onSuccessForString(String content) {
						log.d("精品推荐缓存:" + content);
						SharedPreferenceUtil.saveRecommendData(null, null,
								content, null, null);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							return;
						}
						try {
							if (list.size() < 6) {
								recommendGameLst = list.subList(0, list.size());
								int size = 6 - list.size();
								for (int i = 0; i < size; i++) {
									if (i == size) {
										return;
									}
									recommendGameLst.add(new GameItem());
								}
							} else {
								recommendGameLst = list.subList(0, 6);
							}
							recommendGameAdapter = new GameStoreRecomListAdapter(
									GameStoreActivity.this, recommendGameLst,
									gridViewRecommendGame);
							gridViewRecommendGame
									.setAdapter(recommendGameAdapter);
						} catch (Exception e) {
							log.e(e);
						}
						super.onSuccessForList(list);
					}
				});
	}

	private void registReceiver() {
		if (mDownloadReceiver == null) {
			mDownloadReceiver = new DownloadCountReceiver();
		}
		registerReceiver(mDownloadReceiver, new IntentFilter(
				Contants.ACTION.DOWNLOADING_COUNT));
	}

	private void unregistReceiver() {
		if (mDownloadReceiver != null) {
			unregisterReceiver(mDownloadReceiver);
		}
	}

//	public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
//		if (keyCode == KeyEvent.KEYCODE_BACK) {
//			CyouApplication.returnHomeIndex = Contants.RETURNHOME.RETURN_HOME_SEARCHGAME;
//			Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
//			mHomeIntent.addCategory(Intent.CATEGORY_HOME);
//			startActivity(mHomeIntent);
//			MainActivity.mFirstResume = true;
//			return true;
//		}
//		return false;
//	}

	private class DownloadCountReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(DownloadParam.STATE, -1);
			updateDownloadCount(status);
		}

	}

	/**
	 * 
	 * tuozhonghua_zk 2013-9-25下午4:46:10
	 * 
	 * @param status
	 */
	private void updateDownloadCount(int status) {
		Animation anim = AnimationUtils.loadAnimation(this,
				R.anim.download_count);
		int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
		log.d("MainActivity: game Count is:" + count);
		if (count > 0) {
			mDownloadCountTV.setVisibility(View.VISIBLE);
			if (count <= 99) {
				mDownloadCountTV.setText(String.valueOf(count));
			} else {
				mDownloadCountTV.setText("N");
			}
			if (status == DownloadParam.TASK.ADD) {
				mDownloadCountTV.startAnimation(anim);
			}
		} else {
			mDownloadCountTV.setVisibility(View.GONE);
			mDownloadCountTV.setText("");
		}
	}

	protected void initView() {
		rlytTop = (RelativeLayout) findViewById(R.id.ryot_top);
		OrderView refreshableView = (OrderView) findViewById(R.id.refreshview);
		mDownloadCountTV = (TextView) findViewById(R.id.game_store_header_download_count_tv);
		this.mSearchIBtn = (ImageButton) findViewById(R.id.game_store_search_btn);
		this.mSearchIBtn.setOnClickListener(this);
		this.mDownloadIBtn = (ImageButton) findViewById(R.id.game_store_header_download_btn);
		this.mDownloadIBtn.setOnClickListener(this);
		viewFlow = (ViewFlow) findViewById(R.id.viewflow_recom_game);
//		viewFlow.setIsTouch(false);
		circleFlowIndicator = (CircleFlowIndicator) findViewById(R.id.indicator_recom_game);
		width = Util.getScreenWidthSize(this);
		topAdsHeight = (int) (Math
				.ceil(Util.getScreenWidthSize(this) * 100 / 480) / 100 * 192);
		if (null != viewFlow && topRecomGameLst != null
				&& topRecomGameLst.size() > 0) {
			viewFlow.stopAutoFlowTimer();
			viewFlow.startAutoFlowTimer();
		}
		viewFlow.setLayoutParams(new LayoutParams(width, topAdsHeight));
		txtDesc = (TextView) findViewById(R.id.txt_game_info);
		relaytiveLayoutTop = (RelativeLayout) findViewById(R.id.rlot_game_store_top);
		seminarHeight = (int) (Math
				.ceil(Util.getScreenWidthSize(this) * 100 / 684) / 100 * 254);
		relaytiveLayoutTop
				.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
						width, topAdsHeight));
		ryoutHeader = (RelativeLayout) findViewById(R.id.ryot);
		gameSeminarViewFlow = (ViewFlow) findViewById(R.id.viewflow_seminar_game);
		gameSeminarViewFlow.setIsTouch(true);
		gameSeminarFlowIndicator = (CircleFlowIndicator) findViewById(R.id.indicator_seminar_game);
		gameSeminarFlowIndicator.setSetImg(true);// 自定义图片显示
		gameSeminarFlowIndicator
				.setActiveResId(R.drawable.gamestore_senamir_active);
		gameSeminarFlowIndicator
				.setInActiveResId(R.drawable.gamestore_senamir_inactive);
		gameSeminarFlowIndicator.initBgImg();

		if (null != gameSeminarViewFlow && seminarGameLst != null
				&& seminarGameLst.size() > 0) {
//			gameSeminarViewFlow.stopAutoFlowTimer();
//			gameSeminarViewFlow.startAutoFlowTimer();
		}
		gameSeminarViewFlow
				.setLayoutParams(new android.widget.RelativeLayout.LayoutParams(
						width, seminarHeight));

		gridViewRecommendGame = (GameGridView) findViewById(R.id.gv_recommend_game);
		gridViewRecommendGame.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridViewRecommendGame.setCacheColorHint(Color.TRANSPARENT);
//		gridViewRecommendGame.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				BehaviorInfo behaviorInfo = new BehaviorInfo(
//						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_ICON_ID,
//						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESS_ICON_NAME);
//				CYSystemLogUtil.behaviorLog(behaviorInfo);
//				if (recommendGameLst == null || recommendGameLst.size() == 0
//						|| position > recommendGameLst.size()) {
//					return;
//				}
//				final GameItem item = recommendGameLst.get(position);
//				if (item != null) {
//					Intent mIntent = new Intent();
//					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
//					mIntent.setClass(GameStoreActivity.this,
//							GameCircleDetailActivity.class);
//					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
//					startActivity(mIntent);
//				}
//
//			}
//		});

		gridViewHotGame = (GameGridView) findViewById(R.id.gv_hot_game);
		gridViewHotGame.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridViewHotGame.setCacheColorHint(Color.TRANSPARENT);
		gridViewHotGame.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_HOTRANK_MOREGAME_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_HOTRANK_MOREGAME_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (hotGameLst == null || hotGameLst.size() == 0
						|| position >= hotGameLst.size()) {
					Intent mIntent = new Intent();
					mIntent.setClass(GameStoreActivity.this,
							GameHotActivity.class);
					startActivity(mIntent);
					return;
				}
				GameItem item = hotGameLst.get(position);
				if (item != null && item.getGamecode() != null) {
					Intent mIntent = new Intent();
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.setClass(GameStoreActivity.this,
							GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					startActivity(mIntent);
				}
			}
		});
		gridViewNewGame = (GameGridView) findViewById(R.id.gv_new_game);
		gridViewNewGame.setSelector(new ColorDrawable(Color.TRANSPARENT));
		gridViewNewGame.setCacheColorHint(Color.TRANSPARENT);
		gridViewNewGame.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEWRANK_MOREGAME_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEWRANK_MOREGAME_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);

				if (newGameLst == null || newGameLst.size() == 0
						|| position >= newGameLst.size()) {
					Intent mIntent = new Intent();
					mIntent.setClass(GameStoreActivity.this,
							GameNewActivity.class);
					startActivity(mIntent);
					return;
				}
				final GameItem item = newGameLst.get(position);
				if (item != null && null != item.getGamecode()) {
					Intent mIntent = new Intent();
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.setClass(GameStoreActivity.this,
							GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					startActivity(mIntent);
				}

			}
		});
		txtFriendPlay = (TextView) findViewById(R.id.txt_friend_play);
		txtNearPlay = (TextView) findViewById(R.id.txt_near_play);
		txtClassify = (TextView) findViewById(R.id.txt_classify);
		txtYouMayLike = (TextView) findViewById(R.id.txt_you_may_like);

		txtFriendPlayStatic = (TextView) findViewById(R.id.txt_friend_play_static);
		txtNearPlayStatic = (TextView) findViewById(R.id.txt_near_play_static);
		txtClassifyStatic = (TextView) findViewById(R.id.txt_classify_static);
		txtYouMayLikeStatic = (TextView) findViewById(R.id.txt_you_may_like_static);

		btnMoreRecommend = (RelativeLayout) findViewById(R.id.btn_more_recommend);
		btnMoreSeminar = (Button) findViewById(R.id.btn_more_seminar);
		txtSeminarInfo = (TextView) findViewById(R.id.txt_game_info_seminar);
		rlytSeminar = (RelativeLayout) findViewById(R.id.rlot_seminar_game);
		rlytSeminar.setOnClickListener(null);
		initEvent();
		scrollView = (ScrollView) findViewById(R.id.sc_root);

		refreshableView.setStayView(findViewById(R.id.theview), scrollView,
				new StayViewListener() {
					@Override
					public void onStayViewShow() {
						findViewById(R.id.theviewstay).setVisibility(
								View.VISIBLE);
					}

					@Override
					public void onStayViewGone() {
						findViewById(R.id.theviewstay).setVisibility(View.GONE);
					}
				});

	}

	protected void initEvent() {
		txtFriendPlay.setOnClickListener(this);
		txtNearPlay.setOnClickListener(this);
		txtClassify.setOnClickListener(this);
		txtYouMayLike.setOnClickListener(this);

		txtFriendPlayStatic.setOnClickListener(this);
		txtNearPlayStatic.setOnClickListener(this);
		txtClassifyStatic.setOnClickListener(this);
		txtYouMayLikeStatic.setOnClickListener(this);

		btnMoreRecommend.setOnClickListener(this);
		btnMoreSeminar.setOnClickListener(this);
	}

	/**
	 * 最新游戏列表
	 */
	private void loadNewGameLst() {
		if (!NetUtil.isNetworkAvailable()) {
			String recommendGameData = SharedPreferenceUtil.getNewGameData();
			if (!TextUtils.isEmpty(recommendGameData)) {
				String data = JsonUtils.getJsonValue(recommendGameData,
						JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					newGameLst = (ArrayList<GameItem>) JsonUtils.json2List(
							data, GameItem.class);
					if (newGameLst != null && newGameLst.size() > 0) {
						newGameAdapter = new GameStoreNewListAdapter(
								GameStoreActivity.this, newGameLst,
								gridViewNewGame);
						gridViewNewGame.setAdapter(newGameAdapter);
						return;
					}
				}
			}
		}
		if (null == newGameLst) {
			newGameLst = new ArrayList<GameItem>();
		}
		final GameItem tempItem = new GameItem();
		tempItem.setGamecode("1");// 设置空数据的标识 避免跟更多处理产生冲突
		newGameLst.clear();
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		newGameLst.add(tempItem);
		if (newGameAdapter == null) {
			newGameAdapter = new GameStoreNewListAdapter(
					GameStoreActivity.this, newGameLst, gridViewNewGame);
		} else {
			newGameAdapter.updateData(newGameLst);
		}
		gridViewNewGame.setAdapter(newGameAdapter);
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(1));
		params.put("count", "7");
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.WORLD_NEWRANK,
				params,
				new JSONAsyncHttpResponseHandler<GameItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
						dismissProgressDialog();
						super.onFailure(error);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveRecommendData(null, null,
								null, content, null);
						log.d("最新游戏缓存:" + SharedPreferenceUtil.getNewGameData());
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						// dismissProgressDialog();
						if (null == list || list.size() == 0) {
							return;
						}
						newGameLst.clear();
						try {
							if (list.size() < 7) {
								newGameLst = list.subList(0, list.size());
								int size = 7 - list.size();
								for (int i = 0; i < size; i++) {
									if (i == size) {
										return;
									}
									newGameLst.add(tempItem);
								}
							} else {
								newGameLst = list;
								if (newGameLst.size() >= 7) {
									newGameLst = newGameLst.subList(0, 7);
								}
							}
							newGameAdapter = new GameStoreNewListAdapter(
									GameStoreActivity.this, newGameLst,
									gridViewNewGame);
							gridViewNewGame.setAdapter(newGameAdapter);
						} catch (Exception e) {
							log.e(e);
						}
						super.onSuccessForList(list);
					}
				});
	}

	/**
	 * 最热 游戏列表
	 */
	private void loadHotGameLst() {
		if (!NetUtil.isNetworkAvailable()) {
			String recommendGameData = SharedPreferenceUtil.getHotGameData();
			if (!TextUtils.isEmpty(recommendGameData)) {
				String data = JsonUtils.getJsonValue(recommendGameData,
						JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					hotGameLst = (ArrayList<GameItem>) JsonUtils.json2List(
							data, GameItem.class);
					hotGameAdapter = new GameStoreHotListAdapter(
							GameStoreActivity.this, hotGameLst, gridViewHotGame);
					gridViewHotGame.setAdapter(hotGameAdapter);
					return;
				}
			}
		}
		if (null == hotGameLst) {
			hotGameLst = new ArrayList<GameItem>();
		}
		final GameItem tempItem = new GameItem();
		tempItem.setGamecode("1");// 设置空数据的标识 避免跟更多处理产生冲突
		hotGameLst.clear();
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		hotGameLst.add(tempItem);
		if (hotGameAdapter == null) {
			hotGameAdapter = new GameStoreHotListAdapter(
					GameStoreActivity.this, hotGameLst, gridViewHotGame);
		} else {
			hotGameAdapter.updateData(hotGameLst);
		}
		gridViewHotGame.setAdapter(hotGameAdapter);
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(1));
		params.put("count", "7");
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GAME_RANK_WORLD,
				params,
				new JSONAsyncHttpResponseHandler<GameItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
						super.onFailure(error);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveRecommendData(null, null,
								null, null, content);
						log.d("最热缓存:" + SharedPreferenceUtil.getHotGameData());
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							return;
						}
						hotGameLst.clear();
						try {
							if (list.size() < 7) {
								hotGameLst = list.subList(0, list.size());
								int size = 7 - list.size();
								for (int i = 0; i < size; i++) {
									if (i == size) {
										return;
									}
									hotGameLst.add(tempItem);
								}
							} else {
								hotGameLst = list;
								if (hotGameLst.size() >= 7) {
									hotGameLst = hotGameLst.subList(0, 7);
								}
							}
							hotGameAdapter = new GameStoreHotListAdapter(
									GameStoreActivity.this, hotGameLst,
									gridViewHotGame);
							gridViewHotGame.setAdapter(hotGameAdapter);
						} catch (Exception e) {
							log.e(e);
						}
						super.onSuccessForList(list);
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.txt_friend_play:// 好友在玩
		case R.id.txt_friend_play_static:// 好友在玩
			mIntent.setClass(GameStoreActivity.this,
					GameFriendPlayActivity.class);
			startActivity(mIntent);
			break;
		case R.id.txt_near_play:// 附近在玩
		case R.id.txt_near_play_static:// 附近在玩
			mIntent.setClass(GameStoreActivity.this,
					GameNearbyPlayActivity.class);
			startActivity(mIntent);
			break;
		case R.id.txt_classify:// 分类大全
		case R.id.txt_classify_static:// 分类大全
			mIntent.setClass(GameStoreActivity.this, GameClassifyActivity.class);
			startActivity(mIntent);
			break;
		case R.id.txt_you_may_like:// 猜你喜欢
		case R.id.txt_you_may_like_static:// 猜你喜欢
			mIntent.setClass(GameStoreActivity.this,
					GameGuessYouLikeActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btn_more_recommend:// 更多精品
			mIntent.setClass(GameStoreActivity.this,
					ReCommendedGameActivity.class);
			startActivity(mIntent);
			break;
		case R.id.btn_more_seminar:// 更多专题
			mIntent.setClass(GameStoreActivity.this,
					GameSpecialTopicActivity.class);
			startActivity(mIntent);
			break;
		case R.id.game_store_search_btn:
			mIntent.setClass(GameStoreActivity.this, SearchActivity.class);
			startActivity(mIntent);
			break;
		case R.id.game_store_header_download_btn:
			mIntent.setClass(GameStoreActivity.this, DownloadActivity.class);
			startActivity(mIntent);
			break;
		}
	}

	/**
	 * 顶部轮播图
	 */
	private void loadTopRecomGameLst() {
		if (null == topRecomGameLst || topRecomGameLst.size() == 0) {
			topRecomGameLst = new ArrayList<AdvertBean>();
		}
		if (classGameViewAdapter == null) {
			classGameViewAdapter = new RecomGameViewFlowAdapter(this,
					topRecomGameLst, topAdsHeight, txtDesc);
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
					log.d("无网下顶部导航:" + topRecomGameLst.size());
					rlytTop.setVisibility(View.VISIBLE);
					classGameViewAdapter.updateListView(topRecomGameLst);
					viewFlow.setAdapter(classGameViewAdapter);
					viewFlow.setmSideBuffer(topRecomGameLst.size());
					circleFlowIndicator.setVisibility(View.VISIBLE);
					viewFlow.setFlowIndicator(circleFlowIndicator);
					viewFlow.setSelection(0);
					viewFlow.startAutoFlowTimer();
					return;
				}
			}
		}
		RequestParams params = new RequestParams();
		params.put("multitype", "1");
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
						SharedPreferenceUtil.saveRecommendData(content, null,
								null, null, null);
						log.d("顶部导航缓存:"
								+ SharedPreferenceUtil.getRecommendTopData());
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						topRecomGameLst = (ArrayList<AdvertBean>) list;
						if (topRecomGameLst == null
								|| topRecomGameLst.size() == 0) {
							return;
						}
						rlytTop.setVisibility(View.VISIBLE);
						classGameViewAdapter.updateListView(topRecomGameLst);
						viewFlow.setAdapter(classGameViewAdapter);
						viewFlow.setmSideBuffer(topRecomGameLst.size());
						circleFlowIndicator.setVisibility(View.VISIBLE);
						viewFlow.setFlowIndicator(circleFlowIndicator);
						viewFlow.setSelection(0);
						viewFlow.startAutoFlowTimer();
						super.onSuccessForList(list);
					}
				});
	}

	/**
	 * 游戏专题
	 */
	private void loadSeminarGameLst() {
		if (null == seminarGameLst || seminarGameLst.size() == 0) {
			seminarGameLst = new ArrayList<SeminarBean>();
		}
		if (!NetUtil.isNetworkAvailable()) {
			String specialData = SharedPreferenceUtil.getSpecialData();
			if (!TextUtils.isEmpty(specialData)) {
				String data = JsonUtils.getJsonValue(specialData,
						JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					seminarGameLst = (ArrayList<SeminarBean>) JsonUtils
							.json2List(data, SeminarBean.class);
					if (seminarGameLst == null || seminarGameLst.size() == 0) {
						return;
					}
					txtSeminarInfo.setVisibility(View.VISIBLE);
					if (gameSeminarViewAdapter == null) {
						gameSeminarViewAdapter = new SpecialGameViewFlowAdapter(
								GameStoreActivity.this, seminarGameLst,
								seminarHeight, txtSeminarInfo);
					}
					gameSeminarViewFlow.setAdapter(gameSeminarViewAdapter);
					gameSeminarViewFlow.setmSideBuffer(seminarGameLst.size());
					gameSeminarFlowIndicator.setVisibility(View.VISIBLE);
					gameSeminarViewFlow
							.setFlowIndicator(gameSeminarFlowIndicator);
					gameSeminarViewFlow.setSelection(0);
//					gameSeminarViewFlow.stopAutoFlowTimer();
//					gameSeminarViewFlow.startAutoFlowTimer();
					return;
				}
			}
		}
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GAMESTORE_TOPIC_LIST,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						SeminarBean.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForString(String content) {
						SharedPreferenceUtil.saveRecommendData(null, content,
								null, null, null);
						log.d("专题缓存:" + SharedPreferenceUtil.getSpecialData());
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						seminarGameLst = (ArrayList<SeminarBean>) list;
						if (seminarGameLst == null
								|| seminarGameLst.size() == 0) {
							return;
						}
						txtSeminarInfo.setVisibility(View.VISIBLE);
						// gameSeminarViewAdapter.updateListView(seminarGameLst);
						if (gameSeminarViewAdapter == null) {
							gameSeminarViewAdapter = new SpecialGameViewFlowAdapter(
									GameStoreActivity.this, seminarGameLst,
									seminarHeight, txtSeminarInfo);
						}
						gameSeminarViewFlow.setAdapter(gameSeminarViewAdapter);
						gameSeminarViewFlow.setmSideBuffer(seminarGameLst
								.size());
						gameSeminarFlowIndicator.setVisibility(View.VISIBLE);
						gameSeminarViewFlow
								.setFlowIndicator(gameSeminarFlowIndicator);
						gameSeminarViewFlow.setSelection(0);
//						gameSeminarViewFlow.stopAutoFlowTimer();
//						gameSeminarViewFlow.startAutoFlowTimer();
						super.onSuccessForList(list);
					}
				});
	}
}
