package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameCircleAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.GameDynamicDao;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.DynamicPic;
import com.cyou.mrd.pengyou.entity.GameCircleDynamic;
import com.cyou.mrd.pengyou.entity.GameDynamicInfoItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.ui.CYBaseActivity.ReConnectListener;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.CircleListView;
import com.cyou.mrd.pengyou.widget.CircleListView.LoadListener;
import com.cyou.mrd.pengyou.widget.CircleListView.RefreshListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GameCircleFragment extends RelationShipBaseFragment implements OnClickListener {

	private CYLog log = CYLog.getInstance();
	private static final int SHOW_COMMENT = 1;
	private static final int DELE_COMMENT = 2;
	private static final int SUPPORT = 9;
	private static final int CANCEL_SUPPORT = 10;
	private static final int GET_COMMENT = 5;
	protected static final int IS_CAN_SEND_DYMIC  = 6;
	protected static final int SHOW_GAME_NAME  = 8;

	private int mCurcor;
	private InputMethodManager mImm;
	private CircleListView mListView;
	private LinearLayout mMessageLL;
	private TextView mMessageTV;
	private LinearLayout mCommentLL;
	private EditText mCommentET;
	private Button mCommentBtn;
	private View mHeaderView;
	private TextView mEmptyView;

	private RefreshReceiver mRefreshReceiver;
	private boolean mHadRegistRefreshReceiver = false;
	private String gameCode;
	private GameDynamicDao mDao;
	private boolean mPublishingDynamic = false;
	private Handler mHandlerParent;
	private boolean isGameDetailPage = true;
	private String  mGamePkgString = "";
	private String  mGCid = "";
	private boolean refreshing = false;
	
	public GameCircleFragment() {
		super();
	}
	
	public GameCircleFragment(Activity activity){
		mActivity = (CYBaseActivity)activity;
	}

	public GameCircleFragment(Activity activity, String gameCode,
			boolean isGameDetailPage, String gcid, String pkg,Handler mhandler) {
		mActivity = (CYBaseActivity) activity;
		this.gameCode = gameCode;
		this.isGameDetailPage = isGameDetailPage;
		this.mGCid = gcid;
		this.mGamePkgString = pkg;
		this.mHandlerParent = mhandler;
	    isCanSendDynamic(0);
	}
	
	public GameCircleFragment(Activity activity, String gameCode,
			Handler mHandler) {
		mActivity = (CYBaseActivity) activity;
		this.gameCode = gameCode;
		this.mHandlerParent = mHandler;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (savedInstanceState != null) {
			mGCid = savedInstanceState.getString("gcid");
		}
		registReceiver();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString("gcid", mGCid);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.gamecircle_dynamic, null);
		mHeaderView = inflater.inflate(R.layout.game_circle_listview_headerview,null);
		mListView = (CircleListView) view.findViewById(R.id.game_circle_lv);
		mListView.setOnRefreshListener(new RefreshListener() {
			@Override
			public void onRefresh() {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.RELATION.BTN_RELATION_REFRESH_ID,
						CYSystemLogUtil.RELATION.BTN_RELATION_REFRESH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if(!TextUtils.isEmpty(mGCid) && !mPublishingDynamic){
					requestDynamic(true, mGCid);
				}
				else {
					mListView.onRefreshFinish();
					mListView.loadComplete();
				}
			}
		});
		mListView.setOnLoadListener(new LoadListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {}
			@Override
			public void onLoad() {
				mListView.onRefreshFinish();
				requestDynamic(false, mGCid);
			}
		});
		try {
			mListView.setOnTouchListener(new OnTouchListener() {			
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if(mListView.getChildCount() > 0){
						hideInputAndKeyboard(false);
					}
					return false;
				}
			});
		}catch (Exception e) {
			log.e(e);
		}
		mMessageLL = (LinearLayout) mHeaderView.findViewById(R.id.game_circle_message_ll);
		mMessageLL.setOnClickListener(this);
		mEmptyView = (TextView)view.findViewById(R.id.game_circle_empty);
		mMessageTV = (TextView) mHeaderView.findViewById(R.id.game_circle_message_tv);

		mCommentLL = (LinearLayout) view.findViewById(R.id.game_circle_comment_ll);
		mCommentET = (EditText) view.findViewById(R.id.game_circle_comment_et);
		mCommentBtn = (Button) view.findViewById(R.id.game_circle_comment_send_btn);
		mCommentBtn.setOnClickListener(this);
		if(!TextUtils.isEmpty(mGCid)){
			requestDynamic(true, mGCid);
		}

		return view;
	}

	private void checkMessage() {
		try {
			if (!TextUtils.isEmpty(mGamePkgString)) {
				int msgCount = SystemCountMsgItem
						.getGameCircleMsgCount(mGamePkgString);
				if (msgCount > 0) {
					mMessageTV.setText(mActivity.getString(R.string.new_msg, msgCount));
					mMessageLL.setVisibility(View.VISIBLE);
				} else {
					mMessageLL.setVisibility(View.GONE);
				}
			}
		} catch (Exception e) {
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		if (mDao == null) {
			mDao = new GameDynamicDao(mActivity);
		}
		if (mAdapter == null) {
			mAdapter = new GameCircleAdapter(mActivity, mData, mHandler);
			try {
				mListView.addHeaderView(mHeaderView);
			} catch (Exception e) {
			}
			mMessageLL.setVisibility(View.GONE);
			mListView.setAdapter(mAdapter);
		}
		if (mImm == null) {
			mImm = (InputMethodManager) mActivity
					.getSystemService(Context.INPUT_METHOD_SERVICE);
		}
	}

	public void registReceiver() {
		if(mRefreshReceiver == null){
			mRefreshReceiver = new RefreshReceiver();
		}
		if(!mHadRegistRefreshReceiver){
			//注册游戏圈动态发布后的消息
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_SUCCESS));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_FAIL));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_SENDING ));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_REPUBLISH));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAMEDYNAMIC_DELETE));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.REFRESH_GAME_CIRCLE));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAME_CIRCLE_SWITCH_TAB));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_GAME_CIRCLE_GCID));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_GAME_CIRCLE_MSG));
			mHadRegistRefreshReceiver = true;
		}
	}
	
	@Override
	public void onDestroy() {
		if(mHadRegistRefreshReceiver && mRefreshReceiver != null){
			mActivity.unregisterReceiver(mRefreshReceiver);
			mHadRegistRefreshReceiver = false;
			mRefreshReceiver = null;
		}
		super.onDestroy();
	}
	
	private void requestDynamic(final boolean refresh, final String gcid) {
		RequestParams params = new RequestParams();
		if (refresh) {
			mCurcor = 0;
		}
		refreshing = true;
		log.d("requestDynamic gid:" + gcid) ;
		params.put("gcid", gcid);
		params.put("cursor", String.valueOf(mCurcor));
		params.put("count", String.valueOf(Config.PAGE_SIZE_RELATION_CIRCLE));
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		mConn.post(HttpContants.NET.GAME_CIRCLE_GAMELIST, params,new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (refresh) {
							checkMessage();
						}
						boolean  emptyFailedDy = false;
						if (TextUtils.isEmpty(content)) {
							if(mListView != null){
								mListView.onRefreshFinish();
								mListView.loadingFinish();
								mListView.loadComplete();
							}
							if (refresh && mData.isEmpty()) {
								List<DynamicItem> dynamicList = getSendDynamicData();
								if (dynamicList != null && dynamicList.size() > 0 ){
									for (DynamicItem  dynamicItem : dynamicList) {
										dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
										mData.add(0, dynamicItem);
									}
									log.d("dynamicList.size"+dynamicList.size());
								}
								else{
									emptyFailedDy = true;
								}
								if(emptyFailedDy){
							         mEmptyView.setVisibility(View.VISIBLE);
							    }else {
							    	mEmptyView.setVisibility(View.GONE);
							    }
							    mAdapter.notifyDataSetChanged();
							}
							refreshing = false;
							return;
						}
						log.i("get game circle result = " + content);
						try {
							GameCircleDynamic dynamic = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(content, new TypeReference<GameCircleDynamic>() {});
							if(dynamic.getIsremarked() != null && !dynamic.getIsremarked().isEmpty()){
								UserInfoUtil.setGameScore(gameCode,Integer.valueOf(dynamic.getIsremarked()));
							}
							isCanSendDynamic(1);
							if(!isGameDetailPage && !TextUtils.isEmpty(dynamic.getGmname())){
								showGameName(dynamic.getGmname());
							}

							if (dynamic.getData() == null || dynamic.getData().isEmpty()) {
								if(mListView != null){
                                    mListView.onRefreshFinish();
									mListView.loadingFinish();
									mListView.loadComplete();
								}
								if (refresh && mData.isEmpty()) {
									List<DynamicItem> dynamicList = getSendDynamicData();
									if (dynamicList != null && dynamicList.size() > 0 ){
										for (DynamicItem  dynamicItem : dynamicList) {
											dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
											mData.add(0, dynamicItem);
										}
										log.d("dynamicList.size"+dynamicList.size());
									}
									else{
										emptyFailedDy = true;
									}
									if(emptyFailedDy){
								        mEmptyView.setVisibility(View.VISIBLE);
								    }else {
								    	mEmptyView.setVisibility(View.GONE);
								    }
								    mAdapter.notifyDataSetChanged();
								}
								refreshing = false;
								return;
							}
							if (refresh) {
								mListView.onRefreshFinish();
								mData.clear();
								boolean topItem = false;
								if(dynamic.getData().size() > 0 && dynamic.getData().get(0).getCursor() < 0){
									topItem = true;
								}
								List<DynamicItem> dynamicList = getSendDynamicData();
								if (dynamicList != null && dynamicList.size() > 0 ){
									for (DynamicItem  dynamicItem : dynamicList) {
										dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
										if(topItem){
											dynamic.getData().add(1, dynamicItem);
										}
										else{
											dynamic.getData().add(0, dynamicItem);
										}
									}
									log.d("dynamicList.size"+dynamicList.size());
								}
							}
							mCurcor = dynamic.getData().get(dynamic.getData().size() - 1).getCursor();
							mData.addAll(dynamic.getData());
							mAdapter.notifyDataSetChanged();
							if (dynamic.getData().size() < Config.PAGE_SIZE) {
								mListView.loadComplete();
							}
							refreshing = false;
						} catch (Exception e) {
							log.e(e);
							mListView.onRefreshFinish();
							mListView.loadComplete();
						}
						
						mListView.loadingFinish();

						if(mData.isEmpty()){
							mEmptyView.setVisibility(View.VISIBLE);
						}else{
							mEmptyView.setVisibility(View.GONE);
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						refreshing = false;
						if(mListView != null){
							mListView.onRefreshFinish();
							mListView.loadingFinish();
							mListView.hideFooterView();
						}
						super.onFailure(error, content);
						if(refresh){
							try {
								if(mActivity instanceof GameCircleActivity)
								    ((CYBaseActivity) mActivity).showNetErrorDialog(mActivity,new ReConnectListener() {								
									@Override
									public void onReconnect() {
										requestDynamic(true,mGCid);
									}
								});
							} catch (Exception e) {
							}
						}
					}
				});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_COMMENT:
				if(refreshing){
					showToastMessage(R.string.forbidden_comment, Toast.LENGTH_SHORT);
					return;
				}
				if (!mCommentLL.isShown()) {
					if(isGameDetailPage){
					    sendHideDownloadLayoutMsg(0);
					}
					mCommentLL.setVisibility(View.VISIBLE);
				}
				mCommentET.requestFocus();
				mCommentBtn.setTag(R.id.relationship_circle_empty,msg.arg1);
				mCommentBtn.setTag(msg.arg2);//点击评论时ListView的Item总量
				mCommentBtn.setTag(R.id.relation_circle_dot,msg.obj);
				try {
					if(msg.obj != null){
						DynamicCommentReplyItem reItem = (DynamicCommentReplyItem)msg.obj;
						mCommentET.setHint(mActivity.getString(R.string.reply)+reItem.getNickname()+":");
						mCommentBtn.setText(R.string.comment);
					}
					else {
					    mCommentET.setHint(mActivity.getString(R.string.comment));
					    mCommentBtn.setText(R.string.send);
					}
				} catch (Exception e) {
				}
				
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {
						mImm.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 100);
				
				break;
			case DELE_COMMENT:
				DynamicCommentItem commentItem = (DynamicCommentItem)msg.obj;
				int position = msg.arg1;
				int type = msg.arg2;
				deleteComment(commentItem, position,type);
				break;
			case SUPPORT:
				support(msg.arg2, msg.arg1 ,0, 2);
				break;
			case CANCEL_SUPPORT:
				support(msg.arg2, msg.arg1 ,1, 2);
				break;
			case GET_COMMENT:
				getComments(msg.arg2, msg.arg1, 2);
				break;
			default:
				break;
			}
		};
	};

//	private void sendComment(final String text, final int aid, final int position, final DynamicCommentReplyItem reItem, final int index) {
//		if(mSending){
//			Toast.makeText(mActivity, R.string.comment_sending_waiting, Toast.LENGTH_SHORT).show();
//			return;
//		}
//		if (mData.get(position) == null) {
//			return;
//		}
//		mData.get(position).setCommentcnt(mData.get(position).getCommentcnt() + 1);
//		final DynamicCommentItem  dyItem = new DynamicCommentItem();
//		dyItem.setUid(UserInfoUtil.getCurrentUserId());
//		dyItem.setNickname(UserInfoUtil.getCurrentUserNickname());
//		dyItem.setText(text);
//		dyItem.setComment(text);
//		dyItem.setReplyto(reItem);
//		dyItem.setSendSuccess(1);
//		dyItem.setCid(-1);
//		dyItem.setTimestamp(System.currentTimeMillis());
//		mData.get(position).getSubCommentData().add(0,dyItem);
//		mAdapter.notifyDataSetChanged();
//
//		RequestParams params = new RequestParams();
//		params.put("aid", String.valueOf(aid));
//		params.put("text", text);
//		if(mData.get(position).getGame() != null)
//		    params.put("gamecode", mData.get(position).getGame().getGamecode());
//		if(reItem != null){
//			params.put("reuid", Integer.valueOf(reItem.getUid()).toString());
//		}
//		mConn.post(HttpContants.NET.SEND_DYNAMIC_COMMENT, params,
//				new AsyncHttpResponseHandler() {
//					@Override
//					public void onStart() {
//						super.onStart();
//						mSending = true;
//					}
//					@Override
//					public void onLoginOut() {
//						LoginOutDialog dialog = new LoginOutDialog(mActivity);
//						dialog.create().show();
//					}
//					@Override
//					public void onFailure(Throwable error, String content) {
//						super.onFailure(error, content);
//						mSending = false;
//						try {
//							mAdapter.relCache.saveFailedComment(dyItem,mData.get(position).getAid());
//							Config.needResendComment = true;
//						} catch (Exception e) {
//							log.e(e);
//						}	
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						super.onSuccess(statusCode, content);
//						mSending = false;
//						if (TextUtils.isEmpty(content)) {
//							return;
//						}
//						log.i("send comment result = " + content);
//						try {
//							JSONObject obj = new JSONObject(content);
//							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
//								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
//									String cid = JsonUtils.getJsonValue(
//		    								JsonUtils.getJsonValue(content, "data"),
//		    								"cid");
//									DynamicCommentItem  dyItem2 = new DynamicCommentItem();
//									dyItem2.setUid(UserInfoUtil.getCurrentUserId());
//									dyItem2.setNickname(UserInfoUtil.getCurrentUserNickname());
//									dyItem2.setComment(text);
//	                                dyItem2.setText(text);
//									dyItem2.setReplyto(reItem);
//									dyItem2.setSendSuccess(0);
//									dyItem2.setCid(Integer.parseInt(cid));
//									dyItem2.setTimestamp(System.currentTimeMillis());
//									int index = mData.get(position).getSubCommentData().indexOf(dyItem);
//									if(index >=0 ){
//										mData.get(position).getSubCommentData().remove(index);
//										mData.get(position).getSubCommentData().add(index,dyItem2);
//										mAdapter.notifyDataSetChanged();
//										Intent intent = new Intent(
//												Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
//										intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,0); //0:评论 1：删除评论
//										intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,2); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态, 4:关系圈
//										intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
//										intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dyItem2);
//										mActivity.sendBroadcast(intent);
//									}
//								}
//								else {
//									String errorNo = JsonUtils.getJsonValue(content,
//	        								"errorNo");
//	        						if (!TextUtils.isEmpty(errorNo))
//	        						{
//	        							if(errorNo.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)){
//	        								showToastMessage(getResources().getString(R.string.comment_maskword_failed), Toast.LENGTH_SHORT);
//	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)){
//	        								showToastMessage(getResources().getString(R.string.comment_deleted_send_failed), Toast.LENGTH_SHORT);
//	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)){
//	        								showToastMessage(getResources().getString(R.string.comment_operation_send_failed), Toast.LENGTH_SHORT);
//	        							}else {
//	        								showToastMessage(getResources().getString(R.string.comment_send_failed), Toast.LENGTH_SHORT);
//	        							}
//	        						}
//								}
//							}
//						} catch (Exception e) {
//							log.e(e);
//						}
//						mSending = false;
//					}
//				});
//	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.game_circle_comment_send_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_PUBLISH_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_PUBLISH_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			
			int nowDataCount = mAdapter.getCount(); //发表评论时ListView的Item总量
			int pastDataCount = (Integer)v.getTag();//点击评论时ListView的Item总量
			Integer position = null ;
			if(nowDataCount==pastDataCount){
				 position = Integer.valueOf(v.getTag(R.id.relationship_circle_empty).toString());
			}else if(nowDataCount>pastDataCount){
				 position = Integer.valueOf(v.getTag(R.id.relationship_circle_empty).toString())+(nowDataCount-pastDataCount);
			}else if(nowDataCount<pastDataCount){
				 position = Integer.valueOf(v.getTag(R.id.relationship_circle_empty).toString())-( pastDataCount-nowDataCount);
			}
			
			DynamicCommentReplyItem reItem = (DynamicCommentReplyItem)v.getTag(R.id.relation_circle_dot);
			String content = mCommentET.getText().toString();
			if (TextUtils.isEmpty(content.replaceAll("\\s*", ""))) {
				showToastMessage(R.string.input_empty, Toast.LENGTH_SHORT);
				return;
			}
			hideInputAndKeyboard(true);
			sendComment(content, mData.get(position).getAid(), position, reItem, 2);
			break;
		case R.id.game_circle_message_ll:
			mMessageLL.setVisibility(View.GONE);
			SystemCountMsgItem.changeGameCircleMsg(true,mActivity,mGamePkgString,0);
			i.setClass(mActivity,MessageCenterActivity.class);
			i.putExtra(Params.FROM, 1);
			i.putExtra(Params.GCID, mGCid);
			mActivity.startActivity(i);
			break;
		case R.id.game_circle_message_avatar_iv:
			int uid = Integer.parseInt(v.getTag().toString());
			i.setClass(mActivity, FriendInfoActivity.class);
			i.putExtra(Params.FRIEND_INFO.UID, uid);
			mActivity.startActivity(i);
			break;
		default:
			break;
		}
	}

	private List<DynamicItem> getSendDynamicData () {
		if (mDao == null) {
			mDao = new GameDynamicDao(mActivity);
		}
		List<GameDynamicInfoItem> list = mDao.getAllDynamic(gameCode,UserInfoUtil.getCurrentUserId());
		log.d("list.size="+list.size());
		String avatarString =  UserInfoUtil.getCurrentUserPicture();
		String nickNameString =UserInfoUtil.getCurrentUserNickname();
		int gender = UserInfoUtil.getCurrentUserGender();
		int uid = UserInfoUtil.getCurrentUserId();
		List<DynamicItem> dynamicList = new ArrayList<DynamicItem>();
		if (list != null && list.size() > 0) {
			for(GameDynamicInfoItem item : list){
				DynamicItem dynamicItem = new DynamicItem();
				dynamicItem.setPid(item.getPid());
				dynamicItem.setText(item.getContent());
				dynamicItem.setMyPicture(item.getPicture());
				dynamicItem.setCreatedtime(item.getDate());
				dynamicItem.setSendStatus(item.getStatus());
				dynamicItem.setType(Contants.DYNAMIC_TYPE.MY_DYNAMIC);
				dynamicItem.setAvatar(avatarString);
				dynamicItem.setNickname(nickNameString);
				dynamicItem.setGender(gender);
				dynamicItem.setUid(uid);
				dynamicItem.setStar(String.valueOf(item.getScore()));
				DynamicPic  picInf = new DynamicPic();
				picInf.setPath(item.getPicture());
				picInf.setHeight(item.getHeight());
				picInf.setWidth(item.getWidth());
				dynamicItem.setPicture(picInf);
				dynamicList.add(dynamicItem);
			}
		}
		return dynamicList;
	}
	
	/**
	 * 接到广播后刷新数据
	 * @author xumengyang
	 *
	 */
	private class RefreshReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			if (Contants.ACTION.SEND_GAMEDYNAMIC_SENDING.equals(intent.getAction())) {
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, -1);
				GameDynamicInfoItem item = null;
				if (pid != null && pid > -1) {
					item = mDao.getDynamicByPid(pid);
				}
				if (item != null && item.getPid() != null && item.getPid().intValue() > -1) {
					DynamicItem dynamicItem = new DynamicItem();
					dynamicItem.setPid(item.getPid());
					dynamicItem.setText(item.getContent());
					dynamicItem.setMyPicture(item.getPicture());
					dynamicItem.setCreatedtime(item.getDate());
					dynamicItem.setSendStatus(item.getStatus());
					dynamicItem.setType(Contants.DYNAMIC_TYPE.MY_DYNAMIC);
					dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
					dynamicItem.setAvatar(UserInfoUtil.getCurrentUserPicture());
					dynamicItem.setNickname(UserInfoUtil.getCurrentUserNickname());
					dynamicItem.setGender(UserInfoUtil.getCurrentUserGender());
					dynamicItem.setStar((String.valueOf(item.getScore())));
					dynamicItem.setUid(UserInfoUtil.getCurrentUserId());
					DynamicPic  picInf = new DynamicPic();
					picInf.setPath(item.getPicture());
					picInf.setHeight(item.getHeight());
					picInf.setWidth(item.getWidth());
					dynamicItem.setPicture(picInf);
					if(item.getScore()!= 0){
	                	UserInfoUtil.setGameScore(gameCode,1);
	                }
					if(mData.size() > 0 && mData.get(0).getCursor() < 0){
						mData.add(1,dynamicItem);
					}
					else
					    mData.add(0,dynamicItem);
					if(mEmptyView.getVisibility() == View.VISIBLE){
						mEmptyView.setVisibility(View.GONE);
					}
					mPublishingDynamic = true;
					mAdapter.notifyDataSetChanged();
					if (mListView != null) {
						mListView.setSelection(0);
					}
				}
				
			}else if (Contants.ACTION.SEND_GAMEDYNAMIC_FAIL.equals(intent.getAction())) {
				mPublishingDynamic = false;
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				if (pid != null && pid > -1) {
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
							if (pid == dynamicItem.getPid()) {
								dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
								break;
							}
						}
						mAdapter.notifyDataSetChanged();
					}
				}
				String  gameScoreString  =  intent.getStringExtra(Params.INTRO.GAME_SCORE);
				if(!gameScoreString.isEmpty() && !gameScoreString.equals("0.0") && !gameScoreString.equals("0")){
                	UserInfoUtil.setGameScore(gameCode,0);
                }
				if(intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_FAIL_TYPE,-1) == 4){
					showToastMessage(R.string.publish_maskword_failed, Toast.LENGTH_SHORT);
				}
			}else if (Contants.ACTION.SEND_GAMEDYNAMIC_SUCCESS.equals(intent.getAction())) {
				log.i("GameCircleFragment SEND_GAMEDYNAMIC_SUCCESS");
				mPublishingDynamic = false;
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				Integer aid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,-1);
				if (pid != null && pid > -1) {
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
							if (pid != null) {
								if (pid == dynamicItem.getPid()) {
									dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.SUCCESS);
									dynamicItem.setAid(aid);
									mAdapter.notifyDataSetChanged();
									break;
								}
							}
						}
					}
				}
				String  gameScoreString  =  intent.getStringExtra(Params.INTRO.GAME_SCORE);
				if(!gameScoreString.isEmpty() && !gameScoreString.equals("0.0") && !gameScoreString.equals("0")){
                	UserInfoUtil.setGameScore(gameCode,1);
                }
				
				if(isGameDetailPage){
				    updateGameCircleDyanmicCount();
				}
				try {
					Intent it = new Intent(Contants.ACTION.UPDATE_GAME_DYNAMIC_COUNT);
					it.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, gameCode);
					log.i("UPDATE_GAME_DYNAMIC_COUNT gameCode: " + gameCode);
					mActivity.sendBroadcast(it);
				} catch (Exception e) {
					log.e(e);
				}
			}else if (Contants.ACTION.SEND_GAMEDYNAMIC_REPUBLISH.equals(intent.getAction())) {
				Integer pidStr = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				if (pidStr != null && pidStr > -1) {
					Integer pid = Integer.valueOf(pidStr);
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
							if (pid == dynamicItem.getPid()) {
								dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.SENDING);
								break;
							}
						}
						mPublishingDynamic = true;
						mAdapter.notifyDataSetChanged();
					}
				}
			}else if (Contants.ACTION.SEND_GAMEDYNAMIC_DELETE.equals(intent.getAction())) {
				Integer pidStr = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				if (pidStr != null && pidStr > -1) {
					Integer pid = Integer.valueOf(pidStr);
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
							if (pid == dynamicItem.getPid()) {
								mData.remove(dynamicItem);
								break;
							}
						}
						mAdapter.notifyDataSetChanged();
					}
				}
			}
			else if (Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO.equals(intent.getAction())){
				try {
					int aid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					log.i(" GameCircleFragment UPDATE_RELATION_SUPPORT_INFO aid: " + aid);
					int cancel = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT, 0);
					int pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0);
					if( pid == 2){
						//赞同步来自游戏圈 自己不必更新自己
						return;
					}
					log.i(" GameCircleFragment UPDATE_RELATION_SUPPORT_INFO pid: " + aid);
					if (mData != null && mData.size() > 0) {
						boolean needNotify = false;
						for (DynamicItem dynamicItem : mData) {
							if (aid == dynamicItem.getAid()) {
								int index = mData.indexOf(dynamicItem);
								log.i(" GameCircleFragment UPDATE_RELATION_SUPPORT_INFO index: " + index);
								if(cancel == 0){
									 mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() + 1);
									 mData.get(index).setSupported(Contants.SUPPORT.YES);
								}
								else{								
									 mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() - 1);
									 mData.get(index).setSupported(Contants.SUPPORT.NO);
								}
								needNotify = true;
								break;
							}
						}
						if(needNotify)						
						    mAdapter.notifyDataSetChanged();
					}				
				} catch (Exception e) {
					log.e(e);
				}			
			}
			else if (Contants.ACTION.REFRESH_GAME_CIRCLE.equals(intent.getAction())){
				requestDynamic(true, mGCid);
			}
			else if (Contants.ACTION.SEND_GAME_CIRCLE_SWITCH_TAB.equals(intent.getAction())){
				if(mCommentLL != null && mCommentLL.isShown()){
		    		sendHideDownloadLayoutMsg(0);	
		        }
			}
			else if (Contants.ACTION.SEND_GAME_CIRCLE_GCID.equals(intent.getAction())){
				try {
					if(!TextUtils.isEmpty(mGCid)){
						return;
					}
					mGCid = intent.getStringExtra("gcid");
					mGamePkgString = intent.getStringExtra("pkname");
					if (mListView != null) {
						requestDynamic(true, mGCid);
					}
				} catch (Exception e) {
				}
			}
			else if (Contants.ACTION.UPDATE_GAME_CIRCLE_MSG.equals(intent.getAction())){
				checkMessage();
			}
			else if(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO.equals(intent.getAction())){
				try {
					int source =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0);
					if(source == 2){
						return;
					}
					int type =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE, 0);			
					int aid =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					DynamicCommentItem mCItem = (DynamicCommentItem)intent.getSerializableExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT);
					if(mData!= null && mData.size() > 0){
						int index= 0;
						boolean needed = false;
						for(DynamicItem item: mData){
							if(item.getAid() == aid){
								index =  mData.indexOf(item);
								needed = true;
								break;
							}
						}
						if(needed){
							if(type == 0){
								if(mData.get(index).getSubCommentData() != null){
									for(DynamicCommentItem mItem : mData.get(index).getSubCommentData()){
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
									mData.get(index).setCommentcnt(mData.get(index).getCommentcnt() + 1);
								    mData.get(index).getSubCommentData().add(0, mCItem);
								    mAdapter.notifyDataSetChanged();
								}
							}
							else {
								int cIndex = 0;
								if(mData.get(index).getSubCommentData() != null){
									for(DynamicCommentItem mItem : mData.get(index).getSubCommentData()){
										if(mItem.getCid() == mCItem.getCid()){
											cIndex = mData.get(index).getSubCommentData().indexOf(mItem);
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
									mData.get(index).setCommentcnt(mData.get(index).getCommentcnt() - 1);
									mData.get(index).getSubCommentData().remove(cIndex);
								    mAdapter.notifyDataSetChanged();
								}
							}
						}
					}
				} catch (Exception e) {
				}
			}
		}
		
	}
	
	private void  hideInputAndKeyboard(boolean clear){
		View v = mActivity.getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				if(mImm != null && mImm.isActive()){
					mImm.hideSoftInputFromWindow(
							binder,
							InputMethodManager.HIDE_NOT_ALWAYS);
					if(isGameDetailPage)
					   sendHideDownloadLayoutMsg(1);
				}
				
			}
		}
		// 隐藏评论输入框和输入法
		if (mCommentLL.isShown()) {
			mCommentET.setText("");
			mCommentLL.setVisibility(View.GONE);
		}
	}


	//隐藏/显示下载框的消息
	private void sendHideDownloadLayoutMsg(int show){
		try {
			Message msgParent = mHandlerParent.obtainMessage();
			msgParent.what = 2;
			msgParent.arg1 = show;
			mHandlerParent.sendMessage(msgParent);
		} catch (Exception e) {
		}
	}

	//刷新游戏圈动态数目
	private void updateGameCircleDyanmicCount(){
		try {
			Message msg = mHandlerParent.obtainMessage();
			msg.what = 3;
			mHandlerParent.sendMessage(msg);
		} catch (Exception e) {
		}
	}
	
	//是否可点击发送动态
	private void isCanSendDynamic(int can){
		try {
			Message msg = mHandlerParent.obtainMessage();
			msg.arg1 = can;
			msg.what = IS_CAN_SEND_DYMIC;
			mHandlerParent.sendMessage(msg);
		} catch (Exception e) {
		}
	}

	//更新游戏名称
	private void showGameName(String name){
		try {
			Message msg = mHandlerParent.obtainMessage();
			msg.obj = name;
			msg.what = SHOW_GAME_NAME;
			mHandlerParent.sendMessage(msg);
		} catch (Exception e) {
		}
	}

}
