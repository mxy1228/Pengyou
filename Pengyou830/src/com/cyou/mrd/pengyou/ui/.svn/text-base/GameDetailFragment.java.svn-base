package com.cyou.mrd.pengyou.ui;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.CaptureAdapter;
import com.cyou.mrd.pengyou.adapter.IntroPlayerAdapter;
import com.cyou.mrd.pengyou.adapter.SimilarGameAdapter;
import com.cyou.mrd.pengyou.adapter.VersionDialogAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.Intro;
import com.cyou.mrd.pengyou.entity.SimilarGameItem;
import com.cyou.mrd.pengyou.entity.base.IntroBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.CYBaseActivity.ReConnectListener;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.CollapsibleTextView;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GameDetailFragment extends BaseFragment implements OnClickListener {

    private CYLog log = CYLog.getInstance();
    private ImageButton mFreshIBtn;
    private ImageView mIconIV;
    private RatingBar mRB;
    private TextView mGameInfoTV;
    private TextView mDownloadCountTV;
    private TextView mPlayerCountTV;
    private GridView mPlayerGV;
    private GridView mCaptureGV;    
    private CollapsibleTextView mCollapsibleTextView;
    private Button mSelectGameVesion;
    private GridView mGuessGV;
    private AlertDialog mSharedDialog;
    private Dialog versionDialog ;
    private WaitingDialog mWaitingDialog;
    private HorizontalScrollView mFriendSV;
    private ProgressBar mReflushPB;
    private MyHttpConnect mConn;
    private String mGameCode;
    private Intro mIntro;
    private IntroPlayerAdapter mPlayerAdapter;
    private CaptureAdapter mCaptureAdapter;
    private ArrayList<SimilarGameItem> guessGameLst;    
    private SimilarGameAdapter similarGameAdapter;
    private CYBaseActivity mActivity;
  //  private FragmentActivity mActivity;
    private OnGetIntroListener mOnGetIntroListener;
    private RefreshReceiver mRefreshReceiver;
	private boolean mHadRegistRefreshReceiver = false;
    private ImageView platfrom;
    private Map<String,String> securitytip =new HashMap<String,String>();
    private ImageView tipimage1,tipimage2,tipimage3,tipimage4;
    private TextView tiptext1,tiptext2,tiptext3,tiptext4;
    private ProgressBar tv_stars5,tv_stars4,tv_stars3,tv_stars2,tv_stars1;;
    private float s5,s4,s3,s2,s1;    
    private float i5, i4, i3, i2, i1;;
    private TextView tv;
    private HorizontalScrollView mCaptureSV; //game image list
    private TextView intro_introduce_tv,mgamedetail_divider1,mgamedetail_divider2;
    private TextView you_may_like;
    private LinearLayout you_may_like_ly;
    private boolean mdispflag,mIsInstalled=false;//mdispflag为true时根据gamecode显示游戏详情      是否显示游戏最高版本，1为根据gamecode显示 
    private float totalpeople;
 //   private LinearLayout ly;
    private MyGameCircleInstallAppReceiver mInstallAppReceiver;
    private RelativeLayout go_appraisal_rl,already_appraisal_rl;
    private Button user_appraisal;
    private TextView user_appraisaltxt,user_appraisaltxt32,user_appraisaltxt31,user_appraisaltxt33;
    private RatingBar appraisal_ratingbar;
    private ImageView alreadyappraisal_img;
    private boolean isInstalledGame;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if(mRefreshReceiver == null){
			mRefreshReceiver = new RefreshReceiver();
		}
		if(!mHadRegistRefreshReceiver){
			//注册游戏圈动态发布后的消息
			mContext.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_STAR_SUCCESS));
			mHadRegistRefreshReceiver = true;
		}		
		if(mInstallAppReceiver==null){
     		mInstallAppReceiver=new MyGameCircleInstallAppReceiver();
     	}
     	if(mInstallAppReceiver!=null){
     		mContext.registerReceiver(mInstallAppReceiver,new IntentFilter(Contants.ACTION.GAME_INSTALL));
     	}
    }
    
    public GameDetailFragment(Activity activity) {
        this.mActivity = (CYBaseActivity)activity;
    }
    public GameDetailFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gameintro1, null);
        Intent intent = mContext.getIntent();
        mGameCode = intent.getStringExtra(Params.INTRO.GAME_CODE);
        mdispflag=intent.getBooleanExtra(Params.INTRO.GAME_DISPFLAG, false);
      
        if (TextUtils.isEmpty(mGameCode)) {
        	
            Toast.makeText(mContext,getResources().getString(R.string.game_code_null),Toast.LENGTH_SHORT).show();
            mContext.finish();
        }else{
        this.mWaitingDialog = new WaitingDialog(mContext);
        this.mWaitingDialog.show();
        initView(view);
        }
        return view;
    }

    private void initView(View view) { 
        mSelectGameVesion = (Button)view.findViewById(R.id.select_gameversion_ll);
        mSelectGameVesion.setOnClickListener(this);
        platfrom=(ImageView)view.findViewById(R.id.platform_image);        
        tipimage1=(ImageView)view.findViewById(R.id.game_tip_image1);
        tiptext1=(TextView)view.findViewById(R.id.game_tip_text1); 
        tipimage2=(ImageView)view.findViewById(R.id.game_tip_image2);
        tiptext2=(TextView)view.findViewById(R.id.game_tip_text2); 
        tipimage3=(ImageView)view.findViewById(R.id.game_tip_image3);
        tiptext3=(TextView)view.findViewById(R.id.game_tip_text3); 
        tipimage4=(ImageView)view.findViewById(R.id.game_tip_image4);
        tiptext4=(TextView)view.findViewById(R.id.game_tip_text4);    
        tv_stars5=(ProgressBar)view.findViewById(R.id.tv_stars5);
        tv_stars4=(ProgressBar)view.findViewById(R.id.tv_stars4);
        tv_stars3=(ProgressBar)view.findViewById(R.id.tv_stars3);
        tv_stars2=(ProgressBar)view.findViewById(R.id.tv_stars2);
        tv_stars1=(ProgressBar)view.findViewById(R.id.tv_stars1);
 
     //   ly=(LinearLayout)view.findViewById(R.id.linear_stars5);
        
        go_appraisal_rl=(RelativeLayout)view.findViewById(R.id.go_appraisal);
        already_appraisal_rl=(RelativeLayout)view.findViewById(R.id.go_appraisal2);
        appraisal_ratingbar=(RatingBar)view.findViewById(R.id.goappraisal_ratingbar);
        user_appraisal=(Button)view.findViewById(R.id.appraisal_btn);
        user_appraisaltxt=(TextView)view.findViewById(R.id.go_appraisaltxt);
        
        user_appraisaltxt32=(TextView)view.findViewById(R.id.go_appraisaltxt32);
        user_appraisaltxt31=(TextView)view.findViewById(R.id.go_appraisaltxt31);
        user_appraisaltxt33=(TextView)view.findViewById(R.id.go_appraisaltxt33);
        alreadyappraisal_img=(ImageView)view.findViewById(R.id.already_appraisal);
        user_appraisal.setOnClickListener(this); 
        intro_introduce_tv=(TextView)view.findViewById(R.id.intro_introduce_tv);
        you_may_like=(TextView)view.findViewById(R.id.text_divider);
        you_may_like_ly=(LinearLayout)view.findViewById(R.id.text_divider_ly);
        tv=(TextView)view.findViewById(R.id.arg_score);
        mFriendSV = (HorizontalScrollView) view
                .findViewById(R.id.intro_header_friend_sv);
        mIconIV = (ImageView) view.findViewById(R.id.intro_icon_iv);
        mRB = (RatingBar) view.findViewById(R.id.intro_rb);
        mRB.setRating(0);
        mGameInfoTV = (TextView) view.findViewById(R.id.intro_game_info_tv);
        mDownloadCountTV = (TextView) view
                .findViewById(R.id.intro_download_count_tv);
        mPlayerCountTV = (TextView) view
                .findViewById(R.id.intro_player_count_tv);
        //friend playing
        mPlayerGV = (GridView) view.findViewById(R.id.intro_player_gv);
        //game scroll image
        mCaptureSV = (HorizontalScrollView) view.findViewById(R.id.intro_capture_sv);
        mCaptureGV = (GridView) view.findViewById(R.id.intro_captrue_gv);
        
        mgamedetail_divider1=(TextView)view.findViewById(R.id.gamedetail_divider1) ;
        mgamedetail_divider2=(TextView)view.findViewById(R.id.gamedetail_divider2) ;
        mCollapsibleTextView = (CollapsibleTextView) view.findViewById(R.id.collapsibletextview);
 
        mReflushPB = (ProgressBar) view.findViewById(R.id.intro_reflush_pb);       
        mGuessGV = (GridView) view.findViewById(R.id.intro_guess_game);
        mFreshIBtn = (ImageButton) view.findViewById(R.id.intro_reflush_game);
        mFreshIBtn.setOnClickListener(this);

        mSharedDialog = new AlertDialog.Builder(mContext).setItems(
                new CharSequence[] { getString(R.string.share_game_friend),
                        getString(R.string.share_game_fzone),
                        getString(R.string.share_game_sina),
                        getString(R.string.share_game_others) },
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        switch (which) {
                        case 0:
                            GameItem item = mIntro.toGameItem();
                            intent.setClass(mContext,
                                    ShareToFriendActivity.class);
                            intent.putExtra(Params.SHARE_TO_FRIEND.GAME_ITEM,
                                    item);
                            startActivity(intent);
                            break;
                        case 1:
                            intent.setClass(mContext,
                                    ShareGameToFZoneAcivity.class);
                            intent.putExtra(Params.GAME_INFO.GAME_NAME,
                                    mIntro.getName());
                            intent.putExtra(Params.GAME_INFO.GAME_CODE,
                                    mIntro.getGamecode());
                            intent.putExtra(Params.GAME_INFO.GAME_ICON,
                                    mIntro.getIcon());
                            startActivity(intent);
                            break;
                        case 2:
                            isValidateSinaToken(mIntro.getName());
                            break;
						case 3:
							//分享
							Util.shareToChooseApps(mActivity, false);
							break;
                        default:
                            break;
                        }
                    }
                }).create();
        mSharedDialog.setTitle(getString(R.string.share));
        mSharedDialog.setCanceledOnTouchOutside(true);
        initData();
    }

    private void initData() {
        mConn = MyHttpConnect.getInstance();
        guessGameLst = new ArrayList<SimilarGameItem>();
        if (similarGameAdapter == null) {
            
            similarGameAdapter = new SimilarGameAdapter(mContext,guessGameLst, mGuessGV);
           
        }
        mGuessGV.setAdapter(similarGameAdapter);
        mGuessGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
               
                BehaviorInfo behaviorInfo = new BehaviorInfo(
                        CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_ICON_ID,
                        CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_ICON_NAME);
                CYSystemLogUtil.behaviorLog(behaviorInfo);
         
                behaviorInfo = new BehaviorInfo(
                        CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_DETAIL_ID,
                        CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_DETAIL_NAME);
                CYSystemLogUtil.behaviorLog(behaviorInfo);
                SimilarGameItem item = guessGameLst.get(position);
                Intent mIntent = new Intent();
                mIntent.setClass(mContext, GameCircleDetailActivity.class);
                mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
                mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
                mIntent.putExtra(Params.INTRO.GAME_CIRCLE, false);
                startActivity(mIntent);

               ((GameCircleDetailActivity) mContext).finishDownloading();
            }

        });
        requestIntro();
       // loadGuessGameLst();
        
    }

    @Override
    public void onDestroy() {
    	 try {
    		 if(versionDialog!=null){
    		 versionDialog.dismiss();
    		 }
         	if(mInstallAppReceiver!=null ){
         		mContext.unregisterReceiver(mInstallAppReceiver);
         	}         
         	if(mHadRegistRefreshReceiver && mRefreshReceiver != null){
        		mContext.unregisterReceiver(mRefreshReceiver);
    			mHadRegistRefreshReceiver = false;
    			mRefreshReceiver = null;
    		}

         } catch (Exception e) {
             log.e(e);
         }
         super.onDestroy();
    	

    }
//execute after mintro get successful
    private void initContentView() {
        mIconIV.setFocusable(true);
        mIconIV.setFocusableInTouchMode(true);
        mIconIV.requestFocus();
        log.i("initContentView start .........");
        if (!TextUtils.isEmpty(mIntro.getIcon())) {
            DisplayImageOptions iconOption = new DisplayImageOptions.Builder()
                    .cacheInMemory(true).cacheOnDisc(true)
                    .showImageForEmptyUri(R.drawable.icon_default)
                    .showImageOnFail(R.drawable.icon_default)
                    .showStubImage(R.drawable.icon_default)
                    .displayer(new RoundedBitmapDisplayer(Config.ROUND))
                    .build();
            CYImageLoader.displayIconImage(mIntro.getIcon(), mIconIV,
                    iconOption);
        }
       
        String argstar=String.valueOf(mIntro.getStar());
        Float argstarRb=mIntro.getStar();
        if(	!argstar.matches("^[-+]?(([0-9]+)([.]([0-9]+))?|([.]([0-9]+))?)$")){
        	log.d("argstar isn't data");
        	argstar= "0";
        	argstarRb=0.0f;
        }
        if(mIntro.getStardistr()!= null &&  !mIntro.getStardistr().isEmpty()){
//        if (mIntro.getStardistr()== null||  mIntro.getStardistr().isEmpty() ) {
//        	tv.setText(this.getString(R.string.arg_stars,argstar));
//        }else{        	
             s5 = (float)Integer.valueOf(mIntro.getStardistr().get("s5").toString());
             s4 = (float)Integer.valueOf(mIntro.getStardistr().get("s4").toString());
             s3 = (float)Integer.valueOf(mIntro.getStardistr().get("s3").toString());
             s2 = (float)Integer.valueOf(mIntro.getStardistr().get("s2").toString());
             s1 = (float)Integer.valueOf(mIntro.getStardistr().get("s1").toString());
//            if (s1 == 0 && s2 == 0 && s3 == 0 && s4 == 0 && s5 == 0) {
//            	tv.setText(this.getString(R.string.arg_stars,argstar));
//            } else if (s1 < 0 || s2 < 0 || s3 < 0 || s4 < 0 || s5 < 0) {
//            	tv.setText(this.getString(R.string.arg_stars,argstar));
//            } else {
               totalpeople = s1 + s2 + s3 + s4 + s5;
                if (s5 >0) {
                    i5 = s5*100 / totalpeople;
                    tv_stars5.setProgress((int)i5);
                }
                if (s4 > 0) {
                	 i4 = s4*100 / totalpeople;
                    tv_stars4.setProgress((int)i4);                
                }
                if (s3 > 0) {   
                	 i3 = s3*100 / totalpeople;
                     tv_stars3.setProgress((int)i3); 
                }
                if (s2 > 0) {        
                	 i2 = s2*100 / totalpeople;
                     tv_stars2.setProgress((int)i2); 
                }
                if (s1 > 0) {     
                	 i1 = s1*100 / totalpeople;
                     tv_stars1.setProgress((int)i1); 
                }
            //    log.d("-------进入游戏详情的各评分人数"+s5+","+s4+","+s3+","+s2+","+ s1);
            //    log.d("-------进入游戏详情的各评分条长"+i5+","+i4+","+i3+","+i2+","+ i1);
               
            }
        tv.setText(this.getString(R.string.arg_stars,argstar));
        try {
           // mRB.setRating(mIntro.getStar());
            mRB.setRating(argstarRb);
        } catch (Exception e) {
            log.e(e);
        }     
         isInstalledGame=false;
        mIsInstalled = Util.isInstallByread(mIntro.getIdentifier());	
        if (mIsInstalled) { 
        	if(TextUtils.isEmpty(mIntro.getVersioncode())||mIntro.getVersioncode().equals("0")){
        		isInstalledGame =true;
        	}else{
			isInstalledGame = Util.isInstalledGame(Util.getAppVersionCode(mIntro.getIdentifier()), mIntro.getVersioncode());
        	}
        }
        log.d("--------gamedetail getIsremarked     :"+mIntro.getIsremarked());
    	log.d("------评分会送的积分数  ："+mIntro.getGetoncescore());
        if(mIntro.getIsremarked()==1){//已经评分
        	log.d("-------游戏已评分");
        	go_appraisal_rl.setVisibility(View.GONE);  
        	already_appraisal_rl.setVisibility(View.VISIBLE);
        	switch(Integer.parseInt(mIntro.getChangeerror())){
        	case 0:
        	//	user_appraisaltxt32.setText(getAppraisalStyle("获得" + mIntro.getChangevalue() + "积分", mIntro.getChangevalue()));
        		user_appraisaltxt32.setText( mIntro.getChangevalue());
        		
        		break;
        	case 402:
        		user_appraisaltxt31.setText(R.string.appraisal_tip5);
        		user_appraisaltxt32.setVisibility(View.GONE);
        		user_appraisaltxt33.setVisibility(View.GONE);
        		alreadyappraisal_img.setVisibility(View.GONE);
        		break;
        	case 504:
        	//	user_appraisaltxt32.setText(getAppraisalStyle("获得80积分","80"));
        		user_appraisaltxt32.setText("0");
        		break;
        	default:
        		break;        		
        	}
        	if(!TextUtils.isEmpty(mIntro.getIsremarkednum())){
        		appraisal_ratingbar.setRating(Float.parseFloat(mIntro.getIsremarkednum()));
        	}
        	
        	
        }else{
        	go_appraisal_rl.setVisibility(View.VISIBLE);  
        	already_appraisal_rl.setVisibility(View.GONE);
        	if(isInstalledGame){//如果安装了，未评分        	
        		log.d("-------游戏安装了，未评分");
        		user_appraisal.setBackgroundDrawable(getResources().getDrawable(R.drawable.appraisal_btnxbg));
        		user_appraisal.setClickable(true);
        		if(!TextUtils.isEmpty(mIntro.getGetoncescore())){
        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,mIntro.getGetoncescore()));
        		}else{
        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,"0"));
        		}
        	}else{//未安装，未评分
        		log.d("-------游戏未安装了，未评分");
        		user_appraisal.setBackgroundResource(R.drawable.appraisalbtn_unclickable);
        		user_appraisal.setClickable(false);
        		user_appraisaltxt.setText(R.string.appraisal_tip1);
        	}
        }       
     
        if (!TextUtils.isEmpty(mIntro.getGtname())
                && !TextUtils.isEmpty(mIntro.getFullsize())
                && !TextUtils.isEmpty(mIntro.getVersion())) {
            String time=new SimpleDateFormat("yyyy-MM-dd").format(new Date(
                    mIntro.getCreatedate() * 1000));
            mGameInfoTV.setText(getString(R.string.game_info,
                    mIntro.getGtname(), Util.getGameSize(mIntro.getFullsize()),time));
        }
        //total downloadcount    
        int downloadcount=mIntro.getDownloadcnt();
        int a=downloadcount/10000;
        int b=downloadcount%10000;
        if(a==0){
        mDownloadCountTV.setText(getString(R.string.download_count,downloadcount,""));
        }else if(a<=9999){
        	if(b==0){
        		mDownloadCountTV.setText(getString(R.string.download_count,a,getString(R.string.tenthousand)));
        	}else{
        		mDownloadCountTV.setText(getString(R.string.download_count,a,getString(R.string.tenthousandmore)));
        	}                  	
        }else{
        	if(b==0){
        	mDownloadCountTV.setText(getString(R.string.download_count,1,getString(R.string.tenmillion)));
        	}else{
        		mDownloadCountTV.setText(getString(R.string.download_count,1,getString(R.string.tenmillionmore)));
        	}
        }
        if(!TextUtils.isEmpty(mIntro.getPlatform()))
         switch(Integer.valueOf(mIntro.getPlatform())){
          case 1:
              platfrom.setVisibility(View.VISIBLE);            
              break;
          case 2:
              break;
          default:
              break;
        }
        
      
        if(mIntro.getSecurityinfo() != null && mIntro.getSecurityinfo().size() > 0){
            int i=0;  
            securitytip=mIntro.getSecurityinfo();  
            String official= securitytip.get("official").toString();
            String safe=securitytip.get("security").toString();
            String isad=securitytip.get("adsdisplay").toString();
            String charge=securitytip.get("feetype").toString();
                if(official.equals("1")){
                    if(i<3){
                        i++;
                        tipimage1.setImageResource(R.drawable.game_official);
                        tiptext1.setText(this.getString(R.string.official));
                        tiptext1.setTextColor(this.getResources().getColor(R.color.tip_green));
                        tipimage1.setVisibility(View.VISIBLE);
                        tiptext1.setVisibility(View.VISIBLE);         
                    }
                }                
                if(safe.equals("0")){
                    if(i<3){
                        i++;
                        tipimage2.setImageResource(R.drawable.game_security);
                        tiptext2.setText(this.getString(R.string.safe));
                        tiptext2.setTextColor(this.getResources().getColor(R.color.tip_green));
                        tipimage2.setVisibility(View.VISIBLE);
                        tiptext2.setVisibility(View.VISIBLE);
                    }
                }
                if(isad.equals("0")){
                    if(i<3){
                        i++;
                        tipimage3.setImageResource(R.drawable.game_noads);
                        tiptext3.setText(R.string.noads);
                        tiptext3.setTextColor(this.getResources().getColor(R.color.tip_green));
                        tipimage3.setVisibility(View.VISIBLE);
                        tiptext3.setVisibility(View.VISIBLE); 
                    }
                }else if(isad.equals("1")){
                    if(i<3){
                        i++;
                        tipimage3.setImageResource(R.drawable.game_inside_ad);         
                        tiptext3.setText(R.string.ads);
                        tiptext3.setTextColor(this.getResources().getColor(R.color.tip_red));
                        tipimage3.setVisibility(View.VISIBLE);
                        tiptext3.setVisibility(View.VISIBLE); 
                    }
                }               
                if(charge.equals("1")){
                    if(i<3){
                        i++;
                        tipimage4.setImageResource(R.drawable.game_inside_charge);
                        tiptext4.setText(R.string.charge);
                        tiptext4.setTextColor(this.getResources().getColor(R.color.tip_red));
                        tipimage4.setVisibility(View.VISIBLE);
                        tiptext4.setVisibility(View.VISIBLE);
                    }
                }
            }   
        
        //显示当前游戏的版本
        if(TextUtils.isEmpty(mIntro.getSource())){
        	mSelectGameVesion.setText("V"+mIntro.getVersion()+"("+getResources().getString(R.string.unkown_source)+")");
        }else{
        	
        mSelectGameVesion.setText("V"+mIntro.getVersion()+"("+mIntro.getSource()+")");
        }
      
        if (mIntro.getFriendplaying() != null
                && !mIntro.getFriendplaying().isEmpty()) {
            mPlayerCountTV.setVisibility(View.VISIBLE);
            mFriendSV.setVisibility(View.VISIBLE);
            mPlayerCountTV.setText(getString(R.string.friends_play_same_game,
                    mIntro.getFriendplaying().size()));
        	mPlayerAdapter = new IntroPlayerAdapter(mContext,
                      mIntro.getFriendplaying());
            mPlayerGV.setAdapter(mPlayerAdapter);
       
            int columWidth = getResources().getDimensionPixelSize(R.dimen.padding)* 2+ getResources().getDimensionPixelSize(R.dimen.avatar_width);
            int totalWidth = mIntro.getFriendplaying().size() * columWidth;
            LayoutParams params = new LayoutParams(totalWidth,
                    LayoutParams.WRAP_CONTENT);
            mPlayerGV.setLayoutParams(params);
            mPlayerGV.setNumColumns(mIntro.getFriendplaying().size());
            mPlayerGV.setColumnWidth(columWidth);
            mPlayerGV.setStretchMode(GridView.NO_STRETCH);
        } else {
            mPlayerCountTV.setVisibility(View.GONE);
            mFriendSV.setVisibility(View.GONE);
        }
       
        mPlayerGV.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    int position, long id) {
                BehaviorInfo behaviorInfo = new BehaviorInfo(
                        CYSystemLogUtil.GAMEDETAIL.BTN_FRDDETAIL_ID,
                        CYSystemLogUtil.GAMEDETAIL.BTN_FRDDETAIL_NAME);
                CYSystemLogUtil.behaviorLog(behaviorInfo);
                FriendItem item = (FriendItem) mPlayerGV
                        .getItemAtPosition(position);
                Intent intent = new Intent(mContext,
                        FriendInfoActivity.class);
                intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
                intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
                //intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
                intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
                startActivity(intent);
                ((GameCircleDetailActivity) mContext).finishDownloading();
            }
        });
  
            ArrayList<String> game_images=new ArrayList<String>();
            if(mIntro.getSmallshots() != null && !mIntro.getSmallshots().isEmpty()){
            	game_images=mIntro.getSmallshots();
            }else{
            	if (mIntro.getGameshots() != null && !mIntro.getGameshots().isEmpty()){
            		game_images=mIntro.getGameshots();
            		log.d("---- 缩略图显示的大图 bigimage");
            	}else{
            		game_images=null;
            		log.d("----  bigimage and smallimage is null");
            	}
            }
//            if (mIntro.getSmallshots() == null || mIntro.getSmallshots().isEmpty()){
//            	if (mIntro.getGameshots() != null &&! mIntro.getGameshots().isEmpty()){
//            		game_images=mIntro.getGameshots();
//            //		log.d("---- 缩略图显示的大图 bigimage");
//            	}else{
//            		game_images=null;
//           // 		log.d("----  bigimage and smallimage is null");
//            	}
//            }else{
//            	game_images=mIntro.getSmallshots();
//          //  	log.d("-----  缩略图显示的小图smallimage");
//            }
//            
        	//if (mIntro.getSmallshots() != null&& !mIntro.getSmallshots().isEmpty()) {
        	if(game_images!= null&& !game_images.isEmpty()){
        	 mCaptureSV.setVisibility(View.VISIBLE);
             mCaptureGV.setVisibility(View.VISIBLE);
             mgamedetail_divider2.setVisibility(View.VISIBLE);
             mgamedetail_divider1.setVisibility(View.VISIBLE);
            int columWidth = getResources().getDimensionPixelSize(
                    R.dimen.capture_width);
            int totalWidth = mIntro.getSmallshots().size() * columWidth;
            LayoutParams params = new LayoutParams(totalWidth,
                    LayoutParams.WRAP_CONTENT);
            mCaptureGV.setLayoutParams(params);
            mCaptureGV.setNumColumns(mIntro.getSmallshots().size());
            mCaptureGV.setColumnWidth(columWidth);
            mCaptureGV.setStretchMode(GridView.NO_STRETCH);
            mCaptureAdapter = new CaptureAdapter(mContext,
            		game_images);
            
            mCaptureGV.setAdapter(mCaptureAdapter);
            mCaptureGV.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                        int arg2, long arg3) {
                    BehaviorInfo behaviorInfo = new BehaviorInfo(
                            CYSystemLogUtil.GAMEDETAIL.BTN_PIC_ID,
                            CYSystemLogUtil.GAMEDETAIL.BTN_PIC_NAME);
                    CYSystemLogUtil.behaviorLog(behaviorInfo);
                    Intent intent = new Intent(mContext,
                            CaptruesActivity.class);
                    if(mIntro.getSmallshots() != null && !mIntro.getSmallshots().isEmpty()){
                    intent.putStringArrayListExtra(Params.CAPTRUE.SMALLCAPTRUE,  mIntro.getSmallshots());     
                    }
                    if(mIntro.getGameshots() != null &&! mIntro.getGameshots().isEmpty()){
                    	 intent.putStringArrayListExtra(Params.CAPTRUE.BIGCAPTRUE,
                                 mIntro.getGameshots());    
                    }                                 
                    intent.putExtra(Params.CAPTRUE.CURRENT_PAGE, arg2);              
                    startActivity(intent);
                    
                }

            });
        }else{
        	 mCaptureSV.setVisibility(View.GONE);
             mCaptureGV.setVisibility(View.GONE);
             mgamedetail_divider2.setVisibility(View.GONE);
             mgamedetail_divider1.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(mIntro.getGamedesc())) {
        	mCollapsibleTextView.setVisibility(View.VISIBLE);
        	intro_introduce_tv.setVisibility(View.VISIBLE);
        	mCollapsibleTextView.setDesc(Html.fromHtml(mIntro.getGamedesc()),TextView.BufferType.NORMAL);           
        }
        
    }


    /**
     *similar game
     */
    private int mCurrentGuessPage = 1;

    private void loadGuessGameLst() {
        RequestParams params = new RequestParams();
        params.put("page", String.valueOf(mCurrentGuessPage));
        params.put("count", "3");
        params.put("gamecode", mGameCode);
        MyHttpConnect.getInstance().post2Json(
                HttpContants.NET.SIMILAR_GAMES,
                params,
                new JSONAsyncHttpResponseHandler<SimilarGameItem>(
                        JSONAsyncHttpResponseHandler.RESULT_LIST,
                        SimilarGameItem.class) {
                    @Override
                    public void onStart() {
                        mReflushPB.setVisibility(View.VISIBLE);
                        mFreshIBtn.setVisibility(View.GONE);
                    }

                    @Override
                    public void onSuccess(String content) {
                        mWaitingDialog.dismiss();
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
                        mWaitingDialog.dismiss();
                        log.e(error);
                        mReflushPB.setVisibility(View.GONE);
                        mFreshIBtn.setVisibility(View.VISIBLE);                        
                        mWaitingDialog.dismiss();
                       
                    }

                    @Override
                    public void onSuccessForList(List list) {
                    	log.i("similar game list request intro result = " + list);
						guessGameLst = (ArrayList<SimilarGameItem>) list;
						if(guessGameLst!=null && !guessGameLst.isEmpty()){
							you_may_like.setVisibility(View.VISIBLE);
							you_may_like_ly.setVisibility(View.VISIBLE);
							if (similarGameAdapter == null) {
								similarGameAdapter = new SimilarGameAdapter(
										mContext, guessGameLst, mGuessGV);
							}
							similarGameAdapter.updateData(guessGameLst);
							similarGameAdapter.notifyDataSetChanged();
							mCurrentGuessPage++;
							mReflushPB.setVisibility(View.GONE);
							mFreshIBtn.setVisibility(View.VISIBLE);
							super.onSuccessForList(list);
						}else{
							log.i("similar game list is null");
							return;
						}
//						if (guessGameLst==null||guessGameLst.isEmpty()) {
//							return;
//						} else {
//							you_may_like.setVisibility(View.VISIBLE);
//							you_may_like_ly.setVisibility(View.VISIBLE);
//							if (similarGameAdapter == null) {
//								similarGameAdapter = new SimilarGameAdapter(
//										mContext, guessGameLst, mGuessGV);
//							}
//							similarGameAdapter.updateData(guessGameLst);
//							similarGameAdapter.notifyDataSetChanged();
//							mCurrentGuessPage++;
//							mReflushPB.setVisibility(View.GONE);
//							mFreshIBtn.setVisibility(View.VISIBLE);
//							super.onSuccessForList(list);
//						}
					}
                });
    }

    public void onClick(View v) {
        switch (v.getId()) {
       
        case R.id.sub_header_bar_left_ibtn:
            BehaviorInfo behaviorInfo = new BehaviorInfo(
                    CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_GONE_ID,
                    CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_GONE_NAME);
            CYSystemLogUtil.behaviorLog(behaviorInfo);
            mContext.finish();
            break;
        case R.id.intro_reflush_game:
            behaviorInfo = new BehaviorInfo(
                    CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_REFRESH_ID,
                    CYSystemLogUtil.GAMEDETAIL.BTN_YOULIKE_REFRESH_NAME);
            CYSystemLogUtil.behaviorLog(behaviorInfo);
            loadGuessGameLst();
            break;
        case R.id.select_gameversion_ll:
        	if(mIntro!=null){
        		 if(mIntro.getVerlist()!=null && !mIntro.getVerlist().isEmpty()){
        	            showDialog();           			
        	     }
        	}
           else{
            	log.d("verlist is null");
            	return;
            }
            
            break;
        case R.id.appraisal_btn:
        	Intent intent=new Intent(mContext,SendGameCircleDynamicActivity.class);
        	intent.putExtra(Params.INTRO.GAME_CODE, mGameCode);
        	intent.putExtra(Params.Dynamic.GAME_CIRCLE_ID, String.valueOf(mIntro.getGcid()));
        	intent.putExtra(Params.INTRO.GAME_NAME, mIntro.getName());
        	intent.putExtra(Params.INTRO.GAME_ISINSTALLED, mIsInstalled );
        	startActivity(intent);
        break;
        default:

            break;
        }
    }
    
    private void showDialog () {
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog, null);
        if(versionDialog==null){
           versionDialog = new Dialog(mContext,R.style.custom_diaolog); 
        }
        versionDialog.setContentView(view,new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        Window dialogWindow = versionDialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        ListView listView = (ListView)view.findViewById(R.id.dialoglv);  
        GameCircleDetailActivity activity = (GameCircleDetailActivity)mContext;
        VersionDialogAdapter adapter = new VersionDialogAdapter(activity,mActivity, mIntro.getVerlist(),mIntro.getName());
        listView.setAdapter(adapter);
        Button button = (Button)view.findViewById(R.id.version_help_cancel);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                versionDialog.dismiss();
               
            }
        });
        versionDialog.setCanceledOnTouchOutside(true);
        versionDialog.setCancelable(true);
        versionDialog.show();
        
        
    }

    private void isValidateSinaToken(String gameName) {
        Intent intent = new Intent(mContext, ShareGameToSinaAcivity.class);
        intent.putExtra(Params.GAME_INFO.GAME_NAME, gameName);
        startActivity(intent);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            mOnGetIntroListener =(OnGetIntroListener)activity;

            mContext = activity;

            }catch(ClassCastException e){
            throw new ClassCastException(activity.toString()+"must implement OnGetIntroListener");
            }
    }

    public interface OnGetIntroListener{
        public void OnGetIntro(Intro mIntro);
    }
	
    private void requestIntro() {
        RequestParams params = new RequestParams();
        params.put("gamecode", mGameCode);
        if(mdispflag){
        params.put("dispflag", "1");
        }
        mConn.post(HttpContants.NET.GAME_INTRO, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        mWaitingDialog.dismiss();
                        super.onFailure(error, content);
                        try {
                        	mActivity.showNetErrorDialog(mActivity,new ReConnectListener() {   							
    							@Override
    							public void onReconnect() {
    								requestIntro();
    								loadGuessGameLst();
    								Intent intent = new Intent(
    				     					Contants.ACTION.REFRESH_GAME_CIRCLE);
    								mActivity.sendBroadcast(intent);
    								
    							}
    						});
						} catch (Exception e) {
							log.e(e);
						}
                    }
                    
                    @Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}

                    @Override
                    public void onSuccess(int statusCode, String content) {
                    	super.onSuccess(statusCode, content);
                        if (TextUtils.isEmpty(content)) {
                            return;
                        }
                        log.i("request intro result = " + content);
                        try {
                            mWaitingDialog.dismiss();
                            IntroBase base = new ObjectMapper().configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                                    .readValue(content, new TypeReference<IntroBase>() {
                                    });
                            if (base != null) {
                                mIntro = base.getData();
                                if (mIntro != null) {
                                	mGameCode=mIntro.getGamecode();
                        //        	log.d("-------requestIntro  服务器返回的gamecode   :"+mGameCode);
                                    log.i(" game cid = " + mIntro.getGcid());
                                    if(mActivity != null){
                                       Intent  intent = new Intent(Contants.ACTION.SEND_GAME_CIRCLE_GCID);
                                       intent.putExtra("gcid", String.valueOf(mIntro.getGcid()));
                                       intent.putExtra("pkname", mIntro.getIdentifier());
                                       mActivity.sendBroadcast(intent);
                                    }
                                    UserInfoUtil.setGameScore(mGameCode,mIntro.getIsremarked());

                                    initContentView();
                                    loadGuessGameLst();
                                    mOnGetIntroListener.OnGetIntro(mIntro);
        							log.i(" game circle count = " + mIntro.getGcacts());
                                }
                                else{
                                	 Toast.makeText(mContext, getResources().getString(R.string.game_code_null),Toast.LENGTH_SHORT).show();
                                	 mContext.finish();
                                }
                            }
                        } catch (Exception e) {
                            log.e(e);
                        }
                    }
                });
    }

    private class RefreshReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			String  gameScore  =  intent.getStringExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_STAR);		
			String  gameCode  =  intent.getStringExtra(Params.INTRO.GAME_CODE);	
	//		log.d("------发布动态广播传过来的gamecode   ："+gameCode);
			int[] starstr=intent.getIntArrayExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_STARDISTR);
			String misremarked=intent.getStringExtra("isremarked");
			String misremarkednum=intent.getStringExtra("isremarkednum");
			String mchangeerror=intent.getStringExtra("changeerror");
			String mchangevalue=intent.getStringExtra("changevalue");
			
	//		log.d("------当前页面游戏的gamecode    ："+mGameCode);
			if(!TextUtils.isEmpty(gameScore)&& mGameCode.equals(gameCode)){
			//	go_appraisal_rl.setVisibility(View.GONE);
				 
				  s5=(float)starstr[0];
				  s4=(float)starstr[1];
				  s3=(float)starstr[2];
				  s2=(float)starstr[3];
				  s1=(float)starstr[4];
		             float totalpeople = s1 + s2 + s3 + s4 + s5;
		             if (s5 >= 0) {
	//	            	 log.d("---------发布动态后come in 5星进度条更改");
		            	 i5 = s5*100 / totalpeople;
	                     tv_stars5.setProgress((int)i5);   
		                }
		                if (s4 >=0) {
		                	i4 = s4*100 / totalpeople;
		                     tv_stars4.setProgress((int)i4);               
		                }
		                if (s3 >=0) {
		                	i3 = s3*100 / totalpeople;
		                     tv_stars3.setProgress((int)i3);                 
		                }
		                if (s2 >= 0) {
		                	i2 = s2*100 / totalpeople;
		                     tv_stars2.setProgress((int)i2);                  
		                }
		                if (s1 >=0) {
		                	 i1 = s1*100 / totalpeople;
		                     tv_stars1.setProgress((int)i1);            
		                }	
		               
		             //   log.d("-------发布动态后的各评分人数"+s5+","+s4+","+s3+","+s2+","+ s1);
		             //   log.d("-------发布动态后的各评分条长"+i5+","+i4+","+i3+","+i2+","+ i1);
		                mRB.setRating(Float.parseFloat(gameScore));
						  tv.setText(gameScore);	
		            }   	
			if(misremarked.equals("1")){//已经评分
	        	log.d("-------游戏已评分");
	        	go_appraisal_rl.setVisibility(View.GONE);  
	        	already_appraisal_rl.setVisibility(View.VISIBLE);
	        	switch(Integer.parseInt(mchangeerror)){
	        	case 0:
	        	//	user_appraisaltxt32.setText(getAppraisalStyle("获得" + mchangevalue + "积分", mchangevalue));
	        		user_appraisaltxt32.setText( mchangevalue);
	        		break;
	        	case 402:
	        		user_appraisaltxt31.setText(R.string.appraisal_tip5);
	        		alreadyappraisal_img.setVisibility(View.GONE);
	        		user_appraisaltxt32.setVisibility(View.GONE);
	        		user_appraisaltxt33.setVisibility(View.GONE);
	        		break;
	        	case 504:
	        		//user_appraisaltxt32.setText(getAppraisalStyle("获得80积分","80"));
	        		user_appraisaltxt32.setText("0");
	        		break;
	        	default:
	        		break;        		
	        	}
	        	if(!TextUtils.isEmpty(misremarkednum)){
	        		appraisal_ratingbar.setRating(Float.parseFloat(misremarkednum));
	        	}
	        	
	        	
	        }
//			else{
//	        	go_appraisal_rl.setVisibility(View.VISIBLE);  
//	        	already_appraisal_rl.setVisibility(View.GONE);
//	        	if(isInstalledGame){//如果安装了，未评分        	
//	        		log.d("-------游戏安装了，未评分");
//	        		user_appraisal.setBackgroundDrawable(getResources().getDrawable(R.drawable.appraisal_btnxbg));
//	        		user_appraisal.setClickable(true);
//	        		if(!TextUtils.isEmpty(mIntro.getGetoncescore())){
//	        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,mIntro.getGetoncescore()));
//	        		}else{
//	        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,"0"));
//	        		}
//	        	}else{//未安装，未评分
//	        		log.d("-------游戏未安装了，未评分");
//	        		user_appraisal.setBackgroundResource(R.drawable.appraisalbtn_unclickable);
//	        		user_appraisal.setClickable(false);
//	        		user_appraisaltxt.setText(R.string.appraisal_tip1);
//	        	}
//	        }       
//	     
		        }
            }
  //监听游戏安装成功
  	private class MyGameCircleInstallAppReceiver extends BroadcastReceiver {
  		@Override
  		public void onReceive(Context context, Intent intent) {
  			if (intent != null) {
  			String packageName=intent.getStringExtra(DownloadParam.PACKAGE_NAME);
  			if ( mIntro.getIdentifier().equals(packageName)) {
  				log.d("-------游戏安装了，未评分");
        		user_appraisal.setBackgroundDrawable(getResources().getDrawable(R.drawable.appraisal_btnxbg));
        		user_appraisal.setClickable(true);
        		log.d("------评分会送的积分数  ："+mIntro.getGetoncescore());
        		if(!TextUtils.isEmpty(mIntro.getGetoncescore())){
        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,mIntro.getGetoncescore()));
        		}else{
        		user_appraisaltxt.setText(getString(R.string.appraisal_tip2,"0"));
        		}
  				
  			}
  			}				
  	}
  }	
	
}
