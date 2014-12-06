package com.cyou.mrd.pengyou.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.UpdateGameViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 游戏收藏
 * 
 * @author wangkang
 * 
 */
public class UpdateGameAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();

	private List<GameItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	public static final int SHOW_NUM = 2;
	private DownloadDao mDownloadDao;

	public UpdateGameAdapter(Context context, List<GameItem> data) {
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

	public void updateData(List<GameItem> data) {
		mData = data;
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

	private Map<Integer, View> viewCaches = new HashMap<Integer, View>();

	UpdateGameViewCache viewCache = null;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		try {
			convertView = viewCaches.get(position);
			if (convertView == null) {
				convertView = mInflater
						.inflate(R.layout.update_game_item, null);
				viewCache = new UpdateGameViewCache(convertView);
				convertView.setTag(viewCache);
			} else {
				viewCache = (UpdateGameViewCache) convertView.getTag();
			}
			viewCaches.put(position, convertView);
			final GameItem item = mData.get(position);
			if (null == item) {
				log.e("this game is null!");
				return convertView;
			}
			viewCache.getTxtGameName().setText(item.getName());
			viewCache.getTxtPlayerCount().setText(
					mContext.getString(R.string.update_game_version,
							Util.getGameSize(item.getFullsize()),
							item.getVersion()));
			CYImageLoader.displayIconImage(item.getIcon(),
					viewCache.getImgGameIcon(), mOptions);
			final Button btnUpdate = viewCache.getBtnDownloadGame();
			btnUpdate.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					downloadGame(btnUpdate, item);
				}
			});
			String packAge = item.getIdentifier();
			if (!TextUtils.isEmpty(packAge)) {
				DownloadItem downloadItem = mDownloadDao.getDowloadItem(
						item.getIdentifier(), item.getVersion());
				if (downloadItem == null
						|| TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
					btnUpdate.setText(R.string.download_btn_update);
				}else if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
					viewCache.getBtnDownloadGame().setText(R.string.done);
				}else {
					btnUpdate.setText(R.string.download_btn_updatting);
					btnUpdate.setBackgroundResource(R.drawable.btn_game_downloading_normal);
				}
			}
		} catch (Exception e) {
			log.e(e);
		}
		return convertView;
	}

	private void downloadGame(Button btnUpdate, GameItem item) {
		Intent intent = new Intent(mContext, DownloadService.class);
		DownloadItem downloadItem = mDownloadDao.getDowloadItem(
				item.getIdentifier(), item.getVersion());
		if (!TextUtils.isEmpty(downloadItem.getmPackageName())) {
			switch (downloadItem.getmState()) {
			case DownloadParam.C_STATE.DOWNLOADING:
				Toast.makeText(mContext,
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
				Toast.makeText(
						mContext,
						mContext.getText(R.string.had_contain_download_task_done),
						Toast.LENGTH_SHORT).show();
				break;
			}
		} else {
			if (!Util.isDownloadUrl(item.getFullurl()) || TextUtils.isEmpty(item.getVersion())) {
				Toast.makeText(mContext,
						R.string.download_url_error,
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (null != btnUpdate) {
				btnUpdate.setText(R.string.download_btn_updatting);
			}
//			Toast.makeText(mContext,
//					mContext.getText(R.string.begin_download_gamename),
//					Toast.LENGTH_SHORT).show();
			intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.ADD);
			downloadItem = new DownloadItem();
			downloadItem.setmName(item.getName());
			downloadItem.setmLogoURL(item.getIcon());
			downloadItem.setmSize(item.getFullsize());
			downloadItem.setmURL(item.getFullurl());
			downloadItem.setGameCode(item.getGamecode());
			downloadItem.setmPackageName(item.getIdentifier());
			downloadItem.setVersionName(item.getVersion());
			intent.putExtra(DownloadParam.DOWNLOAD_ITEM, downloadItem);
			mContext.startService(intent);
		}
	}

}
