package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.WeiboUser;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.viewcache.WeiboFriendViewCache;

public class WeiboFriendAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private List<WeiboUser> mData;
	private LayoutInflater mInflater;
	private Context mContext;

	public WeiboFriendAdapter(List<WeiboUser> data, Context context) {
		this.mData = data;
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public Object getItem(int arg0) {
		return mData.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		try {
			WeiboFriendViewCache viewCache = null;
			if (arg1 == null) {
				arg1 = mInflater.inflate(R.layout.weibo_friend_item, null);
				viewCache = new WeiboFriendViewCache(arg1);
				arg1.setTag(viewCache);
			} else {
				viewCache = (WeiboFriendViewCache) arg1.getTag();
			}
			WeiboUser user = mData.get(arg0);
			if (user != null) {
				ImageView avatar = viewCache.getmAvatarIV();
				avatar.setBackgroundResource(R.drawable.avatar_defaul);
				if (!TextUtils.isEmpty(user.getAvatar())) {
				} else {
					avatar.setImageResource(R.drawable.avatar_defaul);
				}
				viewCache.getmNameTV().setText(user.getName());
				if (user.isState()) {
					viewCache.getmStateTV().setTextColor(
							Color.parseColor("#9fa1a6"));
					viewCache.getmStateTV().setText(R.string.had_invited);
				} else {
					viewCache.getmStateTV().setTextColor(
							Color.parseColor("#336eb2"));
					viewCache.getmStateTV().setText(R.string.invite);
				}
			} else {
				log.e("user is null");
			}
			return arg1;
		} catch (Exception e) {
			log.e(e);
		}
		return null;
	}

}
