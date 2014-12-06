package com.cyou.mrd.pengyou.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
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
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ContactFriendItem;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.FriendInfoActivity;
import com.cyou.mrd.pengyou.ui.SendSMSActivity;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.viewcache.ContactFriendViewCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class ContactFriendAdapter extends BaseAdapter implements
		SectionIndexer, OnScrollListener {

	private CYLog log = CYLog.getInstance();
	private static final int PAY_ATTENTION = 1;
	private Activity mContext;
	private List<ContactFriendItem> mData;
	private LayoutInflater mInflater;
	private MyHttpConnect mConn;
	private DisplayImageOptions mOptions;

	public ContactFriendAdapter(Activity context, List<ContactFriendItem> data) {
		this.mContext = context;
		this.mData = data;
		mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		this.mOptions = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul).cacheInMemory()
				.cacheOnDisc()
				.displayer(new RoundedBitmapDisplayer(Config.ROUND)).build();
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	public void updateListData(List<ContactFriendItem> data) {
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

	ContactFriendViewCache contactviewcache;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.contact_friend_item, null);
			contactviewcache = new ContactFriendViewCache(convertView);
			convertView.setTag(contactviewcache);
		} else {
			contactviewcache = (ContactFriendViewCache) convertView.getTag();
		}

		final ContactFriendItem item = mData.get(position);
		if (item != null) {
			contactviewcache.getmTopTV().setVisibility(View.GONE);
			if (position == 0) {
				String uid = item.getUid();
				if (TextUtils.isEmpty(uid)) {// 不是朋游用户
					contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
					contactviewcache.getmTopTV().setText(
							R.string.they_not_install);
				} else if (!item.isAttention()) {// 没有被关注的已安装朋游的用户
					contactviewcache.getmTopTV().setText(
							R.string.they_already_install);
					contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
				} else { // 被关注的已安装朋游的用户
					contactviewcache.getmTopTV()
							.setText(R.string.foced_already);
					contactviewcache.getmTopTV().setVisibility(View.VISIBLE);
				}
			} else {
				int last = position - 1;
				if (mData.size() > last && last >= 0) {
					ContactFriendItem lastItem = mData.get(last);
					String userId = lastItem.getUid();
					if (!TextUtils.isEmpty(userId)
							&& TextUtils.isEmpty(item.getUid())) {// 已安装朋游和未安装朋游之间显示"他们还没装朋游"
						contactviewcache.getmTopTV()
								.setVisibility(View.VISIBLE);
						contactviewcache.getmTopTV().setText(
								R.string.they_not_install);
					}
					if (TextUtils.isEmpty(userId)
							&& !TextUtils.isEmpty(item.getUid())) {// 未安装朋游和已安装朋游之间显示
						contactviewcache.getmTopTV()
								.setVisibility(View.VISIBLE);
						if (item.isAttention()) {// 如果是后者关注了,显示"你已关注了他们"
							contactviewcache.getmTopTV().setText(
									R.string.foced_already);
						} else {// 如果是后者没有被关注,显示"他们已经安装了朋游"
							contactviewcache.getmTopTV().setText(
									R.string.they_already_install);
						}
					}
					if (!TextUtils.isEmpty(userId) && lastItem.isAttention()
							&& !TextUtils.isEmpty(item.getUid())
							&& !item.isAttention()) {// 前者被关注，后者未被关注，显示"他们已经安装了朋游"
						contactviewcache.getmTopTV()
								.setVisibility(View.VISIBLE);
						contactviewcache.getmTopTV().setText(
								R.string.they_already_install);
					}
					if (!TextUtils.isEmpty(userId) && !lastItem.isAttention()
							&& !TextUtils.isEmpty(item.getUid())
							&& item.isAttention()) {// 前者未关注，后者被关注，显示"你已关注了他们"
						contactviewcache.getmTopTV()
								.setVisibility(View.VISIBLE);
						contactviewcache.getmTopTV().setText(
								R.string.foced_already);
					}
				}
			}
			if (TextUtils.isEmpty(item.getUid())) {// 不是朋游的用户,显示电话号码和名字,显示邀请
				contactviewcache.getName().setText(item.getName());
				contactviewcache.getName().setGravity(Gravity.CENTER);
				contactviewcache.getTxtTele().setText(item.getPhone());
				contactviewcache.getName().setVisibility(View.VISIBLE);
				contactviewcache.getTxtTele().setVisibility(View.VISIBLE);
				contactviewcache.getUserIcon().setVisibility(View.GONE);
				contactviewcache.getTxtPyouNickName().setVisibility(View.GONE);
				contactviewcache.getmInviteIBtn().setText(R.string.invite);
				contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.contact_invite_btn);
				contactviewcache.getmInviteIBtn().setEnabled(true);
				contactviewcache.getmInviteIBtn().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mContext,
										SendSMSActivity.class);
								intent.putExtra(Params.SEND_SMS.ITEM, item);
								mContext.startActivity(intent);
							}
						});
			}

			if (!TextUtils.isEmpty(item.getUid()) && item.isAttention()) {// 若已安装了朋游且已经关注
				// 已经关注过了显示名字和电话号码
				contactviewcache.getName().setText(item.getName());
				contactviewcache.getName().setGravity(Gravity.CENTER);
				contactviewcache.getName().setVisibility(View.VISIBLE);
				contactviewcache.getTxtTele().setVisibility(View.VISIBLE);
				contactviewcache.getUserIcon().setVisibility(View.GONE);
				contactviewcache.getTxtPyouNickName().setVisibility(View.GONE);
				contactviewcache.getTxtTele().setText(item.getPhone());
				contactviewcache.getmInviteIBtn().setBackgroundResource(0);
				contactviewcache.getmInviteIBtn().setEnabled(false);
				contactviewcache.getmInviteIBtn().setText(R.string.had_focus);
				contactviewcache.getItemClick().setOnClickListener(
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								// 显示用户详情界面
								Intent intent = new Intent(mContext,
										FriendInfoActivity.class);
								intent.putExtra(Params.FRIEND_INFO.UID,
										Integer.parseInt(item.getUid()));
								intent.putExtra(Params.FRIEND_INFO.NICKNAME,
										item.getNickname());
								intent.putExtra(Params.FRIEND_INFO.GENDER,
										item.getGender());
								mContext.startActivity(intent);
							}
						});
			} else if (!TextUtils.isEmpty(item.getUid()) && !item.isAttention()) {// 若已安装了朋游但是没有关注
				contactviewcache.getName().setText(item.getName());
				contactviewcache.getName().setGravity(Gravity.LEFT);
				contactviewcache.getName().setVisibility(View.VISIBLE);
				contactviewcache.getUserIcon().setVisibility(View.VISIBLE);
				contactviewcache.getTxtPyouNickName().setVisibility(
						View.VISIBLE);
				contactviewcache.getTxtPyouNickName().setText(
						item.getNickname());
				contactviewcache.getTxtTele().setVisibility(View.INVISIBLE);
				contactviewcache.getmInviteIBtn().setText(R.string.focus);
				contactviewcache.getmInviteIBtn().setBackgroundResource(
						R.drawable.focus_btn_xbg);
				contactviewcache.getmInviteIBtn().setEnabled(true);
				CYImageLoader.displayImg(item.getPicture(), contactviewcache
						.getUserIcon().getImageView(), mOptions);
				// 跳转到用户详情也页面
				contactviewcache.getItemClick().setOnClickListener(
						new OnClickListener() {
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
								BehaviorInfo behaviorInfo = new BehaviorInfo(
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_ID,
										CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ATT_NAME);
								CYSystemLogUtil.behaviorLog(behaviorInfo);
								int uid = 0;
								int postion = mData.indexOf(item);
								try {
									uid = Integer.parseInt(item.getUid());
									focus(PAY_ATTENTION, uid, item, postion,
											contactviewcache.getmInviteIBtn());
								} catch (Exception e) {
									log.e(e);
								}
							}
						});
			}
		} else {
			log.e("info is null");
		}
		return convertView;
	}

	private void focus(int isfocus, int mUID, final ContactFriendItem item,
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
						item.setRelation(3);
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

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}
}