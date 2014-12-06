package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.SectionIndexer;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.SinaFriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.SNSFriendLstActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.utils.WeiboApi.WeiboApiListener;
import com.cyou.mrd.pengyou.viewcache.SinaFriendViewCache;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class AddSNSFriendAdapter extends BaseAdapter implements SectionIndexer,
		OnScrollListener {

	private CYLog log = CYLog.getInstance();
	private static final int PAY_ATTENTION = 1;
	private static final int CANCEL_ATTENTION = 0;
	private Context mContext;
	private List<SinaFriendItem> mData;
	private LayoutInflater mInflater;
	private String[] mSection;
	private MyHttpConnect mConn;
	SNSFriendLstActivity addConFriendAct;
	private DisplayImageOptions mOptions;
	private Drawable attentionAll;
//	private LinearLayout ll_friend_info;
	private Handler myHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Contants.WEIBO_INVITE_RESULT.SUCCESS) {
				contactviewcache.getmInviteIBtn().setBackgroundResource(0);
				contactviewcache.getmInviteIBtn().setEnabled(false);
				contactviewcache.getmInviteIBtn().setText(R.string.already_invite);
				notifyDataSetChanged();
				Toast.makeText(mContext, R.string.invite_success,
						Toast.LENGTH_SHORT).show();
			} else if(msg.what == Contants.WEIBO_INVITE_RESULT.FAILED) {
				String error_code = JsonUtils.getJsonValue((String) msg.obj,
						"error_code");
				if (!TextUtils.isEmpty(error_code)) {
					switch (Integer.parseInt(error_code)) {
					case Contants.WEIBO_ERROR_CODE.TOKEN_EXPIRED:
					case Contants.WEIBO_ERROR_CODE.EXPIRED_TOKEN:
						// 如果过期的话，重新登录
						Toast.makeText(mContext, R.string.token_expired,
								Toast.LENGTH_SHORT).show();
						break;
					case Contants.WEIBO_ERROR_CODE.OUT_OF_LIMIT:
						// 发微博的次数超过限制
						Toast.makeText(mContext, R.string.out_of_limit,
								Toast.LENGTH_SHORT).show();
						break;
					case Contants.WEIBO_ERROR_CODE.REPEAT_SIMINAL_CONTENT:
						// 发相似的微博
						Toast.makeText(mContext, R.string.repeat_siminal_content,
								Toast.LENGTH_SHORT).show();
						break;
					case Contants.WEIBO_ERROR_CODE.REPEAT_SAME_CONTENT:
						// 发相同的微博
						Toast.makeText(mContext, R.string.repeat_same_content,
								Toast.LENGTH_SHORT).show();
						break;
					case Contants.WEIBO_ERROR_CODE.USER_DOES_NOT_EXISTS:
						// 用户不存在
						Toast.makeText(mContext, R.string.user_does_not_exists,
								Toast.LENGTH_SHORT).show();
						break;
					default:
						Toast.makeText(mContext, R.string.invite_failed,
								Toast.LENGTH_SHORT).show();
					}
				}
			} else {
				Toast.makeText(mContext, R.string.invite_failed,
						Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}

	};

	public AddSNSFriendAdapter(Context context, List<SinaFriendItem> data,
			SNSFriendLstActivity maddConFriendAct) {
		this.mContext = context;
		this.mData = data;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		addConFriendAct = maddConFriendAct;
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul).cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
		attentionAll = mContext.getResources().getDrawable(
				R.drawable.btn_attention_all);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void updateListData(List<SinaFriendItem> data) {
		mData = data;
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	SinaFriendViewCache contactviewcache;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
//		convertView = mInflater.inflate(R.layout.add_sina_friend_item, null);
//		contactviewcache = new SinaFriendViewCache(convertView);
		if(convertView == null){
			convertView =  mInflater.inflate(R.layout.add_sina_friend_item, null);
			contactviewcache = new SinaFriendViewCache(convertView);
			convertView.setTag(contactviewcache);
		}else{
			contactviewcache = (SinaFriendViewCache)convertView.getTag();
		}

		final SinaFriendItem item = mData.get(position);
		if (item != null) {
			contactviewcache.getmTopTV().setVisibility(View.GONE);
			if (position == 0) {
				String uid = item.getUid();
				if (TextUtils.isEmpty(uid)) {// 不是朋游用户
					contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
					contactviewcache.getmTopTV().setText(
							R.string.they_are_not_pengyou_user);
					contactviewcache.getmTopTV().setCompoundDrawables(null,
							null, null, null);
				} else {
					contactviewcache.getmTopTV()
							.setText(R.string.you_may_known);
					contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
					contactviewcache.getmTopTV().setOnClickListener(
							new OnClickListener() {

								@Override
								public void onClick(View v) {
									BehaviorInfo behaviorInfo = new BehaviorInfo(
											CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ALL_ID,
											CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ALL_NAME);
									CYSystemLogUtil.behaviorLog(behaviorInfo);
									focusAll();
								}
							});
					Drawable attentionAll = mContext.getResources().getDrawable(R.drawable.btn_attention_all);
					attentionAll.setBounds(0, 0, attentionAll.getMinimumWidth(), attentionAll.getMinimumHeight());
					contactviewcache.getmTopTV().setCompoundDrawables(null, null, attentionAll, null);
				}
			} else {
				int last = position - 1;
				if (mData.size() > last && last >= 0) {
					SinaFriendItem lastItem = mData.get(last);
					String userId = lastItem.getUid();
					if (!TextUtils.isEmpty(userId)
							&& TextUtils.isEmpty(item.getUid())) {
						contactviewcache.getmTopTV()
								.setVisibility(View.VISIBLE);
						contactviewcache.getmTopTV().setText(
								R.string.they_are_not_pengyou_user);
						contactviewcache.getmTopTV().setCompoundDrawables(null,
								null, null, null);
					}
				}
			}
			if (TextUtils.isEmpty(item.getUid())) {//没有安装过朋游的
				contactviewcache.getTxtTeleName().setText(item.getSnsnicknm());
				contactviewcache.getTxtPyouNickName().setVisibility(View.GONE);
				CYImageLoader
						.displayOtherImage(item.getSnsavatar(),
								contactviewcache.getUserIcon().getImageView(),
								mOptions);
			} else {//安装过朋游的
				contactviewcache.getTxtTeleName().setText(item.getNickname());
				contactviewcache.getTxtPyouNickName().setVisibility(View.VISIBLE);
				contactviewcache.getTxtPyouNickName().setText(item.getSnsnicknm());
				CYImageLoader.displayImg(item.getAvatar(), contactviewcache
						.getUserIcon().getImageView(), mOptions);
			}

			if (!TextUtils.isEmpty(item.getUid()) && item.isAttention()) {// 若已经关注
				contactviewcache.getmInviteIBtn().setBackgroundResource(0);
				contactviewcache.getmInviteIBtn().setEnabled(false);
				contactviewcache.getmInviteIBtn().setText(R.string.had_focus);
				contactviewcache.getmInviteIBtn().setOnClickListener(null);
				// 跳转到用户详情页面
				contactviewcache.GetAllUserInfo().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								FriendInfoActivity.class);
						intent.putExtra(Params.FRIEND_INFO.UID,
								Integer.parseInt(item.getUid()));
						intent.putExtra(Params.FRIEND_INFO.NICKNAME,
								item.getNickname());
						mContext.startActivity(intent);
					}
				});
			} else if (!TextUtils.isEmpty(item.getUid())) {//若只安装且没有关注，就显示关注的btn
				contactviewcache.getmInviteIBtn().setEnabled(true);
				contactviewcache.getmInviteIBtn().setText(R.string.focus);
				contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				// 跳转到用户详情也页面
				contactviewcache.GetAllUserInfo().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(mContext,
								FriendInfoActivity.class);
						intent.putExtra(Params.FRIEND_INFO.UID,
								Integer.parseInt(item.getUid()));
						intent.putExtra(Params.FRIEND_INFO.NICKNAME,
								item.getNickname());
						mContext.startActivity(intent);
					}
				});
				contactviewcache.getmInviteIBtn().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								int postion = mData.indexOf(item);
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ID,
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
								int uid = 0;
								try {
									uid = Integer.parseInt(item.getUid());
									focus(PAY_ATTENTION, uid, item, postion,
											contactviewcache.getmInviteIBtn());
								} catch (Exception e) {
									log.e(e);
								}
							}
						});
			} else if (TextUtils.isEmpty(item.getUid()) && item.isInvisition()) {// sns用户已经邀请
				contactviewcache.getmInviteIBtn().setBackgroundResource(0);
				contactviewcache.getmInviteIBtn().setEnabled(false);
				contactviewcache.getmInviteIBtn().setText(R.string.already_invite);
				contactviewcache.getmInviteIBtn().setOnClickListener(null);
			} else if (TextUtils.isEmpty(item.getUid())) {// sns用户 显示邀请按钮
				contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.contact_invite_btn);
				contactviewcache.getmInviteIBtn().setEnabled(true);
				contactviewcache.getmInviteIBtn().setText(R.string.invite);
				contactviewcache.getmInviteIBtn().setOnClickListener(
						new OnClickListener() {

							@Override
							public void onClick(View arg0) {
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_IMPORT_ID,
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_IMPORT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
								int position = mData.indexOf(item);
								inviteUser(item, position);
							}
						});
			}
		} else {
			log.e("info is null");
		}
		return convertView;
	}

	private void inviteUser(final SinaFriendItem item, final int position) {
		String invaiteText = mContext.getString(R.string.add_friend_sina_text,
				PYVersion.IP.APK_URL, item.getSnsnicknm());
		WeiboApi weiboApi = WeiboApi.getInstance();
		weiboApi.share2Weibo(mContext, invaiteText, null, null,
				new WeiboApiListener() {

					@Override
					public void onSuccess(final boolean success) {
						if (success) {
							item.setInvisition(true);
							mData.set(position, item);
							myHandler.sendEmptyMessage(Contants.WEIBO_INVITE_RESULT.SUCCESS);
						}
					}

					@Override
					public void onFinish(String arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onFaild(Exception exp) {
						Message msg = myHandler.obtainMessage();
						msg.what = Contants.WEIBO_INVITE_RESULT.FAILED;
						msg.obj = exp.getMessage();
						myHandler.sendMessage(msg);
						log.e(exp);
					}
				});
	}

	private void focus(int isfocus, int mUID, final SinaFriendItem item,
			final int postion, final Button imgButton) {
		if (0 == mUID) {
			log.e("mUID is null");
			return;
		}

		new RelationUtil(mContext).focus(mUID, isfocus,
				RelationUtil.SINA_WEIBO, new ResultListener() {

					@Override
					public void onSuccuss(boolean eachFocused) {
						Toast.makeText(mContext, R.string.focus_success,
								Toast.LENGTH_SHORT).show();
						/*
						 * imgButton .setBackgroundResource(R.drawable.
						 * had_attention_btn_normal);
						 */
						imgButton.setBackgroundResource(0);
						imgButton.setEnabled(false);
						imgButton.setText(R.string.had_focus);
						item.setAttention(true);
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

	private void focusAll() {
		RequestParams params = new RequestParams();
		params.put("snsnm", "sina");
		mConn.post(HttpContants.NET.ADD_FRIEND_FOCUSALL, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Toast.makeText(mContext, R.string.download_error_network_error,
								Toast.LENGTH_SHORT).show();
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.v("statusCode = " + statusCode);
						log.i("result = " + content);
						String frdCount = JsonUtils.getJsonValue(
								JsonUtils.getJsonValue(content, "data"),
								"frdcount");
						if (!TextUtils.isEmpty(frdCount)) {
							int count = Integer.parseInt(frdCount);
							if (count > 0) {
								Toast.makeText(mContext,
										R.string.focus_success,
										Toast.LENGTH_SHORT).show();
								for(SinaFriendItem item : mData) {
									if(!TextUtils.isEmpty(item.getUid())&& !item.isAttention()) {
										item.setAttention(true);
									}
								}
								notifyDataSetChanged();
							} else {
								Toast.makeText(mContext, R.string.focus_failed,
										Toast.LENGTH_SHORT).show();
							}

						} else {
							Toast.makeText(mContext, R.string.focus_failed,
									Toast.LENGTH_SHORT).show();
						}
					}
					
					@Override
					public void onLoginOut() {
						super.onLoginOut();
					}
				});

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

}
