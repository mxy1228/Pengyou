package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RecommendGameViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 猜你喜欢的游戏
 * 
 * @author wangkang
 * 
 */
public class GameStoreHotListAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<GameItem> mData;
	private DisplayImageOptions mOptions;
	ColorStateList csl = null;
	private String moreGame;

	public GameStoreHotListAdapter(Context context, List<GameItem> data,
			GridView gd) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.icon_default)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15)).build();
		csl = (ColorStateList) context.getResources().getColorStateList(
				R.color.gamestore_more);
		moreGame = mContext.getResources().getString(
				R.string.gamestore_more_game);
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size() + 1;
		}
		return 0;
	}

	public void updateData(List<GameItem> mLst) {
		mData = mLst;
	}

	@Override
	public Object getItem(int position) {
		if (position >= mData.size()) {
			return null;
		}
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		RecommendGameViewCache viewCache = null;
		if (convertView == null) {
			convertView = (LinearLayout) mInflater.inflate(
					R.layout.gamestore_hotlist_item, null);
			viewCache = new RecommendGameViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (RecommendGameViewCache) convertView.getTag();
		}
		if (position >= mData.size()) {
			if (csl != null) {
				viewCache.getmNameTV().setTextColor(csl);
			}
			viewCache.getmAvatarIV().setImageResource(
					R.drawable.gamestore_more_xbg);
			viewCache.getmNameTV().setText(moreGame);
			viewCache.getmNameTV().setPadding(0, 6, 0, 0);
			viewCache.getmTxtGameInfo().setVisibility(View.GONE);
		} else {
			GameItem item = mData.get(position);
			if (item == null || TextUtils.isEmpty(item.getGamecode())) {
				viewCache.getmAvatarIV().setImageResource(
						R.drawable.gamestore_more_xbg);
				viewCache.getmNameTV().setPadding(0, 6, 0, 0);
				viewCache.getmNameTV().setText(moreGame);
				viewCache.getmTxtGameInfo().setVisibility(View.GONE);
				if (csl != null) {
					viewCache.getmNameTV().setTextColor(csl);
				}
			} else {
				if ("1".equals(item.getGamecode())) {
					return convertView;
				}
				String gameSize = Util.getGameIntSize(item.getFullsize()) + "M";
				String gameName = item.getName();
				// if (gameName.length() > 5) {
				// gameName = gameName.substring(0, 5);
				// gameName += "..";
				// }
				viewCache.getmNameTV().setText(gameName);
				if (!TextUtils.isEmpty(item.getGtname())) {
					String gtName = item.getGtname();
					if (!TextUtils.isEmpty(gtName) && gtName.length() > 2) {
						gtName = gtName.substring(0, 2);
					}
					viewCache.getmTxtGameInfo().setText(
							gameSize + "[" + gtName + "]");
				}
				CYImageLoader.displayIconImage(item.getIcon(),
						viewCache.getmAvatarIV(), mOptions);
				viewCache.getmAvatarIV().setBackgroundResource(
						R.drawable.gamestore_icon_default);
				if (position == 0) {
					viewCache.getImgToday().setVisibility(View.VISIBLE);
					viewCache.getImgToday().setImageResource(
							R.drawable.gamestore_rank_first);
				} else if (position == 1) {
					viewCache.getImgToday().setVisibility(View.VISIBLE);
					viewCache.getImgToday().setImageResource(
							R.drawable.gamestore_rank_second);
				} else if (position == 2) {
					viewCache.getImgToday().setVisibility(View.VISIBLE);
					viewCache.getImgToday().setImageResource(
							R.drawable.gamestore_rank_thrid);
				} else {
					viewCache.getImgToday().setVisibility(View.GONE);
				}
			}
		}
		return convertView;
	}
}
