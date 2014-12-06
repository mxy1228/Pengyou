package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.SettingItem;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.viewcache.SettingViewCache;

public class SettingAdapter extends BaseAdapter implements OnCheckedChangeListener{

	private LayoutInflater mInflater;
	private Context mContext;
	private List<SettingItem> mData;
	
	public SettingAdapter(Context context,List<SettingItem> data){
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
		SettingViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.setting_item, null);
			viewCache = new SettingViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (SettingViewCache)convertView.getTag();
		}
		SettingItem item = mData.get(position);
		if(item != null){
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			switch (item.getTag()) {
			case SettingItem.UP:
				params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_margin);
				viewCache.getmContentRL().setLayoutParams(params);
				viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_up_xbg);
				break;
			case SettingItem.MID:
				viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_mid_xbg);
				break;
			case SettingItem.DOWN:
				viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_down_xbg);
				break;
			case SettingItem.WHOLE:
				params.topMargin = mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_margin);
				viewCache.getmContentRL().setLayoutParams(params);
				viewCache.getmContentRL().setBackgroundResource(R.drawable.personal_center_whole_xbg);
				break;
			default:
				break;
			}
			viewCache.getmNameTV().setText(item.getName());
			switch (item.getType()) {
			case SettingItem.ARROW:
				viewCache.getmArrowIV().setVisibility(View.VISIBLE);
				viewCache.getmCheckBox().setVisibility(View.GONE);
				viewCache.getmContentTV().setVisibility(View.GONE);
				break;
			case SettingItem.CHECKBOX:
				viewCache.getmArrowIV().setVisibility(View.GONE);
				viewCache.getmCheckBox().setVisibility(View.VISIBLE);
				viewCache.getmContentTV().setVisibility(View.GONE);
				viewCache.getmCheckBox().setChecked(SharedPreferenceUtil.getNotificationSwitch());
				viewCache.getmCheckBox().setOnCheckedChangeListener(this);
				break;
			case SettingItem.TEXTVIEW:
				viewCache.getmArrowIV().setVisibility(View.GONE);
				viewCache.getmCheckBox().setVisibility(View.GONE);
				viewCache.getmContentTV().setVisibility(View.VISIBLE);
				viewCache.getmContentTV().setText(item.getText());
				break;
			default:
				break;
			}
			viewCache.getmContentRL().setPadding(mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
					, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
					, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding)
					, mContext.getResources().getDimensionPixelSize(R.dimen.personal_center_item_padding));
		}
		return convertView;
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked) {
			SharedPreferenceUtil
					.saveNotificationSwitch(true);
		} else {
			SharedPreferenceUtil
					.saveNotificationSwitch(false);
		}
		((CheckBox)buttonView).setChecked(SharedPreferenceUtil.getNotificationSwitch());
	}

}
