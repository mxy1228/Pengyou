package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RelationCircleMessageAdapter;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.RelationCircleMsgDao;
import com.cyou.mrd.pengyou.entity.RelationCircleMessageItem;
import com.cyou.mrd.pengyou.entity.SystemCountMsgItem;
import com.cyou.mrd.pengyou.entity.base.RelationCircleMessageBase;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.JsonUtils;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.WaitingDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 消息里面的通知
 * @author 
 *
 */
public class RelationCircleMessageFragement extends Fragment implements OnClickListener{

	private CYLog log = CYLog.getInstance();
	private PullToRefreshListView mListView;
	private WaitingDialog mWaitingDialog;
	private TextView mEmptyTV;
	private Button mLoadMoreBtn;

	private List<RelationCircleMessageItem> mData;
	private RelationCircleMessageAdapter mAdapter;
	private RelationCircleMsgDao mDao;
    private String  mGCid ;
	private MyHttpConnect mHttpConnect;
	private Activity mActivity;
	private AlertDialog mNetErrorDialog;
	
	public RelationCircleMessageFragement(){
		
	}
	
	public RelationCircleMessageFragement(String gcid){
		   this.mGCid = gcid;
	}

//	public RelationCircleMessageFragement(Context context) {
//		this.mActivity = context;
//	}
	
	@Override
	public void onAttach(Activity activity) {
		this.mActivity = activity;
		this.mWaitingDialog = new WaitingDialog(mActivity);
		this.mNetErrorDialog = new AlertDialog.Builder(mActivity)
		.setMessage(R.string.net_error)
		.setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		}).setNegativeButton(R.string.my_nearby_reset, new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				requestMessage();
				dialog.dismiss();
			}
		}).create();
		if (mHttpConnect == null) {
			mHttpConnect = MyHttpConnect.getInstance();
		}
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.ME.BTN_MYMSG_RELATION_ID,
				CYSystemLogUtil.ME.BTN_MYMSG_RELATION_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
		if (mHttpConnect == null) {
			mHttpConnect = MyHttpConnect.getInstance();
		}
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.relation_circle_message, null);
		this.mLoadMoreBtn = (Button)inflater.inflate(R.layout.notification_foot_btn,null);
		this.mLoadMoreBtn.setOnClickListener(this);
		this.mEmptyTV = (TextView)view.findViewById(R.id.relation_circle_message_empty);
		this.mListView = (PullToRefreshListView) view
				.findViewById(R.id.relation_circle_message_lv);
		this.mListView.setOnLoadListener(new LoadListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {

			}

			@Override
			public void onLoad() {
				requestMessage();
			}
		});
		mListView.setOnItemClickListener(new ItemClickListener());
		mListView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				BehaviorInfo behaviorInfo = new BehaviorInfo(
						CYSystemLogUtil.ME.BTN_MYMSG_DELETE_ID,
						CYSystemLogUtil.ME.BTN_MYMSG_DELETE_NAME);
				CYSystemLogUtil.behaviorLog(behaviorInfo);
				new AlertDialog.Builder(mActivity)
						.setPositiveButton(getString(R.string.delete),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										RelationCircleMessageItem item = (RelationCircleMessageItem) mData
												.get(arg2);
										deleteMsg(item, arg2);
									}
								}).create().show();
				return false;
			}

		});
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		if (mData == null) {
			mData = new ArrayList<RelationCircleMessageItem>();
		}

		if (mAdapter == null) {
			mAdapter = new RelationCircleMessageAdapter(mActivity, mData);
			mListView.setAdapter(mAdapter);
//			requestMessage();
		}
		if(mDao == null){
			mDao = new RelationCircleMsgDao(getActivity());
		}
	}

	public void requestMessage() {
		RequestParams params = new RequestParams();
		if(!TextUtils.isEmpty(mGCid)){
			params.put("gcid", mGCid);
		}
		mHttpConnect.post(HttpContants.NET.GET_MESSAGE, params,
				new AsyncHttpResponseHandler() {
					@Override
					public void onStart() {
						mNetErrorDialog.dismiss();
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
						log.i("get relation circle msg result = " + content);
						try {
							RelationCircleMessageBase base = new ObjectMapper()
							.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
									.readValue(
											content,
											new TypeReference<RelationCircleMessageBase>() {
											});
							if(mData!=null){
								mData.clear();
							}
							if (base != null && base.getData() != null && !base.getData().isEmpty()) {
								for(RelationCircleMessageItem item : base.getData()){
									mDao.insert(item);
								}
								
								mData.addAll(base.getData());
								addFootView();
							}else{
								List<RelationCircleMessageItem> list = new RelationCircleMsgDao(mActivity).selectMsgs();
								mData.addAll(list);
								removeFootView();
							}
							mListView.loadComplete();
							if(mData.isEmpty()){
								mEmptyTV.setVisibility(View.VISIBLE);
								removeFootView();
							}else{
								mEmptyTV.setVisibility(View.GONE);
							}
							
							SystemCountMsgItem.cleanRelationCircleMsg();
							Util.requestFansAndLetterCount(getActivity(),SystemCountMsgItem.SYSTEM);
						} catch (Exception e) {
							log.e(e);
						}
					}
					
					@Override
					public void onFailure(Throwable error, String content) {
						mNetErrorDialog.show();
					}
				});
	}

	
	private void addFootView(){
		mListView.addFooterView(mLoadMoreBtn);
	}
	
	private void removeFootView(){
		mListView.removeFooterView(mLoadMoreBtn);
	}
	
	private class ItemClickListener implements OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			RelationCircleMessageItem item = (RelationCircleMessageItem) mListView
					.getItemAtPosition(arg2);
			if(item == null){
				return;
			}
			Intent intent = new Intent(mActivity, DynamicDetailActivity.class);
			intent.putExtra(Params.DYNAMIC_DETAIL.AID, item.getAid());
			intent.putExtra(Params.DYNAMIC_DETAIL.UID, item.getUid());
			intent.putExtra(Params.DYNAMIC_DETAIL.POSITION, arg2);
			startActivityForResult(intent, 1);
			// 点击关系圈ITEM
			BehaviorInfo behaviorInfo = new BehaviorInfo(
					CYSystemLogUtil.ME.BTN_MYMSG_DYNAMIC_COMMENT_ID,
					CYSystemLogUtil.ME.BTN_MYMSG_DYNAMIC_COMMENT_NAME);
			CYSystemLogUtil.behaviorLog(behaviorInfo);
		}

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		log.d("onActivityResult");
		switch (requestCode) {
		case 1:
			if(data != null){
				int position = data.getIntExtra(Params.DYNAMIC_DETAIL.POSITION, 0);
				RelationCircleMessageItem item = mData.get(position);
				Iterator<RelationCircleMessageItem> it = mData.iterator();
				mDao.deleteByAid(item.getAid());
				while(it.hasNext()){
					RelationCircleMessageItem i = it.next();
					if(i.getAid() == item.getAid()){
						it.remove();
					}
				}
				mAdapter.notifyDataSetChanged();
				if(mData.isEmpty()){
					mEmptyTV.setVisibility(View.VISIBLE);
					removeFootView();
				}else{
					mEmptyTV.setVisibility(View.GONE);
				}
				Util.requestFansAndLetterCount(getActivity(),SystemCountMsgItem.SYSTEM);
			}
			break;

		default:
			break;
		}
	}
	
	private void deleteMsg(final RelationCircleMessageItem item, final int position) {
		RequestParams params = new RequestParams();
		params.put("mid", String.valueOf(item.getMid()));
		MyHttpConnect.getInstance().post(HttpContants.NET.DELETE_RELATION_MSG,
				params, new AsyncHttpResponseHandler() {

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
						LoginOutDialog dialog = new LoginOutDialog(mActivity);
						dialog.create().show();
					}

					@Override
					public void onSuccess(int statusCode, String content) {
						super.onSuccess(statusCode, content);
						if (TextUtils.isEmpty(content)) {
							return;
						}
						log.i("delete relation msg result = " + content);
						try {
							String result = JsonUtils.getJsonValue(content,
									Params.HttpParams.SUCCESSFUL);
							if (!TextUtils.isEmpty(result)) {
								if (Integer.parseInt(result) == Params.HttpParams.SUCCESS) {
									mDao.delete(item.getMid());
									Toast.makeText(mActivity,
											R.string.delete_success,
											Toast.LENGTH_SHORT).show();
									mData.remove(position);
									mAdapter.notifyDataSetChanged();
									if(mData.isEmpty()){
										List<RelationCircleMessageItem> list = mDao.selectMsgs();
										mData.addAll(list);
										removeFootView();
										if(mData.isEmpty()){
											mEmptyTV.setVisibility(View.VISIBLE);
											removeFootView();
										}
									}else{
										mEmptyTV.setVisibility(View.GONE);
									}
								} else {
									Toast.makeText(mActivity, "failed",
											Toast.LENGTH_SHORT).show();
								}
							}
							Util.requestFansAndLetterCount(getActivity(),SystemCountMsgItem.SYSTEM);
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
		case R.id.relation_circle_message_load_more_btn:
			removeFootView();
			List<RelationCircleMessageItem> list = new RelationCircleMsgDao(mActivity).selectMsgs();
			mData.clear();
			mData.addAll(list);
			mAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}


}
