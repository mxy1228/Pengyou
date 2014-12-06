package com.cyou.mrd.pengyou.receiver;

import java.io.File;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;

import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.db.DBHelper;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.GameDownloadRecordDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.db.provider.MyGameProvider;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.GameCodeInfo;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.base.GameCodeBase;
import com.cyou.mrd.pengyou.entity.base.MyGameBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 接收手机上安装应用的广播
 * 
 * @author xumengyang
 * 
 */
public class InstallAppReceiver extends BroadcastReceiver {

	private CYLog log = CYLog.getInstance();

	private DownloadDao mDownloadDao;
	private GameDownloadRecordDao mGameDownloadRecordDao;
	private MyGameDao mMyGameDao;
	private PackageManager mManager;
	private Context mContext;
	private Handler myHandler=new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			if(msg!=null && msg.what==1 && msg.obj!=null){
				String packageName=msg.obj.toString();
				Intent installIntent = new Intent(Contants.ACTION.GAME_INSTALL);
				installIntent.putExtra(DownloadParam.PACKAGE_NAME,packageName);
				mContext.sendBroadcast(installIntent);
			}
			return false;
		}
	});
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent != null) {
			try {
				mManager = context.getPackageManager();
				mDownloadDao = DownloadDao.getInstance(mContext);;
				mGameDownloadRecordDao = new GameDownloadRecordDao();
				mMyGameDao = new MyGameDao(context);
				this.mContext = context;
				// 因intent.getDataString()的值为package:xxx.xxx，所以取子串substring(8)
				final String packageName = intent.getDataString().substring(8);
				if (intent.getAction().equals(
						"android.intent.action.PACKAGE_ADDED")) {
					log.i(" Install Apk package is :" + packageName);
				}
				log.i("install packageName = " + packageName);
				final String uid = String.valueOf(UserInfoUtil.getCurrentUserId());
				final PackageInfo info = mManager
						.getPackageInfo(packageName, 0);
				new Thread() {
					@Override
					public void run() {
						if (mDownloadDao.isHasInfo(info.packageName,
								info.versionName)) {
							DownloadItem item = mDownloadDao.getDowloadItem(
									info.packageName, info.versionName);
							item.setmState(DownloadParam.C_STATE.INSTALLED);
							log.d(" mGameDownloadRecordDao.insert " + info.packageName);
							mGameDownloadRecordDao.insert(info.packageName,uid);
					        Intent installIntent = new Intent(Contants.ACTION.GAME_DOWNLOAD_AND_INSTALL);
					        mContext.sendBroadcast(installIntent);
							// mDownloadDao.delete(info.packageName,info.versionName);
							// 更新数据库状态
							mDownloadDao.updateItem(item);
							File file = new File(item.getPath());
							if (file.exists()) {
								if (file.delete()) {
									log.d(packageName + " File delete success");
								}
							}
						}
						Message msg=new Message();
						msg.obj=packageName;
						msg.what=1;
						myHandler.sendMessage(msg);
					};
				}.start();
				// 将该游戏添加到“我的游戏”DB中
				getGameCode(info.packageName, info.versionName);
			} catch (Exception e) {
				log.e(e);
			}
		}
	}

	private void getGameCode(String packageName, String versionName) {
		try {
			JSONArray array = new JSONArray();
			JSONObject obj = new JSONObject();
			obj.put("identifier", packageName);
			obj.put("version", versionName);
			array.put(obj);
			RequestParams params = new RequestParams();
			params.put("games", array.toString());
			MyHttpConnect.getInstance().post(HttpContants.NET.GET_GAME_CODE,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
						}
						
						@Override
						public void onSuccess(int statusCode, String content) {
							if (TextUtils.isEmpty(content)) {
								return;
							}
							log.i("get gameCode result = " + content);
							try {
								GameCodeBase base = new ObjectMapper()
										.configure(
												DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
												false)
										.readValue(
												content,
												new TypeReference<GameCodeBase>() {
												});
								if (base == null || base.getData().isEmpty()) {
									log.e("base is null");
									return;
								}
								GameCodeInfo info = base.getData().get(0);
								if (info == null) {
									log.e("info is null");
									return;
								}
								getGameInfo(info);
							} catch (Exception e) {
								log.e(e);
							}
						}
					});
		} catch (Exception e) {
			log.e(e);
		}
	}

	private void getGameInfo(GameCodeInfo info) {
		RequestParams params = new RequestParams();
		params.put("gamecode", info.getGamecode());
		MyHttpConnect.getInstance().post(HttpContants.NET.MY_GAME, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							log.d("get Game Info result = " + content);
							MyGameBase base = new ObjectMapper()
									.configure(
											DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
											false).readValue(content,
											new TypeReference<MyGameBase>() {
											});
							if (base == null) {
								return;
							}
							List<GameItem> list = base.getData();
							if (list == null || list.isEmpty()) {
								return;
							}
							GameItem item = list.get(0);
							ContentValues values = new ContentValues();
							values.put(DBHelper.MYGAME.FULL_SIZE, item.getFullsize());
							values.put(DBHelper.MYGAME.FULL_URL, item.getFullurl());
							values.put(DBHelper.MYGAME.GAME_CODE, item.getGamecode());
							values.put(DBHelper.MYGAME.GCACTS, item.getGcacts());
							values.put(DBHelper.MYGAME.ICON, item.getIcon());
							values.put(DBHelper.MYGAME.IS_SHOWN, "1");
							values.put(DBHelper.MYGAME.NAME, item.getName());
							values.put(DBHelper.MYGAME.PACKAGE_NAME, item.getIdentifier());
							values.put(DBHelper.MYGAME.PLAYER_NUM, item.getFrdplay());
							values.put(DBHelper.MYGAME.STAR, item.getStar());
							values.put(DBHelper.MYGAME.TIME_STAMP, System.currentTimeMillis());
							values.put(DBHelper.MYGAME.VERSION, item.getVersion());
							values.put(DBHelper.MYGAME.VERSION_CODE, item.getVersioncode());
							values.put(DBHelper.MYGAME.LOCAL_VERSION, String.valueOf(Util.getAppVersionCode(item.getIdentifier())));
							mContext.getContentResolver().insert(Uri.parse(MyGameProvider.URI), values);
//							mMyGameDao.insert(item);
//							mMyGameDao.update(item.getIdentifier(), System.currentTimeMillis());
				        	mContext.sendBroadcast(new Intent(Contants.ACTION.INSTALL));
							
							
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
}
