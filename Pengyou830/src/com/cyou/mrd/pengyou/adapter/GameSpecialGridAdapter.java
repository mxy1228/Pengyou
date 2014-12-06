package com.cyou.mrd.pengyou.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.GameSpecialGridViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 猜你喜欢的游戏
 * 
 * @author wangkang
 * 
 */
public class GameSpecialGridAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<GameItem> mData;
	private DisplayImageOptions mOptions;

	public GameSpecialGridAdapter(Context context, List<GameItem> data) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.showStubImage(R.drawable.icon_default).cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(12)).build();
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		}
		return 0;
	}

	public void updateData(ArrayList<GameItem> mLst) {
		mData = mLst;
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
		GameSpecialGridViewCache viewCache = null;
		if (convertView == null) {
			convertView = (LinearLayout) mInflater.inflate(R.layout.game_special_grid_item, null);
			viewCache = new GameSpecialGridViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (GameSpecialGridViewCache) convertView.getTag();
		}
		GameItem item = mData.get(position);
		CYImageLoader.displayIconImage(item.getIcon(),viewCache.getmAvatarIV(), mOptions);
		return convertView;
	}
	
}
