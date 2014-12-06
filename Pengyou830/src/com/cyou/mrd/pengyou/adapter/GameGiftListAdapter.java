package com.cyou.mrd.pengyou.adapter;

import java.util.List;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.GameGiftListItem;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameGiftListViewCache;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;

public class GameGiftListAdapter extends BaseAdapter implements OnClickListener{
	private Activity mContext;
	private List<GameGiftListItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private float mScale = 1.5f;
	
	public GameGiftListAdapter(Activity context, List<GameGiftListItem> data,
			PullToRefreshListView listview, Handler handler) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
		.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(15)).build();
		this.mScale  = Util.getScreenDensity(mContext);

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
		GameGiftListViewCache  viewCache ;
		if(convertView == null){
			convertView  =  mInflater.inflate(R.layout.game_gift_pkg_item, null);
			viewCache = new GameGiftListViewCache(convertView);
			convertView.setTag(viewCache);
			
		}
		else {
			viewCache = (GameGiftListViewCache)convertView.getTag();
		}
		GameGiftListItem giftListItem = mData.get(position);
		if(giftListItem != null){
			if(TextUtils.isEmpty(giftListItem.getUsergift()) || giftListItem.getUsergift().trim().equals("null")){
				viewCache.getGameGFGotIV().setVisibility(View.GONE);
			}
			else {
				viewCache.getGameGFGotIV().setVisibility(View.VISIBLE);
			}
			viewCache.getGameNameTV().setText(giftListItem.getGame().getName());
			CYImageLoader.displayIconImage(giftListItem.getGame().getIcon(),
					viewCache.getGameIconIV(), mOptions);
			viewCache.getGameGFCountTV().setText(giftListItem.getGiftname());
			int percent = 0;
			if(giftListItem.getGiftnum() > 0){
				percent =  (giftListItem.getLastnum() * 100 )/giftListItem.getGiftnum();
			}
			viewCache.getGameGFPercentTV().setText(String.valueOf(percent) + "%");
			LayoutParams  params  = (LayoutParams)viewCache.getGameGFProcessIV().getLayoutParams();			
			params.width = (int)(mScale*percent);
			viewCache.getGameGFProcessIV().setLayoutParams(params);
			
		}		
		
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
