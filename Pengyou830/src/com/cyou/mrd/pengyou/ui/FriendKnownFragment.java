package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.FriendKnownAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.FriendItem;
import com.cyou.mrd.pengyou.entity.base.SNSFriendBase;
import com.cyou.mrd.pengyou.entity.base.YouMayKnownBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.SharedPreferenceUtil;
import com.cyou.mrd.pengyou.utils.WeiboApi;
import com.cyou.mrd.pengyou.utils.WeiboApi.WeiboBindListener;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class FriendKnownFragment extends BaseFragment implements OnClickListener{

	private CYLog log = CYLog.getInstance();

	private PullToRefreshListView mListView;

	private Activity mActivity;

	private List<FriendItem> mData;
	private FriendKnownAdapter mAdapter;
	private MyHttpConnect mConn;
	private Button mBindBtn;
	private View mFootView;

	public FriendKnownFragment() {
		super();

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_ID,
				CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mActivity = getActivity();
		View view = inflater.inflate(R.layout.friend_known, null);
		mListView = (PullToRefreshListView)view.findViewById(R.id.friend_known_lv) ;
		mListView.setOnItemClickListener(new ItemClickListener());
		mFootView = inflater.inflate(R.layout.you_may_known_footer, null);
		mBindBtn = (Button)mFootView.findViewById(R.id.you_may_known_footer_btn);
		mBindBtn.setOnClickListener(this);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if(mConn == null){
			mConn = MyHttpConnect.getInstance();
		}
		if (mData == null) {
			mData = new ArrayList<FriendItem>();
		}
		if (mAdapter == null) {
			this.mAdapter = new FriendKnownAdapter(getActivity(), mData);
			requestSameGameFriends();
		}
		this.mListView.setAdapter(mAdapter);
	}

	private void requestSameGameFriends() {
		RequestParams params = new RequestParams();
		mConn.post(HttpContants.NET.SAME_GAME_FRIENDS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("request friends result = " + content);
						try {
							YouMayKnownBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<YouMayKnownBase>() {
											});
							if (base != null && base.getData() != null
									&& !base.getData().isEmpty()) {
								
							}
							mListView.loadComplete();
							if (WeiboApi.getInstance().isBindSina()) {
								getSNSFriend("sina", SharedPreferenceUtil
										.getSinaWeiboUid(),
										SharedPreferenceUtil
												.getSinaWeiboToken());
							} else {
								mListView.addFooterView(mFootView);
							}
							mAdapter.notifyDataSetChanged();
						} catch (Exception e) {
							log.e(e);
						}
					}

				});
	}

	private void getSNSFriend(final String snsnm, String snsusrid,
			String access_token) {
		RequestParams params = new RequestParams();
		params.put("snsnm", snsnm);
		params.put("snsusrid", snsusrid);
		params.put("access_token", access_token);
		mConn.post(HttpContants.NET.GET_SNS_FRIENDS, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("get sns friends result = " + content);
						try {
							SNSFriendBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.readValue(
									content,
									new TypeReference<SNSFriendBase>() {
									});
							if (base != null) {
								for (FriendItem item : base.getData()) {
									if (snsnm.equals("sina")) {
										
									}
								}
							}
							mAdapter.notifyDataSetChanged();
							mListView.loadComplete();
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}
				});
	}

	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			FriendItem item = (FriendItem) mListView.getItemAtPosition(arg2);
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_FRD_DETAIL_ID,
					CYSystemLogUtil.ME.BTN_MYFRIEND_NEW_FRD_DETAIL_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
			if (item != null) {
				Intent intent = new Intent(mContext,FriendInfoActivity.class);
				intent.putExtra(Params.FRIEND_INFO.UID, item.getUid());
				intent.putExtra(Params.FRIEND_INFO.NICKNAME, item.getNickname());
				intent.putExtra(Params.FRIEND_INFO.GENDER, item.getGender());
				mContext.startActivity(intent);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.you_may_known_footer_btn:
			WeiboApi.getInstance().bindSina(getActivity(),
				new WeiboBindListener() {

					@Override
					public void onBindSuccess() {
						mData.clear();
						requestSameGameFriends();
					}

					@Override
					public void onBindFaild(Object exp) {
						// TODO Auto-generated method stub

					}
				});
			break;

		default:
			break;
		}
	}
}
