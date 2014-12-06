package com.cyou.mrd.pengyou.service;

import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.base.MyGameBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.ui.MyGameActivity;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class AsyncGameThread {

	private CYLog log = CYLog.getInstance();
	
	private static AsyncGameThread mThread;
	private static Handler mHandler;
	private static MyGameDao mDao;
	private boolean mWorking = false;
	
	public static AsyncGameThread getInstance(){
		if(mThread == null){
			mThread = new AsyncGameThread();
		}
		return mThread;
	}
	
	private AsyncGameThread(){
		mDao = new MyGameDao(CyouApplication.mAppContext);
	}
	
	public void startSyncGame(Handler handler){
//		Log.e("MyGameObserver","AsyncGameThread/startSyncGame");
		mWorking = true;
		mHandler = handler;
		List<GameItem> dbList = mDao.selectAll();
		if(dbList != null && dbList.isEmpty()){
			if(mHandler != null){
				Message msg = new Message();
				msg.obj = dbList;
				msg.what = MyGameActivity.ASYNC_GAME;
				mHandler.sendMessage(msg);
			}
			mWorking = false;
			return;
		}
		loadMyGameFromInternet();
	}
	
	public boolean isWorking(){
		return mWorking;
	}
	
	/**
	 * 从服务器加载我的游戏
	 */
	private void loadMyGameFromInternet() {
		log.d("Load MyGame From Internet");
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.MY_GAME, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							log.d("我的游戏 = " + content);
							MyGameBase base = new ObjectMapper().configure(
											DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
											false).readValue(content,
											new TypeReference<MyGameBase>() {
											});
							if (base == null) {
								return;
							}
							List<GameItem> list = base.getData();
							
							if(list != null && !list.isEmpty()){
								for(GameItem item : list){
									if(Util.isInstallByread(item.getIdentifier())){
										item.setLocalVersion(String.valueOf(Util.getAppVersionCode(item.getIdentifier())));
										mDao.insertOrUpdate(item);
									}
								}
								List<GameItem> dblist = mDao.selectAll();
								for(GameItem item : list){
									for(GameItem i : dblist){
										if(i.getIdentifier().equals(item.getIdentifier())){
											i.setGcacts(item.getGcacts());
											i.setGcid(item.getGcid());
											i.setTodaygcacts(item.getTodaygcacts());
											i.setNoticount(item.getNoticount());
											i.setHastop(item.getHastop());
										}
									}
								}
								if(mHandler != null){
									Message msg = new Message();
									msg.obj = dblist;
									msg.what = MyGameActivity.ASYNC_GAME;
									mHandler.sendMessage(msg);
								}
							}
						} catch (Exception e) {
							log.e(e);
						}finally{
							mWorking = false;
						}
					}

					@Override
					public void onFailure(Throwable error, String content) {
						mWorking = false;
					}
				});
	}
}
