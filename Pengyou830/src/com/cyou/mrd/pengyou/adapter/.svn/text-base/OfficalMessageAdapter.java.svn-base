package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.MessageItem;
import com.cyou.mrd.pengyou.viewcache.OfficalMessageItemViewCache;

public class OfficalMessageAdapter extends BaseAdapter {

	private Context mContext;
	private List<MessageItem> mData;
	private LayoutInflater mInflater;
	
	public OfficalMessageAdapter(Context context,List<MessageItem> data){
		this.mContext = context;
		this.mData = data;
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
		OfficalMessageItemViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.offical_message_item, null);
			viewCache = new OfficalMessageItemViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (OfficalMessageItemViewCache)convertView.getTag();
		}
		viewCache.getmAvatarIV().setImageResource(R.drawable.icon);
		return convertView;
	}

}
