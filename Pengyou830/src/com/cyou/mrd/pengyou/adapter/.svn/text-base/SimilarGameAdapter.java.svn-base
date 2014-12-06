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
import com.cyou.mrd.pengyou.entity.SimilarGameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.SimilarGameViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SimilarGameAdapter extends BaseAdapter{
	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<SimilarGameItem> mData;
	private DisplayImageOptions mOptions;

	public SimilarGameAdapter(Context context, List<SimilarGameItem> data,
			GridView gd) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.showStubImage(R.drawable.icon_default).cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
	}

	public int getCount() {
		if (mData != null) {
			return mData.size();
		}
		return 0;
	}

	public void updateData(ArrayList<SimilarGameItem> mLst) {
		mData = mLst;
	}

	public Object getItem(int position) {
		return mData.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		SimilarGameViewCache viewCache = null;
		if (convertView == null) {
			convertView = (LinearLayout) mInflater.inflate(R.layout.similar_game_list_item, null);
			viewCache = new SimilarGameViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (SimilarGameViewCache) convertView.getTag();
		}
		SimilarGameItem item = mData.get(position);
		viewCache.getmNameTV().setText(item.getName());
	//	viewCache.getmGameType().setText("["+item.getGametype()+"]");	
		//viewCache.getFrdcount().setText(mContext.getString(R.string.mygame_playercount,item.getFrdplay()));
	//	viewCache.getFrdcount().setVisibility(View.VISIBLE);
		CYImageLoader.displayIconImage(item.getIcon(),viewCache.getmGameImage(), mOptions);

		return convertView;
	}

}
