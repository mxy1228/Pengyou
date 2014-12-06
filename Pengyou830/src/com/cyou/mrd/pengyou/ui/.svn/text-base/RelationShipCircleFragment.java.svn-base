package com.cyou.mrd.pengyou.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RelationshipCircleAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DynamicDao;
import com.cyou.mrd.pengyou.entity.Dynamic;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicInfoItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.DynamicPic;
import com.cyou.mrd.pengyou.entity.DynamicSupporterItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.CYImageLoader;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.CircleListView;
import com.cyou.mrd.pengyou.widget.CircleListView.LoadListener;
import com.cyou.mrd.pengyou.widget.CircleListView.RefreshListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.RelationshipRoundImageView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.ImageLoader;

public class RelationShipCircleFragment extends RelationShipBaseFragment implements OnClickListener {

	private static final int SHOW_COMMENT = 1;
	private static final int DELE_COMMENT = 2;
	private static final int SUPPORT = 3;
	private static final int CANCEL_SUPPORT = 4;
	private static final int GET_COMMENT = 5;
	private int mCurcor;

	private CircleListView mListView;
	private LinearLayout mMessageLL;
	private RelationshipRoundImageView mMessageAvatarIV;
	private TextView mMessageTV;
	private View mHeaderView;
	private TextView mEmptyView;

	private RefreshReceiver mRefreshReceiver;
	private boolean mHadRegistRefreshReceiver = false;
	private boolean isCacheData = false;
	private DynamicDao mDao;
    private boolean refreshing = false;
    private SystemPullReceiver mSystemPullReceiver;
    private boolean mHadRegistSystemReceiver = false;
    private boolean mPublishingDynamic = false;
    
	private int pos;
	private int count;
    
	public RelationShipCircleFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		registReceiver();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.relation_dynamic, null);
		mHeaderView = inflater.inflate(R.layout.relation_circle_headerview,null);
		mListView = (CircleListView) view.findViewById(R.id.relationship_circle_lv);
		mListView.setOnRefreshListener(new RefreshListener() {
			@Override
			public void onRefresh() {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.RELATION.BTN_RELATION_REFRESH_ID,
						CYSystemLogUtil.RELATION.BTN_RELATION_REFRESH_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				if(Util.isNetworkConnected(mActivity)){
					if(!mPublishingDynamic){
						ImageLoader.getInstance().clearMemoryCache();
						requestDynamic(true);
					}else {
						mListView.onRefreshFinish();
						mListView.loadComplete();
					}
				}
				else{
					showToastMessage(R.string.check_network, Toast.LENGTH_SHORT);
					getCacheData();
					mListView.onRefreshFinish();
					mListView.loadComplete();
					
				}

				SystemCountMsgItem.cleanLastEachFocusDynmiac(UserInfoUtil.getCurrentUserId());
				Util.requestFansAndLetterCount(mActivity,SystemCountMsgItem.SYSTEM);
			}
		});
		mListView.setOnLoadListener(new LoadListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount){}
			@Override
			public void onLoad() {
				mListView.onRefreshFinish();
				requestDynamic(false);
			}
		});

		mMessageLL = (LinearLayout) mHeaderView.findViewById(R.id.relationship_circle_message_ll);
		mMessageLL.setOnClickListener(this);
		mEmptyView = (TextView)view.findViewById(R.id.relationship_circle_empty);
		mMessageAvatarIV = (RelationshipRoundImageView) mHeaderView.findViewById(R.id.relationship_circle_message_avatar_iv);
		mMessageAvatarIV.setOnClickListener(this);
		mMessageTV = (TextView) mHeaderView.findViewById(R.id.relationship_circle_message_tv);
		checkMessage();
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mDao == null) {
			mDao = new DynamicDao(mActivity);
		}
		if (mAdapter == null) {
			mAdapter = new RelationshipCircleAdapter(mActivity, mData, mHandler);
			try {
				mListView.addHeaderView(mHeaderView);
			} catch (Exception e) {
			}
			mMessageLL.setVisibility(View.GONE);
			mListView.setAdapter(mAdapter);
			getCacheData();
			requestDynamic(true);
		}
	}
	
	public void registReceiver(){
		if(mRefreshReceiver == null){
			mRefreshReceiver = new RefreshReceiver();
		}
		if(mSystemPullReceiver == null){
			mSystemPullReceiver = new SystemPullReceiver();
		}
		if(!mHadRegistRefreshReceiver){
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.REFRESH_RELATION_CIRCLE));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.TOP_RELATION_CIRCLE));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_DYNAMIC_FAIL));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_DYNAMIC_SUCCESS));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_DYNAMIC_SENDING));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.RELATION_SEND_FAILED_COMMENT));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_DYNAMIC_REPUBLISH));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.SEND_DYNAMIC_DELETE));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO));
			mActivity.registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO));
			mHadRegistRefreshReceiver = true;
		}
		if(!mHadRegistSystemReceiver){
			mActivity.registerReceiver(mSystemPullReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_MSG_DOT_ACTION));
			mHadRegistSystemReceiver = true;
		}
	}
	
	@Override
	public void onDestroy() {
		if(mHadRegistRefreshReceiver && mRefreshReceiver != null){
			mActivity.unregisterReceiver(mRefreshReceiver);
			mHadRegistRefreshReceiver = false;
			mRefreshReceiver = null;
		}
		if(mHadRegistSystemReceiver && mSystemPullReceiver != null){
			mActivity.unregisterReceiver(mSystemPullReceiver);
			mHadRegistSystemReceiver = false;
			mSystemPullReceiver = null;
		}
		super.onDestroy();
	}
	
	private void requestDynamic(final boolean refresh) {
		
		RequestParams params = new RequestParams();
		if (refresh) {
			mCurcor = 0;
		}
	    refreshing = true;
		params.put("cursor", String.valueOf(mCurcor));
		params.put("count", String.valueOf(Config.PAGE_SIZE_RELATION_CIRCLE));
		log.i("relation ship  requestDynamic mCurcor:" + mCurcor);
		mConn.post(HttpContants.NET.RELATION_CIRCLE, params,
				new AsyncHttpResponseHandler() {
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
						if (TextUtils.isEmpty(content)) {
							if(mListView != null){
								 mListView.onRefreshFinish();
								 mListView.loadingFinish();
								 mListView.loadComplete();
							}
							if(refresh && !isCacheData && mData.isEmpty()){
								mEmptyView.setVisibility(View.VISIBLE);
							}
							else {
								mEmptyView.setVisibility(View.GONE);
							}
							refreshing = false;
							return;
						}
						log.i("get relationship circle result = " + content);
						try {
							Dynamic dynamic = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(content, new TypeReference<Dynamic>() {});
							if (dynamic.getData() == null|| dynamic.getData().isEmpty()) {
								if(mListView != null){
									 mListView.onRefreshFinish();
									 mListView.loadingFinish();
									 mListView.loadComplete();
								}
								if(refresh && !isCacheData && mData.isEmpty()){
									mEmptyView.setVisibility(View.VISIBLE);
								}
								else{
									mEmptyView.setVisibility(View.GONE);
								}
								refreshing = false;
								return;
							}
							if (refresh) {
								mData.clear();
								//从数据库查询还未发或者没有发成功的动态
								List<DynamicItem> dynamicList = getSendDynamicData();
								if (dynamicList != null && dynamicList.size() > 0 ){
									for (DynamicItem  dynamicItem : dynamicList) {
										dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
										mData.add(0, dynamicItem);
									}
									log.d("dynamicList.size"+dynamicList.size());
								}
							}
							if (refresh) {
								mListView.onRefreshFinish();
							}
							mCurcor = dynamic.getData().get(dynamic.getData().size() - 1).getCursor();
							mData.addAll(dynamic.getData());
							mAdapter.notifyDataSetChanged();
							if (dynamic.getData().size() < Config.PAGE_SIZE) {
								mListView.loadComplete();
							}
							SystemCountMsgItem.cleanLastEachFocusDynmiac(UserInfoUtil.getCurrentUserId());
							Util.requestFansAndLetterCount(mActivity,SystemCountMsgItem.SYSTEM);
							SharedPreferenceUtil.saveRelationData(mActivity, content);
						} catch (Exception e) {
							log.e(e);
							if(mListView != null){
								   mListView.onRefreshFinish();
								   mListView.loadingFinish();
								   mListView.loadComplete();
							}
						}
						mListView.loadingFinish();
						
						if(mData.isEmpty()){
							mEmptyView.setVisibility(View.VISIBLE);
						}else{
							mEmptyView.setVisibility(View.GONE);
						}
						refreshing = false;
						
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
				Intent startComment = new Intent(getActivity(),RelationCommentActivity.class);
				pos = msg.arg1;
				count = msg.arg2;
				startComment.putExtra("obj", (Serializable)msg.obj);
				startComment.putExtra("src",Contants.RELATION_CONTANTS.REL_CIRCLE_STARTCOMMENT_SRC );
				startActivityForResult(startComment, Contants.RELATION_CONTANTS.REL_CIRCLE_REQUEST_COMMENT_CODE);
				break;
			case DELE_COMMENT:
				DynamicCommentItem commentItem = (DynamicCommentItem)msg.obj;
				int position = msg.arg1;
				int type = msg.arg2;
				deleteComment(commentItem, position,type);
				break;
			case SUPPORT:
				support(msg.arg2, msg.arg1 ,0,0);
				break;
			case CANCEL_SUPPORT:
				support(msg.arg2, msg.arg1 ,1,0);
				break;
			case GET_COMMENT:
				getComments(msg.arg2, msg.arg1, 0);
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		 if(resultCode==Contants.RELATION_CONTANTS.REL_CIRCLE_BACK_COMMENT_CODE){
			String commentStr = data.getStringExtra("COMMENT");
			Serializable obj = data.getSerializableExtra("obj");
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_PUBLISH_ID,
					CYSystemLogUtil.RELATION.BTN_RELATION_COMMENT_PUBLISH_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			int nowDataCount = mAdapter.getCount(); //发表评论时ListView的Item总量
			Integer position = null ;
			if(nowDataCount == count){
				 position = Integer.valueOf(pos);
			}else if(nowDataCount>count){
				 position = Integer.valueOf(pos+(nowDataCount-count));
			}else if(nowDataCount<count){
				 position = Integer.valueOf(pos-( count-nowDataCount));
			}
			DynamicCommentReplyItem reItem = (DynamicCommentReplyItem)obj;
			sendComment(commentStr, mData.get(position).getAid(), position, reItem, 4);
		 }
		super.onActivityResult(requestCode, resultCode, data);
	}
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.relationship_circle_message_ll:
			i.setClass(mActivity,MessageCenterActivity.class);
			i.putExtra(Params.FROM, 1);
			mActivity.startActivity(i);
			break;
		case R.id.relationship_circle_message_avatar_iv:
			int uid = Integer.parseInt(v.getTag().toString());
			i.setClass(mActivity, FriendInfoActivity.class);
			i.putExtra(Params.FRIEND_INFO.UID, uid);
			mActivity.startActivity(i);
			break;
		default:
			break;
		}
	}

	private void checkMessage(){
		SystemCountMsgItem item = SystemCountMsgItem.get();
		int lastUID = item.getmLastCommentUID();
		String avatar = item.getmAvatar();
		int newCommentCount = item.getmUnreadCommentsCount();
		if(lastUID > 0 && newCommentCount >0){
			if(TextUtils.isEmpty(avatar)){
				mMessageAvatarIV.getImageView().setImageResource(R.drawable.avatar_defaul);
			}else{
				CYImageLoader.displayImg(avatar, mMessageAvatarIV.getImageView(), mOption);
			}
			mMessageAvatarIV.setTag(lastUID);
			mMessageTV.setText(mActivity.getString(R.string.new_msg, newCommentCount));
			mMessageLL.setVisibility(View.VISIBLE);
		}
		else {
			mMessageLL.setVisibility(View.GONE);
		}		
	}
	
	//获取所有未发布成功的动态
	private List<DynamicItem> getSendDynamicData () {
		if (mDao == null) {
			mDao = new DynamicDao(mActivity);
		}
		List<DynamicInfoItem> list = mDao.getAllDynamic(UserInfoUtil.getCurrentUserId());
		log.d("list.size="+list.size());
		List<DynamicItem> dynamicList = new ArrayList<DynamicItem>();
		if (list != null && list.size() > 0) {
			for(DynamicInfoItem item : list){
				DynamicItem dynamicItem = new DynamicItem();
				dynamicItem.setPid(item.getPid());
				dynamicItem.setText(item.getContent());
				dynamicItem.setMyPicture(item.getPicture());
				dynamicItem.setCreatedtime(item.getDate());
				dynamicItem.setSendStatus(item.getStatus());
				dynamicItem.setType(Contants.DYNAMIC_TYPE.MY_DYNAMIC);
				dynamicItem.setAvatar(UserInfoUtil.getCurrentUserPicture());
				dynamicItem.setNickname(UserInfoUtil.getCurrentUserNickname());
				dynamicItem.setGender(UserInfoUtil.getCurrentUserGender());
				dynamicItem.setUid(UserInfoUtil.getCurrentUserId());
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
			if (Contants.ACTION.SEND_DYNAMIC_SENDING.equals(intent.getAction())) {
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, -1);
				DynamicInfoItem item = null;
				if (pid != null && pid > -1) {
					item = mDao.getDynamicByPid(pid,UserInfoUtil.getCurrentUserId());
				}
				if (item != null && item.getPid() != null && item.getPid() > -1) {
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
					dynamicItem.setUid(item.getUid());
					DynamicPic  picInf = new DynamicPic();
					picInf.setPath(item.getPicture());
					picInf.setHeight(item.getHeight());
					picInf.setWidth(item.getWidth());
					dynamicItem.setPicture(picInf);
					mData.add(0,dynamicItem);
					if(mEmptyView.getVisibility() == View.VISIBLE){
						mEmptyView.setVisibility(View.GONE);
					}
					mPublishingDynamic = true;
					mAdapter.notifyDataSetChanged();
				}
				if (mListView != null) {
					mListView.setSelection(0);
				}
				
			}else if (Contants.ACTION.SEND_DYNAMIC_FAIL.equals(intent.getAction())) {
				mPublishingDynamic = false;
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				if (pid != null && pid > -1) {
					if (mData != null && mData.size() > 0) {
							for (DynamicItem dynamicItem : mData) {
								if (pid == dynamicItem.getPid()) {
									dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.FAIL);
									mAdapter.notifyDataSetChanged();
									break;
								}
							}
					}
				}
				if(intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_FAIL_TYPE,-1) == 4){
					showToastMessage(R.string.publish_maskword_failed, Toast.LENGTH_SHORT);
				}
			}else if (Contants.ACTION.SEND_DYNAMIC_SUCCESS.equals(intent.getAction())) {
				mPublishingDynamic = false;
				Integer pid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				Integer aid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,-1);
				if (pid != null && pid > -1) {
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
								if (pid == dynamicItem.getPid()) {
									dynamicItem.setSendStatus(Contants.SEND_DYNAMIC_STATUS.SUCCESS);
									dynamicItem.setAid(aid);
									mAdapter.notifyDataSetChanged();
									break;
								}
						}
					}
				}
			}else if (Contants.ACTION.SEND_DYNAMIC_REPUBLISH.equals(intent.getAction())) {
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
			}else if (Contants.ACTION.SEND_DYNAMIC_DELETE.equals(intent.getAction())) {
				Integer pidStr = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,-1);
				if (pidStr != null && pidStr > -1) {
					Integer pid = Integer.valueOf(pidStr);
					if (mData != null && mData.size() > 0) {
						for (DynamicItem dynamicItem : mData) {
							if (pid == dynamicItem.getPid()) {
								mData.remove(dynamicItem);
								mAdapter.notifyDataSetChanged();
								break;
							}
						}
					}
				}
			}
			else if (Contants.ACTION.REFRESH_RELATION_CIRCLE.equals(intent.getAction())){	        
		        if(intent.getBooleanExtra("force", false)){
					if(mListView != null)
					     mListView.setSelection(0);
					requestDynamic(true);
				}
				else {
					requestHandler.removeMessages(1);
				    Message msg = requestHandler.obtainMessage();
				    msg.what = 1;
			        requestHandler.sendMessageDelayed(msg, 1000);
				}
			}
			else if (Contants.ACTION.TOP_RELATION_CIRCLE.equals(intent.getAction())){
				if(mData != null && !mData.isEmpty()&& mListView != null){
					mListView.setSelection(0);
					if(newDynamic()){
						requestDynamic(true); 
					}
				}
			}
			else if (Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO.equals(intent.getAction())){
				try {
					int aid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					log.i("RelationShipCircleFragement UPDATE_RELATION_SUPPORT_INFO aid: " + aid);
					int cancel = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT, 0);
					if (mData != null && mData.size() > 0) {
						boolean needNotify = false;
						for (DynamicItem dynamicItem : mData) {
							if (aid == dynamicItem.getAid()) {
								 int needAdd = 0;
								 DynamicSupporterItem supporter =  new DynamicSupporterItem();
								 supporter.setNickname(UserInfoUtil.getCurrentUserNickname());
								 supporter.setUid(UserInfoUtil.getCurrentUserId());
								 if(cancel == 0){
									    if(dynamicItem.getSupportusr().size() == 0){
									    	needAdd = 1;
									    }
									    for(int i =0 ; i< dynamicItem.getSupportusr().size(); i++){
											 if(dynamicItem.getSupportusr().get(i).getUid() == UserInfoUtil.getCurrentUserId()){
												 needAdd = 0;
												 break;
											 }
											 needAdd = 1;
										}
									    if(needAdd == 1){
									    	  ArrayList<DynamicSupporterItem> 
									    	  list = (ArrayList<DynamicSupporterItem>)dynamicItem.getSupportusr();
									    	  list.add(supporter);
									    	  int index = mData.indexOf(dynamicItem);
											  mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() + 1);
											  mData.get(index).setSupported(Contants.SUPPORT.YES);
									    	  mData.get(index).setSupportusr(list);
									    	  log.i("RelationShipCircleFragement UPDATE_RELATION_SUPPORT_INFO need add index: " + index);
									    	  needNotify = true;
									    }
								}
								else{								
									 for(int i =0 ; i< dynamicItem.getSupportusr().size(); i++){
										 if(dynamicItem.getSupportusr().get(i).getUid() == UserInfoUtil.getCurrentUserId()){
											 int index = mData.indexOf(dynamicItem);
											 log.i("RelationShipCircleFragement UPDATE_RELATION_SUPPORT_INFO need remove index: " + index);
											 mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() - 1);
											 mData.get(index).setSupported(Contants.SUPPORT.NO);
									    	 mData.get(index).getSupportusr().remove(i);
											 needAdd =1;
											 ArrayList<DynamicSupporterItem> 
									    	 list = (ArrayList<DynamicSupporterItem>)mData.get(index).getSupportusr();
											 mData.get(index).setSupportusr(list);
											 break;
										 }
									 }
									 if(1 == needAdd){
									    needNotify = true;
									 }
								}
								break;
							}
						}
						if(needNotify)						
						    mAdapter.notifyDataSetChanged();
					}				
				} catch (Exception e) {
				}
			}
			else if(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO.equals(intent.getAction())){
				try {
					int source =  intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0);
					if(source == 4){
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
								if(mData.get(index).getComments() != null){
									for(DynamicCommentItem mItem : mData.get(index).getComments()){
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
								    mData.get(index).getComments().add(0, mCItem);
								    mAdapter.notifyDataSetChanged();
								}
							}
							else {
								int cIndex = 0;
								if(mData.get(index).getComments() != null){
									for(DynamicCommentItem mItem : mData.get(index).getComments()){
										if(mItem.getCid() == mCItem.getCid()){
											cIndex = mData.get(index).getComments().indexOf(mItem);
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
									mData.get(index).getComments().remove(cIndex);
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

	private boolean newDynamic(){
		SystemCountMsgItem item = SystemCountMsgItem.get();
		int newComment = item.getmUnreadCommentsCount();
		int lastEachFocusDynamicID = item.getmEachFocusLastDynamicID();	
		log.i("newDynamic lastEachFocusDynamicID: " + lastEachFocusDynamicID + " newComment : " + newComment);
		if(newComment != 0 || (lastEachFocusDynamicID != 0)){
			return true;
		}
		else {
			return false;
		}
	}
	
	private Handler requestHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
            switch (msg.what) {
			case 1:
				if(refreshing) return;
				if(newDynamic()||(mData != null && mData.isEmpty())){
					 mListView.setSelection(0);
					 requestDynamic(true);
				}
				break;
			case 3:
				if(refreshing) return;
				mListView.setSelection(0);
				requestDynamic(true);
			default:
				break;
			}
		}
	};

	private  void  getCacheData(){
		if(mData.isEmpty()){
			String content = SharedPreferenceUtil.getRelationData();
			if (content != null && !"".equals(content)) {
				try {
					Dynamic dynamic = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(
							content, new TypeReference<Dynamic>() {
							});
					if (dynamic.getData() == null
							|| dynamic.getData().isEmpty()) {
						isCacheData = false;
						return;
					}
					isCacheData = true;
					mCurcor = dynamic.getData()
							.get(dynamic.getData().size() - 1).getAid();
					mData.addAll(dynamic.getData());
					mAdapter.notifyDataSetChanged();
					if (dynamic.getData().size() < Config.PAGE_SIZE) {
						mListView.loadComplete();
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			else {
				isCacheData = false;
			}
	    }
	}

	private class SystemPullReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("SystemPullReceiver:onReceive");
			if (intent == null) {
				log.e("intent is null");
				return;
			}
			try {
				checkMessage();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

}
