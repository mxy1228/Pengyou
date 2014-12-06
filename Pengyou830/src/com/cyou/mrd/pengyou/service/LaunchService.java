package com.cyou.mrd.pengyou.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.RootWorker;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.entity.PrivateMessage;
import com.cyou.mrd.pengyou.entity.PrivateMesssageContent;
import com.cyou.mrd.pengyou.entity.ScoreRuleItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.UnreadUserLetterInfo;
import com.cyou.mrd.pengyou.entity.UserInfo;
import com.cyou.mrd.pengyou.entity.base.PrivateMessageBase;
import com.cyou.mrd.pengyou.entity.base.ScoreRuleBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.log.MsgLog;
import com.cyou.mrd.pengyou.utils.FileUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 处理lauchactivity中初始化相关数据
 * 
 * @author wangkang
 * 
 */
public class LaunchService extends IntentService {
	private LetterDao mLetterDao;
	
	public LaunchService(String name) {
		super(name);
	}

	public LaunchService() {
		super("LaunchService");
	}

	private CYLog log = CYLog.getInstance();
	private Context mContext;
	public static boolean mFeedback = true;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mContext = this;
		mLetterDao = new LetterDao(this);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		initData();
	}

	private void initData() {
		/**
		 * 后台统计日志-记录开启
		 */
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.BEHAVIOR_TIMES, CYSystemLogUtil.TIMES);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		loadUserInfo();// 加载个人信息
		//获取个人信息完成度能获取积分个数
		getCMSConfigureScore();
		openApp();
		// 删除自更新任务记录
		startAsync();// 同步好友
		getUnreadMsg();// 从服务器获取未读私信
		initNetConfigPath();// 获取ip配置信息
		filterGame();// 上传游戏
		countUserAndLetter();//统计含有未读私信的用户及私信数目
	//	FileUtil.uploadLogFile();// 上传日志文件        1127版本暂时关闭
	}

	private void countUserAndLetter(){
		UnreadUserLetterInfo info = new LetterDao(LaunchService.this).countUnreadLetterAndUser();
		if(info == null){
			return;
		}
		
	}
	/**
	 * 判断用户是否登陆
	 * 
	 * @return
	 */
	public boolean checkUserHasLogin() {
		String token = UserInfoUtil.getUauth();
		if (!TextUtils.isEmpty(token)) {
			return true;
		}
		return false;
	}

	/**
	 * 进入主页之前加载个人信息
	 */
	private void loadUserInfo() {
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.USER_INFO, params,
				new JsonHttpResponseHandler() {

					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
					}

					@Override
					public void onSuccess(JSONObject response) {
						log.d("get userinfo is:" + response.toString());
						if (response.has("data")) {
							try {
								UserInfo userInfo = JsonUtils.fromJson(
										response.getString("data"),
										UserInfo.class);
								if (null != userInfo) {
									// 删除SP
									String uauth=UserInfoUtil.getUauth();
									boolean isUpdateSign=false;
									if(SharedPreferenceUtil.hasUserSignUpdate()){//先判断清空用户之前有没有编辑过签名
										isUpdateSign=true;
									}
//									SharedPreferences userInfoSp = getSharedPreferences(
//											Contants.SP_NAME.USER_INFO, Context.MODE_PRIVATE);
//									Editor e_userInfo = userInfoSp.edit();
//									e_userInfo.clear();
//									e_userInfo.commit();
									if(!TextUtils.isEmpty(uauth)){
										UserInfoUtil.setUauth(uauth, false);
									}
									UserInfoUtil.saveUserInfo(userInfo);
									if(isUpdateSign){//若编辑过 则更新sp 否则会影响用户进度条的显示
										SharedPreferenceUtil.upadateUserSign();
									}
									//获取未读私信数并保存
									SystemCountMsgItem.clearUnreadLetterCount();
									int unreadLetter = mLetterDao.countUnread();
									SystemCountMsgItem.changeUnreadLetterCount(unreadLetter, LaunchService.this);
//									LetterSPUtil.saveTotalUnreadCount(unreadLetter,LaunchService.this);
//									List<ConversationItem> items = mLetterDao.selectConversations();
//									LetterSPUtil.saveConversations(items,LaunchService.this);
									//更新用户个人信息
									Intent mIntent=new Intent(Contants.ACTION.UPDATE_PERSONAL_INFO);//发送广播
									sendStickyBroadcast(mIntent);
									startCoreService();// 启动CoreService开启推送服务
									if (!TextUtils.isEmpty(userInfo
											.getPicture())) {
										final String imageUrl = PYVersion.IP.IMG_HOST
												+ userInfo.getPicture();
										final String filePath = UserInfoUtil
												.getUserIconPath();
										new Thread(new Runnable() {

											@Override
											public void run() {
												FileUtil.downloadBitmap(
														imageUrl, filePath);
											}
										}).start();
									}
								}
							} catch (Exception e) {
								log.e(e);
							}
						}
					}

				});
	}

	private void getCMSConfigureScore() {
		RequestParams personparams = new RequestParams();
		personparams.put("act",
				String.valueOf(Contants.SCORE_ACTION.QUERY_EXCHANGE_RULES));
		MyHttpConnect.getInstance().post(HttpContants.NET.SCORE_ACTION,
				personparams, new AsyncHttpResponseHandler() {

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							ScoreRuleBase base = new ObjectMapper()
									.configure(
											DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES,
											false).readValue(content,
											new TypeReference<ScoreRuleBase>() {
											});

							if (base == null) {
								return;
							}
							List<ScoreRuleItem> list = base.getData();
							if (list != null && !list.isEmpty()) {
								for (ScoreRuleItem item : list) {
									if (item.getExid() == Contants.SCORE_ACTION.EXCHANGE_PERSON_INFO_SCORE) {
										UserInfoUtil.setCMSPersonalInfoScore(item
												.getScore());
									} else if(item.getExid() == Contants.SCORE_ACTION.EXCHANGE_SHARE_SINA_SCORE){
										UserInfoUtil.setCMSShareSinaScore(item
												.getScore());
									}
								}
							}

						} catch (Exception e) {
							log.e(e);
						}
					}

					@Override
					public void onFailure(Throwable error) {
						if (PYVersion.DEBUG) {
							log.i("jifen 积分规则下载失败 ");
						}
					}

				});
	}

	private void startCoreService() {
		Intent intent = new Intent(this, CoreService.class);
		intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.JOIN);
		intent.putExtra(Params.PUBLISH.UTOKEN, UserInfoUtil.getUToken());
		startService(intent);
	}

	/**
	 * 上传游戏
	 */
	public void filterGame() {
		try {
			SharedPreferenceUtil.mustWaitFilterGameDown();
			RequestParams params = new RequestParams();
			params.put("games", RootWorker.getGameList());
			MyHttpConnect.getInstance().post(HttpContants.NET.SEND_APP_LIST,
					params, new AsyncHttpResponseHandler() {
						@Override
						public void onSuccess(String content) {
							//如果本地游戏上传成功的话，刷新我的游戏列表
							sendStickyBroadcast(new Intent(Contants.ACTION.FILTER_GAME_FINISHED));
						}

						@Override
						public void onFailure(Throwable error) {
							//如果本地游戏上传失败的话，我的游戏列表加载之前的游戏
							sendStickyBroadcast(new Intent(Contants.ACTION.FILTER_GAME_FINISHED));
						}
					});
		} catch (Exception e) {
			log.e(e);
		}
	}

	/**
	 * 获取未读消息
	 */
	private void getUnreadMsg() {
		final FriendDao friendDao = new FriendDao(this);
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_UNREAD_LETTER,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						if (TextUtils.isEmpty(content)) {
							return;
						}
						MsgLog.getInstance().v("获取未读消息 = " + content);
						try {
							PrivateMessageBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<PrivateMessageBase>() {
											});
							if (base != null) {
								List<PrivateMessage> data = base.getData();
								LetterDao dao = new LetterDao(mContext);
								for (PrivateMessage msg : data) {
									PrivateMesssageContent c = new ObjectMapper().readValue(
											msg.getContent(),
											new TypeReference<PrivateMesssageContent>() {
											});
									MyMessageItem item = new MyMessageItem();
									item.setAvatar(msg.getAvatar());
									item.setNickname(msg.getNickname());
									item.setCreatetime(msg.getTimestamp());
									item.setFrom(msg.getFromuid());
									item.setTo(msg.getTouid());
									item.setTauid(msg.getFromuid());
									if (msg.getType().equals(
											Contants.UNREAD_MSG_TYPE.TEXT)) {
										item.setType(Contants.CHAT_TYPE.TEXT);
										item.setContent(c.getText());
										dao.insert(item);
									} else if (msg.getType().equals(
											Contants.UNREAD_MSG_TYPE.GAME)) {
										item.setType(Contants.CHAT_TYPE.GAME);
										item.setGamecode(c.getGamecode());
										dao.insert(item);
									} else if (msg
											.getType()
											.equals(Contants.UNREAD_MSG_TYPE.ADD_FRIEND)) {
										if (friendDao.contain(msg.getFromuid())) {
											return;
										}
										FriendItem i = new FriendItem();
										i.setUid(msg.getFromuid());
										friendDao.insertFriend(i,System.currentTimeMillis());
									} else {
										friendDao.deleteFriend(msg.getFromuid());
									}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}
	

	/**
	 * 同步好友列表
	 * xumengyang
	 */
	private void startAsync() {
		final FriendDao dao = new FriendDao(getApplicationContext());
		RequestParams params = new RequestParams();
		params.put("page", "1");
		params.put("count", "20");
//		MyHttpConnect.getInstance().post(HttpContants.NET.GET_FRIEND_UIDS,
				MyHttpConnect.getInstance().post(HttpContants.NET.MYFRIEND_LIST,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, final String content) {
//						log.i("Async Friends result = " + content);
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
									

								} catch (Exception e) {
									log.e(e);
								}
							}
						}.start();
						
					}
				});
		
	}

//	/**
//	 * 同步好友信息
//	 * xumengyang
//	 * @param newList
//	 */
//	private void syncFriendInfo(List<Integer> newList){
//		JSONArray array = new JSONArray();
//		for(Integer i : newList){
//			array.put(i);
//		}
//		RequestParams p = new RequestParams();
//		p.put("frdids", array.toString());
//		MyHttpConnect.getInstance().post(HttpContants.NET.MYFRIEND_LIST, p,
//				new AsyncHttpResponseHandler() {
//
//					@Override
//					public void onFailure(Throwable error, String content) {
//						// TODO Auto-generated method stub
//						super.onFailure(error, content);
//					}
//
//					@Override
//					public void onStart() {
//						// TODO Auto-generated method stub
//						super.onStart();
//					}
//
//					@Override
//					public void onSuccess(int statusCode, String content) {
//						log.i("同步好友信息=" + content);
//						List<FriendItem> list = JsonUtils.json2List(
//								JsonUtils.getJsonValue(content, "data"),
//								FriendItem.class);
//						if (list != null) {
//							new FriendDao(mContext).insertFriends(list);
//						}
//					}
//				});
//	}
//	
	
	/**
	 * 初始化网络配置相关
	 * 意见反馈开关+礼包开关
	 */
	private void initNetConfigPath() {
		if (PYVersion.DEBUG) {
			return;
		}
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.NET_CONTANS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(String content) {
						log.e("++++++++++++++++++++++++++++++++++++++++++++++++  content:" +content);
						try {
							String data = JsonUtils.getJsonValue(content,
									"data");
							String userImageUrl = JsonUtils.getJsonValue(data,
									"userimageurl");
							String gameImageUrl = JsonUtils.getJsonValue(data,
									"gameimageurl");
							mFeedback = JsonUtils.getJsonValue(data,
									"feedbackdisp").equals("1");
							if (!TextUtils.isEmpty(userImageUrl)
									&& !TextUtils.isEmpty(gameImageUrl)) {
								PYVersion.IP.ICON_HOST = gameImageUrl;
								PYVersion.IP.IMG_HOST = userImageUrl;
							}
						} catch (Exception e) {
							log.e(e);
						}
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
					}
				});
		
	}

	
	
	private void openApp() {
		RequestParams params = new RequestParams();
		params.put("area", UserInfoUtil.getCurrentAreaId());
		params.put("country", Locale.getDefault().getCountry());
		params.put("device", Util.getPhoneUA());
		params.put("downloadType",CyouApplication.getChannel());
		params.put("deviceSystem", Util.getSystemVersion());
		params.put("networkType", Util.getNetType(CyouApplication.mAppContext));
		params.put("prisonBreak", Util.hasRootPer() == true ? "1" : "0");
		if (!TextUtils.isEmpty(Util
				.getNetExtraInfo(CyouApplication.mAppContext))) {
			String[] netInfoArr = Util.getNetExtraInfo(
					CyouApplication.mAppContext).split("_");
			if (netInfoArr != null && netInfoArr.length == 2) {
				params.put("operatorId", netInfoArr[0]);
				params.put("operator", netInfoArr[1]);
			}
		}
		MyHttpConnect.getInstance().post(HttpContants.NET.APPACTIVE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error, String content) {
						log.d("app active error is:" + content);
						super.onFailure(error, content);
					}

					@Override
					public void onSuccess(String content) {
						log.d("app active is:" + content);
						super.onSuccess(content);

					}
				});
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}
}
