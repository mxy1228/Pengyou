package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.ClassifyItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.ClassifyItemViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GameClassifyAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private List<ClassifyItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private int mScreenWidth;
	private static final int mDivider=45;
	private static final int mPicHeight=60;
	private static final int mPicWidth=214;
	private static final int mClassifyRound=8;

	public GameClassifyAdapter(Context context, List<ClassifyItem> data,
			int screenWidth) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.game_classify_icon_default)
				.showImageOnFail(R.drawable.game_classify_icon_default)
				.showStubImage(R.drawable.game_classify_icon_default).cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(mClassifyRound)).build();//Config.ROUND)).build();
		this.mScreenWidth = screenWidth;
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
		try {
			ClassifyItem item = mData.get(position);
			ClassifyItemViewCache viewCache = null;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.gamestore_classify_item, null);
				/*LayoutParams params = new LayoutParams(mScreenWidth / 2,
					LayoutParams.WRAP_CONTENT);
				convertView.setLayoutParams(params);*/
				viewCache = new ClassifyItemViewCache(convertView);
			convertView.setTag(viewCache);
			} else {
			 viewCache = (ClassifyItemViewCache) convertView.getTag();
			}
			if (item == null) {
				return convertView;
			}
			int height = (mScreenWidth-mDivider)*mPicHeight/mPicWidth;
		        RelativeLayout layout = (RelativeLayout)convertView.findViewById(R.id.game_classify_layout); 
		        layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, height));
			String tid = item.getTid();
			if (!TextUtils.isEmpty(item.getName())) {
				TextPaint tp = viewCache.getmTV().getPaint(); 
				tp.setFakeBoldText(true); 
				viewCache.getmTV().setText(item.getName());
				String imageUrl = item.getTypeicon();
				CYImageLoader.displayGameImg(imageUrl,//displayIconImage(imageUrl,
						viewCache.getmIV(), mOptions);
			}
			/*
			if ("-1".equals(tid) || "-2".equals(tid) || "-3".equals(tid)
					|| "-4".equals(tid)) {
				convertView = mInflater.inflate(R.layout.classify_item_title,
						null);
				TextView txtTitle = (TextView) convertView
						.findViewById(R.id.classify_title);  
				if ("-1".equals(tid)) {
					txtTitle.setText(R.string.game_classify);
				} else if ("-3".equals(tid)) {
					txtTitle.setText(R.string.private_classify);

				} else {
					txtTitle.setText("");
				}
				viewCache.getmTV().setVisibility(View.GONE);
				viewCache.getmIV().setVisibility(View.GONE);
			} else if ("-5".equals(tid)) {
				viewCache.getmTV().setVisibility(View.INVISIBLE);
				viewCache.getmIV().setVisibility(View.INVISIBLE);
			} else {
				if (!TextUtils.isEmpty(item.getName())) {
					TextPaint tp = viewCache.getmTV().getPaint(); 
					tp.setFakeBoldText(true); 
					viewCache.getmTV().setText(item.getName());
					String imageUrl = item.getTypeicon();
					CYImageLoader.displayIconImage(imageUrl,
							viewCache.getmIV(), mOptions);
				}
			}*/
		} catch (Exception e) {
			log.e(e);
		}
		return convertView;
	}
}
