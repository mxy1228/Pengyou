package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.PersonalCenterAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.PersonalCenterItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.UnreadUserLetterInfo;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.viewcache.PersonalCenterViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PersonalCenterActivity extends CYBaseActivity implements
		OnItemClickListener, OnClickListener {

	private static final int UPDATE_MSG_COUNT = 1;
	private static final int ROUND = 180;
	
	private ListView mListView;
	private View mHeaderView;
	private Button mLoginOutBtn;
	private ImageView mAvatarIV;
	private ImageView mSinaIV;
	private ImageView mPhoneIV;
	private TextView mNickNameTV;
	private TextView mSignTV;
	private TextView mFansCountTV;
	private TextView mDynamicCountTV;
	private TextView mFocusCountTV;
	private Button mNewFansDotBtn;
	private TextView mProgressTV;
	private RelativeLayout mFocusRL;
	private RelativeLayout mFansRL;
	private RelativeLayout mDynamicRL;
	private AlertDialog mSharedDialog;

	private List<PersonalCenterItem> mData;
	private PersonalCenterAdapter mAdapter;
	private DotCountReceiver mDotCountReceiver;
//	private DisplayImageOptions mOption;
	private UserInfoListener mUserInfoListener;
	private SharedPreferences mUserInfoSP;
	private UserDataListener mUserDataListener;
	private SharedPreferences mUserDataSP;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.personal_center);
		initView();
		initEvent();
		initData();
	}

	@Override
	protected void initView() {
		this.mListView = (ListView) findViewById(R.id.personal_center_lv);
		this.mHeaderView = mInflater.inflate(R.layout.personal_center_header,
				null);
		this.mListView.addHeaderView(mHeaderView, null, false);
		this.mAvatarIV = (ImageView) mHeaderView
				.findViewById(R.id.personal_center_avatar_iv);
		this.mSinaIV = (ImageView) mHeaderView
				.findViewById(R.id.personal_center_sina_iv);
		this.mPhoneIV = (ImageView) mHeaderView
				.findViewById(R.id.personal_center_phone_iv);
		this.mNickNameTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_nickname_tv);
		this.mSignTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_sign_tv);
		this.mFansCountTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_fans_count_tv);
		this.mDynamicCountTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_dynamic_count_tv);
		this.mFocusCountTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_focus_count_tv);
		this.mNewFansDotBtn = (Button) mHeaderView
				.findViewById(R.id.personal_center_fans_dot_btn);
		this.mProgressTV = (TextView) mHeaderView
				.findViewById(R.id.personal_center_info_progress_tv);
		View footView = mInflater
				.inflate(R.layout.personal_center_footer, null);
		this.mLoginOutBtn = (Button) footView
				.findViewById(R.id.personal_center_login_out_btn);
		this.mListView.addFooterView(footView);
		this.mFocusRL = (RelativeLayout) findViewById(R.id.personal_center_focus_rl);
		this.mFansRL = (RelativeLayout) findViewById(R.id.personal_center_fans_rl);
		this.mDynamicRL = (RelativeLayout) findViewById(R.id.personal_center_dynamic_rl);
	}

	@Override
	protected void initEvent() {
		this.mListView.setOnItemClickListener(this);
		this.mDotCountReceiver = new DotCountReceiver();
		registerReceiver(mDotCountReceiver, new IntentFilter(new IntentFilter(
				Contants.ACTION.UPDATE_MY_DOT)));
		this.mUserInfoListener = new UserInfoListener();
		mUserInfoSP = getSharedPreferences(Contants.SP_NAME.USER_INFO,
				Context.MODE_PRIVATE);
		mUserInfoSP.registerOnSharedPreferenceChangeListener(mUserInfoListener);
		this.mLoginOutBtn.setOnClickListener(this);
		this.mSignTV.setOnClickListener(this);
		this.mFocusRL.setOnClickListener(this);
		this.mFansRL.setOnClickListener(this);
		this.mDynamicRL.setOnClickListener(this);
		this.mAvatarIV.setOnClickListener(this);
		this.mUserDataListener = new UserDataListener();
		mUserDataSP = getSharedPreferences(Contants.SP_NAME.USER_DATA,
				Context.MODE_PRIVATE);
		mUserDataSP.registerOnSharedPreferenceChangeListener(mUserDataListener);
	}

	@Override
	protected void onRestart() {
		if (CyouApplication.returnHomeIndex != Contants.RETURNHOME.RETURN_HOME) {// 通过back切换home
			CyouApplication.returnHomeIndex = Contants.RETURNHOME.RETURN_HOME;
			if (CyouApplication.getMainActiv() != null) {
				CyouApplication.getMainActiv().initTabIndex();
			}
		}
//		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
		super.onRestart();
	}


	@Override
	protected void initData() {
		this.mData = new ArrayList<PersonalCenterItem>();
		initArray();
		this.mAdapter = new PersonalCenterAdapter(this, mData);
		this.mListView.setAdapter(mAdapter);
		initUserInfo();
	}

	@Override
	protected void onResume() {
		Util.requestFansAndLetterCount(this,SystemCountMsgItem.SYSTEM);
		if (isBindTelePhone()) {
			this.mLoginOutBtn.setVisibility(View.VISIBLE);
		} else {
			this.mLoginOutBtn.setVisibility(View.GONE);
		}
		CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
				mAvatarIV, new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(ROUND))
				.build());
//		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		try {
			if (mDotCountReceiver != null) {
				unregisterReceiver(mDotCountReceiver);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		super.onDestroy();
	}

	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		PersonalCenterItem item = (PersonalCenterItem) mListView
				.getItemAtPosition(arg2);
		if (item == null) {
			return;
		}
		if(arg2 == 1 
				&& !isBindTelePhone()
				){
			Intent intent = new Intent(PersonalCenterActivity.this,LoginAndRegistActivity.class);
			startActivity(intent);
		}else if(arg2 == 4){
//			PersonalCenterViewCache viewCache = (PersonalCenterViewCache)arg1.getTag();
//			if(viewCache == null){
//				log.e("viewCache is null");
//				return;
//			}
//			String recommendCount = viewCache.getmDotBtn().getText().toString();
			int recommendCount = mData.get(3).getRecommendCount();
			Intent intent = new Intent(item.getAction());
			intent.putExtra("count", recommendCount);
			startActivity(intent);
		}else if(arg2 == 7){
			if (mSharedDialog == null) {
				showShareAppSelector();
			}
			mSharedDialog.show();
		}else{
			Intent intent = new Intent(item.getAction());
			startActivity(intent);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.personal_center_login_out_btn:
			// 退出登录的问题大爷我修了整整一天！
			// 你妹啊！最后不得已使用了必杀技，干掉进程，世界清静了···
			Util.loginOut(true,PersonalCenterActivity.this);
			PersonalCenterActivity.this.finish();
			break;
		case R.id.personal_center_focus_rl:
			intent.setClass(PersonalCenterActivity.this, MyFocusActivity.class);
			startActivity(intent);
			break;
		case R.id.personal_center_dynamic_rl:
			intent.setClass(PersonalCenterActivity.this,
					MyDynamicActivity.class);
			startActivity(intent);
			break;
		case R.id.personal_center_fans_rl:
			intent.setClass(PersonalCenterActivity.this, MyFansActivity.class);
			startActivity(intent);
			mNewFansDotBtn.setVisibility(View.GONE);
			SystemCountMsgItem.cleanNewFans();
			break;
		case R.id.personal_center_sign_tv:
			intent.setClass(PersonalCenterActivity.this, EditInfoAcivity.class);
			startActivity(intent);
			break;
		case R.id.personal_center_avatar_iv:
			intent.setClass(PersonalCenterActivity.this, EditInfoAcivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	/**
	 * 初始化数组数据
	 */
	private void initArray() {
		String[] array = getResources().getStringArray(
				R.array.personal_center_array);
		for (int j = 0; j < array.length; j++) {
			String[] temp = array[j].split(";");
			PersonalCenterItem item = new PersonalCenterItem();
			item.setName(temp[0]);
			item.setTag(Integer.valueOf(temp[1]));
			item.setAction(temp[2]);
			item.setShowHint(false);
			item.setHintContent("");
			if(j == 0){
				if(isBindTelePhone()){
					item.setName(UserInfoUtil.getCurrentUserPhoneNum());
					item.setShowHint(true);
					item.setHintContent(getString(R.string.have_a_change));
				}
			}else if(j == 1){
				item.setName(getString(R.string.scores, UserInfoUtil.getAvailableScore()));
			}
			this.mData.add(item);
		}
	}

	/**
	 * 初始化个人数据
	 */
	private void initUserInfo() {
		CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
				mAvatarIV, new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(ROUND))
				.build());
		this.mNickNameTV.setText(UserInfoUtil.getCurrentUserNickname());
		String tag = UserInfoUtil.getCurrentUserTag();
		if (tag == null || TextUtils.isEmpty(tag)) {
			this.mSignTV.setText(getResources().getString(
					R.string.txt_default_sign));
		} else {
			this.mSignTV.setText(tag);
		}
		this.mFansCountTV.setText(String.valueOf(UserInfoUtil
				.getCurrentFansCount()));
		this.mFocusCountTV.setText(String.valueOf(UserInfoUtil
				.getCurrentFocusCount()));
		this.mDynamicCountTV.setText(String.valueOf(UserInfoUtil
				.getCurrentDynamicCount()));
		UserInfoUtil.updatePersonalCenterUserInfoProgress(mProgressTV);
		mPhoneIV.setImageResource(UserInfoUtil.isBindPhone() ? R.drawable.phone_light
				: R.drawable.phone_grey);
		mSinaIV.setImageResource(WeiboApi.getInstance().isBindSina() ? R.drawable.sina_light
				: R.drawable.sina_grey);
		SystemCountMsgItem item = SystemCountMsgItem.get();
		updateDot(item.getmNewFansCount(),
				item.getmCurrentFansCount(),item.getmRecommendFriendCount());
	}

	/**
	 * 更新粉丝及消息气泡提示
	 * 
	 * @param newFansCount
	 * @param msgCount
	 * @param totalFansCount
	 */
	private void updateDot(int newFansCount, int totalFansCount, int recommendFriendCount) {
		log.d("updateDot");
		int curFansCount = 0;
		try {
			curFansCount = Integer.parseInt(mFansCountTV.getText().toString());
		} catch (Exception e) {
			curFansCount = 999;
		}
		
		if (newFansCount != 0 && newFansCount != curFansCount) {
			mNewFansDotBtn.setVisibility(View.VISIBLE);
			if (newFansCount <= 0) {
				mNewFansDotBtn.setVisibility(View.GONE);
			} else {
				if (newFansCount <= 9999) {
					mNewFansDotBtn.setText(String.valueOf(newFansCount));
				} else {
					mNewFansDotBtn.setText("9999+");
				}
				mNewFansDotBtn.setVisibility(View.VISIBLE);
			}
		}
		if (totalFansCount <= 9999) {
			mFansCountTV.setText(String.valueOf(totalFansCount));
		} else {
			mFansCountTV.setText("9999+");
		}
		mData.get(3).setRecommendCount(recommendFriendCount);
		mAdapter.notifyDataSetChanged();
//		View friendView = mListView.getChildAt(4);
//		if(friendView != null){
//			PersonalCenterViewCache friendViewCache = (PersonalCenterViewCache)friendView.getTag();
//			if(recommendFriendCount == 0){
//				friendViewCache.getmDotBtn().setVisibility(View.GONE);
//				friendViewCache.getmDotBtn().setText("0");
//			}else{
//				friendViewCache.getmDotBtn().setVisibility(View.VISIBLE);
//				if(recommendFriendCount <= 99){
//					friendViewCache.getmDotBtn().setText(String.valueOf(recommendFriendCount));
//				}else{
//					friendViewCache.getmDotBtn().setText("99+");
//				}
//			}
//		}else{
//			log.e("friendView is null");
//		}
	}
	
	private void updateMsgCount(int count){
		mData.get(2).setMsgCount(count);
		mAdapter.notifyDataSetChanged();
//		View view = mListView.getChildAt(3);
//		if (view == null) {
//			log.e("view is null");
//			return;
//		}
//		PersonalCenterViewCache viewCache = (PersonalCenterViewCache) view
//				.getTag();
//		if (count == 0) {
//			viewCache.getmDotBtn().setVisibility(View.GONE);
//		} else {
//			if (count <= 99) {
//				viewCache.getmDotBtn().setText(String.valueOf(count));
//			} else {
//				viewCache.getmDotBtn().setText("N");
//			}
//			viewCache.getmDotBtn().setVisibility(View.VISIBLE);
//		}
	}
	
//	/**
//	 * 更细未读私信数目
//	 * @param totalFansCount
//	 */
//	public void updateUnreadLetterCount(int msgCount){
//		log.d("未读私信数="+msgCount);
//		View view = mListView.getChildAt(2);
//		if (view == null) {
//			log.e("view is null");
//			return;
//		}
//		PersonalCenterViewCache viewCache = (PersonalCenterViewCache) view
//				.getTag();
//		if (msgCount == 0) {
//			viewCache.getmDotBtn().setVisibility(View.GONE);
//		} else {
//			if (msgCount < 99) {
//				viewCache.getmDotBtn().setText(String.valueOf(msgCount));
//			} else {
//				viewCache.getmDotBtn().setText("N");
//			}
//			viewCache.getmDotBtn().setVisibility(View.VISIBLE);
//		}
//	}
	
	private void checkMsgCount(){
		new Thread(){
			public void run() {
				UnreadUserLetterInfo info = new LetterDao(PersonalCenterActivity.this).countUnreadLetterAndUser();
				Message msg = mHandler.obtainMessage();
				msg.what = UPDATE_MSG_COUNT;
				msg.arg1 = info.getmTotalUnreadLetter();
				mHandler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 新粉丝数/未读私信数 接收器
	 * 
	 * @author xumengyang
	 * 
	 */
	private class DotCountReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("DotCountReceiver:onReceive");
			if (intent == null) {
				return;
			}
//			int msgCount = intent.getIntExtra(
//					Params.UPDATE_MY_DOT.NEW_CHAT_COUNT, 0);
			int newFansCount = intent.getIntExtra(
					Params.UPDATE_MY_DOT.NEW_FANS, 0);
			int totalFansCount = intent.getIntExtra(
					Params.UPDATE_MY_DOT.TOTAL_FANS_COUNT, 0);
			int recommendFriendCount = intent.getIntExtra(Params.UPDATE_MY_DOT.RECOMMEND_FRIEND_COUNT, 0);
			updateDot(newFansCount, totalFansCount,recommendFriendCount);
			checkMsgCount();
		}

	}
	
	/**
	 * 监听用户信息改变并更新UI
	 * 
	 * @author xumengyang
	 * 
	 */
	private class UserInfoListener implements OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			log.d("UserInfoListener:onSharedPreferenceChanged="+key);
			if (key.equals(Params.SP_PARAMS.KEY_TAG)) {
				String tag = UserInfoUtil.getCurrentUserTag();
				// 个性签名
				if (TextUtils.isEmpty(tag)) {
					mSignTV.setText(CyouApplication.mAppContext.getResources()
							.getString(R.string.txt_default_sign));
				} else {
					mSignTV.setText(tag);
				}
			} else if (key.equals(Params.SP_PARAMS.KEY_AVATAR)) {
				CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
						mAvatarIV, new DisplayImageOptions.Builder()
						.cacheInMemory(true)
						.cacheOnDisc(true)
						.showImageForEmptyUri(R.drawable.avatar_defaul)
						.showImageOnFail(R.drawable.avatar_defaul)
						.displayer(new RoundedBitmapDisplayer(ROUND))
						.build());
			} else if (key.equals(Params.SP_PARAMS.KEY_NICKNAME)) {
				mNickNameTV.setText(UserInfoUtil.getCurrentUserNickname());
			} else if (key.equals(Params.SP_PARAMS.KEY_PHONE)
					|| key.equals(Params.SP_PARAMS.KEY_PICORIG)
					|| key.equals(Params.SP_PARAMS.KEY_NICKNAME)
					|| key.equals(Params.SP_PARAMS.KEY_GENDER)
					|| key.equals(Params.SP_PARAMS.KEY_BIRTHDAY)
					|| key.equals(Params.SP_PARAMS.KEY_LOCAL)
					|| key.equals(Params.SP_PARAMS.KEY_HASUPDATE_SIGN)) {
				UserInfoUtil.updatePersonalCenterUserInfoProgress(mProgressTV);
				if(key.equals(Params.SP_PARAMS.KEY_PHONE)){
					mPhoneIV.setImageResource(UserInfoUtil.isBindPhone() ? R.drawable.phone_light
							: R.drawable.phone_grey);
					mData.clear();
					initArray();
					mAdapter.notifyDataSetChanged();
				}
			} else if (key.equals(Params.SP_PARAMS.TOKEN)) {
				mSinaIV.setImageResource(WeiboApi.getInstance().isBindSina() ? R.drawable.sina_light
						: R.drawable.sina_grey);
			} if(key.equals(Params.SP_PARAMS.KEY_AVAILABLE_SCORE)){//如果当前用户的积分发生变化，要相应的更新header bar button
                mData.get(1).setName(getString(R.string.scores, UserInfoUtil.getAvailableScore()));
                mAdapter.notifyDataSetChanged();
			}
		}

	}

	/**
	 * 监听用户数据改变并更新UI
	 * 
	 * @author xumengyang
	 * 
	 */
	private class UserDataListener implements OnSharedPreferenceChangeListener {

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			log.d("onSharedPreferenceChanged:key = " + key);
			if (key.equals(Params.SP_PARAMS.KEY_FOCUS)) {
				int focus = sharedPreferences.getInt(
						Params.SP_PARAMS.KEY_FOCUS, 0);
				String strFocus = focus < 999 ? String.valueOf(focus) : "999+";
				mFocusCountTV.setText(strFocus);
			} else if (key.equals(Params.SP_PARAMS.KEY_ACTS)) {
				int dynamic = sharedPreferences.getInt(
						Params.SP_PARAMS.KEY_ACTS, 0);
				String strDynamic = dynamic < 999 ? String.valueOf(dynamic)
						: "999+";
				mDynamicCountTV.setText(strDynamic);
			} else if (key.equals(Params.SP_PARAMS.KEY_AVATAR)) {
				String avatarPath = sharedPreferences.getString(
						Params.SP_PARAMS.KEY_AVATAR, null);
				CYImageLoader.displayImg(avatarPath, mAvatarIV, new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(ROUND))
				.build());
			}
		}

	}


	/**
	 * 检测是否已绑定了手机
	 * 
	 * @return
	 */
	private boolean isBindTelePhone() {
		String phoneNum = UserInfoUtil.getPhoneNumber();
		if (!TextUtils.isEmpty(phoneNum) && Util.isPhoneNum(phoneNum)) {
			return true;
		}
		return false;
	}

	public void showShareAppSelector() {
		try {
			mSharedDialog = new AlertDialog.Builder(this).setItems(
					new CharSequence[] {getString(R.string.share_app_sina),
							getString(R.string.share_app_others)},
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							switch (which) {
							case 0:
								isValidateSinaToken();
								break;
							case 1:
								Util.shareToChooseApps(PersonalCenterActivity.this, false);
								break;
							default:
								break;
							}
						}
					}).create();
			mSharedDialog.setTitle(getString(R.string.share));
			mSharedDialog.setCanceledOnTouchOutside(true);
			mSharedDialog.show();
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	private void isValidateSinaToken() {
		Intent intent = new Intent(PersonalCenterActivity.this, ShareAppToSinaActivity.class);
		startActivity(intent);
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case UPDATE_MSG_COUNT:
				int count = msg.arg1;
				updateMsgCount(count);
				break;

			default:
				break;
			}
		};
	};
}
