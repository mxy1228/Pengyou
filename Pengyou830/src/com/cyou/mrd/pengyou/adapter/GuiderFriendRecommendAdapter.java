package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.entity.RecommendFriendItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.viewcache.RecommendFriendViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GuiderFriendRecommendAdapter extends BaseAdapter implements OnClickListener,AnimationListener{

	private CYLog log = CYLog.getInstance();
	private static final int FOCUS_BTN = 1;
	private static final int ITEM = 2;
	private static final int GV = 3;
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<RecommendFriendItem> mData;
	private DisplayImageOptions mOptions;
	
	public GuiderFriendRecommendAdapter(Context context,List<RecommendFriendItem> data){
		this.mContext = context;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mData = data;
		this.mOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
		.displayer(new RoundedBitmapDisplayer(Config.ROUND))
		.build();
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
		RecommendFriendViewCache viewCache = null;
		if(convertView == null){
			convertView = (LinearLayout)mInflater.inflate(R.layout.my_friend_recommend_item, null);
			viewCache = new RecommendFriendViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (RecommendFriendViewCache)convertView.getTag();
		}
		RecommendFriendItem item = mData.get(position);
		if(item != null){
			CYImageLoader.displayImg(item.getAvatar()
					, viewCache.getmAvatarIV().getImageView()
					, mOptions);
			viewCache.getmDetailTV().setText(item.getRecmndtips());
			viewCache.getmNameTV().setText(item.getNickname());
			viewCache.getmFocusIBtn().setOnClickListener(this);
			viewCache.getmFocusIBtn().setTag(item);
			viewCache.getmFocusIBtn().clearAnimation();
			if(!TextUtils.isEmpty(String.valueOf(item.getGender()))){
				if(item.getGender() == Contants.GENDER_TYPE.BOY){
					viewCache.getmNameTV()
					.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.boy_sign), null, null, null);
				}else{
					viewCache.getmNameTV()
					.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.girl_sign), null, null, null);
				}
			}else{
				viewCache.getmNameTV()
				.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.boy_sign), null, null, null);
			}
			viewCache.getmDetailTV().setText(item.getGame());
		}
		return convertView;
	}

	@Override
	public void onClick(final View v) {
		switch (v.getId()) {
		case R.id.my_friend_recommend_focus_btn:
			if(v.getTag()==null){
				return;
			}
			RecommendFriendItem item = (RecommendFriendItem) v.getTag();
			if(null==item || mData==null || mData.isEmpty()){
				return;
			}
			final  int positon=mData.indexOf(item);
			new RelationUtil(mContext).focus(item.getUid(), Contants.FOCUS.YES,null, new ResultListener() {
				
				@Override
				public void onSuccuss(boolean eachFocused) {
					Toast.makeText(mContext, R.string.focus_success, Toast.LENGTH_SHORT).show();
					try {
					startFocusAnim(v,positon);
					} catch (Exception e) {
						log.e(e);
					}
				}
				
				@Override
				public void onFailed() {
					
				}
			});
			break;

		default:
			break;
		}
	}

	private void startFocusAnim(View v,final int positon){
		Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.focus_btn);
		anim.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation) {
//				mData.remove(positon);
				GuiderFriendRecommendAdapter.this.notifyDataSetChanged();
//				if(mData.isEmpty()){
//					Message msg = mHandler.obtainMessage();
//					msg.what = 1;
//					mHandler.sendMessage(msg);
//				}
			}
		});
		anim.setFillAfter(true);
		v.startAnimation(anim);
		v.setVisibility(View.GONE);
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub
		
	}
}
