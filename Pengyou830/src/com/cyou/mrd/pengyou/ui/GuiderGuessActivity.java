package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GuiderFriendRecommendAdapter;
import com.cyou.mrd.pengyou.entity.RecommendFriendItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.service.LaunchService;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 引导页的猜你认识
 * @author 
 *
 */
public class GuiderGuessActivity extends CYBaseActivity implements OnClickListener{

	private Button mJumpBtn;
	private GridView mGV;
	private ImageButton mBackIBtn;
	private Button mFocusAllBtn;
	
	private GuiderFriendRecommendAdapter mAdapter;
	private List<RecommendFriendItem> mData;
	private WaitingDialog mWaitingDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guider_guess);
		initView();
		initEvent();
		initData();
	}
	
	@Override
	protected void initView() {
		this.mJumpBtn = (Button)findViewById(R.id.guider_guess_jump_btn);
		this.mGV = (GridView)findViewById(R.id.my_friend_recommend_gv);
		this.mBackIBtn = (ImageButton)findViewById(R.id.guider_guess_back_ibtn);
		this.mFocusAllBtn = (Button)findViewById(R.id.guider_guess_focus_all_btn);
		this.mWaitingDialog = new WaitingDialog(this);
	}

	@Override
	protected void initEvent() {
		this.mJumpBtn.setOnClickListener(this);
		this.mBackIBtn.setOnClickListener(this);
		this.mFocusAllBtn.setOnClickListener(this);
	}

	@Override
	protected void initData() {
		this.mData = new ArrayList<RecommendFriendItem>();
		this.mAdapter = new GuiderFriendRecommendAdapter(this, mData);
		this.mGV.setAdapter(mAdapter);
		Intent intent = getIntent();
		Bundle b = intent.getExtras();
		if(b != null){
			ArrayList<RecommendFriendItem> list = b.getParcelableArrayList("list");
			if(list != null){
				this.mData.addAll(list);
				this.mAdapter.notifyDataSetChanged();
			}else{
				log.e("list is null");
			}
		}else{
			log.e("b is null");
		}
	}

	@Override
	public void onClick(View v) {
		Intent mIntent = null;
		switch (v.getId()) {
		case R.id.guider_guess_focus_all_btn:
			focusAll();
			break;
		case R.id.guider_guess_back_ibtn:
			GuiderGuessActivity.this.finish();
			break;
		case R.id.guider_guess_jump_btn:
			close();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 全部关注
	 * xumengyang
	 */
	private void focusAll(){
		JSONArray array = new JSONArray();
		if(mData != null && !mData.isEmpty()){
			for(RecommendFriendItem item : mData){
				array.put(item.getUid());
			}
		}
		RequestParams params = new RequestParams();
		params.put("uidlist", array.toString());
		MyHttpConnect.getInstance().post(HttpContants.NET.FOCUS_ALL, params, new AsyncHttpResponseHandler(){
			
			@Override
			public void onStart() {
				mWaitingDialog.show(); 
			}
			
			@Override
			public void onSuccess(int statusCode, String content) {
				log.d("全部关注结果="+content);
				mWaitingDialog.dismiss();
				try {
					if(JsonUtils.isSuccessful(content)){
						close();
					}else{
						log.e("全部关注失败");
					}
				} catch (Exception e) {
					log.e(e);
				}
			}
			
			@Override
			public void onFailure(Throwable error, String content) {
				mWaitingDialog.dismiss();
			}
			
		});
	}
	
	private void close(){
		if(Util.isNetWorkAvailable()){
			mWaitingDialog.show();
			new Thread(){
				public void run() {
					Intent intent = new Intent(GuiderGuessActivity.this,LaunchService.class);
					startService(intent);
					Intent mIntent = new Intent(GuiderGuessActivity.this,MainActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivity(mIntent);
					mHandler.sendEmptyMessage(0);
				};
			}.start();
		}else{
			showShortToast(R.string.download_error_network_error);
		}
	}

	private Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			mWaitingDialog.dismiss();
			setResult(CompleteRegistActivity.COMPLETE_GUIDER, new Intent());
			GuiderGuessActivity.this.finish();
		};
	};
}
