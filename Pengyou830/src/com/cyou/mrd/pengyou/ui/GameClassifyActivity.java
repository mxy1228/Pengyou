package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameClassifyAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.ClassifGameItem;
import com.cyou.mrd.pengyou.entity.ClassifyItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.widget.GameGridView;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 游戏全局分类
 * 
 * @author wangpingzhi
 * 
 */
public class GameClassifyActivity extends CYBaseActivity implements OnClickListener {

	private CYLog log = CYLog.getInstance();

	private GameGridView mGV;
	private List<ClassifyItem> mData;
	private GameClassifyAdapter mAdapter;
	int screenWidth = 0;
	private ImageButton  mBackBtn;
	private ImageButton  mSearchBtn;
	
	private WaitingDialog waitingDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_ID,
				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		setContentView(R.layout.gamestore_classify);
		initView();
		initData();
	}

	protected void initView() {
		((TextView)findViewById(R.id.gamestore_sub_header_tv)).setText(R.string.game_store_classify);
		mBackBtn=((ImageButton)findViewById(R.id.gamestore_sub_header_back));
		mBackBtn.setOnClickListener(this);	
		((ImageButton)findViewById(R.id.gamestore_sub_header_search)).setVisibility(View.GONE);
		/*mSearchBtn=((ImageButton)findViewById(R.id.gamestore_sub_header_search));
		mSearchBtn.setPadding(0, 0, 15, 0);
		mSearchBtn.setOnClickListener(this);*/
		((ImageButton)findViewById(R.id.gamestore_sub_header_divider)).setVisibility(View.GONE);
		((ImageButton)findViewById(R.id.gamestore_sub_header_download)).setVisibility(View.GONE);
		
		screenWidth = getWindowManager().getDefaultDisplay().getWidth();
		mGV = (GameGridView) findViewById(R.id.gamestore_classify_game_gv);
		mGV.setSelector(new ColorDrawable(Color.TRANSPARENT));
		///mGV.setCacheColorHint(Color.TRANSPARENT);
		mGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					if (mData == null || mData.size() == 0
							|| position > mData.size()) {
						return;
					}
					ClassifyItem item = mData.get(position);
					if(null==item){
						return;
					}
					String tid = item.getTid();
					if (TextUtils.isEmpty(tid)) {
						return;
					}
					/*if (!"-1".equals(tid) && !"-2".equals(tid)
							&& !"-3".equals(tid) && !"-4".equals(tid)
							&& !"-5".equals(tid)) {
						BehaviorInfo behaviorInfo = new BehaviorInfo(
								CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_ITEM_ID,
								item.getName());
						CYSystemLogUtil.behaviorLog(behaviorInfo);
						Intent mIntent = new Intent();
						mIntent.setClass(GameClassifyActivity.this,
								ClassifyGameDetailActvity.class);
						mIntent.putExtra(Params.CLASSIFY_ID, item.getTid());
						mIntent.putExtra(Params.CLASSIFY_NAME, item.getName());
						startActivity(mIntent);
					}*/
					Intent mIntent = new Intent();
					mIntent.setClass(GameClassifyActivity.this,
							ClassifyGameDetailActvity.class);
					mIntent.putExtra(Params.CLASSIFY_ID, item.getTid());
					mIntent.putExtra(Params.CLASSIFY_NAME, item.getName());
					startActivity(mIntent);
				} catch (Exception e) {
					log.e(e);
				}
			}
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	protected void initData() {
		mData = new ArrayList<ClassifyItem>();
		mAdapter = new GameClassifyAdapter(GameClassifyActivity.this, mData, screenWidth);
		/*if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
			String classifyData = SharedPreferenceUtil.getClassifyData();
			if (!TextUtils.isEmpty(classifyData)) {
				loadData(classifyData);
				return;
			}
		}*/
		loadClassifyGameLst();
	}

	private void loadClassifyGameLst() {
		showLoadListProgressDialog();
		RequestParams params = new RequestParams();
		params.put("wideicon", "1");
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_CLASSIFY,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						log.e(error);
						super.onFailure(error);
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						dismissProgressDialog();
						showNetErrorDialog(GameClassifyActivity.this,new ReConnectListener() {				
							@Override
							public void onReconnect() {
								loadClassifyGameLst();
							}
						});
						log.e(content);
						log.e(error.getMessage());
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						dismissProgressDialog();
						SharedPreferenceUtil.saveClassifyData(content);
						loadData(content);
						super.onSuccess(content);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameClassifyActivity.this);
						dialog.create().show();
					}
				});
	}

	private void loadData(String content) {
		try {
			log.d("classify game is :" + content);
			String data = JsonUtils.getJsonValue(content, "data");
			ClassifGameItem item = JsonUtils.fromJson(data,
					ClassifGameItem.class);
			String gametypes = JsonUtils.getJsonValue(data, "gametypes");
			log.d("gametypes result is:" + gametypes);
			if (null != item) {// 游戏分类
				List<ClassifyItem> tempClassGameLst = item.getGametypes();
				mData.addAll(tempClassGameLst);
				List<ClassifyItem> tempCustomGameLst = item.getCusttypes();
				mData.addAll(tempCustomGameLst);
				/*
				if (null != tempClassGameLst && tempClassGameLst.size() > 0) {// 游戏分类
					ClassifyItem tempItem = new ClassifyItem();
					tempItem.setTid("-1");// 表示游戏分类title
					ClassifyItem tempItem1 = new ClassifyItem();
					tempItem1.setTid("-2");// 表示游戏分类title
					mData.add(tempItem);
					mData.add(tempItem1);
					if (tempClassGameLst.size() % 2 != 0) {
						ClassifyItem empty = new ClassifyItem();
						empty.setTid("-5");// 表示补充位
						tempClassGameLst.add(empty);
					}
					mData.addAll(tempClassGameLst);
				}
				List<ClassifyItem> tempCustomGameLst = item.getCusttypes();
				if (null != tempCustomGameLst && tempCustomGameLst.size() > 0) {// 游戏分类
					ClassifyItem tempItem = new ClassifyItem();
					tempItem.setTid("-3");// 表示个性分类title
					ClassifyItem tempItem1 = new ClassifyItem();
					tempItem1.setTid("-4");// 表示个性分类title
					mData.add(tempItem);
					mData.add(tempItem1);
					mData.addAll(tempCustomGameLst);
				}*/
				if (mAdapter == null) {
					mAdapter = new GameClassifyAdapter(GameClassifyActivity.this, mData,screenWidth);
				}
				mGV.setAdapter(mAdapter);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.gamestore_sub_header_back:
			finish();
			break;
		/*case R.id.gamestore_sub_header_search:
			Intent mIntent = new Intent();
			mIntent.setClass(GameClassifyActivity.this, SearchActivity.class);
			startActivity(mIntent);
			break;*/
		}
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
	
	public void showLoadListProgressDialog() {
		waitingDialog = new WaitingDialog(this);
		waitingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		waitingDialog.setMessage(getResources().getString(R.string.list_loading));
		waitingDialog.setIndeterminate(false);
		waitingDialog.setCancelable(true);
		waitingDialog.show();
	}
	/**
	 * 隐藏滚动的对话框
	 */
	public void dismissProgressDialog() {
		if (waitingDialog != null && waitingDialog.isShowing()) {
			waitingDialog.dismiss();
		}
	}

}
