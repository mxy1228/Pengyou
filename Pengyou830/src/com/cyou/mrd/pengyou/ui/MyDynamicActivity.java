package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.DynamicAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.Dynamic;
import com.cyou.mrd.pengyou.entity.DynamicItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.UserInfoUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 我的动态
 * @author lvwenlong
 *
 */
public class MyDynamicActivity extends CYBaseActivity implements OnClickListener {

	private CYLog log = CYLog.getInstance();
	private static final int SHOW_COMMENT_ET = 0;
	private static final int DELETE_DYNAMIC = 1;

	private PullToRefreshListView mListView;
	private ImageButton mBackBtn;
	private TextView mHeaderBarTV;
	private LinearLayout mCommentLL;
	private EditText mCommentET;
	private Button mCommentSendBtn;
	private WaitingDialog mWaitingDialog;
	private TextView mEmptyTV;
	private RefreshReceiver mRefreshReceiver;
	private List<DynamicItem> mData;
	private DynamicAdapter mAdapter;
	private int mCursor;
	private MyHttpConnect mConn;
	private int mCurrentCommentPosition = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dynamic);
		initView();
		initData();
		if(mRefreshReceiver == null){
			mRefreshReceiver = new RefreshReceiver();			
		}
		registerReceiver(mRefreshReceiver, new IntentFilter(Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO));
	}

	@Override
	protected void initView() {
		this.mWaitingDialog = new WaitingDialog(this);
		this.mWaitingDialog.show();
		this.mEmptyTV = (TextView)findViewById(R.id.my_dynamic_empty);
		this.mListView = (PullToRefreshListView) findViewById(R.id.my_dynamic_lv);
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoad() {
				getDynamic();
			}
		});
		// this.mListView.setOnItemClickListener(new DynamicItemClick());
		View headerBar = findViewById(R.id.my_dynamic_header_bar);
		this.mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mHeaderBarTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		this.mHeaderBarTV.setText(R.string.my_dynamic);
		this.mBackBtn.setOnClickListener(this);
		this.mCommentLL = (LinearLayout) findViewById(R.id.my_dynamic_comment_ll);
		this.mCommentET = (EditText) findViewById(R.id.my_dynamic_comment_et);
		this.mCommentSendBtn = (Button) findViewById(R.id.my_dynamic_comment_send_btn);
		this.mCommentSendBtn.setOnClickListener(this);
		this.mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				new AlertDialog.Builder(MyDynamicActivity.this)
				.setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						DynamicItem item = mData.get(arg2);
						if(item != null){
							deleteDynamic(item.getAid(), arg2);
							getDynamic();
						}
					}
				})
				.create().show();
				return true;
			}
			
		});
		this.mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mData.size()<arg2){
					return;
				}
				DynamicItem item = mData.get(arg2);
				Intent intent = new Intent(MyDynamicActivity.this,DynamicDetailActivity.class);
				intent.putExtra(Params.DYNAMIC_DETAIL.AID, item.getAid());
				intent.putExtra(Params.DYNAMIC_DETAIL.UID, item.getUid());
				intent.putExtra(Params.DYNAMIC_DETAIL.POSITION, arg2);
				intent.putExtra(Params.DYNAMIC_DETAIL.DELETE_BUTTON_SHOW, true);
				startActivityForResult(intent, DELETE_DYNAMIC);
			}
		});
	}

	@Override
	protected void initData() {
		mData = new ArrayList<DynamicItem>();
		mAdapter = new DynamicAdapter(this, mData, mListView, mHandler);
		mListView.setAdapter(mAdapter);
		mConn = MyHttpConnect.getInstance();
		getDynamic();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log.d("onActivityResult");
		switch (resultCode) {
		case DELETE_DYNAMIC:
			if(data != null){
				int positon = data.getIntExtra(Params.DYNAMIC_DETAIL.POSITION, 0);
				log.d("delete dynamic position = "+positon);
				mData.remove(positon);
				mAdapter.notifyDataSetChanged();
				showEmptyView();
			}
			break;

		default:
			break;
		}
	}
	
	private void deleteDynamic(int aid,final int position){
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		mConn.post(HttpContants.NET.DELETE_DYNAMIC, params,new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				mWaitingDialog.show();
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
			
			@Override
			public void onLoginOut() {
				LoginOutDialog dialog = new LoginOutDialog(MyDynamicActivity.this);
				dialog.create().show();
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				log.i("delete dynamic result = "+content);
				try {
					JSONObject obj = new JSONObject(content);
					if (obj.has(Params.HttpParams.SUCCESSFUL)) {
						if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
							UserInfoUtil.changeDyanmicCount(-1);
							mData.remove(position);
							mAdapter.notifyDataSetChanged();
							showEmptyView();
						}
					}
					
				} catch (Exception e) {
					log.e(e);
				}
				mWaitingDialog.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			MyDynamicActivity.this.finish();
			break;
		case R.id.my_dynamic_comment_send_btn:
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_PUBLISH_ID,
					CYSystemLogUtil.ME.BTN_MYDYNAMIC_COMMENT_PUBLISH_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			String content = mCommentET.getText().toString();
			mCommentET.setText("");
			if (TextUtils.isEmpty(content.replaceAll("\\s*", ""))) {
				return;
			}
			int position = Integer.valueOf(v.getTag().toString());
			DynamicItem item = (DynamicItem) mListView
					.getItemAtPosition(position);
			sendComment(content, item.getAid(), position);
			break;
		default:
			break;
		}

	}

	/**
	 * 获取动态
	 */
	private void getDynamic() {
		RequestParams params = new RequestParams();
		params.put("uid",
				String.valueOf(UserInfoUtil.getCurrentUserId()));
		params.put("count", String.valueOf(Config.PAGE_SIZE));
		params.put("cursor", String.valueOf(mCursor));
		mConn.post(HttpContants.NET.GET_USER_DYNAMIC, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
					}

					@Override
					public void onFailure(Throwable error, String content) {
						mWaitingDialog.dismiss();
						try {
							 showNetErrorDialog(MyDynamicActivity.this,new ReConnectListener() {									
									@Override
									public void onReconnect() {
										getDynamic();
									}
								});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyDynamicActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						try {
							log.i("result = " + content);
							Dynamic dynamic = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(
									content, new TypeReference<Dynamic>() {
									});
							if (dynamic.getData() == null
									|| dynamic.getData().isEmpty()) {
								mListView.loadComplete();
								mWaitingDialog.dismiss();
								showEmptyView();
								return;
							}
							mData.addAll(dynamic.getData());
							mAdapter.notifyDataSetChanged();
							mCursor = dynamic.getData()
									.get(dynamic.getData().size() - 1).getCursor();
							if (dynamic.getData().size() < Config.PAGE_SIZE) {
								mListView.loadComplete();
							}
							mCursor = dynamic.getData()
									.get(dynamic.getData().size() - 1).getCursor();
							mListView.loadingFinish();
						} catch (Exception e) {
							log.e(e);
						}
						mWaitingDialog.dismiss();
					}
				});
	}

	private void sendComment(final String text, int aid, final int position) {
		RequestParams params = new RequestParams();
		params.put("aid", String.valueOf(aid));
		params.put("text", text);
		mConn.post(HttpContants.NET.SEND_DYNAMIC_COMMENT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(MyDynamicActivity.this);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("send comment result = " + content);
						try {
							JSONObject obj = new JSONObject(content);
							if (obj.has(Params.HttpParams.SUCCESSFUL)) {
								if (obj.getInt(Params.HttpParams.SUCCESSFUL) == 1) {
									showShortToast(R.string.comment_success);
									DynamicItem item = (DynamicItem) mListView
											.getItemAtPosition(position);
									if (item == null) {
										return;
									}
									StringBuilder comment = item.getComment();
									comment.append("<font size='13' color='#7c7c7c'>"
											+ UserInfoUtil.getCurrentUserNickname()
											+ "</>"
											+ ":"
											+ "<font size='13' color='#333333'>"
											+ text + "</><br>");
									item.setCommentcnt(item.getCommentcnt() + 1);
									mAdapter.notifyDataSetChanged();
								}
								else{
									String errorNo = JsonUtils.getJsonValue(content,
											"errorNo");
									if (!TextUtils.isEmpty(errorNo)
											&& String.valueOf(
													Contants.ERROR_NO.ERROR_MASK_WORD_STRING)
													.equals(errorNo)) {
										showShortToast(R.string.comment_maskword_failed);
									}
								}
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
				});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SHOW_COMMENT_ET:
				if (!mCommentLL.isShown()) {
					mCommentLL.setVisibility(View.VISIBLE);
				}
				mCommentET.requestFocus();
				mCurrentCommentPosition = msg.arg1;
				mCommentSendBtn.setTag(msg.arg1);
				Timer timer = new Timer();
				timer.schedule(new TimerTask() {

					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 100);
				break;

			default:
				break;
			}
		};
	};

	private void showEmptyView(){
		if(mData.isEmpty()){
			mEmptyTV.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if( mRefreshReceiver != null){
				unregisterReceiver(mRefreshReceiver);
				mRefreshReceiver = null;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 接到广播后刷新zan数据
	 * @author bichunhe
	 *
	 */
	private class RefreshReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {			
		 if (Contants.ACTION.UPDATE_RELATION_SUPPORT_INFO.equals(intent.getAction())){
				try {
					int aid = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_AID, 0);
					log.i("MyDynamicActivity UPDATE_RELATION_SUPPORT_INFO aid: " + aid);
					int cancel = intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_SUPPORT, 0);
					if(intent.getIntExtra(Contants.SEND_DYNAMIC_DATA.DYNAMIC_DATA_PID, 0) == 0){
						//赞同步来自我的动态 自己不必更新自己
						return;
					}
					if (mData != null && mData.size() > 0) {
						boolean needNotify = false;
						for (DynamicItem dynamicItem : mData) {
							if (aid == dynamicItem.getAid()) {
								int index = mData.indexOf(dynamicItem);
								log.i("MyDynamicActivity UPDATE_RELATION_SUPPORT_INFO index: " + index);
								if(cancel == 0){
//									mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() + 1);
									mData.get(index).setSupported(Contants.SUPPORT.YES);
								}
								else{								
//									mData.get(index).setSupportcnt(dynamicItem.getSupportcnt() - 1);
									mData.get(index).setSupported(Contants.SUPPORT.NO);
								}
								needNotify = true;
								break;
							}
						}
						if(needNotify)						
							mAdapter.notifyDataSetChanged();
					}				
				} catch (Exception e) {
					// TODO: handle exception
				}			
			}
		}
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}

}
