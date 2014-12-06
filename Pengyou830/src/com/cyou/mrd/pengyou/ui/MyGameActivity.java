package com.cyou.mrd.pengyou.ui;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.database.ContentObserver;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MyGameListAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.GameDownloadRecordDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.MyGamePlayRecordDao;
import com.cyou.mrd.pengyou.db.provider.MyGamePlayRecordProvider;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.AdvertBean;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.GuessGameItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.base.AdvertBase;
import com.cyou.mrd.pengyou.entity.base.GuessGameBase;
import com.cyou.mrd.pengyou.entity.base.MyGameBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.AsyncGameThread;
import com.cyou.mrd.pengyou.service.DownloadIntentService;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.MyGameItemViewCache;
import com.cyou.mrd.pengyou.widget.ExpandAnimation;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.CoinAnimation.CoinNumberView;
import com.cyou.mrd.pengyou.widget.CoinAnimation.CoinView;
import com.cyou.mrd.pengyou.widget.CoinAnimation.CoinView.OnAnimationEndListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyGameActivity extends CYBaseActivity implements OnClickListener{

	public static final int NUM_MYGAME_SHOW_YOUMAYLIKE = 5;// 当我的游戏数大于此数时
	private static final int LOAD_MYFAVGAME_BEGIN = 3;
	private static final int NOTIFY = 5;
	private static final int ITEM_CLICK = 6;
	private static final int LOAD_MYGAME_COMPLETE = 1;
	private static final String MY_GAME = "my_game";
	private static final String GUESS_GAME = "guess_game";
	public static final int BAR_ACTION = 8;
	public static final int SHOW_SHARE_DIALOH = 7;
	public static final int ASYNC_GAME = 9;
	public static final int CHANGE_GUESS = 10;//更换猜你喜欢
	public static final int COINS_ANIM_EXCHANGE = 1000;
	public static final int COINS_ANIM_PKG_NUMBER = 1001;
	public static final int COINS_ANIM_APP_INSTALLED = 1002;
	
	private ListView mListView;
	private LinearLayout mUserInfoLL;
	private ImageView mAvatarIV;
	private TextView mNickNameTV;
	private TextView mSignTV;
	private TextView mProgressTV;
	private LinearLayout mContainer;
	private TextView mDownloadCountTV;
	private ImageButton mDownloadIBtn;
	private TextView mDownloadCoinsTV;
	private Dialog mSharedDialog;
	private TextView mFeedBackTV;
	private Button mGiftBtn;
	private ImageView mGiftDotBtn;
	private Button mGiftPackageBtn;
	private View mGiftCoinDV;
	
	private List<GameItem> mData;
	private SharedPreferences mUserInfoSP;
	private UserInfoListener mUserInfoListener;
	private int mGuessPageNo = 1;
	private MyGameDao mMyGameDao;
	private MyGamePlayRecordDao mMyGameRecordDao;
	private MyGameListAdapter mAdapter;
	private MyGameObserver mMyGameObserver;
	private MyGamePlayRecordObserver mMyGamePlayRecordObserver;
	private DownloadCountReceiver mDownloadReceiver;
	private GameInstallReceiver mGameInstallReceiver;
	private NetStateReceiver mNetStateReceiver;
	private FragmentManager mFragmentManager;
	private GiftReceiver mGiftReceiver;
	private boolean mAnimating;
	private GameCircleMsgReceiver mGameCircleMsgReceiver;
	private boolean mHadRegistGameCircleMsgReceiver;
	private AdvertBean mAdvertBean;
	private GuessGameItem mGuessItem;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.d("onCreate");
		setContentView(R.layout.my_game);
		CyouApplication.setMyGameActivity(this);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void onDestroy() {
		log.d("onDestroy");
		super.onDestroy();
		if(mDownloadReceiver != null){
			unregisterReceiver(mDownloadReceiver);
		}
		if(mGameInstallReceiver != null){
			unregisterReceiver(mGameInstallReceiver);
		}
		if(mNetStateReceiver != null){
			unregisterReceiver(mNetStateReceiver);
		}
		if(mMyGameObserver != null){
			getContentResolver().unregisterContentObserver(mMyGameObserver);
		}
		if(mMyGamePlayRecordObserver != null){
			getContentResolver().unregisterContentObserver(mMyGamePlayRecordObserver);
		}
		if(mGiftReceiver != null){
			unregisterReceiver(mGiftReceiver);
		}
		if(mGameCircleMsgReceiver != null && mHadRegistGameCircleMsgReceiver){
			unregisterReceiver(mGameCircleMsgReceiver);
			mHadRegistGameCircleMsgReceiver = false;
		}
	}
	
	@Override
	protected void initView() {
		this.mListView = (ListView)findViewById(R.id.my_game_lstview);
		this.mUserInfoLL = (LinearLayout)findViewById(R.id.my_game_user_info_ll);
		this.mAvatarIV = (ImageView)findViewById(R.id.my_game_avatar_iv);
		this.mNickNameTV = (TextView)findViewById(R.id.my_game_nickname_tv);
		this.mSignTV = (TextView)findViewById(R.id.my_game_sign_tv);
		this.mProgressTV = (TextView)findViewById(R.id.my_game_info_progress_tv);
		this.mContainer = (LinearLayout)findViewById(R.id.my_game_container);
		this.mDownloadCountTV = (TextView)findViewById(R.id.me_header_download_count_tv);
		this.mDownloadIBtn = (ImageButton)findViewById(R.id.me_header_right_btn);
		this.mDownloadCoinsTV = (TextView)findViewById(R.id.me_download_coins_tv);
		this.mFeedBackTV = (TextView)findViewById(R.id.me_header_feedback_btn);
		this.mGiftBtn = (Button)findViewById(R.id.my_game_gift_btn);
		this.mGiftDotBtn = (ImageView)findViewById(R.id.my_game_gist_count_iv);
		this.mGiftCoinDV = (View)findViewById(R.id.my_game_gift_devide);
		SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		if(sp.getBoolean(Params.SP_PARAMS.GIFT_SHOWN, false)){
			this.mGiftDotBtn.setVisibility(View.VISIBLE);
		}else{
			this.mGiftDotBtn.setVisibility(View.GONE);
		}
		if(!(new GameDownloadRecordDao().selectList(String.valueOf(UserInfoUtil.getCurrentUserId())).isEmpty()) && NetUtil.isNetworkAvailable()) {//安装过游戏且未领积分且有网络
			this.mDownloadIBtn.setVisibility(View.GONE);
			this.mDownloadCoinsTV.setVisibility(View.VISIBLE);
		}
		this.mGiftPackageBtn = (Button)findViewById(R.id.my_game_gift_package_btn);
	}

	@Override
	protected void initEvent() {
		this.mUserInfoLL.setOnClickListener(this);
		if(UserInfoUtil.getIsCanExchangeScore()) {
			this.mAvatarIV.setOnClickListener(this);
		}
		if(mMyGameObserver == null){
			mMyGameObserver = new MyGameObserver(mHandler);
		}
		if(mMyGamePlayRecordObserver == null) {
			mMyGamePlayRecordObserver = new MyGamePlayRecordObserver(mHandler);
		}
		getContentResolver().registerContentObserver(Uri.parse(MyGameProvider.URI), true, mMyGameObserver);
		getContentResolver().registerContentObserver(Uri.parse(MyGamePlayRecordProvider.URI), true, mMyGamePlayRecordObserver);
		registAction();
		this.mDownloadIBtn.setOnClickListener(this);
		this.mFeedBackTV.setOnClickListener(this);
		this.mGiftBtn.setOnClickListener(this);
		this.mGiftPackageBtn.setOnClickListener(this);
		this.mDownloadCoinsTV.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		this.mMyGameDao = new MyGameDao(this);
		this.mMyGameRecordDao = new MyGamePlayRecordDao();
		this.mMyGameObserver = new MyGameObserver(mHandler);
		this.mFragmentManager = getSupportFragmentManager();
		this.mData = new ArrayList<GameItem>();
		if(mAdvertBean == null){
			mAdvertBean = new AdvertBean();
		}
		if(mGuessItem == null){
			mGuessItem = new GuessGameItem();
		}
		this.mAdapter = new MyGameListAdapter(this, mData, mHandler, mListView,mAdvertBean, mGuessItem);
		this.mListView.setAdapter(mAdapter);
		loadUserInfo();
		loadMyGameListFromDB(false);
		initServerSwitch();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(mFragmentManager != null){
			Fragment f = mFragmentManager.findFragmentByTag(GUESS_GAME);
			if(f != null){
				((GuessGameFragment)f).notifyData();
			}else{
				mAdapter.notifyDataSetChanged();
			}
		}
		this.mAdapter.notifyDataSetChanged();
		SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
		this.mFeedBackTV.setVisibility(sp.getBoolean(Params.SP_PARAMS.FEED_BACK_SHOWN, false) ? View.VISIBLE : View.GONE);
		CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
				mAvatarIV, new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(120))
				.build());
	}
	
	@Override
	public void onClick(final View v) {
		Intent mIntent = new Intent();
		switch (v.getId()) {
		case R.id.my_game_user_info_ll:
			mIntent.setClass(MyGameActivity.this, EditInfoAcivity.class);
			startActivity(mIntent);
			break;
		case R.id.my_game_avatar_iv:
			//只有在图像区域显示金币的时候，图像区域点击是获取金币，其它时候点击是进入个人信息界面
		if(UserInfoUtil.getIsCanExchangeScore() && UserInfoUtil.getPersonalInfoProgress() == 10){
			v.setClickable(false);
			//不执行原地转的动画，直接飞5个金币
//			mProgressTV.setBackgroundResource(R.drawable.anim_coin_small_spin);
//			final AnimationDrawable anim = (AnimationDrawable) mProgressTV.getBackground();
//			anim.start();

			final int[] start = new int[2];
			mProgressTV.getLocationOnScreen(start);
			start[0] -= mProgressTV.getWidth()/2;
			start[0] += mProgressTV.getHeight();
			
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putInt("anim_start_x", start[0]);
			b.putInt("anim_start_y", start[1]);
			b.putFloat("scale", 0.8f);
			msg.setData(b);
			msg.what = MyGameActivity.COINS_ANIM_EXCHANGE;
			if (mHandler != null) {
				mHandler.sendMessage(msg);
			}
			
			//获取金币
			RequestParams personparams = new RequestParams();
			personparams.put("act", String.valueOf(Contants.SCORE_ACTION.EXCHANGE_PERSON_INFO_SCORE));
			MyHttpConnect.getInstance().post(
					HttpContants.NET.SCORE_ACTION, personparams,
					new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(MyGameActivity.this, R.string.download_error_network_error, Toast.LENGTH_SHORT).show();
							if (mHandler != null) {
								mHandler.post(new Runnable() {
									public void run() {
										v.setClickable(true);
									}
								});
							}
							super.onFailure(error);
						}

						@Override
						public void onLoginOut() {
							LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
							dialog.create().show();
						}

						@Override
						public void onSuccess(String content) {
							if (mHandler != null) {
								mHandler.post(new Runnable() {
									public void run() {
										v.setClickable(true);
									}
								});
							}
							int n = parseScoreActionResponse(content, start, 0.8f);
							if (n > 0) {
								//一旦取积分成功，本地不再能显示个人信息完成度金币和个人信息页面的领金币提示
								UserInfoUtil.setIsCanExchangeScore(false);
							}
							//否则，金币一直显示，直等服务器问题修复
							super.onSuccess(content);
						}
					});
			}else{
				mIntent.setClass(MyGameActivity.this, EditInfoAcivity.class);
				startActivity(mIntent);
			}
			break;
		case R.id.my_game_gift_package_btn:
			//TODO
			mIntent.setClass(MyGameActivity.this, ExchangeIntegralWebActivity.class);
			startActivity(mIntent);
			break;
		case R.id.me_download_coins_tv:
			v.setClickable(false);
			
//			mDownloadCoinsTV.setBackgroundResource(R.drawable.anim_conspin);
//			final AnimationDrawable anim2 = (AnimationDrawable) mDownloadCoinsTV.getBackground();
//			anim2.start();
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			List<String> downloadRecLst = new GameDownloadRecordDao().selectList( String.valueOf(UserInfoUtil.getCurrentUserId()));
			int count = downloadRecLst.size();
			int i = 0;
			Iterator<String> it = downloadRecLst.iterator();
			while (it.hasNext()) {
				String identifier = it.next();
				log.i("下载记录 = " + identifier);
				if (i != count - 1) {
					sb.append("\"" + identifier + "\",");
				} else {
					sb.append("\"" + identifier + "\"");
				}
				i++;
			}
			sb.append("]");
			String identifiers = sb.toString();
			
			final int[] location = new int[2];
			mDownloadCoinsTV.getLocationOnScreen(location);
			location[0] += mDownloadCoinsTV.getWidth();
			//执行飞金币的动画
			Message msg = new Message();
			Bundle b = new Bundle();
			b.putInt("anim_start_x", location[0]);
			b.putInt("anim_start_y", location[1]);
			b.putFloat("scale", 0.55f);
			msg.setData(b);
			msg.what = MyGameActivity.COINS_ANIM_EXCHANGE;
			if (mHandler != null) {
				mHandler.sendMessage(msg);
			}

			RequestParams downloadparams = new RequestParams();
			downloadparams.put("identifiers", identifiers);
			log.i("identifiers = " + identifiers);
			downloadparams.put("act", String.valueOf(Contants.SCORE_ACTION.EXCHANGE_DOWNLOAD_GAME_SCORE));
			MyHttpConnect.getInstance().post(HttpContants.NET.SCORE_ACTION,
					downloadparams, new AsyncHttpResponseHandler() {
						@Override
						public void onFailure(Throwable error) {
							Toast.makeText(MyGameActivity.this,
									R.string.download_error_network_error,
									Toast.LENGTH_SHORT).show();
							super.onFailure(error);
							if(mHandler != null){
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										v.setClickable(true);
										mDownloadCoinsTV.setVisibility(View.GONE);
										mDownloadIBtn.setVisibility(View.VISIBLE);
									}
								});
							}
						}

						@Override
						public void onLoginOut() {
							LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
							dialog.create().show();
						}

						@Override
						public void onSuccess(String content) {
//							Log.e("MyGameActivity", "content = " + content);
							if(mHandler != null){
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										v.setClickable(true);
										mDownloadCoinsTV.setVisibility(View.GONE);
										mDownloadIBtn.setVisibility(View.VISIBLE);
									}
								});
							}
							parseScoreActionResponse(content, location, .8f);
						    new GameDownloadRecordDao().clean( String.valueOf(UserInfoUtil.getCurrentUserId()));
							super.onSuccess(content);
						}
					});
			break;
		case R.id.me_header_right_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.PLAYGAME.BTN_PLAYGAME_DOWNLOADMANAGER_ID,
					CYSystemLogUtil.PLAYGAME.BTN_PLAYGAME_DOWNLOADMANAGER_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			mIntent.setClass(MyGameActivity.this, DownloadActivity.class);
			startActivity(mIntent);
			break;
		case R.id.me_header_feedback_btn:
			mIntent.setClass(MyGameActivity.this, FeedBackActivity.class);
			startActivity(mIntent);
			break;
		case R.id.my_game_gift_btn:
			mIntent.setClass(MyGameActivity.this, GameGiftListActivity.class);
			startActivity(mIntent);
			SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			mGiftDotBtn.setVisibility(View.GONE);
			Editor e = sp.edit();
			e.putBoolean(Params.SP_PARAMS.GIFT_SHOWN, false);
			e.commit();
			break;
		default:
			break;
		}
	}
	
	private int parseScoreActionResponse(String content, int[]location, float scale){
		log.i("jifen content = " + content);
		int coins = -1;
		try {
			if (TextUtils.isEmpty(content)) {
				Toast.makeText(MyGameActivity.this,
						R.string.download_error_network_error,
						Toast.LENGTH_SHORT).show();
				return coins;
			}
			String successful = JsonUtils.getJsonValue(content, "successful");
			if (!TextUtils.isEmpty(successful) && "1".equals(successful)) {
				String isSuccessful = JsonUtils
						.getJsonValue(JsonUtils.getJsonValue(content, "data"),
								"isSuccessful");// 兑换成功
				if (isSuccessful.equals("true")) {
					coins = Integer.parseInt(JsonUtils.getJsonValue(
							JsonUtils.getJsonValue(content, "data"),
							"changeValue"));
					int addOrSub = Integer
							.parseInt(JsonUtils.getJsonValue(
									JsonUtils.getJsonValue(content, "data"),
									"addOrSub"));
					//这一步放在动画结束做
//					Log.e("MyGameActivity", "success coins:" + coins);
					if (coins > 0) {
//						Message msg = new Message();
//						Bundle b = new Bundle();
//						b.putInt("anim_start_x", location[0]);
//						b.putInt("anim_start_y", location[1]);
//						b.putInt("coins", coins);
//						b.putFloat("scale", scale);
//						msg.setData(b);
//						msg.what = MyGameActivity.COINS_ANIM_EXCHANGE;
//						if (mHandler != null) {
//							mHandler.sendMessage(msg);
//						}
						//直接执行+多少金币的动画
						UserInfoUtil.sumAvailableScore(coins, addOrSub);
			            Message m = Message.obtain();
			            m.what = MyGameActivity.COINS_ANIM_PKG_NUMBER;
			            m.arg1 = coins;
			            if (mHandler != null) {
			            	mHandler.sendMessage(m);
						}
					}
				} else {
//					Log.e("MyGameActivity", "success but no coins");
					String data_errorno = JsonUtils.getJsonValue(
							JsonUtils.getJsonValue(content, "data"), "errorNo");
					if (!TextUtils.isEmpty(data_errorno)) {
						switch (Integer.valueOf(data_errorno)) {
						case Contants.EXCHANGE_ERROR_NO.NOT_CONFORM_RULES:
							Toast.makeText(MyGameActivity.this,
									R.string.exchange_not_conform_rules,
									Toast.LENGTH_SHORT).show();
							// 积分获取已达到今日上限
							break;
						default:
							Toast.makeText(MyGameActivity.this,
									R.string.network_not_steady,
									Toast.LENGTH_SHORT).show(); // 网络不稳定
							break;
						}
					}
				}
				return coins;
			}
			// 如果不成功7
			if (coins <= 0) {
				Toast.makeText(MyGameActivity.this,
						R.string.download_error_network_error,
						Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			// TODO: handle exception
			log.e(e);
		}
		return coins;
	}
	
	/**
	 * 注册各种监听器
	 */
	private void registAction(){
		mUserInfoSP = getSharedPreferences(Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
		mUserInfoListener = new UserInfoListener();
		mUserInfoSP.registerOnSharedPreferenceChangeListener(mUserInfoListener);
		mDownloadReceiver = new DownloadCountReceiver();
		mGameInstallReceiver = new GameInstallReceiver();
		mNetStateReceiver = new NetStateReceiver();
		registerReceiver(mDownloadReceiver, new IntentFilter(Contants.ACTION.DOWNLOADING_COUNT));
		registerReceiver(mGameInstallReceiver, new IntentFilter(Contants.ACTION.GAME_DOWNLOAD_AND_INSTALL));
		registerReceiver(mNetStateReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
		if(mGiftReceiver == null){
			mGiftReceiver = new GiftReceiver();
		}
		registerReceiver(mGiftReceiver, new IntentFilter(Contants.ACTION.GIFT));
		if(mGameCircleMsgReceiver == null){
			mGameCircleMsgReceiver = new GameCircleMsgReceiver();
		}
		if(mGameCircleMsgReceiver != null && !mHadRegistGameCircleMsgReceiver){
			registerReceiver(mGameCircleMsgReceiver, new IntentFilter(Contants.ACTION.UPDATE_GAME_CIRCLE_MSG));
			mHadRegistGameCircleMsgReceiver = true;
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_MYGAME_COMPLETE:
				ArrayList<GameItem> list = (ArrayList<GameItem>)msg.obj;

				if(list!=null && !list.isEmpty()){
					Iterator<GameItem> it = list.iterator();
					while(it.hasNext()){
						GameItem item = it.next();
						if(Util.isInstallByread(item.getIdentifier())){
							mMyGameDao.insertOrUpdate(item);
						}else{
							it.remove();
						}
					}
					mData.clear();
					mData.addAll(list);
					mListView.setVisibility(View.VISIBLE);
					mContainer.setVisibility(View.GONE);
					if(mData.isEmpty()){
						mListView.setVisibility(View.GONE);
						mContainer.setVisibility(View.VISIBLE);
						loadGuessGame(3,false);
					}else{
						loadMyGameFromInternet();
					}
				}else{
					loadMyGameFromInternet();
				}
				break;
				
			case BAR_ACTION:
				int postion = msg.arg1;
				int first = mListView.getFirstVisiblePosition();
				barAction(postion, mListView.getChildAt(postion - first + mListView.getHeaderViewsCount()));
				break;
			
			case SHOW_SHARE_DIALOH:
				GameItem gameItem = (GameItem)msg.obj;
				if(gameItem == null){
					log.e("GameItem is null");
					return;
				}
				showShareGameSelector(gameItem);
				mSharedDialog.show();
				break;
			case ASYNC_GAME:
				List<GameItem> asyncList = (List<GameItem>)msg.obj;
				if(asyncList.isEmpty()){
					mListView.setVisibility(View.GONE);
					mContainer.setVisibility(View.VISIBLE);
					loadGuessGame(3,false);
				}else{
					mListView.setVisibility(View.VISIBLE);
					mContainer.setVisibility(View.GONE);
					mData = new ArrayList<GameItem>();
					mData.addAll(asyncList);
					mAdapter = new MyGameListAdapter(MyGameActivity.this, mData, mHandler, mListView,mAdvertBean,mGuessItem);
					mListView.setAdapter(mAdapter);
					loadGuessGame(1, true);
				}
				break;
			case CHANGE_GUESS:
				loadGuessGame(1, false);
				break;
			case COINS_ANIM_EXCHANGE:
				//建立动画层
				int[] anim_start = new int[2];
				int[] anim_end = new int[2];

				Bundle b = msg.getData();
				anim_start[0] = b.getInt("anim_start_x");
				anim_start[1] = b.getInt("anim_start_y");
				
				mGiftPackageBtn.getLocationOnScreen(anim_end);
				anim_end[0] = anim_end[0] < anim_start[0] ? anim_end[0] + mGiftPackageBtn.getWidth()/4 : anim_end[0] + mGiftPackageBtn.getWidth();
				anim_end[1] = anim_end[1] > anim_start[1] ? anim_end[1] : anim_end[1] - mGiftPackageBtn.getHeight()/2;
//				final int coins = b.getInt("coins");//动画之后加多少分
				float scale = b.getFloat("scale");
				excuteCoinsAnimation(anim_start[0], anim_start[1], anim_end[0], anim_end[1], scale);
				break;
			case COINS_ANIM_PKG_NUMBER:
				int coins_number = msg.arg1;
				final LinearLayout animLayout2 = (LinearLayout) createAnimLayout();
		        LinearLayout.LayoutParams cnv_lp = new LinearLayout.LayoutParams(
		        		mGiftPackageBtn.getWidth(),
		        		mGiftPackageBtn.getHeight());
				int[] pkg_btn = new int[2];
				mGiftPackageBtn.getLocationOnScreen(pkg_btn);
				cnv_lp.leftMargin = pkg_btn[0];
				cnv_lp.topMargin = pkg_btn[1] - mGiftPackageBtn.getHeight();
				CoinNumberView cnv = new CoinNumberView(getApplicationContext(), "+" + coins_number);
				animLayout2.addView(cnv, cnv_lp);
				
				CoinNumberView.start(getApplicationContext(),cnv, cnv);
				break;
			default:
				break;
			}
		};
	};
	
	public static int getStatusBarHeight(Context context){
        Class<?> c = null;
        Object obj = null;
        Field field = null;
        int x = 0, statusBarHeight = 0;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            obj = c.newInstance();
            field = c.getField("status_bar_height");
            x = Integer.parseInt(field.get(obj).toString());
            statusBarHeight = context.getResources().getDimensionPixelSize(x);
        } catch (Exception e1) {
            e1.printStackTrace();
        } 
        return statusBarHeight;
    }

	//点击金币就飞5个
	private void excuteCoinsAnimation(int startx, int starty, int endx, int endy, float basicScale){
		final LinearLayout animLayout = (LinearLayout) createAnimLayout();
        //把view加入动画层
		final CoinView cv = new CoinView(getApplicationContext());
		int width = Math.abs(startx - endx);
		int height = Math.abs(starty - endy);
		LinearLayout.LayoutParams cv_lp = new LinearLayout.LayoutParams(
				width,
				height);
		cv_lp.leftMargin = startx < endx ? startx : endx;
		cv_lp.topMargin = starty < endy ? starty : endy;
		animLayout.addView(cv, cv_lp);

        final long interval = 100;

        	cv.setCoinNumber(5);
//	        cv.setFrameNumber(8);
            cv.setInterval(interval);
            if (basicScale > 0) {
				cv.setBasicScale(basicScale);
			}
	        OnAnimationEndListener l = new CoinView.OnAnimationEndListener() {
				@Override
				public void onAnimationEnd() {
					//+多少金币由服务器返回来获取
//		            Message m = Message.obtain();
//		            m.what = COINS_ANIM_PKG_NUMBER;
//		            m.arg1 = coinNumber;
//		            if (mHandler != null) {
//		            	mHandler.sendMessage(m);
//					}
//					UserInfoUtil.sumAvailableScore(coinNumber, Contants.SCORE_ADD_OR_SUB.ADD);
				}
			};
			int dirx = startx < endx ? CoinView.POSITIVEX : CoinView.NEGATIVEX;
			int diry = starty < endy ? CoinView.POSITIVEY : CoinView.NEGATIVEY;
	        cv.start(dirx, diry, l);
	}
	
	private ViewGroup createAnimLayout() {
		ViewGroup rootView = (ViewGroup) getWindow().getDecorView();
		LinearLayout animLayout = new LinearLayout(MyGameActivity.this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		animLayout.setLayoutParams(lp);
		animLayout.setBackgroundResource(android.R.color.transparent);
		rootView.addView(animLayout);
		return animLayout;
	}

	/**
	 * 监听我的游戏的DB变化
	 * @author xumengyang
	 *
	 */
	private class MyGameObserver extends ContentObserver{

		public MyGameObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onChange(boolean selfChange) {
//			Log.e("MyGameObserver","MyGameObserver:onChange");
			//在某种情况下onChange会被多次调用，目前还未找到原因
			//为避免因多次调用而引起的ANR，设计AsyncGameThread单例执行游戏列表同步
			if(!AsyncGameThread.getInstance().isWorking()){
				AsyncGameThread.getInstance().startSyncGame(mHandler);
			}
		}
	}

    //监听MyGamePlayRecordDao的变化，只要insert或者delete，就刷新列表
	//insert代表有游戏玩过，需要刷新列表显示多久玩了多长时间
	//delete代表领取积分之后，需要刷新界面隐藏金币，显示启动按钮
	private class MyGamePlayRecordObserver extends ContentObserver{

		public MyGamePlayRecordObserver(Handler handler) {
			super(handler);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onChange(boolean selfChange) {
			loadMyGameListFromDB(false);
		}
	}
	
	/**
	 * 加载我的游戏
	 */
	public void loadMyGameListFromDB(final boolean needInternet) {
		if(needInternet){
			loadMyGameFromInternet();
		}else{
			new Thread() {
				@Override
				public void run() {
					log.d("Load MyGame From DB");
					List<GameItem> list = mMyGameDao.selectAll();
					Message msg = mHandler.obtainMessage();
					msg.what = LOAD_MYGAME_COMPLETE;
					msg.obj = list;
					mHandler.sendMessage(msg);
				}
			}.start();
		}
	}
	
	/**
	 * 从服务器加载我的游戏
	 */
	private void loadMyGameFromInternet() {
		log.i("Load MyGame From Internet");
		final String uid = String.valueOf(UserInfoUtil.getCurrentUserId());
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.MY_GAME, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						showWaitingDialog();
					}

					@Override
					public void onSuccess(int statusCode, final String content) {
						super.onSuccess(statusCode, content);
//						dismissWaitingDialog();
						new Thread(){
							public void run() {
								if (TextUtils.isEmpty(content)) {
									postDimissDialog();
									return;
								}
								try {
									log.d("我的游戏 = " + content);
									MyGameBase base = new ObjectMapper().configure(
													DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
													false).readValue(content,
													new TypeReference<MyGameBase>() {
													});
									log.d(">>>>>>>>>>>>>>>>>>>>>"+base.getErrorNo());
									log.d(">>>>>>>>>>>>>>>>>>>>>"+base.getSuccessful());
									if (base == null) {
										return;
									}
									List<GameItem> list = base.getData();
									
									if(list != null && !list.isEmpty()){
										if(mData.isEmpty()){
											for(GameItem item : list){
												if(Util.isInstallByread(item.getIdentifier())){
													item.setLocalVersion(String.valueOf(Util.getAppVersionCode(item.getIdentifier())));
													mMyGameDao.insertOrUpdate(item);
												}
											}
										}
										List<GameItem> dbList = mMyGameDao.selectAll();
										for(GameItem item : list){
											for(GameItem i : dbList){
												if(i.getIdentifier().equals(item.getIdentifier())){
													i.setGcacts(item.getGcacts());
													i.setGcid(item.getGcid());
													i.setTodaygcacts(item.getTodaygcacts());
													i.setNoticount(item.getNoticount());
													i.setHastop(item.getHastop());
												}
											}
										}
//										for(GameItem i : dbList){
//											i.setHasscore(mMyGameRecordDao.getGameItemScores(i.getIdentifier(),uid));
//										}
										postLoadGame(dbList);
//										mData.clear();
//										mData.addAll(dbList);
//										loadGuessGame(1,true);
//										if(mData.isEmpty()){
//											mListView.setVisibility(View.GONE);
//											mContainer.setVisibility(View.VISIBLE);
//											loadGuessGame(3,false);
//										}
									}else{
//										mListView.setVisibility(View.GONE);
//										mContainer.setVisibility(View.VISIBLE);
//										loadGuessGame(3,false);
										postLoadGuessGame();
									}
								} catch (Exception e) {
									
									log.e(e);
								}
//								postDimissDialog();
							};
						}.start();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						dismissWaitingDialog();
						if(PYVersion.DEBUG){
							showLongToast("我的游戏列表数据返回失败"+content);
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
						dialog.create().show();
					}
					
				});
	}
	
	private void postDimissDialog(){
		if (mHandler != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					dismissWaitingDialog();
				}
			});
		}
	}
	
	
	private void postLoadGame(final List<GameItem> dbList){
		if (mHandler != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					if (dbList != null) {
						mData.clear();
						mData.addAll(dbList);
//						mListView.setVisibility(View.VISIBLE);
						loadGuessGame(1,true);
						
					}
					if(mData.isEmpty()){
						mListView.setVisibility(View.GONE);
						mContainer.setVisibility(View.VISIBLE);
						loadGuessGame(3,false);
					}
//					dismissWaitingDialog();
				}
			});
		}
	}
	
	
	
	private void postLoadGuessGame(){
		if (mHandler != null) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
						mListView.setVisibility(View.GONE);
						mContainer.setVisibility(View.VISIBLE);
						loadGuessGame(3,false);
				}
			});
		}
	}
	
	/**
	 * 加载猜你喜欢
	 * @param loadAdvert 是否加载广告图
	 * xumengyang
	 */
	private void loadGuessGame(final int count,final boolean loadAdvert){
		RequestParams params = new RequestParams();
		params.put("page", String.valueOf(mGuessPageNo));
		params.put("count", String.valueOf(count));
		MyHttpConnect.getInstance().post(HttpContants.NET.GUESS_GAMS, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					log.e("content is null");
					return;
				}
				try {
					log.d("猜你喜欢 = "+content);
					GuessGameBase base = new ObjectMapper().configure(
							DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false).readValue(content,
							new TypeReference<GuessGameBase>() {
							});
					if(base == null){
						log.e("base is null");
						return;
					}
					if(count == 3){
						ArrayList<GuessGameItem> list = base.getData();
						GuessGameFragment fGuessGame = new GuessGameFragment();
						getSupportFragmentManager().beginTransaction().replace(R.id.my_game_container, fGuessGame,GUESS_GAME).commitAllowingStateLoss();
						Bundle b = new Bundle();
						b.putParcelableArrayList(GuessGameFragment.DATA, list);
						fGuessGame.setArguments(b);
					}else{
						mGuessItem = base.getData().get(0);
						mAdapter.setGuessItem(mGuessItem);
						if(loadAdvert){
							getBannerPic();
						}else{
							mAdapter.notifyDataSetChanged();
						}
					}
					mGuessPageNo++;
				} catch (Exception e) {
					log.e(e);
				}
				
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				dismissWaitingDialog();
				if(PYVersion.DEBUG){
					showLongToast("猜你喜欢数据返回失败"+content);
				}
			}
		});
	}
	
	/**
	 * 加载用户信息
	 */
	private void loadUserInfo(){
		String tag = UserInfoUtil.getCurrentUserTag();
		if(TextUtils.isEmpty(tag)){
			mSignTV.setText(CyouApplication.mAppContext.getResources().getString(R.string.txt_default_sign));
		}else{
			mSignTV.setText(tag);
		}
		CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
				mAvatarIV, new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul)
				.displayer(new RoundedBitmapDisplayer(120))
				.build());
		mNickNameTV.setText(UserInfoUtil.getCurrentUserNickname());
		int genderRes = UserInfoUtil.getCurrentUserGender() == Contants.GENDER_TYPE.BOY ? R.drawable.boy_sign : R.drawable.girl_sign;
		mNickNameTV.setCompoundDrawablesWithIntrinsicBounds(null
				, null
				, getResources().getDrawable(genderRes)
				, null);
		UserInfoUtil.updateUserInfoProgress(mProgressTV);
		mGiftPackageBtn.setText("" + UserInfoUtil.getAvailableScore());
	}
	
	/**
	 * 监听用户信息改变并更新UI
	 * @author xumengyang
	 *
	 */
	private class UserInfoListener implements OnSharedPreferenceChangeListener{

		@Override
		public void onSharedPreferenceChanged(
				SharedPreferences sharedPreferences, String key) {
			log.d("UserInfoListener:onSharedPreferenceChanged");
			if(key.equals(Params.SP_PARAMS.KEY_TAG)){
				String tag = UserInfoUtil.getCurrentUserTag();
				//个性签名
				if(TextUtils.isEmpty(tag)){
					mSignTV.setText(CyouApplication.mAppContext.getResources().getString(R.string.txt_default_sign));
				}else{
					mSignTV.setText(tag);
				}
			}else if(key.equals(Params.SP_PARAMS.KEY_AVATAR)){
				CYImageLoader.displayImg(UserInfoUtil.getCurrentUserPicture(),
						mAvatarIV, new DisplayImageOptions.Builder()
						.cacheInMemory(true)
						.cacheOnDisc(true)
						.showImageForEmptyUri(R.drawable.avatar_defaul)
						.showImageOnFail(R.drawable.avatar_defaul)
						.showStubImage(R.drawable.avatar_defaul)
						.displayer(new RoundedBitmapDisplayer(120))
						.build());
			}else if(key.equals(Params.SP_PARAMS.KEY_NICKNAME)){
				mNickNameTV.setText(UserInfoUtil.getCurrentUserNickname());
			}else if(key.equals(Params.SP_PARAMS.KEY_GENDER)){
				int genderRes = UserInfoUtil.getCurrentUserGender() == Contants.GENDER_TYPE.BOY ? R.drawable.boy_sign : R.drawable.girl_sign;
				mNickNameTV.setCompoundDrawablesWithIntrinsicBounds(null
						, null
						, getResources().getDrawable(genderRes)
						, null);
			}else if(key.equals(Params.SP_PARAMS.KEY_PHONE) 
					|| key.equals(Params.SP_PARAMS.KEY_PICORIG)
					|| key.equals(Params.SP_PARAMS.KEY_NICKNAME)
					|| key.equals(Params.SP_PARAMS.KEY_GENDER)
					|| key.equals(Params.SP_PARAMS.KEY_BIRTHDAY)
					|| key.equals(Params.SP_PARAMS.KEY_LOCAL)
					|| key.equals(Params.SP_PARAMS.KEY_HASUPDATE_SIGN)
					|| key.equals(Params.SP_PARAMS.KEY_IS_CAN_EXCHANGE_SCORE)){
				UserInfoUtil.updateUserInfoProgress(mProgressTV);
			} else if(key.equals(Params.SP_PARAMS.KEY_AVAILABLE_SCORE)){
				mGiftPackageBtn.setText(String.valueOf(UserInfoUtil.getAvailableScore()));
			}
		}
		
	}

	/**
	 * 下载数目接收器
	 * @author xumengyang
	 *
	 */
	private class DownloadCountReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int status = intent.getIntExtra(DownloadParam.STATE, -1);
			updateDownloadCount(status);
		}

	}

    //监听从朋游下载并安装的游戏
	private class GameInstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			mDownloadCoinsTV.setVisibility(View.VISIBLE);
			mDownloadIBtn.setVisibility(View.GONE);
		}
	}
	
	//网络开关判断，好去实时刷新下载btn处的金币显示与否
	private class  NetStateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				// TODO Auto-generated method stub
				ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo gprs = manager
						.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
				NetworkInfo wifi = manager
						.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
				if ((gprs.isConnected() || wifi.isConnected())
						&& !(new GameDownloadRecordDao().selectList(String.valueOf( String.valueOf(UserInfoUtil.getCurrentUserId()))).isEmpty())) {
					mDownloadCoinsTV.setVisibility(View.VISIBLE);
					mDownloadIBtn.setVisibility(View.GONE);
				} else {
					mDownloadCoinsTV.setVisibility(View.GONE);
					mDownloadIBtn.setVisibility(View.VISIBLE);
				}
				mAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
				log.e(e);
			}
		}
	}
	/**
	 * 礼包红点接收器
	 * @author xumengyang
	 *
	 */
	private class GiftReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent == null){
				return;
			}
			SharedPreferences sp = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
			Editor e = sp.edit();
			if(intent.getBooleanExtra("show", false)){
				mGiftDotBtn.setVisibility(View.VISIBLE);
				e.putBoolean(Params.SP_PARAMS.GIFT_SHOWN, true);
			}else{
				mGiftDotBtn.setVisibility(View.GONE);
				e.putBoolean(Params.SP_PARAMS.GIFT_SHOWN, false);
			}
			e.commit();
		}
		
	}
	
	/**
	 * 更新下载任务数字
	 * xumengyang
	 * 2013-9-25下午4:46:10
	 *
	 * @param status
	 */
	private void updateDownloadCount (int status) {
		Animation anim = AnimationUtils.loadAnimation(this,R.anim.download_count);
		int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
		log.d("MainActivity: game Count is:" + count);
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

	private void barAction(int postion,View view){
		int currentIndex = postion;// 处理item显示隐藏问题
		if (currentIndex >= 0 && mData.size() > currentIndex) {
			final GameItem item = mData.get(currentIndex);
				if(mAnimating){
					return;
				}
				mAnimating = true;
				if (item == null) {
					return;
				}
				if(!item.isShowBar){
					for(GameItem i : mData){
						log.d(i.getName()+"="+i.isShowBar);
						if(i.isShowBar){
							i.isShowBar = false;
							int firstVisiblePosition = mListView.getFirstVisiblePosition();
							int index = mData.indexOf(i);
							View preBarView = mListView.getChildAt(index - firstVisiblePosition + mListView.getHeaderViewsCount());
							if(preBarView != null){
								MyGameItemViewCache viewCache = (MyGameItemViewCache)preBarView.getTag();
								if(viewCache != null){
									final LinearLayout preBar = viewCache.getLayoutBar();
									if(preBar != null){
										ExpandAnimation animation = new ExpandAnimation(preBar,getResources().getDimensionPixelSize(R.dimen.my_game_list_bar_height));
										preBar.startAnimation(animation);
										animation.setAnimationListener(new AnimationListener() {
											
											@Override
											public void onAnimationStart(Animation animation) {
												// TODO Auto-generated method stub
												
											}
											
											@Override
											public void onAnimationRepeat(Animation animation) {
												// TODO Auto-generated method stub
												
											}
											
											@Override
											public void onAnimationEnd(Animation animation) {
												preBar.clearAnimation();
												int scx = mListView.getScrollX(); 
												int scy = mListView.getScrollY(); 

											}
										});
										i.isShowBar = false;
									}
								}
							}
							break;
						}
					}
				}
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_GAME_ITEM_ID,
						CYSystemLogUtil.ME.BTN_GAME_ITEM_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				item.isShowBar = !item.isShowBar;
				final View bar = view.findViewById(R.id.my_game_bar);
				ExpandAnimation animation = new ExpandAnimation(bar,getResources().getDimensionPixelSize(R.dimen.my_game_list_bar_height));
				animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						bar.clearAnimation();
						mAnimating = false;
						int scx = bar.getScrollX(); 
						int scy = bar.getScrollY(); 
//						Log.e("SSSS", "top:" + bar.getTop() + " bottom:" + bar.getBottom()
//								+ " left:" + bar.getLeft() + " right:" + bar.getRight());
//						Log.e("SSSS", "w:" + bar.getWidth() + " h:" + bar.getHeight() 
//								+ " scrollx:" + scx + " scrolly:" + scy);
					}
				});
				bar.startAnimation(animation);
		}
	}
	
	public void showShareGameSelector(final GameItem item) {
		try {
			mSharedDialog = new AlertDialog.Builder(this).setItems(
					new CharSequence[] { getString(R.string.share_game_friend),
							getString(R.string.share_game_fzone),
							getString(R.string.share_game_sina),
							getString(R.string.share_game_others)},
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent intent = new Intent();
							switch (which) {
							case 0:
								intent.setClass(MyGameActivity.this,
										ShareToFriendActivity.class);
								intent.putExtra(
										Params.SHARE_TO_FRIEND.GAME_ITEM, item);
								startActivity(intent);
								break;
							case 1:
								intent.setClass(MyGameActivity.this,
										ShareGameToFZoneAcivity.class);
								intent.putExtra(Params.GAME_INFO.GAME_NAME,
										item.getName());
								intent.putExtra(Params.GAME_INFO.GAME_CODE,
										item.getGamecode());
								intent.putExtra(Params.GAME_INFO.GAME_ICON,
										item.getIcon());
								startActivity(intent);
								break;
							case 2:
								isValidateSinaToken(item);
								break;
							case 3:
								Util.shareToChooseApps(MyGameActivity.this, false);
								break;
							default:
								break;
							}
						}
					}).create();
			mSharedDialog.setTitle(getString(R.string.share));
			mSharedDialog.setCanceledOnTouchOutside(true);
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	private void isValidateSinaToken(GameItem item) {
		Intent intent = new Intent(MyGameActivity.this, ShareGameToSinaAcivity.class);
		intent.putExtra(Params.GAME_INFO.GAME_NAME, item.getName());
		startActivity(intent);
	}
	
	/**
	 * 服务器控制的功能开关
	 */
	private void initServerSwitch(){
		isFeedBackVisible();
		isGiftVisible();
	}
	
	/**
	 * 询问服务器是否打开意见反馈
	 */
	private void isFeedBackVisible(){
		MyHttpConnect.getInstance().post(HttpContants.NET.NET_CONTANS, new RequestParams(),new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("意见反馈是否可见= "+content);
				try {
					JSONObject obj = new JSONObject(content);
					if(obj.has("data")){
						SharedPreferences settingSP = getSharedPreferences(Contants.SP_NAME.SETTING, Context.MODE_PRIVATE);
						Editor e = settingSP.edit();
						JSONObject data = obj.getJSONObject("data");
						if(data.has("feedbackdisp_profile")){
							String result = data.getString("feedbackdisp_profile");
							if(result.equals("1")){
								mFeedBackTV.setVisibility(View.VISIBLE);
								e.putBoolean(Params.SP_PARAMS.FEED_BACK_SHOWN, true);
							}else{
								mFeedBackTV.setVisibility(View.GONE);
								e.putBoolean(Params.SP_PARAMS.FEED_BACK_SHOWN, false);
							}
						}
						String feedBackHint = null;
						String feedBackFinish = null;
						if(data.has("feedbacktext_profile")){
							feedBackHint = data.getString("feedbacktext_profile");
						}
						if(data.has("feedback_finsh")){
							feedBackFinish = data.getString("feedback_finsh");
						}
						e.putString(Params.SP_PARAMS.FEED_BACK_HINT, feedBackHint);
						e.putString(Params.SP_PARAMS.FEED_BACK_FINISH, feedBackFinish);
						e.commit();
					}
				} catch (Exception e) {
					log.e(e);
				}
				
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if(PYVersion.DEBUG){
					showLongToast("意见反馈开关数据返回失败");
				}
			}
		});
	}
	
	/**
	 * 询问服务器是否打开礼包功能
	 * result = 1 显示
	 * result = 0不显示
	 */
	private void isGiftVisible(){
		MyHttpConnect.getInstance().post(HttpContants.NET.GIFT_SWITCH, new RequestParams(), new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("礼包开关 = "+content);
				try {
					String data = JsonUtils.getJsonValue(content,
							"data");
					String result = JsonUtils.getJsonValue(data
							, "isshow");
					if(result != null){
						if(result.equals("1")){
							mGiftBtn.setVisibility(View.VISIBLE);
							mGiftCoinDV.setVisibility(View.VISIBLE);
						}else{
							mGiftBtn.setVisibility(View.GONE);
							mGiftCoinDV.setVisibility(View.GONE);
						}
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(MyGameActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				if(PYVersion.DEBUG){
					showLongToast("获取礼包开关失败"+content);
				}
			}
		});
	}
	
	/**
	 * 游戏圈消息接收器
	 * @author xumengyang
	 *
	 */
	private class GameCircleMsgReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("GameCircleMsgReceiver:onReceive");
			if(mData != null && !mData.isEmpty()){
				for(GameItem item : mData){
					item.setNoticount(SystemCountMsgItem.getGameCircleMsgCount(item.getIdentifier()));
				}
				mAdapter.notifyDataSetChanged();
			}
		}
		
	}
	
	@Override
	protected void onRestart() {
		CyouApplication.setMyGameActivity(this);
		//更新主页面底部导航-找游戏红点
//		if(CyouApplication.getMainActiv()!=null){
//			CyouApplication.getMainActiv().checkGameStoreDotImage();
//		}
		super.onRestart();
	}
	
	/**
	 * 获取广告页
	 * xumengyang
	 */
	private void getBannerPic(){
		RequestParams params = new RequestParams();
		params.put("type", "scrollnotxhomepage");
		params.put("multitype", "1");
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_STORE_TOPADS, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.d("获取广告页="+content);
				try {
					AdvertBase base = new ObjectMapper().configure(
							DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
							false).readValue(content,
							new TypeReference<AdvertBase>() {
							});
					if(base != null){
						if(!base.getData().isEmpty()){
							mAdvertBean = base.getData().get(0);
							mAdapter.setAdvertBean(mAdvertBean);
							mAdapter.isBannerVisible(true);
						}else{
							mAdapter.isBannerVisible(false);
						}
					}else{
						log.e("base is null");
					}
					mAdapter.notifyDataSetChanged();
				} catch (Exception e) {
					log.e(e);
				}
				dismissWaitingDialog();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				// TODO Auto-generated method stub
				super.onFailure(error, content);
				dismissWaitingDialog();
			}
			
			@Override
			protected void onLoginOut() {
				super.onLoginOut();
				dismissWaitingDialog();
			}
		});
	}
}
