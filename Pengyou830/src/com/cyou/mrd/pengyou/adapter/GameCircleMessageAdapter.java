package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.db.RelationCircleMsgDao;
import com.cyou.mrd.pengyou.entity.RelationCircleMessageItem;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RelationCircleMessageViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class GameCircleMessageAdapter extends BaseAdapter{

	private Context mContext;
	private List<RelationCircleMessageItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mAvatarOption;
	private DisplayImageOptions mIconOption;
	
	private boolean mShowMoreBtn = true;
	
	public GameCircleMessageAdapter(Context context,List<RelationCircleMessageItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mAvatarOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
		.build();
		this.mIconOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.build();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		RelationCircleMessageViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.relation_circle_message_item, null);
			viewCache = new RelationCircleMessageViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (RelationCircleMessageViewCache)convertView.getTag();
		}
		RelationCircleMessageItem item = mData.get(position);
		if(item != null){
			if(TextUtils.isEmpty(item.getActpic())){
				viewCache.getmRightIV().setVisibility(View.GONE);
				viewCache.getmRightTV().setVisibility(View.VISIBLE);
				CYImageLoader.displayImg(item.getUsrpicture()
						, viewCache.getmAvatarIV().getImageView()
						, mAvatarOption);
				if(!TextUtils.isEmpty(item.getActtext())){
					viewCache.getmRightTV().setText(item.getActtext());
				}
			}else{
				viewCache.getmRightIV().setVisibility(View.VISIBLE);
				viewCache.getmRightTV().setVisibility(View.GONE);
				CYImageLoader.displayIconImage(item.getActpic()
						, viewCache.getmRightIV()
						, mIconOption);
			}
			CYImageLoader.displayImg(item.getUsrpicture(), viewCache.getmAvatarIV().getImageView(), mAvatarOption);
			viewCache.getmNameTV().setText(item.getNickname());
			viewCache.getmTypeTV().setText(item.getMsgmemo());
			viewCache.getmContentTV().setText(item.getMsg());
			viewCache.getmTimeTV().setText(Util.getDate(item.getTimestamp()));
		}
		return convertView;
	}

//	@Override
//	public void onClick(View v) {
//		switch (v.getId()) {
//		case R.id.relation_circle_message_item_load_more_btn:
//			v.setVisibility(View.GONE);
//			List<RelationCircleMessageItem> list = new RelationCircleMsgDao(mContext).selectMsgs();
//			if(list != null && !list.isEmpty()){
//				mData.clear();
//				mData.addAll(list);
//				RelationCircleMessageAdapter.this.notifyDataSetChanged();
//				mShowMoreBtn = false;
//			}
//			break;
//
//		default:
//			break;
//		}
//	}

}
