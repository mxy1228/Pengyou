package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.PersonalCenterItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.viewcache.PersonalCenterViewCache;

public class PersonalCenterAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	
	private static final int[] PICS = {
		R.drawable.personal_center_phone_pic
		,R.drawable.personal_center_score_pic
		,R.drawable.personal_center_message_pic
		,R.drawable.personal_center_friend_pic
		,R.drawable.personal_center_nearby_pic
		,R.drawable.personal_center_collection_pic
		,R.drawable.personal_center_share_pic
		,R.drawable.personal_center_feedback_pic
		,R.drawable.personal_center_setting_pic};
	
	private Context mContext;
	private List<PersonalCenterItem> mData;
	private LayoutInflater mInflater;
	
	public PersonalCenterAdapter(Context context,List<PersonalCenterItem> data){
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
		PersonalCenterViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.personal_center_item, null);
			viewCache = new PersonalCenterViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (PersonalCenterViewCache)convertView.getTag();
		}
		PersonalCenterItem item = mData.get(position);
		if(item == null){
			log.e("item is null");
		}
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		switch (item.getTag()) {
		case PersonalCenterItem.UP:
			params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_margin);
			viewCache.getmContentRL().setLayoutParams(params);
			viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_up_xbg);
			break;
		case PersonalCenterItem.MID:
			viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_mid_xbg);
			break;
		case PersonalCenterItem.DOWN:
			viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_down_xbg);
			break;
		case PersonalCenterItem.WHOLE:
			params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_margin);
			viewCache.getmContentRL().setLayoutParams(params);
			viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_whole_xbg);
			break;
		default:
			break;
		}
		viewCache.getmContentRL().setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
				, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
				, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
				, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding));
		viewCache.getmContentTV().setText(item.getName());
		viewCache.getmDotBtn().setVisibility(View.GONE);
		viewCache.getmPicIV().setImageResource(PICS[position]);
		if(item.isShowHint()){
			viewCache.getmHintTV().setVisibility(View.VISIBLE);
			viewCache.getmHintTV().setText(item.getHintContent());
		}else{
			viewCache.getmHintTV().setVisibility(View.GONE);
			viewCache.getmHintTV().setText("");
		}
		if(position == 2){
			//消息
			if(item.getMsgCount() == 0){
				viewCache.getmDotBtn().setVisibility(View.GONE);
			}else {
				viewCache.getmDotBtn().setVisibility(View.VISIBLE);
				if(item.getMsgCount() <= 99){
					viewCache.getmDotBtn().setText(String.valueOf(item.getMsgCount()));
				}else{
					viewCache.getmDotBtn().setText("99+");
				}
			}
		}else if(position == 3){
			//好友推荐
			if(item.getRecommendCount() == 0){
				viewCache.getmDotBtn().setVisibility(View.GONE);
			}else{
				viewCache.getmDotBtn().setVisibility(View.VISIBLE);
				if(item.getRecommendCount() <= 99){
					viewCache.getmDotBtn().setText(String.valueOf(item.getRecommendCount()));
				}else{
					viewCache.getmDotBtn().setText("99+");
				}
			}
		}
		return convertView;
	}

}
