package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.ImportFriendAdapter;
import com.cyou.mrd.pengyou.entity.SNSItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.utils.WeiboApi.WeiboBindListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * 好友导入
 * 
 * @author wangkang
 * 
 */
public class AddFriendImportFragment extends BaseFragment {

	private PullToRefreshListView mListView;
	private View mView;
	private Activity mActivity;

	private List<SNSItem> mData;
	private ImportFriendAdapter mAdapter;
	private LinearLayout layoutSearchUser;

	public AddFriendImportFragment() {
		super();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.mActivity = activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_ID,
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mActivity = getActivity();
		mView = inflater.inflate(R.layout.import_friend_sns, null);
		mListView = (PullToRefreshListView) mView
				.findViewById(R.id.friend_known_lv);
		layoutSearchUser = (LinearLayout) mView
				.findViewById(R.id.layout_search_user);
		layoutSearchUser.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_FRD_ID,
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_FRD_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				Intent mIntent = new Intent();
				mIntent.setClass(mActivity, SearchUserActivity.class);
				startActivity(mIntent);
			}
		});
		initData();
		return mView;
	}

	private void initData() {
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_ID,
						CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_SEARCH_IMPORT_FRD_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				SNSItem item = mData.get(position);
				isBindSina(item);
			}
		});
		if (mData == null) {
			mData = new ArrayList<SNSItem>();
			SNSItem sinaWeiboItem = new SNSItem();
			sinaWeiboItem.setSnsName(getString(R.string.sina_weibo));
			sinaWeiboItem.setName("sina");
			sinaWeiboItem.setUpdateTime(System.currentTimeMillis() / 1000);
			mData.add(sinaWeiboItem);
			this.mAdapter = new ImportFriendAdapter(mActivity, mData);
			this.mListView.setAdapter(mAdapter);
		} else {
			this.mListView.setAdapter(mAdapter);
		}
		mListView.loadComplete();
	}

	private void isBindSina(final SNSItem item) {

		boolean isBind = WeiboApi.getInstance().isBindSina();
		if (isBind) {
			importSnsFriend(item);
		} else {
			WeiboApi.getInstance().bindSina(mActivity, new WeiboBindListener() {

				@Override
				public void onBindSuccess() {
					importSnsFriend(item);
				}

				@Override
				public void onBindFaild(Object exp) {
					showToastMessage(getString(R.string.bind_sina_weibo_failed), 1);
				}
			});
		}
	}

	private void importSnsFriend(SNSItem item) {
		RequestParams params = new RequestParams();
		params.put("snsnm", item.getName());
		params.put("access_token", SharedPreferenceUtil.getSinaWeiboToken());
		params.put("snsusrid", SharedPreferenceUtil.getSinaWeiboUid());
		MyHttpConnect.getInstance().post(HttpContants.NET.SNS_IMPORT, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode,String content) {
						super.onSuccess(statusCode, content);
						Intent mIntent = new Intent();
						mIntent.setClass(mActivity, SNSFriendLstActivity.class);
						startActivity(mIntent);
						super.onSuccess(content);
					}

					@Override
					public void onFailure(Throwable error) {
						Intent mIntent = new Intent();
						mIntent.setClass(mActivity, SNSFriendLstActivity.class);
						startActivity(mIntent);
						super.onFailure(error);
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}
				});

	}

	@Override
	public void onStart() {
		super.onStart();
	}
}
