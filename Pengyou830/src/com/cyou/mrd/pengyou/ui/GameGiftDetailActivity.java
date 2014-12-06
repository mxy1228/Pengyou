package com.cyou.mrd.pengyou.ui;

import java.io.File;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.ClipboardManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameGiftCode;
import com.cyou.mrd.pengyou.entity.GameGiftListItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.CountService;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GameGiftDetailActivity extends CYBaseActivity implements OnClickListener{
	
//	private WaitingDialog mWaitingDialog;
	private ImageView mGameIconIV;
	private ImageView mGameGiftProcessIV;
	private TextView  mGameNameTV;
	private TextView  mGameTypeTV;
	private TextView  mGameSizeTV;
	private TextView  mGameGiftIntroTV;
	private TextView  mGameGiftPercentTV;
	private TextView  mGameGiftEndDate;
	private TextView  mGameGiftEmptyTV;
	private ImageButton mBackBtn;
	private ImageButton mShareBtn;
	private TextView mHeaderBarTV;
	private Button   mGameDetailBtn;
	private TextView  mGameWelcomeTV;
	private Button  mGameBtnLeft;
	private Button  mGameBtnRight;
	private View  mGameGiftBtnSp;
	private RelativeLayout  mGameGotStruct;
	private String mGiftID = "";
	private String mGiftCodeStr;
	private String mGiftName;
	private String mGameName;
	private String mGameCode;
	private MyHttpConnect mConn;
	private GameGiftListItem mGameDetail;
	private DisplayImageOptions mOptions;
	private LinearLayout  mGiftGetLy;
	private float mScale = 1.5f;
	private int mLeftBtnState = 0; // 0: 领取 1： 复制
	private int mRightBtnState = 0; // 0: 下载游戏  1：启动游戏  2: 正在下载  3：安装游戏
	private GameGiftCode  mGiftCode;
	private String  mIdentifier;
	private ProgressReceiver mProgressReceiver;
//	private InstallReceiver mInstallReceiver;
	private boolean mHadRegistProgressReceiver = false;
//	private boolean mHadRegistInstallReceiver = false;
	private DownloadDao mDownloadDao;
	public  static boolean  mGiftPkgUpdate = false;
	private boolean  mResumed = false;
	private boolean  mResumedFromRBtn = false;
	boolean isClickMultiTimes = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_gift_pkg_detail_layout);
		mGiftID = getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMEGIFTID);
		mGiftCodeStr =  getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMEGIFTCODE);
		mGiftName =  getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMEGIFTNAME);
		mIdentifier = getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMEGIFTIDENTIFIER);
		mGameName = getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMENAME);
		mGameCode = getIntent().getStringExtra(Params.DYNAMIC_DETAIL.GAMECODE);
		mDownloadDao = DownloadDao.getInstance(this);
		initView();
		initData();
	}

	@Override
	protected void onResume(){
		super.onResume();
		if (mProgressReceiver == null) {
            mProgressReceiver = new ProgressReceiver();
        }
		if (mProgressReceiver != null && !mHadRegistProgressReceiver) {
             registerReceiver(mProgressReceiver, new IntentFilter(
                     DownloadParam.UPDATE_PROGRESS_ACTION));
             mHadRegistProgressReceiver = true;
        }
//		if(mInstallReceiver == null){
//			mInstallReceiver = new InstallReceiver();
//		}
//		if (mInstallReceiver != null && !mHadRegistInstallReceiver) {
//            registerReceiver(mInstallReceiver, new IntentFilter(
//                    Intent.ACTION_PACKAGE_ADDED));
//            registerReceiver(mInstallReceiver, new IntentFilter(
//                    Intent.ACTION_PACKAGE_REMOVED));
//            mHadRegistInstallReceiver = true;
//       }
		 if(mResumed && mResumedFromRBtn){
			 mResumedFromRBtn = false;
			 getPkgDownloadState();
		 }
		 if(!mResumed){
			 mResumed = true;
			 getGameGiftDetailInfo();
		 }

	}
	
	@Override
	protected void onDestroy(){
		super.onDestroy();
		if (mProgressReceiver != null && mHadRegistProgressReceiver) {
            unregisterReceiver(mProgressReceiver);
            mHadRegistProgressReceiver = false;
        }
//		if (mInstallReceiver != null && mHadRegistInstallReceiver) {
//            unregisterReceiver(mInstallReceiver);
//            mHadRegistInstallReceiver = false;
//        }
	}
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
//		this.mWaitingDialog = new WaitingDialog(this);
//		View headerBar = findViewById(R.id.game_gift_detail_headerbar);
		this.mHeaderBarTV = (TextView)findViewById(R.id.game_gift_detail_sub_header_bar_tv);
		if(!TextUtils.isEmpty(mGiftName))
		    this.mHeaderBarTV.setText(mGiftName);
		this.mBackBtn = (ImageButton)findViewById(R.id.game_gift_detail_sub_header_bar_left_ibtn);
		this.mBackBtn.setOnClickListener(this);
		this.mShareBtn = (ImageButton)findViewById(R.id.game_gift_detail_sub_header_bar_right_ibtn);
		this.mShareBtn.setOnClickListener(this);
		this.mGameIconIV = (ImageView)findViewById(R.id.game_gift_pkg_detail_avatar);
		this.mGameNameTV = (TextView)findViewById(R.id.game_gift_pkg_detail_gamename);
		this.mGameNameTV.setText(mGameName);
		this.mGameTypeTV = (TextView)findViewById(R.id.game_gift_pkg_detail_gametype);
		this.mGameSizeTV = (TextView)findViewById(R.id.game_gift_pkg_detail_gamesize);
		this.mGameGiftIntroTV = (TextView)findViewById(R.id.game_gift_pkg_detail_gameintro);
		this.mGameGiftPercentTV = (TextView)findViewById(R.id.game_gift_pkg_detail_giftpercent);
		this.mGameGiftProcessIV = (ImageView)findViewById(R.id.game_gift_pkg_detail_giftprocess); 
		this.mGameDetailBtn = (Button)findViewById(R.id.game_gift_pkg_detail_gamedetail);
		this.mGameDetailBtn.setOnClickListener(this);
		this.mGameDetailBtn.setClickable(false);
		this.mGameWelcomeTV = (TextView)findViewById(R.id.game_gift_pkg_detail_welcome);
		this.mGameBtnLeft = (Button)findViewById(R.id.game_gift_pkg_detail_gift_btnleft);
		this.mGameBtnRight = (Button)findViewById(R.id.game_gift_pkg_detail_gift_btnright);
		this.mGameGiftBtnSp = (View)findViewById(R.id.game_gift_pkg_detail_gift_btnspace);
		this.mGameBtnLeft.setOnClickListener(this);
		this.mGameBtnRight.setOnClickListener(this);
		this.mGiftGetLy =  (LinearLayout)findViewById(R.id.game_gift_pkg_detail_giftbtn_ly);
		this.mGameGotStruct = (RelativeLayout)findViewById(R.id.game_gift_pkg_detail_got_st);
		this.mGameGiftEndDate = (TextView)findViewById(R.id.game_gift_pkg_detail_enddate);
		this.mGameGiftEmptyTV = (TextView)findViewById(R.id.game_gift_pkg_detail_empty);
		
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void initData() {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mGameDetail == null) {
			mGameDetail = new  GameGiftListItem();
		}
		if(mGiftCode == null){
			mGiftCode = new GameGiftCode();
		}
		if(mOptions == null){
			this.mOptions = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.icon_default)
			.showImageForEmptyUri(R.drawable.icon_default)
			.showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
			.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new RoundedBitmapDisplayer(15)).build();
		}
		this.mScale  = Util.getScreenDensity(this);
		mGiftPkgUpdate = false;
//		if(Util.isNetworkConnected(GameGiftDetailActivity.this)){
////			this.mWaitingDialog.show();
//			getGameGiftDetailInfo();
//        }
//		else {
//			this.mGameGiftEmptyTV.setVisibility(View.VISIBLE);
//			showShortToast(R.string.networks_available);
//		}
		
	}
	
	private  boolean  isInstalled(String pkg){
		return Util.isInstallByread(pkg);
	}
	
	private void getGameGiftDetailInfo(){
		RequestParams params = new RequestParams();
		params.put("giftid", mGiftID);
		mConn.post(HttpContants.NET.GAME_GIFT_INFO, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
						try {
							showNetErrorDialog(GameGiftDetailActivity.this,
									new ReConnectListener() {
										@Override
										public void onReconnect() {
											getGameGiftDetailInfo();
										}
									});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameGiftDetailActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.v("statusCode = " + statusCode);
						log.i("result = " + content);
						try {
							if (!TextUtils.isEmpty(content)) {
								try {
									JSONObject obj = new JSONObject(content);
									if(!obj.has("data")){
//										mWaitingDialog.dismiss();
										return;
									}
									String json = JsonUtils.getJsonValue(
											content, "data");
									if(TextUtils.isEmpty(json)){
										mGameGiftEmptyTV.setVisibility(View.VISIBLE);
										return;
									}
									mGameDetail = new ObjectMapper().configure(
													DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue(json,
													new TypeReference<GameGiftListItem>() {
													});
//									mWaitingDialog.dismiss();
									if (mGameDetail != null) {										
										update();
									}
								} catch (Exception e) {
									log.e(e);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
	
	private void update(){
		this.mHeaderBarTV.setText(mGameDetail.getGiftname());
		CYImageLoader.displayIconImage(mGameDetail.getGame().getIcon(),
				mGameIconIV, mOptions);
		mGameGiftEmptyTV.setVisibility(View.GONE);
		this.mGameGotStruct.setVisibility(View.VISIBLE);
		this.mGameDetailBtn.setClickable(true);
		if(TextUtils.isEmpty(mGiftName))
		    this.mHeaderBarTV.setText(mGameDetail.getGiftname());
		this.mGameNameTV.setText(mGameDetail.getGame().getName());
		this.mGameTypeTV.setText(getString(R.string.game_gift_game_type,mGameDetail.getGame().getGtname()));
		this.mGameSizeTV.setText(getString(R.string.game_gift_game_size,Util.getGameSize(mGameDetail.getGame().getFullsize())));
		this.mGameGiftEndDate.setText(getString(R.string.game_gift_game_date,Util.getDateForH(mGameDetail.getEndtime())));
		this.mGameGiftIntroTV.setText(Html.fromHtml(mGameDetail.getGiftdesc()));
		int percent =  0 ;
		if(mGameDetail.getGiftnum() > 0){
			percent = (mGameDetail.getLastnum() * 100 )/mGameDetail.getGiftnum();
		}
		mGameGiftPercentTV.setText(String.valueOf(percent) + "%");
		LayoutParams  params  = (LayoutParams)mGameGiftProcessIV.getLayoutParams();			
		params.width = (int)(mScale*percent*0.85f);
		mGameGiftProcessIV.setLayoutParams(params);
		if (TextUtils.isEmpty(mGiftCodeStr)) {
			if (TextUtils.isEmpty(mGameDetail.getUsergift())) {
				this.mGameWelcomeTV.setText(R.string.game_gift_game_wel_got);
				this.mGameBtnRight.setVisibility(View.GONE);
				this.mGameBtnLeft.setText(R.string.game_gift_game_got);
				if(!UserInfoUtil.isBindPhone()){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_nobind);
				}
				else if(System.currentTimeMillis()/1000 > mGameDetail.getEndtime()){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_outdate);
				}
				else if(mGameDetail.getLastnum() == 0){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_nothing);
				}
				this.mGameGiftBtnSp.setVisibility(View.GONE);
				RelativeLayout.LayoutParams pams = (RelativeLayout.LayoutParams) (mGiftGetLy
						.getLayoutParams());
				pams.setMargins(((int) (mScale * 94)), ((int) (mScale * 34)),
						((int) (mScale * 94)), ((int) (mScale * 18)));
				this.mGiftGetLy.setLayoutParams(pams);
				mLeftBtnState = 0;
			} else {
				this.mGameWelcomeTV.setText(getString(R.string.game_gift_game_got_success_number, mGameDetail.getUsergift()));
				this.mGameBtnRight.setVisibility(View.VISIBLE);
				this.mGameGiftBtnSp.setVisibility(View.VISIBLE);
				this.mGameBtnLeft.setText(R.string.game_gift_game_copy);
				RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) (mGiftGetLy
						.getLayoutParams());
				param.setMargins(((int) (mScale * 48)), ((int) (mScale * 34)),
						((int) (mScale * 48)), ((int) (mScale * 18)));
				this.mGiftGetLy.setLayoutParams(param);
				mLeftBtnState = 1;
			}
		}
		else {
			this.mGameWelcomeTV.setText(getString(R.string.game_gift_game_got_success_number, mGiftCodeStr));
			this.mGameBtnRight.setVisibility(View.VISIBLE);
			this.mGameGiftBtnSp.setVisibility(View.VISIBLE);
			this.mGameBtnLeft.setText(R.string.game_gift_game_copy);
			RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) (mGiftGetLy
					.getLayoutParams());
			param.setMargins(((int) (mScale * 48)), ((int) (mScale * 34)),
					((int) (mScale * 48)), ((int) (mScale * 18)));
			this.mGiftGetLy.setLayoutParams(param);
			mLeftBtnState = 1;
		}
		getPkgDownloadState();
			
	}
	
	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.game_gift_detail_sub_header_bar_left_ibtn:
			GameGiftDetailActivity.this.finish();
			break;
		case R.id.game_gift_detail_sub_header_bar_right_ibtn:
			Util.shareToChooseApps(GameGiftDetailActivity.this, true);
			break;
		case R.id.game_gift_pkg_detail_gamedetail:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_GAME_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent mIntent = new Intent();
			mIntent.setClass(GameGiftDetailActivity.this, GameCircleDetailActivity.class);
			mIntent.putExtra(Params.INTRO.GAME_CODE, mGameDetail.getGame().getGamecode());
			mIntent.putExtra(Params.INTRO.GAME_NAME,  mGameDetail.getGame().getName());
			mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
			startActivity(mIntent);
			break;
		case R.id.game_gift_pkg_detail_gift_btnleft:
			log.i("-------------------------  mLeftBtnState:" + mLeftBtnState);
			switch (mLeftBtnState) {
			    case 0:
				    getGiftPkg();
				    break;
			    case 1:
				    copyToClip();
				    break;
			    default:
				    break;
			}
			break;
		case R.id.game_gift_pkg_detail_gift_btnright:
			log.i("-------------------------  mRightBtnState:" + mRightBtnState);
			mResumedFromRBtn = true;
			switch (mRightBtnState) {
		    case 0:
		    	downLoadGame();
			    break;
		    case 1:
				if(isClickMultiTimes){
					return;
				}else{
					isClickMultiTimes = true;
					v.setClickable(false);
					CountDownTimer timer = new CountDownTimer(2000, 2000) {
						/**
						 * 提醒时间到，调用此方法
						 */
						public void onTick(long millisUntilFinished) {
						}
						/**
						 * 定时时间到，调用此方法
						 */
						public void onFinish() {
							isClickMultiTimes = false;
							v.setClickable(true);
						}
					};
					// 开启定时器
					timer.start();
				}
		    	startGame(true);
			    break;
		    case 2:
		    	startActivity(new Intent(this,
                        DownloadActivity.class));
		    	break;
		    case 3:
		    	installGame();
		    default:
			    break;
		}
		break;
		default:
			break;
		}
	}

	private int getPkgDownloadState(){
		 if(mGameDetail.getGame() != null && !TextUtils.isEmpty(mGameDetail.getGame().getIdentifier()) && !TextUtils.isEmpty(mGameDetail.getGame().getVersion())){
			 boolean  mNotGameInstlled = true;
			 if(isInstalled(mGameDetail.getGame().getIdentifier())){
				  mGameBtnRight.setText(R.string.game_gift_game_start);
				  mRightBtnState = 1;
				  return  0; //installed
			 }
			 DownloadItem downloadItem = mDownloadDao.getDowloadItem(
					 mGameDetail.getGame().getIdentifier(), mGameDetail.getGame().getVersion());

			 if (downloadItem != null
	                 && !TextUtils.isEmpty(downloadItem
	                         .getmPackageName())){
	          	 log.i("--------getPkgDownState--------  mRightBtnState:" + mRightBtnState);
				 switch (downloadItem.getmState()) {
	             case DownloadParam.C_STATE.DOWNLOADING:
	             case DownloadParam.C_STATE.PAUSE:
	             case DownloadParam.C_STATE.WAITING:
	            	  mRightBtnState= 2;
	             	  mGameBtnRight.setText(getString(R.string.game_gift_game_downloading,String.valueOf(downloadItem.getmPercent()) + "%"));
	            	  return  1; //downloading
	             case DownloadParam.C_STATE.DONE:
	            	  mRightBtnState= 3;
					  mGameBtnRight.setText(R.string.game_gift_game_install);
	            	  return 2;  //downloaded
	             case DownloadParam.C_STATE.INSTALLED:
	            	 if(mNotGameInstlled){// must be not instaled here
	            		 mDownloadDao.delete(mGameDetail.getGame().getIdentifier(),mGameDetail.getGame().getVersion());
	            		 mRightBtnState= 0;
	    				 mGameBtnRight.setText(R.string.game_gift_game_download);
	    				 return  3;
	            	 }
	            	 mGameBtnRight.setText(R.string.game_gift_game_start);
					 mRightBtnState = 1;
					 return  0; //installed
	             default:
	            	  mRightBtnState= 0;
					  mGameBtnRight.setText(R.string.game_gift_game_download);
	            	  return 3;
	             }
			 }
			 else{
				 mRightBtnState= 0;
				 mGameBtnRight.setText(R.string.game_gift_game_download);
				 return 3; //not download
			 } 
		 }
		 else {
			 mRightBtnState= 0;
			 mGameBtnRight.setText(R.string.game_gift_game_download);
			 Toast.makeText(this, R.string.download_url_error,
						Toast.LENGTH_SHORT).show();
			 mGameBtnRight.setClickable(false);
			 return 4; //not download
		 }
		
	}
	
	private  void  downLoadGame(){
		log.i("---------downLoadGame------ start---------  mRightBtnState:" + mRightBtnState);
//		if(isInstalled(mGameDetail.getGame().getIdentifier())){
//			log.i("---------downLoadGame-----installed--------  mRightBtnState:" + mRightBtnState);
//			startGame(false);
//            log.i("---------downLoadGame-----after start--------  mRightBtnState:" + mRightBtnState);
//        	ContentValues values = new ContentValues();
//			values.put(DBHelper.MYGAME.PACKAGE_NAME, mGameDetail.getGame().getIdentifier());
//			getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
//            playGame(mGameDetail.getGame().getGamecode(), mGameDetail.getGame().getName());
//            log.i("---------downLoadGame-----after palygame--------  mRightBtnState:" + mRightBtnState);
//		}
//		else {
//			log.i("---------downLoadGame-----not installed --------  mRightBtnState:" + mRightBtnState);
//			 Intent intent = new Intent(this, DownloadService.class);
//			// 判断下载状态
//            DownloadItem downloadItem = mDownloadDao.getDowloadItem(
//            		mGameDetail.getGame().getIdentifier(), mGameDetail.getGame().getVersion());
//
//            if (downloadItem != null
//                    && !TextUtils.isEmpty(downloadItem
//                            .getmPackageName())) {
//            	log.i("---------downLoadGame-----not installed --------  downloadItem.getmState():" + downloadItem.getmState());
//                switch (downloadItem.getmState()) {
//                case DownloadParam.C_STATE.DOWNLOADING:
//                	log.i("---------downLoadGame-----DOWNLOADING --------  mRightBtnState:" + mRightBtnState);
//                	mRightBtnState= 2;
//                	mGameBtnRight.setText(getString(R.string.game_gift_game_downloading,String.valueOf(downloadItem.getmPercent()) + "%"));
//					startActivity(new Intent(this,
//	                            DownloadActivity.class));
//                    break;
//                case DownloadParam.C_STATE.PAUSE:
//                	log.i("---------downLoadGame-----PAUSE --------  mRightBtnState:" + mRightBtnState);
//                	mRightBtnState= 2;
//                	mGameBtnRight.setText(getString(R.string.game_gift_game_downloading,String.valueOf(downloadItem.getmPercent()) + "%"));
//                    startActivity(new Intent(this,
//                            DownloadActivity.class));
//                    break;
//                case DownloadParam.C_STATE.WAITING:
//                	log.i("---------downLoadGame-----WAITING --------  mRightBtnState:" + mRightBtnState);
//                	mRightBtnState= 2;
//                	mGameBtnRight.setText(getString(R.string.game_gift_game_downloading,String.valueOf(downloadItem.getmPercent()) + "%"));
//                    startActivity(new Intent(this,
//                            DownloadActivity.class));
//                    break;
//                case DownloadParam.C_STATE.DONE:
//                	log.i("---------downLoadGame-----DONE --------  mRightBtnState:" + mRightBtnState);
//                	mRightBtnState= 1;
//					mGameBtnRight.setText(R.string.game_gift_game_start);
//					installGame(downloadItem);
//                    break;
//                }
//            } else {
//                if (!Util.isDownloadUrl(mGameDetail.getGame().getFullurl())
//                        || TextUtils.isEmpty(mGameDetail.getGame().getVersion())) {
//                    Toast.makeText(this,
//                            R.string.download_url_error,
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                log.i("---------downLoadGame-----ADD --------  mRightBtnState:" + mRightBtnState);
//                mRightBtnState= 2;
//				mGameBtnRight.setText(getString(R.string.game_gift_game_downloading, "0%"));
//                intent.putExtra(DownloadParam.STATE,DownloadParam.TASK.ADD);
//                downloadItem = new DownloadItem();
//                downloadItem.setmName(mGameDetail.getGame().getName());
//                downloadItem.setmLogoURL(mGameDetail.getGame().getIcon());
//                downloadItem.setmSize(mGameDetail.getGame().getFullsize());
//                downloadItem.setmURL(mGameDetail.getGame().getFullurl());
//                downloadItem.setGameCode(mGameDetail.getGame().getGamecode());
//                downloadItem.setmPackageName(mGameDetail.getGame().getIdentifier());
//                downloadItem.setVersionName(mGameDetail.getGame().getVersion());
//                intent.putExtra(DownloadParam.DOWNLOAD_ITEM,downloadItem);
//                startService(intent);
//            }
//        
//		}
//		
//		if(getPkgDownloadState() == 4){
//			Toast.makeText(this, R.string.download_url_error,
//					Toast.LENGTH_SHORT).show();
//			return;
//		}
		
		if (!Util.isDownloadUrl(mGameDetail.getGame().getFullurl())
				|| TextUtils.isEmpty(mGameDetail.getGame().getVersion())) {
			Toast.makeText(this, R.string.download_url_error,
					Toast.LENGTH_SHORT).show();
			return;
		}
		//if(getPkgDownloadState() == 3){
			Intent intent = new Intent(this, DownloadService.class);
			mRightBtnState = 2;
			mGameBtnRight.setText(getString(R.string.game_gift_game_downloading,
					"0%"));
			intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.ADD);
			DownloadItem downloadItem = new DownloadItem();
			downloadItem.setmName(mGameDetail.getGame().getName());
			downloadItem.setmLogoURL(mGameDetail.getGame().getIcon());
			downloadItem.setmSize(mGameDetail.getGame().getFullsize());
			downloadItem.setmURL(mGameDetail.getGame().getFullurl());
			downloadItem.setGameCode(mGameDetail.getGame().getGamecode());
			downloadItem.setmPackageName(mGameDetail.getGame().getIdentifier());
			downloadItem.setVersionName(mGameDetail.getGame().getVersion());
			intent.putExtra(DownloadParam.DOWNLOAD_ITEM, downloadItem);
			startService(intent);
			log.i("---------downLoadGame-----ADD --------  mRightBtnState:"
					+ mRightBtnState);
		//}


	}
	
	@SuppressWarnings("all")
	 private void playGame(String gameCode, String gameName) {
	        MyHttpConnect myHttpConnect = MyHttpConnect.getInstance();
	        RequestParams params = new RequestParams();
	        params.put("gamecode", gameCode);
	        params.put("gamename", gameName);
	        myHttpConnect.post2Json(HttpContants.NET.PLAY_GAME, params,
	                new JSONAsyncHttpResponseHandler(
	                        JSONAsyncHttpResponseHandler.RESULT_BOOEALN, null) {
	                });
	        
	    }
	
//	private void  deleteDownload(){
//		if(mDownloadItem != null && mGameDetail != null){			
//			if (mDownloadDaodao.isHasInfo(mGameDetail.getGame().getIdentifier(),mGameDetail.getGame().getVersion())){	
//				Intent iDownload = new Intent(GameGiftDetailActivity.this,
//						DownloadService.class);	
//				iDownload.putExtra(DownloadParam.STATE,
//						DownloadParam.TASK.DELETE);
//				iDownload.putExtra(DownloadParam.DOWNLOAD_ITEM,
//						mDownloadItem);
//				startService(iDownload);
//				mRightBtnState= 0;
//			}
//		}
//	}

	private void  installGame(){
		 DownloadItem downloadItem = mDownloadDao.getDowloadItem(
				 mGameDetail.getGame().getIdentifier(), mGameDetail.getGame().getVersion());
		 if(downloadItem == null || TextUtils.isEmpty(downloadItem.getPath())){
			 Toast.makeText(
	                    this,
	                    getText(R.string.download_install_file_noexit),
	                    Toast.LENGTH_SHORT).show();
			 mRightBtnState= 0;
			 mGameBtnRight.setText(R.string.game_gift_game_download);
			 return;
			 
		 }
		log.i("--------------installGame --------  downloadItem.getPath():" + downloadItem.getPath());

		Intent intent = new Intent(this, DownloadService.class);
		Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        File file = new File(downloadItem.getPath());
        if (file.exists()) {
            i.setDataAndType(Uri.fromFile(file),
                    "application/vnd.android.package-archive");
            startActivity(i);
        } else {
            Toast.makeText(
                    this,
                    getText(R.string.download_install_file_noexit),
                    Toast.LENGTH_SHORT).show();
            intent.putExtra(DownloadParam.STATE,
                    DownloadParam.TASK.DELETE);
            intent.putExtra(DownloadParam.DOWNLOAD_ITEM,
                    downloadItem);
            startService(intent);
            mRightBtnState= 0;
			mGameBtnRight.setText(R.string.game_gift_game_download);
        }
	}
	
	private void copyToClip(){
		try {
			ClipboardManager cmb = (ClipboardManager)this.getSystemService(Context.CLIPBOARD_SERVICE); 
			if(!TextUtils.isEmpty(mGiftCodeStr)){
				cmb.setText(mGiftCodeStr);  
			}
			else if (!TextUtils.isEmpty(mGiftCode.getUsergift())){
			    cmb.setText(mGiftCode.getUsergift().trim()); 
			}
			else {
				cmb.setText(mGameDetail.getUsergift().trim()); 
			}
			showShortToast("复制成功");
		} catch (Exception e) {
			// TODO: handle exception
			showShortToast("复制失败");
		}
	}
	
	private void startGame(boolean checkInstalled) {
		if (mGameDetail.getGame() != null
				&& !TextUtils.isEmpty(mGameDetail.getGame().getIdentifier())) {
            if(checkInstalled && !isInstalled(mGameDetail.getGame().getIdentifier())){
            	showShortToast("未安装该游戏");
            	mRightBtnState= 0;
            	mGameBtnRight.setText(R.string.game_gift_game_download);
            	return;
            }
			PackageManager manager = getPackageManager();
			Intent iStart = manager.getLaunchIntentForPackage(mGameDetail
					.getGame().getIdentifier());
			if (iStart != null) {
				startActivity(iStart);
				//启动统计Service
				Intent jifen_intent = new Intent(GameGiftDetailActivity.this, CountService.class);
				jifen_intent.putExtra("identifier", mGameDetail.getGame().getIdentifier());
				startService(jifen_intent);
				ContentValues values = new ContentValues();
				values.put(DBHelper.MYGAME.PACKAGE_NAME, mGameDetail.getGame().getIdentifier());
				getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
				playGame(mGameDetail.getGame().getGamecode(), mGameDetail.getGame().getName());
			} else {
				showShortToast("该游戏已启动");
			}

		} else if (!TextUtils.isEmpty(mIdentifier) && isInstalled(mIdentifier)) {
			PackageManager manager = getPackageManager();
			Intent iStart = manager.getLaunchIntentForPackage(mIdentifier);
			if (iStart != null) {
				startActivity(iStart);
				ContentValues values = new ContentValues();
				values.put(DBHelper.MYGAME.PACKAGE_NAME, mGameName);
				getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
				playGame(mGameCode, mGameName);
			} else {
				showShortToast("该游戏已启动");
			}
		} else {
			mRightBtnState= 0;
        	mGameBtnRight.setText(R.string.game_gift_game_download);
			showShortToast("未安装该游戏");
		}

	}
	
	private void updateGettingState(){
		if(mGiftCode != null){
			if(!TextUtils.isEmpty(mGiftCode.getReceived())){
				if(mGiftCode.getReceived().equals("0")){
					showShortToast("领取成功！");
					this.mGameWelcomeTV.setText(getString(R.string.game_gift_game_got_success_number, mGiftCode.getUsergift()));
					this.mGameBtnRight.setVisibility(View.VISIBLE);
					this.mGameGiftBtnSp.setVisibility(View.VISIBLE);
					this.mGameBtnLeft.setText(R.string.game_gift_game_copy);
					RelativeLayout.LayoutParams  param = (RelativeLayout.LayoutParams)(mGiftGetLy.getLayoutParams());
					param.setMargins(((int)(mScale*48)), ((int)(mScale*34)), ((int)(mScale*48)), ((int)(mScale*18)));
					this.mGiftGetLy.setLayoutParams(param);
					mLeftBtnState = 1;
					updateLeftNumber(mGiftCode.getLastnum());
					mGiftPkgUpdate = true;
				}
				else if(mGiftCode.getReceived().equals("1")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_noexist);
					showShortToast("礼包不存在！");
				}
				else if(mGiftCode.getReceived().equals("2")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_notopen);
					showShortToast("礼包未开启！");
				}
				else if(mGiftCode.getReceived().equals("3")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_outdate);
					showShortToast("礼包已过期！");
				}
				else if(mGiftCode.getReceived().equals("4")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_end);
					showShortToast("礼包已领取！");
				}
				else if(mGiftCode.getReceived().equals("5")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_nothing);
					showShortToast("礼包已发完！");
				}
				else if(mGiftCode.getReceived().equals("6")){
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_failed);					
					showShortToast("礼包没抽到！");
				}
				else if(mGiftCode.getReceived().equals("7")){
					this.mGameBtnLeft.setBackgroundResource(R.drawable.game_giftpkg_get_btn_press);
					this.mGameBtnLeft.setTextColor(Color.parseColor("#a3a3a3"));
					this.mGameBtnLeft.setClickable(false);
					this.mGameWelcomeTV.setText(R.string.game_gift_game_got_nobind);
					showShortToast("未绑定手机号！");
				}
			}
			
		}
	}
	
	private void  updateLeftNumber(int number){
		int percent =  0 ;
		if(mGameDetail.getGiftnum() > 0){
			percent = (number * 100 )/mGameDetail.getGiftnum();
		}
		mGameGiftPercentTV.setText(String.valueOf(percent) + "%");
		LayoutParams  params  = (LayoutParams)mGameGiftProcessIV.getLayoutParams();			
		params.width = (int)(mScale*percent*0.85f);
		mGameGiftProcessIV.setLayoutParams(params);
	}

	private void getGiftPkg(){
		RequestParams params = new RequestParams();
		params.put("giftid", mGiftID);
		mConn.post(HttpContants.NET.GAME_GET_GIFT_CODE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
//						mWaitingDialog.show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						showShortToast(R.string.networks_available);
//						mWaitingDialog.dismiss();
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameGiftDetailActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.v("statusCode = " + statusCode);
						log.i("result = " + content);
						try {
//							mWaitingDialog.dismiss();
							if (!TextUtils.isEmpty(content)) {
								try {
									JSONObject obj = new JSONObject(content);
									if(!obj.has("data")){
										
										return;
									}
									String json = JsonUtils.getJsonValue(
											content, "data");
									mGiftCode = new ObjectMapper().configure(
													DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,false).readValue(json,
													new TypeReference<GameGiftCode>() {
													});
									if (mGiftCode != null) {										
										updateGettingState();
									}
								} catch (Exception e) {
									log.e(e);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
	
	private class ProgressReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if (intent != null) {
				try {
					if (mGameDetail == null || mGameDetail.getGame() == null
							|| mDownloadDao == null || mGameBtnRight == null) {
						return;
					}
					Integer gameStatus = intent.getIntExtra(
							DownloadParam.STATE, 100);
					int percent = intent.getIntExtra(DownloadParam.PERCENT, 0);
					String gameString = intent
							.getStringExtra(DownloadParam.GAMESTRING);
					String packagename = intent
							.getStringExtra(DownloadParam.PACKAGE_NAME);
					if (gameString != null
							&& gameString.equals(mGameDetail.getGame()
									.getIdentifier()
									+ mGameDetail.getGame().getVersion())
							|| (packagename != null && packagename
									.equals(mGameDetail.getGame()
											.getIdentifier()))) {
						log.i("--------------onReceive -------- state: " + gameStatus);
						switch (gameStatus) {
						case DownloadParam.TASK.DONE:
							log.i("--------------onReceive -------- DONE ");
							mGameBtnRight
									.setText(R.string.game_gift_game_install);
							mRightBtnState = 3;
//							DownloadItem downloadItem = mDownloadDao
//									.getDowloadItem(mGameDetail.getGame()
//											.getIdentifier(), mGameDetail
//											.getGame().getVersion());
//							installGame(downloadItem);
							break;
						case DownloadParam.C_STATE.DOWNLOADING:
						case DownloadParam.TASK.ADD:
						case DownloadParam.TASK.PAUSE:
						case DownloadParam.TASK.PAUSE_ALL:
						case DownloadParam.TASK.CONTINUE:
						case DownloadParam.TASK.CONTINUE_ALL:
						case DownloadParam.TASK.WAITING_PAUSE:
						case DownloadParam.C_STATE.WAITING:
							log.i("--------------onReceive --------DOWNLOADING ");
							mGameBtnRight.setText(getString(
									R.string.game_gift_game_downloading,
									String.valueOf(percent) + "%"));
							mRightBtnState = 2;
							break;
						case DownloadParam.TASK.DELETE:
							log.i("--------------onReceive --------DELETE ");
							mGameBtnRight
									.setText(R.string.game_gift_game_download);
							mRightBtnState = 0;
							break;

						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			}
		}
		
	}
	
	@Override
	public boolean  onKeyDown(int keyCode ,KeyEvent event){	
		return  super.onKeyDown(keyCode, event);
		
	}

//	public void showShareAppSelector() {
//		try {
//			AlertDialog mSharedDialog = new AlertDialog.Builder(this).setItems(
//					new CharSequence[] {getString(R.string.share_app_sina),
//							getString(R.string.share_app_others)},
//					new DialogInterface.OnClickListener() {
//
//						@Override
//						public void onClick(DialogInterface dialog, int which) {
//							switch (which) {
//							case 0:
//								isValidateSinaToken(mGameName);
//								break;
//							case 1:
//								Util.shareToChooseApps(GameGiftDetailActivity.this, true);
//								break;
//							default:
//								break;
//							}
//						}
//					}).create();
//			mSharedDialog.setTitle(getString(R.string.share));
//			mSharedDialog.setCanceledOnTouchOutside(true);
//			mSharedDialog.show();
//		} catch (Exception e) {
//			log.e(e);
//		}
//	}
//
//	private void isValidateSinaToken(String gameName) {
//		Intent intent = new Intent(GameGiftDetailActivity.this,
//				ShareGameToSinaAcivity.class);
//		intent.putExtra(Params.GAME_INFO.GAME_NAME, gameName);
//		startActivity(intent);
//	}

}
