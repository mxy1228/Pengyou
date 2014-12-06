package com.cyou.mrd.pengyou.utils;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class RelationUtil {

	private static CYLog log = CYLog.getInstance();
	
	public static final String SINA_WEIBO = "sina";
	private static boolean mWorking = false;
	
	private Context mContext;
	
	public RelationUtil(Context context){
		this.mContext = context;
	}
	
	public static interface ResultListener{
		public void onSuccuss(boolean eachFocused);
		public void onFailed();
	}
	
	/**
	 * 关注
	 * @param uid
	 * @param action Contants.FOCUS.YES/Contants.FOCUS.NO
	 * @param snstag RelationUtil.SINA_WEIBO/null
	 * @param listener
	 */
	public synchronized void focus(final int uid,final int action,final String snstag,final ResultListener listener){
		if(mWorking){
			return;
		}
		RequestParams params = new RequestParams();
		params.put("isfocus", String.valueOf(action));
		params.put("frdid", String.valueOf(uid));
		if(snstag != null){
			params.put("fromsns", snstag);
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.FOCUS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						mWorking = true;
					}

					@Override
					public void onFailure(Throwable error, String content) {
						Util.showToast(mContext, mContext.getResources().getString(R.string.download_error_network_error), Toast.LENGTH_SHORT);
						mWorking = false;
					}
					
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						mWorking = false;
						//关注成功后需要发送广播，更新其他页面 ,比如。搜索页 TODO
						Intent intent = new Intent(Contants.ACTION.ATTENTION_NOTIFY_CHANGED);
						intent.putExtra(Params.FRIEND_INFO.UID, uid);
						
						log.d("关注/取消关注结果="+content);
						Log.d("focussddsds", content);
						if(TextUtils.isEmpty(content)){
							return;
						}
						log.i("focus result = "+content);
						String result = JsonUtils.getJsonValue(content,
								Params.HttpParams.SUCCESSFUL);
						if (!TextUtils.isEmpty(result)) {
							if (Integer.parseInt(result) == Params.HttpParams.SUCCESS) {
								if(action == Contants.FOCUS.YES){
									UserInfoUtil.changeFocusCount(1);
								}else{
									UserInfoUtil.changeFocusCount(-1);
								}
								String bilateral = "0";
								try {
									JSONObject obj = new JSONObject(content);
									if(obj.has("data")){
										String data = JsonUtils.getJsonValue(content, "data");
										bilateral = JsonUtils.getJsonValue(data, "bilateral");
									}
								} catch (Exception e) {
									log.e(e);
								}
								//关注成功后需要发送广播，更新其他页面 ,比如。搜索页 猜你认识，朋游推荐，附近的人
								if(bilateral.equals("")){
									
									intent.putExtra(Params.FOLLOW, 0);
								}else{
									intent.putExtra(Params.FOLLOW, 1);
								}
								listener.onSuccuss(bilateral.equals("1"));
							}else{
								//关注成功后需要发送广播，更新其他页面 ,比如。搜索页
								intent.putExtra(Params.FOLLOW, 3);
								listener.onFailed();
							}
							
							//关注成功后需要发送广播，更新其他页面 ,比如。搜索页 
							mContext.sendBroadcast(intent);
						}
					}
				});
	}
}
