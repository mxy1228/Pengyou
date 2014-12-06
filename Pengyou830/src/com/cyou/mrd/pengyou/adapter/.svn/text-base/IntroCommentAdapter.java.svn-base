package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.IntroCommentItem;
import com.cyou.mrd.pengyou.entity.IntroSubComment;
import com.cyou.mrd.pengyou.entity.base.IntroSubCommentBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.MyGameActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.IntroCommentItemViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class IntroCommentAdapter extends BaseAdapter implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	private Context mContext;
	private List<IntroCommentItem> mData;
	private DisplayImageOptions mOption;
	private Handler mHandler;
	private LayoutInflater mInflater;

	public IntroCommentAdapter(Context context, List<IntroCommentItem> data,
			Handler handler) {
		this.mContext = context;
		this.mData = data;
		this.mOption = new DisplayImageOptions.Builder().cacheInMemory(true)
				.cacheOnDisc(true).showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul).build();
		this.mHandler = handler;
		this.mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
		IntroCommentItemViewCache viewCache = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.intro_comment_item, null);
			viewCache = new IntroCommentItemViewCache(convertView);
			convertView.setTag(viewCache);
		} else {
			viewCache = (IntroCommentItemViewCache) convertView.getTag();
		}
		IntroCommentItem item = mData.get(position);
		if (item != null) {
			if (!TextUtils.isEmpty(item.getNickname())) {
				viewCache.getmNickNameTV().setText(item.getNickname());
			}
			CYImageLoader.displayImg(item.getAvatar(), viewCache
					.getmAvatarIV().getImageView(), mOption);
			viewCache.getmAvatarIV().setOnClickListener(this);
			viewCache.getmAvatarIV().setTag(position);
			viewCache.getmRB().setRating(item.getStar());
			if (!TextUtils.isEmpty(item.getText())) {
				viewCache.getmContentTV().setText(item.getText());
			}
			viewCache.getmDateTV().setText(Util.getDate(item.getTimestamp()));
			viewCache.getmCommentCountTV().setText(
					mContext.getString(R.string.comment_count,
							item.getReplynum()));
			viewCache.getmCommentCountTV().setOnClickListener(this);
			viewCache.getmCommentCountTV().setTag(position);
			viewCache.getmCommentCountTV().setTag(R.id.intro_comment_ll,
					viewCache.getmCommentLL());
			viewCache.getmCommentCountTV().setTag(R.id.intro_comment_item_pb,viewCache.getmPB());
			viewCache.getmCommentCountTV().setTag(R.id.intro_comment_item_empty_tv,viewCache.getmEmptyTV());
			initSubComment(item, viewCache.getmSubCommentTV(),viewCache.getmPB(),viewCache.getmEmptyTV());
			viewCache.getmPackUpIBtn().setTag(position);
			viewCache.getmPackUpIBtn().setOnClickListener(this);
			viewCache.getmPackUpIBtn().setTag(R.id.intro_comment_ll,
					viewCache.getmCommentLL());
			viewCache.getmSubCommentLL().setOnClickListener(this);
			viewCache.getmSubCommentLL().setTag(position);
			if (item.isOpen()) {
				viewCache.getmCommentLL().setVisibility(View.VISIBLE);
			} else {
				viewCache.getmCommentLL().setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	private void initSubComment(IntroCommentItem item, TextView tv,ProgressBar pb,TextView emptyTV) {
		if (item.getSubcomments() != null) {
			tv.setText(Html.fromHtml(item.getSubcomments().toString()));
			tv.setMovementMethod(LinkMovementMethod.getInstance());
			Util.initComment(tv);
			pb.setVisibility(View.GONE);
			if(TextUtils.isEmpty(item.getSubcomments().toString())){
				emptyTV.setVisibility(View.VISIBLE);
			}else{
				emptyTV.setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		int position = Integer.parseInt(v.getTag().toString());
		IntroCommentItem item = mData.get(position);
		switch (v.getId()) {
		case R.id.intro_comment_item_comment_count_tv:
			LinearLayout ll = (LinearLayout) v.getTag(R.id.intro_comment_ll);
			ProgressBar pb = (ProgressBar)v.getTag(R.id.intro_comment_item_pb);
			TextView empty = (TextView)v.getTag(R.id.intro_comment_item_empty_tv);
			if (!ll.isShown()) {
				ll.setVisibility(View.VISIBLE);
				if(TextUtils.isEmpty(item.getSubcomments().toString())){
					getComment(mData.get(position));
					pb.setVisibility(View.VISIBLE);
					empty.setVisibility(View.GONE);
				}
				item.setOpen(true);
			}else{
				ll.setVisibility(View.GONE);
				item.setOpen(false);
			}
			break;
		case R.id.intro_comment_item_pack_up_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_RE_MORE_GONE_ID,
					CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_RE_MORE_GONE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			LinearLayout rll = (LinearLayout) v.getTag(R.id.intro_comment_ll);
			if (rll.isShown()) {
				rll.setVisibility(View.GONE);
			}
			mData.get(position).setOpen(false);
			break;
		case R.id.intro_comment_item_ll:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_RE_ID,
					CYSystemLogUtil.GAMEDETAIL.BTN_COMMENT_RE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Message msg = mHandler.obtainMessage();
			msg.what = 0;
			msg.arg1 = position;
			mHandler.sendMessage(msg);
			break;
		case R.id.intro_comment_item_avatar_iv:
			//TODO
			Intent intent = new Intent(mContext,FriendInfoActivity.class);
			intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
			intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
			mContext.startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void getComment(final IntroCommentItem item) {
		RequestParams params = new RequestParams();
		params.put("cid", String.valueOf(item.getCid()));
		params.put("cursor", String.valueOf(item.getCursor()));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		MyHttpConnect.getInstance().post(
				HttpContants.NET.GET_INTRO_SUB_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						
					}
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mContext);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("get sub comment result = " + content);
						try {
							IntroSubCommentBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<IntroSubCommentBase>() {
											});
							List<IntroSubComment> data = base.getData();
							if (data != null) {
								item.setSubCommentData(data);
								item.setSubcomments(Util.comment2Html(data));
								//TODO
//								StringBuilder sb = new StringBuilder();
//								for (IntroSubComment comment : data) {
//									sb.append(Util.getDynamicComment(comment.getNickname(),comment.getText(),comment.getUid()));
//								}
//								item.setSubcomments(sb);
							} 
							IntroCommentAdapter.this.notifyDataSetChanged();
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
}
