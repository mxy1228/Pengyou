package com.cyou.mrd.pengyou.adapter;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GuessGameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GuessGameItemViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GuessGameAdapter extends BaseAdapter implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private List<GuessGameItem> mData;
	private LayoutInflater mInfalter;
	private DisplayImageOptions mOptions;
	private DownloadDao mDownloadDao;
	
	public GuessGameAdapter(Context context,List<GuessGameItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInfalter = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.displayer(new RoundedBitmapDisplayer(Config.ROUND))
		.build();
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		log.d("getView");
		GuessGameItemViewCache viewCache = null;
		if(convertView == null){
			convertView = mInfalter.inflate(R.layout.guess_game_item, null);
			viewCache = new GuessGameItemViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (GuessGameItemViewCache)convertView.getTag();
		}
		GuessGameItem item = mData.get(position);
		if(item == null){
			log.e("item is null");
			return convertView;
		}
		CYImageLoader.displayIconImage(item.getIcon(), viewCache.getmIconIV(), mOptions);
		viewCache.getmNameTV().setText(item.getName());
		viewCache.getmPlayNumTV().setText(mContext.getString(R.string.mygame_playercount, item.getFrdplay()));
		viewCache.getmDownloadBtn().setOnClickListener(this);
		viewCache.getmDownloadBtn().setTag(item);
		DownloadItem downloadItem = mDownloadDao.getDowloadItem(item.getIdentifier(),item.getVersion());
		if(downloadItem != null && !TextUtils.isEmpty(downloadItem.getmPackageName())){
			switch (downloadItem.getmState()) {
			case DownloadParam.C_STATE.DOWNLOADING:
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
				viewCache.getmDownloadBtn().setText(R.string.game_downloading);
				viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
				break;
			case DownloadParam.C_STATE.DONE:
				viewCache.getmDownloadBtn().setText(R.string.install);
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
				break;
			case DownloadParam.C_STATE.PAUSE:
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
				viewCache.getmDownloadBtn().setText(R.string.game_downloading);
				viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
				break;
			case DownloadParam.C_STATE.WAITING:
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.downloading_btn_xbg);
				viewCache.getmDownloadBtn().setText(R.string.game_downloading);
				viewCache.getmDownloadBtn().setTextColor(Color.parseColor("#919191"));
				break;
			default:
				viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
				viewCache.getmDownloadBtn().setText(R.string.download);
				viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
				break;
			}
		}else{
			viewCache.getmDownloadBtn().setBackgroundResource(R.drawable.download_btn_xbg);
			viewCache.getmDownloadBtn().setText(R.string.download);
			viewCache.getmDownloadBtn().setTextColor(mContext.getResources().getColor(R.color.white));
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		GuessGameItem item = (GuessGameItem)v.getTag();
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.guess_game_item_download_btn:
			intent.setClass(mContext,DownloadService.class);
			DownloadItem downloadItem = mDownloadDao.getDowloadItem(item.getIdentifier(),item.getVersion());
			if (downloadItem != null && !TextUtils.isEmpty(downloadItem.getmPackageName())) {
				switch (downloadItem.getmState()) {
				case DownloadParam.C_STATE.DONE:
					//若下载完成则安装
					Intent iInstall = new Intent(Intent.ACTION_VIEW);
					iInstall.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					iInstall.setDataAndType(Uri.fromFile(
							new File(SharedPreferenceUtil.getAPKPath(mContext),downloadItem.getmPackageName()+downloadItem.getVersionName()+ ".apk")),
							"application/vnd.android.package-archive");
					mContext.startActivity(iInstall);
					break;
				default:
					Intent iDownding = new Intent(mContext, DownloadActivity.class);
					mContext.startActivity(iDownding);
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
				intent.putExtra(DownloadParam.STATE,
						DownloadParam.TASK.ADD);
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
			}
			break;

		default:
			break;
		}
	}

}
