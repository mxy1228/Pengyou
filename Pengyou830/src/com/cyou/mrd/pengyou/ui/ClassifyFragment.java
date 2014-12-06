package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ClassifyAdapter;
import com.cyou.mrd.pengyou.entity.ClassifGameItem;
import com.cyou.mrd.pengyou.entity.ClassifyItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 游戏全局分类
 * 
 * @author wangkang
 * 
 */
public class ClassifyFragment extends BaseFragment {

	private CYLog log = CYLog.getInstance();

	private GridView mGV;
	private Activity mActivity;
	private List<ClassifyItem> mData;
	private ClassifyAdapter mAdapter;
	int screenWidth = 0;
	View view;
	private LinearLayout layoutRoot;

	public ClassifyFragment() {
		super();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		BehaviorInfo behaviorInfo = new BehaviorInfo(
//				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_ID,
//				CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_NAME);
//		CYSystemLogUtil.behaviorLog(behaviorInfo);
		this.mActivity = getActivity();
		view = inflater.inflate(R.layout.classify, null);
		initView();
		return view;
	}

	private void initView() {
		screenWidth = mActivity.getWindowManager().getDefaultDisplay()
				.getWidth();
		this.mGV = (GridView) view.findViewById(R.id.classify_game_gv);
		mGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				try {
//					if (mData == null || mData.size() == 0
//							|| position > mData.size()) {
//						return;
//					}
//					ClassifyItem item = mData.get(position);
//					if(null==item){
//						return;
//					}
//					String tid = item.getTid();
//					if (TextUtils.isEmpty(tid)) {
//						return;
//					}
//					if (!"-1".equals(tid) && !"-2".equals(tid)
//							&& !"-3".equals(tid) && !"-4".equals(tid)
//							&& !"-5".equals(tid)) {
//						BehaviorInfo behaviorInfo = new BehaviorInfo(
//								CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_CLASSIFY_ITEM_ID,
//								item.getName());
//						CYSystemLogUtil.behaviorLog(behaviorInfo);
//						Intent mIntent = new Intent();
//						mIntent.setClass(mActivity,
//								ClassifyGameDetailActvity.class);
//						mIntent.putExtra(Params.CLASSIFY_ID, item.getTid());
//						mIntent.putExtra(Params.CLASSIFY_NAME, item.getName());
//						startActivity(mIntent);
//					}
//				} catch (Exception e) {
//					log.e(e);
//				}
				Intent mIntent = new Intent();
				mIntent.setClass(mActivity,ReCommendedGameActivity.class);
				startActivity(mIntent);
			}
			
		});
		layoutRoot = (LinearLayout) view.findViewById(R.id.layout_root);
		mGV.setSelector(new ColorDrawable(R.color.empty_bg));
		initData();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	private void initData() {
		mData = new ArrayList<ClassifyItem>();
		mAdapter = new ClassifyAdapter(mActivity, mData, screenWidth);
		if (!NetUtil.isNetworkAvailable()) {// 网络是否可用
			String classifyData = SharedPreferenceUtil.getClassifyData();
			if (!TextUtils.isEmpty(classifyData)) {
				loadData(classifyData);
				return;
			}
		}
		loadClassifyGameLst();
	}

	private void loadClassifyGameLst() {
		RequestParams params = new RequestParams();
		MyHttpConnect.getInstance().post(HttpContants.NET.GAME_CLASSIFY,
				params, new AsyncHttpResponseHandler() {
					@Override
					public void onFailure(Throwable error) {
						log.e(error);
						super.onFailure(error);
					}

					@Override
					public void onSuccess(String content) {
						super.onSuccess(content);
						SharedPreferenceUtil.saveClassifyData(content);
						loadData(content);
						super.onSuccess(content);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
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
				}
				if (mAdapter == null) {
					mAdapter = new ClassifyAdapter(mActivity, mData,
							screenWidth);
				}
				mGV.setAdapter(mAdapter);
			}
		} catch (Exception e) {
			log.e(e);
		}
	}
}
