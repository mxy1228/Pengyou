package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.DynamicDetailCommentViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class DynamicDetailCommentAdapter extends BaseAdapter implements OnClickListener{

	private Context mContext;
	private List<DynamicCommentItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	
	public DynamicDetailCommentAdapter(Context context,List<DynamicCommentItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
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
		DynamicDetailCommentViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dynamic_detail_comment_item, null);
			viewCache = new DynamicDetailCommentViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (DynamicDetailCommentViewCache)convertView.getTag();
		}
		DynamicCommentItem item = mData.get(position);
		if(item != null){
			CYImageLoader.displayImg(item.getAvatar()
					, viewCache.getmAvatarIV().getImageView()
					, mOptions);
			viewCache.getmAvatarIV().setTag(position);
			viewCache.getmAvatarIV().setOnClickListener(this);
			if(!TextUtils.isEmpty(item.getNickname())){
				viewCache.getmNickNameTV().setText(item.getNickname());
			}
			if(!TextUtils.isEmpty(item.getComment())){
				StringBuilder sb = new StringBuilder();
				if(item.getReplyto() != null){
					sb.append(mContext.getString(R.string.reply)).append(item.getReplyto().getNickname()).append(":");
				}
				sb.append(item.getComment());
				SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());
				if(item.getReplyto() != null){
					ForegroundColorSpan span = new ForegroundColorSpan(Color.parseColor("#265FA5"));
					ssb.setSpan(span, 2, item.getReplyto().getNickname().length()+2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					viewCache.getmContentTV().setText(ssb);
				}else{
					viewCache.getmContentTV().setText(sb.toString());
				}
			}
			viewCache.getmDateTV().setText(Util.getFormatDate(item.getTimestamp()));
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dynamic_detail_comment_item_avatar_iv:
			int position = Integer.valueOf(v.getTag().toString());
			DynamicCommentItem item = mData.get(position);
			Intent intent = new Intent(mContext,FriendInfoActivity.class);
			intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
			intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
			mContext.startActivity(intent);
			break;

		default:
			break;
		}
	}

}
