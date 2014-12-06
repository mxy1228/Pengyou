package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.UpdateGameAdapter;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.download.DownloadService;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.NetUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.UpdateGameViewCache;

/**
 * 可更新
 * 
 * @author wangkang
 * 
 */
public class UpdateGameFragment extends BaseFragment implements OnClickListener {
	private CYLog log = CYLog.getInstance();
	private Activity mActivity;
	private View contentView;
	private ListView mPullListView;
	private List<GameItem> gameList = new ArrayList<GameItem>();
	private UpdateGameAdapter myGameAdapter;
	private TextView txtEmpty;
	private RelativeLayout rlytTop;
	private MyGameDao mMyGameDao;
	private DownloadDao mDownloadDao;
	private Button btnUpdateAll;
	private UpdateStatusReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		this.mActivity = getActivity();
		if (mMyGameDao == null) {
			mMyGameDao = new MyGameDao(getActivity());
		}
		if (mDownloadDao == null) {
			mDownloadDao = DownloadDao.getInstance(mContext);;
		}
		contentView = inflater.from(mActivity).inflate(R.layout.update_game_layout, null);
		initView();
		return contentView;
	}

	@Override
	public void onResume() {
		initData();
		registReceiver();
		super.onResume();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
		if (mReceiver != null) {
			mActivity.unregisterReceiver(mReceiver);
		}
		super.onDestroyView();
	}

	public void registReceiver() {
		// 注册下载监听器
		if (mReceiver == null) {
			mReceiver = new UpdateStatusReceiver();
		}
		mActivity.registerReceiver(mReceiver, new IntentFilter(DownloadParam.UPDATE_PROGRESS_ACTION));
	}
	private void downloadAll() {
		if (!NetUtil.isNetworkAvailable()) {
			Toast.makeText(mContext,mContext.getString(R.string.download_error_network_error),0).show();
			return;
		}
		log.d("全部更新");
		if (gameList != null) {
			for (GameItem item : gameList) {
				downloadGame(item);
			}
//			myGameAdapter.notifyDataSetChanged();
		}
	}

	private void downloadGame(GameItem item) {
		Intent intent = new Intent(mContext, DownloadService.class);
		DownloadItem downloadItem = mDownloadDao.getDowloadItem(
				item.getIdentifier(), item.getVersion());
		if (!TextUtils.isEmpty(downloadItem.getmPackageName())) {
			switch (downloadItem.getmState()) {
			case DownloadParam.C_STATE.DOWNLOADING:
				Util.showToast(mContext.getApplicationContext(),mContext.getString(R.string.had_contain_download_task),Toast.LENGTH_SHORT);
				break;
			case DownloadParam.C_STATE.PAUSE:
				Util.showToast(mContext.getApplicationContext(),mContext.getString(R.string.had_contain_download_task_pause),Toast.LENGTH_SHORT);
				break;
			case DownloadParam.C_STATE.DONE:
				Util.showToast(mContext.getApplicationContext(),mContext.getString(R.string.had_contain_download_task_done),Toast.LENGTH_SHORT);
				break;
			}
		} else {
			if (!Util.isDownloadUrl(item.getFullurl()) || TextUtils.isEmpty(item.getVersion())) {
				Util.showToast(mContext.getApplicationContext(),mContext.getString(R.string.download_url_error),Toast.LENGTH_SHORT);
				return;
			}
			intent.putExtra(DownloadParam.STATE, DownloadParam.TASK.ADD);
			downloadItem = new DownloadItem();
			downloadItem.setmName(item.getName());
			downloadItem.setmLogoURL(item.getIcon());
			downloadItem.setmSize(item.getFullsize());
			downloadItem.setmURL(item.getFullurl());
			downloadItem.setGameCode(item.getGamecode());
			downloadItem.setmPackageName(item.getIdentifier());
			downloadItem.setVersionName(item.getVersion());
			intent.putExtra(DownloadParam.DOWNLOAD_ITEM, downloadItem);
			mContext.startService(intent);
			updateBtnStatus(item);
		}
	}
	/**
	 * 刷新更新按钮的状态
	 * tuozhonghua_zk
	 * 2013-11-7下午5:47:01
	 *
	 * @param item
	 */
	private void updateBtnStatus (GameItem item) {
		int firstVisiblePosition = mPullListView.getFirstVisiblePosition();
		int index = gameList.indexOf(item);
		View view = mPullListView.getChildAt(index - firstVisiblePosition);
		if (view != null) {
			UpdateGameViewCache viewCache = (UpdateGameViewCache)view.getTag();
			viewCache.getBtnDownloadGame().setText(R.string.download_btn_updatting);
			viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.btn_game_downloading_normal);
		}
	}

	@Override
	public void onStart() {
//		initData();
		super.onStart();
	}

	public void loadUpdateGameList() {
		if (gameList != null) {
			gameList.clear();
		}
		List<GameItem> tempLst = new MyGameDao().selectAll();
		if (null != tempLst && tempLst.size() > 0) {
			for (int i = 0; i < tempLst.size(); i++) {
				GameItem item = tempLst.get(i);
				log.d("item version Code is:" + item.getVersioncode() + "___"
						+ Util.getAppVersionCode(item.getIdentifier()));
				if (Util.isInstallByread(item.getIdentifier())) {
					boolean isUpdate = Util.isUpdate(Util.getAppVersionCode(item.getIdentifier()),item.getVersioncode());
					if (isUpdate) {
						log.d("服务端游戏相关信息:"+item.getGamecode()+"__"+item.getIdentifier()+"__"+item.getVersion()+"__"+item.getVersioncode());
						gameList.add(item);
					}
				}
			}
		}
	}
	
	/**
	 * 
	 * tuozhonghua_zk
	 * 2013-10-12下午3:25:00
	 *
	 * @return
	 */
	public int getUpdateListCount () {
		int count = 0;
		if (gameList != null) {
			count = gameList.size();
		}
		return count;
	}

	private void initData() {
		if (gameList == null || gameList.size() == 0) {
			gameList = new ArrayList<GameItem>();
			myGameAdapter = new UpdateGameAdapter(mActivity, gameList);
			mPullListView.setAdapter(myGameAdapter);
		} else {
			if (myGameAdapter == null) {
				myGameAdapter = new UpdateGameAdapter(mActivity, gameList);
			}
			mPullListView.setAdapter(myGameAdapter);
		}
		loadUpdateGameList();
		mPullListView.setVisibility(View.VISIBLE);
		if (gameList == null || gameList.size() == 0) {
			txtEmpty.setVisibility(View.VISIBLE);
			mPullListView.setVisibility(View.GONE);
			mPullListView.setEmptyView(txtEmpty);
			rlytTop.setVisibility(View.GONE);
			DownloadActivity downloadActivity = (DownloadActivity) mActivity;
            if (null != downloadActivity) {
                downloadActivity.setUpdateCount(0);
            }
		} else {
			try {
	            DownloadActivity downloadActivity = (DownloadActivity) mActivity;
	            if (null != downloadActivity) {
	                downloadActivity.setUpdateCount(gameList.size());
	            }
	            rlytTop.setVisibility(View.VISIBLE);
		    } catch (Exception e) {
	            log.e(e);
		    }
			myGameAdapter.updateData(gameList);
			myGameAdapter.notifyDataSetChanged();
//			mPullListView.loadingFinish();
		}
	}

	private void initView() {
		if (null == contentView) {
			return;
		}
		txtEmpty = (TextView) contentView.findViewById(R.id.txt_empty);
		txtEmpty.setText(R.string.nodata_update_game);
		mPullListView = (ListView) contentView.findViewById(R.id.mygame_lstview);
		// ((ViewGroup) mPullListView.getParent()).addView(emptyView);

//		mPullListView.setOnLoadListener(new LoadListener() {
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
//
//			}
//
//			@Override
//			public void onLoad() {
//				// loadFavGameList();
//			}
//		});
		rlytTop = (RelativeLayout) contentView.findViewById(R.id.rlyt_game_tile);
		btnUpdateAll = (Button) contentView.findViewById(R.id.btn_update_all);
		btnUpdateAll.setOnClickListener(this);
	}

	String resultMsg = "";
	String resultErrorMsg = "";

	public UpdateGameFragment() {
		super();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_update_all:
			downloadAll();
			break;

		}
	}
	private class UpdateStatusReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent != null) {
				int state = intent.getIntExtra(DownloadParam.STATE, 0);
				String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
				if (packageName != null && !"".equals(packageName) && state > 0 && gameList != null && gameList.size() > 0) {
					for (GameItem item : gameList) {
						if (item.getIdentifier().equals(packageName)) {
							updateListviewItem(item,state);
							break;
						}
					}
				}
			}
			
		}
	}
	
	private void updateListviewItem (GameItem item,int status) {
		if (gameList != null && gameList.size() > 0) {
			int firstVisiblePosition = mPullListView.getFirstVisiblePosition();
			int index = gameList.indexOf(item);
			View view = mPullListView.getChildAt(index - firstVisiblePosition);
			if (view != null) {
				UpdateGameViewCache viewCache = (UpdateGameViewCache)view.getTag();
				switch (status) {
				case DownloadParam.TASK.DONE:
					viewCache.getBtnDownloadGame().setText(R.string.done);
					break;
				case DownloadParam.TASK.DELETE:
					viewCache.getBtnDownloadGame().setText(R.string.download_btn_update);
					break;
				case DownloadParam.TASK.CONTINUE:
					viewCache.getBtnDownloadGame().setText(R.string.download_btn_updatting);
					viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.btn_game_downloading_normal);
					break;
				default:
					break;
				}
			}
		}
	}
}
