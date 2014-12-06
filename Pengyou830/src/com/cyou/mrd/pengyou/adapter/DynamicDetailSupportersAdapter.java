package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.DynamicSupporters;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.DynamicDetailSupporterViewCache;
import com.cyou.mrd.pengyou.widget.SupporterRoundImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class DynamicDetailSupportersAdapter extends BaseAdapter{

	private Context mContext;
	private List<DynamicSupporters> mData;
	private DisplayImageOptions mOption;
	private LayoutInflater mInflater;
	
	public DynamicDetailSupportersAdapter(List<DynamicSupporters> data,Context context){
		this.mContext = context;
		this.mData = data;
		this.mOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
		.build();
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		DynamicDetailSupporterViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dynamic_detail_supporter_item, null);
			viewCache = new DynamicDetailSupporterViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (DynamicDetailSupporterViewCache)convertView.getTag();
		}
		DynamicSupporters supporter = mData.get(position);
		SupporterRoundImageView  mRIV = (SupporterRoundImageView)viewCache.getmAvatarIV().findViewById(R.id.dynamic_detail_supporter_iv);
		CYImageLoader.displayImg(supporter.getAvatar(), mRIV.getImageView(), mOption);
		return convertView;
	}

}
