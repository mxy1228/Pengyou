package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.viewcache.SearchUserItemViewCache;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class SearchUserAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	private static final int PAY_ATTENTION = 1;
	private List<FriendItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private MyHttpConnect myHttpConnect;
	public static final int SHOW_NUM = 2;
	private Drawable boyDrawable;
	private Drawable girlDrawable;

	public SearchUserAdapter(Context context, List<FriendItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.avatar_defaul)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new RoundedBitmapDisplayer(15))
				.resetViewBeforeLoading().build();
		myHttpConnect = MyHttpConnect.getInstance();
		boyDrawable = mContext.getResources().getDrawable(R.drawable.boy_sign);
		girlDrawable = mContext.getResources()
				.getDrawable(R.drawable.girl_sign);
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

	public void updateData(List<FriendItem> lst) {
		mData = lst;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		SearchUserItemViewCache viewCache = null;
		UserInfo infos = UserInfoUtil.getCurrentUserInfo();
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.search_user_list_item,
					null);
			viewCache = new SearchUserItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (SearchUserItemViewCache) convertView.getTag();
		}
		final FriendItem item = mData.get(position);
		if (null != item) {
			CYImageLoader.displayImg(item.getPicture(), viewCache
					.getmAvatarIV().getImageView(), mOptions);
			viewCache.getmNickNameTV().setText(item.getNickname());
			if (!TextUtils.isEmpty(String.valueOf(item.getGender()))) {
				int gender = item.getGender();
				if (Contants.GENDER_TYPE.GIRL == gender) {
					viewCache.getmNickNameTV().setCompoundDrawables(null, null,
							girlDrawable, null);
				} else {
					viewCache.getmNickNameTV().setCompoundDrawables(null, null,
							boyDrawable, null);
				}
			}
			if (Contants.FOCUS.YES == item.getIsfocus()) {// 若已经关注
				/*
				 * viewCache.getmFocusIBtn().setBackgroundResource(
				 * R.drawable.had_attention_btn_normal);
				 * viewCache.getmFocusIBtn().setOnClickListener(null);
				 * viewCache.getmFocusIBtn().setText("");
				 */
				viewCache.getmFocusIBtn().setBackgroundResource(0);
				// viewCache.getmFocusIBtn().setEnabled(false);
				viewCache.getmFocusIBtn().setText(R.string.had_focus);
			} else {
				// 不能自己关注自己
				if (item.getUid() == infos.getUid()) {
					viewCache.getmFocusIBtn().setVisibility(View.INVISIBLE);
				}
				viewCache.getmFocusIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				viewCache.getmFocusIBtn().setText(R.string.focus);
				final Button isFoucsBtn = viewCache.getmFocusIBtn();
				viewCache.getmFocusIBtn().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_SFRD_ATT_ID,
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_SFRD_ATT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
								int postion = mData.indexOf(item);
								focus(PAY_ATTENTION, item.getUid(), item,
										postion, isFoucsBtn);
							}
						});
			}
		}
		return convertView;
	}

	private void focus(int isfocus, int mUID, final FriendItem item,
			final int postion, final Button imgButton) {
		if (0 == mUID) {
			log.e("mUID is null");
			return;
		}
		new RelationUtil(mContext).focus(mUID, isfocus, null,
				new ResultListener() {

					@Override
					public void onSuccuss(boolean eachFocused) {
						Toast.makeText(mContext, R.string.focus_success,
								Toast.LENGTH_SHORT).show();
						/*
						 * imgButton .setBackgroundResource(R.drawable.
						 * had_attention_btn_normal);
						 */
						imgButton.setBackgroundResource(0);
						// imgButton.setEnabled(false);
						imgButton.setText(R.string.had_focus);
						item.setIsfocus(Contants.FOCUS.YES);
						mData.set(postion, item);
						notifyDataSetChanged();
					}

					@Override
					public void onFailed() {
						Toast.makeText(mContext, R.string.focus_failed,
								Toast.LENGTH_SHORT).show();
					}

				});

	}

}
