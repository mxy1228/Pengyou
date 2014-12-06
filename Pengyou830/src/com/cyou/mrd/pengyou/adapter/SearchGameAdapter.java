package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.SecurityinfoBean;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.service.CountService;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameItemViewCache;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SearchGameAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();

	private List<GameItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private MyHttpConnect myHttpConnect;
	public static final int SHOW_NUM = 2;
	private DownloadDao mDownloadDao;
	boolean isClickMultiTimes = false;

	private static final int MAX_THOUSAND=9999;
	private static final int MAX_MILLON=99999999;
	private static final int TEN_THOUSAND=10000;
	private static final int MILLON_NUM=100000000;
	private static final int MAX_SECURITY_TXT =3;
	private static final int MAX_GAME_NAME =7;

	private boolean mRefresh=false;
	private boolean mDownloading=false;

	public SearchGameAdapter(Context context, List<GameItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading().build();
		myHttpConnect = MyHttpConnect.getInstance();
		this.mDownloadDao = DownloadDao.getInstance(mContext);;
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

	public void updateData(List<GameItem> lst) {
		mData = lst;
	}

	private Map<Integer, View> viewCaches = new HashMap<Integer, View>();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		GameItemViewCache viewCache = null;
		convertView = viewCaches.get(position);
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_game_item, null);
			viewCache = new GameItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (GameItemViewCache) convertView.getTag();
		}
		viewCaches.put(position, convertView);
		final GameItem item = mData.get(position);
		if (null == item) {
			log.e("this game is null!");
			return convertView;
		}
		String gamename="";
		if(item.getName()!=null){
			gamename=item.getName();
			if(gamename.length()>MAX_GAME_NAME){
				gamename=gamename.substring(0, MAX_GAME_NAME)+"...";
			}
		}
			
		viewCache.getTxtGameName().setText(gamename);
		/*
		String source = item.getSource();
		if (!TextUtils.isEmpty(source)) {
			viewCache.getTxtPlayerCount().setText(
					mContext.getString(R.string.source, source));
		} else {
			viewCache.getTxtPlayerCount().setText(
					Util.getGameSize(item.getFullsize()) + "MB");
		}
		*/
		try {
			viewCache.getRatingBar().setRating(item.getStar());
		} catch (Exception e) {
			log.e(e);
		}
		CYImageLoader.displayIconImage(item.getIcon(),
				viewCache.getImgGameIcon(), mOptions);
		//String friendCount = item.getFrdplay();
		/*if (TextUtils.isEmpty(friendCount)) {
			friendCount = "0";
		}*/
		String downloadCount = item.getDownloadcnt();
		if (TextUtils.isEmpty(downloadCount)) {
			downloadCount = "0";
		}
		int count=Integer.valueOf(downloadCount);
		if(count < MAX_THOUSAND){	
			
		}else if(count > MAX_THOUSAND&& count< MAX_MILLON){
			if(count%TEN_THOUSAND!=0){
				downloadCount = String.valueOf(count/TEN_THOUSAND)+mContext.getString(R.string.search_game_count_thousand)+"+";
			}else{
				downloadCount = String.valueOf(count/TEN_THOUSAND)+mContext.getString(R.string.search_game_count_thousand);
			}
		}else{
			if(count%MILLON_NUM!=0){
				downloadCount = String.valueOf(count/MILLON_NUM)+mContext.getString(R.string.search_game_count_millon)+"+";
			}else{
				downloadCount = String.valueOf(count/MILLON_NUM)+mContext.getString(R.string.search_game_count_thousand);
			}
		}
		viewCache.getTxtGameInfo().setText(
				mContext.getString(R.string.search_game_download_counts,downloadCount));
		//获取安全信息
		SecurityinfoBean securityinfo=item.getSecurityinfo();
		if(securityinfo != null){
			int countTxt=0;
			if(securityinfo.getSecurity().trim().equals("0")){//是否安全认证 0-安全认证，1-没有安全认证，N-未知
				viewCache.getTxtSecurity().setVisibility(View.VISIBLE); 
				countTxt++;
			}else{
				viewCache.getTxtSecurity().setVisibility(View.GONE); 
			}
			if(securityinfo.getOfficial().equals("1")){//是否官方版本 1-官方，0-非官方，N-未知
				viewCache.getTxtOfficial().setVisibility(View.VISIBLE); 
				countTxt++;
			}else{
				viewCache.getTxtOfficial().setVisibility(View.GONE);
			}
			String ads=securityinfo.getAdsdisplay();//是否有广告 0-无广告，1-有广告，N-未知
			if((ads).equals("1")){
				viewCache.getTxtAdsdisplay().setVisibility(View.VISIBLE); 
				viewCache.getTxtNoads().setVisibility(View.GONE);
				countTxt++;
			}else if((ads).equals("0")){
				viewCache.getTxtNoads().setVisibility(View.VISIBLE);
				viewCache.getTxtAdsdisplay().setVisibility(View.GONE);
				countTxt++;
			}else{
				viewCache.getTxtAdsdisplay().setVisibility(View.GONE);
				viewCache.getTxtNoads().setVisibility(View.GONE); 
			}
			if(securityinfo.getFeetype().equals("1")&&countTxt<MAX_SECURITY_TXT){//是否收费 0-免费，1-收费，N-未知
				viewCache.getTxtFeetype().setVisibility(View.VISIBLE);
			}else{
				viewCache.getTxtFeetype().setVisibility(View.GONE);
			}
		}			
		//final TextView txtDownload = viewCache.getTxtDownload();
		final Button btnDownload = viewCache.getBtnDownloadGame();
		btnDownload.setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(final View v) {
						BehaviorInfo behaviorInfo = new BehaviorInfo(
								CYSystemLogUtil.SEARCHGAME.BTN_DOWNLOAD_ID,
								CYSystemLogUtil.SEARCHGAME.BTN_DOWNLOAD_NAME);
						CYSystemLogUtil.behaviorLog(behaviorInfo);
						Intent intent = new Intent(mContext,
								DownloadService.class);
						if (Util.isInstallByread(item.getIdentifier())&&!mRefresh) {// 若已安装
																			// 直接运行
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
							PackageManager manager = mContext
									.getPackageManager();
							Intent iStart = manager
									.getLaunchIntentForPackage(item
											.getIdentifier());
//							Intent playIntent = new Intent("android.intent.action.playgame");
//							playIntent.putExtra(Params.GAME_INFO.GAME_IDENTIFY, item.getIdentifier());
//							playIntent.putExtra(Params.GAME_INFO.GAME_TIMESTAMP, System.currentTimeMillis());
							mContext.startActivity(iStart);
							//启动统计Service
							Intent jifen_intent = new Intent(mContext, CountService.class);
							jifen_intent.putExtra("identifier",
									item.getIdentifier());
							mContext.startService(jifen_intent);
							ContentValues values = new ContentValues();
							values.put(DBHelper.MYGAME.PACKAGE_NAME, item.getIdentifier());
							mContext.getContentResolver().update(Uri.parse(MyGameProvider.URI), values, null, null);
							playGame(item.getGamecode(), item.getName());
						} else {// 判断下载状态
							DownloadItem downloadItem = mDownloadDao
									.getDowloadItem(item.getIdentifier(),item.getVersion());
							if (!TextUtils.isEmpty(downloadItem
									.getmPackageName())) {
								switch (downloadItem.getmState()) {
								case DownloadParam.C_STATE.DOWNLOADING:
									/*Toast.makeText(
											mContext,
											mContext.getText(R.string.had_contain_download_task),
											Toast.LENGTH_SHORT).show();*/
									mContext.startActivity(new Intent(mContext,DownloadActivity.class));
									break;
								case DownloadParam.C_STATE.PAUSE:
									/*Toast.makeText(
											mContext,
											mContext.getText(R.string.had_contain_download_task_pause),
											Toast.LENGTH_SHORT).show();*/
									mContext.startActivity(new Intent(mContext,DownloadActivity.class));
									break;
								case DownloadParam.C_STATE.WAITING:
									mContext.startActivity(new Intent(mContext,DownloadActivity.class));
									break;
								case DownloadParam.C_STATE.DONE: //If done,install!
									Intent i = new Intent(Intent.ACTION_VIEW);
									i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);					
									File file = new File(downloadItem.getPath());
									if (file.exists()) {
										i.setDataAndType(Uri.fromFile(file),
												"application/vnd.android.package-archive");
										mContext.startActivity(i);
									}else {
										Toast.makeText(
												mContext,
												mContext.getText(R.string.download_install_file_noexit),
												Toast.LENGTH_SHORT).show();
										intent.putExtra(DownloadParam.STATE,
												DownloadParam.TASK.DELETE);
										intent.putExtra(DownloadParam.DOWNLOAD_ITEM,
												downloadItem);
										mContext.startService(intent);
										btnDownload.setBackgroundResource(
												R.drawable.download_btn_xbg);
										btnDownload.setText(
												R.string.game_download);
										btnDownload.setTextColor(
												mContext.getResources().getColor(R.color.white));
									}									
									/*
									Toast.makeText(
											mContext,
											mContext.getText(R.string.had_contain_download_task_done),
											Toast.LENGTH_SHORT).show();*/
									break;
								}
							} else {
								if (!Util.isDownloadUrl(item.getFullurl()) || TextUtils.isEmpty(item.getVersion())) {
									Toast.makeText(mContext,
											R.string.download_url_error,
											Toast.LENGTH_SHORT).show();
									return;
								}
								//((ImageButton) v).setImageResource(R.drawable.downloading_btn_normal);
								//txtDownload.setText(R.string.game_downloading);
								btnDownload.setBackgroundResource(R.drawable.downloading_btn_xbg);
								btnDownload.setText(mRefresh?R.string.updating:R.string.game_downloading);
								btnDownload.setTextColor(mContext.getResources().getColor(R.color.game_downloading_text_color));
								mDownloading = true;
//								Toast.makeText(
//										mContext,
//										mContext.getText(R.string.begin_download_gamename),
//										Toast.LENGTH_SHORT).show();
								intent.putExtra(DownloadParam.STATE,
										DownloadParam.TASK.ADD);
								downloadItem = new DownloadItem();
								downloadItem.setmName(item.getName());
								downloadItem.setmLogoURL(item.getIcon());
								downloadItem.setmSize(item.getFullsize());
								downloadItem.setmURL(item.getFullurl());
								downloadItem.setGameCode(item.getGamecode());
								downloadItem.setmPackageName(item
										.getIdentifier());
								downloadItem.setVersionName(item.getVersion());
								intent.putExtra(DownloadParam.DOWNLOAD_ITEM,
										downloadItem);
								mContext.startService(intent);
							}
						}

					}
				});
		viewCache.getTxtGameSize().setText(Util.getGameSize(item.getFullsize()) + "MB");
		String packAge = item.getIdentifier();
		
		if (!TextUtils.isEmpty(packAge)) {
			if (Util.isInstallByread(packAge)) {// 若已安装
				PackageManager manager = mContext.getPackageManager();
				try {
					int code = manager.getPackageInfo(item.getIdentifier(), 0).versionCode;
					if(code<Integer.valueOf(item.getVersioncode())){
						mRefresh=true;
					}
				} catch (Exception e) {
					// TODO: handle exception
					log.e(e);
				}		
				//viewCache.getBtnDownloadGame().setImageResource(R.drawable.play_btn_normal_mygame);
				//viewCache.getTxtDownload().setText(R.string.game_play);
				if(!mRefresh){
					btnDownload.setBackgroundResource(R.drawable.play_btn_xbg);
					btnDownload.setText(R.string.game_play);
					btnDownload.setTextColor(mContext.getResources().getColor(R.color.white));
				} else {
					btnDownload.setBackgroundResource(R.drawable.downloading_btn_xbg);
					btnDownload.setText(R.string.mygame_item_btn_update);
					btnDownload.setTextColor(mContext.getResources().getColor(R.color.game_downloading_text_color));
				}
				
			} else {
				DownloadItem downloadItem = mDownloadDao.getDowloadItem(item.getIdentifier(),item.getVersion());
				if (downloadItem == null
						|| TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
					if(position==0&&mDownloading){
						mDownloading = false;
						return convertView;
					}
					btnDownload.setBackgroundResource(R.drawable.download_btn_xbg);
					btnDownload.setText(R.string.game_download);
					btnDownload.setTextColor(mContext.getResources().getColor(R.color.white));
					
				} else {
					//viewCache.getBtnDownloadGame().setImageResource(R.drawable.downloading_btn_normal);
					//viewCache.getTxtDownload().setText(R.string.game_downloading);
					boolean install=(downloadItem.getmState()== DownloadParam.C_STATE.DONE);
					if(install){
						btnDownload.setBackgroundResource(R.drawable.download_btn_xbg);
						btnDownload.setText(R.string.install);
						btnDownload.setTextColor(mContext.getResources().getColor(R.color.white));
					}else{
						btnDownload.setBackgroundResource(R.drawable.downloading_btn_xbg);
						btnDownload.setText(R.string.game_downloading);
						btnDownload.setTextColor(mContext.getResources().getColor(R.color.game_downloading_text_color));
					}	
				}
			}
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
		if (null == myHttpConnect) {
			myHttpConnect = MyHttpConnect.getInstance();
		}
		RequestParams params = new RequestParams();
		params.put("gamecode", gameCode);
		params.put("gamename", gameName);
		myHttpConnect.post2Json(HttpContants.NET.PLAY_GAME, params,
				new JSONAsyncHttpResponseHandler(
						JSONAsyncHttpResponseHandler.RESULT_BOOEALN, null) {
				});
	}
}
