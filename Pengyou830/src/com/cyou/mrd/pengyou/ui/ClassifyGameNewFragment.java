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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ClassifyGameNewAdapter;
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
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.ClassifyGameNewViewCache;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.RequestParams;

public class ClassifyGameNewFragment extends BaseFragment implements
		OnClickListener {
	private CYLog log = CYLog.getInstance();
	private ClassifyGameDetailActvity mActivity;
	private View contentView;
	PullToRefreshListView mPullListView;
	boolean isInitData = false;// 是否已经初始化
	private List<GameItem> gameList = new ArrayList<GameItem>();
	private ClassifyGameNewAdapter newGameAdapter;
	private int currentPageNum = 1;
	private boolean refresh = false;
	private String classifyId;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mActivity = (ClassifyGameDetailActvity) getActivity();
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_NEW_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_NEW_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		if (!isInitData) {
			isInitData = true;
		}
		downloadDao = DownloadDao.getInstance(mContext);;
		contentView = inflater.from(mActivity).inflate(
				R.layout.classify_game_detail_layout, null);
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
		if(newGameAdapter!=null){
			newGameAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}

	/**
	 * 加载我的游戏
	 */
	private void loadMyGameList() {
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(currentPageNum));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		if(TextUtils.isEmpty(classifyId)){
			classifyId=mActivity.getTid();
		}
		params.put("tid", classifyId);
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GAME_CLASSIFY_NEW,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						//mPullListView.loadComplete();
                        if(mPullListView != null){
                        	mPullListView.onRefreshFinish();
                            mPullListView.loadingFinish();
                            mPullListView.loadComplete();
                        }
						refresh = false;
						super.onFailure(error, content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (null == list || list.size() == 0) {
							mPullListView.onRefreshFinish();
                            mPullListView.loadingFinish();
							mPullListView.loadComplete();
							return;
						}			
			            if (refresh) {
			            	gameList.clear();
	                        mPullListView.onRefreshFinish();
	                    }
			            gameList.addAll(list);
			            currentPageNum++;
			            newGameAdapter.notifyDataSetChanged();
	                    mPullListView.loadingFinish();
	                    if (list.size() < Config.PAGE_SIZE) {
	                        mPullListView.loadComplete();
	                    }
	                    refresh = false;
	                    super.onSuccessForList(list);
					}
				});
	}

	private void initData() {
		if (gameList == null || gameList.size() == 0) {
			gameList = new ArrayList<GameItem>();
			newGameAdapter = new ClassifyGameNewAdapter(mActivity, gameList);
			mPullListView.setAdapter(newGameAdapter);
			loadMyGameList();
		} else {
			if (newGameAdapter == null) {
				newGameAdapter = new ClassifyGameNewAdapter(mActivity, gameList);
			}
			mPullListView.setAdapter(newGameAdapter);
		}
	}

	private void initView() {
		if (null == contentView) {
			return;
		}
		mPullListView = (PullToRefreshListView) contentView
				.findViewById(R.id.mygame_lstview);
		mPullListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (gameList == null || gameList.size() == 0
						|| position >= gameList.size()) {
					return;
				}
				GameItem item = gameList.get(position-1);
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
		mPullListView.setOnLoadListener(new LoadListener() {

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
				loadMyGameList();
			}
		});
		mPullListView.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {
				currentPageNum = 1;
				refresh = true;
				loadMyGameList();
			}
		});
		initData();
	}

	public ClassifyGameNewFragment() {
		super();
	}

	public ClassifyGameNewFragment(Activity mActivity2, String tid) {
		super(mActivity2);
		this.mActivity = (ClassifyGameDetailActvity) mActivity2;
		classifyId = tid;
	}

	@Override
	public void onClick(View v) {
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
	private DownloadDao downloadDao;
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
		index = gameList.indexOf(item)+1;
		if (index < 0) {
			return;
		}
		view = mPullListView.getChildAt(index - firstVisiblePosition);
		if (null == view) {
			mPullListView.getAdapter().getView(index, view, mPullListView);
		}
		if (view != null) {
			ClassifyGameNewViewCache viewCache = (ClassifyGameNewViewCache) view
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
