package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.RecentlyViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class RecentlyGameAdapter extends BaseAdapter {

	private Context mContext;
	private List<GameItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	
	public RecentlyGameAdapter(Context context,List<GameItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.displayer(new RoundedBitmapDisplayer(Config.ROUND))
		.build();
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
		RecentlyViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.recently_game_item, null);
			viewCache = new RecentlyViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (RecentlyViewCache)convertView.getTag();
		}
		GameItem item = mData.get(position);
		viewCache.getmTV().setText(item.getName());
		CYImageLoader.displayIconImage(item.getIcon()
				, viewCache.getmIconIV()
				, mOptions);
		return convertView;
	}

}
