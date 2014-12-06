package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.entity.MyNearByItem;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.widget.WaitingDialog;

public class MyNearByActivity extends CYBaseActivity implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	public static final int FEMALE = 2;
	public static final int MALE = 1;
	public static final int WHOLE = 3;
	
	private LinearLayout mFailLayout;
	private Button mReTryBtn;
	private ImageButton mSelectIBtn;
	private ImageButton mBackIBtn;
	private PopupWindow mPopWindow;
	private LinearLayout mFemaleLL;
	private LinearLayout mMaleLL;
	private Button mAllBtn;
	private LinearLayout mContainer;
	private WaitingDialog mWaitingDialog;

	private List<MyNearByItem> mData;
	private FragmentManager mFragmentManager;

	private int mCurrentF = WHOLE;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYNEAR_ID,
				CYSystemLogUtil.ME.BTN_MYNEAR_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_nearby);
		initView();
		initData();
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	protected void initView() {
		this.mFailLayout = (LinearLayout) findViewById(R.id.nearby_fail);
		this.mReTryBtn = (Button) findViewById(R.id.btn_nearby_retry);
		this.mReTryBtn.setOnClickListener(this);
		this.mContainer = (LinearLayout)findViewById(R.id.my_nearby_container);
		this.mSelectIBtn = (ImageButton)findViewById(R.id.my_nearby_select_ibtn);
		this.mSelectIBtn.setOnClickListener(this);
		this.mBackIBtn = (ImageButton)findViewById(R.id.my_nearby_back_ibtn);
		this.mBackIBtn.setOnClickListener(this);
		View popView = getLayoutInflater().inflate(
				R.layout.my_nearby_pop_window, null);
		this.mPopWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		this.mPopWindow.setBackgroundDrawable(new BitmapDrawable());
		this.mPopWindow.setFocusable(true);
		this.mPopWindow.setOutsideTouchable(true);
		this.mFemaleLL = (LinearLayout) popView.findViewById(R.id.nearby_female_ll);
		this.mFemaleLL.setOnClickListener(this);
		this.mMaleLL = (LinearLayout) popView.findViewById(R.id.nearby_male_ll);
		this.mMaleLL.setOnClickListener(this);
		this.mAllBtn = (Button) popView.findViewById(R.id.nearby_all_btn);
		this.mAllBtn.setOnClickListener(this);
		this.mWaitingDialog = new WaitingDialog(MyNearByActivity.this);
	}

	@Override
	protected void initEvent() {
		// TODO Auto-generated method stub
		
	}
	
	public void showWaiting(){
		if(mWaitingDialog != null){
			mWaitingDialog.show();
		}
	}
	
	public void dismissWaiting(){
		if(mWaitingDialog != null){
			mWaitingDialog.dismiss();
		}
	}

	@Override
	protected void initData() {
		this.mFragmentManager = getSupportFragmentManager();
		if (mData == null) {
			mData = new ArrayList<MyNearByItem>();
		}
		this.mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByWholeFragment()).commit();
		this.mCurrentF = WHOLE;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.my_nearby_back_ibtn:
			MyNearByActivity.this.finish();
			break;
		case R.id.my_nearby_select_ibtn:
			mPopWindow.showAsDropDown(mSelectIBtn, 0, 0);
			break;
		case R.id.nearby_female_ll:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByFemaleFragment()).commit();
			this.mCurrentF = FEMALE;
			mPopWindow.dismiss();
			break;
		case R.id.nearby_male_ll:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByMaleFragment()).commit();
			this.mCurrentF = MALE;
			mPopWindow.dismiss();
			break;
		case R.id.nearby_all_btn:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByWholeFragment()).commit();
			this.mCurrentF = WHOLE;
			mPopWindow.dismiss();
			break;
		case R.id.btn_nearby_retry:
			retry();
			break;
		default:
			break;
		}
	}
	
	public void showNetError(){
		showNetErrorDialog(this, new ReConnectListener() {
			
			@Override
			public void onReconnect() {
				retry();
			}
		});
	}
	
	public void showLocationError(){
		this.mContainer.setVisibility(View.GONE);
		this.mFailLayout.setVisibility(View.VISIBLE);
	}
	
	private void retry(){
		this.mContainer.setVisibility(View.VISIBLE);
		this.mFailLayout.setVisibility(View.GONE);
		switch (mCurrentF) {
		case MALE:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByMaleFragment()).commit();
			mCurrentF = MALE;
			break;
		case FEMALE:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByFemaleFragment()).commit();
			mCurrentF = FEMALE;
			break;
		case WHOLE:
			mFragmentManager.beginTransaction().replace(R.id.my_nearby_container, new MyNearByWholeFragment()).commit();
			mCurrentF = WHOLE;
			break;
		default:
			break;
		}
	}
}
