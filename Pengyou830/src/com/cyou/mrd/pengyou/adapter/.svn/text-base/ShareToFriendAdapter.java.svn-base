package com.cyou.mrd.pengyou.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.ui.ShareToFriendActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.ShareToFriendViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class ShareToFriendAdapter extends BaseAdapter implements
		OnCheckedChangeListener {

	private Context mContext;
	private List<FriendItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;

	public ShareToFriendAdapter(Context context, List<FriendItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.build();
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showImageForEmptyUri(R.drawable.avatar_defaul).build();
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
		ShareToFriendViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater
					.inflate(R.layout.share_to_friend_item, null);
			viewCache = new ShareToFriendViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ShareToFriendViewCache) convertView.getTag();
		}
		FriendItem item = mData.get(position);
		if(item != null){
			if (!TextUtils.isEmpty(item.getPicture())) {
				CYImageLoader.displayImg(item.getPicture(), viewCache
						.getmAvatarIV().getImageView(), mOptions);
			}else{
				viewCache.getmAvatarIV().getImageView().setImageResource(R.drawable.avatar_defaul);
			}
			if (!TextUtils.isEmpty(item.getNickname())) {
				viewCache.getmNickNameTV().setText(item.getNickname());
			}else{
				viewCache.getmNickNameTV().setText("");
			}
			viewCache.getmCheckBox().setOnCheckedChangeListener(this);
			viewCache.getmCheckBox().setTag(position);
			if(ShareToFriendActivity.mSelectData.containsKey(item.getUid())){
				viewCache.getmCheckBox().setChecked(true);
			}else{
				viewCache.getmCheckBox().setChecked(false);
			}
		}
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		int postion = Integer.parseInt(buttonView.getTag().toString());
		FriendItem item = mData.get(postion);
		if (isChecked) {
			ShareToFriendActivity.mSelectData.put(item.getUid(), item);
		} else {
			ShareToFriendActivity.mSelectData.remove(item.getUid());
		}
	}

}
