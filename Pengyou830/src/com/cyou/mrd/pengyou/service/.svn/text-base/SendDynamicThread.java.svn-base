package com.cyou.mrd.pengyou.service;

import java.io.File;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.DynamicDao;
import com.cyou.mrd.pengyou.entity.DynamicInfoItem;
import com.cyou.mrd.pengyou.entity.DynamicRootPojo;
import com.cyou.mrd.pengyou.entity.RootPojo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.loopj.android.http.RequestParams;

public class SendDynamicThread extends Thread {
    private CYLog log = CYLog.getInstance();
    private DynamicInfoItem item;
    private Context context;
    private MyHttpConnect mConn;
    private DynamicDao mDao;
    
    public SendDynamicThread(Context context,DynamicInfoItem item) {
        this.context = context;
        this.item = item;
        mDao = new DynamicDao(context);
    }
    
    @Override
    public void run() {
        super.run();
        RequestParams params = new RequestParams();
        params.put("text", item.getContent());
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
            HttpContants.NET.PUBLISH_MYACT, params,
                new JSONAsyncHttpResponseHandler() {
                    @Override
                    public void onFailure(Throwable error, String content) {
                        sendDynamicFailBroadCast(0);
                        log.e("error is:" + content);
                        MyHttpConnect.getInstance().setTimeout(20*1000);
                        super.onFailure(error, content);
                    }
    
                    @Override
                    public void onSuccess(int statusCode,JSONObject response) {
                        MyHttpConnect.getInstance().setTimeout(20*1000);
                        if (response == null) {
//                            sendDynamicFailBroadCast(1);
                            return;
                        }
                        log.d(" update userinfo result is:"+ response.toString());
                        DynamicRootPojo rootPojo = JsonUtils.fromJson(response.toString(), DynamicRootPojo.class);
                        if (null == rootPojo) {
//                            sendDynamicFailBroadCast(1);
                            return;
                        }
                        try {
                            String successful = rootPojo.getSuccessful();
                            if (String.valueOf(Contants.SEND_DYNAMIC_STATUS.SUCCESS).equals(successful)) {
                                Integer aid = rootPojo.getData().getAid();
                                sendDynamicSuccessBroadCast(aid);
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
    	mDao.updateStatus(Contants.SEND_DYNAMIC_STATUS.SUCCESS,item.getPid(),UserInfoUtil.getCurrentUserId());
//    	mDao.deleteByPid(item.getPid(),UserInfoUtil.getCurrentUserId());
        UserInfoUtil.changeDyanmicCount(1);
        Intent intent = new Intent(Contants.ACTION.SEND_DYNAMIC_SUCCESS);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, this.item.getPid());
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, aid);
        context.sendBroadcast(intent);
    }
    
    private void sendDynamicFailBroadCast (int type) {
        mDao.updateStatus(Contants.SEND_DYNAMIC_STATUS.FAIL,item.getPid(),UserInfoUtil.getCurrentUserId());
        Intent intent = new Intent(Contants.ACTION.SEND_DYNAMIC_FAIL);
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, this.item.getPid());
        intent.putExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_FAIL_TYPE, type);
        context.sendBroadcast(intent);
    }
}
