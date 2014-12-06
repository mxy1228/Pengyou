package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.InstallGameAdapter;
import com.cyou.mrd.pengyou.db.MyGameDao;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.log.CYLog;
import com.cyou.mrd.pengyou.utils.Util;

/**
 * 已安装
 * 
 * @author wangkang
 * 
 */
public class InstallGameFragment extends BaseFragment {
	private CYLog log = CYLog.getInstance();
	private Activity mActivity;
	private View contentView;
	ListView mPullListView;
	private List<GameItem> gameList = new ArrayList<GameItem>();
	private InstallGameAdapter myGameAdapter;
	private TextView txtEmpty;
	private MyGameDao mMyGameDao;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		log.d("Init InstallGameFragment onCreateView ");
		this.mActivity = getActivity();
		if (mMyGameDao == null) {
			mMyGameDao = new MyGameDao(getActivity());
		}
		contentView = inflater.from(mActivity).inflate(
				R.layout.install_game_layout, null);
		initView();
		return contentView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onResume() {
		super.onResume();
		if(mActivity==null){
			mActivity=getActivity();
		}
		initData();
		
	}
	/**
	 * 加载游戏列表
	 */
	@SuppressWarnings("all")
	public void loadInstallGameList() {
		if(gameList!=null){
			gameList.clear();
		}
		List<GameItem> tempLst = mMyGameDao.selectAll();
		if (null != tempLst && tempLst.size() > 0) {
			for (int i = 0; i < tempLst.size(); i++) {
				GameItem item = tempLst.get(i);
				if (Util.isInstallByread(item.getIdentifier())) {
					item.setFullsize(Util.getInstallGameSize(mContext,item.getIdentifier()));
					gameList.add(item);
				}
			}
		}
		try {
			Collections.sort(gameList, new ComparatorGameSize());
		} catch (Exception e) {
			e.fillInStackTrace();
			log.e(e);
		}
		txtEmpty.setVisibility(View.GONE);
		mPullListView.setVisibility(View.VISIBLE);
		if (gameList == null || gameList.size() == 0) {
			txtEmpty.setVisibility(View.VISIBLE);
			mPullListView.setVisibility(View.GONE);
			mPullListView.setEmptyView(txtEmpty);
		} else {
			myGameAdapter.updateData(gameList);
			myGameAdapter.notifyDataSetChanged();
		}
	}

	public void initData() {
		if(mActivity==null){
			mActivity=getActivity();
		}
		if(mPullListView==null){
//			initView();
			return;
		}
		if (gameList == null || gameList.size() == 0) {
			gameList = new ArrayList<GameItem>();
			myGameAdapter = new InstallGameAdapter(mActivity, gameList);
			log.d("InstallGame initData"+mPullListView+"__"+myGameAdapter);
			mPullListView.setAdapter(myGameAdapter);
		} else {
			if (myGameAdapter == null) {
				myGameAdapter = new InstallGameAdapter(mActivity, gameList);
			}
			mPullListView.setAdapter(myGameAdapter);
		}
		loadInstallGameList();
	}

	private void initView() {
		if (null == contentView) {
			return;
		}
		txtEmpty = (TextView) contentView.findViewById(R.id.txt_empty);
		txtEmpty.setText(R.string.nodata_install_game);
		mPullListView = (ListView) contentView.findViewById(R.id.mygame_lstview);
	}

	String resultMsg = "";
	String resultErrorMsg = "";

	public InstallGameFragment() {
		super();
	}

	public class ComparatorGameSize implements Comparator {

		public int compare(Object arg0, Object arg1) {
			try {
				GameItem item1 = (GameItem) arg0;
				GameItem item2 = (GameItem) arg1;
				Double fullsize1 = Double.parseDouble(item1.getFullsize());
				Double fullsize2 = Double.parseDouble(item2.getFullsize());
				int flag = fullsize2.compareTo(fullsize1);
				return flag;
			} catch (Exception e) {
				e.fillInStackTrace();
				log.e(e);
			}
			return 0;
		}

	}
}
