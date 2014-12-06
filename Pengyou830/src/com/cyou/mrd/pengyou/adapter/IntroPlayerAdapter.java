package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.IntroPlayerViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class IntroPlayerAdapter extends BaseAdapter {

	private Context mContext;
	private List<FriendItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOption;
	
	public IntroPlayerAdapter(Context context,List<FriendItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
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
		IntroPlayerViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.intro_player_item, null);
			viewCache = new IntroPlayerViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (IntroPlayerViewCache)convertView.getTag();
		}
		FriendItem item = mData.get(position);
		if(item != null){
			CYImageLoader.displayImg(item.getPicture(), viewCache.getmAvatarIV().getImageView(), mOption);
			viewCache.getmNickNameTV().setText(item.getNickname());
		}
		return convertView;
	}

}
