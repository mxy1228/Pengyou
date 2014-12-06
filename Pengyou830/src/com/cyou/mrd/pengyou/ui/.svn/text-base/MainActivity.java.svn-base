package com.cyou.mrd.pengyou.ui;


import android.app.Dialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TabHost;

import cn.optimad.trackingcode.android.OptiMadTrackingCode;
import cn.optimad.trackingcode.android.OptiMadTrackingCodeListener;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.CheckUpdateService;
import com.cyou.mrd.pengyou.service.DownloadIntentService;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.RelationCircleCache;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.inmobi.adtracker.androidsdk.IMAdTracker;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.madhouse.android.trackingcode.SmartmadTrackingCode;
import com.madhouse.android.trackingcode.SmartmadTrackingCodeListener;
import com.tessarmobile.sdk.TessarMobileSDK;

public class MainActivity extends TabActivity implements
		OnCheckedChangeListener, OnClickListener, OptiMadTrackingCodeListener {

	private CYLog log = CYLog.getInstance();
	private static final String ME = "me";
	private static final String GAME_LIBRARY = "game_library";
	private static final String RELATIONSHIP_CIRCLE = "relationship_circle";
	private static final String MORE = "more";

	private TabHost mTabHost;
	private RadioGroup mRG;
	private RadioButton mMyGameBtn;
	private RadioButton mGameLibraryBtn;
	private RadioButton mRelationCircleBtn;
	private RadioButton mMeBtn;
	private int mCurIndex = 0;
	private Dialog mUpdateDialog;
	// private TextView mVersionTV;
	// private TextView mUpdateContentTV;
	private Button mIgnoreBtn;
	private Button mOKBtn;
	private Button mMeDotBtn;
	private Button mRelationCircleDotBtn;
	private Button mGameStoreDotBtn;
	private long firClick = 0;
	private long secClick = 0;

	private static final int UPDATE_ME_DOT = 3;

	private SystemPullReceiver mSystemPullReceiver;
	private ConnectivityReceiver mConnectReceiver;
	public static boolean mFirstResume = true;
	private boolean mHadRegistSystemPullReceiver = false;
	private boolean mHadRegistConnectivityReceiver = false;
	public static boolean mInstance = true;
	private RelationCircleCache relCache;
	private SharedPreferences mLetterSP;
	private ForceLoginOutReceiver mForceLoginOutReceiver;
	private boolean mHadRegistForceLoginOut;
	private boolean isClickGameStore = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.i("MainActivity onCreate");
		if (savedInstanceState != null) {
			mFirstResume = savedInstanceState.getBoolean("first");
		}
		setContentView(R.layout.main);
		String utoken = UserInfoUtil.getUToken();
		log.i("utoken = "+utoken);
		//为什么加他？？？？？  TODO
//		if (utoken == null||TextUtils.isEmpty(utoken)) {
//			log.e("utoken is null");
//			Intent mIntent = new Intent();
//			mIntent.setClass(MainActivity.this, LoginActivity.class);
//			mIntent.putExtra(Params.FROM, true);
//			startActivity(mIntent);
//			Log.d("fugaianzhuang", "mainacitivity==113");
//			finish();
//			return;
//		}
		CyouApplication.setMainActiv(this);
		platformStatistics();
		/**
		 * 后台统计日志
		 */
		BehaviorInfo behaviorInfo = new BehaviorInfo(
		CYSystemLogUtil.BEHAVIOR_OPENAPP,
		CYSystemLogUtil.getCurrentTimeByFormat(CYSystemLogUtil.TIME_FORMAT));
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		initView();
		if (!CyouApplication.isOpenApp) {
			Intent downloadService = new Intent(this, DownloadIntentService.class);
			startService(downloadService);
		}
		CyouApplication.isOpenApp = true;
		// 获取屏幕宽度，为关系圈适应显示图片大小
		Config.screenWidthWithDip = Util.getScreenDpWidth(this);
		Config.SreenDensity = Util.getScreenDensity(this);
		this.relCache = new RelationCircleCache(this);
	}

	/**
	 * 判断是否登陆 若没登陆 跳转至登陆页面
	 */
//	private boolean checkLogin() {
//		String utoken = UserInfoUtil.getUToken();
//		if (TextUtils.isEmpty(utoken)) {
//			log.e("utoken = "+utoken);
//			return false;
//		}
//		return true;
//	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	/**
	 * 第三方平台统计
	 */
	private void platformStatistics() {
		// SmartMad平台统计
		if (PYVersion.SMARTMAD_SWITCH) {
			SmartmadTrackingCode stc = SmartmadTrackingCode.getInstance();
			stc.setTrackingListener(new SmartmadTrackingCodeListener() {

				@Override
				public void onTrackingStatus(int statusCode) {
					log.v("smartTrackingCode statusCode = " + statusCode);
				}
			});
			stc.startTracking(this, PYVersion.SMARTMAD_CODE);
		}
		// SmartMad平台统计
		// inmobi平台统计
		if (PYVersion.INMOBI_SWITCH) {
			IMAdTracker.getInstance().init(getApplicationContext(),
					PYVersion.INMOBI_CODE);
		}
		// // inmobi平台统计
		// //sonnar统计
		try {
			if (!PYVersion.DEBUG) {
				TessarMobileSDK
						.getInstance(
								this,
								getString(R.string.app_name),
								CyouApplication.getChannel(),
								getPackageManager().getPackageInfo(
										getPackageName(), 0).versionName, 5)
						.onCreate();
			}
		} catch (Exception e) {
			log.e(e);
		}
		// sonnar统计
	}

	private void initView() {
		this.mTabHost = getTabHost();
		this.mRG = (RadioGroup) findViewById(R.id.main_rg);
		this.mRG.setOnCheckedChangeListener(this);
	    getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		this.mMyGameBtn = (RadioButton) findViewById(R.id.main_mygame_btn);
		this.mGameLibraryBtn = (RadioButton) findViewById(R.id.main_game_library_btn);
		mGameLibraryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mCurIndex == 1 && isClickGameStore) {
					isClickGameStore = false;
				} else if (mCurIndex == 1) {// 重复刷新
					if (CyouApplication.getGameStoreAct() != null) {
						CyouApplication.getGameStoreAct().initData(true);
					}
				}
			}
		});
		this.mRelationCircleBtn = (RadioButton) findViewById(R.id.main_relationship_btn);
		this.mRelationCircleBtn.setOnClickListener(this);
		this.mMeBtn = (RadioButton) findViewById(R.id.main_me_btn);
		this.mTabHost.addTab(mTabHost.newTabSpec(ME).setIndicator(ME)
				.setContent(new Intent(this, MyGameActivity.class)));
		this.mTabHost.addTab(mTabHost.newTabSpec(GAME_LIBRARY)
				.setIndicator(GAME_LIBRARY)
				.setContent(new Intent(this, GameStoreActivity.class)));
		this.mTabHost.addTab(mTabHost.newTabSpec(RELATIONSHIP_CIRCLE)
				.setIndicator(RELATIONSHIP_CIRCLE)
				.setContent(new Intent(this, RelationCircleActvity.class)));
		this.mTabHost.addTab(mTabHost.newTabSpec(MORE).setIndicator(MORE)
				.setContent(new Intent(this, PersonalCenterActivity.class)));
		this.mRG.setOnCheckedChangeListener(this);
		resetRadioBtn(0);
		View updateView = getLayoutInflater().inflate(R.layout.update_dialog,
				null);
		// this.mVersionTV = (TextView) updateView
		// .findViewById(R.id.update_dialog_version_tv);
		// this.mUpdateContentTV = (TextView) updateView
		// .findViewById(R.id.update_dialog_content_tv);
		this.mIgnoreBtn = (Button) updateView
				.findViewById(R.id.update_dialog_ignore_btn);
		this.mIgnoreBtn.setOnClickListener(this);
		this.mOKBtn = (Button) updateView.findViewById(R.id.update_dialog_ok_btn);
		this.mOKBtn.setOnClickListener(this);
		this.mUpdateDialog = new Dialog(this, R.style.custom_diaolog);
		this.mUpdateDialog.setContentView(updateView);
		mMyGameBtn.setChecked(true);
		this.mMeDotBtn = (Button) findViewById(R.id.me_dot);
		this.mRelationCircleDotBtn = (Button) findViewById(R.id.main_relation_circle_dot);
		this.mGameStoreDotBtn = (Button) findViewById(R.id.main_gamestore_dot);
		checkMessage(false);

	}

	public void checkGameStoreDotImage(long time) {
		boolean isToddy = SharedPreferenceUtil.checkIsShowDot(time);
		if (isToddy) {
			showGameStoreDotImage();
		} else {
			hideGameStoreDotImage();
		}
	}

	@Override
	protected void onResume() {
		CyouApplication.setMainActiv(this);
		CyouApplication.isOpenApp = true;
		if (!mFirstResume) {
			super.overridePendingTransition(R.anim.activity_close_in,R.anim.activity_close_out);
		}
		super.onResume();
		try {
			if (mSystemPullReceiver == null) {
				mSystemPullReceiver = new SystemPullReceiver();
			}
			if (mSystemPullReceiver != null && !mHadRegistSystemPullReceiver) {
				registerReceiver(mSystemPullReceiver, new IntentFilter(Contants.ACTION.SYSTEM_MSG_ACTION));
				mHadRegistSystemPullReceiver = true;
			}
			if (mConnectReceiver == null) {
				mConnectReceiver = new ConnectivityReceiver();
			}
			if (mConnectReceiver != null && !mHadRegistConnectivityReceiver) {
				registerReceiver(mConnectReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
				mHadRegistConnectivityReceiver = true;
			}
			if (mFirstResume) {
				checkUpdate();// 检查更新
				mFirstResume = false;
			}
			if (mLetterSP == null) {
				mLetterSP = getSharedPreferences(Contants.SP_NAME.LETTER_ABOUT,
						Context.MODE_PRIVATE);
			}
			if (mForceLoginOutReceiver == null) {
				mForceLoginOutReceiver = new ForceLoginOutReceiver();
			}
			if (mForceLoginOutReceiver != null && !mHadRegistForceLoginOut) {
				registerReceiver(mForceLoginOutReceiver, new IntentFilter(
						Contants.ACTION.FORCE_LOGIN_OUT));
				mHadRegistForceLoginOut = true;
			}
			
		} catch (Exception e) {
			log.e(e);
		}
		/**
		 * 刷新右侧红色气泡
		 */
		log.d("MainActivity: send game count Broadcast!");
		Intent downloadIntent = new Intent(Contants.ACTION.DOWNLOADING_COUNT);
		sendBroadcast(downloadIntent);
		Util.requestFansAndLetterCount(this, SystemCountMsgItem.SYSTEM);
		checkGameStoreDot();
	}

	@Override
	protected void onRestart() {
		log.d("当前索引:" + CyouApplication.returnHomeIndex);
		CyouApplication.setMainActiv(this);
		// checkGameStoreDotImage();
		super.onRestart();
	}

	private void checkUpdate() {
		Intent intent = new Intent(this, CheckUpdateService.class);
		intent.putExtra(Params.PENGYOU_UPDATE.TYPE, CheckUpdateService.CHECK);
		startService(intent);
	}

	private void resetRadioBtn(int index) {
		RequestParams params = new RequestParams();
		switch (index) {
		case 0:
			mMyGameBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.game_library_press),
					null, null);
			mRelationCircleBtn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.relation_circle_normal), null, null);
			mGameLibraryBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.search_game_normal),
					null, null);
			mMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
					.getDrawable(R.drawable.me_normal), null, null);
			params.put("page", "1");
			MyHttpConnect.getInstance().post(HttpContants.NET.APPACTIVE,
					params, new AsyncHttpResponseHandler());
			break;
		case 1:
			mMyGameBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.game_library_normal),
					null, null);
			mRelationCircleBtn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.relation_circle_normal), null, null);
			mGameLibraryBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.search_game_press),
					null, null);
			mMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
					.getDrawable(R.drawable.me_normal), null, null);
			isClickGameStore = true;
			break;
		case 2:
			mMyGameBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.game_library_normal),
					null, null);
			mRelationCircleBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources()
							.getDrawable(R.drawable.relation_circle_press),
					null, null);
			mGameLibraryBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.search_game_normal),
					null, null);
			mMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
					.getDrawable(R.drawable.me_normal), null, null);
			break;
		case 3:
			mMyGameBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.game_library_normal),
					null, null);
			mRelationCircleBtn.setCompoundDrawablesWithIntrinsicBounds(
					null,
					getResources().getDrawable(
							R.drawable.relation_circle_normal), null, null);
			mGameLibraryBtn.setCompoundDrawablesWithIntrinsicBounds(null,
					getResources().getDrawable(R.drawable.search_game_normal),
					null, null);
			mMeBtn.setCompoundDrawablesWithIntrinsicBounds(null, getResources()
					.getDrawable(R.drawable.me_press), null, null);
			params.put("page", "0");
			MyHttpConnect.getInstance().post(HttpContants.NET.APPACTIVE,
					params, new AsyncHttpResponseHandler());
			break;
		default:
			break;
		}
		for (int i = 0; i < mRG.getChildCount(); i++) {
			if (i == index) {
				((RadioButton) mRG.getChildAt(i)).setTextColor(getResources()
						.getColor(R.color.white));
			} else {
				((RadioButton) mRG.getChildAt(i)).setTextColor(getResources()
						.getColor(R.color.bottom_normal_text_color));
			}
		}
	}

	public void initTabIndex() {
		mMyGameBtn.setChecked(true);
		Intent launchService = new Intent(this, LaunchService.class);// 上传数据
		startService(launchService);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// try to recycle memmory
		System.runFinalization();
		System.gc();
		switch (checkedId) {
		case R.id.main_mygame_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(CYSystemLogUtil.ME.ID,
					CYSystemLogUtil.ME.NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			resetRadioBtn(0);
			mCurIndex = 0;
			mTabHost.setCurrentTab(0);
			break;
		case R.id.main_game_library_btn:
			resetRadioBtn(1);
			mCurIndex = 1;
			mTabHost.setCurrentTab(1);
			hideGameStoreDotImage();
			break;
		case R.id.main_relationship_btn:// 关系圈
			resetRadioBtn(2);
			mCurIndex = 2;
			mTabHost.setCurrentTab(2);
			break;
		case R.id.main_me_btn:
			resetRadioBtn(3);
			behaviorInfo = new BehaviorInfo(CYSystemLogUtil.MORE.BTN_MORE_ID,
					CYSystemLogUtil.MORE.BTN_MORE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			mCurIndex = 3;
			mTabHost.setCurrentTab(3);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.header_search_btn:// 游戏搜索
			mIntent.setClass(MainActivity.this, SearchActivity.class);
			startActivity(mIntent);
			break;
		case R.id.header_download_btn:// 下载管理
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
					CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			if (mCurIndex == 2) {
				mIntent.setClass(MainActivity.this, SendMyDynamicAcivity.class);
			} else {
				mIntent.setClass(MainActivity.this, DownloadActivity.class);
			}
			startActivity(mIntent);
			break;
		case R.id.update_dialog_ignore_btn:
			mUpdateDialog.dismiss();
			break;
		case R.id.update_dialog_ok_btn:
			mUpdateDialog.dismiss();
			break;
		case R.id.header_feedback_btn:
			mIntent.setClass(MainActivity.this, FeedBackActivity.class);
			startActivity(mIntent);
			break;
		case R.id.header_find_btn:
			mIntent.setClass(MainActivity.this, FindFriendActivity.class);
			startActivity(mIntent);
			break;
		case R.id.main_relationship_btn:
			firClick = secClick;
			secClick = System.currentTimeMillis();
			if ((secClick - firClick) < 1000) {
				Intent intent = new Intent(Contants.ACTION.TOP_RELATION_CIRCLE);
				sendBroadcast(intent);
			}
			break;
		}
	}

	@Override
	protected void onDestroy() {
		log.d("onDestroy");
		/**
		 * 后台统计日志
		 */
		
		try {
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.BEHAVIOR_CLOSEAPP,
					CYSystemLogUtil.getCurrentTimeByFormat(CYSystemLogUtil.TIME_FORMAT));
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			unregistReceiver();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.e(e);
		}
		super.onDestroy();
	}
	@Override
	protected void onStop() {
		unregistReceiver();
		super.onStop();
	}
	/**
	 * 关闭广播
	 */
	private void unregistReceiver(){
		try {
			if (mSystemPullReceiver != null && mHadRegistSystemPullReceiver) {
				unregisterReceiver(mSystemPullReceiver);
				mHadRegistSystemPullReceiver = false;
			}
			if (mConnectReceiver != null && mHadRegistConnectivityReceiver) {
				unregisterReceiver(mConnectReceiver);
				mHadRegistConnectivityReceiver = false;
			}
			if (mForceLoginOutReceiver != null && mHadRegistForceLoginOut) {
				unregisterReceiver(mForceLoginOutReceiver);
				mHadRegistForceLoginOut = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_ME_DOT:
				int count = msg.arg1;
				if (count == 0) {
					mMeDotBtn.setVisibility(View.GONE);
				} else {
					mMeDotBtn.setVisibility(View.VISIBLE);
				}
				break;
			default:
				break;
			}
		};
	};
	

	// public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
	// if (keyCode == KeyEvent.KEYCODE_BACK) {
	// Intent mHomeIntent = new Intent(Intent.ACTION_MAIN);
	// mHomeIntent.addCategory(Intent.CATEGORY_HOME);
	// startActivity(mHomeIntent);
	// return true;
	// }
	// return false;
	// };

	/**
	 * 统计信息数目
	 */
	private void countLettersAndNewFans() {
		new Thread() {
			public void run() {
				try {
					SystemCountMsgItem item = SystemCountMsgItem.get();
					int currentFansCount = item.getmCurrentFansCount();
					int newFansCount = item.getmNewFansCount();
					int unreadChatLetter = item.getmUnreadLetterCount();
					int recommendFriendCount = item.getmRecommendFriendCount();
					Intent intent = new Intent(Contants.ACTION.UPDATE_MY_DOT);
					intent.putExtra(Params.UPDATE_MY_DOT.TOTAL_FANS_COUNT,
							currentFansCount);
					log.d("粉丝总数:" + currentFansCount);
					intent.putExtra(Params.UPDATE_MY_DOT.NEW_FANS,
							newFansCount < 0 ? 0 : newFansCount);
					log.d("新粉丝:" + newFansCount);
					intent.putExtra(Params.UPDATE_MY_DOT.NEW_CHAT_COUNT,
							unreadChatLetter);
					log.d("新私信数:" + unreadChatLetter);
					log.d("好友推荐数:" + recommendFriendCount);
					intent.putExtra(
							Params.UPDATE_MY_DOT.RECOMMEND_FRIEND_COUNT,
							recommendFriendCount);
					sendBroadcast(intent);
					Message msg = mHandler.obtainMessage();
					msg.what = UPDATE_ME_DOT;
					msg.arg1 = unreadChatLetter + newFansCount
							+ recommendFriendCount;
					mHandler.sendMessage(msg);
				} catch (Exception e) {
					log.e(e);
				}

			};
		}.start();
	}

	private class SystemPullReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("SystemPullReceiver:onReceive");
			if (intent == null) {
				log.e("intent is null");
				return;
			}
			String type = intent.getStringExtra("type");
			if (type.equals(SystemCountMsgItem.GAME_CIRCLE_MSG)) {
				sendBroadcast(new Intent(Contants.ACTION.UPDATE_GAME_CIRCLE_MSG));
			} else if (type.equals(SystemCountMsgItem.RELATION_CIRCLE_MSG)) {
				checkMessage(true);
			} else if (type.equals(SystemCountMsgItem.SYSTEM)) {
				countLettersAndNewFans();
				checkMessage(true);
			} else if (type.equals(SystemCountMsgItem.RECOMMEND_FRIEND)) {
				countLettersAndNewFans();
			}
		}

	}

	/**
	 * 发送离线消息
	 * 
	 * @author bichunhe
	 * 
	 */
	private class ConnectivityReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Util.isNetworkConnected(MainActivity.this)) {
				if (Config.needResendComment) {
					Config.needResendComment = false;
					relCache.sendFailedComments();
				}
				if (Config.needResendSupport) {
					Config.needResendSupport = false;
					relCache.sendFailedSupport();
				}
			} else {
				log.d("network invalid.");
			}
		}
	}

	private void checkMessage(boolean save) {
		SystemCountMsgItem item = SystemCountMsgItem.get();
		int newComment = item.getmUnreadCommentsCount();
		int lastEachFocusDynamicID = item.getmEachFocusLastDynamicID();
		int userid = UserInfoUtil.getCurrentUserId();
		int lastDynamicRead = SystemCountMsgItem.getLastDynmiacRead(userid);
		int lastMaxDynamicID = SystemCountMsgItem.getLastMaxDynmiacID(userid);
		if (save) {
			log.i("SystemMsg newComment ：" + newComment);
			log.i("SystemMsg lastEachFocusDynamicID：" + lastEachFocusDynamicID);
			log.i("SystemMsg lastDynamicRead：" + lastDynamicRead);
			log.i("SystemMsg lastMaxDynamicID：" + lastMaxDynamicID);
		} else {
			log.i("checkMessage newComment ：" + newComment);
			log.i("checkMessage lastEachFocusDynamicID："
					+ lastEachFocusDynamicID);
			log.i("checkMessage lastDynamicRead：" + lastDynamicRead);
			log.i("checkMessage lastMaxDynamicID：" + lastMaxDynamicID);
		}
		int showType = 0; // 1:显示红点， 2：显示提示框
		if (newComment == 0) {
			if (lastEachFocusDynamicID != 0
					&& lastEachFocusDynamicID != lastMaxDynamicID) {
				if (save) {
					if (lastMaxDynamicID != 0)
						SystemCountMsgItem.saveLastDynmiacRead(1, userid);
					SystemCountMsgItem.saveLastMaxDynmiacID(
							lastEachFocusDynamicID, userid);
				}
				if (lastMaxDynamicID != 0) {
					showType = 1;
				} else {
					showType = 0;
				}

			} else if (lastEachFocusDynamicID == lastMaxDynamicID
					&& lastDynamicRead != 0) {
				showType = 1;
			} else {
				showType = 0;
			}

		} else {
			showType = 2;
		}

		Intent msgIntent = new Intent(
				Contants.ACTION.UPDATE_RELATION_MSG_DOT_ACTION);
		switch (showType) {
		case 0:
			mRelationCircleDotBtn.setVisibility(View.GONE);
			msgIntent.putExtra("newComment", 0);
			msgIntent.putExtra("newDynamic", 0);
			MainActivity.this.sendBroadcast(msgIntent);
			break;
		case 1:
			mRelationCircleDotBtn.setBackgroundResource(R.drawable.message_dot);
			mRelationCircleDotBtn.setText("");
			mRelationCircleDotBtn.setVisibility(View.VISIBLE);
			msgIntent.putExtra("newComment", 0);
			msgIntent.putExtra("newDynamic", 1);
			MainActivity.this.sendBroadcast(msgIntent);
			break;
		case 2:
			mRelationCircleDotBtn
					.setBackgroundResource(R.drawable.message_count);
			if (newComment <= 99) {
				mRelationCircleDotBtn.setText(String.valueOf(newComment));
			} else {
				mRelationCircleDotBtn.setText("N");
			}
			mRelationCircleDotBtn.setVisibility(View.VISIBLE);
			msgIntent.putExtra("newComment", newComment);
			msgIntent.putExtra("newDynamic", 0);
			MainActivity.this.sendBroadcast(msgIntent);
			break;
		default:
			break;
		}
	}

	/**
	 * 显示找游戏底部红点
	 */
	public void showGameStoreDotImage() {
		mGameStoreDotBtn.setVisibility(View.VISIBLE);
	}

	/**
	 * 隐藏找游戏底部红点
	 */
	public void hideGameStoreDotImage() {
		mGameStoreDotBtn.setVisibility(View.GONE);
	}

	/**
	 * 切换到关系圈中的广场
	 */
	public void intent2GRelationCircle() {
		try {
			mRelationCircleBtn.setChecked(true);
			resetRadioBtn(2);
			mCurIndex = 2;
			mTabHost.setCurrentTab(2);
			if (null != CyouApplication.getRelationCircleAct()) {
				CyouApplication.getRelationCircleAct().intentSquareFragment();
			}
		} catch (Exception e) {
			log.e(e);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("first", false);
	}

	/**
	 * 互斥登录监听器
	 * @author xumengyang
	 */
	private class ForceLoginOutReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			LoginOutDialog dialog = new LoginOutDialog(MainActivity.this);
			dialog.create().show();
		}

	}

	@Override
	protected void onStart() {
		super.onStart();
		log.d("---->>>>> onStart");
		boolean isMadHouse = CyouApplication.isMadHouseSdk();
		if (isMadHouse) {
			String madHouseCode = CyouApplication.getMadHouseCode();
			if (!TextUtils.isEmpty(madHouseCode)) {
				OptiMadTrackingCode omtc = OptiMadTrackingCode.getInstance();
				omtc.setTrackingListener(this);
				omtc.startTracking(this, madHouseCode);
			}
		}

	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		log.d("---->>>>> onNewIntent");
		boolean isMadHouse = CyouApplication.isMadHouseSdk();
		if (isMadHouse) {
			this.setIntent(intent);
		}
	}

	@Override
	public void onTrackingStatus(int statusCode) {
		log.d("madhouseStatusCode is:" + statusCode);
	}

	private void checkGameStoreDot() {
		RequestParams params = new RequestParams();
		// params.put("area", UserInfoUtil.getCurrentAreaId());
		// params.put("country", Locale.getDefault().getCountry());
		// params.put("device", Util.getPhoneUA());
		// params.put("downloadType", CyouApplication.getChannel());
		// params.put("deviceSystem", Util.getSystemVersion());
		// params.put("networkType",
		// Util.getNetType(CyouApplication.mAppContext));
		// params.put("prisonBreak", Util.hasRootPer() == true ? "1" : "0");
		// if (!TextUtils.isEmpty(Util
		// .getNetExtraInfo(CyouApplication.mAppContext))) {
		// String[] netInfoArr = Util.getNetExtraInfo(
		// CyouApplication.mAppContext).split("_");
		// if (netInfoArr != null && netInfoArr.length == 2) {
		// params.put("operatorId", netInfoArr[0]);
		// params.put("operator", netInfoArr[1]);
		// }
		// }
		MyHttpConnect.getInstance().post(HttpContants.NET.APPACTIVE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						log.d("app active error is:" + content);
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(String content) {
						log.d("app active is:" + content);
						System.err.println("更新:"+content);
						String time = JsonUtils.getJsonValue(content,
								"gameadtime");
						if (!TextUtils.isEmpty(time)) {
							try {
								// 更新主页面底部导航-找游戏红点
								long gameadtime = Long.parseLong(time);
								checkGameStoreDotImage(gameadtime);
								SharedPreferenceUtil
										.setGamestoreDataTime(gameadtime);
							} catch (Exception e) {
								log.e(e);
							}
						}
						super.onSuccess(content);

					}
				});
	}

}
