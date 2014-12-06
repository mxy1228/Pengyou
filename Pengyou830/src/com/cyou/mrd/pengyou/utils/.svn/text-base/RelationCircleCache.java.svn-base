package com.cyou.mrd.pengyou.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.RelationCircleCommentDao;
import com.cyou.mrd.pengyou.db.RelationCircleDynamicDao;
import com.cyou.mrd.pengyou.db.RelationCircleGameDao;
import com.cyou.mrd.pengyou.db.RelationCircleSupporterDao;
import com.cyou.mrd.pengyou.entity.DynamicCommentItem;
import com.cyou.mrd.pengyou.entity.DynamicCommentReplyItem;
import com.cyou.mrd.pengyou.entity.DynamicGameItem;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.entity.DynamicPic;
import com.cyou.mrd.pengyou.entity.DynamicSupporterItem;
import com.cyou.mrd.pengyou.entity.RelaionShipCircleSupporterItem;
import com.cyou.mrd.pengyou.entity.RelationShipCircleCommentItem;
import com.cyou.mrd.pengyou.entity.RelationShipCircleDynamicItem;
import com.cyou.mrd.pengyou.entity.RelationShipCircleGameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.cyou.mrd.pengyou.log.CYLog;

public class RelationCircleCache {
	/**
	 * 保存及发送离线赞及评论
	 * @author bichunhe
	 *
	 */

	private RelationCircleSupporterDao  supporterDao;	
	private Activity mContext;
	private RelationCircleDynamicDao  dynamicDao;
	private RelationCircleGameDao  gameDao;
	private RelationCircleCommentDao  commentDao;
	private CYLog log = CYLog.getInstance();

	public RelationCircleCache(Activity context){
		this.mContext = context;
	}

	/*
	 * 保存失败的赞
	 */
	public void  saveFailedSupport(int aid, int cancel){
		try {
			if (supporterDao == null) {
				supporterDao = new RelationCircleSupporterDao(mContext);
			}
			log.i("saveFailedSupport aid: " + aid);
			log.i("saveFailedSupport cancel: " + cancel);
			RelaionShipCircleSupporterItem  sItem = new RelaionShipCircleSupporterItem();
			sItem.setAid(aid);
			sItem.setAvatar("");
			sItem.setCancel(cancel);
			sItem.setFailed(1);
			sItem.setDynamicType(0);
			sItem.setNickname(UserInfoUtil.getCurrentUserNickname());
			sItem.setUid(UserInfoUtil.getCurrentUserId());
			supporterDao.insert(sItem);
		} catch (Exception e) {
			// TODO: handle exception
		}	
	}
	
	/*
	 * 保存失败的评论
	 */
	public void  saveFailedComment( DynamicCommentItem item, int aid){
		try {
			if (commentDao == null) {
				commentDao = new RelationCircleCommentDao(mContext);
			}
			log.i(" saveFailedComment aid: " + aid);
			log.i(" saveFailedComment cid: " + item.getCid());
			log.i(" saveFailedComment name: " + item.getNickname());
			log.i(" saveFailedComment text: " + item.getText());
			RelationShipCircleCommentItem cItem = new RelationShipCircleCommentItem();
			cItem.setAid(aid);
			cItem.setCid(item.getCid());
			
		    cItem.setAvatar(item.getAvatar());
			 
			cItem.setComment(item.getText());
			cItem.setNickname(item.getNickname());
			cItem.setSendSuccess(item.getSendSuccess());
			cItem.setUid(item.getUid());
			cItem.setTimestamp(item.getTimestamp());
			cItem.setDynamicType(0);
			if(item.getReplyto() != null)
			cItem.setReplyto(item.getReplyto());
			commentDao.insert(cItem);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	/*
	 * 发送缓存中失败的赞
	 */
	public void  sendFailedSupport(){
		try {
			 if (supporterDao == null) {
					supporterDao = new RelationCircleSupporterDao(mContext);
				}
				
				List<RelaionShipCircleSupporterItem>  sList  = supporterDao.selectFailedSupporter(0);
				log.i("sendFailedSupport size: " +sList.size());
				if(sList.size() == 0){
					Config.needResendSupport = false;
				}
				int k = 0;
				while(k < sList.size()){
					log.i(" sendFailedComments aid: " + sList.get(k).getAid());
					log.i(" sendFailedComments cancel: " + sList.get(k).getCancel());
					sendSupportFailed(sList.get(k));
					k++;
				}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * 发送缓存中失败的评论
	 */		
	public void  sendFailedComments(){
		try {
			if (commentDao == null) {
				commentDao = new RelationCircleCommentDao(mContext);
			}		
			List<RelationShipCircleCommentItem>  cList  = commentDao.selectDynamicCommentFailed(0);
			log.i("sendFailedComments size: " +cList.size());
			if(cList.size() == 0){
				Config.needResendComment = false;
			}
			int j = 0;
			while(j < cList.size()){
				log.i(" sendFailedComments aid: " + cList.get(j).getAid());
				log.i(" sendFailedComments cid: " + cList.get(j).getCid());
				log.i(" sendFailedComments name: " + cList.get(j).getNickname());
				log.i(" sendFailedComments text: " + cList.get(j).getComment());
				sendCommentFailed(cList.get(j));
				j++;
			}		
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * 发送缓存中失败的评论信息
	 */		
	private void sendCommentFailed(final RelationShipCircleCommentItem item) {
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(item.getAid()));
		params.put("text", item.getComment());
		if(item.getReplyto().getNickname() != null && !item.getReplyto().getNickname().isEmpty()){
			params.put("reuid", Integer.valueOf(item.getReplyto().getUid()).toString());
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.SEND_DYNAMIC_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);	
						Config.needResendComment = true;
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {									
									deleteDbComment(item);
									Config.needResendComment = false;
								}
							}
						} catch (Exception e) {
						}
					}
				});
	}
	
	/*
	 * 发送缓存中失败的赞信息
	 */		
    private void sendSupportFailed(final RelaionShipCircleSupporterItem item) {
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(item.getAid()));
		if(item.getCancel() == 1)
			params.put("cancel", "1");
		
		MyHttpConnect.getInstance().post(HttpContants.NET.SUPPORT_DYNAMIC, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						super.onStart();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						Config.needResendSupport= true;
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {									
									deleteDbSupport(item);
									Config.needResendSupport = false;

								}
							}
						} catch (Exception e) {
						}
					}
				});
	}
	
	/*
	 * 从数据库删除发送成功的评论信息
	 */	
	private void  deleteDbComment( RelationShipCircleCommentItem item){
		try {
			if (commentDao == null) {
				commentDao = new RelationCircleCommentDao(mContext);
			}
			log.i("deleteDbComment aid: " +item.getAid() + "   cid:" + item.getCid());
			commentDao.delete(item.getAid(), item.getCid(), 0, item.getTimestamp());
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * 从数据库删除发送成功的赞信息
	 */
	private void  deleteDbSupport (RelaionShipCircleSupporterItem item){
		try {
			if (supporterDao == null) {
				supporterDao = new RelationCircleSupporterDao(mContext);
			}
			log.i("deleteDbSupport aid: " +item.getAid() + "   uid:" + item.getUid());
			supporterDao.deleteFailedDbSupport(item.getAid(), item.getUid(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	/*
	 * 存储缓存数据至数据库
	 */
	public  boolean saveDynamicDataToDb( List<DynamicItem> data){
		
		if (dynamicDao == null) {
			dynamicDao = new RelationCircleDynamicDao(mContext);
		}
		;
		if (gameDao == null) {
            gameDao = new RelationCircleGameDao(mContext);
		}
		;
		if (commentDao == null) {
			commentDao = new RelationCircleCommentDao(mContext);
		}
		;
		if (supporterDao == null) {
			supporterDao = new RelationCircleSupporterDao(mContext);
		}
		;
		RelationShipCircleDynamicItem dItem ;
		for(int i = 0; i < data.size(); i++){
		    dItem = new RelationShipCircleDynamicItem();
			DynamicItem  dataItem = data.get(i);
			
			dItem.setAid(dataItem.getAid());
			dItem.setNickname(dataItem.getNickname());
			dItem.setUid(dataItem.getUid());
			
			dItem.setAvatar(dataItem.getAvatar());
			dItem.setCommentcnt(dataItem.getCommentcnt());
			dItem.setSupportcnt(dataItem.getSupportcnt());
			dItem.setCreatedtime(dataItem.getCreatedtime());
			dItem.setCursor(dataItem.getCursor());
			
			dItem.setType(dataItem.getType());
            dItem.setGender(dataItem.getGender());
			dItem.setText(dataItem.getText());
			dItem.setSupported(dataItem.getSupported());
			
			dItem.setStar(dataItem.getStar());
			
			dItem.setDynamicType(0);  // just fixed here
			
			if(dataItem.getPicture() != null){
				dItem.setPicture(dataItem.getPicture());
				
			}
			else{
				DynamicPic pic = new DynamicPic();
				pic.setHeight(0);
				pic.setPath("");
				pic.setWidth(0);
				dItem.setPicture(pic);
			}
			if(dataItem.getPicturemiddle() != null){
				dItem.setPicturemiddle(dataItem.getPicturemiddle());
				
			}
			else{
				DynamicPic pic = new DynamicPic();
				pic.setHeight(0);
				pic.setPath("");
				pic.setWidth(0);
				dItem.setPicturemiddle(pic);
			}

			if(dataItem.getPicturesmall()!= null){
				dItem.setPicturesmall(dataItem.getPicturesmall());
				
			}
			else{
				DynamicPic pic = new DynamicPic();
				pic.setHeight(0);
				pic.setPath("");
				pic.setWidth(0);
				dItem.setPicturesmall(pic);
			}
			if(dataItem.getComments() != null && dataItem.getComments().size() > 0){
				RelationShipCircleCommentItem cItem;
				 for(int j = 0; j< dataItem.getComments().size(); j++ ){
					 DynamicCommentItem dataCommentItem = dataItem.getComments().get(j);
					 cItem = new RelationShipCircleCommentItem();
					 cItem.setAid(dataItem.getAid());
					 cItem.setCid(dataCommentItem.getCid());
					
				     cItem.setAvatar(dataCommentItem.getAvatar());
					 
					 cItem.setComment(dataCommentItem.getText());
					 cItem.setNickname(dataCommentItem.getNickname());
					 cItem.setSendSuccess(dataCommentItem.getSendSuccess());
					 cItem.setUid(dataCommentItem.getUid());
					 cItem.setTimestamp(dataCommentItem.getTimestamp());
					 cItem.setDynamicType(0);
					 if(dataCommentItem.getReplyto() != null ){
						 cItem.setReplyto(dataCommentItem.getReplyto());
					 }
					 else{
						 DynamicCommentReplyItem reply = new DynamicCommentReplyItem();
						 reply.setNickname("");
						 reply.setUid(0);
						 cItem.setReplyto(reply);						 
					 }
					 commentDao.insert(cItem);					 
				 }
			}
			
			
			if(dataItem.getGame() != null){
				DynamicGameItem gameItem =dataItem.getGame();
				RelationShipCircleGameItem gItem = new RelationShipCircleGameItem();
				gItem.setAid(dataItem.getAid());
				gItem.setDynamicType(0);
				
		        if(gameItem.getGamecode() != null){
		        	gItem.setGamecode(gameItem.getGamecode());
		        }
		        else{
		        	gItem.setGamecode("");
		        }
				
		        if(gameItem.getGamedesc() != null){
		        	gItem.setGamedesc(gameItem.getGamedesc());
		        }
		        else{
		        	gItem.setGamedesc("");
		        }
		        if(gameItem.getGameicon() != null){
		        	gItem.setGameicon(gameItem.getGameicon());
		        }
		        else{
		        	gItem.setGameicon("");
		        }
		        if(gameItem.getGamenm() != null){
		        	gItem.setGamenm(gameItem.getGamenm());
		        }
		        else{
		        	gItem.setGamenm("");
		        }
		        if(gameItem.getGametype() != null){
		        	gItem.setGametype(gameItem.getGametype());
		        }
		        else{
		        	gItem.setGametype("");
		        }
		       
		        gItem.setPlatform(gameItem.getPlatform());
		        gItem.setPlaynum(gameItem.getPlaynum());
		        gameDao.insert(gItem);
			}
			
			if(dataItem.getSupportusr() != null && dataItem.getSupportusr().size() > 0){
				 RelaionShipCircleSupporterItem sItem;
				 for(int k = 0; k< dataItem.getSupportusr().size(); k++ ){
					 DynamicSupporterItem dataSupporterItem = dataItem.getSupportusr().get(k);
					 sItem = new RelaionShipCircleSupporterItem();
					 sItem.setAid(dataItem.getAid());
					 sItem.setUid(dataSupporterItem.getUid());
					 sItem.setAvatar(dataSupporterItem.getAvatar());
					 sItem.setNickname(dataSupporterItem.getNickname());
					 sItem.setCancel(0);
					 sItem.setFailed(0);
					 sItem.setDynamicType(0);
					 supporterDao.insert(sItem);					 
				 }			
			}		
			dynamicDao.insert(dItem);
		
		}
		return true;
	}
	
   /*
    * 从数据库获得缓存数据
    */
	public List<DynamicItem>  getCircleCacheDataFromDb(){
		List<DynamicItem> mData  = new ArrayList<DynamicItem>();
		if(mData.isEmpty() || mData.size() < 10){
			if (dynamicDao == null) {
				dynamicDao = new RelationCircleDynamicDao(mContext);
			}
			;
			if (gameDao == null) {
	            gameDao = new RelationCircleGameDao(mContext);
			}
			;
			if (commentDao == null) {
				commentDao = new RelationCircleCommentDao(mContext);
			}
			;
			if (supporterDao == null) {
				supporterDao = new RelationCircleSupporterDao(mContext);
			}
			;
			List<RelationShipCircleDynamicItem>  dynamicItems = dynamicDao.selectAll();
			for(int i =0; i< dynamicItems.size(); i++){
				DynamicItem  dItem = new DynamicItem();
				RelationShipCircleDynamicItem  dynamicItem = dynamicItems.get(i);
				dItem.setAid(dynamicItem.getAid());
				dItem.setCursor(dynamicItem.getCursor());
				dItem.setAvatar(dynamicItem.getAvatar());
				dItem.setGender(dynamicItem.getGender());
				dItem.setCreatedtime(dynamicItem.getCreatedtime());
				dItem.setSupported(dynamicItem.getSupported());
				dItem.setCommentcnt(dynamicItem.getCommentcnt());
				dItem.setSupportcnt(dynamicItem.getSupportcnt());
				dItem.setText(dynamicItem.getText());
				dItem.setType(dynamicItem.getType());
				dItem.setUid(dynamicItem.getUid());
				dItem.setNickname(dynamicItem.getNickname());				
				List<RelationShipCircleCommentItem>  cList  = commentDao.selectDynamicComment(dynamicItem.getAid(), 0);
				int j = 0;
				while(j < cList.size()){
					DynamicCommentItem  commentItem = new DynamicCommentItem();
					commentItem.setCid(cList.get(j).getCid());
					commentItem.setNickname(cList.get(j).getNickname());
					commentItem.setAvatar(cList.get(j).getAvatar());
					commentItem.setReplyto(cList.get(j).getReplyto());
					commentItem.setText(cList.get(j).getComment());
					commentItem.setTimestamp(cList.get(j).getTimestamp());
					dItem.getComments().add(commentItem);
					dItem.getSubCommentData().add(commentItem);
					j++;
				}

				RelationShipCircleGameItem  gameItem =  gameDao.selectGame(dynamicItem.getAid(), 0);

				if(gameItem != null){
					DynamicGameItem  gItem = new DynamicGameItem();
					gItem.setGamecode(gameItem.getGamecode());
					gItem.setGamedesc(gameItem.getGamedesc());
					gItem.setGamenm(gameItem.getGamenm());
					gItem.setGameicon(gameItem.getGameicon());
					gItem.setPlatform(gameItem.getPlatform());
					gItem.setPlaynum(gameItem.getPlaynum());
					gItem.setGametype(gameItem.getGametype());
					
					dItem.setGame(gItem);
				}

				List<RelaionShipCircleSupporterItem>  sList = supporterDao.selectSupporter(dynamicItem.getAid(), 0);
				int k =0;
				while (k < sList.size()) {
					DynamicSupporterItem sItem = new DynamicSupporterItem();
					sItem.setAvatar(sList.get(k).getAvatar());
					sItem.setNickname(sList.get(k).getNickname());
					sItem.setUid(sList.get(k).getUid());
					dItem.getSupportusr().add(sItem);										
					
					k++;
					
				}
				mData.add(dItem);
			}
					
		}
		return mData;
	}
}
