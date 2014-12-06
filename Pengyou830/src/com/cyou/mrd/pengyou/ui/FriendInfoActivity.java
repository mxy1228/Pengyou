package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.FriendInfoAdapter;
import com.cyou.mrd.pengyou.adapter.RecentlyGameAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.RelationCircleCommentDao;
import com.cyou.mrd.pengyou.entity.Dynamic;
import com.cyou.mrd.pengyou.entity.DynamicComment;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.FriendInfo;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.RelationCircleCache;
import com.cyou.mrd.pengyou.utils.RelationUtil;
import com.cyou.mrd.pengyou.utils.RelationUtil.ResultListener;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
/**
 * 好友列表进入的私信聊天页面
 * @author 
 *
 */
public class FriendInfoActivity extends CYBaseActivity implements
		OnClickListener {
	private static final int SHOW_COMMENT = 1;
	private static final int DELE_COMMENT = 2;
	private static final int SUPPORT = 3;
	private static final int CANCEL_SUPPORT = 4;
	private static final int GET_COMMENT = 5;
	private CYLog log = CYLog.getInstance();
	private PullToRefreshListView mListView;
	private ImageView mAvatarIV;
	private TextView mAgeTV;
	private TextView mLocationTV;
	private TextView mSignTV;
	private RelativeLayout mFocusRL;
	private RelativeLayout mFansRL;
	private TextView mAttentionCountTV;
	private TextView mFensiCountTV;
	private GridView mGV;
	private TextView mTitleTV;
	private ImageButton mBackIBtn;
	private ImageButton mPopBtn;
	private Button mShieldDynamicBtn;
	private Button mShieldLetterBtn;
	private Button mBackHomeBtn;
	private Button mCancelFocusBtn;
	// add by xuhan
	private LinearLayout mShieldDynamicLyt;
	private LinearLayout mShieldLetterLyt;
	private LinearLayout mCancelFocusLyt;
	private PopupWindow mPopWindow;
	private TextView mDynamicTV;
	private View mListViewHeaderView;
	private RelativeLayout mLetterRL;
	private LinearLayout mCommentLL;
	private EditText mCommentET;
	private Button mCommentSendBtn;
	private WaitingDialog mWaitingDialog;
	private TextView mRecentGamesTV;
	private TextView mRecentGameEmptyTV;
	private LinearLayout mEmptyDynamicLL;
	private ImageView mBindPhoneIV;
	private ImageView mBindSinaIV;
	private RelativeLayout mAddFocusRL;
	private TextView mAddFocusTV;
	private TextView mConstellationTV;

	private List<DynamicItem> mDynamicData;
	private RecentlyGameAdapter mRecentlyAdapter;
	private FriendInfoAdapter mDynamicAdapter;
	private int mUID;
	private MyHttpConnect mConn;
	private FriendInfo mFriendInfo;
	private int mCursor = 0;
	private int mCurrentCommentPosition;
	private boolean mSendingComment = false;
	private boolean mSupporting = false;
	private RefreshReceiver mRefreshReceiver;
	private RelationCircleCache  relCache;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_info);
		init();
		initView();
		initData();
		if (mRefreshReceiver == null) {
			mRefreshReceiver = new RefreshReceiver();
		}
		registerReceiver(mRefreshReceiver, new IntentFilter(
				Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO));
		registerReceiver(mRefreshReceiver, new IntentFilter(
				Contants.ACTION.UPDATE_RELATION_COMMENT_INFO));
	}

	private void init() {
		mFriendInfo = new FriendInfo();
		Intent intent = getIntent();
		if (intent != null) {
			mUID = intent.getIntExtra(Params.FRIEND_INFO.UID, 0);
			if (mUID == 0 && intent.getData() != null) {
				mUID = Integer.parseInt(intent.getData().getQueryParameter(
						"uid"));
			}
			mFriendInfo.setNickname(intent
					.getStringExtra(Params.FRIEND_INFO.NICKNAME));
			mFriendInfo.setGender(intent
					.getStringExtra(Params.FRIEND_INFO.GENDER));
		}
		this.relCache = new RelationCircleCache(FriendInfoActivity.this);
	}

	@Override
	protected void initView() {
		this.mWaitingDialog = new WaitingDialog(this);
		this.mWaitingDialog.show();
		this.mCommentLL = (LinearLayout) findViewById(R.id.friend_info_comment_ll);
		this.mCommentET = (EditText) findViewById(R.id.friend_info_comment_et);
		this.mCommentSendBtn = (Button) findViewById(R.id.friend_info_comment_send_btn);
		this.mCommentSendBtn.setOnClickListener(this);
		this.mBackIBtn = (ImageButton) findViewById(R.id.friend_info_back_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		this.mPopBtn = (ImageButton) findViewById(R.id.friend_info_pop_ibtn);
		this.mPopBtn.setOnClickListener(this);
		this.mTitleTV = (TextView) findViewById(R.id.friend_info_header_tv);
		this.mListView = (PullToRefreshListView) findViewById(R.id.friend_info_lv);
		mListViewHeaderView = getLayoutInflater().inflate(
				R.layout.friend_info_header_view, null);
		this.mBindPhoneIV = (ImageView) mListViewHeaderView
				.findViewById(R.id.friend_info_bind_phone);
		this.mBindSinaIV = (ImageView) mListViewHeaderView
				.findViewById(R.id.friend_info_bind_sina);
		this.mEmptyDynamicLL = (LinearLayout) mListViewHeaderView
				.findViewById(R.id.friend_info_dynamic_empty_ll);
		this.mRecentGameEmptyTV = (TextView) mListViewHeaderView
				.findViewById(R.id.player_info_recent_game_empty_tv);
		this.mRecentGamesTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_recent_games_tv);
		this.mAvatarIV = (ImageView) mListViewHeaderView
				.findViewById(R.id.player_info_avatar_iv);
		this.mFocusRL = (RelativeLayout) mListViewHeaderView
				.findViewById(R.id.personal_center_focus_rl);
		this.mFocusRL.setOnClickListener(this);
		this.mAgeTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_age_tv);
		this.mLocationTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_location_tv);
		this.mSignTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_sign_tv);
		this.mFansRL = (RelativeLayout) mListViewHeaderView
				.findViewById(R.id.personal_center_fans_rl);
		this.mFansRL.setOnClickListener(this);
		this.mAttentionCountTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_focus_count_tv);
		this.mFensiCountTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_fans_count_tv);
		this.mGV = (GridView) mListViewHeaderView
				.findViewById(R.id.player_info_gv);
		this.mConstellationTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_constellation_tv);
		View popView = getLayoutInflater().inflate(
				R.layout.friend_info_pop_window, null);
		this.mCancelFocusBtn = (Button) popView
				.findViewById(R.id.friend_info_cancel_focus_btn);
		this.mCancelFocusBtn.setOnClickListener(this);// 取消关注
		this.mShieldDynamicBtn = (Button) popView
				.findViewById(R.id.friend_info_shield_dynamic_btn);
		this.mShieldDynamicBtn.setOnClickListener(this);// 屏蔽动态
		this.mShieldLetterBtn = (Button) popView
				.findViewById(R.id.friend_info_shield_letter_btn);// 屏蔽私信
		this.mShieldLetterBtn.setOnClickListener(this);
		this.mBackHomeBtn = (Button) popView
				.findViewById(R.id.friend_info_back_home_btn);// 返回首页
		this.mBackHomeBtn.setOnClickListener(this);
		// add by xuhan
		this.mCancelFocusLyt = (LinearLayout) popView
				.findViewById(R.id.friend_info_cancel_focus_lyt);
		this.mShieldDynamicLyt = (LinearLayout) popView
				.findViewById(R.id.friend_info_shield_dynamic_lyt);
		this.mShieldLetterLyt = (LinearLayout) popView
				.findViewById(R.id.friend_info_shield_letter_lyt);// 屏蔽私信

		this.mPopWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		this.mPopWindow.setBackgroundDrawable(new BitmapDrawable());// 必须设置，要不然setOutsideTouchable不起作用
		this.mPopWindow.setFocusable(true);
		this.mPopWindow.setOutsideTouchable(true);
		this.mAddFocusRL = (RelativeLayout) mListViewHeaderView
				.findViewById(R.id.friend_info_add_focus_rl);
		this.mAddFocusRL.setOnClickListener(this);
		this.mDynamicTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_dynamic_tv);
		this.mListView.addHeaderView(mListViewHeaderView);
		this.mLetterRL = (RelativeLayout) mListViewHeaderView
				.findViewById(R.id.friend_info_letter_rl);
		if (mUID != UserInfoUtil.getCurrentUserId()) {
			this.mLetterRL.setOnClickListener(this);
		}
		this.mAddFocusTV = (TextView) mListViewHeaderView
				.findViewById(R.id.friend_info_add_focus_tv);
		if (mUID == UserInfoUtil.getCurrentUserId()) {
			this.mCancelFocusLyt.setVisibility(View.GONE);
			this.mAddFocusRL.setVisibility(View.GONE);
			this.mAddFocusRL.setVisibility(View.GONE);
			this.mShieldDynamicLyt.setVisibility(View.GONE);
			this.mShieldLetterLyt.setVisibility(View.GONE);
		}
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (mCommentLL.isShown()) {
					mCommentLL.setVisibility(View.GONE);
				}
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				View v = FriendInfoActivity.this.getCurrentFocus();
				if (v != null) {
					IBinder binder = v.getWindowToken();
					if (binder != null) {
						imm.hideSoftInputFromWindow(binder,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			@Override
			public void onLoad() {
				// 非关注好友，只浏览10条动态
				if (mFriendInfo.getUid() == UserInfoUtil.getCurrentUserId()
						|| mFriendInfo.getIsfocus() == Contants.FOCUS.YES) {
					getDynamic(false);
				} else {
					try {
						mListView.loadingFinish();
						mListView.loadComplete();
					} catch (Exception e) {
					}
				}
			}
		});
		this.mGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GameItem item = (GameItem) mGV.getItemAtPosition(arg2);
				if (item != null) {
					BehaviorInfo behaviorInfo = new BehaviorInfo(
							CYSystemLogUtil.FRIENDDETAIL.BTN_ID,
							CYSystemLogUtil.FRIENDDETAIL.BTN_NAME);
					CYSystemLogUtil.behaviorLog(behaviorInfo);
					Intent intent = new Intent(FriendInfoActivity.this,
							GameCircleDetailActivity.class);
					intent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
					intent.putExtra(Params.INTRO.GAME_NAME, item.getName());
					intent.putExtra(Params.INTRO.GAME_CIRCLE, false);
					startActivity(intent);
				}
			}
		});
		mGV.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.FRIENDDETAIL.BTN_SLIDE_ID,
						CYSystemLogUtil.FRIENDDETAIL.BTN_SLIDE_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				return false;
			}
		});
		mAvatarIV.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mDynamicData == null) {
			mDynamicData = new ArrayList<DynamicItem>();
		}
		requestFriendInfo();
	}

	private void initFriendInfo() {
		if (!TextUtils.isEmpty(mFriendInfo.getNickname())) {
			mTitleTV.setText(mFriendInfo.getNickname());
		}
		if (!TextUtils.isEmpty(mFriendInfo.getGender())) {
			if (Integer.parseInt(mFriendInfo.getGender()) == Contants.GENDER_TYPE.BOY) {
				this.mDynamicTV.setText(getString(R.string.his_dynamic,
						getString(R.string.him)));
				mAgeTV.setCompoundDrawablesWithIntrinsicBounds(null, null,
						getResources().getDrawable(R.drawable.boy_sign), null);
			} else {
				this.mDynamicTV.setText(getString(R.string.his_dynamic,
						getString(R.string.her)));
				mAgeTV.setCompoundDrawablesWithIntrinsicBounds(null, null,
						getResources().getDrawable(R.drawable.girl_sign), null);
			}
		} else {
			this.mDynamicTV.setText(getString(R.string.his_dynamic,
					getString(R.string.him)));
		}
		if (mFriendInfo.getBirthday() != null) {
			mAgeTV.setText(getString(R.string.age, String.valueOf(Util
					.getAge(Long.valueOf(mFriendInfo.getBirthday())))));
			mConstellationTV.setText(Util.getConstellation(Long
					.valueOf(mFriendInfo.getBirthday())));
		} else {
			mAgeTV.setText(getString(R.string.age, "25"));
			mConstellationTV.setText(R.string.default_constellation);
		}
		if (!TextUtils.isEmpty(mFriendInfo.getSignature())) {
			mSignTV.setText(mFriendInfo.getSignature());
		} else {
			mSignTV.setText(R.string.friend_sign_default);
		}
		if (!TextUtils.isEmpty(mFriendInfo.getPicture())) {
			DisplayImageOptions option = new DisplayImageOptions.Builder()
					.showImageForEmptyUri(R.drawable.avatar_defaul)
					.showImageOnFail(R.drawable.avatar_defaul)
					.showStubImage(R.drawable.avatar_defaul)
					.cacheInMemory(true).cacheOnDisc(true)
					.displayer(new RoundedBitmapDisplayer(120)).build();
			CYImageLoader.displayImg(mFriendInfo.getPicture(), mAvatarIV,
					option);
		}
		if (mFriendInfo.getUid() == UserInfoUtil.getCurrentUserId()) {
			mAddFocusRL.setVisibility(View.GONE);
		} else {
			if (mFriendInfo.getIsfocus() == Contants.FOCUS.YES) {
				mCancelFocusLyt.setVisibility(View.VISIBLE);
				mAddFocusTV.setText(R.string.had_focus);
				mAddFocusTV.setCompoundDrawablesWithIntrinsicBounds(
						getResources().getDrawable(
								R.drawable.friend_info_had_foucs_pic), null,
						null, null);
				mAddFocusRL.setOnClickListener(null);
			} else {
				mCancelFocusLyt.setVisibility(View.GONE);
				mAddFocusTV.setText(R.string.focus);
				mAddFocusTV.setCompoundDrawablesWithIntrinsicBounds(
						getResources().getDrawable(
								R.drawable.friend_info_foucs_pic), null, null,
						null);
				mAddFocusRL.setOnClickListener(this);
			}
		}
		if (TextUtils.isEmpty(mFriendInfo.getPhone())) {
			mBindPhoneIV.setImageResource(R.drawable.phone_grey);
		} else {
			mBindPhoneIV.setImageResource(R.drawable.phone_light);
		}
		if (mFriendInfo.getSnsbindlist() != null
				&& !mFriendInfo.getSnsbindlist().isEmpty()) {
			if (mFriendInfo.getSnsbindlist().get(0).getSnsname().equals("sina")) {
				mBindSinaIV.setImageResource(R.drawable.sina_light);
			}
		} else {
			mBindSinaIV.setImageResource(R.drawable.sina_grey);
		}
		if (mFriendInfo.getMsgmask().equals(Contants.SHIELD.YES)) {
			mShieldLetterBtn.setText(getString(R.string.cancel_shield_letter));
		} else {
			mShieldLetterBtn.setText(getString(R.string.shield_letter));
		}
		if (mFriendInfo.getActmask().equals(Contants.SHIELD.YES)) {
			mShieldDynamicBtn
					.setText(getString(R.string.cancel_shield_dynamic));
		} else {
			mShieldDynamicBtn.setText(getString(R.string.shield_dynamic));
		}
		mAttentionCountTV.setText(String.valueOf(mFriendInfo.getFocus()));
		mFensiCountTV.setText(String.valueOf(mFriendInfo.getFans()));
		if (!TextUtils.isEmpty(mFriendInfo.getAreaid())) {
			mLocationTV.setText(mFriendInfo.getAreaid());
		} else {
			mLocationTV.setText(R.string.location_default);
		}
		initRecentGames(mFriendInfo.getRecentgms());
	}

	private void initRecentGames(List<GameItem> data) {
		if (data != null && !data.isEmpty()) {
			mRecentlyAdapter = new RecentlyGameAdapter(this, data);
			mGV.setAdapter(mRecentlyAdapter);
			int columWidth = getResources().getDimensionPixelSize(
					R.dimen.recent_game_padding)
					* 2
					+ getResources().getDimensionPixelSize(R.dimen.icon_width);
			int totalWidth = data.size() * columWidth;
			LayoutParams params = new LayoutParams(totalWidth,
					LayoutParams.WRAP_CONTENT);
			mGV.setLayoutParams(params);
			mGV.setNumColumns(data.size());
			mGV.setColumnWidth(columWidth);
			mGV.setStretchMode(GridView.NO_STRETCH);
			this.mRecentGamesTV.setText(this.getString(
					R.string.friend_info_recent_games_counts, data.size()));
		} else {
			mGV.setVisibility(View.GONE);
			int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
			LayoutParams params = new LayoutParams(screenWidth,
					LayoutParams.WRAP_CONTENT);
			mRecentGameEmptyTV.setLayoutParams(params);
			mRecentGameEmptyTV.setVisibility(View.VISIBLE);
			this.mRecentGamesTV.setText(this.getString(
					R.string.friend_info_recent_games_counts, 0));
		}
	}

	private void requestFriendInfo() {
		log.d("requestFriendInfo");
		RequestParams params = new RequestParams();
		params.put("frdid", String.valueOf(mUID));
		mConn.post(HttpContants.NET.FRIEND_INFO, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						mWaitingDialog.show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						try {
							showNetErrorDialog(FriendInfoActivity.this,
									new ReConnectListener() {

										@Override
										public void onReconnect() {
											requestFriendInfo();
										}
									});
						} catch (Exception e) {
						}
						log.e(content);
						log.e(error.getMessage());
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.v("statusCode = " + statusCode);
						log.i("result = " + content);
						try {
							if (!TextUtils.isEmpty(content)) {
								try {
									JSONObject obj = new JSONObject(content);
									if (!obj.has("data")) {
										mWaitingDialog.dismiss();
										FriendInfoActivity.this.finish();
										return;
									}
									String json = JsonUtils.getJsonValue(
											content, "data");
									mFriendInfo = new ObjectMapper()
											.configure(
													DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
													false)
											.readValue(
													json,
													new TypeReference<FriendInfo>() {
													});
									if (mFriendInfo != null) {
										if (TextUtils.isEmpty(mFriendInfo
												.getActmask())) {
											mFriendInfo
													.setActmask(Contants.SHIELD.NO);
										}
										if (TextUtils.isEmpty(mFriendInfo
												.getMsgmask())) {
											mFriendInfo
													.setMsgmask(Contants.SHIELD.NO);
										}
										initFriendInfo();
									}
									getDynamic(false);
								} catch (Exception e) {
									log.e(e);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_info_cancel_focus_btn:
			new AlertDialog.Builder(FriendInfoActivity.this)
					.setTitle(R.string.cancel_focus)
					.setPositiveButton(R.string.ok,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									cancelFocus();
								}
							})
					.setNegativeButton(R.string.cancel,
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create().show();

			break;
		case R.id.friend_info_add_focus_rl:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_ATT_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_ATT_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			int action = 0;
			if (Contants.FOCUS.YES == mFriendInfo.getIsfocus()) {
				action = Contants.FOCUS.NO;
			} else {
				action = Contants.FOCUS.YES;
			}
			new RelationUtil(FriendInfoActivity.this).focus(mUID, action, null,
					new ResultListener() {

						@Override
						public void onSuccuss(boolean eachFocused) {
							if (mFriendInfo.getIsfocus() == Contants.FOCUS.YES) {
								mFriendInfo.setIsfocus(Contants.FOCUS.NO);
								mAddFocusTV.setText(R.string.focus);
								mAddFocusTV
										.setCompoundDrawablesWithIntrinsicBounds(
												getResources()
														.getDrawable(
																R.drawable.friend_info_foucs_pic),
												null, null, null);
								mAddFocusRL
										.setOnClickListener(FriendInfoActivity.this);
							} else {
								mFriendInfo.setIsfocus(Contants.FOCUS.YES);
								mAddFocusTV.setText(R.string.had_focus);
								mAddFocusTV
										.setCompoundDrawablesWithIntrinsicBounds(
												getResources()
														.getDrawable(
																R.drawable.friend_info_had_foucs_pic),
												null, null, null);
								mCancelFocusLyt.setVisibility(View.VISIBLE);
								mAddFocusRL.setOnClickListener(null);
							}
							
							
							// 需求：未关注好友只能浏览10条动态，关注或取消关注后需要刷新动态
							getDynamic(true);

						}

						@Override
						public void onFailed() {

						}

					});
			break;
		case R.id.friend_info_back_ibtn:
			FriendInfoActivity.this.finish();
			break;
		case R.id.friend_info_pop_ibtn:
			this.mPopWindow.showAsDropDown(mPopBtn, 0, 0);
			break;
		case R.id.friend_info_shield_dynamic_btn:
			shieldDyanmic();
			break;
		case R.id.friend_info_shield_letter_btn:
			shieldLetter();
			break;
		case R.id.friend_info_back_home_btn:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_MAINPAGE_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_MAINPAGE_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent mIntent = new Intent();
			mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mIntent.setClass(FriendInfoActivity.this, MainActivity.class);
			startActivity(mIntent);
			finish();
			break;
		case R.id.friend_info_letter_rl:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_PRIMSG_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_PRIMSG_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent iChat = new Intent(FriendInfoActivity.this,
					ChatActivity.class);
			iChat.putExtra(Params.CHAT.FROM, mUID);
			startActivity(iChat);
			break;
		case R.id.personal_center_focus_rl:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_ATT_LIST_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_ATT_LIST_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent iFocus = new Intent(FriendInfoActivity.this,
					MyFocusActivity.class);
			iFocus.putExtra(Params.FOCUS.UID, mUID);
			startActivity(iFocus);
			break;
		case R.id.personal_center_fans_rl:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_FANS_LIST_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_FANS_LIST_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			Intent iFans = new Intent(FriendInfoActivity.this,
					MyFansActivity.class);
			iFans.putExtra(Params.FANS.UID, mUID);
			startActivity(iFans);
			break;
		case R.id.friend_info_comment_send_btn:
			behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_PUBLISH_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_DYNAMIC_COMMENT_PUBLISH_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			if (v.getTag(R.id.relationship_circle_empty) != null) {
				int position = Integer.valueOf(v.getTag(
						R.id.relationship_circle_empty).toString());
				DynamicCommentReplyItem reItem = (DynamicCommentReplyItem) v
						.getTag(R.id.relation_circle_dot);

				String content = mCommentET.getText().toString();
				if (TextUtils.isEmpty(content.replaceAll("\\s*", ""))) {
					return;
				}

				mCommentET.setText("");
				if (TextUtils.isEmpty(content)) {
					return;
				}
				DynamicItem item = (DynamicItem) mListView
						.getItemAtPosition(mCurrentCommentPosition + 1);
				if (item == null) {
					return;
				}
				sendComment(content, item.getAid(), position, reItem);
			}
			break;
		case R.id.player_info_avatar_iv:
			String imageBigUrl = mFriendInfo.getPictureorig();
			String imageUrl = mFriendInfo.getPicture();
			if (TextUtils.isEmpty(imageUrl) && TextUtils.isEmpty(imageBigUrl)) {
				return;
			}
			mIntent = new Intent();
			mIntent.putExtra(Params.SHOW_PHOTO.PHOTO_TYPE,
					Params.SHOW_PHOTO.PHOTO_PIC);
			mIntent.setClass(FriendInfoActivity.this,
					ShowLargePhotoActivity.class);
			if (!TextUtils.isEmpty(imageUrl)) {
				mIntent.putExtra(Params.PHOTO_MIDDLE_URL, imageUrl);
			}
			if (!TextUtils.isEmpty(imageBigUrl)) {
				mIntent.putExtra(Params.PHOTO_URL, imageBigUrl);
			}
			startActivity(mIntent);
			break;
		default:
			break;
		}
	}

	// 取消关注
	private void cancelFocus() {
		new RelationUtil(FriendInfoActivity.this).focus(mUID,
				Contants.FOCUS.NO, null, new ResultListener() {

					@Override
					public void onSuccuss(boolean eachFocused) {
						mFriendInfo.setIsfocus(Contants.FOCUS.NO);
						mAddFocusTV.setText(R.string.focus);
						mAddFocusTV.setCompoundDrawablesWithIntrinsicBounds(
								getResources().getDrawable(
										R.drawable.friend_info_foucs_pic),
								null, null, null);
						mAddFocusRL.setOnClickListener(FriendInfoActivity.this);
						mCancelFocusLyt.setVisibility(View.GONE);
						// 需求：未关注好友只能浏览10条动态，关注或取消关注后需要刷新动态
						getDynamic(true);
					}

					@Override
					public void onFailed() {

					}
				});
	}

	/**
	 * 获取动态
	 */
	private void getDynamic(final boolean clear) {
		RequestParams params = new RequestParams();
		params.put("uid", String.valueOf(mUID));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		if (clear) {
			mCursor = 0;
		}
		params.put("cursor", String.valueOf(mCursor));
		mConn.post(HttpContants.NET.GET_USER_DYNAMIC, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							log.i("result = " + content);
							Dynamic dynamic = new ObjectMapper()
									.configure(
											DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
											false).readValue(content,
											new TypeReference<Dynamic>() {
											});
							if (dynamic.getData() == null) {
								return;
							}
							if (dynamic.getData() != null) {
								if (mDynamicAdapter == null) {
									mDynamicAdapter = new FriendInfoAdapter(
											FriendInfoActivity.this,
											mDynamicData, mHandler);
									mListView.setAdapter(mDynamicAdapter);
								}
							}
							if (!dynamic.getData().isEmpty()) {
								if (clear) {
									mDynamicData.clear();
									mListView.onRefreshFinish();
								}
								mDynamicData.addAll(dynamic.getData());
								mDynamicAdapter.notifyDataSetChanged();
								mCursor = dynamic.getData()
										.get(dynamic.getData().size() - 1)
										.getCursor();
								if (dynamic.getData().size() < Config.PAGE_SIZE) {
									mListView.loadComplete();
								}
							} else {
								mListView.loadComplete();
							}
							mListView.loadingFinish();
						} catch (Exception e) {
							log.e(e);
						}
						mWaitingDialog.dismiss();
						if (mDynamicData.isEmpty()) {
							mEmptyDynamicLL.setVisibility(View.VISIBLE);
						}
					}
				});
	}

	/**
	 * 屏蔽动态
	 */
	private void shieldDyanmic() {
		RequestParams params = new RequestParams();
		params.put("frdid", String.valueOf(mUID));
		if (mFriendInfo.getActmask().equals(Contants.SHIELD.YES)) {
			params.put("ismask", String.valueOf(0));
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_SHOW_RELATION_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_SHOW_RELATION_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
		} else {
			params.put("ismask", String.valueOf(1));
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_GONE_RELATION_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_GONE_RELATION_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
		}
		mConn.post(HttpContants.NET.SHIELD_DYNAMIC, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("shield dynamic result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has("successful")) {
								if (obj.getInt("successful") == 1) {
									if (mFriendInfo.getActmask().equals(
											Contants.SHIELD.NO)) {
										mFriendInfo
												.setActmask(Contants.SHIELD.YES);
										mShieldDynamicBtn
												.setText(getString(R.string.cancel_shield_dynamic));
									} else {
										mFriendInfo
												.setActmask(Contants.SHIELD.NO);
										mShieldDynamicBtn
												.setText(getString(R.string.shield_dynamic));
									}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}

				});
	}

	/**
	 * 屏蔽私信
	 */
	private void shieldLetter() {
		RequestParams params = new RequestParams();
		params.put("frdid", String.valueOf(mUID));
		if (mFriendInfo.getMsgmask().equals(Contants.SHIELD.YES)) {
			params.put("ismask", String.valueOf(0));
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_SHOW_PRIMSG_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_SHOW_PRIMSG_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
		} else {
			params.put("ismask", String.valueOf(1));
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.FRIENDDETAIL.BTN_GONE_PRIMSG_ID,
					CYSystemLogUtil.FRIENDDETAIL.BTN_GONE_PRIMSG_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
		}
		mConn.post(HttpContants.NET.SHIELD_LETTER, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("shield letter result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has("successful")) {
								if (obj.getInt("successful") == 1) {
									if (mFriendInfo.getMsgmask().equals(
											Contants.SHIELD.NO)) {
										mFriendInfo
												.setMsgmask(Contants.SHIELD.YES);
										mShieldLetterBtn
												.setText(getString(R.string.cancel_shield_letter));
									} else {
										mFriendInfo
												.setMsgmask(Contants.SHIELD.NO);
										mShieldLetterBtn
												.setText(getString(R.string.shield_letter));
									}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}

				});
	}

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_COMMENT:
				if (!mCommentLL.isShown()) {
					mCommentLL.setVisibility(View.VISIBLE);
				}
				mCommentET.requestFocus();
				mCurrentCommentPosition = msg.arg1;
				mCommentSendBtn
						.setTag(R.id.relationship_circle_empty, msg.arg1);
				mCommentSendBtn.setTag(R.id.relation_circle_dot, msg.obj);
				if (msg.obj != null) {
					DynamicCommentReplyItem reItem = (DynamicCommentReplyItem) msg.obj;
					mCommentET.setHint(getString(R.string.reply)
							+ reItem.getNickname() + ":");
					mCommentSendBtn.setText(R.string.comment);
				} else {
					mCommentET.setHint(getString(R.string.comment));
					mCommentSendBtn.setText(R.string.send);
				}
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 100);
				break;
			case DELE_COMMENT:
				DynamicCommentItem commentItem = (DynamicCommentItem)msg.obj;
				int position = msg.arg1;
				deleteComment(commentItem, position);
				break;
			case SUPPORT:
				support(msg.arg2, msg.arg1 ,0);
				break;
			case CANCEL_SUPPORT:
				support(msg.arg2, msg.arg1 ,1);
				break;
			case GET_COMMENT:
				getComments(msg.arg2, msg.arg1);
				break;
			default:
				break;
			}
		};
	};

	private void sendComment(final String text, final int aid, final int position,
			final DynamicCommentReplyItem reItem) {
		if (mSendingComment) {
			Toast.makeText(FriendInfoActivity.this, R.string.comment_sending_waiting, Toast.LENGTH_SHORT).show();
			return;
		}

		final DynamicItem item = (DynamicItem) mListView
				.getItemAtPosition(position + 1);
		if (item == null) {
			return;
		}
		item.setCommentcnt(item.getCommentcnt() + 1);
		final DynamicCommentItem dyItem = new DynamicCommentItem();
		dyItem.setUid(UserInfoUtil.getCurrentUserId());
		dyItem.setNickname(UserInfoUtil
				.getCurrentUserNickname());
		dyItem.setComment(text);
		dyItem.setText(text);
		dyItem.setReplyto(reItem);
		dyItem.setSendSuccess(1);
		dyItem.setCid(-1);
		dyItem.setTimestamp(System.currentTimeMillis());
		item.getSubCommentData().add(0, dyItem);
		mDynamicAdapter.notifyDataSetChanged();

		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		params.put("text", text);
		if (reItem != null) {
			params.put("reuid", Integer.valueOf(reItem.getUid()).toString());
		}
		mConn.post(HttpContants.NET.SEND_DYNAMIC_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						mSendingComment = true;
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mSendingComment = false;
						try {
							mDynamicAdapter.relCache.saveFailedComment(dyItem,
									item.getAid());
							Config.needResendComment = true;
							// 隐藏评论输入框和输入法
							if (mCommentLL.isShown()) {
								mCommentLL.setVisibility(View.GONE);
								mCommentET.setText("");
							}
							View v = FriendInfoActivity.this.getCurrentFocus();
							if (v != null) {
								IBinder binder = v.getWindowToken();
								if (binder != null) {
									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.hideSoftInputFromWindow(binder,
											InputMethodManager.HIDE_NOT_ALWAYS);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mSendingComment = false;
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("send comment result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
									String cid = JsonUtils.getJsonValue(
											JsonUtils.getJsonValue(content,
													"data"), "cid");
									DynamicCommentItem dyItem2 = new DynamicCommentItem();
									dyItem2.setUid(UserInfoUtil
											.getCurrentUserId());
									dyItem2.setNickname(Util
											.getNickName(FriendInfoActivity.this));
									dyItem2.setComment(text);
									dyItem2.setText(text);
									dyItem2.setReplyto(reItem);
									dyItem2.setCid(Integer.parseInt(cid));
									dyItem2.setSendSuccess(0);
									dyItem2.setTimestamp(System
											.currentTimeMillis());									
									int index = item.getSubCommentData().indexOf(dyItem);
									if(index >=0 ){
										item.getSubCommentData().remove(index);
										item.getSubCommentData().add(index,dyItem2);
										mDynamicAdapter.notifyDataSetChanged();
										try {
											Intent intent = new Intent(
													Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,0); //0:评论 1：删除评论
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,3); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dyItem2);
											sendBroadcast(intent);
										} catch (Exception e) {
											// TODO: handle exception
										}

									}

									// 隐藏评论输入框和输入法
									if (mCommentLL.isShown()) {
										mCommentLL.setVisibility(View.GONE);
										mCommentET.setText("");
									}
									View v = FriendInfoActivity.this
											.getCurrentFocus();
									if (v != null) {
										IBinder binder = v.getWindowToken();
										if (binder != null) {
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													binder,
													InputMethodManager.HIDE_NOT_ALWAYS);
										}
									}
								} else {
									String errorNo = JsonUtils.getJsonValue(
											content, "errorNo");
	        						if (!TextUtils.isEmpty(errorNo))
	        						{
	        							if(errorNo.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)){
	        								showShortToast(R.string.comment_maskword_failed);
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)){
	        								showShortToast(R.string.comment_deleted_send_failed);
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)){
	        								showShortToast(R.string.comment_operation_send_failed);
	        							}else {
	        								showShortToast(R.string.comment_send_failed);
	        							}
	        						}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						mSendingComment = false;
					}
				});
	}

	/*
	 * 
	 * */
	public void support(final int aid, final int position, final int cancel) {
		if(mSupporting){
			return;
		}
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		log.i("support cancel=  " + cancel + "  aid:" + aid);
		if(cancel == 1){
			params.put("cancel", "1");
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.SUPPORT_DYNAMIC,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						mSupporting = true;
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(FriendInfoActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						mSupporting = false;
						Config.needResendSupport = true;
						try {
							relCache.saveFailedSupport(aid,cancel);
							if (cancel == 0) {
								mDynamicData.get(position).setSupportcnt(
										mDynamicData.get(position)
												.getSupportcnt() + 1);
							} else {
								mDynamicData.get(position).setSupportcnt(
										mDynamicData.get(position)
												.getSupportcnt() - 1);
							}
							mDynamicAdapter.notifyDataSetChanged();
							if(cancel == 0){
							    Toast.makeText(FriendInfoActivity.this, FriendInfoActivity.this.getString(R.string.support_failed_comments), Toast.LENGTH_SHORT).show();
							}
							else{
							    Toast.makeText(FriendInfoActivity.this, FriendInfoActivity.this.getString(R.string.cancel_support_failed_comments), Toast.LENGTH_SHORT).show();
							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							mSupporting = false;
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
									if (cancel == 0) {
										mDynamicData
												.get(position)
												.setSupportcnt(
														mDynamicData
																.get(position)
																.getSupportcnt() + 1);
										mDynamicData.get(position)
												.setSupported(
														Contants.SUPPORT.YES);
									} else {
										mDynamicData
												.get(position)
												.setSupportcnt(
														mDynamicData
																.get(position)
																.getSupportcnt() - 1);
										mDynamicData.get(position)
												.setSupported(
														Contants.SUPPORT.NO);
									}

									mDynamicAdapter.notifyDataSetChanged();
									sendUpdateSupportBroadcast(aid,cancel);
									if (cancel == 0)
										Toast.makeText(
												FriendInfoActivity.this,
												FriendInfoActivity.this.getString(R.string.support_success),
												Toast.LENGTH_SHORT).show();
									else
										Toast.makeText(
												FriendInfoActivity.this,
												FriendInfoActivity.this.getString(R.string.cancel_support_success),
												Toast.LENGTH_SHORT).show();
								}
								else {
									String errorNo = JsonUtils.getJsonValue(
											content, "errorNo");
	        						if (!TextUtils.isEmpty(errorNo))
	        						{
	        							if(errorNo.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)){
	        								Toast.makeText(FriendInfoActivity.this,R.string.comment_maskword_failed, Toast.LENGTH_SHORT).show();
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)){
	        								Toast.makeText(FriendInfoActivity.this,R.string.comment_deleted_send_failed, Toast.LENGTH_SHORT).show();
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)){
	        								Toast.makeText(FriendInfoActivity.this,R.string.comment_operation_send_failed, Toast.LENGTH_SHORT).show();
	        							}else {
	        								Toast.makeText(FriendInfoActivity.this,R.string.comment_send_failed, Toast.LENGTH_SHORT).show();
	        							}
	        						}
	        						if (cancel == 0) {
	        							mDynamicData.get(position).setSupported(
												Contants.SUPPORT.NO);
									} else {
										mDynamicData.get(position).setSupported(
												Contants.SUPPORT.YES);
									}
	        						mDynamicAdapter.notifyDataSetChanged();
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						mSupporting = false;
					}
				});
	}
	
	/**
	 * 获取动态评论
	 * 
	 * @param aid
	 *            //动态id
	 */
	private void getComments(int aid, final int position) {
		final DynamicItem i = mDynamicData.get(position);
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		if (!TextUtils.isEmpty(i.getComment())) {
			params.put("cursor", String.valueOf(i.getLastcommentid()));
		}
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_DYNAMIC_COMMENT,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						i.setLoadingComments(true);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(FriendInfoActivity.this);
						dialog.create().show();
					}
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						try {
							 Toast.makeText(FriendInfoActivity.this, R.string.networks_available, Toast.LENGTH_SHORT).show();
			                 i.setCommentUpdate(true);
			                 mDynamicAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							// TODO: handle exception
						}
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						log.d("getComments = " + content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							DynamicComment comment = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<DynamicComment>() {
											});
							if (comment != null) {
								if (comment.getData() != null
										&& !comment.getData().isEmpty()) {
										i.setSubCommentData(comment.getData());
										i.setComment(Util.dynamicComment2Html(comment.getData()));

										i.setLastcommentid(comment.getData()
												.get(0).getCid());
										i.setCommentcnt(comment.getData().size());
										i.setCommentUpdate(false);							
								}
								else{
									i.setCommentcnt(0);
								}
							}
							else {
								i.setCommentcnt(0);
							}
							i.setLoadingComments(false);
							mDynamicAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}

	private void  deleteComment(final DynamicCommentItem dynamicCommentItem, final int position){
		final DynamicItem i = mDynamicData.get(position);
		RequestParams params = new RequestParams();
		if(dynamicCommentItem.getSendSuccess() == 1){
            i.getSubCommentData().remove(dynamicCommentItem);
    		if(i.getCommentcnt() >0){
                  i.setCommentcnt(i.getCommentcnt() - 1);
            }
            try {
            	new RelationCircleCommentDao(FriendInfoActivity.this).delete(i.getAid(), dynamicCommentItem.getCid(), 0, dynamicCommentItem.getTimestamp());
            	mDynamicAdapter.notifyDataSetChanged();
			} catch (Exception e) {
			}
			return;
		}
		params.put("cid", String.valueOf(dynamicCommentItem.getCid()));
		MyHttpConnect.getInstance().post(HttpContants.NET.DELETE_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						try {
		                    Toast.makeText(FriendInfoActivity.this,R.string.networks_available, Toast.LENGTH_SHORT).show();
						} catch (Exception e) {
							// TODO: handle exception
						}			
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(FriendInfoActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("send comment result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
									if (i == null) {
										return;
									}
									for (int ii = 0; ii < i.getSubCommentData()
											.size(); ii++) {
										if (i.getSubCommentData().get(ii)
												.getCid() == dynamicCommentItem
												.getCid()) {
											i.getSubCommentData().remove(ii);
											if (i.getCommentcnt() > 0) {
												i.setCommentcnt(i
														.getCommentcnt() - 1);
											}
											sendUpdateCommentBroadcast(
													i.getAid(),
													dynamicCommentItem);
											Toast.makeText(
													FriendInfoActivity.this,
													R.string.delete_success,
													Toast.LENGTH_SHORT).show();
											break;
										}
									}
									mDynamicAdapter.notifyDataSetChanged();
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if (mRefreshReceiver != null) {
				unregisterReceiver(mRefreshReceiver);
				mRefreshReceiver = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 接到广播后刷新zan数据
	 * 
	 * @author bichunhe
	 * 
	 */
	private class RefreshReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO.equals(intent
					.getAction())) {
				try {
					int aid = intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					log.i("FriendInfoActivity UPDATE_RELATION_SUPPORT_INFO aid: "
							+ aid);
					int cancel = intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT, 0);
					if (intent.getIntExtra(
							Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0) == 3) {
						// 赞同步来自他的动态 自己不必更新自己
						return;
					}
					if (mDynamicData != null && mDynamicData.size() > 0) {
						boolean needNotify = false;
						for (DynamicItem dynamicItem : mDynamicData) {
							if (aid == dynamicItem.getAid()) {
								int index = mDynamicData.indexOf(dynamicItem);
								log.i("FriendInfoActivity UPDATE_RELATION_SUPPORT_INFO index: "
										+ index);
								if (cancel == 0) {
									mDynamicData.get(index).setSupportcnt(
											dynamicItem.getSupportcnt() + 1);
									mDynamicData.get(index).setSupported(
											Contants.SUPPORT.YES);
								} else {
									mDynamicData.get(index).setSupportcnt(
											dynamicItem.getSupportcnt() - 1);
									mDynamicData.get(index).setSupported(
											Contants.SUPPORT.NO);
								}
								needNotify = true;
								break;
							}
						}
						if (needNotify)
							mDynamicAdapter.notifyDataSetChanged();
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			else if(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO.equals(intent.getAction())){
				try {
					int source =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0);
					if(source == 3){
						return;
					}
					int type =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE, 0);			
					int aid =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					DynamicCommentItem mCItem = (DynamicCommentItem)intent.getSerializableExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT);
					if(mDynamicData!= null && mDynamicData.size() > 0){
						int index= 0;
						boolean needed = false;
						for(DynamicItem item: mDynamicData){
							if(item.getAid() == aid){
								index =  mDynamicData.indexOf(item);
								needed = true;
								break;
							}
						}
						if(needed){
							if(type == 0){
								if(mDynamicData.get(index).getSubCommentData() != null){
									for(DynamicCommentItem mItem : mDynamicData.get(index).getSubCommentData()){
										if(mItem.getCid() == mCItem.getCid()){
											needed = false;
											break;
										}
									}
								}
								else {
									needed = false;
								}
								if(needed){
									mDynamicData.get(index).setCommentcnt(mDynamicData.get(index).getCommentcnt() + 1);
									mDynamicData.get(index).getSubCommentData().add(0, mCItem);
									mDynamicAdapter.notifyDataSetChanged();
								}
							}
							else {
								int cIndex = 0;
								if(mDynamicData.get(index).getSubCommentData() != null){
									for(DynamicCommentItem mItem : mDynamicData.get(index).getSubCommentData()){
										if(mItem.getCid() == mCItem.getCid()){
											cIndex = mDynamicData.get(index).getSubCommentData().indexOf(mItem);
											needed = true;
											break;
										}
										else{
											needed = false;
										}
									}
								}
								else {
									needed = false;
								}
								if(needed){
									mDynamicData.get(index).setCommentcnt(mDynamicData.get(index).getCommentcnt() - 1);
									mDynamicData.get(index).getSubCommentData().remove(cIndex);
									mDynamicAdapter.notifyDataSetChanged();
								}
							}
						}
					}
				} catch (Exception e) {
					
					// TODO: handle exception
				}
			}
		}

	}
	private void  sendUpdateCommentBroadcast(int aid, DynamicCommentItem dynamicCommentItem){
		Intent intent = new Intent(
				Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,3); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
		sendBroadcast(intent);
	}

	private void  sendUpdateSupportBroadcast(int aid, int cancel){
		Intent intent = new Intent(
				Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO);
		intent.putExtra(
				Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
		intent.putExtra(
				Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT,
				cancel);
		intent.putExtra(
				Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,
				3); // 3：TA的动态， 1：广场 2： 游戏圈
		sendBroadcast(intent);

	}
	@Override
	protected void initEvent() {

	}

}
