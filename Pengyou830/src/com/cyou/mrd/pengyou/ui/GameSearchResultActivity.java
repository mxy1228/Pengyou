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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ScrollView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuessYouLikeGameAdapter;
import com.cyou.mrd.pengyou.adapter.SearchGameAdapter;
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
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameItemViewCache;
import com.cyou.mrd.pengyou.viewcache.RankingGameViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.SearchBar;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.SearchBar.TextAndActionListsner;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GameSearchResultActivity extends CYBaseActivity implements OnClickListener  {
	private CYLog log = CYLog.getInstance();
	private PullToRefreshListView mPullListViewGuess;
	private PullToRefreshListView mPullListViewResult;
	private PullToRefreshListView mPullListViewMore;
	private PullToRefreshListView mPullListViewFoot;
	private RelativeLayout mNoGameLayout;
	private ScrollView mSearchGameResult;
	private ArrayList<GameItem> guessGameList = new ArrayList<GameItem>();
	private List<GameItem> mData;
	private List<GameItem> mTempData;
	private List<GameItem> mPartData  = new ArrayList<GameItem>();

	private int mRequestCount = 1;	
	private SearchBar mSearchBar;
	private GuessYouLikeGameAdapter guessGameAdapter;
	private SearchGameAdapter mSearchAdapter;
	private SearchGameAdapter mSearchMoreAdapter;
	private DownloadDao downloadDao;
	private Button mSearchMoreButton;
	private ImageButton  mBackBtn;
	private ImageButton mDownloadImg;
	private TextView mDownloadCountTV;
	private boolean mHadRegistDownloadReceiver = false;
	private DownloadCountReceiver mDownloadReceiver;
	public static final int GUESS_YOU_LIKE_TOTAL = 21;
	public static final int GUESS_YOU_LIKE_DISPLAY = 3;
	private static final  int historyMaxCount=10;
	private static final  int GAME_MAX_SIZE = 200;
	WaitingDialog mWaitingDialog;
	private String receiveSearchKey = "";
	private boolean mTextChange = false;
	private String mCurrentSearch = "";
	
	LinearLayout linear_footView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gamestore_search);
		mWaitingDialog = new WaitingDialog(this);
		receiveSearchKey = getIntent().getStringExtra("gamedetail_gamename");
		initView();
		initData();
		initEvent();
		registReceiver();
		registerDownloads();
	}
	
	protected void initData() {	
		this.downloadDao = DownloadDao.getInstance(this);
		if (mData == null) {
			mData = new ArrayList<GameItem>();
		}
		if (mPartData == null) {
			mPartData = new ArrayList<GameItem>();
		}
		if (mSearchAdapter == null) {
			mSearchAdapter = new SearchGameAdapter(this, mPartData);
		}
		if (mSearchMoreAdapter == null) {
			mSearchMoreAdapter = new SearchGameAdapter(this, mData);
		}

		if (guessGameList == null) {
			guessGameList = new ArrayList<GameItem>();
		}
		if (guessGameAdapter == null) {
			guessGameAdapter = new GuessYouLikeGameAdapter(GameSearchResultActivity.this,guessGameList);
		}
		mPullListViewGuess.setAdapter(guessGameAdapter);
		mPullListViewFoot.setAdapter(guessGameAdapter);
		mPullListViewMore.setAdapter(mSearchMoreAdapter);
		mPullListViewResult.setAdapter(mSearchAdapter);
		search(receiveSearchKey);
		firstGuessGame();	
	}

	protected void initView() {
		((TextView)findViewById(R.id.sub_header_bar_tv)).setText(R.string.search_game_title);	
		mDownloadCountTV = (TextView) findViewById(R.id.header_download_count_tv);
		mBackBtn=((ImageButton)findViewById(R.id.sub_header_bar_left_ibtn));
		mBackBtn.setOnClickListener(this);	
		mDownloadImg = (ImageButton) findViewById(R.id.sub_header_download_btn);
		mDownloadImg.setOnClickListener(this);
		mSearchBar = (SearchBar) findViewById(R.id.gamestore_searchbar);
		mSearchBar.getEditText().setText(receiveSearchKey);
		mSearchBar.addCancelButton(true);
		
		mCurrentSearch = receiveSearchKey;
		
		mSearchMoreButton=(Button)findViewById(R.id.gamestore_search_more_btn);
		mSearchMoreButton.setOnClickListener(this);
		
		mPullListViewMore = (PullToRefreshListView) findViewById(R.id.gamestore_search_lv);
		mSearchGameResult = (ScrollView) findViewById(R.id.gamestore_search_container);
		mPullListViewResult = (PullToRefreshListView) findViewById(R.id.gamestore_search_result);
		 
	 	mNoGameLayout = (RelativeLayout) findViewById(R.id.gamestore_search_no_data);
	 	
	 	linear_footView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.gamestore_footview_layout, null);
	 	mPullListViewFoot = (PullToRefreshListView)linear_footView.findViewById(R.id.gamestore_footview_guess_lstview);
	

		mPullListViewGuess = (PullToRefreshListView)findViewById(R.id.gamestore_search_guess_lstview);
		mPullListViewGuess.setDividerHeight(2);
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		log.d("PersonalCenterFragment on start!");
		if(null!=guessGameAdapter){
			guessGameAdapter.notifyDataSetChanged();
		}
		super.onStart();
	}
	
	/**
	 * 猜你喜欢
	 */
	private int mCurrentGuessPage = 1;

	private void loadGuessGameList() {
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
						showNetErrorDialog(GameSearchResultActivity.this,new ReConnectListener() {				
							@Override
							public void onReconnect() {
								loadGuessGameList();
							}
						});
						log.e(content);
						log.e(error.getMessage());
					}
					
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameSearchResultActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}

					@Override
					public void onSuccessForString(String content) {
						//SharedPreferenceUtil.saveRecommendData(null, content,
								//null);
						super.onSuccessForString(content);
					}

					@Override
					public void onSuccessForList(List list) {
						if (guessGameList != null) {
							guessGameList.clear();
						}
						guessGameList = (ArrayList<GameItem>) list;
						if (guessGameAdapter == null) {
							guessGameAdapter = new GuessYouLikeGameAdapter(GameSearchResultActivity.this, guessGameList);
							mPullListViewGuess.setAdapter(guessGameAdapter);
						}
						guessGameAdapter.updateData(guessGameList);
						guessGameAdapter.notifyDataSetChanged();
						setFootListViewHeightBasedOnChildren(mPullListViewGuess);
						mCurrentGuessPage++;
						mPullListViewGuess.loadComplete();
						mPullListViewGuess.loadingFinish();
						super.onSuccessForList(list);
					}
				});
	}
	
	private void firstGuessGame(){
		mWaitingDialog.show();	
		String myGuessGameData = SharedPreferenceUtil.getGuessYouLikeGame();
		log.d("onLoad() 1 ==="+myGuessGameData);
		if (!TextUtils.isEmpty(myGuessGameData)) {
			String data = JsonUtils.getJsonValue(myGuessGameData,JsonUtils.DATA);
			if (!TextUtils.isEmpty(data)) {
				guessGameList = (ArrayList<GameItem>) JsonUtils.json2List(
							data, GameItem.class);
				ArrayList<GameItem> tempGameList=new ArrayList<GameItem>();
				//随机显示本地3个游戏
				Random random = new Random();
				HashSet<Integer> set = new HashSet<Integer>();
				while(true){
					int a = random.nextInt(guessGameList.size());
					set.add(a);
					if(set.size()==GUESS_YOU_LIKE_DISPLAY||set.size()==guessGameList.size()){
						break;
					}
				}
				Iterator<Integer> it = set.iterator();
				while(it.hasNext()){
					tempGameList.add(guessGameList.get(it.next().intValue()));
				}		
				guessGameList = tempGameList;
				if (guessGameAdapter == null) {
					guessGameAdapter = new GuessYouLikeGameAdapter(GameSearchResultActivity.this,tempGameList);
					mPullListViewGuess.setAdapter(guessGameAdapter);
				}
				mPullListViewGuess.loadComplete();
				mPullListViewGuess.loadingFinish();
				guessGameAdapter.updateData(tempGameList);
				guessGameAdapter.notifyDataSetChanged();
				setFootListViewHeightBasedOnChildren(mPullListViewGuess);
			}	
		}else{
			loadGuessGameList();
			saveGuessGameList();
		}
		mWaitingDialog.dismiss();
	}
	
	private void saveGuessGameList() {
		RequestParams params = new RequestParams();
		params.put("count", String.valueOf(GUESS_YOU_LIKE_TOTAL));
		params.put("color", String.valueOf(1));
		MyHttpConnect.getInstance().post(HttpContants.NET.GUESS_GAMS,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						log.e(error);
						search(receiveSearchKey);
						super.onFailure(error);
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}
					@Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameSearchResultActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}

					@Override
					public void onSuccess(String content) {
						SharedPreferenceUtil.saveGuessYouLikeGame(content);
						super.onSuccess(content);
					}
				});
	}
	
	/**
	 * 搜索游戏
	 * 
	 * @param keyword
	 * @param currentPage
	 */
	private void search(final String keyword) {
		mNoGameLayout.setVisibility(View.GONE);
		mPullListViewMore.reset();
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_ID,
				CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		if (mData == null) {
			mData = new ArrayList<GameItem>();
		}
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mRequestCount));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("schkey", keyword);
		/*if (!TextUtils.isEmpty(tid)) {
			params.put("tid", tid);
		}*/
		MyHttpConnect.getInstance().post2Json(
				HttpContants.NET.SEARCH_GAME,
				params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_LIST,
						GameItem.class) {
					@Override
					public void onFailure(Throwable error, String content) {
						if(mSearchGameResult == null || mPullListViewMore == null){
							return;
						}
						///dismissWaitingDialog();
						mPullListViewMore.loadingFinish();
					    mPullListViewMore.loadComplete();
						showNetErrorDialog(GameSearchResultActivity.this,new ReConnectListener() {							
							@Override
							public void onReconnect() {
								search(keyword);
							}
						});
						super.onFailure(error, content);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameSearchResultActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccessForList(List list) {
						////dismissWaitingDialog();
						mTempData = list;
						if (null == mTempData || mTempData.size() == 0) {
							mPullListViewResult.loadComplete();
							if (mData == null || mData.size() == 0) {							
							    mSearchGameResult.setVisibility(View.VISIBLE);
							    mNoGameLayout.setVisibility(View.VISIBLE);
							    mPullListViewResult.setVisibility(View.GONE);
							    mSearchMoreButton.setVisibility(View.GONE);
							    mPullListViewMore.setVisibility(View.GONE);
							}
							return;
						}
						
						mData.addAll(mTempData);

						if(mSearchGameResult.isShown()) {
							if(mTempData.size()>5){
								mSearchMoreButton.setVisibility(View.VISIBLE);
							}
							for(int i=0;i<mTempData.size();i++) {
								mPartData.add(mTempData.get(i));
								if(i==4){
								    break;
								}
							}							
							mPullListViewResult.loadComplete();
							mSearchAdapter.updateData(mPartData);
							mSearchAdapter.notifyDataSetChanged();
							setListViewHeightBasedOnChildren(mPullListViewResult);
						}
						
						if(mPullListViewMore.isShown()) {
							///mPullListViewMore.reset();
							if (mTempData.size() < Config.PAGE_SIZE || mData.size()== GAME_MAX_SIZE) {
								mPullListViewMore.loadComplete();
								setFootListViewHeightBasedOnChildren(mPullListViewFoot);
								mPullListViewMore.addFooterView(linear_footView);							
							} 
							mSearchMoreAdapter.updateData(mData);
							mSearchMoreAdapter.notifyDataSetChanged();							
							mPullListViewMore.loadingFinish();
						}
						mRequestCount++;
						super.onSuccessForList(list);
					}
				});
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			///Intent intent = new Intent();
			///setResult(1, intent);
			finish();
			break;
		case R.id.gamestore_search_more_btn:
			if (mData.size() < Config.PAGE_SIZE) {
				mSearchMoreButton.setVisibility(View.GONE);
				mPartData.clear();
				mPartData.addAll(mData);
				mSearchAdapter.updateData(mPartData);
				mSearchAdapter.notifyDataSetChanged();
				setListViewHeightBasedOnChildren(mPullListViewResult);
			}else{
				mPullListViewMore.smoothScrollToPosition(0); 
				mSearchMoreAdapter.notifyDataSetChanged();
				mSearchGameResult.setVisibility(View.GONE);
				mPullListViewMore.setVisibility(View.VISIBLE);
				mPullListViewMore.removeFooterView(linear_footView);	
				mPullListViewMore.reset();		
			}
			break;
		case R.id.sub_header_download_btn:
			Intent mIntent = new Intent();
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			mIntent.setClass(GameSearchResultActivity.this, DownloadActivity.class);
			startActivity(mIntent);
			break;
		default:
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
			int count = DownloadDao.getInstance(GameSearchResultActivity.this).getDownloadingTaskSize();
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
			String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
			updateListViewItem(getGameItemByPackage(packageName),0);				
			updateListViewItem(getGameDataItemByPackage(packageName),1);
			updateListViewItem(getGameDataItemByPackage(packageName),2);
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
			String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
			updateListViewItem(getGameItemByPackage(packageName),0);				
			updateListViewItem(getGameDataItemByPackage(packageName),1);
			updateListViewItem(getGameDataItemByPackage(packageName),2);
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
			String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
			switch (state) {
			case DownloadParam.TASK.DELETE:
			case DownloadParam.TASK.DONE:
			case DownloadParam.TASK.ADD:
			case DownloadParam.TASK.CONTINUE:
				updateListViewItem(getGameItemByPackage(packageName),0);				
				updateListViewItem(getPartDataItemByPackage(packageName),1);
				updateListViewItem(getGameDataItemByPackage(packageName),2);
				break;
			default:
				break;
			}
		}

	}

	private GameItem getGameItemByPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		GameItem mGameItem = null;
		if (null == guessGameList || guessGameList.isEmpty()) {
			return mGameItem;
		}
		for (GameItem item : guessGameList) {
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
	
	private GameItem getGameDataItemByPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		GameItem mGameItem = null;
		if (null == mData || mData.isEmpty()) {
			return mGameItem;
		}
		for (GameItem item : mData) {
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
	private GameItem getPartDataItemByPackage(String packageName) {
		if (TextUtils.isEmpty(packageName)) {
			return null;
		}
		GameItem mGameItem = null;
		if (null == mPartData || mPartData.isEmpty()) {
			return mGameItem;
		}
		for (GameItem item : mPartData) {
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

	private void updateListViewItem(GameItem item, int type) {
		if (item == null) {
			return;
		}
		int firstVisiblePosition;
		int index = 0;
		View view = null;
		switch(type){
		case 0:
			if (guessGameList == null || guessGameList.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewGuess.getFirstVisiblePosition();
			index = guessGameList.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewGuess.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewGuess.getAdapter().getView(index, view,mPullListViewGuess);
				return;
			}
			updateListViewItemGuess(item,view);
			break;
		case 1:
			if (mPartData == null || mPartData.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewResult.getFirstVisiblePosition();
			index = mPartData.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewResult.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewResult.getAdapter().getView(index, view,mPullListViewResult);
				return;
			}
			updateListViewItemGame(item,view);
			break;
		case 2:
			if (mData == null || mData.isEmpty()) {
				return;
			}
			firstVisiblePosition = mPullListViewMore.getFirstVisiblePosition();
			index = mData.indexOf(item);
			if (index < 0) {
				return;
			}
			view = mPullListViewMore.getChildAt(index - firstVisiblePosition);
			if (null == view) {
				mPullListViewMore.getAdapter().getView(index, view,mPullListViewMore);
				return;
			}
			updateListViewItemGame(item,view);
			break;
		default:
			break;
		}
	}
	
	private void updateListViewItemGuess(GameItem item ,View view) {
		RankingGameViewCache viewCache = (RankingGameViewCache) view.getTag();
		if(viewCache==null){
			return;
		}
		if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
			viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.play_btn_xbg);
			viewCache.getBtnDownloadGame().setText(R.string.game_play);
			viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
		} else {
			if (!downloadDao.isHasInfo(item.getIdentifier(),item.getVersion())) {
				viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getBtnDownloadGame().setText(R.string.game_download);
				viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
				return;
			}
			DownloadItem downloadItem = downloadDao.getDowloadItem(item.getIdentifier(), item.getVersion());
			if (downloadItem == null || TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
				viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getBtnDownloadGame().setText(R.string.game_download);
				viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
			} else {
				if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
					viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
					viewCache.getBtnDownloadGame().setText(R.string.download_btn_install);
					viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
				} else {
					if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
							|| downloadItem.getmState() == DownloadParam.C_STATE.WAITING
							|| downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
						viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.downloading_btn_xbg);
						viewCache.getBtnDownloadGame().setText(R.string.game_downloading);
						viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.downloading_text));
					} else {
						viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
						viewCache.getBtnDownloadGame().setText(R.string.game_download);
						viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
					}
				}
			}
		}
	}
	
	private void updateListViewItemGame(GameItem item ,View view) {
		GameItemViewCache viewCache = (GameItemViewCache) view.getTag();
		if(viewCache==null){
			return;
		}
		if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
			viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.play_btn_xbg);
			viewCache.getBtnDownloadGame().setText(R.string.game_play);
			viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
		} else {
			if (!downloadDao.isHasInfo(item.getIdentifier(),item.getVersion())) {
				viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getBtnDownloadGame().setText(R.string.game_download);
				viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
				return;
			}
			DownloadItem downloadItem = downloadDao.getDowloadItem(item.getIdentifier(), item.getVersion());
			if (downloadItem == null || TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
				viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getBtnDownloadGame().setText(R.string.game_download);
				viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
			} else {
				if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
					viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
					viewCache.getBtnDownloadGame().setText(R.string.download_btn_install);
					viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
				} else {
					if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
							|| downloadItem.getmState() == DownloadParam.C_STATE.WAITING
							|| downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
						viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.downloading_btn_xbg);
						viewCache.getBtnDownloadGame().setText(R.string.game_downloading);
						viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.downloading_text));
					} else {
						viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
						viewCache.getBtnDownloadGame().setText(R.string.game_download);
						viewCache.getBtnDownloadGame().setTextColor(getResources().getColor(R.color.white));
					}
				}
			}
		}
	}

	@Override
	protected void initEvent() {
		mPullListViewMore.setOnLoadListener(new LoadListener() {

			@Override
			public void onLoad() {
				search(mCurrentSearch);
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
		});	
		
		mPullListViewMore.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hideInputMethod();
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.SEARCHGAME.BTN_DETAIL_ID,
						CYSystemLogUtil.SEARCHGAME.BTN_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (mData != null && mData.size() > position) {
					GameItem item = mData.get(position);
					Intent mIntent = new Intent();
					mIntent.setClass(GameSearchResultActivity.this, GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					startActivity(mIntent);
				}
			}
		});	
		mPullListViewResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				hideInputMethod();
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.SEARCHGAME.BTN_DETAIL_ID,
						CYSystemLogUtil.SEARCHGAME.BTN_DETAIL_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if (mPartData != null && mPartData.size() > position) {
					GameItem item = mPartData.get(position);
					Intent mIntent = new Intent();
					mIntent.setClass(GameSearchResultActivity.this, GameCircleDetailActivity.class);
					mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					startActivity(mIntent);
				}
			}
		});
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
				mIntent.setClass(GameSearchResultActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivity(mIntent);
			}
		});
		
		mPullListViewFoot.setOnItemClickListener(new OnItemClickListener() {
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
				mIntent.setClass(GameSearchResultActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivity(mIntent);
			}
		});
		
		mSearchBar.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		mSearchBar.setTextAndActionListener(new TextAndActionListsner() {
			@Override
			public void onText() {
				mTextChange = true;
			}

			@Override
			public void onEmpty() {
		
			}

			@Override
			public void onAction(String key) {
				searchAction();
			}
		});
		
		mSearchBar.getCancelButton().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				searchAction();
			}
		});
	}
	
	private void searchAction() {
		String key =mSearchBar.getEditText().getText().toString();
		if (TextUtils.isEmpty(key)) {
			showLongToast(R.string.game_search_nokey);
			return;
		}
		
		hideInputMethod();
		if(!mTextChange){
			return;
		}
		mTextChange=false;
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_ID,
				CYSystemLogUtil.SEARCHGAME.BTN_SEARCH_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);

		if(mData != null) {
			mData.clear();
		}
		if(mPartData != null) {
			mPartData.clear();
		}
		mRequestCount=1;
		mCurrentSearch = key;
		mSearchGameResult.setVisibility(View.VISIBLE);
		mPullListViewResult.setVisibility(View.VISIBLE);
		mPullListViewMore.setVisibility(View.GONE);
		mSearchMoreButton.setVisibility(View.GONE);
		mPullListViewResult.reset();
		search(key);
		saveSerchHistory(key);
	}
	
	//保存搜索记录
	private void saveSerchHistory(String keyword){
		if (TextUtils.isEmpty(keyword)) {
			return;
		}	
		SharedPreferences mHistoryPreference = getSharedPreferences("game_search_history", 0);
		int count = mHistoryPreference.getInt("count",0);
		
		SharedPreferences.Editor editor = mHistoryPreference.edit();
		for(int i=1;i<count+1;i++){
			if(mHistoryPreference.getString("history_"+i,"").equals(keyword)){
				for(int j=i;j<count;j++){
					editor.putString("history_"+j,mHistoryPreference.getString("history_"+(j+1),""));
				}
				editor.putString("history_"+count,keyword);
				editor.commit();
				return;
			}
		}
		if(count<historyMaxCount){	
			editor.putInt("count",count+1);
			editor.putString("history_"+(count+1),keyword);
		}
		else{
			for(int i=1;i<historyMaxCount;i++){	
				editor.putString("history_"+i,mHistoryPreference.getString("history_"+(i+1),""));
			}
			editor.putString("history_"+historyMaxCount,keyword);
		}
		editor.commit();
		
	}
		
    public void setListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
        int totalHeight = 0;  
        for (int i = 0; i < listAdapter.getCount(); i++) {  
            View listItem = listAdapter.getView(i, null, listView);  
            listItem.measure(0, 0);  
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    }  
    public void setFootListViewHeightBasedOnChildren(ListView listView) {  
        ListAdapter listAdapter = listView.getAdapter();   
        if (listAdapter == null) {  
            return;  
        }  
        View listItem = listAdapter.getView(0, null, listView);  
        listItem.measure(0, 0); 
        int totalHeight = 0;
        if(listAdapter.getCount()>2){
        	totalHeight = listItem.getMeasuredHeight()*(GUESS_YOU_LIKE_DISPLAY);
        }else {
        	totalHeight = listItem.getMeasuredHeight()*(listAdapter.getCount() - 1);
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();  
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));  
        listView.setLayoutParams(params);  
    } 
    
    private InputMethodManager getInputMethodManager() {
        return (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    public void showInputMethod() {
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.showSoftInput(mSearchBar.getEditText(), 0);
        }
    }

    public void hideInputMethod() {
        InputMethodManager imm = getInputMethodManager();
        if (imm != null) {
            imm.hideSoftInputFromWindow(mSearchBar.getEditText().getWindowToken(), 0);
        }
    }
}
