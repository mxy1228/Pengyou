/**
 * tuozhonghua_zk
 * 2013-10-16
 * TODO
 */
package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;


import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameNewAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameNewViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.RequestParams;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author tuozhonghua_zk
 *
 */
public class GameNewActivity extends CYBaseActivity implements OnClickListener {
    
    private ImageButton imgBtnSearch;
    private ImageButton mDownloadIBtn;
    private ImageButton mBackBtn;
    private ImageButton mDividerBtn;
    private TextView mDownloadCountTV;
    
    private PullToRefreshListView mPullListView;
    
    private boolean refreshing = false;
    private int gameNewPageNum = 1;
    
    private List<GameItem> mGameNewList;
    private GameNewAdapter mGameNewAdapter;
    private DownloadCountReceiver mDownloadCountReceiver;
    private InstallAppReceiver installAppReceiver;
    private UnInstallAppReceiver unstallAppReceiver;
    private DownloadAppReceiver downloadAppReceiver;
    private Context mContext;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_new_layout);
        mContext = this;
        this.initView();
        this.initEvent();
        this.initData();
        registReceiver();
    }
    
    @Override
    protected void onResume() {
        updateDownloadCount(-1);
        super.onResume();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregistReceiver();
    }

    @Override
    protected void initView() {
        View headerBar = findViewById(R.id.layout_headerbar);
        mBackBtn = (ImageButton) headerBar.findViewById(R.id.header_bar_left_ibtn);
        mBackBtn.setBackgroundResource(R.drawable.back_btn_xbg);
        mBackBtn.setVisibility(View.VISIBLE);
        headerBar.findViewById(R.id.header_feedback_btn).setVisibility(View.GONE);
        TextView mHeaderTV = (TextView) headerBar.findViewById(R.id.header_tv);
        mHeaderTV.setText(R.string.game_new_title);
        imgBtnSearch = (ImageButton) findViewById(R.id.header_search_btn);
        mDownloadIBtn = (ImageButton) findViewById(R.id.header_download_btn);
        mDownloadIBtn.setVisibility(View.VISIBLE);
        mDownloadCountTV = (TextView) findViewById(R.id.header_download_count_tv);
        
        mDividerBtn = (ImageButton) headerBar.findViewById(R.id.vertical_divider_header);
        mDividerBtn.setVisibility(View.VISIBLE);
        
        mPullListView = (PullToRefreshListView) findViewById(R.id.game_new_listview);
        
        mPullListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                BehaviorInfo behaviorInfo = new BehaviorInfo(CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_ID,
                        CYSystemLogUtil.GAMESTORE.BTN_RECOMMEND_NEWRANK_MOREGAME_GAMEDETAIL_NAME);
                CYSystemLogUtil.behaviorLog(behaviorInfo);
                if (mGameNewList == null || mGameNewList.size() == 0 || position >= mGameNewList.size()) {
                    return;
                }
                GameItem item = mGameNewList.get(position-1);
                if (null == item) {
                    return;
                }
                Intent mIntent = new Intent();
                mIntent.setClass(GameNewActivity.this, GameCircleDetailActivity.class);
                mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
                mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
                startActivity(mIntent);
            }
        });
        mPullListView.setOnRefreshListener(new RefreshListener() {

            @Override
            public void onRefresh() {
                if(Util.isNetworkConnected(GameNewActivity.this)){
                    requestGameNewData(true);
                }else{
                    Toast.makeText(GameNewActivity.this, GameNewActivity.this.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                    mPullListView.onRefreshFinish();
                    mPullListView.loadComplete();
                    
                }
            }
        });
        mPullListView.setOnLoadListener(new LoadListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onLoad() {
                requestGameNewData(false);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        imgBtnSearch.setOnClickListener(this);
        mDownloadIBtn.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (mGameNewList == null) {
            mGameNewList = new ArrayList<GameItem>();
        }
        if (mGameNewAdapter == null) {
            mGameNewAdapter = new GameNewAdapter(GameNewActivity.this, mGameNewList);
        }
        if (mPullListView != null) {
            mPullListView.setAdapter(mGameNewAdapter);
        }
        requestGameNewData(true);
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:28
     *
     * @param refresh
     */
    @SuppressWarnings("all")
    private void requestGameNewData(final boolean refresh) {
        RequestParams params = new RequestParams();
        if (refresh) {
            gameNewPageNum = 1;
        }
        this.refreshing = true;
        mPullListView.reset();
        params.put("page", String.valueOf(gameNewPageNum));
        params.put("count", String.valueOf(Config.PAGE_SIZE));
        MyHttpConnect.getInstance().post(HttpContants.NET.WORLD_NEWRANK, params,
                new JSONAsyncHttpResponseHandler(JSONAsyncHttpResponseHandler.RESULT_LIST,GameItem.class) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    public void onSuccessForList(List list) {
                        if (null == list || list.size() == 0) {
                            mPullListView.loadComplete();
                            return;
                        }
                        if (refresh) {
                            mGameNewList.clear();
                            mPullListView.onRefreshFinish();
                        }
                        mGameNewList.addAll(list);
                        gameNewPageNum++;
                        mGameNewAdapter.notifyDataSetChanged();
                        mPullListView.loadingFinish();
                        if (list.size() < Config.PAGE_SIZE) {
                        	mPullListView.loadComplete();
                        }
                        super.onSuccessForList(list);
                    }
                    @Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameNewActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
                    @Override
                    public void onFailure(Throwable error, String content) {
                        refreshing = false;
                        if(mPullListView != null){
                              mPullListView.onRefreshFinish();
                              mPullListView.loadingFinish();
                              mPullListView.loadComplete();
                        }
                        if (mContext != null) {
                        	showNetErrorDialog(mContext,new ReConnectListener() {
                        		
                        		@Override
                        		public void onReconnect() {
                        			requestGameNewData(refresh);
                        		}
                        	});
                        	
                        }
                        super.onFailure(error, content);
                    }
                });
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:46
     *
     */
    private void registReceiver(){
        if(mDownloadCountReceiver == null){
            mDownloadCountReceiver = new DownloadCountReceiver();
        }
        if (installAppReceiver == null) {
            installAppReceiver = new InstallAppReceiver();
        }
        if (unstallAppReceiver == null) {
            unstallAppReceiver = new UnInstallAppReceiver();
        }
        if (downloadAppReceiver == null) {
            downloadAppReceiver = new DownloadAppReceiver();
        }
        this.registerReceiver(unstallAppReceiver, new IntentFilter(Contants.ACTION.UNINSTALL));
        this.registerReceiver(downloadAppReceiver, new IntentFilter(DownloadParam.UPDATE_PROGRESS_ACTION));
        this.registerReceiver(installAppReceiver, new IntentFilter(Contants.ACTION.GAME_INSTALL));
        registerReceiver(mDownloadCountReceiver, new IntentFilter(Contants.ACTION.DOWNLOADING_COUNT));
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:53
     *
     */
    private void unregistReceiver(){
        if(mDownloadCountReceiver != null){
            unregisterReceiver(mDownloadCountReceiver);
        }
        if (installAppReceiver != null) {
            this.unregisterReceiver(installAppReceiver);
            installAppReceiver = null;
        }
        if (unstallAppReceiver != null) {
            this.unregisterReceiver(unstallAppReceiver);
            unstallAppReceiver = null;
        }
        if (downloadAppReceiver != null) {
            this.unregisterReceiver(downloadAppReceiver);
            downloadAppReceiver = null;
        }
    }
    /**
     * 
     * @author tuozhonghua_zk
     *
     */
    private class DownloadCountReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int status = intent.getIntExtra(DownloadParam.STATE, -1);
            updateDownloadCount(status);
        }
    }
    /**
     * 
     * @author tuozhonghua_zk
     *
     */
    class InstallAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
            updateListViewItem(getGameItemByPackage(packageName));
        }

    }

    /**
     * 
     * @author tuozhonghua_zk
     *
     */
    class UnInstallAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
            updateListViewItem(getGameItemByPackage(packageName));
        }

    }

    /**
     * 
     * @author tuozhonghua_zk
     *
     */
    class DownloadAppReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (null == intent) {
                return;
            }
            int state = intent.getIntExtra(DownloadParam.STATE, 100);
            String packageName = intent.getStringExtra(DownloadParam.PACKAGE_NAME);
            switch (state) {
            case DownloadParam.TASK.DELETE:
                log.d("删除:"+packageName);
                updateListViewItem(getGameItemByPackage(packageName));
                break;
            case DownloadParam.TASK.DONE:
                updateListViewItem(getGameItemByPackage(packageName));
                break;
            case DownloadParam.TASK.ADD:
                updateListViewItem(getGameItemByPackage(packageName));
                break;
            case DownloadParam.TASK.CONTINUE:
                updateListViewItem(getGameItemByPackage(packageName));
                break;
            }
        }
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16下午2:06:31
     *
     * @param item
     */
    private void updateListViewItem(GameItem item) {
        if (item == null) {
            return;
        }
        int firstVisiblePosition;
        int index;
        View view = null;
        if (mGameNewList == null || mGameNewList.isEmpty()) {
            return;
        }
        firstVisiblePosition = mPullListView.getFirstVisiblePosition();
        index = mGameNewList.indexOf(item)+1;
        if (index < 0) {
            return;
        }
        view = mPullListView.getChildAt(index - firstVisiblePosition);
        if (null == view) {
            mPullListView.getAdapter().getView(index, view,mPullListView);
        }
        if (view != null) {
            GameNewViewCache viewCache = (GameNewViewCache) view.getTag();
            if(viewCache==null){
                log.e("视图为空!");
                return;
            }
            if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
                viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.play_btn_xbg);
                viewCache.getBtnDownloadGame().setText(R.string.game_play);
                viewCache.getBtnDownloadGame().setTextColor(GameNewActivity.this.getResources().getColor(R.color.white));
            } else {
                if (!DownloadDao.getInstance(this).isHasInfo(item.getIdentifier(),item.getVersion())) {
                    viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                    viewCache.getBtnDownloadGame().setText(R.string.game_download);
                    viewCache.getBtnDownloadGame().setTextColor(GameNewActivity.this.getResources().getColor(R.color.white));
                    return;
                }
                DownloadItem downloadItem = DownloadDao.getInstance(this).getDowloadItem(item.getIdentifier(), item.getVersion());
                if (downloadItem == null || TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
                    viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                    viewCache.getBtnDownloadGame().setText(R.string.game_download);
                    viewCache.getBtnDownloadGame().setTextColor(GameNewActivity.this.getResources().getColor(R.color.white));
                } else {
                    if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
                        viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                        viewCache.getBtnDownloadGame().setText(R.string.download_btn_install);
                        viewCache.getBtnDownloadGame().setTextColor(GameNewActivity.this.getResources().getColor(R.color.white));
                    } else {
                        if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
                                || downloadItem.getmState() == DownloadParam.C_STATE.WAITING
                                || downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
                            viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.downloading_btn_xbg);
                            viewCache.getBtnDownloadGame().setText(R.string.game_downloading);
                            viewCache.getBtnDownloadGame().setTextColor( GameNewActivity.this.getResources().getColor(R.color.downloading_text));
                        } else {
                            viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                            viewCache.getBtnDownloadGame().setText(R.string.game_download);
                            viewCache.getBtnDownloadGame().setTextColor(GameNewActivity.this.getResources().getColor(R.color.white));
                        }
                    }
                }

            }
        }
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16下午2:06:38
     *
     * @param packageName
     * @return
     */
    private GameItem getGameItemByPackage(String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return null;
        }
        GameItem mGameItem = null;
        if (null == mGameNewList || mGameNewList.isEmpty()) {
            return mGameItem;
        }
        for (GameItem item : mGameNewList) {
            if (item == null || TextUtils.isEmpty(item.getIdentifier())) {
                continue;
            }
            if (packageName.equals(item.getIdentifier())) {
                mGameItem = item;
                break;
            }
        }
        return mGameItem;
    }
    
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:14
     *
     * @param status
     */
    private void updateDownloadCount (int status) {
        Animation anim = AnimationUtils.loadAnimation(this,R.anim.download_count);
        int count = DownloadDao.getInstance(this).getDownloadingTaskSize();
        log.d("MainActivity: game Count is:" + count);
        if (count > 0) {
            mDownloadCountTV.setVisibility(View.VISIBLE);
            if (count <= 99) {
                mDownloadCountTV.setText(String.valueOf(count));
            } else {
                mDownloadCountTV.setText("N");
            }
            if (status == DownloadParam.TASK.ADD ) {
                mDownloadCountTV.startAnimation(anim);
            }
        } else {
            mDownloadCountTV.setVisibility(View.GONE);
            mDownloadCountTV.setText("");
        }
    }
    
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
        case R.id.header_bar_left_ibtn://返回
            finish();
            break;
        case R.id.header_search_btn:
            intent.setClass(this, SearchActivity.class);
            startActivity(intent);
            break;
        case R.id.header_download_btn:
            intent.setClass(this, DownloadActivity.class);
            startActivity(intent);
            break;
        default:
            break;
        }
    }
}
