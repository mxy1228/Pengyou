package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameGiftListAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameGiftListItem;
import com.cyou.mrd.pengyou.entity.base.GameGiftListBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class GameGiftListActivity extends CYBaseActivity implements OnClickListener{
	
	private PullToRefreshListView mListView;
	private ImageButton mBackBtn;
	private TextView mHeaderBarTV;
	private TextView mEmptyTV;
//	private WaitingDialog mWaitingDialog;
	private List<GameGiftListItem> mData;
	private GameGiftListAdapter mAdapter;
	private  boolean mResumed = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_gift_pkg_list);
		initView();
		initData();
	}

	@Override
	protected void initView() {
		// TODO Auto-generated method stub
//		this.mWaitingDialog = new WaitingDialog(this);
		this.mEmptyTV = (TextView)findViewById(R.id.game_gift_pkg_list_empty);
		this.mListView = (PullToRefreshListView) findViewById(R.id.game_gift_pkg_list_lv);
//		this.mListView.setOnLoadListener(new LoadListener() {
//
//			@Override
//			public void onScrollStateChanged(AbsListView view, int scrollState) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onScroll(AbsListView view, int firstVisibleItem,
//					int visibleItemCount, int totalItemCount) {
//				// TODO Auto-generated method stub
//
//			}
//
//			@Override
//			public void onLoad() {
//				if(Util.isNetworkConnected(GameGiftListActivity.this)){
//					getGameGiftList();
//		        }
//				else {
//					try {
//						mListView.onRefreshFinish();
//						mListView.loadComplete();
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					showShortToast(R.string.networks_available);
//					mEmptyTV.setVisibility(View.VISIBLE);
//				}
//
//			}
//		});

//	   this.mListView.setOnRefreshListener(new  RefreshListener(){
//		   @Override
//		   public void onRefresh() {
//			   if(Util.isNetworkConnected(GameGiftListActivity.this)){
//					getGameGiftList();
//		        }
//				else {
//					try {
//						mListView.onRefreshFinish();
//						mListView.loadComplete();
//					} catch (Exception e) {
//						// TODO: handle exception
//					}
//					showShortToast(R.string.networks_available);
//					mEmptyTV.setVisibility(View.VISIBLE);
//				}
//			
//		   }
//		  
//	   });
		
		View headerBar = findViewById(R.id.game_gift_pkg_list_headerbar);
		this.mBackBtn = (ImageButton) headerBar
				.findViewById(R.id.sub_header_bar_left_ibtn);
		this.mBackBtn.setOnClickListener(this);
		this.mHeaderBarTV = (TextView) headerBar
				.findViewById(R.id.sub_header_bar_tv);
		this.mHeaderBarTV.setText(R.string.game_gift);
		
		this.mListView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if(mData == null || mData.isEmpty()){
					return;
				}
				try {
					GameGiftListItem item = mData.get(arg2);
					Intent intent = new Intent(GameGiftListActivity.this,GameGiftDetailActivity.class);
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTID, String.valueOf(item.getGiftid()));
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTCODE, item.getUsergift());
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTNAME, item.getGiftname());
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMEGIFTIDENTIFIER, item.getGame().getIdentifier());
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMECODE, item.getGame().getGamecode());
					intent.putExtra(Params.DYNAMIC_DETAIL.GAMENAME, item.getGame().getName());
					startActivityForResult(intent, 1);
				} catch (Exception e) {
				}
				
			}
		});
		
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub		
	}

	@Override
	protected void onDestroy(){
		super.onDestroy();
	}
	@Override
	protected void initData() {
		mData = new ArrayList<GameGiftListItem>();
		mAdapter = new GameGiftListAdapter(this, mData, mListView, mHandler);
		mListView.setAdapter(mAdapter);
		log.i("游戏礼包列表 initData");
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		if(!mResumed || GameGiftDetailActivity.mGiftPkgUpdate){
			mResumed = true;
			getGameGiftList();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sub_header_bar_left_ibtn:
			GameGiftListActivity.this.finish();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		log.d("onActivityResult");
		switch (resultCode) {
		case 1:
			mAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	private void getGameGiftList(){
		RequestParams  params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_GIFT_LIST, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						try {
							super.onSuccess(statusCode, content);
							mListView.onRefreshFinish();
							mListView.loadingFinish();
							mListView.loadComplete();
						    if (TextUtils.isEmpty(content)) {
							   mEmptyTV.setVisibility(View.VISIBLE);
							   return;
						    }
						    log.i("游戏礼包列表 result = " + content);

							GameGiftListBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(content, new TypeReference<GameGiftListBase>() {
									});
							
							if (base.getData() == null
									|| base.getData().isEmpty()) {
								mEmptyTV.setVisibility(View.VISIBLE);
								return;
							}
							else {
								mEmptyTV.setVisibility(View.GONE);
								mData.clear();								
								mData.addAll(base.getData());
								mAdapter.notifyDataSetChanged();
							}
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameGiftListActivity.this);
						dialog.create().show();
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						super.onFailure(error, content);
						try {
							showNetErrorDialog(GameGiftListActivity.this,
									new ReConnectListener() {
										@Override
										public void onReconnect() {
											getGameGiftList();
										}
									});
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
					});
		
	}
	
	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			
		}
		
	};

}
