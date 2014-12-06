package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONArray;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.CyouApplication;
import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ChatAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.LetterDBHelper;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.MyMessageItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.UserSimpleInfo;
import com.cyou.mrd.pengyou.entity.base.UserSimpleInfoBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.service.CoreService;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.ChatImmeLayout;
import com.cyou.mrd.pengyou.widget.ChatImmeLayout.ImmeListener;
import com.cyou.mrd.pengyou.widget.ChatListView;
import com.cyou.mrd.pengyou.widget.ChatListView.LoadListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class ChatActivity extends BaseActivity implements OnClickListener,LoadListener{

	private CYLog log = CYLog.getInstance();

	private static final int LOAD_CHAT_COMPLETE = 2;
	private static final long TIME = 1 * 1000;//循环间隔
	public static final int CHANGE_CURSOR = 1;
	public static final int RE_SEND = 3;
	private static final int NOTIFY_LIST = 4;
	
	private ChatListView mListView;
	private EditText mET;
	private Button mSendBtn;
	private ImageButton mBackBtn;
	private TextView mTitleTV;
	private ChatImmeLayout mImmeLayout;

	private List<MyMessageItem> mData;
	private ChatAdapter mAdapter;
	public static int mUID;
	private String mNickName;
	private LetterDao mDao;
	private InputMethodManager mImm;
	private ChatReceiver mChatReceiver;
	private boolean mHadRegistChatReceiver = false;//开启和关闭广播判断
	public static boolean isShown = false;
	private NotificationManager mNitiManager;
	private long mCursor;
//	private long mLastCursor;
//	private ConcurrentLinkedQueue<Long> mRequestQueue;
//	private Timer mTimer;
	private boolean mWorking;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		log.d("onCreate");
		setContentView(R.layout.chat);
		CyouApplication.setChatActivity(this);
		mImm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);                          
		mNitiManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
		registReceiver();
		initView();
		initData();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		log.d("onNewIntent");
		setIntent(intent);
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		SharedPreferenceUtil.saveChatActivityState(true, mUID);
		mNitiManager.cancel(R.id.letter_notification);
		initData();
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		SharedPreferenceUtil.saveChatActivityState(false, 0);
		
	}
	
	
	private void initView() {
		this.mImmeLayout = (ChatImmeLayout)findViewById(R.id.chat_immelayout);
		this.mImmeLayout.setImmeListener(new ImmeListener() {
			
			@Override
			public void onImmeShow() {
				mListView.setSelection(mData.size()-1);
			}
			
			@Override
			public void onImmeHide() {
				mListView.setSelection(mData.size()-1);
			}
		});
		this.mListView = (ChatListView) findViewById(R.id.chat_lv);
		this.mET = (EditText) findViewById(R.id.chat_bottom_et);
		this.mSendBtn = (Button) findViewById(R.id.chat_bottom_send_btn);
		this.mSendBtn.setOnClickListener(this);
		View header = findViewById(R.id.chat_header_bar);
		this.mBackBtn = (ImageButton) header.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackBtn.setOnClickListener(this);
		this.mTitleTV = (TextView)header.findViewById(R.id.sub_header_bar_tv);
	}
	
	private void initData() {
		log.d("initData");
//		mTimer = new Timer();
//		mRequestQueue = new ConcurrentLinkedQueue<Long>();
		Intent intent = getIntent();
		mUID = intent.getIntExtra(Params.CHAT.FROM,0);
		mNickName = intent.getStringExtra(Params.CHAT.NICK_NAME);
		log.d("mUID = "+mUID);
		if (mUID == 0) {
			log.e("uid is 0");
			return;
		}
		log.d("mNickName = "+mNickName);
		if(!TextUtils.isEmpty(mNickName)){
			mTitleTV.setText(mNickName);
		}else{
			getUserInfo();
		}
		mData = new ArrayList<MyMessageItem>();
		mAdapter = new ChatAdapter(this, mData,mHandler);
		mListView.setAdapter(mAdapter);
		mListView.setOnLoadListener(this);
		mDao = new LetterDao(this);
		List<MyMessageItem> list = mDao.selectDataByUID(mUID,0);
		for(int i=0;i<list.size();i++){
			mData.add(list.get(list.size() - i - 1));
		}
		if(!mData.isEmpty()){
			mCursor = mData.get(0).getCreatetime();
		}
		mAdapter.notifyDataSetChanged();
		if(!mData.isEmpty()){
			mListView.setSelection(mData.size()-1);
		}
		if(mData.size() < LetterDao.PAGE_SIZE){
			mListView.loadComplete();
		}
		mDao.markRead(mUID);
		markRead();
		Util.requestFansAndLetterCount(this,SystemCountMsgItem.SYSTEM);
//		mTimer.schedule(new AppendChatThread(), 0, TIME);
	}
	
	private void markRead(){
		new Thread(){
			public void run() {
				mDao.markRead(mUID);
				SystemCountMsgItem.clearUnreadLetterCount();
				int unread = mDao.countUnread();
				SystemCountMsgItem.changeUnreadLetterCount(unread, ChatActivity.this) ;
			};
		}.start();
	}
	
	private void registReceiver(){
		if(mChatReceiver == null){
			mChatReceiver = new ChatReceiver();
		}
		if(!mHadRegistChatReceiver && mChatReceiver!=null){
			IntentFilter filter = new IntentFilter(Contants.ACTION.NEW_CHAT);
			filter.setPriority(Config.CHAT_BRROADCAST_PRIORITY.CHATACTIVITY);
			registerReceiver(mChatReceiver, filter);
			mHadRegistChatReceiver = true;
		}
	}
	/**
	 * 关闭广播吧？？
	 */
	private void unregistReceiver(){
		try {
			if(mHadRegistChatReceiver && mChatReceiver != null){
				unregisterReceiver(mChatReceiver);
				mHadRegistChatReceiver = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOAD_CHAT_COMPLETE:
//				mRequestQueue.poll();
				mAdapter.notifyDataSetChanged();
				break;
			case CHANGE_CURSOR:
//				long cursor = Long.valueOf(msg.obj.toString());
//				mLastCursor = cursor;
				break;
			case RE_SEND:
				MyMessageItem item = (MyMessageItem)msg.obj;
				if(item != null){
					Iterator<MyMessageItem> it = mData.iterator();
					while(it.hasNext()){
						MyMessageItem i = it.next();
						if(i.getCreatetime() == item.getCreatetime()){
							it.remove();
							break;
						}
					}
					mDao.deleteSingleLetter(item);
					item.setCreatetime(System.currentTimeMillis());
					item.setSendSuccess(null);
					item.setSendState(LetterDBHelper.SEND_STATE.SENDING);
					mData.add(item);
					mDao.insert(item);
					sendMsg2Server(item.getContent(), item.getCreatetime());
					mDao.markRead(item.getTo());

					mAdapter.notifyDataSetChanged();
//					mLastCursor = item.getCreatetime();
				}else{
					log.e("item is null");
				}
				break;
			case NOTIFY_LIST:
				mAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.chat_bottom_send_btn:
			String content = mET.getText().toString().trim();
			if (!TextUtils.isEmpty(content)) {
				final MyMessageItem item = new MyMessageItem();
				item.setContent(content);
				item.setFrom(UserInfoUtil.getCurrentUserId());
				item.setTo(mUID);
				item.setAvatar(UserInfoUtil.getCurrentUserPicture());
				item.setNickname(UserInfoUtil.getCurrentUserNickname());
				item.setCreatetime(System.currentTimeMillis());
				item.setType(Contants.CHAT_TYPE.TEXT);
				item.setTauid(mUID);
				item.setMsgseq("");
				item.setSendState(LetterDBHelper.SEND_STATE.SENDING);
				mData.add(item);
				mAdapter.notifyDataSetChanged();
				mET.setText("");
				mListView.setSelection(mData.size() - 1);
				mDao.insert(item);
				sendMsg2Server(content,item.getCreatetime());
//				new Thread(){
//					public void run() {
//						mDao.insert(item);
//						long precursor = mData.isEmpty() ? 0 : mData.get(mData.size()-1).getCreatetime();
//						List<MyMessageItem> list = mDao.selectLetterDescByCursor(item.getCreatetime(),precursor, mUID);
//						mData.addAll(list);
//						Message msg = mHandler.obtainMessage();
//						msg.what = NOTIFY_LIST;
//						mHandler.sendMessage(msg);
//					};
//				}.start();
			}
			break;
		case R.id.sub_header_bar_left_ibtn:
			InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
			View view = ChatActivity.this.getCurrentFocus();
			if(view != null){
				IBinder binder = view.getWindowToken();
				if(binder != null){
					imm.hideSoftInputFromWindow(binder,
							InputMethodManager.HIDE_NOT_ALWAYS);
				}
			}
			ChatActivity.this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		unregistReceiver();
	}

	/**
	 * 退出时销毁全局存放的当前act实例zz
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregistReceiver();
	}

	/**
	 * 发送私信到IM服务器
	 * 
	 * @param content
	 */
	private void sendMsg2Server(final String content,final long createTime) {
		new Thread(){
			public void run() {
				Intent intent = new Intent(ChatActivity.this, CoreService.class);
				intent.putExtra(Params.PUBLISH.ACTION, Contants.PUBLISH_ACTION.CHAT);
				intent.putExtra(Params.CHAT.FROM, mUID);
				intent.putExtra(Params.CHAT.MSG, content);
				intent.putExtra(Params.CHAT.TAG, Contants.CHAT_TYPE.TEXT);
				intent.putExtra(Params.CHAT.GAMECODE, "");
				intent.putExtra(Params.CHAT.TIME, createTime);
				startService(intent);
				//将该会话中所有消息标记为已读
				mDao.markRead(mUID);

			};
		}.start();
	}

	private class ChatReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent == null){
				return;
			}
			log.d("ChatReceiver:onReceive");
			int uid = intent.getIntExtra(Params.CHAT.FROM, 0);
			final long time = intent.getLongExtra(Params.CHAT.TIME,0);
			log.d("from = "+uid);
			log.d("from="+uid);
			if(uid == 0){
				return;
			}
			if(uid == UserInfoUtil.getCurrentUserId()){
				log.d("time = "+time);
				final int to = intent.getIntExtra(Params.CHAT.TO,0);
				log.d("to = "+to);
				if(to != mUID){
					return;
				}
				for(MyMessageItem item : mData){
					if(item.getCreatetime() == time){
						item.setSendSuccess(Contants.LETTER_SEND_SUCCESS.YES);
						mAdapter.notifyDataSetChanged();
						item.setSendState(LetterDBHelper.SEND_STATE.SEND_SUCCESS);
						return;
					}
				}
				return;
			}else if(uid == mUID){
				MyMessageItem item = (MyMessageItem)intent.getSerializableExtra(Params.CHAT.ITEM);
				mData.add(item);
				mAdapter.notifyDataSetChanged();
				markRead();
			}
		}
		
	}
	
//	private class AppendChatThread extends TimerTask{
//		@Override
//		public void run() {
//			if(mWorking){
//				return;
//			}
//			if(!mRequestQueue.isEmpty()){
//				log.d("有新私信");
//				List<MyMessageItem> list = mDao.selectLetterDescByCursor(mRequestQueue.poll(), mData.get(mData.size()-1).getCreatetime(), mUID);
//				if(list != null && !list.isEmpty()){
//					mData.addAll(list);
//					Message msg = new Message();
//					msg.what = LOAD_CHAT_COMPLETE;
//					mHandler.sendMessage(msg);
//				}else{
//					log.e("list is empty");
//					mRequestQueue.clear();
//				}
//				mWorking = false;
//			}
//		}
//	}
	
	private void getUserInfo(){
		RequestParams params = new RequestParams();
		String uid = new JSONArray().put(mUID).toString();
		params.put("uidlist", uid);
		MyHttpConnect.getInstance().post(HttpContants.NET.GET_USER_SIMPLE_INFO, params, new AsyncHttpResponseHandler(){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				if(TextUtils.isEmpty(content)){
					return;
				}
				log.i("get user info result = "+content);
				try {
					UserSimpleInfoBase base = new ObjectMapper()
					.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
					.readValue(content, new TypeReference<UserSimpleInfoBase>() {
					});
					if(base == null){
						return;
					}
					UserSimpleInfo info = base.getData().get(0);
					mTitleTV.setText(info.getNickname());
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(ChatActivity.this);
				dialog.create().show();
			}
		});
	}
	
	@Override
	public void onLoad() {
		List<MyMessageItem> list = mDao.selectDataByUID(mUID,mCursor);
		mListView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
		int index = mListView.getFirstVisiblePosition()+LetterDao.PAGE_SIZE;
		View v = mListView.getChildAt(0);
		int top = (v == null) ? 0 : v.getTop();
		if(list != null && !list.isEmpty()){
			List<MyMessageItem> tempList = new ArrayList<MyMessageItem>();
			for(int i=0;i<list.size();i++){
				tempList.add(list.get(list.size() - i - 1));
			}
			List<MyMessageItem> curList = new ArrayList<MyMessageItem>();
			curList.addAll(mData);
			mData.clear();
			mData.addAll(tempList);
			mData.addAll(curList);
			mCursor = mData.get(0).getCreatetime();
			mAdapter.notifyDataSetChanged();
			mListView.setSelectionFromTop(index, top);
			if(list.size() < LetterDao.PAGE_SIZE){
				mListView.loadComplete();
			}
		}
		mListView.loadingFinish();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		View v = getCurrentFocus();
		if (v != null) {
			IBinder binder = v.getWindowToken();
			if (binder != null) {
				mImm.hideSoftInputFromWindow(binder,
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		
	}
	
}