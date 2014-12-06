package com.cyou.mrd.pengyou.ui;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameCircleViewPagerAdapter;
import com.cyou.mrd.pengyou.adapter.GameDetailGuessLikeAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.Intro;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.CountService;
import com.cyou.mrd.pengyou.ui.GameDetailFragment.OnGetIntroListener;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.IntroDownloadBtn;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


/**
 * @author 
 */

public class GameCircleDetailActivity extends CYBaseActivity implements
        OnClickListener, OnCheckedChangeListener, OnGetIntroListener{
	private String TAG = "GameCircleDetailActivity";
    private static final int ANIM_END = 1;
	private static final int HIDE_DOWDNLOAD_LAYOUT = 2;
	protected static final int UPDATE_GAMECIRCLE_DYNAMIC_COUNT = 3;
	protected static final int DISPLAY_DOWNLOAD_LAYOUT = 5;
	protected static final int ISDISPLAY_SCORE = 6;
	protected static final int FAVGAME_TO_SERVER = 7;
    private CYLog log = CYLog.getInstance();
    private ViewPager mViewPager;
    private ImageView mRightHearIV;
    //发布按钮
    private ImageButton  mReleaseBtn;
    private TextView mGameTitleTV;
    private RadioButton mGameCircleDynamicRBtn;
    private TextView mTabLine;
    private TextView mAnimTV;
    private RadioGroup mRG;
    private  IntroDownloadBtn downloadBtn;
    private int mAnimTVWidth = 0;
    private int mCurIndex = 0;
    private View mGameCircleDownloadLL;
    private String mGameCode;
    private String mGameName;
    private Intro mIntro;
    private boolean mIsInstalled ; //游戏是否已安装
    private ProgressReceiver mProgressReceiver;
    private AlertDialog mSharedDialog;
    private ImageButton mGameCircleFavBtn;
    private ImageButton mGameCircleShareBtn;
    private String resultErrorMsg;
    private String resultMsg;
    private String fav;
    private WaitingDialog mWaitingDialog;    
    private ImageButton mDownloadIBtn;
    private Button mDownloadCountTV;
    private static int count;
    private DownloadItem downloadItem;
    private int downloading_task_size;
    private int mGamecircle_count = 0; //游戏圈动态数目
    private  int screenWidth;
    private boolean jumpToGameZone = false;
    private DownloadDao downloaddao;
    private DownloadCountReceiver mDownloadReceiver;
    private MyGameCircleInstallAppReceiver mInstallAppReceiver;
    private boolean flag=true;
    private boolean isInstalledGame;
	private UnInstallAppReceiver unstallAppReceiver;
	private boolean unpublished=false,unshare=false,unfav=false; 		
    private GameDetailGuessLikeAdapter guessLikeAdapter;   
    private RelativeLayout guessgamepop;
    private PullToRefreshListView mPullListViewGuess;
    private ArrayList<GameItem> guessGameList ;
    private Animation uploadanimation, downloadanimation;
    private boolean mHadRegistDownloadReceiver = false;
    private Intent iDownload ;
    private boolean mHadRegistProgressReceiver = false;
    
    private String mIdentifier;
    private String mVersion;
    private  View headerbar;
	boolean isClickMultiTimes = false,isStartUpGame=false;
	private Intent mIntent = new Intent();
	private boolean isResume=false;
	
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_circle_layout);
       mGameCode= getIntent().getStringExtra(Params.INTRO.GAME_CODE);
    //   log.d("---------入口传过来的gamecode    ："+mGameCode);
       //从玩游戏进来的游戏，如果安装了非最高版本，intent传的是非最高版本的gamecode，但页面显示的是最高版本的信息，导致下载进度匹配不上
        jumpToGameZone = getIntent().getBooleanExtra(Params.INTRO.GAME_CIRCLE, false);
       // mGameName = getIntent().getStringExtra(Params.INTRO.GAME_NAME);
        downloaddao=DownloadDao.getInstance(this);
             
        if(!TextUtils.isEmpty(mGameCode)){
        	mWaitingDialog = new WaitingDialog(this);
            mWaitingDialog.show();
           initView();
           regReceiver();
        }else{
        	Toast.makeText(getApplicationContext(),this.getResources().getString(R.string.game_code_null),Toast.LENGTH_LONG).show();
        	this.finish();
        }
    }
   
    public boolean onKeyDown(int keyCode, android.view.KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return false;
    };
    public void regReceiver(){
    	 try {
         	
         	if(mInstallAppReceiver==null){
         		mInstallAppReceiver=new MyGameCircleInstallAppReceiver();
         	}
         	if(mInstallAppReceiver!=null){
         		registerReceiver(mInstallAppReceiver,new IntentFilter(Contants.ACTION.GAME_INSTALL));
         	}
             if (mDownloadReceiver == null) {
                 mDownloadReceiver = new DownloadCountReceiver();
             }        
             if (mDownloadReceiver != null && !mHadRegistDownloadReceiver) {
                 registerReceiver(mDownloadReceiver, new IntentFilter(
                         Contants.ACTION.DOWNLOADING_COUNT));
                 mHadRegistDownloadReceiver = true;
             }        	
 			if (unstallAppReceiver == null) {
 				unstallAppReceiver = new UnInstallAppReceiver();
 			}
 			if (unstallAppReceiver != null) {
 				registerReceiver(unstallAppReceiver, new IntentFilter(
 						Contants.ACTION.UNINSTALL));
 			}
 			if (mProgressReceiver == null) {
 				mProgressReceiver = new ProgressReceiver();
 			}
 			if (mProgressReceiver != null && !mHadRegistProgressReceiver) {
 				registerReceiver(mProgressReceiver, new IntentFilter(
 						DownloadParam.UPDATE_PROGRESS_ACTION));
 				mHadRegistProgressReceiver = true;
 			}
         } catch (Exception e) {
             log.e(e);
         }
    }

    protected void onResume() {
       
        try {       	
			if (mIntro != null) {	
			
				if(isStartUpGame){
		//			log.d("-------启动游戏后，进入onresume中刷新界面");
				mIntent.setClass(GameCircleDetailActivity.this,GameCircleDetailActivity.class);    
				mIntent.putExtra(Params.INTRO.GAME_CODE, mIntro.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, mIntro.getName());
		        startActivity(mIntent);    
		        isStartUpGame=false;
		        finish();
				}else{
					isResume=true;
		//			log.d("-------进入onresume中，重置数据状态");
				setRightHeadState(-1);
				count = downloaddao.getDownloadPauseTaskSize(); 
			//	updateBtnState();
				initDownloadState();
				}
			}             
        } catch (Exception e) {
            log.e(e);
        }
        super.onResume();
    }
    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
   
        try {
        	if(mInstallAppReceiver!=null ){
        		unregisterReceiver(mInstallAppReceiver);
        	}
        	if (mProgressReceiver != null && mHadRegistProgressReceiver) {
                unregisterReceiver(mProgressReceiver);
                mHadRegistProgressReceiver = false;
            }
            if (mDownloadReceiver != null && mHadRegistDownloadReceiver) {
                unregisterReceiver(mDownloadReceiver);
                mHadRegistDownloadReceiver = false;
            }     
            if (unstallAppReceiver != null) {
				unregisterReceiver(unstallAppReceiver);
			}

        } catch (Exception e) {
            log.e(e);
        }
        super.onDestroy();
    }

	protected void initView(){
		 
        headerbar = findViewById(R.id.gamecircle_title_bar);
        headerbar.findViewById(R.id.game_circle_back_btn).setOnClickListener(this);
        mRightHearIV = (ImageView) headerbar.findViewById(R.id.game_circle_senddynamic_iv);
        mRightHearIV.setOnClickListener(this);
        mGameTitleTV = (TextView) headerbar.findViewById(R.id.game_circle_message_tv);       
        mReleaseBtn = (ImageButton) headerbar.findViewById(R.id.sub_header_bar_right_ibtn);
        mReleaseBtn.setVisibility(View.GONE);
        mReleaseBtn.setOnClickListener(this);    
       
        mViewPager = (ViewPager) findViewById(R.id.game_circle_viewpager);
        mViewPager.setAdapter(new GameCircleViewPagerAdapter(GameCircleDetailActivity.this,true,mGameCode,mHandler));
        mViewPager.setOnPageChangeListener(new PageChangeListener());
        mViewPager.setOffscreenPageLimit(0);
        mRG = (RadioGroup) findViewById(R.id.gamecircle_rg);
        mRG.setOnCheckedChangeListener(this);
        mRG.setVisibility(View.VISIBLE);
        mGameCircleDynamicRBtn = (RadioButton) findViewById(R.id.gamecircle);
        mTabLine = (TextView)findViewById(R.id.tab_line);
        mTabLine.setVisibility(View.VISIBLE);               
        mAnimTV = (TextView) findViewById(R.id.gamecircle_anim_tv);
        RelativeLayout.LayoutParams lp=(RelativeLayout.LayoutParams) mAnimTV.getLayoutParams();
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		lp.width = (screenWidth - (3 * getResources().getDimensionPixelSize(R.dimen.search_bar_height)))/2;
		mAnimTV.setLayoutParams(lp);
		 mAnimTV.setVisibility(View.VISIBLE);
		mAnimTVWidth = (screenWidth - (2 * getResources()
				.getDimensionPixelSize(R.dimen.search_bar_height)))/2;		
        mGameCircleDownloadLL =findViewById(R.id.gamecircle_download_ll);
        mGameCircleDownloadLL.setVisibility(View.VISIBLE);       
        downloadBtn = (IntroDownloadBtn) findViewById(R.id.gamecircle_download_btn);
        downloadBtn.setOnClickListener(this);    
        mGameCircleFavBtn = (ImageButton) findViewById(R.id.gamecircle_fav_ibtn);
        mGameCircleFavBtn.setOnClickListener(this);
        mGameCircleShareBtn = (ImageButton) findViewById(R.id.gamecircle_share_ibtn);
        mGameCircleShareBtn.setOnClickListener(this);                
        mDownloadIBtn = (ImageButton) findViewById(R.id.game_circle_senddynamic_iv);
        mDownloadIBtn.setOnClickListener(this);
        mDownloadCountTV = (Button) findViewById(R.id.game_circle_senddynamic_tv);
        uploadanimation=AnimationUtils.loadAnimation(GameCircleDetailActivity.this, R.anim.settingswindow_in_anim);	

        if(jumpToGameZone){
        	mViewPager.setCurrentItem(1);
        }
        else{
            mViewPager.setCurrentItem(0);
        }
       initPopup();
       
 }    


    public void initPopup(){
   	 guessgamepop=(RelativeLayout)findViewById(R.id.guess_game_pop);
   	 guessgamepop.setOnClickListener(new OnClickListener() {
	//为了防止点击猜你喜欢无数据的地方会产生点击事件	
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			return ;
		}
	});
   	 mPullListViewGuess = (PullToRefreshListView)findViewById(R.id.guess_youlike_listview);
   	 mPullListViewGuess.setDividerHeight(2);
   	 ImageButton popcanclebtn=(ImageButton)findViewById(R.id.guess_youlike_cancle);
   	 popcanclebtn.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {		
				downloadanimation=AnimationUtils.loadAnimation(GameCircleDetailActivity.this, R.anim.settingswindow_out_anim);
				guessgamepop.startAnimation(downloadanimation);
				guessgamepop.setVisibility(View.GONE); 									
			}
		});
        if (null == guessGameList) {
			guessGameList = new ArrayList<GameItem>();
		}
		if (guessLikeAdapter == null) {
			guessLikeAdapter=new GameDetailGuessLikeAdapter(GameCircleDetailActivity.this, guessGameList,guessgamepop); 		
		}
		mPullListViewGuess.setAdapter(guessLikeAdapter);
		mPullListViewGuess.setOnItemClickListener(new OnItemClickListener() {
        
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				GameItem item=null;
				if(guessGameList != null && guessGameList.size() > 0
						&& ! (position > guessGameList.size())){
					item = guessGameList.get(position);
				}else{
					log.i("guessGameList is null or position >guessGameList.size()");
					return;
				}				
				if (null == item) {
					return;
				}
				
				mIntent.setClass(GameCircleDetailActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivityForResult(mIntent, 100);							
				/*if (guessGameList == null || guessGameList.size() == 0
						|| position > guessGameList.size()) {
					return;
				}
				GameItem item = guessGameList.get(position);;
				if (null == item) {
					return;
				}
				
				mIntent.setClass(GameCircleDetailActivity.this, GameCircleDetailActivity.class);
				mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
				mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
				startActivityForResult(mIntent, 100);
				*/				
			}
		});
   }
     protected void onActivityResult(int requestCode, int resultCode, Intent data){
    	 //可以根据多个请求代码来作相应的操作  
         if(100==resultCode)  
         {  
        	 guessgamepop.setVisibility(View.GONE); 
         }  
         super.onActivityResult(requestCode, resultCode, data);  
     }  
 
    @Override
    public void onClick(final View v) {
        
        switch (v.getId()) {
        case R.id.game_circle_back_btn:
        	setResult(100);
            finish();
            break;
        case R.id.game_circle_senddynamic_iv:
            BehaviorInfo behaviorInfo = new BehaviorInfo(
                    CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_ID,
                    CYSystemLogUtil.DOWNLOAD.BTN_DOWNLOAD_NAME);
            CYSystemLogUtil.behaviorLog(behaviorInfo);
            mIntent.setClass(GameCircleDetailActivity.this,
                    DownloadActivity.class);
            startActivity(mIntent);
            break;
        case R.id.gamecircle_download_btn:
        	 downloadBtnOperation1();
        	// 根据pakageName获取DownloadItem
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
            break;
		case R.id.gamecircle_share_ibtn:
			if (mIntro == null) {
				return;
			}
			if (mIntro.getPublished().equals("0") && unshare == true) {// 0表示未发布
				Toast.makeText(this, getString(R.string.game_unpublished),
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (1 == Integer.valueOf(mIntro.getPlatform())) {// 如果是苹果平台
				Toast.makeText(GameCircleDetailActivity.this,
						getString(R.string.cannot_share), Toast.LENGTH_SHORT)
						.show();
				return;
			}
			if (mSharedDialog == null) {
				initShareDialog();
			}
			mSharedDialog.show();
            break;
        case R.id.gamecircle_fav_ibtn:
        	if(mIntro == null){
        		Toast.makeText(this, R.string.wating_for_get_data, Toast.LENGTH_SHORT).show();
        		return;
        	}
        	if(mIntro.getPublished().equals("0") && unfav==true){//0表示未发布
        		Toast.makeText(this, getString(R.string.game_unpublished), Toast.LENGTH_SHORT).show();        		
        	}else{
            favGame();
        	}
            break;
        case R.id.sub_header_bar_right_ibtn:
        	if(mIntro == null){
        		Toast.makeText(this, R.string.wating_for_get_data, Toast.LENGTH_SHORT).show();
        		return;
        	}
        	Intent releaseIntent = new Intent();
        	releaseIntent.setClass(GameCircleDetailActivity.this, SendGameCircleDynamicActivity.class);
        	releaseIntent.putExtra(Params.INTRO.GAME_CODE, mGameCode);
        	releaseIntent.putExtra(Params.Dynamic.GAME_CIRCLE_ID, String.valueOf(mIntro.getGcid()));
        	releaseIntent.putExtra(Params.INTRO.GAME_NAME, mIntro.getName());
        	releaseIntent.putExtra(Params.INTRO.GAME_ISINSTALLED, mIsInstalled );
//        	releaseIntent.putExtra(Params.INTRO.GAME_ISDISPLAYSTAR, isDisplayStar);
        	startActivity(releaseIntent);
        	break;     
        default:
            break;
        }
    }  
   
//public void updateBtnState(){
//	if(mIntro!=null){
//		if (1 == Integer.valueOf(mIntro.getPlatform())) {
//			downloadBtn.setSomething(0, getString(R.string.search_ios_download), false);
//			downloadBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					mIntent.setClass(GameCircleDetailActivity.this,
//							GameSearchResultActivity.class);
//					mIntent.putExtra("gamedetail_gamecode", mGameCode);
//					mIntent.putExtra("gamedetail_gamename", mGameName);
//					startActivity(mIntent);
//				}
//			});
//			return;
//		}	
//		 downloadBtn.setOnClickListener(new OnClickListener() {			
//				@Override
//				public void onClick(final View v) {
//					downloadBtnOperation1();
//					if(isClickMultiTimes){
//						return;
//					}else{
//						isClickMultiTimes = true;
//						v.setClickable(false);
//						CountDownTimer timer = new CountDownTimer(2000, 2000) {
//							/**
//							 * 提醒时间到，调用此方法
//							 */
//							public void onTick(long millisUntilFinished) {
//							}
//							/**
//							 * 定时时间到，调用此方法
//							 */
//							public void onFinish() {
//								isClickMultiTimes = false;
//								v.setClickable(true);
//							}
//						};
//						// 开启定时器
//						timer.start();
//					}
//					
//				}
//			});
//	mIsInstalled = Util.isInstallByread(mIdentifier);
//	//安装了任何一个版本的游戏就不可收藏所有版本的该游戏了
//	if (mIsInstalled) { // 如果安装了
//		mGameCircleFavBtn.setClickable(false);
//		mGameCircleFavBtn.setImageResource(R.drawable.imgfav_unclickable);		
//		if(TextUtils.isEmpty(mIntro.getVersioncode())||mIntro.getVersioncode().equals("0")){
//    		isInstalledGame=true;
//    	}else{
//		isInstalledGame = Util.isInstalledGame(Util.getAppVersionCode(mIdentifier), mIntro.getVersioncode());
//    	}
//		if(isInstalledGame){				
//			downloadBtn.removeView(downloadBtn.getmPB());
//			//downloadBtn.reinit();
//			Drawable drawable = this.getResources().getDrawable(
//					R.drawable.intro_download_btn_startup_xbg);
//			drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
//					.getBounds());
//			downloadBtn.setBackgroundDrawable(drawable);			
//			downloadBtn.setSomething(R.drawable.qidong,
//					getString(R.string.start_up), true);        				
//		return;
//		}		
//	}		
//	
//	    downloadBtn.reinit();
//		downloadItem = downloaddao.getDowloadItem(mIdentifier, mIntro.getVersion());
//		if (downloaddao.isHasInfo(mIdentifier,mIntro.getVersion())) {
//			if(mIntro.getPublished().equals("0")){
//			 if(downloadItem.getmState()==DownloadParam.C_STATE.DONE){
//				downloadBtn.removeView(downloadBtn.getmPB());
//				Drawable drawable = this.getResources().getDrawable(
//						R.drawable.intro_download_btn_install_xbg);
//				drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
//						.getBounds());
//				downloadBtn.setBackgroundDrawable(drawable);
//				downloadBtn.setSomething(R.drawable.anzhuang,
//						getString(R.string.install), true);	
//				return;
//			}		
//			 downloadBtn.removeView(downloadBtn.getmPB());
//				Drawable drawable = this.getResources().getDrawable(
//						R.drawable.intro_download_unpublished);
//				drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
//						.getBounds());
//				downloadBtn.setBackgroundDrawable(drawable);
//				downloadBtn.setSomething(R.drawable.unpublished_icon,getString(R.string.unpublished),true);
//				unpublished=true;//不是安装和启动状态
//				return;
//			}
//			switch (downloadItem.getmState()) {
//			case DownloadParam.C_STATE.DOWNLOADING:
//				int percent=1;
//				if(0==downloadItem.getmPercent()){
//				 	downloadBtn.setProgress(percent);			
//					downloadBtn.setSomething(0, getString(R.string.press_to_pause) + "  "+ 0 + "%", false);			
//				}else{
//					downloadBtn.setProgress(downloadItem.getmPercent());			
//					downloadBtn.setSomething(0, getString(R.string.press_to_pause) + "  "+ downloadItem.getmPercent() + "%", false);
//				}
//				break;
//			case DownloadParam.C_STATE.PAUSE:
//				downloadBtn.setProgress(downloadItem.getmPercent()); 
//				downloadBtn.setSomething(0, getString(R.string.press_to_con) + "  "+ downloadItem.getmPercent() + "%", false);			
//				break;
//			case DownloadParam.C_STATE.DONE:
//				downloadBtn.removeView(downloadBtn.getmPB());
//				Drawable drawable = this.getResources().getDrawable(
//						R.drawable.intro_download_btn_install_xbg);
//				drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
//						.getBounds());
//				downloadBtn.setBackgroundDrawable(drawable);
//				downloadBtn.setSomething(R.drawable.anzhuang,
//						getString(R.string.install), true);
//				break;
//			case DownloadParam.C_STATE.WAITING:				
//				downloading_task_size=downloaddao.getDownloadTaskSize();
//				if(downloading_task_size>=3){
//				downloadBtn.setProgress(downloadItem.getmPercent());
//				downloadBtn.setSomething(0, getString(R.string.gamecircledetailactivity_pending),false);				
//				}		
//				break;
//			default:				
//				downloadBtn.setProgress(0);
//				downloadBtn.setSomething(R.drawable.xiazai,getDownloadBtnStyle("下载(安装+" + mIntro.getDownloadscore() + " )", mIntro.getDownloadscore()),true);
//				break;
//			}
//		}else{
//			if(mIntro.getPublished().equals("0")){
//				downloadBtn.removeView(downloadBtn.getmPB());
//				Drawable drawable = this.getResources().getDrawable(
//						R.drawable.intro_download_unpublished);
//				drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
//						.getBounds());
//				downloadBtn.setBackgroundDrawable(drawable);
//				downloadBtn.setSomething(R.drawable.unpublished_icon,getString(R.string.unpublished),true);
//				unpublished=true;//不是安装和启动状态
//				unshare=true;  //不是安装和启动状态,如果下架，不可分享
//				unfav=true;
//				
//			}else{
//            //	downloadBtn.setBackgroundDrawable(this.getResources().getDrawable(R.drawable.intro_download_btn_back));
//			downloadBtn.setProgress(0);
//			downloadBtn.setSomething(R.drawable.xiazai,getDownloadBtnStyle("下载(安装+" + mIntro.getDownloadscore() + " )", mIntro.getDownloadscore()),true);
//			}				  
//		}          
//}
//}    
//监听游戏安装成功
	private class MyGameCircleInstallAppReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
			String packageName=intent.getStringExtra(DownloadParam.PACKAGE_NAME);
			if (mIdentifier!=null && mIdentifier.equals(packageName)) {
				 fav = mIntro.getIsfavorite();
                 if(String.valueOf(Contants.GAME_FAV.FAV).equals(fav)){//如果安装之前就收藏了，安装后把取消收藏状态给服务器
                	 flag=false;
                	 Message msg = mHandler.obtainMessage();
                     msg.what = FAVGAME_TO_SERVER;
                     mHandler.sendMessage(msg);               	
                }
                //安装完游戏之后，收藏不可点击                
		        downloadBtn.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(final View v) {
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
						startUpGame();
					}
				});
				
			}
			}				
	}
}
	/**启动游戏
	 * */
	public void startUpGame(){
		isStartUpGame=true;
		ContentValues values = new ContentValues();
		values.put(DBHelper.MYGAME.PACKAGE_NAME,mIdentifier);
		getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
		
		   PackageManager manager = getPackageManager();
		   mIntent = manager.getLaunchIntentForPackage(mIdentifier);
           if(mIntent!=null){
           startActivity(mIntent);
           }
			//启动统计Service
			Intent jifen_intent = new Intent(GameCircleDetailActivity.this, CountService.class);
			jifen_intent.putExtra("identifier",
					mIdentifier);
			startService(jifen_intent);
	}
	/**
	 * 监听游戏卸载
	 * */
	private class UnInstallAppReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String packageName = intent
					.getStringExtra(DownloadParam.PACKAGE_NAME);
			if (mIdentifier.equals(packageName)) {
                flag=true;
				mGameCircleFavBtn.setImageResource(R.drawable.img_unfav);
				mGameCircleFavBtn.setClickable(true);
			}
		}
	}
	/**下载个数的显示
	 * */
    private class DownloadCountReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
        	int status = intent.getIntExtra(DownloadParam.STATE, -1);
        	 setRightHeadState(status);            
        }
    }        
    private void initShareDialog() {
        mSharedDialog = new AlertDialog.Builder(GameCircleDetailActivity.this)
                .setItems(
                        new CharSequence[] {
                                getString(R.string.share_game_friend),
                                getString(R.string.share_game_fzone),
                                getString(R.string.share_game_sina),
                                getString(R.string.share_game_others)},
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                    int which) {
                              //  Intent intent = new Intent();
                                switch (which) {
                                case 0:
                                    GameItem item = mIntro.toGameItem();
                                    mIntent.setClass(
                                            GameCircleDetailActivity.this,
                                            ShareToFriendActivity.class);
                                    mIntent.putExtra(
                                            Params.SHARE_TO_FRIEND.GAME_ITEM,
                                            item);
                                    startActivity(mIntent);
                                    break;
                                case 1:
                                	mIntent.setClass(
                                            GameCircleDetailActivity.this,
                                            ShareGameToFZoneAcivity.class);
                                	mIntent.putExtra(Params.GAME_INFO.GAME_NAME,
                                            mIntro.getName());
                                	mIntent.putExtra(Params.GAME_INFO.GAME_CODE,
                                            mIntro.getGamecode());
                                	mIntent.putExtra(Params.GAME_INFO.GAME_ICON,
                                            mIntro.getIcon());
                                    startActivity(mIntent);
                                    break;
                                case 2:
                                    isValidateSinaToken(mIntro.getName());
                                    break;
    							case 3:
    								Util.shareToChooseApps(GameCircleDetailActivity.this, false);
    								break;
                                default:
                                    break;
                                }
                            }
                        }).create();
        mSharedDialog.setTitle(getString(R.string.share));
        mSharedDialog.setCanceledOnTouchOutside(true);
    }

    private void initBtnState(){
    	 mGameTitleTV.setText(mGameName);
    	 if (1 != Integer.valueOf(mIntro.getPlatform())){
    		//不是苹果版本的，显示收藏状态
    			String isFav = mIntro.getIsfavorite();
    			if (String.valueOf(Contants.GAME_FAV.FAV).equals(isFav)) {
    				mGameCircleFavBtn.setImageResource(R.drawable.img_fav1);
    			} else {
    				mGameCircleFavBtn.setImageResource(R.drawable.img_unfav);
    			}		
    	 }
    }
    private void initDownloadState() {
    	
		if (1 == Integer.valueOf(mIntro.getPlatform())) {
			downloadBtn.setSomething(0, getString(R.string.search_ios_download), false);
			downloadBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mIntent.setClass(GameCircleDetailActivity.this,
							GameSearchResultActivity.class);
					mIntent.putExtra("gamedetail_gamecode", mGameCode);
					mIntent.putExtra("gamedetail_gamename", mGameName);
					startActivity(mIntent);
				}
			});
			return;
		}
		
		mIsInstalled = Util.isInstallByread(mIdentifier);	
		//log.d("------mIsInstalled---"+mIsInstalled);
		if (mIsInstalled) { // 如果安装了
			mGameCircleFavBtn.setClickable(false);
        	mGameCircleFavBtn.setImageResource(R.drawable.imgfav_unclickable);	    
        	if(TextUtils.isEmpty(mIntro.getVersioncode())||mIntro.getVersioncode().equals("0")){
        		isInstalledGame =true;
        	}else{
        		log.d("------当前游戏versioncode"+mIntro.getVersioncode());
			isInstalledGame = Util.isInstalledGame(Util.getAppVersionCode(mIdentifier), mIntro.getVersioncode());
        	}
        	log.d("-------isInstalledGame----"+isInstalledGame);
			if(isInstalledGame){ //如果为true表示当前和本地已经下载版本相同，显示“启动”			    
	        	downloadBtn.removeView(downloadBtn.getmPB());
				Drawable drawable = this.getResources().getDrawable(
						R.drawable.intro_download_btn_startup_xbg);
				drawable.setBounds(downloadBtn.getmPB()
						.getProgressDrawable().getBounds());
				downloadBtn.setBackgroundDrawable(drawable);
				downloadBtn.setSomething(R.drawable.qidong,getString(R.string.start_up),true);				
				return;
			}
		}   
		if(isResume){
		//	log.d("---------isResume为 true，下载按钮去掉启动设置的背景");
			downloadBtn.setBackgroundDrawable(null);
			downloadBtn.reinit();
			
		}
			downloadItem = downloaddao.getDowloadItem(mIdentifier, mIntro.getVersion());
			if (downloaddao.isHasInfo(mIdentifier,mIntro.getVersion())) {
	            if(mIntro.getPublished().equals("0")){
	            	if(DownloadParam.C_STATE.DONE==downloadItem.getmState()){
	            		downloadBtn.removeView(downloadBtn.getmPB());
						Drawable drawable = this.getResources().getDrawable(
								R.drawable.intro_download_btn_install_xbg);
						drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
								.getBounds());
						downloadBtn.setBackgroundDrawable(drawable);
						downloadBtn.setSomething(R.drawable.anzhuang,
								getString(R.string.install), true);
						return;
	            	}else{
	            		downloadBtn.removeView(downloadBtn.getmPB());				
						Drawable drawable = this.getResources().getDrawable(
								R.drawable.intro_download_unpublished);
						drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
								.getBounds());
						downloadBtn.setBackgroundDrawable(drawable);
						downloadBtn.setSomething(R.drawable.unpublished_icon,getString(R.string.unpublished),true);
						unpublished=true;
						unshare=true;
						unfav=true;
	            		return;
	            	}
	            }
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DOWNLOADING:
					if(0==downloadItem.getmPercent()){
					 	downloadBtn.setProgress(1);			
						downloadBtn.setSomething(0, getString(R.string.press_to_pause) + "  "+ 0 + "%", false);			
					}else{
						downloadBtn.setProgress(downloadItem.getmPercent());			
						downloadBtn.setSomething(0, getString(R.string.press_to_pause) + "  "+ downloadItem.getmPercent() + "%", false);
					}
					break;
				case DownloadParam.C_STATE.PAUSE:
					downloadBtn.setProgress(downloadItem.getmPercent());
					downloadBtn.setSomething(0, getString(R.string.press_to_con) + "  "+ downloadItem.getmPercent() + "%", false);
					break;
				case DownloadParam.C_STATE.DONE:
					downloadBtn.removeView(downloadBtn.getmPB());
					Drawable drawable = this.getResources().getDrawable(
							R.drawable.intro_download_btn_install_xbg);
					drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
							.getBounds());
					downloadBtn.setBackgroundDrawable(drawable);
					downloadBtn.setSomething(R.drawable.anzhuang,
							getString(R.string.install), true);
					break;
				case DownloadParam.C_STATE.WAITING:
					downloading_task_size=downloaddao.getDownloadTaskSize();
					if(downloading_task_size>=3){
					    downloadBtn.setProgress(downloadItem.getmPercent());
					    downloadBtn.setSomething(0, getString(R.string.gamecircledetailactivity_pending),false);
					}		
					break;				
				default:             
					downloadBtn.setProgress(0);
					downloadBtn.setSomething(R.drawable.xiazai,getDownloadBtnStyle("下载(安装+" + mIntro.getDownloadscore() + " )", mIntro.getDownloadscore()),true);
					break;
				}
			}else { // 数据库中没有这条游戏	
				if(mIntro.getPublished().equals("0")){
					downloadBtn.removeView(downloadBtn.getmPB());
					Drawable drawable = this.getResources().getDrawable(
							R.drawable.intro_download_unpublished);
					drawable.setBounds(downloadBtn.getmPB().getProgressDrawable()
							.getBounds());
					downloadBtn.setBackgroundDrawable(drawable);
					downloadBtn.setSomething(R.drawable.unpublished_icon,getString(R.string.unpublished),true);
					unpublished=true;//不是安装和启动状态
					unshare=true;  //不是安装和启动状态,如果下架，不可分享
					unfav=true;
					
				}else{
				downloadBtn.setProgress(0);				
				downloadBtn.setSomething(R.drawable.xiazai,getDownloadBtnStyle("下载(安装+" + mIntro.getDownloadscore() + " )", mIntro.getDownloadscore()),true);
				}				
			}	
			
    }
    private void setRightHeadState(int status) {
        if (mCurIndex == 0){ 
        	count = downloaddao.getDownloadPauseTaskSize();  
        	
        	Animation anim = AnimationUtils.loadAnimation(GameCircleDetailActivity.this,
                    R.anim.download_count);
        	if (count > 0) {
            
                mDownloadCountTV.setVisibility(View.VISIBLE);
                if (count <= 99) {
                    mDownloadCountTV.setText(String.valueOf(count));
                } else {
                    mDownloadCountTV.setText("N");
                }
                if (status == 0||status == 3||status == 11) {
                	mDownloadCountTV.startAnimation(anim);
                }
            }else{
            	mDownloadCountTV.setVisibility(View.GONE);
            }         
        	mReleaseBtn.setVisibility(View.GONE);
        	mRightHearIV.setVisibility(View.VISIBLE);       	
        }else {
            //显示发布按钮
        	mReleaseBtn.setVisibility(View.VISIBLE);
        	mRightHearIV.setVisibility(View.GONE);
        	mDownloadCountTV.setVisibility(View.GONE);
        }
        mWaitingDialog.dismiss();
    }
    public void downloadBtnOperation1() {
        if (mIntro == null) {
            Toast.makeText(this,getResources().getString(R.string.intro_null),Toast.LENGTH_SHORT).show();
			return;
		}
    	if(mIntro.getPublished().equals("0" )&& unpublished==true){//0表示未发布,而且游戏不是安装和启动的状态
    		return;
    	}
       
		if(mIsInstalled){
			if(isInstalledGame){//和当前版本一样，启动游戏
				startUpGame();			
				return;				
			}		
		}	
	//	log.d("----come in  downloadbtnoperation1");
		 //必须重新获取downloaditem否则点击一次后不可再次点击
		 downloadItem = downloaddao.getDowloadItem(mIdentifier,mIntro.getVersion());
		 iDownload = new Intent(GameCircleDetailActivity.this,DownloadService.class);		
	        //	已经安装和当前版本不一样，或者未安装，都显示当前版本状态
			if (downloaddao.isHasInfo(mIdentifier,mIntro.getVersion())) {// DB中有数据
		//		log.d("----downloaddao   ishasinfo");
				if (downloadItem != null) {
		//			log.d("----downloadItem   is not null");
		//			log.d("-------状态      ："+downloadItem.getmState());
					switch (downloadItem.getmState()) {
					case DownloadParam.C_STATE.DOWNLOADING:
						iDownload.putExtra(DownloadParam.STATE,
								DownloadParam.TASK.PAUSE);
						iDownload.putExtra(DownloadParam.DOWNLOAD_ITEM,
								downloadItem);
						startService(iDownload);
						break;
					case DownloadParam.C_STATE.PAUSE:
						iDownload.putExtra(DownloadParam.STATE,
								DownloadParam.TASK.CONTINUE);
						iDownload.putExtra(DownloadParam.DOWNLOAD_ITEM,
								downloadItem);
						startService(iDownload);
						break;
					case DownloadParam.C_STATE.DONE:
						Intent i = new Intent(Intent.ACTION_VIEW);
						i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						String fileName = mIntro.getKey();
						File file = new File(
								SharedPreferenceUtil.getAPKPath(this), fileName
										+ Config.APK_SUFFIX);
						if (file.exists()) {
							i.setDataAndType(Uri.fromFile(file),
									"application/vnd.android.package-archive");
							startActivity(i);
						}
						break;
				    case DownloadParam.C_STATE.WAITING:
//						downloading_task_size=downloaddao.getDownloadTaskSize();												
//						if(downloading_task_size>=3){
				    	mIntent.setClass(GameCircleDetailActivity.this,DownloadActivity.class);
						    startActivity(mIntent);
//						}
//						startService(iDownload);
                        break;
					default:
						log.d("----default");
						break;
					}
				}
			} else {// DB中没有数据，给db中添加数据，并创建下载任务
				if (!Util.isDownloadUrl(mIntro.getFullurl())) {
					Toast.makeText(GameCircleDetailActivity.this,
							R.string.download_url_error, Toast.LENGTH_SHORT)
							.show();
					return;
				}
			//	log.d("----downloaddao  not has  info ,come in add task service");
				downloading_task_size=downloaddao.getDownloadTaskSize();												
				if(downloading_task_size>=3){	
			//		log.d("----downloading_task_size>=3");
					 downloadBtn.setProgress(0);
					 downloadBtn.setSomething(0, getString(R.string.gamecircledetailactivity_pending),false);
				}else{
				//	log.d("----come in  setProgress(1) and R.string.press_to_pause");
				downloadBtn.setProgress(1);
				downloadBtn.setSomething(0, getString(R.string.press_to_pause) + "  0%", false);
				}
			    	    	iDownload.putExtra(DownloadParam.STATE, DownloadParam.TASK.ADD);
			    			DownloadItem item = new DownloadItem();
			    			item.setmName(mIntro.getName());
			    			item.setmLogoURL(mIntro.getIcon());
			    			item.setmSize(mIntro.getFullsize());
			    			item.setmURL(mIntro.getFullurl());
			    			item.setmPackageName(mIdentifier);
			    			item.setGameCode(mIntro.getGamecode());
			    			item.setVersionName(mIntro.getVersion());
			    			iDownload.putExtra(DownloadParam.DOWNLOAD_ITEM, item);
			    			startService(iDownload);	
			    			loadGuessGameList();
	  
}
    }

    private class PageChangeListener implements OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            // TODO Auto-generated method stub
        }
        @Override
        public void onPageSelected(int arg0) {
        	//页面切换时，显示发布和下载按钮
            resetRadioBtn(arg0);
            setRightHeadState(-1);
       	    hideKeyboard();

       	 if(arg0  == 0){
     		if(mGameCircleDownloadLL != null && !mGameCircleDownloadLL.isShown())
     		     mGameCircleDownloadLL.setVisibility(View.VISIBLE);
     	}
     	else {
           	sendBroadcast(new Intent(Contants.ACTION.SEND_GAME_CIRCLE_SWITCH_TAB));
     	}
      }
     		}
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
        case R.id.gamedetail:
            mViewPager.setCurrentItem(0);
            break;
        case R.id.gamecircle:
            mViewPager.setCurrentItem(1);
            break;
        }
    }
    private void resetRadioBtn(final int index) {
        Animation anim = new TranslateAnimation(mCurIndex * mAnimTVWidth, index
               * mAnimTVWidth, 0, 0);
    	
        anim.setFillAfter(true);
        anim.setDuration(300);
        anim.setAnimationListener(new AnimationListener() {

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
                Message msg = mHandler.obtainMessage();
                msg.what = ANIM_END;
                mHandler.sendMessage(msg);
            }
        });
        mAnimTV.startAnimation(anim);
        mCurIndex = index;
    }
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case ANIM_END:
                for (int i = 0; i < mRG.getChildCount(); i++) {
                    if (i == mCurIndex) {
                        ((RadioButton) mRG.getChildAt(i))
                                .setTextColor(getResources().getColor(
                                        R.color.tab_text_select));
                        ((RadioButton) mRG.getChildAt(i)).performClick();
                    } else {
                        ((RadioButton) mRG.getChildAt(i))
                                .setTextColor(getResources().getColor(
                                        R.color.tab_text_normal));
                    }
                }
                break;
            case HIDE_DOWDNLOAD_LAYOUT :
            	if(msg.arg1 == 0){
            		mGameCircleDownloadLL.setVisibility(View.GONE);
            	}
            	else{
            		mGameCircleDownloadLL.setVisibility(View.VISIBLE);
            	}
            	break;
            case DISPLAY_DOWNLOAD_LAYOUT:
				if (mGameCircleDownloadLL.getVisibility() == View.GONE) {
					mGameCircleDownloadLL.setVisibility(View.VISIBLE);
				} else {
					mGameCircleDownloadLL.setVisibility(View.GONE);
				}
				break;
            case UPDATE_GAMECIRCLE_DYNAMIC_COUNT:
            	if(mGameCircleDynamicRBtn != null){
            		mGamecircle_count += 1;
                    mGameCircleDynamicRBtn.setText(getString(R.string.game_circle) + "(" +  mGamecircle_count +  ")");
                }
            	break;
            case  ISDISPLAY_SCORE:
                break;
            case  FAVGAME_TO_SERVER:
              	  favGame(); 
               break;
            default:
                break;
            }
        };
    };
/**获取下载状态和下载进度
 * */
	private class ProgressReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mIntro != null) {
					if (intent != null) {
						
						Integer gameStatus = intent.getIntExtra(
								DownloadParam.STATE, 10000);
						int percent = intent.getIntExtra(DownloadParam.PERCENT,
								-1);
						String gameString = intent
								.getStringExtra(DownloadParam.GAMESTRING);
						String gamecode=intent.getStringExtra(DownloadParam.GAMECODE);
						String packagename = intent
								.getStringExtra(DownloadParam.PACKAGE_NAME);
		//				log.d("-------收到进度条广播      ："+gameStatus.toString()+","+percent+","+gameString+","+gamecode+","+packagename);
						switch (gameStatus) {
						case DownloadParam.TASK.DONE:
							if (packagename.equals(mIdentifier)) {
								setRightHeadState(0);
								downloadBtn.setSomething(R.drawable.anzhuang,
										getString(R.string.install), true);
							}
							break;
						case DownloadParam.C_STATE.DOWNLOADING:
							if (percent > -1 && gameString.equals(mIdentifier+ mIntro.getVersion())&& mGameCode.equals(gamecode)) {
								if (percent == 0) {
									downloadBtn.setProgress(1);
								} else {
									downloadBtn.setProgress(percent);
								}
								downloadBtn.setSomething(0,
										getString(R.string.press_to_pause)
												+ "  " + percent + "%", false);
							}
							break;
						case DownloadParam.TASK.PAUSE:
							if (percent > -1&& gameString.equals(mIdentifier+ mIntro.getVersion())&& mGameCode.equals(gamecode)) {
								if (percent == 0) {
									downloadBtn.setProgress(1);
								} else {
									downloadBtn.setProgress(percent);
								}
								downloadBtn.setSomething(0,
										getString(R.string.press_to_con) + "  "
												+ percent + "%", false);
							}
							break;
						case DownloadParam.TASK.PAUSE_ALL:
							downloadItem = downloaddao
									.getDowloadItem(mIdentifier,
											mIntro.getVersion());
							if (downloadItem.getmPercent() == 0) {
								downloadBtn.setProgress(1);
							} else {
								downloadBtn.setProgress(downloadItem
										.getmPercent());
							}
							downloadBtn.setSomething(0,
									getString(R.string.press_to_con) + "  "
											+ downloadItem.getmPercent() + "%",
									false);
							break;
						case DownloadParam.TASK.CONTINUE:
							if (gameString.equals(mIdentifier
									+ mIntro.getVersion())&& mGameCode.equals(gamecode)) {
								downloadItem = downloaddao.getDowloadItem(
										mIdentifier,
										mIntro.getVersion());
								if (downloadItem.getmPercent() == 0) {
									downloadBtn.setProgress(1);
								} else {
									downloadBtn.setProgress(downloadItem
											.getmPercent());
								}
								downloadBtn.setSomething(
										0,
										getString(R.string.press_to_pause)
												+ "  "
												+ downloadItem.getmPercent()
												+ "%", false);
							}
							break;
						case DownloadParam.TASK.CONTINUE_ALL:
							downloadItem = downloaddao
									.getDowloadItem(mIdentifier,
											mIntro.getVersion());
							if (downloadItem.getmPercent() == 0) {
								downloadBtn.setProgress(1);
							} else {
								downloadBtn.setProgress(downloadItem
										.getmPercent());
							}
							downloadBtn.setSomething(0,
									getString(R.string.press_to_pause) + "  "
											+ downloadItem.getmPercent() + "%",
									false);
							break;
						case DownloadParam.TASK.WAITING_PAUSE:// 等待中，点击跳转到下载管理页
							downloadItem = downloaddao
									.getDowloadItem(mIdentifier,
											mIntro.getVersion());
							if ((mIdentifier + mIntro.getVersion())
									.equals(gameString) && mGameCode.equals(gamecode)) {// 加上这个判断会崩
								downloading_task_size = downloaddao
										.getDownloadTaskSize();
								if (downloading_task_size >= 3) {
									downloadBtn.setProgress(downloadItem
											.getmPercent());
									downloadBtn
											.setSomething(
													0,
													getString(R.string.gamecircledetailactivity_pending),
													false);
								}
							}
							break;
						case DownloadParam.C_STATE.WAITING:
							downloadItem = downloaddao.getDowloadItem(mIdentifier,mIntro.getVersion());
							downloading_task_size = downloaddao.getDownloadTaskSize();
							if (downloading_task_size >= 3) {
								if (downloadItem.getmState() == DownloadParam.C_STATE.WAITING) {
									downloadBtn.setProgress(downloadItem.getmPercent());
									downloadBtn.setSomething(0,getString(R.string.gamecircledetailactivity_pending),
													false);
								}
							}
							// }
							break;
						case DownloadParam.TASK.DELETE:
							setRightHeadState(0);
							//卸载和清理记录的时候都会删除包，已经安装的清理记录不变，已安装的卸载后变为下载状态，未安装的清理记录变为下载
						    if (gameString.equals(mIdentifier+ mIntro.getVersion())&& mGameCode.equals(gamecode)) {
						    	if(TextUtils.isEmpty(mIntro.getVersioncode())||mIntro.getVersioncode().equals("0")){
					        		isInstalledGame=true;
					        	}else{
								isInstalledGame = Util.isInstalledGame(Util.getAppVersionCode(mIdentifier), mIntro.getVersioncode());
					        	}
							     
							     if (isInstalledGame == false) {
								      downloadBtn.reinit();							 
							     }
						    }
							break;
						default:
							break;

						}
					}
				}
			}
			
		}

    private void isValidateSinaToken(String gameName) {
    	mIntent.setClass(GameCircleDetailActivity.this,
                ShareGameToSinaAcivity.class);
    	mIntent.putExtra(Params.GAME_INFO.GAME_NAME, gameName);
        startActivity(mIntent);
    }

    @Override
    public void OnGetIntro(Intro mIntro) {
        this.mIntro = mIntro;
        mIdentifier=mIntro.getIdentifier();
        mVersion=mIntro.getVersion();
        mGameCode=mIntro.getGamecode();
        mGameName=mIntro.getName();
   //     log.d("---------在OnGetIntro获取的包名和版本号     ："+mIdentifier+","+mVersion);
   //     log.d("---------在OnGetIntro获取的gamecode     ："+mIntro.getGamecode());
   //     log.d("---------在OnGetIntro获取的gtname    ："+mIntro.getGtname());
        
        if(TextUtils.isEmpty(mGameName)){
        	mGameName = "";
        }  
       // initView();
        mGamecircle_count = mIntro.getGcacts();
        if(mGameCircleDynamicRBtn != null){
            mGameCircleDynamicRBtn.setText(getString(R.string.game_circle) + "(" +  mGamecircle_count +  ")");
        }
        initBtnState();
        initDownloadState();
        //初始化页面，未切换页面时，如果有则显示下载个数
        setRightHeadState(-1);
   //     mWaitingDialog.dismiss();
    }
	
    //加载猜你喜欢的游戏
    private void loadGuessGameList(){
    	int mpage=(int)(Math.random()*100);
    	RequestParams params = new RequestParams();
    	params.put("page", String.valueOf(mpage));
    	params.put("count", "4");
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

    				@Override
    				public void onFailure(Throwable error, String content) {
    					showNetErrorDialog(GameCircleDetailActivity.this,new ReConnectListener() {				
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
						LoginOutDialog dialog = new LoginOutDialog(
								GameCircleDetailActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
    				@Override
    				public void onSuccessForString(String content) {
    					super.onSuccessForString(content);
    				}

    				@Override
    				public void onSuccessForList(List list) {  
    					if(list!=null && list.size()>0){
    						guessGameList = (ArrayList<GameItem>) list;
        					if (guessLikeAdapter == null) {
        						guessLikeAdapter = new GameDetailGuessLikeAdapter(GameCircleDetailActivity.this, guessGameList,guessgamepop);    					
        						mPullListViewGuess.setAdapter(guessLikeAdapter);
        					}else{
        	            	guessLikeAdapter.updateData(guessGameList);
        					}
        					mPullListViewGuess.loadComplete();
        					mPullListViewGuess.loadingFinish();		
        			    	guessgamepop.setVisibility(View.VISIBLE);
        			    	guessgamepop.startAnimation(uploadanimation);   						               
     		               super.onSuccessForList(list);
    					}else{
    						Toast.makeText(GameCircleDetailActivity.this, getString(R.string.no_guessdata), Toast.LENGTH_SHORT).show();
    						return;
    					}   					
    				}
    			});
    }

	private void favGame() {
        if (mIntro == null) {
            return;
        }else if(1==Integer.valueOf(mIntro.getPlatform())){//如果是苹果平台
        	Toast.makeText(GameCircleDetailActivity.this, getString(R.string.cannot_fav), Toast.LENGTH_SHORT).show();
        	return;
        }else{
        	 fav = mIntro.getIsfavorite();
        if (String.valueOf(Contants.GAME_FAV.FAV).equals(fav)) {// if already fav then make it not fav
            fav = String.valueOf(Contants.GAME_FAV.UNFAV);
            resultErrorMsg = getResources().getString(R.string.unfav_error);
            resultMsg = getResources().getString(R.string.unfav_success);
        } else {
            fav = String.valueOf(Contants.GAME_FAV.FAV);
            resultErrorMsg = getResources().getString(R.string.fav_error);
            resultMsg = getResources().getString(R.string.fav_success);
        }
        RequestParams params = new RequestParams();
        params.put("optflag", String.valueOf(fav));
        params.put("gamecode", mIntro.getGamecode());
        MyHttpConnect.getInstance().post(HttpContants.NET.GAME_FAV, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable error) {
                    	if(flag){
                        Toast.makeText(GameCircleDetailActivity.this,
                        		resultErrorMsg, 0).show();
                        
                    	}
                    }
                    
                    @Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameCircleDetailActivity.this);
						dialog.create().show();
					}

                    @Override
                    public void onSuccess(String content) {
                    	super.onSuccess(content);
                        if (TextUtils.isEmpty(content)) {
                        	if(flag){
                            Toast.makeText(GameCircleDetailActivity.this,
                                    resultErrorMsg, 0).show();
                        	}
                            return;
                        }
                        RootPojo rootPojo = JsonUtils.fromJson(content,
                                RootPojo.class);
                        if (null == rootPojo) {
                        	if(flag){
                            Toast.makeText(GameCircleDetailActivity.this,
                                    resultErrorMsg, 0).show();
                        	}
                            return;
                        }
							if (RootPojo.SUCCESS.equals(rootPojo
									.getSuccessful())) {
								if (flag) {
									Toast.makeText(
											GameCircleDetailActivity.this,
											resultMsg, 0).show();
									if (String.valueOf(Contants.GAME_FAV.FAV)
											.equals(fav)) {
										mGameCircleFavBtn
												.setImageResource(R.drawable.img_fav1);
									} else {
										mGameCircleFavBtn
												.setImageResource(R.drawable.img_unfav);
									}
								}
								mIntro.setIsfavorite(fav);

							}
							super.onSuccess(content);
                    }
                });
        sendBroadcast(new Intent(Contants.ACTION.FAVCHANGE));
        }
    }
	public void finishDownloading(){
		if (mProgressReceiver != null && mHadRegistProgressReceiver) {
            unregisterReceiver(mProgressReceiver);
            mHadRegistProgressReceiver = false;
        }
	}
	
	private void hideKeyboard() {
		try {
		   InputMethodManager imm = (InputMethodManager)getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
		   if (imm.isActive()) {
			imm.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS); 
		   }
		}
		 catch (Exception e) {
			// TODO: handle exception
		}
	}


	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}


	protected void initData() {
		// TODO Auto-generated method stub
		
	}

    
	private SpannableStringBuilder getDownloadBtnStyle(String str, int downloadscore) {
		//填写个人信息获取积分的界面美化效果
		int index_begin = str.indexOf("(");
		log.i("index_begin = " + index_begin);
		SpannableStringBuilder style=new SpannableStringBuilder(str);

		style.setSpan(new AbsoluteSizeSpan(10,true), index_begin, index_begin + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), index_begin, index_begin + 3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		
		int number_begin = str.indexOf("+") + 1;
		log.i("number_begin = " + number_begin);
		int step = String.valueOf(downloadscore).length();
		log.i("step = " + step);
		style.setSpan(new AbsoluteSizeSpan(15,true), number_begin, number_begin + step, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#fcf32d")), number_begin, number_begin + step, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//金币图片
		Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.score_icon_42x42);
		ImageSpan imgSpan = new ImageSpan(bm, ImageSpan.ALIGN_BOTTOM);
		style.setSpan(imgSpan, number_begin + step,  number_begin + step + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		//反括号
		style.setSpan(new AbsoluteSizeSpan(10,true), number_begin + step + 1,number_begin + step + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		style.setSpan(new ForegroundColorSpan(Color.parseColor("#ffffff")), number_begin + step + 1,number_begin + step + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return style;
	}
}
