package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.cyou.mrd.pengyou.R;
import com.cyou.mrd.pengyou.log.CYLog;

public class ChatListView extends ListView implements OnScrollListener{

	private CYLog log = CYLog.getInstance();
	
	private LoadListener mListener;
	private boolean mLoadingMore;
	private boolean mLoadComplete;
	
	private View mHeaderView;
	private LinearLayout mHeaderViewLL;
	
	public ChatListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	public ChatListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	
	public ChatListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init(){
		this.setOnScrollListener(this);
		this.mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.pull_fresh_footview, this,false);
		this.mHeaderViewLL = (LinearLayout)mHeaderView.findViewById(R.id.pull_refresh_footview_ll);
		addHeaderView(mHeaderView);
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if(mListener != null){
			mListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		log.d("getFirstVisiblePosition()="+getFirstVisiblePosition());
		if(mListener != null){
			mListener.onScrollStateChanged(view, scrollState);
		}
		if(getFirstVisiblePosition() == 0){
			if(mListener != null && !mLoadingMore && !mLoadComplete){
				log.d("onLoad");
				mLoadingMore = true;
				mListener.onLoad();
			}
		}
	}
	
	/**
	 * 一页加载更多完成后调用
	 * xumengyang
	 */
	public void loadingFinish(){
		this.mLoadingMore = false;
	}
	
	/**
	 * 所有数据加载完后调用
	 * xumengyang
	 */
	public void loadComplete(){
		this.mLoadComplete = true;
		removeHeaderView(mHeaderView);
	}

	public interface LoadListener{
		public void onLoad();
		public void onScrollStateChanged(AbsListView view, int scrollState);
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount);
	}
	
	public void setOnLoadListener(LoadListener listener){
		this.mListener = listener;
	}
}
