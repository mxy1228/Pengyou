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
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.ChatActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.viewcache.MyFriendItemViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyFriendAdapter extends BaseAdapter implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	private List<FriendItem> mData;
	private Context mContext;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;
	private FriendDao mDao;

	public MyFriendAdapter(Context context, List<FriendItem> data) {
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
		this.mDao = new FriendDao(mContext);
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
		viewCache.getmLetterIBtn().setOnClickListener(this);
		FriendItem item = mData.get(position);
		if (item != null) {
			viewCache.getmEachFocusIV().setVisibility(View.GONE);
			viewCache.getmLetterIBtn().setVisibility(View.VISIBLE);
			viewCache.getmFocusIBtn().setVisibility(View.GONE);
			viewCache.getmNickNameTV().setText(item.getNickname());
			viewCache.getmGameCountTV().setText(
					mContext.getString(R.string.play_game_count,
							item.getPlaynum()));
			CYImageLoader.displayImg(item.getPicture(), viewCache
					.getmAvatarIV().getImageView(), mOptions);
			viewCache.getmLetterIBtn().setTag(item);
			if(item.getRecentgmsStr() != null && !TextUtils.isEmpty(item.getRecentgmsStr()) && !item.getRecentgmsStr().equals("[]")){
				viewCache.getmGameDetailTV().setText(mContext.getString(R.string.recent_games, item.getRecentgmsStr()));
			}else{
				viewCache.getmGameDetailTV().setText(mContext.getString(R.string.no_recent_game));
			}
			if(!TextUtils.isEmpty(String.valueOf(item.getGender()))){
				if(item.getGender() == Contants.GENDER_TYPE.BOY){
					viewCache.getmNickNameTV()
					.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.boy_sign), null);
				}else{
					viewCache.getmNickNameTV()
					.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.girl_sign), null);
				}
			}else{
				viewCache.getmNickNameTV()
				.setCompoundDrawablesWithIntrinsicBounds(null, null, mContext.getResources().getDrawable(R.drawable.boy_sign), null);
			}
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		FriendItem item = (FriendItem) v.getTag();
		if (item == null) {
			return;
		}
		switch (v.getId()) {
		case R.id.my_friend_item_letter_ibtn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_PRI_MSG_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_PRI_MSG_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent intent = new Intent(mContext, ChatActivity.class);
			intent.putExtra(Params.CHAT.FROM, item.getUid());
			intent.putExtra(Params.CHAT.NICK_NAME, item.getNickname());
			mContext.startActivity(intent);
			mDao.updateTime(item.getUid(), System.currentTimeMillis());
			break;

		default:
			break;
		}
	}

	public void changeData(List<FriendItem> data){
		this.mData = data;
		this.notifyDataSetChanged();
	}
}
