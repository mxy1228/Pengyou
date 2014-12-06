package com.cyou.mrd.pengyou.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.bayeux.client.ClientSessionChannel.MessageListener;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.json.JSONArray;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.PYVersion;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.FriendDao;
import com.cyou.mrd.pengyou.db.LetterDBHelper;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.ConversationItem;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.UnreadUserLetterInfo;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.MsgLog;
import com.cyou.mrd.pengyou.service.MessageManager.WriteDBListener;
import com.cyou.mrd.pengyou.ui.ChatActivity;
import com.cyou.mrd.pengyou.ui.MessageCenterActivity;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class CoreService extends Service implements WriteDBListener{
	private MsgLog log = MsgLog.getInstance();

	private static final int OPERATE_NOTIFICATION = 5;//运营推送
	private static final int BE_FRIEND = 4;
	private static final int NOTIFICATION = 1;
	private static final int DEBUG_MSG = 2;
	private static final int LOGIN_OUT = 1;

	private BayeuxClient mClient;
	private ChatListsner chatListener = new ChatListsner();
	public String CHANNEL_SYSTEM_MSG = "/pengyou/msguser/19be46ab76515c10";// 系统消息推送频道
//	public final String SERVICE_MEMBER = "/service/members";// 私信功能- 加入聊天室
	public final String SERVICE_PRIVATECHATE = "/service/privatechat";// 发送私信
	private static final String RELATION_CHANGE = "/pengyou/useraction/19be46ab76515c10";// 关系
	private static final String REPLY = "/service/privatechatreply";
	private static final String SYSTEM = "/system";//登录互斥
	private static final String CHAT_DEMO = "/chat/demo";//接收私信 
	private LetterDao mLetterDao;
	private FriendDao mFriendDao;
	private NotificationManager mNManager;
	private AudioManager mAudioManager;
	private boolean mHadConnect2Server = false;
	private boolean mConnecting = false;
	private MessageManager mMessageManager;
	private String mUToken;
	private boolean mBinding = false;
	private boolean mFromLogin = false;

//	private ClientSessionChannel serviceMemberChannel;//聊天室Channel
	private ClientSessionChannel chatPrivateChannel;//聊天Channel
	private ClientSessionChannel systemMsgChannel;//系统信息Channel
	private ClientSessionChannel relationChannel;//关系Channel
	private ClientSessionChannel chatDemo;//
	private ClientSessionChannel replyChannel;//回应服务器
	private ClientSessionChannel mSystemChannel;//登录互斥
	
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() { 
		super.onCreate();
		log.v("CoreService create");
		mLetterDao = new LetterDao(this);
		mFriendDao = new FriendDao(this);
		if (mNManager == null) {
			mNManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		}
		if(mAudioManager == null){
			mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		}
		if(mMessageManager == null){
			mMessageManager = MessageManager.getInstance(this);
			mMessageManager.setWriteDBListener(this);
		}
	}

	@Override
	public void onStart(Intent intent, int startId) {
		super.onStart(intent, startId);
		if (intent == null) {
			log.v("intent is null");
			return;
		}
		int type = intent.getIntExtra(Params.PUBLISH.ACTION, 0);
		if (type == 0) {
			log.v("type = 0");
			return;
		}
		mFromLogin = intent.getBooleanExtra(Params.PUBLISH.LOGIN, false);
		switch (type) {
		case Contants.PUBLISH_ACTION.CHAT:
			final int to_uid = intent.getIntExtra(Params.CHAT.FROM, 0);
			final int tag = intent.getIntExtra(Params.CHAT.TAG, 1);
			final String gamecode = intent.getStringExtra(Params.CHAT.GAMECODE);
			final String msg = intent.getStringExtra(Params.CHAT.MSG);
			final long time = intent.getLongExtra(Params.CHAT.TIME, 0);
			new Thread() {
				public void run() {
					initChatPrivate(to_uid, msg, tag, gamecode,time);
				};
			}.start();
			break;
		case Contants.PUBLISH_ACTION.JOIN:
			if(mClient != null && mClient.isConnected()){
				log.v("已经连接消息服务器");
				return;
			}
			//启动推送服务
			log.v("开始连接消息服务器");
			mUToken = intent.getStringExtra(Params.PUBLISH.UTOKEN);
			if(mUToken == null || TextUtils.isEmpty(mUToken)){
				log.v("mUToken is null");
			}else{
				mUToken = UserInfoUtil.getUToken();
			}
			startPushMsgCenter();
			break;
		case Contants.PUBLISH_ACTION.CHANGE_BIND_ING:
			mBinding = intent.getBooleanExtra(Params.PUBLISH.STATE, false);
			break;
		default:
			break;
		}
	}
	
	/*
	 * 启动消息推送服务
	 */
	private void startPushMsgCenter() {
		log.v("startPushMsgCenter");
		if(mConnecting){
			return;
		}
		new Thread(){
			public void run() {
				try {
					mConnecting = true;
					if(mClient != null){
						mClient.disconnect();
						mClient = null;
					}
					mClient = new BayeuxClient(PYVersion.IP.PUSH_URL,
							LongPollingTransport.create(null));
					mClient.setDebugEnabled(true);
//					mClient.getChannel(Channel.META_HANDSHAKE).addListener(
//							new InitializerListener());
					mClient.getChannel(Channel.META_CONNECT).addListener(
							new ConnectListener());
					initialize();
					Map<String,Object> params = new HashMap<String, Object>();
					if(mUToken == null){
						mUToken = UserInfoUtil.getUToken();
					}
					int uid = UserInfoUtil.getCurrentUserId();
					if(mUToken != null && uid!=0){
						log.v("连接消息服务器的utoken = "+mUToken);
						params.put("utoken", mUToken);
						params.put("uid", String.valueOf(UserInfoUtil.getCurrentUserId()));
						mClient.handshake(params);
						mHadConnect2Server = mClient.waitFor(3000, BayeuxClient.State.CONNECTED);
						android.os.Message msg = mHandler.obtainMessage();
						msg.what = DEBUG_MSG;
						if (!mHadConnect2Server) {
							if (PYVersion.DEBUG) {
								msg.obj = "DEBUG:握手失败";
							}
							log.v("Could not handshake with server");
						} else {
							if (PYVersion.DEBUG) {
								msg.obj = "DEBUG:握手成功";
							}
							log.v("success ");
						}
						mHandler.sendMessage(msg);
						mConnecting = false;
					}else{
						log.v("mUToken = "+mUToken+" uid = "+uid+" mUToken is null or empty");
					}
				} catch (Exception e) {
					log.v(e);
				}finally{
					mConnecting = false;
				}
			};
		}.start();
	}
	
	private void initialize() {
		log.v("开始初始化消息频道");
		if(systemMsgChannel != null){
			systemMsgChannel.unsubscribe();
		}
		if(relationChannel != null){
			relationChannel.unsubscribe();
		}
		if(chatPrivateChannel != null){
			chatPrivateChannel.unsubscribe();
		}
		if(chatDemo != null){
			chatDemo.unsubscribe();
		}
		if(replyChannel != null){
			replyChannel.unsubscribe();
		}
		if(mSystemChannel != null){
			mSystemChannel.unsubscribe();
		}
		systemMsgChannel = mClient.getChannel(CHANNEL_SYSTEM_MSG
				+UserInfoUtil.getCurrentUserId());// 系统推送
		systemMsgChannel.addListener(new SystemMessageListener());
//		systemMsgChannel.subscribe(new SystemMessageListener());
		relationChannel = mClient.getChannel(RELATION_CHANGE
				+UserInfoUtil.getCurrentUserId());// 关系改变
		relationChannel.addListener(new RelationListener());
//		relationChannel.subscribe(new RelationListener());
		mSystemChannel = mClient.getChannel(SYSTEM);
		mSystemChannel.addListener(new SystemListener());
//		mSystemChannel.subscribe(new SystemListener());
		// 首先加入聊天室,才可和某人聊天
		chatPrivateChannel = mClient.getChannel(SERVICE_PRIVATECHATE);
		chatDemo = mClient.getChannel(CHAT_DEMO);
		chatDemo.addListener(chatListener);
//		chatDemo.subscribe(chatListener);
		replyChannel = mClient.getChannel(REPLY);
		log.v("初始化消息频道完成");
	}

	
	/**
	 * 加入聊天室初始化
	 */
//	private void initJoinChat(String userID) {
//		HashMap<String, String> maps = new HashMap<String, String>();
//		maps.put("room", CHAT_DEMO);
//		maps.put("user", "user"+userID);
//		serviceMemberChannel.publish(maps, new MessageListener() {
//			@Override
//			public void onMessage(ClientSessionChannel arg0, Message arg1) {
//				if (arg1.isSuccessful()) {// 加入聊天室成功
//					log.v("加入聊天室成功");
//				}
//			}
//
//		});
//	}

	/**
	 * 登录互斥监听
	 * @author xumengyang
	 *
	 */
	private class SystemListener implements ClientSessionChannel.MessageListener{

		@Override
		public void onMessage(ClientSessionChannel arg0, Message arg1) {
			if(mBinding){
				log.v("mChangBinding");
				return;
			}
			log.v("接收到登录互斥消息 = " + arg1.getData().toString());
			log.v("当前utoken = "+UserInfoUtil.getUToken());
			Map<String, Object> data = arg1.getDataAsMap();
			if(data.containsKey("tag")){
				int tag = Integer.parseInt(data.get("tag").toString());
				if(tag == LOGIN_OUT){
					Intent intent = new Intent(Contants.ACTION.FORCE_LOGIN_OUT);
					sendBroadcast(intent);
				}
			}
		}
		
	}
	
	/**
	 * 初始化
	 * 
	 * @author wangkang
	 * 
	 */
//	private class InitializerListener implements
//			ClientSessionChannel.MessageListener {
//		@Override
//		public void onMessage(ClientSessionChannel arg0,
//				org.cometd.bayeux.Message arg1) {
//			log.v("InitializerListener onMessage " + arg0.toString() + "____"
//					+ arg1.toString());
//			if(arg1.isSuccessful()){
//				log.v("初始化成功");
//				initialize();
//			}
//		}
//	}

	/**
	 * 发送私信
	 * 
	 * @param peerId
	 *            私聊用户id
	 * @param chatContent
	 *            私聊内容
	 */
	private void initChatPrivate(int peerId, String chatContent, int tag,
			String gamecode,long time) {
		final HashMap<String, String> maps = new HashMap<String, String>();
		maps.put("room", CHAT_DEMO);
		maps.put("chat", chatContent);
		maps.put("user","user"+String.valueOf(UserInfoUtil.getCurrentUserId()));
		maps.put("peer","user"+String.valueOf(peerId));
		maps.put("tag", String.valueOf(tag));// 区分消息格式
		maps.put("gamecode", gamecode);
		maps.put("time", String.valueOf(time));
		if((chatPrivateChannel == null && mClient == null) || !mClient.isConnected()){
			log.v("服务器连接断开");
			MyMessageItem item = new MyMessageItem();
			item.setFrom(UserInfoUtil.getCurrentUserId());
			item.setTo(peerId);
			item.setType(tag);
			item.setGamecode(gamecode);
			item.setCreatetime(time);
			item.setContent(chatContent);
			startPushMsgCenter();
			return;
		}
		chatPrivateChannel.publish(maps, new MessageListener() {
			
			@Override
			public void onMessage(ClientSessionChannel arg0, Message arg1) {
				log.v("chat onMessage = "+arg1.toString());
				if(!arg1.isSuccessful()){
					log.v("发送失败，尝试重新发送");
//					mClient.getChannel(SERVICE_PRIVATECHATE).publish(maps);
				}else{
					log.v("发送成功");
				}
			}
		});
	}

	/**
	 * 系统推送
	 * 
	 * @author wangkang A|1|B|2|C|3|D|4|E|5|F|6|G|7 A : 关注 B : 粉丝 C : 动态 D : 私信
	 *         E : 新的朋友 F : 关系圈 G : 官方动态
	 */
	private class SystemMessageListener implements
			ClientSessionChannel.MessageListener {

		@Override
		public void onMessage(ClientSessionChannel arg0,
				org.cometd.bayeux.Message arg1) {

			if (arg1 != null) {
				Map<String, Object> hashMap = arg1.getDataAsMap();
				if (hashMap != null) {
					log.v("push system msg is:" + hashMap.toString());
//					hashMap.clear();
//					hashMap.put("actiontype", "newcontactnotify");
//					hashMap.put("newcontacts", "1");
					SystemCountMsgItem.save(hashMap,mFromLogin);
					mFromLogin = false;
					Util.requestFansAndLetterCount(CoreService.this,hashMap.get("actiontype").toString());
				}
			}
		}
	}

	/**
	 * 好友关系改变 receiver:"5" // 被删除者，接收消息者 sponsor:"2" // 发起这个消息的uid, 删除动作发起者
	 * actiontype : frdadd / frddel / frdremove ciper : "2 del 5"
	 * 
	 * @author xumengyang
	 * 
	 */
	private class RelationListener implements
			ClientSessionChannel.MessageListener {

		@Override
		public void onMessage(ClientSessionChannel arg0, Message arg1) {
			if (arg1 == null) {
				return;
			}
			log.v("接到关系改变推送消息 = " + arg1.getData().toString());
			Map<String, Object> data = arg1.getDataAsMap();
			int status = Integer.valueOf(data.get("status").toString());
			int sponsor = Integer.valueOf(data.get("sponsor").toString());// 主动发起关系改变一方
			int receiver = Integer.valueOf(data.get("receiver").toString());// 被动接受关系改变一方
			int myUid = UserInfoUtil.getCurrentUserId();
			switch (status) {
			case BE_FRIEND:
				if (myUid != sponsor) {
					if (!mFriendDao.contain(sponsor)) {
						getUserInfo(sponsor);
					}
				} else if (myUid != receiver) {
					if (!mFriendDao.contain(receiver)) {
						getUserInfo(receiver);
					}
				}
				break;
			default:
				if (sponsor != myUid) {
					new FriendDao(CoreService.this).deleteFriend(sponsor);
				} else if (receiver != myUid) {
					new FriendDao(CoreService.this).deleteFriend(receiver);
				}
				break;
			}
			android.os.Message msg = mHandler.obtainMessage();
			msg.what = DEBUG_MSG;
			msg.obj = arg1.getData().toString();
			mHandler.sendMessage(msg);
		}

	}

	/**
	 * 获取用户相关信息
	 */
	private void getUserInfo(final int uid) {
		RequestParams params = new RequestParams();
		String u = new JSONArray().put(uid).toString();
		params.put("frdids", u);
		MyHttpConnect.getInstance().post(HttpContants.NET.MYFRIEND_LIST, params,new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
//					Log.e("CoreService", "getUserInfo onSuccess:" + content);
					String data = JsonUtils.getJsonValue(content,"data");
					if (TextUtils.isEmpty(data)) {
						return;
					}
					List<FriendItem> list = JsonUtils.json2List(data,
							FriendItem.class);
					if (list != null) {
						new FriendDao(CoreService.this).insertOrUpdateFriends(list);
					}
				} catch (Exception e) {
					log.v(e);
				}
			}
		});
	}

	/**
	 * 接受到私信消息
	 * 
	 * @author wangkang
	 * 
	 */
	private class ChatListsner implements ClientSessionChannel.MessageListener {

		@Override
		public void onMessage(ClientSessionChannel arg0,
				org.cometd.bayeux.Message arg1) {
			log.v("收到私信消息"+arg1.toString());
			if (null != arg1.getData()) {
				Map<String, Object> data = arg1.getDataAsMap();
				//TODO 拼接的假数据
//				Map<String, Object> data =new HashMap<String, Object>();
//				Map<String, Object> datachat =new HashMap<String, Object>();
//				datachat.put("title", "测试标题");
//				datachat.put("desc", "测试描述测试描述测试描述测试描述测试描述测试描述");
//				datachat.put("pic", "");
//				datachat.put("uri", "com.cyou.mrd.pengyou.ui.MessageCenterActivity");
//				
//				data.put("peer", "user0");
//				data.put("nickname", "朋游用户2090");
//				data.put("tag", "5");
//				data.put("chat", datachat);
//				data.put("user", "user2090");
				
				int tag = Integer.valueOf(data.get("tag").toString());
				MyMessageItem item = new MyMessageItem();
				String chatMsg = String.valueOf(data.get("chat"));
				
				String avatar = String.valueOf(data.get("avatar"));
				String nickname = String.valueOf(data.get("nickname"));
				int uid = Integer.parseInt(data.get("user").toString().replace("user", ""));
				switch (tag) {
				case Contants.CHAT_TYPE.TEXT:
					item.setType(Contants.CHAT_TYPE.TEXT);
					saveToDatabase(chatMsg,avatar,uid,data,arg1,item,nickname);
					break;
				case Contants.CHAT_TYPE.GAME:
					if(data.containsKey("gamecode") && data.get("gamecode")!= null){
						String gamecode = data.get("gamecode").toString();
						item.setType(Contants.CHAT_TYPE.GAME);
						item.setGamecode(gamecode);
					}
					saveToDatabase(chatMsg,avatar,uid,data,arg1,item,nickname);
					break;
				case Contants.CHAT_TYPE.TO_BE_FRIEND:
					chatMsg = getString(R.string.we_are_friends);
					int to = Integer.parseInt(data.get("user").toString().replace("user", ""));
					if(to != UserInfoUtil.getCurrentUserId()){
						saveToDatabase(chatMsg,avatar,uid,data,arg1,item,nickname);
					}
					break;
				case Contants.CHAT_TYPE.GIFT:
					Intent intent = new Intent(Contants.ACTION.GIFT);
					intent.putExtra("show", true);
					sendBroadcast(intent);
					saveToDatabase(chatMsg,avatar,uid,data,arg1,item,nickname);
					break;
				case Contants.CHAT_TYPE.OPERATE://运营推送消息
					//TODO
					chatTypeOperate(data);
					break;
				default:
					break;
				}
			
			}
		}

	}
	/**
	 * 运营推送解析
	 * @param data 
	 */
	private void chatTypeOperate(Map<String, Object> data){
		Map<String, Object> chatMsgOperate = (Map<String, Object>)data.get("chat");
		String title = String.valueOf(chatMsgOperate.get("title"));
		String desc = String.valueOf(chatMsgOperate.get("desc"));
		String pic =String.valueOf(chatMsgOperate.get("pic"));
		String uri = String.valueOf(chatMsgOperate.get("uri"));
		android.os.Message mMessage = mHandler.obtainMessage();
		mMessage.what = OPERATE_NOTIFICATION;
		Bundle b = new Bundle();
		b.putString("title", title);
		b.putString("desc", desc);
		b.putString("pic", pic);
		b.putString("uri", uri);
		mMessage.setData(b);
		mHandler.sendMessage(mMessage);
	}
	/**
	 * 保存消息推送到数据库
	 * @param chatMsg
	 * @param avatar
	 * @param uid
	 * @param data
	 * @param arg1
	 * @param item
	 * @param nickname
	 */
	public void saveToDatabase(String chatMsg,String avatar,int uid,Map<String, Object> data,org.cometd.bayeux.Message arg1,MyMessageItem item,String nickname){
		
		if (uid == UserInfoUtil.getCurrentUserId()) {
			long time = Long.valueOf(data.get("time").toString());
			int to = Integer.parseInt(data.get("peer").toString().replace("user", ""));
			String msgseq = arg1.get("msgseq").toString();
			mLetterDao.updateSendResult(time, LetterDBHelper.SEND_STATE.SEND_SUCCESS, to,msgseq);
			item.setMsgseq(msgseq);
			Intent intent = new Intent(Contants.ACTION.NEW_CHAT);
			intent.putExtra(Params.CHAT.FROM, uid);
			intent.putExtra(Params.CHAT.TO, to);
			intent.putExtra(Params.CHAT.ITEM, item);
			intent.putExtra(Params.CHAT.TIME, time);
			sendOrderedBroadcast(intent, null);
			sendBroadcast(intent);
		}else{
			item.setMsgid(arg1.get("msgseq").toString());
			item.setMsgseq(arg1.get("msgseq").toString());
			item.setFrom(uid);
			item.setTo(UserInfoUtil.getCurrentUserId());
			item.setCreatetime(System.currentTimeMillis());
			item.setContent(chatMsg);
			item.setTauid(uid);
			item.setAvatar(avatar);
			item.setNickname(nickname);
			mMessageManager.receiveMessage(item);
		//	mLetterDao.insert(item);
			//发送广播更新个人中心页面
		//	Intent iSysItem = new Intent(Contants.ACTION.SYSTEM_MSG_ACTION);
		//	sendBroadcast(iSysItem);
			//回复服务器"收到了"
			HashMap<String, String> maps = new HashMap<String, String>();
			maps.put("msgseq", item.getMsgid());
			replyChannel.publish(maps);
		}
	}
	/**
	 * 心跳监听器
	 * @author xumengyang
	 *
	 */
	private class ConnectListener implements
			ClientSessionChannel.MessageListener {

		private boolean connected;
		
		@Override
		public void onMessage(ClientSessionChannel arg0,
				org.cometd.bayeux.Message arg1) {
			log.v("onMessage:"+new SimpleDateFormat("MM-dd HH:mm:ss").format(new Date(System.currentTimeMillis())).toString()+arg1.toString());
			if(mClient != null && mClient.isDisconnected()){
				connected = false;
				return;
			}
			connected = arg1.isSuccessful();
			if(!mConnecting && !connected){
//				systemMsgChannel.unsubscribe();
//				relationChannel.unsubscribe();
//				chatDemo.unsubscribe();
				mClient.disconnect();
				mClient = null;
				startPushMsgCenter();
			}
		}
	}


	private void showLetterNotification(final String content, final int uid,final String nickname) {
		if(!SharedPreferenceUtil.getNotificationSwitch()){
			return;
		}
		log.v("ChatActivity.mUID="+SharedPreferenceUtil.getChatActivityUID());
		log.v("ChatActivity.isShown = "+SharedPreferenceUtil.isChatActivityShown());
		if(SharedPreferenceUtil.isChatActivityShown() && uid == SharedPreferenceUtil.getChatActivityUID()){
			return;
		}
		new Thread(){
			public void run() {
				UnreadUserLetterInfo info = mLetterDao.countUnreadLetterAndUser();
				if(info.getmTotalUnreadLetter() == 0 || info.getmTotalUnreadUser() == 0){
					log.v("info.getmTotalUnreadLetter() = "+info.getmTotalUnreadLetter()+"&info.getmTotalUnreadUser() = "+info.getmTotalUnreadUser());
				}
				android.os.Message msg = mHandler.obtainMessage();
				msg.what = NOTIFICATION;
				msg.obj = info;
				Bundle b = new Bundle();
				b.putString("content", content);
				b.putInt("uid", uid);
				b.putString("nickname", nickname);
				msg.setData(b);
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case DEBUG_MSG:
				if (PYVersion.DEBUG) {
					Toast.makeText(CoreService.this, msg.obj.toString(),
							Toast.LENGTH_LONG).show();
				}
				break;
			case NOTIFICATION:
				UnreadUserLetterInfo info = (UnreadUserLetterInfo)msg.obj;
				Bundle b = msg.getData();
				String content = b.getString("content");
				int uid = b.getInt("uid");
				String nickname = b.getString("nickname");
				Notification mNotification = new Notification();
				PendingIntent pending = null;
				if(info.getmTotalUnreadUser() == 1 || info.getmTotalUnreadUser() == 0){
					Intent intent = new Intent(CoreService.this, ChatActivity.class);
					intent.putExtra(Params.CHAT.FROM, uid);
					intent.putExtra(Params.CHAT.NICK_NAME, nickname);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					pending = PendingIntent.getActivity(CoreService.this, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					mNotification.icon = R.drawable.icon;
					mNotification.tickerText = getString(R.string.letter_notification_tickerText_single, nickname);
					mNotification.when = System.currentTimeMillis();
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.setLatestEventInfo(CoreService.this,
							getString(R.string.letter_notification_tickerText, nickname,info.getmTotalUnreadLetter()), content, pending);
				}else{
					Intent intent = new Intent(CoreService.this,MessageCenterActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					pending = PendingIntent.getActivity(CoreService.this, 0, intent,
							PendingIntent.FLAG_UPDATE_CURRENT);
					mNotification.icon = R.drawable.icon;
					mNotification.tickerText = getString(R.string.letter_notification_tickerText_single, nickname);
					mNotification.when = System.currentTimeMillis();
					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					mNotification.setLatestEventInfo(CoreService.this,
							getString(R.string.app_name),getString(R.string.letter_notification_title,info.getmTotalUnreadUser(),info.getmTotalUnreadLetter()), pending);
				}
				switch (mAudioManager.getRingerMode()) {
				case AudioManager.RINGER_MODE_NORMAL:
					mNotification.defaults = Notification.DEFAULT_SOUND;
					break;
				default:
					mNotification.defaults = Notification.DEFAULT_VIBRATE;
					break;
				}
				mNManager.notify(R.id.letter_notification, mNotification);
				break;
				//TODO
			case OPERATE_NOTIFICATION:
				Bundle mBundle = msg.getData();
				String title = mBundle.getString("title");
				String desc = mBundle.getString("desc");
				String pic = mBundle.getString("pic");
				String uri = mBundle.getString("uri");
				
				Notification mOperateNotification = new Notification();
				Intent intent = new Intent();
				try {
					intent.setClass(CoreService.this, Class.forName(uri));
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				pending = PendingIntent.getActivity(CoreService.this, 0, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				mOperateNotification.icon = R.drawable.icon;
				mOperateNotification.when = System.currentTimeMillis();
				mOperateNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mOperateNotification.setLatestEventInfo(CoreService.this,title,desc, pending);
				//此处的0需要更改，更改为唯一标示值，参考上面
				mNManager.notify(R.id.letter_notification, mOperateNotification);
				
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onDestroy() {
		if(mClient != null){
			log.v("CoreService onDestroy");
			systemMsgChannel.unsubscribe();
			relationChannel.unsubscribe();
			chatDemo.unsubscribe();
			mClient.disconnect();
			mClient = null;
		}
		super.onDestroy();
		
	}

	@Override
	public void insertDBComplete(int count, ConversationItem item) {
		
		if(SharedPreferenceUtil.isChatActivityShown() && item.getUid() == SharedPreferenceUtil.getChatActivityUID()){
			//TODO
		}else{
			//更新私信未读数
			SystemCountMsgItem.changeUnreadLetterCount(count, this);
		}
		showLetterNotification(item.getLastLetter(), item.getUid(), item.getNickname());
		Intent intent = new Intent(Contants.ACTION.NEW_CONVERSATION);
		intent.putExtra(Params.CHAT.FROM, item.getUid());
		intent.putExtra(Params.CHAT.TO, UserInfoUtil.getCurrentUserId());
		intent.putExtra(Params.CHAT.ITEM, item);
		intent.putExtra(Params.CHAT.TIME, item.getTime());
		sendOrderedBroadcast(intent, null);
	}
}
