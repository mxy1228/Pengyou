package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.GuessYourFriendItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.viewcache.YouMayKnownItemViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GuessYourFriendAdapter extends BaseAdapter implements OnClickListener {

	private Context mContext;
	private List<GuessYourFriendItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mOptions;

	public GuessYourFriendAdapter(Context context, List<GuessYourFriendItem> data) {
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mOptions = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul)
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
		YouMayKnownItemViewCache viewCache = null;
		convertView = mInflater.inflate(R.layout.you_may_known_item, null);
		viewCache = new YouMayKnownItemViewCache(convertView);
		GuessYourFriendItem item = mData.get(position);
		if (item != null) {
			CYImageLoader.displayImg(item.getAvatar(), viewCache.getmAvatarIV()
					.getImageView(), mOptions);
			viewCache.getmNickNameTV().setText(item.getNickname());
			viewCache.getmGameCountTV().setText(
					mContext.getString(R.string.play_game_count,
							item.getPlaynum()));
			viewCache.getmGameCountTV().setVisibility(View.GONE);
			Log.d("luochuang11111", item.getPlaynum() + "");
			viewCache.getmFocusIBtn().setOnClickListener(this);
			viewCache.getmFocusIBtn().setTag(R.id.you_may_known_item_focus_btn,
					viewCache.getmFocusIBtn());
			viewCache.getmFocusIBtn().setTag(position);
			//1:通讯录用户   2：微博用户  3：玩同款游戏
			switch(item.getRecommedType()) {
			case 1:
				viewCache.getmContentTV().setText(
						mContext.getResources().getString(
								R.string.guess_from_contacts)
								+ "：" + item.getSmsname());
				break;
			case 2:
				viewCache.getmContentTV().setText(
						mContext.getResources().getString(
								R.string.guess_from_sina)
								+ "：" + item.getSnsname() + " ");
				break;
			case 3:
				viewCache.getmContentTV().setText(
						mContext.getResources().getString(
								R.string.guess_from_same_game_player)
								+ "：" + item.getGame());
				break;
			default:break;
			}
			if (item.getIsfocus() == Contants.FOCUS.YES) {
				viewCache.getmFocusIBtn().setBackgroundResource(0);
				viewCache.getmFocusIBtn().setEnabled(false);
				viewCache.getmFocusIBtn().setText(R.string.had_focus);
			} else {
				viewCache.getmFocusIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				viewCache.getmFocusIBtn().setEnabled(true);
				viewCache.getmFocusIBtn().setText(R.string.focus);
			}
			if (!TextUtils.isEmpty(String.valueOf(item.getGender()))) {
				if (item.getGender() == Contants.GENDER_TYPE.BOY) {
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
		}
		return convertView;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.you_may_known_focus_ibtn:

			// 关注
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_ATTENTION_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_ATTENTION_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			int position = Integer.parseInt(v.getTag().toString());
			final GuessYourFriendItem item = mData.get(position);
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
								GuessYourFriendAdapter.this.notifyDataSetChanged();
							}
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
	public void updateData(List<GuessYourFriendItem> lst) {
		mData = lst;
	}
}