package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RelationshipBaseAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.RelationCircleCommentDao;
import com.cyou.mrd.pengyou.entity.DynamicComment;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.DynamicSupporterItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.RelationCircleCache;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.ViewToast;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class RelationShipBaseFragment extends Fragment {

	public CYLog log = CYLog.getInstance();
	public MyHttpConnect mConn;
	public List<DynamicItem> mData;
	public DisplayImageOptions mOption;
	public Activity mActivity;
	public boolean mSupporting = false;
	public RelationCircleCache  relCache;	
	public RelationshipBaseAdapter mAdapter;
	private boolean mSending = false;

	public RelationShipBaseFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.relCache = new RelationCircleCache(mActivity);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return null;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		if (mData == null) {
			mData = new ArrayList<DynamicItem>();
		}
		if (mOption == null) {
			mOption = new DisplayImageOptions.Builder().cacheInMemory(true)
					.cacheOnDisc(true)
					.showImageForEmptyUri(R.drawable.avatar_defaul)
					.showImageOnFail(R.drawable.avatar_defaul)
					.showStubImage(R.drawable.avatar_defaul).build();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	public void sendComment(final String text, final int aid,
			final int position, final DynamicCommentReplyItem reItem,
			final int type) {
		if (mSending) {
			Toast.makeText(mActivity, R.string.comment_sending_waiting,
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (mData.get(position) == null) {
			return;
		}
		mData.get(position).setCommentcnt(
				mData.get(position).getCommentcnt() + 1);
		final DynamicCommentItem dyItem = new DynamicCommentItem();
		dyItem.setUid(UserInfoUtil.getCurrentUserId());
		dyItem.setNickname(UserInfoUtil.getCurrentUserNickname());
		dyItem.setText(text);
		dyItem.setComment(text);
		dyItem.setReplyto(reItem);
		dyItem.setSendSuccess(1);
		dyItem.setCid(-1);
		dyItem.setTimestamp(System.currentTimeMillis());
		if(type == 4){
			mData.get(position).setCommentUpdate(false);
			mData.get(position).getComments().add(0, dyItem);
		}
		else {
			mData.get(position).getSubCommentData().add(0,dyItem);
		}
		
		mAdapter.notifyDataSetChanged();

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
						mSending = true;
					}

					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mSending = false;
						try {
							mAdapter.relCache.saveFailedComment(dyItem, mData
									.get(position).getAid());
							Config.needResendComment = true;
						} catch (Exception e) {
							log.e(e);
						}
					}

					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mSending = false;
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
									dyItem2.setNickname(UserInfoUtil
											.getCurrentUserNickname());
									dyItem2.setText(text);
									dyItem2.setComment(text);
									dyItem2.setReplyto(reItem);
									dyItem2.setSendSuccess(0);
									dyItem2.setCid(Integer.parseInt(cid));
									dyItem2.setTimestamp(System
											.currentTimeMillis());
									int index;
									if(type == 4){
										index = mData.get(position).getComments().indexOf(dyItem);
									}
									else {
										index = mData.get(position).getSubCommentData().indexOf(dyItem);
									}
									if (index >= 0) {
										if (type == 4) {
											mData.get(position).getComments().remove(index);
											mData.get(position).getComments().add(index, dyItem2);
											mData.get(position).setCommentUpdate(true);
										}
										else{
											mData.get(position).getSubCommentData().remove(index);
											mData.get(position).getSubCommentData().add(index,dyItem2);
										}
										mAdapter.notifyDataSetChanged();
										try {
											Intent intent = new Intent(Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,0); // 0:评论 1：删除评论
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,type); // 0：我的动态， 1：广场 2： 游戏圈
														// 3：他的动态, 4:关系圈
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
											intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT,dyItem2);
											mActivity.sendBroadcast(intent);
										} catch (Exception e) {
											// TODO: handle exception
										}

									}
								} else {
									String errorNo = JsonUtils.getJsonValue(
											content, "errorNo");
									if (!TextUtils.isEmpty(errorNo)) {
										if (errorNo
												.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)) {
											showToastMessage(R.string.comment_maskword_failed,Toast.LENGTH_SHORT);
										} else if (errorNo
												.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)) {
											showToastMessage(R.string.comment_deleted_send_failed,Toast.LENGTH_SHORT);
										} else if (errorNo
												.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)) {
											showToastMessage(R.string.comment_operation_send_failed,Toast.LENGTH_SHORT);
										} else {
											showToastMessage(R.string.comment_send_failed,Toast.LENGTH_SHORT);
										}
									}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						mSending = false;
					}
				});
	}

	/*
	 * type : 0 关系圈， 1 广场， 2 游戏圈  3 他的动态
	 * */
	public void support(final int aid, final int position, final int cancel, final int type) {
		if(mSupporting){
			return;
		}
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		log.i("support cancel=  " + cancel + "  aid:" + aid + "  type:" + type);
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
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						mSupporting = false;
						Config.needResendSupport = true;
						try {
							relCache.saveFailedSupport(aid,cancel);
							if(type == 0){
								addToSupporterList(position,cancel);
							}
							else{
								if(cancel == 0){
									mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() + 1);
								}
								else{
									mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() - 1);
								}
							}
							mAdapter.notifyDataSetChanged();
							if(cancel == 0){
								showToastMessage(R.string.support_failed_comments, Toast.LENGTH_SHORT);
							}
							else{
								showToastMessage(R.string.cancel_support_failed_comments, Toast.LENGTH_SHORT);
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
									if(type == 0){
										addToSupporterList(position,cancel);
									}
									else{
										if(cancel == 0){
											mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() + 1);
											mData.get(position).setSupported(
													Contants.SUPPORT.YES);
										}
										else{
											mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() - 1);
											mData.get(position).setSupported(
													Contants.SUPPORT.NO);
										}
									}
									mAdapter.notifyDataSetChanged();
									log.i("support cancel=  " + cancel + "  aid:" + aid + "  type:" + type);
									sendUpdateSupportBroadcast(type,aid,cancel);
									if (cancel == 0)
										showToastMessage(R.string.support_success,Toast.LENGTH_SHORT);
									else
										showToastMessage(R.string.cancel_support_success,Toast.LENGTH_SHORT);
								}
								else {
									String errorNo = JsonUtils.getJsonValue(
											content, "errorNo");
	        						if (!TextUtils.isEmpty(errorNo))
	        						{
	        							if(errorNo.equals(Contants.ERROR_NO.ERROR_MASK_WORD_STRING)){
	        								showToastMessage(R.string.comment_maskword_failed,Toast.LENGTH_SHORT);
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_NOT_EXIST_STRING)){
	        								showToastMessage(R.string.comment_deleted_send_failed,Toast.LENGTH_SHORT);
	        							}else if(errorNo.equals(Contants.ERROR_NO.ERROR_OPERATION_STRING)){
	        								showToastMessage(R.string.comment_operation_send_failed,Toast.LENGTH_SHORT);
	        							}else {
	        								showToastMessage(R.string.comment_send_failed,Toast.LENGTH_SHORT);
	        							}
	        						}
	        						if (cancel == 0) {
										mData.get(position).setSupported(
												Contants.SUPPORT.NO);
									} else {
										mData.get(position).setSupported(
												Contants.SUPPORT.YES);
									}
	        						mAdapter.notifyDataSetChanged();
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						mSupporting = false;
					}
				});
	}
	
	private void  addToSupporterList(int position, int cancel){
		if(cancel == 0){
			mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() + 1);
			mData.get(position).setSupported(Contants.SUPPORT.YES);
		    DynamicSupporterItem supporter =  new DynamicSupporterItem();
		    supporter.setNickname(UserInfoUtil.getCurrentUserNickname());
		    supporter.setUid(UserInfoUtil.getCurrentUserId());

		   if(mData.get(position).getSupportusr().size() == 0){
		       mData.get(position).getSupportusr().add(supporter);
		       ArrayList< DynamicSupporterItem> list = new  ArrayList<DynamicSupporterItem>();
		       list.add(supporter);
		       mData.get(position).setSupportusr(list);
		   }
		   else {
			   int needAdd = 0;
			   for(int i =0 ; i< mData.get(position).getSupportusr().size(); i++){
					 if(mData.get(position).getSupportusr().get(i).getUid() == UserInfoUtil.getCurrentUserId()){
						 needAdd = 0;
						 break;
					 }
					 needAdd = 1;
				}
			    if(needAdd == 1){
			    	  ArrayList<DynamicSupporterItem> 
			    	  list = (ArrayList<DynamicSupporterItem>)mData.get(position).getSupportusr();
			    	  list.add(supporter);
					  mData.get(position).setSupportusr(list);	
			    }
		   }
		}
		else{ 
			mData.get(position).setSupportcnt(mData.get(position).getSupportcnt() - 1);
			mData.get(position).setSupported(Contants.SUPPORT.NO);
			 for(int i =0 ; i< mData.get(position).getSupportusr().size(); i++){
				 if(mData.get(position).getSupportusr().get(i).getUid() == UserInfoUtil.getCurrentUserId()){
					 mData.get(position).getSupportusr().remove(i);
					 break;
				 }
			 }
			 ArrayList<DynamicSupporterItem> 
	    	 list = (ArrayList<DynamicSupporterItem>)mData.get(position).getSupportusr();
			 mData.get(position).setSupportusr(list);
		}
	}
	
	/**
	 * 获取动态评论
	 * 
	 * @param aid
	 *            //动态id
	 */
	public void getComments(int aid, final int position, final int type) {
		final DynamicItem i = mData.get(position);
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
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						if(type != 0){
							try {
								 showToastMessage(R.string.networks_available, Toast.LENGTH_SHORT);
				                 i.setCommentUpdate(true);
				                 mAdapter.notifyDataSetChanged();
							} catch (Exception e) {
								// TODO: handle exception
							}
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
									if(type == 0){
										i.setComments(comment.getData());
										for(int j = 0 ; j< comment.getData().size(); j++){
											i.getComments().get(j).setText(comment.getData().get(j).getComment());
											
										}
										i.setComment(Util.dynamicComment2Html(comment.getData()));
										i.setLastcommentid(comment.getData()
												.get(0).getCid());
										i.setCommentcnt(comment.getData().size());
										i.setCommentUpdate(false);
									}
									else {
										i.setSubCommentData(comment.getData());
										i.setComment(Util.dynamicComment2Html(comment.getData()));

										i.setLastcommentid(comment.getData()
												.get(0).getCid());
										i.setCommentcnt(comment.getData().size());
										i.setCommentUpdate(false);
									}
									
								}
								else{
									i.setCommentcnt(0);
								}
							}
							else {
								i.setCommentcnt(0);
							}
							i.setLoadingComments(false);
							mAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
	
	public void  deleteComment(final DynamicCommentItem dynamicCommentItem, final int position, final int type){
		final DynamicItem i = mData.get(position);
		RequestParams params = new RequestParams();
		if(dynamicCommentItem.getSendSuccess() == 1){
            if(type == 0){
            	i.getComments().remove(dynamicCommentItem);
    			if(i.getCommentcnt() >0){
                    i.setCommentcnt(i.getCommentcnt() - 1);
    				i.setCommentUpdate(true);
                }
            }
            else {
            	i.getSubCommentData().remove(dynamicCommentItem);
    			if(i.getCommentcnt() >0){
                    i.setCommentcnt(i.getCommentcnt() - 1);
                }
            }
            try {
            	new RelationCircleCommentDao(mActivity).delete(i.getAid(), dynamicCommentItem.getCid(), 0, dynamicCommentItem.getTimestamp());
            	mAdapter.notifyDataSetChanged();
			} catch (Exception e) {
				// TODO: handle exception
			}
			return;
		}
		params.put("cid", String.valueOf(dynamicCommentItem.getCid()));
		MyHttpConnect.getInstance().post(HttpContants.NET.DELETE_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
						mSending = true;
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						mSending = false;
						try {
		                    showToastMessage(R.string.networks_available, Toast.LENGTH_SHORT);
						} catch (Exception e) {
							// TODO: handle exception
						}			
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
                                    if(type == 0){
                                    	for(int ii = 0; ii < i.getComments().size(); ii++){
    										if(i.getComments().get(ii).getCid()  == dynamicCommentItem.getCid()){
    											i.getComments().remove(ii);
    											if(i.getCommentcnt() >0){
    			                                    i.setCommentcnt(i.getCommentcnt() - 1);
    			    								i.setCommentUpdate(true);
    			                                }
    											sendUpdateCommentBroadcast(type, i.getAid(),dynamicCommentItem );
    											showToastMessage(R.string.delete_success, Toast.LENGTH_SHORT);
    											break;
    										}
    									}
                                    }
                                    else {
                                    	for(int ii = 0; ii < i.getSubCommentData().size(); ii++){
    										if(i.getSubCommentData().get(ii).getCid()  == dynamicCommentItem.getCid()){
    											i.getSubCommentData().remove(ii);
    											if(i.getCommentcnt() > 0){
    											    i.setCommentcnt(i.getCommentcnt() - 1);
    		                                    }
    											sendUpdateCommentBroadcast(type, i.getAid(),dynamicCommentItem );
    											showToastMessage(R.string.delete_success, Toast.LENGTH_SHORT);
    											break;
    										}
    									}
                                    }
                                    mAdapter.notifyDataSetChanged();						
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
						mSending = false;
					}
				});
	}
	
	private void  sendUpdateCommentBroadcast(int type, int aid, DynamicCommentItem dynamicCommentItem){
		Intent intent = new Intent(
				Contants.ACTION.UPDATE_RELATION_COMMENT_INFO);
		switch (type) {
		case 0:
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,4); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
			mActivity.sendBroadcast(intent);
			break;
		case 1:
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,1); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
			mActivity.sendBroadcast(intent);
			break;
		case 2:
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,2); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
			mActivity.sendBroadcast(intent);
			break;
		case 3:
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TYPE,1); //0:评论 1：删除评论
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,3); // 0：我的动态， 1：广场 2： 游戏圈  3：他的动态， 4：关系圈
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
			intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_TEXT, dynamicCommentItem);
			mActivity.sendBroadcast(intent);
			break;
		default:
			break;
		}
	}
	
	private void  sendUpdateSupportBroadcast(int type, int aid, int cancel){
		Intent intent = new Intent(
				Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO);
		log.i("sendUpdateSupportBroadcast cancel=  " + cancel + "  aid:" + aid + "  type:" + type);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID,aid);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT,cancel);
		intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID,type); // 3：TA的动态， 1：广场 2： 游戏圈
		mActivity.sendBroadcast(intent);
		
	}
	
	public void showToastMessage(int resId, int duration) {
		ViewToast.showToast(CyouApplication.mAppContext, resId, duration);
		return;
	}

}
