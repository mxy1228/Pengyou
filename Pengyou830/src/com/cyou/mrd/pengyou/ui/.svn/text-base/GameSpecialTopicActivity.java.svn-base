/**
 * tuozhonghua_zk
 * 2013-10-16
 * TODO
 */
package com.cyou.mrd.pengyou.ui;

import java.util.ArrayList;
import java.util.List;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.adapter.GameSpecialTopicAdapter;
import com.cyou.mrd.pengyou.config.Config;
import com.cyou.mrd.pengyou.config.Params;
import com.cyou.mrd.pengyou.entity.GameItem;
import com.cyou.mrd.pengyou.entity.SeminarBean;
import com.cyou.mrd.pengyou.http.HttpContants;
import com.cyou.mrd.pengyou.http.JSONAsyncHttpResponseHandler;
import com.cyou.mrd.pengyou.http.MyHttpConnect;
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
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @author tuozhonghua_zk
 *
 */
public class GameSpecialTopicActivity extends CYBaseActivity implements OnClickListener {
    
    private ImageButton imgBtnSearch;
    private ImageButton mBackBtn;
    
    private PullToRefreshListView mPullListView;
    
    private boolean refreshing = false;
    private int gameNewPageNum = 1;
    
    private List<SeminarBean> mGameSpecialList;
    private GameSpecialTopicAdapter mAdapter;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_special_layout);
        mContext = this;
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
        mHeaderTV.setText(R.string.game_special_title);
        imgBtnSearch = (ImageButton) findViewById(R.id.header_search_btn);
        imgBtnSearch.setVisibility(View.GONE);
        
        mPullListView = (PullToRefreshListView) findViewById(R.id.game_special_listview);
        //取消分割线
        mPullListView.setDivider(null);
        mPullListView.setDividerHeight(0);
        mPullListView.setOnRefreshListener(new RefreshListener() {

            @Override
            public void onRefresh() {
                if(Util.isNetworkConnected(GameSpecialTopicActivity.this)){
                    requestData(true);
                }else{
                    Toast.makeText(GameSpecialTopicActivity.this, GameSpecialTopicActivity.this.getString(R.string.check_network), Toast.LENGTH_SHORT).show();
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
        mPullListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
                if (mGameSpecialList == null || mGameSpecialList.size() == 0 || position >= mGameSpecialList.size()) {
                    return;
                }
                SeminarBean item = mGameSpecialList.get(position);
                if (null == item) {
                    return;
                }
                Intent mIntent = new Intent();
                mIntent.setClass(GameSpecialTopicActivity.this, GameSpecialDetailActivity.class);
                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_ID, item.getId());
//                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_NAME, item.getName());
//                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_DATE, item.getTopicdate());
//                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_IMAGE, item.getPicture());
//                mIntent.putExtra(Params.GAME_SPECIAL.SPECIAL_DESC, item.getDesc());
                startActivity(mIntent);
            }
        });
    }

    @Override
    protected void initData() {
        if (mGameSpecialList == null) {
            mGameSpecialList = new ArrayList<SeminarBean>();
        }
        if (mAdapter == null) {
            mAdapter = new GameSpecialTopicAdapter(GameSpecialTopicActivity.this, mGameSpecialList);
        }
        if (mPullListView != null) {
            mPullListView.setAdapter(mAdapter);
        }
        requestData(true);
    }
    
    /**
     * 
     * tuozhonghua_zk
     * 2013-10-16上午11:21:28
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
        MyHttpConnect.getInstance().post(HttpContants.NET.GAME_SPECIAL_DETAIL_LIST, params,
                new JSONAsyncHttpResponseHandler(JSONAsyncHttpResponseHandler.RESULT_LIST,SeminarBean.class) {
                    @Override
                    public void onStart() {
                        super.onStart();
                    }

                    public void onSuccessForList(List list) {
                        if (refresh) {
                            mGameSpecialList.clear();
                            mPullListView.onRefreshFinish();
                        }
                        mPullListView.loadingFinish();
                        if (null == list || list.size() == 0) {
                            mPullListView.loadComplete();
                            return;
                        }
                        if (list.size() < Config.PAGE_SIZE) {
                            mPullListView.loadComplete();
                        }
                        mGameSpecialList.addAll(list);
                        gameNewPageNum++;
                        mAdapter.notifyDataSetChanged();
                        super.onSuccessForList(list);
                    }
                    @Override
					protected void onLoginOut() {
						LoginOutDialog dialog = new LoginOutDialog(
								GameSpecialTopicActivity.this);
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
                        			requestData(refresh);
                        		}
                        	});
                        	
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
