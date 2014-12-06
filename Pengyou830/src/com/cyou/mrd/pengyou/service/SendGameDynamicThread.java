package com.cyou.mrd.pengyou.service;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.GameDynamicDao;
import com.cyou.mrd.pengyou.entity.DynamicRootPojo;
import com.cyou.mrd.pengyou.entity.GameDynamicInfoItem;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.loopj.android.http.RequestParams;

public class SendGameDynamicThread extends Thread {
    private CYLog log = CYLog.getInstance();
    private GameDynamicInfoItem item;
    private Context context;
    private MyHttpConnect mConn;
    private GameDynamicDao mDao;
    private String mGamecode;
    private String mGcid;
    private float mScore;
    public SendGameDynamicThread(Context context,GameDynamicInfoItem item,String mGamecode,String mGcid,float Score) {
        this.context = context;
        this.item = item;
        this.mGamecode = mGamecode;
        this.mGcid = mGcid;
        this.mScore = Score;
        mDao = new GameDynamicDao(context);
    }
    
    @Override
    public void run() {
        super.run();
        RequestParams params = new RequestParams();
        params.put("text", item.getContent());
        params.put("gameid", mGcid);
        params.put("gamecode", mGamecode);
        params.put("star", Float.toString(mScore));
        if (mConn == null) {
            mConn = MyHttpConnect.getInstance();
        }
        if (!TextUtils.isEmpty(item.getPicture())) {
            try {
                    File mPicFile = new File(item.getPicture());
                    if (mPicFile.exists()) {
                        log.d("file size is:" + mPicFile.length() / 1024);
                        MyHttpConnect.getInstance().setTimeout(150*1000);
                        params.put("picture", mPicFile);
                    }
                } catch (Exception e) {
                    log.e(e);
                }
            }
        MyHttpConnect.getInstance().post2Json(
            HttpContants.NET.SHARE_GAME_COMMENT, params,
                new JSONAsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable error, String content) {
                        sendDynamicFailBroadCast(0);
                        log.e("error is:" + content);
                        MyHttpConnect.getInstance().setTimeout(20*1000);
                        super.onFailure(error, content);
                    }
    
                    @Override
                    public void onSuccess(int statusCode,
                            JSONObject response) {
                        MyHttpConnect.getInstance().setTimeout(20*1000);
                        log.d(" update userinfo result is:"
                                + response.toString());
                        if (response.toString() == null) {
//                            sendDynamicFailBroadCast(1);
                            return;
                        }
                        DynamicRootPojo rootPojo = JsonUtils.fromJson(
                                response.toString(), DynamicRootPojo.class);
                        if (null == rootPojo) {
//                            sendDynamicFailBroadCast(1);
                            return;
                        }
                        try {
                            String successful = rootPojo.getSuccessful();
                            String argstar=rootPojo.getData().getGamestar();        
                            int[] stardistr=rootPojo.getData().getStardistr();
                            String misremarked=rootPojo.getData().getIsremarked();
                            String misremarkednum=rootPojo.getData().getIsremarkednum();
                            String mchangeerror=rootPojo.getData().getChangeerror();                            
                            String mchangevalue=rootPojo.getData().getChangevalue();
//                            log.d("rootPojo.getData().getGamestar()"+ argstar);
//                            log.d("rootPojo.getData().getStardistr()"+ stardistr);
                            if (String.valueOf(Contants.SEND_DYNAMIC_STATUS.SUCCESS).equals(successful)) {
                                Integer aid = rootPojo.getData().getAid();
                                sendDynamicSuccessBroadCast(aid);
                                if(!TextUtils.isEmpty(argstar)&& stardistr.length>0 && stardistr!=null &&!TextUtils.isEmpty(misremarked)&&
                                		!TextUtils.isEmpty(misremarkednum)&& !TextUtils.isEmpty(mchangeerror)&& !TextUtils.isEmpty(mchangevalue)){
                                	 sendDynamicSuccessOfStarBroadCast(argstar,stardistr,misremarked,misremarkednum,mchangeerror,mchangevalue);   
                                 }
                                 return;
                            }else {
                            	String errorNo = rootPojo.getErrorNo();
        						if (!TextUtils.isEmpty(errorNo)
        								&& Contants.ERROR_NO.ERROR_MASK_WORD_STRING
        										.equals(errorNo)) {  
        							sendDynamicFailBroadCast(4);
        						} else {
        							sendDynamicFailBroadCast(2);
        						}
                            }
                        } catch (Exception e) {
                            log.e(e);
//                            sendDynamicFailBroadCast(3);
//                            return;
                        }
                        super.onSuccess(statusCode, response);
                    }
                });
    }

    /**
     * 发送成功后发送广播，并删除图片
     */
    private void sendDynamicSuccessBroadCast (Integer aid) {
    	mDao.updateStatus(Contants.SEND_DYNAMIC_STATUS.SUCCESS,item.getPid());
//    	mDao.deleteByPid(item.getPid());
        UserInfoUtil.changeDyanmicCount(1);
        Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_SUCCESS);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, this.item.getPid());
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, aid);
        intent.putExtra(Params.INTRO.GAME_CODE, mGamecode);
        intent.putExtra(Params.INTRO.GAME_SCORE, Float.toString(mScore));
        context.sendBroadcast(intent);
    }
    /**
     * 发送成功,接收服务器返回的star,更新游戏详情中的UI   wuzheng
     */
    private void sendDynamicSuccessOfStarBroadCast (String margstar,int[] mstardistr,String misremarked,String misremarkednum,String mchangeerror,String mchangevalue) {
    	
    	Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_STAR_SUCCESS);
//    	Bundle bundle = new Bundle();  
//    	Set<String> keySet = mstardistr.keySet();  
//    	Iterator<String> iter = keySet.iterator();                      
//    	while(iter.hasNext())  
//    	{  
//    	    String key = iter.next();  
//    	    bundle.putInt(key,mstardistr.get(key));  
//    	} 
//    	log.d("bundle"+bundle);
//    	intent.putExtra("map", bundle);          	      
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_STAR, margstar);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_STARDISTR, mstardistr);
        intent.putExtra(Params.INTRO.GAME_CODE, mGamecode);
        intent.putExtra("isremarked", misremarked);
        intent.putExtra("isremarkednum", misremarkednum);
        intent.putExtra("changeerror", mchangeerror);
        intent.putExtra("changevalue", mchangevalue);
       // intent.putExtra(Params.INTRO.GAME_SCORE, Float.toString(mScore));
        context.sendBroadcast(intent);
    }

    private void sendDynamicFailBroadCast (int type) {
        mDao.updateStatus(Contants.SEND_DYNAMIC_STATUS.FAIL,item.getPid());
        Intent intent = new Intent(Contants.ACTION.SEND_GAMEDYNAMIC_FAIL);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, this.item.getPid());
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_FAIL_TYPE, type);
        intent.putExtra(Params.INTRO.GAME_SCORE, Float.toString(mScore));
        context.sendBroadcast(intent);
    }
}
