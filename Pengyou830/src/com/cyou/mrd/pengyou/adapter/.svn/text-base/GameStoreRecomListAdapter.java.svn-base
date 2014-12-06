package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.GameCircleDetailActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.RecommendGameViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 猜你喜欢的游戏
 * 
 * @author wangkang
 * 
 */
public class GameStoreRecomListAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	private LayoutInflater mInflater;
	private List<GameItem> mData;
	private DisplayImageOptions mOptions;
	int height = 0;
	private LayoutParams imgLayoutParams;

	public GameStoreRecomListAdapter(Context context, List<GameItem> data,
			GridView gd) {
		this.mContext = context;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
		this.mOptions = new DisplayImageOptions.Builder()
		// .showImageForEmptyUri(R.drawable.gamestore_recommend_defaul_bg)
		// .showImageOnFail(R.drawable.gamestore_recommend_defaul_bg)
		// .showStubImage(R.drawable.gamestore_recommend_defaul_bg)
				.cacheInMemory(true).cacheOnDisc(true).build();
		height = (int) (Util.getScreenWidth((Activity) mContext) / 480.0f * 82);
		imgLayoutParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, height);
	}

	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		}
		return 0;
	}

	public void updateData(List<GameItem> mLst) {
		mData = mLst;
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
		RecommendGameViewCache viewCache = null;
		if (convertView == null) {
			convertView = (RelativeLayout) mInflater.inflate(
					R.layout.gamestore_recommend_list_item, null);
			viewCache = new RecommendGameViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (RecommendGameViewCache) convertView.getTag();
		}
		try {
			final GameItem item = mData.get(position);
			if (item == null || TextUtils.isEmpty(item.getGamecode())) {
				return convertView;
			} else {
				if (!TextUtils.isEmpty(item.getName())) {
					viewCache.getmNameTV().setText(item.getName().trim());
				}
				if (!TextUtils.isEmpty(item.getRecdesc())) {
					viewCache.getmTxtGameInfo().setText(item.getRecdesc());
				}
				viewCache.getRatingBarFirst().setRating(item.getStar());
				CYImageLoader.displayGameImg(item.getRecpic(),
						viewCache.getmAvatarIV(), mOptions);
				viewCache.getmAvatarIV().setLayoutParams(imgLayoutParams);
				long time = 0;
				try {
					if (!TextUtils.isEmpty(item.getRecdate())) {
						time = Long.parseLong(item.getRecdate());
					}
				} catch (Exception e) {
					log.e(e);
				}
				log.d("精品推荐时间戳:" + item.getRecdate());
				boolean isToday = Util.isToday(time);
				log.d("是否为今天:" + isToday);
				if (isToday) {
					viewCache.getImgToday().setVisibility(View.VISIBLE);
				} else {
					viewCache.getImgToday().setVisibility(View.GONE);
				}
				viewCache.getmAvatarIV().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent mIntent = new Intent();
								mIntent.putExtra(Params.INTRO.GAME_CODE,
										item.getGamecode());
								mIntent.setClass(mContext,
										GameCircleDetailActivity.class);
								mIntent.putExtra(Params.INTRO.GAME_NAME,
										item.getName());
								mContext.startActivity(mIntent);
							}
						});
				viewCache.getLinearLayout().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View v) {
								Intent mIntent = new Intent();
								mIntent.putExtra(Params.INTRO.GAME_CODE,
										item.getGamecode());
								mIntent.setClass(mContext,
										GameCircleDetailActivity.class);
								mIntent.putExtra(Params.INTRO.GAME_NAME,
										item.getName());
								mContext.startActivity(mIntent);
							}
						});
				viewCache.getmAvatarIV().setOnTouchListener(
						Util.onTouchCaptureListener);
			}
		} catch (Exception e) {
			log.e(e);
		}
		return convertView;
	}

}
