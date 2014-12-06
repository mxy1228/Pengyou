package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.ConversationItem;
import com.cyou.mrd.pengyou.entity.UserSimpleInfo;
import com.cyou.mrd.pengyou.entity.base.UserSimpleInfoBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.MyGameActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.PersonalMessageViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class MessageAdapter extends BaseAdapter {

	private CYLog log = CYLog.getInstance();
	
	private Context mContext;
	private List<ConversationItem> mData;
	private LayoutInflater mInflater;
	private DisplayImageOptions mIconOption;
	private DisplayImageOptions mAvatarOption;
	
	public MessageAdapter(Context context,List<ConversationItem> data){
		this.mContext = context;
		this.mData = data;
		this.mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mIconOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.icon_default)
		.showImageOnFail(R.drawable.icon_default)
		.showStubImage(R.drawable.icon_default)
		.build();
		this.mAvatarOption = new DisplayImageOptions.Builder()
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.showImageForEmptyUri(R.drawable.avatar_defaul)
		.showImageOnFail(R.drawable.avatar_defaul)
		.showStubImage(R.drawable.avatar_defaul)
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
		PersonalMessageViewCache viewCache = null;
		if(convertView == null){
			convertView = mInflater.inflate(R.layout.personal_message_item, null);
			viewCache = new PersonalMessageViewCache(convertView);
			convertView.setTag(viewCache);
		}else{
			viewCache = (PersonalMessageViewCache)convertView.getTag();
		}
		ConversationItem item = mData.get(position);
		if(item != null){
			viewCache.getmNameTV().setTag(item.getUid()
					+ Contants.MESSAGE_LISTVIEW_TAG.NICKNAME_TV);
			if(item.getNickname() != null){
				viewCache.getmNameTV().setText(item.getNickname());
			}else{
				viewCache.getmNameTV().setText("");
			}
			viewCache.getmContentTV().setText(item.getLastLetter());
			viewCache.getmDateTV().setText(Util.getDate(item.getTime()/1000));
			viewCache.getmAvatarIV().getImageView().setTag(item.getUid() 
					+ Contants.MESSAGE_LISTVIEW_TAG.AVATAR_IV);
			if(item.getUnreadLetterCount() == 0){
				viewCache.getmUnreadCountTV().setVisibility(View.GONE);
			}else{
				viewCache.getmUnreadCountTV().setVisibility(View.VISIBLE);
				if(item.getUnreadLetterCount() <= 99){
					viewCache.getmUnreadCountTV().setText(String.valueOf(item.getUnreadLetterCount()));
				}else{
					viewCache.getmUnreadCountTV().setText("N");
				}
			}
			CYImageLoader.displayImg(item.getAvatar()
					, viewCache.getmAvatarIV().getImageView()
					, mAvatarOption);
			if(item.getNickname() == null || item.getAvatar() == null){
				getUserInfo(item);
			}
		}
		return convertView;
	}

	/**
	 * 获取用户相关信息
	 */
	private void getUserInfo(final ConversationItem item){
		RequestParams params = new RequestParams();
		String uid = new JSONArray().put(item.getUid()).toString();
		params.put("uidlist", uid);
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_USER_SIMPLE_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onStart() {
				super.onStart();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(mContext);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.i("get simple user info result = "+content);
				try {
					UserSimpleInfoBase base = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<UserSimpleInfoBase>() {
					});
					if(base == null){
						return;
					}
					UserSimpleInfo info = base.getData().get(0);
					if(info.getAvatar() == null){
						item.setAvatar("null");
					}else{
						item.setAvatar(info.getAvatar());
					}
					item.setNickname(info.getNickname());
					MessageAdapter.this.notifyDataSetChanged();
					new LetterDao(mContext).updateRecentUser(info.getUid(), info.getAvatar(), info.getNickname());
				} catch (Exception e) {
					log.e(e);
				}
			}
			
		});
	}
}
