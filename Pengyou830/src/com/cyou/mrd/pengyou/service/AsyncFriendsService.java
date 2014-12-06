package com.cyou.mrd.pengyou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncFriendsService extends Service {

	private CYLog log = CYLog.getInstance();

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		startAsync();
	}

	/**
	 * 和服务器同步好友列表
	 */
	private void asyncFriends() {
		// FriendDao dao = new FriendDao(this);
		// List<FriendItem> allFriendsInDB = dao.getAllFriendList();
		// if(allFriendsInDB != null){
		// if(allFriendsInDB.isEmpty()){
		// log.d("====好友列表为空，开始同步====");
		// startAsync();
		// }else{
		// log.d("====好友列表不为空，取消同步====");
		// }
		// }
	}


	private void startAsync() {
		final FriendDao dao = new FriendDao(getApplicationContext());
		RequestParams params = new RequestParams();
		params.put("page", "1");
		params.put("count", "20");
//			MyHttpConnect.getInstance().post(HttpContants.NET.GET_FRIEND_UIDS,
				MyHttpConnect.getInstance().post(HttpContants.NET.MYFRIEND_LIST,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, final String content) {
							log.i("Async Friends result = " + content);
//						Log.e("LaunchService", "startAsync onSuccess:" + content);
						new Thread(){
							@Override
							public void run() {
								super.run();
								try {
									String data = JsonUtils.getJsonValue(content,"data");
									if (TextUtils.isEmpty(data)) {
										return;
									}
									List<FriendItem> list = JsonUtils.json2List(data,
											FriendItem.class);
									if (list != null) {
										dao.insertOrUpdateFriends(list);
									}
									stop();
								} catch (Exception e) {
									log.e(e);
								}
							}
						}.start();
						
					}
				});
//		final FriendDao dao = new FriendDao(getApplicationContext());
//		RequestParams params = new RequestParams();
//		MyHttpConnect.getInstance().post(HttpContants.NET.GET_FRIEND_UIDS,
//				params, new AsyncHttpResponseHandler() {
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						log.i("Async Friends result = " + content);
//						try {
//							JSONObject obj = new JSONObject(content);
//							JSONArray fridsFromServer = obj
//									.getJSONArray("data");
//							Set<Integer> fridsFromDB = dao.selectUids();
//							List<Integer> newList = new ArrayList<Integer>();
//							int length = fridsFromServer.length();
//							for (int i = 0; i < length; i++) {
//								int uid = Integer.valueOf(fridsFromServer
//										.get(i).toString());
//								if (fridsFromDB.contains(uid)) {
//									fridsFromDB.remove(uid);
//								} else {
//									newList.add(uid);
//								}
//							}
//							// 此时newList中是新添加的好友
//							// fridsFromDB中时被删除的好友
//							if (!fridsFromDB.isEmpty()) {
//								dao.deleteFriends(new ArrayList<Integer>(
//										fridsFromDB));
//							}
//							if (!newList.isEmpty()) {
//								List<FriendItem> l = new ArrayList<FriendItem>();
//								for(Integer i : newList){
//									FriendItem item = new FriendItem();
//									item.setUid(i);
//									item.setRecentgms(new ArrayList<String>());
//									l.add(item);
//								}
//								dao.insertFriends(l);
//							}
//						} catch (Exception e) {
//							log.e(e);
//						}
//						stop();
//					}
//				});
	}

	private void stop() {
		this.stopSelf();
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
}
