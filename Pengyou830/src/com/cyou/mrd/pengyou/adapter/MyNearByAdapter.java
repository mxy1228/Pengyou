package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.MyNearByItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.MyNearByViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyNearByAdapter extends BaseAdapter implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	private Context mContext;
	private List<MyNearByItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private double myLatitue;
	private double myLongitude;

	public MyNearByAdapter(Context context, List<MyNearByItem> data,
			double MyLatitude, double MyLongitude) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.avatar_defaul)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul).cacheInMemory(true)
				.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading(true).build();
		this.myLatitue = MyLatitude;
		this.myLongitude = MyLongitude;
	}

	public void setLongitude(double longitude) {
		this.myLongitude = longitude;
	}

	public void setLatitue(double latitue) {
		this.myLatitue = latitue;
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
	public void updateData(List<MyNearByItem> lst) {
		mData = lst;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		MyNearByViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_nearby_item, null);
			viewCache = new MyNearByViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (MyNearByViewCache) convertView.getTag();
		}
		MyNearByItem item = mData.get(position);
		if (item != null) {
			if (!TextUtils.isEmpty(item.getNickname())) {
				viewCache.getmNickNameTV().setText(item.getNickname());
			} else {
				viewCache.getmNickNameTV().setText("");
			}
			// viewCache.getmDistanceTV().setText(String.valueOf(Util.getDistance(myLatitue,
			// myLongitude
			// , item.getLatitude(), item.getLongitude())+"米"));
			String distance = Util.getDistance(myLatitue, myLongitude,
					item.getLatitude(), item.getLongitude());
			try {
				double doubledistance = Double.parseDouble(distance);
				if (doubledistance < 10000) {
					doubledistance = (int) (doubledistance / 100) + 1;
					distance = Util.getDouble(doubledistance * 100);
					distance = mContext.getString(R.string.meter, distance);
				} else {
					doubledistance = doubledistance / 1000;
					// distance = Util.getTwoDouble(doubledistance);
					distance = Util.getOneDouble(doubledistance);
					distance = mContext
							.getString(R.string.kilo_meter, distance);
				}
			} catch (Exception e) {
				log.e(e);
			}
			viewCache.getmDistanceTV().setText(distance);
			if (item.getRecentgms() != null && !item.getRecentgms().isEmpty()) {
				viewCache.getmGameTV().setText(
						mContext.getString(R.string.recent_games,
								item.getRecentgms()));
			} else {
				viewCache.getmGameTV().setText(
						mContext.getString(R.string.no_recent_game));
			}
			// if (!TextUtils.isEmpty(item.getRecentgms())) {
			// viewCache.getmGameTV().setText(
			// mContext.getString(R.string.recent_games,
			// item.getRecentgms()));
			// } else {
			// viewCache.getmGameTV().setText(
			// mContext.getString(R.string.recent_games, ""));
			// }
			CYImageLoader.displayImg(item.getPicture(), viewCache
					.getmAvatarIV().getImageView(), mOptions);
			if (item.getGender() != null
					&& !TextUtils.isEmpty(item.getGender().toString())) {
				if (Integer.valueOf(item.getGender()) == Contants.GENDER_TYPE.BOY) {
					viewCache.getmNickNameTV()
							.setCompoundDrawablesWithIntrinsicBounds(
									null,
									null,
									mContext.getResources().getDrawable(
											R.drawable.boy_sign), null);
				} else {
					viewCache.getmNickNameTV()
							.setCompoundDrawablesWithIntrinsicBounds(
									null,
									null,
									mContext.getResources().getDrawable(
											R.drawable.girl_sign), null);
				}
			} else {
				viewCache.getmNickNameTV()
						.setCompoundDrawablesWithIntrinsicBounds(
								null,
								null,
								mContext.getResources().getDrawable(
										R.drawable.boy_sign), null);
			}
			if (item.getIsfocus() == Contants.FOCUS.YES) {
				viewCache.getmAttentionIBtn().setText(R.string.had_focus);
				viewCache.getmAttentionIBtn().setBackgroundResource(
						R.drawable.friend_info_had_focus);
				// viewCache.getmAttentionIBtn().setBackgroundResource(0);
				viewCache.getmAttentionIBtn().setEnabled(false);
				// viewCache.getmAttentionIBtn().setText(R.string.had_focus);
			} else {
				viewCache.getmAttentionIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				viewCache.getmAttentionIBtn().setEnabled(true);
				viewCache.getmAttentionIBtn().setText(R.string.focus);
			}
			viewCache.getmAttentionIBtn().setTag(position);
			viewCache.getmAttentionIBtn().setOnClickListener(this);
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_nearby_item_attention_ibtn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYNEAR_ATT_ID,
					CYSystemLogUtil.ME.BTN_MYNEAR_ATT_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			int position = Integer.valueOf(v.getTag().toString());
			final MyNearByItem item = mData.get(position);
			int action = 0;
			if (item.getIsfocus() == Contants.FOCUS.YES) {
				action = Contants.FOCUS.NO;
			} else {
				action = Contants.FOCUS.YES;
			}
			new RelationUtil(mContext).focus(item.getUid(), action, null,
					new ResultListener() {

						@Override
						public void onSuccuss(boolean eachFocused) {
							if (item.getIsfocus() == Contants.FOCUS.YES) {
								item.setIsfocus(Contants.FOCUS.NO);
							} else {
								item.setIsfocus(Contants.FOCUS.YES);
							}
							MyNearByAdapter.this.notifyDataSetChanged();
						}

						@Override
						public void onFailed() {
							Toast.makeText(mContext, R.string.focus_failed,
									Toast.LENGTH_SHORT).show();
						}

					});
			break;

		default:
			break;
		}
	}

}
