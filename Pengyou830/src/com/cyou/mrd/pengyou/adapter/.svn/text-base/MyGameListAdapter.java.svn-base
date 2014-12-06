package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.MyGamePlayRecordDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.AdvertBean;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.GamePlayRecord;
import com.cyou.mrd.pengyou.entity.GuessGameItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.CountService;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.ui.GameCircleActivity;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.ui.GameGiftDetailActivity;
import com.cyou.mrd.pengyou.ui.GameSpecialDetailActivity;
import com.cyou.mrd.pengyou.ui.GameStoreWebActivity;
import com.cyou.mrd.pengyou.ui.MyGameActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.MyGameItemViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyGameListAdapter extends BaseAdapter implements OnClickListener{

	private CYLog log = CYLog.getInstance();

	private List<GameItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private MyHttpConnect myHttpConnect;
	public static final int SHOW_NUM = 2;
	private DownloadDao mDownloadDao;
	private Handler mHandler;
	private ListView mListView;
	private MyGamePlayRecordDao mMyGamePlayRecordDao;
	private boolean mRequesting;
	private int mLcdWidth;
	private float mDensity = 0;
	private GuessGameItem mGuessItem;
	private AdvertBean mAdvertBean;
	private boolean isBannerShow = true;
	boolean isClickMultiTimes = false;
	
	private int mWinHeight;
	private int mStatusBarHeight;
	private int mWinWidth;
	private AnimationSet myAnimationSet;

	private Activity mActivity;

	public MyGameListAdapter(Activity activity, List<GameItem> data,
			Handler handler,ListView listview,AdvertBean advertbean,GuessGameItem guessitem) {
		this.mActivity = activity;
		this.mContext = activity;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
		myHttpConnect = MyHttpConnect.getInstance();
		this.mDownloadDao = DownloadDao.getInstance(mContext);;
		this.mMyGamePlayRecordDao = new MyGamePlayRecordDao();
		this.mHandler = handler;
		this.mListView = listview;
		DisplayMetrics dm = mContext.getResources().getDisplayMetrics();
		this.mLcdWidth = dm.widthPixels;
		this.mDensity = dm.density;
		mWinHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
		mStatusBarHeight = getStatusBarHeight(mContext);
		mWinWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
		this.mGuessItem = guessitem;
		this.mAdvertBean = advertbean;
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	public void setGuessItem(GuessGameItem item){
		this.mGuessItem = item;
	}
	
	public void setAdvertBean(AdvertBean bean){
		this.mAdvertBean = bean;
	}
	
	public void changeData(List<GameItem> data){
		log.d("changData");
		this.mData = data;
		this.notifyDataSetChanged();
	}
	
	public void isBannerVisible(boolean b){
		this.isBannerShow = b;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try {
			log.d("getView");
			MyGameItemViewCache viewCache = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.my_game_item, null);
				viewCache = new MyGameItemViewCache(convertView);
				convertView.setTag(viewCache);
			}else{
				viewCache = (MyGameItemViewCache)convertView.getTag();
			}
			GameItem item = mData.get(position);
			if (null == item) {
				log.e("this game is null!");
				return convertView;
			}else{
				log.d("我的游戏="+item.getName());
			}
			if(position == 0 ){
				viewCache.getmGuessChangeBtn().setVisibility(View.VISIBLE);
				viewCache.getmGuessPBLL().setVisibility(View.GONE);
				mWinWidth = mActivity.getWindowManager().getDefaultDisplay().getWidth();
				mWinHeight = mActivity.getWindowManager().getDefaultDisplay().getHeight();
				viewCache.getmTopLL().setVisibility(View.VISIBLE);
				viewCache.getmBannerDivider().setVisibility(View.VISIBLE);
				int width = mWinWidth - 
						(mContext.getResources().getDimensionPixelSize(R.dimen.my_game_item_banner_margin) * 2);
				LayoutParams lp = new LayoutParams(width, width / 5);
				lp.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.my_game_item_banner_margin);
				lp.leftMargin = mContext.getResources().getDimensionPixelSize(R.dimen.my_game_item_banner_margin);
				lp.rightMargin = mContext.getResources().getDimensionPixelSize(R.dimen.my_game_item_banner_margin);
				lp.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.my_game_item_banner_margin);
				viewCache.getmBannerRL().setLayoutParams(lp);
				viewCache.getmBannerIV().setScaleType(ScaleType.FIT_XY);
				if(isBannerShow){
					viewCache.getmBannerRL().setVisibility(View.VISIBLE);
					CYImageLoader.displayGameImg(mAdvertBean.getImg()
							, viewCache.getmBannerIV()
							, new DisplayImageOptions.Builder()
							.cacheInMemory(true)
							.cacheOnDisc(true)
							.showImageOnFail(R.drawable.gamestore_recommend_defaul_bg)
							.showImageForEmptyUri(R.drawable.gamestore_recommend_defaul_bg)
							.build());
					viewCache.getmBannerIV().setOnClickListener(this);
					viewCache.getmBannerIV().setTag(new GameItem());
				}else{
					viewCache.getmBannerRL().setVisibility(View.GONE);
				}
				viewCache.getmBannerCloseBtn().setTag(new GameItem());
				viewCache.getmBannerCloseBtn().setOnClickListener(this);
				CYImageLoader.displayIconImage(mGuessItem.getIcon(), viewCache.getmGuessIconIV(), mOptions);
				DownloadItem downloadItem = mDownloadDao.getDowloadItem(mGuessItem.getIdentifier(),mGuessItem.getVersion());
				if(downloadItem != null && !TextUtils.isEmpty(downloadItem.getmPackageName())){
					switch (downloadItem.getmState()) {
					case DownloadParam.C_STATE.DOWNLOADING:
						viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
						viewCache.getmGuessDownloadBtn().setText(R.string.game_downloading);
						viewCache.getmGuessDownloadBtn().setTextColor(Color.parseColor("#919191"));
						break;
					case DownloadParam.C_STATE.DONE:
						viewCache.getmGuessDownloadBtn().setText(R.string.install);
						viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
						viewCache.getmGuessDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
						break;
					case DownloadParam.C_STATE.PAUSE:
						viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
						viewCache.getmGuessDownloadBtn().setText(R.string.game_downloading);
						viewCache.getmGuessDownloadBtn().setTextColor(Color.parseColor("#919191"));
						break;
					case DownloadParam.C_STATE.WAITING:
						viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
						viewCache.getmGuessDownloadBtn().setText(R.string.game_downloading);
						viewCache.getmGuessDownloadBtn().setTextColor(Color.parseColor("#919191"));
						break;
					default:
						viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
						viewCache.getmGuessDownloadBtn().setText(R.string.download);
						viewCache.getmGuessDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
						break;
					}
				}else{
					viewCache.getmGuessDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
					viewCache.getmGuessDownloadBtn().setText(R.string.download);
					viewCache.getmGuessDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
				}
				viewCache.getmGuessGameNameTV().setText(mGuessItem.getName());
				viewCache.getmGuessPlayCountTV().setText(mGuessItem.getRecdesc());
				viewCache.getmGuessChangeBtn().setTag(viewCache.getmGuessPBLL());
				viewCache.getmGuessChangeBtn().setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						v.setVisibility(View.GONE);
						LinearLayout ll = (LinearLayout)v.getTag();
						ll.setVisibility(View.VISIBLE);
						Message msg = new Message();
						msg.what = MyGameActivity.CHANGE_GUESS;
						mHandler.sendMessage(msg);
					}
				});
				viewCache.getmGuessDownloadBtn().setOnClickListener(this);
				viewCache.getmGuessDownloadBtn().setTag(new GameItem());
				viewCache.getmGuessRL().setVisibility(View.VISIBLE);
				viewCache.getmGuessRL().setOnClickListener(this);
			}else{
				viewCache.getmTopLL().setVisibility(View.GONE);
				viewCache.getmGuessRL().setVisibility(View.GONE);
				viewCache.getmBannerRL().setVisibility(View.GONE);
				viewCache.getmBannerDivider().setVisibility(View.GONE);
			}
			viewCache.getTxtGameName().setText(item.getName());
			viewCache.getTxtPlayerCount().setText(
					mContext.getString(R.string.mygame_playercount,item.getFrdplay()));
			if(item.getRecplaydat() <= 0) {
				viewCache.getTxtPlayTime().setText(mContext.getString(R.string.mygame_recent_does_not_play));
			} else {
			viewCache.getTxtPlayTime().setText(mContext.getString(R.string.mygame_recent_play_duration,Util.getFormatDate(item.getRecplaydat()/1000),item.getRecplaydur()));
			}
			String text = mContext.getString(R.string.game_circle_count,
					item.getTodaygcacts() <= 99 ? String.valueOf(item.getTodaygcacts()) : "99+",
					Integer.valueOf(item.getGcacts()) <= 999 ? item.getGcacts() : "999+");
			if(item.getTodaygcacts() != 0){
				SpannableStringBuilder sb = new SpannableStringBuilder(text);
				sb.setSpan(new ForegroundColorSpan(Color.parseColor("#FF4E00")),text.indexOf("今")+1, text.indexOf(","), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
				viewCache.getmGameCircleTV().setText(sb);
			}else{
				viewCache.getmGameCircleTV().setText(text);
			}
			CYImageLoader.displayIconImage(item.getIcon(),
					viewCache.getImgGameIcon(), mOptions);
			viewCache.getLayoutGameCircle().setOnClickListener(this);
			viewCache.getLayoutGameCircle().setTag(item);
			viewCache.getmGameCircleTV().setOnClickListener(this);
			viewCache.getmGameCircleTV().setTag(item);
			// 分享
			viewCache.getBtnShareGame().setOnClickListener(this);
			viewCache.getBtnShareGame().setTag(item);
			viewCache.getBtnGameDetail().setOnClickListener(this);
			viewCache.getBtnGameDetail().setTag(item);
			// 详细
			int ispublic = item.getIspublic();
			if (Contants.GAME_ISPUBLIC.GAME_PUBLIC == ispublic) {
				viewCache.getmShowGameBtn().setText(R.string.had_public);
				viewCache.getmShowGameBtn().setBackgroundResource(R.drawable.game_bar_public_btn_xbg);
				viewCache.getImgGameShow().setVisibility(View.GONE);
			} else {
				viewCache.getmShowGameBtn().setText(R.string.had_hide);
				viewCache.getmShowGameBtn().setBackgroundResource(R.drawable.game_bar_hide_btn_xbg);
				viewCache.getImgGameShow().setVisibility(View.VISIBLE);
			}
				viewCache.getmBtnPlayGame().setVisibility(View.VISIBLE);
				viewCache.getmBtnPlayGame().setText(R.string.game_play);
				viewCache.getmDownloadBtn().setVisibility(View.GONE);
				viewCache.getmBtnPlayGame().setOnClickListener(this);
				viewCache.getmBtnPlayGame().setTag(item);
			//判断要不要显示金币按钮
			log.i("item.getHasscore() = " + item.getHasscore() + "item.getIdentifier() = " + item.getIdentifier());
			if(item.getHasscore() > 0 && NetUtil.isNetworkAvailable()) {
				viewCache.getmExchangeCoinsLL().setVisibility(View.VISIBLE);
				viewCache.getmBtnPlayGame().setVisibility(View.GONE);
				viewCache.getmExchangeCoinsPb().setProgress(item.getHasscore() > 60 ? 100 : ((item.getHasscore() * 100) / 60));
			} else {
				viewCache.getmExchangeCoinsLL().setVisibility(View.GONE);
				viewCache.getmBtnPlayGame().setVisibility(View.VISIBLE);
			}
			viewCache.getmExchangeCoinsIv().setOnClickListener(this);
			viewCache.getmExchangeCoinsIv().setClickable(true);
			viewCache.getmExchangeCoinsIv().setTag(item);
			DownloadItem downloadItem = mDownloadDao.getDowloadItem(item.getIdentifier());
			boolean isUpdate = Util.isUpdate(Util.getAppVersionCode(item.getIdentifier()),item.getVersioncode());
			if(downloadItem != null && !TextUtils.isEmpty(downloadItem.getmPackageName())){
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DOWNLOADING:
					viewCache.getBtnUpdate().setText(R.string.updating);
					viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
					viewCache.getmDownloadBtn().setText(R.string.game_downloading);
					viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
					if (isUpdate) {
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
								, null
								, null
								, null);
						if(position < SHOW_NUM){
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
									, null);
						}else{
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, null
									, null);
						}
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(true);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(false);
					}
					break;
				case DownloadParam.C_STATE.DONE:
					viewCache.getBtnUpdate().setText(R.string.install);
					viewCache.getmDownloadBtn().setText(R.string.install);
					viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
					viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
					if (isUpdate) {
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
								, null
								, null
								, null);
						if(position < SHOW_NUM){
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
									, null);
						}else{
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, null
									, null);
						}
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(true);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(false);
					}
					break;
				case DownloadParam.C_STATE.PAUSE:
					viewCache.getBtnUpdate().setText(R.string.updating);
					viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
					viewCache.getmDownloadBtn().setText(R.string.game_downloading);
					viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
					if (isUpdate) {
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
								, null
								, null
								, null);
						if(position < SHOW_NUM){
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
									, null);
						}else{
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, null
									, null);
						}
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(true);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(false);
					}
					break;
				case DownloadParam.C_STATE.WAITING:
					viewCache.getBtnUpdate().setText(R.string.updating);
					viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
					viewCache.getmDownloadBtn().setText(R.string.game_downloading);
					viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
					if (isUpdate) {
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
								, null
								, null
								, null);
						if(position < SHOW_NUM){
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
									, null);
						}else{
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, null
									, null);
						}
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(true);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(false);
					}
					break;
				default:
					viewCache.getBtnUpdate().setText(R.string.mygame_item_btn_update);
					viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
					viewCache.getmDownloadBtn().setText(R.string.download);
					viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
					if (isUpdate) {
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
								, null
								, null
								, null);
						if(position < SHOW_NUM){
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
									, null);
						}else{
							viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
									, null
									, null
									, null);
						}
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
						viewCache.getmUpdateDotIV().setVisibility(View.VISIBLE);
						viewCache.getBtnUpdate().setEnabled(true);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
								, null
								, null
								, null);
						viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
						viewCache.getmUpdateDotIV().setVisibility(View.GONE);
						viewCache.getBtnUpdate().setEnabled(false);
					}
					break;
				}
			}else{
				viewCache.getBtnUpdate().setText(R.string.mygame_item_btn_update);
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getmDownloadBtn().setText(R.string.download);
				viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
				if (isUpdate) {
					viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
							, null
							, null
							, null);
					if(position < SHOW_NUM){
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
								, null);
					}else{
						viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
								, null
								, null
								, null);
					}
					viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
					viewCache.getmUpdateDotIV().setVisibility(View.VISIBLE);
					viewCache.getBtnUpdate().setEnabled(true);
				}else{
					viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
							, null
							, null
							, null);
					viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
							, null
							, null
							, null);
					viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
					viewCache.getmUpdateDotIV().setVisibility(View.GONE);
					viewCache.getBtnUpdate().setEnabled(false);
				}
			}
			// 判断是否隐藏更新按钮
			if (TextUtils.isEmpty(item.getVersion())
					|| Util.getAppVersionCode(item.getIdentifier()) == 0) {
				viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
						, null
						, null
						, null);
				viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
						, null
						, null
						, null);
				viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
				viewCache.getBtnUpdate().setEnabled(false);
				viewCache.getmUpdateDotIV().setVisibility(View.GONE);
			} 
//			else {
//				
//				if (isUpdate && position < SHOW_NUM) {
//					viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
//							, null
//							, mContext.getResources().getDrawable(R.drawable.img_game_update_new)
//							, null);
//					viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
//							, null
//							, null
//							, null);
//					viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
//					viewCache.getmUpdateDotIV().setVisibility(View.VISIBLE);
//					viewCache.getBtnUpdate().setEnabled(true);
//				} else if(isUpdate){
//					viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
//							, null
//							, null
//							, null);
//					viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_pic)
//							, null
//							, null
//							, null);
//					viewCache.getBtnUpdate().setTextColor(Color.parseColor("#333333"));
//					viewCache.getmUpdateDotIV().setVisibility(View.VISIBLE);
//					viewCache.getBtnUpdate().setEnabled(true);
//				}else{
//					viewCache.getTxtGameName().setCompoundDrawablesWithIntrinsicBounds(null
//							, null
//							, null
//							, null);
//					viewCache.getBtnUpdate().setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.my_game_item_bar_update_unavailable_pic)
//							, null
//							, null
//							, null);
//					viewCache.getBtnUpdate().setTextColor(Color.parseColor("#ababab"));
//					viewCache.getmUpdateDotIV().setVisibility(View.GONE);
//					viewCache.getBtnUpdate().setEnabled(false);
//				}
//				
//			}
			
			// 更新操作
			viewCache.getBtnUpdate().setOnClickListener(this);
			viewCache.getBtnUpdate().setTag(item);
			viewCache.getBtnUpdate().setTag(R.id.my_game_item_update_dot_iv, viewCache.getmUpdateDotIV());
			viewCache.getBtnUpdate().setTag(R.id.btn_update, viewCache.getBtnUpdate());
			viewCache.getmShowGameBtn().setOnClickListener(this);
			viewCache.getmShowGameBtn().setTag(item);
			viewCache.getmShowGameBtn().setTag(R.id.img_show, viewCache.getImgGameShow());
			LayoutParams barLayoutParam = (LayoutParams)viewCache.getLayoutBar().getLayoutParams();
			int widthSpec = MeasureSpec.makeMeasureSpec(mLcdWidth, MeasureSpec.EXACTLY);
			viewCache.getLayoutBar().measure(widthSpec, 0);
			if (item.isShowBar) {
				barLayoutParam.bottomMargin = 0;
				viewCache.getLayoutBar().setVisibility(View.VISIBLE);
			} else {
				barLayoutParam.bottomMargin = -mContext.getResources().getDimensionPixelSize(R.dimen.my_game_list_bar_height);
				viewCache.getLayoutBar().setVisibility(View.GONE);
			}
			viewCache.getmClickLL().setOnClickListener(this);
			viewCache.getmClickLL().setTag(item);
			if(item.getHastop() == Config.MY_GAME_ACTIVITY.YES){
				viewCache.getmActivityIV().setVisibility(View.VISIBLE);
			}else{
				viewCache.getmActivityIV().setVisibility(View.GONE);
			}
			if(item.getNoticount() == 0){
				viewCache.getmMsgBtn().setVisibility(View.GONE);
			}else{
				viewCache.getmMsgBtn().setVisibility(View.VISIBLE);
				viewCache.getmMsgBtn().setText(item.getNoticount() <= 99 ? String.valueOf(item.getNoticount()) : "99+");
			}
			SystemCountMsgItem.changeGameCircleMsg(false, mContext, item.getIdentifier(), item.getNoticount());
		} catch (Exception e) {
			log.e(e);
		}
		return convertView;
	}

	/**
	 * 玩游戏
	 * 
	 * @param gameCode
	 * @param gameName
	 */
	private void playGame(String gameCode, String gameName) {
		try {
			if (null == myHttpConnect) {
				myHttpConnect = MyHttpConnect.getInstance();
			}
			RequestParams params = new RequestParams();
			params.put("gamecode", gameCode);
			params.put("gamename", gameName);
			myHttpConnect.post2Json(HttpContants.NET.PLAY_GAME, params,
					new JSONAsyncHttpResponseHandler(
							JSONAsyncHttpResponseHandler.RESULT_BOOEALN, null) {
						@Override
						public void onSuccess(int statusCode, String content) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, content);
						}

						@Override
						public void onFailure(Throwable error) {
							super.onFailure(error);
						}

					});
		} catch (Exception e) {
			log.e(e);
		}
	}

//	/**
//	 * 游戏设置隐私
//	 * 
//	 * @param gameCode
//	 * @param gameName
//	 * @param ispublic
//	 *            1可见 0 不可见
//	 */
//	int isPublic = 0;


	@Override
	public void onClick(final View v) {
		final GameItem item = (GameItem)v.getTag();
		Intent gameCircleIntent = new Intent();
		BehaviorInfo behaviorInfo = null;
		DownloadItem downloadItem = null;
		Message msg = null;
		switch (v.getId()) {
		case R.id.game_circle:
			log.d("游戏圈ID:" + item.getGcid());

			if(TextUtils.isEmpty(item.getGcid()) || item.getGcid().equals("null")){
				Toast.makeText(mContext, R.string.game_circle_notexist, Toast.LENGTH_SHORT).show();
				return;
			}
			gameCircleIntent.putExtra(Params.Dynamic.GAME_CIRCLE_ID,
					item.getGcid());
			if (Util.isInstallByread(item.getIdentifier())) {
				gameCircleIntent.putExtra(
						Params.INTRO.GAME_ISINSTALLED, true);
			} else {
				gameCircleIntent.putExtra(
						Params.INTRO.GAME_ISINSTALLED, false);
			}
			gameCircleIntent.setClass(mContext,
					GameCircleActivity.class);
			gameCircleIntent.putExtra(Params.INTRO.GAME_CODE,
					item.getGamecode());
			gameCircleIntent.putExtra(Params.INTRO.GAME_NAME,
					item.getName());
			gameCircleIntent.putExtra(Params.INTRO.GAME_PKGE,item.getIdentifier());
			mContext.startActivity(gameCircleIntent);
			break;
		case R.id.my_game_item_game_circle:
			if(TextUtils.isEmpty(item.getGcid()) || item.getGcid().equals("null")){
				Toast.makeText(mContext, R.string.game_circle_notexist, Toast.LENGTH_SHORT).show();
				return;
			}
			gameCircleIntent.setClass(mContext,
					GameCircleActivity.class);
			gameCircleIntent.putExtra(Params.Dynamic.GAME_CIRCLE_ID,
					item.getGcid());
			if (Util.isInstallByread(item.getIdentifier())) {
				gameCircleIntent.putExtra(
						Params.INTRO.GAME_ISINSTALLED, true);
			} else {
				gameCircleIntent.putExtra(
						Params.INTRO.GAME_ISINSTALLED, false);
			}
			gameCircleIntent.putExtra(Params.INTRO.GAME_CODE,
					item.getGamecode());
			gameCircleIntent.putExtra(Params.INTRO.GAME_NAME,
					item.getName());
			gameCircleIntent.putExtra(Params.INTRO.GAME_PKGE,item.getIdentifier());
			mContext.startActivity(gameCircleIntent);
			break;
		case R.id.btn_share:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_GAME_SHARE_ID,
					CYSystemLogUtil.ME.BTN_GAME_SHARE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			msg = new Message();
			msg.obj = item;
			msg.what = MyGameActivity.SHOW_SHARE_DIALOH;
			mHandler.sendMessage(msg);
			break;
		case R.id.btn_detail:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_GAME_DETAIL_ID,
					CYSystemLogUtil.ME.BTN_GAME_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);

			Intent detailIntent = new Intent();
			detailIntent.setClass(mContext,
					GameCircleDetailActivity.class);
			detailIntent.putExtra(Params.INTRO.GAME_CODE,
					item.getGamecode());
			detailIntent.putExtra(Params.INTRO.GAME_NAME,
					item.getName());
			mContext.startActivity(detailIntent);
			break;
		case R.id.mygame_item_playgame_btn:
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
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_PLAY_GAME_ID,
					CYSystemLogUtil.ME.BTN_PLAY_GAME_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			if (!Util.isInstallByread(item.getIdentifier())) {
				Toast.makeText(
						mContext,
						mContext.getText(R.string.play_mygame_error),
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				ContentValues values = new ContentValues();
				values.put(DBHelper.MYGAME.PACKAGE_NAME, item.getIdentifier());
				new MyGameDao(mContext).update(item.getIdentifier(), System.currentTimeMillis());
				mContext.getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
				PackageManager manager = mContext.getPackageManager();
				Intent iStart = manager.getLaunchIntentForPackage(item.getIdentifier());
				if(iStart != null){
					mContext.startActivity(iStart);
					playGame(item.getGamecode(), item.getName());
				}else{
					ResolveInfo info = findResolveInfoByPackage(item.getIdentifier());
					if(info != null){
						ComponentName com = new ComponentName(info.activityInfo.packageName, info.activityInfo.name);
						Intent intent = new Intent();
						intent.setComponent(com);
						mContext.startActivity(intent);
						playGame(item.getGamecode(), item.getName());
					}
				}
				//启动统计Service
				Intent jifen_intent = new Intent(mContext, CountService.class);
				jifen_intent.putExtra("identifier",
						item.getIdentifier());
				mContext.startService(jifen_intent);
				playGame(item.getGamecode(), item.getName());
			}
			List<GameItem> temp = new ArrayList<GameItem>();
			Iterator<GameItem> itData = mData.iterator();
			while(itData.hasNext()){
				if(itData.next().getIdentifier().equals(item.getIdentifier())){
					itData.remove();
				}
			}
//			mData.remove(mData.indexOf(item));
			temp.add(item);
			temp.addAll(mData);
			mData.clear();
			mData.addAll(temp);
			MyGameListAdapter.this.notifyDataSetChanged();
			break;
		case R.id.mygame_item_coins_iv:
			v.setClickable(false);
			//点击了金币，不管网络怎样，都显示为启动
			item.setHasscore(0);
			notifyDataSetChanged();
			final int[] location = new int[2];
			v.getLocationOnScreen(location);
			location[0] += v.getWidth();
			final ImageView iv = (ImageView)v;
			//改方案了，不需要金币旋转
//			iv.setImageResource(R.drawable.anim_conspin);
//			final AnimationDrawable anim = (AnimationDrawable) iv.getDrawable();
//			anim.start();

			//直接飞金币
			Message msg_fly_coins = new Message();
			Bundle b = new Bundle();
			b.putInt("anim_start_x", location[0]);
			b.putInt("anim_start_y", location[1]);
			b.putFloat("scale", .8f);
			msg_fly_coins.setData(b);
			msg_fly_coins.what = MyGameActivity.COINS_ANIM_EXCHANGE;
			if(mHandler != null){
				mHandler.sendMessage(msg_fly_coins);
			}
			
			StringBuilder sb = new StringBuilder();
			sb.append("[");
			List<GamePlayRecord> gamePlayRecordList = mMyGamePlayRecordDao
					.selectGamePlayRecordList(item.getIdentifier(),String.valueOf(UserInfoUtil.getCurrentUserId()));
			int count = gamePlayRecordList.size();
			int j = 0;
			Iterator<GamePlayRecord> it = gamePlayRecordList.iterator();
			
			while (it.hasNext()) {
				GamePlayRecord record = it.next();
				if (j != count - 1) {
					sb.append("{\"begin\":\"" + record.getBegin() + "\",\"end\":\""
							+ record.getEnd() + "\"},");
				} else {
					sb.append("{\"begin\":\"" + record.getBegin() + "\",\"end\":\""
							+ record.getEnd() + "\"}");
				}
				j ++;
			}
			sb.append("]");
			String recordStr = sb.toString();
			log.i("jifen recordStr = " + recordStr);

			RequestParams recordparams = new RequestParams();
			recordparams.put("identifier", item.getIdentifier());
			recordparams.put("playrecordlist", recordStr);
			recordparams.put("act", String.valueOf(Contants.SCORE_ACTION.EXCHANGE_PLAY_GAME_SCORE));

			MyHttpConnect.getInstance().post(HttpContants.NET.SCORE_ACTION,
					recordparams, new AsyncHttpResponseHandler() {

						@Override
						public void onFailure(Throwable error) {
							//改方案了，不需要金币旋转
							if(mHandler != null){
								mHandler.post(new Runnable() {
									@Override
									public void run() {
										v.setClickable(true);
									}
								});
							}
							Toast.makeText(mContext,
									R.string.download_error_network_error,
									Toast.LENGTH_SHORT).show();
							super.onFailure(error);
						}
	
						@Override
						public void onLoginOut() {
							// TODO Auto-generated method stub
							LoginOutDialog dialog = new LoginOutDialog(mContext);
							dialog.create().show();
						}

						@Override
						public void onSuccess(String content) {
							log.i("jifen content = " + content);
							int coins = -1;
							try {
								//只要请求服务器成功，不管获取积分是否成功，都将数据库清除
								new MyGamePlayRecordDao().delete(item.getIdentifier(),String.valueOf(UserInfoUtil.getCurrentUserId()));
								new MyGameDao().clearCoin(item.getIdentifier());
								//改方案了，不需要金币旋转
								if(mHandler != null){
									mHandler.post(new Runnable() {
										@Override
										public void run() {
											v.setClickable(true);
										}
									});
								}
								
								if (TextUtils.isEmpty(content)) {
									Toast.makeText(mContext,
											R.string.download_error_network_error,
											Toast.LENGTH_SHORT).show();
									//服务器问题，白玩了
									return;
								}
								String successful = JsonUtils.getJsonValue(content,
										"successful");
								if (!TextUtils.isEmpty(successful)
										&& "1".equals(successful)) {
									String isSuccessful = JsonUtils.getJsonValue(
											JsonUtils.getJsonValue(content, "data"),
											"isSuccessful");// 兑换成功
									if (isSuccessful.equals("true")) { //如果isSuccessful为true,则data中的errorNo肯定为0，反之不行
										coins = Integer.parseInt(JsonUtils
												.getJsonValue(JsonUtils
														.getJsonValue(content,
																"data"),
														"changeValue"));//这次请求获取的真实的积分
										int addOrSub = Integer.parseInt(JsonUtils.getJsonValue(
												JsonUtils.getJsonValue(content,
														"data"), "addOrSub"));
										if(coins > 0) {
											//显示+多少金币的动画
											UserInfoUtil.sumAvailableScore(coins, addOrSub);
								            Message m = Message.obtain();
								            m.what = MyGameActivity.COINS_ANIM_PKG_NUMBER;
								            m.arg1 = coins;
								            if (mHandler != null) {
								            	mHandler.sendMessage(m);
											}
										} else {
											Toast.makeText(mContext,
													R.string.network_not_steady,
													Toast.LENGTH_SHORT).show(); // 网络不稳定
										}
									} else {
										coins = 0;
										String data_errorno = JsonUtils.getJsonValue(
												JsonUtils.getJsonValue(content, "data"),
												"errorNo");
										if (!TextUtils.isEmpty(data_errorno)) {
											switch (Integer.valueOf(data_errorno)) {
											case Contants.EXCHANGE_ERROR_NO.NOT_CONFORM_RULES:
												Toast.makeText(
														mContext,
														R.string.exchange_not_conform_rules,
														Toast.LENGTH_SHORT).show(); // 积分兑换已达到今日上限
												break;
											case Contants.EXCHANGE_ERROR_NO.TIMESTAMP_INVALID:
												Toast.makeText(
														mContext,
														R.string.play_game_timestamp_invalid,
														Toast.LENGTH_SHORT).show(); // 时间片不对，用户系统时间设置不准确
												break;
											default:
												Toast.makeText(mContext,
														R.string.network_not_steady,
														Toast.LENGTH_SHORT).show(); // 网络不稳定
												break;
											}
										}
									}
								}
								
								if(coins < 0) {
									Toast.makeText(mContext,
											R.string.download_error_network_error,
											Toast.LENGTH_SHORT).show();	
								}
							} catch (NumberFormatException e) {
								// TODO Auto-generated catch block
								log.e(e);
							} catch (NotFoundException e) {
								// TODO Auto-generated catch block
								log.e(e);
							} finally {
								v.setClickable(true);
							}
							super.onSuccess(content);
						}
					});

			break;
		case R.id.my_game_item_updat_btn:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_GAME_UPDATE_ID,
					CYSystemLogUtil.ME.BTN_GAME_UPDATE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);

			Intent intent = new Intent(mContext, DownloadService.class);
			downloadItem = mDownloadDao.getDowloadItem(
					item.getIdentifier(), item.getVersion());
			if (!TextUtils.isEmpty(downloadItem.getmPackageName())) {
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DOWNLOADING:
					
					break;
				case DownloadParam.C_STATE.PAUSE:
					Toast.makeText(
							mContext,
							mContext.getText(R.string.had_contain_download_task_pause),
							Toast.LENGTH_SHORT).show();
					break;
				case DownloadParam.C_STATE.WAITING:
					
					break;
				case DownloadParam.C_STATE.DONE:
					Toast.makeText(
							mContext,
							mContext.getText(R.string.had_contain_download_task_done),
							Toast.LENGTH_SHORT).show();
					break;
				}
			} else {
				ImageView dot = (ImageView)v.getTag(R.id.my_game_item_update_dot_iv);
				if (!Util.isDownloadUrl(item.getFullurl())
						|| TextUtils.isEmpty(item.getVersion())) {
					Toast.makeText(mContext,
							R.string.download_url_error,
							Toast.LENGTH_SHORT).show();
					return;
				}
				((Button)v).setText(R.string.updating);
				intent.putExtra(DownloadParam.STATE,DownloadParam.TASK.ADD);
				downloadItem = new DownloadItem();
				downloadItem.setmName(item.getName());
				downloadItem.setmLogoURL(item.getIcon());
				downloadItem.setmSize(item.getFullsize());
				downloadItem.setmURL(item.getFullurl());
				downloadItem.setGameCode(item.getGamecode());
				downloadItem.setmPackageName(item.getIdentifier());
				downloadItem.setVersionName(item.getVersion());
				intent.putExtra(DownloadParam.DOWNLOAD_ITEM,
						downloadItem);
				mContext.startService(intent);
				dot.setVisibility(View.GONE);
			}
			break;
		case R.id.my_game_item_showgame_btn:
			if(mRequesting){
				//TODO
				
				return;
			}else{
				
//				final ImageView iv = (ImageView)v.getTag(R.id.img_show);
				behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_GAME_ISPUBLIC_ID,
						CYSystemLogUtil.ME.BTN_GAME_ISPUBLIC_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				RequestParams params = new RequestParams();
				params.put("gamecode", item.getGamecode());
				params.put("gamename", item.getName());
				if (Contants.GAME_ISPUBLIC.GAME_PUBLIC == item.getIspublic()) {
					params.put("ispublic",String.valueOf(Contants.GAME_ISPUBLIC.GAME_UNPUBLIC));
				} else {
					params.put("ispublic",String.valueOf(Contants.GAME_ISPUBLIC.GAME_PUBLIC));
				}
				MyHttpConnect.getInstance().post(HttpContants.NET.GAME_PRIVACY,params, new AsyncHttpResponseHandler(){
					
					@Override
					public void onStart() {
						mRequesting = true;
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mRequesting = false;
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mContext);
						dialog.create().show();
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.d("隐藏我的游戏result = "+content);
						mRequesting = false;
						if(TextUtils.isEmpty(content)){
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if(obj.has("successful")){
								if(obj.getInt("successful") == 1){
									item.setIspublic(item.getIspublic() == Contants.GAME_ISPUBLIC.GAME_PUBLIC ? Contants.GAME_ISPUBLIC.GAME_UNPUBLIC : Contants.GAME_ISPUBLIC.GAME_PUBLIC);
									new MyGameDao(mContext).updateIsShown(item.getIdentifier(), item.getIspublic());
								}
							}
							int firstVisiblePosition = mListView.getFirstVisiblePosition();
							int index = mData.indexOf(item);
							View itemView = mListView.getChildAt(index - firstVisiblePosition + mListView.getHeaderViewsCount());
							if(itemView != null){
								MyGameItemViewCache viewCache = (MyGameItemViewCache)itemView.getTag();
								int ispublic = item.getIspublic();
								if (Contants.GAME_ISPUBLIC.GAME_PUBLIC == ispublic) {
									viewCache.getmShowGameBtn().setText(R.string.had_public);
									viewCache.getmShowGameBtn().setBackgroundResource(R.drawable.game_bar_public_btn_xbg);
									viewCache.getImgGameShow().setVisibility(View.GONE);
								} else {
									viewCache.getmShowGameBtn().setText(R.string.had_hide);
									viewCache.getmShowGameBtn().setBackgroundResource(R.drawable.game_bar_hide_btn_xbg);
									viewCache.getImgGameShow().setVisibility(View.VISIBLE);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
			}
			break;
		case R.id.mygame_item_download:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_DOWNLOAD_ID,
					CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_DOWNLOAD_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent i = new Intent(mContext,DownloadService.class);
			downloadItem = mDownloadDao.getDowloadItem(item.getIdentifier(),item.getVersion());
			if (!TextUtils.isEmpty(downloadItem.getmPackageName())) {
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DOWNLOADING:
					Toast.makeText(
							mContext,
							mContext.getText(R.string.had_contain_download_task),
							Toast.LENGTH_SHORT).show();
					break;
				case DownloadParam.C_STATE.PAUSE:
					Toast.makeText(
							mContext,
							mContext.getText(R.string.had_contain_download_task_pause),
							Toast.LENGTH_SHORT).show();
					break;
				case DownloadParam.C_STATE.DONE:
					//若下载完成则安装
					Intent iInstall = new Intent(Intent.ACTION_VIEW);
					iInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					iInstall.setDataAndType(Uri.fromFile(
							new File(SharedPreferenceUtil.getAPKPath(mContext),downloadItem.getmPackageName()+downloadItem.getVersionName()+ ".apk")),
							"application/vnd.android.package-archive");
					mContext.startActivity(iInstall);
					break;
				}
			} else {
				if (!Util.isDownloadUrl(item.getFullurl()) || TextUtils.isEmpty(item.getVersion())) {
					Toast.makeText(mContext,
							R.string.download_url_error,
							Toast.LENGTH_SHORT).show();
					return;
				}								
				((Button)v).setBackgroundResource(R.drawable.downloading_btn_xbg);
				((Button)v).setText(R.string.game_downloading);
				((Button)v).setTextColor(mContext.getResources().getColor(R.color.downloading_text));
				Toast.makeText(
						mContext,
						mContext.getText(R.string.begin_download_gamename),
						Toast.LENGTH_SHORT).show();
				i.putExtra(DownloadParam.STATE,
						DownloadParam.TASK.ADD);
				downloadItem = new DownloadItem();
				downloadItem.setmName(item.getName());
				downloadItem.setmLogoURL(item.getIcon());
				downloadItem.setmSize(item.getFullsize());
				downloadItem.setmURL(item.getFullurl());
				downloadItem.setGameCode(item.getGamecode());
				downloadItem.setmPackageName(item.getIdentifier());
				downloadItem.setVersionName(item.getVersion());
				i.putExtra(DownloadParam.DOWNLOAD_ITEM,downloadItem);
				mContext.startService(i);
			}
			break;
		case R.id.my_game_item_click_ll:
			msg = new Message();
			msg.what = MyGameActivity.BAR_ACTION;
			msg.arg1 = mData.indexOf(item);
			mHandler.sendMessage(msg);
			break;
		case R.id.my_game_item_guess_download_btn:
			Intent iDownload = new Intent();
			iDownload.setClass(mContext,DownloadService.class);
			downloadItem = mDownloadDao.getDowloadItem(mGuessItem.getIdentifier(),mGuessItem.getVersion());
			if (downloadItem != null && !TextUtils.isEmpty(downloadItem.getmPackageName())) {
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DOWNLOADING:
					Intent iDownding = new Intent(mContext, DownloadActivity.class);
					mContext.startActivity(iDownding);
					break;
				case DownloadParam.C_STATE.PAUSE:
					Toast.makeText(
							mContext,
							mContext.getText(R.string.had_contain_download_task_pause),
							Toast.LENGTH_SHORT).show();
					break;
				case DownloadParam.C_STATE.DONE:
					//若下载完成则安装
					Intent iInstall = new Intent(Intent.ACTION_VIEW);
					iInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					iInstall.setDataAndType(Uri.fromFile(
							new File(SharedPreferenceUtil.getAPKPath(mContext),downloadItem.getmPackageName()+downloadItem.getVersionName()+ ".apk")),
							"application/vnd.android.package-archive");
					mContext.startActivity(iInstall);
					break;
				}
			} else {
				if (!Util.isDownloadUrl(mGuessItem.getFullurl()) || TextUtils.isEmpty(mGuessItem.getVersion())) {
					Toast.makeText(mContext,
							R.string.download_url_error,
							Toast.LENGTH_SHORT).show();
					return;
				}								
//				((Button)v).setBackgroundResource(R.drawable.downloading_btn_xbg);
//				((Button)v).setText(R.string.game_downloading);
//				((Button)v).setTextColor(mContext.getResources().getColor(R.color.downloading_text));
				Toast.makeText(
						mContext,
						mContext.getText(R.string.begin_download_gamename),
						Toast.LENGTH_SHORT).show();
				iDownload.putExtra(DownloadParam.STATE,
						DownloadParam.TASK.ADD);
				downloadItem = new DownloadItem();
				downloadItem.setmName(mGuessItem.getName());
				downloadItem.setmLogoURL(mGuessItem.getIcon());
				downloadItem.setmSize(mGuessItem.getFullsize());
				downloadItem.setmURL(mGuessItem.getFullurl());
				downloadItem.setGameCode(mGuessItem.getGamecode());
				downloadItem.setmPackageName(mGuessItem.getIdentifier());
				downloadItem.setVersionName(mGuessItem.getVersion());
				iDownload.putExtra(DownloadParam.DOWNLOAD_ITEM,downloadItem);
				mContext.startService(iDownload);
				msg = new Message();
				msg.what = MyGameActivity.CHANGE_GUESS;
				mHandler.sendMessage(msg);
			}
			break;
		case R.id.my_game_item_banner_iv:
			intent = new Intent();
			String type = Util.getAdsType(mAdvertBean.getUri());
			if (TextUtils.isEmpty(type)) {
				Toast.makeText(
						mContext,
						mContext.getText(R.string.gamestore_error_params),
						0).show();
				return;
			}
			String value = "";
			value = Util.getGameCodeByUrl(mAdvertBean.getUri(), type);
			if (Contants.SCROLL_ADS.GAME.equals(type)) {// 游戏详细
				String gameName = mAdvertBean.getName();
				intent.putExtra(Params.INTRO.GAME_CODE, value);
				intent.setClass(mContext,
						GameCircleDetailActivity.class);
				intent.putExtra(Params.INTRO.GAME_NAME, gameName);
			} else if (Contants.SCROLL_ADS.TOPIC.equals(type)) {// 专题
				// 跳转至专题详细
				intent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID, value);
				intent.setClass(mContext,
						GameSpecialDetailActivity.class);
			} else if (Contants.SCROLL_ADS.CIRCLE.equals(type)) {// 游戏圈
				intent.putExtra(Params.Dynamic.GAME_CIRCLE_ID, value);
				if (Util.isInstallByread(mAdvertBean.getIdentifier())) {
					intent.putExtra(Params.INTRO.GAME_ISINSTALLED, true);
				} else {
					intent.putExtra(Params.INTRO.GAME_ISINSTALLED,
							false);
				}
				intent.setClass(mContext, GameCircleActivity.class);
				intent.putExtra(Params.INTRO.GAME_CODE,
						mAdvertBean.getGamecode());
			    intent.putExtra(Params.INTRO.GAME_PKGE,mAdvertBean.getIdentifier());
			} else if (Contants.SCROLL_ADS.WEB.equals(type)) {// HTML
				intent.putExtra(Params.GAMESTORE_HTML, value);
				intent.putExtra(Params.GAMESTORE_HTML_NAME,
						mAdvertBean.getName());
				intent.setClass(mContext, GameStoreWebActivity.class);
			} else if (Contants.SCROLL_ADS.GIFT.equals(type)) {// 礼包
				// 跳转至礼包详细
				intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTID, value);
				intent.setClass(mContext, GameGiftDetailActivity.class);
			} else if (Contants.SCROLL_ADS.GLOBALACT.equals(type)) {// 广场
				if(null!=CyouApplication.getMainActiv()){
					CyouApplication.getMainActiv().intent2GRelationCircle();
				}
				return;
			} else {
				Toast.makeText(
						mContext,
						mContext.getText(R.string.gamestore_error_params),
						0).show();
				return;
			}
			intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
			mContext.startActivity(intent);
			break;
		case R.id.my_game_item_banner_close_btn:
			isBannerShow = false;
			MyGameListAdapter.this.notifyDataSetChanged();
			break;
		case R.id.my_game_item_guess_rl:
			intent = new Intent();
			intent.setClass(mContext,
					GameCircleDetailActivity.class);
			intent.putExtra(Params.INTRO.GAME_CODE,
					mGuessItem.getGamecode());
			intent.putExtra(Params.INTRO.GAME_NAME,
					mGuessItem.getName());
			mContext.startActivity(intent); 
			break;
		default:
			break;
		}
	}
	
	private ResolveInfo findResolveInfoByPackage(String packageName){
		Intent i = new Intent(Intent.ACTION_MAIN,null);
		i.addCategory(Intent.CATEGORY_LAUNCHER);
		i.setPackage(packageName);
		List<ResolveInfo> apps = mContext.getPackageManager().queryIntentActivities(i, 0);
		if(apps != null){
			for(ResolveInfo info : apps){
				log.d(info.activityInfo.packageName);
				if(info.activityInfo.packageName.equals(packageName)){
					return info;
				}
			}
		}
		return null;
	}

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
	
}
