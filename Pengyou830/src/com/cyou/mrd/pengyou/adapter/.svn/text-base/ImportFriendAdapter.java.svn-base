package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.SNSItem;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.ImportFriendItemViewCache;

public class ImportFriendAdapter extends BaseAdapter {

	private Context mContext;
	private List<SNSItem> mData;
	private LayoutInflater mInflater;

	public ImportFriendAdapter(Context context, List<SNSItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		ImportFriendItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.import_friend_item, null);
			viewCache = new ImportFriendItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ImportFriendItemViewCache) convertView.getTag();
		}
		SNSItem item = mData.get(position);
		viewCache.getmNickNameTV().setText(item.getSnsName());
//		if (position == 1) {
//			viewCache.getmAvatarIV().setImageResource(R.drawable.icon_tencent);
//		}
		if (position == 0) {
			viewCache.getmAvatarIV().setImageResource(R.drawable.icon_sina);
		}
		viewCache.getmTopTV().setText(mContext.getString(R.string.recent_update_date, Util.getDateTime(item.getUpdateTime())));
		return convertView;
	}

}
