package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuessYourFriendAdapter;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ChangeFocusInfo;
import com.cyou.mrd.pengyou.entity.ContactItem;
import com.cyou.mrd.pengyou.entity.GuessYourFriendItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.base.YouMayKnownBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Edit by luochuang
 * 
 * @author luochuang_zk
 * 
 */
public class GuessYouKnowActivity extends CYBaseActivity {

	private TextView tv_head_title;

	private PullToRefreshListView mListView;

	private ImageButton iv_sub_left;
	// 隐藏清空按钮
	// private Button iv_sub_right;

	private MyHttpConnect mConn;

	private GuessYourFriendAdapter mAdapter;

	private List<GuessYourFriendItem> mData;
	// 猜你认识为空
	private TextView tv_guess_empty;
	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);

		setContentView(R.layout.guess_your_know);

		View headview = this.findViewById(R.id.sub_header_guess_your_know);

		tv_head_title = (TextView) headview
				.findViewById(R.id.sub_header_bar_tv);

		tv_head_title.setText(R.string.guess_you_know);

		iv_sub_left = (ImageButton) headview
				.findViewById(R.id.sub_header_bar_left_ibtn);

		// iv_sub_right = (Button) findViewById(R.id.sub_header_bar_right_ibtn);
		// this.iv_sub_right.setBackgroundResource(R.drawable.header_btn_xbg);
		// this.iv_sub_right.setText(R.string.calear_all);

		/*
		 * iv_sub_right.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { mListView.setAdapter(null); }
		 * });
		 */

		iv_sub_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		initView();
		mListView.setOnItemClickListener(new ItemClickListener());
		read();
		// 隐藏长按删除功能
		// mListView.setOnItemLongClickListener(new LongItemClickLister());
	}

	public void initView() {
		tv_guess_empty = (TextView) findViewById(R.id.tv_empty);
		mListView = (PullToRefreshListView) findViewById(R.id.guess_your_know_search_lv);
	}

	@Override
	protected void onStart() {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mData == null) {
			mData = new ArrayList<GuessYourFriendItem>();
		}
		if (mAdapter == null) {
			requestSameGameFriends();
			mAdapter = new GuessYourFriendAdapter(GuessYouKnowActivity.this,
					mData);
		}
		this.mListView.setAdapter(mAdapter);
		super.onStart();
	}
	@Override
	protected void onResume() {
		// 刷新关注按钮页面不同步 广播 TODO
				if (ChangeFocusInfo.changeList != null) {
					ArrayList<ChangeFocusInfo> ChangeList = ChangeFocusInfo.changeList;
					for(ChangeFocusInfo changeFocusBean:ChangeList){
						int follow = changeFocusBean.follow;
						int uid = changeFocusBean.uid;
						if(follow==1||follow==0){
							for(int i=0;i<mData.size();i++){
								if(uid==mData.get(i).getUid()){
									mData.get(i).setIsfocus(follow);
								}
							}
						}
					}
				}
				
				if (mAdapter != null) {
					mAdapter.updateData(mData);
					mAdapter.notifyDataSetChanged();
				}
		super.onResume();
	}
//	private void requestSameGameFriends() {
//		RequestParams params = new RequestParams();
//		mConn.post(HttpContants.NET.SAME_GAME_FRIENDS, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						super.onStart();
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						if (TextUtils.isEmpty(content)) {
//							return;
//						}
//						try {
//							YouMayKnownBase base = new ObjectMapper()
//									.readValue(
//											content,
//											new TypeReference<YouMayKnownBase>() {
//											});
//							if (base != null && base.getData() != null
//									&& !base.getData().isEmpty()) {
//								for (GuessYourFriendItem item : base.getData()) {
//									mData.add(item);
//								}
//							}
//                            if(mData.isEmpty()) {
//                            	tv_guess_empty.setVisibility(View.VISIBLE);
//                            } else {
//                            	tv_guess_empty.setVisibility(View.GONE);
//                            }
//							mListView.loadComplete();
//							mAdapter.notifyDataSetChanged();
//						} catch (Exception e) {
//						}
//					}
//
//					@Override
//					public void onFailure(Throwable error, String content) {
//						try {
//							showNetErrorDialog(GuessYouKnowActivity.this,
//									new ReConnectListener() {
//										@Override
//										public void onReconnect() {
//											requestSameGameFriends();
//										}
//									});
//							super.onFailure(error, content);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//				});
//	}

	private class ItemClickListener implements OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			GuessYourFriendItem item = (GuessYourFriendItem) mListView
					.getItemAtPosition(arg2);
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_FRD_DETAIL_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_FRD_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			if (item != null) {
				Intent intent = new Intent(GuessYouKnowActivity.this,
						FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
				intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
				intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
				startActivity(intent);
			}
		}
	}

	// 常按删除当前条目
	private class LongItemClickLister implements OnItemLongClickListener {
		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view,
				final int position, long id) {
			AlertDialog.Builder builder = new Builder(GuessYouKnowActivity.this);
			builder.setTitle(R.string.dialog_title);
			builder.setMessage(R.string.dialog_message);
			builder.setPositiveButton(R.string.dialog_ok,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							mData.remove(position);
							mAdapter.notifyDataSetChanged();
							dialog.dismiss();
						}
					});
			builder.setNeutralButton(R.string.dialog_cancel,
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			builder.create().show();
			return false;
		}

	}

	@Override
	protected void initEvent() {

	}

	@Override
	protected void initData() {

	}

	private AsyncQueryHandler mQueryHandler;
	private String[] mProjection = {
			ContactsContract.CommonDataKinds.Phone._ID,
			ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
			ContactsContract.CommonDataKinds.Phone.DATA1, "sort_key",
			ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
			ContactsContract.CommonDataKinds.Phone.PHOTO_ID,
			ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY };
	private Uri mUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	Set<String> set = new HashSet<String>();// 过滤重复号码

	private void requestSameGameFriends() {
		if (mQueryHandler == null) {
			mQueryHandler = new MyAsyncQueryHandler(getContentResolver());
		}
		mQueryHandler.startQuery(0, null, mUri, mProjection, null, null,
				"sort_key COLLATE LOCALIZED asc");
	}

	private class MyAsyncQueryHandler extends AsyncQueryHandler {
		public MyAsyncQueryHandler(ContentResolver cr) {
			super(cr);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {

			String contactStr = null;
			try {
				if (cursor != null && cursor.getCount() > 0) {
					StringBuilder sb = new StringBuilder();
					sb.append("[");
					int i = 0;
					while (cursor.moveToNext()) {
						ContactItem item = new ContactItem();
						item.name = cursor.getString(1);
						item.num = cursor.getString(2);
						item.num = item.num != null ? item.num.replace(" ", "") : item.num;
						item.sortKey = cursor.getString(3);
                        if(item.num != null && item.num.equals(UserInfoUtil.getCurrentUserPhoneNum())) {
                        	i++;
                        	continue;
                        }
						if (i != cursor.getCount() - 1) {
							sb.append("{\"phone\":\"" + item.num
									+ "\",\"name\":\"" + item.name + "\"},");
						} else {
							sb.append("{\"phone\":\"" + item.num
									+ "\",\"name\":\"" + item.name + "\"}");
						}
						i++;
					}

					sb.append("]");
					contactStr = sb.toString();
					if (contactStr.length() > 1) {
						contactStr = contactStr.substring(0,
								contactStr.length() - 1)
								+ "]";
					} else {
						contactStr = contactStr.substring(0,
								contactStr.length())
								+ "]";
					}
				} else {
					contactStr = "[]";
				}
			} catch (Exception e) {
				log.e(e);
				contactStr = "[]";
			} finally {
				if (cursor != null) {
					cursor.close();
				}
			}
			RequestParams params = new RequestParams();
			params.put("contacts", contactStr);
			params.put("snsnm", "sina");
			params.put("snsusrid", SharedPreferenceUtil.getSinaWeiboUid());
			params.put("access_token", SharedPreferenceUtil.getSinaWeiboToken());

			mConn.post(HttpContants.NET.GUESS_YOUR_FRIENDS, params,
					new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							super.onStart();
						}
						
						@Override
						public void onLoginOut() {
							LoginOutDialog dialog = new LoginOutDialog(GuessYouKnowActivity.this);
							dialog.create().show();
						}

						@Override
						public void onSuccess(int statusCode, String content) {
							super.onSuccess(statusCode, content);
							if (TextUtils.isEmpty(content)) {
								return;
							}
							try {
								YouMayKnownBase base = new ObjectMapper()
										.readValue(
												content,
												new TypeReference<YouMayKnownBase>() {
												});
								if (base != null && base.getData() != null
										&& !base.getData().isEmpty()) {
									ArrayList<GuessYourFriendItem> fromContacts = new ArrayList<GuessYourFriendItem>();
									ArrayList<GuessYourFriendItem> fromSns = new ArrayList<GuessYourFriendItem>();
									ArrayList<GuessYourFriendItem> fromSameGmPlayer = new ArrayList<GuessYourFriendItem>();
									for (GuessYourFriendItem item : base
											.getData()) {
										//1:通讯录用户   2：微博用户  3：玩同款游戏
										switch(item.getRecommedType()) {
										case 1:
											fromContacts.add(item);
											break;
										case 2:
											fromSns.add(item);
											break;
										case 3:
											fromSameGmPlayer.add(item);
											break;
										default:break;
										}
									}
									if (null != fromContacts
											&& fromContacts.size() > 0) {
										mData.addAll(fromContacts);
									}
									if (null != fromSns && fromSns.size() > 0) {
										mData.addAll(fromSns);
									}
									if (null != fromSameGmPlayer
											&& fromSameGmPlayer.size() > 0) {
										mData.addAll(fromSameGmPlayer);
									}
								}
								if (mData.isEmpty()) {
									tv_guess_empty.setVisibility(View.VISIBLE);
								} else {
									tv_guess_empty.setVisibility(View.GONE);
								}
								mListView.loadComplete();
								mAdapter.notifyDataSetChanged();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onFailure(Throwable error, String content) {
							try {
								showNetErrorDialog(GuessYouKnowActivity.this,
										new ReConnectListener() {
											@Override
											public void onReconnect() {
												requestSameGameFriends();
											}
										});
								super.onFailure(error, content);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});

		}
	}
	
	/**
	 * 反叛猜你认识的推送
	 * xumengyang
	 */
	private void read(){
		SystemCountMsgItem.clearRecommendFriendCount();
		sendBroadcast(new Intent(Contants.ACTION.SYSTEM_MSG_ACTION).putExtra("type", SystemCountMsgItem.RECOMMEND_FRIEND));
		MyHttpConnect.getInstance().post(HttpContants.NET.READ_RECOMMEND_FRIEND_MSG, new RequestParams(), new AsyncHttpResponseHandler(){
		});
	}
}