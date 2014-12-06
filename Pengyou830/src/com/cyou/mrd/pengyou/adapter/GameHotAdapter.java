package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.util.List;

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
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
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
import com.cyou.mrd.pengyou.viewcache.GameNewViewCache;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 
 * @author tuozhonghua_zk
 *
 */
public class GameHotAdapter extends BaseAdapter {

    private CYLog log = CYLog.getInstance();

    private List<GameItem> mData;
    private Context mContext;
    private LayoutInflater mInflater;
    private DisplayImageOptions mOptions;
    public static final int SHOW_NUM = 2;
    private DownloadDao mDownloadDao;
	private static final int MAX_GAME_NAME =7;
	boolean isClickMultiTimes = false;

    @SuppressWarnings("all")
    public GameHotAdapter(Context context, List<GameItem> data) {
        this.mContext = context;
        this.mData = data;
        this.mInflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.mOptions = new DisplayImageOptions.Builder()
                .showStubImage(R.drawable.icon_default)
                .showImageForEmptyUri(R.drawable.icon_default)
                .showImageOnFail(R.drawable.icon_default).cacheInMemory()
                .cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new RoundedBitmapDisplayer(15))
                .resetViewBeforeLoading().build();
        this.mDownloadDao = DownloadDao.getInstance(mContext);;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void updateData(List<GameItem> listData, int index) {
        mData = listData;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    // private Map<Integer, View> viewCaches = new HashMap<Integer, View>();

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GameNewViewCache viewCache = null;
        // convertView = viewCaches.get(position);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.game_new_item, null);
            viewCache = new GameNewViewCache(convertView);
            convertView.setTag(viewCache);
        } else {
            viewCache = (GameNewViewCache) convertView.getTag();
        }
        // viewCaches.put(position, convertView);
        final GameItem item = mData.get(position);
        if (null == item) {
            log.e("this game is null!");
            return convertView;
        }
		String gamename = item.getName();
		if (gamename.length() > MAX_GAME_NAME) {
			gamename = gamename.substring(0, MAX_GAME_NAME) + "...";
		}
        viewCache.getTxtGameName().setText(gamename);
        String userplay = item.getUsrplay();
        if (TextUtils.isEmpty(userplay)) {
            userplay = "0";
        }
		if (!TextUtils.isEmpty(item.getGtname())) {
			String gtName=item.getGtname();
			if(!TextUtils.isEmpty(gtName) && gtName.length()>2){
				gtName=gtName.substring(0, 2);
			}
			gtName = gtName;
			viewCache.getTxtPlayerCount().setText(mContext.getString(R.string.rank_game_world,userplay,Util.getGameSize(item.getFullsize()), gtName));
		} else {
		    viewCache.getTxtPlayerCount().setText(mContext.getString(R.string.rank_game_world,userplay,Util.getGameSize(item.getFullsize()),""));
		}
        try {
            viewCache.getRatingBar().setRating(item.getStar());
        } catch (Exception e) {
            log.e(e);
        }
        CYImageLoader.displayIconImage(item.getIcon(),viewCache.getImgGameIcon(), mOptions);
        final Button btnDownload = viewCache.getBtnDownloadGame();
        btnDownload.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(mContext, DownloadService.class);
                if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
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
                    PackageManager manager = mContext.getPackageManager();
                    Intent iStart = manager.getLaunchIntentForPackage(item
                            .getIdentifier());
                    Intent playIntent = new Intent(
                            "android.intent.action.playgame");
                    playIntent.putExtra(Params.GAME_INFO.GAME_IDENTIFY,
                            item.getIdentifier());
                    playIntent.putExtra(Params.GAME_INFO.GAME_TIMESTAMP,
                            System.currentTimeMillis());
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
                    DownloadItem downloadItem = mDownloadDao.getDowloadItem(
                            item.getIdentifier(), item.getVersion());

                    if (downloadItem != null
                            && !TextUtils.isEmpty(downloadItem
                                    .getmPackageName())) {
                        switch (downloadItem.getmState()) {
                        case DownloadParam.C_STATE.DOWNLOADING:
                            mContext.startActivity(new Intent(mContext,
                                    DownloadActivity.class));
                            break;
                        case DownloadParam.C_STATE.PAUSE:
                            mContext.startActivity(new Intent(mContext,
                                    DownloadActivity.class));
                            break;
                        case DownloadParam.C_STATE.WAITING:
                            mContext.startActivity(new Intent(mContext,
                                    DownloadActivity.class));
                            break;
                        case DownloadParam.C_STATE.DONE:
                            Intent i = new Intent(Intent.ACTION_VIEW);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            File file = new File(downloadItem.getPath());
                            if (file.exists()) {
                                i.setDataAndType(Uri.fromFile(file),
                                        "application/vnd.android.package-archive");
                                mContext.startActivity(i);
                            } else {
                                Toast.makeText(
                                        mContext,
                                        mContext.getText(R.string.download_install_file_noexit),
                                        Toast.LENGTH_SHORT).show();
                                intent.putExtra(DownloadParam.STATE,
                                        DownloadParam.TASK.DELETE);
                                intent.putExtra(DownloadParam.DOWNLOAD_ITEM,
                                        downloadItem);
                                mContext.startService(intent);
                                btnDownload
                                        .setBackgroundResource(R.drawable.download_btn_xbg);
                                btnDownload.setText(R.string.game_download);
                                btnDownload
                                        .setTextColor(mContext.getResources()
                                                .getColor(R.color.white));
                            }
                            break;
                        }
                    } else {
                        if (!Util.isDownloadUrl(item.getFullurl())
                                || TextUtils.isEmpty(item.getVersion())) {
                            Toast.makeText(mContext,
                                    R.string.download_url_error,
                                    Toast.LENGTH_SHORT).show();
                            return;
                        }
                        btnDownload.setBackgroundResource(R.drawable.downloading_btn_xbg);
                        btnDownload.setText(R.string.game_downloading);
                        btnDownload.setTextColor(mContext.getResources().getColor(R.color.downloading_text));
                        intent.putExtra(DownloadParam.STATE,DownloadParam.TASK.ADD);
                        downloadItem = new DownloadItem();
                        downloadItem.setmName(item.getName());
                        downloadItem.setmLogoURL(item.getIcon());
                        downloadItem.setmSize(item.getFullsize());
                        downloadItem.setmURL(item.getFullurl());
                        downloadItem.setGameCode(item.getGamecode());
                        downloadItem.setmPackageName(item.getIdentifier());
                        downloadItem.setVersionName(item.getVersion());
                        intent.putExtra(DownloadParam.DOWNLOAD_ITEM,downloadItem);
                        mContext.startService(intent);
                        //用户行为统计
                        BehaviorInfo behaviorInfo = new BehaviorInfo(CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_ID,
                                CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_HOTRANK_MOREGAME_GAMEDETAIL_DOWNLOAD_NAME);
                        CYSystemLogUtil.behaviorLog(behaviorInfo);
                    }
                }

            }
        });
        String packAge = item.getIdentifier();
        if (!TextUtils.isEmpty(packAge)) {
            if (Util.isInstallByread(packAge)) {// 若已安装
                btnDownload.setBackgroundResource(R.drawable.play_btn_xbg);
                btnDownload.setText(R.string.game_play);
                btnDownload.setTextColor(mContext.getResources().getColor(
                        R.color.white));
            } else {
                DownloadItem downloadItem = mDownloadDao.getDowloadItem(
                        item.getIdentifier(), item.getVersion());
                if (downloadItem == null
                        || TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
                    btnDownload
                            .setBackgroundResource(R.drawable.download_btn_xbg);
                    btnDownload.setText(R.string.game_download);
                    btnDownload.setTextColor(mContext.getResources().getColor(
                            R.color.white));
                } else {
                    if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
                        btnDownload
                                .setBackgroundResource(R.drawable.download_btn_xbg);
                        btnDownload.setText(R.string.download_btn_install);
                        btnDownload.setTextColor(mContext.getResources()
                                .getColor(R.color.white));
                    } else {
                        if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
                                || downloadItem.getmState() == DownloadParam.C_STATE.WAITING
                                || downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
                            btnDownload
                                    .setBackgroundResource(R.drawable.downloading_btn_xbg);
                            btnDownload.setText(R.string.game_downloading);
                            btnDownload.setTextColor(mContext.getResources()
                                    .getColor(R.color.downloading_text));
                        } else {
                            btnDownload
                                    .setBackgroundResource(R.drawable.download_btn_xbg);
                            btnDownload.setText(R.string.game_download);
                            btnDownload.setTextColor(mContext.getResources()
                                    .getColor(R.color.white));
                        }
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
}
