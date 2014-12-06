package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.MyFocusItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.ChatActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.viewcache.MyFriendItemViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyFocusAdapter extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private List<MyFocusItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;

	public MyFocusAdapter(Context context, List<MyFocusItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul).cacheInMemory(true)
				.cacheOnDisc(true)
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
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
		MyFriendItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.my_friend_item, null);
			viewCache = new MyFriendItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (MyFriendItemViewCache) convertView.getTag();
		}
		MyFocusItem item = mData.get(position);
		if (item != null) {
			CYImageLoader.displayImg(item.getPicture(), viewCache
					.getmAvatarIV().getImageView(), mOptions);
			viewCache.getmNickNameTV().setText(item.getNickname());
//			if (item.getBilateral() == Contants.FOCUS.YES) {
//				viewCache.getmEachFocusIV().setVisibility(View.VISIBLE);
//			} else {
//				viewCache.getmEachFocusIV().setVisibility(View.GONE);
//			}
			if (item.getGender() != null && !TextUtils.isEmpty(item.getGender().toString())) {
				if (item.getGender().equals(
						String.valueOf(Contants.GENDER_TYPE.BOY))) {
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
			viewCache.getmGameCountTV().setText(
					mContext.getString(R.string.play_game_count,
							item.getPlaynum()));
			if (item.getRecentgms() != null && !item.getRecentgms().isEmpty()) {
				viewCache.getmGameDetailTV().setText(
						mContext.getString(R.string.recent_games,
								item.getRecentgms().toString()));
			} else {
				viewCache.getmGameDetailTV().setText(mContext.getString(R.string.no_recent_game));
			}
			if(item.getRelation() == Contants.RELATION.NO_RELATION){
				viewCache.getmFocusIBtn().setVisibility(View.VISIBLE);
				viewCache.getmEachFocusIV().setVisibility(View.GONE);
				viewCache.getmLetterIBtn().setVisibility(View.GONE);
			}else{
				viewCache.getmFocusIBtn().setVisibility(View.GONE);
				if(item.getRelation() == Contants.RELATION.EACH_FOCUS){
					viewCache.getmEachFocusIV().setVisibility(View.VISIBLE);
				}else{
					viewCache.getmEachFocusIV().setVisibility(View.GONE);
				}
				viewCache.getmLetterIBtn().setVisibility(View.VISIBLE);
			}
			if(item.getUid() == UserInfoUtil.getCurrentUserId()){
				viewCache.getmFocusIBtn().setVisibility(View.GONE);
				viewCache.getmEachFocusIV().setVisibility(View.GONE);
				viewCache.getmLetterIBtn().setVisibility(View.GONE);
			}
			viewCache.getmFocusIBtn().setOnClickListener(this);
			viewCache.getmFocusIBtn().setTag(item);
			viewCache.getmLetterIBtn().setOnClickListener(this);
			viewCache.getmLetterIBtn().setTag(item);
//			viewCache.getmLetterIBtn().setVisibility(View.VISIBLE);
//			viewCache.getmLetterIBtn().setOnClickListener(this);
//			viewCache.getmLetterIBtn().setTag(position);
//			viewCache.getmFocusIBtn().setVisibility(View.GONE);
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		final MyFocusItem item = (MyFocusItem)v.getTag();
		switch (v.getId()) {
		case R.id.my_friend_item_letter_ibtn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYATTENTION_PRIMSG_ID,
					CYSystemLogUtil.ME.BTN_MYATTENTION_PRIMSG_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent intent = new Intent(mContext, ChatActivity.class);
			intent.putExtra(Params.CHAT.FROM, item.getUid());
			intent.putExtra(Params.CHAT.NICK_NAME, item
					.getNickname());
			mContext.startActivity(intent);
			break;
		case R.id.my_friend_item_focus_ibtn:
			new RelationUtil(mContext).focus(item.getUid(), Contants.FOCUS.YES, null, new ResultListener(){

				@Override
				public void onSuccuss(boolean eachFocused) {
					if(eachFocused){
						item.setRelation(Contants.RELATION.EACH_FOCUS);
					}else{
						item.setRelation(Contants.RELATION.HAD_FOCUS);
					}
					MyFocusAdapter.this.notifyDataSetChanged();
				}

				@Override
				public void onFailed() {
					// TODO Auto-generated method stub
					
				}
				
			});
			break;
		default:
			break;
		}
	}

}
