package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import org.taptwo.android.widget.DynamicCommentListView;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.viewcache.DynamicCommentItemViewCache;

public class DynamicCommentAdapter extends BaseAdapter {

	private List<DynamicCommentItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DynamicCommentListView mListView;
	
	public DynamicCommentAdapter(List<DynamicCommentItem> data,Context context,DynamicCommentListView listview){
		this.mData = data;
		this.mContext = context;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mListView = listview;
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
		DynamicCommentItemViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.dynamic_comment_item, null);
			viewCache = new DynamicCommentItemViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (DynamicCommentItemViewCache)convertView.getTag();
		}
		DynamicCommentItem item = mData.get(position);
		if(item != null){
			String html = "<font size='13' color='#7c7c7c'>"+item.getNickname()+"</>"
					+":"
					+"<font size='13' color='#333333'>"+item.getComment()+"</>";
			viewCache.getmContentTV().setText(Html.fromHtml(html));
		}
		return convertView;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		mListView.notifyDataSetChanged();
	}
}
