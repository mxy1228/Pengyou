/**
 * tuozhonghua_zk
 * 2013-10-16
 * TODO
 */
package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.RecommendedGameAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
import com.cyou.mrd.pengyou.log.BehaviorInfo;
import com.cyou.mrd.pengyou.log.CYSystemLogUtil;
import com.cyou.mrd.pengyou.utils.Util;
import com.cyou.mrd.pengyou.widget.LoginOutDialog;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.LoadListener;
import com.cyou.mrd.pengyou.widget.pull2refresh.PullToRefreshListView.RefreshListener;
import com.loopj.android.http.RequestParams;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author tuozhonghua_zk
 *
 */
public class ReCommendedGameActivity extends CYBaseActivity implements OnClickListener {
    
    private ImageButton imgBtnSearch;
    private ImageButton mBackBtn;
    
    private PullToRefreshListView mPullListView;
    
    private boolean refreshing = false;
    private int gameNewPageNum = 1;
    
    private List<GameItem> mRecommendGameList;
    private RecommendedGameAdapter mRecommendGameAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BehaviorInfo behaviorInfo = new BehaviorInfo(
				CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_ID,
				CYSystemLogUtil.GAMESTORE.BTN_NICERECOMMEND_MOREGOODGAME_NAME);
		CYSystemLogUtil.behaviorLog(behaviorInfo);
        setContentView(R.layout.recommended_game_layout);
        mContext = this;
        log.i("density= " + this.mDensity +"width=" + this.mScreenWidth + "height=" + this.mScreenHeight);
        initView();
        initEvent();
        initData();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void initView() {
        View headerBar = findViewById(R.id.layout_headerbar);
        mBackBtn = (ImageButton) headerBar.findViewById(R.id.header_bar_left_ibtn);
        mBackBtn.setBackgroundResource(R.drawable.back_btn_xbg);
        mBackBtn.setVisibility(View.VISIBLE);
        headerBar.findViewById(R.id.header_feedback_btn).setVisibility(View.GONE);
        TextView mHeaderTV = (TextView) headerBar.findViewById(R.id.header_tv);
        mHeaderTV.setText(R.string.recommend_good_title);
        imgBtnSearch = (ImageButton) findViewById(R.id.header_search_btn);
        imgBtnSearch.setVisibility(View.GONE);
        mPullListView = (PullToRefreshListView) findViewById(R.id.recommended_game_listview);
        mPullListView.setDivider(null);
        mPullListView.setDividerHeight(0);
        mPullListView.setOnRefreshListener(new RefreshListener() {

            @Override
            public void onRefresh() {
                if(Util.isNetworkConnected(ReCommendedGameActivity.this)){
                    requestRecommendedGameData(true);
                }else{
                    Util.showToast(ReCommendedGameActivity.this, ReCommendedGameActivity.this.getString(R.string.check_network), Toast.LENGTH_SHORT);
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
            public void onScroll(AbsListView view, int firstVisibleItem,
                    int visibleItemCount, int totalItemCount) {
            }

            @Override
            public void onLoad() {
                requestRecommendedGameData(false);
            }
        });
    }

    @Override
    protected void initEvent() {
        mBackBtn.setOnClickListener(this);
        imgBtnSearch.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        if (mRecommendGameList == null) {
            mRecommendGameList = new ArrayList<GameItem>();
        }
        if (mRecommendGameAdapter == null) {
            mRecommendGameAdapter = new RecommendedGameAdapter(ReCommendedGameActivity.this, mRecommendGameList);
        }
        if (mPullListView != null) {
            mPullListView.setAdapter(mRecommendGameAdapter);
        }
        requestRecommendedGameData(true);
    }
    
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:28
     *
     * @param refresh
     */
    @SuppressWarnings("all")
    private void requestRecommendedGameData(final boolean refresh) {
        RequestParams params = new RequestParams();
        if (refresh) {
            gameNewPageNum = 1;
        }
        this.refreshing = true;
        params.put("withpic", "1");//新加参数
        params.put("page", String.valueOf(gameNewPageNum));
        params.put("count", String.valueOf(Config.PAGE_SIZE));
        MyHttpConnect.getInstance().post(HttpContants.NET.RECOM_GAME, params,
                new JSONAsyncHttpResponseHandler(JSONAsyncHttpResponseHandler.RESULT_LIST,GameItem.class) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }
                    @Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								ReCommendedGameActivity.this);
						dialog.create().show();
						super.onLoginOut();
					}
                    public void onSuccessForList(List list) {
                        if (refresh) {
                            mRecommendGameList.clear();
                            mPullListView.onRefreshFinish();
                        }
                        mPullListView.loadingFinish();
                        if (null == list || list.size() == 0) {
                            mPullListView.loadComplete();
                            return;
                        }
                        mPullListView.onRefreshFinish();
                        mPullListView.loadingFinish();
                        if (list.size() < Config.PAGE_SIZE) {
                            mPullListView.loadComplete();
                        }
                        mRecommendGameList.addAll(list);
                        gameNewPageNum++;
                        mRecommendGameAdapter.notifyDataSetChanged();
                        super.onSuccessForList(list);
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
                        refreshing = false;
                        if (mContext != null) {
                        	showNetErrorDialog(mContext,new ReConnectListener() {
                        		
                        		@Override
                        		public void onReconnect() {
                        			requestRecommendedGameData(refresh);
                        		}
                        	});
                        }
                        if(mPullListView != null){
                              mPullListView.onRefreshFinish();
                              mPullListView.loadingFinish();
                              mPullListView.loadComplete();
                        }
                        super.onFailure(error, content);
                    }
                });
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
        }
    }

}
