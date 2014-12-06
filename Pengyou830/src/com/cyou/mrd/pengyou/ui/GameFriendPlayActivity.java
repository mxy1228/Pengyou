package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuessYouLikeGameAdapter;
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
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RankingGameViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GameFriendPlayActivity extends CYBaseActivity implements OnClickListener  {
	public static final int INDEX_FRIEND = 0;// 朋友榜
	private CYLog log = CYLog.getInstance();
	PullToRefreshListView mPullListViewFriend;
	PullToRefreshListView mPullListViewGuess;
	private ArrayList<GameItem> guessGameList = new ArrayList<GameItem>();
	private List<GameItem> friendGameList = new ArrayList<GameItem>();
	
	private RankingGameAdapter myGameFriendAdapter;
	private GuessYouLikeGameAdapter guessGameAdapter;
	
	private int friendCurrentPageNum = 1;
	private DownloadDao downloadDao;
	private RelativeLayout mFailLayout;
	private Button  mFriendAddBtn;
	private ImageButton  mBackBtn;
	private ImageButton  mSearchBtn;
	private ImageButton mDownloadImg;
	private TextView mDownloadCountTV;
	private boolean mHadRegistDownloadReceiver = false;
	private DownloadCountReceiver mDownloadReceiver;
	private boolean refresh = false;
	public static final int GUESS_YOU_LIKE_TOTAL = 12;
	public static final int GUESS_YOU_LIKE_DISPLAY = 3;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_FRIENDPLAYING_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_FRIENDPLAYING_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		downloadDao = DownloadDao.getInstance(this);
		setContentView(R.layout.gamestore_friend);
		initView();
		registReceiver();
		registerDownloads();
	}

	@Override
	public void onResume() {
		log.d("onResume");
		if(friendCurrentPageNum==1){
			loadFriendGameList();
		}	
		super.onResume();
	}

	@Override
	public void onStart() {
		log.d("PersonalCenterFragment on start!");
		if(null!=myGameFriendAdapter){
			myGameFriendAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}

	/**
	 * 加载我的游戏
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void loadFriendGameList() {
		log.i("loadFriendGameList");
		mPullListViewFriend.setVisibility(View.VISIBLE);
		mFailLayout.setVisibility(View.GONE);
		RequestParams params = new RequestParams();
		String url = "";
		params.put("page", String.valueOf(friendCurrentPageNum));
		url = HttpContants.NET.GAME_RANK_FRINED;
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		mPullListViewFriend.reset();
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
						showNetErrorDialog(GameFriendPlayActivity.this,new ReConnectListener() {				
							@Override
							public void onReconnect() {
								loadFriendGameList();
							}
						});
						log.e(content);
						log.e(error.getMessage());
						super.onFailure(error, content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameFriendPlayActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onSuccessForString(String content) {
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						//log.i("loadFriendGameList list.size()==="+list.size());					
				
						if (null == list || list.size() == 0) {
							mPullListViewFriend.loadComplete();
							refresh = false;
							if (friendGameList == null
									|| friendGameList.size() == 0) {
								loadNoFriendData();
							}
							return;
						}
						
	                    if (refresh) {
	                    	friendGameList.clear();
	                    	mPullListViewFriend.onRefreshFinish();
	                    }
	                    friendGameList.addAll(list);
	                    friendCurrentPageNum++;
	                    myGameFriendAdapter.updateData(friendGameList,INDEX_FRIEND);
	                    myGameFriendAdapter.notifyDataSetChanged();
	                    mPullListViewFriend.loadingFinish();
	                    if (list.size() < Config.PAGE_SIZE) {
	                    	mPullListViewFriend.loadComplete();
	                    }
	                    refresh = false;
						super.onSuccessForList(list);
					}
				});
	}

	protected void initData() {
		/*if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
			String friendGameData = SharedPreferenceUtil.getGameRankFriendData();
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
					mFailLayout.setVisibility(View.VISIBLE);
				}
			} else {
				mPullListViewFriend.setVisibility(View.GONE);
				mFailLayout.setVisibility(View.VISIBLE);
			}
		}*/
		if (friendGameList != null && friendGameList.size() > 0) {
			myGameFriendAdapter.updateData(friendGameList, INDEX_FRIEND);
			mPullListViewFriend.loadingFinish(); 
			return;
		}
		///loadFriendGameList();
	}

	protected void initView() {
		((TextView)findViewById(R.id.gamestore_sub_header_tv)).setText(R.string.game_store_friend_play);	
		mFailLayout = (RelativeLayout) findViewById(R.id.gamestore_friend_fail);
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
				mIntent.setClass(GameFriendPlayActivity.this, DownloadActivity.class);
				startActivity(mIntent);
			}
		});
		mDownloadCountTV = (TextView) findViewById(R.id.gamestore_sub_header_download_count);
		
		mPullListViewFriend = (PullToRefreshListView)findViewById(R.id.gamestore_friend_listview);
		mPullListViewFriend.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				BehaviorInfo behaviorInfo = null;
				GameItem item = null;
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_FRIENDPLAYING_GAMEDETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_FRIENDPLAYING_GAMEDETAIL_NAME);
				if (friendGameList == null || friendGameList.size() == 0
						|| position > friendGameList.size()) {
					return;
				}
				item = friendGameList.get(position-1);
				if (null != behaviorInfo) {
					CYSystemLogUtil.behaviorLog(behaviorInfo);
				}
				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(GameFriendPlayActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
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
		
		mPullListViewFriend.setOnRefreshListener(new RefreshListener() {

			@Override
			public void onRefresh() {
				friendCurrentPageNum = 1;
				refresh=true;
				loadFriendGameList();
			}
		});

		if (null == friendGameList) {
			friendGameList = new ArrayList<GameItem>();
		}
		if (myGameFriendAdapter == null) {
			myGameFriendAdapter = new RankingGameAdapter(GameFriendPlayActivity.this,
					friendGameList, INDEX_FRIEND);
		}
		mPullListViewFriend.setAdapter(myGameFriendAdapter); 
		initData();
	}
	
	private void  loadNoFriendData() {	
		mFriendAddBtn=((Button)findViewById(R.id.gamestore_friend_add_btn));
		mFriendAddBtn.setOnClickListener(this);
		
		mPullListViewGuess = (PullToRefreshListView)findViewById(R.id.gamestore_friend_guess);
		mPullListViewGuess.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESSYOULIKE_GAMEDETAIL_ID,
						CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_GUESSYOULIKE_GAMEDETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);

				GameItem item = null;
				if (guessGameList == null || guessGameList.size() == 0
						|| position > guessGameList.size()) {
					return;
				}
				item = guessGameList.get(position);

				if (null == item) {
					return;
				}
				Intent mIntent = new Intent();
				mIntent.setClass(GameFriendPlayActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivity(mIntent);
			}
		});
		mPullListViewFriend.setVisibility(View.GONE);
		mFailLayout.setVisibility(View.VISIBLE);
		firstGuessGame();
	}
	
	/**
	 * 猜你喜欢
	 */
	private int mCurrentGuessPage = 1;

	private void loadGuessGameList() {
		/*if (!NetUtil.isNetworkAvailable()) {
			String myGuessGameData = SharedPreferenceUtil.getRecommendGuessData();
			if (!TextUtils.isEmpty(myGuessGameData)) {
				String data = JsonUtils.getJsonValue(myGuessGameData,JsonUtils.DATA);
				if (!TextUtils.isEmpty(data)) {
					guessGameList = (ArrayList<GameItem>) JsonUtils.json2List(
							data, GameItem.class);
					if (guessGameAdapter == null) {
						guessGameAdapter = new GuessYouLikeGameAdapter(GameFriendPlayActivity.this,guessGameList);
						mPullListViewGuess.setAdapter(guessGameAdapter);
					}
					guessGameAdapter.updateData(guessGameList);
					guessGameAdapter.notifyDataSetChanged();
					return;
				}
			}
		}*/
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mCurrentGuessPage));
		params.put("count", "3");
		params.put("color", String.valueOf(1));
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.GUESS_GAMS,
				params,
				new JSONAsyncHttpResponseHandler<GameItem>(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {

					@Override
					public void onStart() {
						
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
					}

					public void onFailure(Throwable error, String content) {
						showNetErrorDialog(GameFriendPlayActivity.this,new ReConnectListener() {				
							@Override
							public void onReconnect() {
								loadGuessGameList();
							}
						});
						log.e(content);
						log.e(error.getMessage());
						super.onFailure(error, content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameFriendPlayActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
					@Override
					public void onSuccessForString(String content) {
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						guessGameList = (ArrayList<GameItem>) list;
						if (guessGameAdapter == null) {
							guessGameAdapter = new GuessYouLikeGameAdapter(GameFriendPlayActivity.this, guessGameList);
							mPullListViewGuess.setAdapter(guessGameAdapter);
						}
						guessGameAdapter.updateData(guessGameList);
						guessGameAdapter.notifyDataSetChanged();
						mPullListViewGuess.loadComplete();
						mPullListViewGuess.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}
	
	private void firstGuessGame(){
		//随机显示本地3个游戏
		Random random = new Random();
		HashSet<Integer> set = new HashSet<Integer>();
		while(true){
			int a = random.nextInt(GUESS_YOU_LIKE_TOTAL);
			set.add(a);
			if(set.size()==GUESS_YOU_LIKE_DISPLAY){
				break;
			}			
		}
		Iterator<Integer> it = set.iterator();
	
		String myGuessGameData = SharedPreferenceUtil.getGuessYouLikeGame();
		log.d("onLoad() 1 ==="+myGuessGameData);
		if (!TextUtils.isEmpty(myGuessGameData)) {
			String data = JsonUtils.getJsonValue(myGuessGameData,JsonUtils.DATA);
			if (!TextUtils.isEmpty(data)) {
				guessGameList = (ArrayList<GameItem>) JsonUtils.json2List(
							data, GameItem.class);
				ArrayList<GameItem> tempGameList=new ArrayList<GameItem>();
				while(it.hasNext()){
					tempGameList.add(guessGameList.get(it.next().intValue()));
				}		
				guessGameList = tempGameList;
				if (guessGameAdapter == null) {
					guessGameAdapter = new GuessYouLikeGameAdapter(GameFriendPlayActivity.this,tempGameList);
					mPullListViewGuess.setAdapter(guessGameAdapter);
				}
				guessGameAdapter.updateData(tempGameList);
				guessGameAdapter.notifyDataSetChanged();
				mPullListViewGuess.loadComplete();
				mPullListViewGuess.loadingFinish();
			}	
		}else{
			loadGuessGameList();
			saveGuessGameList();
		}
	}
	
	private void saveGuessGameList() {
		RequestParams params = new RequestParams();
		params.put("count", "21");
		params.put("color", String.valueOf(1));
		MyHttpConnect.getInstance().post(HttpContants.NET.GUESS_GAMS,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						log.e(error);
						super.onFailure(error);
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						log.e(content);
						log.e(error.getMessage());
					}

					@Override
					public void onSuccess(String content) {
						SharedPreferenceUtil.saveGuessYouLikeGame(content);
						super.onSuccess(content);
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.gamestore_friend_add_btn:
			intent.setClass(GameFriendPlayActivity.this, FindFriendActivity.class);
			startActivity(intent);
			break;
		case R.id.gamestore_sub_header_search:
			intent.setClass(GameFriendPlayActivity.this, SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.gamestore_sub_header_back:
			finish();
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
			int count = DownloadDao.getInstance(GameFriendPlayActivity.this).getDownloadingTaskSize();
			if (count > 0) {
				mDownloadCountTV.setVisibility(View.VISIBLE);
				if (count <= 99) {
					mDownloadCountTV.setText(String.valueOf(count));
				} else {
					mDownloadCountTV.setText("N");
				}
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

	@Override
	public void onDestroy() {
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
		return mGameItem;
	}

	private void updateListViewItem(GameItem item) {
		if (item == null) {
			return;
		}
		int firstVisiblePosition;
		int index;
		View view = null;
		if (friendGameList == null || friendGameList.isEmpty()) {
			return;
		}
		firstVisiblePosition = mPullListViewFriend
				.getFirstVisiblePosition();
		index = friendGameList.indexOf(item)+1;
		if (index < 0) {
			return;
		}
		view = mPullListViewFriend.getChildAt(index - firstVisiblePosition);
		if (null == view) {
			mPullListViewFriend.getAdapter().getView(index, view,
					mPullListViewFriend);
		}else {
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
