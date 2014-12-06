/**
 * tuozhonghua_zk
 * 2013-10-18
 * TODO
 */
package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameSpecialDetailAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Contants;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.db.DownloadDao;
import com.cyou.mrd.pengyou.download.DownloadItem;
import com.cyou.mrd.pengyou.download.DownloadParam;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.GameSpecailDetailBean;
import com.cyou.mrd.pengyou.entity.SeminarBean;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.viewcache.GameSpecailDetailViewCache;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * @author tuozhonghua_zk
 *
 */
public class GameSpecialDetailActivity extends CYBaseActivity implements
        OnClickListener {
    private ImageButton imgBtnSearch;
    private ImageButton mBackBtn;
    private ImageButton mDownloadIBtn;
    private ImageButton mDividerBtn;
    private TextView mDownloadCountTV;
    
    private List<GameItem> mGameSpecialList;
    private PullToRefreshListView mPullListView;
    private GameSpecialDetailAdapter mAdapter;
    
    private DownloadCountReceiver mDownloadCountReceiver;
    private InstallAppReceiver installAppReceiver;
    private UnInstallAppReceiver unstallAppReceiver;
    private DownloadAppReceiver downloadAppReceiver;
    
    private boolean refreshing = false;
    private int gameNewPageNum = 1;
    
    private String mSpecialId;
    private String mSpecialName;
    private Long mSpecialDate;
    private String mSpecialImage;
    private String mSpecialDesc;
    
    private SeminarBean mSpecailGame;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_special_detail_layout);
        mContext = this;
        initView();
        initEvent();
        initData();
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
        mHeaderTV.setText(R.string.game_special_title);
        imgBtnSearch = (ImageButton) findViewById(R.id.header_search_btn);
        imgBtnSearch.setVisibility(View.GONE);
        mDownloadIBtn = (ImageButton) findViewById(R.id.header_download_btn);
        mDownloadIBtn.setVisibility(View.VISIBLE);
        mDownloadCountTV = (TextView) findViewById(R.id.header_download_count_tv);
        mDividerBtn = (ImageButton) headerBar.findViewById(R.id.vertical_divider_header);
        mDividerBtn.setVisibility(View.GONE);
        


        mPullListView = (PullToRefreshListView) findViewById(R.id.game_special_detail_listview);
        mPullListView.setDivider(null);
        mPullListView.setDividerHeight(0);
        mPullListView.setOnRefreshListener(new RefreshListener() {

            @Override
            public void onRefresh() {
                if(Util.isNetworkConnected(GameSpecialDetailActivity.this)){
                    requestData(true);
                }else{
                    Toast.makeText(GameSpecialDetailActivity.this, GameSpecialDetailActivity.this.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
                    mPullListView.onRefreshFinish();
                    mPullListView.loadComplete();
                    
                }
            }
        });
        mPullListView.setOnLoadListener(new LoadListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {

            }

            @Override
            public void onLoad() {
                requestData(false);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        imgBtnSearch.setOnClickListener(this);
        mDownloadIBtn.setOnClickListener(this);
        mPullListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent,View view,int position, long id) {
                if (position == 0 || mGameSpecialList == null || mGameSpecialList.size() == 0 || position > mGameSpecialList.size()) {
                    return;
                }
                GameItem item = mGameSpecialList.get(position-1);
                if (null == item) {
                    return;
                }
                Intent mIntent = new Intent();
                mIntent.setClass(GameSpecialDetailActivity.this, GameCircleDetailActivity.class);
                mIntent.putExtra(Params.INTRO.GAME_CODE, item.getGamecode());
                mIntent.putExtra(Params.INTRO.GAME_NAME, item.getName());
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void initData() {
        
        mSpecialId = getIntent().getStringExtra(Params.GAME_SPECIAL.SPECIAL_ID);
//        mSpecialName = getIntent().getStringExtra(Params.GAME_SPECIAL.SPECIAL_NAME);
//        mSpecialDate = getIntent().getLongExtra(Params.GAME_SPECIAL.SPECIAL_DATE,0);
//        mSpecialImage = getIntent().getStringExtra(Params.GAME_SPECIAL.SPECIAL_IMAGE);
//        mSpecialDesc = getIntent().getStringExtra(Params.GAME_SPECIAL.SPECIAL_DESC);
//        
//        SeminarBean mSpecailGame = new SeminarBean();
//        mSpecailGame.setName(mSpecialName);
//        mSpecailGame.setTopicdate(mSpecialDate);
//        mSpecailGame.setPicture(mSpecialImage);
//        mSpecailGame.setDesc(mSpecialDesc);
        
        if (mGameSpecialList == null) {
            mGameSpecialList = new ArrayList<GameItem>();
        }
        if (mSpecailGame == null) {
            mSpecailGame = new SeminarBean();
        }
        if (mAdapter == null) {
            mAdapter = new GameSpecialDetailAdapter(GameSpecialDetailActivity.this,mSpecailGame, mGameSpecialList);
        }
        if (mPullListView != null) {
            mPullListView.setAdapter(mAdapter);
        }
        if (mSpecialId != null && !"".equals(mSpecialId)){
            requestData(true);
        }
    }
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-28上午9:51:03
     *
     * @param refresh
     */
    @SuppressWarnings("all")
    private void requestData(final boolean refresh) {
        RequestParams params = new RequestParams();
        if (refresh) {
            gameNewPageNum = 1;
        }
        this.refreshing = true;
        params.put("page", String.valueOf(gameNewPageNum));
        params.put("count", String.valueOf(Config.PAGE_SIZE));
        params.put("topicid", String.valueOf(this.mSpecialId));
        MyHttpConnect.getInstance().post(HttpContants.NET.GAME_SPECIAL_DETAIL_LIST, params,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                    
                    @Override
					public void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(GameSpecialDetailActivity.this);
						dialog.create().show();
					}

                    public  void onSuccess(int statusCode, String content) {
                    	super.onSuccess(statusCode, content);
                        mPullListView.loadingFinish();
                        if (refresh) {
                            mGameSpecialList.clear();
                            mPullListView.onRefreshFinish();
                        }
                        if (content == null) {
                            if(mPullListView != null){
                                mPullListView.onRefreshFinish();
                                mPullListView.loadingFinish();
                                mPullListView.loadComplete();
                            }
                            return;
                        }
                        try {
                            GameSpecailDetailBean gameSpecailDetail = new ObjectMapper()
                            .configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                            .readValue(content, new TypeReference<GameSpecailDetailBean>() {});
                            if (refresh) {
                            	mSpecailGame.setName(gameSpecailDetail.getData().getName());
                            	mSpecailGame.setTopicdate(gameSpecailDetail.getData().getTopicdate());
                            	mSpecailGame.setPicture(gameSpecailDetail.getData().getPicture());
                            	mSpecailGame.setDesc(gameSpecailDetail.getData().getDesc());
                            	mAdapter.notifyDataSetChanged();
                            }
                            if (gameSpecailDetail.getData().getTopicgms() == null || gameSpecailDetail.getData().getTopicgms().size() < 1) {
                                if(mPullListView != null){
                                    mPullListView.onRefreshFinish();
                                    mPullListView.loadingFinish();
                                    mPullListView.loadComplete();
                                }
                                return;
                            }
                            mGameSpecialList.addAll(gameSpecailDetail.getData().getTopicgms());
                            mPullListView.onRefreshFinish();
                            mPullListView.loadingFinish();
                            if (gameSpecailDetail.getData().getTopicgms().size() < Config.PAGE_SIZE) {
                            	mPullListView.loadComplete();
                            }
                            gameNewPageNum++;
                            mAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            log.e(e);
                            if(mPullListView != null){
                                mPullListView.onRefreshFinish();
                                mPullListView.loadingFinish();
                                mPullListView.loadComplete();
                            }
                        }
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
                        			requestData(refresh);
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
        if (mGameSpecialList == null || mGameSpecialList.isEmpty()) {
            return;
        }
        firstVisiblePosition = mPullListView.getFirstVisiblePosition();
        index = mGameSpecialList.indexOf(item);
        if (index < 0) {
            return;
        }
        view = mPullListView.getChildAt(index - firstVisiblePosition + 1);
        if (null == view) {
            mPullListView.getAdapter().getView(index, view,mPullListView);
        }
        if (view != null) {
            GameSpecailDetailViewCache viewCache = (GameSpecailDetailViewCache) view.getTag();
            if(viewCache==null){
                log.e("视图为空!");
                return;
            }
            if (Util.isInstallByread(item.getIdentifier())) {// 若已安装
                viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.play_btn_xbg);
                viewCache.getBtnDownloadGame().setText(R.string.game_play);
                viewCache.getBtnDownloadGame().setTextColor(GameSpecialDetailActivity.this.getResources().getColor(R.color.white));
            } else {
                if (!DownloadDao.getInstance(this).isHasInfo(item.getIdentifier(),item.getVersion())) {
                    viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                    viewCache.getBtnDownloadGame().setText(R.string.game_download);
                    viewCache.getBtnDownloadGame().setTextColor(GameSpecialDetailActivity.this.getResources().getColor(R.color.white));
                    return;
                }
                DownloadItem downloadItem = DownloadDao.getInstance(this).getDowloadItem(item.getIdentifier(), item.getVersion());
                if (downloadItem == null || TextUtils.isEmpty(downloadItem.getmPackageName())) {// 没下载
                    viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                    viewCache.getBtnDownloadGame().setText(R.string.game_download);
                    viewCache.getBtnDownloadGame().setTextColor(GameSpecialDetailActivity.this.getResources().getColor(R.color.white));
                } else {
                    if (downloadItem.getmState() == DownloadParam.C_STATE.DONE) {
                        viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                        viewCache.getBtnDownloadGame().setText(R.string.download_btn_install);
                        viewCache.getBtnDownloadGame().setTextColor(GameSpecialDetailActivity.this.getResources().getColor(R.color.white));
                    } else {
                        if (downloadItem.getmState() == DownloadParam.C_STATE.DOWNLOADING
                                || downloadItem.getmState() == DownloadParam.C_STATE.WAITING
                                || downloadItem.getmState() == DownloadParam.C_STATE.PAUSE) {
                            viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.downloading_btn_xbg);
                            viewCache.getBtnDownloadGame().setText(R.string.game_downloading);
                            viewCache.getBtnDownloadGame().setTextColor( GameSpecialDetailActivity.this.getResources().getColor(R.color.downloading_text));
                        } else {
                            viewCache.getBtnDownloadGame().setBackgroundResource(R.drawable.download_btn_xbg);
                            viewCache.getBtnDownloadGame().setText(R.string.game_download);
                            viewCache.getBtnDownloadGame().setTextColor(GameSpecialDetailActivity.this.getResources().getColor(R.color.white));
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
        if (null == mGameSpecialList || mGameSpecialList.isEmpty()) {
            return mGameItem;
        }
        for (GameItem item : mGameSpecialList) {
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
        }
    }
}
