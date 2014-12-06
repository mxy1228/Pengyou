package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.MessageAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.LetterDao;
import com.cyou.mrd.pengyou.entity.ConversationItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 
 * @author wangkang
 * 
 */
public class MyMessageFragment extends BaseFragment {
	private CYLog log = CYLog.getInstance();
	
	private static final int SELECT_COMPLETE = 1;

	private PullToRefreshListView mListView;
	private TextView mEmptyTV;

	private List<ConversationItem> mData;
	private Activity mActivity;
	private MessageAdapter mAdapter;
	private MyHttpConnect mConn;
	private int mPage = 1;
	private LetterDao mDao;
	private DisplayImageOptions mIconOption;
	private DisplayImageOptions mAvatarOption;
	private ChatReceiver mChatReceiver;
	private DataThread mDataThread;
	private boolean mHadRegistChatReceiver = false;
	private boolean mWorking;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYMSG_PRI_MSG_ID,
				CYSystemLogUtil.ME.BTN_MYMSG_PRI_MSG_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.my_message, null);
		this.mEmptyTV = (TextView)view.findViewById(R.id.my_message_empty);
		mListView = (PullToRefreshListView) view.findViewById(R.id.my_message_lv);
		mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			@Override
			public void onLoad() {

			}
		});
		mListView.setOnItemClickListener(new ItemClickListener());
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view,
					final int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYMSG_DELETE_ID,
						CYSystemLogUtil.ME.BTN_MYMSG_DELETE_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				new AlertDialog.Builder(getActivity()).setMessage(R.string.delete_hint)
				.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						int headerCount = mListView.getHeaderViewsCount();
						log.e("position = "+position);
						ConversationItem item = mData.get(position-headerCount);
						mData.remove(position-headerCount);
						mAdapter.notifyDataSetChanged();
						new LetterDao(getActivity()).deleteByUid(item.getUid());
						Toast.makeText(getActivity(), R.string.delete_success, Toast.LENGTH_SHORT).show();
						if(mData.isEmpty()){
							mEmptyTV.setVisibility(View.VISIBLE);
						}
						SystemCountMsgItem.changeUnreadLetterCount(0 - item.getUnreadLetterCount(), mActivity);
//						Util.requestFansAndLetterCount(getActivity(),SystemCountMsgItem.SYSTEM);
					}
				})
				.create()
				.show();
				return false;
			}
		});
		return view;
	}


	public MyMessageFragment(){
		
	}
	@Override
	public void onStart() {
		super.onStart();
		this.mIconOption = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.icon_default)
				.showImageOnFail(R.drawable.icon_default)
				.showStubImage(R.drawable.icon_default).build();
		this.mAvatarOption = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.showImageForEmptyUri(R.drawable.avatar_defaul)
				.showImageOnFail(R.drawable.avatar_defaul)
				.showStubImage(R.drawable.avatar_defaul).build();
		if (mConn == null) {
			mConn = MyHttpConnect.getInstance();
		}
		initData();
		registReceiver();
		mListView.loadComplete();
	}
	
	private void registReceiver(){
		if(mChatReceiver == null){
			mChatReceiver = new ChatReceiver();
		}
		if(mChatReceiver != null && !mHadRegistChatReceiver){
			IntentFilter filter = new IntentFilter(Contants.ACTION.NEW_CONVERSATION);
			filter.setPriority(Config.CHAT_BRROADCAST_PRIORITY.MY_MESSAGE_FRAGMENT);
			getActivity().registerReceiver(mChatReceiver, filter);
			mHadRegistChatReceiver = true;
		}
	}
	
	private void unregistReceiver(){
		try {
			if(mChatReceiver != null && mHadRegistChatReceiver){
				getActivity().unregisterReceiver(mChatReceiver);
				mHadRegistChatReceiver = false;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregistReceiver();
	}
	
	private void initData(){
		if (mData == null) {
			mData = new ArrayList<ConversationItem>();
		}else{
			mData.clear();
		}
		mDao = new LetterDao(getActivity());
		if (mAdapter == null) {
			mAdapter = new MessageAdapter(getActivity(), mData);
			mListView.setAdapter(mAdapter);
		}
		mListView.onRefreshFinish();
		mListView.loadComplete();
		getData();
	}

	private void getData(){
		if(mWorking){
			log.e("working");
			return;
		}
		new DataThread().start();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			ConversationItem item = (ConversationItem) mListView
					.getItemAtPosition(arg2);
			if (item != null) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYMSG_RE_MSG_ID,
						CYSystemLogUtil.ME.BTN_MYMSG_RE_MSG_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);

				Intent intent = new Intent(getActivity(), ChatActivity.class);
				intent.putExtra(Params.CHAT.FROM, item.getUid());
				intent.putExtra(Params.CHAT.NICK_NAME, item.getNickname());
				getActivity().startActivity(intent);
				
				new LetterDao(getActivity()).markRead(item.getUid());
				item.setUnreadLetterCount(0);
				
				Util.requestFansAndLetterCount(getActivity(),SystemCountMsgItem.SYSTEM);
			}
		}

	}

	private class ChatReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			log.d("MyMessageFragment:ChatReceiver:onReceive");
			getData();
		}
		
	}
	
	private class DataThread extends Thread{
		@Override
		public void run() {
			mWorking = true;
			List<ConversationItem> items = mDao.selectConversations();
			Collections.sort(mData, new Comparator<ConversationItem>() {

				@Override
				public int compare(ConversationItem lhs, ConversationItem rhs) {
					return lhs.getTime() > rhs.getTime() ? -1 : 1;
				}
			});
			if(SharedPreferenceUtil.isChatActivityShown()){
				for(ConversationItem item : items){
					if(item.getUid() == SharedPreferenceUtil.getChatActivityUID()){
						item.setUnreadLetterCount(0);
						break;
					}
				}
			}
			Message msg = new Message();
			msg.what = SELECT_COMPLETE;
			msg.obj = items;
			mHandler.sendMessage(msg);
		}
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SELECT_COMPLETE:
				List<ConversationItem> items = (List<ConversationItem>)msg.obj;
				if(items == null){
					return;
				}
				mData.clear();
				mData.addAll(items);
				mAdapter.notifyDataSetChanged();
				if(mData.isEmpty()){
					mEmptyTV.setVisibility(View.VISIBLE);
				}else{
					mEmptyTV.setVisibility(View.GONE);
				}
				mWorking = false;
				break;

			default:
				break;
			}
		};
	};
}
