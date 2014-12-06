package com.cyou.mrd.pengyou.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.DownloadActivity;
import com.cyou.mrd.pengyou.ui.SearchActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameItemViewCache;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SearchHistoryAdapter extends BaseAdapter {

	private List<Map<String,Object>> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private boolean mShowIcon=false;
	public boolean mShowHistory=true;
	private DisplayImageOptions mOptions;

	public final class ListItemView{
		public ImageView image;
		public TextView title;
		public ImageButton delete;
		public RelativeLayout imageLayout;
		public RelativeLayout historyLayout;
		public View dividerView;
	}

	public SearchHistoryAdapter(Context context, List<Map<String,Object>> listItems,boolean show) {
		super();     
		this.mContext = context;
		this.mData=listItems;
		this.mShowIcon=show;
		this.mOptions = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.icon_default)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default).cacheInMemory(true)
		.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new RoundedBitmapDisplayer(15))
		.resetViewBeforeLoading().build();
	}

	@Override
	public int getCount() {
		return mData.size();
	}
	
	public void updateData(List<Map<String,Object>> lst,boolean iconShow,boolean history) {
		mData = lst;
		mShowIcon=iconShow;
		mShowHistory=history;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private Map<Integer, View> viewCaches = new HashMap<Integer, View>();

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ListItemView viewCache = null;
		convertView = viewCaches.get(position);
		if (convertView == null) {
			mInflater = LayoutInflater.from(mContext);
			convertView = mInflater.inflate(R.layout.search_history_item, null);
			viewCache = new ListItemView();
			viewCache.title=(TextView) convertView.findViewById(R.id.history_gamename);
			viewCache.image=(ImageView) convertView.findViewById(R.id.img_gameicon);
			viewCache.delete=(ImageButton) convertView.findViewById(R.id.delete_gamename);
			viewCache.imageLayout=(RelativeLayout) convertView.findViewById(R.id.layout_icon);
			viewCache.historyLayout=(RelativeLayout) convertView.findViewById(R.id.layout_history);
			viewCache.dividerView =(View)convertView.findViewById(R.id.divider);
			convertView.setTag(viewCache);
		} else {
			viewCache = (ListItemView) convertView.getTag();
		}
		if(position==getCount()-1){
			viewCache.dividerView.setVisibility(View.VISIBLE);
		}

		viewCache.title.setText((String)mData.get(position).get("title"));
		viewCache.imageLayout.setVisibility(mShowIcon?View.VISIBLE:View.GONE);
		viewCache.historyLayout.setVisibility((mShowHistory||!mShowIcon)?View.VISIBLE:View.GONE);
		CYImageLoader.displayIconImage((String)mData.get(position).get("icon"),
				viewCache.image, mOptions);
		viewCache.image.setVisibility(mShowIcon?View.VISIBLE:View.GONE);
		viewCache.delete.setVisibility(mShowHistory?View.VISIBLE:View.GONE);
		viewCache.delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteSerchHistory(position);
				mData.remove(position);
				notifyDataSetChanged();
			}
		});
		return convertView;
	}
	
	//删除搜索历史记录
	public  void deleteSerchHistory(int postion){
		Log.d("SearchActivity","delete---"+postion);
		SharedPreferences  mHistoryPreference = mContext.getSharedPreferences("game_search_history", 0);
		int count = mHistoryPreference.getInt("count",0);			
		SharedPreferences.Editor editor = mHistoryPreference.edit();
		editor.putInt("count", count-1);
		if(postion==0) {
			Log.d("SearchActivity","remove---"+postion+"--count="+count);
			editor.remove("history_"+count);
			editor.commit();
			return;
		}
		for(int i=count-postion;i<count;i++){
			Log.d("SearchActivity","move"+mHistoryPreference.getString("history_"+(i+1),"")+"--to--history_"+i);
			editor.putString("history_"+i,mHistoryPreference.getString("history_"+(i+1),""));		
		}
		editor.commit();
	}
}
