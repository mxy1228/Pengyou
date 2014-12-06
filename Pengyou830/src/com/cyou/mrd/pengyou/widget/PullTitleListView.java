package com.cyou.mrd.pengyou.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.cyou.mrd.pengyou.adapter.ContactsAdapter;
import com.cyou.mrd.pengyou.log.CYLog;

public class PullTitleListView extends ListView {

	private CYLog log = CYLog.getInstance();
	
	private View mTitle;
	private int mTitleWidth;
	private int mTitleHeight;
	private boolean visible = true;
	private ContactsAdapter mAdapter;
	private Context mContext;
	
	public PullTitleListView(Context context) {
		super(context);
		this.mContext = context;
	}
	
	public PullTitleListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}
	
	public PullTitleListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}
	
	public void setTitleView(View view,int height,int width){
		this.mTitle = view;
		this.mTitleHeight = height;
		this.mTitleWidth = width;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}
	
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
//		if(mTitle != null){
//			mTitle.layout(0, 0, mTitleWidth, mTitleHeight);
//		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//		if(mTitle != null){
//			measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
//			mTitleWidth = mTitle.getMeasuredWidth();
//			mTitleHeight = mTitle.getMeasuredHeight();
//		}
	}
	
	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if(visible){
			drawChild(canvas, mTitle, getDrawingTime());
		}
	}
	
	public void titleLayout(int firstVisiblePosition){
		if(mTitle == null){
			return;
		}
		if(mAdapter == null || !(mAdapter instanceof ContactsAdapter)){
			return;
		}
		int state = mAdapter.getTitleState(firstVisiblePosition);
		log.d("state = "+state);
		switch (state) {
		case 0:
			visible = true;
			break;
		case 1:
			if(mTitle.getTop() != 0){
				mTitle.layout(0, 0, mTitleWidth, mTitleHeight);
			}
			mAdapter.setTitleText(mTitle, firstVisiblePosition);
			visible = true;
			break;
		case 2:
			View firstView = getChildAt(0);
			if(firstView != null){
				int bottom = firstView.getBottom();
				int headerHeight = mTitleHeight;
				int top;
				if(bottom < headerHeight){
					top = (bottom - headerHeight);
				}else{
					top = 0;
				}
				mAdapter.setTitleText(mTitle, firstVisiblePosition);
				if(mTitle.getTop() != top){
					log.d("top = "+top);
					mTitle.layout(0, top, mTitleWidth, mTitleHeight + top);
				}
				visible = true;
			}
			break;
		default:
			break;
		}
	}
	
	@Override
	public void setAdapter(ListAdapter adapter) {
		if(adapter instanceof ContactsAdapter){
			mAdapter = (ContactsAdapter)adapter;
			super.setAdapter(adapter);
		}
	}
}
